package siap.sius.generaleprocedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.dao.SIAPSqlDAO;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;

/**
 * Title: GeneraleProcedimentoSqlDAO
 * Description: Classe SqlDAO che rappresenta la tabella GeneraleProcedimento
 * 
 * @version 1.0
 */
public class GeneraleProcedimentoSqlDAO extends SIAPSqlDAO {

	public GeneraleProcedimentoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaGeneraleProcedimento(GeneraleProcedimentoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaGeneraleProcedimentoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_GENERALE_PROCEDIMENTO, " + "ANNO_S1, " + "PROGR_S1, "
				+ "COD_TIPO_REGISTRO, " + "COD_OGGETTO_PROCEDIMENTO, " + "DATA_RICHIESTA, "
				+ "DATA_ARRIVO_CANCELLERIA, " + "DATA_CAMERA_CONSIGLIO, " + "DESCR_RICHIESTA_DELEGAZIONE, "
				+ "COD_AUTORITA_DELEGATA, " + "DATA_RESTITUZ_DELEGAZIONE, " + "DATA_RICORSO_IMPUGN, "
				+ "DATA_INVIO_ATTI_IMPUGN, " + "DATA_INVIO_ESECUZ_PROVVISORIA, "
				+ "DATA_INVIO_ESECUZ_ORDINARIA, " + "DATA_COMPILAZ_COMPLEMENTARE, "
				+ "COD_TIPO_FOGLIO_COMPLEMENTARE, " + "DATA_ANNOTAZIONE, " + "ANNOTAZIONE, "
				+ "TIPO_DEFINIZIONE, " + "DATA_DEFINIZIONE, " + "DESCR_DEFINIZIONE, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "COD_TIPO_ATTO, " + "COD_SEDE_MITTENTE, " + "COD_TIPO_MITTENTE_ATTO, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS, " + "SEZIONE, " + "DATA_FINE_PENA, "
				+ "COD_POSIZIONE_GIURIDICA, " + "UDI_ID_UDIENZA, " + "DESCR_MITTENTE, "
				// MEV_9: aggiunti campi in estrazione
				+ "DATA_RESTITUZIONE, " + "DESCR_RESTITUZIONE";

		lStatement += " FROM GENERALE_PROCEDIMENTO";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		GeneraleProcedimentoModel aModel = new GeneraleProcedimentoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdGeneraleProcedimento(getBigDecimal("ID_GENERALE_PROCEDIMENTO"));
		aModel.setAnnoS1(getBigDecimal("ANNO_S1"));
		aModel.setProgrS1(getBigDecimal("PROGR_S1"));
		aModel.setCodTipoRegistro(getString("COD_TIPO_REGISTRO"));
		aModel.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		aModel.setDataRichiesta(getDate("DATA_RICHIESTA"));
		aModel.setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA"));
		aModel.setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));
		aModel.setDescrRichiestaDelegazione(getString("DESCR_RICHIESTA_DELEGAZIONE"));
		aModel.setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));
		aModel.setDataRestituzDelegazione(getDate("DATA_RESTITUZ_DELEGAZIONE"));
		aModel.setDataRicorsoImpugn(getDate("DATA_RICORSO_IMPUGN"));
		aModel.setDataInvioAttiImpugn(getDate("DATA_INVIO_ATTI_IMPUGN"));
		aModel.setDataInvioEsecuzProvvisoria(getDate("DATA_INVIO_ESECUZ_PROVVISORIA"));
		aModel.setDataInvioEsecuzOrdinaria(getDate("DATA_INVIO_ESECUZ_ORDINARIA"));
		aModel.setDataCompilazComplementare(getDate("DATA_COMPILAZ_COMPLEMENTARE"));
		aModel.setCodTipoFoglioComplementare(getString("COD_TIPO_FOGLIO_COMPLEMENTARE"));
		aModel.setDataAnnotazione(getDate("DATA_ANNOTAZIONE"));
		aModel.setAnnotazione(getString("ANNOTAZIONE"));
		aModel.setTipoDefinizione(getString("TIPO_DEFINIZIONE"));
		aModel.setDataDefinizione(getDate("DATA_DEFINIZIONE"));
		aModel.setDescrDefinizione(getString("DESCR_DEFINIZIONE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setCodTipoAtto(getString("COD_TIPO_ATTO"));
		aModel.setCodSedeMittente(getString("COD_SEDE_MITTENTE"));
		aModel.setCodTipoMittenteAtto(getString("COD_TIPO_MITTENTE_ATTO"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setSezione(getString("SEZIONE"));
		aModel.setDataFinePena(getDate("DATA_FINE_PENA"));
		aModel.setCodPosGiuridica(getString("COD_POSIZIONE_GIURIDICA"));
		aModel.setUdiIdUdienza(getBigDecimal("UDI_ID_UDIENZA"));
		aModel.setDescrMittente(getString("DESCR_MITTENTE"));
		// MEV: aggiunti campi in estrazione
		aModel.setDataRestituzione(getDate("DATA_RESTITUZIONE"));
		aModel.setDescrRestituzione(getString("DESCR_RESTITUZIONE"));
		
		return aModel;
	}

	public String setCondizione(GeneraleProcedimentoModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;

		if (aModel.getIdGeneraleProcedimento() != null) {
			lCondizioni += " ID_GENERALE_PROCEDIMENTO = " + aModel.getIdGeneraleProcedimento();
			lInserito = true;
		}

		if (aModel.getAnnoS1() != null) {
			if (lInserito)
				lCondizioni += " AND ";
			else
				lInserito = true;
			lCondizioni += " ANNO_S1 = " + aModel.getAnnoS1();
		}
		if (aModel.getProgrS1() != null) {
			if (lInserito)
				lCondizioni += " AND ";
			else
				lInserito = true;
			lCondizioni += " PROGR_S1 = " + aModel.getProgrS1();
		}
		if (aModel.getCodUfficioInserimento() != null
				&& aModel.getCodUfficioInserimento().trim().length() > 1) {
			if (lInserito)
				lCondizioni += " AND ";
			else
				lInserito = true;
			lCondizioni += " COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
		}
		if (aModel.getCodUfficioAggiornamento() != null
				&& aModel.getCodUfficioAggiornamento().trim().length() > 1) {
			if (lInserito)
				lCondizioni += " AND ";
			else
				lInserito = true;
			lCondizioni += " COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "'";
		}
		if (aModel.getCodOggettoProcedimento() != null
				&& aModel.getCodOggettoProcedimento().trim().length() > 1) {
			if (lInserito)
				lCondizioni += " AND ";
			else
				lInserito = true;
			lCondizioni += " COD_OGGETTO_PROCEDIMENTO = '" + aModel.getCodOggettoProcedimento() + "'";
		}
		// Ordinamento
		lCondizioni += " ORDER BY DATA_INSERIMENTO";

		if (lInserito)
			lCondizioni = " WHERE " + lCondizioni;

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " WHERE ID_GENERALE_PROCEDIMENTO = " + aKey;
	}

	/**
	 * Calcola il Massimo Progressivo relativo all'anno in corso. Il massimo progressivo rappresenta anche
	 * l'ultimo progressivo inserito.
	 *
	 * @param aGPModel
	 * @throws DAOException
	 */
	public void getProgrS1(GeneraleProcedimentoModel aGPModel) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_S1) aMAX";
		lStatement += " FROM GENERALE_PROCEDIMENTO GP";
		lStatement += " WHERE GP.ANNO_S1 = " + aGPModel.getAnnoS1();
		lStatement += " AND GP.COD_UFFICIO_INSERIMENTO = " + aGPModel.getCodUfficioInserimento();

		setStatement(lStatement);
	}

	/**
	 * Calcola il Massimo Progressivo relativo all'anno in corso. Il massimo progressivo rappresenta anche
	 * l'ultimo progressivo inserito.
	 *
	 * @param aGPModel
	 * @throws DAOException
	 */
	public void getProgrS1TipoReg(GeneraleProcedimentoModel aGPModel) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_S1) aMAX";
		lStatement += " FROM GENERALE_PROCEDIMENTO GP";
		lStatement += " WHERE GP.ANNO_S1 = " + aGPModel.getAnnoS1();
		lStatement += " AND GP.COD_UFFICIO_INSERIMENTO = " + aGPModel.getCodUfficioInserimento();
		lStatement += " AND GP.COD_TIPO_REGISTRO = '" + aGPModel.getCodTipoRegistro() + "'";

		setStatement(lStatement);
	}
	/*
	 * 19/07/2007 non più usato. public boolean ExistProcedimentoEsecuzione(BigDecimal aChiaveAnno, BigDecimal
	 * aChiaveProgr, BigDecimal aIdSoggetto, String ufficioUtenteConnesso ) throws DAOException { String
	 * S22="S22"; String U004="U004"; String lStatement = "select count(*) as COUNT "; lStatement+=
	 * " from GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FS, SOGGETTO SOG1, SOGGETTO SOG2"; lStatement+=
	 * " where GP.PROGR_S1 = '"+aChiaveProgr+"' and GP.ANNO_S1 = '"+aChiaveAnno+"'"; lStatement+=
	 * " and GP.COD_UFFICIO_INSERIMENTO = '"+ufficioUtenteConnesso+"'"; lStatement+=
	 * " and GP.COD_TIPO_REGISTRO='"+S22+"'"; lStatement+= " and GP.COD_OGGETTO_PROCEDIMENTO ='"+U004+"'";
	 * lStatement+= " and GP.FAS_SIU_ID_FASCICOLO_SIUS = FS.ID_FASCICOLO_SIUS "; lStatement+=
	 * " and FS.SOG_ID_SOGGETTO = SOG1.ID_SOGGETTO"; lStatement+= " and SOG2.ID_SOGGETTO='"+aIdSoggetto+"'";
	 * lStatement+= " and SOG1.NOME = SOG2.NOME"; lStatement+= " and SOG1.COGNOME = SOG2.COGNOME";
	 *
	 * setStatement( lStatement );
	 *
	 * this.start();
	 *
	 * BigDecimal lCount = null; if( this.next() ) lCount = this.getBigDecimal("COUNT");
	 *
	 * if( lCount.intValue() > 0 ) return true; else return false; }
	 */

	/**
	 * Verifica l'esistenza del procedimento di Esecuzione (MA, SS, o altri eventuali). 19/07/2007
	 *
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @param aCodContenuto
	 * @param aTipoRegistro
	 * @param aIdSoggetto
	 * @param ufficioUtenteConnesso
	 * @throws DAOException
	 */
	public boolean ExistProcedimentoEsecuzione(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr,
			String aCodContenuto, String aTipoRegistro, BigDecimal aIdSoggetto, String ufficioUtenteConnesso)
			throws DAOException {
		String lStatement = "select count(*) as COUNT ";
		lStatement += " from GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FS, SOGGETTO SOG1, SOGGETTO SOG2";
		lStatement += " where GP.PROGR_S1 = '" + aChiaveProgr + "' and GP.ANNO_S1 = '" + aChiaveAnno + "'";
		lStatement += " and GP.COD_UFFICIO_INSERIMENTO = '" + ufficioUtenteConnesso + "'";
		lStatement += " and GP.COD_TIPO_REGISTRO='" + aTipoRegistro + "'";
		lStatement += " and GP.COD_OGGETTO_PROCEDIMENTO ='" + aCodContenuto + "'";
		lStatement += " and GP.FAS_SIU_ID_FASCICOLO_SIUS = FS.ID_FASCICOLO_SIUS ";
		lStatement += " and FS.SOG_ID_SOGGETTO = SOG1.ID_SOGGETTO";
		lStatement += " and SOG2.ID_SOGGETTO='" + aIdSoggetto + "'";
		lStatement += " and SOG1.NOME = SOG2.NOME";
		lStatement += " and SOG1.COGNOME = SOG2.COGNOME";

		setStatement(lStatement);

		this.start();

		BigDecimal lCount = null;
		if (this.next())
			lCount = this.getBigDecimal("COUNT");

		if (lCount.intValue() > 0)
			return true;
		else
			return false;
	}

	public void ricercaGeneraleProcedimentoByIdFas(BigDecimal aIdFas) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " WHERE FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFas;
		setStatement(lSql);
	}

	/**
	 * Metodo di ricerca di un GENERALE_PROCEDIMENTO con stessi: COD_TIPO_ATTO, DATA_RICHIESTA,
	 * COD_TIPO_MITTENTE_ATTO, COD_SEDE_MITTENTE, COD_OGGETTO_PROCEDIMENTO, DATA_ARRIVO_CANCELLERIA.
	 * <p>
	 *
	 * @param aCodTipoAtto
	 * @param aDataRichiesta
	 * @param aCodTipoMittenteAtto
	 * @param aCodSedeMittente
	 * @param aCodOggettoProcedimento
	 * @param aDataArrivoCancelleria
	 * @throws DAOException
	 * @return boolean
	 */
	public String ExistAttoSius(BigDecimal aIdSoggetto, String aCodTipoAtto, Date aDataRichiesta,
			String aCodTipoMittenteAtto, String aCodSedeMittente, String aCodOggettoProcedimento,
			Date aDataArrivoCancelleria, String aChiaveUfficio) throws DAOException {
		String response = "";
		String lStatement = "select FS.CHIAVE_ANNO CHIAVE_ANNO, FS.CHIAVE_PROGR ";
		lStatement += " from GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FS, SOGGETTO SG ";
		lStatement += " where GP.COD_TIPO_ATTO = '" + aCodTipoAtto + "' ";
		if (DateUtils.getDateToString(aDataRichiesta, "ddMMyyyy") != null)
			lStatement += " and GP.DATA_RICHIESTA = TO_DATE('"
					+ DateUtils.getDateToString(aDataRichiesta, "ddMMyyyy") + "', 'DDMMYYYY') ";
		lStatement += " and FS.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS and FS.SOG_ID_SOGGETTO ='"
				+ aIdSoggetto + "' and FS.SOG_ID_SOGGETTO = SG.ID_SOGGETTO";
		lStatement += " and GP.COD_TIPO_MITTENTE_ATTO = '" + aCodTipoMittenteAtto
				+ "' and GP.COD_SEDE_MITTENTE='" + aCodSedeMittente + "' and FS.CHIAVE_UFFICIO ='"
				+ aChiaveUfficio + "'";
		lStatement += " and GP.COD_OGGETTO_PROCEDIMENTO = '" + aCodOggettoProcedimento
				+ "' and GP.DATA_ARRIVO_CANCELLERIA = TO_DATE('"
				+ DateUtils.getDateToString(aDataArrivoCancelleria, "ddMMyyyy") + "', 'DDMMYYYY') ";

		setStatement(lStatement);

		this.start();

		// BigDecimal lCount = null;
		if (this.next()) {
			response = this.getBigDecimal("CHIAVE_ANNO").toString() + "/"
					+ this.getBigDecimal("CHIAVE_PROGR").toString();
			return response;
		} else
			return response;
	}

}