package siap.sius.udienzaprocedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import siap.dao.SIAPSqlDAO;
import siap.sius.udienza.model.UdienzaMagistratoRelModel;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoUdiModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: UdienzaProcedimentoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella UdienzaProcedimento
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UdienzaProcedimentoSqlDAO extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public UdienzaProcedimentoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaUdienzaProcedimento(UdienzaProcedimentoModel aModel) throws DAOException {
		setStatement(setCondizione(aModel, getSqlQuery()));
	}

	public void ricercaUdienzaProcedimentoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaUdienzaProcedimentoByGeneraleProcedimento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByGeneraleProcedimento(aKey);
		lSql += " " + setOrderByDataInserimento("DESC");
		setStatement(lSql);
	}

	public void ricercaUdienzaProcedimentoByGenProAndFlagRinviata(BigDecimal aKey, String aFilter)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByGeneraleProcedimento(aKey);
		lSql += " " + setCondizioniByAndFlagRinviata(aFilter);
		lSql += " " + setOrderByDataInserimento("DESC");
		setStatement(lSql);
	}

	/**
	 * Metodo che imposta la ricerca Udienza Procedimento, con dati Udienza, per ID Generale Procedimento e
	 * per FlagRinviata.
	 * <p>
	 * 
	 * @param aKey
	 *            ID Generale Procedimento.
	 * @param aFilter
	 *            Flag Rinviata.
	 * @throws DAOException
	 *             Propaga errore di eccezione.
	 */
	public void ricercaUdienzaProcedimentoUdienzaByGenProByFlagRinviata(BigDecimal aKey, String aFilter)
			throws DAOException {
		String lSql = getSqlQueryConUdienza();

		lSql += " " + setCondizioniByGeneraleProcedimento(aKey);
		lSql += " " + setCondizioniByAndFlagRinviata(aFilter);
		lSql += " " + setOrderByDataInserimento("DESC");

		setStatement(lSql);
	}

	public void ricercaUdienzaProcedimentoByEve(BigDecimal aIdEvento) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE EVE_ID_EVENTO = " + aIdEvento;
		setStatement(lSql);
	}

	public void ricercaUdienzaProcedimentoUdienzaByGenProc(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQueryConUdienza();

		lSql += " " + setCondizioniByGeneraleProcedimento(aKey);
		lSql += " " + setOrderByDataInserimento("ASC");
		setStatement(lSql);
	}

	protected String getSqlQueryBase() {
		String lStatement = new String("");

		lStatement += " SELECT " + "UDIENZA_PROCEDIMENTO.ID_UDIENZA_PROCEDIMENTO, "
				+ "UDIENZA_PROCEDIMENTO.FLAG_RINVIATA, " + "UDIENZA_PROCEDIMENTO.COD_OPERATORE_INSERIMENTO, "
				+ "UDIENZA_PROCEDIMENTO.DATA_INSERIMENTO, " + "UDIENZA_PROCEDIMENTO.COD_UFFICIO_INSERIMENTO, "
				+ "UDIENZA_PROCEDIMENTO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "UDIENZA_PROCEDIMENTO.DATA_AGGIORNAMENTO, "
				+ "UDIENZA_PROCEDIMENTO.COD_UFFICIO_AGGIORNAMENTO, "
				+ "UDIENZA_PROCEDIMENTO.GEN_PRID_GENERALE_PROCEDIMENTO, "
				+ "UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA, " + "UDIENZA_PROCEDIMENTO.EVE_ID_EVENTO ";
		return lStatement;
	}

	protected String getSqlQuery() {
		String lStatement = getSqlQueryBase();

		/*
		 * String lStatement = new String("");
		 * 
		 * lStatement += " SELECT " + "ID_UDIENZA_PROCEDIMENTO, " + "FLAG_RINVIATA, " +
		 * "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " +
		 * "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " +
		 * "GEN_PRID_GENERALE_PROCEDIMENTO, " + "UDI_ID_UDIENZA, " + "EVE_ID_EVENTO ";
		 */
		lStatement += " FROM UDIENZA_PROCEDIMENTO";
		return lStatement;
	}

	protected String getSqlQueryConUdienza() {
		String lStatement = getSqlQueryBase();
		lStatement += ", DATA_UDIENZA,";
		lStatement += " NUM_COLLEGIO";
		lStatement += " FROM UDIENZA_PROCEDIMENTO";
		lStatement += " INNER JOIN  UDIENZA ON ID_UDIENZA = UDI_ID_UDIENZA";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		UdienzaProcedimentoModel aModel = new UdienzaProcedimentoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdUdienzaProcedimento(getBigDecimal("ID_UDIENZA_PROCEDIMENTO"));
		aModel.setFlagRinviata(getString("FLAG_RINVIATA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setGenPridGeneraleProcedimento(getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"));
		aModel.setUdiIdUdienza(getBigDecimal("UDI_ID_UDIENZA"));
		aModel.seEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));

		return aModel;
	}

	public GenericModel getModelConUdienza() throws DAOException {
		UdienzaProcedimentoUdiModel aModel = new UdienzaProcedimentoUdiModel(
				(UdienzaProcedimentoModel) getModel());
		aModel.setDataUdienza(getDate("DATA_UDIENZA"));
		aModel.setNumCollegio(getBigDecimal("NUM_COLLEGIO"));
		return aModel;
	}

	public String setCondizioneField(String lFieldValue, String lFieldName, String lSql) {
		String lCondizioneField = new String();

		if (lSql.toUpperCase().indexOf("WHERE") == -1)
			lCondizioneField = lCondizioneField + " WHERE ";
		else
			lCondizioneField = lCondizioneField + " AND ";

		lCondizioneField = lCondizioneField + " AND " + lFieldName + " = '" + lFieldValue + "'";

		return lCondizioneField;
	}

	public String setCondizione(UdienzaProcedimentoModel aModel, String lSql) {
		if (aModel.getGenPridGeneraleProcedimento() != null)
			lSql = lSql + setCondizioneField(aModel.getGenPridGeneraleProcedimento().toString(),
					"GEN_PRID_GENERALE_PROCEDIMENTO", lSql);

		if (aModel.getUdiIdUdienza() != null)
			lSql = lSql + setCondizioneField(aModel.getUdiIdUdienza().toString(), "UDI_ID_UDIENZA", lSql);

		return lSql;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " WHERE ID_UDIENZA_PROCEDIMENTO = " + aKey;
	}

	public String setCondizioniByGeneraleProcedimento(BigDecimal aKey) {
		return " WHERE GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
	}

	public String setCondizioniByAndFlagRinviata(String aKey) {
		return " AND FLAG_RINVIATA IN (" + aKey + ")";
	}

	public String setOrderByDataInserimento(String aKey) {
		return " ORDER BY DATA_INSERIMENTO " + aKey;
	}

	//////
	/**
	 * Funzione di Ricerca Udienze - Magistrato Relatori - Procedimento tra loro associati.
	 * 
	 * @param UdienzaModel
	 *            contenente le condizioni di ricerca
	 * @return ArrayList lista di oggetti UdienzaMagistratoRelModel
	 * @throws DAOException
	 */
	public ArrayList ricercaUdienzeMagistratiProcedimenti(UdienzaModel aModel) throws DAOException {
		// Lista risultati della ricerca
		ArrayList lLista = new ArrayList();
		// Preparazione ed attivazione della Query
		String lSql = getSqlQueryUdiMagProv();
		lSql += setCondizioneUdiMagProv(aModel);
		setStatement(lSql);

		// Valorizzazione fuori ciclo del model relativo alla lettura nel cicol precedente
		UdienzaMagistratoRelModel lModelPrec = new UdienzaMagistratoRelModel();
		lModelPrec.setIdUdienza(new BigDecimal(0));

		// Ciclo di letture
		start();
		while (next()) {
			// Lettura della tupla corrente
			UdienzaMagistratoRelModel lModel = getUdiMagProcModel();

			if (lModel.getIdUdienza().compareTo(lModelPrec.getIdUdienza()) != 0) {
				// Nuova Udienza: si aggiunge alla lista
				lLista.add(lModel);

				// Il Model corrente viene salvato per il confronto
				// nel prossimo ciclo
				lModelPrec = (UdienzaMagistratoRelModel) lLista.get(lLista.size() - 1);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nuova Udienza: " + lLista.size());

			} else if (lModelPrec.getUltimoMagistrato().equals(lModel.getUltimoMagistrato())) {
				// Stessa Udienza Stesso Magistrato: si aggiunge solo il procedimento
				lModelPrec.getUltimoMagistrato()
						.addProcedimento(lModel.getUltimoMagistrato().getUltimoProcedimento());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nuovo Procedimento: " + lLista.size());
			} else {
				// Stessa Udienza Magistrato Diverso: si aggiunge il magistrato
				lModelPrec.addMagistrato(lModel.getUltimoMagistrato());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nuovo Magistrato: " + lLista.size());
			}
		}
		stop();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Udienza-Magistrato-Procedimento letti -> " + lLista.size());

		return lLista;
	}

	/**
	 * Query utilizzata per la ricercaUdienzeMagistratiProcedimenti()
	 * 
	 * @return String
	 */
	private String getSqlQueryUdiMagProv() {
		// Preparazione della select
		String lSql = " SELECT distinct UDIENZA.ID_UDIENZA, UDIENZA.DATA_UDIENZA, UDIENZA.NUM_COLLEGIO ,GENERALE_PROCEDIMENTO.ID_GENERALE_PROCEDIMENTO, ";
		lSql += " FASCICOLO_SIUS.ID_FASCICOLO_SIUS, MAGISTRATO.COGNOME MAG_COGNOME, MAGISTRATO.NOME MAG_NOME, ESPERTO.COGNOME ESP_COGNOME,  ESPERTO.NOME ESP_NOME, MAGISTRATO.COD_MAGISTRATO, ";
		lSql += " ESPERTO.ID_ESPERTO,  UDIENZA_PROCEDIMENTO.FLAG_RINVIATA FROM UDIENZA";
		lSql += " INNER JOIN UDIENZA_PROCEDIMENTO ON UDIENZA_PROCEDIMENTO.UDI_ID_UDIENZA = UDIENZA.ID_UDIENZA AND UDIENZA_PROCEDIMENTO.FLAG_RINVIATA IN ('S', 'F', 'P')";
		lSql += " INNER  JOIN GENERALE_PROCEDIMENTO ON GENERALE_PROCEDIMENTO.ID_GENERALE_PROCEDIMENTO = UDIENZA_PROCEDIMENTO.GEN_PRID_GENERALE_PROCEDIMENTO";
		lSql += " INNER JOIN FASCICOLO_SIUS ON FASCICOLO_SIUS.ID_FASCICOLO_SIUS = GENERALE_PROCEDIMENTO.FAS_SIU_ID_FASCICOLO_SIUS";
		lSql += " LEFT OUTER JOIN MAGISTRATO_RELATORE ON MAGISTRATO_RELATORE.FAS_SIU_ID_FASCICOLO_SIUS=  FASCICOLO_SIUS.ID_FASCICOLO_SIUS ";
		lSql += " AND MAGISTRATO_RELATORE.DATA_FINE IS NULL ";
		lSql += " LEFT OUTER JOIN MAGISTRATO ON MAGISTRATO_RELATORE.MAG_COD_MAGISTRATO=MAGISTRATO.COD_MAGISTRATO ";
		lSql += " LEFT OUTER JOIN ESPERTO ON MAGISTRATO_RELATORE.ESP_ID_ESPERTO=ESPERTO.ID_ESPERTO ";
		return lSql;
	}

	/**
	 * Setta nello statement della Query per la ricerca di Udienze -Magistrati-Procedimenti le condizioni di
	 * ricerca
	 * 
	 * @param UdienzaModel
	 * @return
	 */
	private String setCondizioneUdiMagProv(UdienzaModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;

		if (aModel.getNumCollegio() != null) {
			lCondizioni += " UDIENZA.NUM_COLLEGIO = " + aModel.getNumCollegio();
			lInserito = true;
		}
		if (aModel.getCodUfficioAppartenenza() != null) {
			if (lInserito)
				lCondizioni += "AND ";
			lCondizioni += " UDIENZA.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() + "'";
			lInserito = true;
		}
		if (aModel.getDataUdienza() != null) {
			if (lInserito)
				lCondizioni += "AND ";
			lCondizioni += " UDIENZA.DATA_UDIENZA >= TO_DATE("
					+ DateUtils.getDateToString(aModel.getDataUdienza(), "yyyyMMdd") + ",'YYYYMMDD' )";
			lInserito = true;
		}
		if (aModel.getDataUdienzaFine() != null) {
			if (lInserito)
				lCondizioni += "AND ";
			lCondizioni += " UDIENZA.DATA_UDIENZA <= TO_DATE("
					+ DateUtils.getDateToString(aModel.getDataUdienzaFine(), "yyyyMMdd") + ",'YYYYMMDD' )";
			lInserito = true;
		}
		if (lInserito)
			lCondizioni = "WHERE " + lCondizioni;

		lCondizioni += " order by UDIENZA.DATA_UDIENZA, UDIENZA.NUM_COLLEGIO, MAGISTRATO.COD_MAGISTRATO, ESPERTO.ID_ESPERTO ";

		return lCondizioni;
	}

	/**
	 * Valorizza il model UdienzaMagistratoRelModel con i valori restituiti dalla fatch della Query
	 * appropriata.
	 * 
	 * @return UdienzaMagistratoRelModel
	 * @throws DAOException
	 */
	private UdienzaMagistratoRelModel getUdiMagProcModel() throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("getUdiMagProcModel():inizio ");

		UdienzaMagistratoRelModel lModel = new UdienzaMagistratoRelModel();
		lModel.addMagistrato();
		lModel.getUltimoMagistrato().addProcedimento();

		// Lettura dei campi relativvi all'Udienza
		lModel.setIdUdienza(getBigDecimal("ID_UDIENZA"));
		lModel.setDataUdienza(getDate("DATA_UDIENZA"));
		lModel.setNumCollegio(getBigDecimal("NUM_COLLEGIO"));
		// Lettura dei campi relativi al Magistrato Relatore
		lModel.getUltimoMagistrato().setCodMagistrato(getString("COD_MAGISTRATO"));
		lModel.getUltimoMagistrato().setIdEsperto(getBigDecimal("ID_ESPERTO"));
		if (lModel.getUltimoMagistrato().getCodMagistrato() != null
				&& lModel.getUltimoMagistrato().getCodMagistrato().length() > 0) {
			lModel.getUltimoMagistrato().setCognome(getString("MAG_COGNOME"));
			lModel.getUltimoMagistrato().setNome(getString("MAG_NOME"));

		} else if (lModel.getUltimoMagistrato().getIdEsperto() != null) {
			lModel.getUltimoMagistrato().setCognome(getString("ESP_COGNOME"));
			lModel.getUltimoMagistrato().setNome(getString("ESP_NOME"));
		}

		// Lettura dei campi relativi al Procedimento
		lModel.getUltimoMagistrato().getUltimoProcedimento()
				.setIdFascicolo(getBigDecimal("ID_FASCICOLO_SIUS"));
		lModel.getUltimoMagistrato().getUltimoProcedimento().setFlagRinvio(getString("FLAG_RINVIATA"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("getUdiMagProcModel():fine ");

		return lModel;
	}

}