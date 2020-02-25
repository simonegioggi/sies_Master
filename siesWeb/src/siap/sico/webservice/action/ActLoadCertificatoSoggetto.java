package siap.sico.webservice.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.xmlbeans.impl.util.Base64;

import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggettocertificato.controller.ISoggettoCertificato;
import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.web.IWebConstants;

public class ActLoadCertificatoSoggetto extends ActionSiap implements ICostantiSoggetto
{
  public String processRequest() throws Exception
  {
     // BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO);
      BigDecimal lId = getRequestBigDecimalParameter("IDSoggetto");
      SoggettoCertificatoModel lSogCert = new SoggettoCertificatoModel();
      lSogCert.setSogIdSoggetto(lId);
      
      ISoggettoCertificato lCtrlSoggCert = SICOLookupRemote.getSoggettoCertificatoRemote();
      ByteArrayOutputStream lReport = lCtrlSoggCert.ExGetDocumento(lSogCert);

      byte[] certificato = Base64.decode(lReport.toByteArray());  // decodifica  in Base 64
      ByteArrayOutputStream certificato_ByteArray = new ByteArrayOutputStream();     // Trasformiamo in ByteArray
      certificato_ByteArray.write(certificato);
      
      //Prepara la pagina di destinazione
      setRequestAttribute("report", certificato_ByteArray);
      return IWebConstants.PG_DOWNLOAD_PDF;      // Per VISUALIZZAZIONE file PDF

  }
}
