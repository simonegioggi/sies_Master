package siap.sico.trasmissione.dao;

/**
* <p>Title: TrasmissioniDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Trasmissioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.trasmissione.model.TrasmissioniModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class TrasmissioniDAO extends TableDAO { 
  Logger logger = Logger.getLogger("daoLogger");
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public TrasmissioniDAO (Connection con) {
    super(con);
    setTable("TRASMISSIONI");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_TRASMISSIONE","TRAS_SEQ");

    //setField("ID_TRASMISSIONE", BIG_DECIMAL);
    setField("TIPO_TRASMISSIONE", STRING);
    setField("DATA_TRASMISSIONE", DATE);
    setField("ESITO_TRASMISSIONE", STRING);
    setField("COD_ERRORE"       , STRING);
    setField("TIPO_OPERAZIONE"  , STRING);
    setField("DESTINAZIONE"     , STRING);
    setField("CHIAVE_SIES_SOGG" , BIG_DECIMAL);
    setField("CHIAVE_SIES_FASC" , BIG_DECIMAL);
    setField("CHIAVE_NSC_SOGG"  , BIG_DECIMAL);
    setField("CHIAVE_NSC_PROV"  , BIG_DECIMAL);
    setField("CHIAVE_ANNO"      , BIG_DECIMAL);
    setField("CHIAVE_PROGR"     , BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO"     , STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO"     , STRING);
    
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdTrasmissione()    throws DAOException  { return getBigDecimal ("ID_TRASMISSIONE"  ); } 
  public  String      getTipoTrasmissione()  throws DAOException  { return getString     ("TIPO_TRASMISSIONE"); } 
  public  Date        getDataTrasmissione()  throws DAOException  { return getDate       ("DATA_TRASMISSIONE"); } 
  public  String      getEsitoTrasmissione()  throws DAOException  { return getString     ("ESITO_TRASMISSIONE"); } 
  public  String      getCodErrore()         throws DAOException  { return getString     ("COD_ERRORE"       ); } 
  public  String      getTipoOperazione()    throws DAOException  { return getString     ("TIPO_OPERAZIONE"  ); } 
  public  String      getDestinazione()      throws DAOException  { return getString     ("DESTINAZIONE"     ); } 
  public  BigDecimal  getChiaveSiesSogg()    throws DAOException  { return getBigDecimal ("CHIAVE_SIES_SOGG" ); } 
  public  BigDecimal  getChiaveSiesFasc()    throws DAOException  { return getBigDecimal ("CHIAVE_SIES_FASC" ); } 
  public  BigDecimal  getChiaveNscSogg()     throws DAOException  { return getBigDecimal ("CHIAVE_NSC_SOGG"  ); } 
  public  BigDecimal  getChiaveNscProv()     throws DAOException  { return getBigDecimal ("CHIAVE_NSC_PROV"  ); } 
  public  BigDecimal  getChiaveAnno()        throws DAOException  { return getBigDecimal ("CHIAVE_ANNO"  ); } 
  public  BigDecimal  getChiaveProgr()       throws DAOException  { return getBigDecimal ("CHIAVE_PROGR"  ); } 
  public String getCodOperatoreInserimento() throws DAOException  { return getString ("COD_OPERATORE_INSERIMENTO"  ); }  
  public  Date        getDataInserimento()    throws DAOException  { return getDate       ("DATA_INSERIMENTO"); } 
  public String getCodUfficioInserimento()    throws DAOException  { return getString ("COD_UFFICIO_INSERIMENTO"  ); }  

  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdTrasmissione    (BigDecimal  aValore )   { setBigDecimal ("ID_TRASMISSIONE"  , aValore); } 
  public void  setTipoTrasmissione  (String      aValore )   { setString     ("TIPO_TRASMISSIONE", aValore); } 
  public void  setDataTrasmissione  (Date        aValore )   { setDate       ("DATA_TRASMISSIONE", aValore); } 
  public void  setEsitoTrasmissione  (String      aValore )   { setString     ("ESITO_TRASMISSIONE", aValore); } 
  public void  setCodErrore         (String      aValore )   { setString     ("COD_ERRORE"       , aValore); } 
  public void  setTipoOperazione    (String      aValore )   { setString     ("TIPO_OPERAZIONE"  , aValore); } 
  public void  setDestinazione      (String      aValore )   { setString     ("DESTINAZIONE"     , aValore); } 
  public void  setChiaveSiesSogg    (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_SIES_SOGG" , aValore); } 
  public void  setChiaveSiesFasc    (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_SIES_FASC" , aValore); } 
  public void  setChiaveNscSogg     (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_NSC_SOGG"  , aValore); } 
  public void  setChiaveNscProv     (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_NSC_PROV"  , aValore); } 
  public void  setChiaveAnno        (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_ANNO"  , aValore); } 
  public void  setChiaveProgr       (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_PROGR"  , aValore); } 
  public void  setCodOperatoreInserimento (String  aValore )   { setString ("COD_OPERATORE_INSERIMENTO"  , aValore); } 
  public void  setDataInserimento  (Date        aValore )   { setDate       ("DATA_INSERIMENTO", aValore); } 
  public void  setCodUfficioInserimento (String  aValore )   { setString ("COD_UFFICIO_INSERIMENTO"  , aValore); } 

  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new TrasmissioniModel(  
      getIdTrasmissione() , 
      getTipoTrasmissione() , 
      getDataTrasmissione() , 
      getEsitoTrasmissione(),
      getCodErrore() , 
 "",
      getTipoOperazione() , 
      getDestinazione() , 
      getChiaveSiesSogg() , 
      getChiaveSiesFasc() , 
      getChiaveNscSogg() , 
      getChiaveNscProv(),
      getChiaveAnno(),
      getChiaveProgr(),
      getCodOperatoreInserimento(),
      getDataInserimento() ,
      getCodUfficioInserimento()
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(TrasmissioniModel aModel) throws DAOException {
    setIdTrasmissione    ( aModel.getIdTrasmissione()   );  
    setTipoTrasmissione  ( aModel.getTipoTrasmissione() );  
    setDataTrasmissione  ( aModel.getDataTrasmissione() ); 
    setEsitoTrasmissione  ( aModel.getEsitoTrasmissione() );  
    setCodErrore         ( aModel.getCodErrore()        );  
    setTipoOperazione    ( aModel.getTipoOperazione()   );  
    setDestinazione      ( aModel.getDestinazione()     );  
    setChiaveSiesSogg    ( aModel.getChiaveSiesSogg()   );  
    setChiaveSiesFasc    ( aModel.getChiaveSiesFasc()   );  
    setChiaveNscSogg     ( aModel.getChiaveNscSogg()    );  
    setChiaveNscProv     ( aModel.getChiaveNscProv()    );  
    setChiaveAnno        ( aModel.getChiaveAnno()       );  
    setChiaveProgr        (aModel.getChiaveProgr()      );
    setCodOperatoreInserimento (aModel.getCodOperatoreInserimento());
    setDataInserimento (aModel.getDataInserimento());
    setCodUfficioInserimento (aModel.getCodUfficioInserimento());
    
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(TrasmissioniModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdTrasmissione() != null ) { 
      lCondizioni += " and ID_TRASMISSIONE = " + aModel.getIdTrasmissione() + ""; 
    } 
    if (aModel.getTipoTrasmissione() != null && aModel.getTipoTrasmissione().length() > 0) { 
      lCondizioni += " and TIPO_TRASMISSIONE = '" + aModel.getTipoTrasmissione() + "' "; 
    } 
    if (aModel.getDataTrasmissione() != null ) { 
      lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataTrasmissione(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getEsitoTrasmissione() != null && aModel.getEsitoTrasmissione().length() > 0) { 
      lCondizioni += " and ESITO_TRASMISSIONE = '" + aModel.getEsitoTrasmissione() + "' "; 
    } 
    if (aModel.getCodErrore() != null && aModel.getCodErrore().length() > 0) { 
      lCondizioni += " and COD_ERRORE = '" + aModel.getCodErrore() + "' "; 
    } 
    if (aModel.getTipoOperazione() != null && aModel.getTipoOperazione().length() > 0) { 
      lCondizioni += " and TIPO_OPERAZIONE = '" + aModel.getTipoOperazione() + "' "; 
    } 
    if (aModel.getDestinazione() != null && aModel.getDestinazione().length() > 0) { 
      lCondizioni += " and DESTINAZIONE = '" + aModel.getDestinazione() + "' "; 
    } 
    if (aModel.getChiaveSiesSogg() != null ) { 
      lCondizioni += " and CHIAVE_SIES_SOGG = " + aModel.getChiaveSiesSogg() + ""; 
    } 
    if (aModel.getChiaveSiesFasc() != null ) { 
      lCondizioni += " and CHIAVE_SIES_FASC = " + aModel.getChiaveSiesFasc() + ""; 
    } 
    if (aModel.getChiaveNscSogg() != null ) { 
      lCondizioni += " and CHIAVE_NSC_SOGG = " + aModel.getChiaveNscSogg() + ""; 
    } 
    if (aModel.getChiaveNscProv() != null ) { 
      lCondizioni += " and CHIAVE_NSC_PROV = " + aModel.getChiaveNscProv() + ""; 
    } 
    if (aModel.getChiaveAnno() != null ) { 
      lCondizioni += " and CHIAVE_ANNO = " + aModel.getChiaveAnno() + ""; 
    } 
    if (aModel.getChiaveProgr() != null ) { 
      lCondizioni += " and CHIAVE_PROGR = " + aModel.getChiaveProgr() + ""; 
    } 
    
    if (aModel.getCodOperatoreInserimento() != null ) { 
      lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "'"; 
    } 
    
    if (aModel.getDataInserimento() != null ) { 
      lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInserimento(),"dd/MM/yyyy") + "' "; 
    } 

    if (aModel.getCodUfficioInserimento() != null ) { 
      lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'"; 
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
  public void selCondizioneUpdate( BigDecimal aIdTrasmissione) {
    String lCondizioni = new String();

    lCondizioni += " and ID_TRASMISSIONE = " + aIdTrasmissione;
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
