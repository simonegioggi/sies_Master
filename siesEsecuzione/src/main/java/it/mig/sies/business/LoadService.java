package it.mig.sies.business;

import it.mig.sies.exception.LoadException;
import it.mig.sies.exception.ProfileException;
import it.mig.sies.model.MisuraSicurezza;
import it.mig.sies.model.ProvvedimentoCollegato;
import it.mig.sies.model.ResponseData;
import it.mig.sies.model.RiepilogoOperazione;
import it.mig.sies.model.Soggetto;
import it.mig.sies.model.TitoloEsecutivo;
import it.mig.sies.model.TitoloGiudiziario;
import it.mig.sies.model.Utente;
import it.mig.sies.type.esecuzione_NEW.Anagrafica;
import it.mig.sies.type.esecuzione_NEW.Azione;
import it.mig.sies.type.esecuzione_NEW.ChiaviProvvedimentoEsecutivo;
import it.mig.sies.type.esecuzione_NEW.Provvedimento;
import it.mig.sies.type.esecuzione_NEW.RequestData;
import it.mig.sies.util.ApplicationProperties;
import it.mig.sies.util.Mapper;
import it.mig.sies.util.PropertyUtil;
import it.mig.sies.util.SiesDAO;

import java.math.BigInteger;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * SIES FASE 2 - Servizio di caricamento dei dati necessari 
 * per effettuare la richiesta
 * 
 * @author Federico Paparoni
 * */

public class LoadService {
	
	private static final Logger logger=Logger.getLogger(LoadService.class);
	
	private String idEvento;
	private String action;
	//MEV 35253
	private String idUtente;
	private SiesDAO dao;
	private RiepilogoOperazione riepilogoOperazione;
	//MEV 35253
	public final static String LOCAL_SEARCH="SEARCH";
	
	public LoadService(String idEvento) {
		this.idEvento=idEvento;
		this.dao=SiesDAO.getIstance();
		this.riepilogoOperazione=new RiepilogoOperazione();
	}
	
	/**
	 * Costruttore usato solo per il trasferimento massivo 
	 * senza un utente...da modificare
	 * */
	public LoadService(String idEvento,String action) {
		this.idEvento=idEvento;
		this.action=action;
		this.dao=SiesDAO.getIstance();
		this.riepilogoOperazione=new RiepilogoOperazione();
	}
	//MEV 35253
	public LoadService(String idEvento,String action,String idUtente) {
		this.idEvento=idEvento;
		this.idUtente=idUtente;
		this.action=action;
		this.dao=SiesDAO.getIstance();
		this.riepilogoOperazione=new RiepilogoOperazione();
	}
	
	/**
	 * Metodo principale del servizio di caricamento
	 * 
	 * @throws LoadException
	 */
	public RequestData execute() throws LoadException,ProfileException {
		logger.info("Inizio caricamento dati dal database locale");
		//MEV 35253
		verificaUtente(idUtente);
		RequestData requestData = loadData(idEvento, action);
		return requestData;
	}
	
	/**
	 * Verifica che l'utente sia abilitato al trasferimento
	 * MEV 35253
	 * @throws ProfileException 
	 * @throws LoadException 
	 * 
	 * @throws LoadException
	 */
	private void verificaUtente(String idUtente) throws ProfileException, LoadException {
		logger.info("Verifica profilo per utente "+idUtente);
		boolean abilitato=false;
		List<String> profiliUtente=dao.loadProfiliUtente(idUtente);
		
		//Caricamento dei profili abilitati in base al tipo di operazione
		String profiliAbilitati=null;
		if (action.equals(Azione.INSERT.toString())) {
			profiliAbilitati=ApplicationProperties.getIstance().getProperty("profili.abilitati.inserimento");
        } else if (action.equals(Azione.DELETE.toString())) {
        	profiliAbilitati=ApplicationProperties.getIstance().getProperty("profili.abilitati.modifica");
        } else if (action.equals(Azione.UPDATE.toString())) {
        	profiliAbilitati=ApplicationProperties.getIstance().getProperty("profili.abilitati.cancellazione");
        } else if (action.equals(LOCAL_SEARCH)) {
        	profiliAbilitati=ApplicationProperties.getIstance().getProperty("profili.abilitati.storico");
        } else {
        	logger.error("Azione non definita o riconosciuta");
            throw new LoadException("Azione non definita o riconosciuta");
        }
		
		//Verifica della presenza del profilo
		StringTokenizer tokenizer=new StringTokenizer(profiliAbilitati,"|");
		while(tokenizer.hasMoreTokens()) {
			if (profiliUtente.contains(tokenizer.nextToken())) {
				abilitato=true;
				break;
			}
		}
		
		if (!abilitato) {
			throw new ProfileException("Utente ["+idUtente+"] non abilitato al trasferimento");
		}
		logger.info("Utente "+idUtente+" abilitato all'operazione");
	}

