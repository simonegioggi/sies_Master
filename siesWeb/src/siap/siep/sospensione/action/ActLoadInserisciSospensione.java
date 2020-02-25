package siap.siep.sospensione.action;

/**
 * <p>Title: ActLoadInserisciSospensione</p>
 * <p>Description: Classe Action per la load inserimento di Sospensione </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciSospensione extends ActionSiap implements ICostantiSospensione {

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

		setRequestAttribute("posizione", lPosizione);

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
		PenaResiduaModel lPenaResidua = null;
		if (lPosizione.getCodPosizioneGiuridica() != null
				&& (!lPosizione.getCodPosizioneGiuridica().equals("07")
						&& !lPosizione.getCodPosizioneGiuridica().equals("16")
						&& !lPosizione.getCodPosizioneGiuridica().equals("17")
						&& !lPosizione.getCodPosizioneGiuridica().equals("46")
						&& !lPosizione.getCodPosizioneGiuridica().equals("47"))) // LIBERO (07)
		{
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);

			/*
			 * if (lPenaResidua != null && lPenaResidua.getFlagPenaSospesa() != null) { if
			 * (lPenaResidua.getFlagPenaSospesa().equals("S")) throw new
			 * F3BException(F3BException.USER_MESSAGE, "Pena Residua risulta Sospesa"); if
			 * (lPenaResidua.getFlagPenaSospesa().equals("I")) throw new
			 * F3BException(F3BException.USER_MESSAGE, "Pena Residua risulta Interrotta"); if
			 * (lPenaResidua.getFlagPenaSospesa().equals("D")) throw new
			 * F3BException(F3BException.USER_MESSAGE, "Pena Residua risulta Definita"); }
			 */

		} else {
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		}

		// La Pena Residua deve essere presente e deve avere settate le date inizio/fine
		// (a meno del caso libero in cui le date non ci sono e puo' non essere validata)
		String lErrore = null;
		String lAzioneChiamante = null;

		if (lPenaResidua == null) {
			// lErrore = "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?";
			lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
			lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
		} else if ((!lPosizione.isLibero() && !lPosizione.getCodPosizioneGiuridica().equals("16")
				&& !lPosizione.getCodPosizioneGiuridica().equals("17")
				&& !lPosizione.getCodPosizioneGiuridica().equals("46")
				&& !lPosizione.getCodPosizioneGiuridica().equals("47"))// non è libero
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

		// Se posizione = 46 cerca i dati della SOSPENSIONE
		if (lPosizione != null && lPosizione.getCodPosizioneGiuridica() != null
				&& lPosizione.getCodPosizioneGiuridica().equals("46")) // LIBERO IN SOSPENSIONE
		{
			/******************************* SOSPENSIONE ***********************************/
			// Se è libero in sospensione mi aspetto una pena sospesa validata e sospesa
			// con il relativo record di sospensione, se non è l'ultima la cerco
			/*
			 * 15/10/2010 Su indicazione di Michele Testa, La Pos. Giur. Libero in sospensione va trattata
			 * comunque. if(lPenaResidua.getFlagPenaSospesa() == null || (lPenaResidua.getFlagPenaSospesa() !=
			 * null && lPenaResidua.getFlagPenaSospesa().equals("N"))) { PenaResiduaModel
			 * lPenaResiduaSospesaValidata =
			 * lCtrlPenRes.ExRicercaPenaResiduaUltimaValidataSospesa(lIdFascicolo);
			 * 
			 * if(lPenaResiduaSospesaValidata == null) throw new SIEPException(SIEPException.USER_MESSAGE,
			 * "La posizione giuridica è LIBERO IN SOSPENSIONE ma non esiste una pena residua validata e sospesa. Impossibile eseguire la richiesta."
			 * ); }
			 */

			// ** NOTA : mi aspetto un solo record di sopensione per pena residua
			ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
			SospensioneModel lSospensione = lCtrlSosp
					.ExRicercaSospensioneByIdPenaResidua(lPenaResidua.getIdPenaResidua());

			setRequestAttribute("sospensione", lSospensione);
		}

		// ** Ricerca su DECRETO_ORDINANZA_SIEP **
		IDecretoOrdinanzaSiep lCtrlDecOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
		DecretoOrdinanzaSiepModel lDecOrd = lCtrlDecOrd
				.ExRicercaUltimaDecretoOrdinanzaSiepByIdFascicolo(lIdFascicolo);

		setRequestAttribute("decretoordinanza", lDecOrd);
		String lFlagDec = (lDecOrd == null ? "N" : "S");
		setRequestAttribute("flagdecretoordinanza", lFlagDec);

		// TIPO REGISTRO ORDINANZA
		// modifica del 27-06-06 -- dario -- viviana -- a seguito della divisione della maschera di sospensine
		// della pena
		// delle decisioni della sorveglianza da qualle del GE
		Collection lCollTipoReg = DecodificheManager.getInstance().getTipoRegistroOrdinanza();
		Iterator iter = lCollTipoReg.iterator();
		lCollTipoReg = new ArrayList();
		while (iter.hasNext()) {
			DecodificheModel lDecMod = (DecodificheModel) iter.next();
			if (!lDecMod.getCodiceAlternativo().equals("0001"))// SIUS
				lCollTipoReg.add(lDecMod);
		}
		setRequestAttribute("tiporegistroordinanza", lCollTipoReg);

		// TIPO PROVVEDIMENTO
		Option lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOption.setFilter(new String[] { "02", "03" }); // solo DECRETO o ORDINANZA
		if (lDecOrd != null) {
			lOption.setSelected(lDecOrd.getCodTipoProvvedimento());
		}
		setRequestAttribute("tipoprovvedimento", "" + lOption);

		// AUTORITA' EMITTENTE (TIPO_UFFICIO_SOSP)
		ArrayList lListAutoritaSospensione = (ArrayList) DecodificheManager.getInstance()
				.getListaAutoritaSospensione();
		setRequestAttribute("autoritaemittente", lListAutoritaSospensione);

		// CONTENUTO DECISIONE
		ArrayList lContenutoDecisione = (ArrayList) DecodificheManager.getInstance()
				.getListaOggettiSospensione();
		setRequestAttribute("contenutodecisione", lContenutoDecisione);

		// OGGETTO DECISIONE
		ArrayList lMotiviProvvedimento = (ArrayList) DecodificheManager.getInstance()
				.getListaMotiviProvvedimentoSospensione();
		setRequestAttribute("oggettodecisione", lMotiviProvvedimento);

		// TIPOLOGIA DECISIONE
		ArrayList lEsitiTenore = (ArrayList) DecodificheManager.getInstance()
				.getListaEsitiTenoreSospensione();
		setRequestAttribute("tipologiadecisione", lEsitiTenore);

		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
				"siap.siep.sospensione.action.ActLoadInserisciSospensione");

		return PG_LOAD_INSERISCI_SOSPENSIONE; // restituisce la jsp di VIEW
	}

}