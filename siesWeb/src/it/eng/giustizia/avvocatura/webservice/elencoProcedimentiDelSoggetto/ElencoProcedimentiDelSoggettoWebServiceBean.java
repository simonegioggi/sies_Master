/**
 * MEV AVVOCATURA
 */
package it.eng.giustizia.avvocatura.webservice.elencoProcedimentiDelSoggetto;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.action.ElencoProcedimentiDelSoggettoSiusAction;
import it.eng.giustizia.avvocatura.util.AvvocaturaProperties;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti.DATISOGGETTOINPUT;
import it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti.ELENCOPROCEDIMENTIOUTPUT;

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
public class ElencoProcedimentiDelSoggettoWebServiceBean {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public String elencoProcedimentiDelSoggetto(String datiSoggettoInput) throws Exception {

		// info per il log
		avvocaturaLogger.info("Classe ElencoProcedimentiDelSoggettoWebServiceBean, Metodo elencoProcedimentiDelSoggetto");

		ElencoProcedimentiDelSoggettoSiusAction epdssa = new ElencoProcedimentiDelSoggettoSiusAction();
		ELENCOPROCEDIMENTIOUTPUT epo = null;
		String path = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_PATH.concat("elencoprocedimenti"));
		JAXBContext jc = JAXBContext.newInstance(path);

		DATISOGGETTOINPUT dsi = null;

		// instanzio oggetti per la validazione in & out
		// SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		// InputStream inputStream = ElencoProcedimentiDelSoggettoWebServiceBean.class
		// .getResourceAsStream("/elenco_procedimenti.xsd");
		// Source source = new StreamSource(inputStream);
		// Schema schema = sf.newSchema(source);
		// ValidationHandler vh = null;

		try {
			// deserializzo il documento XML in oggetti Java
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			// validazione in entrata
			// unmarshaller.setSchema(schema);
			// vh = new ValidationHandler();
			// unmarshaller.setEventHandler(vh);
			ByteArrayInputStream bais = new ByteArrayInputStream(datiSoggettoInput.getBytes());
			dsi = (DATISOGGETTOINPUT) unmarshaller.unmarshal(bais);

			// controllo se ci sono eventi di errore in ingresso
			String[] errori = new String[2];
			errori = controllaValiditaDatiInput(dsi, errori);
			// if (vh.hasEvents()) { // + vh.formatEvents()
			if ("true".equals(errori[0])) {
				epo = new ELENCOPROCEDIMENTIOUTPUT();
				// imposto l'ERRORE
				epo.setERRORE(Mapper.mapErroreElencoProcedimenti(errori[1], ""));
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				marshaller.marshal(epo, baos);
				return baos.toString();
			} else {
				avvocaturaLogger.info("Nessun errore in fase di validazione");
				// recupero i dati per elaborare la risposta al client
				epo = epdssa.elencoProcedimentiDelSoggetto(dsi.getCodDistretto(),
						dsi.getCodiceFiscaleAvvocato(), dsi.getCodTipoUfficio(), dsi.getIdSoggetto());
			}
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error("Errore generico: " + e.getMessage(), e);
			// stampa dell'errore
			e.printStackTrace();
			// inizializzo l'oggetto di tipo "DATISOGGETTOOUTPUT"
			epo = new ELENCOPROCEDIMENTIOUTPUT();
			// imposto l'ERRORE
			epo.setERRORE(Mapper.mapErroreElencoProcedimenti("003", e.getMessage()));
		}

		// marshall delle stringhe
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		// validazione in uscita
		// marshaller.setSchema(schema);
		// vh = new ValidationHandler();
		// marshaller.setEventHandler(vh);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// serializzo gli oggetti Java in XML
		marshaller.marshal(epo, baos);
		// if (vh.hasEvents()) {
		// avvocaturaLogger.error("Errore di validazione in uscita: " + vh.formatEvents());
		// // inizializzo l'oggetto di tipo "DATISOGGETTOOUTPUT"
		// dso = new DATISOGGETTOOUTPUT();
		// // imposto l'ERRORE
		// dso.setERRORE(ErroreMapper.mapErroreSoggetto("002", vh.formatEvents()));
		// baos = new ByteArrayOutputStream();
		// marshaller.marshal(dso, baos);
		// } else
		// // info per il log
		// avvocaturaLogger.debug("Nessun Errore di validazione, stringa di ritorno: " + baos.toString());

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
	private String[] controllaValiditaDatiInput(DATISOGGETTOINPUT dsi, String[] errori) {

		// info per il log
		avvocaturaLogger.info("Classe ElencoProcedimentiDelSoggettoWebServiceBean, Metodo controllaValiditaDatiInput");

//		if (!PropertyUtil.isPresent(dsi.getCodTipoUfficio())) {
//			errori[0] = "true";
//			errori[1] = "012";
//		} else
		if (!PropertyUtil.isPresent(dsi.getCodDistretto())) {
			errori[0] = "true";
			errori[1] = "013";
		} else if (!PropertyUtil.isPresent(dsi.getCodiceFiscaleAvvocato())) {
			errori[0] = "true";
			errori[1] = "014";
		} else if (!PropertyUtil.isPresent(dsi.getIdSoggetto())) {
			errori[0] = "true";
			errori[1] = "015";
		} else {
			errori[0] = "false";
			errori[1] = "VALIDAZIONE DATI INPUT OK";
		}

		// valore di ritorno
		return errori;
	}

}