package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Collection;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sollecitoesitotrasmissione.controller.ISollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * Action di modifica del sollecito esito trasmissione
 * 
 * @author d.fiorletta
 *
 */
public class ActModificaSollecitoTrasmissione extends ActSIESDettaglioProvvedimento implements
		ICostantiMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

//		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		if (lIdEvento == null) {
			String lStrIdEvento = this.getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
			lIdEvento = new BigDecimal(lStrIdEvento);
		}

		// =========================================================
		// Recupero i dati dell'evento
		// =========================================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("evento", lEveNotMod.getEvento());

		// =========================================================
		// Recupero i dati del sollecito
		// =========================================================
		ISollecitoEsitoTrasmissione lCtrlSollecito = SIEPLookupRemote.getSollecitoEsitoTrasmissioneRemote();
		SollecitoEsitoTrasmissioneModel lSollecitoModel = lCtrlSollecito
				.ExRicercaSollecitoEsitoTrasmissioneByIdEvento(lEveNotMod.getEvento().getIdEvento());
		this.setRequestAttribute("sollecitoEsito", lSollecitoModel);

		// Recupero il messaggio di richiesta Originario
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessaggioRich = lCrtl.ExRicercaMessaggioByKey(lSollecitoModel
				.getMesIdMessaggioSollecitato());
		setRequestAttribute("messaggioRich", lMessaggioRich);

		// Recupero l'eventuale comunicazione di inoltro
		MessaggioModel lMessaggioInoltro = null;
		if (lSollecitoModel.getMesIdMessaggioInoltro() != null) {
			lMessaggioInoltro = lCrtl.ExRicercaMessaggioByKey(lSollecitoModel.getMesIdMessaggioInoltro());
			setRequestAttribute("messaggioInoltro", lMessaggioInoltro);
		}

		String lCodUfficioDaSollecitare = null;
		if (lSollecitoModel.getMesIdMessaggioInoltro() != null) {
			lCodUfficioDaSollecitare = lMessaggioInoltro.getCodUfficioInoltro();
		} else {
			lCodUfficioDaSollecitare = lMessaggioRich.getCodUfficioDestinatario();
		}

		// ==========================
		// Contenuto
		// ==========================
		if (lEveNotMod.getCampoNote() != null && lEveNotMod.getCampoNote().length > 0
				&& lEveNotMod.getCampoNote()[0] != null && lEveNotMod.getCampoNote()[0].getDescr() != null) {
			setRequestAttribute("contenuto", lEveNotMod.getCampoNote()[0].getDescr());
		}

		// ========================
		// Dati per le combo
		// ========================
		UfficioModel lUfficio = getUfficioByCodUfficio(lCodUfficioDaSollecitare);
		setRequestAttribute("ufficioSollecito", lUfficio);

		//
		Collection lMotiviSollecto = DecodificheManager.getInstance().getMotivoSollecitoMisureSicurezza();
		DecodificheModel lOggettoProvvedimento = (DecodificheModel) lMotiviSollecto.toArray()[0];
		setRequestAttribute("oggettoProvvedimento", lOggettoProvvedimento);

		// MAGISTRATO COMPETENTE
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());
		MagistratoCompetenteMagistratoModel lMagCompModel = new MagistratoCompetenteMagistratoModel();
		lMagCompModel.setMagistrato(lMagMod);
		setRequestAttribute("magistratocompetente", lMagCompModel);

		// ALTRO DESTINATARIO x la notifica
		AutoritaEsternaModel lAutEst = null;
		NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
		if (lNotifiche != null) {
			for (int i = 0; i < lNotifiche.length; i++) {
				NotificaModel lNotifica = lNotifiche[i];
				if ("C".equals(lNotifica.getCodTipoNotifica()) && lNotifica.getAutoritaEsterna() != null) {
					lAutEst = lNotifica.getAutoritaEsterna();
				}
			}
		}

		Option lAEOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		if (lAutEst != null) {
			lAEOption.setSelected(lAutEst.getCodTipoAutorita());
			setRequestAttribute("autoritaEsternaNSede", lAutEst.getDescrSede());
		}
		setRequestAttribute("autoritaEsternaN", "" + lAEOption);

		setRequestAttribute("modalita", "M");

		return PG_LOAD_INSERISCI_SOLLECITO_ESITO;
	}

}