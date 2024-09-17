package siap.siep.notifica.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaModel;

/**
 * NotificaAutoritaSqlDAO - Sql di Dao di Notifica in Join con Autorita
 *
 * @version 1.0
 */
public class NotificaAutoritaSqlDAO extends SIAPSqlDAO {

	public NotificaAutoritaSqlDAO(Connection con) {
		super(con);
	}

	public void ricercaNotificaByEvento(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " AND EVE_ID_EVENTO = " + aKey;
		setStatement(lSql);
	}

	public void ricercaNotificaAutByFascicolo(BigDecimal aFascicolo) throws DAOException {

		String lSql = getSqlQueryFascicolo(aFascicolo);
		lSql += " AND N.COD_TIPO_NOTIFICA='C' ";
		lSql += "ORDER BY E.DATA_INSERIMENTO DESC ";

		setStatement(lSql);
	}

	public void ricercaNotificaPolByFascicolo(BigDecimal aFascicolo) throws DAOException {

		String lSql = getSqlQueryFascicolo(aFascicolo);
		lSql += " AND N.COD_TIPO_NOTIFICA='E' ";
		lSql += "ORDER BY E.DATA_INSERIMENTO DESC ";

		setStatement(lSql);
	}

	public void ricercaNotificaPoliziaNotificaNByFascicolo(BigDecimal aFascicolo) throws DAOException {
		String lSql = getSqlQueryFascicolo(aFascicolo);
		lSql += " AND N.COD_TIPO_NOTIFICA='N' ";
		lSql += "ORDER BY E.DATA_INSERIMENTO DESC ";

		setStatement(lSql);
	}

	public void ricercaNotificaPoliziaNotificaCByFascicolo(BigDecimal aFascicolo) throws DAOException {

		String lSql = getSqlQueryFascicolo(aFascicolo);
		lSql += " AND N.COD_TIPO_NOTIFICA='C' ";
		lSql += "ORDER BY E.DATA_INSERIMENTO DESC ";

		setStatement(lSql);
	}

	public void ricercaNotificaCompByFascicolo(BigDecimal aFascicolo) throws DAOException {

		String lSql = getSqlQueryFascicolo(aFascicolo);
		lSql += " AND N.COD_TIPO_NOTIFICA='N' ";
		lSql += "ORDER BY E.DATA_INSERIMENTO DESC ";

		setStatement(lSql);
	}

