package siap.siep.misurasicurezza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * 
 * @author d.fiorletta
 *
 */
public class FascMsToFascSiepSqlDAO extends SIAPSqlDAO {

	public FascMsToFascSiepSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		FascMsToFascSiepModel aModel = new FascMsToFascSiepModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFascMsToFascSiep(getBigDecimal("ID_FASC_MS_TO_FASC_SIEP"));
		//
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setChiaveAnnoSiep(getBigDecimal("CHIAVE_ANNO_SIEP"));
		aModel.setChiaveProgrSiep(getBigDecimal("CHIAVE_PROGR_SIEP"));
		aModel.setChiaveUfficioSiep(getString("CHIAVE_UFFICIO_SIEP"));
		aModel.setDescTipoUfficioSiep(getString("UFF_SIEP_DESC"));
		aModel.setDescComuneUfficioSiep(getString("COMU_SIEP_DESC"));

		aModel.setCodTipoRelazioneMS(getString("COD_TIPO_RELAZIONE_MS"));

		//
		aModel.setFasSieIdFascicoloCollegato(getBigDecimal("FAS_SIE_ID_FASCICOLO_COLLEGATO"));
		aModel.setChiaveAnnoSiepCollegato(getBigDecimal("CHIAVE_ANNO_SIEP_COLLEGATO"));
		aModel.setChiaveProgrSiepCollegato(getBigDecimal("CHIAVE_PROGR_SIEP_COLLEGATO"));
		aModel.setChiaveUfficioSiepCollegato(getString("CHIAVE_UFFICIO_SIEP_COLLEGATO"));
		aModel.setDescTipoUfficioSiepCollegato(getString("UFF_SIEP_COLL_DESC"));
		aModel.setDescComuneUfficioSiepCollegato(getString("COMU_SIEP_COLL_DESC"));

