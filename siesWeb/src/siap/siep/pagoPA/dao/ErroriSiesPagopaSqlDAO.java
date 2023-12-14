package siap.siep.pagoPA.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.siep.pagoPA.model.ErroriSiesPagopaModel;

public class ErroriSiesPagopaSqlDAO extends SIAPSqlDAO {

	public ErroriSiesPagopaSqlDAO(Connection con) {
		super(con);
	}

	protected String getSqlQuery() {

		String s = new String("");
		/*
		 * s += "SELECT ID_ERRORI_SIES_PAGOPA, ID_FASCICOLO_SIEP, ID_EVENTO, "; s +=
		 * " AZIONE_CONTESTO_JAVA, DESCRIZIONE_FUNZIONE, "; s += " COD_UTENTE, COD_UFFICIO, "; s +=
		 * " ERRORE_ESECUZIONE, DATA_INSERIMENTO, "; s +=
		 * " DATA_VISUALIZZAZIONE, COD_UTENTE_VISUALIZZAZIONE "; s += "  FROM ERRORI_SIES_PAGOPA  "; s +=
		 * " WHERE 1 = 1 ";
		 */
		s += " SELECT ERR.ID_ERRORI_SIES_PAGOPA, ERR.ID_FASCICOLO_SIEP, ERR.ID_EVENTO ";
		s += " , ERR.AZIONE_CONTESTO_JAVA, ERR.DESCRIZIONE_FUNZIONE  ";
		s += " , ERR.COD_UTENTE, ERR.COD_UFFICIO   ";
		s += " , ERR.ERRORE_ESECUZIONE, ERR.DATA_INSERIMENTO ";
		s += " , ERR.DATA_VISUALIZZAZIONE, ERR.COD_UTENTE_VISUALIZZAZIONE ";
		s += " , FASCICOLO_SIEP.CHIAVE_ANNO, FASCICOLO_SIEP.CHIAVE_PROGR ";
		s += " , SOGGETTO.COGNOME, SOGGETTO.NOME ";
		// s += " , CG_TIPO_PROVV.RV_MEANING DESC_TIPO_PROVV ";
		// s += " , CG_MOTIVO.RV_MEANING DESC_MOTIVO ";
		s += " , CG_TIPO_PROVV.RV_MEANING||' '||CG_MOTIVO.RV_MEANING as DESC_EVENTO ";
		s += " , EVENTO.DATA_EMISSIONE ";
		s += " FROM ERRORI_SIES_PAGOPA ERR ";
		s += " , FASCICOLO_SIEP, SOGGETTO, EVENTO, CG_REF_CODES CG_MOTIVO, CG_REF_CODES CG_TIPO_PROVV ";
		s += " WHERE 1 = 1 ";
		s += " AND ERR.ID_FASCICOLO_SIEP = FASCICOLO_SIEP.ID_FASCICOLO_SIEP ";
		s += " AND SOGGETTO.ID_SOGGETTO = FASCICOLO_SIEP.SOG_ID_SOGGETTO ";
		s += " AND ERR.ID_EVENTO = EVENTO.ID_EVENTO ";
		s += " AND CG_MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND CG_MOTIVO.RV_LOW_VALUE = EVENTO.COD_MOTIVO ";
		s += " AND CG_TIPO_PROVV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND CG_TIPO_PROVV.RV_LOW_VALUE = EVENTO.COD_TIPO_PROVVEDIMENTO  ";

		return s;
	}

	public GenericModel getModel() throws DAOException {

		ErroriSiesPagopaModel lErroriModel = new ErroriSiesPagopaModel();

		lErroriModel.setIdErroriSiesPagopa(getBigDecimal("ID_ERRORI_SIES_PAGOPA"));
		lErroriModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lErroriModel.setIdEvento(getBigDecimal("ID_EVENTO"));
		lErroriModel.setAzioneContestoJava(getString("AZIONE_CONTESTO_JAVA"));
		lErroriModel.setDescrizioneFunzione(getString("DESCRIZIONE_FUNZIONE"));
		lErroriModel.setCodUtente(getString("COD_UTENTE"));
		lErroriModel.setCodUfficio(getString("COD_UFFICIO"));
		lErroriModel.setErroreEsecuzione(getString("ERRORE_ESECUZIONE"));
		lErroriModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		lErroriModel.setDataVisualizzazione(getDate("DATA_VISUALIZZAZIONE"));
		lErroriModel.setCodUtenteVisualizzazione(getString("COD_UTENTE_VISUALIZZAZIONE"));

		//
		lErroriModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lErroriModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lErroriModel.setCognome(getString("COGNOME"));
		lErroriModel.setNome(getString("NOME"));
		lErroriModel.setDescMotivoEvento(getString("DESC_EVENTO"));
		lErroriModel.setDataEmissione(getDate("DATA_EMISSIONE"));

		return lErroriModel;
	}

	public void ricercaErroreaByKey(BigDecimal aIdErrore) throws DAOException {

		String s = getSqlQuery();
		s += " AND ERR.ID_ERRORI_SIES_PAGOPA = " + aIdErrore;
		setStatement(s);
	}

	public void ricercaErroreaByCriteria(ErroriSiesPagopaModel aCriteriRicerca, int aPage)
			throws DAOException {

		String lSql = getSqlQuery();

		// Aggiungere eventuali altri criteri se necessario
		if (aCriteriRicerca.getDataInserimento() != null)
			lSql += " AND ERR.DATA_INSERIMENTO >= TO_DATE ('"
					+ DateUtils.getDateToString(aCriteriRicerca.getDataInserimento(), "dd/MM/yyyy")
					+ " 00:00:00','dd/MM/yyyy hh24:mi:ss') ";

		if (aCriteriRicerca.getDataInserimentoAl() != null)
			lSql += " AND ERR.DATA_INSERIMENTO <= TO_DATE ('"
					+ DateUtils.getDateToString(aCriteriRicerca.getDataInserimentoAl(), "dd/MM/yyyy")
					+ " 23:59:59','dd/MM/yyyy hh24:mi:ss') ";

		if (aCriteriRicerca.getCodUtente() != null && aCriteriRicerca.getCodUtente().trim().length() > 0)
			lSql += " AND ERR.COD_UTENTE = '" + aCriteriRicerca.getCodUtente() + "' ";

		if (aCriteriRicerca.getIdFascicoloSiep() != null)
			lSql += " AND ERR.ID_FASCICOLO_SIEP = " + aCriteriRicerca.getIdFascicoloSiep() + " ";

		if (aCriteriRicerca.getIdEvento() != null)
			lSql += " AND ERR.ID_EVENTO = " + aCriteriRicerca.getIdEvento();

		if (aCriteriRicerca.getAzioneContestoJava() != null
				&& aCriteriRicerca.getAzioneContestoJava().trim().length() > 0)
			lSql += " AND ERR.AZIONE_CONTESTO_JAVA = '" + aCriteriRicerca.getAzioneContestoJava() + "'";

		lSql += " ORDER BY ERR.DATA_INSERIMENTO DESC ";

		if (aPage > 0) {
			lSql = " SELECT * FROM (SELECT INNER.* , Rownum rn FROM ( " + lSql
					+ " ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		}

		setStatement(lSql);
	}

}