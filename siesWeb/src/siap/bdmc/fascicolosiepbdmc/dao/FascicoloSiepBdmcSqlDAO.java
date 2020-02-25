package siap.bdmc.fascicolosiepbdmc.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import siap.web.ISIAPCostantiWeb;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: FascicoloSiepBdmcSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella FascicoloSiepBdmc
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
public class FascicoloSiepBdmcSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public FascicoloSiepBdmcSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountFascicoloSiepBdmc(FascicoloSiepBdmcModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM FASCICOLO_SIEP_BDMC ";

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
	public void ricercaFascicoloSiepBdmcPaged(FascicoloSiepBdmcModel aModel, int aPage) throws DAOException {
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
	public void ricercaFascicoloSiepBdmc(FascicoloSiepBdmcModel aModel) throws DAOException {
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
	public void ricercaFascicoloSiepBdmcByKey(BigDecimal aIdFascicoloBdmc) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " WHERE " + setCondizioniByKey(aIdFascicoloBdmc);

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

		lStatement += " SELECT " + "ID_FASCICOLO_BDMC, " + "CHIAVE_ANNO_BDMC, " + "CHIAVE_UFFICIO_BDMC, "
				+ "CHIAVE_PROGR_BDMC, " + "CHIAVE_ANNO_SIEP, " + "CHIAVE_UFFICIO_SIEP, "
				+ "CHIAVE_PROGR_SIEP, " + "FLAG_TRASMISSIONE, " + "DATA_TRASMISSIONE, "
				+ "DATA_DISATTIVAZIONE, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "TIPO_MISURA, " + "ID_EVENTO, "
				+ "FLAG_ORDINE_ESECUZIONE, " + "COD_COMUNE, " + "ALTRO_LUOGO, " + "ISTITUTO_DETENZIONE ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM FASCICOLO_SIEP_BDMC";

		/*
		 * lStatement +=
		 * " (     nvl(FASCICOLO_SIEP_BDMC.COD_OPERATORE_INSERIMENTO,'-') = CODOPERATOREINSERIMENTO.RV_LOW_VALUE AND CODOPERATOREINSERIMENTO.RV_DOMAIN = 'OPERATORE_INSERIMENTO' ) "
		 * ; lStatement +=
		 * " (     nvl(FASCICOLO_SIEP_BDMC.COD_UFFICIO_INSERIMENTO,'-') = CODUFFICIOINSERIMENTO.RV_LOW_VALUE AND CODUFFICIOINSERIMENTO.RV_DOMAIN = 'UFFICIO_INSERIMENTO' ) "
		 * ; lStatement +=
		 * " (     nvl(FASCICOLO_SIEP_BDMC.COD_OPERATORE_AGGIORNAMENTO,'-') = CODOPERATOREAGGIORNAMENTO.RV_LOW_VALUE AND CODOPERATOREAGGIORNAMENTO.RV_DOMAIN = 'OPERATORE_AGGIORNAMENTO' ) "
		 * ; lStatement +=
		 * " (     nvl(FASCICOLO_SIEP_BDMC.COD_UFFICIO_AGGIORNAMENTO,'-') = CODUFFICIOAGGIORNAMENTO.RV_LOW_VALUE AND CODUFFICIOAGGIORNAMENTO.RV_DOMAIN = 'UFFICIO_AGGIORNAMENTO' ) "
		 * ;
		 */

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		FascicoloSiepBdmcModel aModel = new FascicoloSiepBdmcModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFascicoloBdmc(getBigDecimal("ID_FASCICOLO_BDMC"));
		aModel.setChiaveAnnoBdmc(getBigDecimal("CHIAVE_ANNO_BDMC"));
		aModel.setChiaveUfficioBdmc(getString("CHIAVE_UFFICIO_BDMC"));
		aModel.setChiaveProgrBdmc(getBigDecimal("CHIAVE_PROGR_BDMC"));
		aModel.setChiaveAnnoSiep(getBigDecimal("CHIAVE_ANNO_SIEP"));
		aModel.setChiaveUfficioSiep(getString("CHIAVE_UFFICIO_SIEP"));
		aModel.setChiaveProgrSiep(getBigDecimal("CHIAVE_PROGR_SIEP"));
		aModel.setFlagTrasmissione(getString("FLAG_TRASMISSIONE"));
		aModel.setDataTrasmissione(getDate("DATA_TRASMISSIONE"));
		aModel.setDataDisattivazione(getDate("DATA_DISATTIVAZIONE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setTipoMisura(getString("TIPO_MISURA"));
		aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
		aModel.setCodComune(getString("COD_COMUNE"));
		aModel.setAltroLuogo(getString("ALTRO_LUOGO"));
		aModel.setFlagOrdineEsecuzione(getString("FLAG_ORDINE_ESECUZIONE"));
		aModel.setIstitutoDetenzione(getString("ISTITUTO_DETENZIONE"));

		// aModel.setDescrUfficioAggiornamento(getString("") );

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(FascicoloSiepBdmcModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdFascicoloBdmc() != null) {
			lCondizioni += " and ID_FASCICOLO_BDMC = " + aModel.getIdFascicoloBdmc() + "";
		}
		if (aModel.getChiaveAnnoBdmc() != null) {
			lCondizioni += " and CHIAVE_ANNO_BDMC = " + aModel.getChiaveAnnoBdmc() + "";
		}
		if (aModel.getChiaveUfficioBdmc() != null && aModel.getChiaveUfficioBdmc().length() > 0) {
			lCondizioni += " and CHIAVE_UFFICIO_BDMC = '" + aModel.getChiaveUfficioBdmc() + "' ";
		}
		if (aModel.getChiaveProgrBdmc() != null) {
			lCondizioni += " and CHIAVE_PROGR_BDMC = " + aModel.getChiaveProgrBdmc() + "";
		}
		if (aModel.getChiaveAnnoSiep() != null) {
			lCondizioni += " and CHIAVE_ANNO_SIEP = " + aModel.getChiaveAnnoSiep() + "";
		}
		if (aModel.getChiaveUfficioSiep() != null && aModel.getChiaveUfficioSiep().length() > 0) {
			lCondizioni += " and CHIAVE_UFFICIO_SIEP = '" + aModel.getChiaveUfficioSiep() + "' ";
		}
		if (aModel.getChiaveProgrSiep() != null) {
			lCondizioni += " and CHIAVE_PROGR_SIEP = " + aModel.getChiaveProgrSiep() + "";
		}
		if (aModel.getFlagTrasmissione() != null && aModel.getFlagTrasmissione().length() > 0) {
			lCondizioni += " and FLAG_TRASMISSIONE = '" + aModel.getFlagTrasmissione() + "' ";
		}
		if (aModel.getDataTrasmissione() != null) {
			lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataTrasmissione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataDisattivazione() != null) {
			lCondizioni += " and to_char(DATA_DISATTIVAZIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataDisattivazione(), "dd/MM/yyyy") + "' ";
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
		if (aModel.getTipoMisura() != null && aModel.getTipoMisura().length() > 0) {
			lCondizioni += " and TIPO_MISURA = '" + aModel.getTipoMisura() + "' ";
		}
		if (aModel.getIdEvento() != null) {
			lCondizioni += " and ID_EVENTO = " + aModel.getIdEvento() + "";
		}
		if (aModel.getCodComune() != null && aModel.getCodComune().length() > 0) {
			lCondizioni += " and COD_COMUNE = '" + aModel.getCodComune() + "' ";
		}
		if (aModel.getAltroLuogo() != null && aModel.getAltroLuogo().length() > 0) {
			lCondizioni += " and ALTRO_LUOGO = '" + aModel.getAltroLuogo() + "' ";
		}
		if (aModel.getIstitutoDetenzione() != null && aModel.getIstitutoDetenzione().length() > 0) {
			lCondizioni += " and ISTITUTO_DETENZIONE = '" + aModel.getIstitutoDetenzione() + "' ";
		}
		if (aModel.getFlagOrdineEsecuzione() != null && aModel.getFlagOrdineEsecuzione().length() > 0) {
			lCondizioni += " and FLAG_ORDINE_ESECUZIONE = '" + aModel.getFlagOrdineEsecuzione() + "' ";
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
	public String setCondizioniByKey(BigDecimal aIdFascicoloBdmc) {

		String lCondizioni = new String();

		lCondizioni += " and ID_FASCICOLO_BDMC = " + aIdFascicoloBdmc;

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