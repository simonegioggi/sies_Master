package siap.sius.udienzaprocedimento.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioUdienzaProcedimento</p>
* <p>Description: Classe Action per la load dettaglio di UdienzaProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioUdienzaProcedimento extends ActionSiap implements ICostantiUdienzaProcedimento
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_ID_UDIENZA_PROCEDIMENTO);
 		 // riempie il model
		 // chiama il controller

		 IUdienzaProcedimento lCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
		 UdienzaProcedimentoModel llUdiMod = lCtrl.ExRicercaUdienzaProcedimentoByKey(new BigDecimal(lId));
		 setRequestAttribute("udienzaprocedimento", llUdiMod);

		 return PG_LOAD_DETTAGLIOUDIENZAPROCEDIMENTO;
	 }



}