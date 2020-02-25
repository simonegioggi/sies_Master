package siap.siep.misurasicurezza.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 *
 * <p>
 * Title: ActLoadInserisciRichiestaDAP
 * </p>
 * <p>
 * Description: Classe per la load inserimento Richiesta al DAP
 * </p>
 * *
 * <p>
 * della designazione Istituto per applicazione Misura di Sicurezza
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROS
 *
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciRichiestaDAP extends ActionSiap implements ICostantiMisuraSicurezza {

	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		SoggettoModel lSog = (SoggettoModel) getSessionAttribute("soggetto");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		this.setRequestAttribute("idfascicolo", lFascMod.getIdFascicoloSiep() + "");

		// CONTROLLI SUGLI ELEMENTO DEL FASCICOLO PER POTER ESEGUIRE PROVVEDIMENTI

		// 05/11/2014 Funzione di esclusiva competenza dei proc. di classe IV.
		if (lFascMod.getChiaveProgr().intValue() < 40000 || lFascMod.getChiaveProgr().intValue() >= 50000) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Operazione consentita solo per procedimento di classe IV!");
			return IWebConstants.PG_MESSAGE;
		}

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr()
					+ " non è stato Validato. Impossibile inserire una Misura di Sicurezza!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr()
					+ " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		// Controllo Esistenza pena residua non validata per quel fascicolo

		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		/*
		 * if (lPenaResMod == null) { RedirectTo lRedirigi = new RedirectTo();
		 * lRedirigi.setPage(IWebConstants.PG_MAIN); setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		 * "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
		 * lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
		 * ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		 * 
		 * return IWebConstants.PG_MESSAGE; }
		 */
		setRequestAttribute("penaresidua", lPenaResMod);

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		try {
			Vector lAvvVect = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("avvocati", lAvvVect);
		} catch (SIEPException e) {
			// NON Rilancio l'eccezione: Deve passare anche in assenza del Difensore
			/*
			 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato Numero MEV : SIES
			 * v10 Autore : gioggi Data : 03/feb/2016 Branch : MEV_SIES v10
			 */
			// RedirectTo lRedirigi = new RedirectTo();
			// lRedirigi.setPage(IWebConstants.PG_MAIN);
			// setRequestAttribute(IWebConstants.MESSAGE_TEXT, e.getMessage() +
			// " Impossibile eseguire Richiesta di Accertamento.");
			// lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&" +
			// ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			// return IWebConstants.PG_MESSAGE;
			// ***** FINE INTERVENTO MEV_SIES v10 *****//
		}

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());
		// String lCodPosGiu = "";

		if (lPos == null || lPos.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// lCodPosGiu = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().trim();
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Ricerca Misure sicurezza già presenti nel fascicolo : Sono ORDINATE per DATA_INSERIMENTO
		List lListMis = new ArrayList();
		MisuraSicurezzaModel lMisSicMod = null;
		boolean ldetentiva = false;

		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
		if (lListMis != null && lListMis.size() > 0) {
			for (int k = 0; k < lListMis.size(); k++) {
				lMisSicMod = (MisuraSicurezzaModel) lListMis.get(k);
				if (lMisSicMod != null && lMisSicMod.getCodNatura() != null) {
					if (lMisSicMod.getCodNatura().compareTo("01") == 0) {
						ldetentiva = true;
					}
				}

			}
		} else {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " Risulta privo di Misure di Sicurezza");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		if (!ldetentiva) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr()
					+ " privo di Misure di Sicurezza di tipo DETENTIVO. Impossibile continuare  ");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("listaMisureSic", lListMis);

		// Dati ORDINANZA SIUS ---> Controllo del tipo di DECISIONE / ESITO di MDS
		Vector DepoOrdinanze;
		IDepositoOrdinanzaPc lDepoCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepoOrdinanze = lDepoCtrl.ExRicercaDepositoOrdinanzaPcEventoByFascicoloSiep(lFascMod
				.getIdFascicoloSiep());

		// int conta = 0;
		Iterator Ite1 = DepoOrdinanze.iterator();
		while (Ite1.hasNext()) {
			OrdinanzaEventoTenoriFascicoloSiusModel lOrdMod = (OrdinanzaEventoTenoriFascicoloSiusModel) Ite1
					.next();
			if (lOrdMod.getEvento().getCodEsito().equals("0189")
					|| lOrdMod.getEvento().getCodEsito().equals("0057")
					|| lOrdMod.getEvento().getCodEsito().equals("0051")) {
				// conta++;
			}
		}

		/*
		 * if(conta==0) { RedirectTo lRedirigi = new RedirectTo(); lRedirigi.setPage(IWebConstants.PG_MAIN);
		 * setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Procedimento N." + lFascMod.getChiaveAnno() + "/"
		 * + lFascMod.getChiaveProgr() +
		 * ": Esito Decisione MDS su applicazione Misura \n NON prevede questo provvedimento ");
		 * lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
		 * ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		 * 
		 * return IWebConstants.PG_MESSAGE; }
		 */
		setRequestAttribute("lSogg", lSog);
		setRequestAttribute("ListaOrd", DepoOrdinanze);

		// -----> FINE CONTROLLI

		// Magistrato
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// MERGE v10: cancello codice come in Mev2-s2
		// Avvocato
//		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
//		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
//		setRequestAttribute("avvocati", lAvvocati);

		// Riempimento ComboBox Autorità
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		lOption.setFilter("27");
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		return PG_LOAD_INS_RICHIESTA_DAP; // restituisce la jsp di VIEW

	} // CHIUDE processRequest

} // CHIUDE Classe ActLoad...
