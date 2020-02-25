package siap.siep.nuovaistanza.action;

/**
* <p>Title: ActRicercaNuovaIstanza</p>
* <p>Description: Classe Action per la ricerca di NuovaIstanza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile s.r.l.</p>
* @version 5.0
*/

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActElencoNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione di Ricerca. Recupera i dati dalla form ed effettua la ricerca.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		NuovaIstanzaModel lNuoMod = new NuovaIstanzaModel();
		lNuoMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());

		String lReturnPage = null;

		String lPagina = "1";
		String strCountRisultati;

		// ============================================================================
		// Recupero la pagina da visualizzare se prevengo dalla finestra dei risultati
		// ============================================================================
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// =========================================================
		// Istanzio il controller ed effettuo la ricerca paginata
		// =========================================================

		INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
		Vector lVect = lCtrl.ExRicercaNuovaIstanzaPaged(lNuoMod, Integer.parseInt(lPagina));

		if (lVect.size() == 0) {
			this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessuna Istanza presente");
			return IWebConstants.PG_MESSAGE;
		}

		// ======================================================================
		// Recupero il numero di record totali della ricerca utilizzato per
		// calcolare il numero totale di pagine necessarie a visualizzare i dati
		// ======================================================================
		if (isRequestParameterNullObj("CountRisultati"))
			strCountRisultati = lCtrl.ExGetCountNuovaIstanza(lNuoMod).toString();
		else
			strCountRisultati = getRequestStringParameter("CountRisultati");

		BigDecimal CountRisultati = new BigDecimal(strCountRisultati);

		if (Integer.parseInt(strCountRisultati) == 1) {
			lNuoMod = new NuovaIstanzaModel((NuovaIstanzaModel) lVect.firstElement());

			/** Nel caso di singola istanza lancia il dettaglio **/

			String lPage = "";
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza";
			lPage += "&" + "IdEvento" + "=" + lNuoMod.getEveIdEvento().toString();
			return lPage;
		} else {
			// 06/06/2011 Competenza.
			String lCompetenza = "SI";
			try {
				isFascicoloSiepDiCompetenza();
			} catch (Exception ex) {
				lCompetenza = "NO";
			}
			setRequestAttribute("competenza", lCompetenza);

			Vector<String> istanze_validate = new Vector();
			Iterator itx = lVect.iterator();
			while (itx.hasNext()) {
				NuovaIstanzaModel lIstanza = (NuovaIstanzaModel) itx.next();
				String valflag = lCtrl.ExRicercaFlagValNuovaIstanzaPaged(lIstanza.getEveIdEvento(),
						Integer.parseInt(lPagina));
				istanze_validate.add(valflag);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("istanze_validate = " + istanze_validate.toString());

			setRequestAttribute("istanze_validate", istanze_validate);
			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			setRequestAttribute("nuovaistanza", lVect);

			lReturnPage = PG_ELENCONUOVAISTANZA;
		}

		return lReturnPage;

	}
}