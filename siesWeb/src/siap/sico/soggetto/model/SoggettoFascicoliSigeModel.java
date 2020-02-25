package siap.sico.soggetto.model;

import siap.sige.fascicolo.model.FascicoloSigeSentenzaModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: SoggettoFascicoliSigeModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta i fascicoli Sige legati al Soggetto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class SoggettoFascicoliSigeModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3800139117276246127L;

	private FascicoloSigeSentenzaModel[] mFascicoli;
	private SoggettoModel mSoggetto;

	// COSTRUTTORE DI DEFAULT
	public SoggettoFascicoliSigeModel() {
		mSoggetto = new SoggettoModel();
	}

	// COSTRUTTORE DI COPIA
	public SoggettoFascicoliSigeModel(SoggettoFascicoliSigeModel aModel) {
		mSoggetto = aModel.getSoggetto();
		mFascicoli = aModel.getFascicoli();
	}

	//
	// METODI GET()
	//
	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public FascicoloSigeSentenzaModel[] getFascicoli() {
		return mFascicoli;
	}

	//
	// METODI SET()
	//

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setFascicoli(FascicoloSigeSentenzaModel[] aValore) {
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