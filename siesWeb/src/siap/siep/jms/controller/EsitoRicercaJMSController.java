package siap.siep.jms.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import siap.controller.SiapController;
import siap.jms.ICostantiJMS;
import siap.jms.manage.ManageMessageNoSend;
import siap.jms.messaggio.dao.MessaggioSqlDAO;
import siap.jms.messaggio.model.MessaggioModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 *
 * <p>
 * Title: EsitoRicercaJMSController
 * </p>
 * <p>
 * Description: Calcola gli esiti per le ricerche differite
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EsitoRicercaJMSController extends SiapController implements IEsitoRicercaJMS, ICostantiJMS {

	public MessaggioModel ExRicercaEsitoRicerca(MessaggioModel aMessaggio) throws F3BException {

		return null;
	}

	/*
	 * public Vector ExRicercaMessaggioEsitoRicercaSoggettoPerUfficio(String aUfficioMittente, String
	 * aCodiceUtente, String aTipoOperazione) throws F3BException, Exception { Connection lConn = null; Vector
	 * lMessaggi = new Vector(); MessaggioSqlDAO lMesDao = null;
	 * 
	 * try { lConn = getDBConnection(); lMesDao = new MessaggioSqlDAO(lConn);
	 * lMesDao.ricercaMessaggioRichiestaRicercaSoggettoPerUfficio(aUfficioMittente, aCodiceUtente,
	 * aTipoOperazione);
	 * 
	 * lMessaggi = new Vector(lMesDao.getModels()); lMesDao.stop();
	 * 
	 * if (lMessaggi.size() == 0) { throw new F3BException(F3BException.USER_MESSAGE,
	 * "Nessun Messaggio di Ricerca in Attesa"); }
	 * 
	 * //Collego ai Messaggi di tipo Esito trovati il Rispettivo Messagio di Richiesta for (int i = 0; i <
	 * lMessaggi.size(); i++) { MessaggioModel aMess = (MessaggioModel) lMessaggi.get(i);
	 * 
	 * lMesDao.ricercaMessaggioEsitoRicercaPerJmsId(aMess.getIdMessaggio().toString()); Vector lVect = new
	 * Vector(lMesDao.getModels()); lMesDao.stop(); // ContatoreEsitiModel lContatore = new
	 * ContatoreEsitiModel();
	 * 
	 * if (lVect.size() > 0) { Vector lVectTemp = new Vector(); for (int y = 0; y < lVect.size(); y++) {
	 * MessaggioModel lModelTemp = (MessaggioModel) lVect.get(y); //
	 * aggiornaContatore(lModelTemp.getCodEsito(), lContatore); lVectTemp.add(lModelTemp); //Se non partito
	 * rispedisco...
	 * 
	 * if (lModelTemp.getCodEsito().equals(NON_SPEDITO)) {
	 * 
	 * ManageMessageNoSend lMan = new ManageMessageNoSend(); lMan.rispedisciMessaggioNonSpedito(aMess,
	 * lModelTemp);
	 * 
	 * /*lMesDao.ricercaMessaggioByKey(aMess.getIdMessaggio()); MessaggioModel lModelDaRispedire =
	 * (MessaggioModel) lMesDao.getModelByKey(); lModelDaRispedire.setCodUfficioDestinatario("-");
	 * lModelDaRispedire.setCodBdiDestinataria(lModelTemp.getDescrBdiMittente());
	 */
	// --- SIAPSender lSender = new SIAPSender();
	// --- lSender.sendToTrueDestination(lModelDaRispedire);
	// }
	// }

	// lContatore.calcolaBDIInAttesa();
	// lVectTemp.add(lContatore);
	/*
	 * aMess.setMessaggiCorrelati(lVectTemp); lMessaggi.set(i, aMess); } }
	 * 
	 * } catch (DAOException daoEx) { throw new
	 * F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " + daoEx); } catch (SQLException
	 * sqe) { throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " + sqe); } catch
	 * (IOException ioe) { throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " +
	 * ioe); } catch (ClassNotFoundException ioe) { throw new
	 * F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " + ioe); } finally {
	 * cleanup(lMesDao);
	 * 
	 * cleanup(lConn); }
	 * 
	 * return lMessaggi; }
	 */

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

			// Collego ai Messaggi di tipo Esito trovati il Rispettivo Messagio di Richiesta
			for (int i = 0; i < lMessaggi.size(); i++) {
				MessaggioModel aMess = (MessaggioModel) lMessaggi.get(i);

				lMesDao.ricercaMessaggioEsitoRicercaPerJmsId(aMess.getIdMessaggio().toString());
				Vector lVect = new Vector(lMesDao.getModels());

				if (lVect.size() > 0) {
					MessaggioModel lModelTemp = (MessaggioModel) lVect.firstElement();
					aMess.setMessaggioCorrelato(lModelTemp);
					lMessaggi.set(i, aMess);

					if (lModelTemp.getCodEsito().equals(NON_SPEDITO)) {

						ManageMessageNoSend lMan = new ManageMessageNoSend();
						lMan.rispedisciMessaggioNonSpedito(aMess, lModelTemp);

						/*
						 * lMesDao.ricercaMessaggioByKey(aMess.getIdMessaggio()); MessaggioModel
						 * lModelDaRispedire = (MessaggioModel) lMesDao.getModelByKey();
						 * lModelDaRispedire.setCodUfficioDestinatario("-");
						 * lModelDaRispedire.setCodBdiDestinataria(lModelTemp.getDescrBdiMittente());
						 */
						// --- SIAPSender lSender = new SIAPSender();
						// --- lSender.sendToTrueDestination(lModelDaRispedire);
					}
				}

				lMesDao.stop();
			}
		} catch (DAOException daoEx) {
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " + daoEx);
		} catch (F3BException f3bEx) {
			throw f3bEx;
		} catch (Exception ex) {
			throw new F3BException("MessaggioController.ExRicercaMessaggioEsitoPerUfficio: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

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
			throw new F3BException("MessaggioController.ExGetCountEsitoRicercaPerUfficio: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("MessaggioController.ExGetCountEsitoRicercaPerUfficio: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/**
	 * RIcerca dei messaggi di tipo Esito Ricerca Fascicoli
	 * 
	 * @param lAnnoSiep
	 * @param lProgrSiep
	 * @param codUfficioMittente
	 * @param codUfficioDestinatario
	 * @param codUtente
	 * @param tipoEsito
	 * @param aTipoOperazione
	 * @param dataRicercaInizio
	 * @param dataRicercaFine
	 * @param aPage
	 * @return
	 * @throws F3BException
	 */

	public BigDecimal ExGetCountEsitoRicercaFascAltreBDI(String lAnnoSiep, String lProgrSiep,
			String codUfficioMittente, String codUfficioDestinatario, String codUtente, String tipoEsito,
			String codTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.getCountMessaggiPerRicercaFascAltreBDI(lAnnoSiep, lProgrSiep, codUfficioMittente,
					codUfficioDestinatario, codUtente, tipoEsito, codTipoOperazione, dataRicercaInizio,
					dataRicercaFine);
			lMesDao.start();
			lMesDao.next();
			lCount = lMesDao.getBigDecimal("HowManyRecords");
			lMesDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("MessaggioController.ExGetCountEsitoRicercaFascAltreBDI: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("MessaggioController.ExGetCountEsitoRicercaFascAltreBDI: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * RIcerca dei messaggi di tipo Esito Ricerca Soggetti
	 * 
	 * @param codUtente
	 * @param tipoEsito
	 * @param aTipoOperazione
	 * @param dataRicercaInizio
	 * @param dataRicercaFine
	 * @param aPage
	 * @return
	 * @throws F3BException
	 */

	public BigDecimal ExGetCountEsitoRicercaSoggAltreBDI(String codUfficioMittente, String codUtente,
			String codTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		MessaggioSqlDAO lMesDao = null;

		try {
			lConn = getDBConnection();
			lMesDao = new MessaggioSqlDAO(lConn);
			lMesDao.getCountMessaggiPerRicercaSoggAltreBDI(codUfficioMittente, codUtente, codTipoOperazione,
					dataRicercaInizio, dataRicercaFine);
			lMesDao.start();
			lMesDao.next();
			lCount = lMesDao.getBigDecimal("HowManyRecords");
			lMesDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("MessaggioController.ExGetCountEsitoRicercaSoggAltreBDI: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("MessaggioController.ExGetCountEsitoRicercaSoggAltreBDI: " + ex);
		} finally {
			cleanup(lMesDao);
			cleanup(lConn);
		}
		return lCount;
	}

}