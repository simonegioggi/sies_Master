package siap.siep.istruttoriacumulo.dao;

/**
* <p>Title: EsitoArchiviazioniCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella EsitoArchiviazioniCumulo</p>
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
import siap.siep.istruttoriacumulo.model.EsitoArchiviazioniCumuloModel;

public class EsitoArchiviazioniCumuloSqlDAO extends SqlDAO {

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public EsitoArchiviazioniCumuloSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountEsitoArchiviazioniCumulo(EsitoArchiviazioniCumuloModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM ESITO_ARCHIVIAZIONI_CUMULO ";

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
	public void ricercaEsitoArchiviazioniCumuloPaged(EsitoArchiviazioniCumuloModel aModel, int aPage)
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
	public void ricercaEsitoArchiviazioniCumulo(EsitoArchiviazioniCumuloModel aModel) throws DAOException {
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
	public void ricercaEsitoArchiviazioniCumuloByKey(BigDecimal aIdEsitoArchiviazioniCumulo)
			throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdEsitoArchiviazioniCumulo);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaEsitoArchiviazioniCumuloByEveIdEvento(BigDecimal aIdEvento) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByEveIdEvento(aIdEvento);

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

		lStatement += " SELECT " + "ID_ESITO_ARCHIVIAZIONI_CUMULO, " + "FLAG_ARCHIVIATO, "
				+ "DESCRIZIONE_ESITO, " + "CHIAVE_UFFICIO, " + "CHIAVE_ANNO_FASC_SIEP, "
				+ "CHIAVE_PROGR_FASC_SIEP, " + "FAS_ID_FASCICOLO_SIEP, " + "ISTR_ID_ISTRUTTORIA_CUMULO, "
				+ "EVE_ID_EVENTO, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM ESITO_ARCHIVIAZIONI_CUMULO";

		// lStatement += " ( nvl(ESITO_ARCHIVIAZIONI_CUMULO.COD_OPERATORE_INSERIMENTO,'-') =
		// CODOPERATOREINSERIMENTO.RV_LOW_VALUE AND CODOPERATOREINSERIMENTO.RV_DOMAIN =
		// 'OPERATORE_INSERIMENTO' ) ";
		// lStatement += " ( nvl(ESITO_ARCHIVIAZIONI_CUMULO.COD_UFFICIO_INSERIMENTO,'-') =
		// CODUFFICIOINSERIMENTO.RV_LOW_VALUE AND CODUFFICIOINSERIMENTO.RV_DOMAIN = 'UFFICIO_INSERIMENTO' ) ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		EsitoArchiviazioniCumuloModel aModel = new EsitoArchiviazioniCumuloModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdEsitoArchiviazioniCumulo(getBigDecimal("ID_ESITO_ARCHIVIAZIONI_CUMULO"));
		aModel.setFlagArchiviato(getString("FLAG_ARCHIVIATO"));
		aModel.setDescrizioneEsito(getString("DESCRIZIONE_ESITO"));
		aModel.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		aModel.setChiaveAnnoFascSiep(getBigDecimal("CHIAVE_ANNO_FASC_SIEP"));
		aModel.setChiaveProgrFascSiep(getBigDecimal("CHIAVE_PROGR_FASC_SIEP"));
		aModel.setFasIdFascicoloSiep(getBigDecimal("FAS_ID_FASCICOLO_SIEP"));
		aModel.setIstrIdIstruttoriaCumulo(getBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(EsitoArchiviazioniCumuloModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdEsitoArchiviazioniCumulo() != null) {
			lCondizioni += " and ID_ESITO_ARCHIVIAZIONI_CUMULO = " + aModel.getIdEsitoArchiviazioniCumulo()
					+ "";
		}
		if (aModel.getFlagArchiviato() != null && aModel.getFlagArchiviato().length() > 0) {
			lCondizioni += " and FLAG_ARCHIVIATO = '" + aModel.getFlagArchiviato() + "' ";
		}
		if (aModel.getDescrizioneEsito() != null && aModel.getDescrizioneEsito().length() > 0) {
			lCondizioni += " and DESCRIZIONE_ESITO = '" + aModel.getDescrizioneEsito() + "' ";
		}
		if (aModel.getChiaveUfficio() != null && aModel.getChiaveUfficio().length() > 0) {
			lCondizioni += " and CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "' ";
		}
		if (aModel.getChiaveAnnoFascSiep() != null) {
			lCondizioni += " and CHIAVE_ANNO_FASC_SIEP = " + aModel.getChiaveAnnoFascSiep() + "";
		}
		if (aModel.getChiaveProgrFascSiep() != null) {
			lCondizioni += " and CHIAVE_PROGR_FASC_SIEP = " + aModel.getChiaveProgrFascSiep() + "";
		}
		if (aModel.getFasIdFascicoloSiep() != null) {
			lCondizioni += " and FAS_ID_FASCICOLO_SIEP = " + aModel.getFasIdFascicoloSiep() + "";
		}
		if (aModel.getIstrIdIstruttoriaCumulo() != null) {
			lCondizioni += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aModel.getIstrIdIstruttoriaCumulo() + "";
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
	public String setCondizioniByKey(BigDecimal aIdEsitoArchiviazioniCumulo) {
		String lCondizioni = new String();

		lCondizioni += " and ID_ESITO_ARCHIVIAZIONI_CUMULO = " + aIdEsitoArchiviazioniCumulo;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	public String setCondizioniByEveIdEvento(BigDecimal aIdEvento) {
		String lCondizioni = new String();

		lCondizioni += " and EVE_ID_EVENTO = " + aIdEvento;

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