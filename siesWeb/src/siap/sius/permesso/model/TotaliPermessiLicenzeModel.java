package siap.sius.permesso.model;

/**
* <p>Title: PermessoModel</p>
* <p>Description: Classe Model che rappresenta tutti i dati di FascicoloSius, Generale Procedimento ed Evento (legato al permesso) </p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.model.GenericModel;

public class TotaliPermessiLicenzeModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4659370071889813117L;

	int mNumPN = 0; // Permessi Necessità
	int mNumPP = 0; // Permessi Premio.
	int mNumPI = 0; // Permessi Internati.
	int mNumLC = 0; // Licenze Semilibertà.
	int mNumLI = 0; // Licenze Internati.

	// COSTRUTTORE DI DEFAULT
	public TotaliPermessiLicenzeModel() {
	}

	// COSTRUTTORE DI COPIA
	public TotaliPermessiLicenzeModel(TotaliPermessiLicenzeModel aModel) {
		mNumPN = aModel.mNumPN;
		mNumPP = aModel.mNumPP;
		mNumPI = aModel.mNumPI;
		mNumLC = aModel.mNumLC;
		mNumLI = aModel.mNumLI;
	}

	// COSTRUTTORE MODEL
	public TotaliPermessiLicenzeModel(int aNumPN, int aNumPP, int aNumPI, int aNumLC, int aNumLI) {
		mNumPN = aNumPN;
		mNumPP = aNumPP;
		mNumPI = aNumPI;
		mNumLC = aNumLC;
		mNumLI = aNumLI;
	}

	//
	// METODI GET()
	//
	public int getNumPN() {
		return mNumPN;
	}

	public int getNumPP() {
		return mNumPP;
	}

	public int getNumPI() {
		return mNumPI;
	}

	public int getNumLC() {
		return mNumLC;
	}

	public int getNumLI() {
		return mNumLI;
	}

	public int getNumTot() {
		return mNumPN + mNumPP + mNumPI + mNumLC + mNumLI;
	}

	//
	// METODI SET()
	//
	public void setNumPN(int aValore) {
		mNumPN = aValore;
	}

	public void setNumPP(int aValore) {
		mNumPP = aValore;
	}

	public void setNumPI(int aValore) {
		mNumPI = aValore;
	}

	public void setNumLC(int aValore) {
		mNumLC = aValore;
	}

	public void setNumLI(int aValore) {
		mNumLI = aValore;
	}

}