package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiapMinor;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
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
public class ActRicercaSoggettoFascicoloSiep extends ActionSiapMinor implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		SoggettoModel lSogMod = new SoggettoModel();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// MEV 15 - Revisione SIGE - Codice CUI
		String codAFIS = null;
		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_AFIS)) {
			codAFIS = getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_AFIS);
		}
		lSogMod.setCodAfis(codAFIS);

		// Codice della funzione che ha richiamato la pagina di ricerca,
		// quando la maschera viene richiamata da SIGE - Funzione: Ricerca Procedimento SIEP per Soggetto
		// nella tabella risultato della ricerca viene visulaizzata la colonna con la
		// "Data di Nascita" del soggetto ed eliminata la colonna "Stato"
		String codFunzione = null;
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.COD_FUNZIONE_90020012)) {
			codFunzione = getRequestStringParameter(ICostantiFascicoloSius.COD_FUNZIONE_90020012);
		}

		// parse della request
		if (getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA).length() > 1) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescrFlagVal(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
			lSogMod.setCodComuneNascita(lComMod.getCodComune());
		}

		// riempie il model
		lSogMod.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).toUpperCase());
		lSogMod.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME).toUpperCase());

		if (getRequestStringParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA).length() > 2)
			lSogMod.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
					ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA, ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));

		if (!getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA).equals("-"))
			lSogMod.setCodStatoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA));

		lSogMod.setPaternita(getRequestStringParameter(ICostantiSoggetto.CAMPO_PATERNITA));
		lSogMod.setCognomeMadre(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME_MADRE));
		lSogMod.setNomeMadre(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME_MADRE));

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lSogMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		// chiama il controller per SIEP

		IFascicoloSiep lFascSogCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		Vector lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascicoloSiepBySoggettoPaged(lSogMod,
				Integer.parseInt(lPagina), checkMinori());

		String lReturnPage = "";

		if (lFascicoliSoggetti.size() == 1) {
			gestioneRitorno();

			FascicoloSiepModel lFascicoloSoggettoSiep = new FascicoloSiepModel(
					(FascicoloSiepModel) lFascicoliSoggetti.firstElement());

			// Inserisce il model soggetto in sessione
			setSessionAttribute("fascicolo", lFascicoloSoggettoSiep);

			// Inserisce il model soggetto nella request
			setRequestAttribute("fascicolo", lFascicoloSoggettoSiep);

			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ lFascicoloSoggettoSiep.getIdFascicoloSiep();

		} else {
			setLinkRitorno();

			// Collection di decodifica del COD_STATO
			Collection lCol = DecodificheManager.getInstance().getStatoProcedimento();

			// STUB : Per il momento senza STATO !! Luigi 9-9-04
			setRequestAttribute("CodStato", lCol);
			// setRequestAttribute("Nostato", "NO");

			// Paginazione
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				// MEV_57: aggiunto parametro di passaggio
				CountRisultati = lFascSogCtrl.ExGetNumFascicoloSiepBySoggetto(lSogMod, checkMinori());
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
			setRequestAttribute("codFunzione", codFunzione);

			// setta la risposta nella request
			setRequestAttribute("fascicoli", lFascicoliSoggetti);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + ".processRequest: fine");

			lReturnPage = ICostantiFascicoloSius.PG_RICERCAFASCICOLO;
		}

		// restituisce la jsp di VIEW
		return lReturnPage;
	}

}