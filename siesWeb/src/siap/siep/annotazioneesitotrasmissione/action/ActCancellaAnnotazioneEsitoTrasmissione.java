package siap.siep.annotazioneesitotrasmissione.action;


/**
* <p>Title: ActCancellaAnnotazioneEsitoTrasmissione</p>
* <p>Description: Classe Action per la cancellazione di AnnotazioneEsitoTrasmissione</p>
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

public class ActCancellaAnnotazioneEsitoTrasmissione extends ActionSiap implements ICostantiAnnotazioneEsitoTrasmissione
{
  Logger logger = Logger.getLogger("actionLogger");
 /*****************************************************************************
  * Azione per la cancellazione dei dati. 
  * 
  * @return PG_MESSAGE di avvenuta cancellazione
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    //==========================================
    // Recupera la key del record da Cancellare 
    //==========================================
    BigDecimal lIdEsitoTrasmissione       = getRequestBigDecimalParameter ( CAMPO_ID_ESITO_TRASMISSIONE) ;

    //========================================== 
    // Istanzia il model   
    //========================================== 
    AnnotazioneEsitoTrasmissioneModel lAnnMod = new AnnotazioneEsitoTrasmissioneModel();

    lAnnMod.setIdEsitoTrasmissione       (lIdEsitoTrasmissione);

    //====================================================== 
    // Recupera il Controller ed effettua la cancellazione 
    //====================================================== 
    IAnnotazioneEsitoTrasmissione lCtrl = SIEPLookupRemote.getAnnotazioneEsitoTrasmissioneRemote();
    lCtrl.ExCancellaAnnotazioneEsitoTrasmissione(lAnnMod);

    //===========================================================
    // Restituisce la pagina di Conferma avvenuta Cancellazione. 
    //===========================================================
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Cancellazione effettuata");
    // Specificare eventualmente la jump page dove verrà ridirezionata la 
    // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
    // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
    // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
    //      della root_dir es /siap/frame.htm  
    setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 

    return IWebConstants.PG_MESSAGE;

  }
}