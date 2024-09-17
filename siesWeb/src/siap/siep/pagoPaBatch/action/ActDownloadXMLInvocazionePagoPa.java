package siap.siep.pagoPaBatch.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPaBatch.controller.IInvocazionePagopa;
import siap.siep.pagoPaBatch.model.InvocazionePagopaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActDownloadXMLInvocazionePagoPa extends ActionSiap implements ICostantiBatchPagoPa {
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	public String processRequest() throws Exception {
	    siesLogger.debug("ActDownloadXMLInvocazionePagoPa...");	    
	    
	    BigDecimal idInvocazione = getRequestBigDecimalParameter(ICostantiInvocazionePagopa.CAMPO_ID_INVOCAZIONE_PAGOPA);
	    String tipoXml = getRequestStringParameter(ICostantiInvocazionePagopa.CAMPO_TIPO_XML);
	    
	    // recupera l'xml
	    IInvocazionePagopa lCtrlInvocazione = SIEPLookupRemote.getInvocazionePagopaRemote();
	    InvocazionePagopaModel lInvocazioneModel = lCtrlInvocazione.ExRicercaInvocazioneById(idInvocazione);
	    
	    String xmlDownload = null;
	    String nomeFile = "";
	    if (tipoXml.equals(ICostantiInvocazionePagopa.CAMPO_XML_RICHIESTA)) {
	    	xmlDownload = lInvocazioneModel.getXmlRichiesta();
	    	nomeFile = "Richiesta.xml";
	    }
	    else if (tipoXml.equals(ICostantiInvocazionePagopa.CAMPO_XML_RISPOSTA)) {
	      // Ticket#202405210111 - 20240521012 - Si sposta l'xml di risposta su un campo clob causa dimensioni > 4000
	    	//xmlDownload = lInvocazioneModel.getXmlRisposta();
	    	xmlDownload = lInvocazioneModel.getXmlRispostaClob();
	    	// Ticket#202405210111 - 20240521012 -
	    	nomeFile = "Risposta.xml";
	    }
	    
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] array = xmlDownload.getBytes();
	    out.write(array);
	    
	    setRequestAttribute("report", out);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);
		setRequestAttribute("nomefile", nomeFile);
	    
		return "/jsp/files/DownloadXML.jsp";
		
	    //return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}
}
