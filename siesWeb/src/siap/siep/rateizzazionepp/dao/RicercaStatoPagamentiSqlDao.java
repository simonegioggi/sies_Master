package siap.siep.rateizzazionepp.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.RicercaStatoPagamentiModel;

/**
 * Classe DAO per la gestione dello stato dei pagamenti
 * 
 * @since MEV_2023-33
 * @version 1.0
 */
public class RicercaStatoPagamentiSqlDao extends SIAPSqlDAO {

	// private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	public RicercaStatoPagamentiSqlDao(Connection con) {
		super(con);
	}

	public GenericModel getModel() throws DAOException {

		RicercaStatoPagamentiModel lModel = new RicercaStatoPagamentiModel();

		// Inserire le opportune set delle descrizioni!
		lModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		// lModel.setChiaveUfficio (getString(""));
		lModel.setDataIscrizione(getDate("DATA_ISCRIZIONE"));

		lModel.setCognome(getString("NOME"));
		lModel.setNome(getString("COGNOME"));

		lModel.setTipoRateizzazione(getString("TIPO_RATEIZZAZIONE"));
		lModel.setImportoDaPagare(getBigDecimal("importoDaPagare"));
		lModel.setImportoPagato(getBigDecimal("importoPagato"));
		lModel.setDataUltimaScadenza(getDate("dataUltimaScadenza"));

		return lModel;
	}

	// MEV_2023-33
	public void getCountRicercaFascicoliPerStatoPagamento(FascicoloSiepModel aFasMod, String aTipoRicera)
			throws DAOException {

		String lSql = getSqlRicercaFascicoliPerStatoPagamento(aFasMod, aTipoRicera);

		String lStatement = "SELECT COUNT(*) HowManyRecords FROM ( " + lSql + " ) ";

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	public void ricercaFascicoliPerStatoPagamento(FascicoloSiepModel aFasMod, int aPage, String aTipoRicera)
			throws DAOException {

		String lSql = getSqlRicercaFascicoliPerStatoPagamento(aFasMod, aTipoRicera);

		lSql += "  ORDER BY FASCICOLO_SIEP.CHIAVE_ANNO,FASCICOLO_SIEP.CHIAVE_PROGR  ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	private String getSqlRicercaFascicoliPerStatoPagamento(FascicoloSiepModel aFasMod, String aTipoRicera) {

		String lSql = "";

		lSql += " SELECT FASCICOLO_SIEP.ID_FASCICOLO_SIEP ";
		lSql += "      , FASCICOLO_SIEP.CHIAVE_ANNO, FASCICOLO_SIEP.CHIAVE_PROGR, FASCICOLO_SIEP.DATA_ISCRIZIONE ";
		lSql += "      , SOGGETTO.NOME, SOGGETTO.COGNOME ";
		lSql += "      , rateizzazione.TIPO_RATEIZZAZIONE ";
		lSql += "      , rateizzazione.importoDaPagare";
		lSql += "      , statoPagamenti.importoPagato";
		lSql += "      , ultimaScadenza.dataUltimaScadenza ";
		lSql += "   FROM FASCICOLO_SIEP, SOGGETTO ";
		lSql += "      , (SELECT SUM(IMPORTO_RATA) importoDaPagare, FAS_SIE_ID_FASCICOLO_SIEP, TIPO_RATEIZZAZIONE ";
		lSql += "           FROM RATEIZZAZIONE_PP ";
		lSql += "       GROUP BY FAS_SIE_ID_FASCICOLO_SIEP, TIPO_RATEIZZAZIONE) rateizzazione ";
		lSql += "      , (SELECT NVL(SUM(IMPORTO_PAGATO),0) importoPagato, FAS_SIE_ID_FASCICOLO_SIEP ";
		lSql += "           FROM BOLLETTINO_PAGOPA ";
		lSql += "       GROUP BY FAS_SIE_ID_FASCICOLO_SIEP) statoPagamenti ";
		lSql += "      , (SELECT MAX(DATA_SCADENZA) dataUltimaScadenza, FAS_SIE_ID_FASCICOLO_SIEP ";
		lSql += "           FROM BOLLETTINO_PAGOPA ";
		lSql += "       GROUP BY FAS_SIE_ID_FASCICOLO_SIEP ) ultimaScadenza ";
		lSql += "  WHERE 1=1 ";
		lSql += "    AND FASCICOLO_SIEP.SOG_ID_SOGGETTO = SOGGETTO.ID_SOGGETTO ";
		lSql += "    AND rateizzazione.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIEP.ID_FASCICOLO_SIEP ";
		lSql += "    AND statoPagamenti.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIEP.ID_FASCICOLO_SIEP ";
		lSql += "    AND ultimaScadenza.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIEP.ID_FASCICOLO_SIEP ";

		// Ulteriori condizioni
		lSql += "    AND FASCICOLO_SIEP.CHIAVE_UFFICIO = '" + aFasMod.getChiaveUfficio() + "' ";

		if (aFasMod.getChiaveAnnoIniziale() != null) {
			// Il vincolo sul progressivo (se presente) vale solo per l'anno indicato, per i successivi vanno
			// presi tutti i progressivi
			lSql += " AND (  (     FASCICOLO_SIEP.CHIAVE_ANNO = " + aFasMod.getChiaveAnnoIniziale();
			if (aFasMod.getChiaveProgrIniziale() != null)
				lSql += "          AND FASCICOLO_SIEP.CHIAVE_PROGR >= " + aFasMod.getChiaveProgrIniziale();
			lSql += "        ) ";
			lSql += "      OR FASCICOLO_SIEP.CHIAVE_ANNO > " + aFasMod.getChiaveAnnoIniziale();
			lSql += "     ) ";
		}

		if (aFasMod.getChiaveAnnoFinale() != null) {
			// Il vincolo sul progressivo (se presente) vale solo per l'anno indicato, per i precedenti vanno
			// presi tutti i progressivi
			lSql += " AND (  (     FASCICOLO_SIEP.CHIAVE_ANNO = " + aFasMod.getChiaveAnnoFinale();
			if (aFasMod.getChiaveProgrFinale() != null)
				lSql += "          AND FASCICOLO_SIEP.CHIAVE_PROGR <= " + aFasMod.getChiaveProgrFinale();
			lSql += "        ) ";
			lSql += "      OR FASCICOLO_SIEP.CHIAVE_ANNO < " + aFasMod.getChiaveAnnoFinale();
			lSql += "     ) ";
		}

		lSql += " AND FASCICOLO_SIEP.COD_STATO_FASCICOLO <> '01' "; // Si escludono gli archiviati

		if (aFasMod.getDataIscrizioneIniziale() != null)
			lSql += "    AND FASCICOLO_SIEP.DATA_ISCRIZIONE >= TO_DATE ('"
					+ DateUtils.getDateToString(aFasMod.getDataIscrizioneIniziale(), "dd/MM/yyyy")
					+ "','DD/MM/YYYY')";
		if (aFasMod.getDataIscrizioneFinale() != null)
			lSql += "    AND FASCICOLO_SIEP.DATA_ISCRIZIONE <= TO_DATE ('"
					+ DateUtils.getDateToString(aFasMod.getDataIscrizioneFinale(), "dd/MM/yyyy")
					+ "','DD/MM/YYYY')";

		// String codTipoEvento = "";
		// String codTipoProvvedimento = "";
		// String codMotivo = "";
		if (aTipoRicera.equals(ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_INTERAMENTE_PAGATO)) {
			lSql += " AND importoPagato = importoDaPagare ";
			// TODO Aggiungere filtro sugli eventi
			// codMotivo = "'0045'"; // codice di test
		} else if (aTipoRicera
				.equals(ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_RETEIZZATO_NON_PAGATO)) {
			lSql += " AND rateizzazione.TIPO_RATEIZZAZIONE = 'R' ";
			lSql += " AND importoPagato < importoDaPagare ";
			// TODO Aggiungere filtro sugli eventi
			// codMotivo = "'0045'"; // codice di test
		} else if (aTipoRicera
				.equals(ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_UNICA_RATA_NON_PAGATO)) {
			lSql += " AND rateizzazione.TIPO_RATEIZZAZIONE = 'U' ";
			lSql += " AND importoPagato < importoDaPagare ";
			// TODO Aggiungere filtro sugli eventi
			// codMotivo = "'0045'"; // codice di test
		}

		// TODO Da decommentare quando si avranni i codici degli evento su cui applicare il filtro
		/*
		 * lSql += " AND NOT EXISTS (SELECT 1 "; lSql += "                   FROM EVENTO "; lSql +=
		 * "                  WHERE EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIEP.ID_FASCICOLO_SIEP ";
		 * //lSql += "                    AND EVENTO.COD_TIPO_EVENTO = '' "; //lSql +=
		 * "                    AND EVENTO.COD_TIPO_PROVVEDIMENTO = ''"; lSql +=
		 * "                    AND EVENTO.COD_MOTIVO IN ("+codMotivo+")"; lSql +=
		 * "                    AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' "; lSql += " ) ";
		 */

		return lSql;
	}

}