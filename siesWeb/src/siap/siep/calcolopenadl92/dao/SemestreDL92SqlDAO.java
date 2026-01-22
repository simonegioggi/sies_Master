package siap.siep.calcolopenadl92.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.calcolopena.model.SemestreDL92Model;

/**
 * SqlDAO di accesso alla tabella SEMESTRI_LA_DL92
 * 
 * @since MEV_2026-1
 */
public class SemestreDL92SqlDAO extends SIAPSqlDAO {
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
    public SemestreDL92SqlDAO (Connection con) {
        super(con);
    }
    
    protected String getSqlQuery() {
        String lStatement = new String("");
        
        lStatement += " SELECT ID_SEMESTRI_LA_DL92, CALC_ID_CALCOLO_PENA_DL92 ";
        lStatement +=       ", PROGRESSIVO, NUM_SEMESTRI_MATURATI ";
        lStatement +=       ", RESIDUO_NUM_ANNI, RESIDUO_NUM_MESI, RESIDUO_NUM_GIORNI ";
        lStatement +=       ", LA_APPLICATE, IS_PRESOFFERTO ";
        lStatement +=       ", DATA_MATURAZIONE_LA, NUOVA_DATA_SCADENZA_PENA ";
        lStatement +=       ", IS_COMPRESO, GIORNI_RESIDUI_PRESOFFERTO ";
        lStatement +=       ", COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO ";
        lStatement +=       ", COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO ";
        lStatement += " FROM SEMESTRI_LA_DL92 ";
        lStatement += " WHERE 1=1 ";
        
        return lStatement;
    }
    
    public void ricercaSemestriByIdCalc (BigDecimal aKey) throws DAOException {
        String lSql = getSqlQuery();

        lSql += " AND CALC_ID_CALCOLO_PENA_DL92 = " + aKey;
        lSql += " ORDER BY PROGRESSIVO ";
        setStatement(lSql);
    }
    
    public GenericModel getModel() throws DAOException {
        SemestreDL92Model aModel = new SemestreDL92Model(); 

        aModel.setIdSemestriLaDl92          ( getBigDecimal("ID_SEMESTRI_LA_DL92"));
        aModel.setCalcIdCalcoloPenaDl92     ( getBigDecimal("CALC_ID_CALCOLO_PENA_DL92"));
        aModel.setProgressivo               ( getBigDecimal("PROGRESSIVO"));
        aModel.setResiduoNumAnni            ( getBigDecimal("RESIDUO_NUM_ANNI"));
        aModel.setResiduoNumMesi            ( getBigDecimal("RESIDUO_NUM_MESI"));
        aModel.setResiduoNumGiorni          ( getBigDecimal("RESIDUO_NUM_GIORNI"));
        aModel.setLAApplicate               ( getBigDecimal("LA_APPLICATE"));
        aModel.setDataMaturazioneLA         ( getDate      ("DATA_MATURAZIONE_LA"));
        aModel.setNuovaDataScadenzaPena     ( getDate      ("NUOVA_DATA_SCADENZA_PENA"));
        aModel.setGiorniResiduiPresofferto  ( getBigDecimal("GIORNI_RESIDUI_PRESOFFERTO"));
        aModel.setNumSemestriMaturati       ( getBigDecimal("NUM_SEMESTRI_MATURATI"));
        aModel.setIsPresofferto             ( getString    ("IS_PRESOFFERTO"));
        aModel.setIsCompreso                ( getString    ("IS_COMPRESO"));
        
        aModel.setCodOperatoreInserimento (getString("COD_OPERATORE_INSERIMENTO"));
        aModel.setDataInserimento         (getDate("DATA_INSERIMENTO"));
        aModel.setCodUfficioInserimento   (getString("COD_UFFICIO_INSERIMENTO"));

        aModel.setCodOperatoreAggiornamento (getString("COD_OPERATORE_AGGIORNAMENTO"));
        aModel.setDataAggiornamento         (getDate("DATA_AGGIORNAMENTO"));
        aModel.setCodUfficioAggiornamento   (getString("COD_UFFICIO_AGGIORNAMENTO"));
        
        return aModel;
    } 
    
}
