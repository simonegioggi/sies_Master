package siap.sius.curatore.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sige.curatore.model.CuratoreModel;
import siap.sius.curatore.model.CuratoreSiusModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: CuratoreSiusSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella CuratoreSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class CuratoreSiusSqlDAO extends SIAPSqlDAO
{
  public CuratoreSiusSqlDAO (Connection con)
  {
    super(con);
  }

/**
 * Query SQL Generica.
 * <p>
 * @return la query SQL.
 */
  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += "SELECT ";
		lStatement += "CS.DATA_INIZIO, ";
		lStatement += "CS.DATA_FINE, ";
		lStatement += "CS.FLAG_TIPO, TIPO.RV_MEANING DESCR_TIPO,";
		lStatement += "CS.COD_OPERATORE_INSERIMENTO, ";
		lStatement += "CS.DATA_INSERIMENTO, ";
    lStatement += "CS.COD_UFFICIO_INSERIMENTO, ";
		lStatement += "CS.COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "CS.DATA_AGGIORNAMENTO, ";
		lStatement += "CS.COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += "CS.CUR_ID_CURATORE, ";
		lStatement += "CS.FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += "C.COGNOME, C.NOME ";
    lStatement += "FROM CURATORE_SIUS CS, CURATORE C, CG_REF_CODES TIPO ";
    lStatement += "WHERE CS.CUR_ID_CURATORE = C.ID_CURATORE ";
    lStatement += "AND TIPO.RV_DOMAIN = 'TIPO_CURATORE' AND TIPO.RV_LOW_VALUE = CS.FLAG_TIPO AND ";
    return lStatement;
  }
  
  //
  // METODO GETMODEL()
  //
  public GenericModel  	 getModel() throws DAOException
  {
    CuratoreSiusModel aModel = new  CuratoreSiusModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setDataInizio(getDate("DATA_INIZIO") );
    aModel.setDataFine(getDate("DATA_FINE") );
    aModel.setFlagTipo(getString("FLAG_TIPO") );
    aModel.setDescrTipo(getString("DESCR_TIPO") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setCurIdCuratore(getBigDecimal("CUR_ID_CURATORE") );
    aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS") );

    return aModel;
  }

  /**
   * Imposta condizione per l'id del fgascicolo SIUS.
   * <p>
   * @param aKey id del fascicolo SIUS
   * @return stringa di condizione.
   */
  public String setCondizioniByIdFascicolo( BigDecimal aKey )
  {
    return " FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;
  }

  //
  // METODO RICERCA()
  //
/**
 * Imposta statement sql per ricerca Curatore per fascicolo SIUS.
 * <p>
 * @param aKey l'id del fascicolo SIUS.
 * @throws DAOException propaga errore di eccezione.
 */
  public void ricercaCuratoreSiusByIdFascicolo( BigDecimal aKey, String codUfficioUtenteConnesso ) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByIdFascicolo( aKey );
    setStatement( lSql );
  }

  /**
   * Imposta la select per la ricerca Curatore Corrente per id fascicolo SIUS.
   * Il controllo vine fatto impostando il filtro di ricerca con la data_fine is null.
   * <p>
   * @param aKey id del fascicolo SIUS.
   * @throws DAOException propaga l'errore di eccezione.
   */
  public void ricercaCuratoreSiusCorrenteByFascicolo( BigDecimal aKey )
  throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByIdFascicolo( aKey );
    lSql += " AND DATA_FINE IS NULL ";
    setStatement(lSql);
  }

  /**
   * Imposta la select per la ricerca Curatore Sius Corrente + ultimo per id fascicolo SIUS.
   * Il controllo vine fatto impostando il filtro di ricerca con la data_fine is null e max(data fine) .
   * <p>
   * @param aKey id del fascicolo SIUS.
   * @throws DAOException propaga l'errore di eccezione.
   */
  public void ricercaCuratoreSiusByDataFine( BigDecimal aKey )
  throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByIdFascicolo( aKey );
    lSql += " AND ( DATA_FINE =(SELECT MAX(DATA_FINE) FROM CURATORE_SIUS WHERE " + setCondizioniByIdFascicolo( aKey );
    lSql += " ) OR DATA_FINE IS NULL ) ORDER BY DATA_INIZIO DESC";
    setStatement(lSql);
  }

  /**
   * Metodo che imposta lo statement, per recuperare la count
   * dei records di Curatore Sius che afferiscano al Curatore aIdCuratore
   * <p>
   * @param aCodMagistrato utilizzato per impostare le condizioni di filtro.
   */
  public void countCuratoriByIdCuratore( String aIdCuratore )
  {
    String lStatement = "SELECT COUNT(*) AS COUNT FROM CURATORE_SIUS WHERE";
    lStatement += " CUR_ID_CURATORE = '"+aIdCuratore+"'";

    setStatement( lStatement );
  }
  
  public CuratoreSiusModel getModelCuratoreSius() throws DAOException
  {
	  CuratoreSiusModel aModel = new  CuratoreSiusModel();

    aModel.setFlagTipo(getString("FLAG_TIPO") );
    aModel.setDescrTipo(getString("DESCR_TIPO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setDataInizio(getDate("DATA_INIZIO") );
    aModel.setDataFine(getDate("DATA_FINE") );
    aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS") );
    aModel.setCurIdCuratore(getBigDecimal("CUR_ID_CURATORE") );

    return aModel;
  }

  public CuratoreModel getModelCuratore() throws DAOException
  {
	CuratoreModel aModel = new  CuratoreModel();

    aModel.setCognome(getString("COGNOME") );
    aModel.setNome(getString("NOME") );
    aModel.setDataInizioValidita(getDate("DATA_INIZIO") );
    aModel.setIdCuratore(getBigDecimal("CUR_ID_CURATORE") );

    return aModel;
  }

}
