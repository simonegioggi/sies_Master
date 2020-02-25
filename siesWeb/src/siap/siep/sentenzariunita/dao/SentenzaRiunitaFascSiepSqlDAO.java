package siap.siep.sentenzariunita.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: SentenzariunitaFascSiepSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella SentenzariunitaFascSiep
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
public class SentenzaRiunitaFascSiepSqlDAO extends SqlDAO {

	public SentenzaRiunitaFascSiepSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Effettua la ricerca delle Sentenze riunte associati a una data sentenza:
	 * SentenzaRiunitaFascSiepModel.getSentenzaRiunitaModel().getSenIdSentenza()
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaSentenzaRiunitaFascSiep(SentenzaRiunitaFascSiepModel aModel) throws DAOException {

		String lStatement = new String("");

		lStatement += "SELECT sentenzariunita_fasc_siep.fas_sie_id_fascicolo_siep,"
				+ " sentenzariunita_fasc_siep.ID_SENTENZARIUNITA_FASC_SIEP, "
				+ " sentenzariunita_fasc_siep.SEN_RIU_ID_SENTENZA_RIUNITA," + " ID_SENTENZA_RIUNITA, "
				+ " DATA_SENTENZA, ANNO_SENTENZA, NUMERO_SENTENZA, COD_TIPO_AUTORITA_EMITTENTE, "
				+ " COD_SEDE_NOTIZIA_REATO, LUOGO_REATO.DESCRIZIONE DESCR_LUOGO_REATO,"
				+ " TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, COD_AUTORITA_EMITTENTE,"
				+ " COD_LUOGO_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, SEZIONE_AUTORITA_EMITTENTE,"
				+ " ANNO_REGE_PM, NUMERO_REGE_PM, ANNO_REGE_GIP, NUMERO_REGE_GIP, ANNO_REGE_DIB, NUMERO_REGE_DIB, "
				+ " ANNO_REGE_CAS, NUMERO_REGE_CAS, COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO,"
				+ " COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO, SEN_ID_SENTENZA  "
				+ " FROM SENTENZA_RIUNITA, cg_ref_codes TIPO_AUTORITA_EMITTENTE,comune LUOGO_EMITTENTE,comune LUOGO_REATO,"
				+ " sentenzariunita_fasc_siep ";
		lStatement += " WHERE (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE) ";
		lStatement += " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUOGO_EMITTENTE) AND (LUOGO_REATO.COD_COMUNE = COD_SEDE_NOTIZIA_REATO) ";
		lStatement += " AND sentenzariunita_fasc_siep.fas_sie_id_fascicolo_siep(+)= "
				+ aModel.getFasSieIdFascicoloSiep();
		lStatement += " AND sentenzariunita_fasc_siep.sen_riu_id_sentenza_riunita (+)=sentenza_riunita.id_sentenza_riunita ";
		lStatement += " AND SEN_ID_SENTENZA = " + aModel.getSentenzaRiunitaModel().getSenIdSentenza();
		lStatement += " ORDER BY DATA_SENTENZA DESC";

		setStatement(lStatement);

	}

	/**
	 * 
	 * @param aIdFascicoloSiep
	 * @throws DAOException
	 */
	public void ricercaSentenzaRiunitaFascSiepByIdFasSiep(BigDecimal aIdFascicoloSiep) throws DAOException {
		String lSql = "SELECT ID_SENTENZARIUNITA_FASC_SIEP, SEN_RIU_ID_SENTENZA_RIUNITA, FAS_SIE_ID_FASCICOLO_SIEP "
				+ "  FROM SENTENZARIUNITA_FASC_SIEP " + " WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aIdFascicoloSiep;

		setStatement(lSql);
	}

	public GenericModel getModelSemplice() throws DAOException {
		SentenzaRiunitaFascSiepModel aModel = new SentenzaRiunitaFascSiepModel();

		aModel.setIdSentenzariunitaFascSiep(getBigDecimal("ID_SENTENZARIUNITA_FASC_SIEP"));
		aModel.setSenRiuIdSentenzaRiunita(getBigDecimal("SEN_RIU_ID_SENTENZA_RIUNITA"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));

		return aModel;
	}

