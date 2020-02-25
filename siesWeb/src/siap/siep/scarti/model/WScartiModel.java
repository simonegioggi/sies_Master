package siap.siep.scarti.model;

/**
* <p>Title: WScartiModel</p>
* <p>Description: Classe Model che rappresenta il WScarti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class WScartiModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -8107948925110249012L;

	private BigDecimal mIdScarti;
	private String mTabella;
	private BigDecimal mAnnRes;
	private String mNumRes;
	private String mLetRes;
	private String mChiaveAlternativa;
	private String mNoteScarto;
	private String mCausaScarto;

	// COSTRUTTORE DI DEFAULT
	public WScartiModel() {
		this.mIdScarti = null;
		this.mTabella = "";
		this.mAnnRes = null;
		this.mNumRes = "";
		this.mLetRes = "";
		this.mChiaveAlternativa = "";
		this.mNoteScarto = "";
		this.mCausaScarto = "";
	}

	// COSTRUTTORE DI COPIA
	public WScartiModel(WScartiModel aModel) {
		this.mIdScarti = aModel.mIdScarti;
		this.mTabella = aModel.mTabella;
		this.mAnnRes = aModel.mAnnRes;
		this.mNumRes = aModel.mNumRes;
		this.mLetRes = aModel.mLetRes;
		this.mChiaveAlternativa = aModel.mChiaveAlternativa;
		this.mNoteScarto = aModel.mNoteScarto;
		this.mCausaScarto = aModel.mCausaScarto;
	}

	// COSTRUTTORE MODEL
	public WScartiModel(BigDecimal aIdScarti, String aTabella, BigDecimal aAnnRes, String aNumRes,
			String aLetRes, String aChiaveAlternativa, String aNoteScarto, String aCausaScarto) {
		this.mIdScarti = aIdScarti;
		this.mTabella = aTabella;
		this.mAnnRes = aAnnRes;
		this.mNumRes = aNumRes;
		this.mLetRes = aLetRes;
		this.mChiaveAlternativa = aChiaveAlternativa;
		this.mNoteScarto = aNoteScarto;
		this.mCausaScarto = aCausaScarto;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdScarti() {
		return mIdScarti;
	}

	public String getTabella() {
		return mTabella;
	}

	public BigDecimal getAnnRes() {
		return mAnnRes;
	}

	public String getNumRes() {
		return mNumRes;
	}

	public String getLetRes() {
		return mLetRes;
	}

	public String getChiaveAlternativa() {
		return mChiaveAlternativa;
	}

	public String getNoteScarto() {
		return mNoteScarto;
	}

	public String getCausaScarto() {
		return mCausaScarto;
	}

	//
	// METODI SET()
	//

	public void setIdScarti(BigDecimal aValore) {
		mIdScarti = aValore;
	}

	public void setTabella(String aValore) {
		mTabella = aValore;
	}

	public void setAnnRes(BigDecimal aValore) {
		mAnnRes = aValore;
	}

	public void setNumRes(String aValore) {
		mNumRes = aValore;
	}

	public void setLetRes(String aValore) {
		mLetRes = aValore;
	}

	public void setChiaveAlternativa(String aValore) {
		mChiaveAlternativa = aValore;
	}

	public void setNoteScarto(String aValore) {
		mNoteScarto = aValore;
	}

	public void setCausaScarto(String aValore) {
		mCausaScarto = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdScarti + " - " + mTabella + " - " + mAnnRes + " - " + mNumRes + " - " + mLetRes + " - "
				+ mChiaveAlternativa + " - " + mNoteScarto + " - " + mCausaScarto;

		return lStr;
	}

}