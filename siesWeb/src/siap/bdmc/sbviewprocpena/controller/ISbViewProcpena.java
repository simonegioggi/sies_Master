package siap.bdmc.sbviewprocpena.controller;

/**
* <p>Title: SbViewProcpenaController</p>
* <p>Description: Classe Controller per SbViewProcpena</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISbViewProcpena {

	public SbViewProcpenaModel ExInserisciSbViewProcpena(SbViewProcpenaModel aSbViewProcpena)
			throws F3BException;

	public Vector ExRicercaSbViewProcpena(SbViewProcpenaModel aSbViewProcpena) throws F3BException;

	public void ExModificaSbViewProcpena(SbViewProcpenaModel aSbViewProcpena) throws F3BException;

	public void ExCancellaSbViewProcpena(SbViewProcpenaModel aSbViewProcpena) throws F3BException;

	public BigDecimal ExGetCountSbViewProcpena(SbViewProcpenaModel aSbViewProcpena) throws F3BException;

	public SbViewProcpenaModel ExRicercaSbViewProcpenaById(BigDecimal aIdPren) throws F3BException;

	public Vector ExRicercaSbViewProcpenaPaged(SbViewProcpenaModel aSbViewProcpena, int aPage)
			throws F3BException;

}