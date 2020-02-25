package siap.sius.fascicolo.action;

/**
* <p>Title: ActListaFascicoliOrigineSoggCont</p>
* <p>Description: Classe Action per la visualizzazione ricerca della lista
 * di Fascicoli SIUS per soggetto e per contenuto o per il dettaglio sintetico
 * di un fascicolo origine.</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActListaFascicoliOrigineSoggCont extends ActionSius implements ICostantiFascicoloSius {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		String lRetPage = PG_LISTA_FASCICOLI_ORIGINE;

		if (isRequestParameterNullObj(CAMPO_ID_FASCICOLO_SIUS_ORIGINE)) {
			if (isRequestParameterNullObj("formname"))
				throw (new F3BException(F3BException.USER_MESSAGE, "Nome form assente"));
			if (isRequestParameterNullObj("fieldname1") || isRequestParameterNullObj("fieldname2")
					|| isRequestParameterNullObj("fieldname3"))
				throw (new F3BException(F3BException.USER_MESSAGE, "Nome campo assente"));
			if (isRequestParameterNullObj(CAMPO_COD_OGGETTO))
				throw (new F3BException(F3BException.USER_MESSAGE, "Codice Oggetto assente"));
			if (isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO))
				throw (new F3BException(F3BException.USER_MESSAGE, "ID Soggetto assente"));

			// Vengono passati alla jsp di visualizzazione il nome della form
			// chiamante e del campo da valorizzare
			setRequestAttribute("formname", getRequestStringParameter("formname"));
			setRequestAttribute("fieldname1", getRequestStringParameter("fieldname1"));
			setRequestAttribute("fieldname2", getRequestStringParameter("fieldname2"));
			setRequestAttribute("fieldname3", getRequestStringParameter("fieldname3"));

			BigDecimal lIdSoggetto = getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO);
			String lCodOggetto = getRequestStringParameter(CAMPO_COD_OGGETTO);
			// Ricerca Fascicoli per Soggetto e per Oggetto
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			Vector lFascicoliOrigine = lFasCtrl.ExRicercaFascicoliByIdSoggettoCodOggetto(lIdSoggetto,
					lCodOggetto);
			setRequestAttribute("fascicoli", lFascicoliOrigine);

		} else
			lRetPage = dettaglioFascicoloOrigine();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRetPage; // restituisce la jsp di VIEW
	}

	// Dettaglio Fascicolo Origine
	private String dettaglioFascicoloOrigine() throws Exception {
		BigDecimal lIdFascicoloOrigine = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIUS_ORIGINE);

		FascicoloGPModel lFasGPModOrigine = new FascicoloGPModel();
		lFasGPModOrigine.getFascicoloSiusModel().setIdFascicoloSius(lIdFascicoloOrigine);

		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lFasGPModOrigine = lCtrl.ExRicercaFascicoloByKey(lIdFascicoloOrigine);
		setRequestAttribute("fascicolo_origine", lFasGPModOrigine);

		return PG_SINTESIPROCEDIMENTOORIGINESIUS;
	}

}