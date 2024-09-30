package siap.siep.calcolopena.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.model.CalcoloPenaDL92Model;
import siap.siep.penacomplessiva.action.ICostantiPenaComplessiva;
import siap.siep.penaresidua.action.ICostantiPenaResidua;

/**
 * 
 * @since MEV_2024-092
 */
public class ActCalcoloPenaDL92 extends ActionSiap implements ICostantiCalcoloPena {
	
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	public String processRequest() throws Exception {
		
		
		CalendarUtil lCalUtil = new CalendarUtil();
		
		CalcoloPenaDL92Model lCalcoloModel = new CalcoloPenaDL92Model();
		
		// Quantum Reclusione
		CalendarModel lQuantumReclusione = new CalendarModel();
		
		lQuantumReclusione.setNumAnni   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE));
		lQuantumReclusione.setNumMesi   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE));
		lQuantumReclusione.setNumGiorni (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE));
		
		// Normalizzo i dati
		lQuantumReclusione = lCalUtil.ricalcolaGAM (lQuantumReclusione);
		
		lCalcoloModel.setNumAnniReclusione   (new BigDecimal (lQuantumReclusione.getNumAnni()));
		lCalcoloModel.setNumMesiReclusione   (new BigDecimal (lQuantumReclusione.getNumMesi()));
		lCalcoloModel.setNumGiorniReclusione (new BigDecimal (lQuantumReclusione.getNumGiorni()));

//		lCalcoloModel.setNumAnniReclusione   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE));
//		lCalcoloModel.setNumMesiReclusione   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE));
//		lCalcoloModel.setNumGiorniReclusione (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE));
		
		// Multa  ???
		
		// Quantum Arresto 
		CalendarModel lQuantumArresto = new CalendarModel();
		
		lQuantumArresto.setNumAnni   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO));
		lQuantumArresto.setNumMesi   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO));
		lQuantumArresto.setNumGiorni (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO));
		
	  // Normalizzo i dati
		lQuantumArresto = lCalUtil.ricalcolaGAM (lQuantumArresto);
		
		lCalcoloModel.setNumAnniArresto   (new BigDecimal (lQuantumArresto.getNumAnni()));
		lCalcoloModel.setNumMesiArresto   (new BigDecimal (lQuantumArresto.getNumMesi()));
		lCalcoloModel.setNumGiorniArresto (new BigDecimal (lQuantumArresto.getNumGiorni()));
		
//		lCalcoloModel.setNumAnniArresto   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO));
//		lCalcoloModel.setNumMesiArresto   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO));
//		lCalcoloModel.setNumGiorniArresto (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO));
		
		// Ammenda ??
		
		// Presofferto
		CalendarModel lQuantumPresofferto = new CalendarModel();
		
		lQuantumPresofferto.setNumAnni   (getRequestBigDecimalParameter (ICostantiCalcoloPena.CAMPO_NUM_ANNI_PRESOFFERTO));
		lQuantumPresofferto.setNumMesi   (getRequestBigDecimalParameter (ICostantiCalcoloPena.CAMPO_NUM_MESI_PRESOFFERTO));
		lQuantumPresofferto.setNumGiorni (getRequestBigDecimalParameter (ICostantiCalcoloPena.CAMPO_NUM_GIORNI_PRESOFFERTO));
		
	  // Normalizzo i dati
		lQuantumPresofferto = lCalUtil.ricalcolaGAM (lQuantumPresofferto);
		
		lCalcoloModel.setNumAnniPresofferto   (new BigDecimal (lQuantumPresofferto.getNumAnni()));
		lCalcoloModel.setNumMesiPresofferto   (new BigDecimal (lQuantumPresofferto.getNumMesi()));
		lCalcoloModel.setNumGiorniPresofferto (new BigDecimal (lQuantumPresofferto.getNumGiorni()));
		
//		lCalcoloModel.setNumAnniPresofferto   (getRequestBigDecimalParameter (ICostantiCalcoloPena.CAMPO_NUM_ANNI_PRESOFFERTO));
//		lCalcoloModel.setNumMesiPresofferto   (getRequestBigDecimalParameter (ICostantiCalcoloPena.CAMPO_NUM_MESI_PRESOFFERTO));
//		lCalcoloModel.setNumGiorniPresofferto (getRequestBigDecimalParameter (ICostantiCalcoloPena.CAMPO_NUM_GIORNI_PRESOFFERTO));
		
		// Posizione Giuridica
		lCalcoloModel.setPosizioneGiuridica (getRequestStringParameter (ICostantiCalcoloPena.CAMPO_POSIZIONE_GIURIDICA));
		
		// Data Inizio Pena
		if (lCalcoloModel.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) {
			siesLogger.debug("Recupera la data inizio pena");
			lCalcoloModel.setDataInizioPena (getRequestDateParameter (ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA
					                                                    , ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA
					                                                    , ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA));
			
			siesLogger.debug("Recupera la data inizio pena = "+lCalcoloModel.getDataInizioPena());
		}
		
		lCalcoloModel.calcolaPenaVirtuale();
		lCalcoloModel.stampaCalcolo();
		setRequestAttribute("EsitoCalcolo",lCalcoloModel);
		
		return PG_CALCOLOPENA_DL92;		
	}
}
