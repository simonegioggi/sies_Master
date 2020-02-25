package siap.sico.calendar.model;

import f3b.model.GenericModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class TotalePeriodoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8217922022596199187L;

	private int mTotaleAnni;
	private int mTotaleMesi;
	private int mTotaleGiorni;

	// COSTRUTTORE DI DEFAULT
	public TotalePeriodoModel() {
		this.mTotaleAnni = 0;
		this.mTotaleMesi = 0;
		this.mTotaleGiorni = 0;
	}

	// COSTRUTTORE DI COPIA
	public TotalePeriodoModel(TotalePeriodoModel aModel) {
		this.mTotaleAnni = aModel.mTotaleAnni;
		this.mTotaleMesi = aModel.mTotaleMesi;
		this.mTotaleGiorni = aModel.mTotaleGiorni;
	}

	// COSTRUTTORE MODEL
	public TotalePeriodoModel(int aNumAnni, int aNumMesi, int aNumGiorni) {
		this.mTotaleAnni = aNumAnni;
		this.mTotaleMesi = aNumMesi;
		this.mTotaleGiorni = aNumGiorni;
	}

	//
	// METODI GET()
	//

	public int getTotaleAnni() {
		return mTotaleAnni;
	}

	public int getTotaleMesi() {
		return mTotaleMesi;
	}

	public int getTotaleGiorni() {
		return mTotaleGiorni;
	}

	//
	// METODI SET()
	//

	public void setTotaleAnni(int aValore) {
		mTotaleAnni = aValore;
	}

	public void setTotaleMesi(int aValore) {
		mTotaleMesi = aValore;
	}

	public void setTotaleGiorni(int aValore) {
		mTotaleGiorni = aValore;
	}

	public boolean isSignificativa() {
		return (getTotaleAnni() != 0 || getTotaleMesi() != 0 || getTotaleGiorni() != 0);
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mTotaleAnni + " - " + mTotaleMesi + " - " + mTotaleGiorni;

		return lStr;
	}

}