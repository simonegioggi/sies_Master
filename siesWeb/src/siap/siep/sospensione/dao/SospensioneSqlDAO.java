package siap.siep.sospensione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.sospensione.model.SospensioneModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: SospensioneSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Sospensione
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
public class SospensioneSqlDAO extends SqlDAO {

	/**
	 * Costruttore
	 * 
	 * @param con
	 */
	public SospensioneSqlDAO(Connection con) {
		super(con);
	}

	public void ricercaSospensione(SospensioneModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	/**
	 * Ricerca una sospensione per chiave
	 * 
	 * @param aIdSospensione
	 * @throws DAOException
	 */
	public void ricercaSospensioneByKey(BigDecimal aIdSospensione) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aIdSospensione);
		setStatement(lSql);
	}

	/**
	 * Ricerca tutte le sospensioni legate a un fascicolo
	 * 
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	// STUB 24/10/2005 REWORK STATO ESECUZIONE.
	public void ricercaSospensioneByFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aIdFascicolo);
		setStatement(lSql);
	}

	/**
	 * Ricerca tutte le sospensioni legate a una certa pena residua
	 * 
	 * @param aIdPenaResidua
	 * @throws DAOException
	 */
	public void ricercaSospensioneByIdPenaResidua(BigDecimal aIdPenaResidua) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " " + setCondizioniByIdPenaResidua(aIdPenaResidua);

		setStatement(lStatement);
	}

	/**
	 * Verifica se esistono delle SOSPENSIONI legate all'evento passato in input avente i campi
	 * PENA_RESIDUA_xxx valorizzati. Infatti solo in questo caso viene effettuato un ricalcolo della pena e la
	 * sospensione può essere considerata una Pena Iniziale n.b. la SOSPENSIONE è legata sempre a una pena
	 * residua per cui viene verificato se esiste una pena residua a sua volta collegata all'evento n.b.
	 * utilizzata anche nel caso delle espulsioni EVENTO<--PENA_RESIDUA<--SOSPENSIONE
	 * 
	 * @param aIdEvento
	 * @throws DAOException
	 */
	public void ricercaSospensionePerPenaIniziale(BigDecimal aIdEvento) throws DAOException {
		String lStatement = "";
		lStatement += " SELECT "
				+ " SOSP.ID_SOSPENSIONE, "
				+ " SOSP.DATA_INIZIO, SOSP.DATA_FINE, "
				+ " SOSP.NUM_ANNI_RINVIO, SOSP.NUM_MESI_RINVIO, SOSP.NUM_GIORNI_RINVIO, "
				+ " SOSP.COD_OPERATORE_INSERIMENTO, SOSP.DATA_INSERIMENTO, SOSP.COD_UFFICIO_INSERIMENTO, "
				+ " SOSP.COD_OPERATORE_AGGIORNAMENTO, SOSP.DATA_AGGIORNAMENTO, SOSP.COD_UFFICIO_AGGIORNAMENTO, "
				+ " SOSP.PEN_RES_ID_PENA_RESIDUA, "
				+ " SOSP.NUM_ANNI_PENA_ESPIATA, SOSP.NUM_MESI_PENA_ESPIATA, SOSP.NUM_GIORNI_PENA_ESPIATA, "
				+ " SOSP.NUM_ANNI_PENA_RESIDUA_RECLUS, SOSP.NUM_MESI_PENA_RESIDUA_RECLUS, SOSP.NUM_GIORNI_PENA_RESIDUA_RECLUS, "
				+ " SOSP.NUM_ANNI_PENA_RESIDUA_ARRES, SOSP.NUM_MESI_PENA_RESIDUA_ARRES, SOSP.NUM_GIORNI_PENA_RESIDUA_ARRES, "
				+ " SOSP.NUM_ANNI_INTERRUZIONE, SOSP.NUM_MESI_INTERRUZIONE, SOSP.NUM_GIORNI_INTERRUZIONE, "
				+ " SOSP.MULTA_ESPIATA, SOSP.AMMENDA_ESPIATA, "
				+ " SOSP.MULTA_RESIDUA, SOSP.AMMENDA_RESIDUA, " + " SOSP.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ " SOSP.FLAG_INTERRUZIONE, SOSP.NUM_GIORNI_LIBANTICIPATA ";
		lStatement += " FROM sospensione SOSP, pena_residua ";
		lStatement += " WHERE SOSP.PEN_RES_ID_PENA_RESIDUA = pena_residua.id_pena_residua ";
		lStatement += " AND pena_residua.EVE_ID_EVENTO = " + aIdEvento;
		lStatement += " AND (   (SOSP.NUM_ANNI_PENA_RESIDUA_RECLUS   is not null and SOSP.NUM_ANNI_PENA_RESIDUA_RECLUS<>0) ";
		lStatement += "      or (SOSP.NUM_MESI_PENA_RESIDUA_RECLUS   is not null and SOSP.NUM_MESI_PENA_RESIDUA_RECLUS <>0) ";
		lStatement += "      or (SOSP.NUM_GIORNI_PENA_RESIDUA_RECLUS is not null and SOSP.NUM_GIORNI_PENA_RESIDUA_RECLUS<>0) ";
		lStatement += "      or (SOSP.NUM_ANNI_PENA_RESIDUA_ARRES    is not null and SOSP.NUM_ANNI_PENA_RESIDUA_ARRES<>0) ";
		lStatement += "      or (SOSP.NUM_MESI_PENA_RESIDUA_ARRES    is not null and SOSP.NUM_MESI_PENA_RESIDUA_ARRES<>0) ";
		lStatement += "      or (SOSP.NUM_GIORNI_PENA_RESIDUA_ARRES  is not null and SOSP.NUM_GIORNI_PENA_RESIDUA_ARRES<>0) ";
		lStatement += "     ) ";

		setStatement(lStatement);
	}

	/**
	 * Verifica se esistono delle SOSPENSIONI legate all'evento passato in input avente i campi
	 * NUM_xxx_PENA_ESPIATA valorizzati. Infatti solo in questo caso viene effettuato un ricalcolo della pena
	 * e l'Interruzione può essere considerata una Pena Iniziale<br>
	 * n.b. la SOSPENSIONE è legata sempre a una pena residua per cui viene verificato se esiste una pena
	 * residua a sua volta collegata all'evento
	 *
	 * EVENTO<--PENA_RESIDUA<--SOSPENSIONE
	 * 
	 * @param aIdEvento
	 * @throws DAOException
	 */
	public void ricercaInterruzionePerPenaIniziale(BigDecimal aIdEvento) throws DAOException {
		String lStatement = "";
		lStatement += " SELECT "
				+ " SOSP.ID_SOSPENSIONE, "
				+ " SOSP.DATA_INIZIO, SOSP.DATA_FINE, "
				+ " SOSP.NUM_ANNI_RINVIO, SOSP.NUM_MESI_RINVIO, SOSP.NUM_GIORNI_RINVIO, "
				+ " SOSP.COD_OPERATORE_INSERIMENTO, SOSP.DATA_INSERIMENTO, SOSP.COD_UFFICIO_INSERIMENTO, "
				+ " SOSP.COD_OPERATORE_AGGIORNAMENTO, SOSP.DATA_AGGIORNAMENTO, SOSP.COD_UFFICIO_AGGIORNAMENTO, "
				+ " SOSP.PEN_RES_ID_PENA_RESIDUA, "
				+ " SOSP.NUM_ANNI_PENA_ESPIATA, SOSP.NUM_MESI_PENA_ESPIATA, SOSP.NUM_GIORNI_PENA_ESPIATA, "
				+ " SOSP.NUM_ANNI_PENA_RESIDUA_RECLUS, SOSP.NUM_MESI_PENA_RESIDUA_RECLUS, SOSP.NUM_GIORNI_PENA_RESIDUA_RECLUS, "
				+ " SOSP.NUM_ANNI_PENA_RESIDUA_ARRES, SOSP.NUM_MESI_PENA_RESIDUA_ARRES, SOSP.NUM_GIORNI_PENA_RESIDUA_ARRES, "
				+ " SOSP.NUM_ANNI_INTERRUZIONE, SOSP.NUM_MESI_INTERRUZIONE, SOSP.NUM_GIORNI_INTERRUZIONE, "
				+ " SOSP.MULTA_ESPIATA, SOSP.AMMENDA_ESPIATA, "
				+ " SOSP.MULTA_RESIDUA, SOSP.AMMENDA_RESIDUA, " + " SOSP.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ " SOSP.FLAG_INTERRUZIONE, SOSP.NUM_GIORNI_LIBANTICIPATA ";
		lStatement += " FROM sospensione SOSP, pena_residua ";
		lStatement += " WHERE SOSP.PEN_RES_ID_PENA_RESIDUA = pena_residua.id_pena_residua ";
		lStatement += " AND pena_residua.EVE_ID_EVENTO = " + aIdEvento;
		lStatement += " AND (   (SOSP.NUM_ANNI_PENA_ESPIATA   is not null and SOSP.NUM_ANNI_PENA_ESPIATA<>0) ";
		lStatement += "      or (SOSP.NUM_MESI_PENA_ESPIATA   is not null and SOSP.NUM_MESI_PENA_ESPIATA <>0) ";
		lStatement += "      or (SOSP.NUM_GIORNI_PENA_ESPIATA is not null and SOSP.NUM_GIORNI_PENA_ESPIATA<>0) ";
		lStatement += "     ) ";

		setStatement(lStatement);
	}

	/**
	 * Ricerca tutte le sospensioni legate al fascicolo specificato e VALIDATE nel periodo passato in input.
	 * Utilizzato per il calcolo della pena già espiata. I record vengono restituiti per DATA_INIZIO
	 * decrescente. Dalla sospensione più recente
	 * 
	 * @param aIdFascicolo
	 * @param aDataDal
	 * @param aDataAl
	 */
	public void ricercaSospensioniValidatePerIntervallo(BigDecimal aIdFascicolo, Date aDataDal, Date aDataAl) {
		String lStatement = "";
		lStatement += " SELECT "
				+ " SOSP.ID_SOSPENSIONE, "
				+ " SOSP.DATA_INIZIO, SOSP.DATA_FINE, "
				+ " SOSP.NUM_ANNI_RINVIO, SOSP.NUM_MESI_RINVIO, SOSP.NUM_GIORNI_RINVIO, "
				+ " SOSP.COD_OPERATORE_INSERIMENTO, SOSP.DATA_INSERIMENTO, SOSP.COD_UFFICIO_INSERIMENTO, "
				+ " SOSP.COD_OPERATORE_AGGIORNAMENTO, SOSP.DATA_AGGIORNAMENTO, SOSP.COD_UFFICIO_AGGIORNAMENTO, "
				+ " SOSP.PEN_RES_ID_PENA_RESIDUA, "
				+ " SOSP.NUM_ANNI_PENA_ESPIATA, SOSP.NUM_MESI_PENA_ESPIATA, SOSP.NUM_GIORNI_PENA_ESPIATA, "
				+ " SOSP.NUM_ANNI_PENA_RESIDUA_RECLUS, SOSP.NUM_MESI_PENA_RESIDUA_RECLUS, SOSP.NUM_GIORNI_PENA_RESIDUA_RECLUS, "
				+ " SOSP.NUM_ANNI_PENA_RESIDUA_ARRES, SOSP.NUM_MESI_PENA_RESIDUA_ARRES, SOSP.NUM_GIORNI_PENA_RESIDUA_ARRES, "
				+ " SOSP.NUM_ANNI_INTERRUZIONE, SOSP.NUM_MESI_INTERRUZIONE, SOSP.NUM_GIORNI_INTERRUZIONE, "
				+ " SOSP.MULTA_ESPIATA, SOSP.AMMENDA_ESPIATA, "
				+ " SOSP.MULTA_RESIDUA, SOSP.AMMENDA_RESIDUA, " + " SOSP.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ " SOSP.FLAG_INTERRUZIONE, SOSP.NUM_GIORNI_LIBANTICIPATA ";
		// lStatement += " FROM sospensione SOSP, pena_residua pena, evento eve ";
		lStatement += " FROM evento eve, pena_residua pena, sospensione SOSP  ";
		lStatement += " WHERE sosp.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo + " ";
		lStatement += " AND sosp.PEN_RES_ID_PENA_RESIDUA = pena.ID_PENA_RESIDUA ";
		lStatement += " AND pena.EVE_ID_EVENTO = eve.ID_EVENTO ";
		lStatement += " AND eve.FLAG_DOCUMENTO_REGISTRATO = 'S' ";

		if (aDataDal != null) {
			// lStatement +=
			// "  AND eve.DATA_AGGIORNAMENTO >= to_date ('"+DateUtils.getDateToString(aDataDal,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
			lStatement += "  AND eve.DATA_INSERIMENTO >= to_date ('"
					+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}
		if (aDataAl != null) {// n.b. <= perchè devo beccare anche l'evento corrente
		// lStatement +=
		// "  AND eve.DATA_AGGIORNAMENTO <= to_date ('"+DateUtils.getDateToString(aDataAl,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
			lStatement += "  AND eve.DATA_INSERIMENTO <= to_date ('"
					+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}

		lStatement += " ORDER BY SOSP.DATA_INIZIO desc ";

		setStatement(lStatement);
	}

	/**
	 * Costruisce la query string (SELECT...FROM...)
	 * 
	 * @return
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_SOSPENSIONE, " + "DATA_INIZIO, " + "DATA_FINE, " + "NUM_ANNI_RINVIO, "
				+ "NUM_MESI_RINVIO, " + "NUM_GIORNI_RINVIO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " + "PEN_RES_ID_PENA_RESIDUA, "
				+ "NUM_ANNI_PENA_ESPIATA, " + "NUM_MESI_PENA_ESPIATA, " + "NUM_GIORNI_PENA_ESPIATA, "
				+ "NUM_ANNI_PENA_RESIDUA_RECLUS, " + "NUM_MESI_PENA_RESIDUA_RECLUS, "
				+ "NUM_GIORNI_PENA_RESIDUA_RECLUS, " + "NUM_ANNI_PENA_RESIDUA_ARRES, "
				+ "NUM_MESI_PENA_RESIDUA_ARRES, " + "NUM_GIORNI_PENA_RESIDUA_ARRES, "
				+ "NUM_ANNI_INTERRUZIONE, " + "NUM_MESI_INTERRUZIONE, " + "NUM_GIORNI_INTERRUZIONE, "
				+ "MULTA_ESPIATA, " + "AMMENDA_ESPIATA, " + "MULTA_RESIDUA, " + "AMMENDA_RESIDUA, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, " + "FLAG_INTERRUZIONE, " + "NUM_GIORNI_LIBANTICIPATA ";
		lStatement += " FROM SOSPENSIONE";
		lStatement += " WHERE ";

		return lStatement;
	}

	public GenericModel getModel() throws DAOException {
		SospensioneModel aModel = new SospensioneModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdSospensione(getBigDecimal("ID_SOSPENSIONE"));
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setNumAnniRinvio(getBigDecimal("NUM_ANNI_RINVIO"));
		aModel.setNumMesiRinvio(getBigDecimal("NUM_MESI_RINVIO"));
		aModel.setNumGiorniRinvio(getBigDecimal("NUM_GIORNI_RINVIO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setPenResIdPenaResidua(getBigDecimal("PEN_RES_ID_PENA_RESIDUA"));
		aModel.setNumAnniPenaEspiata(getBigDecimal("NUM_ANNI_PENA_ESPIATA"));
		aModel.setNumMesiPenaEspiata(getBigDecimal("NUM_MESI_PENA_ESPIATA"));
		aModel.setNumGiorniPenaEspiata(getBigDecimal("NUM_GIORNI_PENA_ESPIATA"));
		aModel.setNumAnniPenaResiduaReclus(getBigDecimal("NUM_ANNI_PENA_RESIDUA_RECLUS"));
		aModel.setNumMesiPenaResiduaReclus(getBigDecimal("NUM_MESI_PENA_RESIDUA_RECLUS"));
		aModel.setNumGiorniPenaResiduaReclus(getBigDecimal("NUM_GIORNI_PENA_RESIDUA_RECLUS"));
		aModel.setNumAnniPenaResiduaArres(getBigDecimal("NUM_ANNI_PENA_RESIDUA_ARRES"));
		aModel.setNumMesiPenaResiduaArres(getBigDecimal("NUM_MESI_PENA_RESIDUA_ARRES"));
		aModel.setNumGiorniPenaResiduaArres(getBigDecimal("NUM_GIORNI_PENA_RESIDUA_ARRES"));
		aModel.setNumAnniInterruzione(getBigDecimal("NUM_ANNI_INTERRUZIONE"));
		aModel.setNumMesiInterruzione(getBigDecimal("NUM_MESI_INTERRUZIONE"));
		aModel.setNumGiorniInterruzione(getBigDecimal("NUM_GIORNI_INTERRUZIONE"));
		aModel.setMultaEspiata(getBigDecimal("MULTA_ESPIATA"));
		aModel.setAmmendaEspiata(getBigDecimal("AMMENDA_ESPIATA"));
		aModel.setMultaResidua(getBigDecimal("MULTA_RESIDUA"));
		aModel.setAmmendaResidua(getBigDecimal("AMMENDA_RESIDUA"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFlagInterruzione(getString("FLAG_INTERRUZIONE"));
		aModel.setNumGiorniLibanticipata(getBigDecimal("NUM_GIORNI_LIBANTICIPATA"));

		return aModel;
	}

	public String setCondizione(SospensioneModel aModel) {

		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {

		// return " AND ID_SOSPENSIONE = " + aKey;
		// ** Al momento nella getSqlQuery non ci sono condizioni nella where
		return " ID_SOSPENSIONE = " + aKey;
	}

	public String setCondizioniByIdPenaResidua(BigDecimal aIdPenaResidua) {

		// return " AND PEN_RES_ID_PENA_RESIDUA = " + aIdPenaResidua;
		// ** Al momento nella getSqlQuery non ci sono condizioni nella where
		return " PEN_RES_ID_PENA_RESIDUA = " + aIdPenaResidua;
	}

	public String setCondizioniByIdFascicolo(BigDecimal aIdFascicoloSiep) {

		// return " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;
		// ** Al momento nella getSqlQuery non ci sono condizioni nella where
		return " FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;
	}

}