	public void ricercaSentenzaRiunitaFascSiepByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	/**
	 * 
	 * @param aIdFascicoloSiep
	 * @throws DAOException
	 * @since 30/09/2014
	 */
	public void ricercaSentenzaRiunitaFascSiepByIdFasciolo(BigDecimal aIdFascicoloSiep) throws DAOException {

		String lStatement = new String("");

		lStatement += "SELECT sentenzariunita_fasc_siep.fas_sie_id_fascicolo_siep,"
				+ " sentenzariunita_fasc_siep.ID_SENTENZARIUNITA_FASC_SIEP, "
				+ " sentenzariunita_fasc_siep.SEN_RIU_ID_SENTENZA_RIUNITA," + " ID_SENTENZA_RIUNITA, "
				+ " DATA_SENTENZA, ANNO_SENTENZA, NUMERO_SENTENZA, COD_TIPO_AUTORITA_EMITTENTE, "
				+ " COD_SEDE_NOTIZIA_REATO, LUOGO_REATO.DESCRIZIONE DESCR_LUOGO_REATO,"
				+ " TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, COD_AUTORITA_EMITTENTE,"
				+ " COD_LUOGO_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, SEZIONE_AUTORITA_EMITTENTE,"
				+ " ANNO_REGE_PM, NUMERO_REGE_PM, ANNO_REGE_GIP, NUMERO_REGE_GIP, ANNO_REGE_DIB, NUMERO_REGE_DIB, "
				+ " ANNO_REGE_CAS, NUMERO_REGE_CAS, COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO,"
				+ " COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO, SEN_ID_SENTENZA  "
				+ " FROM SENTENZA_RIUNITA, cg_ref_codes TIPO_AUTORITA_EMITTENTE,comune LUOGO_EMITTENTE,comune LUOGO_REATO,"
				+ " SENTENZARIUNITA_FASC_SIEP ";
		lStatement += " WHERE (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE) ";
		lStatement += " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUOGO_EMITTENTE) AND (LUOGO_REATO.COD_COMUNE = COD_SEDE_NOTIZIA_REATO) ";
		lStatement += " AND sentenzariunita_fasc_siep.sen_riu_id_sentenza_riunita = sentenza_riunita.id_sentenza_riunita ";
		lStatement += " AND sentenzariunita_fasc_siep.fas_sie_id_fascicolo_siep = " + aIdFascicoloSiep;
		lStatement += " ORDER BY DATA_SENTENZA DESC";

		setStatement(lStatement);

	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		return lStatement;
	}

	/**
	 * Estrae il model aggregato: SentenzaRiunitaFascSiepModel (SentenzaRiunitaModel)
	 * 
	 */
	public GenericModel getModel() throws DAOException {
		SentenzaRiunitaFascSiepModel aModel = new SentenzaRiunitaFascSiepModel();

		aModel.setIdSentenzariunitaFascSiep(getBigDecimal("ID_SENTENZARIUNITA_FASC_SIEP"));
		aModel.setSenRiuIdSentenzaRiunita(getBigDecimal("SEN_RIU_ID_SENTENZA_RIUNITA"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));

		SentenzaRiunitaModel lSRModel = new SentenzaRiunitaModel();
		aModel.setSentenzaRiunitaModel(lSRModel);
		aModel.getSentenzaRiunitaModel().setIdSentenzaRiunita(getBigDecimal("ID_SENTENZA_RIUNITA"));
		aModel.getSentenzaRiunitaModel().setDataSentenza(getDate("DATA_SENTENZA"));
		aModel.getSentenzaRiunitaModel().setAnnoSentenza(getBigDecimal("ANNO_SENTENZA"));
		aModel.getSentenzaRiunitaModel().setNumeroSentenza(getString("NUMERO_SENTENZA"));
		aModel.getSentenzaRiunitaModel()
				.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		aModel.getSentenzaRiunitaModel()
				.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
		aModel.getSentenzaRiunitaModel().setCodAutoritaEmittente(getString("COD_AUTORITA_EMITTENTE"));
		// aModel.setDescrAutoritaEmittente(getString("") );
		aModel.getSentenzaRiunitaModel().setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.getSentenzaRiunitaModel().setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		aModel.getSentenzaRiunitaModel().setSezioneAutoritaEmittente(getString("SEZIONE_AUTORITA_EMITTENTE"));
		aModel.getSentenzaRiunitaModel().setAnnoRegePm(getBigDecimal("ANNO_REGE_PM"));
		aModel.getSentenzaRiunitaModel().setNumeroRegePm(getString("NUMERO_REGE_PM"));
		aModel.getSentenzaRiunitaModel().setAnnoRegeGip(getBigDecimal("ANNO_REGE_GIP"));
		aModel.getSentenzaRiunitaModel().setNumeroRegeGip(getString("NUMERO_REGE_GIP"));
		aModel.getSentenzaRiunitaModel().setAnnoRegeDib(getBigDecimal("ANNO_REGE_DIB"));
		aModel.getSentenzaRiunitaModel().setNumeroRegeDib(getString("NUMERO_REGE_DIB"));
		aModel.getSentenzaRiunitaModel().setAnnoRegeCas(getBigDecimal("ANNO_REGE_CAS"));
		aModel.getSentenzaRiunitaModel().setNumeroRegeCas(getString("NUMERO_REGE_CAS"));
		aModel.getSentenzaRiunitaModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.getSentenzaRiunitaModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.getSentenzaRiunitaModel().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.getSentenzaRiunitaModel()
				.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.getSentenzaRiunitaModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.getSentenzaRiunitaModel().setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.getSentenzaRiunitaModel().setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));
		aModel.getSentenzaRiunitaModel().setCodSedeNotiziaReato(getString("COD_SEDE_NOTIZIA_REATO"));
		aModel.getSentenzaRiunitaModel().setDescrSedeNotiziaReato(getString("DESCR_LUOGO_REATO"));

		return aModel;
	}

	public String setCondizione(SentenzaRiunitaFascSiepModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_SENTENZARIUNITA_FASC_SIEP = " + aKey;
	}

}