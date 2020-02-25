package siap.siep.ordinescarcerazione.action;

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
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 *
 * <p>
 * Title: ActLoadInserisciOrdineScarcerazione
 * </p>
 * <p>
 * Description: ActLoadInserisciOrdineScarcerazione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * not attributable 1.0
 */

public class ActLoadInserisciOrdineScarcerazione extends ActionSiap implements ICostantiOrdineScarcerazione {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ==========================================================================
		// Controllo se Fascicolo Validato
		// ==========================================================================
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non è stato Validato. Impossibile inserire l'Ordine di Scarcerazione!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Controllo se Fascicolo di Competenza
		// ==========================================================================
		this.isFascicoloSiepDiCompetenza();

		// ==========================================================================
		// Controllo se il fascicolo è ARCHIVIATO o DEFINITO
		// ==========================================================================
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

		// ==========================================================================
		// Controllo Esistenza pena residua per quel fascicolo
		// ==========================================================================
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Eseguire prima il calcolo della pena. Impossibile eseguire l'Ordine di Scarcerazione.");

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N")) {
			setRequestAttribute("dataeditabile", "S");
		} else {
			// dataeditabile N
		}

		// ==========================================================================
		// Controllo esistenza almeno un avvocato per fascicolo.
		// ==========================================================================
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		try {
			/* Vector lAvvVect = */lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			throw new SIEPException(SIEPException.USER_MESSAGE, e.getMessage()
					+ " Impossibile eseguire l'Ordine di Scarcerazione. Verificare presenza di avvocati.");
		}

		// ==========================================================================
		//
		// ==========================================================================
		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		// Dalla Tabella Posizione Giuridica estrarre i dati con DataFine =Null e Fascicolo Corrente
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl
				.ExRicercaPosizioneLuogoDetAltraCausaByIdFascicoloDataFineNull(lFascMod.getIdFascicoloSiep());

		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non à stata associata una Posizione Giuridica.");

		setRequestAttribute("posizioneluogoaltra", lPos);

		if ((lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10")
				|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07"))
				&& lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("N"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Impossibile effettuare l'Ordine di Scarcerazione per un Condannato Libero.");

		// Imposta Tipo Istituto
		Option lOptionIstituto = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOptionIstituto);

		// posizione giuridica
		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();

		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().compareTo("N") == 0) {
			IPosizioneGiuridica lPosCtrlControl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			lPosMod = lPosCtrlControl
					.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

			if (lPosMod == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
								+ " non à stata associata una Posizione Giuridica.");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" * * * Posizione = " + lPosMod);
			setRequestAttribute("posizione", lPosMod);
			setRequestAttribute("detenutoAltraCausa", "");
			if (lPosMod != null)
				lDataInizioPena = lPosMod.getDataInizio();
		} else {
			setRequestAttribute("detenutoAltraCausa", "SI");
			// ** COMMENTATO CAUSA REWORK del 17.06.2003 **
			// ** il campo DATA (ALTRA_CAUSA) si trova sulla tabella ALTRA_CAUSA **
			// ** -- lDataInizioPena = lFascMod.getDataAltraCausa(); --
			setRequestAttribute("posizione", null);
		}

		// Riempimento ComboBoX
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		// modifica relativa al tipo istituto
		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
			// modifica relativa al tipo istituto
			if (lPos.getAltraCausa().getIstitutoDetenzione() != null)
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
						lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
		} else {
			// modifica relativa al tipo istituto
			// lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),"35");
			if (lPos.getLuogoDetenzione().getIstitutoDetenzione() != null)
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
						lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
		}

		if (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("12") // Espiazione Pena in Regime
																					// di Detenzione
																					// Domiciliare
				|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("14") // Espiazione Pena in
																						// Regime di
																						// Semiliberta'
				|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04") // Arresti Domiciliari
																						// ex art. 656/10
		) {
			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "35");
			setRequestAttribute("autoritaEsternaE", "" + lOption);
		}
		setRequestAttribute("autoritaEsternaE", "" + lOption);
		setRequestAttribute("autoritaEsternaC", "" + lOption);

		// fine modifica relativa al tipo istituto

		// ==========================================================================
		//
		// ==========================================================================
		EventoModel lEve = new EventoModel();
		lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("evento", lEve);

		// ==========================================================================
		// Sta select l'hai già fatta!!!!
		// ==========================================================================
		IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		lDataInizioPena = llPenMod.getDataInizio();
		// lDataFinePenaM = llPenMod.getDataFine();
		lDataFinePenaA = llPenMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

		// leggo su tabella altra causa
		IAltraCausa lCtrlAltraCausa = SIEPLookupRemote.getAltraCausa();
		AltraCausaModel lAltrMod = lCtrlAltraCausa
				.ExRicercaAltraCausaByFascicolo(lFascMod.getIdFascicoloSiep());
		this.setRequestAttribute("altracausaposizionegiuridica", lAltrMod);

		// ==========================================================================
		// Ricerco Istanza
		// ==========================================================================
		EventoModel lEveMod = new EventoModel();
		lEveMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveMod.setCodTipoEvento("03"); // 03 - Istanza

		IEvento lEvCtrl = SICOLookupRemote.getEventoRemote();
		Vector lVectEvento = null;
		try {
			lVectEvento = lEvCtrl.ExRicercaEvento(lEveMod);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE) {
				throw e;
			}
		}

		if (lVectEvento != null) {
			if (lVectEvento.size() > 0) {
				lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
				setRequestAttribute("tipoufficiosius", "" + lOption);
			}
		}
		return PG_INSERISCI_ORDINE_SCARCERAZIONE; // restituisce la jsp di VIEW
	}

}