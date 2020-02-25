package siap.siep.fascicolo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.residenza.action.ICostantiResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActDeassociaResidenzaFascicolo
 * </p>
 * <p>
 * Description: Classe Action per la deassociazione di Residenza da Fascicolo Siep
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActDeassociaResidenzaFascicolo extends ActionSiap implements ICostantiResidenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento della Residenza
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();
		// BigDecimal lIdSoggetto = getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO);
		BigDecimal lIdResidenza = getRequestBigDecimalParameter(CAMPO_ID_RESIDENZA);

		ResidenzaModel lResMod = new ResidenzaModel();
		lResMod.setIdResidenza(lIdResidenza);

		ResidenzaFascicoloSiepModel lResFasc = new ResidenzaFascicoloSiepModel();
		lResFasc.setDataInizioValidita(DateUtils.getSysDate());
		lResFasc.setFasSieIdFascicoloSiep(lIdFascicolo);
		lResFasc.setResIdResidenza(lIdResidenza);

		ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();
		lResAss.setResidenza(lResMod);
		lResAss.setResidenzaFascicoloSiep(lResFasc);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("data = " + lResFasc.getDataInizioValidita());

		// --- FASCICOLO ---
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lResAss = lCtrl.ExDeassociaResidenzaFascicoloSiep(lResAss);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
				+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
	}

}