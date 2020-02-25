package siap.sius.tenore.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: TenoreSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Tenore
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
public class TenoreSqlDAO extends SIAPSqlDAO {

	public TenoreSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaTenore(TenoreModel aModel) throws DAOException {
		String lSql = getTenoreSqlQuery();
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	// STUB 07/11/2003 Corretta ricercaTenoreByKey per puntare ad ID_TENORE.
	public void ricercaTenoreByKey(BigDecimal aIdTenore) throws DAOException {
		String lStatement = getTenoreSqlQuery();
		lStatement += " AND ID_TENORE = " + aIdTenore;
		setStatement(lStatement);
	}

	/**
	 * ricerca i Tenori Associati ad un precedimento SIUS.
	 * <p>
	 *
	 * @param aIdGeneraleProcedimento
	 * @throws DAOException
	 */
	public void ricercaTenoreByGeneraleProc(BigDecimal aIdGeneraleProcedimento) throws DAOException {
		String lStatement = getTenoreSqlQuery();
		lStatement += " AND DATA_FINE IS NULL AND GEN_PRID_GENERALE_PROCEDIMENTO = "
				+ aIdGeneraleProcedimento;
		lStatement += " ORDER BY COD_ESITO_TENORE DESC ";
		setStatement(lStatement);
	}

	/**
	 * ricerca i Tenori Associati ad un precedimento SIUS.
	 * <p>
	 *
	 * @param aIdGeneraleProcedimento
	 * @throws DAOException
	 */
	public void ricercaTenoreByGeneraleProcOrderByPeso(BigDecimal aIdGeneraleProcedimento)
			throws DAOException {
		String lStatement = getTenoreOrderByPesoSqlQuery();
		lStatement += " AND DATA_FINE IS NULL AND GEN_PRID_GENERALE_PROCEDIMENTO = "
				+ aIdGeneraleProcedimento;
		lStatement += " ORDER BY PESO_TENORE.RV_HIGH_VALUE, TEN.COD_OGGETTO_TENORE ";
		setStatement(lStatement);
	}

	/**
	 * ricerca i Tenori Associati ad una ordinanza.
	 * <p>
	 *
	 * @param aIdDepositoOrdinanza
	 * @throws DAOException
	 */

	public void ricercaTenoreByOrdinanza(BigDecimal aIdDepositoOrdinanza) throws DAOException {
		String lStatement = getTenoreSqlQuery();
		lStatement += " AND DEP_OPID_DEPOSITO_ORDINANZA_PC = " + aIdDepositoOrdinanza;
		lStatement += " ORDER BY COD_ESITO_TENORE DESC ";
		setStatement(lStatement);
	}

	/**
	 * ricerca i Tenori Associati ad un decreto.
	 * <p>
	 *
	 * @param aIdDepositoDecreto
	 * @throws DAOException
	 */

	public void ricercaTenoreByDecreto(BigDecimal aIdDepositoDecreto) throws DAOException {
		String lStatement = getTenoreSqlQuery();
		lStatement += " AND DEP_DEC_ID_DEPOSITO_DECRETO = " + aIdDepositoDecreto;
		lStatement += " ORDER BY COD_ESITO_TENORE DESC ";
		setStatement(lStatement);
	}

	/**
	 * ricerca i Tenori Associati ad un decreto.
	 * <p>
	 *
	 * @param aIdDepositoDecreto
	 * @throws DAOException
	 */

	public void ricercaTenoreByDecretoIrreperibilità(BigDecimal aIdDepositoDecreto) throws DAOException {
		String lStatement = getTenoreSqlQueryIrreperibilità();
		lStatement += " AND DEP_DEC_ID_DEPOSITO_DECRETO = " + aIdDepositoDecreto;
		lStatement += " ORDER BY COD_ESITO_TENORE DESC ";
		setStatement(lStatement);
	}

	/**
	 * ricerca i Tenori Associati ad un procedimento SIUS Stralciato.
	 * <p>
	 *
	 * @param aIdGeneraleProcedimento
	 * @throws DAOException
	 */
	public void ricercaTenoreByStralcio(BigDecimal aIdGeneraleProcedimento, String aDataEmissione)
			throws DAOException {
		String lStatement = getTenoreSqlQuery();
		lStatement += " AND DATA_FINE is not NULL AND GEN_PRID_GENERALE_PROCEDIMENTO = "
				+ aIdGeneraleProcedimento;
		lStatement += " AND COD_ESITO_TENORE = " + "'0604'" + "AND TO_CHAR(DATA,'YYYYMMDD') = '"
				+ aDataEmissione + "'";
		lStatement += " ORDER BY COD_ESITO_TENORE DESC ";
		setStatement(lStatement);
	}

	/**
	 *
	 * @return
	 */
	protected String getTenoreSqlQuery() {
		String lStatement = new String();

		lStatement += "SELECT ID_TENORE, COD_ESITO_TENORE,ESITO.RV_MEANING DESCR_ESITO, DATA, NVL(COD_MAGISTRATO, '') COD_MAGISTRATO, TEN.NOTE NOTE, COD_OGGETTO_TENORE,";
		lStatement += " OGGETTO_TENORE.RV_MEANING DESC_OGGETTO_TENORE, TEN.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, TEN.DATA_INSERIMENTO DATA_INSERIMENTO,";
		lStatement += " TEN.COD_UFFICIO_INSERIMENTO COD_UFFICIO_INSERIMENTO, DESCR_COM_UFF.DESCRIZIONE DESC_UFFICIO_INSERIMENTO, TEN.COD_OPERATORE_AGGIORNAMENTO COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " TEN.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO,TEN.DATA_FINE DATA_FINE, TEN.COD_UFFICIO_AGGIORNAMENTO COD_UFFICIO_AGGIORNAMENTO, GEN_PRID_GENERALE_PROCEDIMENTO, ";
		lStatement += " DEP_OPID_DEPOSITO_ORDINANZA_PC, IMP_ID_IMPUGNAZIONE, PROGR_TENORE, DEP_DEC_ID_DEPOSITO_DECRETO, ";
		lStatement += " COD_DETTAGLIO_OGGETTO, DETTAGLIO_OGGETTO.RV_MEANING DESC_DETTAGLIO_OGGETTO, OGGETTO_TENORE.RV_ABBREVIATION ABBR_OGGETTO_TENORE, ";
		lStatement += " DEP_ID_DEPOSITO_SENTENZA ";
		lStatement += " FROM TENORE TEN, GENERALE_PROCEDIMENTO GP, CG_REF_CODES OGGETTO_TENORE, CG_REF_CODES DETTAGLIO_OGGETTO, CG_REF_CODES ESITO,";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF ";

		lStatement += " WHERE (TEN.GEN_PRID_GENERALE_PROCEDIMENTO = GP.ID_GENERALE_PROCEDIMENTO)";
		/*
		 * STUB : 2003-07-03 Commentata perchè non viene usata la tabella (CG_REF_CODES OGGETTO_PROCEDIMENTO)
		 * nella FROM.
		 */
		// lStatement += " AND (GP.COD_OGGETTO_PROCEDIMENTO = OGGETTO_PROCEDIMENTO.RV_LOW_VALUE)";
		/*
		 * STUB : 2003-07-03 Commentata poichè esiste il caso in cui possono essere selezionati OGGETTI
		 * indipendentemente dal contenuto, ossia contenuto pari a (-).
		 */
		// lStatement += " AND (OGGETTO_TENORE.RV_HIGH_VALUE = GP.COD_OGGETTO_PROCEDIMENTO)";
		lStatement += " AND (OGGETTO_TENORE.RV_LOW_VALUE = COD_OGGETTO_TENORE)";
		lStatement += " AND (OGGETTO_TENORE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') ";
		lStatement += " AND (DETTAGLIO_OGGETTO.RV_LOW_VALUE = COD_DETTAGLIO_OGGETTO)";
		lStatement += " AND (DETTAGLIO_OGGETTO.RV_DOMAIN = 'DETTAGLIO_MOTIVO') ";
		lStatement += " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE AND TEN.COD_UFFICIO_INSERIMENTO = UFF.COD_UFFICIO) ";
		lStatement += " AND (ESITO.RV_LOW_VALUE = COD_ESITO_TENORE)";
		// lStatement += " AND (ESITO.RV_DOMAIN = 'ESITO_TENORE') ";
		// Luigi 14-11-2003 COD_ESITO corrisponde a COD_PROVVEDIMENTO
		lStatement += " AND (ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO') ";

		return lStatement;
	}

	/**
	 *
	 * @return
	 */
	protected String getTenoreSqlQueryIrreperibilità() {
		String lStatement = new String();

		lStatement += "SELECT ID_TENORE, COD_ESITO_TENORE, DATA, NVL(COD_MAGISTRATO, '') COD_MAGISTRATO, TEN.NOTE NOTE, COD_OGGETTO_TENORE, null DESCR_ESITO, null DESC_UFFICIO_INSERIMENTO,";
		lStatement += " OGGETTO_TENORE.RV_MEANING DESC_OGGETTO_TENORE, TEN.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, TEN.DATA_INSERIMENTO DATA_INSERIMENTO,";
		lStatement += " TEN.COD_UFFICIO_INSERIMENTO COD_UFFICIO_INSERIMENTO,  TEN.COD_OPERATORE_AGGIORNAMENTO COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " TEN.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO,TEN.DATA_FINE DATA_FINE, TEN.COD_UFFICIO_AGGIORNAMENTO COD_UFFICIO_AGGIORNAMENTO, GEN_PRID_GENERALE_PROCEDIMENTO, ";
		lStatement += " DEP_OPID_DEPOSITO_ORDINANZA_PC, IMP_ID_IMPUGNAZIONE, PROGR_TENORE, DEP_DEC_ID_DEPOSITO_DECRETO, ";
		lStatement += " COD_DETTAGLIO_OGGETTO, DETTAGLIO_OGGETTO.RV_MEANING DESC_DETTAGLIO_OGGETTO, OGGETTO_TENORE.RV_ABBREVIATION ABBR_OGGETTO_TENORE, ";
		lStatement += " DEP_ID_DEPOSITO_SENTENZA ";
		lStatement += " FROM TENORE TEN, GENERALE_PROCEDIMENTO GP, CG_REF_CODES OGGETTO_TENORE, CG_REF_CODES DETTAGLIO_OGGETTO";

		lStatement += " WHERE (TEN.GEN_PRID_GENERALE_PROCEDIMENTO = GP.ID_GENERALE_PROCEDIMENTO)";
		/*
		 * STUB : 2003-07-03 Commentata perchè non viene usata la tabella (CG_REF_CODES OGGETTO_PROCEDIMENTO)
		 * nella FROM.
		 */
		// lStatement += " AND (GP.COD_OGGETTO_PROCEDIMENTO = OGGETTO_PROCEDIMENTO.RV_LOW_VALUE)";
		/*
		 * STUB : 2003-07-03 Commentata poichè esiste il caso in cui possono essere selezionati OGGETTI
		 * indipendentemente dal contenuto, ossia contenuto pari a (-).
		 */
		// lStatement += " AND (OGGETTO_TENORE.RV_HIGH_VALUE = GP.COD_OGGETTO_PROCEDIMENTO)";
		lStatement += " AND (OGGETTO_TENORE.RV_LOW_VALUE = COD_OGGETTO_TENORE)";
		lStatement += " AND (OGGETTO_TENORE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') ";
		lStatement += " AND (DETTAGLIO_OGGETTO.RV_LOW_VALUE = COD_DETTAGLIO_OGGETTO)";
		lStatement += " AND (DETTAGLIO_OGGETTO.RV_DOMAIN = 'DETTAGLIO_MOTIVO') ";

		return lStatement;
	}

	/**
	 *
	 * @param aIdGeneraleProcedimento
	 */
	public void ricercaTenoriByGeneraleProcOrderByPeso(BigDecimal aIdGeneraleProcedimento) {
		String lStatement = new String();

		lStatement += "SELECT ID_TENORE,COD_ESITO_TENORE,ESITO.RV_MEANING DESCR_ESITO, DATA, NVL(COD_MAGISTRATO, '') COD_MAGISTRATO, TEN.NOTE NOTE, COD_OGGETTO_TENORE,";
		lStatement += " OGGETTO_TENORE.RV_MEANING DESC_OGGETTO_TENORE, TEN.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, TEN.DATA_INSERIMENTO DATA_INSERIMENTO,";
		lStatement += " TEN.COD_UFFICIO_INSERIMENTO COD_UFFICIO_INSERIMENTO, DESCR_COM_UFF.DESCRIZIONE DESC_UFFICIO_INSERIMENTO, TEN.COD_OPERATORE_AGGIORNAMENTO COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " TEN.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, TEN.DATA_FINE DATA_FINE,TEN.COD_UFFICIO_AGGIORNAMENTO COD_UFFICIO_AGGIORNAMENTO, GEN_PRID_GENERALE_PROCEDIMENTO, ";
		lStatement += " DEP_OPID_DEPOSITO_ORDINANZA_PC, IMP_ID_IMPUGNAZIONE, PROGR_TENORE, PESO_TENORE.RV_HIGH_VALUE PESO_ESITO_TENORE, DEP_DEC_ID_DEPOSITO_DECRETO, ";
		lStatement += " COD_DETTAGLIO_OGGETTO, DETTAGLIO_OGGETTO.RV_MEANING DESC_DETTAGLIO_OGGETTO, OGGETTO_TENORE.RV_ABBREVIATION ABBR_OGGETTO_TENORE, ";
		lStatement += " DEP_ID_DEPOSITO_SENTENZA ";
		lStatement += " FROM TENORE TEN, GENERALE_PROCEDIMENTO GP, CG_REF_CODES OGGETTO_TENORE,CG_REF_CODES ESITO,";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, CG_REF_CODES PESO_TENORE, CG_REF_CODES DETTAGLIO_OGGETTO ";
		lStatement += " WHERE (TEN.GEN_PRID_GENERALE_PROCEDIMENTO = GP.ID_GENERALE_PROCEDIMENTO)";
		lStatement += " AND (OGGETTO_TENORE.RV_LOW_VALUE = COD_OGGETTO_TENORE)";
		lStatement += " AND (OGGETTO_TENORE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') ";
		lStatement += " AND (DETTAGLIO_OGGETTO.RV_LOW_VALUE = COD_DETTAGLIO_OGGETTO)";
		lStatement += " AND (DETTAGLIO_OGGETTO.RV_DOMAIN = 'DETTAGLIO_MOTIVO') ";
		lStatement += " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE AND TEN.COD_UFFICIO_INSERIMENTO = UFF.COD_UFFICIO) ";
		lStatement += " AND (ESITO.RV_LOW_VALUE = COD_ESITO_TENORE)";
		lStatement += " AND (ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO') ";
		lStatement += " AND (PESO_TENORE.RV_DOMAIN = 'PESO_ESITO_TENORE' AND PESO_TENORE.RV_LOW_VALUE = COD_ESITO_TENORE) ";
		lStatement += " AND GEN_PRID_GENERALE_PROCEDIMENTO = " + aIdGeneraleProcedimento;
		lStatement += " ORDER BY PESO_TENORE.RV_HIGH_VALUE, TEN.COD_OGGETTO_TENORE ";

		setStatement(lStatement);
	}

	protected String getTenoreOrderByPesoSqlQuery() {
		String lStatement = new String();
		lStatement = "SELECT ID_TENORE,COD_ESITO_TENORE,ESITO.RV_MEANING DESCR_ESITO, DATA, NVL(COD_MAGISTRATO, '') COD_MAGISTRATO, TEN.NOTE NOTE, COD_OGGETTO_TENORE,";
		lStatement += " OGGETTO_TENORE.RV_MEANING DESC_OGGETTO_TENORE, TEN.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, TEN.DATA_INSERIMENTO DATA_INSERIMENTO,";
		lStatement += " TEN.COD_UFFICIO_INSERIMENTO COD_UFFICIO_INSERIMENTO, DESCR_COM_UFF.DESCRIZIONE DESC_UFFICIO_INSERIMENTO, TEN.COD_OPERATORE_AGGIORNAMENTO COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " TEN.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, TEN.DATA_FINE DATA_FINE, TEN.COD_UFFICIO_AGGIORNAMENTO COD_UFFICIO_AGGIORNAMENTO, GEN_PRID_GENERALE_PROCEDIMENTO, ";
		lStatement += " DEP_OPID_DEPOSITO_ORDINANZA_PC, IMP_ID_IMPUGNAZIONE, PROGR_TENORE, PESO_TENORE.RV_HIGH_VALUE PESO_ESITO_TENORE, DEP_DEC_ID_DEPOSITO_DECRETO, ";
		lStatement += " COD_DETTAGLIO_OGGETTO, DETTAGLIO_OGGETTO.RV_MEANING DESC_DETTAGLIO_OGGETTO, OGGETTO_TENORE.RV_ABBREVIATION ABBR_OGGETTO_TENORE, ";
		lStatement += " DEP_ID_DEPOSITO_SENTENZA ";
		lStatement += " FROM TENORE TEN, GENERALE_PROCEDIMENTO GP, CG_REF_CODES OGGETTO_TENORE,CG_REF_CODES ESITO,";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, CG_REF_CODES PESO_TENORE, CG_REF_CODES DETTAGLIO_OGGETTO ";
		lStatement += " WHERE (TEN.GEN_PRID_GENERALE_PROCEDIMENTO = GP.ID_GENERALE_PROCEDIMENTO)";
		lStatement += " AND (OGGETTO_TENORE.RV_LOW_VALUE = COD_OGGETTO_TENORE)";
		lStatement += " AND (OGGETTO_TENORE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') ";
		lStatement += " AND (DETTAGLIO_OGGETTO.RV_LOW_VALUE = COD_DETTAGLIO_OGGETTO)";
		lStatement += " AND (DETTAGLIO_OGGETTO.RV_DOMAIN = 'DETTAGLIO_MOTIVO') ";
		lStatement += " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE AND TEN.COD_UFFICIO_INSERIMENTO = UFF.COD_UFFICIO) ";
		lStatement += " AND (ESITO.RV_LOW_VALUE = COD_ESITO_TENORE)";
		lStatement += " AND (ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO') ";
		lStatement += " AND (PESO_TENORE.RV_DOMAIN = 'PESO_ESITO_TENORE' AND PESO_TENORE.RV_LOW_VALUE = COD_ESITO_TENORE) ";

		return lStatement;
	}

	// copiato il metodo getTenoreOrderByPesoSqlQuery ma senza generale procedimento perchè in SIEP
	// non viene inserito il generale procedimento e quindi non è verificata la condizione richiesta nel
	// metodo sopra indicato -- Dario -- Viviana -- 19-05-2006
	protected String getTenoreOrderByPesoNoGenProcSqlQuery() {
		String lStatement = new String();
		lStatement = "SELECT ID_TENORE,COD_ESITO_TENORE,ESITO.RV_MEANING DESCR_ESITO, DATA, NVL(COD_MAGISTRATO, '') COD_MAGISTRATO, TEN.NOTE NOTE, COD_OGGETTO_TENORE,";
		lStatement += " OGGETTO_TENORE.RV_MEANING DESC_OGGETTO_TENORE, TEN.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, TEN.DATA_INSERIMENTO DATA_INSERIMENTO,";
		lStatement += " TEN.COD_UFFICIO_INSERIMENTO COD_UFFICIO_INSERIMENTO, DESCR_COM_UFF.DESCRIZIONE DESC_UFFICIO_INSERIMENTO, TEN.COD_OPERATORE_AGGIORNAMENTO COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += " TEN.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, TEN.DATA_FINE DATA_FINE, TEN.COD_UFFICIO_AGGIORNAMENTO COD_UFFICIO_AGGIORNAMENTO, GEN_PRID_GENERALE_PROCEDIMENTO, ";
		lStatement += " DEP_OPID_DEPOSITO_ORDINANZA_PC, IMP_ID_IMPUGNAZIONE, PROGR_TENORE, PESO_TENORE.RV_HIGH_VALUE PESO_ESITO_TENORE, DEP_DEC_ID_DEPOSITO_DECRETO, ";
		lStatement += " COD_DETTAGLIO_OGGETTO, DETTAGLIO_OGGETTO.RV_MEANING DESC_DETTAGLIO_OGGETTO, OGGETTO_TENORE.RV_ABBREVIATION ABBR_OGGETTO_TENORE, ";
		lStatement += " DEP_ID_DEPOSITO_SENTENZA ";
		lStatement += " FROM TENORE TEN, CG_REF_CODES OGGETTO_TENORE,CG_REF_CODES ESITO,";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, CG_REF_CODES PESO_TENORE, CG_REF_CODES DETTAGLIO_OGGETTO ";
		lStatement += " WHERE (OGGETTO_TENORE.RV_LOW_VALUE = COD_OGGETTO_TENORE)";
		lStatement += " AND (OGGETTO_TENORE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO') ";
		lStatement += " AND (DETTAGLIO_OGGETTO.RV_LOW_VALUE = COD_DETTAGLIO_OGGETTO)";
		lStatement += " AND (DETTAGLIO_OGGETTO.RV_DOMAIN = 'DETTAGLIO_MOTIVO') ";
		lStatement += " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE AND TEN.COD_UFFICIO_INSERIMENTO = UFF.COD_UFFICIO) ";
		lStatement += " AND (ESITO.RV_LOW_VALUE = COD_ESITO_TENORE)";
		lStatement += " AND (ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO') ";
		lStatement += " AND (PESO_TENORE.RV_DOMAIN = 'PESO_ESITO_TENORE' AND PESO_TENORE.RV_LOW_VALUE = COD_ESITO_TENORE) ";

		return lStatement;
	}

	public void ricercaTenoriByOrdinanzaOrderByPesoNoGenProc(BigDecimal aIdDepositoOrdinanza) {
		String lStatement = getTenoreOrderByPesoNoGenProcSqlQuery();

		lStatement += " AND DEP_OPID_DEPOSITO_ORDINANZA_PC = " + aIdDepositoOrdinanza;
		lStatement += " ORDER BY PESO_TENORE.RV_HIGH_VALUE, TEN.COD_OGGETTO_TENORE ";
		setStatement(lStatement);
	}

	public void ricercaTenoriByDecretoOrderByPesoNoGenProc(BigDecimal aIdDepositoDecreto) {
		String lStatement = getTenoreOrderByPesoNoGenProcSqlQuery();

		lStatement += " AND DEP_DEC_ID_DEPOSITO_DECRETO = " + aIdDepositoDecreto;
		lStatement += " ORDER BY PESO_TENORE.RV_HIGH_VALUE, TEN.COD_OGGETTO_TENORE ";
		setStatement(lStatement);
	}

	/**
	 *
	 * @param aIdDepositoOrdinanza
	 */
	public void ricercaTenoriByOrdinanzaOrderByPeso(BigDecimal aIdDepositoOrdinanza) {

		String lStatement = getTenoreOrderByPesoSqlQuery();
		lStatement += " AND DEP_OPID_DEPOSITO_ORDINANZA_PC = " + aIdDepositoOrdinanza;
		// 20191022 [SG]: l'ordinamento è fondamentale per l'ordine degli esiti su una ordinanza con più
		// oggetti; se gli esiti sono "concede" e "rigetta" vince sempre il "concede"!
		// SEGNALAZIONE VIA MAIL DI GASBARRI --> Ticket#20191023016
		// lStatement += " ORDER BY GP.COD_OGGETTO_PROCEDIMENTO";
		lStatement += " ORDER BY PESO_TENORE.RV_HIGH_VALUE, TEN.COD_OGGETTO_TENORE";
		setStatement(lStatement);
	}

	/**
	 *
	 * @param aIdDepositoDecreto
	 */
	public void ricercaTenoriByDecretoOrderByPeso(BigDecimal aIdDepositoDecreto) {
		String lStatement = getTenoreOrderByPesoSqlQuery();

		lStatement += " AND DEP_DEC_ID_DEPOSITO_DECRETO = " + aIdDepositoDecreto;
		lStatement += " ORDER BY PESO_TENORE.RV_HIGH_VALUE, TEN.COD_OGGETTO_TENORE ";
		setStatement(lStatement);
	}

	/**
	 *
	 * @param aIdDepositoDecreto
	 */
	public void ricercaTenoriByDecretoOrderByPesoNoFine(BigDecimal aIdDepositoDecreto) {
		String lStatement = getTenoreOrderByPesoSqlQuery();
		lStatement += " AND TEN.DATA_FINE IS NULL ";
		lStatement += " AND DEP_DEC_ID_DEPOSITO_DECRETO = " + aIdDepositoDecreto;
		lStatement += " ORDER BY PESO_TENORE.RV_HIGH_VALUE, TEN.COD_OGGETTO_TENORE ";
		setStatement(lStatement);
	}

	/**
	 * Metodo che imposta lo statement, per recuperare la count dei records che rispettano le condizioni di
	 * filtro per Id Generale procedimento e il codice oggetto tenore e per data fine is null.
	 * <p>
	 *
	 * @param aModel
	 *            Dati utilizzati per impostare le condizioni di filtro.
	 */
	public void countTenoriByCodOggettoTenoreGenProcNoFine(TenoreModel aModel) {
		String lStatement = "SELECT COUNT(*) AS COUNT FROM TENORE WHERE ";
		lStatement += " GEN_PRID_GENERALE_PROCEDIMENTO = " + aModel.getGenPridGeneraleProcedimento();
		lStatement += " AND COD_OGGETTO_TENORE = '" + aModel.getCodOggettoTenore() + "'";
		lStatement += " AND DATA_FINE IS NULL ";

		setStatement(lStatement);
	}

	/**
	 * Metodo che ritorna il progressivo max del campo Prog_Tenore, filtrando gli stessi per ID Generale
	 * Procedimento e data di fine == null. NOTA : Il metodo oltre ad impostare la select, esegue anche
	 * l'interrogazione al dbase invocando i metodi start() e stop() del SqlDAO.
	 * <p>
	 *
	 * @param aIdGeneraleProcedimento
	 *            generale procedimento.
	 */
	public BigDecimal getMaxProgTenoreByGenProcNoDataFine(BigDecimal aIdGeneraleProcedimento)
			throws DAOException {
		BigDecimal lProgr = new BigDecimal(0);

		// Si compone lo statement necessario al recupero del Max Num
		// del progressivo.
		String lStatement = "SELECT MAX(PROGR_TENORE) AS aMAX FROM TENORE WHERE ";
		lStatement += " GEN_PRID_GENERALE_PROCEDIMENTO = " + aIdGeneraleProcedimento;
		lStatement += " AND DATA_FINE IS NULL ";

		// Imposta lo statement.
		setStatement(lStatement);

		start(); // Esegue lo statement.

		// Legge il valre ritornato.
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lProgr = getBigDecimal("aMAX");

		stop(); // Stop del dao

		return lProgr;
	}

	/**
	 * Metodo che imposta lo statement, per recuperare i tenori stralciati dal fascicolo puntato. I Tenori
	 * sono individuati per Id Generale procedimento e il codice oggetto tenore.
	 * <p>
	 *
	 * @param aModel
	 *            Dati utilizzati per impostare le condizioni di filtro.
	 */
	public void ricercaTenoriStralcio(BigDecimal aIdFascicoloSius) {
		String lStatement = getTenoreOrderByPesoSqlQuery();

		lStatement += " AND GEN_PRID_GENERALE_PROCEDIMENTO = ID_GENERALE_PROCEDIMENTO ";
		lStatement += " AND DATA_FINE IS NOT NULL ";
		lStatement += " AND COD_ESITO_TENORE = '0604' ";
		lStatement += " AND FAS_SIU_ID_FASCICOLO_SIUS = '" + aIdFascicoloSius + "' ";
		lStatement += " ORDER BY PESO_TENORE.RV_HIGH_VALUE, TEN.COD_OGGETTO_TENORE ";
		setStatement(lStatement);
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		TenoreModel aModel = new TenoreModel();

		aModel.setIdTenore(getBigDecimal("ID_TENORE"));
		aModel.setCodEsitoTenore(getString("COD_ESITO_TENORE"));
		aModel.setDescrEsitoTenore(getString("DESCR_ESITO"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		aModel.setData(getDate("DATA"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOggettoTenore(getString("COD_OGGETTO_TENORE"));
		aModel.setDescrOggettoTenore(getString("DESC_OGGETTO_TENORE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDescrUfficioInserimento(getString("DESC_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setGenPridGeneraleProcedimento(getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"));
		aModel.setDepOpidDepositoOrdinanzaPc(getBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC"));
		aModel.setImpIdImpugnazione(getBigDecimal("IMP_ID_IMPUGNAZIONE"));
		aModel.setProgrTenore(getBigDecimal("PROGR_TENORE"));
		aModel.setDepDecIdDepositoDecreto(getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO"));
		aModel.setCodDettaglioOggetto(getString("COD_DETTAGLIO_OGGETTO"));
		aModel.setDescrDettaglioOggetto(getString("DESC_DETTAGLIO_OGGETTO"));
		aModel.setAbbrOggettoTenore(getString("ABBR_OGGETTO_TENORE"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setDepIdDepositoSentenza(getBigDecimal("DEP_ID_DEPOSITO_SENTENZA"));
		return aModel;
	}

	public String setCondizione(TenoreModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_TENORE = " + aKey;
	}

	/**
	 * @param aIdDepositoSentenza
	 */
	public void ricercaTenoriBySentenzaOrderByPeso(BigDecimal aIdDepositoSentenza) {
		String lStatement = getTenoreOrderByPesoSqlQuery();

		lStatement += " AND DEP_ID_DEPOSITO_SENTENZA = " + aIdDepositoSentenza;
		lStatement += " ORDER BY PESO_TENORE.RV_HIGH_VALUE, TEN.COD_OGGETTO_TENORE ";
		setStatement(lStatement);
	}

}