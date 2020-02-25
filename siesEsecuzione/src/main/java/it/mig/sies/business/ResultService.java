package it.mig.sies.business;

import org.apache.log4j.Logger;

import it.mig.sies.exception.ResultException;
import it.mig.sies.model.DescrizioneProvvedimento;
import it.mig.sies.model.RiepilogoOperazione;
import it.mig.sies.model.Sinonimo;
import it.mig.sies.type.esecuzione_NEW.ChiaviProvvedimentoGiudiziario;
import it.mig.sies.type.esecuzione_NEW.RequestData;
import it.mig.sies.type.esecuzione_NEW.ResponseCode;
import it.mig.sies.type.esecuzione_NEW.ResponseData;
import it.mig.sies.util.Mapper;
import it.mig.sies.util.SiesDAO;

/**
 * SIES FASE 2 - Servizio di decodifica della risposta
 * 
 * @author Federico Paparoni
 * */

public class ResultService {

	private static final Logger logger=Logger.getLogger(ResultService.class);

	private ResponseData responseData;
	private RequestData requestData;
	private String idEvento;
	private SiesDAO dao;
	private RiepilogoOperazione riepilogoOperazione;
	private String tipoOperazione;

	public ResultService(RequestData requestData,ResponseData responseData,String idEvento, RiepilogoOperazione riepilogoOperazione, String tipoOperazione) {
		this.responseData=responseData;
		this.requestData=requestData;
		this.idEvento=idEvento;
		this.dao=SiesDAO.getIstance();
		this.riepilogoOperazione=riepilogoOperazione;
		this.tipoOperazione=tipoOperazione; 
	}

	/**
	 * Metodo principale del servizio di decofifica
	 */
	public it.mig.sies.model.ResponseData execute() throws ResultException {
		try {
			logger.info("Gestione della risposta al trasferimento");
			/**
			 * Se la risposta � positiva, in quanto � stato effettuato un inserimento
			 * si deve aggiornare il database locale di SIES
			 * */
			if (aggiornamentoAvvenuto(responseData)) {
	            aggiornaDBInserimento();
	        } 
			/**
			 * Nel caso in cui sia stata effettuata una cancellazione
			 * si deve aggiornare il database locale di SIES
			 * */
			else if ((responseData.getEsito().getCodice().equals(ResponseCode.DELETE_EFFETTUATA))) {
	        	aggiornaDBCancellazione();
			/**
			 * Nei casi di Annulla Foglio Complementare oppure
			 * Annulla Provvedimento, anche in caso di errore bisogna 
			 * procedere ad aggiornare le tabelle di SIES.
			 * */
			} else {
				if (tipoOperazione != null &&
						(tipoOperazione.equals("DELETE") || tipoOperazione.equals("ANNULLA") ||
						   tipoOperazione.equals("ANNULLACFC"))) {
					aggiornaDBSIES();
				}
	        }
			
			//Mapping tra risposta del webservice 
			//e model locale per la gestione del risultato
	        it.mig.sies.model.ResponseData response = Mapper.map(idEvento, requestData.getAzione().value(),
	        		responseData, dao, riepilogoOperazione.getSoggetto());
	        
	        /**
			 * ISSUE MEV : Gestione specifica nel caso di errore per codici univoci
			 * Ticket    : 23010
			 * Autore    : fpaparoni
			 * Data      : 27/02/2013
			 * Branch    : MEV_23010
			 * 
			 * */
	        if (responseData.getEsito().getCodice().equals(ResponseCode.ERRORE_IN_DECODIFICA_CODICI_UNIVOCI)) {
	        	String codiceUnivoco=requestData.getProvvedimento().getDatiTribunaleSorveglianza()!=null
						? requestData.getProvvedimento().getDatiTribunaleSorveglianza().getCodiceUnivocoProvvedimento() 
						: requestData.getProvvedimento().getDatiUfficioSorveglianza().getCodiceUnivocoProvvedimento();
	        	DescrizioneProvvedimento descrizioneProvvedimento=dao.loadDescrizioneProvvedimento(codiceUnivoco);
	        	response.setDescrizioneProvvedimento(descrizioneProvvedimento);
	        }
	        response.setOperazione(requestData.getAzione().value());
	        response.setSoggetto(riepilogoOperazione.getSoggetto());
	        response.setTitoloEsecutivo(riepilogoOperazione.getTitoloEsecutivo());
	        response.setTitoloGiudiziarioList(riepilogoOperazione.getTitoloGiudiziarioList());
	        
	        // Inserimento della risposta sul DB
	        dao.insertResponse(response);

	        // MEV 31: insert nel db dei certificati di controllo per i sinonimi se presenti
 			if (response.getElencoSinonimi() != null && !response.getElencoSinonimi().isEmpty()) {
 				for (Sinonimo sinonimo : response.getElencoSinonimi()) {
 					it.mig.sies.model.ResponseData resp = new it.mig.sies.model.ResponseData();
 					String esito = Mapper.decodeEsito(requestData.getAzione().value(), responseData);
 					resp.setEsito(esito);
 					resp.setChiaveSies(Long.parseLong(idEvento));
 					resp.setCompleted(false);
 					resp.setEstratto(sinonimo.getCertificatoControlloSinonimo());
 					resp.setOperazione(requestData.getAzione().value());
 					resp.setSoggetto(riepilogoOperazione.getSoggetto());
 					resp.setTitoloEsecutivo(riepilogoOperazione.getTitoloEsecutivo());
 					resp.setTitoloGiudiziarioList(riepilogoOperazione.getTitoloGiudiziarioList());
 					dao.insertResponse(resp);
 					sinonimo.setIdCertificatoControlloSinonimo(resp.getId());
 				}
 			}
	        return response;
		}
		catch(Exception e) {
			logger.error(e.toString());
			throw new ResultException(e);
		}
        
	}
	
