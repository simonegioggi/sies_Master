package siap.siep.competenza.action;


/**
* <p>Title: ActLoadDettaglioCompetenza</p>
* <p>Description: Classe Action per la load dettaglio di Competenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sico.web.ISICOCostantiWeb;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

public class ActLoadDettaglioCompetenza extends ActionSiap implements ICostantiCompetenza
{
 /*****************************************************************************
  * Azione di caricamento della pagina di Dettaglio dei dati. 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    //==========================================
    // Recupera la key del record da Visualizzare 
    //==========================================
    BigDecimal lIdCompetenza                = getRequestBigDecimalParameter ( CAMPO_ID_COMPETENZA) ;

    //========================================== 
    // Recupera i dati del record da modificare  
    //========================================== 
    ICompetenza lCtrl = SIEPLookupRemote.getCompetenzaRemote();
    CompetenzaModel lComMod = lCtrl.ExRicercaCompetenzaById( lIdCompetenza);

    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
    if (lComMod==null){ 
      setRequestAttribute(ISICOCostantiWeb.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
      // Specificare eventualmente la jump page dove verrà ridirezionata la 
      // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
      // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
      // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
      //      della root_dir es /siap/frame.htm  
    setRequestAttribute(ISICOCostantiWeb.GOTO_PAGE, ISICOCostantiWeb.ROOT_DIR); 
      return ISICOCostantiWeb.PG_MESSAGE;
    }

    //==================================================== 
    // Passa il Model alla componente di visualizzazione  
    //==================================================== 
    setRequestAttribute("competenza", lComMod);

    //=========================================================
    // Restituisce la pagina di Visualizzazione del Dettaglio.
    //=========================================================
    // Imposta Modalità.
    setRequestAttribute("modalita", "D");

    return PG_LOAD_DETTAGLIOCOMPETENZA;
  }
}