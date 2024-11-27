package siap.jms.messaggio.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.jms.ICostantiJMS;
import siap.jms.manage.ManageMessageNoSend;
import siap.jms.messaggio.dao.MessaggioDAO;
import siap.jms.messaggio.dao.MessaggioSqlDAO;
import siap.jms.messaggio.model.ContatoreEsitiModel;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siepe.ricezioneatti.model.CruscottoModel;

/**
 * <p>
 * Title: MessaggioController
 * </p>
 * <p>
 * Description: Classe Controller per Messaggio
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
public class MessaggioController extends SiapController implements IMessaggio, ICostantiJMS {

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);

	/**
	 * Inserisce MEssaggio
	 *
	 * @param aMessaggio
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExInserisciMessaggio(MessaggioModel aMessaggio) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		MessaggioDAO lMesDao = null;
		MessaggioModel lMesMod = null;

		try {
			lConn = getDBConnection();
			lMesMod = new MessaggioModel(aMessaggio);
			lMesDao = new MessaggioDAO(lConn);
			lMesDao.setDAOFromModel(aMessaggio);

			BigDecimal lKey = null;
			lKey = lMesDao.insert();

			commit(lConn);

			lMesMod.setIdMessaggio(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "", ex);
			throw new F3BException("MessaggioController.ExInserisciMessaggio: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "", sqe);
			throw new F3BException("MessaggioController.ExInserisciMessaggio: " + sqe);
		} catch (Exception exc) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "", exc);
			throw new F3BException("MessaggioController.ExInserisciMessaggio: " + exc);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");

		return lMesMod;
	}

	/**
	 * Inserisci Messaggio Esito
	 *
	 * @param aMessaggio
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExInserisciMessaggioEsitoTrasferimento(MessaggioModel aMessaggio)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		MessaggioDAO lMesDao = null;
		MessaggioModel lMesMod = null;

		EventoDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lMesMod = new MessaggioModel(aMessaggio);
			lMesDao = new MessaggioDAO(lConn);
			lMesDao.setDAOFromModel(aMessaggio);
			Date dataEmissioneCumulo = aMessaggio.getDataEmissioneCumulo();
			siesLogger.debug(">>>>>>>>>>>>> DataEmissioneCumulo in ARRIVO>>>>>>>>>>>>>"
					+ aMessaggio.getDataEmissioneCumulo());
			if (dataEmissioneCumulo != null && "00079".equals(aMessaggio.getCodTipoOperazione())) {
				lMesDao.setDataEsito(dataEmissioneCumulo);
			}

			BigDecimal lKey = null;
			lKey = lMesDao.insert();

			// Se il messaggio è di tipo ESITO DI TRASFERIMENTO
			if (aMessaggio != null && aMessaggio.getCodTipoOperazione() != null
					&& aMessaggio.getCodTipoOperazione().equals(ICostantiJMS.ESITO_TRASFERIMENTO_ISTANZA)) {
				// Sull'Evento Istanza aggiorna la DATA_RICEZIONE_ATTI
				// e il COD_ESITO
				lEveDao = new EventoDAO(lConn);
				ParserMessage lParser = new ParserMessage(aMessaggio.getTreeModel());
				EventoNotificaModel lEveNotMod = lParser.getEvento();
				if (lEveNotMod != null && lEveNotMod.getEvento() != null) {
					lEveDao.setDataRicezioneAtti(new Date());
					lEveDao.setCodEsito("36");
					lEveDao.selCondizioneUpdate(lEveNotMod.getEvento().getIdEvento());
					lEveDao.update();
				}
			}
			commit(lConn);

			lMesMod.setIdMessaggio(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("MessaggioController.ExInserisciMessaggioEsitoTrasferimento: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqe);
			throw new F3BException("MessaggioController.ExInserisciMessaggioEsitoTrasferimento: " + sqe);
		} catch (Exception exc) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + exc);
			throw new F3BException("MessaggioController.ExInserisciMessaggioEsitoTrasferimento: " + exc);
		} finally {
			cleanup(lMesDao);
			cleanup(lEveDao);

			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMesMod;
	}

	/**
	 * @param aUfficio
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioRichiestaPerUfficio(String aUfficio, String aTipoOperazione)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioRichiestaPerUfficioTipoOperazione(aUfficio, aTipoOperazione);

			lMessaggii = new Vector(lMesDao.getModels());

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Messaggio in Arrivo");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioRichiestaPerUfficio: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * @param aUfficio
	 * @return
	 * @throws F3BException
	 */
	/*
	 * si è scelto di creare un'altro metodo di ricerca per togliere il messaggio di errore all'interno del
	 * metodo e metterlo nella classe 16-03-05 -- Dario -- Viviana
	 */
	public Vector ExRicercaMessaggiRichiestaPerUfficio(String aUfficio, String aTipoOperazione)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioRichiestaPerUfficioTipoOperazione(aUfficio, aTipoOperazione);

			lMessaggii = new Vector(lMesDao.getModels());

			/*
			 * if (lMessaggii.size() == 0) { throw new F3BException(F3BException.USER_MESSAGE,
			 * "Nessun Messaggio in Arrivo"); }
			 */
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggiRichiestaPerUfficio: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * Ricerca Messaggio Esito per Ufficio
	 *
	 * @param aUfficio
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioEsitoPerUfficio(String aUfficio) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioRichiestaSpediti(aUfficio);
			lMessaggii = new Vector(lMesDao.getModels());
			lMesDao.stop();

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Messaggio di Esito in Attesa");
			}

			// Collego ai Messaggi di tipo Esito trovati il Rispettivo Messaggio di Richiesta
			for (int i = 0; i < lMessaggii.size(); i++) {
				MessaggioModel aMess = (MessaggioModel) lMessaggii.get(i);

				lMesDao.ricercaMessaggioPerJmsId(aMess.getIdMessaggio().toString());
				Vector lVect = new Vector(lMesDao.getModels());

				if (lVect.size() > 0) {
					aMess.setMessaggioCorrelato((MessaggioModel) lVect.firstElement());
					lMessaggii.set(i, aMess);
				}

				lMesDao.stop();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + ex);
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * Ricerca Messaggio Esito per Ufficio e Tipo Operazione
	 *
	 * @param aUfficio
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioEsitoPerUfficio(String aUfficio, String aTipoOperazione)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioRichiestaSpediti(aUfficio, aTipoOperazione);
			lMessaggii = new Vector(lMesDao.getModels());
			lMesDao.stop();

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Messaggio di Esito in Attesa");
			}

			// Collego ai Messaggi di tipo Esito trovati il Rispettivo Messaggio di Richiesta
			for (int i = 0; i < lMessaggii.size(); i++) {
				MessaggioModel aMess = (MessaggioModel) lMessaggii.get(i);

				lMesDao.ricercaMessaggioPerJmsId(aMess.getIdMessaggio().toString());
				Vector lVect = new Vector(lMesDao.getModels());

				if (lVect.size() > 0) {
					aMess.setMessaggioCorrelato((MessaggioModel) lVect.firstElement());
					lMessaggii.set(i, aMess);
				}

				lMesDao.stop();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + ex);
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * RIcerca dei messaggi di tipo Esito
	 *
	 * @param aUfficio
	 * @param aTipoOperazione
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioEsitoRicercaPerUfficio(String aUfficio, String aTipoOperazione, int aPage)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		Vector lMessaggi = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioEsitoRicerca(aUfficio, aTipoOperazione, aPage);
			lMessaggi = new Vector(lMesDao.getModels());
			lMesDao.stop();

			if (lMessaggi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Messaggio di Ricerca in Attesa");
			}

			// Collego ai Messaggi di tipo Esito trovati il Rispettivo Messaggio di Richiesta
			for (int i = 0; i < lMessaggi.size(); i++) {
				MessaggioModel aMess = (MessaggioModel) lMessaggi.get(i);

				lMesDao.ricercaMessaggioEsitoRicercaPerJmsId(aMess.getIdMessaggio().toString());
				Vector lVect = new Vector(lMesDao.getModels());

				if (lVect.size() > 0) {
					aMess.setMessaggioCorrelato((MessaggioModel) lVect.firstElement());
					lMessaggi.set(i, aMess);
				}

				lMesDao.stop();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "", daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoRicercaPerUfficio: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "", ex);
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoRicercaPerUfficio: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggi;
	}

	/**
	 * RIcerca dei messaggi di tipo Esito
	 *
	 * @param aUfficio
	 * @param aTipoOperazione
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountEsitoRicercaPerUfficio(String aUfficio, String aTipoOperazione)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.getCountMesaggiPerRicerca(aUfficio, aTipoOperazione);
			lMesDao.start();
			lMesDao.next();
			lCount = lMesDao.getBigDecimal("HowManyRecords");
			lMesDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExGetCountEsitoRicercaPerUfficio: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			throw new F3BException("MessaggioController.ExGetCountEsitoRicercaPerUfficio: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lCount;
	}

	/**
	 * RIcerca dei messaggi di tipo Esito
	 *
	 * @param aUfficio
	 * @param aTipoOperazione
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountMessaggioRichiestaRicercaSoggettoPerUfficioPaged(String aUfficioMittente,
			String aCodiceUtente, String aTipoOperazione) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.getCountMessaggioRichiestaRicercaSoggettoPerUfficioPaged(aUfficioMittente, aCodiceUtente,
					aTipoOperazione);
			lMesDao.start();
			lMesDao.next();
			lCount = lMesDao.getBigDecimal("HowManyRecords");
			lMesDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MessaggioController.ExGetCountMessaggioRichiestaRicercaSoggettoPerUfficioPaged: "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"MessaggioController.ExGetCountMessaggioRichiestaRicercaSoggettoPerUfficioPaged: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lCount;
	}

	/**
	 * Ricerca Messaggi
	 *
	 * @param aMessaggio
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggiRicevutiPaged(MessaggioModel aMessaggio,
			Vector<String> aListaTipoOperazione, Date aDataInizio, Date aDataFine, int aPage)
			throws F3BException {

		siesLogger.debug(" Inizio ExRicercaMessaggiRicevutiPaged...");
		Connection lConn = null;
		Vector lMessaggi = null;
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.RicercaMessaggiRicevutiPaged(aMessaggio, aListaTipoOperazione, aDataInizio, aDataFine,
					aPage);
			lMessaggi = new Vector(lMesDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error(this.getClass().getName(), daoEx);
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggiRicevutiPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		return lMessaggi;
	}

	public BigDecimal ExCountMessaggiRicevutiPaged(MessaggioModel aMessaggio,
			Vector<String> aListaTipoOperazione, Date aDataInizio, Date aDataFine, int aPage)
			throws F3BException {

		// siesLogger.debug(" Inizio ExCountMessaggiRicevutiPaged...");
		Connection lConn = null;

		BigDecimal HowManyRecords = null;
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.RicercaMessaggiRicevutiPaged(aMessaggio, aListaTipoOperazione, aDataInizio, aDataFine,
					aPage);

			lMesDao.start();
			lMesDao.next();
			HowManyRecords = lMesDao.getBigDecimal("HowManyRecords");
		} catch (DAOException daoEx) {
			siesLogger.error(this.getClass().getName(), daoEx);
			throw new F3BException(
					"MessaggioController.ExCountMessaggiRicevutiPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	}

	public Vector ExRicercaMessaggio(MessaggioModel aMessaggio) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggio(aMessaggio);
			lMessaggii = new Vector(lMesDao.getModels());
			/*
			 * if (lMessaggii.size() == 0) { throw new F3BException(F3BException.USER_MESSAGE,
			 * "Nessun Elemento trovato"); }
			 */
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName(), daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;

	}

	/**
	 * Ricerca Cancella Messaggio Esito By Correlation Id
	 *
	 * @param aCorrelationID
	 * @param aBDIMittente
	 * @throws F3BException
	 */
	public void ExRicercaCancellaMessaggioEsitoByCorrelationId(String aCorrelationID, String aBDIMittente)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		MessaggioSqlDAO lMesDao = null;
		MessaggioModel lMesMod;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioPerCorrreBDIId(aCorrelationID, aBDIMittente);
			lMesMod = (MessaggioModel) lMesDao.getModelByKey();
			lMesDao.stop();
			if (lMesMod != null) {
				lMesDao.cancellaEsitoNonSpedito(aCorrelationID, aBDIMittente);
				lMesDao.start();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "", daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggio: Non posso leggere : " + daoEx);
		} catch (Exception oEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "", oEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggio: Non posso leggere : " + oEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	public MessaggioModel ExRicercaMessaggioByCorrelationId(String aKey) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		MessaggioSqlDAO lMesDao = null;
		MessaggioModel lMesMod;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioPerJmsId(aKey);
			lMesMod = (MessaggioModel) lMesDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("MessaggioController.ExRicercaMessaggio: Non posso leggere : " + daoEx);
		} catch (Exception oEx) {
			throw new F3BException("MessaggioController.ExRicercaMessaggio: Non posso leggere : " + oEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMesMod;
	}

	/**
	 * Ricerca un singolo MEssaggio di richiesta spedito a diverse BDI
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExRicercaMessaggioByKeyMultipleBDI(BigDecimal aKey) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		MessaggioSqlDAO lMesDao = null;
		MessaggioModel lMesMod;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioByKey(aKey);
			lMesMod = (MessaggioModel) lMesDao.getModelByKey();
			lMesDao.stop();
			// Ricerco messagi correlati al messaggio di ricerca
			lMesDao.ricercaMessaggioPerJmsIdOnly(aKey.toString());

			if (!lMesMod.getDescrBdiDestinataria().equals("TUTTE")) { // Messaggio inviato ad una sola BDI
				MessaggioModel lMessReturn = (MessaggioModel) lMesDao.getModelByKey();

				if (lMessReturn != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Sono in Ricerca effettuata su una sola BDI");

					// --- Se il Codice è NOn spedito il messaggio viene reinviato
					if (lMessReturn.getCodEsito().equals(NON_SPEDITO)) {
						// Rispedisco Messaggio non spedito...
						ManageMessageNoSend lMan = new ManageMessageNoSend();
						lMan.rispedisciMessaggioNonSpedito(lMesMod, lMessReturn);
					}
					// aggiornaContatore(lMesTemp.getCodEsito(), lContatore);
					lMesMod.setMessaggioCorrelato(lMessReturn);
				}
			} else // Multiple BDI
			{
				// Vettore di Messagi di Risposta
				Vector lMessCorrelati = new Vector(lMesDao.getModels());
				ContatoreEsitiModel lContatore = new ContatoreEsitiModel();

				if (lMessCorrelati != null) {
					Iterator lItx = lMessCorrelati.iterator();

					Vector lVectNew = new Vector();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Sono in Ricerca effettuata su più BDI");
					while (lItx.hasNext()) {
						MessaggioModel lMesTemp = new MessaggioModel();
						lMesTemp = (MessaggioModel) lItx.next();

						// --- Se il Codice è Non spedito il messaggio viene reinviato
						if (lMesTemp.getCodEsito().equals(NON_SPEDITO)) {
							// GDV --- Spedisco Messaggio non spedito...
							ManageMessageNoSend lMan = new ManageMessageNoSend();
							MessaggioModel lMessDaRispedire = new MessaggioModel(lMesMod);
							lMan.rispedisciMessaggioNonSpedito(lMessDaRispedire, lMesTemp);

						}

						aggiornaContatore(lMesTemp.getCodEsito(), lContatore);
						lVectNew.add(lMesTemp);
					}

					lContatore.calcolaBDIInAttesa();
					// lContatore.calcolaBDINonAttive();
					lVectNew.add(lContatore);
					lMesMod.setMessaggiCorrelati(lVectNew);
				} // Fine ricerca su piu' BDI
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggioByKey: Non posso leggere : " + daoEx);
		} catch (Exception oEx) {
			oEx.printStackTrace();
			throw new F3BException("MessaggioController.ExRicercaMessaggioByKey: Non posso leggere : " + oEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMesMod;
	}

	/**
	 * ExRicercaMessaggioByKey
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExRicercaMessaggioByKey(BigDecimal aKey) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		MessaggioSqlDAO lMesDao = null;
		MessaggioModel lMesMod;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioByKey(aKey);
			lMesMod = (MessaggioModel) lMesDao.getModelByKey();
			lMesDao.stop();

			lMesDao.ricercaMessaggioPerJmsIdOnly(aKey.toString());
			MessaggioModel lMessCorrelato = (MessaggioModel) lMesDao.getModelByKey();

			if (lMessCorrelato != null) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("[JMS]: lMessCorrelato : " + lMessCorrelato.toString());

				lMesMod.setMessaggioCorrelato(lMessCorrelato);

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("[JMS]: lMesMod : " + lMesMod.toString());

				if (lMessCorrelato.getCodEsito().equals(NON_SPEDITO)) {
					// Spedisco Messaggio non spedito...
					ManageMessageNoSend lMan = new ManageMessageNoSend();
					lMan.rispedisciMessaggioNonSpedito(lMesMod, lMessCorrelato);
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggioByKey: Non posso leggere : " + daoEx);
		} catch (Exception oEx) {
			throw new F3BException("MessaggioController.ExRicercaMessaggioByKey: Non posso leggere : " + oEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMesMod;
	}

	public MessaggioModel ExModificaMessaggio(MessaggioModel aMessaggio) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		MessaggioDAO lMesDao = null;
		MessaggioModel lMesMod = new MessaggioModel(aMessaggio);

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioDAO(lConn);
			lMesDao.setDAOFromModelForUpdate(aMessaggio);
			lMesDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex, ex);
			throw new F3BException("MessaggioController.ExModifica: Non posso inserire: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqe, sqe);
			throw new F3BException(
					"MessaggioController.ExModificaMessaggio: Non posso inserire il soggetti : " + sqe);
		} catch (Exception exc) {
			rollback(lConn);
			siesLogger.error("Exception: " + exc, exc);
			throw new F3BException(
					"MessaggioController.ExModificaMessaggio: Non posso inserire il soggetti : " + exc);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMesMod;
	}

	public void ExCancellaMessaggio(BigDecimal aKeyMessaggio) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		MessaggioDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioDAO(lConn);
			lMesDao.setCondizioneUpdate(aKeyMessaggio);
			lMesDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExCancellaMessaggio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	/**
	 * Modifica Messaggio Visto
	 *
	 * @param aMessaggio
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExModificaMessaggioVisto(MessaggioModel aMessaggio) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		MessaggioDAO lMesDao = null;
		MessaggioModel lMesMod = new MessaggioModel(aMessaggio);

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioDAO(lConn);
			// lMesDao.setDAOFromModelForUpdate(aMessaggio);

			lMesDao.setFlagVisto(aMessaggio.getFlagVisto());
			// lMesDao.setIdMessaggio(aMessaggio.getIdMessaggio());

			lMesDao.setCondizioneUpdate(aMessaggio.getIdMessaggio());

			lMesDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "DAOException: " + ex);
			throw new F3BException("MessaggioController.ExModifica: Non posso inserire: " + ex);
		} catch (Exception exc) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "SQLException: " + exc);
			throw new F3BException(
					"MessaggioController.ExModificaMessaggio: Non posso inserire il soggetti : " + exc);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMesMod;
	}

	/**
	 * Modifica Messaggio con l'ID JMS
	 *
	 * @param aMessaggio
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExModificaMessaggioJMSId(MessaggioModel aMessaggio) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio Modifica Messaggio");

		Connection lConn = null;

		MessaggioDAO lMesDao = null;
		MessaggioModel lMesMod = new MessaggioModel(aMessaggio);

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioDAO(lConn);
			lMesDao.setJmsIdMessage(aMessaggio.getJmsIdMessaggio());
			lMesDao.setJmsCorrelationIdMessage(aMessaggio.getJmsCorrelationIdMessage());
			lMesDao.setCondizioneUpdate(aMessaggio.getIdMessaggio());

			if (aMessaggio.getIdRichiesta() != null)
				lMesDao.setIdRichiesta(aMessaggio.getIdRichiesta());

			lMesDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "DAOException: " + ex);
			throw new F3BException(
					"MessaggioController.ExModificaMessaggioJMSId: Non posso aggiornare la tabella MESSAGGIO: "
							+ ex);
		} catch (Exception exc) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "SQLException: " + exc);
			throw new F3BException(
					"MessaggioController.ExModificaMessaggioJMSId: Non posso aggiornare la tabella MESSAGGIO : "
							+ exc);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMesMod;
	}

	/**
	 * Selezione dei messaggi destinati a aUfficioRicevente da parte di aUfficioMittente
	 *
	 * @param aUfficioRicevente
	 * @param aUfficioMittente
	 * @param aTipoOperazione
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioRichiestaPerUffici(String aUfficioRicevente, String aUfficioMittente,
			String aTipoOperazione) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioRichiestaPerUfficiTipoOperazione(aUfficioRicevente, aUfficioMittente,
					aTipoOperazione);

			lMessaggii = new Vector(lMesDao.getModels());

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Non vi sono atti da prendere in carico");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioRichiestaPerUffici: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * Selezione dei messaggi destinati a aUfficioRicevente da parte di aUfficioMittente a prescindere dal
	 * Tipo Operazione.
	 *
	 * @param aUfficioRicevente
	 * @param aUfficioMittente
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggiPerUffici(String aUfficioRicevente, String aUfficioMittente)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggiRichiestaPerUffici(aUfficioRicevente, aUfficioMittente);

			lMessaggii = new Vector(lMesDao.getModels());

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non vi sono atti SIUS da prendere in carico");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggiRichiestaPerUffici: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * Selezione dei messaggi destinati a aUfficioRicevente da parte di aUfficioMittente
	 *
	 * @param aUfficioRicevente
	 * @param aUfficioMittente
	 * @param aDataInizioTrasmissione
	 * @param aDataFineTrasmissione
	 * @param aTipoOperazione
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioRichiestaPerDateUffici(String aUfficioRicevente, String aUfficioMittente,
			Date aDataInizioTrasmissione, Date aDataFineTrasmissione, String aIncludeInCarico)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggiRichiestaPerDateUffici(aUfficioRicevente, aUfficioMittente,
					aDataInizioTrasmissione, aDataFineTrasmissione, aIncludeInCarico);

			lMessaggii = new Vector(lMesDao.getModels());

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Non vi sono atti da prendere in carico");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioRichiestaPerUffici: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * Selezione dei messaggi destinati a aUfficioRicevente da parte di aUfficioMittente per Anno/Numero
	 * Fascicolo (SIEP se Istanza, SIUS se Decreto/Ordinanza)
	 *
	 * @param aUfficioRicevente
	 * @param aUfficioMittente
	 * @param aTipoOperazione
	 * @param aAnnoFascicolo
	 * @param aProgrFascicolo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioRichiestaPerTipoOperazioneFascicolo(String aUfficioRicevente,
			String aUfficioMittente, String aTipoOperazione, BigDecimal aAnnoFascicolo,
			BigDecimal aProgrFascicolo, String aIncludeInCarico) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioRichiestaPerTipoOperazioneFascicolo(aUfficioRicevente, aUfficioMittente,
					aTipoOperazione, aAnnoFascicolo, aProgrFascicolo, aIncludeInCarico);

			lMessaggii = new Vector(lMesDao.getModels());

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Non vi sono atti da prendere in carico");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioRichiestaPerUffici: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * Selezione dei messaggi destinati a aUfficioRicevente da parte di aUfficioMittente per Anno/Numero
	 * Fascicolo (SIEP se Istanza, SIUS se Decreto/Ordinanza)
	 *
	 * @param aUfficioRicevente
	 * @param aUfficioMittente
	 * @param aTipoOperazione
	 * @param aAnnoFascicolo
	 * @param aProgrFascicolo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioRichiestaPerTipoOperazioneFascicolo(MessaggioModel aMessaggio,
			String aIncludeInCarico) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggi = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioRichiestaPerTipoOperazioneFascicolo(aMessaggio, aIncludeInCarico);

			lMessaggi = new Vector(lMesDao.getModels());

			if (lMessaggi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Non vi sono atti da prendere in carico");

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioRichiestaPerUffici: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggi;
	}

	/**
	 * Selezione dei messaggi destinati a aUfficioRicevente per Tipo Operazione e Date ricezione (UEPE).
	 *
	 * @param aUfficioRicevente
	 * @param aTipoOperazione
	 * @param aDataInizioTrasmissione
	 * @param aDataFineTrasmissione
	 * @param aIncludeVisto
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioRichiestaPerTipoeDate(String aUfficioRicevente, String aTipoOperazione,
			Date aDataInizioTrasmissione, Date aDataFineTrasmissione, String aIncludeVisto)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggiRichiestaPerTipoeDate(aUfficioRicevente, aTipoOperazione,
					aDataInizioTrasmissione, aDataFineTrasmissione, aIncludeVisto);

			lMessaggii = new Vector(lMesDao.getModels());

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Non vi sono atti da elencare");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioRichiestaPerTipoeDate: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * Selezione dei messaggi destinati a aUfficioRicevente per Soggetto e Tipo Operazione (UEPE).
	 *
	 * @param aUfficioRicevente
	 * @param aCognomeSoggetto
	 * @param aNomeSoggetto
	 * @param aCodComuneNascita
	 * @param aCodStatoNascita
	 * @param aDataNascita
	 * @param aTipoOperazione
	 * @param aIncludeVisto
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioRichiestaPerSoggetto(String aUfficioRicevente, String aCognomeSoggetto,
			String aNomeSoggetto, String aCodComuneNascita, String aCodStatoNascita, Date aDataNascita,
			String aTipoOperazione, String aIncludeVisto) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggiRichiestaPerSoggetto(aUfficioRicevente, aCognomeSoggetto, aNomeSoggetto,
					aCodComuneNascita, aCodStatoNascita, aDataNascita, aTipoOperazione, aIncludeVisto);
			lMessaggii = new Vector(lMesDao.getModels());

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Non vi sono atti da elencare");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioRichiestaPerSoggetto: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * Selezione dei messaggi destinati a aUfficioRicevente da parte di aUfficioMittente di un particolare
	 * Tipo Operazione.
	 *
	 * @param aUfficioRicevente
	 * @param aUfficioMittente
	 * @param aTipoOperazione
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggiPerUfficioTipoOper(String aUfficioMittente, String aTipoOperazione)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggiRichiestaPerUfficioTipoOper(aUfficioMittente, aTipoOperazione);

			lMessaggii = new Vector(lMesDao.getModels());

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun messaggio trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggiRichiestaPerUfficioTipo: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * funzione per aggiornare il COntatore del risultato delle spedizioni alle varie BDI
	 *
	 * @param aEsito
	 * @param aContatore
	 */
	private void aggiornaContatore(String aEsito, ContatoreEsitiModel aContatore) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.error(getClass().getName() + "Calcola Esito = " + aEsito);
		if (aEsito.equals(NON_TROVATO)) {
			aContatore.setNonTrovati(aContatore.getNonTrovati() + 1);
		}
		if (aEsito.equals(TROVATO)) {
			aContatore.setTrovati(aContatore.getTrovati() + 1);
		}
		if (aEsito.equals(DESTINAZIONE_NON_RAGGIUNGIBILE)) {
			aContatore.setNonSpediti(aContatore.getNonSpediti() + 1);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	/**
	 * Ricerca i Messaggi di RICHIESTA (01) inviati da un certo Ufficio corrispondenti ai criteri di ricerca
	 * specificati. Per ogni messaggio trovato verifica se presente la risposta
	 *
	 * @param aUfficio
	 *            - Cod ufficio che ha inviato la richiesta
	 * @param aUtente
	 *            - Eventuale filtro sull'utente
	 * @param aEsito
	 *            - Eventuale filtro sull'esito (
	 * @param aTipoOperazione
	 * @param dataRicercaInizio
	 * @param dataRicercaFine
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioEsitoConFiltri(String aUfficio, String aUtente, String aEsito,
			String aTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioRichiestaConFiltri(aUfficio, aUtente, aEsito, aTipoOperazione,
					dataRicercaInizio, dataRicercaFine);
			lMessaggii = new Vector(lMesDao.getModels());
			lMesDao.stop();

			// if (lMessaggii.size() == 0)
			// {
			// throw new F3BException(F3BException.USER_MESSAGE, "Nessun Messaggio di Esito in Attesa");
			// }

			// Collego ai Messaggi di tipo Esito trovati il Rispettivo Messaggio di Richiesta.
			// Per ogni messaggio di richiesta (01) cerco se presente il relativo messaggio
			// di risposta (02,04).
			// In funzione del tipo di esito richiesto (aEsito) verifico se restituire o
			// meno il messaggio alla funziione di ricerca.
			for (int i = 0; i < lMessaggii.size(); i++) {
				MessaggioModel lMess = (MessaggioModel) lMessaggii.get(i);

				lMesDao.ricercaMessaggioPerJmsId(lMess.getIdMessaggio().toString());
				Vector lVect = new Vector(lMesDao.getModels());

				// Messaggio che ha ricevuto risposta.
				if (lVect.size() > 0) {
					// Richiesta Messaggi in attesa di risposta.
					if (aEsito != null && aEsito.compareTo("10000") == 0) {
						lMessaggii.removeElementAt(i);
						i--;
					}
					// Richiesta Tutti gli esiti o Esito specificato.
					else if (aEsito == null || (aEsito != null && (aEsito
							.compareTo(((MessaggioModel) lVect.firstElement()).getCodEsito())) == 0)) {
						lMess.setMessaggioCorrelato((MessaggioModel) lVect.firstElement());
						lMessaggii.set(i, lMess);
					}
					// Esito richiesto non corrisponde: eliminazione.
					else {
						lMessaggii.removeElementAt(i);
						i--;
					}
				}
				// Richiesta senza risposta.
				else {
					// Eliminato il messaggio se non è Richiesto Tutti gli esiti o in attesa di risposta.
					if (!(aEsito == null || aEsito.compareTo("10000") == 0)) {
						lMessaggii.removeElementAt(i);
						i--;
					}
				}

				lMesDao.stop();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoConFiltri: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + ex);
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoConFiltri: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(">>>>>>>>>>> lMessaggii.size = " + lMessaggii.size());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * Ricerca Messaggio Esito con filtri.
	 *
	 * @param aUfficio
	 * @param aUtente
	 * @param aEsito
	 * @param aTipoOperazione
	 * @param dataRicercaInizio
	 * @param dataRicercaFine
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioEsitoRicercaFascConFiltri(String aAnnoSiep, String aProgrSiep,
			String aUfficio, String aUfficioDestinatario, String aUtente, String aEsito,
			String aTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine, int aPage)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesSqlDao = null;

		try {
			lConn = getDBConnection();
			lMesSqlDao = new MessaggioSqlDAO(lConn);
			// lMesDao.ricercaMessaggioRichiestaConFiltri(aUfficio, aUtente, aEsito, aTipoOperazione,
			// dataRicercaInizio, dataRicercaFine);
			lMesSqlDao.ricercaMessaggioEsitoRicercaFascConFiltri(aAnnoSiep, aProgrSiep, null, aUfficio,
					aUfficioDestinatario, aUtente, aEsito, aTipoOperazione, dataRicercaInizio,
					dataRicercaFine, aPage);

			lMessaggii = new Vector(lMesSqlDao.getModels());
			lMesSqlDao.stop();

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Messaggio di Esito in Attesa");
			}

			// Collego ai Messaggi di tipo Esito trovati il Rispettivo Messaggio di Richiesta
			for (int i = 0; i < lMessaggii.size(); i++) {
				MessaggioModel aMess = (MessaggioModel) lMessaggii.get(i);

				lMesSqlDao.ricercaMessaggioPerJmsId(aMess.getIdMessaggio().toString());
				Vector lVect = new Vector(lMesSqlDao.getModels());

				// Messaggio che ha ricevuto risposta.
				if (lVect.size() > 0) {
					// Richiesta Messaggi in attesa di risposta.
					if (aEsito != null && aEsito.compareTo("10000") == 0) {
						lMessaggii.removeElementAt(i);
						i--;
					}
					// Richiesta Tutti gli esiti o Esito specificato.
					else if (aEsito == null || (aEsito != null && (aEsito
							.compareTo(((MessaggioModel) lVect.firstElement()).getCodEsito())) == 0)) {
						aMess.setMessaggioCorrelato((MessaggioModel) lVect.firstElement());
						lMessaggii.set(i, aMess);

						// Si Rispedisce la richiesta.
						if (aMess.getMessaggioCorrelato().getCodEsito().equals(NON_SPEDITO)) {
							ManageMessageNoSend lMan = new ManageMessageNoSend();
							lMan.rispedisciMessaggioNonSpedito(aMess, aMess.getMessaggioCorrelato());
						}
					}
					// Esito richiesto non corrisponde: eliminazione.
					else {
						lMessaggii.removeElementAt(i);
						i--;
					}
				}
				// Messaggio in attesa di risposta.
				else {
					// Eliminato il messaggio se non è Richiesto Tutti gli esiti o in attesa di risposta.
					if (!(aEsito == null || aEsito.compareTo("10000") == 0)) {
						lMessaggii.removeElementAt(i);
						i--;
					}
				}

				lMesSqlDao.stop();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggioEsitoRicercaFascConFiltri: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: ", ex);
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoRicercaFascConFiltri: " + ex);
		} finally {
			cleanup(lMesSqlDao);

			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(">>>>>>>>>>> lMessaggii.size = " + lMessaggii.size());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * Ricerca Messaggio Esito Istanze con filtri.
	 *
	 * @param aUfficio
	 * @param aUtente
	 * @param aEsito
	 * @param aTipoUfficio
	 * @param aSedeUfficio
	 * @param dataRicercaInizio
	 * @param dataRicercaFine
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioEsitoConFiltri(String aCodTipoOper, String aUfficio, String aUtente,
			String aEsito, String aCodUffDest, String aAnnoSiep, String aProgrSiep, Date dataRicercaInizio,
			Date dataRicercaFine) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioConFiltri(aCodTipoOper, aUfficio, aUtente, aEsito, aCodUffDest, aAnnoSiep,
					aProgrSiep, dataRicercaInizio, dataRicercaFine);
			lMessaggii = new Vector(lMesDao.getModels());
			lMesDao.stop();

			if (lMessaggii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Messaggio di Esito in Attesa");
			}

			// Collego ai Messaggi di tipo Esito trovati il Rispettivo Messaggio di Richiesta
			for (int i = 0; i < lMessaggii.size(); i++) {
				MessaggioModel aMess = (MessaggioModel) lMessaggii.get(i);

				lMesDao.ricercaMessaggioPerJmsId(aMess.getIdMessaggio().toString());
				Vector lVect = new Vector(lMesDao.getModels());

				// Messaggio che ha ricevuto risposta.
				if (lVect.size() > 0) {
					// Richiesta Messaggi in attesa di risposta.
					if (aEsito != null && aEsito.compareTo("10000") == 0) {
						lMessaggii.removeElementAt(i);
						i--;
					}
					// Richiesta Tutti gli esiti o Esito specificato.
					else if (aEsito == null || (aEsito != null && (aEsito
							.compareTo(((MessaggioModel) lVect.firstElement()).getCodEsito())) == 0)) {
						aMess.setMessaggioCorrelato((MessaggioModel) lVect.firstElement());
						lMessaggii.set(i, aMess);
					}
					// Esito richiesto non corrisponde: eliminazione.
					else {
						lMessaggii.removeElementAt(i);
						i--;
					}
				}
				// Messaggio in attesa di risposta.
				else {
					// Eliminato il messaggio se non è Richiesto Tutti gli esiti o in attesa di risposta.
					if (!(aEsito == null || aEsito.compareTo("10000") == 0)) {
						lMessaggii.removeElementAt(i);
						i--;
					}
				}

				lMesDao.stop();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + ex);
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(">>>>>>>>>>> lMessaggii.size = " + lMessaggii.size());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	/**
	 * Ricerca Messaggio Esito con filtri (x soggetto). restituisce un Vettore di Messaggi Esiti Correlati e
	 * l'ultimo Model è un ContatoreEsitiModel che riassume i dati esiti ricevuti per la paginazione.
	 *
	 * @param aUfficioMittente
	 * @param aCodiceUtente
	 * @param aEsito
	 * @param aTipoOperazione
	 * @param dataRicercaInizio
	 * @param dataRicercaFine
	 * @return lMessaggi
	 * @throws F3BException
	 */
	public Vector ExRicercaMessaggioEsitoRicercaSoggettoPerUfficioPaged(String aUfficioMittente,
			String aCodiceUtente, String aTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine,
			int aPage) throws F3BException, Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		Vector lMessaggi = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("* * * Sono in ExRicercaMessaggioEsitoRicercaSoggettoPerUfficioPaged ");

			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioRichiestaRicercaSoggettoPerUfficioPaged(aUfficioMittente, aCodiceUtente,
					aTipoOperazione, dataRicercaInizio, dataRicercaFine, aPage);

			lMessaggi = new Vector(lMesDao.getModels());
			lMesDao.stop();

			if (lMessaggi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Messaggio di Ricerca in Attesa");
			}

			// Collego ai Messaggi di tipo Esito trovati il Rispettivo Messaggio di Richiesta
			for (int i = 0; i < lMessaggi.size(); i++) {
				MessaggioModel aMess = (MessaggioModel) lMessaggi.get(i);

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.info("Vediamo sto messaggio = " + aMess.getCodBdiDestinataria());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("* * * messaggio = " + aMess);
				// Se siamo nel caso si ricerca presso una sola BDI
				if (aMess.getCodBdiDestinataria().compareTo(ICostantiJMS.COD_TUTTE) != 0) {
					lMesDao.ricercaMessaggioEsitoRicercaPerJmsId(aMess.getIdMessaggio().toString());
					MessaggioModel lMessEsito = (MessaggioModel) lMesDao.getModelByKey();
					if (lMessEsito != null) {
						aMess.setMessaggioCorrelato(lMessEsito);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("* * * messaggio Esito= " + lMessEsito);
					}
					lMesDao.stop();
				} else { // caso BDI multiple
					lMesDao.ricercaMessaggioEsitoRicercaPerJmsId(aMess.getIdMessaggio().toString());
					Vector lVect = new Vector(lMesDao.getModels());
					lMesDao.stop();
					ContatoreEsitiModel lContatore = new ContatoreEsitiModel();
					Vector lVectTemp = new Vector();

					if (lVect.size() > 0) {

						for (int y = 0; y < lVect.size(); y++) {
							MessaggioModel lModelTemp = (MessaggioModel) lVect.get(y);
							aggiornaContatore(lModelTemp.getCodEsito(), lContatore);
							lVectTemp.add(lModelTemp);

							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("* * * Trovato un Esito = " + lModelTemp.getCodEsito()
									+ lModelTemp.getIdMessaggio());
							// Se non partito rispedisco...
						}

						lContatore.calcolaBDIInAttesa();
						lVectTemp.add(lContatore);
						aMess.setContatoreEsiti(lContatore);
						aMess.setMessaggiCorrelati(lVectTemp);
					} else // Nessun esito... Tutte le BDI sono inattive
					{
						lContatore.calcolaBDIInAttesa();
						lVectTemp.add(lContatore);
						aMess.setMessaggiCorrelati(lVectTemp);
					}
				}

				lMessaggi.set(i, aMess);
			}

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggioEsitoRicercaSoggettoPerUfficioPaged: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMessaggi;
	}

	/**
	 * Metodo per evitare il loop del listner sui messaggi non spediti.
	 *
	 * @param aMessage
	 * @return
	 * @throws F3BException
	 */
	public boolean ExRicercaMessaggioUgualeNonSpedito(MessaggioModel aMessage) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		boolean lRetMessaggio = true;
		MessaggioSqlDAO lMesDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);

			lMesDao.ricercaMessaggioUgualeNonSpedito(aMessage);
			lMesDao.start();
			while (lMesDao.next()) {
				BigDecimal lIdMess = lMesDao.getBigDecimal("ID_MESSAGGIO");
				if (lIdMess != null && lIdMess.intValue() > 0) {
					lRetMessaggio = false; // Trovato messaggio uguale
				}
			}
			lMesDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExRicercaMessaggioUgualeNonSpedito: " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("Restituisci " + lRetMessaggio);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lRetMessaggio;
	}
	// SIEPE

	/**
	 * Ricerca dei contatori visualizzati nel "Cruscotto" del SIEPE. La funzione restituisce un Vector di
	 * CruscottoModel, in ogni elemento sono memorizzati il numero di messaggi ricevuti nella stessa data e
	 * classificati in base allo stato: Nuovo, Preso in Visione, Preso in carico.
	 *
	 * @param acodUfficioDestinatario
	 * @param aOrd
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaContatoriXCruscotto(String acodUfficioDestinatario, String aOrd)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		MessaggioSqlDAO lMesDao = null;
		Vector lRetVect = null;
		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaXCruscotto(acodUfficioDestinatario, aOrd);

			lMesDao.start();
			lRetVect = new Vector();
			CruscottoModel lNuovo, lUltimo;
			while (lMesDao.next()) {
				// Lettura dei dati
				lNuovo = lMesDao.getCruscottoModel();

				if (lRetVect.size() == 0 || ((CruscottoModel) lRetVect.lastElement()).getDataRicezione()
						.compareTo(lNuovo.getDataRicezione()) != 0) {
					// Se l'ultimo record letto aveva data diversa da quello appena letto
					// o se siamo alla prima lettura si aggiunge un nuovo elemento alla lista.
					lRetVect.add(new CruscottoModel(lNuovo.getDataRicezione()));
				}

				// L'aggiornamento dei contatori nell'ultimo elemento inserito
				lUltimo = (CruscottoModel) lRetVect.lastElement();

				// Aggiornamento
				String lFlag = lNuovo.getMessage();
				if (lFlag.equalsIgnoreCase("N"))
					lUltimo.setINumAttiNuovi(lNuovo.getNumAttiRicevuti());
				else if (lFlag.equalsIgnoreCase("V"))
					lUltimo.setINumAttiPresiInVisione(lNuovo.getNumAttiRicevuti());
				else if (lFlag.equalsIgnoreCase("S"))
					lUltimo.setINumAttiPresiInCarico(lNuovo.getNumAttiRicevuti());
				else if (lFlag.equalsIgnoreCase("R"))
					lUltimo.setINumAttiRestituiti(lNuovo.getNumAttiRicevuti());
				else
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error(
							"Errore nei dati: nella tabella MESSAGGIO rilevato un record con FLAG_VISTO di codice non previsto ->"
									+ lFlag);

				// Aggiornamento del Totale
				lUltimo.setINumAttiRicevuti(lUltimo.getNumAttiNuovi().add(lUltimo.getNumAttiPresiInCarico()
						.add(lUltimo.getNumAttiPresiInVisione().add(lUltimo.getNumAttiRestituiti()))));
			}
			lMesDao.stop();
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			throw new F3BException("MessaggioController.ExRicercaContatoriXCruscotto: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lRetVect;
	}

	/**
	 *
	 * @param aIdMessaggio
	 * @return
	 * @throws F3BException
	 */
	public Vector<MessaggioModel> ExRicercaMessaggiCorrelati(String aIdMessaggio) throws F3BException {

		Connection lConn = null;

		MessaggioSqlDAO lMesSqlDao = null;

		Vector<MessaggioModel> lListaMessaggi;

		try {
			lConn = getDBConnection();

			lMesSqlDao = new MessaggioSqlDAO(lConn);

			lMesSqlDao.ricercaEsitiRicevutiByIdMessaggi(aIdMessaggio);

			lListaMessaggi = new Vector(lMesSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggiCorrelati: Non posso leggere : " + daoEx);
		} catch (Exception oEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggiCorrelati: Non posso leggere : " + oEx);
		} finally {
			cleanup(lMesSqlDao);
			cleanup(lConn);
		}

		return lListaMessaggi;
	}

	/**
	 * ExRicercaMessaggiByIdRichiesta : Ricerca di Messaggi di "Risposta" Oltre agli altri parametri, la
	 * ricerca è fatta per IdRichiesta che deve essere uguale a IdMessag della "Richiesta"
	 */
	// String adeliveryMod 00001 = Inviato, 00002 = Ricevuto
	public MessaggioModel ExRicercaMessaggioByIdRichiesta(String aTipoMes // 01 = RICHIESTA
			, String aTipoOperazione // 00066 = TRASFERIMENTO_COMPETENZA
			, String aCodUffMittenete, BigDecimal aIdRichiesta) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;
		MessaggioSqlDAO lMesDao = null;
		MessaggioModel lMesMod;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioByIdRichiesta(aTipoMes, aTipoOperazione, aCodUffMittenete, aIdRichiesta);

			lMesMod = (MessaggioModel) lMesDao.getModelByKey();

			lMesDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggioByIdRichiesta: Non posso leggere : " + daoEx);
		} catch (Exception oEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggioByIdRichiesta: Non posso leggere : " + oEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lMesMod;
	}

	/**
	 * ExRicercaSollecitiMessaggioRichiestaAtti : Ricerca Messaggi di "Sollecito Richiestra Atti Per
	 * trasferimento Competenza" Oltre agli altri parametri, la ricerca è fatta per IdRMessaggioSollecitato"
	 */

	public Vector<MessaggioModel> ExRicercaSollecitiMessaggioRichiestaAtti(String aTipoMes // 01 = RICHIESTA
			, String aTipoOperazione // 00076 = SOLLECITO Richiesta Atti per Tr. Comp.
			, String aIdMesSollecitato) throws F3BException {

		Connection lConn = null;
		MessaggioSqlDAO lMesSqlDao = null;
		Vector<MessaggioModel> lListaSolleciti;
		try {
			lConn = getDBConnection();
			lMesSqlDao = new MessaggioSqlDAO(lConn);
			lMesSqlDao.RicercaSollecitiMessaggioRichiestaAtti(aTipoMes, aTipoOperazione, aIdMesSollecitato);
			lListaSolleciti = new Vector(lMesSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaSollecitiMessaggioRichiestaAtti: Non posso leggere : "
							+ daoEx);
		} catch (Exception oEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaSollecitiMessaggioRichiestaAtti: Non posso leggere : "
							+ oEx);
		} finally {
			cleanup(lMesSqlDao);
			cleanup(lConn);
		}

		return lListaSolleciti;

	}

	// Seguito Atti
	public MessaggioModel ExRicercaMessaggioByCorrelationIdOnly(String aKey) throws F3BException {

		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		MessaggioSqlDAO lMesDao = null;
		MessaggioModel lMesMod;

		try {
			lConn = getDBConnection();

			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioPerJmsIdOnly(aKey);
			lMesMod = (MessaggioModel) lMesDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggioByCorrelationIdOnly: Non posso leggere : "
							+ daoEx);
		} catch (Exception oEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggioByCorrelationIdOnly: Non posso leggere : " + oEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		siesLogger.info("[JMS]: fine");
		return lMesMod;
	}

	public Vector ExRicercaMessaggiNelPeriodoPaged(MessaggioModel aMessaggio, Date aDataInizio,
			Date aDataFine, int aPage) throws F3BException {

		siesLogger.info("[JMS]: inizio");

		Connection lConn = null;

		Vector lMessaggii = new Vector();
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioNelPeriodoPaged(aMessaggio, aDataInizio, aDataFine, aPage);
			lMessaggii = new Vector(lMesDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error(this.getClass().getName(), daoEx);
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggiNelPeriodoPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		siesLogger.info("[JMS]: fine");
		return lMessaggii;
	}

	public BigDecimal ExCountMessaggiNelPeriodoPaged(MessaggioModel aMessaggio, Date aDataInizio,
			Date aDataFine, int aPage) throws F3BException {

		siesLogger.debug("--XX-- ExCountMessaggiNelPeriodoPaged ...... INIZIO  ");

		BigDecimal HowManyRecords = null;
		Connection lConn = null;

		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.ricercaMessaggioNelPeriodoPaged(aMessaggio, aDataInizio, aDataFine, aPage);
			lMesDao.start();
			lMesDao.next();
			HowManyRecords = lMesDao.getBigDecimal("HowManyRecords");
		} catch (DAOException daoEx) {
			siesLogger.error(this.getClass().getName(), daoEx);
			throw new F3BException(
					"MessaggioController.ExCountMessaggiNelPeriodoPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	}

	/**
	 * Ricerca Messaggi di "Sollecito l'Ufficio Cumulante a Emettere il provvedimento di CUMULO" la ricerca è
	 * fatta per IdRMessaggioSollecitato e Tipo Operazione"
	 */
	public Vector<MessaggioModel> ExRicercaMessaggioByIdMessaggioSollecitato(String aTipoOperazione,
			String aIdMesSollecitato) throws F3BException {

		Connection lConn = null;
		MessaggioSqlDAO lMesSqlDao = null;
		Vector<MessaggioModel> lListaSolleciti;
		try {
			lConn = getDBConnection();
			lMesSqlDao = new MessaggioSqlDAO(lConn);
			lMesSqlDao.RicercaSolleciti(aTipoOperazione, aIdMesSollecitato);
			lListaSolleciti = new Vector(lMesSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggioByIdMessaggioSollecitato: Non posso leggere : "
							+ daoEx);
		} catch (Exception oEx) {
			throw new F3BException(
					"MessaggioController.ExRicercaMessaggioByIdMessaggioSollecitato: Non posso leggere : "
							+ oEx);
		} finally {
			cleanup(lMesSqlDao);
			cleanup(lConn);
		}

		return lListaSolleciti;
	}

	 /**
   * Metodo di eliminazione dei messaggi ricevuti o inviati da un certo ufficio in un certo intervallo di date
   * 
   * @since MEV_2024-DNA 
   */
    public void ExCancellaMessaggioByCodUfficio (String aCodUfficio, Date aDataInviaDal, Date aDataInviaAl) 
        throws F3BException
    {  
        Connection lConn = null;
        MessaggioDAO lMesDao = null;
        
        try {
            lConn = getDBConnection();
            
            lMesDao = new MessaggioDAO(lConn);
            lMesDao.setCondizioneByUfficio(aCodUfficio, aDataInviaDal, aDataInviaAl);
            lMesDao.delete();
            
            commit(lConn);
        } catch (DAOException daoEx) {
            siesLogger.error("ExCancellaMessaggioByCodUfficio ", daoEx);
            rollback(lConn);
            throw new F3BException("MessaggioController.ExCancellaMessaggioByCodUfficio: Non posso leggere : "+ daoEx);
        } catch (Exception oEx) {
            siesLogger.error("ExCancellaMessaggioByCodUfficio ", oEx);
            rollback(lConn);
            throw new F3BException("MessaggioController.ExCancellaMessaggioByCodUfficio: Non posso leggere : "+ oEx);
        } finally {
            cleanup(lMesDao);
            cleanup(lConn);
        }

        return;
    }
  
}