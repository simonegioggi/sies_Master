package siap.sico.jms.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

public class ActLoadRicercaSoggettoAltreBDI extends ActionSiap implements ICostantiSoggetto
{
  /**
   * Azione di caricamento della form di ricerca del Soggetto.
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * <p>
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {

    Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), "039");
    setRequestAttribute("nazioni", "" + lOption );

     lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
     setRequestAttribute("autoritaEsterna", "" + lOption );

    return IWebConstants.ROOT_DIR + "files/siap/sico/jms/LoadRicercaSoggettoAltreBDI.jsp";
  }
}
