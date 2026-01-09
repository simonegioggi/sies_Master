/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.webservice.ricercaAvvisi;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.action.RicercaAvvisiSiusAction;
import it.eng.giustizia.avvocatura.util.AvvocaturaProperties;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi.DATIAVVISOINPUT;
import it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi.DATIAVVISOOUTPUT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

/**
 * @author caporizzo
 */
public class RicercaAvvisiWebServiceBean {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public String ricercaAvvisi(String datiAvvisoInput) throws Exception {

		// info per il log
		avvocaturaLogger.info("Classe RicercaAvvisiWebServiceBean, Metodo ricercaAvvisi");

		RicercaAvvisiSiusAction avvisiAction = new RicercaAvvisiSiusAction();
		DATIAVVISOINPUT datiAvvisiInput = null;
		DATIAVVISOOUTPUT avvisiOutput = null;
		String path = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_PATH
				.concat("ricercaavvisi"));
		JAXBContext jc = JAXBContext.newInstance(path);

		try {
			// deserializzo il documento XML in oggetti Java
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			ByteArrayInputStream bais = new ByteArrayInputStream(datiAvvisoInput.getBytes());
			datiAvvisiInput = (DATIAVVISOINPUT) unmarshaller.unmarshal(bais);

			// controllo se ci sono eventi di errore in ingresso
			String[] errori = new String[2];
			errori = controllaValiditaDatiInput(datiAvvisiInput, errori);
			// if (vh.hasEvents()) { // + vh.formatEvents()
			if ("true".equals(errori[0])) {
				avvisiOutput = new DATIAVVISOOUTPUT();
				// imposto l'ERRORE
				avvisiOutput.setERRORE(Mapper.mapErroreAvviso(errori[1], ""));
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				marshaller.marshal(avvisiOutput, baos);
				return baos.toString();
			} else {
				avvocaturaLogger.info("Nessun errore in fase di validazione");
				// recupero i dati per elaborare la risposta al client
				avvisiOutput = avvisiAction.ricercaAvvisi(datiAvvisiInput);
			}
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error("Errore generico: " + e.getMessage(), e);
			// stampa dell'errore
			e.printStackTrace();
			// inizializzo l'oggetto di tipo "DATIAVVISOOUTPUT"
			avvisiOutput = new DATIAVVISOOUTPUT();
			// imposto l'ERRORE
			avvisiOutput.setERRORE(Mapper.mapErroreAvviso("003", e.getMessage()));
		}

		// marshall delle stringhe
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// serializzo gli oggetti Java in XML
		marshaller.marshal(avvisiOutput, baos);
		// eseguo la "pulizia" dello stream
		baos.flush();
		// valore di ritorno
		return baos.toString();

	}

	/**
	 * Metodo di controllo validità dati input
	 * 
	 * @param datiAvvisiInput
	 * @param errori
	 * @return String[]
	 */
	private String[] controllaValiditaDatiInput(DATIAVVISOINPUT datiAvvisiInput, String[] errori) {

		// info per il log
		avvocaturaLogger.info("Classe RicercaAvvisiWebServiceBean, Metodo controllaValiditaDatiInput");

		// per i dati di input controllare:
		// 1.presenza del CF dell'avvocato
		// 2.presenza codiceDistretto
		// 3.presenza codTipoUfficio
		// 4.codStatoAvviso (S=avviso visualizzato N=avviso non visualizzato T=Tutti)
		if (!PropertyUtil.isPresent(datiAvvisiInput.getCodTipoUfficio())) {
			errori[0] = "true";
			errori[1] = "012";
		} else if (!PropertyUtil.isPresent(datiAvvisiInput.getCodDistretto())) {
			errori[0] = "true";
			errori[1] = "013";
		} else if (!PropertyUtil.isPresent(datiAvvisiInput.getCodiceFiscaleAvvocato())) {
			errori[0] = "true";
			errori[1] = "014";
		} else if (!PropertyUtil.isPresent(datiAvvisiInput.getCodStatoAvviso())) {
			errori[0] = "true";
			errori[1] = "042";
		} else {
			errori[0] = "false";
			errori[1] = "VALIDAZIONE DATI INPUT OK";
		}

		// valore di ritorno
		return errori;
	}

}