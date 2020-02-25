package siap.siep.penaresidua.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.penaresidua.model.PenaPrecedenteModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: PenaPrecedenteSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO
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
public class PenaPrecedenteSqlDAO extends SIAPSqlDAO {

	public PenaPrecedenteSqlDAO(Connection con) {
		super(con);
	}

	/**
	 * Recupera i record PENA_RESIDUA <b>VALIDATI</b> con FLAG_PENA_SOSPESA null o 'N' ordinati per data
	 * inserimento DESC
	 * 
	 * @param aKeyFascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaPrecedenteSospensioniByFascicolo(BigDecimal aKeyFascicolo) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " AND FLAG_VALIDATO = 'S'";
		lSql += " AND (FLAG_PENA_SOSPESA = 'N' OR FLAG_PENA_SOSPESA IS NULL )";
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/**
	 * Restituisce tutti i record pena residua, validati o meno, ordinati per data inserimento decrescente
	 * 
	 * @param aKeyFascicolo
	 * @throws DAOException
	 */
	public void ricercaPenaPrecedenteByFascicolo(BigDecimal aKeyFascicolo) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/**
	 * 
	 * @param aKeyFascicolo
	 * @param aData
	 * @throws DAOException
	 */
	public void ricercaPenaPrecedenteByFascicoloDataIserimento(BigDecimal aKeyFascicolo,
			BigDecimal aIdPenaResidua) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo;
		lSql += " AND FLAG_VALIDATO = 'S' ";
		lSql += " AND DATA_INSERIMENTO < (SELECT DATA_INSERIMENTO FROM PENA_RESIDUA WHERE ID_PENA_RESIDUA = "
				+ aIdPenaResidua + ")";
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	/**
	 * Costruisce e restituisce il SQLStatement
	 * 
	 * @return
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PENA_RESIDUA, " + "DATA_INIZIO, " + "DATA_FINE, "
				+ "NUM_ANNI_RECLUSIONE, " + "NUM_MESI_RECLUSIONE, " + "NUM_GIORNI_RECLUSIONE, "
				+ "IMPORTO_MULTA, " + "NUM_ANNI_ARRESTO, " + "NUM_MESI_ARRESTO, " + "NUM_GIORNI_ARRESTO, "
				+ "IMPORTO_AMMENDA, " + "DIES_A_QUO, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "EVE_ID_EVENTO, " + "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "FLAG_VALIDATO, " + "DATA_FINE_PRESUNTA, " + "DATA_FINE_RECLUSIONE,"
				+ "DATA_INIZIO_ARRESTO," + "FLAG_ERGASTOLO, " + "DATA_INIZIO_ISOLAMENTO_DIURNO, "
				+ "DATA_FINE_ISOLAMENTO_DIURNO, " + "NUM_ANNI_ISOLAMENTO_DIURNO, "
				+ "NUM_MESI_ISOLAMENTO_DIURNO, " + "NUM_GIORNI_ISOLAMENTO_DIURNO, " + "FLAG_PENA_SOSPESA,  "
				+ "MIS_ALT_ID_MISURA_ALTERNATIVA ";
		lStatement += " FROM PENA_RESIDUA";
		lStatement += " WHERE ";

		return lStatement;
	}

	/**
	 * 
	 * @return
	 */
	public GenericModel getModel() throws DAOException {
		PenaPrecedenteModel aModel = new PenaPrecedenteModel();

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

		return aModel;
	}

}