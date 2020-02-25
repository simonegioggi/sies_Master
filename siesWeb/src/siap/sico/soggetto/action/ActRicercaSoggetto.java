package siap.sico.soggetto.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.controller.ISoggettoFascicolo;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiapMinor;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: ActRicercaSoggetto
 * </p>
 * <p>
 * Description: Ricerca del soggetto
 * </p>
 */
public class ActRicercaSoggetto extends ActionSiapMinor implements ICostantiSoggetto {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		SoggettoModel lSogMod = new SoggettoModel();

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
				lComMod = new ComuneModel(getDatiComuneByDescrOmonimiaFlagVal(
						getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
			}

			lSogMod.setCodComuneNascita(lComMod.getCodComune());
		}

		// riempie il model
		lSogMod.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
		lSogMod.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());

		if (!this.isRequestParameterNullObj(CAMPO_ANNO_DATA_NASCITA)
				&& getRequestStringParameter(CAMPO_ANNO_DATA_NASCITA).length() > 2)
			lSogMod.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
					CAMPO_GIORNO_DATA_NASCITA));

		if (!this.isRequestParameterNullObj(CAMPO_COD_STATO_NASCITA)
				&& !getRequestStringParameter(CAMPO_COD_STATO_NASCITA).equals("-"))
			lSogMod.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));

		if (!this.isRequestParameterNullObj(CAMPO_DESC_COMUNE_NASCITA_ESTERO)
				&& !getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO).equals("-"))
			lSogMod.setDescComuneNascitaEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO));

		lSogMod.setPaternita(getRequestStringParameter(CAMPO_PATERNITA));
		lSogMod.setCognomeMadre(getRequestStringParameter(CAMPO_COGNOME_MADRE));
		lSogMod.setNomeMadre(getRequestStringParameter(CAMPO_NOME_MADRE));

		lSogMod.setAttoNascita(getRequestStringParameter(CAMPO_ATTO_NASCITA));
		lSogMod.setCodAfis(getRequestStringParameter(CAMPO_COD_AFIS));

		// * Vengono cercati solo i soggetti appartenenti all'ufficio
		// dell'utente *
		lSogMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// chiama il contreller
		// SoggettoController sogctrl = new SoggettoController();

		ISoggettoFascicolo lFascSogCtrl = SICOLookupRemote.getSoggettoFascicoloRemote();

		// Prima conto il numero totale dei soggetti, usando il vector
		// lSoggettiFasc2,
		// e nella ricerca passo il parametro lPagina = 0

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lFascSogCtrl.ExCountSoggettoPerDistrettoProgFasc(lSogMod,
					getCodDistrettoUtenteConnesso(), 0, checkMinori());
		} else {
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		// usando il vector lSoggettiFasc, faccic la ricerca vera e propia
		// e nella ricerca passo il parametro nteger.parseInt(lPagina) per la
		// paginazione 20 per volta
		Vector lSoggettiFasc = new Vector();

		// lSoggettiFasc =
		// lSogCtrl.ExRicercaSoggettoPerDistretto(lSogMod,getCodDistrettoUtenteConnesso(),Integer.parseInt(lPagina));
		lSoggettiFasc = lFascSogCtrl.ExRicercaSoggettoPerDistrettoProgFasc(lSogMod,
				getCodDistrettoUtenteConnesso(), Integer.parseInt(lPagina), checkMinori());

		// Bottone di Ritorno
		setLinkRitorno();

		String lReturnPage = "";
		if (lSoggettiFasc.size() == 1) {
			// SoggettoModel lSogModel = new
			// SoggettoModel((SoggettoModel)lSoggetti.firstElement());
			SoggettoModel lSogModel = ((FascicoloSiepModel) lSoggettiFasc.get(0)).getSoggetto();

			// Inserisce il model soggetto in sessione
			setSessionAttribute("soggetto", lSogModel);

			// Inserisce il model soggetto nella request
			setRequestAttribute("soggetto", lSogModel);

			// verifica se l'utente è abilitato a modificare il soggetto
			String abilitaUtente = "NO";
			String soggUffIns = lSogModel.getCodUfficioInserimento();
			String codUfficioUtente = getCodUfficioUtenteConnesso();

			if (soggUffIns.equals(codUfficioUtente)) {
				abilitaUtente = "SI";
			} else {
				IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
				Vector lUffAccUtente = lUACon.ListaUfficiAccorpati(null, codUfficioUtente);
				Iterator itr = lUffAccUtente.iterator();
				while (itr.hasNext()) {
					UfficioAccorpatoModel lUAMod = (UfficioAccorpatoModel) itr.next();
					if (soggUffIns.equals(lUAMod.getCodUfficio())) {
						abilitaUtente = "SI";
					}
				}
			}

			// Inserisce il model soggetto nella request
			setRequestAttribute("modificabile", abilitaUtente);

			setFunctionsAvailableToRequest("siap.sico.soggetto.action.ActLoadDettaglioSoggetto");
			lReturnPage = PG_LOAD_DETTAGLIOSOGGETTO;
		} else {
			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
			setRequestAttribute("soggetti", lSoggettiFasc);
			lReturnPage = PG_RICERCASOGGETTO;

			// Non serve più perchè il bottone di ritornoin maniera standard !
			// Luigi
			// String lAzione = "siap.sico.soggetto.action.ActRicercaSoggetto";
			// setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,lAzione);

		}

		return lReturnPage; // restituisce la jsp di VIEW
	} // Chiude processRequest

} // Chiude Classe