package siap.sige.provvedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;

/**
 * <p>
 * Title: ProvvedimentoSigeSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella ProvvedimentoSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
public class ProvvedimentoSigeSqlDAO extends SIAPSqlDAO {

	public ProvvedimentoSigeSqlDAO(Connection con) {
		super(con);
	}

	public void ricercaOrdinanzaRinvioUdienzaDaValidareByFascicolo(BigDecimal idFascicolo) {
		String sql = "select * from provvedimento_sige, evento where " + "fas_id_fascicolo_sige="
				+ idFascicolo + " and " + "cod_tipo_provvedimento_sige='04' and "
				+ "evento.id_evento=provvedimento_sige.id_evento_generato and "
				+ "(evento.flag_documento_registrato is NULL or evento.flag_documento_registrato <> 'S')";
		super.setStatement(sql);
	}

	/**
	 * <p>
	 * Description: metodo di ricerca del Provvedimento definitorio in PROVVEDIMENTO_SIGE collegato a una
	 * occorrenza di EVENTO
	 * </p>
	 *
	 * @param BigDecimal
	 *            aKey : Identificativo Fascicolo SIGE
	 * @throws DAOException
	 */

	public void ricercaProvvedimentoDefinitorioByIdFasSige(BigDecimal aKey) throws DAOException {
		String strQuery = "";
		// strQuery += ricercaEventoProvvedimentoSigeSqlQuery(aKey);
		// commento la query sy indicata a favore della nuova ricerca che va anche su fascicolo_sige
		strQuery += ricercaEventoProvvedimentoSigeQuery(aKey);
		strQuery += setCondizioneDefinitorio();
		strQuery += setOrderDataEmissione();

		setStatement(strQuery);
	}

	public void ricercaProvvedimentiUdienzeByIdFascicolo(BigDecimal idFascicolo) throws DAOException {
		String strQuery = ricercaEventoProvvedimentoSigeSqlQuery(idFascicolo);
		strQuery += " and PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO IN ('02') ";
		strQuery += " and PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE in  ('50','01','04','06','07')";
		super.setStatement(strQuery);
	}

	/**
	 * <p>
	 * Description: metodo di ricerca dei Provvedimento da depositare in PROVVEDIMENTO_SIGE collegato a una
	 * occorrenza di EVENTO
	 * </p>
	 *
	 * @param ProvvedimentoSigeModel
	 *            aProvModel : ProvvedimentoSigeModel con filtri di ricerca.
	 * @throws DAOException
	 */
	public void ricercaProvvedimentoDaDepositare(ProvvedimentoSigeModel aProvModel) throws DAOException {
		String strQuery = "";
		strQuery += ricercaEventoProvvedimentoSigeSqlQuery(aProvModel.getFasIdFascicoloSige());
		strQuery += setCondizioneDaDepositare(aProvModel);
		strQuery += setOrderDataEmissione();
		setStatement(strQuery);
	}

	public void ricercaDecretiDaDepositare(ProvvedimentoSigeModel aProvModel) throws DAOException {
		String strQuery = "";
		strQuery += ricercaEventoProvvedimentoSigeSqlQuery(aProvModel.getFasIdFascicoloSige());
		strQuery += setCondizioneDecretiDaDepositare(aProvModel);
		strQuery += setOrderDataEmissione();
		setStatement(strQuery);
	}

	public void ricercaOrdinanzeDaDepositare(ProvvedimentoSigeModel aProvModel) throws DAOException {
		String strQuery = "";
		strQuery += ricercaEventoProvvedimentoSigeSqlQuery(aProvModel.getFasIdFascicoloSige());
		strQuery += setCondizioneOrdinanzeDaDepositare(aProvModel);
		strQuery += setOrderDataEmissione();
		setStatement(strQuery);
	}

	/**
	 * <p>
	 * Description: metodo di ricerca dei Provvedimento da depositare in PROVVEDIMENTO_SIGE collegato a una
	 * occorrenza di EVENTO
	 * </p>
	 *
	 * @param ProvvedimentoSigeModel
	 *            aProvModel : ProvvedimentoSigeModel con filtri di ricerca.
	 * @throws DAOException
	 */
	public void ricercaProvvedimentoPerEvento(ProvvedimentoSigeModel aProvModel) throws DAOException {
		String strQuery = "";
		strQuery += ricercaEventoProvvedimentoSigeSqlQuery(aProvModel.getFasIdFascicoloSige());
		strQuery += setCondizionePerEvento(aProvModel.getIdEventoGenerato());
		strQuery += setOrderDataEmissione();
		setStatement(strQuery);
	}

	private String setOrderDataEmissione() {
		String lSql = new String();
		lSql += " ORDER BY PROVVEDIMENTO_SIGE.DATA_EMISSIONE DESC ";
		return lSql;
	}

	private String setOrderDataEmissioneInserimentoASC() {
		String lSql = new String();
		lSql += " ORDER BY PROVVEDIMENTO_SIGE.DATA_INSERIMENTO, PROVVEDIMENTO_SIGE.DATA_EMISSIONE ASC ";
		return lSql;
	}

	//
	// METODI DI RICERCA()
	//
	public void ricercaProvvedimentiSigePerIdFasSigePerIdSoggetto(BigDecimal aIdSoggettoSige,
			String codTipoProvvedimento) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT DISTINCT " + "PROVVEDIMENTO_SIGE.ID_PROVVEDIMENTO_SIGE, "
				+ "PROVVEDIMENTO_SIGE.FAS_ID_FASCICOLO_SIGE, " + "PROVVEDIMENTO_SIGE.ID_EVENTO_GENERATO, "
				+ "PROVVEDIMENTO_SIGE.CHIAVE_ANNO, " + "PROVVEDIMENTO_SIGE.CHIAVE_PROGR, "
				+ "PROVVEDIMENTO_SIGE.CHIAVE_UFFICIO, " + "PROVVEDIMENTO_SIGE.DATA_EMISSIONE, "
				+ "PROVVEDIMENTO_SIGE.DATA_DEPOSITO, " + "PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO, "
				+ "PROVVEDIMENTO_SIGE.DEFINITORIO, " + "PROVVEDIMENTO_SIGE.FLAG_ORDINE_TRADUZIONE, "
				+ "PROVVEDIMENTO_SIGE.LUOGO_SVOLGIMENTO, " + "PROVVEDIMENTO_SIGE.COD_OPERATORE_INSERIMENTO, "
				+ "PROVVEDIMENTO_SIGE.COD_UFFICIO_INSERIMENTO, " + "PROVVEDIMENTO_SIGE.DATA_INSERIMENTO, "
				+ "PROVVEDIMENTO_SIGE.COD_OPERATORE_AGGIORNAMENTO, "
				+ "PROVVEDIMENTO_SIGE.COD_UFFICIO_AGGIORNAMENTO, " + "PROVVEDIMENTO_SIGE.DATA_AGGIORNAMENTO, "
				+ "PROVVEDIMENTO_SIGE.COL_ID_COLLEGIO, " + "PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE, "
				+ "PROVVEDIMENTO_SIGE.COD_UFFICIO_DESTINATARIO, " + "PROVVEDIMENTO_SIGE.NOTE, "
				+ "PROVVEDIMENTO_SIGE.PROVV_ID_PROVVEDIMENTO_SIGE, "
				+ "PROVVEDIMENTO_SIGE.UDI_ID_UDIENZA_SIGE, " +
				// "UFFICIO.COD_TIPO_UFFICIO as Tipo_Ufficio, "+
				"UFFICIO.COD_COMUNE as Ufficio_Luogo ";
		lStatement += " FROM PROVVEDIMENTO_SIGE ";
		lStatement += " ,FASCICOLO_SIGE ";
		lStatement += " ,UFFICIO ";

		lStatement += " " + setCondizionePerIdFasSigePerIdSoggetto(aIdSoggettoSige, codTipoProvvedimento);
		lStatement += " " + setOrderDataEmissioneInserimentoASC();
		setStatement(lStatement);
	}

	public void ricercaProvvedimentiSigePerIdFasSige(BigDecimal aIdFasSige) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PROVVEDIMENTO_SIGE, " + "FAS_ID_FASCICOLO_SIGE, "
				+ "ID_EVENTO_GENERATO, " + "CHIAVE_ANNO, " + "CHIAVE_PROGR, " + "CHIAVE_UFFICIO, "
				+ "DATA_EMISSIONE, " + "DATA_DEPOSITO, " + "COD_TIPO_PROVVEDIMENTO, " + "DEFINITORIO, "
				+ "FLAG_ORDINE_TRADUZIONE, " + "LUOGO_SVOLGIMENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COL_ID_COLLEGIO, "
				+ "COD_TIPO_PROVVEDIMENTO_SIGE, " + "COD_UFFICIO_DESTINATARIO, " + "NOTE, "
				+ "PROVV_ID_PROVVEDIMENTO_SIGE, " + "UDI_ID_UDIENZA_SIGE ";
		lStatement += " FROM PROVVEDIMENTO_SIGE";

		lStatement += " " + setCondizionePerIdFasSige(aIdFasSige);
		lStatement += " " + setOrderDataEmissioneInserimentoASC();
		setStatement(lStatement);
	}

	public void ricercaProvvedimentiSigePerOpposizioni(BigDecimal aIdFasSige) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PROVVEDIMENTO_SIGE, " + "FAS_ID_FASCICOLO_SIGE, "
				+ "ID_EVENTO_GENERATO, " + "CHIAVE_ANNO, " + "CHIAVE_PROGR, " + "CHIAVE_UFFICIO, "
				+ "DATA_EMISSIONE, " + "DATA_DEPOSITO, " + "COD_TIPO_PROVVEDIMENTO, " + "DEFINITORIO, "
				+ "FLAG_ORDINE_TRADUZIONE, " + "LUOGO_SVOLGIMENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COL_ID_COLLEGIO, "
				+ "COD_TIPO_PROVVEDIMENTO_SIGE, " + "COD_UFFICIO_DESTINATARIO, " + "NOTE, "
				+ "PROVV_ID_PROVVEDIMENTO_SIGE, " + "UDI_ID_UDIENZA_SIGE ";
		lStatement += " FROM PROVVEDIMENTO_SIGE";
		lStatement += " " + setCondizionePerIdFasSige(aIdFasSige);
		lStatement += " and COD_TIPO_PROVVEDIMENTO_SIGE IN ('02', '03', '04', '05', '06','07') ";
		lStatement += " and ID_EVENTO_GENERATO NOT IN (SELECT ID_EVENTO FROM EVENTO WHERE COD_MOTIVO IN ('0600','0601') ) ";
		lStatement += " and ID_EVENTO_GENERATO NOT IN (SELECT ID_EVENTO FROM EVENTO WHERE DATA_TRASMISSIONE_ATTI IS NULL OR UPPER(FLAG_DOCUMENTO_REGISTRATO)='A') ";
		lStatement += " " + setOrderDataEmissioneInserimentoASC();
		setStatement(lStatement);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaProvvSigePerIdFasSigeTipiProvv(BigDecimal aIdFasSige, String aTipiProvv)
			throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PROVVEDIMENTO_SIGE, " + "FAS_ID_FASCICOLO_SIGE, "
				+ "ID_EVENTO_GENERATO, " + "CHIAVE_ANNO, " + "CHIAVE_PROGR, " + "CHIAVE_UFFICIO, "
				+ "DATA_EMISSIONE, " + "DATA_DEPOSITO, " + "COD_TIPO_PROVVEDIMENTO, " + "DEFINITORIO, "
				+ "FLAG_ORDINE_TRADUZIONE, " + "LUOGO_SVOLGIMENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COL_ID_COLLEGIO, "
				+ "COD_TIPO_PROVVEDIMENTO_SIGE, " + "COD_UFFICIO_DESTINATARIO, " + "NOTE, "
				+ "PROVV_ID_PROVVEDIMENTO_SIGE, " + "UDI_ID_UDIENZA_SIGE ";
		lStatement += " FROM PROVVEDIMENTO_SIGE";

		lStatement += " " + setCondizionePerIdFasSige(aIdFasSige);
		lStatement += " " + setCondizionePerTipiProvv(aTipiProvv);
		lStatement += " " + setOrderDataEmissioneInserimentoASC();

		setStatement(lStatement);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaProvvSigePerIdFasSigeTipiProvvProvvSige(BigDecimal aIdFasSige, String aTipiProvv,
			String aCodTipoProvvSige) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PROVVEDIMENTO_SIGE, " + "FAS_ID_FASCICOLO_SIGE, "
				+ "ID_EVENTO_GENERATO, " + "CHIAVE_ANNO, " + "CHIAVE_PROGR, " + "CHIAVE_UFFICIO, "
				+ "DATA_EMISSIONE, " + "DATA_DEPOSITO, " + "COD_TIPO_PROVVEDIMENTO, " + "DEFINITORIO, "
				+ "FLAG_ORDINE_TRADUZIONE, " + "LUOGO_SVOLGIMENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COL_ID_COLLEGIO, "
				+ "COD_TIPO_PROVVEDIMENTO_SIGE, " + "COD_UFFICIO_DESTINATARIO, " + "NOTE, "
				+ "PROVV_ID_PROVVEDIMENTO_SIGE, " + "UDI_ID_UDIENZA_SIGE ";
		lStatement += " FROM PROVVEDIMENTO_SIGE";

		lStatement += " " + setCondizionePerIdFasSige(aIdFasSige);
		lStatement += " " + setCondizionePerTipiProvv(aTipiProvv);
		lStatement += " " + setCondizionePerCodProvvSige(aCodTipoProvvSige);
		lStatement += " " + setOrderDataEmissioneInserimentoASC();

		setStatement(lStatement);
	}

	public void ricercaProvvedimentoSige(ProvvedimentoSigeModel aModel) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PROVVEDIMENTO_SIGE, " + "FAS_ID_FASCICOLO_SIGE, "
				+ "ID_EVENTO_GENERATO, " + "CHIAVE_ANNO, " + "CHIAVE_PROGR, " + "CHIAVE_UFFICIO, "
				+ "DATA_EMISSIONE, " + "DATA_DEPOSITO, " + "COD_TIPO_PROVVEDIMENTO, " + "DEFINITORIO, "
				+ "FLAG_ORDINE_TRADUZIONE, " + "LUOGO_SVOLGIMENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COL_ID_COLLEGIO, "
				+ "COD_TIPO_PROVVEDIMENTO_SIGE, " + "COD_UFFICIO_DESTINATARIO, " + "NOTE, "
				+ "PROVV_ID_PROVVEDIMENTO_SIGE, " + "UDI_ID_UDIENZA_SIGE ";
		lStatement += " FROM PROVVEDIMENTO_SIGE";
		lStatement += " WHERE ";

		lStatement += " " + setCondizioni(aModel);
		setStatement(lStatement);
	}

	public void ricercaProvvedimentoSigeByKey(BigDecimal aKey) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PROVVEDIMENTO_SIGE, " + "FAS_ID_FASCICOLO_SIGE, "
				+ "ID_EVENTO_GENERATO, " + "CHIAVE_ANNO, " + "CHIAVE_PROGR, " + "CHIAVE_UFFICIO, "
				+ "DATA_EMISSIONE, " + "DATA_DEPOSITO, " + "COD_TIPO_PROVVEDIMENTO, " + "DEFINITORIO, "
				+ "FLAG_ORDINE_TRADUZIONE, " + "LUOGO_SVOLGIMENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COL_ID_COLLEGIO, "
				+ "COD_TIPO_PROVVEDIMENTO_SIGE, " + "COD_UFFICIO_DESTINATARIO, " + "NOTE, "
				+ "PROVV_ID_PROVVEDIMENTO_SIGE, " + "UDI_ID_UDIENZA_SIGE ";
		lStatement += " FROM PROVVEDIMENTO_SIGE";
		lStatement += " WHERE ";

		lStatement += " " + setCondizioneByKey(aKey);
		setStatement(lStatement);
	}

	public void ricercaProvvedimentoSigeByKeySospensione(BigDecimal ProvvIdKey) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PROVVEDIMENTO_SIGE, " + "FAS_ID_FASCICOLO_SIGE, "
				+ "ID_EVENTO_GENERATO, " + "CHIAVE_ANNO, " + "CHIAVE_PROGR, " + "CHIAVE_UFFICIO, "
				+ "PROVVEDIMENTO_SIGE.DATA_EMISSIONE, " + "DATA_DEPOSITO, "
				+ "PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO, " + "DEFINITORIO, " + "FLAG_ORDINE_TRADUZIONE, "
				+ "LUOGO_SVOLGIMENTO, " + "PROVVEDIMENTO_SIGE.COD_OPERATORE_INSERIMENTO, "
				+ "PROVVEDIMENTO_SIGE.COD_UFFICIO_INSERIMENTO, " + "PROVVEDIMENTO_SIGE.DATA_INSERIMENTO, "
				+ "PROVVEDIMENTO_SIGE.COD_OPERATORE_AGGIORNAMENTO, "
				+ "PROVVEDIMENTO_SIGE.COD_UFFICIO_AGGIORNAMENTO, " + "PROVVEDIMENTO_SIGE.DATA_AGGIORNAMENTO, "
				+ "COL_ID_COLLEGIO, " + "PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE, "
				+ "PROVVEDIMENTO_SIGE.COD_UFFICIO_DESTINATARIO, " + "PROVVEDIMENTO_SIGE.NOTE, "
				+ "PROVV_ID_PROVVEDIMENTO_SIGE, " + "PROVVEDIMENTO_SIGE.UDI_ID_UDIENZA_SIGE ";
		lStatement += " FROM PROVVEDIMENTO_SIGE";
		// Aggiunta la join ad evento per evitare la presenza in elenco sospensioni di quelle annullate
		lStatement += " INNER JOIN EVENTO ON ID_EVENTO = ID_EVENTO_GENERATO AND (FLAG_DOCUMENTO_REGISTRATO <> 'A') ";
		lStatement += " WHERE ";

		lStatement += " " + setCondizioneByKeySospensione(ProvvIdKey);
		setStatement(lStatement);
	}

	public void ricercaProvvedimentoSigeByKeySospensioneOLD(BigDecimal ProvvIdKey) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PROVVEDIMENTO_SIGE, " + "FAS_ID_FASCICOLO_SIGE, "
				+ "ID_EVENTO_GENERATO, " + "CHIAVE_ANNO, " + "CHIAVE_PROGR, " + "CHIAVE_UFFICIO, "
				+ "DATA_EMISSIONE, " + "DATA_DEPOSITO, " + "COD_TIPO_PROVVEDIMENTO, " + "DEFINITORIO, "
				+ "FLAG_ORDINE_TRADUZIONE, " + "LUOGO_SVOLGIMENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COL_ID_COLLEGIO, "
				+ "COD_TIPO_PROVVEDIMENTO_SIGE, " + "COD_UFFICIO_DESTINATARIO, " + "NOTE, "
				+ "PROVV_ID_PROVVEDIMENTO_SIGE, " + "UDI_ID_UDIENZA_SIGE ";
		lStatement += " FROM PROVVEDIMENTO_SIGE";
		// Aggiunta la join ad evento per evitare la presenza in elenco sospensioni di quelle annullate
		lStatement += " INNER JOIN EVENTO ON ID_EVENTO = ID_EVENTO_GENERATO AND (FLAG_DOCUMENTO_REGISTRATO <> 'A') ";
		lStatement += " WHERE ";

		lStatement += " " + setCondizioneByKeySospensione(ProvvIdKey);
		setStatement(lStatement);
	}

	/**
	 * <p>
	 * Description: metodo di ricerca del Provvedimento definitorio in PROVVEDIMENTO_SIGE collegato a una
	 * occorrenza di EVENTO
	 * </p>
	 *
	 * @param BigDecimal
	 *            aKey : Identificativo Fascicolo SIGE
	 * @throws DAOException
	 */
	protected String ricercaEventoProvvedimentoSigeSqlQuery(BigDecimal aKey) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT * ";
		lStatement += " FROM PROVVEDIMENTO_SIGE ";
		lStatement += " INNER JOIN EVENTO ON ID_EVENTO = ID_EVENTO_GENERATO AND (FLAG_DOCUMENTO_REGISTRATO IS NULL OR FLAG_DOCUMENTO_REGISTRATO <> 'A') ";
		lStatement += " WHERE FAS_ID_FASCICOLO_SIGE ='" + aKey + "'";

		return lStatement;
	}

	/**
	 * @param aKey
	 * @return
	 * @throws DAOException
	 */
	protected String ricercaEventoProvvedimentoSigeQuery(BigDecimal aKey) throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT * ";
		lStatement += " FROM PROVVEDIMENTO_SIGE ";
		lStatement += " INNER JOIN EVENTO ON ID_EVENTO = ID_EVENTO_GENERATO AND (FLAG_DOCUMENTO_REGISTRATO IS NULL OR FLAG_DOCUMENTO_REGISTRATO <> 'A') ,FASCICOLO_SIGE F ";
		lStatement += " WHERE FAS_ID_FASCICOLO_SIGE ='" + aKey + "'"
				+ " AND F.ID_FASCICOLO_SIGE=PROVVEDIMENTO_SIGE.FAS_ID_FASCICOLO_SIGE ";

		return lStatement;
	}

	/**
	 * @return
	 */
	public String setCondizioneDefinitorio() {
		String lCondizioni = new String();
		// AGGINGO LA CONDIZIONE CHE IL FASCICOLO SIGE DEVE AVERE LA DATA DEFINIZIONE VALORIZZATA (info:
		// 30/10/2019: non so per quale motivo sia stata aggiunta!!)
		lCondizioni = " AND DEFINITORIO = 'S' ";
		// @emma Ticket#20191029011 + Ticket#20191031016 - rimuovo al condizione su data definizione a not
		// null (con questa
		// condizione non
		// funzionano più le stampe per le ordinanze generiche)
		// + "AND F.DATA_DEFINIZIONE IS NOT NULL ";
		return lCondizioni;
	}

	public String setCondizioneDaDepositare(ProvvedimentoSigeModel aModel) {
		String lCondizioni = new String();
		lCondizioni = " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		if (aModel.getCodTipoProvvedimento() != null)
			lCondizioni += " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO ='"
					+ aModel.getCodTipoProvvedimento() + "'";

		lCondizioni = " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE NOT IN ('55', '56', '01', '11') ";

		return lCondizioni;
	}

	private String setCondizioneDecretiDaDepositare(ProvvedimentoSigeModel aModel) {
		String lCondizioni = new String();
		lCondizioni = " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		if (aModel.getCodTipoProvvedimento() != null)
			lCondizioni += " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO ='"
					+ aModel.getCodTipoProvvedimento() + "'";

		lCondizioni = " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE NOT IN ('52', '55', '56', '11', '03','14','18','07','06','04','10','15','16') ";

		return lCondizioni;
	}

	private String setCondizioneOrdinanzeDaDepositare(ProvvedimentoSigeModel aModel) {
		String lCondizioni = new String();
		lCondizioni = " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		if (aModel.getCodTipoProvvedimento() != null)
			lCondizioni += " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO ='"
					+ aModel.getCodTipoProvvedimento() + "'";

		lCondizioni = " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE NOT IN ('52','55', '56', '01', '11','02','05','09','12','17','15','16') ";
		return lCondizioni;
	}

	public String setCondizionePerEvento(BigDecimal aIdEventoGenerato) {
		String lCondizioni = new String();
		lCondizioni = " AND EVENTO.ID_EVENTO ='" + aIdEventoGenerato + "'";
		return lCondizioni;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		ProvvedimentoSigeModel aModel = new ProvvedimentoSigeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdProvvedimentoSige(getBigDecimal("ID_PROVVEDIMENTO_SIGE"));
		aModel.setFasIdFascicoloSige(getBigDecimal("FAS_ID_FASCICOLO_SIGE"));
		aModel.setIdEventoGenerato(getBigDecimal("ID_EVENTO_GENERATO"));
		aModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setDataDeposito(getDate("DATA_DEPOSITO"));
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setDescrTipoProvvedimento("");
		aModel.setDefinitorio(getString("DEFINITORIO"));
		aModel.setFlagOrdineTraduzione(getString("FLAG_ORDINE_TRADUZIONE"));
		aModel.setLuogoSvolgimento(getString("LUOGO_SVOLGIMENTO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setDescrUfficioInserimento((""));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setDescrUfficioAggiornamento("");
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setColIdCollegio(getBigDecimal("COL_ID_COLLEGIO"));
		aModel.setCodTipoProvvedimentoSige(getString("COD_TIPO_PROVVEDIMENTO_SIGE"));
		aModel.setDescrTipoProvvedimentoSige("");
		aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
		aModel.setNote(getString("NOTE"));
		aModel.setProvvIdProvvedimentoSige(getBigDecimal("PROVV_ID_PROVVEDIMENTO_SIGE"));
		aModel.setUdiIdUdienzaSige(getBigDecimal("UDI_ID_UDIENZA_SIGE"));
		return aModel;
	}

	/**
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getEventoProvvedimentoSige() throws DAOException {
		ProvvedimentoSigeEventoModel lProvvedimentoEvento = new ProvvedimentoSigeEventoModel();

		// Provvedimento
		lProvvedimentoEvento.setProvvedimento((ProvvedimentoSigeModel) this.getModel());

		// Evento (Commentato)
		// lProvvedimentoEvento.setEvento(this.getModelEvento());

		return lProvvedimentoEvento;
	}

	/*
	 * Metodo valido in caso di Extends di EventoSqlDAO public EventoModel getModelEvento() throws
	 * DAOException { EventoModel aModel = new EventoModel(); aModel = (EventoModel)super.getModel();
	 *
	 * return aModel; }
	 */

	public String setCondizionePerIdFasSige(BigDecimal aIdFasSige) {
		String lCondizioni = new String();
		lCondizioni = " WHERE PROVVEDIMENTO_SIGE.FAS_ID_FASCICOLO_SIGE ='" + aIdFasSige + "'";

		return lCondizioni;
	}

	public String setCondizionePerIdFasSigePerIdSoggetto(BigDecimal aIdSogSige, String codTipoProvvedimento) {
		String lCondizioni = new String();
		lCondizioni = " WHERE PROVVEDIMENTO_SIGE.FAS_ID_FASCICOLO_SIGE = FASCICOLO_SIGE.ID_FASCICOLO_SIGE "
				+ " AND UFFICIO.COD_UFFICIO = PROVVEDIMENTO_SIGE.CHIAVE_UFFICIO  "
				// + " AND PROVVEDIMENTO_SIGE.FAS_ID_FASCICOLO_SIGE ='"+ aIdFasSige +"'"
				+ " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO ='" + codTipoProvvedimento + "'"
				// MEV SIGE-STEP4 la ricerca deve essere legata solo al soggetto
				+ " AND FASCICOLO_SIGE.SOG_ID_SOGGETTO IN (SELECT DISTINCT SG.ID_SOGGETTO"
				+ "  FROM SOGGETTO S, SOGGETTO SG  WHERE S.ID_SOGGETTO = '" + aIdSogSige + "'"
				+ "  AND S.COGNOME = SG.COGNOME AND S.NOME = SG.NOME AND S.DATA_NASCITA = SG.DATA_NASCITA"
				+ "  AND S.COD_STATO_NASCITA = SG.COD_STATO_NASCITA AND NVL(S.COD_COMUNE_NASCITA, '0') = NVL(SG.COD_COMUNE_NASCITA, '0')) "
				+ "  AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO = '03'";

		return lCondizioni;
	}

	public String setCondizioneByKey(BigDecimal aKey) {
		String lCondizioni = new String();
		lCondizioni = " PROVVEDIMENTO_SIGE.ID_PROVVEDIMENTO_SIGE =" + aKey;

		return lCondizioni;
	}

	public String setCondizioneByKeySospensione(BigDecimal ProvvIdKey) {
		String lCondizioni = new String();
		lCondizioni = " PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE = '"
				+ ICostantiProvvedimentoSige.COD_ORDINANZA_SOSPENSIONE + "' AND ";
		lCondizioni += " PROVVEDIMENTO_SIGE.PROVV_ID_PROVVEDIMENTO_SIGE =" + ProvvIdKey;

		return lCondizioni;
	}

	public String setCondizionePerTipiProvv(String aTipiProvv) {
		String lCondizioni = new String();
		lCondizioni = " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO IN (" + aTipiProvv + ")";

		return lCondizioni;
	}

	public String setCondizionePerCodProvvSige(String aCodProvvSige) {
		String lCondizioni = new String();
		lCondizioni = " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE IN (" + aCodProvvSige + ")";

		return lCondizioni;
	}

	public String setCondizioni(ProvvedimentoSigeModel aModel) {
		String lCondizioni = new String();
		if (aModel.getFasIdFascicoloSige() != null)
			lCondizioni = " AND PROVVEDIMENTO_SIGE.FAS_ID_FASCICOLO_SIGE ='" + aModel.getFasIdFascicoloSige()
					+ "'";
		if (aModel.getChiaveUfficio() != null)
			lCondizioni = " AND PROVVEDIMENTO_SIGE.CHIAVE_UFFICIO ='" + aModel.getChiaveUfficio() + "'";
		if (aModel.getCodTipoProvvedimento() != null)
			lCondizioni = " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO ='"
					+ aModel.getCodTipoProvvedimento() + "'";
		if (aModel.getDataEmissione() != null)
			lCondizioni = " AND PROVVEDIMENTO_SIGE.DATA_EMISSIONE ='" + aModel.getDataEmissione() + "'";

		return lCondizioni;
	}

	/**
	 * Calcola il Massimo CHIAVE_PROGR relativo al tipo Provvedimento, ad un certo ufficio e all'anno in
	 * corso. Il massimo CHIAVE_PROGR rappresenta l'ultimo CHIAVE_PROGR inserito all'interno dell'ufficio
	 * trattato.
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void getProgressivo(ProvvedimentoSigeModel aModel) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(CHIAVE_PROGR) aMAX";
		lStatement += " FROM PROVVEDIMENTO_SIGE ";
		lStatement += " WHERE CHIAVE_ANNO = " + aModel.getChiaveAnno();
		lStatement += " AND COD_TIPO_PROVVEDIMENTO = " + aModel.getCodTipoProvvedimento();
		lStatement += " AND COD_UFFICIO_INSERIMENTO = " + aModel.getCodUfficioInserimento();

		setStatement(lStatement);
	}

	/**
	 * <p>
	 * Metodo di ricerca che restituisce il numero di Provvedimenti Depositati relativi ad un Fascicolo SIGE.
	 * </p>
	 *
	 * @param BigDecimal
	 *            aIdFasSige : Identificativo Fascicolo SIGE
	 * @return int : numero provvedimenti trovati.
	 * @throws DAOException
	 */
	public int getNumProvSIGEDEpositati(BigDecimal aIdFasSius) throws DAOException {
		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = new String();
		// String lStatement = "select count(*) as COUNT from EVENTO where FLAG_DOCUMENTO_REGISTRATO = 'S' AND
		// DATA_TRASMISSIONE_ATTI IS NOT NULL AND FAS_SIU_ID_FASCICOLO_SIUS = " +aIdFasSius;
		lStatement += " SELECT count(*) as COUNT from PROVVEDIMENTO_SIGE p, EVENTO e ";
		lStatement += " WHERE p.FAS_ID_FASCICOLO_SIGE = '" + aIdFasSius + "'";
		lStatement += " AND p.COD_TIPO_PROVVEDIMENTO_SIGE <> '01' "; // dal conteggio vengono esclusi i
																		// decreti di fissazione udienza
																		// altrimenti non è possibile
																		// emmettere un'ordinanza
		lStatement += " AND p.DATA_DEPOSITO  IS NOT NULL ";
		lStatement += " AND p.ID_EVENTO_GENERATO = e.ID_EVENTO ";
		lStatement += " AND e.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		lStatement += " AND e.DATA_TRASMISSIONE_ATTI IS NOT NULL ";

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
	 * Description: metodo di ricerca dei Provvedimenti per i quali è possibile compilare il Foglio
	 * Complementare
	 * </p>
	 *
	 * @param aIdFasSige
	 * @param aTipiProvv
	 * @param aTipoEvento
	 * @throws DAOException
	 */
	public void ricercaProvvSigeXCFC(BigDecimal aIdFasSige, String aTipiProvv, String aTipoEvento)
			throws DAOException {
		String strQuery = "";
		strQuery += ricercaEventoProvvedimentoSigeSqlQuery(aIdFasSige);
		strQuery += setCondizioneXCFC(aTipiProvv, aTipoEvento);
		strQuery += setOrderDataEmissione();

		setStatement(strQuery);
	}

	public String setCondizioneXCFC(String aTipiProvv, String aTipoEvento) {
		String lCondizioni = new String();
		lCondizioni += " AND EVENTO.COD_TIPO_EVENTO = '" + aTipoEvento + "' ";
		lCondizioni += " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO_SIGE IN (" + aTipiProvv + ")";
		lCondizioni += " AND PROVVEDIMENTO_SIGE.COD_TIPO_PROVVEDIMENTO IN (" + aTipiProvv + ")";
		lCondizioni += " AND EVENTO.DATA_TRASMISSIONE_ATTI IS NOT NULL ";

		return lCondizioni;
	}

	public void ricercaAltriProvvByFascicoloSige(BigDecimal aIdFasSige, String aTipoEvento)
			throws DAOException {
		String strQuery = "";
		strQuery += ricercaAltriProvvByFascicoloSigeSqlQuery(aIdFasSige, aTipoEvento);

		setStatement(strQuery);
	}

	protected String ricercaAltriProvvByFascicoloSigeSqlQuery(BigDecimal aIdFasSige, String aTipoEvento)
			throws DAOException {
		String lStatement = new String("");

		lStatement += " SELECT * ";
		lStatement += " FROM PROVVEDIMENTO_SIGE P";
		lStatement += " INNER JOIN EVENTO E ON E.ID_EVENTO = P.ID_EVENTO_GENERATO ";
		lStatement += " WHERE P.FAS_ID_FASCICOLO_SIGE ='" + aIdFasSige + "'";
		lStatement += " AND E.COD_TIPO_EVENTO NOT IN (" + aTipoEvento + ")";
		lStatement += " ORDER BY P.DATA_EMISSIONE ";

		return lStatement;
	}

	public BigDecimal countOpposizioniAccolteByIdProvvedimento(BigDecimal idProvvedimento)
			throws DAOException {
		String selectCount = "SELECT count(*) as conteggio " + "FROM IMPUGNAZIONE_SIGE O "
				+ " WHERE O.PROVV_ID_PROVVEDIMENTO_SIGE = " + idProvvedimento
				+ " AND O.COD_TIPO_IMPUGNAZIONE = '04' AND O.COD_TENORE_DECISIONE = '10'";

		setStatement(selectCount);
		start();
		next();
		BigDecimal conteggio = super.getBigDecimal("conteggio");

		return conteggio;

	}

}