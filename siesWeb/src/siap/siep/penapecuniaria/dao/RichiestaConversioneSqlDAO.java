package siap.siep.penapecuniaria.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.dao.SIAPSqlDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: RichiestaConversioneSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RichiestaConversione
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
public class RichiestaConversioneSqlDAO extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Costruttore
	 * 
	 * @param con
	 */
	public RichiestaConversioneSqlDAO(Connection con) {
		super(con);
	}

	/**
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void getCountRichiestaConversione(RichiestaConversioneModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM RICHIESTA_CONVERSIONE ";

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
	public void ricercaRichiestaConversionePaged(RichiestaConversioneModel aModel, int aPage)
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
	public void ricercaRichiestaConversione(RichiestaConversioneModel aModel) throws DAOException {
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
	public void ricercaRichiestaConversioneByKey(BigDecimal aIdRichiestaConversione) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		// lSql += " WHERE " + setCondizioniByKey( aIdRichiestaConversione);
		lSql += setCondizioniByKey(aIdRichiestaConversione);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * Metodo che imposta la statement di ricerca per chiave evitando il filtro sull'evento
	 * 
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaRichiestaConversioneByKeySenzaEvento(BigDecimal aIdRichiestaConversione)
			throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuerySenzaEvento();

		// Aggiunge le where condition per chiave
		// lSql += " WHERE " + setCondizioniByKey( aIdRichiestaConversione);
		lSql += setCondizioniByKey(aIdRichiestaConversione);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * Metodo che imposta la statement di ricerca per chiave Fascicolo SIEP
	 * 
	 * @param aIdFasSIEP
	 * @throws DAOException
	 */
	public void ricercaRichiestaConversioneByIdFasSIEP(BigDecimal aIdFasSIEP) throws DAOException {
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
	public void ricercaRichiestaConversioneByIdFasSIUS(BigDecimal aIdFasSIUS) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuerySenzaEvento();

		lSql += setCondizioniByIdFasSIUS(aIdFasSIUS);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * Metodo per la costruzione della sql query
	 * 
	 * @return
	 */
	protected String getSqlQuerySenzaEvento() {
		String lStatement = new String("");

		lStatement += " SELECT " + "RI.ID_RICHIESTA_CONVERSIONE, " + "RI.ANNO_PARTITA, " + "RI.NUM_PARTITA, "
				+ "RI.NUM_EX_CAMPIONE, " + "RI.PROT_CIRCOSRIZIONE_DOGANALE, "
				+ "RI.COD_TIPO_AUTORITA_EMITTENTE, " + "CODTIPOAUTORITAEMITTENTE.RV_MEANING DESCR_AUTORITA, "
				+ "RI.COD_LUOGO_EMITTENTE, " + "CODLUOGOEMITTENTE.DESCRIZIONE DESCR_COMUNE, "
				+ "RI.DATA_RICEZIONE_ATTO, " + "RI.DATA_ISCRIZIONE_ATTO, " + "RI.DATA_ESAZIONE, "
				+ "RI.IMPORTO_MULTA, " + "RI.DATA_PRESCRIZIONE_MULTA, " + "RI.FLAG_IMPRESCRITTIBILE_MULTA, "
				+ "RI.IMPORTO_AMMENDA, " + "RI.DATA_PRESCRIZIONE_AMMENDA, "
				+ "RI.FLAG_IMPRESCRITTIBILE_AMMENDA, " + "RI.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "RI.EVE_ID_EVENTO, " + "RI.COD_OPERATORE_INSERIMENTO, " + "RI.DATA_INSERIMENTO, "
				+ "RI.COD_UFFICIO_INSERIMENTO, " + "RI.COD_OPERATORE_AGGIORNAMENTO, "
				+ "RI.DATA_AGGIORNAMENTO, " + "RI.COD_UFFICIO_AGGIORNAMENTO, "
				+ "RI.FAS_SIU_ID_FASCICOLO_SIUS, " + "RI.DURATA_ESITO_ANNI, " + "RI.DURATA_ESITO_MESI, "
				+ "RI.DURATA_ESITO_GIORNI, " + "RI.NUMERO_RATE, " + "RI.VALORE_RATA, "
				+ "RI.VALORE_ULTIMA_RATA, " + "RI.DATA_ANNULLAMENTO, " + "RI.COD_TIPO_SANZIONE, "
				+ "RI.NOTE, " + "RI.DATA_DEPOSITO, " + "RI.DATA_INIZIO_PAGAMENTO, "
				+ "RI.NUMERO_GIORNI_INIZIO_PAGAMENTO ";
		// aggiungere qui gli eventuali campi descrizioni.
		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni).
		lStatement += " FROM RICHIESTA_CONVERSIONE RI ";
		lStatement += " ,CG_REF_CODES CODTIPOAUTORITAEMITTENTE, COMUNE CODLUOGOEMITTENTE ";
		lStatement += " where ";
		lStatement += "( nvl(RI.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_AUTORITA' ) ";
		lStatement += " and ( nvl(RI.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE) ";
		return lStatement;
	}

	/**
	 * Paolo Cherubini 22/04/2011 effettuo una correzione per far si che si vada anche su l'evento ossia la
	 * richiesta di conversione vede essere collegata ad un evento validato ordina per id_evento desc cosi se
	 * dovesse servire si puo prendere l'ultima, anche perchè una sola ce ne dovrebbe essere 09/06/2011
	 * Vincenzo - Modifica per la costruzione della sql query con Evento in "left outer join".
	 * 
	 * @return
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "RI.ID_RICHIESTA_CONVERSIONE, " + "RI.ANNO_PARTITA, " + "RI.NUM_PARTITA, "
				+ "RI.NUM_EX_CAMPIONE, " + "RI.PROT_CIRCOSRIZIONE_DOGANALE, "
				+ "RI.COD_TIPO_AUTORITA_EMITTENTE, " + "CODTIPOAUTORITAEMITTENTE.RV_MEANING DESCR_AUTORITA, "
				+ "RI.COD_LUOGO_EMITTENTE, " + "CODLUOGOEMITTENTE.DESCRIZIONE DESCR_COMUNE, "
				+ "RI.DATA_RICEZIONE_ATTO, " + "RI.DATA_ISCRIZIONE_ATTO, " + "RI.DATA_ESAZIONE, "
				+ "RI.IMPORTO_MULTA, " + "RI.DATA_PRESCRIZIONE_MULTA, " + "RI.FLAG_IMPRESCRITTIBILE_MULTA, "
				+ "RI.IMPORTO_AMMENDA, " + "RI.DATA_PRESCRIZIONE_AMMENDA, "
				+ "RI.FLAG_IMPRESCRITTIBILE_AMMENDA, " + "RI.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "RI.EVE_ID_EVENTO, " + "RI.COD_OPERATORE_INSERIMENTO, " + "RI.DATA_INSERIMENTO, "
				+ "RI.COD_UFFICIO_INSERIMENTO, " + "RI.COD_OPERATORE_AGGIORNAMENTO, "
				+ "RI.DATA_AGGIORNAMENTO, " + "RI.COD_UFFICIO_AGGIORNAMENTO, "
				+ "RI.FAS_SIU_ID_FASCICOLO_SIUS, " + "RI.DURATA_ESITO_ANNI, " + "RI.DURATA_ESITO_MESI, "
				+ "RI.DURATA_ESITO_GIORNI, " + "RI.NUMERO_RATE, " + "VALORE_RATA, "
				+ "RI.VALORE_ULTIMA_RATA, " + "RI.DATA_ANNULLAMENTO, " + "RI.COD_TIPO_SANZIONE, "
				+ "RI.NOTE, " + "RI.DATA_DEPOSITO, " + "RI.DATA_INIZIO_PAGAMENTO, "
				+ "RI.NUMERO_GIORNI_INIZIO_PAGAMENTO ";
		lStatement += " FROM RICHIESTA_CONVERSIONE RI ";
		lStatement += "  join EVENTO ev on(RI.EVE_ID_EVENTO = EV.ID_EVENTO AND EV.FLAG_DOCUMENTO_REGISTRATO = 'S') ";
		lStatement += " ,CG_REF_CODES CODTIPOAUTORITAEMITTENTE, COMUNE CODLUOGOEMITTENTE ";
		lStatement += " where ";
		lStatement += "( nvl(RI.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_AUTORITA' ) ";
		lStatement += " and ( nvl(RI.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE) ";

		return lStatement;
	}

	/**
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		RichiestaConversioneModel aModel = new RichiestaConversioneModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdRichiestaConversione(getBigDecimal("ID_RICHIESTA_CONVERSIONE"));
		aModel.setAnnoPartita(getBigDecimal("ANNO_PARTITA"));
		aModel.setNumPartita(getBigDecimal("NUM_PARTITA"));
		aModel.setNumExCampione(getString("NUM_EX_CAMPIONE"));
		aModel.setProtCircosrizioneDoganale(getString("PROT_CIRCOSRIZIONE_DOGANALE"));
		aModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		aModel.setDescrTipoAutoritaEmittente(getString("DESCR_AUTORITA"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("DESCR_COMUNE"));
		aModel.setDataRicezioneAtto(getDate("DATA_RICEZIONE_ATTO"));
		aModel.setDataIscrizioneAtto(getDate("DATA_ISCRIZIONE_ATTO"));
		aModel.setDataEsazione(getDate("DATA_ESAZIONE"));
		aModel.setImportoMulta(getBigDecimal("IMPORTO_MULTA"));
		aModel.setDataPrescrizioneMulta(getDate("DATA_PRESCRIZIONE_MULTA"));
		aModel.setFlagImprescrittibileMulta(getString("FLAG_IMPRESCRITTIBILE_MULTA"));
		aModel.setImportoAmmenda(getBigDecimal("IMPORTO_AMMENDA"));
		aModel.setDataPrescrizioneAmmenda(getDate("DATA_PRESCRIZIONE_AMMENDA"));
		aModel.setFlagImprescrittibileAmmenda(getString("FLAG_IMPRESCRITTIBILE_AMMENDA"));
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
		aModel.setDurataEsitoAnni(getBigDecimal("DURATA_ESITO_ANNI"));
		aModel.setDurataEsitoMesi(getBigDecimal("DURATA_ESITO_MESI"));
		aModel.setDurataEsitoGiorni(getBigDecimal("DURATA_ESITO_GIORNI"));
		aModel.setNumeroRate(getBigDecimal("NUMERO_RATE"));
		aModel.setValoreRata(getBigDecimal("VALORE_RATA"));
		aModel.setValoreUltimaRata(getBigDecimal("VALORE_ULTIMA_RATA"));
		aModel.setDataAnnullamento(getDate("DATA_ANNULLAMENTO"));
		aModel.setCodTipoSanzione(getString("COD_TIPO_SANZIONE"));
		aModel.setNote(getString("NOTE"));
		aModel.setDataDeposito(getDate("DATA_DEPOSITO"));
		aModel.setDataInizioPagamento(getDate("DATA_INIZIO_PAGAMENTO"));
		aModel.setNumeroGiorniInizioPagamento(getBigDecimal("NUMERO_GIORNI_INIZIO_PAGAMENTO"));

		// aModel.setDescrCodTipoSanzione(getString("") );

		return aModel;
	}

	/**
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 */
	public String setCondizioni(RichiestaConversioneModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdRichiestaConversione() != null) {
			lCondizioni += " and RI.ID_RICHIESTA_CONVERSIONE = " + aModel.getIdRichiestaConversione() + "";
		}
		if (aModel.getAnnoPartita() != null) {
			lCondizioni += " and RI.ANNO_PARTITA = " + aModel.getAnnoPartita() + "";
		}
		if (aModel.getNumPartita() != null) {
			lCondizioni += " and RI.NUM_PARTITA = " + aModel.getNumPartita() + "";
		}
		if (aModel.getNumExCampione() != null && aModel.getNumExCampione().length() > 0) {
			lCondizioni += " and RI.NUM_EX_CAMPIONE = '" + aModel.getNumExCampione() + "' ";
		}
		if (aModel.getProtCircosrizioneDoganale() != null
				&& aModel.getProtCircosrizioneDoganale().length() > 0) {
			lCondizioni += " and RI.PROT_CIRCOSRIZIONE_DOGANALE = '" + aModel.getProtCircosrizioneDoganale()
					+ "' ";
		}
		if (aModel.getCodTipoAutoritaEmittente() != null && aModel.getCodTipoAutoritaEmittente().length() > 0) {
			lCondizioni += " and RI.COD_TIPO_AUTORITA_EMITTENTE = '" + aModel.getCodTipoAutoritaEmittente()
					+ "' ";
		}
		if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) {
			lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' ";
		}
		if (aModel.getDataRicezioneAtto() != null) {
			lCondizioni += " and to_char(RI.DATA_RICEZIONE_ATTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataRicezioneAtto(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataIscrizioneAtto() != null) {
			lCondizioni += " and to_char(RI.DATA_ISCRIZIONE_ATTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataIscrizioneAtto(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataEsazione() != null) {
			lCondizioni += " and to_char(RI.DATA_ESAZIONE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataEsazione(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getImportoMulta() != null) {
			lCondizioni += " and RI.IMPORTO_MULTA = " + aModel.getImportoMulta() + "";
		}
		if (aModel.getDataPrescrizioneMulta() != null) {
			lCondizioni += " and to_char(RI.DATA_PRESCRIZIONE_MULTA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataPrescrizioneMulta(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getFlagImprescrittibileMulta() != null
				&& aModel.getFlagImprescrittibileMulta().length() > 0) {
			lCondizioni += " and RI.FLAG_IMPRESCRITTIBILE_MULTA = '" + aModel.getFlagImprescrittibileMulta()
					+ "' ";
		}
		if (aModel.getImportoAmmenda() != null) {
			lCondizioni += " and RI.IMPORTO_AMMENDA = " + aModel.getImportoAmmenda() + "";
		}
		if (aModel.getDataPrescrizioneAmmenda() != null) {
			lCondizioni += " and to_char(RI.DATA_PRESCRIZIONE_AMMENDA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataPrescrizioneAmmenda(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getFlagImprescrittibileAmmenda() != null
				&& aModel.getFlagImprescrittibileAmmenda().length() > 0) {
			lCondizioni += " and RI.FLAG_IMPRESCRITTIBILE_AMMENDA = '"
					+ aModel.getFlagImprescrittibileAmmenda() + "' ";
		}
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " and RI.FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + "";
		}
		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " and RI.EVE_ID_EVENTO = " + aModel.getEveIdEvento() + "";
		}
		if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
			lCondizioni += " and RI.COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento()
					+ "' ";
		}
		if (aModel.getDataInserimento() != null) {
			lCondizioni += " and to_char(RI.DATA_INSERIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
			lCondizioni += " and RI.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		}
		if (aModel.getCodOperatoreAggiornamento() != null
				&& aModel.getCodOperatoreAggiornamento().length() > 0) {
			lCondizioni += " and RI.COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "' ";
		}
		if (aModel.getDataAggiornamento() != null) {
			lCondizioni += " and to_char(RI.DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
		}
		if (aModel.getFasSiuIdFascicoloSius() != null) {
			lCondizioni += " and RI.FAS_SIU_ID_FASCICOLO_SIUS = " + aModel.getFasSiuIdFascicoloSius() + "";
		}
		if (aModel.getDurataEsitoAnni() != null) {
			lCondizioni += " and RI.DURATA_ESITO_ANNI = " + aModel.getDurataEsitoAnni() + "";
		}
		if (aModel.getDurataEsitoMesi() != null) {
			lCondizioni += " and RI.DURATA_ESITO_MESI = " + aModel.getDurataEsitoMesi() + "";
		}
		if (aModel.getDurataEsitoGiorni() != null) {
			lCondizioni += " and RI.DURATA_ESITO_GIORNI = " + aModel.getDurataEsitoGiorni() + "";
		}
		if (aModel.getNumeroRate() != null) {
			lCondizioni += " and RI.NUMERO_RATE = " + aModel.getNumeroRate() + "";
		}
		if (aModel.getValoreRata() != null) {
			lCondizioni += " and VALORE_RATA = " + aModel.getValoreRata() + "";
		}
		if (aModel.getValoreUltimaRata() != null) {
			lCondizioni += " and RI.VALORE_ULTIMA_RATA = " + aModel.getValoreUltimaRata() + "";
		}
		if (aModel.getDataAnnullamento() != null) {
			lCondizioni += " and to_char(RI.DATA_ANNULLAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAnnullamento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodTipoSanzione() != null && aModel.getCodTipoSanzione().length() > 1) {
			lCondizioni += " and RI.COD_TIPO_SANZIONE = '" + aModel.getCodTipoSanzione() + "' ";
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
	public String setCondizioniByKey(BigDecimal aIdRichiestaConversione) {
		String lCondizioni = new String();

		lCondizioni += " and RI.ID_RICHIESTA_CONVERSIONE = " + aIdRichiestaConversione;

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

		lCondizioni += " and RI.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFasSIEP;

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

		lCondizioni += " and RI.FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFasSIUS;

		return lCondizioni;
	}

	/**
	 * Metodo per la costruzione della sezione order by
	 * 
	 * @return
	 */
	protected String getOrderBy() {
		String orderBy = new String("");
		orderBy = " ORDER BY RI.DATA_ISCRIZIONE_ATTO";
		return orderBy;
	}

	/**
	 * Metodo che imposta la statement di ricerca per id Evento
	 * 
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaRichiestaConversioneByEvento(BigDecimal aIdEvento) throws DAOException {
		// Recupera la select...from
		String lSql_1 = getSqlQuery_1();

		// Aggiunge le where condition per chiave

		lSql_1 += " and EVE_ID_EVENTO = " + aIdEvento;

		// Imposta lo statement da eseguire
		setStatement(lSql_1);
	}

	/**
	 * Metodo che imposta la statement di ricerca per id Fascicolo Siep
	 * 
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaRichiestaConversioneByIdFascicoloSiep(BigDecimal aIdFascicoloSiep) throws DAOException {

		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave

		lSql += " and RI.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;
		lSql += " ORDER BY RI.DATA_INSERIMENTO DESC";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/**
	 * Metodo per la costruzione della sql query per ricercaRichiestaConversioneByEvento
	 * 
	 * @return
	 */
	protected String getSqlQuery_1() {
		String lStatement_1 = new String("");

		lStatement_1 += " SELECT " + "ID_RICHIESTA_CONVERSIONE, " + "ANNO_PARTITA, " + "NUM_PARTITA, "
				+ "NUM_EX_CAMPIONE, " + "PROT_CIRCOSRIZIONE_DOGANALE, " + "COD_TIPO_AUTORITA_EMITTENTE, "
				+ "CODTIPOAUTORITAEMITTENTE.RV_MEANING DESCR_AUTORITA, " + "COD_LUOGO_EMITTENTE, "
				+ "CODLUOGOEMITTENTE.DESCRIZIONE DESCR_COMUNE, " + "DATA_RICEZIONE_ATTO, "
				+ "DATA_ISCRIZIONE_ATTO, " + "DATA_ESAZIONE, " + "IMPORTO_MULTA, "
				+ "DATA_PRESCRIZIONE_MULTA, " + "FLAG_IMPRESCRITTIBILE_MULTA, " + "IMPORTO_AMMENDA, "
				+ "DATA_PRESCRIZIONE_AMMENDA, " + "FLAG_IMPRESCRITTIBILE_AMMENDA, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, " + "EVE_ID_EVENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " + "FAS_SIU_ID_FASCICOLO_SIUS, "
				+ "DURATA_ESITO_ANNI, " + "DURATA_ESITO_MESI, " + "DURATA_ESITO_GIORNI, " + "NUMERO_RATE, "
				+ "VALORE_RATA, " + "VALORE_ULTIMA_RATA, " + "DATA_ANNULLAMENTO, " + "COD_TIPO_SANZIONE, "
				+ "NOTE " + "NOTE, " + "DATA_DEPOSITO, " + "DATA_INIZIO_PAGAMENTO, "
				+ "NUMERO_GIORNI_INIZIO_PAGAMENTO ";

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement_1 += " FROM RICHIESTA_CONVERSIONE";
		lStatement_1 += " ,CG_REF_CODES CODTIPOAUTORITAEMITTENTE";
		lStatement_1 += " ,COMUNE CODLUOGOEMITTENTE";
		lStatement_1 += " where ";
		lStatement_1 += "( nvl(RICHIESTA_CONVERSIONE.COD_TIPO_AUTORITA_EMITTENTE,'-') = CODTIPOAUTORITAEMITTENTE.RV_LOW_VALUE AND CODTIPOAUTORITAEMITTENTE.RV_DOMAIN = 'TIPO_AUTORITA' ) ";
		lStatement_1 += " and ( nvl(RICHIESTA_CONVERSIONE.COD_LUOGO_EMITTENTE,'-') = CODLUOGOEMITTENTE.COD_COMUNE) ";

		return lStatement_1;
	}

}