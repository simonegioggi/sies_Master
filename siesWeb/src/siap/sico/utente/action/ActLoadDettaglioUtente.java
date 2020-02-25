package siap.sico.utente.action;

import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;


/**
* <p>Title: ActLoadDettaglioUtente</p>
* <p>Description: Classe Action per la load dettaglio di Utente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioUtente extends ActionSiap implements ICostantiUtente
{
  public String processRequest() throws Exception {
  	 
 		 String lId = getRequestStringParameter(CAMPO_COD_UTENTE);

		 IUtente lCtrl = SICOLookupRemote.getUtenteRemote();
		 UtenteModel llUteMod = lCtrl.ExRicercaUtenteByKey(lId);
		 setRequestAttribute("utente", llUteMod);

		 return PG_LOAD_DETTAGLIOUTENTE;
	 }
}