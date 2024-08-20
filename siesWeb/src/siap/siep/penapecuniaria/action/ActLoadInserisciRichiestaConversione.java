package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActLoadInserisciRichiestaConversione - Classe Action per la load inserisci di RichiestaConversione
 *
 * @version 1.0
 */
public class ActLoadInserisciRichiestaConversione extends ActionSiap implements ICostantiPenaPecuniaria {

	/**
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare i tale pagina (es: combo)
	 *
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		isFascicoloSiepDiCompetenza();

		this.isEventoNonValidato();

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		BigDecimal lidFascicoloSiep = null;
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lidFascicoloSiep = lFascMod.getIdFascicoloSiep();

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

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
		/*
		 * 07/07/2015 Pene Residua non più presente per MEV27 // Ricerca l'ultima pena residua per quel
		 * fascicolo IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel
		 * lPenaResMod =
		 * lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 *
		 * if( lPenaResMod == null || ( lPos.getPosizioneGiuridica().getCodPosizioneGiuridica()!= null &&
		 * !lPos.getPosizioneGiuridica().isLibero() &&
		 * !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("-") && lPenaResMod != null &&
		 * lPenaResMod.getDataInizio()== null ) ) { //throw new SIEPException(SIEPException.USER_MESSAGE,
		 * "Eseguire prima il calcolo della pena. Impossibile eseguire l'ordine di esecuzione.");
		 *
		 * RedirectTo lRedirigi = new RedirectTo(); lRedirigi.setPage(IWebConstants.PG_MAIN); if
		 * (lPenaResMod==null) { setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		 * "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?"); } else {
		 * setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		 * "Pena Residua da Espiare incoerente con Posizione Giuridica. Eseguire Calcolo della pena?"); }
		 *
		 * lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
		 * ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		 *
		 * return IWebConstants.PG_MESSAGE; }
		 */
		// 07/07/2015

		IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();

