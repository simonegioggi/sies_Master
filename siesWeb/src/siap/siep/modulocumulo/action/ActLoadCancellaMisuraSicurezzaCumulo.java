package siap.siep.modulocumulo.action;


/**
* <p>Title: ActLoadCancellaMisuraSicurezzaCumulo</p>
* <p>Description: Classe Action per la load cancella di MisuraSicurezzaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Classe generata automaticamente del framework, attualmente non utilizzata 
 * dall'applicazione.
 * Codice non verificato.
 * 
 * @deprecated
 * @author 
 */
public class ActLoadCancellaMisuraSicurezzaCumulo extends ActionSiap implements ICostantiMisuraSicurezzaCumulo
{
 /*****************************************************************************
  * Azione di caricamento della pagina di Cancellazione dei dati. 
  * Viene caricata la stessa pagina di Dettaglio con il tasto cancella 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    //==========================================
    // Recupera la key del record da Visualizzare 
    //==========================================
    BigDecimal lIdMisuraSicurezzaCumulo   = getRequestBigDecimalParameter ( CAMPO_ID_MISURA_SICUREZZA_CUMULO) ;

    //========================================== 
    // Recupera i dati del record da Cancellare  
    //========================================== 
    IMisuraSicurezzaCumulo lCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
    MisuraSicurezzaCumuloModel lMisMod = lCtrl.ExRicercaMisuraSicurezzaCumuloById( lIdMisuraSicurezzaCumulo);

    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
    if (lMisMod==null){ 
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
      // Specificare eventualmente la jump page dove verrà ridirezionata la 
      // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
      // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
      // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
      //      della root_dir es /agost-tomcat/frame.htm  
    setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 
      return IWebConstants.PG_MESSAGE;
    }

    //==================================================== 
    // Passa il Model alla componente di visualizzazione  
    //==================================================== 
    setRequestAttribute("misurasicurezzacumulo", lMisMod);

    //=========================================================
    // Restituisce la pagina di Visualizzazione del Dettaglio.
    //=========================================================
    // Imposta Modalità.
    setRequestAttribute("modalita", "C");
     
    return PG_LOAD_CANCELLAMISURASICUREZZACUMULO;
  }
}