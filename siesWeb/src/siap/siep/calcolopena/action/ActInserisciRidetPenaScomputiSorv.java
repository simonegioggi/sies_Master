package siap.siep.calcolopena.action;

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
import siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciRidetPenaScomputiSorv
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di rideterminazione pena Scomputi Permessi
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
public class ActInserisciRidetPenaScomputiSorv extends ActRidetPena {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Effettua l'inserimento del provvedimento di rideterminazione pena per Scomputo Permessi. menu:
	 * 'Decisioni Sorveglianza - Scomputo Permesso' Non viene più effettuato il calcolo della pena in questa
	 * fase ma solo l'annotazione del computo (01-25-xxxx) e delle Annotazioni Manuali che da questa versione
	 * possono essere più di una.
	 * 
	 * Inserisce: - Annotazione (01-25)
	 * 
	 * @return la pagina di dettaglio
	 */
	/***************************************************************************
	 * Genera il singolo evento (Ordinanza)
	 * 
	 * @return
	 * @throws F3BException
	 */

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lFascicoloMod=" + lFascicoloMod);
		lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("=1=");
		String lIScomputo = getRequestStringParameter(
				ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("=2= lIScomputo=" + lIScomputo);

		String lCodiceOperatore = this.getCodUtenteConnesso();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("=3= lCodiceOperatore=" + lCodiceOperatore);
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("=4= lCodiceUfficio=" + lCodiceUfficio);

		// Autorità emittente
		String lCodTipoUff = getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE + "_AA");
		String lDescrComune = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA");

		ComuneModel lComune = this.getCodComuneByDescr(
				getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA"));
		String lCodLuogoEmittente = lComune.getCodComune();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lCodTipoUff        = " + lCodTipoUff);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lCodLuogoEmittente = " + lCodLuogoEmittente);
		String lCodUffEmittente = this.getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff, lDescrComune);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lCodUffEmittente   = " + lCodUffEmittente);

		// ==========================================================================
		// Recupero i dati del Provvedimento di Computo
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Preparo l'evento Annotazione...");

		EventoModel lEveProvvedimentoMod = new EventoModel();

		lEveProvvedimentoMod.setCodTipoEvento("01");
		lEveProvvedimentoMod.setCodTipoProvvedimento("25"); // 25 - Annotazione
		lEveProvvedimentoMod
				.setCodMotivo(this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO + "_AB"));

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

		// scrivo pure l'id evento della sorveglianza
		lEveProvvedimentoMod.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		// Se la pena non è in decorrenza inserisco l'evento come già validato
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lUltimaPenResVal = lPenResCtrl.ExRicercaPenaResiduaUltimaByDate(lIdFascicolo);
		if (lUltimaPenResVal == null || lUltimaPenResVal.getDataInizio() == null
		// MEV29 se ergastolo inserisco l'annotazione validata. Non si può fare altro
				|| lUltimaPenResVal.isErgastolo()) {
			lEveProvvedimentoMod.setFlagDocumentoRegistrato("S");
		}

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

		// ==========================================================================
		// Recupero i dati delle annotazioni manuali/liberazione anticipata
		// Trattasi di computi della Sorveglianza, devo scriverlo su LA
		// ==========================================================================
		Vector lListaAnnotazioni = new Vector();
		LicenzaLibAnticipataModel lLicModel = null;
		lLicModel = new LicenzaLibAnticipataModel();

		if (lEveProvvedimentoMod.getCodMotivo().equals("0958")) {
			// Scomputo Permesso
			lLicModel.setCodTipoLicenza("PP");
		} else {
			// Reclamo Scomputo Permesso (0994)
			lLicModel.setCodTipoLicenza("EP");
		}

		lLicModel.setNumeroGiorni(getRequestBigDecimalParameter("GiorniComputo"));
		lLicModel.setFlagConcesso("C"); // Concessi
		lLicModel.setFlagElaborato("N");
		lLicModel.setFlagScomputo("S");
		// lLicModel.setFlagScorta(aValore)

		lLicModel.setFasSieIdFascicoloSiep(lIdFascicolo);
		// lLicModel.setEveIdEvento(aValore); valorizzato dal Ctrl
		// lLicModel.setAnnotazione(aValore);

		lLicModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lLicModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lLicModel.setDataInserimento(DateUtils.getSysDate());

		// dati dell'ordinanza
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"))
			lLicModel.setAnnoOrdinanza(
					getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"));

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"))
			lLicModel.setNumeroOrdinanza(
					getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"));

		lLicModel.setDataEmissioneOrdinanza(
				getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE + "_AA",
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE + "_AA",
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE + "_AA"));

		lLicModel.setCodUfficioEmittente(lCodUffEmittente);
		lLicModel.setCodLuogoEmittente(lCodLuogoEmittente);

		if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS))
			lLicModel.setAnnoSius(
					getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS));

		if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS))
			lLicModel.setNumeroSius(
					getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lLicModel = " + lLicModel);

		// ==========================================================================
		// Dati del Provvedimento Altra Autorità
		// n.b. è a tutti gli effetti un evento
		// ==========================================================================
		EventoModel lEveAltroUff = null;
		if (getRequestStringParameter("TipoOrd").equals("altroUfficio") && (lIScomputo.equals(""))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("TRATTASI DI ALTRO UFFICIO ...");

			lEveAltroUff = new EventoModel();

			lEveAltroUff.setFasSieIdFascicoloSiep(lIdFascicolo);
			lEveAltroUff.setCodTipoEvento("01");
			lEveAltroUff.setCodTipoProvvedimento(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO + "_AA"));

			lEveAltroUff.setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO + "_AA"));

			/*
			 * if (this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO+"_AA").equals("0958")){
			 * lEveAltroUff.setCodMotivo("2250"); } else { if
			 * (this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO+"_AA").equals("0994"))
			 * lEveAltroUff.setCodMotivo("0039"); }
			 */
			lEveAltroUff.setFlagDocumentoRegistrato("S"); // Per ora lo inserisco validato
			lEveAltroUff.setFlagStampaSiep("S");
			lEveAltroUff.setFlagVideoSiep("S");

			lEveAltroUff.setCodUfficioEmittente(lCodUffEmittente);
			lEveAltroUff.setCodLuogoEmittente(lCodLuogoEmittente);

			lEveAltroUff.setDataEmissione(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE + "_AA"));
			lEveAltroUff.setDataRicezioneAtti(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI + "_AA",
							ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI + "_AA",
							ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI + "_AA"));

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"))
				lEveAltroUff.setAnnoProtocollo(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"));

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"))
				lEveAltroUff.setProgrProtocollo(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"));

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

		// ==========================================================================
		// Inserimento dei dati
		// ==========================================================================
		IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
		EventoModel lEventoIns = null;
		lEventoIns = lAnnManCtrl.ExInserisciEventoAnnotazioni(lEveProvvedimentoMod, lListaAnnotazioni,
				lCampoNota, lEveAltroUff, lLicModel);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("HO ESEGUITO  ExInserisciEventoAnnotazioni RITORNA EVENTO=" + lEventoIns);

		// ==========================================================================
		// Restituisce la pagina di dettaglio
		// ==========================================================================
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.calcolopena.action.ActLoadDettaglioRidetPenaScomputiSorv&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEventoIns.getIdEvento();
		return lPage;
	}

}