		IPenaComplessiva lCtrlPC = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPCMod = new PenaComplessivaModel();
		IPenaResidua lCtrlPR = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPRMod = new PenaResiduaModel();
		int lFascProg = lFascMod.getChiaveProgr().intValue();
		// MG
		setRequestAttribute("multaResidua", new BigDecimal("0"));
		setRequestAttribute("ammendaResidua", new BigDecimal("0"));
		// fine MG
		if (lFascProg > 70000 && lFascProg < 80000) {
			// se sono in classe VII vado a cercare la richiesta di conversione legata al procedimento
			lRicMod = lCtrlRic.ExRicercaRichiestaConversioneByIdFascicoloSiep(lidFascicoloSiep);
			// se sono in classe VII vado a cercare la pena complessiva per passare i quantum.
			lPCMod = lCtrlPC.ExRicercaPenaComplessivaByIdFascicolo(lidFascicoloSiep);
			/*
			 * ISSUE MAC : Gli attributi multaResidua e ammendaResidua vengono passati alla request come
			 * BigDecimal e non più come String 
			 * Numero MAC : 20191202018 
			 * Autore : monica 
			 * Data : 13/dic/2019
			 * Branch : 11.2.4
			 */
			setRequestAttribute("multaResidua", lPCMod.getImportoMulta());
			setRequestAttribute("ammendaResidua", lPCMod.getImportoAmmenda());
			// ***** FINE INTERVENTO 20191202018*****//
		} else {
			// se sono in classe I vado a cercare la richiesta di conversione legata al procedimento collegato
			// ossia di classe VII
			lRicMod = lCtrlRic.ExRicercaRichiestaConversioneByIdFascicoloSiepClasseI(lidFascicoloSiep);
			// se sono in classe I vado a cercare la pena residua per passare i quantum.
			lPRMod = lCtrlPR.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lidFascicoloSiep);
			/*
			 * ISSUE MAC : Gli attributi multaResidua e ammendaResidua vengono passati alla request come
			 * BigDecimal e non più come String 
			 * Numero MAC : 20191202018 
			 * Autore : monica 
			 * Data : 13/dic/2019
			 * Branch : 11.2.4
			 */
			if (lPRMod != null && lPRMod.getImportoMulta() != null)
				setRequestAttribute("multaResidua", lPRMod.getImportoMulta());
			if (lPRMod != null && lPRMod.getImportoAmmenda() != null)
				setRequestAttribute("ammendaResidua", lPRMod.getImportoAmmenda());
			// ***** FINE INTERVENTO 20191202018*****//
		}

		if (lRicMod != null && lRicMod.getIdRichiestaConversione() != null) {
			setRequestAttribute("richiestaconversione", lRicMod);
			return PG_LOAD_DETTAGLIO_RICHIESTA_CONVERSIONE;
		}

		setRequestAttribute("richiestaconversione", lRicMod);

		// Autorità per la conversione.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		lOption.setFilter(new String[] { "-", "36", "99", "57", "37", "98", "38" });
		setRequestAttribute("autoritaConv", "" + lOption);

		// Si Imposta l'Autorita Competente.
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOption.setFilter(new String[] { "PM", "PMM", "PGCAP" }); // solo le Autorità competenti.
		lOption.setSelected("PM");
		setRequestAttribute("AutoritaCompetente", "" + lOption);

		// esegue la query per recuperare l'elenco degli uffici accorpati
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUACon.ListaUfficiAccorpati(null, null);
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCIRICHIESTACONVERSIONE;

		/*
		 *
		 * if (this.isSessionAttributeNullObj("fascicolo")) { return
		 * ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName(); }
		 *
		 * FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		 *
		 * // controllo se ho gia iscritto il fascicolo della conversione pene pecuniare // la numerazione del
		 * fascicolo della conversione pene pecuniare va da 70000 a 80000 // in questo caso posso iscrivere la
		 * richiesta anche se il facicolo non è validato (se vengo da Combo) // e non ha effettuato il calcolo
		 * della pena
		 *
		 * if (!isRequestParameterNullObj(FORM_NAME) ) { String lFormName = ( getRequestStringParameter (
		 * FORM_NAME) ); if (lFormName.equals("GrigliaBottoniConversione" )) { int lFascProg =
		 * lFascMod.getChiaveProgr().intValue(); if (lFascProg > 70000 && lFascProg < 80000) {
		 * isFascicoloSiepDiCompetenza();
		 *
		 * // Se vengo da 'GrigliaBottoniConversione' il Fascicolo DEVE ESSERE VALIDATO if
		 * ("N".equalsIgnoreCase(lFascMod.getFlagValidato())) {
		 *
		 * throw new F3BException(F3BException.USER_MESSAGE, "Il Procedimento N." + lFascMod.getChiaveAnno() +
		 * "/" + lFascMod.getChiaveProgr() + " non è validato. Lavorare dal Menù a tendina."); }
		 *
		 * if (isFascicoloArchiviatoDefinito()) return IWebConstants.PG_MESSAGE;
		 *
		 * this.isEventoNonValidato();
		 *
		 * IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
		 * RichiestaConversioneModel lRicMod = new RichiestaConversioneModel(); Vector lRichiestaConversioni =
		 * new Vector(); lRicMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 * lRichiestaConversioni=lCtrlRic.ExRicercaRichiestaConversione(lRicMod); // Mettere Iterartor : Se
		 * Non Trovo nessuna Richiesta devo inserirla Senza // Duplicare Fascicolo
		 *
		 * if(lRichiestaConversioni.size()> 0) { lRicMod=
		 * (RichiestaConversioneModel)lRichiestaConversioni.get(0); lRicMod =
		 * lCtrlRic.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione());
		 * setRequestAttribute("richiestaconversione",lRicMod); // // [FT] - 03/08/2016 - MAC_LOG - Utilizzo
		 * la variabile di istanza siesLogger al posto di LogF3B.getLogger() //
		 * siesLogger.debug("lRicMod 43 = " + lRicMod); return PG_LOAD_DETTAGLIO_RICHIESTA_CONVERSIONE; }
		 *
		 *
		 * } // chiudo If Classe VII
		 *
		 * } // Chiudo If vengo da GrigliaBottoniConversione }
		 *
		 *
		 * int lFascProg = lFascMod.getChiaveProgr().intValue(); if (lFascProg < 70001 || lFascProg > 80000) {
		 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() //siesLogger.debug("chiave progr < 70001 &&  lFascProg > 80000"); //// [FT] -
		 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * //siesLogger.debug("controllo validato");
		 *
		 * isFascicoloSiepDiCompetenza();
		 *
		 * if(isFascicoloNonValidato()) return IWebConstants.PG_MESSAGE;
		 *
		 * // Paolo cherubini 13/01/2011 permetto la conversione anche per fascicoli archiviati // come
		 * richiesta da Nunzia e Michele con email del 13/01/2011 // if (isFascicoloArchiviatoDefinito()) //
		 * return IWebConstants.PG_MESSAGE;
		 *
		 * this.isEventoNonValidato();
		 *
		 * //Controllo Esistenza pena residua non validata per quel fascicolo PenaResiduaModel lPenaResMod =
		 * new PenaResiduaModel(); IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		 * lPenaResMod =
		 * lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep()); if
		 * (notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod)) return IWebConstants.PG_MESSAGE;
		 *
		 * } // chiude lFascProg < 70000 || lFascProg > 80000 else { IRichiestaConversione lCtrlRic =
		 * SIEPLookupRemote.getRichiestaConversioneRemote(); RichiestaConversioneModel lRicMod = new
		 * RichiestaConversioneModel(); Vector lRichiestaConversioni = new Vector();
		 * lRicMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 * lRichiestaConversioni=lCtrlRic.ExRicercaRichiestaConversione(lRicMod); // Mettere Iterartor : Se
		 * Non Trovo nessuna Richiesta devo inserirla Senza // Duplicare Fascicolo
		 *
		 * if(lRichiestaConversioni.size()> 0) { lRicMod=
		 * (RichiestaConversioneModel)lRichiestaConversioni.get(0); lRicMod =
		 * lCtrlRic.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione());
		 * setRequestAttribute("richiestaconversione",lRicMod); // // [FT] - 03/08/2016 - MAC_LOG - Utilizzo
		 * la variabile di istanza siesLogger al posto di LogF3B.getLogger() //
		 * siesLogger.debug("lRicMod 43 = " + lRicMod); return PG_LOAD_DETTAGLIO_RICHIESTA_CONVERSIONE; }
		 *
		 * }
		 *
		 *
		 * IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod =
		 * lCtrlPos.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.
		 * getIdFascicoloSiep()); setRequestAttribute("posizioneluogoaltra", lPosLuoAltMod);
		 *
		 * // residenza IResidenza lResCtrl = SICOLookupRemote.getResidenzaRemote(); Vector lVecRes =
		 * lResCtrl.ExRicercaResidenzeByIdFascicolo(lFascMod.getIdFascicoloSiep()); ResidenzaAssociataModel
		 * lResAssMod = new ResidenzaAssociataModel();
		 *
		 * if(lVecRes != null && !lVecRes.isEmpty()) { lResAssMod = (ResidenzaAssociataModel)lVecRes.get(0); }
		 * setRequestAttribute("residenzaassociata", lResAssMod);
		 *
		 * // domicilio IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		 * ResidenzaAssociataModel lDomAssMod =
		 * lCtrlFas.ExRicercaDomicilioFascicoloSiepCorrente(lFascMod.getIdFascicoloSiep());
		 * setRequestAttribute("domicilioassociato", lDomAssMod);
		 *
		 * // Autorità per la conversione. Option lOption = new Option(
		 * DecodificheManager.getInstance().getTipoAutorita()); lOption.setFilter( new String[] {"-","36",
		 * "99", "57","37", "98", "38"}); setRequestAttribute("autoritaConv", "" + lOption );
		 *
		 * // Imposta la Modalità a Inserimento. setRequestAttribute("modalita", "I");
		 *
		 * // Restituisce la pagina di Inserimento dei Dati return PG_LOAD_INSERISCIRICHIESTACONVERSIONE;
		 */
	}

}