package siap.siep.certificatostatoesecuzione.action;


/**
* <p>Title: ActCancellaCertificatoStatoEsec</p>
* <p>Description: Classe Action per la cancellazione di CertificatoStatoEsec</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.certificatostatoesecuzione.controller.ICertificatoStatoEsec;
import siap.siep.certificatostatoesecuzione.model.CertificatoStatoEsecModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActCancellaCertificatoStatoEsec extends ActionSiap implements ICostantiCertificatoStatoEsec
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
    BigDecimal lIdCertificatoStatoEsec    = getRequestBigDecimalParameter (CAMPO_ID_CERTIFICATO_STATO_ESEC) ;

    //========================================== 
    // Istanzia il model   
    //========================================== 
    CertificatoStatoEsecModel lCerMod = new CertificatoStatoEsecModel();

    lCerMod.setIdCertificatoStatoEsec    (lIdCertificatoStatoEsec);

    //====================================================== 
    // Recupera il Controller ed effettua la cancellazione 
    //====================================================== 
    ICertificatoStatoEsec lCtrl = SIEPLookupRemote.getCertificatoStatoEsecRemote();
    lCtrl.ExCancellaCertificatoStatoEsec(lCerMod);

    //===========================================================
    // Restituisce la pagina di Conferma avvenuta Cancellazione. 
    //===========================================================
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Cancellazione del Certificato di Esecuzione effettuata");
    // Specificare eventualmente la jump page dove verrà ridirezionata la 
    // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
    // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
    // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
    //      della root_dir es /null/frame.htm
     
    RedirectTo lRedirigi = new RedirectTo();
	lRedirigi.setPage(IWebConstants.PG_MAIN);
	lRedirigi.setAction("siap.siep.certificatostatoesecuzione.action.ActLoadCertificatoEsec");
	setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);	
    
    //setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 
    setRequestAttribute(IWebConstants.GOTO_PAGE, ""+lRedirigi);

    return IWebConstants.PG_MESSAGE;

  }
}