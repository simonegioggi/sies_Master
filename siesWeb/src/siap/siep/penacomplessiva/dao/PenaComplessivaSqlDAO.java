package siap.siep.penacomplessiva.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;

/**
 * <p>
 * Title: PenaComplessivaSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella PenaComplessiva
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
public class PenaComplessivaSqlDAO extends SIAPSqlDAO {

	public PenaComplessivaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODI DI RICERCA
	//

	public void ricercaPenaComplessiva(PenaComplessivaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaPenaComplessivaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaPenaComplessivaByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aIdFascicolo);

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT " + "ID_PENA_COMPLESSIVA, "
				+ "COD_TIPO_PENA_DETENTIVA, PENA_DETENTIVA.RV_MEANING DESCR_PENA_DETENTIVA,"
				+ "NUM_ANNI_RECLUSIONE, " + "NUM_MESI_RECLUSIONE, " + "NUM_GIORNI_RECLUSIONE, "
				+ "IMPORTO_MULTA, " + "NUM_ANNI_ARRESTO, " + "NUM_MESI_ARRESTO, " + "NUM_GIORNI_ARRESTO, "
				+ "IMPORTO_AMMENDA, " + "DATA_INIZIO, " + "DATA_FINE, "
				+ "COD_TIPO_RITO, TIPO_RITO.RV_MEANING DESCR_TIPO_RITO, " + "FLAG_PENA_IN_CONTINUAZIONE, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "NUM_ANNI_CONDONATI, " + "NUM_MESI_CONDONATI, " + "NUM_GIORNI_CONDONATI, "
				+ "IMPORTO_CONDONATO, " + "FAS_SIE_ID_FASCICOLO_SIEP, " + "DATA_INIZIO_ISOLAMENTO_DIURNO, "
				+ "DATA_FINE_ISOLAMENTO_DIURNO, " + "NUM_ANNI_ISOLAMENTO_DIURNO, "
				+ "NUM_MESI_ISOLAMENTO_DIURNO, " + "NUM_GIORNI_ISOLAMENTO_DIURNO, " + "DATA_PRESCRIZIONE ";
		lStatement += " FROM PENA_COMPLESSIVA, CG_REF_CODES PENA_DETENTIVA, CG_REF_CODES TIPO_RITO";
		lStatement += " WHERE (PENA_DETENTIVA.RV_DOMAIN = 'TIPO_PENA_DETENTIVA' AND PENA_COMPLESSIVA.COD_TIPO_PENA_DETENTIVA = PENA_DETENTIVA.RV_LOW_VALUE)";
		lStatement += " AND (TIPO_RITO.RV_DOMAIN = 'TIPO_RITO' AND PENA_COMPLESSIVA.COD_TIPO_RITO = TIPO_RITO.RV_LOW_VALUE)";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		PenaComplessivaModel aModel = new PenaComplessivaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdPenaComplessiva(getBigDecimal("ID_PENA_COMPLESSIVA"));
		aModel.setCodTipoPenaDetentiva(getString("COD_TIPO_PENA_DETENTIVA"));
		aModel.setDescrTipoPenaDetentiva(getString("DESCR_PENA_DETENTIVA"));
		aModel.setNumAnniReclusione(getBigDecimal("NUM_ANNI_RECLUSIONE"));
		aModel.setNumMesiReclusione(getBigDecimal("NUM_MESI_RECLUSIONE"));
		aModel.setNumGiorniReclusione(getBigDecimal("NUM_GIORNI_RECLUSIONE"));
		aModel.setImportoMulta(getBigDecimal("IMPORTO_MULTA"));
		aModel.setNumAnniArresto(getBigDecimal("NUM_ANNI_ARRESTO"));
		aModel.setNumMesiArresto(getBigDecimal("NUM_MESI_ARRESTO"));
		aModel.setNumGiorniArresto(getBigDecimal("NUM_GIORNI_ARRESTO"));
		aModel.setImportoAmmenda(getBigDecimal("IMPORTO_AMMENDA"));
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setCodTipoRito(getString("COD_TIPO_RITO"));
		aModel.setDescrTipoRito(getString("DESCR_TIPO_RITO"));
		aModel.setFlagPenaInContinuazione(getString("FLAG_PENA_IN_CONTINUAZIONE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setNumAnniCondonati(getBigDecimal("NUM_ANNI_CONDONATI"));
		aModel.setNumMesiCondonati(getBigDecimal("NUM_MESI_CONDONATI"));
		aModel.setNumGiorniCondonati(getBigDecimal("NUM_GIORNI_CONDONATI"));
		aModel.setImportoCondonato(getBigDecimal("IMPORTO_CONDONATO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setDataInizioIsolamentoDiurno(getDate("DATA_INIZIO_ISOLAMENTO_DIURNO"));
		aModel.setDataFineIsolamentoDiurno(getDate("DATA_FINE_ISOLAMENTO_DIURNO"));
		aModel.setNumAnniIsolamentoDiurno(getBigDecimal("NUM_ANNI_ISOLAMENTO_DIURNO"));
		aModel.setNumMesiIsolamentoDiurno(getBigDecimal("NUM_MESI_ISOLAMENTO_DIURNO"));
		aModel.setNumGiorniIsolamentoDiurno(getBigDecimal("NUM_GIORNI_ISOLAMENTO_DIURNO"));
		aModel.setDataPrescrizione(getDate("DATA_PRESCRIZIONE"));

		// ----------------------------------------------------------------------------------------------
		PenaResiduaSqlDAO lPenSqlDao = new PenaResiduaSqlDAO(mCon);

		lPenSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(
				getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		PenaResiduaModel lPenRes = (PenaResiduaModel) lPenSqlDao.getModelByKey();

		aModel.setPenaResidua(lPenRes);
		// ----------------------------------------------------------------------------------------------

		return aModel;
	}

	public String setCondizione(PenaComplessivaModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			// lInserito=true;
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		}
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_PENA_COMPLESSIVA = " + aKey;
	}

	public String setCondizioniByIdFascicolo(BigDecimal aIdFascicolo) {
		return " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
	}

}