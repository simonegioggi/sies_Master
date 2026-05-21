package siap.siep.posizione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;

/**
 * PosizioneGiuridicaSqlDAO - Classe SqlDAO che rappresenta la tabella PosizioneGiuridica
 *
 * @version 1.0
 */
public class PosizioneGiuridicaSqlDAO extends SIAPSqlDAO {

	public PosizioneGiuridicaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaPosizioneGiuridica(PosizioneGiuridicaModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);

		setStatement(lSql);
	}

	public void ricercaPosizioneGiuridicaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	public void ricercaPosizioneGiuridicaByIdEvento(BigDecimal aIdEvento) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdEvento(aIdEvento);

		setStatement(lSql);
	}

	public void ricercaPosizioneGiuridicaCorrente(PosizioneGiuridicaModel aModel) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " " + setCondizioniPerIdFascicoloCorrente(aModel);

		setStatement(lStatement);
	}

	public void ricercaPosizioneGiuridicaByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aIdFascicolo);
		// Ordina le Posizioni Giuridiche dalla più recente in poi
		// **** lSql += " ORDER BY ID_POSIZIONE_GIURIDICA DESC"; //****** PROBLEMA ORDINE SEQUENCE
		// ID_POSIZIONE!!!!
		lSql += " ORDER BY DATA_INSERIMENTO ASC";

		setStatement(lSql);
	}

	public void ricercaPosizioneGiuridicaByIdFascicoloDesc(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aIdFascicolo);
		lSql += " ORDER BY DATA_INSERIMENTO DESC";

		setStatement(lSql);
	}

	public void ricercaPosGiuCorrenteByIdFascicoloDataFineNull(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniPosGiuCorrenteByIdFascicoloDataFineNull(aIdFascicolo);

		setStatement(lSql);
	}

	public void ricercaPosGiuCorrenteByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniPosGiuCorrenteByIdFascicolo(aIdFascicolo);

		setStatement(lSql);
	}

	public void ricercaMaxIdPosizioneGiuridicaByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws DAOException {
		String lStatement = new String();

		/*
		 * ********************* //****** PROBLEMA ORDINE SEQUENCE ID_POSIZIONE!!!! lStatement +=
		 * " SELECT MAX(ID_POSIZIONE_GIURIDICA) ID_POSIZIONE_GIURIDICA"; lStatement +=
		 * " FROM POSIZIONE_GIURIDICA"; lStatement += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " +
		 * aIdFascicoloSiep;
		 **********************************/
		lStatement += " SELECT ID_POSIZIONE_GIURIDICA";
		lStatement += " FROM POSIZIONE_GIURIDICA";
		lStatement += " WHERE DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM POSIZIONE_GIURIDICA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aIdFascicoloSiep + ")";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep;

		setStatement(lStatement);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT " + " ID_POSIZIONE_GIURIDICA,"
				+ " COD_POSIZIONE_GIURIDICA, POS_GIU.RV_MEANING DESCR_POSIZIONE_GIURIDICA," + " DATA_INIZIO, "
				+ " DATA_FINE, "
				+ " COD_POSIZIONE_PROCESSUALE, POSIZIONE_PROCESSUALE.RV_MEANING DESCR_POSIZIONE_PROCESSUALE,"
				+ " NOTE," + " LUOGO_PROVA_AFFIDAMENTO, " + " LUOGO_LAVORO_SEMILIBERTA, "
				+ " COD_OPERATORE_INSERIMENTO," + " DATA_INSERIMENTO," + " COD_UFFICIO_INSERIMENTO,"
				+ " COD_OPERATORE_AGGIORNAMENTO," + " DATA_AGGIORNAMENTO," + " COD_UFFICIO_AGGIORNAMENTO,"
				+ " FAS_SIE_ID_FASCICOLO_SIEP," + " ID_EVENTO_RIFERIMENTO," + " LUOGO_ESPIAZIONE,"
				+ " AUTORITA_COMPETENTE,"
				+ " AUTORITA_COMPETENTE_SEDE, LUOGOCOMPE.DESCRIZIONE  AUTORITA_COMPETENTE_SEDE_DESC, "
				+ " AUTORITA_COMPETENTE_INDIRIZZO, ALT_CAU_ID_ALTRA_CAUSA, " + " COD_MASCHERA, "
				+ " POS_GIU.RV_ALT5_VALUE as COD_MASCHERA_CG "; // MEV_2023-33
		lStatement += " FROM POSIZIONE_GIURIDICA, CG_REF_CODES POS_GIU,";
		lStatement += " CG_REF_CODES POSIZIONE_PROCESSUALE, COMUNE LUOGOCOMPE ";
		lStatement += " WHERE POS_GIU.RV_DOMAIN = 'POSIZIONE_GIURIDICA' AND POS_GIU.RV_LOW_VALUE = POSIZIONE_GIURIDICA.COD_POSIZIONE_GIURIDICA";
		lStatement += " AND POSIZIONE_PROCESSUALE.RV_DOMAIN = 'POSIZIONE_PROCESSUALE' AND POSIZIONE_PROCESSUALE.RV_LOW_VALUE = POSIZIONE_GIURIDICA.COD_POSIZIONE_PROCESSUALE";
		lStatement += " AND LUOGOCOMPE.COD_COMUNE (+) = AUTORITA_COMPETENTE_SEDE ";

		return lStatement;
	}

	public BigDecimal getIdPosizioneGiuridica() throws DAOException {
		return getBigDecimal("ID_POSIZIONE_GIURIDICA");
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		PosizioneGiuridicaModel aModel = new PosizioneGiuridicaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdPosizioneGiuridica(getBigDecimal("ID_POSIZIONE_GIURIDICA"));
		aModel.setCodPosizioneGiuridica(getString("COD_POSIZIONE_GIURIDICA"));
		aModel.setDescrPosizioneGiuridica(getString("DESCR_POSIZIONE_GIURIDICA"));
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setCodPosizioneProcessuale(getString("COD_POSIZIONE_PROCESSUALE"));
		aModel.setDescrPosizioneProcessuale(getString("DESCR_POSIZIONE_PROCESSUALE"));
		aModel.setNote(getString("NOTE"));
		aModel.setLuogoProvaAffidamento(getString("LUOGO_PROVA_AFFIDAMENTO"));
		aModel.setLuogoLavoroSemiliberta(getString("LUOGO_LAVORO_SEMILIBERTA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setIdEventoRiferimento(getBigDecimal("ID_EVENTO_RIFERIMENTO"));

		aModel.setLuogoEspiazione(getString("LUOGO_ESPIAZIONE"));
		aModel.setAutoritaCompetente(getString("AUTORITA_COMPETENTE"));
		aModel.setAutoritaCompetenteSede(getString("AUTORITA_COMPETENTE_SEDE"));
		aModel.setAutoritaCompetenteSedeDesc(getString("AUTORITA_COMPETENTE_SEDE_DESC"));
		aModel.setAutoritaCompetenteIndirizzo(getString("AUTORITA_COMPETENTE_INDIRIZZO"));
		aModel.setCodMaschera(getString("COD_MASCHERA"));
		aModel.setAltCauIdAltraCausa(getBigDecimal("ALT_CAU_ID_ALTRA_CAUSA"));

		// MEV_2023-33
		aModel.setCodMascheraCG(getString("COD_MASCHERA_CG"));

		return aModel;
	}

	public String setCondizione(PosizioneGiuridicaModel aModel) {
		String lStatement = new String();

		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
		}
		if (aModel.getDataInizio() != null) {
			lStatement += " AND DATA_INIZIO=TO_DATE('"
					+ DateUtils.getDateToString(aModel.getDataInizio(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		}

		return lStatement;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_POSIZIONE_GIURIDICA = " + aKey;
	}

	public String setCondizioniByIdFascicolo(BigDecimal aIdFascicolo) {
		return " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
	}

	public String setCondizioniByIdEvento(BigDecimal aIdEvento) {
		return " AND ID_EVENTO_RIFERIMENTO = " + aIdEvento;
	}

	private String setCondizioniPerIdFascicoloCorrente(PosizioneGiuridicaModel aModel) {
		String lCondizioni = new String("");

		if ((aModel.getFasSieIdFascicoloSiep() != null)
				&& (aModel.getFasSieIdFascicoloSiep().doubleValue() != 0)) {
			/*******************
			 * //****** PROBLEMA ORDINE SEQUENCE ID_POSIZIONE!!!!! lCondizioni += " AND
			 * FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep(); lCondizioni += " AND
			 * DATA_FINE IS NULL";
			 ******************************/
			lCondizioni += " AND DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM POSIZIONE_GIURIDICA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
					+ aModel.getFasSieIdFascicoloSiep() + ")";
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		}

		return lCondizioni;
	}

	private String setCondizioniPosGiuCorrenteByIdFascicolo(BigDecimal aIdFascicolo) {
		String lCondizioni = new String("");

		/*
		 * CONDIZIONE CHE SI BASA SULLE DATE VALIDITA' GESTITE OBLIGATORIAMENTE
		 *
		 * lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo; lCondizioni +=
		 * " AND DATA_FINE IS NULL";
		 */

		// CONTROLLO CHE SI BASA SULLE DATE VALIDITA' NON GESTITE OBBLIGATORIAMENTE
		// **** //****** PROBLEMA ORDINE SEQUENCE ID_POSIZIONE!!!!
		// ****lCondizioni += " AND ID_POSIZIONE_GIURIDICA = ( SELECT MAX(ID_POSIZIONE_GIURIDICA) FROM
		// POSIZIONE_GIURIDICA WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo + ")";
		lCondizioni += " AND DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM POSIZIONE_GIURIDICA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aIdFascicolo + ")";
		lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		return lCondizioni;
	}

	private String setCondizioniPosGiuCorrenteByIdFascicoloDataFineNull(BigDecimal aIdFascicolo) {
		String lCondizioni = new String("");

		lCondizioni += " AND DATA_FINE IS NULL";
		lCondizioni += " AND DATA_INSERIMENTO = ( SELECT MAX(DATA_INSERIMENTO) FROM POSIZIONE_GIURIDICA WHERE FAS_SIE_ID_FASCICOLO_SIEP = "
				+ aIdFascicolo + ")";
		lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

		return lCondizioni;
	}

	/**
	 * Aggiorna ed inserisce la posizione giuridica e se aLuogoDetenzione = 'S' aggancia il luogo detenzione
	 * precedente alla nuova posizione giuridica
	 *
	 * @param lConn
	 * @param lPosizione
	 * @param lPos
	 * @param lData
	 * @param lEveModel
	 * @param aKeyFasc
	 * @param aKeyEve
	 * @param aLuogoDetenzione
	 * @return lIdPosizioneGiuridica
	 * @throws DAOException
	 * @throws F3BException
	 */
	public BigDecimal inserimentoAggiornamentoPosizioneGiuridica(Connection lConn, String lPosizione,
			PosizioneGiuridicaModel lPos, Date lData, EventoModel lEveModel, BigDecimal aKeyFasc,
			BigDecimal aKeyEve, String aLuogoDetenzione) throws DAOException, F3BException {

		PosizioneGiuridicaDAO lPosDao = null;
		LuogoDetenzioneDAO lLuogoDAO = null;
		LuogoDetenzioneSqlDAO lLuogoSql = null;
		BigDecimal lIdPosizioneGiuridica = null;

		try {
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lLuogoDAO = new LuogoDetenzioneDAO(lConn);
			lLuogoSql = new LuogoDetenzioneSqlDAO(lConn);

			if (lPos != null && lPos.getDataFine() == null) {
				lPosDao.setDataFine(lData);
				lPosDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lPosDao.setDataAggiornamento(
						lEveModel.getDataAggiornamento() != null ? lEveModel.getDataAggiornamento()
								: lEveModel.getDataInserimento());
				lPosDao.setCondizioneUpdate(lPos.getIdPosizioneGiuridica());
				lPosDao.update();
				lPosDao.stop();
			}
			if (lPosizione != null) {
				lPosDao.setCodPosizioneGiuridica(lPosizione);
				lPosDao.setDataInizio(lData);
				lPosDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(
						lEveModel.getDataAggiornamento() != null ? lEveModel.getDataAggiornamento()
								: lEveModel.getDataInserimento());
				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lPosDao.setFasSieIdFascicoloSiep(aKeyFasc);
				lPosDao.setIdEventoRiferimento(aKeyEve);

				lIdPosizioneGiuridica = lPosDao.insert();
				lPosDao.stop();
			}

			if (lIdPosizioneGiuridica != null && "S".equals(aLuogoDetenzione)) {
				LuogoDetenzioneModel lLuoDet = null;
				lLuogoSql.ricercaLuogoDetenzioneByIdPosizione(lPos.getIdPosizioneGiuridica());
				lLuoDet = (LuogoDetenzioneModel) lLuogoSql.getModelByKey();

				if (lLuoDet != null && lLuoDet.getIdLuogoDetenzione() != null) {
					lLuogoDAO.setCondizioneUpdate(lLuoDet.getIdLuogoDetenzione());
					lLuogoDAO.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
					lLuogoDAO.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
					lLuogoDAO.setDataAggiornamento(DateUtils.getSysDate());
					lLuogoDAO.setDataFineDetenzione(DateUtils.getSysDate());
					lLuogoDAO.update();
					lLuogoDAO.stop();

					lLuogoDAO.setDAOFromModel(lLuoDet);
					lLuogoDAO.setDataInserimento(DateUtils.getSysDate());
					lLuogoDAO.setDataInizioDetenzione(DateUtils.getSysDate());
					lLuogoDAO.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
					lLuogoDAO.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());

					lLuogoDAO.setPosGiuIdPosizioneGiuridica(lIdPosizioneGiuridica);
					lLuogoDAO.insert();
					lLuogoDAO.stop();
				}
			}
		} finally {
			cleanup(lPosDao);
			cleanup(lLuogoDAO);
			cleanup(lLuogoSql);
		}

		return lIdPosizioneGiuridica;
	}

}