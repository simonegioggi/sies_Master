package siap.siep.misurasicurezza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

public class FascMsToFascSiepDAO extends SIAPTableDAO
{
  public FascMsToFascSiepDAO (Connection con)
  {
       super(con);
       
       // Table name
       setTable("FASC_MS_TO_FASC_SIEP");
       
       //Sequence
       setSequenceField("ID_FASC_MS_TO_FASC_SIEP", "FASC_MS_TO_FASC_SIEP_SEQ");

       // Primary Key
       setFieldKey("ID_FASC_MS_TO_FASC_SIEP", BIG_DECIMAL);
      
       // Fields
       setField("ID_FASC_MS_TO_FASC_SIEP", BIG_DECIMAL);
       setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
       setField("CHIAVE_ANNO_SIEP", BIG_DECIMAL);
       setField("CHIAVE_PROGR_SIEP", BIG_DECIMAL);
       setField("CHIAVE_UFFICIO_SIEP", STRING);
       
       setField("COD_TIPO_RELAZIONE_MS", STRING);
       
       setField("DATA_CUMULO", DATE);
       
       setField("FAS_SIE_ID_FASCICOLO_COLLEGATO", BIG_DECIMAL);
       setField("CHIAVE_ANNO_SIEP_COLLEGATO", BIG_DECIMAL);
       setField("CHIAVE_PROGR_SIEP_COLLEGATO", BIG_DECIMAL);       
       setField("CHIAVE_UFFICIO_SIEP_COLLEGATO", STRING); 
       
       setField("MES_ID_MESSAGGIO", BIG_DECIMAL); 
       setField("EVE_ID_EVENTO", BIG_DECIMAL); 
       
       setField("COD_OPERATORE_INSERIMENTO", STRING);
       setField("DATA_INSERIMENTO", DATE);
       setField("COD_UFFICIO_INSERIMENTO", STRING);
       
       setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
       setField("DATA_AGGIORNAMENTO", DATE);
       setField("COD_UFFICIO_AGGIORNAMENTO", STRING);        
       
  }
  
