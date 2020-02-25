package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.model.EventoModel;
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
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.util.SIUSLookupRemote;

/**
 *
 * <p>
 * Title: ActLoadInserisciAnnotazioneDecisioneDellaSorveglianza
 * </p>
 * <p>
 * Description: Classe la load inserimento annotazione della decisione del MDS
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author AMBROS
 *
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciAnnotazioneDecisioneDellaSorveglianza extends ActionSiap
		implements ICostantiMisuraSicurezza {

	@SuppressWarnings("unchecked")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo") || this.isSessionAttributeNullObj("soggetto")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		SoggettoModel lSog = (SoggettoModel) getSessionAttribute("soggetto");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		this.setRequestAttribute("idfascicolo", lFascMod.getIdFascicoloSiep() + "");

		// MEV_39 ***** inizio *****
		// -----> controllo se fascicolo di classe I è legato a fascicolo di classe IV
		// -----> in caso affermativo recupero i provvedimenti SIUS legati al fascicolo
		// -----> di classe IV
		IMisuraSicurezza lCtrMis = SIEPLookupRemote.getMisuraSicurezzaRemote();
		BigDecimal fasSiefascCollegato = new BigDecimal(0);
		Vector vectFasIV = new Vector();
		vectFasIV = lCtrMis.ExRicercaFascicoliCollegati(lFascMod.getIdFascicoloSiep());
		if (vectFasIV.size() > 0) {
			Iterator iteIV = vectFasIV.iterator();
			while (iteIV.hasNext()) {
				FascMsToFascSiepModel fascMSMod = (FascMsToFascSiepModel) iteIV.next();
				fasSiefascCollegato = fascMSMod.getFasSieIdFascicoloCollegato();

			}
		}
		// MEV_39 ***** fine ******

		// 05/11/2014 Funzione di esclusiva competenza dei proc. di classe IV.
		if (lFascMod.getChiaveProgr().intValue() < 40000 || lFascMod.getChiaveProgr().intValue() >= 50000) {
			// MEV_39
			// se il fascicolo di calsse I è legato ad un fascicolo di classe IV non visualizzo il messaggio
			if (vectFasIV.size() <= 0) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Operazione consentita solo per procedimento di classe IV!");
				return IWebConstants.PG_MESSAGE;
			}
		}

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
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
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
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
		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N")) {
			setRequestAttribute("dataeditabile", "S");
		}

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;
		if (lPenaResMod != null) {
			lDataInizioPena = lPenaResMod.getDataInizio();
			// lDataFinePenaM = lPenaResMod.getDataFine();
			lDataFinePenaA = lPenaResMod.getDataFinePresunta();
		}
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyy"));
		setRequestAttribute("penaresidua", lPenaResMod);

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		try {
			Vector lAvvVect = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("avvocati", lAvvVect);
		} catch (SIEPException e) {
			// NON Rilancio l'eccezione: Deve Passare anche se Fascicolo è PRIVO di Avvocato
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
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());
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

		// Riempimento ComboBox Autorità
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		EventoModel lEve = new EventoModel();
		lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

		// Magistrato
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// MERGE v10: cancello codice come in Mev2-s2
		// Avvocato
		// IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		// Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		// setRequestAttribute("avvocati", lAvvocati);

		// Ricerca Misure sicurezza già presenti nel fascicolo
		List lListMis = new ArrayList();
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		try {
			lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
		} catch (F3BException e) {
		}

		setRequestAttribute("listaMisurePrec", lListMis);

		// ComboBOX per Scelta NUOVA Misura di Sicurezza
		Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
		Option lOptionT = new Option(DecodificheManager.getInstance().getTipoMisuraSicurezza());

		setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);
		setRequestAttribute("tipoMisuraSicurezza", "" + lOptionT);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		/*
		 * 09/02/2015 Modificato il criterio di individuazione delle MIS.SIC. attraverso il dato
		 * "ProvvedimentoEventoTenoreFascicoloSiusModel.TipoEsitoMisura". / Dati ORDINANZA SIUS Vector
		 * DepoOrdinanze; IDepositoOrdinanzaPc lDepoCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		 * DepoOrdinanze =
		 * lDepoCtrl.ExRicercaDepositoOrdinanzaPcEventoByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 */
		Vector provvSIUS = new Vector();
		IPeriodoAltraMisura lPAMCtrl = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		provvSIUS = lPAMCtrl.ExRicercaProvvedimentoEventoByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// MEV_39 ***** inizio *****
		// se il fascicolo di classe I è legato ad un fascicolo di classe IV recupero i provvedimenti
		// SIUS legatial fascicolo di classe IV
		if (vectFasIV.size() > 0) {
			Vector provvSIUS_classeIV = new Vector();
			provvSIUS_classeIV = lPAMCtrl.ExRicercaProvvedimentoEventoByFascicoloSiep(fasSiefascCollegato);
			if (!provvSIUS_classeIV.isEmpty()) {
				provvSIUS.addAll(provvSIUS_classeIV);
			}
		}
		// MEV_39 ***** fine *****

		setRequestAttribute("lSogg", lSog);
		// setRequestAttribute("ListaOrd", DepoOrdinanze);
		setRequestAttribute("ListaProvv", provvSIUS);

		if (provvSIUS.isEmpty()) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"NON ci sono Provvedimenti SIUS associati al Procedimento N." + lFascMod.getChiaveAnno()
							+ "/" + lFascMod.getChiaveProgr());
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		return PG_LOAD_INS_ANNOTAZIONE_SORVEGLIANZA; // restituisce la jsp di VIEW
	}

}