package siap.sige.udienzamonocratica.action;

/**
* <p>Title: ActRicercaUdienzaMonocraticaSige</p>
* <p>Description: Classe Action per la ricerca di UdienzaMonocraticaSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

public class ActRicercaUdienzaMonocraticaSige extends ActionSige
		implements ICostantiUdienzaMonocraticaSige, ICostantiUdienzaSige {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione di Ricerca. Recupera i dati dalla form ed effettua la ricerca.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");
		// ==============================================================
		// Recupero dalla form i campi di ricerca
		// n.b. cancellare o commentare i campi non presenti nella form
		// ==============================================================

		super.setLinkRitorno();

		UdienzaSigeModel lUdiMod = new UdienzaSigeModel();

		Date[] lRangeDateUdienza = getRequestDateParameters(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA, ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA);

		lUdiMod.setDateUdienze(lRangeDateUdienza);

		lUdiMod.setCodGiudice(getRequestStringParameter(CAMPO_COD_GIUDICE));

		lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		// =================================
		// Selezionare il tipo di ricerca
		// =================================
		String tipo_ricerca = "semplice";
		// String tipo_ricerca = "paginata";

		String lReturnPage = null;

		if (tipo_ricerca.equals("semplice")) {
			// =========================================================
			// Istanzio il controller ed effettuo la ricerca semplice
			// =========================================================
			IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
      //Vector lVect = lCtrl.ExRicercaUdienzaSige(lUdiMod);
      Vector lVect = lCtrl.ExRicercaUdienzaSigePerFunzioniSupporto(lUdiMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("udienzamonocraticasige", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCAUDIENZAMONOCRATICASIGE;
		} else {
			String lPagina = "1";
			String CountRisultati;

			// ============================================================================
			// Recupero la pagina da visualizzare se prevengo dalla finestra dei risultati
			// ============================================================================
			if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
				lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

			// =========================================================
			// Istanzio il controller ed effettuo la ricerca paginata
			// =========================================================
			IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
			Vector lVect = lCtrl.ExRicercaUdienzaSigePaged(lUdiMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountUdienzaSige(lUdiMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lUdiMod = new UdienzaSigeModel((UdienzaSigeModel) lVect.firstElement());
				setRequestAttribute("udienzamonocraticasige", lUdiMod);
				setFunctionsAvailableToRequest(
						"siap.sige.udienzamonocratica.action.ActLoadDettaglioUdienzaMonocraticaSige");

				lReturnPage = PG_LOAD_DETTAGLIOUDIENZAMONOCRATICASIGE;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
				setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("udienzamonocraticasige", lVect);

				lReturnPage = PG_RICERCAUDIENZAMONOCRATICASIGE;
			}
		} // fine if tipo_ricerca

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lReturnPage;
	}
}