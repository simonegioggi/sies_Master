package siap.sico.web;

import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

public class ActionUnderConstruction extends ActionSiap implements ISIAPCostantiWeb
{
  public String processRequest() throws F3BException
  {
   return ISIAPCostantiWeb.PG_UNDER_CONSTRUCTION;
  }
}
