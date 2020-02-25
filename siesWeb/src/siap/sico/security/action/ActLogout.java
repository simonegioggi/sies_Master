package siap.sico.security.action;

import siap.sico.web.ActionSiap;
import f3b.web.IWebConstants;

public class ActLogout extends ActionSiap implements ICostantiSecurity
{
  public String processRequest() throws Exception
  {
    getSession().invalidate();

    return IWebConstants.PAGE_CLOSE_FRAMESET;
  }
}
