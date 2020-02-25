package siap.siep.penaaccessoria.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.sige.penaaccessoria.model.PenaAccSigeModel;

/**
 * <p>
 * Title: PenaAccessoriaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella PenaAccessoria
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

public class PenaAccessoriaSqlDAO extends SIAPSqlDAO {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public PenaAccessoriaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaPenaAccessoria(PenaAccessoriaModel aModel) throws DAOException {
		String lStatement = new String(getSqlPenaAccessoria());

		lStatement += " " + setCondizioni(aModel);
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	public void ricercaPenaAccessoriaByFascicolo(BigDecimal aKey) throws DAOException {
		String lStatement = new String(getSqlPenaAccessoria());

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aKey;

		setStatement(lStatement);
	}

	public void ricercaPenaAccessoriaByKey(BigDecimal aKey) throws DAOException {
		String lStatement = new String(getSqlPenaAccessoria());

		lStatement += " AND ID_PENA_ACCESSORIA=" + aKey;

		setStatement(lStatement);
	}

	// MEV26 CUMULO
	public void ricercaPenaAccessoriaByBenIdBeneficio(BigDecimal aKey) throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--XX-- PenaAccessoriaSqlDAP - ricercaPenaAccessoriaByBenIdBeneficio  ");
		String lStatement = new String(getSqlPenaAccessoria());

		lStatement += " AND BEN_ID_BENEFICIO = " + aKey;

		setStatement(lStatement);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--XX-- PenaAccessoriaSqlDAP -ricercaPenaAccessoriaByBenIdBeneficio  -lStatement = "
				+ lStatement);
	}

	protected String getSqlPenaAccessoria() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PENA_ACCESSORIA, " + "COD_TIPO_PENA_ACCESSORIA, " + "DURATA, "
				+ "NUM_ANNI, " + "NUM_MESI, " + "NUM_GIORNI, " + "FLAG_CONDONATA, " + "DATA_DPR, "
				+ "NUM_DPR, " + "FLAG_DICHIARAZIONE_FALSITA, " + "FLAG_REVOCA_CONDONO, "
				+ "DATA_SENTENZA_REVOCA, " + "ANNO_SENTENZA_REVOCA, " + "NUMERO_SENTENZA_REVOCA, "
				+ "COD_TIPO_UFFICIO_SENTENZA_REVO, " + "COD_LUOGO_SENTENZA_REVOCA, " + "ANNO_REGE_PM_REVOCA, "
				+ "NUMERO_REGE_PM_REVOCA, " + "ANNO_REGE_GIP_REVOCA, " + "NUMERO_REGE_GIP_REVOCA, "
				+ "ANNO_REGE_DIB_REVOCA, " + "NUMERO_REGE_DIB_REVOCA, " + "ANNO_REGE_CAS_REVOCA, "
				+ "NUMERO_REGE_CAS_REVOCA, " + "ANNO_REGE_CAP_REVOCA, " + "NUMERO_REGE_CAP_REVOCA, "
				+ "ANNO_REGE_CASAP_REVOCA, " + "NUMERO_REGE_CASAP_REVOCA, " + "NOTE, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, " + "DATA_INIZIO_VALIDITA, " + "DATA_FINE_VALIDITA, "
				+ "ANNO_ORDINANZA_GE, " + "NUMERO_ORDINANZA_GE, " + "DATA_ORDINANZA_GE, "
				+ "ANNO_ORDINANZA_PA, " + "NUMERO_ORDINANZA_PA, " + "DATA_ORDINANZA_PA, "
				+ "ESTREMI_CONDONO, " + "BEN_ID_BENEFICIO, "
				+ "DESCR_ALTRE_PA, COD_FONTE_GE, ANNO_FONTE_GE, NUMERO_FONTE_GE, "
				+ "COD_SOTTONUMERAZIONE_GE, COMMA_GE, LETTERA_GE, NUMERO_GE, ARTICOLO_GE, "
				+ "ID_PENA_ACCESSORIA_ORIGINE, " + "COD_NUOVO_TIPO_PENA_ACCESSORIA, "
				+ "FONTEGE.RV_MEANING DESCR_FONTE_GE, "
				+ "SOTTONUMERAZIONEGE.RV_MEANING DESCR_SOTTONUMERAZIONE_GE, "
				+ "NUOVOTIPOPENA.RV_MEANING DESCR_NUOVO_TIPO_PENA, "
				+ "TIPOPENA.RV_MEANING DESCRPENA , TIPODURATAPENA.RV_MEANING DESCRDURATA, "
				+ "TIPOUFFICIO_SEN_RE.RV_MEANING DESCRTIPOUFFICIO_SEN_RE, "
				+ "LUOGOUFFICIO_SEN_RE.DESCRIZIONE DESCRLUOGOOUFFICIO_SEN_RE, "
				+ "(select count(*) as COUNT from EVENTO where PEN_ACC_ID_PENA_ACCESSORIA = ID_PENA_ACCESSORIA) COUNT, "
				+ "COD_TIPO_UFFICIO_ORDINANZA_GE, COD_LUOGO_UFFICIO_ORDINANZA_GE, "
				+ "TIPOUFFICIO_OR_GE.RV_MEANING DESCRTIPOUFFICIO_OR_GE, LUOGOUFFICIO_OR_GE.DESCRIZIONE DESCRLUOGOUFFICIO_OR_GE, "
				+ "COD_TIPO_UFFICIO_ORDINANZA_PA, COD_LUOGO_UFFICIO_ORDINANZA_PA, "
				+ "TIPOUFFICIO_OR_PA.RV_MEANING DESCRTIPOUFFICIO_OR_PA, LUOGOUFFICIO_OR_PA.DESCRIZIONE DESCRLUOGOUFFICIO_OR_PA ";
		lStatement += "FROM PENA_ACCESSORIA, CG_REF_CODES TIPOPENA, CG_REF_CODES TIPODURATAPENA,";
		lStatement += " CG_REF_CODES TIPOUFFICIO_SEN_RE, CG_REF_CODES TIPOUFFICIO_OR_GE,";
		lStatement += " CG_REF_CODES TIPOUFFICIO_OR_PA, CG_REF_CODES NUOVOTIPOPENA,";
		lStatement += " CG_REF_CODES FONTEGE, CG_REF_CODES SOTTONUMERAZIONEGE,";
		lStatement += " COMUNE LUOGOUFFICIO_SEN_RE, COMUNE LUOGOUFFICIO_OR_GE, COMUNE LUOGOUFFICIO_OR_PA ";
		lStatement += "WHERE ";
		lStatement += "FONTEGE.RV_DOMAIN='FONTE' AND ";
		lStatement += "FONTEGE.RV_LOW_VALUE=PENA_ACCESSORIA.COD_FONTE_GE AND ";
		lStatement += "SOTTONUMERAZIONEGE.RV_DOMAIN='SOTTONUMERAZIONE' AND ";
		lStatement += "SOTTONUMERAZIONEGE.RV_LOW_VALUE=PENA_ACCESSORIA.COD_SOTTONUMERAZIONE_GE AND ";
		lStatement += "TIPOPENA.RV_DOMAIN='TIPO_PENA_ACCESSORIA' AND ";
		lStatement += "TIPOPENA.RV_LOW_VALUE=PENA_ACCESSORIA.COD_TIPO_PENA_ACCESSORIA AND ";
		lStatement += "NUOVOTIPOPENA.RV_DOMAIN='TIPO_PENA_ACCESSORIA' AND ";
		lStatement += "NUOVOTIPOPENA.RV_LOW_VALUE=PENA_ACCESSORIA.COD_NUOVO_TIPO_PENA_ACCESSORIA AND ";
		lStatement += "TIPODURATAPENA.RV_DOMAIN='TIPO_DURATA' AND ";
		lStatement += "TIPODURATAPENA.RV_LOW_VALUE=PENA_ACCESSORIA.DURATA ";
		lStatement += "AND TIPOUFFICIO_SEN_RE.RV_DOMAIN='TIPO_UFFICIO' AND TIPOUFFICIO_SEN_RE.RV_LOW_VALUE=PENA_ACCESSORIA.COD_TIPO_UFFICIO_SENTENZA_REVO ";
		lStatement += "AND LUOGOUFFICIO_SEN_RE.COD_COMUNE=PENA_ACCESSORIA.COD_LUOGO_SENTENZA_REVOCA ";
		lStatement += "AND TIPOUFFICIO_OR_GE.RV_DOMAIN='TIPO_UFFICIO' AND TIPOUFFICIO_OR_GE.RV_LOW_VALUE=PENA_ACCESSORIA.COD_TIPO_UFFICIO_ORDINANZA_GE ";
		lStatement += "AND LUOGOUFFICIO_OR_GE.COD_COMUNE=PENA_ACCESSORIA.COD_LUOGO_UFFICIO_ORDINANZA_GE ";
		lStatement += "AND TIPOUFFICIO_OR_PA.RV_DOMAIN='TIPO_UFFICIO' AND TIPOUFFICIO_OR_PA.RV_LOW_VALUE=PENA_ACCESSORIA.COD_TIPO_UFFICIO_ORDINANZA_PA ";
		lStatement += "AND LUOGOUFFICIO_OR_PA.COD_COMUNE=PENA_ACCESSORIA.COD_LUOGO_UFFICIO_ORDINANZA_PA";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		PenaAccessoriaModel aModel = new PenaAccessoriaModel();

		aModel.setIdPenaAccessoria(getBigDecimal("ID_PENA_ACCESSORIA"));
		aModel.setCodTipoPenaAccessoria(getString("COD_TIPO_PENA_ACCESSORIA"));
		aModel.setDescrTipoPenaAccessoria(getString("DESCRPENA"));
		aModel.setDurata(getString("DURATA"));
		aModel.setDescrDurata(getString("DESCRDURATA"));
		aModel.setNumAnni(getBigDecimal("NUM_ANNI"));
		aModel.setNumMesi(getBigDecimal("NUM_MESI"));
		aModel.setNumGiorni(getBigDecimal("NUM_GIORNI"));
		aModel.setFlagCondonata(getString("FLAG_CONDONATA"));
		aModel.setDataDpr(getDate("DATA_DPR"));
		aModel.setNumDpr(getString("NUM_DPR"));
		aModel.setFlagDichiarazioneFalsita(getString("FLAG_DICHIARAZIONE_FALSITA"));
		aModel.setFlagRevocaCondono(getString("FLAG_REVOCA_CONDONO"));
		aModel.setDataSentenzaRevoca(getDate("DATA_SENTENZA_REVOCA"));
		aModel.setAnnoSentenzaRevoca(getBigDecimal("ANNO_SENTENZA_REVOCA"));
		aModel.setNumeroSentenzaRevoca(getString("NUMERO_SENTENZA_REVOCA"));
		aModel.setCodTipoUfficioSentenzaRevo(getString("COD_TIPO_UFFICIO_SENTENZA_REVO"));
		aModel.setDescrTipoUfficioSentenzaRevo(getString("DESCRTIPOUFFICIO_SEN_RE"));
		aModel.setCodLuogoSentenzaRevoca(getString("COD_LUOGO_SENTENZA_REVOCA"));
		aModel.setDescrLuogoSentenzaRevoca(getString("DESCRLUOGOOUFFICIO_SEN_RE"));
		aModel.setAnnoRegePmRevoca(getBigDecimal("ANNO_REGE_PM_REVOCA"));
		aModel.setNumeroRegePmRevoca(getString("NUMERO_REGE_PM_REVOCA"));
		aModel.setAnnoRegeGipRevoca(getBigDecimal("ANNO_REGE_GIP_REVOCA"));
		aModel.setNumeroRegeGipRevoca(getString("NUMERO_REGE_GIP_REVOCA"));
		aModel.setAnnoRegeDibRevoca(getBigDecimal("ANNO_REGE_DIB_REVOCA"));
		aModel.setNumeroRegeDibRevoca(getString("NUMERO_REGE_DIB_REVOCA"));
		aModel.setAnnoRegeCasRevoca(getBigDecimal("ANNO_REGE_CAS_REVOCA"));
		aModel.setNumeroRegeCasRevoca(getString("NUMERO_REGE_CAS_REVOCA"));
		aModel.setAnnoRegeCapRevoca(getBigDecimal("ANNO_REGE_CAP_REVOCA"));
		aModel.setNumeroRegeCapRevoca(getString("NUMERO_REGE_CAP_REVOCA"));
		aModel.setAnnoRegeCasapRevoca(getBigDecimal("ANNO_REGE_CASAP_REVOCA"));
		aModel.setNumeroRegeCasapRevoca(getString("NUMERO_REGE_CASAP_REVOCA"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.setAnnoOrdinanzaGE(getBigDecimal("ANNO_ORDINANZA_GE"));
		aModel.setNumeroOrdinanzaGE(getBigDecimal("NUMERO_ORDINANZA_GE"));
		aModel.setDataOrdinanzaGE(getDate("DATA_ORDINANZA_GE"));
		aModel.setEstremiCondono(getString("ESTREMI_CONDONO"));
		aModel.setIdPenaAccessoriaOrigine(getBigDecimal("ID_PENA_ACCESSORIA_ORIGINE"));
		aModel.setCodNuovoTipoPenaAccessoria(getString("COD_NUOVO_TIPO_PENA_ACCESSORIA"));
		aModel.setDescrNuovoTipoPenaAccessoria(getString("DESCR_NUOVO_TIPO_PENA"));
		// aModel.setNumeroEventiCorrelati(this.getNumeroEventiCorrelati(getBigDecimal("ID_PENA_ACCESSORIA"))
		// );
		aModel.setNumeroEventiCorrelati(getBigDecimal("COUNT"));
		aModel.setCodTipoUfficioOrdinanzaGE(getString("COD_TIPO_UFFICIO_ORDINANZA_GE"));
		aModel.setDescrTipoUfficioOrdinanzaGE(getString("DESCRTIPOUFFICIO_OR_GE"));
		aModel.setCodLuogoUfficioOrdinanzaGE(getString("COD_LUOGO_UFFICIO_ORDINANZA_GE"));
		aModel.setDescrLuogoUfficioOrdinanzaGE(getString("DESCRLUOGOUFFICIO_OR_GE"));
		aModel.setDataOrdinanzaPA(getDate("DATA_ORDINANZA_PA"));
		aModel.setAnnoOrdinanzaPA(getBigDecimal("ANNO_ORDINANZA_PA"));
		aModel.setNumeroOrdinanzaPA(getBigDecimal("NUMERO_ORDINANZA_PA"));
		aModel.setCodTipoUfficioOrdinanzaPA(getString("COD_TIPO_UFFICIO_ORDINANZA_PA"));
		aModel.setDescrTipoUfficioOrdinanzaPA(getString("DESCRTIPOUFFICIO_OR_PA"));
		aModel.setCodLuogoUfficioOrdinanzaPA(getString("COD_LUOGO_UFFICIO_ORDINANZA_PA"));
		aModel.setDescrLuogoUfficioOrdinanzaPA(getString("DESCRLUOGOUFFICIO_OR_PA"));
		aModel.setDescrAltrePA(getString("DESCR_ALTRE_PA")); // 04/04/2006
		aModel.setCodFonteGE(getString("COD_FONTE_GE")); // 04/04/2006
		aModel.setDescrFonteGE(getString("DESCR_FONTE_GE")); // 04/04/2006
		aModel.setAnnoFonteGE(getString("ANNO_FONTE_GE")); // 04/04/2006
		aModel.setNumeroFonteGE(getString("NUMERO_FONTE_GE")); // 04/04/2006
		aModel.setCodSottonumerazioneGE(getString("COD_SOTTONUMERAZIONE_GE")); // 04/04/2006
		aModel.setDescrSottonumerazioneGE(getString("DESCR_SOTTONUMERAZIONE_GE")); // 04/04/2006
		aModel.setCommaGE(getString("COMMA_GE")); // 04/04/2006
		aModel.setLetteraGE(getString("LETTERA_GE")); // 04/04/2006
		aModel.setNumeroGE(getString("NUMERO_GE")); // 04/04/2006
		aModel.setArticoloGE(getString("ARTICOLO_GE")); // 04/04/2006

		aModel.setBenIdBeneficio(getBigDecimal("BEN_ID_BENEFICIO"));

		return aModel;
	}

	public void selCondizione(PenaAccessoriaModel aModel) {
		// String lCondizioni = new String();
		// boolean lInserito = false;
	}

	public String setCondizioni(PenaAccessoriaModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		if (aModel.getIdPenaAccessoria() != null) {
			lCondizioni = " AND ID_PENA_ACCESSORIA=" + aModel.getIdPenaAccessoria();
			// lInserito=true;
		}

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni = " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
			// lInserito=true;
		}

		if (aModel.getBenIdBeneficio() != null) {
			lCondizioni = " AND BEN_ID_BENEFICIO=" + aModel.getBenIdBeneficio();
			// lInserito=true;
		}

		// Pena Accessoria SIGE si ricerca attraverso la tabella di relazione PENA_ACCESSORIA_SENTENZA_SIGE
		if (aModel instanceof siap.sige.penaaccessoria.model.PenaAccSigeModel) {
			lCondizioni = " AND ID_PENA_ACCESSORIA IN (SELECT PNA_ID_PENA_ACCESSORIA FROM PENA_ACCESSORIA_SENTENZA_SIGE WHERE FAS_SIGE_SEN_ID = "
					+ ((PenaAccSigeModel) aModel).getFasSigeSenId() + ")";
			// lInserito=true;
		}

		return lCondizioni;
	}

	private String setOrder() {
		String lOrder = new String();
		lOrder = " ORDER BY DATA_INSERIMENTO, ID_PENA_ACCESSORIA DESC";
		return lOrder;
	}

	public BigDecimal getNumeroEventiCorrelati(BigDecimal aIdPenaAccessoria) throws DAOException {
		String lStatement = "select count(*) as COUNT from EVENTO where PEN_ACC_ID_PENA_ACCESSORIA = '"
				+ aIdPenaAccessoria + "'";
		setStatement(lStatement);
		this.start();
		this.next();
		BigDecimal lBigDec = this.getBigDecimal("COUNT");
		this.stop();
		return lBigDec;
	}

	public boolean ExistPASostitutiva(BigDecimal aIdPenaAccessoria) throws DAOException {
		String lStatement = "select count(*) as COUNT ";
		lStatement += " from PENA_ACCESSORIA PA, PENA_ACCESSORIA PA_NEW ";
		lStatement += " where PA.ID_PENA_ACCESSORIA = '" + aIdPenaAccessoria + "'";
		lStatement += " and PA.ID_PENA_ACCESSORIA = PA_NEW.ID_PENA_ACCESSORIA_ORIGINE ";

		setStatement(lStatement);

		this.start();

		BigDecimal lCount = null;
		if (this.next())
			lCount = this.getBigDecimal("COUNT");

		if (lCount.intValue() > 0)
			return true;
		else
			return false;
	}

	public boolean ExistEventoCollegato(BigDecimal aIdPenaAccessoria) throws DAOException {
		String lStatement = "select count(*) as COUNT ";
		lStatement += " from PENA_ACCESSORIA PA, EVENTO EV ";
		lStatement += " where PA.ID_PENA_ACCESSORIA = '" + aIdPenaAccessoria + "'";
		lStatement += " and PA.ID_PENA_ACCESSORIA = EV.PEN_ACC_ID_PENA_ACCESSORIA ";
		lStatement += " and  FLAG_DOCUMENTO_REGISTRATO != 'A'";
		setStatement(lStatement);

		this.start();

		BigDecimal lCount = null;
		if (this.next())
			lCount = this.getBigDecimal("COUNT");

		if (lCount.intValue() > 0)
			return true;
		else
			return false;
	}

}