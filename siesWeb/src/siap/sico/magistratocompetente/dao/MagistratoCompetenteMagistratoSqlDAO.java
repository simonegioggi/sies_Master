package siap.sico.magistratocompetente.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: MagistratoCompetenteMagistratoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella MagistratoCompetente
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
public class MagistratoCompetenteMagistratoSqlDAO extends SIAPSqlDAO {

	public MagistratoCompetenteMagistratoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaMagistratoCompetente(MagistratoCompetenteMagistratoModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " AND MAG.DATA_FINE_VALIDITA IS NULL ";
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public MagistratoCompetenteMagistratoModel ricercaMagistratoCompetenteByKey(String aKey)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_MAGISTRATO = '" + aKey + "'";
		lSql += " AND MAG.DATA_FINE_VALIDITA IS NULL ";

		setStatement(lSql);

		MagistratoCompetenteMagistratoModel lModel = new MagistratoCompetenteMagistratoModel();

		lModel = (MagistratoCompetenteMagistratoModel) getModelByKey();

		return lModel;
	}

	public void ricercaMagistratoCompetenteByFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		// Questa modifica è per far vedere il magistrato attualmente associato
		// al fascicolo 07-07-2005
		// lSql += " AND MAG.DATA_FINE_VALIDITA IS NULL ";
		lSql += " AND MAGCOMP.DATA_FINE IS NULL ";

		setStatement(lSql);
	}

	// STUB 13/12/2007 Ricerca dei Magistrati Competenti per IdFascicolo.
	public void ricercaMagistratoCompetenteByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		setStatement(lSql);
	}

	public void ricercaMagistratoCompetenteByFascicoloDataFine(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " AND DATA_FINE IS NULL ";

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT MAG.COD_MAGISTRATO CODICE, MAG.COGNOME COGNOME, MAG.NOME NOME, "
				+ "MAG.FLAG_STATO FLAG_STATO, MAG.COD_UFFICIO_APPARTENENZA COD_UFFICIO_APPARTENENZA,"
				+ "MAG.DATA_INIZIO_VALIDITA DATA_INIZIO_VALIDITA, "
				+ "MAG.DATA_FINE_VALIDITA DATA_FINE_VALIDITA, MAG.COD_OPERATORE_INSERIMENTO MAG_OPERATORE_INSERIMENTO, "
				+ "MAG.DATA_INSERIMENTO MAG_DATA_INSERIMENTO, "
				+ "MAG.COD_UFFICIO_INSERIMENTO MAG_COD_UFFICIO_INSERIMENTO, "
				+ "MAG.COD_OPERATORE_AGGIORNAMENTO MAG_OPERATORE_AGGIORNAMENTO, "
				+ "MAG.DATA_AGGIORNAMENTO MAG_DATA_AGGIORNAMENTO, "
				+ "MAG.COD_UFFICIO_AGGIORNAMENTO MAG_UFFICIO_AGGIORNAMENTO,"
				+ "MAGCOMP.COD_RUOLO_MAGISTRATO  COD_RUOLO_MAGISTRATO," + "MAGCOMP.DATA_INIZIO DATA_INIZIO, "
				+ "MAGCOMP.DATA_FINE DATA_FINE, " + "MAGCOMP.COD_RUOLO_MAGISTRATO COD_RUOLO_MAGISTRATO, "
				+ "MAGCOMP.COD_OPERATORE_INSERIMENTO COMP_OPERATORE_INSERIMENTO, "
				+ "MAGCOMP.DATA_INSERIMENTO COMP_DATA_INSERIMENTO, "
				+ "MAGCOMP.COD_UFFICIO_INSERIMENTO COMP_UFFICIO_INSERIMENTO, "
				+ "MAGCOMP.COD_OPERATORE_AGGIORNAMENTO COMP_OPERATORE_AGGIORNAMENTO, "
				+ "MAGCOMP.DATA_AGGIORNAMENTO COMP_DATA_AGGIORNAMENTO, "
				+ "MAGCOMP.COD_UFFICIO_AGGIORNAMENTO COMP_UFFICIO_AGGIORNAMENTO "
				+ "from magistrato MAG, MAGISTRATO_COMPETENTE MAGCOMP "
				+ "WHERE MAG.COD_MAGISTRATO = MAGCOMP.MAG_COD_MAGISTRATO ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		MagistratoCompetenteMagistratoModel aModel = new MagistratoCompetenteMagistratoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.getMagistrato().setCodMagistrato(getString("CODICE"));
		// aModel.setDescrMagistrato(getString("") );
		aModel.getMagistrato().setCognome(getString("COGNOME"));
		aModel.getMagistrato().setNome(getString("NOME"));
		aModel.getMagistrato().setFlagStato(getString("FLAG_STATO"));
		aModel.getMagistrato().setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		// aModel.setDescrUfficioAppartenenza( getString("") );
		aModel.getMagistrato().setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.getMagistrato().setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.getMagistrato().setCodOperatoreInserimento(getString("MAG_OPERATORE_INSERIMENTO"));
		aModel.getMagistrato().setDataInserimento(getDate("MAG_DATA_INSERIMENTO"));
		aModel.getMagistrato().setCodUfficioInserimento(getString("MAG_COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.getMagistrato().setCodOperatoreAggiornamento(getString("MAG_OPERATORE_AGGIORNAMENTO"));
		aModel.getMagistrato().setDataAggiornamento(getDate("MAG_DATA_AGGIORNAMENTO"));
		aModel.getMagistrato().setCodUfficioAggiornamento(getString("MAG_UFFICIO_AGGIORNAMENTO"));
		aModel.getMagistratoCompetente().setDataInizio(getDate("DATA_INIZIO"));
		aModel.getMagistratoCompetente().setDataFine(getDate("DATA_FINE"));
		aModel.getMagistratoCompetente().setCodRuoloMagistrato(getString("COD_RUOLO_MAGISTRATO"));
		// aModel.getMagistratoCompetente().setDescrRuoloMagistrato(getString("") );
		aModel.getMagistratoCompetente().setCodOperatoreInserimento(getString("COMP_OPERATORE_INSERIMENTO"));
		aModel.getMagistratoCompetente().setDataInserimento(getDate("COMP_DATA_INSERIMENTO"));
		aModel.getMagistratoCompetente().setCodUfficioInserimento(getString("COMP_UFFICIO_INSERIMENTO"));
		// aModel.getMagistratoCompetente().setDescrUfficioInserimento(getString("") );
		aModel.getMagistratoCompetente()
				.setCodOperatoreAggiornamento(getString("COMP_OPERATORE_AGGIORNAMENTO"));
		aModel.getMagistratoCompetente().setDataAggiornamento(getDate("COMP_DATA_AGGIORNAMENTO"));
		aModel.getMagistratoCompetente().setCodUfficioAggiornamento(getString("COMP_UFFICIO_AGGIORNAMENTO"));
		// aModel.getMagistratoCompetente().setDescrUfficioAggiornamento(getString("") );
		// aModel.getMagistratoCompetente().setMagCodMagistrato(getString("MAG_COD_MAGISTRATO") );
		// aModel.getMagistratoCompetente().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP")
		// );

		return aModel;
	}

	public String setCondizione(MagistratoCompetenteMagistratoModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_MAGISTRATO_COMPETENTE = " + aKey;
	}

}