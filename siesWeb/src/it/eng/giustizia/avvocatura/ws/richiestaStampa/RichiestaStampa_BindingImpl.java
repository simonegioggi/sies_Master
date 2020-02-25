/**
 * RichiestaStampa_BindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.richiestaStampa;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.webservice.richiestaStampa.RichiestaStampaWebServiceBean;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

public class RichiestaStampa_BindingImpl implements RichiestaStampa_PortType {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public String richiestaStampa(String datiStampaInput) throws RemoteException {

		// valore di ritorno
		String datiStampaOutput = "";
		RichiestaStampaWebServiceBean rswsb = new RichiestaStampaWebServiceBean();
		// info per il log
		avvocaturaLogger.info("Starting Point del WS RichiestaStampa_BindingImpl - richiestaStampa:\n"
				+ datiStampaInput);
		// controllo di consistenza dell'oggetto "String"
		if (PropertyUtil.isPresent(datiStampaInput)) {
			try {
				// elaborazione della stringa
				datiStampaOutput = rswsb.richiestaStampa(datiStampaInput);
			} catch (Exception e) {
				// info per il log
				avvocaturaLogger.error("Errore in RichiestaStampa_BindingImpl - richiestaStampa:\n" + e.getMessage(), e);
				e.printStackTrace();
			}
		} else
			throw new UnsupportedOperationException("WEB SERVICE NON ANCORA IMPLEMENTATO!");

		// info per il log
		avvocaturaLogger.info(datiStampaOutput);
		// valore di ritorno
		return datiStampaOutput;
	}

}