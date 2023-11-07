package siap.siep.notifica.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;


/**
 * <p>Title: NotificaEVENTOSqlDAO</p>
 * <p>Description: Sql di Dao di Notifica in Join con Evento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class NotificaEventoSqlDAO extends SIAPSqlDAO
{
  public NotificaEventoSqlDAO (Connection con)
  {
    super(con);
  }

  /**
   * Imposta la Ricerca delle notifiche Associate ad un evento. Restituisce la
   * lista ordinata per ID_NOTIFICA 
   * 
   * @param aIdEvento id dell'evento
   * @throws DAOException
   */
  public void ricercaNotificaByEvento(BigDecimal aIdEvento)	throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " AND EVE_ID_EVENTO = " +aIdEvento;
    lSql += " ORDER BY ID_NOTIFICA " ;
    setStatement(lSql);
  }

  /**
   * Imposta la Ricerca delle notifiche Associate ad un evento. Con codice tipo 
   * notifica = 'E'
   *  
   * @param aIdEvento id dell'evento
   * @throws DAOException
   */
  public void ricercaNotificaByEventoCondannato(BigDecimal aIdEvento)	throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += " AND EVE_ID_EVENTO = " +aIdEvento;
    lSql += " AND COD_TIPO_NOTIFICA = 'E' ";

    setStatement(lSql);
  }

  /**
   * Imposta la Ricerca delle notifiche Associate ad un evento. Con codice tipo 
   * notifica = 'N' legate a un difensore
   * 
   * @param aIdEvento id dell'evento
   * @throws DAOException
   */
  public void ricercaNotificaByEventoDifensore(BigDecimal aIdEvento)	throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " AND EVE_ID_EVENTO = " +aIdEvento;
    lSql += " AND COD_TIPO_NOTIFICA = 'N' ";
    lSql += " AND AVV_ID_AVVOCATO_FASCICOLO_SIEP IS NOT NULL";

    setStatement(lSql);
  }


  /**
   * Costruisce la query (SELECT...FROM...WHERE) impostando le join condition
   * con la CG_REF_CODES per il recupero delle descrizioni
   * 
   * @return query costruita 
   */
  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement +=
    "SELECT NOTI.ID_NOTIFICA ID_NOTIFICA, " +
    " NOTI.COD_TIPO_NOTIFICA COD_TIPO_NOTIFICA, COD_NOT.RV_MEANING TIP_NOT," +
    " NOTI.DATA_AVVENUTA_NOTIFICA DATA_AVVENUTA_NOTIFICA, " +
    " NOTI.DATA_INVIO DATA_INVIO, " +
    " NOTI.COD_ESITO COD_ESITO, COD_ESI.RV_MEANING ESITO," +
    " NOTI.NOTE NOTE,  " +
    " NOTI.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, " +
    " NOTI.DATA_INSERIMENTO DATA_INSERIMENTO, " +
    " NOTI.COD_UFFICIO_INSERIMENTO COD_UFFICIO_INSERIMENTO, " +
    " NOTI.CODICE_OPERATORE_AGGIORNAMENTO CODICE_OPERATORE_AGGIORNAMENTO, " +
    " NOTI.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, " +
    " NOTI.COD_UFFICIO_AGGIORNAMENTO COD_UFFICIO_AGGIORNAMENTO, " +
    " NOTI.EVE_ID_EVENTO EVE_ID_EVENTO, " +
    " NOTI.AUT_EST_ID_AUTORITA_ESTERNA AUT_EST_ID_AUTORITA_ESTERNA, " +
    " NOTI.SOG_ID_SOGGETTO SOG_ID_SOGGETTO, " +
    " NOTI.AVV_ID_AVVOCATO_FASCICOLO_SIEP AVV_ID_AVVOCATO_FASCICOLO_SIEP, " +
    " NOTI.AVV_ID_AVVOCATO_FASCICOLO_SIUS AVV_ID_AVVOCATO_FASCICOLO_SIUS, " +
    " NOTI.AVV_ID_AVVOCATO_FASCICOLO_SIGE AVV_ID_AVVOCATO_FASCICOLO_SIGE, " +
    " NOTI.UFF_COD_UFFICIO UFF_COD_UFFICIO," +
    " NOTI.SOLLECITO SOLLECITO," +
    " NOTI.CSS_ID_CSSA, CSS_ID_CSSA, " +
    " NOTI.IST_DET_ID_ISTITUTO_DETENZIONE, " +
    // MERGE v10 COLLAUDO: aggiunti 6 campi in estrazione
    " NOTI.CUR_ID_CURATORE," +
    " NOTI.FLAG_NOTIFICA_VIA_FAX," +
    " NOTI.ID_PARTE_UDIENZA," +
    " NOTI.COD_UFF_UEPE_USSM_SS," +
    " NOTI.COD_UFF_UDS_UDSM," +
    " NOTI.COD_UFF_TDS_TDSM," +
    " NOTI.AUT_EST_ID_AUTORITA_EST_DELEG" +
    " , NOTI.ID_CIVILMENTE_OBBLIGATO " + //MEV_2023-13 
    " FROM NOTIFICA NOTI, CG_REF_CODES COD_NOT, CG_REF_CODES COD_ESI" +
    " WHERE COD_NOT.RV_DOMAIN = 'TIPO_NOTIFICA' AND COD_NOT.RV_LOW_VALUE = COD_TIPO_NOTIFICA" +
    " AND COD_ESI.RV_DOMAIN = 'ESITO_NOTIFICA' AND COD_ESI.RV_LOW_VALUE = COD_ESITO";

    return lStatement;
  }
  
  /**
   * Metodo per caricare il risultato della query in un model
   * @return GenericModel
   */
  public GenericModel  	 getModel() throws DAOException
  {
    NotificaModel aModel = new  NotificaModel();

    //AutoritaEsternaModel lAut = new  AutoritaEsternaModel();
    //aModel.setAutoritaEsterna(lAut);

    //Inserire le opportune set delle descrizioni!
    aModel.setIdNotifica(getBigDecimal("ID_NOTIFICA") );
    aModel.setCodTipoNotifica(getString("COD_TIPO_NOTIFICA") );
    aModel.setDescrTipoNotifica(getString("TIP_NOT") );
    aModel.setDataAvvenutaNotifica(getDate("DATA_AVVENUTA_NOTIFICA") );
    aModel.setDataInvio(getDate("DATA_INVIO") );
    aModel.setCodEsito(getString("COD_ESITO") );
    aModel.setDescrEsito(getString("ESITO") );
    aModel.setNote(getString("NOTE") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    // aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodiceOperatoreAggiornamento(getString("CODICE_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO") );
    aModel.setAutEstIdAutoritaEsterna(getBigDecimal("AUT_EST_ID_AUTORITA_ESTERNA") );
    aModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );
    aModel.setAvvIdAvvocatoFascicoloSiep(getBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIEP") );
    aModel.setAvvIdAvvocatoFascicoloSius(getBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIUS") );
    aModel.setAvvIdAvvocatoFascicoloSige(getBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIGE") );
    aModel.setUffCodUfficio(getString("UFF_COD_UFFICIO") );
    aModel.setSollecito(getBigDecimal("SOLLECITO") );
    aModel.setCssIdCssa(getBigDecimal("CSS_ID_CSSA") );
    aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
    aModel.setAutEstIdAutoritaEstDeleg(getBigDecimal("AUT_EST_ID_AUTORITA_EST_DELEG"));
    // MERGE v10 COLLAUDO: aggiunti 6 campi in estrazione
    aModel.setCurIdCuratore(getBigDecimal("CUR_ID_CURATORE"));
    aModel.setFlagNotificaViaFax(getBigDecimal("FLAG_NOTIFICA_VIA_FAX"));
    aModel.setIdParteUdienza(getBigDecimal("ID_PARTE_UDIENZA"));
    aModel.setCodUffUepeUssmSS(getString("COD_UFF_UEPE_USSM_SS"));
    aModel.setCodUffUdsUdsm(getString("COD_UFF_UDS_UDSM"));
    aModel.setCodUffTdsTdsm(getString("COD_UFF_TDS_TDSM"));
    
    // MEV_2023-13 
    aModel.setIdCivilmenteObbligato (getBigDecimal("ID_CIVILMENTE_OBBLIGATO"));

    return aModel;
  }
}