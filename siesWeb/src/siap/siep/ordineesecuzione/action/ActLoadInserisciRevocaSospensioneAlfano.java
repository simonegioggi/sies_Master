package siap.siep.ordineesecuzione.action;

import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadInserisciRevocaSospensioneAlfano extends ActionSiap implements ICostantiOrdineEsecuzione {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		this.isEventoNonValidato();

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		// Controllo Posizioni Giuridiche ammesse
		// La posizione giuridica deve essere "Libero" o 01, 02, 03, 53
		String lCodPosGiu = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica();
		if (lCodPosGiu.equals("07") // LIBERO
				|| lCodPosGiu.equals("10") // LIBERO
				|| lCodPosGiu.equals("16") // LIBERO IN DIFFERIMENTO PENA
				|| lCodPosGiu.equals("17") // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
				|| lCodPosGiu.equals("46") // LIBERO in Sospensione
				|| lCodPosGiu.equals("47") // LIBERO in Sospensione DPR 309/90
				|| lCodPosGiu.equals("01") || lCodPosGiu.equals("02") || lCodPosGiu.equals("03")
				|| lCodPosGiu.equals("53") || lCodPosGiu.equals("85") || lCodPosGiu.equals("86")
				|| lCodPosGiu.equals("87") || lCodPosGiu.equals("73") || lCodPosGiu.equals("70")
				|| lCodPosGiu.equals("71") || lCodPosGiu.equals("72")) {
		} else {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Posizione Giuridica non gestita per il provvedimento selezionato");
		}

		setRequestAttribute("posizioneluogoaltra", lPos);
		setRequestAttribute("lcodicePosizione", lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());

		// Controllo Esistenza pena residua per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
			return IWebConstants.PG_MESSAGE;

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		setRequestAttribute("penaresidua", lPenaResMod);

		Date lDataInizioPena = lPenaResMod.getDataInizio();
		// Date lDataFinePenaM = lPenaResMod.getDataFine();
		Date lDataFinePenaA = lPenaResMod.getDataFinePresunta();

		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));

		// Controllo se esiste il decreto di Sospensione
		EventoModel lEventoMod = new EventoModel();
		IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
		lEventoMod = lEventoCtrl.ExRicercaEventoByFascicoloSiepOESospensione(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("decreto", lEventoMod);
		/**
		 * Eliminazione avviso di mancanza OE con sospensione !!! (13-12-2010) if (lEventoMod != null) { }else
		 * { if (this.isRequestParameterNullObj("warning")) { setRequestAttribute(IWebConstants.ACTION_FIELD,
		 * "" + getClass().getName()); setRequestAttribute( IWebConstants.MESSAGE_TEXT, "Attenzione: Manca
		 * l'OE con sospensione"); return "/jsp/files/siap/siep/ordineesecuzione/WarningRevocaOEconSosp.jsp";
		 * } }
		 **/

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvVect = new Vector();
		try {
			lAvvVect = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, e.getMessage()
					+ " Impossibile eseguire la revoca del decreto di sospensione.Inserire almeno un avvocato.");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		if (lEventoMod != null) {
			// Warning se il Decreto alfano non è stato notificato agli avvocati.
			Vector lNotifiche = null;
			NotificaModel lNotMod = new NotificaModel();
			INotifica INotifica = SIEPLookupRemote.getNotificaRemote();
			lNotMod.setEveIdEvento(lEventoMod.getIdEvento());
			lNotMod.setCodTipoNotifica("N");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lNotModl = " + lNotMod);
			lNotifiche = INotifica.ExRicercaNotificaAvvocatoNonAvvenuta(lNotMod);

			if (lNotifiche != null && lNotifiche.size() == lAvvVect.size()) {
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lNotifiche.size() = " + lNotifiche.size());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lAvvVect.size() = " + lAvvVect.size());
				/*
				 * if (this.isRequestParameterNullObj("warning")) {
				 * setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
				 * setRequestAttribute( IWebConstants.MESSAGE_TEXT,
				 * "Attenzione: Manca la notifica dell'OE all'avvocato"); return
				 * "/jsp/files/siap/siep/ordineesecuzione/WarningRevocaOEconSosp.jsp"; }
				 */
			}
		}

		setRequestAttribute("avvocati", lAvvVect);

		// Imposta Tipo Istituto
		Option lOptionIstituto = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOptionIstituto);

		// Riempimento ComboBoX
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiepTDSMUDSM(), "-");
		setRequestAttribute("UfficioEmittente", "" + lOption);

		// setto il campo codice motivo
		lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoRigettoMA());
		setRequestAttribute("motivoProvv", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getRevocaDecSosp());
		lOption.setFilter(new String[] { "0002" }); // Solo "Reiezione Istanza"
		setRequestAttribute("motivorevoca", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getMotivoRDS());
		setRequestAttribute("motivorevocapm", "" + lOption);

		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		// Istanza
		EventoModel lEveMod = new EventoModel();
		lEveMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		IEvento lEvCtrl = SICOLookupRemote.getEventoRemote();
		Vector lVectEvento = null;
		try {
			lVectEvento = lEvCtrl.ExRicercaEventoIstanzaRigettata(lEveMod);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE)
				throw e;
		}

		if (lVectEvento != null) {
			if (lVectEvento.size() > 0)
				setRequestAttribute("istanza", lVectEvento.firstElement());
		}

		return PG_LOAD_INSERISCI_REVOCA_SOSPENSIONE_ALFANO; // restituisce la jsp di VIEW
	}
}