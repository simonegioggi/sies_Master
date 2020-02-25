package siap.sico.certificato_omonimi_nsc.dao;

/**
* <p>Title: CertificatoOmonimiNscDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella CertificatoOmonimiNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.certificato_omonimi_nsc.model.CertificatoOmonimiNscModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class CertificatoOmonimiNscDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public CertificatoOmonimiNscDAO (Connection con) {
    super(con);
    setTable("CERTIFICATO_OMONIMI_NSC");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_CERTIFICATO_OMONIMI","CERT_SEQ");

    //setField("ID_CERTIFICATO_OMONIMI", BIG_DECIMAL);
    setField("DATA_INSERIMENTO"      , DATE);
   
    setField("CERTIFICATO", TBLOB);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdCertificatoOmonimi()  throws DAOException  { return getBigDecimal ("ID_CERTIFICATO_OMONIMI"); } 
  public  Date        getDataInserimento()       throws DAOException  { return getDate       ("DATA_INSERIMENTO"      ); } 
  public ByteArrayOutputStream    getDocBlobCertificato()        throws DAOException   { return getBlob("CERTIFICATO"); }

  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdCertificatoOmonimi  (BigDecimal  aValore )   { setBigDecimal ("ID_CERTIFICATO_OMONIMI", aValore); } 
  public void  setDataInserimento       (Date        aValore )   { setDate       ("DATA_INSERIMENTO"      , aValore); } 
  public void setDocBlobCertificato(ByteArrayInputStream aValore )      { setBlob("CERTIFICATO", aValore); }

  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new CertificatoOmonimiNscModel(  
      getIdCertificatoOmonimi() , 
      getDataInserimento()  
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(CertificatoOmonimiNscModel aModel) throws DAOException {
    setIdCertificatoOmonimi  ( aModel.getIdCertificatoOmonimi() );  
    setDataInserimento       ( aModel.getDataInserimento()      );  
  }

  public void setDAOFromModelForUpdateBlob(CertificatoOmonimiNscModel aModel ) throws DAOException
  {
      // Il primo campo serve per consentire il SET del campo BLOB
      setDataInserimento(aModel.getDataInserimento());
      setDocBlobCertificato(aModel.getDocBlobCertificato() );
  }

  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(CertificatoOmonimiNscModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdCertificatoOmonimi() != null ) { 
      lCondizioni += " and ID_CERTIFICATO_OMONIMI = " + aModel.getIdCertificatoOmonimi() + ""; 
    } 
    if (aModel.getDataInserimento() != null ) { 
      lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInserimento(),"dd/MM/yyyy") + "' "; 
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
  public void selCondizioneUpdate( BigDecimal aIdCertificatoOmonimi) {
    String lCondizioni = new String();

    lCondizioni += " and ID_CERTIFICATO_OMONIMI = " + aIdCertificatoOmonimi;
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
  public void selCondizioneDelete( ) {
    String lCondizioni = new String();
   
    lCondizioni += " and DATA_INSERIMENTO < to_date('" + DateUtils.getSysDate("dd/MM/yyyy") + "', 'dd/MM/yyyy') "; 
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
