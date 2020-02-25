package siap.sius.magistratorelatore.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


public class ActLoadRicercaProcedimentiSorveglianzaAssegnati extends ActionSiap implements ICostantiMagistratoRelatore
{
  /**
  * Azione di Caricamento della Pagina di Ricerca Procedimenti assegnati a un 
  * Magistrato di Sorveglianza
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException
  {
    return PG_LOAD_RICERCAPROCEDIMENTI_SORVEGLIANZA;
  }
}
