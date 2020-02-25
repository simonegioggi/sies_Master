package siap.siep.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.residenza.controller.ResidenzaController;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciAvvocato
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Avvocato
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
public class ActLoadInserisciResidenzaFascicolo extends ActionSiap implements ICostantiFascicoloSiep {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		ResidenzaAssociataModel lResAss = lCtrl.ExRicercaResidenzaFascicoloSiepCorrente(lIdFascicolo);
		setRequestAttribute("residenzaassociata", lResAss);
		setRequestAttribute("modalita", "I");

		// paolo
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("<------------ ActLoadInserisciResidenzaFascicolo1 ------------>");
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug("lResAss = " +lResAss.getResidenza().getIdResidenza());
		ResidenzaController lResCtrl = new ResidenzaController();
		ResidenzaModel lResMod = new ResidenzaModel();
		lResMod.setSogIdSoggetto(((SoggettoModel) getSessionAttribute("soggetto")).getIdSoggetto());
		lResMod.setCodTipoResidenza("R");
		try {
			Vector lVect = lResCtrl.ExRicercaResidenza(lResMod);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" lVect " + lVect.size());
			setRequestAttribute("residenze", lVect);
		} catch (Exception e) {
			// Nessun Elemento Trovato
		}
		// fine paolo

		// Per default ITALIA (039)
		String lCodStato = "039";
		if (lResAss != null && lResAss.getResidenza() != null)
			lCodStato = lResAss.getResidenza().getCodStato();
		Option lOption = new Option(DecodificheManager.getInstance().getNazioni(), lCodStato);
		setRequestAttribute("nazioni", "" + lOption);

		return PG_LOAD_INSERISCRESIDENZAFASCICOLO; // restituisce la jsp di VIEW
	}
}