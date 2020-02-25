package siap.siep.agdgfascicolosiep.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.agdgfascicolosiep.model.AgdgFascicoloSiepModel;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: AgdgFascicoloSiepSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella AgdgFascicoloSiep
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
public class AgdgFascicoloSiepSqlDAO extends SqlDAO {

	public AgdgFascicoloSiepSqlDAO(Connection con) {
		super(con);
	}

	public void ricercaAgdgFascicoloSiep(AgdgFascicoloSiepModel aModel) throws DAOException {
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
				+ "COD_TIPO_RITO, "
				+ "ID_AGDG_FASCICOLO_SIEP, AGDG_ID_ALTRIGRADIGIUDIZIO, FAS_SIE_ID_FASCICOLO_SIEP ";

		lStatement += " FROM ALTRI_GRADI_GIUDIZIO, CG_REF_CODES TIPO_AUTORITA_EMITTENTE, COMUNE LUOGO_EMITTENTE, COMUNE LUOGO_EMITTENTE_RIF,";
		lStatement += " CG_REF_CODES TIPO_AUTORITA_PROVV_RIF,";
		lStatement += " CG_REF_CODES TIPO_DECISIONE_CASSAZIONE,  CG_REF_CODES TIPO_SENTENZA_II_GRADO, AGDG_FASCICOLO_SIEP";
		lStatement += " WHERE ";
		lStatement += " (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_AUT_EMITT_SENT_I_GRADO) ";
		lStatement += " AND (TIPO_AUTORITA_PROVV_RIF.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_PROVV_RIF.RV_LOW_VALUE=COD_AUT_EMITT_SENT_II_GRADO) ";
		lStatement += " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUO_EMITT_SENT_I_GRADO AND LUOGO_EMITTENTE_RIF.COD_COMUNE=COD_LUO_EMITT_SENT_II_GRADO)";
		lStatement += " AND (TIPO_DECISIONE_CASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' AND TIPO_DECISIONE_CASSAZIONE.RV_LOW_VALUE = COD_TIPO_DECISIONE_CASSAZIONE)";
		lStatement += " AND (TIPO_SENTENZA_II_GRADO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' AND TIPO_SENTENZA_II_GRADO.RV_LOW_VALUE = COD_TIPO_SENTENZA_II_GRADO)";
		lStatement += " AND (AGDG_FASCICOLO_SIEP.AGDG_ID_ALTRIGRADIGIUDIZIO(+) = ALTRI_GRADI_GIUDIZIO.ID_ALTRIGRADIGIUDIZIO)";
		lStatement += " AND (AGDG_FASCICOLO_SIEP.fas_sie_id_fascicolo_siep(+)= "
				+ aModel.getFasSieIdFascicoloSiep() + ")";
		lStatement += " AND (SEN_ID_SENTENZA = " + aModel.getAltriGradiGiudizioModel().getSenIdSentenza()
				+ ")";
		setStatement(lStatement);
	}

