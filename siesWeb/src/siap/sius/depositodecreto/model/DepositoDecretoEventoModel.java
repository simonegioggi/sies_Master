package siap.sius.depositodecreto.model;

import siap.sico.evento.model.EventoModel;
import f3b.model.GenericModel;

public class DepositoDecretoEventoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3051070228891351264L;

	private DepositoDecretoModel mDepositoDecreto;
	private EventoModel mEvento;

	// COSTRUTTORE DI CLASSE.
	public DepositoDecretoEventoModel() {
		mDepositoDecreto = new DepositoDecretoModel();
		mEvento = new EventoModel();
	}

	// COSTRTTORE DI CLASSE CON PARAMETRI.
	public DepositoDecretoEventoModel(DepositoDecretoModel aDepositoDecreto, EventoModel aEvento) {
		this.mDepositoDecreto = aDepositoDecreto;
		this.mEvento = aEvento;
	}

	public DepositoDecretoModel getDepositoDecreto() {
		return this.mDepositoDecreto;
	}

	public EventoModel getEvento() {
		return this.mEvento;
	}

	public void setDepositoDecreto(DepositoDecretoModel aValue) {
		this.mDepositoDecreto = aValue;
	}

	public void setEvento(EventoModel aValue) {
		this.mEvento = aValue;
	}

}