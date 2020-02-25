package siap.siep.modulocumulo.dao;

/**
* <p>Title: LibAnticipataCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella LibAnticipataCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;

public class LibAnticipataCumuloSqlDAO extends SqlDAO {

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public LibAnticipataCumuloSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountLibAnticipataCumulo(LibAnticipataCumuloModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM LIB_ANTICIPATA_CUMULO ";

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
	public void ricercaLibAnticipataCumuloPaged(LibAnticipataCumuloModel aModel, int aPage)
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
	public void ricercaLibAnticipataCumulo(LibAnticipataCumuloModel aModel) throws DAOException {
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
	public void ricercaLibAnticipataCumuloByKey(BigDecimal aIdLibAnticipataCumulo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdLibAnticipataCumulo);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaLibAnticipataCumuloByIdStatoEsec(BigDecimal aIdStatoEsec) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition
		lSql += " WHERE " + setCondizioniByKeyIdStatoEsec(aIdStatoEsec);

		// lSql += " ORDER BY DATA_RECLUSIONE_DA ASC";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaLibAnticipataCumuloByIdStatoEsec(BigDecimal aIdStatoEsec, List<String> aListaFlagConc)
			throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition
		lSql += " WHERE " + setCondizioniByKeyIdStatoEsec(aIdStatoEsec);

		// lSql += " ORDER BY DATA_RECLUSIONE_DA ASC";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaLibAnticipataCumuloByIdTitolo(BigDecimal aIdTitolo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition
		lSql += " WHERE " + setCondizioniByKeyIdTitolo(aIdTitolo);

		// lSql += " ORDER BY DATA_RECLUSIONE_DA ASC";

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

		lStatement += " SELECT " + "ID_LIB_ANTICIPATA_CUMULO, "
				+ "COD_TIPO_LICENZA, CODTIPOLIC.RV_MEANING DESC_TIPO_LICENZA, " + "NUMERO_GIORNI, "
				+ "SOMMA_RISARC_DANNI, " + "FLAG_CONCESSO, " + "TIPO_LA, " + "FLAG_ELABORATO, "
				+ "FLAG_STATO, " + "MOTIVO_MODIFICA, " + "TIT_ID_TITOLO_CUMULATO, "
				+ "ID_LIBANTICIPATA_ORIGINE, " + "STAT_ID_STATO_ESEC_TIT_CUM, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";

		lStatement += " FROM LIB_ANTICIPATA_CUMULO";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODTIPOLIC ON CODTIPOLIC.RV_LOW_VALUE = COD_TIPO_LICENZA "
				+ " AND CODTIPOLIC.RV_DOMAIN = 'TIPO_LICENZA' ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		LibAnticipataCumuloModel aModel = new LibAnticipataCumuloModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdLibAnticipataCumulo(getBigDecimal("ID_LIB_ANTICIPATA_CUMULO"));
		aModel.setCodTipoLicenza(getString("COD_TIPO_LICENZA"));
		aModel.setDescrTipoLicenza(getString("DESC_TIPO_LICENZA"));
		aModel.setNumeroGiorni(getBigDecimal("NUMERO_GIORNI"));
		aModel.setSommaRisarcDanni(getBigDecimal("SOMMA_RISARC_DANNI"));
		aModel.setFlagConcesso(getString("FLAG_CONCESSO"));
		aModel.setTipoLa(getString("TIPO_LA"));
		aModel.setFlagElaborato(getString("FLAG_ELABORATO"));
		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setMotivoModifica(getString("MOTIVO_MODIFICA"));
		aModel.setTitIdTitoloCumulato(getBigDecimal("TIT_ID_TITOLO_CUMULATO"));
		aModel.setIdLibanticipataOrigine(getBigDecimal("ID_LIBANTICIPATA_ORIGINE"));
		aModel.setStatIdStatoEsecTitoloCum(getBigDecimal("STAT_ID_STATO_ESEC_TIT_CUM"));
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
	public String setCondizioni(LibAnticipataCumuloModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdLibAnticipataCumulo() != null) {
			lCondizioni += " and ID_LIB_ANTICIPATA_CUMULO = " + aModel.getIdLibAnticipataCumulo() + "";
		}
		if (aModel.getCodTipoLicenza() != null && aModel.getCodTipoLicenza().length() > 0) {
			lCondizioni += " and COD_TIPO_LICENZA = '" + aModel.getCodTipoLicenza() + "' ";
		}
		if (aModel.getNumeroGiorni() != null) {
			lCondizioni += " and NUMERO_GIORNI = " + aModel.getNumeroGiorni() + "";
		}
		if (aModel.getSommaRisarcDanni() != null) {
			lCondizioni += " and SOMMA_RISARC_DANNI = " + aModel.getSommaRisarcDanni() + "";
		}
		if (aModel.getFlagConcesso() != null && aModel.getFlagConcesso().length() > 0) {
			lCondizioni += " and FLAG_CONCESSO = '" + aModel.getFlagConcesso() + "' ";
		}
		if (aModel.getTipoLa() != null && aModel.getTipoLa().length() > 0) {
			lCondizioni += " and TIPO_LA = '" + aModel.getTipoLa() + "' ";
		}
		if (aModel.getFlagElaborato() != null && aModel.getFlagElaborato().length() > 0) {
			lCondizioni += " and FLAG_ELABORATO = '" + aModel.getFlagElaborato() + "' ";
		}
		if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) {
			lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' ";
		}
		if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) {
			lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' ";
		}
		if (aModel.getTitIdTitoloCumulato() != null) {
			lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + "";
		}
		if (aModel.getIdLibanticipataOrigine() != null) {
			lCondizioni += " and ID_LIBANTICIPATA_ORIGINE = " + aModel.getIdLibanticipataOrigine() + "";
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
	public String setCondizioniByKey(BigDecimal aIdLibAnticipataCumulo) {
		String lCondizioni = new String();

		lCondizioni += " and ID_LIB_ANTICIPATA_CUMULO = " + aIdLibAnticipataCumulo;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	public String setCondizioniByKeyIdStatoEsec(BigDecimal aIdStatoEsec) {
		String lCondizioni = new String();

		lCondizioni += " and STAT_ID_STATO_ESEC_TIT_CUM = " + aIdStatoEsec;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	public String setCondizioniByKeyIdStatoEsecFlagConc(BigDecimal aIdStatoEsec,
			List<String> aListaFlagConc) {
		String lCondizioni = new String();

		lCondizioni += " and STAT_ID_STATO_ESEC_TIT_CUM = " + aIdStatoEsec;
		if (aListaFlagConc != null) {
			lCondizioni += " AND (( COD_MOTIVO IN (";
			for (int i = 0; i < aListaFlagConc.size(); i++) {
				lCondizioni += "'" + aListaFlagConc.get(i) + "'";
				if (i != aListaFlagConc.size() - 1)
					lCondizioni += ",";
			}
			lCondizioni += ") ";
		}

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		return lCondizioni;
	}

	public String setCondizioniByKeyIdTitolo(BigDecimal aIdTitolo) {
		String lCondizioni = new String();

		lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;

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