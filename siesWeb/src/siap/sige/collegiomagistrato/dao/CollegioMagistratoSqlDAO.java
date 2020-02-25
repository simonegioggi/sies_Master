package siap.sige.collegiomagistrato.dao;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.magistrato.model.MagistratoModel;

/**
 * <p>
 * Title: CollegioMagistraleSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella CollegioMagistrale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
*/
public class CollegioMagistratoSqlDAO extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public CollegioMagistratoSqlDAO(Connection con) {
    super(con);
  }

/**
 * Query SQL Generica.
 * <p>
	 * 
 * @return la query SQL.
 */
	protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT ";
    lStatement +=   " ROWNUM, ";
		lStatement +=   " COL_MAG.MAG_COD_MAGISTRATO, ";
		lStatement +=   " COL_MAG.COL_ID_COLLEGIO, ";
		lStatement +=   " COL_MAG.COD_OPERATORE_INSERIMENTO, ";
		lStatement +=   " COL_MAG.DATA_INSERIMENTO, ";
    lStatement +=   " COL_MAG.COD_UFFICIO_INSERIMENTO, ";
		// 20171013: [SG] aggiunta variabile di collegamento all'udienza sige
		lStatement += " COL_MAG.UDI_ID_UDIENZA_SIGE, ";
    lStatement +=   " MAG.COGNOME, ";
    lStatement +=   " MAG.NOME ";
    lStatement += " FROM COLLEGIO_MAGISTRATO COL_MAG ";
    lStatement +=   " INNER JOIN MAGISTRATO MAG ON MAG.COD_MAGISTRATO = COL_MAG.MAG_COD_MAGISTRATO ";
    lStatement += " WHERE ";

    return lStatement;
  }

  //
  // METODO GETMODEL()
  //
	public GenericModel getModel() throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".getModel : inizio");
    
    CollegioMagistratoModel aModel = new  CollegioMagistratoModel();
    aModel.setColIdCollegio(getBigDecimal("COL_ID_COLLEGIO"));
    aModel.setMagCodMagistrato(getString("MAG_COD_MAGISTRATO"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    // Aggiunge contatore per elenco dei magistrati.
    aModel.setProgr(getInt("ROWNUM"));
		// 20171013: [SG] aggiunta variabile di collegamento all'udienza sige
		aModel.setUdiIdUdienzaSige(getBigDecimal("UDI_ID_UDIENZA_SIGE"));
    
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug(" aModel I - Valore : " + aModel );
    
    // Dati afferenti al Magistrato.
    aModel.setMagistrato(new MagistratoModel());
    aModel.getMagistrato().setCodMagistrato(aModel.getMagCodMagistrato());
    aModel.getMagistrato().setCognome(getString("COGNOME"));
    aModel.getMagistrato().setNome(getString("NOME"));
    
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug(" aModel II - Valore : " + aModel );

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".getModel : fine");
    
    return aModel;
  }

	public String setCondizioniByIdCollegio(BigDecimal aKey) {
    return " COL_ID_COLLEGIO = " + aKey;
  }

	public String setCondizioniByIdCollegioCodUff(BigDecimal aKey, String aCodUff) {
    return " COL_ID_COLLEGIO = " + aKey + " AND MAG.COD_UFFICIO_APPARTENENZA = " + aCodUff;
  }

  //
  // METODO RICERCA()
  //
  /**
   * Imposta statement sql per ricerca Magistrato per Id Collegio.
   * <p>
	 * 
	 * @param aKey
	 *            l'id Collegio.
	 * @throws DAOException
	 *             propaga errore di eccezione.
   */
	public void ricercaMagistratoByIdCollegio(BigDecimal aKey) throws DAOException {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByIdCollegio( aKey );
    setStatement( lSql );
  }
  
  /**
   * Imposta statement sql per ricerca Magistrato per Id Collegio.
   * <p>
	 * 
	 * @param aKey
	 *            l'id Collegio.
	 * @throws DAOException
	 *             propaga errore di eccezione.
   */
	public void ricercaMagistratoByIdCollegioCodUff(BigDecimal aKey, String aCodUff) throws DAOException {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByIdCollegioCodUff( aKey, aCodUff );
    setStatement( lSql );
  }

	// 20171012: [SG] nuova query di ricerca con join sulla tabella udienza_sige
	public void ricercaMagistratoByIdCollegioCodUffIdUdienza(BigDecimal colIdCollegio,
			String codUfficioAppartenenza, BigDecimal idUdienzaSige) {

		String lStatement = new String("");
		lStatement += "SELECT";
		lStatement += " ROWNUM,";
		lStatement += " COL_MAG.MAG_COD_MAGISTRATO,";
		lStatement += " COL_MAG.COL_ID_COLLEGIO,";
		lStatement += " COL_MAG.COD_OPERATORE_INSERIMENTO,";
		lStatement += " COL_MAG.DATA_INSERIMENTO,";
		lStatement += " COL_MAG.COD_UFFICIO_INSERIMENTO,";
		lStatement += " COL_MAG.UDI_ID_UDIENZA_SIGE, ";
		lStatement += " MAG.COGNOME,";
		lStatement += " MAG.NOME";
		lStatement += " FROM COLLEGIO_MAGISTRATO COL_MAG";
		lStatement += " INNER JOIN MAGISTRATO MAG ON MAG.COD_MAGISTRATO = COL_MAG.MAG_COD_MAGISTRATO";
		lStatement += " LEFT OUTER JOIN UDIENZA_SIGE S ON S.ID_UDIENZA_SIGE = COL_MAG.UDI_ID_UDIENZA_SIGE";
		lStatement += " WHERE COL_MAG.COL_ID_COLLEGIO = '" + colIdCollegio + "'";
		lStatement += " AND MAG.COD_UFFICIO_APPARTENENZA = '" + codUfficioAppartenenza + "'";
		lStatement += " AND S.ID_UDIENZA_SIGE = '" + idUdienzaSige + "'";
		setStatement(lStatement);
	}

  /**
	 * Metodo che imposta lo statement, per recuperare la count dei records di Magistrato Relatore che
	 * afferiscano al Magistrato aCodMagistrato
   * <p>
	 * 
	 * @param aCodMagistrato
	 *            utilizzato per impostare le condizioni di filtro.
   */
/*
	 * public void countMagSezByCodMagistrato( String aCodMagistrato ) { String lStatement =
	 * "SELECT COUNT(*) AS COUNT FROM MAGISTRATO_SEZIONE WHERE"; lStatement += " MAG_COD_MAGISTRATO = '" +
	 * aCodMagistrato + "'"; setStatement( lStatement ); }
*/
}