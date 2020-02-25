package siap.sige.tenore.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sige.tenore.model.TenoreSigeModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: TenoreSigeSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella TenoreSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class TenoreSigeSqlDAO extends SqlDAO {

	public TenoreSigeSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	protected String getSqlQuery() throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "T.ID_TENORE_SIGE, " + "T.COD_OGGETTO_SIGE, " + "T.COD_ESITO_SIGE, "
				+ "T.DATA, " + "T.DATA_FINE, " + "T.RIC_SIG_ID_RICHIESTA_SIGE, "
				+ "T.COD_OPERATORE_INSERIMENTO, " + "T.COD_UFFICIO_INSERIMENTO, " + "T.DATA_INSERIMENTO, "
				+ "T.COD_OPERATORE_AGGIORNAMENTO, " + "T.COD_UFFICIO_AGGIORNAMENTO, "
				+ "T.DATA_AGGIORNAMENTO, " + "T.PROV_ID_PROVVEDIMENTO_SIGE, " + "T.FAS_ID_FASCICOLO_SIGE, "
				+ "T.NOTE, " + "TSR.ID_TEN_SEN_REA, " + "TSR.SEN_ID_SENTENZA, " + "TSR.REA_ID_REATO, "
				+ "TSR.COD_ESITO, " + "C.RV_MEANING " +
				// 23/11/2018 (email Nunzia del 22/11/2018 )ANOMALIA SIGE-STEP4 (OCCORRE ESTRARRE ANCHE LA
				// DESCRIZIONE DELL'OGGETTO PROCEDIMENTO)
				", CC.RV_MEANING DESCR_CONTENUTO, CC.RV_LOW_VALUE COD_CONTENUTO ";
		lStatement += "FROM TENORE_SIGE T JOIN TENORE_SENTENZA_REATO TSR on ( T.ID_TENORE_SIGE = TSR.TEN_ID_TENORE_SIGE) ";
		lStatement += "JOIN CG_REF_CODES C on (C.RV_DOMAIN = 'OGGETTO_SIGE' AND C.RV_LOW_VALUE = T.COD_OGGETTO_SIGE) ";
		// 23/11/2018 (email Nunzia del 22/11/2018 )ANOMALIA SIGE-STEP4 (OCCORRE ESTRARRE ANCHE LA DESCRIZIONE
		// DELL'OGGETTO PROCEDIMENTO)
		lStatement += "JOIN CG_REF_CODES CC on (CC.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND CC.RV_LOW_VALUE = C.RV_HIGH_VALUE)";

		return lStatement;
	}

	public void ricercaTenoriSigeAttivi(TenoreSigeModel aModel) throws DAOException {
		String lStatement = getSqlQuery();
		if (aModel.getFasIdFascicoloSige() != null) {
			lStatement += " " + setCondizioneByIdFasSige(aModel.getFasIdFascicoloSige());
		} else if (aModel.getProvIdProvvedimentoSige() != null) {
			lStatement += " " + setCondizioneByIdProvvedimento(aModel.getProvIdProvvedimentoSige());
		} else if (aModel.getRicSigIdRichiestaSige() != null) {
			lStatement += " " + setCondizioneByIdRichiesta(aModel.getRicSigIdRichiestaSige());
		} else
			throw new DAOException("condizioni di ricerca non valorizzate ");

		lStatement += " AND DATA_FINE IS NULL ";
		// @emma 09072018 intervento post COLLAUDO 11.2 (aggiungo il campo COD_OGGETTO_SIGE in order by )
		lStatement += " ORDER BY  COD_OGGETTO_SIGE, ID_TENORE_SIGE, SEN_ID_SENTENZA";
		setStatement(lStatement);
	}

	public void ricercaTenoriSige(TenoreSigeModel aModel) throws DAOException {
		String lStatement = getSqlQuery();
		if (aModel.getFasIdFascicoloSige() != null) {
			lStatement += " " + setCondizioneByIdFasSige(aModel.getFasIdFascicoloSige());
		} else if (aModel.getProvIdProvvedimentoSige() != null) {
			lStatement += " " + setCondizioneByIdProvvedimento(aModel.getProvIdProvvedimentoSige());
		} else if (aModel.getRicSigIdRichiestaSige() != null) {
			lStatement += " " + setCondizioneByIdRichiesta(aModel.getRicSigIdRichiestaSige());
		} else
			throw new DAOException("condizioni di ricerca non valorizzate ");

		lStatement += " ORDER BY  ID_TENORE_SIGE, SEN_ID_SENTENZA";
		setStatement(lStatement);
	}

	public void ricercaTenoriByRichiesta(BigDecimal aIdRichiesta) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " " + setCondizioneByIdRichiesta(aIdRichiesta);
		lStatement += " ORDER BY  ID_TENORE_SIGE, SEN_ID_SENTENZA";
		setStatement(lStatement);
	}

	public void ricercaTenoriById(BigDecimal aIdTenoreSige) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " " + setCondizioneById(aIdTenoreSige);
		lStatement += " ORDER BY  ID_TENORE_SIGE, SEN_ID_SENTENZA";
		setStatement(lStatement);
	}

	public void ricercaTenoriByProvvedimento(BigDecimal aIdProvvedimento) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " " + setCondizioneByIdProvvedimento(aIdProvvedimento);
		lStatement += " ORDER BY  ID_TENORE_SIGE, SEN_ID_SENTENZA";
		setStatement(lStatement);
	}

	public void ricercaTenoriByProvvedimentoAndCodOggetto(String codOggetto, BigDecimal aIdProvvedimento)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " " + setCondizioneByIdProvvedimento(aIdProvvedimento);
		lStatement += " and COD_OGGETTO_SIGE=" + codOggetto;
		lStatement += " ORDER BY  ID_TENORE_SIGE, SEN_ID_SENTENZA";
		setStatement(lStatement);
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		TenoreSigeModel aModel = new TenoreSigeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdTenoreSige(getBigDecimal("ID_TENORE_SIGE"));
		aModel.setCodOggettoSige(getString("COD_OGGETTO_SIGE"));
		aModel.setDescrOggettoSige(getString("RV_MEANING"));
		aModel.setCodEsitoSige(getString("COD_ESITO_SIGE"));
		// aModel.setDescrEsitoSige(getString("") );
		aModel.setData(getDate("DATA"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setRicSigIdRichiestaSige(getBigDecimal("RIC_SIG_ID_RICHIESTA_SIGE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));
		aModel.setIdReato(getBigDecimal("REA_ID_REATO"));
		aModel.setProvIdProvvedimentoSige(getBigDecimal("PROV_ID_PROVVEDIMENTO_SIGE"));
		aModel.setFasIdFascicoloSige(getBigDecimal("FAS_ID_FASCICOLO_SIGE"));
		aModel.setTenSenReaId(getBigDecimal("ID_TEN_SEN_REA"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodEsitoTenSenRea(getString("COD_ESITO"));
		// [EC] 20190325 INTERVENTO PER 11.1.2
		if (getString("DESCR_CONTENUTO") != null)
			aModel.setDescrContenutoSige(getString("DESCR_CONTENUTO"));
		if (getString("COD_CONTENUTO") != null)
			aModel.setCodContenutoSige(getString("COD_CONTENUTO"));
		return aModel;
	}

	// public void selCondizione(TenoreSigeModel aModel) {
	// String lCondizioni = new String();
	// boolean lInserito = false;
	// }

	public String setCondizioni(TenoreSigeModel aModel) {
		String lCondizioni = new String();
		return lCondizioni;
	}

	public String setCondizioneByIdRichiesta(BigDecimal aIdRichiesta) {
		String lCondizioni = "WHERE RIC_SIG_ID_RICHIESTA_SIGE = " + aIdRichiesta;
		return lCondizioni;
	}

	public String setCondizioneByIdFasSige(BigDecimal aIdFascicolo) {
		String lCondizioni = "WHERE FAS_ID_FASCICOLO_SIGE = " + aIdFascicolo;
		return lCondizioni;
	}

	public String setCondizioneByIdProvvedimento(BigDecimal aIdProvvedimento) {
		String lCondizioni = "WHERE PROV_ID_PROVVEDIMENTO_SIGE = " + aIdProvvedimento;
		return lCondizioni;
	}

	public String setCondizioneById(BigDecimal aIdTenoreSige) {
		String lCondizioni = "WHERE ID_TENORE_SIGE = " + aIdTenoreSige;
		return lCondizioni;
	}

	/**
	 * Individua il codice Esito del TENORE SIGE per Codice Oggetto e per Codice Tipo Provvedimento SIGE.
	 * <p>
	 * 
	 * @return String
	 * @param aCodTipoProvvedimento
	 *            Codice Tipo Provvedimento SIGE.
	 * @param aCodOggetto
	 *            Codice Oggetto SIGE.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public String getCodEsitoTenoreSige(String aCodTipoProvvedimento, String aCodOggetto)
			throws DAOException {
		String lCodEsitoInammissibilità = new String();
		String lStatement = "";
		lStatement += " SELECT rv_low_value Codice FROM cg_ref_codes ";
		lStatement += " WHERE rv_domain = 'ESITO_TENORE_SIGE'";
		lStatement += " AND rv_high_value IN (SELECT rv_high_value FROM cg_ref_codes WHERE rv_domain = 'OGGETTO_SIGE' ";
		lStatement += " AND rv_low_value = '" + aCodOggetto + "')";
		lStatement += " AND rv_alt2_value = '" + aCodTipoProvvedimento + "'";

		setStatement(lStatement);
		start();

		if (next())
			lCodEsitoInammissibilità = getString("Codice");
		stop();
		return lCodEsitoInammissibilità;
	}

	public void getCountSentenzeByIdTenoreSige(BigDecimal aIdTenoreSige) throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyRecords ";
		lStatement += " FROM TENORE_SENTENZA_REATO ";
		lStatement += " WHERE TEN_ID_TENORE_SIGE = " + aIdTenoreSige;

		setStatement(lStatement);
	}

	public void ricercaSentenzeByRichiestaAndIdTenoreSige(BigDecimal aIdRichiesta, BigDecimal aIdTenoreSige)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " JOIN SENTENZA S ON (TSR.SEN_ID_SENTENZA = S.ID_SENTENZA) ";
		lStatement += " WHERE RIC_SIG_ID_RICHIESTA_SIGE = " + aIdRichiesta;
		lStatement += " AND ID_TENORE_SIGE = " + aIdTenoreSige;
		lStatement += " ORDER BY  ID_TENORE_SIGE, SEN_ID_SENTENZA";
		setStatement(lStatement);
	}

	public void ricercaTenoriByRichiesta(BigDecimal aIdRichiesta, String codContenuto) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " " + setCondizioneByIdRichiesta(aIdRichiesta);
		lStatement += " AND CC.RV_LOW_VALUE  = '" + codContenuto + "'";
		lStatement += " ORDER BY  ID_TENORE_SIGE, SEN_ID_SENTENZA";
		setStatement(lStatement);
	}

	// 20190509 [SG]: recupero tenori legati al procedimento
	// public void ricercaTenoriSigeByIdfascicoloIdProvvedimento(BigDecimal aIdFascicolo) throws DAOException
	// {
	//
	// String lStatement = getSqlQuery();
	// lStatement += " " + setCondizioneByIdFasSige(aIdFascicolo);
	// lStatement += " AND T.PROV_ID_PROVVEDIMENTO_SIGE IS NULL";
	// lStatement += " ORDER BY ID_TENORE_SIGE, SEN_ID_SENTENZA";
	// setStatement(lStatement);
	// }

}