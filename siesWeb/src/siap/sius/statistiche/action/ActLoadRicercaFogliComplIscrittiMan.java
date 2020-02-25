package siap.sius.statistiche.action;


/**
 * <p>Title: ActLoadRicercaPerFoglioComplementare</p>
 * <p>Description: Visualizza la form di "Ricerca Fogli Complementari Iscritti Manualmente".
  * </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.web.ActionSiap;

public class ActLoadRicercaFogliComplIscrittiMan extends ActionSiap implements ICostantiStatistiche
{
  public String processRequest() throws Exception
  {
	setRequestAttribute("modalitaRicerca", RICERCA_FC_ISCRITTI_MANUALMENTE); 
	
    return PG_LOAD_RICERCA_FC_TRASMESSI;
  }
}
