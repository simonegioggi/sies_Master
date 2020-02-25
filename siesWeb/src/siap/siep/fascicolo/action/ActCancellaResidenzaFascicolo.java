package siap.siep.fascicolo.action;

/**
* <p>Title: ActCancellaResidenza</p>
* <p>Description: Classe Action per la cancellazione della Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.residenza.action.ICostantiResidenza;
import siap.sico.residenza.controller.ResidenzaController;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
//import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

@SuppressWarnings("rawtypes")
public class ActCancellaResidenzaFascicolo extends ActionSiap implements ICostantiResidenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("<------------ ActCancellaResidenza ------------>");

		ResidenzaModel lResMod = new ResidenzaModel();
		lResMod.setIdResidenza(getRequestBigDecimalParameter(CAMPO_ID_RESIDENZA));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lResMod.getIdResidenza ======  " + lResMod.getIdResidenza());

		ResidenzaController lCtrl = new ResidenzaController();
		Vector lVect = lCtrl.ExRicercaResidenza(lResMod);
		lResMod = (ResidenzaModel) lVect.get(0);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lResMod =====================  " + lResMod);

		lCtrl.ExCancellaResidenza(lResMod);

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO, lResMod.getSogIdSoggetto().toString());
		setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Cancellazione effettuata");
		if (lResMod.getCodTipoResidenza().equals("R")) {
			lRedirigi.setAction("siap.sico.residenza.action.ActRicercaResidenza");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lancio ActRicercaResidenza ");
		} else {
			lRedirigi.setAction("siap.sico.residenza.action.ActRicercaDomicilio");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lancio ActRicercaDomicilio ");
		}

		ResidenzaModel lResModNew = new ResidenzaModel();
		lResModNew.setSogIdSoggetto(lResMod.getSogIdSoggetto());
		lResModNew.setCodTipoResidenza(lResMod.getCodTipoResidenza());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lResModNew.getSogIdSoggetto =  " + lResMod.getSogIdSoggetto());

		// lVect = lCtrl.ExRicercaResidenza(lResModNew);
		try {
			lVect = lCtrl.ExRicercaResidenza(lResModNew);
			setRequestAttribute("residenze", lVect);
		} catch (Exception e) {
			// Nessun Elemento Trovato
			lRedirigi.setAction("siap.sico.soggetto.action.ActLoadDettaglioSoggetto");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lancio ActLoadDettaglioSoggetto ");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("fine ActCancellaResidenza **********");
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		return ISIAPCostantiWeb.PG_MESSAGE;
	}

}