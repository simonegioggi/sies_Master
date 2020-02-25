package siap.sico.webservice.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

public class ActLoadCertificatoCasellarioGiudiziale extends ActionSiap
{
  public String processRequest() throws Exception
  {
      BigDecimal lIdFascicolo = getRequestBigDecimalParameter("IDFascicolo");
      String tipoFascicolo = getRequestStringParameter("TipoFascicolo");
      ByteArrayOutputStream lReport = null;
    		  
      if(tipoFascicolo != null && tipoFascicolo.equals("SIEP")){
    	  IFascicoloSiep lCtrlFascSiep = SIEPLookupRemote.getFascicoloSiepRemote();
    	  lReport = lCtrlFascSiep.ExGetCertificatoPenale(lIdFascicolo);
      } else {
    	  IFascicoloSius lCtrlFascSius = SIUSLookupRemote.getFascicoloSiusRemote();
    	  lReport = lCtrlFascSius.ExGetCertificatoPenale(lIdFascicolo);
      }

      //Prepara la pagina di destinazione
      setRequestAttribute("report", lReport);
      setRequestAttribute("idFascicolo", lIdFascicolo);
      return IWebConstants.PG_DOWNLOAD_PDF;      // Per VISUALIZZAZIONE file PDF

  }
}
