package siap.siep.tipoeventibdmc.controller;

/**
* <p>Title: TipoEventiBdmcController</p>
* <p>Description: Classe Controller per TipoEventiBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ITipoEventiBdmc {

	public TipoEventiBdmcModel ExInserisciTipoEventiBdmc(TipoEventiBdmcModel aTipoEventiBdmc)
			throws F3BException;

	public Vector ExRicercaTipoEventiBdmc(TipoEventiBdmcModel aTipoEventiBdmc) throws F3BException;

	public void ExModificaTipoEventiBdmc(TipoEventiBdmcModel aTipoEventiBdmc) throws F3BException;

	public void ExCancellaTipoEventiBdmc(TipoEventiBdmcModel aTipoEventiBdmc) throws F3BException;

	public BigDecimal ExGetCountTipoEventiBdmc(TipoEventiBdmcModel aTipoEventiBdmc) throws F3BException;

	public TipoEventiBdmcModel ExRicercaTipoEventiBdmcById(BigDecimal aIdTipoEventiBdmc) throws F3BException;

	public Vector ExRicercaTipoEventiBdmcPaged(TipoEventiBdmcModel aTipoEventiBdmc, int aPage)
			throws F3BException;

}
