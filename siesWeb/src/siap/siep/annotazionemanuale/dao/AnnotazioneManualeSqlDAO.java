package siap.siep.annotazionemanuale.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.dao.SIAPSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;

/**
 * Title: AnnotazioneManualeSqlDAO Description: Classe SqlDAO che rappresenta la tabella AnnotazioneManuale
 *
 * @version 1.0
 */
public class AnnotazioneManualeSqlDAO extends SIAPSqlDAO {

	public AnnotazioneManualeSqlDAO(Connection con) {
		super(con);
	}

	/**
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaAnnotazioneManuale(AnnotazioneManualeModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);

		setStatement(lSql);
	}

	public void ricercaAnnotazioneManualeByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	/**
	 * Ricerca TUTTE le annotazioni manuali legate al fascicolo indipendentemente dallo stato di validazione
	 *
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaAnnotazioneManualeByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aKey;

		setStatement(lSql);
	}

	/**
	 * Ricerca TUTTE le annotazioni manuali legate al fascicolo indipendentemente dallo stato di validazione e
	 * ordinate per EVE_ID_EVENTO
	 *
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaAnnotazioneManualeByIdFascicoloPerStatoEsecuzione(BigDecimal aKey)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aKey + " ORDER BY EVE_ID_EVENTO";

		setStatement(lSql);
	}

	/**
	 * Ricerca tutte le Annotazioni Manuali per un certo fascicolo. Se viene specificato un intervallo di date
	 * vengono recuperate solo quelle appartenenti all'intervallo. E' possibile specificare anche un solo
	 * estremo dell'intervallo.
	 *
	 * @param aKey
	 * @param aDataDal
	 * @param aDataAl
	 * @throws DAOException
	 */
	public void ricercaAnnotazioneManualeByIdFascicoloDateIns(BigDecimal aKey, Date aDataDal, Date aDataAl)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aKey;

		if (aDataDal != null) {
			lSql += " AND DATA_INSERIMENTO > to_date ('"
					+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}

		if (aDataAl != null) {
			lSql += " AND DATA_INSERIMENTO < to_date ('"
					+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}

