package f3b.log;

import java.util.Map;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Filtro per escludere dalla loggature i record generati dagli utenti DNA che inizano con J
 * 
 * @since MEV_2024-DNA
 */
public class SIESLogDNAFilter extends Filter<ILoggingEvent> {
  @Override
  public FilterReply decide(ILoggingEvent event) {
    
    Map <String, String> lMdcEventMap = event.getMDCPropertyMap();
    String utente = "";
    for (Map.Entry<String, String> entry : lMdcEventMap.entrySet()) {
      if ("utente".equals(entry.getKey()))
        utente = entry.getValue();
    }
    
    if (utente.startsWith("J")) {
        return FilterReply.DENY; // Si escludono le loggature degli utenti DNA
    } else {
        return FilterReply.NEUTRAL;
    }

  }
}
