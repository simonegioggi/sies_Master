package siap.siep.modulocumulo.model;

import java.util.List;

import f3b.model.GenericModel;

@SuppressWarnings("rawtypes")
public class DettaglioPenaComplessivaCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1650454257577018810L;

	private PenaComplessivaSanzioneSostitutivaCumuloModel mPenaComplSanzioneSostCum;
	private List mContinuazioni;

	public DettaglioPenaComplessivaCumuloModel() {
		mPenaComplSanzioneSostCum = null;
		mContinuazioni = null;
	}

	public DettaglioPenaComplessivaCumuloModel(
			PenaComplessivaSanzioneSostitutivaCumuloModel aPenaComplSanzioneSostCum, List aContinuazioni) {
		mPenaComplSanzioneSostCum = aPenaComplSanzioneSostCum;
		mContinuazioni = aContinuazioni;
	}

	//
	// METODI GET()
	//

	public PenaComplessivaSanzioneSostitutivaCumuloModel getPenaComplessivaSanzioneSostitutivaCumulo() {
		return mPenaComplSanzioneSostCum;
	}

	public List getContinuazioni() {
		return mContinuazioni;
	}

	//
	// METODI SET()
	//

	public void setPenaComplessivaSanzioneSostitutivaCumulo(
			PenaComplessivaSanzioneSostitutivaCumuloModel aValore) {
		mPenaComplSanzioneSostCum = aValore;
	}

	public void setContinuazioni(List aValore) {
		mContinuazioni = aValore;
	}

}