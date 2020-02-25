package siap.sico.soggettocertificato.action;


/**
* <p>Title: ActCancellaSoggettoCertificato</p>
* <p>Description: Classe Action per la cancellazione di SoggettoCertificato</p>
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

public class ActCancellaSoggettoCertificato extends ActionSiap implements ICostantiSoggettoCertificato
{
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
    BigDecimal lIdSoggettoCertificato     = getRequestBigDecimalParameter ( CAMPO_ID_SOGGETTO_CERTIFICATO) ;

    //========================================== 
    // Istanzia il model   
    //========================================== 
    SoggettoCertificatoModel lSogMod = new SoggettoCertificatoModel();

    lSogMod.setIdSoggettoCertificato     (lIdSoggettoCertificato);

    //====================================================== 
    // Recupera il Controller ed effettua la cancellazione 
    //====================================================== 
    ISoggettoCertificato lCtrl = SICOLookupRemote.getSoggettoCertificatoRemote();
    lCtrl.ExCancellaSoggettoCertificato(lSogMod);

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