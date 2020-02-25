package siap.siep.calcolopena.action;

/**
 * <p>Title: ActLoadAnnotazioniManualiCompAltroTitolo</p>
 * <p>Description: Azione Load della form di inserimento Computo Fungibilità (MC)</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadCalcolatrice extends ActionSiap implements ICostantiAnnotazioneManuale
{
  /**
   * Rideterminazione Pena --> Computo misura cautelare altro reato (fungibilità)
   * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws Exception
  {

//    setRequestAttribute("CausaleComputo", "" + lOption );

    String lPage=IWebConstants.ROOT_DIR+"/files/siap/siep/calcolopena/Calcolatrice.jsp";

    return lPage;
   }
}
