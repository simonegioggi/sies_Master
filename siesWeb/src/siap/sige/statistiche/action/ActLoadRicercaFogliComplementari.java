package siap.sige.statistiche.action;

import siap.sige.web.ActionSige;

/**
 * <p>Title: ActLoadRicercaFogliComplementari</p>
 * <p>Description: Visualizza la form di "Ricerca procedimeno per estremi Foglio Complementare".</p>
 * <p>Company: Engineering S.p.A.</p>
 * @version 1.0
 */
public class ActLoadRicercaFogliComplementari extends ActionSige implements ICostantiStatistiche
{
  public String processRequest() throws Exception
  {
	setRequestAttribute("modalitaRicerca", RICERCA_FOGLIO_COMPLEMENTARE); 

	return PG_LOAD_RICERCA_FC;
  }
}