	/**
	 * Caricamento delle varie entity
	 * 
	 * @throws LoadException
	 */
	private RequestData loadData(String idEvento, String action) throws LoadException {
    	try {
    		RequestData requestData = new RequestData();
    		//Caricamento utente
            it.mig.sies.model.Utente utente = loadUtente(idUtente);
            //Caricamento soggetto
            Soggetto soggetto = loadSoggetto(idEvento);
            //Caricamento lista titoli principali
            List<TitoloGiudiziario> titoloGiudiziarioList = loadTitoloGiudiziario(idEvento);
            //Caricamento titolo esecutivo
            TitoloEsecutivo titoloEsecutivo=dao.loadTitoloEsecutivo(idEvento);
            //Popolamento dei dati di riepilogo
            this.riepilogoOperazione.setSoggetto(soggetto);
            this.riepilogoOperazione.setTitoloEsecutivo(titoloEsecutivo);
            this.riepilogoOperazione.setTitoloGiudiziarioList(titoloGiudiziarioList);
            
            Provvedimento provvedimento = new Provvedimento();
            //MEV 23010 - Popolo i nuovi campi anno/numero SIUS
            provvedimento.setAnnoSius(titoloEsecutivo.getAnnoFascicolo());
            provvedimento.setNumeroSius(titoloEsecutivo.getNumeroFascicolo());
            //Mapping per l'invio dei dati
            it.mig.sies.type.esecuzione_NEW.Utente utenteRequest = Mapper.map(utente);
            Anagrafica anagrafica = Mapper.map(soggetto);
            
            //Se il provvedimento non ha 
            //principali ï¿½ lui stesso un principale...
            if ((titoloGiudiziarioList!=null)&&(titoloGiudiziarioList.size()>0)) {
            	for (TitoloGiudiziario titoloGiudiziario : titoloGiudiziarioList) {
                    provvedimento.getProvvedimentoGiudiziario().add(Mapper.map(titoloGiudiziario));
                }
            	provvedimento.setFlagPrincipale(false);
            }
            else {
            	// TODO Cambiare l'XSD
            	provvedimento.getProvvedimentoGiudiziario().add(null);
            	provvedimento.setFlagPrincipale(true);
            }
            	
            
            requestData.setProvvedimento(provvedimento);
            requestData.setAnagrafica(anagrafica);
            requestData.setUtente(utenteRequest);
            //Gestione della azione da inviare
            manageAction(action,requestData);
            //Caricamento provvedimento esecutivo
            loadDettagliProvvedimentoEsecutivo(provvedimento,dao,idEvento,utente);
            return requestData;
    	}
    	catch(Exception e) {
    		logger.error(e.toString());
    		e.printStackTrace();
    		throw new LoadException(e);
    	}
    }
	
	/**
	 * Caricamento dei dati relativi al provvedimento esecutivo
	 * 
	 * @throws LoadException 
	 * */
	private void loadDettagliProvvedimentoEsecutivo(
			Provvedimento provvedimento, SiesDAO dao, String idEvento,
			Utente utente) throws LoadException{
		//Caricamento del provvedimento esecutivo
		//in base alla tipologia di ufficio
		if (utente.getCodiceTipoUfficio().equals("304")) {
        	loadDatiUfficioSorveglianza(provvedimento,dao,idEvento);
        } else if (utente.getCodiceTipoUfficio().equals("305")) {
        	loadDatiTribunaleSorveglianza(provvedimento,dao,idEvento);
        } else {
            throw new LoadException("Codice del sistema non riconosciuto:" + utente.getCodiceTipoUfficio());
        }
	}

	/**
	 * Gestione della action
	 * 
	 * @throws LoadException 
	 * */
	private void manageAction(String action, RequestData requestData) throws LoadException{
		if (action.equals(Azione.INSERT.toString())) {
            requestData.setAzione(Azione.INSERT);
        } else if (action.equals(Azione.DELETE.toString())) {
            requestData.setAzione(Azione.DELETE);
        } else if (action.equals(Azione.UPDATE.toString())) {
            requestData.setAzione(Azione.UPDATE);
        } else {
        	logger.error("Azione non definita o riconosciuta");
            throw new LoadException("Azione non definita o riconosciuta");
        }
	}

