package siap.siep.calcolopena.action;

/**
 * <p>Title: ActLoadGEIncost</p>
 * <p>Description: Azione Load delle Decisioni del GE - Incostituzionalità</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import f3b.web.IWebConstants;

public class ActLoadGEIncost extends ActLoadInserisciAnnotazioniManuali
{
  /**
   * Azione di caricamento della form d'inserimento della Depenalizzazione
   * @return Nome della pagina JSP di inserimento 
   * @throws Exception
   */
  public String processRequest() throws Exception
  {
    String lPageErr = loadRichiestaAnnotazioniManuali("0286"); //INCOSTITUZIONALITA'

    if (lPageErr != null)
      return lPageErr;

    setRequestAttribute("tipoBeneficio", "INCOST");
    String lPage=IWebConstants.ROOT_DIR+"/files/siap/siep/calcolopena/LoadGEIncost.jsp";

    return lPage;
   }
}
