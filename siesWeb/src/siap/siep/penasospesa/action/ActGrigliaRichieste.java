package siap.siep.penasospesa.action;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.web.ActionSiap;
import siap.sius.statistiche.action.ICostantiStatistiche;

public class ActGrigliaRichieste extends ActionSiap 
{
  public String processRequest() throws Exception
  {
	 setRequestAttribute("nome_funzione", "Pene Sospese");
	 setRequestAttribute("titolo", "Richieste");
	 
    return ICostantiStatistiche.PG_LOAD_GRIGLIA_RICERCHE;
  }
}
