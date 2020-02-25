package siap.sius.statistiche.action;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.web.ActionSiap;

public class ActLoadGrigliaMonitoraggio extends ActionSiap implements ICostantiStatistiche
{
  public String processRequest() throws Exception
  {
	 setRequestAttribute("nome_funzione", "Monitoraggio");
	 setRequestAttribute("titolo", "Provvedimenti");
	 
    return PG_LOAD_GRIGLIA_RICERCHE;
  }
}
