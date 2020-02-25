package siap.sico.provvedimentisiesnsc.controller;

/**
* <p>Title: ProvvSiesNscController</p>
* <p>Description: Classe Controller per ProvvSiesNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import siap.sico.provvedimentisiesnsc.model.ProvvSiesNscModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IProvvSiesNsc {

	public Vector ExRicercaProvvSiesNsc(ProvvSiesNscModel aProvvSiesNsc) throws F3BException;

	public ProvvSiesNscModel ExRicercaProvvSiesNscById(String aProvvDomain, String aProvvCodcentr)
			throws F3BException;

	/*
	 * public ProvvSiesNscModel ExInserisciProvvSiesNsc (ProvvSiesNscModel aProvvSiesNsc ) throws F3BException
	 * ; public void ExModificaProvvSiesNsc (ProvvSiesNscModel aProvvSiesNsc ) throws F3BException; public
	 * void ExCancellaProvvSiesNsc (ProvvSiesNscModel aProvvSiesNsc ) throws F3BException ; public BigDecimal
	 * ExGetCountProvvSiesNsc (ProvvSiesNscModel aProvvSiesNsc ) throws F3BException ; public Vector
	 * ExRicercaProvvSiesNscPaged (ProvvSiesNscModel aProvvSiesNsc,int aPage ) throws F3BException ;
	 */

}