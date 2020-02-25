package siap.sius.depositosentenza.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.cssa.dao.CSSASqlDAO;
import siap.sico.decodifiche.dao.DecodificheDAO;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.statoesecuzione.action.ICostantiStatoEsecuzione;
import siap.sius.SIUSException;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusSqlDAO;
import siap.sius.depositosentenza.dao.DepositoSentenzaDAO;
import siap.sius.depositosentenza.dao.DepositoSentenzaSqlDAO;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.depositosentenza.model.SentenzaEventoTenoriGProcModel;
import siap.sius.depositosentenza.model.SentenzaEventoTenoriPrescrizioniModel;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sius.documentoallegato.dao.DocumentoAllegatoSqlDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienzaprocedimento.dao.UdienzaProcedimentoDAO;
import siap.sius.util.SIUSLookupRemote;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: DepositoSentenzaController
 * </p>
 * <p>
 * Description: Classe Controller per DepositoSentenza
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class DepositoSentenzaController extends SiapController implements IDepositoSentenza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public boolean ExEsisteDepositoSentenzaByGenProcEccettoTipi(BigDecimal aKey, String[] aTipiDaEscludere)
			throws F3BException {

		Connection lConn = null;
		DepositoSentenzaSqlDAO lDepSenSqlDao = null;
		boolean lEsiste = false;

		try {
			lConn = getDBConnection();
			lDepSenSqlDao = new DepositoSentenzaSqlDAO(lConn);
			int lNum = lDepSenSqlDao.getNumDepoSentenzaByGenProcEccettoTipi(aKey, aTipiDaEscludere);
			if (lNum > 0)
				lEsiste = true;
		} catch (Exception sqe) {
			throw new SIUSException(
					"DepositoSentenzaController.ExEsisteDepositoSentenzaByGenProcEccettoTipi: " + sqe);
		} finally {
			cleanup(lDepSenSqlDao);
			cleanup(lConn);
		}
		return lEsiste;
	}

	/**
	 * Ricerca DepositoSentenza da ANNO, NUM e cod Ufficio.
	 * 
	 * @param aDepositoSentenza
	 * @return
	 * @throws F3BException
	 */

	public DepositoSentenzaModel ExRicercaDepositoSentenzaByAnnoNumUfficio(
			DepositoSentenzaModel aDepositoSentenza) throws F3BException {
		Connection lConn = null;
		DepositoSentenzaDAO lDepDao = null;
		DepositoSentenzaModel lDepMod = null;
		try {
			// Si effettua una ricerca nella Tabella DEPOSITO_SENTENZA
			lConn = getDBConnection();
			lDepDao = new DepositoSentenzaDAO(lConn);
			lDepDao.setCondizione(aDepositoSentenza);
			lDepMod = (DepositoSentenzaModel) lDepDao.getModelByKey();
			if (lDepMod == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Sentenza non trovata");
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("Errore nella ricerca della Sentenza: " + e);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}

		return lDepMod;
	}

	/**
	 * Esegue l'inserimento del Emissione Sentenza.
	 * <p>
	 * Description: Funzione per l'inserimento dell'Emissione di una Sentenza generica.
	 * </p>
	 * Le tabelle coinvolte sono:
	 * <p>
	 * DEPOSITO_SENTENZA : viene inserito il nuovo record sentenza;
	 * <p>
	 * EVENTO : viene inserito un nuovo record;
	 * <p>
	 * TENORE : vengono chiusi i tenori attivi (data_fine) ed inseriti i nuovi tenori;
	 * <p>
	 * GENERALE_PROCEDIMENTO : update del contenuto del procemimento.
	 * 
	 * @param SentenzaEventoTenoriGProcModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public SentenzaEventoTenoriGProcModel ExInserisciSentenza(
			SentenzaEventoTenoriGProcModel aGProcSenEveTenori) throws F3BException {
		// model di ritorno
		SentenzaEventoTenoriGProcModel lModRet = null;
		Connection lConn = null;

		try {
			lConn = getDBTransaction();
			lModRet = ExInserisciSentenza(aGProcSenEveTenori, lConn);

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("DepositoSentenzaController.ExInserisciSentenza: Non posso leggere : "
					+ daoEx);
		} catch (SQLException sqlEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqlEx);
			throw new SIUSException("DepositoSentenzaController.ExInserisciSentenza: Non posso leggere  : "
					+ sqlEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new SIUSException("DepositoSentenzaController.ExInserisciSentenza:" + e);
		}

		finally {
			cleanup(lConn);
		}

		return lModRet;
	}

	/**
	 * Esegue l'inserimento Emissione Sentenza e l'aggiornamento del Fascicolo SIUS origine collegato al
	 * Fascicolo SIUS.
	 * <p>
	 * Description: Funzione per l'inserimento dell'Emissione di una Sentenza generica.
	 * </p>
	 * Le tabelle coinvolte sono:
	 * <p>
	 * DEPOSITO_SENTENZA : viene inserito il nuovo record decreto;
	 * <p>
	 * EVENTO : viene inserito un nuovo record;
	 * <p>
	 * TENORE : vengono chiusi i tenori attivi (data_fine) ed inseriti i nuovi tenori;
	 * <p>
	 * GENERALE_PROCEDIMENTO : update del contenuto del procemimento. FASCICOLO_SIUS : update del ID FASCICOLO
	 * ORIGINE.
	 * 
	 * @param SentenzaEventoTenoriGProcModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public SentenzaEventoTenoriGProcModel ExInserisciSentenza(
			SentenzaEventoTenoriGProcModel aGProcSenEveTenori, BigDecimal IdFascicoloOrigine)
			throws F3BException {
		// model di ritorno
		SentenzaEventoTenoriGProcModel lModRet = null;
		FascicoloSiusDAO lFasSiusDao = null;

		Connection lConn = null;
		try {
			lConn = getDBTransaction();
			lModRet = ExInserisciSentenza(aGProcSenEveTenori, lConn);

			// Aggiorno il fascicolo
			lFasSiusDao = new FascicoloSiusDAO(lConn);
			lFasSiusDao.setIdFascicoloSiusOrigine(IdFascicoloOrigine);
			lFasSiusDao.update();
			lFasSiusDao.stop();
			commit(lConn);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new SIUSException("DepositoSentenzaController.ExInserisciSentenza:" + e);
		} finally {
			cleanup(lConn);
		}

		return lModRet;
	}

	public SentenzaEventoTenoriGProcModel ExInserisciSentenza(
			SentenzaEventoTenoriGProcModel aGProcSenEveTenori, Connection aConn) throws Exception {
		// model di ritorno
		SentenzaEventoTenoriGProcModel lGProcSenEveTenori = new SentenzaEventoTenoriGProcModel(
				aGProcSenEveTenori);

		GeneraleProcedimentoDAO lGenProcDao = null;
		TenoreDAO lTenoreDao = null;
		TenoreSqlDAO lTenoreSqlDao = null;
		DepositoSentenzaDAO lDepSenDao = null;
		EventoDAO lEventoDao = null;
		EventoSqlDAO lSqlDAO = null;

		lGenProcDao = new GeneraleProcedimentoDAO(aConn);
		lTenoreDao = new TenoreDAO(aConn);
		lTenoreSqlDao = new TenoreSqlDAO(aConn);
		lDepSenDao = new DepositoSentenzaDAO(aConn);
		lEventoDao = new EventoDAO(aConn);

		// Update GeneraleProcedimento.
		lGenProcDao.setCodOggettoProcedimento(aGProcSenEveTenori.getGeneraleProcedimento()
				.getCodOggettoProcedimento());
		lGenProcDao.setDataAggiornamento(aGProcSenEveTenori.getGeneraleProcedimento().getDataAggiornamento());
		lGenProcDao.setCodUfficioAggiornamento(aGProcSenEveTenori.getGeneraleProcedimento()
				.getCodUfficioAggiornamento());
		lGenProcDao.setCodOperatoreAggiornamento(aGProcSenEveTenori.getGeneraleProcedimento()
				.getCodOperatoreAggiornamento());
		lGenProcDao.setCondizioneUpdate(aGProcSenEveTenori.getGeneraleProcedimento()
				.getIdGeneraleProcedimento());
		lGenProcDao.update();

		// Insert DepositoSentenza.
		lDepSenDao.setDAOFromModel(aGProcSenEveTenori.getSentenza());
		BigDecimal lIdDepSen = lDepSenDao.insert();
		lDepSenDao.stop();
		lGProcSenEveTenori.getSentenza().setIdDepositoSentenza(lIdDepSen);

		// -- Parte Gestione Tenori --//
		BigDecimal lIdGenProc = aGProcSenEveTenori.getGeneraleProcedimento().getIdGeneraleProcedimento();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Fase di chiusura per il Tenore");
		TenoreModel lTenore = new TenoreModel();
		// Valorizzazione dei campi da aggiornare + update
		lTenore.setCodOperatoreAggiornamento(lGProcSenEveTenori.getGeneraleProcedimento()
				.getCodOperatoreAggiornamento());
		lTenore.setCodUfficioAggiornamento(lGProcSenEveTenori.getGeneraleProcedimento()
				.getCodUfficioAggiornamento());
		lTenore.setDataAggiornamento(lGProcSenEveTenori.getGeneraleProcedimento().getDataAggiornamento());
		lTenore.setDataFine(lGProcSenEveTenori.getGeneraleProcedimento().getDataAggiornamento());
		lTenore.setGenPridGeneraleProcedimento(lIdGenProc);
		lTenoreDao.setDAOFromModelForUpdateDataFine(lTenore);
		lTenoreDao.update();
		lTenoreDao.stop();

		// Insert dei tenori.
		TenoreModel[] lTenori = lGProcSenEveTenori.getTenori();
		int lCount = lTenori.length;
		for (int x = 0; x < lCount; x++) {
			lTenori[x].setGenPridGeneraleProcedimento(lIdGenProc);
			lTenori[x].setDepIdDepositoSentenza(lIdDepSen);
			lTenori[x].setData(lGProcSenEveTenori.getEvento().getDataEmissione());
			lTenoreDao.setDAOFromModel(lTenori[x]);
			lTenori[x].setIdTenore(lTenoreDao.insert());
			lTenoreDao.stop();
		}
		lGProcSenEveTenori.setTenori(lTenori);

		// Select dati dal tenore + significativo.
		lTenoreSqlDao.ricercaTenoriBySentenzaOrderByPeso(lIdDepSen);
		TenoreModel lTenoreMod = (TenoreModel) lTenoreSqlDao.getModelByKey();
		if (lTenoreMod == null)
			throw new SIUSException("DepositoSentenzaController.ExInserisciSentenza: tenori assenti ");

		EventoModel lEventoModel = new EventoModel(lGProcSenEveTenori.getEvento());

		// Insert Evento
		// Imposta COD_MOTIVO e IdTenore, nel model.
		lEventoModel.setCodMotivo(lTenoreMod.getCodOggettoTenore());
		lEventoModel.setTenIdTenore(lTenoreMod.getIdTenore());
		if (lEventoModel.getCodEsito() != null && lEventoModel.getCodEsito().trim().length() > 0)
			lEventoModel.setCodEsito(lEventoModel.getCodEsito());
		else
			lEventoModel.setCodEsito(lTenoreMod.getCodEsitoTenore());

		// Setto l'anno e il progressivo...
		lSqlDAO = new EventoSqlDAO(aConn);
		BigDecimal lProgr = lSqlDAO.getProgressivo(lEventoModel);
		lEventoModel.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));
		// 06/04/2011 Modifica x Visibilità Stato di Esecuzione.
		lEventoModel.setFasSieIdFascicoloSiep(null);

		lEventoDao.setDAOFromModel(lEventoModel);
		BigDecimal lIdEvento = lEventoDao.insert();
		lEventoDao.stop();
		lEventoModel.setIdEvento(lIdEvento);
		lGProcSenEveTenori.setEvento(lEventoModel);

		// Nel caso di Revoca si effettua l'update dell'Evento Revocato
		if (lEventoModel.getEveIdEvento() != null) {
			lEventoDao.setDataAggiornamento(lEventoModel.getDataInserimento());
			lEventoDao.setCodUfficioAggiornamento(lEventoModel.getCodUfficioInserimento());
			lEventoDao.setCodOperatoreAggiornamento(lEventoModel.getCodOperatoreInserimento());
			lEventoDao.setEveIdEventoRevoca(lEventoModel.getIdEvento());
			lEventoDao.selCondizioneUpdate(lEventoModel.getEveIdEvento());
			lEventoDao.update();
			lEventoDao.stop();
		}

		// Effettua update del campo evento_generato
		lDepSenDao.setCondizioneUpdate(lIdDepSen);
		lDepSenDao.setIdEventoGenerato(lIdEvento);
		lDepSenDao.update();
		lGProcSenEveTenori.getSentenza().setIdEventoGenerato(lIdEvento);

		cleanup(lGenProcDao);
		cleanup(lTenoreDao);
		cleanup(lTenoreSqlDao);
		cleanup(lDepSenDao);
		cleanup(lEventoDao);
		cleanup(lSqlDAO);

		return lGProcSenEveTenori;
	}

	/**
	 * Esegue la ricerca di una Sentenza aggregando dati : Evento e Tenori, per la chiave idEvento.
	 * <p>
	 * 
	 * @param aIdEvento
	 *            id evento per ricerca Sentenza.
	 * @return ritorna i dati di ricerca come aggregato di model.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public SentenzaEventoTenoriPrescrizioniModel ExRicercaSentenzaEventoTenoriPrescrizioniByIdEvento(
			BigDecimal aIdEvento) throws F3BException {
		Connection lConn = null;

		DepositoSentenzaSqlDAO lDepSenSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		SentenzaEventoTenoriPrescrizioniModel lSenEveTenPreMod = new SentenzaEventoTenoriPrescrizioniModel();

		try {
			lConn = getDBConnection();

			// Preleva L'EVENTO
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);

			lSenEveTenPreMod.setEvento((EventoModel) lEveSqlDao.getModelByKey());

			// Dati Sentenza.
			lDepSenSqlDao = new DepositoSentenzaSqlDAO(lConn);
			DepositoSentenzaModel lDepSenMod = new DepositoSentenzaModel();
			lDepSenSqlDao.ricercaDepositoSentenzaByIdEveGenerato(aIdEvento);
			lDepSenMod = (DepositoSentenzaModel) lDepSenSqlDao.getModelByKey();
			// Controllo di esistenza Sentenza.
			if (lDepSenMod == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Nessuna Sentenza per l'evento selezionato.");

			lSenEveTenPreMod.setSentenza(lDepSenMod);

			// Preleva i Tenori.
			lTenSqlDao = new TenoreSqlDAO(lConn);
			// Ricerca Tenori x ID DepositoSentenza
			lTenSqlDao.ricercaTenoriBySentenzaOrderByPeso(lDepSenMod.getIdDepositoSentenza());
			Vector lTenori = new Vector(lTenSqlDao.getModels());
			lSenEveTenPreMod.setTenori((TenoreModel[]) lTenori.toArray(new TenoreModel[0]));

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoSentenzaController.ExRicercaSentenzaEventoTenoriPrescrizioniByIdEvento - Non posso leggere : "
							+ daoEx);
//		} catch (SQLException sqe) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//			siesLogger.debug("SQLException: " + sqe);
//			throw new SIUSException(
//					"DepositoSentenzaController.ExRicercaSentenzaEventoTenoriPrescrizioniByIdEvento - Non posso leggere  : "
//							+ sqe);
		} finally {
			cleanup(lDepSenSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}

		return lSenEveTenPreMod;
	}

	/**
	 * Esegue la ricerca del Deposito Sentenza per l'id Evento.
	 * 
	 * @param aEveKey
	 *            id Evento.
	 * @return dati della Sentenza.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public DepositoSentenzaModel ExRicercaDepositoSentenzaByEvento(BigDecimal aEveKey) throws F3BException {
		Connection lConn = null;
		DepositoSentenzaSqlDAO lDepSenDao = null;
		DepositoSentenzaModel lDepSenMod;

		try {
			lConn = getDBConnection();
			lDepSenDao = new DepositoSentenzaSqlDAO(lConn);
			lDepSenDao.ricercaDepositoSentenzaByIdEveGenerato(aEveKey);
			lDepSenMod = (DepositoSentenzaModel) lDepSenDao.getModelByKey();
			if (lDepSenMod != null)
				lDepSenMod = RicercaUffici(lDepSenMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoSentenzaController.ExRicercaDepositoSentenzaByEvento: Non posso leggere : "
							+ daoEx);
//		} catch (SQLException sqe) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//			siesLogger.debug("SQLException: " + sqe);
//			throw new F3BException(
//					"DepositoSentenzaController.ExRicercaDepositoSentenzaByEvento: Non posso leggere  : "
//							+ sqe);
		} finally {
			cleanup(lDepSenDao);
			cleanup(lConn);
		}
		return lDepSenMod;
	}

	private DepositoSentenzaModel RicercaUffici(DepositoSentenzaModel aDepSentenza) throws F3BException {
		aDepSentenza.setDescrUfficioInserimento(RicercaComunebyUfficio(aDepSentenza
				.getCodUfficioInserimento()));
		aDepSentenza.setDescrUfficioAggiornamento(RicercaComunebyUfficio(aDepSentenza
				.getCodUfficioAggiornamento()));

		return aDepSentenza;
	}

	/*
	 * Funzione di utility. Ricava la Descrizione del comune dal codice dell'ufficio.
	 */
	private String RicercaComunebyUfficio(String aCodUfficio) throws F3BException {
		String lDescComune = null; // Stringa restituita
		if (aCodUfficio != null && aCodUfficio.compareTo("-") != 0) {
			IUfficio lUff = null; // Interfaccia al Controller Ufficio
			lUff = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUfficio = null;

			// Preleva Ufficio Competente
			lUfficio = lUff.getUfficioByKey(aCodUfficio);
			lDescComune = lUfficio.getDescrComune();
		}
		return lDescComune;
	}

	public void ExCancellaDepositoSentenza(DepositoSentenzaModel aDepSen) throws F3BException {

		Connection lConn = null;

		try {
			lConn = getDBConnection();
			ExCancellaDepositoSentenza(aDepSen, lConn);
			commit(lConn);
		} catch (F3BException FEx) {
			rollback(lConn);
			throw FEx;
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("DepositoSentenzaController.ExCancellaDepositoSentenza: " + e);
		} finally {
			cleanup(lConn);
		}
	}

	private void ExCancellaDepositoSentenza(DepositoSentenzaModel aDepSen, Connection aConn)
			throws F3BException {

		DepositoSentenzaDAO lDepDao = null;
		TenoreDAO lTenDao = null;
		EventoDAO lEveDao = null;
		UdienzaProcedimentoDAO udienzaProcedimentoDao = null;

		BigDecimal lIdDepSen = aDepSen.getIdDepositoSentenza();
		try {
			// update Tenori collegati
			lTenDao = new TenoreDAO(aConn);
			lTenDao.setDAOForDeleteDepSen(aDepSen);
			lTenDao.update();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(">>>> Aggiornati tenori collegati a DepSentenza " + lIdDepSen);

			// cancellazione Deposito Sentenza
			lDepDao = new DepositoSentenzaDAO(aConn);
			lDepDao.setCondizioneUpdate(lIdDepSen);
			lDepDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellato DepSentenza " + lIdDepSen);

			// Istanzia EventoDAO
			lEveDao = new EventoDAO(aConn);

			// Cancellazione dei riferimenti in tabella Evento al record cancellato
			EventoModel lEvento = new EventoModel();
			lEvento.setCodOperatoreAggiornamento(aDepSen.getCodOperatoreAggiornamento());
			lEvento.setCodUfficioAggiornamento(aDepSen.getCodUfficioAggiornamento());
			lEvento.setDataAggiornamento(aDepSen.getDataAggiornamento());
			lEvento.setIdEvento(aDepSen.getIdEventoGenerato());

			// Cancellazione ai riferimenti tramite EVE_ID_EVENTO E EVE_ID_EVENTO_REVOCA
			lEveDao.updateDAOFromModelForResetRifEve(lEvento);

			// MERGE v10: cancellazione preventiva
			udienzaProcedimentoDao = new UdienzaProcedimentoDAO(aConn);
			udienzaProcedimentoDao.setCondizioneByIdEvento(aDepSen.getIdEventoGenerato());
			udienzaProcedimentoDao.delete();

			// cancellazione Evento collegato
			lEveDao.selCondizioneUpdate(aDepSen.getIdEventoGenerato());
			lEveDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellato evento collegato a DepSentenza " + lIdDepSen);

		} catch (DAOException daoEx) {
			throw new F3BException("DepositoSentenzaController.ExCancellaDepositoSentenza: " + daoEx);
//		} catch (SQLException sqe) {
//			throw new F3BException("DepositoSentenzaController.ExCancellaDepositoSentenza : " + sqe);
		} catch (Exception e) {
			throw new F3BException("DepositoSentenzaController.ExCancellaDepositoSentenza: " + e);
		}

		finally {
			cleanup(lTenDao);
			cleanup(lEveDao);
			cleanup(lDepDao);
		}
	}

	public void ExCancellaRimessioneAtti(DepositoSentenzaModel aDepSen) throws F3BException {

		Connection lConn = null;

		try {
			lConn = getDBConnection();
			ExCancellaRimessioneAtti(aDepSen, lConn);
			commit(lConn);
		} catch (F3BException FEx) {
			rollback(lConn);
			throw FEx;
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("DepositoSentenzaController.ExCancellaRimessioneAtti: " + e);
		} finally {
			cleanup(lConn);
		}
	}

	public void ExCancellaRimessioneAtti(DepositoSentenzaModel aDepSen, Connection aConn) throws F3BException {
		DepositoSentenzaDAO lDepDao = null;
		TenoreDAO lTenDao = null;
		NotificaDAO lNotDao = null;
		EventoDAO lEveDao = null;

		BigDecimal lIdDepSen = aDepSen.getIdDepositoSentenza();
		BigDecimal lIdEvento = aDepSen.getIdEventoGenerato();
		try {

			// update Tenori collegati
			lTenDao = new TenoreDAO(aConn);
			lTenDao.setDAOForDeleteDepSen(aDepSen);
			lTenDao.update();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(">>>> Aggiornati tenori collegati a DepSentenza " + lIdDepSen);

			// cancellazione Deposito Sentenza
			lDepDao = new DepositoSentenzaDAO(aConn);
			lDepDao.setCondizioneUpdate(lIdDepSen);
			lDepDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellato DepSentenza " + lIdDepSen);

			// Cancellazione Notifiche
			lNotDao = new NotificaDAO(aConn);
			lNotDao.setCondizioneEvento(lIdEvento);
			lNotDao.delete();
			lNotDao.stop();

			// Istanzia EventoDAO
			lEveDao = new EventoDAO(aConn);

			// Cancellazione dei riferimenti in tabella Evento al record cancellato
			EventoModel lEvento = new EventoModel();
			lEvento.setCodOperatoreAggiornamento(aDepSen.getCodOperatoreAggiornamento());
			lEvento.setCodUfficioAggiornamento(aDepSen.getCodUfficioAggiornamento());
			lEvento.setDataAggiornamento(aDepSen.getDataAggiornamento());
			lEvento.setIdEvento(aDepSen.getIdEventoGenerato());

			// Cancellazione ai riferimenti tramite EVE_ID_EVENTO E EVE_ID_EVENTO_REVOCA
			lEveDao.updateDAOFromModelForResetRifEve(lEvento);

			// cancellazione Evento collegato
			lEveDao.selCondizioneUpdate(aDepSen.getIdEventoGenerato());
			lEveDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellato evento collegato a DepSentenza " + lIdDepSen);

		} catch (DAOException daoEx) {
			throw new F3BException("DepositoSentenzaController.ExCancellaRimessioneAtti: " + daoEx);
//		} catch (SQLException sqe) {
//			throw new F3BException("DepositoSentenzaController.ExCancellaRimessioneAtti : " + sqe);
		} catch (Exception e) {
			throw new F3BException("DepositoSentenzaController.ExCancellaRimessioneAtti: " + e);
		}

		finally {
			cleanup(lTenDao);
			cleanup(lEveDao);
			cleanup(lDepDao);

		}
	}

	/**
	 * Esecuzione stampa Emissione Sentenza
	 * 
	 * @param lEvento
	 * @param aCodUff
	 * @param aUtenteModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampEmissioneSentenza(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException {
		ByteArrayOutputStream lByteArrayOut = null;

		// Generazione documento di stampa
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();

		// Da Implementare
		lByteArrayOut = lCtrlSta.ExPreStampaEmissioneSentenza(lEvento, aCodUff, aUtenteModel);

		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(">>>>>> Generato il Documento .");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("EVENTO >>> " + lEvento.toString());
		// Si imposta il ByteArrayInput ovverro il doc generato nell'evento
		// precisamente nel attributo DocBlobIn.
		lEvento.setDocBlobIn(lByteArrayInput);

		// Inserisce il documento generato nel model di ritorno
		// In esso inserisce il Nome del template di ritorno
		// e il documento generato.

		Connection lConn = null;
		EventoDAO lEveDao = null;

		try {
			// Preleva connessione dal Db
			lConn = getDBConnection();

			// Prepara un EventoDAO
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(lEvento);

			// Seleziona le condizioni di Update
			lEveDao.selCondizioneUpdate(lEvento.getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);
			throw new F3BException("DepositoSentenzaController.ExStampEmissioneSentenza: " + daoex);
//		} catch (SQLException sqex) {
//			rollback(lConn);
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//			siesLogger.debug("SQLException: " + sqex);
//			throw new F3BException("DepositoSentenzaController.ExStampEmissioneSentenza: " + sqex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Inserisce la data di deposito della Sentenza, aggiorna l'evento e inserisce una notifica per ogni
	 * destinatario.
	 * <p>
	 * 
	 * @param aFasGPMod
	 *            dati del fasciclo GP Model.
	 * @param aDepositoSentenza
	 *            dati di Deposito Sentenza.
	 * @param aEveNot
	 *            dati di eventonotifica
	 * @return DocumentoAllegatoModel.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public DocumentoAllegatoModel ExInserisciDataDepositoSentenza(FascicoloGPModel aFasGPMod,
			DepositoSentenzaModel aDepositoSentenza, EventoNotificaModel aEveNot, String[] lCheck,
			ScadenzarioSiusModel lScadenzarioSiusModPrincipal, ScadenzarioSiusModel lScadenzarioSiusModSecond)
			throws F3BException {
		AutoritaEsternaDAO lAutDao = null;
		DepositoSentenzaDAO lDepDao = null;
		DepositoSentenzaSqlDAO lDepDaoSql = null;
		Connection lConn = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDao = null;
		DocumentoAllegatoModel lDocAMod = null;
		DecodificheDAO lDecDao = null;
		FascicoloSiusDAO lFasSiusDao = null;

		DepositoSentenzaModel lDepMod = new DepositoSentenzaModel(aDepositoSentenza);

		try {
			// Connessione
			lConn = getDBTransaction();

			// Viene istanziato il DAO per l'update di DEPOSITO_SENTENZA
			lDepDao = new DepositoSentenzaDAO(lConn);

			if (lDepMod.getNumSentenza() == null) {
				// e' il primo inserimento
				// Si valorizza l'Anno corrente perchè il progressivo è calcolato per anno
				lDepMod.setAnnoSentenza(new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));

				// Trova il valore da assegnare al progressivo NUM_SENTENZA.
				lDepDaoSql = new DepositoSentenzaSqlDAO(lConn);
				lDepDaoSql.getProgressivoNumSentenza(lDepMod);
				lDepDaoSql.start();

				BigDecimal lBigDec = new BigDecimal(0);
				if (lDepDaoSql.next() && (lDepDaoSql.getBigDecimal("aMAX") != null))
					lBigDec = lDepDaoSql.getBigDecimal("aMAX");
				lDepDaoSql.stop();

				if (lBigDec == null)
					lBigDec = new BigDecimal(0);

				// Il campo NUM_SENTENZA viene valorizzato con l'ultimo valore presente + 1
				// Setto il NUM_SENTENZA del Model di DepositoSentenza con il MAX + 1
				lDepMod.setNumSentenza(new BigDecimal(lBigDec.intValue() + 1));

				// Update di ANNO e PROGR viene eseguito solo la prima volta
				lDepDao.setAnnoSentenza(lDepMod.getAnnoSentenza());
				lDepDao.setNumSentenza(lDepMod.getNumSentenza());
			}

			// Attraverso il DAO si realizza l'UPDATE

			// Effettuo l'inserimento data deposito in DepositoSentenzaModel;
			// carico i dati da aggiornare.
			lDepDao.setCodUfficioAggiornamento(lDepMod.getCodUfficioAggiornamento());
			lDepDao.setCodOperatoreAggiornamento(lDepMod.getCodOperatoreAggiornamento());
			lDepDao.setDataAggiornamento(lDepMod.getDataAggiornamento());
			lDepDao.setDataDeposito(lDepMod.getDataDeposito());
			lDepDao.setCondizioneUpdate(lDepMod.getIdDepositoSentenza());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Fase di aggiornamento per DepositoSentenza (Data Deposito)");
			lDepDao.update();

			// Update di Evento.
			EventoNotificaModel lEveNot = new EventoNotificaModel(aEveNot);
			EventoModel lEveMod = new EventoModel(lEveNot.getEvento());

			lEveMod.setFlagVideoSiep("");
			lEveMod.setFlagStampaSiep("");
			DecodificheModel lDecodifiche = new DecodificheModel();
			if (aFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				lDecDao = new DecodificheDAO(lConn);
				lDecDao.setCondizioneContestoRwLowValue("OGGETTO_PROCEDIMENTO", aFasGPMod
						.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
				lDecodifiche = (DecodificheModel) lDecDao.getModelByKey();
				if (lDecodifiche.getCodiceAlt2() != null
						&& lDecodifiche.getCodiceAlt2().compareTo(
								ICostantiStatoEsecuzione.STATO_ESECUZIONE_RISTRETTO) == 0) {
					lEveMod.setFlagVideoSiep("S");
					lEveMod.setFlagStampaSiep("S");
					lEveMod.setFasSieIdFascicoloSiep(aFasGPMod.getFascicoloSiusModel()
							.getFasSieIdFascicoloSiep());
				} else if (lDecodifiche.getCodiceAlt2() != null
						&& lDecodifiche.getCodiceAlt2().compareTo(
								ICostantiStatoEsecuzione.STATO_ESECUZIONE_ESTESO) == 0) {
					lEveMod.setFlagVideoSiep("N");
					lEveMod.setFlagStampaSiep("N");
					lEveMod.setFasSieIdFascicoloSiep(aFasGPMod.getFascicoloSiusModel()
							.getFasSieIdFascicoloSiep());
				}
			}

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(lEveMod);
			lEveDao.update();

			// Fase di Insert del Documento Allegato.

			// Occorre cancellare eventuali DocumentiAllegati preesistenti
			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setCondizioneDelete(lEveMod.getIdEvento(), "01");
			lDocAllDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(
					">>>> Cancellati doc allegati collegati a Evento " + lEveMod.getIdEvento());

			// Setto il NumeroProgressivo del Model di DocumentoAllegato con il MAX + 1 (per Uff. Inserimento
			// ed IdEvento).
			lDocAMod = new DocumentoAllegatoModel();
			lDocAMod.setCodUfficioInserimento(lDepMod.getCodUfficioAggiornamento());
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAllSqlDao = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal lBigDecAll = lDocAllSqlDao.getProgressivo(lDocAMod);

			lDocAMod.setNumeroProgressivo(new BigDecimal(lBigDecAll.intValue() + 1));
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAMod.setDataEmissione(lDepMod.getDataDeposito());
			lDocAMod.setCodTipoDocumento("01");
			lDocAMod.setFlagDocumentoRegistrato("N");
			lDocAMod.setCodUfficioInserimento(lDepMod.getCodUfficioAggiornamento());
			lDocAMod.setCodOperatoreInserimento(lDepMod.getCodOperatoreAggiornamento());
			lDocAMod.setDataInserimento(lDepMod.getDataAggiornamento());
			lDocAMod.setTemIdTemplate("SIUS_SE_016");
			lDocAllDao.setDAOFromModel(lDocAMod);
			lDocAMod.setIdDocumentoAllegato(lDocAllDao.insert());

			// Cancellazione delle notifiche preesistenti per le notifiche selezionate.
			lNotDao = new NotificaDAO(lConn);
			if (lCheck != null) {
				for (int z = 0; z < lCheck.length; z++) {
					lNotDao.start();
					lNotDao.setCondizioneUpdate((BigDecimal) new BigDecimal(lCheck[z].toUpperCase()));
					lNotDao.delete();
					lNotDao.stop();
				}
			}

			// Insert delle Notifiche.
			lAutDao = new AutoritaEsternaDAO(lConn);

			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (lEveNot.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Presenti " + lEveNot.getNotifiche().length + " notifiche");

				while (count < lEveNot.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + lEveNot.getNotifiche()[count]);

					if (lEveNot.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(lEveNot.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(lEveNot.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					lNotDao.setDAOFromModel(lEveNot.getNotifiche()[count]);

					lNotDao.insert();
					lNotDao.stop();

					count++;
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Inserite Notifiche per Evento" + lEveMod.getIdEvento());
			}

			// Viene aggiornato lo stato del fascicolo.
			lFasSiusDao = new FascicoloSiusDAO(lConn);
			lFasSiusDao.setDAOFromModelForUpdate(aFasGPMod.getFascicoloSiusModel());
			// MERGE v10: aggiunto controllo preventivo
			if (lEveMod.getCodEsito().compareTo("0603") == 0) {
				lFasSiusDao.setCodStatoFascicolo("02");
			} else if (lEveMod.getCodEsito().compareTo("0605") != 0) {
				lFasSiusDao.setCodStatoFascicolo("07");
			} else if (aFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("07") != 0) {
				lFasSiusDao.setCodStatoFascicolo("13");
			}
			lFasSiusDao.update();
			lFasSiusDao.stop();

			commit(lConn);

		}

		catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);
			throw new SIUSException("DepositoSentenzaController.ExInserisciDataDepositoSentenza: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new SIUSException("DepositoSentenzaController.ExInserisciDataDepositoSentenza: " + ex);
		} finally {
			cleanup(lDepDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lDocAllDao);
			cleanup(lDocAllSqlDao);
			cleanup(lDepDaoSql);
			cleanup(lDecDao);
			cleanup(lConn);
		}
		// Restituito il model del documento allegato inserito.
		return lDocAMod;
	}

	/**
	 * Inserisce la data di deposito della Sentenza, aggiorna l'evento e inserisce una notifica per ogni
	 * destinatario.
	 * 
	 * @param aFasGPMod
	 * @param aDepositoSentenza
	 * @param aEveNot
	 * @return DocumentoAllegatoModel
	 * @throws F3BException
	 */
	public DocumentoAllegatoModel ExModificaDataDepositoSentenza(FascicoloGPModel aFasGPMod,
			DepositoSentenzaModel aDepositoSentenza, EventoNotificaModel aEveNot, String[] lCheck)
			throws F3BException {
		Connection lConn = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		NotificaDAO lNotDaoCanc = null;
		AutoritaEsternaDAO lAutDao = null;
		DepositoSentenzaDAO lDecDao = null;
		DepositoSentenzaSqlDAO lDecDaoSql = null;
		AvvocatoFascicoloSiusSqlDAO lAvvDaoSql = null;
		FascicoloGPSqlDAO lFasDao = null;
		MisuraAlternativaSqlDAO lMASqlDao = null;
		MisuraAlternativaDAO lMADao = null;
		CSSASqlDAO lCSSADao = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDao = null;
		DocumentoAllegatoModel lDocAMod = null;

		DepositoSentenzaModel lDecMod = new DepositoSentenzaModel(aDepositoSentenza);

		try {
			lConn = getDBTransaction();

			// Setto il DAO dal Model di DepositoSentenza per l'Update
			lDecDao = new DepositoSentenzaDAO(lConn);

			// Effettuo l'inserimento data deposito in DepositoSentenzaModel;
			// carico i dati da aggiornare.
			lDecDao.setCodUfficioAggiornamento(lDecMod.getCodUfficioAggiornamento());
			lDecDao.setCodOperatoreAggiornamento(lDecMod.getCodOperatoreAggiornamento());
			lDecDao.setDataAggiornamento(lDecMod.getDataAggiornamento());
			lDecDao.setDataDeposito(lDecMod.getDataDeposito());
			lDecDao.setCondizioneUpdate(lDecMod.getIdDepositoSentenza());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Fase di aggiornamento per DepositoSentenza (Data Deposito)");
			lDecDao.update();

			// Update di Evento.
			EventoNotificaModel lEveNot = new EventoNotificaModel(aEveNot);
			EventoModel lEveMod = new EventoModel(lEveNot.getEvento());

			// STUB: Se poi bisogna trasferire l'evento tocca settare i flag x SIEP
			lEveMod.setFlagVideoSiep("S");
			lEveMod.setFlagStampaSiep("S");

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(lEveMod);
			lEveDao.update();

			// Fase di Insert del Documento Allegato.

			// Occorre cancellare eventuali DocumentiAllegati preesistenti
			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setCondizioneDelete(lEveMod.getIdEvento(), "01");
			lDocAllDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(
					">>>> Cancellati doc allegati collegati a Evento " + lEveMod.getIdEvento());

			// Setto il NumeroProgressivo del Model di DocumentoAllegato con il MAX + 1 (per Uff. Inserimento
			// ed IdEvento).
			lDocAMod = new DocumentoAllegatoModel();
			lDocAMod.setCodUfficioInserimento(lDecMod.getCodUfficioAggiornamento());
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAllSqlDao = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal lBigDecAll = lDocAllSqlDao.getProgressivo(lDocAMod);

			lDocAMod.setNumeroProgressivo(new BigDecimal(lBigDecAll.intValue() + 1));
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAMod.setDataEmissione(lDecMod.getDataDeposito());
			lDocAMod.setCodTipoDocumento("01");
			lDocAMod.setFlagDocumentoRegistrato("N");
			lDocAMod.setCodUfficioInserimento(lDecMod.getCodUfficioAggiornamento());
			lDocAMod.setCodOperatoreInserimento(lDecMod.getCodOperatoreAggiornamento());
			lDocAMod.setDataInserimento(lDecMod.getDataAggiornamento());
			lDocAMod.setTemIdTemplate("SIUS_SE_016");
			lDocAllDao.setDAOFromModel(lDocAMod);
			lDocAMod.setIdDocumentoAllegato(lDocAllDao.insert());

			// Update delle Notifiche.
			lAutDao = new AutoritaEsternaDAO(lConn);

			BigDecimal lKeyAutorita = null;
			int count = 0;
			if (lEveNot.getNotifiche() != null) {

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Presenti " + lEveNot.getNotifiche().length + " notifiche");

				while (count < lEveNot.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + lEveNot.getNotifiche()[count]);

					if (lEveNot.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(lEveNot.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(lEveNot.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					lNotDao = new NotificaDAO(lConn);
					lNotDao.setDAOFromModelForUpdate(lEveNot.getNotifiche()[count]);
					lNotDao.update();

					count++;
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Modificate Notifiche per Evento" + lEveMod.getIdEvento());

				// Cancellazione delle notifiche preesistenti per le notifiche selezionate.
				lNotDaoCanc = new NotificaDAO(lConn);
				if (lCheck != null) {
					for (int z = 0; z < lCheck.length; z++) {
						lNotDaoCanc.start();
						lNotDaoCanc.setCondizioneUpdate((BigDecimal) new BigDecimal(lCheck[z].toUpperCase()));
						lNotDaoCanc.delete();
						lNotDaoCanc.stop();
					}
				}
			}
			commit(lConn);

		}

		catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("DepositoSentenzaController.ExModificaDataDepositoSentenza: " + daoEx);
//		} catch (SQLException sqe) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//			siesLogger.debug("SQLException: " + sqe);
//			rollback(lConn);
//			throw new F3BException("DepositoSentenzaController.ExModificaDataDepositoSentenza: " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("DepositoOrdinanzaController.ExModificaDataDepositoOrdinanza: " + ex);
		} finally {
			cleanup(lDecDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lNotDaoCanc);
			cleanup(lAutDao);
			cleanup(lDecDaoSql);
			cleanup(lAvvDaoSql);
			cleanup(lFasDao);
			cleanup(lMASqlDao);
			cleanup(lMADao);
			cleanup(lCSSADao);
			cleanup(lDocAllDao);
			cleanup(lDocAllSqlDao);

			cleanup(lConn);
		}
		// Restituito il model del documento allegato inserito.
		return lDocAMod;
	}

	/**
	 * Stamapa il documento allegato alla Sentenza
	 * 
	 * @param aIdFascicoloSius
	 * @param aDAMod
	 * @param aCodUff
	 * @param aUtenteModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumentoAllegato(BigDecimal aIdFascicoloSius,
			DocumentoAllegatoModel aDAMod, String aCodUff, UtenteModel aUtenteModel) throws F3BException {
		Connection lConn = null;
		DocumentoAllegatoDAO lDADao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			// Generazione documento di stampa
			IStampaSius lCtrlSt = SIUSLookupRemote.getStampaRemote();
			lByteArrayOut = lCtrlSt.ExPreStampaAllegato(aIdFascicoloSius, aDAMod, aCodUff, aUtenteModel);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("DOCUMENTO_ALLEGATO >>> " + aDAMod.toString());

			aDAMod.setDocBlobIn(lByteArrayInput);

			lConn = getDBConnection();
			lDADao = new DocumentoAllegatoDAO(lConn);
			lDADao.setDAOFromModelForUpdateBlob(aDAMod);

			lDADao.setCondizioneUpdate(aDAMod.getIdDocumentoAllegato());
			lDADao.update();
			commit(lConn);
		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqe);
			throw new F3BException("DepositoSentenzaController.ExStampaDocumentoAllegato: " + sqe);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException("DepositoSentenzaController.ExStampaDocumentoAllegato: " + sqe);
		} finally {
			cleanup(lDADao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	public DepositoSentenzaModel ExRicercaSentenzaRimessioneAttiPcByKeyPerUpdate(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		DepositoSentenzaSqlDAO lDepDao = null;
		DepositoSentenzaModel lDepMod;

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoSentenzaSqlDAO(lConn);
			lDepDao.ricercaSentenzaRimessioneAttiByKeyPerUpdate(aKey);
			lDepMod = (DepositoSentenzaModel) lDepDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoSentenzaController.ExRicercaSentenzaRimessioneAttiPcByKeyPerUpdate: Non posso leggere: "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	public DepositoSentenzaModel ExModificaDepositoSentenza(DepositoSentenzaModel aDepositoSentenza)
			throws F3BException {
		Connection lConn = null;
		DepositoSentenzaDAO lDepDao = null;
		DepositoSentenzaModel lDepMod = new DepositoSentenzaModel(aDepositoSentenza);

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoSentenzaDAO(lConn);
			lDepDao.setDAOFromModelForUpdate(aDepositoSentenza);
			lDepDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException(
					"DepositoSentenzaController.ExModificaDepositoSentenza: Non posso inserire: " + ex);
//		} catch (SQLException sqe) {
//			rollback(lConn);
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//			siesLogger.debug("SQLException: " + sqe);
//			throw new F3BException(
//					"DepositoSentenzaController.ExModificaDepositoSentenza: Non posso inserire il soggetti : "
//							+ sqe);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	/**
	 * Stampa i modelli pe la Sentenza
	 * 
	 * @param aEvento
	 * @param aFasc
	 * @param aUtenteModel
	 * @return lByteArrayOut
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumentoModello(FascicoloGPModel aFasc,
			EventoNotificaModel aEvento, UtenteModel aUtenteModel) throws F3BException {
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			aEvento.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());

			// Generazione documento di stampa
			IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
			lByteArrayOut = lCtrlSta.ExPreStampaDocumentoSentenza(aFasc, aEvento, aUtenteModel);
//			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException("DepositoSentenzaController.ExStampaDocumentoModello: " + sqe);
		}

		return lByteArrayOut;
	}

	/**
	 * Esegue modifica del magistrato per Sentenza.
	 */
	public DepositoSentenzaModel ExModificaMagistratoSentenza(DepositoSentenzaModel aDepSenMod)
			throws F3BException {
		Connection lConn = null;
		DepositoSentenzaDAO lDepSenDao = null;
		DepositoSentenzaSqlDAO lDepSenSqlDao = null;

		EventoDAO lEventoDao = null;
		TenoreDAO lTenoreDao = null;
		TenoreModel lTenoreMod = null;

		try {
			lConn = getDBConnection();
			// modifica l'evento
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setIdEvento(aDepSenMod.getIdEventoGenerato());
			lEventoDao.setCodMagistrato(aDepSenMod.getCodMagistrato());
			lEventoDao.setDataAggiornamento(new Date());
			lEventoDao.setCodUfficioAggiornamento(aDepSenMod.getCodUfficioAggiornamento());
			lEventoDao.setCodOperatoreAggiornamento(aDepSenMod.getCodOperatoreAggiornamento());
			lEventoDao.selByKey();
			lEventoDao.update();

			// legge l'id del deposito sentenza per l'id evento generato.
			lDepSenSqlDao = new DepositoSentenzaSqlDAO(lConn);
			lDepSenSqlDao.ricercaDepositoSentenzaByIdEveGenerato(aDepSenMod.getIdEventoGenerato());
			aDepSenMod.setIdDepositoSentenza((((DepositoSentenzaModel) lDepSenSqlDao.getModelByKey())
					.getIdDepositoSentenza()));

			// modifica magistrato alla sentenza.
			lDepSenDao = new DepositoSentenzaDAO(lConn);
			lDepSenDao.setIdEventoGenerato(aDepSenMod.getIdEventoGenerato());
			lDepSenDao.setCodMagistrato(aDepSenMod.getCodMagistrato());
			lDepSenDao.setDataAggiornamento(new Date());
			lDepSenDao.setCodUfficioAggiornamento(aDepSenMod.getCodUfficioAggiornamento());
			lDepSenDao.setCodOperatoreAggiornamento(aDepSenMod.getCodOperatoreAggiornamento());
			lDepSenDao.setCondizioneUpdate(aDepSenMod.getIdDepositoSentenza());
			lDepSenDao.update();

			// modifica dei tenori afferenti.
			lTenoreMod = new TenoreModel();
			lTenoreMod.setDepIdDepositoSentenza(aDepSenMod.getIdDepositoSentenza());
			lTenoreMod.setCodMagistrato(aDepSenMod.getCodMagistrato());
			lTenoreMod.setCodOperatoreAggiornamento(aDepSenMod.getCodUfficioAggiornamento());
			lTenoreMod.setDataAggiornamento(aDepSenMod.getDataAggiornamento());
			lTenoreDao = new TenoreDAO(lConn);
			lTenoreDao.setDAOFromModelForUpdateMagistratoBySentenza(lTenoreMod);
			lTenoreDao.update();

			commit(lConn);

		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);
			throw new SIUSException("DepositoSentenzaController.ExModificaMagistratoSentenza: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			throw new SIUSException("DepositoSentenzaController.ExModificaMagistratoSentenza: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lDepSenDao);
			cleanup(lDepSenSqlDao);
			cleanup(lTenoreDao);
			cleanup(lConn);
		}
		return aDepSenMod;
	}

	/**
	 * Stamapa la Sentenza
	 * 
	 * @param aFasc
	 * @param aEvento
	 * @param aUtenteModel
	 * @return lByteArrayOut
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumento(FascicoloGPModel aFasc, EventoNotificaModel aEvento,
			UtenteModel aUtenteModel) throws F3BException {
		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		// Generazione documento di stampa
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
		lByteArrayOut = lCtrlSta.ExPreStampaDocumentoSentenza(aFasc, aEvento, aUtenteModel);
		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("EVENTO >>> " + aEvento.getEvento().toString());

		try {
			aEvento.getEvento().setDocBlobIn(lByteArrayInput);

			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqe);
			throw new F3BException("DepositoSentenzaController.ExStampaDocumento: " + sqe);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException("DepositoSentenzaController.ExStampaDocumento: " + sqe);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

}