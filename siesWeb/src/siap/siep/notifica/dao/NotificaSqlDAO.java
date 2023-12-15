package siap.siep.notifica.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.notifica.model.NotificaModel;

/**
 * <p>
 * Title: NotificaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Notifica
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class NotificaSqlDAO extends SIAPSqlDAO {
	public NotificaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaNotifica(NotificaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaNotificaAvvocatoNonAvvenuta(NotificaModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " where EVE_ID_EVENTO = " + aModel.getEveIdEvento();
		lSql += " and COD_TIPO_NOTIFICA = '" + aModel.getCodTipoNotifica() + "'";
		lSql += " and DATA_AVVENUTA_NOTIFICA is not null";
		setStatement(lSql);
	}

	public void ricercaNotificaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaNotificaByEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE EVE_ID_EVENTO = " + aKey;
		lSql += setOrderTipoNotifica(); // STUB 15/10/2008

		setStatement(lSql);
	}

	public void ricercaNotificheByEventoNotParti(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE EVE_ID_EVENTO = " + aKey;
		lSql += " and ID_PARTE_UDIENZA is NULL ";
		lSql += setOrderTipoNotifica();

		setStatement(lSql);
	}

	public void ricercaNotificaDataAvvNotificaNullByEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE EVE_ID_EVENTO = " + aKey;
		lSql += " AND DATA_AVVENUTA_NOTIFICA IS NULL ";
		setStatement(lSql);
	}

	public void ricercaNotificaEsecuzioneByEventoDataNull(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE EVE_ID_EVENTO = " + aKey
				+ " AND COD_TIPO_NOTIFICA='E' AND DATA_AVVENUTA_NOTIFICA IS NULL ";
		setStatement(lSql);
	}

	public void ricercaNotificaEsecuzioneByEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE EVE_ID_EVENTO = " + aKey + " AND COD_TIPO_NOTIFICA='E' ";
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = getSqlQuerySelect() + getSqlQueryFrom();

		return lStatement;
	}

	protected String getSqlQuerySelect() {
		String lStatement = new String("");

		lStatement += " SELECT " + " N.ID_NOTIFICA, " + " N.COD_TIPO_NOTIFICA, COD_NOT.RV_MEANING TIP_NOT,"
				+ " N.DATA_AVVENUTA_NOTIFICA," + " N.DATA_INVIO," + " N.COD_ESITO, COD_ESI.RV_MEANING ESITO,"
				+ " N.NOTE," + " N.COD_OPERATORE_INSERIMENTO," + " N.DATA_INSERIMENTO,"
				+ " N.COD_UFFICIO_INSERIMENTO," + " N.CODICE_OPERATORE_AGGIORNAMENTO,"
				+ " N.DATA_AGGIORNAMENTO," + " N.COD_UFFICIO_AGGIORNAMENTO," + " N.EVE_ID_EVENTO,"
				+ " N.AUT_EST_ID_AUTORITA_ESTERNA," + " N.SOG_ID_SOGGETTO,"
				+ " N.AVV_ID_AVVOCATO_FASCICOLO_SIEP," + " N.AVV_ID_AVVOCATO_FASCICOLO_SIUS,"
				+ " N.AVV_ID_AVVOCATO_FASCICOLO_SIGE," + " N.SOLLECITO, N.CSS_ID_CSSA,"
				+ " N.UFF_COD_UFFICIO," + " N.CUR_ID_CURATORE," +
				// modifica relativa al tipo istituto
				" N.IST_DET_ID_ISTITUTO_DETENZIONE," + " N.ID_PARTE_UDIENZA"
				//MEV_2023-33
				+ " , N.ID_CIVILMENTE_OBBLIGATO ";

		return lStatement;
	}

	protected String getSqlQueryFrom() {
		String lStatement = new String("");

		lStatement += " FROM NOTIFICA N join CG_REF_CODES COD_NOT"
				+ " on (COD_NOT.RV_DOMAIN = 'TIPO_NOTIFICA' AND COD_NOT.RV_LOW_VALUE = N.COD_TIPO_NOTIFICA )"
				+ " join CG_REF_CODES COD_ESI on (COD_ESI.RV_DOMAIN = 'ESITO_NOTIFICA' AND COD_ESI.RV_LOW_VALUE = N.COD_ESITO ) ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		NotificaModel aModel = new NotificaModel();
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
		aModel.setCurIdCuratore(getBigDecimal("CUR_ID_CURATORE"));

		aModel.setIdParteUdienza(getBigDecimal("ID_PARTE_UDIENZA"));
		
		aModel.setIdCivilmenteObbligato(getBigDecimal("ID_CIVILMENTE_OBBLIGATO"));

		return aModel;
	}

	public String setCondizione(NotificaModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " WHERE ID_NOTIFICA = " + aKey;
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce il numero di notifiche legate ad un evento ancora da
	 * notificare, ovvero con data di avvenuta notifica a null.
	 * </p>
	 * 
	 * @param BigDecimal
	 *            aIdEve : Identificativo Evento
	 * @return int : numero di record selezionati
	 * @throws DAOException
	 */

	public int getNumNotificheDaNotificareByEve(BigDecimal aIdEve) throws DAOException {

		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from NOTIFICA  where DATA_AVVENUTA_NOTIFICA IS NULL  AND EVE_ID_EVENTO = "
				+ aIdEve;
		setStatement(lStatement);

		this.start();
		if (this.next()) {
			lCount = this.getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce il numero di notifiche di tipo N legate ad un evento ancora
	 * da notificare, ovvero con data di avvenuta notifica a null.
	 * </p>
	 * 
	 * @param BigDecimal
	 *            aIdEve : Identificativo Evento
	 * @return int : numero di record selezionati
	 * @throws DAOException
	 */

	public int getNumNotificheSigeDaNotificareByEve(BigDecimal aIdEve) throws DAOException {

		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from NOTIFICA  where DATA_AVVENUTA_NOTIFICA IS NULL  AND EVE_ID_EVENTO = "
				+ aIdEve;
		lStatement += " AND COD_TIPO_NOTIFICA = 'N' ";
		setStatement(lStatement);

		this.start();
		if (this.next()) {
			lCount = this.getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce la data più recente di un gruppo di notifiche legate ad uno
	 * stesso evento.
	 * </p>
	 * 
	 * @param BigDecimal
	 *            aIdEve : Identificativo Evento
	 * @return Date data
	 * @throws DAOException
	 */

	public Date SelDataNotificaByEve(BigDecimal aIdEve) throws DAOException {
		// BigDecimal lCount = null;
		Date retData = null;

		String lStatement = "select max(DATA_AVVENUTA_NOTIFICA) as MAXDATA from NOTIFICA  where  EVE_ID_EVENTO = "
				+ aIdEve;
		setStatement(lStatement);

		this.start();
		if (this.next()) {
			retData = this.getDate("MAXDATA");
		}
		return retData;
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce la data più recente di un gruppo di notifiche legate ad uno
	 * stesso evento.
	 * </p>
	 * 
	 * @param BigDecimal
	 *            aIdEve : Identificativo Evento
	 * @return Date data
	 * @throws DAOException
	 */

	public Date SelDataNotificaSigeByEve(BigDecimal aIdEve) throws DAOException {
		// BigDecimal lCount = null;
		Date retData = null;

		String lStatement = "select max(DATA_AVVENUTA_NOTIFICA) as MAXDATA from NOTIFICA  where  EVE_ID_EVENTO = "
				+ aIdEve;
		lStatement += " AND COD_TIPO_NOTIFICA = 'N' ";
		setStatement(lStatement);

		this.start();
		if (this.next()) {
			retData = this.getDate("MAXDATA");
		}
		return retData;
	}

	// Ordinamento per TIPO NOTIFICA
	private String setOrderTipoNotifica() {
		String lOrder = new String();
		lOrder = " ORDER BY COD_TIPO_NOTIFICA asc, ID_NOTIFICA asc ";
		return lOrder;
	}

	public void ricercaNotificaByIdParteUdienza(BigDecimal aIdParteUdienza) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE ID_PARTE_UDIENZA = " + aIdParteUdienza;
		lSql += setOrderTipoNotifica(); // STUB 15/10/2008

		setStatement(lSql);
	}

	public void ricercaNotificaByIdParteUdienzaDifensore(BigDecimal aIdParteUdienza, BigDecimal aIdAvvocato)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE ID_PARTE_UDIENZA = " + aIdParteUdienza;
		lSql += " AND AVV_ID_AVVOCATO_FASCICOLO_SIGE = " + aIdAvvocato;

		setStatement(lSql);
	}

	public void ricercaNotificaByIdSoggetto(BigDecimal aIdParteUdienza) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE ID_PARTE_UDIENZA = " + aIdParteUdienza;
		lSql += " AND AVV_ID_AVVOCATO_FASCICOLO_SIGE IS NULL";

		setStatement(lSql);
	}

}