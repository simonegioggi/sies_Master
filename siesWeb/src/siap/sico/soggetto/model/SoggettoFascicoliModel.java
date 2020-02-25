package siap.sico.soggetto.model;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: SoggettoFascicoliModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta i fascicoli legati al Soggetto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class SoggettoFascicoliModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -869267685178470766L;

	private FascicoloSiepModel[] mFascicoli;
	private SoggettoModel mSoggetto;

	// COSTRUTTORE DI DEFAULT
	public SoggettoFascicoliModel() {
		mSoggetto = new SoggettoModel();
	}

	// COSTRUTTORE DI COPIA
	public SoggettoFascicoliModel(SoggettoFascicoliModel aModel) {
		mSoggetto = aModel.getSoggetto();
		mFascicoli = aModel.getFascicoli();
	}

	//
	// METODI GET()
	//
	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public FascicoloSiepModel[] getFascicoli() {
		return mFascicoli;
	}

	//
	// METODI SET()
	//

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setFascicoli(FascicoloSiepModel[] aValore) {
		mFascicoli = aValore;
	}

	public String toString() {
		String lStr = new String();

		if (mSoggetto != null)
			lStr = mSoggetto.toString();

		if (mFascicoli != null) {
			int count = 0;
			while (count < mFascicoli.length) {
				lStr += mFascicoli[count].toString();
				count++;
			}
		}

		return lStr;
	}

}