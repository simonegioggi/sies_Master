package siap.sius.depositodecreto.model;

import siap.sius.fascicolo.model.FascicoloSiusModel;
import f3b.model.GenericModel;

public class DepositoDecretoFascicoloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2322263456834873924L;

	private DepositoDecretoModel mDepositoDecreto;
	private FascicoloSiusModel mFascicolo;

	// COSTRUTTORE DI CLASSE.
	public DepositoDecretoFascicoloModel() {
		mDepositoDecreto = new DepositoDecretoModel();
		mFascicolo = new FascicoloSiusModel();
	}

	// COSTRTTORE DI CLASSE CON PARAMETRI.
	public DepositoDecretoFascicoloModel(DepositoDecretoModel aDepositoDecreto,
			FascicoloSiusModel aFascicolo) {
		this.mDepositoDecreto = aDepositoDecreto;
		this.mFascicolo = aFascicolo;
	}

	public DepositoDecretoModel getDepositoDecreto() {
		return this.mDepositoDecreto;
	}

	public FascicoloSiusModel getFascicolo() {
		return this.mFascicolo;
	}

	public void setDepositoDecreto(DepositoDecretoModel aValue) {
		this.mDepositoDecreto = aValue;
	}

	public void setFascicolo(FascicoloSiusModel aValue) {
		this.mFascicolo = aValue;
	}

}