package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Title: 		ActInserisciAnnotazioneDecisioneDellaSorveglianza
 * Description: Inserimento della Annotazione della
 * 				Sorveglianza dopo la Accertamento di pericolosità Sociale
 * Copyright: 	Copyright (c) 2014
 *
 * @author AMBROS
 */
public class ActInserisciAnnotazioneDecisioneDellaSorveglianza extends ActionSiap
		implements ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Evento
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// recupero dati Magistrato competente per fascicolo SIEP
		MagistratoCompetenteMagistratoModel lMagCoMag = new MagistratoCompetenteMagistratoModel();
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		lMagCoMag = lMagCtrl.ExRicercaMagistratoCompetenteByFascicolo(lFascicoloModel.getIdFascicoloSiep());

		// String lCodPosGiu =
		// getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI);

		// kEY Evento selezionato dalla lista Ordinanze/Decreti --
		String ordEveKey = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		EventoModel leveMod = new EventoModel();
		// cerco Evento per avere il COdice Motivo
		IEvento lCtrlE = SICOLookupRemote.getEventoRemote();
		leveMod = lCtrlE.ExRicercaEventoByKey(new BigDecimal(ordEveKey));
		if (leveMod != null && leveMod.getIdEvento() != null) {
			siesLogger.debug("ID_EVENTO = " + leveMod.getIdEvento());
		} else {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Ricerca Evento selezionato dalla lista Fallita");
			return IWebConstants.PG_MESSAGE;
		}

		// Ricerca Misure sicurezza già presenti nel fascicoloSIEP
		List lListMis = new ArrayList();
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		try {
			lListMis = lMisCtrl
					.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascicoloModel.getIdFascicoloSiep());
		} catch (F3BException e) {
			siesLogger.error("ERRORE: " + e.getMessage());
		}

		EventoNotificaModel lEve = new EventoNotificaModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();

		// Ricerca del Codice su Cg_Ref_Codes:
		// --> prendo il cod_alt_2 relativo al Cod_Motivo dell'ordinanza e Diventa il cod_Motivo di questo
		// provvedimento
		DecodificheModel lModel = new DecodificheModel();
		Vector lVec = new Vector(DecodificheManager.getInstance().getMotivoProvvedimento());
		Iterator Ite1 = lVec.iterator();
		String lCod = "";
		while (Ite1.hasNext()) {
			lModel = (DecodificheModel) Ite1.next();
			if (lModel.getCode().equals(leveMod.getCodMotivo())) {
				lCod = lModel.getCodiceAlt2();
			}
		}

		if (!lCod.equals("")) {
			lEve.getEvento().setCodMotivo(lCod);
		} else {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Codice Annotazione Ordinanza Errato ");
			return IWebConstants.PG_MESSAGE;
		}
		// --->
		lEve.getEvento().setCodTipoEvento("01");
		lEve.getEvento().setCodTipoProvvedimento("25");
		lEve.getEvento().setFlagStampaSiep("S");
		lEve.getEvento().setFlagVideoSiep("S");

		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEve.getEvento().setDataEmissione(lDataEmissione);

		lEve.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lCodiceUfficio);

		lEve.getEvento().setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEve.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		lEve.getEvento().setCodMagistrato(lMagCoMag.getMagistrato().getCodMagistrato());

		lEve.getEvento().setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		EventoNotificaModel lRetModel = new EventoNotificaModel();
		ArrayList lNotificheArray = new ArrayList();

		// Notifiche
		NotificaModel lNotModUDS = new NotificaModel();

		lNotModUDS.setCodEsito("-");
		lNotModUDS.setCodOperatoreInserimento(lCodiceOperatore);
		lNotModUDS.setDataInserimento(DateUtils.getSysDate());
		lNotModUDS.setCodUfficioInserimento(lCodiceUfficio);
		lNotModUDS.setCodTipoNotifica("MS");
		lNotModUDS.setUffCodUfficio(lCodiceUfficio);
		lNotModUDS.setDataInvio(lDataEmissione);

		lNotificheArray.add(lNotModUDS);

		NotificaModel[] lNotifiche = (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);

		// Pena residua (04/11/2014 va impostata = null se assente)
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA)) {
			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
		} else
			lPenaRes = null;

		// ---> INSERIMENTO PROVVEDIMENTO DI ANNOTAZIONE
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		lEve.setNotifiche(lNotifiche);

		// Ricerca Codice Tipo Misura per il provvedimento selezionato.
		// --> (cerca il corrispondente Cod_Alt_2 relativo al codice esito dell'evento)
		String lCodTipoMisura = "";
		try {
			lCodTipoMisura = lMisCtrl.ExRicercaCodTipoMisuraByIdEvento(
					getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		} catch (F3BException e) {
			siesLogger.error("ERRORE: " + e.getMessage());
		}

		// Dati Provvedimenti SIUS
		Vector VecMisNewSius = new Vector();
		/*
		 * 06/02/2015 Modificato criterio di recupero informazioni Misure di Sicurezza. Il COD_ESITO
		 * dell'evento selezionato viene incrociato col corrispondente valore di RV_LOW_VALUE del Dominio
		 * ESITO_PROVVEDIMENTO in CG_REF_CODES. Solo se il valore di RV_ALT2_VALUE incrociato è = "MSI" si
		 * opera l'inserimento della nuova misura.
		 */
		if (lCodTipoMisura != null && lCodTipoMisura.compareTo("MSI") == 0) {
			VecMisNewSius = lMisCtrl.ExRicercaMisuraSicurezzaByEventoKey(
					getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			if (VecMisNewSius != null && VecMisNewSius.size() > 0) {
				siesLogger.info("Codice Tipo Misura: " + lCodTipoMisura);
				siesLogger.info("EVENTO: " + getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
				siesLogger.info(
						"ExRicercaMisuraSicurezzaByEventoKey --> Totale: " + VecMisNewSius.size() + " MS!");
			} else {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				siesLogger.error(
						"La Ricerca Misure di Sicurezza legate al provvedimento e' Fallita per Evento: "
								+ getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"La Ricerca Misure di Sicurezza legate al provvedimento e' Fallita");
				lRedirigi.setAction(
						"siap.siep.misurasicurezza.action.ActLoadInserisciAnnotazioneDecisioneDellaSorveglianza&"
								+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			}
			lRetModel = lCtrl.ExInserisciOENotificaMisSic(lEve, lPenaRes, lListMis, VecMisNewSius);
		} else
			lRetModel = lCtrl.ExInserisciAnnNotifica(lEve, lPenaRes, ordEveKey);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActDettaglioAnnotazioneDecisioneDellaSorveglianza&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
	} // Chiude processRequest

} // Chiude Action