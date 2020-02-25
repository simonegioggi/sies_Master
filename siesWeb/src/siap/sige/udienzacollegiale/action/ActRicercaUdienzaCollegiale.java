package siap.sige.udienzacollegiale.action;

/**
* <p>Title: ActRicercaUdienzaCollegiale</p>
* <p>Description: Classe Action per la ricerca di UdienzaCollegialeSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

public class ActRicercaUdienzaCollegiale extends ActionSige
		implements ICostantiUdienzaCollegiale, ICostantiUdienzaSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione di Ricerca. Recupera i dati dalla form ed effettua la ricerca.
	 *
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		CollegioModel collMod = new CollegioModel();
		CollegioMagistratoModel magModel = new CollegioMagistratoModel();

		Date[] lRangeDateUdienza = getRequestDateParameters(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA, ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA);

		lUdiMod.setDateUdienze(lRangeDateUdienza);

		// INTERVENTO PER 11.2.1 (ELIMINO FILTRO di ricerca PER ID COLLEGIO)
		// lUdiMod.setColIdCollegio(getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_ID_COLLEGIO));

		// INTERVENTO PER 11.2.1 (INTRODOTTA RICERCA PER MAGISTRATO COLLEGIO)
		String codMagistrato = null;
		lUdiMod.setCollegio(collMod);
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_GIUDICE)
				&& !getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_GIUDICE).equals("-")) {

			siesLogger.debug("CODICE MAGISTRATO>>"
					+ getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_GIUDICE));
			codMagistrato = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_GIUDICE);
			ArrayList lArrayList = new ArrayList();
			collMod.setMagCodMagistrato(codMagistrato);
			magModel.setMagCodMagistrato(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_GIUDICE));
			lArrayList.add(magModel);
			lUdiMod.getCollegio().setCollegioMagistrati(
					(CollegioMagistratoModel[]) lArrayList.toArray(new CollegioMagistratoModel[0]));
		}

		if (!isRequestParameterNullObj(ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE)
				&& !getRequestStringParameter(ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE).equals("-")) {
			lUdiMod.getCollegio().setSezIdSezione(
					new BigDecimal(getRequestStringParameter(ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE)));
		}

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

			// INTERVENTO PER 11.2.1 COMMENTO QUESTO METODO DI RICERCA A FAVAORE DI
			// ExRicercaUdienzaCollegialeSige SOTTO RIPORTATO
			// Vector lVect = lCtrl.ExRicercaUdienzaSige(lUdiMod);

			Vector lVect = lCtrl.ExRicercaUdienzaCollegialeSige(lUdiMod, getCodUfficioUtenteConnesso(), "FA"); // FA:
																												// PROVENIENZA
																												// DA
																												// FUNZIONI
																												// AMMINISTRATIVE

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("udienzasige", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCAUDIENZACOLLEGIALE;
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
				setRequestAttribute("udienzasige", lUdiMod);
				setFunctionsAvailableToRequest(
						"siap.sige.udienzacollegiale.action.ActLoadDettaglioUdienzaCollegiale");

				lReturnPage = PG_LOAD_DETTAGLIOUDIENZACOLLEGIALE;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
				setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("udienzesige", lVect);

				lReturnPage = PG_RICERCAUDIENZACOLLEGIALE;
			}
		} // fine if tipo_ricerca

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lReturnPage;
	}

}