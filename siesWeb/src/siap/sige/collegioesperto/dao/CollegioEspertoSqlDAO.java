package siap.sige.collegioesperto.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.dao.SIAPSqlDAO;
import siap.sige.collegioesperto.model.CollegioEspertoModel;
import siap.sius.esperto.model.EspertoModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
* <p>Title: CollegioGiudicePopolareSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella CollegioGiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
*/
public class CollegioEspertoSqlDAO extends SIAPSqlDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public CollegioEspertoSqlDAO (Connection con)
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

    lStatement += " SELECT ";
		lStatement +=   " ESP.NOME, ";
		lStatement +=   " ESP.COGNOME, ";
		lStatement +=   " COL_ESP.COL_ID_COLLEGIO, ";
		lStatement +=   " COL_ESP.ESP_ID_ESPERTO, ";
		lStatement +=   " COL_ESP.COD_OPERATORE_INSERIMENTO, ";
		lStatement +=   " COL_ESP.DATA_INSERIMENTO, ";
    lStatement +=   " COL_ESP.COD_UFFICIO_INSERIMENTO ";
    lStatement += " FROM COLLEGIO_ESPERTO COL_ESP ";
    lStatement +=   " INNER JOIN ESPERTO ESP " +
    		" ON ESP.ID_ESPERTO = COL_ESP.ESP_ID_ESPERTO ";
    lStatement += " WHERE ";

    return lStatement;
  }

  //
  // METODO GETMODEL()
  //
  public GenericModel getModel() throws DAOException
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".getModel : inizio");
    
    CollegioEspertoModel aModel = new  CollegioEspertoModel();
    aModel.setColIdCollegio(getBigDecimal("COL_ID_COLLEGIO"));
    aModel.setEspIdEsperto(getBigDecimal("ESP_ID_ESPERTO"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(" aModel I - Valore : " + aModel );
    
    // Dati afferenti al GiudicePopolare.
    aModel.setEsperto(new EspertoModel());
    aModel.getEsperto().setIdEsperto(aModel.getEspIdEsperto());
    aModel.getEsperto().setCognome(getString("COGNOME"));
    aModel.getEsperto().setNome(getString("NOME"));
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(" aModel II - Valore : " + aModel );
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".getModel : fine");

    return aModel;
  }
  
  public String setCondizioniByIdCollegio( BigDecimal aKey )
  {
    return " COL_ID_COLLEGIO = " + aKey;
  }

  //
  // METODO RICERCA()
  //
/**
 * Imposta statement sql per ricerca Giudice Popolare per Id Collegio.
 * <p>
 * @param aKey l'id Collegio.
 * @throws DAOException propaga errore di eccezione.
 */
  public void ricercaEspertoByIdCollegio( BigDecimal aKey ) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByIdCollegio( aKey );
    setStatement( lSql );
  }
}