  //================
  // METODI GET()
  //================
  public BigDecimal getIdFascMsToFascSiep()        throws DAOException { return getBigDecimal ("ID_FASC_MS_TO_FASC_SIEP"); }
  public BigDecimal getFasSieIdFascicoloSiep()     throws DAOException { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public BigDecimal getChiaveAnnoSiep()            throws DAOException { return getBigDecimal ("CHIAVE_ANNO_SIEP"); } 
  public BigDecimal getChiaveProgrSiep()           throws DAOException { return getBigDecimal ("CHIAVE_PROGR_SIEP"); } 
  public String     getChiaveUfficioSiep()         throws DAOException { return getString     ("CHIAVE_UFFICIO_SIEP"); } 

  public String     getCodTipoRelazioneMS()        throws DAOException { return getString     ("COD_TIPO_RELAZIONE_MS"); } 
  
  public Date       getDataCumulo()		             throws DAOException { return getDate   ("DATA_CUMULO"); } 
  
  public BigDecimal getFasSieIdFascicoloCollegato() throws DAOException { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_COLLEGATO"); } 
  public BigDecimal getChiaveAnnoSiepCollegato()    throws DAOException { return getBigDecimal ("CHIAVE_ANNO_SIEP_COLLEGATO"); } 
  public BigDecimal getChiaveProgrSiepCollegato()   throws DAOException { return getBigDecimal ("CHIAVE_PROGR_SIEP_COLLEGATO"); } 
  public String     getChiaveUfficioSiepCollegato() throws DAOException { return getString     ("CHIAVE_UFFICIO_SIEP_COLLEGATO"); } 
  
  public BigDecimal getMesIdMessaggio()            throws DAOException { return getBigDecimal ("MES_ID_MESSAGGIO"); } 
  public BigDecimal getEveIdEvento()               throws DAOException { return getBigDecimal ("EVE_ID_EVENTO"); } 
  
  public String     getCodOperatoreInserimento()   throws DAOException { return getString ("COD_OPERATORE_INSERIMENTO"); }  
  public Date       getDataInserimento()           throws DAOException { return getDate   ("DATA_INSERIMENTO"); } 
  public String     getCodUfficioInserimento()     throws DAOException { return getString ("COD_UFFICIO_INSERIMENTO"); } 
  public String     getCodOperatoreAggiornamento() throws DAOException { return getString ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public Date       getDataAggiornamento()         throws DAOException { return getDate   ("DATA_AGGIORNAMENTO"); } 
  public String     getCodUfficioAggiornamento()   throws DAOException { return getString ("COD_UFFICIO_AGGIORNAMENTO"); } 

  //================
  // METODI SET()
  //================
  public void setIdFascMsToFascSiep        (BigDecimal aValore) { setBigDecimal ("ID_FASC_MS_TO_FASC_SIEP", aValore); }
  public void setFasSieIdFascicoloSiep     (BigDecimal aValore) { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void setChiaveAnnoSiep            (BigDecimal aValore) { setBigDecimal ("CHIAVE_ANNO_SIEP", aValore); }
  public void setChiaveProgrSiep           (BigDecimal aValore) { setBigDecimal ("CHIAVE_PROGR_SIEP", aValore); }
  public void setChiaveUfficioSiep         (String     aValore) { setString     ("CHIAVE_UFFICIO_SIEP", aValore); }

  public void setCodTipoRelazioneMS        (String     aValore) { setString     ("COD_TIPO_RELAZIONE_MS", aValore); }
  
  public void setDataCumulo                (Date       aValore ) { setDate   ("DATA_CUMULO", aValore); }
  
  public void setFasSieIdFascicoloCollegato (BigDecimal aValore) { setBigDecimal ("FAS_SIE_ID_FASCICOLO_COLLEGATO", aValore); }
  public void setChiaveAnnoSiepCollegato    (BigDecimal aValore) { setBigDecimal ("CHIAVE_ANNO_SIEP_COLLEGATO", aValore); }
  public void setChiaveProgrSiepCollegato   (BigDecimal aValore) { setBigDecimal ("CHIAVE_PROGR_SIEP_COLLEGATO", aValore); }
  public void setChiaveUfficioSiepCollegato (String     aValore) { setString     ("CHIAVE_UFFICIO_SIEP_COLLEGATO", aValore); }
  
  public void setMesIdMessaggio            (BigDecimal aValore) { setBigDecimal ("MES_ID_MESSAGGIO", aValore); }  
  public void setEveIdEvento               (BigDecimal aValore) { setBigDecimal ("EVE_ID_EVENTO", aValore); }  
                                                                    
  public void setCodOperatoreInserimento   (String aValore ) { setString ("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento           (Date   aValore ) { setDate   ("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento     (String aValore ) { setString ("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento (String aValore ) { setString ("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento         (Date   aValore ) { setDate   ("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento   (String aValore ) { setString ("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  
  /**
   * 
   */
  public GenericModel getModel() throws DAOException
  {
    return new FascMsToFascSiepModel(getIdFascMsToFascSiep() ,
                                     getFasSieIdFascicoloSiep() ,
                                     getChiaveAnnoSiep(),
                                     getChiaveProgrSiep() ,
                                     getChiaveUfficioSiep() ,
                                     "","",
                                     getCodTipoRelazioneMS(),
                                     getDataCumulo() ,
                                     
                                     getFasSieIdFascicoloCollegato() ,
                                     getChiaveAnnoSiepCollegato() ,
                                     getChiaveProgrSiepCollegato() ,
                                     getChiaveUfficioSiepCollegato() ,
                                     "","",
                                     getMesIdMessaggio(),
                                     getEveIdEvento(),
                                     getCodOperatoreInserimento() ,
                                     getDataInserimento() ,
                                     getCodUfficioInserimento() ,
                                     "",
                                     getCodOperatoreAggiornamento() ,
                                     getDataAggiornamento() ,
                                     getCodUfficioAggiornamento() ,
                                     ""
                                     );
  }
  
  /**
   * 
   * @param aModel
   * @throws DAOException
   */
  public void   setDAOFromModel(FascMsToFascSiepModel aModel) throws DAOException
  {
    setIdFascMsToFascSiep        (aModel.getIdFascMsToFascSiep()     );
    setFasSieIdFascicoloSiep     (aModel.getFasSieIdFascicoloSiep()  ); 
    setChiaveAnnoSiep            (aModel.getChiaveAnnoSiep()         ); 
    setChiaveProgrSiep           (aModel.getChiaveProgrSiep()        ); 
    setChiaveUfficioSiep         (aModel.getChiaveUfficioSiep()      );

    setCodTipoRelazioneMS        (aModel.getCodTipoRelazioneMS()     );
    
    setDataCumulo				 (aModel.getDataCumulo()			);	
    
    setFasSieIdFascicoloCollegato (aModel.getFasSieIdFascicoloCollegato()  ); 
    setChiaveAnnoSiepCollegato    (aModel.getChiaveAnnoSiepCollegato()         ); 
    setChiaveProgrSiepCollegato   (aModel.getChiaveProgrSiepCollegato()        ); 
    setChiaveUfficioSiepCollegato (aModel.getChiaveUfficioSiepCollegato()      ); 
    
    setMesIdMessaggio            (aModel.getMesIdMessaggio()        );  
    setEveIdEvento               (aModel.getEveIdEvento()           );
    
    setCodOperatoreInserimento   (aModel.getCodOperatoreInserimento()   );
    setDataInserimento           (aModel.getDataInserimento()           );
    setCodUfficioInserimento     (aModel.getCodUfficioInserimento()     ); 
    setCodOperatoreAggiornamento (aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento         (aModel.getDataAggiornamento()         );
    setCodUfficioAggiornamento   (aModel.getCodUfficioAggiornamento()   );
  }
  
  /**
   * 
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForUpdate(FascMsToFascSiepModel aModel) throws DAOException
  {
    //setIdFascMsToFascSiep        (aModel.getIdFascMsToFascSiep()     );
    setFasSieIdFascicoloSiep     (aModel.getFasSieIdFascicoloSiep()  ); 
    setChiaveAnnoSiep            (aModel.getChiaveAnnoSiep()         ); 
    setChiaveProgrSiep           (aModel.getChiaveProgrSiep()        ); 
    setChiaveUfficioSiep         (aModel.getChiaveUfficioSiep()      );

    setCodTipoRelazioneMS        (aModel.getCodTipoRelazioneMS()     );
    
    setDataCumulo                (aModel.getDataCumulo()			);	
    
    setFasSieIdFascicoloCollegato (aModel.getFasSieIdFascicoloCollegato()  ); 
    setChiaveAnnoSiepCollegato    (aModel.getChiaveAnnoSiepCollegato()     ); 
    setChiaveProgrSiepCollegato   (aModel.getChiaveProgrSiepCollegato()    ); 
    setChiaveUfficioSiepCollegato (aModel.getChiaveUfficioSiepCollegato()  ); 
    
    setMesIdMessaggio            (aModel.getMesIdMessaggio()        );
    setEveIdEvento               (aModel.getEveIdEvento()           );
    
    setCodOperatoreInserimento   (aModel.getCodOperatoreInserimento()   );
    setDataInserimento           (aModel.getDataInserimento()           );
    setCodUfficioInserimento     (aModel.getCodUfficioInserimento()     ); 
    setCodOperatoreAggiornamento (aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento         (aModel.getDataAggiornamento()         );
    setCodUfficioAggiornamento   (aModel.getCodUfficioAggiornamento()   );
  }
  
  
  public void setCondizione(FascMsToFascSiepModel aModel)
  {
    String lCondizioni = new String();
  
    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }
  
  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_FASC_MS_TO_FASC_SIEP = " + key );
  }
}
