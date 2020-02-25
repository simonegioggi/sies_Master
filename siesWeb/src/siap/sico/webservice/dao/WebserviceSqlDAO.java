package siap.sico.webservice.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.webservice.model.DatiNscToSiesModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.model.ReatoModel;

public class WebserviceSqlDAO extends SIAPSqlDAO {
	public WebserviceSqlDAO(Connection aCon) {
		super(aCon);
	}

	/**
	 * MEV 16: aggiunto parametri di passaggio per differenziare collegato al cumulo
	 *
	 * @param aDatiNscToSiesModel
	 * @param isForCumulo
	 * @param idIstruttoriaCumulo
	 * @throws DAOException
	 */
	public void ricercaSoggettoWebServices(DatiNscToSiesModel aDatiNscToSiesModel, boolean isForCumulo,
			String idIstruttoriaCumulo) throws DAOException {
		String lStatement = "";

		// MEV 16 CUMULO: modificata query solo per cumulo
		if (isForCumulo) {
			lStatement = "SELECT DISTINCT null as ID_FASCICOLO_SIEP, null as CHIAVE_ANNO, null as CHIAVE_PROGR, S.ID_SOGGETTO_ORIGINE ";
			lStatement += "FROM SOGGETTO SS";
		} else {
			lStatement = "SELECT DISTINCT FS.ID_FASCICOLO_SIEP, FS.CHIAVE_ANNO, FS.CHIAVE_PROGR ";
			lStatement += ", S.ID_SOGGETTO ";
			lStatement += "FROM FASCICOLO_SIEP FS";
		}
		if (isForCumulo) {
			lStatement += ", ISTRUTTORIA_CUMULO IC, SOGGETTO_CUMULATO S, TITOLO_CUMULATO TC ";
			lStatement += "WHERE SS.ID_SOGGETTO = S.ID_SOGGETTO_ORIGINE ";
			lStatement += "AND TC.ID_SENTENZA_ORIGINE = "
					+ aDatiNscToSiesModel.getSentenzaModel().getIdSentenza();
			lStatement += " AND TC.ISTR_ID_ISTRUTTORIA_CUMULO = IC.ID_ISTRUTTORIA_CUMULO ";
			lStatement += "AND IC.ID_ISTRUTTORIA_CUMULO = '" + idIstruttoriaCumulo + "' ";
		} else {
			lStatement += ", SOGGETTO S ";
			lStatement += "WHERE FS.SOG_ID_SOGGETTO = S.ID_SOGGETTO ";
			lStatement += "AND FS.SEN_ID_SENTENZA = '"
					+ aDatiNscToSiesModel.getSentenzaModel().getIdSentenza() + "' ";
		}
		lStatement += "AND UPPER(s.cognome) = '"
				+ aDatiNscToSiesModel.getSoggettoModel().getCognome().toUpperCase() + "' ";
		lStatement += "AND UPPER(s.nome) = '" + aDatiNscToSiesModel.getSoggettoModel().getNome().toUpperCase()
				+ "' ";
		if (aDatiNscToSiesModel.getSoggettoModel().getDataNascita() != null) {
			lStatement += " AND TO_CHAR(s.data_nascita,'YYYYMMDD') = '" + DateUtils.getDateToString(
					aDatiNscToSiesModel.getSoggettoModel().getDataNascita(), "yyyyMMdd") + "' ";
		}
		lStatement += "AND s.cod_comune_Nascita = '"
				+ aDatiNscToSiesModel.getSoggettoModel().getCodComuneNascita() + "' ";

		setStatement(lStatement);
	}

