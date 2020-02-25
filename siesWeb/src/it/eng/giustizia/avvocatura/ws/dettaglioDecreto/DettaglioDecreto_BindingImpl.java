/**
 * DettaglioDecreto_BindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.dettaglioDecreto;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.webservice.dettaglioDecreto.DettaglioDecretoWebServiceBean;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

public class DettaglioDecreto_BindingImpl implements DettaglioDecreto_PortType {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

    public String dettaglioDecreto(String datiDecretoInput) throws RemoteException {

		// valore di ritorno
		String datiDecretoOutput = "";
		DettaglioDecretoWebServiceBean ddwsb = new DettaglioDecretoWebServiceBean();
		// info per il log
		avvocaturaLogger.info("Starting Point del WS DettaglioDecreto_BindingImpl - dettaglioDecreto:\n" + datiDecretoInput);
		// controllo di consistenza dell'oggetto "String"
		if (PropertyUtil.isPresent(datiDecretoInput)) {
			try {
				// elaborazione della stringa
				datiDecretoOutput = ddwsb.dettaglioDecreto(datiDecretoInput);
			} catch (Exception e) {
				// info per il log
				avvocaturaLogger.error("Errore in DettaglioDecreto_BindingImpl - dettaglioDecreto:\n" + e.getMessage(), e);
				e.printStackTrace();
			}
		} else
			throw new UnsupportedOperationException("WEB SERVICE NON ANCORA IMPLEMENTATO!");

		// info per il log
		avvocaturaLogger.info(datiDecretoOutput);
		// valore di ritorno
		return datiDecretoOutput;
	}

}