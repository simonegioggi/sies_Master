package siap.siep.statoesecuzione.model;

import java.util.Vector;

import f3b.model.GenericModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;

/**
 * Model utilizzato dallo stato di esecuzione per trasportare i dati relativi alle LA da visualizzare sui
 * template
 *
 *
 * @author Giselda De Vita
 *
 */
public class LAModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -3539296288534869829L;
	private LicenzaLibAnticipataModel mLAModel = null;
	private Vector<PeriodoLAModel> mPeriodiLA = null;

	// Attenzione il vettore lStrPeriodiLA viene valorizzato con UN SOLO elemento di tipo
	// PeriodoLAModel che di fatto è una Stringa. Contiene quindi la stringa
	// composta con tutti i periodi collegati alla LA.
	// es dal 01/01/20011 al 02/02/2011, dal 03/03/2011 al 04/04/2011.

	// Peggio ancora!!!!!!! nella realtà il modulo che carica il dato (StatoEsecuzioneLA)
	// compone la stringa con i periodi di tutte le LA concesse sull'evento e non
	// solo su quelle della property mLAModel. Tale property è inoltre non significativa
	// perchè contiene solo l'ultimo record LA associato all'evento. In caso di LA
	// concesse potrebbero essere piu' di una, uno per ogni per ogni 45gg

	public LicenzaLibAnticipataModel getLAModel() {
		return mLAModel;
	}

	public void setLAModel(LicenzaLibAnticipataModel model) {
		mLAModel = model;
	}

	public Vector<PeriodoLAModel> getPeriodiLA() {
		return mPeriodiLA;
	}

	public void setPeriodiLA(Vector<PeriodoLAModel> periodiLA) {
		mPeriodiLA = periodiLA;
	}

	@Override
	public String toString() {
		String lString = "";

		if (mLAModel != null)
			lString += "mLAModel.getId = " + mLAModel.getIdLicenzaLibanticipata() + "\n";
		else
			lString += "mLAModel is null \n";

		lString += " mPeriodiLA.size() = " + mPeriodiLA.size() + "\n";
		for (int i = 0; i < mPeriodiLA.size(); i++) {
			lString += "Descrizione = " + mPeriodiLA.elementAt(i).getDescrizione() + "\n";
		}

		return lString;
	}

}
