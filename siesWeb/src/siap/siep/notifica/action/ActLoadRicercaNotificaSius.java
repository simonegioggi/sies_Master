package siap.siep.notifica.action;



import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaNotificaSIUS</p>
* <p>Description: Questa Action è demandata alla visualizzazione della maschera 
*   di Ricerca Notifiche/Comunicazioni Atti SIUS.</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaNotificaSius extends ActionSiap implements ICostantiNotifica
{
  public String processRequest() throws F3BException
  {
    return PG_LOAD_RICERCA_NOTIFICHE_COMUNICAZIONI_SIUS;
  }

}




