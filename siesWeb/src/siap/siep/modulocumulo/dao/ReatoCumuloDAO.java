package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: ReatoCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Reato_CUMULO</p>
* @version 4.0
*/

public class ReatoCumuloDAO extends SIAPTableDAO
{
  public ReatoCumuloDAO (Connection con)
  {
    super(con);
    setTable("REATO_CUMULO");

    setSequenceField("ID_REATO_CUM", "REA_CUM_SEQ");

    setFieldKey("ID_REATO_CUM", BIG_DECIMAL);

    setField("ID_REATO_CUM", BIG_DECIMAL);
    setField("COD_TIPO_REATO", STRING);
    setField("DATA_REATO", DATE);
    setField("PROGR_NUMERO_MANUALE", STRING);
    setField("PROGR_REATO", BIG_DECIMAL);
    setField("PROGR_CIRCOSTANZA", BIG_DECIMAL);
    setField("DATA_INIZIO", DATE);
    setField("ANNO_INIZIO", BIG_DECIMAL);
    setField("MESE_INIZIO", BIG_DECIMAL);
    setField("GIORNO_INIZIO", BIG_DECIMAL);
    setField("DATA_FINE", DATE);
    setField("ANNO_FINE", BIG_DECIMAL);
    setField("MESE_FINE", BIG_DECIMAL);
    setField("GIORNO_FINE", BIG_DECIMAL);
    setField("COD_PERIODO_CONSUMAZIONE", STRING);
    setField("DESC_LUOGO", STRING);
    setField("COD_FONTE", STRING);
    setField("ANNO_FONTE", BIG_DECIMAL);
    setField("NUMERO_FONTE", STRING);
    setField("COD_SOTTONUMERAZIONE", STRING);
    setField("COMMA", STRING);
    setField("COMMA_QUALIFICANTE", STRING);
    setField("LETTERA", STRING);
    setField("NUMERO", STRING);
    setField("ARTICOLO", STRING);
    setField("COD_TIPO_PENA_DETENTIVA", STRING);
    setField("NUM_ANNI", BIG_DECIMAL);
    setField("NUM_MESI", BIG_DECIMAL);
    setField("NUM_GIORNI", BIG_DECIMAL);
    setField("COD_TIPO_SANZIONE", STRING);
    setField("SANZIONE_PECUNIARIA", BIG_DECIMAL);
    setField("FLAG_ERGASTOLO", STRING);
    setField("ANNI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("MESI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("GIORNI_ISOLAMENTO_DIURNO", BIG_DECIMAL);

    setField("ID_CONTINUAZIONE_REATO_CUM", BIG_DECIMAL);
    setField("TIPO_CONTINUAZIONE_REATO", STRING);
    setField("NOTE", STRING);
    setField("KEY_REATO_NSC", BIG_DECIMAL);
    
    setField("FLAG_STATO", STRING);
    setField("TIT_ID_TITOLO_CUMULATO", BIG_DECIMAL);    
    setField("ID_REATO_ORIGINE", BIG_DECIMAL);
    setField("MOTIVO_MODIFICA", STRING);

    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);    

  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdReatoCum()                 throws DAOException  { return getBigDecimal ("ID_REATO_CUM"               ); } 
  public  String      getCodTipoReato()               throws DAOException  { return getString     ("COD_TIPO_REATO"             ); } 
  public  Date        getDataReato()                  throws DAOException  { return getDate       ("DATA_REATO"                 ); } 
  public  String      getProgrNumeroManuale()         throws DAOException  { return getString     ("PROGR_NUMERO_MANUALE"       ); } 
  public  BigDecimal  getProgrReato()                 throws DAOException  { return getBigDecimal ("PROGR_REATO"                ); } 
  public  BigDecimal  getProgrCircostanza()           throws DAOException  { return getBigDecimal ("PROGR_CIRCOSTANZA"          ); } 
  public  Date        getDataInizio()                 throws DAOException  { return getDate       ("DATA_INIZIO"                ); } 
  public  BigDecimal  getAnnoInizio()                 throws DAOException  { return getBigDecimal ("ANNO_INIZIO"                ); } 
  public  BigDecimal  getMeseInizio()                 throws DAOException  { return getBigDecimal ("MESE_INIZIO"                ); } 
  public  BigDecimal  getGiornoInizio()               throws DAOException  { return getBigDecimal ("GIORNO_INIZIO"              ); } 
  public  Date        getDataFine()                   throws DAOException  { return getDate       ("DATA_FINE"                  ); } 
  public  BigDecimal  getAnnoFine()                   throws DAOException  { return getBigDecimal ("ANNO_FINE"                  ); } 
  public  BigDecimal  getMeseFine()                   throws DAOException  { return getBigDecimal ("MESE_FINE"                  ); } 
  public  BigDecimal  getGiornoFine()                 throws DAOException  { return getBigDecimal ("GIORNO_FINE"                ); } 
  public  String      getCodPeriodoConsumazione()     throws DAOException  { return getString     ("COD_PERIODO_CONSUMAZIONE"   ); } 
  public  String      getDescLuogo()                  throws DAOException  { return getString     ("DESC_LUOGO"                 ); } 
  public  String      getCodFonte()                   throws DAOException  { return getString     ("COD_FONTE"                  ); } 
  public  BigDecimal  getAnnoFonte()                  throws DAOException  { return getBigDecimal ("ANNO_FONTE"                 ); } 
  public  String      getNumeroFonte()                throws DAOException  { return getString     ("NUMERO_FONTE"               ); } 
  public  String      getCodSottonumerazione()        throws DAOException  { return getString     ("COD_SOTTONUMERAZIONE"       ); } 
  public  String      getComma()                      throws DAOException  { return getString     ("COMMA"                      ); } 
  public  String      getCommaQualificante()          throws DAOException  { return getString     ("COMMA_QUALIFICANTE"         ); } 
  public  String      getLettera()                    throws DAOException  { return getString     ("LETTERA"                    ); } 
  public  String      getNumero()                     throws DAOException  { return getString     ("NUMERO"                     ); } 
  public  String      getArticolo()                   throws DAOException  { return getString     ("ARTICOLO"                   ); } 
  public  String      getCodTipoPenaDetentiva()       throws DAOException  { return getString     ("COD_TIPO_PENA_DETENTIVA"    ); } 
  public  BigDecimal  getNumAnni()                    throws DAOException  { return getBigDecimal ("NUM_ANNI"                   ); } 
  public  BigDecimal  getNumMesi()                    throws DAOException  { return getBigDecimal ("NUM_MESI"                   ); } 
  public  BigDecimal  getNumGiorni()                  throws DAOException  { return getBigDecimal ("NUM_GIORNI"                 ); } 
  public  String      getCodTipoSanzione()            throws DAOException  { return getString     ("COD_TIPO_SANZIONE"          ); } 
  public  BigDecimal  getSanzionePecuniaria()         throws DAOException  { return getBigDecimal ("SANZIONE_PECUNIARIA"        ); } 
  public  String      getFlagErgastolo()              throws DAOException  { return getString     ("FLAG_ERGASTOLO"             ); } 
  public  BigDecimal  getGiorniIsolamentoDiurno()     throws DAOException  { return getBigDecimal ("GIORNI_ISOLAMENTO_DIURNO"   ); } 
  public  BigDecimal  getMesiIsolamentoDiurno()       throws DAOException  { return getBigDecimal ("MESI_ISOLAMENTO_DIURNO"     ); } 
  public  BigDecimal  getAnniIsolamentoDiurno()       throws DAOException  { return getBigDecimal ("ANNI_ISOLAMENTO_DIURNO"     ); } 
  public  String      getNote()                       throws DAOException  { return getString     ("NOTE"                       ); } 
  public  BigDecimal  getIdContinuazioneReatoCum()    throws DAOException  { return getBigDecimal ("ID_CONTINUAZIONE_REATO_CUM" ); } 
  public  String      getTipoContinuazioneReato()     throws DAOException  { return getString     ("TIPO_CONTINUAZIONE_REATO"   ); } 
  public  BigDecimal  getKeyReatoNsc()                throws DAOException  { return getBigDecimal ("KEY_REATO_NSC"              ); } 
  public  String      getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                 ); } 
  public  String      getMotivoModifica()             throws DAOException  { return getString     ("MOTIVO_MODIFICA"            ); } 
  public  BigDecimal  getTitIdTitoloCumulato()        throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"     ); } 
  public  BigDecimal  getIdReatoOrigine()             throws DAOException  { return getBigDecimal ("ID_REATO_ORIGINE"           ); } 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); }   

  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdReatoCum                 (BigDecimal  aValore )   { setBigDecimal ("ID_REATO_CUM"               , aValore); } 
  public void  setCodTipoReato               (String      aValore )   { setString     ("COD_TIPO_REATO"             , aValore); } 
  public void  setDataReato                  (Date        aValore )   { setDate       ("DATA_REATO"                 , aValore); } 
  public void  setProgrNumeroManuale         (String      aValore )   { setString     ("PROGR_NUMERO_MANUALE"       , aValore); } 
  public void  setProgrReato                 (BigDecimal  aValore )   { setBigDecimal ("PROGR_REATO"                , aValore); } 
  public void  setProgrCircostanza           (BigDecimal  aValore )   { setBigDecimal ("PROGR_CIRCOSTANZA"          , aValore); } 
  public void  setDataInizio                 (Date        aValore )   { setDate       ("DATA_INIZIO"                , aValore); } 
  public void  setAnnoInizio                 (BigDecimal  aValore )   { setBigDecimal ("ANNO_INIZIO"                , aValore); } 
  public void  setMeseInizio                 (BigDecimal  aValore )   { setBigDecimal ("MESE_INIZIO"                , aValore); } 
  public void  setGiornoInizio               (BigDecimal  aValore )   { setBigDecimal ("GIORNO_INIZIO"              , aValore); } 
  public void  setDataFine                   (Date        aValore )   { setDate       ("DATA_FINE"                  , aValore); } 
  public void  setAnnoFine                   (BigDecimal  aValore )   { setBigDecimal ("ANNO_FINE"                  , aValore); } 
  public void  setMeseFine                   (BigDecimal  aValore )   { setBigDecimal ("MESE_FINE"                  , aValore); } 
  public void  setGiornoFine                 (BigDecimal  aValore )   { setBigDecimal ("GIORNO_FINE"                , aValore); } 
  public void  setCodPeriodoConsumazione     (String      aValore )   { setString     ("COD_PERIODO_CONSUMAZIONE"   , aValore); } 
  public void  setDescLuogo                  (String      aValore )   { setString     ("DESC_LUOGO"                 , aValore); } 
  public void  setCodFonte                   (String      aValore )   { setString     ("COD_FONTE"                  , aValore); } 
  public void  setAnnoFonte                  (BigDecimal  aValore )   { setBigDecimal ("ANNO_FONTE"                 , aValore); } 
  public void  setNumeroFonte                (String      aValore )   { setString     ("NUMERO_FONTE"               , aValore); } 
  public void  setCodSottonumerazione        (String      aValore )   { setString     ("COD_SOTTONUMERAZIONE"       , aValore); } 
  public void  setComma                      (String      aValore )   { setString     ("COMMA"                      , aValore); } 
  public void  setCommaQualificante          (String      aValore )   { setString     ("COMMA_QUALIFICANTE"         , aValore); } 
  public void  setLettera                    (String      aValore )   { setString     ("LETTERA"                    , aValore); } 
  public void  setNumero                     (String      aValore )   { setString     ("NUMERO"                     , aValore); } 
  public void  setArticolo                   (String      aValore )   { setString     ("ARTICOLO"                   , aValore); } 
  public void  setCodTipoPenaDetentiva       (String      aValore )   { setString     ("COD_TIPO_PENA_DETENTIVA"    , aValore); } 
  public void  setNumAnni                    (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI"                   , aValore); } 
  public void  setNumMesi                    (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI"                   , aValore); } 
  public void  setNumGiorni                  (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI"                 , aValore); } 
  public void  setCodTipoSanzione            (String      aValore )   { setString     ("COD_TIPO_SANZIONE"          , aValore); } 
  public void  setSanzionePecuniaria         (BigDecimal  aValore )   { setBigDecimal ("SANZIONE_PECUNIARIA"        , aValore); } 
  public void  setFlagErgastolo              (String      aValore )   { setString     ("FLAG_ERGASTOLO"             , aValore); } 
  public void  setGiorniIsolamentoDiurno     (BigDecimal  aValore )   { setBigDecimal ("GIORNI_ISOLAMENTO_DIURNO"   , aValore); } 
  public void  setMesiIsolamentoDiurno       (BigDecimal  aValore )   { setBigDecimal ("MESI_ISOLAMENTO_DIURNO"     , aValore); } 
  public void  setAnniIsolamentoDiurno       (BigDecimal  aValore )   { setBigDecimal ("ANNI_ISOLAMENTO_DIURNO"     , aValore); } 
  public void  setNote                       (String      aValore )   { setString     ("NOTE"                       , aValore); } 
  public void  setIdContinuazioneReatoCum    (BigDecimal  aValore )   { setBigDecimal ("ID_CONTINUAZIONE_REATO_CUM" , aValore); } 
  public void  setTipoContinuazioneReato     (String      aValore )   { setString     ("TIPO_CONTINUAZIONE_REATO"   , aValore); } 
  public void  setKeyReatoNsc                (BigDecimal  aValore )   { setBigDecimal ("KEY_REATO_NSC"              , aValore); } 
  public void  setFlagStato                  (String      aValore )   { setString     ("FLAG_STATO"                 , aValore); } 
  public void  setMotivoModifica             (String      aValore )   { setString     ("MOTIVO_MODIFICA"            , aValore); } 
  public void  setTitIdTitoloCumulato        (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"     , aValore); } 
  public void  setIdReatoOrigine             (BigDecimal  aValore )   { setBigDecimal ("ID_REATO_ORIGINE"           , aValore); } 
  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"  , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"           , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"    , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"         , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"  , aValore); } 
  
  
  public GenericModel getModel() throws DAOException
  {
    return new ReatoCumuloModel(
                           getIdReatoCum() ,
                           getCodTipoReato() ,  "",
                           getDataReato() ,
                           getProgrNumeroManuale() ,
                           getProgrReato() ,
                           getProgrCircostanza() ,
                           getDataInizio() ,
                           getAnnoInizio() ,
                           getMeseInizio() ,
                           getGiornoInizio() ,
                           getDataFine() ,
                           getAnnoFine() ,
                           getMeseFine() ,
                           getGiornoFine() ,
                           getCodPeriodoConsumazione() , "",
                           getDescLuogo() ,
                           
                           getCodFonte() , "",
                           getAnnoFonte() ,
                           getNumeroFonte() ,
                           getCodSottonumerazione() , "",
                           getComma() ,
                           getCommaQualificante(), "", 
                           getLettera() ,
                           getNumero() ,
                           getArticolo() ,
                           
                           getCodTipoPenaDetentiva() , "",
                           getNumAnni() ,
                           getNumMesi() ,
                           getNumGiorni() ,
                           
                           getCodTipoSanzione(), "",
                           getSanzionePecuniaria() ,
                           getFlagErgastolo() ,                           
                           getAnniIsolamentoDiurno(),
                           getMesiIsolamentoDiurno(),
                           getGiorniIsolamentoDiurno(),
                           
                           getNote(),
                           getKeyReatoNsc(),
                           getIdContinuazioneReatoCum(),
                           getTipoContinuazioneReato(),                           
                           
                           getFlagStato(),
                           getMotivoModifica(),
                           getTitIdTitoloCumulato(),                           
                           getIdReatoOrigine(),
                           
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
  public void setDAOFromModel(ReatoCumuloModel aModel) throws DAOException {
    setIdReatoCum                 ( aModel.getIdReatoCum()                );  
    setCodTipoReato               ( aModel.getCodTipoReato()              );  
    setDataReato                  ( aModel.getDataReato()                 );  
    setProgrNumeroManuale         ( aModel.getProgrNumeroManuale()        );  
    setProgrReato                 ( aModel.getProgrReato()                );  
    setProgrCircostanza           ( aModel.getProgrCircostanza()          );  
    setDataInizio                 ( aModel.getDataInizio()                );  
    setAnnoInizio                 ( aModel.getAnnoInizio()                );  
    setMeseInizio                 ( aModel.getMeseInizio()                );  
    setGiornoInizio               ( aModel.getGiornoInizio()              );  
    setDataFine                   ( aModel.getDataFine()                  );  
    setAnnoFine                   ( aModel.getAnnoFine()                  );  
    setMeseFine                   ( aModel.getMeseFine()                  );  
    setGiornoFine                 ( aModel.getGiornoFine()                );  
    setCodPeriodoConsumazione     ( aModel.getCodPeriodoConsumazione()    );  
    setDescLuogo                  ( aModel.getDescLuogo()                 );  
    setCodFonte                   ( aModel.getCodFonte()                  );  
    setAnnoFonte                  ( aModel.getAnnoFonte()                 );  
    setNumeroFonte                ( aModel.getNumeroFonte()               );  
    setCodSottonumerazione        ( aModel.getCodSottonumerazione()       );  
    setComma                      ( aModel.getComma()                     );  
    setCommaQualificante          ( aModel.getCommaQualificante()         );  
    setLettera                    ( aModel.getLettera()                   );  
    setNumero                     ( aModel.getNumero()                    );  
    setArticolo                   ( aModel.getArticolo()                  );  
    setCodTipoPenaDetentiva       ( aModel.getCodTipoPenaDetentiva()      );  
    setNumAnni                    ( aModel.getNumAnni()                   );  
    setNumMesi                    ( aModel.getNumMesi()                   );  
    setNumGiorni                  ( aModel.getNumGiorni()                 );  
    setCodTipoSanzione            ( aModel.getCodTipoSanzione()           );  
    setSanzionePecuniaria         ( aModel.getSanzionePecuniaria()        );  
    setFlagErgastolo              ( aModel.getFlagErgastolo()             );  
    setGiorniIsolamentoDiurno     ( aModel.getNumGiorniIsolamentoDiurno() );  
    setMesiIsolamentoDiurno       ( aModel.getNumMesiIsolamentoDiurno()   );  
    setAnniIsolamentoDiurno       ( aModel.getNumAnniIsolamentoDiurno()   );  
    setNote                       ( aModel.getNote()                      );  
    setIdContinuazioneReatoCum    ( aModel.getIdContinuazioneReatoCum()   );  
    setTipoContinuazioneReato     ( aModel.getTipoContinuazioneReato()    );  
    setKeyReatoNsc                ( aModel.getKeyReatoNsc()               );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModificaNote()        );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setIdReatoOrigine             ( aModel.getIdReatoOrigine()            );  
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  /**
   * Metodo generico di aggiornamento dei dati del Reato. 
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForUpdate(ReatoCumuloModel aModel) throws DAOException
  {
//    setIdReatoCum                 ( aModel.getIdReatoCum()                );  
    setCodTipoReato               ( aModel.getCodTipoReato()              );  
    setDataReato                  ( aModel.getDataReato()                 );  //???
    setProgrNumeroManuale         ( aModel.getProgrNumeroManuale()        );  
//    setProgrReato                 ( aModel.getProgrReato()                );  
//    setProgrCircostanza           ( aModel.getProgrCircostanza()          );  
    setDataInizio                 ( aModel.getDataInizio()                );  
    setAnnoInizio                 ( aModel.getAnnoInizio()                );  
    setMeseInizio                 ( aModel.getMeseInizio()                );  
    setGiornoInizio               ( aModel.getGiornoInizio()              );  
    setDataFine                   ( aModel.getDataFine()                  );  
    setAnnoFine                   ( aModel.getAnnoFine()                  );  
    setMeseFine                   ( aModel.getMeseFine()                  );  
    setGiornoFine                 ( aModel.getGiornoFine()                );  
    setCodPeriodoConsumazione     ( aModel.getCodPeriodoConsumazione()    );  
   
    setDescLuogo                  ( aModel.getDescLuogo()                 );  
    setCodFonte                   ( aModel.getCodFonte()                  );  
    setAnnoFonte                  ( aModel.getAnnoFonte()                 );  
    setNumeroFonte                ( aModel.getNumeroFonte()               );  
    setCodSottonumerazione        ( aModel.getCodSottonumerazione()       );  
    setComma                      ( aModel.getComma()                     );  
    setCommaQualificante          ( aModel.getCommaQualificante()         );  
    setLettera                    ( aModel.getLettera()                   );  
    setNumero                     ( aModel.getNumero()                    );  
    setArticolo                   ( aModel.getArticolo()                  );  
    //FIXME da verificare perchè la pena non è modificabile
//    setCodTipoPenaDetentiva       ( aModel.getCodTipoPenaDetentiva()      );  
//    setNumAnni                    ( aModel.getNumAnni()                   );  
//    setNumMesi                    ( aModel.getNumMesi()                   );  
//    setNumGiorni                  ( aModel.getNumGiorni()                 );  
//    setCodTipoSanzione            ( aModel.getCodTipoSanzione()           );  
//    setSanzionePecuniaria         ( aModel.getSanzionePecuniaria()        );  
//    setFlagErgastolo              ( aModel.getFlagErgastolo()             );  
//    setGiorniIsolamentoDiurno     ( aModel.getNumGiorniIsolamentoDiurno() );  
//    setMesiIsolamentoDiurno       ( aModel.getNumMesiIsolamentoDiurno()   );  
//    setAnniIsolamentoDiurno       ( aModel.getNumAnniIsolamentoDiurno()   );  
    setNote                       ( aModel.getNote()                      );  
//    setIdContinuazioneReatoCum    ( aModel.getIdContinuazioneReatoCum()   );  
//    setTipoContinuazioneReato     ( aModel.getTipoContinuazioneReato()    );  
//    setKeyReatoNsc                ( aModel.getKeyReatoNsc()               );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModificaNote()        );  
//    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
//    setIdReatoOrigine             ( aModel.getIdReatoOrigine()            );  
//    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
//    setDataInserimento            ( aModel.getDataInserimento()           );  
//    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  

    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
    
    setCondizioneUpdate(aModel.getIdReatoCum());
  }

  /**
   * Nel caso di Modifica del record principale, vanno aggiornati anche i campi
   * comuni presenti sui record con PROGR_CIRCOSTANZA>1
   * - Tipo reato, luogo, periodo consumazione, note, prog manuale
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForUpdateUlterioriNorme(ReatoCumuloModel aModel) throws DAOException
  {
    setProgrNumeroManuale ( aModel.getProgrNumeroManuale() );
    
    setCodTipoReato ( aModel.getCodTipoReato() );
    setDescLuogo( aModel.getDescLuogo() );
    setCodPeriodoConsumazione( aModel.getCodPeriodoConsumazione() );
    setDataInizio( aModel.getDataInizio() );
    setAnnoInizio( aModel.getAnnoInizio() );
    setMeseInizio( aModel.getMeseInizio() );
    setGiornoInizio( aModel.getGiornoInizio() );
    setDataFine( aModel.getDataFine() );
    setAnnoFine( aModel.getAnnoFine() );
    setMeseFine( aModel.getMeseFine() );
    setGiornoFine( aModel.getGiornoFine() );

    // n.b. per ora anche i dati della NOTE, STATO, MOTIVO MODIFICA vengono
    //      considerati comunu a tutti i record dello stesso reato e quindi
    //      tenuti allineati
    setNote           ( aModel.getNote() ); // le note Reato sono in comune
    setFlagStato      ( aModel.getFlagStato() );
    setMotivoModifica ( aModel.getMotivoModificaNote() );

    setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento         ( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento   ( aModel.getCodUfficioAggiornamento() );

    setCondizioneUpdateUlterioriNorme (aModel.getProgrReato(), aModel.getTitIdTitoloCumulato());
  }

  /**
   * Effettua l'aggiornamento dei soli campi relativi agli estremi della norma.
   * Nel caso di aggiornamento di record con PROG_CIRCOSTANZA >1
   * 
   * @param aModel
   * @throws DAOException
   *
   */
  public void setDAOFromModelForUpdateNonPrimaNorma(ReatoCumuloModel aModel) throws DAOException
  {
    setCodFonte            ( aModel.getCodFonte() );
    setAnnoFonte           ( aModel.getAnnoFonte() );
    setNumeroFonte         ( aModel.getNumeroFonte() );
    setCodSottonumerazione ( aModel.getCodSottonumerazione() );
    setComma               ( aModel.getComma() );
    setCommaQualificante   ( aModel.getCommaQualificante() );
    setLettera             ( aModel.getLettera() );
    setNumero              ( aModel.getNumero() );
    setArticolo            ( aModel.getArticolo() );

    setFlagStato      ( aModel.getFlagStato() );
    setMotivoModifica ( aModel.getMotivoModificaNote() );
    
    setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento         ( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento   ( aModel.getCodUfficioAggiornamento() );
    
    setCondizioneUpdate(aModel.getIdReatoCum());
  }
  

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition (" ID_REATO_CUM="+ key);
  }  


  /**
   * Imposta le condizioni di update dei record ulteriori norme (PROGR_CIRCOSTANZA>1)
   * per il reato e titolo indicato
   * @param aProgReato
   * @param aIdTitolo
   */
  public void setCondizioneUpdateUlterioriNorme (BigDecimal aProgReato, BigDecimal aIdTitolo)
  {
    String lCondition = " PROGR_REATO="+ aProgReato+" AND TIT_ID_TITOLO_CUMULATO = "+aIdTitolo+" AND PROGR_CIRCOSTANZA>1";
    setCondition (lCondition);
  }

  /**
   * Imposta la condizione di update per tutti i record del Reato identificato da
   * PROGR_REATO e titolo TIT_ID_TITOLO_CUMULATO
   * @param aProgReato
   * @param aIdTitolo
   */
  public void setCondizioneUpdateReatoECircostanze (BigDecimal aProgReato, BigDecimal aIdTitolo)
  {
    setCondition (" PROGR_REATO="+ aProgReato+" AND TIT_ID_TITOLO_CUMULATO="+aIdTitolo);
  }
    
  /**
   * Imposta le condizioni per IdTitolo ed eventualmente PROGR_REATO recuperando i dati dal model
   * @param aModel
   */
  public void setCondizione_Continuazioni(ReatoCumuloModel aModel)
  {
    String lCondizioni = "";
    
    lCondizioni += " TIT_ID_TITOLO_CUMULATO = "+aModel.getTitIdTitoloCumulato();
    
    if (aModel.getProgrReato() != null) {        
      lCondizioni += " AND PROGR_REATO = "+aModel.getProgrReato();
    }
    
    setCondition (lCondizioni);
  }

  /**
   * Condizione di Update per ID_CONTINUAZIONE_REATO_CUM e TIT_ID_TITOLO_CUMULATO
   * @param aModel
   */
  public void setCondizione_CancellaContinuazione(ReatoCumuloModel aModel)
  {
    String lCondizioni = "";
    
    lCondizioni = " ID_CONTINUAZIONE_REATO_CUM = (select A.ID_CONTINUAZIONE_REATO_CUM " +
    	                                          	" from REATO_CUMULO A " +
    	                                           " where ";    
    lCondizioni += "     TIT_ID_TITOLO_CUMULATO = "+aModel.getTitIdTitoloCumulato();    
    lCondizioni += " AND PROGR_REATO = "+aModel.getProgrReato();
    lCondizioni += " AND PROGR_CIRCOSTANZA = "+aModel.getProgrCircostanza();
    lCondizioni += ")";
    
    lCondizioni += " AND TIT_ID_TITOLO_CUMULATO = "+aModel.getTitIdTitoloCumulato();
    
    
    setCondition (lCondizioni);
  }
   
}
