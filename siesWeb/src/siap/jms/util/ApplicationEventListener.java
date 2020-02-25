package siap.jms.util;

import org.apache.log4j.Logger;

import siap.jms.connection.ConnectionPoolJMS;

import com.sun.messaging.jms.notification.Event;
import com.sun.messaging.jms.notification.EventListener;

import f3b.log.LogF3B;

public class ApplicationEventListener implements EventListener {

  // 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
  private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);
		
  public void onEvent ( Event aConnEvent ) {
    log(aConnEvent);
    if( aConnEvent.getEventCode().equalsIgnoreCase("E201") || 
        aConnEvent.getEventCode().equalsIgnoreCase("E401") ) {
      ConnectionPoolJMS.setAccessible(false);
    }else if( aConnEvent.getEventCode().equalsIgnoreCase("E301") ){
      ConnectionPoolJMS.setAccessible(true);
    }
  }  
  private void log ( Event aConnEvent ) {
    String lEventCode = aConnEvent.getEventCode();
    String lEventMessage = aConnEvent.getEventMessage();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: " + "eventCode : " + lEventCode + ";" 
        + "eventMessage : " + lEventMessage );
  }
}