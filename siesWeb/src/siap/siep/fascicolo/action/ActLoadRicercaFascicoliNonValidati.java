package siap.siep.fascicolo.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadRicercaFascicoliNonValidati</p>
 * <p>Description: Carica la pagina della Ricerca del Soggetto</p>
 * <p>Copyright: Bull Italia Copyright (c) 2002</p>
 * <p>Company: Bull Italia</p>
 */

public class ActLoadRicercaFascicoliNonValidati extends ActionSiap
                                                implements ICostantiFascicoloSiep
{
  /**
   * Azione di caricamento della form di ricerca dei Fascicoli Siep non validati.
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * <p>
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
    return PG_RICERCA_LOAD_RICERCA_FASCICOLI_NON_VALIDATI; //restituisce la jsp di VIEW
  }
}
