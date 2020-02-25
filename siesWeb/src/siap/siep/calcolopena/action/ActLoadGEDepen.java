package siap.siep.calcolopena.action;

/**
 * <p>Title: ActLoadGEDepen</p>
 * <p>Description: Azione Load delle Decisioni del GE - Depenalizzazione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

public class ActLoadGEDepen extends ActLoadInserisciAnnotazioniManuali
{

  /**
   * Azione di caricamento della form d'inserimento della Depenalizzazione
   * @return Nome della pagina JSP di inserimento 
   * @throws Exception
   */
  public String processRequest() throws Exception
  {
    String lPageErr = loadRichiestaAnnotazioniManuali("0285"); //DEPENALIZZAZIONE

    if (lPageErr != null)
      return lPageErr;

    Option lOption  = new Option( DecodificheManager.getInstance().getTipoFonteReato() );
    setRequestAttribute("TipiFontiReato", "" + lOption );

    lOption  = new Option( DecodificheManager.getInstance().getSottonumerazione() );
    setRequestAttribute("TipiSottonumerazione", "" + lOption );

    setRequestAttribute("tipoBeneficio", "DEPEN");
    String lPage=IWebConstants.ROOT_DIR+"/files/siap/siep/calcolopena/LoadGEDepen.jsp";

    return lPage;
  }
}