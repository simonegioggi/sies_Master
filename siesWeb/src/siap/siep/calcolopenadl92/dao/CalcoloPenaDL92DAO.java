package siap.siep.calcolopenadl92.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import siap.dao.SIAPTableDAO;
import siap.siep.calcolopenadl92.model.CalcoloPenaDL92ModelDB;

/**
 * DAO di accesso alla tabella CALCOLO_PENA_DL92
 * 
 * @since MEV_2026-1
 */
public class CalcoloPenaDL92DAO extends SIAPTableDAO {
    
    public CalcoloPenaDL92DAO (Connection con) {
        super(con);
        setTable("CALCOLO_PENA_DL92");

        // Settare la Sequence e i campi chiave
        setSequenceField("ID_CALCOLO_PENA_DL92", "CALCOLO_PENA_DL92_SEQ");

        setFieldKey("ID_CALCOLO_PENA_DL92", BIG_DECIMAL);
        setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);

        setField("NUM_ANNI_RECLUSIONE", BIG_DECIMAL);
        setField("NUM_MESI_RECLUSIONE", BIG_DECIMAL);
        setField("NUM_GIORNI_RECLUSIONE", BIG_DECIMAL);
        setField("IMPORTO_MULTA", BIG_DECIMAL);
        
        setField("NUM_ANNI_ARRESTO", BIG_DECIMAL);
        setField("NUM_MESI_ARRESTO", BIG_DECIMAL);
        setField("NUM_GIORNI_ARRESTO", BIG_DECIMAL);
        setField("IMPORTO_AMMENDA", BIG_DECIMAL);    
        
        setField("NUM_ANNI_PRESOFFERTO", BIG_DECIMAL);
        setField("NUM_MESI_PRESOFFERTO", BIG_DECIMAL);
        setField("NUM_GIORNI_PRESOFFERTO", BIG_DECIMAL);    
        
        setField("POSIZIONE_GIURIDICA", STRING); 
        
        setField("DATA_INIZIO_PENA", DATE);    

        setField("NUM_ANNI_DA_ESPIARE", BIG_DECIMAL);
        setField("NUM_MESI_DA_ESPIARE", BIG_DECIMAL);
        setField("NUM_GIORNI_DA_ESPIARE", BIG_DECIMAL);  
     
        setField("SEMESTRI_UTILI", BIG_DECIMAL);
        setField("NUM_GG_LA_MAT_IN_PENA_RES", BIG_DECIMAL);
        setField("LA_FUNGIBILI", BIG_DECIMAL);        
        setField("LA_NON_CONCESSE", BIG_DECIMAL);
        
        setField("NUM_ANNI_PENA_IPOTETICA", BIG_DECIMAL);
        setField("NUM_MESI_PENA_IPOTETICA", BIG_DECIMAL);
        setField("NUM_GIORNI_PENA_IPOTETICA", BIG_DECIMAL);
        
        setField("SEMESTRI_UTILI_PENA_SCONTATA", BIG_DECIMAL);

        setField("LA_MATURATE", BIG_DECIMAL);
        setField("LA_APPLICATE", BIG_DECIMAL);