	/**
	 * Controlla se � avvenuto l'aggiornamento in base al codice di risposta
	 * Metodo necessario per complessit� ciclomatica!!!
	 * */
	private boolean aggiornamentoAvvenuto(ResponseData responseData) {
		return (responseData.getEsito().getCodice().equals(ResponseCode.INSERT_EFFETTUATA))
        		||(responseData.getEsito().getCodice().equals(ResponseCode.UPDATE_EFFETTUATA))
        		||(responseData.getEsito().getCodice().equals(ResponseCode.WARNING_IN_FASE_DI_INSERIMENTO))
        		||(responseData.getEsito().getCodice().equals(ResponseCode.PROBLEMI_CON_LA_GENERAZIONE_ESTRATTO))
        		||(responseData.getEsito().getCodice().equals(ResponseCode.ERRORI_IN_CALCOLO_MENZIONABILITA))
        		||(responseData.getEsito().getCodice().equals(ResponseCode.ERRORI_IN_VERIFICA_PROVVEDIMENTO));
	}

	/**
	 * Effettua le delete logiche sulle tabelle locali di SIES 
	 * a fronte di una cancellazione
	 * */
	private void aggiornaDBCancellazione() {
		logger.info("Delete logica delle chiavi provenienti da NSC");
		
		//Cancello l'id NSC soltanto se viene comunicato che 
		if (responseData.isAnagraficaCancellata())
			dao.deleteLogicaSoggetto(responseData.getChiaviAnagrafica());
		
		if (responseData.getEstratto()!=null)
			logger.info("Diverso da null");
		
		//Se il provvedimento non ha 
        //principali � lui stesso un principale...
        if ((!requestData.getProvvedimento().isFlagPrincipale())&&(responseData.getChiaviProvvedimentoGiudiziario()!=null)) {
			//Viene richiamata la delete logica per ogni titolo principale presente
	        for(ChiaviProvvedimentoGiudiziario chiaviProvvedimentoGiudiziario:responseData.getChiaviProvvedimentoGiudiziario()) 
	        	dao.deleteLogicaTitoloGiudiziario(chiaviProvvedimentoGiudiziario);
        }
        //In base alla tipologia di ufficio
        //vengono richiamate due differenti query di aggiornamento
        if (requestData.getUtente().getUfficio().getCodiceTipo().equals("304"))
        	dao.deleteLogicaDUS(responseData.getChiaviProvvedimentoEsecutivo());
        else
        	dao.deleteLogicaDTS(responseData.getChiaviProvvedimentoEsecutivo());
        
        // MEV 6 - SIUS Compilazione Foglio Complementare
        // In fase di "Cancellazione del Foglio Complementare"
        // bisogna aggiornare la data Trasmissione uguale a null 
        // sulla tabella D_DOCUMENTO_ALLEGATO; 
		// in fase di "Annullamento del Foglio Complementare" bisogna 
		// settare la DATA_ANNULLAMENTO uguale a sysdate e il campo 
		// FLAG_DOCUMENTO_REGISTRATO uguale ad 'A' sulla tabella DOCUMENTO_ALLEGATO; 
		// in fase di "Annullamento Provvedimento" bisogna settare la DATA_ANNULLAMENTO
        // uguale a sysdate e il campo FLAG_DOCUMENTO_REGISTRATO uguale ad
		// 'A' sulla tabella DOCUMENTO_ALLEGATO ED EVENTO.
          if(tipoOperazione != null && tipoOperazione.equals("DELETE")){
        	//cancellazione FC
        	dao.updateDocumentoAllegato(idEvento, "DELETE");
        } else if (tipoOperazione != null && tipoOperazione.equals("ANNULLA")){
        	//annullamento FC ed evento
        	dao.updateDocumentoAllegato(idEvento, "ANNULLA");
        } else {
        	// annullamento FC
        	dao.updateDocumentoAllegato(idEvento, "ANNULLACFC");
        }
	}
	
