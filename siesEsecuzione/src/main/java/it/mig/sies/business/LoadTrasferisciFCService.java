package it.mig.sies.business;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import it.mig.sies.exception.ControlException;
import it.mig.sies.exception.LoadException;
import it.mig.sies.exception.ProfileException;
import it.mig.sies.model.DatiPubblicoMinistero;
import it.mig.sies.model.DettagliFascicolo;
import it.mig.sies.model.ResponseData;
import it.mig.sies.model.RiepilogoOperazione;
import it.mig.sies.model.Soggetto;
import it.mig.sies.model.TitoloEsecutivo;
import it.mig.sies.model.TitoloGiudiziario;
import it.mig.sies.model.Utente;
import it.mig.sies.type.foglicomplementari.Anagrafica;
import it.mig.sies.type.foglicomplementari.Azione;
import it.mig.sies.type.foglicomplementari.ChiaviProvvedimentoEsecutivo;
import it.mig.sies.type.foglicomplementari.FoglioComplementare;
import it.mig.sies.type.foglicomplementari.RequestData;
import it.mig.sies.util.ApplicationProperties;
import it.mig.sies.util.Mapper;
import it.mig.sies.util.SiesDAO;

/**
 * MEV 16 - Servizio di caricamento dei dati necessari per effettuare la richiesta
 *
 * @author Simone Gioggi
 *
 */
public class LoadTrasferisciFCService {

	private static final Logger logger = Logger.getLogger(LoadTrasferisciFCService.class);

	private String idEvento;
	private String action;
	private String idUtente;
	private SiesDAO dao;
	private RiepilogoOperazione riepilogoOperazione;
	public final static String LOCAL_SEARCH = "SEARCH";
	private String idSoggetto;
	private String idSentenza;
	private String idFascicoloSiep;
	// MEV 16 CUMULO: aggiunte variabili di classe e modificati i costruttori
	private String idSinonimo;

	/**
	 * Costuttore
	 *
	 * @param idEvento
	 */
	public LoadTrasferisciFCService(String idEvento) {

		this.idEvento = idEvento;
		this.dao = SiesDAO.getIstance();
		this.riepilogoOperazione = new RiepilogoOperazione();
	}

	/**
	 * Costruttore usato solo per il trasferimento massivo senza un utente...da modificare
	 *
	 * @param idEvento
	 * @param action
	 */
	public LoadTrasferisciFCService(String idEvento, String action) {

		this.idEvento = idEvento;
		this.action = action;
		this.dao = SiesDAO.getIstance();
		this.riepilogoOperazione = new RiepilogoOperazione();
	}

	/**
	 * Costruttore
	 *
	 * @param idEvento
	 * @param action
	 * @param idUtente
	 * @param idSoggetto
	 * @param idSentenza
	 * @param idFascicoloSiep
	 * @param idSinonimo
	 */
	public LoadTrasferisciFCService(String idEvento, String action, String idUtente, String idSoggetto,
			String idSentenza, String idFascicoloSiep, String idSinonimo) {

		this.idEvento = idEvento;
		this.idUtente = idUtente;
		this.action = action;
		this.dao = SiesDAO.getIstance();
		this.riepilogoOperazione = new RiepilogoOperazione();
		this.idSoggetto = idSoggetto;
		this.idSentenza = idSentenza;
		this.idFascicoloSiep = idFascicoloSiep;
		this.idSinonimo = idSinonimo;
	}

	/**
	 * Metodo principale del servizio di caricamento
	 *
	 * @return List<RequestData>
	 * @throws LoadException
	 * @throws ProfileException
	 * @throws ControlException
	 */
	public List<RequestData> execute() throws LoadException, ProfileException, ControlException {

		logger.info("Inizio caricamento dati dal database locale");
		verificaUtente();
		// valore di ritorno
		return loadData(action);
	}

