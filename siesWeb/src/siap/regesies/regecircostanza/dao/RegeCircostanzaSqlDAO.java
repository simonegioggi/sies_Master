package siap.regesies.regecircostanza.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.regesies.regecircostanza.model.RegeCircostanzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>Title: RegeCircostanzaSqlDAO</p>
 * <p>Description: Classe SqlDAO che rappresenta la tabella RegeCircostanza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class RegeCircostanzaSqlDAO extends SIAPSqlDAO
{
  public RegeCircostanzaSqlDAO(Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //

  public void ricercaRegeCircostanza(String aKey) throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizione(aKey);
    setStatement(lSql);
  }

  public void ricercaRegeCircostanzaByKey(String aKey, int aProgr) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " AND ID_FILE = '" + aKey+"'";
    lSql += " AND PROGR_CIRCOSTANZA = " + aProgr;
    setStatement(lSql);
  }

  public void ricercaRegeCircostanzaByIdFile(String aKey) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " AND ID_FILE = '" + aKey+"'";
    lSql += " ORDER by PROGR_CIRCOSTANZA";

    setStatement(lSql);
  }

  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
      "ID_FILE, " +
      "PROGR_CIRCOSTANZA, " +
      "COD_TIPO_CIRCOSTANZA, " +
      "COD_FONTE, " +
      "ANNO_FONTE, " +
      "NUMERO_FONTE, " +
      "COD_SOTTONUMERAZIONE, " +
      "COMMA, " +
      "LETTERA, " +
      "NUMERO, " +
      "ARTICOLO, " +
      "COD_OPERATORE_INSERIMENTO, " +
      "DATA_INSERIMENTO, " +
      "COD_UFFICIO_INSERIMENTO, " +
      "COD_OPERATORE_AGGIORNAMENTO, " +
      "DATA_AGGIORNAMENTO, " +
      "COD_UFFICIO_AGGIORNAMENTO, ";
    lStatement += "DECOFONTE.RV_MEANING DESCFONTE, ";
    lStatement += "DECOSOTTONUM.RV_MEANING DESCSOTTONUM ";
    lStatement += " FROM REGE_CIRCOSTANZA, CG_REF_CODES DECOSOTTONUM, CG_REF_CODES DECOFONTE ";
    lStatement += " WHERE ";
    lStatement += "DECOSOTTONUM.RV_DOMAIN='SOTTONUMERAZIONE' AND DECOSOTTONUM.RV_LOW_VALUE=REGE_CIRCOSTANZA.COD_SOTTONUMERAZIONE ";
    lStatement += "AND DECOFONTE.RV_DOMAIN='FONTE' AND DECOFONTE.RV_LOW_VALUE=REGE_CIRCOSTANZA.COD_FONTE ";
    return lStatement;
  }

  //
  // METODO GETMODEL()
  //

  public GenericModel getModel() throws DAOException
  {
    RegeCircostanzaModel aModel = new RegeCircostanzaModel();

//Inserire le opportune set delle descrizioni!
    aModel.setIdFile(getString("ID_FILE"));
    aModel.setProgrCircostanza(getInt("PROGR_CIRCOSTANZA"));
    aModel.setCodTipoCircostanza(getString("COD_TIPO_CIRCOSTANZA"));
    //--- aModel.setDescrTipoCircostanza(getString("") );
    aModel.setCodFonte(getString("COD_FONTE"));
    aModel.setDescrFonte(getString("DESCFONTE"));
    aModel.setAnnoFonte(getInt("ANNO_FONTE"));
    aModel.setNumeroFonte(getString("NUMERO_FONTE"));
    aModel.setCodSottonumerazione(getString("COD_SOTTONUMERAZIONE"));
    aModel.setDescrSottonumerazione(getString("DESCSOTTONUM"));
    aModel.setComma(getString("COMMA"));
    aModel.setLettera(getString("LETTERA"));
    aModel.setNumero(getString("NUMERO"));
    aModel.setArticolo(getString("ARTICOLO"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    //aModel.setDescrUfficioInserimento(getString(""));
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
    //	 aModel.setDescrUfficioAggiornamento(getString("") );
    return aModel;
  }

  public String setCondizione(String aIdFile)
  {
    String lCondizioni = new String();

    lCondizioni = " AND ID_FILE = '" + aIdFile+"'";


    return lCondizioni;
  }

  public String setCondizioniByKey(String aKey)
  {
    return " AND ID_FILE = " + aKey;
  }
}