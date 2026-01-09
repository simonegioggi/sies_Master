package it.mig.sies.business;

import it.mig.sies.exception.CommunicationException;
import it.mig.sies.exception.PropertiesException;
import it.mig.sies.type.esecuzione_NEW.RequestData;
import it.mig.sies.type.esecuzione_NEW.ResponseData;
import it.mig.sies.type.esecuzione_NEW.IscriviProvvedimentoEsecuzioneNEW;
import it.mig.sies.type.esecuzione_NEW.IscriviProvvedimentoEsecuzioneNEW_Service;
import it.mig.sies.util.NscProperties;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.WebServiceClient;

import org.apache.log4j.Logger;

/**
 * SIES FASE 2 - Servizio di comunicazione con il webservice disponibile su NSC/SIES
 * per effettuare la richiesta e ricevere la risposta
 * 
 * @author Federico Paparoni
 */
public class CommunicationService {

	private RequestData requestData;
	
	private static final Logger logger=Logger.getLogger(CommunicationService.class);
	
	public CommunicationService(RequestData requestData) {

		this.requestData=requestData;
	}
	
	/**
	 * Metodo principale del servizio di comunicazione
	 * 
	 * @throws CommunicationException
	 */
	public ResponseData execute() throws CommunicationException {

		try {
			logger.info("Invio richiesta tramite webservice");
			
			IscriviProvvedimentoEsecuzioneNEW myService = setupWS();
	        JAXBContext jc = JAXBContext.newInstance("it.mig.sies.type.esecuzione_NEW");
	        
	        String wsrequest = marshal(jc);
	        
	        /**
	         * Chiamata al webservice di iscrizione provvedimento dell'esecuzione
	         * e salvataggio della risposta come stringa
	         */
	        String wsresponse = myService.iscriviProvvedimentoEsecuzioneNEW(wsrequest);
	        ResponseData responseData = unmarshal(jc, wsresponse);
	        
	        logger.info("Codice esito: "+responseData.getEsito().getCodice());
	        logger.info("Descrizione esito: "+responseData.getEsito().getDescrizione());
	        return responseData;
		} catch(Exception e) {
			logger.error(e.toString());
			throw new CommunicationException(e.getMessage());
		}
	}
	
	/**
	 * Effettua l'unmarshal dei dati di risposta del webservice
	 *
	 * @throws JAXBException
	 */
	private ResponseData unmarshal(JAXBContext jc,String wsresponse) throws JAXBException {

		Unmarshaller unmarshaller = jc.createUnmarshaller();
        ByteArrayInputStream bais = new ByteArrayInputStream(wsresponse.getBytes());
        ResponseData responseData = (ResponseData) unmarshaller.unmarshal(bais);
        return responseData;
	}
	
	/**
	 * Effettua il marshal dei dati di richiesta
	 * 
	 * @throws JAXBException
	 */
	private String marshal(JAXBContext jc) throws JAXBException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Marshaller marshaller = jc.createMarshaller();
        marshaller.marshal(requestData, baos);
        logger.info("Richiesta da trasferire: "+baos.toString());
        return baos.toString();
	}
	
	/**
	 * Istanzia l'interfaccia per la comunicazione con
	 * il webservice di NSC/SIES
	 * 
	 * @throws PropertiesException 
	 */
	private IscriviProvvedimentoEsecuzioneNEW setupWS() throws PropertiesException {

		// Recupero l'indirizzo dal file di properties
//		ApplicationProperties applicationProperties = ApplicationProperties.getIstance();
		NscProperties nscProperty = NscProperties.getInstance();
        String url = nscProperty.getProperty("nsc.address");
        logger.info("Indirizzo webservice: " + url);

        // NUOVA INFRASTRUTTURA: recupero del wsdl e lo passo al metodo "IscriviProvvedimentoEsecuzione_NEW_Service"
        URL wsdl = CommunicationService.class.getResource("esecuzione_NEW.wsdl");

        // Inizializzo la classe di JAXWS per l'utilizzo del webservice
        WebServiceClient ann = (WebServiceClient) IscriviProvvedimentoEsecuzioneNEW_Service.class.getAnnotation(WebServiceClient.class);
        IscriviProvvedimentoEsecuzioneNEW_Service service = new IscriviProvvedimentoEsecuzioneNEW_Service(/*new URL(url)*/wsdl, new QName(ann.targetNamespace(), ann.name()));
        // IscriviProvvedimentoEsecuzione_NEW_Service service = new IscriviProvvedimentoEsecuzione_NEW_Service();
        IscriviProvvedimentoEsecuzioneNEW myService = service.getIscriviProvvedimentoEsecuzioneNEW();
        // MEV 35253
        ((BindingProvider)myService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        // valore di ritorno
        return myService;
	}

}