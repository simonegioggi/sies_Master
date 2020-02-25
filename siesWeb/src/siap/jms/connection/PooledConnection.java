package siap.jms.connection;

import javax.jms.JMSException;
import javax.jms.QueueConnection;


/**
 * <p>Title: PooledConnection </p>
 * <p>Description: Classe che incapsula gli oggetti da mettere all'interno del Pool</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 */
public class PooledConnection
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  // JMS Connection
  private QueueConnection mConnection = null;
  // flag boolean usato per determinare se la connessione è in uso
  private boolean mInuse = false;

  
  //
  private long mLastUsed = 0L;  // 
  private String mConsumerClassName = null;  // nome della classe che ha chiesto la connessione 

  
  
  
  // Construttore che prende la JMS Connection
  // e la memorizza nel suo attributo.
  public PooledConnection(QueueConnection value)
  {
    if (value != null)
    {
      mConnection = value;
//      try{
//        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//        siesLogger.debug("ClientID: "+mConnection.getClientID());
//      }catch(Exception e){}
    }
  }

  public QueueConnection getConnection()
  {
    return mConnection;
  }

  public void setInUse(boolean value)
  {
    mInuse = value;
  }

  public boolean inUse()
  {
    return mInuse;
  }

  /**
   * Imposta il Timestamp in cui questa connection è stata data al consumer 
   * @param timeUsed
   */
  public void setLastUsed (long timeUsed)
  {
    mLastUsed = timeUsed;
  } 
  
  public long  getLastUsed ()
  {
    return mLastUsed;
  } 

  /**
   * Imposta il nome della classe e metodo del consumer che ha richiesto questa
   * connessione
   * @param aClassName
   */
  public void setConsumerClassName(String aClassName){
    mConsumerClassName = aClassName;
  }
  
  public String getConsumerClassName(){
    return mConsumerClassName;
  }
  
  
  public void close()
  {
    try
    {
      mConnection.close();
    }
    catch (JMSException sqle)
    {

      System.err.println(sqle.getMessage());
    }
  }
}