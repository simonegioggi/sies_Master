package siap.siep.luogodetenzione.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;

/**
 * <p>
 * Title: LuogoDetenzioneSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella LuogoDetenzione
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

public class LuogoDetenzioneSqlDAO extends SIAPSqlDAO {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public LuogoDetenzioneSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaLuogoDetenzione(LuogoDetenzioneModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);

		setStatement(lSql);
	}

	public void ricercaLuogoDetByFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQueryFascicolo(aKey);

		setStatement(lSql);
	}

	protected String getSqlQueryFascicolo(BigDecimal aKey) {

		String lSql = new String("");
		lSql = "SELECT " + " L.ID_LUOGO_DETENZIONE, " +
		// modifica relativa al tipo istituto
				" L.IST_DET_ID_ISTITUTO_DETENZIONE, " +
				// " L.COD_TIPO_ISTITUTO, TIPO_IST.RV_MEANING DESC_TIPO_ISTITUTO," +
				// " L.COD_LUOGO, LUOGO.DESCRIZIONE DESC_LUOGO, " +
				// " L.INDIRIZZO, " +
				// " L.DESCR, " +
				" L.NOTE, " + " L.DATA_INIZIO_DETENZIONE, " + " L.DATA_FINE_DETENZIONE, "
				+ " L.COD_OPERATORE_INSERIMENTO, " + " L.DATA_INSERIMENTO, " + " L.COD_UFFICIO_INSERIMENTO, "
				+ " L.COD_OPERATORE_AGGIORNAMENTO, " + " L.DATA_AGGIORNAMENTO, "
				+ " L.COD_UFFICIO_AGGIORNAMENTO, " + " L.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ " L.FAS_SIU_ID_FASCICOLO_SIUS, " + " L.POS_GIU_ID_POSIZIONE_GIURIDICA, " +
				// modifica relativa al tipo istituto
				" L.ALTRO_LUOGO " + " FROM LUOGO_DETENZIONE L,  " +
				// modifica relativa al tipo istituto
				// " CG_REF_CODES TIPO_IST, COMUNE LUOGO,
				" NOTIFICA N, EVENTO E, MISURA_ALTERNATIVA MA" + " WHERE " +
				// " TIPO_IST.RV_DOMAIN = 'TIPO_ISTITUTO' and TIPO_IST.RV_LOW_VALUE = COD_TIPO_ISTITUTO "
				// " AND LUOGO.COD_COMUNE = COD_LUOGO"+
				"  N.EVE_ID_EVENTO = E.ID_EVENTO  " + " AND E.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey
				+ " AND E.COD_TIPO_PROVVEDIMENTO='03'" + " AND MA.EVE_ID_EVENTO=E.ID_EVENTO "
				+ " AND N.COD_TIPO_NOTIFICA= 'E' " + " AND L.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		return lSql;
	}

	public void ricercaLuogoDetenzioneByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	public void ricercaLuogoDetenzioneCorrenteByFascicoloSiep(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		// lSql += " AND DATA_FINE_DETENZIONE IS NULL AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey ;
		// modifica relativa al tipo istituto
		// lSql += " AND ID_LUOGO_DETENZIONE = ( SELECT MAX(ID_LUOGO_DETENZIONE) FROM LUOGO_DETENZIONE WHERE
		// FAS_SIE_ID_FASCICOLO_SIEP = " + aKey + ")";
		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM LUOGO_DETENZIONE WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aKey + ")";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("XXXXX " + lSql);
		setStatement(lSql);
	}

	public void ricercaLuogoDetenzioneCorrenteByFascicoloSius(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		// modifica relativa al tipo istituto
		lSql += " DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM LUOGO_DETENZIONE WHERE FAS_SIU_ID_FASCICOLO_SIUS = "
				+ aKey + ")";
		lSql += " AND FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;

		setStatement(lSql);
	}

	public void ricercaLuoghiDetenzioneByFascicoloSius(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		// modifica relativa al tipo istituto
		lSql += " FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	public void ricercaLuogoDetenzioneByIdPosizione(BigDecimal aIdPosizione) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdPosizione(aIdPosizione);

		setStatement(lSql);
	}

	protected String getSqlQuery() {

		String lStatement = new String("");

		lStatement = "SELECT " + " ID_LUOGO_DETENZIONE, " +
		// modifica relativa al tipo istituto
				" IST_DET_ID_ISTITUTO_DETENZIONE, " +
				// " COD_TIPO_ISTITUTO, TIPO_IST.RV_MEANING DESC_TIPO_ISTITUTO," +
				// " COD_LUOGO, LUOGO.DESCRIZIONE DESC_LUOGO, " +
				// " INDIRIZZO, " +
				// " DESCR, " +
				" NOTE, " + " DATA_INIZIO_DETENZIONE, " + " DATA_FINE_DETENZIONE, "
				+ " COD_OPERATORE_INSERIMENTO, " + " DATA_INSERIMENTO, " + " COD_UFFICIO_INSERIMENTO, "
				+ " COD_OPERATORE_AGGIORNAMENTO, " + " DATA_AGGIORNAMENTO, " + " COD_UFFICIO_AGGIORNAMENTO, "
				+ " FAS_SIE_ID_FASCICOLO_SIEP, " + " FAS_SIU_ID_FASCICOLO_SIUS, "
				+ " POS_GIU_ID_POSIZIONE_GIURIDICA, " +
				// modifica relativa al tipo istituto
				" ALTRO_LUOGO " +
				// modifica relativa al tipo istituto
				" FROM LUOGO_DETENZIONE " + // CG_REF_CODES TIPO_IST, COMUNE LUOGO" +
				" WHERE ";// TIPO_IST.RV_DOMAIN = 'TIPO_ISTITUTO' and TIPO_IST.RV_LOW_VALUE =
							// COD_TIPO_ISTITUTO " +
		// " AND LUOGO.COD_COMUNE = COD_LUOGO";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		LuogoDetenzioneModel aModel = new LuogoDetenzioneModel();

		aModel.setIdLuogoDetenzione(getBigDecimal("ID_LUOGO_DETENZIONE"));
		// modifica relativa al tipo istituto
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
		// aModel.setCodTipoIstituto(getString("COD_TIPO_ISTITUTO") );
		// aModel.setDescrTipoIstituto(getString("DESC_TIPO_ISTITUTO") );
		// aModel.setCodLuogo(getString("COD_LUOGO") );
		// aModel.setDescrLuogo(getString("DESC_LUOGO") );
		// aModel.setIndirizzo(getString("INDIRIZZO") );
		// aModel.setDescr(getString("DESCR") );
		aModel.setNote(getString("NOTE"));
		aModel.setDataInizioDetenzione(getDate("DATA_INIZIO_DETENZIONE"));
		aModel.setDataFineDetenzione(getDate("DATA_FINE_DETENZIONE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setPosGiuIdPosizioneGiuridica(getBigDecimal("POS_GIU_ID_POSIZIONE_GIURIDICA"));
		// modifica relativa al tipo istituto
		aModel.setAltroLuogo(getString("ALTRO_LUOGO"));

		return aModel;
	}

	public String setCondizione(LuogoDetenzioneModel aModel) {
		String lCondizioni = new String();

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		// modifica relativa al tipo istituto
		// return " AND ID_LUOGO_DETENZIONE = " + aKey;
		return " ID_LUOGO_DETENZIONE = " + aKey;
	}

	public String setCondizioniByIdPosizione(BigDecimal aIdPosizione) {
		// modifica relativa al tipo istituto
		// return " AND POS_GIU_ID_POSIZIONE_GIURIDICA = " + aIdPosizione;
		return " POS_GIU_ID_POSIZIONE_GIURIDICA = " + aIdPosizione;
	}

}