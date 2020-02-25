package siap.sico.magistrato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>Title: MagistratoSqlDAO</p>
 * <p>Description: Classe SqlDAO che rappresenta la tabella Magistrato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class MagistratoWMagistratoSqlDAO extends SIAPSqlDAO
{
  public MagistratoWMagistratoSqlDAO (Connection con)
  {
    super(con);
  }



  /**
   *  Ricerca del magistrato per codice ufficio di appartenenza
   *  + magistrato senza codice ufficio che costituisce il magistrato indefinito.
   * <p>
   * @param aCodUfficio
   * @throws DAOEXception
   */
  public void ricercaMagistratoByCodUfficio( String aCodUfficio ) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " WHERE COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
    lSql += " OR COD_UFFICIO_APPARTENENZA IS null";
    lSql += " ORDER BY COGNOME";
    setStatement( lSql );
  }

  /**
   * <p>
   * @param aCodUfficio
   * @throws DAOEXception
   */
  public void ricercaMagistratoByKey( BigDecimal aKey ) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " WHERE COD_MAGISTRATO = '" + aKey + "'";
    setStatement( lSql );
  }

  /**
   *
   * <p>
   * @param aCodMagistrato
   * @throws DAOException
   */
  public void ricercaMagistratoByCod( String aCodMagistrato ) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " WHERE COD_MAGISTRATO = '" + aCodMagistrato + "'";
    setStatement( lSql );
  }

  public void ricercaMagistratoByCognome( String  aCognome, String aUfficio)	 throws DAOException
  {
    String lSql = getSqlQuery();
    
    if (aCognome != null)
    {
      lSql += " " + "WHERE MAGISTRATO.COGNOME LIKE '" + aCognome + "%'";
      lSql += " AND W_MAGISTRATO.COD_MAGISTRATO = MAGISTRATO.COD_MAGISTRATO ";
      lSql += " AND MAGISTRATO.COD_UFFICIO_APPARTENENZA = '" + aUfficio +"'";
      // Vengono selezionati soltanto i magistrati ancora attivi
      lSql += " AND (MAGISTRATO.DATA_FINE_VALIDITA IS NULL OR MAGISTRATO.DATA_FINE_VALIDITA > SYSDATE)";
      
      lSql += " ORDER BY MAGISTRATO.COGNOME";
    }
      
    setStatement(lSql);
  }


 /**
  *
  * <p>
  * @return
  */
  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += "SELECT " +
                  "MAGISTRATO.COD_MAGISTRATO, "  +
                  "MAGISTRATO.COGNOME, " +
                  "MAGISTRATO.NOME, "  +
                  "MAGISTRATO.FLAG_STATO, "  +
                  "MAGISTRATO.COD_UFFICIO_APPARTENENZA, "  +
                  "MAGISTRATO.DATA_INIZIO_VALIDITA, "+
                  "MAGISTRATO.DATA_FINE_VALIDITA, "+
                  "MAGISTRATO.COD_OPERATORE_INSERIMENTO, "+
                  "MAGISTRATO.DATA_INSERIMENTO, "+
                  "MAGISTRATO.COD_UFFICIO_INSERIMENTO, "+
                  "MAGISTRATO.COD_OPERATORE_AGGIORNAMENTO, "+
                  "MAGISTRATO.DATA_AGGIORNAMENTO, "+
                  "MAGISTRATO.COD_UFFICIO_AGGIORNAMENTO, "+
                  "MAGISTRATO.E_MAIL_UFFICIO, "+
                  "MAGISTRATO.E_MAIL_PRIVATA, "+
                  "MAGISTRATO.NUM_CELLULARE, "+
                  "W_MAGISTRATO.DATA_NASCITA";

    lStatement += " FROM MAGISTRATO, W_MAGISTRATO";

    return lStatement;
  }


  //
  // METODO GETMODEL()
  //

  /**
   *
   * <p>
   * @return
   * @throws DAOException
   */
  public GenericModel getModel() throws DAOException
  {
    MagistratoModel aModel = new  MagistratoModel();
    //Inserire le opportune set delle descrizioni!
    aModel.setCodMagistrato(getString("COD_MAGISTRATO") );
    //aModel.setDescrMagistrato(getString("") );
    aModel.setCognome(getString("COGNOME") );
    aModel.setNome(getString("NOME") );
    aModel.setFlagStato(getString("FLAG_STATO") );
    aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA") );
    //aModel.setDescrUfficioAppartenenza( getString("") );
    aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA") );
    aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setEMailUfficio(getString("E_MAIL_UFFICIO") );
    aModel.setEMailPrivata(getString("E_MAIL_PRIVATA") );
    aModel.setNumCellulare(getString("NUM_CELLULARE") );
    aModel.setDataNascita(getDate("DATA_NASCITA"));


    return aModel;
  }

  /**
   * 
   * @param aCognome
   * @param aUfficio
   * @throws DAOException
   */
  public void ricercaMagistratoByCognomeCodUfficio( String  aCognome, String aUfficio)   throws DAOException
  {
    String lSql = getSqlQuery();
    
    if (aCognome != null)
    {
      lSql += " " + "WHERE MAGISTRATO.COGNOME LIKE '" + aCognome + "%'";
      lSql += " AND W_MAGISTRATO.COD_MAGISTRATO = MAGISTRATO.COD_MAGISTRATO ";
      lSql += " AND MAGISTRATO.COD_UFFICIO_APPARTENENZA = '" + aUfficio +"'";
      
      lSql += " ORDER BY MAGISTRATO.COGNOME";
    }
      
    setStatement(lSql);
  }

}
