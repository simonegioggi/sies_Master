package siap.sige.richiesta.controller;

import java.math.BigDecimal;

import siap.sige.richiesta.model.RichiestaSigeModel;
import f3b.util.F3BException;




/**
* <p>Title: RichiestaSigeController</p>
* <p>Description: Classe Controller per RichiestaSige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface IRichiestaSige
{
	public RichiestaSigeModel ExRicercaRichiestaSigeByKey (BigDecimal aKey)
     throws F3BException;

	/*
	public RichiestaSigeModel ExInserisciRichiestaSige (RichiestaSigeModel aRichiestaSige )
 		        throws F3BException;
	public Vector ExRicercaRichiestaSige (RichiestaSigeModel aRichiestaSige )
 		        throws F3BException;
	public RichiestaSigeModel ExModificaRichiestaSige (RichiestaSigeModel aRichiestaSige )
 		        throws F3BException;
	public void ExCancellaRichiestaSige (RichiestaSigeModel aRichiestaSige )
 		        throws F3BException;
 		        */
}
