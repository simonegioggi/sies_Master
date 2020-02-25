package it.eng.giustizia.avvocatura.webservice.dettaglioProcedimento;
//package it.eng.giustizia.avvocatura.webservice;
//
//import it.eng.giustizia.avvocatura.util.PropertyUtil;
//
//import org.apache.log4j.Logger;
//
//import siap.sico.webservice.action.ActWsBase;
//import f3b.log.LogF3B;
//
//public class DettaglioProcedimento extends ActWsBase {
//
//	// variabile di classe per il log
//	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);
//
//	/**
//	 * Classe esponente il WS di dettaglio procedimento
//	 * 
//	 * @param flussoXml
//	 * @return
//	 * @throws Exception
//	 */
//	public String dettaglioProcedimento(/*DATIPROCEDIMENTOINPUT*/String flussoXml) throws Exception {
//
//		// valore di ritorno
//		String datiProcedimetnoOutput = "";
//		// instanzio un oggetto di tipo "DettaglioProcedimentoWebServiceBean"
//		DettaglioProcedimentoWebServiceBean dpwsb = null;
//		// info per il log
//		logger.info("DettaglioProcedimento - dettaglioProcedimento:\n" + flussoXml);
//		// controllo di consistenza dell'oggetto "String"
//		if (PropertyUtil.isPresent(flussoXml)) {
//			try {
//				// inizializzo un oggetto di tipo "DettaglioProcedimentoWebServiceBean"
//				dpwsb = new DettaglioProcedimentoWebServiceBean();
//				// elaborazione della stringa
//				datiProcedimetnoOutput = dpwsb.dettaglioProcedimento(flussoXml);
//			} catch (Exception e) {
//				// info per il log
//				logger.error("Errore in DettaglioProcedimento - dettaglioProcedimento:\n" + e.getMessage());
//				e.printStackTrace();
//			}
//		} else
//			// lancio nuova eccezione
//			throw new UnsupportedOperationException("WEB SERVICE NON ANCORA IMPLEMENTATO!");
//		// info per il log
//		logger.info(datiProcedimetnoOutput);
//		// valore di ritorno
//		return datiProcedimetnoOutput;
//	}
//
//}