	/**
	 * Verifica che l'utente sia abilitato al trasferimento
	 *
	 * @throws ProfileException
	 * @throws LoadException
	 */
	private void verificaUtente() throws ProfileException, LoadException {

		logger.info("Verifica profilo per utente " + idUtente);
		boolean abilitato = false;
		List<String> profiliUtente = dao.loadProfiliUtente(idUtente);

		// Caricamento dei profili abilitati in base al tipo di operazione
		String profiliAbilitati = null;
		if (action.equals(Azione.INSERT.toString())) {
			profiliAbilitati = ApplicationProperties.getIstance()
					.getProperty("profili.abilitati.inserimento");
		} else if (action.equals(Azione.DELETE.toString())) {
			profiliAbilitati = ApplicationProperties.getIstance().getProperty("profili.abilitati.modifica");
		} else if (action.equals(Azione.UPDATE.toString())) {
			profiliAbilitati = ApplicationProperties.getIstance()
					.getProperty("profili.abilitati.cancellazione");
		} else if (action.equals(LOCAL_SEARCH)) {
			profiliAbilitati = ApplicationProperties.getIstance().getProperty("profili.abilitati.storico");
		} else {
			logger.error("Azione non definita o riconosciuta");
			throw new LoadException("Azione non definita o riconosciuta");
		}

		// Verifica della presenza del profilo
		StringTokenizer tokenizer = new StringTokenizer(profiliAbilitati, "|");
		while (tokenizer.hasMoreTokens()) {
			if (profiliUtente.contains(tokenizer.nextToken())) {
				abilitato = true;
				break;
			}
		}

		if (!abilitato) {
			throw new ProfileException("Utente [" + idUtente + "] non abilitato al trasferimento");
		}
		logger.info("Utente " + idUtente + " abilitato all'operazione");
	}

	/**
	 * Caricamento delle varie entity
	 *
	 * @param action
	 * @return List<RequestData>
	 * @throws LoadException
	 * @throws ControlException
	 */
	private List<RequestData> loadData(String action) throws LoadException, ControlException {

		try {
			List<RequestData> requestDataList = new ArrayList<>();
			// Caricamento utente
			it.mig.sies.model.Utente utente = loadUtente();
			// Caricamento soggetto
			Soggetto soggetto = loadSoggetto();
			// controllo se trattasi di cumulo
			DettagliFascicolo df = dao.getFascicoloByID(idEvento);
			if (df == null) {
				logger.error(
						"Validare il Procedimento prima di procedere con la trasmissione del Foglio Complementare!");
				String responseCode = "messaggio.errore.controllo.procedimento.non.validato";
				String responseMessage = ApplicationProperties.getIstance().getProperty(responseCode);
				throw new ControlException(responseMessage);
			}

			// controllo se siamo in presenza di cumulo: se si valorizzo una variabile booleana
			boolean isForCumulo = false;
			String codMotivo = df.getCodMotivo();
			// MEV 16 CUMULO: aggiunto metodo di controllo
			isForCumulo = dao.isCumulo(codMotivo);

			// se non trattasi di cumulo, controllo se siamo in presenza di sospensione od avvenuta esecuzione
			// pena: se si valorizzo una variabile booleana
			boolean isAvvenutaEsecuzionePena = false;
			if (!isForCumulo) {
				String codiciAvvenutaEsecuzionePena = ApplicationProperties.getIstance()
						.getProperty("codici.avvenutaesecuzionepena");
				// Verifica della presenza del profilo
				StringTokenizer tokenizerAEP = new StringTokenizer(codiciAvvenutaEsecuzionePena, "|");
				while (tokenizerAEP.hasMoreTokens()) {
					if (codMotivo.equals(tokenizerAEP.nextToken())) {
						isAvvenutaEsecuzionePena = true;
						break;
					}
				}
			}

			// Caricamento lista titoli principali
			List<TitoloGiudiziario> titoloGiudiziarioList = dao.getTitoloGiudiziarioByID(idSentenza,
					// MEV 16 CUMULO: aggiunto parametro di passaggio
					idFascicoloSiep, isForCumulo, idEvento);
			// Caricamento titolo esecutivo
			TitoloEsecutivo titoloEsecutivo = dao.getTitoloEsecutivoByID(idEvento);
			// Popolamento dei dati di riepilogo
			this.riepilogoOperazione.setSoggetto(soggetto);
			this.riepilogoOperazione.setTitoloEsecutivo(titoloEsecutivo);
			this.riepilogoOperazione.setTitoloGiudiziarioList(titoloGiudiziarioList);

			// Mapping per l'invio dei dati
			List<FoglioComplementare> foglioComplementareList = new ArrayList<>();
			// Caricamento provvedimento esecutivo
			loadDettagliProvvedimentoEsecutivo(foglioComplementareList, utente, isForCumulo, df,
					isAvvenutaEsecuzionePena);

			// ciclo sulla lista
			for (FoglioComplementare fc : foglioComplementareList) {
				fc.setAnnoSiep(titoloEsecutivo.getAnnoFascicolo());
				fc.setNumeroSiep(titoloEsecutivo.getNumeroFascicolo());
				it.mig.sies.type.foglicomplementari.Utente utenteRequest = Mapper.mapUtenteFC(utente);
				// MEV 16 CUMULO: aggiunti parametri di passaggio
				Anagrafica anagrafica = Mapper.mapSoggettoFC(soggetto, idSinonimo);

				// Se il provvedimento non ha principali e' lui stesso un principale...
				if ((titoloGiudiziarioList != null) && (titoloGiudiziarioList.size() > 0)) {
					for (TitoloGiudiziario titoloGiudiziario : titoloGiudiziarioList) {
						// MEV 16 CUMULO: aggiunti parametri di passaggio
						fc.getProvvedimentoGiudiziario().add(
								Mapper.mapPGFC(titoloGiudiziario, soggetto.getChiaveSies(), dao, idSinonimo));
					}
					fc.setFlagPrincipale(false);
				} else {
					fc.getProvvedimentoGiudiziario().add(null);
					fc.setFlagPrincipale(true);
				}

				// instanzio ed inizializzo un oggetto di tipo "RequestData"
				RequestData requestData = new RequestData();
				// imposto le proprieta' dell'oggetto
				requestData.setFoglioComplementare(fc);
				requestData.setAnagrafica(anagrafica);
				requestData.setUtente(utenteRequest);
				// MEV 16 CUMULO: aggiunto controllo
				// if (PropertyUtil.isPresent(idSinonimo) && idSinonimo.contains("#TFCCUM")) {
				// String[] split = idSinonimo.split("#");
				// if (split.length < 3)
				// requestData.setAzioneCumulo(split[1]);
				// else
				// requestData.setAzioneCumulo(split[1] + "#" + split[2]);
				// }
				// Gestione della azione da inviare
				manageAction(action, requestData);
				// aggiungo alla lista
				requestDataList.add(requestData);
			}

			// valore di ritorno
			return requestDataList;
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			if (e instanceof ControlException)
				throw new ControlException(e.getMessage());
			else
				throw new LoadException(e);
		}
	}

