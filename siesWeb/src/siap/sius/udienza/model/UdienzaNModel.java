package siap.sius.udienza.model;

/**
* <p>Title: UdienzaModel</p>
* <p>Description: Classe Model che rappresenta il Udienza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

public class UdienzaNModel extends UdienzaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4793202330364121792L;

	private BigDecimal mNumProvvedimenti;
	private BigDecimal mNumProvvedimentiDaRinvio;

	// COSTRUTTORE DI DEFAULT
	public UdienzaNModel() {
		super();
		mNumProvvedimenti = null;
		mNumProvvedimentiDaRinvio = null;

	}

	// ALTRI COSTRUTTORI
	public UdienzaNModel(UdienzaModel aModel) {
		super(aModel);
		mNumProvvedimenti = null;
		mNumProvvedimentiDaRinvio = null;

	}

	public UdienzaNModel(UdienzaNModel aModel) {
		super((UdienzaModel) aModel);
		mNumProvvedimenti = aModel.mNumProvvedimenti;
		mNumProvvedimenti = aModel.mNumProvvedimentiDaRinvio;

	}

	public UdienzaNModel(UdienzaModel aModel, BigDecimal aNumProvvedimenti) {
		super(aModel);
		mNumProvvedimenti = aNumProvvedimenti;
		mNumProvvedimentiDaRinvio = null;
	}

	public UdienzaNModel(UdienzaModel aModel, BigDecimal aNumProvvedimenti,
			BigDecimal aNumProvvedimentiDaRinvio) {
		super(aModel);
		mNumProvvedimenti = aNumProvvedimenti;
		mNumProvvedimentiDaRinvio = aNumProvvedimentiDaRinvio;
	}

	//
	// METODI GET()
	//

	public BigDecimal getNumProvvedimenti() {
		return mNumProvvedimenti;
	}

	public BigDecimal getNumProvvedimentiDaRinvio() {
		return mNumProvvedimentiDaRinvio;
	}

	//
	// METODI SET()
	//

	public void setNumProvvedimenti(BigDecimal aValore) {
		mNumProvvedimenti = aValore;
	}

	public void setNumProvvedimentiiDaRinvio(BigDecimal aValore) {
		mNumProvvedimentiDaRinvio = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = super.toString() + " - " + mNumProvvedimenti + " - " + mNumProvvedimentiDaRinvio;
		return lStr;
	}

}