package siap.siep.rateizzazionepp.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
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
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;

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
			cleanup(lBollettinoSqlDAO);

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
						// 2023.09.19 - posso cancellare solo le rate non collegati ad eventi
						iterRPPM.remove();
						/*
						 * IEvento ie = SICOLookupRemote.getEventoRemote(); EventoModel em =
						 * ie.ExRicercaEventoByKey(idEvento); if ("A".equals(em.getFlagDocumentoRegistrato()))
						 * iterRPPM.remove();
						 */
						// 2023.09.19 - FINE
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
						// 2023.09.19 - posso cancellare solo le rate non collegati ad eventi
						iterRPPM.remove();
						/*
						 * IEvento ie = SICOLookupRemote.getEventoRemote(); EventoModel em =
						 * ie.ExRicercaEventoByKey(idEvento); if ("A".equals(em.getFlagDocumentoRegistrato()))
						 * iterRPPM.remove();
						 */
						// 2023.09.19 - FINE
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

	/**
	 * Override del metodo per esporre la chiamata consentendo si specificare anche se se non si \E8
	 * interessati agli eventi non validati
	 */
	@Override
	public Vector<EventoRateizzazionePPModel> exRicercaEventoRateizzazionePP(BigDecimal idFascicolo,
			String motivo) throws F3BException {

		if ("ALL".equals(motivo)) {
			return exRicercaEventiRateizzazionePP(idFascicolo);
		}
		return exRicercaEventoRateizzazionePP(idFascicolo, motivo, "S");
	}

	private Vector<EventoRateizzazionePPModel> exRicercaEventiRateizzazionePP(BigDecimal idFascicolo)
			throws F3BException {

		Connection c = null;

		RateizzazionePPSqlDAO rppsdao = null;
		EventoSqlDAO esdao = null;

		// Ricerca gli eventi per id Fascicolo
		EventoModel em = new EventoModel();
		em.setFlagDocumentoRegistrato("S");
		em.setFasSieIdFascicoloSiep(idFascicolo);
		String[] motivi = new String[] { "0622", "1307", "1308" };
		// String[] tipi = new String[] { "04", "06" };
		em.setCodTipoEvento("01");

		// Ricerca gli eventi per id Fascicolo
		Vector<EventoRateizzazionePPModel> listaEventoRateizzazioniPP = new Vector<>();

		try {
			c = getDBConnection();
			esdao = new EventoSqlDAO(c);
			esdao.ricercaEventoPerMotivo(motivi, em);
			List<EventoModel> listaEventi = new ArrayList(esdao.getModels());
			esdao.stop();
			if (!listaEventi.isEmpty() && listaEventi.size() > 1)
				listaEventi = reverseList(listaEventi);

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
			throw new F3BException("RateizzazionePPController.exRicercaEventiRateizzazionePP: " + daoEx);
		} finally {
			cleanup(rppsdao);
			cleanup(esdao);
			cleanup(c);
		}

		return listaEventoRateizzazioniPP;
	}

	private List<EventoModel> reverseList(List<EventoModel> listaEventi) {

		List<EventoModel> reverse = new ArrayList<>(listaEventi);
		Collections.reverse(reverse);
		return reverse;
	}

	@Override
	public Vector<EventoRateizzazionePPModel> exRicercaEventoRateizzazionePP(BigDecimal idFascicolo,
			String motivo, String validato) throws F3BException {

		Connection c = null;

		RateizzazionePPSqlDAO rppsdao = null;
		EventoSqlDAO esdao = null;

		// Ricerca gli eventi per id Fascicolo
		EventoModel em = new EventoModel();
		// 2023.09.19
		if ("S".equals(validato))
			em.setFlagDocumentoRegistrato("S");
		// 2023.09.19
		em.setFasSieIdFascicoloSiep(idFascicolo);
		if ("rpp".equals(motivo)) {
			em.setCodMotivo("1307");
			em.setCodTipoProvvedimento("04");
		} else {
			em.setCodMotivo("0622");
			em.setCodTipoProvvedimento("06");
		}
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
			throw new F3BException("RateizzazionePPController.exRicercaEventoRateizzazionePP: " + daoEx);
		} finally {
			cleanup(rppsdao);
			cleanup(esdao);
			cleanup(c);
		}

		return listaEventoRateizzazioniPP;
	}

	/**
	 * 2023.09.27
	 *
	 * @param idFascicolo
	 * @return
	 * @throws F3BException
	 */
	public Vector<EventoRateizzazionePPModel> exRicercaEventiRateizzazionePP(BigDecimal idFascicolo,
			String[] motivi, boolean soloValidati) throws F3BException {

		Connection c = null;

		RateizzazionePPSqlDAO rppsdao = null;
		EventoSqlDAO esdao = null;

		// Ricerca gli eventi per id Fascicolo
		EventoModel em = new EventoModel();
		if (soloValidati)
			em.setFlagDocumentoRegistrato("S");
		em.setFasSieIdFascicoloSiep(idFascicolo);
		// String[] motivi = new String[] { "0622", "1307", "1308" };
		// String[] tipi = new String[] { "04", "06" };
		em.setCodTipoEvento("01");

		// Ricerca gli eventi per id Fascicolo
		Vector<EventoRateizzazionePPModel> listaEventoRateizzazioniPP = new Vector<>();

		try {
			c = getDBConnection();
			esdao = new EventoSqlDAO(c);
			esdao.ricercaEventoPerMotivoOrderDesc(motivi, em);
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
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("RateizzazionePPController.exRicercaEventiRateizzazionePP: " + daoEx);
		} finally {
			cleanup(rppsdao);
			cleanup(esdao);
			cleanup(c);
		}

		return listaEventoRateizzazioniPP;
	}

	/*
	 * ISSUE MEV : aggiunti metodi per insert, update, print Numero MEV : 2023-33 Autore : sgioggi Data : 29
	 * ago 2023 Branch : MEV_2023-33
	 */
	@Override
	public BigDecimal exInserisciRideterminazionePP(EventoNotificaModel enm, String[] arrayIdRate,
			AnnotazioneManualeModel amm) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		NotificaDAO nDAO = null;
		AutoritaEsternaDAO aeDAO = null;
		RateizzazionePPDAO rPPDAO = null;
		AnnotazioneManualeDAO amDAO = null;

		BigDecimal idEvento = null;

		try {
			c = getDBConnection();

			// =========================================
			// Inserisco l'Evento
			// =========================================
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModel(enm.getEvento());
			idEvento = eDAO.insert();
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
							aeDAO.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel aem = new AutoritaEsternaModel();
							aem = (AutoritaEsternaModel) aeDAO.getModelByKey();
							if (aem == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								siesLogger.debug(
										"Ins Aut Est = " + enm.getNotifiche()[count].getAutoritaEsterna());
								aeDAO.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
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

			// inserisco l'annotazione manuale
			amm.setEveIdEvento(idEvento);
			amDAO = new AnnotazioneManualeDAO(c);
			amDAO.setDAOFromModel(amm);
			BigDecimal idAnnotazioneManuale = amDAO.insert();
			amDAO.stop();
			siesLogger.debug("idAnnotazioneManuale = " + idAnnotazioneManuale);

			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciRideterminazionePP: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciRideterminazionePP: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(nDAO);
			cleanup(aeDAO);
			cleanup(rPPDAO);
			cleanup(amDAO);

			cleanup(c);
		}
		return idEvento;
	}

	@Override
	public void exUpdateRideterminazionePP(EventoModel em) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		EventoSqlDAO esDAO = null;
		StatoProcedimentoDAO spDAO = null;
		EventoDAO eDAOBlob = null;

		try {
			c = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			esDAO = new EventoSqlDAO(c);
			esDAO.ricercaEventoByKey(em.getIdEvento());
			EventoModel emRic = (EventoModel) esDAO.getModelByKey();
			esDAO.stop();

			// ====================================
			// Modifico lo stato del procedimento
			// ====================================
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// ========================================================================
			siesLogger.debug("Aggiornamento stato procedimento");
			StatoProcedimentoModel spm = new StatoProcedimentoModel();
			spm.setProgressivo(new BigDecimal(1));
			spm.setFasSieIdFascicoloSiep(emRic.getFasSieIdFascicoloSiep());
			spm.setData(emRic.getDataEmissione());
			spm.setCodStatoProcedimento("0364");
			spm.setCodOperatoreInserimento(em.getCodOperatoreAggiornamento());
			spm.setDataInserimento(em.getDataAggiornamento());
			spm.setCodUfficioInserimento(em.getCodUfficioAggiornamento());
			spDAO = new StatoProcedimentoDAO(c);
			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			spDAO.setCondizioneByIdFascicolo(emRic.getFasSieIdFascicoloSiep());
			spDAO.delete();
			// - Inserisce
			spDAO.setDAOFromModel(spm);
			spDAO.insert();
			spDAO.stop();
			siesLogger.debug("FINE Aggiornamento stato procedimento");

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			siesLogger.debug("Aggiornamento Blob");
			eDAOBlob = new EventoDAO(c);
			eDAOBlob.setDAOFromModelForUpdateBlob(em);
			eDAOBlob.selCondizioneUpdate(em.getIdEvento());
			eDAOBlob.update();
			eDAOBlob.stop();
			siesLogger.debug("Blob Aggiornato");

			commit(c);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exUpdateRideterminazionePP: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exUpdateRideterminazionePP: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(esDAO);
			cleanup(spDAO);
			cleanup(eDAOBlob);

			cleanup(c);
		}
	}

	@Override
	public EventoNotificaModel exModificaRideterminazionePP(EventoNotificaModel enm, String[] arrayIdRate,
			AnnotazioneManualeModel amm) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		NotificaDAO nDAO = null;
		AutoritaEsternaDAO aeDAO = null;
		RateizzazionePPDAO rPPDAO = null;
		AnnotazioneManualeDAO amDAO = null;

		EventoNotificaModel enmRet = new EventoNotificaModel(enm);

		try {
			c = getDBConnection();

			BigDecimal idEvento = enm.getEvento().getIdEvento();

			// Cancello preventivamente tutti i dati
			// Sgancio le rate dall'evento
			rPPDAO = new RateizzazionePPDAO(c);
			rPPDAO.setEveIdEvento(null);
			rPPDAO.selCondizioneByIdEvento(enm.getEvento().getIdEvento());
			rPPDAO.update();

			nDAO = new NotificaDAO(c);
			nDAO.setCondizioneEvento(idEvento);
			nDAO.delete();

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			aeDAO = new AutoritaEsternaDAO(c);
			BigDecimal keyAutorita = null;

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
							aeDAO.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) aeDAO.getModelByKey();
							if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								aeDAO.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
								keyAutorita = aeDAO.insert();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(keyAutorita);
							} else {
								keyAutorita = lAutMod.getIdAutoritaEsterna();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(keyAutorita);
							}
						}

						// ===========================================
						enm.getNotifiche()[count].setEveIdEvento(idEvento);
						nDAO = new NotificaDAO(c);
						nDAO.setDAOFromModel(enm.getNotifiche()[count]);
						BigDecimal keyNotifica = nDAO.insert();
						nDAO.stop();
						siesLogger.debug("Inserita Notifica " + keyNotifica);
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

			// aggiorrno l'annotazione manuale
			amDAO = new AnnotazioneManualeDAO(c);
			amDAO.setDAOFromModelForUpdate(amm);
			amDAO.update();
			amDAO.stop();

			// aggiorrno l'evento
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModelForUpdate(enm.getEvento());
			eDAO.update();
			eDAO.stop();

			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaRideterminazionePP: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaRideterminazionePP: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(nDAO);
			cleanup(aeDAO);
			cleanup(rPPDAO);
			cleanup(amDAO);

			cleanup(c);
		}

		return enmRet;
	}

	/**
	 * Ricerca i record reatizzazione collegati al fascicolo ma non ad alcun evento, per le quali puo' essere
	 * emessu un OI
	 *
	 * @param aIdFasc
	 * @return
	 * @throws F3BException
	 * @since MEV_2023-33
	 */
	public Vector<RateizzazionePPModel> exRicercaRateizzazioniLibereByIdFasc(BigDecimal aIdFasc)
			throws F3BException {

		Vector<RateizzazionePPModel> lListaRate = new Vector<>();

		Connection c = null;

		RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
		BollettinoPagopaSqlDAO lBollettinoSqlDAO = null;

		try {
			c = getDBConnection();

			lRateizzazioneSqlDao = new RateizzazionePPSqlDAO(c);
			lBollettinoSqlDAO = new BollettinoPagopaSqlDAO(c);

			lRateizzazioneSqlDao.ricercaRateizzazionePPByIdFasSIEPLibero(aIdFasc);

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
			throw new F3BException(
					"RateizzazionePPController.exRicercaRateizzazioniLibereByIdFasc: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniLibereByIdFasc: " + ex);
		} finally {
			cleanup(lRateizzazioneSqlDao);
			cleanup(lBollettinoSqlDAO);

			cleanup(c);
		}

		return lListaRate;
	}

	@Override
	public BigDecimal exInserisciAvvisoMancatoPagamento(EventoNotificaModel enm, RateizzazionePPModel rppm)
			throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		NotificaDAO nDAO = null;
		AutoritaEsternaDAO aeDAO = null;
		RateizzazionePPDAO rPPDAO = null;

		BigDecimal idEvento = null;

		try {
			c = getDBConnection();

			// =========================================
			// Inserisco l'Evento
			// =========================================
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModel(enm.getEvento());
			idEvento = eDAO.insert();
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
							aeDAO.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel aem = new AutoritaEsternaModel();
							aem = (AutoritaEsternaModel) aeDAO.getModelByKey();
							if (aem == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								siesLogger.debug(
										"Ins Aut Est = " + enm.getNotifiche()[count].getAutoritaEsterna());
								aeDAO.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
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
			// Inserisco la rata unica collegandola all'evento
			// =========================================================
			rPPDAO = new RateizzazionePPDAO(c);
			rppm.setEveIdEvento(idEvento);
			rPPDAO.setDAOFromModel(rppm);
			BigDecimal idRata = rPPDAO.insert();
			siesLogger.debug("INSERITA RATEIZZAZIONE_PP con idRata = " + idRata);

			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciAvvisoMancatoPagamento: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciAvvisoMancatoPagamento: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(nDAO);
			cleanup(aeDAO);
			cleanup(rPPDAO);

			cleanup(c);
		}
		return idEvento;
	}

	@Override
	public void exUpdateAvvisoMancatoPagamento(EventoModel em) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		EventoSqlDAO esDAO = null;
		StatoProcedimentoDAO spDAO = null;
		EventoDAO eDAOBlob = null;

		try {
			c = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			esDAO = new EventoSqlDAO(c);
			esDAO.ricercaEventoByKey(em.getIdEvento());
			EventoModel emRic = (EventoModel) esDAO.getModelByKey();
			esDAO.stop();

			// ====================================
			// Modifico lo stato del procedimento
			// ====================================
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// ========================================================================
			siesLogger.debug("Aggiornamento stato procedimento");
			StatoProcedimentoModel spm = new StatoProcedimentoModel();
			spm.setProgressivo(new BigDecimal(1));
			spm.setFasSieIdFascicoloSiep(emRic.getFasSieIdFascicoloSiep());
			spm.setData(emRic.getDataEmissione());
			spm.setCodStatoProcedimento("0365");
			spm.setCodOperatoreInserimento(em.getCodOperatoreAggiornamento());
			spm.setDataInserimento(em.getDataAggiornamento());
			spm.setCodUfficioInserimento(em.getCodUfficioAggiornamento());
			spDAO = new StatoProcedimentoDAO(c);
			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			spDAO.setCondizioneByIdFascicolo(emRic.getFasSieIdFascicoloSiep());
			spDAO.delete();
			// - Inserisce
			spDAO.setDAOFromModel(spm);
			spDAO.insert();
			spDAO.stop();
			siesLogger.debug("FINE Aggiornamento stato procedimento");

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			siesLogger.debug("Aggiornamento Blob");
			eDAOBlob = new EventoDAO(c);
			eDAOBlob.setDAOFromModelForUpdateBlob(em);
			eDAOBlob.selCondizioneUpdate(em.getIdEvento());
			eDAOBlob.update();
			eDAOBlob.stop();
			siesLogger.debug("Blob Aggiornato");

			commit(c);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exUpdateAvvisoMancatoPagamento: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exUpdateAvvisoMancatoPagamento: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(esDAO);
			cleanup(spDAO);
			cleanup(eDAOBlob);

			cleanup(c);
		}
	}

	@Override
	public EventoNotificaModel exModificaAvvisoMancatoPagamento(EventoNotificaModel enm) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		NotificaDAO nDAO = null;
		AutoritaEsternaDAO aeDAO = null;

		EventoNotificaModel enmRet = new EventoNotificaModel(enm);

		try {
			c = getDBConnection();

			BigDecimal idEvento = enm.getEvento().getIdEvento();

			// Cancello preventivamente tutte le notifiche
			nDAO = new NotificaDAO(c);
			nDAO.setCondizioneEvento(idEvento);
			nDAO.delete();

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			aeDAO = new AutoritaEsternaDAO(c);
			BigDecimal keyAutorita = null;

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
							aeDAO.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) aeDAO.getModelByKey();
							if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								aeDAO.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
								keyAutorita = aeDAO.insert();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(keyAutorita);
							} else {
								keyAutorita = lAutMod.getIdAutoritaEsterna();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(keyAutorita);
							}
						}

						// ===========================================
						enm.getNotifiche()[count].setEveIdEvento(idEvento);
						nDAO = new NotificaDAO(c);
						nDAO.setDAOFromModel(enm.getNotifiche()[count]);
						BigDecimal keyNotifica = nDAO.insert();
						nDAO.stop();
						siesLogger.debug("Inserita Notifica " + keyNotifica);
					}
					count++;
				}
			}

			// aggiorrno l'evento
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModelForUpdate(enm.getEvento());
			eDAO.update();
			eDAO.stop();

			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaAvvisoMancatoPagamento: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaAvvisoMancatoPagamento: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(nDAO);
			cleanup(aeDAO);

			cleanup(c);
		}

		return enmRet;
	}

	@Override
	public BigDecimal exInserisciProvvedimentoEstinzionePP(EventoNotificaModel enm, String[] arrayIdRate,
			AnnotazioneManualeModel amm) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		NotificaDAO nDAO = null;
		AutoritaEsternaDAO aeDAO = null;
		RateizzazionePPDAO rPPDAO = null;
		AnnotazioneManualeDAO amDAO = null;

		BigDecimal idEvento = null;

		try {
			c = getDBConnection();

			// =========================================
			// Inserisco l'Evento
			// =========================================
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModel(enm.getEvento());
			idEvento = eDAO.insert();
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
							aeDAO.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel aem = new AutoritaEsternaModel();
							aem = (AutoritaEsternaModel) aeDAO.getModelByKey();
							if (aem == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								siesLogger.debug(
										"Ins Aut Est = " + enm.getNotifiche()[count].getAutoritaEsterna());
								aeDAO.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
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

			// inserisco l'annotazione manuale
			amm.setEveIdEvento(idEvento);
			amDAO = new AnnotazioneManualeDAO(c);
			amDAO.setDAOFromModel(amm);
			BigDecimal idAnnotazioneManuale = amDAO.insert();
			amDAO.stop();
			siesLogger.debug("idAnnotazioneManuale = " + idAnnotazioneManuale);

			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciProvvedimentoEstinzionePP: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciProvvedimentoEstinzionePP: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(nDAO);
			cleanup(aeDAO);
			cleanup(rPPDAO);
			cleanup(amDAO);

			cleanup(c);
		}
		return idEvento;
	}

	@Override
	public void exUpdateProvvedimentoEstinzionePP(EventoModel em) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		EventoSqlDAO esDAO = null;
		StatoProcedimentoDAO spDAO = null;
		EventoDAO eDAOBlob = null;

		try {
			c = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			esDAO = new EventoSqlDAO(c);
			esDAO.ricercaEventoByKey(em.getIdEvento());
			EventoModel emRic = (EventoModel) esDAO.getModelByKey();
			esDAO.stop();

			// ====================================
			// Modifico lo stato del procedimento
			// ====================================
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// ========================================================================
			siesLogger.debug("Aggiornamento stato procedimento");
			StatoProcedimentoModel spm = new StatoProcedimentoModel();
			spm.setProgressivo(new BigDecimal(1));
			spm.setFasSieIdFascicoloSiep(emRic.getFasSieIdFascicoloSiep());
			spm.setData(emRic.getDataEmissione());
			spm.setCodStatoProcedimento("0366");
			spm.setCodOperatoreInserimento(em.getCodOperatoreAggiornamento());
			spm.setDataInserimento(em.getDataAggiornamento());
			spm.setCodUfficioInserimento(em.getCodUfficioAggiornamento());
			spDAO = new StatoProcedimentoDAO(c);
			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			spDAO.setCondizioneByIdFascicolo(emRic.getFasSieIdFascicoloSiep());
			spDAO.delete();
			// - Inserisce
			spDAO.setDAOFromModel(spm);
			spDAO.insert();
			spDAO.stop();
			siesLogger.debug("FINE Aggiornamento stato procedimento");

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			siesLogger.debug("Aggiornamento Blob");
			eDAOBlob = new EventoDAO(c);
			eDAOBlob.setDAOFromModelForUpdateBlob(em);
			eDAOBlob.selCondizioneUpdate(em.getIdEvento());
			eDAOBlob.update();
			eDAOBlob.stop();
			siesLogger.debug("Blob Aggiornato");

			commit(c);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exUpdateProvvedimentoEstinzionePP: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exUpdateProvvedimentoEstinzionePP: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(esDAO);
			cleanup(spDAO);
			cleanup(eDAOBlob);

			cleanup(c);
		}
	}

	@Override
	public EventoNotificaModel exModificaProvvedimentoEstinzionePP(EventoNotificaModel enm,
			String[] arrayIdRate, AnnotazioneManualeModel amm) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		NotificaDAO nDAO = null;
		AutoritaEsternaDAO aeDAO = null;
		RateizzazionePPDAO rPPDAO = null;
		AnnotazioneManualeDAO amDAO = null;

		EventoNotificaModel enmRet = new EventoNotificaModel(enm);

		try {
			c = getDBConnection();

			BigDecimal idEvento = enm.getEvento().getIdEvento();

			// Cancello preventivamente tutti i dati
			// Sgancio le rate dall'evento
			rPPDAO = new RateizzazionePPDAO(c);
			rPPDAO.setEveIdEvento(null);
			rPPDAO.selCondizioneByIdEvento(enm.getEvento().getIdEvento());
			rPPDAO.update();

			nDAO = new NotificaDAO(c);
			nDAO.setCondizioneEvento(idEvento);
			nDAO.delete();

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			aeDAO = new AutoritaEsternaDAO(c);
			BigDecimal keyAutorita = null;

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
							aeDAO.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) aeDAO.getModelByKey();
							if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								aeDAO.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
								keyAutorita = aeDAO.insert();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(keyAutorita);
							} else {
								keyAutorita = lAutMod.getIdAutoritaEsterna();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(keyAutorita);
							}
						}

						// ===========================================
						enm.getNotifiche()[count].setEveIdEvento(idEvento);
						nDAO = new NotificaDAO(c);
						nDAO.setDAOFromModel(enm.getNotifiche()[count]);
						BigDecimal keyNotifica = nDAO.insert();
						nDAO.stop();
						siesLogger.debug("Inserita Notifica " + keyNotifica);
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

			// aggiorrno l'annotazione manuale
			amDAO = new AnnotazioneManualeDAO(c);
			amDAO.setDAOFromModelForUpdate(amm);
			amDAO.update();
			amDAO.stop();

			// aggiorrno l'evento
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModelForUpdate(enm.getEvento());
			eDAO.update();
			eDAO.stop();

			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaProvvedimentoEstinzionePP: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaProvvedimentoEstinzionePP: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(nDAO);
			cleanup(aeDAO);
			cleanup(rPPDAO);
			cleanup(amDAO);

			cleanup(c);
		}

		return enmRet;
	}

	@Override
	public BigDecimal exInserisciTrasmissioneAttiConversione(EventoNotificaModel enm, String[] arrayIdRate,
			AnnotazioneManualeModel amm) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		NotificaDAO nDAO = null;
		AutoritaEsternaDAO aeDAO = null;
		RateizzazionePPDAO rPPDAO = null;
		AnnotazioneManualeDAO amDAO = null;

		BigDecimal idEvento = null;

		try {
			c = getDBConnection();

			// =========================================
			// Inserisco l'Evento
			// =========================================
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModel(enm.getEvento());
			idEvento = eDAO.insert();
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
							aeDAO.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel aem = new AutoritaEsternaModel();
							aem = (AutoritaEsternaModel) aeDAO.getModelByKey();
							if (aem == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								siesLogger.debug(
										"Ins Aut Est = " + enm.getNotifiche()[count].getAutoritaEsterna());
								aeDAO.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
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

			// inserisco l'annotazione manuale
			amm.setEveIdEvento(idEvento);
			amDAO = new AnnotazioneManualeDAO(c);
			amDAO.setDAOFromModel(amm);
			BigDecimal idAnnotazioneManuale = amDAO.insert();
			amDAO.stop();
			siesLogger.debug("idAnnotazioneManuale = " + idAnnotazioneManuale);

			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciTrasmissioneAttiConversione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciTrasmissioneAttiConversione: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(nDAO);
			cleanup(aeDAO);
			cleanup(rPPDAO);
			cleanup(amDAO);

			cleanup(c);
		}
		return idEvento;
	}

	@Override
	public void exUpdateTrasmissioneAttiConversione(EventoModel em) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		EventoSqlDAO esDAO = null;
		StatoProcedimentoDAO spDAO = null;
		EventoDAO eDAOBlob = null;

		try {
			c = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			esDAO = new EventoSqlDAO(c);
			esDAO.ricercaEventoByKey(em.getIdEvento());
			EventoModel emRic = (EventoModel) esDAO.getModelByKey();
			esDAO.stop();

			// ====================================
			// Modifico lo stato del procedimento
			// ====================================
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// ========================================================================
			siesLogger.debug("Aggiornamento stato procedimento");
			StatoProcedimentoModel spm = new StatoProcedimentoModel();
			spm.setProgressivo(new BigDecimal(1));
			spm.setFasSieIdFascicoloSiep(emRic.getFasSieIdFascicoloSiep());
			spm.setData(emRic.getDataEmissione());
			spm.setCodStatoProcedimento("0367");
			spm.setCodOperatoreInserimento(em.getCodOperatoreAggiornamento());
			spm.setDataInserimento(em.getDataAggiornamento());
			spm.setCodUfficioInserimento(em.getCodUfficioAggiornamento());
			spDAO = new StatoProcedimentoDAO(c);
			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			spDAO.setCondizioneByIdFascicolo(emRic.getFasSieIdFascicoloSiep());
			spDAO.delete();
			// - Inserisce
			spDAO.setDAOFromModel(spm);
			spDAO.insert();
			spDAO.stop();
			siesLogger.debug("FINE Aggiornamento stato procedimento");

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			siesLogger.debug("Aggiornamento Blob");
			eDAOBlob = new EventoDAO(c);
			eDAOBlob.setDAOFromModelForUpdateBlob(em);
			eDAOBlob.selCondizioneUpdate(em.getIdEvento());
			eDAOBlob.update();
			eDAOBlob.stop();
			siesLogger.debug("Blob Aggiornato");

			commit(c);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exUpdateTrasmissioneAttiConversione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exUpdateTrasmissioneAttiConversione: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(esDAO);
			cleanup(spDAO);
			cleanup(eDAOBlob);

			cleanup(c);
		}
	}

	@Override
	public EventoNotificaModel exModificaTrasmissioneAttiConversione(EventoNotificaModel enm,
			String[] arrayIdRate, AnnotazioneManualeModel amm) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		NotificaDAO nDAO = null;
		AutoritaEsternaDAO aeDAO = null;
		RateizzazionePPDAO rPPDAO = null;
		AnnotazioneManualeDAO amDAO = null;

		EventoNotificaModel enmRet = new EventoNotificaModel(enm);

		try {
			c = getDBConnection();

			BigDecimal idEvento = enm.getEvento().getIdEvento();

			// Cancello preventivamente tutti i dati
			// Sgancio le rate dall'evento
			rPPDAO = new RateizzazionePPDAO(c);
			rPPDAO.setEveIdEvento(null);
			rPPDAO.selCondizioneByIdEvento(enm.getEvento().getIdEvento());
			rPPDAO.update();

			nDAO = new NotificaDAO(c);
			nDAO.setCondizioneEvento(idEvento);
			nDAO.delete();

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			aeDAO = new AutoritaEsternaDAO(c);
			BigDecimal keyAutorita = null;

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
							aeDAO.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) aeDAO.getModelByKey();
							if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								aeDAO.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
								keyAutorita = aeDAO.insert();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(keyAutorita);
							} else {
								keyAutorita = lAutMod.getIdAutoritaEsterna();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(keyAutorita);
							}
						}

						// ===========================================
						enm.getNotifiche()[count].setEveIdEvento(idEvento);
						nDAO = new NotificaDAO(c);
						nDAO.setDAOFromModel(enm.getNotifiche()[count]);
						BigDecimal keyNotifica = nDAO.insert();
						nDAO.stop();
						siesLogger.debug("Inserita Notifica " + keyNotifica);
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

			// aggiorrno l'annotazione manuale
			amDAO = new AnnotazioneManualeDAO(c);
			amDAO.setDAOFromModelForUpdate(amm);
			amDAO.update();
			amDAO.stop();

			// aggiorrno l'evento
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModelForUpdate(enm.getEvento());
			eDAO.update();
			eDAO.stop();

			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaTrasmissioneAttiConversione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaTrasmissioneAttiConversione: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(nDAO);
			cleanup(aeDAO);
			cleanup(rPPDAO);
			cleanup(amDAO);

			cleanup(c);
		}

		return enmRet;
	}

	@Override
	public BigDecimal exInserisciDefinizioneProcedimentoPP(EventoNotificaModel enm, String[] arrayIdRate,
			AnnotazioneManualeModel amm) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		NotificaDAO nDAO = null;
		AutoritaEsternaDAO aeDAO = null;
		RateizzazionePPDAO rPPDAO = null;
		AnnotazioneManualeDAO amDAO = null;

		BigDecimal idEvento = null;

		try {
			c = getDBConnection();

			// =========================================
			// Inserisco l'Evento
			// =========================================
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModel(enm.getEvento());
			idEvento = eDAO.insert();
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
							aeDAO.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel aem = new AutoritaEsternaModel();
							aem = (AutoritaEsternaModel) aeDAO.getModelByKey();
							if (aem == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								siesLogger.debug(
										"Ins Aut Est = " + enm.getNotifiche()[count].getAutoritaEsterna());
								aeDAO.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
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

			// inserisco l'annotazione manuale
			amm.setEveIdEvento(idEvento);
			amDAO = new AnnotazioneManualeDAO(c);
			amDAO.setDAOFromModel(amm);
			BigDecimal idAnnotazioneManuale = amDAO.insert();
			amDAO.stop();
			siesLogger.debug("idAnnotazioneManuale = " + idAnnotazioneManuale);

			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciDefinizioneProcedimentoPP: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exInserisciDefinizioneProcedimentoPP: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(nDAO);
			cleanup(aeDAO);
			cleanup(rPPDAO);
			cleanup(amDAO);

			cleanup(c);
		}
		return idEvento;
	}

	@Override
	public void exUpdateDefinizioneProcedimentoPP(EventoModel em) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		EventoSqlDAO esDAO = null;
		StatoProcedimentoDAO spDAO = null;
		EventoDAO eDAOBlob = null;

		try {
			c = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			esDAO = new EventoSqlDAO(c);
			esDAO.ricercaEventoByKey(em.getIdEvento());
			EventoModel emRic = (EventoModel) esDAO.getModelByKey();
			esDAO.stop();

			// ====================================
			// Modifico lo stato del procedimento
			// ====================================
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// ========================================================================
			siesLogger.debug("Aggiornamento stato procedimento");
			StatoProcedimentoModel spm = new StatoProcedimentoModel();
			spm.setProgressivo(new BigDecimal(1));
			spm.setFasSieIdFascicoloSiep(emRic.getFasSieIdFascicoloSiep());
			spm.setData(emRic.getDataEmissione());
			spm.setCodStatoProcedimento("0368");
			spm.setCodOperatoreInserimento(em.getCodOperatoreAggiornamento());
			spm.setDataInserimento(em.getDataAggiornamento());
			spm.setCodUfficioInserimento(em.getCodUfficioAggiornamento());
			spDAO = new StatoProcedimentoDAO(c);
			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			spDAO.setCondizioneByIdFascicolo(emRic.getFasSieIdFascicoloSiep());
			spDAO.delete();
			// - Inserisce
			spDAO.setDAOFromModel(spm);
			spDAO.insert();
			spDAO.stop();
			siesLogger.debug("FINE Aggiornamento stato procedimento");

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			siesLogger.debug("Aggiornamento Blob");
			eDAOBlob = new EventoDAO(c);
			eDAOBlob.setDAOFromModelForUpdateBlob(em);
			eDAOBlob.selCondizioneUpdate(em.getIdEvento());
			eDAOBlob.update();
			eDAOBlob.stop();
			siesLogger.debug("Blob Aggiornato");

			commit(c);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exUpdateDefinizioneProcedimentoPP: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exUpdateDefinizioneProcedimentoPP: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(esDAO);
			cleanup(spDAO);
			cleanup(eDAOBlob);

			cleanup(c);
		}
	}

	@Override
	public EventoNotificaModel exModificaDefinizioneProcedimentoPP(EventoNotificaModel enm,
			String[] arrayIdRate, AnnotazioneManualeModel amm) throws F3BException {

		Connection c = null;

		EventoDAO eDAO = null;
		NotificaDAO nDAO = null;
		AutoritaEsternaDAO aeDAO = null;
		RateizzazionePPDAO rPPDAO = null;
		AnnotazioneManualeDAO amDAO = null;

		EventoNotificaModel enmRet = new EventoNotificaModel(enm);

		try {
			c = getDBConnection();

			BigDecimal idEvento = enm.getEvento().getIdEvento();

			// Cancello preventivamente tutti i dati
			// Sgancio le rate dall'evento
			rPPDAO = new RateizzazionePPDAO(c);
			rPPDAO.setEveIdEvento(null);
			rPPDAO.selCondizioneByIdEvento(enm.getEvento().getIdEvento());
			rPPDAO.update();

			nDAO = new NotificaDAO(c);
			nDAO.setCondizioneEvento(idEvento);
			nDAO.delete();

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			aeDAO = new AutoritaEsternaDAO(c);
			BigDecimal keyAutorita = null;

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
							aeDAO.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) aeDAO.getModelByKey();
							if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								aeDAO.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
								keyAutorita = aeDAO.insert();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(keyAutorita);
							} else {
								keyAutorita = lAutMod.getIdAutoritaEsterna();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(keyAutorita);
							}
						}

						// ===========================================
						enm.getNotifiche()[count].setEveIdEvento(idEvento);
						nDAO = new NotificaDAO(c);
						nDAO.setDAOFromModel(enm.getNotifiche()[count]);
						BigDecimal keyNotifica = nDAO.insert();
						nDAO.stop();
						siesLogger.debug("Inserita Notifica " + keyNotifica);
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

			// aggiorrno l'annotazione manuale
			amDAO = new AnnotazioneManualeDAO(c);
			amDAO.setDAOFromModelForUpdate(amm);
			amDAO.update();
			amDAO.stop();

			// aggiorrno l'evento
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModelForUpdate(enm.getEvento());
			eDAO.update();
			eDAO.stop();

			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaDefinizioneProcedimentoPP: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exModificaDefinizioneProcedimentoPP: " + ex);
		} finally {
			cleanup(eDAO);
			cleanup(nDAO);
			cleanup(aeDAO);
			cleanup(rPPDAO);
			cleanup(amDAO);

			cleanup(c);
		}

		return enmRet;
	}

	/**
	 * Recupera le rateizzazioni collegata a un evento e i relativi bollettini se presenti
	 *
	 * @param aIdEvento
	 * @return
	 * @throws F3BException
	 */
	public Vector<RateizzazionePPModel> exRicercaRateizzazioniBollettiniByIdEvento(BigDecimal aIdEvento)
			throws F3BException {

		Vector<RateizzazionePPModel> lListaRate = new Vector<>();

		Connection c = null;

		RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
		BollettinoPagopaSqlDAO lBollettinoSqlDAO = null;

		try {
			c = getDBConnection();

			lRateizzazioneSqlDao = new RateizzazionePPSqlDAO(c);
			lRateizzazioneSqlDao.ricercaRateizzazionePPByEveIdEvento(aIdEvento);
			lListaRate = new Vector<RateizzazionePPModel>(lRateizzazioneSqlDao.getModels());
			lRateizzazioneSqlDao.stop();

			lBollettinoSqlDAO = new BollettinoPagopaSqlDAO(c);

			for (RateizzazionePPModel lRata : lListaRate) {
				lBollettinoSqlDAO.ricercaBollettinoPagopaByReteizzazione(lRata.getIdRateizzazionePP());
				Vector<BollettinoPagopaModel> lListaBollettini = new Vector<BollettinoPagopaModel>(
						lBollettinoSqlDAO.getModels());
				lRata.setListaBollettini(lListaBollettini);
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			throw new F3BException(
					"RateizzazionePPController.exRicercaRateizzazioniBollettiniByIdEvento: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			throw new F3BException(
					"RateizzazionePPController.exRicercaRateizzazioniBollettiniByIdEvento: " + ex);
		} finally {
			cleanup(lRateizzazioneSqlDao);
			cleanup(lBollettinoSqlDAO);

			cleanup(c);
		}

		return lListaRate;
	}

	@Override
	public Vector<EventoRateizzazionePPModel> exRicercaAvvisoMancatoPagamento(BigDecimal idFascicolo)
			throws F3BException {

		Connection c = null;

		RateizzazionePPSqlDAO rppsdao = null;
		EventoSqlDAO esdao = null;

		// Ricerca gli eventi per id Fascicolo
		EventoModel em = new EventoModel();
		em.setFlagDocumentoRegistrato("S");
		em.setFasSieIdFascicoloSiep(idFascicolo);
		String[] motivi = new String[] { "0622", "1307", "1308" };
		em.setCodTipoEvento("01");

		// Ricerca gli eventi per id Fascicolo
		Vector<EventoRateizzazionePPModel> listaEventoRateizzazioniPP = new Vector<>();

		try {
			c = getDBConnection();
			esdao = new EventoSqlDAO(c);
			esdao.ricercaAvvisoMancatoPagamento(motivi, em);
			List<EventoModel> listaEventi = new ArrayList(esdao.getModels());
			esdao.stop();
			if (!listaEventi.isEmpty() && listaEventi.size() > 1)
				listaEventi = reverseList(listaEventi);

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
			throw new F3BException("RateizzazionePPController.exRicercaAvvisoMancatoPagamento: " + daoEx);
		} finally {
			cleanup(rppsdao);
			cleanup(esdao);
			cleanup(c);
		}

		return listaEventoRateizzazioniPP;
	}

	@Override
	public Vector<RateizzazionePPModel> exRicercaMancatiPagamentiUnicaSoluzione(BigDecimal idFascicoloSiep)
			throws F3BException {

		Vector<RateizzazionePPModel> rppVector = new Vector<>();

		Connection c = null;

		RateizzazionePPSqlDAO rppsDAO = null;
		BollettinoPagopaSqlDAO bpsDAO = null;

		// Ricerca le rateizzazioni per id Fascicolo
		RateizzazionePPModel rppmRic = new RateizzazionePPModel();
		rppmRic.setFasSieIdFascicoloSiep(idFascicoloSiep);
		rppmRic.setTipoRateizzazione("U");
		rppmRic.setNumeroRate(new BigDecimal(1));
		rppmRic.setProgressivoRata(new BigDecimal(1));

		try {
			c = getDBConnection();

			rppsDAO = new RateizzazionePPSqlDAO(c);
			bpsDAO = new BollettinoPagopaSqlDAO(c);

			rppsDAO.ricercaRateizzazionePP(rppmRic, "IS NOT NULL");

			rppsDAO.start();
			while (rppsDAO.next())
				rppVector.add((RateizzazionePPModel) rppsDAO.getModel());
			rppsDAO.stop();

			for (RateizzazionePPModel rppm : rppVector) {
				bpsDAO.ricercaBollettinoPagopaByReteizzazione(rppm.getIdRateizzazionePP());
				Vector<BollettinoPagopaModel> bpmVector = new Vector<BollettinoPagopaModel>(
						bpsDAO.getModels());
				rppm.setListaBollettini(bpmVector);
			}

			commit(c);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(c);
			throw new F3BException(
					"RateizzazionePPController.exRicercaRateizzazioniLibereByIdFasc: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(c);
			throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniLibereByIdFasc: " + ex);
		} finally {
			cleanup(rppsDAO);
			cleanup(bpsDAO);

			cleanup(c);
		}

		return rppVector;
	}
	// ***** FINE INTERVENTO MEV_2023-33 *****//

}