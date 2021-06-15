package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;
import java.util.Vector;

//import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiapMinor;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaSoggettoIstanza
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 5.0
 */

public class ActRicercaSoggettoIstanza extends ActionSiapMinor implements ICostantiSoggetto {
	@SuppressWarnings("rawtypes")
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
				// 20210524	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag validità.
				//lComMod = new ComuneModel(getDatiComuneByDescrOmonimiaFlagVal(
				lComMod = new ComuneModel(getDatiComuneByDescrOmonimia(
						getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
			}

			lSogMod.setCodComuneNascita(lComMod.getCodComune());
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

		// Controlla il tipo di ricerca se "ufficio" o "distretto"
		String TipoRicerca = null;
		if (!isRequestParameterNullObj("tipoRicerche")) {
			TipoRicerca = getRequestStringParameter("tipoRicerche");

			// Passa alla JSP il tipo ricerca "ufficio" o "distretto"
			setRequestAttribute("tipoRicerche", TipoRicerca);
		}

		// prende dalla sessione il codice dell'ufficio dell'utente connesso
		String ufficio = this.getCodUfficioUtenteConnesso();

		// prende dalla sessione il codice del Distretto dell'utente connesso
		String distretto = this.getCodDistrettoUtenteConnesso();

		String majorOffice = checkMinori();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Impostazione di CountRisultati.
		BigDecimal CountRisultati;
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		IFascicoloSiep lFascSogCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		if (isRequestParameterNullObj("CountRisultati"))
			// paolo cherubini 09/02/2011
			// CountRisultati = lSogCtrl.ExGetCountSoggettiPerProcedimenti(lSogMod, ufficio, distretto,
			// TipoRicerca );
			CountRisultati = lFascSogCtrl.ExCountFascicoliBySuperSoggettoPaged(lSogMod, ufficio, 0, distretto,
					TipoRicerca);
		else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);

		Vector lSoggettoFascicoli = new Vector();
		try {
			lSoggettoFascicoli = lSogCtrl.ExRicercaSoggettoConFascicoliPaged(lSogMod, ufficio, distretto,
					TipoRicerca, Integer.parseInt(lPagina), majorOffice);
		} catch (Exception Ex) {
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, Ex.getMessage());

			// Prepara la "pagina" di destinAction
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.nuovaistanza.action.ActLoadInserisciIstanzaPerSoggetto");

			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("soggettifascicoli", lSoggettoFascicoli);

		return ICostantiNuovaIstanza.PG_RICERCA_SOGGETTO_ISTANZA; // restituisce la jsp di VIEW
	}
}