	protected String getSqlQueryFascicolo(BigDecimal aFascicolo) {

		String lStatement = new String("");

		lStatement += "SELECT N.ID_NOTIFICA ID_NOTIFICA, "
				+ "N.COD_TIPO_NOTIFICA COD_TIPO_NOTIFICA,COD_NOT.RV_MEANING TIP_NOT, "
				+ "N.DATA_AVVENUTA_NOTIFICA DATA_AVVENUTA_NOTIFICA, N.DATA_INVIO DATA_INVIO, "
				+ "AUT.COD_TIPO_AUTORITA COD_TIPO_AUTORITA, TIP_AUT.RV_MEANING TIPO_AUT, "
				+ "AUT.COD_SEDE COD_SEDE,COMUNE.DESCRIZIONE SEDE, "
				+ "N.COD_ESITO COD_ESITO, COD_ESI.RV_MEANING ESITO, N.NOTE NOTE, "
				+ "N.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, "
				+ "N.DATA_INSERIMENTO DATA_INSERIMENTO, "
				+ "N.COD_UFFICIO_INSERIMENTO COD_UFFICIO_INSERIMENTO, "
				+ "N.CODICE_OPERATORE_AGGIORNAMENTO CODICE_OPERATORE_AGGIORNAMENTO, "
				+ "N.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, "
				+ "N.COD_UFFICIO_AGGIORNAMENTO COD_UFFICIO_AGGIORNAMENTO, "
				+ "N.EVE_ID_EVENTO EVE_ID_EVENTO, "
				+ "N.AUT_EST_ID_AUTORITA_ESTERNA AUT_EST_ID_AUTORITA_ESTERNA, "
				+ "N.SOG_ID_SOGGETTO SOG_ID_SOGGETTO, "
				+ "N.AVV_ID_AVVOCATO_FASCICOLO_SIEP AVV_ID_AVVOCATO_FASCICOLO_SIEP, "
				+ "N.AVV_ID_AVVOCATO_FASCICOLO_SIUS AVV_ID_AVVOCATO_FASCICOLO_SIUS, "
				+ "N.AVV_ID_AVVOCATO_FASCICOLO_SIGE AVV_ID_AVVOCATO_FASCICOLO_SIGE, "
				+ "N.UFF_COD_UFFICIO UFF_COD_UFFICIO, N.SOLLECITO SOLLECITO, "
				+ "N.CSS_ID_CSSA CSS_ID_CSSA, "
				// modifica relativa al tipo istituto
				+ "N.IST_DET_ID_ISTITUTO_DETENZIONE "
				+ "FROM NOTIFICA N, CG_REF_CODES COD_NOT, AUTORITA_ESTERNA AUT,CG_REF_CODES TIP_AUT, "
				+ "CG_REF_CODES COD_ESI, COMUNE, EVENTO E "
				+ "WHERE AUT.ID_AUTORITA_ESTERNA = N.AUT_EST_ID_AUTORITA_ESTERNA "
				+ "AND COD_ESI.RV_DOMAIN = 'ESITO_NOTIFICA' AND COD_ESI.RV_LOW_VALUE = N.COD_ESITO "
				+ "AND COD_NOT.RV_DOMAIN = 'TIPO_NOTIFICA' AND COD_NOT.RV_LOW_VALUE = COD_TIPO_NOTIFICA "
				+ "AND TIP_AUT.RV_DOMAIN = 'TIPO_AUTORITA' AND TIP_AUT.RV_LOW_VALUE = COD_TIPO_AUTORITA "
				+ "AND COMUNE.COD_COMUNE = AUT.COD_SEDE AND N.EVE_ID_EVENTO = E.ID_EVENTO  "
				+ "AND E.COD_TIPO_PROVVEDIMENTO = '03' AND E.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicolo;

		return lStatement;
	}

