/**
 * RicercaSoggettiConProcedimenti_BindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.ricercaSoggettiConProcedimenti;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.webservice.ricercaSoggettiConProcedimenti.RicercaSoggettiConProcedimentiWebServiceBean;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

public class RicercaSoggettiConProcedimenti_BindingImpl implements RicercaSoggettiConProcedimenti_PortType {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public String ricercaSoggettiConProcedimenti(String datiSoggettoInput) throws RemoteException {

		// valore di ritorno
		String datiSoggettoOutput = "";
		RicercaSoggettiConProcedimentiWebServiceBean espwsb = new RicercaSoggettiConProcedimentiWebServiceBean();
		// info per il log
		avvocaturaLogger.info("Starting Point del WS RicercaSoggettiConProcedimenti_BindingImpl - ricercaSoggettiConProcedimenti:\n"
				+ datiSoggettoInput);
		// controllo di consistenza dell'oggetto "String"
		if (PropertyUtil.isPresent(datiSoggettoInput)) {
			try {
				// elaborazione della stringa
				datiSoggettoOutput = espwsb.ricercaSoggettiConProcedimenti(datiSoggettoInput);
			} catch (Exception e) {
				// info per il log
				avvocaturaLogger.error("Errore in RicercaSoggettiConProcedimenti_BindingImpl - ricercaSoggettiConProcedimenti:\n"
						+ e.getMessage());
				e.printStackTrace();
			}
		} else
			throw new UnsupportedOperationException("WEB SERVICE NON ANCORA IMPLEMENTATO!");

		// info per il log
		avvocaturaLogger.info(datiSoggettoOutput);
		// valore di ritorno
		return datiSoggettoOutput;
	}

}