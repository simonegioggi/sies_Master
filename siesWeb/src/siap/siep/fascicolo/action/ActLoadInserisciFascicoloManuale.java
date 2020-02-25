package siap.siep.fascicolo.action;


public class ActLoadInserisciFascicoloManuale extends ActLoadInserisciFascicolo
{
  public String processRequest() throws Exception
  {
   
	  setRequestAttribute("assegnazione_manuale", "S");

	  return super.processRequest();
  }
}
