package siap.sico.residenza.model;

/**
* <p>Title: ResidenzaFascicoloSiusModel</p>
* <p>Description: Classe Model che rappresenta il ResidenzaFascicoloSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class ResidenzaFascicoloSiusModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 2369140660308305007L;

	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private BigDecimal mResIdResidenza;
	private BigDecimal mFasSiuIdFascicoloSius;

	// COSTRUTTORE DI DEFAULT
	public ResidenzaFascicoloSiusModel() {
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mResIdResidenza = null;
		this.mFasSiuIdFascicoloSius = null;
	}

	// COSTRUTTORE DI COPIA
	public ResidenzaFascicoloSiusModel(ResidenzaFascicoloSiusModel aModel) {
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mResIdResidenza = aModel.mResIdResidenza;
		this.mFasSiuIdFascicoloSius = aModel.mFasSiuIdFascicoloSius;
	}

	// COSTRUTTORE MODEL
	public ResidenzaFascicoloSiusModel(Date aDataInizioValidita, Date aDataFineValidita,
			BigDecimal aResIdResidenza, BigDecimal aFasSiuIdFascicoloSius) {
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mResIdResidenza = aResIdResidenza;
		this.mFasSiuIdFascicoloSius = aFasSiuIdFascicoloSius;
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

	public BigDecimal getFasSiuIdFascicoloSius() {
		return mFasSiuIdFascicoloSius;
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

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		mFasSiuIdFascicoloSius = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mDataInizioValidita + " - " + mDataFineValidita + " - " + mResIdResidenza + " - "
				+ mFasSiuIdFascicoloSius;

		return lStr;
	}

}