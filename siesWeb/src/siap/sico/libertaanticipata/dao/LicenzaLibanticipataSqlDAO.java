package siap.sico.libertaanticipata.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.dao.SIAPSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.utente.model.DatiOperazioneModel;

/**
 * LicenzaLibanticipataSqlDAO - Classe SqlDAO che rappresenta la tabella LicenzaLibanticipata
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LicenzaLibanticipataSqlDAO extends SIAPSqlDAO {

	public LicenzaLibanticipataSqlDAO(Connection con) {

		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaLicenzaLibanticipata(LicenzaLibAnticipataModel aModel) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaLicenzaLibanticipataUltimaByIDFascicoloSIEP(BigDecimal aFascID) {

		String lSql = getSqlQuery();
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aFascID;
		lSql += " AND (COD_TIPO_LICENZA = 'LA')"; // Considera solo le Liberazioni Anticipate
		lSql += " AND ID_LICENZA_LIBANTICIPATA=(SELECT MAX(ID_LICENZA_LIBANTICIPATA) FROM "
				+ "LICENZA_LIBANTICIPATA WHERE FAS_SIE_ID_FASCICOLO_SIEP=" + aFascID + ")";
		setStatement(lSql);
	}

	/**
	 *
	 * @param aFascID
	 * @param aFlagElaborato
	 */
	public void ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP(BigDecimal aFascID,
			String aFlagElaborato) {

		ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP(aFascID, aFlagElaborato, null);
	}

	/**
	 * Ricerca le LA associate al Fascicolo con FLAG_ELABORATO legato al valore passato in input.
	 *
	 * @param aFascID
	 * @param aFlagElaborato
	 * @param aDL92
	 *            se <> null include anche le Licenze 'RD' del DL 92
	 */
	public void ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP(BigDecimal aFascID,
			String aFlagElaborato, String aDL92) {

		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aFascID;
		// lSql += " AND (FLAG_CONCESSO = 'C')"; Anna ottobre 2010
		lSql += " AND (FLAG_CONCESSO = 'C' OR FLAG_CONCESSO='S')";

		// lSql += " AND (COD_TIPO_LICENZA = 'LA')"; // Considera solo le Liberazioni Anticipate
		// Anna ottobre 2010 per scomputo permesso
		if (aDL92 == null)
			lSql += " AND (COD_TIPO_LICENZA = 'LA' or COD_TIPO_LICENZA = 'EP' or COD_TIPO_LICENZA = 'PP' )";
		else
			lSql += " AND (COD_TIPO_LICENZA = 'LA' or COD_TIPO_LICENZA = 'EP' or COD_TIPO_LICENZA = 'PP' "
					+ "or COD_TIPO_LICENZA = 'RD')";

		// lSql += " AND (EVE_ID_EVENTO IS NOT NULL)"; // Per risolvere il problema del cumulo che non associa
		// nessun evento alla Liberazione Anticipata
		lSql += " AND (FLAG_ELABORATO != 'A' OR FLAG_ELABORATO IS NULL)";
		// Non considera legate ad Eventi annullati (tale flag viene settato lato SIUS)
		if (aFlagElaborato != null && "N".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL)";
		if (aFlagElaborato != null && "S".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'S')";
		if (aFlagElaborato != null && "SF".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'S' OR FLAG_ELABORATO = 'F')";
		if (aFlagElaborato != null && "E".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'E')";
		if (aFlagElaborato != null && "NE".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL OR FLAG_ELABORATO = 'E')";
		lSql += " " + setOrderLicenzaLib();

		setStatement(lSql);
	}

	/**
	 * Come ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP, ma modificata conforme al nuovo calcolo pena
	 * per evitare di computare anche le LA inserite prima di un evento interruttivo e non computate.
	 *
	 * NOTA: Questo metodo viene utilizzato nella classe StampaEventoUtils per caricare nell'XML tutte le
	 * liberazioni anticipate, e non solo quelle concesse (FLAG_ELABORATO a C). Per il calcolo del totale
	 * vengono vengono filtrati solo quelli di tipo "C" nello StampaEventoUtils
	 *
	 * @param aFascID
	 * @param aFlagElaborato
	 */
	public void ricercaLicenzaLibanticipataByIDFascicoloSIEPNew(BigDecimal aFascID, Date aDataDal,
			Date aDataAl) {

		String lStatement = new String("");

		lStatement += " SELECT LA.ID_LICENZA_LIBANTICIPATA, LA.COD_TIPO_LICENZA, "
				+ "TIPO_LICENZA.RV_MEANING DESCR_TIPO_LICENZA, LA.NUMERO_GIORNI, "
				+ "LA.DATA_INIZIO, LA.ORA_INIZIO, LA.DATA_FINE, LA.ORA_FINE, "
				+ "LA.LUOGO_SVOLGIMENTO_PROVA, LA.DATA_DETENZ_RIF_DA, LA.DATA_DETENZ_RIF_A, "
				+ "LA.FLAG_INFRAZIONE_OBBLIGHI, LA.DATA_INFRAZIONE_OBBLIGHI, "
				+ "DESCR_INFRAZIONE_OBBLIGHI, LA.FLAG_SCOMPUTO, "
				+ "LA.COD_OPERATORE_INSERIMENTO, LA.DATA_INSERIMENTO, LA.COD_UFFICIO_INSERIMENTO, "
				+ "LA.COD_OPERATORE_AGGIORNAMENTO, LA.DATA_AGGIORNAMENTO, LA.COD_UFFICIO_AGGIORNAMENTO, "
				+ "LA.FAS_SIU_ID_FASCICOLO_SIUS, LA.EVE_ID_EVENTO, LA.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "LA.FLAG_CONCESSO, LA.FLAG_ELABORATO, NVL(LA.FLAG_SCORTA,'N') FLAG_SCORTA, "
				+ "LA.COD_STATO_PERMESSO, LA.DESCR_STATO_PERMESSO, "
				+ "STATO_PERMESSO.RV_MEANING DESCR_STATO_PERMESSO2, LA.NUMERO_ORE, "
				+ "LA.ANNO_SIUS, LA.NUMERO_SIUS, LA.ANNO_ORDINANZA, LA.NUMERO_ORDINANZA, "
				+ "LA.COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , "
				+ "LA.COD_LUOGO_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, "
				// MEV10-s3: aggiunto campo in estrazione
				+ "UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFFICIO_EMITTENTE, LA.DATA_EMISSIONE_ORDINANZA " +
				// 20/06/2008 aggiunta campi.
				", LA.GIORNI_SCOMPUTATI, LA.NUMERO_ORE_NO_FRUITE, LA.NUMERO_GIORNI_NO_FRUITI, LA.COD_ESITO,"
				+ " LA.DATA_ANNOTAZIONE_ESITO, LA.ANNOTAZIONE,"
				+ " LA.NUMERO_GIORNI,LA.NUMERO_MESI, LA.SOMMA_RISARC_DANNI ";
		lStatement += "FROM LICENZA_LIBANTICIPATA LA, EVENTO ";
		lStatement += ", CG_REF_CODES TIPO_LICENZA, CG_REF_CODES STATO_PERMESSO ";
		lStatement += ", COMUNE LUOGO_EMITTENTE, UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI";
		lStatement += " WHERE (TIPO_LICENZA.RV_DOMAIN = 'TIPO_LICENZA' AND"
				+ " NVL(LA.COD_TIPO_LICENZA,'01') = TIPO_LICENZA.RV_LOW_VALUE) ";
		lStatement += " AND (STATO_PERMESSO.RV_DOMAIN = 'STATO_PERMESSO' AND"
				+ " NVL(LA.COD_STATO_PERMESSO,'99') = STATO_PERMESSO.RV_LOW_VALUE) ";
		lStatement += " AND (NVL(LA.COD_LUOGO_EMITTENTE, '-') = LUOGO_EMITTENTE.COD_COMUNE)";
		lStatement += " AND UFF_EMI.COD_UFFICIO = LA.COD_UFFICIO_EMITTENTE AND"
				+ " UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND LA.FAS_SIE_ID_FASCICOLO_SIEP=" + aFascID;
		// lStatement += " AND (LA.FLAG_CONCESSO = 'C')";
		// Paolo Cherubini 23/11/2010 vedi anche Anna ottobre 2010 per scomputo permesso
		// lStatement += " AND (LA.COD_TIPO_LICENZA = 'LA')"; // Considera solo le Liberazioni Anticipate
		lStatement += " AND (LA.COD_TIPO_LICENZA = 'LA' or LA.COD_TIPO_LICENZA = 'EP'"
				+ " or LA.COD_TIPO_LICENZA = 'PP' )";
		// Non considera quelle legate ad Eventi annullati (tale flag viene settato lato SIUS)
		lStatement += " AND (LA.FLAG_ELABORATO != 'A' OR LA.FLAG_ELABORATO IS NULL)";
		// ==========================================================================
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		lStatement += " AND LA.EVE_ID_EVENTO = evento.ID_EVENTO ";
		String aFlagDocumentoRegistrato = "X";
		String lSql = getCondizioniLA(aFascID, aDataDal, aDataAl, aFlagDocumentoRegistrato);
		lStatement += lSql;
		// Paolo Cherubini 29/11/2010 non vengono estratte tutte le LA, poiche' questa ricerca viene usata
		// solo
		// nella stampa
		// provo ad asteriscare tutto il codice seguente
		/*
		 * //========================================================================== // La seguente parte
		 * aggiunge due condizioni: // 1) la LA deve essere collegata a una ordinaza (01-03-2130) a sua //
		 * volta collegata a un provvedimento (Comunicazione/Ordine di scarcerazione) // validato,
		 * eventualmente nel periodo specificato in input // 2) la LA deve essere collegata a un evento di
		 * stampa Cumulo validato, // eventualmente nel periodo specificato in input
		 * //========================================================================== lStatement +=
		 * " AND ( "; // Condizione sulle LA concesse con ORDINANZA TDS o UDS lStatement +=
		 * " (((evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO='03' AND evento.COD_MOTIVO='2130')"
		 * ; // Liberazione Anticipata - UDS lStatement +=
		 * " OR  (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO='03' AND evento.COD_MOTIVO='0076')"
		 * ; // Liberazione Anticipata - TDS lStatement +=
		 * " OR  (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO='03' AND evento.COD_MOTIVO='0113')"
		 * ; // Reclamo su Liberazione Anticipata lStatement += ") "; // Aggiungo la select che filtra solo le
		 * ordinanze collegate a provvedimenti // di comunicazione (01-12-0923) o ordine di scarcerazione
		 * (01-09-0081) emessi // in fase di acquisizione dell'ordinanza di concessione LA e validati, //
		 * eventualmente nel periodo specificato in input lStatement +=
		 * " and evento.ID_EVENTO in ( select evento.EVE_ID_EVENTO "; lStatement += " from evento ";
		 * lStatement += " where evento.FAS_SIE_ID_FASCICOLO_SIEP = "+aFascID; // lStatement +=
		 * " and evento.FLAG_DOCUMENTO_REGISTRATO = 'S' "; if (aDataDal!=null){ lStatement +=
		 * "                                and evento.DATA_INSERIMENTO > to_date ('"
		 * +DateUtils.getDateToString(aDataDal,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')"; } // n.b.
		 * devo inserire <= per poter prendere in considerazion anche le LA // associate all'evento di
		 * Riferimento (se validato) if (aDataAl!=null){ lStatement +=
		 * "                                and evento.DATA_INSERIMENTO <= to_date ('"
		 * +DateUtils.getDateToString(aDataAl,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')"; }
		 * lStatement +=
		 * "and ((evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO='12' AND evento.COD_MOTIVO='0923')"
		 * ; // comunicazione - Concessione Liberazione Anticipata - condannato Libero lStatement +=
		 * "or (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO='12' AND evento.COD_MOTIVO='0922')"
		 * ; // comunicazione - Concessione Liberazione Anticipata - condannato in Ergastolo lStatement +=
		 * "or (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO='09' AND evento.COD_MOTIVO='0081')"
		 * ; // ordine di scarcerazione - Nuova scadenza pena a seguito concessione Liberazione Anticipata -
		 * condannato detenuto lStatement +=
		 * "or (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO = '09' AND evento.COD_MOTIVO='0083')"
		 * ; // ordine di scarcerazione - Nuova scadenza pena a seguito concessione Liberazione Anticipata -
		 * condannato in misura alternativa lStatement += "                                      )";
		 * lStatement += "                              )"; lStatement += "   )"; // Fine condizione sulle LA
		 * concesse con ORDINANZA // Aggiungo la select che filtra solo le LA collegate a eventi di // STAMPA
		 * CUMULO che validano anche le LA. n.b. nel caso di cumulo non // ho un evento intermedio
		 * (ordinanza), ma la LA e' direttamente collegata // al provvedimento di Cumulo lStatement += "OR
		 * (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO = '04' AND evento.COD_MOTIVO
		 * in('0222','0223','0224','0277') " ; if (aDataDal!=null){ // n.b. verificare se la data dal deve
		 * essere >=. Infatti la data dal e' la // data dell'evento 'Pena Iniziale', ma il cumulo e' proprio
		 * uno di // questi eventi, per cui se metto > (strettamente) rischio di ecludere // le LA del CUMULO
		 * lStatement += "  and evento.DATA_INSERIMENTO >= to_date ('"
		 * +DateUtils.getDateToString(aDataDal,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')"; } if
		 * (aDataAl!=null){ lStatement +=
		 * "  and evento.DATA_INSERIMENTO <= to_date ('"+DateUtils.getDateToString
		 * (aDataAl,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')"; } lStatement += "   )"; // fine OR //
		 * Aggiungo eventuale OR per le LA inserite com la pena residua manuale lStatement +=
		 * "OR (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO='04' AND evento.COD_MOTIVO='0925'"
		 * ; if (aDataDal!=null){ // n.b. verificare se la data dal deve essere >=. Infatti la data dal e' la
		 * // data dell'evento 'Pena Iniziale', ma il cumulo e' proprio uno di // questi eventi, per cui se
		 * metto > (strettamente) rischio di ecludere // le LA del CUMULO lStatement +=
		 * "  and evento.DATA_INSERIMENTO >= to_date ('"
		 * +DateUtils.getDateToString(aDataDal,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";} if
		 * (aDataAl!=null){ lStatement +=
		 * "  and evento.DATA_INSERIMENTO <= to_date ('"+DateUtils.getDateToString
		 * (aDataAl,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')"; } lStatement += ")"; // fine OR
		 * lStatement += ")";
		 */
		// fine Paolo Cherubini 29/11/2010

		lStatement += " " + setOrderLicenzaLib();

		setStatement(lStatement);
	}

	public void ricercaLicenzaLibanticipataByIDFascicoloSIEP(BigDecimal aFascID, String aFlagElaborato) {

		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aFascID;
		// lSql += " AND (FLAG_CONCESSO = 'C')";
		lSql += " AND (COD_TIPO_LICENZA = 'LA')"; // Considera solo le Liberazioni Anticipate
		// lSql += " AND (EVE_ID_EVENTO IS NOT NULL)"; // Per risolvere il problema del cumulo che non associa
		// nessun evento alla Liberazione Anticipata
		// Non considera legate ad Eventi annullati (tale flag viene settato lato SIUS)
		lSql += " AND (FLAG_ELABORATO != 'A' OR FLAG_ELABORATO IS NULL)";
		if (aFlagElaborato != null && "N".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL)";
		if (aFlagElaborato != null && "S".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'S')";
		if (aFlagElaborato != null && "SF".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'S' OR FLAG_ELABORATO = 'F')";
		if (aFlagElaborato != null && "E".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'E')";
		if (aFlagElaborato != null && "NE".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL OR FLAG_ELABORATO = 'E')";

		lSql += " " + setOrderLicenzaLib();

		setStatement(lSql);
	}

	public void ricercaLicenzaLibanticipataNonConcesseByIDFascicoloSIEP(BigDecimal aFascID,
			BigDecimal aKeyEvento) {

		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aFascID;
		lSql += " AND FLAG_CONCESSO IN ('R','I','N')";
		lSql += " AND COD_TIPO_LICENZA = 'LA'"; // Considera solo le Liberazioni Anticipate
		if (aKeyEvento != null) {
			lSql += " AND EVE_ID_EVENTO = " + aKeyEvento;
		}
		lSql += " " + setOrderLicenzaLib();

		setStatement(lSql);
	}

	public void ricercaLicenzaLibanticipataConcesseDepositate(BigDecimal aFascID, String aFlagElaborato) {

		String lSql = getSqlEventoQuery();

		lSql += " AND LIC.FAS_SIE_ID_FASCICOLO_SIEP=" + aFascID;
		lSql += " AND (FLAG_CONCESSO = 'C')";
		lSql += " AND (COD_TIPO_LICENZA = 'LA')"; // Considera solo le Liberazioni Anticipate
		// Considera le LA legate a ordinanze depositate
		// (quelle cioe' che hanno DATA_TRASMISSIONE valorizzata come DATA_DEPOSITO della tabella
		// DEPOSITO_ORDINANZA_PC)
		lSql += " AND (EVENTO.DATA_TRASMISSIONE_ATTI IS NOT NULL)";
		lSql += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO in ('02','03')  )";
		// Non considera legate ad Eventi annullati (tale flag viene settato lato SIUS)
		lSql += " AND (FLAG_ELABORATO != 'A' OR FLAG_ELABORATO IS NULL)";
		if (aFlagElaborato != null && "N".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL)";
		if (aFlagElaborato != null && "S".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'S')";
		if (aFlagElaborato != null && "E".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'E')";
		if (aFlagElaborato != null && "NE".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL OR FLAG_ELABORATO = 'E')";
		lSql += " " + setOrderLicenzaLib();

		setStatement(lSql);
	}

	public void ricercaRidimLicenzaLibanticipataConcesseDepositate(BigDecimal aFascID,
			String aFlagElaborato) {

		String lSql = getSqlEventoQuery();

		lSql += " AND LIC.FAS_SIE_ID_FASCICOLO_SIEP=" + aFascID;
		lSql += " AND (FLAG_CONCESSO = 'C')";
		lSql += " AND (COD_TIPO_LICENZA = 'LA')"; // Considera solo le Liberazioni Anticipate
		// Considera le LA legate a ordinanze depositate
		// (quelle cioe' che hanno DATA_TRASMISSIONE valorizzata come DATA_DEPOSITO della tabella
		// DEPOSITO_ORDINANZA_PC)
		// lSql += " AND (EVENTO.DATA_TRASMISSIONE_ATTI IS NOT NULL)";
		lSql += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO in ('02','03')  )";
		// Non considera legate ad Eventi annullati (tale flag viene settato lato SIUS)
		lSql += " AND (FLAG_ELABORATO != 'A' OR FLAG_ELABORATO IS NULL)";
		if (aFlagElaborato != null && "N".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL)";
		if (aFlagElaborato != null && "S".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'S')";
		if (aFlagElaborato != null && "E".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'E')";
		if (aFlagElaborato != null && "NE".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL OR FLAG_ELABORATO = 'E')";
		lSql += " " + setOrderLicenzaLib();

		setStatement(lSql);
	}

	/**
	 * Query per la ricerca delle LA Revocate e relativi periodo
	 *
	 * @param aFascID
	 * @param aFlagElaborato
	 */
	public void ricercaLicenzaLibanticipataRevocateDepositate(BigDecimal aFascID, String aFlagElaborato) {

		String lSql = getSqlEventoQuery();

		lSql += " AND LIC.FAS_SIE_ID_FASCICOLO_SIEP=" + aFascID;
		lSql += " AND (FLAG_CONCESSO = 'S')"; // in caso di revoca e' sempre 'S' scomputate
		lSql += " AND (COD_TIPO_LICENZA = 'LA')"; // Considera solo le Liberazioni Anticipate
		// Considera le LA legate a ordinanze depositate
		// (quelle cioe' che hanno DATA_TRASMISSIONE valorizzata come DATA_DEPOSITO della tabella
		// DEPOSITO_ORDINANZA_PC)
		// lSql += " AND (EVENTO.DATA_TRASMISSIONE_ATTI IS NOT NULL)";
		lSql += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO in ('02','03')  )";
		// Solo LA legate ad eventi di revoca
		// '2135','2136','2137' UDS
		// '0028','0620','0621' TDS
		lSql += " AND (EVENTO.COD_MOTIVO in ('2135','2136','2137', '0028','0620','0621')  )";
		// Non considera legate ad Eventi annullati (tale flag viene settato lato SIUS)
		lSql += " AND (FLAG_ELABORATO != 'A' OR FLAG_ELABORATO IS NULL)";
		if (aFlagElaborato != null && "N".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL)";
		if (aFlagElaborato != null && "S".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'S')";
		if (aFlagElaborato != null && "E".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'E')";
		if (aFlagElaborato != null && "NE".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL OR FLAG_ELABORATO = 'E')";
		lSql += " " + setOrderLicenzaLib();

		setStatement(lSql);
	}

	public void ricercaLicenzaScomputiConcessiDepositati(BigDecimal aFascID, String aFlagElaborato) {

		String lSql = getSqlEventoQuery();

		/*
		 * OCCORRE VERIFICARE CHE siano giuste tutte le condizioni messe in and, in particolare
		 * FLAG_CONCESSO='C' e EVENTO.DATA_TRASMISSIONE_ATTI IS NOT NULL, attualmente lo commento...
		 */
		lSql += " AND LIC.FAS_SIE_ID_FASCICOLO_SIEP=" + aFascID;
		// lSql += " AND (FLAG_CONCESSO = 'C')";
		lSql += " AND ((COD_TIPO_LICENZA = 'PP') or (COD_TIPO_LICENZA = 'EP') )"; // Considera solo Scomputi
		// Considera le LA legate a ordinanze depositate
		// (quelle cioe' che hanno DATA_TRASMISSIONE valorizzata come DATA_DEPOSITO della tabella
		// DEPOSITO_ORDINANZA_PC)
		// lSql += " AND (EVENTO.DATA_TRASMISSIONE_ATTI IS NOT NULL)";
		lSql += " AND (EVENTO.COD_TIPO_PROVVEDIMENTO in ('02','03')  )";
		// Non considera legate ad Eventi annullati (tale flag viene settato lato SIUS)
		lSql += " AND (FLAG_ELABORATO != 'A' OR FLAG_ELABORATO IS NULL)";
		if (aFlagElaborato != null && "N".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL)";
		if (aFlagElaborato != null && "S".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'S')";
		if (aFlagElaborato != null && "E".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'E')";
		if (aFlagElaborato != null && "NE".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL OR FLAG_ELABORATO = 'E')";
		lSql += " " + setOrderLicenzaLib();

		setStatement(lSql);
	}

	public void ricercaLicenzaLibanticipataByKey(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	/**
	 * Recuper TUTTE le licenze associate all'evento passato in input indipendentemente dalla stato e dal tipo
	 *
	 * @param aIdEvento
	 * @throws DAOException
	 */
	public void ricercaLicenzaLibanticipataByEve(BigDecimal aIdEvento) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " AND EVE_ID_EVENTO = " + aIdEvento;
		setStatement(lSql);
	}

	// 20/05/2014 Nuova L.A. DL 146/2013

	/**
	 * Recuper TUTTE le licenze di un determinato Tipo L.A. (L.A., L.A. SPECIALE, o INTEGRAZIONE L.A.)
	 * associate all'evento passato in input indipendentemente dalla stato e dal tipo
	 *
	 * @param aIdEvento
	 *            , aTipoLA
	 * @throws DAOException
	 */
	public void ricercaLicenzaLibanticipataByEveTipoLA(BigDecimal aIdEvento, String aTipoLA)
			throws DAOException {

		String lSql = getSqlQuery();
		lSql += "  AND EVE_ID_EVENTO = " + aIdEvento;
		if (aTipoLA.equals("LA"))
			lSql += " AND (DESCR_STATO_PERMESSO = 'LA' OR DESCR_STATO_PERMESSO = 'LAU') ";
		if (aTipoLA.equals("LS"))
			lSql += " AND (DESCR_STATO_PERMESSO = 'LS' OR DESCR_STATO_PERMESSO = 'LSU') ";
		if (aTipoLA.equals("LI"))
			lSql += " AND (DESCR_STATO_PERMESSO = 'LI' OR DESCR_STATO_PERMESSO = 'LIU') ";

		setStatement(lSql);
	}
	// End DL 146/2013

	/**
	 * Recuper TUTTE le licenze associate all'evento passato in input indipendentemente dalla stato e dal
	 * tipo, Ordinate per FLAG_CONCESSO Crescente.
	 *
	 * @param aIdEvento
	 * @throws DAOException
	 */
	public void ricercaLicenzaLibanticipataByEveOrderByFlagConc(BigDecimal aIdEvento) throws DAOException {

		String lSql = getSqlQuery();
		lSql += "  AND EVE_ID_EVENTO = " + aIdEvento;
		lSql += " " + setOrderByConcesso();

		setStatement(lSql);
	}

	public void ricercaLicenzaLibanticipataConcessaByEve(BigDecimal aIdEvento) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " AND EVE_ID_EVENTO = " + aIdEvento;
		lSql += " AND (FLAG_CONCESSO = 'C' or FLAG_CONCESSO='S')";
		lSql += " AND (COD_TIPO_LICENZA = 'LA')"; // Considera solo le Liberazioni Anticipate

		setStatement(lSql);
	}

	public void ricercaRimediRisarcitoriConcessiByEve(BigDecimal aIdEvento) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " AND EVE_ID_EVENTO = " + aIdEvento;
		lSql += " AND FLAG_CONCESSO = 'C' ";
		lSql += " AND COD_TIPO_LICENZA = 'RD' "; // Considera solo i giorni di Riduzione

		setStatement(lSql);
	}

	public void ricercaRimediRisarcitoriESommeConcessiByEve(BigDecimal aIdEvento) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " AND EVE_ID_EVENTO = " + aIdEvento;
		lSql += " AND FLAG_CONCESSO = 'C' ";
		lSql += " AND COD_TIPO_LICENZA in ('RD','SL') "; // Considera sia i giorni che le somma

		setStatement(lSql);
	}

	public void ricercaLicenzaLibanticipataByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws DAOException {

		String lSql = getSqlQuery();
		lSql += "  AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;

		setStatement(lSql);
	}

	// 10-09-2014 Siep_MA_Affi_DS - Calcolo i gg di L.A. del FASCICOLO - Serve per la stampa dei provvedimenti
	// in cui devono comparire i gg di L.A. dell'intero fascicolo
	// (Es. Stampa Comunicazione Aff in Prova ai Serv Sociali e nel fascicolo e' presente una concessione di
	// L.A. PRECEDENTE)
	// NON AUTORIZZATO - non deve andare in produzione
	public void ricercaLicenzaLibanticipataByIdFascicoloSiepTutti(BigDecimal aIdFascicoloSiep)
			throws DAOException {

		String lSql = getSqlQuery();
		lSql += "  AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;
		lSql += " AND (FLAG_ELABORATO != 'A' OR FLAG_ELABORATO IS NULL)";

		setStatement(lSql);
	}

	/**
	 * Ricerca tutte le LA computabili in un calcolo pena, vale a dire associate a un evento SIEP validato,
	 * eventualmente nel periodo specificato in input. Vengono prese in considerazione: - LA concesse con
	 * ordinanza collegate a provvedimenti esecutivi (comunicazione/Ordine di scarcerazione) - LA revocate RES
	 * con ordinanza collegate a provvedimenti esecutivi - LA concesse in Cumulo - LA registrate con la Pena
	 * Manuale
	 *
	 * @param aFascID
	 * @param aDataDal
	 * @param aDataAl
	 */
	public void ricercaLAComputabiliByIdFascicoloDataValidazione(BigDecimal aFascID, Date aDataDal,
			Date aDataAl) {

		String lStatement = new String("");

		lStatement += " SELECT LA.ID_LICENZA_LIBANTICIPATA, LA.COD_TIPO_LICENZA, " +
		// "TIPO_LICENZA.RV_MEANING DESCR_TIPO_LICENZA, "+
				"'' DESCR_TIPO_LICENZA, LA.NUMERO_GIORNI, LA.DATA_INIZIO, LA.ORA_INIZIO, "
				+ "LA.DATA_FINE, LA.ORA_FINE, LA.LUOGO_SVOLGIMENTO_PROVA, LA.DATA_DETENZ_RIF_DA, "
				+ "LA.DATA_DETENZ_RIF_A, LA.FLAG_INFRAZIONE_OBBLIGHI, LA.DATA_INFRAZIONE_OBBLIGHI, "
				// "DESCR_INFRAZIONE_OBBLIGHI, "+
				+ "null DESCR_INFRAZIONE_OBBLIGHI, LA.FLAG_SCOMPUTO, "
				+ "LA.COD_OPERATORE_INSERIMENTO, LA.DATA_INSERIMENTO, LA.COD_UFFICIO_INSERIMENTO, "
				+ "LA.COD_OPERATORE_AGGIORNAMENTO, LA.DATA_AGGIORNAMENTO, LA.COD_UFFICIO_AGGIORNAMENTO, "
				+ "LA.FAS_SIU_ID_FASCICOLO_SIUS, LA.EVE_ID_EVENTO, LA.FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "LA.FLAG_CONCESSO, LA.FLAG_ELABORATO, NVL(LA.FLAG_SCORTA,'N') FLAG_SCORTA, "
				+ "LA.COD_STATO_PERMESSO, LA.DESCR_STATO_PERMESSO, " +
				// "STATO_PERMESSO.RV_MEANING DESCR_STATO_PERMESSO2, "+
				"null DESCR_STATO_PERMESSO2, LA.NUMERO_ORE, LA.ANNO_SIUS, LA.NUMERO_SIUS, "
				+ "LA.ANNO_ORDINANZA, LA.NUMERO_ORDINANZA, LA.COD_UFFICIO_EMITTENTE, " +
				// "UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , "+
				"null DESC_UFF_EMITTENTE, LA.COD_LUOGO_EMITTENTE, " +
				// "LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE,"+
				"null DESCR_LUOGO_EMITTENTE, "
				// MEV10-s3: aggiunto campo in estrazione
				// 20/06/2008 aggiunta campi.
				+ "null COD_TIPO_UFFICIO_EMITTENTE, LA.DATA_EMISSIONE_ORDINANZA "
				+ ", LA.GIORNI_SCOMPUTATI, LA.NUMERO_ORE_NO_FRUITE, LA.NUMERO_GIORNI_NO_FRUITI, LA.COD_ESITO"
				+ ", LA.DATA_ANNOTAZIONE_ESITO, LA.ANNOTAZIONE, LA.NUMERO_GIORNI, "
				+ "LA.NUMERO_MESI, LA.SOMMA_RISARC_DANNI ";
		lStatement += " FROM LICENZA_LIBANTICIPATA LA, EVENTO ";
		// lStatement += " ,CG_REF_CODES TIPO_LICENZA, CG_REF_CODES STATO_PERMESSO ";
		// lStatement += " ,COMUNE LUOGO_EMITTENTE, UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI";
		lStatement += " WHERE ";
		lStatement += " LA.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascID;
		// considero solo le revoche res che vengono inserite come LA e non AM
		lStatement += " AND (LA.FLAG_CONCESSO = 'C'"
				+ " OR (la.flag_concesso = 'R' AND la.cod_operatore_inserimento LIKE '%res%') "
				+ " OR LA.FLAG_CONCESSO = 'S'  ) ";
		// lStatement += " AND (LA.COD_TIPO_LICENZA = 'LA')"; // Considera solo le Liberazioni Anticipate
		// Anna per scomputo permesso
		lStatement += " AND (LA.COD_TIPO_LICENZA = 'LA' "
				+ " OR LA.COD_TIPO_LICENZA = 'EP' OR LA.COD_TIPO_LICENZA = 'PP' "
				+ " OR LA.COD_TIPO_LICENZA = 'RD' " + // Rimedi Risarcitrori DL 92 d.f. 10/2014
				") ";
		lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		lStatement += " AND LA.EVE_ID_EVENTO = evento.ID_EVENTO ";

		String lFlagDocumentoRegistrato = "S";
		String lSql = getCondizioniLA(aFascID, aDataDal, aDataAl, lFlagDocumentoRegistrato);
		lStatement += lSql;
		// lStatement += " ORDER BY " ;
		setStatement(lStatement);
	}

	/**
	 *
	 * @param aFascID
	 * @param aDataDal
	 * @param aDataAl
	 * @param aFlagDocumentoRegistrato
	 * @return
	 */
	protected String getCondizioniLA(BigDecimal aFascID, Date aDataDal, Date aDataAl,
			String aFlagDocumentoRegistrato) {

		String lStatement = new String("");
		lStatement += " and LA.EVE_ID_EVENTO = EVENTO.ID_EVENTO ";
		// ==========================================================================
		// La seguente parte aggiunge due condizioni:
		// 1) la LA deve essere collegata a una ordinaza (01-03-2130) a sua
		// volta collegata a un provvedimento (Comunicazione/Ordine di scarcerazione)
		// validato, eventualmente nel periodo specificato in input
		// 2) la LA deve essere collegata a un evento di stampa Cumulo validato,
		// eventualmente nel periodo specificato in input
		// ==========================================================================
		lStatement += " and ";
		lStatement += " ( "; // 1a
		// Condizione sulle LA concesse con ORDINANZA TDS o UDS
		lStatement += " ("; // 11a
		lStatement += " ("; // 111a
		lStatement += " (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '03' "
				+ "and EVENTO.COD_MOTIVO = '2130') "; // UDS
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '03' "
				+ "and EVENTO.COD_MOTIVO = '0076') "; // TDS
		// Revoca RES
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '03' "
				+ "and EVENTO.COD_MOTIVO = '2135' and EVENTO.COD_OPERATORE_INSERIMENTO like '%res%') ";
		// Concessione su Reclamo (MEV A8RR083)
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '03' "
				+ "and EVENTO.COD_MOTIVO = '0113') ";
		// LA Speciale
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '03' "
				+ "and EVENTO.COD_MOTIVO = '2131') ";
		// Integrazione LA
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '03' "
				+ "and EVENTO.COD_MOTIVO = '2132') ";
		// Rimedi Risarcitori DL 02 Decreto e Ordinanza cod 2790 aggiunto il 10/2014
		// Reclamo Rimedi Risarcitori DL n. 92 aggiunto il 04/04/2015
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO in ('02','03') "
				+ "and EVENTO.COD_MOTIVO = '2790' or EVENTO.COD_MOTIVO = '9027') ";
		lStatement += " ) "; // 111b

		// Aggiungo la select che filtra solo le ordinanze collegate a provvedimenti
		// di comunicazione (01-12-0923) o ordine di scarcerazione (01-09-0081) emessi
		// in fase di acquisizione dell'ordinanza di concessione LA e validati,
		// eventualmente nel periodo specificato in input
		// Aggiunto l'OS res per le revoche LA (01-09-0028)
		lStatement += " and EVENTO.ID_EVENTO in ";
		lStatement += " ( " + // 112a
				"select EVENTO.EVE_ID_EVENTO ";
		lStatement += " from EVENTO ";
		lStatement += " where EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascID;
		if (aDataDal != null) {
			lStatement += " and EVENTO.DATA_INSERIMENTO >= to_date ('"
					+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}
		if (aDataAl != null) {
			lStatement += " and EVENTO.DATA_INSERIMENTO <= to_date ('"
					+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}
		if (aFlagDocumentoRegistrato.equals("S"))
			lStatement += " and EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		lStatement += " and (   "; // 1121a
		lStatement += " (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '12' "
				+ "and EVENTO.COD_MOTIVO = '0923') ";
		// comunicazione - Concessione Liberazione Anticipata - condannato Libero
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '12' "
				+ "and EVENTO.COD_MOTIVO = '0922') ";
		// comunicazione - Concessione Liberazione Anticipata - condannato in Ergastolo
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '09' "
				+ "and EVENTO.COD_MOTIVO = '0081') ";
		// ordine di scarcerazione - Nuova scadenza pena a seguito concessione Liberazione Anticipata -
		// condannato detenuto
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '09' "
				+ "and EVENTO.COD_MOTIVO = '0083') ";
		// ordine di scarcerazione - Nuova scadenza pena a seguito concessione Liberazione Anticipata -
		// condannato in misura alternativa
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '09' "
				+ "and EVENTO.COD_MOTIVO = '0028'  ";
		// ordine di scarcerazione - Revoca Liberazione Anticipata (RES)
		lStatement += " and EVENTO.COD_OPERATORE_INSERIMENTO like '%res%')";
		// Rimedi Risarcitori DL 02 Decreto e Ordinanza cod 2790 aggiunto il 10/2014
		// Ticket#20190805017 aggiungo i codici '9032','9033','9034' nella 'IN' seguente, relativi al Reclamo
		// Rimedi Risarcitori DL n. 92
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO in ('09','12') "
				+ "and EVENTO.COD_MOTIVO in ('5491','5492','5493','5494','5495','9032','9033','9034','9154','9254')) ";
		lStatement += " )"; // 1121b
		lStatement += " )"; // 112b
		lStatement += " )"; // 11b Fine condizione sulle LA concesse con ORDINANZA

		// ==========================================================================
		// Oppure LA direttamente legate a un provvedimento SIEP di:
		// - CUMULO
		// - RIdimensionamento
		// - Revoca
		// ==========================================================================
		// se entrambe le date sono null che succede??
		lStatement += "   or ( "; // 12a
		lStatement += "     		EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascID;
		if (aDataDal != null) {
			lStatement += " and EVENTO.DATA_INSERIMENTO >= to_date ('"
					+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}
		if (aDataAl != null) {
			lStatement += "   and EVENTO.DATA_INSERIMENTO <= to_date ('"
					+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}

		lStatement += " and  ( "; // 121a
		// Ridimensionamento LA
		lStatement += " (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '25' "
				+ "and EVENTO.COD_MOTIVO = '0995') ";
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '25' "
				+ "and EVENTO.COD_MOTIVO = '1008') "; // new
		// 07/2014 REVOCA LA
		// Rideterminazione della pena a seguito di scomputo di permesso
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '25' "
				+ "and EVENTO.COD_MOTIVO = '0958') ";
		// Rideterminazione fine pena a seguito accoglimento reclamo scomputo permesso
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '25' "
				+ "and EVENTO.COD_MOTIVO = '0994') ";
		// ==========================================================================
		// Aggiungo la select che filtra solo le LA collegate a eventi di
		// STAMPA CUMULO che validano anche le LA. n.b. nel caso di cumulo non
		// ho un evento intermedio (ordinanza), ma la LA e' direttamente collegata
		// al provvedimento di Cumulo
		// ==========================================================================
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '04' "
				+ "and EVENTO.COD_MOTIVO in('0222','0223','0224','0277')) ";
		// MEV26 Nuovo Cumulo
		// lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '04' and
		// (TO_NUMBER(EVENTO.COD_MOTIVO) BETWEEN 630 and 651) ) ";
		// Fiorletta 23/12/2019 GESTIONE TUTTI I NUOVI CODICI DEL CUMULO
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '04'"
				+ " and EVENTO.COD_MOTIVO IN (SELECT RV_LOW_VALUE  FROM CG_REF_CODES "
				+ " WHERE RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'  AND RV_HIGH_VALUE= 'CUMULO_NEW')) ";
		// ==========================================================================
		// Aggiungo eventuale OR per le LA inserite con la pena residua manuale
		// ==========================================================================
		lStatement += " or (EVENTO.COD_TIPO_EVENTO='01' and EVENTO.COD_TIPO_PROVVEDIMENTO = '04'"
				+ " and EVENTO.COD_MOTIVO = '0925') ";
		lStatement += " )"; // 121b
		lStatement += " )"; // fine OR 12b
		lStatement += " )"; // 1b
		// lStatement += " ORDER BY " ;
		return lStatement;
	}

	protected String getSqlQuery() {

		String lStatement = new String("");
		lStatement += "SELECT ID_LICENZA_LIBANTICIPATA, COD_TIPO_LICENZA, "
				+ "TIPO_LICENZA.RV_MEANING DESCR_TIPO_LICENZA, NUMERO_GIORNI, DATA_INIZIO, "
				+ "ORA_INIZIO, DATA_FINE, ORA_FINE, LUOGO_SVOLGIMENTO_PROVA, "
				+ "DATA_DETENZ_RIF_DA, DATA_DETENZ_RIF_A, FLAG_INFRAZIONE_OBBLIGHI, "
				+ "DATA_INFRAZIONE_OBBLIGHI, DESCR_INFRAZIONE_OBBLIGHI, FLAG_SCOMPUTO, "
				+ "COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS, EVE_ID_EVENTO, FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "FLAG_CONCESSO, FLAG_ELABORATO, NVL(FLAG_SCORTA,'N') FLAG_SCORTA, "
				+ "COD_STATO_PERMESSO, DESCR_STATO_PERMESSO, "
				+ "STATO_PERMESSO.RV_MEANING DESCR_STATO_PERMESSO2, NUMERO_ORE, ANNO_SIUS, "
				+ "NUMERO_SIUS, ANNO_ORDINANZA, NUMERO_ORDINANZA, "
				+ "COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, " +
				// MEV10-s3: aggiunto campo in estrazione
				"UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFFICIO_EMITTENTE, DATA_EMISSIONE_ORDINANZA " +
				// 20/06/2008 aggiunta campi.
				", GIORNI_SCOMPUTATI, NUMERO_ORE_NO_FRUITE, NUMERO_GIORNI_NO_FRUITI, COD_ESITO, "
				+ "DATA_ANNOTAZIONE_ESITO, ANNOTAZIONE, NUMERO_MESI, SOMMA_RISARC_DANNI"
				// MEV_2025-48: aggiunta nuova sezione - codice motivo detenzione
				+ ", COD_MOTIVO_DETENZIONE, MOTIVO_DETENZIONE.RV_MEANING DESCR_MOTIVO_DETENZIONE";
		lStatement += " FROM LICENZA_LIBANTICIPATA, CG_REF_CODES TIPO_LICENZA, CG_REF_CODES STATO_PERMESSO,";
		lStatement += " COMUNE LUOGO_EMITTENTE, UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI"
				+ ", CG_REF_CODES MOTIVO_DETENZIONE";
		lStatement += " WHERE (TIPO_LICENZA.RV_DOMAIN = 'TIPO_LICENZA' AND "
				+ "NVL(COD_TIPO_LICENZA,'01') = TIPO_LICENZA.RV_LOW_VALUE) ";
		lStatement += " AND (STATO_PERMESSO.RV_DOMAIN = 'STATO_PERMESSO' AND "
				+ "NVL(COD_STATO_PERMESSO,'99') = STATO_PERMESSO.RV_LOW_VALUE) ";
		lStatement += " AND (NVL(COD_LUOGO_EMITTENTE, '-') = LUOGO_EMITTENTE.COD_COMUNE )";
		lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND "
				+ "UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO'";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND MOTIVO_DETENZIONE.RV_DOMAIN(+) = 'MOTIVO_DETENZIONE'"
				+ " AND MOTIVO_DETENZIONE.RV_LOW_VALUE(+) = COD_MOTIVO_DETENZIONE";

		return lStatement;
	}

	protected String getSqlEventoQuery() {

		String lStatement = new String("");
		lStatement += " SELECT ID_LICENZA_LIBANTICIPATA, COD_TIPO_LICENZA, "
				+ "TIPO_LICENZA.RV_MEANING DESCR_TIPO_LICENZA, NUMERO_GIORNI, DATA_INIZIO, "
				+ "ORA_INIZIO, DATA_FINE, ORA_FINE, LUOGO_SVOLGIMENTO_PROVA, "
				+ "DATA_DETENZ_RIF_DA, DATA_DETENZ_RIF_A, FLAG_INFRAZIONE_OBBLIGHI, "
				+ "DATA_INFRAZIONE_OBBLIGHI, DESCR_INFRAZIONE_OBBLIGHI, FLAG_SCOMPUTO, "
				+ "LIC.COD_OPERATORE_INSERIMENTO, LIC.DATA_INSERIMENTO, "
				+ "LIC.COD_UFFICIO_INSERIMENTO, LIC.COD_OPERATORE_AGGIORNAMENTO, "
				+ "LIC.DATA_AGGIORNAMENTO, LIC.COD_UFFICIO_AGGIORNAMENTO, "
				+ "LIC.FAS_SIU_ID_FASCICOLO_SIUS, LIC.EVE_ID_EVENTO, "
				+ "LIC.FAS_SIE_ID_FASCICOLO_SIEP, LIC.SOMMA_RISARC_DANNI, FLAG_CONCESSO, "
				+ "FLAG_ELABORATO, NVL(FLAG_SCORTA,'N') FLAG_SCORTA, COD_STATO_PERMESSO, "
				+ "DESCR_STATO_PERMESSO, STATO_PERMESSO.RV_MEANING DESCR_STATO_PERMESSO2, "
				+ "NUMERO_ORE, ANNO_SIUS, NUMERO_SIUS, ANNO_ORDINANZA, NUMERO_ORDINANZA, "
				+ "LIC.COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , "
				+ "LIC.COD_LUOGO_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, "
				// MEV10-s3: aggiunto campo in estrazione
				+ "UFF_TIPO_EMI.RV_LOW_VALUE COD_TIPO_UFFICIO_EMITTENTE, DATA_EMISSIONE_ORDINANZA"
				// 20/06/2008 aggiunta campi.
				+ ", GIORNI_SCOMPUTATI, NUMERO_ORE_NO_FRUITE, NUMERO_GIORNI_NO_FRUITI, LIC.COD_ESITO, "
				+ "LIC.DATA_ANNOTAZIONE_ESITO, LIC.ANNOTAZIONE, NUMERO_MESI"
				// MEV_2025-48: aggiunta nuova sezione - codice motivo detenzione
				+ ", LIC.COD_MOTIVO_DETENZIONE, MOTIVO_DETENZIONE.RV_MEANING DESCR_MOTIVO_DETENZIONE";
		lStatement += " FROM LICENZA_LIBANTICIPATA LIC, EVENTO, CG_REF_CODES TIPO_LICENZA,"
				+ " CG_REF_CODES STATO_PERMESSO, CG_REF_CODES MOTIVO_DETENZIONE,";
		lStatement += " COMUNE LUOGO_EMITTENTE, UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI";
		lStatement += " WHERE (TIPO_LICENZA.RV_DOMAIN  = 'TIPO_LICENZA' AND"
				+ " NVL(COD_TIPO_LICENZA,'01') = TIPO_LICENZA.RV_LOW_VALUE) ";
		lStatement += " AND (STATO_PERMESSO.RV_DOMAIN  = 'STATO_PERMESSO' AND"
				+ " NVL(COD_STATO_PERMESSO,'99') = STATO_PERMESSO.RV_LOW_VALUE) ";
		lStatement += " AND (NVL(LIC.COD_LUOGO_EMITTENTE, '-') = LUOGO_EMITTENTE.COD_COMUNE )";
		lStatement += " AND UFF_EMI.COD_UFFICIO = LIC.COD_UFFICIO_EMITTENTE AND"
				+ " UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO'";
		lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
		lStatement += " AND EVENTO.ID_EVENTO = LIC.EVE_ID_EVENTO";
		lStatement += " AND MOTIVO_DETENZIONE.RV_DOMAIN(+) = 'MOTIVO_DETENZIONE'"
				+ " AND MOTIVO_DETENZIONE.RV_LOW_VALUE(+) = COD_MOTIVO_DETENZIONE";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {

		LicenzaLibAnticipataModel aModel = new LicenzaLibAnticipataModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdLicenzaLibanticipata(getBigDecimal("ID_LICENZA_LIBANTICIPATA"));
		aModel.setCodTipoLicenza(getString("COD_TIPO_LICENZA"));
		aModel.setDescrTipoLicenza(getString("DESCR_TIPO_LICENZA"));
		aModel.setNumeroGiorni(getBigDecimal("NUMERO_GIORNI"));
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setOraInizio(getString("ORA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setOraFine(getString("ORA_FINE"));
		aModel.setLuogoSvolgimentoProva(getString("LUOGO_SVOLGIMENTO_PROVA"));
		aModel.setDataDetenzRifDa(getDate("DATA_DETENZ_RIF_DA"));
		aModel.setDataDetenzRifA(getDate("DATA_DETENZ_RIF_A"));
		aModel.setFlagInfrazioneObblighi(getString("FLAG_INFRAZIONE_OBBLIGHI"));
		aModel.setDataInfrazioneObblighi(getDate("DATA_INFRAZIONE_OBBLIGHI"));
		aModel.setDescrInfrazioneObblighi(getString("DESCR_INFRAZIONE_OBBLIGHI"));
		aModel.setFlagScomputo(getString("FLAG_SCOMPUTO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDescrUfficioInserimento("");
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setDescrUfficioAggiornamento("");
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFlagConcesso(getString("FLAG_CONCESSO"));
		aModel.setFlagElaborato(getString("FLAG_ELABORATO"));
		aModel.setFlagScorta(getString("FLAG_SCORTA"));
		aModel.setCodStatoPermesso(getString("COD_STATO_PERMESSO"));
		if (aModel.getCodStatoPermesso() == null || aModel.getCodStatoPermesso().equals("99"))
			aModel.setDescrStatoPermesso(getString("DESCR_STATO_PERMESSO"));
		else
			aModel.setDescrStatoPermesso(getString("DESCR_STATO_PERMESSO2"));
		aModel.setNumeroOre(getBigDecimal("NUMERO_ORE"));
		aModel.setAnnoSius(getBigDecimal("ANNO_SIUS"));
		aModel.setNumeroSius(getString("NUMERO_SIUS"));
		aModel.setAnnoOrdinanza(getBigDecimal("ANNO_ORDINANZA"));
		aModel.setNumeroOrdinanza(getBigDecimal("NUMERO_ORDINANZA"));
		aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		aModel.setDescrUfficioEmittente(getString("DESC_UFF_EMITTENTE"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		// MEV10-s3: aggiunto recupero di proprieta'
		aModel.setCodTipoUfficioEmittente(getString("COD_TIPO_UFFICIO_EMITTENTE"));
		aModel.setDataEmissioneOrdinanza(getDate("DATA_EMISSIONE_ORDINANZA"));
		// 20/06/2008 Aggiunta campi
		if (this.getString("GIORNI_SCOMPUTATI") != null)
			aModel.setGiorniScomputati(getString("GIORNI_SCOMPUTATI"));
		if (this.getString("COD_ESITO") != null)
			aModel.setCodEsito(getString("COD_ESITO"));
		if (this.getDate("DATA_ANNOTAZIONE_ESITO") != null)
			aModel.setDataAnnotazioneEsito(getDate("DATA_ANNOTAZIONE_ESITO"));
		if (this.getString("ANNOTAZIONE") != null)
			aModel.setAnnotazione(getString("ANNOTAZIONE"));
		if (this.getBigDecimal("NUMERO_GIORNI_NO_FRUITI") != null)
			aModel.setNumeroGiorniNoFruiti(getBigDecimal("NUMERO_GIORNI_NO_FRUITI"));
		if (this.getBigDecimal("NUMERO_ORE_NO_FRUITE") != null)
			aModel.setNumeroOreNoFruite(getBigDecimal("NUMERO_ORE_NO_FRUITE"));
		aModel.setNumeroMesi(getBigDecimal("NUMERO_MESI"));
		aModel.setSommaRisarcDanni(getBigDecimal("SOMMA_RISARC_DANNI"));
		// MEV_2025-48: aggiunta nuova sezione - codice motivo detenzione
		if (findColumn("COD_MOTIVO_DETENZIONE"))
			aModel.setCodMotivoDetenzione(getString("COD_MOTIVO_DETENZIONE"));
		if (findColumn("DESCR_MOTIVO_DETENZIONE"))
			aModel.setDescrMotivoDetenzione(getString("DESCR_MOTIVO_DETENZIONE"));

		return aModel;
	}

	public String setCondizione(LicenzaLibAnticipataModel aModel) {

		String lCondizioni = new String();

		return lCondizioni;
	}

	private String setOrderLicenzaLib() {

		String lCondizioni = " ORDER BY  DATA_INSERIMENTO DESC, ID_LICENZA_LIBANTICIPATA DESC";
		return lCondizioni;
	}

	private String setOrderByConcesso() {

		String lCondizioni = " ORDER BY  FLAG_CONCESSO ASC";
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {

		return " AND ID_LICENZA_LIBANTICIPATA = " + aKey;
	}

	/**
	 * Aggiorna tutte le LA associate al Fascicolo con FLAG_ELABORATO = aFlagElaboratoVecchio modifcando il
	 * flag in aFlagElaboratoNuovo
	 *
	 * @param aKeyFascicolo
	 * @param aFlagElaboratoVecchio
	 * @param aFlagElaboratoNuovo
	 * @throws DAOException
	 */
	public void updateFlagElaboratoByIdFascicolo(BigDecimal aKeyFascicolo, String aFlagElaboratoVecchio,
			String aFlagElaboratoNuovo) throws DAOException {

		LicenzaLibanticipataDAO lLicDao = new LicenzaLibanticipataDAO(this.mCon);

		ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP(aKeyFascicolo, aFlagElaboratoVecchio, "DL92");

		List lListLib = new ArrayList(getModels());

		LicenzaLibAnticipataModel lLibMod = null;
		for (int i = 0; i < lListLib.size(); i++) {
			lLibMod = (LicenzaLibAnticipataModel) lListLib.get(i);

			lLicDao.setFlagElaborato(aFlagElaboratoNuovo);
			lLicDao.setCondizioneUpdate(lLibMod.getIdLicenzaLibanticipata());
			lLicDao.update();
			lLicDao.stop();
		}
	}

	public void updateFlagElaboratoByEveIdEvento(BigDecimal aEveIdEvento, String aFlagElaboratoNuovo,
			DatiOperazioneModel aDatiOperazione) throws DAOException {
		LicenzaLibanticipataDAO lLicDao = new LicenzaLibanticipataDAO(this.mCon);

		ricercaLicenzaLibanticipataByEve(aEveIdEvento);

		List lListLib = new ArrayList(getModels());

		LicenzaLibAnticipataModel lLibMod = null;
		for (int i = 0; i < lListLib.size(); i++) {
			lLibMod = (LicenzaLibAnticipataModel) lListLib.get(i);

			lLicDao.setFlagElaborato(aFlagElaboratoNuovo);
			// La data di inserimento viene cambiata perche' se
			// successiva ad una forzatura non verrebbe conteggiata (quando validata)
			// ad esempio nel Dettaglio del Fascicolo,
			// in questo modo (se elaborata e validata dopo) compare anche nel Dettaglio Fascicolo
			lLicDao.setDataInserimento(aDatiOperazione.getData());

			lLicDao.setCondizioneUpdate(lLibMod.getIdLicenzaLibanticipata());
			lLicDao.update();
			lLicDao.stop();
		}
	}

	/**
	 * Imposta le condition per la query di estrazione della LA di concessione dei rimedi risarcitori DL 92
	 * ovvero i record con IPO_LICENZA RD o SL
	 *
	 * @param aFascID
	 *            - Id del fascicolo SIEP a cui sono legate le LA
	 * @param aFlagElaborato
	 *            : null,N,S,E,NE ??
	 */
	public void ricercaRimediRisarcitoriConcessaDepositata(BigDecimal aFascID, String aFlagElaborato) {

		String lSql = getSqlEventoQuery();
		lSql += " AND LIC.FAS_SIE_ID_FASCICOLO_SIEP=" + aFascID;
		lSql += " AND FLAG_CONCESSO = 'C' ";
		lSql += " AND COD_TIPO_LICENZA in ('RD','SL')"; // RD = RIduzione, SL = Somma Liquidata
		// Considera le LA legate a ordinanze/decreti depositati
		// (quelle cioe' che hanno DATA_TRASMISSIONE valorizzata come DATA_DEPOSITO della tabella
		// DEPOSITO_ORDINANZA_PC o DEPOSITO_DECRETO)
		lSql += " AND EVENTO.DATA_TRASMISSIONE_ATTI IS NOT NULL ";
		lSql += " AND EVENTO.COD_TIPO_PROVVEDIMENTO in ('02','03')  ";
		// Non considera legate ad Eventi annullati (tale flag viene settato lato SIUS)
		lSql += " AND (FLAG_ELABORATO != 'A' OR FLAG_ELABORATO IS NULL)";
		if ("N".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL)";
		else if ("S".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'S')";
		else if ("E".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'E')";
		else if ("NE".equals(aFlagElaborato))
			lSql += " AND (FLAG_ELABORATO = 'N' OR FLAG_ELABORATO IS NULL OR FLAG_ELABORATO = 'E')";
		lSql += " ORDER BY DATA_INSERIMENTO DESC, ID_LICENZA_LIBANTICIPATA DESC";

		setStatement(lSql);
	}

}