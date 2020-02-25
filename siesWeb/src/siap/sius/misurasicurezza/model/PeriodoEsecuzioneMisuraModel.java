package siap.sius.misurasicurezza.model;

import f3b.model.GenericModel;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;

public class PeriodoEsecuzioneMisuraModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 113538965876942345L;
	private PeriodoAltraMisuraModel mPeriodoAltraMisura;
	private EsecuzioneMisuraSicurezzaModel mEsecuzioneMisuraSicurezza;

	public PeriodoAltraMisuraModel getPeriodoAltraMisura() {
		return mPeriodoAltraMisura;
	}

	public EsecuzioneMisuraSicurezzaModel getEsecuzioneMisuraSicurezza() {
		return mEsecuzioneMisuraSicurezza;
	}

	public void setPeriodoAltraMisura(PeriodoAltraMisuraModel aValore) {
		mPeriodoAltraMisura = aValore;
	}

	public void setEsecuzioneMisuraSicurezza(EsecuzioneMisuraSicurezzaModel aValore) {
		mEsecuzioneMisuraSicurezza = aValore;
	}

}
