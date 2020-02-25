package siap.sius.sanzionesostitutiva.model;

import f3b.model.GenericModel;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;

public class PeriodoEsecuzioneSanzioneModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -4659891840829251478L;
	private PeriodoAltraSanzioneModel mPeriodoAltraSanzione;
	private EsecuzioneSanzioneSostitutivaModel mEsecuzioneSanzioneSostitutiva;

	public PeriodoAltraSanzioneModel getPeriodoAltraSanzione() {
		return mPeriodoAltraSanzione;
	}

	public EsecuzioneSanzioneSostitutivaModel getEsecuzioneSanzioneSostitutiva() {
		return mEsecuzioneSanzioneSostitutiva;
	}

	public void setPeriodoAltraSanzione(PeriodoAltraSanzioneModel aValore) {
		mPeriodoAltraSanzione = aValore;
	}

	public void setEsecuzioneSanzioneSostitutiva(EsecuzioneSanzioneSostitutivaModel aValore) {
		mEsecuzioneSanzioneSostitutiva = aValore;
	}

}
