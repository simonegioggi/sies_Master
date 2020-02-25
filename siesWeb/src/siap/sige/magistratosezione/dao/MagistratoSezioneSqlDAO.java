package siap.sige.magistratosezione.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sige.magistratosezione.model.MagistratoSezioneModel;
import siap.sige.sezione.model.SezioneModel;

/**
* <p>Title: MagistratoSezioneSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella MagistratoSezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
*/
public class MagistratoSezioneSqlDAO extends SIAPSqlDAO
{
  public MagistratoSezioneSqlDAO (Connection con)
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
		lStatement +=   " MAG_SEZ.MAG_COD_MAGISTRATO, ";
		lStatement +=   " SEZ_ID_SEZIONE, ";
		lStatement +=   " SEZ.CODICE, ";
		lStatement +=   " SEZ.DESCRIZIONE, ";
		lStatement +=   " MAG_SEZ.COD_OPERATORE_INSERIMENTO, ";
		lStatement +=   " MAG_SEZ.DATA_INSERIMENTO, ";
		lStatement +=   " MAG_SEZ.COD_UFFICIO_INSERIMENTO, ";
		lStatement +=   " MAG_SEZ.COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement +=   " MAG_SEZ.DATA_AGGIORNAMENTO, ";		
		lStatement +=   " MAG_SEZ.DATA_INIZIO_ASS, ";
		lStatement +=   " MAG_SEZ.DATA_FINE_ASS, ";	
		lStatement +=   " MAG_SEZ.FLG_VALIDO_SN, ";	
		lStatement +=   " MAG_SEZ.COD_UFFICIO_AGGIORNAMENTO ";
    lStatement += " FROM MAGISTRATO_SEZIONE MAG_SEZ ";
    lStatement +=   " LEFT OUTER JOIN SEZIONE SEZ ON SEZ.ID_SEZIONE = MAG_SEZ.SEZ_ID_SEZIONE ";
    lStatement += " WHERE ";

