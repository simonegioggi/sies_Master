package siap.regesies.regereato.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.regesies.regereato.model.RegeReatoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>Title: RegeReatoSqlDAO</p>
 * <p>Description: Classe SqlDAO che rappresenta la tabella RegeReato</p>
 * <p>Copyright: Copyright (c) 2006</p>
 */
public class RegeReatoSqlDAO extends SIAPSqlDAO
{
  public RegeReatoSqlDAO(Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //

  public void ricercaRegeReato(String aKey) throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizione(aKey);
    lSql += " ORDER by PROGR_REATO,PROGR_CIRCOSTANZA";
    setStatement(lSql);
  }

  public void ricercaRegeReatoByKey(String aKey, int aProgr, int aCircProgr) throws DAOException
  {
    String lSql = getSqlQuery();

    String lCondizioni = " AND ID_FILE = '" + aKey + "'";
    lCondizioni += " AND PROGR_REATO = " + aProgr;
    lCondizioni += " AND PROGR_CIRCOSTANZA = " + aCircProgr;
    lSql += " " + lCondizioni;
    setStatement(lSql);
  }

  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
      "ID_FILE, " +
      "COD_TIPO_REATO, " +
      "DATA_REATO, " +
      "PROGR_NUMERO_MANUALE, " +
      "PROGR_REATO, " +
      "PROGR_CIRCOSTANZA, " +
      "DATA_INIZIO, " +
      "ANNO_INIZIO, " +
      "MESE_INIZIO, " +
      "GIORNO_INIZIO, " +
      "DATA_FINE, " +
      "ANNO_FINE, " +
      "MESE_FINE, " +
      "GIORNO_FINE, " +
      "COD_PERIODO_CONSUMAZIONE, " +
      "DESC_LUOGO, " +
      "COD_FONTE, " +
      "ANNO_FONTE, " +
      "NUMERO_FONTE, " +
      "COD_SOTTONUMERAZIONE, " +
      "COMMA, " +
      "LETTERA, " +
      "NUMERO, " +
      "ARTICOLO, " +
      "NOTE, " +
      "COD_OPERATORE_INSERIMENTO, " +
      "DATA_INSERIMENTO, " +
      "COD_UFFICIO_INSERIMENTO, " +
      "COD_OPERATORE_AGGIORNAMENTO, " +
      "DATA_AGGIORNAMENTO, " +
      "COD_UFFICIO_AGGIORNAMENTO, " +
      "NOTA_QGF,";
    lStatement += "DECOTIPOREATO.RV_MEANING DESCTIPOREATO,";
    lStatement += "DECOPERCONS.RV_MEANING DESCPECONS,";
    lStatement += "DECOSOTTONUM.RV_MEANING DESCSOTTONUM, ";
    lStatement += "DECOFONTE.RV_MEANING DESCFONTE ";
    lStatement += "FROM REGE_REATO, CG_REF_CODES DECOTIPOREATO, CG_REF_CODES DECOPERCONS, CG_REF_CODES DECOSOTTONUM,  CG_REF_CODES DECOFONTE ";
    lStatement += " WHERE ";
    lStatement += "DECOTIPOREATO.RV_DOMAIN='TIPO_REATO' AND DECOTIPOREATO.RV_LOW_VALUE=REGE_REATO.COD_TIPO_REATO ";
    lStatement += "AND DECOPERCONS.RV_DOMAIN='PERIODO_CONSUMAZIONE' AND DECOPERCONS.RV_LOW_VALUE=REGE_REATO.COD_PERIODO_CONSUMAZIONE ";
    lStatement += "AND DECOSOTTONUM.RV_DOMAIN='SOTTONUMERAZIONE' AND DECOSOTTONUM.RV_LOW_VALUE=REGE_REATO.COD_SOTTONUMERAZIONE ";
    //lStatement += "";
    lStatement += "AND DECOFONTE.RV_DOMAIN='FONTE' AND DECOFONTE.RV_LOW_VALUE=REGE_REATO.COD_FONTE ";
    //lStatement += " WHERE ";
    return lStatement;
  }

  //
  // METODO GETMODEL()
  //

  public GenericModel getModel() throws DAOException
  {
    RegeReatoModel aModel = new RegeReatoModel();

//Inserire le opportune set delle descrizioni!
    aModel.setIdFile(getString("ID_FILE"));
    aModel.setCodTipoReato(getString("COD_TIPO_REATO"));
    aModel.setDescrTipoReato(getString("DESCTIPOREATO"));
    aModel.setDataReato(getDate("DATA_REATO"));
    aModel.setProgrNumeroManuale(getString("PROGR_NUMERO_MANUALE"));
    aModel.setProgrReato(getInt("PROGR_REATO"));
    aModel.setProgrCircostanza(getInt("PROGR_CIRCOSTANZA"));
    aModel.setDataInizio(getDate("DATA_INIZIO"));
    aModel.setAnnoInizio(getInt("ANNO_INIZIO"));
    aModel.setMeseInizio(getInt("MESE_INIZIO"));
    aModel.setGiornoInizio(getInt("GIORNO_INIZIO"));
    aModel.setDataFine(getDate("DATA_FINE"));
    aModel.setAnnoFine(getInt("ANNO_FINE"));
    aModel.setMeseFine(getInt("MESE_FINE"));
    aModel.setGiornoFine(getInt("GIORNO_FINE"));
    aModel.setCodPeriodoConsumazione(getString("COD_PERIODO_CONSUMAZIONE"));
    aModel.setDescrPeriodoConsumazione(getString("DESCPECONS"));
    aModel.setDescLuogo(getString("DESC_LUOGO"));
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
    aModel.setNote(getString("NOTE"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
    aModel.setNotaQgf(getString("NOTA_QGF"));

    return aModel;
  }

  public String setCondizione(String aIdFile)
  {
    String lCondizioni = "AND iD_FILE = '" + aIdFile + "'";
    return lCondizioni;
  }

  /**
   * Seleziona il reato da chiave e progressivo
   * @param aKey - chiave id file
   * @param aProgr - progressivo circostanza
   * @return Stringa
   */
  public String setCondizioniByKey(String aKey, int aProgr)
  {
    String lCondizioni = " AND ID_FILE = '" + aKey + "'";
    lCondizioni += " AND PROGR_CIRCOSTANZA = " + aProgr;
    return lCondizioni;
  }

  /**
   * ricerca RegeReato By Provvedimento
   * @param aKey - chiave file
   */
  public void ricercaRegeReatoByProvvedimento(String aKey)
  {
    String lSql = getSqlQuery();

    lSql += " AND PROGR_CIRCOSTANZA = 1";
    //I record con PROGR_CIRCOSTANZA = 1 sono reati e non circostanze
    lSql += " AND ID_FILE = '" + aKey + "'";
    lSql += " ORDER by PROGR_REATO,PROGR_CIRCOSTANZA";

    setStatement(lSql);
  }

  /**
   * ricerca Circostanze Reato By Provvedimento
   * @param aNumReato - numero reato
   * @param aKey - chiave file
   */
  public void ricercaCircostanzeReatoByProvvedimento(int aNumReato, String aKey)
  {
    String lSql = getSqlQuery();
    lSql += " AND PROGR_REATO = " + aNumReato;
    lSql += " AND PROGR_CIRCOSTANZA != 1";
    //I record con PROGR_CIRCOSTANZA = 1 sono reati e non circostanze
    lSql += " AND ID_FILE = '" + aKey + "'";
    lSql += " ORDER by PROGR_REATO,PROGR_CIRCOSTANZA";


    setStatement(lSql);
  }

}