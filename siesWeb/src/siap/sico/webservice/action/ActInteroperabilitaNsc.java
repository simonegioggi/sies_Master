package siap.sico.webservice.action;



public class ActInteroperabilitaNsc extends ActWsBase implements ICostantiNsc
{
  public String processRequest() throws Exception
  {

      setRequestAttribute("strFunzione", "Interoperabilita con NSC");

      return ICostantiNsc.PG_INTEROPERABILITA_NSC; 
  }  
}