    return lStatement;
  }

  //
  // METODO GETMODEL()
  //
  public GenericModel getModel() throws DAOException
  {
    MagistratoSezioneModel aModel = new  MagistratoSezioneModel();
    aModel.setMagCodMagistrato(getString("MAG_COD_MAGISTRATO"));
    aModel.setSezIdSezione(getBigDecimal("SEZ_ID_SEZIONE"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));   
    aModel.setDataInizioAssegnazione(getDate("DATA_INIZIO_ASS"));
    aModel.setDataFineAssegnazione(getDate("DATA_FINE_ASS"));   
    aModel.setFlagValidoSN(getString("FLG_VALIDO_SN"));
    // Dati afferenti alla Sezione.
    aModel.setSezione(new SezioneModel());
    aModel.getSezione().setIdSezione(aModel.getSezIdSezione());
    aModel.getSezione().setCodice(getString("CODICE"));
    aModel.getSezione().setDescrizione(getString("DESCRIZIONE"));

    return aModel;
  }

  /**
   * Imposta condizione per l'id del fgascicolo SIUS.
   * <p>
   * @param aKey id del fascicolo SIUS
   * @return stringa di condizione.
   */
  public String setCondizioniByCodMagistrato( String aKey )
  {
    return " MAG_COD_MAGISTRATO = " + aKey;
  }
  
  /**
   * Imposta condizione per l'id del fgascicolo SIUS con FLG_VALIDO_SN='S'
   * <p>
   * @param aKey id del fascicolo SIUS
   * @return stringa di condizione.
   */
  public String setCondizioniByCodMagistratoFlgValidoSN( String aKey )
  {
	  return " FLG_VALIDO_SN='S' AND MAG_COD_MAGISTRATO = " + aKey;
  }
  
  /**
   * Imposta condizione per l'id del fgascicolo SIUS con FLG_VALIDO_SN='S'
   * <p>
   * @param aKey id del fascicolo SIUS
   * @return stringa di condizione.
   */
  public String setCondizioniByCodMagistratoFlgValidoSN( String aKey, String sezione )
  {
	  String condizione = "";
	  if (sezione != null && !sezione.equals("")){
		  //[EC] 20171012: per le vecchie sezioni non esisteva il FLG_VALIDO_SN e pertanto ho aggiunto or FLG_VALIDO_SN is null
		  condizione = " ( FLG_VALIDO_SN = 'S' or  FLG_VALIDO_SN is null) AND SEZ_ID_SEZIONE = " +sezione+ " AND MAG_COD_MAGISTRATO = " + aKey;
	  } else {
		  //[EC] 20171012: per le vecchie sezioni non esisteva il FLG_VALIDO_SN e pertanto ho aggiunto or FLG_VALIDO_SN is null
		  condizione = " ( FLG_VALIDO_SN = 'S' or  FLG_VALIDO_SN is null) AND MAG_COD_MAGISTRATO = " + aKey;
	  }
	  return condizione;
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
  public void ricercaMagistratoSezioneByCodMagistrato( String aKey ) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByCodMagistrato( aKey );
    lSql += " ORDER BY MAG_SEZ.DATA_INIZIO_ASS  desc,  MAG_SEZ.DATA_FINE_ASS  desc ";
    setStatement( lSql );
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
  public void ricercaMagistratoSezioneByCodMagistratoFlgValidoSN( String aKey ) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByCodMagistratoFlgValidoSN( aKey );
    lSql += " ORDER BY MAG_SEZ.DATA_INIZIO_ASS  desc,  MAG_SEZ.DATA_FINE_ASS  desc ";
    setStatement( lSql );
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
  public void ricercaMagistratoSezioneByCodMagistratoFlgValidoSN( String aKey, String sezione ) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " " + setCondizioniByCodMagistratoFlgValidoSN( aKey,sezione );
    lSql += " ORDER BY MAG_SEZ.DATA_INIZIO_ASS  desc,  MAG_SEZ.DATA_FINE_ASS  desc ";
    setStatement( lSql );
  }
  
  /**
   * Metodo che imposta lo statement, per recuperare la count
   * dei records di Magistrato Relatore che afferiscano al Magistrato aCodMagistrato
   * <p>
   * @param aCodMagistrato utilizzato per impostare le condizioni di filtro.
   */
  public void countMagSezByCodMagistrato( String aCodMagistrato )
  {
    String lStatement = "SELECT COUNT(*) AS COUNT FROM MAGISTRATO_SEZIONE WHERE";
    lStatement += " MAG_COD_MAGISTRATO = '" + aCodMagistrato + "'";
    setStatement( lStatement );
  }

	/**
	 * 20171013: [EC] aggiungo metodo per recuperare le sezioni per codice magistrato ed ufficio appartenenza
	 * 
	 * @param codMagistrato
	 * @param aCodUfficio
	 */
	public void ricercaMagistratoSezioneByCodMagistrato(String codMagistrato,
			String aCodUfficio) {
		
//	    String lSql = " SELECT SEZ.ID_SEZIONE, SEZ.CODICE, SEZ.DESCRIZIONE FROM MAGISTRATO_SEZIONE MAG_SEZ"
//	    		+ "  LEFT OUTER JOIN SEZIONE SEZ ON SEZ.ID_SEZIONE = MAG_SEZ.SEZ_ID_SEZIONE WHERE" ;
//	    lSql += " " + setCondizioniByCodMagistrato( codMagistrato );
	    String lSql = getSqlQuery();
	    lSql += " " + setCondizioniByCodMagistrato( codMagistrato );
	    if(aCodUfficio != null && !aCodUfficio.equals("")){
	    	 lSql += " AND SEZ.COD_UFFICIO_APPARTENENZA = '" +aCodUfficio+"'" ;
	    }	   
	    lSql += " ORDER BY MAG_SEZ.DATA_INIZIO_ASS  desc,  MAG_SEZ.DATA_FINE_ASS  desc ";
	    setStatement( lSql );		
	}
}