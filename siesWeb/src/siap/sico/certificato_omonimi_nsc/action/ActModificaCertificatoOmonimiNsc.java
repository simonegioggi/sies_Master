package siap.sico.certificato_omonimi_nsc.action;


/**
* <p>Title: ActModificaCertificatoOmonimiNsc</p>
* <p>Description: Classe Action per la modifica di CertificatoOmonimiNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.certificato_omonimi_nsc.controller.ICertificatoOmonimiNsc;
import siap.sico.certificato_omonimi_nsc.model.CertificatoOmonimiNscModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import f3b.web.RedirectTo;

public class ActModificaCertificatoOmonimiNsc extends ActionSiap implements ICostantiCertificatoOmonimiNsc
{
 /*****************************************************************************
  * Azione di Modifica del CertificatoOmonimiNsc
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {

    //===================================================== 
    // Riempie il model con i campi recuperati dalla form 
    // n.b. per i campi del model non valorizzati, i corrispondenti  
    //      campi della tabella verranno impostati a null  
    // Il campo chiave è obbligatorio perchè utilizzato nelle clausola where 
    // per individuare il record da aggiornare 
    //===================================================== 
    CertificatoOmonimiNscModel lCerMod = new CertificatoOmonimiNscModel ();

    lCerMod.setIdCertificatoOmonimi ( getRequestBigDecimalParameter ( CAMPO_ID_CERTIFICATO_OMONIMI) );
    lCerMod.setDataInserimento      ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO) );

    //=================================================== 
    // Recupera il controller ed effettua la modifica 
    //=================================================== 
    ICertificatoOmonimiNsc lCtrl = SICOLookupRemote.getCertificatoOmonimiNscRemote();
    lCtrl.ExModificaCertificatoOmonimiNsc(lCerMod);

    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.certificato_omonimi_nsc.action.ActLoadDettaglioCertificatoOmonimiNsc";
    lPage += "&" + CAMPO_ID_CERTIFICATO_OMONIMI + "=" + lCerMod.getIdCertificatoOmonimi().toString();

    return lPage;
  }
}