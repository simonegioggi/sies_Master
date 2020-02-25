package siap.sius.remissionedebito.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.dao.SIAPSqlDAO;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: RichiestaRemissioneSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RichiestaRemissione
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
public class RichiestaRemissioneSqlDAO extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Costruttore
	 * 
	 * @param con
	 */
	public RichiestaRemissioneSqlDAO(Connection con) {
		super(con);
	}

	/**
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void getCountRichiestaRemissione(RichiestaRemissioneModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM RICHIESTA_REMISSIONE ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/**
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
	 * passata in input
	 * 
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 */
	public void ricercaRichiestaRemissionePaged(RichiestaRemissioneModel aModel, int aPage)
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

	/**
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaRichiestaRemissione(RichiestaRemissioneModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " AND " + lCondizioni;

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * Metodo che imposta la statement di ricerca per chiave
	 * 
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaRichiestaRemissioneByKey(BigDecimal aIdRichiestaRemissione) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		// lSql += " WHERE " + setCondizioniByKey( aIdRichiestaRemissione);
		lSql += setCondizioniByKey(aIdRichiestaRemissione);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * Metodo che imposta la statement di ricerca per chiave Fascicolo SIEP
	 * 
	 * @param aIdFasSIEP
	 * @throws DAOException
	 */
	public void ricercaRichiestaRemissioneByIdFasSIEP(BigDecimal aIdFasSIEP) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		lSql += setCondizioniByIdFasSIEP(aIdFasSIEP);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * Metodo che imposta la statement di ricerca per chiave Fascicolo SIUS
	 * 
	 * @param aIdFasSIUS
	 * @throws DAOException
	 */
	public void ricercaRichiestaRemissioneByIdFasSIUS(BigDecimal aIdFasSIUS) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		lSql += setCondizioniByIdFasSIUS(aIdFasSIUS);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * Metodo per la costruzione della sql query
	 * 
	 * @return
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		// lStatement += " SELECT " +
		lStatement += " SELECT DISTINCT " + "ID_RICHIESTA_REMISSIONE, " + "ANNO_PARTITA, " + "NUM_PARTITA, "
				+ "NUM_EX_CAMPIONE, " + "PROT_CIRCOSRIZIONE_DOGANALE, " + "COD_TIPO_AUTORITA_EMITTENTE, "
				+ "CODTIPOAUTORITAEMITTENTE.RV_MEANING DESCR_AUTORITA, " + "COD_LUOGO_EMITTENTE, "
				+ "CODLUOGOEMITTENTE.DESCRIZIONE DESCR_COMUNE, " +

				"COD_TIPO_PROVVEDIMENTO, " + "CODTIPOPROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, "
				+ "DATA_EMISSIONE, " + "COD_AUTORITA_EMITTENTE_PROVV, "
				+ "CODAUTORITAEMITTENTEPROVV.RV_MEANING DESCR_AUTORITA_EMITTENTE_PROVV, "
				+ "COD_LUOGO_EMITTENTE_PROVV, "
				+ "CODLUOGOEMITTENTEPROVV.DESCRIZIONE DESCR_LUOGO_EMITTENTE_PROVV, " + "FLAG_SPESE_CARCERE, "
				+ "IMPORTO_SPESE_CARCERE, " + "FLAG_SPESE_PROCEDIMENTO, " + "IMPORTO_SPESE_PROCEDIMENTO, " +

				"FAS_SIE_ID_FASCICOLO_SIEP, " + "EVE_ID_EVENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " + "FAS_SIU_ID_FASCICOLO_SIUS, "
				+ "NOTE ";

		// aggiungere qui gli eventuali campi descrizioni.
		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni).
		lStatement += " FROM RICHIESTA_REMISSIONE";
		lStatement += " ,CG_REF_CODES CODTIPOAUTORITAEMITTENTE, COMUNE CODLUOGOEMITTENTE ";
		lStatement += " ,CG_REF_CODES CODTIPOPROVVEDIMENTO, CG_REF_CODES CODAUTORITAEMITTENTEPROVV, COMUNE CODLUOGOEMITTENTEPROVV ";
		lStatement += " where ";
		lStatement += "( nvl(RICHIESTA_REMISSIONE.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_AUTORITA' ) ";
		lStatement += " and ( nvl(RICHIESTA_REMISSIONE.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE) ";
		lStatement += " and ( nvl(RICHIESTA_REMISSIONE.COD_TIPO_PROVVEDIMENTO,'-') = CODTIPOPROVVEDIMENTO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) ";
		// lStatement +=
		// " and ( nvl(RICHIESTA_REMISSIONE.COD_AUTORITA_EMITTENTE_PROVV,'-') = CODAUTORITAEMITTENTEPROVV.RV_LOW_VALUE AND CODAUTORITAEMITTENTEPROVV.RV_DOMAIN = 'TIPO_AUTORITA' ) ";
		lStatement += " and ( nvl(RICHIESTA_REMISSIONE.COD_AUTORITA_EMITTENTE_PROVV,'-') = CODAUTORITAEMITTENTEPROVV.RV_LOW_VALUE AND ( CODAUTORITAEMITTENTEPROVV.RV_DOMAIN = 'TIPO_AUTORITA' OR CODAUTORITAEMITTENTEPROVV.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE' ) ) ";
		lStatement += " and ( nvl(RICHIESTA_REMISSIONE.COD_LUOGO_EMITTENTE_PROVV,'-') = CODLUOGOEMITTENTEPROVV.COD_COMUNE) ";

		return lStatement;
	}

	/**
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		RichiestaRemissioneModel aModel = new RichiestaRemissioneModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdRichiestaRemissione(getBigDecimal("ID_RICHIESTA_REMISSIONE"));
		aModel.setAnnoPartita(getBigDecimal("ANNO_PARTITA"));
		aModel.setNumPartita(getBigDecimal("NUM_PARTITA"));
		aModel.setNumExCampione(getString("NUM_EX_CAMPIONE"));
		aModel.setProtCircosrizioneDoganale(getString("PROT_CIRCOSRIZIONE_DOGANALE"));
		aModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		aModel.setDescrTipoAutoritaEmittente(getString("DESCR_AUTORITA"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("DESCR_COMUNE"));

		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setDescrTipoProvvedimento(getString("DESCR_PROVVEDIMENTO"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodAutoritaEmittenteProvv(getString("COD_AUTORITA_EMITTENTE_PROVV"));
		aModel.setDescrAutoritaEmittenteProvv(getString("DESCR_AUTORITA_EMITTENTE_PROVV"));
		aModel.setCodLuogoEmittenteProvv(getString("COD_LUOGO_EMITTENTE_PROVV"));
		aModel.setDescrLuogoEmittenteProvv(getString("DESCR_LUOGO_EMITTENTE_PROVV"));
		aModel.setFlagSpeseCarcere(getString("FLAG_SPESE_CARCERE"));
		aModel.setImportoSpeseCarcere(getBigDecimal("IMPORTO_SPESE_CARCERE"));
		aModel.setFlagSpeseProcedimento(getString("FLAG_SPESE_PROCEDIMENTO"));
		aModel.setImportoSpeseProcedimento(getBigDecimal("IMPORTO_SPESE_PROCEDIMENTO"));

		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setNote(getString("NOTE"));

		// aModel.setDescrCodTipoSanzione(getString("") );

		return aModel;
	}

	/**
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 */
	public String setCondizioni(RichiestaRemissioneModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdRichiestaRemissione() != null) {
			lCondizioni += " and ID_RICHIESTA_REMISSIONE = " + aModel.getIdRichiestaRemissione() + "";
		}
		if (aModel.getAnnoPartita() != null) {
			lCondizioni += " and ANNO_PARTITA = " + aModel.getAnnoPartita() + "";
		}
		if (aModel.getNumPartita() != null) {
			lCondizioni += " and NUM_PARTITA = " + aModel.getNumPartita() + "";
		}
		if (aModel.getNumExCampione() != null && aModel.getNumExCampione().length() > 0) {
			lCondizioni += " and NUM_EX_CAMPIONE = '" + aModel.getNumExCampione() + "' ";
		}
		if (aModel.getProtCircosrizioneDoganale() != null
				&& aModel.getProtCircosrizioneDoganale().length() > 0) {
			lCondizioni += " and PROT_CIRCOSRIZIONE_DOGANALE = '" + aModel.getProtCircosrizioneDoganale()
					+ "' ";
		}
		if (aModel.getCodTipoAutoritaEmittente() != null && aModel.getCodTipoAutoritaEmittente().length() > 0) {
			lCondizioni += " and COD_TIPO_AUTORITA_EMITTENTE = '" + aModel.getCodTipoAutoritaEmittente()
					+ "' ";
		}
		if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) {
			lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' ";
		}

		if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) {
			lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' ";
		}
		if (aModel.getDataEmissione() != null) {
			lCondizioni += " and to_char(DATA_EMISSIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataEmissione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodAutoritaEmittenteProvv() != null
				&& aModel.getCodAutoritaEmittenteProvv().length() > 0) {
			lCondizioni += " and COD_AUTORITA_EMITTENTE_PROVV = '" + aModel.getCodAutoritaEmittenteProvv()
					+ "' ";
		}
		if (aModel.getCodLuogoEmittenteProvv() != null && aModel.getCodLuogoEmittenteProvv().length() > 0) {
			lCondizioni += " and COD_LUOGO_EMITTENTE_PROVV = '" + aModel.getCodLuogoEmittenteProvv() + "' ";
		}
		if (aModel.getFlagSpeseCarcere() != null && aModel.getFlagSpeseCarcere().length() > 0) {
			lCondizioni += " and FLAG_SPESE_CARCERE = '" + aModel.getFlagSpeseCarcere() + "' ";
		}
		if (aModel.getImportoSpeseCarcere() != null) {
			lCondizioni += " and IMPORTO_SPESE_CARCERE = " + aModel.getImportoSpeseCarcere() + "";
		}
		if (aModel.getFlagSpeseProcedimento() != null && aModel.getFlagSpeseProcedimento().length() > 0) {
			lCondizioni += " and FLAG_SPESE_PROCEDIMENTO = '" + aModel.getFlagSpeseProcedimento() + "' ";
		}
		if (aModel.getImportoSpeseProcedimento() != null) {
			lCondizioni += " and IMPORTO_SPESE_PROCEDIMENTO = " + aModel.getImportoSpeseProcedimento() + "";
		}

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
		}
		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + "";
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
		if (aModel.getFasSiuIdFascicoloSius() != null) {
			lCondizioni += " and FAS_SIU_ID_FASCICOLO_SIUS = " + aModel.getFasSiuIdFascicoloSius() + "";
		}

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		logger.info("lCondizioni = " + lCondizioni);
		return lCondizioni;
	}

	/**
	 * Metodo che imposta le condizioni di select per chiave
	 * 
	 * @param aKey
	 * @return
	 */
	public String setCondizioniByKey(BigDecimal aIdRichiestaRemissione) {
		String lCondizioni = new String();

		lCondizioni += " and ID_RICHIESTA_REMISSIONE = " + aIdRichiestaRemissione;

		// Elimino il primo and
		// if (lCondizioni.length() > 0) {
		// lCondizioni = lCondizioni.substring(4);
		// }

		logger.info("lCondizioni = " + lCondizioni);

		return lCondizioni;
	}

	/**
	 * Metodo che imposta le condizioni di select per Id Fascicolo SIEP
	 * 
	 * @param aKey
	 * @return
	 */
	public String setCondizioniByIdFasSIEP(BigDecimal aIdFasSIEP) {
		String lCondizioni = new String();

		lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFasSIEP;

		return lCondizioni;
	}

	/**
	 * Metodo che imposta le condizioni di select per Id Fascicolo SIUS
	 * 
	 * @param aKey
	 * @return
	 */
	public String setCondizioniByIdFasSIUS(BigDecimal aIdFasSIUS) {
		String lCondizioni = new String();

		lCondizioni += " and FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFasSIUS;

		return lCondizioni;
	}

	/**
	 * Metodo per la costruzione della sezione order by
	 * 
	 * @return
	 */
	protected String getOrderBy() {

		String orderBy = new String("");
		// orderBy = " ORDER BY DATA_ISCRIZIONE_ATTO";
		orderBy = " ORDER BY DATA_EMISSIONE, DATA_INSERIMENTO";
		return orderBy;
	}

	/**
	 * Metodo che imposta la statement di ricerca per id Evento
	 * 
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaRichiestaRemissioneByEvento(BigDecimal aIdEvento) throws DAOException {

		// Recupera la select...from
		String lSql_1 = getSqlQuery_1();

		// Aggiunge le where condition per chiave

		lSql_1 += " and EVE_ID_EVENTO = " + aIdEvento;

		// Imposta lo statement da eseguire
		setStatement(lSql_1);
	}

	/**
	 * Metodo per la costruzione della sql query per ricercaRichiestaRemissioneByEvento
	 * 
	 * @return
	 */
	protected String getSqlQuery_1() {

		String lStatement_1 = new String("");

		lStatement_1 += " SELECT " + "ID_RICHIESTA_REMISSIONE, " + "ANNO_PARTITA, " + "NUM_PARTITA, "
				+ "NUM_EX_CAMPIONE, " + "PROT_CIRCOSRIZIONE_DOGANALE, " + "COD_TIPO_AUTORITA_EMITTENTE, "
				+ "CODTIPOAUTORITAEMITTENTE.RV_MEANING DESCR_AUTORITA, " + "COD_LUOGO_EMITTENTE, "
				+ "CODLUOGOEMITTENTE.DESCRIZIONE DESCR_COMUNE, " + "COD_TIPO_PROVVEDIMENTO, "
				+ "DATA_EMISSIONE, " + "COD_AUTORITA_EMITTENTE_PROVV, " + "COD_LUOGO_EMITTENTE_PROVV, "
				+ "FLAG_SPESE_CARCERE, " + "IMPORTO_SPESE_CARCERE, " + "FLAG_SPESE_PROCEDIMENTO, "
				+ "IMPORTO_SPESE_PROCEDIMENTO, " + "FAS_SIE_ID_FASCICOLO_SIEP, " + "EVE_ID_EVENTO, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS, " + "NOTE ";

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement_1 += " FROM RICHIESTA_REMISSIONE";
		lStatement_1 += " ,CG_REF_CODES CODTIPOAUTORITAEMITTENTE";
		lStatement_1 += " ,COMUNE CODLUOGOEMITTENTE";
		lStatement_1 += " where ";
		lStatement_1 += "( nvl(RICHIESTA_REMISSIONE.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_AUTORITA' ) ";
		lStatement_1 += " and ( nvl(RICHIESTA_REMISSIONE.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE) ";

		return lStatement_1;
	}

}