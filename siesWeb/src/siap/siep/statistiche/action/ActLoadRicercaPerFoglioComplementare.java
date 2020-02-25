package siap.siep.statistiche.action;

import siap.sico.web.ActionSiap;

/**
 * <p>Title: ActLoadRicercaPerFoglioComplementare</p>
 * <p>Description: Visualizza la form di "Ricerca Procedimeno per Estremi Foglio Complementare".</p>
 * <p>Company: Engineering S.p.A.</p>
 * @version 1.0
 */
public class ActLoadRicercaPerFoglioComplementare extends ActionSiap implements ICostantiStatistiche
{
  public String processRequest() throws Exception{
	super.setLinkRitorno();  
	setRequestAttribute("modalitaRicerca", RICERCA_FOGLIO_COMPLEMENTARE); 
    return PG_LOAD_RICERCA_PER_ESTREMI_FC;
  }
}
