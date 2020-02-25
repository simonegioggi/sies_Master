package siap.sico.magistratocompetente.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


public class ActLoadRicercaProcedimentiAssegnati extends ActionSiap implements ICostantiMagistratoCompetente
{
  /**
  * Azione di Caricamento della Pagina di Ricerca Procedimenti assegnati a un 
  * Magistrato
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException
  {
    return PG_LOAD_RICERCAPROCEDIMENTI;
  }
}
