package siap.siep.scambiosanzione.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.scambiosanzione.model.ScambioSanzioneRichiestaConvModel;

/**
 * <p>
 * Title: ScambioSanzioneRichiestaConvSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta le tabelle ScambioSanzione e RichiestaConversione
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
public class ScambioSanzioneRichiestaConvSqlDAO extends SIAPSqlDAO {

	/*****************************************************************************
	 * Costruttore
	 *
	 * @param con
	 ****************************************************************************/
	public ScambioSanzioneRichiestaConvSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca di ScambioSanzione e RichiestaConversione in base ai dati specificati nel
	 * model di Input
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaScambioSanzioneRichConv(String TipoDec, BigDecimal aFascicolo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQueryPenPec();

		lSql += " AND SCAMBIO_SANZIONE.COD_TIPO_DECISIONE = '" + TipoDec + "' ";
		lSql += " AND RICHIESTA_CONVERSIONE.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicolo;
		lSql += " AND RICHIESTA_CONVERSIONE.DATA_DEPOSITO IS NOT NULL";
		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query di ricercaScambioSanzioneRichConv
	 *
	 * @return
	 ****************************************************************************/
	protected String getSqlQueryPenPec() {
		String lStatement = new String("");

		lStatement += " SELECT " + "SCAMBIO_SANZIONE.ID_SCAMBIO_SANZIONE, "
				+ "SCAMBIO_SANZIONE.COD_TIPO_DECISIONE, "
				+ "D_TIPO_DECISIONE.RV_MEANING DESCR_TIPO_DECISIONE, "
				+ "SCAMBIO_SANZIONE.COD_NATURA_SANZIONE, "
				+ "D_NATURA_SANZIONE.RV_MEANING DESCR_NATURA_SANZIONE, "
				+ "SCAMBIO_SANZIONE.COD_TIPO_SANZIONE, " + "null COD_ESITO, "
				+ "D_TIPO_SANZIONE.RV_MEANING DESCR_TIPO_SANZIONE, " + "SCAMBIO_SANZIONE.DATA_INIZIO, "
				+ "SCAMBIO_SANZIONE.DATA_FINE, " + "SCAMBIO_SANZIONE.NOTE, "
				+ "SCAMBIO_SANZIONE.ANNO_REGISTRO, " + "SCAMBIO_SANZIONE.NUMERO_REGISTRO, "
				+ "SCAMBIO_SANZIONE.CHIAVE_ANNO_FASCICOLO_SIUS, "
				+ "SCAMBIO_SANZIONE.CHIAVE_PROGR_FASCICOLO_SIUS, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_SORVEGLIANZA, "
				+ "D_UFF_SORV.DESCR_TIPO_UFFICIO DESCR_UFFICIO_SORVEGLIANZA, "
				+ "D_UFF_SORV.DESCR_COMUNE COMUNE_UFFICIO_SORVEGLIANZA, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_EMITTENTE, "
				+ "D_UFF_EM.DESCR_TIPO_UFFICIO DESCR_UFFICIO_EMITTENTE, "
				+ "D_UFF_EM.DESCR_COMUNE COMUNE_UFFICIO_EMITTENTE, "
				+ "SCAMBIO_SANZIONE.COD_OPERATORE_INSERIMENTO, " + "SCAMBIO_SANZIONE.DATA_INSERIMENTO, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_INSERIMENTO, "
				+ "D_UFF_INS.DESCR_TIPO_UFFICIO DESCR_UFFICIO_INSERIMENTO, "
				+ "SCAMBIO_SANZIONE.COD_OPERATORE_AGGIORNAMENTO, " + "SCAMBIO_SANZIONE.DATA_AGGIORNAMENTO, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_AGGIORNAMENTO, "
				+ "D_UFF_AGG.DESCR_TIPO_UFFICIO DESCR_UFFICIO_AGGIORNAMENTO, "
				+ "SCAMBIO_SANZIONE.DATA_EMISSIONE, " + "SCAMBIO_SANZIONE.EVE_ID_EVENTO, "
				+ "SCAMBIO_SANZIONE.FAS_SIE_ID_FASCICOLO_SIEP, " +
				// conversione della sanzione sostitutiva paolo c. 3/3/2008
				"SCAMBIO_SANZIONE.NUM_GIORNI_RECLUSIONE, " + "SCAMBIO_SANZIONE.NUM_MESI_RECLUSIONE, "
				+ "SCAMBIO_SANZIONE.NUM_ANNI_RECLUSIONE, " + "SCAMBIO_SANZIONE.NUM_GIORNI_ARRESTO, "
				+ "SCAMBIO_SANZIONE.NUM_MESI_ARRESTO, " + "SCAMBIO_SANZIONE.NUM_ANNI_ARRESTO, " +
				// aggiungo campi di RichiestaConversione
				"RICHIESTA_CONVERSIONE.ID_RICHIESTA_CONVERSIONE, " + "RICHIESTA_CONVERSIONE.ANNO_PARTITA, "
				+ "RICHIESTA_CONVERSIONE.NUM_PARTITA, " + "RICHIESTA_CONVERSIONE.NUM_EX_CAMPIONE, "
				+ "RICHIESTA_CONVERSIONE.PROT_CIRCOSRIZIONE_DOGANALE, "
				+ "RICHIESTA_CONVERSIONE.COD_TIPO_AUTORITA_EMITTENTE, "
				+ "RICHIESTA_CONVERSIONE.COD_LUOGO_EMITTENTE, "
				+ "RICHIESTA_CONVERSIONE.DATA_RICEZIONE_ATTO, "
				+ "RICHIESTA_CONVERSIONE.DATA_ISCRIZIONE_ATTO, " + "RICHIESTA_CONVERSIONE.DATA_ESAZIONE, "
				+ "RICHIESTA_CONVERSIONE.IMPORTO_MULTA, " + "RICHIESTA_CONVERSIONE.DATA_PRESCRIZIONE_MULTA, "
				+ "RICHIESTA_CONVERSIONE.FLAG_IMPRESCRITTIBILE_MULTA, "
				+ "RICHIESTA_CONVERSIONE.IMPORTO_AMMENDA, "
				+ "RICHIESTA_CONVERSIONE.DATA_PRESCRIZIONE_AMMENDA, "
				+ "RICHIESTA_CONVERSIONE.FLAG_IMPRESCRITTIBILE_AMMENDA, "
				+ "RICHIESTA_CONVERSIONE.COD_OPERATORE_INSERIMENTO, "
				+ "RICHIESTA_CONVERSIONE.DATA_INSERIMENTO, "
				+ "RICHIESTA_CONVERSIONE.COD_UFFICIO_INSERIMENTO, "
				+ "RICHIESTA_CONVERSIONE.COD_OPERATORE_AGGIORNAMENTO, "
				+ "RICHIESTA_CONVERSIONE.DATA_AGGIORNAMENTO, "
				+ "RICHIESTA_CONVERSIONE.COD_UFFICIO_AGGIORNAMENTO, "
				+ "RICHIESTA_CONVERSIONE.FAS_SIU_ID_FASCICOLO_SIUS, "
				+ "RICHIESTA_CONVERSIONE.DATA_ANNULLAMENTO, " + "RICHIESTA_CONVERSIONE.COD_TIPO_SANZIONE, "
				+ "RICHIESTA_CONVERSIONE.NOTE, " + "RICHIESTA_CONVERSIONE.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "RICHIESTA_CONVERSIONE.DATA_DEPOSITO, " + "RICHIESTA_CONVERSIONE.EVE_ID_EVENTO, "
				+ "RICHIESTA_CONVERSIONE.DURATA_ESITO_ANNI, " + "RICHIESTA_CONVERSIONE.DURATA_ESITO_MESI, "
				+ "RICHIESTA_CONVERSIONE.DURATA_ESITO_GIORNI, " + "RICHIESTA_CONVERSIONE.NUMERO_RATE, "
				+ "RICHIESTA_CONVERSIONE.VALORE_RATA, " + "RICHIESTA_CONVERSIONE.VALORE_ULTIMA_RATA, "
				+ "RICHIESTA_CONVERSIONE.DATA_INIZIO_PAGAMENTO, "
				+ "RICHIESTA_CONVERSIONE.NUMERO_GIORNI_INIZIO_PAGAMENTO ";

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		// 17/02/2016 INIZIO lStatement += " FROM EVENTO, RICHIESTA_CONVERSIONE, SCAMBIO_SANZIONE ";
		lStatement += " FROM EVENTO, EVENTO EV1, RICHIESTA_CONVERSIONE, SCAMBIO_SANZIONE "; // 17/02/2016 FINE
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_TIPO_DECISIONE ON (SCAMBIO_SANZIONE.COD_TIPO_DECISIONE = D_TIPO_DECISIONE.RV_LOW_VALUE"
				+ " AND D_TIPO_DECISIONE.RV_DOMAIN='TIPO_PROVVEDIMENTO' )";
		// Ticket#20190913017 — Minori SIEP - SELEZIONE PROVVEDIMENTO DELLA SORVEGLIANZA DALLA LISTA -
		// conversione pene pecuniarie: aggiunta OR condition per estrarre la descr della NATURA SANZIONE
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_NATURA_SANZIONE ON ((SCAMBIO_SANZIONE.COD_NATURA_SANZIONE ="
				+ " D_NATURA_SANZIONE.RV_LOW_VALUE OR SCAMBIO_SANZIONE.COD_NATURA_SANZIONE ="
				+ " D_NATURA_SANZIONE.RV_HIGH_VALUE)"
				+ " AND D_NATURA_SANZIONE.RV_DOMAIN = 'ESITO_PROVVEDIMENTO')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_TIPO_SANZIONE ON (SCAMBIO_SANZIONE.COD_TIPO_SANZIONE = D_TIPO_SANZIONE.RV_LOW_VALUE"
				+ " AND D_TIPO_SANZIONE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO')";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_SORV ON (SCAMBIO_SANZIONE.COD_UFFICIO_SORVEGLIANZA = D_UFF_SORV.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_EM ON (SCAMBIO_SANZIONE.COD_UFFICIO_EMITTENTE = D_UFF_EM.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_INS ON (SCAMBIO_SANZIONE.COD_UFFICIO_INSERIMENTO = D_UFF_INS.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_AGG ON (SCAMBIO_SANZIONE.COD_UFFICIO_AGGIORNAMENTO = D_UFF_AGG.COD_UFFICIO)";

		lStatement += " WHERE";
		lStatement += " SCAMBIO_SANZIONE.EVE_ID_EVENTO = EVENTO.ID_EVENTO AND";
		lStatement += " SCAMBIO_SANZIONE.FAS_SIE_ID_FASCICOLO_SIEP = RICHIESTA_CONVERSIONE.FAS_SIE_ID_FASCICOLO_SIEP AND";
		lStatement += " EVENTO.FLAG_DOCUMENTO_REGISTRATO ='S'";
		// 17/02/2016 INIZIO
		lStatement += " AND RICHIESTA_CONVERSIONE.EVE_ID_EVENTO = EV1.ID_EVENTO";
		lStatement += " AND EV1.FLAG_DOCUMENTO_REGISTRATO ='S'";
		// 17/02/2016 FINE
		return lStatement;
	}

	/********************************************************************************************
	 * 10/03/2015 Metodo per la costruzione della sql query di ricercaScambioSanzioneRichConv EPS
	 *
	 * @return
	 ********************************************************************************************/
	protected String getSqlQueryPenPecEPS() {
		String lStatement = new String("");

		lStatement += " SELECT " + "SCAMBIO_SANZIONE.ID_SCAMBIO_SANZIONE, "
				+ "SCAMBIO_SANZIONE.COD_TIPO_DECISIONE, "
				+ "D_TIPO_DECISIONE.RV_MEANING DESCR_TIPO_DECISIONE, "
				+ "SCAMBIO_SANZIONE.COD_NATURA_SANZIONE, "
				+ "D_NATURA_SANZIONE.RV_MEANING DESCR_NATURA_SANZIONE, "
				+ "SCAMBIO_SANZIONE.COD_TIPO_SANZIONE, " + "D_TIPO_SANZIONE.RV_MEANING DESCR_TIPO_SANZIONE, "
				+ "CORRISPONDENZA.RV_ALT3_VALUE COD_ESITO, " + "SCAMBIO_SANZIONE.DATA_INIZIO, "
				+ "SCAMBIO_SANZIONE.DATA_FINE, " + "SCAMBIO_SANZIONE.NOTE, "
				+ "SCAMBIO_SANZIONE.ANNO_REGISTRO, " + "SCAMBIO_SANZIONE.NUMERO_REGISTRO, "
				+ "SCAMBIO_SANZIONE.CHIAVE_ANNO_FASCICOLO_SIUS, "
				+ "SCAMBIO_SANZIONE.CHIAVE_PROGR_FASCICOLO_SIUS, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_SORVEGLIANZA, "
				+ "D_UFF_SORV.DESCR_TIPO_UFFICIO DESCR_UFFICIO_SORVEGLIANZA, "
				+ "D_UFF_SORV.DESCR_COMUNE COMUNE_UFFICIO_SORVEGLIANZA, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_EMITTENTE, "
				+ "D_UFF_EM.DESCR_TIPO_UFFICIO DESCR_UFFICIO_EMITTENTE, "
				+ "D_UFF_EM.DESCR_COMUNE COMUNE_UFFICIO_EMITTENTE, "
				+ "SCAMBIO_SANZIONE.COD_OPERATORE_INSERIMENTO, " + "SCAMBIO_SANZIONE.DATA_INSERIMENTO, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_INSERIMENTO, "
				+ "D_UFF_INS.DESCR_TIPO_UFFICIO DESCR_UFFICIO_INSERIMENTO, "
				+ "SCAMBIO_SANZIONE.COD_OPERATORE_AGGIORNAMENTO, " + "SCAMBIO_SANZIONE.DATA_AGGIORNAMENTO, "
				+ "SCAMBIO_SANZIONE.COD_UFFICIO_AGGIORNAMENTO, "
				+ "D_UFF_AGG.DESCR_TIPO_UFFICIO DESCR_UFFICIO_AGGIORNAMENTO, "
				+ "SCAMBIO_SANZIONE.DATA_EMISSIONE, " + "SCAMBIO_SANZIONE.EVE_ID_EVENTO, "
				+ "SCAMBIO_SANZIONE.FAS_SIE_ID_FASCICOLO_SIEP, " + "SCAMBIO_SANZIONE.NUM_GIORNI_RECLUSIONE, "
				+ "SCAMBIO_SANZIONE.NUM_MESI_RECLUSIONE, " + "SCAMBIO_SANZIONE.NUM_ANNI_RECLUSIONE, "
				+ "SCAMBIO_SANZIONE.NUM_GIORNI_ARRESTO, " + "SCAMBIO_SANZIONE.NUM_MESI_ARRESTO, "
				+ "SCAMBIO_SANZIONE.NUM_ANNI_ARRESTO, " + "RICHIESTA_CONVERSIONE.ID_RICHIESTA_CONVERSIONE, "
				+ "RICHIESTA_CONVERSIONE.ANNO_PARTITA, " + "RICHIESTA_CONVERSIONE.NUM_PARTITA, "
				+ "RICHIESTA_CONVERSIONE.NUM_EX_CAMPIONE, "
				+ "RICHIESTA_CONVERSIONE.PROT_CIRCOSRIZIONE_DOGANALE, "
				+ "RICHIESTA_CONVERSIONE.COD_TIPO_AUTORITA_EMITTENTE, "
				+ "RICHIESTA_CONVERSIONE.COD_LUOGO_EMITTENTE, "
				+ "RICHIESTA_CONVERSIONE.DATA_RICEZIONE_ATTO, "
				+ "RICHIESTA_CONVERSIONE.DATA_ISCRIZIONE_ATTO, " + "RICHIESTA_CONVERSIONE.DATA_ESAZIONE, "
				+ "RICHIESTA_CONVERSIONE.IMPORTO_MULTA, " + "RICHIESTA_CONVERSIONE.DATA_PRESCRIZIONE_MULTA, "
				+ "RICHIESTA_CONVERSIONE.FLAG_IMPRESCRITTIBILE_MULTA, "
				+ "RICHIESTA_CONVERSIONE.IMPORTO_AMMENDA, "
				+ "RICHIESTA_CONVERSIONE.DATA_PRESCRIZIONE_AMMENDA, "
				+ "RICHIESTA_CONVERSIONE.FLAG_IMPRESCRITTIBILE_AMMENDA, "
				+ "RICHIESTA_CONVERSIONE.COD_OPERATORE_INSERIMENTO, "
				+ "RICHIESTA_CONVERSIONE.DATA_INSERIMENTO, "
				+ "RICHIESTA_CONVERSIONE.COD_UFFICIO_INSERIMENTO, "
				+ "RICHIESTA_CONVERSIONE.COD_OPERATORE_AGGIORNAMENTO, "
				+ "RICHIESTA_CONVERSIONE.DATA_AGGIORNAMENTO, "
				+ "RICHIESTA_CONVERSIONE.COD_UFFICIO_AGGIORNAMENTO, "
				+ "RICHIESTA_CONVERSIONE.FAS_SIU_ID_FASCICOLO_SIUS, "
				+ "RICHIESTA_CONVERSIONE.DATA_ANNULLAMENTO, " + "RICHIESTA_CONVERSIONE.COD_TIPO_SANZIONE, "
				+ "RICHIESTA_CONVERSIONE.NOTE, " + "RICHIESTA_CONVERSIONE.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "RICHIESTA_CONVERSIONE.DATA_DEPOSITO, " + "RICHIESTA_CONVERSIONE.EVE_ID_EVENTO, "
				+ "RICHIESTA_CONVERSIONE.DURATA_ESITO_ANNI, " + "RICHIESTA_CONVERSIONE.DURATA_ESITO_MESI, "
				+ "RICHIESTA_CONVERSIONE.DURATA_ESITO_GIORNI, " + "RICHIESTA_CONVERSIONE.NUMERO_RATE, "
				+ "RICHIESTA_CONVERSIONE.VALORE_RATA, " + "RICHIESTA_CONVERSIONE.VALORE_ULTIMA_RATA, "
				+ "RICHIESTA_CONVERSIONE.DATA_INIZIO_PAGAMENTO, "
				+ "RICHIESTA_CONVERSIONE.NUMERO_GIORNI_INIZIO_PAGAMENTO ";

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM EVENTO, RICHIESTA_CONVERSIONE, SCAMBIO_SANZIONE ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_TIPO_DECISIONE ON (SCAMBIO_SANZIONE.COD_TIPO_DECISIONE = D_TIPO_DECISIONE.RV_LOW_VALUE"
				+ " AND D_TIPO_DECISIONE.RV_DOMAIN='TIPO_PROVVEDIMENTO' )";
		// Ticket#20190913017 — Minori SIEP - SELEZIONE PROVVEDIMENTO DELLA SORVEGLIANZA DALLA LISTA -
		// conversione pene pecuniarie: aggiunta OR condition per estrarre la descr della NATURA SANZIONE
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_NATURA_SANZIONE ON ((SCAMBIO_SANZIONE.COD_NATURA_SANZIONE ="
				+ " D_NATURA_SANZIONE.RV_LOW_VALUE OR SCAMBIO_SANZIONE.COD_NATURA_SANZIONE ="
				+ " D_NATURA_SANZIONE.RV_HIGH_VALUE)"
				// lStatement += " LEFT OUTER JOIN CG_REF_CODES D_NATURA_SANZIONE ON
				// (SCAMBIO_SANZIONE.COD_NATURA_SANZIONE = D_NATURA_SANZIONE.RV_LOW_VALUE"
				+ " AND D_NATURA_SANZIONE.RV_DOMAIN = 'ESITO_PROVVEDIMENTO')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES D_TIPO_SANZIONE ON (SCAMBIO_SANZIONE.COD_TIPO_SANZIONE = D_TIPO_SANZIONE.RV_LOW_VALUE"
				+ " AND D_TIPO_SANZIONE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO')";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES CORRISPONDENZA ON (CORRISPONDENZA.RV_LOW_VALUE = SCAMBIO_SANZIONE.COD_NATURA_SANZIONE"
				+ " AND CORRISPONDENZA.RV_DOMAIN = 'ESITO_PROVVEDIMENTO')";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_SORV ON (SCAMBIO_SANZIONE.COD_UFFICIO_SORVEGLIANZA = D_UFF_SORV.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_EM ON (SCAMBIO_SANZIONE.COD_UFFICIO_EMITTENTE = D_UFF_EM.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_INS ON (SCAMBIO_SANZIONE.COD_UFFICIO_INSERIMENTO = D_UFF_INS.COD_UFFICIO)";
		lStatement += " LEFT OUTER JOIN UFFICIO_DESCR  D_UFF_AGG ON (SCAMBIO_SANZIONE.COD_UFFICIO_AGGIORNAMENTO = D_UFF_AGG.COD_UFFICIO)";

		lStatement += " WHERE";
		lStatement += " SCAMBIO_SANZIONE.EVE_ID_EVENTO = EVENTO.ID_EVENTO AND";
		lStatement += " SCAMBIO_SANZIONE.FAS_SIE_ID_FASCICOLO_SIEP = RICHIESTA_CONVERSIONE.FAS_SIE_ID_FASCICOLO_SIEP AND";
		lStatement += " EVENTO.FLAG_DOCUMENTO_REGISTRATO ='S'";
		lStatement += " AND EVENTO.COD_ESITO NOT IN ('0156', '0157','0158') "; // 27/07/2015
		return lStatement;
	}

	/*****************************************************************************
	 * 09/03/2015 Effettua la ricerca di ScambioSanzione e RichiestaConversione in base ai dati specificati
	 * nel model di Input riferiti a un range di ESITO_PROVVEDIMENTO specifico
	 *
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaScambioSanzioneRichConvEPS(String TipoDec, BigDecimal aFascicolo) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQueryPenPecEPS();

		lSql += " AND SCAMBIO_SANZIONE.COD_TIPO_DECISIONE = " + TipoDec;
		lSql += " AND RICHIESTA_CONVERSIONE.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicolo;
		lSql += " AND RICHIESTA_CONVERSIONE.DATA_DEPOSITO IS NOT NULL";
		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 *
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		ScambioSanzioneRichiestaConvModel aModel = new ScambioSanzioneRichiestaConvModel();
		ScambioSanzioneModel sModel = new ScambioSanzioneModel();
		RichiestaConversioneModel rModel = new RichiestaConversioneModel();

		// Campi relativi alla parte ScambioSanzione
		sModel.setIdScambioSanzione(getBigDecimal("ID_SCAMBIO_SANZIONE"));
		sModel.setCodTipoDecisione(getString("COD_TIPO_DECISIONE"));
		sModel.setCodNaturaSanzione(getString("COD_NATURA_SANZIONE"));
		sModel.setCodTipoSanzione(getString("COD_TIPO_SANZIONE"));
		sModel.setDataInizio(getDate("DATA_INIZIO"));
		sModel.setDataFine(getDate("DATA_FINE"));
		sModel.setNote(getString("NOTE"));
		sModel.setAnnoRegistro(getBigDecimal("ANNO_REGISTRO"));
		sModel.setNumeroRegistro(getBigDecimal("NUMERO_REGISTRO"));
		sModel.setChiaveAnnoFascicoloSius(getBigDecimal("CHIAVE_ANNO_FASCICOLO_SIUS"));
		sModel.setChiaveProgrFascicoloSius(getBigDecimal("CHIAVE_PROGR_FASCICOLO_SIUS"));
		sModel.setCodUfficioSorveglianza(getString("COD_UFFICIO_SORVEGLIANZA"));
		sModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		sModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		sModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		sModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		sModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		sModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		sModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		sModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		sModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		sModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		sModel.setDescrTipoDecisione(getString("DESCR_TIPO_DECISIONE"));
		sModel.setDescrNaturaSanzione(getString("DESCR_NATURA_SANZIONE"));
		sModel.setDescrTipoSanzione(getString("DESCR_TIPO_SANZIONE"));
		sModel.setDescrUfficioSorveglianza(getString("DESCR_UFFICIO_SORVEGLIANZA"));
		sModel.setComuneUfficioSorveglianza(getString("COMUNE_UFFICIO_SORVEGLIANZA"));
		sModel.setDescrUfficioEmittente(getString("DESCR_UFFICIO_EMITTENTE"));
		sModel.setComuneUfficioEmittente(getString("COMUNE_UFFICIO_EMITTENTE"));
		sModel.setDescrUfficioInserimento(getString("DESCR_UFFICIO_INSERIMENTO"));
		sModel.setDescrUfficioAggiornamento(getString("DESCR_UFFICIO_AGGIORNAMENTO"));
		// conversione della sanzione sostitutiva paolo c. 3/3/2008
		sModel.setNumGiorniReclusione(getBigDecimal("NUM_GIORNI_RECLUSIONE"));
		sModel.setNumMesiReclusione(getBigDecimal("NUM_MESI_RECLUSIONE"));
		sModel.setNumAnniReclusione(getBigDecimal("NUM_ANNI_RECLUSIONE"));
		sModel.setNumGiorniArresto(getBigDecimal("NUM_GIORNI_ARRESTO"));
		sModel.setNumMesiArresto(getBigDecimal("NUM_MESI_ARRESTO"));
		sModel.setNumAnniArresto(getBigDecimal("NUM_ANNI_ARRESTO"));

		aModel.setScambioSanzione(sModel);
		// Campi relativi alla parte RichiestaConversione

		rModel.setIdRichiestaConversione(getBigDecimal("ID_RICHIESTA_CONVERSIONE"));
		rModel.setAnnoPartita(getBigDecimal("ANNO_PARTITA"));
		rModel.setNumPartita(getBigDecimal("NUM_PARTITA"));
		rModel.setNumExCampione(getString("NUM_EX_CAMPIONE"));
		rModel.setProtCircosrizioneDoganale(getString("PROT_CIRCOSRIZIONE_DOGANALE"));
		rModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		rModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		rModel.setDataRicezioneAtto(getDate("DATA_RICEZIONE_ATTO"));
		rModel.setDataIscrizioneAtto(getDate("DATA_ISCRIZIONE_ATTO"));
		rModel.setDataEsazione(getDate("DATA_ESAZIONE"));
		rModel.setImportoMulta(getBigDecimal("IMPORTO_MULTA"));
		rModel.setDataPrescrizioneMulta(getDate("DATA_PRESCRIZIONE_MULTA"));
		rModel.setFlagImprescrittibileMulta(getString("FLAG_IMPRESCRITTIBILE_MULTA"));
		rModel.setImportoAmmenda(getBigDecimal("IMPORTO_AMMENDA"));
		rModel.setDataPrescrizioneAmmenda(getDate("DATA_PRESCRIZIONE_AMMENDA"));
		rModel.setFlagImprescrittibileAmmenda(getString("FLAG_IMPRESCRITTIBILE_AMMENDA"));
		rModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		rModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		rModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		rModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		rModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		rModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		rModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		rModel.setDataAnnullamento(getDate("DATA_ANNULLAMENTO"));
		rModel.setCodTipoSanzione(getString("COD_TIPO_SANZIONE"));
		rModel.setNote(getString("NOTE"));
		rModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		rModel.setDataDeposito(getDate("DATA_DEPOSITO"));
		rModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		rModel.setDurataEsitoAnni(getBigDecimal("DURATA_ESITO_ANNI"));
		rModel.setDurataEsitoMesi(getBigDecimal("DURATA_ESITO_MESI"));
		rModel.setDurataEsitoGiorni(getBigDecimal("DURATA_ESITO_GIORNI"));
		rModel.setNumeroRate(getBigDecimal("NUMERO_RATE"));
		rModel.setValoreRata(getBigDecimal("VALORE_RATA"));
		rModel.setValoreUltimaRata(getBigDecimal("VALORE_ULTIMA_RATA"));
		rModel.setDataInizioPagamento(getDate("DATA_INIZIO_PAGAMENTO"));
		rModel.setNumeroGiorniInizioPagamento(getBigDecimal("NUMERO_GIORNI_INIZIO_PAGAMENTO"));

		aModel.setRichiestaConv(rModel);

		aModel.setCodEsito(getBigDecimal("COD_ESITO"));

		return aModel;
	}

}