        setField("DATA_SCARC_NO_LA", DATE);
        setField("DATA_SCARC_LA_FUNG", DATE);
        setField("DATA_SCARC_LA_NO_FUNG", DATE);
        setField("DATA_SCARC_PENULTIMO_SEM", DATE);
        
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
    public BigDecimal getIdCalcoloPenaDL92()     throws DAOException  { return getBigDecimal("ID_CALCOLO_PENA_DL92"); } 
    public BigDecimal getFasSieIdFascicoloSiep() throws DAOException  { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); } 
    
    public BigDecimal getNumAnniReclusione()    throws DAOException  { return getBigDecimal("NUM_ANNI_RECLUSIONE"); } 
    public BigDecimal getNumMesiReclusione()    throws DAOException  { return getBigDecimal("NUM_MESI_RECLUSIONE"); } 
    public BigDecimal getNumGiorniReclusione()  throws DAOException  { return getBigDecimal("NUM_GIORNI_RECLUSIONE"); } 
    public BigDecimal getImportoMulta()         throws DAOException  { return getBigDecimal("IMPORTO_MULTA"); } 
    public BigDecimal getNumAnniArresto()       throws DAOException  { return getBigDecimal("NUM_ANNI_ARRESTO"); } 
    public BigDecimal getNumMesiArresto()       throws DAOException  { return getBigDecimal("NUM_MESI_ARRESTO"); } 
    public BigDecimal getNumGiorniArresto()     throws DAOException  { return getBigDecimal("NUM_GIORNI_ARRESTO"); } 
    public BigDecimal getImportoAmmenda()       throws DAOException  { return getBigDecimal("IMPORTO_AMMENDA"); } 
    
    public BigDecimal getNumAnniPresofferto()   throws DAOException  { return getBigDecimal("NUM_ANNI_PRESOFFERTO"); } 
    public BigDecimal getNumMesiPresofferto()   throws DAOException  { return getBigDecimal("NUM_MESI_PRESOFFERTO"); } 
    public BigDecimal getNumGiorniPresofferto() throws DAOException  { return getBigDecimal("NUM_GIORNI_PRESOFFERTO"); } 
    
    public String     getPosizioneGiuridica()   throws DAOException  { return getString("POSIZIONE_GIURIDICA"); } 
    public Date       getDataInizioPena()       throws DAOException  { return getDate  ("DATA_INIZIO_PENA"); } 
    
    public BigDecimal getNumAnniDaEspiare()     throws DAOException  { return getBigDecimal("NUM_ANNI_DA_ESPIARE"); } 
    public BigDecimal getNumMesiDaEspiare()     throws DAOException  { return getBigDecimal("NUM_MESI_DA_ESPIARE"); } 
    public BigDecimal getNumGiorniDaEspiare()   throws DAOException  { return getBigDecimal("NUM_GIORNI_DA_ESPIARE"); } 
    
    public BigDecimal getSemestriUtili()        throws DAOException  { return getBigDecimal("SEMESTRI_UTILI"); }
    public BigDecimal getNumGgLaMatInPenaRes()  throws DAOException  { return getBigDecimal("NUM_GG_LA_MAT_IN_PENA_RES"); }
    public BigDecimal getLaFungibili()          throws DAOException  { return getBigDecimal("LA_FUNGIBILI"); }
    public BigDecimal getLaNonConcesse()        throws DAOException  { return getBigDecimal("LA_NON_CONCESSE"); }
    
    public BigDecimal getNumAnniPenaIpotetica()   throws DAOException  { return getBigDecimal("NUM_ANNI_PENA_IPOTETICA"); }
    public BigDecimal getNumMesiPenaIpotetica()   throws DAOException  { return getBigDecimal("NUM_MESI_PENA_IPOTETICA"); }
    public BigDecimal getNumGiorniPenaIpotetica() throws DAOException  { return getBigDecimal("NUM_GIORNI_PENA_IPOTETICA"); }
    
    public BigDecimal getSemestriUtiliPenaScontata() throws DAOException  { return getBigDecimal("SEMESTRI_UTILI_PENA_SCONTATA"); }
    
    public BigDecimal getLaMaturate()    throws DAOException  { return getBigDecimal("LA_MATURATE"); }
    public BigDecimal getLaApplicate()  throws DAOException  { return getBigDecimal("LA_APPLICATE"); }
    
    public Date getDataScarcNoLa()         throws DAOException  { return getDate  ("DATA_SCARC_NO_LA"); } 
    public Date getDataScarcLaFung()       throws DAOException  { return getDate  ("DATA_SCARC_LA_FUNG"); } 
    public Date getDataScarcLaNoFung()     throws DAOException  { return getDate  ("DATA_SCARC_LA_NO_FUNG"); } 
    public Date getDataScarcPenultimoSem() throws DAOException  { return getDate  ("DATA_SCARC_PENULTIMO_SEM"); } 
    
    public String getCodOperatoreInserimento()    throws DAOException  { return getString("COD_OPERATORE_INSERIMENTO"); }
    public Date   getDataInserimento()            throws DAOException  { return getDate  ("DATA_INSERIMENTO"); }
    public String getCodUfficioInserimento()      throws DAOException  { return getString("COD_UFFICIO_INSERIMENTO"); }
    public String getCodOperatoreAggiornamento()  throws DAOException  { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
    public Date   getDataAggiornamento()          throws DAOException  { return getDate  ("DATA_AGGIORNAMENTO"); }
    public String getCodUfficioAggiornamento()    throws DAOException  { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
    
    
    //
    // METODI SET()
    //   
    public void setIdCalcoloPenaDL92     (BigDecimal aValore) { setBigDecimal("ID_CALCOLO_PENA_DL92", aValore); }
    public void setFasSieIdFascicoloSiep (BigDecimal aValore) { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
    
    public void setNumAnniReclusione   (BigDecimal aValore) { setBigDecimal("NUM_ANNI_RECLUSIONE", aValore); }
    public void setNumMesiReclusione   (BigDecimal aValore) { setBigDecimal("NUM_MESI_RECLUSIONE", aValore); }
    public void setNumGiorniReclusione (BigDecimal aValore) { setBigDecimal("NUM_GIORNI_RECLUSIONE", aValore); }
    public void setImportoMulta        (BigDecimal aValore) { setBigDecimal("IMPORTO_MULTA", aValore); }
    
    public void setNumAnniArresto   (BigDecimal aValore) { setBigDecimal("NUM_ANNI_ARRESTO", aValore); }
    public void setNumMesiArresto   (BigDecimal aValore) { setBigDecimal("NUM_MESI_ARRESTO", aValore); }
    public void setNumGiorniArresto (BigDecimal aValore) { setBigDecimal("NUM_GIORNI_ARRESTO", aValore); }
    public void setImportoAmmenda   (BigDecimal aValore) { setBigDecimal("IMPORTO_AMMENDA", aValore); }
    
    public void setNumAnniPresofferto   (BigDecimal aValore) { setBigDecimal("NUM_ANNI_PRESOFFERTO", aValore); }
    public void setNumMesiPresofferto   (BigDecimal aValore) { setBigDecimal("NUM_MESI_PRESOFFERTO", aValore); }
    public void setNumGiorniPresofferto (BigDecimal aValore) { setBigDecimal("NUM_GIORNI_PRESOFFERTO", aValore); }
    
    public void setPosizioneGiuridica (String aValore) { setString("POSIZIONE_GIURIDICA", aValore); }
    public void setDataInizioPena     (Date aValore)   { setDate  ("DATA_INIZIO_PENA", aValore); }
    
    public void setNumAnniDaEspiare   (BigDecimal aValore) { setBigDecimal("NUM_ANNI_DA_ESPIARE", aValore); }
    public void setNumMesiDaEspiare   (BigDecimal aValore) { setBigDecimal("NUM_MESI_DA_ESPIARE", aValore); }
    public void setNumGiorniDaEspiare (BigDecimal aValore) { setBigDecimal("NUM_GIORNI_DA_ESPIARE", aValore); }
    
    public void setSemestriUtili        (BigDecimal aValore) { setBigDecimal("SEMESTRI_UTILI", aValore); }
    public void setNumGgLaMatInPenaRes  (BigDecimal aValore) { setBigDecimal("NUM_GG_LA_MAT_IN_PENA_RES", aValore); }
    public void setLaFungibili          (BigDecimal aValore) { setBigDecimal("LA_FUNGIBILI", aValore); }
    public void setLaNonConcesse        (BigDecimal aValore) { setBigDecimal("LA_NON_CONCESSE", aValore); }
    
    public void setNumAnniPenaIpotetica   (BigDecimal aValore) { setBigDecimal("NUM_ANNI_PENA_IPOTETICA", aValore); }
    public void setNumMesiPenaIpotetica   (BigDecimal aValore) { setBigDecimal("NUM_MESI_PENA_IPOTETICA", aValore); }
    public void setNumGiorniPenaIpotetica (BigDecimal aValore) { setBigDecimal("NUM_GIORNI_PENA_IPOTETICA", aValore); }

    public void setSemestriUtiliPenaScontata (BigDecimal aValore) { setBigDecimal("SEMESTRI_UTILI_PENA_SCONTATA", aValore); }
    public void setLaMaturate                (BigDecimal aValore) { setBigDecimal("LA_MATURATE", aValore); }
    public void setLaApplicate               (BigDecimal aValore) { setBigDecimal("LA_APPLICATE", aValore); }
    
    public void setDataScarcNoLa         (Date aValore) { setDate  ("DATA_SCARC_NO_LA", aValore); }
    public void setDataScarcLaFung       (Date aValore) { setDate  ("DATA_SCARC_LA_FUNG", aValore); }
    public void setDataScarcLaNoFung     (Date aValore) { setDate  ("DATA_SCARC_LA_NO_FUNG", aValore); }
    public void setDataScarcPenultimoSem (Date aValore) { setDate  ("DATA_SCARC_PENULTIMO_SEM", aValore); }

    public void setCodOperatoreInserimento  (String aValore ) { setString ("COD_OPERATORE_INSERIMENTO", aValore); }
    public void setDataInserimento          (Date   aValore ) { setDate   ("DATA_INSERIMENTO", aValore); }
    public void setCodUfficioInserimento    (String aValore ) { setString ("COD_UFFICIO_INSERIMENTO", aValore); }
    public void setCodOperatoreAggiornamento(String aValore ) { setString ("COD_OPERATORE_AGGIORNAMENTO", aValore); }
    public void setDataAggiornamento        (Date   aValore ) { setDate   ("DATA_AGGIORNAMENTO", aValore); }
    public void setCodUfficioAggiornamento  (String aValore ) { setString ("COD_UFFICIO_AGGIORNAMENTO", aValore); }  
    
    
    public void setDAOFromModel(CalcoloPenaDL92ModelDB aModel) throws DAOException
    {
      setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
      
      setNumAnniReclusione( aModel.getNumAnniReclusione() );
      setNumMesiReclusione( aModel.getNumMesiReclusione() );
      setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
      setImportoMulta( aModel.getImportoMulta() );
      
      setNumAnniArresto( aModel.getNumAnniArresto() );
      setNumMesiArresto( aModel.getNumMesiArresto() );
      setNumGiorniArresto( aModel.getNumGiorniArresto() );
      setImportoAmmenda( aModel.getImportoAmmenda() );
    
      setNumAnniPresofferto   ( aModel.getNumAnniPresofferto() );
      setNumMesiPresofferto   ( aModel.getNumMesiPresofferto() );
      setNumGiorniPresofferto ( aModel.getNumGiorniPresofferto() );
      
      setPosizioneGiuridica ( aModel.getPosizioneGiuridica() );
      setDataInizioPena     ( aModel.getDataInizioPena() );
      
      setNumAnniDaEspiare   ( aModel.getNumAnniDaEspiare() );
      setNumMesiDaEspiare   ( aModel.getNumMesiDaEspiare() );
      setNumGiorniDaEspiare ( aModel.getNumGiorniDaEspiare() );
        
      setSemestriUtili        ( aModel.getSemestriUtili() );
      setNumGgLaMatInPenaRes  ( aModel.getNumGgLaMatInPenaRes() );
      setLaFungibili          ( aModel.getLaFungibili() );
      setLaNonConcesse        ( aModel.getLaNonConcesse() ); 

      setNumAnniPenaIpotetica   ( aModel.getNumAnniPenaIpotetica() );
      setNumMesiPenaIpotetica   ( aModel.getNumMesiPenaIpotetica() );
      setNumGiorniPenaIpotetica ( aModel.getNumGiorniPenaIpotetica() ); 
      
      setSemestriUtiliPenaScontata ( aModel.getSemestriUtiliPenaScontata() );
      setLaMaturate                ( aModel.getLaMaturate() );
      setLaApplicate               ( aModel.getLaApplicate() );
      
      setDataScarcNoLa         ( aModel.getDataScarcNoLa() );
      setDataScarcLaFung       ( aModel.getDataScarcLaFung() );
      setDataScarcLaNoFung     ( aModel.getDataScarcLaNoFung() );
      setDataScarcPenultimoSem ( aModel.getDataScarcPenultimoSem() );
        
      setCodOperatoreInserimento ( aModel.getCodOperatoreInserimento() );
      setDataInserimento         ( aModel.getDataInserimento() );
      setCodUfficioInserimento   ( aModel.getCodUfficioInserimento() );
      
      setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento         ( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento   ( aModel.getCodUfficioAggiornamento() );
    }    

    public void setDAOFromModelForUpdate (CalcoloPenaDL92ModelDB aModel) throws DAOException
    {
      //setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
      
      setNumAnniReclusione( aModel.getNumAnniReclusione() );
      setNumMesiReclusione( aModel.getNumMesiReclusione() );
      setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
      setImportoMulta( aModel.getImportoMulta() );
      
      setNumAnniArresto( aModel.getNumAnniArresto() );
      setNumMesiArresto( aModel.getNumMesiArresto() );
      setNumGiorniArresto( aModel.getNumGiorniArresto() );
      setImportoAmmenda( aModel.getImportoAmmenda() );
    
      setNumAnniPresofferto   ( aModel.getNumAnniPresofferto() );
      setNumMesiPresofferto   ( aModel.getNumMesiPresofferto() );
      setNumGiorniPresofferto ( aModel.getNumGiorniPresofferto() );
      
      setPosizioneGiuridica ( aModel.getPosizioneGiuridica() );
      setDataInizioPena     ( aModel.getDataInizioPena() );
      
      setNumAnniDaEspiare   ( aModel.getNumAnniDaEspiare() );
      setNumMesiDaEspiare   ( aModel.getNumMesiDaEspiare() );
      setNumGiorniDaEspiare ( aModel.getNumGiorniDaEspiare() );
        
      setSemestriUtili        ( aModel.getSemestriUtili() );
      setNumGgLaMatInPenaRes  ( aModel.getNumGgLaMatInPenaRes() );
      setLaFungibili          ( aModel.getLaFungibili() );
      setLaNonConcesse        ( aModel.getLaNonConcesse() ); 

      setNumAnniPenaIpotetica   ( aModel.getNumAnniPenaIpotetica() );
      setNumMesiPenaIpotetica   ( aModel.getNumMesiPenaIpotetica() );
      setNumGiorniPenaIpotetica ( aModel.getNumGiorniPenaIpotetica() ); 
      
      setSemestriUtiliPenaScontata ( aModel.getSemestriUtiliPenaScontata() );
      setLaMaturate                ( aModel.getLaMaturate() );
      setLaApplicate               ( aModel.getLaApplicate() );
      
      setDataScarcNoLa         ( aModel.getDataScarcNoLa() );
      setDataScarcLaFung       ( aModel.getDataScarcLaFung() );
      setDataScarcLaNoFung     ( aModel.getDataScarcLaNoFung() );
      setDataScarcPenultimoSem ( aModel.getDataScarcPenultimoSem() );
        
//      setCodOperatoreInserimento ( aModel.getCodOperatoreInserimento() );
//      setDataInserimento         ( aModel.getDataInserimento() );
//      setCodUfficioInserimento   ( aModel.getCodUfficioInserimento() );
      
      setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento         ( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento   ( aModel.getCodUfficioAggiornamento() );
      
      // Update per chiave record
      setCondizioneUpdate (aModel.getIdCalcoloPenaDL92());
    }
    
    public void setCondizioneUpdate(BigDecimal key)
    {
      setCondition(" ID_CALCOLO_PENA_DL92 = " + key );
    }    
}
