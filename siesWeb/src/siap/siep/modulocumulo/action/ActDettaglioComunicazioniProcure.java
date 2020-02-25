package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sollecitoesitotrasmissione.action.ICostantiSollecitoEsitoTrasmissione;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActDettaglioComunicazioniProcure Action per la visualizzazione del dettaglio delle Comunicazioni di Cumulo
 * alle Procure/Uff. della Sorv. Interessati.
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioComunicazioniProcure extends ActionModuloCumulo
		implements ICostantiModuloCumulo, ICostantiSollecitoEsitoTrasmissione {

	public String processRequest() throws F3BException {

		// FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		if (lIdEvento == null) {
			String lStrIdEvento = this.getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
			lIdEvento = new BigDecimal(lStrIdEvento);
		}

		// =========================================================
		// Recupero l'evento
		// =========================================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

		// =========================================================
		// Recupero le notifiche
		// =========================================================
		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector<NotificaModel> lNotificheCur = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(lIdEvento);
		lEveNotMod.setNotifiche(lNotificheCur.toArray(new NotificaModel[0]));

		this.setRequestAttribute("eventonotifica", lEveNotMod);

		IstruttoriaCumuloModel IstruModel = null;
		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
				&& getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) != null
				&& !getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
						.equals(null)) {
			IstruModel = super.getDatiIstruttoria();
		} else {
			IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			IstruModel = lIstrCtrl
					.ExRicercaIstruttoriaCumuloById(lEveNotMod.getEvento().getIstruIdIstruttoriaCumulo());
		}

		setRequestAttribute("IstruttoriaCumulo", IstruModel);

		// Cerco i dati dei procedimenti SIEP destinatari delle Comunicazioni
		Vector<ProcedimentoCumulatoModel> lProcedimentiCumulati = new Vector<>();
		NotificaModel lNotMod = null;
		ProcedimentoCumulatoModel lProcMod = null;
		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();

		Iterator Itx = lNotificheCur.iterator();
		while (Itx.hasNext()) {
			lNotMod = (NotificaModel) Itx.next();
			if (lNotMod != null && lNotMod.getCurIdCuratore() != null) {
				lProcMod = lCtrlT.ExRicercaProcedimentoCumulatoByIdTitolo(lNotMod.getCurIdCuratore());

				if (lProcMod != null && lProcMod.getIdProcedimentoCumulato() != null) {
					if ("S".equals(lProcMod.getFlagAccorpato())) {
						UfficioModel lUfficioOrigine = getUfficioByCodUfficio(
								lProcMod.getChiaveUfficioOrigine());
						lProcMod.setUfficioOrigine(lUfficioOrigine);
					}

					lProcedimentiCumulati.add(lProcMod);
				}
			}
		}

		setRequestAttribute("ProcedimentiCumulati", lProcedimentiCumulati);

		return PG_LOAD_DETTAGLIO_COMUNICAZIONI_PROCURE;
	}

}