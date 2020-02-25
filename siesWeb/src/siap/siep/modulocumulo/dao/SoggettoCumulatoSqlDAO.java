package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: SoggettoCumulatoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella SoggettoCumulato
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
public class SoggettoCumulatoSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public SoggettoCumulatoSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountSoggettoCumulato(SoggettoCumulatoModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM SOGGETTO_CUMULATO ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
	 * passata in input
	 * 
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaSoggettoCumulatoPaged(SoggettoCumulatoModel aModel, int aPage) throws DAOException {
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
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lPaginedStatement = " + lPaginedStatement);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaSoggettoCumulato(SoggettoCumulatoModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lSql = " + lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 * 
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaSoggettoCumulatoByKey(BigDecimal aIdSoggettoCumulato) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		// lSql += " WHERE " + setCondizioniByKey( aIdSoggettoCumulato);
		// lSql += " AND " + setCondizioniByKey( aIdSoggettoCumulato);

		lSql += " AND ID_SOGGETTO_CUMULATO = " + aIdSoggettoCumulato;

		// Imposta lo statement da eseguire
		setStatement(lSql);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lSql = " + lSql);
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 * 
	 * @return
	 ****************************************************************************/
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_SOGGETTO_CUMULATO, " + "COGNOME, " + "NOME, " + "SESSO, "
				+ "DATA_NASCITA, " + "DATA_NASCITA_PRESUNTA, " + "ANNO_NASCITA, " + "MESE_NASCITA, "
				+ "COD_COMUNE_NASCITA, CODCOMUNENASCITA.DESCRIZIONE DESC_COMUNE_NASCITA, "
				+ "COD_PROVINCIA_NASCITA, "
				+ "COD_STATO_NASCITA, STATO_NASCITA.RV_MEANING as DESC_STATO_NASCITA, "
				+ "DESC_COMUNE_NASCITA_ESTERO, "
				+ "NAZIONALITA, NAZIONALITA.RV_MEANING as DESC_NAZIONALITA, " + "PATERNITA, "
				+ "COGNOME_MADRE, " + "NOME_MADRE, " + "COD_FISCALE, " + "ATTO_NASCITA, " + "COD_AFIS, "
				+ "COD_COMUNE_CASELLARIO, " + "NOTE, " + "KEY_SOGG_NSC, " + "TIT_ID_TITOLO_CUMULATO, "
				+ "FLAG_STATO, " + "MOTIVO_MODIFICA, "
				+ "COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO ";
		// aggiungere qui gli eventuali campi descrizioni

		// Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		lStatement += " FROM SOGGETTO_CUMULATO, COMUNE CODCOMUNENASCITA ";
		lStatement += " , CG_REF_CODES STATO_NASCITA ";
		lStatement += " , CG_REF_CODES NAZIONALITA ";

		lStatement += " WHERE 1=1 ";
		lStatement += " AND (nvl(SOGGETTO_CUMULATO.COD_COMUNE_NASCITA,'-') = CODCOMUNENASCITA.COD_COMUNE ) ";
		lStatement += " AND (NVL (SOGGETTO_CUMULATO.COD_STATO_NASCITA, '-') = STATO_NASCITA.RV_LOW_VALUE AND STATO_NASCITA.RV_DOMAIN = 'NAZIONE') ";
		lStatement += " AND (NVL (SOGGETTO_CUMULATO.NAZIONALITA, '-') = NAZIONALITA.RV_LOW_VALUE AND NAZIONALITA.RV_DOMAIN = 'NAZIONE') ";
		// lStatement +=
		// " AND ( nvl(SOGGETTO_CUMULATO.COD_PROVINCIA_NASCITA,'-') = CODPROVINCIANASCITA.RV_LOW_VALUE AND CODPROVINCIANASCITA.RV_DOMAIN = 'PROVINCIA_NASCITA' ) ";
		// lStatement +=
		// " AND ( nvl(SOGGETTO_CUMULATO.COD_STATO_NASCITA,'-') = CODSTATONASCITA.RV_LOW_VALUE AND CODSTATONASCITA.RV_DOMAIN = 'STATO_NASCITA' ) ";
		// lStatement +=
		// " AND ( nvl(SOGGETTO_CUMULATO.COD_COMUNE_CASELLARIO,'-') = CODCOMUNECASELLARIO.RV_LOW_VALUE AND CODCOMUNECASELLARIO.RV_DOMAIN = 'COMUNE_CASELLARIO' ) ";
		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		SoggettoCumulatoModel aModel = new SoggettoCumulatoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdSoggettoCumulato(getBigDecimal("ID_SOGGETTO_CUMULATO"));
		aModel.setCognome(getString("COGNOME"));
		aModel.setNome(getString("NOME"));
		aModel.setSesso(getString("SESSO"));
		aModel.setDataNascita(getDate("DATA_NASCITA"));
		aModel.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));
		aModel.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		aModel.setMeseNascita(getBigDecimal("MESE_NASCITA"));
		aModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		aModel.setDescrComuneNascita(getString("DESC_COMUNE_NASCITA"));
		aModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		// aModel.setDescrProvinciaNascita(getString("") );
		aModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		aModel.setDescrStatoNascita(getString("DESC_STATO_NASCITA"));
		aModel.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		aModel.setNazionalita(getString("NAZIONALITA"));
		aModel.setPaternita(getString("PATERNITA"));
		aModel.setCognomeMadre(getString("COGNOME_MADRE"));
		aModel.setNomeMadre(getString("NOME_MADRE"));
		aModel.setCodFiscale(getString("COD_FISCALE"));
		aModel.setAttoNascita(getString("ATTO_NASCITA"));
		aModel.setCodAfis(getString("COD_AFIS"));
		aModel.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO"));
		// aModel.setDescrComuneCasellario(getString("") );
		aModel.setNote(getString("NOTE"));
		aModel.setKeySoggNsc(getBigDecimal("KEY_SOGG_NSC"));
		aModel.setTitIdTitoloCumulato(getBigDecimal("TIT_ID_TITOLO_CUMULATO"));
		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setMotivoModifica(getString("MOTIVO_MODIFICA"));

		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(SoggettoCumulatoModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdSoggettoCumulato() != null) {
			lCondizioni += " and ID_SOGGETTO_CUMULATO = " + aModel.getIdSoggettoCumulato() + "";
		}
		if (aModel.getCognome() != null && aModel.getCognome().length() > 0) {
			lCondizioni += " and COGNOME = '" + aModel.getCognome() + "' ";
		}
		if (aModel.getNome() != null && aModel.getNome().length() > 0) {
			lCondizioni += " and NOME = '" + aModel.getNome() + "' ";
		}
		if (aModel.getSesso() != null && aModel.getSesso().length() > 0) {
			lCondizioni += " and SESSO = '" + aModel.getSesso() + "' ";
		}
		if (aModel.getDataNascita() != null) {
			lCondizioni += " and to_char(DATA_NASCITA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataNascita(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataNascitaPresunta() != null && aModel.getDataNascitaPresunta().length() > 0) {
			lCondizioni += " and DATA_NASCITA_PRESUNTA = '" + aModel.getDataNascitaPresunta() + "' ";
		}
		if (aModel.getAnnoNascita() != null) {
			lCondizioni += " and ANNO_NASCITA = " + aModel.getAnnoNascita() + "";
		}
		if (aModel.getMeseNascita() != null) {
			lCondizioni += " and MESE_NASCITA = " + aModel.getMeseNascita() + "";
		}
		if (aModel.getCodComuneNascita() != null && aModel.getCodComuneNascita().length() > 0) {
			lCondizioni += " and COD_COMUNE_NASCITA = '" + aModel.getCodComuneNascita() + "' ";
		}
		if (aModel.getCodProvinciaNascita() != null && aModel.getCodProvinciaNascita().length() > 0) {
			lCondizioni += " and COD_PROVINCIA_NASCITA = '" + aModel.getCodProvinciaNascita() + "' ";
		}
		if (aModel.getCodStatoNascita() != null && aModel.getCodStatoNascita().length() > 0) {
			lCondizioni += " and COD_STATO_NASCITA = '" + aModel.getCodStatoNascita() + "' ";
		}
		if (aModel.getDescComuneNascitaEstero() != null && aModel.getDescComuneNascitaEstero().length() > 0) {
			lCondizioni += " and DESC_COMUNE_NASCITA_ESTERO = '" + aModel.getDescComuneNascitaEstero() + "' ";
		}
		if (aModel.getNazionalita() != null && aModel.getNazionalita().length() > 0) {
			lCondizioni += " and NAZIONALITA = '" + aModel.getNazionalita() + "' ";
		}
		if (aModel.getPaternita() != null && aModel.getPaternita().length() > 0) {
			lCondizioni += " and PATERNITA = '" + aModel.getPaternita() + "' ";
		}
		if (aModel.getCognomeMadre() != null && aModel.getCognomeMadre().length() > 0) {
			lCondizioni += " and COGNOME_MADRE = '" + aModel.getCognomeMadre() + "' ";
		}
		if (aModel.getNomeMadre() != null && aModel.getNomeMadre().length() > 0) {
			lCondizioni += " and NOME_MADRE = '" + aModel.getNomeMadre() + "' ";
		}
		if (aModel.getCodFiscale() != null && aModel.getCodFiscale().length() > 0) {
			lCondizioni += " and COD_FISCALE = '" + aModel.getCodFiscale() + "' ";
		}
		if (aModel.getAttoNascita() != null && aModel.getAttoNascita().length() > 0) {
			lCondizioni += " and ATTO_NASCITA = '" + aModel.getAttoNascita() + "' ";
		}
		if (aModel.getCodAfis() != null && aModel.getCodAfis().length() > 0) {
			lCondizioni += " and COD_AFIS = '" + aModel.getCodAfis() + "' ";
		}
		if (aModel.getCodComuneCasellario() != null && aModel.getCodComuneCasellario().length() > 0) {
			lCondizioni += " and COD_COMUNE_CASELLARIO = '" + aModel.getCodComuneCasellario() + "' ";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getKeySoggNsc() != null) {
			lCondizioni += " and KEY_SOGG_NSC = " + aModel.getKeySoggNsc() + "";
		}
		if (aModel.getTitIdTitoloCumulato() != null) {
			lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + "";
		}
		if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) {
			lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' ";
		}
		if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) {
			lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' ";
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
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lCondizioni = " + lCondizioni);
		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di select per chiave
	 * 
	 * @param aKey
	 * @return
	 ****************************************************************************/
	public String setCondizioniByKey(BigDecimal aIdSoggettoCumulato) {
		String lCondizioni = new String();

		lCondizioni += " and ID_SOGGETTO_CUMULATO = " + aIdSoggettoCumulato;

		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lCondizioni = " + lCondizioni);

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sezione order by
	 * 
	 * @return
	 ****************************************************************************/
	protected String getOrderBy() {
		String orderBy = new String("");
		// orderBy = " ORDER BY ";
		return orderBy;
	}

	public void ricercaSoggettoCumulatoByIdTitolo(BigDecimal aIdTitoloCumulato) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Aggiunge le where condition per chiave
		lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitoloCumulato;

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

}