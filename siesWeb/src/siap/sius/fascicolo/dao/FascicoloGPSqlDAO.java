package siap.sius.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * Title: FascicoloSiusSqlDAO
 * Description: Classe SqlDAO che rappresenta la tabella FascicoloSius
 * 
 * @version 1.0
 */
public class FascicoloGPSqlDAO extends SIAPSqlDAO {

	public FascicoloGPSqlDAO(Connection con) {

		super(con);
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {

		FascicoloGPModel aModel = new FascicoloGPModel();

		// Inserire le opportune set delle descrizioni!
		aModel.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		aModel.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aModel.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		aModel.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		aModel.getFascicoloSiusModel().setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO"));
		aModel.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.getFascicoloSiusModel().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		aModel.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		aModel.getFascicoloSiusModel().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.getFascicoloSiusModel().setChiaveAnnoSIEP(getBigDecimal("CHIAVE_ANNO_SIEP"));
		aModel.getFascicoloSiusModel().setChiaveProgrSIEP(getBigDecimal("CHIAVE_PROGR_SIEP"));
		aModel.getFascicoloSiusModel().setChiaveUfficioSIEP(getString("CHIAVE_UFFICIO_SIEP"));
		aModel.getFascicoloSiusModel().setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.getFascicoloSiusModel().setIdFascicoloSiusOrigine(getBigDecimal("ID_FASCICOLO_SIUS_ORIGINE")); // 15/01/2004
		aModel.getFascicoloSiusModel().setDataDefinizione(getDate("DATA_DEFINIZIONE"));
		aModel.getFascicoloSiusModel()
				.setNumeroFascicoliUnificati(getBigDecimal("NUMERO_FASCICOLI_UNIFICATI")); // STUB 29/04/2004
		aModel.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		aModel.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		aModel.getFascicoloSiusModel().setCodTipoUfficio(getString("COD_TIPO_UFFICIO"));
		// MEV10-s3: aggiunto campo in db per gestire età minore/maggiore
		aModel.getFascicoloSiusModel().setVisibilitaMinorenne(getString("VISIBILITA_EX_MINORENNE"));

		aModel.getGeneraleProcedimentoModel()
				.setIdGeneraleProcedimento(getBigDecimal("ID_GENERALE_PROCEDIMENTO"));
		aModel.getGeneraleProcedimentoModel().setAnnoS1(getBigDecimal("ANNO_S1"));
		aModel.getGeneraleProcedimentoModel().setProgrS1(getBigDecimal("PROGR_S1"));
		aModel.getGeneraleProcedimentoModel().setCodTipoRegistro(getString("COD_TIPO_REGISTRO"));
		aModel.getGeneraleProcedimentoModel().setDescrTipoRegistro(getString("DESCR_TIPO_REGISTRO"));
		aModel.getGeneraleProcedimentoModel().setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));
		aModel.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		aModel.getGeneraleProcedimentoModel()
				.setDescrOggettoProcedimento(getString("DESCR_OGGETTO_PROCEDIMENTO"));
		aModel.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		aModel.getGeneraleProcedimentoModel().setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA"));
		aModel.getGeneraleProcedimentoModel().setCodTipoAtto(getString("COD_TIPO_ATTO"));
		aModel.getGeneraleProcedimentoModel().setDescrTipoAtto(getString("DESCR_TIPO_ATTO"));
		aModel.getGeneraleProcedimentoModel().setCodTipoMittenteAtto(getString("COD_TIPO_MITTENTE_ATTO"));
		aModel.getGeneraleProcedimentoModel().setDescrTipoMittenteAtto(getString("DESCR_TIPO_MITTENTE"));
		aModel.getGeneraleProcedimentoModel().setCodSedeMittente(getString("COD_SEDE_MITTENTE"));
		aModel.getGeneraleProcedimentoModel().setDescrSedeMittente(getString("DESCR_SEDE_MITTENTE"));
		aModel.getGeneraleProcedimentoModel().setAnnotazione(getString("ANNOTAZIONE"));
		aModel.getGeneraleProcedimentoModel()
				.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.getGeneraleProcedimentoModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.getGeneraleProcedimentoModel().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.getGeneraleProcedimentoModel().setDescrUfficioInserimento("");
		aModel.getGeneraleProcedimentoModel().setCodOperatoreAggiornamento(getString("GP_COD_OP_AGG"));
		aModel.getGeneraleProcedimentoModel().setDataAggiornamento(getDate("GP_D_AGG"));
		aModel.getGeneraleProcedimentoModel().setCodUfficioAggiornamento(getString("GP_COD_UFF_AGG"));
		aModel.getGeneraleProcedimentoModel().setDescrUfficioAggiornamento("");
		aModel.getGeneraleProcedimentoModel().setFasSiuIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		aModel.getGeneraleProcedimentoModel().setSezione(getString("SEZIONE"));
		aModel.getGeneraleProcedimentoModel().setDataFinePena(getDate("DATA_FINE_PENA"));
		aModel.getGeneraleProcedimentoModel().setCodPosGiuridica(getString("COD_POSIZIONE_GIURIDICA"));
		aModel.getGeneraleProcedimentoModel().setDescrPosGiuridica(getString("DESCR_POSIZIONE_GIURIDICA"));
		aModel.getGeneraleProcedimentoModel().setDataCameraConsiglio(getDate("DATA_UDIENZA"));
		aModel.getGeneraleProcedimentoModel().setUdiIdUdienza(getBigDecimal("UDI_ID_UDIENZA"));
		aModel.getGeneraleProcedimentoModel().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));
		aModel.getGeneraleProcedimentoModel().setDescrMittente(getString("DESCR_MITTENTE"));
		// 3/5/2005
		aModel.getGeneraleProcedimentoModel().setTipoDefinizione(getString("TIPO_DEFINIZIONE"));
		aModel.getGeneraleProcedimentoModel().setDescrDefinizione(getString("DESCR_DEFINIZIONE"));
		aModel.getGeneraleProcedimentoModel().setDataDefinizione(getDate("DATA_DEFINIZIONE"));
		// MEV_): aggiunta estrazione data_restituzione
		aModel.getGeneraleProcedimentoModel().setDataRestituzione(getDate("DATA_RESTITUZIONE"));
		return aModel;
	}

	/**
	 * Esegue la ricerca di un fascicolo tramite Chiave
	 *
	 * @param aIdFascicoloSius
	 * @throws DAOException
	 */
	public void ricercaFascicoloByKey(BigDecimal aIdFascicoloSius) throws DAOException {

		// Controllo l'esistenza del Fascicolo Siep (se non esiste lo escludo dalla query
		String lStatement = new String();
		if (existFasSiep(aIdFascicoloSius))
			lStatement = getFascicoloSqlQuery();
		else
			lStatement = getFascicoloSqlQueryNoFaSiep();

		lStatement += " AND ID_FASCICOLO_SIUS = " + aIdFascicoloSius;

		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca di un fascicolo tramite ID Origine
	 *
	 * @param aIdOrigine
	 * @throws DAOException
	 */
	public void ricercaFascicoloXIdOrigine(BigDecimal aIdOrigine) throws DAOException {

		String lStatement = new String("");
		lStatement = getFascicoloSqlQueryNoFaSiep();
		lStatement += " AND ID_FASCICOLO_SIUS_ORIGINE = " + aIdOrigine.toString();
		lStatement += " ORDER by CHIAVE_UFFICIO, CHIAVE_ANNO, CHIAVE_PROGR asc ";
		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca di un fascicolo tramite Generale Procedimento
	 *
	 * @param aIdGenProc
	 * @throws DAOException
	 */
	public void ricercaFascicoloByGenProc(BigDecimal aIdGenProc) throws DAOException {

		// Controllo l'esistenza del Fascicolo Siep (se non esiste lo escludo dalla query
		String lStatement = new String();
		if (existFasSiepByGenProc(aIdGenProc))
			lStatement = getFascicoloSqlQuery();
		else
			lStatement = getFascicoloSqlQueryNoFaSiep();

		lStatement += " AND GP.ID_GENERALE_PROCEDIMENTO = " + aIdGenProc;

		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca dei Fascicoli unificati tramite Chiave Fascicolo Unificante
	 *
	 * @param aIdFascicoloSius
	 * @throws DAOException
	 */
	public void ricercaFascicoliUnificatiByKey(BigDecimal aIdFascicoloUnificante) throws DAOException {

		// Controllo l'esistenza del Fascicolo Siep (se non esiste lo escludo dalla query
		String lStatement = new String();
		if (existFasSiep(aIdFascicoloUnificante))
			lStatement = getFascicoloSqlQuery();
		else
			lStatement = getFascicoloSqlQueryNoFaSiep();

		lStatement += " AND FASC.FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicoloUnificante;

		setStatement(lStatement);
	}

	protected String getFascicoloSqlQuery() {

		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR,"
				+ " FASC.CHIAVE_UFFICIO,";
		lStatement += " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, "
				+ "DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
		lStatement += " FASC.COD_OPERATORE_INSERIMENTO, FASC.COD_OPERATORE_AGGIORNAMENTO,";
		lStatement += " FASC.COD_UFFICIO_INSERIMENTO, FASC.COD_UFFICIO_AGGIORNAMENTO,";
		lStatement += " FASC.DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE,"
				+ "FASC.DATA_AGGIORNAMENTO,";
		lStatement += " FASC.FAS_SIE_ID_FASCICOLO_SIEP, FASC.FAS_SIU_ID_FASCICOLO_SIUS, "
				+ "FASC.ID_FASCICOLO_SIUS_ORIGINE, FASC.SOG_ID_SOGGETTO, FASC.DATA_DEFINIZIONE,"
				+ "FASC.NUMERO_FASCICOLI_UNIFICATI,";
		lStatement += " GP.ID_GENERALE_PROCEDIMENTO, GP.ANNO_S1, GP.PROGR_S1, GP.COD_OGGETTO_PROCEDIMENTO,"
				+ " GP.COD_TIPO_REGISTRO, TIPO_REGISTRO.RV_MEANING DESCR_TIPO_REGISTRO,";
		lStatement += " OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA,"
				+ " GP.UDI_ID_UDIENZA,";
		lStatement += " GP.DATA_DEFINIZIONE, GP.TIPO_DEFINIZIONE, GP.DESCR_DEFINIZIONE,";
		lStatement += " GP.DATA_ARRIVO_CANCELLERIA, GP.DATA_CAMERA_CONSIGLIO, GP.COD_TIPO_ATTO,"
				+ " TIPO_ATTO.RV_MEANING DESCR_TIPO_ATTO,";
		lStatement += " GP.COD_TIPO_MITTENTE_ATTO, MITTENTE_ATTO.RV_MEANING DESCR_TIPO_MITTENTE,"
				+ " NVL(UD.DATA_UDIENZA, GP.DATA_CAMERA_CONSIGLIO) DATA_UDIENZA,";
		lStatement += " GP.COD_SEDE_MITTENTE, DESCR_COM_MIT.DESCRIZIONE DESCR_SEDE_MITTENTE,"
				+ " GP.ANNOTAZIONE, GP.DATA_RESTITUZIONE,"; // MEV_9: estraggo DATA_RESTITUZIONE
		lStatement += " GP.COD_OPERATORE_AGGIORNAMENTO GP_COD_OP_AGG,"
				+ " GP.COD_UFFICIO_AGGIORNAMENTO GP_COD_UFF_AGG, GP.DATA_AGGIORNAMENTO GP_D_AGG,";
		lStatement += " GP.SEZIONE SEZIONE, GP.DATA_FINE_PENA DATA_FINE_PENA,"
				+ " GP.COD_POSIZIONE_GIURIDICA COD_POSIZIONE_GIURIDICA,"
				+ " NVL(POS_GIURIDICA.RV_MEANING, '') DESCR_POSIZIONE_GIURIDICA,"
				+ " NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA,"
				+ " NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE,";
		lStatement += " FASIEP.CHIAVE_ANNO CHIAVE_ANNO_SIEP, FASIEP.CHIAVE_PROGR CHIAVE_PROGR_SIEP,"
				+ " FASIEP.CHIAVE_UFFICIO CHIAVE_UFFICIO_SIEP,";
		lStatement += " DESCR_TIPO_UFF.RV_LOW_VALUE COD_TIPO_UFFICIO,";
		lStatement += " FASC.CERTIFICATO_PENALE, FASC.VISIBILITA_EX_MINORENNE,";
		// MEV10-s3: aggiunto recupero nuovo campo in tabella
		lStatement += " DBMS_LOB.GETLENGTH(FASC.CERTIFICATO_PENALE) LEN_BLOB_CERT_PENALE";
		lStatement += " FROM FASCICOLO_SIUS FASC, GENERALE_PROCEDIMENTO GP, CG_REF_CODES STATO_FASCICOLO,"
				+ " UDIENZA UD,";
		lStatement += " CG_REF_CODES MITTENTE_ATTO, CG_REF_CODES DESCR_TIPO_UFF, CG_REF_CODES TIPO_ATTO,"
				+ " CG_REF_CODES OGGETTO_PROCEDIMENTO, CG_REF_CODES TIPO_REGISTRO,";
		lStatement += " CG_REF_CODES POS_GIURIDICA, UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_MIT,"
				+ " FASCICOLO_SIEP FASIEP";
		lStatement += " WHERE (STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO'"
				+ " AND NVL(STATO_FASCICOLO.RV_LOW_VALUE,'-') = FASC.COD_STATO_FASCICOLO)";
		lStatement += " AND (DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO'"
				+ " AND NVL(UFF.COD_TIPO_UFFICIO,'-') = DESCR_TIPO_UFF.RV_LOW_VALUE)";
		lStatement += " AND (MITTENTE_ATTO.RV_DOMAIN = 'MITTENTE_ATTO'"
				+ " AND NVL(GP.COD_TIPO_MITTENTE_ATTO,'-') = MITTENTE_ATTO.RV_LOW_VALUE)";
		lStatement += " AND (TIPO_REGISTRO.RV_DOMAIN = 'TIPO_REGISTRO'"
				+ " AND NVL(GP.COD_TIPO_REGISTRO,'-') = TIPO_REGISTRO.RV_LOW_VALUE)";
		lStatement += " AND (TIPO_ATTO.RV_DOMAIN = 'TIPO_ATTO'"
				+ " AND NVL(GP.COD_TIPO_ATTO,'-') = TIPO_ATTO.RV_LOW_VALUE)";
		lStatement += " AND (POS_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA'"
				+ " AND NVL(GP.COD_POSIZIONE_GIURIDICA,'-') = POS_GIURIDICA.RV_LOW_VALUE)";
		lStatement += " AND (OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'"
				+ " AND NVL(GP.COD_OGGETTO_PROCEDIMENTO,'-') = OGGETTO_PROCEDIMENTO.RV_LOW_VALUE)";
		lStatement += " AND (FASC.FAS_SIE_ID_FASCICOLO_SIEP = FASIEP.ID_FASCICOLO_SIEP)";
		lStatement += " AND (FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS)";
		lStatement += " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE"
				+ " AND FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO)";
		lStatement += " AND (GP.COD_SEDE_MITTENTE = DESCR_COM_MIT.COD_COMUNE)";
		lStatement += " AND (GP.UDI_ID_UDIENZA = UD.ID_UDIENZA (+))";

		return lStatement;
	}

	protected String getFascicoloSqlQueryNoFaSiep() {

		String lStatement = new String();
		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR,"
				+ " FASC.CHIAVE_UFFICIO,";
		lStatement += " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO,"
				+ " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, ";
		lStatement += " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
		lStatement += " FASC.COD_OPERATORE_INSERIMENTO, FASC.COD_OPERATORE_AGGIORNAMENTO,";
		lStatement += " FASC.COD_UFFICIO_INSERIMENTO, FASC.COD_UFFICIO_AGGIORNAMENTO,";
		lStatement += " FASC.DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE,"
				+ " FASC.DATA_AGGIORNAMENTO,";
		lStatement += " FASC.FAS_SIE_ID_FASCICOLO_SIEP, FASC.SOG_ID_SOGGETTO, FASC.FAS_SIU_ID_FASCICOLO_SIUS,"
				+ " FASC.ID_FASCICOLO_SIUS_ORIGINE, FASC.DATA_DEFINIZIONE, FASC.NUMERO_FASCICOLI_UNIFICATI,";
		lStatement += " GP.ID_GENERALE_PROCEDIMENTO, GP.ANNO_S1, GP.PROGR_S1, GP.COD_OGGETTO_PROCEDIMENTO,"
				+ " GP.COD_TIPO_REGISTRO, TIPO_REGISTRO.RV_MEANING DESCR_TIPO_REGISTRO, ";
		lStatement += " OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA,"
				+ " GP.UDI_ID_UDIENZA, GP.DATA_RESTITUZIONE,"; // MEV_9: estraggo DATA_RESTITUZIONE
		lStatement += " GP.DATA_ARRIVO_CANCELLERIA, GP.DATA_CAMERA_CONSIGLIO, GP.COD_TIPO_ATTO,"
				+ " TIPO_ATTO.RV_MEANING DESCR_TIPO_ATTO,";
		lStatement += " GP.DATA_DEFINIZIONE, GP.TIPO_DEFINIZIONE, GP.DESCR_DEFINIZIONE,";
		lStatement += " GP.COD_TIPO_MITTENTE_ATTO, MITTENTE_ATTO.RV_MEANING DESCR_TIPO_MITTENTE,"
				+ " NVL(UD.DATA_UDIENZA, GP.DATA_CAMERA_CONSIGLIO) DATA_UDIENZA,";
		lStatement += " GP.COD_SEDE_MITTENTE, DESCR_COM_MIT.DESCRIZIONE DESCR_SEDE_MITTENTE, GP.ANNOTAZIONE,";
		lStatement += " GP.COD_OPERATORE_AGGIORNAMENTO GP_COD_OP_AGG,"
				+ " GP.COD_UFFICIO_AGGIORNAMENTO GP_COD_UFF_AGG, GP.DATA_AGGIORNAMENTO GP_D_AGG,";
		lStatement += " GP.SEZIONE SEZIONE, GP.DATA_FINE_PENA DATA_FINE_PENA,"
				+ " GP.COD_POSIZIONE_GIURIDICA COD_POSIZIONE_GIURIDICA,"
				+ " NVL(POS_GIURIDICA.RV_MEANING, '') DESCR_POSIZIONE_GIURIDICA,"
				+ " NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA,"
				+ " NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE,";
		lStatement += " null CHIAVE_ANNO_SIEP, null CHIAVE_PROGR_SIEP, '-' CHIAVE_UFFICIO_SIEP,";
		lStatement += " DESCR_TIPO_UFF.RV_LOW_VALUE COD_TIPO_UFFICIO,";
		lStatement += " FASC.CERTIFICATO_PENALE, FASC.VISIBILITA_EX_MINORENNE,";
		// MEV10-s3: aggiunto recupero nuovo campo in tabella
		lStatement += " DBMS_LOB.GETLENGTH(FASC.CERTIFICATO_PENALE) LEN_BLOB_CERT_PENALE";
		lStatement += " FROM FASCICOLO_SIUS FASC, GENERALE_PROCEDIMENTO GP, CG_REF_CODES STATO_FASCICOLO,"
				+ " UDIENZA UD,";
		lStatement += " CG_REF_CODES MITTENTE_ATTO, CG_REF_CODES DESCR_TIPO_UFF, CG_REF_CODES TIPO_ATTO,"
				+ " CG_REF_CODES OGGETTO_PROCEDIMENTO, CG_REF_CODES TIPO_REGISTRO,";
		lStatement += " CG_REF_CODES POS_GIURIDICA, UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_MIT";
		lStatement += " WHERE (STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO'"
				+ " AND NVL(STATO_FASCICOLO.RV_LOW_VALUE,'-') = FASC.COD_STATO_FASCICOLO)";
		lStatement += " AND (DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO'"
				+ " AND NVL(UFF.COD_TIPO_UFFICIO,'-') = DESCR_TIPO_UFF.RV_LOW_VALUE)";
		lStatement += " AND (MITTENTE_ATTO.RV_DOMAIN = 'MITTENTE_ATTO'"
				+ " AND NVL(GP.COD_TIPO_MITTENTE_ATTO,'-') = MITTENTE_ATTO.RV_LOW_VALUE)";
		lStatement += " AND (TIPO_REGISTRO.RV_DOMAIN = 'TIPO_REGISTRO'"
				+ " AND NVL(GP.COD_TIPO_REGISTRO,'-') = TIPO_REGISTRO.RV_LOW_VALUE)";
		lStatement += " AND (TIPO_ATTO.RV_DOMAIN = 'TIPO_ATTO'"
				+ " AND NVL(GP.COD_TIPO_ATTO,'-') = TIPO_ATTO.RV_LOW_VALUE)";
		lStatement += " AND (POS_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA'"
				+ " AND NVL(GP.COD_POSIZIONE_GIURIDICA,'-') = POS_GIURIDICA.RV_LOW_VALUE)";
		lStatement += " AND (OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'"
				+ " AND NVL(GP.COD_OGGETTO_PROCEDIMENTO,'-') = OGGETTO_PROCEDIMENTO.RV_LOW_VALUE)";
		lStatement += " AND (FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS)";
		lStatement += " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE"
				+ " AND FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO)";
		lStatement += " AND (GP.COD_SEDE_MITTENTE = DESCR_COM_MIT.COD_COMUNE)";
		lStatement += " AND (GP.UDI_ID_UDIENZA = UD.ID_UDIENZA (+))";

		return lStatement;
	}

	/*
	 * protected String getFascicoloSqlQueryPerPassaggioEvento() { String lStatement = new String();
	 * lStatement +=
	 * "SELECT FASC.ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, FASC.CHIAVE_UFFICIO, "; lStatement
	 * += " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, ";
	 * lStatement += " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO, ";
	 * lStatement += " FASC.COD_OPERATORE_INSERIMENTO, FASC.COD_OPERATORE_AGGIORNAMENTO, "; lStatement +=
	 * " FASC.COD_UFFICIO_INSERIMENTO, FASC.COD_UFFICIO_AGGIORNAMENTO, "; lStatement +=
	 * " FASC.DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, FASC.DATA_AGGIORNAMENTO, "; lStatement
	 * +=
	 * " FASC.FAS_SIE_ID_FASCICOLO_SIEP, FASC.SOG_ID_SOGGETTO, FASC.FAS_SIU_ID_FASCICOLO_SIUS, FASC.ID_FASCICOLO_SIUS_ORIGINE, FASC.DATA_DEFINIZIONE, FASC.NUMERO_FASCICOLI_UNIFICATI, "
	 * ; lStatement +=
	 * " GP.ID_GENERALE_PROCEDIMENTO, GP.ANNO_S1, GP.PROGR_S1, GP.COD_OGGETTO_PROCEDIMENTO,  GP.COD_TIPO_REGISTRO, TIPO_REGISTRO.RV_MEANING DESCR_TIPO_REGISTRO,"
	 * ; lStatement +=
	 * " OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA, GP.UDI_ID_UDIENZA, ";
	 * lStatement +=
	 * " GP.DATA_ARRIVO_CANCELLERIA, GP.DATA_CAMERA_CONSIGLIO, GP.COD_TIPO_ATTO, TIPO_ATTO.RV_MEANING DESCR_TIPO_ATTO, "
	 * ; lStatement +=
	 * " GP.COD_TIPO_MITTENTE_ATTO, MITTENTE_ATTO.RV_MEANING DESCR_TIPO_MITTENTE, NVL(UD.DATA_UDIENZA, GP.DATA_CAMERA_CONSIGLIO ) DATA_UDIENZA, "
	 * ; lStatement +=
	 * " GP.COD_SEDE_MITTENTE, DESCR_COM_MIT.DESCRIZIONE DESCR_SEDE_MITTENTE, GP.ANNOTAZIONE, "; lStatement +=
	 * " GP.COD_OPERATORE_AGGIORNAMENTO GP_COD_OP_AGG, GP.COD_UFFICIO_AGGIORNAMENTO GP_COD_UFF_AGG, GP.DATA_AGGIORNAMENTO GP_D_AGG, "
	 * ; lStatement +=
	 * " GP.SEZIONE SEZIONE, GP.DATA_FINE_PENA DATA_FINE_PENA, GP.COD_POSIZIONE_GIURIDICA COD_POSIZIONE_GIURIDICA, NVL(POS_GIURIDICA.RV_MEANING, '') DESCR_POSIZIONE_GIURIDICA, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, "
	 * ; lStatement +=
	 * " FASIEP.CHIAVE_ANNO CHIAVE_ANNO_SIEP, FASIEP.CHIAVE_PROGR CHIAVE_PROGR_SIEP, FASIEP.CHIAVE_UFFICIO CHIAVE_UFFICIO_SIEP "
	 * ; lStatement += " ,DESCR_TIPO_UFF.RV_LOW_VALUE COD_TIPO_UFFICIO "; lStatement +=
	 * " FROM FASCICOLO_SIUS FASC, GENERALE_PROCEDIMENTO GP, CG_REF_CODES STATO_FASCICOLO, UDIENZA UD, ";
	 * lStatement +=
	 * " CG_REF_CODES MITTENTE_ATTO, CG_REF_CODES DESCR_TIPO_UFF, CG_REF_CODES TIPO_ATTO, CG_REF_CODES OGGETTO_PROCEDIMENTO, CG_REF_CODES TIPO_REGISTRO,"
	 * ; lStatement +=
	 * " CG_REF_CODES POS_GIURIDICA, UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_MIT, FASCICOLO_SIEP FASIEP, PASSAGGIO_EVENTO PE  "
	 * ; lStatement +=
	 * " WHERE (STATO_FASCICOLO.RV_DOMAIN  = 'STATO_FASCICOLO' AND NVL(STATO_FASCICOLO.RV_LOW_VALUE,'-') = FASC.COD_STATO_FASCICOLO)"
	 * ; lStatement +=
	 * " AND (DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO'    AND NVL(UFF.COD_TIPO_UFFICIO,'-') = DESCR_TIPO_UFF.RV_LOW_VALUE)"
	 * ; lStatement +=
	 * " AND (MITTENTE_ATTO.RV_DOMAIN  = 'MITTENTE_ATTO'   AND NVL(GP.COD_TIPO_MITTENTE_ATTO,'-') = MITTENTE_ATTO.RV_LOW_VALUE)"
	 * ; lStatement +=
	 * " AND (TIPO_REGISTRO.RV_DOMAIN  = 'TIPO_REGISTRO'   AND NVL(GP.COD_TIPO_REGISTRO,'-') = TIPO_REGISTRO.RV_LOW_VALUE)"
	 * ; lStatement +=
	 * " AND (TIPO_ATTO.RV_DOMAIN      = 'TIPO_ATTO'       AND NVL(GP.COD_TIPO_ATTO,'-') = TIPO_ATTO.RV_LOW_VALUE)"
	 * ; lStatement +=
	 * " AND (POS_GIURIDICA.RV_DOMAIN        = 'POSIZIONE_GIURIDICA'  AND NVL(GP.COD_POSIZIONE_GIURIDICA,'-') = POS_GIURIDICA.RV_LOW_VALUE)"
	 * ; lStatement +=
	 * " AND (OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND NVL(GP.COD_OGGETTO_PROCEDIMENTO,'-') = OGGETTO_PROCEDIMENTO.RV_LOW_VALUE)"
	 * ; lStatement += " AND (FASC.FAS_SIE_ID_FASCICOLO_SIEP = FASIEP.ID_FASCICOLO_SIEP)"; lStatement +=
	 * " AND (FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS)"; lStatement +=
	 * " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE AND FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO)";
	 * lStatement += " AND (GP.COD_SEDE_MITTENTE = DESCR_COM_MIT.COD_COMUNE)"; lStatement +=
	 * " AND (PE.ANNO_FASCICOLO_SIUS = FASC.CHIAVE_ANNO)"; lStatement +=
	 * " AND (PE.PROGR_FASCICOLO_SIUS = FASC.CHIAVE_PROGR)"; lStatement +=
	 * " AND (PE.UFFICIO_FASCICOLO_SIUS = FASC.CHIAVE_UFFICIO)"; lStatement +=
	 * " AND (GP.UDI_ID_UDIENZA = UD.ID_UDIENZA (+) )";
	 *
	 * return lStatement; }
	 *
	 * protected String getFascicoloSqlQueryNoFaSiepPerPassaggioEvento() { String lStatement = new String();
	 * lStatement +=
	 * "SELECT FASC.ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, FASC.CHIAVE_UFFICIO, "; lStatement
	 * += " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, ";
	 * lStatement += " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO, ";
	 * lStatement += " FASC.COD_OPERATORE_INSERIMENTO, FASC.COD_OPERATORE_AGGIORNAMENTO, "; lStatement +=
	 * " FASC.COD_UFFICIO_INSERIMENTO, FASC.COD_UFFICIO_AGGIORNAMENTO, "; lStatement +=
	 * " FASC.DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, FASC.DATA_AGGIORNAMENTO, "; lStatement
	 * +=
	 * " FASC.FAS_SIE_ID_FASCICOLO_SIEP, FASC.SOG_ID_SOGGETTO, FASC.FAS_SIU_ID_FASCICOLO_SIUS, FASC.ID_FASCICOLO_SIUS_ORIGINE, FASC.DATA_DEFINIZIONE, FASC.NUMERO_FASCICOLI_UNIFICATI, "
	 * ; lStatement +=
	 * " GP.ID_GENERALE_PROCEDIMENTO, GP.ANNO_S1, GP.PROGR_S1, GP.COD_OGGETTO_PROCEDIMENTO,  GP.COD_TIPO_REGISTRO, TIPO_REGISTRO.RV_MEANING DESCR_TIPO_REGISTRO,"
	 * ; lStatement +=
	 * " OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA, GP.UDI_ID_UDIENZA, ";
	 * lStatement +=
	 * " GP.DATA_ARRIVO_CANCELLERIA, GP.DATA_CAMERA_CONSIGLIO, GP.COD_TIPO_ATTO, TIPO_ATTO.RV_MEANING DESCR_TIPO_ATTO, "
	 * ; lStatement +=
	 * " GP.COD_TIPO_MITTENTE_ATTO, MITTENTE_ATTO.RV_MEANING DESCR_TIPO_MITTENTE, NVL(UD.DATA_UDIENZA, GP.DATA_CAMERA_CONSIGLIO ) DATA_UDIENZA, "
	 * ; lStatement +=
	 * " GP.COD_SEDE_MITTENTE, DESCR_COM_MIT.DESCRIZIONE DESCR_SEDE_MITTENTE, GP.ANNOTAZIONE, "; lStatement +=
	 * " GP.COD_OPERATORE_AGGIORNAMENTO GP_COD_OP_AGG, GP.COD_UFFICIO_AGGIORNAMENTO GP_COD_UFF_AGG, GP.DATA_AGGIORNAMENTO GP_D_AGG, "
	 * ; lStatement +=
	 * " GP.SEZIONE SEZIONE, GP.DATA_FINE_PENA DATA_FINE_PENA, GP.COD_POSIZIONE_GIURIDICA COD_POSIZIONE_GIURIDICA, NVL(POS_GIURIDICA.RV_MEANING, '') DESCR_POSIZIONE_GIURIDICA, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, "
	 * ; lStatement += " null CHIAVE_ANNO_SIEP, null CHIAVE_PROGR_SIEP , '-' CHIAVE_UFFICIO_SIEP "; lStatement
	 * += " ,DESCR_TIPO_UFF.RV_LOW_VALUE COD_TIPO_UFFICIO "; lStatement +=
	 * " FROM FASCICOLO_SIUS FASC, GENERALE_PROCEDIMENTO GP, CG_REF_CODES STATO_FASCICOLO, UDIENZA UD, ";
	 * lStatement +=
	 * " CG_REF_CODES MITTENTE_ATTO, CG_REF_CODES DESCR_TIPO_UFF, CG_REF_CODES TIPO_ATTO, CG_REF_CODES OGGETTO_PROCEDIMENTO, CG_REF_CODES TIPO_REGISTRO,"
	 * ; lStatement +=
	 * " CG_REF_CODES POS_GIURIDICA, UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_MIT, PASSAGGIO_EVENTO PE "
	 * ; lStatement +=
	 * " WHERE (STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' AND STATO_FASCICOLO.RV_LOW_VALUE = FASC.COD_STATO_FASCICOLO)"
	 * ; lStatement +=
	 * " AND (DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE)"
	 * ; lStatement +=
	 * " AND (MITTENTE_ATTO.RV_DOMAIN = 'MITTENTE_ATTO' AND GP.COD_TIPO_MITTENTE_ATTO = MITTENTE_ATTO.RV_LOW_VALUE)"
	 * ; lStatement +=
	 * " AND (TIPO_REGISTRO.RV_DOMAIN = 'TIPO_REGISTRO' AND GP.COD_TIPO_REGISTRO = TIPO_REGISTRO.RV_LOW_VALUE)"
	 * ; lStatement +=
	 * " AND (TIPO_ATTO.RV_DOMAIN = 'TIPO_ATTO' AND GP.COD_TIPO_ATTO = TIPO_ATTO.RV_LOW_VALUE)"; lStatement +=
	 * " AND (OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND GP.COD_OGGETTO_PROCEDIMENTO = OGGETTO_PROCEDIMENTO.RV_LOW_VALUE)"
	 * ; lStatement +=
	 * " AND (POS_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA' AND GP.COD_POSIZIONE_GIURIDICA = POS_GIURIDICA.RV_LOW_VALUE)"
	 * ; lStatement += " AND (FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS)"; lStatement +=
	 * " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE AND FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO)";
	 * lStatement += " AND (GP.COD_SEDE_MITTENTE = DESCR_COM_MIT.COD_COMUNE)"; lStatement +=
	 * " AND (PE.ANNO_FASCICOLO_SIUS = FASC.CHIAVE_ANNO)"; lStatement +=
	 * " AND (PE.PROGR_FASCICOLO_SIUS = FASC.CHIAVE_PROGR)"; lStatement +=
	 * " AND (PE.UFFICIO_FASCICOLO_SIUS = FASC.CHIAVE_UFFICIO)"; lStatement +=
	 * " AND (GP.UDI_ID_UDIENZA = UD.ID_UDIENZA (+) )";
	 *
	 * return lStatement; }
	 */

	/**
	 * Esegue la ricerca di un fascicolo tramite Chiave_Anno e Chiave_Progr
	 *
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @throws DAOException
	 */
	public void ricercaFascicoloByAnnoProgr(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr)
			throws DAOException {

		// Controllo l'esistenza del Fascicolo Siep (se non esiste lo escludo dalla query
		String lStatement = new String();
		if (existFasSiep(aChiaveAnno, aChiaveProgr))
			lStatement = getFascicoloSqlQuery();
		else
			lStatement = getFascicoloSqlQueryNoFaSiep();

		lStatement += " AND FASC.CHIAVE_ANNO = '" + aChiaveAnno + "'";
		lStatement += " AND FASC.CHIAVE_PROGR = '" + aChiaveProgr + "'";

		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca di un fascicolo tramite Chiave_Anno , Chiave_Progr, Chiave_Ufficio
	 *
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @throws DAOException
	 */
	public void ricercaFascicoloByAnnoProgrUfficio(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr,
			String aChiaveUfficio) throws DAOException {

		// Controllo l'esistenza del Fascicolo Siep (se non esiste lo escludo dalla query
		String lStatement = new String();
		if (existFasSiep(aChiaveAnno, aChiaveProgr, aChiaveUfficio))
			lStatement = getFascicoloSqlQuery();
		else
			lStatement = getFascicoloSqlQueryNoFaSiep();

		lStatement += " AND FASC.CHIAVE_ANNO = " + aChiaveAnno;
		lStatement += " AND FASC.CHIAVE_PROGR = " + aChiaveProgr;
		lStatement += " AND FASC.CHIAVE_UFFICIO = '" + aChiaveUfficio + "'";

		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca di un fascicolo tramite ID_Soggetto e Cod_Oggetto
	 *
	 * @param ID_Soggetto
	 * @param Cod_Oggetto
	 * @throws DAOException
	 */
	public void ricercaFascicoloByIdSoggettoCodOggetto(BigDecimal aIdSoggetto, String aCodOggetto)
			throws DAOException {

		String lStatement = getFascicoloSqlQueryNoFaSiep();

		lStatement += " AND FASC.SOG_ID_SOGGETTO = " + aIdSoggetto;
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = '" + aCodOggetto + "'";

		setStatement(lStatement);
	}

	public boolean existFasSiep(BigDecimal aIdFascicoloSius) throws DAOException {

		String lStatement = "select count(*) as COUNT from fascicolo_sius fs where fs.FAS_SIE_ID_FASCICOLO_SIEP > 0 and fs.ID_FASCICOLO_SIUS = '"
				+ aIdFascicoloSius + "'";
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

	public boolean existFasSiep(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr) throws DAOException {

		String lStatement = "select count(*) as COUNT from fascicolo_sius fs where fs.FAS_SIE_ID_FASCICOLO_SIEP > 0 and fs.CHIAVE_PROGR = '"
				+ aChiaveProgr + "' and fs.CHIAVE_ANNO = '" + aChiaveAnno + "'";
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

	public boolean existFasSiep(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr, String aChiaveUfficio)
			throws DAOException {

		String lStatement = "select count(*) as COUNT from fascicolo_sius fs where fs.FAS_SIE_ID_FASCICOLO_SIEP > 0 and fs.CHIAVE_PROGR = '"
				+ aChiaveProgr + "' and fs.CHIAVE_ANNO = '" + aChiaveAnno + "' and fs.CHIAVE_UFFICIO = '"
				+ aChiaveUfficio + "'";
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

	public boolean existFasSiepByGenProc(BigDecimal aIdGenProc) throws DAOException {

		String lStatement = "select count(*) as COUNT from fascicolo_sius fs, generale_procedimento gp where fs.FAS_SIE_ID_FASCICOLO_SIEP > 0 and fs.ID_FASCICOLO_SIUS = gp.FAS_SIU_ID_FASCICOLO_SIUS and gp.ID_GENERALE_PROCEDIMENTO = '"
				+ aIdGenProc + "'";
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

	/*
	 * public boolean existFasSiepPerPassaggioEvento(BigDecimal aIdPassaggioEvento ) throws DAOException {
	 * String lStatement =
	 * "select count(*) as COUNT from passaggio_evento pe where pe.ID_PASSAGGIO_EVENTO = '"+
	 * aIdPassaggioEvento +"' and pe.PROGR_FASCICOLO_SIEP IS NOT NULL '"; setStatement( lStatement );
	 *
	 * this.start();
	 *
	 * BigDecimal lCount = null;
	 *
	 * if( this.next() ) lCount = this.getBigDecimal("COUNT");
	 *
	 * if( lCount.intValue() > 0 ) return true; else return false; }
	 */

	public boolean existFasSius(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr) throws DAOException {

		String lStatement = "select count(*) as COUNT from fascicolo_sius fs where fs.CHIAVE_PROGR = '"
				+ aChiaveProgr + "' and fs.CHIAVE_ANNO = '" + aChiaveAnno + "'";
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

	public boolean existFasSiusUfficio(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr,
			String ufficioUtenteConnesso) throws DAOException {

		String lStatement = "select count(*) as COUNT from fascicolo_sius fs where fs.CHIAVE_PROGR = '"
				+ aChiaveProgr + "' and fs.CHIAVE_ANNO = '" + aChiaveAnno + "' and fs.CHIAVE_UFFICIO = '"
				+ ufficioUtenteConnesso + "'";
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

	/**
	 * Esegue la ricerca di un fascicolo tramite IdPassaggioEvento
	 *
	 * @param aIdPassaggioEvento
	 * @throws DAOException
	 *
	 *             public void ricercaFascicoloByIdPassaggioEvento(BigDecimal aIdPassaggioEvento) throws
	 *             DAOException { //Controllo l'esistenza del Fascicolo Siep (se non esiste lo escludo dalla
	 *             query String lStatement = new String(); if (
	 *             existFasSiepPerPassaggioEvento(aIdPassaggioEvento) ) lStatement =
	 *             getFascicoloSqlQueryPerPassaggioEvento(); else lStatement =
	 *             getFascicoloSqlQueryNoFaSiepPerPassaggioEvento();
	 *
	 *             lStatement += " AND PE.ID_PASSAGGIO_EVENTO = " + aIdPassaggioEvento; setStatement(
	 *             lStatement ); }
	 */

	public BigDecimal ricercaFasciIdFascSIEP(BigDecimal idSius) throws DAOException {

		String lStatement = new String(" select fas_sie_id_fascicolo_siep from fascicolo_sius f where ");
		lStatement += " f.id_fascicolo_sius = " + idSius.toString();
		setStatement(lStatement);

		this.start();

		BigDecimal fas_sie_id_fascicolo_siep = null;
		if (this.next())
			fas_sie_id_fascicolo_siep = this.getBigDecimal("fas_sie_id_fascicolo_siep");

		return fas_sie_id_fascicolo_siep;
	}

}