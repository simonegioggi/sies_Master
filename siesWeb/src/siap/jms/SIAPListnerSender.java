package siap.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
//import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import siap.jms.config.JMSProperties;
import siap.jms.connection.ConnectionPoolJMS;
import siap.jms.manage.ManageMessageNoSend;
import f3b.log.LogF3B;
import f3b.util.xml.TreeModel;

/**
 * <p>Title: SIAPListnerSender</p>
 * <p>Description: Classe che realizza il Listner in ascolto per i messaggi in arrivo</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SIAPListnerSender  implements MessageListener,ICostantiJMS
{
 
 // 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
  private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);
  private static String idMessage = "";
	QueueReceiver mReceiver;
	private static ConnectionPoolJMS mPoolConnection;

  protected SIAPListnerSender()
  {
	  super();
  }
  
  /**
	 * Metodo statico per l'unico punto di accesso al listner
	 * @return
	 * @throws JMSException
	 * @throws NamingException
	 * @throws Exception
	 */
	public synchronized static SIAPListnerSender newSIAPListnerSender()
	throws JMSException, NamingException, Exception 
	{
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.info("[JMS]: inizio");
	  
		mPoolConnection = ConnectionPoolJMS.getInstance();
		SIAPListnerSender listner = new SIAPListnerSender();
		listner.initialize();
		
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		
		return listner;
	}
	/**
	 * initialize
	 * @throws NamingException
	 * @throws JMSException
	 * @throws Exception
	 */
	/*
	protected void initialize()
	throws NamingException, JMSException,Exception 
	{
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.info("[JMS]: inizio");
	  
		QueueConnection lConnection = mPoolConnection.getConnection();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("[JMS]: HashConnessione "+lConnection.hashCode());
		QueueSession qSession = lConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

		String lNameQueue = JMSProperties.getInstance().getProperty(QUEUE_IN_PARTENZA);

		Queue lQueue = qSession.createQueue(lNameQueue);
		QueueReceiver mReceiver = qSession.createReceiver(lQueue);

		SIAPListnerSender qListener = this;
		mReceiver.setMessageListener(qListener);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("[JMS]: Partito il Listner "+qListener.hashCode()+" sulla Coda " + lNameQueue);

		lConnection.start();
		
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}
	*/
	 protected void initialize() throws Exception {
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.info("[JMS]: inizio");
	    mPoolConnection = ConnectionPoolJMS.getInstance();

	    QueueConnection lConnection = mPoolConnection.getConnection();
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.info("[JMS]: HashConnessione " + lConnection.hashCode());

	    try{
	      QueueSession qSession = 
	        lConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	      String lNameQueue = JMSProperties.getInstance().getProperty(QUEUE_IN_PARTENZA);

	      Queue lQueue = qSession.createQueue(lNameQueue);
	      QueueReceiver mReceiver = qSession.createReceiver(lQueue);

	      SIAPListnerSender qListener = this;
	      mReceiver.setMessageListener(qListener);
	      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	      siesLogger.info("[JMS]: Partito il Listner " + qListener.hashCode()+" " +
	          "sulla Coda " + lNameQueue);

	      lConnection.start();
	    }
	    catch(Exception ex)
	    {
	      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	      siesLogger.error("[JMS]: Errore in SIAPListnerSender.inizialize" + ex.getMessage(),ex);
	      //ex.printStackTrace();
	      
	      if( ex instanceof javax.jms.IllegalStateException ) {
	        ConnectionPoolJMS.restart();
	        //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	        //siesLogger.error("[JMS]: Errore in SIAPListnerSender.inizialize" + ex.getMessage(),ex);
	        this.initialize();
	      }
	    }
	    
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.info("[JMS]: fine");
	  }



  /**
   * Metodo che viene attivato alla ricezione di un messaggio
   * @param aMessage
   * @throws RuntimeException
   */
  public void onMessage(Message aMessage) throws RuntimeException
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: inizio");
    try
    {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.info("[JMS]: Letto Messaggio in Partenza " + aMessage.getJMSType() + " ID = " + aMessage.getJMSMessageID());

   /*  if (aMessage.getJMSType().equalsIgnoreCase("TextMessage"))
      {
        TextMessage aText = (TextMessage) aMessage;
      }*/

    /*  if (aMessage.getJMSType().equalsIgnoreCase("ObjectMessage"))
      {*/
        ObjectMessage lMess = (ObjectMessage) aMessage;

        if (!idMessage.equals(lMess.getJMSMessageID()))
         {
           // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
           siesLogger.debug("[JMS]: \n\nNO LOOP "+idMessage+" != "+lMess.getJMSMessageID()+" \n\n");
           idMessage = lMess.getJMSMessageID();
         }
         else
         {
           // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
           siesLogger.debug("[JMS]: \n\nLOOP\n\n");
           return;
         }

        Object lObj = lMess.getObject();

        if (lObj instanceof TreeModel)
        {
           //Spedisco al Vero Destinatario il Messaggio
          String lDestConnection = lMess.getStringProperty(BDI_DESTINATARIA);

          try
          {
            SIAPSender lSend = new SIAPSender();
            lSend.sendToTrueDestination(lMess);
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.info("[JMS]: >>>>>>>> MESSAGGIO INVIATO  A "+lDestConnection+" <<<<<<<<<");
          }
          catch (Exception ex)
          {
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.error("[JMS]: *** ERRORE durante la Spedizione a " + lDestConnection + ":" + ex );
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.info("[JMS]: >>>>>>>> MESSAGGIO NON INVIATO  A " + lDestConnection + " <<<<<<<<<");

            //Non risco a arggiungere la destinazione. Aggiungo un messaggio di tipo Evento NON SPEDITO
            //nella tabella messaggio
            try{
              ManageMessageNoSend lMan = new ManageMessageNoSend();
              // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
              siesLogger.info("[JMS]: >>>new ManageMessageNoSend() <<<<<<<<<");

              lMan.elaboraMessaggioNonSpedito(lMess);

              // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
              siesLogger.info("[JMS]: \n>>> Messaggio NON SPEDITO memorizzato <<<<\n");
            }
            catch(Exception exCh)
            {
               // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
               siesLogger.info("[JMS]: \n>>> Errore durante la memorizzazione del messaggio NON spedito<<<<\n");
            }

            //-----throw new Exception("Impossibile connettersi alla Destinazione.");
         }


          //>>>>>>>>>>>>>>>>>>> INSERIRE CODICE PER TRATTARE I MESSAGGI <<<<<<<<<<<<<<

          if (aMessage.getJMSReplyTo() != null)
          {
            Queue lq = (Queue) aMessage.getJMSReplyTo();
             // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
             siesLogger.debug("[JMS]: Rispedire il messagio a = " + lq.getQueueName());
          }
/*        }
        else
         // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
         siesLogger.error(" * * * Eccezione in SIAPListnerSender onMessage: Il Messaggio " +
                             "con ID= " + lMess.getJMSMessageID() +" arrivato da = " +lMess.getStringProperty(BDI_MITTENTE) +
                             " arrivato con TimeStamp = " + lMess.getJMSTimestamp() + " NON CONTIENE UN BODY VALIDO");
*/
      }
      
        //Spedisco al Vero Destinatario del Messaggio di ERRORE
        String codiceEsito = lMess.getStringProperty(COD_ESITO);
	    if(ICostantiJMS.ERRORE_DEPLOY.equals(codiceEsito)){
	        String lDestConnection = lMess.getStringProperty(BDI_DESTINATARIA);	    	 
	    	SIAPSender lSend = new SIAPSender();
	        lSend.sendToTrueDestination(lMess);
	        siesLogger.info("[JMS]: >>>>>>>> MESSAGGIO ERRORE INVIATO  A "+lDestConnection+" <<<<<<<<<");
	    }
    }
    catch (JMSException eJms)
    {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.error("[JMS]: \n\n Eccezione nella spedizione dei messaggi in Partenza: " + eJms.getMessage());
      eJms.printStackTrace();
    }
    catch (Exception ex)
    {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.error("[JMS]: \n\n Eccezione nella spedizione dei messaggi in Partenza: " + ex.getMessage());
      ex.printStackTrace();
    }
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: fine");
  }

}