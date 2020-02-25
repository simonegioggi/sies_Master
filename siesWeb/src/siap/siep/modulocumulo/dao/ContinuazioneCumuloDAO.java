package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: ContinuazioneCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Continuazione</p>
*  <p>		in ambito Cumulo (Continuazione_Cumulo) </p>
* @version 1.0
*/

public class ContinuazioneCumuloDAO extends TableDAO
{
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public ContinuazioneCumuloDAO (Connection con) {
    super(con);
    setTable("CONTINUAZIONE_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_CONTINUAZIONE_CUM","CON_CUM_SEQ");

    //setField("ID_CONTINUAZIONE_CUM", BIG_DECIMAL);
    setField("PROGR_CONTINUAZIONE"        , BIG_DECIMAL);
    setField("COD_TIPO_CONTINUAZIONE"     , STRING);
    setField("COD_TIPO_AUTORITA"          , STRING);
    setField("COD_LUOGO_AUTORITA"         , STRING);
    setField("DATA_SENTENZA"              , DATE);
    setField("ANNO_SENTENZA"              , BIG_DECIMAL);
    setField("NUM_SENTENZA"               , STRING);
    
    setField("ANNO_REGE_PM"               , BIG_DECIMAL);
    setField("NUM_REGE_PM"                , STRING);
    
    setField("ANNO_REG_GEN"               , BIG_DECIMAL);
    setField("NUMERO_REG_GEN"             , STRING);
    setField("TIPO_REG_GEN"               , STRING);
    
    setField("PC_ID_PENA_COMPLESSIVA_CUM" , BIG_DECIMAL);
    setField("TIT_ID_TITOLO_CUMULATO_CONT" , BIG_DECIMAL);

    setField("FLAG_STATO"                 , STRING);
    setField("MOTIVO_MODIFICA"            , STRING);
    setField("ID_CONTINUAZIONE_ORIGINE"   , BIG_DECIMAL);
    setField("TIT_ID_TITOLO_CUMULATO"     , BIG_DECIMAL);

    setField("COD_OPERATORE_INSERIMENTO"  , STRING);
    setField("DATA_INSERIMENTO"           , DATE);
    setField("COD_UFFICIO_INSERIMENTO"    , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO"         , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"  , STRING);
  }

  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdContinuazioneCum()         throws DAOException  { return getBigDecimal ("ID_CONTINUAZIONE_CUM"       ); } 
  public  BigDecimal  getProgrContinuazione()         throws DAOException  { return getBigDecimal ("PROGR_CONTINUAZIONE"        ); } 
  public  String      getCodTipoContinuazione()       throws DAOException  { return getString     ("COD_TIPO_CONTINUAZIONE"     ); } 
  public  String      getCodTipoAutorita()            throws DAOException  { return getString     ("COD_TIPO_AUTORITA"          ); } 
  public  String      getCodLuogoAutorita()           throws DAOException  { return getString     ("COD_LUOGO_AUTORITA"         ); } 
  public  Date        getDataSentenza()               throws DAOException  { return getDate       ("DATA_SENTENZA"              ); } 
  public  BigDecimal  getAnnoSentenza()               throws DAOException  { return getBigDecimal ("ANNO_SENTENZA"              ); } 
  public  String      getNumSentenza()                throws DAOException  { return getString     ("NUM_SENTENZA"               ); } 
  public  BigDecimal  getAnnoRegePm()                 throws DAOException  { return getBigDecimal ("ANNO_REGE_PM"               ); } 
  public  String      getNumRegePm()                  throws DAOException  { return getString     ("NUM_REGE_PM"                ); } 
  
  public  BigDecimal  getAnnoRegGen()                 throws DAOException  { return getBigDecimal ("ANNO_REG_GEN"               ); } 
  public  String      getNumeroRegGen()               throws DAOException  { return getString     ("NUMERO_REG_GEN"             ); } 
  public  String      getTipoRegGen()                 throws DAOException  { return getString     ("TIPO_REG_GEN"               ); } 

