package siap.sico.utente.action;

import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActCancellaUtente</p>
* <p>Description: Classe Action per la cancellazione di Utente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActCancellaUtente extends ActionSiap implements ICostantiUtente
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_COD_UTENTE);
 		 // riempie il model
		 // chiama il controller
                 UtenteModel lUt=new UtenteModel();
                 lUt.setUserId(lId);
                 lUt.setCodOperatoreAggiornamento(getCodUtenteConnesso());
                 lUt.setDataAggiornamento(DateUtils.getSysDate());
                 lUt.setDataFineValidita(DateUtils.getSysDate());
		 IUtente lCtrl = SICOLookupRemote.getUtenteRemote();
		 lCtrl.ExCancellaUtente(lUt);

		 return IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.sico.utente.action.ActListaUtentiAttivi";
	 }



}