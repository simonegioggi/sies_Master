package siap.siep.provvedimentopm.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.provvedimentopm.model.ProvvedimentoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ProvvedimentoDAO
 * </p>
 * <p>
 * Description: Classe DAO che rrapèpresenta la tabella Provvedimento
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
public class ProvvedimentoDAO extends SIAPTableDAO {

	public ProvvedimentoDAO(Connection con) {
		super(con);
		setTable("PROVVEDIMENTO");

		setSequenceField("ID_PROVVEDIMENTO", "PRV_SEQ");
		setFieldKey("ID_PROVVEDIMENTO", BIG_DECIMAL);
		setField("ID_PROVVEDIMENTO", BIG_DECIMAL);
		setField("COD_TIPO", STRING);
		setField("COD_MOTIVO", STRING);
		setField("DATA", DATE);
		setField("COD_ESITO", STRING);
		setField("FLAG_PIU_MENO", STRING);
		setField("DATA_TRASMISSIONE_ATTI", DATE);
		setField("DATA_SCADENZA", DATE);
		setField("ANNO_PROTOCOLLO", BIG_DECIMAL);
		setField("PROGR_PROTOCOLLO", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("MAG_COD_MAGISTRATO", STRING);
		// setField("DOC_BLOB", BINARY_STREAM);
		setField("DOC_BLOB", TBLOB);
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdProvvedimento() throws DAOException {
		return getBigDecimal("ID_PROVVEDIMENTO");
	}

	public String getCodTipo() throws DAOException {
		return getString("COD_TIPO");
	}

	public String getCodMotivo() throws DAOException {
		return getString("COD_MOTIVO");
	}

	public Date getData() throws DAOException {
		return getDate("DATA");
	}

	public String getCodEsito() throws DAOException {
		return getString("COD_ESITO");
	}

	public String getFlagPiuMeno() throws DAOException {
		return getString("FLAG_PIU_MENO");
	}

	public Date getDataTrasmissioneAtti() throws DAOException {
		return getDate("DATA_TRASMISSIONE_ATTI");
	}

	public Date getDataScadenza() throws DAOException {
		return getDate("DATA_SCADENZA");
	}

	public BigDecimal getAnnoProtocollo() throws DAOException {
		return getBigDecimal("ANNO_PROTOCOLLO");
	}

	public BigDecimal getProgrProtocollo() throws DAOException {
		return getBigDecimal("PROGR_PROTOCOLLO");
	}

	public String getCodOperatoreInserimento() throws DAOException {
		return getString("COD_OPERATORE_INSERIMENTO");
	}

	public Date getDataInserimento() throws DAOException {
		return getDate("DATA_INSERIMENTO");
	}

	public String getCodOperatoreAggiornamento() throws DAOException {
		return getString("COD_OPERATORE_AGGIORNAMENTO");
	}

	public Date getDataAggiornamento() throws DAOException {
		return getDate("DATA_AGGIORNAMENTO");
	}

	public BigDecimal getFasSieIdFascicoloSiep() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
	}

	public String getMagCodMagistrato() throws DAOException {
		return getString("MAG_COD_MAGISTRATO");
	}

	public ByteArrayOutputStream getDocBlob() throws DAOException {
		return getBlob("DOC_BLOB");
	}

	//
	// METODI SET()
	//

	public void setIdProvvedimento(BigDecimal aValore) {
		setBigDecimal("ID_PROVVEDIMENTO", aValore);
	}

	public void setCodTipo(String aValore) {
		setString("COD_TIPO", aValore);
	}

	public void setCodMotivo(String aValore) {
		setString("COD_MOTIVO", aValore);
	}

	public void setData(Date aValore) {
		setDate("DATA", aValore);
	}

	public void setCodEsito(String aValore) {
		setString("COD_ESITO", aValore);
	}

	public void setFlagPiuMeno(String aValore) {
		setString("FLAG_PIU_MENO", aValore);
	}

	public void setDataTrasmissioneAtti(Date aValore) {
		setDate("DATA_TRASMISSIONE_ATTI", aValore);
	}

	public void setDataScadenza(Date aValore) {
		setDate("DATA_SCADENZA", aValore);
	}

	public void setAnnoProtocollo(BigDecimal aValore) {
		setBigDecimal("ANNO_PROTOCOLLO", aValore);
	}

	public void setProgrProtocollo(BigDecimal aValore) {
		setBigDecimal("PROGR_PROTOCOLLO", aValore);
	}

	public void setCodOperatoreInserimento(String aValore) {
		setString("COD_OPERATORE_INSERIMENTO", aValore);
	}

	public void setDataInserimento(Date aValore) {
		setDate("DATA_INSERIMENTO", aValore);
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		setString("COD_OPERATORE_AGGIORNAMENTO", aValore);
	}

	public void setDataAggiornamento(Date aValore) {
		setDate("DATA_AGGIORNAMENTO", aValore);
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
	}

	public void setMagCodMagistrato(String aValore) {
		setString("MAG_COD_MAGISTRATO", aValore);
	}

	// ---GDV public void setDocumento( InputStream aValore ) { setBinaryStream("DOC_BLOB", aValore ); }
	// public void setDocumento( InputStream aValore ) { setBlob("DOC_BLOB"); }

	public void setDocBlob(ByteArrayInputStream aValore) {
		// try{
		// Inizializzo il locator del BLOB
		/*
		 * initBlob(lKey);
		 * 
		 * this.setIdProvvedimento(lKey); this.selByKey(); this.setForUpdate(); this.start();
		 * 
		 * BLOB lBlob = null;
		 * 
		 * while(this.next()) { lBlob = ((OracleResultSet)super.mRs).getBLOB("DOC_BLOB"); }
		 * 
		 * OutputStream lStream = lBlob.getBinaryOutputStream(); int lSize = lBlob.getBufferSize();
		 * 
		 * 
		 * byte[] lBuffer = new byte[lSize]; int lLength = -1;
		 * 
		 * while( (lLength = aValore.read(lBuffer)) != -1) lStream.write(lBuffer);
		 * 
		 * lStream.flush(); lStream.close();
		 */

		setBlob("DOC_BLOB", aValore);

		/*
		 * } catch (SQLException lSqe) { lSqe.printStackTrace(); throw new DAOException(lSqe); } catch
		 * (IOException lIoe) { lIoe.printStackTrace(); throw new DAOException(lIoe.getMessage()); }
		 */

	}

//	private void initBlob(BigDecimal lKey) throws DAOException {
//		// Inizializzo il locator del BLOB
//		String lSql = "UPDATE PROVVEDIMENTO set DOC_BLOB = empty_blob() " + "where ID_PROVVEDIMENTO = "
//				+ lKey;
//
//		update(lSql);
//	}

	public GenericModel getModel() throws DAOException {
		return new ProvvedimentoModel(getIdProvvedimento(), getCodTipo(), "", getCodMotivo(), "", getData(),
				getCodEsito(), "", getFlagPiuMeno(), getDataTrasmissioneAtti(), getDataScadenza(),
				getAnnoProtocollo(), getProgrProtocollo(), getCodOperatoreInserimento(),
				getDataInserimento(), getCodOperatoreAggiornamento(), getDataAggiornamento(),
				getFasSieIdFascicoloSiep(), getMagCodMagistrato(), getDocBlob());
	}

	public void setDAOFromModel(ProvvedimentoModel aModel) throws DAOException {
		setIdProvvedimento(aModel.getIdProvvedimento());
		setCodTipo(aModel.getCodTipo());
		setCodMotivo(aModel.getCodMotivo());
		setData(aModel.getData());
		setCodEsito(aModel.getCodEsito());
		setFlagPiuMeno(aModel.getFlagPiuMeno());
		setDataTrasmissioneAtti(aModel.getDataTrasmissioneAtti());
		setDataScadenza(aModel.getDataScadenza());
		setAnnoProtocollo(aModel.getAnnoProtocollo());
		setProgrProtocollo(aModel.getProgrProtocollo());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setMagCodMagistrato(aModel.getMagCodMagistrato());
		setDocBlob(aModel.getDocumentoIn());
	}

	public void selCondizione(ProvvedimentoModel aModel) {
		String lCondizioni = new String();
		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void selCondizioneUpdate(BigDecimal key) {
	}

}