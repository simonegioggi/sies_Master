package siap.sico.misuraalternativa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaEventoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.CalendarUtil;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.refertoscarcerazione.dao.RefertoScarcerazioneSqlDAO;
import siap.siep.refertoscarcerazione.model.RefertoScarcerazioneModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sospensione.dao.SospensioneDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.dao.VerbaleSqlDAO;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: MisuraAlternativaController
 * </p>
 * <p>
 * Description: Classe Controller per MisuraAlternativa
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
public class MisuraAlternativaController extends SiapController implements IMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// inserimento misura alternativa del TDS
	public MisuraAlternativaModel ExInserisciMisuraAlternativaEventoNotifica(EventoNotificaModel aEveNotMod,
			DepositoOrdinanzaPcModel lDepOrdMod, TenoreModel lTenMod,
			MisuraAlternativaModel aMisuraAlternativa) throws F3BException {

		Connection lConn = null;

		MisuraAlternativaDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = null;
		EventoDAO lEveDAO = null;
		NotificaDAO lNotDAO = null;
		DepositoOrdinanzaPcDAO lDepOrdDAO = null;
		AutoritaEsternaDAO lAutDao = null;
		TenoreDAO lTenDAO = null;

		try {
			lConn = getDBTransaction();
			// inserimento evento ORDINANZA/DECRETO
			EventoModel lEveMod = new EventoModel(aEveNotMod.getEvento());
			lEveDAO = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDAO = new NotificaDAO(lConn);
			lEveDAO.setDAOFromModel(lEveMod);
			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDAO.insert();

			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (aEveNotMod.getNotifiche() != null) {
				while (count < aEveNotMod.getNotifiche().length) {
					if (aEveNotMod.getNotifiche()[count] != null) {
						if (aEveNotMod.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao.setRicercaByAutSede(
									aEveNotMod.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								lAutDao.setDAOFromModel(
										aEveNotMod.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								aEveNotMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEveNotMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						aEveNotMod.getNotifiche()[count].setEveIdEvento(lKeyEvento);

						lNotDAO.setDAOFromModel(aEveNotMod.getNotifiche()[count]);
						lNotDAO.insert();
						lNotDAO.stop();

					}
					count++;
				}
			}

			lMisMod = new MisuraAlternativaModel(aMisuraAlternativa);

			// insert misura alternativa
			lMisMod.setEveIdEvento(lKeyEvento);

			lMisDao = new MisuraAlternativaDAO(lConn);
			lMisDao.setDAOFromModel(lMisMod);
			// BigDecimal lKeyMA = null;
			/* lKeyMA = */lMisDao.insert();

			BigDecimal lKeyDepOrd = null;

			// insert Deposito Ordinanza
			lDepOrdDAO = new DepositoOrdinanzaPcDAO(lConn);
			DepositoOrdinanzaPcModel lDepPCMod = new DepositoOrdinanzaPcModel(lDepOrdMod);
			lDepPCMod.setIdEventoGenerato(lKeyEvento);
			lDepOrdDAO.setDAOFromModel(lDepPCMod);
			lKeyDepOrd = lDepOrdDAO.insert();

			// insert tenore
			lTenDAO = new TenoreDAO(lConn);

			lTenDAO.setDepOpidDepositoOrdinanzaPc(lKeyDepOrd);

			lTenDAO.setDAOFromModel(lTenMod);
			// BigDecimal lKeyTenore = null;
			/* lKeyTenore = */lTenDAO.insert();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"MisuraAlternativaController.ExInserisciMisuraAlternativaEventoNotifica: " + ex);
			// } catch (SQLException sqe) {
			// rollback(lConn);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: ", sqe);
			// throw new
			// F3BException("MisuraAlternativaController.ExInserisciMisuraAlternativaEventoNotifica: "
			// + sqe);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);

			throw new F3BException(
					"MisuraAlternativaController.ExInserisciMisuraAlternativaEventoNotifica: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lEveDAO);
			cleanup(lNotDAO);
			cleanup(lDepOrdDAO);
			cleanup(lAutDao);
			cleanup(lTenDAO);

			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExInserisciDecretoSospEventoNotifica - inserimento decreto sospensione
	 *
	 * @param aEveNotMod
	 * @param lDepDecMod
	 * @param lTenMod
	 * @param aMisuraAlternativa
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExInserisciDecretoSospEventoNotifica(EventoNotificaModel aEveNotMod,
			DepositoDecretoModel lDepDecMod, TenoreModel lTenMod, MisuraAlternativaModel aMisuraAlternativa)
			throws F3BException {

		Connection lConn = null;

		MisuraAlternativaDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = null;
		EventoDAO lEveDAO = null;
		NotificaDAO lNotDAO = null;
		AutoritaEsternaDAO lAutDao = null;
		DepositoDecretoDAO lDepDAO = null;
		TenoreDAO lTenDAO = null;

		try {
			lConn = getDBTransaction();

			lEveDAO = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDAO = new NotificaDAO(lConn);

			// inserimento evento
			EventoModel lEveMod = new EventoModel(aEveNotMod.getEvento());
			lEveDAO.setDAOFromModel(lEveMod);
			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDAO.insert();

			BigDecimal lKeyAutorita = null;
			int count = 0;

			// ========================================================================
			// Inserimento Notifiche
			// ========================================================================
			if (aEveNotMod != null && aEveNotMod.getNotifiche() != null) {
				while (count < aEveNotMod.getNotifiche().length) {
					if (aEveNotMod.getNotifiche()[count] != null) {

						if (aEveNotMod.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao.setRicercaByAutSede(
									aEveNotMod.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								lAutDao.setDAOFromModel(
										aEveNotMod.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								aEveNotMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEveNotMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						aEveNotMod.getNotifiche()[count].setEveIdEvento(lKeyEvento);

						lNotDAO.setDAOFromModel(aEveNotMod.getNotifiche()[count]);
						lNotDAO.insert();
						lNotDAO.stop();

					}
					count++;
				}
			}
			BigDecimal lKeyDepDec = null;

			// ========================================================================
			//
			// ========================================================================
			lDepDAO = new DepositoDecretoDAO(lConn);
			DepositoDecretoModel lDepDecretoMod = new DepositoDecretoModel(lDepDecMod);
			lDepDecretoMod.setIdEventoGenerato(lKeyEvento);
			lDepDAO.setDAOFromModel(lDepDecretoMod);
			lKeyDepDec = lDepDAO.insert();

			// insert tenore
			lTenDAO = new TenoreDAO(lConn);
			lTenMod.setDepDecIdDepositoDecreto(lKeyDepDec);
			lTenDAO.setDAOFromModel(lTenMod);

			// BigDecimal lKeyTenore = null;
			/* lKeyTenore = */lTenDAO.insert();

			lMisMod = new MisuraAlternativaModel(aMisuraAlternativa);

			// insert misura alternativa
			lMisMod.setEveIdEvento(lKeyEvento);

			lMisDao = new MisuraAlternativaDAO(lConn);
			lMisDao.setDAOFromModel(lMisMod);
			// BigDecimal lKeyMA = null;
			/* lKeyMA = */lMisDao.insert();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("MisuraAlternativaController.ExInserisciDecretoSospEventoNotifica: " + ex);
			// } catch (SQLException sqe) {
			// rollback(lConn);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException("MisuraAlternativaController.ExInserisciDecretoSospEventoNotifica: " +
			// sqe);
		} finally {
			cleanup(lMisDao);
			cleanup(lEveDAO);
			cleanup(lNotDAO);
			cleanup(lDepDAO);
			cleanup(lAutDao);
			cleanup(lTenDAO);

			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExRicercaMisuraAlternativaByKey
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDao.ricercaMisuraAlternativaByKey(aKey);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MisuraAlternativaController.ExRicercaMisuraAlternativaByKey: " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException("MisuraAlternativaController.ExRicercaMisuraAlternativaByKey: " + sqe);
		} finally {
			cleanup(lMisDao);

			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExRicercaMisuraAlternativaByIdEvento
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaByIdEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDao.ricercaMisuraAlternativaByIdEvento(aKey);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaController.ExRicercaMisuraAlternativaByIdEvento: " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException("MisuraAlternativaController.ExRicercaMisuraAlternativaByIdEvento: " +
			// sqe);
		} finally {
			cleanup(lMisDao);

			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExRicercaMisuraAlternativaCorrenteByIdFascicolo
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaCorrenteByIdFascicolo(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = null;
		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDao.ricercaMisuraAlternativaByIdFascicolo(aKey);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaController.ExRicercaMisuraAlternativaCorrenteByIdFascicolo: " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException(
			// "MisuraAlternativaController.ExRicercaMisuraAlternativaCorrenteByIdFascicolo: " + sqe);
		} finally {
			cleanup(lMisDao);

			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExRicercaMisureAlternativeByIdFascicolo
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMisureAlternativeByIdFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		Vector lMisureAlternative = new Vector();
		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDao.ricercaMisuraAlternativaByIdFascicolo(aKey);
			lMisureAlternative = new Vector(lMisDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaController.ExRicercaMisureAlternativeByIdFascicolo: " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException("MisuraAlternativaController.ExRicercaMisureAlternativeByIdFascicolo: "
			// + sqe);
		} finally {
			cleanup(lMisDao);

			cleanup(lConn);
		}
		return lMisureAlternative;
	}

	/**
	 * ExRicercaMisuraAlternativaPerOrdineScarcerazioneByIdFascicolo Controllo della coerenza della richiesta
	 * di emissione del provvedimento con L'Ordinanza del TS per l'Ordine di Scarcerazione
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaPerOrdineScarcerazioneByIdFascicolo(
			BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = null;
		EventoSqlDAO lEveSqlDAO = null;
		Vector lEveVec;
		EventoModel lEveMod;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);

			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoOrdinanzaOrdineScarcerazioneIdFascicolo(aKey);

			lEveVec = new Vector(lEveSqlDAO.getModels());
			if (lEveVec.size() > 0) {
				lEveMod = (EventoModel) lEveVec.get(0);
				BigDecimal lKeyEvento = lEveMod.getIdEvento();

				lMisDao.ricercaMisuraAlternativaOSLiberazioneAnticipataByIdEvento(lKeyEvento);
				lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaController.ExRicercaMisuraAlternativaPerOrdineScarcerazioneByIdFascicolo: Non posso leggere : "
							+ daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException(
			// "MisuraAlternativaController.ExRicercaMisuraAlternativaPerOrdineScarcerazioneByIdFascicolo: Non
			// posso leggere : "
			// + sqe);
		} finally {
			cleanup(lMisDao);
			cleanup(lEveSqlDAO);

			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExRicercaMisuraAlternativaPerOSLiberazioneAnticipataMAByIdFascicolo
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaPerOSLiberazioneAnticipataMAByIdFascicolo(
			BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = null;
		EventoSqlDAO lEveSqlDAO = null;
		Vector lEveVec;
		EventoModel lEveMod;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);

			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoOrdinanzaOSLiberazioneAnticipataMAIdFascicolo(aKey);

			// lEveMod=(EventoModel) lEveSqlDAO.getModels();
			lEveVec = new Vector(lEveSqlDAO.getModels());
			if (lEveVec.size() > 0) {
				lEveMod = (EventoModel) lEveVec.get(0);
				BigDecimal lKeyEvento = lEveMod.getIdEvento();

				lMisDao.ricercaMisuraAlternativaOSLiberazioneAnticipataMAByIdEvento(lKeyEvento);
				lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaController.ExRicercaMisuraAlternativaPerOSLiberazioneAnticipataMAByIdFascicolo: Non posso leggere : "
							+ daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException(
			// "MisuraAlternativaController.ExRicercaMisuraAlternativaPerOSLiberazioneAnticipataMAByIdFascicolo:
			// Non posso leggere : "
			// + sqe);
		} finally {
			cleanup(lMisDao);
			cleanup(lEveSqlDAO);

			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExRicercaMisuraAlternativaConcessaCorrenteByIdFascicolo Ricerca la Misura Alternativa Concessa Corrente
	 * by Id_Fascicolo
	 *
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaConcessaCorrenteByIdFascicolo(
			BigDecimal aKeyFascicolo) throws F3BException {

		Connection lConn = null;

		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDao.ricercaMisuraAlternativaConcessaCorrenteByIdFascicolo(aKeyFascicolo);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaController.ExRicercaMisuraAlternativaConcessaCorrenteByIdFascicolo: "
							+ daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException(
			// "MisuraAlternativaController.ExRicercaMisuraAlternativaConcessaCorrenteByIdFascicolo: "
			// + sqe);
		} finally {
			cleanup(lMisDao);

			cleanup(lConn);
		}

		return lMisMod;
	}

	/**
	 * Aggiorna la misura alternativa con data inizio, data fine e durata. Calcolo la durata della misura
	 * alternativa che e' = pena residua se il soggetto era inizialmente libero, o pari al residuo pena alla
	 * data di sottoscrizione se il soggetto era gia' in espiazione. Aggiorna il fine pena manuale e Valida la
	 * pena residua associata al Verbale Aggiorna lo scadenzario.
	 *
	 * @param lVerMod
	 *            - Model del verbale di sottoscrizione
	 * @param lPen
	 *            - PenaResiduaModel legato al verbale di sottoscrizione
	 * @param lPos
	 *            - Posizione Giuridica Precedente
	 * @param lMisMod
	 *            - Misura Alternativa Corrente
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExCalcolaFineEspiazionePenaMAConcessa(VerbaleModel lVerMod,
			PenaResiduaModel lPen, PosizioneGiuridicaModel lPos, MisuraAlternativaModel lMisMod,
			BigDecimal aKeyFascicolo) throws F3BException {

		Connection lConn = null;

		MisuraAlternativaDAO lMisuraDAO = null;
		PenaResiduaDAO lPenResiduaDAO = null;
		PenaResiduaModel lPenMod = null;
		PosizioneGiuridicaSqlDAO lPosSql = null;
		ScadenzarioSqlDAO lScaSql = null;
		ScadenzarioDAO lScaDao = null;

		BigDecimal AnniTot, MesiTot, GiorniTot;

		try {
			lConn = getDBTransaction();
			lPenResiduaDAO = new PenaResiduaDAO(lConn);
			lMisuraDAO = new MisuraAlternativaDAO(lConn);
			lPosSql = new PosizioneGiuridicaSqlDAO(lConn);
			lScaSql = new ScadenzarioSqlDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);

			// ========================================================================
			// modifica record pena residua (data fine pena e flag validato a S)
			// ========================================================================
			lPenResiduaDAO.setDAOFromModelForUpdate(lPen);
			lPenResiduaDAO.update();
			lPenMod = (PenaResiduaModel) lPenResiduaDAO.getModelByKey();
			lPenResiduaDAO.stop();

			Date dataInizioMisura = lMisMod.getDataInizioMisura();

			// ricerca posizione giuridica corrente
			PosizioneGiuridicaModel lPosCor = new PosizioneGiuridicaModel();
			lPosSql.ricercaPosGiuCorrenteByIdFascicolo(aKeyFascicolo);
			lPosCor = (PosizioneGiuridicaModel) lPosSql.getModelByKey();

			// 0011 - Differimento Pena Nelle Forme della Detenzione Domiciliare
			// 0197 - Proroga Differimento Pena nelle forme della Detenzione Domiciliare
			if (!lMisMod.getCodTipoMisura().equals("0011") && !lMisMod.getCodTipoMisura().equals("0197")) {
				if (lPosCor != null && lPos != null && lPosCor.getCodPosizioneGiuridica() != null
						&& lPos.getCodPosizioneGiuridica() != null
						&& (lPosCor.getCodPosizioneGiuridica().equals("13")
								|| lPosCor.getCodPosizioneGiuridica().equals("54")
								|| lPosCor.getCodPosizioneGiuridica().equals("12")
								|| lPosCor.getCodPosizioneGiuridica().equals("29")
								|| lPosCor.getCodPosizioneGiuridica().equals("14")
								|| lPosCor.getCodPosizioneGiuridica().equals("27")
								|| lPosCor.getCodPosizioneGiuridica().equals("50")) // 29/09/2010
						&& (lPos.isLibero())) {
					// Se libero la durata della misura alternativa e' pari all'intera pena residua
					if (lPenMod.getNumAnniReclusione() != null && lPenMod.getNumMesiReclusione() != null
							&& lPenMod.getNumGiorniReclusione() != null) {
						if (lPenMod.getNumAnniArresto() != null && lPenMod.getNumMesiArresto() != null
								&& lPenMod.getNumGiorniArresto() != null) {
							// Sommo reclusione e arresto
							AnniTot = lPenMod.getNumAnniReclusione().add(lPenMod.getNumAnniArresto());
							MesiTot = lPenMod.getNumMesiReclusione().add(lPenMod.getNumMesiArresto());
							GiorniTot = lPenMod.getNumGiorniReclusione().add(lPenMod.getNumGiorniArresto());

							// AGGIORNA MISURA ALTERNATIVA CON NUOVA DATA FINE e quantum
							lMisMod.setNumAnniMisura(AnniTot);
							lMisMod.setNumMesiMisura(MesiTot);
							lMisMod.setNumGiorniMisura(GiorniTot);
							lMisMod.setDataInizioMisura(dataInizioMisura);
							lMisMod.setDataFineMisura(lPenMod.getDataFine());

							lMisuraDAO.setDAOFromModelForUpdate(lMisMod);
							lMisuraDAO.update();
						}
					}
				} else {
					// Se diverso da libero, la durata della misura e' pari al residuo pena
					// al momento della sottoscrizione (data sottoscrizione-data fine)
					// CALCOLO NUM-ANNI NUM-MESI NUM-GIORNI
					CalendarModel lCalMod = new CalendarModel();

					lCalMod.setDataFine(lPenMod.getDataFine());
					lCalMod.setDataInizio(dataInizioMisura);
					CalendarUtil lCal = new CalendarUtil();

					lCalMod = lCal.CalcolaNumGiorniMesiAnni(lCalMod);
					// Paolo Cherubini 01/02/2011 su segnalazione di Nunzia
					// quando parto dal 01/02/2011 sbaglia il calcolo della pena che da 3 anni diventa 2 anni
					// 11 mesi 30 giorni
					// per ovviare a cio' aggiungo la seguente routine
					lCalMod = lCal.ricalcolaGAM(lCalMod);

					// AGGIORNA MISURA ALTERNATIVA CON NUOVA DATA FINE
					BigDecimal anni = new BigDecimal(lCalMod.getNumAnni());
					BigDecimal mesi = new BigDecimal(lCalMod.getNumMesi());
					BigDecimal giorni = new BigDecimal(lCalMod.getNumGiorni());

					lMisMod.setNumAnniMisura(anni);
					lMisMod.setNumMesiMisura(mesi);
					lMisMod.setNumGiorniMisura(giorni);
					lMisMod.setDataInizioMisura(dataInizioMisura);
					lMisMod.setDataFineMisura(lPenMod.getDataFine());
					lMisuraDAO.setDAOFromModelForUpdate(lMisMod);
					lMisuraDAO.update();

					// INSERISCI NUOVO ACCORRENZA PENA RESIDUA agganciandola all'Ordinanza
					lPenMod.setEveIdEvento(lMisMod.getEveIdEvento());
					lPenMod.setMisAltIdMisuraAlternativa(lMisMod.getIdMisuraAlternativa());
					lPenResiduaDAO.setDAOFromModel(lPenMod);

					// BigDecimal lKey = null;
					/* lKey = */lPenResiduaDAO.insert();
				}
			} else // caso detenzione domiciliare a termine
			{
				// AGGIORNA MISURA ALTERNATIVA
				lMisMod.setDataInizioMisura(dataInizioMisura);

				if (lMisMod.getDataFineMisura() == null && (lMisMod.getNumAnniMisura() != null
						|| lMisMod.getNumMesiMisura() != null || lMisMod.getNumGiorniMisura() != null)) {
					CalendarModel lCalMod = new CalendarModel();

					lCalMod.setNumAnni(lMisMod.getNumAnniMisura());
					lCalMod.setNumGiorni(lMisMod.getNumGiorniMisura());
					lCalMod.setNumMesi(lMisMod.getNumMesiMisura());

					Date lDataFineMisura = new Date();
					ICalcoloPena lCtrlCalcolo = SIEPLookupRemote.getCalcoloPenaRemote();
					lDataFineMisura = lCtrlCalcolo.exCalcolaNuovaDataFine(dataInizioMisura, lCalMod, true);

					lMisMod.setDataFineMisura(lDataFineMisura);
				}

				lMisuraDAO.setDAOFromModelForUpdate(lMisMod);
				lMisuraDAO.update();
			}

			// SCADENZARIO
			if (lPos != null && lPosCor != null && lPos.getCodPosizioneGiuridica() != null
					&& lPosCor.getCodPosizioneGiuridica() != null
					&& (lPosCor.getCodPosizioneGiuridica().equals("12")
							|| lPosCor.getCodPosizioneGiuridica().equals("13")
							|| lPosCor.getCodPosizioneGiuridica().equals("14")
							|| lPosCor.getCodPosizioneGiuridica().equals("27")
							|| lPosCor.getCodPosizioneGiuridica().equals("50")) // 29/09/2010
					&& (lPos.isLibero())) {
				// scadenzario Fine Pena
				ScadenzarioModel lScaMod = new ScadenzarioModel();
				lScaSql.ricercaScadenzarioByIdFascicoloCorrente(aKeyFascicolo);
				lScaMod = (ScadenzarioModel) lScaSql.getModelByKey();
				if (lScaMod != null) {
					if (lMisMod != null && lMisMod.getDataFineMisura() != null) {
						lScaDao.setDAOFromModelForUpdate(lScaMod);
						lScaDao.setDataFineScadenza(lMisMod.getDataFineMisura());
						lScaDao.setCodOperatoreAggiornamento(lPen.getCodOperatoreAggiornamento());
						lScaDao.setDataAggiornamento(DateUtils.getSysDate());
						lScaDao.setCodUfficioAggiornamento(lPen.getCodUfficioAggiornamento());
						lScaDao.update();
						lScaDao.stop();
					}
					/*
					 * else { throw new F3BException(SIAPException.USER_MESSAGE,
					 * "Non e' possibile effettuare l'aggiornamento dello scadenzario fine pena : Data Fine misura mancante."
					 * ); }
					 */
				} else if (lPen != null && lPen.getDataInizio() != null) {
					lScaDao.setCodOperatoreInserimento(lPen.getCodOperatoreAggiornamento());
					lScaDao.setDataInserimento(DateUtils.getSysDate());
					lScaDao.setCodUfficioInserimento(lPen.getCodUfficioAggiornamento());
					lScaDao.setCodTipoScadenzario("02"); // Fine Pena
					lScaDao.setFasSieIdFascicoloSiep(aKeyFascicolo);
					lScaDao.setDataInizioScadenza(lPen.getDataInizio());
					lScaDao.setDataFineScadenza(lPen.getDataFine());
					lScaDao.insert();
					lScaDao.stop();
				}

				// scadenzario misura alternativa
				ScadenzarioModel lScaModel = new ScadenzarioModel();

				String lTipoScadenzario = null;
				if (lMisMod != null && lMisMod.getCodTipoMisura().equals("0011"))
					lTipoScadenzario = "14"; // Detenzione Domiciliare a termine
				else
					lTipoScadenzario = "13"; // Misura Alternativa

				lScaSql.ricercaScadenzarioByTipoScadenzarioIdFascicolo(lTipoScadenzario, aKeyFascicolo);
				lScaModel = (ScadenzarioModel) lScaSql.getModelByKey();

				if (lScaModel != null && lScaModel.getIdScadenzario() != null
						&& lScaModel.getIdScadenzario().compareTo(new BigDecimal(0)) != 0) {
					if (lMisMod != null && lMisMod.getDataFineMisura() != null) {
						lScaDao.setDAOFromModelForUpdate(lScaModel);
						lScaDao.setDataFineScadenza(lMisMod.getDataFineMisura());
						lScaDao.setCodOperatoreAggiornamento(lPen.getCodOperatoreAggiornamento());
						lScaDao.setDataAggiornamento(DateUtils.getSysDate());
						lScaDao.setCodUfficioAggiornamento(lPen.getCodUfficioAggiornamento());
						lScaDao.update();
						lScaDao.stop();
					}
					/*
					 * else { throw new F3BException(SIAPException.USER_MESSAGE,
					 * "Non e' possibile effettuare l'aggiornamento dello scadenzario misura alternativa : Data Fine misura mancante."
					 * ); }
					 */
				} else {
					if (lMisMod != null && lMisMod.getDataFineMisura() != null
							&& lMisMod.getDataInizioMisura() != null) {
						lScaDao.setCodOperatoreInserimento(lPen.getCodOperatoreAggiornamento());
						lScaDao.setDataInserimento(DateUtils.getSysDate());
						lScaDao.setCodUfficioInserimento(lPen.getCodUfficioAggiornamento());
						lScaDao.setCodTipoScadenzario(lTipoScadenzario);
						lScaDao.setFasSieIdFascicoloSiep(aKeyFascicolo);
						lScaDao.setDataInizioScadenza(lMisMod.getDataInizioMisura());
						lScaDao.setDataFineScadenza(lMisMod.getDataFineMisura());
						lScaDao.insert();
						lScaDao.stop();
					}
					/*
					 * else { throw new F3BException(SIAPException.USER_MESSAGE,
					 * "Non e' possibile effettuare l'inserimento dello scadenzario misura alternativa : Data Fine/Inizio misura mancante."
					 * ); }
					 */
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);

			rollback(lConn);
			throw new F3BException(
					"MisuraAlternativaController.ExCalcolaFineEspiazionePenaMAConcessa: " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			//
			// rollback(lConn);
			// throw new F3BException("MisuraAlternativaController.ExCalcolaFineEspiazionePenaMAConcessa: "
			// + sqe);
		} catch (F3BException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();

			rollback(lConn);
			throw ex;
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();

			rollback(lConn);
			throw new F3BException(
					"MisuraAlternativaController.ExCalcolaFineEspiazionePenaMAConcessa: " + ex);
		} finally {
			cleanup(lMisuraDAO);
			cleanup(lPenResiduaDAO);
			cleanup(lPosSql);
			cleanup(lScaSql);
			cleanup(lScaDao);

			cleanup(lConn);
		}
		return lMisMod;
	}
	// FINE PENA concessa
	// CALCOLA DATA FINE ESPIAZIONE PENA revocata

	/**
	 * ExCalcolaFineEspiazionePenaMARevocata CALCOLA DATA FINE ESPIAZIONE PENA revocata
	 *
	 * @param lPos
	 * @param lMisMod
	 * @param aKeyFascicolo
	 * @param FlagRicalcola
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExCalcolaFineEspiazionePenaMARevocata(PosizioneGiuridicaModel lPos,
			MisuraAlternativaModel lMisMod, BigDecimal aKeyFascicolo, String FlagRicalcola)
			throws F3BException {

		Connection lConn = null;

		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaDAO lMisuraDAO = null;
		PenaResiduaSqlDAO lPenResDAO = null;
		PenaResiduaDAO lPenResiduaDAO = null;
		PenaResiduaModel lPenMod = null;

		// BigDecimal AnniTot, MesiTot, GiorniTot;

		try {
			lConn = getDBTransaction();

			Date dataInizioMisura = lMisMod.getDataInizioMisura();
			Date dataSospensioneMisura = lMisMod.getDataFineMisura();

			// CALCOLO NUM-ANNI NUM-MESI NUM-GIORNI
			CalendarModel lCalModMA = new CalendarModel();
			lCalModMA.setDataFine(dataSospensioneMisura);
			lCalModMA.setDataInizio(dataInizioMisura);
			CalendarUtil lCal = new CalendarUtil();
			lCalModMA = lCal.CalcolaNumGiorniMesiAnni(lCalModMA);

			// AGGIORNA MISURA ALTERNATIVA CON NUOVA DATA FINE

			lMisuraDAO = new MisuraAlternativaDAO(lConn);
			BigDecimal anni = new BigDecimal(lCalModMA.getNumAnni());
			BigDecimal mesi = new BigDecimal(lCalModMA.getNumMesi());
			BigDecimal giorni = new BigDecimal(lCalModMA.getNumGiorni());

			lMisMod.setNumAnniMisura(anni);
			lMisMod.setNumMesiMisura(mesi);
			lMisMod.setNumGiorniMisura(giorni);
			lMisMod.setDataFineMisura(dataSospensioneMisura);
			lMisuraDAO.setDAOFromModelForUpdate(lMisMod);
			lMisuraDAO.update();

			// RICERCA PENA RESIDUA

			lPenResDAO = new PenaResiduaSqlDAO(lConn);
			lPenResDAO.ricercaPenaResiduaCorrenteByFascicoloSiepData(aKeyFascicolo);
			lPenMod = (PenaResiduaModel) lPenResDAO.getModelByKey();

			CalendarModel lCalModRE = new CalendarModel();

			lCalModRE.setDataInizio(lPenMod.getDataFine());
			lCalModRE.setDataFine(dataSospensioneMisura);
			lCalModRE = lCal.CalcolaNumGiorniMesiAnni(lCalModRE);

			BigDecimal anniResidui = new BigDecimal(lCalModRE.getNumAnni());
			BigDecimal mesiResidui = new BigDecimal(lCalModRE.getNumMesi());
			BigDecimal giorniResidui = new BigDecimal(lCalModRE.getNumGiorni());
			// CalendarModel lCalModFin = new CalendarModel();
			if (FlagRicalcola.equals("S") && FlagRicalcola != null) {
				// lCalModFin = lCal.sommaGiorni(lCalModMA, lCalModRE.getNumGiorni(), lCalModRE.getNumMesi(),
				// lCalModRE.getNumAnni());
				ICalcoloPena lCtrlCalcolo = SIEPLookupRemote.getCalcoloPenaRemote();
				Vector lcalcoloFinePena = lCtrlCalcolo.exCalcolaDataFinePena(dataSospensioneMisura, lPenMod,
						true); // il flag true indica CON DIES_A_QUO
				// inserisce pena residua

				lPenMod.setEveIdEvento(lMisMod.getEveIdEvento());

				lPenMod.setNumAnniReclusione(anniResidui);
				lPenMod.setNumMesiReclusione(mesiResidui);
				lPenMod.setNumAnniReclusione(giorniResidui);
				lPenMod.setDataInizio(dataSospensioneMisura);
				if (lcalcoloFinePena.size() == 1) {
					lPenMod.setDataFine((Date) lcalcoloFinePena.get(0));
				} else if (lcalcoloFinePena.size() == 2) {
					lPenMod.setDataFine((Date) lcalcoloFinePena.get(1));
				}

				lPenMod.setEveIdEvento(lMisMod.getEveIdEvento());
				lPenMod.setMisAltIdMisuraAlternativa(lMisMod.getIdMisuraAlternativa());

				/*
				 * ISSUE MEV : aggiunta linea di codice per prevenire nullpointer Numero MEV : SIES v10 Autore
				 * : gioggi Data : 28/gen/2016 Branch : MEV_SIES v10
				 */
				lPenResiduaDAO = new PenaResiduaDAO(lConn);
				// ***** FINE INTERVENTO MEV_SIES v10 *****//

				lPenResiduaDAO.setDAOFromModel(lPenMod);
				// BigDecimal lKey = null;
				/* lKey = */lPenResiduaDAO.insert();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);

			rollback(lConn);
			throw new F3BException(
					"MisuraAlternativaController.ExCalcolaFineEspiazionePenaMARevocata: " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			//
			// rollback(lConn);
			// throw new F3BException("MisuraAlternativaController.ExCalcolaFineEspiazionePenaMARevocata: "
			// + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();

			rollback(lConn);
			throw new F3BException(
					"MisuraAlternativaController.ExCalcolaFineEspiazionePenaMARevocata: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lMisuraDAO);
			cleanup(lPenResDAO);
			cleanup(lPenResiduaDAO);

			cleanup(lConn);
		}

		return lMisMod;
	}

	/**
	 * ExRicercaMisuraAlternativaByFascicoloOrdinanza fine calcolo pena revocata
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaByFascicoloOrdinanza(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;

		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDao.ricercaMisuraAlternativaByFascicoloOrdinanza(aKey);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

			if (lMisMod == null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error(
						"+++++Non e' presente nessuna Misura Alternativa associata a quel fascicolo con quelle condizioni+++");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaController.ExRicercaMisuraAlternativaByFascicoloOrdinanza: " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException(
			// "MisuraAlternativaController.ExRicercaMisuraAlternativaByFascicoloOrdinanza: " + sqe);
		} finally {
			cleanup(lMisDao);

			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExModificaMisuraAlternativa
	 *
	 * @param aMisuraAlternativa
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExModificaMisuraAlternativa(MisuraAlternativaModel aMisuraAlternativa)
			throws F3BException {

		Connection lConn = null;
		MisuraAlternativaDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel(aMisuraAlternativa);

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaDAO(lConn);
			lMisDao.setDAOFromModelForUpdate(aMisuraAlternativa);
			lMisDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("MisuraAlternativaController.ExModifica: " + ex);
			// } catch (SQLException sqe) {
			// rollback(lConn);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException("MisuraAlternativaController.ExModificaMisuraAlternativa: " + sqe);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * ExInserisciOModificaOSNotifica
	 *
	 * @param aEvento
	 * @param tipoMisura
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaOSNotifica(EventoNotificaModel aEvento, String tipoMisura)
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

			lSqlDAO.ricercaEventoOSNonRegistratoMAByFascicoloSiep(
					aEvento.getEvento().getFasSieIdFascicoloSiep(), aEvento.getEvento().getEveIdEvento());

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

				EventoModel lEveMod = aEvento.getEvento();
				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				lEveDao.setCodMotivo(aEvento.getEvento().getCodMotivo());
				lEveDao.setDataEmissione(lEveMod.getDataEmissione());
				lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
				lEveDao.setCodLuogoEmittente(lEveMod.getCodLuogoEmittente());
				lEveDao.setCodUfficioEmittente(lEveMod.getCodUfficioEmittente());
				lEveDao.setCodMagistrato(lEveMod.getCodMagistrato());
				lEveDao.setAnnoProtocollo(lEveMod.getAnnoProtocollo());

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
			while (count < aEvento.getNotifiche().length) {
				if (aEvento.getNotifiche()[count] != null) {

					if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// Inserimento delle eventuali note aggiuntive.
			if (aEvento.getCampoNote() != null) {
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
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);

			rollback(lConn);
			throw new F3BException("MisuraAlternativaController.ExInserisciOModificaOSNotifica: " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			//
			// rollback(lConn);
			// throw new F3BException("MisuraAlternativaController.ExInserisciOModificaOSNotifica: " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();

			rollback(lConn);
			throw new F3BException("MisuraAlternativaController.ExInserisciOModificaOSNotifica: " + ex);
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
	 * ExInserisciOModificaMANotifica
	 *
	 * @param aEvento
	 * @param aPenaResidua
	 * @param aMisura
	 * @param aSospensione
	 * @return EventoNotificaModel
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaMANotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, MisuraAlternativaModel aMisura, SospensioneModel aSospensione)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaDAO lPenaResDao = null;
		PenaResiduaSqlDAO lPenaResSqlDao = null;
		MisuraAlternativaDAO lMisDAO = null;
		SospensioneDAO lSospDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);
			lSospDao = new SospensioneDAO(lConn);
			lPenaResSqlDao = new PenaResiduaSqlDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);

			// Recupero s presente l'evento SIEP
			lSqlDAO.ricercaEventoMANonRegistratoMAByFascicoloSiep(
					aEvento.getEvento().getFasSieIdFascicoloSiep(), aEvento.getEvento().getEveIdEvento());

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

				EventoModel lEveMod = aEvento.getEvento();
				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				lEveDao.setCodMotivo(aEvento.getEvento().getCodMotivo());
				lEveDao.setDataEmissione(lEveMod.getDataEmissione());
				lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
				lEveDao.setCodLuogoEmittente(lEveMod.getCodLuogoEmittente());
				lEveDao.setCodUfficioEmittente(lEveMod.getCodUfficioEmittente());
				lEveDao.setCodMagistrato(lEveMod.getCodMagistrato());
				lEveDao.setAnnoProtocollo(lEveMod.getAnnoProtocollo());

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
			while (count < aEvento.getNotifiche().length) {
				if (aEvento.getNotifiche()[count] != null) {
					if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);

					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// Inserimento delle eventuali note aggiuntive.
			if (aEvento.getCampoNote() != null) {
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

			// Aggiorna/Inserisce Pena Residua
			// il ramo del else serve solo per le revoche con calcolo e le prosecuzioni provvisoria e
			// sospensioni della pena
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("aPenaResidua = " + aPenaResidua);
			lPenaResDao = new PenaResiduaDAO(lConn);
			BigDecimal lPenKey = null;
			if (aPenaResidua != null && aPenaResidua.getIdPenaResidua() != null
					&& aPenaResidua.getDataFine() != null) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("1");
				lPenKey = aPenaResidua.getIdPenaResidua();
				lPenaResSqlDao.ricercaPenaResiduaByKey(lPenKey);
				PenaResiduaModel lPenMod = (PenaResiduaModel) lPenaResSqlDao.getModelByKey();

				if (lPenMod != null && "N".equals(lPenMod.getFlagValidato())) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("getFlagValidato = N vado in aggiornamento");
					// Se PR a sistema ma non validata. Aggancio la PR e la valido aggiornando
					// solo decorrenza e scadenza
					lPenaResDao.setIdPenaResidua(lPenKey);
					if (aPenaResidua.getDataInizio() != null) {
						lPenaResDao.setDataInizio(aPenaResidua.getDataInizio());
					}

					if (lPenMod.getDataFinePresunta() == null) {
						lPenaResDao.setDataFinePresunta(aPenaResidua.getDataFine());
					}
					lPenaResDao.setDataFine(aPenaResidua.getDataFine());

					lPenaResDao
							.setCodOperatoreAggiornamento(aEvento.getEvento().getCodOperatoreInserimento());
					lPenaResDao.setDataAggiornamento(DateUtils.getSysDate());
					lPenaResDao.setCodUfficioAggiornamento(aEvento.getEvento().getCodUfficioInserimento());

					lPenaResDao.selByKey();
					lPenaResDao.update();
					lPenaResDao.stop();
				} else if (lPenMod != null && "S".equals(lPenMod.getFlagValidato())) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("getFlagValidato = S vado in inserimento");
					lPenMod.setDataFine(aPenaResidua.getDataFine());
					if (aPenaResidua.getDataInizio() != null) {
						lPenaResDao.setDataInizio(aPenaResidua.getDataInizio());
					}

					if (lPenMod.getDataFinePresunta() == null) {
						lPenaResDao.setDataFinePresunta(aPenaResidua.getDataFine());
					}
					lPenMod.setIdPenaResidua(null);

					lPenMod.setCodOperatoreAggiornamento(null);
					lPenMod.setDataAggiornamento(null);
					lPenMod.setCodUfficioAggiornamento(null);

					lPenMod.setEveIdEvento(null);
					lPenMod.setFlagValidato("N");
					lPenMod.setCodOperatoreInserimento(aEvento.getEvento().getCodOperatoreInserimento());
					lPenMod.setDataInserimento(DateUtils.getSysDate());
					lPenMod.setCodUfficioInserimento(aEvento.getEvento().getCodUfficioInserimento());

					lPenaResDao.setDAOFromModel(lPenMod);
					lPenKey = lPenaResDao.insert();
					lPenaResDao.stop();
				}
			} else if (aPenaResidua != null && aPenaResidua.getIdPenaResidua() == null) // solo quando si
																						// ricalcola la pena
																						// quindi revoca e
																						// prosecuzioni e
																						// sospensioni della
																						// pena
			{
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("2 vado in inserimento");
				lPenaResDao.setDAOFromModel(aPenaResidua);
				if (lKeyEvento != null) {
					lPenaResDao.setEveIdEvento(lKeyEvento);
				}

				lPenKey = lPenaResDao.insert();
				lPenaResDao.stop();
			}

			if (aSospensione != null && lPenKey != null) {
				lSospDao.setDAOFromModel(aSospensione);
				lSospDao.setPenResIdPenaResidua(lPenKey);
				lSospDao.insert();
				lSospDao.stop();
			}

			// Aggiorna Misura alternativa data scarcerazione
			lMisDAO = new MisuraAlternativaDAO(lConn);
			if (aMisura != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiorna Misura alternativa: " + aMisura);
				lMisDAO.setDAOFromModelForUpdate(aMisura);
				lMisDAO.update();
				lMisDAO.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", daoEx);

			rollback(lConn);
			throw new F3BException("MisuraAlternativaController.ExInserisciOModificaMANotifica: " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: ", sqe);
			//
			// rollback(lConn);
			// throw new F3BException("MisuraAlternativaController.ExInserisciOModificaMANotifica: " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			ex.printStackTrace();

			rollback(lConn);
			throw new F3BException("MisuraAlternativaController.ExInserisciOModificaMANotifica: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lPenaResDao);
			cleanup(lPenaResSqlDao);
			cleanup(lMisDAO);
			cleanup(lSospDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * ExUpdateValidaMA
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMA(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		MisuraAlternativaDAO lMiDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		VerbaleModel lVerbMod = null;
		VerbaleSqlDAO lVerSql = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		LuogoDetenzioneDAO lLuogoDAO = null;
		LuogoDetenzioneSqlDAO lLuogoSql = null;
		EventoModel lEveMod = new EventoModel(aEvento);

		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoDAO lEveDaoMisAlt = null;

		EventoSqlDAO lEventoperAmmProvAffSqlDAO = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			String lDescrMotivo = lEveModel.getCodMotivo();

			// cerca Misura Alternariva by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// valido l'evento riferito alla misura alternativa
			if (lMisModelOrder != null && lMisModelOrder.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
				EventoModel lEveModelTDS = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelTDS != null && (lEveModelTDS.getFlagDocumentoRegistrato() == null
						|| lEveModelTDS.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelTDS.setFlagDocumentoRegistrato("S");
					lEveModelTDS.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelTDS.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelTDS.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelTDS);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// se vengo dalla sanzione sostitutiva forzo il la posizione giuridica che e' 19 in 03
			// perche' deve richiamare lo stesso template
			if (lMisModelOrder.getCodTipoMisura().equals("0001")
					|| lMisModelOrder.getCodTipoMisura().equals("0002")
					|| lMisModelOrder.getCodTipoMisura().equals("0003")) {
				if ("0605".equals(lDescrMotivo) || "0606".equals(lDescrMotivo)
						|| "0607".equals(lDescrMotivo)) {
					lCodPosizione = "03";
					lPosMod.setCodPosizioneGiuridica(lCodPosizione);
				}
			}

			// Cerca POSIZIONE_GIURIDICA precedente per vedere se e' gia' stato firmato il verbale di sott.
			// degli obllighi
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			Vector lPosizioni = new Vector();

			lPosSqlDAO.ricercaPosizioneGiuridicaByIdFascicoloDesc(aFascicolo.getIdFascicoloSiep());
			lPosizioni = new Vector(lPosSqlDAO.getModels());
			PosizioneGiuridicaModel lPoModelPrec = null;

			if (lPosizioni.size() > 1) {
				lPoModelPrec = (PosizioneGiuridicaModel) lPosizioni.get(1);
			}

			// Aggiorna Inserisci PENA_RESIDUA
			PenaResiduaModel lPenResMod = new PenaResiduaModel();
			lPenResMod = InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep(), null);

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = null;
			EventoModel lModel = new EventoModel(lEveMod);
			lModel.setDataEmissione(lEveModel.getDataEmissione());

			// Cerca "Evento Ammissione Provvisoria Affidamento"

			// Instanzia il model dell'evento
			EventoModel lEventperAmmProvAff = new EventoModel();

			// prende dalla Session l'ID del procedimento e lo carica nel model
			lEventperAmmProvAff.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			// carica nel model il Tipo evento
			lEventperAmmProvAff.setCodTipoEvento("01");
			// carica nel model il flag documento registrato
			lEventperAmmProvAff.setFlagDocumentoRegistrato("S");
			// carica nel model il tipo motivo

			/*
			 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato Numero MEV : SIES
			 * v10 Autore : gioggi Data : 28/gen/2016 Branch : MEV_SIES v10
			 */
			// String[] lMotivo = { "2006", "2008" };
			// ***** FINE INTERVENTO MEV_SIES v10 *****//

			String[] lMotivo = { "2006" };
			// carica nel model il codice dei provvedimenti
			String[] lProvv = { "04", "09", "12" };

			// Ricerca nella tabella EVENTO
			lEventoperAmmProvAffSqlDAO = new EventoSqlDAO(lConn);
			lEventoperAmmProvAffSqlDAO.ricercaEventoPerMotivoPerProvv(lMotivo, lProvv, lEventperAmmProvAff);
			EventoModel lEveModelperAmmProvAff = (EventoModel) lEventoperAmmProvAffSqlDAO.getModelByKey();
			BigDecimal IdEveAmmProvvAff = null;
			if (lEveModelperAmmProvAff != null && lEveModelperAmmProvAff.getIdEvento() != null) {
				IdEveAmmProvvAff = lEveModelperAmmProvAff.getIdEvento();
			}

			// if (tipoMisura.equals("DETENZIONE"))
			// Controllo per "DETENZIONE"
			if (lMisModelOrder.getCodTipoMisura().equals("0005")
					|| lMisModelOrder.getCodTipoMisura().equals("0010")
					|| lMisModelOrder.getCodTipoMisura().equals("0013")) {
				// Aggiunta gestione per Posizione Giuridica "29"
				if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null
				// new 50 Espiazione presso il domicilio
						&& lPosMod.getCodPosizioneGiuridica().equals("50")) {
					// Espiazione Pena in Regime di Detenzione Domiciliare - Emessa Comunicazione Scadenza
					// Misura
					lStatoProcMod = "0029";
				} else if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null
						&& lPosMod.getCodPosizioneGiuridica().equals("29") && lPoModelPrec != null
						&& lPoModelPrec.getCodPosizioneGiuridica() != null && lPoModelPrec.isLibero()) {
					lStatoProcMod = "0026";
				} else if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null
						&& lPosMod.getCodPosizioneGiuridica().equals("29") // Aggiunta gestione per Posizione
																			// Giuridica "29"
						&& lPoModelPrec != null && lPoModelPrec.getCodPosizioneGiuridica() != null
						&& lPoModelPrec.isLibero()) { // Passaggio da Libero a Detenzione Provvisoria
														// (verbale) a Definitiva
					lStatoProcMod = "0026"; // Espiazione Pena in Regime di Detenzione Domiciliare - Emessa
											// Ordinanza TDS
				} else if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null
						&& ((lPosMod.getCodPosizioneGiuridica().equals("12") // detenzione Domiciliare
								&& lPoModelPrec != null && lPoModelPrec.getCodPosizioneGiuridica() != null
								&& lPoModelPrec.isLibero() && lMisModelOrder.getDataInizioMisura() != null)
								|| (lPosMod.getCodPosizioneGiuridica().equals("29")))) {
					lStatoProcMod = "0028"; // Espiazione Pena in Regime di Detenzione Domiciliare - Emesso
											// Decreto con Decorrenza/Scadenza
				} else if ((lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null
						&& lPosMod.isLibero() && lMisModelOrder.getDataInizioMisura() == null)
						|| "PROC".equals(lMisModelOrder.getCodTipoUfficioScarcerazione())) {
					lStatoProcMod = "0027"; // Espiazione Pena in Regime di Detenzione Domiciliare - Emesso
											// Ordine di Esecuzione
				} else if ("SORV".equals(lMisModelOrder.getCodTipoUfficioScarcerazione())) {
					lStatoProcMod = "0029"; // Espiazione Pena in Regime di Detenzione Domiciliare - Emessa
											// Comunicazione Scadenza Misura
				} else {
					lStatoProcMod = "0026"; // Default
				}
			}
			// else if (tipoMisura.equals("AFFIDAMENTO"))
			// Controllo per affidamento in prova
			else if (lMisModelOrder.getCodTipoMisura().equals("0001")
					|| lMisModelOrder.getCodTipoMisura().equals("0002")
					|| lMisModelOrder.getCodTipoMisura().equals("0003")
					|| lMisModelOrder.getCodTipoMisura().equals("0030")) {
				if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null && lPosMod.isLibero()
						&& lMisModelOrder != null && lMisModelOrder.getDataInizioMisura() == null) {
					lStatoProcMod = "0023";
				} else if ((lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null
						&& lPoModelPrec != null && lPoModelPrec.getCodPosizioneGiuridica() != null
						&& lPoModelPrec.isLibero() // Precedente libero
						&& lPosMod.getCodPosizioneGiuridica().equals("13") // Attuale in Affidamento
						&& lMisModelOrder.getDataInizioMisura() != null)
						|| ("PROC".equals(lMisModelOrder.getCodTipoUfficioScarcerazione())
								&& !lPosMod.getCodPosizioneGiuridica().equals("29") // FIXME 04/09/2015 TEST
																					// MEVxx -
																					// Gestione Misure
																					// Provvisorie
						)) {
					lStatoProcMod = "0024"; // Espiazione Pena in Regime di Affidamento in Prova - Emesso
											// Decreto con Decorrenza/Scadenza
				} else if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
						&& "SORV".equals(lMisModelOrder.getCodTipoUfficioScarcerazione())) {
					lStatoProcMod = "0025"; // Espiazione Pena in Regime di Affidamento in Prova - Emessa
											// Comunicazione Scadenza Misura
				} else if (lPosMod.getCodPosizioneGiuridica().equals("13")
						&& (IdEveAmmProvvAff != null && !"".equals(IdEveAmmProvvAff.toString()))) {
					lStatoProcMod = "0025"; // Espiazione Pena in Regime di Affidamento in Prova - Emessa
											// Comunicazione Scadenza Misura
				} else {
					if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null && lPoModelPrec != null
							&& lPoModelPrec.isLibero() && lPosMod.getCodPosizioneGiuridica().equals("13")
							&& lMisModelOrder != null && lMisModelOrder.getDataInizioMisura() != null) {
						lModel.setDataEmissione(lMisModelOrder.getDataInizioMisura());
					}

					lStatoProcMod = "0022";
				}
			}
			// else if (tipoMisura.equals("SEMILIBERTA"))
			// Controllo per semiliberta
			else if (lMisModelOrder.getCodTipoMisura().equals("0004")) {
				if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null && (lPosMod.isLibero())
						&& lMisModelOrder != null && lMisModelOrder.getDataInizioMisura() == null) {

					lStatoProcMod = "0031";
				} else if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null
						&& ((lPoModelPrec != null && lPoModelPrec.getCodPosizioneGiuridica() != null
								&& (lPoModelPrec.isLibero())
								&& lPosMod.getCodPosizioneGiuridica().equals("14") && lMisModelOrder != null
								&& lMisModelOrder.getDataInizioMisura() != null)
								|| lPosMod.getCodPosizioneGiuridica().equals("03")
								|| lPosMod.getCodPosizioneGiuridica().equals("04")
								|| lPosMod.getCodPosizioneGiuridica().equals("53")
								// 16/04/2015
								// MEV 10 S3 gestite le nuove posizioni giuridiche (82,83,84)
								|| lPosMod.getCodPosizioneGiuridica().equals("82")
								|| lPosMod.getCodPosizioneGiuridica().equals("83")
								|| lPosMod.getCodPosizioneGiuridica().equals("84"))) // 17/12/2010

				{
					lStatoProcMod = "0032";
				} else {
					lStatoProcMod = "0030";
				}
			}
			// else if (tipoMisura.equals("INDULTINO"))
			// Controllo per indultino
			else if (lMisModelOrder.getCodTipoMisura().equals("2245")) {
				if ((lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null && lPoModelPrec != null
						&& lPoModelPrec.getCodPosizioneGiuridica() != null
						&& ((lPoModelPrec.isLibero()) && lMisModelOrder.getDataInizioMisura() != null
								&& lPosMod.getCodPosizioneGiuridica().equals("27")))
						|| lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
					lStatoProcMod = "0080";
				} else if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null
						&& (lPosMod.isLibero()) && lMisModelOrder.getDataInizioMisura() == null) {
					lStatoProcMod = "0079";
				} else if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null
						&& (lPosMod.getCodPosizioneGiuridica().equals("03")
								|| lPosMod.getCodPosizioneGiuridica().equals("12")
								|| lPosMod.getCodPosizioneGiuridica().equals("04")
								|| lPosMod.getCodPosizioneGiuridica().equals("53") || // 17/12/2010
								lPosMod.getCodPosizioneGiuridica().equals("50") || // 15/02/2011 paolo
																					// cherubini
								lPosMod.getCodPosizioneGiuridica().equals("14")
								|| lPosMod.getCodPosizioneGiuridica().equals("82")
								|| lPosMod.getCodPosizioneGiuridica().equals("83")
								|| lPosMod.getCodPosizioneGiuridica().equals("84")
								|| lPosMod.getCodPosizioneGiuridica().equals("85")
								|| lPosMod.getCodPosizioneGiuridica().equals("86")
								|| lPosMod.getCodPosizioneGiuridica().equals("87"))
						&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
					lStatoProcMod = "0081";
				} else {
					lStatoProcMod = "0080";
				}
			}

			// 28/09/2010 Controllo per Espiazione Pena Presso Domicilio
			else if (lMisModelOrder.getCodTipoMisura().equals("2630")
					|| lMisModelOrder.getCodTipoMisura().equals("0610"))
			/*
			 * { if ( ( lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null && lPoModelPrec != null
			 * && lPoModelPrec.getCodPosizioneGiuridica() != null && ( ( lPoModelPrec.isLibero() ) &&
			 * lMisModelOrder.getDataInizioMisura() != null && lPosMod.getCodPosizioneGiuridica().equals("50")
			 * ) ) || lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) { lStatoProcMod =
			 * "0391"; } else if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null &&
			 * (lPosMod.isLibero()) && lMisModelOrder.getDataInizioMisura() == null) { lStatoProcMod = "0390";
			 * } else if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null &&
			 * (lPosMod.getCodPosizioneGiuridica().equals("03") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("12") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("04") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("53") || //17/12/2010
			 * lPosMod.getCodPosizioneGiuridica().equals("14")) &&
			 * lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) { lStatoProcMod = "0392"; }
			 * else { lStatoProcMod = "0391"; } }
			 */

			// Paolo Cherubini 04/02/2011 semplifico l'if soprastante poiche' ce' un errore per soggetto
			// libero
			// mette subito
			// 0391 Concessione Esecuzione Pena presso Domicilio - Emesso Decreto con Decorrenza/Scadenza
			{
				// 16/06/2011 Test NullValue delle Pos. Giu.
				if (lPosMod != null && lPosMod.isLibero()) {
					// per libero e' sempre 0390 Concessione Esecuzione Pena presso Domicilio - Richiesta Data
					// Sottoscrizione Obblighi
					lStatoProcMod = "0390";
				} else if (lPoModelPrec != null && lPoModelPrec.isLibero()
						&& lPosMod.getCodPosizioneGiuridica().equals("50")) {
					// se era libero ed ora e' in 50 Esecuzione Pena presso Domicilio significa che siamo al
					// II
					// giro dopo Verbale sto
					// sicuramente facendo un decreto Decorrenza/Scadenza
					// 0391 Concessione Esecuzione Pena presso Domicilio - Emesso Decreto con
					// Decorrenza/Scadenza
					lStatoProcMod = "0391";
				} else {
					// tutti gli altri casi
					// 0392 Concessione Esecuzione Pena presso Domicilio - Emessa Comunicazione Scadenza
					// Misura
					lStatoProcMod = "0392";
				}
			}

			// AMBROSINO - VENGO DA AMMISSIONE PROVVISORIA - Cambio da 51 a 54
			// if(lPosMod.getCodPosizioneGiuridica().equals("54"))
			// lStatoProcMod = "0022"; //Espiazione Pena in Regime di Affidamento in Prova - Emessa Ordinanza
			// TDS
			// FIXME da correggere: se sto concedendo Detenzione Domiciliare da Affidamento Porvvisorio
			// lo stato 0022 non e' corretto (0026)

			if (lStatoProcMod != null) {
				InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lModel,
						lStatoProcMod);
			}

			// paolo cherubini 27/01/2011 provo a commentare tutto il paragrafo relativo alla modifica della
			// misura alternativa
			// mi sembra assurdo modificare la misura dopo aver stampato il template
			/*
			 * // aggiorna misura alternativa lMiDAO = new MisuraAlternativaDAO(lConn);
			 *
			 * //if (tipoMisura.equals("DETENZIONE")) // Controllo per "DETENZIONE DOMICILIARE" if
			 * (lMisModelOrder.getCodTipoMisura().equals("0005") ||
			 * lMisModelOrder.getCodTipoMisura().equals("0010") ||
			 * lMisModelOrder.getCodTipoMisura().equals("0013")) { if (lPosMod != null &&
			 * lPosMod.getCodPosizioneGiuridica() != null && (lPosMod.getCodPosizioneGiuridica().equals("13")
			 * || lPosMod.getCodPosizioneGiuridica().equals("14"))) {
			 * lMisSqlDAO.ricercaMisuraAlternativaByIdEventoDataInizio(lEveModel.getIdEvento(), lPosMod);
			 * MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey(); // se
			 * esiste occorrenza con dataInizioPosizioneGiuridica=dataInizioMisura //aggiorno la misura if
			 * (lMisModel != null) { lMiDAO.setCondizioneUpdate(lMisModel.getIdMisuraAlternativa());
			 * lMiDAO.setDataFineMisura(lEveModel.getDataEmissione()); lMiDAO.update(); lMiDAO.stop(); }
			 *
			 * if (lMisModelOrder != null) { // se esiste occorrenza data inserimento piu' alta //aggiorno la
			 * misura lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataInizioMisura(lEveModel.getDataEmissione());
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine()); lMiDAO.update(); lMiDAO.stop(); } } else if
			 * (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null && lMisModelOrder != null &&
			 * lMisModelOrder.getCodTipoUfficioScarcerazione() != null &&
			 * lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV") &&
			 * (lPosMod.getCodPosizioneGiuridica().equals("03") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("14") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("04")||
			 * lPosMod.getCodPosizioneGiuridica().equals("53"))) { if (lMisModelOrder != null) { // se esiste
			 * occorrenza corrente //aggiorno la misura
			 * lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataInizioMisura(lMisModelOrder.getDataScarcerazione());
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine()); lMiDAO.update(); lMiDAO.stop(); } } else if
			 * ((lCodPosizione.equals("12") && lPoModelPrec != null && lPoModelPrec.isLibero()) ||
			 * lCodPosizione.equals("29")) {
			 * lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine()); lMiDAO.update(); lMiDAO.stop(); } else if
			 * (!lPosMod.isLibero()) { if (lMisModelOrder != null) {
			 * lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataInizioMisura(lEveModel.getDataEmissione()); lMiDAO.update(); lMiDAO.stop(); } } }
			 * //else if (tipoMisura.equals("AFFIDAMENTO")) // Controllo per affidamento in prova else if
			 * (lMisModelOrder.getCodTipoMisura().equals("0001") ||
			 * lMisModelOrder.getCodTipoMisura().equals("0002") ||
			 * lMisModelOrder.getCodTipoMisura().equals("0003") ||
			 * lMisModelOrder.getCodTipoMisura().equals("0030") ) { if (lPosMod != null &&
			 * lPosMod.getCodPosizioneGiuridica() != null && (lPosMod.getCodPosizioneGiuridica().equals("12")
			 * || lPosMod.getCodPosizioneGiuridica().equals("14"))) {
			 * lMisSqlDAO.ricercaMisuraAlternativaByIdEventoDataInizio(lEveModel.getIdEvento(), lPosMod);
			 * MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey(); // se
			 * esiste occorrenza con dataInizioPosizioneGiuridica=dataInizioMisura //aggiorno la misura if
			 * (lMisModel != null) { lMiDAO.setCondizioneUpdate(lMisModel.getIdMisuraAlternativa());
			 * lMiDAO.setDataFineMisura(lEveModel.getDataEmissione()); lMiDAO.update(); lMiDAO.stop(); }
			 *
			 * if (lMisModelOrder != null) { // se esiste occorrenza data inserimento piu' alta //aggiorno la
			 * misura lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataInizioMisura(lEveModel.getDataEmissione());
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine());
			 *
			 * lMiDAO.update(); lMiDAO.stop(); } } else if (lPosMod != null &&
			 * lPosMod.getCodPosizioneGiuridica() != null && lMisModelOrder != null &&
			 * lMisModelOrder.getCodTipoUfficioScarcerazione() != null &&
			 * lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV") &&
			 * (lPosMod.getCodPosizioneGiuridica().equals("03") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("14") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("04") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("53") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("12"))) { // se esiste occorrenza corrente //aggiorno
			 * la misura lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataInizioMisura(lMisModelOrder.getDataScarcerazione());
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine());
			 *
			 * lMiDAO.update(); lMiDAO.stop(); } else if (lCodPosizione.equals("13") && lPoModelPrec != null
			 * && lPoModelPrec.isLibero()) {
			 * lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine()); lMiDAO.update(); lMiDAO.stop(); } else
			 * if(lPosMod.getCodPosizioneGiuridica().equals("13") && (IdEveAmmProvvAff != null &&
			 * !IdEveAmmProvvAff.equals(""))) {
			 * lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine()); lMiDAO.update(); lMiDAO.stop(); } else if (
			 * (! (lCodPosizione.equals("13") && lPoModelPrec != null && lPoModelPrec.isLibero())) &&
			 * (!lPosMod.isLibero())) { if (lMisModelOrder != null) {
			 * lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa()); if(lMisModelOrder != null
			 * && lMisModelOrder.getDataScarcerazione() != null) {
			 * lMiDAO.setDataInizioMisura(lMisModelOrder.getDataScarcerazione()); } else {
			 * lMiDAO.setDataInizioMisura(lEveModel.getDataEmissione()); }
			 *
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine());
			 *
			 * lMiDAO.update(); lMiDAO.stop(); } } } //else if (tipoMisura.equals("SEMILIBERTA")) //Controllo
			 * per semiliberta else if (lMisModelOrder.getCodTipoMisura().equals("0004")) { if
			 * (lCodPosizione.equals("14") && lPoModelPrec != null && lPoModelPrec.isLibero()) {
			 * lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine()); lMiDAO.update(); lMiDAO.stop(); } else if
			 * (!lPosMod.isLibero()) {
			 * lMisSqlDAO.ricercaMisuraAlternativaByIdEventoDataInizio(lEveModel.getIdEvento(), lPosMod);
			 * MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey(); // se
			 * esiste occorrenza con dataInizioPosizioneGiuridica=dataInizioMisura //aggiorno la misura if
			 * (lMisModel != null) { lMiDAO.setCondizioneUpdate(lMisModel.getIdMisuraAlternativa());
			 * lMiDAO.setDataFineMisura(lEveModel.getDataEmissione()); lMiDAO.update(); lMiDAO.stop(); } if
			 * (lMisModelOrder != null) { // se esiste occorrenza data inserimento piu' alta //aggiorno la
			 * misura lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataInizioMisura(lEveModel.getDataEmissione());
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine());
			 *
			 * lMiDAO.update(); lMiDAO.stop(); } } } //else if (tipoMisura.equals("INDULTINO")) //Controllo
			 * per indultino // 28/09/2010 Aggiunto Controllo per Espiazione Pena presso Domicilio else if
			 * (lMisModelOrder.getCodTipoMisura().equals("2245") ||
			 * lMisModelOrder.getCodTipoMisura().equals("2630")) { if (lPosMod != null &&
			 * lPosMod.getCodPosizioneGiuridica() != null && lMisModelOrder != null &&
			 * lMisModelOrder.getCodTipoUfficioScarcerazione() != null &&
			 * lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV") &&
			 * (lPosMod.getCodPosizioneGiuridica().equals("03") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("14") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("04") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("53") ||
			 * lPosMod.getCodPosizioneGiuridica().equals("12"))) {
			 * lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataInizioMisura(lMisModelOrder.getDataScarcerazione());
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine()); lMiDAO.update(); lMiDAO.stop(); } else if
			 * (lCodPosizione.equals("27") && lPoModelPrec != null && lPoModelPrec.isLibero()) {
			 * lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine()); lMiDAO.update(); lMiDAO.stop(); } else if (
			 * (! (lCodPosizione.equals("27") && lPoModelPrec != null && lPoModelPrec.isLibero())) &&
			 * (!lPosMod.isLibero())) { if (lMisModelOrder != null) {
			 * lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa()); if(lMisModelOrder != null
			 * && lMisModelOrder.getDataScarcerazione() != null)
			 * lMiDAO.setDataInizioMisura(lMisModelOrder.getDataScarcerazione()); else
			 * lMiDAO.setDataInizioMisura(lEveModel.getDataEmissione());
			 *
			 * lMiDAO.setDataFineMisura(lPenResMod.getDataFine()); lMiDAO.update(); lMiDAO.stop(); } } } fine
			 * paolo cherubini 27/01/2011
			 */

			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);

			// ** Aggiorna SCADENZARIO FINE PENA**
			ScadenzarioModel lScaMod = null;
			if (lPenResMod != null && lPenResMod.getDataInizio() != null && lPenResMod.getDataFine() != null
					&& lPenResMod.getFlagValidato().equals("S")) {
				InserimentoAggiornamentoScadenzarioFinePena(lConn, lPenResMod, lEveModel,
						lPenResMod.getDataFine(), aFascicolo.getIdFascicoloSiep());
			}

			// scadenzario MISURA ALTERNATIVA
			if (lMisModelOrder != null && lMisModelOrder.getDataFineMisura() != null
					&& lMisModelOrder.getDataInizioMisura() != null) {
				// aggiorna scadenzario MISURA ALTERNATIVA
				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("13",
						aFascicolo.getIdFascicoloSiep());
				lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

				if (lScaMod != null) {
					lScaDao.setDataFineScadenza(lMisModelOrder.getDataFineMisura());
					lScaDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
					lScaDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
					lScaDao.setDataAggiornamento(DateUtils.getSysDate());
					lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDao.update();
					lScaDao.stop();
				} else {
					// inserisce scadenzario
					ScadenzarioModel lScaModel = new ScadenzarioModel();
					lScaModel.setCodTipoScadenzario("13");
					lScaModel.setDataInizioScadenza(lMisModelOrder.getDataInizioMisura());
					lScaModel.setDataFineScadenza(lMisModelOrder.getDataFineMisura());

					lScaModel.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
					lScaModel.setDataInserimento(DateUtils.getSysDate());
					lScaModel.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
					lScaModel.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

					lScaDao.setDAOFromModel(lScaModel);
					lScaDao.insert();
					lScaDao.stop();
				}
			}

			// Aggiorna POSIZIONE_GIURIDICA commentata 6//11/03
			BigDecimal lKeyPos = null;
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosizioneGiuridicaCorrente(lPosMod);
			PosizioneGiuridicaModel lPosModelData = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();
			PosizioneGiuridicaModel lPosizione = new PosizioneGiuridicaModel();

			// if (tipoMisura.equals("DETENZIONE"))
			// Controllo per "DETENZIONE"
			if (lMisModelOrder.getCodTipoMisura().equals("0005") // Detenzione Domiciliare art. 47 ter O.P.
					|| lMisModelOrder.getCodTipoMisura().equals("0010") // Detenzione Domiciliare art. 47 ter
																		// 1 bis
					|| lMisModelOrder.getCodTipoMisura().equals("0013")) // Detenzione Domiciliare art. 47
																			// quater o.p.
			{
				if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null && !lPosMod.isLibero()
						&& !lPosMod.getCodPosizioneGiuridica().equals("12")) {
					if (lPosMod.getCodPosizioneGiuridica().equals("03")
							|| lPosMod.getCodPosizioneGiuridica().equals("04")
							|| lPosMod.getCodPosizioneGiuridica().equals("53")
							|| lPosMod.getCodPosizioneGiuridica().equals("50")
							|| lPosMod.getCodPosizioneGiuridica().equals("14")
							|| lPosMod.getCodPosizioneGiuridica().equals("29")
							// || lPosMod.getCodPosizioneGiuridica().equals("54") //04/09/2015 MEVxx -
							// Gestione Misure
							// Provvisorie
							// 16/04/2015
							// MEV 10 S3 gestite le nuove posizioni giuridiche (82,83,84)
							|| lPosMod.getCodPosizioneGiuridica().equals("82")
							|| lPosMod.getCodPosizioneGiuridica().equals("83")
							|| lPosMod.getCodPosizioneGiuridica().equals("84")
							// 07/09/2015
							// MEV 10 S3 gestite le nuove posizioni giuridiche (85,86,87)
							|| lPosMod.getCodPosizioneGiuridica().equals("85")
							|| lPosMod.getCodPosizioneGiuridica().equals("86")
							|| lPosMod.getCodPosizioneGiuridica().equals("87")) {
						if (lPosModelData != null && lPosModelData.getDataFine() == null) {
							if (lMisModelOrder != null
									&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
									&& "SORV".equals(lMisModelOrder.getCodTipoUfficioScarcerazione())) {
								lPosDao.setDataFine(lMisModelOrder.getDataScarcerazione());
							} else if (lPosMod.getCodPosizioneGiuridica().equals("29")
							// || lPosMod.getCodPosizioneGiuridica().equals("54") //FIXME 04/09/2015 TEST
							// MEVxx - Gestione Misure Provvisorie
							) {
								lPosDao.setDataFine(lMisModelOrder.getDataInizioMisura());
							} else {
								lPosDao.setDataFine(lEveModel.getDataEmissione());
							}

							lPosDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
							lPosDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
							lPosDao.setDataAggiornamento(lEveMod.getDataAggiornamento());

							lPosDao.setCondizioneUpdate(lPosModelData.getIdPosizioneGiuridica());
							lPosDao.update();
							lPosDao.stop();
						}

						// Inserisco la nuova PG
						lPosizione.setCodPosizioneGiuridica("12");

						if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
								&& "SORV".equals(lMisModelOrder.getCodTipoUfficioScarcerazione())) {
							lPosizione.setDataInizio(lMisModelOrder.getDataScarcerazione());
						} else if (lPosMod.getCodPosizioneGiuridica().equals("29")
								|| lPosMod.getCodPosizioneGiuridica().equals("54") // FIXME 04/09/2015 TEST
																					// MEVxx - Gestione Misure
																					// Provvisorie
						) {
							lPosizione.setDataInizio(lMisModelOrder.getDataInizioMisura());
						} else {
							lPosizione.setDataInizio(lEveModel.getDataEmissione());
						}

						lPosizione.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
						lPosizione.setIdEventoRiferimento(aEvento.getIdEvento());
						lPosizione.setCodPosizioneProcessuale("-");

						lPosizione.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
						lPosizione.setDataInserimento(lEveMod.getDataAggiornamento());
						lPosizione.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());

						lPosDao.setDAOFromModel(lPosizione);
						lKeyPos = lPosDao.insert();
						lPosDao.stop();
					}
				}
			} else
			// if (tipoMisura.equals("SEMILIBERTA"))
			// Controllo per semiliberta
			if (lMisModelOrder.getCodTipoMisura().equals("0004")) // Semiliberta'
			{
				if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null && !lPosMod.isLibero()
						&& !lPosMod.getCodPosizioneGiuridica().equals("14")) {
					if (lPosMod.getCodPosizioneGiuridica().equals("03")
							|| lPosMod.getCodPosizioneGiuridica().equals("04")
							|| lPosMod.getCodPosizioneGiuridica().equals("53")
							// 16/04/2015
							// MEV 10 S3 gestite le nuove posizioni giuridiche (82,83,84)
							|| lPosMod.getCodPosizioneGiuridica().equals("82")
							|| lPosMod.getCodPosizioneGiuridica().equals("83")
							|| lPosMod.getCodPosizioneGiuridica().equals("84")) {
						if (lPosModelData != null && lPosModelData.getDataFine() == null) {
							lPosDao.setDataFine(lEveModel.getDataEmissione());
							lPosDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
							lPosDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
							lPosDao.setDataAggiornamento(lEveMod.getDataAggiornamento());

							lPosDao.setCondizioneUpdate(lPosModelData.getIdPosizioneGiuridica());
							lPosDao.update();
							lPosDao.stop();
						}
						lPosizione.setCodPosizioneGiuridica("14");
						lPosizione.setDataInizio(lEveMod.getDataEmissione());
						lPosizione.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
						lPosizione.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
						lPosizione.setDataInserimento(lEveMod.getDataAggiornamento());
						lPosizione.setCodPosizioneProcessuale("-");
						lPosizione.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
						lPosizione.setIdEventoRiferimento(aEvento.getIdEvento());

						lPosDao.setDAOFromModel(lPosizione);
						lKeyPos = lPosDao.insert();
						lPosDao.stop();
					}
				}
			} else
			// if (tipoMisura.equals("AFFIDAMENTO"))
			// Controllo per affidamento in prova
			if (lMisModelOrder.getCodTipoMisura().equals("0001") // Affidamento Servizio Sociale ex art. 94
																	// DPR 309/90
					|| lMisModelOrder.getCodTipoMisura().equals("0002") // Affidamento al Servizio Sociale
					|| lMisModelOrder.getCodTipoMisura().equals("0003") // Affidamento art. 47 quater O.P.
					|| lMisModelOrder.getCodTipoMisura().equals("0030") // Differimento Pena facoltativo
																		// attesa grazia
			) {
				if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null && !lPosMod.isLibero()
						&& !lPosMod.getCodPosizioneGiuridica().equals("13")) {
					if (lPosMod.getCodPosizioneGiuridica().equals("03")
							|| lPosMod.getCodPosizioneGiuridica().equals("14")
							|| lPosMod.getCodPosizioneGiuridica().equals("12")
							|| lPosMod.getCodPosizioneGiuridica().equals("04")
							|| lPosMod.getCodPosizioneGiuridica().equals("53")
							|| lPosMod.getCodPosizioneGiuridica().equals("50") // PAOLO CHERUBINI 15/02/2011
							// 17/04/2015
							// MEV 10 S3 gestite le nuove posizioni giuridiche (82,83,84)
							|| lPosMod.getCodPosizioneGiuridica().equals("82")
							|| lPosMod.getCodPosizioneGiuridica().equals("83")
							|| lPosMod.getCodPosizioneGiuridica().equals("84")
							|| lPosMod.getCodPosizioneGiuridica().equals("54") // Affidamento Provvisorio
							|| lPosMod.getCodPosizioneGiuridica().equals("29") // 04/09/2015 MEV29 - Punto 20
																				// Gestione Affidamento da
																				// Detenzione Provvisoria
					) {
						if (lPosModelData != null && lPosModelData.getDataFine() == null) {
							// Chiudo la vecchia posizione (data Fine)
							if (lMisModelOrder != null
									&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
									&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
								lPosDao.setDataFine(lMisModelOrder.getDataScarcerazione());
							} else {
								lPosDao.setDataFine(lEveModel.getDataEmissione());
							}

							lPosDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
							lPosDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
							lPosDao.setDataAggiornamento(lEveMod.getDataAggiornamento());

							lPosDao.setCondizioneUpdate(lPosModelData.getIdPosizioneGiuridica());
							lPosDao.update();
							lPosDao.stop();
						}

						// Inserisco la nuova posizione
						lPosizione.setCodPosizioneGiuridica("13");
						if (lMisModelOrder != null && lMisModelOrder.getDescrLuogoProva() != null) {
							lPosizione.setLuogoProvaAffidamento(lMisModelOrder.getDescrLuogoProva());
						}
						if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
								&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
							lPosizione.setDataInizio(lMisModelOrder.getDataScarcerazione());
						} else {
							lPosizione.setDataInizio(lEveModel.getDataEmissione());
						}

						lPosizione.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
						lPosizione.setIdEventoRiferimento(aEvento.getIdEvento());
						lPosizione.setCodPosizioneProcessuale("-");

						lPosizione.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
						lPosizione.setDataInserimento(lEveMod.getDataAggiornamento());
						lPosizione.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());

						lPosDao.setDAOFromModel(lPosizione);
						lKeyPos = lPosDao.insert();
						lPosDao.stop();
					}
				} else if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null
						&& lPosMod.getCodPosizioneGiuridica().equals("13") && lMisModelOrder != null
						&& lMisModelOrder.getDescrLuogoProva() != null) {
					if (lPosModelData != null) { // Aggiorno solo il luogo
						lPosDao.setLuogoProvaAffidamento(lMisModelOrder.getDescrLuogoProva());

						lPosDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
						lPosDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
						lPosDao.setDataAggiornamento(lEveMod.getDataAggiornamento());

						lPosDao.setCondizioneUpdate(lPosModelData.getIdPosizioneGiuridica());
						lPosDao.update();
						lPosDao.stop();
					}
				}
			}
			// else if (tipoMisura.equals("INDULTINO"))
			// Controllo per indultino ed Espiazione presso il domicilio
			// 28/09/2010 Aggiunto Controllo per Espiazione Pena presso Domicilio
			else if (lMisModelOrder.getCodTipoMisura().equals("2245") // INDULTINO
					|| lMisModelOrder.getCodTipoMisura().equals("2630")
					|| lMisModelOrder.getCodTipoMisura().equals("0610") // ESPIAZIONE PRESSO IL DOMICILIO
			) {
				if (lPosMod.getCodPosizioneGiuridica().equals("03")
						|| lPosMod.getCodPosizioneGiuridica().equals("14")
						|| lPosMod.getCodPosizioneGiuridica().equals("12")
						|| lPosMod.getCodPosizioneGiuridica().equals("04")
						|| lPosMod.getCodPosizioneGiuridica().equals("53")
						|| lPosMod.getCodPosizioneGiuridica().equals("82")
						|| lPosMod.getCodPosizioneGiuridica().equals("83")
						|| lPosMod.getCodPosizioneGiuridica().equals("84")
						|| lPosMod.getCodPosizioneGiuridica().equals("85")
						|| lPosMod.getCodPosizioneGiuridica().equals("86")
						|| lPosMod.getCodPosizioneGiuridica().equals("87")
						// MEV29 aggiunte le provvisorie per ESP PRE DOM
						|| lPosMod.getCodPosizioneGiuridica().equals("29")
						|| lPosMod.getCodPosizioneGiuridica().equals("54")) {
					// chiudo la vecchia posizione (data fine)
					if (lPosMod != null && lPosMod.getDataFine() == null) {
						if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
								&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
							lPosDao.setDataFine(lMisModelOrder.getDataScarcerazione());
						} else {
							lPosDao.setDataFine(lEveModel.getDataEmissione());
						}

						lPosDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
						lPosDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
						lPosDao.setDataAggiornamento(lEveMod.getDataAggiornamento());

						lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());
						lPosDao.update();
						lPosDao.stop();
					}

					// Inserisco la nuova posizione
					if (lMisModelOrder.getCodTipoMisura().equals("2245"))
						lPosizione.setCodPosizioneGiuridica("27");
					if (lMisModelOrder.getCodTipoMisura().equals("2630")
							|| lMisModelOrder.getCodTipoMisura().equals("0610"))
						lPosizione.setCodPosizioneGiuridica("50");

					if (lMisModelOrder != null && lMisModelOrder.getDescrLuogoProva() != null) {
						lPosizione.setLuogoProvaAffidamento(lMisModelOrder.getDescrLuogoProva());
					}
					if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& "SORV".equals(lMisModelOrder.getCodTipoUfficioScarcerazione())) {
						lPosizione.setDataInizio(lMisModelOrder.getDataScarcerazione());
					} else {
						lPosizione.setDataInizio(lEveModel.getDataEmissione());
					}
					lPosizione.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
					lPosizione.setDataInserimento(lEveMod.getDataAggiornamento());
					lPosizione.setCodPosizioneProcessuale("-");
					lPosizione.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
					lPosizione.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					lPosizione.setIdEventoRiferimento(aEvento.getIdEvento());

					lPosDao.setDAOFromModel(lPosizione);
					lKeyPos = lPosDao.insert();
					lPosDao.stop();
				}
			}

			// AMBROSINO - Vengo da Ammissione provvisoria
			// if (tipoMisura.equals("AFFIDAMENTO"))
			// Controllo per affidamento in prova
			if (lMisModelOrder.getCodTipoMisura().equals("0001")
					|| lMisModelOrder.getCodTipoMisura().equals("0002")
					|| lMisModelOrder.getCodTipoMisura().equals("0003")
					|| lMisModelOrder.getCodTipoMisura().equals("0030")) {
				if (lPosMod.getCodPosizioneGiuridica().equals("54")) {
					if (lPosModelData != null && lPosModelData.getDataFine() == null) {
						if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
								&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
							lPosDao.setDataFine(lMisModelOrder.getDataScarcerazione());
						} else {
							lPosDao.setDataFine(lEveModel.getDataEmissione());
						}

						lPosDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
						lPosDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
						lPosDao.setDataAggiornamento(lEveMod.getDataAggiornamento());

						lPosDao.setCondizioneUpdate(lPosModelData.getIdPosizioneGiuridica());
						lPosDao.update();
						lPosDao.stop();
					}
					lPosizione.setCodPosizioneGiuridica("13");
					if (lMisModelOrder != null && lMisModelOrder.getDescrLuogoProva() != null) {
						lPosizione.setLuogoProvaAffidamento(lMisModelOrder.getDescrLuogoProva());
					}
					if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
						lPosizione.setDataInizio(lMisModelOrder.getDataScarcerazione());
					} else {
						lPosizione.setDataInizio(lEveModel.getDataEmissione());
					}

					lPosizione.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
					lPosizione.setDataInserimento(lEveMod.getDataAggiornamento());
					lPosizione.setCodPosizioneProcessuale("-");
					lPosizione.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
					lPosizione.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					lPosizione.setIdEventoRiferimento(aEvento.getIdEvento()); // **

					lPosDao.setDAOFromModel(lPosizione);
					lKeyPos = lPosDao.insert();
					lPosDao.stop();
				}
			}
			// -- End Ambrosino

			// Aggiorna tabella nome_provvedimento
			String lPosizioneGiu = lPosModelData.getCodPosizioneGiuridica();

			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			// if (tipoMisura.equals("DETENZIONE"))
			// Controllo per "DETENZIONE"
			if (lMisModelOrder.getCodTipoMisura().equals("0005")
					|| lMisModelOrder.getCodTipoMisura().equals("0010")
					|| lMisModelOrder.getCodTipoMisura().equals("0013")) {
				if (lPosizioneGiu.equals("03")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP029");
				} else if (lPosMod.isLibero() && lMisModelOrder.getDataFineMisura() != null) {
					lNomProvvDAO.setCodNomeProvvedimento("NP028");
				} else if ((lPosMod.isLibero() && lMisModelOrder.getDataFineMisura() == null)
						|| lPosizioneGiu.equals("14") || lPosizioneGiu.equals("04")
						|| lPosizioneGiu.equals("53")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP027");
				} else if (lPosizioneGiu.equals("29")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP125");
				} else {
					lNomProvvDAO.setCodNomeProvvedimento("NPDD0");
				}
			}
			// else if (tipoMisura.equals("SEMILIBERTA"))
			// Controllo per semiliberta
			else if (lMisModelOrder.getCodTipoMisura().equals("0004")) {
				if ((lPosMod.isLibero() && lMisModelOrder.getDataFineMisura() == null)
						|| lPosizioneGiu.equals("04")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP030");

				} else if (lPosMod.isLibero() && lMisModelOrder.getDataFineMisura() != null) {
					lNomProvvDAO.setCodNomeProvvedimento("NP031");
				} else if (lPosizioneGiu.equals("03")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP032");
				} else {
					lNomProvvDAO.setCodNomeProvvedimento("NPSL0");
				}
			}
			// else if (tipoMisura.equals("AFFIDAMENTO"))
			// Controllo per affidamento in prova
			else if (lMisModelOrder.getCodTipoMisura().equals("0001")
					|| lMisModelOrder.getCodTipoMisura().equals("0002")
					|| lMisModelOrder.getCodTipoMisura().equals("0003")
					|| lMisModelOrder.getCodTipoMisura().equals("0030")) {
				if ((lPosMod.isLibero() && (lVerbMod == null || lVerbMod.getIdVerbale() == null))
						|| lPosizioneGiu.equals("04")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP023");
				} else if (lPosMod.isLibero() && (lVerbMod != null && lVerbMod.getIdVerbale() != null)) {
					lNomProvvDAO.setCodNomeProvvedimento("NP024");
				} else if (lPosizioneGiu.equals("03")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP025");
				} else if (lPosizioneGiu.equals("12") || (lPosizioneGiu.equals("14"))) {
					lNomProvvDAO.setCodNomeProvvedimento("NP026");
				} else if (lPosMod.getCodPosizioneGiuridica().equals("13")
						&& (IdEveAmmProvvAff != null && !"".equals(IdEveAmmProvvAff.toString()))) {
					lNomProvvDAO.setCodNomeProvvedimento("NP126");
				} else {
					lNomProvvDAO.setCodNomeProvvedimento("NPAF0");
				}
			}
			// else if (tipoMisura.equals("INDULTINO"))
			// Controllo per indultino
			else if (lMisModelOrder.getCodTipoMisura().equals("2245")) {
				if (lPoModelPrec != null && lPoModelPrec.getCodPosizioneGiuridica() != null
						&& (lPoModelPrec.isLibero()) && lMisModelOrder.getDataInizioMisura() != null
						&& lPosizioneGiu.equals("27")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP100");

				} else if (lPosMod.isLibero() && lMisModelOrder.getDataInizioMisura() == null) {
					lNomProvvDAO.setCodNomeProvvedimento("NP101");
				} else if ((lPosizioneGiu.equals("03") || lPosizioneGiu.equals("12")
						|| lPosizioneGiu.equals("04") || lPosizioneGiu.equals("14"))
						&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP102");
				} else if ((lPosizioneGiu.equals("03") || lPosizioneGiu.equals("12")
						|| lPosizioneGiu.equals("04") || lPosizioneGiu.equals("14"))
						&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP103");
				} else {
					lNomProvvDAO.setCodNomeProvvedimento("NP013");
				}
			}
			// 28/09/2010 Controllo per Espiazione Pena presso Domicilio
			else if (lMisModelOrder.getCodTipoMisura()
					.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM_MOTIVO)
					|| lMisModelOrder.getCodTipoMisura().equals("0610")) {
				if (lPoModelPrec != null && lPoModelPrec.getCodPosizioneGiuridica() != null
						&& (lPoModelPrec.isLibero()) && lMisModelOrder.getDataInizioMisura() != null
						&& lPosizioneGiu.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM_POS_GIU)) {
					lNomProvvDAO.setCodNomeProvvedimento("NP220");

				} else if (lPosMod.isLibero() && lMisModelOrder.getDataInizioMisura() == null) {
					lNomProvvDAO.setCodNomeProvvedimento("NP221");
				} else if ((lPosizioneGiu.equals("03") || lPosizioneGiu.equals("12")
						|| lPosizioneGiu.equals("04") || lPosizioneGiu.equals("14"))
						&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP222");
				} else if ((lPosizioneGiu.equals("03") || lPosizioneGiu.equals("12")
						|| lPosizioneGiu.equals("04") || lPosizioneGiu.equals("14"))
						&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP223");
				} else {
					lNomProvvDAO.setCodNomeProvvedimento("NP013");
				}
			}

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			// Vector lNotifiche = new Vector(lNotEveDao.getModels());

			// aggiorna il luogo detenzione LUOGO DETENZIONE MODIFICA DEL 08/03/2004
			lLuogoDAO = new LuogoDetenzioneDAO(lConn);
			lLuogoSql = new LuogoDetenzioneSqlDAO(lConn);

			LuogoDetenzioneModel lLuoDet = null;

			// if (tipoMisura.equals("DETENZIONE"))
			// Controllo per "DETENZIONE"
			if (lMisModelOrder.getCodTipoMisura().equals("0005")
					|| lMisModelOrder.getCodTipoMisura().equals("0010")
					|| lMisModelOrder.getCodTipoMisura().equals("0013")) {
				if (lMisModelOrder != null && lMisModelOrder.getDescrLuogoProva() != null) {
					lLuogoSql.ricercaLuogoDetenzioneByIdPosizione(lPosModelData.getIdPosizioneGiuridica());
					lLuoDet = (LuogoDetenzioneModel) lLuogoSql.getModelByKey();

					if (lLuoDet != null && lLuoDet.getIdLuogoDetenzione() != null) {
						lLuogoDAO.setCondizioneUpdate(lLuoDet.getIdLuogoDetenzione());
						lLuogoDAO.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
						lLuogoDAO.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
						lLuogoDAO.setDataAggiornamento(DateUtils.getSysDate());
						lLuogoDAO.setDataFineDetenzione(DateUtils.getSysDate());
						lLuogoDAO.update();
						lLuogoDAO.stop();
					}

					lLuogoDAO.setDataInserimento(DateUtils.getSysDate());
					lLuogoDAO.setDataInizioDetenzione(DateUtils.getSysDate());
					lLuogoDAO.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
					lLuogoDAO.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
					lLuogoDAO.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					lLuogoDAO.setAltroLuogo(lMisModelOrder.getDescrLuogoProva());

					if (lKeyPos != null) {
						lLuogoDAO.setPosGiuIdPosizioneGiuridica(lKeyPos);
					} else {
						lLuogoDAO.setPosGiuIdPosizioneGiuridica(lPosModelData.getIdPosizioneGiuridica());
					}

					lLuogoDAO.insert();
					lLuogoDAO.stop();
				}
			}

			// -------Si inserisce il documento nel campo BLOB--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// rollback(lConnBlob);

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMA : " + daoEx);
			// } catch (SQLException sqe) {
			// sqe.printStackTrace();
			//
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// rollback(lConn);
			// // rollback(lConnBlob);
			//
			// throw new F3BException("MisuraAternativaController.ExUpdateValidaMA : " + sqe);
		} catch (Exception ex) {
			ex.printStackTrace();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			// rollback(lConnBlob);

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMA : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDAO);
			cleanup(lNotEveDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lMiDAO);
			cleanup(lNomProvvDAO);
			cleanup(lVerSql);
			cleanup(lLuogoDAO);
			cleanup(lMisSqlDAO);
			cleanup(lLuogoSql);
			cleanup(lEveDaoMisAlt);
			cleanup(lEventoperAmmProvAffSqlDAO);
			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	} // FINE - ExUploadValidaMA

	/**
	 * ExUpdateValidaMASospProvv validazione documento Sosp Provv
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMASospProvv(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		// EventoModel lEveModelMDS = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;
		LuogoDetenzioneSqlDAO lLuogoSql = null;
		LuogoDetenzioneDAO lLuogoDao = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		MisuraAlternativaDAO lMiDAO = null;
		EventoDAO lEveDaoMisAlt = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();
			// 18/03/2011 Recupero Informazioni di aggiornamento.
			lEveModel.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lEveModel.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lEveModel.setDataAggiornamento(DateUtils.getSysDate());

			// cercaDecreto Sosp by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			// lMisSqlDAO.ricercaMisuraAlternativaByKey(aKeyMis);
			lMisSqlDAO.ricercaMisuraAlternativaByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento riferito alla misura alternativa modifica del 11-10-04 --dario
			if (lMisModelOrder != null && lMisModelOrder.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
				EventoModel lEveModelTDS = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelTDS != null && (lEveModelTDS.getFlagDocumentoRegistrato() == null
						|| lEveModelTDS.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelTDS.setFlagDocumentoRegistrato("S");
					lEveModelTDS.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelTDS.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelTDS.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelTDS);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			// SETTA LO STATO PROCEDIMENTO
			Date lData = null;
			EventoModel lModel = new EventoModel(lEveModel);
			if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")
					&& lMisModelOrder.getDataIngressoIstituto() != null) {
				lData = lMisModelOrder.getDataIngressoIstituto();
				lModel.setDataEmissione(lMisModelOrder.getDataIngressoIstituto());
			} else {
				lData = lEveModel.getDataEmissione();
			}

			String lStatoProcMod = null;
			if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {

				// if (tipoMisura != null && (tipoMisura.equals("AFFIDAMENTO") ||
				// tipoMisura.equals("DETENZIONE") || tipoMisura.equals("SEMILIBERTA")))
				// Per Tipo Misura "DETENZIONE"
				if (lMisModelOrder.getCodTipoMisura().equals("2149")
						|| lMisModelOrder.getCodTipoMisura().equals("2150")
						|| lMisModelOrder.getCodTipoMisura().equals("2151")
						|| lMisModelOrder.getCodTipoMisura().equals("2293")
						|| lMisModelOrder.getCodTipoMisura().equals("2153")
						// Per Tipo Misura "AFFIDAMENTO"
						|| lMisModelOrder.getCodTipoMisura().equals("2145")
						|| lMisModelOrder.getCodTipoMisura().equals("2146")
						|| lMisModelOrder.getCodTipoMisura().equals("2147")
						// Per Tipo Misura "SEMILIBERTA"
						|| lMisModelOrder.getCodTipoMisura().equals("2148")) {
					lStatoProcMod = "0035";
					// else if (tipoMisura != null && (tipoMisura.equals("AFFIDAMENTO51BIS") ||
					// tipoMisura.equals("DETENZIONE51BIS") || tipoMisura.equals("SEMILIBERTA51BIS")))
					// Per Tipo Misura "DETENZIONE51BIS"
				} else if (lMisModelOrder.getCodTipoMisura().equals("2284")
						|| lMisModelOrder.getCodTipoMisura().equals("2285")
						|| lMisModelOrder.getCodTipoMisura().equals("2288")
						|| lMisModelOrder.getCodTipoMisura().equals("2286")
						|| lMisModelOrder.getCodTipoMisura().equals("2287")
						// Per Tipo Misura "AFFIDAMENTO51BIS"
						|| lMisModelOrder.getCodTipoMisura().equals("2205")
						|| lMisModelOrder.getCodTipoMisura().equals("2281")
						|| lMisModelOrder.getCodTipoMisura().equals("2282")
						// Per Tipo Misura "SEMILIBERTA51BIS"
						|| lMisModelOrder.getCodTipoMisura().equals("2283")) {
					lStatoProcMod = "0099";
					// else if (tipoMisura != null && tipoMisura.equals("INDULTINO51BIS"))
					// Per Tipo Misura "INDULTINO51BIS"
				} else if (lMisModelOrder.getCodTipoMisura().equals("2290")) {
					lStatoProcMod = "0106";
					// else if (tipoMisura != null && tipoMisura.equals("INDULTINO"))
					// Per Tipo Misura "INDULTINO"
				} else if (lMisModelOrder.getCodTipoMisura().equals("2280")) {
					lStatoProcMod = "0088";
					// 10/11/2010 Per Tipo Misura "Sospensione Espiazione Pena presso Domicilio"
				} else if (lMisModelOrder.getCodTipoMisura()
						.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_MOTIVO)) {
					lStatoProcMod = "0396";
					// 10/11/2010 Per Tipo Misura "Sospensione Espiazione Pena presso Domicilio 51 bis"
				} else if (lMisModelOrder.getCodTipoMisura()
						.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_MOTIVO)) {
					lStatoProcMod = "0404";
				}

				// Oggetto Decreto COD_MOTIVO=2756 ==> POSIZIONE_GIURIDICA=62
				// Oggetto Decreto COD_MOTIVO=2741 ==> POSIZIONE_GIURIDICA=63
				// Oggetto Decreto COD_MOTIVO=2742 ==> POSIZIONE_GIURIDICA=64
				// Oggetto Decreto COD_MOTIVO=2743 ==> POSIZIONE_GIURIDICA=65
				// Inizio MAC 2016/10/21
				// Oggetto Decreto COD_MOTIVO=2291 ==> POSIZIONE_GIURIDICA=49
				// Fine MAC 2016/10/21
				if (lMisModelOrder.getCodTipoMisura().equals("2756")) {
					lStatoProcMod = "0529";
				} else if (lMisModelOrder.getCodTipoMisura().equals("2741")) {
					lStatoProcMod = "0531";
				} else if (lMisModelOrder.getCodTipoMisura().equals("2742")) {
					lStatoProcMod = "0533";
				} else if (lMisModelOrder.getCodTipoMisura().equals("2743")) {
					lStatoProcMod = "0535";
					// Inizio MAC 2016/10/21
					// Gestione codice MOTIVO_PROVVEDIMENTO = 2291
					// Il codice 2291 sostituisce i codici (2741,2742,2743,2756)
					// eliminati dalla base dati
				}
				// MAC 2017/04/01 Ripristinati i codici (2741,2742,2743,2756)
				// PEC m_dg.DOG07.01-10-2018.0030206.U (deve essere censito anche i codice 2291)
				else if (lMisModelOrder.getCodTipoMisura().equals("2291")) {
					lStatoProcMod = "0600";
				}
				// Fine MAC 2016/10/21
			} else if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				// if (tipoMisura != null && (tipoMisura.equals("AFFIDAMENTO") ||
				// tipoMisura.equals("DETENZIONE") || tipoMisura.equals("SEMILIBERTA")))
				// Per Tipo Misura "DETENZIONE"
				if (lMisModelOrder.getCodTipoMisura().equals("2149")
						|| lMisModelOrder.getCodTipoMisura().equals("2150")
						|| lMisModelOrder.getCodTipoMisura().equals("2151")
						|| lMisModelOrder.getCodTipoMisura().equals("2293")
						|| lMisModelOrder.getCodTipoMisura().equals("2153")
						// Per Tipo Misura "AFFIDAMENTO"
						|| lMisModelOrder.getCodTipoMisura().equals("2145")
						|| lMisModelOrder.getCodTipoMisura().equals("2146")
						|| lMisModelOrder.getCodTipoMisura().equals("2147")
						// Per Tipo Misura "SEMILIBERTA"
						|| lMisModelOrder.getCodTipoMisura().equals("2148")) {
					lStatoProcMod = "0034";
					// else if (tipoMisura != null && (tipoMisura.equals("AFFIDAMENTO51BIS") ||
					// tipoMisura.equals("DETENZIONE51BIS") || tipoMisura.equals("SEMILIBERTA51BIS")))
					// Per Tipo Misura "DETENZIONE51BIS"
				} else if (lMisModelOrder.getCodTipoMisura().equals("2284")
						|| lMisModelOrder.getCodTipoMisura().equals("2285")
						|| lMisModelOrder.getCodTipoMisura().equals("2288")
						|| lMisModelOrder.getCodTipoMisura().equals("2286")
						|| lMisModelOrder.getCodTipoMisura().equals("2287")
						// Per Tipo Misura "AFFIDAMENTO51BIS"
						|| lMisModelOrder.getCodTipoMisura().equals("2205")
						|| lMisModelOrder.getCodTipoMisura().equals("2281")
						|| lMisModelOrder.getCodTipoMisura().equals("2282")
						// Per Tipo Misura "SEMILIBERTA51BIS"
						|| lMisModelOrder.getCodTipoMisura().equals("2283")) {
					lStatoProcMod = "0098";
					// if (tipoMisura != null && tipoMisura.equals("INDULTINO51BIS"))
					// Per Tipo Misura "INDULTINO51BIS"
				} else if (lMisModelOrder.getCodTipoMisura().equals("2290")) {
					lStatoProcMod = "0105";

					// else if (tipoMisura != null && tipoMisura.equals("INDULTINO"))
				} else if (lMisModelOrder.getCodTipoMisura().equals("2280")) {
					lStatoProcMod = "0087";
					// 10/11/2010 Per Tipo Misura "Sospensione Espiazione Pena presso Domicilio"
				} else if (lMisModelOrder.getCodTipoMisura()
						.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_MOTIVO)) {
					lStatoProcMod = "0396";
					// 10/11/2010 Per Tipo Misura "Sospensione Espiazione Pena presso Domicilio 51 bis"
				} else if (lMisModelOrder.getCodTipoMisura()
						.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_MOTIVO)) {
					lStatoProcMod = "0404";
				}

				// Oggetto Decreto COD_MOTIVO=2756 ==> POSIZIONE_GIURIDICA=62
				// Oggetto Decreto COD_MOTIVO=2741 ==> POSIZIONE_GIURIDICA=63
				// Oggetto Decreto COD_MOTIVO=2742 ==> POSIZIONE_GIURIDICA=64
				// Oggetto Decreto COD_MOTIVO=2743 ==> POSIZIONE_GIURIDICA=65
				// Inizio MAC 2016/10/21
				// Oggetto Decreto COD_MOTIVO=2291 ==> POSIZIONE_GIURIDICA=49
				// Fine MAC 2016/10/21
				if (lMisModelOrder.getCodTipoMisura().equals("2756")) {
					lStatoProcMod = "0530";
				} else if (lMisModelOrder.getCodTipoMisura().equals("2741")) {
					lStatoProcMod = "0532";
				} else if (lMisModelOrder.getCodTipoMisura().equals("2742")) {
					lStatoProcMod = "0534";
				} else if (lMisModelOrder.getCodTipoMisura().equals("2743")) {
					lStatoProcMod = "0536";
					// Inizio MAC 2016/10/21
					// Gestione codice MOTIVO_PROVVEDIMENTO = 2291
					// Il codice 2291 sostituisce i codici (2741,2742,2743,2756)
					// eliminati dalla base dati
				}
				// MAC 2017/04/01 Ripristinati i codici (2741,2742,2743,2756)
				// PEC m_dg.DOG07.01-10-2018.0030206.U (deve essere censito anche i codice 2291)
				else if (lMisModelOrder.getCodTipoMisura().equals("2291")) {
					lStatoProcMod = "0601";
				}
				// Fine MAC 2016/10/21

			}

			if (lStatoProcMod != null) {
				InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lModel,
						lStatoProcMod);
			}

			// Aggiorna Inserisci PENA_RESIDUA
			// PenaResiduaModel lPenResMod = new PenaResiduaModel();
			/* lPenResMod = */InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep(), null);

			// cancellazione scadenzario misura alternativa
			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);

			ScadenzarioModel lScaMod = null;
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("13", aFascicolo.getIdFascicoloSiep());
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

			if (lScaMod != null) {
				lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
				lScaDao.delete();
				lScaDao.stop();
			}

			// Aggiorna POSIZIONE_GIURIDICA
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lLuogoSql = new LuogoDetenzioneSqlDAO(lConn);
			lLuogoDao = new LuogoDetenzioneDAO(lConn);
			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			String lPosizione = null;

			if (lMisModelOrder != null && lMisModelOrder.getCodTipoMisura() != null) {
				// if (tipoMisura.equals("DETENZIONE"))
				// Per Tipo Misura "DETENZIONE"
				if (lMisModelOrder.getCodTipoMisura().equals("2149")
						|| lMisModelOrder.getCodTipoMisura().equals("2150")
						|| lMisModelOrder.getCodTipoMisura().equals("2151")
						|| lMisModelOrder.getCodTipoMisura().equals("2293")
						|| lMisModelOrder.getCodTipoMisura().equals("2153")) {
					lPosizione = "31";
					// else if (tipoMisura.equals("AFFIDAMENTO"))
					// Per Tipo Misura "AFFIDAMENTO"
				} else if (lMisModelOrder.getCodTipoMisura().equals("2145")
						|| lMisModelOrder.getCodTipoMisura().equals("2146")
						|| lMisModelOrder.getCodTipoMisura().equals("2147")) {
					lPosizione = "32";
					// else if (tipoMisura.equals("SEMILIBERTA"))
					// Per Tipo Misura "SEMILIBERTA"
				} else if (lMisModelOrder.getCodTipoMisura().equals("2148")) {
					lPosizione = "33";
					// else if (tipoMisura.equals("AFFIDAMENTO51BIS"))
					// Per Tipo Misura "AFFIDAMENTO51BIS"
				} else if (lMisModelOrder.getCodTipoMisura().equals("2205")
						|| lMisModelOrder.getCodTipoMisura().equals("2281")
						|| lMisModelOrder.getCodTipoMisura().equals("2282")) {
					lPosizione = "37";
					// else if (tipoMisura.equals("SEMILIBERTA51BIS"))
					// Per Tipo Misura "SEMILIBERTA51BIS"
				} else if (lMisModelOrder.getCodTipoMisura().equals("2283")) {
					lPosizione = "38";
					// else if (tipoMisura.equals("DETENZIONE51BIS"))
					// Per Tipo Misura "DETENZIONE51BIS"
				}
				// Sospensione provvisoria Arresti Domiciliari ex art. 656 comma 10
				// la posizione giuridica cambia in funzione dell'Oggetto Decreto COD_MOTIVO
				// Oggetto Decreto COD_MOTIVO=2756 ==> POSIZIONE_GIURIDICA=62
				// Oggetto Decreto COD_MOTIVO=2741 ==> POSIZIONE_GIURIDICA=63
				// Oggetto Decreto COD_MOTIVO=2742 ==> POSIZIONE_GIURIDICA=64
				// Oggetto Decreto COD_MOTIVO=2743 ==> POSIZIONE_GIURIDICA=65
				// Inizio MAC 2016/10/21
				// Oggetto Decreto COD_MOTIVO=2291 ==> POSIZIONE_GIURIDICA=49
				// Fine MAC 2016/10/21
				else if (lMisModelOrder.getCodTipoMisura().equals("2756")) {
					lPosizione = "62";
				} else if (lMisModelOrder.getCodTipoMisura().equals("2741")) {
					lPosizione = "63";
				} else if (lMisModelOrder.getCodTipoMisura().equals("2742")) {
					lPosizione = "64";
				} else if (lMisModelOrder.getCodTipoMisura().equals("2743")) {
					lPosizione = "65";
					// Inizio MAC 2016/10/21
					// Gestione codice MOTIVO_PROVVEDIMENTO = 2291
					// Il codice 2291 sostituisce i codici (2741,2742,2743,2756)
					// eliminati dalla base dati
				}
				// MAC 2017/04/01 Ripristinati i codici (2741,2742,2743,2756)
				// PEC m_dg.DOG07.01-10-2018.0030206.U (deve essere censito anche i codice 2291)
				else if (lMisModelOrder.getCodTipoMisura().equals("2291")) {
					lStatoProcMod = "0601";
				}
				// Fine MAC 2016/10/21

				// Sospensione provvisoria Arresti Domiciliari ex art. 656 comma 10
				else if (lMisModelOrder.getCodTipoMisura().equals("2284")
						|| lMisModelOrder.getCodTipoMisura().equals("2285")
						|| lMisModelOrder.getCodTipoMisura().equals("2288")
						|| lMisModelOrder.getCodTipoMisura().equals("2286")
						|| lMisModelOrder.getCodTipoMisura().equals("2287")) {
					if (lMisModelOrder != null && lMisModelOrder.getCodTipoMisura() != null
							&& lMisModelOrder.getCodTipoMisura().equals("2287")) {
						lPosizione = "39";
					} else {
						lPosizione = "36";
					}
				}
				// else if (tipoMisura.equals("INDULTINO51BIS") || tipoMisura.equals("INDULTINO"))
				// Per Tipo Misura "INDULTINO51BIS" e "INDULTINO"
				else if (lMisModelOrder.getCodTipoMisura().equals("2290")
						|| lMisModelOrder.getCodTipoMisura().equals("2280")) {
					lPosizione = "35";
				}

				// 23/11/2010 Per Tipo Misura "Sospensione Espiazione Pena presso Domicilio" e
				// "Sospensione Espiazione Pena presso Domicilio 51 bis"
				else if (lMisModelOrder.getCodTipoMisura()
						.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_MOTIVO)) {
					lPosizione = ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_POS_GIU;
				} else if (lMisModelOrder.getCodTipoMisura()
						.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_MOTIVO)) {
					lPosizione = ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_POS_GIU;
				}
			}

			BigDecimal lKeyPos = null;
			lKeyPos = InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizione, lPosMod, lData, lEveModel,
					aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());

			// LUOGO DETENZIONE
			if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				LuogoDetenzioneModel lLuoDet = null;
				lLuogoSql.ricercaLuogoDetenzioneByIdPosizione(lPosMod.getIdPosizioneGiuridica());
				lLuoDet = (LuogoDetenzioneModel) lLuogoSql.getModelByKey();
				if (lLuoDet != null && lLuoDet.getIdLuogoDetenzione() != null) {
					lLuogoDao.setCondizioneUpdate(lLuoDet.getIdLuogoDetenzione());
					lLuogoDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
					lLuogoDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
					lLuogoDao.setDataAggiornamento(DateUtils.getSysDate());
					lLuogoDao.setDataFineDetenzione(DateUtils.getSysDate());
					lLuogoDao.update();
					lLuogoDao.stop();
				}

				// * Cerca le NOTIFICHE *
				lNotEveDao.ricercaNotificaByEvento(lEveModel.getIdEvento());
				Vector lNotifiche = new Vector(lNotEveDao.getModels());
				NotificaModel lNotMod = new NotificaModel();
				String lCodIstituto = null;

				Iterator iter = lNotifiche.iterator();
				while (iter.hasNext()) {
					lNotMod = (NotificaModel) iter.next();
					if (lNotMod != null && lNotMod.getIstDetIdIstitutoDetenzione() != null) {
						lCodIstituto = lNotMod.getIstDetIdIstitutoDetenzione();
					}
				}

				lLuogoDao.setIstDetIdIstitutoDetenzione(lCodIstituto);
				lLuogoDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lLuogoDao.setDataInserimento(DateUtils.getSysDate());
				lLuogoDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lLuogoDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lLuogoDao.setPosGiuIdPosizioneGiuridica(lKeyPos);
				lLuogoDao.setDataInizioDetenzione(DateUtils.getSysDate());
				lLuogoDao.insert();
				lLuogoDao.stop();
			}

			// Aggiorna tabella nome_provvedimento
			// lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			if (lMisModelOrder != null) {
				// if (tipoMisura != null && (tipoMisura.equals("AFFIDAMENTO") ||
				// tipoMisura.equals("DETENZIONE") || tipoMisura.equals("SEMILIBERTA")))
				// Per Tipo Misura "DETENZIONE"
				if (lMisModelOrder.getCodTipoMisura().equals("2149")
						|| lMisModelOrder.getCodTipoMisura().equals("2150")
						|| lMisModelOrder.getCodTipoMisura().equals("2151")
						|| lMisModelOrder.getCodTipoMisura().equals("2293")
						|| lMisModelOrder.getCodTipoMisura().equals("2153")
						// Per Tipo Misura "AFFIDAMENTO"
						|| lMisModelOrder.getCodTipoMisura().equals("2145")
						|| lMisModelOrder.getCodTipoMisura().equals("2146")
						|| lMisModelOrder.getCodTipoMisura().equals("2147")
						// Per Tipo Misura "SEMILIBERTA"
						|| lMisModelOrder.getCodTipoMisura().equals("2148")) {
					if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP034");
					} else if (lMisModelOrder != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP033");
					}
				}
				// else if (tipoMisura != null && (tipoMisura.equals("AFFIDAMENTO51BIS") ||
				// tipoMisura.equals("DETENZIONE51BIS") || tipoMisura.equals("SEMILIBERTA51BIS")))
				// Per Tipo Misura "DETENZIONE51BIS"
				else if (lMisModelOrder.getCodTipoMisura().equals("2284")
						|| lMisModelOrder.getCodTipoMisura().equals("2285")
						|| lMisModelOrder.getCodTipoMisura().equals("2288")
						|| lMisModelOrder.getCodTipoMisura().equals("2286")
						|| lMisModelOrder.getCodTipoMisura().equals("2287")
						// Per Tipo Misura "AFFIDAMENTO51BIS"
						|| lMisModelOrder.getCodTipoMisura().equals("2205")
						|| lMisModelOrder.getCodTipoMisura().equals("2281")
						|| lMisModelOrder.getCodTipoMisura().equals("2282")
						// Per Tipo Misura "SEMILIBERTA51BIS"
						|| lMisModelOrder.getCodTipoMisura().equals("2283")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP120");
				}
				// else if (tipoMisura != null && tipoMisura.equals("INDULTINO51BIS"))
				// Per Tipo Misura "INDULTINO51BIS"
				else if (lMisModelOrder.getCodTipoMisura().equals("2290")) {
					lNomProvvDAO.setCodNomeProvvedimento("NP125");
				}
				// else if (tipoMisura != null && tipoMisura.equals("INDULTINO"))
				else if (lMisModelOrder.getCodTipoMisura().equals("2280")) {
					if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP108");
					} else if (lMisModelOrder != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP107");
					}
				}
				// 10/11/2010 tipoMisura = "SOSP_ESP_PRESSO_DOM_MOTIVO"))
				else if (lMisModelOrder.getCodTipoMisura()
						.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_MOTIVO)) {
					if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP225");
					} else if (lMisModelOrder != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP224");
					}
				}
				// 23/11/2010 tipoMisura = "SOSP_ESP_PRESSO_DOM_51BIS_MOTIVO"))
				else if (lMisModelOrder.getCodTipoMisura()
						.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_MOTIVO)) {
					if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP231");
					} else if (lMisModelOrder != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP230");
					}
				}
				// Sospensione provvisoria arresti Domiciliari
				else if (lMisModelOrder != null && lMisModelOrder.getCodTipoMisura() != null
						&& (lMisModelOrder.getCodTipoMisura().equals("2756")
								|| lMisModelOrder.getCodTipoMisura().equals("2741")
								|| lMisModelOrder.getCodTipoMisura().equals("2742")
								|| lMisModelOrder.getCodTipoMisura().equals("2743")
								// Inizio MAC 2016/10/21
								// Gestione codice MOTIVO_PROVVEDIMENTO = 2291
								// Il codice 2291 sostituisce i codici (2741,2742,2743,2756)
								// eliminati dalla base dati
								// MAC 2017/04/01 Ripristinati i codici (2741,2742,2743,2756)
								// PEC m_dg.DOG07.01-10-2018.0030206.U (deve essere censito anche i codice
								// 2291)
								|| lMisModelOrder.getCodTipoMisura().equals("2291")))
				// Fine MAC 2016/10/21
				{ // impostiamo la posizione giuridica
					// per Sospensione provvisoria arresti
					// Domiciliari
					if (lMisModelOrder.getCodTipoMisura().equals("2756")) {
						lPosizione = "62";
					} else if (lMisModelOrder.getCodTipoMisura().equals("2741")) {
						lPosizione = "63";
					} else if (lMisModelOrder.getCodTipoMisura().equals("2742")) {
						lPosizione = "64";
					} else if (lMisModelOrder.getCodTipoMisura().equals("2743")) {
						lPosizione = "65";
						// Inizio MAC 2016/10/21
						// Gestione codice MOTIVO_PROVVEDIMENTO = 2291
						// Il codice 2291 sostituisce i codici (2741,2742,2743,2756)
						// eliminati dalla base dati
					}
					// MAC 2017/04/01 Ripristinati i codici (2741,2742,2743,2756)
					// PEC m_dg.DOG07.01-10-2018.0030206.U (deve essere censito anche i codice 2291)
					else if (lMisModelOrder.getCodTipoMisura().equals("2291")) {
						lPosizione = "49";
					}
					// Fine MAC 2016/10/21
					if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP301");
					} else if (lMisModelOrder != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP302");
					}

				}
				// Revoca Arresti Domiciliari e Rigetta applicazione misura alternativa
				else if (lMisModelOrder != null && lMisModelOrder.getCodTipoMisura() != null
						&& (lMisModelOrder.getCodTipoMisura().equals("2744")
								|| lMisModelOrder.getCodTipoMisura().equals("2757")
								|| lMisModelOrder.getCodTipoMisura().equals("2746")
								|| lMisModelOrder.getCodTipoMisura().equals("2747"))) {
					lPosizione = "3";
					if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP307");
					} else if (lMisModelOrder != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
							&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
						lNomProvvDAO.setCodNomeProvvedimento("NP308");
					}
				}
				// Ripristino Degli Arresti Domiciliari
				else if (lMisModelOrder != null && lMisModelOrder.getCodTipoMisura() != null
						&& (lMisModelOrder.getCodTipoMisura().equals("2748")
								|| lMisModelOrder.getCodTipoMisura().equals("2749")
								|| lMisModelOrder.getCodTipoMisura().equals("2758")
								|| lMisModelOrder.getCodTipoMisura().equals("2759")
								|| lMisModelOrder.getCodTipoMisura().equals("2752")
								|| lMisModelOrder.getCodTipoMisura().equals("2753")
								|| lMisModelOrder.getCodTipoMisura().equals("2754")
								|| lMisModelOrder.getCodTipoMisura().equals("2755"))) {
					if (lMisModelOrder.getCodTipoMisura().equals("2748")) {
						lPosizione = "62";
					} else if (lMisModelOrder.getCodTipoMisura().equals("2749")) {
						lPosizione = "67";
					} else if (lMisModelOrder.getCodTipoMisura().equals("2758")) {
						lPosizione = "64";
					} else if (lMisModelOrder.getCodTipoMisura().equals("2759")) {
						lPosizione = "69";
					} else if (lMisModelOrder.getCodTipoMisura().equals("2752")) {
						lPosizione = "4";
					} else if (lMisModelOrder.getCodTipoMisura().equals("2753")) {
						lPosizione = "63";
					} else if (lMisModelOrder.getCodTipoMisura().equals("2754")) {
						lPosizione = "68";
					} else if (lMisModelOrder.getCodTipoMisura().equals("2755")) {
						lPosizione = "65";
					}
					if (lMisModelOrder != null && lMisModelOrder.getCodTipoDecisione() != null
							&& lMisModelOrder.getCodTipoDecisione().equalsIgnoreCase("Decreto")) {
						if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
								&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
							lNomProvvDAO.setCodNomeProvvedimento("NP304");
						} else if (lMisModelOrder != null
								&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
								&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
							lNomProvvDAO.setCodNomeProvvedimento("NP303");
						}
					} else if (lMisModelOrder != null && lMisModelOrder.getCodTipoDecisione() != null
							&& lMisModelOrder.getCodTipoDecisione().equalsIgnoreCase("Ordinanza")) {
						if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null
								&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
							lNomProvvDAO.setCodNomeProvvedimento("NP306");
						} else if (lMisModelOrder != null
								&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
								&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
							lNomProvvDAO.setCodNomeProvvedimento("NP305");
						}
					}
				}

			}

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
			// commit(lConnBlob);

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMASospProvv : " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// rollback(lConn);
			// // rollback(lConnBlob);
			//
			// sqe.printStackTrace();
			//
			// throw new F3BException("MisuraAternativaController.ExUpdateValidaMASospProvv : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMASospProvv : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lNotEveDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lLuogoDao);
			cleanup(lLuogoSql);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);
			cleanup(lMiDAO);

			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	// upload revoca
	/**
	 * ExUpdateValidaMARevoca
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @param tipoMisura
	 * @param lKeyMis
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMARevoca(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;
		MisuraAlternativaDAO lMisDao = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		EventoDAO lEveDaoMisAlt = null;

		try {
			lConn = getDBTransaction();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("------------------->: lMisMDS.ExUpdateValidaMARevoca(): ");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("------------------->: aEvento: " + aEvento);

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();
			String Motivo = lEveModel.getCodMotivo();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("------------------->: Motivo: " + Motivo);

			// cercaDecreto Sosp
			lMisDao = new MisuraAlternativaDAO(lConn);
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
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
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("------------------->: lCodPosizione: " + lCodPosizione);

			// Aggiorna NOME_PROVVREDIMENTO

			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			if (Motivo.equals("0014") || Motivo.equals("0015") || Motivo.equals("0086")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP042");
			} else if (Motivo.equals("2270") || Motivo.equals("0016") || Motivo.equals("0087")
					|| Motivo.equals("0089") || Motivo.equals("0088")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP043");
			} else if (Motivo.equals("0091")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP044");
			} else if (Motivo.equals("0196")) // indultino
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP021");
			} else if (Motivo.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO) || // Espiazione
																									// Pena
																									// presso
																									// Domicilio
					Motivo.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO_DAUDS)) // 05/10/2011
																									// Espiazione
																									// Pena
																									// presso
																									// Domicilio
																									// da UDS
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP021");
			} else if (Motivo.equals("0000")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP045");
			}
			// La revoca degli arresti domiciliari puo' avvenire dopo la sospensione provvisoria, oppure,
			// direttamente.
			// Nel caso (revoca dopo sospensione) Posizione giuridica=62/63/64/65/49 in riferimento alle
			// Sospensioni a),b),c),d)
			// a) Sospensione provvisoria prosecuzione arresti domiciliari ex art. 656 comma 10 //Motivo=2756
			// b) Sospensione provvisoria prosecuzione Arresti Domiciliari ex art 89 dpr 309/90 //Motivo=2741
			// c) Sospensione provvisoria prosecuzione permanenza in casa ex art. 656 comma 10 //Motivo=2742
			// d) Sospensione provvisoria prosecuzione collocamento in comunita' ex art. 656 comma 10.
			// //Motivo=2743
			// Posizione giuridica
			if (lCodPosizione.equalsIgnoreCase("62") || lCodPosizione.equalsIgnoreCase("63")
					|| lCodPosizione.equalsIgnoreCase("64") || lCodPosizione.equalsIgnoreCase("65")
					// Inizio MAC 2016/10/21
					// Gestione codice MOTIVO_PROVVEDIMENTO = 2291
					// Il codice 2291 sostituisce i codici (2741,2742,2743,2756)
					// eliminati dalla base dati
					// MAC 2017/04/01 Ripristinati i codici (2741,2742,2743,2756)
					// PEC m_dg.DOG07.01-10-2018.0030206.U (deve essere censito anche i codice 2291)
					|| lCodPosizione.equalsIgnoreCase("49")) {
				// Fine MAC 2016/10/21
				// revocaDopoSospensioneProvvisoria = "dopoSospensione";
				lNomProvvDAO.setCodNomeProvvedimento("NP308");
			} else {
				// revocaDopoSospensioneProvvisoria = "direttamente";
				lNomProvvDAO.setCodNomeProvvedimento("NP307");
			}

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = null;

			if (Motivo.equals("0014") || Motivo.equals("0015") || Motivo.equals("0086")) {
				lStatoProcMod = "0053";
			} else if (Motivo.equals("0016") || Motivo.equals("0087") || Motivo.equals("0089")
			// 20170928: [SG] aggiunto motivo 0232 su segnalazione M.T.
					|| Motivo.equals("0088") || "0232".equals(Motivo)) {
				lStatoProcMod = "0054"; // Revoca Detenzione Domiciliare - Emesso Ordine Esecuzione
			} else if (Motivo.equals("2270")) // Revoca ammissione provvisoria alla detenzione domiciliare
			{
				lStatoProcMod = "0196"; // Disposta revoca applicazione provvisoria di misura alternativa in
										// data
			} else if (Motivo.equals("0091")) {
				lStatoProcMod = "0055";
			} else if (Motivo.equals("0196")) // indultino
			{
				lStatoProcMod = "0103";
			} else
			// 10/11/2010 Espiazione Pena presso Domicilio
			if (Motivo.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO)
					|| Motivo.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO_DAUDS)) { // 05/10/2011
				lStatoProcMod = "0401";
			} else if (Motivo.equals("0000")) {
				lStatoProcMod = "0056";
			} else if (Motivo.equals("2744")) {
				lStatoProcMod = "0545";
			} else if (Motivo.equals("2757")) {
				lStatoProcMod = "0547";
			} else if (Motivo.equals("2746")) {
				lStatoProcMod = "0546";
			} else if (Motivo.equals("2747")) {
				lStatoProcMod = "0548";
			}
			// 20170928: [SG] invertiti 0547 e 0546 su segnalazione M.T.

			if (lStatoProcMod != null) {
				InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
						lStatoProcMod);
			}

			String lRevocaCalcolo = null;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("------------------->: lRevocaCalcolo: " + lRevocaCalcolo);

			// Per Tipo Misura "INDULTINO" , "AFFIDAMENTO" e "ESPIAZIONE PENA PRESSO DOMICILIO"
			if (lMisMDS.getCodTipoMisura().equals("0196") || lMisMDS.getCodTipoMisura().equals("0014")
					|| lMisMDS.getCodTipoMisura().equals("0015") || lMisMDS.getCodTipoMisura()
							.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO)
					|| // Esp. presso Dom.
					lMisMDS.getCodTipoMisura()
							.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO_DAUDS)
					|| // 05/10/2011
						// Revoca Esp.
						// presso Dom.
						// da UDS
					lMisMDS.getCodTipoMisura().equals("0086")) {
				lRevocaCalcolo = "S";
			}
			// Aggiorna Inserisci PENA_RESIDUA

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("------------------->: lRevocaCalcolo: " + lRevocaCalcolo);

			PenaResiduaModel lPenResMod = new PenaResiduaModel();
			lPenResMod = InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep(), lRevocaCalcolo);

			// Aggiorna POSIZIONE_GIURIDICA

			// Per Tipo Misura "DETENZIONE", "SEMILIBERTA", "AFFIDAMENTO" e "INDULTINO"
			// detenzione
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger
					.error("------------------->: lMisMDS.getCodTipoMisura(): " + lMisMDS.getCodTipoMisura());

			if (((lMisMDS.getCodTipoMisura().equals("2270") || lMisMDS.getCodTipoMisura().equals("0016")
					|| lMisMDS.getCodTipoMisura().equals("0087") || lMisMDS.getCodTipoMisura().equals("0089")
					|| lMisMDS.getCodTipoMisura().equals("0088"))
					&& lCodPosizione != null
					&& (lCodPosizione.equals("31") || lCodPosizione.equals("36") || lCodPosizione.equals("12")
							|| lCodPosizione.equals("29")))
					// semiliberta'
					|| (lMisMDS.getCodTipoMisura().equals("0091") && lCodPosizione != null
							&& (lCodPosizione.equals("33") || lCodPosizione.equals("38")
									|| lCodPosizione.equals("14")))
					// affidamento
					|| ((lMisMDS.getCodTipoMisura().equals("0014")
							|| lMisMDS.getCodTipoMisura().equals("0015")
							|| lMisMDS.getCodTipoMisura().equals("0086")) && lCodPosizione != null
							&& (lCodPosizione.equals("32") || lCodPosizione.equals("37")
									|| lCodPosizione.equals("13")))
					// indultino
					|| (lMisMDS.getCodTipoMisura().equals("0196") && lCodPosizione != null
							&& (lCodPosizione.equals("35") || lCodPosizione.equals("40")
									|| lCodPosizione.equals("27")))
					// 05/10/2011 Espiazione pena presso domicilio
					|| ((lMisMDS.getCodTipoMisura().equals("2744")
							|| lMisMDS.getCodTipoMisura().equals("2757")
							|| lMisMDS.getCodTipoMisura().equals("2746")
							|| lMisMDS.getCodTipoMisura().equals("2747"))
							&& (lCodPosizione != null)
							&& (lCodPosizione.equals("62") || lCodPosizione.equals("63")
									|| lCodPosizione.equals("64") || lCodPosizione.equals("65")
									|| lCodPosizione.equals("70") || lCodPosizione.equals("71")
									|| lCodPosizione.equals("72") || lCodPosizione.equals("07")
									|| lCodPosizione.equals("04"))
							// Modifica 13/01/2017
							|| lCodPosizione.equals("49"))
					|| ((lMisMDS.getCodTipoMisura()
							.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO)
							|| lMisMDS.getCodTipoMisura()
									.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO_DAUDS))
							&& lCodPosizione != null && (lCodPosizione.equals("51")
									|| lCodPosizione.equals("52") || lCodPosizione.equals("50")))) {
				String lPosizione = null;
				// if (lCodPosizione.equals("13") || lCodPosizione.equals("27") || lCodPosizione.equals("50"))
				if (lCodPosizione.equals("13") || lCodPosizione.equals("27")) {
					// 15-07-2015 MEV 29 punto 4 - Revoca Affidamento inprova art 47 quater
					if (lMisMDS.getDataIngressoIstituto() != null)
						lPosizione = "03";
					else
						lPosizione = "10";
				} else if (lCodPosizione.equals("50") || lCodPosizione.equals("51")
						|| lCodPosizione.equals("52") || lCodPosizione.equals("53")) {
					if (lMisMDS.getCodTipoUfficioScarcerazione().compareTo("SORV") == 0) // selezionato flag
																							// detenuto in
																							// fase di
																							// inserimento
																							// revoca
						lPosizione = "03";
					else
						lPosizione = "10";
				} else if (lCodPosizione.equals("62") || lCodPosizione.equals("63")
						|| lCodPosizione.equals("64") || lCodPosizione.equals("65")
						|| lCodPosizione.equals("70") || lCodPosizione.equals("71")
						|| lCodPosizione.equals("72") || lCodPosizione.equals("07")
						|| lCodPosizione.equals("04")
						// Modifica 13/01/2017
						// Se la revoca degli arresti domiciliari avviene dopo la sospensione provvisoria,
						// e la posizione giuridica corrente e' 49, bisogna aggiornare la posizione giuridica
						// settandola a 03
						|| lCodPosizione.equals("49")) {
					lPosizione = "03";
				} else {
					// 20/12/2010 Se il soggetto e' non detenuto la posizione giuridica e' = LIBERO.
					// Altrimenti la posizione giuridica e' = Espiazione pena in regime carcerario.
					if (lMisMDS.getDataIngressoIstituto() != null)
						lPosizione = "03";
					else
						lPosizione = "10";
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("------------------->: lPosizione " + lPosizione);

				InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizione, lPosMod,
						lEveModel.getDataEmissione(), lEveModel, aFascicolo.getIdFascicoloSiep(),
						aEvento.getIdEvento());
			}

			// cancellazione SCADENZARIO Misura Alternativa

			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);

			ScadenzarioModel lScaMod = null;
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("13", aFascicolo.getIdFascicoloSiep());
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
			if (lScaMod != null) {
				lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
				lScaDao.delete();
				lScaDao.stop();
			}

			// Data fine Misura Alternativa
			// per tipo Misura "AFFIDAMENTO" e "INDULTINO" // 10/11/2010 e "Esp. presso Dom"
			if (((lMisMDS.getCodTipoMisura().equals("0014") || lMisMDS.getCodTipoMisura().equals("0015")
					|| lMisMDS.getCodTipoMisura().equals("0086")) && lMisMDS != null
					&& lMisMDS.getDataInizioMisura() != null)
					|| (lMisMDS.getCodTipoMisura().equals("0196") && lMisMDS != null
							&& lMisMDS.getDataInizioMisura() != null)
					|| (lMisMDS.getCodTipoMisura().equals("0316") && lMisMDS != null
							&& lMisMDS.getDataInizioMisura() != null)) {

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("------------------->: getDataFine ");

				if (lPenResMod != null && lPenResMod.getDataFine() != null) {
					lMisDao.setDAOFromModelForUpdate(lMisMDS);
					lMisDao.setDataFineMisura(lPenResMod.getDataFine());
					lMisDao.update();
					lMisDao.stop();
				}
			}

			// ** Aggiorna SCADENZARIO FINE PENA**

			// per tipo Misura "AFFIDAMENTO" e "INDULTINO" // 10/11/2010 e "Esp. presso Dom" 05/10/2011 e
			// "Esp. presso Dom. da UDS"
			if (((lMisMDS.getCodTipoMisura().equals("0014") || lMisMDS.getCodTipoMisura().equals("0015")
					|| lMisMDS.getCodTipoMisura().equals("0086")) && lCodPosizione != null
					&& (lCodPosizione.equals("32") || lCodPosizione.equals("37")))
					|| (lMisMDS.getCodTipoMisura().equals("0196") && lCodPosizione != null
							&& (lCodPosizione.equals("35") || lCodPosizione.equals("40")))
					|| ((lMisMDS.getCodTipoMisura()
							.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO)
							|| lMisMDS.getCodTipoMisura()
									.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO_DAUDS))
							&& lCodPosizione != null
							&& (lCodPosizione.equals("51") || lCodPosizione.equals("52")))) {
				if (lPenResMod != null && lPenResMod.getDataInizio() != null
						&& lPenResMod.getDataFine() != null) {
					InserimentoAggiornamentoScadenzarioFinePena(lConn, lPenResMod, lEveModel,
							lPenResMod.getDataFine(), aFascicolo.getIdFascicoloSiep());
				}
			}

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());

			// aggiorna le notifiche
			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// commit(lConnBlob);

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMARevoca : " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// rollback(lConn);
			// // rollback(lConnBlob);
			//
			// sqe.printStackTrace();
			//
			// throw new F3BException("MisuraAternativaController.ExUpdateValidaMARevoca : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMARevoca : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lNotEveDao);
			cleanup(lNomProvvDAO);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lMisDao);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);
			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	// ***************************************
	// 13.12.2010 AGGIUNTO DANIELA : Inizio
	// ***************************************
	// upload cessazione
	/**
	 * Metodo per la validazione della Cessazione di una mIsura Alternativa n.b. gestisce la validazione della
	 * cessazione per tutte le misure.
	 */
	public EventoModel ExUpdateValidaMACessazione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;
		MisuraAlternativaDAO lMisDao = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		EventoDAO lEveDaoMisAlt = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();
			String Motivo = lEveModel.getCodMotivo();

			// cercaDecreto Sosp
			lMisDao = new MisuraAlternativaDAO(lConn);
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisMDS = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento (ordinanza/decreto) riferito alla misura alternativa modifica del 11-10-04
			// --dario
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
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// Aggiorna NOME_PROVVREDIMENTO
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP045");

			if (Motivo.equals("0167") || Motivo.equals("0166") || Motivo.equals("0168") // TDS Attuali
			// ||Motivo.equals("5430") || Motivo.equals("5431") || Motivo.equals("5432") // new MDS DL
			// 146/2013
					|| Motivo.equals("5450") || Motivo.equals("5451") || Motivo.equals("5452") // new TDS DL
																								// 146/2013 su
																								// reclamo PM
			) { // Affidamento
				lNomProvvDAO.setCodNomeProvvedimento("NP042");
			} else if (Motivo.equals("0024") || Motivo.equals("0110") || Motivo.equals("0112")
					|| Motivo.equals("0111") // TDS attuale
					// || Motivo.equals("5433") || Motivo.equals("5434") || Motivo.equals("5435") ||
					// Motivo.equals("5436") || Motivo.equals("5439") // new MDS DL 146/2013
					|| Motivo.equals("5453") || Motivo.equals("5454") || Motivo.equals("5455")
					|| Motivo.equals("5456") || Motivo.equals("5459") // new TDS DL 146/2013 su reclamo PM
			) { // Detenzione Domiciliare
				lNomProvvDAO.setCodNomeProvvedimento("NP043");
			} else if (Motivo.equals("0169") // TDS
					// || Motivo.equals("5437") // new MDS DL 146/2013
					|| Motivo.equals("5457") // new TDS DL 146/2013 su reclamo PM
			) { // Semiliberta'
				lNomProvvDAO.setCodNomeProvvedimento("NP044");
			} else if (Motivo.equals("0172")) // indultino
			{ //
				lNomProvvDAO.setCodNomeProvvedimento("NP021");
			} else if (Motivo.equals("0363") || Motivo.equals("5458")) // Espiazione Pena presso Domicilio
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP021");
			} else if (Motivo.equals("0000")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP045");
			}

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = null;

			if (Motivo.equals("0167") || Motivo.equals("0166") || Motivo.equals("0168")
			// || Motivo.equals("5430") || Motivo.equals("5431") || Motivo.equals("5432") // new MDS DL
			// 146/2013
					|| Motivo.equals("5450") || Motivo.equals("5451") || Motivo.equals("5452") // new TDS DL
																								// 146/2013 su
																								// reclamo PM
			) { // Affidamento
				lStatoProcMod = "0500"; // Cessazione Affidamento in Prova - Emesso Ordine di Esecuzione
			} else if (Motivo.equals("0024") || Motivo.equals("0110") || Motivo.equals("0112")
					|| Motivo.equals("0111")
					// || Motivo.equals("5433") || Motivo.equals("5434") || Motivo.equals("5435") ||
					// Motivo.equals("5436") || Motivo.equals("5439")// new MDS DL 146/2013
					|| Motivo.equals("5453") || Motivo.equals("5454") || Motivo.equals("5455")
					|| Motivo.equals("5456") || Motivo.equals("5459") // new TDS DL 146/2013 su reclamo PM
			) {
				lStatoProcMod = "0501"; // Cessazione Detenzione Domiciliare - Emesso Ordine Esecuzione
			} else if (Motivo.equals("0169")
					// || Motivo.equals("5437") // new MDS DL 146/2013
					|| Motivo.equals("5457") // new TDS DL 146/2013 su reclamo PM
			) {
				lStatoProcMod = "0502"; // Cessazione Semiliberta' - Emesso Ordine Esecuzione
			} else if (Motivo.equals("0172")) // indultino
			{
				lStatoProcMod = "0504"; // Cessazione L.207/2003 - Emesso Ordine di Esecuzione
			} else
			// 10/11/2010 Espiazione Pena presso Domicilio
			if (Motivo.equals(ICostantiMisuraAlternativa.CESSAZIONE_ESP_PRESSO_DOM_MOTIVO)
					|| Motivo.equals("5458")) {
				lStatoProcMod = "0506"; // Cessazione Esec.Pena presso Dom. - Emesso Ordine di Esecuzione
			} else if (Motivo.equals("0000")) {
				lStatoProcMod = "0503"; // Cessazione Misura Alternativa - Emesso Ordine Esecuzione
			}

			if (lStatoProcMod != null) {
				aEvento.setDataEmissione(lEveModel.getDataEmissione());
				InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), aEvento,
						lStatoProcMod);
			}

			String lRevocaCalcolo = null;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger
					.error("------------------->: lMisMDS.getCodTipoMisura(): " + lMisMDS.getCodTipoMisura());

			// Per Tipo Misura "INDULTINO" e "AFFIDAMENTO"
			if (
			// lMisMDS.getCodTipoMisura().equals("0172") // Idultino
			// || lMisMDS.getCodTipoMisura().equals("0363") || lMisMDS.getCodTipoMisura().equals("5499") //
			// Esp. presso Dom.
			// || lMisMDS.getCodTipoMisura().equals("0167") || lMisMDS.getCodTipoMisura().equals("0166") ||
			// lMisMDS.getCodTipoMisura().equals("0168") // AFFIDAMENTO
			Motivo.equals("0172") // Idultino
					|| Motivo.equals("0363") || Motivo.equals("5458") || Motivo.equals("0167")
					|| Motivo.equals("0166") || Motivo.equals("0168") || Motivo.equals("5430")
					|| Motivo.equals("5431") || Motivo.equals("5432") // new MDS DL 146/2013
					|| Motivo.equals("5450") || Motivo.equals("5451") || Motivo.equals("5452") // new TDS DL
																								// 146/2013 su
																								// reclamo PM

			) {
				lRevocaCalcolo = "S";
			}

			// ======================================
			// Aggiorna Inserisci PENA_RESIDUA
			// ======================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("------------------->: lRevocaCalcolo: " + lRevocaCalcolo);

			PenaResiduaModel lPenResMod = new PenaResiduaModel();
			lPenResMod = InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep(), lRevocaCalcolo);

			// ======================================
			// Aggiorna POSIZIONE_GIURIDICA
			// ======================================
			// Per Tipo Misura "DETENZIONE", "SEMILIBERTA", "AFFIDAMENTO" e "INDULTINO" E
			// "ESEC_PRESSO_DOMICILIO
			// si gestisce il cambio posizione giuridica SOLO se quella di partenza e' coerente con la misra
			// che si
			// sta cessando, ovvero se il condannato era in esecuzione (anche privvisoria) o in sosponsione
			// (cautelativa o provvisoria)
			// String[] lCodiciDetenzione = {"0024","0110","0112","0111"};
			List<String> lCodiciDetenzione = Arrays.asList("0024", "0110", "0112", "0111");
			List<String> lCodiciDetenzioneMDS51Bis = Arrays.asList("5453", "5454", "5455", "5456", "5459",
					"5433", "5434", "5435", "5436", "5439");

			if (((lCodiciDetenzione.contains(Motivo) || lCodiciDetenzioneMDS51Bis.contains(Motivo))
					&& (lCodPosizione.equals("31") || lCodPosizione.equals("36") || lCodPosizione.equals("12")
							|| lCodPosizione.equals("29")))
					// semiliberta'
					|| ((Motivo.equals("0169") || Motivo.equals("5457") || Motivo.equals("5437"))
							&& lCodPosizione != null && (lCodPosizione.equals("33")
									|| lCodPosizione.equals("38") || lCodPosizione.equals("14")))
					// affidamento
					|| ((Motivo.equals("0167") || Motivo.equals("0166") || Motivo.equals("0168")
							|| Motivo.equals("5430") || Motivo.equals("5431") || Motivo.equals("5432") // new
																										// MDS
																										// DL
																										// 146/2013
							|| Motivo.equals("5450") || Motivo.equals("5451") || Motivo.equals("5452") // new
																										// TDS
																										// DL
																										// 146/2013
																										// su
																										// reclamo
																										// PM
					) && lCodPosizione != null && (lCodPosizione.equals("32") || lCodPosizione.equals("37")
							|| lCodPosizione.equals("13") || lCodPosizione.equals("54")))
					// indultino
					|| (lMisMDS.getCodTipoMisura().equals("0172") && lCodPosizione != null
							&& (lCodPosizione.equals("35") || lCodPosizione.equals("40")
									|| lCodPosizione.equals("27")))
					// 04/11/2010 Espiazione pena presso domicilio
					|| ((lMisMDS.getCodTipoMisura()
							.equals(ICostantiMisuraAlternativa.CESSAZIONE_ESP_PRESSO_DOM_MOTIVO)
							|| lMisMDS.getCodTipoMisura().equals("5458")) && lCodPosizione != null
							&& (lCodPosizione.equals("51") || lCodPosizione.equals("52")
									|| lCodPosizione.equals("50")))) {

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiornamento posizione giuridica");

				String lPosizione = null;
				// n.b. Detenzione Domiciliare/Semiliberta'/Det Dom Termine non prevedono
				// il ricalcolo della pena ne l'inidcazione se detenuto o non detenuto
				// (lMisMDS.getCodTipoUfficioScarcerazione()='-')
				// if (lCodPosizione.equals("13") || lCodPosizione.equals("27") || lCodPosizione.equals("50"))

				// 13 - Espiazione Pena in Regime di Affidamento in Prova
				if (lCodPosizione.equals("13")) {
					// Michele: se in misura la Posizione Giuridica deve passare sempre in 03
					// indipendentemente se esegue PROC o SORV
					lPosizione = "03";
				} else if (lCodPosizione.equals("27")) // 27 - Sospensione Pena ex L. 207/03 (indultino)
				{
					lPosizione = "10";
					// FIXME perche' libero ?? Se eseguito contro soggetto detenuto (SORV) andrebbe messo 03 -
					// Detenuto
				} else if (lCodPosizione.equals("51") || lCodPosizione.equals("52")
						|| lCodPosizione.equals("53")) { // 51, 52,53 Esecuzione presso il domicilio IN
															// SOSPENSIONE
					if (lMisMDS.getCodTipoUfficioScarcerazione().compareTo("SORV") == 0) // selezionato flag
																							// detenuto in
																							// fase di
																							// inserimento
																							// revoca
						lPosizione = "03";
					else
						lPosizione = "10"; // FIXME perche' se in sospensione passa libero?
				} else if (lCodPosizione.equals("12") || lCodPosizione.equals("14")
						|| lCodPosizione.equals("50")) {
					// 12 - Espiazione Pena in Regime di Detenzione Domiciliare
					// 14 - Espiazione Pena in Regime di Semiliberta'
					// 50 - Esecuzione presso domicilio della pena detentiva
					lPosizione = "03";
				} else if (lCodPosizione.equals("31") || lCodPosizione.equals("36")) { // Detenzione
																						// domiciliare: in
																						// sospensione di
																						// misura
					// 31 - Sosp Cautelativa Det Dom
					// 36 - Sosp. Provvisoria Det Dom
					lPosizione = "03";
				} else { // In tutti gli altri casi verifico la presenza della data ingresso
							// istituto che viene valorizzata solo per (AFFI, INDULTINO, ESEC PRE DOM)
							// e solo se !isLibero
							// 20/12/2010 Se il soggetto e' non detenuto la posizione giuridica e' = LIBERO.
							// Altrimenti la posizione giuridica e' = Espiazione pena in regime carcerario.
					if (lMisMDS.getDataIngressoIstituto() != null)
						lPosizione = "03";
					else
						lPosizione = "10";
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("------------------->: lPosizione " + lPosizione);

				InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizione, lPosMod,
						lEveModel.getDataEmissione(), aEvento, aFascicolo.getIdFascicoloSiep(),
						aEvento.getIdEvento());
			}

			// ================================================
			// cancellazione SCADENZARIO Misura Alternativa
			// ================================================
			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);

			ScadenzarioModel lScaMod = null;
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("13", aFascicolo.getIdFascicoloSiep());
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
			if (lScaMod != null) {
				lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
				lScaDao.delete();
				lScaDao.stop();
			}

			// ================================================
			// Data fine Misura Alternativa
			// per tipo Misura "AFFIDAMENTO" e "INDULTINO" e "ESEC_DOM"
			if (((Motivo.equals("0167") || Motivo.equals("0166") || Motivo.equals("0168")
					|| Motivo.equals("5430") || Motivo.equals("5431") || Motivo.equals("5432") // new MDS DL
																								// 146/2013
					|| Motivo.equals("5450") || Motivo.equals("5451") || Motivo.equals("5452"))
					&& lMisMDS != null && lMisMDS.getDataInizioMisura() != null)
					|| (lMisMDS.getCodTipoMisura().equals("0172") && lMisMDS != null
							&& lMisMDS.getDataInizioMisura() != null) // indultino
					|| ((lMisMDS.getCodTipoMisura()
							.equals(ICostantiMisuraAlternativa.CESSAZIONE_ESP_PRESSO_DOM_MOTIVO)
							|| lMisMDS.getCodTipoMisura().equals("5458")) && lCodPosizione != null
							&& (lCodPosizione.equals("51") || lCodPosizione.equals("52")))) {

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("------------------->: Data fine Misura Alternativa: ");

				if (lPenResMod != null && lPenResMod.getDataFine() != null) {
					lMisDao.setDAOFromModelForUpdate(lMisMDS);
					lMisDao.setDataFineMisura(lPenResMod.getDataFine());
					lMisDao.update();
					lMisDao.stop();
				}
			}

			// ** Aggiorna SCADENZARIO FINE PENA**
			// per tipo Misura "AFFIDAMENTO" e "INDULTINO" e "ESEC_DOM
			if (((Motivo.equals("0167") || Motivo.equals("0166") || Motivo.equals("0168")
					|| Motivo.equals("5430") || Motivo.equals("5431") || Motivo.equals("5432") // new MDS DL
																								// 146/2013
					|| Motivo.equals("5450") || Motivo.equals("5451") || Motivo.equals("5452"))
					&& lCodPosizione != null && (lCodPosizione.equals("32") || lCodPosizione.equals("37")))
					|| (lMisMDS.getCodTipoMisura().equals("0172") && lCodPosizione != null
							&& (lCodPosizione.equals("35") || lCodPosizione.equals("40")))
					|| ((lMisMDS.getCodTipoMisura()
							.equals(ICostantiMisuraAlternativa.CESSAZIONE_ESP_PRESSO_DOM_MOTIVO)
							|| lMisMDS.getCodTipoMisura().equals("5458")) && lCodPosizione != null
							&& (lCodPosizione.equals("51") || lCodPosizione.equals("52")))) {
				if (lPenResMod != null && lPenResMod.getDataInizio() != null
						&& lPenResMod.getDataFine() != null) {
					InserimentoAggiornamentoScadenzarioFinePena(lConn, lPenResMod, lEveModel,
							lPenResMod.getDataFine(), aFascicolo.getIdFascicoloSiep());
				}
			}

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());

			// aggiorna le notifiche
			// ------- EVENTO--------
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			commit(lConnBlob);

			commit(lConn);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);

			rollback(lConn);
			rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMACessazione : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lNotEveDao);
			cleanup(lNomProvvDAO);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lMisDao);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);
			cleanup(lConn);

			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * Funzione di validazione dei provvedimenti a seguito della cessazione 51bis disposta del MDS DL 146/2013
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMACessazione51bisMDS(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;
		Connection lConnBlob = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		EventoDAO lEveDaoBlob = null;
		MisuraAlternativaDAO lMisDao = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		EventoDAO lEveDaoMisAlt = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();
			String Motivo = lEveModel.getCodMotivo();

			// Recupera la MA di cessazione
			lMisDao = new MisuraAlternativaDAO(lConn);
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisMDS = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento (ordinanza/decreto) riferito alla misura alternativa se
			// non validata quindi inserita SIEP
			if (lMisMDS != null && lMisMDS.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisMDS.getEveIdEvento());
				EventoModel lEveModelMis = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelMis != null && (lEveModelMis.getFlagDocumentoRegistrato() == null
						|| lEveModelMis.getFlagDocumentoRegistrato().equals("N"))) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Procedo alla validazione anche del provvedimento della Sorveglianza");
					lEveModelMis.setFlagDocumentoRegistrato("S");

					lEveModelMis.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelMis.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelMis.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelMis);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// ===============================
			// Stabilisco il tipo di misura
			// ===============================
			List<String> lCodiciAffidamento = Arrays.asList("2281", "2282", "2205");
			List<String> lCodiciDetenzione = Arrays.asList("2284", "2285", "2286", "2287", "2288"); // 2286 =
																									// Det.
																									// Dom.
																									// Termine
			List<String> lCodiciSemiliberta = Arrays.asList("2283");
			List<String> lCodiciIndultino = Arrays.asList("2290");
			List<String> lCodiciEspPreDom = Arrays.asList("2299");

			String lTipoMisura = null;
			if (lCodiciAffidamento.contains(lMisMDS.getCodTipoMisura())) {
				lTipoMisura = "AFFIDAMENTO";
			} else if (lCodiciDetenzione.contains(lMisMDS.getCodTipoMisura())) {
				lTipoMisura = "DETENZIONE";
			} else if (lCodiciSemiliberta.contains(lMisMDS.getCodTipoMisura())) {
				lTipoMisura = "SEMILIBERTA";
			} else if (lCodiciIndultino.contains(lMisMDS.getCodTipoMisura())) {
				lTipoMisura = "INDULTINO";
			} else if (lCodiciEspPreDom.contains(lMisMDS.getCodTipoMisura())) {
				lTipoMisura = "ESP_PRESSO_DOM";
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lTipoMisura = " + lTipoMisura);

			// ===============================
			// Aggiorna NOME_PROVVREDIMENTO
			// ===============================
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP045");

			if (lTipoMisura.equals("AFFIDAMENTO")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP042");
			} else if (lTipoMisura.equals("DETENZIONE")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP043");
			} else if (lTipoMisura.equals("SEMILIBERTA")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP044");
			} else if (lTipoMisura.equals("INDULTINO")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP021");
			} else if (lTipoMisura.equals("ESP_PRESSO_DOM")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP021");
			} else if (Motivo.equals("0000")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP045");
			}

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// =============================
			// SETTA LO STATO PROCEDIMENTO
			// =============================
			String lStatoProcMod = null;

			if (lTipoMisura.equals("AFFIDAMENTO")) {
				if (lEveModel.getCodTipoProvvedimento().equals("06"))
					lStatoProcMod = "0500"; // Cessazione Affidamento in Prova - Emesso Ordine di Esecuzione
				else if (lEveModel.getCodTipoProvvedimento().equals("12"))
					lStatoProcMod = "0524"; // Comunicazione Cessazione Affidamento in Prova
			} else if (lTipoMisura.equals("DETENZIONE")) {
				if (lEveModel.getCodTipoProvvedimento().equals("06"))
					lStatoProcMod = "0501"; // Cessazione Detenzione Domiciliare - Emesso Ordine Esecuzione
				else if (lEveModel.getCodTipoProvvedimento().equals("12"))
					lStatoProcMod = "0525"; // Comunicazione Cessazione Detenzione Domiciliare
			} else if (lTipoMisura.equals("SEMILIBERTA")) {
				if (lEveModel.getCodTipoProvvedimento().equals("06"))
					lStatoProcMod = "0502"; // Cessazione Semiliberta' - Emesso Ordine Esecuzione
				else if (lEveModel.getCodTipoProvvedimento().equals("12"))
					lStatoProcMod = "0526"; // Comunicazione Cessazione Semiliberta'
			} else if (lTipoMisura.equals("INDULTINO")) {
				if (lEveModel.getCodTipoProvvedimento().equals("06"))
					lStatoProcMod = "0504"; // Cessazione L.207/2003 - Emesso Ordine di Esecuzione
				else if (lEveModel.getCodTipoProvvedimento().equals("12"))
					lStatoProcMod = "0527"; // Comunicazione Cessazione L.207/2003
			} else if (lTipoMisura.equals("ESP_PRESSO_DOM")) {
				if (lEveModel.getCodTipoProvvedimento().equals("06"))
					lStatoProcMod = "0506"; // Cessazione Esec.Pena presso Dom. - Emesso Ordine di Esecuzione
				else if (lEveModel.getCodTipoProvvedimento().equals("12"))
					lStatoProcMod = "0528"; // Comunicazione Cessazione Esec. Pena presso Dom.
			} else if (Motivo.equals("0000")) {
				lStatoProcMod = "0503"; // Cessazione Misura Alternativa - Emesso Ordine Esecuzione
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Stato Procedimento: " + lStatoProcMod);
			if (lStatoProcMod != null) {
				aEvento.setDataEmissione(lEveModel.getDataEmissione());
				InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), aEvento,
						lStatoProcMod);
			}

			// ======================================
			// Aggiorna Inserisci PENA_RESIDUA
			// ======================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento Pena Residua");
			// ---> Forza l'update della pena passata in input. La pena viene sempre inserita
			// in quanto ricalcolata
			// PenaResiduaModel lPenResMod = new PenaResiduaModel();
			// lPenResMod = InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
			// aFascicolo.getIdFascicoloSiep(),"S");
			PenaResiduaModel lPenResMod = null;

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

			// Recupero la PR legata all'evento, inserita in fase di inserimento
			// dell'evento
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEvento.getIdEvento());
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			lPenResDao = new PenaResiduaDAO(lConn);
			if (lPenResMod != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trovata PR agganciata ad evento la valido...");
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());

				lPenResDao.setFlagValidato("S");

				lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPenResDao.setDataAggiornamento(DateUtils.getSysDate());

				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nessuna PR agganciata all'evento, recupero l'ultima a sistema...");
				// NON dovrebbe mai accadere. Una PR viene sempre legata all'evento di
				// cessazione essendo un evento di pena Iniziale. Faccio comunque una copia
				// della pena piu' recente
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

				if ("S".equals(lPenResMod.getFlagValidato())) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Ultima PR a sistema gia' validata, vado in copia");
					// Gia' validata, vado in copia
					lPenResDao.setDAOFromModel(lPenResMod);

					lPenResDao.setFlagValidato("S");
					lPenResDao.setEveIdEvento(lEveModel.getIdEvento());

					lPenResDao.setDataInserimento(DateUtils.getSysDate());
					lPenResDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lPenResDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

					lPenResDao.insert();
					lPenResDao.stop();
				} else {
					// Non validata la aggancio e valido
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Ultima PR a sistema non validata, vado in validazione e aggancio");
					lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());

					lPenResDao.setFlagValidato("S");
					lPenResDao.setEveIdEvento(lEveModel.getIdEvento());

					lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lPenResDao.setDataAggiornamento(DateUtils.getSysDate());

					lPenResDao.selByKey();
					lPenResDao.update();
					lPenResDao.stop();
				}
			}

			// ======================================
			// Aggiorna POSIZIONE_GIURIDICA
			// ======================================
			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// Se libero resta Libero
			// Se detenuto resta detenuto
			// Se in misura passa detenuto
			// In tutti gli altri casi la posizione non cambia
			String lNuovaPosizione = null;
			if ((lTipoMisura.equals("AFFIDAMENTO")
					&& (lCodPosizione.equals("13") || lCodPosizione.equals("54")))
					|| (lTipoMisura.equals("DETENZIONE") && (lCodPosizione.equals("12")
							|| lCodPosizione.equals("25") || lCodPosizione.equals("29")))
					|| (lTipoMisura.equals("SEMILIBERTA") && lCodPosizione.equals("14"))
					|| (lTipoMisura.equals("INDULTINO") && lCodPosizione.equals("27"))
					|| (lTipoMisura.equals("ESP_PRESSO_DOM") && lCodPosizione.equals("50"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiorno Posizione Giuridica");
				lNuovaPosizione = "03";

				// Aggiorno/Inserisco
				InserimentoAggiornamentoPosizioneGiuridica(lConn, lNuovaPosizione, lPosMod,
						lEveModel.getDataEmissione(), aEvento, aFascicolo.getIdFascicoloSiep(),
						aEvento.getIdEvento());

			} else {
				// La posizione non viene aggiornata
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Posizione Giuridica NON aggiornata");
			}

			// ================================================
			// Cancellazione SCADENZARIO Fine Misura Alternativa
			// ================================================
			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);

			ScadenzarioModel lScaMod = null; // "13" Scadenzario fine misura
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("13", aFascicolo.getIdFascicoloSiep());
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
			if (lScaMod != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancella scadenzario Fine Misura con id: " + lScaMod.getIdScadenzario());

				lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
				lScaDao.delete();
				lScaDao.stop();
			}

			// ================================================
			// Data fine Misura Alternativa
			// In fase di inserimento della Cessazione, se la Misura era in corso
			// e' stata inserita la DataInizioMisura nella MA di cessazione.
			// per tipo Misura "AFFIDAMENTO" e "INDULTINO" e "ESEC_DOM"
			// if ( ( ( Motivo.equals("0167") || Motivo.equals("0166") || Motivo.equals("0168")
			// || Motivo.equals("5430") || Motivo.equals("5431") || Motivo.equals("5432") // new MDS DL
			// 146/2013
			// || Motivo.equals("5450") || Motivo.equals("5451") || Motivo.equals("5452")
			// )
			// && lMisMDS != null
			// && lMisMDS.getDataInizioMisura() != null
			// )
			// || (lMisMDS.getCodTipoMisura().equals("0172") && lMisMDS != null &&
			// lMisMDS.getDataInizioMisura() != null) // indultino
			// || ( (
			// lMisMDS.getCodTipoMisura().equals(ICostantiMisuraAlternativa.CESSAZIONE_ESP_PRESSO_DOM_MOTIVO)
			// ||lMisMDS.getCodTipoMisura().equals("5499")
			// )
			// && lCodPosizione != null && (lCodPosizione.equals("51") || lCodPosizione.equals("52")))
			// )
			// {
			//
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("------------------->: Data fine Misura Alternativa: " );
			//
			// if (lPenResMod != null && lPenResMod.getDataFine() != null)
			// {
			// lMisDao.setDAOFromModelForUpdate(lMisMDS);
			// lMisDao.setDataFineMisura(lPenResMod.getDataFine());
			// lMisDao.update();
			// lMisDao.stop();
			// }
			// }

			// ** Aggiorna SCADENZARIO FINE PENA**
			// per tipo Misura "AFFIDAMENTO" e "INDULTINO" e "ESEC_DOM se in sospensione
			// if ( ( ( Motivo.equals("0167") || Motivo.equals("0166") || Motivo.equals("0168")
			// || Motivo.equals("5430") || Motivo.equals("5431") || Motivo.equals("5432") // new MDS DL
			// 146/2013
			// || Motivo.equals("5450") || Motivo.equals("5451") || Motivo.equals("5452"))
			// && lCodPosizione != null
			// && (lCodPosizione.equals("32") || lCodPosizione.equals("37"))
			// )
			// || (lMisMDS.getCodTipoMisura().equals("0172") && lCodPosizione != null &&
			// (lCodPosizione.equals("35") || lCodPosizione.equals("40")))
			// || ( (
			// lMisMDS.getCodTipoMisura().equals(ICostantiMisuraAlternativa.CESSAZIONE_ESP_PRESSO_DOM_MOTIVO)
			// || lMisMDS.getCodTipoMisura().equals("5499")
			// )
			// && lCodPosizione != null && (lCodPosizione.equals("51") || lCodPosizione.equals("52")))
			// )
			// {
			if (lPenResMod != null && lPenResMod.getDataInizio() != null
					&& lPenResMod.getDataFine() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiorno scadenzario fine pena");
				InserimentoAggiornamentoScadenzarioFinePena(lConn, lPenResMod, lEveModel,
						lPenResMod.getDataFine(), aFascicolo.getIdFascicoloSiep());
			}
			// }

			// aggiorna le notifiche
			// ------- EVENTO--------
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			commit(lConnBlob);

			commit(lConn);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);

			rollback(lConn);
			rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMACessazione51bisMDS : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lMisDao);
			cleanup(lMisSqlDAO);
			cleanup(lEveDaoMisAlt);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);

			cleanup(lConn);

			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	// 13.12.2010 Fine AGGIUNTO DANIELA
	/**
	 * ExUpdateValidaMARipristino validazione documento Ripristino
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMARipristino(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDaoMisAlt = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		// EventoModel lEveModelMDS = null;

		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;
		MisuraAlternativaDAO lMiDAO = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// cerca Ordinanza Ripristino by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento riferito alla misura alternativa modifica del 11-10-04 --dario
			if (lMisModelOrder != null && lMisModelOrder.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
				EventoModel lEveModelTDS = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelTDS != null && (lEveModelTDS.getFlagDocumentoRegistrato() == null
						|| lEveModelTDS.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelTDS.setFlagDocumentoRegistrato("S");
					lEveModelTDS.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelTDS.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelTDS.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelTDS);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = null;

			if (lCodPosizione.equals("31") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
				lStatoProcMod = "0046";
			} else if (lCodPosizione.equals("31") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lStatoProcMod = "0038";
			} else if (lCodPosizione.equals("32") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
				lStatoProcMod = "0045";
			} else if (lCodPosizione.equals("32") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lStatoProcMod = "0037";
			} else if (lCodPosizione.equals("33") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
				lStatoProcMod = "0047";
			} else if (lCodPosizione.equals("33") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lStatoProcMod = "0030";
			} else {
				lStatoProcMod = "0040";
			}

			if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2748")) {
				lStatoProcMod = "0537";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2749")) {
				lStatoProcMod = "0538";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2758")) {
				lStatoProcMod = "0539";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2759")) {
				lStatoProcMod = "0540";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2752")) {
				lStatoProcMod = "0541";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2753")) {
				lStatoProcMod = "0542";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2754")) {
				lStatoProcMod = "0543";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2755")) {
				lStatoProcMod = "0544";
			}

			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					lStatoProcMod);

			// Aggiorna Inserisci PENA_RESIDUA
			PenaResiduaModel lPenResMod = new PenaResiduaModel();
			lPenResMod = InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep(), null);

			// scadenzario MISURA ALTERNATIVA
			Date lData = new Date();

			if (lMisModelOrder != null && lMisModelOrder.getCodTipoUfficioScarcerazione() != null) {
				if (lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
					lData = lEveModel.getDataEmissione();
				}
				if (lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
					lData = lMisModelOrder.getDataScarcerazione();
				}
			}

			// aggiorna scadenzario MISURA ALTERNATIVA
			if (lData != null) {
				InserimentoAggiornamentoScadenzarioMisuraAlternativa(lConn, lData, lPenResMod, lEveModel,
						aFascicolo.getIdFascicoloSiep());
			}

			// aggiorna data fine misura
			lMiDAO = new MisuraAlternativaDAO(lConn);
			if (lPosMod != null && !lPosMod.isLibero()) {
				lMiDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
				lMiDAO.setDataFineMisura(lPenResMod.getDataFine());
				lMiDAO.update();
				lMiDAO.stop();
			}

			// Aggiorna POSIZIONE_GIURIDICA
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			String lAggInsPosGiu = "N";
			String lPosGiu = null;

			if (lCodPosizione.equals("31")) {
				lPosGiu = "12";
				lAggInsPosGiu = "S";
			} else if (lCodPosizione.equals("32")) {
				lPosGiu = "13";
				lAggInsPosGiu = "S";
			} else if (lCodPosizione.equals("33")) {
				lPosGiu = "14";
				lAggInsPosGiu = "S";
			}
			// 24/11/2010 Ripristino Espiazione Pena presso Domicilio.
			else if (lCodPosizione.equals("10") || lCodPosizione.equals("51") || lCodPosizione.equals("52")) {
				lPosGiu = "50";
				lAggInsPosGiu = "S";
			}
			// else if (lCodPosizione.equals("62") ||
			// lCodPosizione.equals("63") ||
			// lCodPosizione.equals("64") ||
			// lCodPosizione.equals("65"))
			// {
			// lPosGiu = "04";
			// lAggInsPosGiu = "S";
			// }

			if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2748")) {
				lPosGiu = "04";
				lAggInsPosGiu = "S";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2749")) {
				lPosGiu = "67";
				lAggInsPosGiu = "S";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2758")) {
				// MAC 2017/04/01
				// Modifica del 04/04/2017 (richiesta da Michele)
				// La posizione giuridica dopo il Ripristino deve essere:
				// "Permanenza in Casa ex art. 656 comma 10" (cod. 82)
				// lPosGiu = "68";
				lPosGiu = "82";
				lAggInsPosGiu = "S";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2759")) {
				// MAC 2017/04/01
				// Modifica del 04/04/2017 (richiesta da Michele)
				// La posizione giuridica dopo il Ripristino deve essere:
				// "Collocamento in Comunita'  ex art. 656 comma 10 cpp" (cod. 83)
				// lPosGiu = "69";
				lPosGiu = "83";
				lAggInsPosGiu = "S";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2752")) {
				lPosGiu = "04";
				lAggInsPosGiu = "S";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2753")) {
				lPosGiu = "67";
				lAggInsPosGiu = "S";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2754")) {
				// MAC 2017/04/01
				// Modifica del 04/04/2017 (richiesta da Michele)
				// La posizione giuridica dopo il Ripristino deve essere:
				// "Permanenza in Casa ex art. 656 comma 10" (cod. 82)
				// lPosGiu = "68";
				lPosGiu = "82";
				lAggInsPosGiu = "S";
			} else if (lMisModelOrder.getCodTipoMisura().equalsIgnoreCase("2755")) {
				// MAC 2017/04/01
				// Modifica del 04/04/2017 (richiesta da Michele)
				// La posizione giuridica dopo il Ripristino deve essere:
				// "Collocamento in Comunita'  ex art. 656 comma 10 cpp" (cod. 83)
				// lPosGiu = "69";
				lPosGiu = "83";
				lAggInsPosGiu = "S";
			}

			if (lAggInsPosGiu.equals("S")) {
				InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosGiu, lPosMod, lData, lEveModel,
						aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());
			}

			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			if (lCodPosizione.equals("31") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP036");
			} else if (lCodPosizione.equals("31") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP040");
			} else if (lCodPosizione.equals("32") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP035");
			} else if (lCodPosizione.equals("32") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP039");
			} else if (lCodPosizione.equals("33") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP037");
			} else if (lCodPosizione.equals("33") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP041");
			} else {
				lNomProvvDAO.setCodNomeProvvedimento("NP038");
			}

			// TipoProvvedimento= "ORDINANZA(03)/DECRETO(02)";
			if (lEveModel.getCodTipoProvvedimento().equals("D") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP303");
			} else if (lEveModel.getCodTipoProvvedimento().equals("D") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP304");
			} else if (lEveModel.getCodTipoProvvedimento().equals("O") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("SORV")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP305");
			} else if (lEveModel.getCodTipoProvvedimento().equals("O") && lMisModelOrder != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione() != null
					&& lMisModelOrder.getCodTipoUfficioScarcerazione().equals("PROC")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP306");
			}

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			// Vector lNotifiche = new Vector(lNotEveDao.getModels());

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// commit(lConnBlob);

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMARipristino : " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// rollback(lConn);
			// // rollback(lConnBlob);
			//
			// sqe.printStackTrace();
			//
			// throw new F3BException("MisuraAternativaController.ExUpdateValidaMARipristino : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMARipristino : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lNotEveDao);
			cleanup(lEveDaoMisAlt);
			cleanup(lNomProvvDAO);
			cleanup(lMisSqlDAO);
			cleanup(lMiDAO);
			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * ExRicercaMisuraAlternativaSospesaCorrenteByIdFascicolo
	 *
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public MisuraAlternativaModel ExRicercaMisuraAlternativaSospesaCorrenteByIdFascicolo(
			BigDecimal aKeyFascicolo) throws F3BException {

		Connection lConn = null;

		MisuraAlternativaSqlDAO lMisDao = null;

		MisuraAlternativaModel lMisMod = null;

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraAlternativaSqlDAO(lConn);

			lMisDao.ricercaMisuraAlternativaSospesaCorrenteByIdFascicolo(aKeyFascicolo);

			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraAlternativaController.ExRicercaMisuraAlternativaSospesaCorrenteByIdFascicolo: "
							+ daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException(
			// "MisuraAlternativaController.ExRicercaMisuraAlternativaSospesaCorrenteByIdFascicolo: "
			// + sqe);
		} finally {
			cleanup(lMisDao);

			cleanup(lConn);
		}

		return lMisMod;
	}

	/**
	 * ExUpdateValidaMADetDomTemp
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @param tipoMisura
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMADetDomTemp(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		// MisuraAlternativaDAO lMisDAO = null;
		LuogoDetenzioneDAO lLuogoDAO = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		EventoDAO lEveDaoMisAlt = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			String lMotivo = null;
			if (lEveModel != null && lEveModel.getCodMotivo() != null)
				lMotivo = lEveModel.getCodMotivo();

			lEveMod.setDataEmissione(lEveModel.getDataEmissione());

			// cerca Misura Alternariva by Evento
			lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdEvento(lEveModel.getEveIdEvento());
			MisuraAlternativaModel lMisModelOrder = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

			// Valido l'evento riferito alla misura alternativa modifica del 11-10-04 --dario
			if (lMisModelOrder != null && lMisModelOrder.getIdMisuraAlternativa() != null) {
				lEveSqlDao.ricercaEventoByKey(lMisModelOrder.getEveIdEvento());
				EventoModel lEveModelTDS = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDaoMisAlt = new EventoDAO(lConn);

				if (lEveModelTDS != null && (lEveModelTDS.getFlagDocumentoRegistrato() == null
						|| lEveModelTDS.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelTDS.setFlagDocumentoRegistrato("S");
					lEveModelTDS.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelTDS.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelTDS.setDataAggiornamento(DateUtils.getSysDate());

					lEveDaoMisAlt.setDAOFromModelForUpdate(lEveModelTDS);
					lEveDaoMisAlt.update();
					lEveDaoMisAlt.stop();
				}
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosizioneGiuridicaByIdFascicoloDesc(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = null;
			String lCodPosizione = null;

			// Cerca POSIZIONE_GIURIDICA precedente e attuale
			PosizioneGiuridicaModel lPoModelPrec = new PosizioneGiuridicaModel();
			Vector lPosizioni = new Vector();
			lPosizioni = new Vector(lPosSqlDao.getModels());
			if (lPosizioni.size() > 0) {
				lPosMod = (PosizioneGiuridicaModel) lPosizioni.get(0);
				lCodPosizione = lPosMod.getCodPosizioneGiuridica();
				if (lPosizioni.size() > 1) {
					lPoModelPrec = (PosizioneGiuridicaModel) lPosizioni.get(1);
				}
			}

			// Aggiorna Inserisci PENA_RESIDUA
			// PenaResiduaModel lPenResMod = new PenaResiduaModel();
			/* lPenResMod = */InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveMod,
					aFascicolo.getIdFascicoloSiep(), null);

			// AGGIORNA MISURA ALTERNATIVA
			// lMisDAO = new MisuraAlternativaDAO(lConn);
			/*
			 * if (!"PROROGAPROVVISORIA".equals(tipoMisura) && lCodPosizione.equals("12") && lPoModelPrec !=
			 * null && lPoModelPrec.isLibero() && lMisModelOrder != null && lMisModelOrder.getDataFineMisura()
			 * != null) { lMisDAO.setCondizioneUpdate(lMisModelOrder.getIdMisuraAlternativa());
			 * lMisDAO.setDataFineMisura(lPenResMod.getDataFine()); lMisDAO.update(); lMisDAO.stop();
			 * lMisModelOrder.setDataFineMisura(lPenResMod.getDataFine());
			 *
			 * }
			 */

			// SETTA LO STATO PROCEDIMENTO
			if (lMotivo != null) {
				String lStatoProc = null;
				if (lMotivo.equals("2340")) {
					lStatoProc = "0160";
				} else if ((lMotivo.equals("0011") && lCodPosizione.equals("12")) || lMotivo.equals("0197")) {
					lStatoProc = "0134";
				} else if (lMotivo.equals("0011") || lMotivo.equals("0000")) {
					lStatoProc = "0069";
				}

				if (lStatoProc != null)
					InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveMod,
							lStatoProc);
			}

			// ** Aggiorna SCADENZARIO**
			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			if (lMotivo.equals("2340") && lMisModelOrder.getDataScadenzaProroga() != null) {
				lMisModelOrder.setDataFineMisura(lMisModelOrder.getDataScadenzaProroga());
			}

			if (lMisModelOrder != null && lMisModelOrder.getDataFineMisura() != null && !lPosMod.isLibero()) {
				Date lData = lMisModelOrder.getDataFineMisura();
				ScadenzarioModel lScaMod = null;

				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("14",
						aFascicolo.getIdFascicoloSiep());
				lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

				if (lScaMod != null) {
					lScaDao.setDataFineScadenza(lData);
					lScaDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
					lScaDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
					lScaDao.setDataAggiornamento(DateUtils.getSysDate());
					lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDao.update();
					lScaDao.stop();
				} else if (lMisModelOrder.getDataInizioMisura() != null) {
					// inserisce scadenzario
					ScadenzarioModel lScaModel = new ScadenzarioModel();
					lScaModel.setCodTipoScadenzario("14");
					lScaModel.setDataInizioScadenza(lMisModelOrder.getDataInizioMisura());
					lScaModel.setDataFineScadenza(lData);

					lScaModel.setCodOperatoreInserimento(lEveMod.getCodOperatoreAggiornamento());
					lScaModel.setDataInserimento(DateUtils.getSysDate());
					lScaModel.setCodUfficioInserimento(lEveMod.getCodUfficioAggiornamento());
					lScaModel.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

					lScaDao.setDAOFromModel(lScaModel);
					lScaDao.insert();
					lScaDao.stop();
				}
			}

			// Aggiorna POSIZIONE_GIURIDICA
			Date lData = null;
			if (lPosMod.getCodPosizioneGiuridica().equals("03") // detenuto
					|| (!lMotivo.equals("2340") && lPosMod.getCodPosizioneGiuridica().equals("13"))
					|| (!lMotivo.equals("2340") && lPosMod.getCodPosizioneGiuridica().equals("54"))
					|| (!lMotivo.equals("2340") && lPosMod.getCodPosizioneGiuridica().equals("29"))
					|| (!lMotivo.equals("2340") && lPosMod.getCodPosizioneGiuridica().equals("14"))) // semiliberta'
			{
				if (lMisModelOrder.getDataScarcerazione() != null)
					lData = lMisModelOrder.getDataScarcerazione();
				else
					lData = lEveModel.getDataEmissione();
			} else if (lPoModelPrec != null && lPoModelPrec.isLibero() && lCodPosizione.equals("12") // libero
																										// con
																										// inizio
																										// misura
					&& (lMisModelOrder != null && lMisModelOrder.getDataInizioMisura() != null)) {
				lData = lEveModel.getDataEmissione();
			}

			if (lData != null) {
				InserimentoAggiornamentoPosizioneGiuridica(lConn, "12", lPosMod, lData, lEveMod,
						aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());
			}

			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			if (lMotivo.equals("2340")) {
				if (lCodPosizione.equals("03") || lCodPosizione.equals("12"))
					lNomProvvDAO.setCodNomeProvvedimento("NP127");
				else
					lNomProvvDAO.setCodNomeProvvedimento("NPDT0");
			} else {
				if (lPosMod.isLibero() && lMisModelOrder.getDataInizioMisura() == null) // libero
					lNomProvvDAO.setCodNomeProvvedimento("NP064");
				else if (lPoModelPrec != null && lPoModelPrec.isLibero() && lCodPosizione.equals("12") // libero
																										// con
																										// inizio
																										// misura
						&& (lMisModelOrder != null && lMisModelOrder.getDataInizioMisura() != null)) {
					lNomProvvDAO.setCodNomeProvvedimento("NP065");
				} else if (lCodPosizione.equals("03") || lCodPosizione.equals("14")
						|| lCodPosizione.equals("13") || lCodPosizione.equals("29")
						|| (lCodPosizione.equals("12") && lMotivo.equals("0011")))
					lNomProvvDAO.setCodNomeProvvedimento("NP066");
				else if (lCodPosizione.equals("12") && lMotivo.equals("0197"))
					lNomProvvDAO.setCodNomeProvvedimento("NP070");
				else
					lNomProvvDAO.setCodNomeProvvedimento("NPDT0");
			}

			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			// Vector lNotifiche = new Vector(lNotEveDao.getModels());

			// aggiorna il luogo detenzione
			if (lMisModelOrder != null && lMisModelOrder.getDescrLuogoProva() != null) {
				lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

				PosizioneGiuridicaModel lPosModCorr = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

				lLuogoDAO = new LuogoDetenzioneDAO(lConn);
				lLuogoDAO.setDataInserimento(aEvento.getDataAggiornamento());
				lLuogoDAO.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lLuogoDAO.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lLuogoDAO.setAltroLuogo(lMisModelOrder.getDescrLuogoProva());
				lLuogoDAO.setCondizioneIdPosizioneGiuridica(lPosModCorr.getIdPosizioneGiuridica());
				lLuogoDAO.insert();
				lLuogoDAO.stop();
			}

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMADetDomTemp : " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// rollback(lConn);
			// // rollback(lConnBlob);
			//
			// sqe.printStackTrace();
			//
			// throw new F3BException("MisuraAternativaController.ExUpdateValidaMADetDomTemp : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaMADetDomTemp : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lNomProvvDAO);
			cleanup(lLuogoDAO);
			cleanup(lMisSqlDAO);
			cleanup(lScaDao);
			cleanup(lEveDaoMisAlt);
			cleanup(lScaSqlDao);
			// cleanup(lMisDAO);

			cleanup(lConn);
			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	// validazione documento Perdita Efficacia
	/**
	 * ExUpdateValidaMAPerditaEfficacia
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaMAPerditaEfficacia(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		// EventoModel lEveModelMDS = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisSql = null;
		RefertoScarcerazioneSqlDAO lRefScaSql = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;
		EventoDAO lEveDaoMisAlt = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

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

			// referto scarcerazione
			lRefScaSql = new RefertoScarcerazioneSqlDAO(lConn);
			lRefScaSql.ricercaRefertoScarcerazioneByEveIdEvento(lEveModel.getEveIdEvento());
			RefertoScarcerazioneModel lRefScaMod = (RefertoScarcerazioneModel) lRefScaSql.getModelByKey();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = null;
			if (lRefScaMod != null && lRefScaMod.getIdRefertoScarcerazione() != null) {
				lStatoProcMod = "0077";
			} else if (lMisMod != null && lMisMod.getIdMisuraAlternativa() != null) {
				lStatoProcMod = "0036";
			}

			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
					lStatoProcMod);

			// Aggiorna Inserisci PENA_RESIDUA
			PenaResiduaModel lPenResMod = new PenaResiduaModel();
			lPenResMod = InserimentoAggiornamentoPenResMisuraAlternativa(lConn, lEveModel,
					aFascicolo.getIdFascicoloSiep(), null);

			// gestione data
			Date lData = null;

			if (lRefScaMod != null && lRefScaMod.getIdRefertoScarcerazione() != null) {
				lData = lRefScaMod.getDataScarcerazione();
			} else if (lMisMod != null && lMisMod.getIdMisuraAlternativa() != null) {
				lData = lMisMod.getDataDecisione();
			}

			// aggiorna scadenzario MISURA ALTERNATIVA
			if (lData != null)
				InserimentoAggiornamentoScadenzarioMisuraAlternativa(lConn, lData, lPenResMod, lEveModel,
						aFascicolo.getIdFascicoloSiep());

			// Aggiorna POSIZIONE_GIURIDICA 23/11//2010
			String lPosizione = null;
			if (lCodPosizione.equals("31") || lCodPosizione.equals("32") || lCodPosizione.equals("33")
					|| lCodPosizione.equals("35")
					|| lCodPosizione.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_POS_GIU)
					|| lCodPosizione.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_POS_GIU)) {
				if (lCodPosizione.equals("31")) {
					lPosizione = "12";
				} else if (lCodPosizione.equals("32")) {
					lPosizione = "13";
				} else if (lCodPosizione.equals("33")) {
					lPosizione = "14";

				} else if (lCodPosizione.equals("35")) {
					lPosizione = "27";
				}
				// 23/11/2010 Ripristino Posizione Giuridica ESP_PRESSO_DOM_POS_GIU.
				else if (lCodPosizione.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_POS_GIU)
						|| lCodPosizione
								.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_POS_GIU)) {
					lPosizione = ICostantiMisuraAlternativa.ESP_PRESSO_DOM_POS_GIU;
				}

				InserimentoAggiornamentoPosizioneGiuridica(lConn, lPosizione, lPosMod, lData, lEveModel,
						aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento());
			}

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(lEveModel.getIdEvento());
			// Vector lNotifiche = new Vector(lNotEveDao.getModels());

			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			if (lEveModel.getCodMotivo().equals("2160") || lEveModel.getCodMotivo().equals("2161")
					|| lEveModel.getCodMotivo().equals("2162")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP097");
			}

			else if (lEveModel.getCodMotivo().equals("2164") || lEveModel.getCodMotivo().equals("2165")
					|| lEveModel.getCodMotivo().equals("2166") || lEveModel.getCodMotivo().equals("2167"))

			// else if (tipoMisura.equals("DETENZIONE"))
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP098");
			} else if (lEveModel.getCodMotivo().equals("2163")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP099");
			} else if (lEveModel.getCodMotivo().equals("2289")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP111");
			}
			// 09/11/2010 Espiazione pena presso domicilio
			else if (lEveModel.getCodMotivo()
					.equals(ICostantiMisuraAlternativa.PEREFF_ESP_PRESSO_DOM_MOTIVO)) {
				lNomProvvDAO.setCodNomeProvvedimento("NP228");
			}

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

			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);

			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaPerditaEfficacia : " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			//
			// rollback(lConn);
			// // rollback(lConnBlob);
			//
			// sqe.printStackTrace();
			//
			// throw new F3BException("MisuraAternativaController.ExUpdateValidaPerditaEfficacia : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);

			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("MisuraAternativaController.ExUpdateValidaPerditaEfficacia : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lNotEveDao);
			cleanup(lNomProvvDAO);
			cleanup(lMisSql);
			cleanup(lRefScaSql);
			cleanup(lEveDaoMisAlt);
			cleanup(lConn);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * Restituisce l'elenco delle MisureAlternativa e EventiSIUS (decretiOrdinanze) validati.
	 */
	public List<MisuraAlternativaEventoModel> ExRicercaMisureAlternativeEventiOrderDesc(
			BigDecimal aIdFascicolo, String[] aCodTipoDecisione, String[] aCodNaturaDecisione,
			String[] aCodTipoMisura) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveDao = null;
		MisuraAlternativaSqlDAO lMisSqlDao = null;

		List lMisureAlternative = null;
		List lListaMisureEventi = new ArrayList();

		try {
			lConn = getDBConnection();

			lMisSqlDao = new MisuraAlternativaSqlDAO(lConn);

			lMisSqlDao.ricercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisioneOrderDescData(
					aIdFascicolo, aCodTipoDecisione, aCodNaturaDecisione, aCodTipoMisura);

			lMisureAlternative = new ArrayList(lMisSqlDao.getModels());

			Iterator lItx = lMisureAlternative.iterator();

			lEveDao = new EventoSqlDAO(lConn);
			while (lItx.hasNext()) {
				MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lItx.next();

				lEveDao.ricercaEventoByKey(lMisMod.getEveIdEvento());
				EventoModel lEvento = (EventoModel) lEveDao.getModelByKey();

				if (lEvento != null && lEvento.getFlagDocumentoRegistrato() != null
						&& !"A".equals(lEvento.getFlagDocumentoRegistrato())
						&& !"N".equals(lEvento.getFlagDocumentoRegistrato())) {
					MisuraAlternativaEventoModel lMisEve = new MisuraAlternativaEventoModel();

					lMisEve.setEvento(lEvento);
					lMisEve.setMisuraAlternativa(lMisMod);
					lListaMisureEventi.add(lMisEve);
				}
			}

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);

			throw new F3BException(
					"MisuraAlternativaController.ExRicercaMisureAlternativeEventiOrderDesc: " + daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.error("SQLException: " + sqe);
			// throw new
			// F3BException("MisuraAlternativaController.ExRicercaMisureAlternativeEventiOrderDesc: "
			// + sqe);
		} finally {
			cleanup(lEveDao);
			cleanup(lMisSqlDao);

			cleanup(lConn);
		}

		return lListaMisureEventi;
	}

	// METODI PRIVATI
	/**
	 * InserimentoAggiornamentoPenResMisuraAlternativa
	 *
	 * @param lConn
	 * @param lEveModel
	 * @param aKey
	 * @return
	 * @throws DAOException
	 * @throws F3BException
	 */
	private PenaResiduaModel InserimentoAggiornamentoPenResMisuraAlternativa(Connection lConn,
			EventoModel lEveModel, BigDecimal aKey, String lRevocaCalcolo) throws DAOException, F3BException {

		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaModel lPenResMod = null;

		try {
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResMod = new PenaResiduaModel();

			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aKey);
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			if ("S".equals(lRevocaCalcolo)) {
				lPenResMod.setEveIdEvento(null);
			}

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
	/**
	 * InserimentoAggiornamentoPosizioneGiuridica
	 *
	 * @param lConn
	 * @param lPosizione
	 * @param lPos
	 * @param lData
	 * @param lEveModel
	 * @param aKeyFasc
	 * @param aKeyEve
	 * @return
	 * @throws DAOException
	 * @throws F3BException
	 */
	private BigDecimal InserimentoAggiornamentoPosizioneGiuridica(Connection lConn, String lPosizione,
			PosizioneGiuridicaModel lPos, Date lData, EventoModel lEveModel, BigDecimal aKeyFasc,
			BigDecimal aKeyEve) throws DAOException, F3BException {

		PosizioneGiuridicaDAO lPosDao = null;
		BigDecimal lIdPosizioneGiuridica = null;

		try {

			lPosDao = new PosizioneGiuridicaDAO(lConn);

			if (lPos != null && lPos.getDataFine() == null) {
				lPosDao.setDataFine(lData);

				/*
				 * ISSUE MAC : modifica al codice Numero MAC : 13/01/2017 Autore : Sessa Data : 16/gen/2017
				 * Branch : MAC_13/01/2017
				 */
				if (lEveModel.getCodUfficioAggiornamento() != null) {
					lPosDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				} else {
					lPosDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioInserimento());
				}
				if (lEveModel.getCodOperatoreAggiornamento() != null) {
					lPosDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				} else {
					lPosDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreInserimento());
				}
				if (lEveModel.getDataAggiornamento() != null) {
					lPosDao.setDataAggiornamento(lEveModel.getDataAggiornamento());
				} else {
					lPosDao.setDataAggiornamento(lEveModel.getDataInserimento());
				}
				// ***** FINE INTERVENTO MAC_13/01/2017 *****//

				lPosDao.setCondizioneUpdate(lPos.getIdPosizioneGiuridica());

				lPosDao.update();
				lPosDao.stop();
			}

			if (lPosizione != null) {
				lPosDao.setCodPosizioneGiuridica(lPosizione);
				lPosDao.setDataInizio(lData);

				/*
				 * ISSUE MAC : modifica al codice Numero MAC : 13/01/2017 Autore : Gioggi Data : 16/gen/2017
				 * Branch : MAC_13/01/2017
				 */
				if (lEveModel.getCodOperatoreAggiornamento() != null) {
					lPosDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				} else {
					lPosDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreInserimento());
				}
				if (lEveModel.getDataAggiornamento() != null) {
					lPosDao.setDataInserimento(lEveModel.getDataAggiornamento());
				} else {
					lPosDao.setDataInserimento(lEveModel.getDataInserimento());
				}
				if (lEveModel.getCodUfficioAggiornamento() != null) {
					lPosDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				} else {
					lPosDao.setCodUfficioInserimento(lEveModel.getCodUfficioInserimento());
				}
				// ***** FINE INTERVENTO MAC_13/01/2017 *****//

				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setFasSieIdFascicoloSiep(aKeyFasc);
				lPosDao.setIdEventoRiferimento(aKeyEve);

				lIdPosizioneGiuridica = lPosDao.insert();
				lPosDao.stop();
			}
		} finally {
			cleanup(lPosDao);
		}

		return lIdPosizioneGiuridica;
	}

	/**
	 * InserimentoAggiornamentoScadenzarioFinePena
	 *
	 * @param lConn
	 * @param lPenResMod
	 * @param lEveModel
	 * @param lData
	 * @param aKey
	 * @throws DAOException
	 * @throws F3BException
	 */
	private void InserimentoAggiornamentoScadenzarioFinePena(Connection lConn, PenaResiduaModel lPenResMod,
			EventoModel lEveModel, Date lData, BigDecimal aKey) throws DAOException, F3BException {

		ScadenzarioDAO lScaDao = new ScadenzarioDAO(lConn);
		ScadenzarioSqlDAO lScaSqlDao = new ScadenzarioSqlDAO(lConn);
		ScadenzarioModel lScaMod = null;
		try {
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("02", aKey);
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

			if (lScaMod != null) {
				lScaDao.setDataFineScadenza(lData);
				lScaDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lScaDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lScaDao.setDataAggiornamento(DateUtils.getSysDate());
				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
				lScaDao.update();
				lScaDao.stop();
			} else {
				// inserisce scadenzario
				ScadenzarioModel lScaModel = new ScadenzarioModel();
				lScaModel.setCodTipoScadenzario("02");
				lScaModel.setDataInizioScadenza(lPenResMod.getDataInizio());
				lScaModel.setDataFineScadenza(lData);

				lScaModel.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lScaModel.setDataInserimento(DateUtils.getSysDate());
				lScaModel.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lScaModel.setFasSieIdFascicoloSiep(aKey);

				lScaDao.setDAOFromModel(lScaModel);
				lScaDao.insert();
				lScaDao.stop();
			}
		} finally {
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
		}
	}

	/**
	 * InserimentoAggiornamentoScadenzarioMisuraAlternativa
	 *
	 * @param lConn
	 * @param lData
	 * @param lPenResMod
	 * @param lEveModel
	 * @param aKey
	 * @throws DAOException
	 * @throws F3BException
	 */
	private void InserimentoAggiornamentoScadenzarioMisuraAlternativa(Connection lConn, Date lData,
			PenaResiduaModel lPenResMod, EventoModel lEveModel, BigDecimal aKey)
			throws DAOException, F3BException {

		ScadenzarioDAO lScaDao = new ScadenzarioDAO(lConn);
		ScadenzarioSqlDAO lScaSqlDao = new ScadenzarioSqlDAO(lConn);
		ScadenzarioModel lScaMod = null;

		try {
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("13", aKey);
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

			if (lScaMod != null) {
				lScaDao.setDataInizioScadenza(lData);
				lScaDao.setDataFineScadenza(lPenResMod.getDataFine());
				lScaDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lScaDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lScaDao.setDataAggiornamento(DateUtils.getSysDate());
				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
				lScaDao.update();
				lScaDao.stop();
			} else {
				// inserisce scadenzario
				ScadenzarioModel lScaModel = new ScadenzarioModel();
				lScaModel.setCodTipoScadenzario("13");
				lScaModel.setDataInizioScadenza(lData);
				lScaModel.setDataFineScadenza(lPenResMod.getDataFine());

				lScaModel.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lScaModel.setDataInserimento(DateUtils.getSysDate());
				lScaModel.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lScaModel.setFasSieIdFascicoloSiep(aKey);

				lScaDao.setDAOFromModel(lScaModel);
				lScaDao.insert();
				lScaDao.stop();
			}
		} finally {
			// sca
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
		}
	}

	/**
	 * InserimentoCancellazioneStatoProcedimento
	 *
	 * @param lConn
	 * @param aKey
	 * @param lEveModel
	 * @param lStatoProcMod
	 * @throws DAOException
	 * @throws F3BException
	 */
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
	 * Effettua l'inserimento di un Elenco di MISURA_ALTERNATIVA <br>
	 *
	 * @param aMisureAlternative
	 *            - Elenco Misure Alternative
	 * @param lConn
	 *            - Connection parametro
	 * @return String - Rapporto Operazione
	 */
	public String ExInserisciMisuraAlternativaWithoutSequence(ArrayList aMisureAlternative, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		MisuraAlternativaDAO lMisAltDao = null;
		MisuraAlternativaModel lMisAltMod = null;
		try {
			lMisAltDao = new MisuraAlternativaDAO(lConn);

			if (aMisureAlternative != null && aMisureAlternative.size() > 0) {
				for (int i = 0; i < aMisureAlternative.size(); i++) {
					lMisAltMod = (MisuraAlternativaModel) aMisureAlternative.get(i);
					siesLogger.info(" Misura Alternativa da inserire = " + lMisAltMod.toString());
					if (lMisAltMod != null && lMisAltMod.getIdMisuraAlternativa() != null) {
						lMisAltDao.setDAOFromModel(lMisAltMod);
						lMisAltDao.setWithoutSequence(true);
						lMisAltDao.insert();
						lMisAltDao.stop();
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.warn("Misura Alternativa gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error(F3BException.USER_MESSAGE + " Impossibile inserire la Misura Alternativa! ");
			}
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("MisuraAlternativaController.ExInserisciMisuraAlternativaWithoutSequence
			// SQLException: "
			// + sqe);
		} finally {
			cleanup(lMisAltDao);
		}
		return lCodEsito;
	}

	@Override
	public MisuraAlternativaModel ExInserisciMisuraAlternativa(MisuraAlternativaModel aMisuraAlternativa)
			throws F3BException {

		Connection lConn = null;

		MisuraAlternativaDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = null;
		try {
			lConn = getDBConnection();

			lMisMod = new MisuraAlternativaModel(aMisuraAlternativa);
			lMisMod.setDataInserimento(new Date());
			lMisDao = new MisuraAlternativaDAO(lConn);

			lMisDao.setDAOFromModel(lMisMod);
			BigDecimal lKey = null;
			lKey = lMisDao.insert();
			lMisMod.setIdMisuraAlternativa(lKey);
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"MisuraAlternativaController.ExInserisciMisuraAlternativa: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisMod;
	}

	@Override
	public MisuraAlternativaModel ExRicercaMisuraAlternativaCorrenteByAnnoProgr(BigDecimal anno,
			BigDecimal progr) throws F3BException {

		Connection lConn = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDao.ricercaMisuraAlternativaByAnnoProgr(anno, progr);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MisuraAlternativaController.ExRicercaMisuraAlternativaByKey: " + daoEx);
		} finally {
			cleanup(lMisDao);

			cleanup(lConn);
		}
		return lMisMod;
	}

	/**
	 * Effettua l'inserimento di un Elenco di MISURA_ALTERNATIVA <br>
	 *
	 * @param aMisureAlternative
	 *            - Elenco Misure Alternative
	 * @param lConn
	 *            - Connection parametro
	 * @return String - Rapporto Operazione
	 */
	public String ExInserisciMisuraAlternativaWithoutSequence(MisuraAlternativaModel aMisuraAlternativa,
			Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		MisuraAlternativaDAO lMisAltDao = null;

		try {
			lMisAltDao = new MisuraAlternativaDAO(lConn);

			if (aMisuraAlternativa != null && aMisuraAlternativa.getIdMisuraAlternativa() != null) {
				siesLogger.debug(
						" Misura Alternativa da inserire = " + aMisuraAlternativa.getIdMisuraAlternativa());
				lMisAltDao.setDAOFromModel(aMisuraAlternativa);
				lMisAltDao.setWithoutSequence(true);
				lMisAltDao.insert();
				lMisAltDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				siesLogger.warn(
						"Misura Alternativa gia' presente..." + aMisuraAlternativa.getIdMisuraAlternativa());
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				siesLogger.error(F3BException.USER_MESSAGE + " Impossibile inserire la Misura Alternativa! ");
			}
		} finally {
			cleanup(lMisAltDao);
		}
		return lCodEsito;
	}

}