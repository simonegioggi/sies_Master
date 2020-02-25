package siap.bdmc.sbviewcapoimpu.controller;

/**
* <p>Title: SbViewCapoimpuController</p>
* <p>Description: Classe Controller per SbViewCapoimpu</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISbViewCapoimpu {

	public SbViewCapoimpuModel ExInserisciSbViewCapoimpu(SbViewCapoimpuModel aSbViewCapoimpu)
			throws F3BException;

	public Vector ExRicercaSbViewCapoimpu(SbViewCapoimpuModel aSbViewCapoimpu) throws F3BException;

	public void ExModificaSbViewCapoimpu(SbViewCapoimpuModel aSbViewCapoimpu) throws F3BException;

	public void ExCancellaSbViewCapoimpu(SbViewCapoimpuModel aSbViewCapoimpu) throws F3BException;

	public BigDecimal ExGetCountSbViewCapoimpu(SbViewCapoimpuModel aSbViewCapoimpu) throws F3BException;

	public SbViewCapoimpuModel ExRicercaSbViewCapoimpuById(BigDecimal aIdPren, BigDecimal aNumeProgCapoImpu)
			throws F3BException;

	public Vector ExRicercaSbViewCapoimpuPaged(SbViewCapoimpuModel aSbViewCapoimpu, int aPage)
			throws F3BException;

}