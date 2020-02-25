package siap.sico.certificato_omonimi_nsc.action;


/**
* <p>Title: ActLoadModificaCertificatoOmonimiNsc</p>
* <p>Description: Classe Action per la load modifica di CertificatoOmonimiNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.certificato_omonimi_nsc.controller.ICertificatoOmonimiNsc;
//import siap.sico.certificato_omonimi_nsc.controller.ICertificatoOmonimiNsc;
import siap.sico.certificato_omonimi_nsc.model.CertificatoOmonimiNscModel;
//import per le combo
//import f3b.web.html.Option;
//import xxxx.decodifiche.controller.DecodificheManager;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadModificaCertificatoOmonimiNsc extends ActionSiap implements ICostantiCertificatoOmonimiNsc
{
 /*****************************************************************************
  * Azione di caricamento della pagina di Modifica dei dati. Si occupa anche 
  * di precaricare tutti i dati da visualizzare i tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    //==========================================
    // Recupera la key del record da modificare 
    //==========================================
    BigDecimal lIdCertificatoOmonimi = getRequestBigDecimalParameter ( CAMPO_ID_CERTIFICATO_OMONIMI) ;

    //========================================== 
    // Recupera i dati del record da modificare  
    //========================================== 
    ICertificatoOmonimiNsc lCtrl = SICOLookupRemote.getCertificatoOmonimiNscRemote();
    CertificatoOmonimiNscModel lCertificatoOmonimiNsc = lCtrl.ExRicercaCertificatoOmonimiNscById( lIdCertificatoOmonimi );
 
    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
    if (lCertificatoOmonimiNsc==null){ 
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
    setRequestAttribute("certificatoomoniminsc", lCertificatoOmonimiNsc);

    // Inserire Eventuali ComboBOX precaricando i dati del model
    // Option lOption = new Option( DecodificheManager.getInstance().get???());
    // setRequestAttribute("???", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");

    //==========================================================================
    // Restituisce la pagina di modifica.
    // n.b. è la stessa della pagina di Inserimento ma con modalità differente 
    //==========================================================================
    return PG_LOAD_INSERISCICERTIFICATOOMONIMINSC; 
  }
}