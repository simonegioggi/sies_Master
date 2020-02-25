package siap.sico.certificato_omonimi_nsc.action;


/**
* <p>Title: ActLoadDettaglioCertificatoOmonimiNsc</p>
* <p>Description: Classe Action per la load dettaglio di CertificatoOmonimiNsc</p>
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

public class ActLoadDettaglioCertificatoOmonimiNsc extends ActionSiap implements ICostantiCertificatoOmonimiNsc
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
    BigDecimal lIdCertificatoOmonimi = getRequestBigDecimalParameter ( CAMPO_ID_CERTIFICATO_OMONIMI) ;

    //========================================== 
    // Recupera i dati del record da modificare  
    //========================================== 
    ICertificatoOmonimiNsc lCtrl = SICOLookupRemote.getCertificatoOmonimiNscRemote();
    CertificatoOmonimiNscModel lCerMod = lCtrl.ExRicercaCertificatoOmonimiNscById( lIdCertificatoOmonimi);

    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
    if (lCerMod==null){ 
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
    setRequestAttribute("certificatoomoniminsc", lCerMod);

    //=========================================================
    // Restituisce la pagina di Visualizzazione del Dettaglio.
    //=========================================================
    // Imposta Modalità.
    setRequestAttribute("modalita", "D");

    return PG_LOAD_DETTAGLIOCERTIFICATOOMONIMINSC;
  }
}