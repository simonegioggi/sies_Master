package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichPMTitoloCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il caricamento del dettaglio della richiesta al G.E. di Revoca Pena principale (per
 * Indulto, Incostituzionalità, Illecito AMM)
 * 
 * @author
 *
 */
public class ActDettaglioRichiestaGERevocaPenaPrincCum extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		// cerco la Richiesta
		BigDecimal aIdRich = new BigDecimal(getRequestStringParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
		IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicMod = (RichiestePmInCumuloModel) lCtrlRich
				.ExRicercaRichiestePmInCumuloById(aIdRich);

		setRequestAttribute("RichiestaGE", lRicMod);

		// Ricerca dei Titoli collegati alla Richiesta (tramite tabella di Relazione RICHPM_TITOLO_CUM)
		Vector<RichPMTitoloCumModel> VecRicPM = new Vector<RichPMTitoloCumModel>();
		VecRicPM = lCtrlRich.ExRicercaRichPMTitoliCum(aIdRich);

		// Ricerca completa dei dati Aggregati ai Titoli coinvolti nella Richiesta di Revoca (Reati_Cum)
		Vector<TitoloCumulatoModel> VecTitoli = new Vector<TitoloCumulatoModel>();
		VecTitoli = lCtrlRich.ExRicercaAltriDatiRichiestaGE(VecRicPM);

		siesLogger.debug("--XX-- i Titoli collegati alla Richiesta sono " + VecTitoli.size());
		setRequestAttribute("TitoliRichiesta", VecTitoli);

		// recupero Descrizione Fonte Leg e Cod. Sottonumerazione (se esistono)
		String lDescrFonte = "";
		String lDescrSottoNum = "";
		if (lRicMod.getCodFonte() != null && lRicMod.getDescrFonte() != null) {
			// Fonte leg.
			lDescrFonte = lRicMod.getDescrFonte();
		}

		if (lRicMod.getCodSottonumerazione() != null && lRicMod.getDescrSottonumerazione() != null) {
			// Sottonumerazione
			lDescrSottoNum = lRicMod.getDescrSottonumerazione();
		}

		setRequestAttribute("DescrFonte", lDescrFonte);
		setRequestAttribute("DescrBisTer", lDescrSottoNum);

		return PG_DETT_RICH_GE_REV_PENA_PRINC;
	}

}