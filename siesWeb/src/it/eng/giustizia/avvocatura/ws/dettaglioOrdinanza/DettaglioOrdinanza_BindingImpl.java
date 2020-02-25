/**
 * DettaglioOrdinanza_BindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.dettaglioOrdinanza;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.webservice.dettaglioOrdinanza.DettaglioOrdinanzaWebServiceBean;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

public class DettaglioOrdinanza_BindingImpl implements DettaglioOrdinanza_PortType {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public String dettaglioOrdinanza(String datiOrdinanzaInput)
			throws RemoteException {

		// valore di ritorno
		String datiOrdinanzaOutput = "";
		DettaglioOrdinanzaWebServiceBean ddwsb = new DettaglioOrdinanzaWebServiceBean();
		// info per il log
		avvocaturaLogger.info("Starting Point del WS DettaglioOrdinanza_BindingImpl - dettaglioOrdinanza:\n" + datiOrdinanzaInput);
		// controllo di consistenza dell'oggetto "String"
		if (PropertyUtil.isPresent(datiOrdinanzaInput)) {
			try {
				// elaborazione della stringa
				datiOrdinanzaOutput = ddwsb.dettaglioOrdinanza(datiOrdinanzaInput);
			} catch (Exception e) {
				// info per il log
				avvocaturaLogger.error("Errore in DettaglioOrdinanza_BindingImpl - dettaglioOrdinanza:\n" + e.getMessage(), e);
				e.printStackTrace();
			}
		} else
			throw new UnsupportedOperationException("WEB SERVICE NON ANCORA IMPLEMENTATO!");

		// info per il log
		avvocaturaLogger.info(datiOrdinanzaOutput);
		// valore di ritorno
		return datiOrdinanzaOutput;
	}

}