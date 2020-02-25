package siap.sico.magistrato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;

/**
 * <p>
 * Title: MagistratoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Magistrato
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
public class MagistratoSqlDAO extends SIAPSqlDAO {

	public MagistratoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	/**
	 *
	 * <p>
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaMagistrato(MagistratoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		lSql += " ORDER BY COGNOME ";
		setStatement(lSql);
	}

	public void ricercaMagistratoPaged(MagistratoModel aModel, int aPage) throws DAOException {
		String lSql = getSqlQuery();
		String lPaginedStatement = new String("");

		lSql += " " + setCondizione(aModel);
		lSql += " ORDER BY COGNOME ";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	public void getCountMagistrati(MagistratoModel aModel) throws DAOException

	{
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM MAGISTRATO";
		lStatement += " " + setCondizione(aModel);
		lStatement += " ORDER BY COGNOME ";

		setStatement(lStatement);
	}

	/**
	 * Ricerca del magistrato per codice ufficio di appartenenza + magistrato senza codice ufficio che
	 * costituisce il magistrato indefinito.
	 * <p>
	 * 
	 * @param aCodUfficio
	 * @throws DAOEXception
	 */
	public void ricercaMagistratoByCodUfficio(String aCodUfficio) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " WHERE COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
		lSql += " OR COD_UFFICIO_APPARTENENZA IS null";
		lSql += " ORDER BY COGNOME";
		setStatement(lSql);
	}

	/**
	 * Ricerca del magistrato per codice comune e per tipo di ufficio di appartenenza, + magistrato senza
	 * codice ufficio che costituisce il magistrato indefinito.
	 * <p>
	 * 
	 * @param aCodComune
	 * @param aCodTipoUfficio
	 * @throws DAOEXception
	 */
	public void ricercaMagByCodComuneCodTipoUff(String aCodComune, String aCodTipoUfficio)
			throws DAOException {
		String lSql = new String();

		lSql += "SELECT MAG.COD_MAGISTRATO, ";
		lSql += "MAG.COGNOME AS COGNOME, ";
		lSql += "MAG.NOME, ";
		lSql += "MAG.FLAG_STATO, ";
		lSql += "MAG.COD_UFFICIO_APPARTENENZA, ";
		lSql += "MAG.DATA_INIZIO_VALIDITA, ";
		lSql += "MAG.DATA_FINE_VALIDITA, ";
		lSql += "MAG.COD_OPERATORE_INSERIMENTO, ";
		lSql += "MAG.DATA_INSERIMENTO, ";
		lSql += "MAG.COD_UFFICIO_INSERIMENTO, ";
		lSql += "MAG.COD_OPERATORE_AGGIORNAMENTO, ";
		lSql += "MAG.DATA_AGGIORNAMENTO, ";
		lSql += "MAG.COD_UFFICIO_AGGIORNAMENTO, ";
		lSql += "MAG.E_MAIL_UFFICIO, ";
		lSql += "MAG.E_MAIL_PRIVATA, ";
		lSql += "MAG.NUM_CELLULARE ";
		lSql += "FROM MAGISTRATO MAG, UFFICIO UFF ";
		lSql += "WHERE MAG.COD_UFFICIO_APPARTENENZA = UFF.COD_UFFICIO ";
		lSql += "AND UFF.COD_COMUNE = '" + aCodComune + "' ";
		lSql += "AND UFF.COD_TIPO_UFFICIO = '" + aCodTipoUfficio + "' ";
		lSql += "UNION ";
		lSql += getSqlQuery();
		lSql += "WHERE COD_UFFICIO_APPARTENENZA IS null ";
		lSql += " ORDER BY COGNOME";

		setStatement(lSql);
	}

	public void ricercaMagistratoByFascicolo(BigDecimal aFascicolo) throws DAOException {
		String lSql = new String();
		lSql += "SELECT  ";
		lSql += " M.COD_MAGISTRATO,  ";
		lSql += " M.COGNOME,  ";
		lSql += " M.NOME,  ";
		lSql += " M.FLAG_STATO,  ";
		lSql += " M.COD_UFFICIO_APPARTENENZA,  ";
		lSql += " M.DATA_INIZIO_VALIDITA,  ";
		lSql += " M.DATA_FINE_VALIDITA,  ";
		lSql += " M.COD_OPERATORE_INSERIMENTO,  ";
		lSql += " M.DATA_INSERIMENTO,  ";
		lSql += " M.COD_UFFICIO_INSERIMENTO,  ";
		lSql += " M.COD_OPERATORE_AGGIORNAMENTO,  ";
		lSql += " M.DATA_AGGIORNAMENTO,  ";
		lSql += " M.COD_UFFICIO_AGGIORNAMENTO,  ";
		lSql += "M.E_MAIL_UFFICIO,  ";
		lSql += " M.E_MAIL_PRIVATA,  ";
		lSql += " M.NUM_CELLULARE  ";
		lSql += " FROM MAGISTRATO M,MISURA_ALTERNATIVA MA, EVENTO E, NOTIFICA N  ";
		lSql += " WHERE MA.COD_MAGISTRATO = M.COD_MAGISTRATO ";
		lSql += " AND E.ID_EVENTO= MA.EVE_ID_EVENTO   ";
		lSql += " AND N.COD_TIPO_NOTIFICA='C' ";
		lSql += " AND E.COD_TIPO_PROVVEDIMENTO='03'  ";
		lSql += " AND E.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicolo;

		setStatement(lSql);
	}

	/**
	 *
	 * <p>
	 * 
	 * @param aCodUfficio
	 * @throws DAOEXception
	 */
	public void ricercaMagistratoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " WHERE ID_MAGISTRATO = " + aKey;
		setStatement(lSql);
	}

	/**
	 *
	 * <p>
	 * 
	 * @param aCodMagistrato
	 * @throws DAOException
	 */
	public void ricercaMagistratoByCod(String aCodMagistrato) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " WHERE COD_MAGISTRATO = '" + aCodMagistrato + "'";
		setStatement(lSql);
	}

	public void ricercaMagistratoByCognome(String aCognome) throws DAOException {
		String lSql = getSqlQuery();
		if (aCognome != null)
			lSql += " " + "WHERE COGNOME LIKE '" + aCognome + "%'";
		lSql += " ORDER BY COGNOME";
		setStatement(lSql);
	}

	/**
	 *
	 * <p>
	 * 
	 * @return
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT " + "COD_MAGISTRATO, " + "COGNOME, " + "NOME, " + "FLAG_STATO, "
				+ "COD_UFFICIO_APPARTENENZA, " + "DATA_INIZIO_VALIDITA, " + "DATA_FINE_VALIDITA, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "E_MAIL_UFFICIO, " + "E_MAIL_PRIVATA, " + "NUM_CELLULARE ";

		lStatement += " FROM MAGISTRATO ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	/**
	 *
	 * <p>
	 * 
	 * @return
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		MagistratoModel aModel = new MagistratoModel();
		// Inserire le opportune set delle descrizioni!
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		// aModel.setDescrMagistrato(getString("") );
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		// aModel.setDescrUfficioAppartenenza( getString("") );
		aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setEMailUfficio(getString("E_MAIL_UFFICIO"));
		aModel.setEMailPrivata(getString("E_MAIL_PRIVATA"));
		aModel.setNumCellulare(getString("NUM_CELLULARE"));

		return aModel;
	}

	/**
	 *
	 * <p>
	 * 
	 * @param aModel
	 * @return
	 */
	public String setCondizione(MagistratoModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;

		if ((aModel.getCodUfficioAppartenenza()).length() != 0) {
			lCondizioni = " COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() + "'";
			lInserito = true;
		}
		if ((aModel.getCodMagistrato()).length() != 0) {
			if (lInserito)
				lCondizioni += " AND";
			lCondizioni += " COD_MAGISTRATO = '" + aModel.getCodMagistrato() + "'";
			lInserito = true;
		}
		if ((aModel.getCognome()).length() != 0) {
			if (lInserito)
				lCondizioni += " AND";
			// 20200124 [SG]: aggiunto controllo convertSqlString su nome e cognome
			lCondizioni += " COGNOME LIKE '" + StringUtils.convertSqlString(aModel.getCognome()) + "%'";
			lInserito = true;
		}
		if ((aModel.getNome()).length() != 0) {
			if (lInserito)
				lCondizioni += " AND";
			lCondizioni += " NOME LIKE '" + StringUtils.convertSqlString(aModel.getNome()) + "%'";
			lInserito = true;
		}
		if (aModel.getDataInizioValidita() != null) {
			if (lInserito)
				lCondizioni += " AND";
			lCondizioni += " DATA_INIZIO_VALIDITA >= TO_DATE("
					+ DateUtils.getDateToString(aModel.getDataInizioValidita(), "yyyyMMdd") + ",'YYYYMMDD' )";
			lInserito = true;
		}
		if (aModel.getDataFineValidita() != null) {
			if (lInserito)
				lCondizioni += " AND";
			lCondizioni += " DATA_FINE_VALIDITA <= TO_DATE("
					+ DateUtils.getDateToString(aModel.getDataFineValidita(), "yyyyMMdd") + ",'YYYYMMDD' )";
			lInserito = true;
		}
		if (lInserito)
			lCondizioni = " WHERE " + lCondizioni;

		return lCondizioni;
	}

	/**
	 * Ricerca dei magistrati Validi per codice ufficio di appartenenza + magistrato senza codice ufficio che
	 * costituisce il magistrato indefinito.
	 * <p>
	 * 
	 * @param aCodUfficio
	 * @throws DAOEXception
	 */
	public void ricercaMagistratiValidiByCodUfficio(String aCodUfficio) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " WHERE (COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
		lSql += " OR COD_UFFICIO_APPARTENENZA IS null)";
		// Vengono selezionati soltanto i magistrati ancora attivi
		lSql += " AND (DATA_FINE_VALIDITA IS NULL OR DATA_FINE_VALIDITA > SYSDATE)";
		lSql += " ORDER BY COGNOME";
		setStatement(lSql);
	}

}