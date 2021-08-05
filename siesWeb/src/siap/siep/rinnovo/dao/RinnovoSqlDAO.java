package siap.siep.rinnovo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siep.rinnovo.model.RinnovoModel;

/**
 * <p>
 * Title: RinnovoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Rinnovo
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

public class RinnovoSqlDAO extends SqlDAO {
	public RinnovoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRinnovo(RinnovoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);

		setStatement(lSql);
	}

	public void ricercaRinnovoIdNotifica(BigDecimal aKeyNot) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND NOT_ID_NOTIFICA = " + aKeyNot;

		lSql += " AND FLAG_DOCUMENTO_REGISTRATO = 'S'";

		lSql += " ORDER BY DATA_RINNOVO DESC, ID_RINNOVO DESC";

		setStatement(lSql);
	}

	// Paolo Cherubini 06/02/2012
	// Aggiunto metodo relativamente alla segnalazione bb/rr/004 v.a. StatoEsecuzioneController
	// prendo il più recente
	public void ricercaRinnovoByKeyEvento(BigDecimal aKeyNot) throws DAOException {
		String lSql = getSqlQueryxEvento();
		lSql += " AND ID_EVENTO = " + aKeyNot;

		lSql += " AND ID_EVENTO=NOTIFICA.EVE_ID_EVENTO ";
		lSql += " AND NOTIFICA.ID_NOTIFICA=RINNOVO.NOT_ID_NOTIFICA ";

		lSql += " ORDER BY DATA_RINNOVO DESC, ID_RINNOVO DESC";

		setStatement(lSql);
	}

	/**
	 * Ticket#202106220110 - per la stampa mi interessa solo un rinnovo con
	 * DATA_RINNOVO valorizzata
	 * Metodo aggiunto in sostituzione del metodo ricercaRinnovoByKeyEvento precedentemente 
	 * utilizzato.
	 * @param aKeyNot
	 * @throws DAOException
	 */
	public void ricercaUltimoRinnovoByKeyEvento(BigDecimal aKeyNot) throws DAOException {
		String lSql = getSqlQueryxEvento();
		lSql += " AND ID_EVENTO = " + aKeyNot;

		lSql += " AND ID_EVENTO=NOTIFICA.EVE_ID_EVENTO ";
		lSql += " AND NOTIFICA.ID_NOTIFICA=RINNOVO.NOT_ID_NOTIFICA ";
		
		// Ticket#202106220110 - Recupero solo la RINNOVAZIONI con DATA_RINNOVO valorizzata
		lSql += " AND DATA_RINNOVO IS NOT NULL ";
		//lSql += " AND RINNOVO.FLAG_DOCUMENTO_REGISTRATO = 'S' ";  // e validati
		// Ticket#202106220110 - FINE
		lSql += " ORDER BY DATA_RINNOVO DESC, ID_RINNOVO DESC";

		setStatement(lSql);
	}
	
	public void ricercaRinnovoIdNotificaCodTipoRinnovo(BigDecimal aKeyNot, String[] aCodTipoRinnovo)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND NOT_ID_NOTIFICA = " + aKeyNot;

		if (aCodTipoRinnovo != null) {
			if (aCodTipoRinnovo.length > 0) {
				lSql += " AND COD_TIPO_RINNOVO IN (";

				for (int i = 0; i < aCodTipoRinnovo.length; i++) {
					lSql += "'" + aCodTipoRinnovo[i] + "'";

					if (aCodTipoRinnovo.length > 1 && i < aCodTipoRinnovo.length - 1) {
						lSql += ",";
					}
				}

				lSql += ")";
			}
		}

		lSql += " AND FLAG_DOCUMENTO_REGISTRATO = 'S'";

		lSql += " ORDER BY DATA_RINNOVO DESC";

		setStatement(lSql);
	}

	public void ricercaRinnovoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_RINNOVO, " + "COD_TIPO_RINNOVO,COD_TIP_RIN.RV_MEANING DESCRINNOVO, "
				+ "DATA_RINNOVO, " + "COD_TIPO_AUTORITA_RINNOVO, COD_UFF_RIN.RV_MEANING AUTORITA, "
				+ "COD_LUOGO_RINNOVO, COD_LUO_RIN.DESCRIZIONE COMUNI, " + "NOTE, " + "DOC_BLOB, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "NOT_ID_NOTIFICA, " + "VER_ID_VERBALE, " + "FLAG_DOCUMENTO_REGISTRATO, "
				+ "TEM_ID_TEMPLATE, " + "NUOVO_LUOGO_NOTIFICA ";

		lStatement += " FROM RINNOVO,CG_REF_CODES COD_UFF_RIN, COMUNE COD_LUO_RIN,CG_REF_CODES COD_TIP_RIN ";
		lStatement += " WHERE  COD_UFF_RIN.RV_DOMAIN = 'TIPO_AUTORITA' AND COD_UFF_RIN.RV_LOW_VALUE = COD_TIPO_AUTORITA_RINNOVO";
		lStatement += " AND COD_LUO_RIN.COD_COMUNE = COD_LUOGO_RINNOVO";
		lStatement += " AND COD_TIP_RIN.RV_DOMAIN = 'TIPO_RINNOVO' AND COD_TIP_RIN.RV_LOW_VALUE = COD_TIPO_RINNOVO ";

		return lStatement;
	}

	protected String getSqlQueryxEvento() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_RINNOVO, " + "COD_TIPO_RINNOVO,COD_TIP_RIN.RV_MEANING DESCRINNOVO, "
				+ "DATA_RINNOVO, " + "COD_TIPO_AUTORITA_RINNOVO, COD_UFF_RIN.RV_MEANING AUTORITA, "
				+ "COD_LUOGO_RINNOVO, COD_LUO_RIN.DESCRIZIONE COMUNI, " + "RINNOVO.NOTE, "
				+ "RINNOVO.DOC_BLOB, " + "RINNOVO.COD_OPERATORE_INSERIMENTO, " + "RINNOVO.DATA_INSERIMENTO, "
				+ "RINNOVO.COD_UFFICIO_INSERIMENTO, " + "RINNOVO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "RINNOVO.DATA_AGGIORNAMENTO, " + "RINNOVO.COD_UFFICIO_AGGIORNAMENTO, " + "NOT_ID_NOTIFICA, "
				+ "VER_ID_VERBALE, " + "RINNOVO.FLAG_DOCUMENTO_REGISTRATO, " + "RINNOVO.TEM_ID_TEMPLATE, "
				+ "NUOVO_LUOGO_NOTIFICA ";

		lStatement += " FROM EVENTO,NOTIFICA,RINNOVO,CG_REF_CODES COD_UFF_RIN, COMUNE COD_LUO_RIN,CG_REF_CODES COD_TIP_RIN ";
		lStatement += " WHERE  COD_UFF_RIN.RV_DOMAIN = 'TIPO_AUTORITA' AND COD_UFF_RIN.RV_LOW_VALUE = COD_TIPO_AUTORITA_RINNOVO";
		lStatement += " AND COD_LUO_RIN.COD_COMUNE = COD_LUOGO_RINNOVO";
		lStatement += " AND COD_TIP_RIN.RV_DOMAIN = 'TIPO_RINNOVO' AND COD_TIP_RIN.RV_LOW_VALUE = COD_TIPO_RINNOVO ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		RinnovoModel aModel = new RinnovoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdRinnovo(getBigDecimal("ID_RINNOVO"));
		aModel.setCodTipoRinnovo(getString("COD_TIPO_RINNOVO"));
		aModel.setDescrTipoRinnovo(getString("DESCRINNOVO"));
		aModel.setDataRinnovo(getDate("DATA_RINNOVO"));
		aModel.setCodTipoAutoritaRinnovo(getString("COD_TIPO_AUTORITA_RINNOVO"));
		aModel.setDescrTipoAutoritaRinnovo(getString("AUTORITA"));
		aModel.setCodLuogoRinnovo(getString("COD_LUOGO_RINNOVO"));
		aModel.setDescrLuogoRinnovo(getString("COMUNI"));
		aModel.setNote(getString("NOTE"));
		// aModel.setDocBlob(getBlob("DOC_BLOB") );
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setNotIdNotifica(getBigDecimal("NOT_ID_NOTIFICA"));
		aModel.setVerIdVerbale(getBigDecimal("VER_ID_VERBALE"));
		aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
		aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
		aModel.setNuovoLuogoNotifica(getString("NUOVO_LUOGO_NOTIFICA"));

		return aModel;
	}

	public String setCondizione(RinnovoModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_RINNOVO = " + aKey;
	}

}