	protected String getSqlQuery() {

		String lStatement = new String("");

		lStatement += "SELECT NOTI.ID_NOTIFICA ID_NOTIFICA, "
				+ "NOTI.COD_TIPO_NOTIFICA COD_TIPO_NOTIFICA, COD_NOT.RV_MEANING TIP_NOT, "
				+ "NOTI.DATA_AVVENUTA_NOTIFICA DATA_AVVENUTA_NOTIFICA, NOTI.DATA_INVIO DATA_INVIO, "
				+ "AUT.COD_TIPO_AUTORITA COD_TIPO_AUTORITA, TIP_AUT.RV_MEANING TIPO_AUT, "
				+ "AUT.COD_SEDE COD_SEDE, COMUNE.DESCRIZIONE SEDE, "
				+ "NOTI.COD_ESITO COD_ESITO, COD_ESI.RV_MEANING ESITO, NOTI.NOTE NOTE, "
				+ "NOTI.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, "
				+ "NOTI.DATA_INSERIMENTO DATA_INSERIMENTO, "
				+ "NOTI.COD_UFFICIO_INSERIMENTO COD_UFFICIO_INSERIMENTO, "
				+ "NOTI.CODICE_OPERATORE_AGGIORNAMENTO CODICE_OPERATORE_AGGIORNAMENTO, "
				+ "NOTI.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, "
				+ "NOTI.COD_UFFICIO_AGGIORNAMENTO COD_UFFICIO_AGGIORNAMENTO, "
				+ "NOTI.EVE_ID_EVENTO EVE_ID_EVENTO, "
				+ "NOTI.AUT_EST_ID_AUTORITA_ESTERNA AUT_EST_ID_AUTORITA_ESTERNA, "
				+ "NOTI.SOG_ID_SOGGETTO SOG_ID_SOGGETTO, "
				+ "NOTI.AVV_ID_AVVOCATO_FASCICOLO_SIEP AVV_ID_AVVOCATO_FASCICOLO_SIEP, "
				+ "NOTI.AVV_ID_AVVOCATO_FASCICOLO_SIUS AVV_ID_AVVOCATO_FASCICOLO_SIUS, "
				+ "NOTI.AVV_ID_AVVOCATO_FASCICOLO_SIGE AVV_ID_AVVOCATO_FASCICOLO_SIGE, "
				+ "NOTI.UFF_COD_UFFICIO UFF_COD_UFFICIO "
				+ "NOTI.SOLLECITO SOLLECITO, NOTI.CSS_ID_CSSA, CSS_ID_CSSA, "
				// modifica relativa al tipo istituto
				+ "NOTI.IST_DET_ID_ISTITUTO_DETENZIONE "
				+ "FROM NOTIFICA NOTI, CG_REF_CODES COD_NOT, AUTORITA_ESTERNA AUT,CG_REF_CODES  TIP_AUT, "
				+ "CG_REF_CODES COD_ESI, COMUNE "
				+ "WHERE AUT.ID_AUTORITA_ESTERNA = NOTI.AUT_EST_ID_AUTORITA_ESTERNA "
				+ "AND COD_NOT.RV_DOMAIN = 'TIPO_NOTIFICA' AND COD_NOT.RV_LOW_VALUE = COD_TIPO_NOTIFICA "
				+ "AND COD_ESI.RV_DOMAIN = 'ESITO_NOTIFICA' AND COD_ESI.RV_LOW_VALUE = COD_ESITO "
				+ "AND TIP_AUT.RV_DOMAIN = 'TIPO_AUTORITA' AND TIP_AUT.RV_LOW_VALUE =  COD_TIPO_AUTORITA "
				+ "AND COMUNE.COD_COMUNE = AUT.COD_SEDE ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {

		NotificaModel aModel = new NotificaModel();
		AutoritaEsternaModel lAut = new AutoritaEsternaModel();

		lAut.setCodSede(getString("COD_SEDE"));
		lAut.setDescrSede(getString("SEDE"));
		lAut.setDescrTipoAutorita(getString("TIPO_AUT"));
		lAut.setCodTipoAutorita(getString("COD_TIPO_AUTORITA"));
		aModel.setAutoritaEsterna(lAut);
		// Inserire le opportune set delle descrizioni!
		aModel.setIdNotifica(getBigDecimal("ID_NOTIFICA"));
		aModel.setCodTipoNotifica(getString("COD_TIPO_NOTIFICA"));
		aModel.setDescrTipoNotifica(getString("TIP_NOT"));
		aModel.setDataAvvenutaNotifica(getDate("DATA_AVVENUTA_NOTIFICA"));
		aModel.setDataInvio(getDate("DATA_INVIO"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("ESITO"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodiceOperatoreAggiornamento(getString("CODICE_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setAutEstIdAutoritaEsterna(getBigDecimal("AUT_EST_ID_AUTORITA_ESTERNA"));
		aModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		aModel.setAvvIdAvvocatoFascicoloSiep(getBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIEP"));
		aModel.setAvvIdAvvocatoFascicoloSius(getBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIUS"));
		aModel.setAvvIdAvvocatoFascicoloSige(getBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIGE"));
		aModel.setUffCodUfficio(getString("UFF_COD_UFFICIO"));
		aModel.setSollecito(getBigDecimal("SOLLECITO"));
		aModel.setCssIdCssa(getBigDecimal("CSS_ID_CSSA"));
		// modifica relativa al tipo istituto
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));

		return aModel;
	}

}