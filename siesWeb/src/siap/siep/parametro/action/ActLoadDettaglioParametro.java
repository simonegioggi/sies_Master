package siap.siep.parametro.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioParametro</p>
* <p>Description: Classe Action per la load dettaglio di Parametro</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioParametro extends ActionSiap implements ICostantiParametro
{
public String processRequest() throws F3BException {

   //  ParametroModel lParametro = (ParametroModel)getRequestAttribute("parametro");

      String lId = getRequestStringParameter(CAMPO_ID_PARAMETRO);
 		 // riempie il model
		 // chiama il controller

		 IParametro lCtrl = SIEPLookupRemote.getParametroRemote();
		 ParametroModel llParMod = lCtrl.ExRicercaParametroByKey(new BigDecimal(lId));
		 setRequestAttribute("parametro", llParMod);

		 return PG_LOAD_DETTAGLIOPARAMETRO;
	 }



}