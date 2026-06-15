package siap.siep.calcolopenadl92.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.calcolopenadl92.model.CalcoloPenaDL92ModelDB;

/**
 * SqlDAO di accesso alla tabella CALCOLO_PENA_DL92
 *
 * @since MEV_2026-1
 */
public class CalcoloPenaDL92SqlDAO extends SIAPSqlDAO {

	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public CalcoloPenaDL92SqlDAO(Connection con) {
		super(con);
	}

	protected String getSqlQuery() {

		String lStatement = new String("");

		lStatement += "SELECT ID_CALCOLO_PENA_DL92, FAS_SIE_ID_FASCICOLO_SIEP ";
		lStatement += ", NUM_ANNI_RECLUSIONE, NUM_MESI_RECLUSIONE, NUM_GIORNI_RECLUSIONE, IMPORTO_MULTA ";
		lStatement += ", NUM_ANNI_ARRESTO, NUM_MESI_ARRESTO, NUM_GIORNI_ARRESTO, IMPORTO_AMMENDA ";
		lStatement += ", NUM_ANNI_PRESOFFERTO, NUM_MESI_PRESOFFERTO, NUM_GIORNI_PRESOFFERTO ";
		lStatement += ", POSIZIONE_GIURIDICA, DATA_INIZIO_PENA ";
		lStatement += ", NUM_ANNI_DA_ESPIARE, NUM_MESI_DA_ESPIARE, NUM_GIORNI_DA_ESPIARE ";
		lStatement += ", SEMESTRI_UTILI, NUM_GG_LA_MAT_IN_PENA_RES, LA_FUNGIBILI, LA_NON_CONCESSE ";
		lStatement += ", NUM_ANNI_PENA_IPOTETICA, NUM_MESI_PENA_IPOTETICA, NUM_GIORNI_PENA_IPOTETICA ";
		lStatement += ", SEMESTRI_UTILI_PENA_SCONTATA, LA_MATURATE, LA_APPLICATE ";
		lStatement += ", DATA_SCARC_NO_LA, DATA_SCARC_LA_FUNG, DATA_SCARC_LA_NO_FUNG, DATA_SCARC_PENULTIMO_SEM ";
		lStatement += ", COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO ";
		lStatement += ", COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += " FROM CALCOLO_PENA_DL92 ";
		lStatement += " WHERE 1=1 ";

		return lStatement;
	}

	public void ricercaCalcoloPenaDL92ById(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " AND ID_CALCOLO_PENA_DL92 = " + aKey;
		setStatement(lSql);
	}

	public void ricercaCalcoloPenaDL92ByIdFasc(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lSql += " ORDER BY DATA_INSERIMENTO ";
		setStatement(lSql);
	}

	public GenericModel getModel() throws DAOException {

		CalcoloPenaDL92ModelDB aModel = new CalcoloPenaDL92ModelDB();

		aModel.setIdCalcoloPenaDL92(getBigDecimal("ID_CALCOLO_PENA_DL92"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));

		aModel.setNumAnniReclusione(getBigDecimal("NUM_ANNI_RECLUSIONE"));
		aModel.setNumMesiReclusione(getBigDecimal("NUM_MESI_RECLUSIONE"));
		aModel.setNumGiorniReclusione(getBigDecimal("NUM_GIORNI_RECLUSIONE"));
		aModel.setImportoMulta(getBigDecimal("IMPORTO_MULTA"));

		aModel.setNumAnniArresto(getBigDecimal("NUM_ANNI_ARRESTO"));
		aModel.setNumMesiArresto(getBigDecimal("NUM_MESI_ARRESTO"));
		aModel.setNumGiorniArresto(getBigDecimal("NUM_GIORNI_ARRESTO"));
		aModel.setImportoAmmenda(getBigDecimal("IMPORTO_AMMENDA"));

		aModel.setNumAnniPresofferto(getBigDecimal("NUM_ANNI_PRESOFFERTO"));
		aModel.setNumMesiPresofferto(getBigDecimal("NUM_MESI_PRESOFFERTO"));
		aModel.setNumGiorniPresofferto(getBigDecimal("NUM_GIORNI_PRESOFFERTO"));

		aModel.setPosizioneGiuridica(getString("POSIZIONE_GIURIDICA"));
		aModel.setDataInizioPena(getDate("DATA_INIZIO_PENA"));

		aModel.setNumAnniDaEspiare(getBigDecimal("NUM_ANNI_DA_ESPIARE"));
		aModel.setNumMesiDaEspiare(getBigDecimal("NUM_MESI_DA_ESPIARE"));
		aModel.setNumGiorniDaEspiare(getBigDecimal("NUM_GIORNI_DA_ESPIARE"));

		aModel.setSemestriUtili(getBigDecimal("SEMESTRI_UTILI"));
		aModel.setNumGgLaMatInPenaRes(getBigDecimal("NUM_GG_LA_MAT_IN_PENA_RES"));
		aModel.setLaFungibili(getBigDecimal("LA_FUNGIBILI"));
		aModel.setLaNonConcesse(getBigDecimal("LA_NON_CONCESSE"));

		aModel.setNumAnniPenaIpotetica(getBigDecimal("NUM_ANNI_PENA_IPOTETICA"));
		aModel.setNumMesiPenaIpotetica(getBigDecimal("NUM_MESI_PENA_IPOTETICA"));
		aModel.setNumGiorniPenaIpotetica(getBigDecimal("NUM_GIORNI_PENA_IPOTETICA"));

		aModel.setSemestriUtiliPenaScontata(getBigDecimal("SEMESTRI_UTILI_PENA_SCONTATA"));
		aModel.setLaMaturate(getBigDecimal("LA_MATURATE"));
		aModel.setLaApplicate(getBigDecimal("LA_APPLICATE"));

		aModel.setDataScarcNoLa(getDate("DATA_SCARC_NO_LA"));
		aModel.setDataScarcLaFung(getDate("DATA_SCARC_LA_FUNG"));
		aModel.setDataScarcLaNoFung(getDate("DATA_SCARC_LA_NO_FUNG"));
		aModel.setDataScarcPenultimoSem(getDate("DATA_SCARC_PENULTIMO_SEM"));

		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));

		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		return aModel;
	}

}