	/**
	 * Caricamento dei dati relativi al provvedimento esecutivo
	 *
	 * @param foglioComplementareList
	 * @param utente
	 * @param isForCumulo
	 * @param df
	 * @param isAvvenutaEsecuzionePena
	 * @throws LoadException
	 */
	private void loadDettagliProvvedimentoEsecutivo(List<FoglioComplementare> foglioComplementareList,
			Utente utente, boolean isForCumulo, DettagliFascicolo df, boolean isAvvenutaEsecuzionePena)
			throws LoadException {

		// Caricamento del provvedimento esecutivo in base alla tipologia di ufficio
		if ("301".equals(utente.getCodiceTipoUfficio()) || "302".equals(utente.getCodiceTipoUfficio())) {
			loadDatiProvvedimentoPM(foglioComplementareList, df, isForCumulo, isAvvenutaEsecuzionePena);
		} else {
			throw new LoadException(
					"Codice Tipo Ufficio dell'utente non riconosciuto: " + utente.getCodiceTipoUfficio());
		}
	}

	/**
	 * Carica i dati del Pubblico Ministero
	 *
	 * @param foglioComplementareList
	 * @param dettagliFascicolo
	 * @param isForCumulo
	 * @param isAvvenutaEsecuzionePena
	 * @throws LoadException
	 */
	private void loadDatiProvvedimentoPM(List<FoglioComplementare> foglioComplementareList,
			DettagliFascicolo dettagliFascicolo, boolean isForCumulo, boolean isAvvenutaEsecuzionePena)
			throws LoadException {

		logger.info("Caricamento Dati Pubblico Ministero");
		List<DatiPubblicoMinistero> datiPubblicoMinisteroList = dao.loadDatiProvvedimentoPM(idEvento,
				idFascicoloSiep, isForCumulo, isAvvenutaEsecuzionePena);

		// Se il caricamento non trova l'entity si deve bloccare il trasferimento
		if (datiPubblicoMinisteroList == null || datiPubblicoMinisteroList.isEmpty()) {
			logger.error("Dati Pubblico Ministero non trovati");
			throw new LoadException("Dati Pubblico Ministero non trovati");
		}

		// Caricamento dettagli fascicolo
		logger.info("Dettaglio fascicolo: " + dettagliFascicolo.toString());
		// ciclo sul risultato del caricamento dati
		for (int index = 0; index < datiPubblicoMinisteroList.size(); index++) {
			DatiPubblicoMinistero datiPubblicoMinistero = datiPubblicoMinisteroList.get(index);
			String chiaveSies = "", chiaveNSC = "";
			try {
				chiaveSies = "" + datiPubblicoMinistero.getChiaveSies();
				chiaveNSC = "" + datiPubblicoMinistero.getChiaveNSC();
			} catch (NullPointerException npe) {
				logger.info("ChiaveSies non trovata, salto questo record!");
				continue;
			}
			// Mapping per l'invio dei dati
			it.mig.sies.type.foglicomplementari.DatiPubblicoMinistero datiPubblicoMinisteroRequest = Mapper
					.mapDatiProvvedimentoPMFC(datiPubblicoMinistero, dettagliFascicolo);
			// instanzio ed inizializzo un oggetto di tipo "FoglioComplementare"
			FoglioComplementare foglioComplementare = new FoglioComplementare();
			// imposto i dati nel nuovo oggetto
			foglioComplementare.setDatiPubblicoMinistero(datiPubblicoMinisteroRequest);

			// Settaggio chiavi provvedimento esecutivo
			ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo = new ChiaviProvvedimentoEsecutivo();
			chiaviProvvedimentoEsecutivo.setNsc(new BigInteger(chiaveNSC));
			chiaviProvvedimentoEsecutivo.setSies(new BigInteger(chiaveSies));
			foglioComplementare.setChiaviProvvedimentoEsecutivo(chiaviProvvedimentoEsecutivo);
			// aggiungo alla lista
			foglioComplementareList.add(foglioComplementare);
		}
	}

