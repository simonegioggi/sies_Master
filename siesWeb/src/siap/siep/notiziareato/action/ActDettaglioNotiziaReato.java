package siap.siep.notiziareato.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.notiziareato.controller.NotiziaReatoController;
import siap.siep.notiziareato.model.NotiziaReatoModel;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadDettaglioNotiziaReato</p>
* <p>Description: Classe Action per la load dettaglio Notizia di Reato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActDettaglioNotiziaReato extends ActionSiap implements ICostantiNotiziaReato
{

public String processRequest() throws F3BException {


 		 String lId = getRequestStringParameter(CAMPO_ID_NOTIZIA_REATO);

		 // chiama il controller
		 //INotiziaReato lCtrl = SIEPLookupRemote.getNotiziaReatoRemote();
     NotiziaReatoController lCtrl = new NotiziaReatoController();

     // Instanzia e riempie il model
		 NotiziaReatoModel llNotMod = lCtrl.ExRicercaNotiziaReatoByKey(new BigDecimal(lId));
		 setRequestAttribute("notiziareato", llNotMod);
		 
	 // IMPOSTAZIONI PER FUNZIONALITà BACK
		if(!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)){
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
		}

     // Apre la pagina di dettaglio della Notizia di Reato
		 return PG_LOAD_DETTAGLIONOTIZIAREATO;
	 }



}