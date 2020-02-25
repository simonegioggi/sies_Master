package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il caricamento del dettaglio della richiesta al G.E. di Revoca Pena Accessoria (Gestione
 * Cumulo)
 * 
 * @author
 *
 */
public class ActDettaglioRichiestaGERevocaPenaAcc extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicMod = (RichiestePmInCumuloModel) lCtrlRich
				.ExRicercaRichiestePmInCumuloById(aIdRich);

		setRequestAttribute("RichiestaGE", lRicMod);

		Vector<TitoloCumulatoModel> lVecTitoliPA = new Vector<TitoloCumulatoModel>();
		// Ricerca dei Titoli e delle Pene Accessoria collegate alla Richiesta (tramite tabelle di Relazione
		// RICHPM_TITOLO_CUM e RICHPM_PENACC_CUM)
		lVecTitoliPA = lCtrlRich.ExRicercaTitoli_e_PeneAccCumByRichiestaGE(aIdRich);

		setRequestAttribute("ListaTitoliPA", lVecTitoliPA);

		return PG_DETT_RICH_GE_REVOCA_PA;
	}

}