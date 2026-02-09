package siap.sius.scadenzario.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.Utils;
import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;

/**
 * ScadenzarioSiusSqlDAO - Classe SqlDAO che rappresenta la tabella ScadenzarioSius
 *
 * @version 1.0
 */
public class ScadenzarioSiusSqlDAO extends SIAPSqlDAO {

	public ScadenzarioSiusSqlDAO(Connection con) {

		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaScadenzarioSius(ScadenzarioSiusModel aModel) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaScadenzarioSiusByKey(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaScadenzarioSiusByIdFascicoloTipo(BigDecimal aIdFascicolo, String aTipo)
			throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdFascicoloTipo(aIdFascicolo, aTipo);
		setStatement(lSql);
	}

	public void ricercaScadenzarioSiusPerDate(Date aData1, Date aData2) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioniPerDate(aData1, aData2);
		setStatement(lSql);
	}

	// Ticket#202305250112 - aggiunto filtro per codice ufficio
	public void ricercaScadenzarioSiusPerTipoDate(String aTipoScadenzario, Date aData1, Date aData2,
			String aCodUfficio) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioniPerTipoDate(aTipoScadenzario, aData1, aData2);

		// Ticket#202305250112 - aggiunto filtro per codice ufficio
		if (aCodUfficio != null)
			lSql += " AND COD_UFFICIO_INSERIMENTO = '" + aCodUfficio + "' ";
		// Ticket#202305250112 - FINE

