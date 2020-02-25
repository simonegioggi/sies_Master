package siap.jms.jmscode.model;

/**
* <p>Title: JmsCodeModel</p>
* <p>Description: Classe Model che rappresenta il JmsCode</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.model.GenericModel;

public class JmsCodeModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 2930122951561498713L;

	private String mDominio;
	private String mCodice;
	private String mDescrizione;

	// COSTRUTTORE DI DEFAULT
	public JmsCodeModel() {
		this.mDominio = "";
		this.mCodice = "";
		this.mDescrizione = "";
	}

	// COSTRUTTORE DI COPIA
	public JmsCodeModel(JmsCodeModel aModel) {
		this.mDominio = aModel.mDominio;
		this.mCodice = aModel.mCodice;
		this.mDescrizione = aModel.mDescrizione;
	}

	// COSTRUTTORE MODEL
	public JmsCodeModel(String aDominio, String aCodice, String aDescrice, String aDescrizione) {
		this.mDominio = aDominio;
		this.mCodice = aCodice;
		this.mDescrizione = aDescrizione;
	}

	//
	// METODI GET()
	//

	public String getDominio() {
		return mDominio;
	}

	public String getCodice() {
		return mCodice;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	//
	// METODI SET()
	//

	public void setDominio(String aValore) {
		mDominio = aValore;
	}

	public void setCodice(String aValore) {
		mCodice = aValore;
	}

	public void setDescrizione(String aValore) {
		mDescrizione = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mDominio + " - " + mCodice + " - " + mDescrizione;

		return lStr;
	}

}