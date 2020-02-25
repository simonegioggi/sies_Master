package siap.siep.continuazione.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.continuazione.model.ContinuazioneModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ContinuazioneSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Continuazione
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
public class ContinuazioneSqlDAO extends SIAPSqlDAO {

	public ContinuazioneSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaContinuazione(ContinuazioneModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		lSql += " " + setOrderByProgressivo();

		setStatement(lSql);
	}

	public void ricercaContinuazioneByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	public void ricercaContinuazioneByIdPenaComplessiva(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdPenaComplessiva(aKey);
		lSql += " " + setOrderByProgressivo();

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_CONTINUAZIONE, " + "PROGR_CONTINUAZIONE, "
				+ "COD_TIPO_CONTINUAZIONE, TIPO_CONTINUAZIONE.RV_MEANING DESCR_TIPO_CONT, "
				+ "COD_TIPO_AUTORITA, TIPO_AUTORITA.RV_MEANING DESCR_TIPO_AUT, "
				+ "COD_LUOGO_AUTORITA, LUOGO_AUT.DESCRIZIONE DESCR_LUOGO_AUT, " + "DATA_SENTENZA, "
				+ "ANNO_SENTENZA, " + "NUM_SENTENZA, " + "ANNO_REGE_PM, " + "NUM_REGE_PM, "
				+ "ANNO_REGE_GIP, " + "NUM_REGE_GIP, " + "ANNO_REGE_DIB, " + "NUM_REGE_DIB, "
				+ "ANNO_REGE_CAS, " + "NUM_REGE_CAS, " + "ANNO_REGE_CAP, " + "NUM_REGE_CAP, "
				+ "ANNO_REGE_CASAP, " + "NUM_REGE_CASAP, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " + "PEN_COM_ID_PENA_COMPLESSIVA ";
		lStatement += " FROM CONTINUAZIONE, CG_REF_CODES TIPO_CONTINUAZIONE, CG_REF_CODES TIPO_AUTORITA, COMUNE LUOGO_AUT ";
		lStatement += " WHERE (TIPO_CONTINUAZIONE.RV_DOMAIN = 'TIPO_CONTINUAZIONE' AND TIPO_CONTINUAZIONE.RV_LOW_VALUE = COD_TIPO_CONTINUAZIONE)";
		lStatement += " AND (TIPO_AUTORITA.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA.RV_LOW_VALUE = COD_TIPO_AUTORITA)";
		lStatement += " AND (LUOGO_AUT.COD_COMUNE = COD_LUOGO_AUTORITA)";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		ContinuazioneModel aModel = new ContinuazioneModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdContinuazione(getBigDecimal("ID_CONTINUAZIONE"));
		aModel.setProgrContinuazione(getBigDecimal("PROGR_CONTINUAZIONE"));
		aModel.setCodTipoContinuazione(getString("COD_TIPO_CONTINUAZIONE"));
		aModel.setDescrTipoContinuazione(getString("DESCR_TIPO_CONT"));
		aModel.setCodTipoAutorita(getString("COD_TIPO_AUTORITA"));
		aModel.setDescrTipoAutorita(getString("DESCR_TIPO_AUT"));
		aModel.setCodLuogoAutorita(getString("COD_LUOGO_AUTORITA"));
		aModel.setDescrLuogoAutorita(getString("DESCR_LUOGO_AUT"));
		aModel.setDataSentenza(getDate("DATA_SENTENZA"));
		aModel.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA"));
		aModel.setNumSentenza(getString("NUM_SENTENZA"));
		aModel.setAnnoRegePm(getBigDecimal("ANNO_REGE_PM"));
		aModel.setNumRegePm(getString("NUM_REGE_PM"));
		aModel.setAnnoRegeGip(getBigDecimal("ANNO_REGE_GIP"));
		aModel.setNumRegeGip(getString("NUM_REGE_GIP"));
		aModel.setAnnoRegeDib(getBigDecimal("ANNO_REGE_DIB"));
		aModel.setNumRegeDib(getString("NUM_REGE_DIB"));
		aModel.setAnnoRegeCas(getBigDecimal("ANNO_REGE_CAS"));
		aModel.setNumRegeCas(getString("NUM_REGE_CAS"));
		aModel.setAnnoRegeCap(getBigDecimal("ANNO_REGE_CAP"));
		aModel.setNumRegeCap(getString("NUM_REGE_CAP"));
		aModel.setAnnoRegeCasap(getBigDecimal("ANNO_REGE_CASAP"));
		aModel.setNumRegeCasap(getString("NUM_REGE_CASAP"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setPenComIdPenaComplessiva(getBigDecimal("PEN_COM_ID_PENA_COMPLESSIVA"));

		return aModel;
	}

	public String setCondizione(ContinuazioneModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_CONTINUAZIONE = " + aKey;
	}

	public String setCondizioniByIdPenaComplessiva(BigDecimal aKeyPena) {
		return " AND PEN_COM_ID_PENA_COMPLESSIVA = " + aKeyPena;
	}

	public String setOrderByProgressivo() {
		return " ORDER BY PROGR_CONTINUAZIONE";
	}

	public BigDecimal getProgressivoContinuazione(BigDecimal aKeyPenaCompl) throws DAOException {
		String lStatement = new String();

		lStatement += "SELECT MAX(PROGR_CONTINUAZIONE) aMAX";
		lStatement += " FROM CONTINUAZIONE";
		lStatement += " WHERE PEN_COM_ID_PENA_COMPLESSIVA = " + aKeyPenaCompl;

		setStatement(lStatement);

		this.start();

		BigDecimal lProgressivo = null;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lProgressivo = this.getBigDecimal("aMAX");

		this.stop();

		if (lProgressivo == null)
			lProgressivo = new BigDecimal(0);

		return lProgressivo;
	}

}