	/**
	 * Caricamento della lista di titoli principali
	 * 
	 * @throws LoadException 
	 * */
	private List<TitoloGiudiziario> loadTitoloGiudiziario(String idEvento) throws LoadException{
		List<TitoloGiudiziario> titoloGiudiziarioList = null;
		titoloGiudiziarioList = dao.loadTitoloGiudiziario(idEvento);
		
		//Se il caricamento non trova l'entity 
		//si deve bloccare il trasferimento
		
		//04/04/2012 - Controllo commentato in quanto devono essere gestiti 
		//dei fogli complementari che non hanno dei titoli principali associati
        /*if ((titoloGiudiziarioList == null) || (titoloGiudiziarioList.isEmpty())) {
        	logger.error("Provvedimenti principali associati non trovati");
            throw new LoadException("Provvedimenti principali associati non trovati");
        }*/
        return titoloGiudiziarioList;
	}

	/**
	 * Caricamento del soggetto
	 * 
	 * @throws LoadException 
	 * */
	private Soggetto loadSoggetto(String idEvento) throws LoadException{
		Soggetto soggetto=null;
		soggetto=dao.loadSoggetto(idEvento);
		
		//Se il caricamento non trova l'entity 
		//si deve bloccare il trasferimento
        if (soggetto == null) {
        	logger.error("Soggetto non trovato");
            throw new LoadException("Soggetto non trovato");
        }
        return soggetto;
	}

	/**
	 * Caricamento dell'utente che ha inserito il provvedimento dell'esecuzione
	 * 
	 * @throws LoadException 
	 * */
	private Utente loadUtente(String idUtente) throws LoadException{
		it.mig.sies.model.Utente utente = null;
		utente=dao.loadUtente(idUtente);
		
		//Se il caricamento non trova l'entity 
		//si deve bloccare il trasferimento
        if (utente == null) {
        	logger.error("Utente non trovato");
            throw new LoadException("Utente non trovato");
        }
        return utente;
	}
	
	/**
	 * Carica i dati del Tribunale di Sorveglianza
	 * 
	 * @throws LoadException 
	 * */
	private void loadDatiTribunaleSorveglianza(Provvedimento provvedimento,
			SiesDAO dao, String idEvento) throws LoadException {
		logger.info("Caricamento Dati Tribunale Sorveglianza");
		it.mig.sies.model.DatiTribunaleSorveglianza datiTribunaleSorveglianza = dao.loadTDS(idEvento);
        
		//Se il caricamento non trova l'entity 
		//si deve bloccare il trasferimento
        if (datiTribunaleSorveglianza == null) {
        	logger.error("DatiTribunaleSorveglianza non trovato");
            throw new LoadException("DatiTribunaleSorveglianza non trovato");
        }
        
        //ID TERZO COLLEGATO, NON SEMPRE PRESENTE
        //MEV 23010 - Viene mappato anche l'id SIES
        List<ProvvedimentoCollegato> idRevocatoList=dao.loadRevocatoTribunaleSorveglianza(idEvento);
        ProvvedimentoCollegato provvedimentoCollegato=null;
        if (idRevocatoList!=null) {
        	//SE PRESENTE ED UNICO LO UTILIZZO
        	//ALTRIMENTI CARICO IL CAMPO NOTE
        	if (idRevocatoList.size()==1)
        		provvedimentoCollegato=idRevocatoList.getFirst();
        	else {
        		//Caricamento delle note
                String note = dao.loadNoteTribunaleSorveglianza(idEvento);
                datiTribunaleSorveglianza.setNote(note);
        	}
        		
        }
        datiTribunaleSorveglianza.setProvvedimentoCollegato(provvedimentoCollegato);
        
        //Caricamento dettagli fascicolo
        it.mig.sies.model.DettagliFascicolo dettagliFascicolo = dao.loadDettagliFascicolo(idEvento);
        //Mapping per l'invio dei dati
        it.mig.sies.type.esecuzione_NEW.DatiTribunaleSorveglianza datiTribunaleSorveglianzaRequest = Mapper.map(datiTribunaleSorveglianza,dettagliFascicolo);
        provvedimento.setDatiTribunaleSorveglianza(datiTribunaleSorveglianzaRequest);
        
        //Caricamento misure di sicurezza
        List<MisuraSicurezza> misuraSicurezzaList=dao.loadMisuraSicurezza(idEvento);
        if (PropertyUtil.checkMisureSicurezza(misuraSicurezzaList))
        	datiTribunaleSorveglianzaRequest.getMisuraSicurezza().addAll(Mapper.mapMS(misuraSicurezzaList));
        
        //Settaggio chiavi provvedimento esecutivo
        ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo = new ChiaviProvvedimentoEsecutivo();
        chiaviProvvedimentoEsecutivo.setNsc(new BigInteger(""+datiTribunaleSorveglianza.getChiaveNSC()));
        chiaviProvvedimentoEsecutivo.setSies(new BigInteger(""+datiTribunaleSorveglianza.getChiaveSies()));
        provvedimento.setChiaviProvvedimentoEsecutivo(chiaviProvvedimentoEsecutivo);
		
	}
	
