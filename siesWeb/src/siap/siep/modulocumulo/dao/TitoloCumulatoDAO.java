package siap.siep.modulocumulo.dao;

/**
* <p>Title: TitoloCumulatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella TitoloCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class TitoloCumulatoDAO extends TableDAO { 

  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public TitoloCumulatoDAO (Connection con) {
    super(con);
    setTable("TITOLO_CUMULATO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_TITOLO_CUMULATO","TITOLO_CUMULATO_SEQ");

    //setField("ID_TITOLO_CUMULATO", BIG_DECIMAL);
    setField("COD_TIPO_PROVVEDIMENTO"        , STRING);
    setField("DATA_IRREVOCABILITA"           , DATE);
    setField("ANNO_REGE_PM"                  , BIG_DECIMAL);
    setField("NUMERO_REGE_PM"                , STRING);
    setField("COD_SEDE_NOTIZIA_REATO"        , STRING);
    setField("ANNO_REG_GEN"                  , BIG_DECIMAL);
    setField("NUMERO_REG_GEN"                , STRING);
    setField("TIPO_REG_GEN"                  , STRING);
    setField("DATA_PROVVEDIMENTO"            , DATE);
    setField("ANNO_SENTENZA"                 , BIG_DECIMAL);
    setField("NUMERO_SENTENZA"               , STRING);
    setField("COD_TIPO_AUTORITA_EMITTENTE"   , STRING);
    setField("COD_LUOGO_EMITTENTE"           , STRING);
    setField("NUM_SEZIONE_AUTORITA_EMITTENTE", STRING);
    setField("COD_TIPO_RITO"                 , STRING);
    setField("COD_TIPO_PROVVEDIMENTO_RIF"    , STRING);
    setField("COD_TIPO_PROVV_RIF"            , STRING);
    setField("DATA_PROVV_RIF"                , DATE);
    setField("ANNO_PROVV_RIF"                , BIG_DECIMAL);
    setField("NUMERO_PROVV_RIF"              , STRING);
    setField("COD_TIPO_AUTORITA_PROVV_RIF"   , STRING);
    setField("COD_LUOGO_PROVV_RIF"           , STRING);
    setField("NUM_SEZIONE_AUTORITA_PROVV_RIF", STRING);
    setField("COD_TIPO_RITO_RIF"             , STRING);
    setField("COD_TIPO_PROVVEDIMENTO_ALTRO"  , STRING);
    setField("NOTE1_DECISIONE_CASSAZIONE"    , STRING);
    setField("NOTE2_DECISIONE_CASSAZIONE"    , STRING);
    setField("ANNO_SENTENZA_CASSAZIONE"      , BIG_DECIMAL);
    setField("NUMERO_SENTENZA_CASSAZIONE"    , STRING);
    setField("ANNO_RACCOLTA_GENERALE"        , BIG_DECIMAL);
    setField("NUMERO_RACCOLTA_GENERALE"      , STRING);
    setField("COD_TIPO_DECISIONE_CASSAZIONE" , STRING);
    setField("NOTE"                          , STRING);
    setField("ID_SENTENZA_ORIGINE"           , BIG_DECIMAL);    
    setField("ISTR_ID_ISTRUTTORIA_CUMULO"    , BIG_DECIMAL);
    setField("FLAG_STATO"                    , STRING);
    setField("MOTIVO_MODIFICA"               , STRING);
    setField("FLAG_ESCLUSO"                  , STRING);
    setField("MESS_ID_MESSAGGIO" 		     , BIG_DECIMAL);
    setField("TIPO_ISCRIZIONE"               , STRING);
    setField("DATA_PRESA_IN_CARICO"          , DATE);
    
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
  public  BigDecimal  getIdTitoloCumulato()             throws DAOException  { return getBigDecimal ("ID_TITOLO_CUMULATO"            ); } 
  public  String      getCodTipoProvvedimento()         throws DAOException  { return getString     ("COD_TIPO_PROVVEDIMENTO"        ); } 
  public  Date        getDataIrrevocabilita()           throws DAOException  { return getDate       ("DATA_IRREVOCABILITA"           ); } 
  public  BigDecimal  getAnnoRegePm()                   throws DAOException  { return getBigDecimal ("ANNO_REGE_PM"                  ); } 
  public  String      getNumeroRegePm()                 throws DAOException  { return getString     ("NUMERO_REGE_PM"                ); } 
  public  String      getCodSedeNotiziaReato()          throws DAOException  { return getString     ("COD_SEDE_NOTIZIA_REATO"        ); } 
  public  BigDecimal  getAnnoRegGen()                   throws DAOException  { return getBigDecimal ("ANNO_REG_GEN"                  ); } 
  public  String      getNumeroRegGen()                 throws DAOException  { return getString     ("NUMERO_REG_GEN"                ); } 
  public  String      getTipoRegGen()                   throws DAOException  { return getString     ("TIPO_REG_GEN"                  ); } 
  public  Date        getDataProvvedimento()            throws DAOException  { return getDate       ("DATA_PROVVEDIMENTO"            ); } 
  public  BigDecimal  getAnnoSentenza()                 throws DAOException  { return getBigDecimal ("ANNO_SENTENZA"                 ); } 
  public  String      getNumeroSentenza()               throws DAOException  { return getString     ("NUMERO_SENTENZA"               ); } 
  public  String      getCodTipoAutoritaEmittente()     throws DAOException  { return getString     ("COD_TIPO_AUTORITA_EMITTENTE"   ); } 
  public  String      getCodLuogoEmittente()            throws DAOException  { return getString     ("COD_LUOGO_EMITTENTE"           ); } 
  public  String      getNumSezioneAutoritaEmittente()  throws DAOException  { return getString     ("NUM_SEZIONE_AUTORITA_EMITTENTE"); } 
  public  String      getCodTipoRito()                  throws DAOException  { return getString     ("COD_TIPO_RITO"                 ); } 
  public  String      getCodTipoProvvedimentoRif()      throws DAOException  { return getString     ("COD_TIPO_PROVVEDIMENTO_RIF"    ); } 
  public  String      getCodTipoProvvRif()              throws DAOException  { return getString     ("COD_TIPO_PROVV_RIF"            ); } 
  public  Date        getDataProvvRif()                 throws DAOException  { return getDate       ("DATA_PROVV_RIF"                ); } 
  public  BigDecimal  getAnnoProvvRif()                 throws DAOException  { return getBigDecimal ("ANNO_PROVV_RIF"                ); } 
  public  String      getNumeroProvvRif()               throws DAOException  { return getString     ("NUMERO_PROVV_RIF"              ); } 
  public  String      getCodTipoAutoritaProvvRif()      throws DAOException  { return getString     ("COD_TIPO_AUTORITA_PROVV_RIF"   ); } 
  public  String      getCodLuogoProvvRif()             throws DAOException  { return getString     ("COD_LUOGO_PROVV_RIF"           ); } 
  public  String      getNumSezioneAutoritaProvvRif()   throws DAOException  { return getString     ("NUM_SEZIONE_AUTORITA_PROVV_RIF"); } 
  public  String      getCodTipoRitoRif()               throws DAOException  { return getString     ("COD_TIPO_RITO_RIF"             ); } 
  public  String      getCodTipoProvvedimentoAltro()    throws DAOException  { return getString     ("COD_TIPO_PROVVEDIMENTO_ALTRO"  ); } 
  public  String      getNote1DecisioneCassazione()     throws DAOException  { return getString     ("NOTE1_DECISIONE_CASSAZIONE"    ); } 
  public  String      getNote2DecisioneCassazione()     throws DAOException  { return getString     ("NOTE2_DECISIONE_CASSAZIONE"    ); } 
  public  BigDecimal  getAnnoSentenzaCassazione()       throws DAOException  { return getBigDecimal ("ANNO_SENTENZA_CASSAZIONE"      ); } 
  public  String      getNumeroSentenzaCassazione()     throws DAOException  { return getString     ("NUMERO_SENTENZA_CASSAZIONE"    ); } 
  public  BigDecimal  getAnnoRaccoltaGenerale()         throws DAOException  { return getBigDecimal ("ANNO_RACCOLTA_GENERALE"        ); } 
  public  String      getNumeroRaccoltaGenerale()       throws DAOException  { return getString     ("NUMERO_RACCOLTA_GENERALE"      ); } 
  public  String      getCodTipoDecisioneCassazione()   throws DAOException  { return getString     ("COD_TIPO_DECISIONE_CASSAZIONE" ); } 
  public  String      getNote()                         throws DAOException  { return getString     ("NOTE"                          ); } 
  
  public  BigDecimal  getIstrIdIstruttoriaCumulo()      throws DAOException  { return getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"    ); } 
  public  String      getFlagStato()                    throws DAOException  { return getString     ("FLAG_STATO"                    ); } 
  public  String      getMotivoModifica()               throws DAOException  { return getString     ("MOTIVO_MODIFICA"               ); } 
  public  BigDecimal  getIdSentenzaOrigine()            throws DAOException  { return getBigDecimal ("ID_SENTENZA_ORIGINE"           ); } 
  public  String      getFlagEscluso()                  throws DAOException  { return getString     ("FLAG_ESCLUSO"                  ); } 
  public  BigDecimal  getMessIdMessaggio()				throws DAOException  { return getBigDecimal ("MESS_ID_MESSAGGIO"			 ); }
  public  String      getTipoIscrizione()               throws DAOException  { return getString     ("TIPO_ISCRIZIONE"               ); } 
  
  public  Date        getDataPresaInCarico()            throws DAOException  { return getDate       ("DATA_PRESA_IN_CARICO"          ); } 

  public  String      getCodOperatoreInserimento()      throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"     ); } 
  public  Date        getDataInserimento()              throws DAOException  { return getDate       ("DATA_INSERIMENTO"              ); } 
  public  String      getCodUfficioInserimento()        throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"       ); } 
  public  String      getCodOperatoreAggiornamento()    throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"   ); } 
  public  Date        getDataAggiornamento()            throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"            ); } 
  public  String      getCodUfficioAggiornamento()      throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"     ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdTitoloCumulato             (BigDecimal  aValore )   { setBigDecimal ("ID_TITOLO_CUMULATO"            , aValore); } 
  public void  setCodTipoProvvedimento         (String      aValore )   { setString     ("COD_TIPO_PROVVEDIMENTO"        , aValore); } 
  public void  setDataIrrevocabilita           (Date        aValore )   { setDate       ("DATA_IRREVOCABILITA"           , aValore); } 
  public void  setAnnoRegePm                   (BigDecimal  aValore )   { setBigDecimal ("ANNO_REGE_PM"                  , aValore); } 
  public void  setNumeroRegePm                 (String      aValore )   { setString     ("NUMERO_REGE_PM"                , aValore); } 
  public void  setCodSedeNotiziaReato          (String      aValore )   { setString     ("COD_SEDE_NOTIZIA_REATO"        , aValore); } 
  public void  setAnnoRegGen                   (BigDecimal  aValore )   { setBigDecimal ("ANNO_REG_GEN"                  , aValore); } 
  public void  setNumeroRegGen                 (String      aValore )   { setString     ("NUMERO_REG_GEN"                , aValore); } 
  public void  setTipoRegGen                   (String      aValore )   { setString     ("TIPO_REG_GEN"                  , aValore); } 
  public void  setDataProvvedimento            (Date        aValore )   { setDate       ("DATA_PROVVEDIMENTO"            , aValore); } 
  public void  setAnnoSentenza                 (BigDecimal  aValore )   { setBigDecimal ("ANNO_SENTENZA"                 , aValore); } 
  public void  setNumeroSentenza               (String      aValore )   { setString     ("NUMERO_SENTENZA"               , aValore); } 
  public void  setCodTipoAutoritaEmittente     (String      aValore )   { setString     ("COD_TIPO_AUTORITA_EMITTENTE"   , aValore); } 
  public void  setCodLuogoEmittente            (String      aValore )   { setString     ("COD_LUOGO_EMITTENTE"           , aValore); } 
  public void  setNumSezioneAutoritaEmittente  (String      aValore )   { setString     ("NUM_SEZIONE_AUTORITA_EMITTENTE", aValore); } 
  public void  setCodTipoRito                  (String      aValore )   { setString     ("COD_TIPO_RITO"                 , aValore); } 
  public void  setCodTipoProvvedimentoRif      (String      aValore )   { setString     ("COD_TIPO_PROVVEDIMENTO_RIF"    , aValore); } 
  public void  setCodTipoProvvRif              (String      aValore )   { setString     ("COD_TIPO_PROVV_RIF"            , aValore); } 
  public void  setDataProvvRif                 (Date        aValore )   { setDate       ("DATA_PROVV_RIF"                , aValore); } 
  public void  setAnnoProvvRif                 (BigDecimal  aValore )   { setBigDecimal ("ANNO_PROVV_RIF"                , aValore); } 
  public void  setNumeroProvvRif               (String      aValore )   { setString     ("NUMERO_PROVV_RIF"              , aValore); } 
  public void  setCodTipoAutoritaProvvRif      (String      aValore )   { setString     ("COD_TIPO_AUTORITA_PROVV_RIF"   , aValore); } 
  public void  setCodLuogoProvvRif             (String      aValore )   { setString     ("COD_LUOGO_PROVV_RIF"           , aValore); } 
  public void  setNumSezioneAutoritaProvvRif   (String      aValore )   { setString     ("NUM_SEZIONE_AUTORITA_PROVV_RIF", aValore); } 
  public void  setCodTipoRitoRif               (String      aValore )   { setString     ("COD_TIPO_RITO_RIF"             , aValore); } 
  public void  setCodTipoProvvedimentoAltro    (String      aValore )   { setString     ("COD_TIPO_PROVVEDIMENTO_ALTRO"  , aValore); } 
  public void  setNote1DecisioneCassazione     (String      aValore )   { setString     ("NOTE1_DECISIONE_CASSAZIONE"    , aValore); } 
  public void  setNote2DecisioneCassazione     (String      aValore )   { setString     ("NOTE2_DECISIONE_CASSAZIONE"    , aValore); } 
  public void  setAnnoSentenzaCassazione       (BigDecimal  aValore )   { setBigDecimal ("ANNO_SENTENZA_CASSAZIONE"      , aValore); } 
  public void  setNumeroSentenzaCassazione     (String      aValore )   { setString     ("NUMERO_SENTENZA_CASSAZIONE"    , aValore); } 
  public void  setAnnoRaccoltaGenerale         (BigDecimal  aValore )   { setBigDecimal ("ANNO_RACCOLTA_GENERALE"        , aValore); } 
  public void  setNumeroRaccoltaGenerale       (String      aValore )   { setString     ("NUMERO_RACCOLTA_GENERALE"      , aValore); } 
  public void  setCodTipoDecisioneCassazione   (String      aValore )   { setString     ("COD_TIPO_DECISIONE_CASSAZIONE" , aValore); } 
  public void  setNote                         (String      aValore )   { setString     ("NOTE"                          , aValore); } 
  
  public void  setIstrIdIstruttoriaCumulo      (BigDecimal  aValore )   { setBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"    , aValore); } 
  public void  setFlagStato                    (String      aValore )   { setString     ("FLAG_STATO"                    , aValore); } 
  public void  setMotivoModifica               (String      aValore )   { setString     ("MOTIVO_MODIFICA"               , aValore); } 
  public void  setIdSentenzaOrigine            (BigDecimal  aValore )   { setBigDecimal ("ID_SENTENZA_ORIGINE"           , aValore); } 
  public void  setFlagEscluso                  (String      aValore )   { setString     ("FLAG_ESCLUSO"                  , aValore); }
  
  public void  setMessIdMessaggio       	   (BigDecimal  aValore )   { setBigDecimal ("MESS_ID_MESSAGGIO"      , aValore); }
  public void  setTipoIscrizione               (String      aValore )   { setString     ("TIPO_ISCRIZIONE"        , aValore); }

  public void  setDataPresaInCarico            (Date        aValore )   { setDate       ("DATA_PRESA_IN_CARICO"          , aValore); } 
  
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
    return new TitoloCumulatoModel(  
      getIdTitoloCumulato() , 
      getCodTipoProvvedimento() , 
 "",
      getDataIrrevocabilita() , 
      getAnnoRegePm() , 
      getNumeroRegePm() , 
      getCodSedeNotiziaReato() , 
 "",
      getAnnoRegGen() , 
      getNumeroRegGen() , 
      getTipoRegGen() , 
      
      getDataProvvedimento() , 
      getAnnoSentenza() , 
      getNumeroSentenza() , 
      getCodTipoAutoritaEmittente() , 
 "",
      getCodLuogoEmittente() , 
 "",
      getNumSezioneAutoritaEmittente() , 
      getCodTipoRito() , 
 "",
 
      getCodTipoProvvedimentoRif() , 
 "",
      getCodTipoProvvRif() , 
 "",
      getDataProvvRif() , 
      getAnnoProvvRif() , 
      getNumeroProvvRif() , 
      getCodTipoAutoritaProvvRif() , 
 "",
      getCodLuogoProvvRif() , 
 "",
      getNumSezioneAutoritaProvvRif() , 
      getCodTipoRitoRif() , 
 "",
      getCodTipoProvvedimentoAltro() , 
 "",
      getNote1DecisioneCassazione() , 
      getNote2DecisioneCassazione() , 
      getAnnoSentenzaCassazione() , 
      getNumeroSentenzaCassazione() , 
      getAnnoRaccoltaGenerale() , 
      getNumeroRaccoltaGenerale() , 
      getCodTipoDecisioneCassazione() , 
 "",
      getNote() ,

      getIstrIdIstruttoriaCumulo() , 
      getFlagStato() , 
      getMotivoModifica() , 
      getIdSentenzaOrigine(),
      getFlagEscluso(),
      
      getMessIdMessaggio(),
      getTipoIscrizione(),
      getDataPresaInCarico(),
      
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
  public void setDAOFromModel(TitoloCumulatoModel aModel) throws DAOException {
    setIdTitoloCumulato             ( aModel.getIdTitoloCumulato()            );  
    setCodTipoProvvedimento         ( aModel.getCodTipoProvvedimento()        );  
    setDataIrrevocabilita           ( aModel.getDataIrrevocabilita()          );  
    setAnnoRegePm                   ( aModel.getAnnoRegePm()                  );  
    setNumeroRegePm                 ( aModel.getNumeroRegePm()                );  
    setCodSedeNotiziaReato          ( aModel.getCodSedeNotiziaReato()         );  
    setAnnoRegGen                   ( aModel.getAnnoRegGen()                  );  
    setNumeroRegGen                 ( aModel.getNumeroRegGen()                );  
    setTipoRegGen                   ( aModel.getTipoRegGen()                  );  
    setDataProvvedimento            ( aModel.getDataProvvedimento()           );  
    setAnnoSentenza                 ( aModel.getAnnoSentenza()                );  
    setNumeroSentenza               ( aModel.getNumeroSentenza()              );  
    setCodTipoAutoritaEmittente     ( aModel.getCodTipoAutoritaEmittente()    );  
    setCodLuogoEmittente            ( aModel.getCodLuogoEmittente()           );  
    setNumSezioneAutoritaEmittente  ( aModel.getNumSezioneAutoritaEmittente() );  
    setCodTipoRito                  ( aModel.getCodTipoRito()                 );  
    setCodTipoProvvedimentoRif      ( aModel.getCodTipoProvvedimentoRif()     );  
    setCodTipoProvvRif              ( aModel.getCodTipoProvvRif()             );  
    setDataProvvRif                 ( aModel.getDataProvvRif()                );  
    setAnnoProvvRif                 ( aModel.getAnnoProvvRif()                );  
    setNumeroProvvRif               ( aModel.getNumeroProvvRif()              );  
    setCodTipoAutoritaProvvRif      ( aModel.getCodTipoAutoritaProvvRif()     );  
    setCodLuogoProvvRif             ( aModel.getCodLuogoProvvRif()            );  
    setNumSezioneAutoritaProvvRif   ( aModel.getNumSezioneAutoritaProvvRif()  );  
    setCodTipoRitoRif               ( aModel.getCodTipoRitoRif()              );  
    setCodTipoProvvedimentoAltro    ( aModel.getCodTipoProvvedimentoAltro()   );  
    setNote1DecisioneCassazione     ( aModel.getNote1DecisioneCassazione()    );  
    setNote2DecisioneCassazione     ( aModel.getNote2DecisioneCassazione()    );  
    setAnnoSentenzaCassazione       ( aModel.getAnnoSentenzaCassazione()      );  
    setNumeroSentenzaCassazione     ( aModel.getNumeroSentenzaCassazione()    );  
    setAnnoRaccoltaGenerale         ( aModel.getAnnoRaccoltaGenerale()        );  
    setNumeroRaccoltaGenerale       ( aModel.getNumeroRaccoltaGenerale()      );  
    setCodTipoDecisioneCassazione   ( aModel.getCodTipoDecisioneCassazione()  );  
    setNote                         ( aModel.getNote()                        );
    
    setIstrIdIstruttoriaCumulo      ( aModel.getIstrIdIstruttoriaCumulo()     );  
    setFlagStato                    ( aModel.getFlagStato()                   );  
    setMotivoModifica               ( aModel.getMotivoModifica()              );  
    setIdSentenzaOrigine            ( aModel.getIdSentenzaOrigine()            );
    setFlagEscluso                  ( aModel.getFlagEscluso()                  );
    
    setMessIdMessaggio				(aModel.getMessIdMessaggio()				);
    setTipoIscrizione				(aModel.getTipoIscrizione()					);
    
    setDataPresaInCarico            (aModel.getDataPresaInCarico());
    
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
  public void setDAOFromModelForUpdate (TitoloCumulatoModel aModel) throws DAOException {
    // setIdTitoloCumulato             ( aModel.getIdTitoloCumulato()            );  
     setCodTipoProvvedimento         ( aModel.getCodTipoProvvedimento()        );  
     setDataIrrevocabilita           ( aModel.getDataIrrevocabilita()          );  
     setAnnoRegePm                   ( aModel.getAnnoRegePm()                  );  
     setNumeroRegePm                 ( aModel.getNumeroRegePm()                );  
     setCodSedeNotiziaReato          ( aModel.getCodSedeNotiziaReato()         );  
     setAnnoRegGen                   ( aModel.getAnnoRegGen()                  );  
     setNumeroRegGen                 ( aModel.getNumeroRegGen()                );  
     setTipoRegGen                   ( aModel.getTipoRegGen()                  );  
     setDataProvvedimento            ( aModel.getDataProvvedimento()           );  
     setAnnoSentenza                 ( aModel.getAnnoSentenza()                );  
     setNumeroSentenza               ( aModel.getNumeroSentenza()              );  
     setCodTipoAutoritaEmittente     ( aModel.getCodTipoAutoritaEmittente()    );  
     setCodLuogoEmittente            ( aModel.getCodLuogoEmittente()           );  
     setNumSezioneAutoritaEmittente  ( aModel.getNumSezioneAutoritaEmittente() );  
     setCodTipoRito                  ( aModel.getCodTipoRito()                 );  
     setCodTipoProvvedimentoRif      ( aModel.getCodTipoProvvedimentoRif()     );  
     setCodTipoProvvRif              ( aModel.getCodTipoProvvRif()             );  
     setDataProvvRif                 ( aModel.getDataProvvRif()                );  
     setAnnoProvvRif                 ( aModel.getAnnoProvvRif()                );  
     setNumeroProvvRif               ( aModel.getNumeroProvvRif()              );  
     setCodTipoAutoritaProvvRif      ( aModel.getCodTipoAutoritaProvvRif()     );  
     setCodLuogoProvvRif             ( aModel.getCodLuogoProvvRif()            );  
     setNumSezioneAutoritaProvvRif   ( aModel.getNumSezioneAutoritaProvvRif()  );  
     setCodTipoRitoRif               ( aModel.getCodTipoRitoRif()              );  
     setCodTipoProvvedimentoAltro    ( aModel.getCodTipoProvvedimentoAltro()   );  
     setNote1DecisioneCassazione     ( aModel.getNote1DecisioneCassazione()    );  
     setNote2DecisioneCassazione     ( aModel.getNote2DecisioneCassazione()    );  
     setAnnoSentenzaCassazione       ( aModel.getAnnoSentenzaCassazione()      );  
     setNumeroSentenzaCassazione     ( aModel.getNumeroSentenzaCassazione()    );  
     setAnnoRaccoltaGenerale         ( aModel.getAnnoRaccoltaGenerale()        );  
     setNumeroRaccoltaGenerale       ( aModel.getNumeroRaccoltaGenerale()      );  
     setCodTipoDecisioneCassazione   ( aModel.getCodTipoDecisioneCassazione()  );  
     setNote                         ( aModel.getNote()                        );
     
     //setIstrIdIstruttoriaCumulo      ( aModel.getIstrIdIstruttoriaCumulo()     );  
     setFlagStato                    ( aModel.getFlagStato()                   );  
     setMotivoModifica               ( aModel.getMotivoModifica()              );  
     //setIdSentenzaOrigine            ( aModel.getIdSentenzaOrigine()            );
     //setFlagEscluso                  ( aModel.getFlagEscluso()                  );
     
//     setCodOperatoreInserimento      ( aModel.getCodOperatoreInserimento()     );  
//     setDataInserimento              ( aModel.getDataInserimento()             );  
//     setCodUfficioInserimento        ( aModel.getCodUfficioInserimento()       );  
     setCodOperatoreAggiornamento    ( aModel.getCodOperatoreAggiornamento()   );  
     setDataAggiornamento            ( aModel.getDataAggiornamento()           );  
     setCodUfficioAggiornamento      ( aModel.getCodUfficioAggiornamento()     );  
   }  


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(TitoloCumulatoModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdTitoloCumulato() != null ) { 
      lCondizioni += " and ID_TITOLO_CUMULATO = " + aModel.getIdTitoloCumulato() + ""; 
    } 
    if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' "; 
    } 
    if (aModel.getDataIrrevocabilita() != null ) { 
      lCondizioni += " and to_char(DATA_IRREVOCABILITA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataIrrevocabilita(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getAnnoRegePm() != null ) { 
      lCondizioni += " and ANNO_REGE_PM = " + aModel.getAnnoRegePm() + ""; 
    } 
    if (aModel.getNumeroRegePm() != null && aModel.getNumeroRegePm().length() > 0) { 
      lCondizioni += " and NUMERO_REGE_PM = '" + aModel.getNumeroRegePm() + "' "; 
    } 
    if (aModel.getCodSedeNotiziaReato() != null && aModel.getCodSedeNotiziaReato().length() > 0) { 
      lCondizioni += " and COD_SEDE_NOTIZIA_REATO = '" + aModel.getCodSedeNotiziaReato() + "' "; 
    } 
    if (aModel.getAnnoRegGen() != null ) { 
      lCondizioni += " and ANNO_REG_GEN = " + aModel.getAnnoRegGen() + ""; 
    } 
    if (aModel.getNumeroRegGen() != null && aModel.getNumeroRegGen().length() > 0) { 
      lCondizioni += " and NUMERO_REG_GEN = '" + aModel.getNumeroRegGen() + "' "; 
    } 
    if (aModel.getTipoRegGen() != null && aModel.getTipoRegGen().length() > 0) { 
      lCondizioni += " and TIPO_REG_GEN = '" + aModel.getTipoRegGen() + "' "; 
    } 
    if (aModel.getDataProvvedimento() != null ) { 
      lCondizioni += " and to_char(DATA_PROVVEDIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataProvvedimento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getAnnoSentenza() != null ) { 
      lCondizioni += " and ANNO_SENTENZA = " + aModel.getAnnoSentenza() + ""; 
    } 
    if (aModel.getNumeroSentenza() != null && aModel.getNumeroSentenza().length() > 0) { 
      lCondizioni += " and NUMERO_SENTENZA = '" + aModel.getNumeroSentenza() + "' "; 
    } 
    if (aModel.getCodTipoAutoritaEmittente() != null && aModel.getCodTipoAutoritaEmittente().length() > 0) { 
      lCondizioni += " and COD_TIPO_AUTORITA_EMITTENTE = '" + aModel.getCodTipoAutoritaEmittente() + "' "; 
    } 
    if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) { 
      lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' "; 
    } 
    if (aModel.getNumSezioneAutoritaEmittente() != null && aModel.getNumSezioneAutoritaEmittente().length() > 0) { 
      lCondizioni += " and NUM_SEZIONE_AUTORITA_EMITTENTE = '" + aModel.getNumSezioneAutoritaEmittente() + "' "; 
    } 
    if (aModel.getCodTipoRito() != null && aModel.getCodTipoRito().length() > 0) { 
      lCondizioni += " and COD_TIPO_RITO = '" + aModel.getCodTipoRito() + "' "; 
    } 
    if (aModel.getCodTipoProvvedimentoRif() != null && aModel.getCodTipoProvvedimentoRif().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO_RIF = '" + aModel.getCodTipoProvvedimentoRif() + "' "; 
    } 
    if (aModel.getCodTipoProvvRif() != null && aModel.getCodTipoProvvRif().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVV_RIF = '" + aModel.getCodTipoProvvRif() + "' "; 
    } 
    if (aModel.getDataProvvRif() != null ) { 
      lCondizioni += " and to_char(DATA_PROVV_RIF,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataProvvRif(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getAnnoProvvRif() != null ) { 
      lCondizioni += " and ANNO_PROVV_RIF = " + aModel.getAnnoProvvRif() + ""; 
    } 
    if (aModel.getNumeroProvvRif() != null && aModel.getNumeroProvvRif().length() > 0) { 
      lCondizioni += " and NUMERO_PROVV_RIF = '" + aModel.getNumeroProvvRif() + "' "; 
    } 
    if (aModel.getCodTipoAutoritaProvvRif() != null && aModel.getCodTipoAutoritaProvvRif().length() > 0) { 
      lCondizioni += " and COD_TIPO_AUTORITA_PROVV_RIF = '" + aModel.getCodTipoAutoritaProvvRif() + "' "; 
    } 
    if (aModel.getCodLuogoProvvRif() != null && aModel.getCodLuogoProvvRif().length() > 0) { 
      lCondizioni += " and COD_LUOGO_PROVV_RIF = '" + aModel.getCodLuogoProvvRif() + "' "; 
    } 
    if (aModel.getNumSezioneAutoritaProvvRif() != null && aModel.getNumSezioneAutoritaProvvRif().length() > 0) { 
      lCondizioni += " and NUM_SEZIONE_AUTORITA_PROVV_RIF = '" + aModel.getNumSezioneAutoritaProvvRif() + "' "; 
    } 
    if (aModel.getCodTipoRitoRif() != null && aModel.getCodTipoRitoRif().length() > 0) { 
      lCondizioni += " and COD_TIPO_RITO_RIF = '" + aModel.getCodTipoRitoRif() + "' "; 
    } 
    if (aModel.getCodTipoProvvedimentoAltro() != null && aModel.getCodTipoProvvedimentoAltro().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO_ALTRO = '" + aModel.getCodTipoProvvedimentoAltro() + "' "; 
    } 
    if (aModel.getNote1DecisioneCassazione() != null && aModel.getNote1DecisioneCassazione().length() > 0) { 
      lCondizioni += " and NOTE1_DECISIONE_CASSAZIONE = '" + aModel.getNote1DecisioneCassazione() + "' "; 
    } 
    if (aModel.getNote2DecisioneCassazione() != null && aModel.getNote2DecisioneCassazione().length() > 0) { 
      lCondizioni += " and NOTE2_DECISIONE_CASSAZIONE = '" + aModel.getNote2DecisioneCassazione() + "' "; 
    } 
    if (aModel.getAnnoSentenzaCassazione() != null ) { 
      lCondizioni += " and ANNO_SENTENZA_CASSAZIONE = " + aModel.getAnnoSentenzaCassazione() + ""; 
    } 
    if (aModel.getNumeroSentenzaCassazione() != null && aModel.getNumeroSentenzaCassazione().length() > 0) { 
      lCondizioni += " and NUMERO_SENTENZA_CASSAZIONE = '" + aModel.getNumeroSentenzaCassazione() + "' "; 
    } 
    if (aModel.getAnnoRaccoltaGenerale() != null ) { 
      lCondizioni += " and ANNO_RACCOLTA_GENERALE = " + aModel.getAnnoRaccoltaGenerale() + ""; 
    } 
    if (aModel.getNumeroRaccoltaGenerale() != null && aModel.getNumeroRaccoltaGenerale().length() > 0) { 
      lCondizioni += " and NUMERO_RACCOLTA_GENERALE = '" + aModel.getNumeroRaccoltaGenerale() + "' "; 
    } 
    if (aModel.getCodTipoDecisioneCassazione() != null && aModel.getCodTipoDecisioneCassazione().length() > 0) { 
      lCondizioni += " and COD_TIPO_DECISIONE_CASSAZIONE = '" + aModel.getCodTipoDecisioneCassazione() + "' "; 
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    if (aModel.getIstrIdIstruttoriaCumulo() != null ) { 
      lCondizioni += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aModel.getIstrIdIstruttoriaCumulo() + ""; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    }     
    if (aModel.getIdSentenzaOrigine() != null ) { 
      lCondizioni += " and ID_SENTENZA_ORIGINE = " + aModel.getIdSentenzaOrigine() + " "; 
    }
    if (aModel.getFlagEscluso() != null && aModel.getFlagEscluso().length() > 0) { 
      lCondizioni += " and FLAG_ESCLUSO = '" + aModel.getFlagEscluso() + "' "; 
    }       
    
    if (aModel.getDataPresaInCarico() != null ) { 
      lCondizioni += " and to_char(DATA_PRESA_IN_CARICO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataPresaInCarico(),"dd/MM/yyyy") + "' "; 
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
  public void selCondizioneUpdate( BigDecimal aIdTitoloCumulato) {
    String lCondizioni = new String();

    lCondizioni += " and ID_TITOLO_CUMULATO = " + aIdTitoloCumulato;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

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
