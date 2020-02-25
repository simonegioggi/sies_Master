package siap.sico.webservice.action;

import java.io.ByteArrayOutputStream;

import org.apache.xmlbeans.impl.util.Base64;

import siap.sico.certificato_omonimi_nsc.controller.ICertificatoOmonimiNsc;
import siap.sico.certificato_omonimi_nsc.model.CertificatoOmonimiNscModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.web.IWebConstants;

public class ActNscToSiesLoadCertificato extends ActionSiap 
{

    public String processRequest() throws Exception
    {
        
        CertificatoOmonimiNscModel lCertMod = new CertificatoOmonimiNscModel();
        lCertMod.setIdCertificatoOmonimi(getRequestBigDecimalParameter("IDCertificato"));

        ICertificatoOmonimiNsc lCtrlCertOmonimiNsc = SICOLookupRemote.getCertificatoOmonimiNscRemote();
        ByteArrayOutputStream lReport = lCtrlCertOmonimiNsc.ExGetDocumento( lCertMod );
       
        byte[] certificato = Base64.decode(lReport.toByteArray());  // decodifica  in Base 64
        ByteArrayOutputStream certificato_ByteArray = new ByteArrayOutputStream();     // Ritrasformiamo in ByteArray
        certificato_ByteArray.write(certificato);
        
        //Prepara la pagina di destinazione
        setRequestAttribute("report", certificato_ByteArray);
       
        return IWebConstants.PG_DOWNLOAD_PDF;      // Per VISUALIZZAZIONE file PDF
  
    }
}
