package siap.siep.rateizzazionepp.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.dao.SIAPSqlDAO;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: RateizzazionePPSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RATEIZZAZIONE_PP
 * </p>
 * @version 1.0
 */
public class RateizzazionePPSqlDAO extends SIAPSqlDAO {

  private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

  /**
   * Costruttore
   * 
   * @param con
   */
  public RateizzazionePPSqlDAO(Connection con) {
    super(con);
  }

  /**
   * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
   * model utilizzato per la ricerca
   * 
   * @param aModel
   * @throws DAOException
   */
  public void getCountRateizzazionePP (RateizzazionePPModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire
    String lStatement = "SELECT COUNT(*) HowManyRecords FROM RATEIZZAZIONE_PP ";

    // Recupero la where condition in base al model
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals(""))
      lStatement += " WHERE " + lCondizioni;

    // Imposta lo statement da eseguire
    setStatement(lStatement);
  }

  /**
   * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
   * passata in input
   * 
   * @param aModel
   * @param aPage
   * @throws DAOException
   */
  public void ricercaRateizzazionePPPaged (RateizzazionePPModel aModel, int aPage)
      throws DAOException {
    String lStatement = new String("");

    lStatement += getSqlQuery();

    // Recupero la where condition in base al model
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals(""))
      lStatement += " WHERE " + lCondizioni;

    lStatement += " " + getOrderBy() + " ";

    String lPaginedStatement = "";
    lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
        + "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
        + " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

    setStatement(lPaginedStatement);
  }

  /**
   * Effettua la generica ricerca in base ai dati specificati nel model
   * 
   * @param aModel
   * @throws DAOException
   */
  public void ricercaRateizzazionePP (RateizzazionePPModel aModel) throws DAOException {
    // Recupera la select...from
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model
    String lCondizioni = setCondizioni(aModel);

    if (!lCondizioni.trim().equals(""))
      lSql += " AND " + lCondizioni;

    lSql += " " + getOrderBy() + " ";

    // Imposta lo statement da eseguire
    setStatement(lSql);
  }

  /**
   * Metodo che imposta la statement di ricerca per chiave
   * 
   * @param aKey
   * @throws DAOException
   */
  public void ricercaRateizzazionePPByKey(BigDecimal aIdRateizzazionePP) throws DAOException {
    // Recupera la select...from
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave
    lSql += setCondizioniByKey(aIdRateizzazionePP);

    // Imposta lo statement da eseguire
    setStatement(lSql);
  }


  /**
   * Metodo che imposta la statement di ricerca per chiave Fascicolo SIEP
   * 
   * @param aIdFasSIEP
   * @throws DAOException
   */
  public void ricercaRateizzazionePPByIdFasSIEP(BigDecimal aIdFasSIEP) throws DAOException {
    // Recupera la select...from
    String lSql = getSqlQuery();

    lSql += setCondizioniByIdFasSIEP(aIdFasSIEP);

    // Imposta lo statement da eseguire
    setStatement(lSql);
  }

  /**
   * Metodo che carica il record del result set nel model
   * 
   * @return
   * @throws DAOException
   */
  public GenericModel getModel() throws DAOException {
    RateizzazionePPModel lModel = new RateizzazionePPModel();

    // Inserire le opportune set delle descrizioni!
    lModel.setIdRateizzazionePP (getBigDecimal("ID_RATEIZZAZIONE_PP"));
    lModel.setImportoDaPagare   (getBigDecimal("IMPORTO_DA_PAGARE"));
    lModel.setImportoRata       (getBigDecimal("IMPORTO_RATA"));
    lModel.setNumeroRate        (getBigDecimal("NUMERO_RATE"));
    lModel.setTipoRateizzazione (getString    ("TIPO_RATEIZZAZIONE"));
    lModel.setScadenzaGiorni    (getBigDecimal("SCADENZA_GIORNI"));
    lModel.setDescrTipoRateizzazione(getString("descTipoRateizzazione") );

    lModel.setFasSieIdFascicoloSiep (getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
    //lModel.setEveIdEvento           (getBigDecimal("ID_RATEIZZAZIONE_PP"));

    lModel.setCodOperatoreInserimento (getString("COD_OPERATORE_INSERIMENTO"));
    lModel.setDataInserimento         (getDate("DATA_INSERIMENTO"));
    lModel.setCodUfficioInserimento   (getString("COD_UFFICIO_INSERIMENTO"));

    lModel.setCodOperatoreAggiornamento (getString("COD_OPERATORE_AGGIORNAMENTO"));
    lModel.setDataAggiornamento         (getDate("DATA_AGGIORNAMENTO"));
    lModel.setCodUfficioAggiornamento   (getString("COD_UFFICIO_AGGIORNAMENTO"));    

    return lModel;
  }

  /**
   * Metodo che imposta le condizioni di where per la ricerca
   * 
   * @param aModel
   * @return
   */
  public String setCondizioni(RateizzazionePPModel aModel) {
    String lCondizioni = new String();

    if (aModel.getIdRateizzazionePP() != null) {
      lCondizioni += " and ID_RATEIZZAZIONE_PP = " + aModel.getIdRateizzazionePP() + "";
    }
    
    if (aModel.getImportoRata() != null) {
      lCondizioni += " and IMPORTO_RATA = " + aModel.getImportoRata() + "";
    }
    
    if (aModel.getNumeroRate() != null) {
      lCondizioni += " and NUMERO_RATE = " + aModel.getNumeroRate() + "";
    }
    
    if (aModel.getTipoRateizzazione() != null && aModel.getTipoRateizzazione().length() > 0) {
      lCondizioni += " and TIPO_RATEIZZAZIONE = '" + aModel.getTipoRateizzazione() + "' ";
    }
    
    if (aModel.getScadenzaGiorni() != null) {
      lCondizioni += " and SCADENZA_GIORNI = " + aModel.getScadenzaGiorni() + "";
    }
    
    if (aModel.getFasSieIdFascicoloSiep() != null) {
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
    } 
    
//    if (aModel.getEveIdEvento() != null) {
//      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + "";
//    }

    /*
    if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
      lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento()+ "' ";
    }
    if (aModel.getDataInserimento() != null) {
      lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '"
          + DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
    }    
    if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
      lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
    }    
    if (aModel.getCodOperatoreAggiornamento() != null
        && aModel.getCodOperatoreAggiornamento().length() > 0) {
      lCondizioni += " and RI.COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
          + "' ";
    }    
    if (aModel.getDataAggiornamento() != null) {
      lCondizioni += " and to_char(RI.DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
          + DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
    }    
    if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
      lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
    }
    */

    // Elimino il primo and
    if (lCondizioni.length() > 0) {
      lCondizioni = lCondizioni.substring(4);
    }

    logger.debug("lCondizioni = " + lCondizioni);
    return lCondizioni;
  }

  /**
   * Metodo che imposta le condizioni di select per chiave
   * 
   * @param aKey
   * @return
   */
  public String setCondizioniByKey(BigDecimal aIdRateizzazionePP) {
    String lCondizioni = new String();

    lCondizioni += " and ID_RATEIZZAZIONE_PP = " + aIdRateizzazionePP;

    return lCondizioni;
  }

  /**
   * Metodo che imposta le condizioni di select per Id Fascicolo SIEP
   * 
   * @param aKey
   * @return
   */
  public String setCondizioniByIdFasSIEP(BigDecimal aIdFasSIEP) {
    String lCondizioni = new String();

    lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFasSIEP;

    return lCondizioni;
  }

  /**
   * Metodo per la costruzione della sezione order by
   * 
   * @return
   */
  protected String getOrderBy() {
    String orderBy = new String("");
    //orderBy = " ORDER BY xxxxx";
    return orderBy;
  }


  /**
   * Metodo che imposta la statement di ricerca per id Fascicolo Siep
   * 
   * @param aKey
   * @throws DAOException
   */
  public void ricercaRateizzazionePPByIdFascicoloSiep(BigDecimal aIdFascicoloSiep) throws DAOException {

    // Recupera la select...from
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave

    lSql += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;
    lSql += " ORDER BY DATA_INSERIMENTO DESC";

    // Imposta lo statement da eseguire
    setStatement(lSql);
  }

  /**
   * Metodo per la costruzione della sql query per ricercaRichiestaConversioneByEvento
   * 
   * @return
   */
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT ID_RATEIZZAZIONE_PP, IMPORTO_DA_PAGARE, IMPORTO_RATA, NUMERO_RATE "
                      + ", TIPO_RATEIZZAZIONE, SCADENZA_GIORNI, FAS_SIE_ID_FASCICOLO_SIEP "
                      + ", cgTipoRateizzazione.RV_MEANING descTipoRateizzazione"
                      + ", COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO " 
                      + ", COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO ";

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement +=  " FROM RATEIZZAZIONE_PP, CG_REF_CODES cgTipoRateizzazione";
    lStatement += " WHERE 1=1 ";
    lStatement +=   " AND cgTipoRateizzazione.RV_DOMAIN = 'TIPO_RATEIZZAZIONE'  ";
    lStatement +=   " AND (nvl(RATEIZZAZIONE_PP.TIPO_RATEIZZAZIONE,'-')) = cgTipoRateizzazione.RV_LOW_VALUE ";

    return lStatement;
  }

}