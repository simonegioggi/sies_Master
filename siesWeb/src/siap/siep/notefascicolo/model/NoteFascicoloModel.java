package siap.siep.notefascicolo.model;

/**
* <p>Title: NoteFascicoloModel</p>
* <p>Description: Classe Model che rappresenta il NoteFascicolo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class NoteFascicoloModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = -6000076073993894929L;
	private String mNotaDispositivo;
	private String mNotaAvvocati;
	private BigDecimal mFasIdFascicoloSiep;

	// COSTRUTTORE DI DEFAULT
	public NoteFascicoloModel() {
		this.mNotaDispositivo = "";
		this.mNotaAvvocati = "";
		this.mFasIdFascicoloSiep = null;
	}

	// COSTRUTTORE DI COPIA
	public NoteFascicoloModel(NoteFascicoloModel aModel) {
		this.mNotaDispositivo = aModel.mNotaDispositivo;
		this.mNotaAvvocati = aModel.mNotaAvvocati;
		this.mFasIdFascicoloSiep = aModel.mFasIdFascicoloSiep;
	}

	// COSTRUTTORE MODEL
	public NoteFascicoloModel(String aNotaDispositivo, String aNotaAvvocati, BigDecimal aFasIdFascicoloSiep) {
		this.mNotaDispositivo = aNotaDispositivo;
		this.mNotaAvvocati = aNotaAvvocati;
		this.mFasIdFascicoloSiep = aFasIdFascicoloSiep;
	}

	//
	// METODI GET()
	//

	public String getNotaDispositivo() {
		return mNotaDispositivo;
	}

	public String getNotaAvvocati() {
		return mNotaAvvocati;
	}

	public BigDecimal getFasIdFascicoloSiep() {
		return mFasIdFascicoloSiep;
	}

	//
	// METODI SET()
	//

	public void setNotaDispositivo(String aValore) {
		mNotaDispositivo = aValore;
	}

	public void setNotaAvvocati(String aValore) {
		mNotaAvvocati = aValore;
	}

	public void setFasIdFascicoloSiep(BigDecimal aValore) {
		mFasIdFascicoloSiep = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mNotaDispositivo + " - " + mNotaAvvocati + " - " + mFasIdFascicoloSiep;

		return lStr;
	}
}
