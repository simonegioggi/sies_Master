package siap.sico.libertaanticipata.model;

/**
* <p>Title: LicenzaLibanticipataModel</p>
* <p>Description: Classe Model che rappresenta il LicenzaLibanticipata</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.evento.model.EventoModel;
import siap.sius.permesso.model.EventoPermessoLicenzaModel;
import f3b.model.GenericModel;

public class LicenzaPeriodiLibAnticipataModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6431556363391388787L;

	private LicenzaLibAnticipataModel mLicenza;
	private PeriodoLibAnticipataModel[] mPeriodi;
	private EventoPermessoLicenzaModel[] mEventiPermLic;

	private EventoModel mEvento;

	// COSTRUTTORE DI DEFAULT
	public LicenzaPeriodiLibAnticipataModel() {
		mLicenza = null;
		mPeriodi = null;
		mEventiPermLic = null;
		mEvento = null;
	}

	// COSTRUTTORE DI COPIA
	public LicenzaPeriodiLibAnticipataModel(LicenzaPeriodiLibAnticipataModel aModel) {
		this.mLicenza = aModel.mLicenza;
		this.mPeriodi = aModel.mPeriodi;
		this.mEventiPermLic = aModel.mEventiPermLic;
		this.mEvento = aModel.mEvento;
	}

	// COSTRUTTORE MODEL
	public LicenzaPeriodiLibAnticipataModel(LicenzaLibAnticipataModel aLicenza,
			PeriodoLibAnticipataModel[] aPeriodi) {
		this.mLicenza = aLicenza;
		this.mPeriodi = aPeriodi;
	}

	// COSTRUTTORE MODEL
	public LicenzaPeriodiLibAnticipataModel(LicenzaLibAnticipataModel aLicenza,
			PeriodoLibAnticipataModel[] aPeriodi, EventoPermessoLicenzaModel[] aEventiPermLic) {
		this.mLicenza = aLicenza;
		this.mPeriodi = aPeriodi;
		this.mEventiPermLic = aEventiPermLic;
	}

	//
	// METODI GET()
	//
	public LicenzaLibAnticipataModel getLicenza() {
		return mLicenza;
	}

	public PeriodoLibAnticipataModel[] getPeriodi() {
		return mPeriodi;
	}

	public EventoPermessoLicenzaModel[] getEventiPermLic() {
		return mEventiPermLic;
	}

	public EventoModel getEvento() {
		return mEvento;
	}

	//
	// METODI SET()
	//
	public void setLicenza(LicenzaLibAnticipataModel aValore) {
		mLicenza = aValore;
	}

	public void setPeriodi(PeriodoLibAnticipataModel[] aValore) {
		mPeriodi = aValore;
	}

	public void setEventiPermLic(EventoPermessoLicenzaModel[] aValore) {
		mEventiPermLic = aValore;
	}

	public void setEvento(EventoModel aValore) {
		mEvento = aValore;
	}

	public String toString() {
		String lStr = new String("");

		if (mLicenza != null) {
			lStr = "Licenza" + " - " + "\n" + mLicenza;
			if (mPeriodi != null)
				for (int i = 0; i < mPeriodi.length; i++) {
					lStr += "\nPeriodo  " + i + "\n" + mPeriodi[i];
					lStr += "\n---------- ";
				}

			if (mEventiPermLic != null)
				for (int i = 0; i < mEventiPermLic.length; i++) {
					lStr += "\nEvento Permesso Licenza " + i + "\n" + mEventiPermLic[i];
					lStr += "\n---------- ";
				}
		}
		return lStr;
	}

}