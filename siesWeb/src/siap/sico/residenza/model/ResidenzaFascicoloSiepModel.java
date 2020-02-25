package siap.sico.residenza.model;

/**
* <p>Title: ResidenzaFascicoloSiepModel</p>
* <p>Description: Classe Model che rappresenta il ResidenzaFascicoloSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class ResidenzaFascicoloSiepModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 3399463416040010777L;

	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private BigDecimal mResIdResidenza;
	private BigDecimal mFasSieIdFascicoloSiep;

	// COSTRUTTORE DI DEFAULT
	public ResidenzaFascicoloSiepModel() {
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mResIdResidenza = null;
		this.mFasSieIdFascicoloSiep = null;
	}

	// COSTRUTTORE DI COPIA
	public ResidenzaFascicoloSiepModel(ResidenzaFascicoloSiepModel aModel) {
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mResIdResidenza = aModel.mResIdResidenza;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
	}

	// COSTRUTTORE MODEL
	public ResidenzaFascicoloSiepModel(Date aDataInizioValidita, Date aDataFineValidita,
			BigDecimal aResIdResidenza, BigDecimal aFasSieIdFascicoloSiep) {
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mResIdResidenza = aResIdResidenza;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
	}

	//
	// METODI GET()
	//

	public Date getDataInizioValidita() {
		return mDataInizioValidita;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public BigDecimal getResIdResidenza() {
		return mResIdResidenza;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	//
	// METODI SET()
	//

	public void setDataInizioValidita(Date aValore) {
		mDataInizioValidita = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setResIdResidenza(BigDecimal aValore) {
		mResIdResidenza = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mDataInizioValidita + " - " + mDataFineValidita + " - " + mResIdResidenza + " - "
				+ mFasSieIdFascicoloSiep;

		return lStr;
	}

}