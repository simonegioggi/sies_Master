package siap.sico.decodifiche.model;

/**
 * <p>Title: OggettiModel</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @author unascribed
 * @version 1.0
 */

import f3b.model.GenericModel;

public class OggettiModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -664426985898456599L;

	private String mCodOggetto;
	private String mDescOggetto;
	private String mCodContenuto;
	private String mDescContenuto;
	private String mCodDettaglio;
	private String mDescDettaglio;
	private String mAbbrOggetto; // 13/09/2004

	// COSTRUTTORE DI DEFAULT
	public OggettiModel() {
		this.mCodOggetto = "";
		this.mDescOggetto = "";
		this.mCodContenuto = "";
		this.mDescContenuto = "";
		this.mCodDettaglio = "";
		this.mDescDettaglio = "";
		this.mAbbrOggetto = "";
	}

	// COSTRUTTORE DI COPIA
	public OggettiModel(OggettiModel aModel) {
		this.mCodOggetto = aModel.mCodOggetto;
		this.mDescOggetto = aModel.mDescOggetto;
		this.mCodContenuto = aModel.mCodContenuto;
		this.mDescContenuto = aModel.mDescContenuto;
		this.mCodDettaglio = aModel.mCodDettaglio;
		this.mDescDettaglio = aModel.mDescDettaglio;
		this.mAbbrOggetto = aModel.mAbbrOggetto;
	}

	// COSTRUTTORE MODEL
	public OggettiModel(String aCodOggetto, String aDescOggetto, String aCodContenuto, String aDescContenuto,
			String aCodDettaglio, String aDescDettaglio, String aAbbrOggetto) {
		this.mCodOggetto = aCodOggetto;
		this.mDescOggetto = aDescOggetto;
		this.mCodContenuto = aCodContenuto;
		this.mDescContenuto = aDescContenuto;
		this.mCodDettaglio = aCodDettaglio;
		this.mDescDettaglio = aDescDettaglio;
		this.mAbbrOggetto = aAbbrOggetto;
	}

	//
	// METODI GET()
	//
	public String getCodOggetto() {
		return mCodOggetto;
	}

	public String getDescOggetto() {
		return mDescOggetto;
	}

	public String getCodContenuto() {
		return mCodContenuto;
	}

	public String getDescContenuto() {
		return mDescContenuto;
	}

	public String getCodDettaglio() {
		return mCodDettaglio;
	}

	public String getDescDettaglio() {
		return mDescDettaglio;
	}

	public String getAbbrOggetto() {
		return mAbbrOggetto;
	}

	//
	// METODI SET()
	//
	public void setCodOggetto(String aValore) {
		this.mCodOggetto = aValore;
	}

	public void setDescOggetto(String aValore) {
		this.mDescOggetto = aValore;
	}

	public void setCodContenuto(String aValore) {
		this.mCodContenuto = aValore;
	}

	public void setDescContenuto(String aValore) {
		this.mDescContenuto = aValore;
	}

	public void setCodDettaglio(String aValore) {
		this.mCodDettaglio = aValore;
	}

	public void setDescDettaglio(String aValore) {
		this.mDescDettaglio = aValore;
	}

	public void setAbbrOggetto(String aValore) {
		this.mAbbrOggetto = aValore;
	}

	public String toString() {
		String lToString = "CodOggetto = " + this.mCodOggetto + " - " + "DescOggetto = " + this.mDescOggetto
				+ " - " + "CodContenuto = " + this.mCodContenuto + " - " + "DescContenuto = "
				+ this.mDescContenuto + " - " + "CodDettaglio = " + this.mCodDettaglio + " - "
				+ "DescDettaglio = " + this.mDescDettaglio + " - " + "AbbrOggetto = " + this.mAbbrOggetto;
		return lToString;
	}

}