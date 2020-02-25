package siap.bdmc.sbpren.controller;

/**
* <p>Title: SbPrenController</p>
* <p>Description: Classe Controller per SbPren</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.bdmc.sbpren.model.SbPrenModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISbPren {

	public SbPrenModel ExInserisciSbPren(SbPrenModel aSbPren) throws F3BException;

	public Vector ExRicercaSbPren(SbPrenModel aSbPren) throws F3BException;

	public void ExModificaSbPren(SbPrenModel aSbPren) throws F3BException;

	public void ExCancellaSbPren(SbPrenModel aSbPren) throws F3BException;

	public BigDecimal ExGetCountSbPren(SbPrenModel aSbPren) throws F3BException;

	public SbPrenModel ExRicercaSbPrenById(BigDecimal aIdPren) throws F3BException;

	public siap.bdmc.sbpren.model.ProvvedimentoModelBDMC ExRicercaSogg(SbPrenModel aSbPren,
			siap.bdmc.sbpren.model.ProvvedimentoModelBDMC aProvvedimento) throws F3BException;

	public Vector ExRicercaSbPrenPaged(SbPrenModel aSbPren, int aPage) throws F3BException;

}