package siap.siep.penaresidua.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.dao.SIAPSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;

/**
 * Classe SqlDAO che rappresenta la tabella PenaResidua
 *
 * @version 1.0
 */

public class PenaResiduaSqlDAO extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public PenaResiduaSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Costruisce la query in base al contenuto del model <b>In realtà utilizza solo l'id fascicolo</b>
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaPenaResidua(PenaResiduaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Effettua la ricerca per id pena_residua
	 *
	 * @param aKey
	 *            - id_Pena_residua
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Ricerca Pena Residua By Key Evento
	 * 
	 * @param aKey
	 *            - ID_evento
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaByKeyEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " EVE_ID_EVENTO = " + aKey;

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Effettua la ricerca dei record pena_residua NON VALIDATI (FLAG_VALIDATO = 'N') ordinati per data
	 * CRESCENTE (dal meno recente)
	 * 
	 * @param aKeyFascicolo
	 *            - id fascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaFlagValidato(BigDecimal aKeyFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo + " AND FLAG_VALIDATO = 'N'";
		lSql += " ORDER BY DATA_INSERIMENTO ";

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Effettua la ricerca dei record pena_residua NON VALIDATI (FLAG_VALIDATO = 'N') ordinati per data
	 * descrescente (dal più recente)
	 * 
	 * @param aKeyFascicolo
	 *            - id fascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaFlagNonValidatoDesc(BigDecimal aKeyFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo + " AND FLAG_VALIDATO = 'N'";
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Effettua la ricerca dei record pena_residua ordinati per data decrescente (dal più recente) validati o
	 * meno
	 *
	 * @param aKeyFascicolo
	 *            - id fascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaCorrenteByFascicoloSiepData(BigDecimal aKeyFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " ORDER BY DATA_INSERIMENTO DESC ";

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Restituisce l'elenco delle PENE_RESIDUE VALIDATE ordinate per data decrescente
	 *
	 * @param aKeyFascicolo
	 *            - id fascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaCorrenteByFascicoloSiepDataDesc(BigDecimal aKeyFascicolo)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " AND FLAG_VALIDATO = 'S'";
		lSql += " ORDER BY DATA_INSERIMENTO DESC, PENA_RESIDUA.ID_PENA_RESIDUA DESC ";

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Restituisce l'elenco delle PENE_RESIDUE ordinate per data decrescente indipendentemente dallo stato di
	 * validazione
	 * 
	 * @param aKeyFascicolo
	 *            - id fascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaCorrenteByFascicoloSiepDataDesc_IgnoraValidazione(BigDecimal aKeyFascicolo)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Restituisce l'elenco delle PENE_RESIDUE ordinate per data inserimento decrescente (validate o meno)
	 * 
	 * @param aKeyFascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaByIdFascicoloDataDesc(BigDecimal aKeyFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += setCondizioneByIdFascicolo(aKeyFascicolo);
		lSql += setOrderDataInserimentoDesc();

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Restituisce la PENA_RESIDUA VALIDATA con data inserimento più recente
	 * 
	 * @param aKeyFascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(BigDecimal aKeyFascicolo)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " AND FLAG_VALIDATO = 'S' ";
		lSql += " AND DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM PENA_RESIDUA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aKeyFascicolo + " AND FLAG_VALIDATO = 'S') ";
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Restituisce la PENA_RESIDUA VALIDATA con data inserimento più recente e data fine valorizzata (not
	 * null)
	 *
	 * @param aKeyFascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidataDataFinePena(BigDecimal aKeyFascicolo)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " AND FLAG_VALIDATO = 'S' ";
		lSql += " AND DATA_FINE IS NOT NULL ";
		lSql += " AND DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM PENA_RESIDUA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aKeyFascicolo + " AND FLAG_VALIDATO = 'S' AND DATA_FINE IS NOT NULL) ";
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Restituisce la PENA_RESIDUA VALIDATA con data inserimento più recente e FLAG_PENA_SOSPESA = 'S'
	 * 
	 * @param aKeyFascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidataSospesa(BigDecimal aKeyFascicolo)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " AND FLAG_VALIDATO = 'S' ";
		lSql += " AND FLAG_PENA_SOSPESA = 'S' ";
		lSql += " AND DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM PENA_RESIDUA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aKeyFascicolo + " AND FLAG_VALIDATO = 'S' AND FLAG_PENA_SOSPESA = 'S') ";
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Restituisce la PENA_RESIDUA NON VALIDATA con data inserimento più recente e FLAG_PENA_SOSPESA passato
	 * in input
	 * 
	 * @param aKeyFascicolo
	 * @param aFlagPena
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaCorrenteByFascicoloSiepUltimaNonValidataFlagPena(BigDecimal aKeyFascicolo,
			String aFlagPena) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " AND FLAG_VALIDATO = 'N' ";
		lSql += " AND FLAG_PENA_SOSPESA = '" + aFlagPena + "' ";
		lSql += " AND DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM PENA_RESIDUA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aKeyFascicolo + " AND FLAG_VALIDATO = 'S' AND FLAG_PENA_SOSPESA = 'S') ";
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Restituisce la PENA_RESIDUA NON VALIDATA con data inserimento più recente e FLAG_PENA_SOSPESA S
	 * 
	 * @param aKeyFascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaCorrenteByFascicoloSiepUltimaNonValidataSospesa(BigDecimal aKeyFascicolo)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " AND FLAG_VALIDATO = 'N' ";
		lSql += " AND FLAG_PENA_SOSPESA = 'S' ";
		lSql += " AND DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM PENA_RESIDUA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aKeyFascicolo + " AND FLAG_VALIDATO = 'N' AND FLAG_PENA_SOSPESA = 'S') ";
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Restituisce la PENA_RESIDUA VALIDATA con data inserimento più recente e FLAG_PENA_SOSPESA S
	 * 
	 * @param aKeyFascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidataSospesaInterruzione(
			BigDecimal aKeyFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " AND FLAG_VALIDATO = 'S' ";
		lSql += " AND FLAG_PENA_SOSPESA = 'I' ";
		lSql += " AND DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM PENA_RESIDUA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aKeyFascicolo + " AND FLAG_VALIDATO = 'S' AND FLAG_PENA_SOSPESA = 'I') ";
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/*****************************************************************************
	 * Recupera il record PENA_RESIDUA con data inserimento più recente indipendentemente dallo stato
	 * 
	 * @param aKey
	 *            - id del fascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaCorrenteByFascicoloSiep(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM PENA_RESIDUA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aKey + ")";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

		setStatement(lSql);
	}

	/*****************************************************************************
	 * 
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaCorrenteFlagPiuMenoByFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws DAOException {
		String lSql = "SELECT " + "ID_PENA_RESIDUA, " + "DATA_INIZIO, " + "DATA_FINE, "
				+ "NUM_ANNI_RECLUSIONE, " + "NUM_MESI_RECLUSIONE, " + "NUM_GIORNI_RECLUSIONE, "
				+ "IMPORTO_MULTA, " + "NUM_ANNI_ARRESTO, " + "NUM_MESI_ARRESTO, " + "NUM_GIORNI_ARRESTO, "
				+ "IMPORTO_AMMENDA, " + "DIES_A_QUO, " + "PENA_RESIDUA.COD_OPERATORE_INSERIMENTO, "
				+ "PENA_RESIDUA.DATA_INSERIMENTO, " + "PENA_RESIDUA.COD_UFFICIO_INSERIMENTO, "
				+ "PENA_RESIDUA.COD_OPERATORE_AGGIORNAMENTO, " + "PENA_RESIDUA.DATA_AGGIORNAMENTO, "
				+ "PENA_RESIDUA.COD_UFFICIO_AGGIORNAMENTO, " + "PENA_RESIDUA.EVE_ID_EVENTO, "
				+ "PENA_RESIDUA.FAS_SIE_ID_FASCICOLO_SIEP, " + "FLAG_VALIDATO, " + "DATA_FINE_PRESUNTA, "
				+ "DATA_FINE_RECLUSIONE," + "DATA_INIZIO_ARRESTO," + "FLAG_ERGASTOLO, "
				+ "DATA_INIZIO_ISOLAMENTO_DIURNO, " + "DATA_FINE_ISOLAMENTO_DIURNO, "
				+ "NUM_ANNI_ISOLAMENTO_DIURNO, " + "NUM_MESI_ISOLAMENTO_DIURNO, "
				+ "NUM_GIORNI_ISOLAMENTO_DIURNO, " + "MIS_ALT_ID_MISURA_ALTERNATIVA, " +
				// "FLAG_SANZIONE_SOSTITUTIVA, "+
				// "COD_TIPO_SANZIONE, "+
				// "NUM_ANNI_SS, "+
				// "NUM_MESI_SS, "+
				// "NUM_GIORNI_SS, "+
				// "IMPORTO_SS, "+
				// "DATA_INIZIO_SS, "+
				// "DATA_FINE_SS, "+
				"FLAG_PENA_SOSPESA " + "FROM PENA_RESIDUA, EVENTO "
				+ "WHERE EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep
				+ " AND EVENTO.FLAG_PIU_MENO IS NOT NULL "
				+ " AND PENA_RESIDUA.EVE_ID_EVENTO = EVENTO.ID_EVENTO "
				+ " ORDER BY EVENTO.DATA_INSERIMENTO DESC ";

		setStatement(lSql);
	}

	/**
	 * Ricerca la pena residua da associare a una archiviazione RES. La pena da recuperare è l'ultima pena
	 * inserita prima dell'archiviazione, quindi viene ricercata sugli eventi inseriti prima
	 * dell'archiviazione
	 *
	 * @param aIdFascicolo
	 * @param aDataInserimento
	 *            - data inserimento dell'evevnto di archiviazione
	 * @throws DAOException
	 */
	public void ricercaPenaDaArchiviazione(BigDecimal aIdFascicolo, Date aDataInserimento)
			throws DAOException {

		String lStatement = "";
		lStatement += " SELECT  PR.ID_PENA_RESIDUA, "
				+ " PR.DATA_INIZIO, PR.DATA_FINE_PRESUNTA, PR.DATA_FINE, "
				+ " PR.DATA_FINE_RECLUSIONE, PR.DATA_INIZIO_ARRESTO,"
				+ " PR.NUM_ANNI_RECLUSIONE, PR.NUM_MESI_RECLUSIONE, PR.NUM_GIORNI_RECLUSIONE, "
				+ " PR.IMPORTO_MULTA, " + " PR.NUM_ANNI_ARRESTO, PR.NUM_MESI_ARRESTO, PR.NUM_GIORNI_ARRESTO, "
				+ " PR.IMPORTO_AMMENDA, " + " PR.DIES_A_QUO, "
				+ " PR.COD_OPERATORE_INSERIMENTO, PR.DATA_INSERIMENTO, PR.COD_UFFICIO_INSERIMENTO, "
				+ " PR.COD_OPERATORE_AGGIORNAMENTO, PR.DATA_AGGIORNAMENTO, PR.COD_UFFICIO_AGGIORNAMENTO, "
				+ " PR.EVE_ID_EVENTO, PR.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ " PR.FLAG_VALIDATO, PR.FLAG_ERGASTOLO, PR.FLAG_PENA_SOSPESA, "
				+ " PR.DATA_INIZIO_ISOLAMENTO_DIURNO,PR.DATA_FINE_ISOLAMENTO_DIURNO, "
				+ " PR.NUM_ANNI_ISOLAMENTO_DIURNO, PR.NUM_MESI_ISOLAMENTO_DIURNO, PR.NUM_GIORNI_ISOLAMENTO_DIURNO, "
				+ " PR.MIS_ALT_ID_MISURA_ALTERNATIVA ";
		// " PR.FLAG_SANZIONE_SOSTITUTIVA, "+
		// " PR.COD_TIPO_SANZIONE, "+
		// " PR.NUM_ANNI_SS, "+
		// " PR.NUM_MESI_SS, "+
		// " PR.NUM_GIORNI_SS, "+
		// " PR.IMPORTO_SS, "+
		// " PR.DATA_INIZIO_SS, "+
		// " PR.DATA_FINE_SS ";
		lStatement += "    FROM PENA_RESIDUA PR, EVENTO";
		lStatement += "   WHERE PR.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += "     AND PR.EVE_ID_EVENTO = EVENTO.ID_EVENTO ";
		// n.b. sulle date il test è <= perchè in genere per i dati migrati RES non sono presenti ora, minuti
		// e secondi
		// per cui due eventi inseriti lo stesso giorno hanno la stessa data inserimento. E' il motivo per cui
		// l'order by è anche per ID_EVENTO in modo da poter ordinare gli eventi inseriti nello stesso giorno.
		lStatement += "     AND EVENTO.DATA_INSERIMENTO <= to_date ('"
				+ DateUtils.getDateToString(aDataInserimento, "dd/MM/yyyy HH:mm:ss")
				+ "','dd/MM/yyyy hh24:mi:ss')";
		lStatement += "   ORDER BY EVENTO.DATA_INSERIMENTO DESC, EVENTO.ID_EVENTO DESC ";
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" " + lStatement);

		setStatement(lStatement);
	}

	/**
	 * Recupera l'ultima pena migrata
	 *
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaUltimaPenaMigrata(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = "";
		lStatement += " SELECT  PR.ID_PENA_RESIDUA, "
				+ " PR.DATA_INIZIO, PR.DATA_FINE_PRESUNTA, PR.DATA_FINE, "
				+ " PR.DATA_FINE_RECLUSIONE, PR.DATA_INIZIO_ARRESTO,"
				+ " PR.NUM_ANNI_RECLUSIONE, PR.NUM_MESI_RECLUSIONE, PR.NUM_GIORNI_RECLUSIONE, "
				+ " PR.IMPORTO_MULTA, " + " PR.NUM_ANNI_ARRESTO, PR.NUM_MESI_ARRESTO, PR.NUM_GIORNI_ARRESTO, "
				+ " PR.IMPORTO_AMMENDA, " + " PR.DIES_A_QUO, "
				+ " PR.COD_OPERATORE_INSERIMENTO, PR.DATA_INSERIMENTO, PR.COD_UFFICIO_INSERIMENTO, "
				+ " PR.COD_OPERATORE_AGGIORNAMENTO, PR.DATA_AGGIORNAMENTO, PR.COD_UFFICIO_AGGIORNAMENTO, "
				+ " PR.EVE_ID_EVENTO, PR.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ " PR.FLAG_VALIDATO, PR.FLAG_ERGASTOLO, PR.FLAG_PENA_SOSPESA, "
				+ " PR.DATA_INIZIO_ISOLAMENTO_DIURNO,PR.DATA_FINE_ISOLAMENTO_DIURNO, "
				+ " PR.NUM_ANNI_ISOLAMENTO_DIURNO, PR.NUM_MESI_ISOLAMENTO_DIURNO, PR.NUM_GIORNI_ISOLAMENTO_DIURNO, "
				+ " PR.MIS_ALT_ID_MISURA_ALTERNATIVA ";
		// " PR.FLAG_SANZIONE_SOSTITUTIVA, "+
		// " PR.COD_TIPO_SANZIONE, "+
		// " PR.NUM_ANNI_SS, "+
		// " PR.NUM_MESI_SS, "+
		// " PR.NUM_GIORNI_SS, "+
		// " PR.IMPORTO_SS, "+
		// " PR.DATA_INIZIO_SS, "+
		// " PR.DATA_FINE_SS ";
		lStatement += "    FROM PENA_RESIDUA PR";
		lStatement += "   WHERE PR.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += "     AND PR.COD_OPERATORE_INSERIMENTO like '%res%' ";
		lStatement += "   ORDER BY PR.DATA_INSERIMENTO DESC ";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" " + lStatement);

		setStatement(lStatement);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PENA_RESIDUA, " + "FAS_SIE_ID_FASCICOLO_SIEP, " + "EVE_ID_EVENTO, "
				+ "MIS_ALT_ID_MISURA_ALTERNATIVA, " +
				// Decorrenza/Scadenza
				"DATA_INIZIO, DATA_FINE, " + "DATA_FINE_PRESUNTA, " + "DATA_FINE_RECLUSIONE,"
				+ "DATA_INIZIO_ARRESTO," + "DIES_A_QUO, " +
				// Quantum/Importi
				"NUM_ANNI_RECLUSIONE, NUM_MESI_RECLUSIONE, NUM_GIORNI_RECLUSIONE, " + "IMPORTO_MULTA, "
				+ "NUM_ANNI_ARRESTO, NUM_MESI_ARRESTO, NUM_GIORNI_ARRESTO, " + "IMPORTO_AMMENDA, " +
				// Ergastolo
				"FLAG_ERGASTOLO, " + "DATA_INIZIO_ISOLAMENTO_DIURNO, DATA_FINE_ISOLAMENTO_DIURNO, "
				+ "NUM_ANNI_ISOLAMENTO_DIURNO, NUM_MESI_ISOLAMENTO_DIURNO, NUM_GIORNI_ISOLAMENTO_DIURNO, " +
				// Sanzione Sostitutiva
				// "FLAG_SANZIONE_SOSTITUTIVA, "+
				// "COD_TIPO_SANZIONE, "+
				// "NUM_ANNI_SS, NUM_MESI_SS, NUM_GIORNI_SS, "+
				// "IMPORTO_SS, "+
				// "DATA_INIZIO_SS, DATA_FINE_SS, "+
				//
				"FLAG_VALIDATO, " + "FLAG_PENA_SOSPESA, "
				+ "COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += " FROM PENA_RESIDUA";
		lStatement += " WHERE ";

		return lStatement;
	}

	/**
	 * 
	 */
	public GenericModel getModel() throws DAOException {
		PenaResiduaModel aModel = new PenaResiduaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdPenaResidua(getBigDecimal("ID_PENA_RESIDUA"));
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setNumAnniReclusione(getBigDecimal("NUM_ANNI_RECLUSIONE"));
		aModel.setNumMesiReclusione(getBigDecimal("NUM_MESI_RECLUSIONE"));
		aModel.setNumGiorniReclusione(getBigDecimal("NUM_GIORNI_RECLUSIONE"));
		aModel.setImportoMulta(getBigDecimal("IMPORTO_MULTA"));
		aModel.setNumAnniArresto(getBigDecimal("NUM_ANNI_ARRESTO"));
		aModel.setNumMesiArresto(getBigDecimal("NUM_MESI_ARRESTO"));
		aModel.setNumGiorniArresto(getBigDecimal("NUM_GIORNI_ARRESTO"));
		aModel.setImportoAmmenda(getBigDecimal("IMPORTO_AMMENDA"));
		aModel.setDiesAQuo(getString("DIES_A_QUO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFlagValidato(getString("FLAG_VALIDATO"));
		aModel.setDataFinePresunta(getDate("DATA_FINE_PRESUNTA"));
		aModel.setDataFineReclusione(getDate("DATA_FINE_RECLUSIONE"));
		aModel.setDataInizioArresto(getDate("DATA_INIZIO_ARRESTO"));
		aModel.setFlagErgastolo(getString("FLAG_ERGASTOLO"));
		aModel.setDataInizioIsolamentoDiurno(getDate("DATA_INIZIO_ISOLAMENTO_DIURNO"));
		aModel.setDataFineIsolamentoDiurno(getDate("DATA_FINE_ISOLAMENTO_DIURNO"));
		aModel.setNumAnniIsolamentoDiurno(getBigDecimal("NUM_ANNI_ISOLAMENTO_DIURNO"));
		aModel.setNumMesiIsolamentoDiurno(getBigDecimal("NUM_MESI_ISOLAMENTO_DIURNO"));
		aModel.setNumGiorniIsolamentoDiurno(getBigDecimal("NUM_GIORNI_ISOLAMENTO_DIURNO"));
		aModel.setMisAltIdMisuraAlternativa(getBigDecimal("MIS_ALT_ID_MISURA_ALTERNATIVA"));
		aModel.setFlagPenaSospesa(getString("FLAG_PENA_SOSPESA"));

		// aModel.setFlagSanzioneSostitutiva (getString ("FLAG_SANZIONE_SOSTITUTIVA") );
		// aModel.setCodTipoSanzione (getString ("COD_TIPO_SANZIONE") );
		// aModel.setNumAnniSS (getBigDecimal ("NUM_ANNI_SS") );
		// aModel.setNumMesiSS (getBigDecimal ("NUM_MESI_SS") );
		// aModel.setNumGiorniSS (getBigDecimal ("NUM_GIORNI_SS") );
		// aModel.setImportoSS (getBigDecimal ("IMPORTO_SS") );
		// aModel.setDataInizioSS (getDate ("DATA_INIZIO_SS") );
		// aModel.setDataFineSS (getDate ("DATA_FINE_SS") );

		return aModel;
	}

	/*****************************************************************************
	 * 
	 * @param aModel
	 * @return
	 */
	public String setCondizione(PenaResiduaModel aModel) {
		String lCondizioni = new String();

		if (aModel.getFasSieIdFascicoloSiep() != null)
			lCondizioni += "  FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " ID_PENA_RESIDUA = " + aKey;
	}

	public String setCondizioneByIdFascicolo(BigDecimal aIdFascicolo) {
		return " FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
	}

	public String setOrderDataInserimentoDesc() {
		String lCondizioni = " ORDER BY DATA_INSERIMENTO DESC ";

		return lCondizioni;
	}

	/*****************************************************************************
	 * Ricerca l'ultimo record PENA_RESIDUA inserito per il fascicolo e se ha FLAG_VALIDATO diverso da S lo
	 * aggiorna, altrimenti inserisce un nuovo record PENA_RESIDUA.
	 * 
	 * @param aPenaResidua
	 *            - model con i dati da inserire/aggiornare
	 * @return model con i dati inseriti/aggiornati
	 */
	public PenaResiduaModel inserisciOModificaPenaResidua(PenaResiduaModel aPenaResidua) throws DAOException {

		PenaResiduaDAO lPenDao = new PenaResiduaDAO(this.mCon);

		// Verifico subito se la pena da inserire deve essere agganciata ad un evento, in
		// questo caso verifico se su quell'evento è già presente una pena. In questo
		// caso la aggiorno.
		// n.b. patch per duplicazione della PR sullo stesso evento in fase di inserimento
		// delle richieste al GE di Indulto. In questo caso l'utente entra ed
		// effettua il calcolo pena che viene agganciato all'evento fittizio.
		// Esce ed effettua altra operazione che genera e valida la pena. In
		// questo caso quando rientra e rieffettua il calcolo la funzione
		// ricercaPenaResiduaCorrenteByFascicoloSiep verificando che l'ultima
		// pena a sistema è validata ne inserisce una nuova ma su un evento che
		// già eveva una pena associata.
		PenaResiduaModel lPenMod = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Verifico se presente PR su evento = " + aPenaResidua.getEveIdEvento());
		if (aPenaResidua.getEveIdEvento() != null) {
			ricercaPenaResiduaByKeyEvento(aPenaResidua.getEveIdEvento());
			lPenMod = (PenaResiduaModel) getModelByKey();
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Pena su evento = " + lPenMod);

		// Se non è presente una pena sull'evento recupero l'ultima pena
		if (lPenMod == null) {
			// Recupero l'ultima pena residua (data ins) indipendentemente dallo stato.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena su evento assente, cerco l'ultima pena");
			ricercaPenaResiduaCorrenteByFascicoloSiep(aPenaResidua.getFasSieIdFascicoloSiep());
			lPenMod = (PenaResiduaModel) getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ultima pena = " + lPenMod);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Pena recuperata = " + lPenMod);
		// se esiste non validata aggiorna
		if (lPenMod != null && (!"S".equals(lPenMod.getFlagValidato()))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Vado in update");
			aPenaResidua.setIdPenaResidua(lPenMod.getIdPenaResidua());

			lPenDao.setDataInserimento(aPenaResidua.getDataInserimento());
			lPenDao.setDAOFromModelForUpdate(aPenaResidua);
			lPenDao.update();
			lPenDao.stop();
		} else { // altrimenti inserisce
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Vado in insert");
			lPenDao.setDAOFromModel(aPenaResidua);
			BigDecimal lKey = lPenDao.insert();
			lPenDao.stop();

			aPenaResidua.setIdPenaResidua(lKey);
		}

		return aPenaResidua;
	}

	/*****************************************************************************
	 * Restituisce l'elenco delle PENE_RESIDUE VALIDATE ordinate per data inserimento decrescente, ma inserite
	 * prima della data passata in input
	 * 
	 * @param aKeyFascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaValidataPerData(BigDecimal aKeyFascicolo, Date aDataIns)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " AND pena_residua.FLAG_VALIDATO = 'S'";
		lSql += " AND pena_residua.DATA_INSERIMENTO < to_date ('"
				+ DateUtils.getDateToString(aDataIns, "dd/MM/yyyy HH:mm:ss") + "','dd/MM/yyyy hh24:mi:ss')";
		lSql += setOrderDataInserimentoDesc();

		setStatement(lSql);
	}

	/**
	 * Ricerca il record pena residua associata all'ultima richiesta al GE di Amnistia/Indulto
	 *
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaResiduaRichestaAmnistiaIndulto(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT PR.ID_PENA_RESIDUA,  PR.DATA_INIZIO, PR.DATA_FINE, "
				+ " PR.NUM_ANNI_RECLUSIONE, PR.NUM_MESI_RECLUSIONE, PR.NUM_GIORNI_RECLUSIONE, "
				+ " PR.IMPORTO_MULTA, " + " PR.NUM_ANNI_ARRESTO, PR.NUM_MESI_ARRESTO, PR.NUM_GIORNI_ARRESTO, "
				+ " PR.IMPORTO_AMMENDA, " + " PR.DIES_A_QUO, "
				+ " PR.COD_OPERATORE_INSERIMENTO, PR.DATA_INSERIMENTO, PR.COD_UFFICIO_INSERIMENTO, "
				+ " PR.COD_OPERATORE_AGGIORNAMENTO, PR.DATA_AGGIORNAMENTO, PR.COD_UFFICIO_AGGIORNAMENTO, "
				+ " PR.EVE_ID_EVENTO, PR.FAS_SIE_ID_FASCICOLO_SIEP, PR.FLAG_VALIDATO, "
				+ " PR.DATA_FINE_PRESUNTA, " + " PR.DATA_FINE_RECLUSIONE," + " PR.DATA_INIZIO_ARRESTO,"
				+ " PR.FLAG_ERGASTOLO, " + " PR.DATA_INIZIO_ISOLAMENTO_DIURNO, "
				+ " PR.DATA_FINE_ISOLAMENTO_DIURNO, " + " PR.NUM_ANNI_ISOLAMENTO_DIURNO, "
				+ " PR.NUM_MESI_ISOLAMENTO_DIURNO, " + " PR.NUM_GIORNI_ISOLAMENTO_DIURNO, "
				+ " PR.MIS_ALT_ID_MISURA_ALTERNATIVA, " + " PR.FLAG_PENA_SOSPESA ";
		// " PR.FLAG_SANZIONE_SOSTITUTIVA, "+
		// " PR.COD_TIPO_SANZIONE, " +
		// " PR.NUM_ANNI_SS, "+
		// " PR.NUM_MESI_SS, "+
		// " PR.NUM_GIORNI_SS, "+
		// " PR.IMPORTO_SS, "+
		// " PR.DATA_INIZIO_SS, "+
		// " PR.DATA_FINE_SS ";
		lStatement += " FROM PENA_RESIDUA PR, EVENTO ";
		lStatement += " WHERE PR.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
		lStatement += " AND PR.EVE_ID_EVENTO = EVENTO.ID_EVENTO ";
		lStatement += " AND evento.cod_tipo_evento = '01' ";
		lStatement += " AND evento.cod_tipo_provvedimento IN ('12', '26', '09') ";
		lStatement += " AND evento.cod_motivo IN ('0298', '0290', '0294', '0367') ";
		lStatement += " ORDER BY EVENTO.DATA_INSERIMENTO DESC ";

		setStatement(lStatement);
	}

}