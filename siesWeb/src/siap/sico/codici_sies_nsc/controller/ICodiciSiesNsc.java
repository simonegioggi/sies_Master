package siap.sico.codici_sies_nsc.controller;

/**
* <p>Title: CodiciSiesNscController</p>
* <p>Description: Classe Controller per CodiciSiesNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ICodiciSiesNsc {

	public CodiciSiesNscModel ExRicercaCodiciSiesNscById(String aCoDomain, String aCoCodcentr)
			throws F3BException;

	public Vector ExRicercaCodiciSiesNsc(CodiciSiesNscModel aCodiciSiesNsc) throws F3BException;

	// public CodiciSiesNscModel ExInserisciCodiciSiesNsc (CodiciSiesNscModel aCodiciSiesNsc ) throws
	// F3BException ;
	// public void ExModificaCodiciSiesNsc (CodiciSiesNscModel aCodiciSiesNsc ) throws F3BException;
	// public void ExCancellaCodiciSiesNsc (CodiciSiesNscModel aCodiciSiesNsc ) throws F3BException ;
	// public BigDecimal ExGetCountCodiciSiesNsc (CodiciSiesNscModel aCodiciSiesNsc ) throws F3BException ;
	// public Vector ExRicercaCodiciSiesNscPaged (CodiciSiesNscModel aCodiciSiesNsc,int aPage ) throws
	// F3BException ;

}