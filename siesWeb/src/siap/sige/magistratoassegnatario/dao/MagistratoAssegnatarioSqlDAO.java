package siap.sige.magistratoassegnatario.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: MagistratoAssegnatarioSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella MagistratoAssegnatario</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class MagistratoAssegnatarioSqlDAO extends SIAPSqlDAO
{
  public MagistratoAssegnatarioSqlDAO (Connection con)
  {
   super(con);
  }

  //
  // METODO RICERCA()
  //

  public void ricercaMagistratoAssegnatario( MagistratoAssegnatarioModel  aModel)	 throws DAOException
		{
			 String lSql = getSqlQuery();

		 lSql += " " + setCondizione(aModel);
			 setStatement(lSql);
		}

  public void ricercaMagistratoAssegnatarioByFascicolo( BigDecimal aKey, String codUffUteConnesso)	 throws DAOException
      {
         String lSql = getSqlQuery();

         lSql += " AND FAS_SIGE_ID_FASCICOLO_SIGE = "+ aKey;
         lSql += " AND M.COD_UFFICIO_APPARTENENZA = "+ codUffUteConnesso;
         lSql += " ORDER BY MA.DATA_INSERIMENTO DESC ";
         setStatement(lSql);
      }


  public void ricercaMagistratoAssegnatarioByKey( BigDecimal aKey)	 throws DAOException
		{
			 String lSql = getSqlQuery();

		 lSql += " " + setCondizioniByKey(aKey);
			 setStatement(lSql);
		}

  public void ricercaMagistratoEsistente( MagistratoAssegnatarioMagistratoModel  aModel)	 throws DAOException
  {
      String lSql = getSqlQuery();
      lSql += " AND MAG_COD_MAGISTRATO = " +"'" + aModel.getMagistrato().getCodMagistrato()+"'";
      lSql += " AND COD_RUOLO_MAGISTRATO = '03' " ;
      lSql += " AND FAS_SIGE_ID_FASCICOLO_SIGE =" + aModel.getMagistratoAssegnatario().getFasSigeIdFascicoloSige();
      lSql += " ORDER BY DATA_INIZIO DESC ";
      setStatement(lSql);
  }

  protected String getSqlQuery()
  {			 
	 String lStatement = new String("");
	 lStatement += " SELECT " ;
	 lStatement += " MA.DATA_INSERIMENTO, ";
	 lStatement += " MA.DATA_INIZIO, ";
	 lStatement += " MA.DATA_FINE, ";
	 lStatement += " MA.COD_RUOLO_MAGISTRATO, COD_RUOL.RV_MEANING RUOLO, ";
	 lStatement += " MA.COD_OPERATORE_INSERIMENTO, ";
	 lStatement += " MA.COD_UFFICIO_INSERIMENTO, ";
	 lStatement += " MA.COD_OPERATORE_AGGIORNAMENTO, ";
	 lStatement += " MA.DATA_AGGIORNAMENTO, ";
	 lStatement += " MA.COD_UFFICIO_AGGIORNAMENTO, ";
	 lStatement += " MA.MAG_COD_MAGISTRATO, ";
	 lStatement += " M.COGNOME, ";
	 lStatement += " M.NOME, ";
	 lStatement += " MA.FAS_SIGE_ID_FASCICOLO_SIGE ";
	 lStatement += "FROM MAGISTRATO_ASSEGNATARIO MA, MAGISTRATO M, CG_REF_CODES COD_RUOL ";
	 lStatement += "WHERE MA.MAG_COD_MAGISTRATO = M.COD_MAGISTRATO ";
	 lStatement += " AND COD_RUOL.RV_DOMAIN = 'RUOLO_MAGISTRATO' AND COD_RUOL.RV_LOW_VALUE = COD_RUOLO_MAGISTRATO";
	 return lStatement;
  }

  //
  // METODO GETMODEL()
  //

  public GenericModel  	 getModel() throws DAOException
  {
	 MagistratoAssegnatarioModel aModel = new  MagistratoAssegnatarioModel();

	 //Inserire le opportune set delle descrizioni!

	 aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
	 aModel.setDataInizio(getDate("DATA_INIZIO") );
	 aModel.setDataFine(getDate("DATA_FINE") );
	 aModel.setCodRuoloMagistrato(getString("COD_RUOLO_MAGISTRATO") );
	 aModel.setDescrRuoloMagistrato(getString("RUOLO") );
	 aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
	 aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
	 aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
	 // aModel.setDescrUfficioInserimento(getString("") );
	 aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
	 aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
	 aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
	 // aModel.setDescrUfficioAggiornamento(getString("") );
	 aModel.setMagCodMagistrato(getString("MAG_COD_MAGISTRATO") );
	 aModel.setFasSigeIdFascicoloSige(getBigDecimal("FAS_SIGE_ID_FASCICOLO_SIGE") );
	 return aModel;
  }


  public String  setCondizione(MagistratoAssegnatarioModel aModel)
  {
	 String lCondizioni = new String();
	 return lCondizioni;
  }

  public String setCondizioniByKey(BigDecimal aKey)
  {
	 return " AND ID_MAGISTRATO_ASSEGNATARIO = " + aKey;
  }

  /**
   * Metodo che imposta lo statement, per recuperare la count
   * dei records di Magistrato Assegnatario che afferiscano al Magistrato aCodMagistrato
   * <p>
   * @param aCodMagistrato utilizzato per impostare le condizioni di filtro.
   */
  public void countMagCompByCodMagistrato( String aCodMagistrato )
  {
    String lStatement = "SELECT COUNT(*) AS COUNT FROM MAGISTRATO_COMPETENTE WHERE";
    lStatement += " MAG_COD_MAGISTRATO = '"+aCodMagistrato+"'";

    setStatement( lStatement );
  }

  public MagistratoAssegnatarioModel getModelMagistratoAssegnatario() throws DAOException
  {
	  MagistratoAssegnatarioModel aModel = new  MagistratoAssegnatarioModel();

    aModel.setMagCodMagistrato(getString("MAG_COD_MAGISTRATO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setDataInizio(getDate("DATA_INIZIO") );
    aModel.setDataFine(getDate("DATA_FINE") );
    aModel.setFasSigeIdFascicoloSige(getBigDecimal("FAS_SIGE_ID_FASCICOLO_SIGE") );

    return aModel;
  }

  public MagistratoModel getModelMagistrato() throws DAOException
  {
	MagistratoModel aModel = new  MagistratoModel();

    aModel.setCodMagistrato(getString("MAG_COD_MAGISTRATO") );
    aModel.setCognome(getString("COGNOME") );
    aModel.setNome(getString("NOME") );
    aModel.setDataInizioValidita(getDate("DATA_INIZIO") );

    return aModel;
  }

  public void ricercaMagistratoAssegnatarioByFascicolo( BigDecimal aKey) throws DAOException
  {
     String lSql = getSqlQuery();

     lSql += " AND FAS_SIGE_ID_FASCICOLO_SIGE = "+ aKey;
     lSql += " AND DATA_FINE IS NULL ";
     setStatement(lSql);
  }
   
}
