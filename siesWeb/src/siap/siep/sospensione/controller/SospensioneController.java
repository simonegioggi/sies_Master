package siap.siep.sospensione.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.decretoordinanza.dao.DecretoOrdinanzaSiepDAO;
import siap.siep.decretoordinanza.dao.DecretoOrdinanzaSiepSqlDAO;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
//import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.penacomplessiva.dao.PenaComplessivaSqlDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sospensione.action.ICostantiSospensione;
import siap.siep.sospensione.dao.SospensioneDAO;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.verbale.dao.VerbaleDAO;
import siap.siep.verbale.model.VerbaleModel;

/**
 * SospensioneController - Classe Controller per Sospensione
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SospensioneController extends SiapController implements ISospensione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaEventoNotificaSosp(EventoNotificaModel aEvento)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);
		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);

			lSqlDAO = new EventoSqlDAO(lConn);

			lSqlDAO.ricercaEventoTipoCodMotProvNonValidato(aEvento.getEvento());

			EventoModel lEveModel = (EventoModel) lSqlDAO.getModelByKey();
			BigDecimal lKeyEvento = null;

			if (lEveModel == null) // Se non presente lo inserisce
			{
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
				aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				lEveDao.setDAOFromModel(aEvento.getEvento());

				lKeyEvento = lEveDao.insert();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

			} else // Se presente lo aggiorna
			{
				lKeyEvento = lEveModel.getIdEvento();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				lEveDao.setCodMotivo(aEvento.getEvento().getCodMotivo());
				lEveDao.setDataEmissione(aEvento.getEvento().getDataEmissione());
				lEveDao.setDataTrasmissioneAtti(aEvento.getEvento().getDataTrasmissioneAtti());
				lEveDao.setCodLuogoEmittente(aEvento.getEvento().getCodLuogoEmittente());
				lEveDao.setCodUfficioEmittente(aEvento.getEvento().getCodUfficioEmittente());
				lEveDao.setCodMagistrato(aEvento.getEvento().getCodMagistrato());
				lEveDao.setAnnoProtocollo(aEvento.getEvento().getAnnoProtocollo());

				lEveDao.setCodOperatoreAggiornamento(aEvento.getEvento().getCodOperatoreInserimento());
				lEveDao.setDataAggiornamento(aEvento.getEvento().getDataInserimento());
				lEveDao.setCodUfficioAggiornamento(aEvento.getEvento().getCodUfficioInserimento());

				lEveDao.setIdEvento(lKeyEvento);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
				// ************************************************************************

				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();

			}

			BigDecimal lKeyAutorita = null;
			int count = 0;

			// ========================================================================
			// Ciclo di caricamento delle notifiche
			// ========================================================================
			while (count < aEvento.getNotifiche().length) {
				if (aEvento.getNotifiche()[count] != null) {
					// Se è stata specificata anche l'autorità esterna per l'avvocato,
					// recupero l'id da inserire nella notifica
					// n.b. se autorità non presente la creo
					if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
						// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
						// già l'autorità esterna specificata nella form (dalla form ho solo
						// codice e sede)
						lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
							lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					// Collego la notifica all'evento
					aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					// Inserisco la notifica
					lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();

				}
				count++;
			}

			// Inserimento delle eventuali note aggiuntive.
			if (aEvento.getCampoNote() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : "
						+ aEvento.getCampoNote().length);

				count = 0;
				while (count < aEvento.getCampoNote().length) {
					aEvento.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					aEvento.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEvento.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);

			rollback(lConn);
			throw new F3BException("SospensioneController.ExInserisciOModificaEventoNotificaSosp: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();

			rollback(lConn);
			throw new F3BException("SospensioneController.ExInserisciOModificaEventoNotificaSosp: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * ExInserisciEventoNotificaVerbale
	 *
	 * @param aEventoNot
	 * @param aPena
	 * @param aEvento
	 * @param aVerbale
	 * @return idevento
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciEventoNotificaVerbale(EventoNotificaModel aEventoNot,
			PenaResiduaModel aPena, EventoModel aEvento, VerbaleModel aVerbale) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		VerbaleDAO lVerDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEventoNot);
		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);
			lVerDao = new VerbaleDAO(lConn);

			// Evento Verbale
			BigDecimal lKeyEventoVer = null;
			BigDecimal lProgrVer = lSqlDAO.getProgressivo(aEvento);
			aEvento.setProgrProtocollo(new BigDecimal(lProgrVer.intValue() + 1));

			lEveDao.setDAOFromModel(aEvento);
			lKeyEventoVer = lEveDao.insert();
			lEveDao.stop();

			// ===============================================
			// Inserisco il Verbale
			// ===============================================
			aVerbale.setEveIdEvento(lKeyEventoVer);
			lVerDao.setDAOFromModel(aVerbale);
			lVerDao.insert();
			lVerDao.stop();

			// Evento provvedimento
			BigDecimal lKeyEvento = null;
			BigDecimal lProgr = lSqlDAO.getProgressivo(aEventoNot.getEvento());
			aEventoNot.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			lEveDao.setDAOFromModel(aEventoNot.getEvento());
			lEveDao.setEveIdEvento(lKeyEventoVer);
			lKeyEvento = lEveDao.insert();
			lEveRet.getEvento().setIdEvento(lKeyEvento);

			// ========================================================================
			// Ciclo di caricamento delle notifiche
			// ========================================================================
			BigDecimal lKeyAutorita = null;
			int count = 0;
			while (count < aEventoNot.getNotifiche().length) {
				if (aEventoNot.getNotifiche()[count] != null) {
					// Se è stata specificata anche l'autorità esterna per l'avvocato,
					// recupero l'id da inserire nella notifica
					// n.b. se autorità non presente la creo
					if (aEventoNot.getNotifiche()[count].getAutoritaEsterna() != null) {
						// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
						// già l'autorità esterna specificata nella form (dalla form ho solo
						// codice e sede)
						lAutDao.setRicercaByAutSede(aEventoNot.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
							lAutDao.setDAOFromModel(aEventoNot.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEventoNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEventoNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					// Collego la notifica all'evento
					aEventoNot.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					// Inserisco la notifica
					lNotDao.setDAOFromModel(aEventoNot.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();

				}
				count++;
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);

			rollback(lConn);
			throw new F3BException("SospensioneController.ExInserisciEventoNotificaVerbale: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();

			rollback(lConn);
			throw new F3BException("SospensioneController.ExInserisciEventoNotificaVerbale: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lVerDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	public SospensioneModel ExInserisciSospensione(SospensioneModel aSospensione) throws F3BException {

		Connection lConn = null;

		SospensioneDAO lSosDao = null;
		SospensioneModel lSosMod = null;

		try {
			lConn = getDBConnection();

			lSosMod = new SospensioneModel(aSospensione);
			lSosDao = new SospensioneDAO(lConn);
			lSosDao.setDAOFromModel(aSospensione);
			BigDecimal lKey = null;
			lKey = lSosDao.insert();
			commit(lConn);
			lSosMod.setIdSospensione(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("SospensioneController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSosDao);
			cleanup(lConn);
		}

		return lSosMod;
	}

	/**
	 * Effettua Insert dei records SOSPENSIONE senza assegnare la Nuova sequence (Mantiene la KEY originale
	 * passata in Input); Chiamata dalla procedura di Presa in Carico dopo TRSF-CMPTZ
	 *
	 * @param aListaSospensioni
	 * @param lConn
	 * @return lCodEsito
	 * @throws F3BException
	 */
	public String ExInserisciSospensioneWithoutSequence(SospensioneModel aSospensioneModel, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";

		SospensioneDAO lSopensioneDAO = null;

		try {
			lSopensioneDAO = new SospensioneDAO(lConn);

			// Inserisco SOSPENSIONE
			lSopensioneDAO.setDAOFromModel(aSospensioneModel);
			lSopensioneDAO.setWithoutSequence(true);
			lSopensioneDAO.insert();
			lSopensioneDAO.stop();

			siesLogger.debug("=========> SOSPENSIONE scritta----->");
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				siesLogger.error("Sospensione gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				siesLogger.error("Errore in fase di inserimento Sospensione:", ex);
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire Record Sospensione! ");
			}
		} finally {
			cleanup(lSopensioneDAO);
		}

		return lCodEsito;
	}

	/**
	 * Effettua la <b>SVALIDAZIONE</b> dell'evento e della pena residua. Cancellando le eventuali vecchie
	 * annotazioni (Quali?) e sostituendole con quelle passare in input.
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExModificaEventoInserisciNotificaSosp(EventoNotificaModel aEvento)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		PenaResiduaDAO lPenDAO = null;
		PenaResiduaSqlDAO lPenSqlDAO = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;

		EventoModel lEveModRic = null;

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);

			lSqlDAO = new EventoSqlDAO(lConn);

			// Recupero l'evento
			EventoModel lEveMod = aEvento.getEvento();
			lSqlDAO.ricercaEvento(lEveMod);
			lEveModRic = new EventoModel();
			lEveModRic = (EventoModel) lSqlDAO.getModelByKey();

			// =======================================================================
			// Recupero la pena residua associata all'evento e la aggiorno svalidandola
			// =======================================================================
			lPenSqlDAO = new PenaResiduaSqlDAO(lConn);
			lPenSqlDAO.ricercaPenaResiduaByKeyEvento(lEveModRic.getIdEvento());
			PenaResiduaModel lModel = (PenaResiduaModel) lPenSqlDAO.getModelByKey();

			lPenDAO = new PenaResiduaDAO(lConn);
			lPenDAO.setFlagValidato("N");
			lPenDAO.setIdPenaResidua(lModel.getIdPenaResidua());

			lPenDAO.setCodOperatoreAggiornamento(aEvento.getEvento().getCodOperatoreInserimento());
			lPenDAO.setDataAggiornamento(aEvento.getEvento().getDataInserimento());
			lPenDAO.setCodUfficioAggiornamento(aEvento.getEvento().getCodUfficioInserimento());
			lPenDAO.selByKey();
			lPenDAO.update();
			lPenDAO.stop();

			// =======================================================================
			// Aggiorno l'evento svalidandolo
			// =======================================================================
			lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
			lEveDao.setCodMotivo(aEvento.getEvento().getCodMotivo());
			lEveDao.setDataEmissione(aEvento.getEvento().getDataEmissione());
			lEveDao.setDataTrasmissioneAtti(aEvento.getEvento().getDataTrasmissioneAtti());
			lEveDao.setCodLuogoEmittente(aEvento.getEvento().getCodLuogoEmittente());
			lEveDao.setCodUfficioEmittente(aEvento.getEvento().getCodUfficioEmittente());
			lEveDao.setCodMagistrato(aEvento.getEvento().getCodMagistrato());
			lEveDao.setAnnoProtocollo(aEvento.getEvento().getAnnoProtocollo());

			// Aggiorno con i dati passati in input
			lEveDao.setCodOperatoreAggiornamento(aEvento.getEvento().getCodOperatoreInserimento());
			lEveDao.setDataAggiornamento(aEvento.getEvento().getDataInserimento());
			lEveDao.setCodUfficioAggiornamento(aEvento.getEvento().getCodUfficioInserimento());

			lEveDao.setIdEvento(lEveModRic.getIdEvento());

			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// =============================================================
			// Cancello le notifiche associate all'evento per reinserirle
			// =============================================================
			lNotDao.setCondizioneEvento(lEveModRic.getIdEvento());
			lNotDao.delete();
			lNotDao.stop();

			// =============================
			// Inserisco le notifiche
			// =============================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Presenti " + aEvento.getNotifiche().length + " notifiche");
			int count = 0;
			while (count < aEvento.getNotifiche().length) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Notifica[" + count + "] = " + aEvento.getNotifiche()[count]);

				if (aEvento.getNotifiche()[count] != null) {
					if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
						BigDecimal lKeyAutorita = null;
						lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					aEvento.getNotifiche()[count].setEveIdEvento(lEveModRic.getIdEvento());

					lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();

				}
				count++;
			}

			// ================================================
			// Inserimento delle eventuali note aggiuntive.
			// ================================================
			if (aEvento.getCampoNote() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : "
						+ aEvento.getCampoNote().length);
				count = 0;
				while (count < aEvento.getCampoNote().length) {
					aEvento.getCampoNote()[count].setEveIdEvento(lEveModRic.getIdEvento());
					aEvento.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEvento.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);

			rollback(lConn);
			throw new F3BException("SospensioneController.ExModificaEventoInserisciNotificaSosp: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();

			rollback(lConn);
			throw new F3BException("SospensioneController.ExModificaEventoInserisciNotificaSosp: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lPenDAO);
			cleanup(lPenSqlDAO);

			cleanup(lConn);
		}

		return lEveModRic;
	}

	public Vector ExRicercaSospensione(SospensioneModel aSospensione) throws F3BException {

		Connection lConn = null;

		Vector lSospensioni = new Vector();
		SospensioneSqlDAO lSosDao = null;

		try {
			lConn = getDBConnection();

			lSosDao = new SospensioneSqlDAO(lConn);
			lSosDao.ricercaSospensione(aSospensione);
			lSospensioni = new Vector(lSosDao.getModels());

			if (lSospensioni.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SospensioneController.ExRicercaSospensione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSosDao);
			cleanup(lConn);
		}

		return lSospensioni;
	}

	public SospensioneModel ExRicercaSospensioneByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		SospensioneSqlDAO lSosDao = null;
		SospensioneModel lSosMod;

		try {
			lConn = getDBConnection();

			lSosDao = new SospensioneSqlDAO(lConn);
			lSosDao.ricercaSospensioneByKey(aKey);
			lSosMod = (SospensioneModel) lSosDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("SospensioneController.ExRicercaSospensioneByKey: " + daoEx);
		} finally {
			cleanup(lSosDao);
			cleanup(lConn);
		}

		return lSosMod;
	}

	public SospensioneModel ExRicercaSospensioneByIdPenaResidua(BigDecimal aIdPenaResidua)
			throws F3BException {

		Connection lConn = null;

		SospensioneSqlDAO lSosDao = null;
		SospensioneModel lSosMod;

		try {
			lConn = getDBConnection();

			lSosDao = new SospensioneSqlDAO(lConn);
			lSosDao.ricercaSospensioneByIdPenaResidua(aIdPenaResidua);
			lSosMod = (SospensioneModel) lSosDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("SospensioneController.ExRicercaSospensioneByIdPenaResidua: " + daoEx);
		} finally {
			cleanup(lSosDao);
			cleanup(lConn);
		}

		return lSosMod;
	}

	public SospensioneModel ExModificaSospensione(SospensioneModel aSospensione) throws F3BException {

		Connection lConn = null;

		SospensioneDAO lSosDao = null;
		SospensioneModel lSosMod = new SospensioneModel(aSospensione);

		try {
			lConn = getDBConnection();
			lSosDao = new SospensioneDAO(lConn);
			lSosDao.setDAOFromModelForUpdate(aSospensione);
			lSosDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("SospensioneController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lSosDao);
			cleanup(lConn);
		}

		return lSosMod;
	}

	public void ExCancellaSospensione(SospensioneModel aSospensione) throws F3BException {

		Connection lConn = null;

		SospensioneDAO lSosDao = null;

		try {
			lConn = getDBConnection();

			lSosDao = new SospensioneDAO(lConn);
			lSosDao.setCondizioneUpdate(aSospensione.getIdSospensione());
			lSosDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SospensioneController.ExCancellaSospensione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSosDao);
			cleanup(lConn);
		}
	}

	public Vector<SospensioneModel> ExRicercaSospensioneByIdFascicoloSiep(BigDecimal aKeyFascicolo)
			throws F3BException {

		Connection lConn = null;

		Vector<SospensioneModel> lSospensioni = new Vector();
		SospensioneSqlDAO lSosDao = null;

		try {
			lConn = getDBConnection();

			lSosDao = new SospensioneSqlDAO(lConn);
			// Ticket#202105240111 - Modificato metodo chiamato per recuperare solo le
			// sospenzioni legate ed eventi e PR trasferibili (validati)
			// lSosDao.ricercaSospensioneByFascicolo(aKeyFascicolo);
			lSosDao.ricercaSospensioneByFascicoloXTrasferimento(aKeyFascicolo);
			// Ticket#202105240111 - FINE
			lSospensioni = new Vector<SospensioneModel>(lSosDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SospensioneController.ExRicercaSospensioneByIdFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSosDao);
			cleanup(lConn);
		}

		return lSospensioni;
	}

	public EventoModel ExUpdateValidaSospensione(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			String aPosGiu, String aNomProv, String aStatoProc) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		DecretoOrdinanzaSiepDAO lDecDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecSql = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			EventoSqlDAO lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvento.getIdEvento());

			EventoModel lEveModel = (EventoModel) lEveSqlDAO.getModelByKey();

			// * Cerca L'EVENTO *
			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
			}

			// ricerca pena residua sospesa

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao = new PenaResiduaDAO(lConn);

			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaNonValidataSospesa(
					aFascicolo.getIdFascicoloSiep());

			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			lPenResDao.setFlagValidato("S");
			// lPenResDao.setEveIdEvento(aEvento.getIdEvento());
			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			lPenResDao.setDAOFromModel(lPenResMod);
			lPenResDao.setFlagValidato("S");
			lPenResDao.setEveIdEvento(aEvento.getIdEvento());
			lPenResDao.setDataInserimento(lEveMod.getDataAggiornamento());
			lPenResDao.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
			lPenResDao.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
			lPenResDao.insert();
			lPenResDao.stop();

			// update decreto_ordinanza
			lDecSql = new DecretoOrdinanzaSiepSqlDAO(lConn);
			lDecSql.ricercaDecretoOrdinanzaSiepByIdEvento(lEveModel.getEveIdEvento());

			DecretoOrdinanzaSiepModel lDecMod = (DecretoOrdinanzaSiepModel) lDecSql.getModelByKey();
			if (lDecMod != null) {
				lDecDao = new DecretoOrdinanzaSiepDAO(lConn);
				lDecDao.setIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());
				lDecDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lDecDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				lDecDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

				lDecDao.setFlagElaborato("S");
				lDecDao.selByKey();
				lDecDao.update();
				lDecDao.stop();
			}
			// update evento legato all decreto

			lEveSqlDAO.ricercaEventoByKey(lEveModel.getEveIdEvento());

			EventoModel lEveModDec = (EventoModel) lEveSqlDAO.getModelByKey();

			lEveDao.setIdEvento(lEveModDec.getIdEvento());
			lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
			lEveDao.setFlagDocumentoRegistrato("S");
			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// Cerca POSIZIONE_GIURIDICA corrente
			lEveModel.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lEveModel.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lEveModel.setDataAggiornamento(aEvento.getDataAggiornamento());
			// Cerca POSIZIONE_GIURIDICA corrente
			// inserisciModificaPosizioneGiuridica(lConn, aFascicolo , lEveModel , aPosGiu , aEvento );

			Date lDataInizioNuovaPos = lDecMod.getDataSospensioneEsecuzione();
			if (aNomProv.equals("NP115")) // -- Provvedimento di Espulsione --
			{
				lDataInizioNuovaPos = lDecMod.getDataEspulsione();
			}

			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);

			lPosSqlDao.ricercaPosizioneGiuridicaByIdFascicoloDesc(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());

			lPosDao.setDataFine(lDataInizioNuovaPos);

			lPosDao.selByKey();
			lPosDao.update();
			lPosDao.stop();

			// inserimento nuova occorrenza
			PosizioneGiuridicaModel lPosizione = new PosizioneGiuridicaModel();

			lPosizione.setCodPosizioneGiuridica(aPosGiu);
			lPosizione.setDataInizio(lDataInizioNuovaPos);

			lPosizione.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());

			lPosizione.setDataInserimento(aEvento.getDataAggiornamento());
			lPosizione.setCodPosizioneProcessuale("-");
			lPosizione.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			lPosizione.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lPosizione.setIdEventoRiferimento(aEvento.getIdEvento()); // **

			lPosDao.setDAOFromModel(lPosizione);
			lPosDao.insert();
			lPosDao.stop();
			// **************************************************************

			// SETTA LO STATO PROCEDIMENTO
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveApp.getDataEmissione());

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(lEveMod.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
			lStatoProcMod.setCodStatoProcedimento(aStatoProc);

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();

			// Aggiorna tabella nome_provvedimento

			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento(aNomProv);

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// Commentato sotto indicazioni di pellegrini --Dario --Luciana
			// modifica del 24-02-05 dario -- luciana -- quando valido l'espulsione devo archiviare il
			// fascicolo
			/*
			 * lFascDao = new FascicoloSiepDAO(lConn); if(lEveModel != null && lEveModel.getCodMotivo() !=
			 * null && lEveModel.getCodMotivo().equals("0276")) {
			 * lFascDao.setDAOFromModelForUpdate(aFascicolo); lFascDao.setCodStatoFascicolo("01");
			 * lFascDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());
			 * lFascDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			 * lFascDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
			 * lFascDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			 *
			 * lFascDao.update(); lFascDao.stop(); }
			 */

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConn);
			// commit(lConnBlob);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaSospensione: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaSospensione: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lDecDao);
			cleanup(lDecSql);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveMod;
	}

	/**
	 * Effettua l'aggiornamento del blob e la validazione del Differimento.
	 *
	 *
	 * @param aEvento
	 *            - Evento dell'Esecuzione (Comunicazione o Ordine di scarcerazione)
	 * @param aFascicolo
	 *            -
	 * @param aPosGiu
	 *            - Nuova posizione giuridica (serve per l'aggiornamento)
	 * @param aNomProv
	 *            - Cod. Nome Provvedimento
	 * @param aStatoProc
	 *            - Cod. Stato procedimento
	 * @deprecated Utilizzato dalla vecchia gestione del Differimento
	 * @see ExUpdateValidaDifferimentoNew *
	 */
	public EventoModel ExUpdateValidaDifferimento(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			String aPosGiu, String aNomProv, String aStatoProc) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		DecretoOrdinanzaSiepDAO lDecDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecSql = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Recupero l'EVENTO (perchè? è in input!)
			lEveDao = new EventoDAO(lConn);
			EventoSqlDAO lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDAO.getModelByKey();

			// * Cerca L'EVENTO * ???????????????????
			/*
			 * EventoModel lEveApp = new EventoModel();
			 *
			 * lEveDao.setIdEvento(aEvento.getIdEvento()); lEveDao.selByKey(); lEveDao.start(); if
			 * (lEveDao.next()) { lEveApp.setIdEvento(lEveDao.getIdEvento());
			 * lEveApp.setDataEmissione(lEveDao.getDataEmissione()); } // [FT] - 03/08/2016 - MAC_LOG -
			 * Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("EVENTO@@@@@@@@@@ appena inserito" + lEveModel);
			 */

			// ========================================================================
			// Recupero l'evento associato al decreto ordinanza
			// ========================================================================
			lEveSqlDAO.ricercaEventoByKey(lEveModel.getEveIdEvento());
			EventoModel lEveModelDec = (EventoModel) lEveSqlDAO.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("EVENTO@@@@@@@@@@  inserito prima" + lEveModelDec);

			// ========================================================================
			// Recupero il Decreto Ordinanza Siep
			// ??? perchè non usa ExRicercaDecretoOrdinanzaSiepByKey come nella action?
			// ========================================================================
			lDecSql = new DecretoOrdinanzaSiepSqlDAO(lConn);
			lDecSql.ricercaDecretoOrdinanzaSiepByIdEvento(lEveModelDec.getIdEvento());

			Vector lDec = new Vector(lDecSql.getModels());
			DecretoOrdinanzaSiepModel lDecMod = (DecretoOrdinanzaSiepModel) lDec.get(0);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("select su decreto  ord" + lDecMod);

			// ========================================================================
			// Valida la pena residua associata al decreto (FLAG_VALIDATO=S)
			// ========================================================================
			// Attenzione tutto da verificare: non recupera la pena residua associata
			// al decreto sebbene conosca l'idEvento, ma recupera l'ultima inserita
			// indipendentemente dal fatto che sia validata o meno
			// Recupera la pena residua e aggiorna: flag validato
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" ricerca penaredsidua" + lPenResMod);

			// Aggiorno la pena_residua agganciandola comunque al decreto
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());

			lPenResDao.setEveIdEvento(lDecMod.getIdEventoGenerato());
			lPenResDao.setFlagValidato("S");
			// lPenResDao.setEveIdEvento(aEvento.getIdEvento());
			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" update penaredsidua");

			// ========================================================================
			// Duplico e valido la pena residua del decreto agganciandola al provvedimento
			// che sto validando (FLAG_VALIDATO=S)
			// ========================================================================
			lPenResDao.setDAOFromModel(lPenResMod);
			lPenResDao.setFlagValidato("S");
			lPenResDao.setEveIdEvento(aEvento.getIdEvento());
			lPenResDao.setDataInserimento(lEveMod.getDataAggiornamento());
			lPenResDao.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
			lPenResDao.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
			lPenResDao.insert();
			lPenResDao.stop();

			// update decreto_ordinanza

			// ========================================================================
			// Aggiorno flag_elaborato del decreto ordinanza siep (FLAG_ELABORATO=S)
			// ========================================================================
			lDecDao = new DecretoOrdinanzaSiepDAO(lConn);
			lDecDao.setIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());
			lDecDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			lDecDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			lDecDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

			lDecDao.setFlagElaborato("S");
			lDecDao.selByKey();
			lDecDao.update();
			lDecDao.stop();

			// ========================================================================
			// Aggiorno l'evento legato al decreto ordinanza (FLAG_DOCUMENTO_REGISTRATO=S)
			// ========================================================================
			lEveSqlDAO.ricercaEventoByKey(lDecMod.getIdEventoGenerato());
			EventoModel lEveModDec = (EventoModel) lEveSqlDAO.getModelByKey();

			lEveDao.setIdEvento(lEveModDec.getIdEvento());
			lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

			lEveDao.setFlagDocumentoRegistrato("S");
			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// -----------------------------------------------------------------------

			// ========================================================================
			// Aggiorno la data fine della vecchia Posizione Giuridica con la
			// data del differimento
			// ========================================================================
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosizioneGiuridicaByIdFascicoloDesc(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());

			lPosDao.setDataFine(lDecMod.getDataDifferimento());

			lPosDao.selByKey();
			lPosDao.update();
			lPosDao.stop();

			// ========================================================================
			// Inserisco Nuova Posizione Giuridica (data_inizio = data differimento)
			// agganciandola all'evento che sto validando
			// ========================================================================
			PosizioneGiuridicaModel lPosizione = new PosizioneGiuridicaModel();

			lPosizione.setCodPosizioneGiuridica(aPosGiu);
			lPosizione.setDataInizio(lDecMod.getDataDifferimento());

			lPosizione.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lPosizione.setDataInserimento(aEvento.getDataAggiornamento());
			lPosizione.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lPosizione.setCodPosizioneProcessuale("-");
			lPosizione.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lPosizione.setIdEventoRiferimento(aEvento.getIdEvento());

			lPosDao.setDAOFromModel(lPosizione);
			lPosDao.insert();
			lPosDao.stop();

			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data emissione provvedimento (decreto)
			// ========================================================================
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			// lStatoProcMod.setData(lEveApp.getDataEmissione());
			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			lStatoProcMod.setData(lDecMod.getDataEmissioneProvvedimento());
			lStatoProcMod.setCodStatoProcedimento(aStatoProc);

			lStatoProcMod.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(lEveMod.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();

			// ========================================================================
			// Aggiorna tabella nome_provvedimento
			// ========================================================================
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento(aNomProv);
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			// -----------------------
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaDifferimento : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaDifferimento : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lStatoDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lDecDao);
			cleanup(lDecSql);
			cleanup(lPosSqlDAO);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveMod;
	}

	/**
	 * Effettua la validazione e dei provvedimenti collegati a una <b>Interruzione</b><br>
	 * - Valida l'evento<br>
	 * - Valida la pena residua<br>
	 * - Valida il decreto ordinanza siep<br>
	 * - Effettua l'aggiornamento dello Stato del Procedimento, dello Scadenzario Vane Ricerche, del Nome
	 * Provvedimento<br>
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @param aNomProv
	 *            - nome provvedimento @param aStatoProc - stato procedimento @return @throws
	 */
	public EventoModel ExUpdateValidaSospensioneOE(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			String aNomProv, String aStatoProc) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		DecretoOrdinanzaSiepDAO lDecDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecSql = null;
		ScadenzarioDAO lScaDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// * Cerca L'EVENTO *
			// ========================================================================
			// Recupero l'evento e lo aggiorno
			// ========================================================================
			EventoSqlDAO lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDAO.getModelByKey();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			lEveDao.setIdEvento(lEveModel.getIdEvento());

			// La data aggiornamento non cambia
			lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

			lEveDao.setFlagDocumentoRegistrato("S");
			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// ========================================================================
			// Valida la pena residua
			// ========================================================================
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			lPenResDao.setEveIdEvento(lEveModel.getIdEvento());

			lPenResDao.setFlagValidato("S");
			// lPenResDao.setEveIdEvento(aEvento.getIdEvento());
			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			// ========================================================================
			// Valida Il decreto ordinanza
			// ========================================================================
			lDecSql = new DecretoOrdinanzaSiepSqlDAO(lConn);
			lDecSql.ricercaDecretoOrdinanzaSiepByKey(lEveModel.getDecIdDecretoOrdinanzaSiep());
			Vector lDec = new Vector(lDecSql.getModels());
			DecretoOrdinanzaSiepModel lDecMod = (DecretoOrdinanzaSiepModel) lDec.get(0);

			lDecDao = new DecretoOrdinanzaSiepDAO(lConn);
			lDecDao.setIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());
			lDecDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			lDecDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			lDecDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

			lDecDao.setFlagElaborato("S");
			lDecDao.selByKey();
			lDecDao.update();
			lDecDao.stop();

			// ========================================================================
			// Aggiorna lo Stato del Procedimento, lo Scadenzario (vane Ricerche) e
			// nome provvedimento
			// ========================================================================
			lScaDao = new ScadenzarioDAO(lConn);
			if (!aStatoProc.equals("") && !aNomProv.equals("")) {
				// INSERISCE SCADENZARIO VANE RICERCHE
				// lScaDao = new ScadenzarioDAO(lConn);

				ScadenzarioModel lScaModel = new ScadenzarioModel();
				lScaModel.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lScaModel.setCodTipoScadenzario("03");
				lScaModel.setDataInizioScadenza(lEveModel.getDataEmissione());

				lScaModel.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
				lScaModel.setDataInserimento(lEveMod.getDataAggiornamento());
				lScaModel.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
				// a6-rr-238 -11-2010
				lScaModel.setCodStatoNotifica("NP");

				lScaDao.setDAOFromModel(lScaModel);
				lScaDao.insert();
				lScaDao.stop();

				// ** STATO_PROCEDIMENTO **
				StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

				lStatoProcMod.setProgressivo(new BigDecimal(1));
				lStatoProcMod.setData(lEveModel.getDataEmissione());

				lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lStatoProcMod.setCodStatoProcedimento(aStatoProc);

				lStatoProcMod.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
				lStatoProcMod.setDataInserimento(lEveMod.getDataAggiornamento());
				lStatoProcMod.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());

				lStatoDao = new StatoProcedimentoDAO(lConn);

				// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
				lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				lStatoDao.delete();

				// - Inserisce
				lStatoDao.setDAOFromModel(lStatoProcMod);
				lStatoDao.insert();

				// Aggiorna tabella nome_provvedimento
				lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
				lNomProvvDAO.setCodNomeProvvedimento(aNomProv);
				lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
				lNomProvvDAO.insert();
				lNomProvvDAO.stop();
			}

			// ===================================================================
			// Cancella tutti gli scadenzari di tipo LEGGE SIMEONE ( Tipo = 01 )
			// ===================================================================
			// lScaDao = new ScadenzarioDAO(lConn);
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFascicolo.getIdFascicoloSiep(), "01");
			lScaDao.delete();
			lScaDao.stop();

			commit(lConn);

			// =============================
			// Effettua l'update del BLOB
			// =============================
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaSospensioneOE : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaSospensioneOE : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lDecDao);
			cleanup(lDecSql);
			cleanup(lScaDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveMod;
	}

	/**
	 * Valida l'evento, la pena residua, il decreto ordinanza siep legati a una interruzione. Se codMotivo =
	 * in (0266,0267,0268,0269) aggiorna anche la posizione giuridica Se codMotivo 0268 cambia anche lo stato
	 * del procedimento in 0137 (Disposta consegna temporanea all'estero del condannato)
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @param aPosGiu
	 * @param aPenaResidua
	 * @param aDecreto
	 */
	public EventoModel ExUpdateValidaInterruzione(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			PosizioneGiuridicaModel aPosGiu, PenaResiduaModel aPenaResidua,
			DecretoOrdinanzaSiepModel aDecreto) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PenaResiduaDAO lPenResDao = null;
		DecretoOrdinanzaSiepDAO lDecDao = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Valido l'evento
			// ========================================================================
			lEveDao = new EventoDAO(lConn);
			EventoModel lEveModel = new EventoModel(aEvento);

			lEveDao.setIdEvento(lEveModel.getIdEvento());
			lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

			// lEveDao.setFlagDocumentoRegistrato(null);
			lEveDao.setFlagDocumentoRegistrato("S"); // BLOB potrebbe essere null
			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// ========================================================================
			// Valido la Pena Residua
			// ========================================================================
			lPenResDao = new PenaResiduaDAO(lConn);

			PenaResiduaModel lPenResMod = new PenaResiduaModel(aPenaResidua);

			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			lPenResDao.setFlagValidato("S");

			// lPenResDao.setEveIdEvento(aEvento.getIdEvento());

			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			// ========================================================================
			// Validazione flag_elaborato DECRETO_ORDINANZA_SIEP
			// ========================================================================
			lDecDao = new DecretoOrdinanzaSiepDAO(lConn);

			lDecDao.setFlagElaborato("S");
			lDecDao.setCondizioneUpdate(aDecreto.getIdDecretoOrdinanzaSiep());
			lDecDao.update();
			lDecDao.stop();

			// ========================================================================
			// Aggiorno la posizione giuridica
			// ========================================================================
			if (lEveModel.getCodMotivo().equals("0266") // avvenuto decesso
					|| lEveModel.getCodMotivo().equals("0269") // esecuzione penale all'estero della condanna
																// ex art. 742 c.p.p.
					|| lEveModel.getCodMotivo().equals("0268") // consegna temporanea art. 709 comma 1c.p.p.
					|| lEveModel.getCodMotivo().equals("0267") // avvenuta evasione
					|| lEveModel.getCodMotivo().equals("0366") // Scarcerazione provvisoria ex art. 672 co. 3
																// c.p.p.
			) {
				// POSIZIONE_GIURIDICA corrente, aggiorno la data fine
				PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel(aPosGiu);

				lPosDao = new PosizioneGiuridicaDAO(lConn);
				lPosDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());

				lPosDao.setDataFine(aDecreto.getDataInterruzionePena());

				lPosDao.selByKey();
				lPosDao.update();
				lPosDao.stop();

				// inserimento nuova occorrenza
				PosizioneGiuridicaModel lPosizione = new PosizioneGiuridicaModel();

				if (lEveModel.getCodMotivo().equals("0267"))
					lPosizione.setCodPosizioneGiuridica("20"); // Evaso

				if (lEveModel.getCodMotivo().equals("0266"))
					lPosizione.setCodPosizioneGiuridica("48"); // Deceduto

				if (lEveModel.getCodMotivo().equals("0269"))
					lPosizione.setCodPosizioneGiuridica("30"); // Estradato

				if (lEveModel.getCodMotivo().equals("0268"))
					lPosizione.setCodPosizioneGiuridica("30"); // Estradato

				if (lEveModel.getCodMotivo().equals("0366"))
					lPosizione.setCodPosizioneGiuridica("10"); // Libero

				lPosizione.setDataInizio(aDecreto.getDataInterruzionePena());
				lPosizione.setCodPosizioneProcessuale("-");
				lPosizione.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPosizione.setIdEventoRiferimento(lEveModel.getIdEvento());

				lPosizione.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
				lPosizione.setDataInserimento(lEveMod.getDataAggiornamento());
				lPosizione.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());

				lPosDao.setDAOFromModel(lPosizione);
				lPosDao.insert();
				lPosDao.stop();

				// Modifica Stato Procedimento. Luigi 24-10-2005
				if (lEveModel.getCodMotivo().equals("0268")) {
					// Se il motivo è Consegna Temp. Art 709 bisogna cambiare lo stato del procedimento in
					// 0137
					// 0137 - Disposta consegna temporanea all'estero del condannato
					aggiornaStatoProcedimento(aFascicolo.getIdFascicoloSiep(), lEveModel, "0137", lConn);
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaInterruzione : " + daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqe);
			rollback(lConn);
			sqe.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaInterruzione : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaInterruzione : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lPosDao);
			cleanup(lPenResDao);
			cleanup(lDecDao);

			cleanup(lConn);
		}

		return lEveMod;
	}

	// Funzione che richiama l'aggiornamento dello Stato Procedimento. Luigi 24-10-2005
	private void aggiornaStatoProcedimento(BigDecimal aIdFascicoloSiep, EventoModel aEve,
			String aCodStatoProcedimento, Connection aConn) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("aggiornaStatoProcedimento: inizio");

		StatoProcedimentoModel lStatoMod = new StatoProcedimentoModel();
		lStatoMod.setCodStatoProcedimento(aCodStatoProcedimento);
		lStatoMod.setData(aEve.getDataEmissione());
		lStatoMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);
		lStatoMod.setDataInserimento(DateUtils.getSysDate());
		lStatoMod.setCodUfficioInserimento(aEve.getCodUfficioAggiornamento());
		lStatoMod.setCodOperatoreInserimento(aEve.getCodOperatoreAggiornamento());
		lStatoMod.setProgressivo(new BigDecimal(1));

		StatoProcedimentoDAO lStatoDao = null;

		try {
			lStatoDao = new StatoProcedimentoDAO(aConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aIdFascicoloSiep);
			lStatoDao.delete();

			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoMod);
			lStatoDao.insert();
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lStatoDao);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("aggiornaStatoProcedimento: fine");
	}

	public EventoModel ExUpdateValidaDecretoSospensione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		ScadenzarioDAO lScaDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		NotificaDAO lNotDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// evento
			// lEveDao = new EventoDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);

			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			/*
			 * lEveDao.setIdEvento(lEveModel.getIdEvento());
			 * lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			 * lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			 * lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
			 *
			 * lEveDao.setFlagDocumentoRegistrato("S"); lEveDao.selByKey(); lEveDao.update(); lEveDao.stop();
			 */

			// scadenzario simeone
			/*
			 * lScaSqlDao = new ScadenzarioSqlDAO(lConn); lScaDao = new ScadenzarioDAO(lConn);
			 *
			 * lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("01",
			 * aFascicolo.getIdFascicoloSiep()); ScadenzarioModel lScaMod = new ScadenzarioModel(); lScaMod =
			 * (ScadenzarioModel)lScaSqlDao.getModelByKey();
			 *
			 * if( lScaMod != null && lScaMod.getIdScadenzario() != null ) {
			 * lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
			 * lScaDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			 * lScaDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
			 * lScaDao.setDataAggiornamento(DateUtils.getSysDate());
			 * lScaDao.setDataInizioScadenza(lEveModel.getDataEmissione());
			 * lScaDao.setDataFineScadenza(DateUtils.moveDateTo(lEveModel.getDataEmissione(),
			 * java.util.Calendar.DAY_OF_MONTH, 30));
			 *
			 * lScaDao.update(); lScaDao.stop(); } else { lScaDao.setCodTipoScadenzario("01");
			 * lScaDao.setDataInizioScadenza(lEveModel.getDataEmissione());
			 * lScaDao.setDataFineScadenza(DateUtils.moveDateTo(lEveModel.getDataEmissione(),
			 * java.util.Calendar.DAY_OF_MONTH, 30));
			 * lScaDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			 * lScaDao.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
			 * lScaDao.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
			 * lScaDao.setDataInserimento(DateUtils.getSysDate()); lScaDao.insert(); lScaDao.stop(); }
			 */

			// SCADENZARIO SIMEONE:
			// Nel caso sia emesso un OE 0061 o 0117 viene inserita sulla tabella SCADENZARIO_SIEP
			// un record con DATA_INIZIO_SCADENZA uguale a quella di emissione dell'OE.
			// Funzionalmente questo record su SCADENZARIO_SIEP dice che l'OE Simeone è in attesa di notifica.
			// Se viene validato un decreto di irreperibilità, lo scadenzario in attesa di notifica
			// deve fare riferimento a questo nuovo evento

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Inizio Scadenzario Simeone");

			if ("0282".equals(lEveModel.getCodMotivo())) // 0282 = Irreperibilita'
			{
				lScaDao = new ScadenzarioDAO(lConn);

				// Controlla l'esistenza dello scadenzario (nel caso lo aggiorna)
				lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFascicolo.getIdFascicoloSiep(), "01");
				ScadenzarioModel lScaMod = (ScadenzarioModel) lScaDao.getModelByKey();
				lScaDao.stop();

				lScaDao.setCodTipoScadenzario("01");
				lScaDao.setDataInizioScadenza(lEveModel.getDataEmissione());
				lScaDao.setDataFineScadenza(null);
				lScaDao.setFlagVisto("N");
				lScaDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lScaDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lScaDao.setDataInserimento(DateUtils.getSysDate());
				lScaDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lScaDao.setCodStatoNotifica("A"); // Attesa notifica all'avvocato
				// lScaDao.setEveIdEvento(aEvento.getIdEvento());
				// lScaDao.setNotIdNotifica(lNotMod.getIdNotifica());

				if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
					lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDao.update();
				}

				lScaDao.stop();
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Fine Scadenzario Simeone");

			// Stato procedimento
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// cancellazione
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();
			lStatoDao.stop();

			// inserimento
			lStatoDao.setProgressivo(new BigDecimal(1));
			lStatoDao.setData(lEveModel.getDataEmissione());
			lStatoDao.setCodStatoProcedimento("0115");
			lStatoDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoDao.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
			lStatoDao.setDataInserimento(lEveMod.getDataAggiornamento());
			lStatoDao.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
			lStatoDao.insert();
			lStatoDao.stop();

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaDecretoSospensione : " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaDecretoSospensione : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lStatoDao);
			cleanup(lScaDao);
			cleanup(lEveDaoBlob);
			cleanup(lNotEveDao);
			cleanup(lNotDao);

			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * Stampa un documento Sospensioni
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumentoSospensioni(EventoNotificaModel aEvento,
			UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			// ========================================================================
			// Recupero i dati dell'evento e delle notifiche associate
			// ========================================================================
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveMod = lCtrl
					.ExRicercaEventoNotificaByKey(aEvento.getEvento().getIdEvento());

			lEveMod.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());

			// ========================================================================
			// Costriusco il TreeModel per la stampa
			// ========================================================================
			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			TreeModel lTree = lStampa.prelevaDatiSospensioni(lEveMod, aUtente);

			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());

			// ========================================================================
			// Genero il report (rtf) e aggiorno l'evento inserendo il report
			// ========================================================================
			ReportGenerator lReport = new ReportGenerator();
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			aEvento.getEvento().setDocBlobIn(lByteArrayInput);

			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("SospensioneController.ExStampaDocumentoSospensioni: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/*****************************************************************************
	 * Effettua l'inserimento di:
	 *
	 */
	public EventoModel ExInserisciEventoDecretoOrdinanzaSiep(DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep,
			EventoNotificaModel aEventoNot, CalcoloPenaModel aCalcoloPenaMod) throws F3BException {

		Connection lConn = null;

		DecretoOrdinanzaSiepDAO lDecDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaDAO lPenDao = null;
		PenaResiduaSqlDAO lPenSqlDao = null;
		SospensioneDAO lSosDao = null;
		PenaComplessivaSqlDAO lPenCompSqlDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;

		EventoModel lEventoRitono = new EventoModel();
		DecretoOrdinanzaSiepModel lDecMod = null;

		try {
			lConn = getDBTransaction();
			lNotDao = new NotificaDAO(lConn);
			EventoModel aEvento = new EventoModel(aEventoNot.getEvento());

			// INSERIMENTO O MODIFICA DECRETO_ORDINANZA_SIEP
			lDecMod = new DecretoOrdinanzaSiepModel(aDecretoOrdinanzaSiep);
			lDecDao = new DecretoOrdinanzaSiepDAO(lConn);

			// cerca l'eventuale record da aggiornare
			DecretoOrdinanzaSiepModel lDecModPresente = null;
			if (lDecMod.getIdDecretoOrdinanzaSiep() != null) {
				lDecDao.setCondizioneByIdDecretoOrdinanzaFlagNonElaborato(
						lDecMod.getIdDecretoOrdinanzaSiep());
				lDecModPresente = (DecretoOrdinanzaSiepModel) lDecDao.getModelByKey();
			}
			if (lDecModPresente == null) {
				lDecDao.setDAOFromModel(lDecMod);
				BigDecimal lKeyDecOrd = null;
				lKeyDecOrd = lDecDao.insert();

				lDecMod.setIdDecretoOrdinanzaSiep(lKeyDecOrd);
			} else {
				lDecDao.setDAOFromModelForUpdate(lDecMod);
				lDecDao.update();
				lDecMod.setIdEventoGenerato(lDecModPresente.getIdEventoGenerato());
			}
			lDecDao.stop();

			// INSERIMENTO O AGGIORNAMENTO EVENTO
			lEveDao = new EventoDAO(lConn);
			lEveSqlDAO = new EventoSqlDAO(lConn);

			EventoModel lEveMod = null;
			if (lDecMod.getIdEventoGenerato() != null) {
				lEveSqlDAO.ricercaEventoNonRegistratoByKey(lDecMod.getIdEventoGenerato());
				lEveMod = (EventoModel) lEveSqlDAO.getModelByKey();
			}

			BigDecimal lKeyEvento = null;
			// Se non presente oppure presente non validato
			if (lEveMod == null) {
				// Setto l'anno e il progressivo...
				BigDecimal lProgr = lEveSqlDAO.getProgressivo(aEvento);
				aEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				// Setto ID_DECRETO_ORDINANZA_SIEP
				aEvento.setDecIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());

				lEveDao.setDAOFromModel(aEvento);
				lKeyEvento = lEveDao.insert();
				aEvento.setIdEvento(lKeyEvento);
			} else // Se presente lo aggiorna
			{
				lKeyEvento = lEveMod.getIdEvento();

				lEveDao.setIdEvento(lKeyEvento);

				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				lEveDao.setCodTipoEvento(aEvento.getCodTipoEvento());
				lEveDao.setCodTipoProvvedimento(aEvento.getCodTipoProvvedimento());
				lEveDao.setCodMotivo(aEvento.getCodMotivo());
				lEveDao.setCodUfficioEmittente(aEvento.getCodUfficioEmittente());
				lEveDao.setCodLuogoEmittente(aEvento.getCodLuogoEmittente());
				lEveDao.setDataEmissione(aEvento.getDataEmissione());
				lEveDao.setDataTrasmissioneAtti(aEvento.getDataTrasmissioneAtti());
				lEveDao.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
				lEveDao.setFlagVideoSiep(aEvento.getFlagVideoSiep());
				lEveDao.setFlagStampaSiep(aEvento.getFlagStampaSiep());
				lEveDao.setDecIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());

				// !!Problema delle date aggiornamento!!
				lEveDao.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lEveDao.setDataInserimento(aEvento.getDataInserimento());
				lEveDao.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
				lEveDao.setCodOperatoreAggiornamento(null);
				lEveDao.setDataAggiornamento(null);
				lEveDao.setCodUfficioAggiornamento(null);

				if (aEvento.getFlagPiuMeno() != null)
					lEveDao.setFlagPiuMeno(aEvento.getFlagPiuMeno());

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				// *********** Cancella le NOTIFICHE associate al EVENTO ******************
				lNotDao.setCondizioneEvento(lKeyEvento);
				lNotDao.delete();
				lNotDao.stop();
			}

			// AGGIORNAMENTO DECRETO_ORDINANZA_SIEP CON ID_EVENTO generato
			lDecDao.setIdEventoGenerato(lKeyEvento);
			lDecDao.setIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep()); // Controllare che non sia
																					// null
			lDecDao.selByKey();
			lDecDao.update();
			lDecDao.stop();

			// INSERISCI NOTIFICHE

			BigDecimal lKeyAutorita = null;
			lAutDao = new AutoritaEsternaDAO(lConn);
			int count = 0;

			while (count < aEventoNot.getNotifiche().length) {
				if (aEventoNot.getNotifiche()[count] != null) {
					if (aEventoNot.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEventoNot.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEventoNot.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEventoNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEventoNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					aEventoNot.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					lNotDao.setDAOFromModel(aEventoNot.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			lEventoRitono.setIdEvento(lKeyEvento);

			// cancella pena residua non validata e relative sospensioni
			/*
			 * lPenDao = new PenaResiduaDAO(lConn);
			 * lPenDao.setCondizioneUpdateNonValidato(lDecMod.getFasSieIdFascicoloSiep()); List
			 * lListPosNonValidate = new ArrayList( lPenDao.getModels() ); lSosDao = new
			 * SospensioneDAO(lConn); for (Iterator i = lListPosNonValidate.iterator(); i.hasNext(); ) {
			 * PenaResiduaModel lPenaResidua = (PenaResiduaModel)i.next();
			 * lSosDao.setCondizioneIdPenaResidua(lPenaResidua.getIdPenaResidua()); lSosDao.delete();
			 * lSosDao.stop(); lPenDao.setCondizioneUpdate(lPenaResidua.getIdPenaResidua()); lPenDao.delete();
			 * lPenDao.stop(); }
			 */

			// Cerca la Posizione Giurica
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lDecMod.getFasSieIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			if (lPosMod == null || lPosMod.getCodPosizioneGiuridica() == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

			// Cerca la Pena Complessiva
			lPenCompSqlDao = new PenaComplessivaSqlDAO(lConn);
			lPenCompSqlDao.ricercaPenaComplessivaByIdFascicolo(lDecMod.getFasSieIdFascicoloSiep());
			PenaComplessivaModel lPenMod = (PenaComplessivaModel) lPenCompSqlDao.getModelByKey();

			if (lPenMod == null || lPenMod.getCodTipoPenaDetentiva() == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Inserire prima la Pena Complessiva. Impossibile eseguire la richiesta.");

			String lFlagErgastolo = "N";

			// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
			if (lPenMod.getCodTipoPenaDetentiva() != null && lPenMod.getCodTipoPenaDetentiva() != ""
					&& (lPenMod.getCodTipoPenaDetentiva().equals("03")
							|| lPenMod.getCodTipoPenaDetentiva().equals("04"))) {
				lFlagErgastolo = "S";
			}

			// INSERIMENTO PENA RESIDUA
			lPenSqlDao = new PenaResiduaSqlDAO(lConn);

			// Cerca l'ultima pena residua validata...
			lPenSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(
					lDecMod.getFasSieIdFascicoloSiep());
			PenaResiduaModel lUltimaPenaResidua = (PenaResiduaModel) lPenSqlDao.getModelByKey();
			// ...se non la trova cerca l'ultima in assoluto
			if (lUltimaPenaResidua == null) {
				lPenSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lDecMod.getFasSieIdFascicoloSiep());
				lUltimaPenaResidua = (PenaResiduaModel) lPenSqlDao.getModelByKey();
			}

			lPenSqlDao.stop();

			if (lUltimaPenaResidua == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Pena Residua non trovata, impossibile procedere.");

			PenaResiduaModel lPenaResiduaNuova = new PenaResiduaModel();
			CalendarModel lPenaEspiataSosp = new CalendarModel();

			if (lFlagErgastolo.equals("N")) {
				// Pena Ricalcolata sul
				PenaResiduaModel lPenaResiduaIniziale = aCalcoloPenaMod
						.getPenaDaEspiare(lUltimaPenaResidua.getDataInizio(), null, "all");
				aCalcoloPenaMod.calcolaPenaDaSospensione(lPenaResiduaIniziale,
						lDecMod.getDataSospensioneEsecuzione());
				lPenaResiduaNuova = aCalcoloPenaMod.getPenaResiduaRicalcolata();
				lPenaEspiataSosp = aCalcoloPenaMod.getPenaEspiata();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lPenaResiduaNuova : " + lPenaResiduaNuova);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lPenaEspiataSosp : " + lPenaEspiataSosp);

				// Vengono settati quei parametri
				// che non vengono gestiti nel CalcoloPenaModel
				// lPenaResiduaNuova.setIdPenaResidua( lUltimaPenaResidua.getIdPenaResidua() );
				lPenaResiduaNuova.setFasSieIdFascicoloSiep(lDecMod.getFasSieIdFascicoloSiep());
				lPenaResiduaNuova.setDiesAQuo(lUltimaPenaResidua.getDiesAQuo());
				lPenaResiduaNuova.setFlagErgastolo(lUltimaPenaResidua.getFlagErgastolo());
				lPenaResiduaNuova.setFlagValidato("N");
				lPenaResiduaNuova.setFlagPenaSospesa("S");
				lPenaResiduaNuova.setEveIdEvento(lKeyEvento);
			} else {
				lPenaResiduaNuova = lUltimaPenaResidua;

				lPenaResiduaNuova.setMisAltIdMisuraAlternativa(null);
				lPenaResiduaNuova.setEveIdEvento(lKeyEvento);
				lPenaResiduaNuova.setFlagPenaSospesa("S");
				lPenaResiduaNuova.setFlagValidato("N");
			}

			// Gestione della Data Inserimento
			lPenaResiduaNuova.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
			lPenaResiduaNuova.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
			lPenaResiduaNuova.setDataInserimento(aEvento.getDataInserimento());
			lPenaResiduaNuova.setCodOperatoreAggiornamento(null);
			lPenaResiduaNuova.setDataAggiornamento(null);
			lPenaResiduaNuova.setCodUfficioAggiornamento(null);

			if (lFlagErgastolo.equals("S")) {
				lPenaResiduaNuova.setDataFine(DateUtils.getDate(9999, 12, 31));
				if (lPenMod.getCodTipoPenaDetentiva().equals("03")) {
					lPenaResiduaNuova.setFlagErgastolo("S");
				} else if (lPenMod.getCodTipoPenaDetentiva().equals("04")) {
					lPenaResiduaNuova.setFlagErgastolo("D");
				}
			}

			lPenaResiduaNuova = lPenSqlDao.inserisciOModificaPenaResidua(lPenaResiduaNuova);
			BigDecimal lKeyPena = lPenaResiduaNuova.getIdPenaResidua();

			// INSERIMENTO SOSPENSIONE
			if (!lPosMod.isLibero() && lUltimaPenaResidua.getDataInizio() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Prepara SopensioneModel");

				lSosDao = new SospensioneDAO(lConn);
				SospensioneModel lSospensione = new SospensioneModel();

				lSospensione.setDataInizio(lDecMod.getDataSospensioneEsecuzione());

				if (lFlagErgastolo.equals("N")) {
					lSospensione.setNumAnniPenaResiduaReclus(lPenaResiduaNuova.getNumAnniReclusione());
					lSospensione.setNumMesiPenaResiduaReclus(lPenaResiduaNuova.getNumMesiReclusione());
					lSospensione.setNumGiorniPenaResiduaReclus(lPenaResiduaNuova.getNumGiorniReclusione());
					lSospensione.setNumAnniPenaResiduaArres(lPenaResiduaNuova.getNumAnniArresto());
					lSospensione.setNumMesiPenaResiduaArres(lPenaResiduaNuova.getNumMesiArresto());
					lSospensione.setNumGiorniPenaResiduaArres(lPenaResiduaNuova.getNumGiorniArresto());

					lSospensione.setAmmendaResidua(lPenaResiduaNuova.getImportoAmmenda());
					lSospensione.setMultaResidua(lPenaResiduaNuova.getImportoMulta());

					lSospensione.setNumAnniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumAnni()));
					lSospensione.setNumMesiPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumMesi()));
					lSospensione.setNumGiorniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumGiorni()));
				} else {
					// Calcolo la pena espiata come intervallo tra la data inizio e la data
					// di sospensione (considerato come giorno espiato)
					CalendarModel lCalPenaEspiataCalcoloErg = new CalendarModel();
					CalendarUtil lCalUtil = new CalendarUtil();

					lCalPenaEspiataCalcoloErg.setDataInizio(lUltimaPenaResidua.getDataInizio());
					lCalPenaEspiataCalcoloErg.setDataFine(lDecMod.getDataSospensioneEsecuzione());

					lCalPenaEspiataCalcoloErg = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiataCalcoloErg);
					lCalPenaEspiataCalcoloErg = lCalUtil.ricalcolaGAM(lCalPenaEspiataCalcoloErg);

					lSospensione
							.setNumAnniPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumAnni()));
					lSospensione
							.setNumMesiPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumMesi()));
					lSospensione.setNumGiorniPenaEspiata(
							new BigDecimal(lCalPenaEspiataCalcoloErg.getNumGiorni()));
				}

				lSospensione.setPenResIdPenaResidua(lKeyPena);
				lSospensione.setFasSieIdFascicoloSiep(aDecretoOrdinanzaSiep.getFasSieIdFascicoloSiep());
				lSospensione.setNumGiorniLibanticipata(
						new BigDecimal(aCalcoloPenaMod.getLiberazioneAnticipata()));

				lSospensione.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lSospensione.setDataInserimento(aEvento.getDataInserimento());
				lSospensione.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());

				lSosDao.setDAOFromModel(lSospensione);
				lSosDao.insert();
				lSosDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("SospensioneController.ExInserisciEventoDecretoOrdinanzaSiep: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqe);
			throw new F3BException("SospensioneController.ExInserisciEventoDecretoOrdinanzaSiep: " + sqe);
		} catch (SIEPException siepEx) {
			rollback(lConn);
			siepEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SIEPException: " + siepEx);
			throw siepEx;
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			throw new F3BException("SospensioneController.ExInserisciEventoDecretoOrdinanzaSiep: " + ex);
		} finally {
			cleanup(lDecDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lPosSqlDao);
			cleanup(lPenDao);
			cleanup(lPenSqlDao);
			cleanup(lSosDao);
			cleanup(lPenCompSqlDao);
			cleanup(lNotDao);
			cleanup(lAutDao);

			cleanup(lConn);
		}

		return lEventoRitono;
	}

	/**
	 * Validazione del Provvedimento/Ordine Scarcerazione legato alla Sospensione del PM - valida l'evento -
	 * valida la pena residua - aggiorna la posizione giuridica - nome provvedimento - aggiorna stato
	 * procedimento - aggiorna il blob - Cancella l'eventuale Scadenzario Simeone
	 *
	 * @param
	 * @param
	 * @return
	 */
	public EventoModel ExUpdateValidaSospensioneEsecPenaDispPm(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecSqlDao = null;
		ScadenzarioDAO lScaDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ================================================
			// Recupera l'evento ed effettua la Validazione
			// ================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();
			lEveSqlDao.stop();

			lEveDao = new EventoDAO(lConn);
			lEveDao.setIdEvento(lEveModel.getIdEvento());
			lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

			// modifica dario--umberto per errore jasper segnalato da imperia non deve
			// mettere a null il flag piumeno
			// lEveDao.setFlagPiuMeno(null);
			lEveDao.setFlagDocumentoRegistrato("S");
			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// ===================================
			// Ricerca Pena residua e la Valida
			// ===================================
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

			// Ricerca pena residua sospesa
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaNonValidataSospesa(
					aFascicolo.getIdFascicoloSiep());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			if (lPenResMod != null) {
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
				lPenResDao.setFlagValidato("S");
				// lPenResDao.setEveIdEvento(aEvento.getIdEvento());
				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			}

			/*
			 * modifica del 05-05-2005 -- Dario --Luciana nn deve inserire una nuova pena ma aggiornare
			 * sempre, l'id dell'evento è già presente grazie alla fase d'inserimento quindi si mette solo la
			 * 'S' in FlagValidato
			 *
			 * PenaResiduaModel lPenResMod = new PenaResiduaModel();
			 * lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			 * lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey(); if (lPenResMod.getEveIdEvento()
			 * == null) { lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			 * lPenResDao.setFlagValidato("S");
			 * lPenResDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			 * lPenResDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
			 * lPenResDao.setDataAggiornamento(DateUtils.getSysDate()); lPenResDao.selByKey();
			 * lPenResDao.update(); lPenResDao.stop(); } else { lPenResDao.setDAOFromModel(lPenResMod);
			 * lPenResDao.setFlagValidato("S"); lPenResDao.setEveIdEvento(lEveModel.getIdEvento());
			 * lPenResDao.setDataInserimento(DateUtils.getSysDate());
			 * lPenResDao.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
			 * lPenResDao.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento()); lPenResDao.insert();
			 * lPenResDao.stop(); }
			 */

			// ============================================
			// Posizione giuridica
			// ============================================
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao = new PosizioneGiuridicaDAO(lConn);

			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			// ===================================
			// cerco decreto ordinanza siep
			// ===================================
			lDecSqlDao = new DecretoOrdinanzaSiepSqlDAO(lConn);
			DecretoOrdinanzaSiepModel lDecOrdMod = new DecretoOrdinanzaSiepModel();
			lDecSqlDao
					.ricercaDecretoOrdinanzaSiepByFascicoloSiepFlagElaborato(aFascicolo.getIdFascicoloSiep());
			lDecOrdMod = (DecretoOrdinanzaSiepModel) lDecSqlDao.getModelByKey();

			if (lPosMod != null && lPosMod.getDataFine() == null && lDecOrdMod != null
					&& lDecOrdMod.getDataSospensioneEsecuzione() != null) {
				lPosDao.setDataFine(lDecOrdMod.getDataSospensioneEsecuzione());
				lPosDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				lPosDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());
				lPosDao.update();
				lPosDao.stop();
			}

			// =============================================
			// Inserisco la nuova posizione giuridica (46)
			// =============================================
			if (lPosMod != null) {
				lPosDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPosDao.setIdEventoRiferimento(lEveModel.getIdEvento());
				lPosDao.setCodPosizioneGiuridica("46"); // Libero in Sospensione
				if (lDecOrdMod != null)
					lPosDao.setDataInizio(lDecOrdMod.getDataSospensioneEsecuzione());
				lPosDao.setCodPosizioneProcessuale("-");

				lPosDao.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(lEveMod.getDataAggiornamento());
				lPosDao.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());

				lPosDao.insert();
				lPosDao.stop();
			}

			// ========================================
			// Aggiorna tabella nome_provvedimento
			// ========================================
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP208"); // Rideterminazione pena - altro
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// =======================================================
			// Aggiorno lo stato del procedimento
			// cancello quello corrente e ne inserisco uno nuovo
			// =======================================================
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();
			lStatoDao.stop();

			// inserimento
			lStatoDao.setProgressivo(new BigDecimal(1));
			lStatoDao.setData(lEveModel.getDataEmissione());
			lStatoDao.setCodStatoProcedimento("0084"); // Emesso Decreto Sospensione Pena Detentiva
			lStatoDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoDao.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
			lStatoDao.setDataInserimento(lEveMod.getDataAggiornamento());
			lStatoDao.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
			lStatoDao.insert();

			// ===================================================================
			// Cancella tutti gli scadenzari di tipo LEGGE SIMEONE ( Tipo = 01 )
			// ===================================================================
			lScaDao = new ScadenzarioDAO(lConn);
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFascicolo.getIdFascicoloSiep(), "01");
			lScaDao.delete();

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"SospensioneController.ExUpdateValidaSospensioneEsecPenaDispPm : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaSospensioneEsecPenaDispPm : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lStatoDao);
			cleanup(lNomProvvDAO);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lPosDao);
			cleanup(lDecSqlDao);
			cleanup(lScaDao);
			cleanup(lEveDaoBlob);

			cleanup(lConn);
		}

		return lEveMod;
	}

	public EventoModel ExUpdateValidaSospensioneDecisioniSorveglianza(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		EventoDAO lEveDaoMisAlt = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();
			String motivo = lEveModel.getCodMotivo();

			lEveModel.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lEveModel.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lEveModel.setDataAggiornamento(aEvento.getDataAggiornamento());

			// cerca la misura
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			// lMisSqlDAO.ricercaMisuraAlternativaByKey(lKeyMis);
			lMisSqlDAO.ricercaMisuraAlternativaByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisMDS = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento riferito alla misura alternativa modifica del 11-10-04 --dario
			if (lMisMDS != null && lMisMDS.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisMDS.getEveIdEvento());
				EventoModel lEveModelMis = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);
				if (lEveModelMis != null && (lEveModelMis.getFlagDocumentoRegistrato() == null
						|| lEveModelMis.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelMis.setFlagDocumentoRegistrato("S");
					lEveModelMis.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelMis.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelMis.setDataAggiornamento(DateUtils.getSysDate());
					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelMis);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}
			
			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			// Aggiorna NOME_PROVVREDIMENTO
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			if (motivo.equals("0263"))
				lNomProvvDAO.setCodNomeProvvedimento("NP104");
			else if (motivo.equals("0241"))
				lNomProvvDAO.setCodNomeProvvedimento("NP105");
			// MEV_9-SIEP: gestione nuovi codici tipo misura
			else if (motivo.equals("5469") || motivo.equals("5496")) 
				lNomProvvDAO.setCodNomeProvvedimento("NP104");

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = null;
			if (motivo.equals("0263"))
				lStatoProcMod = "0083";
			else if (motivo.equals("0241"))
				lStatoProcMod = "0155";
			// MEV_9-SIEP: gestione nuovi codici tipo misura
			else if (motivo.equals("5469") || motivo.equals("5496"))
				lStatoProcMod = "0577";

			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					lStatoProcMod);

			// Aggiorna Inserisci PENA_RESIDUA
			// PenaResiduaModel lPenResMod = new PenaResiduaModel();
			/* lPenResMod = */InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep());

			// Aggiorna POSIZIONE_GIURIDICA
			String lPosizione = null;
			if (motivo.equals("0263"))
				lPosizione = "46";
			else if (motivo.equals("0241"))
				lPosizione = "47";
			// MEV_9-SIEP: gestione nuovi codici tipo misura
			else if (motivo.equals("5469") || motivo.equals("5496"))
				lPosizione = "46";

			Date lData = lEveModel.getDataEmissione();
			if (lMisMDS != null && lMisMDS.getDataScarcerazione() != null)
				lData = lMisMDS.getDataScarcerazione();// data sospensione esecuzione

			InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizione, lPosMod, lData, lEveModel,
					aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"SospensioneController.ExUpdateValidaSospensioneDecisioniSorveglianza : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(
					"SospensioneController.ExUpdateValidaSospensioneDecisioniSorveglianza : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveMod;
	}

	// ==============================================================================
	// METODI PRIVATI
	// ==============================================================================
	private PenaResiduaModel InserimentoAggiornamentoPenResMisuraAlternativa(Connection lConn,
			EventoModel lEveModel, BigDecimal aKey) throws DAOException, F3BException {

		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaModel lPenResMod = null;

		try {
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResMod = new PenaResiduaModel();

			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aKey);
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			if (lPenResMod.getEveIdEvento() == null) {
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
				lPenResDao.setEveIdEvento(lEveModel.getIdEvento());
				lPenResDao.setFlagValidato("S");
				lPenResDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lPenResDao.setDataAggiornamento(DateUtils.getSysDate());
				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				lPenResDao.setDAOFromModel(lPenResMod);
				lPenResDao.setFlagValidato("S");
				lPenResDao.setEveIdEvento(lEveModel.getIdEvento());
				lPenResDao.setDataInserimento(DateUtils.getSysDate());
				lPenResDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lPenResDao.insert();
				lPenResDao.stop();
			}
		} finally {
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
		}

		return lPenResMod;
	}

	// Aggiorna/inserisce POSIZIONE_GIURIDICA
	private BigDecimal InserimentoAggiornamentoPosizioneGiuridica(Connection lConn, String lPosizione,
			PosizioneGiuridicaModel lPos, Date lData, EventoModel lEveModel, BigDecimal aKeyFasc,
			BigDecimal aKeyEve) throws DAOException, F3BException {

		PosizioneGiuridicaDAO lPosDao = null;
		BigDecimal lIdPosizioneGiuridica = null;

		try {

			lPosDao = new PosizioneGiuridicaDAO(lConn);

			if (lPos != null && lPos.getDataFine() == null) {
				lPosDao.setDataFine(lData);
				lPosDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lPosDao.setDataAggiornamento(lEveModel.getDataAggiornamento());
				lPosDao.setCondizioneUpdate(lPos.getIdPosizioneGiuridica());
				lPosDao.update();
				lPosDao.stop();
			}
			if (lPosizione != null) {
				lPosDao.setCodPosizioneGiuridica(lPosizione);
				lPosDao.setDataInizio(lData);
				lPosDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(lEveModel.getDataAggiornamento());
				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lPosDao.setFasSieIdFascicoloSiep(aKeyFasc);
				lPosDao.setIdEventoRiferimento(aKeyEve);

				lIdPosizioneGiuridica = lPosDao.insert();
				lPosDao.stop();
			}
		} finally {
			// sca
			cleanup(lPosDao);
		}

		return lIdPosizioneGiuridica;
	}

	private void InserimentoCancellazioneStatoProcedimento(Connection lConn, BigDecimal aKey,
			EventoModel lEveModel, String lStatoProcMod) throws DAOException, F3BException {

		StatoProcedimentoDAO lStatoDao = new StatoProcedimentoDAO(lConn);

		try {
			// cancellazione
			lStatoDao.setCondizioneByIdFascicolo(aKey);
			lStatoDao.delete();
			lStatoDao.stop();
			// inserimento
			lStatoDao.setProgressivo(new BigDecimal(1));
			lStatoDao.setData(lEveModel.getDataEmissione());
			lStatoDao.setCodStatoProcedimento(lStatoProcMod);
			lStatoDao.setFasSieIdFascicoloSiep(aKey);
			lStatoDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
			lStatoDao.setDataInserimento(lEveModel.getDataAggiornamento());
			lStatoDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
			lStatoDao.insert();
		} finally {
			cleanup(lStatoDao);
		}
	}

	/**
	 * Effettua l'inserimento del Provvedimento conseguente un Differimento. Eventualmente calcola la pena
	 * residua e la sospensione.
	 *
	 * @param aEventoNotifica
	 *            - model contenente i dati dell'evento da inserire e delle relative notifiche. Il campo
	 *            mEveIdEvento dell'EventoModel deve essere valorizzato con l'id dell'Evento legato al
	 *            provvedimento della Sorveglianza.
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciEventoDifferimento(String aTipoDifferimento,
			EventoNotificaModel aEventoNotifica, CalcoloPenaModel aCalcoloPenaMod) throws F3BException {

		Connection lConn = null;

		/*
		 * In realtà questo metodo registra un provvedimento dell'esecuzione che può essere: - 12
		 * (comunicazione) - in caso di differimento provv/def di condannato libero o comunque scarcerato - 09
		 * (ordine di scarcerazione)- in caso di differimento provv/def di condannato
		 * detenuto/ArrestiDomiciliari/Misura Alternativa - 06 (ordine di esecuzione) - rigetto solo di
		 * condannato in differimento provvisorio - ?? (??) - revoca
		 *
		 * Le operazioni da compiere sono: - Registrazione del Provvedimento - Registrazione delle notifiche -
		 * Eventuale calcolo e inserimento della pena residua e pena espiata solo se detenuto se rigetto la
		 * pena residua va aggiornata (duplicata?) per togliere il flag pena sospesa = D
		 */

		EventoNotificaModel lEventoNotifica = new EventoNotificaModel(aEventoNotifica);

		// Dichiarazione DAO e sqlDAO
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		MisuraAlternativaSqlDAO lMisAltSqlDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		EventoDAO lEveDao = null;
		SospensioneDAO lSospDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaComplessivaSqlDAO lPenCompSqlDao = null;

		BigDecimal idFascicoloSiep = aEventoNotifica.getEvento().getFasSieIdFascicoloSiep();
		String lCodOperatoreInserimento = aEventoNotifica.getEvento().getCodOperatoreInserimento();
		String lCodUfficioInserimento = aEventoNotifica.getEvento().getCodUfficioInserimento();
		Date lDataInserimento = aEventoNotifica.getEvento().getDataInserimento();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("aTipoDifferimento = " + aTipoDifferimento);

		try {
			lConn = getDBTransaction();

			/*
			 * TODO Attenzione i vari step vanno subordinati al tipo di provvedimento che si sta emettendo
			 */

			// ========================================================================
			// Recupero la posizione Giuridica
			// ========================================================================
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(idFascicoloSiep);
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lPosMod = " + lPosMod);

			// ========================================================================
			// Recupero l'ultima Pena Residua a sistema da cui partire per i calcoli
			// ========================================================================
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

			// Cerca l'ultima pena residua validata...
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(idFascicoloSiep);
			PenaResiduaModel lUltimaPenaResidua = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			// ...se non la trova cerca l'ultima in assoluto
			if (lUltimaPenaResidua == null) {
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(idFascicoloSiep);
				lUltimaPenaResidua = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			}
			lPenResSqlDao.stop();

			if (lUltimaPenaResidua == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Pena Residua non trovata, impossibile procedere.");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lUltimaPenaResidua = " + lUltimaPenaResidua);

			// ========================================================================
			// Recupero la Pena Complessiva per verificare se trattasi di ergastolo
			// ========================================================================
			lPenCompSqlDao = new PenaComplessivaSqlDAO(lConn);

			lPenCompSqlDao.ricercaPenaComplessivaByIdFascicolo(idFascicoloSiep);
			PenaComplessivaModel lPenCompMod = (PenaComplessivaModel) lPenCompSqlDao.getModelByKey();

			// Verifico il tipo di pena (se ergastolo)
			String lFlagErgastolo = "N";
			if (lPenCompMod.getCodTipoPenaDetentiva() != null && lPenCompMod.getCodTipoPenaDetentiva() != ""
					&& (lPenCompMod.getCodTipoPenaDetentiva().equals("03")
							|| lPenCompMod.getCodTipoPenaDetentiva().equals("04"))) {
				if (lPenCompMod.getCodTipoPenaDetentiva().equals("03")) {
					lFlagErgastolo = "S";
				} else if (lPenCompMod.getCodTipoPenaDetentiva().equals("04")) {
					lFlagErgastolo = "D";
				}
			}

			// ========================================================================
			// Recupero la Misura Alternativa per ottenere la data differimento
			// ========================================================================
			BigDecimal idEventoSorv = aEventoNotifica.getEvento().getEveIdEvento();
			lMisAltSqlDao = new MisuraAlternativaSqlDAO(lConn);
			lMisAltSqlDao.ricercaMisuraAlternativaByIdEvento(idEventoSorv);
			MisuraAlternativaModel lMisAltMod = (MisuraAlternativaModel) lMisAltSqlDao.getModelByKey();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lMisAltMod = " + lMisAltMod.toString());
			// ------------------------------------------------------------------------------

			// ========================================================================
			// Registro il Provvedimento (evento) dell'Esecuzione
			// ========================================================================
			lEveSqlDAO = new EventoSqlDAO(lConn);

			BigDecimal lProgr = lEveSqlDAO.getProgressivo(lEventoNotifica.getEvento());
			lEventoNotifica.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Evento = " + lEventoNotifica.getEvento());
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(lEventoNotifica.getEvento());

			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEventoNotifica.getEvento().setIdEvento(lKeyEvento);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lKeyEvento = " + lKeyEvento);

			// ========================================================================
			// Ciclo di caricamento delle notifiche
			// ========================================================================
			BigDecimal lKeyAutorita = null;
			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);

			int count = 0;
			while (count < lEventoNotifica.getNotifiche().length) {
				if (lEventoNotifica.getNotifiche()[count] != null) {
					// Se è stata specificata anche l'autorità esterna per l'avvocato,
					// recupero l'id da inserire nella notifica
					// n.b. se autorità non presente la creo
					if (lEventoNotifica.getNotifiche()[count].getAutoritaEsterna() != null) {
						// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
						// già l'autorità esterna specificata nella form (dalla form ho solo
						// codice e sede)
						lAutDao.setRicercaByAutSede(
								lEventoNotifica.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
												// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
												// istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.debug("Ins Aut Est = "
									+ lEventoNotifica.getNotifiche()[count].getAutoritaEsterna());
							lAutDao.setDAOFromModel(
									lEventoNotifica.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							lEventoNotifica.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							lEventoNotifica.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					// Collego la notifica all'evento
					lEventoNotifica.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					// Inserisco la notifica
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica = " + lEventoNotifica.getNotifiche()[count]);
					lNotDao.setDAOFromModel(lEventoNotifica.getNotifiche()[count]);
					BigDecimal lKeyNotifica = lNotDao.insert();
					lNotDao.stop();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lKeyNotifica = " + lKeyNotifica);
				}

				count++;
			}

			// ========================================================================
			// Calcolo la nuova Pena Residua e Sospensione
			// - se differimento provvisorio o definitivo e soggetto NON LIBERO
			// effettuo il calcolo della pena e della sospensione
			// - se differimento provvisorio o definitivo e soggetto LIBERO duplico la
			// pena residua e la inserisco con flag_pena_sospesa= 'D' ma NON inserisco
			// la sospensione
			// - se rigetto di soggetto in differimento provvisorio duplico la
			// pena residua e aggiorno il FLAG_PENA_SOSPESA a null
			// - se revoca (???)
			// n.b. la pena viene comunque sempre inserita e legata all'evento,
			// eventualmente viene semplicemente duplicata con l'aggiornamento
			// del flag_pena_sospesa
			// n.b. la sospensione viene inserita solo se la pena viene effettivamente
			// sospesa
			// - 12 (comunicazione) - in caso di differimento provv/def di condannato libero o comunque
			// scarcerato
			// - 09 (ordine di scarcerazione)- in caso di differimento provv/def di condannato
			// detenuto/ArrestiDomiciliari/Misura Alternativa
			// - 06 (ordine di esecuzione) - rigetto solo di condannato in differimento provvisorio
			// ========================================================================
			PenaResiduaModel lPenaResiduaNuova = null;
			CalendarModel lPenaEspiataSosp = new CalendarModel();

			SospensioneModel lSospensione = null;

			if (!lPosMod.isLibero()) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Non Libero: calcolo la pena residua");

				if (lFlagErgastolo.equals("N")) {
					// Pena Ricalcolata sul
					PenaResiduaModel lPenaResiduaIniziale = aCalcoloPenaMod
							.getPenaDaEspiare(lUltimaPenaResidua.getDataInizio(), null, "all");
					aCalcoloPenaMod.calcolaPenaDaSospensione(lPenaResiduaIniziale,
							lMisAltMod.getDataInizioMisura());
					lPenaResiduaNuova = aCalcoloPenaMod.getPenaResiduaRicalcolata();
					lPenaEspiataSosp = aCalcoloPenaMod.getPenaEspiata();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lPenaResiduaNuova : " + lPenaResiduaNuova);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lPenaEspiataSosp : " + lPenaEspiataSosp);

					// Vengono settati quei parametri
					// che non vengono gestiti nel CalcoloPenaModel
					// lPenaResiduaNuova.setIdPenaResidua( lUltimaPenaResidua.getIdPenaResidua() );
					lPenaResiduaNuova.setFlagErgastolo(lUltimaPenaResidua.getFlagErgastolo());
				} else {
					// In caso di ergastolo non effettuo il ricalcolo della pena, i quantum
					// restano invariati
					lPenaResiduaNuova = lUltimaPenaResidua;

					lPenaResiduaNuova.setDataInizio(null);
					lPenaResiduaNuova.setDataFine(null);
					lPenaResiduaNuova.setFlagErgastolo(lFlagErgastolo);
				}

				lPenaResiduaNuova.setIdPenaResidua(null);
				lPenaResiduaNuova.setFlagValidato("N");
				lPenaResiduaNuova.setDiesAQuo("S");

				// ========================================================================
				// Calcolo la Pena Espiata carico il model della Sospensione
				// ========================================================================
				if (lUltimaPenaResidua.getDataInizio() != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Prepara SopensioneModel");

					lSospensione = new SospensioneModel();
					lSospensione.setDataInizio(lMisAltMod.getDataInizioMisura());

					if (lFlagErgastolo.equals("N")) {
						lSospensione.setNumAnniPenaResiduaReclus(lPenaResiduaNuova.getNumAnniReclusione());
						lSospensione.setNumMesiPenaResiduaReclus(lPenaResiduaNuova.getNumMesiReclusione());
						lSospensione
								.setNumGiorniPenaResiduaReclus(lPenaResiduaNuova.getNumGiorniReclusione());
						lSospensione.setNumAnniPenaResiduaArres(lPenaResiduaNuova.getNumAnniArresto());
						lSospensione.setNumMesiPenaResiduaArres(lPenaResiduaNuova.getNumMesiArresto());
						lSospensione.setNumGiorniPenaResiduaArres(lPenaResiduaNuova.getNumGiorniArresto());

						lSospensione.setAmmendaResidua(lPenaResiduaNuova.getImportoAmmenda());
						lSospensione.setMultaResidua(lPenaResiduaNuova.getImportoMulta());

						lSospensione.setNumAnniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumAnni()));
						lSospensione.setNumMesiPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumMesi()));
						lSospensione.setNumGiorniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumGiorni()));
					} else {
						// Calcolo la pena espiata come intervallo tra la data inizio e la data
						// di sospensione (considerato come giorno espiato)
						CalendarModel lCalPenaEspiataCalcoloErg = new CalendarModel();
						CalendarUtil lCalUtil = new CalendarUtil();

						lCalPenaEspiataCalcoloErg.setDataInizio(lUltimaPenaResidua.getDataInizio());
						lCalPenaEspiataCalcoloErg.setDataFine(lMisAltMod.getDataInizioMisura());

						lCalPenaEspiataCalcoloErg = lCalUtil
								.CalcolaNumGiorniMesiAnni(lCalPenaEspiataCalcoloErg);
						lCalPenaEspiataCalcoloErg = lCalUtil.ricalcolaGAM(lCalPenaEspiataCalcoloErg);

						lSospensione.setNumAnniPenaEspiata(
								new BigDecimal(lCalPenaEspiataCalcoloErg.getNumAnni()));
						lSospensione.setNumMesiPenaEspiata(
								new BigDecimal(lCalPenaEspiataCalcoloErg.getNumMesi()));
						lSospensione.setNumGiorniPenaEspiata(
								new BigDecimal(lCalPenaEspiataCalcoloErg.getNumGiorni()));
					}
				}
			} else {
				// In caso di soggetto libero devo semplicemente duplicare la pena
				// residua impostando opportunamente il FLAG_PENA_SOSPESA
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Libero: duplico la pena resiua e aggiorno FLAG_PENA_SOSPESA");

				lPenaResiduaNuova = lUltimaPenaResidua;

				lPenaResiduaNuova.setIdPenaResidua(null);

				lPenaResiduaNuova.setFlagValidato("N");
				lPenaResiduaNuova.setDiesAQuo("S");

				lPenaResiduaNuova.setDataInizio(null);
				lPenaResiduaNuova.setDataFine(null);
				lPenaResiduaNuova.setFlagErgastolo(lFlagErgastolo);
			}

			// Imposto il flagPenaSospesa
			if (aTipoDifferimento.equals(ICostantiSospensione.DIFFERIMENTO_PROV)
					|| aTipoDifferimento.equals(ICostantiSospensione.DIFFERIMENTO_DEF)) {
				lPenaResiduaNuova.setFlagPenaSospesa("D");
			} else if (aTipoDifferimento.equals(ICostantiSospensione.DIFFERIMENTO_REVOCA)
					|| aTipoDifferimento.equals(ICostantiSospensione.DIFFERIMENTO_RIGETTO)) {
				// Ero in differimento provvisorio devo ripristinare la pena residua
				lPenaResiduaNuova.setFlagPenaSospesa("");
			}

			// ========================================================================
			// Registro la Nuova Pena Residua
			// ========================================================================
			lPenaResiduaNuova.setEveIdEvento(lKeyEvento);
			lPenaResiduaNuova.setFasSieIdFascicoloSiep(idFascicoloSiep);

			lPenaResiduaNuova.setCodOperatoreInserimento(lCodOperatoreInserimento);
			lPenaResiduaNuova.setCodUfficioInserimento(lCodUfficioInserimento);
			lPenaResiduaNuova.setDataInserimento(lDataInserimento);
			lPenaResiduaNuova.setCodOperatoreAggiornamento(null);
			lPenaResiduaNuova.setDataAggiornamento(null);
			lPenaResiduaNuova.setCodUfficioAggiornamento(null);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lPenaResiduaNuova = " + lPenaResiduaNuova);

			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResDao.setDAOFromModel(lPenaResiduaNuova);
			BigDecimal lKeyPenaRes = lPenResDao.insert();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lKeyPenaRes = " + lKeyPenaRes);

			// ========================================================================
			// Registro la Nuova Sospensione
			// ========================================================================
			if (lSospensione != null) {
				lSospensione.setPenResIdPenaResidua(lKeyPenaRes);
				lSospensione.setFasSieIdFascicoloSiep(idFascicoloSiep);
				lSospensione.setNumGiorniLibanticipata(
						new BigDecimal(aCalcoloPenaMod.getLiberazioneAnticipata()));

				lSospensione.setCodOperatoreInserimento(lCodOperatoreInserimento);
				lSospensione.setCodUfficioInserimento(lCodUfficioInserimento);
				lSospensione.setDataInserimento(lDataInserimento);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lSospensione = " + lSospensione);

				lSospDao = new SospensioneDAO(lConn);
				lSospDao.setDAOFromModel(lSospensione);
				BigDecimal lKeySosp = lSospDao.insert();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lKeySosp = " + lKeySosp);
			}

			// rollback(lConn);
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"SospensioneController.ExInserisciEventoDifferimento: Non posso inserire: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException(
					"SospensioneController.ExInserisciEventoDifferimento: Non posso inserire: " + e);
		} finally {
			cleanup(lPenCompSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lMisAltSqlDao);
			cleanup(lEveSqlDAO);
			cleanup(lEveDao);
			cleanup(lSospDao);
			cleanup(lNotDao);
			cleanup(lAutDao);

			cleanup(lConn);
		}

		return lEventoNotifica;
	}

	/**
	 * Stampa un documento dei Differimenti
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumentoDifferimento(EventoNotificaModel aEventoNot,
			UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			// ========================================================================
			// Recupero i dati dell'evento e delle notifiche associate
			// ========================================================================
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveMod = lCtrl
					.ExRicercaEventoNotificaByKey(aEventoNot.getEvento().getIdEvento());

			lEveMod.getEvento().setDescrUfficioEmittente(aEventoNot.getEvento().getDescrUfficioEmittente());

			// ========================================================================
			// Costruisco il TreeModel per la stampa
			// ========================================================================
			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			// TreeModel lTree = lStampa.prelevaDatiDifferimento(lEveMod, aUtente);

			TreeModel lTree = lStampa.prelevaDatiEventoSiep(lEveMod, aUtente);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("lTreeTot: "+ReportGenerator.debugTreeXML(lTree));

			String lNomeTemplate = TemplateManager.getInstance()
					.getTemplateName(aEventoNot.getNomeTemplate());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lNomeTemplate: " + lNomeTemplate);

			// ========================================================================
			// Genero il report (rtf) e aggiorno l'evento inserendo il report
			// ========================================================================
			ReportGenerator lReport = new ReportGenerator();
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// ======================================================
			// Aggiorno l'evento inserendo il report
			// ======================================================
			aEventoNot.getEvento().setDocBlobIn(lByteArrayInput);

			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEventoNot.getEvento());

			lEveDao.selCondizioneUpdate(aEventoNot.getEvento().getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(daoEx.getMessage(), daoEx);
			throw new F3BException("SospensioneController.ExStampaDocumentoSospensioni: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * ************************************************************************** Effettua l'aggiornamento del
	 * blob e la validazione degli eventi legati al differimento<br>
	 *
	 * - validazione evento della sorveglianza<br>
	 * - aggiornamento blob e validazione evento dell'Esecuzione<br>
	 * - validazione pena residua<br>
	 * - modifica posizione giuridica<br>
	 * - aggiornamento stato del procedimento<br>
	 *
	 * @since v 2.0
	 * @param aTipoProvvedimento
	 * @param aEvento
	 *            - Evento da Aggiornare/Validare
	 * @param aMisAlt
	 *            - Record Misura Alternativa contenete i dati del'ordinanza
	 * @param aFascicoloKey
	 *            - Id del fascicolo
	 * @param aPosGiu
	 *            - Nuova posizione giuridica
	 * @param aNomProv
	 *            - Nome del provvedimento
	 * @param aStatoProc
	 *            - Stato del procedimento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaDifferimentoNew(String aTipoProvvedimento, EventoModel aEvento,
			MisuraAlternativaModel aMisAlt, BigDecimal aFascicoloKey, String aPosGiu, String aNomProv,
			String aStatoProc) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			EventoSqlDAO lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDAO.getModelByKey();
			lEveSqlDAO.stop();

			// ========================================================================
			// Valida la Pena Residua associata al decreto (FLAG_VALIDATO=S)
			// ========================================================================
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lEveModel.getIdEvento());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Residua da validare" + lPenResMod);

			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			lPenResDao.setFlagValidato("S");
			lPenResDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" update Pena Residua avvenuto");

			// ========================================================================
			// Recupero e valido l'evento della sorveglianza associato
			// (FLAG_DOCUMENTO_REGISTRATO=S)
			// ========================================================================
			lEveDao = new EventoDAO(lConn);
			lEveDao.setIdEvento(lEveModel.getEveIdEvento());
			lEveDao.setFlagDocumentoRegistrato("S");

			lEveDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lEveDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lEveDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// ------------------------------------------------------------------------------
			// ========================================================================
			// Aggiorno la data fine della vecchia Posizione Giuridica con la
			// data del differimento e inserisco la nuova posizione solo se concessione
			// differimento Prov e Def perchè solo in questi casi viene modificata la
			// posizione giuridica
			// ========================================================================
			if (aTipoProvvedimento.equals(ICostantiSospensione.DIFFERIMENTO_PROV)
					|| aTipoProvvedimento.equals(ICostantiSospensione.DIFFERIMENTO_DEF)) {
				lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
				lPosSqlDao.ricercaPosizioneGiuridicaByIdFascicoloDesc(aFascicoloKey);
				PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

				lPosDao = new PosizioneGiuridicaDAO(lConn);
				lPosDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());

				lPosDao.setDataFine(aMisAlt.getDataInizioMisura());

				lPosDao.selByKey();
				lPosDao.update();
				lPosDao.stop();

				// ========================================================================
				// Inserisco Nuova Posizione Giuridica (data_inizio = data differimento)
				// agganciandola all'evento che sto validando
				// ========================================================================
				PosizioneGiuridicaModel lPosizione = new PosizioneGiuridicaModel();

				lPosizione.setCodPosizioneGiuridica(aPosGiu);
				lPosizione.setDataInizio(aMisAlt.getDataInizioMisura());

				lPosizione.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lPosizione.setDataInserimento(aEvento.getDataAggiornamento());
				lPosizione.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

				lPosizione.setCodPosizioneProcessuale("-");
				lPosizione.setFasSieIdFascicoloSiep(aFascicoloKey);
				lPosizione.setIdEventoRiferimento(aEvento.getIdEvento());

				lPosDao.setDAOFromModel(lPosizione);
				lPosDao.insert();
				lPosDao.stop();
			}
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data emissione provvedimento (decreto)
			// ========================================================================
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicoloKey);

			lStatoProcMod.setData(lEveModel.getDataEmissione());
			lStatoProcMod.setCodStatoProcedimento(aStatoProc);

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicoloKey);
			lStatoDao.delete();

			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// ========================================================================
			// Aggiorna tabella nome_provvedimento
			// ========================================================================
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento(aNomProv);
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// AMBROSINO 09-02-2011 - NOTIFICHE - le prendo per la data inizioscadenzario

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			/* Vector lNotifiche = new Vector( */lNotEveDao.getModels()/* ) */;
			// NotificaModel lNotifica = (NotificaModel) lNotifiche.get(0);

			// ========================================================================
			// Cancella tutti gli scadenzari di tipo LEGGE SIMEONE ( Tipo = 01 )
			// indipendentemente dal tipo di differimento, come indicato nel Vision
			// SIEP-Revisione Scadenzario Simeone
			// ========================================================================
			lScaDao = new ScadenzarioDAO(lConn);
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFascicoloKey, "01");
			lScaDao.delete();
			lScaDao.stop();

			// ========================================================================
			// Aggiorno lo Scadenzario se differimento definitivo o provvisorio
			// e presente la data fine differimento
			// ========================================================================
			if ((aTipoProvvedimento.equals(ICostantiSospensione.DIFFERIMENTO_PROV)
					|| aTipoProvvedimento.equals(ICostantiSospensione.DIFFERIMENTO_DEF))
					&& aMisAlt.getDataFineMisura() != null) {
				lScaSqlDao = new ScadenzarioSqlDAO(lConn);

				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("06", aFascicoloKey);
				ScadenzarioModel lScaMod = new ScadenzarioModel();
				lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
				lScaSqlDao.stop();

				if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
					// Aggiorno Scadenzario se già presente (es: inserimento diff definitivo
					// di un soggetto in differimento provvisorio)
					lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());

					lScaDao.setDataInizioScadenza(aMisAlt.getDataInizioMisura());
					lScaDao.setDataFineScadenza(aMisAlt.getDataFineMisura());

					lScaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDao.setDataAggiornamento(aEvento.getDataAggiornamento());

					lScaDao.update();
					lScaDao.stop();
				} else {
					// Inserisco Scadenzario
					lScaDao.setCodTipoScadenzario("06");

					lScaDao.setDataInizioScadenza(aMisAlt.getDataInizioMisura());
					lScaDao.setDataFineScadenza(aMisAlt.getDataFineMisura());

					lScaDao.setFasSieIdFascicoloSiep(aFascicoloKey);

					lScaDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lScaDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lScaDao.setDataInserimento(aEvento.getDataAggiornamento());

					lScaDao.insert();
					lScaDao.stop();
				}

				// ======================================================================
				// Devo cancellare Scadenzario fine Pena(02) e Scadenzario Simeone (01)
				// se presenti
				// ======================================================================
				/*
				 * lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("01", aFascicoloKey);
				 * ScadenzarioModel lScaModSimeone = new ScadenzarioModel(); lScaModSimeone =
				 * (ScadenzarioModel)lScaSqlDao.getModelByKey(); lScaSqlDao.stop(); if (lScaModSimeone != null
				 * && lScaModSimeone.getIdScadenzario() != null) {
				 * lScaDao.setCondizioneDelete(lScaModSimeone.getIdScadenzario()); lScaDao.delete();
				 * lScaDao.stop(); }
				 */
				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("02", aFascicoloKey);
				ScadenzarioModel lScaModFinePena = new ScadenzarioModel();
				lScaModFinePena = (ScadenzarioModel) lScaSqlDao.getModelByKey();
				lScaSqlDao.stop();
				if (lScaModFinePena != null && lScaModFinePena.getIdScadenzario() != null) {
					lScaDao.setCondizioneDelete(lScaModFinePena.getIdScadenzario());
					lScaDao.delete();
					lScaDao.stop();
				}
			} else if (aTipoProvvedimento.equals(ICostantiSospensione.DIFFERIMENTO_RIGETTO)
					|| aTipoProvvedimento.equals(ICostantiSospensione.DIFFERIMENTO_REVOCA)) {
				// Nel caso di rigetto/revoca devo cancellare la voce dello scadenzario inserita
				// in fase di concessione (se presente)
				lScaSqlDao = new ScadenzarioSqlDAO(lConn);

				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("06", aFascicoloKey);
				ScadenzarioModel lScaMod = new ScadenzarioModel();
				lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
				lScaSqlDao.stop();

				if (lScaMod != null) {
					lScaDao = new ScadenzarioDAO(lConn);
					lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
					lScaDao.delete();
					lScaDao.stop();
				}
			}

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			// -----------------------
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(daoEx.getMessage(), daoEx);
			throw new F3BException("SospensioneController.ExUpdateValidaDifferimentoNew : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(ex.getMessage(), ex);
			throw new F3BException("SospensioneController.ExUpdateValidaDifferimentoNew : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lStatoDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lScaSqlDao);
			cleanup(lScaDao);
			cleanup(lNotEveDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveMod;
	}

	/**
	 * ExUpdateRinunciaOpEspulsione
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateRinunciaOpEspulsione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		EventoDAO lEveDaoBlob = null;
		EventoDAO lEveDao = null;
		MisuraAlternativaDAO lMisDAO = null;
		MisuraAlternativaSqlDAO lMisDao = null;

		try {
			lConn = getDBTransaction();

			// =======================================================================================
			// aggiorno la misura alternativa con flag_situazione ad N
			// PER NON VISULIZZARLA NEL DETTAGLIO PROCEDIMENTO
			// ========================================================================================
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDAO = new MisuraAlternativaDAO(lConn);
			lMisDao.ricercaMisuraAlternativaByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

			if (lMisMod != null && lMisMod.getIdMisuraAlternativa() != null) {
				lMisDAO.setDAOFromModelForUpdate(lMisMod);
				lMisDAO.setFlagSituazione("N");
				lMisDAO.update();
				lMisDAO.stop();
			}

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// Valido l'evento riferito al verbale
			if (lEveModel != null && lEveModel.getEveIdEvento() != null) {
				lEveSqlDao.ricercaEventoByKey(lEveModel.getEveIdEvento());
				EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDao = new EventoDAO(lConn);

				if (lEveMod != null && (lEveMod.getFlagDocumentoRegistrato() == null
						|| lEveMod.getFlagDocumentoRegistrato().equals("N"))) {
					lEveDao.setDAOFromModelForUpdate(lEveMod);
					lEveDao.setFlagDocumentoRegistrato("S");
					lEveDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveDao.setDataAggiornamento(DateUtils.getSysDate());

					lEveDao.update();
					lEveDao.stop();
				}
			}

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = "0172";
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					lStatoProcMod);

			// Aggiorna Inserisci PENA_RESIDUA
			InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep());

			// Aggiorna POSIZIONE_GIURIDICA
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lPosizione = lPosMod.getCodPosizioneGiuridica();

			Date lData = lEveModel.getDataEmissione();
			lPosSqlDao.inserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizione, lPosMod, lData, aEvento,
					aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento(), "S");

			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP215");

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateRinunciaOpEspulsione : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateRinunciaOpEspulsione : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lEveDao);
			cleanup(lMisDao);
			cleanup(lMisDAO);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return aEvento;
	}

	/**
	 * ExUpdateEspulsione
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateEspulsione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		StatoProcedimentoDAO lStatoProDao = null;
		MisuraAlternativaSqlDAO lMisSql = null;
		EventoDAO lEveDaoBlob = null;
		EventoDAO lEveDaoMisAlt = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();
			String lCodiceMotivo = lEveModel.getCodMotivo();
			// misura alternativa
			lMisSql = new MisuraAlternativaSqlDAO(lConn);
			lMisSql.ricercaMisuraAlternativaByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisSql.getModelByKey();

			// Valido l'evento riferito alla misura alternativa modifica del 11-10-04 --dario
			if (lMisMod != null && lMisMod.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisMod.getEveIdEvento());
				EventoModel lEveModelMis = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelMis != null && (lEveModelMis.getFlagDocumentoRegistrato() == null
						|| lEveModelMis.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelMis.setFlagDocumentoRegistrato("S");
					lEveModelMis.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelMis.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelMis.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelMis);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// Aggiorna Inserisci PENA_RESIDUA
			PenaResiduaModel lPenMod = InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep());

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = null;
			if (lCodiceMotivo.equals("2142")) // concessione con Invio Comunicazioni
				lStatoProcMod = "0231";
			else if (lCodiceMotivo.equals("0394")) // accoglimento opposizione
				lStatoProcMod = "0171";
			else if (lCodiceMotivo.equals("0395")) // rigetto opposizione
				lStatoProcMod = "0172";
			else if (lCodiceMotivo.equals("2140")) // AMBROS 05/2013 concessione SENZA Invio Comunicazioni
				lStatoProcMod = "0138"; // NEW

			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					lStatoProcMod);

			// inserimento secondo stato procedimento solo se accogliemento opposizione
			lStatoProDao = new StatoProcedimentoDAO(lConn);
			if (lCodiceMotivo.equals("0394")) // accoglimento opposizione
			{
				lStatoProDao.setProgressivo(new BigDecimal(2));
				lStatoProDao.setData(lPenMod.getDataFine());
				lStatoProDao.setCodStatoProcedimento("0010");
				lStatoProDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lStatoProDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lStatoProDao.setDataInserimento(lEveModel.getDataAggiornamento());
				lStatoProDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lStatoProDao.insert();
			}

			// Aggiorna POSIZIONE_GIURIDICA
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lPosizione = lPosMod.getCodPosizioneGiuridica();

			Date lData = lEveModel.getDataEmissione();
			lPosSqlDao.inserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizione, lPosMod, lData,
					lEveModel, aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento(), "S");

			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			if (lCodiceMotivo.equals("2142")) // concessione
				lNomProvvDAO.setCodNomeProvvedimento("NP214");
			else if (lCodiceMotivo.equals("0394")) // accoglimento opposizione
				lNomProvvDAO.setCodNomeProvvedimento("NP216");
			else if (lCodiceMotivo.equals("0395")) // rigetto opposizione
				lNomProvvDAO.setCodNomeProvvedimento("NP217");
			else if (lCodiceMotivo.equals("2140")) // AMBROS 05/2013 Emessa Decreto
				lNomProvvDAO.setCodNomeProvvedimento("NP237"); // NEW cod

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateEspulsione : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateEspulsione : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lMisSql);
			cleanup(lEveDaoMisAlt);
			cleanup(lStatoProDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return aEvento;
	}

	/**
	 * ExInserisciEventoNotVerbaleSospPena Metodo d'inserimento per l'avvenuta espulsione
	 *
	 * @param aEveVer
	 * @param aVerMod
	 * @param aSosp
	 * @param aPenMod
	 * @param aEveNot
	 * @return idevento
	 * @throws F3BException
	 */
	public EventoModel ExInserisciEventoNotVerbaleSospPena(EventoModel aEveVer, VerbaleModel aVerMod,
			SospensioneModel aSosp, PenaResiduaModel aPenMod, EventoNotificaModel aEveNot)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		VerbaleDAO lVerDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaDAO lPenResDao = null;
		SospensioneDAO lSospDao = null;

		EventoModel lEveRet = new EventoModel(aEveVer);
		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);
			lVerDao = new VerbaleDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lPenResDao = new PenaResiduaDAO(lConn);
			lSospDao = new SospensioneDAO(lConn);

			// ===============================================
			// Inserisco Evento Verbale
			// ===============================================
			BigDecimal lKeyEventoVer = null;
			BigDecimal lProgrVer = lSqlDAO.getProgressivo(aEveVer);
			aEveVer.setProgrProtocollo(new BigDecimal(lProgrVer.intValue() + 1));

			lEveDao.setDAOFromModel(aEveVer);
			lKeyEventoVer = lEveDao.insert();
			lEveDao.stop();

			// ===============================================
			// Inserisco il Verbale
			// ===============================================
			aVerMod.setEveIdEvento(lKeyEventoVer);
			lVerDao.setDAOFromModel(aVerMod);
			/* BigDecimal lKey = */lVerDao.insert();
			lVerDao.stop();

			// ===============================================
			// Evento Provvedimento
			// ===============================================
			BigDecimal lKeyEvento = null;
			BigDecimal lProgr = lSqlDAO.getProgressivo(aEveNot.getEvento());
			aEveNot.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			lEveDao.setDAOFromModel(aEveNot.getEvento());
			lEveDao.setEveIdEvento(lKeyEventoVer);
			lKeyEvento = lEveDao.insert();
			lEveRet.setIdEvento(lKeyEvento);

			// ========================================================================
			// Ciclo di caricamento delle notifiche
			// ========================================================================
			BigDecimal lKeyAutorita = null;
			int count = 0;
			while (count < aEveNot.getNotifiche().length) {
				if (aEveNot.getNotifiche()[count] != null) {
					// Se è stata specificata anche l'autorità esterna per l'avvocato,
					// recupero l'id da inserire nella notifica
					// n.b. se autorità non presente la creo
					if (aEveNot.getNotifiche()[count].getAutoritaEsterna() != null) {
						// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
						// già l'autorità esterna specificata nella form (dalla form ho solo
						// codice e sede)
						lAutDao.setRicercaByAutSede(aEveNot.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
							lAutDao.setDAOFromModel(aEveNot.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					// Collego la notifica all'evento
					aEveNot.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					// Inserisco la notifica
					lNotDao.setDAOFromModel(aEveNot.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// ========================================================================
			// Registro la Nuova Pena residua
			// ========================================================================
			aPenMod.setEveIdEvento(lKeyEvento);
			lPenResDao.setDAOFromModel(aPenMod);
			BigDecimal lKeyPenaRes = lPenResDao.insert();

			// ========================================================================
			// Registro la Nuova Sospensione
			// ========================================================================
			aSosp.setPenResIdPenaResidua(lKeyPenaRes);
			lSospDao.setDAOFromModel(aSosp);
			/* BigDecimal lKeySosp = */lSospDao.insert();

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("SospensioneController.ExInserisciEventoNotVerbaleSospPena: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("SospensioneController.ExInserisciEventoNotVerbaleSospPena: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lSqlDAO);
			cleanup(lVerDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lPenResDao);
			cleanup(lSospDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * ExUpdateValidaEspulsione
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @param aPosGiu
	 * @param aNomProv
	 * @param aStatoProc
	 * @return EventoModel
	 * @throws F3BException
	 */

	public EventoModel ExUpdateValidaEspulsione(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			String aPosGiu, String aNomProv, String aStatoProc) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		SospensioneSqlDAO lSospSqlDao = null;
		ScadenzarioDAO lScaDao = null;
		MisuraAlternativaSqlDAO lMisSqlDao = null;
		MisuraAlternativaDAO lMisDAO = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// =======================================================
			// aggiorno la misura alternativa con flag_situazione ad N
			// =======================================================
			lMisSqlDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDAO = new MisuraAlternativaDAO(lConn);
			lMisSqlDao.ricercaMisuraAlternativaByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisSqlDao.getModelByKey();

			if (lMisModel != null && ("2140".equals(lMisModel.getCodTipoMisura())
					|| "0029".equals(lMisModel.getCodTipoMisura()))) {
				lMisDAO.setDAOFromModelForUpdate(lMisModel);
				lMisDAO.setFlagSituazione("N");
				lMisDAO.update();
				lMisDAO.stop();
			}

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			EventoSqlDAO lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDAO.getModelByKey();

			// update evento legato al verbale
			lEveSqlDAO.ricercaEventoByKey(lEveModel.getEveIdEvento());
			EventoModel lEveModVer = (EventoModel) lEveSqlDAO.getModelByKey();

			lEveDao.setIdEvento(lEveModVer.getIdEvento());
			lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
			lEveDao.setFlagDocumentoRegistrato("S");
			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// ricerca pena residua da validare
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lEveMod.getIdEvento());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			lPenResDao.setDAOFromModelForUpdate(lPenResMod);
			lPenResDao.setFlagPenaSospesa("S");
			lPenResDao.setFlagValidato("S");
			lPenResDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
			lPenResDao.update();
			lPenResDao.stop();

			// ricerca sospensione
			lSospSqlDao = new SospensioneSqlDAO(lConn);
			lSospSqlDao.ricercaSospensioneByIdPenaResidua(lPenResMod.getIdPenaResidua());
			SospensioneModel lSospMod = (SospensioneModel) lSospSqlDao.getModelByKey();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosizioneGiuridicaByIdFascicoloDesc(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
			lPosDao.setDataFine(lSospMod.getDataInizio());
			lPosDao.selByKey();
			lPosDao.update();
			lPosDao.stop();

			// inserimento nuova occorrenza
			PosizioneGiuridicaModel lPosizione = new PosizioneGiuridicaModel();
			lPosizione.setCodPosizioneGiuridica(aPosGiu);
			lPosizione.setDataInizio(lSospMod.getDataInizio());
			lPosizione.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());

			lPosizione.setDataInserimento(aEvento.getDataAggiornamento());
			lPosizione.setCodPosizioneProcessuale("-");
			lPosizione.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			lPosizione.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lPosizione.setIdEventoRiferimento(aEvento.getIdEvento()); // **

			lPosDao.setDAOFromModel(lPosizione);
			lPosDao.insert();
			lPosDao.stop();

			// SETTA LO STATO PROCEDIMENTO
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lSospMod.getDataInizio());

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(lEveMod.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
			lStatoProcMod.setCodStatoProcedimento(aStatoProc);

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();

			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento(aNomProv);
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// Scadenzario Espulsione
			lScaDao = new ScadenzarioDAO(lConn);

			ScadenzarioModel lScaModel = new ScadenzarioModel();
			lScaModel.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lScaModel.setCodTipoScadenzario("15");
			lScaModel.setDataInizioScadenza(lSospMod.getDataInizio());

			// ricerca misura alternativa per trovare la durata dell'espulsione altrimenti si considerano
			// 10 anni come valore di default
			String[] lNatura = { "CO" };
			String[] lTipoMisura = { "2140" };
			String[] lTipoDecisione = { "02" };
			lMisSqlDao.ricercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisioneOrderDescData(
					aFascicolo.getIdFascicoloSiep(), lTipoDecisione, lNatura, lTipoMisura);
			MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisSqlDao.getModelByKey();

			int lAnni = 0;
			int lMesi = 0;
			int lGiorni = 0;

			if (lMisMod != null && lMisMod.getIdMisuraAlternativa() != null) {
				if (lMisMod.getNumAnniMisura() != null
						&& lMisMod.getNumAnniMisura().compareTo(new BigDecimal(0)) != 0)
					lAnni = lMisMod.getNumAnniMisura().intValue();

				if (lMisMod.getNumMesiMisura() != null
						&& lMisMod.getNumMesiMisura().compareTo(new BigDecimal(0)) != 0)
					lMesi = lMisMod.getNumMesiMisura().intValue();

				if (lMisMod.getNumGiorniMisura() != null
						&& lMisMod.getNumGiorniMisura().compareTo(new BigDecimal(0)) != 0)
					lGiorni = lMisMod.getNumGiorniMisura().intValue();
			}

			if (lAnni == 0 && lMesi == 0 && lGiorni == 0) {
				lAnni = 10;
				lMesi = 0;
				lGiorni = 0;
			}

			Date lSommaAnni = null;
			Date lSommaMesi = null;
			Date lFineScadenza = null;

			lSommaAnni = DateUtils.moveDateTo(lSospMod.getDataInizio(), java.util.Calendar.YEAR, lAnni);
			lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH, lMesi);
			lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH, lGiorni);

			lScaModel.setDataFineScadenza(lFineScadenza);
			lScaModel.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
			lScaModel.setDataInserimento(lEveMod.getDataAggiornamento());
			lScaModel.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
			lScaModel.setEveIdEvento(aEvento.getIdEvento());

			lScaDao.setDAOFromModel(lScaModel);
			lScaDao.insert();
			lScaDao.stop();

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaEspulsione: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("SospensioneController.ExUpdateValidaEspulsione: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lSospSqlDao);
			cleanup(lScaDao);
			cleanup(lMisSqlDao);
			cleanup(lMisDAO);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveMod;
	}

	/**
	 * MEV_9-SIEP
	 */
	public EventoModel ExUpdateValidaSospensioneDecisioniSorveglianza678(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		EventoDAO lEveDaoMisAlt = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();
			// String motivo = lEveModel.getCodMotivo();

			lEveModel.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lEveModel.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lEveModel.setDataAggiornamento(aEvento.getDataAggiornamento());

			// cerca la misura
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisMDS = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento riferito alla misura alternativa modifica
			if (lMisMDS != null && lMisMDS.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisMDS.getEveIdEvento());
				EventoModel lEveModelMis = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelMis != null && (lEveModelMis.getFlagDocumentoRegistrato() == null
						|| lEveModelMis.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelMis.setFlagDocumentoRegistrato("S");
					lEveModelMis.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelMis.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelMis.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelMis);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = "0575";

			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					lStatoProcMod);

			// Aggiorna Inserisci PENA_RESIDUA
			InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep());

			// Aggiorna POSIZIONE_GIURIDICA
			String lPosizione = "47"; // Libero in Sospensione 309/90

			Date lData = lEveModel.getDataEmissione();
			if (lMisMDS != null && lMisMDS.getDataScarcerazione() != null)
				lData = lMisMDS.getDataScarcerazione();// data sospensione esecuzione

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizione, lPosMod, lData, lEveModel,
					aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());

			// ------- EVENTO--------
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"SospensioneController.ExUpdateValidaSospensioneDecisioniSorveglianza678 : " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(
					"SospensioneController.ExUpdateValidaSospensioneDecisioniSorveglianza678 : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);

			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveMod;
	}

}