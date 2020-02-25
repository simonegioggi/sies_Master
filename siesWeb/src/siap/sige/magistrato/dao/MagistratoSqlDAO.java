package siap.sige.magistrato.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratosezione.model.MagistratoSezioneModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: MagistratoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Magistrato
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MagistratoSqlDAO extends siap.sico.magistrato.dao.MagistratoSqlDAO {

	public MagistratoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	/**
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

		lSql += " " + setCondizione(aModel);
		lSql += " ORDER BY COGNOME ";
		String lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
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
	 * 
	 * @param aCodUfficio
	 * @throws DAOEXception
	 */
	public void ricercaMagistratoByCodUfficio(String aCodUfficio) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " WHERE COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
		lSql += " ORDER BY COGNOME";
		setStatement(lSql);
	}
	
	/**
	 * [EC] richiesto da Nunzia il 30/05/2019 devono essere selezionati soltanto i magistrati 
	 * 
	 * Ricerca del magistrato per codice ufficio di appartenenza ancora attivi
	 * 
	 * @param aCodUfficio
	 * @throws DAOEXception
	 */
	public void ricercaMagistratoByCodUfficioAncoraValidi(String aCodUfficio) throws DAOException {
		String lSql = getSqlQuery();
		lSql +=   "AND (MAG.DATA_FINE_VALIDITA IS NULL OR MAG.DATA_FINE_VALIDITA > SYSDATE)";
		lSql += " WHERE COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
		lSql += " ORDER BY COGNOME";
		setStatement(lSql);
	}

	/**
	 * Ricerca del magistrato per codice ufficio di appartenenza + magistrato senza codice ufficio che
	 * costituisce il magistrato indefinito.
	 * 
	 * @param aCodUfficio
	 * @param aCodMagistrato
	 * @throws DAOEXception
	 */
	public void ricercaMagistratoByCodUfficioCodMagistrato(String aCodUfficio, String aCodMagistrato)
			throws DAOException {
		String lSql = getSqlQuery();
		lSql += " WHERE COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
		lSql += " AND COD_MAGISTRATO = '" + aCodMagistrato + "'";
		lSql += " ORDER BY COGNOME";
		setStatement(lSql);
	}

	/**
	 * Ricerca del magistrato per codice comune e per tipo di ufficio di appartenenza, + magistrato senza
	 * codice ufficio che costituisce il magistrato indefinito.
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
		lSql += "'-' as DESCR_FLAG_STATO, ";
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
    // Modifica del 29/11/2016 MEV_15_S4
    // Vengono selezionati soltanto i magistrati ancora attivi
    lSql +=   "AND (MAG.DATA_FINE_VALIDITA IS NULL OR MAG.DATA_FINE_VALIDITA > SYSDATE)";

		// lSql += "UNION ";
		// lSql += getSqlQuery();
		// lSql += "WHERE COD_UFFICIO_APPARTENENZA IS null ";
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
	 * @param aCodUfficio
	 * @throws DAOEXception
	 */
	public void ricercaMagistratoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " WHERE ID_MAGISTRATO = " + aKey;
		setStatement(lSql);
	}

	/**
	 * 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un ufficio
	 * differente da quello in cui ha delle udienze poichè trasferito
	 * 
	 * @param aCodMagistrato
	 * @param codUfficioAppartenenza
	 * @throws DAOException
	 */
	public void ricercaMagistratoByCod(String aCodMagistrato, String codUfficioAppartenenza)
			throws DAOException {

		String lSql = getSqlQuery();
		lSql += " WHERE COD_MAGISTRATO = '" + aCodMagistrato + "'";
		// 20170918: [SG] aggiunta and condition poichè il magistrato può essere inserito da un ufficio
		// differente da quello in cui ha delle udienze poichè trasferito
		lSql += " AND COD_UFFICIO_APPARTENENZA = '" + codUfficioAppartenenza + "'";

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
	 * @return
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");
		lStatement += " SELECT " + " COD_MAGISTRATO, " + " COGNOME, " + " NOME, " + " FLAG_STATO, "
				+ " DISP.RV_MEANING AS DESCR_FLAG_STATO, " + " COD_UFFICIO_APPARTENENZA, "
				+ " DATA_INIZIO_VALIDITA, " + " DATA_FINE_VALIDITA, " + " COD_OPERATORE_INSERIMENTO, "
				+ " DATA_INSERIMENTO, " + " COD_UFFICIO_INSERIMENTO, " + " COD_OPERATORE_AGGIORNAMENTO, "
				+ " DATA_AGGIORNAMENTO, " + " COD_UFFICIO_AGGIORNAMENTO, " + " E_MAIL_UFFICIO, "
				+ " E_MAIL_PRIVATA, " + " NUM_CELLULARE ";
		lStatement += " FROM MAGISTRATO MAG ";
		lStatement += " INNER JOIN CG_REF_CODES DISP ON DISP.RV_LOW_VALUE = MAG.FLAG_STATO ";
		lStatement += " AND DISP.RV_DOMAIN = 'FLAG_STATO' ";
		return lStatement;
	}

	public void ricercaMagistratoByCognomeSezione(String aCognome, String aSezione, String aUfficio)
			throws DAOException {
		String lSql = "";

		if (aSezione != null && !aSezione.equals("") && aSezione.trim().compareTo("-") != 0) {
			lSql += getSqlQueryWMagSez();
		} else {
			lSql += getSqlQueryWMag();
		}

		String attach = " WHERE ";
		if (aSezione.trim().compareTo("-") != 0) {
			lSql += ", SEZIONE S, MAGISTRATO_SEZIONE MS ";
			lSql += " WHERE S.ID_SEZIONE = " + aSezione;
			lSql += " AND MS.SEZ_ID_SEZIONE = S.ID_SEZIONE ";
			lSql += " AND MS.MAG_COD_MAGISTRATO = MAGISTRATO.COD_MAGISTRATO";
			lSql += " AND MAGISTRATO.COD_UFFICIO_APPARTENENZA = S.COD_UFFICIO_APPARTENENZA";
			lSql += " AND MS.DATA_FINE_ASS is NULL ";
			attach = " AND ";
		}
		if (aCognome != null) {
			lSql += attach + " MAGISTRATO.COGNOME LIKE '" + aCognome + "%'";
			attach = " AND ";
		}
		lSql += attach + " DISP.RV_LOW_VALUE = MAGISTRATO.FLAG_STATO AND DISP.RV_DOMAIN = 'FLAG_STATO' ";
		lSql += " AND W_MAGISTRATO.COD_MAGISTRATO = MAGISTRATO.COD_MAGISTRATO ";
		lSql += " AND MAGISTRATO.COD_UFFICIO_APPARTENENZA = '" + aUfficio + "'";
		// Vengono selezionati soltanto i magistrati ancora attivi
		lSql += " AND (MAGISTRATO.DATA_FINE_VALIDITA IS NULL OR MAGISTRATO.DATA_FINE_VALIDITA > SYSDATE)";

		lSql += " ORDER BY MAGISTRATO.COGNOME";

		setStatement(lSql);
	}

	public void ricercaMagistratoByIdSezioneUffApp(BigDecimal idSezione, String aUfficio) throws DAOException {
		String lSql = "SELECT  MAG.COD_MAGISTRATO as COD_MAGISTRATO,  MAG.COGNOME as COGNOME,  MAG.NOME as NOME,  MAG.FLAG_STATO as FLAG_STATO,  DISP.RV_MEANING AS DESCR_FLAG_STATO,  MAGISTRATO.COD_UFFICIO_APPARTENENZA,  MAGISTRATO.DATA_INIZIO_VALIDITA,  MAGISTRATO.DATA_FINE_VALIDITA,  MAGISTRATO.COD_OPERATORE_INSERIMENTO,  MAGISTRATO.DATA_INSERIMENTO,  MAGISTRATO.COD_UFFICIO_INSERIMENTO,  MAGISTRATO.COD_OPERATORE_AGGIORNAMENTO,  MAGISTRATO.DATA_AGGIORNAMENTO,  MAGISTRATO.COD_UFFICIO_AGGIORNAMENTO,  MAGISTRATO.E_MAIL_UFFICIO,  MAGISTRATO.E_MAIL_PRIVATA,  MAGISTRATO.NUM_CELLULARE FROM "
				+ "magistrato_sezione MAGSEZ, MAGISTRATO MAG  INNER JOIN CG_REF_CODES DISP ON DISP.RV_LOW_VALUE = MAG.FLAG_STATO  AND DISP.RV_DOMAIN = 'FLAG_STATO'  WHERE "
				+ " MAG.COD_UFFICIO_APPARTENENZA = '"
				+ aUfficio
				+ "' and "
				+ " MAG.COD_MAGISTRATO=MAGSEZ.MAG_COD_MAGISTRATO and "
				+ " MAGSEZ.SEZ_ID_SEZIONE= "
				+ idSezione.intValue() + " ORDER BY MAGISTRATO.COGNOME";

		setStatement(lSql);
	}

	/**
	 * @return
	 */
	protected String getSqlQueryWMag() {
		String lStatement = new String("");

		lStatement += "SELECT " + "MAGISTRATO.COD_MAGISTRATO, " + "MAGISTRATO.COGNOME, "
				+ "MAGISTRATO.NOME, " + "MAGISTRATO.FLAG_STATO, " + " DISP.RV_MEANING AS DESCR_FLAG_STATO, "
				+ "MAGISTRATO.COD_UFFICIO_APPARTENENZA, " + "MAGISTRATO.DATA_INIZIO_VALIDITA, "
				+ "MAGISTRATO.DATA_FINE_VALIDITA, " + "MAGISTRATO.COD_OPERATORE_INSERIMENTO, "
				+ "MAGISTRATO.DATA_INSERIMENTO, " + "MAGISTRATO.COD_UFFICIO_INSERIMENTO, "
				+ "MAGISTRATO.COD_OPERATORE_AGGIORNAMENTO, " + "MAGISTRATO.DATA_AGGIORNAMENTO, "
				+ "MAGISTRATO.COD_UFFICIO_AGGIORNAMENTO, " + "MAGISTRATO.E_MAIL_UFFICIO, "
				+ "MAGISTRATO.E_MAIL_PRIVATA, " + "MAGISTRATO.NUM_CELLULARE, " + "null DATA_INIZIO_ASS, "
				+ "null DATA_FINE_ASS, " + "W_MAGISTRATO.DATA_NASCITA";

		lStatement += " FROM MAGISTRATO, W_MAGISTRATO, CG_REF_CODES DISP ";

		return lStatement;
	}

	protected String getSqlQueryWMagSez() {
		String lStatement = new String("");

		lStatement += "SELECT " + "MAGISTRATO.COD_MAGISTRATO, " + "MAGISTRATO.COGNOME, "
				+ "MAGISTRATO.NOME, " + "MAGISTRATO.FLAG_STATO, " + " DISP.RV_MEANING AS DESCR_FLAG_STATO, "
				+ "MAGISTRATO.COD_UFFICIO_APPARTENENZA, " + "MAGISTRATO.DATA_INIZIO_VALIDITA, "
				+ "MAGISTRATO.DATA_FINE_VALIDITA, " + "MAGISTRATO.COD_OPERATORE_INSERIMENTO, "
				+ "MAGISTRATO.DATA_INSERIMENTO, " + "MAGISTRATO.COD_UFFICIO_INSERIMENTO, "
				+ "MAGISTRATO.COD_OPERATORE_AGGIORNAMENTO, " + "MAGISTRATO.DATA_AGGIORNAMENTO, "
				+ "MAGISTRATO.COD_UFFICIO_AGGIORNAMENTO, " + "MAGISTRATO.E_MAIL_UFFICIO, "
				+ "MAGISTRATO.E_MAIL_PRIVATA, " + "MAGISTRATO.NUM_CELLULARE, " + "MS.DATA_INIZIO_ASS, "
				+ "MS.DATA_FINE_ASS, " + "W_MAGISTRATO.DATA_NASCITA";

		lStatement += " FROM MAGISTRATO, W_MAGISTRATO, CG_REF_CODES DISP ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	/**
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
		// aModel.setDescrFlagStato(getString("DESCR_FLAG_STATO") );
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
		// aModel.setDataNascita(getDate("DATA_NASCITA") );

		return aModel;
	}

	public GenericModel getWModel() throws DAOException {
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
		aModel.setDataNascita(getDate("DATA_NASCITA"));

		MagistratoSezioneModel aSezioneModel = new MagistratoSezioneModel();
		aSezioneModel.setDataInizioAssegnazione(getDate("DATA_INIZIO_ASS"));
		aSezioneModel.setDataFineAssegnazione(getDate("DATA_FINE_ASS"));
		ArrayList lArrayList = new ArrayList();
		lArrayList.add(aSezioneModel);
		if (lArrayList.size() != 0)
			aModel.setMagistratoSezioni((MagistratoSezioneModel[]) lArrayList
					.toArray(new MagistratoSezioneModel[0]));

		return aModel;
	}

	/**
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
			lCondizioni += " COGNOME LIKE '" + aModel.getCognome() + "%'";
			lInserito = true;
		}
		if ((aModel.getNome()).length() != 0) {
			if (lInserito)
				lCondizioni += " AND";
			lCondizioni += " NOME LIKE '" + aModel.getNome() + "%'";
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
	 * 20190403: [EC] aggiunto metodo
	 * 
	 * @param aCodMagistrato
	 * @param codUfficioAppartenenza
	 * @throws DAOException
	 */
	public void ExRicercaMagistratoByCodETipoUfficio(String codMag,
			String codTipoUfficio, String comuneUfficio)
			throws DAOException {

		String lSql = new String();

		lSql += "SELECT MAG.COD_MAGISTRATO, ";
		lSql += "MAG.COGNOME AS COGNOME, ";
		lSql += "MAG.NOME, ";
		lSql += "MAG.FLAG_STATO, ";
		lSql += "'-' as DESCR_FLAG_STATO, ";
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
		lSql += "AND MAG.COD_MAGISTRATO = '" + codMag + "' ";
		lSql += "AND UFF.COD_COMUNE = '" + comuneUfficio + "' ";
		lSql += "AND UFF.COD_TIPO_UFFICIO = '" + codTipoUfficio + "' ";   
        lSql += "AND (MAG.DATA_FINE_VALIDITA IS NULL OR MAG.DATA_FINE_VALIDITA > SYSDATE)";

		lSql += " ORDER BY COGNOME";

		setStatement(lSql);
	}

}