package siap.sico.webservice.action;


public class wsNscToSies
{
	  public String importNscToSies(String flussoXML) throws Exception
	  {
			  String flussoXmlRisposta;
      
			  ActNscToSies objActNscToSieso = new ActNscToSies();
			  flussoXmlRisposta = objActNscToSieso.processRequest(flussoXML);
        
			  return flussoXmlRisposta;

	  }

}
