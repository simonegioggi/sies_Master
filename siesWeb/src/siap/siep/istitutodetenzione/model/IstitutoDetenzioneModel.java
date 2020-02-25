package siap.siep.istitutodetenzione.model;

/**
* <p>Title: IstitutoDetenzioneModel</p>
* <p>Description: Classe Model che rappresenta il IstitutoDetenzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.model.GenericModel;

public class IstitutoDetenzioneModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = -6444959755380709453L;
	private String mIdIstitutoDetenzione;
	private String mCodTipoIstituto;
	private String mDescrTipoIstituto;
	private String mCodComune;
	private String mDescrComune;
	private String mCodProvincia;
	private String mDescrProvincia;
	private String mIndirizzo;
	private String mDescrizione;
	private String mNote;
	private String mCodDistretto;

	// COSTRUTTORE DI DEFAULT
	public IstitutoDetenzioneModel() {
		this.mIdIstitutoDetenzione = "";
		this.mCodTipoIstituto = "";
		this.mDescrTipoIstituto = "";
		this.mCodComune = "";
		this.mDescrComune = "";
		this.mCodProvincia = "";
		this.mDescrProvincia = "";
		this.mIndirizzo = "";
		this.mDescrizione = "";
		this.mNote = "";
		this.mCodDistretto = "";
	}

	// COSTRUTTORE DI COPIA
	public IstitutoDetenzioneModel(IstitutoDetenzioneModel aModel) {
		this.mIdIstitutoDetenzione = aModel.mIdIstitutoDetenzione;
		this.mCodTipoIstituto = aModel.mCodTipoIstituto;
		this.mDescrTipoIstituto = aModel.mDescrTipoIstituto;
		this.mCodComune = aModel.mCodComune;
		this.mDescrComune = aModel.mDescrComune;
		this.mCodProvincia = aModel.mCodProvincia;
		this.mDescrProvincia = aModel.mDescrProvincia;
		this.mIndirizzo = aModel.mIndirizzo;
		this.mDescrizione = aModel.mDescrizione;
		this.mNote = aModel.mNote;
		this.mCodDistretto = aModel.mCodDistretto;
	}

	// COSTRUTTORE MODEL
	public IstitutoDetenzioneModel(String aIdIstitutoDetenzione, String aCodTipoIstituto,
			String aDescrTipoIstituto, String aCodComune, String aDescrComune, String aCodProvincia,
			String aDescrProvincia, String aIndirizzo, String aDescrizione, String aNote,
			String aCodDistretto) {
		this.mIdIstitutoDetenzione = aIdIstitutoDetenzione;
		this.mCodTipoIstituto = aCodTipoIstituto;
		this.mDescrTipoIstituto = aDescrTipoIstituto;
		this.mCodComune = aCodComune;
		this.mDescrComune = aDescrComune;
		this.mCodProvincia = aCodProvincia;
		this.mDescrProvincia = aDescrProvincia;
		this.mIndirizzo = aIndirizzo;
		this.mDescrizione = aDescrizione;
		this.mNote = aNote;
		this.mCodDistretto = aCodDistretto;
	}

	//
	// METODI GET()
	//

	public String getIdIstitutoDetenzione() {
		return mIdIstitutoDetenzione;
	}

	public String getCodTipoIstituto() {
		return mCodTipoIstituto;
	}

	public String getDescrTipoIstituto() {
		return mDescrTipoIstituto;
	}

	public String getCodComune() {
		return mCodComune;
	}

	public String getDescrComune() {
		return mDescrComune;
	}

	public String getCodProvincia() {
		return mCodProvincia;
	}

	public String getDescrProvincia() {
		return mDescrProvincia;
	}

	public String getIndirizzo() {
		return mIndirizzo;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodDistretto() {
		return mCodDistretto;
	}

	//
	// METODI SET()
	//

	public void setIdIstitutoDetenzione(String aValore) {
		mIdIstitutoDetenzione = aValore;
	}

	public void setCodTipoIstituto(String aValore) {
		mCodTipoIstituto = aValore;
	}

	public void setDescrTipoIstituto(String aValore) {
		mDescrTipoIstituto = aValore;
	}

	public void setCodComune(String aValore) {
		mCodComune = aValore;
	}

	public void setDescrComune(String aValore) {
		mDescrComune = aValore;
	}

	public void setCodProvincia(String aValore) {
		mCodProvincia = aValore;
	}

	public void setDescrProvincia(String aValore) {
		mDescrProvincia = aValore;
	}

	public void setIndirizzo(String aValore) {
		mIndirizzo = aValore;
	}

	public void setDescrizione(String aValore) {
		mDescrizione = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodDistretto(String aValore) {
		mCodDistretto = aValore;
	}

	public String getDescrizioneIstitutoPerVisualizzazione() {
		String lDescrizione = "";
		lDescrizione += mDescrTipoIstituto + " di " + mDescrizione + " - " + mIndirizzo;
		return lDescrizione;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdIstitutoDetenzione + " - " + mCodTipoIstituto + " - " + mDescrTipoIstituto + " - "
				+ mCodComune + " - " + mDescrComune + " - " + mCodProvincia + " - " + mDescrProvincia + " - "
				+ mIndirizzo + " - " + mDescrizione + " - " + mNote + " - " + mCodDistretto;

		return lStr;
	}
}
