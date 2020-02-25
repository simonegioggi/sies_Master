package siap.siep.annotazionemanuale.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
/**
* <p>Title: ActLoadStampeAnnotazioni</p>
* <p>Description: Classe Action per l'inserimento della nuova Pena validata</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/import f3b.util.F3BException;

import f3b.web.IWebConstants;

public class ActLoadStampeAnnotazioniRichieste extends ActLoadStampeAnnotazioni
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
 * Azione di visualizzazione stampe per le annotazioni manuali
 * @return Nome della pagina JSP da visualizzare
 * al termine dell'elaborazione
 * @throws F3BException
 */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    String lPage = loadStampe();

    if (lPage == null)
       lPage = IWebConstants.ROOT_DIR +"/files/siap/siep/calcolopena/VediCalcoloPenaValidataRichiestaGE.jsp";

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Pagina di ritorno ->" + lPage);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );

    return lPage;
  }
}