package siap.sius.depositodecreto.action;

import org.apache.log4j.Logger;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;
import f3b.log.LogF3B;

/**
 * ActLoadFSPModelliDecreto - Azione che permette di puntare da un determinato procedimento.
 * per poi proseguire verso la funzionalità di pertinenza. 
 */
public class ActLoadFSPModelliDecreto extends ActLoadRicercaFSPuntuale
implements ICostantiDepositoDecreto
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Generazione Modelli");
    setRequestAttribute("nextAction", "siap.sius.depositodecreto.action.ActRicercaFSPModelliDecreto");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): FINE");
    
    return lPage;
  }
}