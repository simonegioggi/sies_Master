package siap.siep.tipoeventibdmc.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel;
import siap.web.ISIAPCostantiWeb;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: TipoEventiBdmcSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella TipoEventiBdmc
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
public class TipoEventiBdmcSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public TipoEventiBdmcSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountTipoEventiBdmc(TipoEventiBdmcModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM TIPO_EVENTI_BDMC ";

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
	public void ricercaTipoEventiBdmcPaged(TipoEventiBdmcModel aModel, int aPage) throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		lStatement += " " + getOrderBy() + " ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * ISIAPCostantiWeb.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * ISIAPCostantiWeb.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaTipoEventiBdmc(TipoEventiBdmcModel aModel) throws DAOException {
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
	public void ricercaTipoEventiBdmcByKey(BigDecimal aIdTipoEventiBdmc) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdTipoEventiBdmc);

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

		lStatement += " SELECT " + "ID_TIPO_EVENTI_BDMC, " + "COD_TIPO_EVENTO, " + "COD_PROVVEDIMENTO, "
				+ "COD_MOTIVO ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM TIPO_EVENTI_BDMC";

		// lStatement +=
		// " (     nvl(TIPO_EVENTI_BDMC.COD_TIPO_EVENTO,'-') = CODTIPOEVENTO.RV_LOW_VALUE AND CODTIPOEVENTO.RV_DOMAIN = 'TIPO_EVENTO' ) "
		// ;
		// lStatement +=
		// " (     nvl(TIPO_EVENTI_BDMC.COD_PROVVEDIMENTO,'-') = CODPROVVEDIMENTO.RV_LOW_VALUE AND CODPROVVEDIMENTO.RV_DOMAIN = 'PROVVEDIMENTO' ) "
		// ;
		// lStatement +=
		// " (     nvl(TIPO_EVENTI_BDMC.COD_MOTIVO,'-') = CODMOTIVO.RV_LOW_VALUE AND CODMOTIVO.RV_DOMAIN = 'MOTIVO' ) "
		// ;

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		TipoEventiBdmcModel aModel = new TipoEventiBdmcModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdTipoEventiBdmc(getBigDecimal("ID_TIPO_EVENTI_BDMC"));
		aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		// aModel.setDescrTipoEvento(getString("") );
		aModel.setCodProvvedimento(getString("COD_PROVVEDIMENTO"));
		// aModel.setDescrProvvedimento(getString("") );
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		// aModel.setDescrMotivo(getString("") );

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(TipoEventiBdmcModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdTipoEventiBdmc() != null) {
			lCondizioni += " and ID_TIPO_EVENTI_BDMC = " + aModel.getIdTipoEventiBdmc() + "";
		}
		if (aModel.getCodTipoEvento() != null && aModel.getCodTipoEvento().length() > 0) {
			lCondizioni += " and COD_TIPO_EVENTO = '" + aModel.getCodTipoEvento() + "' ";
		}
		if (aModel.getCodProvvedimento() != null && aModel.getCodProvvedimento().length() > 0) {
			lCondizioni += " and COD_PROVVEDIMENTO = '" + aModel.getCodProvvedimento() + "' ";
		}
		if (aModel.getCodMotivo() != null && aModel.getCodMotivo().length() > 0) {
			lCondizioni += " and COD_MOTIVO = '" + aModel.getCodMotivo() + "' ";
		}
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		logger.info("lCondizioni = " + lCondizioni);
		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di select per chiave
	 * 
	 * @param aKey
	 * @return
	 ****************************************************************************/
	public String setCondizioniByKey(BigDecimal aIdTipoEventiBdmc) {

		String lCondizioni = new String();

		lCondizioni += " and ID_TIPO_EVENTI_BDMC = " + aIdTipoEventiBdmc;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		logger.info("lCondizioni = " + lCondizioni);

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