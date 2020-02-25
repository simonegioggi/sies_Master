package siap.sius.statistiche.action;


/**
 * <p>Title: ActLoadRicercaFogliComplementariDaTrasmettere</p>
 * <p>Description: Visualizza la form di "Ricerca Ordinanze Prive di Fogli Complementari".
  * </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.web.ActionSiap;

public class ActLoadRicercaOrdinanzePriveDiFC extends ActionSiap implements ICostantiStatistiche
{
  public String processRequest() throws Exception
  {
	setRequestAttribute("modalitaRicerca", RICERCA_ORDINANZE_PRIVE_DI_FC); 
	
    return PG_LOAD_RICERCA_ORD_PRIVE_DI_FC;
  }
}
