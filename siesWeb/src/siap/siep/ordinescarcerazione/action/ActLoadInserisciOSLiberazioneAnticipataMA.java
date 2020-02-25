package siap.siep.ordinescarcerazione.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.w_magistrato.controller.IWMagistrato;
import siap.sico.w_magistrato.model.WMagistratoModel;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadInserisciOSLiberazioneAnticipataMA extends ActOrdineScarcerazione
		implements ICostantiOrdineScarcerazione {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		String lControl = controllaFascicolo();
		if (lControl != null)
			return lControl;

		if (this.isRequestParameterNullObj("lAzioneOS")) {
			this.isEventoNonValidato();
		}

		LicenzaLibAnticipataModel llibAntMod = null;
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// Controllo Esistenza LiberazioneAnticipata
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();

		// verifica esistenza evento liberazione anticipata
		EventoModel lEveMod = new EventoModel();
		// Se vengo da ActCalcoloPenaLiberazioneAnticipata
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		if (isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO)) {
			String[] lArrayMot = { "0076", "2130" };
			List lListEventi = lEveCtrl.ExRicercaEventiNOTAnnullati(lFascMod.getIdFascicoloSiep(), lArrayMot,
					"03", "01");

			if (!lListEventi.isEmpty()) {
				lEveMod = (EventoModel) lListEventi.get(0);
			}
		} else // Se vengo da Menu orizzontale
		{
			BigDecimal lIdEventoOrdinanza = getRequestBigDecimalParameter(
					ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO);
			lEveMod = lEveCtrl.ExRicercaEventoByKey(lIdEventoOrdinanza);
		}

		// Ricerco la liberazione anticipata
		// 20/05/2014 Nuova L.A. : E' indispensabile inserire nella request il Vector VectLA per poter
		// distingure le diverse
		// tipologie di L.A. con i rispettivi giorni concessi

		DepositoOrdinanzaPcModel lDepOrdMod = null;
		Vector VectLA = new Vector();
		if (lEveMod != null && lEveMod.getIdEvento() != null) {
			// RICERCA DEPOSITO_ORDINANZA_PC
			IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lDepOrdMod = lCtrlDep.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod.getIdEvento());

			VectLA = lCtrlLib.ExRicercaLicenzeByEve(lEveMod.getIdEvento());
			if (!VectLA.isEmpty()) {
				llibAntMod = (LicenzaLibAnticipataModel) VectLA.get(0);
			}

			setRequestAttribute("lIdEventoOrdinanza", lEveMod.getIdEvento());
		}

		try {
			// llibAntMod =
			// lCtrlLib.ExRicercaLicenzaLibanticipataConcessayIDFascicoloSIEP(lFascMod.getIdFascicoloSiep(),
			// "E");
			// int lTotGiorniConcessi =
			// lCtrlLib.ExTotalePeriodiConcessiComputatiByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			int lTotGiorniConcessi = 0;

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
			// 20/05/2014 Nuova L.A. E' indispensabile prendere i gg concessi totali da
			// lDepOrdMod.getNumGiorniLibanticipata(), ma NON si possono
			// inserire nelle licenze (llibAntMod.setNumeroGiorni(lDepOrdMod.getNumGiorniLibanticipata());) in
			// quanto serve anche
			// il n.ro gg della singola Licenza; i gg concessi totali Vanno inseriti nella request in un campo
			// stringa lTotGiorniConcessi

			setRequestAttribute("VetLicenze", VectLA);
			setRequestAttribute("lTotGiorniConcessi", "" + lTotGiorniConcessi);
		} catch (Exception e) {
			// nessun elemento trovato
		}

		if (llibAntMod == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Non ci sono dati sufficienti per emette Ordine di Scarcerazione.");
		}

		// modifica 27-02-2006 -- Dario -- Viviana
		// Bisogna visualizzare i Rigettati,Inammissibili e N.L.P./N.D.P.
		Vector llibAntModNonConcessi = null;
		llibAntModNonConcessi = lCtrlLib.ExRicercaLicenzaLibanticipataNonConcesseByIDFascicoloSIEP(
				lFascMod.getIdFascicoloSiep(), llibAntMod.getEveIdEvento());

		setRequestAttribute("liberazioninonconcesse", llibAntModNonConcessi);

		// Controllo Esistenza pena residua non validata per quel fascicolo

		controllaPenaResiduaAvvocato(lFascMod.getIdFascicoloSiep());
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();

		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaPerFascicolo(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Eseguire prima il calcolo della pena. Impossibile eseguire l'Ordine di Scarcerazione.");
		/*
		 * if ( (lPenaResMod != null)&& (lPenaResMod.getFlagValidato().equals("N"))) throw new
		 * SIEPException(SIEPException.USER_MESSAGE,
		 * "Verificare e Validare il Calcolo della Pena. Esiste una Pena Residua non Validata per altri Provvedimenti. Impossibile eseguire l'Ordine di Scarcerazione."
		 * );
		 */
		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvVect = null;
		try {
			lAvvVect = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					e.getMessage() + " Impossibile eseguire l'Ordine di Scarcerazione.");
		}

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		Date lDataFinePenaM = null;

		// Imposta Tipo Istituto
		Option lOptionIstituto = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOptionIstituto);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		/*
		 * // posizione giuridica PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		 * 
		 * //NOn è altra Causa if (lFascMod.getFlagAltraCausa().compareTo("N") == 0) { IPosizioneGiuridica
		 * lPosCtrlControl = SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPosMod =
		 * lPosCtrlControl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		 * 
		 * if (lPosMod == null) throw new SIEPException(SIEPException.USER_MESSAGE, "Al Procedimento N." +
		 * lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() +
		 * " non à stata associata una Posizione Giuridica.");
		 * 
		 * if ( (lPosMod.getCodPosizioneGiuridica().equals("10") ||
		 * lPosMod.getCodPosizioneGiuridica().equals("07"))) throw new
		 * SIEPException(SIEPException.USER_MESSAGE,
		 * "Impossibile effettuare l'Ordine di Scarcerazione per un Condannato Libero.");
		 * 
		 * setRequestAttribute("posizione", lPosMod); setRequestAttribute("detenutoAltraCausa", "NO");
		 * lDataInizioPena = lPosMod.getDataInizio();
		 * 
		 * ILuogoDetenzione lLuo = SIEPLookupRemote.getLuogoDetenzioneRemote(); LuogoDetenzioneModel lDet =
		 * lLuo.ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep(lPosMod.getFasSieIdFascicoloSiep()); if
		 * (lDet.getIstitutoDetenzione() != null) lOption = new
		 * Option(DecodificheManager.getInstance().getTipoAutorita(),
		 * lDet.getIstitutoDetenzione().getCodTipoIstituto());
		 * 
		 * setRequestAttribute("luogoDetenzione", lDet); } else //ALTRA CAUSA { //Dalla Tabella Posizione
		 * Giuridica estrarre i dati con DataFine =Null e Fascicolo Corrente IPosizioneGiuridica lPosCtrl =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPos =
		 * lPosCtrl.ExRicercaPosizioneLuogoDetAltraCausaByIdFascicoloDataFineNull(lFascMod.getIdFascicoloSiep(
		 * ));
		 * 
		 * if (lPos == null || lPos.getPosizioneGiuridica() == null) throw new
		 * SIEPException(SIEPException.USER_MESSAGE, "Al Procedimento N." + lFascMod.getChiaveAnno() + "/" +
		 * lFascMod.getChiaveProgr() + " non à stata associata una Posizione Giuridica.");
		 * 
		 * if (lPos.getAltraCausa().getIstitutoDetenzione() != null) lOption = new
		 * Option(DecodificheManager.getInstance().getTipoAutorita(),
		 * lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
		 * 
		 * setRequestAttribute("posizioneluogoaltra", lPos); setRequestAttribute("detenutoAltraCausa", "SI");
		 * 
		 * IAltraCausa lCtrlAltraCausa = SIEPLookupRemote.getAltraCausa(); AltraCausaModel lAltrMod =
		 * lCtrlAltraCausa.ExRicercaAltraCausaByFascicolo(lFascMod.getIdFascicoloSiep());
		 * setRequestAttribute("altracausaposizionegiuridica", lAltrMod); }
		 */
		/******************************* Posizione Giuridica **********************************/
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltra = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
						lFascMod.getIdFascicoloSiep());

		if (lPosLuoAltra == null || lPosLuoAltra.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		if (lPosLuoAltra.getPosizioneGiuridica().isLibero())
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Impossibile effettuare l'Ordine di Scarcerazione per un Condannato Libero.");

		if (!lPosLuoAltra.getPosizioneGiuridica().isMisAlt()
				&& !lPosLuoAltra.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")
				// MEV 10 S3 - Gestione nuove Posizioni Giuridiche
				&& !lPosLuoAltra.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
				&& !lPosLuoAltra.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("70")
				&& !lPosLuoAltra.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("71")
				&& !lPosLuoAltra.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("72")
				&& !lPosLuoAltra.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("82")
				&& !lPosLuoAltra.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("83")
				&& !lPosLuoAltra.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("84")
				&& !lPosLuoAltra.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("85")
				&& !lPosLuoAltra.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("86")
				&& !lPosLuoAltra.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("87"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Impossibile effettuare l'Ordine di Scarcerazione per un Condannato non in Misura Alternativa.");

		setRequestAttribute("posizioneluogoaltra", lPosLuoAltra);

		/******************************* Pena Complessiva *****************************/

		// Riempimento ComboBoX

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		// fine modifica relativa al tipo istituto
		EventoModel lEve = new EventoModel();
		lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

		// Ricerca Magistrato
		IWMagistrato lWCtrl = SICOLookupRemote.getWMagistratoRemote();
		WMagistratoModel lMagi = lWCtrl.ExRicercaWMagistratoByKey(lEve.getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);
		setRequestAttribute("avvocati", lAvvVect);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("evento", lEve);

		lDataInizioPena = lPenaResMod.getDataInizio();
		lDataFinePenaM = lPenaResMod.getDataFine();
		lDataFinePenaA = lPenaResMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaM", DateUtils.getDateToString(lDataFinePenaM, "dd-MM-yyyy"));

		setRequestAttribute("penaresidua", lPenaResMod);

		// setto il campo codice motivo
		lOption = new Option(DecodificheManager.getInstance().getMotivoOrdineScarcerazione());
		setRequestAttribute("motivoProvv", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "TDS");
		setRequestAttribute("tipoUfficio", "" + lOption);

		// magistrato con TIPO NOTIFICA = C

		/*
		 * if (lMisAlModConcessa != null) { String lCodMag = lMisAlModConcessa.getCodMagistrato();
		 * MagistratoModel lMagSorvMod = new MagistratoModel(); IMagistrato lCtrlMagSorv =
		 * SICOLookupRemote.getMagistratoRemote(); lMagSorvMod =
		 * lCtrlMagSorv.ExRicercaMagistratoByCod(lCodMag); setRequestAttribute("magistratosorveglianza",
		 * lMagSorvMod); }
		 */
		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagiMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		if (lMagiMod != null)
			setRequestAttribute("magistratocompetente", lMagiMod);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UfficioModel lUfficioMod = this.getUfficioUtenteConnesso();
		String lCodTipoUfficio = lUfficioMod.getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		return PG_INSERISCI_OS_LIBERAZIONE_ANTICIPATA_MA; // restituisce la jsp di VIEW
	}
}