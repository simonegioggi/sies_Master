package siap.siep.sentenza.model;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: EventoNotificaModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta i fascicoli legati alla sentenza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Agile Servizi
 * </p>
 * 
 * @version 5.0
 */
public class SentenzaFascicoliModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1465989816270116811L;

	private FascicoloSiepModel[] mFascicoli;
	private SentenzaModel mSentenza;

	// COSTRUTTORE DI DEFAULT
	public SentenzaFascicoliModel() {
		mSentenza = new SentenzaModel();
	}

	// COSTRUTTORE DI COPIA
	public SentenzaFascicoliModel(SentenzaFascicoliModel aModel) {
		mSentenza = aModel.getSentenza();
		mFascicoli = aModel.getFascicoli();
	}

	//
	// METODI GET()
	//
	public SentenzaModel getSentenza() {
		return mSentenza;
	}

	public FascicoloSiepModel[] getFascicoli() {
		return mFascicoli;
	}

	//
	// METODI SET()
	//

	public void setSentenza(SentenzaModel aValore) {
		mSentenza = aValore;
	}

	public void setFascicoli(FascicoloSiepModel[] aValore) {
		mFascicoli = aValore;
	}

	public String toString() {
		String lStr = new String();

		if (mSentenza != null)
			lStr = mSentenza.toString();

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