package siap.sico.utente.model;

/**
* <p>Title: DatiOperazioneModel</p>
* <p>
*   Description: Classe Model contenente
*   i dati per identificare l'autore di una data operazione
* </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;

public class DatiOperazioneModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1070614800172078788L;

	private String mCodOperatore;
	private Date mData;
	private String mCodUfficio;
	private String mDescrUfficio;

	// COSTRUTTORE DI DEFAULT
	public DatiOperazioneModel() {
		this.mCodOperatore = "";
		this.mData = null;
		this.mCodUfficio = "";
		this.mDescrUfficio = "";
	}

	// COSTRUTTORE MODEL
	public DatiOperazioneModel(String aCodOperatore, Date aData, String aCodUfficio, String aDescrUfficio) {
		this.mCodOperatore = aCodOperatore;
		this.mData = aData;
		this.mCodUfficio = aCodUfficio;
		this.mDescrUfficio = aDescrUfficio;
	}

	//
	// METODI GET()
	//

	public String getCodOperatore() {
		return mCodOperatore;
	}

	public Date getData() {
		return mData;
	}

	public String getCodUfficio() {
		return mCodUfficio;
	}

	public String getDescrUfficio() {
		return mDescrUfficio;
	}

	//
	// METODI SET()
	//

	public void setCodOperatore(String aValore) {
		mCodOperatore = aValore;
	}

	public void setData(Date aValore) {
		mData = aValore;
	}

	public void setCodUfficio(String aValore) {
		mCodUfficio = aValore;
	}

	public void setDescrUfficio(String aValore) {
		mDescrUfficio = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mCodOperatore + " - " + mData + " - " + mCodUfficio + " - " + mDescrUfficio;

		return lStr;
	}

}