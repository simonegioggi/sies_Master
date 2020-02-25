package siap.sige.statistiche.action;

import siap.sige.web.ActionSige;

public class ActLoadGrigliaRicercheFC extends ActionSige implements ICostantiStatistiche{
  public String processRequest() {
	  //return PG_LOAD_GRIGLIA_RICERCA_FC;
	  return PG_LOAD_RICERCA_STATISTICHE_FC;
  }
}