package siap.siepe.fascicolo.model;

/**
* <p>Title: FascicoloSoggAttModel</p>
* <p>Description: Classe Model che rappresenta i dati di FascicoloSiepe, Soggetto.
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.soggetto.model.SoggettoModel;
import f3b.model.GenericModel;

public class FascicoloSoggAttModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4928930067826376181L;

	private FascicoloSiepeRicercaModel mFascicoloSiepeRicercaModel;
	private SoggettoModel mSoggettoModel;

	// COSTRUTTORE DI DEFAULT
	public FascicoloSoggAttModel() {
		this.mFascicoloSiepeRicercaModel = new FascicoloSiepeRicercaModel();
		this.mSoggettoModel = new SoggettoModel();
	}

	// COSTRUTTORE DI COPIA
	public FascicoloSoggAttModel(FascicoloSoggAttModel aModel) {
		this.mFascicoloSiepeRicercaModel = new FascicoloSiepeRicercaModel(aModel.mFascicoloSiepeRicercaModel);
		this.mSoggettoModel = new SoggettoModel(aModel.mSoggettoModel);
	}

	// COSTRUTTORE MODEL
	public FascicoloSoggAttModel(FascicoloSiepeRicercaModel aFascicoloSiepeRicercaModel,
			SoggettoModel aSoggettoModel) {
		this.mFascicoloSiepeRicercaModel = aFascicoloSiepeRicercaModel;
		this.mSoggettoModel = aSoggettoModel;
	}

	// METODI GET()
	//
	public FascicoloSiepeRicercaModel getFascicoloSiepeRicercaModel() {
		return mFascicoloSiepeRicercaModel;
	}

	public SoggettoModel getSoggettoModel() {
		return mSoggettoModel;
	}

	// METODI SET()
	//
	public void setFascicoloSiepeRicercaModel(FascicoloSiepeRicercaModel aValore) {
		mFascicoloSiepeRicercaModel = aValore;
	}

	public void setSoggettoModel(SoggettoModel aValore) {
		mSoggettoModel = aValore;
	}

}