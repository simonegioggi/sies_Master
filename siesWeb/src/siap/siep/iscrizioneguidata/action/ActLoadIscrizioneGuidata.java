package siap.siep.iscrizioneguidata.action;

/**
 * <p>Title: ActLoadIscrizioneGuidata</p>
 * <p>Description: Azione Load dell'Inserisci Soggetto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadIscrizioneGuidata extends ActionSiap
{
  /**
   * Azione di caricamento della form per l'iscrizione guidata di un fascicolo
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
     String lPage = null;

     this.setRequestAttribute("lTipoFunzione","ISCRGUIDATA");

     setSessionAttribute("fascicolo",null);
     setSessionAttribute("soggetto",null);
     setSessionAttribute("sentenza",null);

     //if(this.isSessionAttributeNullObj("soggetto"))
     // {
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.soggetto.action.ActLoadInserisciSoggetto";
     // }else
     // {
     //   SoggettoModel lSogMod = (SoggettoModel) getSessionAttribute("soggetto");

     //   lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.soggetto.action.ActLoadModificaSoggetto&"
     //     + ICostantiSoggetto.CAMPO_ID_SOGGETTO+"="+lSogMod.getIdSoggetto();
     // }

    return lPage;
  }
}
