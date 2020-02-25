package siap.siep.ripristino.action;

/**
 * <p>Title: ActInserisciProvvedimentoRipristino</p>
 * <p>Description: Classe Action per l'inserimento Provvedimento Ripristino</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordinescarcerazione.controller.IOrdineScarcerazione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciProvvedimentoRipristino extends ActionSiap implements ICostantiRipristino {

	/**
	 * Azione di Inserimento del Provvedimento di Ripristino
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		EventoModel lEve = new EventoModel();

		lEve.setCodTipoEvento("01"); // PROVVEDIMENTO
		// Cod Tipo Provvedimento passa da 04 a 09. Luigi 5-10-2005
		lEve.setCodTipoProvvedimento("09"); // PROVVEDIMENTO
		lEve.setCodMotivo("0272");
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

		// lEve.setEveIdEvento(getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO));

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
				+ "=siap.siep.ripristino.action.ActDettaglioProvvedimentoRipristino&"
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

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		ArrayList lNotifiche = new ArrayList();

		// SETTO ISTITUTO DETENZIONE
		if ((!this
				.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))) {
			NotificaModel lNotModIst = new NotificaModel();

			String lIstituto = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
			String lNoteIstituto = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_NOTE);

			lNotModIst.setCodTipoNotifica("E");
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