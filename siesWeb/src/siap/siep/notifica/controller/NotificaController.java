package siap.siep.notifica.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.dao.AutoritaEsternaSqlDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepPerEventoSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.dao.EveNotificaSqlDAO;
import siap.siep.notifica.dao.NotificaAutoritaSqlDAO;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaFasSiusEveSqlDAO;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaFasSiusEveModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.notifica.model.RicercaNotificheSiusModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.sige.avvocato.dao.AvvocatoFascicoloSigeSqlDAO;
import siap.sige.avvocato.dao.AvvocatoSqlDAO;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusSqlDAO;
import siap.sius.avvocato.model.AvvocatoSiusModel;

/**
 * NotificaController - Classe Controller per Notifica
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NotificaController extends SiapController implements INotifica {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public NotificaModel ExInserisciNotifica(NotificaModel aNotifica) throws F3BException {

		Connection lConn = null;
		NotificaDAO lNotDao = null;
		NotificaModel lNotMod = null;

		try {
			lConn = getDBConnection();
			lNotMod = new NotificaModel(aNotifica);
			lNotDao = new NotificaDAO(lConn);
			lNotDao.setDAOFromModel(aNotifica);
			BigDecimal lKey = null;
			lKey = lNotDao.insert();
			commit(lConn);
			lNotMod.setIdNotifica(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("NotificaController.ExInserisciNotifica: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExInserisciNotifica: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotMod;
	}

	public ArrayList ExInserisciNotifiche(ArrayList aNotifica) throws F3BException {

		Connection lConn = null;
		NotificaDAO lNotDao = null;
		ArrayList lNotifiche = new ArrayList();
		AutoritaEsternaDAO lAutDao = null;

		try {
			lConn = getDBTransaction();

			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);

			BigDecimal lKeyAutorita = null;
			BigDecimal lKey = null;

			Iterator iter = aNotifica.iterator();
			while (iter.hasNext()) {
				NotificaModel lNotModel = (NotificaModel) iter.next();
				if (lNotModel.getAutoritaEsterna() != null) {
					lAutDao.setRicercaByAutSede(lNotModel.getAutoritaEsterna());
					AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
					lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

					if (lAutMod == null) {
						lAutDao.setDAOFromModel(lNotModel.getAutoritaEsterna());
						lKeyAutorita = lAutDao.insert();
						lNotModel.setAutEstIdAutoritaEsterna(lKeyAutorita);
					} else {
						lKeyAutorita = lAutMod.getIdAutoritaEsterna();
						lNotModel.setAutEstIdAutoritaEsterna(lKeyAutorita);
					}
				}

				lNotDao.setDAOFromModel(lNotModel);
				lKey = lNotDao.insert();
				lNotDao.stop();

				lNotModel.setIdNotifica(lKey);
				lNotifiche.add(lNotModel);
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("NotificaController.ExInserisciNotifiche: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("LException: " + e);
			throw new F3BException("NotificaController.ExInserisciNotifiche: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lConn);
		}
		return lNotifiche;
	}

	/**
	 * Ricerca Notifica dall'Evento
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaNotificaByKeyEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lNotifici = new Vector();
		NotificaSqlDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			lNotDao.ricercaNotificaByEvento(aKey);
			lNotifici = new Vector(lNotDao.getModels());

			if (lNotifici.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaNotificaByKeyEvento: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaNotificaByKeyEvento: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotifici;
	}

	/**
	 * ExRicercaEstesaNotificaByKeyEvento (BigDecimal aKey) Ricerca tutte le Notifica legate ad un Evento.
	 * Ricerca Estesa perchè cerca anche gli eventuali destinatari:
	 *
	 * @param BigDecimal
	 *            aKey : ID Evento
	 * @return Vector : contiene le notifiche selezionate
	 * @throws F3BException
	 */
	public Vector<NotificaModel> ExRicercaEstesaNotificaByKeyEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null; // connessione
		Vector<NotificaModel> lNotifici = null;
		NotificaSqlDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			lNotDao.ricercaNotificaByEvento(aKey);
			lNotifici = new Vector<NotificaModel>(lNotDao.getModels());
			cleanup(lNotDao);
			// Inserimento di eventuali destinatari: Autorita Esterna,
			// Ufficio, Avvocato Siep, Avvocato Sius, CSSA.
			lNotifici = RicercaDestinatari(lNotifici, lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaEstesaNotificaByKeyEvento: " + daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqe);
			throw new F3BException("NotificaController.ExRicercaEstesaNotificaByKeyEvento: " + sqe);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaEstesaNotificaByKeyEvento: " + e);
		} finally {
			cleanup(lNotDao); // sca
			cleanup(lConn);
		}
		return lNotifici;
	}

	public Vector<NotificaModel> ExRicercaEstesaNotificaDataAvvNotificaNullByKeyEvento(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null; // connessione
		Vector<NotificaModel> lNotifici = null;
		NotificaSqlDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			lNotDao.ricercaNotificaDataAvvNotificaNullByEvento(aKey);
			lNotifici = new Vector<NotificaModel>(lNotDao.getModels());
			cleanup(lNotDao);
			// Inserimento di eventuali destinatari: Autorita Esterna,
			// Ufficio, Avvocato Siep, Avvocato Sius, CSSA.
			lNotifici = RicercaDestinatari(lNotifici, lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"NotificaController.ExRicercaEstesaNotificaDataAvvNotificaNullByKeyEvento: " + daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqe);
			throw new F3BException(
					"NotificaController.ExRicercaEstesaNotificaDataAvvNotificaNullByKeyEvento: " + sqe);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException(
					"NotificaController.ExRicercaEstesaNotificaDataAvvNotificaNullByKeyEvento: " + e);
		} finally {
			cleanup(lNotDao); // sca
			cleanup(lConn);
		}
		return lNotifici;
	}

	private Vector<NotificaModel> RicercaDestinatari(Vector<NotificaModel> aNotifiche, Connection aConn)
			throws Exception {

		NotificaSqlDAO lNotDao = null;
		UfficioSqlDAO lUffDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;

		try {

			for (NotificaModel lNotifica : aNotifiche) {
				// Autorita Esterne
				if (lNotifica.getAutEstIdAutoritaEsterna() != null) {
					lAutoritaSqlDao = new AutoritaEsternaSqlDAO(aConn);
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(lNotifica.getAutEstIdAutoritaEsterna());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lNotifica.setAutoritaEsterna(lAutorita);
					cleanup(lAutoritaSqlDao);
				}
				// Preleva gli uffici
				if (lNotifica.getUffCodUfficio() != null
						&& lNotifica.getUffCodUfficio().compareTo("-") != 0) {
					IUfficio lUff = SICOLookupRemote.getUfficioRemote();
					UfficioModel lUffMod = lUff.getUfficioByKey(lNotifica.getUffCodUfficio());
					// Inserisce l'occorrenza nel model delle notifiche.
					lNotifica.setUfficio(lUffMod);
				}
				// Preleva CSSA
				if (lNotifica.getCssIdCssa() != null) {
					ICSSA lCssa = SICOLookupRemote.getCSSARemote();
					lNotifica.setCSSA(lCssa.getCSSAByKey(lNotifica.getCssIdCssa()));
				}

				// Preleva gli avvocati SIEP
				if (lNotifica.getAvvIdAvvocatoFascicoloSiep() != null) {
					AvvocatoFascicoloSiepPerEventoSqlDAO lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(
							aConn);
					lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSiep(lNotifica.getAvvIdAvvocatoFascicoloSiep());
					lNotifica.setAvvSiep((AvvocatoSiepModel) lAvvDao.getModelByKey());
					cleanup(lAvvDao);
				}
				// Preleva gli avvocati SIUS
				if (lNotifica.getAvvIdAvvocatoFascicoloSius() != null) {
					AvvocatoFascicoloSiusSqlDAO lAvvSiusDao = new AvvocatoFascicoloSiusSqlDAO(aConn);
					lAvvSiusDao
							.ricercaAvvocatoByKeyAvvocatoFasSius(lNotifica.getAvvIdAvvocatoFascicoloSius());
					lNotifica.setAvvSius((AvvocatoSiusModel) lAvvSiusDao.getModelByKey());
					cleanup(lAvvSiusDao);
				}
				// Preleva gli avvocati SIGE
				if (lNotifica.getAvvIdAvvocatoFascicoloSige() != null) {
					/*
					 * ISSUE MAC : decommentata vecchia valorizzazione avvocato sige Numero MAC : 20200107012
					 * Autore : monica Data : 10/gen/2020 Branch : 11.2.5
					 */

					AvvocatoFascicoloSigeSqlDAO lAvvSigeDao = new AvvocatoFascicoloSigeSqlDAO(aConn);
					lAvvSigeDao
							.ricercaAvvocatoByKeyAvvocatoFasSige(lNotifica.getAvvIdAvvocatoFascicoloSige());
					lNotifica.setAvvSige((AvvocatoSigeModel) lAvvSigeDao.getModelByKey());
					cleanup(lAvvSigeDao);

					/*
					 * AvvocatoSqlDAO avvocatoSqlDao = new AvvocatoSqlDAO(aConn);
					 * avvocatoSqlDao.ricercaAvvocatobyKey(lNotifica.getAvvIdAvvocatoFascicoloSige());
					 * avvocatoSqlDao.start(); if (avvocatoSqlDao.next())
					 * lNotifica.setAvvocato((AvvocatoModel) avvocatoSqlDao.getModelByKey());
					 *
					 * avvocatoSqlDao.stop(); cleanup(avvocatoSqlDao);
					 */
					// ***** FINE INTERVENTO 20200107012 *****//
				}
				// tipo istituto
				if (lNotifica.getIstDetIdIstitutoDetenzione() != null
						&& !lNotifica.getIstDetIdIstitutoDetenzione().equals("")) {
					lIstDao = new IstitutoDetenzioneSqlDAO(aConn);
					lIstDao.ricercaIstitutoDetenzioneByKey(lNotifica.getIstDetIdIstitutoDetenzione());
					IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					lNotifica.setIstitutoDetenzione(lIstituto);
					lIstDao.stop();
				}
			} // endwhile
		} finally {
			cleanup(lNotDao); // sca
			cleanup(lUffDao); // sca
			cleanup(lAutoritaSqlDao); // sca
			cleanup(lIstDao); // sca
		}
		return aNotifiche;
	}

	/**
	 * Ricerca Notifiche attraverso la chiave del Fascicolo Sius.
	 *
	 * @param aEventoKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaNotificheByFascicoloSius(BigDecimal aFascKey, String aTipoEvento)
			throws F3BException {

		Connection lConn = null; // connessione
		Vector lNotifiche = null;
		EveNotificaSqlDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new EveNotificaSqlDAO(lConn);
			lNotDao.ricercaEveNotificaByFascicoloSius(aFascKey, aTipoEvento);
			lNotifiche = new Vector(lNotDao.getModels());
			cleanup(lNotDao);
			// Inserimento di eventuali destinatari: Autorita Esterna,
			// Ufficio, Avvocato Siep, Avvocato Sius, CSSA.
			lNotifiche = RicercaDestinatari(lNotifiche, lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaEventoByFascicoloSius: " + daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqe);
			throw new F3BException("NotificaController.ExRicercaEventoByFascicoloSius: " + sqe);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaEventoByFascicoloSius: " + e);
		} finally {
			cleanup(lNotDao); // sca

			cleanup(lConn);
		}
		return lNotifiche;
	}

	public Vector ExRicercaNotifica(NotificaModel aNotifica) throws F3BException {

		Connection lConn = null;
		Vector lNotifici = new Vector();
		NotificaSqlDAO lNotDao = null;
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			lNotDao.ricercaNotifica(aNotifica);
			lNotifici = new Vector(lNotDao.getModels());
			if (lNotifici.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaNotifica: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaNotifica: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotifici;
	}

	public Vector ExRicercaNotificaAvvocatoNonAvvenuta(NotificaModel aNotifica) throws F3BException {

		Connection lConn = null;
		Vector lNotifici = new Vector();
		NotificaSqlDAO lNotDao = null;
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			lNotDao.ricercaNotificaAvvocatoNonAvvenuta(aNotifica);

			lNotifici = new Vector(lNotDao.getModels());

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaNotificaAvvocatoNonAvvenuta: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaNotificaAvvocatoNonAvvenuta: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotifici;
	}

	public NotificaModel ExRicercaNotificaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		NotificaSqlDAO lNotDao = null;
		Vector lNotifici = null;
		NotificaModel lNotifica = null;
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			lNotDao.ricercaNotificaByKey(aKey);
			// lNotMod = (NotificaModel) lNotDao.getModelByKey();

			lNotifici = new Vector();
			lNotifici.add(lNotDao.getModelByKey());
			cleanup(lNotDao);
			// Inserimento di eventuali destinatari
			lNotifici = RicercaDestinatari(lNotifici, lConn);
			Iterator itx = lNotifici.iterator();
			while (itx.hasNext()) {
				lNotifica = (NotificaModel) itx.next();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaNotificaByKey:  " + daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqe);
			throw new F3BException("NotificaController.ExRicercaNotificaByKey: " + sqe);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaNotificaByKey: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}

		return lNotifica;
	}

	public NotificaModel ExRicercaNotificaCompByFascicolo(BigDecimal aFascicolo) throws F3BException {

		Connection lConn = null;
		NotificaAutoritaSqlDAO lNotDao = null;
		NotificaModel lNotMod;
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaAutoritaSqlDAO(lConn);
			lNotDao.ricercaNotificaCompByFascicolo(aFascicolo);
			lNotMod = (NotificaModel) lNotDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaNotificaCompByFascicolo:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaNotificaCompByFascicolo: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotMod;
	}

	public NotificaModel ExRicercaNotificaPolByFascicolo(BigDecimal aFascicolo) throws F3BException {

		Connection lConn = null;
		NotificaAutoritaSqlDAO lNotDao = null;
		NotificaModel lNotMod;
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaAutoritaSqlDAO(lConn);
			lNotDao.ricercaNotificaPolByFascicolo(aFascicolo);
			lNotMod = (NotificaModel) lNotDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaNotificaPolByFascicolo:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaNotificaPolByFascicolo: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotMod;
	}

	// ricerca tipo notifica=N
	public NotificaModel ExRicercaNotificaTipoNotNByFascicolo(BigDecimal aFascicolo) throws F3BException {

		Connection lConn = null;
		NotificaAutoritaSqlDAO lNotDao = null;
		NotificaModel lNotMod;
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaAutoritaSqlDAO(lConn);
			lNotDao.ricercaNotificaPoliziaNotificaNByFascicolo(aFascicolo);
			lNotMod = (NotificaModel) lNotDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaNotificaTipoNotNByFascicolo:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaNotificaTipoNotNByFascicolo: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotMod;
	}

	// ricerca tipo notifica=C
	public NotificaModel ExRicercaNotificaTipoNotCByFascicolo(BigDecimal aFascicolo) throws F3BException {

		Connection lConn = null;
		NotificaAutoritaSqlDAO lNotDao = null;
		NotificaModel lNotMod;
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaAutoritaSqlDAO(lConn);
			lNotDao.ricercaNotificaPoliziaNotificaCByFascicolo(aFascicolo);
			lNotMod = (NotificaModel) lNotDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaNotificaTipoNotCByFascicolo:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException("NotificaController.ExRicercaNotificaTipoNotCByFascicolo: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotMod;
	}

	public NotificaModel ExRicercaNotificaUffByFascicolo(BigDecimal aFascicolo) throws F3BException {

		Connection lConn = null;
		NotificaAutoritaSqlDAO lNotDao = null;
		NotificaModel lNotMod;
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaAutoritaSqlDAO(lConn);
			lNotDao.ricercaNotificaAutByFascicolo(aFascicolo);
			lNotMod = (NotificaModel) lNotDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaNotificaUffByFascicolo:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaNotificaUffByFascicolo: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotMod;
	}

	public NotificaModel ExModificaNotifica(NotificaModel aNotifica) throws F3BException {

		Connection lConn = null;
		NotificaDAO lNotDao = null;
		NotificaModel lNotMod = new NotificaModel(aNotifica);

		try {
			lConn = getDBConnection();
			lNotDao = new NotificaDAO(lConn);
			lNotDao.setDAOFromModelForUpdate(aNotifica);
			lNotDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("NotificaController.ExModifica: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExModificaNotifica: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotMod;
	}

	public void ExCancellaNotifica(NotificaModel aNotifica) throws F3BException {

		Connection lConn = null;
		NotificaDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotificaDAO(lConn);
			lNotDao.setCondizioneUpdate(aNotifica.getIdNotifica());
			lNotDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExCancellaNotifica:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExCancellaNotifica:  " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
	}

	public Vector ExAggiornaRegistrazioneNotificaDecretoIrreeribilita(NotificaModel[] IdNotifiche,
			BigDecimal aFasc) throws F3BException {

		Connection lConn = null;

		Vector lVectNot = new Vector();
		NotificaDAO lNotDAO = null;
		ScadenzarioDAO lScaDao = null;

		try {
			lConn = getDBTransaction();

			lNotDAO = new NotificaDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);
			int lLungVect = IdNotifiche.length;

			for (int i = 0; i < lLungVect; i++) {
				NotificaModel lNot = new NotificaModel();
				lNot = IdNotifiche[i];

				lNotDAO.setDataAvvenutaNotifica(lNot.getDataAvvenutaNotifica());
				lNotDAO.setCodiceOperatoreAggiornamento(lNot.getCodiceOperatoreAggiornamento());
				lNotDAO.setCodUfficioAggiornamento(lNot.getCodUfficioAggiornamento());
				lNotDAO.setDataAggiornamento(lNot.getDataAggiornamento());
				lNotDAO.setCondizioneUpdate(lNot.getIdNotifica());
				lNotDAO.setEveIdEvento(lNot.getEveIdEvento());
				lNotDAO.setCodEsito(lNot.getCodEsito());

				lNotDAO.update();
				lNotDAO.stop();
				lVectNot.add(lNot);

				/*
				 * se data di fine scadenza è compresa tra il 1 agosto e il 15 settembre la data fine scadenza
				 * viene messa al 16 settembre
				 */
				Date lDataFineScadenza = DateUtils.moveDateTo(IdNotifiche[i].getDataAvvenutaNotifica(),
						java.util.Calendar.DAY_OF_MONTH, 30);
				GregorianCalendar lDataFine = new GregorianCalendar();
				lDataFine.setTime(lDataFineScadenza);

				GregorianCalendar lDataAgosto = new GregorianCalendar(lDataFine.get(Calendar.YEAR),
						Calendar.AUGUST, 1);
				GregorianCalendar lDataSettembre = new GregorianCalendar(lDataFine.get(Calendar.YEAR),
						Calendar.SEPTEMBER, 15);

				if (!IdNotifiche[i].getDataAvvenutaNotifica().before(lDataAgosto.getTime())
						&& !IdNotifiche[i].getDataAvvenutaNotifica().after(lDataSettembre.getTime())) {
					lDataFineScadenza = DateUtils.moveDateTo(lDataSettembre.getTime(),
							java.util.Calendar.DAY_OF_MONTH, 30);
				} else {
					if (!lDataFine.before(lDataAgosto) && !lDataFine.after(lDataSettembre)) {
						lDataFineScadenza = DateUtils.moveDateTo(lDataFineScadenza,
								java.util.Calendar.DAY_OF_MONTH, 46);
					}
				}

				lScaDao.setCodTipoScadenzario("01");
				lScaDao.setDataInizioScadenza(IdNotifiche[i].getDataAvvenutaNotifica());
				lScaDao.setDataFineScadenza(lDataFineScadenza);
				lScaDao.setFlagVisto("N");
				lScaDao.setCodOperatoreInserimento(IdNotifiche[i].getCodiceOperatoreAggiornamento());
				lScaDao.setCodUfficioInserimento(IdNotifiche[i].getCodUfficioAggiornamento());
				lScaDao.setDataInserimento(DateUtils.getSysDate());
				lScaDao.setFasSieIdFascicoloSiep(aFasc);
				lScaDao.setNotIdNotifica(IdNotifiche[i].getIdNotifica());
				lScaDao.insert();
				lScaDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"OrdineEsecuzioneController.ExAggiornaAvvenutaNotifica: daoEx--> " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("OrdineEsecuzioneController.ExAggiornaAvvenutaNotifica: ex--> " + ex);
		} finally {
			cleanup(lNotDAO);
			cleanup(lScaDao);

			cleanup(lConn);
		}

		return lVectNot;
	}

	/**
	 * ExAggiornaDateNotifica() Aggiorna la data di avvenuta notifica di un elenco di notifiche passato come
	 * parametro.
	 *
	 * @param NotificaModel
	 *            IdNotifiche[]
	 * @return void
	 * @throws F3BException
	 */
	public void ExAggiornaDateNotifica(NotificaModel[] aNotifiche) throws F3BException {

		Connection lConn = null;

		NotificaDAO lNotDAO = null;

		try {
			lConn = getDBTransaction();

			lNotDAO = new NotificaDAO(lConn);

			int lLung = aNotifiche.length;

			for (int i = 0; i < lLung; i++) {
				lNotDAO.setDataAvvenutaNotifica(aNotifiche[i].getDataAvvenutaNotifica());
				lNotDAO.setCodiceOperatoreAggiornamento(aNotifiche[i].getCodiceOperatoreAggiornamento());
				lNotDAO.setCodUfficioAggiornamento(aNotifiche[i].getCodUfficioAggiornamento());
				lNotDAO.setDataAggiornamento(aNotifiche[i].getDataAggiornamento());
				// lNotDAO.setEveIdEvento(IdNotifiche[i].getEveIdEvento());
				lNotDAO.setCodEsito(aNotifiche[i].getCodEsito());

				lNotDAO.setCondizioneUpdate(aNotifiche[i].getIdNotifica());

				lNotDAO.update();
				lNotDAO.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();

			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExAggiornaDateNotifica: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();

			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException("NotificaController.ExAggiornaDateNotifica: " + ex);
		} finally {
			cleanup(lNotDAO);

			cleanup(lConn);
		}
	}

	/**
	 * Verifica se tutte le notifiche legate ad un evento sono già state notificate.
	 *
	 * @param aKey
	 *            : Identificativo Evento
	 * @return boolean : true se tutte le notifiche hanno data di avvenuta notifica
	 * @throws F3BException
	 */
	public boolean ExSonoNotificate(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		NotificaSqlDAO lNotDao = null;
		boolean bRet = false; // valore di ritorno
		int lnum = -1; // numero di record da notificare

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("-------- ExSonoNotificate: inizio ");
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			lnum = lNotDao.getNumNotificheDaNotificareByEve(aKey);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExSonoNotificate:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExSonoNotificate:  " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
			if (lnum == 0)
				bRet = true;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("-------- ExSonoNotificate: fine num->  " + lnum);
		}
		return bRet;
	}

	/**
	 * Verifica se tutte le notifiche di tipo N legate ad un evento sono già state notificate.
	 *
	 * @param aKey
	 *            : Identificativo Evento
	 * @return boolean : true se tutte le notifiche hanno data di avvenuta notifica
	 * @throws F3BException
	 */
	public boolean ExSonoNotificateSige(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		NotificaSqlDAO lNotDao = null;
		boolean bRet = false; // valore di ritorno
		int lnum = -1; // numero di record da notificare

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("-------- ExSonoNotificate: inizio ");
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			lnum = lNotDao.getNumNotificheSigeDaNotificareByEve(aKey);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExSonoNotificate:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExSonoNotificate:  " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
			if (lnum == 0)
				bRet = true;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("-------- ExSonoNotificate: fine num->  " + lnum);
		}
		return bRet;
	}

	/**
	 * Restituisce la data di avvenuta notifica più alta in un gruppo di di notifiche legate allo stesso
	 * evento.
	 *
	 * @param aKey
	 *            : Identificativo Evento
	 * @return data : data di avvenuta notifica massima
	 * @throws F3BException
	 */
	public Date ExRicercaDataNotifica(BigDecimal aIdEve) throws F3BException {

		Connection lConn = null;
		NotificaSqlDAO lNotDao = null;
		Date rData = null; // data di ritorno
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("-------- ExRicercaDataNotifica: inizio");
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			rData = lNotDao.SelDataNotificaByEve(aIdEve);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaDataNotifica:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaDataNotifica:  " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("-------- ExRicercaDataNotifica: fine data-> " + rData);
		}
		return rData;
	}

	/**
	 * Restituisce la data di avvenuta notifica più alta in un gruppo di notifiche di tipo N legate allo
	 * stesso evento.
	 *
	 * @param aKey
	 *            : Identificativo Evento
	 * @return data : data di avvenuta notifica massima
	 * @throws F3BException
	 */
	public Date ExRicercaDataNotificaSige(BigDecimal aIdEve) throws F3BException {

		Connection lConn = null;
		NotificaSqlDAO lNotDao = null;
		Date rData = null; // data di ritorno
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("-------- ExRicercaDataNotifica: inizio");
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			rData = lNotDao.SelDataNotificaSigeByEve(aIdEve);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaDataNotifica:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaDataNotifica:  " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("-------- ExRicercaDataNotifica: fine data-> " + rData);
		}
		return rData;
	}

	/**
	 * La funzione realizza una ricerca di Notifiche/Comunicazione di Procedimenti SIUS. La ricerca utilizza
	 * una serie di condizioni di filtro che sono passate attraverso il RicercaNotificheSiusModel. Il
	 * risultato è una lista (Vector) di model NotificaFasSiusEveModel; in questi sono presenti oltre ai dati
	 * della Notifica anche dati relativi al Fascicolo SIUS, Provvedimento (Evento) e Soggetto.
	 *
	 * @param aModel
	 *            RicercaNotificheSiusModel.
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaPaginataNotificheXFasSius(RicercaNotificheSiusModel aModel, int aPageNum)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExRicercaPaginataNotificheXFasSius(): inizio ");
		Connection lConn = null; // connessione
		Vector lNotifiche = new Vector();
		NotificaFasSiusEveSqlDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotificaFasSiusEveSqlDAO(lConn);
			lNotDao.ricercaNotificheFasSiusProvSog(aModel);

			// Ricerca paginata
			// Viene fatta la ricerca non paginata se aPageNum ha un valore <= 0
			if (aPageNum > 0)
				lNotDao.startPage(aPageNum);
			else
				lNotDao.start();

			NotificaFasSiusEveModel lNotifica = null;
			while (lNotDao.next()) {
				lNotifica = (NotificaFasSiusEveModel) lNotDao.getModel();
				lNotifiche.add(lNotifica);
			}
			lNotDao.stop();

			if (lNotifiche.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

			// cleanup(lNotDao);
			// Inserimento di eventuali destinatari: Autorita Esterna,
			// Ufficio, Avvocato Siep, Avvocato Sius, CSSA.
			lNotifiche = RicercaDestinatari(lNotifiche, lConn);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("NotificaController.ExRicercaPaginataNotificheXFasSius: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lNotDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExRicercaPaginataNotificheXFasSius(): fine ");

		return lNotifiche;
	}

	/**
	 * Ritorna n.ro di record risultato della ExRicercaPaginataNotificheXFasSius
	 *
	 * @param RicercaNotificheSiusModel
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaNotificheXFasSius(RicercaNotificheSiusModel aModel) throws F3BException {

		Connection lConn = null; // connessione
		NotificaFasSiusEveSqlDAO lNotDao = null;
		BigDecimal lCont = new BigDecimal(0);
		try {
			lConn = getDBConnection();
			lNotDao = new NotificaFasSiusEveSqlDAO(lConn);
			lNotDao.ricercaNotificheFasSiusProvSog(aModel);
			lCont = lNotDao.getNumRowsSelected();
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ricerca Notifiche attraverso la chiave del Fascicolo Sius.
	 *
	 * @param aEventoKey
	 * @param aTipoEvento
	 * @param aCodMotivo
	 * @return
	 * @throws F3BException
	 */
	public Date ExRicercaDataInvioCertCasellario(BigDecimal aFascKey, String aTipoEvento, String aCodMotivo)
			throws F3BException {

		Connection lConn = null;
		EveNotificaSqlDAO lNotDao = null;
		Date dataInvioCert = null;

		try {
			lConn = getDBConnection();
			lNotDao = new EveNotificaSqlDAO(lConn);
			dataInvioCert = lNotDao.getDataInvioCertCasellario(aFascKey, aTipoEvento, aCodMotivo);
		} catch (DAOException daoEx) {
			throw new F3BException("NotificaController.ExRicercaDataInvioCertCasellario: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("NotificaController.ExRicercaDataInvioCertCasellario: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}

		return dataInvioCert;
	}

	/**
	 * Ricerca tutte le Notifica legate alla Parte (civile/offesa) inserita in un'udienza. Ricerca Estesa
	 * perchè cerca anche gli eventuali destinatari.
	 *
	 * @param BigDecimal
	 *            aIdParteUdienza : ID_PARTE_UDIENZA
	 * @return Vector : contiene le notifiche selezionate
	 * @throws F3BException
	 */
	public Vector ExRicercaEstesaNotificaByIdParteUdienza(BigDecimal aIdParteUdienza) throws F3BException {

		Connection lConn = null; // connessione
		Vector lNotifiche = null;
		NotificaSqlDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			lNotDao.ricercaNotificaByIdParteUdienza(aIdParteUdienza);
			lNotifiche = new Vector(lNotDao.getModels());
			cleanup(lNotDao);
			// Inserimento di eventuali destinatari: Autorita Esterna,
			// Ufficio, Avvocato Siep, Avvocato Sius, CSSA.
			lNotifiche = RicercaDestinatariParteUdienza(lNotifiche, lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaEstesaNotificaByIdParteUdienza: " + daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqe);
			throw new F3BException("NotificaController.ExRicercaEstesaNotificaByIdParteUdienza: " + sqe);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaEstesaNotificaByIdParteUdienza: " + e);
		} finally {
			cleanup(lNotDao); // sca
			cleanup(lConn);
		}
		return lNotifiche;
	}

	/**
	 * Ricerca tutte le Notifica legate alla Parte (civile/offesa) inserita in un'udienza.
	 *
	 * @param BigDecimal
	 *            aIdParteUdienza : ID_PARTE_UDIENZA
	 * @return Vector : contiene le notifiche selezionate
	 * @throws F3BException
	 */
	public Vector ExRicercaNotificaByIdParteUdienza(BigDecimal aIdParteUdienza) throws F3BException {

		Connection lConn = null; // connessione
		Vector lNotifiche = null;
		NotificaSqlDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotificaSqlDAO(lConn);
			lNotDao.ricercaNotificaByIdParteUdienza(aIdParteUdienza);
			lNotifiche = new Vector(lNotDao.getModels());
			cleanup(lNotDao);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("NotificaController.ExRicercaNotificaByIdParteUdienza: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("NotificaController.ExRicercaNotificaByIdParteUdienza: " + e);
		} finally {
			cleanup(lNotDao); // sca
			cleanup(lConn);
		}
		return lNotifiche;
	}

	private Vector RicercaDestinatariParteUdienza(Vector aNotifiche, Connection aConn) throws Exception {

		NotificaSqlDAO lNotDao = null;
		UfficioSqlDAO lUffDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		try {
			Iterator itx = aNotifiche.iterator();
			while (itx.hasNext()) {
				NotificaModel lNotifica = (NotificaModel) itx.next();

				// Autorita Esterne
				if (lNotifica.getAutEstIdAutoritaEsterna() != null) {
					lAutoritaSqlDao = new AutoritaEsternaSqlDAO(aConn);
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(lNotifica.getAutEstIdAutoritaEsterna());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lNotifica.setAutoritaEsterna(lAutorita);
					cleanup(lAutoritaSqlDao);
				}

				// Preleva gli uffici
				if (lNotifica.getUffCodUfficio() != null
						&& lNotifica.getUffCodUfficio().compareTo("-") != 0) {
					IUfficio lUff = SICOLookupRemote.getUfficioRemote();
					UfficioModel lUffMod = lUff.getUfficioByKey(lNotifica.getUffCodUfficio());
					// Inserisce l'occorrenza nel model delle notifiche.
					lNotifica.setUfficio(lUffMod);
				}

				// Preleva gli avvocati SIGE
				if (lNotifica.getAvvIdAvvocatoFascicoloSige() != null) {
					AvvocatoSqlDAO lAvvSigeDao = new AvvocatoSqlDAO(aConn);
					lAvvSigeDao.ricercaAvvocatobyKey(lNotifica.getAvvIdAvvocatoFascicoloSige());
					lNotifica.setAvvParteUdienza((AvvocatoModel) lAvvSigeDao.getModelByKey());
					cleanup(lAvvSigeDao);
				}

				// tipo istituto
				if (lNotifica.getIstDetIdIstitutoDetenzione() != null
						&& !lNotifica.getIstDetIdIstitutoDetenzione().equals("")) {
					lIstDao = new IstitutoDetenzioneSqlDAO(aConn);
					lIstDao.ricercaIstitutoDetenzioneByKey(lNotifica.getIstDetIdIstitutoDetenzione());
					IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					lNotifica.setIstitutoDetenzione(lIstituto);
					lIstDao.stop();
				}
			} // endwhile
		} finally {
			cleanup(lNotDao);
			cleanup(lUffDao);
			cleanup(lAutoritaSqlDao);
			cleanup(lIstDao);
		}
		return aNotifiche;
	}

	public ArrayList ExInserisciNotifiche(ArrayList aNotifica, Connection aConn) throws F3BException {

		NotificaDAO lNotDao = null;
		ArrayList lNotifiche = new ArrayList();
		AutoritaEsternaDAO lAutDao = null;

		try {

			lAutDao = new AutoritaEsternaDAO(aConn);
			lNotDao = new NotificaDAO(aConn);

			BigDecimal lKeyAutorita = null;
			BigDecimal lKey = null;

			Iterator iter = aNotifica.iterator();
			while (iter.hasNext()) {
				NotificaModel lNotModel = (NotificaModel) iter.next();
				if (lNotModel.getAutoritaEsterna() != null) {
					lAutDao.setRicercaByAutSede(lNotModel.getAutoritaEsterna());
					AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
					lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

					if (lAutMod == null) {
						lAutDao.setDAOFromModel(lNotModel.getAutoritaEsterna());
						lKeyAutorita = lAutDao.insert();
						lNotModel.setAutEstIdAutoritaEsterna(lKeyAutorita);
					} else {
						lKeyAutorita = lAutMod.getIdAutoritaEsterna();
						lNotModel.setAutEstIdAutoritaEsterna(lKeyAutorita);
					}
				}

				lNotDao.setDAOFromModel(lNotModel);
				lKey = lNotDao.insert();
				lNotDao.stop();

				lNotModel.setIdNotifica(lKey);
				lNotifiche.add(lNotModel);
			}
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("NotificaController.ExInserisciNotifiche: " + ex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("LException: " + e);
			throw new F3BException("NotificaController.ExInserisciNotifiche: " + e);
		} finally {
			cleanup(lNotDao);
			cleanup(lAutDao);
		}
		return lNotifiche;
	}

	/*
	 * ISSUE MEV : AGGIUNTO METODO DI AGGIORNAMENTO DATI NOTIFICHE
	 * Numero MEV : 9
	 * Autore : sgioggi
	 * Data : 18 apr 2023
	 * Branch : MEV_9
	 */
	@Override
	public void ExModificaNotifiche(Vector<NotificaModel> notifiche, String[] check) throws F3BException {

		Connection c = null;
		NotificaDAO ndao = null;
		NotificaDAO ndaoCanc = null;
		AutoritaEsternaDAO aedao = null;

		try {
			c = getDBTransaction();

			// Update delle Notifiche
			aedao = new AutoritaEsternaDAO(c);
			BigDecimal keyAutorita = null;
			if (notifiche != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + notifiche.size() + " notifiche");

				Iterator<NotificaModel> listaNotifiche = notifiche.iterator();
				while (listaNotifiche.hasNext()) {
					NotificaModel nm = listaNotifiche.next();
					if (nm.getAutoritaEsterna() != null) {
						aedao.setRicercaByAutSede(nm.getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) aedao.getModelByKey();
						if (lAutMod == null) {
							aedao.setDAOFromModel(nm.getAutoritaEsterna());
							keyAutorita = aedao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Inserita AUTORITA con ID = " + keyAutorita);
							nm.setAutEstIdAutoritaEsterna(keyAutorita);
						} else {
							keyAutorita = lAutMod.getIdAutoritaEsterna();
							nm.setAutEstIdAutoritaEsterna(keyAutorita);
						}
					}
					ndao = new NotificaDAO(c);
					ndao.setDAOFromModelForUpdate(nm);
					ndao.update();
				}

				// Cancellazione delle notifiche preesistenti per le notifiche selezionate.
				ndaoCanc = new NotificaDAO(c);
				if (check != null) {
					for (int z = 0; z < check.length; z++) {
						ndaoCanc.start();
						ndaoCanc.setCondizioneUpdate(new BigDecimal(check[z].toUpperCase()));
						ndaoCanc.delete();
						ndaoCanc.stop();
					}
				}
			}

			commit(c);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(c);
			throw new F3BException("NotificaController.ExModificaNotifiche: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			ex.printStackTrace();
			rollback(c);
			throw new F3BException("NotificaController.ExModificaNotifiche: " + ex);
		} finally {
			cleanup(ndao);
			cleanup(ndaoCanc);

			cleanup(c);
		}
	}
	// ***** FINE INTERVENTO MEV_9 *****//

}