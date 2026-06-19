package siap.siep.modulocumulo.action;

import org.apache.log4j.Logger;
import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.sollecitoesitotrasmissione.action.ICostantiSollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.controller.ISollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActDettaglioSollecitoRichiestaAttiTrasfCompCumulo Action per la visualizzazione del dettaglio del sollecito
 * della Richiesta Atti per Competenza Cumulo
 */
public class ActDettaglioSollecitoRichiestaAttiTrasfCompCumulo extends ActionModuloCumulo
		implements ICostantiModuloCumulo, ICostantiSollecitoEsitoTrasmissione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

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
			IstruModel = (IstruttoriaCumuloModel) super.getDatiIstruttoria();
		} else {
			IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			IstruModel = lIstrCtrl
					.ExRicercaIstruttoriaCumuloById(lEveNotMod.getEvento().getIstruIdIstruttoriaCumulo());
		}

		setRequestAttribute("IstruttoriaCumulo", IstruModel);

		// =========================================================
		// Recupero i dati del sollecito
		// =========================================================
		ISollecitoEsitoTrasmissione lCtrlSollecito = SIEPLookupRemote.getSollecitoEsitoTrasmissioneRemote();
		SollecitoEsitoTrasmissioneModel lSollecitoModel = lCtrlSollecito
				.ExRicercaSollecitoEsitoTrasmissioneByIdEvento(lEveNotMod.getEvento().getIdEvento());
		this.setRequestAttribute("sollecitoEsito", lSollecitoModel);

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagMod);

		// Ufficio Destinatario del sollecito
		UfficioModel lUffDestinataro = getUfficioByCodUfficio(
				lEveNotMod.getEvento().getCodUfficioDestinatario());
		setRequestAttribute("ufficioDestinatario", lUffDestinataro);

		return PG_DETTAGLIO_SOLLECITO_RICHIESTA_ATTI_TC_CUMULO;
	}

}
