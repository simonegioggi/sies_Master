package siap.siep.sanzionesostitutiva.dao;

/**
* <p>Title: SanzioneSostResiduaSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella SanzioneSostResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class SanzioneSostResiduaSqlDAO extends SqlDAO {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /*****************************************************************************
   * Costruttore
   * @param con
   ****************************************************************************/
  public SanzioneSostResiduaSqlDAO (Connection con) {
    super(con);
  }


  /*****************************************************************************
   * Restituisce il numero di record dell'operazione di ricerca costruendo
   * la clausola where con lo stesso model utilizzato per la ricerca
   * @param aModel
   * @throws DAOException
   ****************************************************************************/
  public void getCountSanzioneSostResidua(SanzioneSostResiduaModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM SANZIONE_SOST_RESIDUA ";

    // Recupero la where condition in base al model
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals(""))
      lStatement+=" WHERE " + lCondizioni;

    // Imposta lo statement da eseguire
    setStatement(lStatement);
  }

  /*****************************************************************************
   * Effettua la ricerca e restituisce solo i risultati nel range di record che
   * vanno inseriti nella pagfina passata in input
   * @param aModel
   * @param aPage
   * @throws DAOException
   ****************************************************************************/
  public void ricercaSanzioneSostResiduaPaged(SanzioneSostResiduaModel aModel, int aPage) throws DAOException {
    String lStatement = new String("");

    lStatement += getSqlQuery();

    // Recupero la where condition in base al model
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals(""))
      lStatement+=" WHERE " + lCondizioni;

    lStatement += " "+getOrderBy()+" ";

    String lPaginedStatement = "";
    lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement + "  ) INNER ) WHERE rn between  " + ( (aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) +
          " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

    setStatement(lPaginedStatement);
  }


  /*****************************************************************************
   * Effettua la generica ricerca in base ai dati specificati nel model
   * @param aModel
   * @throws DAOException
   ****************************************************************************/
  public void ricercaSanzioneSostResidua( SanzioneSostResiduaModel  aModel)  throws DAOException {
    // Recupera la select...from
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model
    String lCondizioni = setCondizioni(aModel);

    if (!lCondizioni.trim().equals(""))
      lSql+=" WHERE " + lCondizioni;

    lSql += " "+getOrderBy()+" ";

    // Imposta lo statement da eseguire
    setStatement(lSql);
  }


  /*****************************************************************************
   * Metodo che imposta la statement di ricerca per chiave
   * @param aKey
   * @throws DAOException
   ****************************************************************************/
  public void ricercaSanzioneSostResiduaByKey( BigDecimal aIdSanzioneSostResidua) throws DAOException {
    // Recupera la select...from
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave
    lSql += " WHERE " + setCondizioniByKey( aIdSanzioneSostResidua);

    // Imposta lo statement da eseguire
    setStatement(lSql);
  }

  /*****************************************************************************
   * Restituisce l'elenco delle SANZIONE_SOST_RESIDUA per IdFascicolo
   *
   * @param aKeyFascicolo
   * @throws DAOException
   ************************************************************************** */
  public void ricercaSanzioneSostResiduaByIdFascicolo(BigDecimal aKeyFascicolo)	 throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += setCondizioniByIdFascicolo(aKeyFascicolo);

    setStatement(lSql);
  }

  /**
   * Imposta la select per recuperare l'elenco delle SS Ordinate per data
   * inserimento desc.
   * @param aIdFascioloSiep
   * @param aFlagValidata. Se 'S' recupera solo quelle validate, se 'N' solo
   * quelle non validate se null recupera tutte le SS
   * @throws DAOException
   */
  public void ricercaUltimaSanzioneSostResiduaByIdFasc( BigDecimal aIdFascioloSiep, String aFlagValidata) throws DAOException {
    // Recupera la select...from
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave fascicolo
    lSql += "   AND SSR.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascioloSiep;
    if (aFlagValidata!=null && aFlagValidata.equals("S")){
      lSql += "   AND PR.FLAG_VALIDATO = 'S' ";
    }
    else if (aFlagValidata!=null && aFlagValidata.equals("N")){
      lSql += "   AND ( PR.FLAG_VALIDATO is null OR PR.FLAG_VALIDATO = 'N' ) ";
    }
    lSql += " ORDER BY SSR.DATA_INSERIMENTO DESC ";
    // Imposta lo statement da eseguire
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lSql = "+lSql);
    setStatement(lSql);
  }

  /**
   * Imposta la select per recuperare la/le SS collegate a una Pena Residu
   * @param aIdFascioloSiep
   * @throws DAOException
   */
  public void ricercaSanzioneSostResiduaByIdPenRes( BigDecimal aIdPenaResidua) throws DAOException {
    // Recupera la select...from
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave pena residua
    lSql += "   AND SSR.PEN_RES_ID_PENA_RESIDUA = " + aIdPenaResidua;
    // Imposta lo statement da eseguire
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lSql = "+lSql);
    setStatement(lSql);
  }


  /*****************************************************************************
   * Metodo per la costruzione della sql query
   * @return
   ****************************************************************************/
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "SSR.ID_SANZIONE_SOST_RESIDUA, "+
                  "SSR.FAS_SIE_ID_FASCICOLO_SIEP, "+
                  "SSR.EVE_ID_EVENTO, "+
                  "SSR.PEN_RES_ID_PENA_RESIDUA, "+
                  "SSR.COD_TIPO_SANZIONE, COD_TIPO_SANZIONE.RV_MEANING DESCR_TIPO_SANZIONE, "+
                  "SSR.DATA_INIZIO, "+
                  "SSR.DATA_FINE_PRESUNTA, "+
                  "SSR.DATA_FINE, "+
                  "SSR.NUM_ANNI, "+
                  "SSR.NUM_MESI, "+
                  "SSR.NUM_GIORNI, "+
                  "SSR.SANZIONE_PECUNIARIA_MULTA, "+
                  "SSR.SANZIONE_PECUNIARIA_AMMENDA, "+
                  "SSR.COD_OPERATORE_INSERIMENTO, "+
                  "SSR.DATA_INSERIMENTO, "+
                  "SSR.COD_UFFICIO_INSERIMENTO, "+
                  "SSR.COD_OPERATORE_AGGIORNAMENTO, "+
                  "SSR.DATA_AGGIORNAMENTO, "+
                  "SSR.COD_UFFICIO_AGGIORNAMENTO, " +
                  "PR.FLAG_VALIDATO " ;

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM SANZIONE_SOST_RESIDUA SSR, PENA_RESIDUA PR, CG_REF_CODES COD_TIPO_SANZIONE";

    lStatement += " WHERE SSR.PEN_RES_ID_PENA_RESIDUA = PR.ID_PENA_RESIDUA ";

    lStatement += " AND (  nvl(SSR.COD_TIPO_SANZIONE,'-') = COD_TIPO_SANZIONE.RV_LOW_VALUE AND COD_TIPO_SANZIONE.RV_DOMAIN = 'TIPO_SANZIONE_SOSTITUTIVA' ) " ;

    return lStatement;
  }


  /*****************************************************************************
   * Metodo che carica il record del result set nel model
   * @return
   * @throws DAOException
   ****************************************************************************/
  public GenericModel  getModel() throws DAOException {
     SanzioneSostResiduaModel aModel = new  SanzioneSostResiduaModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdSanzioneSostResidua     ( getBigDecimal ("ID_SANZIONE_SOST_RESIDUA"   ) );
    aModel.setFasSieIdFascicoloSiep     ( getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"  ) );
    aModel.setEveIdEvento               ( getBigDecimal ("EVE_ID_EVENTO"              ) );
    aModel.setPenResIdPenaResidua       ( getBigDecimal ("PEN_RES_ID_PENA_RESIDUA"    ) );
    aModel.setCodTipoSanzione           ( getString     ("COD_TIPO_SANZIONE"          ) );
aModel.setDescrTipoSanzione(getString("DESCR_TIPO_SANZIONE") );
    aModel.setDataInizio                ( getDate       ("DATA_INIZIO"                ) );
    aModel.setDataFinePresunta          ( getDate       ("DATA_FINE_PRESUNTA"         ) );
    aModel.setDataFine                  ( getDate       ("DATA_FINE"                  ) );
    aModel.setNumAnni                   ( getBigDecimal ("NUM_ANNI"                   ) );
    aModel.setNumMesi                   ( getBigDecimal ("NUM_MESI"                   ) );
    aModel.setNumGiorni                 ( getBigDecimal ("NUM_GIORNI"                 ) );
    aModel.setSanzionePecuniariaMulta   ( getBigDecimal ("SANZIONE_PECUNIARIA_MULTA"  ) );
    aModel.setSanzionePecuniariaAmmenda ( getBigDecimal ("SANZIONE_PECUNIARIA_AMMENDA") );
    aModel.setFlagValidato              ( getString     ("FLAG_VALIDATO"              ) );
    aModel.setCodOperatoreInserimento   ( getString     ("COD_OPERATORE_INSERIMENTO"  ) );
    aModel.setDataInserimento           ( getDate       ("DATA_INSERIMENTO"           ) );
    aModel.setCodUfficioInserimento     ( getString     ("COD_UFFICIO_INSERIMENTO"    ) );
//aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento ( getString     ("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento         ( getDate       ("DATA_AGGIORNAMENTO"         ) );
    aModel.setCodUfficioAggiornamento   ( getString     ("COD_UFFICIO_AGGIORNAMENTO"  ) );
//aModel.setDescrUfficioAggiornamento(getString("") );

    return aModel;
  }


  /*****************************************************************************
   * Metodo che imposta le condizioni di where per la ricerca
   * @param aModel
   * @return
   ****************************************************************************/
  public String setCondizioni(SanzioneSostResiduaModel aModel) {
    String lCondizioni = new String();

    if (aModel.getIdSanzioneSostResidua() != null ) {
      lCondizioni += " and SSR.ID_SANZIONE_SOST_RESIDUA = " + aModel.getIdSanzioneSostResidua() + "";
    }
    if (aModel.getFasSieIdFascicoloSiep() != null ) {
      lCondizioni += " and SSR.FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
    }
    if (aModel.getEveIdEvento() != null ) {
      lCondizioni += " and SSR.EVE_ID_EVENTO = " + aModel.getEveIdEvento() + "";
    }
    if (aModel.getPenResIdPenaResidua() != null ) {
      lCondizioni += " and SSR.PEN_RES_ID_PENA_RESIDUA = " + aModel.getPenResIdPenaResidua() + "";
    }
    if (aModel.getCodTipoSanzione() != null && aModel.getCodTipoSanzione().length() > 0) {
      lCondizioni += " and SSR.COD_TIPO_SANZIONE = '" + aModel.getCodTipoSanzione() + "' ";
    }
    if (aModel.getDataInizio() != null ) {
      lCondizioni += " and to_char(SSR.DATA_INIZIO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizio(),"dd/MM/yyyy") + "' ";
    }
    if (aModel.getDataFinePresunta() != null ) {
      lCondizioni += " and to_char(SSR.DATA_FINE_PRESUNTA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFinePresunta(),"dd/MM/yyyy") + "' ";
    }
    if (aModel.getDataFine() != null ) {
      lCondizioni += " and to_char(SSR.DATA_FINE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFine(),"dd/MM/yyyy") + "' ";
    }
    if (aModel.getNumAnni() != null ) {
      lCondizioni += " and SSR.NUM_ANNI = " + aModel.getNumAnni() + "";
    }
    if (aModel.getNumMesi() != null ) {
      lCondizioni += " and SSR.NUM_MESI = " + aModel.getNumMesi() + "";
    }
    if (aModel.getNumGiorni() != null ) {
      lCondizioni += " and SSR.NUM_GIORNI = " + aModel.getNumGiorni() + "";
    }
    if (aModel.getSanzionePecuniariaMulta() != null ) {
      lCondizioni += " and SSR.SANZIONE_PECUNIARIA_MULTA = " + aModel.getSanzionePecuniariaMulta() + "";
    }
    if (aModel.getSanzionePecuniariaAmmenda() != null ) {
      lCondizioni += " and SSR.SANZIONE_PECUNIARIA_AMMENDA = " + aModel.getSanzionePecuniariaAmmenda() + "";
    }
    if (aModel.getFlagValidato() != null && aModel.getFlagValidato().length() > 0) {
      lCondizioni += " and FLAG_VALIDATO = '" + aModel.getFlagValidato() + "' ";
    }
    if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
      lCondizioni += " and SSR.COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' ";
    }
    if (aModel.getDataInserimento() != null ) {
      lCondizioni += " and to_char(SSR.DATA_INSERIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInserimento(),"dd/MM/yyyy") + "' ";
    }
    if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
      lCondizioni += " and SSR.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
    }
    if (aModel.getCodOperatoreAggiornamento() != null && aModel.getCodOperatoreAggiornamento().length() > 0) {
      lCondizioni += " and SSR.COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento() + "' ";
    }
    if (aModel.getDataAggiornamento() != null ) {
      lCondizioni += " and to_char(SSR.DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataAggiornamento(),"dd/MM/yyyy") + "' ";
    }
    if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
      lCondizioni += " and SSR.COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
    }
    // Elimino il primo and
    if (lCondizioni.length() > 0) {
      lCondizioni = lCondizioni.substring(4);
    }

    return lCondizioni;
  }


  /*****************************************************************************
   * Metodo che imposta le condizioni di select per chiave
   * @param aKey
   * @return
   ****************************************************************************/
  public String setCondizioniByKey( BigDecimal aIdSanzioneSostResidua  ) {
    String lCondizioni = new String();

    lCondizioni += " and SSR.ID_SANZIONE_SOST_RESIDUA = " + aIdSanzioneSostResidua;

    // Elimino il primo and
    if (lCondizioni.length() > 0) {
      lCondizioni = lCondizioni.substring(4);
    }

    return lCondizioni;
  }

  /*****************************************************************************
   * Metodo che imposta le condizioni di select per chiave Fascicolo
   * @param aKey
   * @return
   ****************************************************************************/
  public String setCondizioniByIdFascicolo( BigDecimal aIdFascicolo  )
  {
    String lCondizioni = new String();

    lCondizioni += " and SSR.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

    return lCondizioni;
  }

  /*****************************************************************************
   * Metodo per la costruzione della sezione order by
   * @return
   ****************************************************************************/
  protected String getOrderBy() {
    String orderBy = new String("");
    //orderBy = " ORDER BY ";
    return orderBy;
  }
}