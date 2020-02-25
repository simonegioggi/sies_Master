package siap.siep.sollecitoesitotrasmissione.dao;

/**
* <p>Title: SollecitoEsitoTrasmissioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella SollecitoEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class SollecitoEsitoTrasmissioneDAO extends TableDAO { 
  Logger logger = Logger.getLogger("daoLogger");
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public SollecitoEsitoTrasmissioneDAO (Connection con) {
    super(con);
    setTable("SOLLECITO_ESITO_TRASMISSIONE");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_SOLLECITO","SOLL_ESITO_TRASM_SEQ");

    //setField("ID_SOLLECITO", BIG_DECIMAL);
    setField("COD_UFF_SOLLECITATO"         , STRING);
    setField("OGGETTO_MS_SOLLECITO"        , STRING);
    setField("OGGETTO_MS_SOLLECITATO"      , STRING);
    setField("DATA_INVIO_MS_SOLLECITATO"   , DATE);
    setField("MES_ID_MESSAGGIO_SOLLECITATO", BIG_DECIMAL);
    setField("COD_UFF_INOLTRANTE"          , STRING);
    setField("DATA_INOLTRO"                , DATE);
    setField("MES_ID_MESSAGGIO_INOLTRO"    , BIG_DECIMAL);
    setField("EVE_ID_EVENTO"               , BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP"   , BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO"   , STRING);
    setField("DATA_INSERIMENTO"            , DATE);
    setField("COD_UFFICIO_INSERIMENTO"     , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO" , STRING);
    setField("DATA_AGGIORNAMENTO"          , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"   , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdSollecito()                throws DAOException  { return getBigDecimal ("ID_SOLLECITO"                ); } 
  public  String      getCodUffSollecitato()          throws DAOException  { return getString     ("COD_UFF_SOLLECITATO"         ); } 
  public  String      getOggettoMsSollecito()         throws DAOException  { return getString     ("OGGETTO_MS_SOLLECITO"        ); } 
  public  String      getOggettoMsSollecitato()       throws DAOException  { return getString     ("OGGETTO_MS_SOLLECITATO"      ); } 
  public  Date        getDataInvioMsSollecitato()     throws DAOException  { return getDate       ("DATA_INVIO_MS_SOLLECITATO"   ); } 
  public  BigDecimal  getMesIdMessaggioSollecitato()  throws DAOException  { return getBigDecimal ("MES_ID_MESSAGGIO_SOLLECITATO"); } 
  public  String      getCodUffInoltrante()           throws DAOException  { return getString     ("COD_UFF_INOLTRANTE"          ); } 
  public  Date        getDataInoltro()                throws DAOException  { return getDate       ("DATA_INOLTRO"                ); } 
  public  BigDecimal  getMesIdMessaggioInoltro()      throws DAOException  { return getBigDecimal ("MES_ID_MESSAGGIO_INOLTRO"    ); } 
  public  BigDecimal  getEveIdEvento()                throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO"               ); } 
  public  BigDecimal  getFasSieIdFascicoloSiep()      throws DAOException  { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"   ); } 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"   ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"            ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"     ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO" ); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"          ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"   ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdSollecito                (BigDecimal  aValore )   { setBigDecimal ("ID_SOLLECITO"                , aValore); } 
  public void  setCodUffSollecitato          (String      aValore )   { setString     ("COD_UFF_SOLLECITATO"         , aValore); } 
  public void  setOggettoMsSollecito         (String      aValore )   { setString     ("OGGETTO_MS_SOLLECITO"        , aValore); } 
  public void  setOggettoMsSollecitato       (String      aValore )   { setString     ("OGGETTO_MS_SOLLECITATO"      , aValore); } 
  public void  setDataInvioMsSollecitato     (Date        aValore )   { setDate       ("DATA_INVIO_MS_SOLLECITATO"   , aValore); } 
  public void  setMesIdMessaggioSollecitato  (BigDecimal  aValore )   { setBigDecimal ("MES_ID_MESSAGGIO_SOLLECITATO", aValore); } 
  public void  setCodUffInoltrante           (String      aValore )   { setString     ("COD_UFF_INOLTRANTE"          , aValore); } 
  public void  setDataInoltro                (Date        aValore )   { setDate       ("DATA_INOLTRO"                , aValore); } 
  public void  setMesIdMessaggioInoltro      (BigDecimal  aValore )   { setBigDecimal ("MES_ID_MESSAGGIO_INOLTRO"    , aValore); } 
  public void  setEveIdEvento                (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO"               , aValore); } 
  public void  setFasSieIdFascicoloSiep      (BigDecimal  aValore )   { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"   , aValore); } 
  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"   , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"            , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"     , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO" , aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"          , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"   , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new SollecitoEsitoTrasmissioneModel(  
      getIdSollecito() , 
      getCodUffSollecitato() , 
 "",
      getOggettoMsSollecito() , 
      getOggettoMsSollecitato() , 
      getDataInvioMsSollecitato() , 
      getMesIdMessaggioSollecitato() , 
      getCodUffInoltrante() , 
 "",
      getDataInoltro() , 
      getMesIdMessaggioInoltro() , 
      getEveIdEvento() , 
      getFasSieIdFascicoloSiep() , 
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


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(SollecitoEsitoTrasmissioneModel aModel) throws DAOException {
    setIdSollecito                ( aModel.getIdSollecito()               );  
    setCodUffSollecitato          ( aModel.getCodUffSollecitato()         );  
    setOggettoMsSollecito         ( aModel.getOggettoMsSollecito()        );  
    setOggettoMsSollecitato       ( aModel.getOggettoMsSollecitato()      );  
    setDataInvioMsSollecitato     ( aModel.getDataInvioMsSollecitato()    );  
    setMesIdMessaggioSollecitato  ( aModel.getMesIdMessaggioSollecitato() );  
    setCodUffInoltrante           ( aModel.getCodUffInoltrante()          );  
    setDataInoltro                ( aModel.getDataInoltro()               );  
    setMesIdMessaggioInoltro      ( aModel.getMesIdMessaggioInoltro()     );  
    setEveIdEvento                ( aModel.getEveIdEvento()               );  
    setFasSieIdFascicoloSiep      ( aModel.getFasSieIdFascicoloSiep()     );  
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
  public void setCondizioni(SollecitoEsitoTrasmissioneModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdSollecito() != null ) { 
      lCondizioni += " and ID_SOLLECITO = " + aModel.getIdSollecito() + ""; 
    } 
    if (aModel.getCodUffSollecitato() != null && aModel.getCodUffSollecitato().length() > 0) { 
      lCondizioni += " and COD_UFF_SOLLECITATO = '" + aModel.getCodUffSollecitato() + "' "; 
    } 
    if (aModel.getOggettoMsSollecito() != null && aModel.getOggettoMsSollecito().length() > 0) { 
      lCondizioni += " and OGGETTO_MS_SOLLECITO = '" + aModel.getOggettoMsSollecito() + "' "; 
    } 
    if (aModel.getOggettoMsSollecitato() != null && aModel.getOggettoMsSollecitato().length() > 0) { 
      lCondizioni += " and OGGETTO_MS_SOLLECITATO = '" + aModel.getOggettoMsSollecitato() + "' "; 
    } 
    if (aModel.getDataInvioMsSollecitato() != null ) { 
      lCondizioni += " and to_char(DATA_INVIO_MS_SOLLECITATO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInvioMsSollecitato(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getMesIdMessaggioSollecitato() != null ) { 
      lCondizioni += " and MES_ID_MESSAGGIO_SOLLECITATO = " + aModel.getMesIdMessaggioSollecitato() + ""; 
    } 
    if (aModel.getCodUffInoltrante() != null && aModel.getCodUffInoltrante().length() > 0) { 
      lCondizioni += " and COD_UFF_INOLTRANTE = '" + aModel.getCodUffInoltrante() + "' "; 
    } 
    if (aModel.getDataInoltro() != null ) { 
      lCondizioni += " and to_char(DATA_INOLTRO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInoltro(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getMesIdMessaggioInoltro() != null ) { 
      lCondizioni += " and MES_ID_MESSAGGIO_INOLTRO = " + aModel.getMesIdMessaggioInoltro() + ""; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
    } 
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdSollecito) {
    String lCondizioni = new String();

    lCondizioni += " and ID_SOLLECITO = " + aIdSollecito;
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
