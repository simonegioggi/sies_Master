/**
 * MEV AVVOCATURA
 */
package it.eng.giustizia.avvocatura.webservice.richiestaStampa;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.action.RichiestaStampaSiusAction;
import it.eng.giustizia.avvocatura.util.AvvocaturaProperties;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.ws.type.richiestaStampa.DATISTAMPAINPUT;
import it.eng.giustizia.avvocatura.ws.type.richiestaStampa.DATISTAMPAOUTPUT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

/**
 * @author Gioggi
 *
 */
public class RichiestaStampaWebServiceBean {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public String richiestaStampa(String datiStampaInput) throws Exception {

		// info per il log
		avvocaturaLogger.info("Classe RichiestaStampaWebServiceBean, Metodo richiestaStampa");

		// recupero dati di input ed elaborazione di quelli di output
		DATISTAMPAINPUT dsi = null;
		DATISTAMPAOUTPUT dso = null;
		RichiestaStampaSiusAction rssa = new RichiestaStampaSiusAction();
		String path = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_PATH
				.concat("richiestastampa"));
		JAXBContext jc = JAXBContext.newInstance(path);
		// SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		// InputStream inputStream = RichiestaStampaWebServiceBean.class
		// .getResourceAsStream("/richiesta_stampa.xsd");
		// Source source = new StreamSource(inputStream);
		// Schema schema = sf.newSchema(source);
		// ValidationHandler vh = null;

		try {
			// deserializzo il documento XML in oggetti Java
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			// unmarshaller.setSchema(schema);
			// vh = new ValidationHandler();
			// unmarshaller.setEventHandler(vh);
			ByteArrayInputStream bais = new ByteArrayInputStream(datiStampaInput.getBytes());
			dsi = (DATISTAMPAINPUT) unmarshaller.unmarshal(bais);

			// controllo se ci sono eventi di errore in ingresso
			String[] errori = new String[2];
			errori = controllaValiditaDatiInput(dsi, errori);
			// if (vh.hasEvents()) {
			// avvocaturaLogger.error("Errore di validazione in ingresso: " + vh.formatEvents());
			// }
			if ("true".equals(errori[0])) {
				// inizializzo l'oggetto di tipo "DATISTAMPAOUTPUT"
				dso = new DATISTAMPAOUTPUT();
				// imposto l'ERRORE
				dso.setERRORE(Mapper.mapErroreStampaRichiesta(errori[1], ""));
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				marshaller.marshal(dso, baos);
				return baos.toString();
			} else {
				// info per il log
				avvocaturaLogger.info("Nessun errore in fase di validazione");
				// recupero i dati per elaborare la risposta al client
				dso = rssa.richiestaStampa(new BigDecimal(dsi.getIdFascicoloSius()), dsi.getCodDistretto(),
						dsi.getCodiceFiscaleAvvocato(), dsi.getCodTipoUfficio());
			}
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error("Errore generico: " + e.getMessage(), e);
			// stampa dell'errore
			e.printStackTrace();
			dso = new DATISTAMPAOUTPUT();
			// imposto l'ERRORE
			dso.setERRORE(Mapper.mapErroreStampaRichiesta("003", e.getMessage()));
		}

		// marshall delle stringhe
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// serializzo gli oggetti Java in XML
		marshaller.marshal(dso, baos);

		// eseguo la "pulizia" dello stream
		baos.flush();
		// valore di ritorno
		return baos.toString();
	}

	/**
	 * Metodo di controllo validità dati input
	 * 
	 * @param dsi
	 * @param errori
	 * @return String[]
	 */
	private String[] controllaValiditaDatiInput(DATISTAMPAINPUT dsi, String[] errori) {

		// info per il log
		avvocaturaLogger.info("Classe RichiestaStampaWebServiceBean, Metodo controllaValiditaDatiInput");

		if (!PropertyUtil.isPresent(dsi.getCodTipoUfficio())) {
			errori[0] = "true";
			errori[1] = "012";
		} else if (!PropertyUtil.isPresent(dsi.getCodDistretto())) {
			errori[0] = "true";
			errori[1] = "013";
		} else if (!PropertyUtil.isPresent(dsi.getCodiceFiscaleAvvocato())) {
			errori[0] = "true";
			errori[1] = "014";
		} else if (!PropertyUtil.isPresent(dsi.getIdFascicoloSius())) {
			errori[0] = "true";
			errori[1] = "016";
		} else {
			errori[0] = "false";
			errori[1] = "VALIDAZIONE DATI INPUT OK";
		}

		// valore di ritorno
		return errori;
	}

}