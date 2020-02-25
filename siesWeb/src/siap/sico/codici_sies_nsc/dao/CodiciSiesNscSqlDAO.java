package siap.sico.codici_sies_nsc.dao;

/**
* <p>Title: CodiciSiesNscSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella CodiciSiesNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.sql.Connection;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.web.IWebConstants;

public class CodiciSiesNscSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public CodiciSiesNscSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountCodiciSiesNsc(CodiciSiesNscModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM CODICI_SIES_NSC ";

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
  public void ricercaCodiciSiesNscPaged(CodiciSiesNscModel aModel, int aPage) throws DAOException { 
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
  public void ricercaCodiciSiesNsc( CodiciSiesNscModel  aModel)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model 
    String lCondizioni = setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lSql+=" WHERE " + lCondizioni;

    lSql += " "+getOrderBy()+" "; 

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }


  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaCodiciSiesNscByKey( String aCoDomain, String aCoCodcentr) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE " + setCondizioniByKey( aCoDomain, aCoCodcentr);

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
                  "CO_DOMAIN, "+  
                  "CO_CODCENTR, "+  
                  "CO_NSC, "+  
                  "CO_NSC_DES, "+  
                  "CO_SIES, "+  
                  "CO_SIES_DES, "+  
                  "CO_VAL1, "+  
                  "CO_VAL2, "+  
                  "CO_VAL3 "; 
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM CODICI_SIES_NSC";


    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     CodiciSiesNscModel aModel = new  CodiciSiesNscModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setCoDomain   ( getString     ("CO_DOMAIN"  ) ); 
    aModel.setCoCodcentr ( getString     ("CO_CODCENTR") ); 
    aModel.setCoNsc      ( getString     ("CO_NSC"     ) ); 
    aModel.setCoNscDes   ( getString     ("CO_NSC_DES" ) ); 
    aModel.setCoSies     ( getString     ("CO_SIES"    ) ); 
    aModel.setCoSiesDes  ( getString     ("CO_SIES_DES") ); 
    aModel.setCoVal1     ( getString     ("CO_VAL1"    ) ); 
    aModel.setCoVal2     ( getString     ("CO_VAL2"    ) ); 
    aModel.setCoVal3     ( getString     ("CO_VAL3"    ) ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(CodiciSiesNscModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getCoDomain() != null && aModel.getCoDomain().length() > 0) { 
      lCondizioni += " and CO_DOMAIN = '" + aModel.getCoDomain() + "' "; 
    } 
    if (aModel.getCoCodcentr() != null ) { 
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

    return lCondizioni; 
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di select per chiave 
   * @param aKey 
   * @return 
   ****************************************************************************/ 
  public String setCondizioniByKey( String aCoDomain, String aCoCodcentr  ) {
    String lCondizioni = new String();

    lCondizioni += " CO_CODCENTR = '" + aCoCodcentr + "'";
    lCondizioni += " and CO_DOMAIN = '" + aCoDomain + "'";

    return lCondizioni;
  }


  /***************************************************************************** 
   * Metodo per la costruzione della sezione order by 
   * @return 
   ****************************************************************************/ 
  protected String getOrderBy() { 
    String orderBy = new String(""); 
    //orderBy = " ORDER BY "; 
    return orderBy; 
  } 
}
