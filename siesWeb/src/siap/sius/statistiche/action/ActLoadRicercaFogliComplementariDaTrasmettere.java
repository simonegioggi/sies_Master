package siap.sius.statistiche.action;


/**
 * <p>Title: ActLoadRicercaFogliComplementariDaTrasmettere</p>
 * <p>Description: Visualizza la form di "Ricerca Fogli Complementari Non Ancora Trasmessi".
  * </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.web.ActionSiap;

public class ActLoadRicercaFogliComplementariDaTrasmettere extends ActionSiap implements ICostantiStatistiche
{
  public String processRequest() throws Exception
  {
	setRequestAttribute("modalitaRicerca", RICERCA_FC_DA_TRASMETTERE); 
	
    return PG_LOAD_RICERCA_FC_DA_TRASMETTERE;
  }
}
