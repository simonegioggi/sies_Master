package siap.sius.documentoallegato.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: DocumentoAllegatoDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella DocumentoAllegato
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

public class DocumentoAllegatoDAO extends TableDAO {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public DocumentoAllegatoDAO(Connection con) {
		super(con);
		setTable("DOCUMENTO_ALLEGATO");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID_DOCUMENTO_ALLEGATO", "DOC_ALL_SEQ");
		setFieldKey("ID_DOCUMENTO_ALLEGATO", BIG_DECIMAL);

		setField("ID_DOCUMENTO_ALLEGATO", BIG_DECIMAL);
		setField("DATA_EMISSIONE", DATE);
		setField("COD_TIPO_DOCUMENTO", STRING);
		setField("NUMERO_PROGRESSIVO", BIG_DECIMAL);
		setField("FLAG_DOCUMENTO_REGISTRATO", STRING);
		setField("DOC_BLOB", TBLOB);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("EVE_ID_EVENTO", BIG_DECIMAL);
		setField("TEM_ID_TEMPLATE", STRING);
		setField("ANNO_FOGLIO_COMPLEMENTARE", BIG_DECIMAL);
		setField("PROGR_FOGLIO_COMPLEMENTARE", BIG_DECIMAL);
		setField("DATA_TRASMISSIONE", DATE);
		setField("DATA_ANNULLAMENTO", DATE);
		setField("MOTIVO_ANNULLAMENTO", STRING);
		setField("COMUNE_SEDE_GIUDIZIARIA", STRING);
		setField("CODI_MOTIVAZIONE_NON_INVIO", STRING);
		setField("DESCRIZIONE_NON_INVIO", STRING);
		setField("DATA_ULT_INVIO", DATE);
		setField("DATA_INS_MAN", DATE);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdDocumentoAllegato() throws DAOException {
		return getBigDecimal("ID_DOCUMENTO_ALLEGATO");
	}

	public Date getDataEmissione() throws DAOException {
		return getDate("DATA_EMISSIONE");
	}

	public String getCodTipoDocumento() throws DAOException {
		return getString("COD_TIPO_DOCUMENTO");
	}

	public BigDecimal getNumeroProgressivo() throws DAOException {
		return getBigDecimal("NUMERO_PROGRESSIVO");
	}

	public String getFlagDocumentoRegistrato() throws DAOException {
		return getString("FLAG_DOCUMENTO_REGISTRATO");
	}

	// public Blob getDocBlob() throws DAOException { return getBlob("DOC_BLOB"); }
	public ByteArrayOutputStream getDocBlob() throws DAOException {
		return getBlob("DOC_BLOB");
	}

	public String getCodOperatoreInserimento() throws DAOException {
		return getString("COD_OPERATORE_INSERIMENTO");
	}

	public Date getDataInserimento() throws DAOException {
		return getDate("DATA_INSERIMENTO");
	}

	public String getCodUfficioInserimento() throws DAOException {
		return getString("COD_UFFICIO_INSERIMENTO");
	}

	public String getCodOperatoreAggiornamento() throws DAOException {
		return getString("COD_OPERATORE_AGGIORNAMENTO");
	}

	public Date getDataAggiornamento() throws DAOException {
		return getDate("DATA_AGGIORNAMENTO");
	}

	public String getCodUfficioAggiornamento() throws DAOException {
		return getString("COD_UFFICIO_AGGIORNAMENTO");
	}

	public BigDecimal getEveIdEvento() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO");
	}

	public String getTemIdTemplate() throws DAOException {
		return getString("TEM_ID_TEMPLATE");
	}

	public BigDecimal getAnnoFoglioComplementare() throws DAOException {
		return getBigDecimal("ANNO_FOGLIO_COMPLEMENTARE");
	}

	public BigDecimal getProgrFoglioComplementare() throws DAOException {
		return getBigDecimal("PROGR_FOGLIO_COMPLEMENTARE");
	}

	public Date getDataTrasmissione() throws DAOException {
		return getDate("DATA_TRASMISSIONE");
	}

