package siap.siep.rateizzazionepp.dao;

/**
* <p>Title: RateizzazionePPDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RATEIZZAZIONE_PP</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class RateizzazionePPDAO extends SIAPTableDAO 
{ 
  
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public RateizzazionePPDAO (Connection con) 
  {
    super(con);
    setTable("RATEIZZAZIONE_PP");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_RATEIZZAZIONE_PP","RAT_PEN_SEQ");

    //setField("ID_RATEIZZAZIONE_PP", BIG_DECIMAL);
    
    setField("IMPORTO_DA_PAGARE"            , BIG_DECIMAL);
    setField("IMPORTO_RATA"                 , BIG_DECIMAL);
    setField("NUMERO_RATE"                  , BIG_DECIMAL);
    setField("TIPO_RATEIZZAZIONE"           , STRING);
    setField("SCADENZA_GIORNI"              , BIG_DECIMAL);
    
    setField("FAS_SIE_ID_FASCICOLO_SIEP"    , BIG_DECIMAL);
    setField("EVE_ID_EVENTO"                , BIG_DECIMAL);
    setField("PROGRESSIVO_RATA"             , BIG_DECIMAL);    
    
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
  public  BigDecimal  getIdRateizzazionePP()         throws DAOException  { return getBigDecimal ("ID_RATEIZZAZIONE_PP" ); } 
  public  BigDecimal  getImportoDaPagare()           throws DAOException  { return getBigDecimal ("IMPORTO_DA_PAGARE"  ); } 
  public  BigDecimal  getImportoRata()               throws DAOException  { return getBigDecimal ("IMPORTO_RATA"       ); } 
  public  BigDecimal  getNumeroRate()                throws DAOException  { return getBigDecimal ("NUMERO_RATE"        ); } 
  public  String      getTipoRateizzazione()         throws DAOException  { return getString     ("TIPO_RATEIZZAZIONE" ); } 
  public  BigDecimal  getScadenzaGiorni()            throws DAOException  { return getBigDecimal ("SCADENZA_GIORNI"    ); } 
  public  BigDecimal  getProgressivoRata()           throws DAOException  { return getBigDecimal ("PROGRESSIVO_RATA"    ); } 
  
  public  BigDecimal  getFasSieIdFascicoloSiep()     throws DAOException  { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"    ); } 
  public  BigDecimal  getEveIdEvento()               throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO"                ); } 
  public  String      getCodOperatoreInserimento()   throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"    ); } 
  public  Date        getDataInserimento()           throws DAOException  { return getDate       ("DATA_INSERIMENTO"             ); } 
  public  String      getCodUfficioInserimento()     throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"      ); } 
  public  String      getCodOperatoreAggiornamento() throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"  ); } 
  public  Date        getDataAggiornamento()         throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"           ); } 
  public  String      getCodUfficioAggiornamento()   throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"    ); } 

  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdRateizzazionePP            (BigDecimal  aValore )   { setBigDecimal ("ID_RATEIZZAZIONE_PP" , aValore); } 
  public void  setImportoDaPagare              (BigDecimal  aValore )   { setBigDecimal ("IMPORTO_DA_PAGARE"   , aValore); } 
  public void  setImportoRata                  (BigDecimal  aValore )   { setBigDecimal ("IMPORTO_RATA"       , aValore); } 
  public void  setNumeroRate                   (BigDecimal  aValore )   { setBigDecimal ("NUMERO_RATE"        , aValore); } 
  public void  setTipoRateizzazione            (String      aValore )   { setString     ("TIPO_RATEIZZAZIONE" , aValore); } 
  public void  setScadenzaGiorni               (BigDecimal  aValore )   { setBigDecimal ("SCADENZA_GIORNI"    , aValore); } 
  public void  setProgressivoRata              (BigDecimal  aValore )   { setBigDecimal ("PROGRESSIVO_RATA"    , aValore); } 
  
  public void  setFasSieIdFascicoloSiep        (BigDecimal  aValore )   { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"    , aValore); } 
  public void  setEveIdEvento                  (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO"                , aValore); } 
  
  public void  setCodOperatoreInserimento      (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"    , aValore); } 
  public void  setDataInserimento              (Date        aValore )   { setDate       ("DATA_INSERIMENTO"             , aValore); } 
  public void  setCodUfficioInserimento        (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"      , aValore); } 
  public void  setCodOperatoreAggiornamento    (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO"  , aValore); } 
  public void  setDataAggiornamento            (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"           , aValore); } 
  public void  setCodUfficioAggiornamento      (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"    , aValore); } 
  
  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
   
  public GenericModel getModel() throws DAOException 
  { 
    RateizzazionePPModel lModel = new RateizzazionePPModel();

    lModel.setIdRateizzazionePP (getIdRateizzazionePP());
    lModel.setImportoDaPagare   (getImportoDaPagare());
    lModel.setImportoRata       (getImportoRata());
    lModel.setNumeroRate        (getNumeroRate());
    lModel.setTipoRateizzazione (getTipoRateizzazione());
    lModel.setScadenzaGiorni    (getScadenzaGiorni());
    lModel.setProgressivoRata   (getProgressivoRata());

    lModel.setFasSieIdFascicoloSiep (getFasSieIdFascicoloSiep());
    lModel.setEveIdEvento           (getEveIdEvento());

    lModel.setCodOperatoreInserimento (getCodOperatoreInserimento());
    lModel.setDataInserimento         (getDataInserimento());
    lModel.setCodUfficioInserimento   (getCodUfficioInserimento());
    
    lModel.setCodOperatoreAggiornamento (getCodOperatoreAggiornamento());
    lModel.setDataAggiornamento         (getDataAggiornamento());
    lModel.setCodUfficioAggiornamento   (getCodUfficioAggiornamento());
    
    return lModel; 
  }


  /** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   */ 
  public void setDAOFromModel(RateizzazionePPModel aModel) throws DAOException {
    setIdRateizzazionePP ( aModel.getIdRateizzazionePP());  
    setImportoDaPagare   ( aModel.getImportoDaPagare());
    setImportoRata       ( aModel.getImportoRata()      );  
    setNumeroRate        ( aModel.getNumeroRate()       );  
    setTipoRateizzazione ( aModel.getTipoRateizzazione());  
    setScadenzaGiorni    ( aModel.getScadenzaGiorni()   );  
    setProgressivoRata   ( aModel.getProgressivoRata());
   
    setFasSieIdFascicoloSiep     ( aModel.getFasSieIdFascicoloSiep()     );  
    setEveIdEvento               ( aModel.getEveIdEvento()               );  
    
    setCodOperatoreInserimento   ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento           ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento     ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento         ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento   ( aModel.getCodUfficioAggiornamento()   );

  }

  /** 
   * Metodo che imposta i campi delle operazioni di Aggiornamento x Emissione Ordinanza 
   * @param aModel 
   * @throws DAOException 
   */ 
  public void setDAOFromModelForUpdate(RateizzazionePPModel aModel) throws DAOException {
    if ( aModel.getIdRateizzazionePP() != null  )
      setIdRateizzazionePP( aModel.getIdRateizzazionePP());  
 
    if ( aModel.getImportoDaPagare() != null )
        setImportoDaPagare ( aModel.getImportoDaPagare() ); 
    if ( aModel.getImportoRata() != null )
      setImportoRata ( aModel.getImportoRata() );  
    if ( aModel.getNumeroRate() != null )
      setNumeroRate ( aModel.getNumeroRate());  
    if ( aModel.getTipoRateizzazione() != null  )
      setTipoRateizzazione ( aModel.getTipoRateizzazione() );  
    if ( aModel.getScadenzaGiorni() != null  )
      setScadenzaGiorni ( aModel.getScadenzaGiorni() );  
    if ( aModel.getProgressivoRata() != null  )
        setProgressivoRata ( aModel.getProgressivoRata() );  
    
    if ( aModel.getCodOperatoreAggiornamento() != null  && aModel.getCodOperatoreAggiornamento().length() > 1  )
      setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento()   );  
    if ( aModel.getDataAggiornamento() != null )
      setDataAggiornamento ( aModel.getDataAggiornamento() );  
    if ( aModel.getCodUfficioAggiornamento() != null  && aModel.getCodUfficioAggiornamento().length() > 1  )
      setCodUfficioAggiornamento ( aModel.getCodUfficioAggiornamento() );    
    
    if ( aModel.getEveIdEvento() != null  )
      setEveIdEvento ( aModel.getEveIdEvento() );
  }
  
  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(RateizzazionePPModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdRateizzazionePP() != null ) { 
      lCondizioni += " and ID_RATEIZZAZIONE_PP = " + aModel.getIdRateizzazionePP() + ""; 
    } 
    if (aModel.getImportoDaPagare() != null ) { 
        lCondizioni += " and IMPORTO_DA_PAGARE = " + aModel.getImportoDaPagare() + ""; 
    } 
    if (aModel.getImportoRata() != null ) { 
      lCondizioni += " and IMPORTO_RATA = " + aModel.getImportoRata() + ""; 
    } 
    if (aModel.getNumeroRate() != null ) { 
      lCondizioni += " and NUMERO_RATE = " + aModel.getNumeroRate() + ""; 
    } 
    if (aModel.getTipoRateizzazione() != null && aModel.getTipoRateizzazione().length() > 0) { 
      lCondizioni += " and TIPO_RATEIZZAZIONE = '" + aModel.getTipoRateizzazione() + "' "; 
    }    
    if (aModel.getProgressivoRata() != null) { 
        lCondizioni += " and PROGRESSIVO_RATA = '" + aModel.getProgressivoRata() + "' "; 
    } 

    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
    } 
    
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
    } 

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni); 
  }


  /** 
   * Imposta la condizione di where per l'operazione di update puntuale 
   * si entra sempre in chiave 
   * @param key 
   */ 
  public void selCondizioneUpdate( BigDecimal aIdRateizzazionePP) {
    String lCondizioni = new String();

    lCondizioni += " ID_RATEIZZAZIONE_PP = " + aIdRateizzazionePP;

    setCondition(lCondizioni);
  }

  /** 
   * Imposta la condizione where per FAS_SIE_ID_FASCICOLO_SIEP. 
   * @param aIdFasSius 
   */ 
  public void selCondizioneByIdFasSiep( BigDecimal aIdFasSiep) {
    String lCondizioni = new String();

    lCondizioni += " FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFasSiep;

    setCondition(lCondizioni);
  }

  /** 
   * Imposta la condizione where per FAS_SIU_ID_FASCICOLO_SIUS ed EVE_ID_EVENTO. 
   * @param aIdFasSius 
   * @param aIdEvento 
   */ 
  public void selCondizioneByIdFasSiepIdEvento( BigDecimal aIdFasSiep, BigDecimal aIdEvento) {
    String lCondizioni = new String();

    lCondizioni += " FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFasSiep;
    lCondizioni += " and EVE_ID_EVENTO = " + aIdEvento;

    setCondition(lCondizioni);
  }
  
  /** 
   * Imposta la condizione where per EVE_ID_EVENTO. 
   * @param aIdFasSius 
   */ 
  public void selCondizioneByIdEvento( BigDecimal aIdEvento) {
    String lCondizioni = new String();

    lCondizioni += " EVE_ID_EVENTO = " + aIdEvento;

    setCondition(lCondizioni);
  }
  

  /** 
   * Imposta la condizione di order by per la ricerca  
   *  
   */ 
  public void setOrderBy() 
  { 
    String orderBy = ""; 
    //======================================================================= 
    // Lasciare orderBy="" se non si vuole scegliere un ordinamento, 
    // altrimenti elencare i campi separati da virgola 
    // n.b. non inserire la clausola ORDER BY, viene aggiunta automaticamente 
    //======================================================================= 

    setOrder(orderBy); 
  } 
}
