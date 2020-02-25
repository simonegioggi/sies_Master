package siap.siep.verbale.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.verbale.model.VerbaleDataInizioModel;
import siap.siep.verbale.model.VerbaleModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: VerbaleSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Verbale
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
public class VerbaleSqlDAO extends SqlDAO {

	public VerbaleSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaVerbale(VerbaleModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaVerbaleByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaVerbaleObblighiByIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQueryObblighi();

		lSql += " " + setCondizioniObblighiByIdEvento(aKey);
		setStatement(lSql);
	}

	// STUB 24/10/2005 REWORK STATO ESECUZIONE
	public void ricercaVerbaleByIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdEvento(aKey);
		setStatement(lSql);
	}

	public void ricercaVerbaleByCodTipoIdEvento(BigDecimal aKey, String aCodTipo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND EVE_ID_EVENTO = " + aKey;
		lSql += " AND COD_TIPO_VERBALE = '" + aCodTipo + "'";

		setStatement(lSql);
	}

	public void ricercaVerbaleByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQueryObblighi();

		lSql += " " + this.setCondizioniByIdFascicolo(aKey);
		setStatement(lSql);
	}

	// Ambrosino 26/07/2010
	public void ricercaVerbaleByIdFascicoloSiep(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQueryFascSiep();

		lSql += " AND  EV.ID_EVENTO = VE.EVE_ID_EVENTO";
		lSql += " AND  EV.FAS_SIE_ID_FASCICOLO_SIEP = FA.ID_FASCICOLO_SIEP";
		lSql += " AND  EV.FLAG_DOCUMENTO_REGISTRATO != 'A'";
		lSql += " AND  FA.ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND  MA.EVE_ID_EVENTO = EV.EVE_ID_EVENTO ";

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_VERBALE, " + "COD_TIPO_VERBALE, COD_VER.RV_MEANING VERBALI,"
				+ "DATA_EMISSIONE, " + "DATA_PERVENIMENTO, "
				+ "COD_TIPO_UFFICIO_FIRMATARIO, COD_UFF_FIR.RV_MEANING AUTORITA, "
				+ "COD_LUOGO_UFFICIO_FIRMATARIO, COD_LUO_FIR.DESCRIZIONE COMUNI, " + "NOTE, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "EVE_ID_EVENTO, " + "CSS_ID_CSSA, " + "IST_DET_ID_ISTITUTO_DETENZIONE, "
				+ "NUMERO_PROTOCOLLO, " + "NUM_ANNI_ESPULSIONE, " + "NUM_MESI_ESPULSIONE, "
				+ "NUM_GIORNI_ESPULSIONE, " + "VERBALE.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " FROM VERBALE,  CG_REF_CODES COD_VER, CG_REF_CODES COD_UFF_FIR, COMUNE COD_LUO_FIR ";
		lStatement += " WHERE COD_VER.RV_DOMAIN = 'TIPO_VERBALE' AND COD_VER.RV_LOW_VALUE = COD_TIPO_VERBALE"
				+ " AND COD_UFF_FIR.RV_DOMAIN = 'TIPO_AUTORITA' AND COD_UFF_FIR.RV_LOW_VALUE = COD_TIPO_UFFICIO_FIRMATARIO"
				+ " AND COD_LUO_FIR.COD_COMUNE = COD_LUOGO_UFFICIO_FIRMATARIO";
		return lStatement;
	}

	protected String getSqlQueryObblighi() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_VERBALE, " + "V.COD_TIPO_VERBALE, COD_VER.RV_MEANING VERBALI,"
				+ "V.DATA_EMISSIONE, " + "V.DATA_PERVENIMENTO, "
				+ "V.COD_TIPO_UFFICIO_FIRMATARIO, COD_UFF_FIR.RV_MEANING AUTORITA, "
				+ "V.COD_LUOGO_UFFICIO_FIRMATARIO, COD_LUO_FIR.DESCRIZIONE COMUNI, " + "V.NOTE, "
				+ "V.COD_OPERATORE_INSERIMENTO, " + "V.DATA_INSERIMENTO, " + "V.COD_UFFICIO_INSERIMENTO, "
				+ "V.COD_OPERATORE_AGGIORNAMENTO, " + "V.DATA_AGGIORNAMENTO, "
				+ "V.COD_UFFICIO_AGGIORNAMENTO, " + "V.EVE_ID_EVENTO, " + "V.CSS_ID_CSSA, "
				+ "V.IST_DET_ID_ISTITUTO_DETENZIONE, " + "V.NUMERO_PROTOCOLLO, " + "V.NUM_ANNI_ESPULSIONE, "
				+ "V.NUM_MESI_ESPULSIONE, " + "V.NUM_GIORNI_ESPULSIONE, " + "V.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " FROM VERBALE V,  CG_REF_CODES COD_VER, CG_REF_CODES COD_UFF_FIR, COMUNE COD_LUO_FIR";
		lStatement += " WHERE COD_VER.RV_DOMAIN = 'TIPO_VERBALE' AND COD_VER.RV_LOW_VALUE = COD_TIPO_VERBALE"
				+ " AND COD_UFF_FIR.RV_DOMAIN = 'TIPO_AUTORITA' AND COD_UFF_FIR.RV_LOW_VALUE = COD_TIPO_UFFICIO_FIRMATARIO"
				+ " AND COD_LUO_FIR.COD_COMUNE = V.COD_LUOGO_UFFICIO_FIRMATARIO";
		return lStatement;
	}

	// Ambrosino 26/07/2010
	protected String getSqlQueryFascSiep() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_VERBALE, " + "COD_TIPO_VERBALE, COD_VER.RV_MEANING VERBALI,"
				+ "VE.DATA_EMISSIONE, " + "VE.DATA_PERVENIMENTO, "
				+ "COD_TIPO_UFFICIO_FIRMATARIO, COD_UFF_FIR.RV_MEANING AUTORITA, "
				+ "COD_LUOGO_UFFICIO_FIRMATARIO, COD_LUO_FIR.DESCRIZIONE COMUNI, " + "VE.NOTE, "
				+ "VE.COD_OPERATORE_INSERIMENTO, " + "VE.DATA_INSERIMENTO, " + "VE.COD_UFFICIO_INSERIMENTO, "
				+ "VE.COD_OPERATORE_AGGIORNAMENTO, " + "VE.DATA_AGGIORNAMENTO, "
				+ "VE.COD_UFFICIO_AGGIORNAMENTO, " + "VE.EVE_ID_EVENTO, " + "VE.CSS_ID_CSSA, "
				+ "VE.IST_DET_ID_ISTITUTO_DETENZIONE, " + "VE.FAS_SIU_ID_FASCICOLO_SIUS, "
				+ "VE.NUMERO_PROTOCOLLO, " + "NUM_ANNI_ESPULSIONE, " + "NUM_MESI_ESPULSIONE, "
				+ "NUM_GIORNI_ESPULSIONE, " +
				// Ambrosino 26/07/2010
				"NUM_GIORNI_MISURA, " + "NUM_MESI_MISURA, " + "NUM_ANNI_MISURA, " + "DATA_INIZIO_MISURA, "
				+ "DATA_FINE_MISURA ";

		lStatement += " FROM VERBALE VE, EVENTO EV, FASCICOLO_SIEP FA, MISURA_ALTERNATIVA MA,";
		lStatement += " CG_REF_CODES COD_VER, CG_REF_CODES COD_UFF_FIR, COMUNE COD_LUO_FIR ";
		lStatement += " WHERE COD_VER.RV_DOMAIN = 'TIPO_VERBALE' AND COD_VER.RV_LOW_VALUE = COD_TIPO_VERBALE"
				+ " AND COD_UFF_FIR.RV_DOMAIN = 'TIPO_AUTORITA' AND COD_UFF_FIR.RV_LOW_VALUE = COD_TIPO_UFFICIO_FIRMATARIO"
				+ " AND COD_LUO_FIR.COD_COMUNE = COD_LUOGO_UFFICIO_FIRMATARIO";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		VerbaleModel aModel = new VerbaleModel();
		// Inserire le opportune set delle descrizioni!
		aModel.setIdVerbale(getBigDecimal("ID_VERBALE"));
		aModel.setCodTipoVerbale(getString("COD_TIPO_VERBALE"));
		aModel.setDescrTipoVerbale(getString("VERBALI"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setDataPervenimento(getDate("DATA_PERVENIMENTO"));
		aModel.setCodTipoUfficioFirmatario(getString("COD_TIPO_UFFICIO_FIRMATARIO"));
		aModel.setDescrTipoUfficioFirmatario(getString("AUTORITA"));
		aModel.setCodLuogoUfficioFirmatario(getString("COD_LUOGO_UFFICIO_FIRMATARIO"));
		aModel.setDescrLuogoUfficioFirmatario(getString("COMUNI"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setCssIdCssa(getBigDecimal("CSS_ID_CSSA"));
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setNumeroProtocollo(getString("NUMERO_PROTOCOLLO"));
		aModel.setNumAnniEspulsione(getBigDecimal("NUM_ANNI_ESPULSIONE"));
		aModel.setNumMesiEspulsione(getBigDecimal("NUM_MESI_ESPULSIONE"));
		aModel.setNumGiorniEspulsione(getBigDecimal("NUM_GIORNI_ESPULSIONE"));

		return aModel;
	}

	// Ambrosino 26/07/2010
	public GenericModel getModelVerb() throws DAOException {
		VerbaleDataInizioModel aModel = new VerbaleDataInizioModel();
		// Inserire le opportune set delle descrizioni!
		aModel.setIdVerbale(getBigDecimal("ID_VERBALE"));
		aModel.setCodTipoVerbale(getString("COD_TIPO_VERBALE"));
		aModel.setDescrTipoVerbale(getString("VERBALI"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setDataPervenimento(getDate("DATA_PERVENIMENTO"));
		aModel.setCodTipoUfficioFirmatario(getString("COD_TIPO_UFFICIO_FIRMATARIO"));
		aModel.setDescrTipoUfficioFirmatario(getString("AUTORITA"));
		aModel.setCodLuogoUfficioFirmatario(getString("COD_LUOGO_UFFICIO_FIRMATARIO"));
		aModel.setDescrLuogoUfficioFirmatario(getString("COMUNI"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setCssIdCssa(getBigDecimal("CSS_ID_CSSA"));
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setNumeroProtocollo(getString("NUMERO_PROTOCOLLO"));
		aModel.setNumAnniEspulsione(getBigDecimal("NUM_ANNI_ESPULSIONE"));
		aModel.setNumMesiEspulsione(getBigDecimal("NUM_MESI_ESPULSIONE"));
		aModel.setNumGiorniEspulsione(getBigDecimal("NUM_GIORNI_ESPULSIONE"));
		aModel.setNumAnniMisura(getBigDecimal("NUM_ANNI_MISURA"));
		aModel.setNumMesiMisura(getBigDecimal("NUM_MESI_MISURA"));
		aModel.setNumGiorniMisura(getBigDecimal("NUM_GIORNI_MISURA"));
		aModel.setDataInizioMisura(getDate("DATA_INIZIO_MISURA"));
		aModel.setDataFineMisura(getDate("DATA_FINE_MISURA"));

		return aModel;
	}

	public String setCondizione(VerbaleModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_VERBALE = " + aKey;
	}

	public String setCondizioniObblighiByIdEvento(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " AND V.EVE_ID_EVENTO = " + aKey;
		lCondizioni += " AND V.COD_TIPO_VERBALE = '03'";
		return lCondizioni;
	}

	// STUB 24/10/2005 REWORK STATO ESECUZIONE
	public String setCondizioniByIdEvento(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " AND EVE_ID_EVENTO = " + aKey;
		return lCondizioni;
	}

	public String setCondizioniByIdFascicolo(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni += " AND V.FAS_SIU_ID_FASCICOLO_SIUS = " + aKey;
		lCondizioni += " AND V.COD_TIPO_VERBALE = '03'";
		return lCondizioni;
	}

}