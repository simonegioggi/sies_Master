package siap.sico.soggettocertificato.action;


/**
* <p>Title: ActLoadDettaglioSoggettoCertificato</p>
* <p>Description: Classe Action per la load dettaglio di SoggettoCertificato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.soggettocertificato.controller.ISoggettoCertificato;
import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadDettaglioSoggettoCertificato extends ActionSiap implements ICostantiSoggettoCertificato
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
    BigDecimal lIdSoggettoCertificato     = getRequestBigDecimalParameter ( CAMPO_ID_SOGGETTO_CERTIFICATO) ;

    //========================================== 
    // Recupera i dati del record da modificare  
    //========================================== 
    ISoggettoCertificato lCtrl = SICOLookupRemote.getSoggettoCertificatoRemote();
    SoggettoCertificatoModel lSogMod = lCtrl.ExRicercaSoggettoCertificatoById( lIdSoggettoCertificato);

    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
    if (lSogMod==null){ 
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
      // Specificare eventualmente la jump page dove verrà ridirezionata la 
      // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
      // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
      // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
      //      della root_dir es /siap/frame.htm  
    setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 
      return IWebConstants.PG_MESSAGE;
    }

    //==================================================== 
    // Passa il Model alla componente di visualizzazione  
    //==================================================== 
    setRequestAttribute("soggettocertificato", lSogMod);

    //=========================================================
    // Restituisce la pagina di Visualizzazione del Dettaglio.
    //=========================================================
    // Imposta Modalità.
    setRequestAttribute("modalita", "D");

    return PG_LOAD_DETTAGLIOSOGGETTOCERTIFICATO;
  }
}