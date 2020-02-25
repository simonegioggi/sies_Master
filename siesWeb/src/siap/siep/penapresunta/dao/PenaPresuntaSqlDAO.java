package siap.siep.penapresunta.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.penapresunta.model.PenaPresuntaModel;

/**
 * <p>
 * Title: PenaPresuntaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella PenaPresunta
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

public class PenaPresuntaSqlDAO extends SIAPSqlDAO {
	public PenaPresuntaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaPenaPresunta(PenaPresuntaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaPenaPresuntaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaPenaPresuntaCorrenteByFascicoloSiep(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE  ID_PENA_PRESUNTA = ( SELECT MAX(ID_PENA_PRESUNTA) FROM PENA_PRESUNTA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aKey + ")";

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PENA_PRESUNTA, " + "DATA_INIZIO, " + "DATA_FINE, "
				+ "NUM_ANNI_RECLUSIONE, " + "NUM_MESI_RECLUSIONE, " + "NUM_GIORNI_RECLUSIONE, "
				+ "IMPORTO_MULTA, " + "NUM_ANNI_ARRESTO, " + "NUM_MESI_ARRESTO, " + "NUM_GIORNI_ARRESTO, "
				+ "IMPORTO_AMMENDA, " + "DIES_A_QUO, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "FAS_SIE_ID_FASCICOLO_SIEP, " + "DATA_FINE_RECLUSIONE, "
				+ "DATA_INIZIO_ARRESTO ";
		lStatement += " FROM PENA_PRESUNTA";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		PenaPresuntaModel aModel = new PenaPresuntaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdPenaPresunta(getBigDecimal("ID_PENA_PRESUNTA"));
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
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setDataFineReclusione(getDate("DATA_FINE_RECLUSIONE"));
		aModel.setDataInizioArresto(getDate("DATA_INIZIO_ARRESTO"));
		return aModel;
	}

	public String setCondizione(PenaPresuntaModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_PENA_PRESUNTA = " + aKey;
	}
}
