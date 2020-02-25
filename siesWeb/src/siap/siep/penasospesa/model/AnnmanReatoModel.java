package siap.siep.penasospesa.model;

/**
* <p>Title: AnnmanReatoModel</p>
* <p>Description: Classe Model che rappresenta il AnnmanReato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class AnnmanReatoModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = -962675003652341446L;
	private BigDecimal mIdAnnmanReato;
	private BigDecimal mAnnotazionemanualeId;
	private BigDecimal mReatoId;

	// COSTRUTTORE DI DEFAULT
	public AnnmanReatoModel() {
		this.mIdAnnmanReato = null;
		this.mAnnotazionemanualeId = null;
		this.mReatoId = null;
	}

	// COSTRUTTORE DI COPIA
	public AnnmanReatoModel(AnnmanReatoModel aModel) {
		this.mIdAnnmanReato = aModel.mIdAnnmanReato;
		this.mAnnotazionemanualeId = aModel.mAnnotazionemanualeId;
		this.mReatoId = aModel.mReatoId;
	}

	// COSTRUTTORE MODEL
	public AnnmanReatoModel(BigDecimal aIdAnnmanReato, BigDecimal aAnnotazionemanualeId,
			BigDecimal aReatoId) {
		this.mIdAnnmanReato = aIdAnnmanReato;
		this.mAnnotazionemanualeId = aAnnotazionemanualeId;
		this.mReatoId = aReatoId;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAnnmanReato() {
		return mIdAnnmanReato;
	}

	public BigDecimal getAnnotazionemanualeId() {
		return mAnnotazionemanualeId;
	}

	public BigDecimal getReatoId() {
		return mReatoId;
	}

	//
	// METODI SET()
	//

	public void setIdAnnmanReato(BigDecimal aValore) {
		mIdAnnmanReato = aValore;
	}

	public void setAnnotazionemanualeId(BigDecimal aValore) {
		mAnnotazionemanualeId = aValore;
	}

	public void setReatoId(BigDecimal aValore) {
		mReatoId = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdAnnmanReato + " - " + mAnnotazionemanualeId + " - " + mReatoId;

		return lStr;
	}
}
