package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.web.ISIAPCostantiWeb;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * Action per la load della modifica
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadModificaTrasmissioneCompetenza extends ActSIESDettaglioProvvedimento implements
		ICostantiMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		if (lIdEvento == null) {
			String lStrIdEvento = this.getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
			lIdEvento = new BigDecimal(lStrIdEvento);
		}

		// ==========================================================================
		// ==========================================================================
		// Verifico l'esistenza della Posizione Giuridica da caricare in maschera
		// ==========================================================================
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);

		PosizioneGiuridicaModel lPosizione = lPos.getPosizioneGiuridica();

		// ==========================================================================
		// Verifico se Ergastolo recuperando il dato della Pena Complessiva
		// ==========================================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		/*PenaComplessivaModel lPenComMod = */ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		// 24/10/2014 Abolito il controllo di Pena Complessiva per i Procedimenti della classe IV.
		// if (lPenComMod == null)
		// throw new SIEPException(SIEPException.USER_MESSAGE,
		// "Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		// if( lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "")
		// {
		// if(lPenComMod.getCodTipoPenaDetentiva().equals("03")) {
		// lFlagErgastolo = "S";
		// }
		// else if(lPenComMod.getCodTipoPenaDetentiva().equals("04")) {
		// lFlagErgastolo = "D";
		// }
		// }

		setRequestAttribute("flagergastolo", lFlagErgastolo);

		// ==========================================================================
		// Recupero e controllo la Pena Residua (da caricare in maschera)
		// ==========================================================================
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lPenaResidua = null;
		if (lPosizione.getCodPosizioneGiuridica() != null && !lPosizione.isLibero()) {
			// Detenuto l'ultima pena Validata
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());
		} else { // Libero recupero l'ultima Pena Residua (anche se non validata)
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod
					.getIdFascicoloSiep());
		}

		// La Pena Residua deve essere presente e deve avere settate le date inizio/fine
		// (a meno del caso libero in cui le date non ci sono e puo' non essere validata)
		// SOLO SE DI CLASSE I, PER LA CLASSE IV LA PENA RESIDUA NON E DETTO CHE SIA
		// PRESENTE
		if (!(lFascMod.getChiaveProgr().intValue() > 40000 && lFascMod.getChiaveProgr().intValue() < 50000)) {
			String lErrore = null;
			String lAzioneChiamante = null;

			if (lPenaResidua == null) {
				lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
				lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
			} else if (!lPosizione.isLibero()
					&& (lPenaResidua.getDataInizio() == null || lPenaResidua.getDataFine() == null)
					&& lFlagErgastolo.equals("N")) {
				lErrore = "Data decorrenza pena inestistente. Rivedere la Posizione Giuridica";
				lAzioneChiamante = "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica";
			}

			if (lErrore != null) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, lErrore);
				lRedirigi.setAction(lAzioneChiamante + "&" + ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "="
						+ getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}
		}
		setRequestAttribute("penaresidua", lPenaResidua);

		// ==================================================
		// Recupero le Misure di sicurezza da visualizzare
		// ==================================================
		if ("S".equalsIgnoreCase(lFascMod.getFlagCumulante())) {
			ICumulo lCtrlCum = SIEPLookupRemote.getCumuloRemote();
			Vector lCumuli = lCtrlCum
					.ExRicercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(lFascMod
							.getIdFascicoloSiep());

			PenaCumuloModel lPenCumMod = new PenaCumuloModel();

			if (lCumuli.size() > 0) {
				CumuloModel lCumMod = ((CumuloModel) (lCumuli).get(0));

				if (lCumMod != null && lCumMod.getIdCumulo() != null) {
					IPenaCumulo lCtrlPen = SIEPLookupRemote.getPenaCumuloRemote();
					lPenCumMod = lCtrlPen.ExRicercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo());
				}
			}

			this.setRequestAttribute("penacumulo", lPenCumMod);
		} else {
			IMisuraSicurezza lCtrlMs = SIEPLookupRemote.getMisuraSicurezzaRemote();
			List lListMisure = lCtrlMs.ExRicercaMisuraSicurezzaByIdFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("listaMisure", lListMisure);
		}

		// ==================================================
		// Recupero la Residenza da visualizzare
		// ==================================================
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		ResidenzaAssociataModel lResAss = lCtrl.ExRicercaResidenzaFascicoloSiepCorrente(lFascMod
				.getIdFascicoloSiep());
		if (lResAss != null && lResAss.getResidenza() != null)
			setRequestAttribute("residenza", lResAss.getResidenza());

		//
		// ==========================================================================

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("evento", lEveNotMod.getEvento());

		// Ufficio Competente all'Emissione del Provvedimento
		UfficioModel lUffDestinataro = getUfficioByCodUfficio(lEveNotMod.getEvento()
				.getCodUfficioDestinatario());

		// ========================
		// Dati per le combo
		// ========================
		Option lOption = null;

		// Tipo UFFICIO Destinatario
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
		String[] lFiltroUffici = { "-", "PM", "PMM" }; // i destinatari sono solo PM e PMM
		lOption.setFilter(lFiltroUffici);
		lOption.setSelected(lUffDestinataro.getCodTipoUfficio());
		setRequestAttribute("ufficioPM", "" + lOption);

		setRequestAttribute("sedeUfficioPM", lUffDestinataro.getDescrComune());

		// Tipologia Atto: EVENTO.TIPO_PROVVEDIMENTO
		lOption = new Option(DecodificheManager.getInstance().getTipoRichiestaT());
		setRequestAttribute("richiesta", "" + lOption);

		// Oggetto Atto: EVENTO.COD_MOTIVO
		// RV_HIGH_VALUE = 'RICHGEN'
		// 5404 = Atti per competenza ai fini dell'esecuzione della misura di sicurezza
		// TODO aggiungere ulteriori codici nel caso di Restituzione atti
		lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentiRichGen());
		lOption.setFilter(new String[] { "-", "5404", "5416" });
		lOption.setSelected(lEveNotMod.getEvento().getCodMotivo());
		setRequestAttribute("oggetto", "" + lOption);

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

		// Tipo UDS per la comunicazione
		Option lOptionUDS = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOptionUDS.setFilter(new String[] { "-", "UDS", "UDSM" });

		// ====================================
		// ALTRO DESTINATARIO x la notifica
		// ====================================
		Option lAEOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");

		NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
		if (lNotifiche != null) {
			for (int i = 0; i < lNotifiche.length; i++) {
				NotificaModel lNotifica = lNotifiche[i];

				// Ufficio del MDS
				if ("C".equals(lNotifica.getCodTipoNotifica()) && lNotifica.getUffCodUfficio() != null
						&& lNotifica.getUfficio() != null) {
					lOptionUDS.setSelected(lNotifica.getUfficio().getCodTipoUfficio());
					setRequestAttribute("comuneUDS", lNotifica.getUfficio().getDescrComune());
				} else if ("C".equals(lNotifica.getCodTipoNotifica())
						&& lNotifica.getIstDetIdIstitutoDetenzione() != null
						&& lNotifica.getIstitutoDetenzione() != null) { // Istituto di detenzione
					setRequestAttribute("istitutoDetenzioneDest", lNotifica.getIstitutoDetenzione());
				} else if ("C".equals(lNotifica.getCodTipoNotifica())
						&& lNotifica.getAutoritaEsterna() != null) {
					// Altra autorita
					AutoritaEsternaModel lAutEst = lNotifica.getAutoritaEsterna();
					lAEOption.setSelected(lAutEst.getCodTipoAutorita());

					setRequestAttribute("sedeAltroDest", lAutEst.getDescrSede());
				}
			}
		}

		setRequestAttribute("tipoUDS", "" + lOptionUDS);
		setRequestAttribute("autoritaEsternaN", "" + lAEOption);

		setRequestAttribute("dataTrasmissioneStr",
				DateUtils.getDateToString(lEveNotMod.getEvento().getDataTrasmissioneAtti(), "dd/MM/yyyy"));

		setRequestAttribute("modalita", "M");

		return PG_LOAD_INS_TRASM_COMP;
	}

}