  public  BigDecimal  getPcIdPenaComplessivaCum()     throws DAOException  { return getBigDecimal ("PC_ID_PENA_COMPLESSIVA_CUM" ); } 
  public  BigDecimal  getTitIdTitoloCumulatoCont()    throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO_CONT" ); } 
  
  public  String      getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                 ); } 
  public  String      getMotivoModifica()             throws DAOException  { return getString     ("MOTIVO_MODIFICA"            ); } 
  public  BigDecimal  getIdContinuazioneOrigine()     throws DAOException  { return getBigDecimal ("ID_CONTINUAZIONE_ORIGINE"   ); } 
  public  BigDecimal  getTitIdTitoloCumulato()        throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"     ); } 

  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdContinuazioneCum         (BigDecimal  aValore )   { setBigDecimal ("ID_CONTINUAZIONE_CUM"       , aValore); } 
  public void  setProgrContinuazione         (BigDecimal  aValore )   { setBigDecimal ("PROGR_CONTINUAZIONE"        , aValore); } 
  public void  setCodTipoContinuazione       (String      aValore )   { setString     ("COD_TIPO_CONTINUAZIONE"     , aValore); } 
  public void  setCodTipoAutorita            (String      aValore )   { setString     ("COD_TIPO_AUTORITA"          , aValore); } 
  public void  setCodLuogoAutorita           (String      aValore )   { setString     ("COD_LUOGO_AUTORITA"         , aValore); } 
  public void  setDataSentenza               (Date        aValore )   { setDate       ("DATA_SENTENZA"              , aValore); } 
  public void  setAnnoSentenza               (BigDecimal  aValore )   { setBigDecimal ("ANNO_SENTENZA"              , aValore); } 
  public void  setNumSentenza                (String      aValore )   { setString     ("NUM_SENTENZA"               , aValore); } 
  public void  setAnnoRegePm                 (BigDecimal  aValore )   { setBigDecimal ("ANNO_REGE_PM"               , aValore); } 
  public void  setNumRegePm                  (String      aValore )   { setString     ("NUM_REGE_PM"                , aValore); } 
  
  public void  setAnnoRegGen                 (BigDecimal  aValore )   { setBigDecimal ("ANNO_REG_GEN"               , aValore); } 
  public void  setNumeroRegGen               (String      aValore )   { setString     ("NUMERO_REG_GEN"             , aValore); } 
  public void  setTipoRegGen                 (String      aValore )   { setString     ("TIPO_REG_GEN"               , aValore); } 
  
  public void  setPcIdPenaComplessivaCum     (BigDecimal  aValore )   { setBigDecimal ("PC_ID_PENA_COMPLESSIVA_CUM"  , aValore); } 
  public void  setTitIdTitoloCumulatoCont    (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO_CONT" , aValore); } 
  
  public void  setFlagStato                  (String      aValore )   { setString     ("FLAG_STATO"                 , aValore); } 
  public void  setMotivoModifica             (String      aValore )   { setString     ("MOTIVO_MODIFICA"            , aValore); } 
  public void  setTitIdTitoloCumulato        (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"     , aValore); } 
  public void  setIdContinuazioneOrigine     (BigDecimal  aValore )   { setBigDecimal ("ID_CONTINUAZIONE_ORIGINE"   , aValore); } 

  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"  , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"           , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"    , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"         , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"  , aValore); } 
  
  
	public GenericModel getModel() throws DAOException
  {
    return new ContinuazioneCumuloModel(getIdContinuazioneCum() ,
                                   getProgrContinuazione() ,
                                   getCodTipoContinuazione() ,
                                   "",
                                   getCodTipoAutorita() ,
                                   "",
                                   getCodLuogoAutorita() ,
                                   "",
                                   getDataSentenza() ,
                                   getAnnoSentenza() ,
                                   getNumSentenza() ,
                                   getAnnoRegePm() ,
                                   getNumRegePm() ,
                                   
                                   getAnnoRegGen() , 
                                   getNumeroRegGen() , 
                                   getTipoRegGen() , 
                                   
                                   getPcIdPenaComplessivaCum(),
                                   getTitIdTitoloCumulatoCont(),
                                   
                                   getFlagStato(),
                                   getMotivoModifica(),
                                   getTitIdTitoloCumulato(),
                                   getIdContinuazioneOrigine(),
                                   
                                   getCodOperatoreInserimento() ,
                                   getDataInserimento() ,
                                   getCodUfficioInserimento() ,
                                   getCodOperatoreAggiornamento() ,
                                   getDataAggiornamento() ,
                                   getCodUfficioAggiornamento()
                                );
  }

  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(ContinuazioneCumuloModel aModel) throws DAOException {
    setIdContinuazioneCum         ( aModel.getIdContinuazioneCum()        );  
    setProgrContinuazione         ( aModel.getProgrContinuazione()        );  
    setCodTipoContinuazione       ( aModel.getCodTipoContinuazione()      );  
    setCodTipoAutorita            ( aModel.getCodTipoAutorita()           );  
    setCodLuogoAutorita           ( aModel.getCodLuogoAutorita()          );  
    setDataSentenza               ( aModel.getDataSentenza()              );  
    setAnnoSentenza               ( aModel.getAnnoSentenza()              );  
    setNumSentenza                ( aModel.getNumSentenza()               );  
    
    setAnnoRegePm                 ( aModel.getAnnoRegePm()                );  
    setNumRegePm                  ( aModel.getNumRegePm()                 );  
    
    setAnnoRegGen                 ( aModel.getAnnoRegGen()                );  
    setNumeroRegGen               ( aModel.getNumeroRegGen()              );  
    setTipoRegGen                 ( aModel.getTipoRegGen()                );   
    
    setPcIdPenaComplessivaCum     ( aModel.getPcIdPenaComplessivaCum()    );  
    setTitIdTitoloCumulatoCont    ( aModel.getTitIdTitoloCumulatoCont()   );    
    
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setIdContinuazioneOrigine     ( aModel.getIdContinuazioneOrigine()    );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );
 
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  public void setDAOFromModelForUpdate(ContinuazioneCumuloModel aModel) throws DAOException
  {
    //setProgrContinuazione         ( aModel.getProgrContinuazione()        );  // non mdificabile
    setCodTipoContinuazione       ( aModel.getCodTipoContinuazione()      );  
    setCodTipoAutorita            ( aModel.getCodTipoAutorita()           );  
    setCodLuogoAutorita           ( aModel.getCodLuogoAutorita()          );  
    
    setDataSentenza               ( aModel.getDataSentenza()              );  
    setAnnoSentenza               ( aModel.getAnnoSentenza()              );  
    setNumSentenza                ( aModel.getNumSentenza()               );  
    
    setAnnoRegePm                 ( aModel.getAnnoRegePm()                );  
    setNumRegePm                  ( aModel.getNumRegePm()                 );
    
    setAnnoRegGen                 ( aModel.getAnnoRegGen()              );  
    setNumeroRegGen               ( aModel.getNumeroRegGen()            );  
    setTipoRegGen                 ( aModel.getTipoRegGen()              );   

    //setPcIdPenaComplessivaCum     ( aModel.getPcIdPenaComplessivaCum()    );  // non mdificabile
    setTitIdTitoloCumulatoCont    ( aModel.getTitIdTitoloCumulatoCont()   );    
    
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    //setIdContinuazioneOrigine     ( aModel.getIdContinuazioneOrigine()    );  // non mdificabile
    //setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  // non mdificabile
    
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  

    //
    setCondizioneUpdate(aModel.getIdContinuazioneCum());
  }

	public void setCondizione(ContinuazioneCumuloModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_CONTINUAZIONE_CUM = " + key );
  }

  public void setCondizioneByIdPenaComplessiva(BigDecimal aIdPenaComplessiva)
  {
    setCondition(" PC_ID_PENA_COMPLESSIVA_CUM = " + aIdPenaComplessiva);
  }
}
