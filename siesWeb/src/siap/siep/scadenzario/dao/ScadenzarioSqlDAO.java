package siap.siep.scadenzario.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.siep.scadenzario.model.ScadenzarioModel;

/**
 * <p>
 * Title: ScadenzarioSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella Scadenzario
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
public class ScadenzarioSqlDAO extends SqlDAO {

	public ScadenzarioSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaScadenzario(ScadenzarioModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		lSql += " " + setOrderByResiduo();

		setStatement(lSql);
	}

	public void getCountScadenzari(ScadenzarioModel aModel) throws DAOException {
		String lStatement = "SELECT COUNT(*) HowManyRecords ";

		lStatement += " FROM SCADENZARIO_SIEP SCA, CG_REF_CODES TIPSCA";
		lStatement += " WHERE TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO' AND TIPSCA.RV_LOW_VALUE = COD_TIPO_SCADENZARIO";
		lStatement += " " + setCondizione(aModel);

		setStatement(lStatement);
	}

	public void ricercaScadenzarioPaged(ScadenzarioModel aModel, int aPage) throws DAOException {
		String lSql = getSqlQuery();
		String lPaginedStatement = new String("");
		lSql += " " + setCondizione(aModel);
		lSql += " " + setOrderByResiduo();
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lSql
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
		setStatement(lPaginedStatement);
	}

	/**
	 * Restituisce l'elenco degli scadenzari FINE PENA per il fascicolo ordinati per data inserimento
	 * decrescente (dal più recente)
	 *
	 * @param aKey
	 *            - IdFascicolo
	 * @throws DAOException
	 */
	public void ricercaScadenzarioByIdFascicoloCorrente(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKeyCorrente(aKey);
		setStatement(lSql);
	}

	public void ricercaScadenzarioByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaScadenzarioByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdFascicolo(aIdFascicolo);

		setStatement(lSql);
	}

	public void ricercaScadenzarioByTipoScadenzarioIdFascicolo(String aTipoSca, BigDecimal aKeyFasc)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_SCADENZARIO = '" + aTipoSca + "'";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFasc;

		setStatement(lSql);
	}

	public void ricercaScadenzarioByIdEvento(BigDecimal aKeyEve) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND EVE_ID_EVENTO = " + aKeyEve;

		setStatement(lSql);
	}

	public void ricercaScadenzarioCorrenteByIdFascicoloCodTipoScadenzario(BigDecimal aIdFasc,
			String aCodTipoSca) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND COD_TIPO_SCADENZARIO = '" + aCodTipoSca + "'";
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFasc;
		lSql += " ORDER BY DATA_INSERIMENTO DESC ";

		setStatement(lSql);
	}

	public void ricercaScadenzarioByIdFascicoloIdNotifica(BigDecimal aKeyFasc, BigDecimal aKeyNot,
			String aTipSca) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND NOT_ID_NOTIFICA = " + aKeyNot;
		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKeyFasc;
		lSql += " AND COD_TIPO_SCADENZARIO = '" + aTipSca + "'";
		lSql += " ORDER BY DATA_INSERIMENTO DESC ";

		setStatement(lSql);
	}

	public void ricercaScadenzarioVerbaleArresto(ScadenzarioModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneScadenzarioVerbaleArresto(aModel);
		lSql += " " + setOrderByResiduo();

		setStatement(lSql);
	}

	public void ricercaScadenzarioFinePenaVaneRicerche(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneScadenzarioFinePenaVaneRicerche(aKey);
		lSql += " ORDER BY DATA_INSERIMENTO DESC   ";

		setStatement(lSql);
	}

	public void ricercaScadenzarioPerTipoScadenzario(String aTipoSca[], BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		if (aTipoSca.length > 0) {
			lSql += " AND COD_TIPO_SCADENZARIO IN (";
			for (int i = 0; i < aTipoSca.length; i++) {
				lSql += "'" + aTipoSca[i] + "'";
				if (aTipoSca.length > 1 && i < aTipoSca.length - 1)
					lSql += ",";

			}
			lSql += ")";
		}

		lSql += " AND FAS_SIE_ID_FASCICOLO_SIEP = '" + aKey + "'";
		lSql += " ORDER BY DATA_INSERIMENTO DESC   ";

		setStatement(lSql);
	}

	public String setCondizioniByIdFascicolo(BigDecimal aIdFascicolo) {
		return " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
	}

	private String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT" + " ID_SCADENZARIO_SIEP,"
				+ " COD_TIPO_SCADENZARIO, TIPSCA.RV_MEANING DESCR_TIPO_SCADENZARIO,"
				+ " DATA_INIZIO_SCADENZA, " + " DATA_FINE_SCADENZA, " + " FLAG_VISTO, " + " DATA_VISTO,"
				+ " COD_OPERATORE_INSERIMENTO, " + " DATA_INSERIMENTO, " + " COD_UFFICIO_INSERIMENTO,"
				+ " COD_OPERATORE_AGGIORNAMENTO, " + " DATA_AGGIORNAMENTO, " + " COD_UFFICIO_AGGIORNAMENTO,"
				+ " FAS_SIE_ID_FASCICOLO_SIEP, " + " NOT_ID_NOTIFICA, " + " EVE_ID_EVENTO,"
				+ " COD_STATO_NOTIFICA,"
				+ " (DATA_FINE_SCADENZA-TO_DATE(TO_CHAR(SYSDATE,'DD/MM/YYYY'),'DD/MM/YYYY')) RESIDUO";
		lStatement += " FROM SCADENZARIO_SIEP SCA, CG_REF_CODES TIPSCA";
		lStatement += " WHERE TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO' AND TIPSCA.RV_LOW_VALUE = COD_TIPO_SCADENZARIO";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		ScadenzarioModel aModel = new ScadenzarioModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdScadenzario(getBigDecimal("ID_SCADENZARIO_SIEP"));
		aModel.setCodTipoScadenzario(getString("COD_TIPO_SCADENZARIO"));
		aModel.setDescrTipoScadenzario(getString("DESCR_TIPO_SCADENZARIO"));
		aModel.setDataInizioScadenza(getDate("DATA_INIZIO_SCADENZA"));
		aModel.setDataFineScadenza(getDate("DATA_FINE_SCADENZA"));
		aModel.setFlagVisto(getString("FLAG_VISTO"));
		aModel.setDataVisto(getDate("DATA_VISTO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setGiorniResidui(getBigDecimal("RESIDUO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setNotIdNotifica(getBigDecimal("NOT_ID_NOTIFICA"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setCodStatoNotifica(getString("COD_STATO_NOTIFICA"));

		return aModel;
	}

	public String setCondizione(ScadenzarioModel aModel) {

		String lCondizioni = new String();

		String ldata1 = new String();
		String ldata2 = new String();

		if (aModel.getDataInizioScadenza() != null)
			ldata1 = DateUtils.getDateToString(aModel.getDataInizioScadenza(), "dd/MM/yyyy");

		if (aModel.getDataFineScadenza() != null)
			ldata2 = DateUtils.getDateToString(aModel.getDataFineScadenza(), "dd/MM/yyyy");

		if (aModel.getCodTipoScadenzario() != null) {
			lCondizioni += " AND SCA.COD_TIPO_SCADENZARIO = '" + aModel.getCodTipoScadenzario() + "'";
		}

		if (aModel.getFasSieIdFascicoloSiep() != null
				&& aModel.getFasSieIdFascicoloSiep().compareTo(new BigDecimal(0)) != 0) {
			lCondizioni += " AND SCA.FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		}

		// PER UFFICIO
		if (aModel.getCodUfficioInserimento() != null) {
			lCondizioni += " AND SCA.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
		}

		// in scadenza
		if (aModel.getTipoRic().equals("sette")) {
			lCondizioni += " AND SCA.DATA_FINE_SCADENZA BETWEEN TO_DATE('" + ldata1
					+ "','DD/MM/YYYY') AND TO_DATE('" + ldata2 + "','DD/MM/YYYY')";
		}

		// scaduti
		if (aModel.getTipoRic().equals("scaduto")) {
			lCondizioni += " AND SCA.DATA_FINE_SCADENZA < TO_DATE('" + ldata1 + "','DD/MM/YYYY')";
		}

		// oggi
		if (aModel.getTipoRic().equals("oggi")) {
			lCondizioni += " AND SCA.DATA_FINE_SCADENZA = TO_DATE('" + ldata2 + "','DD/MM/YYYY')";
		}
		// PER STATO NOTIFICA
		if (aModel.getCodStatoNotifica() != null && !aModel.getCodStatoNotifica().equals("")) {
			lCondizioni += " AND SCA.COD_STATO_NOTIFICA = '" + aModel.getCodStatoNotifica() + "'";
		}

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_SCADENZARIO_SIEP = " + aKey;
	}

	public String setCondizioniByKeyCorrente(BigDecimal aKey) {
		String lCondizioni = new String();

		lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
		lCondizioni += " AND COD_TIPO_SCADENZARIO = '02'"; // 02 = fine pena
		lCondizioni += " ORDER BY DATA_INSERIMENTO DESC   ";

		return lCondizioni;
	}

	public String setCondizioneScadenzarioVerbaleArresto(ScadenzarioModel aModel) {
		String lCondizioni = new String();

		lCondizioni += " AND COD_TIPO_SCADENZARIO = '" + aModel.getCodTipoScadenzario() + "'";
		lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = '" + aModel.getFasSieIdFascicoloSiep() + "'";

		return lCondizioni;
	}

	public String setCondizioneScadenzarioFinePenaVaneRicerche(BigDecimal aKey) {
		String lCondizioni = new String();

		lCondizioni += " AND COD_TIPO_SCADENZARIO IN ('02','03')";
		lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = '" + aKey + "'";

		return lCondizioni;
	}

	public String setOrderByResiduo() {
		return " ORDER BY RESIDUO DESC ";
	}

	// 27/03/2015 Scadenzario Fine pena Mis Sic --->
	// public void getCountScadenzariMisSic(ScadenzarioModel aModel) throws DAOException {
	//
	// String lStatement = "SELECT COUNT(*) HowManyRecords";
	//
	// lStatement += " FROM SCADENZARIO_SIEP SCA, FASC_MS_TO_FASC_SIEP FASCMS, CG_REF_CODES TIPSCA ";
	// // lStatement += " , EVENTO EV";
	// // MEV_39: aggiunte tabelle in join
	// lStatement += ", FASCICOLO_SIEP FAS, PENA_RESIDUA PR, STATO_PROCEDIMENTO SP ";
	// lStatement += " WHERE TIPSCA.RV_DOMAIN = 'TIPO_SCADENZARIO' AND TIPSCA.RV_LOW_VALUE =
	// SCA.COD_TIPO_SCADENZARIO";
	// lStatement += " AND SCA.FAS_SIE_ID_FASCICOLO_SIEP = FASCMS.FAS_SIE_ID_FASCICOLO_SIEP(+)";
	// // MEV_39: aggiunte and condition
	// lStatement += " AND FASCMS.FAS_SIE_ID_FASCICOLO_SIEP(+) = FAS.ID_FASCICOLO_SIEP";
	// lStatement += " AND FAS.ID_FASCICOLO_SIEP = SCA.FAS_SIE_ID_FASCICOLO_SIEP";
	// lStatement += " AND PR.FAS_SIE_ID_FASCICOLO_SIEP(+) = SCA.RIF_FASC_SIEP_ORIG";
	// lStatement += " AND PR.FLAG_VALIDATO = 'S' AND PR.DATA_FINE IS NOT NULL AND
	// // NON DEVE PRENDERE I FASCICOLI ARCHIVIATI
	// SP.FAS_SIE_ID_FASCICOLO_SIEP=FAS.ID_FASCICOLO_SIEP AND SP.COD_STATO_PROCEDIMENTO <> '0076' ";
	// lStatement += " AND SCA.ID_SCADENZARIO_SIEP ="
	// + " (SELECT MAX(SCADE.ID_SCADENZARIO_SIEP)"
	// + " FROM SCADENZARIO_SIEP SCADE"
	// // 20191121 [SG]: aggiunta and condition
	// + " WHERE SCADE.FAS_SIE_ID_FASCICOLO_SIEP=FAS.ID_FASCICOLO_SIEP and COD_TIPO_SCADENZARIO = '20')";
	// lStatement += " AND PR.DATA_INSERIMENTO =";
	// lStatement += " (SELECT MAX(DATA_INSERIMENTO)";
	// lStatement += " FROM PENA_RESIDUA";
	// lStatement += " WHERE FAS_SIE_ID_FASCICOLO_SIEP = SCA.RIF_FASC_SIEP_ORIG";
	// lStatement += " AND FLAG_VALIDATO = 'S')";
	// lStatement += " AND FAS.FLAG_VALIDATO = 'S' ";
	// // MEV_39 In scadenziario non devono apparire i procedimenti per i quali è stata fatta richiesta di
	// // accertamento di pericolosità sociale
	// // oppure una trasmissione atti per competenza
	// lStatement += " AND ( FAS.ID_FASCICOLO_SIEP NOT IN (SELECT EE.FAS_SIE_ID_FASCICOLO_SIEP"
	// + " FROM EVENTO EE WHERE EE.FAS_SIE_ID_FASCICOLO_SIEP=FAS.ID_FASCICOLO_SIEP "
	// + " AND EE.COD_MOTIVO IN ('5404', '5416', '2110', '2114') AND EE.FLAG_DOCUMENTO_REGISTRATO <> 'A' ) "
	// + " OR FAS.ID_FASCICOLO_SIEP IN (SELECT EE.FAS_SIE_ID_FASCICOLO_SIEP FROM EVENTO EE,
	// ANNOTAZIONE_ESITO_TRASMISSIONE AA"
	// + " WHERE EE.FAS_SIE_ID_FASCICOLO_SIEP = FAS.ID_FASCICOLO_SIEP AND EE.COD_MOTIVO IN ('5200')"
	// + " AND EE.FLAG_DOCUMENTO_REGISTRATO <> 'A' AND AA.FAS_SIE_ID_FASCICOLO_SIEP=FAS.ID_FASCICOLO_SIEP AND
	// AA.EVE_ID_EVENTO=EE.ID_EVENTO AND AA.COD_ESITO = '01003') )";
	// String ldata1 = new String();
	// String ldata2 = new String();
	// if (aModel.getDataInizioScadenza() != null)
	// ldata1 = DateUtils.getDateToString(aModel.getDataInizioScadenza(), "dd/MM/yyyy");
	// if (aModel.getDataFineScadenza() != null)
	// ldata2 = DateUtils.getDateToString(aModel.getDataFineScadenza(), "dd/MM/yyyy");
	// if (aModel.getCodTipoScadenzario() != null) {
	// lStatement += " AND SCA.COD_TIPO_SCADENZARIO = '" + aModel.getCodTipoScadenzario() + "'";
	// }
	//
	// // PER UFFICIO
	// if (aModel.getCodUfficioInserimento() != null) {
	// lStatement += " AND SCA.COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
	// }
	//
	// // MEV_39: sostituita "PR.DATA_FINE" a "SCA.DATA_FINE_SCADENZA"
	// // in scadenza
	// if (aModel.getTipoRic().equals("sette")) {
	// lStatement += " AND PR.DATA_FINE BETWEEN TO_DATE('" + ldata1 + "','DD/MM/YYYY') AND TO_DATE('"
	// + ldata2 + "','DD/MM/YYYY')";
	// }
	//
	// // scaduti
	// if (aModel.getTipoRic().equals("scaduto")) {
	// lStatement += " AND PR.DATA_FINE < TO_DATE('" + ldata1 + "','DD/MM/YYYY')";
	// }
	//
	// // oggi
	// if (aModel.getTipoRic().equals("oggi")) {
	// lStatement += " AND PR.DATA_FINE = TO_DATE('" + ldata2 + "','DD/MM/YYYY')";
	// }
	// // PER STATO NOTIFICA
	// if (aModel.getCodStatoNotifica() != null && !aModel.getCodStatoNotifica().equals("")) {
	// lStatement += " AND SCA.COD_STATO_NOTIFICA = '" + aModel.getCodStatoNotifica() + "'";
	// }
	//
	// setStatement(lStatement);
	// }
	// End

} // Chiude ScadenzarioSqlDAO