	/**
	 * Gestione della action
	 *
	 * @param action
	 * @param requestData
	 * @throws LoadException
	 */
	private void manageAction(String action, RequestData requestData) throws LoadException {

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
	 * Caricamento del soggetto
	 *
	 * @return Soggetto
	 * @throws LoadException
	 */
	private Soggetto loadSoggetto() throws LoadException {

		Soggetto soggetto = dao.getSoggettoByID(idSoggetto);

		// Se il caricamento non trova l'entity
		// si deve bloccare il trasferimento
		if (soggetto == null) {
			logger.error("Soggetto non trovato");
			throw new LoadException("Soggetto non trovato");
		}
		return soggetto;
	}

	/**
	 * Caricamento dell'utente che ha inserito il provvedimento dell'esecuzione
	 *
	 * @return Utente
	 * @throws LoadException
	 */
	private Utente loadUtente() throws LoadException {

		it.mig.sies.model.Utente utente = null;
		utente = dao.getUtenteByID(idUtente);

		// Se il caricamento non trova l'entity
		// si deve bloccare il trasferimento
		if (utente == null) {
			logger.error("Utente non trovato");
			throw new LoadException("Utente non trovato");
		}
		return utente;
	}

	/**
	 * Caricamento della risposta storicizzata su database
	 *
	 * @return ResponseData
	 */
	public ResponseData loadResponse() {

		ResponseData responseData = dao.loadResponse(idEvento);
		return responseData;
	}

	/**
	 * Caricamento delle operazioni storicizzate su database
	 *
	 * @return List<ResponseData>
	 * @throws LoadException
	 * @throws ProfileException
	 */
	public List<ResponseData> loadResponseList() throws LoadException, ProfileException {

		verificaUtente();
		List<ResponseData> responseList = dao.loadResponseList(idEvento);
		return responseList;
	}

	/**
	 * @return the riepilogoOperazione
	 */
	public RiepilogoOperazione getRiepilogoOperazione() {
		return riepilogoOperazione;
	}

	/**
	 * @param riepilogoOperazione
	 *            the riepilogoOperazione to set
	 */
	public void setRiepilogoOperazione(RiepilogoOperazione riepilogoOperazione) {
		this.riepilogoOperazione = riepilogoOperazione;
	}

}