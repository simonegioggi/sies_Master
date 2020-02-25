package siap.siep.certificatostatoesecuzione.action;


import java.io.ByteArrayOutputStream;

import siap.sico.web.ActionSiap;
import siap.siep.certificatostatoesecuzione.controller.ICertificatoStatoEsec;
import siap.siep.certificatostatoesecuzione.model.CertificatoStatoEsecModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActLoadDocumento</p>
 * <p>Description: Carica il Documento BLOB </p>
 * <p>Copyright: Copyright (c) 2002</p>
 */
public class ActLoadDocumento extends ActionSiap implements ICostantiCertificatoStatoEsec
{
  public String processRequest() throws Exception
  {
    CertificatoStatoEsecModel lCertSEModel = new CertificatoStatoEsecModel();
    lCertSEModel.setIdCertificatoStatoEsec(getRequestBigDecimalParameter( CAMPO_ID_CERTIFICATO_STATO_ESEC) );

    ICertificatoStatoEsec lCtrl = SIEPLookupRemote.getCertificatoStatoEsecRemote();
    ByteArrayOutputStream lReport = lCtrl.ExGetDocumento( lCertSEModel );	
    
    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    //return IWebConstants.PG_DOWNLOAD; // 20100409 - sostituita pernuova implementazione lib TIKA
    return IWebConstants.PG_DOWNLOAD_DOCUMENT;
   }
}
