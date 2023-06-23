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
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
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

		Connection lConn = null;

		RateizzazionePPDAO lRateizzazioneDao = null;
		try {
			lConn = getDBTransaction();

			lRateizzazioneDao = new RateizzazionePPDAO(lConn);

			siesLogger.debug("Ciclo caricamento rate. Num rate = " + aListaRate.size());
			for (int i = 0; i < aListaRate.size(); i++) {
				RateizzazionePPModel lRataModel = aListaRate.elementAt(i);
				lRateizzazioneDao.setDAOFromModel(lRataModel);
				lRateizzazioneDao.insert();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(lConn);
			throw new F3BException("RateizzazionePPController.exInserisciRateizzazioni: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(lConn);
			throw new F3BException("RateizzazionePPController.exInserisciRateizzazioni: " + ex);
		} finally {
			cleanup(lRateizzazioneDao);

			cleanup(lConn);
		}

		return;
	}

	public Vector<RateizzazionePPModel> exRicercaRateizzazioniByIdFasc(BigDecimal aIdFasc)
			throws F3BException {

		Vector<RateizzazionePPModel> lListaRate = new Vector<>();

		Connection lConn = null;

		RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
		BollettinoPagopaSqlDAO lBollettinoSqlDAO = null;
		
		try {
			lConn = getDBConnection();

			lRateizzazioneSqlDao = new RateizzazionePPSqlDAO (lConn);
			lBollettinoSqlDAO = new BollettinoPagopaSqlDAO (lConn);

			lRateizzazioneSqlDao.ricercaRateizzazionePPByIdFasSIEP(aIdFasc);

			lRateizzazioneSqlDao.start();
			while (lRateizzazioneSqlDao.next()) {
				lListaRate.add((RateizzazionePPModel) lRateizzazioneSqlDao.getModel());
			}
			lRateizzazioneSqlDao.stop();

			for (RateizzazionePPModel lRata:  lListaRate) {
			    lBollettinoSqlDAO.ricercaBollettinoPagopaByReteizzazione (lRata.getIdRateizzazionePP()); 
			    Vector <BollettinoPagopaModel> lListaBollettini = new Vector <BollettinoPagopaModel> (lBollettinoSqlDAO.getModels());
			    lRata.setListaBollettini (lListaBollettini);
			}
			
			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(lConn);
			throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdFasc: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(lConn);
			throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdFasc: " + ex);
		} finally {
			cleanup(lRateizzazioneSqlDao);

			cleanup(lConn);
		}

		return lListaRate;
	}

	public void exCancellaRateizzazioniByIdFasc(BigDecimal aIdFasc) throws F3BException {

		Connection lConn = null;

		RateizzazionePPDAO lRateizzazioneDao = null;
        RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
		BollettinoPagopaDAO lBollettiniDao = null;
		
		try {
			lConn = getDBConnection();
			
			lRateizzazioneSqlDao = new RateizzazionePPSqlDAO(lConn);
			lRateizzazioneDao = new RateizzazionePPDAO(lConn);
			lBollettiniDao = new BollettinoPagopaDAO (lConn);
			
			
			lRateizzazioneSqlDao.ricercaRateizzazionePPByIdFascicoloSiep(aIdFasc);
			Vector <RateizzazionePPModel> lListaRate = new Vector <RateizzazionePPModel> (lRateizzazioneSqlDao.getModels());
			for (RateizzazionePPModel lrata : lListaRate)
			{
	            lBollettiniDao.selCondizioneDeleteByIdRata (lrata.getIdRateizzazionePP());
	            lBollettiniDao.delete();
	            
	            lRateizzazioneDao.selCondizioneUpdate(lrata.getIdRateizzazionePP());
	            lRateizzazioneDao.delete();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(lConn);
			throw new F3BException("RateizzazionePPController.exCancellaRateizzazioniByIdFasc: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(lConn);
			throw new F3BException("RateizzazionePPController.exCancellaRateizzazioniByIdFasc: " + ex);
		} finally {
			cleanup(lRateizzazioneDao);
			cleanup(lBollettiniDao);
			cleanup(lRateizzazioneSqlDao);
			
			cleanup(lConn);
		}
	}

	public void exModificaRateizzazioni(Vector<RateizzazionePPModel> aListaRate, BigDecimal aIdFasc)
			throws F3BException {

		Connection lConn = null;

        RateizzazionePPDAO lRateizzazioneDao = null;
        RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
        BollettinoPagopaDAO lBollettiniDao = null;
        
		try {
			lConn = getDBTransaction();

            lRateizzazioneSqlDao = new RateizzazionePPSqlDAO(lConn);
            lRateizzazioneDao = new RateizzazionePPDAO(lConn);
            lBollettiniDao = new BollettinoPagopaDAO (lConn);

			siesLogger.debug("Cancello le precedenti rate.");
            lRateizzazioneSqlDao.ricercaRateizzazionePPByIdFascicoloSiep(aIdFasc);
            Vector <RateizzazionePPModel> lListaRate = new Vector <RateizzazionePPModel> (lRateizzazioneSqlDao.getModels());
            for (RateizzazionePPModel lrata : lListaRate)
            {
                lBollettiniDao.selCondizioneDeleteByIdRata (lrata.getIdRateizzazionePP());
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

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(lConn);
			throw new F3BException("RateizzazionePPController.exModificaRateizzazioni: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(lConn);
			throw new F3BException("RateizzazionePPController.exModificaRateizzazioni: " + ex);
		} finally {
            cleanup(lRateizzazioneDao);
            cleanup(lBollettiniDao);
            cleanup(lRateizzazioneSqlDao);

			cleanup(lConn);
		}

		return;
	}

	public Vector<RateizzazionePPModel> exRicercaRateizzazioniByIdEvento(BigDecimal aIdEvento)
			throws F3BException {

		Vector<RateizzazionePPModel> lListaRate = new Vector<>();

		Connection lConn = null;

		RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
		try {
			lConn = getDBConnection();

			lRateizzazioneSqlDao = new RateizzazionePPSqlDAO(lConn);

			lRateizzazioneSqlDao.ricercaRateizzazionePPByEveIdEvento(aIdEvento);

			lRateizzazioneSqlDao.start();
			while (lRateizzazioneSqlDao.next()) {
				lListaRate.add((RateizzazionePPModel) lRateizzazioneSqlDao.getModel());
			}
			lRateizzazioneSqlDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(lConn);
			throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdEvento: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(lConn);
			throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdEvento: " + ex);
		} finally {
			cleanup(lRateizzazioneSqlDao);

			cleanup(lConn);
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

}