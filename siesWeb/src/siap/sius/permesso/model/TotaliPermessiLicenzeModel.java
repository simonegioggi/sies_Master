package siap.sius.permesso.model;

import f3b.model.GenericModel;

/**
 * PermessoModel - Classe Model che rappresenta tutti i dati di FascicoloSius, Generale Procedimento ed Evento
 * (legato al permesso / licenza)
 *
 * @version 1.0
 */
public class TotaliPermessiLicenzeModel extends GenericModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4659370071889813117L;

	int mNumPN = 0; // Permessi Necessità
	int mNumPP = 0; // Permessi Premio.
	int mNumPI = 0; // Permessi Internati.
	int mNumLC = 0; // Licenze Semilibertà.
	int mNumLI = 0; // Licenze Internati.
	// MEV_2023-35: aggiungo Licenza pene sostitutive (LP) E GESTITO NELLA CLASSE
	int mNumLP = 0;
	// MEV_2025-48: aggiunte 4 variabili per il motivo detenzione
	int mNumPP51 = 0;
	int mNumPP41bis = 0;
	int mNumPN51 = 0;
	int mNumPN41bis = 0;

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
		mNumLP = aModel.mNumLP;
		// MEV_2025-48: aggiunte 4 variabili per il motivo detenzione
		mNumPP51 = aModel.mNumPP51;
		mNumPP41bis = aModel.mNumPP41bis;
		mNumPN51 = aModel.mNumPN51;
		mNumPN41bis = aModel.mNumPN41bis;
	}

	// COSTRUTTORE MODEL
	public TotaliPermessiLicenzeModel(int aNumPN, int aNumPP, int aNumPI, int aNumLC, int aNumLI, int aNumLP,
			// MEV_2025-48: aggiunte 4 variabili per il motivo detenzione
			int aNumPP51, int aNumPP41bis, int aNumPN51, int aNumPN41bis) {

		mNumPN = aNumPN;
		mNumPP = aNumPP;
		mNumPI = aNumPI;
		mNumLC = aNumLC;
		mNumLI = aNumLI;
		mNumLP = aNumLP;
		// MEV_2025-48: aggiunte 4 variabili per il motivo detenzione
		mNumPP51 = aNumPP51;
		mNumPP41bis = aNumPP41bis;
		mNumPN51 = aNumPN51;
		mNumPN41bis = aNumPN41bis;
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

	public int getNumLP() {
		return mNumLP;
	}

	public int getNumTot() {
		return mNumPN + mNumPP + mNumPI + mNumLC + mNumLI + mNumLP;
	}

	// MEV_2025-48: aggiunte 4 variabili per il motivo detenzione
	public int getNumPP51() {
		return mNumPP51;
	}

	public int getNumPP41bis() {
		return mNumPP41bis;
	}

	public int getNumPN51() {
		return mNumPN51;
	}

	public int getNumPN41bis() {
		return mNumPN41bis;
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

	public void setNumLP(int aValore) {
		mNumLP = aValore;
	}

	// MEV_2025-48: aggiunte 4 variabili per il motivo detenzione
	public void setNumPP51(int aValore) {
		mNumPP51 = aValore;
	}

	public void setNumPP41bis(int aValore) {
		mNumPP41bis = aValore;
	}

	public void setNumPN51(int aValore) {
		mNumPN51 = aValore;
	}

	public void setNumPN41bis(int aValore) {
		mNumPN41bis = aValore;
	}

}