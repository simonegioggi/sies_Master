package siap.siep.statis.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siep.statis.model.IspProvvedimentiModel;

/**
 * <p>
 * Title: IspProvvedimentiSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella IspProvvedimenti
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
public class IspProvvedimentiSqlDAO extends SqlDAO {

	public IspProvvedimentiSqlDAO(Connection con) {
		super(con);
	}

	public void RicercaDettaglioProcedimenti(String[] aCod) throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + " ID_FASCICOLO_SIEP, " + " NRES, " + " CHIAVE_ANNO, " + " CHIAVE_PROGR, "
				+ " ULT_TIPO_PROVVEDIMENTO, " + " ULT_COD_MOTIVO, " + " PEN_TIPO_PROVVEDIMENTO, "
				+ " PEN_COD_MOTIVO, " + " CONTA, " + " COD_UFFICIO, " + " COD_STATO_PROCEDIMENTO, "
				+ " COD_STATO_FASCICOLO_RES, DESCRIZIONE, " + " COD_POSIZIONE_GIURIDICA, " +
				// NGG Statistiche SIEP
				" COD_UFFICIO_INSERIMENTO, " + " CHIAVE_PROGR_ORIG, " + " DESC_UFFICIO_INSERIMENTO, "
				+ " TIPO_CAMPO ";
		// END NGG
		lStatement += " FROM ISP_PROVVEDIMENTI, STATO_FASCICOLO_RES";
		lStatement += " WHERE ";

		lStatement += " " + setCondizioni(aCod);
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	public void RicercaDettaglioProcedimentiMS(String[] aCod) throws DAOException {

		String lStatement = new String("");

		lStatement += " SELECT " + " ID_FASCICOLO_SIEP, " + " NRES, " + " CHIAVE_ANNO, " + " CHIAVE_PROGR, "
				+ " ULT_TIPO_PROVVEDIMENTO, " + " ULT_COD_MOTIVO, " + " PEN_TIPO_PROVVEDIMENTO, "
				+ " PEN_COD_MOTIVO, " + " CONTA, " + " COD_UFFICIO, " + " COD_STATO_PROCEDIMENTO, "
				+ " COD_STATO_FASCICOLO_RES, DESCRIZIONE, " + " COD_POSIZIONE_GIURIDICA, " +
				// NGG Statistiche SIEP
				" COD_UFFICIO_INSERIMENTO, " + " CHIAVE_PROGR_ORIG, " + " DESC_UFFICIO_INSERIMENTO, "
				+ " TIPO_CAMPO ";

		// 24/11/2019: AGGIUGNO PER GESTIRE LE MISURE PROVVISORIE
		// 30/11/2019 [SG]: tolti alias dalla vista (VW_MS_APPL_GIU_COGNIZIONE) e dalla tabella
		// (ISP_PROVVEDIMENTI_MS)
		lStatement += " , CASE WHEN (select distinct count(provv.FASC_SIEP)"
				+ " from VW_MS_APPL_GIU_COGNIZIONE provv"
				+ " where provv.FASC_SIEP = ISP_PROVVEDIMENTI_MS.id_fascicolo_siep) > 0 THEN   'MP'  END TIPO_MISURA";
		// FINE PARTE MISURE PROVVISORIE

		// END NGG
		lStatement += " FROM ISP_PROVVEDIMENTI_MS, STATO_FASCICOLO_RES_MS";
		lStatement += " WHERE ";

		lStatement += " " + setCondizioni(aCod);
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	public void RicercaDettaglioProcedimenti_CPP(String[] aCod) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + " ID_FASCICOLO_SIEP, " + " NRES, " + " CHIAVE_ANNO, " + " CHIAVE_PROGR, "
				+ " ULT_TIPO_PROVVEDIMENTO, " + " ULT_COD_MOTIVO, " + " PEN_TIPO_PROVVEDIMENTO, "
				+ " PEN_COD_MOTIVO, " + " CONTA, " + " COD_UFFICIO, " + " COD_STATO_PROCEDIMENTO, "
				+ " COD_STATO_FASCICOLO_RES, DESCRIZIONE, " + " COD_POSIZIONE_GIURIDICA, " +
				// NGG Statistiche SIEP
				" COD_UFFICIO_INSERIMENTO, " + " CHIAVE_PROGR_ORIG, " + " DESC_UFFICIO_INSERIMENTO, "
				+ " TIPO_CAMPO ";
		// END NGG
		lStatement += " FROM ISP_PROVVEDIMENTI_CPP, STATO_FASCICOLO_RES";
		lStatement += " WHERE ";

		lStatement += " " + setCondizioni(aCod);
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	/**
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void getCountRiepilogoIspProvvedimenti(IspProvvedimentiModel aModel) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT DESCRIZIONE, ORDINAMENTO , count(COD_STATO_FASCICOLO_RES) CONTA, COD_STATO_FASCICOLO, TIPO_CAMPO ";
		lStatement += " FROM ISP_PROVVEDIMENTI, STATO_FASCICOLO_RES ";
		lStatement += " WHERE COD_STATO_FASCICOLO = COD_STATO_FASCICOLO_RES(+) ";
		lStatement += " GROUP BY DESCRIZIONE, ORDINAMENTO, COD_STATO_FASCICOLO_RES, COD_STATO_FASCICOLO, TIPO_CAMPO ";
		lStatement += " ORDER BY ORDINAMENTO ";
		// lStatement += " " + setCondizioni(aModel);
		setStatement(lStatement);
	}

	/**
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void getCountRiepilogoIspProvvedimentiMS(IspProvvedimentiModel aModel) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT DESCRIZIONE, ORDINAMENTO , count(COD_STATO_FASCICOLO_RES) CONTA, COD_STATO_FASCICOLO, TIPO_CAMPO ";
		lStatement += " FROM ISP_PROVVEDIMENTI_MS, STATO_FASCICOLO_RES_MS ";
		lStatement += " WHERE COD_STATO_FASCICOLO = COD_STATO_FASCICOLO_RES(+) ";
		lStatement += " GROUP BY DESCRIZIONE, ORDINAMENTO, COD_STATO_FASCICOLO_RES, COD_STATO_FASCICOLO, TIPO_CAMPO ";
		lStatement += " ORDER BY ORDINAMENTO ";
		// lStatement += " " + setCondizioni(aModel);
		setStatement(lStatement);
	}

	public void getCountRiepilogoIspProvvedimenti_CPP(IspProvvedimentiModel aModel) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT DESCRIZIONE, ORDINAMENTO , count(COD_STATO_FASCICOLO_RES) CONTA, COD_STATO_FASCICOLO, TIPO_CAMPO ";
		lStatement += " FROM ISP_PROVVEDIMENTI_CPP, STATO_FASCICOLO_RES ";
		lStatement += " WHERE COD_STATO_FASCICOLO = COD_STATO_FASCICOLO_RES(+) ";
		// 07-06-2016 - Riciclo dopo primo collaudo V.10
		lStatement += " AND VIS_CPP != 'NO' ";
		// 07-06-2016 - END Riciclo
		lStatement += " GROUP BY DESCRIZIONE, ORDINAMENTO, COD_STATO_FASCICOLO_RES, COD_STATO_FASCICOLO, TIPO_CAMPO ";
		lStatement += " ORDER BY ORDINAMENTO ";
		// lStatement += " " + setCondizioni(aModel);
		setStatement(lStatement);
	}

	public GenericModel getModel() throws DAOException {
		IspProvvedimentiModel aModel = new IspProvvedimentiModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		aModel.setNres(getBigDecimal("NRES"));
		aModel.setChiaveAnno(getInteger("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setUltTipoProvvedimento(getString("ULT_TIPO_PROVVEDIMENTO"));
		aModel.setUltCodMotivo(getString("ULT_COD_MOTIVO"));
		aModel.setPenTipoProvvedimento(getString("PEN_TIPO_PROVVEDIMENTO"));
		aModel.setPenCodMotivo(getString("PEN_COD_MOTIVO"));
		aModel.setConta(getInteger("CONTA"));
		aModel.setCodUfficio(getString("COD_UFFICIO"));
		// aModel.setDescrUfficio(getString("") );
		aModel.setCodStatoProcedimento(getString("COD_STATO_PROCEDIMENTO"));
		// aModel.setDescrStatoProcedimento(getString("") );
		aModel.setCodStatoFascicoloRes(getInteger("COD_STATO_FASCICOLO_RES"));
		aModel.setDescrStatoFascicoloRes(getString("DESCRIZIONE"));
		aModel.setCodPosizioneGiuridica(getString("COD_POSIZIONE_GIURIDICA"));
		// aModel.setDescrPosizioneGiuridica(getString("") );

		// NGG Statistiche SIEP
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setChiaveProgrOrig(getBigDecimal("CHIAVE_PROGR_ORIG"));
		aModel.setDescUfficioInserimento(getString("DESC_UFFICIO_INSERIMENTO"));
		// END NGG
		aModel.setTipoCampo(getString("TIPO_CAMPO"));

		return aModel;
	}

	/**
	 * @return // 24/11/2019 (INTERVENTO POST COLLAUDO 11.3) : GESTIONE MISURE PROVVISORIE
	 * @throws DAOException
	 */
	public GenericModel getModelExtend() throws DAOException {
		IspProvvedimentiModel aModel = new IspProvvedimentiModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		aModel.setNres(getBigDecimal("NRES"));
		aModel.setChiaveAnno(getInteger("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setUltTipoProvvedimento(getString("ULT_TIPO_PROVVEDIMENTO"));
		aModel.setUltCodMotivo(getString("ULT_COD_MOTIVO"));
		aModel.setPenTipoProvvedimento(getString("PEN_TIPO_PROVVEDIMENTO"));
		aModel.setPenCodMotivo(getString("PEN_COD_MOTIVO"));
		aModel.setConta(getInteger("CONTA"));
		aModel.setCodUfficio(getString("COD_UFFICIO"));
		// aModel.setDescrUfficio(getString("") );
		aModel.setCodStatoProcedimento(getString("COD_STATO_PROCEDIMENTO"));
		// aModel.setDescrStatoProcedimento(getString("") );
		aModel.setCodStatoFascicoloRes(getInteger("COD_STATO_FASCICOLO_RES"));
		aModel.setDescrStatoFascicoloRes(getString("DESCRIZIONE"));
		aModel.setCodPosizioneGiuridica(getString("COD_POSIZIONE_GIURIDICA"));
		// aModel.setDescrPosizioneGiuridica(getString("") );

		// NGG Statistiche SIEP
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setChiaveProgrOrig(getBigDecimal("CHIAVE_PROGR_ORIG"));
		aModel.setDescUfficioInserimento(getString("DESC_UFFICIO_INSERIMENTO"));
		// END NGG
		aModel.setTipoCampo(getString("TIPO_CAMPO"));
		aModel.setTipoMisura(getString("TIPO_MISURA"));

		return aModel;
	}

	public GenericModel getModelCountRiepilogo() throws DAOException {
		IspProvvedimentiModel aModel = new IspProvvedimentiModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setConta(getInteger("CONTA"));

		aModel.setDescrStatoFascicoloRes(getString("DESCRIZIONE"));

		aModel.setCodStatoFascicoloRes(getInt("COD_STATO_FASCICOLO"));

		aModel.setTipoCampo(getString("TIPO_CAMPO"));

		return aModel;
	}

	public String setCondizioni(String[] aCodStati) {
		String lCondizioni = new String();

		lCondizioni = " COD_STATO_FASCICOLO_RES in (";

		for (int i = 0; i < aCodStati.length; i++) {
			lCondizioni += aCodStati[i];

			if (i != aCodStati.length - 1)
				lCondizioni += ", ";
		}

		lCondizioni += ") AND COD_STATO_FASCICOLO_RES = COD_STATO_FASCICOLO ";

		return lCondizioni;
	}

	public String setOrder() {
		String lOrder = new String();

		// lOrder = " order by DESCRIZIONE, CHIAVE_ANNO, COD_UFFICIO_INSERIMENTO, CHIAVE_PROGR";
		lOrder = " order by ORDINAMENTO, CHIAVE_ANNO, COD_UFFICIO_INSERIMENTO, CHIAVE_PROGR";
		// lOrder = " order by DESCRIZIONE, CHIAVE_ANNO, CHIAVE_PROGR";

		return lOrder;
	}

	// FIXME completare
	public void getCountRiepilogoIspProvvedimentiPerTitolo(IspProvvedimentiModel aModel) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT count(*), STATO_FASCICOLO_RES.T1, STATO_FASCICOLO_RES.T2 ";
		lStatement += " FROM ISP_PROVVEDIMENTI, STATO_FASCICOLO_RES ";
		lStatement += " WHERE COD_STATO_FASCICOLO = COD_STATO_FASCICOLO_RES";
		lStatement += " AND STATO_FASCICOLO_RES.TIPO_CAMPO = 'V0'";

		lStatement += " GROUP BY STATO_FASCICOLO_RES.T1, STATO_FASCICOLO_RES.T2 ";
		lStatement += " ORDER STATO_FASCICOLO_RES.T1, STATO_FASCICOLO_RES.T2 ";
		// lStatement += " " + setCondizioni(aModel);
		setStatement(lStatement);
	}

	public void getTitoliPerCodiciPerTipoTitolo(String[] aCodiciSelezionati, String aTipoTitolo)
			throws DAOException {

		String lStatement = new String("");

		String lInCondition = "(";
		for (int i = 0; i < aCodiciSelezionati.length; i++) {
			lInCondition += aCodiciSelezionati[i];

			if (i != aCodiciSelezionati.length - 1)
				lInCondition += ", ";
		}
		lInCondition += ")";

		lStatement += " SELECT DISTINCT " + aTipoTitolo;
		lStatement += " FROM STATO_FASCICOLO_RES, ISP_PROVVEDIMENTI ";
		lStatement += " WHERE ISP_PROVVEDIMENTI.COD_STATO_FASCICOLO_RES = STATO_FASCICOLO_RES.COD_STATO_FASCICOLO ";
		lStatement += " AND ISP_PROVVEDIMENTI.ID_FASCICOLO_SIEP is not null ";
		lStatement += " AND ISP_PROVVEDIMENTI.COD_STATO_FASCICOLO_RES in " + lInCondition;

		setStatement(lStatement);
	}

	public void getTitoliPerCodiciPerTipoTitolo_CPP(String[] aCodiciSelezionati, String aTipoTitolo)
			throws DAOException {

		String lStatement = new String("");

		String lInCondition = "(";
		for (int i = 0; i < aCodiciSelezionati.length; i++) {
			lInCondition += aCodiciSelezionati[i];

			if (i != aCodiciSelezionati.length - 1)
				lInCondition += ", ";
		}
		lInCondition += ")";

		lStatement += " SELECT DISTINCT " + aTipoTitolo;
		lStatement += " FROM STATO_FASCICOLO_RES, ISP_PROVVEDIMENTI_CPP ";
		lStatement += " WHERE ISP_PROVVEDIMENTI_CPP.COD_STATO_FASCICOLO_RES = STATO_FASCICOLO_RES.COD_STATO_FASCICOLO ";
		lStatement += " AND ISP_PROVVEDIMENTI_CPP.ID_FASCICOLO_SIEP is not null ";
		lStatement += " AND ISP_PROVVEDIMENTI_CPP.COD_STATO_FASCICOLO_RES in " + lInCondition;

		setStatement(lStatement);
	}

	// MEV_39: aggiunto metodo 20191125 [SG]
	public void getTitoliPerCodiciPerTipoTitoloMS(String[] codiciSelezionati, String tipo)
			throws DAOException {

		String lStatement = new String("");

		String lInCondition = "(";
		for (int i = 0; i < codiciSelezionati.length; i++) {
			lInCondition += codiciSelezionati[i];
			if (i != codiciSelezionati.length - 1)
				lInCondition += ", ";
		}
		lInCondition += ")";

		lStatement += " SELECT DISTINCT " + tipo;
		lStatement += " FROM STATO_FASCICOLO_RES_MS y, ISP_PROVVEDIMENTI_MS x";
		lStatement += " WHERE x.COD_STATO_FASCICOLO_RES = y.COD_STATO_FASCICOLO";
		lStatement += " AND x.ID_FASCICOLO_SIEP is not null";
		lStatement += " AND x.COD_STATO_FASCICOLO_RES in " + lInCondition;

		setStatement(lStatement);
	}

}