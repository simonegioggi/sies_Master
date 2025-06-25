package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggettoFascicolo;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActRicercaSoggettoIscrizioneMisuraProvvisoria
 * </p>
 * <p>
 * Description: Ricerca Soggetto per l'iscrizione del procedimento di Misura Sicurezza Provvisoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Intersistemi Italia s.p.a.
 * </p>
 * 
 * @version 8.2
 */
@SuppressWarnings("rawtypes")
public class ActRicercaSoggettoIscrizioneMisuraProvvisoria extends ActionSiap implements ICostantiSoggetto {

	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		SoggettoModel lSogMod = new SoggettoModel();

		// parse della request
		if (getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA).length() > 1) {
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
			// 20250612 [SG]: risolto problema ricerca soggetto col "-" pari al cod comune nascita
			// Ticket#20250612016 - SIES - ricerche soggetto
			String codComuneNascita = "-".equals(lComMod.getCodComune()) ? "" : lComMod.getCodComune();
			lSogMod.setCodComuneNascita(codComuneNascita);
		}

		// riempie il model
		lSogMod.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
		lSogMod.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());

		if (getRequestStringParameter(CAMPO_ANNO_DATA_NASCITA).length() > 2) {
			lSogMod.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
					CAMPO_GIORNO_DATA_NASCITA));
		}

		if (!getRequestStringParameter(CAMPO_COD_STATO_NASCITA).equals("-")) {
			lSogMod.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
		}

		lSogMod.setPaternita(getRequestStringParameter(CAMPO_PATERNITA));
		lSogMod.setCognomeMadre(getRequestStringParameter(CAMPO_COGNOME_MADRE));
		lSogMod.setNomeMadre(getRequestStringParameter(CAMPO_NOME_MADRE));

		lSogMod.setCodAfis(getRequestStringParameter(CAMPO_COD_AFIS));

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" --XX-- ActRicercaSoggettoIscrizioneMisuraProvvisoria - NumerazioneManuale = "+getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS));
		setRequestAttribute("NumerazioneManualeMisureProvvFS",
				getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS));

		// Viene impostato il parametro tipo_ricerca, in quanto è innescata una query
		// che precedentemente lavorava con la scelta "ufficio" o "distretto";
		// adesso mettiamo una terza scelta "tutto" per cercare in tutto il DB
		String TipoRicerca = null;
		if (!isRequestParameterNullObj("tipoRicerche")) {
			TipoRicerca = getRequestStringParameter("tipoRicerche");
		} else {
			TipoRicerca = "tutto";
		}

		setRequestAttribute("tipoRicerche", TipoRicerca);

//		String ufficio = this.getCodUfficioUtenteConnesso();
		String distretto = this.getCodDistrettoUtenteConnesso();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Impostazione di CountRisultati.
		BigDecimal CountRisultati;
		ISoggettoFascicolo lFascSogCtrl = SICOLookupRemote.getSoggettoFascicoloRemote();

		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lFascSogCtrl.ExCountSoggettoPerDistrettoProgFasc(lSogMod, distretto, 0);
		} else {
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		setRequestAttribute("CountRisultati", CountRisultati);

		Vector lSoggettoFascicoli = new Vector();
		try {
			lSoggettoFascicoli = lFascSogCtrl.ExRicercaSoggettoPerDistrettoProgFasc(lSogMod, distretto,
					Integer.parseInt(lPagina));
		} catch (Exception Ex) {
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, Ex.getMessage());
			// Prepara la "pagina" di destinAction
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi
					.setAction("siap.siep.misurasicurezza.action.ActLoadIscrizioneProcApplicazioneMisuraProvvisoria");
			lRedirigi
					.setParameter(
							ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS,
							getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

//		SoggettoModel lSogModel = ((FascicoloSiepModel) lSoggettoFascicoli.get(0)).getSoggetto();

		setRequestAttribute("soggettifascicoli", lSoggettoFascicoli);

		return ICostantiMisuraSicurezza.PG_RICERCA_SOGG_MIS_SIC_PROVVISORIA; // restituisce la jsp di VIEW
	}

}