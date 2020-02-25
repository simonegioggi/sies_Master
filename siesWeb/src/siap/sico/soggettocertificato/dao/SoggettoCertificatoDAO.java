package siap.sico.soggettocertificato.dao;

/**
* <p>Title: SoggettoCertificatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella SoggettoCertificato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class SoggettoCertificatoDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public SoggettoCertificatoDAO (Connection con) {
    super(con);
    setTable("SOGGETTO_CERTIFICATO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_SOGGETTO_CERTIFICATO","SOCE_SEQ");

    //setField("ID_SOGGETTO_CERTIFICATO", BIG_DECIMAL);
    setField("SOG_ID_SOGGETTO"            , BIG_DECIMAL);
    setField("DATA_INSERIMENTO"           , DATE);
    setField("COD_OPERATORE_INSERIMENTO"  , STRING);
    setField("COD_UFFICIO_INSERIMENTO"    , STRING);
    setField("DATA_AGGIORNAMENTO"         , DATE);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("COD_UFFICIO_AGGIORNAMENTO"  , STRING);
    setField("CERTIFICATO", TBLOB);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdSoggettoCertificato()          throws DAOException  { return getBigDecimal ("ID_SOGGETTO_CERTIFICATO"    ); } 
  public  BigDecimal  getSogIdSoggetto()                  throws DAOException  { return getBigDecimal ("SOG_ID_SOGGETTO"            ); } 
  public  Date        getDataInserimento()                throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodOperatoreInserimento()        throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  String      getCodUfficioInserimento()          throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  Date        getDataAggiornamento()              throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodOperatoreAggiornamento()      throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  String      getCodUfficioAggiornamento()        throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 
  public ByteArrayOutputStream    getDocBlobCertificato() throws DAOException   { return getBlob("CERTIFICATO"); }

  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdSoggettoCertificato      (BigDecimal  aValore )           { setBigDecimal ("ID_SOGGETTO_CERTIFICATO"    , aValore); } 
  public void  setSogIdSoggetto              (BigDecimal  aValore )           { setBigDecimal ("SOG_ID_SOGGETTO"            , aValore); } 
  public void  setDataInserimento            (Date        aValore )           { setDate       ("DATA_INSERIMENTO"           , aValore); } 
  public void  setCodOperatoreInserimento    (String      aValore )           { setString     ("COD_OPERATORE_INSERIMENTO"  , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )           { setString     ("COD_UFFICIO_INSERIMENTO"    , aValore); } 
  public void  setDataAggiornamento          (Date        aValore )           { setDate       ("DATA_AGGIORNAMENTO"         , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )           { setString     ("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )           { setString     ("COD_UFFICIO_AGGIORNAMENTO"  , aValore); } 
  public void setDocBlobCertificato          (ByteArrayInputStream aValore )  { setBlob("CERTIFICATO", aValore); }


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new SoggettoCertificatoModel(  
      getIdSoggettoCertificato() , 
      getSogIdSoggetto() , 
      getDataInserimento() , 
      getCodOperatoreInserimento() , 
      getCodUfficioInserimento() , 
      "",
      getDataAggiornamento() , 
      getCodOperatoreAggiornamento() , 
      getCodUfficioAggiornamento(),
      ""
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(SoggettoCertificatoModel aModel) throws DAOException {
    setIdSoggettoCertificato      ( aModel.getIdSoggettoCertificato()     );  
    setSogIdSoggetto              ( aModel.getSogIdSoggetto()             );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  public void setDAOFromModelForUpdateBlob(SoggettoCertificatoModel aModel ) throws DAOException
  {
      // Il primo campo serve per consentire il SET del campo BLOB
      setDataAggiornamento(aModel.getDataAggiornamento());
      setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
      setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
      setDocBlobCertificato(aModel.getDocBlobCertificato() );
  }
  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(SoggettoCertificatoModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdSoggettoCertificato() != null ) { 
      lCondizioni += " and ID_SOGGETTO_CERTIFICATO = " + aModel.getIdSoggettoCertificato() + ""; 
    } 
    if (aModel.getSogIdSoggetto() != null ) { 
      lCondizioni += " and SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto() + ""; 
    } 
    if (aModel.getDataInserimento() != null ) { 
      lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInserimento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) { 
      lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' "; 
    } 
    if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' "; 
    } 
    if (aModel.getDataAggiornamento() != null ) { 
      lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataAggiornamento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodOperatoreAggiornamento() != null && aModel.getCodOperatoreAggiornamento().length() > 0) { 
      lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento() + "' "; 
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
  public void selCondizioneUpdate( BigDecimal aIdSoggettoCertificato) {
    String lCondizioni = new String();

    lCondizioni += " and ID_SOGGETTO_CERTIFICATO = " + aIdSoggettoCertificato;
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
