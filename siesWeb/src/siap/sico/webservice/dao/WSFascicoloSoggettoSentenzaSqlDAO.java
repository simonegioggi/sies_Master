package siap.sico.webservice.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;

public class WSFascicoloSoggettoSentenzaSqlDAO extends SIAPSqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public WSFascicoloSoggettoSentenzaSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void RicercaTitoloEsecutivoTrasferito(Date dataRicercaInizio, Date dataRicercaFine,
			String lDestinazione, String lStatoFascicolo, String lCodUfficioUtenteConnesso, int aPage) {

		String lSql = "SELECT * FROM (SELECT INNER.*, ROWNUM rn FROM ";

		// ----> Query Principale
		lSql += "(SELECT DATA_TRASMISSIONE, fas.CHIAVE_ANNO,fas.CHIAVE_PROGR ,ANNO_SENTENZA,NUMERO_SENTENZA,DATA_PROVVEDIMENTO,COGNOME,NOME,COD_STATO_FASCICOLO,RV_MEANING DESCR_STATO_FASCICOLO FROM FASCICOLO_SIEP fas, SOGGETTO sog, SENTENZA sen, CG_REF_CODES, TRASMISSIONI tra WHERE KEY_PROVV_NSC is not null";

		if (dataRicercaInizio != null) {
			lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') >= TO_DATE('"
					+ DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy") + "', 'dd/MM/yyyy')";
		}

		if (dataRicercaFine != null) {
			lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') <= TO_DATE('"
					+ DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy") + "', 'dd/MM/yyyy')";
		}

		if (lDestinazione.equals("1")) // da NSC a SIES
		{
			lSql += " and SUBSTR(fas.COD_OPERATORE_INSERIMENTO,1,4) = 'nsc-'";
		} else {
			lSql += " and SUBSTR(fas.COD_OPERATORE_INSERIMENTO,1,4) != 'nsc-'";
			lSql += " and TIPO_OPERAZIONE = 'INSERT'";
		}

		if (!lStatoFascicolo.equals("") && !lStatoFascicolo.equals("-")) {
			if (lStatoFascicolo.equals("02")) // Iscritto
			{
				lSql += " and COD_STATO_FASCICOLO = '" + lStatoFascicolo + "'";
			} else {
				lSql += " and COD_STATO_FASCICOLO IN ('01','03')";
			}
		}

		lSql += " AND ID_SOGGETTO =  SOG_ID_SOGGETTO";
		lSql += " AND ID_SENTENZA =  SEN_ID_SENTENZA";

		lSql += " AND KEY_PROVV_NSC = CHIAVE_NSC_PROV";
		lSql += " AND KEY_SOGG_NSC = CHIAVE_NSC_SOGG";

		lSql += " AND tra.COD_UFFICIO_INSERIMENTO = '" + lCodUfficioUtenteConnesso + "' ";

		lSql += " AND (RV_DOMAIN =   'STATO_FASCICOLO'  AND RV_LOW_VALUE = fas.COD_STATO_FASCICOLO)";

		lSql += " ORDER BY DATA_TRASMISSIONE DESC)";

		// ----> Fine Query Principale
		lSql += "INNER)";

		lSql += " WHERE rn BETWEEN " + +((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + " AND "
				+ (aPage) * IWebConstants.RESULT_PER_PAGE;

		// Imposta lo statement da eseguire
		setStatement(lSql);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("lSql = " + lSql);
	}

	public void getCountRicercaTitoloEsecutivoTrasferito(Date dataRicercaInizio, Date dataRicercaFine,
			String lDestinazione, String lStatoFascicolo, String lCodUfficioUtenteConnesso)
			throws DAOException {
		String lSql = "";

		// lSql =
		// "SELECT count(*) HowManyRecords FROM FASCICOLO_SIEP fas, SOGGETTO sog, SENTENZA sen, CG_REF_CODES,
		// TRASMISSIONI tra WHERE KEY_PROVV_NSC is not null";
		lSql = "SELECT count(*) HowManyRecords FROM FASCICOLO_SIEP fas, SOGGETTO sog,  TRASMISSIONI tra WHERE KEY_PROVV_NSC is not null";

		if (dataRicercaInizio != null) {
			lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') >= TO_DATE('"
					+ DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy") + "', 'dd/MM/yyyy')";
		}

		if (dataRicercaFine != null) {
			lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') <= TO_DATE('"
					+ DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy") + "', 'dd/MM/yyyy')";
		}

		if (lDestinazione.equals("1")) // da NSC a SIES
		{
			lSql += " and SUBSTR(fas.COD_OPERATORE_INSERIMENTO,1,4) = 'nsc-'";
		} else {
			lSql += " and SUBSTR(fas.COD_OPERATORE_INSERIMENTO,1,4) != 'nsc-'";
			lSql += " and TIPO_OPERAZIONE = 'INSERT'";
		}

		if (!lStatoFascicolo.equals("") && !lStatoFascicolo.equals("-")) {
			if (lStatoFascicolo.equals("02")) // Iscritto
			{
				lSql += " and COD_STATO_FASCICOLO = '" + lStatoFascicolo + "'";
			} else {
				lSql += " and COD_STATO_FASCICOLO IN ('01','03')";
			}
		}

		// lSql +=
		// " AND (RV_DOMAIN = 'STATO_FASCICOLO' AND RV_LOW_VALUE = fas.COD_STATO_FASCICOLO)";
		lSql += " AND ID_SOGGETTO =  SOG_ID_SOGGETTO";
		// lSql += " AND ID_SENTENZA = SEN_ID_SENTENZA";
		lSql += " AND KEY_PROVV_NSC = CHIAVE_NSC_PROV";
		lSql += " AND KEY_SOGG_NSC = CHIAVE_NSC_SOGG";

		lSql += " AND tra.COD_UFFICIO_INSERIMENTO = '" + lCodUfficioUtenteConnesso + "' ";

		// lSql += " ORDER BY DATA_TRASMISSIONE DESC";

		setStatement(lSql);
	}

	/*
	 * --------------------------------------------------------------------------
	 * --------------------------------------------- public void RicercaTitoloEsecutivoTrasferito(Date
	 * dataRicercaInizio, Date dataRicercaFine, String lDestinazione, String lStatoFascicolo, String
	 * lCodUfficioUtenteConnesso, int aPage) {
	 *
	 * String lSql = "SELECT * FROM (SELECT INNER.*, ROWNUM rn FROM ";
	 *
	 * //----> Query Principale
	 *
	 * if (lDestinazione.equals("1")) // da NSC a SIES { lSql +=
	 * "(SELECT fas.CHIAVE_ANNO,fas.CHIAVE_PROGR ,ANNO_SENTENZA,NUMERO_SENTENZA,DATA_PROVVEDIMENTO,COGNOME,NOME,COD_STATO_FASCICOLO,RV_MEANING DESCR_STATO_FASCICOLO FROM FASCICOLO_SIEP fas, SOGGETTO sog, SENTENZA sen, CG_REF_CODES WHERE KEY_PROVV_NSC is not null"
	 * ; } else { lSql +=
	 * "(SELECT fas.CHIAVE_ANNO,fas.CHIAVE_PROGR ,ANNO_SENTENZA,NUMERO_SENTENZA,DATA_PROVVEDIMENTO,COGNOME,NOME,COD_STATO_FASCICOLO,RV_MEANING DESCR_STATO_FASCICOLO FROM FASCICOLO_SIEP fas, SOGGETTO sog, SENTENZA sen, CG_REF_CODES, TRASMISSIONI tra WHERE KEY_PROVV_NSC is not null"
	 * ; }
	 *
	 *
	 * if (dataRicercaInizio != null ) {
	 *
	 * if (lDestinazione.equals("1")) // da NSC a SIES { lSql +=
	 * " and TRUNC(fas.DATA_INSERIMENTO, 'dd') >= TO_DATE('" + DateUtils.getDateToString(dataRicercaInizio,
	 * "dd/MM/yyyy") + "', 'dd/MM/yyyy')"; } else { lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') >= TO_DATE('"
	 * + DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy") + "', 'dd/MM/yyyy')"; } }
	 *
	 * if (dataRicercaFine != null ) { if (lDestinazione.equals("1")) // da NSC a SIES { lSql +=
	 * " and TRUNC(fas.DATA_INSERIMENTO, 'dd') <= TO_DATE('" + DateUtils.getDateToString(dataRicercaFine,
	 * "dd/MM/yyyy") + "', 'dd/MM/yyyy')"; } else { lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') <= TO_DATE('"
	 * + DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy") + "', 'dd/MM/yyyy')"; } }
	 *
	 * if (lDestinazione.equals("1")) // da NSC a SIES { lSql +=
	 * " and SUBSTR(fas.COD_OPERATORE_INSERIMENTO,1,4) = 'nsc-'"; } else { lSql +=
	 * " and SUBSTR(fas.COD_OPERATORE_INSERIMENTO,1,4) != 'nsc-'"; lSql += " and TIPO_OPERAZIONE = 'INSERT'";
	 * }
	 *
	 * if (!lStatoFascicolo.equals("") && !lStatoFascicolo.equals("-")) { if (lStatoFascicolo.equals("02")) //
	 * Iscritto { lSql += " and COD_STATO_FASCICOLO = '" + lStatoFascicolo + "'"; } else { lSql +=
	 * " and COD_STATO_FASCICOLO IN ('01','03')" ; } }
	 *
	 * lSql += " AND ID_SOGGETTO =  SOG_ID_SOGGETTO"; lSql += " AND ID_SENTENZA =  SEN_ID_SENTENZA";
	 *
	 * if (lDestinazione.equals("2")) // da SIES a NSC { lSql += " AND KEY_PROVV_NSC = CHIAVE_NSC_PROV"; lSql
	 * += " AND KEY_SOGG_NSC = CHIAVE_NSC_SOGG"; }
	 *
	 * lSql += " AND tra.COD_UFFICIO_INSERIMENTO = '" + lCodUfficioUtenteConnesso + "' ";
	 *
	 * lSql += " AND (RV_DOMAIN =   'STATO_FASCICOLO'  AND RV_LOW_VALUE = fas.COD_STATO_FASCICOLO)" ;
	 *
	 * if (lDestinazione.equals("1")) // da NSC a SIES { lSql += " ORDER BY fas.DATA_INSERIMENTO DESC)"; }
	 * else { lSql += " ORDER BY DATA_TRASMISSIONE DESC)"; } //----> Query Principale lSql += "INNER)";
	 *
	 * lSql += " WHERE rn BETWEEN " + +((aPage-1)*IWebConstants.RESULT_PER_PAGE+1)+ " AND "+
	 * (aPage)*IWebConstants.RESULT_PER_PAGE;
	 *
	 * // Imposta lo statement da eseguire setStatement(lSql);
	 *
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.info("lSql = "+lSql); }
	 *
	 *
	 *
	 * public void getCountRicercaTitoloEsecutivoTrasferito(Date dataRicercaInizio, Date dataRicercaFine,
	 * String lDestinazione, String lStatoFascicolo, String lCodUfficioUtenteConnesso) throws DAOException {
	 * String lSql="";
	 *
	 * if (lDestinazione.equals("1")) // da NSC a SIES { lSql =
	 * "SELECT count(*) HowManyRecords FROM FASCICOLO_SIEP fas, SOGGETTO sog, SENTENZA sen, CG_REF_CODES WHERE KEY_PROVV_NSC is not null"
	 * ; } else { lSql =
	 * "SELECT count(*) HowManyRecords FROM FASCICOLO_SIEP fas, SOGGETTO sog, SENTENZA sen, CG_REF_CODES, TRASMISSIONI tra WHERE KEY_PROVV_NSC is not null"
	 * ; }
	 *
	 * if (dataRicercaInizio != null ) {
	 *
	 * if (lDestinazione.equals("1")) // da NSC a SIES { lSql +=
	 * " and TRUNC(fas.DATA_INSERIMENTO, 'dd') >= TO_DATE('" + DateUtils.getDateToString(dataRicercaInizio,
	 * "dd/MM/yyyy") + "', 'dd/MM/yyyy')"; } else { lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') >= TO_DATE('"
	 * + DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy") + "', 'dd/MM/yyyy')"; } }
	 *
	 * if (dataRicercaFine != null ) { if (lDestinazione.equals("1")) // da NSC a SIES { lSql +=
	 * " and TRUNC(fas.DATA_INSERIMENTO, 'dd') <= TO_DATE('" + DateUtils.getDateToString(dataRicercaFine,
	 * "dd/MM/yyyy") + "', 'dd/MM/yyyy')"; } else { lSql += " and TRUNC(DATA_TRASMISSIONE, 'dd') <= TO_DATE('"
	 * + DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy") + "', 'dd/MM/yyyy')"; } }
	 *
	 * if (lDestinazione.equals("1")) // da NSC a SIES { lSql +=
	 * " and SUBSTR(fas.COD_OPERATORE_INSERIMENTO,1,4) = 'nsc-'"; } else { lSql +=
	 * " and SUBSTR(fas.COD_OPERATORE_INSERIMENTO,1,4) != 'nsc-'"; lSql += " and TIPO_OPERAZIONE = 'INSERT'";
	 * }
	 *
	 * if (!lStatoFascicolo.equals("") && !lStatoFascicolo.equals("-")) { if (lStatoFascicolo.equals("02")) //
	 * Iscritto { lSql += " and COD_STATO_FASCICOLO = '" + lStatoFascicolo + "'"; } else { lSql +=
	 * " and COD_STATO_FASCICOLO IN ('01','03')" ; } }
	 *
	 * lSql += " AND (RV_DOMAIN =   'STATO_FASCICOLO'  AND RV_LOW_VALUE = fas.COD_STATO_FASCICOLO)" ; lSql +=
	 * " AND ID_SOGGETTO =  SOG_ID_SOGGETTO"; lSql += " AND ID_SENTENZA =  SEN_ID_SENTENZA"; if
	 * (lDestinazione.equals("2")) // da SIES a NSC { lSql += " AND KEY_PROVV_NSC = CHIAVE_NSC_PROV"; lSql +=
	 * " AND KEY_SOGG_NSC = CHIAVE_NSC_SOGG"; }
	 *
	 * lSql += " AND tra.COD_UFFICIO_INSERIMENTO = '" + lCodUfficioUtenteConnesso + "' ";
	 *
	 * if (lDestinazione.equals("1")) // da NSC a SIES { lSql += " ORDER BY fas.DATA_INSERIMENTO DESC"; } else
	 * { lSql += " ORDER BY DATA_TRASMISSIONE DESC"; }
	 *
	 *
	 * setStatement(lSql); } ----------------------------------------------------
	 * ---------------------------------------------
	 */

	public GenericModel getModel() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		// lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP") );
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		// lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO") );
		// lFascicolo.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO") );
		// lFascicolo.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO") );
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setDescrTipoUfficio(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getTipoUfficio(), lFascicolo.getChiaveUfficio()));
		lFascicolo.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lFascicolo.setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO"));
		lFascicolo.setDataIscrizione(getDate("DATA_TRASMISSIONE"));
		// lFascicolo.setDataArchiviazione(getDate("DATA_ARCHIVIAZIONE") );
		// lFascicolo.setCodMotivoArchiviazione(getString("COD_MOTIVO_ARCHIVIAZIONE")
		// );
		// lFascicolo.setDescrMotivoArchiviazione(getString("DESCR_MOTIVO_ARCHIVIAZIONE")
		// );
		/*
		 * lFascicolo.setLetteraFascicolo(getString("LETTERA_FASCICOLO") );
		 * lFascicolo.setAnnoFascicoloUnione(getString("ANNO_FASCICOLO_UNIONE") );
		 * lFascicolo.setNumFascicoloUnione(getString("NUM_FASCICOLO_UNIONE") );
		 * lFascicolo.setDataUnione(getDate("DATA_UNIONE") ); lFascicolo.setNote(getString("NOTE_FASCICOLO")
		 * ); lFascicolo.setCodTipoPosLibero(getString("COD_TIPO_POS_LIBERO") );
		 * lFascicolo.setDescrTipoPosLibero(getString("DESCR_TIPO_POS_LIBERO") );
		 * lFascicolo.setFlagValidato(getString("FLAG_VALIDATO") ); lFascicolo
		 * .setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		 * lFascicolo.setDataInserimento(getDate("DATA_INSERIMENTO") ); lFascicolo
		 * .setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") ); lFascicolo
		 * .setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO" ) );
		 * lFascicolo.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") ); lFascicolo
		 * .setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
		 */
		/*
		 * lFascicolo.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );
		 * lFascicolo.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA") );
		 */
		/*
		 * lFascicolo.setFasSieIdFascicoloSiep(getBigDecimal( "FAS_SIE_ID_FASCICOLO_SIEP") );
		 * lFascicolo.setDataAltraCausa(getDate("DATA_ALTRA_CAUSA") );
		 * lFascicolo.setAnnoAltraCausa(getBigDecimal("ANNO_ALTRA_CAUSA") );
		 * lFascicolo.setNumeroAltraCausa(getString("NUMERO_ALTRA_CAUSA") );
		 * lFascicolo.setCodLuogoAltraCausa(getString("COD_LUOGO_ALTRA_CAUSA") );
		 * lFascicolo.setDescrLuogoAltraCausa(getString("DESCR_LUOGO_ALTRA_CAUSA" ) );
		 */

		SoggettoModel lSoggetto = new SoggettoModel();

		// lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
		// lSoggetto.setCodFiscale(getString("COD_FISCALE") );
		/*
		 * lSoggetto.setCodCs(getString("COD_CS") ); lSoggetto.setCodAfis(getString("COD_AFIS") );
		 */
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		/*
		 * lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		 * lSoggetto.setDataNascita(getDate("DATA_NASCITA") );
		 * lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		 * lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		 * lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA") );
		 * lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA") );
		 * lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA" ) );
		 * lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		 * lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") ); lSoggetto
		 * .setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );
		 * lSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * lSoggetto.setPaternita(getString("PATERNITA") );
		 * lSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
		 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 */
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO")
		// );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO")
		// );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO")
		// );

		SentenzaModel lSentenza = new SentenzaModel();

		// lSentenza.setIdSentenza(getBigDecimal("ID_SENTENZA") );
		// lSentenza.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO")
		// );
		// / STUB 29/11/2004
		// lSentenza.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO")
		// );
		lSentenza.setDescrTipoProvvedimento(
				DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoProvvedimenti(),
						lSentenza.getCodTipoProvvedimento()));
		// lSentenza.setAnnoRegePm(getBigDecimal("ANNO_REGE_PM") );
		// lSentenza.setNumeroRegePm(getString("NUMERO_REGE_PM") );
		// lSentenza.setDataArrivoAtto(getDate("DATA_ARRIVO_ATTO") );
		lSentenza.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		// lSentenza.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE")
		// );
		// / STUB 29/11/2004
		// lSentenza.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE")
		// );
		lSentenza.setDescrTipoAutoritaEmittente(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getTipoUfficio(), lSentenza.getCodTipoAutoritaEmittente()));
		// lSentenza.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE") );
		// lSentenza.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE")
		// );
		// /
		// lSentenza.setNumSezioneAutoritaEmittente(getString("NUM_SEZIONE_AUTORITA_EMITTENTE")
		// );
		lSentenza.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA"));
		lSentenza.setNumeroSentenza(getString("NUMERO_SENTENZA"));
		// lSentenza.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA") );
		// lSentenza.setFlagSentenzaApplicazPena(getString("FLAG_SENTENZA_APPLICAZ_PENA")
		// );
		// lSentenza.setCodTipoProvvRif(getString("COD_TIPO_PROVV_RIF") );
		// lSentenza.setDescrTipoProvvRif(getString("DESCR_TIPO_PROVVEDIMENTO_RIF")
		// );
		/*
		 * lSentenza.setDataProvvRif(getDate("DATA_PROVV_RIF") ); lSentenza.setCodTipoAutoritaProvvRif
		 * (getString("COD_TIPO_AUTORITA_PROVV_RIF") ); lSentenza.setDescrTipoAutoritaProvvRif
		 * (getString("DESCR_TIPO_AUTORITA_PROVV_RIF") );
		 * lSentenza.setAnnoProvvRif(getBigDecimal("ANNO_PROVV_RIF") );
		 * lSentenza.setNumeroProvvRif(getString("NUMERO_PROVV_RIF") );
		 * lSentenza.setCodLuogoProvvRif(getString("COD_LUOGO_PROVV_RIF") );
		 * lSentenza.setDescrLuogoProvvRif(getString("DESCR_LUOGO_PROVV_RIF") ); lSentenza
		 * .setNumSezioneAutoritaProvvRif(getString("NUM_SEZIONE_AUTORITA_PROVV_RIF" ) );
		 * lSentenza.setCodTipoDecisioneCassazione(getString( "COD_TIPO_DECISIONE_CASSAZIONE") );
		 * lSentenza.setDescrTipoDecisioneCassazione (getString("DESCR_TIPO_DECISIONE_CASS") );
		 * lSentenza.setNote1DecisioneCassazione (getString("NOTE1_DECISIONE_CASSAZIONE") );
		 * lSentenza.setNote2DecisioneCassazione (getString("NOTE2_DECISIONE_CASSAZIONE") );
		 * lSentenza.setAnnoSentenzaCassazione (getBigDecimal("ANNO_SENTENZA_CASSAZIONE") );
		 * lSentenza.setNumeroSentenzaCassazione (getString("NUMERO_SENTENZA_CASSAZIONE") );
		 * lSentenza.setAnnoRaccoltaGenerale (getBigDecimal("ANNO_RACCOLTA_GENERALE") );
		 * lSentenza.setNumeroRaccoltaGenerale (getString("NUMERO_RACCOLTA_GENERALE") );
		 * lSentenza.setFlagAltreSentenze(getString("FLAG_ALTRE_SENTENZE") );
		 * lSentenza.setDescrAltreSentenze(getString("DESCR_ALTRE_SENTENZE") );
		 * lSentenza.setAnnoRegistro35(getBigDecimal("ANNO_REGISTRO_35") );
		 * lSentenza.setNumRegistro35(getString("NUM_REGISTRO_35") );
		 * lSentenza.setNote(getString("NOTE_SENTENZA") ); lSentenza.setDescrNumCampionePenale
		 * (getString("DESCR_NUM_CAMPIONE_PENALE") ); lSentenza.setAnnoRegeGip(getBigDecimal("ANNO_REGE_GIP")
		 * ); lSentenza.setNumeroRegeGip(getString("NUMERO_REGE_GIP") );
		 * lSentenza.setAnnoRegeDib(getBigDecimal("ANNO_REGE_DIB") );
		 * lSentenza.setNumeroRegeDib(getString("NUMERO_REGE_DIB") );
		 * lSentenza.setAnnoRegeCas(getBigDecimal("ANNO_REGE_CAS") );
		 * lSentenza.setNumeroRegeCas(getString("NUMERO_REGE_CAS") );
		 * lSentenza.setAnnoRegeCap(getBigDecimal("ANNO_REGE_CAP") );
		 * lSentenza.setNumeroRegeCap(getString("NUMERO_REGE_CAP") );
		 * lSentenza.setAnnoRegeCasap(getBigDecimal("ANNO_REGE_CASAP") );
		 * lSentenza.setNumeroRegeCasap(getString("NUMERO_REGE_CASAP") ); // MEV_66: aggiunte quattro nuove
		 * proprietà lSentenza.setAnnoRegeGup(getBigDecimal("ANNO_REGE_GUP"));
		 * lSentenza.setNumeroRegeGup(getString("NUMERO_REGE_GUP") );
		 * lSentenza.setAnnoRegeCapsm(getBigDecimal("ANNO_REGE_CAPSM"));
		 * lSentenza.setNumeroRegeCapsm(getString("NUMERO_REGE_CAPSM") );
		 */

		// lSentenza.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		// lSentenza.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSentenza.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO")
		// );
		// lSentenza.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lSentenza.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSentenza.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO")
		// );

		lFascicolo.setSoggetto(lSoggetto);
		lFascicolo.setSentenza(lSentenza);

		return lFascicolo;
	}

	// MEV 16 CUMULO: per il cumulo eseguo un'altra count
	public void getCountCercaFascSogg(BigDecimal lKeyProvvNsc, BigDecimal lKeySoggNsc, boolean isForCumulo)
			throws DAOException {

		String lSql = "";
		if (isForCumulo) {
			String keyProvvNsc = Utils.isPresent(lKeyProvvNsc) ? lKeyProvvNsc.toString() : "";
			String keySoggNsc = Utils.isPresent(lKeySoggNsc) ? lKeySoggNsc.toString() : "";
			BigDecimal bm = new BigDecimal(keyProvvNsc + keySoggNsc);
			lSql = "SELECT count(*) HowManyRecords FROM titolo_cumulato t";
			lSql += " WHERE t.MESS_ID_MESSAGGIO = " + bm;
		} else {
			lSql = "SELECT count(*) HowManyRecords FROM FASCICOLO_SIEP fas, SOGGETTO sog ";
			lSql += " WHERE sog.id_soggetto = fas.sog_id_soggetto";
			lSql += " AND KEY_PROVV_NSC = " + lKeyProvvNsc;
			lSql += " AND KEY_SOGG_NSC  = " + lKeySoggNsc;
		}

		setStatement(lSql);

	}

	public void RicercaAnnoNumeroFas(BigDecimal lKeyProvvNsc, BigDecimal lKeySoggNsc) throws DAOException {
		String lSql = "";
		lSql = "SELECT CHIAVE_ANNO,CHIAVE_PROGR FROM FASCICOLO_SIEP fas, SOGGETTO sog ";
		lSql += " WHERE sog.id_soggetto = fas.sog_id_soggetto";
		lSql += " AND KEY_PROVV_NSC = " + lKeyProvvNsc;
		lSql += " AND KEY_SOGG_NSC  = " + lKeySoggNsc;

		setStatement(lSql);

	}

	public FascicoloSiepModel getRicercaAnnoNumeroFas() throws DAOException {

		FascicoloSiepModel lFascicoloSiepModel = new FascicoloSiepModel();
		// Campi prelevati dal DB e impostati sulle strutture
		// lUfficioModel.setCodUfficio(getString("COD_UFFICIO"));
		lFascicoloSiepModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicoloSiepModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));

		return lFascicoloSiepModel;
	}

	/**
	 * MEV 16: aggiunto metodo per gestione fascicoli coinvolti in un cumulo
	 *
	 * @param lKeyProvvNsc
	 * @param lKeySoggNsc
	 * @param idFascicoloSiep
	 * @throws DAOException
	 */
	public void getCountCercaFascSoggCumulo(BigDecimal lKeyProvvNsc, BigDecimal lKeySoggNsc,
			String idFascicoloSiep) throws DAOException {

		String lSql = "";
		lSql = "SELECT count(*) HowManyRecords FROM FASCICOLO_SIEP fas, SOGGETTO sog, CUMULO c";
		lSql += " WHERE sog.id_soggetto = fas.sog_id_soggetto";
		lSql += " AND KEY_PROVV_NSC = " + lKeyProvvNsc;
		lSql += " AND KEY_SOGG_NSC  = " + lKeySoggNsc;
		lSql += " and fas.id_fascicolo_siep = c.fas_sie_id_fascicolo_siep";
		lSql += " and c.fas_sie_id_fascicolo_siep = " + idFascicoloSiep;
		setStatement(lSql);
	}
	// FINE MEV 16

}