	/**
	 * 
	 * @param aIdFascicoloSiep
	 * @throws DAOException
	 */
	public void ricercaAgdgFascicoloSiepByIdFasSiep(BigDecimal aIdFascicoloSiep) throws DAOException {
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
				+ "COD_TIPO_RITO, "
				+ "ID_AGDG_FASCICOLO_SIEP, AGDG_ID_ALTRIGRADIGIUDIZIO, FAS_SIE_ID_FASCICOLO_SIEP "
				+ " FROM ALTRI_GRADI_GIUDIZIO, CG_REF_CODES TIPO_AUTORITA_EMITTENTE, COMUNE LUOGO_EMITTENTE, COMUNE LUOGO_EMITTENTE_RIF,"
				+ " CG_REF_CODES TIPO_AUTORITA_PROVV_RIF,"
				+ " CG_REF_CODES TIPO_DECISIONE_CASSAZIONE,  CG_REF_CODES TIPO_SENTENZA_II_GRADO, AGDG_FASCICOLO_SIEP"
				+ " WHERE "
				+ " (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_AUT_EMITT_SENT_I_GRADO) "
				+ " AND (TIPO_AUTORITA_PROVV_RIF.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_PROVV_RIF.RV_LOW_VALUE=COD_AUT_EMITT_SENT_II_GRADO) "
				+ " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUO_EMITT_SENT_I_GRADO AND LUOGO_EMITTENTE_RIF.COD_COMUNE=COD_LUO_EMITT_SENT_II_GRADO)"
				+ " AND (TIPO_DECISIONE_CASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' AND TIPO_DECISIONE_CASSAZIONE.RV_LOW_VALUE = COD_TIPO_DECISIONE_CASSAZIONE)"
				+ " AND (TIPO_SENTENZA_II_GRADO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' AND TIPO_SENTENZA_II_GRADO.RV_LOW_VALUE = COD_TIPO_SENTENZA_II_GRADO)"
				+ " AND (AGDG_FASCICOLO_SIEP.AGDG_ID_ALTRIGRADIGIUDIZIO = ALTRI_GRADI_GIUDIZIO.ID_ALTRIGRADIGIUDIZIO) "
				+ " AND (AGDG_FASCICOLO_SIEP.fas_sie_id_fascicolo_siep = " + aIdFascicoloSiep + ")";
		// " AND (AGDG_FASCICOLO_SIEP.AGDG_ID_ALTRIGRADIGIUDIZIO(+) =
		// ALTRI_GRADI_GIUDIZIO.ID_ALTRIGRADIGIUDIZIO)"+
		// " AND (AGDG_FASCICOLO_SIEP.fas_sie_id_fascicolo_siep(+)= " + aIdFascicoloSiep+")";
		setStatement(lStatement);
	}

	public void ricercaAgdgFascicoloSiepByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_AGDG_FASCICOLO_SIEP, " + "AGDG_ID_ALTRIGRADIGIUDIZIO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP ";
		lStatement += " FROM AGDG_FASCICOLO_SIEP";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		AgdgFascicoloSiepModel aModel = new AgdgFascicoloSiepModel();
		AltriGradiGiudizioModel lagdgModel = new AltriGradiGiudizioModel();

