/**
 * RicercaAvvisi_BindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.ricercaAvvisi;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.webservice.ricercaAvvisi.RicercaAvvisiWebServiceBean;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

public class RicercaAvvisi_BindingImpl implements RicercaAvvisi_PortType {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

    public String ricercaAvvisi(String datiAvvisoInput) throws RemoteException {

		// valore di ritorno
		String datiAvvisoOutput = "";
		RicercaAvvisiWebServiceBean rawsb = new RicercaAvvisiWebServiceBean();
		// info per il log
		avvocaturaLogger.info("Starting Point del WS RicercaAvvisi_BindingImpl - ricercaAvvisi:\n"
				+ datiAvvisoInput);
		// controllo di consistenza dell'oggetto "String"
		if (PropertyUtil.isPresent(datiAvvisoInput)) {
			try {
				// elaborazione della stringa
				datiAvvisoOutput = rawsb.ricercaAvvisi(datiAvvisoInput);
			} catch (Exception e) {
				// info per il log
				avvocaturaLogger.error("Errore in RicercaAvvisi_BindingImpl - ricercaAvvisi:\n" + e.getMessage(), e);
				e.printStackTrace();
			}
		} else
			throw new UnsupportedOperationException("WEB SERVICE NON ANCORA IMPLEMENTATO!");

		// info per il log
		avvocaturaLogger.info(datiAvvisoOutput);
		// valore di ritorno
		return datiAvvisoOutput;
	}

}