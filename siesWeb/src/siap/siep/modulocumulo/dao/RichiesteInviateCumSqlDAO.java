package siap.siep.modulocumulo.dao;

/**
* <p>Title: RichiesteInviateCumSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Richieste_Inviate_Cum</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;

public class RichiesteInviateCumSqlDAO extends SqlDAO {
	/*****************************************************************************
	 * Costruttore
	 *
	 * @param con
	 ****************************************************************************/
	public RichiesteInviateCumSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountRichiesteInViateCum(RichiesteInviateCumModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM RICHIESTE_INVIATE_CUM ";

		// Recupero la where condition in base al model
		/* String lCondizioni = */ this.setCondizioni(aModel);

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
	public void ricercaRichiesteInviateCumPaged(RichiesteInviateCumModel aModel, int aPage)
			throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		/* String lCondizioni = */this.setCondizioni(aModel);

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
	public void ricercaRichiesteInviateCum(RichiesteInviateCumModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		/* String lCondizioni = */setCondizioni(aModel);

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaRichiesteInviateCumByIdIstruttoria(BigDecimal aIdIstru) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();
		lSql += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aIdIstru;
		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaRichiesteInviateCumByIdIstruttoriaTipoRichiesta(BigDecimal aIdIstruttoria,
			String aCodTipoRichiesta) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();
		lSql += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aIdIstruttoria;
		lSql += " and ID_RICHIESTE_INVIATE_CUM in (Select RIC_ID_RICHIESTE_INVIATE_CUM from  RICHIESTE_PM_IN_CUMULO where COD_TIPO_RICHIESTA ='"
				+ aCodTipoRichiesta + "' )";
		lSql += " " + getOrderByDataEmi() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 *
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaRichiesteInviateCumByKey(BigDecimal aIdRichiesteInviateCum) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		// lSql += " WHERE " + setCondizioniByKey( aIdRichiestePmInCumulo);
		lSql += setCondizioniByKey(aIdRichiesteInviateCum);

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

		lStatement += " SELECT " + "ID_RICHIESTE_INVIATE_CUM, " + "DATA_EMISSIONE, " + "DATA_TRASMISSIONE, "
				+ "COD_MAGISTRATO, " + "CONTENUTO,  "
				+ "COD_UFFICIO_DEST, CODUFFICIO.RV_MEANING DESCR_UFFICIO, " + // TIPOUFF.RV_MEANING
																				// DESCR_UFFICIO,
				"COD_LUOGO_DEST, COMUNE.DESCRIZIONE DESCR_SEDE_UFFICIO, " + // COMUNE.DESCRIZIONE
																			// DESCR_SEDE_UFFICIO, "+
				"ISTR_ID_ISTRUTTORIA_CUMULO, " + "DOC_BLOB, " + "FLAG_VALIDATO, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";

		lStatement += " FROM RICHIESTE_INVIATE_CUM";
		// lStatement += " LEFT OUTER JOIN UFFICIO ON UFFICIO.COD_UFFICIO = COD_UFFICIO_DEST ";
		// lStatement += " LEFT JOIN CG_REF_CODES TIPOUFF ON TIPOUFF.RV_LOW_VALUE = UFFICIO.COD_TIPO_UFFICIO
		// ";
		// lStatement += " AND TIPOUFF.RV_DOMAIN = 'TIPO_UFFICIO' ";

		// lStatement += " LEFT OUTER JOIN COMUNE ON COMUNE.COD_COMUNE = COD_LUOGO_DEST ";

		lStatement += " , CG_REF_CODES CODUFFICIO, UFFICIO ";
		lStatement += " , COMUNE ";

		lStatement += " WHERE 1=1 ";
		// lStatement += " AND ( nvl(UFFICIO.COD_UFFICIO,'-') = COD_UFFICIO_DEST ) ";
		lStatement += " AND ( nvl(COD_UFFICIO_DEST,'-') = UFFICIO.COD_UFFICIO ) ";
		lStatement += " AND ( nvl(CODUFFICIO.RV_LOW_VALUE,'-') = UFFICIO.COD_TIPO_UFFICIO AND CODUFFICIO.RV_DOMAIN = 'TIPO_UFFICIO' ) ";
		// lStatement += " AND ( nvl(COMUNE.COD_COMUNE,'-') = COD_LUOGO_DEST ) ";
		lStatement += " AND ( nvl(COD_LUOGO_DEST,'-') = COMUNE.COD_COMUNE ) ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 *
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		RichiesteInviateCumModel aModel = new RichiesteInviateCumModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdRichiesteInviateCum(getBigDecimal("ID_RICHIESTE_INVIATE_CUM"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setDataTrasmissione(getDate("DATA_TRASMISSIONE"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		aModel.setContenuto(getString("CONTENUTO"));
		aModel.setCodUfficioDest(getString("COD_UFFICIO_DEST"));
		aModel.setDescrUfficioDest(getString("DESCR_UFFICIO"));
		aModel.setCodLuogoDest(getString("COD_LUOGO_DEST"));
		aModel.setDescrLuogoDest(getString("DESCR_SEDE_UFFICIO"));
		aModel.setIstrIdIstruttoriaCumulo(getBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO"));
		aModel.setFlagDocValidato(getString("FLAG_VALIDATO"));

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
	public String setCondizioni(RichiesteInviateCumModel aModel) {
		String lCondizioni = new String();

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
	public String setCondizioniByKey(BigDecimal aIdRichiesteInviateCum) {
		String lCondizioni = new String();

		lCondizioni += " and ID_RICHIESTE_INVIATE_CUM = " + aIdRichiesteInviateCum;

		// Elimino il primo and
		// if (lCondizioni.length() > 0) {
		// lCondizioni = lCondizioni.substring(4);
		// }

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

	protected String getOrderByDataEmi() {
		String orderBy = new String("");
		orderBy = " ORDER BY DATA_EMISSIONE DESC";
		return orderBy;
	}

}