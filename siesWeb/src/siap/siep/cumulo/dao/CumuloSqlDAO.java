package siap.siep.cumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.siep.cumulo.model.CumuloModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: CumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class CumuloSqlDAO extends SqlDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public CumuloSqlDAO (Connection con)
  {
    super(con);
  }

  public void ricercaCumulo( CumuloModel  aModel)  throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " AND (FLAG_VALIDATO = 'S' OR FLAG_VALIDATO = 'N' OR FLAG_VALIDATO is null)";
    lSql += " " + setCondizione(aModel);
    setStatement(lSql);
  }

  public void ricercaCumuloSentenzaFascicoloSige( CumuloModel  aModel)  throws DAOException
  {
    String lSql = getSqlQueryCumuloSentenzaFascicoloSige();
    lSql += " AND (FLAG_VALIDATO = 'S' OR FLAG_VALIDATO = 'N' OR FLAG_VALIDATO is null)";
    lSql += " " + setCondizione(aModel);
    setStatement(lSql);
  }
  
  public void ricercaFascicoliCumulobyIdFascicoloSiep(BigDecimal aFascId)
  {
    String lSql = getSqlQuery();
    lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aFascId;
    lSql += " AND (FLAG_VALIDATO = 'S' OR FLAG_VALIDATO = 'N' OR FLAG_VALIDATO is null)";
    lSql += " ORDER BY CUMULO.DATA_INSERIMENTO ASC ";
    setStatement(lSql);
  }

  /**
   * Imposta la condizione di ricerca per cumuli <b>non validati</b> e per data 
   * inserimento ascendente (dal meno recente)
   * @param aFascId
   */
  public void ricercaFascicoliCumulobyIdFascicoloSiepFlagValidato(BigDecimal aFascId)
  {
    String lSql = getSqlQuery();
    lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP="+aFascId;
    lSql += " AND (FLAG_VALIDATO = 'N' OR FLAG_VALIDATO IS NULL)";
    lSql += " ORDER BY CUMULO.DATA_INSERIMENTO ASC ";
    setStatement(lSql);
  }

  public void ricercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(BigDecimal aFascId)
  {
    String lSql = getSqlQuery();
    lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP="+aFascId;
    lSql += " AND FLAG_VALIDATO = 'S'";
    lSql += " AND DATA_CUMULO IS NOT NULL";
    lSql += " ORDER BY CUMULO.DATA_INSERIMENTO DESC ";
    setStatement(lSql);
  }

  public void ricercaCumuloByEveIdEvento(BigDecimal aId)
  {
    String lSql = getSqlQuery();
    lSql += " AND EVE_ID_EVENTO = "+aId;
    setStatement(lSql);
  }

  /**
   * Ricerca Tutti CUMULI legati a una certa istruttoria
   * 
   * @param aIdIstruttoria
   */
  public void ricercaCumuloByIdIstruttoria(BigDecimal aIdIstruttoria)
  {
    String lSql = getSqlQuery();
    lSql += " AND ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria;
    //lSql += " AND (FLAG_VALIDATO = 'S' OR FLAG_VALIDATO = 'N') ";
    setStatement(lSql);
  }

  public void ricercaCumuloByKey( BigDecimal aKey)   throws DAOException
  {
    String lSql = getSqlQuery();
    //lSql += " AND (FLAG_VALIDATO = 'S' OR FLAG_VALIDATO = 'N' OR FLAG_VALIDATO is null)";
    lSql += " " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }
  

  /**
   * Affettua la ricerca dell'ultimo cumulo <b>VALIDATO</b> inserito prima della
   * data specificata.
   * Se non viene specificata una data in input viene ricercato l'ultimo cumulo 
   * in assoluto.???????????
   * 
   * @param aIdFascicolo
   * @param aDataIns
   * @throws DAOException
   */
  public void ricercaUltimoCumuloByDataIns(BigDecimal aIdFascicolo, Date aDataIns)   throws DAOException
  {
//     String lSql = getSqlQueryNoJoin();
    String lSql = getSqlQuery();
    lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = "+aIdFascicolo;
    lSql += " AND FLAG_VALIDATO = 'S' ";
    lSql += " AND CUMULO.DATA_INSERIMENTO < to_date ('"+DateUtils.getDateToString(aDataIns,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lSql = "+lSql);
    
    setStatement(lSql);
  }

  
  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  " ID_CUMULO, "+
                  " ID_FASCICOLO_SIEP_CUMULATO, "+
                  " CHIAVE_ANNO_FAS_CUMULATO, "+
                  " CHIAVE_PROGR_FAS_CUMULATO, "+
                  " COD_TIPO_UFFICIO_FAS_CUMULATO, "+
                  " COD_LUOGO_UFFICIO_FAS_CUMULATO, "+
                  " COD_UFFICIO_FAS_CUMULATO, "+
                  " COD_TIPO_CUMULO, "+
                  " DATA_RICHIESTA_FASCICOLO, "+
                  " DATA_PERVENIMENTO_FASCICOLO, "+
                  " DATA_CUMULO, "+
                  " COD_MOTIVO_SOSPENSIONE_CUMULO, "+
                  " DATA_SOSPENSIONE_CUMULO, "+
                  " ISTR_ID_ISTRUTTORIA_CUMULO, "+
                  " CUMULO.NOTE NOTE_CUMULO, "+
                  " CUMULO.COD_OPERATORE_INSERIMENTO OPERATORE_INSERIMENTO, "+
                  " CUMULO.DATA_INSERIMENTO DATA_INSER, "+
                  " CUMULO.COD_UFFICIO_INSERIMENTO UFFICIO_INSERIMENTO, "+
                  " CUMULO.COD_OPERATORE_AGGIORNAMENTO OPERATORE_AGGIORNAMENTO, "+
                  " CUMULO.DATA_AGGIORNAMENTO DATA_AGGIOR, "+
                  " CUMULO.COD_UFFICIO_AGGIORNAMENTO UFFICIO_AGGIORNAMENTO, "+
                  " FAS_SIE_ID_FASCICOLO_SIEP, "+
                  " FLAG_TIPO_STAMPA, TISTAMPA.RV_MEANING DESCRSTAMPA, "+
                  " SEN_ID_SENTENZA, FLAG_VALIDATO,EVE_ID_EVENTO,PRIMO_CUMULO,";
    lStatement += "   SENT.DATA_PROVVEDIMENTO DATA_PROVVEDIMENTO,SENT.COD_TIPO_PROVVEDIMENTO COD_TIPO_PROVVEDIMENTO, TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO,";
    lStatement += "   SENT.COD_TIPO_AUTORITA_EMITTENTE COD_TIPO_AUTORITA_EMITTENTE,TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE,";
    lStatement += "   SENT.COD_LUOGO_EMITTENTE COD_LUOGO_EMITTENTE,LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE,";
    lStatement += "   SENT.DATA_IRREVOCABILITA DATA_IRREVOCABILITA ,SENT.NUMERO_SENTENZA SENTENZA_NUMERO,SENT.ANNO_SENTENZA SENTENZA_ANNO";
    lStatement += "   , SENT.FLAG_VISIBILITA ";
    // From condition
    lStatement += " FROM CUMULO, CG_REF_CODES TISTAMPA, SENTENZA SENT, COMUNE LUOGO_EMITTENTE,";
    lStatement += "      CG_REF_CODES TIPO_AUTORITA_EMITTENTE , CG_REF_CODES TIPO_PROVVEDIMENTO";
    // From condition
    lStatement += " WHERE TISTAMPA.RV_DOMAIN = 'STAMPE_CUMULO' AND TISTAMPA.RV_HIGH_VALUE = FLAG_TIPO_STAMPA";
    lStatement +=  "  AND SEN_ID_SENTENZA = SENT.ID_SENTENZA";
    lStatement +=  "  AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ";
    lStatement +=  "  AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = SENT.COD_TIPO_PROVVEDIMENTO ";
    lStatement +=  "  AND TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' ";
    lStatement +=  "  AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = SENT.COD_TIPO_AUTORITA_EMITTENTE";
    lStatement +=  "  AND LUOGO_EMITTENTE.COD_COMUNE = SENT.COD_LUOGO_EMITTENTE";

    return lStatement;
  }


  protected String getSqlQueryCumuloSentenzaFascicoloSige()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  " ID_CUMULO, "+
                  " ID_FASCICOLO_SIEP_CUMULATO, "+
                  " CHIAVE_ANNO_FAS_CUMULATO, "+
                  " CHIAVE_PROGR_FAS_CUMULATO, "+
                  " COD_TIPO_UFFICIO_FAS_CUMULATO, "+
                  " COD_LUOGO_UFFICIO_FAS_CUMULATO, "+
                  " COD_UFFICIO_FAS_CUMULATO, "+
                  " COD_TIPO_CUMULO, "+
                  " DATA_RICHIESTA_FASCICOLO, "+
                  " DATA_PERVENIMENTO_FASCICOLO, "+
                  " DATA_CUMULO, "+
                  " COD_MOTIVO_SOSPENSIONE_CUMULO, "+
                  " DATA_SOSPENSIONE_CUMULO, "+
                  " ISTR_ID_ISTRUTTORIA_CUMULO, "+
                  " CUMULO.NOTE NOTE_CUMULO, "+
                  " CUMULO.COD_OPERATORE_INSERIMENTO OPERATORE_INSERIMENTO, "+
                  " CUMULO.DATA_INSERIMENTO DATA_INSER, "+
                  " CUMULO.COD_UFFICIO_INSERIMENTO UFFICIO_INSERIMENTO, "+
                  " CUMULO.COD_OPERATORE_AGGIORNAMENTO OPERATORE_AGGIORNAMENTO, "+
                  " CUMULO.DATA_AGGIORNAMENTO DATA_AGGIOR, "+
                  " CUMULO.COD_UFFICIO_AGGIORNAMENTO UFFICIO_AGGIORNAMENTO, "+
                  " FAS_SIE_ID_FASCICOLO_SIEP, "+
                  " FLAG_TIPO_STAMPA, TISTAMPA.RV_MEANING DESCRSTAMPA, "+
                  " SEN_ID_SENTENZA, FLAG_VALIDATO,EVE_ID_EVENTO,PRIMO_CUMULO,";
    lStatement += "   SENT.DATA_PROVVEDIMENTO DATA_PROVVEDIMENTO,SENT.COD_TIPO_PROVVEDIMENTO COD_TIPO_PROVVEDIMENTO, TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO,";
    lStatement += "   SENT.COD_TIPO_AUTORITA_EMITTENTE COD_TIPO_AUTORITA_EMITTENTE,TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE,";
    lStatement += "   SENT.COD_LUOGO_EMITTENTE COD_LUOGO_EMITTENTE,LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE,";
    lStatement += "   SENT.DATA_IRREVOCABILITA DATA_IRREVOCABILITA ,SENT.NUMERO_SENTENZA SENTENZA_NUMERO,SENT.ANNO_SENTENZA SENTENZA_ANNO,SENT.FLAG_VISIBILITA";
    // From condition
    lStatement += " FROM CUMULO, CG_REF_CODES TISTAMPA, SENTENZA SENT, COMUNE LUOGO_EMITTENTE,";
    lStatement += "      CG_REF_CODES TIPO_AUTORITA_EMITTENTE , CG_REF_CODES TIPO_PROVVEDIMENTO, FASCICOLO_SIGE FAS_SIGE ";
    // From condition
    lStatement += " WHERE TISTAMPA.RV_DOMAIN = 'STAMPE_CUMULO' AND TISTAMPA.RV_HIGH_VALUE = FLAG_TIPO_STAMPA";
    lStatement +=  "  AND SEN_ID_SENTENZA = SENT.ID_SENTENZA";
    lStatement +=  "  AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ";
    lStatement +=  "  AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = SENT.COD_TIPO_PROVVEDIMENTO ";
    lStatement +=  "  AND TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' ";
    lStatement +=  "  AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = SENT.COD_TIPO_AUTORITA_EMITTENTE";
    lStatement +=  "  AND FAS_SIGE.SEN_ID_SENTENZA_CUMULO = SENT.ID_SENTENZA";
    lStatement +=  "  AND LUOGO_EMITTENTE.COD_COMUNE = SENT.COD_LUOGO_EMITTENTE";

    return lStatement;
  }

  /*****************************************************************************
   * METODO GETMODEL()
   ****************************************************************************/
  public GenericModel getModel() throws DAOException
  {
    CumuloModel aModel = new  CumuloModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdCumulo(getBigDecimal("ID_CUMULO") );
    aModel.setIdFascicoloSiepCumulato(getBigDecimal("ID_FASCICOLO_SIEP_CUMULATO") );
    aModel.setChiaveAnnoFasCumulato(getBigDecimal("CHIAVE_ANNO_FAS_CUMULATO") );
    aModel.setChiaveProgrFasCumulato(getBigDecimal("CHIAVE_PROGR_FAS_CUMULATO") );
    aModel.setCodTipoUfficioFasCumulato(getString("COD_TIPO_UFFICIO_FAS_CUMULATO") );
    //aModel.setDescrTipoUfficioFasCumulato(getString("") );
    aModel.setCodLuogoUfficioFasCumulato(getString("COD_LUOGO_UFFICIO_FAS_CUMULATO") );
    //aModel.setDescrLuogoUfficioFasCumulato(getString("") );
    aModel.setCodUfficioFasCumulato(getString("COD_UFFICIO_FAS_CUMULATO") );
    //aModel.setDescrUfficioFasCumulato(getString("") );
    aModel.setCodTipoCumulo(getString("COD_TIPO_CUMULO") );
    //aModel.setDescrTipoCumulo(getString("") );
    aModel.setDataRichiestaFascicolo(getDate("DATA_RICHIESTA_FASCICOLO") );
    aModel.setDataPervenimentoFascicolo(getDate("DATA_PERVENIMENTO_FASCICOLO") );
    aModel.setDataCumulo(getDate("DATA_CUMULO") );
    aModel.setCodMotivoSospensioneCumulo(getString("COD_MOTIVO_SOSPENSIONE_CUMULO") );
    //aModel.setDescrMotivoSospensioneCumulo(getString("") );
    aModel.setDataSospensioneCumulo(getDate("DATA_SOSPENSIONE_CUMULO") );
    aModel.setNote(getString("NOTE_CUMULO") );
    aModel.setCodOperatoreInserimento(getString("OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSER") );
    aModel.setCodUfficioInserimento(getString("UFFICIO_INSERIMENTO") );
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIOR") );
    aModel.setCodUfficioAggiornamento(getString("UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP") );
    aModel.setFlagTipoStampa(getString("FLAG_TIPO_STAMPA"));
    aModel.setDescrTipoStampa(getString("DESCRSTAMPA"));
    aModel.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA") );
    aModel.setFlagValidato(getString("FLAG_VALIDATO"));

    aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO") );
    aModel.setPrimoCumulo(getString("PRIMO_CUMULO"));
    
    aModel.setIstrIdIstruttoriaCumulo(getBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO"));
    
    //==========================================================================
    // SENTENZA
    //==========================================================================
    SentenzaModel lSentenza = new SentenzaModel();
    
    lSentenza.setAnnoSentenza                (getBigDecimal ("SENTENZA_ANNO"));
    lSentenza.setNumeroSentenza              (getString     ("SENTENZA_NUMERO"));
    lSentenza.setCodTipoProvvedimento        (getString     ("COD_TIPO_PROVVEDIMENTO") );
    lSentenza.setDescrTipoProvvedimento      (getString     ("DESCR_TIPO_PROVVEDIMENTO") );
    lSentenza.setDataProvvedimento           (getDate       ("DATA_PROVVEDIMENTO") );
    lSentenza.setCodTipoAutoritaEmittente    (getString     ("COD_TIPO_AUTORITA_EMITTENTE") );
    lSentenza.setDescrTipoAutoritaEmittente  (getString     ("DESCR_TIPO_AUTORITA_EMITTENTE") );
    lSentenza.setCodLuogoEmittente           (getString     ("COD_LUOGO_EMITTENTE") );
    lSentenza.setDescrLuogoEmittente         (getString     ("DESCR_LUOGO_EMITTENTE") );
//  modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
    //lSentenza.setDataIrrevocabilita          (getDate       ("DATA_IRREVOCABILITA") );
    lSentenza.setFlagVisibilita              (getString     ("FLAG_VISIBILITA") );
    
    aModel.setSentenza(lSentenza);


    return aModel;
  }


  public String  setCondizione(CumuloModel aModel)
  {
    String lCondizioni = new String();
//    boolean lInserito = false;
    if  (aModel.getFasSieIdFascicoloSiep()!=null)
    {
     lCondizioni=" AND FAS_SIE_ID_FASCICOLO_SIEP="+aModel.getFasSieIdFascicoloSiep();
    }
    lCondizioni +=" ORDER BY CUMULO.DATA_INSERIMENTO ASC ";

    return lCondizioni;
  }


  public String setCondizioniByKey(BigDecimal aKey)
  {
    return " AND ID_CUMULO = " + aKey;
  }

  public void ricercaCumuloByIdSentenza(BigDecimal aId, String flagValidato)
  {
    String lSql = getSqlQuery();
    lSql += " AND SEN_ID_SENTENZA = " + aId;
    if(flagValidato != null){
    	lSql += " AND (FLAG_VALIDATO = '" + flagValidato + "' OR FLAG_VALIDATO IS NULL )";	
    }
    setStatement(lSql);
  }

}