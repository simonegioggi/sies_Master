package siap.sige.sentenza.model;

/**
* <p>Title: FasSigeSentenzaRicercaModel</p>
* <p>Description: Classe Model per la ricerca delle sentenze riferite da Fascicoli SIGE </p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.sentenza.model.SentenzaModel;
import f3b.model.GenericModel;

public class FasSigeSentenzaRicercaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -361072322071269379L;

	private BigDecimal mNumFascicoliSige;
	private FasSigeSentenzaModel mFasSigeSentenzaModel;
	private SentenzaModel mSentenzaModel;

	// COSTRUTTORE DI DEFAULT
	public FasSigeSentenzaRicercaModel() {
		this.mNumFascicoliSige = null;
		this.mFasSigeSentenzaModel = new FasSigeSentenzaModel();
		this.mSentenzaModel = new SentenzaModel();
	}

	// COSTRUTTORE DI COPIA
	public FasSigeSentenzaRicercaModel(FasSigeSentenzaRicercaModel aModel) {
		this.mNumFascicoliSige = aModel.mNumFascicoliSige;
		this.mFasSigeSentenzaModel = new FasSigeSentenzaModel(aModel.mFasSigeSentenzaModel);
		this.mSentenzaModel = new SentenzaModel(aModel.mSentenzaModel);
	}

	// COSTRUTTORE MODEL
	public FasSigeSentenzaRicercaModel(BigDecimal aNumFascicoliSige,
			FasSigeSentenzaModel aFasSigeSentenzaModel, SentenzaModel aSentenzaModel) {
		this.mNumFascicoliSige = aNumFascicoliSige;
		this.mFasSigeSentenzaModel = aFasSigeSentenzaModel;
		this.mSentenzaModel = aSentenzaModel;
	}

	//
	// METODI GET()
	//
	public BigDecimal getNumFascicoliSige() {
		return mNumFascicoliSige;
	}

	public FasSigeSentenzaModel getFasSigeSentenzaModel() {
		return mFasSigeSentenzaModel;
	}

	public SentenzaModel getSentenzaModel() {
		return mSentenzaModel;
	}

	//
	// METODI SET()
	//
	public void setNumFascicoliSige(BigDecimal aValore) {
		mNumFascicoliSige = aValore;
	}

	public void setFasSigeSentenzaModel(FasSigeSentenzaModel aValore) {
		mFasSigeSentenzaModel = aValore;
	}

	public void setSentenzaModel(SentenzaModel aValore) {
		mSentenzaModel = aValore;
	}

}