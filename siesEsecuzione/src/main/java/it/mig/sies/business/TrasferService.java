package it.mig.sies.business;

import it.mig.sies.model.TitoloEsecutivo;
import it.mig.sies.model.TrasferEntry;
import it.mig.sies.util.SiesDAO;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * SIES FASE 2 - Servizio di caricamento dei dati necessari 
 * per effettuare la richiesta una richiesta massiva di inserimento
 * 
 * @author Federico Paparoni
 * */
public class TrasferService {
	
	private static final Logger logger=Logger.getLogger(TrasferService.class);
	
	/**
	 * Carica una lista di entry da trasferire a partire
	 * dalla tipologia di provvedimento
	 * 
	 * @param tipologia Tipologia di provvedimento da caricare
	 * */
	public List<TrasferEntry> load(String tipologia) {
		logger.info("Caricamento tipologia: "+tipologia);
		
		//Viene istanziato il DAO
		SiesDAO dao=SiesDAO.getIstance();
		
		//Caricamento della lista di titoli da trasferire
		List<TitoloEsecutivo> entryList = dao.loadTrasferEntry(tipologia);
		List<TrasferEntry> trasferList = new ArrayList<TrasferEntry>();
		
		//Per ogni entry caricata viene effettuato
		//il mapping con il model definito per il
		//trasferimento massivo
		for(TitoloEsecutivo esecutivo:entryList) {
			TrasferEntry trasferEntry=new TrasferEntry();
			trasferEntry.setIdEvento(esecutivo.getIdEvento());
			trasferEntry.setAnnoProtocollo(esecutivo.getAnnoFascicolo());
			trasferEntry.setNumeroProtocollo(esecutivo.getNumeroFascicolo());
			trasferList.add(trasferEntry);
		}
		
		return trasferList;
	}
	
}
