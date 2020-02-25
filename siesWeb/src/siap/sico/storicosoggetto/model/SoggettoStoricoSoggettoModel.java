package siap.sico.storicosoggetto.model;

import java.util.List;

import siap.sico.soggetto.model.SoggettoModel;
import f3b.model.GenericModel;

@SuppressWarnings("rawtypes")
public class SoggettoStoricoSoggettoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3688307219316031723L;

	private SoggettoModel mSoggetto;
	private List mStoricoSoggetto;
	private List mFascicoli;
	private List mFascicoliSius;

	public SoggettoStoricoSoggettoModel() {
		mSoggetto = null;
		mStoricoSoggetto = null;
		mFascicoli = null;
		mFascicoliSius = null;
	}

	public SoggettoStoricoSoggettoModel(SoggettoModel aSoggetto, List aStoricoSoggetto, List aFascicoli,
			List aFascicoliSius) {
		mSoggetto = aSoggetto;
		mStoricoSoggetto = aStoricoSoggetto;
		mFascicoli = aFascicoli;
		mFascicoliSius = aFascicoliSius;
	}

	//
	// METODI GET()
	//

	public SoggettoModel getSoggetto() {
		return mSoggetto;
	}

	public List getStoricoSoggetto() {
		return mStoricoSoggetto;
	}

	public List getFascicoloSiep() {
		return mFascicoli;
	}

	public List getFascicoloSius() {
		return mFascicoliSius;
	}

	//
	// METODI SET()
	//

	public void setSoggetto(SoggettoModel aValore) {
		mSoggetto = aValore;
	}

	public void setStoricoSoggetto(List aValore) {
		mStoricoSoggetto = aValore;
	}

	public void setFascicoloSiep(List aValore) {
		mFascicoli = aValore;
	}

	public void setFascicoloSius(List aValore) {
		mFascicoliSius = aValore;
	}

}