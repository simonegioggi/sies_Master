package siap.bdmc.sbperipren.controller;

/**
* <p>Title: SbPeriprenController</p>
* <p>Description: Classe Controller per SbPeripren</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.bdmc.sbperipren.model.SbPeriprenModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISbPeripren {

	public SbPeriprenModel ExInserisciSbPeripren(SbPeriprenModel aSbPeripren) throws F3BException;

	public Vector ExRicercaSbPeripren(SbPeriprenModel aSbPeripren) throws F3BException;

	public void ExModificaSbPeripren(SbPeriprenModel aSbPeripren) throws F3BException;

	public void ExCancellaSbPeripren(SbPeriprenModel aSbPeripren) throws F3BException;

	public BigDecimal ExGetCountSbPeripren(SbPeriprenModel aSbPeripren) throws F3BException;

	public SbPeriprenModel ExRicercaSbPeriprenById(BigDecimal aProgPeriPres) throws F3BException;

	public Vector ExRicercaSbPeriprenPaged(SbPeriprenModel aSbPeripren, int aPage) throws F3BException;

}