	public Date getDataAnnullamento() throws DAOException {
		return getDate("DATA_ANNULLAMENTO");
	}

	public String getMotivoAnnullamento() throws DAOException {
		return getString("MOTIVO_ANNULLAMENTO");
	}

	public String getComuneSedeGiudiziaria() throws DAOException {
		return getString("COMUNE_SEDE_GIUDIZIARIA");
	}

	public String getCodMotivazioneNonInvio() throws DAOException {
		return getString("CODI_MOTIVAZIONE_NON_INVIO");
	}

	public String getDescrizioneNonInvio() throws DAOException {
		return getString("DESCRIZIONE_NON_INVIO");
	}

	public Date getDataUltInvio() throws DAOException {
		return getDate("DATA_ULT_INVIO");
	}

	public Date getDataInsMan() throws DAOException {
		return getDate("DATA_INS_MAN");
	}

	//
	// METODI SET()
	//
	public void setIdDocumentoAllegato(BigDecimal aValore) {
		setBigDecimal("ID_DOCUMENTO_ALLEGATO", aValore);
	}

	public void setDataEmissione(Date aValore) {
		setDate("DATA_EMISSIONE", aValore);
	}

	public void setCodTipoDocumento(String aValore) {
		setString("COD_TIPO_DOCUMENTO", aValore);
	}

	public void setNumeroProgressivo(BigDecimal aValore) {
		setBigDecimal("NUMERO_PROGRESSIVO", aValore);
	}

	public void setFlagDocumentoRegistrato(String aValore) {
		setString("FLAG_DOCUMENTO_REGISTRATO", aValore);
	}

	// public void setDocBlob(Blob aValore ) { setBlob("DOC_BLOB", aValore); }
	public void setDocBlob(ByteArrayInputStream aValore) {
		setBlob("DOC_BLOB", aValore);
	}

	public void setCodOperatoreInserimento(String aValore) {
		setString("COD_OPERATORE_INSERIMENTO", aValore);
	}

	public void setDataInserimento(Date aValore) {
		setDate("DATA_INSERIMENTO", aValore);
	}

	public void setCodUfficioInserimento(String aValore) {
		setString("COD_UFFICIO_INSERIMENTO", aValore);
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		setString("COD_OPERATORE_AGGIORNAMENTO", aValore);
	}

	public void setDataAggiornamento(Date aValore) {
		setDate("DATA_AGGIORNAMENTO", aValore);
	}

	public void setCodUfficioAggiornamento(String aValore) {
		setString("COD_UFFICIO_AGGIORNAMENTO", aValore);
	}

	public void setEveIdEvento(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO", aValore);
	}

	public void setTemIdTemplate(String aValore) {
		setString("TEM_ID_TEMPLATE", aValore);
	}

	public void setAnnoFoglioComplementare(BigDecimal aValore) {
		setBigDecimal("ANNO_FOGLIO_COMPLEMENTARE", aValore);
	}

	public void setProgrFoglioComplementare(BigDecimal aValore) {
		setBigDecimal("PROGR_FOGLIO_COMPLEMENTARE", aValore);
	}

	public void setDataTrasmissione(Date aValore) {
		setDate("DATA_TRASMISSIONE", aValore);
	}

	public void setDataAnnullamento(Date aValore) {
		setDate("DATA_ANNULLAMENTO", aValore);
	}

	public void setMotivoAnnullamento(String aValore) {
		setString("MOTIVO_ANNULLAMENTO", aValore);
	}

	public void setComuneSedeGiudiziaria(String aValore) {
		setString("COMUNE_SEDE_GIUDIZIARIA", aValore);
	}

	public void setCodMotivazioneNonInvio(String aValore) {
		setString("CODI_MOTIVAZIONE_NON_INVIO", aValore);
	}

	public void setDescrizioneNonInvio(String aValore) {
		setString("DESCRIZIONE_NON_INVIO", aValore);
	}

	public void setDataUltInvio(Date aValore) {
		setDate("DATA_ULT_INVIO", aValore);
	}

