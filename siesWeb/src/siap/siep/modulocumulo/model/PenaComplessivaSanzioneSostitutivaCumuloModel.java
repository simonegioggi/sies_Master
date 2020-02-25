package siap.siep.modulocumulo.model;

import f3b.model.GenericModel;

public class PenaComplessivaSanzioneSostitutivaCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 7888018834238988615L;
	private PenaComplessivaCumuloModel mPenaComplessivaCum;
	private SanzioneSostitutivaCumuloModel mSanzioneSostitutivaCum;

	public PenaComplessivaSanzioneSostitutivaCumuloModel() {
		mPenaComplessivaCum = null;
		mSanzioneSostitutivaCum = null;
	}

	public PenaComplessivaSanzioneSostitutivaCumuloModel(PenaComplessivaCumuloModel aPenaComplessivaModel,
			SanzioneSostitutivaCumuloModel aSanzioneSostitutivaModel) {
		mPenaComplessivaCum = aPenaComplessivaModel;
		mSanzioneSostitutivaCum = aSanzioneSostitutivaModel;
	}

	//
	// METODI GET()
	//
	public PenaComplessivaCumuloModel getPenaComplessivaCumulo() {
		return mPenaComplessivaCum;
	}

	public SanzioneSostitutivaCumuloModel getSanzioneSostitutivaCumulo() {
		return mSanzioneSostitutivaCum;
	}

	//
	// METODI SET()
	//
	public void setPenaComplessivaCumulo(PenaComplessivaCumuloModel aValore) {
		mPenaComplessivaCum = aValore;
	}

	public void setSanzioneSostitutivaCumulo(SanzioneSostitutivaCumuloModel aValore) {
		mSanzioneSostitutivaCum = aValore;
	}
}