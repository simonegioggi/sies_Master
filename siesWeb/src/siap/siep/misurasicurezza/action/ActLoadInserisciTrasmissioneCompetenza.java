package siap.siep.misurasicurezza.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.model.DecodeModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

public class ActLoadInserisciTrasmissioneCompetenza extends ActionSiap implements ICostantiMisuraSicurezza {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ==========================================================================
		// Verifico se fascicolo di competenza
		// ==========================================================================
		this.isFascicoloSiepDiCompetenza();

		// ==========================================================================
		// Controllo Validazione Fascicolo
		// ==========================================================================
		if (this.isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		// ==========================================================================
		// Controllo Fascicolo definito
		// ==========================================================================
		if (this.isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		// ==========================================================================
		// Verifico se esistono eventi non validati
		// ==========================================================================
		this.isEventoNonValidato();

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
		// FIXME 'MS' Verificare presebza e obbligatorietà della pena residua
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

		// ========================
		// Dati per le combo
		// ========================
		Option lOption = null;

		// Tipo UFFICIO Destinatario
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
		String[] lFiltroUffici = { "-", "PM", "PMM" }; // i destinatari sono solo PM e PMM
		lOption.setFilter(lFiltroUffici);
		setRequestAttribute("ufficioPM", "" + lOption);

		// Tipologia Atto: EVENTO.TIPO_PROVVEDIMENTO
		lOption = new Option(DecodificheManager.getInstance().getTipoRichiestaT());
		setRequestAttribute("richiesta", "" + lOption);

		// Oggetto Atto: EVENTO.COD_MOTIVO
		// RV_HIGH_VALUE = 'RICHGEN'
		// 5404 = Atti per competenza ai fini dell'esecuzione della misura di sicurezza
		// lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentiRichGen());
		// lOption.setFilter(new String[]{"-", "5404","5416"});

		Collection lOggettiOrdinati = new ArrayList();

		Collection lMotivoRichGen = DecodificheManager.getInstance().getMotivoProvvedimentiRichGen();
		Iterator lItxMotRichGen = lMotivoRichGen.iterator();
		while (lItxMotRichGen.hasNext()) {
			DecodeModel lDecoModel = (DecodeModel) lItxMotRichGen.next();
			if ("5404".equals(lDecoModel.getCode())) {
				lOggettiOrdinati.add(lDecoModel);
				break;
			}
		}

		lItxMotRichGen = lMotivoRichGen.iterator();
		while (lItxMotRichGen.hasNext()) {
			DecodeModel lDecoModel = (DecodeModel) lItxMotRichGen.next();
			if ("5416".equals(lDecoModel.getCode())) {
				lOggettiOrdinati.add(lDecoModel);
				break;
			}
		}

		lOption = new Option(lOggettiOrdinati);

		setRequestAttribute("oggetto", "" + lOption);

		// MAGISTRATO COMPETENTE
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistratocompetente", lMagi);

		// Tipo UDS per la comunicazione
		// lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		// lOption.setFilter(new String[]{"-", "UDS", "UDSM"});
		// setRequestAttribute("tipoUDS","" + lOption);

		Collection lUfficiOrdinati = new ArrayList();
		lUfficiOrdinati.add(new DecodeModel("-", "-"));

		Collection lTipoUfficio = DecodificheManager.getInstance().getTipoUfficio();
		Iterator lItxTipoUfficio = lTipoUfficio.iterator();

		while (lItxTipoUfficio.hasNext()) {
			DecodeModel lDecoModel = (DecodeModel) lItxTipoUfficio.next();
			if ("UDS".equals(lDecoModel.getCode())) {
				lUfficiOrdinati.add(lDecoModel);
				break;
			}
		}

		lItxTipoUfficio = lTipoUfficio.iterator();
		while (lItxTipoUfficio.hasNext()) {
			DecodeModel lDecoModel = (DecodeModel) lItxTipoUfficio.next();
			if ("UDSM".equals(lDecoModel.getCode())) {
				lUfficiOrdinati.add(lDecoModel);
				break;
			}
		}
		lOption = new Option(lUfficiOrdinati);
		setRequestAttribute("tipoUDS", "" + lOption);

		// ALTRO DESTINATARIO x la notifica
		Option lAEOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaN", "" + lAEOption);

		return PG_LOAD_INS_TRASM_COMP;
	}

}