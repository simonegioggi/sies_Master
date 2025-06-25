package siap.sico.soggetto.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciSoggetto
 * </p>
 * <p>
 * Description: Azione di Inserimento del Soggetto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActInserisciSoggetto extends ActionSiap implements ICostantiSoggetto {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Soggetto
	 * <p>
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		SoggettoModel lSogMod = new SoggettoModel();
		this.setLinkRitorno();

		UtenteModel lUtente = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		if (!this.isRequestParameterNullObj("lTipoFunzione")) {
			// paramentro passato
			// solo nel caso di
			// iscrizione guidata
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		// ---12/07/2006 Eliminato Codice CS lSogMod.setCodCs(
		// getRequestStringParameter(CAMPO_COD_CS).toUpperCase() );
		lSogMod.setCodFiscale(getRequestStringParameter(CAMPO_COD_FISCALE).toUpperCase());
		lSogMod.setCodAfis(getRequestStringParameter(CAMPO_COD_AFIS).toUpperCase());
		lSogMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lSogMod.setAnnoNascita(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_NASCITA));
		lSogMod.setMeseNascita(getRequestBigDecimalParameter(CAMPO_MESE_DATA_NASCITA));
		lSogMod.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
				CAMPO_GIORNO_DATA_NASCITA));
		if (!this.isRequestParameterNullObj(CAMPO_ANNO_DATA_COMMESSO_REATO)
				&& !this.isRequestParameterNullObj(CAMPO_MESE_DATA_COMMESSO_REATO)
				&& !this.isRequestParameterNullObj(CAMPO_GIORNO_DATA_COMMESSO_REATO)) {
			lSogMod.setDataReatoSius(getRequestDateParameter(CAMPO_ANNO_DATA_COMMESSO_REATO,
					CAMPO_MESE_DATA_COMMESSO_REATO, CAMPO_GIORNO_DATA_COMMESSO_REATO));
		}
		lSogMod.setDataNascitaPresunta(getRequestStringParameter(CAMPO_DATA_NASCITA_PRESUNTA));
		// MERGE v10 COLLAUDO: aggiunti controlli preventivi
		if (!this.isRequestParameterNullObj(CAMPO_ETA_PRESUNTA_ANNI))
			lSogMod.setEtaPresuntaAnni(getRequestBigDecimalParameter(CAMPO_ETA_PRESUNTA_ANNI));
		if (!this.isRequestParameterNullObj(CAMPO_ETA_PRESUNTA_MESI))
			lSogMod.setEtaPresuntaMesi(getRequestBigDecimalParameter(CAMPO_ETA_PRESUNTA_MESI));

		// Recupero dati del Comune di nascita
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			// 20210524	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag validità.
			//lComMod = new ComuneModel(getDatiComuneByCodDescrFlagVal(
			lComMod = new ComuneModel(getDatiComuneByCodDescr(
					getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
					getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(
					// 20210524	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag validità.
					//getDatiComuneByDescrOmonimiaFlagVal(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
					getDatiComuneByDescrOmonimia(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		}
		lSogMod.setCodComuneNascita(lComMod.getCodComune());
		lSogMod.setCodProvinciaNascita(lComMod.getCodProvincia());

		// if (!this.isRequestParameterNullObj(CAMPO_NAZIONALITA) &&
		// getRequestStringParameter(CAMPO_NAZIONALITA) != null &&
		// getRequestStringParameter(CAMPO_NAZIONALITA).equals("E")) {

		// Paolo Cherubini 21/12/2010
		// prendo il casellario di roma per gli stranieri (CAMPO_COD_STATO_NASCITA != 039 -->
		// CodComuneCasellario = 342 roma)
		// prendo la sede giudiziaria del luogo di nascita per gli italiani (CAMPO_COD_STATO_NASCITA = 039 -->
		// CodComuneCasellario = sede giudiziaria comune di nascita)
		if (!this.isRequestParameterNullObj(CAMPO_COD_STATO_NASCITA)
				&& getRequestStringParameter(CAMPO_COD_STATO_NASCITA) != null
				&& !getRequestStringParameter(CAMPO_COD_STATO_NASCITA).equals("039")) {
			lSogMod.setCodComuneCasellario("342");
		} else {
			lSogMod.setCodComuneCasellario(lComMod.getCodSedeGiudiziaria());
		}

		lSogMod.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
		lSogMod.setDescComuneNascitaEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO));
		lSogMod.setNazionalita(getRequestStringParameter(CAMPO_NAZIONALITA));
		lSogMod.setPaternita(getRequestStringParameter(CAMPO_PATERNITA));
		lSogMod.setCognomeMadre(getRequestStringParameter(CAMPO_COGNOME_MADRE));
		lSogMod.setNomeMadre(getRequestStringParameter(CAMPO_NOME_MADRE));
		lSogMod.setSesso(getRequestStringParameter(CAMPO_SESSO));
		lSogMod.setAttoNascita(getRequestStringParameter(CAMPO_ATTO_NASCITA).toUpperCase());
		lSogMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lSogMod.setFlagPresenzaFascicolo("N");

		lSogMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lSogMod.setDataInserimento(DateUtils.getSysDate());
		lSogMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// Imposto la descrizione della nazione
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(lSogMod.getCodStatoNascita());
		List lNazioni = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModel = lNazioni.indexOf(lDecMod);
		String lDescri = ((DecodificheModel) lNazioni.get(lIndModel)).getDescription();
		lSogMod.setDescrStatoNascita(lDescri);

		lSogMod.setDescrComuneNascita(lComMod.getDescrizione());

		// Imposto la descrizione del comune
		lDecMod = new DecodificheModel();
		lDecMod.setContesto("PROVINCIA");
		lDecMod.setCode(lSogMod.getCodProvinciaNascita());
		List lProvincie = (List) DecodificheManager.getInstance().getProvincie();
		lIndModel = lProvincie.indexOf(lDecMod);
		lDescri = ((DecodificheModel) lProvincie.get(lIndModel)).getDescription();
		lSogMod.setDescrProvinciaNascita(lDescri);

		// Imposto la descrizione di Stato Cittadinanza (nel model sono NAZIONALITA e DESCRNAZIONALITA)
		lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(lSogMod.getNazionalita());
		List lStatoCitt = (List) DecodificheManager.getInstance().getStatoCittadinanza();
		lIndModel = lStatoCitt.indexOf(lDecMod);
		lDescri = ((DecodificheModel) lStatoCitt.get(lIndModel)).getDescription();
		lSogMod.setDescrNazionalita(lDescri);

		// Se non c'è il flag si fa il controllo
		if (isRequestParameterNullObj(FLAG_OMONIMI))
			lSogMod.setMessage("omonimi");

		// Chiama il controller
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		IFascicoloSiep lFascSogCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		SoggettoModel lSogRetMod = null;
		// SoggettoModel lSogRetMod = new SoggettoModel();
		// lSogRetMod = lSogCtrl.ExInserisciSoggetto(lSogMod);

		String lPage = ""; // per passaggio a jsp
		String lPagina = "1"; // per contare ricerca

		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

//		String lReturnPage = "";
		BigDecimal CountRisultati = null;

		// MEV 15 Revisione Sige Parte 2
		// In caso di Omonimia l'inserimento del soggetto avverrà solo
		// dopo conferma da parte dell'utente
		// Vengono caricati anche i Fascicoli Sige a cui è asociato il soggetto
		if (lUtente.getUserProfile() != null && lUtente.getUserProfile().isSige()) {
			lSogMod.setDescrComuneNascita(lComMod.getDescrizione());
			Vector lSoggettiOmonimi = lSogCtrl.ExRicercaSoggettiFascicoliOmonimi(lSogMod);
			// presenza omonimi
			if (lSoggettiOmonimi.size() > 0) {
				lPage = PG_LOAD_INSERISCIOMONIMI_SIGE;
				setRequestAttribute("SoggOmonimi", lSoggettiOmonimi);
				setRequestAttribute("soggetto", lSogMod);
				setRequestAttribute("newsoggetto", lSogRetMod);
				this.setFunctionsAvailableToRequest("siap.sico.soggetto.action.ActRicercaSoggetto");
			} else {
				lSogRetMod = lSogCtrl.ExInserisciSoggetto(lSogMod);
			}
		} else {
			lSogRetMod = lSogCtrl.ExInserisciSoggetto(lSogMod);
		}

		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		if (lSogRetMod != null && lSogRetMod.getMessage().startsWith("Inserimento")) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&" + CAMPO_ID_SOGGETTO + "="
					+ lSogRetMod.getIdSoggetto().toString();
		} else if (lSogRetMod != null && lSogRetMod.getMessage().startsWith("Omonimi")) {
			lPage = PG_LOAD_INSERISCIOMONIMI;
			lSogMod.setDescrComuneNascita(lComMod.getDescrizione());
			Vector lSoggettiOmonimi = lSogCtrl.ExRicercaSoggettiOmonimi(lSogMod);
			setRequestAttribute("SoggOmonimi", lSoggettiOmonimi);
			setRequestAttribute("soggetto", lSogMod);
			setRequestAttribute("newsoggetto", lSogRetMod);
			this.setFunctionsAvailableToRequest("siap.sico.soggetto.action.ActRicercaSoggetto");
		} else if (lSogRetMod != null && lSogRetMod.getMessage().startsWith("Soggetto")) {
			SoggettoModel lSogCuiMod = new SoggettoModel();
			String uffcio = this.getCodUfficioUtenteConnesso();
			String distretto = this.getCodDistrettoUtenteConnesso();
			String TipoRicerca = "ufficio";
			lSogCuiMod.setCodAfis(lSogRetMod.getCodAfis());
			Vector lFascicoliSoggetti = null;
			try {
				lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascicoliBySoggettoPaged(lSogCuiMod, uffcio,
						Integer.parseInt(lPagina), distretto, TipoRicerca);
			} catch (Exception sEx) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&" + CAMPO_ID_SOGGETTO + "="
						+ lSogRetMod.getIdSoggetto().toString();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Fascicoli non trovati per quel soggetto");
			}

			if (lFascicoliSoggetti != null) {
				// Ambros SuperSoggetto 08/2009
				// Vector lFascicoliSoggetti =
				// lFascSogCtrl.ExRicercaFascicoliBySuperSoggettoPaged(lSogCuiMod,uffcio,Integer.parseInt(lPagina),distretto,TipoRicerca);

				setRequestAttribute("fascicoli", lFascicoliSoggetti);
				if (isRequestParameterNullObj("CountRisultati")) {
					CountRisultati = lSogCtrl.ExGetCountSoggettiPerProcedimenti(lSogMod, uffcio, distretto,
							TipoRicerca);
				} else {
					CountRisultati = getRequestBigDecimalParameter("CountRisultati");
				}

				// Vector lSoggettiOmonimi = lSogCtrl.ExRicercaSoggetto(lSogCuiMod);
				// setRequestAttribute("SoggOmonimi", lSoggettiOmonimi);

				setRequestAttribute("soggetto", lSogRetMod);
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

				lPage = PG_LOAD_INSERISCICUI;
				this.setFunctionsAvailableToRequest("siap.sico.soggetto.action.ActRicercaSoggetto");
			}
		}
//		else {
			// lPage = IWebConstants.PG_MESSAGE;
			// setRequestAttribute(IWebConstants.MESSAGE_TEXT,lSogRetMod.getMessage());
			// // Prepara la "pagina" di destinazione
			// RedirectTo lRedirigi = new RedirectTo();
			// lRedirigi.setPage(IWebConstants.PG_MAIN);
			// lRedirigi.setAction("siap.sico.soggetto.action.ActLoadDettaglioSoggetto");
			// lRedirigi.setParameter(CAMPO_ID_SOGGETTO,lSogRetMod.getIdSoggetto().toString());
			// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
//		}
		// valore di ritorno
		return lPage;
	}

}