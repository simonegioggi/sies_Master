package siap.sico.helponline.dao;

/**
* <p>Title: HelponlineDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Helponline</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.helponline.model.HelponlineModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

public class HelponlineDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public HelponlineDAO (Connection con) {
    super(con);
    setTable("HELPONLINE");

      
    setField("FUN_ID_FUNZIONE", BIG_DECIMAL);
    setField("NOME_PAGINA"    , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  
  public  BigDecimal  getFunIdFunzione()  throws DAOException  { return getBigDecimal ("FUN_ID_FUNZIONE"); } 
  public  String      getNomePagina()     throws DAOException  { return getString     ("NOME_PAGINA"    ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  
  public void  setFunIdFunzione  (BigDecimal  aValore )   { setBigDecimal ("FUN_ID_FUNZIONE", aValore); } 
  public void  setNomePagina     (String      aValore )   { setString     ("NOME_PAGINA"    , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new HelponlineModel(  
       getFunIdFunzione() , 
      getNomePagina()  
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(HelponlineModel aModel) throws DAOException {
    setFunIdFunzione  ( aModel.getFunIdFunzione() );  
    setNomePagina     ( aModel.getNomePagina()    );  
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(HelponlineModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getFunIdFunzione() != null ) { 
      lCondizioni += " and FUN_ID_FUNZIONE = " + aModel.getFunIdFunzione() + ""; 
    } 
    if (aModel.getNomePagina() != null && aModel.getNomePagina().length() > 0) { 
      lCondizioni += " and NOME_PAGINA = '" + aModel.getNomePagina() + "' "; 
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
  public void selCondizioneUpdate( BigDecimal aFunIDFunzione) {
    String lCondizioni = new String();

    lCondizioni += " and FUN_ID_FUNZIONE = " + aFunIDFunzione;
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
