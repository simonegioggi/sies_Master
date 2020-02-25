package siap.sige.udienzaprocedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

//import f3b.util.DateUtils;
import siap.dao.SIAPSqlDAO;
import siap.sige.udienza.model.UdienzaSigeMagistratoAssModel;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.model.UdienzaProcSigeUdienzaModel;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
//import siap.sius.udienza.model.UdienzaMagistratoRelModel;
//import siap.sius.udienza.model.UdienzaModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: UdienzaProcedimentoSigeSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella UdienzaProcedimentoSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UdienzaProcedimentoSigeSqlDAO extends SIAPSqlDAO {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public UdienzaProcedimentoSigeSqlDAO(Connection con) {

		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaUdienzaProcedimentoSige(UdienzaProcedimentoSigeModel aModel) throws DAOException {

		setStatement(setCondizione(aModel, getSqlQuery()));
	}

	public void ricercaUdienzaProcedimentoByKey(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaUdienzaProcedimentoByIdFascicoloSige(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicoloSige(aKey);
		lSql += " " + setOrderByDataInserimento("DESC");
		setStatement(lSql);
	}

	public void ricercaUdienzaProcedimentoByFasSigeAndFlagRinviata(BigDecimal aKey, String aFilter)
			throws DAOException {

		String lSql = getSqlQueryConUdienza();

		lSql += " " + setCondizioniByIdFascicoloSige(aKey);
		lSql += " " + setCondizioniByAndFlagRinviata(aFilter);
		lSql += " " + setOrderByDataInserimento("DESC");
		setStatement(lSql);
	}

	/**
	 * Metodo che imposta la ricerca Udienza Procedimento, con dati Udienza, per ID Fascicolo e per
	 * FlagRinviata.
	 * <p>
	 * 
	 * @param aKey
	 *            ID Generale Procedimento.
	 * @param aFilter
	 *            Flag Rinviata.
	 * @throws DAOException
	 *             Propaga errore di eccezione.
	 */
	public void ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(BigDecimal aKey, String aFilter)
			throws DAOException {

		ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(aKey, aFilter, true);
	}

	public void ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(BigDecimal aKey, String aFilter,
			boolean complete) throws DAOException {

		String lSql = getSqlQueryConUdienza(complete);

		lSql += " " + setCondizioniByIdFascicoloSige(aKey);
		lSql += " " + setCondizioniByAndFlagRinviata(aFilter);
		lSql += " " + setOrderByAndFlagRinviata("DESC");

		setStatement(lSql);
	}

	public void ricercaUdienzaProcedimentoByEve(BigDecimal aIdEvento) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " WHERE EVE_ID_EVENTO = " + aIdEvento;
		setStatement(lSql);
	}

	public void ricercaUdienzaProcedimentoUdienzaByIdFascicoloSige(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQueryConUdienza();

		lSql += " " + setCondizioniByIdFascicoloSige(aKey);
		lSql += " " + setOrderByDataInserimento("ASC");
		setStatement(lSql);
	}

	protected String getSqlQueryBase() {
		String lStatement = new String("");

		lStatement += " SELECT * ";
		return lStatement;
	}

	protected String getSqlQuery() {
		String lStatement = getSqlQueryBase();

		lStatement += " FROM UDIENZA_PROCEDIMENTO_SIGE";
		return lStatement;
	}

	protected String getSqlQueryConUdienza() {
		return getSqlQueryConUdienza(true);
	}

	protected String getSqlQueryConUdienza(boolean complete) {
		String lStatement = "";

		lStatement += " SELECT " + "ID_UDIENZA_PROCEDIMENTO_SIGE, " + "FLAG_RINVIATA, "
				+ "UP.COD_OPERATORE_INSERIMENTO, " + "UP.DATA_INSERIMENTO, " + "UP.COD_UFFICIO_INSERIMENTO, "
				+ "UP.COD_OPERATORE_AGGIORNAMENTO, " + "UP.DATA_AGGIORNAMENTO, "
				+ "UP.COD_UFFICIO_AGGIORNAMENTO, " + "FAS_ID_FASCICOLO_SIGE, " + "UDI_ID_UDIENZA_SIGE, "
				+ "UDI_ID_UDIENZA_RINVIO, " + "EVE_ID_EVENTO, " + "UDIENZA_SIGE.DATA_UDIENZA ";

		lStatement += " FROM UDIENZA_PROCEDIMENTO_SIGE UP ";
		// 20170921: [SG] vado sempre in outer join poichè possiamo iscrivere un decreto di fissazione udienza
		// senza udienza!
		// if (complete) {
		// lStatement += " INNER JOIN UDIENZA_SIGE ON ID_UDIENZA_SIGE = UDI_ID_UDIENZA_SIGE";
		// } else {
		lStatement += " LEFT OUTER JOIN UDIENZA_SIGE ON ID_UDIENZA_SIGE = UDI_ID_UDIENZA_SIGE";
		// }

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		UdienzaProcedimentoSigeModel aModel = new UdienzaProcedimentoSigeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdUdienzaProcedimentoSige(getBigDecimal("ID_UDIENZA_PROCEDIMENTO_SIGE"));
		aModel.setFlagRinviata(getString("FLAG_RINVIATA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasIdFascicoloSige(getBigDecimal("FAS_ID_FASCICOLO_SIGE"));
		aModel.setUdiIdUdienzaRinvio(getBigDecimal("UDI_ID_UDIENZA_RINVIO"));
		aModel.setUdiIdUdienzaSige(getBigDecimal("UDI_ID_UDIENZA_SIGE"));
		// aModel.setDataUdienzaSige(getDate("DATA_UDIENZA_SIGE") );
		aModel.seEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));

		return aModel;
	}

	public GenericModel getModelConDataUdienza() throws DAOException {
		UdienzaProcedimentoSigeModel aModel = (UdienzaProcedimentoSigeModel) this.getModel();
		aModel.setDataUdienzaSige(getDate("DATA_UDIENZA"));
		return aModel;
	}

	public GenericModel getModelConUdienza() throws DAOException {
		UdienzaProcSigeUdienzaModel aModel = new UdienzaProcSigeUdienzaModel(
				(UdienzaProcedimentoSigeModel) getModel());
		aModel.setDataUdienza(getDate("DATA_UDIENZA"));
		// Commentato poichè non sembra essere utilizzato
		// aModel.setNumCollegio(getBigDecimal("NUM_COLLEGIO") );
		return aModel;
	}

	public String setCondizioneField(String lFieldValue, String lFieldName, String lSql) {
		String lCondizioneField = new String();

		if (lSql.toUpperCase().indexOf("WHERE") == -1)
			lCondizioneField = lCondizioneField + " WHERE ";
		else
			lCondizioneField = lCondizioneField + " AND ";

		lCondizioneField = lCondizioneField + lFieldName + " = '" + lFieldValue + "'";

		return lCondizioneField;
	}

	public String setCondizione(UdienzaProcedimentoSigeModel aModel, String lSql) {
		if (aModel.getFasIdFascicoloSige() != null)
			lSql = lSql
					+ setCondizioneField(aModel.getFasIdFascicoloSige().toString(), "FAS_ID_FASCICOLO_SIGE",
							lSql);

		if (aModel.getUdiIdUdienzaSige() != null)
			lSql = lSql
					+ setCondizioneField(aModel.getUdiIdUdienzaSige().toString(), "UDI_ID_UDIENZA_SIGE", lSql);

		if (aModel.getFlagRinviata() != null && aModel.getFlagRinviata().length() == 1)
			lSql = lSql + setCondizioneField(aModel.getFlagRinviata(), "FLAG_RINVIATA", lSql);

		return lSql;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " WHERE ID_UDIENZA_PROCEDIMENTO_SIGE = " + aKey;
	}

	public String setCondizioniByIdFascicoloSige(BigDecimal aKey) {
		return " WHERE FAS_ID_FASCICOLO_SIGE = " + aKey;
	}

	public String setCondizioniByAndFlagRinviata(String aKey) {
		return " AND FLAG_RINVIATA IN (" + aKey + ")";
	}

	public String setOrderByDataInserimento(String aKey) {
		return " ORDER BY UDIENZA_PROCEDIMENTO_SIGE.DATA_INSERIMENTO " + aKey;
	}

	public String setOrderByAndFlagRinviata(String aKey) {
		return " ORDER BY UP.DATA_INSERIMENTO " + aKey;
	}

	/**
	 * Funzione di Ricerca Udienze - Magistrato Relatori - Procedimento tra loro associati.
	 * 
	 * @param UdienzaModel
	 *            contenente le condizioni di ricerca
	 * @return ArrayList lista di oggetti UdienzaSigeMagistratoAssModel
	 * @throws DAOException
	 */

	public ArrayList ricercaUdienzeMagistratiProcedimenti(UdienzaSigeModel aModel) throws DAOException {
		// Lista risultati della ricerca
		ArrayList lLista = new ArrayList();
		// Preparazione ed attivazione della Query
		String lSql = getSqlQueryUdiMagProv();
		// lSql += setCondizioneUdiMagProv( aModel);
		setStatement(lSql);

		// Valorizzazione fuori ciclo del model relativo alla lettura nel ciclo precedente
		UdienzaSigeMagistratoAssModel lModelPrec = new UdienzaSigeMagistratoAssModel();
		lModelPrec.setIdUdienza(new BigDecimal(0));

		// Ciclo di letture
		start();
		while (next()) {
			// Lettura della tupla corrente
			UdienzaSigeMagistratoAssModel lModel = getUdiMagProcModel();

			if (lModel.getIdUdienza().compareTo(lModelPrec.getIdUdienza()) != 0) {
				// Nuova Udienza: si aggiunge alla lista
				lLista.add(lModel);

				// Il Model corrente viene salvato per il confronto
				// nel prossimo ciclo
				lModelPrec = (UdienzaSigeMagistratoAssModel) lLista.get(lLista.size() - 1);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nuova Udienza: " + lLista.size());
			} else if (lModelPrec.getUltimoMagistrato().equals(lModel.getUltimoMagistrato())) {
				// Stessa Udienza Stesso Magistrato: si aggiunge solo il procedimento
				lModelPrec.getUltimoMagistrato().addProcedimento(
						lModel.getUltimoMagistrato().getUltimoProcedimento());
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
		String lSql = " SELECT distinct UDIENZA_SIGE.ID_UDIENZA, "
				+ " UDIENZA_SIGE.DATA_UDIENZA, UDIENZA_SIGE.NUM_COLLEGIO ,FASCICOLO_SIGE.ID_FASCICOLO_SIGE, ";
		lSql += " MAGISTRATO.COGNOME MAG_COGNOME, MAGISTRATO.NOME MAG_NOME, MAGISTRATO.COD_MAGISTRATO, ";
		lSql += " UDIENZA_PROCEDIMENTO_SIGE.FLAG_RINVIATA FROM UDIENZA_SIGE";
		lSql += " INNER JOIN UDIENZA_PROCEDIMENTO_SIGE ON UDIENZA_PROCEDIMENTO_SIGE.UDI_ID_UDIENZA_SIGE = UDIENZA_SIGE.ID_UDIENZA_SIGE "
				+ " AND UDIENZA_PROCEDIMENTO_SIGE.FLAG_RINVIATA IN ('S', 'F')";
		lSql += " INNER JOIN GENERALE_PROCEDIMENTO ON GENERALE_PROCEDIMENTO.ID_GENERALE_PROCEDIMENTO = UDIENZA_PROCEDIMENTO.GEN_PRID_GENERALE_PROCEDIMENTO";
		lSql += " INNER JOIN FASCICOLO_SIGE ON FASCICOLO_SIGE.ID_FASCICOLO_SIGE = UDIENZA_PROCEDIMENTO_SIGE.FAS_ID_FASCICOLO_SIGE";
		lSql += " LEFT OUTER JOIN MAGISTRATO_ASSEGNATARIO ON MAGISTRATO_ASSEGNATARIO.FAS_SIGE_ID_FASCICOLO_SIGE = FASCICOLO_SIGE.ID_FASCICOLO_SIGE ";
		lSql += " AND MAGISTRATO_ASSEGNATARIO.DATA_FINE IS NULL ";
		lSql += " LEFT OUTER JOIN MAGISTRATO ON MAGISTRATO_ASSEGNATARIO.MAG_COD_MAGISTRATO=MAGISTRATO.COD_MAGISTRATO ";

		return lSql;
	}

	/**
	 * Setta nello statement della Query per la ricerca di Udienze -Magistrati-Procedimenti le condizioni di
	 * ricerca
	 * 
	 * @param UdienzaModel
	 * @return
	 */
	// private String setCondizioneUdiMagProv(UdienzaSigeModel aModel) {
	// String lCondizioni = new String();
	//
	// boolean lInserito = false;
	//
	// if (aModel.getColIdCollegio() != null) {
	// lCondizioni += " UDIENZA_SIGE.COL_ID_COLLEGIO = " + aModel.getColIdCollegio();
	// lInserito = true;
	// }
	//
	// if (aModel.getCodUfficioAppartenenza() != null) {
	// if (lInserito)
	// lCondizioni += "AND ";
	// lCondizioni += " UDIENZA_SIGE.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza()
	// + "'";
	// lInserito = true;
	// }
	//
	// if (aModel.getDateUdienze() != null) {
	// if (lInserito && aModel.getDateUdienze().length > 0 && aModel.getDateUdienze()[0] != null)
	// lCondizioni += " AND UDIENZA_SIGE.DATA_UDIENZA  >= " + " TO_DATE('"
	// + DateUtils.getDateToString(aModel.getDateUdienze()[0], "dd/MM/yyyy")
	// + "', 'DD/MM/YYYY')";
	//
	// if (lInserito && aModel.getDateUdienze().length > 1 && aModel.getDateUdienze()[1] != null)
	// lCondizioni += " AND UDIENZA_SIGE.DATA_UDIENZA  <= " + " TO_DATE('"
	// + DateUtils.getDateToString(aModel.getDateUdienze()[1], "dd/MM/yyyy")
	// + "', 'DD/MM/YYYY')";
	//
	// lInserito = true;
	// }
	//
	// if (lInserito)
	// lCondizioni = "WHERE " + lCondizioni;
	//
	// lCondizioni +=
	// " order by UDIENZA_SIGE.DATA_UDIENZA, UDIENZA_SIGE.NUM_COLLEGIO, MAGISTRATO.COD_MAGISTRATO ";
	//
	// return lCondizioni;
	// }

	/**
	 * Funzione di Ricerca Udienze - Magistrato Relatori - Procedimento tra loro associati.
	 * 
	 * @param UdienzaModel
	 *            contenente le condizioni di ricerca
	 * @return ArrayList lista di oggetti UdienzaMagistratoRelModel
	 * @throws DAOException
	 */
	/*
	 * public ArrayList ricercaUdienzeMagistratiProcedimenti( UdienzaSigeModel aModel) throws DAOException {
	 * // Lista risultati della ricerca ArrayList lLista = new ArrayList(); // Preparazione ed attivazione
	 * della Query String lSql = getSqlQueryUdiMagProv(); lSql += setCondizioneUdiMagProv( aModel);
	 * setStatement(lSql);
	 * 
	 * // Valorizzazione fuori ciclo del model relativo alla lettura nel cicol precedente
	 * UdienzaMagistratoRelModel lModelPrec = new UdienzaMagistratoRelModel(); lModelPrec.setIdUdienza(new
	 * BigDecimal(0));
	 * 
	 * // Ciclo di letture start(); while(next()) { // Lettura della tupla corrente UdienzaMagistratoRelModel
	 * lModel = getUdiMagProcModel();
	 * 
	 * if (lModel.getIdUdienza().compareTo(lModelPrec.getIdUdienza()) != 0) { // Nuova Udienza: si aggiunge
	 * alla lista lLista.add(lModel);
	 * 
	 * // Il Model corrente viene salvato per il confronto // nel prossimo ciclo lModelPrec = (// [FT] -
	 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 * (UdienzaMagistratoRelModel)lLista.get(lLista.size() - 1) ; siesLogger.debug("Nuova Udienza: " +
	 * lLista.size());
	 * 
	 * } else if (lModelPrec.getUltimoMagistrato().equals(lModel.getUltimoMagistrato())) { // Stessa Udienza
	 * Stesso Magistrato: si aggiunge solo il procedimento
	 * lModelPrec.getUltimoMagistrato().addProcedimento(lModel.getUltimoMagistrato().getUltimoProcedimento());
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.debug("Nuovo Procedimento: " + lLista.size()); } else { // Stessa Udienza
	 * Magistrato Diverso: si aggiunge il magistrato lModelPrec.addMagistrato(lModel.getUltimoMagistrato());
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.debug("Nuovo Magistrato: " + lLista.size()); } } stop(); // [FT] -
	 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 * siesLogger.debug("Udienza-Magistrato-Procedimento letti -> "+ lLista.size());
	 * 
	 * return lLista; }
	 */
	/**
	 * Valorizza il model UdienzaMagistratoAssModel con i valori restituiti dalla fatch della Query
	 * appropriata.
	 * 
	 * @return UdienzaMagistratoAssModel
	 * @throws DAOException
	 */
	private UdienzaSigeMagistratoAssModel getUdiMagProcModel() throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("getUdiMagProcModel():inizio ");

		UdienzaSigeMagistratoAssModel lModel = new UdienzaSigeMagistratoAssModel();
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

	public void ricercaUdienzaProcedimentoByIdUdienzaSige(BigDecimal aIdUdienzaSige) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE UDI_ID_UDIENZA_SIGE = " + aIdUdienzaSige;
		setStatement(lSql);
	}

	public UdienzaProcedimentoSigeModel ricercaUdienzaProcedimentoByIdUdienza(BigDecimal idUdienza)
			throws DAOException {
		this.ricercaUdienzaProcedimentoByIdUdienzaSige(idUdienza);
		super.start();
		UdienzaProcedimentoSigeModel model = null;
		if (super.next())
			model = (UdienzaProcedimentoSigeModel) this.getModel();

		super.stop();
		return model;
	}

	public BigDecimal countUdienzeByIdFascicolo(BigDecimal idFascicolo) throws DAOException {

		String sql = "select count (*) as numUdienze from UDIENZA_PROCEDIMENTO_SIGE where FLAG_RINVIATA not in ('A','R') and "
				+ "FAS_ID_FASCICOLO_SIGE=" + idFascicolo;

		super.setStatement(sql);
		super.start();

		super.next();
		BigDecimal numDec = getBigDecimal("numUdienze");
		super.stop();

		return numDec;

	}

	/**
	 * MERGE v10: aggiunto metodo di controllo
	 * 
	 * @param idUdienza
	 * @return
	 * @throws DAOException
	 */
	public BigDecimal contaUdienzeProcedimentoSigeByIdUdienza(BigDecimal idUdienza) throws DAOException {

		String sql = "select count(*) as numUdienze" + "  from udienza_sige              u,"
				+ "       udienza_procedimento_sige g," + "       evento                    e,"
				+ "       provvedimento_sige        s" + " where u.id_udienza_sige = " + idUdienza
				+ "   and u.id_udienza_sige = g.udi_id_udienza_sige" + "   and g.eve_id_evento = e.id_evento"
				+ "   and e.id_evento = s.id_evento_generato";

		// sequenza di istruzione per eseguire la query
		setStatement(sql);
		start();
		next();
		// ricavo il valore della conta
		BigDecimal numUdienze = getBigDecimal("numUdienze");
		stop();

		// valore di ritorno
		return numUdienze;
	}

}