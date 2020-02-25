package siap.sige.curatore.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sige.curatore.model.CuratoreModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: CuratoreSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Curatore</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class CuratoreSqlDAO extends SIAPSqlDAO
{
  public CuratoreSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //
  public void ricercaCuratore(CuratoreModel aModel)
  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += "  " + setCondizione(aModel);
    lSql += " ORDER BY COGNOME ";
    setStatement(lSql);
  }


  public void ricercaCuratoreByKey(BigDecimal aKey)
  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " WHERE " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }

  public void ricercaCuratoreByCodUfficio(String aCodUfficio) throws DAOException
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

    lStatement += "SELECT " +
                     " ID_CURATORE, "+
                     " COGNOME, "+
                     " NOME, "+
                     " INDIRIZZO, "+
                     " TELEFONO, "+
                     " COD_UFFICIO_APPARTENENZA, "+
                     " EMAIL, "+
                     " FAX, "+
                     " CELLULARE, "+
                     " DATA_INIZIO_VALIDITA, "+
                     " DATA_FINE_VALIDITA, "+
                     " FLAG_STATO, "+
                     " DISP.RV_MEANING AS DESCR_FLAG_STATO, " +
                     " COD_OPERATORE_INSERIMENTO, "+
                     " DATA_INSERIMENTO, "+
                     " COD_UFFICIO_INSERIMENTO, "+
                     " COD_OPERATORE_AGGIORNAMENTO, "+
                     " DATA_AGGIORNAMENTO, "+
                     " COD_UFFICIO_AGGIORNAMENTO, "+
                     " CODICE_FISCALE " +
                 "FROM CURATORE CUR " +
                     " INNER JOIN CG_REF_CODES DISP ON DISP.RV_LOW_VALUE = CUR.FLAG_STATO " +
                     " AND DISP.RV_DOMAIN = 'FLAG_STATO' " ;    
    return lStatement;
  }


  //
  // METODO GETMODEL()
  //
  public GenericModel getModel()
  throws DAOException
  {
    CuratoreModel aModel = new  CuratoreModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdCuratore(getBigDecimal("ID_CURATORE") );
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
    aModel.setDescrFlagStato(getString("DESCR_FLAG_STATO") );
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

  public String  setCondizione(CuratoreModel aModel)
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
    return " ID_CURATORE = " + aKey;
  }
}