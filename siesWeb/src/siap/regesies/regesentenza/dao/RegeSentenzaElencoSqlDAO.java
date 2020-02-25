package siap.regesies.regesentenza.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.web.IWebConstants;

/**
 * <p>Title: RegeSentenzaSqlDAO</p>
 * <p>Description: Classe SqlDAO che realizza le query di elenco sulla tabella rege_sentenza</p>
 */
public class RegeSentenzaElencoSqlDAO extends SIAPSqlDAO
{

  /**
   * Sql Elenco Provvedimenti con filtro su Codice COmune
   * @param aCodComune
   * @return string sql
   */
  private String getSqlElencoProvv(String aCodComune)
  {
    String lSql = getSqlQuery();
    lSql += " AND REGE_FILE.COD_COMUNE = '" + aCodComune + "'";
    //Group by
    lSql += "  GROUP BY  cod_tipo_provvedimento, tipo_provvedimento.rv_meaning," +
      " cod_tipo_autorita_emittente,data_provvedimento," +
      " cod_luogo_emittente,  luogo_emittente.descrizione," +
      " anno_sentenza, numero_sentenza, tipo_autorita_emittente.rv_meaning";

    return lSql;
  }

  /**
   * get Sql per Uffici di secondo grado senza filtro su comune
   * @return string sql
   */
  private String getSqlElencoProvvSecondoGrado()
  {
    String lSql = getSqlQueryPerRicercaEstremi();
    //  lSql += " AND REGE_FILE.COD_COMUNE = '" + aCodComune + "'";
    //Group by
    lSql += "  GROUP BY  cod_tipo_provvedimento, tipo_provvedimento.rv_meaning," +
      " cod_tipo_autorita_emittente,data_provvedimento," +
      " cod_luogo_emittente,  luogo_emittente.descrizione," +
      " anno_sentenza, numero_sentenza, tipo_autorita_emittente.rv_meaning";

    return lSql;
  }

  /**
   * elenco Provvedimenti
   * @param aCodComune
   * @throws DAOException
   */
  public void elencoProvvedimenti(String aCodComune) throws DAOException
  {
    String lSql = getSqlElencoProvv(aCodComune);

    setStatement(lSql);

  }

  /**
   * getCountProvvedimenti
   * Conta i provvedimenti per la ricerca
   * @param aCodComune
   */
  public void getCountProvvedimenti(String aCodComune)
  {
    String lStatement = "SELECT COUNT(*) HowManyRecords ";
    lStatement += " FROM (";
    String lSql = getSqlElencoProvv(aCodComune);
    lStatement += lSql + " )";
    setStatement(lStatement);
  }

  public void getCountProvvedimenti()
  {
    String lStatement = "SELECT COUNT(*) HowManyRecords ";
    lStatement += " FROM (";
    String lSql = getSqlElencoProvvSecondoGrado();
    lStatement += lSql + " )";
    setStatement(lStatement);
  }

  public RegeSentenzaElencoSqlDAO(Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //
  public void ricercaRegeSentenza(RegeSentenzaModel aModel) throws DAOException
  {
    String lSql = getSqlQueryPerRicercaEstremi();
    lSql += " AND ANNO_SENTENZA = " + aModel.getAnnoSentenza() +
      " AND NUMERO_SENTENZA = '" + aModel.getNumeroSentenza() + "'" +
      " AND LUOGO_EMITTENTE.DESCRIZIONE = '" + aModel.getCodLuogoEmittente() + "'" +
      " AND COD_TIPO_AUTORITA_EMITTENTE = '" + aModel.getCodTipoAutoritaEmittente() + "'";
    // lSql += " AND REGE_FILE.COD_COMUNE = '" + aCodComune + "'";
    //Group by
    lSql += "  GROUP BY  cod_tipo_provvedimento, tipo_provvedimento.rv_meaning," +
      " cod_tipo_autorita_emittente,data_provvedimento," +
      " cod_luogo_emittente,  luogo_emittente.descrizione," +
      " anno_sentenza, numero_sentenza, tipo_autorita_emittente.rv_meaning";

    setStatement(lSql);
  }

  public void ricercaPaginataProvvedimento(int aPage, String aComune) throws DAOException
  {

    String lStatement = new String("SELECT * FROM (");

    lStatement += " Select countSogg, cod_tipo_provvedimento,descr_tipo_provvedimento, " +
      " cod_tipo_autorita_emittente,data_provvedimento,descr_tipo_autorita_emittente," +
      "cod_luogo_emittente, descr_luogo_emittente,anno_sentenza, numero_sentenza, ROWNUM rn ";
    lStatement += " FROM (" + getSqlElencoProvv(aComune);
    lStatement += " ) inner ) WHERE rn BETWEEN " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) +
      " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

    setStatement(lStatement);
  }

