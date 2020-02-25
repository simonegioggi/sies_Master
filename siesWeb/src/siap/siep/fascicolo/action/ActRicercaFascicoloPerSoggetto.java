package siap.siep.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoAliasFascicoloModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiapMinor;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaFascicoloPerSoggetto extends ActionSiapMinor implements ICostantiSoggetto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private SoggettoModel getSoggetto(boolean fromDetail) throws F3BException {

		SoggettoModel lSogMod = new SoggettoModel();

		if (fromDetail) {

			lSogMod.setIdSoggetto(getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO));

			// MEV 10 Step 1 parag. 6
			// L'intervento prevede che l'utente possa effettuare la ricerca
			// per soggetto utilizzando come criteri di selezione i dati del
			// soggetto presenti nella interfaccia e non per Id soggetto
			ISoggetto lCtrlSoggetto = SICOLookupRemote.getSoggettoRemote();
			lSogMod = lCtrlSoggetto.ExRicercaSoggettoByKey(lSogMod.getIdSoggetto());
			lSogMod.setIdSoggetto(new BigDecimal(0));

		} else {

			// parse della request
			if (!this.isRequestParameterNullObj(CAMPO_COD_COMUNE_NASCITA)
					&& getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA).length() > 1) {
				// Recupero dati del Comune di nascita
				ComuneModel lComMod;
				if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
						&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
					// se presente dal codice comune (e descrizione)
					lComMod = new ComuneModel(getDatiComuneByCodDescrFlagVal(
							getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
							getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
				} else {
					// altrimenti dalla sola descrizione (rischio omonimi)
					lComMod = new ComuneModel(
							getDatiComuneByDescrOmonimiaFlagVal(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
				}

				lSogMod.setCodComuneNascita(lComMod.getCodComune());
			}

			// riempie il model
			lSogMod.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
			lSogMod.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());

			if (getRequestStringParameter(CAMPO_ANNO_DATA_NASCITA).length() > 2) {
				lSogMod.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA,
						CAMPO_MESE_DATA_NASCITA, CAMPO_GIORNO_DATA_NASCITA));
			}

			if (!getRequestStringParameter(CAMPO_COD_STATO_NASCITA).equals("-")) {
				lSogMod.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
			}

			if (!this.isRequestParameterNullObj(CAMPO_DESC_COMUNE_NASCITA_ESTERO)
					&& !getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO).equals("-"))
				lSogMod.setDescComuneNascitaEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO));

			lSogMod.setPaternita(getRequestStringParameter(CAMPO_PATERNITA));
			lSogMod.setCognomeMadre(getRequestStringParameter(CAMPO_COGNOME_MADRE));
			lSogMod.setNomeMadre(getRequestStringParameter(CAMPO_NOME_MADRE));
			lSogMod.setCodAfis(getRequestStringParameter(CAMPO_COD_AFIS));

			lSogMod.setAttoNascita(getRequestStringParameter(CAMPO_ATTO_NASCITA));

			// AMBROSINO
			String StrClassiFascicolo = "";
			if (!isRequestParameterNullObj("tipoClasse")) {
				String[] lClassiFascicolo = this.getRequestStringParameters("tipoClasse");
				lSogMod.setClassiFascicolo(lClassiFascicolo);

				for (int i = 0; i < lClassiFascicolo.length; i++) {
					StrClassiFascicolo = StrClassiFascicolo + lClassiFascicolo[i] + "/";
				}

			}

			setRequestAttribute("StrClassiFascicolo", StrClassiFascicolo);
			// Per poter passare fino alla fine ho bisogno di "StrClassiFascicolo" in formato String semplice
			// Fine AMBROSINO
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("soggetto--->" + lSogMod);

		return lSogMod;
	}

	private String standardRequest(boolean fromDetail) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("\n\nREQUEST - Ricerca Soggetto = " + this.getCompleteRequestURL() + "\n\n");

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		SoggettoModel lSogMod = getSoggetto(fromDetail);

		String dalDettaglio = "N";
		if (fromDetail) {
			dalDettaglio = "S";
		}
		setRequestAttribute("fromDetail", dalDettaglio);

		// Controlla il tipo di ricerca se "ufficio" o "distretto"
		String TipoRicerca = null;
		if (!isRequestParameterNullObj("tipoRicerche")) {
			TipoRicerca = getRequestStringParameter("tipoRicerche");

			// Passa alla JSP il tipo ricerca "ufficio" o "distretto"
			setRequestAttribute("tipoRicerche", TipoRicerca);
		}

		// Verifica se la ricerca deve essere fatta anche per "ancheAlias"
		String strAncheAlias = null;
		if (!isRequestParameterNullObj("ancheAlias")) {
			strAncheAlias = getRequestStringParameter("ancheAlias");
		}

		// chiamata ai controller
		IFascicoloSiep lFascSogCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		// prende dalla sessione il codice dell'ufficio dell'utente connesso
		UfficioModel um = getUfficioUtenteConnesso();
		// String uffcio = this.getCodUfficioUtenteConnesso();
		String uffcio = um.getCodUfficio();
		String tipoUffcio = um.getCodTipoUfficio();
		String majorOffice = checkMinori();

		// prende dalla sessione il codice del Distretto dell'utente connesso
		String distretto = this.getCodDistrettoUtenteConnesso();

		String lReturnPage = "";
		BigDecimal CountRisultati = null;

		// Ricerca per alias/uffcio o alias/distretto
		if (strAncheAlias != null) {
			Vector lSoggettoAliasFascicolo = lFascSogCtrl.ExRicercaSoggettoAliasFascicoloPaged(lSogMod,
					uffcio, Integer.parseInt(lPagina), distretto, TipoRicerca, majorOffice);

			setRequestAttribute("fascicoliSogAlias", lSoggettoAliasFascicolo);

			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lFascSogCtrl.ExGetCountSoggettoAliasFascicolo(lSogMod, uffcio, distretto,
						TipoRicerca, majorOffice);
			} else {
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");
			}

			// Estrazione model Soggetto e relativo inserimento nella request.
			// Utile per la JSP SintesiSoggetto.jsp
			if (lSoggettoAliasFascicolo != null) {
				SoggettoModel lSoggetto = ((SoggettoAliasFascicoloModel) lSoggettoAliasFascicolo.get(0))
						.getSoggetto();
				setRequestAttribute("soggetto", lSoggetto);
				setSessionAttribute("soggetto", lSoggetto); // STUB 26/01/2004 Da rivedere
			}

			lReturnPage = ICostantiFascicoloSiep.PG_LOAD_RICERCAFASCICOLOSOGGETTI_SIEP_PER_SOGGETTO_PER_ALIAS;
			// FINE - Ricerca per alias/ufficio o alias/distretto

		} else {

			// Ricerca solo per ufficio o per distretto
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lFascSogCtrl.ExCountFascicoliBySuperSoggettoPaged(lSogMod, uffcio, 0,
						distretto, TipoRicerca, majorOffice, fromDetail, tipoUffcio);
			} else {
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");
			}

			// -- * -- Ambros SuperSoggetto 08/2009 - cambio la ricerca -- * --
			// Vector lFascicoliSoggetti =
			// lFascSogCtrl.ExRicercaFascicoliBySoggettoPaged(lSogMod,uffcio,Integer.parseInt(lPagina),distretto,TipoRicerca);
			Vector lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascicoliBySuperSoggettoPaged(lSogMod, uffcio,
					Integer.parseInt(lPagina), distretto, TipoRicerca, majorOffice, fromDetail, tipoUffcio);

			setRequestAttribute("fascicoli", lFascicoliSoggetti);

			// Estrazione model Soggetto e relativo inserimento nella request.
			// Utile per la JSP SintesiSoggetto.jsp
			if (lFascicoliSoggetti != null) {
				SoggettoModel lSoggetto = ((FascicoloSiepModel) lFascicoliSoggetti.get(0)).getSoggetto();
				setRequestAttribute("soggetto", lSoggetto);
				setSessionAttribute("soggetto", lSoggetto); // STUB 26/01/2004 Da rivedere
			}

			lReturnPage = ICostantiFascicoloSiep.PG_LOAD_RICERCAFASCICOLOSOGGETTI_SIEP_PER_SOGGETTO;
			// Fine - Ricerca solo per ufficio o per distretto

		}

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		String lAzione = "siap.siep.fascicolo.action.ActRicercaFascicoloPerSoggetto";
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

		return lReturnPage; // restituisce la jsp di VIEW
	}

	public String processRequest() throws F3BException {

		return standardRequest(!isRequestParameterNullObj("DalDettaglio"));
	}

}