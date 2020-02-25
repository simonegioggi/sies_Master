package siap.sico.provvedimentisiesnsc.dao;

/**
* <p>Title: ProvvSiesNscSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella ProvvSiesNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.sql.Connection;

import siap.sico.provvedimentisiesnsc.model.ProvvSiesNscModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.web.IWebConstants;

public class ProvvSiesNscSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public ProvvSiesNscSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountProvvSiesNsc(ProvvSiesNscModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM PROVV_SIES_NSC ";

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
  public void ricercaProvvSiesNscPaged(ProvvSiesNscModel aModel, int aPage) throws DAOException { 
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
  public void ricercaProvvSiesNsc( ProvvSiesNscModel  aModel)  throws DAOException {
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
  public void ricercaProvvSiesNscByKey( String aProvvDomain, String aProvvCodcentr) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE " + setCondizioniByKey(aProvvDomain ,aProvvCodcentr);

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
                  "PROVV_DOMAIN, "+  
                  "PROVV_CODCENTR, "+  
                  "PROVV_NSC_CAT, "+  
                  "PROVV_NSC_DES_CAT, "+  
                  "PROVV_NSC_NAT, "+  
                  "PROVV_NSC_DES_NAT, "+  
                  "PROVV_SIES_OGGETTO, "+  
                  "PROVV_SIES_DES_OGGETTO, "+  
                  "PROVV_SIES_MOTIVO, "+  
                  "PROVV_SIES_DES_MOTIVO, "+  
                  "PROVV_SIES_ESITO, "+  
                  "PROVV_SIES_DES_ESITO, "+  
                  "PROVV_VAL1, "+  
                  "PROVV_VAL2, "+  
                  "PROVV_VAL3 "; 
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM PROVV_SIES_NSC";


    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     ProvvSiesNscModel aModel = new  ProvvSiesNscModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setProvvDomain         ( getString ("PROVV_DOMAIN"          ) ); 
    aModel.setProvvCodcentr       ( getString ("PROVV_CODCENTR"        ) ); 
    aModel.setProvvNscCat         ( getString ("PROVV_NSC_CAT"         ) ); 
    aModel.setProvvNscDesCat      ( getString ("PROVV_NSC_DES_CAT"     ) ); 
    aModel.setProvvNscNat         ( getString ("PROVV_NSC_NAT"         ) ); 
    aModel.setProvvNscDesNat      ( getString ("PROVV_NSC_DES_NAT"     ) ); 
    aModel.setProvvSiesOggetto    ( getString ("PROVV_SIES_OGGETTO"    ) ); 
    aModel.setProvvSiesDesOggetto ( getString ("PROVV_SIES_DES_OGGETTO") ); 
    aModel.setProvvSiesMotivo     ( getString ("PROVV_SIES_MOTIVO"     ) ); 
    aModel.setProvvSiesDesMotivo  ( getString ("PROVV_SIES_DES_MOTIVO" ) ); 
    aModel.setProvvSiesEsito      ( getString ("PROVV_SIES_ESITO"      ) ); 
    aModel.setProvvSiesDesEsito   ( getString ("PROVV_SIES_DES_ESITO"  ) ); 
    aModel.setProvvVal1           ( getString ("PROVV_VAL1"            ) ); 
    aModel.setProvvVal2           ( getString ("PROVV_VAL2"            ) ); 
    aModel.setProvvVal3           ( getString ("PROVV_VAL3"            ) ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(ProvvSiesNscModel aModel) {
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

    return lCondizioni; 
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di select per chiave 
   * @param aKey 
   * @return 
   ****************************************************************************/ 
  public String setCondizioniByKey( String aProvvDomain, String aProvvCodcentr  ) {
    String lCondizioni = new String();

    lCondizioni += " and PROVV_CODCENTR = '" + aProvvCodcentr + "'";
    lCondizioni += " and PROVV_DOMAIN = '" + aProvvDomain + "'";

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
    //orderBy = " ORDER BY "; 
    return orderBy; 
  } 
}
