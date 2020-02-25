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

public class ActLoadInserisciRevocaSospensioneSimeone extends ActionSiap
		implements ICostantiOrdineEsecuzione {
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
		if (lEventoMod != null) {
		} else {
			if (this.isRequestParameterNullObj("warning")) {
				setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Attenzione: Manca l'OE con sospensione");
				return "/jsp/files/siap/siep/ordineesecuzione/WarningRevocaOEconSosp.jsp";
			}
		}

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
			// Warning se il Decreto simeone non è stato notificato agli avvocati.
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
				if (this.isRequestParameterNullObj("warning")) {
					setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Attenzione: Manca la notifica dell'OE all'avvocato");
					return "/jsp/files/siap/siep/ordineesecuzione/WarningRevocaOEconSosp.jsp";
				}
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

		// setto il campo codice motivo
		lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoRigettoMA());
		setRequestAttribute("motivoProvv", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getRevocaDecSosp());
		setRequestAttribute("motivorevoca", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getMotivoRDS());
		setRequestAttribute("motivorevocapm", "" + lOption);

		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// Lista UFFICIO DI SORVEGLIANZA (-/TDS/TDSM)
		Option lOptionUffDest = new Option(DecodificheManager.getInstance().getTipoUfficioSiepTDSMUDSM(),
				"-");
		String[] lFiltro = new String[3];
		lFiltro[0] = "-";
		lFiltro[1] = "TDS";
		lFiltro[2] = "TDSM";
		lOptionUffDest.setFilter(lFiltro);
		setRequestAttribute("tipUffDestEntSor", "" + lOptionUffDest);

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

		return PG_LOAD_INSERISCI_REVOCA_SOSPENSIONE_SIMEONE; // restituisce la jsp di VIEW
	}

}