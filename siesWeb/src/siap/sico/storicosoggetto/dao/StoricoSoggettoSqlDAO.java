package siap.sico.storicosoggetto.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.storicosoggetto.model.StoricoSoggettoModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: StoricoSoggettoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella StoricoSoggetto
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
public class StoricoSoggettoSqlDAO extends SqlDAO {

	public StoricoSoggettoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaStoricoSoggetto(StoricoSoggettoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaStoricoSoggettoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaStoricoSoggettoByIdSogVariato(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneByIdSogVariato(aKey);
		lSql += " ORDER BY DATA_VARIAZIONE DESC ";

		setStatement(lSql);
	}

	public void ricercaStoricoSoggettoByIdSogNuovo(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneByIdSogNuovo(aKey);
		setStatement(lSql);
	}

	public void ricercaStoricoSoggettoByIdSog(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneByIdSogVariato(aKey);
		setStatement(lSql);
	}

	public void ricercaStoricoSoggettoPerElenco(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneByIdSogNuovo(aKey);
		lSql += " AND " + setCondizioneByIdSogNuovo(aKey);

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "PROGRESSIVO_STORICO, " + "DATA_VARIAZIONE, " + "ID_SOGGETTO_VARIATO, "
				+ "COD_FISCALE, " + "COD_CS, " + "COD_AFIS, " + "COGNOME, " + "NOME, " + "ANNO_NASCITA, "
				+ "DATA_NASCITA, " + "DATA_NASCITA_PRESUNTA, " + "COD_COMUNE_NASCITA, "
				+ " COM.DESCRIZIONE COMUNE," + "COD_PROVINCIA_NASCITA, " + "COD_STATO_NASCITA, "
				+ " NAZ.RV_MEANING STATO," + "DESC_COMUNE_NASCITA_ESTERO, " + "NAZIONALITA, "
				+ " DECODENAZ.RV_MEANING DESCRNAZ," + "PATERNITA, " + "COGNOME_MADRE, " + "NOME_MADRE, "
				+ "SESSO, " + "ATTO_NASCITA, " + "NOTE, " + "COD_COMUNE_CASELLARIO, "
				+ "FLAG_PRESENZA_FASCICOLO, " + "MESE_NASCITA, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, " + "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "ID_SOGGETTO_NUOVO, " + "ETA_PRESUNTA_ANNI, " + "ETA_PRESUNTA_MESI, "
				+ "FAS_SIE_ID_FASCICOLO_SIUS ";
		lStatement += " FROM storico_soggetto, COMUNE COM ,CG_REF_CODES DECODENAZ , CG_REF_CODES NAZ ";
		lStatement += " WHERE ";
		lStatement += " COD_COMUNE_NASCITA = COM.COD_COMUNE";
		lStatement += " AND DECODENAZ.RV_LOW_VALUE = NAZIONALITA ";
		lStatement += " AND DECODENAZ.RV_DOMAIN='NAZIONE' ";
		lStatement += " AND NAZ.RV_LOW_VALUE = COD_STATO_NASCITA  ";
		lStatement += " AND NAZ.RV_DOMAIN = 'NAZIONE'  ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		StoricoSoggettoModel aModel = new StoricoSoggettoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setProgressivoStorico(getBigDecimal("PROGRESSIVO_STORICO"));
		aModel.setDataVariazione(getDate("DATA_VARIAZIONE"));
		aModel.setIdSoggettoVariato(getBigDecimal("ID_SOGGETTO_VARIATO"));
		aModel.setCodFiscale(getString("COD_FISCALE"));
		aModel.setCodCs(getString("COD_CS"));
		aModel.setCodAfis(getString("COD_AFIS"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));
		aModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		// aModel.setDescrComuneNascita(getString("") );
		aModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		// aModel.setDescrProvinciaNascita(getString("") );
		aModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		aModel.setDescrComuneNascita(getString("COMUNE"));
		aModel.setDescrNazionalita(getString("DESCRNAZ"));
		aModel.setDescrStatoNascita(getString("STATO"));

		// aModel.setDescrStatoNascita(getString("") );
		aModel.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		aModel.setNazionalita(getString("NAZIONALITA"));
		aModel.setDescrStatoNascita(getString("STATO"));
		aModel.setPaternita(getString("PATERNITA"));
		aModel.setCognomeMadre(getString("COGNOME_MADRE"));
		aModel.setNomeMadre(getString("NOME_MADRE"));
		aModel.setSesso(getString("SESSO"));
		aModel.setAttoNascita(getString("ATTO_NASCITA"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO"));
		// aModel.setDescrComuneCasellario(getString("") );
		aModel.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO"));
		aModel.setMeseNascita(getBigDecimal("MESE_NASCITA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));

		aModel.setIdSoggettoNuovo(getBigDecimal("ID_SOGGETTO_NUOVO"));
		aModel.setFasSieIdFascicoloSius(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIUS"));
		aModel.setEtaPresuntaAnni(getBigDecimal("ETA_PRESUNTA_ANNI"));
		aModel.setEtaPresuntaMesi(getBigDecimal("ETA_PRESUNTA_MESI"));

		return aModel;
	}

	public String setCondizione(StoricoSoggettoModel aModel) {
		String lCondizioni = new String();

		lCondizioni += " AND PROGRESSIVO_STORICO = '" + aModel.getProgressivoStorico() + "'";

		if ((aModel.getIdSoggettoNuovo() != null) && !("".equals(aModel.getIdSoggettoNuovo().toString()))) {
			lCondizioni += " AND ID_SOGGETTO_NUOVO = '" + aModel.getIdSoggettoNuovo() + "'";
		}
		if ((aModel.getIdSoggettoVariato() != null)
				&& !("".equals(aModel.getIdSoggettoVariato().toString()))) {
			lCondizioni += " AND ID_SOGGETTO_VARIATO = '" + aModel.getIdSoggettoVariato() + "'";
		}

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioneByIdSogVariato(BigDecimal aKey) {
		String lCondizioni = new String();

		lCondizioni += " AND ID_SOGGETTO_VARIATO = '" + aKey + "'";

		return lCondizioni;
	}

	public String setCondizioneByIdSogNuovo(BigDecimal aKey) {
		String lCondizioni = new String();

		lCondizioni += " AND ID_SOGGETTO_NUOVO = '" + aKey + "'";

		return lCondizioni;
	}

	public void nextProgressivo(BigDecimal aKey) {

		String lSql = "select MAX(PROGRESSIVO_STORICO) max_progressivo from storico_soggetto where ID_SOGGETTO_VARIATO="
				+ aKey;
		setStatement(lSql);
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_storico_soggetto = " + aKey;
	}

}