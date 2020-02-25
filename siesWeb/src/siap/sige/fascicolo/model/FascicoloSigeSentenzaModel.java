package siap.sige.fascicolo.model;

import siap.siep.sentenza.model.SentenzaModel;
import f3b.model.GenericModel;

public class FascicoloSigeSentenzaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7147275662284539921L;

	private FascicoloSigeEstesoModel mFascicoloEstesoSige;
	private SentenzaModel mSentenza;

	// COSTRUTTORE DI DEFAULT
	public FascicoloSigeSentenzaModel() {
	}

	// COSTRUTTORE DI COPIA
	public FascicoloSigeSentenzaModel(FascicoloSigeSentenzaModel aModel) {
		mFascicoloEstesoSige = aModel.getFascicoloEstesoSige();
		mSentenza = aModel.getSentenza();
	}

	public FascicoloSigeEstesoModel getFascicoloEstesoSige() {
		return mFascicoloEstesoSige;
	}

	public void setFascicoloEstesoSige(FascicoloSigeEstesoModel mFascicoloEstesoSige) {
		this.mFascicoloEstesoSige = mFascicoloEstesoSige;
	}

	public SentenzaModel getSentenza() {
		return mSentenza;
	}

	public void setSentenza(SentenzaModel mSentenza) {
		this.mSentenza = mSentenza;
	}

}