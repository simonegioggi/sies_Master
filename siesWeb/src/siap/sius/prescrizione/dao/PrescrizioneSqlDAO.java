package siap.sius.prescrizione.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sius.prescrizione.model.PrescrizioneModel;

/**
 * <p>
 * Title: PrescrizioneSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Prescrizione
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
public class PrescrizioneSqlDAO extends SIAPSqlDAO {

	/**
	 * Costruttore di classe con argomento.
	 * <p>
	 * 
	 * @param con
	 *            prende la connessione al dbase.
	 */
	public PrescrizioneSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Ricerca prescrizione.
	 * <p>
	 * 
	 * @param aModel
	 *            model con i dati per la ricerca della prescrizione.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void ricercaPrescrizione(PrescrizioneModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizione(aModel);

		setStatement(lSql);
	}

	/**
	 * Imposta lo statement per la ricerca delle prescrizioni per l'id del deposito ordinanza pc.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id del deposito ordinaza pc.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void ricercaPrescrizioneByDepOrdinanzaPc(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " AND EVE_ID_EVENTO = " + aKey;
		lSql += " ORDER BY PROGR_PRESCRIZIONE,COD_TIPO_PRESCRIZIONE ";

		setStatement(lSql);
	}

	/**
	 * Imposta lo statement per la ricerca delle prescrizioni per l'id dell'evento.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id dell'evento.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	// STUB: il campo si chiama ancora EVE_ID_EVENTO ma andrà cambiato
	public void ricercaPrescrizioneByIdEve(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " AND EVE_ID_EVENTO = " + aKey;
		lSql += " ORDER BY PROGR_PRESCRIZIONE,COD_TIPO_PRESCRIZIONE ";
		setStatement(lSql);
	}

	/**
	 * Imposta lo statement per la ricerca di una prescrizione per la propria chiave.
	 * 
	 * @param aKey
	 *            chiave id prescrizione da ricercare.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void ricercaPrescrizioneByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	/**
	 * Query Sql principale e generica.
	 * <p>
	 * 
	 * @return la stringa sql.
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT  ";
		lStatement += "	ID_PRESCRIZIONE, ";
		lStatement += "	COD_TIPO_PRESCRIZIONE, TIP_PRE.RV_MEANING DESC_TIP_PRE,";
		lStatement += "	COD_LUOGO_AFFIDAMENTO, LUO_AFF.DESCRIZIONE DESC_LUO_AFF,";
		lStatement += "	COD_UFF_MAGISTRATO_COMPETENTE, ";
		lStatement += "	COD_LUOGO_AUTORIZZATO, LUO_AUT.DESCRIZIONE DESC_LUO_AUT,";
		lStatement += "	ID_CSSA_COMPETENTE, CSSA.COMUNE DESC_LUO_CSS,";
		lStatement += "	DESCR_MANSIONE_LAVORATIVA, ";
		lStatement += "	DESCR_LUOGO_LAVORO, ";
		lStatement += "	COD_PROVINCIA_AUTORIZZATA, LUO_PRO.RV_MEANING DESC_LUO_PRO,";
		lStatement += "	ORA_USCITA_ABITAZIONE, ";
		lStatement += "	ORA_RIENTRO_ABITAZIONE, ";
		lStatement += "	AUTORITA_COMPETENTE_CONTROLLO, ";
		lStatement += "	NUM_VOLTE_CONTROLLO, ";
		lStatement += "	DESCR_ALTRA_PRESCRIZIONE, ";
		lStatement += "	COD_OPERATORE_INSERIMENTO, ";
		lStatement += "	DATA_INSERIMENTO, ";
		lStatement += "	COD_UFFICIO_INSERIMENTO, ";
		lStatement += "	COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "	DATA_AGGIORNAMENTO, ";
		lStatement += "	COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += " EVE_ID_EVENTO, ";
		lStatement += " DESCR_COMUNITA_TERAPEUTICA, ";
		lStatement += " PROGR_PRESCRIZIONE, ";
		lStatement += " DESCR_PRESCRIZIONE_1, ";
		lStatement += " DESCR_PRESCRIZIONE_2, ";
		lStatement += " DESCR_PRESCRIZIONE_3, ";
		lStatement += " LUO_MAG_COMP.DESCRIZIONE DESCR_UFF_MAG_COMP";
		lStatement += "	FROM PRESCRIZIONE,CG_REF_CODES TIP_PRE,";
		lStatement += "	COMUNE LUO_AFF, COMUNE LUO_AUT, CSSA, CG_REF_CODES LUO_PRO,";
		lStatement += "	UFFICIO UFF_MAG_COMP, COMUNE LUO_MAG_COMP";
		lStatement += "	WHERE ";
		lStatement += " TIP_PRE.RV_DOMAIN = 'TIPO_PRESCRIZIONE' AND COD_TIPO_PRESCRIZIONE= TIP_PRE.RV_LOW_VALUE";
		lStatement += " AND LUO_AFF.COD_COMUNE = COD_LUOGO_AFFIDAMENTO";
		lStatement += "	AND LUO_AUT.COD_COMUNE = COD_LUOGO_AUTORIZZATO";
		lStatement += "	AND LUO_MAG_COMP.COD_COMUNE = UFF_MAG_COMP.COD_COMUNE AND UFF_MAG_COMP.COD_UFFICIO = PRESCRIZIONE.COD_UFF_MAGISTRATO_COMPETENTE";
		lStatement += "	AND CSSA.ID_CSSA = ID_CSSA_COMPETENTE";
		lStatement += " AND LUO_PRO.RV_DOMAIN = 'PROVINCIA' AND LUO_PRO.RV_LOW_VALUE = COD_PROVINCIA_AUTORIZZATA ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	/**
	 * Popola il model con i dati prelevati da DAO.
	 * <p>
	 * 
	 * @return istanza singola del model popolato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public GenericModel getModel() throws DAOException {
		PrescrizioneModel aModel = new PrescrizioneModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdPrescrizione(getBigDecimal("ID_PRESCRIZIONE"));
		aModel.setCodTipoPrescrizione(getString("COD_TIPO_PRESCRIZIONE"));
		aModel.setDescrTipoPrescrizione(getString("DESC_TIP_PRE"));
		aModel.setCodLuogoAffidamento(getString("COD_LUOGO_AFFIDAMENTO"));
		aModel.setDescrLuogoAffidamento(getString("DESC_LUO_AFF"));
		aModel.setCodUffMagistratoCompetente(getString("COD_UFF_MAGISTRATO_COMPETENTE"));
		aModel.setDescrUffMagistratoCompetente(getString("DESCR_UFF_MAG_COMP"));
		aModel.setCodLuogoAutorizzato(getString("COD_LUOGO_AUTORIZZATO"));
		aModel.setDescrLuogoAutorizzato(getString("DESC_LUO_AUT"));
		aModel.setIdCssaCompetente(getBigDecimal("ID_CSSA_COMPETENTE"));
		aModel.setDescrComuneCssaCompetente(getString("DESC_LUO_CSS"));
		aModel.setDescrMansioneLavorativa(getString("DESCR_MANSIONE_LAVORATIVA"));
		aModel.setDescrLuogoLavoro(getString("DESCR_LUOGO_LAVORO"));
		aModel.setCodProvinciaAutorizzata(getString("COD_PROVINCIA_AUTORIZZATA"));
		aModel.setDescrProvinciaAutorizzata(getString("DESC_LUO_PRO"));
		aModel.setOraUscitaAbitazione(getString("ORA_USCITA_ABITAZIONE"));
		aModel.setOraRientroAbitazione(getString("ORA_RIENTRO_ABITAZIONE"));
		aModel.setAutoritaCompetenteControllo(getString("AUTORITA_COMPETENTE_CONTROLLO"));
		aModel.setNumVolteControllo(getBigDecimal("NUM_VOLTE_CONTROLLO"));
		aModel.setDescrAltraPrescrizione(getString("DESCR_ALTRA_PRESCRIZIONE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setEveIdEve(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setDescrComunitaTerapeutica(getString("DESCR_COMUNITA_TERAPEUTICA"));
		aModel.setProgrPrescrizione(getBigDecimal("PROGR_PRESCRIZIONE"));
		aModel.setDescrPrescrizione1(getString("DESCR_PRESCRIZIONE_1"));
		aModel.setDescrPrescrizione2(getString("DESCR_PRESCRIZIONE_2"));
		aModel.setDescrPrescrizione3(getString("DESCR_PRESCRIZIONE_3"));
		return aModel;
	}

	/**
	 * Imposta le condizioni genrali.
	 * <p>
	 * 
	 * @param aModel
	 *            dati di filtro.
	 * @return condizione.
	 */
	public String setCondizione(PrescrizioneModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	/**
	 * Imposta le condizioni di ricerca per la chiave della prescrizione.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id prescrzione.
	 * @return ritorna istruzione SQL.
	 */
	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_PRESCRIZIONE = " + aKey;
	}

}