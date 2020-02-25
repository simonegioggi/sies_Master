package siap.siep.provvedimentopm.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: ProvvedimentoModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il Provvedimento
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
public class ProvvedimentoModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 5775296730890408788L;
	private BigDecimal mIdProvvedimento;
	private String mCodTipo;
	private String mDescrTipo;
	private String mCodMotivo;
	private String mDescrMotivo;
	private Date mData;
	private String mCodEsito;
	private String mDescrEsito;
	private String mFlagPiuMeno;
	private Date mDataTrasmissioneAtti;
	private Date mDataScadenza;
	private BigDecimal mAnnoProtocollo;
	private BigDecimal mProgrProtocollo;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private String mMagCodMagistrato;
	private Date mDataEmissione;
	private String mDestinatario;
	private String mSedeDestinatario;
	private ByteArrayInputStream mDocumentoIn;
	private ByteArrayOutputStream mDocumentoOut;

	// COSTRUTTORE DI DEFAULT
	public ProvvedimentoModel() {
		this.mIdProvvedimento = null;
		this.mCodTipo = "";
		this.mDescrTipo = "";
		this.mCodMotivo = "";
		this.mDescrMotivo = "";
		this.mData = null;
		this.mCodEsito = "";
		this.mDescrEsito = "";
		this.mFlagPiuMeno = "";
		this.mDataTrasmissioneAtti = null;
		this.mDataScadenza = null;
		this.mAnnoProtocollo = null;
		this.mProgrProtocollo = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mMagCodMagistrato = "";
		this.mDataEmissione = null;
		this.mDestinatario = "";
		this.mSedeDestinatario = "";
		this.mDocumentoIn = null;
		this.mDocumentoOut = null;
	}

	// COSTRUTTORE DI COPIA
	public ProvvedimentoModel(ProvvedimentoModel aModel) {
		this.mIdProvvedimento = aModel.mIdProvvedimento;
		this.mCodTipo = aModel.mCodTipo;
		this.mDescrTipo = aModel.mDescrTipo;
		this.mCodMotivo = aModel.mCodMotivo;
		this.mDescrMotivo = aModel.mDescrMotivo;
		this.mData = aModel.mData;
		this.mCodEsito = aModel.mCodEsito;
		this.mDescrEsito = aModel.mDescrEsito;
		this.mFlagPiuMeno = aModel.mFlagPiuMeno;
		this.mDataTrasmissioneAtti = aModel.mDataTrasmissioneAtti;
		this.mDataScadenza = aModel.mDataScadenza;
		this.mAnnoProtocollo = aModel.mAnnoProtocollo;
		this.mProgrProtocollo = aModel.mProgrProtocollo;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mMagCodMagistrato = aModel.mMagCodMagistrato;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mDestinatario = aModel.mDestinatario;
		this.mSedeDestinatario = aModel.mSedeDestinatario;
		this.mDocumentoIn = aModel.mDocumentoIn;
		this.mDocumentoOut = aModel.mDocumentoOut;

	}

	// COSTRUTTORE MODEL
	public ProvvedimentoModel(BigDecimal aIdProvvedimento, String aCodTipo, String aDescrTipo,
			String aCodMotivo, String aDescrMotivo, Date aData, String aCodEsito, String aDescrEsito,
			String aFlagPiuMeno, Date aDataTrasmissioneAtti, Date aDataScadenza, BigDecimal aAnnoProtocollo,
			BigDecimal aProgrProtocollo, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, BigDecimal aFasSieIdFascicoloSiep,
			String aMagCodMagistrato, ByteArrayOutputStream aDocBlob) {
		this.mIdProvvedimento = aIdProvvedimento;
		this.mCodTipo = aCodTipo;
		this.mDescrTipo = aDescrTipo;
		this.mCodMotivo = aCodMotivo;
		this.mDescrMotivo = aDescrMotivo;
		this.mData = aData;
		this.mCodEsito = aCodEsito;
		this.mDescrEsito = aDescrEsito;
		this.mFlagPiuMeno = aFlagPiuMeno;
		this.mDataTrasmissioneAtti = aDataTrasmissioneAtti;
		this.mDataScadenza = aDataScadenza;
		this.mAnnoProtocollo = aAnnoProtocollo;
		this.mProgrProtocollo = aProgrProtocollo;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mMagCodMagistrato = aMagCodMagistrato;
		// this.mDataEmissione = aDataEmissione;
		// this.mDestinatario = aDestinatario;
		// this.mSedeDestinatario = aSedeDestinatario;
		this.mDocumentoOut = aDocBlob;
	}

	public ProvvedimentoModel(BigDecimal aIdProvvedimento, String aCodTipo, String aCodMotivo, Date aData,
			String aCodEsito, String aFlagPiuMeno, Date aDataTrasmissioneAtti, Date aDataScadenza,
			BigDecimal aAnnoProtocollo, BigDecimal aProgrProtocollo, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, String aMagCodMagistrato, Date aDataEmissione,
			String aDestinatario, String aSedeDestinatario) {
		this.mIdProvvedimento = aIdProvvedimento;
		this.mCodTipo = aCodTipo;
		this.mCodMotivo = aCodMotivo;
		this.mData = aData;
		this.mCodEsito = aCodEsito;
		this.mFlagPiuMeno = aFlagPiuMeno;
		this.mDataTrasmissioneAtti = aDataTrasmissioneAtti;
		this.mDataScadenza = aDataScadenza;
		this.mAnnoProtocollo = aAnnoProtocollo;
		this.mProgrProtocollo = aProgrProtocollo;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mMagCodMagistrato = aMagCodMagistrato;
		this.mDataEmissione = aDataEmissione;
		this.mDestinatario = aDestinatario;
		this.mSedeDestinatario = aSedeDestinatario;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdProvvedimento() {
		return mIdProvvedimento;
	}

	public String getCodTipo() {
		return mCodTipo;
	}

	public String getDescrTipo() {
		return mDescrTipo;
	}

	public String getCodMotivo() {
		return mCodMotivo;
	}

	public String getDescrMotivo() {
		return mDescrMotivo;
	}

	public Date getData() {
		return mData;
	}

	public String getCodEsito() {
		return mCodEsito;
	}

	public String getDescrEsito() {
		return mDescrEsito;
	}

	public String getFlagPiuMeno() {
		return mFlagPiuMeno;
	}

	public Date getDataTrasmissioneAtti() {
		return mDataTrasmissioneAtti;
	}

	public Date getDataScadenza() {
		return mDataScadenza;
	}

	public BigDecimal getAnnoProtocollo() {
		return mAnnoProtocollo;
	}

	public BigDecimal getProgrProtocollo() {
		return mProgrProtocollo;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getMagCodMagistrato() {
		return mMagCodMagistrato;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public String getDestinatario() {
		return mDestinatario;
	}

	public String getSedeDestinatario() {
		return mSedeDestinatario;
	}

	public ByteArrayInputStream getDocumentoIn() {
		return mDocumentoIn;
	}

	public ByteArrayOutputStream getDocumentoOut() {
		return mDocumentoOut;
	}

	//
	// METODI SET()
	//

	public void setIdProvvedimento(BigDecimal aValore) {
		mIdProvvedimento = aValore;
	}

	public void setCodTipo(String aValore) {
		mCodTipo = aValore;
	}

	public void setDescrTipo(String aValore) {
		mDescrTipo = aValore;
	}

	public void setCodMotivo(String aValore) {
		mCodMotivo = aValore;
	}

	public void setDescrMotivo(String aValore) {
		mDescrMotivo = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setCodEsito(String aValore) {
		mCodEsito = aValore;
	}

	public void setDescrEsito(String aValore) {
		mDescrEsito = aValore;
	}

	public void setFlagPiuMeno(String aValore) {
		mFlagPiuMeno = aValore;
	}

	public void setDataTrasmissioneAtti(Date aValore) {
		mDataTrasmissioneAtti = aValore;
	}

	public void setDataScadenza(Date aValore) {
		mDataScadenza = aValore;
	}

	public void setAnnoProtocollo(BigDecimal aValore) {
		mAnnoProtocollo = aValore;
	}

	public void setProgrProtocollo(BigDecimal aValore) {
		mProgrProtocollo = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setMagCodMagistrato(String aValore) {
		mMagCodMagistrato = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setDestinatario(String aValore) {
		mDestinatario = aValore;
	}

	public void setSedeDestinatario(String aValore) {
		mSedeDestinatario = aValore;
	}

	public void setDocumentoIn(ByteArrayInputStream aValore) {
		mDocumentoIn = aValore;
	}

	public void setDocumentoOut(ByteArrayOutputStream aValore) {
		mDocumentoOut = aValore;
	}

}