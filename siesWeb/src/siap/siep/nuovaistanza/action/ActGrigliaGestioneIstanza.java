package siap.siep.nuovaistanza.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActGrigliaGestioneIstanza extends ActionSiap implements ICostantiNuovaIstanza
{
  public String processRequest() throws F3BException
  {
    //setRequestAttribute("strFunzione", "Iscrizione Istanza");
   // return ICostantiNuovaIstanza.PG_GRIGLIA_GESTIONE_ISTANZA;
	  return IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/GrigliaGestioneIstanza.jsp";
  }
}
