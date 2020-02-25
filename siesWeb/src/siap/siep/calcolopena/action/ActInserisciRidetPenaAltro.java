package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.CalendarUtil;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.calcolopena.controller.ICalcoloPenaF5;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciRidetPenaAltro
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di rideterminazione pena altro
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciRidetPenaAltro extends ActRidetPena {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Nuova versione "Rideterminazione pena - Altro"
	 * 
	 * Effettua l'inserimento del provvedimento di rideterminazione pena per un motivo generico. menu:
	 * 'Rideterminazione Pena - Provvedimenti del PM - Altro'
	 * 
	 * Non viene più effettuato il calcolo della pena in questa fase ma solo l'annotazione del computo
	 * (01-25-xxxx) e delle Annotazioni Manuali che da questa versione possono essere più di una.
	 * 
	 * Inserisce: - Annotazione (01-25) - n Annotazioni Manuali
	 * 
	 * n.b. non sono previsti destinatari
	 * 
	 * @return la pagina di dettaglio
	 */
	public String processRequest() throws Exception {

		isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

		// ==========================================================================
		// Recupero i dati del Provvedimento di Computo
		// ==========================================================================
		EventoModel lEveProvvedimentoMod = new EventoModel();

		lEveProvvedimentoMod.setCodTipoEvento("01");
		lEveProvvedimentoMod.setCodTipoProvvedimento("25"); // 25 - Annotazione
		if (getRequestStringParameter("TipoOrd").equals("dufficio")) {
			lEveProvvedimentoMod.setCodMotivo(this
					.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
		} else {
			lEveProvvedimentoMod.setCodMotivo(this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO
					+ "_AA"));
		}
		lEveProvvedimentoMod.setFlagDocumentoRegistrato(null);
		lEveProvvedimentoMod.setFlagStampaSiep("S");
		lEveProvvedimentoMod.setFlagVideoSiep("S");

		lEveProvvedimentoMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
		lEveProvvedimentoMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
		lEveProvvedimentoMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		lEveProvvedimentoMod.setDataEmissione(getRequestDateParameter(
				ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		// lEveProvvedimentoMod.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
		// ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
		// ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		lEveProvvedimentoMod
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		lEveProvvedimentoMod.setCodEsito("-");
		lEveProvvedimentoMod.setCodTipoUfficioDestinatario("-");
		lEveProvvedimentoMod.setCodLuogoDestinatario("-");

		lEveProvvedimentoMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveProvvedimentoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveProvvedimentoMod.setDataInserimento(DateUtils.getSysDate());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(lEveProvvedimentoMod);

		// ===========================================================
		// Recupero le Note del Provvedimento (se presenti)
		// ===========================================================
		// EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		// lEveNotMod.setEvento(lEveProvvedimentoMod);
		CampoNotaModel lCampoNota = new CampoNotaModel();

		lCampoNota.setFasSieIdFascicoloSiep(lIdFascicolo);

		if (getRequestStringParameter("noteComputo") != null
				&& !getRequestStringParameter("noteComputo").equals(""))
			lCampoNota.setDescr(getRequestStringParameter("noteComputo"));
		else
			lCampoNota.setDescr("");

		lCampoNota.setProgressivo(new BigDecimal(1));

		lCampoNota.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lCampoNota.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lCampoNota.setDataInserimento(DateUtils.getSysDate());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lCampoNota = " + lCampoNota);

		// ==========================================================================
		// Recupero i dati delle annotazioni manuali/liberazione anticipata
		// ==========================================================================
		Vector lListaAnnotazioni = new Vector();
		LicenzaLibAnticipataModel lLicModel = null;

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
		} else {
			// ================================================================
			// Trattasi di computi della Sorveglianza, devo scriverlo su LA
			// n.b. Per Ora non operante, le dec sorveglianza verrano spostate in
			// altra funzione
			// ================================================================
			lLicModel = new LicenzaLibAnticipataModel();

			if (lEveProvvedimentoMod.getCodMotivo().equals("0957")) {
				// Ridimensionamento LA
				lLicModel.setCodTipoLicenza("LA");
			} else {
				// Scomputo Permesso (0958)
				lLicModel.setCodTipoLicenza("PP");
			}
			lLicModel.setNumeroGiorni(getRequestBigDecimalParameter("GiorniComputo"));
			lLicModel.setFlagConcesso("S"); // Scomputato
			// lLicModel.setFlagElaborato(aValore)
			// lLicModel.setFlagScomputo(aValore)
			// lLicModel.setFlagScorta(aValore)

			lLicModel.setFasSieIdFascicoloSiep(lIdFascicolo);
			// lLicModel.setEveIdEvento(aValore); valorizzato dal Ctrl

			// lLicModel.setAnnotazione(aValore);

			lLicModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lLicModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lLicModel.setDataInserimento(DateUtils.getSysDate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lLicModel = " + lLicModel);
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
			lEveAltroUff
					.setCodTipoProvvedimento(getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO
							+ "_AA"));
			lEveAltroUff.setCodMotivo(this
					.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO + "_AA"));

			// MEV29 - inserisco l'evento non validato
			lEveAltroUff.setFlagDocumentoRegistrato("N");
			// lEveAltroUff.setFlagDocumentoRegistrato("S"); // Per ora lo inserisco validato
			lEveAltroUff.setFlagStampaSiep("S");
			lEveAltroUff.setFlagVideoSiep("S");

			// Autorità emittente
			String lCodTipoUff = getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE
					+ "_AA");
			String lDescrComune = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA");

			ComuneModel lComune = this
					.getCodComuneByDescr(getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE
							+ "_AA"));
			String lCodLuogoEmittente = lComune.getCodComune();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lCodTipoUff        = " + lCodTipoUff);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lCodLuogoEmittente = " + lCodLuogoEmittente);
			String lCodUffEmittente = this
					.getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff, lDescrComune);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lCodUffEmittente   = " + lCodUffEmittente);

			lEveAltroUff.setCodUfficioEmittente(lCodUffEmittente);
			lEveAltroUff.setCodLuogoEmittente(lCodLuogoEmittente);

			lEveAltroUff.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE
					+ "_AA", ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE + "_AA",
					ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE + "_AA"));
			lEveAltroUff.setDataRicezioneAtti(getRequestDateParameter(
					ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI + "_AA",
					ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI + "_AA",
					ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI + "_AA"));

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"))
				lEveAltroUff
						.setAnnoProtocollo(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO
								+ "_AA"));

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"))
				lEveAltroUff
						.setProgrProtocollo(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO
								+ "_AA"));

			//
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
		lEventoIns = lAnnManCtrl.ExInserisciEventoAnnotazioni(lEveProvvedimentoMod, lListaAnnotazioni,
				lCampoNota, lEveAltroUff, lLicModel);

		// ==========================================================================
		// Restituisce la pagina di dettaglio
		// ==========================================================================
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.calcolopena.action.ActLoadDettaglioRidetPenaNew&"
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
		// reclusione
		// ===========================================
		String GRec = getRequestStringParameter("GRec_" + id_computo);
		String MRec = getRequestStringParameter("MRec_" + id_computo);
		String ARec = getRequestStringParameter("ARec_" + id_computo);
		String Multa = getRequestStringParameter("Multa_" + id_computo);
		String Multa_dec = getRequestStringParameter("Mul_dec_" + id_computo);

		if (!ARec.equals(""))
			lAnnMod.setNumAnniReclusione(new BigDecimal(ARec));
		else
			lAnnMod.setNumAnniReclusione(new BigDecimal(0));

		if (!MRec.equals(""))
			lAnnMod.setNumMesiReclusione(new BigDecimal(MRec));
		else
			lAnnMod.setNumMesiReclusione(new BigDecimal(0));

		if (!GRec.equals(""))
			lAnnMod.setNumGiorniReclusione(new BigDecimal(GRec));
		else
			lAnnMod.setNumGiorniReclusione(new BigDecimal(0));

		if (!Multa.equals("")) {
			if (!Multa_dec.equals("")) {
				lAnnMod.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
			} else
				lAnnMod.setImportoMulta(new BigDecimal(Multa));
		} else if (!Multa_dec.equals(""))
			lAnnMod.setImportoMulta(new BigDecimal("0." + Multa_dec));

		// ===========================================
		// arresto
		// ===========================================
		String GArr = getRequestStringParameter("GArr_" + id_computo);
		String MArr = getRequestStringParameter("MArr_" + id_computo);
		String AArr = getRequestStringParameter("AArr_" + id_computo);
		String Ammenda = getRequestStringParameter("Ammenda_" + id_computo);
		String Ammenda_dec = getRequestStringParameter("Amm_dec_" + id_computo);

		if (!AArr.equals(""))
			lAnnMod.setNumAnniArresto(new BigDecimal(AArr));
		else
			lAnnMod.setNumAnniArresto(new BigDecimal(0));

		if (!MArr.equals(""))
			lAnnMod.setNumMesiArresto(new BigDecimal(MArr));
		else
			lAnnMod.setNumMesiArresto(new BigDecimal(0));

		if (!GArr.equals(""))
			lAnnMod.setNumGiorniArresto(new BigDecimal(GArr));
		else
			lAnnMod.setNumGiorniArresto(new BigDecimal(0));

		if (!Ammenda.equals("")) {
			if (!Ammenda_dec.equals("")) {
				lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
			} else
				lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda));
		} else if (!Ammenda_dec.equals(""))
			lAnnMod.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));

		return lAnnMod;
	}

	/**
	 * Effettua l'inserimento del provvedimento di rideterminazione pena per un motivo generico. menu:
	 * 'Rideterminazione Pena - Provvedimenti del PM - Altro' Viene effettuato anche il calcolo e inserimento
	 * della Pena e Fungibilità
	 * 
	 * Inserisce: - Annotazione Manuale - Provvedimento (01-04) o Comunicazione (01-12) in funzione della
	 * Causale - Pena Residua - Fungibilità
	 * 
	 * @return la pagina di dettaglio
	 */
	public String processRequestAttuale() throws Exception {

		isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

		// ==========================================================================
		// Recupero dalla form i dati dell'annotazione manuale da inserire
		// ==========================================================================
		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();

		lAnnMod.setCodTipoAnnotazione("014"); // altro
		lAnnMod.setFlagPiuMeno(getRequestStringParameter("PM"));
		lAnnMod.setFlagConforme("-");
		lAnnMod.setFlagValidato("N");
		lAnnMod.setCodFonte("-");
		lAnnMod.setCodSottonumerazione("-");
		lAnnMod.setCodCausaleComputo("-");
		lAnnMod.setCodDpr("-");
		lAnnMod.setFlagAppProvvisoria("-");
		lAnnMod.setMotivazioni(getRequestStringParameter("motivazioni"));

		lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAnnMod.setDataInserimento(DateUtils.getSysDate());

		// ===========================================
		// reclusione
		// ===========================================
		String GRec = getRequestStringParameter("GRec");
		String MRec = getRequestStringParameter("MRec");
		String ARec = getRequestStringParameter("ARec");
		String Multa = getRequestStringParameter("Multa");
		String Multa_dec = getRequestStringParameter("Mul_dec");

		if (!ARec.equals(""))
			lAnnMod.setNumAnniReclusione(new BigDecimal(ARec));
		else
			lAnnMod.setNumAnniReclusione(new BigDecimal(0));

		if (!MRec.equals(""))
			lAnnMod.setNumMesiReclusione(new BigDecimal(MRec));
		else
			lAnnMod.setNumMesiReclusione(new BigDecimal(0));

		if (!GRec.equals(""))
			lAnnMod.setNumGiorniReclusione(new BigDecimal(GRec));
		else
			lAnnMod.setNumGiorniReclusione(new BigDecimal(0));

		if (!Multa.equals("")) {
			if (!Multa_dec.equals("")) {
				lAnnMod.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
			} else
				lAnnMod.setImportoMulta(new BigDecimal(Multa));
		} else if (!Multa_dec.equals(""))
			lAnnMod.setImportoMulta(new BigDecimal("0." + Multa_dec));

		// ===========================================
		// arresto
		// ===========================================
		String GArr = getRequestStringParameter("GArr");
		String MArr = getRequestStringParameter("MArr");
		String AArr = getRequestStringParameter("AArr");
		String Ammenda = getRequestStringParameter("Ammenda");
		String Ammenda_dec = getRequestStringParameter("Amm_dec");

		if (!AArr.equals(""))
			lAnnMod.setNumAnniArresto(new BigDecimal(AArr));
		else
			lAnnMod.setNumAnniArresto(new BigDecimal(0));

		if (!MArr.equals(""))
			lAnnMod.setNumMesiArresto(new BigDecimal(MArr));
		else
			lAnnMod.setNumMesiArresto(new BigDecimal(0));

		if (!GArr.equals(""))
			lAnnMod.setNumGiorniArresto(new BigDecimal(GArr));
		else
			lAnnMod.setNumGiorniArresto(new BigDecimal(0));

		if (!Ammenda.equals("")) {
			if (!Ammenda_dec.equals("")) {
				lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
			} else
				lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda));
		} else if (!Ammenda_dec.equals(""))
			lAnnMod.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));

		// ==========================================================================
		// Evento Provvedimento/Comunicazione
		// ==========================================================================
		EventoModel lEveProvvedimentoMod = new EventoModel();

		lEveProvvedimentoMod.setCodTipoEvento("01");
		// modifica 17-06-05 --Dario --Luciana
		// lEveProvvedimentoMod.setCodTipoProvvedimento("04");
		// Cambiata la codifica del Tipo Provvedimento. Luigi 14-10-2005
		lEveProvvedimentoMod.setCodMotivo(this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
		lEveProvvedimentoMod.setCodTipoProvvedimento(getCodTipoProvvedimento(lEveProvvedimentoMod
				.getCodMotivo()));
		lEveProvvedimentoMod.setFlagDocumentoRegistrato(null);
		lEveProvvedimentoMod.setFlagStampaSiep("S");
		lEveProvvedimentoMod.setFlagVideoSiep("S");
		lEveProvvedimentoMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
		lEveProvvedimentoMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
		lEveProvvedimentoMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveProvvedimentoMod.setDataEmissione(getRequestDateParameter(
				ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveProvvedimentoMod.setDataTrasmissioneAtti(getRequestDateParameter(
				ICostantiNotifica.CAMPO_ANNO_DATA_INVIO, ICostantiNotifica.CAMPO_MESE_DATA_INVIO,
				ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
		lEveProvvedimentoMod
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveProvvedimentoMod.setCodEsito("-");
		lEveProvvedimentoMod.setCodTipoUfficioDestinatario("-");
		lEveProvvedimentoMod.setCodLuogoDestinatario("-");
		lEveProvvedimentoMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveProvvedimentoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveProvvedimentoMod.setDataInserimento(DateUtils.getSysDate());

		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		lEveNotMod.setEvento(lEveProvvedimentoMod);

		// ==========================================================================
		// Recupero le notifiche
		// ==========================================================================
		NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
		lEveNotMod.setNotifiche(lNotifiche);

		// =========================================================================
		// Inserimento annotazioni provvedimento e notifiche
		// =========================================================================
		IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnManIns = null;
		lAnnManIns = lAnnManCtrl.ExInserisciAnnotazioneManualeEventoNotifica(lAnnMod, lEveNotMod);

		// Nuova versione Richiama la funzione di Calcolo e inserimento della pena
		// String lPage = null;
		// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.calcolopena.action.ActCalcoloPenaComputo"
		// +"&lFlagPage=ALTRO"
		// +"&"+ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO + "=" + lAnnManIns.getEveIdEvento()
		// +"&"+ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE + "=" +
		// lAnnManIns.getCodTipoAnnotazione()
		// +"&"+ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE + "=" +
		// lAnnManIns.getIdAnnotazioneManuale();

		// Vecchia versione
		// ==========================================================================
		// EFFETTUA IL CALCOLO DELLA PENA
		// ==========================================================================
		String lPage = "";
		String errMsg = this.calcolaPena(lAnnManIns);
		if (errMsg != null) {
			return errMsg;
		}
		// ==========================================================================
		// Prepara la pagina di destinazione (dettaglio)
		// ==========================================================================
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.calcolopena.action.ActLoadDettaglioRidetPena&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lAnnManIns.getEveIdEvento();
		return lPage;

	}

	/**
	 * Effettua l'inserimento del provvedimento di rideterminazione pena per un motivo generico. menu:
	 * 'Rideterminazione Pena - Provvedimenti del PM - Altro' Viene effettuato anche il calcolo e inserimento
	 * della Pena e Fungibilità
	 * 
	 * Inserisce: - annotazione manuale - Provvedimento (01-04) o Comunicazione (01-12) - Pena Residua -
	 * Fungibilità
	 * 
	 * @return la pagina di dettaglio
	 * @deprecated Vecchia gestione calcolo pena
	 */
	// public String processRequestOLD() throws Exception {
	// isFascicoloSiepDiCompetenza();
	//
	// FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
	// BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();
	//
	// // ==========================================================================
	// // Recupero dalla form i dati dell'annotazione manuale da inserire
	// // ==========================================================================
	// AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
	//
	// lAnnMod.setCodTipoAnnotazione("014"); // altro
	// lAnnMod.setFlagPiuMeno(getRequestStringParameter("PM"));
	// lAnnMod.setFlagConforme("-");
	// lAnnMod.setFlagValidato("N");
	// lAnnMod.setCodFonte("-");
	// lAnnMod.setCodSottonumerazione("-");
	// lAnnMod.setCodCausaleComputo("-");
	// lAnnMod.setCodDpr("-");
	// lAnnMod.setFlagAppProvvisoria("-");
	// lAnnMod.setMotivazioni(getRequestStringParameter("motivazioni"));
	//
	// lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);
	//
	// lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
	// lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	// lAnnMod.setDataInserimento(DateUtils.getSysDate());
	//
	// // ===========================================
	// // reclusione
	// // ===========================================
	// String GRec = getRequestStringParameter("GRec");
	// String MRec = getRequestStringParameter("MRec");
	// String ARec = getRequestStringParameter("ARec");
	// String Multa = getRequestStringParameter("Multa");
	// String Multa_dec = getRequestStringParameter("Mul_dec");
	//
	// if (!ARec.equals(""))
	// lAnnMod.setNumAnniReclusione(new BigDecimal(ARec));
	// else
	// lAnnMod.setNumAnniReclusione(new BigDecimal(0));
	//
	// if (!MRec.equals(""))
	// lAnnMod.setNumMesiReclusione(new BigDecimal(MRec));
	// else
	// lAnnMod.setNumMesiReclusione(new BigDecimal(0));
	//
	// if (!GRec.equals(""))
	// lAnnMod.setNumGiorniReclusione(new BigDecimal(GRec));
	// else
	// lAnnMod.setNumGiorniReclusione(new BigDecimal(0));
	//
	// if (!Multa.equals("")) {
	// if (!Multa_dec.equals("")) {
	// lAnnMod.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
	// } else
	// lAnnMod.setImportoMulta(new BigDecimal(Multa));
	// } else if (!Multa_dec.equals(""))
	// lAnnMod.setImportoMulta(new BigDecimal("0." + Multa_dec));
	//
	// // ===========================================
	// // arresto
	// // ===========================================
	// String GArr = getRequestStringParameter("GArr");
	// String MArr = getRequestStringParameter("MArr");
	// String AArr = getRequestStringParameter("AArr");
	// String Ammenda = getRequestStringParameter("Ammenda");
	// String Ammenda_dec = getRequestStringParameter("Amm_dec");
	//
	// if (!AArr.equals(""))
	// lAnnMod.setNumAnniArresto(new BigDecimal(AArr));
	// else
	// lAnnMod.setNumAnniArresto(new BigDecimal(0));
	//
	// if (!MArr.equals(""))
	// lAnnMod.setNumMesiArresto(new BigDecimal(MArr));
	// else
	// lAnnMod.setNumMesiArresto(new BigDecimal(0));
	//
	// if (!GArr.equals(""))
	// lAnnMod.setNumGiorniArresto(new BigDecimal(GArr));
	// else
	// lAnnMod.setNumGiorniArresto(new BigDecimal(0));
	//
	// if (!Ammenda.equals("")) {
	// if (!Ammenda_dec.equals("")) {
	// lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
	// } else
	// lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda));
	// } else if (!Ammenda_dec.equals(""))
	// lAnnMod.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));
	//
	// // ==========================================================================
	// // Evento Provvedimento
	// // ==========================================================================
	// EventoModel lEveProvvedimentoMod = new EventoModel();
	//
	// lEveProvvedimentoMod.setCodTipoEvento("01");
	// // modifica 17-06-05 --Dario --Luciana
	// // lEveProvvedimentoMod.setCodTipoProvvedimento("04");
	// // Cambiata la codifica del Tipo Provvedimento. Luigi 14-10-2005
	// lEveProvvedimentoMod.setCodMotivo(this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
	// lEveProvvedimentoMod.setCodTipoProvvedimento(getCodTipoProvvedimento(lEveProvvedimentoMod
	// .getCodMotivo()));
	// lEveProvvedimentoMod.setFlagDocumentoRegistrato(null);
	// lEveProvvedimentoMod.setFlagStampaSiep("S");
	// lEveProvvedimentoMod.setFlagVideoSiep("S");
	// lEveProvvedimentoMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
	// lEveProvvedimentoMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
	// lEveProvvedimentoMod.setFasSieIdFascicoloSiep(lIdFascicolo);
	// lEveProvvedimentoMod.setDataEmissione(getRequestDateParameter(
	// ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
	// ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
	// lEveProvvedimentoMod.setDataTrasmissioneAtti(getRequestDateParameter(
	// ICostantiNotifica.CAMPO_ANNO_DATA_INVIO, ICostantiNotifica.CAMPO_MESE_DATA_INVIO,
	// ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
	// lEveProvvedimentoMod
	// .setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
	// lEveProvvedimentoMod.setCodEsito("-");
	// lEveProvvedimentoMod.setCodTipoUfficioDestinatario("-");
	// lEveProvvedimentoMod.setCodLuogoDestinatario("-");
	// lEveProvvedimentoMod.setCodOperatoreInserimento(getCodUtenteConnesso());
	// lEveProvvedimentoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	// lEveProvvedimentoMod.setDataInserimento(DateUtils.getSysDate());
	//
	// EventoNotificaModel lEveNotMod = new EventoNotificaModel();
	// lEveNotMod.setEvento(lEveProvvedimentoMod);
	//
	// // ==========================================================================
	// // Recupero le notifiche
	// // ==========================================================================
	// NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
	// lEveNotMod.setNotifiche(lNotifiche);
	//
	// // =========================================================================
	// // Inserimento annotazioni provvedimento e notifiche
	// // =========================================================================
	// IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
	// AnnotazioneManualeModel lAnnManIns = null;
	// lAnnManIns = lAnnManCtrl.ExInserisciAnnotazioneManualeEventoNotifica(lAnnMod, lEveNotMod);
	//
	// // **************************************************************************
	//
	// // ==========================================================================
	// // Viene rieffettuato il Calcolo della Pena e agganciato all'evento
	// // dell'annotazione manuale.
	// // Due Casi
	// // abInizio: si parte dalla dalla pena complessiva e ...
	// // non AbInizio: si parte dall'ultima pena residua validata e si sommano
	// // i soli quantum delle annotazioni manuali concesse in questa
	// // fase
	// // ==========================================================================
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("=================================================");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("                                                 ");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("         INIZIO CALCOLO DELLA PENA               ");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("                                                 ");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("=================================================");
	//
	// // ==================================
	// // Recupero posizione giuridica
	// // ==================================
	// PosizioneGiuridicaModel PGMod = new PosizioneGiuridicaModel();
	// PGMod.setFasSieIdFascicoloSiep(lIdFascicolo);
	//
	// IPosizioneGiuridica lPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
	// PosizioneGiuridicaModel PGMod2 = lPG.ExRicercaPosizioneGiuridicaCorrente(PGMod);
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("POSIZIONE GIURIDICA : " + PGMod2.getCodPosizioneGiuridica());
	//
	// if (PGMod2 == null) // mai vera !!!
	// throw new SIEPException(SIEPException.USER_MESSAGE,
	// "Rivedere Misure Cautelari o Posizione Giuridica !");
	//
	// boolean detenutoQuestaCausa = false;
	// boolean libero = false;
	// boolean detenutoAltraCausa = false;
	//
	// if (PGMod2.getCodPosizioneGiuridica().equals("07") // LIBERO
	// || PGMod2.getCodPosizioneGiuridica().equals("10") // LIBERO
	// || PGMod2.getCodPosizioneGiuridica().equals("16") // LIBERO IN DIFFERIMENTO PENA
	// || PGMod2.getCodPosizioneGiuridica().equals("17") // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
	// ) {
	// libero = true;
	// } else
	// detenutoQuestaCausa = true;
	//
	// // ==========================================================================
	// // Recupero la data inizio pena per i calcoli:
	// // Se libero, potrebbe essere detenuto per altra causa. In questo caso utilizzo
	// // la data_scadenza (se presente) pena altra causa + 1 come data inizio.
	// // Altrimenti (libero veramente) non ho una data inizio pena e non calcolerò
	// // le date di decorrenza
	// // ==========================================================================
	// String GiornoInizio = new String("");
	// String MeseInizio = new String("");
	// String AnnoInizio = new String("");
	//
	// String FlagAltraCausa = "N";
	//
	// // ================================================
	// //
	// // ================================================
	// if (libero) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("LIBERO");
	//
	// FascicoloSiepModel lFascMod = new FascicoloSiepModel();
	// IFascicoloSiep lFS = SIEPLookupRemote.getFascicoloSiepRemote();
	// lFascMod = lFS.ExRicercaFascicoloByKey(lIdFascicolo);
	//
	// if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
	// detenutoAltraCausa = true;
	// FlagAltraCausa = "S";
	//
	// AltraCausaModel lAcModel = new AltraCausaModel();
	// IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
	// lAcModel = lAC.ExRicercaAltraCausaByFascicolo(lIdFascicolo);
	//
	// if (lAcModel.getDataDecorrenza() != null && lAcModel.getDataScadenza() != null) {
	// Date datainizio = DateUtils.getDayAfter(lAcModel.getDataScadenza());
	//
	// GiornoInizio = DateUtils.getDayToString(datainizio);
	// MeseInizio = DateUtils.getMonthToString(datainizio);
	// AnnoInizio = DateUtils.getYearToString(datainizio);
	// } else {
	// GiornoInizio = "-";
	// }
	// }
	// }
	//
	// // ==============================
	// // Recupero la PENA COMPLESSIVA
	// // ==============================
	// PenaComplessivaModel lPenMod = new PenaComplessivaModel();
	// lPenMod.setFasSieIdFascicoloSiep(lIdFascicolo);
	//
	// IPenaComplessiva lPCon = SIEPLookupRemote.getPenaComplessivaRemote();
	// Vector lPComples = lPCon.ExRicercaPenaComplessivaNoError(lPenMod);
	//
	// if (lPComples.size() == 0) { // IMPOSSIBILE
	// RedirectTo lRedirigi = new RedirectTo();
	// lRedirigi.setPage(IWebConstants.PG_MAIN);
	// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Complessiva mancante");
	// lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
	// + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
	// + ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
	// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
	// return IWebConstants.PG_MESSAGE;
	// }
	//
	// lPenMod = (PenaComplessivaModel) (lPComples.get(0));
	//
	// // ==========================================================================
	// // CALCOLO PENA GIA ESPIATA
	// // ==========================================================================
	// // Viene recuperata l'ultima PENA_RESIDUA VALIDATA e utilizzata la data
	// // inizio come data inizio della pena già espiata e data fine la data corrente
	// // (sysdate)
	// // Viene quindi calcolato il quantum
	// // n.b. questo è vero se il soggetto non è libero e detenuto per questa causa????
	// // La pena già espiata non viene utilizzata per il calcolo della pena residua,
	// // ma nel calcolo della fungibilità
	// // Potrebbe accadere che esiste la data inizio ma abbia un valore futuro
	// // rispetto alla data dei calcoli. In questo caso la pena espiata è ovviamente
	// // null.
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug(" CALCOLO PENA GIA ESPIATA ");
	//
	// Date lDataInizioPena = null;
	// Date lDataFineReclusione = null;
	// Date lDataInizioArresto = null;
	// Date lDataFinePena = null;
	//
	// ICalcoloPena lCalPen = SIEPLookupRemote.getCalcoloPenaRemote();
	// CalendarUtil lCalCon = new CalendarUtil();
	//
	// CalendarModel lPenaGiaEspiata = new CalendarModel();
	// boolean ForzaFungibilita = false;
	//
	// PenaResiduaModel lUltimaPenRes = new PenaResiduaModel();
	// IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
	// lUltimaPenRes = IPenRes.ExRicercaPenaResiduaUltimaByDate(lIdFascicolo);
	//
	// if (lUltimaPenRes == null || lUltimaPenRes.getDataInizio() == null) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger
	// .debug("ATTENZIONE! non esiste una pena validata a sistema o data inizio pena assente, impossibile calcolare la Pena Espiata");
	// GiornoInizio = "-";
	// } else {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Calcolo la pena espiata");
	// lDataInizioPena = lUltimaPenRes.getDataInizio();
	// lDataFinePena = lUltimaPenRes.getDataFine();
	//
	// // if (!DateUtils.isGreater(lDataInizioPena,dataSistemaPerCalcoli)) {
	// // Il calcolo dell'espiato viene fatto solo se la data inizio<=data calcoli
	// lPenaGiaEspiata.setDataInizio(lUltimaPenRes.getDataInizio());
	// lPenaGiaEspiata.setDataFine(DateUtils.getSysDate());
	//
	// lPenaGiaEspiata = lCalCon.CalcolaNumGiorniMesiAnni(lPenaGiaEspiata, false);
	// lPenaGiaEspiata = lCalCon.ricalcolaGAM(lPenaGiaEspiata);
	// }
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Fine CALCOLO PENA GIA ESPIATA = " + lPenaGiaEspiata);
	//
	// // ==========================================================================
	// // Controlla se il calcolo della pena deve essere AB INITIO o no
	// // Se il calcolo è abInizio vengono prese in considerazione:
	// // - PENA COMPLESSIVA IN SENTENZA
	// // - BENEFICI (concessi, revocati)
	// // - MISURE CAUTELARI (computabili)
	// // - ANNOTAZIONI MANUALI (a sistema, tutte, validate o meno ma solo A/R richieste al GE)
	// // Se non ab inizio:
	// // - ultima pena residua VALIDATA
	// // - ANNOTAZIONI MANUALI (a sistema solo A/R )
	// //
	// // ==========================================================================
	//
	// boolean lIsCalcoloPenaAbInitio = lCalPen.ExIsCalcoloPenaAbInizio(lIdFascicolo);
	//
	// CalendarModel lBeneficiConcessiReclusione = new CalendarModel();
	// CalendarModel lBeneficiRevocatiReclusione = new CalendarModel();
	// CalendarModel lBeneficiConcessiArresto = new CalendarModel();
	// CalendarModel lBeneficiRevocatiArresto = new CalendarModel();
	//
	// CalendarModel lMisCauComputabiliReclusione = new CalendarModel();
	// CalendarModel lMisCauComputabiliArresto = new CalendarModel();
	//
	// if (!lIsCalcoloPenaAbInitio) {
	// // Se CALCOLO PENA NON AB INITIO, utilizzo come punto di partenza per il
	// // calcolo della pena residua, l'ultimo record pena residua VALIDATO (se
	// // esiste) invece della pena complessiva in sentenza.
	// // E comunque non considero i benefici e le misure cautelari
	// // CALCOLO NUOVO QUANTUM (gg,mm,aa) A PARTIRE DALLE DATE
	// PenaResiduaModel lPenaRicalcolata = null;
	//
	// if (lUltimaPenRes != null && lUltimaPenRes.getDataInizio() != null
	// && lUltimaPenRes.getDataFine() != null) {
	// // Punto di partenza Ultima Pena NORMALIZZATA in quanto ho le date
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Pena residua in espiazione, ricalcolo i quantum (normalizzo)");
	// lPenaRicalcolata = PenaResiduaUtil.calcolaPenaNuovaDataFine(lUltimaPenRes.getDataFine(),
	// lUltimaPenRes, false); // true o false ??
	//
	// lPenMod.setNumAnniArresto(lPenaRicalcolata.getNumAnniArresto());
	// lPenMod.setNumMesiArresto(lPenaRicalcolata.getNumMesiArresto());
	// lPenMod.setNumGiorniArresto(lPenaRicalcolata.getNumGiorniArresto());
	//
	// lPenMod.setImportoAmmenda(lPenaRicalcolata.getImportoAmmenda());
	//
	// lPenMod.setNumAnniReclusione(lPenaRicalcolata.getNumAnniReclusione());
	// lPenMod.setNumMesiReclusione(lPenaRicalcolata.getNumMesiReclusione());
	// lPenMod.setNumGiorniReclusione(lPenaRicalcolata.getNumGiorniReclusione());
	//
	// lPenMod.setImportoMulta(lPenaRicalcolata.getImportoMulta());
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("==> Punto di partenza Ultima Pena Residua VALIDATA e NORMALIZZATA: = "
	// + lPenMod);
	// } else if (lUltimaPenRes != null && PGMod2.isLibero()) {
	// // Punto di partenza Ultima Pena NON NORMALIZZATA in quanto soggetto libero
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Sono Libero: punto di partenza la pena residua a sistema");
	// lPenMod.setNumAnniArresto(lUltimaPenRes.getNumAnniArresto());
	// lPenMod.setNumMesiArresto(lUltimaPenRes.getNumMesiArresto());
	// lPenMod.setNumGiorniArresto(lUltimaPenRes.getNumGiorniArresto());
	//
	// lPenMod.setImportoAmmenda(lUltimaPenRes.getImportoAmmenda());
	//
	// lPenMod.setNumAnniReclusione(lUltimaPenRes.getNumAnniReclusione());
	// lPenMod.setNumMesiReclusione(lUltimaPenRes.getNumMesiReclusione());
	// lPenMod.setNumGiorniReclusione(lUltimaPenRes.getNumGiorniReclusione());
	//
	// lPenMod.setImportoMulta(lUltimaPenRes.getImportoMulta());
	// } else {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger
	// .debug("ATTENZIONE!! Pena residua validata assente o mancante di date di non in espiazione");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("==> Punto di partenza resta la Pena Complessiva");
	// }
	// } else {
	// // Pena abInizio ho già la pena Complessiva, recupero Benefici e MC computabili
	// // BENEFICI
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Recupero i benefici concessi e revocati (Reclusione/Arresto");
	// lBeneficiConcessiReclusione = new CalendarModel(
	// lCalPen.exGetBeneficiConcessiReclusione(lIdFascicolo));
	// lBeneficiRevocatiReclusione = new CalendarModel(
	// lCalPen.exGetBeneficiRevocatiReclusione(lIdFascicolo));
	// lBeneficiConcessiArresto = new CalendarModel(lCalPen.exGetBeneficiConcessiArresto(lIdFascicolo));
	// lBeneficiRevocatiArresto = new CalendarModel(lCalPen.exGetBeneficiRevocatiArresto(lIdFascicolo));
	//
	// // MISURE CAUTELARI
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Recupero le Misure Cautelari Computabili");
	// lMisCauComputabiliReclusione = new CalendarModel(
	// lCalPen.exGetMisureCautelariComputabiliReclusione(lIdFascicolo));
	// lMisCauComputabiliArresto = new CalendarModel(
	// lCalPen.exGetMisureCautelariComputabiliArresto(lIdFascicolo));
	// }
	//
	// // ==========================================================================
	// // DA QUI LE EFFETTIVE ANNOTAZIONI MANUALI
	// // ==========================================================================
	// // recupera le sole annotazioni manuali del tipo di quelle inserite (014-Altro)
	// // se abInizio = true recupero tutte le annotazioni Validate
	// String lCodTipoAnnotazione = "014";
	//
	// CalendarModel lAnnManConcessiReclusione = new CalendarModel(
	// lCalPen.exGetAnnotazioniManualiConcessiReclusione(lIdFascicolo, lCodTipoAnnotazione,
	// lIsCalcoloPenaAbInitio));
	// CalendarModel lAnnManRevocatiReclusione = new CalendarModel(
	// lCalPen.exGetAnnotazioniManualiRevocatiReclusione(lIdFascicolo, lCodTipoAnnotazione,
	// lIsCalcoloPenaAbInitio));
	//
	// CalendarModel lAnnManConcessiArresto = new CalendarModel(
	// lCalPen.exGetAnnotazioniManualiConcessiArresto(lIdFascicolo, lCodTipoAnnotazione,
	// lIsCalcoloPenaAbInitio));
	// CalendarModel lAnnManRevocatiArresto = new CalendarModel(
	// lCalPen.exGetAnnotazioniManualiRevocatiArresto(lIdFascicolo, lCodTipoAnnotazione,
	// lIsCalcoloPenaAbInitio));
	//
	// // SOLO se il calcolo è abInizio, recupero anche le altre annotazioni
	// // inserite con anticipazione degli effetti, che devono essere computate nel
	// // quantum finale
	// if (lIsCalcoloPenaAbInitio) {
	// CalendarModel lAnnManConcessiReclusioneAnt = new CalendarModel(
	// lCalPen.exGetAnnotazioniManualiConcessiAnticipazioneReclusione(lIdFascicolo,
	// lCodTipoAnnotazione, lIsCalcoloPenaAbInitio));
	// CalendarModel lAnnManRevocatiReclusioneAnt = new CalendarModel(
	// lCalPen.exGetAnnotazioniManualiRevocatiAnticipazioneReclusione(lIdFascicolo,
	// lCodTipoAnnotazione, lIsCalcoloPenaAbInitio));
	// CalendarModel lAnnManConcessiArrestoAnt = new CalendarModel(
	// lCalPen.exGetAnnotazioniManualiConcessiAnticipazioneArresto(lIdFascicolo,
	// lCodTipoAnnotazione, lIsCalcoloPenaAbInitio));
	// CalendarModel lAnnManRevocatiArrestoAnt = new CalendarModel(
	// lCalPen.exGetAnnotazioniManualiRevocatiAnticipazioneArresto(lIdFascicolo,
	// lCodTipoAnnotazione, lIsCalcoloPenaAbInitio));
	//
	// // Sommo il tutto
	// lAnnManConcessiReclusione = new CalendarModel(lCalCon.sommaGiornieValute(
	// lAnnManConcessiReclusione, lAnnManConcessiReclusioneAnt));
	// lAnnManRevocatiReclusione = new CalendarModel(lCalCon.sommaGiornieValute(
	// lAnnManRevocatiReclusione, lAnnManRevocatiReclusioneAnt));
	// lAnnManConcessiArresto = new CalendarModel(lCalCon.sommaGiornieValute(lAnnManConcessiArresto,
	// lAnnManConcessiArrestoAnt));
	// lAnnManRevocatiArresto = new CalendarModel(lCalCon.sommaGiornieValute(lAnnManRevocatiArresto,
	// lAnnManRevocatiArrestoAnt));
	// }
	//
	// // Sommo Annotazioni Manuali e Benefici .....
	// lBeneficiConcessiReclusione = new CalendarModel(lCalCon.sommaGiornieValute(
	// lBeneficiConcessiReclusione, lAnnManConcessiReclusione));
	// lBeneficiRevocatiReclusione = new CalendarModel(lCalCon.sommaGiornieValute(
	// lBeneficiRevocatiReclusione, lAnnManRevocatiReclusione));
	// lBeneficiConcessiArresto = new CalendarModel(lCalCon.sommaGiornieValute(lBeneficiConcessiArresto,
	// lAnnManConcessiArresto));
	// lBeneficiRevocatiArresto = new CalendarModel(lCalCon.sommaGiornieValute(lBeneficiRevocatiArresto,
	// lAnnManRevocatiArresto));
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("--------------------------------------");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger
	// .debug("Benefici Reclusione Concessi : " + lBeneficiConcessiReclusione.getImportoAmmenda()
	// + "----" + lBeneficiConcessiReclusione.getNumAnni() + "/"
	// + lBeneficiConcessiReclusione.getNumMesi() + "/"
	// + lBeneficiConcessiReclusione.getNumGiorni());
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger
	// .debug("Benefici Reclusione Revocati : " + lBeneficiRevocatiReclusione.getImportoAmmenda()
	// + "----" + lBeneficiRevocatiReclusione.getNumAnni() + "/"
	// + lBeneficiRevocatiReclusione.getNumMesi() + "/"
	// + lBeneficiRevocatiReclusione.getNumGiorni());
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("--------------------------------------");
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("--------------------------------------");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Totali Arresto Concessi : " + lBeneficiConcessiArresto.getImportoAmmenda() + "----"
	// + lBeneficiConcessiArresto.getNumAnni() + "/" + lBeneficiConcessiArresto.getNumMesi() + "/"
	// + lBeneficiConcessiArresto.getNumGiorni());
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Totali Arresto Revocati : " + lBeneficiRevocatiArresto.getImportoAmmenda() + "----"
	// + lBeneficiRevocatiArresto.getNumAnni() + "/" + lBeneficiRevocatiArresto.getNumMesi() + "/"
	// + lBeneficiRevocatiArresto.getNumGiorni());
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("--------------------------------------");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("--------------------------------------");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger
	// .debug("Totali Reclusione Concessi : " + lBeneficiConcessiReclusione.getImportoMulta()
	// + "----" + lBeneficiConcessiReclusione.getNumAnni() + "/"
	// + lBeneficiConcessiReclusione.getNumMesi() + "/"
	// + lBeneficiConcessiReclusione.getNumGiorni());
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger
	// .debug("Totali Reclusione Revocati : " + lBeneficiRevocatiReclusione.getImportoMulta()
	// + "----" + lBeneficiRevocatiReclusione.getNumAnni() + "/"
	// + lBeneficiRevocatiReclusione.getNumMesi() + "/"
	// + lBeneficiRevocatiReclusione.getNumGiorni());
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("--------------------------------------");
	//
	// // -------------------------------------------------------
	// // FINE ANNOTAZIONI MANUALI
	// // -------------------------------------------------------
	//
	// // ==========================================================================
	// // Solo se non ergastolo viene rieffettuato il vero calcolo della pena,
	// // vale a dire vengono:
	// // - ricalcolati i quantum
	// // - le date di decorrenza
	// // - l'eventuale fungibilità
	// //
	// // Se ergastolo invece:
	// // - copia i dati dell'ultima pena
	// // ==========================================================================
	// if (!(lPenMod.getCodTipoPenaDetentiva().equals("03") || lPenMod.getCodTipoPenaDetentiva()
	// .equals("04"))) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("FORZAFUNG:" + ForzaFungibilita);
	// // =======================================================
	// // Aggiorno/inseriscio il quantum pena residua + importi
	// // =======================================================
	// PenaResiduaModel lPenaComplessiva = null;
	// lPenaComplessiva = lCalPen.exCalcolaQuantumPenaComplessivaNuovo(
	// lIdFascicolo,
	// lPenMod, // O pena complessiva in sentenza o Ultima pena Residua Validata e Normalizzata
	// lBeneficiConcessiReclusione, lBeneficiRevocatiReclusione, lBeneficiConcessiArresto,
	// lBeneficiRevocatiArresto, lMisCauComputabiliReclusione, lMisCauComputabiliArresto,
	// getCodUtenteConnesso(), getCodUfficioUtenteConnesso(), ForzaFungibilita, // sempre false
	// lPenaGiaEspiata); // non usato se ForzaFungibilita = false, quindi non usato
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("lPenaComplessiva = " + lPenaComplessiva);
	//
	// CalendarModel lTotRideterminato = new CalendarModel();
	// lTotRideterminato.setNumAnni(lPenaComplessiva.getNumAnniArresto().add(
	// lPenaComplessiva.getNumAnniReclusione()));
	// lTotRideterminato.setNumMesi(lPenaComplessiva.getNumMesiArresto().add(
	// lPenaComplessiva.getNumMesiReclusione()));
	// lTotRideterminato.setNumGiorni(lPenaComplessiva.getNumGiorniArresto().add(
	// lPenaComplessiva.getNumGiorniReclusione()));
	//
	// if (!detenutoAltraCausa
	// && (lCalCon.isGreater(lTotRideterminato, lPenaGiaEspiata) || lCalCon
	// .isZero(lTotRideterminato))) {
	// ForzaFungibilita = false;
	// }
	//
	// BigDecimal lIndicePenaResidua = null;
	// lIndicePenaResidua = lPenaComplessiva.getIdPenaResidua();
	// // // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// // siesLogger.debug("GIORNO INIZIO :
	// // "+getRequestStringParameter("GiornoInizio") );
	// // ========================================================================
	// // Calcolo le date di espiazione della pena residua se è presente una
	// // lDataInizioPena: dataInizio, DataFineReclusione, DataInizioArresti,
	// // DataFine.
	// // Quindi aggiorno il record PENA_RESIDUA
	// // n.b. lDataInizioPena = data inizio ultima pena residua Validata
	// // ========================================================================
	// if (lDataInizioPena != null) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger
	// .debug("-------------------------------------------------------------------------------");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug(" Ricalcolo le date di espiazione a partire dalla data inizio ("
	// + lDataInizioPena + ") della pena a sistema (validata) e del quantum calcolato ("
	// + lPenaComplessiva + ")");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger
	// .debug("-------------------------------------------------------------------------------");
	//
	// Vector lDateFine = lCalPen.exCalcolaDataFinePena(lDataInizioPena, lPenaComplessiva, true);
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Date intermedie calcolate " + lDateFine.size());
	//
	// if (lDateFine.size() == 1) {
	// // solo Reclusione o Arresti: ho quindi solo data fine
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Ho solo una data: " + lDateFine.get(0));
	//
	// boolean cond = lPenaComplessiva.getNumAnniArresto().equals(null)
	// || lPenaComplessiva.getNumAnniArresto().intValue() == 0;
	// cond = cond
	// && (lPenaComplessiva.getNumMesiArresto().equals(null) || lPenaComplessiva
	// .getNumMesiArresto().intValue() == 0);
	// cond = cond
	// && (lPenaComplessiva.getNumGiorniArresto().equals(null) || lPenaComplessiva
	// .getNumGiorniArresto().intValue() == 0);
	//
	// if (!cond)
	// lDataInizioArresto = lDataInizioPena;
	//
	// lDataFinePena = (Date) lDateFine.get(0);
	// }
	//
	// if (lDataFinePena != null && lDataFinePena.after(DateUtils.getSysDate())) {
	// if (lDateFine.size() == 2) {
	// lDataFineReclusione = (Date) lDateFine.get(0);
	// lDataInizioArresto = DateUtils.getDayAfter(lDataFineReclusione);
	// lDataFinePena = (Date) lDateFine.get(1);
	// }
	// }
	//
	// if (lDateFine.size() != 0 && !ForzaFungibilita) {
	// // Se ho quantum positivi Aggiorno le date della pena residua
	// // precedentemente inserita
	// lPenaComplessiva.setIdPenaResidua(lIndicePenaResidua);
	//
	// lPenaComplessiva.setDataInizio(lDataInizioPena);
	// lPenaComplessiva.setDataFineReclusione(lDataFineReclusione);
	// lPenaComplessiva.setDataInizioArresto(lDataInizioArresto);
	// lPenaComplessiva.setDataFinePresunta(lDataFinePena);// !!!!!!!!
	//
	// lPenaComplessiva.setDiesAQuo("S");
	//
	// lPenaComplessiva.setCodOperatoreInserimento(getCodUtenteConnesso());
	// lPenaComplessiva.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	// lPenaComplessiva.setDataInserimento(DateUtils.getSysDate());
	//
	// IPenaResidua lPPres = SIEPLookupRemote.getPenaResiduaRemote();
	// lPPres.ExModificaPenaResidua(lPenaComplessiva);
	// } else { // lDateFine.size() = 0 per cui ho dei quantum negativi o nulli,
	// // con soggetto in espiazione (il che implica che necessariamente
	// // ho una fungibilità) aggiorno la data inizio e data fine
	// // lDataFinePena = data fine pena attuale, non quella ricalcolata
	//
	// // Se il quantum di pena ricalcolato è negativo o nullo lo lascio
	// // a sistema, ma lascio anche le date di decorrenza previste per
	// // prima del calcolo solo la data
	// lPenaComplessiva.setIdPenaResidua(lIndicePenaResidua);
	//
	// lPenaComplessiva.setDataInizio(lDataInizioPena); // data decorrenza pena in corso di
	// // espiazione
	// lPenaComplessiva.setDataFine(lDataFinePena); // è la data fine pena attuale, non quella
	// // rideterminata
	//
	// lPenaComplessiva.setDiesAQuo("S");
	//
	// lPenaComplessiva.setCodOperatoreInserimento(getCodUtenteConnesso());
	// lPenaComplessiva.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	// lPenaComplessiva.setDataInserimento(DateUtils.getSysDate());
	//
	// IPenaResidua lPPres = SIEPLookupRemote.getPenaResiduaRemote();
	// // ??? data fine pena è quella prevista prima dell'inizio dei calcoli
	// // se sono giunto quì vuol dire che la pena rideterminata è <=0
	// // in questo caso aggiorno le date a sistema solo se il soggetto
	// // non è ancora in espiazione.
	// // In questo caso ho un soggetto in espiazione al momento del computo
	// // (data fine pena < data sistema) al quale sono stati applicati
	// // dei benefici tali da azzerargli la pena o portarla addirittura
	// // in negativo. Devo comunque inserire le date sulla pena residua.
	// if (lDataFinePena.after(DateUtils.getSysDate()))
	// lPPres.ExModificaPenaResidua(lPenaComplessiva);
	// }
	// }
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("DataInizioPena     = "
	// + DateUtils.getDateToString(lDataInizioPena, "dd/MM/yyyy"));
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("DataFineReclusione = "
	// + DateUtils.getDateToString(lDataFineReclusione, "dd/MM/yyyy"));
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("DataInizioArresto  = "
	// + DateUtils.getDateToString(lDataInizioArresto, "dd/MM/yyyy"));
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger
	// .debug("DataFinePena       = " + DateUtils.getDateToString(lDataFinePena, "dd/MM/yyyy"));
	//
	// // =======================================================================
	// // CALCOLO LA DELLA GESTIONE DELLA FUNGIBILITA
	// // =======================================================================
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("-------------------------------------------");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("  Determino l'eventuale Fungibilità:       ");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("-------------------------------------------");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Tot Pena Rideterminata (reclusione+arresti): Anni: "
	// + lTotRideterminato.getNumAnni() + ", Mesi: " + lTotRideterminato.getNumMesi()
	// + ", Giorni: " + lTotRideterminato.getNumGiorni());
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Pena Già Espiata                           : Anni: "
	// + lPenaGiaEspiata.getNumAnni() + ", Mesi: " + lPenaGiaEspiata.getNumMesi() + ", Giorni: "
	// + lPenaGiaEspiata.getNumGiorni());
	//
	// CalendarModel lFungCalendar = new CalendarModel();
	//
	// // ========================================================================
	// // Se il tot rideterminato è 0, e la pena è in espiazione, la fungbilità
	// // coincide con la pena già espiata. La data fine pena viene posta = data
	// // inizio in quanto la pena non sarebbe mai dovuta iniziare
	// // ========================================================================
	// if (lCalCon.isZero(lTotRideterminato)) {
	// lFungCalendar = new CalendarModel();
	//
	// if (lUltimaPenRes != null && lUltimaPenRes.getDataInizio() != null) {
	// lDataFinePena = lUltimaPenRes.getDataInizio();
	// }
	//
	// lFungCalendar = new CalendarModel(lPenaGiaEspiata);
	// } else {
	// // Nuova Pena da Espiare < Pena già espiata
	// if (!lCalCon.isGreater(lTotRideterminato, lPenaGiaEspiata)) {
	// if (lDataInizioPena != null) { // Soggetto in espiazione: in questo caso la pena
	// // rideterminata
	// // potrebbe essere sia <0 che >0
	// // n.b. non è detto che il soggetto sia in espiazione
	// // perchè la data
	// // inizio potrebbe essere futura
	// if (detenutoQuestaCausa) {
	// if (lDataFinePena == null) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
	// // al posto di LogF3B.getLogger()
	// siesLogger.debug("ENTRO SU DATA FINE A NULL");
	//
	// if (lPenaGiaEspiata.getDataFine().before(DateUtils.getSysDate())) {
	// CalendarModel tmp = new CalendarModel();
	// tmp.setDataFine(DateUtils.getSysDate());
	// tmp.setDataInizio(lPenaGiaEspiata.getDataFine());
	// lFungCalendar = lCalCon.CalcolaNumGiorniMesiAnni(tmp);
	// }
	// } else {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
	// // al posto di LogF3B.getLogger()
	// siesLogger.debug("ENTRO SU DATA FINE NON A NULL!!!!!!");
	// if (lDataFinePena.before(DateUtils.getSysDate())) {
	// CalendarModel tmp = new CalendarModel();
	// tmp.setDataFine(DateUtils.getSysDate());
	// tmp.setDataInizio(lDataFinePena);
	// lFungCalendar = lCalCon.CalcolaNumGiorniMesiAnni(tmp);
	// }
	// }
	// } else if (FlagAltraCausa.equals("S")) {
	// if (lDataFinePena.before(DateUtils.getSysDate())) {
	// CalendarModel tmp = new CalendarModel();
	// tmp.setDataFine(DateUtils.getSysDate());
	// tmp.setDataInizio(lDataFinePena);
	// lFungCalendar = lCalCon.CalcolaNumGiorniMesiAnni(tmp);
	// }
	// } else {
	// if (lDataFinePena.before(DateUtils.getSysDate())) {
	// lFungCalendar = lCalCon.ricalcolaGAM(lCalCon.BeneficisottraiGiornieValute(
	// lTotRideterminato, lPenaGiaEspiata));
	// }
	// }
	// } else
	// lFungCalendar = lCalCon.ricalcolaGAM(lCalCon.BeneficisottraiGiornieValute(
	// lTotRideterminato, lPenaGiaEspiata));
	// }
	// }
	//
	// if (lCalCon.isZero(lTotRideterminato) && !detenutoQuestaCausa && FlagAltraCausa.equals("N"))
	// lFungCalendar = new CalendarModel(lPenaGiaEspiata);
	//
	// FungibilitaModel lFunMod = new FungibilitaModel();
	// if (!lCalCon.isZero(lFungCalendar)) {
	// lFunMod.setFasSieIdFascicoloSiep(lIdFascicolo);
	//
	// lFunMod.setCodTipoFungibilita("02"); // 02=PENA ESPIATA IN ECCESSO ....Cg_ref_codes 24/10/2003
	// lFunMod.setNumAnni(new BigDecimal(lFungCalendar.getNumAnni() + ""));
	// lFunMod.setNumMesi(new BigDecimal(lFungCalendar.getNumMesi() + ""));
	// lFunMod.setNumGiorni(new BigDecimal(lFungCalendar.getNumGiorni() + ""));
	// lFunMod.setFlagValidato("N");
	//
	// lFunMod.setCodOperatoreInserimento(getCodUtenteConnesso());
	// lFunMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	// lFunMod.setDataInserimento(DateUtils.getSysDate());
	//
	// // **************
	// /**
	// * TODO negli altri casi veniva recuperato dalla request
	// */
	// lFunMod.setEveIdEvento(lAnnManIns.getEveIdEvento());
	// // **************
	//
	// IFungibilita iFun = SIEPLookupRemote.getFungibilitaRemote();
	// lFunMod = iFun.ExInserisciFungibilita(lFunMod);
	//
	// if (detenutoAltraCausa)
	// lDataFinePena = DateUtils.getSysDate();
	// }
	//
	// // ========================================================================
	// // Aggancio la pena rideterminata all'evento
	// // n.b. negli altri casi
	// // ========================================================================
	// lPenaComplessiva.setEveIdEvento(lAnnManIns.getEveIdEvento());
	// IPenRes.ExModificaPenaResidua(lPenaComplessiva);
	// } else // ERGASTOLO
	// {
	// PenaResiduaModel lPenRes = new PenaResiduaModel();
	//
	// lPenRes = new PenaResiduaModel();
	//
	// if (!GiornoInizio.equals("") && !GiornoInizio.equals("-"))
	// lDataInizioPena = DateUtils.getDate(AnnoInizio, MeseInizio, GiornoInizio);
	//
	// lDataFinePena = DateUtils.getDate(9999, 12, 31);
	//
	// if (lPenMod.getCodTipoPenaDetentiva().equals("03")) {
	// lPenRes.setFlagErgastolo("S");
	// } else if (lPenMod.getCodTipoPenaDetentiva().equals("04")) {
	// lPenRes.setFlagErgastolo("D");
	// }
	//
	// lPenRes.setDataFine(lDataFinePena);
	// lPenRes.setDataInizio(lDataInizioPena);
	//
	// lPenRes.setDataInizioIsolamentoDiurno(lPenMod.getDataInizioIsolamentoDiurno());
	// lPenRes.setDataFineIsolamentoDiurno(lPenMod.getDataFineIsolamentoDiurno());
	//
	// lPenRes.setNumGiorniIsolamentoDiurno(lPenMod.getNumGiorniIsolamentoDiurno());
	// lPenRes.setNumMesiIsolamentoDiurno(lPenMod.getNumMesiIsolamentoDiurno());
	// lPenRes.setNumAnniIsolamentoDiurno(lPenMod.getNumAnniIsolamentoDiurno());
	// lPenRes.setFlagValidato("N");
	// lPenRes.setDiesAQuo("S");
	// lPenRes.setFasSieIdFascicoloSiep(lIdFascicolo);
	//
	// lPenRes.setCodOperatoreInserimento(getCodUtenteConnesso());
	// lPenRes.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	// lPenRes.setDataInserimento(DateUtils.getSysDate());
	//
	// // !!!! PROBLEMA DELLA TRANSAZIONE !!!!
	// IPenRes.ExInsertOrUpdatePenaResidua(lPenRes);
	// }
	//
	// // ==========================================================================
	// // Prepara la pagina di destinazione (dettaglio)
	// // ==========================================================================
	// String lPage = "";
	// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
	// + "=siap.siep.calcolopena.action.ActLoadDettaglioRidetPena&"
	// + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lAnnManIns.getEveIdEvento();
	// return lPage;
	// }

	/**
	 * Restituisce il tipo provvedimento in funzione del codice motivo Luigi 14-10-2005
	 * 
	 * @param aCodMotivo
	 *            codice motivo
	 * @return codice tipo provvedimento
	 * @throws F3BException
	 *             se il codice motivo non è tra quelli gestiti
	 */
	private String getCodTipoProvvedimento(String aCodMotivo) throws F3BException {
		String lCodTipoProv = "";

		if (aCodMotivo == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Codice Motivo assente");
		else if (aCodMotivo.compareTo("0913") == 0)
			lCodTipoProv = "12";
		else if (aCodMotivo.compareTo("0914") == 0)
			lCodTipoProv = "12";
		else if (aCodMotivo.compareTo("0915") == 0)
			lCodTipoProv = "12";
		else if (aCodMotivo.compareTo("0916") == 0)
			lCodTipoProv = "12";
		else if (aCodMotivo.compareTo("0917") == 0)
			lCodTipoProv = "04";
		else if (aCodMotivo.compareTo("0918") == 0)
			lCodTipoProv = "04";
		else if (aCodMotivo.compareTo("0919") == 0)
			lCodTipoProv = "04";
		else
			throw new F3BException(F3BException.USER_MESSAGE, "Codice Motivo errato -> " + aCodMotivo);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("CodTipoProvvedimento -> " + lCodTipoProv);
		return lCodTipoProv;
	}

	/**
	 * Metodo che effettua ilk calcolo della pena e l'inserimento a sistema
	 * 
	 * @param lAnnMod
	 * @return
	 * @throws Exception
	 */
	private String calcolaPena(AnnotazioneManualeModel lAnnMod) throws Exception {

		FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

		// ==========================================================================
		// Viene rieffettuato il Calcolo della Pena e agganciato all'evento
		// dell'annotazione manuale.
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("=================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("                                                 ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("   INIZIO CALCOLO DELLA PENA (ridet pena altro)  ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("                                                 ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("=================================================");

		// ==================================
		// Recupero posizione giuridica
		// ==================================
		PosizioneGiuridicaModel PGMod = new PosizioneGiuridicaModel();
		PGMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		IPosizioneGiuridica lPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel PGMod2 = lPG.ExRicercaPosizioneGiuridicaCorrente(PGMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("POSIZIONE GIURIDICA : " + PGMod2.getCodPosizioneGiuridica());

//		if (PGMod2 == null) // mai vera !!!
//			throw new SIEPException(SIEPException.USER_MESSAGE,
//					"Rivedere Misure Cautelari o Posizione Giuridica !");

		// ==============================
		// Recupero la PENA COMPLESSIVA
		// ==============================
		PenaComplessivaModel lPenMod = new PenaComplessivaModel();
		lPenMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		IPenaComplessiva lPCon = SIEPLookupRemote.getPenaComplessivaRemote();
		Vector lPComples = lPCon.ExRicercaPenaComplessivaNoError(lPenMod);

		if (lPComples.size() == 0) { // IMPOSSIBILE
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Complessiva mancante");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		lPenMod = (PenaComplessivaModel) (lPComples.get(0));

		// ==========================================================================
		// Recupero la data per i calcoli della fungibilità:
		// - data di scarcerazione se passata in input
		// - data di systema altrimenti
		// ==========================================================================
		Date dataSistemaPerCalcoli = null;
		Date dataScarcerazione = null;

		if (!isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE)) {
			dataScarcerazione = getRequestDateParameter(
					ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE,
					ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE,
					ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Data scarcerazione = " + dataScarcerazione);
		if (dataScarcerazione != null) {
			dataSistemaPerCalcoli = dataScarcerazione;
		} else {
			dataSistemaPerCalcoli = DateUtils.getSysDate();
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("dataSistemaPerCalcoli = " + dataSistemaPerCalcoli);

		// ==========================================================================
		// Recupero la data inizio pena per i calcoli delle date di decorrenza:
		// La data inizio pena è data da: dataInizio dell'ultima pena residua VALIDATA
		// se esiste, altrimenti dell'ultima non validata, altrimenti viene recuperata
		// come nel primo calcolo
		// ==========================================================================
		Date lDataInizioPena = null;

		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lUltimaPenResVal = new PenaResiduaModel();
		lUltimaPenResVal = lPenResCtrl.ExRicercaPenaResiduaUltimaByDate(lIdFascicolo);
		// if (lUltimaPenResVal == null)
		// { // se non presente una pena validata utilizzo l'ultima in assoluto NO!
		// // non ha senso! Se non esiste una validata tanto vale ricalcolarla come
		// // se fosse il primo calcolo della pena ricalcolando la data inizio.
		// // Se si usa la data inizio pena dll'ultima non validata si rischia di
		// // agganciare un calcolo intermedio errato effettuato prima della modifica
		// // della posizione giuridica
		// lUltimaPenResVal = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		// }
		if (lUltimaPenResVal == null) { // Non esiste proprio un pena validata non è mai stato fatto il primo
										// calcolo
										// della pena lo effettuo ora per avere dati da visualizzare sulla
										// form
										// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
										// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Pena residua assente effettuo il primo calcolo");
			ActCalcoloPenaMain lActCalcoloPenaMain = new ActCalcoloPenaMain();
			CalcoloPenaModel lCalcPenaModel = lActCalcoloPenaMain.calcoloPena(lIdFascicolo, null);
			lDataInizioPena = lActCalcoloPenaMain.getDataPrimoCalcolo(lIdFascicolo);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Data inizio pena: " + lDataInizioPena);
			try {
				lUltimaPenResVal = lCalcPenaModel.getPenaDaEspiare(lDataInizioPena, null, null);
			} catch (Exception e) {
				// pezza da togliere serve solo per gestire la catch
				throw new F3BException(e);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ultima pena residua VALIDATA = " + lUltimaPenResVal);

		if (lUltimaPenResVal == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("ATTENZIONE! non esiste una pena validata a sistema, impossibile determinare la Data Inizio Pena");
		} else if (lUltimaPenResVal.getDataInizio() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("ATTENZIONE! Pena Validata a sistema senza data inizio pena assente, impossibile determinare la Data Inizio Pena");
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Calcolo la pena espiata");
			lDataInizioPena = lUltimaPenResVal.getDataInizio();
		}

		// ==========================================================================
		// Solo se non ergastolo viene rieffettuato il vero calcolo della pena,
		// vale a dire vengono:
		// - ricalcolati i quantum
		// - le date di decorrenza
		// - l'eventuale fungibilità
		//
		// Se ergastolo invece:
		// - copia i dati dell'ultima pena
		// ==========================================================================
		if (!(lPenMod.getCodTipoPenaDetentiva().equals("03") || lPenMod.getCodTipoPenaDetentiva()
				.equals("04"))) {
			// ========================================================================
			// Recupero i quantum di pena Validati che concorrono alla calcolo della
			// pena (n.b. non vengono recuperati i dati correnti da computare in
			// quanto non validati)
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
			ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
			CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

			// ========================================================================
			// Recupero i quantum di Benefici Concessi/Revocati per i quali sto
			// effettuando i calcoli
			// ========================================================================
			String lCodTipoAnnotazione = "014"; // 'Altro'
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("Recupero le Annotazioni manuali correnti (da validare): " + lCodTipoAnnotazione);

			ICalcoloPenaF5 lCalPenCtrlF5 = SIEPLookupRemote.getCalcoloPenaF5();

			Vector lAnnotazioniCorrentiDaComputare = new Vector();
			lAnnotazioniCorrentiDaComputare = lCalPenCtrlF5.exGetAnnotazioniManualiDaComputare(lIdFascicolo,
					lCodTipoAnnotazione, "-", null);

			// ==========================================================================
			// Aggiungo i quantum recuperati al lCalcoloPenaModel
			// ==========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lAnnotazioniCorrentiDaComputare.size()="
					+ lAnnotazioniCorrentiDaComputare.size());

			boolean lIsSoloImporti = true;
			for (int i = 0; i < lAnnotazioniCorrentiDaComputare.size(); i++) {
				AnnotazioneManualeModel lAnnModel = (AnnotazioneManualeModel) lAnnotazioniCorrentiDaComputare
						.elementAt(i);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Annotazione corrente:" + lAnnModel.toString2());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiungo le annotazioni correnti ai dati validati...");

				// ======================================================================
				// Aggiungi il provvedimento al CalcoloPenaModel per utilizzarli nel
				// calcolo totale della pena
				// ======================================================================
				lCalcoloPenaModel.getComputoAltro().add(lAnnModel);

				CalendarModel lCalModRec = lAnnMod.getQuantumReclusione();
				CalendarModel lCalModArr = lAnnMod.getQuantumArresto();

				if (CalendarUtil.getTotGiorni(lCalModRec) > 0 || CalendarUtil.getTotGiorni(lCalModArr) > 0) {
					lIsSoloImporti = false;
				}
			}

			// ==========================================================================
			// Effettuo i calcoli
			// ==========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Effettuo il calcolo della pena con tutti i dati...");
			PenaResiduaModel lPenaRideterminata = null;

			if (lIsSoloImporti && lUltimaPenResVal != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiorno solo gli importi");
				// Effettuo i soli calcoli degli importi.
				// n.b. il metodo rieffettua tutti i calcoli, ma poi prendo per buoni
				// i soli importi e copio tal quali i quantum e le date
				lPenaRideterminata = lCalcoloPenaModel.getPenaDaEspiare(null, null, null);

				// Copio i quantum
				lPenaRideterminata.setNumGiorniReclusione(lUltimaPenResVal.getNumGiorniReclusione());
				lPenaRideterminata.setNumMesiReclusione(lUltimaPenResVal.getNumMesiReclusione());
				lPenaRideterminata.setNumAnniReclusione(lUltimaPenResVal.getNumAnniReclusione());

				lPenaRideterminata.setNumGiorniArresto(lUltimaPenResVal.getNumGiorniArresto());
				lPenaRideterminata.setNumMesiArresto(lUltimaPenResVal.getNumMesiArresto());
				lPenaRideterminata.setNumAnniArresto(lUltimaPenResVal.getNumAnniArresto());

				// Copio le date dell'ultima pena validata
				lPenaRideterminata.setDataInizio(lUltimaPenResVal.getDataInizio());
				lPenaRideterminata.setDataFineReclusione(lUltimaPenResVal.getDataFineReclusione());
				lPenaRideterminata.setDataInizioArresto(lUltimaPenResVal.getDataInizioArresto());
				lPenaRideterminata.setDataFinePresunta(lUltimaPenResVal.getDataFinePresunta());
				lPenaRideterminata.setDataFine(lUltimaPenResVal.getDataFine());

				// annullo l'eventuale fungibilità calcolate se quantum negativi
				lCalcoloPenaModel.setFungibilitaCalcolata(new FungibilitaModel());
			} else {
				lPenaRideterminata = lCalcoloPenaModel.getPenaDaEspiare(lDataInizioPena,
						dataSistemaPerCalcoli, "all");
			}

			FungibilitaModel lFungModel = lCalcoloPenaModel.getFungibilitaCalcolata();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena residua ricalcolata: " + lPenaRideterminata);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fungibilità ricalcolata: " + lFungModel);

			// ==========================================================================
			// Finisco di valorizzare i campi del PenaResiduaModel.
			// n.b. la funzione getPenaDaEspiare valorizza SOLO i quantum e gli importi
			// del model oltre alle date di espiazione (se possibile)
			// ==========================================================================
			lPenaRideterminata.setFasSieIdFascicoloSiep(lIdFascicolo);
			lPenaRideterminata.setEveIdEvento(lAnnMod.getEveIdEvento());
			lPenaRideterminata.setFlagValidato("N");
			lPenaRideterminata.setDiesAQuo("S");
			lPenaRideterminata.setFlagErgastolo("N");
			lPenaRideterminata.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenaRideterminata.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPenaRideterminata.setDataInserimento(DateUtils.getSysDate());
			// devo copiare il flag perchè altrimenti mi perdo l'informazione sullo
			// stato della pena (se sospesa, deve rimanere sospesa)
			if (lUltimaPenResVal != null) {
				lPenaRideterminata.setFlagPenaSospesa(lUltimaPenResVal.getFlagPenaSospesa());
			}

			// ==========================================================================
			// Finisco di valorizzare i campi del FungibilitaModel.
			// n.b. la funzione getFungibilitaCalcolata valorizza SOLO i quantum della
			// fungibilità
			// ==========================================================================
			if (lFungModel != null) {
				// La fungibilità è legata all'evento e al fascicolo
				lFungModel.setFasSieIdFascicoloSiep(lIdFascicolo);
				lFungModel.setEveIdEvento(lAnnMod.getEveIdEvento());

				lFungModel.setCodTipoFungibilita("02"); // 02=PENA ESPIATA IN ECCESSO
				lFungModel.setFlagValidato("N");

				lFungModel.setCodOperatoreInserimento(getCodUtenteConnesso());
				lFungModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lFungModel.setDataInserimento(DateUtils.getSysDate());
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena residua ricalcolata: " + lPenaRideterminata);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fungibilità ricalcolata: " + lFungModel);

			// ==========================================================================
			// Inserisco i dati a sistema PenaResidua-Fungibilità
			// ==========================================================================
			lPenResCtrl.ExInsertOrUpdatePenaResidua(lPenaRideterminata);
			CalendarUtil lCalUtil = new CalendarUtil();

			// !!!!!!!!! Da verificare come si comportava com la fungibilità, in
			// questo caso infatti l'utente non interagisce
			if (!lCalUtil.isZero(lFungModel.getQuantumFungibilita())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserisco la fungibilità");
				IFungibilita lFungCtrl = SIEPLookupRemote.getFungibilitaRemote();
				lFungModel = lFungCtrl.ExInserisciFungibilita(lFungModel);
			}
		} else // ERGASTOLO
		{
			PenaResiduaModel lPenRes = new PenaResiduaModel();

			lPenRes = new PenaResiduaModel();

			Date lDataFinePena = DateUtils.getDate(9999, 12, 31);

			if (lPenMod.getCodTipoPenaDetentiva().equals("03")) {
				lPenRes.setFlagErgastolo("S");
			} else if (lPenMod.getCodTipoPenaDetentiva().equals("04")) {
				lPenRes.setFlagErgastolo("D");
			}

			lPenRes.setDataInizio(lDataInizioPena);
			lPenRes.setDataFine(lDataFinePena);

			lPenRes.setDataInizioIsolamentoDiurno(lPenMod.getDataInizioIsolamentoDiurno());
			lPenRes.setDataFineIsolamentoDiurno(lPenMod.getDataFineIsolamentoDiurno());

			lPenRes.setNumGiorniIsolamentoDiurno(lPenMod.getNumGiorniIsolamentoDiurno());
			lPenRes.setNumMesiIsolamentoDiurno(lPenMod.getNumMesiIsolamentoDiurno());
			lPenRes.setNumAnniIsolamentoDiurno(lPenMod.getNumAnniIsolamentoDiurno());

			lPenRes.setFlagValidato("N");
			lPenRes.setDiesAQuo("S");

			// devo copiare il flag perchè altrimenti mi perdo l'informazione sullo
			// stato della pena (se sospesa, deve rimanere sospesa)
			if (lUltimaPenResVal != null) {
				lPenRes.setFlagPenaSospesa(lUltimaPenResVal.getFlagPenaSospesa());
			}

			lPenRes.setFasSieIdFascicoloSiep(lIdFascicolo);

			lPenRes.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenRes.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPenRes.setDataInserimento(DateUtils.getSysDate());

			lPenResCtrl.ExInsertOrUpdatePenaResidua(lPenRes);
		}

		return null;
	}

}