package siap.siep.penasospesa.model;

/**
* <p>Title: AnnmanPenacomplModel</p>
* <p>Description: Classe Model che rappresenta il AnnmanPenacompl</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class AnnmanPenacomplModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = -6046590533721477499L;
	private BigDecimal mIdAnnmanPenacompl;
	private BigDecimal mAnnotazionemanualeId;
	private BigDecimal mPenacomplessivaId;

	// COSTRUTTORE DI DEFAULT
	public AnnmanPenacomplModel() {
		this.mIdAnnmanPenacompl = null;
		this.mAnnotazionemanualeId = null;
		this.mPenacomplessivaId = null;
	}

	// COSTRUTTORE DI COPIA
	public AnnmanPenacomplModel(AnnmanPenacomplModel aModel) {
		this.mIdAnnmanPenacompl = aModel.mIdAnnmanPenacompl;
		this.mAnnotazionemanualeId = aModel.mAnnotazionemanualeId;
		this.mPenacomplessivaId = aModel.mPenacomplessivaId;
	}

	// COSTRUTTORE MODEL
	public AnnmanPenacomplModel(BigDecimal aIdAnnmanPenacompl, BigDecimal aAnnotazionemanualeId,
			BigDecimal aPenacomplessivaId) {
		this.mIdAnnmanPenacompl = aIdAnnmanPenacompl;
		this.mAnnotazionemanualeId = aAnnotazionemanualeId;
		this.mPenacomplessivaId = aPenacomplessivaId;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAnnmanPenacompl() {
		return mIdAnnmanPenacompl;
	}

	public BigDecimal getAnnotazionemanualeId() {
		return mAnnotazionemanualeId;
	}

	public BigDecimal getPenacomplessivaId() {
		return mPenacomplessivaId;
	}

	//
	// METODI SET()
	//

	public void setIdAnnmanPenacompl(BigDecimal aValore) {
		mIdAnnmanPenacompl = aValore;
	}

	public void setAnnotazionemanualeId(BigDecimal aValore) {
		mAnnotazionemanualeId = aValore;
	}

	public void setPenacomplessivaId(BigDecimal aValore) {
		mPenacomplessivaId = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdAnnmanPenacompl + " - " + mAnnotazionemanualeId + " - " + mPenacomplessivaId;

		return lStr;
	}
}
