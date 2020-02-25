package siap.siep.annotazioneesitotrasmissione.dao;

/**
* <p>Title: AnnotazioneEsitoTrasmissioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella AnnotazioneEsitoTrasmissione</p>
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

import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;

import org.apache.log4j.Logger; 

public class AnnotazioneEsitoTrasmissioneDAO extends TableDAO { 
  Logger logger = Logger.getLogger("daoLogger");
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public AnnotazioneEsitoTrasmissioneDAO (Connection con) {
    super(con);
    setTable("ANNOTAZIONE_ESITO_TRASMISSIONE");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_ESITO_TRASMISSIONE","ANNOTA_ESITO_TRASM_SEQ");

    //setField("ID_ESITO_TRASMISSIONE", BIG_DECIMAL);
    setField("DATA_TRASMISSIONE"          , DATE);
    setField("OGGETTO_TRASMISSIONE"       , STRING);
    setField("COD_UFFICIO_DESTINATARIO"   , STRING);
    setField("COD_UFFICIO_INOLTRANTE"     , STRING);
    setField("COD_UFFICIO_INOLTRO"        , STRING);
    setField("COD_UFFICIO_ESITO"          , STRING);
    setField("DATA_ESITO"                 , DATE);
    setField("COD_ESITO"                  , STRING);
    setField("NOTE_ESITO"                 , STRING);
    setField("CHIAVE_ANNO"                , BIG_DECIMAL);
    setField("CHIAVE_PROGR"               , BIG_DECIMAL);
    setField("CHIAVE_UFFICIO"             , STRING);
    setField("EVE_ID_EVENTO"              , BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP"  , BIG_DECIMAL);
    setField("MES_ID_MESSAGGIO_RICHIESTA" , BIG_DECIMAL);
    setField("MES_ID_MESSAGGIO_ESITO"     , BIG_DECIMAL);
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
  public  BigDecimal  getIdEsitoTrasmissione()        throws DAOException  { return getBigDecimal ("ID_ESITO_TRASMISSIONE"      ); } 
  public  Date        getDataTrasmissione()           throws DAOException  { return getDate       ("DATA_TRASMISSIONE"          ); } 
  public  String      getOggettoTrasmissione()        throws DAOException  { return getString     ("OGGETTO_TRASMISSIONE"       ); } 
  public  String      getCodUfficioDestinatario()     throws DAOException  { return getString     ("COD_UFFICIO_DESTINATARIO"   ); } 
  public  String      getCodUfficioInoltrante()       throws DAOException  { return getString     ("COD_UFFICIO_INOLTRANTE"     ); } 
  public  String      getCodUfficioInoltro()          throws DAOException  { return getString     ("COD_UFFICIO_INOLTRO"        ); } 
  public  String      getCodUfficioEsito()            throws DAOException  { return getString     ("COD_UFFICIO_ESITO"          ); } 
  public  Date        getDataEsito()                  throws DAOException  { return getDate       ("DATA_ESITO"                 ); } 
  public  String      getCodEsito()                   throws DAOException  { return getString     ("COD_ESITO"                  ); } 
  public  String      getNoteEsito()                  throws DAOException  { return getString     ("NOTE_ESITO"                 ); } 
  public  BigDecimal  getChiaveAnno()                 throws DAOException  { return getBigDecimal ("CHIAVE_ANNO"                ); } 
  public  BigDecimal  getChiaveProgr()                throws DAOException  { return getBigDecimal ("CHIAVE_PROGR"               ); } 
  public  String      getChiaveUfficio()              throws DAOException  { return getString     ("CHIAVE_UFFICIO"             ); } 
  public  BigDecimal  getEveIdEvento()                throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO"              ); } 
  public  BigDecimal  getFasSieIdFascicoloSiep()      throws DAOException  { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"  ); } 
  public  BigDecimal  getMesIdMessaggioRichiesta()    throws DAOException  { return getBigDecimal ("MES_ID_MESSAGGIO_RICHIESTA" ); } 
  public  BigDecimal  getMesIdMessaggioEsito()        throws DAOException  { return getBigDecimal ("MES_ID_MESSAGGIO_ESITO"     ); } 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdEsitoTrasmissione        (BigDecimal  aValore )   { setBigDecimal ("ID_ESITO_TRASMISSIONE"      , aValore); } 
  public void  setDataTrasmissione           (Date        aValore )   { setDate       ("DATA_TRASMISSIONE"          , aValore); } 
  public void  setOggettoTrasmissione        (String      aValore )   { setString     ("OGGETTO_TRASMISSIONE"       , aValore); } 
  public void  setCodUfficioDestinatario     (String      aValore )   { setString     ("COD_UFFICIO_DESTINATARIO"   , aValore); } 
  public void  setCodUfficioInoltrante       (String      aValore )   { setString     ("COD_UFFICIO_INOLTRANTE"     , aValore); } 
  public void  setCodUfficioInoltro          (String      aValore )   { setString     ("COD_UFFICIO_INOLTRO"        , aValore); } 
  public void  setCodUfficioEsito            (String      aValore )   { setString     ("COD_UFFICIO_ESITO"          , aValore); } 
  public void  setDataEsito                  (Date        aValore )   { setDate       ("DATA_ESITO"                 , aValore); } 
  public void  setCodEsito                   (String      aValore )   { setString     ("COD_ESITO"                  , aValore); } 
  public void  setNoteEsito                  (String      aValore )   { setString     ("NOTE_ESITO"                 , aValore); } 
  public void  setChiaveAnno                 (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_ANNO"                , aValore); } 
  public void  setChiaveProgr                (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_PROGR"               , aValore); } 
  public void  setChiaveUfficio              (String      aValore )   { setString     ("CHIAVE_UFFICIO"             , aValore); } 
  public void  setEveIdEvento                (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO"              , aValore); } 
  public void  setFasSieIdFascicoloSiep      (BigDecimal  aValore )   { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"  , aValore); } 
  public void  setMesIdMessaggioRichiesta    (BigDecimal  aValore )   { setBigDecimal ("MES_ID_MESSAGGIO_RICHIESTA" , aValore); } 
  public void  setMesIdMessaggioEsito        (BigDecimal  aValore )   { setBigDecimal ("MES_ID_MESSAGGIO_ESITO"     , aValore); } 
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
    return new AnnotazioneEsitoTrasmissioneModel (  
      getIdEsitoTrasmissione() , 
      getDataTrasmissione() , 
      getOggettoTrasmissione() , 
 "",
      getCodUfficioDestinatario() , 
 "",
      getCodUfficioInoltrante() , 
 "",
      getCodUfficioInoltro() , 
 "",
 "",
      getCodUfficioEsito() , 
 "",
 "",
      getDataEsito() , 
      getCodEsito() , 
 "",
      getNoteEsito() , 
      getChiaveAnno() , 
      getChiaveProgr() , 
      getChiaveUfficio() , 
 "",
      getEveIdEvento() , 
      getFasSieIdFascicoloSiep() , 
      getMesIdMessaggioRichiesta() , 
      getMesIdMessaggioEsito() , 
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
  public void setDAOFromModel(AnnotazioneEsitoTrasmissioneModel aModel) throws DAOException {
    setIdEsitoTrasmissione        ( aModel.getIdEsitoTrasmissione()       );  
    setDataTrasmissione           ( aModel.getDataTrasmissione()          );  
    setOggettoTrasmissione        ( aModel.getOggettoTrasmissione()       );  
    setCodUfficioDestinatario     ( aModel.getCodUfficioDestinatario()    );  
    setCodUfficioInoltrante       ( aModel.getCodUfficioInoltrante()      );  
    setCodUfficioInoltro          ( aModel.getCodUfficioInoltro()         );  
    setCodUfficioEsito            ( aModel.getCodUfficioEsito()           );  
    setDataEsito                  ( aModel.getDataEsito()                 );  
    setCodEsito                   ( aModel.getCodEsito()                  );  
    setNoteEsito                  ( aModel.getNoteEsito()                 );  
    setChiaveAnno                 ( aModel.getChiaveAnno()                );  
    setChiaveProgr                ( aModel.getChiaveProgr()               );  
    setChiaveUfficio              ( aModel.getChiaveUfficio()             );  
    setEveIdEvento                ( aModel.getEveIdEvento()               );  
    setFasSieIdFascicoloSiep      ( aModel.getFasSieIdFascicoloSiep()     );  
    setMesIdMessaggioRichiesta    ( aModel.getMesIdMessaggioRichiesta()   );  
    setMesIdMessaggioEsito        ( aModel.getMesIdMessaggioEsito()       );  
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(AnnotazioneEsitoTrasmissioneModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdEsitoTrasmissione() != null ) { 
      lCondizioni += " and ID_ESITO_TRASMISSIONE = " + aModel.getIdEsitoTrasmissione() + ""; 
    } 
    if (aModel.getDataTrasmissione() != null ) { 
      lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataTrasmissione(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getOggettoTrasmissione() != null && aModel.getOggettoTrasmissione().length() > 0) { 
      lCondizioni += " and OGGETTO_TRASMISSIONE = '" + aModel.getOggettoTrasmissione() + "' "; 
    } 
    if (aModel.getCodUfficioDestinatario() != null && aModel.getCodUfficioDestinatario().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_DESTINATARIO = '" + aModel.getCodUfficioDestinatario() + "' "; 
    } 
    if (aModel.getCodUfficioInoltrante() != null && aModel.getCodUfficioInoltrante().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_INOLTRANTE = '" + aModel.getCodUfficioInoltrante() + "' "; 
    } 
    if (aModel.getCodUfficioInoltro() != null && aModel.getCodUfficioInoltro().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_INOLTRO = '" + aModel.getCodUfficioInoltro() + "' "; 
    } 
    if (aModel.getCodUfficioEsito() != null && aModel.getCodUfficioEsito().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_ESITO = '" + aModel.getCodUfficioEsito() + "' "; 
    } 
    if (aModel.getDataEsito() != null ) { 
      lCondizioni += " and to_char(DATA_ESITO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataEsito(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodEsito() != null && aModel.getCodEsito().length() > 0) { 
      lCondizioni += " and COD_ESITO = '" + aModel.getCodEsito() + "' "; 
    } 
    if (aModel.getNoteEsito() != null && aModel.getNoteEsito().length() > 0) { 
      lCondizioni += " and NOTE_ESITO = '" + aModel.getNoteEsito() + "' "; 
    } 
    if (aModel.getChiaveAnno() != null ) { 
      lCondizioni += " and CHIAVE_ANNO = " + aModel.getChiaveAnno() + ""; 
    } 
    if (aModel.getChiaveProgr() != null ) { 
      lCondizioni += " and CHIAVE_PROGR = " + aModel.getChiaveProgr() + ""; 
    } 
    if (aModel.getChiaveUfficio() != null && aModel.getChiaveUfficio().length() > 0) { 
      lCondizioni += " and CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "' "; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
    } 
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
    } 
    if (aModel.getMesIdMessaggioRichiesta() != null ) { 
      lCondizioni += " and MES_ID_MESSAGGIO_RICHIESTA = " + aModel.getMesIdMessaggioRichiesta() + ""; 
    } 
    if (aModel.getMesIdMessaggioEsito() != null ) { 
      lCondizioni += " and MES_ID_MESSAGGIO_ESITO = " + aModel.getMesIdMessaggioEsito() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdEsitoTrasmissione) {
    String lCondizioni = new String();

    lCondizioni += " and ID_ESITO_TRASMISSIONE = " + aIdEsitoTrasmissione;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }
  
  public void selCondizioneUpdateByEveIdEvento( BigDecimal aIdEvento) {
    String lCondizioni = new String();

    lCondizioni += " and EVE_ID_EVENTO = " + aIdEvento;
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
