package siap.siep.penacomplessiva.model;

import f3b.model.GenericModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;

public class PenaComplessivaSanzioneSostitutivaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -895024333722285849L;
	private PenaComplessivaModel mPenaComplessiva;
	private SanzioneSostitutivaModel mSanzioneSostitutiva;

	public PenaComplessivaSanzioneSostitutivaModel() {
		mPenaComplessiva = null;
		mSanzioneSostitutiva = null;
	}

	public PenaComplessivaSanzioneSostitutivaModel(PenaComplessivaModel aPenaComplessivaModel,
			SanzioneSostitutivaModel aSanzioneSostitutivaModel) {
		mPenaComplessiva = aPenaComplessivaModel;
		mSanzioneSostitutiva = aSanzioneSostitutivaModel;
	}

	//
	// METODI GET()
	//
	public PenaComplessivaModel getPenaComplessiva() {
		return mPenaComplessiva;
	}

	public SanzioneSostitutivaModel getSanzioneSostitutiva() {
		return mSanzioneSostitutiva;
	}

	//
	// METODI SET()
	//
	public void setPenaComplessiva(PenaComplessivaModel aValore) {
		mPenaComplessiva = aValore;
	}

	public void setSanzioneSostitutiva(SanzioneSostitutivaModel aValore) {
		mSanzioneSostitutiva = aValore;
	}
}