	public void setDataInsMan(Date aValore) {
		setDate("DATA_INS_MAN", aValore);
	}

	public void setDAOFromModel(DocumentoAllegatoModel aModel) throws DAOException {
		setIdDocumentoAllegato(aModel.getIdDocumentoAllegato());
		setDataEmissione(aModel.getDataEmissione());
		setCodTipoDocumento(aModel.getCodTipoDocumento());
		setNumeroProgressivo(aModel.getNumeroProgressivo());
		setFlagDocumentoRegistrato(aModel.getFlagDocumentoRegistrato());
		setDocBlob(aModel.getDocBlobIn());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setEveIdEvento(aModel.getEveIdEvento());
		setTemIdTemplate(aModel.getTemIdTemplate());
		setAnnoFoglioComplementare(aModel.getAnnoFoglioComplementare());
		setProgrFoglioComplementare(aModel.getProgrFoglioComplementare());
		setDataTrasmissione(aModel.getDataTrasmissione());
		setDataAnnullamento(aModel.getDataAnnullamento());
		setMotivoAnnullamento(aModel.getMotivoAnnullamento());
		setComuneSedeGiudiziaria(aModel.getComuneSedeGiudiziaria());
		setCodMotivazioneNonInvio(aModel.getCodMotivazioneNonInvio());
		setDescrizioneNonInvio(aModel.getDescrizioneNonInvio());
		setDataUltInvio(aModel.getDataUltInvio());
		setDataInsMan(aModel.getDataInsMan());
	}

	public void setDAOFromModelForUpdate(DocumentoAllegatoModel aModel) throws DAOException {
		setIdDocumentoAllegato(aModel.getIdDocumentoAllegato());
		setDataEmissione(aModel.getDataEmissione());
		setCodTipoDocumento(aModel.getCodTipoDocumento());
		setNumeroProgressivo(aModel.getNumeroProgressivo());
		setFlagDocumentoRegistrato(aModel.getFlagDocumentoRegistrato());
		setDocBlob(aModel.getDocBlobIn());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setEveIdEvento(aModel.getEveIdEvento());
		setTemIdTemplate(aModel.getTemIdTemplate());
		setCondizioneUpdate(aModel.getIdDocumentoAllegato());
		setAnnoFoglioComplementare(aModel.getAnnoFoglioComplementare());
		setProgrFoglioComplementare(aModel.getProgrFoglioComplementare());
		setDataTrasmissione(aModel.getDataTrasmissione());
		setDataAnnullamento(aModel.getDataAnnullamento());
		setMotivoAnnullamento(aModel.getMotivoAnnullamento());
		setComuneSedeGiudiziaria(aModel.getComuneSedeGiudiziaria());
		setCodMotivazioneNonInvio(aModel.getCodMotivazioneNonInvio());
		setDescrizioneNonInvio(aModel.getDescrizioneNonInvio());
		setDataUltInvio(aModel.getDataUltInvio());
		setDataInsMan(aModel.getDataInsMan());

	}

	public void setDAOFromModelForUpdateBlob(DocumentoAllegatoModel aModel) throws DAOException {
		setDocBlob(aModel.getDocBlobIn());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setFlagDocumentoRegistrato(aModel.getFlagDocumentoRegistrato());
	}

