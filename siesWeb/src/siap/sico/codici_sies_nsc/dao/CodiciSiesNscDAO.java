package siap.sico.codici_sies_nsc.dao;

/**
* <p>Title: CodiciSiesNscDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella CodiciSiesNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

public class CodiciSiesNscDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public CodiciSiesNscDAO (Connection con) {
    super(con);
    setTable("CODICI_SIES_NSC");

    setField("CO_DOMAIN"  , STRING);
    setField("CO_CODCENTR", STRING);
    setField("CO_NSC"     , STRING);
    setField("CO_NSC_DES" , STRING);
    setField("CO_SIES"    , STRING);
    setField("CO_SIES_DES", STRING);
    setField("CO_VAL1"    , STRING);
    setField("CO_VAL2"    , STRING);
    setField("CO_VAL3"    , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  String      getCoDomain()    throws DAOException  { return getString     ("CO_DOMAIN"  ); } 
  public  String      getCoCodcentr()  throws DAOException  { return getString     ("CO_CODCENTR"); } 
  public  String      getCoNsc()       throws DAOException  { return getString     ("CO_NSC"     ); } 
  public  String      getCoNscDes()    throws DAOException  { return getString     ("CO_NSC_DES" ); } 
  public  String      getCoSies()      throws DAOException  { return getString     ("CO_SIES"    ); } 
  public  String      getCoSiesDes()   throws DAOException  { return getString     ("CO_SIES_DES"); } 
  public  String      getCoVal1()      throws DAOException  { return getString     ("CO_VAL1"    ); } 
  public  String      getCoVal2()      throws DAOException  { return getString     ("CO_VAL2"    ); } 
  public  String      getCoVal3()      throws DAOException  { return getString     ("CO_VAL3"    ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setCoDomain    (String      aValore )   { setString     ("CO_DOMAIN"  , aValore); } 
  public void  setCoCodcentr  (String      aValore)   { setString      ("CO_CODCENTR", aValore); } 
  public void  setCoNsc       (String      aValore )   { setString     ("CO_NSC"     , aValore); } 
  public void  setCoNscDes    (String      aValore )   { setString     ("CO_NSC_DES" , aValore); } 
  public void  setCoSies      (String      aValore )   { setString     ("CO_SIES"    , aValore); } 
  public void  setCoSiesDes   (String      aValore )   { setString     ("CO_SIES_DES", aValore); } 
  public void  setCoVal1      (String      aValore )   { setString     ("CO_VAL1"    , aValore); } 
  public void  setCoVal2      (String      aValore )   { setString     ("CO_VAL2"    , aValore); } 
  public void  setCoVal3      (String      aValore )   { setString     ("CO_VAL3"    , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new CodiciSiesNscModel(  
      getCoDomain() , 
      getCoCodcentr() , 
      getCoNsc() , 
      getCoNscDes() , 
      getCoSies() , 
      getCoSiesDes() , 
      getCoVal1() , 
      getCoVal2() , 
      getCoVal3()  
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(CodiciSiesNscModel aModel) throws DAOException {
    setCoDomain    ( aModel.getCoDomain()   );  
    setCoCodcentr  ( aModel.getCoCodcentr() );  
    setCoNsc       ( aModel.getCoNsc()      );  
    setCoNscDes    ( aModel.getCoNscDes()   );  
    setCoSies      ( aModel.getCoSies()     );  
    setCoSiesDes   ( aModel.getCoSiesDes()  );  
    setCoVal1      ( aModel.getCoVal1()     );  
    setCoVal2      ( aModel.getCoVal2()     );  
    setCoVal3      ( aModel.getCoVal3()     );  
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(CodiciSiesNscModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getCoDomain() != null && aModel.getCoDomain().length() > 0) { 
      lCondizioni += " and CO_DOMAIN = '" + aModel.getCoDomain() + "' "; 
    } 
    if (aModel.getCoCodcentr() != null && aModel.getCoCodcentr().length() > 0) { 
      lCondizioni += " and CO_CODCENTR = '" + aModel.getCoCodcentr() + "' "; 
    } 
    if (aModel.getCoNsc() != null && aModel.getCoNsc().length() > 0) { 
      lCondizioni += " and CO_NSC = '" + aModel.getCoNsc() + "' "; 
    } 
    if (aModel.getCoNscDes() != null && aModel.getCoNscDes().length() > 0) { 
      lCondizioni += " and CO_NSC_DES = '" + aModel.getCoNscDes() + "' "; 
    } 
    if (aModel.getCoSies() != null && aModel.getCoSies().length() > 0) { 
      lCondizioni += " and CO_SIES = '" + aModel.getCoSies() + "' "; 
    } 
    if (aModel.getCoSiesDes() != null && aModel.getCoSiesDes().length() > 0) { 
      lCondizioni += " and CO_SIES_DES = '" + aModel.getCoSiesDes() + "' "; 
    } 
    if (aModel.getCoVal1() != null && aModel.getCoVal1().length() > 0) { 
      lCondizioni += " and CO_VAL1 = '" + aModel.getCoVal1() + "' "; 
    } 
    if (aModel.getCoVal2() != null && aModel.getCoVal2().length() > 0) { 
      lCondizioni += " and CO_VAL2 = '" + aModel.getCoVal2() + "' "; 
    } 
    if (aModel.getCoVal3() != null && aModel.getCoVal3().length() > 0) { 
      lCondizioni += " and CO_VAL3 = '" + aModel.getCoVal3() + "' "; 
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
  public void selCondizioneUpdate( BigDecimal aCoCodcentr) {
    String lCondizioni = new String();

    lCondizioni += " and CO_CODCENTR = '" + aCoCodcentr + "' ";
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
