package siap.siep.calcolopenadl92.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import siap.dao.SIAPTableDAO;
import siap.siep.calcolopena.model.SemestreDL92Model;

/**
 * DAO di accesso alla tabella SEMESTRI_LA_DL92
 * 
 * @since MEV_2026-1
 */
public class SemestreDL92DAO extends SIAPTableDAO 
{
    public SemestreDL92DAO (Connection con) {
        super(con);
        
        setTable("SEMESTRI_LA_DL92");

        // Settare la Sequence e i campi chiave
        setSequenceField("ID_SEMESTRI_LA_DL92", "SEMESTRI_LA_DL92_SEQ");

        setFieldKey("ID_SEMESTRI_LA_DL92", BIG_DECIMAL);
        setField("CALC_ID_CALCOLO_PENA_DL92", BIG_DECIMAL);
        
        setField("PROGRESSIVO", BIG_DECIMAL);
        setField("NUM_SEMESTRI_MATURATI", BIG_DECIMAL);
        
        setField("RESIDUO_NUM_ANNI", BIG_DECIMAL);
        setField("RESIDUO_NUM_MESI", BIG_DECIMAL);
        setField("RESIDUO_NUM_GIORNI", BIG_DECIMAL);
        
        setField("LA_APPLICATE", BIG_DECIMAL);
        
        setField("IS_PRESOFFERTO", STRING); 
        
        setField("DATA_MATURAZIONE_LA", DATE);    
        setField("NUOVA_DATA_SCADENZA_PENA", DATE);   
        setField("IS_COMPRESO", STRING);   
        
        setField("GIORNI_RESIDUI_PRESOFFERTO", BIG_DECIMAL);
        
        setField("COD_OPERATORE_INSERIMENTO", STRING);
        setField("DATA_INSERIMENTO", DATE);
        setField("COD_UFFICIO_INSERIMENTO", STRING);
        setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
        setField("DATA_AGGIORNAMENTO", DATE);
        setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    }
    
    //
    // METODI GET()
    //
    public BigDecimal getIdSemestriLaDl92()         throws DAOException  { return getBigDecimal("ID_SEMESTRI_LA_DL92"); }
    public BigDecimal getCalcIdCalcoloPenaDl92()    throws DAOException  { return getBigDecimal("CALC_ID_CALCOLO_PENA_DL92"); }
    public BigDecimal getProgressivo()              throws DAOException  { return getBigDecimal("PROGRESSIVO"); }
    public BigDecimal getNumSemestriMaturati()      throws DAOException  { return getBigDecimal("NUM_SEMESTRI_MATURATI"); }
    public BigDecimal getResiduoNumAnni()           throws DAOException  { return getBigDecimal("RESIDUO_NUM_ANNI"); }
    public BigDecimal getResiduoNumMesi()           throws DAOException  { return getBigDecimal("RESIDUO_NUM_MESI"); }
    public BigDecimal getResiduoNumGiorni()         throws DAOException  { return getBigDecimal("RESIDUO_NUM_GIORNI"); }
    public BigDecimal getLAApplicate()              throws DAOException  { return getBigDecimal("LA_APPLICATE"); }
    public String     getIsPresofferto()            throws DAOException  { return getString    ("IS_PRESOFFERTO"); }
    public Date       getDataMaturazioneLA()        throws DAOException  { return getDate      ("DATA_MATURAZIONE_LA"); }
    public Date       getNuovaDataScadenzaPena()    throws DAOException  { return getDate      ("NUOVA_DATA_SCADENZA_PENA"); }
    public BigDecimal getGiorniResiduiPresofferto() throws DAOException  { return getBigDecimal("GIORNI_RESIDUI_PRESOFFERTO"); }
    public String     getIsCompreso()               throws DAOException  { return getString    ("IS_COMPRESO"); }
    