		// Modifica del 17/11/2016 MEV_50
		// Aggiunto ordinamento per "Data Scadenza"
		lSql += " ORDER BY DATA_FINE_SCADENZA";
		setStatement(lSql);
	}

	protected String getSqlQuery() {

		String lStatement = new String("");
		lStatement += "SELECT ID_SCADENZARIO_SIUS, "
				+ "COD_TIPO_SCADENZARIO, TIPSCA.RV_MEANING DESCR_TIPO_SCADENZARIO, "
				+ "DATA_INIZIO_SCADENZA, DATA_FINE_SCADENZA, FLAG_VISTO, DATA_VISTO, "
				+ "COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS, EVE_ID_EVENTO, "
				+ "(DATA_FINE_SCADENZA-TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) RESIDUO";
		lStatement += " FROM SCADENZARIO_SIUS, CG_REF_CODES TIPSCA ";
		lStatement += " WHERE TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO' AND TIPSCA.RV_LOW_VALUE = COD_TIPO_SCADENZARIO ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {

		ScadenzarioSiusModel aModel = new ScadenzarioSiusModel();
		// Inserire le opportune set delle descrizioni!
		aModel.setIdScadenzarioSius(getBigDecimal("ID_SCADENZARIO_SIUS"));
		aModel.setCodTipoScadenzario(getString("COD_TIPO_SCADENZARIO"));
		aModel.setDescrTipoScadenzario("DESCR_TIPO_SCADENZARIO");
		aModel.setDataInizioScadenza(getDate("DATA_INIZIO_SCADENZA"));
		aModel.setDataFineScadenza(getDate("DATA_FINE_SCADENZA"));
		aModel.setFlagVisto(getString("FLAG_VISTO"));
		aModel.setDataVisto(getDate("DATA_VISTO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDescrUfficioInserimento("");
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setDescrUfficioAggiornamento("");
		aModel.setGiorniResidui(getBigDecimal("RESIDUO"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		return aModel;
	}

	public String setCondizione(ScadenzarioSiusModel aModel) {

		String lCondizioni = new String();
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {

		return " AND ID_SCADENZARIO_SIUS = " + aKey;
	}

	public String setCondizioniByIdFascicoloTipo(BigDecimal aIdFascicolo, String aTipo) {

		String lStatement = new String();

		lStatement += " AND FAS_SIU_ID_FASCICOLO_SIUS = '" + aIdFascicolo + "'";
		lStatement += " AND COD_TIPO_SCADENZARIO = " + aTipo;

		return lStatement;
	}

	public String setCondizioniPerDate(Date aData1, Date aData2) {

		String lCondizioni = new String();
		if (aData1 != null) {
			lCondizioni += " AND DATA_FINE_SCADENZA >= TO_DATE("
					+ DateUtils.getDateToString(aData1, "yyyyMMdd") + ",'YYYYMMDD')";
		}
		if (aData2 != null) {
			lCondizioni += " AND DATA_FINE_SCADENZA <= TO_DATE("
					+ DateUtils.getDateToString(aData2, "yyyyMMdd") + ",'YYYYMMDD')";
		}
		return lCondizioni;
	}

	public String setCondizioniPerTipoDate(String aTipoScadenzario, Date aData1, Date aData2) {

		String lCondizioni = new String();
		lCondizioni += " AND COD_TIPO_SCADENZARIO = '" + aTipoScadenzario + "'";
		if (aData1 != null)
			lCondizioni += " AND DATA_FINE_SCADENZA >= TO_DATE("
					+ DateUtils.getDateToString(aData1, "yyyyMMdd") + ",'YYYYMMDD')";
		if (aData2 != null)
			lCondizioni += " AND DATA_FINE_SCADENZA <= TO_DATE("
					+ DateUtils.getDateToString(aData2, "yyyyMMdd") + ",'YYYYMMDD')";
		return lCondizioni;
	}

	public void ricercaTipiScadenzariSiusByTipoUfficio(String aTipoUfficio) throws DAOException {

		String lStatement = "SELECT RV_LOW_VALUE COD_TIPO_SCADENZARIO, RV_MEANING DESCR_TIPO_SCADENZARIO ";
		lStatement += "FROM CG_REF_CODES TIPSCA ";
		lStatement += "WHERE TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO' ";
		lStatement += "AND (TIPSCA.RV_ABBREVIATION = 'SIUS' ";
		lStatement += "OR TIPSCA.RV_ABBREVIATION = '" + aTipoUfficio + "')";

		setStatement(lStatement);
	}

	/**
	 * Metodi per la Ricerca Fine Pena Procedimenti Pendenti Paginata
	 *
	 * @author sgioggi
	 * @since MEV_2026-1
	 */
	public void ricercaFinePenaProcedimentiPendentiPaginata(String riferimento, BigDecimal ai, BigDecimal ni,
			BigDecimal af, BigDecimal nf, Date dii, Date dif, Date dsi, Date dsf, String codUfficio)
			throws DAOException {

		final String codStatoFascicolo = "('01', '05', '07')";
		final boolean test = "reale".equals(riferimento);

		String lStatement = "SELECT DISTINCT FSIUS.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS,"
				+ " FSIUS.CHIAVE_ANNO ANNO_SIUS, FSIUS.CHIAVE_PROGR PROGR_SIUS,"
				+ " SOGG.ID_SOGGETTO, SOGG.COGNOME, SOGG.NOME,"
				+ " SOGG.COD_COMUNE_NASCITA, DESCR_COMUNE_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA,"
				+ " SOGG.COD_PROVINCIA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO,"
				+ " DESCR_COMUNE_NASCITA.DESCRIZIONE DESCR_PROVINCIA_NASCITA, SOGG.COD_STATO_NASCITA,"
				+ " DESCR_STATO_NASCITA.RV_MEANING DESCR_STATO_NASCITA,"
				+ " SOGG.DATA_NASCITA, PG.COD_POSIZIONE_GIURIDICA,"
				+ " DESCR_POSIZIONE_GIURIDICA.RV_MEANING DESCR_POSIZIONE_GIURIDICA,"
				+ " GP.COD_OGGETTO_PROCEDIMENTO,"
				+ " DESCR_OGGETTO_PROCEDIMENTO.RV_MEANING CONTENUTO, PR.DATA_INIZIO,"
				// + " PR.DATA_FINE,"
				+ " nvl(PR.DATA_FINE, to_date('01/01/0001', 'dd/MM/yyyy')) DATA_FINE,"
				+ " TRUNC(PR.DATA_FINE - SYSDATE) GIORNI_RESIDUI, FSIEP.ID_FASCICOLO_SIEP,"
				+ " FSIEP.CHIAVE_ANNO ANNO_SIEP, FSIEP.CHIAVE_PROGR PROGR_SIEP,"
				+ " DESCR_COMUNE_UFFICIO_SIEP.DESCRIZIONE DESCR_COMUNE_UFFICIO_SIEP,"
				+ " UFFSIEP.COD_TIPO_UFFICIO,"
				// + " CP.DATA_SCARC_LA_FUNG FINE_PENA_VIRTUALE,"
				+ " nvl(CP.DATA_SCARC_LA_FUNG, to_date('01/01/0001', 'dd/MM/yyyy')) FINE_PENA_VIRTUALE,"
				+ " TRUNC(CP.DATA_SCARC_LA_FUNG - SYSDATE) GIORNI_RESIDUI_VIRTUALI"
				+ " FROM FASCICOLO_SIUS FSIUS,"
				+ " SOGGETTO SOGG, GENERALE_PROCEDIMENTO GP, CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,"
				+ " UFFICIO UFF, COMUNE DESCR_COMUNE_UFFICIO, COMUNE DESCR_COMUNE_NASCITA,"
				+ " PENA_RESIDUA PR, POSIZIONE_GIURIDICA PG,"
				+ " CG_REF_CODES DESCR_POSIZIONE_GIURIDICA, CG_REF_CODES DESCR_STATO_NASCITA,"
				+ " UFFICIO UFFSIEP, COMUNE DESCR_COMUNE_UFFICIO_SIEP, FASCICOLO_SIEP FSIEP";
		lStatement += " LEFT OUTER JOIN (select FAS_SIE_ID_FASCICOLO_SIEP,"
				+ " max(DATA_INSERIMENTO) as MAX_DATA_INSERIMENTO from CALCOLO_PENA_DL92"
				+ " where DATA_SCARC_LA_FUNG IS NOT NULL group by FAS_SIE_ID_FASCICOLO_SIEP) cp2"
				+ " on cp2.FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP"
				+ " LEFT OUTER JOIN CALCOLO_PENA_DL92 CP"
				+ " on (CP.FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP AND"
				+ " CP.DATA_SCARC_LA_FUNG IS NOT NULL and"
				+ " cp.data_inserimento = cp2.MAX_DATA_INSERIMENTO)";
		lStatement += " WHERE FSIUS.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO"
				+ " AND FSIUS.FAS_SIE_ID_FASCICOLO_SIEP is not null"
				+ " AND FSIUS.FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP"
				+ " AND PR.FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP"
				+ " AND UFFSIEP.COD_UFFICIO = FSIEP.CHIAVE_UFFICIO"
				+ " AND UFFSIEP.COD_COMUNE = DESCR_COMUNE_UFFICIO_SIEP.COD_COMUNE"
				+ " AND PR.FLAG_VALIDATO = 'S'"
				+ " AND PR.DATA_INSERIMENTO = (SELECT MAX(DATA_INSERIMENTO) FROM PENA_RESIDUA WHERE"
				+ " FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP AND FLAG_VALIDATO = 'S')"
				+ " AND PG.FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP AND PG.DATA_FINE IS NULL"
				+ " AND PG.DATA_INSERIMENTO = (SELECT MAX(DATA_INSERIMENTO) FROM POSIZIONE_GIURIDICA WHERE"
				+ " FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP)"
				+ " AND DESCR_POSIZIONE_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA'"
				+ " AND DESCR_POSIZIONE_GIURIDICA.RV_LOW_VALUE = PG.COD_POSIZIONE_GIURIDICA"
				+ " AND DESCR_STATO_NASCITA.RV_DOMAIN = 'NAZIONE'"
				+ " AND DESCR_STATO_NASCITA.RV_LOW_VALUE = SOGG.COD_STATO_NASCITA"
				+ " AND FSIUS.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS"
				+ " AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'"
				+ " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE"
				+ " AND UFF.COD_UFFICIO = FSIUS.CHIAVE_UFFICIO"
				+ " AND UFF.COD_COMUNE = DESCR_COMUNE_UFFICIO.COD_COMUNE"
				+ " AND SOGG.COD_COMUNE_NASCITA = DESCR_COMUNE_NASCITA.COD_COMUNE";
		if (Utils.isPresent(dsi)) {
			if (!test) {
				lStatement += " AND CP.DATA_SCARC_LA_FUNG >= TO_DATE("
						+ DateUtils.getDateToString(dsi, "yyyyMMdd") + ",'YYYYMMDD')";
			} else {
				lStatement += " AND PR.DATA_FINE >= TO_DATE(" + DateUtils.getDateToString(dsi, "yyyyMMdd")
						+ ",'YYYYMMDD')";
			}
		}
		if (Utils.isPresent(dsf)) {
			if (!test) {
				lStatement += " AND CP.DATA_SCARC_LA_FUNG <= TO_DATE("
						+ DateUtils.getDateToString(dsf, "yyyyMMdd") + ",'YYYYMMDD')";
			} else {
				lStatement += " AND PR.DATA_FINE <= TO_DATE(" + DateUtils.getDateToString(dsf, "yyyyMMdd")
						+ ",'YYYYMMDD')";
			}
		}
		lStatement += " AND TO_CHAR(FSIUS.DATA_INSERIMENTO, 'YYYYMMDD') <= TO_CHAR(sysdate, 'YYYYMMDD')"
				+ " AND UFF.COD_UFFICIO = nvl('" + codUfficio + "', UFF.COD_UFFICIO)";
		if (Utils.isPresent(dii)) {
			lStatement += " AND TO_CHAR(FSIUS.DATA_ISCRIZIONE, 'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(dii, "yyyyMMdd") + "'"
					+ " AND TO_CHAR(FSIUS.DATA_ISCRIZIONE, 'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(dif, "yyyyMMdd") + "'";
		} else {
			lStatement += " AND FSIUS.CHIAVE_ANNO >= " + ai + " AND FSIUS.CHIAVE_ANNO <= " + af
					+ " AND FSIUS.CHIAVE_PROGR >= " + ni + " AND FSIUS.CHIAVE_PROGR <= " + nf;
		}
		lStatement += " AND FSIUS.COD_STATO_FASCICOLO not in " + codStatoFascicolo;
		// lStatement += " ORDER BY FSIUS.CHIAVE_ANNO, FSIUS.CHIAVE_PROGR";
		if (!test) {
			// lStatement += " ORDER BY CP.DATA_SCARC_LA_FUNG desc, PR.DATA_FINE desc";
			lStatement += " ORDER BY FINE_PENA_VIRTUALE desc, DATA_FINE desc";
		} else {
			// lStatement += " ORDER BY PR.DATA_FINE desc";
			lStatement += " ORDER BY DATA_FINE desc";
		}

		setStatement(lStatement);
	}

	// creato modello ad hoc per scadenzario fine pena
	public ScadenzarioSiusModel getFinePenaModel() throws DAOException {

		ScadenzarioSiusModel ssm = new ScadenzarioSiusModel();

		FascicoloSiusModel fsm = new FascicoloSiusModel();
		fsm.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		fsm.setChiaveAnno(getBigDecimal("ANNO_SIUS"));
		fsm.setChiaveProgr(getBigDecimal("PROGR_SIUS"));
		ssm.setFascicoloSius(fsm);
		SoggettoModel sm = new SoggettoModel();
		sm.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		sm.setCognome(getString("COGNOME"));
		sm.setNome(getString("NOME"));
		sm.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		sm.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		sm.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		sm.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA"));
		sm.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		sm.setDescrStatoNascita(getString("DESCR_STATO_NASCITA"));
		sm.setDataNascita(getDate("DATA_NASCITA"));
		sm.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		fsm.setSoggetto(sm);
		PosizioneGiuridicaModel pgm = new PosizioneGiuridicaModel();
		pgm.setCodPosizioneGiuridica(getString("COD_POSIZIONE_GIURIDICA"));
		pgm.setDescrPosizioneGiuridica(getString("DESCR_POSIZIONE_GIURIDICA"));
		ssm.setPosizioneGiuridica(pgm);
		GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
		gpm.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		gpm.setDescrOggettoProcedimento(getString("CONTENUTO"));
		ssm.setGeneraleProcedimento(gpm);
		ssm.setDataInizioScadenza(getDate("DATA_INIZIO"));
		ssm.setDataFineScadenza(getDate("DATA_FINE"));
		ssm.setGiorniResidui(getBigDecimal("GIORNI_RESIDUI"));
		if (findColumn("FINE_PENA_VIRTUALE"))
			ssm.setDataFinePenaVirtuale(getDate("FINE_PENA_VIRTUALE"));
		if (findColumn("GIORNI_RESIDUI_VIRTUALI"))
			ssm.setGiorniResiduiVirtuali(getBigDecimal("GIORNI_RESIDUI_VIRTUALI"));
		RiferimentoFascicoloSiepModel rfsm = new RiferimentoFascicoloSiepModel();
		rfsm.setFasSieIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		rfsm.setAnnoFascicoloSiep(getBigDecimal("ANNO_SIEP"));
		rfsm.setProgrFascicoloSiep(getBigDecimal("PROGR_SIEP"));
		rfsm.setDescrUffFascicoloSiep(getString("DESCR_COMUNE_UFFICIO_SIEP"));
		rfsm.setCodUffFascicoloSiep(getString("COD_TIPO_UFFICIO"));
		ssm.setRiferimentoFascicoloSiep(rfsm);

		// modello di ritorno
		return ssm;
	}
	// FINE MEV_2026-1

}