	/**
	 * Valorizza le condizioni di filtro in base al contenuto del model DocumentoAllegatoModel passato.
	 * 
	 * @param aModel
	 */
	public void setCondizione(DocumentoAllegatoModel aModel) {
		String lCondizioni = new String("");
		String lAppoggio = new String("");
		// MEV10-s3: modificata logica di costruzione della query
//		lInserito = false;

		if (aModel != null) {
			if (aModel.getCodTipoDocumento() != null && aModel.getCodTipoDocumento().trim().length() > 0)
				lAppoggio = " COD_TIPO_DOCUMENTO = '" + aModel.getCodTipoDocumento() + "'";
			else {
				lInserito = false;
				lAppoggio = "";
			}
			lCondizioni += setAND(lAppoggio);

			if (aModel.getAnnoFoglioComplementare() != null)
				lAppoggio = " ANNO_FOGLIO_COMPLEMENTARE = " + aModel.getAnnoFoglioComplementare();
			else {
				lInserito = false;
				lAppoggio = "";
			}
			lCondizioni += setAND(lAppoggio);

			if (aModel.getProgrFoglioComplementare() != null)
				lAppoggio = " PROGR_FOGLIO_COMPLEMENTARE = " + aModel.getProgrFoglioComplementare();
			else {
				lInserito = false;
				lAppoggio = "";
			}
			lCondizioni += setAND(lAppoggio);

			if (aModel.getCodUfficioInserimento() != null
					&& aModel.getCodUfficioInserimento().trim().length() > 0)
				lAppoggio = " COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			else {
				lInserito = false;
				lAppoggio = "";
			}
			lCondizioni += setAND(lAppoggio);

			if (aModel.getComuneSedeGiudiziaria() != null
					&& aModel.getComuneSedeGiudiziaria().trim().length() > 0)
				lAppoggio = " COMUNE_SEDE_GIUDIZIARIA = '" + aModel.getComuneSedeGiudiziaria().trim() + "'";
			else {
				lInserito = false;
				lAppoggio = "";
			}
			lCondizioni += setAND(lAppoggio);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Condizione -> " + lCondizioni);

			setCondition(lCondizioni);
		}
	}

	boolean lInserito = false;

	private String setAND(String aCondizioni) {
		if (lInserito)
			aCondizioni = " AND " + aCondizioni;
		lInserito = true;
		return aCondizioni;
	}

	public void setCondizioneUpdate(BigDecimal key) {
		setCondition(" ID_DOCUMENTO_ALLEGATO = " + key);
	}

	public void setCondizioneDelete(BigDecimal aIdEvento, String aCodTipoDocumento) {
		setCondition(" EVE_ID_EVENTO = " + aIdEvento + " AND COD_TIPO_DOCUMENTO = '" + aCodTipoDocumento
				+ "'");
	}

	public void setCondizioneByEve(BigDecimal aIdEvento) {
		setCondition(" EVE_ID_EVENTO = " + aIdEvento);
	}

	public GenericModel getModel() throws DAOException {
		DocumentoAllegatoModel aModel = new DocumentoAllegatoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdDocumentoAllegato(getIdDocumentoAllegato());
		aModel.setDataEmissione(getDataEmissione());
		aModel.setCodTipoDocumento(getCodTipoDocumento());
		aModel.setNumeroProgressivo(getNumeroProgressivo());
		aModel.setFlagDocumentoRegistrato(getFlagDocumentoRegistrato());
		// aModel.setDocBlob(getBlob("DOC_BLOB") );
		aModel.setCodOperatoreInserimento(getCodOperatoreInserimento());
		aModel.setDataInserimento(getDataInserimento());
		aModel.setCodUfficioInserimento(getCodUfficioInserimento());
		aModel.setCodOperatoreAggiornamento(getCodOperatoreAggiornamento());
		aModel.setDataAggiornamento(getDataAggiornamento());
		aModel.setCodUfficioAggiornamento(getCodUfficioAggiornamento());
		aModel.setEveIdEvento(getEveIdEvento());
		aModel.setTemIdTemplate(getTemIdTemplate());
		aModel.setAnnoFoglioComplementare(getAnnoFoglioComplementare());
		aModel.setProgrFoglioComplementare(getProgrFoglioComplementare());
		aModel.setDataTrasmissione(getDataTrasmissione());
		aModel.setDataAnnullamento(getDataAnnullamento());
		aModel.setMotivoAnnullamento(getMotivoAnnullamento());
		aModel.setComuneSedeGiudiziaria(getComuneSedeGiudiziaria());
		aModel.setCodMotivazioneNonInvio(getCodMotivazioneNonInvio());
		aModel.setDescrizioneNonInvio(getDescrizioneNonInvio());
		aModel.setDataUltInvio(getDataUltInvio());
		aModel.setDataInsMan(getDataInsMan());

		return aModel;
	}
}