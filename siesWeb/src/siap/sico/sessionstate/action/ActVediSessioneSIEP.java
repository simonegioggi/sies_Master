package siap.sico.sessionstate.action;

/**
 * <p>Title: ActInserisciSoggetto</p>
 * <p>Description: Azione di visualizzazione degli oggetti in sessione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

//import siap.sico.soggetto.controller.SoggettoController;
//import siap.sico.decodifiche.controller.ComuneController;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActVediSessioneSIEP extends ActionSiap
{
  /**
   * Azione di Inserimento del Soggetto
   * <p>
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {

     String lPage = "/jsp/files/siap/siep/VediStatoSessione.jsp";

     return lPage;
  }
}
