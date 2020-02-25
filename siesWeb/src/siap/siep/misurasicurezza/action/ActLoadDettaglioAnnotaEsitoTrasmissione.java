package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazioneesitotrasmissione.action.ICostantiAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.controller.IAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * Action per il caricamento del dettaglio dell'annotazione esito trasmissione
 * 
 * @author d.fiorletta
 */
public class ActLoadDettaglioAnnotaEsitoTrasmissione extends ActSIESDettaglioProvvedimento implements
		ICostantiAnnotazioneEsitoTrasmissione, ICostantiMisuraSicurezza {

	public String processRequest() throws F3BException {

//		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		if (lIdEvento == null) {
			String lStrIdEvento = this.getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
			lIdEvento = new BigDecimal(lStrIdEvento);
		}

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagMod);

		// Recupero le annotazioni
		AnnotazioneEsitoTrasmissioneModel lAnnotaModel = null;
		IAnnotazioneEsitoTrasmissione lCtrlAnnot = SIEPLookupRemote.getAnnotazioneEsitoTrasmissioneRemote();
		lAnnotaModel = lCtrlAnnot.ExRicercaAnnotazioneEsitoTrasmissioneByIdEvento(lEveMod.getEvento()
				.getIdEvento());

		this.setRequestAttribute("annotazioneEsito", lAnnotaModel);

		if (lAnnotaModel.getCodUfficioEsito() != null) {
			UfficioModel lUffEsito = getUfficioByCodUfficio(lAnnotaModel.getCodUfficioEsito());
			this.setRequestAttribute("ufficioEsito", lUffEsito);
		}

		if (lAnnotaModel.getCodUfficioInoltro() != null) {
			UfficioModel lUffInoltro = getUfficioByCodUfficio(lAnnotaModel.getCodUfficioInoltro());
			this.setRequestAttribute("ufficioInoltro", lUffInoltro);
		}

		return PG_DETTAGLIO_ANNOTAZIONE_ESITO;
	}

}