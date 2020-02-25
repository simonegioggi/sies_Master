package siap.sius.statistiche.action;


/**
 * <p>Title: ActLoadRicercaPerFoglioComplementare</p>
 * <p>Description: Visualizza la form di "Ricerca procedimeno per estremi Foglio Complementare".
  * </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.web.ActionSiap;

public class ActLoadRicercaPerFoglioComplementare extends ActionSiap implements ICostantiStatistiche
{
  public String processRequest() throws Exception
  {
	setRequestAttribute("modalitaRicerca", RICERCA_FOGLIO_COMPLEMENTARE); 
	
	
    return PG_LOAD_RICERCHE_ORDINANZA;
  }
}
