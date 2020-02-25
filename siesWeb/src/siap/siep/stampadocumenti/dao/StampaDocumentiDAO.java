package siap.siep.stampadocumenti.dao;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.siep.stampadocumenti.model.StampaDocumentiModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: StampaDocumentiDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella StampaDocumenti</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 1.0
*/
public class StampaDocumentiDAO extends TableDAO { 
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public StampaDocumentiDAO (Connection con) {
    super(con);
    setTable("STAMPA_DOCUMENTI");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_STAMPA","STA_SEQ");

    //setField("ID_STAMPA", BIG_DECIMAL);
    setField("ID_UTENTE"             , STRING);
    setField("DATA"                  , DATE);
    setField("STATO"                 , STRING);
    setField("DESCRIZIONE"           , STRING);
    
    setField("NUM_STAMPE_RICHIESTE"  , INTEGER);
    setField("NUM_STAMPE_EFFETTUATE", INTEGER);
    
    setField("DOC_BLOB", TBLOB);
    
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdStampa()           throws DAOException  { return getBigDecimal ("ID_STAMPA"             ); } 
  public  String      getIdUtente()           throws DAOException  { return getString     ("ID_UTENTE"             ); } 
  public  Date        getData()               throws DAOException  { return getDate       ("DATA"                  ); } 
  public  String      getStato()              throws DAOException  { return getString     ("STATO"                 ); } 
  public  Integer  getNumStampeRichieste()    throws DAOException  { return getInteger ("NUM_STAMPE_RICHIESTE"  ); } 
  public  Integer  getNumeStampeEffettuate()  throws DAOException  { return getInteger ("NUM_STAMPE_EFFETTUATE"); } 
  public  ByteArrayOutputStream   getDocBlob()throws DAOException  { return getBlob("DOC_BLOB"); }
  public  String   getDescrizione()throws DAOException  { return getString("DESCRIZIONE"); }
  

  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdStampa              (BigDecimal  aValore )   { setBigDecimal ("ID_STAMPA"             , aValore); } 
  public void  setIdUtente              (String      aValore )   { setString     ("ID_UTENTE"             , aValore); } 
  public void  setData                  (Date        aValore )   { setDate       ("DATA"                  , aValore); } 
  public void  setStato                 (String      aValore )   { setString     ("STATO"                 , aValore); } 
  public void  setNumStampeRichieste    (Integer  aValore )      { setInteger ("NUM_STAMPE_RICHIESTE"  , aValore); } 
  public void  setNumeStampeEffettuate  (Integer  aValore )      { setInteger ("NUM_STAMPE_EFFETTUATE", aValore); } 
  public void  setDocBlob               (ByteArrayInputStream aValore ){ setBlob ("DOC_BLOB", aValore); }
  public void  setDescrizione               (String aValore ){ setString ("DESCRIZIONE", aValore); }
   

  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new StampaDocumentiModel(  
      getIdStampa() , 
      getIdUtente() , 
      getData() , 
      getStato() , 
      getNumStampeRichieste() , 
      getNumeStampeEffettuate(),
      getDescrizione() 
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(StampaDocumentiModel aModel) throws DAOException {
    setIdStampa              ( aModel.getIdStampa()             );  
    setIdUtente              ( aModel.getIdUtente()             );  
    setData                  ( aModel.getData()                 );  
    setStato                 ( aModel.getStato()                );  
    setNumStampeRichieste    ( aModel.getNumStampeRichieste()   );  
    setNumeStampeEffettuate  ( aModel.getNumeStampeEffettuate() );  
    setDocBlob( aModel.getDocBlobIn());
    setDescrizione( aModel.getDescrizione());
    
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(StampaDocumentiModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdStampa() != null ) { 
      lCondizioni += " and ID_STAMPA = " + aModel.getIdStampa() + ""; 
    } 
    if (aModel.getIdUtente() != null && aModel.getIdUtente().length() > 0) { 
      lCondizioni += " and ID_UTENTE = '" + aModel.getIdUtente() + "' "; 
    } 
    if (aModel.getData() != null ) { 
      lCondizioni += " and to_char(DATA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getData(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getStato() != null && aModel.getStato().length() > 0) { 
      lCondizioni += " and STATO = '" + aModel.getStato() + "' "; 
    } 
    if (aModel.getNumStampeRichieste() != null ) { 
      lCondizioni += " and NUM_STAMPE_RICHIESTE = " + aModel.getNumStampeRichieste() + ""; 
    } 
    if (aModel.getNumeStampeEffettuate() != null ) { 
      lCondizioni += " and NUME_STAMPE_EFFETTUATE = " + aModel.getNumeStampeEffettuate() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdStampa) {
    String lCondizioni = new String();

    lCondizioni += " ID_STAMPA = " + aIdStampa;
    
    setCondition(lCondizioni);
  }

  /**
   * setDAOFromModelForUpdateBlob
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForUpdateBlob(StampaDocumentiModel aModel) throws DAOException
  {
	if(aModel.getDocBlobIn()!=null)
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("Cicciooooooooooooooo = " + aModel.getDocBlobIn().available());  
    
	setDocBlob( aModel.getDocBlobIn());
    
    if(aModel.getData()!=null)
    	setData( aModel.getData() );
    
    setIdStampa(aModel.getIdStampa()             );  
    
    if(aModel.getIdUtente()!= null && aModel.getIdUtente().length()>1)
    	setIdUtente              ( aModel.getIdUtente()             );  
    
    if(aModel.getDescrizione()!= null && aModel.getDescrizione().length()>1)
    	setDescrizione( aModel.getDescrizione()                 );  
    
    if(aModel.getStato()!= null )
    	setStato( aModel.getStato()  );  
    
    if(aModel.getNumStampeRichieste()!= null)
        setNumStampeRichieste    ( aModel.getNumStampeRichieste()   );
 
    if(aModel.getNumeStampeEffettuate()!= null)
    	setNumeStampeEffettuate  ( aModel.getNumeStampeEffettuate() );  
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