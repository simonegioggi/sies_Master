package siap.siepe.assistentesociale.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siepe.assistentesociale.model.AssistenteSocialeModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: AssistenteSocialeSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella AssistenteSociale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class AssistenteSocialeSqlDAO extends SIAPSqlDAO
{
  /**
   * Costruttore di classe, con parametro.
   * <p>
   * @param con Connection
   */
  public AssistenteSocialeSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //
  /**
   * Ricerca assistenti sociali ordinati per cognome.
   * <p>
   * @param aModel AssistenteSocialeModel
   * @throws DAOException propaga errori di eccezione.
   */
  public void ricercaAssistenteSociale( AssistenteSocialeModel  aModel)
  throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += "  " + setCondizione(aModel);
    lSql += " ORDER BY COGNOME ";

    setStatement(lSql);
  }

  /**
   * Ricerca l'assistente sociale per il proprio ID
   * <p>
   * @param aKey BigDecimal Id dell'assistente sociale.
   * @throws DAOException propaga errore di eccezione.
   */
  public void ricercaAssistenteSocialeByKey( BigDecimal aKey)
  throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " WHERE " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }

  /**
   * Metodo che imposta le condizioni di flitro per ricercare Assistenti Sociali
   * filtrati per il codice ufficio di appartenenza.
   * <p>
   * @param aCodUfficio Codice Ufficio.
   * @throws DAOException propaga errore di eccezione.
   */
  public void ricercaAssistenteSocialeByCodUfficio( String aCodUfficio )
  throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " WHERE COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
    lSql += " OR COD_UFFICIO_APPARTENENZA IS NULL";
    lSql += " ORDER BY COGNOME";

    setStatement( lSql );
  }

  /**
   * Metodo che crea la stringa sql da ritornare.
   * <p>
   * @return String statement sql.
   */
  protected String getSqlQuery()
  {
    String lStatement = new String();
    lStatement += "SELECT " +
                  "ID_ASSISTENTE_SOCIALE, " +
                  "COGNOME, " +
                  "NOME, " +
                  "INDIRIZZO, " +
                  "TELEFONO, " +
                  "COD_UFFICIO_APPARTENENZA, " +
                  "EMAIL, " +
                  "FAX, " +
                  "CELLULARE, " +
                  "DATA_INIZIO_VALIDITA, " +
                  "DATA_FINE_VALIDITA, " +
                  "FLAG_STATO, " +
                  "FLAG_STATO.RV_MEANING DESCR_FLAG_STATO, " +
                  "COD_OPERATORE_INSERIMENTO, " +
                  "DATA_INSERIMENTO, " +
                  "COD_UFFICIO_INSERIMENTO, " +
                  "COD_OPERATORE_AGGIORNAMENTO, " +
                  "DATA_AGGIORNAMENTO, " +
                  "COD_UFFICIO_AGGIORNAMENTO, " +
                  "CODICE_FISCALE " +
                  "FROM ASSISTENTE_SOCIALE " +
                  "INNER JOIN CG_REF_CODES FLAG_STATO ON FLAG_STATO.RV_DOMAIN = 'FLAG_STATO' AND FLAG_STATO.RV_LOW_VALUE = ASSISTENTE_SOCIALE.FLAG_STATO " ;

    return lStatement;
  }


  //
  // METODO GETMODEL()
  //

  /**
   * Metodo che ritorna il AssisteteSocialeModel, opportunamente popolato.
   * <p>
   * @throws DAOException propaga errore di eccezione
   * @return GenericModel ritorna il model popolato.
   */
  public GenericModel  getModel() throws DAOException
  {
    AssistenteSocialeModel aModel = new  AssistenteSocialeModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdAssistenteSociale(getBigDecimal("ID_ASSISTENTE_SOCIALE") );
    aModel.setCognome(getString("COGNOME") );
    aModel.setNome(getString("NOME") );
    aModel.setIndirizzo(getString("INDIRIZZO") );
    aModel.setTelefono(getString("TELEFONO") );
    aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA") );
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
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    aModel.setCodiceFiscale(getString("CODICE_FISCALE") );

    return aModel;
  }

  /**
   * Imposta le condizioni di filtro, prendento come parametro il model.
   * <p>
   * @param aModel AssistenteSocialeModel Oggetto Model.
   * @return String Stringa con le condizioni di filtro.
   */
  public String  setCondizione(AssistenteSocialeModel aModel)
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

  /**
   * Imposta la condizione di filtro sulla chiave.
   * <p>
   * @param aKey BigDecimal chiave
   * @return String stringa di ritorno.
   */
  public String setCondizioniByKey(BigDecimal aKey)
  {
    return " ID_ASSISTENTE_SOCIALE = " + aKey;
  }

}
