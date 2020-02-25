package siap.sige.udienzaprocedimento.model;

/**
* <p>Title: UdienzaProcSigeUdienzaModel</p>
* <p>Description: Classe Model che rappresenta il UdienzaProcedimento
 * più alcuni campi identificativi dell'Udienza.</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class UdienzaProcSigeUdienzaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4029420308362632857L;

	private UdienzaProcedimentoSigeModel mUdienzaProcedimento;
	private Date mDataUdienza;
	private BigDecimal mNumCollegio;

	// COSTRUTTORE DI DEFAULT
	public UdienzaProcSigeUdienzaModel() {
		mUdienzaProcedimento = null;
		mDataUdienza = null;
		mNumCollegio = null;
	}

	// COSTRUTTORE DI COPIA
	public UdienzaProcSigeUdienzaModel(UdienzaProcSigeUdienzaModel aModel) {
		mUdienzaProcedimento = aModel.mUdienzaProcedimento;
		mDataUdienza = aModel.mDataUdienza;
		mNumCollegio = aModel.mNumCollegio;

	}

	// COSTRUTTORE MODEL
	public UdienzaProcSigeUdienzaModel(UdienzaProcedimentoSigeModel aUdienzaProcedimento, Date aDataUdienza,
			BigDecimal aNumCollegio) {
		mUdienzaProcedimento = aUdienzaProcedimento;
		mDataUdienza = aDataUdienza;
		mNumCollegio = aNumCollegio;
	}

	// COSTRUTTORE MODEL
	public UdienzaProcSigeUdienzaModel(UdienzaProcedimentoSigeModel aUdienzaProcedimento) {
		mUdienzaProcedimento = aUdienzaProcedimento;
		mDataUdienza = null;
		mNumCollegio = null;
	}

	//
	// METODI GET()
	//
	public UdienzaProcedimentoSigeModel getUdienzaProcedimento() {
		return mUdienzaProcedimento;
	}

	public Date getDataUdienza() {
		return mDataUdienza;
	}

	public BigDecimal getNumCollegio() {
		return mNumCollegio;
	}

	//
	// METODI SET()
	//
	public void setUdienzaProcedimento(UdienzaProcedimentoSigeModel aValore) {
		mUdienzaProcedimento = aValore;
	}

	public void setDataUdienza(Date aValore) {
		mDataUdienza = aValore;
	}

	public void setNumCollegio(BigDecimal aValore) {
		mNumCollegio = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mUdienzaProcedimento + " - " + mDataUdienza + " - " + mNumCollegio;
		return lStr;
	}

}