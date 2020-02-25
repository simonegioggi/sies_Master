package siap.siep.stampadocumenti.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.stampadocumenti.model.StampaDocumentiModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;


/**
* <p>Title: StampaDocumentiSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella StampaDocumenti</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 1.0
*/
public class StampaDocumentiSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public StampaDocumentiSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountStampaDocumenti(StampaDocumentiModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM STAMPA_DOCUMENTI ";

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lStatement+=" WHERE " + lCondizioni;

    // Imposta lo statement da eseguire 
    setStatement(lStatement);
  }

  /***************************************************************************** 
   * Effettua la ricerca e restituisce solo i risultati nel range di record che 
   * vanno inseriti nella pagfina passata in input 
   * @param aModel 
   * @param aPage 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaStampaDocumentiPaged(StampaDocumentiModel aModel, int aPage) throws DAOException { 
    String lStatement = new String(""); 

    lStatement += getSqlQuery(); 

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lStatement+=" WHERE " + lCondizioni;

    lStatement += " "+getOrderBy()+" "; 

    String lPaginedStatement = ""; 
    lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement + "  ) INNER ) WHERE rn between  " + ( (aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + 
          " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE; 

    setStatement(lPaginedStatement); 
  } 


  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaStampaDocumenti( StampaDocumentiModel  aModel)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model 
    String lCondizioni = setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lSql += " WHERE " + lCondizioni;

    lSql += " "+getOrderBy()+" "; 

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }


  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaStampaDocumentiByKey( BigDecimal aIdStampa) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE " + setCondizioniByKey( aIdStampa);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }


  /***************************************************************************** 
   * Metodo per la costruzione della sql query 
   * @return 
   ****************************************************************************/ 
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_STAMPA, "+  
                  "ID_UTENTE, "+  
                  "DATA, "+  
                  "STATO, "+  
                  "DESCRIZIONE, "+  
                  "NUM_STAMPE_RICHIESTE, "+  
                  "NUM_STAMPE_EFFETTUATE "; 
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM STAMPA_DOCUMENTI";


    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     StampaDocumentiModel aModel = new  StampaDocumentiModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdStampa             ( getBigDecimal ("ID_STAMPA" ) ); 
    aModel.setIdUtente             ( getString     ("ID_UTENTE" ) ); 
    aModel.setData                 ( getDate       ("DATA"      ) ); 
    aModel.setStato                ( getString     ("STATO"     ) ); 
    aModel.setDescrizione          ( getString     ("DESCRIZIONE" ) ); 
    aModel.setNumStampeRichieste   ( getInteger ("NUM_STAMPE_RICHIESTE"  ) ); 
    aModel.setNumeStampeEffettuate ( getInteger ("NUM_STAMPE_EFFETTUATE") ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(StampaDocumentiModel aModel) {
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
      lCondizioni += " and NUM_STAMPE_EFFETTUATE = " + aModel.getNumeStampeEffettuate() + ""; 
    } 
    if (aModel.getNumeStampeEffettuate() != null ) { 
        lCondizioni += " and DESCRIZIONE= '" + aModel.getDescrizione() + "'"; 
      } 
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    return lCondizioni; 
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di select per chiave 
   * @param aKey 
   * @return 
   ****************************************************************************/ 
  public String setCondizioniByKey( BigDecimal aIdStampa  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_STAMPA = " + aIdStampa;

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    return lCondizioni;
  }


  /***************************************************************************** 
   * Metodo per la costruzione della sezione order by 
   * @return 
   ****************************************************************************/ 
  protected String getOrderBy() { 
    String orderBy = new String(""); 
    orderBy = " ORDER BY DATA DESC"; 
    return orderBy; 
  } 
}
