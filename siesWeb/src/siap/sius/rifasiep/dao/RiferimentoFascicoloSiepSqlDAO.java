package siap.sius.rifasiep.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: RiferimentoFascicoloSiepSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella RiferimentoFascicoloSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class RiferimentoFascicoloSiepSqlDAO extends SqlDAO
 {

	public RiferimentoFascicoloSiepSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRiferimentoFascicoloSiep(RiferimentoFascicoloSiepModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaRiferimentoFascicoloSiepByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);

	}

	protected String getSqlQuery() {
		String lStatement = new String("");
		lStatement += " SELECT "
				+ "ID_RIFERIMENTO_FASCICOLO_SIEP, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "ANNO_FASCICOLO_SIEP, "
				+ "PROGR_FASCICOLO_SIEP, "
				+ "COD_UFF_FASCICOLO_SIEP, "
				+ "DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, "
				+ "COD_TIPO_PROVVEDIMENTO, " + "DESCR_TIPO_PROVV.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, "
				+ "DATA_PROVVEDIMENTO, " + "ANNO_PROVVEDIMENTO, " + "NUMERO_PROVVEDIMENTO, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, "
				+ "DESCR_AUTORITA_EMIT.RV_MEANING DESCR_AUTORITA_EMITTENTE, " + "COD_LUOGO_EMITTENTE, "
				+ "DESCR_LUOGO_EMIT.DESCRIZIONE DESCR_LUOGO_EMITTENTE, " + "DATA_IRREVOCABILITA, "
				+ "DATA_FINE_VALIDITA, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "FAS_SIU_ID_FASCICOLO_SIUS, " + "NVL (NOTE, '-') NOTE, "
				+ "FLAG_MS_SN, " + "FLAG_FAS_SIUS_UNIF_SN ";
		lStatement += " FROM RIFERIMENTO_FASCICOLO_SIEP RFS, CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF, UFFICIO UFF, CG_REF_CODES DESCR_TIPO_PROVV ,COMUNE DESCR_LUOGO_EMIT, CG_REF_CODES DESCR_AUTORITA_EMIT ";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		RiferimentoFascicoloSiepModel aModel = new RiferimentoFascicoloSiepModel();
		// Inserire le opportune set delle descrizioni!
		aModel.setIdRiferimentoFascicoloSiep(getBigDecimal("ID_RIFERIMENTO_FASCICOLO_SIEP"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setAnnoFascicoloSiep(getBigDecimal("ANNO_FASCICOLO_SIEP"));
		aModel.setProgrFascicoloSiep(getBigDecimal("PROGR_FASCICOLO_SIEP"));
		aModel.setCodUffFascicoloSiep(getString("COD_UFF_FASCICOLO_SIEP"));
		aModel.setDescrUffFascicoloSiep(getString("DESCR_TIPO_UFFICIO") + " di "
				+ getString("DESCR_COMUNE_UFFICIO"));
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
		aModel.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		aModel.setAnnoProvvedimento(getBigDecimal("ANNO_PROVVEDIMENTO"));
		aModel.setNumeroProvvedimento(getString("NUMERO_PROVVEDIMENTO"));
		aModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		aModel.setDescrTipoAutoritaEmittente(getString("DESCR_AUTORITA_EMITTENTE"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		aModel.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDescrUfficioInserimento("");
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setDescrUfficioAggiornamento("");
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setNote(getString("NOTE"));
		aModel.setFlagMS(getString("FLAG_MS_SN"));
		aModel.setFlagFasSiusUnif(getString("FLAG_FAS_SIUS_UNIF_SN"));
		return aModel;
	}

	public String setCondizione(RiferimentoFascicoloSiepModel aModel) {
		String lCondizioni = new String();

//		boolean lInserito = false;
		lCondizioni += " WHERE DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND NVL(UFF.COD_TIPO_UFFICIO,'-') = DESCR_TIPO_UFF.RV_LOW_VALUE";
		lCondizioni += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lCondizioni += " AND RFS.COD_UFF_FASCICOLO_SIEP = UFF.COD_UFFICIO ";
		lCondizioni += " AND DESCR_TIPO_PROVV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND RFS.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROVV.RV_LOW_VALUE ";
		lCondizioni += " AND DESCR_AUTORITA_EMIT.RV_DOMAIN = 'TIPO_UFFICIO' AND RFS.COD_TIPO_AUTORITA_EMITTENTE = DESCR_AUTORITA_EMIT.RV_LOW_VALUE ";
		lCondizioni += " AND RFS.COD_LUOGO_EMITTENTE = DESCR_LUOGO_EMIT.COD_COMUNE ";
		if (aModel.getFasSiuIdFascicoloSius() != null)
			lCondizioni += " AND RFS.FAS_SIU_ID_FASCICOLO_SIUS = '" + aModel.getFasSiuIdFascicoloSius() + "'";
		if (aModel.getAnnoFascicoloSiep() != null)
			lCondizioni += " AND RFS.ANNO_FASCICOLO_SIEP = '" + aModel.getAnnoFascicoloSiep() + "'";
		if (aModel.getProgrFascicoloSiep() != null)
			lCondizioni += " AND RFS.PROGR_FASCICOLO_SIEP = '" + aModel.getProgrFascicoloSiep() + "'";
		if (aModel.getCodUffFascicoloSiep() != null && aModel.getCodUffFascicoloSiep() != "")
			lCondizioni += " AND RFS.COD_UFF_FASCICOLO_SIEP = '" + aModel.getCodUffFascicoloSiep() + "'";

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " WHERE ID_RIFERIMENTO_FASCICOLO_SIEP = " + aKey;
		lCondizioni += " AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND NVL(UFF.COD_TIPO_UFFICIO,'-') = DESCR_TIPO_UFF.RV_LOW_VALUE";
		lCondizioni += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lCondizioni += " AND RFS.COD_UFF_FASCICOLO_SIEP = UFF.COD_UFFICIO ";
		lCondizioni += " AND DESCR_TIPO_PROVV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND RFS.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROVV.RV_LOW_VALUE ";
		lCondizioni += " AND DESCR_AUTORITA_EMIT.RV_DOMAIN = 'TIPO_UFFICIO' AND RFS.COD_TIPO_AUTORITA_EMITTENTE = DESCR_AUTORITA_EMIT.RV_LOW_VALUE ";
		lCondizioni += " AND RFS.COD_LUOGO_EMITTENTE = DESCR_LUOGO_EMIT.COD_COMUNE ";

		return lCondizioni;
	}

	// STUB 14/04/2005 Nuova Condizione per IdFascicoloSius.
	public void ricercaRiferimentoFascicoloSiepByIdFasSius(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdFasSius(aKey);
		setStatement(lSql);
	}

	// STUB 14/04/2005 Nuova Condizione per IdFascicoloSius.
	public String setCondizioniByIdFasSius(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " WHERE FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;
		lCondizioni += " AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND NVL(UFF.COD_TIPO_UFFICIO,'-') = DESCR_TIPO_UFF.RV_LOW_VALUE";
		lCondizioni += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lCondizioni += " AND RFS.COD_UFF_FASCICOLO_SIEP = UFF.COD_UFFICIO ";
		lCondizioni += " AND DESCR_TIPO_PROVV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND RFS.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROVV.RV_LOW_VALUE ";
		lCondizioni += " AND DESCR_AUTORITA_EMIT.RV_DOMAIN = 'TIPO_UFFICIO' AND RFS.COD_TIPO_AUTORITA_EMITTENTE = DESCR_AUTORITA_EMIT.RV_LOW_VALUE ";
		lCondizioni += " AND RFS.COD_LUOGO_EMITTENTE = DESCR_LUOGO_EMIT.COD_COMUNE ";

		return lCondizioni;

	}

	// TODO carmela (da verificare)
	public void ricercaRifTitoloEsecutivoByIdFascicoloSius(BigDecimal aKey) throws DAOException {
		String lStatement = "";
		// Si costruisce la query relativa al Riferimento Titolo Esecutivo Principale.
		lStatement += getRiferimentoRifTitoloEsecutivoPrincipale(aKey);

		lStatement += " UNION ";
		// Si costruisce la query relativa al Riferimento Altri Titoli Esecutivi.
		lStatement += getRiferimentoRifAltroTitoloEsecutivo(aKey);

		// lStatement += setOrderAnnoProgr();
		setStatement(lStatement);

	}

	protected String getRiferimentoRifTitoloEsecutivoPrincipale(BigDecimal aKey) {
		String lStatement = new String();

		lStatement += "SELECT SENT.ID_SENTENZA ID_RIFERIMENTO_TITOLO_ESEC, ";
		lStatement += "		  F_SIEP.CHIAVE_ANNO ANNO_FASCICOLO_SIEP, F_SIEP.CHIAVE_PROGR PROGR_FASCICOLO_SIEP,";
		lStatement += "       DECODE(F_SIEP.FLAG_CUMULANTE,'S', F_SIEP.DATA_ISCRIZIONE,SENT.DATA_PROVVEDIMENTO) DATA_PROVVEDIMENTO,";
		lStatement += "       DESCR_AUTORITA_EMIT.RV_MEANING DESCR_AUTORITA_EMITTENTE,";
		lStatement += "       DESCR_LUOGO_EMIT.DESCRIZIONE DESCR_LUOGO_EMITTENTE,'N' FLAG_MS_SN, 'P' RIF_TITOLO_ESECUTIVO ";

		lStatement += " FROM FASCICOLO_SIUS F_SIUS, FASCICOLO_SIEP F_SIEP, SENTENZA SENT,";
		lStatement += "	     UFFICIO UFF, COMUNE DESCR_COM_UFF, CG_REF_CODES DESCR_TIPO_UFF,";
		lStatement += "	     CG_REF_CODES DESCR_TIPO_PROVV, COMUNE DESCR_LUOGO_EMIT, CG_REF_CODES DESCR_AUTORITA_EMIT ";

		lStatement += "	WHERE F_SIUS.FAS_SIE_ID_FASCICOLO_SIEP = F_SIEP.ID_FASCICOLO_SIEP";
		lStatement += "	  AND F_SIEP.SEN_ID_SENTENZA = SENT.ID_SENTENZA";
		lStatement += "	  AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND NVL(UFF.COD_TIPO_UFFICIO,'-') = DESCR_TIPO_UFF.RV_LOW_VALUE";
		lStatement += "	  AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += "	  AND F_SIEP.CHIAVE_UFFICIO = UFF.COD_UFFICIO";
		lStatement += "	  AND DESCR_TIPO_PROVV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND SENT.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROVV.RV_LOW_VALUE";
		lStatement += "	  AND DESCR_AUTORITA_EMIT.RV_DOMAIN = 'TIPO_UFFICIO' AND SENT.COD_TIPO_AUTORITA_EMITTENTE = DESCR_AUTORITA_EMIT.RV_LOW_VALUE";
		lStatement += "	  AND SENT.COD_LUOGO_EMITTENTE = DESCR_LUOGO_EMIT.COD_COMUNE";
		lStatement += "	  AND F_SIUS.ID_FASCICOLO_SIUS = '" + aKey + "' ";

		return lStatement;
	}

	protected String getRiferimentoRifAltroTitoloEsecutivo(BigDecimal aKey) {
		String lStatement = new String();

		lStatement += "SELECT ID_RIFERIMENTO_FASCICOLO_SIEP ID_RIFERIMENTO_TITOLO_ESEC,";
		lStatement += "       ANNO_FASCICOLO_SIEP ANNO_FASCICOLO_SIEP, PROGR_FASCICOLO_SIEP PROGR_FASCICOLO_SIEP,";
		lStatement += "       DATA_PROVVEDIMENTO DATA_PROVVEDIMENTO, DESCR_AUTORITA_EMIT.RV_MEANING DESCR_AUTORITA_EMITTENTE,";
		lStatement += "       DESCR_LUOGO_EMIT.DESCRIZIONE DESCR_LUOGO_EMITTENTE,";
		lStatement += "       DECODE(FLAG_MS_SN,'S', 'S', 'N') FLAG_MS_SN, 'R' RIF_TITOLO_ESECUTIVO";

		lStatement += "  FROM RIFERIMENTO_FASCICOLO_SIEP RFS, CG_REF_CODES DESCR_TIPO_UFF,";
		lStatement += "       COMUNE DESCR_COM_UFF, UFFICIO UFF, CG_REF_CODES DESCR_TIPO_PROVV,";
		lStatement += "       COMUNE DESCR_LUOGO_EMIT, CG_REF_CODES DESCR_AUTORITA_EMIT ";

		lStatement += "	WHERE DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND NVL(UFF.COD_TIPO_UFFICIO,'-') = DESCR_TIPO_UFF.RV_LOW_VALUE";
		lStatement += "	      AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += "	      AND RFS.COD_UFF_FASCICOLO_SIEP = UFF.COD_UFFICIO";
		lStatement += "	      AND DESCR_TIPO_PROVV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND RFS.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROVV.RV_LOW_VALUE";
		lStatement += "	      AND DESCR_AUTORITA_EMIT.RV_DOMAIN = 'TIPO_UFFICIO' AND RFS.COD_TIPO_AUTORITA_EMITTENTE = DESCR_AUTORITA_EMIT.RV_LOW_VALUE";
		lStatement += "	      AND RFS.COD_LUOGO_EMITTENTE = DESCR_LUOGO_EMIT.COD_COMUNE";
		lStatement += "	      AND FAS_SIU_ID_FASCICOLO_SIUS = '" + aKey + "' ";

		return lStatement;
	}

	//
	// Restituisce il modello
	//
	public GenericModel getModelRifTitoloEsec() throws DAOException {
		RiferimentoFascicoloSiepModel aModel = new RiferimentoFascicoloSiepModel();
		aModel.setIdRiferimentoFascicoloSiep(getBigDecimal("ID_RIFERIMENTO_TITOLO_ESEC"));
		aModel.setAnnoFascicoloSiep(getBigDecimal("ANNO_FASCICOLO_SIEP"));
		aModel.setProgrFascicoloSiep(getBigDecimal("PROGR_FASCICOLO_SIEP"));
		aModel.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		aModel.setDescrTipoAutoritaEmittente(getString("DESCR_AUTORITA_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		aModel.setFlagMS(getString("FLAG_MS_SN"));
		aModel.setFlagRifTitoloEsecutivo(getString("RIF_TITOLO_ESECUTIVO"));
		return aModel;
	}

}