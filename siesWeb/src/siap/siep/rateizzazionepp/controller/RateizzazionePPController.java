package siap.siep.rateizzazionepp.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.rateizzazionepp.dao.RateizzazionePPDAO;
import siap.siep.rateizzazionepp.dao.RateizzazionePPSqlDAO;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;

public class RateizzazionePPController extends SiapController implements IRateizzazionePP {
    
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
    /**
     * 
     */
    public void exInserisciRateizzazioni (Vector <RateizzazionePPModel> aListaRate) throws F3BException
    {
        Connection lConn = null;
        
        RateizzazionePPDAO lRateizzazioneDao = null;
        try {
            lConn = getDBTransaction();
            
            lRateizzazioneDao = new RateizzazionePPDAO(lConn);
            
            siesLogger.debug("Ciclo caricamento rate. Num rate = "+aListaRate.size());
            for (int i=0; i<aListaRate.size(); i++) {
                RateizzazionePPModel lRataModel = aListaRate.elementAt(i);
                lRateizzazioneDao.setDAOFromModel(lRataModel);
                lRateizzazioneDao.insert();
            }
            
            commit(lConn);
        } catch (DAOException daoEx) {
            siesLogger.error("DAOException",daoEx);
            rollback(lConn);
            throw new F3BException("RateizzazionePPController.exInserisciRateizzazioni: " + daoEx);
        } catch (Exception ex) {
            siesLogger.error("Exception",ex);
            rollback(lConn);
            throw new F3BException("RateizzazionePPController.exInserisciRateizzazioni: " + ex);
        } finally {
            cleanup(lRateizzazioneDao);

            cleanup(lConn);
        }

        return;        
    }
    
    /**
     * 
     */
    public Vector <RateizzazionePPModel> exRicercaRateizzazioniByIdFasc (BigDecimal aIdFasc) throws F3BException
    {   
        Vector <RateizzazionePPModel> lListaRate = new Vector <RateizzazionePPModel> ();
        
        Connection lConn = null;
        
        RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
        try {
            lConn = getDBConnection();
            
            lRateizzazioneSqlDao = new RateizzazionePPSqlDAO (lConn);           
                        
            lRateizzazioneSqlDao.ricercaRateizzazionePPByIdFasSIEP (aIdFasc);

            lRateizzazioneSqlDao.start();
            while (lRateizzazioneSqlDao.next()) {
                lListaRate.add((RateizzazionePPModel)lRateizzazioneSqlDao.getModel());
            }
            lRateizzazioneSqlDao.stop();
            
            commit(lConn);
        } catch (DAOException daoEx) {
            siesLogger.error("DAOException",daoEx);
            rollback(lConn);
            throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdFasc: " + daoEx);
        } catch (Exception ex) {
            siesLogger.error("Exception",ex);
            rollback(lConn);
            throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdFasc: " + ex);
        } finally {
            cleanup(lRateizzazioneSqlDao);

            cleanup(lConn);
        }        
        
        return lListaRate;
    }
    
    
    /**
     * 
     */
    public void exCancellaRateizzazioniByIdFasc (BigDecimal aIdFasc) throws F3BException
    {
        Connection lConn = null;
        
        RateizzazionePPDAO lRateizzazioneDao = null;
        try {
            lConn = getDBConnection();
            
            lRateizzazioneDao = new RateizzazionePPDAO(lConn);
            
            lRateizzazioneDao.selCondizioneByIdFasSiep(aIdFasc);
            lRateizzazioneDao.delete();
            
            commit(lConn);
        } catch (DAOException daoEx) {
            siesLogger.error("DAOException",daoEx);
            rollback(lConn);
            throw new F3BException("RateizzazionePPController.exCancellaRateizzazioniByIdFasc: " + daoEx);
        } catch (Exception ex) {
            siesLogger.error("Exception",ex);
            rollback(lConn);
            throw new F3BException("RateizzazionePPController.exCancellaRateizzazioniByIdFasc: " + ex);
        } finally {
            cleanup(lRateizzazioneDao);

            cleanup(lConn);
        }       
    }
    
    /**
     * 
     */
    public void exModificaRateizzazioni (Vector <RateizzazionePPModel> aListaRate, BigDecimal aIdFasc) throws F3BException
    {
        Connection lConn = null;
        
        RateizzazionePPDAO lRateizzazioneDao = null;
        try {
            lConn = getDBTransaction();
            
            lRateizzazioneDao = new RateizzazionePPDAO(lConn);
            
            siesLogger.debug("Cancello le precedenti rate.");
            lRateizzazioneDao.selCondizioneByIdFasSiep(aIdFasc);
            lRateizzazioneDao.delete();
            
            siesLogger.debug("Ciclo caricamento rate. Num rate = "+aListaRate.size());
            for (int i=0; i<aListaRate.size(); i++) {
                RateizzazionePPModel lRataModel = aListaRate.elementAt(i);
                lRateizzazioneDao.setDAOFromModel(lRataModel);
                lRateizzazioneDao.insert();
            }
            
            commit(lConn);
        } catch (DAOException daoEx) {
            siesLogger.error("DAOException",daoEx);
            rollback(lConn);
            throw new F3BException("RateizzazionePPController.exModificaRateizzazioni: " + daoEx);
        } catch (Exception ex) {
            siesLogger.error("Exception",ex);
            rollback(lConn);
            throw new F3BException("RateizzazionePPController.exModificaRateizzazioni: " + ex);
        } finally {
            cleanup(lRateizzazioneDao);

            cleanup(lConn);
        }

        return;        
    } 
    
    /**
     * 
     */
    public Vector <RateizzazionePPModel> exRicercaRateizzazioniByIdEvento (BigDecimal aIdEvento) throws F3BException
    {   
        Vector <RateizzazionePPModel> lListaRate = new Vector <RateizzazionePPModel> ();
        
        Connection lConn = null;
        
        RateizzazionePPSqlDAO lRateizzazioneSqlDao = null;
        try {
            lConn = getDBConnection();
            
            lRateizzazioneSqlDao = new RateizzazionePPSqlDAO (lConn);           
                        
            lRateizzazioneSqlDao.ricercaRateizzazionePPByEveIdEvento (aIdEvento);

            lRateizzazioneSqlDao.start();
            while (lRateizzazioneSqlDao.next()) {
                lListaRate.add((RateizzazionePPModel)lRateizzazioneSqlDao.getModel());
            }
            lRateizzazioneSqlDao.stop();
            
            commit(lConn);
        } catch (DAOException daoEx) {
            siesLogger.error("DAOException",daoEx);
            rollback(lConn);
            throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdEvento: " + daoEx);
        } catch (Exception ex) {
            siesLogger.error("Exception",ex);
            rollback(lConn);
            throw new F3BException("RateizzazionePPController.exRicercaRateizzazioniByIdEvento: " + ex);
        } finally {
            cleanup(lRateizzazioneSqlDao);

            cleanup(lConn);
        }        
        
        return lListaRate;
    }
        
    
}
