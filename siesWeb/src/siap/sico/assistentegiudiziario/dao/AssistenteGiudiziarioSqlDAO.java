package siap.sico.assistentegiudiziario.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import f3b.dao.DAOException;
//import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: AssistenteGiudiziarioSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella AssistenteGiudiziario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AssistenteGiudiziarioSqlDAO extends SIAPSqlDAO
{
  public AssistenteGiudiziarioSqlDAO (Connection con)
  {
    super(con);
  }


 //
  // METODO RICERCA()
  //


  public void ricercaAssistenteGiudiziario( AssistenteGiudiziarioModel  aModel)	 throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizione(aModel);
    setStatement(lSql);
  }

  /**
   *
   * @param aCodUfficio
   * @throws DAOException
   */
  public void ricercaAssistenteGiudiziarioByCodUfficio( String aCodUfficio ) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " WHERE COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
    lSql += " OR COD_UFFICIO_APPARTENENZA IS NULL";
    lSql += " ORDER BY COGNOME";
    setStatement( lSql );
  }


  public void ricercaAssistenteGiudiziarioByKey( BigDecimal aKey)	 throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " WHERE  " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }


  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_ASSISTENTE_GIUDIZIARIO, "+
                  "COGNOME, "+
                  "NOME, "+
                 "COD_UFFICIO_APPARTENENZA, "+
                 "FLAG_STATO, "+
                 "DATA_INIZIO_VALIDITA, "+
                 "DATA_FINE_VALIDITA, "+
                 "COD_OPERATORE_INSERIMENTO, "+
                 "DATA_INSERIMENTO, "+
                 "COD_UFFICIO_INSERIMENTO, "+
                 "COD_OPERATORE_AGGIORNAMENTO, "+
                 "DATA_AGGIORNAMENTO, "+
                 "COD_UFFICIO_AGGIORNAMENTO ";
    lStatement += " FROM ASSISTENTE_GIUDIZIARIO";
     //lStatement += " WHERE ";
    return lStatement;
  }


 //
  // METODO GETMODEL()
  //


  public GenericModel  	 getModel() throws DAOException
  {
    AssistenteGiudiziarioModel aModel = new  AssistenteGiudiziarioModel();

//Inserire le opportune set delle descrizioni!
    aModel.setIdAssistenteGiudiziario(getBigDecimal("ID_ASSISTENTE_GIUDIZIARIO") );
    aModel.setCognome(getString("COGNOME") );
    aModel.setNome(getString("NOME") );
    aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA") );
    //aModel.setDescrUfficioAppartenenza(getString("") );
    aModel.setFlagStato(getString("FLAG_STATO") );
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
    return aModel;
  }


  public String  setCondizione(AssistenteGiudiziarioModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;

    if((aModel.getCodUfficioAppartenenza()).length() != 0)
    {
      lCondizioni = " COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() +"'";
      lInserito = true;
    }
    if((aModel.getCognome()).length() != 0)
    {
      if(lInserito)
        lCondizioni += " AND";
      lCondizioni += " COGNOME LIKE '" + aModel.getCognome() + "%'";
      lInserito = true;
    }
    if((aModel.getNome()).length() != 0)
    {
      if(lInserito)
        lCondizioni += " AND";
      lCondizioni += " NOME LIKE '" + aModel.getNome() + "%'";
      lInserito = true;
    }
    if(lInserito)
      lCondizioni = " WHERE " + lCondizioni;
    return lCondizioni;
  }


  public String setCondizioniByKey(BigDecimal aKey)
  {
    return " ID_ASSISTENTE_GIUDIZIARIO = " + aKey;
  }
}
