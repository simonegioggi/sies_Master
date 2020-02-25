package siap.sico.decodifiche.model;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import f3b.model.GenericModel;

public class ComuneProvinciaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8666810492283006597L;

	private String mCodProvincia;
	private String mProvincia;

	// COSTRUTTORE DI DEFAULT
	public ComuneProvinciaModel() {
		this.mCodProvincia = "";
		this.mProvincia = "";
	}

	// COSTRUTTORE DI COPIA
	public ComuneProvinciaModel(ComuneProvinciaModel aModel) {

		this.mCodProvincia = aModel.mCodProvincia;
		this.mProvincia = aModel.mProvincia;

	}

	// COSTRUTTORE MODEL
	public ComuneProvinciaModel(String aCodProvincia, String aProvincia) {

		this.mCodProvincia = aCodProvincia;
		this.mProvincia = aProvincia;

	}

	//
	// METODI GET()
	//

	public String getCodProvincia() {
		return mCodProvincia;
	}

	public String getProvincia() {
		return mProvincia;
	}

	//
	// METODI SET()
	//

	public void setCodProvincia(String aValore) {
		this.mCodProvincia = aValore;
	}

	public void setProvincia(String aValore) {
		this.mProvincia = aValore;
	}

	public String toString() {
		String lToString = this.mCodProvincia + " - " + this.mProvincia + " - ";
		return lToString;
	}

}