package siap.siep.modulocumulo.dao;

/**
* <p>Title: NotificaCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella NotificaCumulo</p>
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

import siap.siep.modulocumulo.model.NotificaCumuloModel;

public class NotificaCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public NotificaCumuloDAO (Connection con) {
    super(con);
    setTable("NOTIFICA_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_NOTIFICA_CUMULO","NOTIFICA_CUMULO_SEQ");

    //setField("ID_NOTIFICA_CUMULO", BIG_DECIMAL);
    setField("COD_TIPO_NOTIFICA"             , STRING);
    setField("DATA_AVVENUTA_NOTIFICA"        , DATE);
    setField("DATA_INVIO"                    , DATE);
    setField("COD_ESITO"                     , STRING);
    setField("NOTE"                          , STRING);
    
    setField("STAT_ID_STATO_ESEC_TIT_CUM"    , BIG_DECIMAL);
    setField("AUT_EST_ID_AUTORITA_ESTERNA"   , BIG_DECIMAL);
    setField("SOG_ID_SOGGETTO"               , BIG_DECIMAL);
    setField("AVV_ID_AVVOCATO_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("UFF_COD_UFFICIO"               , STRING);
    setField("CSS_ID_CSSA"                   , BIG_DECIMAL);
    setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
    
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
  public  BigDecimal  getIdNotificaCumulo()              throws DAOException  { return getBigDecimal ("ID_NOTIFICA_CUMULO"            ); } 
  public  String      getCodTipoNotifica()               throws DAOException  { return getString     ("COD_TIPO_NOTIFICA"             ); } 
  public  Date        getDataAvvenutaNotifica()          throws DAOException  { return getDate       ("DATA_AVVENUTA_NOTIFICA"        ); } 
  public  Date        getDataInvio()                     throws DAOException  { return getDate       ("DATA_INVIO"                    ); } 
  public  String      getCodEsito()                      throws DAOException  { return getString     ("COD_ESITO"                     ); } 
  public  String      getNote()                          throws DAOException  { return getString     ("NOTE"                          ); } 
  
  public  BigDecimal  getStatIdStatoEsecTitCum()         throws DAOException  { return getBigDecimal ("STAT_ID_STATO_ESEC_TIT_CUM"    ); } 
  public  BigDecimal  getAutEstIdAutoritaEsterna()       throws DAOException  { return getBigDecimal ("AUT_EST_ID_AUTORITA_ESTERNA"   ); } 
  public  BigDecimal  getSogIdSoggetto()                 throws DAOException  { return getBigDecimal ("SOG_ID_SOGGETTO"               ); } 
  public  BigDecimal  getAvvIdAvvocatoFascicoloSiep()    throws DAOException  { return getBigDecimal ("AVV_ID_AVVOCATO_FASCICOLO_SIEP"); } 
  public  String      getUffCodUfficio()                 throws DAOException  { return getString     ("UFF_COD_UFFICIO"               ); } 
  public  BigDecimal  getCssIdCssa()                     throws DAOException  { return getBigDecimal ("CSS_ID_CSSA"                   ); } 
  public  String      getIstDetIdIstitutoDetenzione()    throws DAOException  { return getString     ("IST_DET_ID_ISTITUTO_DETENZIONE"); } 
  
  public  String      getCodOperatoreInserimento()       throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"     ); } 
  public  Date        getDataInserimento()               throws DAOException  { return getDate       ("DATA_INSERIMENTO"              ); } 
  public  String      getCodUfficioInserimento()         throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"       ); } 
  public  String      getCodOperatoreAggiornamento()     throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"   ); } 
  public  Date        getDataAggiornamento()             throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"            ); } 
  public  String      getCodUfficioAggiornamento()       throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"     ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdNotificaCumulo              (BigDecimal  aValore )   { setBigDecimal ("ID_NOTIFICA_CUMULO"            , aValore); } 
  public void  setCodTipoNotifica               (String      aValore )   { setString     ("COD_TIPO_NOTIFICA"             , aValore); } 
  public void  setDataAvvenutaNotifica          (Date        aValore )   { setDate       ("DATA_AVVENUTA_NOTIFICA"        , aValore); } 
  public void  setDataInvio                     (Date        aValore )   { setDate       ("DATA_INVIO"                    , aValore); } 
  public void  setCodEsito                      (String      aValore )   { setString     ("COD_ESITO"                     , aValore); } 
  public void  setNote                          (String      aValore )   { setString     ("NOTE"                          , aValore); } 
  
  public void  setStatIdStatoEsecTitCum         (BigDecimal  aValore )   { setBigDecimal ("STAT_ID_STATO_ESEC_TIT_CUM"    , aValore); } 
  public void  setAutEstIdAutoritaEsterna       (BigDecimal  aValore )   { setBigDecimal ("AUT_EST_ID_AUTORITA_ESTERNA"   , aValore); } 
  public void  setSogIdSoggetto                 (BigDecimal  aValore )   { setBigDecimal ("SOG_ID_SOGGETTO"               , aValore); } 
  public void  setAvvIdAvvocatoFascicoloSiep    (BigDecimal  aValore )   { setBigDecimal ("AVV_ID_AVVOCATO_FASCICOLO_SIEP", aValore); } 
  public void  setUffCodUfficio                 (String      aValore )   { setString     ("UFF_COD_UFFICIO"               , aValore); } 
  public void  setCssIdCssa                     (BigDecimal  aValore )   { setBigDecimal ("CSS_ID_CSSA"                   , aValore); } 
  public void  setIstDetIdIstitutoDetenzione    (String      aValore )   { setString     ("IST_DET_ID_ISTITUTO_DETENZIONE", aValore); } 
  
  public void  setCodOperatoreInserimento       (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"     , aValore); } 
  public void  setDataInserimento               (Date        aValore )   { setDate       ("DATA_INSERIMENTO"              , aValore); } 
  public void  setCodUfficioInserimento         (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"       , aValore); } 
  public void  setCodOperatoreAggiornamento     (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO"   , aValore); } 
  public void  setDataAggiornamento             (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"            , aValore); } 
  public void  setCodUfficioAggiornamento       (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"     , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new NotificaCumuloModel(  
      getIdNotificaCumulo() , 
      getCodTipoNotifica() , 
 "",
      getDataAvvenutaNotifica() , 
      getDataInvio() , 
      getCodEsito() , 
 "",
      getNote() , 
      getStatIdStatoEsecTitCum() , 
      getAutEstIdAutoritaEsterna() , 
      getSogIdSoggetto() , 
      getAvvIdAvvocatoFascicoloSiep() , 
      getUffCodUfficio() , 
      getCssIdCssa() , 
      getIstDetIdIstitutoDetenzione() , 
      
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
  public void setDAOFromModel(NotificaCumuloModel aModel) throws DAOException {
    setIdNotificaCumulo              ( aModel.getIdNotificaCumulo()             );  
    setCodTipoNotifica               ( aModel.getCodTipoNotifica()              );  
    setDataAvvenutaNotifica          ( aModel.getDataAvvenutaNotifica()         );  
    setDataInvio                     ( aModel.getDataInvio()                    );  
    setCodEsito                      ( aModel.getCodEsito()                     );  
    setNote                          ( aModel.getNote()                         );  
    
    setStatIdStatoEsecTitCum         ( aModel.getStatIdStatoEsecTitCum()        );  
    setAutEstIdAutoritaEsterna       ( aModel.getAutEstIdAutoritaEsterna()      );  
    setSogIdSoggetto                 ( aModel.getSogIdSoggetto()                );  
    setAvvIdAvvocatoFascicoloSiep    ( aModel.getAvvIdAvvocatoFascicoloSiep()   );  
    setUffCodUfficio                 ( aModel.getUffCodUfficio()                );  
    setCssIdCssa                     ( aModel.getCssIdCssa()                    );  
    setIstDetIdIstitutoDetenzione    ( aModel.getIstDetIdIstitutoDetenzione()   );  
    
    setCodOperatoreInserimento       ( aModel.getCodOperatoreInserimento()      );  
    setDataInserimento               ( aModel.getDataInserimento()              );  
    setCodUfficioInserimento         ( aModel.getCodUfficioInserimento()        );  
    setCodOperatoreAggiornamento     ( aModel.getCodOperatoreAggiornamento()    );  
    setDataAggiornamento             ( aModel.getDataAggiornamento()            );  
    setCodUfficioAggiornamento       ( aModel.getCodUfficioAggiornamento()      );  
  }
  
  public void setDAOFromModelForUpdate (NotificaCumuloModel aModel) throws DAOException {
    //setIdNotificaCumulo              ( aModel.getIdNotificaCumulo()             );  
    setCodTipoNotifica               ( aModel.getCodTipoNotifica()              );  
    setDataAvvenutaNotifica          ( aModel.getDataAvvenutaNotifica()         );  
    setDataInvio                     ( aModel.getDataInvio()                    );  
    setCodEsito                      ( aModel.getCodEsito()                     );  
    setNote                          ( aModel.getNote()                         );  
    
    setStatIdStatoEsecTitCum         ( aModel.getStatIdStatoEsecTitCum()        );  
    setAutEstIdAutoritaEsterna       ( aModel.getAutEstIdAutoritaEsterna()      );  
    setSogIdSoggetto                 ( aModel.getSogIdSoggetto()                );  
    setAvvIdAvvocatoFascicoloSiep    ( aModel.getAvvIdAvvocatoFascicoloSiep()   );  
    setUffCodUfficio                 ( aModel.getUffCodUfficio()                );  
    setCssIdCssa                     ( aModel.getCssIdCssa()                    );  
    setIstDetIdIstitutoDetenzione    ( aModel.getIstDetIdIstitutoDetenzione()   );  
    
//    setCodOperatoreInserimento       ( aModel.getCodOperatoreInserimento()      );  
//    setDataInserimento               ( aModel.getDataInserimento()              );  
//    setCodUfficioInserimento         ( aModel.getCodUfficioInserimento()        );  
    setCodOperatoreAggiornamento     ( aModel.getCodOperatoreAggiornamento()    );  
    setDataAggiornamento             ( aModel.getDataAggiornamento()            );  
    setCodUfficioAggiornamento       ( aModel.getCodUfficioAggiornamento()      );  
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(NotificaCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdNotificaCumulo() != null ) { 
      lCondizioni += " and ID_NOTIFICA_CUMULO = " + aModel.getIdNotificaCumulo() + ""; 
    } 
    if (aModel.getCodTipoNotifica() != null && aModel.getCodTipoNotifica().length() > 0) { 
      lCondizioni += " and COD_TIPO_NOTIFICA = '" + aModel.getCodTipoNotifica() + "' "; 
    } 
    if (aModel.getDataAvvenutaNotifica() != null ) { 
      lCondizioni += " and to_char(DATA_AVVENUTA_NOTIFICA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataAvvenutaNotifica(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataInvio() != null ) { 
      lCondizioni += " and to_char(DATA_INVIO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInvio(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodEsito() != null && aModel.getCodEsito().length() > 0) { 
      lCondizioni += " and COD_ESITO = '" + aModel.getCodEsito() + "' "; 
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    if (aModel.getStatIdStatoEsecTitCum() != null ) { 
      lCondizioni += " and STAT_ID_STATO_ESEC_TIT_CUM = " + aModel.getStatIdStatoEsecTitCum() + ""; 
    } 
    if (aModel.getAutEstIdAutoritaEsterna() != null ) { 
      lCondizioni += " and AUT_EST_ID_AUTORITA_ESTERNA = " + aModel.getAutEstIdAutoritaEsterna() + ""; 
    } 
    if (aModel.getSogIdSoggetto() != null ) { 
      lCondizioni += " and SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto() + ""; 
    } 
    if (aModel.getAvvIdAvvocatoFascicoloSiep() != null ) { 
      lCondizioni += " and AVV_ID_AVVOCATO_FASCICOLO_SIEP = " + aModel.getAvvIdAvvocatoFascicoloSiep() + ""; 
    } 
    if (aModel.getUffCodUfficio() != null && aModel.getUffCodUfficio().length() > 0) { 
      lCondizioni += " and UFF_COD_UFFICIO = '" + aModel.getUffCodUfficio() + "' "; 
    } 
    if (aModel.getCssIdCssa() != null ) { 
      lCondizioni += " and CSS_ID_CSSA = " + aModel.getCssIdCssa() + ""; 
    } 
    if (aModel.getIstDetIdIstitutoDetenzione() != null && aModel.getIstDetIdIstitutoDetenzione().length() > 0) { 
      lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione() + "' "; 
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
  public void selCondizioneUpdate( BigDecimal aIdNotificaCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and ID_NOTIFICA_CUMULO = " + aIdNotificaCumulo;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }
  
  public void selCondizioneUpdateByIdStatEsec( BigDecimal aIdStatoEsecuzione) {
    String lCondizioni = new String();

    lCondizioni += " and STAT_ID_STATO_ESEC_TIT_CUM = " + aIdStatoEsecuzione;
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
