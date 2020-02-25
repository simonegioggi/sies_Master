package siap.sius.richiestaatti.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/**
 * <p>Title: ActLoadRichiestaSanzSostRisottoposizioneSospensione </p>
 * <p>Description: Classe di Azione responsabile della composizione dei
 * dati per le combobox e ritorna la chiamata alla corrispondente JSP.
 * La classe estende la classe ActLoadRichiestaSanzSostDocumentiIstruttori
 * per il riuso di parti comuni.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadRichiestaSanzSostRisottoposizioneSospensione 
extends ActLoadRichiestaSanzSostDocumentiIstruttori
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Metodo processRequest che prepara i dati necessari per la
   * composizione della form, e ritorna come parametro la relativa
   * JSP compresiva di path.
   * <p>
   * @return pagina JSP da caricare
   * @throws Exception propaga qualunque errore di eccezione.
   */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");    
    // Imposta Azione e Funzione.
    setRequestAttribute("Azione","siap.sius.richiestaatti.action.ActInserisciSanzSostRisottoposizioneSospensione");
    setRequestAttribute("Funzione", "Risottoposizione a seguito di Sospensione");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    return super.processRequest(); // Restituisce la jsp di VIEW 
  }
}