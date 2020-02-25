package siap.sius.impugnazione.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: ImpugnazioneModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il Impugnazione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ImpugnazioneModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1197238671965701341L;

	private BigDecimal mIdImpugnazione;
	private BigDecimal mAnnoS7;
	private BigDecimal mProgrS7;
	private String mCodTipoImpugnazione;
	private String mDescrTipoImpugnazione;
	private String mSoggettoImpugnante;
	private String mDescrSoggettoImpugnante;
	private Date mDataRicorso;
	private Date mDataAnnotazione;
	private String mAnnotazione;
	private Date mDataArrivoCancelleria;
	private Date mDataTrasmissioneAtti;
	private String mCodAutoritaDestinataria;
	private String mDescrAutoritaDestinataria;
	private Date mDataDecisione;
	private String mCodTenoreDecisione;
	private String mDescrTenoreDecisione;
	private Date mDataRestituzioneAtti;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mDepOpidDepositoOrdinanzaPc;
	private BigDecimal mDepDecIdDepositoDecreto;
	// Nuovi campi 19-04-2005
	private String mFlagAnnullamento;
	private Date mDataAnnullamento;
	private String mMotivoAnnullamento;
	private String mFlagSospEsec; // Nuovo campo 30/04/2007

	private BigDecimal mDepIdDepositoSentenza;

	private String mDescrizioneAltro; // Nuovo campo MEV_50 16/11/2016

	// COSTRUTTORE DI DEFAULT
	public ImpugnazioneModel() {
		this.mIdImpugnazione = null;
		this.mAnnoS7 = null;
		this.mProgrS7 = null;
		this.mCodTipoImpugnazione = "";
		this.mDescrTipoImpugnazione = "";
		this.mSoggettoImpugnante = "";
		this.mDescrSoggettoImpugnante = "";
		this.mDataRicorso = null;
		this.mDataAnnotazione = null;
		this.mAnnotazione = "";
		this.mDataArrivoCancelleria = null;
		this.mDataTrasmissioneAtti = null;
		this.mCodAutoritaDestinataria = "";
		this.mDescrAutoritaDestinataria = "";
		this.mDataDecisione = null;
		this.mCodTenoreDecisione = "";
		this.mDescrTenoreDecisione = "";
		this.mDataRestituzioneAtti = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mDepOpidDepositoOrdinanzaPc = null;
		this.mDepDecIdDepositoDecreto = null;
		this.mFlagAnnullamento = "";
		this.mDataAnnullamento = null;
		this.mMotivoAnnullamento = "";
		this.mFlagSospEsec = "";
		this.mDepIdDepositoSentenza = null;
		this.mDescrizioneAltro = "";
	}

	// COSTRUTTORE DI COPIA
	public ImpugnazioneModel(ImpugnazioneModel aModel) {
		this.mIdImpugnazione = aModel.mIdImpugnazione;
		this.mAnnoS7 = aModel.mAnnoS7;
		this.mProgrS7 = aModel.mProgrS7;
		this.mCodTipoImpugnazione = aModel.mCodTipoImpugnazione;
		this.mDescrTipoImpugnazione = aModel.mDescrTipoImpugnazione;
		this.mSoggettoImpugnante = aModel.mSoggettoImpugnante;
		this.mDescrSoggettoImpugnante = aModel.mDescrSoggettoImpugnante;
		this.mDataRicorso = aModel.mDataRicorso;
		this.mDataAnnotazione = aModel.mDataAnnotazione;
		this.mAnnotazione = aModel.mAnnotazione;
		this.mDataArrivoCancelleria = aModel.mDataArrivoCancelleria;
		this.mDataTrasmissioneAtti = aModel.mDataTrasmissioneAtti;
		this.mCodAutoritaDestinataria = aModel.mCodAutoritaDestinataria;
		this.mDescrAutoritaDestinataria = aModel.mDescrAutoritaDestinataria;
		this.mDataDecisione = aModel.mDataDecisione;
		this.mCodTenoreDecisione = aModel.mCodTenoreDecisione;
		this.mDescrTenoreDecisione = aModel.mDescrTenoreDecisione;
		this.mDataRestituzioneAtti = aModel.mDataRestituzioneAtti;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mDepOpidDepositoOrdinanzaPc = aModel.mDepOpidDepositoOrdinanzaPc;
		this.mDepDecIdDepositoDecreto = aModel.mDepDecIdDepositoDecreto;
		this.mFlagAnnullamento = aModel.mFlagAnnullamento;
		this.mDataAnnullamento = aModel.mDataAnnullamento;
		this.mMotivoAnnullamento = aModel.mMotivoAnnullamento;
		this.mFlagSospEsec = aModel.mFlagSospEsec; // 30/04/2007
		this.mDepIdDepositoSentenza = aModel.mDepIdDepositoSentenza;
		this.mDescrizioneAltro = aModel.mDescrizioneAltro;
	}

	// COSTRUTTORE MODEL
	public ImpugnazioneModel(BigDecimal aIdImpugnazione, BigDecimal aAnnoS7, BigDecimal aProgrS7,
			String aCodTipoImpugnazione, String aDescrTipoImpugnazione, String aSoggettoImpugnante,
			String aDescrSoggettoImpugnante, Date aDataRicorso, Date aDataAnnotazione, String aAnnotazione,
			Date aDataArrivoCancelleria, Date aDataTrasmissioneAtti, String aCodAutoritaDestinataria,
			String aDescrAutoritaDestinataria, Date aDataDecisione, String aCodTenoreDecisione,
			String aDescrTenoreDecisione, Date aDataRestituzioneAtti, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento, BigDecimal aDepOpidDepositoOrdinanzaPc,
			BigDecimal aDepDecIdDepositoDecreto, String aFlagAnnullamento, Date aDataAnnullamento,
			String aMotivoAnnullamento, String aFlagSospEsec, BigDecimal aDepIdDepositoSentenza,
			String aDescrizioneAltro) {
		this.mIdImpugnazione = aIdImpugnazione;
		this.mAnnoS7 = aAnnoS7;
		this.mProgrS7 = aProgrS7;
		this.mCodTipoImpugnazione = aCodTipoImpugnazione;
		this.mDescrTipoImpugnazione = aDescrTipoImpugnazione;
		this.mSoggettoImpugnante = aSoggettoImpugnante;
		this.mDescrSoggettoImpugnante = aDescrSoggettoImpugnante;
		this.mDataRicorso = aDataRicorso;
		this.mDataAnnotazione = aDataAnnotazione;
		this.mAnnotazione = aAnnotazione;
		this.mDataArrivoCancelleria = aDataArrivoCancelleria;
		this.mDataTrasmissioneAtti = aDataTrasmissioneAtti;
		this.mCodAutoritaDestinataria = aCodAutoritaDestinataria;
		this.mDescrAutoritaDestinataria = aDescrAutoritaDestinataria;
		this.mDataDecisione = aDataDecisione;
		this.mCodTenoreDecisione = aCodTenoreDecisione;
		this.mDescrTenoreDecisione = aDescrTenoreDecisione;
		this.mDataRestituzioneAtti = aDataRestituzioneAtti;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mDepOpidDepositoOrdinanzaPc = aDepOpidDepositoOrdinanzaPc;
		this.mDepDecIdDepositoDecreto = aDepDecIdDepositoDecreto;
		this.mFlagAnnullamento = aFlagAnnullamento;
		this.mDataAnnullamento = aDataAnnullamento;
		this.mMotivoAnnullamento = aMotivoAnnullamento;
		this.mFlagSospEsec = aFlagSospEsec; // 30/04/2007
		this.mDepIdDepositoSentenza = aDepIdDepositoSentenza;
		this.mDescrizioneAltro = aDescrizioneAltro;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdImpugnazione() {
		return mIdImpugnazione;
	}

	public BigDecimal getAnnoS7() {
		return mAnnoS7;
	}

	public BigDecimal getProgrS7() {
		return mProgrS7;
	}

	public String getCodTipoImpugnazione() {
		return mCodTipoImpugnazione;
	}

	public String getDescrTipoImpugnazione() {
		return mDescrTipoImpugnazione;
	}

	public String getSoggettoImpugnante() {
		return mSoggettoImpugnante;
	}

	public String getDescrSoggettoImpugnante() {
		return mDescrSoggettoImpugnante;
	}

	public Date getDataRicorso() {
		return mDataRicorso;
	}

	public Date getDataAnnotazione() {
		return mDataAnnotazione;
	}

	public String getAnnotazione() {
		return mAnnotazione;
	}

	public Date getDataArrivoCancelleria() {
		return mDataArrivoCancelleria;
	}

	public Date getDataTrasmissioneAtti() {
		return mDataTrasmissioneAtti;
	}

	public String getCodAutoritaDestinataria() {
		return mCodAutoritaDestinataria;
	}

	public String getDescrAutoritaDestinataria() {
		return mDescrAutoritaDestinataria;
	}

	public Date getDataDecisione() {
		return mDataDecisione;
	}

	public String getCodTenoreDecisione() {
		return mCodTenoreDecisione;
	}

	public String getDescrTenoreDecisione() {
		return mDescrTenoreDecisione;
	}

	public Date getDataRestituzioneAtti() {
		return mDataRestituzioneAtti;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public BigDecimal getDepOpidDepositoOrdinanzaPc() {
		return mDepOpidDepositoOrdinanzaPc;
	}

	public BigDecimal getDepDecIdDepositoDecreto() {
		return mDepDecIdDepositoDecreto;
	}

	public String getFlagAnnullamento() {
		return this.mFlagAnnullamento;
	}

	public Date getDataAnnullamento() {
		return this.mDataAnnullamento;
	}

	public String getMotivoAnnullamento() {
		return this.mMotivoAnnullamento;
	}

	public String getFlagSospEsec() {
		return this.mFlagSospEsec;
	} // 30/04/2007

	public BigDecimal getDepIdDepositoSentenza() {
		return mDepIdDepositoSentenza;
	}

	public String getDescrizioneAltro() {
		return this.mDescrizioneAltro;
	}

	//
	// METODI SET()
	//
	public void setIdImpugnazione(BigDecimal aValore) {
		mIdImpugnazione = aValore;
	}

	public void setAnnoS7(BigDecimal aValore) {
		mAnnoS7 = aValore;
	}

	public void setProgrS7(BigDecimal aValore) {
		mProgrS7 = aValore;
	}

	public void setCodTipoImpugnazione(String aValore) {
		mCodTipoImpugnazione = aValore;
	}

	public void setDescrTipoImpugnazione(String aValore) {
		mDescrTipoImpugnazione = aValore;
	}

	public void setSoggettoImpugnante(String aValore) {
		mSoggettoImpugnante = aValore;
	}

	public void setDescrSoggettoImpugnante(String aValore) {
		mDescrSoggettoImpugnante = aValore;
	}

	public void setDataRicorso(Date aValore) {
		mDataRicorso = aValore;
	}

	public void setDataAnnotazione(Date aValore) {
		mDataAnnotazione = aValore;
	}

	public void setAnnotazione(String aValore) {
		mAnnotazione = aValore;
	}

	public void setDataArrivoCancelleria(Date aValore) {
		mDataArrivoCancelleria = aValore;
	}

	public void setDataTrasmissioneAtti(Date aValore) {
		mDataTrasmissioneAtti = aValore;
	}

	public void setCodAutoritaDestinataria(String aValore) {
		mCodAutoritaDestinataria = aValore;
	}

	public void setDescrAutoritaDestinataria(String aValore) {
		mDescrAutoritaDestinataria = aValore;
	}

	public void setDataDecisione(Date aValore) {
		mDataDecisione = aValore;
	}

	public void setCodTenoreDecisione(String aValore) {
		mCodTenoreDecisione = aValore;
	}

	public void setDescrTenoreDecisione(String aValore) {
		mDescrTenoreDecisione = aValore;
	}

	public void setDataRestituzioneAtti(Date aValore) {
		mDataRestituzioneAtti = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setDepOpidDepositoOrdinanzaPc(BigDecimal aValore) {
		mDepOpidDepositoOrdinanzaPc = aValore;
	}

	public void setDepDecIdDepositoDecreto(BigDecimal aValore) {
		mDepDecIdDepositoDecreto = aValore;
	}

	public void setFlagAnnullamento(String aValore) {
		mFlagAnnullamento = aValore;
	}

	public void setDataAnnullamento(Date aValore) {
		mDataAnnullamento = aValore;
	}

	public void setMotivoAnnullamento(String aValore) {
		this.mMotivoAnnullamento = aValore;
	}

	public void setFlagSospEsec(String aValore) {
		this.mFlagSospEsec = aValore;
	} // 30/04/2007

	public void setDepIdDepositoSentenza(BigDecimal aValore) {
		mDepIdDepositoSentenza = aValore;
	}

	public void setDescrizioneAltro(String aValore) {
		this.mDescrizioneAltro = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdImpugnazione + " - " + mAnnoS7 + " - " + mProgrS7 + " - " + mCodTipoImpugnazione
				+ " - " + mDescrTipoImpugnazione + " - " + mSoggettoImpugnante + " - "
				+ mDescrSoggettoImpugnante + " - " + mDataRicorso + " - " + mDataAnnotazione + " - "
				+ mAnnotazione + " - " + mDataArrivoCancelleria + " - " + mDataTrasmissioneAtti + " - "
				+ mCodAutoritaDestinataria + " - " + mDescrAutoritaDestinataria + " - " + mDataDecisione
				+ " - " + mCodTenoreDecisione + " - " + mDescrTenoreDecisione + " - " + mDataRestituzioneAtti
				+ " - " + mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento
				+ " - " + mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - "
				+ mDataAggiornamento + " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento
				+ " - " + mDepOpidDepositoOrdinanzaPc + " - " + mDepDecIdDepositoDecreto + " - "
				+ mFlagAnnullamento + " - " + mDataAnnullamento + " - " + mMotivoAnnullamento + " - "
				+ mFlagSospEsec + " - " + mDepIdDepositoSentenza + " - " + mDescrizioneAltro;
		return lStr;
	}

}