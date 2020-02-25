package siap.sico.test.action;

import siap.sico.web.ActionSiap;
import f3b.web.IWebConstants;

public class ActLoadTestSistema  extends ActionSiap
{
  public String processRequest()
  {
        return IWebConstants.ROOT_DIR + "files/siap/sico/test/StampaTest.jsp";

  }

}