package siap.siep.archiviazione.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.archiviazione.model.ArchiviazioneModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ArchiviazioneSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Archiviazione
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
public class ArchiviazioneSqlDAO extends SqlDAO {
	
	public ArchiviazioneSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaArchiviazione(ArchiviazioneModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaArchiviazioneByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaArchiviazioneByIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " AND EVE_ID_EVENTO = " + aKey;
		lSql += setOrderDesc();
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_ARCHIVIAZIONE, "
				+ "COD_TIPO_PROVVEDIMENTO, CODTIPRO.RV_MEANING COD_TIP_PRO, " + "DATA_EMISSIONE, "
				+ "DATA_RICEZIONE, " + "ANNO_NOTA, " + "NUM_NOTA, "
				+ "COD_PROVVEDIMENTO, CODPRO.RV_MEANING COD_PRO, " + "ANNO_PROVVEDIMENTO, "
				+ "NUM_PROVVEDIMENTO, "
				+ "COD_TIPO_PROVVEDIMENTO_ARC, CODTIPROAR.RV_MEANING COD_TIP_PRO_ARC, " + "DATA_DEFINIZIONE, "
				+ "COD_OGGETTO_DEFINIZIONE, CODOGGET.RV_MEANING COD_OGG, "
				+ "COD_TIPO_EMITTENTE, CODEMI.RV_MEANING COD_TIP_EMI, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, CODAUTEMI.RV_MEANING COD_TIP_AUT_EMI, "
				+ "COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, " + "INDIRIZZO_EMITTENTE, "
				+ "ALTRA_AUTORITA, " + "NOTE, " + "FLAG_ANNULLAMENTO, " + "DATA_ANNULLAMENTO, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, " + "EVE_ID_EVENTO, " + "IST_DET_ID_ISTITUTO_DETENZIONE, "
				+ "CSS_ID_CSSA, " +
				// 02-04-2015
				"CHIAVE_ANNO, CHIAVE_PROGR, " +
				// 22-06-2015
				"COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO";
		//
		lStatement += " FROM ARCHIVIAZIONE, CG_REF_CODES CODTIPRO, CG_REF_CODES CODPRO, CG_REF_CODES CODTIPROAR,";
		lStatement += " CG_REF_CODES CODEMI, CG_REF_CODES CODAUTEMI,CG_REF_CODES CODOGGET, COMUNE LUOEMI";
		lStatement += " WHERE ";
		lStatement += " ARCHIVIAZIONE.COD_TIPO_PROVVEDIMENTO = CODTIPRO.RV_LOW_VALUE AND CODTIPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
		lStatement += " ARCHIVIAZIONE.COD_PROVVEDIMENTO = CODPRO.RV_LOW_VALUE AND CODPRO.RV_DOMAIN = 'DEFI_ALTRO' AND";
		lStatement += " ARCHIVIAZIONE.COD_TIPO_PROVVEDIMENTO_ARC = CODTIPROAR.RV_LOW_VALUE AND CODTIPROAR.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_ARC' AND";
		lStatement += " ARCHIVIAZIONE.COD_OGGETTO_DEFINIZIONE = CODOGGET.RV_LOW_VALUE AND CODOGGET.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND";
		lStatement += " ARCHIVIAZIONE.COD_TIPO_EMITTENTE = CODEMI.RV_LOW_VALUE AND CODEMI.RV_DOMAIN = 'TIPO_EMITTENTE' AND";
		// Ticket#202209120114 — SIEP 481/2021 procura generale di bari. In caso di autorità mittente = PDR Presidenza della repubblica
		// la join con i domini TIPO_AUTORITA OR TIPO_UFFICIO filliva in quanto il PDR non è censito si tali domini
		// si aggiunge la join con TIPO_UFFICIO_DEFI (dominio da cui vengono attinti alcuni valori) 
		// lStatement += " ARCHIVIAZIONE.COD_TIPO_AUTORITA_EMITTENTE = CODAUTEMI.RV_LOW_VALUE AND (CODAUTEMI.RV_DOMAIN = 'TIPO_AUTORITA' OR CODAUTEMI.RV_DOMAIN = 'TIPO_UFFICIO')AND";
		lStatement += " (   ARCHIVIAZIONE.COD_TIPO_AUTORITA_EMITTENTE = CODAUTEMI.RV_LOW_VALUE AND (CODAUTEMI.RV_DOMAIN = 'TIPO_AUTORITA' OR CODAUTEMI.RV_DOMAIN = 'TIPO_UFFICIO') ";
		lStatement += "   OR (    ARCHIVIAZIONE.COD_TIPO_AUTORITA_EMITTENTE = 'PDR' " +
	                       "  AND ARCHIVIAZIONE.COD_TIPO_AUTORITA_EMITTENTE = CODAUTEMI.RV_HIGH_VALUE " + //n.b RV_HIGH_VALUE e non RV_LOW_VALUE
	                       "  AND CODAUTEMI.RV_DOMAIN = 'TIPO_UFFICIO_DEFI' " +
	                       " ) " +
                      " ) AND " ;
		// Ticket#202209120114 — FINE
		lStatement += " ARCHIVIAZIONE.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		ArchiviazioneModel aModel = new ArchiviazioneModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdArchiviazione(getBigDecimal("ID_ARCHIVIAZIONE"));
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setDescrTipoProvvedimento(getString("COD_TIP_PRO"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setDataRicezione(getDate("DATA_RICEZIONE"));
		aModel.setAnnoNota(getBigDecimal("ANNO_NOTA"));
		aModel.setNumNota(getString("NUM_NOTA"));
		aModel.setCodProvvedimento(getString("COD_PROVVEDIMENTO"));
		aModel.setDescrProvvedimento(getString("COD_PRO"));
		aModel.setAnnoProvvedimento(getBigDecimal("ANNO_PROVVEDIMENTO"));
		aModel.setNumProvvedimento(getString("NUM_PROVVEDIMENTO"));
		aModel.setCodTipoProvvedimentoArc(getString("COD_TIPO_PROVVEDIMENTO_ARC"));
		aModel.setDescrTipoProvvedimentoArc(getString("COD_TIP_PRO_ARC"));
		aModel.setDataDefinizione(getDate("DATA_DEFINIZIONE"));
		aModel.setCodOggettoDefinizione(getString("COD_OGGETTO_DEFINIZIONE"));
		aModel.setDescrOggettoDefinizione(getString("COD_OGG"));
		aModel.setCodTipoEmittente(getString("COD_TIPO_EMITTENTE"));
		aModel.setDescrTipoEmittente(getString("COD_TIP_EMI"));
		aModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		aModel.setDescrTipoAutoritaEmittente(getString("COD_TIP_AUT_EMI"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("LUO_EMI"));
		aModel.setIndirizzoEmittente(getString("INDIRIZZO_EMITTENTE"));
		aModel.setAltraAutorita(getString("ALTRA_AUTORITA"));
		aModel.setNote(getString("NOTE"));
		aModel.setFlagAnnullamento(getString("FLAG_ANNULLAMENTO"));
		aModel.setDataAnnullamento(getDate("DATA_ANNULLAMENTO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
		aModel.setCssIdCssa(getBigDecimal("CSS_ID_CSSA"));

		// 02-04-2015
		aModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		// 22-06-2015
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		return aModel;
	}

	public String setCondizione(ArchiviazioneModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_ARCHIVIAZIONE = " + aKey;
	}

	private String setOrderDesc() {
		String lCondizioni = " ORDER BY DATA_INSERIMENTO DESC, DATA_EMISSIONE DESC";

		return lCondizioni;
	}

}