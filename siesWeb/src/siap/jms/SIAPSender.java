package siap.jms;

import javax.jms.Message;

import siap.jms.messaggio.model.MessaggioModel;

/**
 * <p>Title: SIAPSender</p>
 * <p>Description: Classe che ha la responsabilità delle spedizioni dei messaggi
 * Deriva dalla vecchia gestione con OpenJMS e ora rimane solo di facciata per chiamare la classe 
 * SIAPSendereMQ che svolge il lavoro reale di spedizione.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 */

public class SIAPSender implements ICostantiJMS
{

 

  public SIAPSender() 
  //throws Exception
  {
    //Prende il riferimento all'unica istanza del Connection Pool
  }

 
  
  
  /**
   * Metodo per spedire un Messaggio con openJMS. Spedisce i messaggi sempre sulla 
   * conda inPartenza del server JMS locale utilizzando una connessione prelevata
   * dal Connection POOL
   * @param aMessage
   * @throws Exception
   */
  public void send(MessaggioModel aSiapMessage) throws Exception
  {
	  SIAPSenderMQ.newSIAPSenderMQ().send(aSiapMessage);
   }

  /**
   * Metodo per spedire un Messaggio con openJMS a diverse BDI
   * Metodo utilizzato per la ricerca su piu' BDI
   * @param aMessage
   * @throws Exception
   */
  public void sendToMultipleBDI(MessaggioModel aSiapMessage) throws Exception
  {
	  SIAPSenderMQ.newSIAPSenderMQ().sendToMultipleBDI(aSiapMessage);
  }

  /**
   * Metodo usato per spedire i Messaggi all'effettivo desitnatario e non
   * alla coda locale di Partenza
   * @param aMessage
   * @throws Exception
   */
  public void sendToTrueDestination(Message aMessage) throws Exception
  {
	  SIAPSenderMQ.newSIAPSenderMQ().sendToTrueDestination(aMessage);
  }

  /**
   * Metodo usato per rispedire i Messaggi all'effettivo desitnatario e non
   * alla coda locale di Partenza.
   * @param aMessage
   * @throws Exception
   */
  public void sendToTrueDestination(MessaggioModel aMessage) throws Exception
  {
	  SIAPSenderMQ.newSIAPSenderMQ().sendToTrueDestination(aMessage);
  }

  /** 
   * AGGIUNGO METODO PER MEV PROBLEMA CODE
   * 
 * @param aSiapMessage
 * @throws Exception
 */
public void sendError(MessaggioModel aSiapMessage) throws Exception
  {
	  SIAPSenderMQ.newSIAPSenderMQ().sendError(aSiapMessage);
   }

}