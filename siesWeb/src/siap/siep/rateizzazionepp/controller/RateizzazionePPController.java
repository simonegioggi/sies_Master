package siap.siep.rateizzazionepp.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.controller.SiapController;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.pagoPA.dao.BollettinoPagopaDAO;
import siap.siep.pagoPA.dao.BollettinoPagopaSqlDAO;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.rateizzazionepp.dao.RateizzazionePPDAO;
import siap.siep.rateizzazionepp.dao.RateizzazionePPSqlDAO;
import siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;

/**
 * Classe controller per la gestione delle rateizzazioni
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RateizzazionePPController extends SiapController implements IRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public void exInserisciRateizzazioni(Vector<RateizzazionePPModel> aListaRate) throws F3BException {

		Connection c = null;

		RateizzazionePPDAO lRateizzazioneDao = null;
		try {
			c = getDBTransaction();

			lRateizzazioneDao = new RateizzazionePPDAO(c);

			siesLogger.debug("Ciclo caricamento rate. Num rate = " + aListaRate.size());
			for (int i = 0; i < aListaRate.size(); i++) {
				RateizzazionePPModel lRataModel = aListaRate.elementAt(i);
				lRateizzazioneDao.setDAOFromModel(lRataModel);
				lRateizzazioneDao.insert();
			}

			commit(c);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciRateizzazioni: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciRateizzazioni: " + ex);
		} finally {
			cleanup(lRateizzazioneDao);

			cleanup(c);
		}

		return;
	}

	public Vector<RateizzazionePPModel> exRicercaRateizzazioniByIdFasc(BigDecimal aIdFasc)
			throws F3BException {

		Vector<RateizzazionePPModel> lListaRate = new Vector<>();

		Connection c = null;

		RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
		BollettinoPagopaSqlDAO lBollettinoSqlDAO = null;

		try {
			c = getDBConnection();

			lRateizzazioneSqlDao = new RateizzazionePPSqlDAO(c);
			lBollettinoSqlDAO = new BollettinoPagopaSqlDAO(c);

			lRateizzazioneSqlDao.ricercaRateizzazionePPByIdFasSIEP(aIdFasc);

			lRateizzazioneSqlDao.start();
			while (lRateizzazioneSqlDao.next()) {
				lListaRate.add((RateizzazionePPModel) lRateizzazioneSqlDao.getModel());
			}
			lRateizzazioneSqlDao.stop();

			for (RateizzazionePPModel lRata : lListaRate) {
				lBollettinoSqlDAO.ricercaBollettinoPagopaByReteizzazione(lRata.getIdRateizzazionePP());
				Vector<BollettinoPagopaModel> lListaBollettini = new Vector<BollettinoPagopaModel>(
						lBollettinoSqlDAO.getModels());
				lRata.setListaBollettini(lListaBollettini);
			}

			commit(c);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdFasc: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdFasc: " + ex);
		} finally {
			cleanup(lRateizzazioneSqlDao);

			cleanup(c);
		}

		return lListaRate;
	}

	public void exCancellaRateizzazioniByIdFasc(BigDecimal aIdFasc) throws F3BException {

		Connection c = null;

		RateizzazionePPDAO lRateizzazioneDao = null;
		RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
		BollettinoPagopaDAO lBollettiniDao = null;

		try {
			c = getDBConnection();

			lRateizzazioneSqlDao = new RateizzazionePPSqlDAO(c);
			lRateizzazioneDao = new RateizzazionePPDAO(c);
			lBollettiniDao = new BollettinoPagopaDAO(c);

			lRateizzazioneSqlDao.ricercaRateizzazionePPByIdFascicoloSiep(aIdFasc);
			Vector<RateizzazionePPModel> lListaRate = new Vector<RateizzazionePPModel>(
					lRateizzazioneSqlDao.getModels());
			// MEV_2023-33: aggiunta gestione storicizzazione rate e bollettini
			if (!lListaRate.isEmpty()) {
				Iterator<RateizzazionePPModel> iterRPPM = lListaRate.iterator();
				while (iterRPPM.hasNext()) {
					RateizzazionePPModel rppm = iterRPPM.next();
					BigDecimal idEvento = rppm.getEveIdEvento();
					if (!Utils.isNullObj(idEvento)) {
						IEvento ie = SICOLookupRemote.getEventoRemote();
						EventoModel em = ie.ExRicercaEventoByKey(idEvento);
						if ("A".equals(em.getFlagDocumentoRegistrato()))
							iterRPPM.remove();
					}
				}
			}
			// FINE MEV_2023-33
			for (RateizzazionePPModel lrata : lListaRate) {
				lBollettiniDao.selCondizioneDeleteByIdRata(lrata.getIdRateizzazionePP());
				lBollettiniDao.delete();
				lRateizzazioneDao.selCondizioneUpdate(lrata.getIdRateizzazionePP());
				lRateizzazioneDao.delete();
			}

			commit(c);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exCancellaRateizzazioniByIdFasc: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exCancellaRateizzazioniByIdFasc: " + ex);
		} finally {
			cleanup(lRateizzazioneDao);
			cleanup(lBollettiniDao);
			cleanup(lRateizzazioneSqlDao);

			cleanup(c);
		}
	}

	public void exModificaRateizzazioni(Vector<RateizzazionePPModel> aListaRate, BigDecimal aIdFasc)
			throws F3BException {

		Connection c = null;

		RateizzazionePPDAO lRateizzazioneDao = null;
		RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
		BollettinoPagopaDAO lBollettiniDao = null;

		try {
			c = getDBTransaction();

			lRateizzazioneSqlDao = new RateizzazionePPSqlDAO(c);
			lRateizzazioneDao = new RateizzazionePPDAO(c);
			lBollettiniDao = new BollettinoPagopaDAO(c);

			siesLogger.debug("Cancello le precedenti rate.");
			lRateizzazioneSqlDao.ricercaRateizzazionePPByIdFascicoloSiep(aIdFasc);
			Vector<RateizzazionePPModel> lListaRate = new Vector<RateizzazionePPModel>(
					lRateizzazioneSqlDao.getModels());
			// MEV_2023-33: aggiunta gestione storicizzazione rate e bollettini
			if (!lListaRate.isEmpty()) {
				Iterator<RateizzazionePPModel> iterRPPM = lListaRate.iterator();
				while (iterRPPM.hasNext()) {
					RateizzazionePPModel rppm = iterRPPM.next();
					BigDecimal idEvento = rppm.getEveIdEvento();
					if (!Utils.isNullObj(idEvento)) {
						IEvento ie = SICOLookupRemote.getEventoRemote();
						EventoModel em = ie.ExRicercaEventoByKey(idEvento);
						if ("A".equals(em.getFlagDocumentoRegistrato()))
							iterRPPM.remove();
					}
				}
			}
			// FINE MEV_2023-33
			for (RateizzazionePPModel lrata : lListaRate) {
				lBollettiniDao.selCondizioneDeleteByIdRata(lrata.getIdRateizzazionePP());
				lBollettiniDao.delete();
				lRateizzazioneDao.selCondizioneUpdate(lrata.getIdRateizzazionePP());
				lRateizzazioneDao.delete();
			}

			siesLogger.debug("Ciclo caricamento rate. Num rate = " + aListaRate.size());
			for (int i = 0; i < aListaRate.size(); i++) {
				RateizzazionePPModel lRataModel = aListaRate.elementAt(i);
				lRateizzazioneDao.setDAOFromModel(lRataModel);
				lRateizzazioneDao.insert();
			}

			commit(c);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaRateizzazioni: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaRateizzazioni: " + ex);
		} finally {
			cleanup(lRateizzazioneDao);
			cleanup(lBollettiniDao);
			cleanup(lRateizzazioneSqlDao);

			cleanup(c);
		}

		return;
	}

	public Vector<RateizzazionePPModel> exRicercaRateizzazioniByIdEvento(BigDecimal aIdEvento)
			throws F3BException {

		Vector<RateizzazionePPModel> lListaRate = new Vector<>();

		Connection c = null;

		RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
		try {
			c = getDBConnection();

			lRateizzazioneSqlDao = new RateizzazionePPSqlDAO(c);

			lRateizzazioneSqlDao.ricercaRateizzazionePPByEveIdEvento(aIdEvento);

			lRateizzazioneSqlDao.start();
			while (lRateizzazioneSqlDao.next()) {
				lListaRate.add((RateizzazionePPModel) lRateizzazioneSqlDao.getModel());
			}
			lRateizzazioneSqlDao.stop();

			commit(c);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdEvento: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdEvento: " + ex);
		} finally {
			cleanup(lRateizzazioneSqlDao);

			cleanup(c);
		}

		return lListaRate;
	}

	@Override
	public Vector<EventoRateizzazionePPModel> exRicercaEventoRateizzazionePP(BigDecimal idFascicolo)
			throws F3BException {

		Connection c = null;

		RateizzazionePPSqlDAO rppsdao = null;
		EventoSqlDAO esdao = null;

		// Ricerca gli eventi per id Fascicolo
		EventoModel em = new EventoModel();
		em.setFlagDocumentoRegistrato("S");
		em.setFasSieIdFascicoloSiep(idFascicolo);
		em.setCodMotivo("0622");
		em.setCodTipoProvvedimento("06");
		em.setCodTipoEvento("01");

		Vector<EventoRateizzazionePPModel> listaEventoRateizzazioniPP = new Vector<>();

		try {
			c = getDBConnection();
			esdao = new EventoSqlDAO(c);
			esdao.ricercaEvento(em);
			List<EventoModel> listaEventi = new ArrayList(esdao.getModels());
			esdao.stop();

			Iterator<EventoModel> iter = listaEventi.iterator();
			while (iter.hasNext()) {
				EventoModel evm = iter.next();
				Vector<RateizzazionePPModel> listaRateizzazionePP = exRicercaRateizzazioniByIdEvento(
						evm.getIdEvento());
				// Aggiungo l'evento
				EventoRateizzazionePPModel erppm = new EventoRateizzazionePPModel();
				erppm.setEvento(evm);
				erppm.setListaRateizzazioniPP(listaRateizzazionePP);
				listaEventoRateizzazioniPP.add(erppm);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"LicenzaLibanticipataController.ExRicercaRimediRisarcitoriConcessiDepositatiByIdFascicoloSIEP: "
							+ daoEx);
		} finally {
			cleanup(rppsdao);
			cleanup(esdao);
			cleanup(c);
		}

		return listaEventoRateizzazioniPP;
	}

	@Override
	/**
	 * @since MEV_2023-33
	 */
	public void exInserisciRideterminazionePP(EventoNotificaModel enm, String[] arrayIdRate)
			throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		NotificaDAO nDAO = null;
		AutoritaEsternaDAO aeDAO = null;
		RateizzazionePPDAO rPPDAO = null;

		try {
			c = getDBConnection();

			// =========================================
			// Inserisco l'OE
			// =========================================
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModel(enm.getEvento());
			BigDecimal idEvento = eDAO.insert();
			eDAO.stop();
			siesLogger.debug("idEvento = " + idEvento);

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			aeDAO = new AutoritaEsternaDAO(c);
			BigDecimal idAutorita = null;

			if (enm != null && enm.getNotifiche() != null) {
				siesLogger.debug("Presenti " + enm.getNotifiche().length + " notifiche");
				while (count < enm.getNotifiche().length) {
					siesLogger.debug("count = " + count);
					siesLogger.debug("Notifica[" + count + "] = " + enm.getNotifiche()[count]);

					if (enm.getNotifiche()[count] != null) {
						// Se è stata specificata anche l'autorità esterna per l'avvocato,
						// recupero l'id da inserire nella notifica
						// n.b. se autorità non presente la creo
						if (enm.getNotifiche()[count].getAutoritaEsterna() != null) {
							// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
							// già l'autorità esterna specificata nella form (dalla form ho solo
							// codice e sede)
							aeDAO.setRicercaByAutSede(
									enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel aem = new AutoritaEsternaModel();
							aem = (AutoritaEsternaModel) aeDAO.getModelByKey();
							if (aem == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								siesLogger.debug("Ins Aut Est = "
										+ enm.getNotifiche()[count].getAutoritaEsterna());
								aeDAO.setDAOFromModel(
										enm.getNotifiche()[count].getAutoritaEsterna());
								idAutorita = aeDAO.insert();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(idAutorita);
							} else {
								idAutorita = aem.getIdAutoritaEsterna();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(idAutorita);
							}
						}

						// ===========================================
						enm.getNotifiche()[count].setEveIdEvento(idEvento);

						nDAO = new NotificaDAO(c);
						nDAO.setDAOFromModel(enm.getNotifiche()[count]);
						BigDecimal idNotifica = nDAO.insert();
						nDAO.stop();
						siesLogger.debug("Inserita Notifica " + idNotifica);
					}
					count++;
				}
			}

			// =========================================================
			// Aggiorno le rate collegandole all'evento
			// =========================================================
			rPPDAO = new RateizzazionePPDAO(c);
			for (int i = 0; i < arrayIdRate.length; i++) {
				String idRata = arrayIdRate[i];
				siesLogger.debug("idRata = " + idRata);
				rPPDAO.setEveIdEvento(idEvento);
				rPPDAO.selCondizioneUpdate(new BigDecimal(idRata));
				rPPDAO.update();
			}

			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("SanzioneSostitutivaController.exInserisciOrdineIngiunzione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(c);
			throw new F3BException("SanzioneSostitutivaController.exInserisciOrdineIngiunzione: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(nDAO);
			cleanup(aeDAO);
			cleanup(rPPDAO);

			cleanup(c);
		}
	}

}