  public void ricercaPaginataProvvedimentoSecGrado(int aPage) throws DAOException
  {
    String lStatement = new String("SELECT * FROM (");

    lStatement += " Select countSogg, cod_tipo_provvedimento,descr_tipo_provvedimento, " +
      " cod_tipo_autorita_emittente,data_provvedimento,descr_tipo_autorita_emittente," +
      "cod_luogo_emittente, descr_luogo_emittente,anno_sentenza, numero_sentenza, ROWNUM rn ";
    lStatement += " FROM (" + getSqlElencoProvvSecondoGrado();
    lStatement += " ) inner ) WHERE rn BETWEEN " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) +
      " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

    setStatement(lStatement);
  }

  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement = "select count(rege_sentenza.id_file) countSogg, cod_tipo_provvedimento," +
      "tipo_provvedimento.rv_meaning descr_tipo_provvedimento, " +
      "cod_tipo_autorita_emittente," +
      "data_provvedimento," +
      "tipo_autorita_emittente.rv_meaning descr_tipo_autorita_emittente," +
      "cod_luogo_emittente, " +
      "luogo_emittente.descrizione descr_luogo_emittente," +
      "anno_sentenza, numero_sentenza     " +
      "FROM rege_sentenza, cg_ref_codes tipo_provvedimento," +
      "cg_ref_codes tipo_autorita_emittente," +
      "comune luogo_emittente," +
      "rege_file" +
      " WHERE (tipo_provvedimento.rv_domain = 'TIPO_PROVVEDIMENTO'" +
      " AND tipo_provvedimento.rv_low_value = cod_tipo_provvedimento)" +
      " AND (tipo_autorita_emittente.rv_domain = 'TIPO_UFFICIO'" +
      " AND tipo_autorita_emittente.rv_low_value = cod_tipo_autorita_emittente)" +
      " AND (luogo_emittente.cod_comune = cod_luogo_emittente)" +
      " AND rege_file.id_file = rege_sentenza.id_file";

    return lStatement;
  }

  protected String getSqlQueryPerRicercaEstremi()
  {
    String lStatement = new String("");

    lStatement = "select count(rege_sentenza.id_file) countSogg," +
      "cod_tipo_provvedimento," +
      "tipo_provvedimento.rv_meaning descr_tipo_provvedimento, " +
      "cod_tipo_autorita_emittente," +
      "data_provvedimento," +
      "tipo_autorita_emittente.rv_meaning descr_tipo_autorita_emittente," +
      "cod_luogo_emittente, " +
      "luogo_emittente.descrizione descr_luogo_emittente," +
      "anno_sentenza, numero_sentenza " +
      "FROM rege_sentenza, cg_ref_codes tipo_provvedimento," +
      "cg_ref_codes tipo_autorita_emittente," +
      "comune luogo_emittente" +
      " WHERE (tipo_provvedimento.rv_domain = 'TIPO_PROVVEDIMENTO'" +
      " AND tipo_provvedimento.rv_low_value = cod_tipo_provvedimento)" +
      " AND (tipo_autorita_emittente.rv_domain = 'TIPO_UFFICIO'" +
      " AND tipo_autorita_emittente.rv_low_value = cod_tipo_autorita_emittente)" +
      " AND (luogo_emittente.cod_comune = cod_luogo_emittente)";
    //  " AND rege_file.id_file = rege_sentenza.id_file";

    return lStatement;
  }

  public GenericModel getModel() throws DAOException
  {
    RegeSentenzaModel aModel = new RegeSentenzaModel();

    //  aModel.setIdFile(getString("ID_FILE"));
    aModel.setCountSoggetti(getInt("countSogg"));
    aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
    aModel.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
    aModel.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));

    aModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
    aModel.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
    aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
    aModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));

    aModel.setAnnoSentenza(getInt("ANNO_SENTENZA"));
    aModel.setNumeroSentenza(getString("NUMERO_SENTENZA"));

    return aModel;
  }

  public String setCondizione(RegeSentenzaModel aModel)
  {
    String lCondizioni = new String();

    lCondizioni += " AND ANNO_SENTENZA = " + aModel.getAnnoSentenza() +
      " AND NUMERO_SENTENZA = '" + aModel.getNumeroSentenza() + "'" +
      " AND LUOGO_EMITTENTE.DESCRIZIONE = '" + aModel.getCodLuogoEmittente() + "'" +
      " AND COD_TIPO_AUTORITA_EMITTENTE = '" + aModel.getCodTipoAutoritaEmittente() + "'";

    return lCondizioni;
  }

  public String setCondizioniByKey(String aKey)
  {
    return " AND ID_FILE = '" + aKey + "'";
  }

}