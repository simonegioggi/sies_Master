package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.sollecitoesitotrasmissione.controller.ISollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * Action per la visualizzazione del dettaglio del sollecito esito trasmissione
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadDettaglioSollecitoEsitoTrasmissione extends ActSIESDettaglioProvvedimento implements
		ICostantiMisuraSicurezza {

	public String processRequest() throws F3BException {

//		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

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
		UfficioModel lUffDestinataro = getUfficioByCodUfficio(lEveNotMod.getEvento()
				.getCodUfficioDestinatario());
		setRequestAttribute("ufficioDestinatario", lUffDestinataro);

		return PG_DETTAGLIO_SOLLECITO_ESITO;
	}

}