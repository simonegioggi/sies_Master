package siap.siepe.richiesta.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/*
import siap.sico.util.SICOLookupRemote;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;

import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
*/

/**
* <p>Title: ActLoadTrasferisciRichiesta</p>
* <p>Description: Classe Action per il caricamento dela form per il trasferimento Richiesta</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadTrasferisciRichiesta extends ActLoadDettaglioRichiesta
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug( this.getClass().getName() + ".processRequest(): inizio" );

    String lPage = super.processRequest();

    // Imposta Modalità.
    setRequestAttribute("modalita", "T");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( this.getClass().getName() + ".processRequest(): fine" );

    return lPage;  //restituisce la jsp di VIEW
  }
  /*
  public void ritorno() throws Exception
  {
     gestioneRitorno();
  }
  */
}