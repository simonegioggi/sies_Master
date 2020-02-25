package siap.sico.provvedimentisiesnsc.dao;

/**
* <p>Title: ProvvSiesNscDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella ProvvSiesNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.sql.Connection;

import siap.sico.provvedimentisiesnsc.model.ProvvSiesNscModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

public class ProvvSiesNscDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public ProvvSiesNscDAO (Connection con) {
    super(con);
    setTable("PROVV_SIES_NSC");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("PROVV_CODCENTR","PROVV_SEQ");

    setField("PROVV_DOMAIN"          , STRING);
    //setField("PROVV_CODCENTR", STRING);
    setField("PROVV_NSC_CAT"         , STRING);
    setField("PROVV_NSC_DES_CAT"     , STRING);
    setField("PROVV_NSC_NAT"         , STRING);
    setField("PROVV_NSC_DES_NAT"     , STRING);
    setField("PROVV_SIES_OGGETTO"    , STRING);
    setField("PROVV_SIES_DES_OGGETTO", STRING);
    setField("PROVV_SIES_MOTIVO"     , STRING);
    setField("PROVV_SIES_DES_MOTIVO" , STRING);
    setField("PROVV_SIES_ESITO"      , STRING);
    setField("PROVV_SIES_DES_ESITO"  , STRING);
    setField("PROVV_VAL1"            , STRING);
    setField("PROVV_VAL2"            , STRING);
    setField("PROVV_VAL3"            , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  String  getProvvDomain()          throws DAOException  { return getString ("PROVV_DOMAIN"          ); } 
  public  String  getProvvCodcentr()        throws DAOException  { return getString ("PROVV_CODCENTR"        ); } 
  public  String  getProvvNscCat()          throws DAOException  { return getString ("PROVV_NSC_CAT"         ); } 
  public  String  getProvvNscDesCat()       throws DAOException  { return getString ("PROVV_NSC_DES_CAT"     ); } 
  public  String  getProvvNscNat()          throws DAOException  { return getString ("PROVV_NSC_NAT"         ); } 
  public  String  getProvvNscDesNat()       throws DAOException  { return getString ("PROVV_NSC_DES_NAT"     ); } 
  public  String  getProvvSiesOggetto()     throws DAOException  { return getString ("PROVV_SIES_OGGETTO"    ); } 
  public  String  getProvvSiesDesOggetto()  throws DAOException  { return getString ("PROVV_SIES_DES_OGGETTO"); } 
  public  String  getProvvSiesMotivo()      throws DAOException  { return getString ("PROVV_SIES_MOTIVO"     ); } 
  public  String  getProvvSiesDesMotivo()   throws DAOException  { return getString ("PROVV_SIES_DES_MOTIVO" ); } 
  public  String  getProvvSiesEsito()       throws DAOException  { return getString ("PROVV_SIES_ESITO"      ); } 
  public  String  getProvvSiesDesEsito()    throws DAOException  { return getString ("PROVV_SIES_DES_ESITO"  ); } 
  public  String  getProvvVal1()            throws DAOException  { return getString ("PROVV_VAL1"            ); } 
  public  String  getProvvVal2()            throws DAOException  { return getString ("PROVV_VAL2"            ); } 
  public  String  getProvvVal3()            throws DAOException  { return getString ("PROVV_VAL3"            ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setProvvDomain          (String  aValore )   { setString ("PROVV_DOMAIN"          , aValore); } 
  public void  setProvvCodcentr        (String  aValore )   { setString ("PROVV_CODCENTR"        , aValore); } 
  public void  setProvvNscCat          (String  aValore )   { setString ("PROVV_NSC_CAT"         , aValore); } 
  public void  setProvvNscDesCat       (String  aValore )   { setString ("PROVV_NSC_DES_CAT"     , aValore); } 
  public void  setProvvNscNat          (String  aValore )   { setString ("PROVV_NSC_NAT"         , aValore); } 
  public void  setProvvNscDesNat       (String  aValore )   { setString ("PROVV_NSC_DES_NAT"     , aValore); } 
  public void  setProvvSiesOggetto     (String  aValore )   { setString ("PROVV_SIES_OGGETTO"    , aValore); } 
  public void  setProvvSiesDesOggetto  (String  aValore )   { setString ("PROVV_SIES_DES_OGGETTO", aValore); } 
  public void  setProvvSiesMotivo      (String  aValore )   { setString ("PROVV_SIES_MOTIVO"     , aValore); } 
  public void  setProvvSiesDesMotivo   (String  aValore )   { setString ("PROVV_SIES_DES_MOTIVO" , aValore); } 
  public void  setProvvSiesEsito       (String  aValore )   { setString ("PROVV_SIES_ESITO"      , aValore); } 
  public void  setProvvSiesDesEsito    (String  aValore )   { setString ("PROVV_SIES_DES_ESITO"  , aValore); } 
  public void  setProvvVal1            (String  aValore )   { setString ("PROVV_VAL1"            , aValore); } 
  public void  setProvvVal2            (String  aValore )   { setString ("PROVV_VAL2"            , aValore); } 
  public void  setProvvVal3            (String  aValore )   { setString ("PROVV_VAL3"            , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new ProvvSiesNscModel(  
      getProvvDomain() , 
      getProvvCodcentr() , 
      getProvvNscCat() , 
      getProvvNscDesCat() , 
      getProvvNscNat() , 
      getProvvNscDesNat() , 
      getProvvSiesOggetto() , 
      getProvvSiesDesOggetto() , 
      getProvvSiesMotivo() , 
      getProvvSiesDesMotivo() , 
      getProvvSiesEsito() , 
      getProvvSiesDesEsito() , 
      getProvvVal1() , 
      getProvvVal2() , 
      getProvvVal3()  
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(ProvvSiesNscModel aModel) throws DAOException {
    setProvvDomain          ( aModel.getProvvDomain()         );  
    setProvvCodcentr        ( aModel.getProvvCodcentr()       );  
    setProvvNscCat          ( aModel.getProvvNscCat()         );  
    setProvvNscDesCat       ( aModel.getProvvNscDesCat()      );  
    setProvvNscNat          ( aModel.getProvvNscNat()         );  
    setProvvNscDesNat       ( aModel.getProvvNscDesNat()      );  
    setProvvSiesOggetto     ( aModel.getProvvSiesOggetto()    );  
    setProvvSiesDesOggetto  ( aModel.getProvvSiesDesOggetto() );  
    setProvvSiesMotivo      ( aModel.getProvvSiesMotivo()     );  
    setProvvSiesDesMotivo   ( aModel.getProvvSiesDesMotivo()  );  
    setProvvSiesEsito       ( aModel.getProvvSiesEsito()      );  
    setProvvSiesDesEsito    ( aModel.getProvvSiesDesEsito()   );  
    setProvvVal1            ( aModel.getProvvVal1()           );  
    setProvvVal2            ( aModel.getProvvVal2()           );  
    setProvvVal3            ( aModel.getProvvVal3()           );  
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(ProvvSiesNscModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getProvvDomain() != null && aModel.getProvvDomain().length() > 0) { 
      lCondizioni += " and PROVV_DOMAIN = '" + aModel.getProvvDomain() + "' "; 
    } 
    if (aModel.getProvvCodcentr() != null && aModel.getProvvCodcentr().length() > 0) { 
      lCondizioni += " and PROVV_CODCENTR = '" + aModel.getProvvCodcentr() + "' "; 
    } 
    if (aModel.getProvvNscCat() != null && aModel.getProvvNscCat().length() > 0) { 
      lCondizioni += " and PROVV_NSC_CAT = '" + aModel.getProvvNscCat() + "' "; 
    } 
    if (aModel.getProvvNscDesCat() != null && aModel.getProvvNscDesCat().length() > 0) { 
      lCondizioni += " and PROVV_NSC_DES_CAT = '" + aModel.getProvvNscDesCat() + "' "; 
    } 
    if (aModel.getProvvNscNat() != null && aModel.getProvvNscNat().length() > 0) { 
      lCondizioni += " and PROVV_NSC_NAT = '" + aModel.getProvvNscNat() + "' "; 
    } 
    if (aModel.getProvvNscDesNat() != null && aModel.getProvvNscDesNat().length() > 0) { 
      lCondizioni += " and PROVV_NSC_DES_NAT = '" + aModel.getProvvNscDesNat() + "' "; 
    } 
    if (aModel.getProvvSiesOggetto() != null && aModel.getProvvSiesOggetto().length() > 0) { 
      lCondizioni += " and PROVV_SIES_OGGETTO = '" + aModel.getProvvSiesOggetto() + "' "; 
    } 
    if (aModel.getProvvSiesDesOggetto() != null && aModel.getProvvSiesDesOggetto().length() > 0) { 
      lCondizioni += " and PROVV_SIES_DES_OGGETTO = '" + aModel.getProvvSiesDesOggetto() + "' "; 
    } 
    if (aModel.getProvvSiesMotivo() != null && aModel.getProvvSiesMotivo().length() > 0) { 
      lCondizioni += " and PROVV_SIES_MOTIVO = '" + aModel.getProvvSiesMotivo() + "' "; 
    } 
    if (aModel.getProvvSiesDesMotivo() != null && aModel.getProvvSiesDesMotivo().length() > 0) { 
      lCondizioni += " and PROVV_SIES_DES_MOTIVO = '" + aModel.getProvvSiesDesMotivo() + "' "; 
    } 
    if (aModel.getProvvSiesEsito() != null && aModel.getProvvSiesEsito().length() > 0) { 
      lCondizioni += " and PROVV_SIES_ESITO = '" + aModel.getProvvSiesEsito() + "' "; 
    } 
    if (aModel.getProvvSiesDesEsito() != null && aModel.getProvvSiesDesEsito().length() > 0) { 
      lCondizioni += " and PROVV_SIES_DES_ESITO = '" + aModel.getProvvSiesDesEsito() + "' "; 
    } 
    if (aModel.getProvvVal1() != null && aModel.getProvvVal1().length() > 0) { 
      lCondizioni += " and PROVV_VAL1 = '" + aModel.getProvvVal1() + "' "; 
    } 
    if (aModel.getProvvVal2() != null && aModel.getProvvVal2().length() > 0) { 
      lCondizioni += " and PROVV_VAL2 = '" + aModel.getProvvVal2() + "' "; 
    } 
    if (aModel.getProvvVal3() != null && aModel.getProvvVal3().length() > 0) { 
      lCondizioni += " and PROVV_VAL3 = '" + aModel.getProvvVal3() + "' "; 
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
  public void selCondizioneUpdate( String aProvvCodcentr) {
    String lCondizioni = new String();

    lCondizioni += " and PROVV_CODCENTR = '" + aProvvCodcentr+"'";
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
