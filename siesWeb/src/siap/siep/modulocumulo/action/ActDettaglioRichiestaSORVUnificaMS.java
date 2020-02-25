package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.RichPMMisSicCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il caricamento del dettaglio della richiesta di Unificazione Misure di Sicurezza
 * 
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActDettaglioRichiestaSORVUnificaMS extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

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

		// Lettura delle relazioni RICHPM_MISSICUR_CUM)
		Vector<RichPMMisSicCumModel> VecRicPM = new Vector<RichPMMisSicCumModel>();
		VecRicPM = lCtrlRich.ExRicercaRichPMMisSicCum(aIdRich);
		siesLogger.info(">>>>>>>>>> aIdRich = " + aIdRich);

		// Lettura delle Misure di Sicurezza coinvolte.
		Vector<MisuraSicurezzaCumuloModel> VecMisSic = new Vector<MisuraSicurezzaCumuloModel>();
		IMisuraSicurezzaCumulo lCtrMSC = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
		for (int i = 0; i < VecRicPM.size(); i++) {
			RichPMMisSicCumModel lRicMSMod = (RichPMMisSicCumModel) VecRicPM.elementAt(i);
			MisuraSicurezzaCumuloModel lMisSicMod = lCtrMSC
					.ExRicercaMisuraSicurezzaCumuloById(lRicMSMod.getMisIdMisSicCumulo());
			VecMisSic.add(lMisSicMod);
		}

		// Lettura dei Titoli collegati alle Misure di Sicurezza.
		ITitoloCumulato lCtrlTitCum = SIEPLookupRemote.getTitoloCumulatoRemote();
		ArrayList<BigDecimal> lIdTitoli = new ArrayList<BigDecimal>();
		Vector<TitoloCumulatoModel> VecTitoli = new Vector<TitoloCumulatoModel>();
		for (int j = 0; j < VecMisSic.size(); j++) {
			MisuraSicurezzaCumuloModel lMisSicMod = (MisuraSicurezzaCumuloModel) VecMisSic.elementAt(j);
			if (lIdTitoli.contains(lMisSicMod.getTitIdTitoloCumulato())) {
			} else {
				TitoloCumulatoModel lTitCumMod = lCtrlTitCum
						.ExRicercaTitoloCumulatoById(lMisSicMod.getTitIdTitoloCumulato());
				VecTitoli.add(lTitCumMod);
				lIdTitoli.add(lTitCumMod.getIdTitoloCumulato());
			}
		}
		siesLogger.info(">>>>>>>>>> SIZE Vector Titoli della richiesta = " + VecTitoli.size());

		setRequestAttribute("MisureSicurezza", VecMisSic);
		setRequestAttribute("TitoliRichiesta", VecTitoli);

		// ==========================================================================
		//
		// ==========================================================================
		String[] aFiltroUffici = { "CAP", "CAS", "CASAP", "GIP", "DIB", "TRIBSD" };
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), true);
		lOption.setValueBlankItem("-");
		lOption.setFilter(aFiltroUffici);
		setRequestAttribute("UfficioEmittente", "" + lOption);

		return PG_DETT_RICH_SORV_UNIFICA_MS;
	}

}