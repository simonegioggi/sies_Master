package siap.siep.certificatostatoesecuzione.dao;

/**
* <p>Title: CertificatoStatoEsecDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella CertificatoStatoEsec</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.certificatostatoesecuzione.model.CertificatoStatoEsecModel;
import f3b.dao.DAOException;
import f3b.dao.TableOracleDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class CertificatoStatoEsecDAO extends TableOracleDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public CertificatoStatoEsecDAO (Connection con) {
    super(con);
    setTable("CERTIFICATO_STATO_ESEC");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_CERTIFICATO_STATO_ESEC","CER_STA_ESE_SEQ");

    //setField("ID_CERTIFICATO_STATO_ESEC", BIG_DECIMAL);
    setField("ANNOTAZIONI"                , STRING);
    setField("FLAG_UPLOAD"                , STRING);
    setField("DOC_BLOB", TBLOB);
    setField("COD_OPERATORE_INSERIMENTO"  , STRING);
    setField("DATA_INSERIMENTO"           , DATE);
    setField("COD_UFFICIO_INSERIMENTO"    , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO"         , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"  , STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP"  , BIG_DECIMAL);    
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdCertificatoStatoEsec()     throws DAOException  { return getBigDecimal ("ID_CERTIFICATO_STATO_ESEC"  ); } 
  public  String      getAnnotazioni()                throws DAOException  { return getString     ("ANNOTAZIONI"                ); } 
  public  String      getFlagUpload()                 throws DAOException  { return getString     ("FLAG_UPLOAD"                ); }
  public  ByteArrayOutputStream    getDocBlob()		  throws DAOException  { return getBlob		  ("DOC_BLOB"					);}
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 
  public  BigDecimal  getFasSieIdFascicoloSiep()      throws DAOException  { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"  ); }
  


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdCertificatoStatoEsec     (BigDecimal  aValore )   { setBigDecimal ("ID_CERTIFICATO_STATO_ESEC"  , aValore); } 
  public void  setAnnotazioni                (String      aValore )   { setString     ("ANNOTAZIONI"                , aValore); } 
  public void  setFlagUpload                 (String      aValore )   { setString     ("FLAG_UPLOAD"                , aValore); }
  public void    setDocBlob( ByteArrayInputStream aValore )      { setBlob("DOC_BLOB", aValore); }
  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"  , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"           , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"    , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"         , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"  , aValore); } 
  public void  setFasSieIdFascicoloSiep      (BigDecimal  aValore )   { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"  , aValore); }
  

  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new CertificatoStatoEsecModel(  
      getIdCertificatoStatoEsec() , 
      getAnnotazioni() , 
      getFlagUpload() ,
      //getDocBlob() ,
      getCodOperatoreInserimento() , 
      getDataInserimento() , 
      getCodUfficioInserimento() , 
 "",
      getCodOperatoreAggiornamento() , 
      getDataAggiornamento() , 
      getCodUfficioAggiornamento() , 
 "",
      getFasSieIdFascicoloSiep()  
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(CertificatoStatoEsecModel aModel) throws DAOException {
    setIdCertificatoStatoEsec     ( aModel.getIdCertificatoStatoEsec()    );  
    setAnnotazioni                ( aModel.getAnnotazioni()               );  
    setFlagUpload                 ( aModel.getFlagUpload()                );
    setDocBlob( aModel.getDocBlobIn() );
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
    setFasSieIdFascicoloSiep      ( aModel.getFasSieIdFascicoloSiep()     );  
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(CertificatoStatoEsecModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdCertificatoStatoEsec() != null ) { 
      lCondizioni += " and ID_CERTIFICATO_STATO_ESEC = " + aModel.getIdCertificatoStatoEsec() + ""; 
    } 
    if (aModel.getAnnotazioni() != null && aModel.getAnnotazioni().length() > 0) { 
      lCondizioni += " and ANNOTAZIONI = '" + aModel.getAnnotazioni() + "' "; 
    } 
    if (aModel.getFlagUpload() != null && aModel.getFlagUpload().length() > 0) { 
      lCondizioni += " and FLAG_UPLOAD = '" + aModel.getFlagUpload() + "' "; 
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
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdCertificatoStatoEsec) {
    String lCondizioni = new String();

    lCondizioni += " and ID_CERTIFICATO_STATO_ESEC = " + aIdCertificatoStatoEsec;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }


  /***************************************************************************** 
   * Imposta la condizione di order by per la ricerca
   *****************************************************************************/ 
  public void setOrderBy() { 
    String orderBy = "DATA_INSERIMENTO"; 
    setOrder(orderBy); 
  } 

}
