package siap.siep.modulocumulo.dao;

/**
* <p>Title: NotificaCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella NotificaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.siep.modulocumulo.model.NotificaCumuloModel;

public class NotificaCumuloSqlDAO extends SqlDAO {

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public NotificaCumuloSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaNotificaCumulo(NotificaCumuloModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 * 
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaNotificaCumuloByKey(BigDecimal aIdNotificaCumulo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdNotificaCumulo);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaNotificheCumuloByIdStatoEsec(BigDecimal aIdStatoEsec) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " AND STAT_ID_STATO_ESEC_TIT_CUM = " + aIdStatoEsec;

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 * 
	 * @return
	 ****************************************************************************/
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_NOTIFICA_CUMULO, "
				+ "COD_TIPO_NOTIFICA, TIPONOTIFICA.RV_MEANING DESC_NOTIFICA, " + "DATA_AVVENUTA_NOTIFICA, "
				+ "DATA_INVIO, " + "COD_ESITO, ESITONOTIFICA.RV_MEANING DESC_ESITO, " + "NOTE, "
				+ "STAT_ID_STATO_ESEC_TIT_CUM, " + "AUT_EST_ID_AUTORITA_ESTERNA, " + "SOG_ID_SOGGETTO, "
				+ "AVV_ID_AVVOCATO_FASCICOLO_SIEP, " + "UFF_COD_UFFICIO, " + "CSS_ID_CSSA, "
				+ "IST_DET_ID_ISTITUTO_DETENZIONE, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO ";

		lStatement += " FROM NOTIFICA_CUMULO ";
		lStatement += "LEFT OUTER JOIN CG_REF_CODES TIPONOTIFICA ON TIPONOTIFICA.RV_LOW_VALUE = COD_TIPO_NOTIFICA "
				+ " AND TIPONOTIFICA.RV_DOMAIN = 'TIPO_NOTIFICA' ";
		lStatement += "LEFT OUTER JOIN CG_REF_CODES ESITONOTIFICA ON ESITONOTIFICA.RV_LOW_VALUE = COD_ESITO "
				+ " AND ESITONOTIFICA.RV_DOMAIN = 'ESITO_NOTIFICA' ";

		lStatement += " WHERE 1=1 ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		NotificaCumuloModel aModel = new NotificaCumuloModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdNotificaCumulo(getBigDecimal("ID_NOTIFICA_CUMULO"));
		aModel.setCodTipoNotifica(getString("COD_TIPO_NOTIFICA"));
		aModel.setDescrTipoNotifica(getString("DESC_NOTIFICA"));
		aModel.setDataAvvenutaNotifica(getDate("DATA_AVVENUTA_NOTIFICA"));
		aModel.setDataInvio(getDate("DATA_INVIO"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("DESC_ESITO"));
		aModel.setNote(getString("NOTE"));

		aModel.setStatIdStatoEsecTitCum(getBigDecimal("STAT_ID_STATO_ESEC_TIT_CUM"));
		aModel.setAutEstIdAutoritaEsterna(getBigDecimal("AUT_EST_ID_AUTORITA_ESTERNA"));
		aModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		aModel.setAvvIdAvvocatoFascicoloSiep(getBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SIEP"));
		aModel.setUffCodUfficio(getString("UFF_COD_UFFICIO"));
		aModel.setCssIdCssa(getBigDecimal("CSS_ID_CSSA"));
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));

		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(NotificaCumuloModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdNotificaCumulo() != null) {
			lCondizioni += " and ID_NOTIFICA_CUMULO = " + aModel.getIdNotificaCumulo() + "";
		}
		if (aModel.getCodTipoNotifica() != null && aModel.getCodTipoNotifica().length() > 0) {
			lCondizioni += " and COD_TIPO_NOTIFICA = '" + aModel.getCodTipoNotifica() + "' ";
		}
		if (aModel.getDataAvvenutaNotifica() != null) {
			lCondizioni += " and to_char(DATA_AVVENUTA_NOTIFICA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAvvenutaNotifica(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataInvio() != null) {
			lCondizioni += " and to_char(DATA_INVIO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInvio(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodEsito() != null && aModel.getCodEsito().length() > 0) {
			lCondizioni += " and COD_ESITO = '" + aModel.getCodEsito() + "' ";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getStatIdStatoEsecTitCum() != null) {
			lCondizioni += " and STAT_ID_STATO_ESEC_TIT_CUM = " + aModel.getStatIdStatoEsecTitCum() + "";
		}
		if (aModel.getAutEstIdAutoritaEsterna() != null) {
			lCondizioni += " and AUT_EST_ID_AUTORITA_ESTERNA = " + aModel.getAutEstIdAutoritaEsterna() + "";
		}
		if (aModel.getSogIdSoggetto() != null) {
			lCondizioni += " and SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto() + "";
		}
		if (aModel.getAvvIdAvvocatoFascicoloSiep() != null) {
			lCondizioni += " and AVV_ID_AVVOCATO_FASCICOLO_SIEP = " + aModel.getAvvIdAvvocatoFascicoloSiep()
					+ "";
		}
		if (aModel.getUffCodUfficio() != null && aModel.getUffCodUfficio().length() > 0) {
			lCondizioni += " and UFF_COD_UFFICIO = '" + aModel.getUffCodUfficio() + "' ";
		}
		if (aModel.getCssIdCssa() != null) {
			lCondizioni += " and CSS_ID_CSSA = " + aModel.getCssIdCssa() + "";
		}
		if (aModel.getIstDetIdIstitutoDetenzione() != null
				&& aModel.getIstDetIdIstitutoDetenzione().length() > 0) {
			lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione()
					+ "' ";
		}
		if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' ";
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
			lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "' ";
		}
		if (aModel.getDataAggiornamento() != null) {
			lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
		}
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di select per chiave
	 * 
	 * @param aKey
	 * @return
	 ****************************************************************************/
	public String setCondizioniByKey(BigDecimal aIdNotificaCumulo) {
		String lCondizioni = new String();

		lCondizioni += " and ID_NOTIFICA_CUMULO = " + aIdNotificaCumulo;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sezione order by
	 * 
	 * @return
	 ****************************************************************************/
	protected String getOrderBy() {
		String orderBy = new String("");
		// orderBy = " ORDER BY ";
		return orderBy;
	}

}