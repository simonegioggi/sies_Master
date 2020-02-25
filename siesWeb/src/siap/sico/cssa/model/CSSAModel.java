package siap.sico.cssa.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class CSSAModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2233248916763362209L;

	private BigDecimal mIdCSSA;
	private String mTipo;
	private String mComune;
	private String mIndirizzo;
	private String mEMail;
	private String mFax;
	private String mTel;
	private String mIncarico;
	private String mTitolo;
	private String mNome;
	private String mCognome;
	private Date mDataCaricamento;

	private String mCodComune;

	// COSTRUTTORE DI DEFAULT
	public CSSAModel() {

		this.mIdCSSA = null;
		this.mTipo = "";
		this.mComune = "";
		this.mIndirizzo = "";
		this.mEMail = "";
		this.mFax = "";
		this.mTel = "";
		this.mIncarico = "";
		this.mTitolo = "";
		this.mNome = "";
		this.mCognome = "";
		this.mDataCaricamento = null;

		this.mCodComune = "";
	}

	// COSTRUTTORE DI COPIA
	public CSSAModel(CSSAModel aModel) {

		this.mIdCSSA = aModel.mIdCSSA;
		this.mTipo = aModel.mTipo;
		this.mComune = aModel.mComune;
		this.mIndirizzo = aModel.mIndirizzo;
		this.mEMail = aModel.mEMail;
		this.mFax = aModel.mFax;
		this.mTel = aModel.mTel;
		this.mIncarico = aModel.mIncarico;
		this.mTitolo = aModel.mTitolo;
		this.mNome = aModel.mNome;
		this.mCognome = aModel.mCognome;
		this.mDataCaricamento = aModel.mDataCaricamento;

		this.mCodComune = aModel.mCodComune;
	}

	// COSTRUTTORE MODEL
	public CSSAModel(BigDecimal aIdCSSA, String aTipo, String aComune, String aIndirizzo, String aEMail,
			String aFax, String aTel, String aIncarico, String aTitolo, String aNome, String aCognome,
			Date aDataCaricamento, String aCodComune) {

		this.mIdCSSA = aIdCSSA;
		this.mTipo = aTipo;
		this.mComune = aComune;
		this.mIndirizzo = aIndirizzo;
		this.mEMail = aEMail;
		this.mFax = aFax;
		this.mTel = aTel;
		this.mIncarico = aIncarico;
		this.mTitolo = aTitolo;
		this.mNome = aNome;
		this.mCognome = aCognome;
		this.mDataCaricamento = aDataCaricamento;

		this.mCodComune = aCodComune;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdCSSA() {
		return mIdCSSA;
	}

	public String getTipo() {
		return mTipo;
	}

	public String getComune() {
		return mComune;
	}

	public String getIndirizzo() {
		return mIndirizzo;
	}

	public String getEMail() {
		return mEMail;
	}

	public String getFax() {
		return mFax;
	}

	public String getTel() {
		return mTel;
	}

	public String getIncarico() {
		return mIncarico;
	}

	public String getTitolo() {
		return mTitolo;
	}

	public String getNome() {
		return mNome;
	}

	public String getCognome() {
		return mCognome;
	}

	public Date getDataCaricamento() {
		return mDataCaricamento;
	}

	public String getCodComune() {
		return mCodComune;
	}

	//
	// METODI SET()
	//
	public void setIdCSSA(BigDecimal aValore) {
		mIdCSSA = aValore;
	}

	public void setTipo(String aValore) {
		mTipo = aValore;
	}

	public void setComune(String aValore) {
		mComune = aValore;
	}

	public void setIndirizzo(String aValore) {
		mIndirizzo = aValore;
	}

	public void setEMail(String aValore) {
		mEMail = aValore;
	}

	public void setFax(String aValore) {
		mFax = aValore;
	}

	public void setTel(String aValore) {
		mTel = aValore;
	}

	public void setIncarico(String aValore) {
		mIncarico = aValore;
	}

	public void setTitolo(String aValore) {
		mTitolo = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setDataCaricamento(Date aValore) {
		mDataCaricamento = aValore;
	}

	public void setCodComune(String aValore) {
		mCodComune = aValore;
	}

	public String getTipoDesc() {
		String ret = "";
		if ("UEPE".equals(mTipo) || "UEPESS".equals(mTipo)) {
			ret = "UEPE";
		} else if ("USSM".equals(mTipo) || "USSMSS".equals(mTipo)) {
			ret = "USSM";
		}
		return ret;
	}

	public String getTipoDescXStampe() {
		String ret = "";
		if ("UEPE".equals(mTipo) || "UEPESS".equals(mTipo)) {
			ret = "UFFICIO DI ESECUZIONE PENALE ESTERNA";
		} else if ("USSM".equals(mTipo) || "USSMSS".equals(mTipo)) {
			ret = "UFFICIO SERVIZI SOCIALI PER I MINORENNI";
		}
		return ret;
	}

	//
	// METODO toString()
	//
	public String toString() {
		String lToString = this.mIdCSSA + " - " + this.mTipo + " - " + this.mComune + " - " + this.mIndirizzo
				+ " - " + this.mEMail + " - " + this.mFax + " - " + this.mTel + " - " + this.mIncarico + " - "
				+ this.mTitolo + " - " + this.mNome + " - " + this.mCognome + " - " + this.mDataCaricamento
				+ " - " + this.mCodComune;

		return lToString;
	}

}