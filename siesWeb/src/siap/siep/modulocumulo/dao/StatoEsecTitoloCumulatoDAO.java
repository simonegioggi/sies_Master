package siap.siep.modulocumulo.dao;

/**
* <p>Title: StatoEsecTitoloCumulatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella StatoEsecTitoloCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import f3b.util.DateUtils;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;

public class StatoEsecTitoloCumulatoDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public StatoEsecTitoloCumulatoDAO (Connection con) {
    super(con);
    setTable("STATO_ESEC_TITOLO_CUMULATO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_STATO_ESEC_TITOLO_CUMULATO","STATO_ESEC_TITOLO_CUMULATO_SEQ");

    //setField("ID_STATO_ESEC_TITOLO_CUMULATO", BIG_DECIMAL);
    setField("COD_TIPO_EVENTO"              , STRING);
    setField("COD_TIPO_PROVVEDIMENTO"       , STRING);
    setField("COD_MOTIVO"                   , STRING);
    setField("COD_MOTIVO_REVOCA"            , STRING);    
    setField("COD_MOTIVO_REVOCA_PM"         , STRING);
    
    setField("COD_UFFICIO_EMITTENTE"        , STRING);
    setField("COD_AUTORITA_EMITTENTE"       , STRING);
    setField("COD_LUOGO_EMITTENTE"          , STRING);
    setField("DATA_EMISSIONE"               , DATE);
    setField("COD_ESITO"                    , STRING);
    setField("COD_ESITO_TENORE"             , STRING);
    setField("ANNO_PROCEDIMENTO"            , BIG_DECIMAL);
    setField("PROGR_PROCEDIMENTO"           , BIG_DECIMAL);
    setField("ANNO_PROVVEDIMENTO"           , BIG_DECIMAL);
    setField("PROGR_PROVVEDIMENTO"          , BIG_DECIMAL);
    setField("NOTE"                         , STRING);
    
    setField("COD_CONTENUTO_ISTANZA"        , STRING);
    setField("DATA_ISTANZA"                 , DATE);
    setField("FLAG_ISTANZA_PRESDEP"         , STRING);
    setField("COD_STATO_ISTANZA"            , STRING);
    setField("COD_TIPO_UFFICIO_DESTINATARIO", STRING);
    setField("COD_LUOGO_DESTINATARIO"       , STRING);
    setField("COD_UFFICIO_DESTINATARIO"     , STRING);
    setField("DATA_TRASMISSIONE"            , DATE);    
    
    setField("FLAG_TIPO_SOSP"               , STRING);
    
    setField("COD_TIPO_ISTANTE"             , STRING);
    setField("COD_TIPO_UFFICIO_ALTRO"       , STRING);
    setField("COD_TIPO_AUTORITA_ALTRO"      , STRING);
    setField("COD_LUOGO_ALTRO"              , STRING);
    setField("COD_UFFICIO_ALTRO"            , STRING);
    setField("SEZIONE_ALTRO"                , STRING);
    setField("DATA_EMISSIONE_ALTRO"         , DATE);    
    
    setField("TIT_ID_TITOLO_CUMULATO"       , BIG_DECIMAL);
    setField("ISTR_ID_ISTRUTTORIA_CUMULO"   , BIG_DECIMAL);
    setField("FLAG_STATO"                   , STRING);
    setField("MOTIVO_MODIFICA"              , STRING);
    setField("ID_EVENTO_ORIGINE"            , BIG_DECIMAL);
    setField("EVE_ID_EVENTO_ORIGINE"        , BIG_DECIMAL);
    
    setField("COD_OPERATORE_INSERIMENTO"    , STRING);
    setField("DATA_INSERIMENTO"             , DATE);
    setField("COD_UFFICIO_INSERIMENTO"      , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO"  , STRING);
    setField("DATA_AGGIORNAMENTO"           , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"    , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdStatoEsecTitoloCumulato()  throws DAOException  { return getBigDecimal ("ID_STATO_ESEC_TITOLO_CUMULATO"); } 
  public  String      getCodTipoEvento()              throws DAOException  { return getString     ("COD_TIPO_EVENTO"              ); } 
  public  String      getCodTipoProvvedimento()       throws DAOException  { return getString     ("COD_TIPO_PROVVEDIMENTO"       ); } 
  public  String      getCodMotivo()                  throws DAOException  { return getString     ("COD_MOTIVO"                   ); } 
  public  String      getCodMotivoRevoca()            throws DAOException  { return getString     ("COD_MOTIVO_REVOCA"            ); } 
  public  String      getCodMotivoRevocaPm()          throws DAOException  { return getString     ("COD_MOTIVO_REVOCA_PM"         ); } 
  public  String      getCodUfficioEmittente()        throws DAOException  { return getString     ("COD_UFFICIO_EMITTENTE"        ); } 
  public  String      getCodAutoritaEmittente()       throws DAOException  { return getString     ("COD_AUTORITA_EMITTENTE"       ); } 
  public  String      getCodLuogoEmittente()          throws DAOException  { return getString     ("COD_LUOGO_EMITTENTE"          ); } 
  public  Date        getDataEmissione()              throws DAOException  { return getDate       ("DATA_EMISSIONE"               ); } 
  public  String      getCodEsito()                   throws DAOException  { return getString     ("COD_ESITO"                    ); } 
  public  String      getCodEsitoTenore()             throws DAOException  { return getString     ("COD_ESITO_TENORE"             ); } 
  public  BigDecimal  getAnnoProcedimento()           throws DAOException  { return getBigDecimal ("ANNO_PROCEDIMENTO"            ); } 
  public  BigDecimal  getProgrProcedimento()          throws DAOException  { return getBigDecimal ("PROGR_PROCEDIMENTO"           ); } 
  public  BigDecimal  getAnnoProvvedimento()          throws DAOException  { return getBigDecimal ("ANNO_PROVVEDIMENTO"           ); } 
  public  BigDecimal  getProgrProvvedimento()         throws DAOException  { return getBigDecimal ("PROGR_PROVVEDIMENTO"          ); } 
  public  String      getNote()                       throws DAOException  { return getString     ("NOTE"                         ); } 
  
  public  String      getCodContenutoIstanza()         throws DAOException  { return getString     ("COD_CONTENUTO_ISTANZA"        ); } 
  public  Date        getDataIstanza()                 throws DAOException  { return getDate       ("DATA_ISTANZA"                 ); } 
  public  String      getFlagIstanzaPresdep()          throws DAOException  { return getString     ("FLAG_ISTANZA_PRESDEP"         ); } 
  public  String      getCodStatoIstanza()             throws DAOException  { return getString     ("COD_STATO_ISTANZA"            ); } 
  
  public  String      getCodTipoUfficioDestinatario()  throws DAOException  { return getString     ("COD_TIPO_UFFICIO_DESTINATARIO"); } 
  public  String      getCodLuogoDestinatario()        throws DAOException  { return getString     ("COD_LUOGO_DESTINATARIO"       ); } 
  public  String      getCodUfficioDestinatario()      throws DAOException  { return getString     ("COD_UFFICIO_DESTINATARIO"     ); } 
  public  Date        getDataTrasmissione()            throws DAOException  { return getDate       ("DATA_TRASMISSIONE"            ); }   
  
  public  String      getFlagTipoSosp()                throws DAOException  { return getString     ("FLAG_TIPO_SOSP"               ); } 
  
  public  String      getCodTipoIstante()              throws DAOException  { return getString     ("COD_TIPO_ISTANTE"             ); } 
  public  String      getCodTipoUfficioAltro()         throws DAOException  { return getString     ("COD_TIPO_UFFICIO_ALTRO"       ); } 
  public  String      getCodTipoAutoritaAltro()        throws DAOException  { return getString     ("COD_TIPO_AUTORITA_ALTRO"      ); } 
  public  String      getCodLuogoAltro()               throws DAOException  { return getString     ("COD_LUOGO_ALTRO"              ); } 
  public  String      getCodUfficioAltro()             throws DAOException  { return getString     ("COD_UFFICIO_ALTRO"            ); } 
  public  String      getSezioneAltro()                throws DAOException  { return getString     ("SEZIONE_ALTRO"                ); } 
  public  Date        getDataEmissioneAltro()          throws DAOException  { return getDate       ("DATA_EMISSIONE_ALTRO"         ); }
  
  public  BigDecimal  getTitIdTitoloCumulato()        throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"       ); } 
  public  BigDecimal  getIstrIdIstruttoriaCumulo()    throws DAOException  { return getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"   ); } 
  public  String      getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                   ); } 
  public  String      getMotivoModifica()             throws DAOException  { return getString     ("MOTIVO_MODIFICA"              ); } 
  public  BigDecimal  getIdEventoOrigine()            throws DAOException  { return getBigDecimal ("ID_EVENTO_ORIGINE"            ); } 
  public  BigDecimal  getEveIdEventoOrigine()         throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO_ORIGINE"        ); } 
  
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"    ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"             ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"      ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"  ); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"           ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"    ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdStatoEsecTitoloCumulato  (BigDecimal  aValore )   { setBigDecimal ("ID_STATO_ESEC_TITOLO_CUMULATO", aValore); } 
  public void  setCodTipoEvento              (String      aValore )   { setString     ("COD_TIPO_EVENTO"              , aValore); } 
  public void  setCodTipoProvvedimento       (String      aValore )   { setString     ("COD_TIPO_PROVVEDIMENTO"       , aValore); } 
  public void  setCodMotivo                  (String      aValore )   { setString     ("COD_MOTIVO"                   , aValore); } 
  public void  setCodMotivoRevoca            (String      aValore )   { setString     ("COD_MOTIVO_REVOCA"            , aValore); } 
  public void  setCodMotivoRevocaPm          (String      aValore )   { setString     ("COD_MOTIVO_REVOCA_PM"         , aValore); } 
  public void  setCodUfficioEmittente        (String      aValore )   { setString     ("COD_UFFICIO_EMITTENTE"        , aValore); } 
  public void  setCodAutoritaEmittente       (String      aValore )   { setString     ("COD_AUTORITA_EMITTENTE"       , aValore); } 
  public void  setCodLuogoEmittente          (String      aValore )   { setString     ("COD_LUOGO_EMITTENTE"          , aValore); } 
  public void  setDataEmissione              (Date        aValore )   { setDate       ("DATA_EMISSIONE"               , aValore); } 
  public void  setCodEsito                   (String      aValore )   { setString     ("COD_ESITO"                    , aValore); } 
  public void  setCodEsitoTenore             (String      aValore )   { setString     ("COD_ESITO_TENORE"             , aValore); } 
  public void  setAnnoProcedimento           (BigDecimal  aValore )   { setBigDecimal ("ANNO_PROCEDIMENTO"            , aValore); } 
  public void  setProgrProcedimento          (BigDecimal  aValore )   { setBigDecimal ("PROGR_PROCEDIMENTO"           , aValore); } 
  public void  setAnnoProvvedimento          (BigDecimal  aValore )   { setBigDecimal ("ANNO_PROVVEDIMENTO"           , aValore); } 
  public void  setProgrProvvedimento         (BigDecimal  aValore )   { setBigDecimal ("PROGR_PROVVEDIMENTO"          , aValore); } 
  public void  setNote                       (String      aValore )   { setString     ("NOTE"                         , aValore); } 
  
  public void  setCodContenutoIstanza         (String      aValore )   { setString     ("COD_CONTENUTO_ISTANZA"        , aValore); } 
  public void  setDataIstanza                 (Date        aValore )   { setDate       ("DATA_ISTANZA"                 , aValore); } 
  public void  setFlagIstanzaPresdep          (String      aValore )   { setString     ("FLAG_ISTANZA_PRESDEP"         , aValore); } 
  public void  setCodStatoIstanza             (String      aValore )   { setString     ("COD_STATO_ISTANZA"            , aValore); } 

  public void  setCodTipoUfficioDestinatario  (String      aValore )   { setString     ("COD_TIPO_UFFICIO_DESTINATARIO", aValore); } 
  public void  setCodLuogoDestinatario        (String      aValore )   { setString     ("COD_LUOGO_DESTINATARIO"       , aValore); } 
  public void  setCodUfficioDestinatario      (String      aValore )   { setString     ("COD_UFFICIO_DESTINATARIO"     , aValore); } 
  public void  setDataTrasmissione            (Date        aValore )   { setDate       ("DATA_TRASMISSIONE"            , aValore); } 
  
  public void  setFlagTipoSosp                (String      aValore )   { setString     ("FLAG_TIPO_SOSP"               , aValore); } 
  
  public void  setCodTipoIstante              (String      aValore )   { setString     ("COD_TIPO_ISTANTE"             , aValore); } 
  public void  setCodTipoUfficioAltro         (String      aValore )   { setString     ("COD_TIPO_UFFICIO_ALTRO"       , aValore); } 
  public void  setCodTipoAutoritaAltro        (String      aValore )   { setString     ("COD_TIPO_AUTORITA_ALTRO"      , aValore); } 
  public void  setCodLuogoAltro               (String      aValore )   { setString     ("COD_LUOGO_ALTRO"              , aValore); } 
  public void  setCodUfficioAltro             (String      aValore )   { setString     ("COD_UFFICIO_ALTRO"            , aValore); } 
  public void  setSezioneAltro                (String      aValore )   { setString     ("SEZIONE_ALTRO"                , aValore); } 
  public void  setDataEmissioneAltro          (Date        aValore )   { setDate       ("DATA_EMISSIONE_ALTRO"         , aValore); }   
  
  public void  setTitIdTitoloCumulato        (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"       , aValore); } 
  public void  setIstrIdIstruttoriaCumulo    (BigDecimal  aValore )   { setBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"   , aValore); } 
  public void  setFlagStato                  (String      aValore )   { setString     ("FLAG_STATO"                   , aValore); } 
  public void  setMotivoModifica             (String      aValore )   { setString     ("MOTIVO_MODIFICA"              , aValore); } 
  public void  setIdEventoOrigine            (BigDecimal  aValore )   { setBigDecimal ("ID_EVENTO_ORIGINE"            , aValore); } 
  public void  setEveIdEventoOrigine         (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO_ORIGINE"        , aValore); } 
  
  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"    , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"             , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"      , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO"  , aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"           , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"    , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new StatoEsecTitoloCumulatoModel(  
      getIdStatoEsecTitoloCumulato() , 
      getCodTipoEvento() ,  "",
      getCodTipoProvvedimento() ,  "",
      getCodMotivo() ,  "",
      getCodMotivoRevoca() , "",
      getCodUfficioEmittente() ,  "",
      getCodAutoritaEmittente() , "",
      getCodLuogoEmittente() ,  "",
      getDataEmissione() , 
      getCodEsito() ,  "",
      getCodEsitoTenore() ,  "",
      getAnnoProcedimento() , 
      getProgrProcedimento() , 
      getAnnoProvvedimento() , 
      getProgrProvvedimento() , 
      getNote() , 
      
      getCodMotivoRevocaPm() , "",
      getCodContenutoIstanza() , "",
      getDataIstanza() , 
      getFlagIstanzaPresdep() , 
      getCodStatoIstanza() , "",
      getCodTipoUfficioDestinatario() ,  "",
      getCodLuogoDestinatario() ,  "",
      getCodUfficioDestinatario() ,  "",
      getDataTrasmissione(),      
      
      getFlagTipoSosp(),
      
      getCodTipoIstante() , 
      getCodTipoUfficioAltro() ,  "",
      getCodTipoAutoritaAltro() ,  "",
      getCodLuogoAltro() ,  "",
      getCodUfficioAltro() ,
      getSezioneAltro() , 
      getDataEmissioneAltro(),
      
      getTitIdTitoloCumulato() , 
      getIstrIdIstruttoriaCumulo() , 
      getFlagStato() , 
      getMotivoModifica() , 
      getIdEventoOrigine() , 
      getEveIdEventoOrigine() , 
      
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
  public void setDAOFromModel(StatoEsecTitoloCumulatoModel aModel) throws DAOException {
    setIdStatoEsecTitoloCumulato  ( aModel.getIdStatoEsecTitoloCumulato() );  
    setCodTipoEvento              ( aModel.getCodTipoEvento()             );  
    setCodTipoProvvedimento       ( aModel.getCodTipoProvvedimento()      );  
    setCodMotivo                  ( aModel.getCodMotivo()                 );  
    setCodMotivoRevoca            ( aModel.getCodMotivoRevoca()           );  
    setCodMotivoRevocaPm          ( aModel.getCodMotivoRevocaPm()         );  
    
    setCodUfficioEmittente        ( aModel.getCodUfficioEmittente()       );  
    setCodAutoritaEmittente       ( aModel.getCodAutoritaEmittente()      );  
    setCodLuogoEmittente          ( aModel.getCodLuogoEmittente()         );  
    setDataEmissione              ( aModel.getDataEmissione()             );  
    
    setCodEsito                   ( aModel.getCodEsito()                  );  
    setCodEsitoTenore             ( aModel.getCodEsitoTenore()            );  
    setAnnoProcedimento           ( aModel.getAnnoProcedimento()          );  
    setProgrProcedimento          ( aModel.getProgrProcedimento()         );  
    setAnnoProvvedimento          ( aModel.getAnnoProvvedimento()         );  
    setProgrProvvedimento         ( aModel.getProgrProvvedimento()        );  
    setNote                       ( aModel.getNote()                      );  
    
    setCodContenutoIstanza         ( aModel.getCodContenutoIstanza()        );  
    setDataIstanza                 ( aModel.getDataIstanza()                );  
    setFlagIstanzaPresdep          ( aModel.getFlagIstanzaPresdep()         );  
    setCodStatoIstanza             ( aModel.getCodStatoIstanza()            );  
    
    setCodTipoUfficioDestinatario  ( aModel.getCodTipoUfficioDestinatario() );  
    setCodLuogoDestinatario        ( aModel.getCodLuogoDestinatario()       );  
    setCodUfficioDestinatario      ( aModel.getCodUfficioDestinatario()     );  
    setDataTrasmissione            ( aModel.getDataTrasmissione()           );     
    
    setFlagTipoSosp                ( aModel.getFlagTipoSosp()             );
    
    setCodTipoIstante              ( aModel.getCodTipoIstante()             );  
    setCodTipoUfficioAltro         ( aModel.getCodTipoUfficioAltro()        );  
    setCodTipoAutoritaAltro        ( aModel.getCodTipoAutoritaAltro()       );  
    setCodLuogoAltro               ( aModel.getCodLuogoAltro()              );  
    setCodUfficioAltro             ( aModel.getCodUfficioAltro()            );  
    setSezioneAltro                ( aModel.getSezioneAltro()               );  
    setDataEmissioneAltro          ( aModel.getDataEmissioneAltro()         );      
    
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setIstrIdIstruttoriaCumulo    ( aModel.getIstrIdIstruttoriaCumulo()   );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setIdEventoOrigine            ( aModel.getIdEventoOrigine()           );  
    setEveIdEventoOrigine         ( aModel.getEveIdEventoOrigine()        );  
    
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }
  
  
  public void setDAOFromModelForUpdate (StatoEsecTitoloCumulatoModel aModel) throws DAOException {
    //setIdStatoEsecTitoloCumulato  ( aModel.getIdStatoEsecTitoloCumulato() );  
    setCodTipoEvento              ( aModel.getCodTipoEvento()             );  
    setCodTipoProvvedimento       ( aModel.getCodTipoProvvedimento()      );  
    setCodMotivo                  ( aModel.getCodMotivo()                 );  
    setCodMotivoRevoca            ( aModel.getCodMotivoRevoca()           );  
    setCodMotivoRevocaPm          ( aModel.getCodMotivoRevocaPm()         );  
    
    setCodUfficioEmittente        ( aModel.getCodUfficioEmittente()       );  
    setCodAutoritaEmittente       ( aModel.getCodAutoritaEmittente()      );  
    setCodLuogoEmittente          ( aModel.getCodLuogoEmittente()         );  
    setDataEmissione              ( aModel.getDataEmissione()             );  
    
    setCodEsito                   ( aModel.getCodEsito()                  );  
    setCodEsitoTenore             ( aModel.getCodEsitoTenore()            );  
    setAnnoProcedimento           ( aModel.getAnnoProcedimento()          );  
    setProgrProcedimento          ( aModel.getProgrProcedimento()         );  
    setAnnoProvvedimento          ( aModel.getAnnoProvvedimento()         );  
    setProgrProvvedimento         ( aModel.getProgrProvvedimento()        );
    
    setNote                       ( aModel.getNote()                      );  
    
    setCodContenutoIstanza         ( aModel.getCodContenutoIstanza()        );  
    setDataIstanza                 ( aModel.getDataIstanza()                );  
    setFlagIstanzaPresdep          ( aModel.getFlagIstanzaPresdep()         );  
    setCodStatoIstanza             ( aModel.getCodStatoIstanza()            );  
    
    setCodTipoUfficioDestinatario  ( aModel.getCodTipoUfficioDestinatario() );  
    setCodLuogoDestinatario        ( aModel.getCodLuogoDestinatario()       );  
    setCodUfficioDestinatario      ( aModel.getCodUfficioDestinatario()     );  
    setDataTrasmissione            ( aModel.getDataTrasmissione()           );      
    
    //setFlagTipoSosp                ( aModel.getFlagTipoSosp()               );
    setCodTipoIstante              ( aModel.getCodTipoIstante()             );  
    setCodTipoUfficioAltro         ( aModel.getCodTipoUfficioAltro()        );  
    setCodTipoAutoritaAltro        ( aModel.getCodTipoAutoritaAltro()       );  
    setCodLuogoAltro               ( aModel.getCodLuogoAltro()              );  
    setCodUfficioAltro             ( aModel.getCodUfficioAltro()            );  
    setSezioneAltro                ( aModel.getSezioneAltro()               );  
    setDataEmissioneAltro          ( aModel.getDataEmissioneAltro()         );      
    
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setIstrIdIstruttoriaCumulo    ( aModel.getIstrIdIstruttoriaCumulo()   );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setIdEventoOrigine            ( aModel.getIdEventoOrigine()           );  
    setEveIdEventoOrigine         ( aModel.getEveIdEventoOrigine()        );  
    
//    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
//    setDataInserimento            ( aModel.getDataInserimento()           );  
//    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }  


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(StatoEsecTitoloCumulatoModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdStatoEsecTitoloCumulato() != null ) { 
      lCondizioni += " and ID_STATO_ESEC_TITOLO_CUMULATO = " + aModel.getIdStatoEsecTitoloCumulato() + ""; 
    } 
    if (aModel.getCodTipoEvento() != null && aModel.getCodTipoEvento().length() > 0) { 
      lCondizioni += " and COD_TIPO_EVENTO = '" + aModel.getCodTipoEvento() + "' "; 
    } 
    if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' "; 
    } 
    if (aModel.getCodMotivo() != null && aModel.getCodMotivo().length() > 0) { 
      lCondizioni += " and COD_MOTIVO = '" + aModel.getCodMotivo() + "' "; 
    }
    if (aModel.getCodMotivoRevoca() != null && aModel.getCodMotivoRevoca().length() > 0) { 
      lCondizioni += " and COD_MOTIVO_REVOCA = '" + aModel.getCodMotivoRevoca() + "' "; 
    }     
    if (aModel.getCodMotivoRevocaPm() != null && aModel.getCodMotivoRevocaPm().length() > 0) { 
      lCondizioni += " and COD_MOTIVO_REVOCA_PM = '" + aModel.getCodMotivoRevocaPm() + "' "; 
    } 
    if (aModel.getCodUfficioEmittente() != null && aModel.getCodUfficioEmittente().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_EMITTENTE = '" + aModel.getCodUfficioEmittente() + "' "; 
    } 
    if (aModel.getCodAutoritaEmittente() != null && aModel.getCodAutoritaEmittente().length() > 0) { 
      lCondizioni += " and COD_AUTORITA_EMITTENTE = '" + aModel.getCodAutoritaEmittente() + "' "; 
    } 
    if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) { 
      lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' "; 
    } 
    if (aModel.getDataEmissione() != null ) { 
      lCondizioni += " and to_char(DATA_EMISSIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataEmissione(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodEsito() != null && aModel.getCodEsito().length() > 0) { 
      lCondizioni += " and COD_ESITO = '" + aModel.getCodEsito() + "' "; 
    } 
    if (aModel.getCodEsitoTenore() != null && aModel.getCodEsitoTenore().length() > 0) { 
      lCondizioni += " and COD_ESITO_TENORE = '" + aModel.getCodEsitoTenore() + "' "; 
    } 
    if (aModel.getAnnoProcedimento() != null ) { 
      lCondizioni += " and ANNO_PROCEDIMENTO = " + aModel.getAnnoProcedimento() + ""; 
    } 
    if (aModel.getProgrProcedimento() != null ) { 
      lCondizioni += " and PROGR_PROCEDIMENTO = " + aModel.getProgrProcedimento() + ""; 
    } 
    if (aModel.getAnnoProvvedimento() != null ) { 
      lCondizioni += " and ANNO_PROVVEDIMENTO = " + aModel.getAnnoProvvedimento() + ""; 
    } 
    if (aModel.getProgrProvvedimento() != null ) { 
      lCondizioni += " and PROGR_PROVVEDIMENTO = " + aModel.getProgrProvvedimento() + ""; 
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    
    if (aModel.getCodContenutoIstanza() != null && aModel.getCodContenutoIstanza().length() > 0) { 
      lCondizioni += " and COD_CONTENUTO_ISTANZA = '" + aModel.getCodContenutoIstanza() + "' "; 
    } 
    if (aModel.getDataIstanza() != null ) { 
      lCondizioni += " and to_char(DATA_ISTANZA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataIstanza(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getFlagIstanzaPresdep() != null && aModel.getFlagIstanzaPresdep().length() > 0) { 
      lCondizioni += " and FLAG_ISTANZA_PRESDEP = '" + aModel.getFlagIstanzaPresdep() + "' "; 
    } 
    if (aModel.getCodStatoIstanza() != null && aModel.getCodStatoIstanza().length() > 0) { 
      lCondizioni += " and COD_STATO_ISTANZA = '" + aModel.getCodStatoIstanza() + "' "; 
    } 
    if (aModel.getCodTipoUfficioDestinatario() != null && aModel.getCodTipoUfficioDestinatario().length() > 0) { 
      lCondizioni += " and COD_TIPO_UFFICIO_DESTINATARIO = '" + aModel.getCodTipoUfficioDestinatario() + "' "; 
    } 
    if (aModel.getCodLuogoDestinatario() != null && aModel.getCodLuogoDestinatario().length() > 0) { 
      lCondizioni += " and COD_LUOGO_DESTINATARIO = '" + aModel.getCodLuogoDestinatario() + "' "; 
    } 
    if (aModel.getCodUfficioDestinatario() != null && aModel.getCodUfficioDestinatario().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_DESTINATARIO = '" + aModel.getCodUfficioDestinatario() + "' "; 
    } 
    if (aModel.getDataTrasmissione() != null ) { 
      lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataTrasmissione(),"dd/MM/yyyy") + "' "; 
    }
    
    if (aModel.getFlagTipoSosp() != null && aModel.getFlagTipoSosp().length() > 0) { 
      lCondizioni += " and FLAG_TIPO_SOSP = '" + aModel.getFlagTipoSosp() + "' "; 
    } 
    
    if (aModel.getCodTipoIstante() != null && aModel.getCodTipoIstante().length() > 0) { 
      lCondizioni += " and COD_TIPO_ISTANTE = '" + aModel.getCodTipoIstante() + "' "; 
    } 
    if (aModel.getCodTipoUfficioAltro() != null && aModel.getCodTipoUfficioAltro().length() > 0) { 
      lCondizioni += " and COD_TIPO_UFFICIO_ALTRO = '" + aModel.getCodTipoUfficioAltro() + "' "; 
    } 
    if (aModel.getCodTipoAutoritaAltro() != null && aModel.getCodTipoAutoritaAltro().length() > 0) { 
      lCondizioni += " and COD_TIPO_AUTORITA_ALTRO = '" + aModel.getCodTipoAutoritaAltro() + "' "; 
    } 
    if (aModel.getCodLuogoAltro() != null && aModel.getCodLuogoAltro().length() > 0) { 
      lCondizioni += " and COD_LUOGO_ALTRO = '" + aModel.getCodLuogoAltro() + "' "; 
    } 
    if (aModel.getCodUfficioAltro() != null && aModel.getCodUfficioAltro().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_ALTRO = '" + aModel.getCodUfficioAltro() + "' "; 
    } 
    if (aModel.getSezioneAltro() != null && aModel.getSezioneAltro().length() > 0) { 
      lCondizioni += " and SEZIONE_ALTRO = '" + aModel.getSezioneAltro() + "' "; 
    } 
    if (aModel.getDataEmissioneAltro() != null ) { 
      lCondizioni += " and to_char(DATA_EMISSIONE_ALTRO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataEmissioneAltro(),"dd/MM/yyyy") + "' "; 
    }     
    
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
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
    if (aModel.getIdEventoOrigine() != null ) { 
      lCondizioni += " and ID_EVENTO_ORIGINE = " + aModel.getIdEventoOrigine() + ""; 
    } 
    if (aModel.getEveIdEventoOrigine() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO_ORIGINE = " + aModel.getEveIdEventoOrigine() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdStatoEsecTitoloCumulato) {
    String lCondizioni = new String();

    lCondizioni += " and ID_STATO_ESEC_TITOLO_CUMULATO = " + aIdStatoEsecTitoloCumulato;
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
