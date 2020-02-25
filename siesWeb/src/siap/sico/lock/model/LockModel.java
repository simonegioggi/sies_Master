package siap.sico.lock.model;

/**
* <p>Title: LockModel</p>
* <p>Description: Classe Model che rappresenta il Lock</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class LockModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -1706547631057586022L;

	private String mEntity;
	private String mCodOperatore;
	private Date mData;
	private String mIdEntity;

	// COSTRUTTORE DI DEFAULT
	public LockModel() {
		this.mEntity = "";
		this.mCodOperatore = "";
		this.mData = DateUtils.getSysDate();
		this.mIdEntity = "";

	}

	// COSTRUTTORE DI COPIA
	public LockModel(LockModel aModel) {
		this.mEntity = aModel.mEntity;
		this.mCodOperatore = aModel.mCodOperatore;
		this.mData = aModel.mData;
		this.mIdEntity = aModel.mIdEntity;

	}

	// COSTRUTTORE MODEL
	public LockModel(String aEntity, String aCodOperatore, Date aData, String aIdEntity) {
		this.mEntity = aEntity;
		this.mCodOperatore = aCodOperatore;
		this.mData = aData;
		this.mIdEntity = aIdEntity;

	}

	//
	// METODI GET()
	//

	public String getEntity() {
		return mEntity;
	}

	public String getCodOperatore() {
		return mCodOperatore;
	}

	public Date getData() {
		return mData;
	}

	public String getIdEntity() {
		return mIdEntity;
	}

	//
	// METODI SET()
	//

	public void setEntity(String aValore) {
		mEntity = aValore;
	}

	public void setCodOperatore(String aValore) {
		mCodOperatore = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setIdEntity(String aValore) {
		mIdEntity = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mEntity + " - " + mCodOperatore + " - " + mData + " - " + mIdEntity + " - ";

		return lStr;
	}

}