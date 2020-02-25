package siap.sico.test.action;

import java.io.ByteArrayOutputStream;

import siap.sico.test.controller.TestController;
import siap.sico.test.model.TestModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 *
 * <p>Title: ActTestSistema</p>
 * <p>Description: Classe che implementa il test del sistema SIES</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Bull Italia S.p.A.</p>
 *  not attributable
 *  1.0
 */
public class ActTestSistema extends ActionSiap
{
  public String processRequest() throws Exception
  {
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUfficio = this.getUfficioUtenteConnesso();
    String lJspReturn = "";

    TestController lTestCtrl = new TestController();
    try
    {
      ByteArrayOutputStream lReport = lTestCtrl.ExTestSistema(lUtenteMod, lUfficio);
      setRequestAttribute("report", lReport);
      lJspReturn = IWebConstants.PG_DOWNLOAD;
    }
    catch (F3BException fex)
    {
      setRequestAttribute("test", new TestModel());
      setRequestAttribute("errore", fex.getMessage());
      lJspReturn = IWebConstants.ROOT_DIR + "files/siap/sico/test/TestFallito.jsp";
    }
    //Prepara la pagina di destinazione
    return lJspReturn;
  }

}