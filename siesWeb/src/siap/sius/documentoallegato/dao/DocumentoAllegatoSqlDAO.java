package siap.sius.documentoallegato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;

/**
 * <p>
 * Title: DocumentoAllegatoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella DocumentoAllegato
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
public class DocumentoAllegatoSqlDAO extends SIAPSqlDAO {

	public DocumentoAllegatoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaDocumentoAllegato(DocumentoAllegatoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaDocumentoAllegatoAnnullato(DocumentoAllegatoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneNulle(aModel);
		setStatement(lSql);
	}

	public void ricercaDocumentoAllegatoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaDocumentoAllegatoByIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdEvento(aKey);
		setStatement(lSql);
	}

	public void ricercaSollecitoByIdEvento(BigDecimal aKey) throws DAOException {
		ricercaDocumentoAllegatoByIdEvento(aKey);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT ";
		lStatement += "ID_DOCUMENTO_ALLEGATO, ";
		lStatement += "DATA_EMISSIONE, ";
		lStatement += "COD_TIPO_DOCUMENTO, CODTIPDOC.RV_MEANING DESCR_TIPO_DOCUMENTO, ";
		lStatement += "NUMERO_PROGRESSIVO, ";
		lStatement += "FLAG_DOCUMENTO_REGISTRATO, ";
		lStatement += "DOC_BLOB, ";
		lStatement += "COD_OPERATORE_INSERIMENTO, ";
		lStatement += "DATA_INSERIMENTO, ";
		lStatement += "COD_UFFICIO_INSERIMENTO, ";
		lStatement += "COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "DATA_AGGIORNAMENTO, ";
		lStatement += "COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += "EVE_ID_EVENTO, ";
		lStatement += "TEM_ID_TEMPLATE, ";
		lStatement += "ANNO_FOGLIO_COMPLEMENTARE, ";
		lStatement += "PROGR_FOGLIO_COMPLEMENTARE, ";
		lStatement += "DATA_TRASMISSIONE, ";
		lStatement += "DATA_ANNULLAMENTO, MOTIVO_ANNULLAMENTO, COMUNE_SEDE_GIUDIZIARIA ";
		lStatement += ",CODI_MOTIVAZIONE_NON_INVIO, DESCRIZIONE_NON_INVIO, ";
		lStatement += "CODMOTIVNONINVIO.RV_MEANING DESCR_MOTIV_NON_INVIO, ";
		lStatement += "DATA_ULT_INVIO, DATA_INS_MAN ";
		// lStatement += " FROM DOCUMENTO_ALLEGATO, CG_REF_CODES CODTIPDOC ";
		// lStatement += " , CG_REF_CODES CODMOTIVNONINVIO ";
		// lStatement +=
		// " WHERE DOCUMENTO_ALLEGATO.COD_TIPO_DOCUMENTO = CODTIPDOC.RV_LOW_VALUE AND CODTIPDOC.RV_DOMAIN =
		// 'TIPO_DOCUMENTO_ALLEGATO'";

		// lStatement += " AND DOCUMENTO_ALLEGATO.CODI_MOTIVAZIONE_NON_INVIO = CODMOTIVNONINVIO.RV_LOW_VALUE";
		// lStatement += " AND CODMOTIVNONINVIO.RV_DOMAIN = 'MOTIVAZIONE_NON_INVIO_FC'";

		lStatement += " FROM DOCUMENTO_ALLEGATO ";
		lStatement += " join CG_REF_CODES CODTIPDOC on (DOCUMENTO_ALLEGATO.COD_TIPO_DOCUMENTO = CODTIPDOC.RV_LOW_VALUE and CODTIPDOC.RV_DOMAIN = 'TIPO_DOCUMENTO_ALLEGATO') ";
		lStatement += " left outer join CG_REF_CODES CODMOTIVNONINVIO ON (CODMOTIVNONINVIO.RV_LOW_VALUE = DOCUMENTO_ALLEGATO.CODI_MOTIVAZIONE_NON_INVIO and CODMOTIVNONINVIO.RV_DOMAIN = 'MOTIVAZIONE_NON_INVIO_FC')";
		lStatement += " WHERE 1 = 1 ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		DocumentoAllegatoModel aModel = new DocumentoAllegatoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdDocumentoAllegato(getBigDecimal("ID_DOCUMENTO_ALLEGATO"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodTipoDocumento(getString("COD_TIPO_DOCUMENTO"));
		aModel.setDescrTipoDocumento(getString("DESCR_TIPO_DOCUMENTO"));
		aModel.setNumeroProgressivo(getBigDecimal("NUMERO_PROGRESSIVO"));
		aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
		// aModel.setDocBlob(getBlob("DOC_BLOB") );
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
		aModel.setAnnoFoglioComplementare(getBigDecimal("ANNO_FOGLIO_COMPLEMENTARE"));
		aModel.setProgrFoglioComplementare(getBigDecimal("PROGR_FOGLIO_COMPLEMENTARE"));
		aModel.setDataTrasmissione(getDate("DATA_TRASMISSIONE"));
		aModel.setDataAnnullamento(getDate("DATA_ANNULLAMENTO"));
		aModel.setMotivoAnnullamento(getString("MOTIVO_ANNULLAMENTO"));
		aModel.setComuneSedeGiudiziaria(getString("COMUNE_SEDE_GIUDIZIARIA"));
		aModel.setCodMotivazioneNonInvio(getString("CODI_MOTIVAZIONE_NON_INVIO"));
		aModel.setDescrMotivazioneNonInvio(getString("DESCR_MOTIV_NON_INVIO"));
		aModel.setDescrizioneNonInvio(getString("DESCRIZIONE_NON_INVIO"));
		aModel.setDataUltInvio(getDate("DATA_ULT_INVIO"));
		aModel.setDataInsMan(getDate("DATA_INS_MAN"));

		return aModel;
	}

	public String setCondizione(DocumentoAllegatoModel aModel) {
		String lCondizioni = new String();

		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " AND EVE_ID_EVENTO =" + aModel.getEveIdEvento();
		}

		if (aModel.getCodTipoDocumento() != null) {
			if (aModel.getCodTipoDocumento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_DOCUMENTO ='" + aModel.getCodTipoDocumento() + "'";
		}
		lCondizioni += " AND DATA_ANNULLAMENTO IS NULL ";
		return lCondizioni;
	}

	public String setCondizioneNulle(DocumentoAllegatoModel aModel) {
		String lCondizioni = new String();

		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " AND EVE_ID_EVENTO =" + aModel.getEveIdEvento();
		}

		if (aModel.getCodTipoDocumento() != null) {
			if (aModel.getCodTipoDocumento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_DOCUMENTO ='" + aModel.getCodTipoDocumento() + "'";
		}
		lCondizioni += " AND DATA_ANNULLAMENTO IS NOT NULL ";
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_DOCUMENTO_ALLEGATO = " + aKey;
	}

	public String setCondizioniByIdEvento(BigDecimal aKey) {
		return " AND EVE_ID_EVENTO = " + aKey + " AND DATA_ANNULLAMENTO IS NULL";
	}

	public BigDecimal getProgressivo(DocumentoAllegatoModel aDocumentoAllegato) throws DAOException {

		String lStatement = new String();

		lStatement += " SELECT MAX(NUMERO_PROGRESSIVO) aMAX";
		lStatement += " FROM DOCUMENTO_ALLEGATO ";
		lStatement += " WHERE DOCUMENTO_ALLEGATO.COD_UFFICIO_INSERIMENTO = '"
				+ aDocumentoAllegato.getCodUfficioInserimento() + "'";
		lStatement += " AND DOCUMENTO_ALLEGATO.EVE_ID_EVENTO = '" + aDocumentoAllegato.getEveIdEvento() + "'";

		setStatement(lStatement);
		this.start();

		BigDecimal lBigDec = new BigDecimal(0);

		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lBigDec = this.getBigDecimal("aMAX");

		this.stop();

		if (lBigDec == null)
			lBigDec = new BigDecimal(0);

		return lBigDec;
	}

	public BigDecimal getProgressivoByUff(String aKey, BigDecimal aAnno) throws DAOException {

		String lStatement = "SELECT DISTINCT nvl(MAX(PROGR_FOGLIO_COMPLEMENTARE),0) AS MAX_PROG FROM DOCUMENTO_ALLEGATO";
		lStatement += " WHERE COD_TIPO_DOCUMENTO = '06' AND COD_UFFICIO_INSERIMENTO = '" + aKey
				+ "' AND ANNO_FOGLIO_COMPLEMENTARE = '" + aAnno.toString() + "'";

		setStatement(lStatement);
		this.start();

		BigDecimal lBigDec = new BigDecimal(0);

		if (this.next() && (this.getBigDecimal("MAX_PROG") != null))
			lBigDec = this.getBigDecimal("MAX_PROG");

		this.stop();

		if (lBigDec == null)
			lBigDec = new BigDecimal(0);

		return lBigDec;
	}

	public String setCondizioni(DocumentoAllegatoModel aModel) {

		String lCondizioni = new String();

		if (aModel.getEveIdEvento() != null) {
			lCondizioni += " AND EVE_ID_EVENTO=" + aModel.getEveIdEvento();
		}
		if (aModel.getCodTipoDocumento() != null) {
			if (aModel.getCodTipoDocumento().compareTo("") != 0)
				lCondizioni += " AND COD_TIPO_DOCUMENTO =" + aModel.getCodTipoDocumento();
		}

		if (aModel.getDataEmissione() != null) {
			if (!aModel.getDataEmissione().toString().equals(""))
				lCondizioni += " AND DATA_EMISSIONE =" + aModel.getDataEmissione();
		}

		return lCondizioni;
	}

	public void ricercaDocumentoAllegatoByIdEventoAndCodTipoDoc(BigDecimal aKey, String codTipoDocumento)
			throws DAOException {

		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdEventoAndCodTipoDoc(aKey, codTipoDocumento);
		setStatement(lSql);
	}

	public String setCondizioniByIdEventoAndCodTipoDoc(BigDecimal aKey, String codTipoDocumento) {

		return " AND EVE_ID_EVENTO = " + aKey + "AND COD_TIPO_DOCUMENTO = " + codTipoDocumento
				+ " AND DATA_ANNULLAMENTO IS NULL";
	}

	public void ricercaDocumentoAllegatoByIdEventoAndCodTipoDocTrasmesso(BigDecimal aKey,
			String codTipoDocumento) throws DAOException {

		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdEventoAndCodTipoDocTrasmesso(aKey, codTipoDocumento);
		setStatement(lSql);
	}

	public String setCondizioniByIdEventoAndCodTipoDocTrasmesso(BigDecimal aKey, String codTipoDocumento) {

		return " AND EVE_ID_EVENTO = " + aKey + "AND COD_TIPO_DOCUMENTO = " + codTipoDocumento
				+ " AND DATA_ANNULLAMENTO IS NULL AND DATA_TRASMISSIONE IS NOT NULL";
	}

	public void verificoEsitoTrasmissioneFC(BigDecimal aKey) {

		String lStatement = new String("");

		lStatement += " SELECT COUNT(*) HowManyRecords ";
		lStatement += " FROM LOG_TRASFERIMENTO_ESECUZIONE A";
		lStatement += " WHERE A.CHIAVE_SIES = " + aKey;
		lStatement += " AND A.COMPLETATO = '1'";
		lStatement += " AND A.OPERAZIONE <> 'DELETE'";
		lStatement += " AND A.DATA_OPERAZIONE = (SELECT MAX(DATA_OPERAZIONE) FROM LOG_TRASFERIMENTO_ESECUZIONE B WHERE B.CHIAVE_SIES = "
				+ aKey + ")";

		setStatement(lStatement);
	}

	/**
	 * MERGE v10: aggiunto metodo di ricerca senza condizioni
	 *
	 * @param lDocAllMod
	 */
	public void ricDocAllByIdEvento(DocumentoAllegatoModel lDocAllMod) {

		String lSql = getSqlQuery();

		lSql += " AND EVE_ID_EVENTO = " + lDocAllMod.getEveIdEvento();
		setStatement(lSql);
	}

}