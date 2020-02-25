package siap.siep.annotazionemanuale.action;

/**
* <p>Title: ActLoadStampeAnnotazioniBenefici</p>
* <p>Description: Classe Action per la Stampa </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Eunics</p>
* @version 1.0
*/

import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadStampeAnnotazioniBenefici extends ActLoadStampeAnnotazioni
{
/**
 * Azione di visualizzazione stampe per le annotazioni manuali
 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
 * @throws F3BException
 */
  public String processRequest() throws Exception
  {
    String lPage = loadStampe();

    if (lPage != null)
      return lPage;

    return IWebConstants.ROOT_DIR +"/files/siap/siep/calcolopena/VediCalcoloPenaValidataAnnotazioni.jsp";
  }
}