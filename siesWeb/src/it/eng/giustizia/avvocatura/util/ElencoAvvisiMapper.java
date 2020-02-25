package it.eng.giustizia.avvocatura.util;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi.AVVISO;
import it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi.DATATYPE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sius.avvocatura.model.AvvisiElencoModel;

/**
 * @author Caporizzo
 */
public class ElencoAvvisiMapper {
	
	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);	

	/**
	 * @param elencoAvvisi
	 * @return
	 */
	public static List<AVVISO> mapElencoAvvisi(Vector<AvvisiElencoModel> elencoAvvisi) {		

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: ElencoAvvisiMapper, metodo: mapElencoAvvisi");

		// instanzio un oggetto di tipo "ArrayList"
		List<AVVISO> lst = null;
		if (elencoAvvisi != null) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lst = new ArrayList<AVVISO>(elencoAvvisi.size());
			for (int i = 0; i < elencoAvvisi.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "AvvisiElencoModel"
				AvvisiElencoModel modelFascicolo = (AvvisiElencoModel) elencoAvvisi.get(i);
				
				// instanzio ed inizializzo un oggetto di tipo "AVVISO"
				AVVISO st = new AVVISO();
				
				// imposto i dati
				
				if (PropertyUtil.isPresent(modelFascicolo.getmIdAvviso())){
					st.setIdAvviso(modelFascicolo.getmIdAvviso().toBigInteger());
				}				
				
				if (PropertyUtil.isPresent(modelFascicolo.getmCodFiscaleAvvocato())){
					st.setCodiceFiscaleAvvocato(modelFascicolo.getmCodFiscaleAvvocato());
				}				
			
				if (PropertyUtil.isPresent(modelFascicolo.getmDescProvvedimento())){
					st.setDescrTipoProvvedimento(modelFascicolo.getmDescProvvedimento());
				}
				if (PropertyUtil.isPresent(modelFascicolo.getmTestoAvviso())){
					st.setContenuto(modelFascicolo.getmTestoAvviso());
				}
				if (PropertyUtil.isPresent(modelFascicolo.getmUfficioEmittente())){
					st.setUfficioEmittente(modelFascicolo.getmUfficioEmittente());
				}
				if (PropertyUtil.isPresent(modelFascicolo.getmDataDeposito())){
					st.setDataInserimento(creaDataTypeAvvisi(modelFascicolo.getmDataDeposito()));
				}				
				if (PropertyUtil.isPresent(modelFascicolo.getmAnnoSius())){
					st.setAnnoProcedimentoSIUS(modelFascicolo.getmAnnoSius().toBigInteger());
				}
				if (PropertyUtil.isPresent(modelFascicolo.getmNumeroSius())){
					st.setNumeroProcedimentoSIUS(modelFascicolo.getmNumeroSius().toBigInteger());
				}
				if (PropertyUtil.isPresent(modelFascicolo.getmNomeSoggetto())){
					st.setNomeSoggetto(modelFascicolo.getmNomeSoggetto());
				}
				if (PropertyUtil.isPresent(modelFascicolo.getmCognomeSoggetto())){
					st.setCognomeSoggetto(modelFascicolo.getmCognomeSoggetto());
				}
				if (PropertyUtil.isPresent(modelFascicolo.getmDataUdienza())){
					st.setDataUdienza(creaDataTypeAvvisi(modelFascicolo.getmDataUdienza()));
				}
				if (PropertyUtil.isPresent(modelFascicolo.getmFlagVisualizzazione())){
					st.setCodStatoAvviso(modelFascicolo.getmFlagVisualizzazione());
				}
				if (PropertyUtil.isPresent(modelFascicolo.getmIdEvento())){
					st.setIdEvento(modelFascicolo.getmIdEvento().toString());
				}	
				if (PropertyUtil.isPresent(modelFascicolo.getmCodiTipoProvvedimento())){
					st.setCodTipoProvvedimento(modelFascicolo.getmCodiTipoProvvedimento());
				}
				
				if (PropertyUtil.isPresent(modelFascicolo.getmCodiEsito())){
					st.setCodiceEsito(modelFascicolo.getmCodiEsito());
				}
				
				// aggiungo alla lista di ritorno
				lst.add(st);
			}
		}

		// valore di ritorno
		return lst;
	
	}
	
	
	/**
	 * Metodo per l'impostazione del tipo data per i soggetti con procedimenti
	 * 
	 * @param dataNascita
	 * @return DATATYPE
	 */
	public static DATATYPE creaDataTypeAvvisi(Date data) {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: creaDataTypeAvvisi");

		DATATYPE dt = new DATATYPE();
		if (data != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(data);
			dt.setGiorno(gc.get(Calendar.DAY_OF_MONTH));
			dt.setMese(gc.get(Calendar.MONTH) + 1);
			dt.setAnno(gc.get(Calendar.YEAR));
		}

		// valore di ritorno
		return dt;
	}

}
