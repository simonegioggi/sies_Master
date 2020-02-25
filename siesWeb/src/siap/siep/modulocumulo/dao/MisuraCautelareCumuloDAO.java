package siap.siep.modulocumulo.dao;

/**
* <p>Title: MisuraCautelareCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella MisuraCautelareCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class MisuraCautelareCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public MisuraCautelareCumuloDAO (Connection con) {
    super(con);
    setTable("MISURA_CAUTELARE_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_MISURA_CAUTELARE_CUMULO","MIS_CAU_CUM_SEQ");

    //setField("ID_MISURA_CAUTELARE_CUMULO", BIG_DECIMAL);
    setField("COD_TIPO_MISURA"               , STRING);
    setField("DATA_INIZIO"                   , DATE);
    setField("DATA_FINE"                     , DATE);
    setField("NUM_ANNI"                      , BIG_DECIMAL);
    setField("NUM_MESI"                      , BIG_DECIMAL);
    setField("NUM_GIORNI"                    , BIG_DECIMAL);
    setField("GIORNI"                        , BIG_DECIMAL);
    setField("FLAG_MODIFICA_MANUALE"         , STRING);
    setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
    setField("ALTRO_LUOGO_DETENZIONE"        , STRING);
    setField("AUTORITA_COMPETENTE"           , STRING);
    setField("AUTORITA_COMPETENTE_SEDE"      , STRING);
    setField("AUTORITA_COMPETENTE_INDIRIZZO" , STRING);
    /*
    setField("FLAG_COMPUTABILE"              , STRING);
    setField("COD_MOTIVO_NON_COMPUTABILE"    , STRING);
    setField("COD_TIPO_UFFICIO_RIFER"        , STRING);
    setField("COD_LUOGO_UFFICIO_RIFER"       , STRING);
    setField("DATA_FUNGIBILITA"              , DATE);
    setField("ANNO_RIFER"                    , BIG_DECIMAL);
    setField("NUM_RIFER"                     , STRING);
    setField("NOTE"                          , STRING);
    setField("ANNO_FASC_BDMC"                , BIG_DECIMAL);
    setField("NUME_FASC_BDMC"                , BIG_DECIMAL);
    setField("CODICE_UFFICIO_PM_SEDE"        , STRING);
    setField("ANNO_RGNR"                     , BIG_DECIMAL);
    setField("NUMERO_RGNR"                   , BIG_DECIMAL);
    setField("ANNO_REG_GEN"                  , BIG_DECIMAL);
    setField("NUMERO_REG_GEN"                , BIG_DECIMAL);
    setField("TIPO_UFFICIO_REG_GEN"          , STRING);
    setField("AUTORITA_EMITTENTE"            , STRING);
    setField("AUTORITA_EMITTENTE_LUOGO"      , STRING);
    setField("DATA_EMISSIONE_ORDINANZA"      , DATE);
    */
    setField("TIT_ID_TITOLO_CUMULATO"        , BIG_DECIMAL);
    setField("FLAG_STATO"                    , STRING);
    setField("MOTIVO_MODIFICA"               , STRING);
    setField("ID_MISURA_CAUTELARE_ORIGINE"   , BIG_DECIMAL);
    
    setField("COD_OPERATORE_INSERIMENTO"     , STRING);
    setField("DATA_INSERIMENTO"              , DATE);
    setField("COD_UFFICIO_INSERIMENTO"       , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO"   , STRING);
    setField("DATA_AGGIORNAMENTO"            , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"     , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdMisuraCautelareCumulo()      throws DAOException  { return getBigDecimal ("ID_MISURA_CAUTELARE_CUMULO"    ); } 
  public  String      getCodTipoMisura()                throws DAOException  { return getString     ("COD_TIPO_MISURA"               ); } 
  public  Date        getDataInizio()                   throws DAOException  { return getDate       ("DATA_INIZIO"                   ); } 
  public  Date        getDataFine()                     throws DAOException  { return getDate       ("DATA_FINE"                     ); } 
  public  BigDecimal  getNumAnni()                      throws DAOException  { return getBigDecimal ("NUM_ANNI"                      ); } 
  public  BigDecimal  getNumMesi()                      throws DAOException  { return getBigDecimal ("NUM_MESI"                      ); } 
  public  BigDecimal  getNumGiorni()                    throws DAOException  { return getBigDecimal ("NUM_GIORNI"                    ); } 
  public  BigDecimal  getGiorni()                       throws DAOException  { return getBigDecimal ("GIORNI"                        ); } 
  public  String      getFlagModificaManuale()          throws DAOException  { return getString     ("FLAG_MODIFICA_MANUALE"); }
  
  public  String      getIstDetIdIstitutoDetenzione()   throws DAOException  { return getString     ("IST_DET_ID_ISTITUTO_DETENZIONE"); } 
  public  String      getAltroLuogoDetenzione()         throws DAOException  { return getString     ("ALTRO_LUOGO_DETENZIONE"        ); } 
  public  String      getAutoritaCompetente()           throws DAOException  { return getString     ("AUTORITA_COMPETENTE"           ); } 
  public  String      getAutoritaCompetenteSede()       throws DAOException  { return getString     ("AUTORITA_COMPETENTE_SEDE"      ); } 
  public  String      getAutoritaCompetenteIndirizzo()  throws DAOException  { return getString     ("AUTORITA_COMPETENTE_INDIRIZZO" ); } 
  
  /*
  public  String      getFlagComputabile()              throws DAOException  { return getString     ("FLAG_COMPUTABILE"              ); } 
  public  String      getCodMotivoNonComputabile()      throws DAOException  { return getString     ("COD_MOTIVO_NON_COMPUTABILE"    ); } 
  public  String      getCodTipoUfficioRifer()          throws DAOException  { return getString     ("COD_TIPO_UFFICIO_RIFER"        ); } 
  public  String      getCodLuogoUfficioRifer()         throws DAOException  { return getString     ("COD_LUOGO_UFFICIO_RIFER"       ); } 
  public  Date        getDataFungibilita()              throws DAOException  { return getDate       ("DATA_FUNGIBILITA"              ); } 
  public  BigDecimal  getAnnoRifer()                    throws DAOException  { return getBigDecimal ("ANNO_RIFER"                    ); } 
  public  String      getNumRifer()                     throws DAOException  { return getString     ("NUM_RIFER"                     ); } 
  public  String      getNote()                         throws DAOException  { return getString     ("NOTE"                          ); } 
  
  public  BigDecimal  getAnnoFascBdmc()                 throws DAOException  { return getBigDecimal ("ANNO_FASC_BDMC"                ); } 
  public  BigDecimal  getNumeFascBdmc()                 throws DAOException  { return getBigDecimal ("NUME_FASC_BDMC"                ); } 
  public  String      getCodiceUfficioPmSede()          throws DAOException  { return getString     ("CODICE_UFFICIO_PM_SEDE"        ); } 
  public  BigDecimal  getAnnoRgnr()                     throws DAOException  { return getBigDecimal ("ANNO_RGNR"                     ); } 
  public  BigDecimal  getNumeroRgnr()                   throws DAOException  { return getBigDecimal ("NUMERO_RGNR"                   ); } 
  public  BigDecimal  getAnnoRegGen()                   throws DAOException  { return getBigDecimal ("ANNO_REG_GEN"                  ); } 
  public  BigDecimal  getNumeroRegGen()                 throws DAOException  { return getBigDecimal ("NUMERO_REG_GEN"                ); } 
  public  String      getTipoUfficioRegGen()            throws DAOException  { return getString     ("TIPO_UFFICIO_REG_GEN"          ); } 
  public  String      getAutoritaEmittente()            throws DAOException  { return getString     ("AUTORITA_EMITTENTE"            ); } 
  public  String      getAutoritaEmittenteLuogo()       throws DAOException  { return getString     ("AUTORITA_EMITTENTE_LUOGO"      ); } 
  public  Date        getDataEmissioneOrdinanza()       throws DAOException  { return getDate       ("DATA_EMISSIONE_ORDINANZA"      ); } 
  */
  
  public  BigDecimal  getTitIdTitoloCumulato()          throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"        ); } 
  public  String      getFlagStato()                    throws DAOException  { return getString     ("FLAG_STATO"                    ); } 
  public  String      getMotivoModifica()               throws DAOException  { return getString     ("MOTIVO_MODIFICA"               ); } 
  public  BigDecimal  getIdMisuraCautelareOrigine()     throws DAOException  { return getBigDecimal ("ID_MISURA_CAUTELARE_ORIGINE"   ); } 
  
  public  String      getCodOperatoreInserimento()      throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"     ); } 
  public  Date        getDataInserimento()              throws DAOException  { return getDate       ("DATA_INSERIMENTO"              ); } 
  public  String      getCodUfficioInserimento()        throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"       ); } 
  public  String      getCodOperatoreAggiornamento()    throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"   ); } 
  public  Date        getDataAggiornamento()            throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"            ); } 
  public  String      getCodUfficioAggiornamento()      throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"     ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdMisuraCautelareCumulo      (BigDecimal  aValore )   { setBigDecimal ("ID_MISURA_CAUTELARE_CUMULO"    , aValore); } 
  public void  setCodTipoMisura                (String      aValore )   { setString     ("COD_TIPO_MISURA"               , aValore); } 
  public void  setDataInizio                   (Date        aValore )   { setDate       ("DATA_INIZIO"                   , aValore); } 
  public void  setDataFine                     (Date        aValore )   { setDate       ("DATA_FINE"                     , aValore); } 
  public void  setNumAnni                      (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI"                      , aValore); } 
  public void  setNumMesi                      (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI"                      , aValore); } 
  public void  setNumGiorni                    (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI"                    , aValore); } 
  public void  setGiorni                       (BigDecimal  aValore )   { setBigDecimal ("GIORNI"                        , aValore); } 
  public void  setFlagModificaManuale          (String      aValore )   { setString     ("FLAG_MODIFICA_MANUALE"         , aValore); }
  
  public void  setIstDetIdIstitutoDetenzione   (String      aValore )   { setString     ("IST_DET_ID_ISTITUTO_DETENZIONE", aValore); } 
  public void  setAltroLuogoDetenzione         (String      aValore )   { setString     ("ALTRO_LUOGO_DETENZIONE"        , aValore); } 
  public void  setAutoritaCompetente           (String      aValore )   { setString     ("AUTORITA_COMPETENTE"           , aValore); } 
  public void  setAutoritaCompetenteSede       (String      aValore )   { setString     ("AUTORITA_COMPETENTE_SEDE"      , aValore); } 
  public void  setAutoritaCompetenteIndirizzo  (String      aValore )   { setString     ("AUTORITA_COMPETENTE_INDIRIZZO" , aValore); } 
  
  /*
  public void  setFlagComputabile              (String      aValore )   { setString     ("FLAG_COMPUTABILE"              , aValore); } 
  public void  setCodMotivoNonComputabile      (String      aValore )   { setString     ("COD_MOTIVO_NON_COMPUTABILE"    , aValore); } 
  public void  setCodTipoUfficioRifer          (String      aValore )   { setString     ("COD_TIPO_UFFICIO_RIFER"        , aValore); } 
  public void  setCodLuogoUfficioRifer         (String      aValore )   { setString     ("COD_LUOGO_UFFICIO_RIFER"       , aValore); } 
  public void  setDataFungibilita              (Date        aValore )   { setDate       ("DATA_FUNGIBILITA"              , aValore); } 
  public void  setAnnoRifer                    (BigDecimal  aValore )   { setBigDecimal ("ANNO_RIFER"                    , aValore); } 
  public void  setNumRifer                     (String      aValore )   { setString     ("NUM_RIFER"                     , aValore); } 
  public void  setNote                         (String      aValore )   { setString     ("NOTE"                          , aValore); } 
  
  public void  setAnnoFascBdmc                 (BigDecimal  aValore )   { setBigDecimal ("ANNO_FASC_BDMC"                , aValore); } 
  public void  setNumeFascBdmc                 (BigDecimal  aValore )   { setBigDecimal ("NUME_FASC_BDMC"                , aValore); } 
  public void  setCodiceUfficioPmSede          (String      aValore )   { setString     ("CODICE_UFFICIO_PM_SEDE"        , aValore); } 
  public void  setAnnoRgnr                     (BigDecimal  aValore )   { setBigDecimal ("ANNO_RGNR"                     , aValore); } 
  public void  setNumeroRgnr                   (BigDecimal  aValore )   { setBigDecimal ("NUMERO_RGNR"                   , aValore); } 
  public void  setAnnoRegGen                   (BigDecimal  aValore )   { setBigDecimal ("ANNO_REG_GEN"                  , aValore); } 
  public void  setNumeroRegGen                 (BigDecimal  aValore )   { setBigDecimal ("NUMERO_REG_GEN"                , aValore); } 
  public void  setTipoUfficioRegGen            (String      aValore )   { setString     ("TIPO_UFFICIO_REG_GEN"          , aValore); } 
  public void  setAutoritaEmittente            (String      aValore )   { setString     ("AUTORITA_EMITTENTE"            , aValore); } 
  public void  setAutoritaEmittenteLuogo       (String      aValore )   { setString     ("AUTORITA_EMITTENTE_LUOGO"      , aValore); } 
  public void  setDataEmissioneOrdinanza       (Date        aValore )   { setDate       ("DATA_EMISSIONE_ORDINANZA"      , aValore); } 
  */
  
  public void  setTitIdTitoloCumulato          (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"        , aValore); } 
  public void  setFlagStato                    (String      aValore )   { setString     ("FLAG_STATO"                    , aValore); } 
  public void  setMotivoModifica               (String      aValore )   { setString     ("MOTIVO_MODIFICA"               , aValore); } 
  public void  setIdMisuraCautelareOrigine     (BigDecimal  aValore )   { setBigDecimal ("ID_MISURA_CAUTELARE_ORIGINE"   , aValore); } 
  
  public void  setCodOperatoreInserimento      (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"     , aValore); } 
  public void  setDataInserimento              (Date        aValore )   { setDate       ("DATA_INSERIMENTO"              , aValore); } 
  public void  setCodUfficioInserimento        (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"       , aValore); } 
  public void  setCodOperatoreAggiornamento    (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO"   , aValore); } 
  public void  setDataAggiornamento            (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"            , aValore); } 
  public void  setCodUfficioAggiornamento      (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"     , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new MisuraCautelareCumuloModel(  
      getIdMisuraCautelareCumulo() , 
      getCodTipoMisura() , 
 "",
      getDataInizio() , 
      getDataFine() , 
      getNumAnni() , 
      getNumMesi() , 
      getNumGiorni() , 
      getGiorni() , 
      getFlagModificaManuale(),
      
      getIstDetIdIstitutoDetenzione() , 
      getAltroLuogoDetenzione() , 
      getAutoritaCompetente() , 
      getAutoritaCompetenteSede() , 
      getAutoritaCompetenteIndirizzo() , 
      /*
      getFlagComputabile() , 
      getCodMotivoNonComputabile() , 
 "",
      getCodTipoUfficioRifer() , 
 "",
      getCodLuogoUfficioRifer() , 
 "",
      getDataFungibilita() , 
      getAnnoRifer() , 
      getNumRifer() , 
      getNote() , 
      getAnnoFascBdmc() , 
      getNumeFascBdmc() , 
      getCodiceUfficioPmSede() , 
 "",
 "",
      getAnnoRgnr() , 
      getNumeroRgnr() , 
      getAnnoRegGen() , 
      getNumeroRegGen() , 
      getTipoUfficioRegGen() , 
      getAutoritaEmittente() , 
      getAutoritaEmittenteLuogo() , 
      getDataEmissioneOrdinanza() ,
      */
      getTitIdTitoloCumulato() , 
      getFlagStato() , 
      getMotivoModifica() , 
      getIdMisuraCautelareOrigine() , 
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
  public void setDAOFromModel (MisuraCautelareCumuloModel aModel) throws DAOException {
    setIdMisuraCautelareCumulo      ( aModel.getIdMisuraCautelareCumulo()     );  
    setCodTipoMisura                ( aModel.getCodTipoMisura()               );  
    setDataInizio                   ( aModel.getDataInizio()                  );  
    setDataFine                     ( aModel.getDataFine()                    );  
    setNumAnni                      ( aModel.getNumAnni()                     );  
    setNumMesi                      ( aModel.getNumMesi()                     );  
    setNumGiorni                    ( aModel.getNumGiorni()                   );  
    setGiorni                       ( aModel.getGiorni()                      );  
    setFlagModificaManuale          ( aModel.getFlagModificaManuale()         ); 
    
    setIstDetIdIstitutoDetenzione   ( aModel.getIstDetIdIstitutoDetenzione()  );  
    setAltroLuogoDetenzione         ( aModel.getAltroLuogoDetenzione()        );  
    setAutoritaCompetente           ( aModel.getAutoritaCompetente()          );  
    setAutoritaCompetenteSede       ( aModel.getAutoritaCompetenteSede()      );  
    setAutoritaCompetenteIndirizzo  ( aModel.getAutoritaCompetenteIndirizzo() );  
    /*
    setFlagComputabile              ( aModel.getFlagComputabile()             );  
    setCodMotivoNonComputabile      ( aModel.getCodMotivoNonComputabile()     );  
    setCodTipoUfficioRifer          ( aModel.getCodTipoUfficioRifer()         );  
    setCodLuogoUfficioRifer         ( aModel.getCodLuogoUfficioRifer()        );  
    setDataFungibilita              ( aModel.getDataFungibilita()             );  
    setAnnoRifer                    ( aModel.getAnnoRifer()                   );  
    setNumRifer                     ( aModel.getNumRifer()                    );  
    setNote                         ( aModel.getNote()                        );  
    
    setAnnoFascBdmc                 ( aModel.getAnnoFascBdmc()                );  
    setNumeFascBdmc                 ( aModel.getNumeFascBdmc()                );  
    setCodiceUfficioPmSede          ( aModel.getCodiceUfficioPmSede()         );  
    setAnnoRgnr                     ( aModel.getAnnoRgnr()                    );  
    setNumeroRgnr                   ( aModel.getNumeroRgnr()                  );  
    setAnnoRegGen                   ( aModel.getAnnoRegGen()                  );  
    setNumeroRegGen                 ( aModel.getNumeroRegGen()                );  
    setTipoUfficioRegGen            ( aModel.getTipoUfficioRegGen()           );  
    setAutoritaEmittente            ( aModel.getAutoritaEmittente()           );  
    setAutoritaEmittenteLuogo       ( aModel.getAutoritaEmittenteLuogo()      );  
    setDataEmissioneOrdinanza       ( aModel.getDataEmissioneOrdinanza()      ); 
    */
    setTitIdTitoloCumulato          ( aModel.getTitIdTitoloCumulato()         );  
    setFlagStato                    ( aModel.getFlagStato()                   );  
    setMotivoModifica               ( aModel.getMotivoModifica()              );  
    setIdMisuraCautelareOrigine     ( aModel.getIdMisuraCautelareOrigine()    );  
    
    setCodOperatoreInserimento      ( aModel.getCodOperatoreInserimento()     );  
    setDataInserimento              ( aModel.getDataInserimento()             );  
    setCodUfficioInserimento        ( aModel.getCodUfficioInserimento()       );  
    setCodOperatoreAggiornamento    ( aModel.getCodOperatoreAggiornamento()   );  
    setDataAggiornamento            ( aModel.getDataAggiornamento()           );  
    setCodUfficioAggiornamento      ( aModel.getCodUfficioAggiornamento()     );  
  }
  
  /**
   * 
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForUpdate (MisuraCautelareCumuloModel aModel) throws DAOException {
    //setIdMisuraCautelareCumulo      ( aModel.getIdMisuraCautelareCumulo()     );  
    setCodTipoMisura                ( aModel.getCodTipoMisura()               );  
    setDataInizio                   ( aModel.getDataInizio()                  );  
    setDataFine                     ( aModel.getDataFine()                    );  
    setNumAnni                      ( aModel.getNumAnni()                     );  
    setNumMesi                      ( aModel.getNumMesi()                     );  
    setNumGiorni                    ( aModel.getNumGiorni()                   );  
    setGiorni                       ( aModel.getGiorni()                      );  
    setFlagModificaManuale          ( aModel.getFlagModificaManuale()         ); 
    
    setIstDetIdIstitutoDetenzione   ( aModel.getIstDetIdIstitutoDetenzione()  );  
    setAltroLuogoDetenzione         ( aModel.getAltroLuogoDetenzione()        );  
    setAutoritaCompetente           ( aModel.getAutoritaCompetente()          );  
    setAutoritaCompetenteSede       ( aModel.getAutoritaCompetenteSede()      );  
    setAutoritaCompetenteIndirizzo  ( aModel.getAutoritaCompetenteIndirizzo() );  
    
    /*
    setFlagComputabile              ( aModel.getFlagComputabile()             );  
    setCodMotivoNonComputabile      ( aModel.getCodMotivoNonComputabile()     );  
    setCodTipoUfficioRifer          ( aModel.getCodTipoUfficioRifer()         );  
    setCodLuogoUfficioRifer         ( aModel.getCodLuogoUfficioRifer()        );  
    setDataFungibilita              ( aModel.getDataFungibilita()             );  
    setAnnoRifer                    ( aModel.getAnnoRifer()                   );  
    setNumRifer                     ( aModel.getNumRifer()                    );  
    setNote                         ( aModel.getNote()                        );  
    
    setAnnoFascBdmc                 ( aModel.getAnnoFascBdmc()                );  
    setNumeFascBdmc                 ( aModel.getNumeFascBdmc()                );  
    setCodiceUfficioPmSede          ( aModel.getCodiceUfficioPmSede()         );  
    setAnnoRgnr                     ( aModel.getAnnoRgnr()                    );  
    setNumeroRgnr                   ( aModel.getNumeroRgnr()                  );  
    setAnnoRegGen                   ( aModel.getAnnoRegGen()                  );  
    setNumeroRegGen                 ( aModel.getNumeroRegGen()                );  
    setTipoUfficioRegGen            ( aModel.getTipoUfficioRegGen()           );  
    setAutoritaEmittente            ( aModel.getAutoritaEmittente()           );  
    setAutoritaEmittenteLuogo       ( aModel.getAutoritaEmittenteLuogo()      );  
    setDataEmissioneOrdinanza       ( aModel.getDataEmissioneOrdinanza()      ); 
    */
    
    //setTitIdTitoloCumulato          ( aModel.getTitIdTitoloCumulato()         );  
    setFlagStato                    ( aModel.getFlagStato()                   );  
    setMotivoModifica               ( aModel.getMotivoModifica()              );  
    //setIdMisuraCautelareOrigine     ( aModel.getIdMisuraCautelareOrigine()    );  
    
//    setCodOperatoreInserimento      ( aModel.getCodOperatoreInserimento()     );  
//    setDataInserimento              ( aModel.getDataInserimento()             );  
//    setCodUfficioInserimento        ( aModel.getCodUfficioInserimento()       );  
    setCodOperatoreAggiornamento    ( aModel.getCodOperatoreAggiornamento()   );  
    setDataAggiornamento            ( aModel.getDataAggiornamento()           );  
    setCodUfficioAggiornamento      ( aModel.getCodUfficioAggiornamento()     );  
  }  


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni (MisuraCautelareCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdMisuraCautelareCumulo() != null ) { 
      lCondizioni += " and ID_MISURA_CAUTELARE_CUMULO = " + aModel.getIdMisuraCautelareCumulo() + ""; 
    } 
    if (aModel.getCodTipoMisura() != null && aModel.getCodTipoMisura().length() > 0) { 
      lCondizioni += " and COD_TIPO_MISURA = '" + aModel.getCodTipoMisura() + "' "; 
    } 
    if (aModel.getDataInizio() != null ) { 
      lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizio(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFine() != null ) { 
      lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFine(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getNumAnni() != null ) { 
      lCondizioni += " and NUM_ANNI = " + aModel.getNumAnni() + ""; 
    } 
    if (aModel.getNumMesi() != null ) { 
      lCondizioni += " and NUM_MESI = " + aModel.getNumMesi() + ""; 
    } 
    if (aModel.getNumGiorni() != null ) { 
      lCondizioni += " and NUM_GIORNI = " + aModel.getNumGiorni() + ""; 
    } 
    if (aModel.getGiorni() != null ) { 
      lCondizioni += " and GIORNI = " + aModel.getGiorni() + ""; 
    } 
    if (aModel.getFlagModificaManuale() != null ) { 
      lCondizioni += " and FLAG_MODIFICA_MANUALE = " + aModel.getFlagModificaManuale() + ""; 
    } 
    if (aModel.getIstDetIdIstitutoDetenzione() != null && aModel.getIstDetIdIstitutoDetenzione().length() > 0) { 
      lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione() + "' "; 
    } 
    if (aModel.getAltroLuogoDetenzione() != null && aModel.getAltroLuogoDetenzione().length() > 0) { 
      lCondizioni += " and ALTRO_LUOGO_DETENZIONE = '" + aModel.getAltroLuogoDetenzione() + "' "; 
    } 
    if (aModel.getAutoritaCompetente() != null && aModel.getAutoritaCompetente().length() > 0) { 
      lCondizioni += " and AUTORITA_COMPETENTE = '" + aModel.getAutoritaCompetente() + "' "; 
    } 
    if (aModel.getAutoritaCompetenteSede() != null && aModel.getAutoritaCompetenteSede().length() > 0) { 
      lCondizioni += " and AUTORITA_COMPETENTE_SEDE = '" + aModel.getAutoritaCompetenteSede() + "' "; 
    } 
    if (aModel.getAutoritaCompetenteIndirizzo() != null && aModel.getAutoritaCompetenteIndirizzo().length() > 0) { 
      lCondizioni += " and AUTORITA_COMPETENTE_INDIRIZZO = '" + aModel.getAutoritaCompetenteIndirizzo() + "' "; 
    } 
    /*
    if (aModel.getFlagComputabile() != null && aModel.getFlagComputabile().length() > 0) { 
      lCondizioni += " and FLAG_COMPUTABILE = '" + aModel.getFlagComputabile() + "' "; 
    } 
    if (aModel.getCodMotivoNonComputabile() != null && aModel.getCodMotivoNonComputabile().length() > 0) { 
      lCondizioni += " and COD_MOTIVO_NON_COMPUTABILE = '" + aModel.getCodMotivoNonComputabile() + "' "; 
    } 
    if (aModel.getCodTipoUfficioRifer() != null && aModel.getCodTipoUfficioRifer().length() > 0) { 
      lCondizioni += " and COD_TIPO_UFFICIO_RIFER = '" + aModel.getCodTipoUfficioRifer() + "' "; 
    } 
    if (aModel.getCodLuogoUfficioRifer() != null && aModel.getCodLuogoUfficioRifer().length() > 0) { 
      lCondizioni += " and COD_LUOGO_UFFICIO_RIFER = '" + aModel.getCodLuogoUfficioRifer() + "' "; 
    } 
    if (aModel.getDataFungibilita() != null ) { 
      lCondizioni += " and to_char(DATA_FUNGIBILITA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFungibilita(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getAnnoRifer() != null ) { 
      lCondizioni += " and ANNO_RIFER = " + aModel.getAnnoRifer() + ""; 
    } 
    if (aModel.getNumRifer() != null && aModel.getNumRifer().length() > 0) { 
      lCondizioni += " and NUM_RIFER = '" + aModel.getNumRifer() + "' "; 
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    if (aModel.getAnnoFascBdmc() != null ) { 
      lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + ""; 
    } 
    if (aModel.getNumeFascBdmc() != null ) { 
      lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + ""; 
    } 
    if (aModel.getCodiceUfficioPmSede() != null && aModel.getCodiceUfficioPmSede().length() > 0) { 
      lCondizioni += " and CODICE_UFFICIO_PM_SEDE = '" + aModel.getCodiceUfficioPmSede() + "' "; 
    } 
    if (aModel.getAnnoRgnr() != null ) { 
      lCondizioni += " and ANNO_RGNR = " + aModel.getAnnoRgnr() + ""; 
    } 
    if (aModel.getNumeroRgnr() != null ) { 
      lCondizioni += " and NUMERO_RGNR = " + aModel.getNumeroRgnr() + ""; 
    } 
    if (aModel.getAnnoRegGen() != null ) { 
      lCondizioni += " and ANNO_REG_GEN = " + aModel.getAnnoRegGen() + ""; 
    } 
    if (aModel.getNumeroRegGen() != null ) { 
      lCondizioni += " and NUMERO_REG_GEN = " + aModel.getNumeroRegGen() + ""; 
    } 
    if (aModel.getTipoUfficioRegGen() != null && aModel.getTipoUfficioRegGen().length() > 0) { 
      lCondizioni += " and TIPO_UFFICIO_REG_GEN = '" + aModel.getTipoUfficioRegGen() + "' "; 
    } 
    if (aModel.getAutoritaEmittente() != null && aModel.getAutoritaEmittente().length() > 0) { 
      lCondizioni += " and AUTORITA_EMITTENTE = '" + aModel.getAutoritaEmittente() + "' "; 
    } 
    if (aModel.getAutoritaEmittenteLuogo() != null && aModel.getAutoritaEmittenteLuogo().length() > 0) { 
      lCondizioni += " and AUTORITA_EMITTENTE_LUOGO = '" + aModel.getAutoritaEmittenteLuogo() + "' "; 
    }     
    if (aModel.getDataEmissioneOrdinanza() != null ) { 
      lCondizioni += " and to_char(DATA_EMISSIONE_ORDINANZA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataEmissioneOrdinanza(),"dd/MM/yyyy") + "' "; 
    } 
    */
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    } 
    if (aModel.getIdMisuraCautelareOrigine() != null ) { 
      lCondizioni += " and ID_MISURA_CAUTELARE_ORIGINE = " + aModel.getIdMisuraCautelareOrigine() + ""; 
    } 
    if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) { 
      lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' "; 
    } 
    if (aModel.getDataInserimento() != null ) { 
      lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInserimento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' "; 
    } 
    if (aModel.getCodOperatoreAggiornamento() != null && aModel.getCodOperatoreAggiornamento().length() > 0) { 
      lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento() + "' "; 
    } 
    if (aModel.getDataAggiornamento() != null ) { 
      lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataAggiornamento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' "; 
    } 
    
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni); 
  }


  /***************************************************************************** 
   * Imposta la condizione di where per l'operazione di update puntuale 
   * si entra sempre in chiave 
   * @param key 
   ****************************************************************************/ 
  public void selCondizioneUpdate( BigDecimal aIdMisuraCautelareCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and ID_MISURA_CAUTELARE_CUMULO = " + aIdMisuraCautelareCumulo;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }


  public void selCondizioneUpdateByIdTitolo( BigDecimal aIdTitolo)
  {
     String lCondizioni = " TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;

     setCondition(lCondizioni);
  } 
  
  
  /***************************************************************************** 
   * Imposta la condizione di order by per la ricerca  
   *  
   *****************************************************************************/ 
  public void setOrderBy() { 
    String orderBy = ""; 
    //======================================================================= 
    // Lasciare orderBy="" se non si vuole scegliere un ordinamento, 
    // altrimenti elencare i campi separati da virgola 
    // n.b. non inserire la clausola ORDER BY, viene aggiunta automaticamente 
    //======================================================================= 

    setOrder(orderBy); 
  } 

}
