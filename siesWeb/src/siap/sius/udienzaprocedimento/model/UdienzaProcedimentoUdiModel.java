package siap.sius.udienzaprocedimento.model;

/**
* <p>Title: udienzaprocedimentoUdi</p>
* <p>Description: Classe Model che rappresenta il UdienzaProcedimento
 * più alcuni campi identificativi dell'Udienza.</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class UdienzaProcedimentoUdiModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7680407413260508305L;

	private UdienzaProcedimentoModel mUdienzaProcedimento;
	private Date mDataUdienza;
	private BigDecimal mNumCollegio;

	// COSTRUTTORE DI DEFAULT
	public UdienzaProcedimentoUdiModel() {
		mUdienzaProcedimento = null;
		mDataUdienza = null;
		mNumCollegio = null;
	}

	// COSTRUTTORE DI COPIA
	public UdienzaProcedimentoUdiModel(UdienzaProcedimentoUdiModel aModel) {
		mUdienzaProcedimento = aModel.mUdienzaProcedimento;
		mDataUdienza = aModel.mDataUdienza;
		mNumCollegio = aModel.mNumCollegio;

	}

	// COSTRUTTORE MODEL
	public UdienzaProcedimentoUdiModel(UdienzaProcedimentoModel aUdienzaProcedimento, Date aDataUdienza,
			BigDecimal aNumCollegio) {
		mUdienzaProcedimento = aUdienzaProcedimento;
		mDataUdienza = aDataUdienza;
		mNumCollegio = aNumCollegio;
	}

	// COSTRUTTORE MODEL
	public UdienzaProcedimentoUdiModel(UdienzaProcedimentoModel aUdienzaProcedimento) {
		mUdienzaProcedimento = aUdienzaProcedimento;
		mDataUdienza = null;
		mNumCollegio = null;
	}

	//
	// METODI GET()
	//
	public UdienzaProcedimentoModel getUdienzaProcedimento() {
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
	public void setUdienzaProcedimento(UdienzaProcedimentoModel aValore) {
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