		setStatement(lSql);
	}

	public void ricercaAnnotazioneManualeByIdsAnnMan(String ids) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND ID_ANNOTAZIONE_MANUALE in (" + ids + ")";

		setStatement(lSql);
	}

	/**
	 * Ricerca tutte le Annotazioni Manuali per un certo fascicolo legate a un Computo e validate nel periodo
	 * passato in input (se specificato) - Presofferto Altro Reato (005-Pena Espiata per lo Stesso Titolo) -
	 * Fungibilità altro Reato - Misura Cautelare (006-Pena Espiata per Altro Titolo) - Pena Detentiva
	 * (007-Pena Espiata Senza Titolo) - Altro (014-Altro)
	 *
	 * - Computi iscritti RES (evento '0162' di rideterminazione pena con cod tipo annotazione ='-'))
	 *
	 * E' possibile specificare anche un solo estremo dell'intervallo.
	 *
	 * @param aKey
	 * @param aDataDal
	 * @param aDataAl
	 * @throws DAOException
	 */
	public void ricercaAnnManualeComputiByIdFascicoloDataValidazione(BigDecimal aKey, Date aDataDal,
			Date aDataAl) throws DAOException {
		String lStatement = "";

		// La select va in join con la tabella evento per recuperare solo le
		// annotazioni associate ad eventi validati (eventualmente non periodo
		// specificato in input)
		lStatement += " SELECT " + " AM.ID_ANNOTAZIONE_MANUALE, " + " AM.ANNO_ID_ANNOTAZIONE_MANUALE, "
				+ " AM.COD_TIPO_ANNOTAZIONE,null DESCR_TIPO, " + " AM.FLAG_PIU_MENO, "
				+ " AM.NUM_ANNI_RECLUSIONE, AM.NUM_MESI_RECLUSIONE, AM.NUM_GIORNI_RECLUSIONE, AM.IMPORTO_MULTA, "
				+ " AM.NUM_ANNI_ARRESTO, AM.NUM_MESI_ARRESTO, AM.NUM_GIORNI_ARRESTO, AM.IMPORTO_AMMENDA, "
				+ " AM.DATA_ARRESTO_DA, AM.DATA_ARRESTO_A, "
				+ " AM.DATA_RECLUSIONE_DA, AM.DATA_RECLUSIONE_A, "
				+ " AM.DATA_RICEZIONE_DOC, AM.MOTIVAZIONI, AM.NOTE_RECLUSIONE, "
				+ " AM.ANNO_GE, AM.NUMERO_GE, AM.ANNO_REGE, AM.NUMERO_REGE, AM.ANNO_MC, "
				+ " AM.NUMERO_MC, AM.ANNO_CDA, AM.NUMERO_CDA, AM.ANNO_CC, AM.NUMERO_CC, "
				+ " AM.ANNO_SIEP, AM.NUMERO_SIEP, " + " AM.COD_TIPO_UFFICIO_SIEP, "
				+ " AM.COD_LUOGO_UFFICIO_SIEP, " + " AM.DATA_ISCRIZIONE_SIEP, "
				+ " AM.COD_FONTE, null DESCR_FONTE, AM.ANNO_FONTE, AM.NUMERO_FONTE, "
				+ " AM.COD_SOTTONUMERAZIONE, null DESCR_SOTTONUM,"
				+ " AM.COMMA, AM.LETTERA, AM.NUMERO, AM.ARTICOLO, "
				+ " AM.COD_CAUSALE_COMPUTO, null DESCR_CAUCOMP," + " AM.COD_DPR, null DESCR_DPR,"
				+ " AM.COD_OPERATORE_INSERIMENTO, AM.DATA_INSERIMENTO, AM.COD_UFFICIO_INSERIMENTO, "
				+ " AM.COD_OPERATORE_AGGIORNAMENTO, AM.DATA_AGGIORNAMENTO, AM.COD_UFFICIO_AGGIORNAMENTO, "
				+ " AM.FAS_SIE_ID_FASCICOLO_SIEP, " + " AM.REA_ID_REATO, " + " AM.EVE_ID_EVENTO, "
				+ " AM.FLAG_VALIDATO, " + " AM.FLAG_CONFORME, " + " AM.FLAG_APP_PROVVISORIA, "
				+ " AM.PEN_RES_ID_PENA_RESIDUA, " + " AM.FUN_ID_FUNGIBILITA, " + " AM.DATA_RICHIESTA, "
				+ " AM.DATA_CC, " + " AM.DATA_GE, "
				+ " AM.ANNO_SENTENZA_SIAP, AM.NUMERO_SENTENZA_SIAP, AM.DATA_SENTENZA_SIAP, " +
				// 07_2015 MEV29 punto 11 - Anno e Numero procedimento SIGE
				" AM.ANNO_SIGE, AM.NUMERO_SIGE, " + " AM.FLAG_COMPUTABILE, " + " AM.SEN_ID_SENTENZA, "
				+ " AM.TEN_ID_TENORE_SIGE, " + " FLAG_SEL_QUANTUM, " + " AM.FLG_BENEFICIO_DETRATTO ";
		lStatement += " FROM ANNOTAZIONE_MANUALE AM, EVENTO ";
		lStatement += " WHERE AM.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += "   AND AM.EVE_ID_EVENTO = evento.ID_EVENTO ";
		lStatement += "   AND AM.COD_TIPO_ANNOTAZIONE in ('005','006','007','014','-','015') "; // n.b. '-'
																								// sono le
																								// ridet RES
		lStatement += "   AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		lStatement += "   AND AM.FLAG_VALIDATO <> 'A' ";
		// Attenzione! Verificare se è corretto mettere il filtro sull'evento che
		// limita la generalità della select è comporta il suo aggiornamento in caso
		// di modifica/aggiunta dei codici evento
		// Condizioni sul tipo di evento
		lStatement += "   AND ( ";
		// Computi Presofferto/fungibilita(MC/PD)
		lStatement += "           (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO = '04' AND evento.COD_MOTIVO in ('0121','0212','0213'))  ";
		// Computo Altro (RV_HIGH_VALUE = RIDPE)
		// attenzione alcuni computi 'Altro' sono legati a comunicazioni, altri a Provvedimenti
		lStatement += "        OR ";
		lStatement += "           (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO = '04' AND evento.COD_MOTIVO in ('0917','0918','0919')) ";
		lStatement += "        OR ";
		// n.b. il tipo provv 04 è stato aggiunto il 27/04/2007 sui motivi in ('0913','0914','0915','0916')
		// perchè ci si è accorti che
		// su GENOVA fino a gennaio 2006 hanno utilizzato 04 e non 12
		// Paolo Cherubini 04/5/2011 aggiungo computo di annotazione da richiesta conversione pena pecuniaria
		// lStatement +=
		// " (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO in ('12','04') AND
		// evento.COD_MOTIVO in ('0913','0914','0915','0916')) ";
		lStatement += "           (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO in ('12','04') AND evento.COD_MOTIVO in ('0913','0914','0915','0916','0942')) ";

		// Computo Altro Nuovo v4.0 (RV_HIGH_VALUE = RIDPE_UFF o RIDPE_AUFF)
		lStatement += "        OR "; // AMBROSINO 04/2011 - Annota avvenuto pagamento Pena pecuniaria - Tipo
										// Eve = 01 ; Topo Provvedimento = 25 ; Cod Motivo Provvedimento =
										// 1007 (nuovo)
		lStatement += "           (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO = '25' "
				+ "AND evento.COD_MOTIVO in ('0948','0949','0950','0951','0952','0953','0954','0955','0956','0957','0958','0959','1007',"
				+ "'0999','1000','1003','1004','1005','1006', " // aggiunti da Paolo Cherubini 27/09/2011
				+ "'0987', '0988'))  "; // aggiunti codici 0987 e 0988 per ticket 20191210013

		// MEV27 07/2015 computi derivanti da conversione sanzione sostitutiva
		lStatement += "        OR ";
		lStatement += "           (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO = '25' AND evento.COD_MOTIVO in ('1016','1015')) ";

		// Nuova condizione per computi RES di rideterminazione pena '0162' non gestiti SIES
		lStatement += "        OR ";
		lStatement += "           (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO = '04' AND evento.COD_MOTIVO = '0162') ";
		// Nuova condizione su annotazioni di Revoca Sanzioni Sostitutive
		lStatement += "        OR ";
		lStatement += "           (evento.COD_TIPO_EVENTO='01' AND evento.COD_TIPO_PROVVEDIMENTO = '04' AND evento.COD_MOTIVO = '0262') ";
		lStatement += "       )  ";
		// Aggiungo le condizioni sull'intervallo di validazione
		if (aDataDal != null) {
			lStatement += " AND evento.DATA_INSERIMENTO > to_date ('"
					+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}
		if (aDataAl != null) {
			// n.b. <= serve per recuperare anche l'annotazione legata all'evento corrente
			// se l'evento corrente è appunto un computo
			lStatement += " AND evento.DATA_INSERIMENTO <= to_date ('"
					+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}

		setStatement(lStatement);
	}

	/**
	 * Ricerca tutte le Annotazioni Manuali per un certo fascicolo legate a una Decisione del GE e validate
	 * nel periodo passato in input (se specificato). - Depenalizzazione (004)<br>
	 * - Incostituzionalità (013)<br>
	 * - Amnistia (003)<br>
	 * - Indulto (002)<br>
	 * - (MEV 37) Illecito Amministrativo (017)<br>
	 *
	 * E' possibile specificare anche un solo estremo dell'intervallo.
	 *
	 * @param aKey
	 * @param aDataDal
	 * @param aDataAl
	 * @throws DAOException
	 */
	public void ricercaAnnManualeDecisioniGEByIdFascicoloDateValidazione(BigDecimal aKey, Date aDataDal,
			Date aDataAl) throws DAOException {
		String lStatement = "";

		// La select va in join con la tabella evento per recuperare solo le
		// annotazioni associate ad eventi validati (eventualmente non periodo
		// specificato in input)
		// n.b. le decisioni del GE (annotazioni) sono legate al provvedimento di
		// concessione. Tale provvedimento non produce una stampa. L'evento
		// che valida il provvedimento di concessione può essere un ordine di
		// scarcerazione, oppure una comunicazione, o entrambi.
		// Nel primo caso, (validazione dell'OS), viene validato il provvedimento
		// e l'annotazione, ma non vengono valorizzate le date aggiornamento.
		// Per cui non è possibile sapere QUANDO è avvenuta la validazione se non
		// andando in join con l'OS.
		// Nel caso della Comunicazione invece vengono validate le date aggiornamento,
		// ma la comunicazione non viene legata al provvedimento di concessione, (manca eve_id_evento)
		// per cui per conoscere quando è avvenuta la validazione è possibile
		// solo lavorare sul provvedimento di concessione.
		lStatement += " SELECT " + " AM.ID_ANNOTAZIONE_MANUALE, " + " AM.ANNO_ID_ANNOTAZIONE_MANUALE, "
				+ " AM.COD_TIPO_ANNOTAZIONE,null DESCR_TIPO, " + " AM.FLAG_PIU_MENO, "
				+ " AM.NUM_ANNI_RECLUSIONE, AM.NUM_MESI_RECLUSIONE, AM.NUM_GIORNI_RECLUSIONE, AM.IMPORTO_MULTA, "
				+ " AM.NUM_ANNI_ARRESTO, AM.NUM_MESI_ARRESTO, AM.NUM_GIORNI_ARRESTO, AM.IMPORTO_AMMENDA, "
				+ " AM.DATA_ARRESTO_DA, AM.DATA_ARRESTO_A, "
				+ " AM.DATA_RECLUSIONE_DA, AM.DATA_RECLUSIONE_A, "
				+ " AM.DATA_RICEZIONE_DOC, AM.MOTIVAZIONI, AM.NOTE_RECLUSIONE, "
				+ " AM.ANNO_GE, AM.NUMERO_GE, AM.ANNO_REGE, AM.NUMERO_REGE, AM.ANNO_MC, "
				+ " AM.NUMERO_MC, AM.ANNO_CDA, AM.NUMERO_CDA, AM.ANNO_CC, AM.NUMERO_CC, "
				+ " AM.ANNO_SIEP, AM.NUMERO_SIEP, " + " AM.COD_TIPO_UFFICIO_SIEP, "
				+ " AM.COD_LUOGO_UFFICIO_SIEP, " + " AM.DATA_ISCRIZIONE_SIEP, "
				+ " AM.COD_FONTE, null DESCR_FONTE, AM.ANNO_FONTE, AM.NUMERO_FONTE, "
				+ " AM.COD_SOTTONUMERAZIONE, null DESCR_SOTTONUM,"
				+ " AM.COMMA, AM.LETTERA, AM.NUMERO, AM.ARTICOLO, "
				+ " AM.COD_CAUSALE_COMPUTO, null DESCR_CAUCOMP," + " AM.COD_DPR, null DESCR_DPR,"
				+ " AM.COD_OPERATORE_INSERIMENTO, AM.DATA_INSERIMENTO, AM.COD_UFFICIO_INSERIMENTO, "
				+ " AM.COD_OPERATORE_AGGIORNAMENTO, AM.DATA_AGGIORNAMENTO, AM.COD_UFFICIO_AGGIORNAMENTO, "
				+ " AM.FAS_SIE_ID_FASCICOLO_SIEP, " + " AM.REA_ID_REATO, " + " AM.EVE_ID_EVENTO, "
				+ " AM.FLAG_VALIDATO, " + " AM.FLAG_CONFORME, " + " AM.FLAG_APP_PROVVISORIA, "
				+ " AM.PEN_RES_ID_PENA_RESIDUA, " + " AM.FUN_ID_FUNGIBILITA, " + " AM.DATA_RICHIESTA, "
				+ " AM.DATA_CC, " + " AM.DATA_GE, "
				+ " AM.ANNO_SENTENZA_SIAP, AM.NUMERO_SENTENZA_SIAP, AM.DATA_SENTENZA_SIAP, " +
				// 07_2015 MEV29 punto 11 - Anno e Numero procedimento SIGE
				" AM.ANNO_SIGE, AM.NUMERO_SIGE, " + " AM.FLAG_COMPUTABILE, " + " AM.SEN_ID_SENTENZA, "
				+ " AM.TEN_ID_TENORE_SIGE, " + " AM.FLAG_SEL_QUANTUM, " + " AM.FLG_BENEFICIO_DETRATTO ";
		lStatement += " FROM ANNOTAZIONE_MANUALE AM, EVENTO ";
		lStatement += " WHERE AM.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += "   AND AM.EVE_ID_EVENTO = evento.ID_EVENTO "; // join con il provvedimento
		// MEV 37 - inizio
		// lStatement += " AND AM.COD_TIPO_ANNOTAZIONE in ('002','003','004','013') ";
		lStatement += "   AND AM.COD_TIPO_ANNOTAZIONE in ('002','003','004','013','017') ";
		// MEV 37 - Fine
		lStatement += "   AND AM.FLAG_APP_PROVVISORIA = '-' "; // solo le decisioni

		// Scarto le decisioni migrate RES, hanno una gestione particolare
		lStatement += "   AND EVENTO.cod_operatore_inserimento not like '%res%' ";

		// Scarto le decisioni di Rigetto (R), Inammissibilità(I), Riunisce ('U')
		// in questi casi infatti le richieste non verranno considerate in quanto
		// legate a una richiesta, ma non bisogna nemmeno considerare le decisioni
		// altrimenti si revocano due volte.
		lStatement += "   AND AM.FLAG_COMPUTABILE = 'S' "; //
		// lStatement += " AND AM.FLAG_CONFORME not in ('R','I','U') ";
		// Attenzione! Verificare se è corretto mettere il filtro sull'evento che
		// limita la generalità della select è comporta il suo aggiornamento in caso
		// di modifica/aggiunta dei codici evento
		// Condizioni sul tipo di evento. Le annotazioni devono essere legate a un
		// provvedimento di 'Applicazione'
		// 0284-Applicazione Amnistia / Indulto
		// 0285-Applicazione depenalizzazione
		// 0286-Applicazione incostituzionalita'
		lStatement += "   AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		lStatement += "   AND evento.COD_TIPO_EVENTO='01' ";
		lStatement += "   AND evento.COD_TIPO_PROVVEDIMENTO = '04' ";
		lStatement += "   AND evento.COD_MOTIVO in ('0284','0285','0286')  ";
		// ==========================================================================
		// Per verificare le date di validazione devo andare in join con l'evento
		// che ha validato l'annotazione. Tale evento è un ordine di scarcerazione
		// per nuova scadenza pena (se in espiazione) o una comunicazione (se libero).
		// Il problema è che la semplice comunicazione non è collegata al provvedimento,
		// manca l'eve_id_evento. Per cui nel caso di concessione dei benefici a
		// soggetti liberi in cui viene emessa la sola comunicazione non posso
		// verificare se l'annotazione è collegata a un evento che la ha validata
		// Devo lavorare per forza sulla data di validazione del provvedimento.
		// ==========================================================================
		// Aggiungo le condizioni sull'intervallo di validazione sul provvedimento
		// sebbene venga validato indirettamente
		if (aDataDal != null || aDataAl != null) {
			// Condizioni valide SOLO se ho una comunicazione (solo in questo caso è valorizzata la data agg.
			// sul provvedimento)
			lStatement += " AND ( ";
			lStatement += "      ( ";
			if (aDataDal != null) {
				// lStatement +=
				// " evento.DATA_AGGIORNAMENTO > to_date ('"+DateUtils.getDateToString(aDataDal,"dd/MM/yyyy
				// HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
				lStatement += "      evento.DATA_INSERIMENTO > to_date ('"
						+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss")
						+ "','dd/MM/yyyy hh24:mi:ss')";
			}
			if (aDataAl != null) {// n.b. <= perchè devo beccare anche l'evento corrente
				if (aDataDal != null) {
					lStatement += " AND";
				}
				// lStatement +=
				// " evento.DATA_AGGIORNAMENTO <= to_date ('"+DateUtils.getDateToString(aDataAl,"dd/MM/yyyy
				// HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
				lStatement += "      evento.DATA_INSERIMENTO <= to_date ('"
						+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
						+ "','dd/MM/yyyy hh24:mi:ss')";
			}
			lStatement += "      ) "; // chiude la condizione sul data agg provvedimento
			// La join l'ordine di scarcerazione in OR per evitare che vengano scartate
			// le AM legate solo a OS
			// n.b. la join è necessaria solo se ho un intervallo temporale di ricerca
			lStatement += "OR evento.id_evento in ( ";
			lStatement += "     SELECT evento.eve_id_evento ";
			lStatement += "       FROM evento ";
			lStatement += "      WHERE evento.fas_sie_id_fascicolo_siep = " + aKey;
			lStatement += "        AND evento.flag_documento_registrato = 'S' ";
			lStatement += "        AND evento.cod_tipo_evento = '01' ";
			lStatement += "        AND evento.cod_tipo_provvedimento = '09' ";
			lStatement += "        AND evento.cod_motivo IN ('0160', '0158', '0159')";
			// Aggiungo le condizioni sull'intervallo di validazione
			if (aDataDal != null) {
				// lStatement +=
				// " AND evento.DATA_AGGIORNAMENTO > to_date
				// ('"+DateUtils.getDateToString(aDataDal,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
				lStatement += "      AND evento.DATA_INSERIMENTO > to_date ('"
						+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss")
						+ "','dd/MM/yyyy hh24:mi:ss')";
			}
			if (aDataAl != null) {// n.b. <= perchè devo beccare anche l'evento corrente
				// lStatement +=
				// " AND evento.DATA_AGGIORNAMENTO <= to_date
				// ('"+DateUtils.getDateToString(aDataAl,"dd/MM/yyyy HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
				lStatement += "      AND evento.DATA_INSERIMENTO <= to_date ('"
						+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
						+ "','dd/MM/yyyy hh24:mi:ss')";
			}
			lStatement += "   )    "; // and select in join
			lStatement += "   )    "; // and condizine sulle date
		}

		setStatement(lStatement);
	}

	/**
	 * Ricerca tutte le Annotazioni Manuali per un certo fascicolo legate a una Richiesta al GE con
	 * anticipazione degli effetti e validate nel periodo passato in input (se specificato). -
	 * Depenalizzazione (004)<br>
	 * - Incostituzionalità (013)<br>
	 * - Amnistia (003)<br>
	 * - Indulto (002)<br>
	 * - (MEV 37) Illecito Amministrativo (017)<br>
	 *
	 * E' possibile specificare anche un solo estremo dell'intervallo. La select esclude le richieste legate a
	 * una decisione validata nel periodo in esame.
	 *
	 * @param aKey
	 * @param aDataDal
	 *            >
	 * @param aDataAl
	 *            <=
	 * @throws DAOException
	 */
	public void ricercaAnnManualeRichiesteGEByIdFascicoloDateValidazione(BigDecimal aKey, Date aDataDal,
			Date aDataAl) throws DAOException {
		String lStatement = "";

		// La select va in join con la tabella evento per recuperare solo le
		// annotazioni associate ad eventi validati (eventualmente nel periodo
		// specificato in input)
		lStatement += " SELECT AM.ID_ANNOTAZIONE_MANUALE, AM.ANNO_ID_ANNOTAZIONE_MANUALE,  AM.COD_TIPO_ANNOTAZIONE,null DESCR_TIPO, "
				+ " AM.FLAG_PIU_MENO, "
				+ " AM.NUM_ANNI_RECLUSIONE, AM.NUM_MESI_RECLUSIONE, AM.NUM_GIORNI_RECLUSIONE, AM.IMPORTO_MULTA, "
				+ " AM.NUM_ANNI_ARRESTO, AM.NUM_MESI_ARRESTO, AM.NUM_GIORNI_ARRESTO, AM.IMPORTO_AMMENDA, "
				+ " AM.DATA_ARRESTO_DA, AM.DATA_ARRESTO_A, AM.DATA_RECLUSIONE_DA, AM.DATA_RECLUSIONE_A, "
				+ " AM.DATA_RICEZIONE_DOC, AM.MOTIVAZIONI, AM.NOTE_RECLUSIONE, "
				+ " AM.ANNO_GE, AM.NUMERO_GE, AM.ANNO_REGE, AM.NUMERO_REGE, AM.ANNO_MC, "
				+ " AM.NUMERO_MC, AM.ANNO_CDA, AM.NUMERO_CDA, AM.ANNO_CC, AM.NUMERO_CC, "
				+ " AM.ANNO_SIEP, AM.NUMERO_SIEP, "
				+ " AM.COD_TIPO_UFFICIO_SIEP, AM.COD_LUOGO_UFFICIO_SIEP, AM.DATA_ISCRIZIONE_SIEP, "
				+ " AM.COD_FONTE, null DESCR_FONTE, AM.ANNO_FONTE, AM.NUMERO_FONTE, "
				+ " AM.COD_SOTTONUMERAZIONE, null DESCR_SOTTONUM, AM.COMMA, AM.LETTERA, AM.NUMERO, AM.ARTICOLO, "
				+ " AM.COD_CAUSALE_COMPUTO, null DESCR_CAUCOMP, AM.COD_DPR, null DESCR_DPR,"
				+ " AM.COD_OPERATORE_INSERIMENTO, AM.DATA_INSERIMENTO, AM.COD_UFFICIO_INSERIMENTO, "
				+ " AM.COD_OPERATORE_AGGIORNAMENTO, AM.DATA_AGGIORNAMENTO, AM.COD_UFFICIO_AGGIORNAMENTO, "
				+ " AM.FAS_SIE_ID_FASCICOLO_SIEP, AM.REA_ID_REATO, AM.EVE_ID_EVENTO, AM.FLAG_VALIDATO, "
				+ " AM.FLAG_CONFORME, AM.FLAG_APP_PROVVISORIA, AM.PEN_RES_ID_PENA_RESIDUA, AM.FUN_ID_FUNGIBILITA, "
				+ " AM.DATA_RICHIESTA, AM.DATA_CC, AM.DATA_GE, "
				+ " AM.ANNO_SENTENZA_SIAP, AM.NUMERO_SENTENZA_SIAP, AM.DATA_SENTENZA_SIAP, " +
				// 07_2015 MEV29 punto 11 - Anno e Numero procedimento SIGE
				" AM.ANNO_SIGE, AM.NUMERO_SIGE, " + " AM.FLAG_COMPUTABILE, " + " AM.FLG_BENEFICIO_DETRATTO, "
				+ " AM.SEN_ID_SENTENZA, " + " AM.TEN_ID_TENORE_SIGE, " + " AM.FLAG_SEL_QUANTUM  ";
		lStatement += " FROM ANNOTAZIONE_MANUALE AM  "; // , EVENTO
		lStatement += " WHERE AM.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		// MEV 37 - Inizio
		// lStatement += " AND AM.COD_TIPO_ANNOTAZIONE in ('002','003','004','013') ";
		lStatement += "   AND AM.COD_TIPO_ANNOTAZIONE in ('002','003','004','013','017') ";
		// MEV 37 - Fine
		lStatement += "   AND AM.FLAG_APP_PROVVISORIA = 'A' ";
		lStatement += "   AND AM.FLAG_VALIDATO = 'S' ";

		// Escludo le richieste migrate RES che verranno considerate a parte
		lStatement += "   AND AM.COD_OPERATORE_INSERIMENTO not like '%res%' ";

		// Attenzione! Nel caso delle Richieste, l'evento che le valida non è
		// ricollegabile all'annotazione (mancano i ref sulle tabelle) per cui devo
		// lavorare direttamente sulla data_aggiornamento dell'annotazione che per
		// fortuna viene valorizzata con la data validazione
		if (aDataDal != null) {
			// lStatement +=
			// " AND AM.DATA_AGGIORNAMENTO > to_date ('"+DateUtils.getDateToString(aDataDal,"dd/MM/yyyy
			// HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
			lStatement += "      AND AM.DATA_INSERIMENTO > to_date ('"
					+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}
		if (aDataAl != null) {// n.b. <= perchè devo beccare anche l'evento corrente
			// lStatement +=
			// " AND AM.DATA_AGGIORNAMENTO <= to_date ('"+DateUtils.getDateToString(aDataAl,"dd/MM/yyyy
			// HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
			lStatement += "      AND AM.DATA_INSERIMENTO <= to_date ('"
					+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
		}

		// aggiungo condizione che permette di verificare se la Richiesta è collegata
		// a una decisione, in questo caso non va considerata.
		// La richiesta non deve essere collegata ad alcuna decisione (anno_id_annotazione_manuale = null)
		// o comunque non a una decisione validata
		lStatement += " AND (   am.anno_id_annotazione_manuale is null ";
		lStatement += "      OR am.anno_id_annotazione_manuale NOT IN (select id_annotazione_manuale";
		lStatement += "                                                  from annotazione_manuale";
		// MEV 37 - Inizio
		// lStatement +=
		// " where cod_tipo_annotazione IN ('002', '003', '004', '013')";
		lStatement += "                                          where cod_tipo_annotazione IN ('002', '003', '004', '013', '017')";
		// MEV 37 - Fine
		lStatement += "                                                   and flag_app_provvisoria = '-'"; // decisioni
		lStatement += "                                                   and flag_validato = 'S'";
		if (aDataDal != null) {
			lStatement += "                                                and DATA_INSERIMENTO > to_date ('"
					+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
			// lStatement +=
			// " and DATA_AGGIORNAMENTO > to_date ('"+DateUtils.getDateToString(aDataDal,"dd/MM/yyyy
			// HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
		}
		if (aDataAl != null) {// n.b. <= perchè devo beccare anche l'evento corrente
			lStatement += "                                                and DATA_INSERIMENTO <= to_date ('"
					+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
			// lStatement +=
			// " and DATA_AGGIORNAMENTO <= to_date ('"+DateUtils.getDateToString(aDataAl,"dd/MM/yyyy
			// HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
		}
		lStatement += "                                            )";
		lStatement += "     )";

		// La join con l'evento direttamente collegato con la AM è superflua in quanto
		// viene validato contestualmente all'inserimento dell'annotazione per cui
		// non mi fornisce alcuna informazione
		// lStatement += " AND AM.EVE_ID_EVENTO = evento.ID_EVENTO ";
		// lStatement += " AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		// lStatement += " AND evento.COD_TIPO_EVENTO='01' " ;
		// lStatement += " AND evento.COD_TIPO_PROVVEDIMENTO = '26' ";
		// lStatement += " AND evento.COD_MOTIVO in ('0210','0211','0122') ";
		// if (aDataDal!=null){
		// lStatement +=
		// " AND evento.DATA_AGGIORNAMENTO > to_date ('"+DateUtils.getDateToString(aDataDal,"dd/MM/yyyy
		// HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
		// }
		// if (aDataAl!=null){// n.b. <= perchè devo beccare anche l'evento corrente
		// lStatement +=
		// " AND evento.DATA_AGGIORNAMENTO <= to_date ('"+DateUtils.getDateToString(aDataAl,"dd/MM/yyyy
		// HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
		// }

		setStatement(lStatement);
	}

	/**
	 * Ricerca tutte le Annotazioni Manuali per un certo fascicolo legate a un Indulto (2006) migrato RES
	 * validate nel periodo passato in input (se specificato).
	 *
	 * Recupera sia le richieste che le decisioni Richieste: 01- (04,26) - 0161 Determinazione pena a seguito
	 * di applicazione beneficio Decisioni: 01- 04 - 0284 - Applicazione Amnistia / Indulto
	 *
	 * E' possibile specificare anche un solo estremo dell'intervallo.
	 *
	 * @param aKey
	 * @param aDataDal
	 * @param aDataAl
	 * @throws DAOException
	 */
	public void ricercaAnnManualeIndultiRESByIdFascicoloDateValidazione(BigDecimal aKey, Date aDataDal,
			Date aDataAl) throws DAOException {
		String lStatement = "";

		// La select va in join con la tabella evento per recuperare solo le
		// annotazioni associate ad eventi validati (eventualmente non periodo
		// specificato in input)
		// n.b. le decisioni del GE (annotazioni) sono legate al provvedimento di
		// concessione. Tale provvedimento non produce una stampa. L'evento
		// che valida il provvedimento di concessione può essere un ordine di
		// scarcerazione, oppure una comunicazione, o entrambi.
		// Nel primo caso, (validazione dell'OS), viene validato il provvedimento
		// e l'annotazione, ma non vengono valorizzate le date aggiornamento.
		// Per cui non è possibile sapere QUANDO è avvenuta la validazione se non
		// andando in join con l'OS.
		// Nel caso della Comunicazione invece vengono validate le date aggiornamento,
		// ma la comunicazione non viene legata al provvedimento di concessione, (manca eve_id_evento)
		// per cui per conoscere quando è avvenuta la validazione è possibile
		// solo lavorare sul provvedimento di concessione.
		lStatement += " SELECT " + " AM.ID_ANNOTAZIONE_MANUALE, " + " AM.ANNO_ID_ANNOTAZIONE_MANUALE, "
				+ " AM.COD_TIPO_ANNOTAZIONE,null DESCR_TIPO, " + " AM.FLAG_PIU_MENO, "
				+ " AM.NUM_ANNI_RECLUSIONE, AM.NUM_MESI_RECLUSIONE, AM.NUM_GIORNI_RECLUSIONE, AM.IMPORTO_MULTA, "
				+ " AM.NUM_ANNI_ARRESTO, AM.NUM_MESI_ARRESTO, AM.NUM_GIORNI_ARRESTO, AM.IMPORTO_AMMENDA, "
				+ " AM.DATA_ARRESTO_DA, AM.DATA_ARRESTO_A, "
				+ " AM.DATA_RECLUSIONE_DA, AM.DATA_RECLUSIONE_A, "
				+ " AM.DATA_RICEZIONE_DOC, AM.MOTIVAZIONI, AM.NOTE_RECLUSIONE, "
				+ " AM.ANNO_GE, AM.NUMERO_GE, AM.ANNO_REGE, AM.NUMERO_REGE, AM.ANNO_MC, "
				+ " AM.NUMERO_MC, AM.ANNO_CDA, AM.NUMERO_CDA, AM.ANNO_CC, AM.NUMERO_CC, "
				+ " AM.ANNO_SIEP, AM.NUMERO_SIEP, " + " AM.COD_TIPO_UFFICIO_SIEP, "
				+ " AM.COD_LUOGO_UFFICIO_SIEP, " + " AM.DATA_ISCRIZIONE_SIEP, "
				+ " AM.COD_FONTE, null DESCR_FONTE, AM.ANNO_FONTE, AM.NUMERO_FONTE, "
				+ " AM.COD_SOTTONUMERAZIONE, null DESCR_SOTTONUM,"
				+ " AM.COMMA, AM.LETTERA, AM.NUMERO, AM.ARTICOLO, "
				+ " AM.COD_CAUSALE_COMPUTO, null DESCR_CAUCOMP," + " AM.COD_DPR, null DESCR_DPR,"
				+ " AM.COD_OPERATORE_INSERIMENTO, AM.DATA_INSERIMENTO, AM.COD_UFFICIO_INSERIMENTO, "
				+ " AM.COD_OPERATORE_AGGIORNAMENTO, AM.DATA_AGGIORNAMENTO, AM.COD_UFFICIO_AGGIORNAMENTO, "
				+ " AM.FAS_SIE_ID_FASCICOLO_SIEP, " + " AM.REA_ID_REATO, " + " AM.EVE_ID_EVENTO, "
				+ " AM.FLAG_VALIDATO, " + " AM.FLAG_CONFORME, " + " AM.FLAG_APP_PROVVISORIA, "
				+ " AM.PEN_RES_ID_PENA_RESIDUA, " + " AM.FUN_ID_FUNGIBILITA, " + " AM.DATA_RICHIESTA, "
				+ " AM.DATA_CC, " + " AM.DATA_GE, "
				+ " AM.ANNO_SENTENZA_SIAP, AM.NUMERO_SENTENZA_SIAP, AM.DATA_SENTENZA_SIAP, " +
				// 07_2015 MEV29 punto 11 - Anno e Numero procedimento SIGE
				" AM.ANNO_SIGE, AM.NUMERO_SIGE, "
				+ " AM.FLAG_COMPUTABILE, AM.SEN_ID_SENTENZA, AM.TEN_ID_TENORE_SIGE, "
				+ " AM.FLAG_SEL_QUANTUM, " + " AM.FLG_BENEFICIO_DETRATTO ";
		lStatement += " FROM ANNOTAZIONE_MANUALE AM, EVENTO ";
		lStatement += " WHERE AM.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lStatement += "   AND AM.EVE_ID_EVENTO = evento.ID_EVENTO "; // join con il provvedimento
		lStatement += "   AND AM.COD_TIPO_ANNOTAZIONE = '002' ";
		// lStatement += " AND AM.FLAG_APP_PROVVISORIA = '-' "; // n.b. vengono scartati le R

		lStatement += "   AND AM.FLAG_APP_PROVVISORIA in ('A','-') "; // n.b. vengono scartati le R

		// --------------------------------------------------------------------------------
		// ANNO_ID_ANNOTAZIONE_MANUALE è <> null solo sulle richieste agganciate da
		// una decisione. In questo caso fa fede la decisione per cui scarto la richiesta
		// Se invece non è ancora intervenuta una decisione, ANNO_ID_ANNOTAZIONE_MANUALE = null
		// e quinid devo computare anche la richiesta.
		// lStatement += " AND AM.ANNO_ID_ANNOTAZIONE_MANUALE is null "; // ????
		// aggiungo condizione che permette di verificare se la Richiesta è collegata
		// a una decisione, in questo caso non va considerata.
		// La richiesta non deve essere collegata ad alcuna decisione (anno_id_annotazione_manuale = null)
		// o comunque non a una decisione validata
		lStatement += " AND (   am.anno_id_annotazione_manuale is null ";
		lStatement += "      OR am.anno_id_annotazione_manuale NOT IN (select id_annotazione_manuale";
		lStatement += "                                                  from annotazione_manuale";
		lStatement += "                                                 where cod_tipo_annotazione IN ('002')"; // ,
																												// '003',
																												// '004',
																												// '013'
		lStatement += "                                                   and flag_app_provvisoria = '-'"; // decisioni
		lStatement += "                                                   and flag_validato = 'S'";
		if (aDataDal != null) {
			lStatement += "                                                and DATA_INSERIMENTO > to_date ('"
					+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
			// lStatement +=
			// " and DATA_AGGIORNAMENTO > to_date ('"+DateUtils.getDateToString(aDataDal,"dd/MM/yyyy
			// HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
		}
		if (aDataAl != null) {// n.b. <= perchè devo beccare anche l'evento corrente
			lStatement += "                                                and DATA_INSERIMENTO <= to_date ('"
					+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
					+ "','dd/MM/yyyy hh24:mi:ss')";
			// lStatement +=
			// " and DATA_AGGIORNAMENTO <= to_date ('"+DateUtils.getDateToString(aDataAl,"dd/MM/yyyy
			// HH:mm:ss")+"','dd/MM/yyyy hh24:mi:ss')";
		}
		lStatement += "                                                )";
		lStatement += "     )";
		// --------------------------------------------------------------------------------

		// Solo le decisioni migrate RES
		lStatement += "   AND EVENTO.cod_operatore_inserimento like '%res%' ";

		// Condizioni sul tipo di evento. Le annotazioni devono essere legate a un
		// provvedimento di Richieste o Applicazione
		// 0161-Determinazione pena a seguito di applicazione beneficio
		// 0284-Applicazione Amnistia / Indulto
		lStatement += "   AND evento.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		lStatement += "   AND evento.COD_TIPO_EVENTO='01' ";

		lStatement += "   AND (    (     evento.COD_TIPO_PROVVEDIMENTO in ('04','26') ";
		lStatement += "              AND evento.COD_MOTIVO = '0161'  ";
		lStatement += "            )  ";
		lStatement += "         OR   ";
		lStatement += "            (     evento.COD_TIPO_PROVVEDIMENTO = '04' ";
		lStatement += "              AND evento.COD_MOTIVO = '0284'  ";
		lStatement += "            )  ";
		lStatement += "       )  ";

		// lStatement += " AND evento.COD_TIPO_PROVVEDIMENTO = '04' ";
		// lStatement += " AND evento.COD_MOTIVO in ('0161','0284') ";

		// ==========================================================================
		// Aggiungo le condizioni sull'intervallo di validazione sul provvedimento
		if (aDataDal != null || aDataAl != null) {
			//
			lStatement += " AND ( ";
			if (aDataDal != null) {
				lStatement += "      evento.DATA_INSERIMENTO > to_date ('"
						+ DateUtils.getDateToString(aDataDal, "dd/MM/yyyy HH:mm:ss")
						+ "','dd/MM/yyyy hh24:mi:ss')";
			}
			if (aDataAl != null) {// n.b. <= perchè devo beccare anche l'evento corrente
				if (aDataDal != null) {
					lStatement += " AND";
				}
				lStatement += "      evento.DATA_INSERIMENTO <= to_date ('"
						+ DateUtils.getDateToString(aDataAl, "dd/MM/yyyy HH:mm:ss")
						+ "','dd/MM/yyyy hh24:mi:ss')";
			}
			lStatement += "   )    "; // and condizine sulle date
		}

		// Ordino dal più recente
		lStatement += "ORDER BY evento.DATA_INSERIMENTO DESC ";

		setStatement(lStatement);
	}

	public void ricercaAnnotazioneManualeByIdReato(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND REA_ID_REATO=" + aKey;

		setStatement(lSql);
	}

	/**
	 * Ricerca tutte le annotazioni manuali con FLAG_APP_PROVVISORIA<>A e R associate al reato
	 *
	 * @param aKey
	 *            id del reato
	 * @throws DAOException
	 */
	public void ricercaAnnotazioneManualeNonRichiesteByIdReato(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		// lSql += " AND COD_TIPO_ANNOTAZIONE<>'002'";
		// lSql += " AND COD_TIPO_ANNOTAZIONE<>'003'";
		lSql += " AND FLAG_APP_PROVVISORIA<>'A'";
		lSql += " AND FLAG_APP_PROVVISORIA<>'R'";
		lSql += " AND REA_ID_REATO=" + aKey;

		setStatement(lSql);
	}

	/**
	 * Ricerca le annotazioni manuali per codice fascicolo siep, tipo annotazione se specificato,
	 * eventualmente validate. <b>scartando le richieste al GE</b> ORDER BY DATA_INSERIMENTO
	 *
	 * @param aIdFascicolo
	 * @param aCodTipoAnnotazione
	 * @param aFlagValidazione
	 * @throws DAOException
	 */
	public void ricercaAnnotazioneManualeByIdFascicoloNonRichiesteTipoAnn(BigDecimal aIdFascicolo,
			String aCodTipoAnnotazione, String aFlagValidazione) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;
		if (aCodTipoAnnotazione != null) {
			if (aCodTipoAnnotazione.equals("003") || aCodTipoAnnotazione.equals("002")) // Nel Caso di
																						// Amnistia o Indulto
																						// prende anche
																						// l'altro
																						// direttamente
				lSql += " AND ( COD_TIPO_ANNOTAZIONE='003' OR COD_TIPO_ANNOTAZIONE='002' )";
			// MEV 37 - Depenalizzazione
			else if (aCodTipoAnnotazione.equals("004") || aCodTipoAnnotazione.equals("017")) // Nel Caso di
																								// Depenalizzazione
																								// prende
																								// entrambi i
																								// codici
																								// previsti
				lSql += " AND ( COD_TIPO_ANNOTAZIONE='004' OR COD_TIPO_ANNOTAZIONE='017' )";
			// Fine MEV 37 - Depenalizzazione
			else
				lSql += " AND COD_TIPO_ANNOTAZIONE='" + aCodTipoAnnotazione + "'";
		}
		lSql += " AND FLAG_VALIDATO='" + aFlagValidazione + "'";
		lSql += " AND FLAG_APP_PROVVISORIA<>'A'";
		lSql += " AND FLAG_APP_PROVVISORIA<>'R'";

		lSql += setOrder(); // ORDINE CRESCENTE PER DATA_INSERIMENTO

		setStatement(lSql);
	}

	public void ricercaAnnotazioneManuale_non_richieste_ByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aKey;
		lSql += " AND COD_TIPO_ANNOTAZIONE<>'012' AND COD_TIPO_ANNOTAZIONE<>'011'";

		setStatement(lSql);
	}

	/**
	 * Recupera tutte le annotazioni di amnistia e indulto manuali legate a Richieste con ancticipazione ma
	 * non ancora validate
	 *
	 * @param aKey
	 * @throws DAOException
	 */
	public void ricercaAnnotazioneManualeRichiesteAnticipazioneAministiaIndultoByIdFascicolo(BigDecimal aKey)
			throws DAOException {

		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aKey;
		lSql += " AND ( COD_TIPO_ANNOTAZIONE='003' OR COD_TIPO_ANNOTAZIONE='002' )"; // AMNISTIA-INDULTO
		lSql += " AND FLAG_APP_PROVVISORIA='A'"; // ANTICIPAZIONE
		lSql += " AND FLAG_VALIDATO='N'";

		setStatement(lSql);
	}

	/**
	 * Ricerca le annotazioni di tipo Amnistia o Indulto lagate a Richieste senza anticipazione <b>non
	 * validate</b>
	 *
	 * @param aKey
	 *            idFascicolo
	 * @throws DAOException
	 */
	public void ricercaAnnotazioneManualeRichiesteAministiaIndultoByIdFascicolo(BigDecimal aKey)
			throws DAOException {

		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aKey;
		lSql += " AND ( COD_TIPO_ANNOTAZIONE='003' OR COD_TIPO_ANNOTAZIONE='002' )"; // AMNISTIA-INDULTO
		lSql += " AND FLAG_APP_PROVVISORIA='R'"; // RICHIESTA
		lSql += " AND FLAG_VALIDATO='N'";

		setStatement(lSql);
	}

	public void ricercaAnnotazioneManualeGenerico(AnnotazioneManualeModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}
		if (aModel.getCodTipoAnnotazione() != null) {
			String lCodTipo = aModel.getCodTipoAnnotazione();
			if (lCodTipo.equals("003") || lCodTipo.equals("002")) // Nel Caso di Amnistia o Indulto prende
																	// anche l'altro direttamente
				lSql += " AND ( COD_TIPO_ANNOTAZIONE='003' OR COD_TIPO_ANNOTAZIONE='002' )";
			// MEV 37 - Inizio
			else if (lCodTipo.equals("004")) // Nel Caso di Depenalizzazione, aggiungo anche ILLECITO
												// AMMINISTRATIVO (cod=017)
				lSql += " AND ( COD_TIPO_ANNOTAZIONE='004' OR COD_TIPO_ANNOTAZIONE='017' )";
			// MEV 37 - Fine
			else
				lSql += " AND COD_TIPO_ANNOTAZIONE='" + aModel.getCodTipoAnnotazione() + "'";
		}
		if (aModel.getFlagAppProvvisoria() != null && !aModel.getFlagAppProvvisoria().equals("")) {
			String lFlagApp = aModel.getFlagAppProvvisoria();
			if (lFlagApp.equals("RICHIESTE"))
				lSql += " AND ( FLAG_APP_PROVVISORIA='R' OR FLAG_APP_PROVVISORIA='A')";
			else
				lSql += " AND FLAG_APP_PROVVISORIA='" + aModel.getFlagAppProvvisoria() + "'";
		}
		if (aModel.getFlagValidato() != null && !aModel.getFlagValidato().equals("")) {
			lSql += " AND FLAG_VALIDATO='" + aModel.getFlagValidato() + "'";
		}

		// MEV9 - inserisco order by
		lSql += " ORDER BY ID_ANNOTAZIONE_MANUALE ";

		setStatement(lSql);
	}

	public void ricercaRichieste(AnnotazioneManualeModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}
		if (aModel.getCodTipoAnnotazione() != null) {
			lSql += " AND COD_TIPO_ANNOTAZIONE='" + aModel.getCodTipoAnnotazione() + "'";
		}

		lSql += " AND ( FLAG_APP_PROVVISORIA='R' OR FLAG_APP_PROVVISORIA='A')";

		if (aModel.getFlagValidato() != null && !aModel.getFlagValidato().equals("")) {
			lSql += " AND FLAG_VALIDATO='" + aModel.getFlagValidato() + "'";
		}

		setStatement(lSql);
	}

	public void ricercaRichiesteTenoriSige(AnnotazioneManualeModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}
		if (aModel.getCodTipoAnnotazione() != null) {
			lSql += " AND COD_TIPO_ANNOTAZIONE='" + aModel.getCodTipoAnnotazione() + "'";
		}

		lSql += " AND ( FLAG_APP_PROVVISORIA='R' OR FLAG_APP_PROVVISORIA='A')";

		if (aModel.getFlagValidato() != null && !aModel.getFlagValidato().equals("")) {
			lSql += " AND FLAG_VALIDATO='" + aModel.getFlagValidato() + "'";
		}

		lSql += " AND ANNO_ID_ANNOTAZIONE_MANUALE is NULL";

		setStatement(lSql);
	}

	public void ricercaAnnotazioneManuale(BigDecimal aKey, String aTipoAnnotazione, String aFlagAppProvv,
			String aFlagValidato) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aKey;
		lSql += " AND COD_TIPO_ANNOTAZIONE='" + aTipoAnnotazione + "'";
		lSql += " AND FLAG_APP_PROVVISORIA='" + aFlagAppProvv + "'";
		lSql += " AND FLAG_VALIDATO='" + aFlagValidato + "'";

		setStatement(lSql);
	}

	public void ricercaAnnotazioneManualeConSenzaRichiestaByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aKey;
		lSql += " AND ( COD_TIPO_ANNOTAZIONE='003' OR COD_TIPO_ANNOTAZIONE='002' )"; // AMNISTIA-INDULTO
		lSql += " AND ( FLAG_APP_PROVVISORIA='R' OR FLAG_APP_PROVVISORIA='A' )";
		lSql += " AND FLAG_VALIDATO='N'";

		setStatement(lSql);
	}

	/**
	 * Ricerca le Annotazioni Manuali con richiesta al GE: flag_app_provvisoria 'R' o 'A' indipendentemente
	 * dallo stato (FLAG_VALIDATO) ordinate per data_inserimento desc
	 *
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaAnnotazioneManualeFlagAppProvByIdFascicolo(BigDecimal aIdFascicolo)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP= " + aIdFascicolo;
		lSql += " AND ( FLAG_APP_PROVVISORIA='R' OR FLAG_APP_PROVVISORIA='A' )";
		lSql += " ORDER BY DATA_INSERIMENTO DESC ";

		setStatement(lSql);
	}

	/**
	 * Ricerca le Annotazioni Manuali legate all'evento passato in input
	 *
	 * @param aKeyEvento
	 * @throws DAOException
	 */
	public void ricercaAnnotazioneManualeByIdEvento(BigDecimal aKeyEvento) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND EVE_ID_EVENTO=" + aKeyEvento;

		setStatement(lSql);
	}

	public void ricercaAnnotazioneManualeByIdSentenza(BigDecimal aKeySentenza) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND SEN_ID_SENTENZA=" + aKeySentenza;

		setStatement(lSql);
	}

	public void ricercaAnnotazioneManualeByIdSentenzaIdTenore(BigDecimal aKeySentenza,
			BigDecimal aKeyIdTenore) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND SEN_ID_SENTENZA=" + aKeySentenza;
		lSql += " AND TEN_ID_TENORE_SIGE=" + aKeyIdTenore;

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_ANNOTAZIONE_MANUALE, " + "ANNO_ID_ANNOTAZIONE_MANUALE, "
				+ "COD_TIPO_ANNOTAZIONE,DESCR_TIPO_ANN.RV_MEANING DESCR_TIPO, " + "FLAG_PIU_MENO, "
				+ "NUM_ANNI_RECLUSIONE, " + "NUM_MESI_RECLUSIONE, " + "NUM_GIORNI_RECLUSIONE, "
				+ "IMPORTO_MULTA, " + "NUM_ANNI_ARRESTO, " + "NUM_MESI_ARRESTO, " + "NUM_GIORNI_ARRESTO, "
				+ "IMPORTO_AMMENDA, " + "DATA_ARRESTO_DA, " + "DATA_ARRESTO_A, " + "DATA_RECLUSIONE_DA, "
				+ "DATA_RECLUSIONE_A, " + "DATA_RICEZIONE_DOC, " + "MOTIVAZIONI, " + "NOTE_RECLUSIONE, "
				+ "ANNO_GE, " + "NUMERO_GE, " + "ANNO_REGE, " + "NUMERO_REGE, " + "ANNO_MC, " + "NUMERO_MC, "
				+ "ANNO_CDA, " + "NUMERO_CDA, " + "ANNO_CC, " + "NUMERO_CC, " + "ANNO_SIEP, "
				+ "NUMERO_SIEP, " + "COD_TIPO_UFFICIO_SIEP, " + "COD_LUOGO_UFFICIO_SIEP, "
				+ "DATA_ISCRIZIONE_SIEP, " + "COD_FONTE, DECOFONTE.RV_MEANING DESCR_FONTE," + "ANNO_FONTE, "
				+ "NUMERO_FONTE, " + "COD_SOTTONUMERAZIONE, DECOSOTTONUM.RV_MEANING DESCR_SOTTONUM,"
				+ "COMMA, " + "LETTERA, " + "NUMERO, " + "ARTICOLO, "
				+ "COD_CAUSALE_COMPUTO, DECCAUCOMP.RV_MEANING DESCR_CAUCOMP,"
				+ "COD_DPR, DESCR_DPR.RV_MEANING DESCR_DPR," + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " + "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "REA_ID_REATO, " + "EVE_ID_EVENTO, " + "FLAG_VALIDATO, " + "FLAG_CONFORME, "
				+ "FLAG_APP_PROVVISORIA, " + "PEN_RES_ID_PENA_RESIDUA, " + "FUN_ID_FUNGIBILITA, "
				+ "DATA_RICHIESTA, " + "DATA_CC, " + "DATA_GE, " + "ANNO_SENTENZA_SIAP, "
				+ "NUMERO_SENTENZA_SIAP, " + "DATA_SENTENZA_SIAP, " +
				// 07-2015 MEV29 punto 11 - Anno e Numro Procedimento SIGE
				"ANNO_SIGE, NUMERO_SIGE, " + "FLAG_COMPUTABILE, " + "FLG_BENEFICIO_DETRATTO, "
				+ "SEN_ID_SENTENZA, " + "TEN_ID_TENORE_SIGE, " + " FLAG_SEL_QUANTUM ";
		lStatement += " FROM ANNOTAZIONE_MANUALE, CG_REF_CODES DESCR_TIPO_ANN,";
		lStatement += " CG_REF_CODES DESCR_DPR, CG_REF_CODES DECOFONTE, CG_REF_CODES DECOSOTTONUM,";
		lStatement += " CG_REF_CODES DECCAUCOMP";
		lStatement += " WHERE COD_TIPO_ANNOTAZIONE = DESCR_TIPO_ANN.RV_LOW_VALUE";
		lStatement += " AND DESCR_TIPO_ANN.RV_DOMAIN = 'TIPO_ANNOTAZIONE'";
		lStatement += " AND COD_DPR = DESCR_DPR.RV_LOW_VALUE";
		lStatement += " AND DESCR_DPR.RV_DOMAIN = 'DPR'";
		lStatement += " AND DECOFONTE.RV_DOMAIN='FONTE' AND DECOFONTE.RV_LOW_VALUE=COD_FONTE ";
		lStatement += " AND DECOSOTTONUM.RV_DOMAIN='SOTTONUMERAZIONE' AND DECOSOTTONUM.RV_LOW_VALUE=COD_SOTTONUMERAZIONE ";
		lStatement += " AND DECCAUCOMP.RV_DOMAIN='CAUSALE_COMPUTO' AND DECCAUCOMP.RV_LOW_VALUE=COD_CAUSALE_COMPUTO ";

		return lStatement;
	}

	/**
	 * Metodo che carica il contenuto del record nel Model
	 */
	public GenericModel getModel() throws DAOException {
		AnnotazioneManualeModel aModel = new AnnotazioneManualeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdAnnotazioneManuale(getBigDecimal("ID_ANNOTAZIONE_MANUALE"));
		aModel.setCodTipoAnnotazione(getString("COD_TIPO_ANNOTAZIONE"));
		aModel.setDescrTipoAnnotazione(getString("DESCR_TIPO"));
		aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO"));
		aModel.setNumAnniReclusione(getBigDecimal("NUM_ANNI_RECLUSIONE"));
		aModel.setNumMesiReclusione(getBigDecimal("NUM_MESI_RECLUSIONE"));
		aModel.setNumGiorniReclusione(getBigDecimal("NUM_GIORNI_RECLUSIONE"));
		aModel.setImportoMulta(getBigDecimal("IMPORTO_MULTA"));
		aModel.setNumAnniArresto(getBigDecimal("NUM_ANNI_ARRESTO"));
		aModel.setNumMesiArresto(getBigDecimal("NUM_MESI_ARRESTO"));
		aModel.setNumGiorniArresto(getBigDecimal("NUM_GIORNI_ARRESTO"));
		aModel.setImportoAmmenda(getBigDecimal("IMPORTO_AMMENDA"));
		aModel.setDataReclusioneDa(getDate("DATA_RECLUSIONE_DA"));
		aModel.setDataReclusioneA(getDate("DATA_RECLUSIONE_A"));
		aModel.setDataArrestoDa(getDate("DATA_ARRESTO_DA"));
		aModel.setDataArrestoA(getDate("DATA_ARRESTO_A"));
		aModel.setDataRicezioneDoc(getDate("DATA_RICEZIONE_DOC"));
		aModel.setMotivazioni(getString("MOTIVAZIONI"));
		aModel.setNoteReclusione(getString("NOTE_RECLUSIONE"));
		aModel.setAnnoGe(getBigDecimal("ANNO_GE"));
		aModel.setNumeroGe(getString("NUMERO_GE"));
		aModel.setAnnoRege(getBigDecimal("ANNO_REGE"));
		aModel.setNumeroRege(getString("NUMERO_REGE"));
		aModel.setAnnoMc(getBigDecimal("ANNO_MC"));
		aModel.setNumeroMc(getString("NUMERO_MC"));
		aModel.setAnnoCda(getBigDecimal("ANNO_CDA"));
		aModel.setNumeroCda(getString("NUMERO_CDA"));
		aModel.setAnnoCc(getBigDecimal("ANNO_CC"));
		aModel.setNumeroCc(getString("NUMERO_CC"));
		aModel.setAnnoSiep(getBigDecimal("ANNO_SIEP"));
		aModel.setNumeroSiep(getString("NUMERO_SIEP"));
		aModel.setCodTipoUfficioSiep(getString("COD_TIPO_UFFICIO_SIEP"));
		aModel.setCodLuogoUfficioSiep(getString("COD_LUOGO_UFFICIO_SIEP"));
		aModel.setDataIscrizioneSiep(getDate("DATA_ISCRIZIONE_SIEP"));
		aModel.setCodFonte(getString("COD_FONTE"));
		aModel.setDescrFonte(getString("DESCR_FONTE"));
		aModel.setAnnoFonte(getBigDecimal("ANNO_FONTE"));
		aModel.setNumeroFonte(getString("NUMERO_FONTE"));
		aModel.setCodSottonumerazione(getString("COD_SOTTONUMERAZIONE"));
		aModel.setDescrSottonumerazione(getString("DESCR_SOTTONUM"));
		aModel.setComma(getString("COMMA"));
		aModel.setLettera(getString("LETTERA"));
		aModel.setNumero(getString("NUMERO"));
		aModel.setArticolo(getString("ARTICOLO"));
		aModel.setCodCausaleComputo(getString("COD_CAUSALE_COMPUTO"));
		aModel.setDescrCausaleComputo(getString("DESCR_CAUCOMP"));
		aModel.setCodDpr(getString("COD_DPR"));
		aModel.setDescrDpr(getString("DESCR_DPR"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setReaIdReato(getBigDecimal("REA_ID_REATO"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setFlagValidato(getString("FLAG_VALIDATO"));
		aModel.setFlagConforme(getString("FLAG_CONFORME"));
		aModel.setFlagAppProvvisoria(getString("FLAG_APP_PROVVISORIA"));
		aModel.setPenResIdPenaResidua(getBigDecimal("PEN_RES_ID_PENA_RESIDUA"));
		aModel.setFunIdFungibilita(getBigDecimal("FUN_ID_FUNGIBILITA"));
		aModel.setDataRichiesta(getDate("DATA_RICHIESTA"));
		aModel.setDataCC(getDate("DATA_CC"));
		aModel.setDataGE(getDate("DATA_GE"));
		aModel.setAnnoSentenzaSiap(getBigDecimal("ANNO_SENTENZA_SIAP"));
		aModel.setNumeroSentenzaSiap(getString("NUMERO_SENTENZA_SIAP"));
		aModel.setDataSentenzaSiap(getDate("DATA_SENTENZA_SIAP"));
		aModel.setAnnoIdAnnotazioneManuale(getBigDecimal("ANNO_ID_ANNOTAZIONE_MANUALE"));
		aModel.setFlagComputabile(getString("FLAG_COMPUTABILE"));
		aModel.setFlagBeneficioDetratto(getString("FLG_BENEFICIO_DETRATTO"));

		if (findColumn("SEN_ID_SENTENZA"))
			aModel.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));

		if (findColumn("TEN_ID_TENORE_SIGE"))
			aModel.setTenIdTenoreSige(getBigDecimal("TEN_ID_TENORE_SIGE"));

		if (findColumn("FLAG_SEL_QUANTUM"))
			aModel.setFlagSelQuantum(getString("FLAG_SEL_QUANTUM"));

		// 07-2015 MEV29 punto 11 - Anno e Numro Procedimento SIGE
		aModel.setChiaveAnnoSige(getBigDecimal("ANNO_SIGE"));
		aModel.setChiaveNumeroSige(getBigDecimal("NUMERO_SIGE"));

		// MEV_2023-33: aggiunta valorizzazione colonne se presenti
		if (findColumn("DESCR_TIPO_UFFICIO"))
			aModel.setDescrTipoUfficioSiep(getString("DESCR_TIPO_UFFICIO"));
		if (findColumn("DESCR_COMUNE"))
			aModel.setDescrLuogoUfficioSiep(getString("DESCR_COMUNE"));

		return aModel;
	}

	public String setCondizione(AnnotazioneManualeModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_ANNOTAZIONE_MANUALE = " + aKey;
	}

	private String setOrder() {
		String lCondizioni = new String(" ORDER BY DATA_INSERIMENTO");

		return lCondizioni;
	}

	public void ricercaAnnManualeRichiesteGEByIdDecisione(BigDecimal aIdDecisione) throws DAOException {
		String lStatement = "";

		lStatement += " SELECT " + " AM.ID_ANNOTAZIONE_MANUALE, " + " AM.ANNO_ID_ANNOTAZIONE_MANUALE, "
				+ " AM.COD_TIPO_ANNOTAZIONE,null DESCR_TIPO, " + " AM.FLAG_PIU_MENO, "
				+ " AM.NUM_ANNI_RECLUSIONE, AM.NUM_MESI_RECLUSIONE, AM.NUM_GIORNI_RECLUSIONE, AM.IMPORTO_MULTA, "
				+ " AM.NUM_ANNI_ARRESTO, AM.NUM_MESI_ARRESTO, AM.NUM_GIORNI_ARRESTO, AM.IMPORTO_AMMENDA, "
				+ " AM.DATA_ARRESTO_DA, AM.DATA_ARRESTO_A, "
				+ " AM.DATA_RECLUSIONE_DA, AM.DATA_RECLUSIONE_A, "
				+ " AM.DATA_RICEZIONE_DOC, AM.MOTIVAZIONI, AM.NOTE_RECLUSIONE, "
				+ " AM.ANNO_GE, AM.NUMERO_GE, AM.ANNO_REGE, AM.NUMERO_REGE, AM.ANNO_MC, "
				+ " AM.NUMERO_MC, AM.ANNO_CDA, AM.NUMERO_CDA, AM.ANNO_CC, AM.NUMERO_CC, "
				+ " AM.ANNO_SIEP, AM.NUMERO_SIEP, " + " AM.COD_TIPO_UFFICIO_SIEP, "
				+ " AM.COD_LUOGO_UFFICIO_SIEP, " + " AM.DATA_ISCRIZIONE_SIEP, "
				+ " AM.COD_FONTE, null DESCR_FONTE, AM.ANNO_FONTE, AM.NUMERO_FONTE, "
				+ " AM.COD_SOTTONUMERAZIONE, null DESCR_SOTTONUM,"
				+ " AM.COMMA, AM.LETTERA, AM.NUMERO, AM.ARTICOLO, "
				+ " AM.COD_CAUSALE_COMPUTO, null DESCR_CAUCOMP," + " AM.COD_DPR, null DESCR_DPR,"
				+ " AM.COD_OPERATORE_INSERIMENTO, AM.DATA_INSERIMENTO, AM.COD_UFFICIO_INSERIMENTO, "
				+ " AM.COD_OPERATORE_AGGIORNAMENTO, AM.DATA_AGGIORNAMENTO, AM.COD_UFFICIO_AGGIORNAMENTO, "
				+ " AM.FAS_SIE_ID_FASCICOLO_SIEP, " + " AM.REA_ID_REATO, " + " AM.EVE_ID_EVENTO, "
				+ " AM.FLAG_VALIDATO, " + " AM.FLAG_CONFORME, " + " AM.FLAG_APP_PROVVISORIA, "
				+ " AM.PEN_RES_ID_PENA_RESIDUA, " + " AM.FUN_ID_FUNGIBILITA, " + " AM.DATA_RICHIESTA, "
				+ " AM.DATA_CC, " + " AM.DATA_GE, "
				+ " AM.ANNO_SENTENZA_SIAP, AM.NUMERO_SENTENZA_SIAP, AM.DATA_SENTENZA_SIAP, " +
				// 07_2015 MEV29 punto 11 - Anno e Numero procedimento SIGE
				" AM.ANNO_SIGE, AM.NUMERO_SIGE, " + " AM.FLAG_COMPUTABILE, " + " AM.SEN_ID_SENTENZA, "
				+ " AM.TEN_ID_TENORE_SIGE, " + " AM.FLAG_SEL_QUANTUM, " + " AM.FLG_BENEFICIO_DETRATTO ";
		lStatement += " FROM ANNOTAZIONE_MANUALE AM  ";
		lStatement += " WHERE AM.ANNO_ID_ANNOTAZIONE_MANUALE = " + aIdDecisione;

		setStatement(lStatement);
	}

	/**
	 * Ricerca le Annotazioni Manuali con richiesta al GE: COD_TIPO_ANNOTAZIONE = '002' (Indulto) o '003'
	 * (Amnistia) flag_app_provvisoria <> ('R','A') e FLAG_VALIDATO = 'N')
	 *
	 * @param aIdEvento
	 *            identificativo dell'evento
	 * @throws DAOException
	 */
	public void ricercaAnnotazioneManualeIndultoAmnistiaByIdEvento(BigDecimal aIdEvento) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND ( COD_TIPO_ANNOTAZIONE = '003' OR COD_TIPO_ANNOTAZIONE = '002' )";
		lSql += " AND ( FLAG_APP_PROVVISORIA <> 'R' AND FLAG_APP_PROVVISORIA <> 'A' )";
		lSql += " AND FLAG_VALIDATO ='N'";
		lSql += " AND EVE_ID_EVENTO = " + aIdEvento;

		setStatement(lSql);
	}

	/**
	 * Aggiunto metodo di ricerca puntuale
	 *
	 * @author sgioggi
	 * @since MEV_2023-33
	 */
	public void ricercaAnnotazioneManualeByIdEventoIdFascicolo(BigDecimal idEvento,
			BigDecimal idFascicoloSiep) {

		String lStatement = "";

		lStatement += "SELECT AM.ID_ANNOTAZIONE_MANUALE, AM.ANNO_ID_ANNOTAZIONE_MANUALE,"
				+ " AM.COD_TIPO_ANNOTAZIONE, TP.RV_MEANING DESCR_TIPO,"
				+ " AM.FLAG_PIU_MENO, AM.IMPORTO_MULTA, AM.NUM_ANNI_RECLUSIONE,"
				+ " AM.NUM_MESI_RECLUSIONE, AM.NUM_GIORNI_RECLUSIONE,"
				+ " AM.NUM_ANNI_ARRESTO, AM.NUM_MESI_ARRESTO, AM.NUM_GIORNI_ARRESTO, AM.IMPORTO_AMMENDA,"
				+ " AM.DATA_ARRESTO_DA, AM.DATA_ARRESTO_A, AM.DATA_RECLUSIONE_DA,"
				+ " AM.DATA_RECLUSIONE_A, AM.DATA_RICEZIONE_DOC, AM.MOTIVAZIONI,"
				+ " AM.NOTE_RECLUSIONE, AM.ANNO_GE, AM.NUMERO_GE,"
				+ " AM.ANNO_REGE, AM.NUMERO_REGE, AM.ANNO_MC, AM.NUMERO_MC, AM.ANNO_CDA, AM.NUMERO_CDA,"
				+ " AM.ANNO_CC, AM.NUMERO_CC, AM.ANNO_SIEP, AM.NUMERO_SIEP, AM.COD_TIPO_UFFICIO_SIEP,"
				+ " AM.COD_LUOGO_UFFICIO_SIEP, AM.DATA_ISCRIZIONE_SIEP, AM.COD_FONTE, null DESCR_FONTE,"
				+ " AM.ANNO_FONTE, AM.NUMERO_FONTE, AM.COD_SOTTONUMERAZIONE,"
				+ " null DESCR_SOTTONUM, AM.COMMA, AM.LETTERA, AM.NUMERO, AM.ARTICOLO,"
				+ " AM.COD_CAUSALE_COMPUTO, null DESCR_CAUCOMP, AM.COD_DPR, null DESCR_DPR,"
				+ " AM.COD_OPERATORE_INSERIMENTO, AM.DATA_INSERIMENTO,"
				+ " AM.COD_UFFICIO_INSERIMENTO, AM.COD_OPERATORE_AGGIORNAMENTO,"
				+ " AM.DATA_AGGIORNAMENTO, AM.COD_UFFICIO_AGGIORNAMENTO,"
				+ " AM.FAS_SIE_ID_FASCICOLO_SIEP, AM.REA_ID_REATO,"
				+ " AM.EVE_ID_EVENTO, AM.FLAG_VALIDATO, AM.FLAG_CONFORME,"
				+ " AM.FLAG_APP_PROVVISORIA, AM.PEN_RES_ID_PENA_RESIDUA,"
				+ " AM.FUN_ID_FUNGIBILITA, AM.DATA_RICHIESTA, AM.DATA_CC,"
				+ " AM.DATA_GE, AM.ANNO_SENTENZA_SIAP, AM.NUMERO_SENTENZA_SIAP,"
				+ " AM.DATA_SENTENZA_SIAP, AM.ANNO_SIGE, AM.NUMERO_SIGE,"
				+ " AM.FLAG_COMPUTABILE, AM.SEN_ID_SENTENZA, AM.TEN_ID_TENORE_SIGE, AM.FLAG_SEL_QUANTUM,"
				+ " AM.FLG_BENEFICIO_DETRATTO, C.DESCRIZIONE DESCR_COMUNE, TU.RV_MEANING DESCR_TIPO_UFFICIO"
				+ " FROM ANNOTAZIONE_MANUALE AM, CG_REF_CODES TP, CG_REF_CODES TU, COMUNE C"
				+ " WHERE AM.EVE_ID_EVENTO = " + idEvento + " AND AM.FAS_SIE_ID_FASCICOLO_SIEP = "
				+ idFascicoloSiep + " AND AM.COD_TIPO_ANNOTAZIONE = TP.RV_LOW_VALUE"
				+ " AND TP.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'"
				+ " AND AM.COD_TIPO_UFFICIO_SIEP = TU.RV_LOW_VALUE AND TU.RV_DOMAIN = 'TIPO_UFFICIO'"
				+ " AND AM.COD_LUOGO_UFFICIO_SIEP = C.COD_COMUNE";

		setStatement(lStatement);
	}

}