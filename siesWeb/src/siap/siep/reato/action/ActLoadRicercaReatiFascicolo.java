package siap.siep.reato.action;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import siap.sico.web.ActionSiap;

public class ActLoadRicercaReatiFascicolo extends ActionSiap implements ICostantiReato
{
  public String processRequest() throws Exception
  {
    String formname = new String();
    formname = getRequestStringParameter("formname");
    
    setRequestAttribute("formname",formname);
    
    return PG_LOADRICERCAREATIFASCICOLO;  //restituisce la jsp di VIEW
  }
}
