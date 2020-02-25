package siap.siep.posizione.model;

import f3b.model.GenericModel;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.model.MisuraCautelareModel;

public class PosizioneGiuridicaLuogoDetenzioneAltraCausaModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 6382853788832437157L;
	private PosizioneGiuridicaLuogoDetenzioneModel mPosizioneGiuridicaLuogoDetenzioneModel;
	private AltraCausaModel mAltraCausa;
	private MisuraCautelareModel mMisuraCautelare;

	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel() {
		mPosizioneGiuridicaLuogoDetenzioneModel = new PosizioneGiuridicaLuogoDetenzioneModel();
		mAltraCausa = null;
	}

	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(PosizioneGiuridicaModel aPosizioneGiuridica,
			LuogoDetenzioneModel aLuogoDetenzione, AltraCausaModel aAltraCausa) {
		mPosizioneGiuridicaLuogoDetenzioneModel = new PosizioneGiuridicaLuogoDetenzioneModel(
				aPosizioneGiuridica, aLuogoDetenzione);
		mAltraCausa = aAltraCausa;
	}

	//
	// METODI GET()
	//
	public PosizioneGiuridicaModel getPosizioneGiuridica() {
		return mPosizioneGiuridicaLuogoDetenzioneModel.getPosizioneGiuridica();
	}

	public LuogoDetenzioneModel getLuogoDetenzione() {
		return mPosizioneGiuridicaLuogoDetenzioneModel.getLuogoDetenzione();
	}

	public AltraCausaModel getAltraCausa() {
		return mAltraCausa;
	}

	public MisuraCautelareModel getMisuraCautelare() {
		return mMisuraCautelare;
	}

	//
	// METODI SET()
	//
	public void setPosizioneGiuridica(PosizioneGiuridicaModel aValore) {
		mPosizioneGiuridicaLuogoDetenzioneModel.setPosizioneGiuridica(aValore);
	}

	public void setLuogoDetenzione(LuogoDetenzioneModel aValore) {
		mPosizioneGiuridicaLuogoDetenzioneModel.setLuogoDetenzione(aValore);
	}

	public void setAltraCausa(AltraCausaModel aValore) {
		mAltraCausa = aValore;
	}

	public void setMisuraCautelare(MisuraCautelareModel misuraCautelare) {
		mMisuraCautelare = misuraCautelare;
	}
}