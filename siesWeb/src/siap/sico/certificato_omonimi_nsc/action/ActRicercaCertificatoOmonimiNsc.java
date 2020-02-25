package siap.sico.certificato_omonimi_nsc.action;

/**
* <p>Title: ActRicercaCertificatoOmonimiNsc</p>
* <p>Description: Classe Action per la ricerca di CertificatoOmonimiNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import siap.sico.certificato_omonimi_nsc.controller.ICertificatoOmonimiNsc;
import siap.sico.certificato_omonimi_nsc.model.CertificatoOmonimiNscModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActRicercaCertificatoOmonimiNsc extends ActionSiap implements ICostantiCertificatoOmonimiNsc {

	/*****************************************************************************
	 * Azione di Ricerca. Recupera i dati dalla form ed effettua la ricerca.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// ==============================================================
		// Recupero dalla form i campi di ricerca
		// n.b. cancellare o commentare i campi non presenti nella form
		// ==============================================================
		CertificatoOmonimiNscModel lCerMod = new CertificatoOmonimiNscModel();

		lCerMod.setIdCertificatoOmonimi(getRequestBigDecimalParameter(CAMPO_ID_CERTIFICATO_OMONIMI));
		lCerMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));

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
			ICertificatoOmonimiNsc lCtrl = SICOLookupRemote.getCertificatoOmonimiNscRemote();
			Vector lVect = lCtrl.ExRicercaCertificatoOmonimiNsc(lCerMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("certificatoomoniminsc", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCACERTIFICATOOMONIMINSC;
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
			ICertificatoOmonimiNsc lCtrl = SICOLookupRemote.getCertificatoOmonimiNscRemote();
			Vector lVect = lCtrl.ExRicercaCertificatoOmonimiNscPaged(lCerMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountCertificatoOmonimiNsc(lCerMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lCerMod = new CertificatoOmonimiNscModel((CertificatoOmonimiNscModel) lVect.firstElement());
				setRequestAttribute("certificatoomoniminsc", lCerMod);
				setFunctionsAvailableToRequest(
						"siap.sico.certificato_omonimi_nsc.action.ActLoadDettaglioCertificatoOmonimiNsc");

				lReturnPage = PG_LOAD_DETTAGLIOCERTIFICATOOMONIMINSC;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
				setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("certificatoomoniminsc", lVect);

				lReturnPage = PG_RICERCACERTIFICATOOMONIMINSC;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}