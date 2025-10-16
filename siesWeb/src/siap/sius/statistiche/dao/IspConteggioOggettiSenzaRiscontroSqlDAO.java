package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.sius.statistiche.model.IspConteggioOggettiModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

public class IspConteggioOggettiSenzaRiscontroSqlDAO extends SqlDAO {
    
    public IspConteggioOggettiSenzaRiscontroSqlDAO(Connection conn)
    {
        super(conn);
    }

    protected String getSqlQuery(String aFasSiuChiaveUfficio)
    {
       String lStatement = 
                       "SELECT " +
                           "0 AS NUM_DEF_ESITO6, " +
                           "0 AS NUM_DEF_ESITO5, " +
                           "0 AS NUM_DEF_ESITO4, " +
                           "0 AS NUM_DEF_ESITO3, " +
                           "0 AS NUM_PENDENTI_FINE, " +
                           "ISP_MOTIVO_OGGETTO.DESC_MOTIVO AS DESC_OGGETTO, " + // Il model IspConteggioOggettoModel, che popola IspConteggioOggettiDAO inserisce DESC_MOTIVO nel campo DESC_OGGETT
                           "0 AS NUM_DEF_ESITO2, " +
                           "0 AS NUM_PENDENTI_INIZIO, " +
                           "0 AS NUM_DEF_ESITO1, " +
                           "ISP_MOTIVO_OGGETTO.COD_MOTIVO AS COD_OGGETTO, " + // Il model IspConteggioOggettoModel, che popola IspConteggioOggettiDAO inserisce COD_MOTIVO nel campo COD_OGGETT
                           "0 AS NUM_UNIFICATI, " +
                           "0 AS NUM_DEF_ISC_ERR, " +
                           "0 AS NUM_SOPRAVVENUTI, " +
                           "0 AS NUM_CANCELLATI, " +
                           "0 AS NUM_APP_PROVV, " + // MEV_2019-09
                           "NVL(ISP_OGGETTO_PROCEDIMENTO.ISP_COD_OGGETTO_PROCEDIMENTO, '') AS DESC_CONTENUTO_STATIS, " +
                           "'" + aFasSiuChiaveUfficio + "' AS FAS_SIU_CHIAVE_UFFICIO  " +
                       "FROM " +
                           "ISP_MOTIVO_OGGETTO " +
                           "LEFT JOIN ISP_OGGETTO_PROCEDIMENTO ON ISP_MOTIVO_OGGETTO.COD_OGGETTO = ISP_OGGETTO_PROCEDIMENTO.COD_OGGETTO_PROCEDIMENTO " +
                       "WHERE " +
                           "ISP_MOTIVO_OGGETTO.TIPO_UFFICIO IN " +
                           "(" +
                               "SELECT " +
                                   "COD_TIPO_UFFICIO " + 
                               "FROM " +
                                   "UFFICIO " +
                               "WHERE " +
                                   "COD_UFFICIO = '" + aFasSiuChiaveUfficio + "' " +
                           ")" +
                           "AND " +
                           "ISP_MOTIVO_OGGETTO.COD_MOTIVO NOT IN " +
                           "( " +
                               "SELECT " +
                                   "COD_OGGETTO " + // ISP_CONTEGGIO_OGGETTI contiente, nel campo COD_OGGETTO, il valore di COD_MOTIVO
                               "FROM  " +
                                   "ISP_CONTEGGIO_OGGETTI " +
                               "WHERE " +
                                   "FAS_SIU_CHIAVE_UFFICIO = '" + aFasSiuChiaveUfficio + "' " +
                           ") " +
                       "ORDER BY " +
                           "ISP_MOTIVO_OGGETTO.DESC_OGGETTO ";
           
       return lStatement;
   }
    
    public GenericModel getModel() throws DAOException
    {
        IspConteggioOggettiModel aModel = new IspConteggioOggettiModel();

        aModel.setNumDefEsito6(getBigDecimal("NUM_DEF_ESITO6"));
        aModel.setNumDefEsito5(getBigDecimal("NUM_DEF_ESITO5"));
        aModel.setNumDefEsito4(getBigDecimal("NUM_DEF_ESITO4"));
        aModel.setNumDefEsito3(getBigDecimal("NUM_DEF_ESITO3"));
        aModel.setNumPendentiFine(getBigDecimal("NUM_PENDENTI_FINE"));
        aModel.setDescOggetto(getString("DESC_OGGETTO"));
        aModel.setNumDefEsito2(getBigDecimal("NUM_DEF_ESITO2"));
        aModel.setNumPendentiInizio(getBigDecimal("NUM_PENDENTI_INIZIO"));
        aModel.setNumDefEsito1(getBigDecimal("NUM_DEF_ESITO1"));
        aModel.setCodOggetto(getString("COD_OGGETTO"));
        aModel.setNumUnificati(getBigDecimal("NUM_UNIFICATI"));
        aModel.setNumDefIscErr(getBigDecimal("NUM_DEF_ISC_ERR"));
        aModel.setNumSopravvenuti(getBigDecimal("NUM_SOPRAVVENUTI"));
        aModel.setNumCancellati(getBigDecimal("NUM_CANCELLATI"));
        // MEV_2019-09
        aModel.setNumAccoltiProvv(getBigDecimal("NUM_APP_PROVV"));
        // MEV_2019-09 - FINE        
        aModel.setDescContenutoStatis(getString("DESC_CONTENUTO_STATIS"));
        aModel.setFasSiuChiaveUfficio(getString("FAS_SIU_CHIAVE_UFFICIO"));

        return aModel;
    }
    
    public void ricercaOggettiSenzaRiscontro(String aFasSiuChiaveUfficio) throws DAOException
    {
       String lSql = getSqlQuery(aFasSiuChiaveUfficio);

       setStatement(lSql);
    }
}