	/**
	 * Effettua l'aggiornamento delle tabelle locali di SIES 
	 * a fronte di un nuovo inserimento
	 * */
	private void aggiornaDBInserimento() {
		logger.info("Update delle chiavi provenienti da NSC");
        dao.updateSoggetto(responseData.getChiaviAnagrafica());
        
        //Se il provvedimento non ha 
        //principali e' lui stesso un principale...
        if (!requestData.getProvvedimento().isFlagPrincipale()) {
        	//Viene effettuato l'update per ogni titolo principale presente
            for(ChiaviProvvedimentoGiudiziario chiaviProvvedimentoGiudiziario:responseData.getChiaviProvvedimentoGiudiziario()) 
            	dao.updateTitoloGiudiziario(chiaviProvvedimentoGiudiziario);
        }
        
        //In base alla tipologia di ufficio
        //vengono richiamate due differenti query di aggiornamento
        if (requestData.getUtente().getUfficio().getCodiceTipo().equals("304"))
        	dao.updateDUS(responseData.getChiaviProvvedimentoEsecutivo());
        else
        	dao.updateDTS(responseData.getChiaviProvvedimentoEsecutivo());

        // MEV 6 - SIUS Compilazione Foglio Complementare
        // In fase di "Trasmissione del Foglio Complementare" oppure di "Modifica del Foglio Complementare"
        // bisogna aggiornare le date di Emissione, Trasmissione e Data Ultimo Invio
        // sulla tabella D_DOCUMENTO_ALLEGATO
        if (requestData.getAzione().value().equals("INSERT") ){
        	//trasmissione FC
        	dao.updateDocumentoAllegato(idEvento, "INSERT");
        } else {
        	//modifica FC
        	dao.updateDocumentoAllegato(idEvento, "UPDATE");
        }
        
	}

	/**
	 * Aggiornale tabelle locali di SIES a fronte di un annullamento 
	 * di un Foglio Complementare oppure di un Provvedimento
	 * */
	private void aggiornaDBSIES() {
		logger.info("Operazione di Annulla Provvedimento oppure Annulla Foglio Complementare");
        
        // MEV 6 - SIUS Compilazione Foglio Complementare
        // In fase di "Cancellazione del Foglio Complementare"
        // bisogna aggiornare la data Trasmissione uguale a null 
        // sulla tabella D_DOCUMENTO_ALLEGATO; 
		// in fase di "Annullamento del Foglio Complementare" bisogna 
		// settare la DATA_ANNULLAMENTO uguale a sysdate e il campo 
		// FLAG_DOCUMENTO_REGISTRATO uguale ad 'A' sulla tabella DOCUMENTO_ALLEGATO; 
		// in fase di "Annullamento Provvedimento" bisogna settare la DATA_ANNULLAMENTO
        // uguale a sysdate e il campo FLAG_DOCUMENTO_REGISTRATO uguale ad
		// 'A' sulla tabella DOCUMENTO_ALLEGATO ED EVENTO.
        if(tipoOperazione != null && tipoOperazione.equals("DELETE")){
        	// cancellazione FC
        	dao.updateDocumentoAllegato(idEvento, "DELETE");
        } else if (tipoOperazione != null && tipoOperazione.equals("ANNULLA")){
        	// annullamento FC ed evento
        	dao.updateDocumentoAllegato(idEvento, "ANNULLA");
        } else {
        	// annullamento FC
        	dao.updateDocumentoAllegato(idEvento, "ANNULLACFC");
        }
	}
	
}
