package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichPMTitoloCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il caricamento del dettaglio della richiesta Revoca Lib. Anticipata
 * 
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActDettaglioRichiestaSORVRevocaLA extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		// Lettura della Richiesta
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		RichiestePmInCumuloModel lRicMod = (RichiestePmInCumuloModel) lCtrlRich
				.ExRicercaRichiestePmInCumuloById(aIdRich);

		setRequestAttribute("RichiestaSORV", lRicMod);

		if (lRicMod.getDecisioneGeSorvCum() != null
				&& lRicMod.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null)
			setRequestAttribute("DecisioneSorv", lRicMod.getDecisioneGeSorvCum());

		// Ricerca dei Titoli collegati alla Richiesta (tramite tabella di Relazione RICHPM_TITOLO_CUM)
		Vector<RichPMTitoloCumModel> VecRicPM = new Vector<RichPMTitoloCumModel>();
		VecRicPM = lCtrlRich.ExRicercaRichPMTitoliCum(aIdRich);

		// Lettura dei Titoli collegati alla Richiesta e dei dati di Stato_Esec_Cum Aggregati.
		Vector<TitoloCumulatoModel> VecTitoli = new Vector<TitoloCumulatoModel>();
		VecTitoli = lCtrlRich.ExRicercaTitoliDiLibAntPerRichiesta(VecRicPM);

		setRequestAttribute("TitoliRichiesta", VecTitoli);

		// ==========================================================================
		//
		// ==========================================================================
		String[] aFiltroUffici = { "CAP", "CAS", "CASAP", "GIP", "DIB", "TRIBSD" };
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), true);
		lOption.setValueBlankItem("-");
		lOption.setFilter(aFiltroUffici);
		setRequestAttribute("UfficioEmittente", "" + lOption);

		return PG_DETT_RICH_SORV_REV_LA;
	}

}