package siap.siep.istanza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.dao.IstanzaDAO;
import siap.siep.istanza.dao.IstanzaSqlDAO;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoSqlDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;

/**
 * <p>
 * Title: IstanzaController
 * </p>
 * <p>
 * Description: Classe Controller per Istanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IstanzaController extends SiapController implements IIstanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserimento dell'Istanza
	 *
	 * @param aIstanza
	 * @return Istanza inserita
	 * @throws F3BException
	 */
	public IstanzaModel ExInserisciIstanza(IstanzaModel aIstanza) throws F3BException {

		Connection lConn = null;

		IstanzaDAO lIstDao = null;
		IstanzaSqlDAO lIstSqlDAO = null;

		IstanzaModel lIstMod = null;

		try {
			lConn = getDBConnection();

			lIstMod = new IstanzaModel(aIstanza);

			lIstDao = new IstanzaDAO(lConn);

			// Gestione del Progressivo Registro
			lIstSqlDAO = new IstanzaSqlDAO(lConn);
			BigDecimal lProgr = lIstSqlDAO.getProgressivoRegistro(lIstMod.getAnnoRegistro(),
					lIstMod.getCodUfficioInserimento());
			lIstMod.setProgrRegistro(new BigDecimal(lProgr.intValue() + 1));

			lIstDao.setDAOFromModel(aIstanza);

			BigDecimal lKey = null;
			lKey = lIstDao.insert();

			commit(lConn);

			lIstMod.setIdIstanza(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("IstanzaController.ExInserisci: " + ex);
		} finally {
			cleanup(lIstDao);
			cleanup(lIstSqlDAO);
			cleanup(lConn);
		}

		return lIstMod;
	}

	/**
	 * Inserisce l'istanza ed il soggetto
	 *
	 * @param aIstanza
	 * @param aSoggetto
	 * @return
	 * @throws F3BException
	 */
	public IstanzaModel ExInserisciIstanzaSoggetto(IstanzaModel aIstanza, SoggettoModel aSoggetto)
			throws F3BException {

		Connection lConn = null;

		IstanzaDAO lIstDao = null;
		IstanzaSqlDAO lIstSqlDAO = null;
		SoggettoDAO lSoggDao = null;

		IstanzaModel lIstMod = aIstanza;
		SoggettoModel lSogg = null;

		try {
			lConn = getDBTransaction();

			// * Inserimento Soggetto *
			lSoggDao = new SoggettoDAO(lConn);
			ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
			// Verifico l'esistenza di un soggetto con lo stesso Codice CS
			lSogg = lSogCtrl.ExVerifyCodCS(aSoggetto.getCodCs(), lSoggDao);
			// Verifico l'esistenza di un soggetto con lo stesso atto di Nascita
			if (lSogg == null)
				lSogg = lSogCtrl.ExVerifyAttoNascita(aSoggetto.getAttoNascita(), lSoggDao);

			// Se non trovato attraverso codice CS o atto di nascita viene inserito record nella tabella
			// soggetto e associato all'istanza
			if (lSogg == null) {
				lSoggDao.setDAOFromModel(aSoggetto);
				BigDecimal lKeySogg = lSoggDao.insert();

				lIstMod.setSogIdSoggetto(lKeySogg);
			}
			// Se presente in BDI (individuato da codice CS o Atto di Nascita) questo viene associato
			// all'istanza
			else {
				lIstMod.setSogIdSoggetto(lSogg.getIdSoggetto());
			}

			// * Inserimento Istanza *
			lIstDao = new IstanzaDAO(lConn);

			// Gestione del Progressivo Registro
			lIstSqlDAO = new IstanzaSqlDAO(lConn);
			BigDecimal lProgr = lIstSqlDAO.getProgressivoRegistro(lIstMod.getAnnoRegistro(),
					lIstMod.getCodUfficioInserimento());
			lIstMod.setProgrRegistro(new BigDecimal(lProgr.intValue() + 1));

			lIstDao.setDAOFromModel(lIstMod);
			BigDecimal lKeyIst = lIstDao.insert();

			commit(lConn);

			lIstMod.setIdIstanza(lKeyIst);
		} catch (DAOException dex) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + dex);
			throw new F3BException("IstanzaController.ExInserisciIstanzaSoggetto : " + dex);
		} catch (Exception ex) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException("IstanzaController.ExInserisciIstanzaSoggetto : " + ex);
		} finally {
			cleanup(lSoggDao);
			cleanup(lIstDao);
			cleanup(lIstSqlDAO);

			cleanup(lConn);
		}

		return lIstMod;
	}

	/**
	 * Inserisce istanza legata ad un fascicolo - Crea un evento di tipo istanza 03-08
	 *
	 * @param aIstanza
	 * @param aFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public IstanzaModel ExInserisciIstanzaFascicoloSiep(IstanzaModel aIstanza,
			FascicoloSiepModel aFascicoloSiep) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		IstanzaDAO lIstDao = null;
		IstanzaSqlDAO lIstSqlDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;

		IstanzaModel lIstMod = aIstanza;

		try {
			lConn = getDBTransaction();

			// * Inserimento record evento *
			lEveDao = new EventoDAO(lConn);

			EventoModel lEvento = new EventoModel();
			lEvento.setCodTipoEvento("03"); // Tipo Evento = ISTANZA
			lEvento.setCodTipoProvvedimento("08"); // Tipo Provvedimento = ISTANZA
			lEvento.setCodMotivo(aIstanza.getCodMotivo());
			lEvento.setCodUfficioEmittente(aIstanza.getCodUfficioInserimento());
			// * Il campo DescrUfficioInserimento viene usato come appoggio al CodComuneUtenteConnesso
			// * per inserirlo nell' evento come CodLuogoEmittente
			lEvento.setCodLuogoEmittente(aIstanza.getDescrUfficioInserimento());
			lEvento.setDataEmissione(aIstanza.getDataPresentazione());
			lEvento.setCodEsito(aIstanza.getCodEsito());
			// lEvento.setFlagPiuMeno(""); // ?
			// lEvento.setDataTrasmissioneAtti(); //?
			// lEvento.setDataRicezioneAtti(); //?
			lEvento.setCodUfficioDestinatario(aIstanza.getCodUfficioDestinatario());
			lEvento.setAnnoProtocollo(aIstanza.getAnnoRegistro());
			lEvento.setCodOperatoreInserimento(aIstanza.getCodOperatoreInserimento());
			lEvento.setDataInserimento(aIstanza.getDataInserimento());
			lEvento.setCodUfficioInserimento(aIstanza.getCodUfficioInserimento());
			lEvento.setCodLuogoDestinatario("-");
			lEvento.setFlagDocumentoRegistrato("S"); // Per defalut si assume l'istanza a 'S'
			lEvento.setCodTipoUfficioDestinatario(aIstanza.getCodTipoUfficioDestinatario());
			lEvento.setCognomeSoggettoPresentante(aIstanza.getCognomeSoggettoPresentante());
			lEvento.setNomeSoggettoPresentante(aIstanza.getNomeSoggettoPresentante());
			lEvento.setFlagStampaSiep("S");
			lEvento.setFlagVideoSiep("S");

			lEvento.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());

			// Setto l'anno e il progressivo...
			lEveSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgrEvento = lEveSqlDAO.getProgressivo(lEvento);
			lEvento.setProgrProtocollo(new BigDecimal(lProgrEvento.intValue() + 1));

			lEveDao.setDAOFromModel(lEvento);

			BigDecimal lKeyEvento = lEveDao.insert();
			lEvento.setIdEvento(lKeyEvento);

			lIstMod.setEveIdEvento(lKeyEvento); // Associa l'istanza all'evento corrispondente

			// * Inserimento Istanza *
			lIstDao = new IstanzaDAO(lConn);

			// Gestione del Progressivo Registro
			lIstSqlDAO = new IstanzaSqlDAO(lConn);
			BigDecimal lProgr = lIstSqlDAO.getProgressivoRegistro(lIstMod.getAnnoRegistro(),
					lIstMod.getCodUfficioInserimento());
			lIstMod.setProgrRegistro(new BigDecimal(lProgr.intValue() + 1));

			lIstDao.setDAOFromModel(lIstMod);
			BigDecimal lKeyIst = lIstDao.insert();

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO

			StatoProcedimentoModel lProcModel = new StatoProcedimentoModel();
			lStatoSqlDao.ricercaStatoProcedimentoByFascicoloSiepDesc(aFascicoloSiep.getIdFascicoloSiep());
			lProcModel = (StatoProcedimentoModel) lStatoSqlDao.getModelByKey();
			BigDecimal codStatoMax = null;

			// ========================================================================
			// Se l'ultimo stato procedimento è:
			// 0011 - Emesso Ordine di Esecuzione con Contestuale Sospensione il
			// oppure
			// 0013 - Atti Trasmessi al TDS il
			// Vado in append aggiungendo 0012 - Presentata Istanza il, altrimenti
			// cancello tutto e inserisco solo 0012
			// ========================================================================
			if (lProcModel != null && lProcModel.getCodStatoProcedimento() != null
					&& (lProcModel.getCodStatoProcedimento().equals("0011") // Emesso Ordine di Esecuzione con
																			// Contestuale Sospensione il
							|| lProcModel.getCodStatoProcedimento().equals("0013") // Atti Trasmessi al TDS il
					)) {
				codStatoMax = lStatoSqlDao.getProgressivo(aFascicoloSiep.getIdFascicoloSiep());
				codStatoMax = new BigDecimal(codStatoMax.intValue() + 1);
			} else {
				lStatoDao.setCondizioneByIdFascicolo(aFascicoloSiep.getIdFascicoloSiep());
				lStatoDao.delete();
				lStatoDao.stop();
				codStatoMax = new BigDecimal(1);
			}

			StatoProcedimentoModel lStatoUno = new StatoProcedimentoModel();

			// Presentata istanza
			lStatoUno.setProgressivo(codStatoMax);

			lStatoUno.setCodStatoProcedimento("0012"); // Presentata Istanza il
			lStatoUno.setData(aIstanza.getDataPresentazione());
			lStatoUno.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			lStatoUno.setCodOperatoreInserimento(aIstanza.getCodOperatoreInserimento());
			lStatoUno.setDataInserimento(aIstanza.getDataInserimento());
			lStatoUno.setCodUfficioInserimento(aIstanza.getCodUfficioInserimento());

			lStatoDao.setDAOFromModel(lStatoUno);
			lStatoDao.insert();
			lStatoDao.stop();

			commit(lConn);

			lIstMod.setIdIstanza(lKeyIst);
		} catch (DAOException dex) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + dex);
			throw new F3BException("IstanzaController.ExInserisciIstanzaFascicoloSiep : " + dex);
		} catch (Exception ex) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException("IstanzaController.ExInserisciIstanzaFascicoloSiep : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lIstDao);
			cleanup(lIstSqlDAO);
			cleanup(lStatoDao);
			cleanup(lStatoSqlDao);

			cleanup(lConn);
		}

		return lIstMod;
	}

	/**
	 * Ricerco l'istanza
	 *
	 * @param aIstanza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaIstanza(IstanzaModel aIstanza) throws F3BException {

		Connection lConn = null;
		Vector lIstanzi = new Vector();
		IstanzaSqlDAO lIstDao = null;

		try {
			lConn = getDBConnection();
			lIstDao = new IstanzaSqlDAO(lConn);
			lIstDao.ricercaIstanza(aIstanza);
			lIstanzi = new Vector(lIstDao.getModels());
			if (lIstanzi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("IstanzaController.ExRicercaIstanza: " + daoEx);
		} finally {
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lIstanzi;
	}

	/**
	 * Ricerco l'istanza dalla chiave
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public IstanzaModel ExRicercaIstanzaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		IstanzaSqlDAO lIstDao = null;
		IstanzaModel lIstMod;

		try {
			lConn = getDBConnection();
			lIstDao = new IstanzaSqlDAO(lConn);
			lIstDao.ricercaIstanzaByKey(aKey);
			lIstMod = (IstanzaModel) lIstDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("IstanzaController.ExRicercaIstanza: " + daoEx);
		} finally {
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lIstMod;
	}

	/**
	 * Ricerca soggetto, fascicolo ed evento dall'id dell'istanza
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */

	public IstanzaSoggettoEventoFascicoloSiepModel ExRicercaIstanzaSoggettoEventoFascicoloSiepByKey(
			BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		IstanzaSqlDAO lIstDao = null;
		SoggettoSqlDAO lSoggSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		FascicoloSiepSqlDAO lFascSqlDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;

		IstanzaSoggettoEventoFascicoloSiepModel lMod = new IstanzaSoggettoEventoFascicoloSiepModel();
		IstanzaModel lIstMod = null;
		SoggettoModel lSoggMod = null;
		EventoModel lEveMod = null;
		FascicoloSiepModel lFascMod = null;
		SoggettoModel lSoggettoMod = null;
		SentenzaModel lSentMod = null;

		try {
			lConn = getDBConnection();

			// * Istanza *
			lIstDao = new IstanzaSqlDAO(lConn);

			lIstDao.ricercaIstanzaByKey(aKey);

			lIstMod = (IstanzaModel) lIstDao.getModelByKey();

			if (lIstMod != null) {
				lMod.setIstanza(lIstMod);

				// * Soggetto *
				lSoggSqlDao = new SoggettoSqlDAO(lConn);
				lSoggSqlDao.ricercaSoggettoByKey(lIstMod.getSogIdSoggetto());
				lSoggMod = (SoggettoModel) lSoggSqlDao.getModelByKey();
				lMod.setSoggetto(lSoggMod);

				// * Evento *
				if (lIstMod.getEveIdEvento() != null) {
					lEveSqlDao = new EventoSqlDAO(lConn);
					lEveSqlDao.ricercaEventoIstanzaByKey(lIstMod.getEveIdEvento());
					lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
					lMod.setEvento(lEveMod);

					// * Fascicolo Siep *
					if (lEveMod != null && lEveMod.getFasSieIdFascicoloSiep() != null) {
						lFascSqlDao = new FascicoloSiepSqlDAO(lConn);
						lSoggDao = new SoggettoSqlDAO(lConn);
						lSentDao = new SentenzaSqlDAO(lConn);

						lFascSqlDao.ricercaFascicoloByKey(lEveMod.getFasSieIdFascicoloSiep());

						lFascMod = (FascicoloSiepModel) lFascSqlDao.getModelByKey();

						if (lFascMod == null)
							throw new SIEPException(F3BException.USER_MESSAGE, "Fascicolo Siep non trovato");

						// Cerca il soggetto associato al fascicolo
						lSoggDao.ricercaSoggettoByKey(lFascMod.getSogIdSoggetto());
						lSoggettoMod = (SoggettoModel) lSoggDao.getModelByKey();

						if (lSoggettoMod == null)
							throw new SIEPException(F3BException.USER_MESSAGE,
									"Soggetto associato non trovato");

						lFascMod.setSoggetto(lSoggettoMod);

						// Cerca la sentenza associata al fascicolo
						lSentDao.ricercaSentenzaBykey(lFascMod.getSenIdSentenza());
						lSentMod = (SentenzaModel) lSentDao.getModelByKey();

						if (lSentMod == null)
							throw new SIEPException(F3BException.USER_MESSAGE,
									"Sentenza associata non trovata");

						lFascMod.setSentenza(lSentMod);

						lMod.setFascicoloSiep(lFascMod);
					}
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"IstanzaController.ExRicercaIstanzaSoggettoEventoFascicoloSiepByKey: " + daoEx);
		} finally {
			cleanup(lIstDao);
			cleanup(lSoggSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lFascSqlDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);
			cleanup(lConn);
		}

		return lMod;
	}

	/**
	 * Ricerca Istanza e tutto dall'id evento
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public IstanzaSoggettoEventoFascicoloSiepModel ExRicercaIstanzaSoggettoEventoFascicoloSiepByIdEvento(
			BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		IstanzaSqlDAO lIstDao = null;
		SoggettoSqlDAO lSoggSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		FascicoloSiepSqlDAO lFascSqlDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;

		IstanzaSoggettoEventoFascicoloSiepModel lMod = new IstanzaSoggettoEventoFascicoloSiepModel();
		IstanzaModel lIstMod = null;
		SoggettoModel lSoggMod = null;
		EventoModel lEveMod = null;
		FascicoloSiepModel lFascMod = null;
		SoggettoModel lSoggettoMod = null;
		SentenzaModel lSentMod = null;

		try {
			lConn = getDBConnection();

			// * Istanza *
			lIstDao = new IstanzaSqlDAO(lConn);

			lIstDao.ricercaByKeyEvento(aKey);

			lIstMod = (IstanzaModel) lIstDao.getModelByKey();

			if (lIstMod != null) {
				lMod.setIstanza(lIstMod);

				// * Soggetto *
				lSoggSqlDao = new SoggettoSqlDAO(lConn);
				lSoggSqlDao.ricercaSoggettoByKey(lIstMod.getSogIdSoggetto());
				lSoggMod = (SoggettoModel) lSoggSqlDao.getModelByKey();
				lMod.setSoggetto(lSoggMod);

				// * Evento *
				if (lIstMod.getEveIdEvento() != null) {
					lEveSqlDao = new EventoSqlDAO(lConn);
					lEveSqlDao.ricercaEventoIstanzaByKey(lIstMod.getEveIdEvento());
					lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
					lMod.setEvento(lEveMod);

					// * Fascicolo Siep *
					if (lEveMod != null && lEveMod.getFasSieIdFascicoloSiep() != null) {
						lFascSqlDao = new FascicoloSiepSqlDAO(lConn);
						lSoggDao = new SoggettoSqlDAO(lConn);
						lSentDao = new SentenzaSqlDAO(lConn);

						lFascSqlDao.ricercaFascicoloByKey(lEveMod.getFasSieIdFascicoloSiep());

						lFascMod = (FascicoloSiepModel) lFascSqlDao.getModelByKey();

						if (lFascMod == null)
							throw new SIEPException(F3BException.USER_MESSAGE, "Fascicolo Siep non trovato");

						// Cerca il soggetto associato al fascicolo
						lSoggDao.ricercaSoggettoByKey(lFascMod.getSogIdSoggetto());
						lSoggettoMod = (SoggettoModel) lSoggDao.getModelByKey();

						if (lSoggettoMod == null)
							throw new SIEPException(F3BException.USER_MESSAGE,
									"Soggetto associato non trovato");

						lFascMod.setSoggetto(lSoggettoMod);

						// Cerca la sentenza associata al fascicolo
						lSentDao.ricercaSentenzaBykey(lFascMod.getSenIdSentenza());
						lSentMod = (SentenzaModel) lSentDao.getModelByKey();

						if (lSentMod == null)
							throw new SIEPException(F3BException.USER_MESSAGE,
									"Sentenza associata non trovata");

						lFascMod.setSentenza(lSentMod);

						lMod.setFascicoloSiep(lFascMod);
					}
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"IstanzaController.ExRicercaIstanzaSoggettoEventoFascicoloSiepByKey: " + daoEx);
		} finally {
			cleanup(lIstDao);
			cleanup(lSoggSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lFascSqlDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);
			cleanup(lConn);
		}

		return lMod;
	}

	/**
	 * MOdifica Istanza
	 *
	 * @param aIstanza
	 * @return
	 * @throws F3BException
	 */
	public IstanzaModel ExModificaIstanza(IstanzaModel aIstanza) throws F3BException {

		Connection lConn = null;

		IstanzaDAO lIstDao = null;

		IstanzaModel lIstMod = new IstanzaModel(aIstanza);

		try {
			lConn = getDBConnection();
			lIstDao = new IstanzaDAO(lConn);
			lIstDao.setDAOFromModelForUpdate(aIstanza);
			lIstDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("IstanzaController.ExModifica: " + ex);
		} finally {
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lIstMod;
	}

	/**
	 * MOdifica Istanza Legata a fascicolo
	 *
	 * @param aIstanza
	 * @param aFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public IstanzaModel ExModificaIstanzaFascicoloSiep(IstanzaModel aIstanza,
			FascicoloSiepModel aFascicoloSiep) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		IstanzaDAO lIstDao = null;
		IstanzaSqlDAO lIstSqlDAO = null;

		IstanzaModel lIstMod = aIstanza;

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			EventoModel lEvento = new EventoModel();

			if (lIstMod.getEveIdEvento() == null) {

				// * Inserimento record evento *
				lEvento.setCodTipoEvento("03"); // Tipo Evento = ISTANZA
				lEvento.setCodTipoProvvedimento("08"); // Tipo Provvedimento = ISTANZA
				lEvento.setCodMotivo(lIstMod.getCodMotivo());
				lEvento.setCodUfficioEmittente(lIstMod.getCodUfficioAggiornamento());
				// * Il campo DescrUfficioAggiornamento viene usato come appoggio al CodComuneUtenteConnesso
				// * per inserirlo nell' evento come CodLuogoEmittente
				lEvento.setCodLuogoEmittente(lIstMod.getDescrUfficioAggiornamento());
				lEvento.setDataEmissione(lIstMod.getDataPresentazione());
				lEvento.setCodEsito(lIstMod.getCodEsito());
				// lEvento.setFlagPiuMeno(""); // ?
				// lEvento.setDataTrasmissioneAtti(); //?
				// lEvento.setDataRicezioneAtti(); //?
				lEvento.setCodUfficioDestinatario(lIstMod.getCodUfficioDestinatario());
				lEvento.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
				lEvento.setCodOperatoreInserimento(lIstMod.getCodOperatoreAggiornamento());
				lEvento.setDataInserimento(lIstMod.getDataAggiornamento());
				lEvento.setCodUfficioInserimento(lIstMod.getCodUfficioAggiornamento());
				lEvento.setCodLuogoDestinatario(lIstMod.getCodLuogoDestinatario());
				// lEvento.setTenIdTenore(); //?
				// lEvento.setEveIdEvento(); //?
				lEvento.setFlagDocumentoRegistrato("S"); // Per defalut si assume l'istanza a 'S'
				// lEvento.getCodMagistrato(); //?
				lEvento.setCodTipoUfficioDestinatario(lIstMod.getCodTipoUfficioDestinatario());
				lEvento.setCognomeSoggettoPresentante(lIstMod.getCognomeSoggettoPresentante());
				lEvento.setNomeSoggettoPresentante(lIstMod.getNomeSoggettoPresentante());
				lEvento.setFlagStampaSiep("S");
				lEvento.setFlagVideoSiep("S");

				lEvento.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());

				// Setto l'anno e il progressivo...
				lEveSqlDAO = new EventoSqlDAO(lConn);
				BigDecimal lProgrEvento = lEveSqlDAO.getProgressivo(lEvento);
				lEvento.setProgrProtocollo(new BigDecimal(lProgrEvento.intValue() + 1));

				lEveDao.setDAOFromModel(lEvento);

				BigDecimal lKeyEvento = lEveDao.insert();
				lEvento.setIdEvento(lKeyEvento);

				lIstMod.setEveIdEvento(lKeyEvento); // Associa l'istanza all'evento corrispondente
			} else {

				// * Modifica record evento associato *
				lEveSqlDAO = new EventoSqlDAO(lConn);

				lEveSqlDAO.ricercaEventoIstanzaByKey(lIstMod.getEveIdEvento());

				lEvento = (EventoModel) lEveSqlDAO.getModelByKey();
				;

				lEvento.setCodMotivo(lIstMod.getCodMotivo());
				lEvento.setCodEsito(lIstMod.getCodEsito());
				lEvento.setCodTipoUfficioDestinatario(lIstMod.getCodTipoUfficioDestinatario());
				lEvento.setCodLuogoDestinatario(lIstMod.getCodLuogoDestinatario());
				lEvento.setCognomeSoggettoPresentante(lIstMod.getCognomeSoggettoPresentante());
				lEvento.setNomeSoggettoPresentante(lIstMod.getNomeSoggettoPresentante());

				lEvento.setCodOperatoreAggiornamento(lIstMod.getCodOperatoreAggiornamento());
				lEvento.setDataAggiornamento(lIstMod.getDataAggiornamento());
				lEvento.setCodUfficioAggiornamento(lIstMod.getCodUfficioAggiornamento());

				lEveDao.setDAOFromModelForUpdate(lEvento);
				lEveDao.update();

			}

			// * Modifica Istanza *
			lIstDao = new IstanzaDAO(lConn);

			lIstDao.setDAOFromModelForUpdate(lIstMod);

			lIstDao.update();

			commit(lConn);
		} catch (DAOException dex) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + dex);
			throw new F3BException("IstanzaController.ExModificaIstanzaFascicoloSiep : " + dex);
		} catch (Exception ex) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			throw new F3BException("IstanzaController.ExModificaIstanzaFascicoloSiep : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lIstDao);
			cleanup(lIstSqlDAO);

			cleanup(lConn);
		}

		return lIstMod;
	}

	/**
	 * RIcerca le Istanze associate ad un Fascicolo SIEP
	 *
	 * @param aIstanza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaIstanzaByFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		Vector lIstanzi = new Vector();
		// EventoSqlDAO lEveDao = null;
		IstanzaSqlDAO lDao = null;

		try {
			lConn = getDBConnection();

			// lEveDao = new EventoSqlDAO(lConn);
			// lEveDao.ricercaIstanzeByFascicolo(aKey);
			lDao = new IstanzaSqlDAO(lConn);
			lDao.ricercaIstanzeByFascicolo(aKey);

			// lIstanzi = new Vector(lEveDao.getModels());
			lDao.start();
			while (lDao.next()) {
				lIstanzi.add(lDao.getModelIstEve());
			}
			lDao.stop();

			if (lIstanzi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("IstanzaController.ExRicercaIstanzaByFascicolo: " + daoEx);
		} finally {
			// cleanup(lEveDao);
			cleanup(lDao);

			cleanup(lConn);
		}

		return lIstanzi;
	}

	/**
	 * Ricerca Istanza dal Soggetto
	 *
	 * @param aSogMod
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaIstanzaSoggetto(SoggettoModel aSogMod) throws F3BException {

		Connection lConn = null;

		Vector lIstVect = new Vector();
		IstanzaSqlDAO lIstSqlDao = null;

		try {
			lConn = getDBConnection();

			lIstSqlDao = new IstanzaSqlDAO(lConn);
			lIstSqlDao.ricercaIstanzaSoggetto(aSogMod);

			lIstSqlDao.start();

			while (lIstSqlDao.next()) {
				lIstVect.add(lIstSqlDao.getModelIstSogEveFasc());
			}

			lIstSqlDao.stop();

			if (lIstVect.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("IstanzaController.ExRicercaIstanzaSoggetto: " + daoEx);
		} finally {
			cleanup(lIstSqlDao);
			cleanup(lConn);
		}

		return lIstVect;
	}

	/**
	 * Ricerca Istanza paginandola
	 *
	 * @param aSogMod
	 * @param aPage
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaIstanzaSoggettoPaged(SoggettoModel aSogMod, int aPage) throws F3BException {

		Connection lConn = null;

		Vector lIstVect = new Vector();
		IstanzaSqlDAO lIstSqlDao = null;

		try {
			lConn = getDBConnection();

			lIstSqlDao = new IstanzaSqlDAO(lConn);
			lIstSqlDao.ricercaIstanzaSoggettoPaged(aSogMod, aPage);

			lIstSqlDao.start();

			while (lIstSqlDao.next()) {
				lIstVect.add(lIstSqlDao.getModelIstSogEveFascForPaged());
			}

			lIstSqlDao.stop();

			if (lIstVect.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("IstanzaController.ExRicercaIstanzaSoggettoPaged: " + daoEx);
		} finally {
			cleanup(lIstSqlDao);
			cleanup(lConn);
		}

		return lIstVect;
	}

	/**
	 * Count istanza per soggetto
	 *
	 * @param aSogMod
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountIstanzaSoggettoPaged(SoggettoModel aSogMod) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		IstanzaSqlDAO lSqlDao = null;
		try {
			lConn = getDBConnection();
			lSqlDao = new IstanzaSqlDAO(lConn);
			lSqlDao.getCountIstanzaSoggettoPaged(aSogMod);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("IstanzaController.ExGetCountIstanzaSoggettoPaged: " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Count istanze
	 *
	 * @param aIstMod
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountIstanzaOggettoPaged(IstanzaModel aIstMod) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		IstanzaSqlDAO lSqlDao = null;
		try {
			lConn = getDBConnection();
			lSqlDao = new IstanzaSqlDAO(lConn);
			lSqlDao.getCountIstanzaOggettoPaged(aIstMod);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("IstanzaController.ExGetCountIstanzaOggettoPaged: " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Ricerca per Oggetto
	 *
	 * @param aIstMod
	 * @param aPage
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaIstanzaOggettoPaged(IstanzaModel aIstMod, int aPage) throws F3BException {

		Connection lConn = null;

		Vector lIstVect = new Vector();
		IstanzaSqlDAO lIstSqlDao = null;

		try {
			lConn = getDBConnection();

			lIstSqlDao = new IstanzaSqlDAO(lConn);
			lIstSqlDao.ricercaIstanzaOggettoPaged(aIstMod, aPage);

			lIstSqlDao.start();
			while (lIstSqlDao.next()) {
				lIstVect.add(lIstSqlDao.getModelIstSogEveFascForPaged());

			}
			lIstSqlDao.stop();

			if (lIstVect.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("IstanzaController.ExRicercaIstanzaOggettoPaged: " + daoEx);
		} finally {
			cleanup(lIstSqlDao);
			cleanup(lConn);
		}

		return lIstVect;
	}

	/**
	 * Ricerca Istanza Oggetto
	 *
	 * @param aIstMod
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaIstanzaOggetto(IstanzaModel aIstMod) throws F3BException {

		Connection lConn = null;

		Vector lIstVect = new Vector();
		IstanzaSqlDAO lIstSqlDao = null;

		try {
			lConn = getDBConnection();

			lIstSqlDao = new IstanzaSqlDAO(lConn);
			lIstSqlDao.ricercaIstanzaOggetto(aIstMod);

			lIstSqlDao.start();
			while (lIstSqlDao.next()) {
				lIstVect.add(lIstSqlDao.getModelIstSogEveFasc());
			}
			lIstSqlDao.stop();

			if (lIstVect.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("IstanzaController.ExRicercaIstanzaSoggetto: " + daoEx);
		} finally {
			cleanup(lIstSqlDao);
			cleanup(lConn);
		}

		return lIstVect;
	}

	/**
	 * annullamento Istanza
	 *
	 * @param aIstMod
	 * @param aCampoNota
	 * @return
	 * @throws F3BException
	 */
	public IstanzaModel ExAnnulamentoIstanzaInserisciCampoNota(IstanzaModel aIstMod,
			CampoNotaModel aCampoNota) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		CampoNotaDAO lCampoNotaDao = null;
		IstanzaDAO lIstaDao = null;

		EventoModel EveMod = null;
		IstanzaModel lIstMod = new IstanzaModel(aIstMod);

		try {
			lConn = getDBTransaction();

			// RICERCA L'EVENTO DA ANNULLARE assocciato all'istanza se esiste
			lEveDao = new EventoDAO(lConn);
			lEveSqlDAO = new EventoSqlDAO(lConn);

			if (aIstMod != null && aIstMod.getEveIdEvento() != null) {
				lEveSqlDAO.ricercaEventoByKey(aIstMod.getEveIdEvento());
				EveMod = (EventoModel) lEveSqlDAO.getModelByKey();

				lEveDao.setIdEvento(EveMod.getIdEvento());
				lEveDao.setFlagDocumentoRegistrato("A");
				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
			}

			// Inserisco il CampoNote
			lCampoNotaDao = new CampoNotaDAO(lConn);
			BigDecimal lKey = null;
			if (!aCampoNota.getDescr().equals("")) {
				aCampoNota.setProgressivo(new BigDecimal(1));
				lCampoNotaDao.setDAOFromModel(aCampoNota);
				lKey = lCampoNotaDao.insert();
				lCampoNotaDao.stop();
			}

			// ANNULLO L'ISTANZA
			lIstaDao = new IstanzaDAO(lConn);

			lIstaDao.setIdIstanza(aIstMod.getIdIstanza());
			lIstaDao.setCamIdCampoNote(lKey);
			lIstaDao.setCodStatoIstanza(aIstMod.getCodStatoIstanza());
			lIstaDao.selByKey();
			lIstaDao.update();
			lIstaDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("IstanzaController.ExAnnulamentoIstanzaInserisciCampoNota: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("IstanzaController.ExAnnulamentoIstanzaInserisciCampoNota: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lCampoNotaDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lIstaDao);
			cleanup(lConn);
		}
		return lIstMod;
	}

}