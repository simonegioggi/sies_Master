package siap.siep.istruttoria.action;

import org.apache.log4j.Logger;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;

/**
 *
 * <p>
 * Title: ActLoadInserisciEstrattoSentenze
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
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciEstrattoSentenze extends ActionSiap implements ICostantiIstruttoria {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception {

		if (isSessionAttributeNullObj("fascicolo")) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
					+ "siap.siep.istruttoria.action.ActLoadInserisciEstrattoSentenze";

			return lPage;
		}

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		this.isFascicoloSiepDiCompetenza();

		this.isEventoNonValidato();

		// Se vengo da IstruttoriaCUMULO
		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)) {
			setRequestAttribute("IdIstruttoriaCumulo",
					getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("--XX-- ActLoadInserisciEstrattoSentenze - ID_ISTRU_CUM =
			//// "+getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
		}

		// Riempimento ComboBoX
		// 14/06/2010 Sostituzione Elenco Autorità Emittenti
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS());
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");

		setRequestAttribute("autorita", "" + lOption);

		return PG_LOAD_INSERISCI_ESTRATTO_SENTENZE;
	}

}

