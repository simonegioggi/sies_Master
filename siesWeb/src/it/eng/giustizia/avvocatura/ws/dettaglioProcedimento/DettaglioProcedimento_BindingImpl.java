/**
 * DettaglioProcedimento_BindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.dettaglioProcedimento;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.webservice.dettaglioProcedimento.DettaglioProcedimentoWebServiceBean;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

public class DettaglioProcedimento_BindingImpl implements DettaglioProcedimento_PortType {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public String dettaglioProcedimento(String datiProcedimentoInput) throws RemoteException {

		// valore di ritorno
		String datiProcedimentoOutput = "";
		DettaglioProcedimentoWebServiceBean dpwsb = new DettaglioProcedimentoWebServiceBean();
		// info per il log
		avvocaturaLogger.info("Starting Point del WS DettaglioProcedimento_BindingImpl - dettaglioProcedimento:\n"
				+ datiProcedimentoInput);
		// controllo di consistenza dell'oggetto "String"
		if (PropertyUtil.isPresent(datiProcedimentoInput)) {
			try {
				// elaborazione della stringa
				datiProcedimentoOutput = dpwsb.dettaglioProcedimento(datiProcedimentoInput);
			} catch (Exception e) {
				// info per il log
				avvocaturaLogger.error("Errore in DettaglioProcedimento_BindingImpl - dettaglioProcedimento:\n"
						+ e.getMessage());
				e.printStackTrace();
			}
		} else
			throw new UnsupportedOperationException("WEB SERVICE NON ANCORA IMPLEMENTATO!");

		// info per il log
		avvocaturaLogger.info(datiProcedimentoOutput);
		// valore di ritorno
		return datiProcedimentoOutput;
	}

}