package siap.sius.magistratorelatore.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: MagistratoRelatoreSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella MagistratoRelatore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class MagistratoRelatoreSqlDAO extends SIAPSqlDAO
{
  public MagistratoRelatoreSqlDAO (Connection con)
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
		lStatement += "DATA_INIZIO, ";
		lStatement += "DATA_FINE, ";
		lStatement += "COD_RUOLO_MAGISTRATO, ";
		lStatement += "COD_OPERATORE_INSERIMENTO, ";
		lStatement += "DATA_INSERIMENTO, ";
    lStatement += "COD_UFFICIO_INSERIMENTO, ";
		lStatement += "COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "DATA_AGGIORNAMENTO, ";
		lStatement += "COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += "MAG_COD_MAGISTRATO, ";
		lStatement += "FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += "ESP_ID_ESPERTO ";
    lStatement += "FROM MAGISTRATO_RELATORE MAG_REL ";
    lStatement += "WHERE ";

    return lStatement;
  }

  //
  // METODO GETMODEL()
  //
  public GenericModel  	 getModel() throws DAOException
  {
    MagistratoRelatoreModel aModel = new  MagistratoRelatoreModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setDataInizio(getDate("DATA_INIZIO") );
    aModel.setDataFine(getDate("DATA_FINE") );
    aModel.setCodRuoloMagistrato(getString("COD_RUOLO_MAGISTRATO") );
    //aModel.setDescrRuoloMagistrato(getString("") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setMagCodMagistrato(getString("MAG_COD_MAGISTRATO") );
    aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS") );
    aModel.setEspIdEsperto(getBigDecimal("ESP_ID_ESPERTO") );

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
 * Imposta statement sql per ricerca Magistrato Relatore per fascicolo SIUS.
 * <p>
 * @param aKey l'id del fascicolo SIUS.
 * @throws DAOException propaga errore di eccezione.
 */
  public void ricercaMagistratoRelatoreByIdFascicolo( BigDecimal aKey ) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByIdFascicolo( aKey );
    setStatement( lSql );
  }

  /**
   * Imposta la select per la ricerca Magistrato Relatore Corrente per id fascicolo SIUS.
   * Il controllo vine fatto impostando il filtro di ricerca con la data_fine is null.
   * <p>
   * @param aKey id del fascicolo SIUS.
   * @throws DAOException propaga l'errore di eccezione.
   */
  public void ricercaMagistratoRelatoreCorrenteByFascicolo( BigDecimal aKey )
  throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByIdFascicolo( aKey );
    lSql += " AND DATA_FINE IS NULL ";
    setStatement(lSql);
  }

  /**
   * Imposta la select per la ricerca Magistrato Relatore Corrente + ultimo per id fascicolo SIUS.
   * Il controllo vine fatto impostando il filtro di ricerca con la data_fine is null e max(data fine) .
   * <p>
   * @param aKey id del fascicolo SIUS.
   * @throws DAOException propaga l'errore di eccezione.
   */
  public void ricercaMagistratoRelatoreByDataFine( BigDecimal aKey )
  throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByIdFascicolo( aKey );
    lSql += " AND ( DATA_FINE =(SELECT MAX(DATA_FINE) FROM MAGISTRATO_RELATORE WHERE " + setCondizioniByIdFascicolo( aKey );
    lSql += " ) OR DATA_FINE IS NULL ) ORDER BY DATA_INIZIO DESC";
    setStatement(lSql);
  }

  /**
   * Metodo che imposta lo statement, per recuperare la count
   * dei records di Magistrato Relatore che afferiscano al Magistrato aCodMagistrato
   * <p>
   * @param aCodMagistrato utilizzato per impostare le condizioni di filtro.
   */
  public void countMagRelByCodMagistrato( String aCodMagistrato )
  {
    String lStatement = "SELECT COUNT(*) AS COUNT FROM MAGISTRATO_RELATORE WHERE";
    lStatement += " MAG_COD_MAGISTRATO = '"+aCodMagistrato+"'";

    setStatement( lStatement );
  }

}
