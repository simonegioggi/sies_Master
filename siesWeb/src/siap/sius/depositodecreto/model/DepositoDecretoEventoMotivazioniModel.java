package siap.sius.depositodecreto.model;

import siap.sico.evento.model.EventoModel;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import f3b.model.GenericModel;

public class DepositoDecretoEventoMotivazioniModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3183654337553530926L;

	private DepositoDecretoModel mDepositoDecreto;
	private MotivazioneDecretoModel[] mMotivazioniDecreto;
	private EventoModel mEvento;

	public DepositoDecretoEventoMotivazioniModel() {
		mDepositoDecreto = null;
		mMotivazioniDecreto = null;
		mEvento = null;
	}

	public DepositoDecretoEventoMotivazioniModel(DepositoDecretoModel aDepositoDecreto,
			MotivazioneDecretoModel[] aMotivazioniDecreto, EventoModel aEvento) {
		mDepositoDecreto = aDepositoDecreto;
		mMotivazioniDecreto = aMotivazioniDecreto;
		mEvento = aEvento;
	}

	// Metodi di set
	public void setMotivazioniDecreto(MotivazioneDecretoModel[] aMotivazioniDecreto) {
		mMotivazioniDecreto = aMotivazioniDecreto;
	}

	public void setDepositoDecreto(DepositoDecretoModel aDepositoDecreto) {
		mDepositoDecreto = aDepositoDecreto;
	}

	public void setEvento(EventoModel aEvento) {
		mEvento = aEvento;
	}

	// Metodi di get
	public MotivazioneDecretoModel[] getMotivazioniDecreto() {
		return mMotivazioniDecreto;
	}

	public DepositoDecretoModel getDepositoDecreto() {
		return mDepositoDecreto;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

}