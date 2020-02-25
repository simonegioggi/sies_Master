package siap.sige.statistiche.action;

import siap.sige.web.ActionSige;

/**
 * <p>Title: ActLoadRicercaOrdinanzePriveDiFC</p>
 * <p>Description: Visualizza la form di "Ricerca Ordinanze Prive di Fogli Complementari".</p>
 * <p>Company: Engineering S.p.A.</p>
 * @version 1.0
 */
public class ActLoadRicercaOrdinanzePriveDiFC extends ActionSige implements ICostantiStatistiche
{
  public String processRequest() throws Exception
  {
	setRequestAttribute("modalitaRicerca", RICERCA_ORDINANZE_PRIVE_DI_FC); 
	
    return PG_LOAD_RICERCA_ORD_PRIVE_DI_FC;
  }
}
