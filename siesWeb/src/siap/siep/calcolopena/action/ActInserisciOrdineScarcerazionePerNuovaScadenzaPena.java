package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordinescarcerazione.controller.IOrdineScarcerazione;
import siap.siep.util.MinorMask;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciOrdineScarcerazionePerNuovaScadenzaPena
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento dell'Ordine di Scarcerazione per nuova scadenza pena
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
public class ActInserisciOrdineScarcerazionePerNuovaScadenzaPena extends ActionSiap
		implements ICostantiAnnotazioneManuale {

	/**
	 * Azione di Inserimento del
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		EventoModel lEve = new EventoModel();

		lEve.setCodTipoEvento("01"); // PROVVEDIMENTO
		lEve.setCodTipoProvvedimento("09"); // ORDINE SCARCERAZIONE
		lEve.setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
		lEve.setCodUfficioEmittente(lCodiceUfficio);
		lEve.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.setDataEmissione(lDataEmissione);
		lEve.setCodEsito("-");
		// lEve.setFlagPiuMeno();

		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		lEve.setDataTrasmissioneAtti(lDataTrasmissione);
		// lEve.setDataRicezioneAtti();
		// lEve.setCodUfficioDestinatario();
		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		// lEve.setProgrProtocollo();
		lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEve.setCodLuogoDestinatario("-");

		// *** Il FlagDocumentoRegistrato(null) INDICA CHE IL DOCUMENTO NON E' PRESENTE
		lEve.setFlagDocumentoRegistrato(null);
		lEve.setCodMagistrato(calcolaMagistrato());
		lEve.setCodTipoUfficioDestinatario("-");

		lEve.setFlagStampaSiep("S");
		lEve.setFlagVideoSiep("S");

		// collego lìordine di scarcerazione al provvedimneto
		lEve.setEveIdEvento(getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO));

		lEve.setCodOperatoreInserimento(lCodiceOperatore);
		lEve.setCodUfficioInserimento(lCodiceUfficio);
		lEve.setDataInserimento(DateUtils.getSysDate());

		lEve.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEve.setCodUfficioAggiornamento(lCodiceUfficio);
		lEve.setDataAggiornamento(DateUtils.getSysDate());

		NotificaModel[] lNotifiche = setNotifiche(lEve);

		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.setEvento(lEve);
		lEveNot.setNotifiche(lNotifiche);

		IOrdineScarcerazione lCtrl = SIEPLookupRemote.getOrdineScarcerazione();
		EventoNotificaModel lRetModel = lCtrl.ExInserisciOModificaEventoNotifica(lEveNot);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.calcolopena.action.ActDettaglioOrdineScarcerazionePerNuovaScadenzaPena&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

	/**
	 * calcolaMagistrato
	 *
	 * @return
	 */
	protected String calcolaMagistrato() throws F3BException {

		String lCodiceMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

		if (lCodiceMagistrato.compareTo("") == 0) {
			MagistratoModel lMagMod = new MagistratoModel();
			lMagMod.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME).toUpperCase());
			lMagMod.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME).toUpperCase());

			IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
			Vector lVect = new Vector();
			try {
				lVect = lCtrl.ExRicercaMagistrato(lMagMod);
			} catch (F3BException exF3b) {
				throw new F3BException(F3BException.USER_MESSAGE, "Magistrato Inesistente");
			}

			lMagMod = (MagistratoModel) lVect.firstElement();
			lCodiceMagistrato = lMagMod.getCodMagistrato();
		}

		return lCodiceMagistrato;
	}

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 *
	 * @param lKey
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotifiche(EventoModel lEve) throws F3BException {

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		ArrayList lNotifiche = new ArrayList();

		// SETTO UDS
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_UFF_COD_UFFICIO)
				// MEV 37 - Inizio
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_UFF_COD_UFFICIO).equals("")) {
			// MEV 37 - Fine
			NotificaModel lNotModTDS = new NotificaModel();
			String lUDS = getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiNotifica.CAMPO_UFF_COD_UFFICIO));
			String lNoteUDS = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_UFF);
			lNotModTDS.setCodTipoNotifica("N");
			lNotModTDS.setNote(lNoteUDS);
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setDataInvio(lEve.getDataEmissione());
			lNotModTDS.setUffCodUfficio(lUDS);

			// Ticket#202003040112 - SIEP - Errre in provvedimento revoca sentenza per abolizione reato:
			// aggiunto controllo di consistenza
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO))
				lNotModTDS.setCodUffUdsUdsm(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO));

			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotifiche.add(lNotModTDS);
		}

		// SETTO NOTIFICA AUTORITA E
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
			String lCodTipo = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			String lSede = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			String lNote = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			NotificaModel lNotMod = new NotificaModel();
			lNotMod.setCodTipoNotifica("E");
			// lNotMod.setDataAvvenutaNotifica();
			lNotMod.setDataInvio(lEve.getDataTrasmissioneAtti());
			lNotMod.setCodEsito("-");
			lNotMod.setNote(lNote);
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lCodTipo);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSede));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setAutoritaEsterna(lAut);
			lNotifiche.add(lNotMod);
		}

		// SETTO CSSA
		// Ticket#202003040112 - SIEP - Errre in provvedimento revoca sentenza per abolizione reato:
		// modificato controllo di consistenza
		if (!isRequestParameterNullEmptyObj(ICostantiCSSA.CAMPO_ID_CSSA)) {
			NotificaModel lNotModCSSA = new NotificaModel();
			BigDecimal lCssa = getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);
			String lNoteCssa = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_CSSA);
			// Ticket#202003040112 - SIEP - Errre in provvedimento revoca sentenza per abolizione reato:
			// aggiunto controllo di consistenza
			if (!isRequestParameterNullObj(MinorMask.ComboCSSAId))
				lNotModCSSA.setCodUffUepeUssmSS(getRequestStringParameter(MinorMask.ComboCSSAId));

			lNotModCSSA.setCodTipoNotifica("N");
			lNotModCSSA.setNote(lNoteCssa);
			lNotModCSSA.setCodEsito("-");
			lNotModCSSA.setDataInvio(lEve.getDataEmissione());
			lNotModCSSA.setCssIdCssa(lCssa);
			lNotModCSSA.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(lCodiceUfficio);
			lNotifiche.add(lNotModCSSA);
		}

		// ******************************
		String lTipoNotificaIst = "E";
		if (!isRequestParameterNullObj("FlagMisuraAlternativa"))
			lTipoNotificaIst = "N";

		// SETTO ISTITUTO DETENZIONE
		if ((!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))) {
			NotificaModel lNotModIst = new NotificaModel();

			String lIstituto = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
			String lNoteIstituto = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_NOTE);

			lNotModIst.setCodTipoNotifica(lTipoNotificaIst);
			// lNotModIst.setDataAvvenutaNotifica();
			lNotModIst.setDataInvio(lEve.getDataEmissione());
			lNotModIst.setCodEsito("-");
			lNotModIst.setNote(lNoteIstituto);
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotModIst.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(lCodiceUfficio);

			lNotifiche.add(lNotModIst);
		}

		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

}