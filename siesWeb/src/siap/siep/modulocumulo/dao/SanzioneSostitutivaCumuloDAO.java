package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: SanzioneSostitutivaCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella SanzioneSostitutiva</p>
* <p>		in ambito Cumulo (SanzioneSostitutiva_Cumulo) </p>
* @version 1.0
*/

public class SanzioneSostitutivaCumuloDAO extends SIAPTableDAO
{
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public SanzioneSostitutivaCumuloDAO (Connection con) {
    super(con);
    setTable("SANZIONE_SOST_CUM");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_SANZIONE_SOST_CUM","SAN_SOS_CUM_SEQ");

    //setField("ID_SANZIONE_SOST_CUM", BIG_DECIMAL);
    setField("COD_TIPO_SANZIONE"          , STRING);
    setField("NUM_ANNI"                   , BIG_DECIMAL);
    setField("NUM_MESI"                   , BIG_DECIMAL);
    setField("NUM_GIORNI"                 , BIG_DECIMAL);
    setField("SANZIONE_PECUNIARIA_MULTA"  , BIG_DECIMAL);
    setField("SANZIONE_PECUNIARIA_AMMENDA", BIG_DECIMAL);
    setField("PC_ID_PENA_COMPLESSIVA_CUM" , BIG_DECIMAL);
    setField("FLAG_STATO"                 , STRING);
    setField("MOTIVO_MODIFICA"            , STRING);
    setField("TIT_ID_TITOLO_CUMULATO"     , BIG_DECIMAL);
    setField("ID_SANZIONE_SOST_ORIGINE"   , BIG_DECIMAL);
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
  public  BigDecimal  getIdSanzioneSostCum()          throws DAOException  { return getBigDecimal ("ID_SANZIONE_SOST_CUM"       ); } 
  public  String      getCodTipoSanzione()            throws DAOException  { return getString     ("COD_TIPO_SANZIONE"          ); } 
  public  BigDecimal  getNumAnni()                    throws DAOException  { return getBigDecimal ("NUM_ANNI"                   ); } 
  public  BigDecimal  getNumMesi()                    throws DAOException  { return getBigDecimal ("NUM_MESI"                   ); } 
  public  BigDecimal  getNumGiorni()                  throws DAOException  { return getBigDecimal ("NUM_GIORNI"                 ); } 
  public  BigDecimal  getSanzionePecuniariaMulta()    throws DAOException  { return getBigDecimal ("SANZIONE_PECUNIARIA_MULTA"  ); } 
  public  BigDecimal  getSanzionePecuniariaAmmenda()  throws DAOException  { return getBigDecimal ("SANZIONE_PECUNIARIA_AMMENDA"); } 
  public  BigDecimal  getPcIdPenaComplessivaCum()     throws DAOException  { return getBigDecimal ("PC_ID_PENA_COMPLESSIVA_CUM" ); } 
  public  String      getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                 ); } 
  public  String      getMotivoModifica()             throws DAOException  { return getString     ("MOTIVO_MODIFICA"            ); } 
  public  BigDecimal  getTitIdTitoloCumulato()        throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"     ); } 
  public  BigDecimal  getIdSanzioneSostOrigine()      throws DAOException  { return getBigDecimal ("ID_SANZIONE_SOST_ORIGINE"   ); } 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdSanzioneSostCum          (BigDecimal  aValore )   { setBigDecimal ("ID_SANZIONE_SOST_CUM"       , aValore); } 
  public void  setCodTipoSanzione            (String      aValore )   { setString     ("COD_TIPO_SANZIONE"          , aValore); } 
  public void  setNumAnni                    (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI"                   , aValore); } 
  public void  setNumMesi                    (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI"                   , aValore); } 
  public void  setNumGiorni                  (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI"                 , aValore); } 
  public void  setSanzionePecuniariaMulta    (BigDecimal  aValore )   { setBigDecimal ("SANZIONE_PECUNIARIA_MULTA"  , aValore); } 
  public void  setSanzionePecuniariaAmmenda  (BigDecimal  aValore )   { setBigDecimal ("SANZIONE_PECUNIARIA_AMMENDA", aValore); } 
  public void  setPcIdPenaComplessivaCum     (BigDecimal  aValore )   { setBigDecimal ("PC_ID_PENA_COMPLESSIVA_CUM" , aValore); } 
  public void  setFlagStato                  (String      aValore )   { setString     ("FLAG_STATO"                 , aValore); } 
  public void  setMotivoModifica             (String      aValore )   { setString     ("MOTIVO_MODIFICA"            , aValore); } 
  public void  setTitIdTitoloCumulato        (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"     , aValore); } 
  public void  setIdSanzioneSostOrigine      (BigDecimal  aValore )   { setBigDecimal ("ID_SANZIONE_SOST_ORIGINE"   , aValore); } 
  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"  , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"           , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"    , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"         , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"  , aValore); } 

  
  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new SanzioneSostitutivaCumuloModel(  
      getIdSanzioneSostCum() , 
      getCodTipoSanzione() ,  "",
      getNumAnni() , 
      getNumMesi() , 
      getNumGiorni() , 
      getSanzionePecuniariaMulta() , 
      getSanzionePecuniariaAmmenda() , 
      getPcIdPenaComplessivaCum() , 
      getFlagStato() , 
      getMotivoModifica() , 
      getTitIdTitoloCumulato() , 
      getIdSanzioneSostOrigine() , 
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
  public void setDAOFromModel(SanzioneSostitutivaCumuloModel aModel) throws DAOException {
    setIdSanzioneSostCum          ( aModel.getIdSanzioneSostitutivaCum()  );  
    setCodTipoSanzione            ( aModel.getCodTipoSanzione()           );  
    setNumAnni                    ( aModel.getNumAnni()                   );  
    setNumMesi                    ( aModel.getNumMesi()                   );  
    setNumGiorni                  ( aModel.getNumGiorni()                 );  
    setSanzionePecuniariaMulta    ( aModel.getSanzionePecuniariaMulta()   );  
    setSanzionePecuniariaAmmenda  ( aModel.getSanzionePecuniariaAmmenda() );  
    setPcIdPenaComplessivaCum     ( aModel.getPcIdPenaComplessivaCum()    );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setIdSanzioneSostOrigine      ( aModel.getIdSanzioneSostOrigine()     );  
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  public void setDAOFromModelForUpdate(SanzioneSostitutivaCumuloModel aModel) throws DAOException
  {
    setCodTipoSanzione            ( aModel.getCodTipoSanzione()           );  
    setNumAnni                    ( aModel.getNumAnni()                   );  
    setNumMesi                    ( aModel.getNumMesi()                   );  
    setNumGiorni                  ( aModel.getNumGiorni()                 );  
    setSanzionePecuniariaMulta    ( aModel.getSanzionePecuniariaMulta()   );  
    setSanzionePecuniariaAmmenda  ( aModel.getSanzionePecuniariaAmmenda() );  
    setPcIdPenaComplessivaCum     ( aModel.getPcIdPenaComplessivaCum()    );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setIdSanzioneSostOrigine      ( aModel.getIdSanzioneSostOrigine()     );  

    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  

    //
    setCondizioneUpdate (aModel.getIdSanzioneSostitutivaCum());
  }

  public void setCondizione(SanzioneSostitutivaCumuloModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_SANZIONE_SOST_CUM = " + key );
  }


  public void setCondizioneByIdPenaComplessivaCum(BigDecimal aIdPenaComplessivaCum)
  {
    setCondition(" PC_ID_PENA_COMPLESSIVA_CUM = " + aIdPenaComplessivaCum);
  }
}
