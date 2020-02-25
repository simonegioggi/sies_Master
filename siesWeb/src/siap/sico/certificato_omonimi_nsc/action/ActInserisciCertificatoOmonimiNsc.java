package siap.sico.certificato_omonimi_nsc.action;


/**
* <p>Title: ActInserisciCertificatoOmonimiNsc</p>
* <p>Description: Classe Action per l'inserimento di CertificatoOmonimiNsc</p>
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

public class ActInserisciCertificatoOmonimiNsc extends ActionSiap implements ICostantiCertificatoOmonimiNsc {

 /*****************************************************************************
  * Azione di Inserimento del CertificatoOmonimiNsc
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  ****************************************************************************/
  public String processRequest() throws F3BException {
    CertificatoOmonimiNscModel lCerMod = new CertificatoOmonimiNscModel();

    //========================================================================== 
    // Recupero i dati presenti in maschera 
    // n.b. eliminare o commentare i campi non presenti in maschera 
    //      es: chiave della tabella, date_ins, foreignkey... 
    //========================================================================== 
    lCerMod.setIdCertificatoOmonimi ( getRequestBigDecimalParameter ( CAMPO_ID_CERTIFICATO_OMONIMI) );
    lCerMod.setDataInserimento      ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );

    //=================================================== 
    // Recupera il controller ed effettua l'inserimento 
    //=================================================== 
    ICertificatoOmonimiNsc lCtrl = SICOLookupRemote.getCertificatoOmonimiNscRemote();
    CertificatoOmonimiNscModel lCerRetMod = new CertificatoOmonimiNscModel();
    lCerRetMod=lCtrl.ExInserisciCertificatoOmonimiNsc(lCerMod);

    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.certificato_omonimi_nsc.action.ActLoadDettaglioCertificatoOmonimiNsc";
    lPage += "&" + CAMPO_ID_CERTIFICATO_OMONIMI + "=" + lCerRetMod.getIdCertificatoOmonimi().toString();

    return lPage;
  }
}