package siap.bdmc.sbviewreat.controller;

/**
* <p>Title: SbViewReatController</p>
* <p>Description: Classe Controller per SbViewReat</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.bdmc.sbviewreat.model.SbViewReatModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISbViewReat {

	public SbViewReatModel ExInserisciSbViewReat(SbViewReatModel aSbViewReat) throws F3BException;

	public Vector ExRicercaSbViewReat(SbViewReatModel aSbViewReat) throws F3BException;

	public void ExModificaSbViewReat(SbViewReatModel aSbViewReat) throws F3BException;

	public void ExCancellaSbViewReat(SbViewReatModel aSbViewReat) throws F3BException;

	public BigDecimal ExGetCountSbViewReat(SbViewReatModel aSbViewReat) throws F3BException;

	public SbViewReatModel ExRicercaSbViewReatById(BigDecimal aIdPren, BigDecimal aNumeProgCapoImpu,
			BigDecimal aNumeProgReat) throws F3BException;

	public Vector ExRicercaSbViewReatPaged(SbViewReatModel aSbViewReat, int aPage) throws F3BException;

}