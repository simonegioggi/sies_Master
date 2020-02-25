package siap.siep.fascicolo.model;

/**
* <p>Title: FascicoloSiepModel</p>
* <p>Description: Classe Model che rappresenta il FascicoloSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class FascicoloCheckModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -724120252051473652L;

	private BigDecimal mChiaveAnno;
	private BigDecimal mNumFascicoli;

	// COSTRUTTORE DI DEFAULT
	public FascicoloCheckModel() {
		this.mChiaveAnno = null;
		this.mNumFascicoli = null;
	}

	// COSTRUTTORE DI COPIA
	public FascicoloCheckModel(FascicoloCheckModel aModel) {
		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mNumFascicoli = aModel.mNumFascicoli;
	}

	// COSTRUTTORE MODEL
	public FascicoloCheckModel(BigDecimal aChiaveAnno, BigDecimal aNumFascicoli) {
		this.mChiaveAnno = aChiaveAnno;
		this.mNumFascicoli = aNumFascicoli;
	}

	//
	// METODI GET()
	//
	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getNumFascicoli() {
		return mNumFascicoli;
	}

	//
	// METODI SET()
	//
	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setNumFascicoli(BigDecimal aValore) {
		mNumFascicoli = aValore;
	}

	public String toString() {
		String lToString = "[ " + this.mChiaveAnno + " - " + this.mNumFascicoli + "]";
		return lToString;
	}

}