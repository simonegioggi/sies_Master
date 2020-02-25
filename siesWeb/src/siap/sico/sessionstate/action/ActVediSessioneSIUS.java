package siap.sico.sessionstate.action;
import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
/**
 * <p>Title: ActVediSessioneSIUS</p>
 * <p>Description: Azione di visualizzazione degli oggetti in sessione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
import f3b.util.F3BException;

public class ActVediSessioneSIUS extends ActionSiap
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Azione di Visualizzazione dati Sessione
   * <p>
   * @return Nome della pagina JSP con la situazione corrente
   *
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
     String lPage = "/jsp/files/siap/sius/sessione/VediStatoSessione.jsp";
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug( "Memoria java totale : " + Runtime.getRuntime().maxMemory() + " byte " );
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug( "Memoria java free : " + Runtime.getRuntime().freeMemory() + " byte " );
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug( "Attivazione garbage collection" );
     // Si tenta di liberare memoria
     System.runFinalization();
     System.gc();

     double lMemFree = Runtime.getRuntime().freeMemory();
     double lMemTotal = Runtime.getRuntime().maxMemory();

//     this.setRequestAttribute("MemoriaLibera", "" + lMemFree);
//     this.setRequestAttribute("MemoriaTotale", "" + lMemTotal);
     long  lMemInUso = (long)(( (lMemTotal - lMemFree)/lMemTotal) * 100.);
     this.setRequestAttribute("MemoriaUtilizzata", "" + lMemInUso);
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.info( "Memoria java totale : " + lMemTotal + " byte " );
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.info( "Memoria java free : " + lMemFree + " byte " );

     return lPage;
  }
}