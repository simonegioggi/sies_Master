package siap.siep.altrigradigiudizio.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;

/**
 * <p>
 * Title: AltriGradiGiudizioSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella AltriGradiGiudizio
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
public class AltriGradiGiudizioSqlDAO extends SqlDAO {

	public AltriGradiGiudizioSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaAltriGradiGiudizio(AltriGradiGiudizioModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql
				+ " ORDER BY ALTRI_GRADI_GIUDIZIO.ANNO_SENTENZA_I_GRADO, ALTRI_GRADI_GIUDIZIO.NUMERO_SENTENZA_I_GRADO");
	}

	public void ricercaAltriGradiGiudizioByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_ALTRIGRADIGIUDIZIO, " + "DATA_SENTENZA_I_GRADO, "
				+ "ANNO_SENTENZA_I_GRADO, " + "NUMERO_SENTENZA_I_GRADO, " + "COD_AUT_EMITT_SENT_I_GRADO, "
				+ "TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_AUT_EMITT_SENT_I_GRADO, "
				+ "COD_LUO_EMITT_SENT_I_GRADO, "
				+ "LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUO_EMITT_SENT_I_GRADO, "
				+ "NUM_SEZ_EMITT_SENT_I_GRADO, " + "COD_TIPO_SENTENZA_II_GRADO, "
				+ "TIPO_SENTENZA_II_GRADO.RV_MEANING DESCR_TIPO_SENTENZA_II_GRADO, "
				+ "DATA_SENTENZA_II_GRADO, " + "ANNO_SENTENZA_II_GRADO, " + "NUMERO_SENTENZA_II_GRADO, "
				+ "COD_AUT_EMITT_SENT_II_GRADO, "
				+ "TIPO_AUTORITA_PROVV_RIF.RV_MEANING DESCR_AUT_EMITT_SENT_II_GRADO, "
				+ "COD_LUO_EMITT_SENT_II_GRADO, "
				+ "LUOGO_EMITTENTE_RIF.DESCRIZIONE DESCR_LUO_EMITT_SENT_II_GRADO, "
				+ "NUM_SEZ_EMITT_SENT_II_GRADO, " + "ANNO_REG_GEN_CASSAZ, " + "NUMERO_REG_GEN_CASSAZ, "
				+ "ANNO_SENTENZA_CASSAZ, " + "NUMERO_SENTENZA_CASSAZ, " + "ANNO_RACC_GENEALE_II_GRADO, "
				+ "NUMERO_RACC_GENEALE_II_GRADO, " + "COD_TIPO_DECISIONE_CASSAZIONE, "
				+ "TIPO_DECISIONE_CASSAZIONE.RV_MEANING DESCR_TIPO_DECISIONE_CASS, " + "SEN_ID_SENTENZA, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "COD_TIPO_RITO ";
		lStatement += " FROM ALTRI_GRADI_GIUDIZIO, CG_REF_CODES TIPO_AUTORITA_EMITTENTE, COMUNE LUOGO_EMITTENTE, COMUNE LUOGO_EMITTENTE_RIF,";
		lStatement += " CG_REF_CODES TIPO_AUTORITA_PROVV_RIF,";
		lStatement += " CG_REF_CODES TIPO_DECISIONE_CASSAZIONE,  CG_REF_CODES TIPO_SENTENZA_II_GRADO";
		lStatement += " WHERE ";
		lStatement += " (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_AUT_EMITT_SENT_I_GRADO) ";
		lStatement += " AND (TIPO_AUTORITA_PROVV_RIF.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_PROVV_RIF.RV_LOW_VALUE=COD_AUT_EMITT_SENT_II_GRADO) ";
		lStatement += " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUO_EMITT_SENT_I_GRADO AND LUOGO_EMITTENTE_RIF.COD_COMUNE=COD_LUO_EMITT_SENT_II_GRADO)";
		lStatement += " AND (TIPO_DECISIONE_CASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' AND TIPO_DECISIONE_CASSAZIONE.RV_LOW_VALUE = COD_TIPO_DECISIONE_CASSAZIONE)";
		lStatement += " AND (TIPO_SENTENZA_II_GRADO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' AND TIPO_SENTENZA_II_GRADO.RV_LOW_VALUE = COD_TIPO_SENTENZA_II_GRADO)";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		AltriGradiGiudizioModel aModel = new AltriGradiGiudizioModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdAltrigradigiudizio(getBigDecimal("ID_ALTRIGRADIGIUDIZIO"));
		aModel.setDataSentenzaIGrado(getDate("DATA_SENTENZA_I_GRADO"));
		aModel.setAnnoSentenzaIGrado(getBigDecimal("ANNO_SENTENZA_I_GRADO"));
		aModel.setNumeroSentenzaIGrado(getString("NUMERO_SENTENZA_I_GRADO"));
		aModel.setCodAutEmittSentIGrado(getString("COD_AUT_EMITT_SENT_I_GRADO"));
		aModel.setDescrAutEmittSentIGrado(getString("DESCR_AUT_EMITT_SENT_I_GRADO"));
		aModel.setCodLuoEmittSentIGrado(getString("COD_LUO_EMITT_SENT_I_GRADO"));
		aModel.setDescrLuoEmittSentIGrado(getString("DESCR_LUO_EMITT_SENT_I_GRADO"));
		aModel.setNumSezEmittSentIGrado(getString("NUM_SEZ_EMITT_SENT_I_GRADO"));
		aModel.setCodTipoSentenzaIiGrado(getString("COD_TIPO_SENTENZA_II_GRADO"));
		aModel.setDescrTipoSentenzaIiGrado(getString("DESCR_TIPO_SENTENZA_II_GRADO"));
		aModel.setDataSentenzaIiGrado(getDate("DATA_SENTENZA_II_GRADO"));
		aModel.setAnnoSentenzaIiGrado(getBigDecimal("ANNO_SENTENZA_II_GRADO"));
		aModel.setNumeroSentenzaIiGrado(getString("NUMERO_SENTENZA_II_GRADO"));
		aModel.setCodAutEmittSentIiGrado(getString("COD_AUT_EMITT_SENT_II_GRADO"));
		aModel.setDescrAutEmittSentIiGrado(getString("DESCR_AUT_EMITT_SENT_II_GRADO"));
		aModel.setCodLuoEmittSentIiGrado(getString("COD_LUO_EMITT_SENT_II_GRADO"));
		aModel.setDescrLuoEmittSentIiGrado(getString("DESCR_LUO_EMITT_SENT_II_GRADO"));
		aModel.setNumSezEmittSentIiGrado(getString("NUM_SEZ_EMITT_SENT_II_GRADO"));
		aModel.setAnnoRegGenCassaz(getBigDecimal("ANNO_REG_GEN_CASSAZ"));
		aModel.setNumeroRegGenCassaz(getString("NUMERO_REG_GEN_CASSAZ"));
		aModel.setAnnoSentenzaCassaz(getBigDecimal("ANNO_SENTENZA_CASSAZ"));
		aModel.setNumeroSentenzaCassaz(getString("NUMERO_SENTENZA_CASSAZ"));
		aModel.setAnnoRaccGenealeIiGrado(getBigDecimal("ANNO_RACC_GENEALE_II_GRADO"));
		aModel.setNumeroRaccGenealeIiGrado(getString("NUMERO_RACC_GENEALE_II_GRADO"));
		aModel.setCodTipoDecisioneCassazione(getString("COD_TIPO_DECISIONE_CASSAZIONE"));
		aModel.setDescrTipoDecisioneCassazione(getString("DESCR_TIPO_DECISIONE_CASS"));
		aModel.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setCodTipoRito(getString("COD_TIPO_RITO"));
		return aModel;
	}

	public String setCondizione(AltriGradiGiudizioModel aModel) {
		String lCondizioni = new String();
		if ((aModel.getSenIdSentenza() != null) && (!aModel.getSenIdSentenza().toString().equals(""))) {
			lCondizioni += " AND SEN_ID_SENTENZA = " + aModel.getSenIdSentenza();
		}

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_ALTRIGRADIGIUDIZIO = " + aKey;
	}

}