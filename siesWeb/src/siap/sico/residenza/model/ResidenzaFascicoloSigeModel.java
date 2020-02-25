package siap.sico.residenza.model;

/**
* <p>Title: ResidenzaFascicoloSigeModel</p>
* <p>Description: Classe Model che rappresenta la ResidenzaFascicoloSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class ResidenzaFascicoloSigeModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -4010629502063578890L;

	private Date mDataInizioValidita;
	private Date mDataFineValidita;
	private BigDecimal mResIdResidenza;
	private BigDecimal mFasSigeIdFascicoloSige;

	// COSTRUTTORE DI DEFAULT
	public ResidenzaFascicoloSigeModel() {
		this.mDataInizioValidita = null;
		this.mDataFineValidita = null;
		this.mResIdResidenza = null;
		this.mFasSigeIdFascicoloSige = null;
	}

	// COSTRUTTORE DI COPIA
	public ResidenzaFascicoloSigeModel(ResidenzaFascicoloSigeModel aModel) {
		this.mDataInizioValidita = aModel.mDataInizioValidita;
		this.mDataFineValidita = aModel.mDataFineValidita;
		this.mResIdResidenza = aModel.mResIdResidenza;
		this.mFasSigeIdFascicoloSige = aModel.mFasSigeIdFascicoloSige;
	}

	// COSTRUTTORE MODEL
	public ResidenzaFascicoloSigeModel(Date aDataInizioValidita, Date aDataFineValidita,
			BigDecimal aResIdResidenza, BigDecimal aFasSigeIdFascicoloSige) {
		this.mDataInizioValidita = aDataInizioValidita;
		this.mDataFineValidita = aDataFineValidita;
		this.mResIdResidenza = aResIdResidenza;
		this.mFasSigeIdFascicoloSige = aFasSigeIdFascicoloSige;
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

	public BigDecimal getFasSigeIdFascicoloSige() {
		return mFasSigeIdFascicoloSige;
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

	public void setFasSigeIdFascicoloSige(BigDecimal aValore) {
		mFasSigeIdFascicoloSige = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mDataInizioValidita + " - " + mDataFineValidita + " - " + mResIdResidenza + " - "
				+ mFasSigeIdFascicoloSige;

		return lStr;
	}

}