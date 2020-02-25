package siap.sius.depositodecreto.model;

import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import f3b.model.GenericModel;

public class DepositoDecretoMotivazioniModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7085751471315545150L;

	private DepositoDecretoModel mDepositoDecreto;
	private MotivazioneDecretoModel[] mMotivazioniDecreto;

	public DepositoDecretoMotivazioniModel() {
		mDepositoDecreto = null;
		mMotivazioniDecreto = null;
	}

	public DepositoDecretoMotivazioniModel(DepositoDecretoModel aDepositoDecreto,
			MotivazioneDecretoModel[] aMotivazioniDecreto) {
		mDepositoDecreto = aDepositoDecreto;
		mMotivazioniDecreto = aMotivazioniDecreto;
	}

	// Metodi di set
	public void setMotivazioniDecreto(MotivazioneDecretoModel[] aMotivazioniDecreto) {
		mMotivazioniDecreto = aMotivazioniDecreto;
	}

	public void setDepositoDecreto(DepositoDecretoModel aDepositoDecreto) {
		mDepositoDecreto = aDepositoDecreto;
	}

	// Metodi di get
	public MotivazioneDecretoModel[] getMotivazioniDecreto() {
		return mMotivazioniDecreto;
	}

	public DepositoDecretoModel getDepositoDecreto() {
		return mDepositoDecreto;
	}

}