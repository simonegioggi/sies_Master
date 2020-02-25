package siap.sico.sessionstate.action;

/**
 * <p>Title: ActInserisciSoggetto</p>
 * <p>Description: Azione di cancellazione degli oggetti in sessione di lavoro</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

//import siap.sico.soggetto.controller.SoggettoController;
//import siap.sico.decodifiche.controller.ComuneController;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActCleanWorkSessionSIEP extends ActionSiap
{
  public String processRequest() throws F3BException
  {
    setSessionAttribute("fascicolo",null);
    setSessionAttribute("soggetto",null);
    setSessionAttribute("sentenza",null);

    setSessionAttribute("cumulowiz",null);
    setSessionAttribute("penaresidua",null);
    setSessionAttribute("reato",null);

    String lPage = "/jsp/files/siap/siep/VediStatoSessione.jsp";

    return lPage;
  }
}