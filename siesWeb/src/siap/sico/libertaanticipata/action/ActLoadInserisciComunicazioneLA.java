package siap.sico.libertaanticipata.action;

/**
 * <p>Title: ActLoadInserisciComunicazioneLA</p>
 * <p>Description: Classe Action per la load inserimento Comunicazione Liberazione Anticipata per Ergastolo/Ergastolo Diurno o Libero</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

@SuppressWarnings("rawtypes")
public class ActLoadInserisciComunicazioneLA extends ActionSiap implements ICostantiLicenzaLibanticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

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
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosizione = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosizione = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (lPosizione == null || lPosizione.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("posizioneluogoaltra", lPosizione);
		/******************************* Fine Posizione Giuridica ************************/

		/******************************* Pena Complessiva ********************************/

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

		setRequestAttribute("penacomplessiva", lPenComMod);

		/******************************* Fine Pena Complessiva ************************/

		/*********************************** Pena Residua ***************************/
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lPenaResidua = null;
		if (lPosizione.getPosizioneGiuridica().getCodPosizioneGiuridica() != null
				&& (!lPosizione.getPosizioneGiuridica().isLibero())) {
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
		} else {
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		}

		setRequestAttribute("penaresidua", lPenaResidua);
		/******************************* Fine Pena Residua ************************/

		boolean lFlagLiberoDataFine = false;
		if (lPenaResidua != null && lPenaResidua.getDataFine() != null
				&& (DateUtils.isGreater(DateUtils.getSysDate(), lPenaResidua.getDataFine())
						&& !DateUtils.isEquals(lPenaResidua.getDataFine(), DateUtils.getSysDate()))) {
			lFlagLiberoDataFine = true;
		}

		if (lFlagErgastolo.equals("N") && !lPosizione.getPosizioneGiuridica().isLibero()
				&& !lFlagLiberoDataFine) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il condannato non è in Ergastolo e non è Libero. Impossibile eseguire la richiesta.");
		}

		/*********************************** Periodi LA ***************************/

		LicenzaLibAnticipataModel llibAntMod = null;

		// Controllo Esistenza LiberazioneAnticipata
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();

		int lTotGiorniConcessi = 0;
		// 20/05/2014 - Errata ; I giorni totali concessi sono inseriti + avanti in DepositoordinanzaPC
		// lTotGiorniConcessi =
		// lCtrlLib.ExTotalePeriodiConcessiComputatiByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// verifica esistenza evento liberazione anticipata
		EventoModel lEveMod = null;
		BigDecimal lIdEventoOrdinanza = null;
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		if (isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO)) {
			String[] lArrayMot = { "0076", "2130" };
			List lListEventi = lEveCtrl.ExRicercaEventiNOTAnnullati(lFascMod.getIdFascicoloSiep(), lArrayMot,
					"03", "01");

			if (!lListEventi.isEmpty()) {
				lEveMod = (EventoModel) lListEventi.get(0);
			}
		} else {
			lIdEventoOrdinanza = getRequestBigDecimalParameter(
					ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO);
			lEveMod = lEveCtrl.ExRicercaEventoByKey(lIdEventoOrdinanza);
		}

		DepositoOrdinanzaPcModel lDepOrdMod = null;
		if (lEveMod != null && lEveMod.getIdEvento() != null) {
			// RICERCA DEPOSITO_ORDINANZA_PC
			IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lDepOrdMod = lCtrlDep.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod.getIdEvento());

			lIdEventoOrdinanza = lEveMod.getIdEvento();
			setRequestAttribute("lIdEventoOrdinanza", lIdEventoOrdinanza);
		}

		try {
			// llibAntMod =
			// lCtrlLib.ExRicercaLicenzaLibanticipataConcessayIDFascicoloSIEP(lFascMod.getIdFascicoloSiep(),
			// null);
			Vector lListLA = lCtrlLib.ExRicercaLicenzeByEve(lEveMod.getIdEvento());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" --- > LICENZE ( misura )= " + lListLA.size());
			if (!lListLA.isEmpty()) {
				llibAntMod = (LicenzaLibAnticipataModel) lListLA.get(0);
			}

			if (llibAntMod != null) {
				String lCodUffEmi = llibAntMod.getCodUfficioEmittente();
				UfficioModel lUffMod = this.getUfficioByCodUfficio(lCodUffEmi);
				llibAntMod.setDescrUfficioEmittente(lUffMod.getDescrTipoUfficio());

				if (lDepOrdMod != null && lDepOrdMod.getNumGiorniLibanticipata() != null) {
					// llibAntMod.setNumeroGiorni(lDepOrdMod.getNumGiorniLibanticipata());
					lTotGiorniConcessi = lDepOrdMod.getNumGiorniLibanticipata().intValue();
				}
			}

			setRequestAttribute("liberazione", llibAntMod);
			setRequestAttribute("lTotGiorniConcessi", "" + lTotGiorniConcessi);
			setRequestAttribute("Licenze", lListLA);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nessun periodo concesso" + e);
		}

		if (llibAntMod == null) {
			// throw new SIEPException(SIEPException.USER_MESSAGE, "Non ci sono dati sufficienti per emettere
			// Ordine di Scarcerazione.");
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Non ci sono dati sufficienti per emettere Comuniaczione.");
		}

		// modifica 27-02-2006 -- Dario -- Viviana
		// Bisogna visualizzare i Rigettati,Inammissibili e N.L.P./N.D.P.
		Vector llibAntModNonConcessi = null;
		llibAntModNonConcessi = lCtrlLib.ExRicercaLicenzaLibanticipataNonConcesseByIDFascicoloSIEP(
				lFascMod.getIdFascicoloSiep(), llibAntMod.getEveIdEvento());

		setRequestAttribute("liberazioninonconcesse", llibAntModNonConcessi);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagiMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		if (lMagiMod != null)
			setRequestAttribute("magistratocompetente", lMagiMod);

		// Imposta Tipo Istituto
		Option lOptionIstituto = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOptionIstituto);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
				"siap.sico.libertaanticipata.action.ActLoadInserisciComunicazioneErgastolo");

		if (!lFlagErgastolo.equals("N")) {
			return PG_LOAD_INSERISCI_COMUNICAZIONE_ERGASTOLO;
		} else {
			return PG_LOAD_INSERISCI_COMUNICAZIONE_LIBERO;
		}
	}

}