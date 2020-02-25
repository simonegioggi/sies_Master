package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.jms.ICostantiJMS;
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
import siap.siep.annotazioneesitotrasmissione.controller.IAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActModificaAnnotaEsitoTrasmissione extends ActSIESDettaglioProvvedimento implements
		ICostantiMisuraSicurezza {

	public String processRequest() throws F3BException {

//		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		if (lIdEvento == null) {
			String lStrIdEvento = this.getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
			lIdEvento = new BigDecimal(lStrIdEvento);
		}

		//
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("evento", lEveNotMod.getEvento());

		// Annotazione Esito
		IAnnotazioneEsitoTrasmissione lCtrlAnnotaz = SIEPLookupRemote.getAnnotazioneEsitoTrasmissioneRemote();
		AnnotazioneEsitoTrasmissioneModel lAnnotazioneEsito = null;
		lAnnotazioneEsito = lCtrlAnnotaz.ExRicercaAnnotazioneEsitoTrasmissioneByIdEvento(lIdEvento);
		setRequestAttribute("annotazioneEsito", lAnnotazioneEsito);

		if (lAnnotazioneEsito.getMesIdMessaggioEsito() != null) {
			IMessaggio lCrtlMess = JMSLookupRemote.getMessaggioRemote();
			MessaggioModel lMessaggioEsito = lCrtlMess.ExRicercaMessaggioByKey(lAnnotazioneEsito
					.getMesIdMessaggioEsito());

			setRequestAttribute("messaggioEsito", lMessaggioEsito);
		}

		if (lAnnotazioneEsito.getMesIdMessaggioRichiesta() != null) {
			IMessaggio lCrtlMess = JMSLookupRemote.getMessaggioRemote();
			MessaggioModel lMessaggioRichiesta = lCrtlMess.ExRicercaMessaggioByKey(lAnnotazioneEsito
					.getMesIdMessaggioRichiesta());

			setRequestAttribute("messaggioRichiesta", lMessaggioRichiesta);
		}

		// Tipologia Atto: EVENTO.TIPO_PROVVEDIMENTO
		//
		Option lOptionTipoProvv = null;
		lOptionTipoProvv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOptionTipoProvv.setFilter("25");
		lOptionTipoProvv.setSelected(lEveNotMod.getEvento().getCodTipoProvvedimento());
		setRequestAttribute("comboTipoProvv", "" + lOptionTipoProvv);

		// EVENTO.COD_MOTIVO
		Vector<DecodificheModel> lMotiviProvv = new Vector<DecodificheModel>();
		lMotiviProvv.add(new DecodificheModel("5200",
				"Esito Trasmissione Atti per competenza ai fini dell'esecuzione della misura di sicurezza",
				"", "", "", "", "", "", ""));
		Option lOptionMotivi = new Option(lMotiviProvv);
		lOptionMotivi.setSelected(lEveNotMod.getEvento().getCodMotivo());
		setRequestAttribute("comboMotivoProvv", "" + lOptionMotivi);

		// ==========================
		// Contenuto
		// ==========================
		if (lEveNotMod.getCampoNote() != null && lEveNotMod.getCampoNote().length > 0
				&& lEveNotMod.getCampoNote()[0] != null && lEveNotMod.getCampoNote()[0].getDescr() != null) {
			setRequestAttribute("contenuto", lEveNotMod.getCampoNote()[0].getDescr());
		}

		// =====================
		// Ricerca Magistrato
		// =====================
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());
		MagistratoCompetenteMagistratoModel lMagCompModel = new MagistratoCompetenteMagistratoModel();
		lMagCompModel.setMagistrato(lMagMod);
		setRequestAttribute("magistratocompetente", lMagCompModel);

		// ========================
		// Dati per le combo
		// ========================
		Option lOption = null;

		// Tipo UFFICIO Esito
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
		String[] lFiltroUffici = { "-", "PM", "PMM" }; // i destinatari sono solo PM e PMM
		lOption.setFilter(lFiltroUffici);
		if (lAnnotazioneEsito.getCodUfficioEsito() != null
				&& lAnnotazioneEsito.getCodUfficioEsito().trim().length() > 0) {
			UfficioModel lUfficioEsito = getUfficioByCodUfficio(lAnnotazioneEsito.getCodUfficioEsito());
			setRequestAttribute("ufficioEsito", lUfficioEsito);

			lOption.setSelected(lUfficioEsito.getCodTipoUfficio());
		}
		setRequestAttribute("comboUfficiPM", "" + lOption);

		// Ufficio Inoltro
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
		lOption.setFilter(lFiltroUffici);
		if (lAnnotazioneEsito.getCodUfficioInoltro() != null
				&& lAnnotazioneEsito.getCodUfficioInoltro().trim().length() > 0) {
			UfficioModel lUfficioInoltro = getUfficioByCodUfficio(lAnnotazioneEsito.getCodUfficioInoltro());
			setRequestAttribute("ufficioInoltro", lUfficioInoltro);
			lOption.setSelected(lUfficioInoltro.getCodTipoUfficio());
		}
		setRequestAttribute("comboUfficiPMInoltro", "" + lOption);

		// Combo esiti
		Vector<DecodificheModel> lEsiti = new Vector<DecodificheModel>();
		lEsiti.add(new DecodificheModel("-", "-", "", "", "", "", "", "", ""));
		lEsiti.add(new DecodificheModel(ICostantiJMS.PRESAINCARICO, "Atti Presi in carico", "", "", "", "",
				"", "", ""));
		lEsiti.add(new DecodificheModel(ICostantiJMS.ISCRITTO_CLASSE_IV,
				"Iscritto procedimento di classe IV ", "", "", "", "", "", "", ""));
		lEsiti.add(new DecodificheModel(ICostantiJMS.RESTITUITO,
				"Atti restituiti per incompetenza territoriale", "", "", "", "", "", "", ""));
		lEsiti.add(new DecodificheModel(ICostantiJMS.TRASFERITO, "Atti trasmessi ad altro ufficio", "", "",
				"", "", "", "", ""));

		Option lOptionEsiti = new Option(lEsiti);
		if (lAnnotazioneEsito.getCodEsito() != null && lAnnotazioneEsito.getCodEsito().trim().length() > 0) {
			lOptionEsiti.setSelected(lAnnotazioneEsito.getCodEsito());
		}

		setRequestAttribute("comboEsitiProvvedimento", "" + lOptionEsiti);

		setRequestAttribute("modalita", "M");

		return PG_LOAD_INSERISCI_ANNOTAZIONE_ESITO;
	}

}