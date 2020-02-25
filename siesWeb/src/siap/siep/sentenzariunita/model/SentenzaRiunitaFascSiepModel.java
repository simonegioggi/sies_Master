package siap.siep.sentenzariunita.model;

/**
* <p>Title: SentenzariunitaFascSiepModel</p>
* <p>Description: Classe Model che rappresenta il SentenzariunitaFascSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class SentenzaRiunitaFascSiepModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -1776171782603381289L;

	private BigDecimal mIdSentenzariunitaFascSiep;
	private BigDecimal mSenRiuIdSentenzaRiunita;
	private BigDecimal mFasSieIdFascicoloSiep;
	private SentenzaRiunitaModel mSentenzaRiunitaModel;

	// COSTRUTTORE DI DEFAULT
	public SentenzaRiunitaFascSiepModel() {
		this.mIdSentenzariunitaFascSiep = null;
		this.mSenRiuIdSentenzaRiunita = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mSentenzaRiunitaModel = null;
	}

	// COSTRUTTORE DI COPIA
	public SentenzaRiunitaFascSiepModel(SentenzaRiunitaFascSiepModel aModel) {
		this.mIdSentenzariunitaFascSiep = aModel.mIdSentenzariunitaFascSiep;
		this.mSenRiuIdSentenzaRiunita = aModel.mSenRiuIdSentenzaRiunita;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mSentenzaRiunitaModel = aModel.mSentenzaRiunitaModel;
	}

	// COSTRUTTORE MODEL
	public SentenzaRiunitaFascSiepModel(BigDecimal aIdSentenzariunitaFascSiep,
			BigDecimal aSenRiuIdSentenzaRiunita, BigDecimal aFasSieIdFascicoloSiep,
			SentenzaRiunitaModel aSentenzaRiunitaModel) {
		this.mIdSentenzariunitaFascSiep = aIdSentenzariunitaFascSiep;
		this.mSenRiuIdSentenzaRiunita = aSenRiuIdSentenzaRiunita;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mSentenzaRiunitaModel = aSentenzaRiunitaModel;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdSentenzaRiunitaFascSiep() {
		return mIdSentenzariunitaFascSiep;
	}

	public BigDecimal getSenRiuIdSentenzaRiunita() {
		return mSenRiuIdSentenzaRiunita;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public SentenzaRiunitaModel getSentenzaRiunitaModel() {
		return mSentenzaRiunitaModel;
	}

	//
	// METODI SET()
	//

	public void setIdSentenzariunitaFascSiep(BigDecimal aValore) {
		mIdSentenzariunitaFascSiep = aValore;
	}

	public void setSenRiuIdSentenzaRiunita(BigDecimal aValore) {
		mSenRiuIdSentenzaRiunita = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setSentenzaRiunitaModel(SentenzaRiunitaModel aValore) {
		mSentenzaRiunitaModel = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdSentenzariunitaFascSiep + " - " + mSenRiuIdSentenzaRiunita + " - "
				+ mFasSieIdFascicoloSiep;

		return lStr;
	}

}
