package siap.sige.tenore.action;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.util.SICOLookupRemote;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActAggiornaTenoriSigeInSessione extends ActionSige implements ICostantiTenoreSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public void caricaTenori(StringTokenizer lCodOggetto, StringTokenizer lIdSentenza, StringTokenizer lIdReati, String modalita, String codContenuto) throws Exception {

	    // Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
    	ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
    	
    	IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();
		 
	    // Di seguito gli scenari che si possono presentare in fase di inserimento/modifica
		// CASO 1) 1 oggetto selezionato – 1 titolo esecutivo selezionato = associazione un oggetto un titolo
		// esecutivo
		// CASO 2) 1 oggetto selezionato – N titoli esecutivi selezionati = associazione oggetto con N titoli
		// esecutivi
		// CASO 3) N oggetti selezionati – N titoli esecutivi selezionati = associazione di ogni oggetto
		// selezionato con N titoli esecutivi
		// CASO 4) N oggetti selezionati – 1 titolo esecutivo selezionato = associazione di ogni oggetto
		// selezionato con un titolo esecutivo.
	    
	    // numero di Oggetti selezionati
	    int lNumOggetti = 1;
	    if (lCodOggetto.countTokens() > 0){
	    	lNumOggetti = lCodOggetto.countTokens();
	    }
	    
	    // numero di Titoli Esecutivi selezionati
	    int lNumTitoliEsecutivi = 1;
	    if (lIdSentenza.countTokens() > 0){
	    	lNumTitoliEsecutivi = lIdSentenza.countTokens();
	    }
	    
	    // Se definiti più reati ci sarà un Tenore per ogni Reato
	    // numero di Reati selezionati
	    int lNumReati = 1; 
	    if (lIdReati.countTokens() > 0){
	    	lNumReati = lIdReati.countTokens();
	    }
	    
        Date lOggi = DateUtils.getSysDate();
    	String codUtenteConnesso = getCodUtenteConnesso();
    	String codUfficioUtenteConnesso = getCodUfficioUtenteConnesso();
        
        // CASO 1
	    if (lNumOggetti == 1 && lNumTitoliEsecutivi == 1){
		    // Si istanzia un array di Tenori
	        TenoreSigeEstesoModel lTenoriEsteso[] = new TenoreSigeEstesoModel[lNumReati];

	    	// ID Oggetto corrente
	    	String lIdOggetto = null;
	    	// ID Titolo ESecutivo corrente
	    	BigDecimal lIdTitoloEsecutivo = null;

	    	if (lCodOggetto.hasMoreTokens() ){
	    		lIdOggetto = lCodOggetto.nextToken();
	    	}

	    	if (lIdSentenza.hasMoreTokens()){
	    		lIdTitoloEsecutivo = new BigDecimal(lIdSentenza.nextToken());
	    	}

	        // Valorizzazione dell'array di Tenori
		    // Si cicla sul numero di Reati selezonati
			for (int i = 0; i < lNumReati; i++) {
		    	// ID Reato corrente
		    	BigDecimal lIdReato = null;

		    	if (lIdReati.hasMoreTokens()){
		    		lIdReato = new BigDecimal(lIdReati.nextToken());
		    	}
		    	
		    	TenoreSigeModel lTenore =  new TenoreSigeModel();

		    	if(i == 0){
		    		lTenore.setFlagOggetto("S");
		    	} else {
		    		lTenore.setFlagOggetto("N");
		    	}

		    	lTenore.setCodOperatoreInserimento(codUtenteConnesso);
		     	lTenore.setCodUfficioInserimento(codUfficioUtenteConnesso);
		     	lTenore.setDataInserimento(lOggi);
		    	if(modalita.equals("M")){
			    	lTenore.setCodOperatoreAggiornamento(codUtenteConnesso);
			     	lTenore.setCodUfficioAggiornamento(codUfficioUtenteConnesso);
			     	lTenore.setDataAggiornamento(lOggi);
		    	}
		    	lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		    	lTenore.setData(lOggi);
		     	lTenore.setCodOggettoSige(lIdOggetto);
		     	lTenore.setDescrOggettoSige(lDecCtrl.ExRicercaDescrByCodOggettoSige(lIdOggetto));
				lTenore.setDescrContenutoSige(lDecCtrl.ExRicercaDescrByCodContenutoSige(codContenuto));
				lTenore.setCodContenutoSige(codContenuto);
		     	lTenore.setIdSentenza(lIdTitoloEsecutivo);
		    	lTenore.setIdReato(lIdReato);

			    // Ricerca Sentenza
		    	SentenzaModel lSentenza = lCtrl.ricercaSentenzaByKey(lIdTitoloEsecutivo);

			    // Ricerca Reato
		    	ReatoModel lReato = lReaCtrl.ExRicercaReatoByKey(lTenore.getIdReato());
		        // Creazione del Model Aggregato ed aggiunta all'elenco Risultato
		        TenoreSigeEstesoModel lTenoreEsteso = new TenoreSigeEstesoModel(lTenore, lSentenza, lReato);
		        // Aggiunto all'array
		        lTenoriEsteso[i] = lTenoreEsteso;
		    	
		    } // endwhile 
		
	        // Inserimento/Modifica Tenori in sessione
	        if(modalita.equals("I")){
	        	inserimentoTenoriInSessione(lTenoriEsteso);
	        } else {
	        	modificaTenoriInSessione(lTenoriEsteso);
	        }
	        
	    }
	    
	    // CASO 2
	    if (lNumOggetti == 1 && lNumTitoliEsecutivi > 1){
		    // Si istanzia un array di Tenori
	        TenoreSigeEstesoModel lTenoriEsteso[] = new TenoreSigeEstesoModel[lNumTitoliEsecutivi];
	        
	    	// ID Oggetto corrente
	    	String lIdOggetto = null;

	    	if (lCodOggetto.hasMoreTokens() ){
	    		lIdOggetto = lCodOggetto.nextToken();
	    	}

	        // Valorizzazione dell'array di Tenori
	        // Si cicla sul numero di Titoli Esecutivi selezonati
			for (int i = 0; i < lNumTitoliEsecutivi; i++) {
		    	// ID Titolo ESecutivo corrente
		    	BigDecimal lIdTitoloEsecutivo = null;
		    	// ID Reato corrente
		    	BigDecimal lIdReato = null;

		    	if (lIdSentenza.hasMoreTokens()){
		    		lIdTitoloEsecutivo = new BigDecimal(lIdSentenza.nextToken());
		    	}
		    	
		    	if (lIdReati.hasMoreTokens()){
		    		lIdReato = new BigDecimal(lIdReati.nextToken());
		    	}
		    	
		    	TenoreSigeModel lTenore =  new TenoreSigeModel();

		    	if(i == 0){
		    		lTenore.setFlagOggetto("S");
		    	} else {
		    		lTenore.setFlagOggetto("N");
		    	}

		    	if(i == 0){
		    		lTenore.setFlagOggetto("S");
		    	} else {
		    		lTenore.setFlagOggetto("N");
		    	}

		    	lTenore.setCodOperatoreInserimento(codUtenteConnesso);
		     	lTenore.setCodUfficioInserimento(codUfficioUtenteConnesso);
		     	lTenore.setDataInserimento(lOggi);
		    	if(modalita.equals("M")){
			    	lTenore.setCodOperatoreAggiornamento(codUtenteConnesso);
			     	lTenore.setCodUfficioAggiornamento(codUfficioUtenteConnesso);
			     	lTenore.setDataAggiornamento(lOggi);
		    	}
		    	lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		    	lTenore.setData(lOggi);
		     	lTenore.setCodOggettoSige(lIdOggetto);
		     	lTenore.setDescrOggettoSige(lDecCtrl.ExRicercaDescrByCodOggettoSige(lIdOggetto));
		     	lTenore.setIdSentenza(lIdTitoloEsecutivo);
		    	lTenore.setIdReato(lIdReato);
				// 20190517 [SG]: aggiunte impostazioni proprieta'
				lTenore.setCodContenutoSige(codContenuto);
				lTenore.setDescrContenutoSige(lDecCtrl.ExRicercaDescrByCodContenutoSige(codContenuto));

			    // Ricerca Sentenza
		    	SentenzaModel lSentenza = lCtrl.ricercaSentenzaByKey(lIdTitoloEsecutivo);

			    // Ricerca Reato
		    	ReatoModel lReato = lReaCtrl.ExRicercaReatoByKey(lTenore.getIdReato());
		        // Creazione del Model Aggregato ed aggiunta all'elenco Risultato
		        TenoreSigeEstesoModel lTenoreEsteso = new TenoreSigeEstesoModel(lTenore, lSentenza, lReato);
		        // Aggiunto all'array
		        lTenoriEsteso[i] = lTenoreEsteso;
		    	
		    } // endwhile 
		
	        // Inserimento/Modifica Tenori in sessione
	        if(modalita.equals("I")){
	        	inserimentoTenoriInSessione(lTenoriEsteso);
	        } else {
	        	modificaTenoriInSessione(lTenoriEsteso);
	        }
	        
	    }
	    
	    // CASO 3
	    if (lNumOggetti > 1 && lNumTitoliEsecutivi > 1){

	        int numRecordTot = lNumOggetti * lNumTitoliEsecutivi;
	        int numRecord = 0;

		    // Si istanzia un array di Tenori
	        TenoreSigeEstesoModel lTenoriEsteso[] = new TenoreSigeEstesoModel[numRecordTot];
	        
	        // si cicla sugli Oggetti selezionati
	        for (int j = 0; j < lNumOggetti; j++){
		        if(j > 0){
		        	numRecord = numRecord +1;
		        }
		        
		    	// ID Oggetto corrente
		    	String lIdOggetto = null;

		    	if (lCodOggetto.hasMoreTokens() ){
		    		lIdOggetto = lCodOggetto.nextToken();
		    	}
		    	
		    	lIdSentenza = new StringTokenizer(getRequestStringParameter(CAMPO_SEN_ID_SENTENZA) ,"|");

		        // Valorizzazione dell'array di Tenori
		        // Si cicla sul numero di Titoli Esecutivi selezonati
				for (int i = 0; i < lNumTitoliEsecutivi; i++) {
		        	if(i > 0){
		        		numRecord = numRecord + 1;
		        	}
		        	
		        	// ID Titolo ESecutivo corrente
			    	BigDecimal lIdTitoloEsecutivo = null;
			    	// ID Reato corrente
			    	BigDecimal lIdReato = null;
	
			    	if (lIdSentenza.hasMoreTokens()){
			    		lIdTitoloEsecutivo = new BigDecimal(lIdSentenza.nextToken());
			    	}
			    	
			    	if (lIdReati.hasMoreTokens()){
			    		lIdReato = new BigDecimal(lIdReati.nextToken());
			    	}
			    	
			    	TenoreSigeModel lTenore =  new TenoreSigeModel();
			    	
			    	if(i == 0){
			    		lTenore.setFlagOggetto("S");
			    	} else {
			    		lTenore.setFlagOggetto("N");
			    	}
			    	lTenore.setCodOperatoreInserimento(codUtenteConnesso);
			     	lTenore.setCodUfficioInserimento(codUfficioUtenteConnesso);
			     	lTenore.setDataInserimento(lOggi);
			    	if(modalita.equals("M")){
				    	lTenore.setCodOperatoreAggiornamento(codUtenteConnesso);
				     	lTenore.setCodUfficioAggiornamento(codUfficioUtenteConnesso);
				     	lTenore.setDataAggiornamento(lOggi);
			    	}
			    	lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			    	lTenore.setData(lOggi);
			     	lTenore.setCodOggettoSige(lIdOggetto);
			     	lTenore.setDescrOggettoSige(lDecCtrl.ExRicercaDescrByCodOggettoSige(lIdOggetto));
			     	lTenore.setIdSentenza(lIdTitoloEsecutivo);
			    	lTenore.setIdReato(lIdReato);
					// 20190517 [SG]: aggiunte impostazioni proprieta'
					lTenore.setCodContenutoSige(codContenuto);
					lTenore.setDescrContenutoSige(lDecCtrl.ExRicercaDescrByCodContenutoSige(codContenuto));

				    // Ricerca Sentenza
			    	SentenzaModel lSentenza = lCtrl.ricercaSentenzaByKey(lIdTitoloEsecutivo);

				    // Ricerca Reato
			    	ReatoModel lReato = lReaCtrl.ExRicercaReatoByKey(lTenore.getIdReato());
			        // Creazione del Model Aggregato ed aggiunta all'elenco Risultato
					TenoreSigeEstesoModel lTenoreEsteso = new TenoreSigeEstesoModel(lTenore, lSentenza,
							lReato);
			        // Aggiunto all'array
			        lTenoriEsteso[numRecord] = lTenoreEsteso;
			    	
			    } // end for (int i = 0; i < lNumTitoliEsecutivi; i++)
			
	    	} // end for (int j = 0; j < lNumOggetti; j++)
	        
	        // Inserimento/Modifica Tenori in sessione
	        if(modalita.equals("I")){
	        	inserimentoTenoriInSessione(lTenoriEsteso);
	        } else {
	        	modificaTenoriInSessione(lTenoriEsteso);
	        }

	    }
	    
	    // CASO 4
	    if (lNumOggetti > 1 && lNumTitoliEsecutivi == 1){

	    	int numRecordTot = lNumOggetti * lNumReati;
	        int numRecord = 0;

	        // Si istanzia un array di Tenori
	        TenoreSigeEstesoModel lTenoriEsteso[] = new TenoreSigeEstesoModel[numRecordTot];
	        
	        // si cicla sugli Oggetti selezionati
	        for (int j = 0; j < lNumOggetti; j++){
		        if(j > 0){
		        	numRecord = numRecord +1;
		        }

		    	lIdSentenza = new StringTokenizer(getRequestStringParameter(CAMPO_SEN_ID_SENTENZA) ,"|");
		    	
		    	// ID Oggetto corrente
		    	String lIdOggetto = null;
		    	// ID Titolo Esecutivo corrente
		    	BigDecimal lIdTitoloEsecutivo = null;

		    	if (lCodOggetto.hasMoreTokens() ){
		    		lIdOggetto = lCodOggetto.nextToken();
		    	}
		    	
		    	if (lIdSentenza.hasMoreTokens()){
		    		lIdTitoloEsecutivo = new BigDecimal(lIdSentenza.nextToken());
		    	}
		    	
		    	lIdReati = new StringTokenizer(getRequestStringParameter(CAMPO_REA_ID_REATO) ,"|");
		    	
		    	// Valorizzazione dell'array di Tenori
		    	// Si cicla sul numero di Reati selezionati
				for (int i = 0; i < lNumReati; i++) {
		        	if(i > 0){
		        		numRecord = numRecord + 1;
		        	}

		        	// ID Reato corrente
			    	BigDecimal lIdReato = null;
	
			    	if (lIdReati.hasMoreTokens()){
			    		lIdReato = new BigDecimal(lIdReati.nextToken());
			    	}
			    	
			    	TenoreSigeModel lTenore =  new TenoreSigeModel();

			    	if(i == 0){
			    		lTenore.setFlagOggetto("S");
			    	} else {
			    		lTenore.setFlagOggetto("N");
			    	}
			    	lTenore.setCodOperatoreInserimento(codUtenteConnesso);
			     	lTenore.setCodUfficioInserimento(codUfficioUtenteConnesso);
			     	lTenore.setDataInserimento(lOggi);
			    	if(modalita.equals("M")){
				    	lTenore.setCodOperatoreAggiornamento(codUtenteConnesso);
				     	lTenore.setCodUfficioAggiornamento(codUfficioUtenteConnesso);
				     	lTenore.setDataAggiornamento(lOggi);
			    	}
			    	lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			     	lTenore.setData(lOggi);
			     	lTenore.setCodOggettoSige(lIdOggetto);
			     	lTenore.setDescrOggettoSige(lDecCtrl.ExRicercaDescrByCodOggettoSige(lIdOggetto));
			     	lTenore.setIdSentenza(lIdTitoloEsecutivo);
			    	lTenore.setIdReato(lIdReato);
					// 20190517 [SG]: aggiunte impostazioni proprieta'
					lTenore.setCodContenutoSige(codContenuto);
					lTenore.setDescrContenutoSige(lDecCtrl.ExRicercaDescrByCodContenutoSige(codContenuto));
			    	
				    // Ricerca Sentenza
			    	SentenzaModel lSentenza = lCtrl.ricercaSentenzaByKey(lIdTitoloEsecutivo);

				    // Ricerca Reato
			    	ReatoModel lReato = lReaCtrl.ExRicercaReatoByKey(lTenore.getIdReato());
			        // Creazione del Model Aggregato ed aggiunta all'elenco Risultato
					TenoreSigeEstesoModel lTenoreEsteso = new TenoreSigeEstesoModel(lTenore, lSentenza,
							lReato);
			        // Aggiunto all'array
			        lTenoriEsteso[numRecord] = lTenoreEsteso;
			    } // endwhile 
	    	}
		   
	        // Inserimento/Modifica Tenori in sessione
	        if(modalita.equals("I")){
	        	inserimentoTenoriInSessione(lTenoriEsteso);
	        } else {
	        	modificaTenoriInSessione(lTenoriEsteso);
	        }

	    }			
	}	

	private void inserimentoTenoriInSessione(TenoreSigeEstesoModel lNuoviTenori[]) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
	    siesLogger.debug(getClass().getName() + "inserimentoTenoriInSessione: inizio");

	    Vector lElencoTenori;
		// Si prelevano i Tenori dalla sessione l'elenco dei Tenori 
		if(!isSessionAttributeNullObj("tenori"))
			lElencoTenori = (Vector) getSessionAttribute("tenori");
		else
			lElencoTenori = new Vector();
	  
		int lRiemp = lElencoTenori.size();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
	    siesLogger.debug("num tenori già in sessione : " + lRiemp);

		if (lRiemp > 0) {
			// Riordinamento dell'elenco
			Collections.sort(lElencoTenori);	
			// Controllo eventuali duplicazioni
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("controllo duplicazione" );
			for (int i = 0; i < lNuoviTenori.length; i++) {
				if (Collections.binarySearch(lElencoTenori, lNuoviTenori[i]) >= 0)
					throw new SIGEException(SIGEException.USER_MESSAGE,
							"Non è possibile inserire 2 volte la stessa tripletta: Oggetto-Sentenza-Reato !");
			}
		}
	
		// Si aggiungono i nuovi Tenori in coda
		//for(int i = 0,j = (lRiemp + 1) ; i < lNuoviTenori.length; i++)
		for (int i = 0; i < lNuoviTenori.length; i++) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
		    siesLogger.debug("inserimento nuovo tenore id : " + i);

			// Valorizzazione di un ID Tenore fittizio per distinguere il tenore in sessione
			lNuoviTenori[i].getTenoreSige().setIdTenoreSige(new BigDecimal(i));
			lElencoTenori.add(lNuoviTenori[i]);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
	    siesLogger.debug("nuovo num tenori aggiornati in sessione : " + lElencoTenori.size());
	    
	    // Riordinamento dell'elenco
	    Collections.sort(lElencoTenori);	
	    
		// Si rimette in sessione l'elenco aggiornato
		setSessionAttribute("tenori", lElencoTenori);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
	    siesLogger.debug(getClass().getName() + "inserimentoTenoriInSessione: fine");
		
	}

	private void modificaTenoriInSessione(TenoreSigeEstesoModel lNuoviTenori[]) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
	    siesLogger.debug(getClass().getName() + "modificaTenoriInSessione: inizio");

	    Vector lElencoTenori = new Vector();
	
		// Si aggiungono i nuovi Tenori
		for (int i = 0; i < lNuoviTenori.length; i++) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
		    siesLogger.debug("inserimento nuovo tenore id : " + i);

			// Valorizzazione di un ID Tenore fittizio per distinguere il tenore in sessione
			lNuoviTenori[i].getTenoreSige().setIdTenoreSige(new BigDecimal(i));
			lElencoTenori.add(lNuoviTenori[i]);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
	    siesLogger.debug("nuovo num tenori aggiornati in sessione : " + lElencoTenori.size());
	    
	    // Riordinamento dell'elenco
	    Collections.sort(lElencoTenori);	
	    
		// Si rimette in sessione l'elenco aggiornato
		setSessionAttribute("tenori", lElencoTenori);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
	    siesLogger.debug(getClass().getName() + "modificaTenoriInSessione: fine");
	}
	
}