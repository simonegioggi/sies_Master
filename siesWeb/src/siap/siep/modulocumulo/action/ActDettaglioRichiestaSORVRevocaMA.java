package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il caricamento del dettaglio della richiesta alla SORV di Revoca M.A. (Gestione Cumulo)
 * 
 * @author Intersistemi Italia S.p.A.
 */
public class ActDettaglioRichiestaSORVRevocaMA extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicMod = (RichiestePmInCumuloModel) lCtrlRich
				.ExRicercaRichiestePmInCumuloById(aIdRich);

		setRequestAttribute("RichiestaSORV", lRicMod);

		// Ricerca del Titolo e del provvedimento di M.A. collegati alla Richiesta (tramite tabella di
		// Relazione RICHPM_TITOLO_CUM, RICHPM_STATO_ESEC_CUM)
		TitoloCumulatoModel lTitoloMod = null;
		lTitoloMod = (TitoloCumulatoModel) lCtrlRich
				.ExRicercaTitolo_e_StatoEsecTitoloCumByRichiestaGE(aIdRich);

		setRequestAttribute("TitoloRichiesta", lTitoloMod);

		return PG_DETT_RICH_SORV_REV_MA;
	}

}