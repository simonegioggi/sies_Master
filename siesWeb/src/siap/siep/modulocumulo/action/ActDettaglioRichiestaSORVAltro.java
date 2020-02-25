package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il Dettaglio della Richiesta al Magistrato di SORVEGLIANZA Tipo Richiesta = Altro (cod =
 * 014) - (Gestione Cumulo)
 * 
 * @author Intersistemi Italia S.p.A.
 */
public class ActDettaglioRichiestaSORVAltro extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicMod = (RichiestePmInCumuloModel) lCtrlRich
				.ExRicercaRichiestePmInCumuloById(aIdRich);
		setRequestAttribute("RichiestaSORV", lRicMod);

		// Ricerca del Titolo collegato alla Richiesta (tramite tabelle di Relazione RICHPM_TITOLO_CUM )
		TitoloCumulatoModel lTitoloMod = lCtrlRich.ExRicercaTitolo_ByRichiestaGE(aIdRich);
		setRequestAttribute("Titolo", lTitoloMod);

		return PG_DETT_RICH_SORV_ALTRO;
	}

}