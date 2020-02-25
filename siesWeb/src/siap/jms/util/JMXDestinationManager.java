package siap.jms.util;

import java.io.IOException;
import java.util.Date;

import javax.jms.JMSException;
import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import org.apache.log4j.Logger;

import com.sun.messaging.AdminConnectionConfiguration;
import com.sun.messaging.AdminConnectionFactory;
import com.sun.messaging.jms.management.server.DestinationAttributes;
import com.sun.messaging.jms.management.server.DestinationOperations;
import com.sun.messaging.jms.management.server.MQObjectName;

import f3b.log.LogF3B;

public class JMXDestinationManager {

  // 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
  private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);
		
  private JMXConnector mJMXC = null;
  private MBeanServerConnection mMBSC = null;
  private static JMXDestinationManager mJMXDM = null;
  
  /**
   * Costructor.
   */ 
  protected JMXDestinationManager() throws Exception { 
    init();
  }
 
  /**
   * Method getIsntace() for singleton class.
   * <p>
   * @return {@link JMXDestinationManager}
   */
  public static synchronized JMXDestinationManager getInstance() {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: inizio");

    try {
      if(mJMXDM == null)
        mJMXDM = new JMXDestinationManager();
      else
        mJMXDM.checkAndReinit();
    }catch (Exception ex){
      ex.printStackTrace();
      //throw new F3BException(F3BException.USER_MESSAGE, 
      //    "Attenzione! Il Servizio JMS non è attivo. Rivolgersi all'amministratore di sistema!");
    }
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: fine");
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info(" ");
    
    return mJMXDM;
  }

  /**
   * Wrap method for init connection with JMX Repository.
   * <p>
   * @throws Exception.
   */
  protected void checkAndReinit() throws Exception {
    this.init();
  }
  
  /**
   * Method for initialize connection to JMX Repository.
   * <p> 
   * @throws Exception.
   */
  protected void init() throws Exception {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: inizio");
    // Get Connector.    
    if( mJMXC == null || this.getConnectionId().equals("-1") ){
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.info("[JMS]: Apertura connessione JMXConnector!");
      mJMXC = this.getJMXConnector();
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.info("[JMS]: Apertura connessione MBeanServer!");
      mMBSC = this.getMBeanSeverConnection();
    }
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: fine");
  }
 
  /**
   * Return connection-id to JMX.
   * <p>
   * @return {@link String}
   */
  protected String getConnectionId(){
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: inizio");
    
    String lConnID = new String("-1");
    try {
      lConnID = mJMXC.getConnectionId();
    }catch (Exception ex){}
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: getConnectionID() " + lConnID);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: fine");
    
    return lConnID;
  }
  
  /**
   * Return a JMXConnector.
   * <p> 
   * @return {@link JMXConnector}
   * @throws Exception
   */
  protected JMXConnector getJMXConnector() throws Exception {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: inizio");
    JMXConnector lJMXC = null; 
    AdminConnectionFactory lACF = new AdminConnectionFactory();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: AdminConnectionConfiguration Value : " + 
    lACF.getProperty(AdminConnectionConfiguration.imqAddress));
    
    try {
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.info("[JMS]: Crea connessione JMXConnector.");      
        //if( lJMXC == null )
        lJMXC = lACF.createConnection("admin", "admin");
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.info("[JMS]: Creata connessione JMXConnector id: " + this.getConnectionId());
    } catch (JMException e) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.error("[JMS]: " + e.getMessage());
      throw e;
    } finally {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.info("[JMS]: fine");
    }
    
    return lJMXC; 
  }
  
  /**
   * Return a MBeanServeronnection.
   * @return {@link MBeanServerConnection}
   * @throws JMSException
   * @throws JMException
   * @throws IOException
   */
  protected MBeanServerConnection getMBeanSeverConnection() 
  throws JMSException, JMException, IOException {
    MBeanServerConnection lMBSC = null;

    try {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.info("[JMS]: Before getMBeanServerConnection() ");
      lMBSC = mJMXC.getMBeanServerConnection();
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.info("[JMS]: After getMBeanServerConnection() ");
    } catch (IOException ioex) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.error("[JMS]: " + ioex.getMessage());
      throw ioex;
    }
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: fine");
    return lMBSC;
  }
  
  /**
   * Return number of active consumers.
   * <p>
   * @param aDestinationName
   * @return int
   * @throws IOException
   * @throws JMSException
   * @throws JMException
   */
  public int getNumActiveConsumer( String aDestinationName ) 
  throws IOException, JMSException, JMException {   
    Date lSTime = new Date();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: inizio - " + lSTime.getTime());
    
    int lNumActiveConsumer = 0;
    
    try { 
      ObjectName lObjName = 
        new ObjectName(MQObjectName.DESTINATION_MANAGER_MONITOR_MBEAN_NAME);
 
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.info("[JMS]: lDestinationObjNames[] ");
      ObjectName lDestinationObjNames[] = 
              (ObjectName[])mMBSC.invoke(lObjName, 
                                       DestinationOperations.GET_DESTINATIONS, null, null);
      
      for (int i = 0; i < lDestinationObjNames.length; ++i)  {
        ObjectName lOneDestObjName = lDestinationObjNames[i];
        String lDestinationName = 
          (String)mMBSC.getAttribute(lOneDestObjName, DestinationAttributes.NAME);
      
        if( lDestinationName.equalsIgnoreCase(aDestinationName) ) {
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.info("[JMS]: lDestinationName: " + lDestinationName + 
              " - aDestinationName : " + aDestinationName );
          
            lNumActiveConsumer = 
              (Integer)mMBSC.getAttribute(lOneDestObjName, 
                                         DestinationAttributes.NUM_ACTIVE_CONSUMERS);
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.info("[JMS]: lNumActiveConsumer : " + lNumActiveConsumer);        
            break;
        }
      }
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.info("[JMS]: endfor ");
      
    } catch (IOException ex) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.error("[JMS]: " + ex.getMessage());
      throw ex;
    } catch (JMException ex) {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.error("[JMS]: " + ex.getMessage());
      throw ex;
    } finally {
    }
    
    Date lETime = new Date();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: fine - " + lETime.getTime() + "ms" + 
        " - Delta : " + (lETime.getTime() - lSTime.getTime()) + "ms" );
    
    return lNumActiveConsumer;
  } 

  /**
   * Close connection to JMS. 
   */
  protected void close() throws IOException {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: Close AdminConnection.");
    mJMXC.close();  
  }
  
}