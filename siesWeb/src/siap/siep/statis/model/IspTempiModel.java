package siap.siep.statis.model;

/**
* <p>Title: IspTempiIscrizioneModel</p>
* <p>Description: Classe Model che rappresenta il IspTempiIscrizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.model.GenericModel;

public class IspTempiModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 13837019769774484L;
	private Integer mEntro5;
	private Integer mEntro20;
	private Integer mEntro30;
	private Integer mEntro60;
	private Integer mEntro90;
	private Integer mOltre90;
	private Integer mAnno;
	// MEV 27 (statistiche CPP)
	private String mPeriodo;
	private String mTipoDistinta;

	// COSTRUTTORE DI DEFAULT
	public IspTempiModel() {
		this.mEntro5 = null;
		this.mEntro20 = null;
		this.mEntro30 = null;
		this.mEntro60 = null;
		this.mEntro90 = null;
		this.mOltre90 = null;
		this.mAnno = null;
		this.mPeriodo = null;
		this.mTipoDistinta = null;
	}

	// COSTRUTTORE MODEL per SqlDAO
	public IspTempiModel(Integer aEntro5, Integer aEntro20, Integer aEntro30, Integer aEntro60,
			Integer aEntro90, Integer aOltre90, Integer aAnno, String aPeriodo, String aTipoDistinta) {
		this.mEntro5 = aEntro5;
		this.mEntro20 = aEntro20;
		this.mEntro30 = aEntro30;
		this.mEntro60 = aEntro60;
		this.mEntro90 = aEntro90;
		this.mOltre90 = aOltre90;
		this.mAnno = aAnno;
		this.mPeriodo = aPeriodo;
		this.mTipoDistinta = aTipoDistinta;
	}

	/**
	 * @return Returns the mAnno.
	 */
	public Integer getAnno() {
		return mAnno;
	}

	/**
	 * @param anno
	 *            The mAnno to set.
	 */
	public void setAnno(Integer anno) {
		mAnno = anno;
	}

	/**
	 * @return Returns the mEntro20.
	 */
	public Integer getEntro20() {
		return mEntro20;
	}

	/**
	 * @param entro20
	 *            The mEntro20 to set.
	 */
	public void setEntro20(Integer entro20) {
		mEntro20 = entro20;
	}

	/**
	 * @return Returns the mEntro30.
	 */
	public Integer getEntro30() {
		return mEntro30;
	}

	/**
	 * @param entro30
	 *            The mEntro30 to set.
	 */
	public void setEntro30(Integer entro30) {
		mEntro30 = entro30;
	}

	/**
	 * @return Returns the mEntro5.
	 */
	public Integer getEntro5() {
		return mEntro5;
	}

	/**
	 * @param entro5
	 *            The mEntro5 to set.
	 */
	public void setEntro5(Integer entro5) {
		mEntro5 = entro5;
	}

	/**
	 * @return Returns the mEntro60.
	 */
	public Integer getEntro60() {
		return mEntro60;
	}

	/**
	 * @param entro60
	 *            The mEntro60 to set.
	 */
	public void setEntro60(Integer entro60) {
		mEntro60 = entro60;
	}

	/**
	 * @return Returns the mEntro90.
	 */
	public Integer getEntro90() {
		return mEntro90;
	}

	/**
	 * @param entro90
	 *            The mEntro90 to set.
	 */
	public void setEntro90(Integer entro90) {
		mEntro90 = entro90;
	}

	/**
	 * @return Returns the mOltre90.
	 */
	public Integer getOltre90() {
		return mOltre90;
	}

	/**
	 * @param oltre90
	 *            The mOltre90 to set.
	 */
	public void setOltre90(Integer oltre90) {
		mOltre90 = oltre90;
	}

	//
	public String getPeriodo() {
		return mPeriodo;
	}

	public void setPeriodo(String aValorePeriodo) {
		mPeriodo = aValorePeriodo;
	}

	//
	public String getTipo() {
		return mTipoDistinta;
	}

	public void setTipo(String aValoreTipo) {
		mTipoDistinta = aValoreTipo;
	}
}
