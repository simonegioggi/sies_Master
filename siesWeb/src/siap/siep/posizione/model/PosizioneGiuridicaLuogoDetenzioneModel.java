package siap.siep.posizione.model;

import f3b.model.GenericModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;

public class PosizioneGiuridicaLuogoDetenzioneModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4119616959550750032L;
	private PosizioneGiuridicaModel mPosizioneGiuridica;
	private LuogoDetenzioneModel mLuogoDetenzione;

	public PosizioneGiuridicaLuogoDetenzioneModel() {
		mPosizioneGiuridica = null;
		mLuogoDetenzione = null;
	}

	public PosizioneGiuridicaLuogoDetenzioneModel(PosizioneGiuridicaModel aPosizioneGiuridica,
			LuogoDetenzioneModel aLuogoDetenzione) {
		mPosizioneGiuridica = aPosizioneGiuridica;
		mLuogoDetenzione = aLuogoDetenzione;
	}

	//
	// METODI GET()
	//
	public PosizioneGiuridicaModel getPosizioneGiuridica() {
		return mPosizioneGiuridica;
	}

	public LuogoDetenzioneModel getLuogoDetenzione() {
		return mLuogoDetenzione;
	}

	//
	// METODI SET()
	//
	public void setPosizioneGiuridica(PosizioneGiuridicaModel aValore) {
		mPosizioneGiuridica = aValore;
	}

	public void setLuogoDetenzione(LuogoDetenzioneModel aValore) {
		mLuogoDetenzione = aValore;
	}
}