	/**
	 * Carica i dati dell'Ufficio di Sorveglianza
	 * 
	 * @throws LoadException 
	 * */
	private void loadDatiUfficioSorveglianza(Provvedimento provvedimento,
			SiesDAO dao, String idEvento) throws LoadException {
		logger.info("Caricamento Dati Ufficio Sorveglianza");
		it.mig.sies.model.DatiUfficioSorveglianza datiUfficioSorveglianza = dao.loadUDS(idEvento);
		
		//Se il caricamento non trova l'entity 
		//si deve bloccare il trasferimento
        if (datiUfficioSorveglianza == null) {
        	logger.error("DatiUfficioSorveglianza non trovato");
            throw new LoadException("DatiUfficioSorveglianza non trovato");
        }
        
        //ID TERZO COLLEGATO, NON SEMPRE PRESENTE
        long idRevocato=dao.loadIdRevocatoUfficioSorveglianza(idEvento);
        datiUfficioSorveglianza.setIdProvvRevocato(idRevocato);
        //Caricamento dettagli fascicolo
        it.mig.sies.model.DettagliFascicolo dettagliFascicolo = dao.loadDettagliFascicolo(idEvento);
        //Mapping per l'invio dei dati
        it.mig.sies.type.esecuzione_NEW.DatiUfficioSorveglianza datiUfficioSorveglianzaRequest = Mapper.map(datiUfficioSorveglianza,dettagliFascicolo);
        provvedimento.setDatiUfficioSorveglianza(datiUfficioSorveglianzaRequest);
        
        //Caricamento misure di sicurezza
        List<MisuraSicurezza> misuraSicurezzaList=dao.loadMisuraSicurezza(idEvento);
        if (PropertyUtil.checkMisureSicurezza(misuraSicurezzaList))
        	datiUfficioSorveglianzaRequest.getMisuraSicurezza().addAll(Mapper.mapMS(misuraSicurezzaList));
        
        //Settaggio chiavi provvedimento esecutivo
        ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo = new ChiaviProvvedimentoEsecutivo();
        chiaviProvvedimentoEsecutivo.setNsc(new BigInteger(""+datiUfficioSorveglianza.getChiaveNSC()));
        chiaviProvvedimentoEsecutivo.setSies(new BigInteger(""+datiUfficioSorveglianza.getChiaveSies()));
        provvedimento.setChiaviProvvedimentoEsecutivo(chiaviProvvedimentoEsecutivo);
		
	}
	
	/**
	 * Caricamento della risposta storicizzata su database
	 * */
	public ResponseData loadResponse() {
		ResponseData responseData = dao.loadResponse(idEvento);
        return responseData;
	}
	
	/**
	 * Caricamento delle operazioni storicizzate su database
	 * @throws LoadException 
	 * @throws ProfileException 
	 * */
	public List<ResponseData> loadResponseList() throws ProfileException, LoadException {
		//MEV 35253
		verificaUtente(idUtente);
		List<ResponseData> responseList=dao.loadResponseList(idEvento);
		return responseList;
	}
	
	/**
	 * @return the riepilogoOperazione
	 */
	public RiepilogoOperazione getRiepilogoOperazione() {
		return riepilogoOperazione;
	}

	/**
	 * @param riepilogoOperazione the riepilogoOperazione to set
	 */
	public void setRiepilogoOperazione(RiepilogoOperazione riepilogoOperazione) {
		this.riepilogoOperazione = riepilogoOperazione;
	}
}
