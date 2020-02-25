package siap.siep.modulocumulo.dao;

/**
* <p>Title: PeriodoLibAntCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella PeriodoLibAntCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.model.PeriodoLibAntCumuloModel;

public class PeriodoLibAntCumuloSqlDAO extends SqlDAO {

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public PeriodoLibAntCumuloSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountPeriodoLibAntCumulo(PeriodoLibAntCumuloModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM PERIODO_LIB_ANT_CUMULO ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
	 * passata in input
	 * 
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaPeriodoLibAntCumuloPaged(PeriodoLibAntCumuloModel aModel, int aPage)
			throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		lStatement += " " + getOrderBy() + " ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaPeriodoLibAntCumulo(PeriodoLibAntCumuloModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 * 
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaPeriodoLibAntCumuloByKey(BigDecimal aIdPeriodoLibAntCumulo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdPeriodoLibAntCumulo);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaPeriodoLibAntCumuloByLibIdLibAntCum(BigDecimal aIdLibAntCum) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKeyIdLibAntCum(aIdLibAntCum);
		lSql += " ORDER BY DATA_INIZIO ASC";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 * 
	 * @return
	 ****************************************************************************/
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PERIODO_LIB_ANT_CUMULO, " + "DATA_INIZIO, " + "DATA_FINE, "
				+ "LIB_ID_LIB_ANTICIPATA_CUMULO, " + "FLAG_STATO, " + "MOTIVO_MODIFICA, "
				+ "ID_PERIODO_LIBANT_ORIGINE, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += " FROM PERIODO_LIB_ANT_CUMULO ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		PeriodoLibAntCumuloModel aModel = new PeriodoLibAntCumuloModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdPeriodoLibAntCumulo(getBigDecimal("ID_PERIODO_LIB_ANT_CUMULO"));
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));

		aModel.setLibIdLibAnticipataCumulo(getBigDecimal("LIB_ID_LIB_ANTICIPATA_CUMULO"));
		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setMotivoModifica(getString("MOTIVO_MODIFICA"));
		aModel.setIdPeriodoLibantOrigine(getBigDecimal("ID_PERIODO_LIBANT_ORIGINE"));

		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(PeriodoLibAntCumuloModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdPeriodoLibAntCumulo() != null) {
			lCondizioni += " and ID_PERIODO_LIB_ANT_CUMULO = " + aModel.getIdPeriodoLibAntCumulo() + "";
		}
		if (aModel.getDataInizio() != null) {
			lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizio(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFine() != null) {
			lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFine(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getLibIdLibAnticipataCumulo() != null) {
			lCondizioni += " and LIB_ID_LIB_ANTICIPATA_CUMULO = " + aModel.getLibIdLibAnticipataCumulo() + "";
		}
		if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) {
			lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' ";
		}
		if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) {
			lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' ";
		}
		if (aModel.getIdPeriodoLibantOrigine() != null) {
			lCondizioni += " and ID_PERIODO_LIBANT_ORIGINE = " + aModel.getIdPeriodoLibantOrigine() + "";
		}
		if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' ";
		}
		if (aModel.getDataInserimento() != null) {
			lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		}
		if (aModel.getCodOperatoreAggiornamento() != null
				&& aModel.getCodOperatoreAggiornamento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "' ";
		}
		if (aModel.getDataAggiornamento() != null) {
			lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
		}
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di select per chiave
	 * 
	 * @param aKey
	 * @return
	 ****************************************************************************/
	public String setCondizioniByKey(BigDecimal aIdPeriodoLibAntCumulo) {
		String lCondizioni = new String();

		lCondizioni += " and ID_PERIODO_LIB_ANT_CUMULO = " + aIdPeriodoLibAntCumulo;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	public String setCondizioniByKeyIdLibAntCum(BigDecimal aIdLibAntCum) {
		String lCondizioni = new String();

		lCondizioni += " and LIB_ID_LIB_ANTICIPATA_CUMULO = " + aIdLibAntCum;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sezione order by
	 * 
	 * @return
	 ****************************************************************************/
	protected String getOrderBy() {
		String orderBy = new String("");
		// orderBy = " ORDER BY ";
		return orderBy;
	}

}