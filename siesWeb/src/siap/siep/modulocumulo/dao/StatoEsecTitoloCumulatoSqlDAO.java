package siap.siep.modulocumulo.dao;

/**
* <p>Title: StatoEsecTitoloCumulatoSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella StatoEsecTitoloCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;

public class StatoEsecTitoloCumulatoSqlDAO extends SqlDAO {

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecTitoloCumulatoSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountStatoEsecTitoloCumulato(StatoEsecTitoloCumulatoModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM STATO_ESEC_TITOLO_CUMULATO ";

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
	public void ricercaStatoEsecTitoloCumulatoPaged(StatoEsecTitoloCumulatoModel aModel, int aPage)
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
	public void ricercaStatoEsecTitoloCumulato(StatoEsecTitoloCumulatoModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += lCondizioni;

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
	public void ricercaStatoEsecTitoloCumulatoByKey(BigDecimal aIdStatoEsecTitoloCumulato)
			throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += setCondizioniByKey(aIdStatoEsecTitoloCumulato);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaStatoEsecTitoloCumulatoByIdTitolo(BigDecimal aIdTitolo, List<String> aListaCodMotivo)
			throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;

		if (aListaCodMotivo != null) {
			lSql += " AND COD_MOTIVO IN (";
			for (int i = 0; i < aListaCodMotivo.size(); i++) {
				lSql += "'" + aListaCodMotivo.get(i) + "'";
				if (i != aListaCodMotivo.size() - 1)
					lSql += ",";
			}
			lSql += ") ";
		}

		lSql += " ORDER BY DATA_EMISSIONE ASC, ID_EVENTO_ORIGINE ASC ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaStatoEsecTitoloCumulatoByIdTitolo(BigDecimal aIdTitolo, List<String> aListaCodMotivo,
			BigDecimal aIdRichiesta) throws DAOException {
		// Recupero della select...from
		String lSql = getSqlQuery();

		// Si aggiunge le where condition per chiave
		lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;

		if (aListaCodMotivo != null) {
			lSql += " AND COD_MOTIVO IN (";
			for (int i = 0; i < aListaCodMotivo.size(); i++) {
				lSql += "'" + aListaCodMotivo.get(i) + "'";
				if (i != aListaCodMotivo.size() - 1)
					lSql += ",";
			}
			lSql += ") ";
		}
		// Si aggiunge la condition per IdRichiesta
		lSql += " and ID_STATO_ESEC_TITOLO_CUMULATO in (Select STAT_ID_STATO_ESEC_TITOLO_CUM from RICHPM_STATO_ESEC_CUM where RIC_ID_RICHIESTE_PM_IN_CUMULO = "
				+ aIdRichiesta + " ) ";

		lSql += " ORDER BY DATA_EMISSIONE ASC, ID_EVENTO_ORIGINE ASC ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaStatoEsecTitoloCumulatoByIdTitolo(BigDecimal aIdTitolo, List<String> aListaCodMotivo,
			List<String> aListaEsitiEsclusi, List<String> aListaCodMotivoBis,
			List<String> aListaEsitiEsclusiBis) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;

		if (aListaCodMotivo != null) {
			lSql += " AND (( COD_MOTIVO IN (";
			for (int i = 0; i < aListaCodMotivo.size(); i++) {
				lSql += "'" + aListaCodMotivo.get(i) + "'";
				if (i != aListaCodMotivo.size() - 1)
					lSql += ",";
			}
			lSql += ") ";
		}
		if (aListaEsitiEsclusi != null) {
			lSql += " AND COD_ESITO NOT IN (";
			for (int i = 0; i < aListaEsitiEsclusi.size(); i++) {
				lSql += "'" + aListaEsitiEsclusi.get(i) + "'";
				if (i != aListaEsitiEsclusi.size() - 1)
					lSql += ",";
			}
			lSql += ")) ";
		}

		if (aListaCodMotivoBis != null) {
			lSql += " OR ( COD_MOTIVO IN (";
			for (int i = 0; i < aListaCodMotivoBis.size(); i++) {
				lSql += "'" + aListaCodMotivoBis.get(i) + "'";
				if (i != aListaCodMotivoBis.size() - 1)
					lSql += ",";
			}
			lSql += ") ";
		}
		if (aListaEsitiEsclusiBis != null) {
			lSql += " AND COD_ESITO NOT IN (";
			for (int i = 0; i < aListaEsitiEsclusiBis.size(); i++) {
				lSql += "'" + aListaEsitiEsclusiBis.get(i) + "'";
				if (i != aListaEsitiEsclusiBis.size() - 1)
					lSql += ",";
			}
			lSql += ")) ";
		}
		if (aListaCodMotivo != null)
			lSql += ") ";

		lSql += " ORDER BY DATA_EMISSIONE ASC, ID_EVENTO_ORIGINE ASC ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaStatoEsecTitoloCumulatoByIdTitolo(BigDecimal aIdTitolo, List<String> aListaCodMotivo,
			List<String> aListaEsitiEsclusi) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;

		if (aListaCodMotivo != null) {
			lSql += " AND COD_MOTIVO IN (";
			for (int i = 0; i < aListaCodMotivo.size(); i++) {
				lSql += "'" + aListaCodMotivo.get(i) + "'";
				if (i != aListaCodMotivo.size() - 1)
					lSql += ",";
			}
			lSql += ") ";
		}
		if (aListaEsitiEsclusi != null) {
			lSql += " AND COD_ESITO NOT IN (";
			for (int i = 0; i < aListaEsitiEsclusi.size(); i++) {
				lSql += "'" + aListaEsitiEsclusi.get(i) + "'";
				if (i != aListaEsitiEsclusi.size() - 1)
					lSql += ",";
			}
			lSql += ") ";
		}
		lSql += " AND COD_TIPO_PROVVEDIMENTO IN ('02','03') ";

		lSql += " ORDER BY DATA_EMISSIONE ASC, ID_EVENTO_ORIGINE ASC ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	//
	public void ricercaStatoEsecTitoloCumulatobylistaProvv(StatoEsecTitoloCumulatoModel aModel,
			Vector<String> alistaProvv) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (alistaProvv != null && alistaProvv.size() > 0) {
			lCondizioni += " AND COD_MOTIVO in (";
			for (int i = 0; i < alistaProvv.size(); i++) {
				lCondizioni += " '" + alistaProvv.elementAt(i) + "'";
				if (i < alistaProvv.size() - 1)
					lCondizioni += ",";
			}

			lCondizioni += " ) ";
		}

		if (!lCondizioni.trim().equals(""))
			lSql += lCondizioni;

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	//
	public void ricercaStatoEsecTitoloCumulatobylisteTipoMotivoProvv(StatoEsecTitoloCumulatoModel aModel,
			Vector<String> alistaTipoProvv, Vector<String> alistaProvv) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (alistaTipoProvv != null && alistaTipoProvv.size() > 0) {
			lCondizioni += " AND COD_TIPO_PROVVEDIMENTO in (";
			for (int i = 0; i < alistaTipoProvv.size(); i++) {
				lCondizioni += " '" + alistaTipoProvv.elementAt(i) + "'";
				if (i < alistaTipoProvv.size() - 1)
					lCondizioni += ",";
			}

			lCondizioni += " ) ";
		}

		if (alistaProvv != null && alistaProvv.size() > 0) {
			lCondizioni += " AND COD_MOTIVO in (";
			for (int i = 0; i < alistaProvv.size(); i++) {
				lCondizioni += " '" + alistaProvv.elementAt(i) + "'";
				if (i < alistaProvv.size() - 1)
					lCondizioni += ",";
			}

			lCondizioni += " ) ";
		}

		if (!lCondizioni.trim().equals(""))
			lSql += lCondizioni;

		lSql += " " + getOrderBy() + " ";

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

		lStatement += " SELECT " + "ID_STATO_ESEC_TITOLO_CUMULATO, "
				+ "COD_TIPO_EVENTO, COD_TIPO_PROVVEDIMENTO, COD_MOTIVO, " +

				"CODTIPOEVENTO.RV_MEANING DESC_TIPO_EVENTO, "
				+ "CODTIPOPROVVEDIMENTO.RV_MEANING DESC_TIPO_PROVVEDIMENTO, "
				+ "CODMOTIVO.RV_MEANING ||' '|| NVL(ltrim(CODMOTIVO.RV_ALT5_VALUE), '') DESC_MOTIVO, " + // 23/07/2018
																											// Aggregazione
																											// del
																											// suffisso
																											// alla
																											// descrizione
																											// MOTIVO.

				"COD_MOTIVO_REVOCA, CODMOTIVOREV.RV_MEANING DESC_MOTIVO_REV, "
				+ "COD_MOTIVO_REVOCA_PM, CODMOTIVOREVPM.RV_MEANING DESC_MOTIVO_REV_PM, " +

				"COD_UFFICIO_EMITTENTE, TIPOUFF.RV_MEANING DESC_UFF_EMITTENTE, "
				+ "COD_AUTORITA_EMITTENTE, TIPOAUT.RV_MEANING DESC_AUT_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, COMUNE.DESCRIZIONE DESC_LUOGO_EMITTENTE, " + "DATA_EMISSIONE, " +

				"COD_ESITO, " + "CODESITO.RV_MEANING DESC_ESITO, " + "COD_ESITO_TENORE, "
				+ "CODESITOTENORE.RV_MEANING DESC_ESITO_TENORE, " + "ANNO_PROCEDIMENTO, PROGR_PROCEDIMENTO, "
				+ "ANNO_PROVVEDIMENTO, PROGR_PROVVEDIMENTO, " + "NOTE, " +

				"COD_CONTENUTO_ISTANZA, CODCONTENUTOISTANZA.RV_MEANING DESC_CONTENUTO_ISTANZA, "
				+ "DATA_ISTANZA, " + "FLAG_ISTANZA_PRESDEP, "
				+ "COD_STATO_ISTANZA, CODSTATOISTANZA.RV_MEANING DESC_STATO_ISTANZA, "
				+ "COD_TIPO_UFFICIO_DESTINATARIO, CODTIPOUFFDEST.RV_MEANING DESC_TIPO_UFF_DEST, "
				+ "COD_LUOGO_DESTINATARIO, COMUNEUFFDEST.DESCRIZIONE DESC_LUOGO_DESTINATARIO,  "
				+ "COD_UFFICIO_DESTINATARIO, " + "DATA_TRASMISSIONE, " +

				"FLAG_TIPO_SOSP, " +

				"COD_TIPO_ISTANTE, " + "COD_TIPO_UFFICIO_ALTRO, CODTIPOUFFALTRO.RV_MEANING DESCUFFALTRO, "
				+ "COD_TIPO_AUTORITA_ALTRO, TIPOAUTALTRO.RV_MEANING DESCAUTALTRO, "
				+ "COD_LUOGO_ALTRO, COMUNELUOGOALTRO.DESCRIZIONE DESCLUOGOALTRO, " + "COD_UFFICIO_ALTRO, "
				+ "SEZIONE_ALTRO, " + "DATA_EMISSIONE_ALTRO, " +

				"TIT_ID_TITOLO_CUMULATO, " + "ISTR_ID_ISTRUTTORIA_CUMULO, " + "FLAG_STATO, "
				+ "MOTIVO_MODIFICA, " + "ID_EVENTO_ORIGINE, " + "EVE_ID_EVENTO_ORIGINE, " +

				"COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM STATO_ESEC_TITOLO_CUMULATO ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODTIPOEVENTO ON CODTIPOEVENTO.RV_LOW_VALUE = COD_TIPO_EVENTO "
				+ " AND CODTIPOEVENTO.RV_DOMAIN = 'TIPO_EVENTO' ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODTIPOPROVVEDIMENTO ON CODTIPOPROVVEDIMENTO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO "
				+ " AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODMOTIVO ON CODMOTIVO.RV_LOW_VALUE = COD_MOTIVO "
				+ " AND CODMOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";

		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODMOTIVOREV ON CODMOTIVOREV.RV_LOW_VALUE = COD_MOTIVO_REVOCA "
				+ " AND CODMOTIVOREV.RV_DOMAIN = 'REVOCA_DECSOSP' ";

		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODMOTIVOREVPM ON CODMOTIVOREVPM.RV_LOW_VALUE = COD_MOTIVO_REVOCA_PM "
				+ " AND CODMOTIVOREVPM.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";

		lStatement += " LEFT OUTER JOIN UFFICIO ON UFFICIO.COD_UFFICIO = COD_UFFICIO_EMITTENTE ";
		lStatement += " LEFT JOIN CG_REF_CODES TIPOUFF ON TIPOUFF.RV_LOW_VALUE = UFFICIO.COD_TIPO_UFFICIO ";
		lStatement += " AND TIPOUFF.RV_DOMAIN = 'TIPO_UFFICIO'   ";

		lStatement += " LEFT JOIN CG_REF_CODES TIPOAUT ON TIPOAUT.RV_LOW_VALUE = COD_AUTORITA_EMITTENTE ";
		lStatement += " AND TIPOAUT.RV_DOMAIN = 'TIPO_AUTORITA'   ";

		lStatement += " LEFT OUTER JOIN COMUNE ON COMUNE.COD_COMUNE = COD_LUOGO_EMITTENTE ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODESITO ON CODESITO.RV_LOW_VALUE = COD_ESITO "
				+ " AND CODESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODESITOTENORE ON CODESITOTENORE.RV_LOW_VALUE = COD_ESITO_TENORE "
				+ " AND CODESITOTENORE.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' ";

		// =====================
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODCONTENUTOISTANZA ON CODCONTENUTOISTANZA.RV_LOW_VALUE = COD_CONTENUTO_ISTANZA "
				+ " AND CODCONTENUTOISTANZA.RV_DOMAIN = 'CONTENUTO_ISTANZA' ";

		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODSTATOISTANZA ON CODSTATOISTANZA.RV_LOW_VALUE = COD_STATO_ISTANZA "
				+ " AND CODSTATOISTANZA.RV_DOMAIN = 'CONTENUTO_ISTANZA' ";

		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODTIPOUFFDEST ON CODTIPOUFFDEST.RV_LOW_VALUE = COD_TIPO_UFFICIO_DESTINATARIO "
				+ " AND CODTIPOUFFDEST.RV_DOMAIN = 'TIPO_UFFICIO' ";

		lStatement += " LEFT OUTER JOIN COMUNE COMUNEUFFDEST ON COMUNEUFFDEST.COD_COMUNE = COD_LUOGO_DESTINATARIO ";
		// =======================
		lStatement += " LEFT OUTER JOIN COMUNE COMUNELUOGOALTRO ON COMUNELUOGOALTRO.COD_COMUNE = COD_LUOGO_ALTRO ";

		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODTIPOUFFALTRO ON CODTIPOUFFALTRO.RV_LOW_VALUE = COD_TIPO_UFFICIO_ALTRO "
				+ " AND CODTIPOUFFALTRO.RV_DOMAIN = 'TIPO_UFFICIO' ";

		lStatement += " LEFT JOIN CG_REF_CODES TIPOAUTALTRO ON TIPOAUTALTRO.RV_LOW_VALUE = COD_TIPO_AUTORITA_ALTRO ";
		lStatement += " AND TIPOAUTALTRO.RV_DOMAIN = 'TIPO_AUTORITA'   ";

		lStatement += " WHERE 1=1 ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		StatoEsecTitoloCumulatoModel aModel = new StatoEsecTitoloCumulatoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdStatoEsecTitoloCumulato(getBigDecimal("ID_STATO_ESEC_TITOLO_CUMULATO"));
		aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		aModel.setDescrTipoEvento(getString("DESC_TIPO_EVENTO"));
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setDescrTipoProvvedimento(getString("DESC_TIPO_PROVVEDIMENTO"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setDescrMotivo(getString("DESC_MOTIVO"));

		aModel.setCodMotivoRevoca(getString("COD_MOTIVO_REVOCA"));
		aModel.setDescrMotivoRevoca(getString("DESC_MOTIVO_REV"));
		aModel.setCodMotivoRevocaPm(getString("COD_MOTIVO_REVOCA_PM"));
		aModel.setDescrMotivoRevocaPm(getString("DESC_MOTIVO_REV_PM"));

		aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		aModel.setDescrUfficioEmittente(getString("DESC_UFF_EMITTENTE"));
		aModel.setCodAutoritaEmittente(getString("COD_AUTORITA_EMITTENTE"));
		aModel.setDescrAutoritaEmittente(getString("DESC_AUT_EMITTENTE"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("DESC_LUOGO_EMITTENTE"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));

		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("DESC_ESITO"));
		aModel.setCodEsitoTenore(getString("COD_ESITO_TENORE"));
		aModel.setDescrEsitoTenore(getString("DESC_ESITO_TENORE"));
		aModel.setAnnoProcedimento(getBigDecimal("ANNO_PROCEDIMENTO"));
		aModel.setProgrProcedimento(getBigDecimal("PROGR_PROCEDIMENTO"));
		aModel.setAnnoProvvedimento(getBigDecimal("ANNO_PROVVEDIMENTO"));
		aModel.setProgrProvvedimento(getBigDecimal("PROGR_PROVVEDIMENTO"));
		aModel.setNote(getString("NOTE"));

		aModel.setCodContenutoIstanza(getString("COD_CONTENUTO_ISTANZA"));
		aModel.setDescrContenutoIstanza(getString("DESC_CONTENUTO_ISTANZA"));
		aModel.setDataIstanza(getDate("DATA_ISTANZA"));
		aModel.setFlagIstanzaPresdep(getString("FLAG_ISTANZA_PRESDEP"));
		aModel.setCodStatoIstanza(getString("COD_STATO_ISTANZA"));
		aModel.setDescrStatoIstanza(getString("DESC_STATO_ISTANZA"));
		aModel.setCodTipoUfficioDestinatario(getString("COD_TIPO_UFFICIO_DESTINATARIO"));
		aModel.setDescrTipoUfficioDestinatario(getString("DESC_TIPO_UFF_DEST"));
		aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
		aModel.setDescrLuogoDestinatario(getString("DESC_LUOGO_DESTINATARIO"));
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		aModel.setDataTrasmissione(getDate("DATA_TRASMISSIONE"));

		aModel.setFlagTipoSosp(getString("FLAG_TIPO_SOSP"));

		aModel.setCodTipoIstante(getString("COD_TIPO_ISTANTE"));
		aModel.setCodTipoUfficioAltro(getString("COD_TIPO_UFFICIO_ALTRO"));
		aModel.setDescrTipoUfficioAltro(getString("DESCUFFALTRO"));
		aModel.setCodTipoAutoritaAltro(getString("COD_TIPO_AUTORITA_ALTRO"));
		aModel.setDescrTipoAutoritaAltro(getString("DESCAUTALTRO"));
		aModel.setCodLuogoAltro(getString("COD_LUOGO_ALTRO"));
		aModel.setDescrLuogoAltro(getString("DESCLUOGOALTRO"));
		aModel.setCodUfficioAltro(getString("COD_UFFICIO_ALTRO"));
		aModel.setSezioneAltro(getString("SEZIONE_ALTRO"));
		aModel.setDataEmissioneAltro(getDate("DATA_EMISSIONE_ALTRO"));

		aModel.setTitIdTitoloCumulato(getBigDecimal("TIT_ID_TITOLO_CUMULATO"));
		aModel.setIstrIdIstruttoriaCumulo(getBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO"));
		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setMotivoModifica(getString("MOTIVO_MODIFICA"));
		aModel.setIdEventoOrigine(getBigDecimal("ID_EVENTO_ORIGINE"));
		aModel.setEveIdEventoOrigine(getBigDecimal("EVE_ID_EVENTO_ORIGINE"));

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
	public String setCondizioni(StatoEsecTitoloCumulatoModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdStatoEsecTitoloCumulato() != null) {
			lCondizioni += " and ID_STATO_ESEC_TITOLO_CUMULATO = " + aModel.getIdStatoEsecTitoloCumulato()
					+ "";
		}
		if (aModel.getCodTipoEvento() != null && aModel.getCodTipoEvento().length() > 0) {
			lCondizioni += " and COD_TIPO_EVENTO = '" + aModel.getCodTipoEvento() + "' ";
		}
		if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) {
			lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' ";
		}
		if (aModel.getCodMotivo() != null && aModel.getCodMotivo().length() > 0) {
			lCondizioni += " and COD_MOTIVO = '" + aModel.getCodMotivo() + "' ";
		}
		if (aModel.getCodMotivoRevoca() != null && aModel.getCodMotivoRevoca().length() > 0) {
			lCondizioni += " and COD_MOTIVO_REVOCA = '" + aModel.getCodMotivoRevoca() + "' ";
		}
		if (aModel.getCodUfficioEmittente() != null && aModel.getCodUfficioEmittente().length() > 0) {
			lCondizioni += " and COD_UFFICIO_EMITTENTE = '" + aModel.getCodUfficioEmittente() + "' ";
		}
		if (aModel.getCodAutoritaEmittente() != null && aModel.getCodAutoritaEmittente().length() > 0) {
			lCondizioni += " and COD_AUTORITA_EMITTENTE = '" + aModel.getCodAutoritaEmittente() + "' ";
		}
		if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) {
			lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' ";
		}
		if (aModel.getDataEmissione() != null) {
			lCondizioni += " and to_char(DATA_EMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataEmissione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodEsito() != null && aModel.getCodEsito().length() > 0) {
			lCondizioni += " and COD_ESITO = '" + aModel.getCodEsito() + "' ";
		}
		if (aModel.getCodEsitoTenore() != null && aModel.getCodEsitoTenore().length() > 0) {
			lCondizioni += " and COD_ESITO_TENORE = '" + aModel.getCodEsitoTenore() + "' ";
		}
		if (aModel.getAnnoProcedimento() != null) {
			lCondizioni += " and ANNO_PROCEDIMENTO = " + aModel.getAnnoProcedimento() + "";
		}
		if (aModel.getProgrProcedimento() != null) {
			lCondizioni += " and PROGR_PROCEDIMENTO = " + aModel.getProgrProcedimento() + "";
		}
		if (aModel.getAnnoProvvedimento() != null) {
			lCondizioni += " and ANNO_PROVVEDIMENTO = " + aModel.getAnnoProvvedimento() + "";
		}
		if (aModel.getProgrProvvedimento() != null) {
			lCondizioni += " and PROGR_PROVVEDIMENTO = " + aModel.getProgrProvvedimento() + "";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getCodMotivoRevocaPm() != null && aModel.getCodMotivoRevocaPm().length() > 0) {
			lCondizioni += " and COD_MOTIVO_REVOCA_PM = '" + aModel.getCodMotivoRevocaPm() + "' ";
		}
		if (aModel.getCodContenutoIstanza() != null && aModel.getCodContenutoIstanza().length() > 0) {
			lCondizioni += " and COD_CONTENUTO_ISTANZA = '" + aModel.getCodContenutoIstanza() + "' ";
		}
		if (aModel.getDataIstanza() != null) {
			lCondizioni += " and to_char(DATA_ISTANZA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataIstanza(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getFlagIstanzaPresdep() != null && aModel.getFlagIstanzaPresdep().length() > 0) {
			lCondizioni += " and FLAG_ISTANZA_PRESDEP = '" + aModel.getFlagIstanzaPresdep() + "' ";
		}
		if (aModel.getCodStatoIstanza() != null && aModel.getCodStatoIstanza().length() > 0) {
			lCondizioni += " and COD_STATO_ISTANZA = '" + aModel.getCodStatoIstanza() + "' ";
		}
		if (aModel.getCodTipoUfficioDestinatario() != null
				&& aModel.getCodTipoUfficioDestinatario().length() > 0) {
			lCondizioni += " and COD_TIPO_UFFICIO_DESTINATARIO = '" + aModel.getCodTipoUfficioDestinatario()
					+ "' ";
		}
		if (aModel.getCodLuogoDestinatario() != null && aModel.getCodLuogoDestinatario().length() > 0) {
			lCondizioni += " and COD_LUOGO_DESTINATARIO = '" + aModel.getCodLuogoDestinatario() + "' ";
		}
		if (aModel.getCodUfficioDestinatario() != null && aModel.getCodUfficioDestinatario().length() > 0) {
			lCondizioni += " and COD_UFFICIO_DESTINATARIO = '" + aModel.getCodUfficioDestinatario() + "' ";
		}
		if (aModel.getDataTrasmissione() != null) {
			lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataTrasmissione(), "dd/MM/yyyy") + "' ";
		}

		if (aModel.getFlagTipoSosp() != null && aModel.getFlagTipoSosp().length() > 0) {
			lCondizioni += " and FLAG_TIPO_SOSP = '" + aModel.getFlagTipoSosp() + "' ";
		}

		if (aModel.getCodTipoIstante() != null && aModel.getCodTipoIstante().length() > 0) {
			lCondizioni += " and COD_TIPO_ISTANTE = '" + aModel.getCodTipoIstante() + "' ";
		}
		if (aModel.getCodTipoUfficioAltro() != null && aModel.getCodTipoUfficioAltro().length() > 0) {
			lCondizioni += " and COD_TIPO_UFFICIO_ALTRO = '" + aModel.getCodTipoUfficioAltro() + "' ";
		}
		if (aModel.getCodTipoAutoritaAltro() != null && aModel.getCodTipoAutoritaAltro().length() > 0) {
			lCondizioni += " and COD_TIPO_AUTORITA_ALTRO = '" + aModel.getCodTipoAutoritaAltro() + "' ";
		}
		if (aModel.getCodLuogoAltro() != null && aModel.getCodLuogoAltro().length() > 0) {
			lCondizioni += " and COD_LUOGO_ALTRO = '" + aModel.getCodLuogoAltro() + "' ";
		}
		if (aModel.getCodUfficioAltro() != null && aModel.getCodUfficioAltro().length() > 0) {
			lCondizioni += " and COD_UFFICIO_ALTRO = '" + aModel.getCodUfficioAltro() + "' ";
		}
		if (aModel.getSezioneAltro() != null && aModel.getSezioneAltro().length() > 0) {
			lCondizioni += " and SEZIONE_ALTRO = '" + aModel.getSezioneAltro() + "' ";
		}
		if (aModel.getDataEmissioneAltro() != null) {
			lCondizioni += " and to_char(DATA_EMISSIONE_ALTRO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataEmissioneAltro(), "dd/MM/yyyy") + "' ";
		}

		if (aModel.getTitIdTitoloCumulato() != null) {
			lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + "";
		}
		if (aModel.getIstrIdIstruttoriaCumulo() != null) {
			lCondizioni += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aModel.getIstrIdIstruttoriaCumulo() + "";
		}
		if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) {
			lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' ";
		}
		if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) {
			lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' ";
		}
		if (aModel.getIdEventoOrigine() != null) {
			lCondizioni += " and ID_EVENTO_ORIGINE = " + aModel.getIdEventoOrigine() + "";
		}
		if (aModel.getEveIdEventoOrigine() != null) {
			lCondizioni += " and EVE_ID_EVENTO_ORIGINE = " + aModel.getEveIdEventoOrigine() + "";
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
		// if (lCondizioni.length() > 0) {
		// lCondizioni = lCondizioni.substring(4);
		// }

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di select per chiave
	 * 
	 * @param aKey
	 * @return
	 ****************************************************************************/
	public String setCondizioniByKey(BigDecimal aIdStatoEsecTitoloCumulato) {
		String lCondizioni = new String();

		lCondizioni += " and ID_STATO_ESEC_TITOLO_CUMULATO = " + aIdStatoEsecTitoloCumulato;

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
		orderBy = " ORDER BY DATA_EMISSIONE ASC, ID_EVENTO_ORIGINE ASC ";
		return orderBy;
	}

	// Ricerca STATO_ESEC_TITOLO_CUMULATO per Tit_Id_Titolo_Cumulato in Join con RICHPM_STATO_ESEC_CUM
	public void ricercaStatoEsecTitoloCumulatoByIdTitoloCumRichGE(BigDecimal aKeyTitolo,
			BigDecimal aKeyRichiesta) throws DAOException {
		String lSql = getSqlQueryJoinRichGE();

		lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aKeyTitolo;
		lSql += " AND RICHPM_STATO_ESEC_CUM.RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aKeyRichiesta;
		lSql += " AND RICHPM_STATO_ESEC_CUM.STAT_ID_STATO_ESEC_TITOLO_CUM = ID_STATO_ESEC_TITOLO_CUMULATO ";

		setStatement(lSql);
	}

	protected String getSqlQueryJoinRichGE() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_STATO_ESEC_TITOLO_CUMULATO, "
				+ "COD_TIPO_EVENTO, COD_TIPO_PROVVEDIMENTO, COD_MOTIVO, " +

				"CODTIPOEVENTO.RV_MEANING DESC_TIPO_EVENTO, "
				+ "CODTIPOPROVVEDIMENTO.RV_MEANING DESC_TIPO_PROVVEDIMENTO, "
				+ "CODMOTIVO.RV_MEANING || NVL(ltrim(CODMOTIVO.RV_ALT5_VALUE), '') DESC_MOTIVO, " + // 23/07/2018
																									// Aggregazione
																									// del
																									// suffisso
																									// alla
																									// descrizione
																									// MOTIVO.

				"COD_MOTIVO_REVOCA, CODMOTIVOREV.RV_MEANING DESC_MOTIVO_REV, "
				+ "COD_MOTIVO_REVOCA_PM, CODMOTIVOREVPM.RV_MEANING DESC_MOTIVO_REV_PM, " +

				"COD_UFFICIO_EMITTENTE, TIPOUFF.RV_MEANING DESC_UFF_EMITTENTE, "
				+ "COD_AUTORITA_EMITTENTE, TIPOAUT.RV_MEANING DESC_AUT_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, COMUNE.DESCRIZIONE DESC_LUOGO_EMITTENTE, " + "DATA_EMISSIONE, " +

				"COD_ESITO, " + "CODESITO.RV_MEANING DESC_ESITO, " + "COD_ESITO_TENORE, "
				+ "CODESITOTENORE.RV_MEANING DESC_ESITO_TENORE, " + "ANNO_PROCEDIMENTO, PROGR_PROCEDIMENTO, "
				+ "ANNO_PROVVEDIMENTO, PROGR_PROVVEDIMENTO, " + "NOTE, " +

				"COD_CONTENUTO_ISTANZA, CODCONTENUTOISTANZA.RV_MEANING DESC_CONTENUTO_ISTANZA, "
				+ "DATA_ISTANZA, " + "FLAG_ISTANZA_PRESDEP, "
				+ "COD_STATO_ISTANZA, CODSTATOISTANZA.RV_MEANING DESC_STATO_ISTANZA, "
				+ "COD_TIPO_UFFICIO_DESTINATARIO, CODTIPOUFFDEST.RV_MEANING DESC_TIPO_UFF_DEST, "
				+ "COD_LUOGO_DESTINATARIO, COMUNEUFFDEST.DESCRIZIONE DESC_LUOGO_DESTINATARIO,  "
				+ "COD_UFFICIO_DESTINATARIO, " + "DATA_TRASMISSIONE, " +

				"FLAG_TIPO_SOSP, " +

				"COD_TIPO_ISTANTE, " + "COD_TIPO_UFFICIO_ALTRO, CODTIPOUFFALTRO.RV_MEANING DESCUFFALTRO, "
				+ "COD_TIPO_AUTORITA_ALTRO, TIPOAUTALTRO.RV_MEANING DESCAUTALTRO, "
				+ "COD_LUOGO_ALTRO, COMUNELUOGOALTRO.DESCRIZIONE DESCLUOGOALTRO, " + "COD_UFFICIO_ALTRO, "
				+ "SEZIONE_ALTRO, " + "DATA_EMISSIONE_ALTRO, " +

				"TIT_ID_TITOLO_CUMULATO, " + "ISTR_ID_ISTRUTTORIA_CUMULO, " + "FLAG_STATO, "
				+ "MOTIVO_MODIFICA, " + "ID_EVENTO_ORIGINE, " + "EVE_ID_EVENTO_ORIGINE, " +

				"COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM STATO_ESEC_TITOLO_CUMULATO ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODTIPOEVENTO ON CODTIPOEVENTO.RV_LOW_VALUE = COD_TIPO_EVENTO "
				+ " AND CODTIPOEVENTO.RV_DOMAIN = 'TIPO_EVENTO' ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODTIPOPROVVEDIMENTO ON CODTIPOPROVVEDIMENTO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO "
				+ " AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODMOTIVO ON CODMOTIVO.RV_LOW_VALUE = COD_MOTIVO "
				+ " AND CODMOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";

		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODMOTIVOREV ON CODMOTIVOREV.RV_LOW_VALUE = COD_MOTIVO_REVOCA "
				+ " AND CODMOTIVOREV.RV_DOMAIN = 'REVOCA_DECSOSP' ";

		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODMOTIVOREVPM ON CODMOTIVOREVPM.RV_LOW_VALUE = COD_MOTIVO_REVOCA_PM "
				+ " AND CODMOTIVOREVPM.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";

		lStatement += " LEFT OUTER JOIN UFFICIO ON UFFICIO.COD_UFFICIO = COD_UFFICIO_EMITTENTE ";
		lStatement += " LEFT JOIN CG_REF_CODES TIPOUFF ON TIPOUFF.RV_LOW_VALUE = UFFICIO.COD_TIPO_UFFICIO ";
		lStatement += " AND TIPOUFF.RV_DOMAIN = 'TIPO_UFFICIO'   ";

		lStatement += " LEFT JOIN CG_REF_CODES TIPOAUT ON TIPOAUT.RV_LOW_VALUE = COD_AUTORITA_EMITTENTE ";
		lStatement += " AND TIPOAUT.RV_DOMAIN = 'TIPO_AUTORITA'   ";

		lStatement += " LEFT OUTER JOIN COMUNE ON COMUNE.COD_COMUNE = COD_LUOGO_EMITTENTE ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODESITO ON CODESITO.RV_LOW_VALUE = COD_ESITO "
				+ " AND CODESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODESITOTENORE ON CODESITOTENORE.RV_LOW_VALUE = COD_ESITO_TENORE "
				+ " AND CODESITOTENORE.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' ";

		// =====================
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODCONTENUTOISTANZA ON CODCONTENUTOISTANZA.RV_LOW_VALUE = COD_CONTENUTO_ISTANZA "
				+ " AND CODCONTENUTOISTANZA.RV_DOMAIN = 'CONTENUTO_ISTANZA' ";

		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODSTATOISTANZA ON CODSTATOISTANZA.RV_LOW_VALUE = COD_STATO_ISTANZA "
				+ " AND CODSTATOISTANZA.RV_DOMAIN = 'CONTENUTO_ISTANZA' ";

		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODTIPOUFFDEST ON CODTIPOUFFDEST.RV_LOW_VALUE = COD_TIPO_UFFICIO_DESTINATARIO "
				+ " AND CODTIPOUFFDEST.RV_DOMAIN = 'TIPO_UFFICIO' ";

		lStatement += " LEFT OUTER JOIN COMUNE COMUNEUFFDEST ON COMUNEUFFDEST.COD_COMUNE = COD_LUOGO_DESTINATARIO ";
		// =======================
		lStatement += " LEFT OUTER JOIN COMUNE COMUNELUOGOALTRO ON COMUNELUOGOALTRO.COD_COMUNE = COD_LUOGO_ALTRO ";

		lStatement += " LEFT OUTER JOIN CG_REF_CODES CODTIPOUFFALTRO ON CODTIPOUFFALTRO.RV_LOW_VALUE = COD_TIPO_UFFICIO_ALTRO "
				+ " AND CODTIPOUFFALTRO.RV_DOMAIN = 'TIPO_UFFICIO' ";

		lStatement += " LEFT JOIN CG_REF_CODES TIPOAUTALTRO ON TIPOAUTALTRO.RV_LOW_VALUE = COD_TIPO_AUTORITA_ALTRO ";
		lStatement += " AND TIPOAUTALTRO.RV_DOMAIN = 'TIPO_AUTORITA' ";

		lStatement += ", RICHPM_STATO_ESEC_CUM";

		lStatement += " WHERE 1=1 ";

		return lStatement;
	}

}