package siap.sico.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

//import java.util.Collection;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActLoadInserisciLiberazioneAnticipata - Azione Load inserimento Liberazione Anticipata
 *
 * @version 1.0
 */
public class ActLoadInserisciLiberazioneAnticipata extends ActionSiap implements ICostantiLibertaAnticipata {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();

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

		this.isEventoNonValidato();

		/******************************* Posizione Giuridica **********************************/
		IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosizione = lCtrlPosGiu
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

		if (lPosizione == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		/*
		 * if ( lPosizione != null && (lPosizione.getCodPosizioneGiuridica() != null &&
		 * lPosizione.getCodPosizioneGiuridica().equals("07")) ) { throw new
		 * SIEPException(SIEPException.USER_MESSAGE,
		 * "Posizione Giuridica non prevista. Impossibile eseguire la richiesta."); }
		 */
		setRequestAttribute("posizione", lPosizione);

		/*******************************
		 * Pena Complessiva ***************************** IPenaComplessiva ICtrlPenCom =
		 * SIEPLookupRemote.getPenaComplessivaRemote(); PenaComplessivaModel lPenComMod =
		 * ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);
		 *
		 * if (lPenComMod == null) throw new SIEPException(SIEPException.USER_MESSAGE, "Pena Complessiva non
		 * presente. Impossibile eseguire la richiesta.");
		 *
		 * String lFlagErgastolo = "N"; // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
		 * diurno if( lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() !=
		 * "" && ( lPenComMod.getCodTipoPenaDetentiva().equals("03") ||
		 * lPenComMod.getCodTipoPenaDetentiva().equals("04") ) ) { lFlagErgastolo = "S"; }
		 *
		 * setRequestAttribute("flagergastolo", lFlagErgastolo); Fine Pena Complessiva
		 ************************/

		/*********************************** Pena Residua ***************************/
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		//
		PenaResiduaModel lPenaResidua = null;
		if (lPosizione.getCodPosizioneGiuridica() != null
				&& !lPosizione.getCodPosizioneGiuridica().equals("07")) // LIBERO (07)
		{
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
		} else {
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		}

		/*
		 * //La Pena Residua deve essere presente e deve avere settate le date inizio/fine // (a meno del caso
		 * libero in cui le date non ci sono e puo' non essere validata) String lErrore = null; String
		 * lAzioneChiamante = null; if(lPenaResidua == null) { //lErrore =
		 * "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?"; lErrore =
		 * "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
		 * lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena"; } /* else if(
		 * (lPosizione.getCodPosizioneGiuridica() != null &&
		 * !lPosizione.getCodPosizioneGiuridica().equals("07")) //LIBERO (07) &&
		 * (lPenaResidua.getFlagValidato() != null && !lPenaResidua.getFlagValidato().equals("S") &&
		 * lFlagErgastolo.equals("N")) ) { lErrore = "Pena Residua da Espiare non validata. Validare?";
		 * lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena"; }
		 *
		 * else if( !lPosizione.isLibero() // non è libero && ( lPenaResidua.getDataInizio() == null // non ha
		 * le date || lPenaResidua.getDataFine() == null) && lFlagErgastolo.equals("N") ) { lErrore =
		 * "Data decorrenza pena inestistente. Rivedere la Posizione Giuridica"; lAzioneChiamante =
		 * "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica"; }
		 *
		 * if(lErrore != null) { RedirectTo lRedirigi = new RedirectTo();
		 * lRedirigi.setPage(IWebConstants.PG_MAIN); setRequestAttribute(IWebConstants.MESSAGE_TEXT, lErrore);
		 * lRedirigi.setAction(lAzioneChiamante+"&" + ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" +
		 * getClass().getName()); setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		 *
		 * return IWebConstants.PG_MESSAGE; }
		 */
		setRequestAttribute("penaresidua", lPenaResidua);

		/*
		 * //ESITO TENORE DecodificheModel lModel = new DecodificheModel(); IDecodifiche lDecodifiche =
		 * SICOLookupRemote.getDecodificheRemote(); Collection lColl =
		 * lDecodifiche.ExRicercaEsitiByOggetto("2130"); Option lOption = new Option(lColl);
		 *
		 * setRequestAttribute("esitotenore", "" + lOption );
		 */
		/*
		 * //========================================================================== // Recupero le Licenze
		 * Liberazione Anticipata a sistema per il fascicolo // corrente, non ancora elaborate nel calcolo
		 * della pena. //==========================================================================
		 * ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		 * LicenzaLibAnticipataModel lModelLib = new LicenzaLibAnticipataModel(); lModelLib =
		 * lCtrlLib.ExRicercaLicenzaLibanticipataConcessayIDFascicoloSIEP(lIdFascicolo,"N");
		 * setRequestAttribute("licenza", lModelLib);
		 */

		// Si cerca la presenza di Liberazioni Anticipate non elaborate
		// per presentare un messaggio di avviso all'utente
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		// LicenzaLibAnticipataModel lModelLib = new LicenzaLibAnticipataModel();
		List lLicenze = lCtrlLib
				.ExRicercaLicenzaLibanticipataConcesseDepositateByIdFascicoloSIEP(lIdFascicolo, "NE");

		if (!lLicenze.isEmpty()) {
			// IL messaggio di Alert:
			// "Attenzione vi sono ordinanze di Liberazione Anticipata già caricate e non elaborate"
			// non deve comparire se è stata emessa e validata una comunicazione per quell'ordinanza di LA
			Iterator iter = lLicenze.iterator();
			while (iter.hasNext()) {
				LicenzaLibAnticipataModel item = (LicenzaLibAnticipataModel) iter.next();
				if (!item.isConProvvedimentoValidato()) {
					setRequestAttribute("FlagLicenzeNonElaborate", "S");
				}
			}
		}

		// AUTORITA' EMITTENTE (TIPO_UFFICIO_SOSP)
		Option lOption = new Option(DecodificheManager.getInstance().getListaAutoritaSospTDSUDS());
		setRequestAttribute("autoritaemittente", "" + lOption);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		// restituisce la jsp di VIEW
		return PG_LOAD_INSERISCILIBANTICIPATA;
	}

}