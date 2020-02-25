package siap.jms.connection;

import java.util.Properties;

import javax.jms.QueueConnection;
import javax.naming.Context;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.config.JMSProperties;

import com.sun.messaging.ConnectionConfiguration;

import f3b.log.LogF3B;

/**
 * <p>Title: ConnectionJMS </p>
 * <p>Description: Connection JMS generica verso un server Open JMS esterno.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ConnectionJMS implements ICostantiJMS
{
  // 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
  private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);
		
  public ConnectionJMS()
  { }

  /**
   * Restituisce una Connessione al Server OpenJMS specificato dalla Stringa
   * @param aDestinatario
   * @return QueueConnection
   * @throws Exception
   */
  public QueueConnection getConnection(String aDestinatario) throws Exception
  {
     QueueConnection lCon = null;

      try
      {
        //Setta le proprietà della connessione JMS

        // --- Qui devo inserire il fatto che il destinatario deve ---
        // --- essere preso dalla tabella ---
        // String lUrl = JMSProperties.getProperty(aDestinatario);
        // --- Qui devo inserire il fatto che il destinatario deve ---
        // --- essere preso dalla tabella ---
        Properties props = new Properties();

        //nuovo codice
        String lUrl = JMSProperties.getInstance().getConnectionString(aDestinatario);

        //fine modifica

        if (lUrl == null)
        {
         // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
         siesLogger.error(getClass().getName() + "Destinatario JMS non impostato correttamente.");
         throw new Exception("Destinatario JMS non impostato correttamente.");
        }

        if (!lUrl.startsWith("http"))
        	/*{
          props.setProperty(Context.INITIAL_CONTEXT_FACTORY,  org.exolab.jms.jndi.InitialContextFactory.class.getName());
        }
        else*/
          throw new Exception("Connessione JMS non impostata con protocollo HTTP! " +
                              "- Verificare che il File siapjms.properties sia impostato correttamente.");

        props.setProperty(Context.PROVIDER_URL, lUrl);

        // Initial Context
        // Context context = new InitialContext(props);
       
        //Connection Factory
        //QueueConnectionFactory qFactory = (QueueConnectionFactory) context.lookup(JMS_CONNECTION_FACTORY);
        com.sun.messaging.ConnectionFactory qFactory = new com.sun.messaging.ConnectionFactory();
        qFactory.setProperty(ConnectionConfiguration.imqAddressList,lUrl+"/imqhttp/tunnel");
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.info(" >>> OpenJMS Connesso a " + lUrl + " <<<");
     
        //Creazione di una Connection per interazioni Point to Point
        lCon = qFactory.createQueueConnection();

      }
      catch (Exception ex)
      {
       // ex.printStackTrace();
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.error(getClass().getName()+"\n\n*** ERRORE durante il collegamento a " + aDestinatario +
        "*** " + ex.toString());
        throw ex;
      }
    return lCon;
  }
}