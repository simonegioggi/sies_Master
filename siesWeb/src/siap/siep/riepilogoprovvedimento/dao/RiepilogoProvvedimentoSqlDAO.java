package siap.siep.riepilogoprovvedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siep.riepilogoprovvedimento.model.RiepilogoProvvedimentoModel;

/**
 * <p>
 * Title: RiepilogoProvvedimentoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RiepilogoProvvedimento
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

public class RiepilogoProvvedimentoSqlDAO extends SqlDAO {
	public RiepilogoProvvedimentoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRiepilogoProvvedimento(RiepilogoProvvedimentoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaRiepilogoProvvedimentoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	// Metodo ricerca Provvedimento by key evento migrato da RES
	public void ricercaRiepilogoProvvedimentoByKeyEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKeyEvento(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_RIEPILOGO_PROVVEDIMENTO, " + "NUM_RES, " + "NUM_PROGRESSIVO_RES, "
				+ "NUM_PROTOCOLLO_RES, " + "FLAG_ERGASTOLO, " + "NUM_ANNI_RECLUSIONE, "
				+ "NUM_MESI_RECLUSIONE, " + "NUM_GIORNI_RECLUSIONE, " + "IMPORTO_MULTA, "
				+ "NUM_ANNI_ARRESTO, " + "NUM_MESI_ARRESTO, " + "NUM_GIORNI_ARRESTO, " + "IMPORTO_AMMENDA, "
				+ "NUM_ANNI_PRESOFFERTO, " + "NUM_MESI_PRESOFFERTO, " + "NUM_GIORNI_PRESOFFERTO, "
				+ "NUM_ANNI_INTERRUZIONE, " + "NUM_MESI_INTERRUZIONE, " + "NUM_GIORNI_INTERRUZIONE, "
				+ "NUM_GIORNI_LIB_ANTICIPATA, " + "NUM_ANNI_RECLUSIONE_BENEFICI, "
				+ "NUM_MESI_RECLUSIONE_BENEFICI, " + "NUM_GIORNI_RECLUSIONE_BENEFICI, "
				+ "IMPORTO_MULTA_BENEFICI, " + "NUM_ANNI_ARRESTO_BENEFICI, " + "NUM_MESI_ARRESTO_BENEFICI, "
				+ "NUM_GIORNI_ARRESTO_BENEFICI, " + "IMPORTO_AMMENDA_BENEFICI, "
				+ "NUM_ANNI_AUMENTI_PENA_RECLUS, " + "NUM_MESI_AUMENTI_PENA_RECLUS, "
				+ "NUM_GIORNI_AUMENTI_PENA_RECLUS, " + "IMPORTO_MULTA_AUMENTI_PENA, "
				+ "NUM_ANNI_AUMENTI_PENA_ARRES, " + "NUM_MESI_AUMENTI_PENA_ARRES, "
				+ "NUM_GIORNI_AUMENTI_PENA_ARRES, " + "IMPORTO_AMMENDA_AUMENTI_PENA, " + "DIES_A_QUO, "
				+ "DATA_INIZIO_PENA, " + "DATA_FINE_PENA, " + "DATA_FINE_RECLUSIONE, "
				+ "DATA_FINE_PRECEDENTE, " + "DATA_FINE_DET_DOMICILIARE, " + "NUM_ANNI_PENA_RESIDUA_RECLUS, "
				+ "NUM_MESI_PENA_RESIDUA_RECLUS, " + "NUM_GIORNI_PENA_RESIDUA_RECLUS, "
				+ "IMPORTO_MULTA_RESIDUA, " + "NUM_ANNI_PENA_RESIDUA_ARRES, "
				+ "NUM_MESI_PENA_RESIDUA_ARRES, " + "NUM_GIORNI_PENA_RESIDUA_ARRES, "
				+ "IMPORTO_AMMENDA_RESIDUA, " + "NUM_ANNI_FUNGIBILITA, " + "NUM_MESI_FUNGIBILITA, "
				+ "NUM_GIORNI_FUNGIBILITA, " + "COD_TIPO_PROVVEDIMENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "EVE_ID_EVENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP ";
		lStatement += " FROM RIEPILOGO_PROVVEDIMENTO";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		RiepilogoProvvedimentoModel aModel = new RiepilogoProvvedimentoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdRiepilogoProvvedimento(getBigDecimal("ID_RIEPILOGO_PROVVEDIMENTO"));
		aModel.setNumRes(getBigDecimal("NUM_RES"));
		aModel.setNumProgressivoRes(getBigDecimal("NUM_PROGRESSIVO_RES"));
		aModel.setNumProtocolloRes(getBigDecimal("NUM_PROTOCOLLO_RES"));
		aModel.setFlagErgastolo(getString("FLAG_ERGASTOLO"));
		aModel.setNumAnniReclusione(getBigDecimal("NUM_ANNI_RECLUSIONE"));
		aModel.setNumMesiReclusione(getBigDecimal("NUM_MESI_RECLUSIONE"));
		aModel.setNumGiorniReclusione(getBigDecimal("NUM_GIORNI_RECLUSIONE"));
		aModel.setImportoMulta(getBigDecimal("IMPORTO_MULTA"));
		aModel.setNumAnniArresto(getBigDecimal("NUM_ANNI_ARRESTO"));
		aModel.setNumMesiArresto(getBigDecimal("NUM_MESI_ARRESTO"));
		aModel.setNumGiorniArresto(getBigDecimal("NUM_GIORNI_ARRESTO"));
		aModel.setImportoAmmenda(getBigDecimal("IMPORTO_AMMENDA"));
		aModel.setNumAnniPresofferto(getBigDecimal("NUM_ANNI_PRESOFFERTO"));
		aModel.setNumMesiPresofferto(getBigDecimal("NUM_MESI_PRESOFFERTO"));
		aModel.setNumGiorniPresofferto(getBigDecimal("NUM_GIORNI_PRESOFFERTO"));
		aModel.setNumAnniInterruzione(getBigDecimal("NUM_ANNI_INTERRUZIONE"));
		aModel.setNumMesiInterruzione(getBigDecimal("NUM_MESI_INTERRUZIONE"));
		aModel.setNumGiorniInterruzione(getBigDecimal("NUM_GIORNI_INTERRUZIONE"));
		aModel.setNumGiorniLibAnticipata(getBigDecimal("NUM_GIORNI_LIB_ANTICIPATA"));
		aModel.setNumAnniReclusioneBenefici(getBigDecimal("NUM_ANNI_RECLUSIONE_BENEFICI"));
		aModel.setNumMesiReclusioneBenefici(getBigDecimal("NUM_MESI_RECLUSIONE_BENEFICI"));
		aModel.setNumGiorniReclusioneBenefici(getBigDecimal("NUM_GIORNI_RECLUSIONE_BENEFICI"));
		aModel.setImportoMultaBenefici(getBigDecimal("IMPORTO_MULTA_BENEFICI"));
		aModel.setNumAnniArrestoBenefici(getBigDecimal("NUM_ANNI_ARRESTO_BENEFICI"));
		aModel.setNumMesiArrestoBenefici(getBigDecimal("NUM_MESI_ARRESTO_BENEFICI"));
		aModel.setNumGiorniArrestoBenefici(getBigDecimal("NUM_GIORNI_ARRESTO_BENEFICI"));
		aModel.setImportoAmmendaBenefici(getBigDecimal("IMPORTO_AMMENDA_BENEFICI"));
		aModel.setNumAnniAumentiPenaReclus(getBigDecimal("NUM_ANNI_AUMENTI_PENA_RECLUS"));
		aModel.setNumMesiAumentiPenaReclus(getBigDecimal("NUM_MESI_AUMENTI_PENA_RECLUS"));
		aModel.setNumGiorniAumentiPenaReclus(getBigDecimal("NUM_GIORNI_AUMENTI_PENA_RECLUS"));
		aModel.setImportoMultaAumentiPena(getBigDecimal("IMPORTO_MULTA_AUMENTI_PENA"));
		aModel.setNumAnniAumentiPenaArres(getBigDecimal("NUM_ANNI_AUMENTI_PENA_ARRES"));
		aModel.setNumMesiAumentiPenaArres(getBigDecimal("NUM_MESI_AUMENTI_PENA_ARRES"));
		aModel.setNumGiorniAumentiPenaArres(getBigDecimal("NUM_GIORNI_AUMENTI_PENA_ARRES"));
		aModel.setImportoAmmendaAumentiPena(getBigDecimal("IMPORTO_AMMENDA_AUMENTI_PENA"));
		aModel.setDiesAQuo(getString("DIES_A_QUO"));
		aModel.setDataInizioPena(getDate("DATA_INIZIO_PENA"));
		aModel.setDataFinePena(getDate("DATA_FINE_PENA"));
		aModel.setDataFineReclusione(getDate("DATA_FINE_RECLUSIONE"));
		aModel.setDataFinePrecedente(getDate("DATA_FINE_PRECEDENTE"));
		aModel.setDataFineDetDomiciliare(getDate("DATA_FINE_DET_DOMICILIARE"));
		aModel.setNumAnniPenaResiduaReclus(getBigDecimal("NUM_ANNI_PENA_RESIDUA_RECLUS"));
		aModel.setNumMesiPenaResiduaReclus(getBigDecimal("NUM_MESI_PENA_RESIDUA_RECLUS"));
		aModel.setNumGiorniPenaResiduaReclus(getBigDecimal("NUM_GIORNI_PENA_RESIDUA_RECLUS"));
		aModel.setImportoMultaResidua(getBigDecimal("IMPORTO_MULTA_RESIDUA"));
		aModel.setNumAnniPenaResiduaArres(getBigDecimal("NUM_ANNI_PENA_RESIDUA_ARRES"));
		aModel.setNumMesiPenaResiduaArres(getBigDecimal("NUM_MESI_PENA_RESIDUA_ARRES"));
		aModel.setNumGiorniPenaResiduaArres(getBigDecimal("NUM_GIORNI_PENA_RESIDUA_ARRES"));
		aModel.setImportoAmmendaResidua(getBigDecimal("IMPORTO_AMMENDA_RESIDUA"));
		aModel.setNumAnniFungibilita(getBigDecimal("NUM_ANNI_FUNGIBILITA"));
		aModel.setNumMesiFungibilita(getBigDecimal("NUM_MESI_FUNGIBILITA"));
		aModel.setNumGiorniFungibilita(getBigDecimal("NUM_GIORNI_FUNGIBILITA"));
		aModel.setCodTipoProvvedimento(getBigDecimal("COD_TIPO_PROVVEDIMENTO"));
		// aModel.setDescrTipoProvvedimento(getString("") );
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		return aModel;
	}

	public String setCondizione(RiepilogoProvvedimentoModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_RIEPILOGO_PROVVEDIMENTO = " + aKey;
	}

	public String setCondizioniByKeyEvento(BigDecimal aKey) {
		return " WHERE EVE_ID_EVENTO = " + aKey;
	}

}