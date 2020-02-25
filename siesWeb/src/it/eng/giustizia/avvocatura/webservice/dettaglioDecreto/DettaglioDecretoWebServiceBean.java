/**
 * MEV AVVOCATURA
 */
package it.eng.giustizia.avvocatura.webservice.dettaglioDecreto;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.action.DettaglioDecretoSiusAction;
import it.eng.giustizia.avvocatura.util.AvvocaturaProperties;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DATIINPUTDETTAGLIO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.OUTPUTDETTAGLIODECRETO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

/**
 * @author Gioggi
 *
 */
public class DettaglioDecretoWebServiceBean {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public String dettaglioDecreto(String datiInputDecreto) throws Exception {

		// info per il log
		avvocaturaLogger.info("Classe DettaglioDecretoWebServiceBean, Metodo dettaglioDecreto");

		// recupero dati di input ed elaborazione di quelli di output
		DATIINPUTDETTAGLIO did = null;
		OUTPUTDETTAGLIODECRETO odd = null;
		DettaglioDecretoSiusAction ddsa = new DettaglioDecretoSiusAction();
		String path = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_PATH.concat("dettagliodecreto"));
		JAXBContext jc = JAXBContext.newInstance(path);
//		SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
//		InputStream inputStream = DettaglioDecretoWebServiceBean.class
//				.getResourceAsStream("/dettaglio_decreto.xsd");
//		Source source = new StreamSource(inputStream);
//		Schema schema = sf.newSchema(source);
//		ValidationHandler vh = null;

		try {
			// deserializzo il documento XML in oggetti Java
			Unmarshaller unmarshaller = jc.createUnmarshaller();
//			unmarshaller.setSchema(schema);
//			vh = new ValidationHandler();
//			unmarshaller.setEventHandler(vh);
			ByteArrayInputStream bais = new ByteArrayInputStream(datiInputDecreto.getBytes());
			did = (DATIINPUTDETTAGLIO) unmarshaller.unmarshal(bais);

			// controllo se ci sono eventi di errore in ingresso
			String[] errori = new String[2];
			errori = controllaValiditaDatiInput(did, errori);
//			if (vh.hasEvents()) {
//				avvocaturaLogger.error("Errore di validazione in ingresso: " + vh.formatEvents());
//			}
			if ("true".equals(errori[0])) {
				// inizializzo l'oggetto di tipo "OUTPUTDETTAGLIODECRETO"
				odd = new OUTPUTDETTAGLIODECRETO();
				// imposto l'ERRORE
				odd.setERRORE(Mapper.mapErroreDecreto(errori[1], ""));
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				marshaller.marshal(odd, baos);
				return baos.toString();
			} else {
				// info per il log
				avvocaturaLogger.info("Nessun errore in fase di validazione");
				// recupero i dati per elaborare la risposta al client
				odd = ddsa.dettaglioDecreto(did.getTipoUfficio(), did.getCodiTipoProvvedimento(),
						did.getIdEvento(), did.getDATIAVVISO());
			}
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error("Errore generico: " + e.getMessage(), e);
			// stampa dell'errore
			e.printStackTrace();
			odd = new OUTPUTDETTAGLIODECRETO();
			// imposto l'ERRORE
			odd.setERRORE(Mapper.mapErroreDecreto("003", e.getMessage()));
		}

		// marshall delle stringhe
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// serializzo gli oggetti Java in XML
		marshaller.marshal(odd, baos);

		// eseguo la "pulizia" dello stream
		baos.flush();
		// valore di ritorno
		return baos.toString();
	}

	/**
	 * Metodo di controllo validità dati input
	 * 
	 * @param dpi
	 * @param errori
	 * @return
	 */
	private String[] controllaValiditaDatiInput(DATIINPUTDETTAGLIO did, String[] errori) {

		// info per il log
		avvocaturaLogger.info("Classe DettaglioDecretoWebServiceBean, Metodo controllaValiditaDatiInput");

		if (!PropertyUtil.isPresent(did.getTipoUfficio())) {
			errori[0] = "true";
			errori[1] = "020";
		} else if (!PropertyUtil.isPresent(did.getCodiTipoProvvedimento())) {
			errori[0] = "true";
			errori[1] = "021";
		} else if (!PropertyUtil.isPresent(did.getIdEvento())) {
			errori[0] = "true";
			errori[1] = "022";
		} else if (PropertyUtil.isPresent(did.getDATIAVVISO())) {
			// dati avviso non obbligatori ma se presenti i 2 dati sono necessari
			if (!PropertyUtil.isPresent(did.getDATIAVVISO().getIdAvviso())) {
				errori[0] = "true";
				errori[1] = "023";
			} else if (!PropertyUtil.isPresent(did.getDATIAVVISO().getCodiceFiscaleAvvocato())) {
				errori[0] = "true";
				errori[1] = "024";
			}
		} else {
			errori[0] = "false";
			errori[1] = "VALIDAZIONE DATI INPUT OK";
		}

		// valore di ritorno
		return errori;
	}

}