		//
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));

		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		aModel.setMesIdMessaggio(getBigDecimal("MES_ID_MESSAGGIO"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));

		// 25-01-2015
		aModel.setDataCumulo(getDate("DATA_CUMULO"));

		return aModel;
	}

	/**
	 * 
	 * @return
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT ID_FASC_MS_TO_FASC_SIEP " + ", FAS_SIE_ID_FASCICOLO_SIEP "
				+ ", CHIAVE_ANNO_SIEP, CHIAVE_PROGR_SIEP, CHIAVE_UFFICIO_SIEP " + ", COD_TIPO_RELAZIONE_MS "
				+ ", FAS_SIE_ID_FASCICOLO_COLLEGATO "
				+ ", CHIAVE_ANNO_SIEP_COLLEGATO, CHIAVE_PROGR_SIEP_COLLEGATO, CHIAVE_UFFICIO_SIEP_COLLEGATO "
				+ ", MES_ID_MESSAGGIO, EVE_ID_EVENTO, DATA_CUMULO "
				+ ", COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO "
				+ ", COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += ", DESC_UFF_SIEP.RV_MEANING UFF_SIEP_DESC, COMUME_SIEP.DESCRIZIONE COMU_SIEP_DESC";
		lStatement += ", DESC_UFF_SIEP_COLL.RV_MEANING UFF_SIEP_COLL_DESC, COMUME_SIEP_COLL.DESCRIZIONE COMU_SIEP_COLL_DESC";
		lStatement += " FROM FASC_MS_TO_FASC_SIEP ";
		lStatement += " , UFFICIO UFF_SIEP, CG_REF_CODES DESC_UFF_SIEP, COMUNE COMUME_SIEP ";
		lStatement += " , UFFICIO UFF_SIEP_COLL, CG_REF_CODES DESC_UFF_SIEP_COLL, COMUNE COMUME_SIEP_COLL ";
		lStatement += " WHERE 1=1"; // non eliminare
		// --=========================================================
		lStatement += " AND CHIAVE_UFFICIO_SIEP = UFF_SIEP.COD_UFFICIO ";
		lStatement += " AND UFF_SIEP.COD_TIPO_UFFICIO = DESC_UFF_SIEP.RV_LOW_VALUE ";
		lStatement += " AND DESC_UFF_SIEP.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_SIEP.COD_COMUNE = COMUME_SIEP.COD_COMUNE ";
		// --=========================================================
		lStatement += " AND CHIAVE_UFFICIO_SIEP_COLLEGATO = UFF_SIEP_COLL.COD_UFFICIO ";
		lStatement += " AND UFF_SIEP_COLL.COD_TIPO_UFFICIO = DESC_UFF_SIEP_COLL.RV_LOW_VALUE ";
		lStatement += " AND DESC_UFF_SIEP_COLL.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_SIEP_COLL.COD_COMUNE = COMUME_SIEP_COLL.COD_COMUNE ";

		return lStatement;
	}

	/**
	 * 
	 * @param aModel
	 * @return
	 */
	public String setCondizione(FascMsToFascSiepModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdFascMsToFascSiep() != null)
			lCondizioni += " AND ID_FASC_MS_TO_FASC_SIEP = " + aModel.getIdFascMsToFascSiep();

		if (aModel.getFasSieIdFascicoloSiep() != null)
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();

		if (aModel.getChiaveAnnoSiep() != null)
			lCondizioni += " AND CHIAVE_ANNO_SIEP = " + aModel.getChiaveAnnoSiep();

		if (aModel.getChiaveProgrSiep() != null)
			lCondizioni += " AND CHIAVE_PROGR_SIEP = " + aModel.getChiaveProgrSiep();

		if (aModel.getChiaveUfficioSiep() != null && !"".equals(aModel.getChiaveUfficioSiep()))
			lCondizioni += " AND CHIAVE_UFFICIO_SIEP = '" + aModel.getChiaveUfficioSiep() + "'";

		if (aModel.getCodTipoRelazioneMS() != null && !"".equals(aModel.getCodTipoRelazioneMS()))
			lCondizioni += " AND COD_TIPO_RELAZIONE_MS = '" + aModel.getCodTipoRelazioneMS() + "'";

		if (aModel.getFasSieIdFascicoloCollegato() != null)
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_COLLEGATO = " + aModel.getFasSieIdFascicoloCollegato();
		if (aModel.getChiaveAnnoSiepCollegato() != null)
			lCondizioni += " AND CHIAVE_ANNO_SIEP_COLLEGATO = " + aModel.getChiaveAnnoSiepCollegato();
		if (aModel.getChiaveProgrSiepCollegato() != null)
			lCondizioni += " AND CHIAVE_PROGR_SIEP_COLLEGATO = " + aModel.getChiaveProgrSiepCollegato();
		if (aModel.getChiaveUfficioSiepCollegato() != null
				&& !"".equals(aModel.getChiaveUfficioSiepCollegato()))
			lCondizioni += " AND CHIAVE_UFFICIO_SIEP_COLLEGATO = '" + aModel.getChiaveUfficioSiepCollegato()
					+ "'";

		if (aModel.getMesIdMessaggio() != null)
			lCondizioni += " AND MES_ID_MESSAGGIO = " + aModel.getMesIdMessaggio();
		if (aModel.getEveIdEvento() != null)
			lCondizioni += " AND EVE_ID_EVENTO = " + aModel.getEveIdEvento();

		// 25-01-2015
		if (aModel.getDataCumulo() != null)
			lCondizioni += " AND DATA_CUMULO = " + aModel.getDataCumulo();

		if (aModel.getCodOperatoreInserimento() != null && !"".equals(aModel.getCodOperatoreInserimento()))
			lCondizioni += " AND COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "'";
		if (aModel.getDataInserimento() != null)
			lCondizioni += " AND DATA_INSERIMENTO = " + aModel.getDataInserimento();
		if (aModel.getCodUfficioInserimento() != null && !"".equals(aModel.getCodUfficioInserimento()))
			lCondizioni += " AND COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
		if (aModel.getCodOperatoreAggiornamento() != null
				&& !"".equals(aModel.getCodOperatoreAggiornamento()))
			lCondizioni += " AND COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "'";
		if (aModel.getDataAggiornamento() != null)
			lCondizioni += " AND DATA_AGGIORNAMENTO = " + aModel.getDataAggiornamento();
		if (aModel.getCodOperatoreAggiornamento() != null
				&& !"".equals(aModel.getCodOperatoreAggiornamento()))
			lCondizioni += " AND COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento() + "'";

		return lCondizioni;
	}

	/**
	 * Metodo generico di ricerca. Imposta le condizioni di ricerca a partire dal contenuto del Model passato
	 * in input. Per ogni property contenente valori significativi viene impostata una condizione Key =
	 * property.
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascMsToFascSiep(FascMsToFascSiepModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		lSql += " " + setOrderByIns();
		setStatement(lSql);
	}

	/**
	 * Recupera tutti i collegamenti per il fascicolo passato in input, sia i fascicoli da cui è stato
	 * iscritto, sia i fascicoli che nati dal procedimento.
	 * 
	 * @param idFascioloSiep
	 * @throws DAOException
	 */
	public void ricercaCollegamentiSiep(BigDecimal idFascioloSiep) throws DAOException {
		String lSql = getSqlQuery();

		// lSql +=
		// " AND ( FAS_SIE_ID_FASCICOLO_SIEP = "+idFascioloSiep+" OR FAS_SIE_ID_FASCICOLO_CLASSE_IV = "+idFascioloSiep+" ) ";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + idFascioloSiep;
		lSql += " ORDER BY DATA_INSERIMENTO ASC ";
		setStatement(lSql);
	}
	
	/**
	 * Recupera tutti i collegamenti per il fascicolo passato in input, sia i fascicoli da cui è stato
	 * iscritto, sia i fascicoli che nati dal procedimento.
	 * 
	 * @param idFascioloSiep
	 * @throws DAOException
	 */
	public void ricercaCollegamentiSiepSorv(BigDecimal idFascioloSiep) throws DAOException {
		String lSql = getSqlQuery();

		// lSql +=
		// " AND ( FAS_SIE_ID_FASCICOLO_SIEP = "+idFascioloSiep+" OR FAS_SIE_ID_FASCICOLO_CLASSE_IV = "+idFascioloSiep+" ) ";
		lSql += " AND FAS_SIE_ID_FASCICOLO_COLLEGATO = " + idFascioloSiep;
		lSql += " ORDER BY DATA_INSERIMENTO ASC ";
		setStatement(lSql);
	}

	/**
	 * Imposta la ricerca in base alla primary key (ID) del fascicolo su cui erano originariamente iscritte le
	 * Misure di Sicurzza.
	 * 
	 * @param aKey
	 *            - Id del fascicolo di classe I
	 * @throws DAOException
	 */
	public void ricercaByKeyFascEsecuzione(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " " + setOrderByIns();
		setStatement(lSql);
	}

	/**
	 * 
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaByEveIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND EVE_ID_EVENTO = " + aKey;
		lSql += " " + setOrderByIns();
		setStatement(lSql);
	}

	public String setOrderByIns() {
		return " ORDER BY DATA_INSERIMENTO ";
	}

	// MEV_39: aggiunto metodo di ricerca
	public void ricercaDatiFascColl(String codUfficioUtenteConnesso, String lId, BigDecimal keyFas) {

		String lStatement = new String("");
		lStatement += "SELECT FASCMS.*, null UFF_SIEP_DESC, null COMU_SIEP_DESC,"
				+ " null UFF_SIEP_COLL_DESC, null COMU_SIEP_COLL_DESC"
				+ " FROM SCADENZARIO_SIEP SCA, FASC_MS_TO_FASC_SIEP FASCMS"
				+ " WHERE SCA.FAS_SIE_ID_FASCICOLO_SIEP = FASCMS.FAS_SIE_ID_FASCICOLO_SIEP"
				+ " AND SCA.ID_SCADENZARIO_SIEP = " + lId + " AND SCA.FAS_SIE_ID_FASCICOLO_SIEP = "
				+ keyFas + " AND SCA.COD_TIPO_SCADENZARIO = '20'"
				+ " AND SCA.COD_UFFICIO_INSERIMENTO = '" + codUfficioUtenteConnesso + "'"
				+ " AND SCA.COD_STATO_NOTIFICA = 'N'";

		setStatement(lStatement);
	}
	
	// MEV_39 03/01/2018 ***** inizio ***** aggiunto metodo di ricerca
	public void ricercaFascSiepColl(BigDecimal keyFas) {

		String lStatement = new String("");
		lStatement += "SELECT FASCMS.*, null UFF_SIEP_DESC, null COMU_SIEP_DESC,"
				+ " null UFF_SIEP_COLL_DESC, null COMU_SIEP_COLL_DESC"
				+ " FROM FASC_MS_TO_FASC_SIEP FASCMS"
				+ " WHERE FASCMS.FAS_SIE_ID_FASCICOLO_COLLEGATO = " + keyFas;

		setStatement(lStatement);
	}
	// MEV_39 fine *****
}