		aModel.setIdAgdgFascicoloSiep(getBigDecimal("ID_AGDG_FASCICOLO_SIEP"));
		aModel.setAgdgIdAltrigradigiudizio(getBigDecimal("AGDG_ID_ALTRIGRADIGIUDIZIO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));

		// Inserire le opportune set delle descrizioni!
		aModel.setAltriGradiGiudizioModel(lagdgModel);

		aModel.getAltriGradiGiudizioModel().setIdAltrigradigiudizio(getBigDecimal("ID_ALTRIGRADIGIUDIZIO"));
		aModel.getAltriGradiGiudizioModel().setDataSentenzaIGrado(getDate("DATA_SENTENZA_I_GRADO"));
		aModel.getAltriGradiGiudizioModel().setAnnoSentenzaIGrado(getBigDecimal("ANNO_SENTENZA_I_GRADO"));
		aModel.getAltriGradiGiudizioModel().setNumeroSentenzaIGrado(getString("NUMERO_SENTENZA_I_GRADO"));
		aModel.getAltriGradiGiudizioModel().setCodAutEmittSentIGrado(getString("COD_AUT_EMITT_SENT_I_GRADO"));
		aModel.getAltriGradiGiudizioModel()
				.setDescrAutEmittSentIGrado(getString("DESCR_AUT_EMITT_SENT_I_GRADO"));
		aModel.getAltriGradiGiudizioModel().setCodLuoEmittSentIGrado(getString("COD_LUO_EMITT_SENT_I_GRADO"));
		aModel.getAltriGradiGiudizioModel()
				.setDescrLuoEmittSentIGrado(getString("DESCR_LUO_EMITT_SENT_I_GRADO"));
		aModel.getAltriGradiGiudizioModel().setNumSezEmittSentIGrado(getString("NUM_SEZ_EMITT_SENT_I_GRADO"));
		aModel.getAltriGradiGiudizioModel()
				.setCodTipoSentenzaIiGrado(getString("COD_TIPO_SENTENZA_II_GRADO"));
		aModel.getAltriGradiGiudizioModel()
				.setDescrTipoSentenzaIiGrado(getString("DESCR_TIPO_SENTENZA_II_GRADO"));
		aModel.getAltriGradiGiudizioModel().setDataSentenzaIiGrado(getDate("DATA_SENTENZA_II_GRADO"));
		aModel.getAltriGradiGiudizioModel().setAnnoSentenzaIiGrado(getBigDecimal("ANNO_SENTENZA_II_GRADO"));
		aModel.getAltriGradiGiudizioModel().setNumeroSentenzaIiGrado(getString("NUMERO_SENTENZA_II_GRADO"));
		aModel.getAltriGradiGiudizioModel()
				.setCodAutEmittSentIiGrado(getString("COD_AUT_EMITT_SENT_II_GRADO"));
		aModel.getAltriGradiGiudizioModel()
				.setDescrAutEmittSentIiGrado(getString("DESCR_AUT_EMITT_SENT_II_GRADO"));
		aModel.getAltriGradiGiudizioModel()
				.setCodLuoEmittSentIiGrado(getString("COD_LUO_EMITT_SENT_II_GRADO"));
		aModel.getAltriGradiGiudizioModel()
				.setDescrLuoEmittSentIiGrado(getString("DESCR_LUO_EMITT_SENT_II_GRADO"));
		aModel.getAltriGradiGiudizioModel()
				.setNumSezEmittSentIiGrado(getString("NUM_SEZ_EMITT_SENT_II_GRADO"));
		aModel.getAltriGradiGiudizioModel().setAnnoRegGenCassaz(getBigDecimal("ANNO_REG_GEN_CASSAZ"));
		aModel.getAltriGradiGiudizioModel().setNumeroRegGenCassaz(getString("NUMERO_REG_GEN_CASSAZ"));
		aModel.getAltriGradiGiudizioModel().setAnnoSentenzaCassaz(getBigDecimal("ANNO_SENTENZA_CASSAZ"));
		aModel.getAltriGradiGiudizioModel().setNumeroSentenzaCassaz(getString("NUMERO_SENTENZA_CASSAZ"));
		aModel.getAltriGradiGiudizioModel()
				.setAnnoRaccGenealeIiGrado(getBigDecimal("ANNO_RACC_GENEALE_II_GRADO"));
		aModel.getAltriGradiGiudizioModel()
				.setNumeroRaccGenealeIiGrado(getString("NUMERO_RACC_GENEALE_II_GRADO"));
		aModel.getAltriGradiGiudizioModel()
				.setCodTipoDecisioneCassazione(getString("COD_TIPO_DECISIONE_CASSAZIONE"));
		aModel.getAltriGradiGiudizioModel()
				.setDescrTipoDecisioneCassazione(getString("DESCR_TIPO_DECISIONE_CASS"));
		aModel.getAltriGradiGiudizioModel().setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));
		aModel.getAltriGradiGiudizioModel()
				.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.getAltriGradiGiudizioModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.getAltriGradiGiudizioModel().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.getAltriGradiGiudizioModel()
				.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.getAltriGradiGiudizioModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.getAltriGradiGiudizioModel()
				.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.getAltriGradiGiudizioModel().setCodTipoRito(getString("COD_TIPO_RITO"));
		return aModel;
	}

	public String setCondizione(AgdgFascicoloSiepModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_AGDG_FASCICOLO_SIEP = " + aKey;
	}

	public String setCondizioniByIdFascicoloSiep(BigDecimal aKey) {
		return " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
	}

}