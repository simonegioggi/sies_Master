package siap.siepe.attivita.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/**
* <p>Title: ActLoadInserisciRelazione</p>
* <p>Description: Classe Action per l'Upload di un documento di tipo Relazione.
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadInserisciRelazione extends ActLoadDettaglioAttivita
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    // Dati aggregati relativi al Fascicolo SIEPE
    //FascicoloSiepeEstesoModel lFascicoloEsteso = null;

    // Si richiama il Dettaglio
    String lPage = super.processRequest();

    // Imposta le Modalità.
    setRequestAttribute("modalita", "R");  // Imposta Modalità ( R -> Relazione ) .
    setRequestAttribute("Upload","NO");    // Imposta la non gestione dell'upload ( bottone e div )

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return lPage;  //restituisce la jsp di VIEW
  }

  /**
   * Metodo sovrascritto, ereditato dal padre, che esegue la gestione
   * del ritorno. Ovviamente, se commentato viene seguito dal padre il
   * setlink di Ritorno.
   * <p>
   * @throws Exception propaga errore di eccezione.
   */
  public void ritorno() throws Exception
  {
     gestioneRitorno();
  }
}