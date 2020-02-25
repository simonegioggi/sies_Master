package siap.sige.collegiogiudicepopolare.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.dao.SIAPSqlDAO;
import siap.sige.collegiogiudicepopolare.model.CollegioGiudicePopolareModel;
import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
* <p>Title: CollegioGiudicePopolareSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella CollegioGiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
*/
public class CollegioGiudicePopolareSqlDAO extends SIAPSqlDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public CollegioGiudicePopolareSqlDAO (Connection con)
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
		lStatement +=   " GIU_POP.NOME, ";
		lStatement +=   " GIU_POP.COGNOME, ";
		lStatement +=   " COL_GIU_POP.COL_ID_COLLEGIO, ";
		lStatement +=   " COL_GIU_POP.GIU_POP_ID_GIUDICE_POPOLARE, ";
		lStatement +=   " COL_GIU_POP.COD_OPERATORE_INSERIMENTO, ";
		lStatement +=   " COL_GIU_POP.DATA_INSERIMENTO, ";
    lStatement +=   " COL_GIU_POP.COD_UFFICIO_INSERIMENTO ";
    lStatement += " FROM COLLEGIO_GIUDICE_POPOLARE COL_GIU_POP ";
    lStatement +=   " INNER JOIN GIUDICE_POPOLARE GIU_POP " +
    		" ON GIU_POP.ID_GIUDICE_POPOLARE = COL_GIU_POP.GIU_POP_ID_GIUDICE_POPOLARE ";
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
    
    CollegioGiudicePopolareModel aModel = new  CollegioGiudicePopolareModel();
    aModel.setColIdCollegio(getBigDecimal("COL_ID_COLLEGIO"));
    aModel.setGiuPopIdGiudicePopolare(getBigDecimal("GIU_POP_ID_GIUDICE_POPOLARE"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(" aModel I - Valore : " + aModel );
    
    // Dati afferenti al GiudicePopolare.
    aModel.setGiudicePopolare(new GiudicePopolareModel());
    aModel.getGiudicePopolare().setIdGiudicePopolare(aModel.getGiuPopIdGiudicePopolare());
    aModel.getGiudicePopolare().setCognome(getString("COGNOME"));
    aModel.getGiudicePopolare().setNome(getString("NOME"));
    
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
  public void ricercaGiudicePopolareByIdCollegio( BigDecimal aKey ) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByIdCollegio( aKey );
    setStatement( lSql );
  }
}