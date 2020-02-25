package siap.sius.esperto.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sius.esperto.model.EspertoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: EspertoSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class EspertoSqlDAO extends SIAPSqlDAO
{
  public EspertoSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //
  public void ricercaEsperto(EspertoModel aModel)
  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += "  " + setCondizione(aModel);
    lSql += " ORDER BY COGNOME ";
    setStatement(lSql);
  }


  public void ricercaEspertoByKey(BigDecimal aKey)
  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " WHERE " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }

  public void ricercaEspertoByCodUfficio(String aCodUfficio) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " WHERE COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
    lSql += " OR COD_UFFICIO_APPARTENENZA IS NULL";
    lSql += " ORDER BY COGNOME";
    setStatement(lSql);
  }

  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_ESPERTO, "+
                  "COGNOME, "+
                  "NOME, "+
                  "INDIRIZZO, "+
                  "TELEFONO, "+
                  "COD_UFFICIO_APPARTENENZA, "+
                  "EMAIL, "+
                  "FAX, "+
                  "CELLULARE, "+
                  "DATA_INIZIO_VALIDITA, "+
                  "DATA_FINE_VALIDITA, "+
                  "FLAG_STATO, "+
                  "COD_OPERATORE_INSERIMENTO, "+
                  "DATA_INSERIMENTO, "+
                  "COD_UFFICIO_INSERIMENTO, "+
                  "COD_OPERATORE_AGGIORNAMENTO, "+
                  "DATA_AGGIORNAMENTO, "+
                  "COD_UFFICIO_AGGIORNAMENTO, "+
                  "CODICE_FISCALE " +
                  " FROM ESPERTO ";
    return lStatement;
  }


  //
  // METODO GETMODEL()
  //
  public GenericModel getModel()
  throws DAOException
  {
    EspertoModel aModel = new  EspertoModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdEsperto(getBigDecimal("ID_ESPERTO") );
    aModel.setCognome(getString("COGNOME") );
    aModel.setNome(getString("NOME") );
    aModel.setIndirizzo(getString("INDIRIZZO") );
    aModel.setTelefono(getString("TELEFONO") );
    aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA") );
    //aModel.setDescrUfficioAppartenenza(getString("") );
    aModel.setEmail(getString("EMAIL") );
    aModel.setFax(getString("FAX") );
    aModel.setCellulare(getString("CELLULARE") );
    aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA") );
    aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA") );
    aModel.setFlagStato(getString("FLAG_STATO") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setCodiceFiscale(getString("CODICE_FISCALE") );
    
    return aModel;
  }

  public String  setCondizione(EspertoModel aModel)
  {
    String lCondizioni = new String();
    boolean lInserito = false;

    // Imposta la condizione di filtro sul codice Ufficio di appartenenza.
    if((aModel.getCodUfficioAppartenenza()).length() != 0)
    {
      lCondizioni = " COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() +"'";
      lInserito = true;
    }

    // Imposta la condizione di filtro sul Cognome
    if((aModel.getCognome()).length() != 0)
    {
      if(lInserito)
        lCondizioni += " AND";
      lCondizioni += " COGNOME LIKE '" + aModel.getCognome() + "%'";

      lInserito = true;
    }

    // Imposta la condizione di filtro sul nome
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

  /**
   * Metodo che imposta il filtro di condizione con l'id
   * <p>
   * @param aKey BigDecimal id esperto.
   * @return String stringa di ritorno con la condizione.
   */
  public String setCondizioniByKey(BigDecimal aKey)
  {
    return " ID_ESPERTO = " + aKey;
  }
}