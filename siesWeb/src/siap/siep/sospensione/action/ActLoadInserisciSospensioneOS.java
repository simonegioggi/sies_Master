package siap.siep.sospensione.action;

/**
 * <p>Title: ActLoadInserisciSospensioneOS</p>
 * <p>Description: Classe Action per la load inserimento di Sospensione Ordine Scarcerazione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

public class ActLoadInserisciSospensioneOS extends ActionSiap implements ICostantiSospensione {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Controllo Validazione Fascicolo
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Controllo Fascicolo definito
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		/******************************* Posizione Giuridica **********************************/
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		PosizioneGiuridicaModel lPosizione = new PosizioneGiuridicaModel(lPos.getPosizioneGiuridica());
		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		setRequestAttribute("posizioneluogoaltra", lPos);

		/******************************* Pena Complessiva *****************************/
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "") {
			if (lPenComMod.getCodTipoPenaDetentiva().equals("03")) {
				lFlagErgastolo = "S";
			} else if (lPenComMod.getCodTipoPenaDetentiva().equals("04")) {
				lFlagErgastolo = "D";
			}
		}

		setRequestAttribute("flagergastolo", lFlagErgastolo);
		/******************************* Fine Pena Complessiva ************************/

		/*********************************** Pena Residua ***************************/
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		//
		PenaResiduaModel lPenaResidua = new PenaResiduaModel();

		// 17-05-2005--dario--luciana perchè per l'estradizione la pena deve continuare e non viene interrotta
		// lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidataSospesaInterruzione(lIdFascicolo);
		lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);

		// ** Ricerca su EVENTO SE Estradato **
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNot = null;
		EventoModel lEveRic = new EventoModel();
		EventoModel lEve = new EventoModel();
		lEveRic.setCodTipoEvento("01");
		lEveRic.setFasSieIdFascicoloSiep(lIdFascicolo);

		// Cambiato il codice Tipo Provvedimento.
		// Comunque si mantiene la compatibilità con i vecchi codici. Luigi 11-10-2005
		String[] lTipoProv = { "04", "12", "04", "25", "04", "25", "25" };
		String[] lMotivi = { "0266", "0266", "0268", "0268", "0269", "0269", "0366" };
		// lEveRic.setCodTipoProvvedimento("04");
		// String[] motivi = {"0266","0269","0268"};
		try {
			lEve = lCtrlEvento.ExRicercaEventoUnicoTipoProvTipoMot(lEveRic, lTipoProv, lMotivi);
			// lEve = lCtrlEvento.ExRicercaEventoPerMotivo(motivi,lEveRic);
		} catch (Exception e) {
		}

		if (lEve == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il Detenuto non risulta Estradato o Deceduto. Impossibile emettere Ordine di Scarcerazione");

		// La Pena Residua deve essere presente e deve avere settate le date inizio/fine
		// (a meno del caso libero in cui le date non ci sono e puo' non essere validata)
		String lErrore = null;
		String lAzioneChiamante = null;
		if (lPenaResidua == null) {
			// lErrore = "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?";
			lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
			lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
		} else if ((!lPosizione.isLibero() && !lPosizione.getCodPosizioneGiuridica().equals("48") // Deceduto
				&& !lPosizione.getCodPosizioneGiuridica().equals("30") // Estradato
		)// non è libero E NON EVASO
				&& (lPenaResidua.getDataInizio() == null // non ha le date
						|| lPenaResidua.getDataFine() == null)
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

		setRequestAttribute("penaresidua", lPenaResidua);

		// ==========================================================================
		// Caricamento dei destinatari
		// ==========================================================================
		if (lEve != null) {
			if (lEve.getFlagDocumentoRegistrato() == null || lEve.getFlagDocumentoRegistrato().equals("N")) {
				lEveNot = lCtrlEvento.ExRicercaEventoNotificaByKey(lEve.getIdEvento());
				setRequestAttribute("eventonotifica", lEveNot);

				IUfficio lUff = SICOLookupRemote.getUfficioRemote();

				IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();

				NotificaModel[] lNotifiche = lEveNot.getNotifiche();

				for (int i = 0; i < lNotifiche.length; i++) {
					// magistratoSorv
					if (lNotifiche[i].getUffCodUfficio() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("C")) {
						UfficioModel lUffModMag = lUff
								.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
						setRequestAttribute("uffMagistrato", lUffModMag);
					}

					// TDS
					if (lNotifiche[i].getUffCodUfficio() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("E")) {
						UfficioModel lUffModTDS = lUff
								.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
						setRequestAttribute("uffTDS", lUffModTDS);
					}

					if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("N")) {
						IstitutoDetenzioneModel lModIst = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(
								lNotifiche[i].getIstDetIdIstitutoDetenzione().toUpperCase());
						setRequestAttribute("Istituto", lModIst);
					}
				}
			}
		}

		// ** Ricerca su DECRETO_ORDINANZA_SIEP **
		IDecretoOrdinanzaSiep lCtrlDecOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();

		DecretoOrdinanzaSiepModel lDecOrd = lCtrlDecOrd
				.ExRicercaDecretoOrdinanzaSiepByKey(lEve.getDecIdDecretoOrdinanzaSiep());
		if (lDecOrd != null) {
			setRequestAttribute("decretoordinanza", lDecOrd);
			String lFlagDec = (lDecOrd == null ? "N" : "S");
			setRequestAttribute("flagdecretoordinanza", lFlagDec);
		}

		// TIPO REGISTRO ORDINANZA
		Collection lCollTipoReg = DecodificheManager.getInstance().getTipoRegistroOrdinanza();
		setRequestAttribute("tiporegistroordinanza", lCollTipoReg);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		// motivo interruzione
		Option lmotivointerruzione = new Option(DecodificheManager.getInstance().getMotivoInterruzione());
		setRequestAttribute("motivointerruzione", "" + lmotivointerruzione);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// MAGISTRATO COMPETENTE
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
				"siap.siep.sospensione.action.ActLoadInserisciSospensioneOS");

		setRequestAttribute("azione", "OS");

		return PG_LOAD_INSERISCI_SOSPENSIONE_OE; // restituisce la jsp di VIEW
	}
}