package siap.siep.modulocumulo.action;

import org.apache.log4j.Logger;
import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il caricamento del dettaglio della richiesta al G.E. di Revoca Sanzione Sostitutiva
 * (Gestione Cumulo)
 * 
 * @author
 *
 */
public class ActDettaglioRichiestaGERevocaSSCum extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicMod = (RichiestePmInCumuloModel) lCtrlRich
				.ExRicercaRichiestePmInCumuloById(aIdRich);

		setRequestAttribute("RichiestaGE", lRicMod);

		// Ricerca dei Titoli e Sanzioni Sostitutive collegati alla Richiesta (tramite tabella di Relazione
		// RICHPM_TITOLO_CUM)
		Vector<TitoloCumulatoModel> VecTitoli = new Vector<TitoloCumulatoModel>();
		VecTitoli = lCtrlRich.ExRicercaTitoli_e_SSCumByRichiestaGE(aIdRich);

		// siesLogger.debug("--XX-- i Titoli collegati alla Richiesta Revoca Sanzione Sost sono:
		// "+VecTitoli.size());
		setRequestAttribute("TitoliRichiesta", VecTitoli);

		return PG_DETT_RICH_GE_REV_SAN_SOST;
	}

}
