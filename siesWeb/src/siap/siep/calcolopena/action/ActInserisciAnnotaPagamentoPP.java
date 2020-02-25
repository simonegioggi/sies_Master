package siap.siep.calcolopena.action;

/**
 * <p>Title: ActInserisciRidetPenaAltro</p>
 * <p>Description: Azione nella Converisione delle Pene Pecuniarie</p>
 * <p>MicroFunzione: Inserimento Annotazione avvenuto pagamento Pene Pecuniarie</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciAnnotaPagamentoPP extends ActRidetPena {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {
		isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

		// ==========================================================================
		// Recupero i dati del Provvedimento di Computo
		// ==========================================================================
		EventoModel lEveProvvedimentoMod = new EventoModel();

		lEveProvvedimentoMod.setCodTipoEvento("01");
		lEveProvvedimentoMod.setCodMotivo("1007"); // Nuovo --> Annotazione Avvenuto Pagamento PP
		lEveProvvedimentoMod.setCodTipoProvvedimento("25"); // 25 - Annotazione

		lEveProvvedimentoMod.setFlagDocumentoRegistrato(null);
		lEveProvvedimentoMod.setFlagStampaSiep("S");
		lEveProvvedimentoMod.setFlagVideoSiep("S");

		lEveProvvedimentoMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
		lEveProvvedimentoMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
		lEveProvvedimentoMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		lEveProvvedimentoMod.setDataEmissione(getRequestDateParameter(
				ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		lEveProvvedimentoMod.setCodEsito("-");
		lEveProvvedimentoMod.setCodTipoUfficioDestinatario("-");
		lEveProvvedimentoMod.setCodLuogoDestinatario("-");

		lEveProvvedimentoMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveProvvedimentoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveProvvedimentoMod.setDataInserimento(DateUtils.getSysDate());

		// lEveMod.setCodMagistrato(lMagistrato);
		// Gestisco l'inserimento del firmatario
		if (!isRequestParameterNullObj("TipoFir")) {
			if (getRequestStringParameter("TipoFir").equalsIgnoreCase("magistrato")) {
				lEveProvvedimentoMod.setCodMagistrato(
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			} else if (getRequestStringParameter("TipoFir").equalsIgnoreCase("funzionario")) {
				lEveProvvedimentoMod.setNomeSoggettoPresentante(getRequestStringParameter("NomeFunzionario"));
				lEveProvvedimentoMod
						.setCognomeSoggettoPresentante(getRequestStringParameter("CognomeFunzionario"));
				lEveProvvedimentoMod.setCodMagistrato("-");
			}
		} else {
			lEveProvvedimentoMod
					.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(lEveProvvedimentoMod);

		int Conta = 0;
		// -------------------------------------------- Campo Nota --------------------------------------
		Vector lListaCampoNota = new Vector();
		// Note
		CampoNotaModel lCampoNota = new CampoNotaModel();

		lCampoNota.setFasSieIdFascicoloSiep(lIdFascicolo);
		lCampoNota.setOggettoNotaRes("NOTE");
		if (getRequestStringParameter("noteComputo") != null
				&& !getRequestStringParameter("noteComputo").equals("")) {
			lCampoNota.setDescr(getRequestStringParameter("noteComputo"));
		} else {
			lCampoNota.setDescr("");
		}
		Conta++;
		lCampoNota.setProgressivo(new BigDecimal(Conta));

		lCampoNota.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lCampoNota.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lCampoNota.setDataInserimento(DateUtils.getSysDate());

		lListaCampoNota.add(lCampoNota);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lCampoNota " + Conta + " = " + lCampoNota);

		// Num EX CAMPIONE Penale viene messo in descr cmpo note passando per
		// (ICostantiEvento.CAMPO_NOME_SOGGETTO_PRESENTANTE)

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_NOME_SOGGETTO_PRESENTANTE)
				&& !getRequestStringParameter(ICostantiEvento.CAMPO_NOME_SOGGETTO_PRESENTANTE).equals("")) {
			lCampoNota = new CampoNotaModel();
			lCampoNota.setFasSieIdFascicoloSiep(lIdFascicolo);
			lCampoNota.setDescr(getRequestStringParameter(ICostantiEvento.CAMPO_NOME_SOGGETTO_PRESENTANTE));
			lCampoNota.setOggettoNotaRes("EXCAMPIONE");
			Conta++;
			lCampoNota.setProgressivo(new BigDecimal(Conta));

			lCampoNota.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lCampoNota.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lCampoNota.setDataInserimento(DateUtils.getSysDate());

			lListaCampoNota.add(lCampoNota);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lCampoNota " + Conta + " = " + lCampoNota);
		}

		// SEZIONE della sede aut viene messo in descr cmpo note passando per
		// (ICostantiEvento.CAMPO_COGNOME_SOGGETTO_PRESENTANTE)

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COGNOME_SOGGETTO_PRESENTANTE)
				&& !getRequestStringParameter(ICostantiEvento.CAMPO_COGNOME_SOGGETTO_PRESENTANTE)
						.equals("")) {
			lCampoNota = new CampoNotaModel();
			lCampoNota.setFasSieIdFascicoloSiep(lIdFascicolo);
			lCampoNota
					.setDescr(getRequestStringParameter(ICostantiEvento.CAMPO_COGNOME_SOGGETTO_PRESENTANTE));
			lCampoNota.setOggettoNotaRes("SEZIONE");
			Conta++;
			lCampoNota.setProgressivo(new BigDecimal(Conta));

			lCampoNota.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lCampoNota.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lCampoNota.setDataInserimento(DateUtils.getSysDate());

			lListaCampoNota.add(lCampoNota);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lCampoNota " + Conta + " = " + lCampoNota);
		}

		// ==========================================================================
		// Recupero i dati delle annotazioni manuali/liberazione anticipata
		// ==========================================================================
		Vector lListaAnnotazioni = new Vector();

		if (!this.isRequestParameterNullObj("maxNumComputi")) {
			int maxNumComputi = getRequestIntParameter("maxNumComputi");
			for (int i = 0; i < maxNumComputi; i++) {
				if (!isRequestParameterNullObj("PM_" + i)) {
					if (!getRequestStringParameter("PM_" + i).equals("")) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Parametro PM_" + i + " impostato");
						AnnotazioneManualeModel lAnnMan = getComputo(i);
						lListaAnnotazioni.add(lAnnMan);
					} else {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Parametro PM_" + i + " non selezionato nella form");
					}
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Parametro PM_" + i + " assente nella form");
				}
			}

			// Solo per il debug
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lListaAnnotazioni.size() = " + lListaAnnotazioni.size());
			for (int i = 0; i < lListaAnnotazioni.size(); i++) {
				AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lListaAnnotazioni.elementAt(i);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(lAnnMod.toString2());
			}
		}

		// ==========================================================================
		// Recupero i dati del Provvedimento Altra Autorità se indicata
		// n.b. è a tutti gli effetti un evento
		// ==========================================================================
		EventoModel lEveAltroUff = null;
		if (getRequestStringParameter("TipoOrd").equals("altroUfficio")) {
			lEveAltroUff = new EventoModel();

			lEveAltroUff.setFasSieIdFascicoloSiep(lIdFascicolo);

			lEveAltroUff.setCodTipoEvento("01");
			lEveAltroUff.setCodTipoProvvedimento("12"); // Comunicazione Altro Uff
			lEveAltroUff.setCodMotivo("1007"); // Nuovo --> Annotazione Avvenuto Pagamento PP

			lEveAltroUff.setFlagDocumentoRegistrato("S"); // Per ora lo inserisco validato
			lEveAltroUff.setFlagStampaSiep("N");
			lEveAltroUff.setFlagVideoSiep("N");

			// Autorità emittente
			String lCodTipoUff = getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE);
			String lDescrComune = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lDescrComune        = " + lDescrComune);

			ComuneModel lComune = this.getCodComuneByDescr(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE));
			String lCodLuogoEmittente = lComune.getCodComune();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lCodTipoUff        = " + lCodTipoUff);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lCodLuogoEmittente = " + lCodLuogoEmittente);

			String lCodUffEmittente = this.getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff,
					lDescrComune);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lCodUffEmittente   = " + lCodUffEmittente);

			lEveAltroUff.setCodUfficioEmittente(lCodUffEmittente);
			lEveAltroUff.setCodLuogoEmittente(lCodLuogoEmittente);

			lEveAltroUff.setDataEmissione(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE + "_AA"));
			lEveAltroUff.setDataRicezioneAtti(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI,
							ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI,
							ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI));

			// AMBROSINO 04/2011
			// Partita di credito ( ANNO e PROGR_PROTOCOLLO)
			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO))
				lEveAltroUff.setAnnoProtocollo(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO));

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO))
				lEveAltroUff.setProgrProtocollo(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO));

			lEveAltroUff.setCodEsito("-");
			lEveAltroUff.setCodTipoUfficioDestinatario("-");
			lEveAltroUff.setCodLuogoDestinatario("-");

			lEveAltroUff.setCodOperatoreInserimento(getCodUtenteConnesso());
			lEveAltroUff.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lEveAltroUff.setDataInserimento(DateUtils.moveDateTo(lEveProvvedimentoMod.getDataInserimento(),
					java.util.Calendar.SECOND, -1));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Provvedimento Altra Autorità = " + lEveAltroUff);

		}

		// if (1==12){
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserimento Effettuato Correttamente!");
		// return IWebConstants.PG_MESSAGE;
		// }

		// ==========================================================================
		// Inserimento dei dati
		// ==========================================================================
		IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
		EventoModel lEventoIns = null;
		lEventoIns = lAnnManCtrl.ExInserisciEventoAnnotazioniCampoNota(lEveProvvedimentoMod,
				lListaAnnotazioni, lListaCampoNota, lEveAltroUff);
		// lEventoIns = lAnnManCtrl.ExInserisciEventoAnnotazioni(lEveProvvedimentoMod,
		// lListaAnnotazioni,lCampoNota,lEveAltroUff,lLicModel);
		// ==========================================================================
		// Restituisce la pagina di dettaglio
		// ==========================================================================
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotaPagamentoPP&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEventoIns.getIdEvento();
		return lPage;
	}

	/**
	 * Recupera dalla form i dati dei quantum di computo relativi all'id passato in input
	 * 
	 * @param id_computo
	 * @return
	 * @throws F3BException
	 */
	private AnnotazioneManualeModel getComputo(int id_computo) throws F3BException {
		FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

		// ==========================================================================
		// Recupero dalla form i dati dell'annotazione manuale da inserire
		// ==========================================================================
		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();

		lAnnMod.setCodTipoAnnotazione("014"); // altro
		lAnnMod.setFlagPiuMeno(getRequestStringParameter("PM_" + id_computo));
		lAnnMod.setFlagConforme("-");
		lAnnMod.setFlagValidato("N");
		lAnnMod.setCodFonte("-");
		lAnnMod.setCodSottonumerazione("-");
		lAnnMod.setCodCausaleComputo("-");
		lAnnMod.setCodDpr("-");
		lAnnMod.setFlagAppProvvisoria("-");
		lAnnMod.setMotivazioni(getRequestStringParameter("motivazioni_" + id_computo));

		lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAnnMod.setDataInserimento(DateUtils.getSysDate());

		// ===========================================
		// Multa
		// ===========================================

		String Multa = getRequestStringParameter("Multa_" + id_computo);
		String Multa_dec = getRequestStringParameter("Mul_dec_" + id_computo);

		if (!Multa.equals("")) {
			if (!Multa_dec.equals(""))
				lAnnMod.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
			else
				lAnnMod.setImportoMulta(new BigDecimal(Multa));
		} else if (!Multa_dec.equals(""))
			lAnnMod.setImportoMulta(new BigDecimal("0." + Multa_dec));

		// ===========================================
		// Ammenda
		// ===========================================

		String Ammenda = getRequestStringParameter("Ammenda_" + id_computo);
		String Ammenda_dec = getRequestStringParameter("Amm_dec_" + id_computo);

		if (!Ammenda.equals("")) {
			if (!Ammenda_dec.equals(""))
				lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
			else
				lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda));
		} else if (!Ammenda_dec.equals(""))
			lAnnMod.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));

		return lAnnMod;
	}

}