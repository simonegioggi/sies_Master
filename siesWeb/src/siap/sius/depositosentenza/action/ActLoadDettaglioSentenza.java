package siap.sius.depositosentenza.action;

/**
* <p>Title: ActLoadDettaglioSentenza</p>
* <p>Description: Classe Action per la load dettaglio di DepositoSentenza</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActLoadDettaglioSentenza extends ActDettaglioEmissioneSentenza
	implements ICostantiDepositoSentenza
{
  public String processRequest() throws Exception
  {
    String retPage = null;
    retPage = super.processRequest();
  
    return retPage;
  }

}