package siap.siep.penacomplessiva.model;

import java.util.List;

import f3b.model.GenericModel;

@SuppressWarnings("rawtypes")
public class DettaglioPenaComplessivaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5145633079989489655L;

	private PenaComplessivaSanzioneSostitutivaModel mPenaComplSanzioneSost;
	private List mContinuazioni;

	public DettaglioPenaComplessivaModel() {
		mPenaComplSanzioneSost = null;
		mContinuazioni = null;
	}

	public DettaglioPenaComplessivaModel(PenaComplessivaSanzioneSostitutivaModel aPenaComplSanzioneSost,
			List aContinuazioni) {
		mPenaComplSanzioneSost = aPenaComplSanzioneSost;
		mContinuazioni = aContinuazioni;
	}

	//
	// METODI GET()
	//

	public PenaComplessivaSanzioneSostitutivaModel getPenaComplessivaSanzioneSostitutiva() {
		return mPenaComplSanzioneSost;
	}

	public List getContinuazioni() {
		return mContinuazioni;
	}

	//
	// METODI SET()
	//

	public void setPenaComplessivaSanzioneSostitutiva(PenaComplessivaSanzioneSostitutivaModel aValore) {
		mPenaComplSanzioneSost = aValore;
	}

	public void setContinuazioni(List aValore) {
		mContinuazioni = aValore;
	}

}