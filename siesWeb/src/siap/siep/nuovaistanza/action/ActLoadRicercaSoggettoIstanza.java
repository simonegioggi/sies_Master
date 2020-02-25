package siap.siep.nuovaistanza.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaSoggettoIstanza</p>
 * <p>Description: Carica la pagina della Ricerca del Soggetto per inserimento istanza</p>
 * <p>Copyright: Agile Copyright (c) 2009</p>
 * <p>Company: Agile</p>
 */

public class ActLoadRicercaSoggettoIstanza extends ActionSiap implements ICostantiNuovaIstanza
{
  /**
   * Azione di caricamento della form di ricerca del Soggetto per l'istanza.
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * <p>
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
      
    Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), "-");
    setRequestAttribute("nazioni", "" + lOption );


    return PG_LOAD_RICERCA_SOGGETTO_ISTANZA; //restituisce la jsp di VIEW
  
  }
}
