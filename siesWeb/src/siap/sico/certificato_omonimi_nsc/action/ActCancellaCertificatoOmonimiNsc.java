package siap.sico.certificato_omonimi_nsc.action;


/**
* <p>Title: ActCancellaCertificatoOmonimiNsc</p>
* <p>Description: Classe Action per la cancellazione di CertificatoOmonimiNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.certificato_omonimi_nsc.controller.ICertificatoOmonimiNsc;
import siap.sico.certificato_omonimi_nsc.model.CertificatoOmonimiNscModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaCertificatoOmonimiNsc extends ActionSiap implements ICostantiCertificatoOmonimiNsc
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
    BigDecimal lIdCertificatoOmonimi = getRequestBigDecimalParameter ( CAMPO_ID_CERTIFICATO_OMONIMI) ;

    //========================================== 
    // Istanzia il model   
    //========================================== 
    CertificatoOmonimiNscModel lCerMod = new CertificatoOmonimiNscModel();

    lCerMod.setIdCertificatoOmonimi (lIdCertificatoOmonimi);

    //====================================================== 
    // Recupera il Controller ed effettua la cancellazione 
    //====================================================== 
    ICertificatoOmonimiNsc lCtrl = SICOLookupRemote.getCertificatoOmonimiNscRemote();
    lCtrl.ExCancellaCertificatoOmonimiNsc(lCerMod);

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