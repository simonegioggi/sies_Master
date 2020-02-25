package siap.sius.permesso.model;

import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;

public class DepositoDecretoMotivazioniLicenzaModel extends DepositoDecretoEventoMotivazioniModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1180703565483882662L;

	private LicenzaLibAnticipataModel mLicenza = null;

	// COSTRUTTORE DI CLASSE.
	public DepositoDecretoMotivazioniLicenzaModel() {
		super();
		mLicenza = null;
	}

	public LicenzaLibAnticipataModel getLicenza() {
		return this.mLicenza;
	}

	public void setLicenza(LicenzaLibAnticipataModel aLicenza) {
		mLicenza = aLicenza;
	}

	// COSTRTTORE DI CLASSE CON PARAMETRI.
	public DepositoDecretoMotivazioniLicenzaModel(DepositoDecretoModel aDepositoDecreto,
			MotivazioneDecretoModel[] aMotivazioni, LicenzaLibAnticipataModel aLicenza) {
		super(aDepositoDecreto, aMotivazioni, null);
		mLicenza = aLicenza;
	}

	public DepositoDecretoMotivazioniLicenzaModel(DepositoDecretoModel aDepositoDecreto,
			MotivazioneDecretoModel[] aMotivazioni, EventoModel aEvento, LicenzaLibAnticipataModel aLicenza) {
		super(aDepositoDecreto, aMotivazioni, aEvento);
		mLicenza = aLicenza;
	}

}