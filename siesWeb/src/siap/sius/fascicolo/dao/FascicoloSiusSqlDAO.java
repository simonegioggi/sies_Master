package siap.sius.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sius.fascicolo.model.FascicoloSiusModel;

/**
 * FascicoloSiusSqlDAO - Classe SqlDAO che rappresenta la tabella FascicoloSius
 *
 * @version 1.0
 */
public class FascicoloSiusSqlDAO extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public FascicoloSiusSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaFascicoloSius(FascicoloSiusModel aModel) throws DAOException {
		String lStatement = new String("");
		lStatement = getSqlQuery();
		lStatement += " WHERE " + setCondizioni(aModel);
		setStatement(lStatement);
	}

	public void ricercaElencoFascicoliUnificati(FascicoloSiusModel aModel) throws DAOException {
		String lStatement = new String("");
		lStatement = getSqlQuery();
		lStatement += " WHERE " + setCondizioni(aModel);
		setStatement(lStatement);
	}

	public void ricercaFascicoloXOrigine(BigDecimal aField) throws DAOException {
		String lStatement = new String("");
		lStatement = getSqlQuery();
		lStatement += " WHERE ID_FASCICOLO_SIUS_ORIGINE = " + aField.toString();
		setStatement(lStatement);
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		FascicoloSiusModel aModel = new FascicoloSiusModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		aModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		// aModel.setDescrStatoFascicolo(getString("") );
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setIdFascicoloSiusOrigine(getBigDecimal("ID_FASCICOLO_SIUS_ORIGINE")); // 15/01/2004
		aModel.setDataDefinizione(getDate("DATA_DEFINIZIONE"));
		aModel.setNumeroFascicoliUnificati(getBigDecimal("NUMERO_FASCICOLI_UNIFICATI"));
		// MEV10-s3: aggiunto campo in db per gestire età minore/maggiore
		aModel.setVisibilitaMinorenne(getString("VISIBILITA_EX_MINORENNE"));
		return aModel;
	}

	/**
	 * Esegue la ricerca di un fascicolo tramite Chiave
	 * <p>
	 *
	 * @param aIdFascicoloSius
	 *            id del fascicolo SIUS da ricercare.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void ricercaFascicoloByKey(BigDecimal aIdFascicoloSius) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " WHERE ID_FASCICOLO_SIUS = " + aIdFascicoloSius;

		setStatement(lStatement);
	}

	/**
	 * Ritorna la select per estrazione dati del fascicolo sius.
	 * <p>
	 *
	 * @return select SQL.
	 */
	protected String getSqlQuery() {
		String lStatement = new String();
		lStatement += " SELECT " + "FASCICOLO_SIUS.ID_FASCICOLO_SIUS, " + "FASCICOLO_SIUS.CHIAVE_ANNO, "
				+ "FASCICOLO_SIUS.CHIAVE_UFFICIO, " + "FASCICOLO_SIUS.CHIAVE_PROGR, "
				+ "FASCICOLO_SIUS.COD_STATO_FASCICOLO, " + "FASCICOLO_SIUS.COD_OPERATORE_INSERIMENTO, "
				+ "FASCICOLO_SIUS.COD_UFFICIO_INSERIMENTO, " + "FASCICOLO_SIUS.DATA_INSERIMENTO, "
				+ "FASCICOLO_SIUS.DATA_ISCRIZIONE, " + "FASCICOLO_SIUS.COD_OPERATORE_AGGIORNAMENTO, "
				+ "FASCICOLO_SIUS.COD_UFFICIO_AGGIORNAMENTO, " + "FASCICOLO_SIUS.DATA_AGGIORNAMENTO, "
				+ "FASCICOLO_SIUS.SOG_ID_SOGGETTO, " + "FASCICOLO_SIUS.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "FASCICOLO_SIUS.FAS_SIU_ID_FASCICOLO_SIUS, " + "FASCICOLO_SIUS.ID_FASCICOLO_SIUS_ORIGINE, "
				+ "FASCICOLO_SIUS.DATA_DEFINIZIONE, " + "FASCICOLO_SIUS.NUMERO_FASCICOLI_UNIFICATI, "
				// MEV10-s3: aggiunto campo in db per gestire età minore/maggiore
				+ "FASCICOLO_SIUS.VISIBILITA_EX_MINORENNE";
		lStatement += " FROM FASCICOLO_SIUS";
		return lStatement;
	}

	/**
	 * Ritorna campi essenziali per elenco fascicoli
	 * <p>
	 *
	 * @return select SQL.
	 */
	protected String getSqlQueryElenco() {
		String lStatement = new String();
		lStatement += " SELECT " + "FASCICOLO_SIUS.ID_FASCICOLO_SIUS, " + "FASCICOLO_SIUS.CHIAVE_ANNO, "
				+ "FASCICOLO_SIUS.CHIAVE_UFFICIO, " + "FASCICOLO_SIUS.CHIAVE_PROGR, "
				+ "FASCICOLO_SIUS.FAS_SIE_ID_FASCICOLO_SIEP, " + "FASCICOLO_SIUS.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " FROM FASCICOLO_SIUS";
		return lStatement;
	}

	// public void selCondizione(FascicoloSiusModel aModel) {
	// String lCondizioni = new String();
	// boolean lInserito = false;
	// }

	public String setCondizioni(FascicoloSiusModel aModel) {
		String lCondizioni = new String();

		if (aModel.getFasSiuIdFascicoloSius() != null) // Mi aspettavo di trovarli gia' ed ho passato un
														// model!!! :-)
		{
			lCondizioni += " FASCICOLO_SIUS.FAS_SIU_ID_FASCICOLO_SIUS =" + aModel.getFasSiuIdFascicoloSius();
		}

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " FASCICOLO_SIUS.FAS_SIE_ID_FASCICOLO_SIEP =" + aModel.getFasSieIdFascicoloSiep();
		}

		// boolean lInserito = false;
		return lCondizioni;
	}

	/**
	 * Calcola il Massimo Progressivo relativo ad un certo ufficio e all'anno in corso. Il massimo progressivo
	 * rappresenta anche l'ultimo progressivo inserito all'intenro dell'ufficio trattato.
	 * <p>
	 *
	 * @param aFascSiusModel
	 *            istanza model del fascicolo SIUS.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void getProgressivoFascicoloSius(FascicoloSiusModel aFascSiusModel) throws DAOException {
		// String lStatement = getSqlQuery(); // 29/04/2004
		String lStatement = "";

		lStatement += " SELECT MAX(CHIAVE_PROGR) aMAX";
		lStatement += " FROM FASCICOLO_SIUS FS";
		lStatement += " WHERE FS.CHIAVE_ANNO = " + aFascSiusModel.getChiaveAnno();
		lStatement += " AND FS.CHIAVE_UFFICIO = '" + aFascSiusModel.getChiaveUfficio() + "'";

		setStatement(lStatement);
	}

	/**
	 * Ricerca per Id_fasciolo_sius_Origine ed il campo cod_oggetto_procedimento
	 * <p>
	 *
	 * @param idFasOrigine
	 *            id del fascicolo sius.
	 * @aCodOggetto codice oggetto procedimento.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */

	public void getOrigineCodOggetto(BigDecimal aIdFascicoloSius, String aCodOggetto) throws DAOException {
		String lStatement = getSqlQuery();
		lStatement += " ,GENERALE_PROCEDIMENTO ";
		lStatement += " WHERE ";
		lStatement += " FASCICOLO_SIUS.ID_FASCICOLO_SIUS_ORIGINE = " + aIdFascicoloSius;
		lStatement += " AND GENERALE_PROCEDIMENTO.FAS_SIU_ID_FASCICOLO_SIUS = FASCICOLO_SIUS.ID_FASCICOLO_SIUS ";
		lStatement += " AND GENERALE_PROCEDIMENTO.COD_OGGETTO_PROCEDIMENTO = '" + aCodOggetto + "' ";
		setStatement(lStatement);
	}

	/**
	 * Metodo di ricerca di un FASCICOLO_SIUS con stessi: CHIAVE_ANNO, CHIAVE_PROGR, CHIAVE_UFFICIO.
	 * <p>
	 *
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @param aChiaveUfficio
	 * @throws DAOException
	 * @return boolean
	 */
	public String ExistAnnoProgrSius(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr, String aChiaveUfficio)
			throws DAOException {
		String response = "";
		String lStatement = "select FS.CHIAVE_ANNO , FS.CHIAVE_PROGR, FS.CHIAVE_UFFICIO ";
		lStatement += " from FASCICOLO_SIUS FS";
		lStatement += " where FS.CHIAVE_ANNO = '" + aChiaveAnno + "' ";
		lStatement += " and FS.CHIAVE_PROGR =  '" + aChiaveProgr + "' ";
		lStatement += " and FS.CHIAVE_UFFICIO =  '" + aChiaveUfficio + "' ";

		setStatement(lStatement);

		start();

		// BigDecimal lCount = null;
		if (next()) {
			response = getBigDecimal("CHIAVE_ANNO").toString() + "/"
					+ getBigDecimal("CHIAVE_PROGR").toString();
			return response;
		} else
			return response;
	}

	/**
	 * Metodo che verifica l'esistenza di FASCICOLI SIUS e/o SIEP per un ID_SOGGETTO.
	 * <p>
	 *
	 * @param aIdSoggetto
	 * @throws DAOException
	 * @return boolean
	 */
	public boolean ExistAltroFascicoloPerSoggetto(BigDecimal aIdSoggetto) throws DAOException {
		boolean response;
		String lStatement = "select ID_FASCICOLO_SIUS ID from fascicolo_SIUS where SOG_ID_SOGGETTO = '"
				+ aIdSoggetto + "' ";
		lStatement += " union (select ID_FASCICOLO_SIEP ID from fascicolo_SIEP where SOG_ID_SOGGETTO = '"
				+ aIdSoggetto + "' )";

		setStatement(lStatement);

		start();

		// BigDecimal lCount = null;
		if (next())
			response = true;
		else
			response = false;

		return response;
	}

	/**
	 * ricercaFascicoloSiusByMagistratoSorvAssegnatarioPaged
	 *
	 * 20251010 [SG]: paginata la ricerca
	 *
	 * @param aCodMagistrato
	 * @param aCodUfficio
	 * @param aStato
	 * @param aPage
	 * @throws DAOException
	 */
	public void ricercaFascicoloSiusByMagistratoSorvAssegnatarioPaged(String aCodMagistrato,
			String aCodUfficio, String[] aStato, int aPage) throws DAOException {

		String lStatement = new String();
		String lPaginedStatement = new String("");

		lStatement += "SELECT FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR,";
		lStatement += " FASC.CHIAVE_UFFICIO, DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO,"
				+ " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_TIPO_UFF.RV_LOW_VALUE  COD_TIPO_UFFICIO,";
		lStatement += " FASC.COD_OPERATORE_AGGIORNAMENTO, FASC.COD_OPERATORE_INSERIMENTO,";
		lStatement += " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
		lStatement += " FASC.COD_UFFICIO_AGGIORNAMENTO, FASC.COD_UFFICIO_INSERIMENTO,"
				+ " FASC.DATA_AGGIORNAMENTO,";
		lStatement += " FASC.DATA_INSERIMENTO,";
		lStatement += " FASC.DATA_ISCRIZIONE, FASC.FAS_SIE_ID_FASCICOLO_SIEP,"
				+ " FASC.FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " FASC.ID_FASCICOLO_SIUS,";
		lStatement += " FASC.ID_FASCICOLO_SIUS_ORIGINE, FASC.DATA_DEFINIZIONE,"
				+ " FASC.NUMERO_FASCICOLI_UNIFICATI,";
		// MEV10-s3: aggiunto campo in db per gestire età minore/maggiore
		lStatement += " FASC.SOG_ID_SOGGETTO, FASC.VISIBILITA_EX_MINORENNE ";
		lStatement += " FROM FASCICOLO_SIUS FASC";
		lStatement += " LEFT OUTER JOIN UFFICIO UFF ON (FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFF ON (UFF.COD_TIPO_UFFICIO ="
				+ " DESCR_TIPO_UFF.RV_LOW_VALUE AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFF ON (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES STATO_FASCICOLO ON (FASC.COD_STATO_FASCICOLO ="
				+ " STATO_FASCICOLO.RV_LOW_VALUE AND STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO')";
		lStatement += " , MAGISTRATO_RELATORE ";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIUS is not NULL";
		lStatement += "  AND (FASC.CHIAVE_UFFICIO = '" + aCodUfficio + "')";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = MAGISTRATO_RELATORE.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND MAGISTRATO_RELATORE.MAG_COD_MAGISTRATO = '" + aCodMagistrato + "'";
		lStatement += " AND MAGISTRATO_RELATORE.DATA_FINE is null ";

		String lCondizioni = "";
		if (aStato != null && aStato.length > 0) {
			lCondizioni += " AND FASC.COD_STATO_FASCICOLO IN (";
			for (int i = 0; i < aStato.length; i++) {
				lCondizioni += "'" + aStato[i] + "'";
				if (aStato.length > 1 && i < aStato.length - 1)
					lCondizioni += ",";
			}
			lCondizioni += ")";
		}

		lStatement += lCondizioni;
		lStatement += " order by FASC.CHIAVE_ANNO asc, FASC.CHIAVE_PROGR asc";

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/**
	 * ricercaFascicoloSiusByMagistratoSorvAssegnatario
	 *
	 * @param aCodMagistrato
	 * @param aCodUfficio
	 * @param aStato
	 * @throws DAOException
	 */
	public void ricercaFascicoloSiusByMagistratoSorvAssegnatario(String aCodMagistrato, String aCodUfficio,
			String[] aStato) throws DAOException {

		String lStatement = new String();

		lStatement += "SELECT FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR,";
		lStatement += " FASC.CHIAVE_UFFICIO, DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO,"
				+ " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_TIPO_UFF.RV_LOW_VALUE  COD_TIPO_UFFICIO,";
		lStatement += " FASC.COD_OPERATORE_AGGIORNAMENTO, FASC.COD_OPERATORE_INSERIMENTO,";
		lStatement += " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
		lStatement += " FASC.COD_UFFICIO_AGGIORNAMENTO, FASC.COD_UFFICIO_INSERIMENTO,"
				+ " FASC.DATA_AGGIORNAMENTO,";
		lStatement += " FASC.DATA_INSERIMENTO,";
		lStatement += " FASC.DATA_ISCRIZIONE, FASC.FAS_SIE_ID_FASCICOLO_SIEP,"
				+ " FASC.FAS_SIU_ID_FASCICOLO_SIUS, ";
		lStatement += " FASC.ID_FASCICOLO_SIUS,";
		lStatement += " FASC.ID_FASCICOLO_SIUS_ORIGINE, FASC.DATA_DEFINIZIONE,"
				+ " FASC.NUMERO_FASCICOLI_UNIFICATI,";
		// MEV10-s3: aggiunto campo in db per gestire età minore/maggiore
		lStatement += " FASC.SOG_ID_SOGGETTO, FASC.VISIBILITA_EX_MINORENNE ";
		lStatement += " FROM FASCICOLO_SIUS FASC";
		lStatement += " LEFT OUTER JOIN UFFICIO UFF ON (FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFF ON (UFF.COD_TIPO_UFFICIO ="
				+ " DESCR_TIPO_UFF.RV_LOW_VALUE AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFF ON (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES STATO_FASCICOLO ON (FASC.COD_STATO_FASCICOLO ="
				+ " STATO_FASCICOLO.RV_LOW_VALUE AND STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO')";
		lStatement += " , MAGISTRATO_RELATORE ";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIUS is not NULL";
		lStatement += "  AND (FASC.CHIAVE_UFFICIO = '" + aCodUfficio + "')";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = MAGISTRATO_RELATORE.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND MAGISTRATO_RELATORE.MAG_COD_MAGISTRATO = '" + aCodMagistrato + "'";
		lStatement += " AND MAGISTRATO_RELATORE.DATA_FINE is null ";

		String lCondizioni = "";
		if (aStato != null && aStato.length > 0) {
			lCondizioni += " AND FASC.COD_STATO_FASCICOLO IN (";
			for (int i = 0; i < aStato.length; i++) {
				lCondizioni += "'" + aStato[i] + "'";
				if (aStato.length > 1 && i < aStato.length - 1)
					lCondizioni += ",";
			}
			lCondizioni += ")";
		}

		lStatement += lCondizioni;
		lStatement += " order by FASC.CHIAVE_ANNO asc, FASC.CHIAVE_PROGR asc ";

		setStatement(lStatement);
	}

	public void getCountProcedimenti(String aCodMagistrato, String aCodUfficio, String[] aStato) {

		String lStatement = new String();

		lStatement += "SELECT count(*) as HowManyRecords FROM FASCICOLO_SIUS FASC";
		lStatement += " LEFT OUTER JOIN UFFICIO UFF ON (FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO )";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES DESCR_TIPO_UFF ON (UFF.COD_TIPO_UFFICIO ="
				+ " DESCR_TIPO_UFF.RV_LOW_VALUE AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " LEFT OUTER JOIN COMUNE DESCR_COM_UFF ON (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE)";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES STATO_FASCICOLO ON (FASC.COD_STATO_FASCICOLO ="
				+ " STATO_FASCICOLO.RV_LOW_VALUE AND STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO')";
		lStatement += " , MAGISTRATO_RELATORE ";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIUS is not NULL";
		lStatement += "  AND (FASC.CHIAVE_UFFICIO = '" + aCodUfficio + "')";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = MAGISTRATO_RELATORE.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND MAGISTRATO_RELATORE.MAG_COD_MAGISTRATO = '" + aCodMagistrato + "'";
		lStatement += " AND MAGISTRATO_RELATORE.DATA_FINE is null ";

		String lCondizioni = "";
		if (aStato != null && aStato.length > 0) {
			lCondizioni += " AND FASC.COD_STATO_FASCICOLO IN (";
			for (int i = 0; i < aStato.length; i++) {
				lCondizioni += "'" + aStato[i] + "'";
				if (aStato.length > 1 && i < aStato.length - 1)
					lCondizioni += ",";
			}
			lCondizioni += ")";
		}

		lStatement += lCondizioni;
		setStatement(lStatement);
	}

	/**
	 * Ritorna la lunghezza del certificato penale associato ad un Fascicolo
	 *
	 * @param aIdFascicolo
	 *            id Fascicolo
	 * @throws DAOException
	 */
	public void getLengthCertPenaleByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT DBMS_LOB.GETLENGTH(CERTIFICATO_PENALE) LEN_BLOB_CERT_PENALE ";
		lStatement += " FROM FASCICOLO_SIUS WHERE ";
		lStatement += " ID_FASCICOLO_SIUS = " + aIdFascicolo;
		setStatement(lStatement);
	}

	/**
	 * Ritorna il certificato penale associato ad un Fascicolo
	 *
	 * @param aIdFascicolo
	 *            id Fascicolo
	 * @throws DAOException
	 */
	public void getCertificatoPenaleByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT CERTIFICATO_PENALE ";
		lStatement += " FROM FASCICOLO_SIUS WHERE ";
		lStatement += " ID_FASCICOLO_SIUS = " + aIdFascicolo;
		setStatement(lStatement);
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'
	 *
	 * @param fsm
	 * @param pagine
	 * @throws DAOException
	 */
	public void ricercaIdFascicoli(FascicoloSiusModel fsm, int pagine) throws DAOException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ricercaIdFascicoli(): inizio!");

		String s = "select id_fascicolo_sius ";
		// 20190530 [SG]: aggiunto alias sul fascicolo
		s += "from fascicolo_sius f ";
		if (Utils.isPresent(fsm.getCodCancelleria()))
			s += ", canc_ass_fasc_sius s ";
		s += "where ";
		if (fsm.getChiaveAnnoIniziale() != null) {
			s += "((f.CHIAVE_ANNO > " + fsm.getChiaveAnnoIniziale() + ")";
			s += " OR (f.CHIAVE_ANNO = " + fsm.getChiaveAnnoIniziale() + " AND f.CHIAVE_PROGR >= "
					+ fsm.getChiaveProgrIniziale() + "))";
			s += " AND ((f.CHIAVE_ANNO < " + fsm.getChiaveAnnoFinale() + ")";
			s += " OR (f.CHIAVE_ANNO = " + fsm.getChiaveAnnoFinale() + " AND f.CHIAVE_PROGR <= "
					+ fsm.getChiaveProgrFinale() + "))";
		}
		if (fsm.getDataIscrizioneIniziale() != null) {
			if (fsm.getChiaveAnnoIniziale() != null)
				s += " AND ";
			s += "TO_CHAR(f.DATA_ISCRIZIONE,'YYYYMMDD') BETWEEN '"
					+ DateUtils.getDateToString(fsm.getDataIscrizioneIniziale(), "yyyyMMdd") + "' AND '"
					+ DateUtils.getDateToString(fsm.getDataIscrizioneFinale(), "yyyyMMdd") + "'";
		}
		if (Utils.isPresent(fsm.getChiaveUfficio()))
			s += " AND f.CHIAVE_UFFICIO = '" + fsm.getChiaveUfficio() + "'";
		else
			s += " AND f.COD_OPERATORE_INSERIMENTO = '" + fsm.getCodOperatoreInserimento() + "'";
		if (Utils.isPresent(fsm.getCodCancelleria())) {
			s += " AND s.fas_sius_id_fascicolo_sius = f.id_fascicolo_sius";
			s += " AND s.cod_cancelleria_assegnataria = '" + fsm.getCodCancelleria() + "'";
		}
		// ordinamento
		s += " order by f.chiave_anno, f.chiave_progr";

		if (pagine > 0) {
			String paginazione = new String("");
			paginazione = "SELECT * FROM (SELECT INNER.*, Rownum rn FROM (" + s
					+ " ) INNER ) WHERE rn between "
					+ ((pagine - 1) * IWebConstants.RESULT_PER_PAGE_ESITO + 1) + " AND "
					+ (pagine) * IWebConstants.RESULT_PER_PAGE_ESITO;
			setStatement(paginazione);
		} else
			setStatement(s);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ricercaIdFascicoli(): fine!");
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'
	 *
	 * @return GenericModel
	 * @throws DAOException
	 */
	public GenericModel getCopertineFascicoliSiusModel() throws DAOException {

		FascicoloSiusModel fsm = new FascicoloSiusModel();

		fsm.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		return fsm;
	}

}