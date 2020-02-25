package siap.sico.ufficio.model;

import f3b.model.GenericModel;

public class UfficiProvvedimentoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3622510107189559954L;

	private String mCodUfficio;
	private String mCodTipoUfficio;
	private String mCodDistretto;
	private String mCodComune;

	private String mDescrTipoUfficio;
	private String mDescrComune;
	private String mChiaveAnnoSiep;
	private String mChiaveProgrSiep;

	// MEV_39 03/01/2018
	private String mFasSiepOrigine;

	// COSTRUTTORE DI DEFAULT
	public UfficiProvvedimentoModel() {
		this.mCodUfficio = "";
		this.mCodTipoUfficio = "";
		this.mCodDistretto = "";
		this.mCodComune = "";

		this.mDescrTipoUfficio = "";
		this.mDescrComune = "";
		this.mChiaveAnnoSiep = "";
		this.mChiaveProgrSiep = "";
		// MEV_39 03/01/2018
		this.mFasSiepOrigine = "";
	}

	// COSTRUTTORE DI COPIA
	public UfficiProvvedimentoModel(UfficiProvvedimentoModel aModel) {
		this.mCodUfficio = aModel.mCodUfficio;
		this.mCodTipoUfficio = aModel.mCodTipoUfficio;
		this.mCodDistretto = aModel.mCodDistretto;
		this.mCodComune = aModel.mCodComune;

		this.mDescrTipoUfficio = aModel.mDescrTipoUfficio;
		this.mDescrComune = aModel.mDescrComune;
		this.mChiaveAnnoSiep = aModel.mChiaveAnnoSiep;
		this.mChiaveProgrSiep = aModel.mChiaveProgrSiep;
		// MEV_39 03/01/2018
		this.mFasSiepOrigine = aModel.mFasSiepOrigine;

	}

	// COSTRUTTORE MODEL
	public UfficiProvvedimentoModel(String aCodUfficio, String aCodTipoUfficio, String aDescrTipoUfficio,
			String aCodDistretto, String aCodComune, String aDescrComune, String aChiaveAnnoSiep,
			String aChiaveProgrSiep,
			// MEV_39 03/01/2018
			String aFasSiepOrigine) {
		this.mCodUfficio = aCodUfficio;
		this.mCodTipoUfficio = aCodTipoUfficio;
		this.mCodDistretto = aCodDistretto;
		this.mCodComune = aCodComune;

		this.mDescrTipoUfficio = aDescrTipoUfficio;
		this.mDescrComune = aDescrComune;
		this.mChiaveAnnoSiep = aChiaveAnnoSiep;
		this.mChiaveProgrSiep = aChiaveProgrSiep;
		// MEV_39 03/01/2018
		this.mFasSiepOrigine = aFasSiepOrigine;
	}

	//
	// METODI GET()
	//
	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getCodTipoUfficio() {
		return mCodTipoUfficio;
	}

	public String getCodDistretto() {
		return mCodDistretto;
	}

	public String getCodComune() {
		return mCodComune;
	}

	public String getDescrTipoUfficio() {
		return mDescrTipoUfficio;
	}

	public String getDescrComune() {
		return mDescrComune;
	}

	public String getChiaveAnnoSiep() {
		return mChiaveAnnoSiep;
	}

	public String getChiaveProgrSiep() {
		return mChiaveProgrSiep;
	}

	// MEV_39 03/01/2018
	public String getFasSiepOrigine() {
		return mFasSiepOrigine;
	}

	//
	// METODI SET()
	//
	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setCodTipoUfficio(String aValore) {
		mCodTipoUfficio = aValore;
	}

	public void setCodDistretto(String aValore) {
		mCodDistretto = aValore;
	}

	public void setCodComune(String aValore) {
		mCodComune = aValore;
	}

	public void setDescrTipoUfficio(String aValore) {
		mDescrTipoUfficio = aValore;
	}

	public void setDescrComune(String aValore) {
		mDescrComune = aValore;
	}

	public void setChiaveAnnoSiep(String aValore) {
		mChiaveAnnoSiep = aValore;
	}

	public void setChiaveProgrSiep(String aValore) {
		mChiaveProgrSiep = aValore;
	}

	// MEV_39 03/01/2018
	public void setFasSiepOrigine(String aValore) {
		mFasSiepOrigine = aValore;
	}

	public String toString() {
		String lToString = this.mCodUfficio + " - " + this.mCodTipoUfficio + " - " + this.mDescrTipoUfficio
				+ " - " + this.mCodDistretto + " - " + this.mCodComune + " - " + this.mDescrComune + " - "
				+ this.mChiaveAnnoSiep + " - " + this.mChiaveProgrSiep + " - " +
				// MEV_39 03/01/2018
				this.mFasSiepOrigine;
		return lToString;
	}

}