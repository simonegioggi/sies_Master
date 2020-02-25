package siap.sige.sezione.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sige.sezione.model.SezioneModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
/**
* <p>Title: SezioneSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Sezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class SezioneSqlDAO extends SIAPSqlDAO
{
  public SezioneSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //
  public void ricercaSezione(SezioneModel aModel)
  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += "  " + setCondizione(aModel);
    lSql += " ORDER BY CODICE ";
    setStatement(lSql);
  }


  public void ricercaSezioneByKey(BigDecimal aKey)
  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " WHERE " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }

  public void ricercaSezioneByCodUfficio(String aCodUfficio) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " WHERE COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
    lSql += " ORDER BY CODICE";
    setStatement(lSql);
  }

  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += "SELECT " +
                     " ID_SEZIONE, " +
                     " CODICE, " +
                     " DESCRIZIONE, " +
                     " COD_UFFICIO_APPARTENENZA, " +
                     " COD_OPERATORE_INSERIMENTO, " +
                     " DATA_INSERIMENTO, " +
                     " COD_UFFICIO_INSERIMENTO, " +
                     " COD_OPERATORE_AGGIORNAMENTO, " +
                     " DATA_AGGIORNAMENTO, " +
                     " COD_UFFICIO_AGGIORNAMENTO " +
                 "FROM SEZIONE SEZ " ;    
    return lStatement;
  }


  //
  // METODO GETMODEL()
  //
  public GenericModel getModel()
  throws DAOException
  {
    SezioneModel aModel = new  SezioneModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdSezione(getBigDecimal("ID_SEZIONE") );
    aModel.setCodice(getString("CODICE") );
    aModel.setDescrizione(getString("DESCRIZIONE") );
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

  public String  setCondizione(SezioneModel aModel)
  {
    String lCondizioni = new String();
    boolean lInserito = false;

    // Imposta la condizione di filtro sul codice Ufficio di appartenenza.
    if((aModel.getCodUfficioAppartenenza()).length() != 0)
    {
      lCondizioni = " COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() +"'";
      lInserito = true;
    }

    // Imposta la condizione di filtro sul Codice
    if((aModel.getCodice()).length() != 0)
    {
      if(lInserito)
        lCondizioni += " AND";
      lCondizioni += " CODICE LIKE '" + aModel.getCodice() + "%'";

      lInserito = true;
    }

    // Imposta la condizione di filtro sulla Descrizione
    if((aModel.getDescrizione()).length() != 0)
    {
      if(lInserito)
        lCondizioni += " AND";
      lCondizioni += " DESCRIZIONE LIKE '" + aModel.getDescrizione() + "%'";

      lInserito = true;
    }

    if(lInserito)
      lCondizioni = " WHERE " + lCondizioni;

    return lCondizioni;
  }

  /**
   * Metodo che imposta il filtro di condizione con l'id
   * <p>
   * @param aKey BigDecimal id sezione.
   * @return String stringa di ritorno con la condizione.
   */
  public String setCondizioniByKey(BigDecimal aKey)
  {
    return " ID_SEZIONE = " + aKey;
  }
  
}