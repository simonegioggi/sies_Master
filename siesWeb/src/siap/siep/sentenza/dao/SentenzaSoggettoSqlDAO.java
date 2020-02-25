package siap.siep.sentenza.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenza.model.SentenzaSoggettoFascicoloModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: SentenzaSoggettoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che esegue ricerca su tabella Sentenza, Soggetto e FascicoloSiep relazionata con
 * il Soggetto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class SentenzaSoggettoSqlDAO extends SIAPSqlDAO {

	public SentenzaSoggettoSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void getCountSentenzeSoggetto(SoggettoModel aSoggetto, SentenzaModel aSentenza)
			throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManySentenze ";
		lStatement += "FROM SENTENZA, ";
		lStatement += " SOGGETTO, Fascicolo_SIEP, COMUNE ";

		lStatement += " WHERE ";
		lStatement += " fascicolo_siep.sog_id_soggetto=soggetto.id_soggetto ";
		lStatement += " AND fascicolo_siep.sen_id_sentenza=sentenza.id_sentenza ";
		lStatement += " AND soggetto.COD_COMUNE_NASCITA=comune.cod_comune";

		if (aSentenza.getCodUfficioInserimento() != null)
			lStatement += " AND sentenza.COD_UFFICIO_INSERIMENTO = '" + aSentenza.getCodUfficioInserimento()
					+ "' ";

		if (aSentenza.getDataProvvedimentoIniziale() != null)
			lStatement += " AND TO_CHAR(DATA_PROVVEDIMENTO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aSentenza.getDataProvvedimentoIniziale(), "yyyyMMdd") + "'";

		if (aSentenza.getDataProvvedimentoFinale() != null)
			lStatement += " AND TO_CHAR(DATA_PROVVEDIMENTO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aSentenza.getDataProvvedimentoFinale(), "yyyyMMdd") + "'";

		if (aSoggetto.getCognome() != null && aSoggetto.getCognome().length() > 0)
			lStatement += " AND Soggetto.Cognome like '"
					+ StringUtils.convertSqlString(aSoggetto.getCognome()) + "%'";

		if (aSoggetto.getNome() != null && aSoggetto.getNome().length() > 0)
			lStatement += " AND Soggetto.nome like '" + StringUtils.convertSqlString(aSoggetto.getNome())
					+ "%'";

		if (aSoggetto.getDataNascita() != null)
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lStatement += " AND trunc(soggetto.DATA_NASCITA) = to_date('"
					+ DateUtils.getDateToString(aSoggetto.getDataNascita(), "dd/MM/yyyy") + "','DD-MM-YYYY')";

		if (aSoggetto.getCodComuneNascita() != null && aSoggetto.getCodComuneNascita().length() > 0)
			lStatement += " AND soggetto.COD_COMUNE_NASCITA = '" + aSoggetto.getCodComuneNascita() + "'";

		lStatement += " AND SENTENZA.FLAG_VISIBILITA IS NULL";

		setStatement(lStatement);
	}

	public void ricercaSentenzeSoggetto(SoggettoModel aSoggetto, SentenzaModel aSentenza, int aPage)
			throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT " + "ID_SENTENZA, " + "COD_TIPO_PROVVEDIMENTO, "
				+ "TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, "
				+ "Fascicolo_SIEP.DATA_ARRIVO_ATTO DATA_ARRIVO_ATTO_FASCICOLO, "
				+ "sentenza.DATA_ISCRIZIONE DATA_ISCRIZIONE_SENTENZA, " + "DATA_PROVVEDIMENTO, "
				+ "COD_TIPO_AUTORITA_EMITTENTE, "
				+ "TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, "
				+ "COD_LUOGO_EMITTENTE, " + "LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, "
				+ "NUM_SEZIONE_AUTORITA_EMITTENTE, " + "ANNO_SENTENZA, " + "NUMERO_SENTENZA, "
				+ "sentenza.DATA_IRREVOCABILITA, " + "FLAG_SENTENZA_APPLICAZ_PENA, " +

				"ID_FASCICOLO_SIEP," + "COD_STATO_FASCICOLO,"
				+ "FASCICOLO_SIEP.DATA_ISCRIZIONE DATA_ISCRIZIONE_FASCICOLO," + "CHIAVE_ANNO,"
				+ "CHIAVE_PROGR," + "SEN_ID_SENTENZA," + "SOG_ID_SOGGETTO," + "CHIAVE_UFFICIO," +

				"NOME," + "COGNOME," + "ANNO_NASCITA," + "DATA_NASCITA," + "COD_COMUNE_NASCITA,"
				+ "COMUNE.DESCRIZIONE DESC_COMUNE," + "COD_PROVINCIA_NASCITA," + "FLAG_VISIBILITA ";

		lStatement += " FROM SENTENZA, CG_REF_CODES TIPO_PROVVEDIMENTO, CG_REF_CODES TIPO_AUTORITA_EMITTENTE,";
		lStatement += " COMUNE LUOGO_EMITTENTE, CG_REF_CODES TIPO_PROVV_RIF, CG_REF_CODES TIPO_AUTORITA_PROVV_RIF,";
		lStatement += " COMUNE SEDE_NOTIZIA, CG_REF_CODES TIPO_PROVVEDIMENTO_RIF, CG_REF_CODES TIPO_PROVVEDIMENTO_ALTRO,";
		lStatement += " COMUNE LUOGO_PROVV_RIF, CG_REF_CODES TIPO_DECISIONE_CASSAZIONE, CG_REF_CODES DECOBILAN, ";

		lStatement += " SOGGETTO, Fascicolo_SIEP, COMUNE ";

		lStatement += " WHERE (TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO)";
		lStatement += " AND (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE)";
		lStatement += " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUOGO_EMITTENTE)";
		lStatement += " AND (DECOBILAN.RV_DOMAIN='BILANCIAMENTO_CIRCOSTANZE' AND DECOBILAN.RV_LOW_VALUE=COD_BILANCIAMENTO_CIRCOSTANZE) ";
		lStatement += " AND (TIPO_PROVV_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO_RIF_P' AND TIPO_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_PROVV_RIF)";
		lStatement += " AND (TIPO_AUTORITA_PROVV_RIF.RV_DOMAIN ='TIPO_UFFICIO' AND TIPO_AUTORITA_PROVV_RIF.RV_LOW_VALUE = COD_TIPO_AUTORITA_PROVV_RIF)";
		lStatement += " AND (LUOGO_PROVV_RIF.COD_COMUNE = COD_LUOGO_PROVV_RIF)";
		lStatement += " AND (TIPO_DECISIONE_CASSAZIONE.RV_DOMAIN = 'TIPO_DECISIONE_CASSAZIONE' AND TIPO_DECISIONE_CASSAZIONE.RV_LOW_VALUE = COD_TIPO_DECISIONE_CASSAZIONE)";

		lStatement += " AND (TIPO_PROVVEDIMENTO_RIF.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO_RIF.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO_RIF)";
		lStatement += " AND (TIPO_PROVVEDIMENTO_ALTRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND TIPO_PROVVEDIMENTO_ALTRO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO_ALTRO)";
		lStatement += " AND (SEDE_NOTIZIA.COD_COMUNE = COD_SEDE_NOTIZIA_REATO)";

		lStatement += " AND fascicolo_siep.sog_id_soggetto=soggetto.id_soggetto ";
		lStatement += " AND fascicolo_siep.sen_id_sentenza=sentenza.id_sentenza ";
		lStatement += " AND soggetto.COD_COMUNE_NASCITA=comune.cod_comune";

		String lPaginedStatement = new String("");

		// Ufficio per la prima query
		if (aSentenza.getCodUfficioInserimento() != null) {
			lStatement += " AND sentenza.COD_UFFICIO_INSERIMENTO = '" + aSentenza.getCodUfficioInserimento()
					+ "' ";
		}

		// lStatement += " " + setCondizioni(aSentenza);
		if (aSentenza.getDataProvvedimentoIniziale() != null) {
			lStatement += " AND TO_CHAR(DATA_PROVVEDIMENTO,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aSentenza.getDataProvvedimentoIniziale(), "yyyyMMdd") + "'";
		}
		if (aSentenza.getDataProvvedimentoFinale() != null) {
			lStatement += " AND TO_CHAR(DATA_PROVVEDIMENTO,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aSentenza.getDataProvvedimentoFinale(), "yyyyMMdd") + "'";
		}

		// lStatement += " " + SetCondizioni(aSoggetto);
		if (aSoggetto.getCognome() != null && aSoggetto.getCognome().length() > 0) {
			lStatement += " AND Soggetto.Cognome like '"
					+ StringUtils.convertSqlString(aSoggetto.getCognome()) + "%'";
		}
		if (aSoggetto.getNome() != null && aSoggetto.getNome().length() > 0) {
			lStatement += " AND Soggetto.nome like '" + StringUtils.convertSqlString(aSoggetto.getNome())
					+ "%'";
		}
		if (aSoggetto.getDataNascita() != null) {
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lStatement += " AND trunc(soggetto.DATA_NASCITA) = to_date('"
					+ DateUtils.getDateToString(aSoggetto.getDataNascita(), "dd/MM/yyyy") + "','DD-MM-YYYY')";
		}
		if (aSoggetto.getCodComuneNascita() != null && aSoggetto.getCodComuneNascita().length() > 0) {
			lStatement += " AND soggetto.COD_COMUNE_NASCITA = '" + aSoggetto.getCodComuneNascita() + "'";
		}

		lStatement += " AND SENTENZA.FLAG_VISIBILITA IS NULL ";

		lStatement += " ORDER BY SENTENZA.DATA_PROVVEDIMENTO ";

		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/**
	 * Restituisce la rappresentazione dei dati selezionati in Model
	 * 
	 * @return GenericModel
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		SentenzaSoggettoFascicoloModel lModel = new SentenzaSoggettoFascicoloModel();

		lModel.setIdSentenza(getBigDecimal("ID_SENTENZA"));
		lModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lModel.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
		// lModel.setDataProvvedimento(getDate("DATA_ARRIVO_ATTO_FASCICOLO") );
		lModel.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		lModel.setDataIscrizione(getDate("DATA_ISCRIZIONE_SENTENZA"));

		lModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		lModel.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
		lModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		lModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		lModel.setNumSezioneAutoritaEmittente(getString("NUM_SEZIONE_AUTORITA_EMITTENTE"));
		lModel.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA"));
		lModel.setNumSentenza(getString("NUMERO_SENTENZA"));
		// lModel.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA") );
		// lModel.setFlagSentenzaApplicazPena(getString("FLAG_SENTENZA_APPLICAZ_PENA") );

		lModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lModel.setCodiceStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lModel.setDataIscrizione(getDate("DATA_ISCRIZIONE_FASCICOLO"));
		lModel.setChiaveAnnoFascicolo(getBigDecimal("CHIAVE_ANNO"));
		lModel.setChiaveNumeroFascicolo(getBigDecimal("CHIAVE_PROGR"));
		lModel.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA"));
		lModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		lModel.setChiaveUfficio(getString("CHIAVE_UFFICIO"));

		lModel.setNome(getString("NOME"));
		lModel.setCognome(getString("COGNOME"));
		lModel.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		lModel.setDataNascita(getDate("DATA_NASCITA"));
		lModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		lModel.setDescrComuneNascita(getString("DESC_COMUNE"));
		lModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		// La descrizione della Provincia di nascita la si ricava dalle Decodifiche in memoria per risparmiare
		// una JOIN
		lModel.setDescrProvinciaNascita(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
				.getProvincie(), lModel.getCodProvinciaNascita()));

		return lModel;
	}

}