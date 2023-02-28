package siap.sico.residenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaFascicoloSigeModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiusModel;
import siap.sico.residenza.model.ResidenzaModel;

/**
 * Title: ResidenzaSqlDAO
 * Description: Classe SqlDAO che rappresenta la tabella Residenza
 * 
 * @version 1.0
 */
public class ResidenzaSqlDAO extends SIAPSqlDAO {

	public ResidenzaSqlDAO(Connection con) {

		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaResidenza(ResidenzaModel aModel) throws DAOException {
		String lStatement = getSqlStringResidenza();

		lStatement += setCondizioni(aModel);

		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaResidenzaByFascicolo(BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = getSqlStringRelazioneSiep();

		lStatement += " AND (RESIDENZA_FASCICOLO_SIEP.FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo + ")";
		lStatement += " AND (COD_TIPO_RESIDENZA = 'R')";
		lStatement += " AND (RESIDENZA_FASCICOLO_SIEP.DATA_FINE_VALIDITA IS NULL)";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaResidenzeDomiciliByFascicolo(BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = getSqlStringRelazioneSiep();

		lStatement += " AND (RESIDENZA_FASCICOLO_SIEP.FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo + ")";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	// 15/01/2008 Ricerca Residenze/Domicili per Fascicolo SIUS.
	public void ricercaResidenzeDomiciliByFascicoloSius(BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = getSqlStringRelazioneSius();

		lStatement += " AND (RESIDENZA_FASCICOLO_SIUS.FAS_SIU_ID_FASCICOLO_SIUS = " + aKeyFascicolo + ")";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaResidenzaByFascicoloXStampa(BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = getSqlStringRelazioneSiep();

		lStatement += " AND (RESIDENZA_FASCICOLO_SIEP.FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo + ")";
		lStatement += " AND (RESIDENZA_FASCICOLO_SIEP.DATA_FINE_VALIDITA IS NULL)";
		lStatement += " ORDER BY COD_TIPO_RESIDENZA DESC, RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaResidenzaCorrenteBySoggetto(BigDecimal aKeySoggetto) throws DAOException {
		String lStatement = getSqlStringResidenza();

		lStatement += " AND ID_RESIDENZA =" + setCondizioneResODomCorrenteByIdFascicolo("R", aKeySoggetto);

		setStatement(lStatement);
	}

	public void ricercaDomicilioByFascicolo(BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = getSqlStringRelazioneSiep();

		lStatement += " AND (RESIDENZA_FASCICOLO_SIEP.FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFascicolo + ")";
		lStatement += " AND (COD_TIPO_RESIDENZA = 'D')";
		lStatement += " AND (RESIDENZA_FASCICOLO_SIEP.DATA_FINE_VALIDITA IS NULL)";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaDomicilioCorrenteBySoggetto(BigDecimal aKeySoggetto) throws DAOException {
		String lStatement = getSqlStringResidenza();

		lStatement += " AND ID_RESIDENZA =" + setCondizioneResODomCorrenteByIdFascicolo("D", aKeySoggetto);

		setStatement(lStatement);
	}

	public void ricercaResidenzaByFascicoloSius(BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = getSqlStringRelazioneSius();

		lStatement += " AND (FASCICOLO_SIUS.ID_FASCICOLO_SIUS = " + aKeyFascicolo + ")";
		lStatement += " AND (COD_TIPO_RESIDENZA = 'R')";
		lStatement += " AND (RESIDENZA_FASCICOLO_SIUS.DATA_FINE_VALIDITA IS NULL)";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaResidenzaByFascicoloSige(BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = getSqlStringRelazioneSige();

		lStatement += " AND (FASCICOLO_SIGE.ID_FASCICOLO_SIGE = " + aKeyFascicolo + ")";
		lStatement += " AND (COD_TIPO_RESIDENZA = 'R')";
		lStatement += " AND (RESIDENZA_FASCICOLO_SIGE.DATA_FINE_VALIDITA IS NULL)";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaDomicilioByFascicoloSige(BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = getSqlStringRelazioneSige();

		lStatement += " AND (FASCICOLO_SIGE.ID_FASCICOLO_SIGE = " + aKeyFascicolo + ")";
		lStatement += " AND (COD_TIPO_RESIDENZA = 'D')";
		lStatement += " AND (RESIDENZA_FASCICOLO_SIGE.DATA_FINE_VALIDITA IS NULL)";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaDomicilioByFascicoloSius(BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = getSqlStringRelazioneSius();

		lStatement += " AND (FASCICOLO_SIUS.ID_FASCICOLO_SIUS = " + aKeyFascicolo + ")";
		lStatement += " AND (COD_TIPO_RESIDENZA = 'D')";
		lStatement += " AND (RESIDENZA_FASCICOLO_SIUS.DATA_FINE_VALIDITA IS NULL)";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	// Ricerca Residenze/Domicili associati al Fascicolo SIUS.
	public void ricercaResidenzeByFascicoloSius(BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = getSqlStringRelazioneSius();

		lStatement += " AND (FASCICOLO_SIUS.ID_FASCICOLO_SIUS = " + aKeyFascicolo + ")";

		setStatement(lStatement);
	}

	public void ricercaSiepResidenzeDomiciliByIdSoggetto(BigDecimal aIdSoggetto) throws DAOException {
		// Ricerca Residenze/Domicili associati al Soggetto
		String lStatement = getSqlStringRelazioneSiep();

		lStatement += " AND (RESIDENZA.SOG_ID_SOGGETTO = " + aIdSoggetto + ")";

		setStatement(lStatement);
	}

	public void ricercaSiusResidenzeDomiciliByIdSoggetto(BigDecimal aIdSoggetto) throws DAOException {
		// Ricerca Residenze/Domicili associati al Soggetto
		String lStatement = getSqlStringRelazioneSius();

		lStatement += " AND (RESIDENZA.SOG_ID_SOGGETTO = " + aIdSoggetto + ")";

		setStatement(lStatement);
	}

	public void ricercaResidenzaByProcedimentoSius(BigDecimal aKeyFascicolo, char aTipoResidenza)
			throws DAOException {
		String lStatement = getSqlStringRelazioneSius();

		lStatement += " AND (FASCICOLO_SIUS.ID_FASCICOLO_SIUS = " + aKeyFascicolo + ")";
		lStatement += " AND (COD_TIPO_RESIDENZA = '" + aTipoResidenza + "')";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaResidenzaByProcedimentoSige(BigDecimal aKeyFascicolo, char aTipoResidenza)
			throws DAOException {
		String lStatement = getSqlStringRelazioneSige();

		lStatement += " AND (FASCICOLO_SIGE.ID_FASCICOLO_SIGE = " + aKeyFascicolo + ")";
		lStatement += " AND (COD_TIPO_RESIDENZA = '" + aTipoResidenza + "')";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaResidenzaUltimaByProcedimentoSius(BigDecimal aKeyFascicolo, char aTipoResidenza)
			throws DAOException {
		String lStatement = getSqlStringRelazioneSius();

		lStatement += " AND (FASCICOLO_SIUS.ID_FASCICOLO_SIUS = " + aKeyFascicolo + ")";
		lStatement += " AND (COD_TIPO_RESIDENZA = '" + aTipoResidenza + "')";
		lStatement += " AND (RESIDENZA_FASCICOLO_SIUS.DATA_FINE_VALIDITA IS NULL)";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaResidenzaUltimaByProcedimentoSige(BigDecimal aKeyFascicolo, char aTipoResidenza)
			throws DAOException {
		String lStatement = getSqlStringRelazioneSige();

		lStatement += " AND (FASCICOLO_SIGE.ID_FASCICOLO_SIGE = " + aKeyFascicolo + ")";
		lStatement += " AND (COD_TIPO_RESIDENZA = '" + aTipoResidenza + "')";
		lStatement += " AND (RESIDENZA_FASCICOLO_SIGE.DATA_FINE_VALIDITA IS NULL)";
		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public void ricercaResidenzaFascicoloSiepByIdResidenzaIdFascicolo(BigDecimal aKeyResidenza,
			BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = "SELECT DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, RES_ID_RESIDENZA, FAS_SIE_ID_FASCICOLO_SIEP";
		lStatement += " FROM RESIDENZA_FASCICOLO_SIEP";
		lStatement += " WHERE RES_ID_RESIDENZA = '" + aKeyResidenza + "'";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = '" + aKeyFascicolo + "'";

		setStatement(lStatement);
	}

	public void ricercaResidenzaFascicoloSiusByIdResidenzaIdFascicolo(BigDecimal aKeyResidenza,
			BigDecimal aKeyFascicolo) throws DAOException {
		String lStatement = "SELECT DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, RES_ID_RESIDENZA, FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " FROM RESIDENZA_FASCICOLO_SIUS";
		lStatement += " WHERE RES_ID_RESIDENZA = '" + aKeyResidenza + "'";
		lStatement += " AND FAS_SIU_ID_FASCICOLO_SIUS = '" + aKeyFascicolo + "'";

		setStatement(lStatement);
	}

	public String getSqlStringRelazioneSiep() throws DAOException {
		String lStatement = "SELECT" + " ID_RESIDENZA," + " COD_STATO,"
				+ " DESCR_STATO.RV_MEANING DESCR_STATO," + " RESIDENZA.COD_PROVINCIA,"
				+ " DESCR_PROVINCIA.RV_MEANING DESCR_PROVINCIA," + " RESIDENZA.COD_COMUNE,"
				+ " COMUNE.DESCRIZIONE DESCR_COMUNE," + " RESIDENZA.CAP," + " INDIRIZZO,"
				+ " COD_TIPO_RESIDENZA," + " DESC_COMUNE_ESTERO,"
				+ " DESCR_TIPO_RESIDENZA.RV_MEANING DESCR_TIPO_RESIDENZA,"
				+ " RESIDENZA.COD_OPERATORE_INSERIMENTO," + " RESIDENZA.DATA_INSERIMENTO,"
				+ " RESIDENZA.COD_UFFICIO_INSERIMENTO," + " RESIDENZA.COD_OPERATORE_AGGIORNAMENTO,"
				+ " RESIDENZA.DATA_AGGIORNAMENTO," + " RESIDENZA.COD_UFFICIO_AGGIORNAMENTO,"
				+ " RESIDENZA.SOG_ID_SOGGETTO," + " RESIDENZA_FASCICOLO_SIEP.DATA_INIZIO_VALIDITA,"
				+ " RESIDENZA_FASCICOLO_SIEP.DATA_FINE_VALIDITA,"
				+ " RESIDENZA_FASCICOLO_SIEP.RES_ID_RESIDENZA,"
				+ " RESIDENZA_FASCICOLO_SIEP.FAS_SIE_ID_FASCICOLO_SIEP," + " RESIDENZA.FLG_DOM_AVV,"
				+ " RESIDENZA.ID_PARTE_UDIENZA," + " RESIDENZA.FLG_DOMICILIO_DIFENSORE";
		lStatement += " FROM RESIDENZA, RESIDENZA_FASCICOLO_SIEP, CG_REF_CODES DESCR_STATO, CG_REF_CODES DESCR_PROVINCIA,";
		lStatement += " COMUNE, CG_REF_CODES DESCR_TIPO_RESIDENZA";
		lStatement += " WHERE (DESCR_STATO.RV_DOMAIN = 'NAZIONE' AND DESCR_STATO.RV_LOW_VALUE = RESIDENZA.COD_STATO)";
		lStatement += " AND (DESCR_PROVINCIA.RV_DOMAIN = 'PROVINCIA' AND DESCR_PROVINCIA.RV_LOW_VALUE = RESIDENZA.COD_PROVINCIA)";
		lStatement += " AND (COMUNE.COD_COMUNE = RESIDENZA.COD_COMUNE)";
		lStatement += " AND (DESCR_TIPO_RESIDENZA.RV_DOMAIN = 'TIPO_RESIDENZA' AND DESCR_TIPO_RESIDENZA.RV_LOW_VALUE = RESIDENZA.COD_TIPO_RESIDENZA)";
		lStatement += " AND (RESIDENZA.ID_RESIDENZA = RESIDENZA_FASCICOLO_SIEP.RES_ID_RESIDENZA(+))";

		return lStatement;
	}

	public String getSqlStringRelazioneSius() throws DAOException {
		String lStatement = "SELECT" + " ID_RESIDENZA," + " COD_STATO,"
				+ " DESCR_STATO.RV_MEANING DESCR_STATO," + " RESIDENZA.COD_PROVINCIA,"
				+ " DESCR_PROVINCIA.RV_MEANING DESCR_PROVINCIA," + " RESIDENZA.COD_COMUNE,"
				+ " COMUNE.DESCRIZIONE DESCR_COMUNE," + " RESIDENZA.CAP," + " INDIRIZZO,"
				+ " DESC_COMUNE_ESTERO," + " COD_TIPO_RESIDENZA,"
				+ " DESCR_TIPO_RESIDENZA.RV_MEANING DESCR_TIPO_RESIDENZA,"
				+ " RESIDENZA.COD_OPERATORE_INSERIMENTO," + " RESIDENZA.DATA_INSERIMENTO,"
				+ " RESIDENZA.COD_UFFICIO_INSERIMENTO," + " RESIDENZA.COD_OPERATORE_AGGIORNAMENTO,"
				+ " RESIDENZA.DATA_AGGIORNAMENTO," + " RESIDENZA.COD_UFFICIO_AGGIORNAMENTO,"
				+ " RESIDENZA.SOG_ID_SOGGETTO," + " RESIDENZA_FASCICOLO_SIUS.DATA_INIZIO_VALIDITA,"
				+ " RESIDENZA_FASCICOLO_SIUS.DATA_FINE_VALIDITA,"
				+ " RESIDENZA_FASCICOLO_SIUS.FAS_SIU_ID_FASCICOLO_SIUS," + " RESIDENZA.FLG_DOM_AVV,"
				+ " RESIDENZA.ID_PARTE_UDIENZA," + " RESIDENZA.FLG_DOMICILIO_DIFENSORE";
		lStatement += " FROM RESIDENZA, FASCICOLO_SIUS, RESIDENZA_FASCICOLO_SIUS, CG_REF_CODES DESCR_STATO, CG_REF_CODES DESCR_PROVINCIA,";
		lStatement += " COMUNE, CG_REF_CODES DESCR_TIPO_RESIDENZA";
		lStatement += " WHERE (DESCR_STATO.RV_DOMAIN = 'NAZIONE' AND DESCR_STATO.RV_LOW_VALUE = RESIDENZA.COD_STATO)";
		lStatement += " AND (DESCR_PROVINCIA.RV_DOMAIN = 'PROVINCIA' AND DESCR_PROVINCIA.RV_LOW_VALUE = RESIDENZA.COD_PROVINCIA)";
		lStatement += " AND (COMUNE.COD_COMUNE = RESIDENZA.COD_COMUNE)";
		lStatement += " AND (DESCR_TIPO_RESIDENZA.RV_DOMAIN = 'TIPO_RESIDENZA' AND DESCR_TIPO_RESIDENZA.RV_LOW_VALUE = RESIDENZA.COD_TIPO_RESIDENZA)";
		lStatement += " AND (RESIDENZA.ID_RESIDENZA = RESIDENZA_FASCICOLO_SIUS.RES_ID_RESIDENZA)";
		lStatement += " AND (FASCICOLO_SIUS.ID_FASCICOLO_SIUS = RESIDENZA_FASCICOLO_SIUS.FAS_SIU_ID_FASCICOLO_SIUS)";

		return lStatement;
	}

	public String getSqlStringRelazioneSige() throws DAOException {
		String lStatement = "SELECT" + " ID_RESIDENZA," + " COD_STATO,"
				+ " DESCR_STATO.RV_MEANING DESCR_STATO," + " RESIDENZA.COD_PROVINCIA,"
				+ " DESCR_PROVINCIA.RV_MEANING DESCR_PROVINCIA," + " RESIDENZA.COD_COMUNE,"
				+ " COMUNE.DESCRIZIONE DESCR_COMUNE," + " RESIDENZA.CAP," + " INDIRIZZO,"
				+ " DESC_COMUNE_ESTERO," + " COD_TIPO_RESIDENZA,"
				+ " DESCR_TIPO_RESIDENZA.RV_MEANING DESCR_TIPO_RESIDENZA,"
				+ " RESIDENZA.COD_OPERATORE_INSERIMENTO," + " RESIDENZA.DATA_INSERIMENTO,"
				+ " RESIDENZA.COD_UFFICIO_INSERIMENTO," + " RESIDENZA.COD_OPERATORE_AGGIORNAMENTO,"
				+ " RESIDENZA.DATA_AGGIORNAMENTO," + " RESIDENZA.COD_UFFICIO_AGGIORNAMENTO,"
				+ " RESIDENZA.SOG_ID_SOGGETTO," + " RESIDENZA_FASCICOLO_SIGE.DATA_INIZIO_VALIDITA,"
				+ " RESIDENZA_FASCICOLO_SIGE.DATA_FINE_VALIDITA,"
				+ " RESIDENZA_FASCICOLO_SIGE.FAS_SIGE_ID_FASCICOLO_SIGE," + " RESIDENZA.FLG_DOM_AVV,"
				+ " RESIDENZA.ID_PARTE_UDIENZA," + " RESIDENZA.FLG_DOMICILIO_DIFENSORE";
		lStatement += " FROM RESIDENZA, FASCICOLO_SIGE, RESIDENZA_FASCICOLO_SIGE, CG_REF_CODES DESCR_STATO, CG_REF_CODES DESCR_PROVINCIA,";
		lStatement += " COMUNE, CG_REF_CODES DESCR_TIPO_RESIDENZA";
		lStatement += " WHERE (DESCR_STATO.RV_DOMAIN = 'NAZIONE' AND DESCR_STATO.RV_LOW_VALUE = RESIDENZA.COD_STATO)";
		lStatement += " AND (DESCR_PROVINCIA.RV_DOMAIN = 'PROVINCIA' AND DESCR_PROVINCIA.RV_LOW_VALUE = RESIDENZA.COD_PROVINCIA)";
		lStatement += " AND (COMUNE.COD_COMUNE = RESIDENZA.COD_COMUNE)";
		lStatement += " AND (DESCR_TIPO_RESIDENZA.RV_DOMAIN = 'TIPO_RESIDENZA' AND DESCR_TIPO_RESIDENZA.RV_LOW_VALUE = RESIDENZA.COD_TIPO_RESIDENZA)";
		lStatement += " AND (RESIDENZA.ID_RESIDENZA = RESIDENZA_FASCICOLO_SIGE.RES_ID_RESIDENZA)";
		lStatement += " AND (FASCICOLO_SIGE.ID_FASCICOLO_SIGE = RESIDENZA_FASCICOLO_SIGE.FAS_SIGE_ID_FASCICOLO_SIGE)";

		return lStatement;
	}

	protected String getSqlStringResidenza() throws DAOException {
		String lStatement = "";

		lStatement += "SELECT" + " ID_RESIDENZA," + " COD_STATO," + " DESCR_STATO.RV_MEANING DESCR_STATO,"
				+ " RESIDENZA.COD_PROVINCIA," + " DESCR_PROVINCIA.RV_MEANING DESCR_PROVINCIA,"
				+ " RESIDENZA.COD_COMUNE," + " COMUNE.DESCRIZIONE DESCR_COMUNE," + " RESIDENZA.CAP,"
				+ " INDIRIZZO," + " DESC_COMUNE_ESTERO," + " COD_TIPO_RESIDENZA,"
				+ " DESCR_TIPO_RESIDENZA.RV_MEANING DESCR_TIPO_RESIDENZA," + " COD_OPERATORE_INSERIMENTO,"
				+ " DATA_INSERIMENTO," + " COD_UFFICIO_INSERIMENTO," + " COD_OPERATORE_AGGIORNAMENTO,"
				+ " DATA_AGGIORNAMENTO," + " COD_UFFICIO_AGGIORNAMENTO," + " SOG_ID_SOGGETTO,"
				+ " RESIDENZA.FLG_DOM_AVV," + " RESIDENZA.ID_PARTE_UDIENZA,"
				+ " RESIDENZA.FLG_DOMICILIO_DIFENSORE";
		lStatement += " FROM RESIDENZA, CG_REF_CODES DESCR_STATO, CG_REF_CODES DESCR_PROVINCIA,";
		lStatement += " COMUNE, CG_REF_CODES DESCR_TIPO_RESIDENZA";
		lStatement += " WHERE (DESCR_STATO.RV_DOMAIN = 'NAZIONE' AND DESCR_STATO.RV_LOW_VALUE = RESIDENZA.COD_STATO)";
		lStatement += " AND (DESCR_PROVINCIA.RV_DOMAIN = 'PROVINCIA' AND DESCR_PROVINCIA.RV_LOW_VALUE = RESIDENZA.COD_PROVINCIA)";
		lStatement += " AND (COMUNE.COD_COMUNE = RESIDENZA.COD_COMUNE)";
		lStatement += " AND (DESCR_TIPO_RESIDENZA.RV_DOMAIN = 'TIPO_RESIDENZA' AND DESCR_TIPO_RESIDENZA.RV_LOW_VALUE = RESIDENZA.COD_TIPO_RESIDENZA)";

		return lStatement;
	}

	public void ricercaMaxIdResidenzaPerIdSoggetto(BigDecimal aIdSoggetto, String aTipoResidenza)
			throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(ID_RESIDENZA) ID_RESIDENZA";
		lStatement += " FROM RESIDENZA";
		lStatement += " WHERE SOG_ID_SOGGETTO = " + aIdSoggetto;
		lStatement += "   AND COD_TIPO_RESIDENZA = '" + aTipoResidenza + "'";

		setStatement(lStatement);
	}

	public BigDecimal getIdResidenza() throws DAOException {
		return getBigDecimal("ID_RESIDENZA");
	}

	//
	// METODO GET MODEL()
	//
	public GenericModel getModel() throws DAOException {
		ResidenzaModel aModel = new ResidenzaModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdResidenza(getBigDecimal("ID_RESIDENZA"));
		aModel.setCodStato(getString("COD_STATO"));
		aModel.setDescrStato(getString("DESCR_STATO"));
		aModel.setCodProvincia(getString("COD_PROVINCIA"));
		aModel.setDescrProvincia(getString("DESCR_PROVINCIA"));
		aModel.setCodComune(getString("COD_COMUNE"));
		aModel.setDescrComune(getString("DESCR_COMUNE"));
		aModel.setCap(getString("CAP"));
		aModel.setIndirizzo(getString("INDIRIZZO"));
		aModel.setCodTipoResidenza(getString("COD_TIPO_RESIDENZA"));
		aModel.setDescrTipoResidenza(getString("DESCR_TIPO_RESIDENZA"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		aModel.setDescComuneEstero(getString("DESC_COMUNE_ESTERO"));
		aModel.setFlgDomAvv(getString("FLG_DOM_AVV"));
		aModel.setIdParteUdienza(getBigDecimal("ID_PARTE_UDIENZA"));
		aModel.setFlgDomicilioDifensore(getString("FLG_DOMICILIO_DIFENSORE"));
		return aModel;
	}

	public ResidenzaFascicoloSiepModel getModelResidenzaFascicoloSiep() throws DAOException {
		ResidenzaFascicoloSiepModel aModel = new ResidenzaFascicoloSiepModel();

		aModel.setResIdResidenza(getBigDecimal("RES_ID_RESIDENZA"));
		aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));

		return aModel;
	}

	public ResidenzaFascicoloSiusModel getModelResidenzaFascicoloSius() throws DAOException {
		ResidenzaFascicoloSiusModel aModel = new ResidenzaFascicoloSiusModel();

		aModel.setResIdResidenza(getBigDecimal("ID_RESIDENZA"));
		aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));

		return aModel;
	}

	public ResidenzaFascicoloSigeModel getModelResidenzaFascicoloSige() throws DAOException {
		ResidenzaFascicoloSigeModel aModel = new ResidenzaFascicoloSigeModel();

		aModel.setResIdResidenza(getBigDecimal("ID_RESIDENZA"));
		aModel.setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
		aModel.setFasSigeIdFascicoloSige(getBigDecimal("FAS_SIGE_ID_FASCICOLO_SIGE"));

		return aModel;
	}

	public String setCondizioneResODomCorrenteByIdFascicolo(String aTipoResidenza, BigDecimal aIdSoggetto) {
		String lCondizioni = new String();

		lCondizioni = " (SELECT MAX(ID_RESIDENZA) FROM RESIDENZA WHERE COD_TIPO_RESIDENZA = '"
				+ aTipoResidenza + "' AND SOG_ID_SOGGETTO = " + aIdSoggetto + ")";

		return lCondizioni;
	}

	public String setCondizioni(ResidenzaModel aModel) {
		String lCondizioni = new String();

		if ((aModel.getIdResidenza() == null) || (aModel.getIdResidenza().intValue() == 0)) {
			if ((aModel.getIndirizzo() != null) && !(aModel.getIndirizzo().equals(""))) {
				lCondizioni += " AND INDIRIZZO = '" + aModel.getIndirizzo() + "'";
			}
			if ((aModel.getCap() != null) && !(aModel.getCap().equals(""))) {
				lCondizioni += " AND CAP = '" + aModel.getCap() + "'";
			}
			if ((aModel.getCodComune() != null) && !(aModel.getCodComune().equals(""))) {
				lCondizioni += " AND COD_COMUNE = '" + aModel.getCodComune() + "'";
			}
			if ((aModel.getCodProvincia() != null) && !(aModel.getCodProvincia().equals(""))) {
				lCondizioni += " AND COD_PROVINCIA = '" + aModel.getCodProvincia() + "'";
			}
			if ((aModel.getCodStato() != null) && !(aModel.getCodStato().equals(""))) {
				lCondizioni += " AND COD_STATO = '" + aModel.getCodStato() + "'";
			}
			if ((aModel.getCodTipoResidenza() != null) && !(aModel.getCodTipoResidenza().equals(""))) {
				lCondizioni += " AND COD_TIPO_RESIDENZA = '" + aModel.getCodTipoResidenza() + "'";
			}
			/*
			 * if( (aModel.getFasSieIdFascicoloSiep() != null) &&
			 * (aModel.getFasSieIdFascicoloSiep().intValue() != 0) ) { lCondizioni +=
			 * " AND FAS_SIE_ID_FASCICOLO_SIEP = "+ aModel.getFasSieIdFascicoloSiep(); } if(
			 * (aModel.getFasSiuIdFascicoloSius() != null) && (aModel.getFasSiuIdFascicoloSius().intValue() !=
			 * 0) ) { lCondizioni += " AND FAS_SIU_ID_FASCICOLO_SIUS = "+ aModel.getFasSiuIdFascicoloSius(); }
			 */
			if ((aModel.getSogIdSoggetto() != null) && (aModel.getSogIdSoggetto().intValue() != 0)) {
				lCondizioni += " AND SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto();
			}
		} else {
			lCondizioni = " AND ID_RESIDENZA = " + aModel.getIdResidenza();
		}

		return lCondizioni;
	}

	/**
	 * <p>
	 * Description: Metodo di ricerca, restituisce il numero di occorrenze nella tabella
	 * RESIDENZA_FASCICOLO_SIUS relative ad una Residenza specifica.
	 * </p>
	 *
	 * @param BigDecimal
	 *            aIdResidenza : Identificativo Residenza
	 * @return int : numero occorrenze.
	 * @throws DAOException
	 */

	public int getNumOccorrenze(BigDecimal aIdResidenza) throws DAOException {
		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from RESIDENZA_FASCICOLO_SIUS  where RES_ID_RESIDENZA = "
				+ aIdResidenza;

		setStatement(lStatement);

		this.start();
		if (this.next()) {
			lCount = this.getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	/**
	 * <p>
	 * Description: Metodo di ricerca, restituisce il numero di occorrenze nella tabella
	 * RESIDENZA_FASCICOLO_SIUS relative ad una Residenza specifica.
	 * </p>
	 *
	 * @param BigDecimal
	 *            aIdResidenza : Identificativo Residenza
	 * @return int : numero occorrenze.
	 * @throws DAOException
	 */

	public int getNumOccorrenzeResSIGE(BigDecimal aIdResidenza) throws DAOException {
		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from RESIDENZA_FASCICOLO_SIGE  where RES_ID_RESIDENZA = "
				+ aIdResidenza;

		setStatement(lStatement);

		this.start();
		if (this.next()) {
			lCount = this.getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	public void ricercaDomicilioCorrenteByIdParteUdienza(BigDecimal aKeyParteUdienza) throws DAOException {
		String lStatement = getSqlStringResidenza();

		lStatement += " AND ID_PARTE_UDIENZA = " + aKeyParteUdienza;

		setStatement(lStatement);
	}

	//
	// METODO RICERCA SIGE
	//
	public void ricercaResidenzaSige(ResidenzaModel aModel, BigDecimal idSoggettoSiep) throws DAOException {
		String lStatement = getSqlStringResidenza();

		lStatement += setCondizioneSige(aModel, idSoggettoSiep);

		lStatement += " ORDER BY RESIDENZA.ID_RESIDENZA DESC";

		setStatement(lStatement);
	}

	public String setCondizioneSige(ResidenzaModel aModel, BigDecimal idSoggettoSiep) {
		String lCondizioni = new String();

		lCondizioni += " AND (SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto() + " OR SOG_ID_SOGGETTO = "
				+ idSoggettoSiep + ")";
		lCondizioni += " AND COD_TIPO_RESIDENZA = 'D'";
		return lCondizioni;
	}

	// MEV_2023-13: aggiunto metodo di ricerca
	public void ricercaDomicilioCorrenteByIdCivilmenteObbligato(BigDecimal idCivilmenteObbligato)
			throws DAOException {

		String lStatement = getSqlStringResidenza();
		lStatement += " AND ID_CIVILMENTE_OBBLIGATO = " + idCivilmenteObbligato;
		setStatement(lStatement);
	}
	// FINE MEV_2023-13

}