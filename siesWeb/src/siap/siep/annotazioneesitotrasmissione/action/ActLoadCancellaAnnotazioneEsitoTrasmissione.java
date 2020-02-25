package siap.siep.annotazioneesitotrasmissione.action;


/**
* <p>Title: ActLoadCancellaAnnotazioneEsitoTrasmissione</p>
* <p>Description: Classe Action per la load cancella di AnnotazioneEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.annotazioneesitotrasmissione.controller.IAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadCancellaAnnotazioneEsitoTrasmissione extends ActionSiap implements ICostantiAnnotazioneEsitoTrasmissione
{
  Logger logger = Logger.getLogger("actionLogger");
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
    BigDecimal lIdEsitoTrasmissione       = getRequestBigDecimalParameter ( CAMPO_ID_ESITO_TRASMISSIONE) ;

    //========================================== 
    // Recupera i dati del record da Cancellare  
    //========================================== 
    IAnnotazioneEsitoTrasmissione lCtrl = SIEPLookupRemote.getAnnotazioneEsitoTrasmissioneRemote();
    AnnotazioneEsitoTrasmissioneModel lAnnMod = lCtrl.ExRicercaAnnotazioneEsitoTrasmissioneById( lIdEsitoTrasmissione);

    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
    if (lAnnMod==null){ 
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
    setRequestAttribute("annotazioneesitotrasmissione", lAnnMod);

    //=========================================================
    // Restituisce la pagina di Visualizzazione del Dettaglio.
    //=========================================================
    // Imposta Modalità.
    setRequestAttribute("modalita", "C");
     
    return PG_LOAD_CANCELLAANNOTAZIONEESITOTRASMISSIONE;
  }
}