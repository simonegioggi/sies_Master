package siap.bdmc.statoprenotazionibdmc.controller;

/**
* <p>Title: StatoPrenotazioniBdmcController</p>
* <p>Description: Classe Controller per StatoPrenotazioniBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IStatoPrenotazioniBdmc {

	public StatoPrenotazioniBdmcModel ExInserisciStatoPrenotazioniBdmc(
			StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc) throws F3BException;

	public Vector ExRicercaStatoPrenotazioniBdmc(StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc)
			throws F3BException;

	public void ExModificaStatoPrenotazioniBdmc(StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc)
			throws F3BException;

	public void ExCancellaStatoPrenotazioniBdmc(StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc)
			throws F3BException;

	public BigDecimal ExGetCountStatoPrenotazioniBdmc(StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc)
			throws F3BException;

	public StatoPrenotazioniBdmcModel ExRicercaStatoPrenotazioniBdmcById(BigDecimal aStatoPrenotazioniBdmc)
			throws F3BException;

	public Vector ExRicercaStatoPrenotazioniBdmcPaged(StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc,
			int aPage) throws F3BException;

}