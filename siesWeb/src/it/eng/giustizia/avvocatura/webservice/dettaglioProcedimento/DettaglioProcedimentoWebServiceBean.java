/**
 * MEV AVVOCATURA
 */
package it.eng.giustizia.avvocatura.webservice.dettaglioProcedimento;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.controller.IAvvocaturaSius;
import it.eng.giustizia.avvocatura.util.AvvocaturaProperties;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.DATIPROCEDIMENTOINPUT;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.DATIPROCEDIMENTOOUTPUT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import siap.sius.util.SIUSLookupRemote;

/**
 * @author Gioggi
 *
 */
public class DettaglioProcedimentoWebServiceBean {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public String dettaglioProcedimento(String datiProcedimentoInput) throws Exception {

		// info per il log
		avvocaturaLogger.info("Classe DettaglioProcedimentoWebServiceBean, Metodo dettaglioProcedimento");
		// PER LA VALORIZZAZIONE DEI DATI IN RISPOSTA AL WEBSERVICE è STATO SVILUPPATO
		// UN PACKAGE ORACLE: "AVVOCATURA_SIUS.CERCA_FASIUS_PER_ESTREMI"

		// PERTANTO I PASSI DA FARE SONO:
		// 1. CONTROLLO DI VALIDAZIONE DEI DATI IN INPUT
		// (DEVONO ESSERE PRESENTI TUTTI I DATI STABILITI DA ANALISI)
		// 2. COSTRUIRE GLI OGGETTI PER LA CHIAMATA AD ORACLE
		// 3. LEGGERE L'OUTPUT DELLA PROCEDURA ORACLE
		// 4. COSTRUIRE GLI OGGETTI JAVA DI RISPOSTA CHE SI ASPETTA IL WEB SERVICE
		DATIPROCEDIMENTOOUTPUT datiProcedimentoOutput = null;
		String path = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_PATH.concat("ricercasoggetticonprocedimenti"));
		JAXBContext jc = JAXBContext.newInstance(path);

		DATIPROCEDIMENTOINPUT dpi = null;

		// instanzio oggetti per la validazione in & out
//		SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
//		InputStream inputStream = DettaglioProcedimentoWebServiceBean.class
//				.getResourceAsStream("/dettaglio_procedimento.xsd");
//		Source source = new StreamSource(inputStream);
//		Schema schema = sf.newSchema(source);
//		ValidationHandler vh = null;

		try {
			// deserializzo il documento XML in oggetti Java
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			// validazione in entrata
//			unmarshaller.setSchema(schema);
//			vh = new ValidationHandler();
//			unmarshaller.setEventHandler(vh);
			ByteArrayInputStream bais = new ByteArrayInputStream(datiProcedimentoInput.getBytes());
			dpi = (DATIPROCEDIMENTOINPUT) unmarshaller.unmarshal(bais);

			// controllo se ci sono eventi di errore in ingresso
			String[] errori = new String[2];
			errori = controllaValiditaDatiInput(dpi, errori);
//			if (vh.hasEvents()) { // + vh.formatEvents()
			if ("true".equals(errori[0])) {
				datiProcedimentoOutput = new DATIPROCEDIMENTOOUTPUT();
				// imposto l'ERRORE
				datiProcedimentoOutput.setERRORE(Mapper.mapErroreProcedimento(errori[1], ""));
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				marshaller.marshal(datiProcedimentoOutput, baos);
				return baos.toString();
			} else {
				avvocaturaLogger.info("Nessun errore in fase di validazione");
				// recupero i dati per invocare la store procedure
				// PER LA VALORIZZAZIONE DEI DATI IN RISPOSTA AL WEBSERVICE
				// E' STATA SVILUPPATA UNA PROCEDURA ORACLE
			    IAvvocaturaSius ias = SIUSLookupRemote.getAvvocaturaSiusRemote();
				datiProcedimentoOutput = ias.callRicercaFascicoloSius(dpi.getCodDistretto(), dpi
						.getCodTipoUfficio(), dpi.getCodiceFiscaleAvvocato(), dpi.getAnnoProcedimento()
						.intValue(), dpi.getNumeroProcedimento().intValue());
			}
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error("Errore generico: " + e.getMessage(), e);
			// stampa dell'errore
			e.printStackTrace();
			// inizializzo l'oggetto di tipo "DATIPROCEDIMENTOOUTPUT"
			datiProcedimentoOutput = new DATIPROCEDIMENTOOUTPUT();
			// imposto l'ERRORE
			datiProcedimentoOutput.setERRORE(Mapper.mapErroreProcedimento("003", e.getMessage()));
		}

		// marshall delle stringhe
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		// validazione in uscita
//		marshaller.setSchema(schema);
//		vh = new ValidationHandler();
//		marshaller.setEventHandler(vh);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// serializzo gli oggetti Java in XML
		marshaller.marshal(datiProcedimentoOutput, baos);
//		if (vh.hasEvents()) {
//			avvocaturaLogger.error("Errore di validazione in uscita: " + vh.formatEvents());
//			// inizializzo l'oggetto di tipo "DATIPROCEDIMENTOOUTPUT"
//			datiProcedimentoOutput = new DATIPROCEDIMENTOOUTPUT();
//			// imposto l'ERRORE
//			datiProcedimentoOutput.setERRORE(ErroreMapper.mapErroreProcedimento("002", vh.formatEvents()));
//			baos = new ByteArrayOutputStream();
//			marshaller.marshal(datiProcedimentoOutput, baos);
//		} else
//			// info per il log
//			avvocaturaLogger.debug("Nessun Errore di validazione, stringa di ritorno: " + baos.toString());

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
	private String[] controllaValiditaDatiInput(DATIPROCEDIMENTOINPUT dpi, String[] errori) {

		// info per il log
		avvocaturaLogger.info("Classe DettaglioProcedimentoWebServiceBean, Metodo controllaValiditaDatiInput");

		if (!PropertyUtil.isPresent(dpi.getAnnoProcedimento())) {
			errori[0] = "true";
			errori[1] = "010";
		} else if (!PropertyUtil.isPresent(dpi.getNumeroProcedimento())) {
			errori[0] = "true";
			errori[1] = "011";
		} else if (!PropertyUtil.isPresent(dpi.getCodTipoUfficio())) {
			errori[0] = "true";
			errori[1] = "012";
		} else if (!PropertyUtil.isPresent(dpi.getCodDistretto())) {
			errori[0] = "true";
			errori[1] = "013";
		} else if (!PropertyUtil.isPresent(dpi.getCodiceFiscaleAvvocato())) {
			errori[0] = "true";
			errori[1] = "014";
		} else {
			errori[0] = "false";
			errori[1] = "VALIDAZIONE DATI INPUT OK";
		}

		// valore di ritorno
		return errori;
	}

}