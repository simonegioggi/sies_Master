package siap.sico.help.action;



import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


public class LoadSendMailToHelpDesk extends ActionSiap implements ICostantiHelp
{
  public String processRequest() throws F3BException
  {
    return PG_LOAD_PAGINAHELPDESK;
  }
}