    public String getCodOperatoreInserimento()    throws DAOException  { return getString("COD_OPERATORE_INSERIMENTO"); }
    public Date   getDataInserimento()            throws DAOException  { return getDate  ("DATA_INSERIMENTO"); }
    public String getCodUfficioInserimento()      throws DAOException  { return getString("COD_UFFICIO_INSERIMENTO"); }
    public String getCodOperatoreAggiornamento()  throws DAOException  { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
    public Date   getDataAggiornamento()          throws DAOException  { return getDate  ("DATA_AGGIORNAMENTO"); }
    public String getCodUfficioAggiornamento()    throws DAOException  { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
    
    
    //
    // METODI SET()
    // 
    public void setIdSemestriLaDl92          (BigDecimal aValore) { setBigDecimal("ID_SEMESTRI_LA_DL92", aValore); }
    public void setCalcIdCalcoloPenaDl92     (BigDecimal aValore) { setBigDecimal("CALC_ID_CALCOLO_PENA_DL92", aValore); }
    public void setProgressivo               (BigDecimal aValore) { setBigDecimal("PROGRESSIVO", aValore); }
    public void setResiduoNumAnni            (BigDecimal aValore) { setBigDecimal("RESIDUO_NUM_ANNI", aValore); }
    public void setResiduoNumMesi            (BigDecimal aValore) { setBigDecimal("RESIDUO_NUM_MESI", aValore); }
    public void setResiduoNumGiorni          (BigDecimal aValore) { setBigDecimal("RESIDUO_NUM_GIORNI", aValore); }
    public void setLAApplicate               (BigDecimal aValore) { setBigDecimal("LA_APPLICATE", aValore); }
    public void setDataMaturazioneLA         (Date aValore)       { setDate      ("DATA_MATURAZIONE_LA", aValore); }
    public void setNuovaDataScadenzaPena     (Date aValore)       { setDate      ("NUOVA_DATA_SCADENZA_PENA", aValore); }
    public void setGiorniResiduiPresofferto  (BigDecimal aValore) { setBigDecimal("GIORNI_RESIDUI_PRESOFFERTO", aValore); }
    public void setNumSemestriMaturati       (BigDecimal aValore) { setBigDecimal("NUM_SEMESTRI_MATURATI", aValore); }
    public void setIsPresofferto             (String aValore)     { setString    ("IS_PRESOFFERTO", aValore); }
    public void setIsCompreso                (String aValore)     { setString    ("IS_COMPRESO", aValore); }
      
    public void setCodOperatoreInserimento   (String aValore ) { setString ("COD_OPERATORE_INSERIMENTO", aValore); }
    public void setDataInserimento           (Date   aValore ) { setDate   ("DATA_INSERIMENTO", aValore); }
    public void setCodUfficioInserimento     (String aValore ) { setString ("COD_UFFICIO_INSERIMENTO", aValore); }
    public void setCodOperatoreAggiornamento (String aValore ) { setString ("COD_OPERATORE_AGGIORNAMENTO", aValore); }
    public void setDataAggiornamento         (Date   aValore ) { setDate   ("DATA_AGGIORNAMENTO", aValore); }
    public void setCodUfficioAggiornamento   (String aValore ) { setString ("COD_UFFICIO_AGGIORNAMENTO", aValore); }     

    
    public void setDAOFromModel (SemestreDL92Model aModel) throws DAOException
    {
        setIdSemestriLaDl92          ( aModel.getIdSemestriLaDl92() );
        setCalcIdCalcoloPenaDl92     ( aModel.getCalcIdCalcoloPenaDl92() );
        setProgressivo               ( aModel.getProgressivo() );
        setResiduoNumAnni            ( aModel.getResiduoNumAnni() );
        setResiduoNumMesi            ( aModel.getResiduoNumMesi() );
        setResiduoNumGiorni          ( aModel.getResiduoNumGiorni() );
        setLAApplicate               ( aModel.getLAApplicate() );
        setDataMaturazioneLA         ( aModel.getDataMaturazioneLA() );
        setNuovaDataScadenzaPena     ( aModel.getNuovaDataScadenzaPena() );
        setGiorniResiduiPresofferto  ( aModel.getGiorniResiduiPresofferto() );
        setNumSemestriMaturati       ( aModel.getNumSemestriMaturati() );
        setIsPresofferto             ( aModel.getIsPresofferto() );
        setIsCompreso                ( aModel.getIsCompreso() );     
        
        setCodOperatoreInserimento ( aModel.getCodOperatoreInserimento() );
        setDataInserimento         ( aModel.getDataInserimento() );
        setCodUfficioInserimento   ( aModel.getCodUfficioInserimento() );
        
        setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );
        setDataAggiornamento         ( aModel.getDataAggiornamento() );
        setCodUfficioAggiornamento   ( aModel.getCodUfficioAggiornamento() );
    }
    
    public void setDAOFromModelForUpdate (SemestreDL92Model aModel) throws DAOException
    {
        //setIdSemestriLaDl92          ( aModel.getIdSemestriLaDl92() );
        //setCalcIdCalcoloPenaDl92     ( aModel.getCalcIdCalcoloPenaDl92() );
        setProgressivo               ( aModel.getProgressivo() );
        setResiduoNumAnni            ( aModel.getResiduoNumAnni() );
        setResiduoNumMesi            ( aModel.getResiduoNumMesi() );
        setResiduoNumGiorni          ( aModel.getResiduoNumGiorni() );
        setLAApplicate               ( aModel.getLAApplicate() );
        setDataMaturazioneLA         ( aModel.getDataMaturazioneLA() );
        setNuovaDataScadenzaPena     ( aModel.getNuovaDataScadenzaPena() );
        setGiorniResiduiPresofferto  ( aModel.getGiorniResiduiPresofferto() );
        setNumSemestriMaturati       ( aModel.getNumSemestriMaturati() );
        setIsPresofferto             ( aModel.getIsPresofferto() );
        setIsCompreso                ( aModel.getIsCompreso() );     
        
        //setCodOperatoreInserimento ( aModel.getCodOperatoreInserimento() );
        //setDataInserimento         ( aModel.getDataInserimento() );
        //setCodUfficioInserimento   ( aModel.getCodUfficioInserimento() );
        
        setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );
        setDataAggiornamento         ( aModel.getDataAggiornamento() );
        setCodUfficioAggiornamento   ( aModel.getCodUfficioAggiornamento() );  
 
        // Update per chiave record
        setCondizioneUpdate (aModel.getIdSemestriLaDl92());
    }

    public void setCondizioneUpdate (BigDecimal key)
    {
      setCondition(" ID_SEMESTRI_LA_DL92 = " + key );
    } 
    
    public void setCondizioneByIdCalc (BigDecimal key)
    {
      setCondition(" CALC_ID_CALCOLO_PENA_DL92 = " + key );
    } 
    
}
