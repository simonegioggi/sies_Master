package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la visualizzazione del Dettaglio del provvedimento di Fungibilità (Titolo Cumulato) 0212 -
 * Fungibilità per computo Misura Cautelare Altro Reato art. 657 c.p.p 0213 - Fungibilità per computo Pena
 * Detentiva Espiata per Altro Reato art. 657 c.p.p
 * 
 * @author
 *
 */
public class ActLoadDettaglioFungibilitaCumulo extends ActionModuloCumulo
		implements ICostantiPresoffertoCumulo, ICostantiComputiCumulo, ICostantiStatoEsecTitoloCumulato {
	public String processRequest() throws F3BException {
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		BigDecimal idStatoEsec = null;
		if (!isRequestParameterNullObj(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO))
			idStatoEsec = getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);

		IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
		// LogF3B.getLogger().debug("--XX-- ActLoadDettaglioFungibilitaCumulo - id_Stato_esec =
		// "+idStatoEsec);
		StatoEsecTitoloCumulatoModel lStato = lCtrlStato
				.ExRicercaStatoEsecTitoloCumulatoByIdFull(idStatoEsec);

		/*
		 * Vector <ComputiCumuloModel> lListaPeriodi = new Vector (lStato.getListaComputi());
		 * 
		 * for (int i=0; i<lListaPeriodi.size(); i++) { ComputiCumuloModel lComputo =
		 * lListaPeriodi.elementAt(i); if (lComputo.getIdComputiCumulo()!=null) {
		 * LogF3B.getLogger().debug("--XX-- ActLoadDettaglioFungibilitaCumulo - Elemento >"
		 * +i+"< - ComputoModel = "+lComputo); } }
		 */
		setRequestAttribute("Provvedimento", lStato);

		return PG_LOAD_DETTAGLIO_FUNGIBILITA_CUMULO;
	}
}