	/**
	 * MEV 16 CUMULO: aggiunto parametro di passaggio x gestione cumulo
	 *
	 * @param isForCumulo
	 * @return
	 * @throws DAOException
	 */
	public GenericModel getRisultatoRicercaSoggettoWebServices(boolean isForCumulo) throws DAOException {

		DatiNscToSiesModel lModel = new DatiNscToSiesModel();

		FascicoloSiepModel lFascicoloSiepModel = new FascicoloSiepModel();
		SoggettoModel lSoggettoModel = new SoggettoModel();

		// Campi prelevati dal DB e impostati sulle strutture
		lFascicoloSiepModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicoloSiepModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicoloSiepModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		// MEV 16 CUMULO: aggiunto controllo preventivo
		if (isForCumulo)
			lSoggettoModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO_ORIGINE"));
		else
			lSoggettoModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));

		lModel.setFascicoloSiepModel(lFascicoloSiepModel);
		lModel.setSoggettoModel(lSoggettoModel);
		return lModel;
	}

	public void ricercaProvinciaSedeGiudiziaria(String aCodIstatComuneNascita) throws DAOException {
		String lStatement = "";

		lStatement = "SELECT * ";
		lStatement += "FROM COMUNE ";
		lStatement += "WHERE Cod_Comune = '" + aCodIstatComuneNascita + "'";

		setStatement(lStatement);
	}

	public ComuneModel getRisultatoRicercaProvinciaSedeGiudiziaria() throws DAOException {

		ComuneModel lComuneModel = new ComuneModel();

		// Campi prelevati dal DB e impostati sulle strutture
		lComuneModel.setCodProvincia(getString("COD_PROVINCIA"));
		lComuneModel.setCodSedeGiudiziaria(getString("COD_SEDE_GIUDIZIARIA"));
		lComuneModel.setDescrizione(getString("DESCRIZIONE"));

		return lComuneModel;
	}

	public void ricercaCodUfficio(String aCodTipoUfficio, String aCodComune) throws DAOException {
		String lStatement = "";

		lStatement += " SELECT COD_UFFICIO ";
		lStatement += " FROM UFFICIO  ";
		lStatement += " WHERE COD_TIPO_UFFICIO = '" + aCodTipoUfficio + "'";
		lStatement += " AND COD_COMUNE = '" + aCodComune + "'";

		setStatement(lStatement);
	}

	public UfficioModel getRisultatoRicercaCodUfficio() throws DAOException {

		UfficioModel lUfficioModel = new UfficioModel();
		// Campi prelevati dal DB e impostati sulle strutture
		lUfficioModel.setCodUfficio(getString("COD_UFFICIO"));

		return lUfficioModel;
	}

	public void RicercaReatiInContinuazione(long aFascicoloSIEP) throws DAOException {
		String lStatement = "";

		lStatement += " SELECT * ";
		lStatement += " FROM REATO  ";
		lStatement += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicoloSIEP + "";
		lStatement += " AND ID_CONTINUAZIONE_REATO IS NOT NULL";
		lStatement += " ORDER BY ID_CONTINUAZIONE_REATO,PROGR_REATO";

		/*
		 * Query Utilizzata per non passare a NSC i Reati con COD_FONE e PERIODO_CONSUMAZIONE <> da quelli
		 * indicati nella query lStatement += "SELECT DISTINCT r.*"; lStatement += " FROM REATO r, ";
		 * lStatement += " (SELECT TIPO_CONTINUAZIONE_REATO, PROGR_REATO"; lStatement += " FROM REATO";
		 * lStatement += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicoloSIEP + ""; lStatement +=
		 * " AND COD_FONTE NOT IN ('10', '26', '23', '12', '24', '25', '14', '09', '17', '19')"; lStatement +=
		 * " AND COD_PERIODO_CONSUMAZIONE NOT IN ('05', '06', '12')"; lStatement +=
		 * " AND ID_CONTINUAZIONE_REATO IS NOT NULL) cont"; lStatement +=
		 * " WHERE r.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicoloSIEP + ""; lStatement +=
		 * " AND r.COD_FONTE NOT IN ('10', '26', '23', '12', '24', '25', '14', '09', '17', '19')"; lStatement
		 * += " AND r.COD_PERIODO_CONSUMAZIONE NOT IN ( '05', '06', '12')"; lStatement +=
		 * " AND r.ID_CONTINUAZIONE_REATO IS NOT NULL"; lStatement +=
		 * " AND r.TIPO_CONTINUAZIONE_REATO = cont.TIPO_CONTINUAZIONE_REATO"; lStatement +=
		 * " AND r.PROGR_REATO <> cont.PROGR_REATO"; lStatement +=
		 * " ORDER BY r.ID_CONTINUAZIONE_REATO,r.PROGR_REATO";
		 */
		setStatement(lStatement);
	}

	public ReatoModel getRisultatoRicercaReatiInContinuazione() throws DAOException {

		ReatoModel lReatoModel = new ReatoModel();
		// Campi prelevati dal DB e impostati sulle strutture
		lReatoModel.setIdReato(getBigDecimal("ID_REATO"));
		lReatoModel.setIdContinuazioneReato(getBigDecimal("ID_CONTINUAZIONE_REATO"));
		lReatoModel.setTipoContinuazioneReato(getString("TIPO_CONTINUAZIONE_REATO"));

		return lReatoModel;
	}

}