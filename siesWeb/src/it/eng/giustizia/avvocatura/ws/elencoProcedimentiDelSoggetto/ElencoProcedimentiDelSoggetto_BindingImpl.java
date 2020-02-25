/**
 * ElencoProcedimentiDelSoggetto_BindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.webservice.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggettoWebServiceBean;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

public class ElencoProcedimentiDelSoggetto_BindingImpl implements ElencoProcedimentiDelSoggetto_PortType {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

    public String elencoProcedimentiDelSoggetto(String datiSoggettoInput) throws RemoteException {

    	// info per il log
		avvocaturaLogger.info("Starting Point del WS ElencoProcedimentiDelSoggetto_BindingImpl - elencoProcedimentiDelSoggetto:\n"
				+ datiSoggettoInput);
		// valore di ritorno
		String elencoProcedimentiOutput = "";
		ElencoProcedimentiDelSoggettoWebServiceBean epdswsb = new ElencoProcedimentiDelSoggettoWebServiceBean();
		// controllo di consistenza dell'oggetto "String"
		if (PropertyUtil.isPresent(datiSoggettoInput)) {
			try {
				// elaborazione della stringa
				elencoProcedimentiOutput = epdswsb.elencoProcedimentiDelSoggetto(datiSoggettoInput);
			} catch (Exception e) {
				// info per il log
				avvocaturaLogger.error("Errore in ElencoProcedimentiDelSoggetto_BindingImpl - elencoProcedimentiDelSoggetto:\n"
						+ e.getMessage());
				e.printStackTrace();
			}
		} else
			throw new UnsupportedOperationException("WEB SERVICE NON ANCORA IMPLEMENTATO!");

		// info per il log
		avvocaturaLogger.info(elencoProcedimentiOutput);
		// valore di ritorno
		return elencoProcedimentiOutput;
    }

}