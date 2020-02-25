package siap.sico.versione.model;

/**
* <p>Title: VersioneModel</p>
* <p>Description: Classe Model che rappresenta il Versione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;

public class VersioneModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -7598552440915694065L;

	private String mCodVersione;
	private Date mData;
	private String mDescrizione;

	// COSTRUTTORE DI DEFAULT
	public VersioneModel() {
		this.mCodVersione = "No Version Found";
		this.mData = null;
		this.mDescrizione = "";
	}

	// COSTRUTTORE DI COPIA
	public VersioneModel(VersioneModel aModel) {
		this.mCodVersione = aModel.mCodVersione;
		this.mData = aModel.mData;
		this.mDescrizione = aModel.mDescrizione;
	}

	// COSTRUTTORE MODEL
	public VersioneModel(String aCodVersione, Date aData, String aDescrizione) {
		this.mCodVersione = aCodVersione;
		this.mData = aData;
		this.mDescrizione = aDescrizione;
	}

	//
	// METODI GET()
	//

	public String getCodVersione() {
		return mCodVersione;
	}

	public Date getData() {
		return mData;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	//
	// METODI SET()
	//

	public void setCodVersione(String aValore) {
		mCodVersione = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setDescrizione(String aValore) {
		mDescrizione = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mCodVersione + " - " + mData + " - " + mDescrizione;

		return lStr;
	}

}
