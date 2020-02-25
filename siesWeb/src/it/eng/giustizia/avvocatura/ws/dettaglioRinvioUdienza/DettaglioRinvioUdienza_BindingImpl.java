/**
 * DettaglioRinvioUdienza_BindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.dettaglioRinvioUdienza;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.webservice.dettaglioRinvioUdienza.DettaglioRinvioUdienzaWebServiceBean;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

public class DettaglioRinvioUdienza_BindingImpl implements DettaglioRinvioUdienza_PortType {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public String dettaglioRinvioUdienza(String datiRinvioUdienzaInput) throws RemoteException {

		// valore di ritorno
		String datiRinvioUdienzaOutput = "";
		DettaglioRinvioUdienzaWebServiceBean ddwsb = new DettaglioRinvioUdienzaWebServiceBean();
		// info per il log
		avvocaturaLogger.info("Starting Point del WS DettaglioRinvioUdienza_BindingImpl - dettaglioRinvioUdienza:\n"
				+ datiRinvioUdienzaInput);
		// controllo di consistenza dell'oggetto "String"
		if (PropertyUtil.isPresent(datiRinvioUdienzaInput)) {
			try {
				// elaborazione della stringa
				datiRinvioUdienzaOutput = ddwsb.dettaglioRinvioUdienza(datiRinvioUdienzaInput);
			} catch (Exception e) {
				// info per il log
				avvocaturaLogger.error(
						"Errore in DettaglioRinvioUdienza_BindingImpl - dettaglioRinvioUdienza:\n"
								+ e.getMessage(), e);
				e.printStackTrace();
			}
		} else
			throw new UnsupportedOperationException("WEB SERVICE NON ANCORA IMPLEMENTATO!");

		// info per il log
		avvocaturaLogger.info(datiRinvioUdienzaOutput);
		// valore di ritorno
		return datiRinvioUdienzaOutput;
	}

}