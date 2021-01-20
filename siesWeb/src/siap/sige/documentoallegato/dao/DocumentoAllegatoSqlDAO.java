package siap.sige.documentoallegato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sige.documentoallegato.model.DocumentoAllegatoModel;

public class DocumentoAllegatoSqlDAO extends SIAPSqlDAO {

	public DocumentoAllegatoSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void ricercaDocumentoAllegatoFCByIdFascicolo(BigDecimal aKey) throws DAOException {
		String lSql = "select doc.* from documento_allegato doc, PROVVEDIMENTO_SIGE prov where "
				+ "doc.cod_tipo_documento='06' and " + "nvl (doc.FLAG_DOCUMENTO_REGISTRATO, 'S') <> 'A' and "
				+ "doc.eve_id_evento=prov.id_evento_generato and "
				+ "prov.COD_TIPO_PROVVEDIMENTO_SIGE IN ('02','03') AND " + "prov.fas_id_fascicolo_sige="
				+ aKey;
		setStatement(lSql);
	}

	public void ricercaSollecitoByIdEvento(BigDecimal aKey) throws DAOException {
		ricercaDocumentoAllegatoByIdEvento(aKey);
	}

	public void ricercaDocumentoAllegatoByIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdEvento(aKey);
		setStatement(lSql);
	}

	public void ricercaDocumentoAllegatoFCByIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniFCByIdEvento(aKey);
		lSql += " order by ID_DOCUMENTO_ALLEGATO ";
		setStatement(lSql);
	}

	public void ricercaDocumentoAllegatoById(BigDecimal aKey) {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniFCById(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT ";
		lStatement += "ID_DOCUMENTO_ALLEGATO, ";
		lStatement += "DATA_EMISSIONE, ";
		lStatement += "COD_TIPO_DOCUMENTO, CODTIPDOC.RV_MEANING DESCR_TIPO_DOCUMENTO, ";
		lStatement += "NUMERO_PROGRESSIVO, ";
		lStatement += "NVL(FLAG_DOCUMENTO_REGISTRATO,'S') as FLAG_DOCUMENTO_REGISTRATO, ";
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
		// lStatement += " WHERE DOCUMENTO_ALLEGATO.COD_TIPO_DOCUMENTO = CODTIPDOC.RV_LOW_VALUE AND
		// CODTIPDOC.RV_DOMAIN = 'TIPO_DOCUMENTO_ALLEGATO'";

		// lStatement += " AND DOCUMENTO_ALLEGATO.CODI_MOTIVAZIONE_NON_INVIO = CODMOTIVNONINVIO.RV_LOW_VALUE";
		// lStatement += " AND CODMOTIVNONINVIO.RV_DOMAIN = 'MOTIVAZIONE_NON_INVIO_FC'";

		lStatement += " FROM DOCUMENTO_ALLEGATO ";
		lStatement += " join CG_REF_CODES CODTIPDOC on (DOCUMENTO_ALLEGATO.COD_TIPO_DOCUMENTO = CODTIPDOC.RV_LOW_VALUE and CODTIPDOC.RV_DOMAIN = 'TIPO_DOCUMENTO_ALLEGATO') ";
		lStatement += " left outer join CG_REF_CODES CODMOTIVNONINVIO ON (CODMOTIVNONINVIO.RV_LOW_VALUE = DOCUMENTO_ALLEGATO.CODI_MOTIVAZIONE_NON_INVIO and CODMOTIVNONINVIO.RV_DOMAIN = 'MOTIVAZIONE_NON_INVIO_FC')";
		lStatement += " WHERE 1 = 1 ";
		return lStatement;
	}

	public String setCondizioniFCById(BigDecimal aKey) {
		return " AND ID_DOCUMENTO_ALLEGATO = " + aKey;

	}

	public String setCondizioniFCByIdEvento(BigDecimal aKey) {
		return " AND EVE_ID_EVENTO = " + aKey + " and " + " COD_TIPO_DOCUMENTO='06'";
	}

	public String setCondizioniByIdEvento(BigDecimal aKey) {
		return " AND EVE_ID_EVENTO = " + aKey + " AND DATA_ANNULLAMENTO IS NULL";
	}

	public GenericModel getModel() throws DAOException {
		DocumentoAllegatoModel aModel = new DocumentoAllegatoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdDocumentoAllegato(getBigDecimal("ID_DOCUMENTO_ALLEGATO"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodTipoDocumento(getString("COD_TIPO_DOCUMENTO"));
		if (findColumn("DESCR_TIPO_DOCUMENTO"))
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

		if (findColumn("DESCR_MOTIV_NON_INVIO"))
			aModel.setDescrMotivazioneNonInvio(getString("DESCR_MOTIV_NON_INVIO"));

		aModel.setDescrizioneNonInvio(getString("DESCRIZIONE_NON_INVIO"));
		aModel.setDataUltInvio(getDate("DATA_ULT_INVIO"));
		aModel.setDataInsMan(getDate("DATA_INS_MAN"));

		return aModel;
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

	public BigDecimal countDecretiDepositoFissazioneUdienzaNonValidati(BigDecimal idFascicolo)
			throws DAOException {
		String sql = "select count (ev.id_evento) as numDec from UDIENZA_PROCEDIMENTO_SIGE ud,evento ev  where "
				+ "ud.fas_id_fascicolo_sige=" + idFascicolo + " and " + "ev.id_evento=ud.eve_id_evento and "
				+ "ev.cod_tipo_evento='01' and " + "ev.cod_tipo_provvedimento='02' and "
				+ "(ev.FLAG_DOCUMENTO_REGISTRATO is NULL or ev.FLAG_DOCUMENTO_REGISTRATO='N') ";

		super.setStatement(sql);
		super.start();

		super.next();
		BigDecimal numDec = super.getBigDecimal("numDec");
		super.stop();

		return numDec;
	}

	public BigDecimal countDecretiDepositoFissazioneUdienzaValidati(BigDecimal idFascicolo)
			throws DAOException {
		String sql = "select count (ev.id_evento) as numDec from UDIENZA_PROCEDIMENTO_SIGE ud,evento ev  where "
				+ "ud.fas_id_fascicolo_sige=" + idFascicolo + " and " + "ev.id_evento=ud.eve_id_evento and "
				+ "ev.cod_tipo_evento='01' and " + "ev.cod_tipo_provvedimento='02' and "
				+ "(ev.FLAG_DOCUMENTO_REGISTRATO is not NULL or ev.FLAG_DOCUMENTO_REGISTRATO='S') ";

		super.setStatement(sql);
		super.start();

		super.next();
		BigDecimal numDec = super.getBigDecimal("numDec");
		super.stop();

		return numDec;
	}

	// private boolean findColumn(String aValue) {
	// try{
	// mRs.findColumn(aValue);
	// }catch( Exception sqex){
	// return false;
	// }
	// return true;
	// }

}
