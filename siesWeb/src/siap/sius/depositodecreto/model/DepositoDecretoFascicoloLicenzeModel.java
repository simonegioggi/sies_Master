package siap.sius.depositodecreto.model;

import java.util.Vector;

import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DepositoDecretoFascicoloLicenzeModel extends DepositoDecretoFascicoloModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8730691233194968124L;

	LicenzaLibAnticipataModel[] mLicenze;

	// COSTRUTTORE DI CLASSE.
	public DepositoDecretoFascicoloLicenzeModel() {
		super();
		mLicenze = null;

	}

	public LicenzaLibAnticipataModel[] getLicenze() {
		return this.mLicenze;
	}

	public void setLicenze(LicenzaLibAnticipataModel[] aLicenze) {
		mLicenze = aLicenze;
	}

	// COSTRTTORE DI CLASSE CON PARAMETRI.
	public DepositoDecretoFascicoloLicenzeModel(DepositoDecretoModel aDepositoDecreto,
			FascicoloSiusModel aFascicolo, Vector aLicenze) {
		super(aDepositoDecreto, aFascicolo);
		mLicenze = (LicenzaLibAnticipataModel[]) aLicenze.toArray(new LicenzaLibAnticipataModel[0]);

	}

}