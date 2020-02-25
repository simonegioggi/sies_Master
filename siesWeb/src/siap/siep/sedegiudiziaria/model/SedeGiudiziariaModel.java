package siap.siep.sedegiudiziaria.model;

/**
* <p>Title: SedeGiudiziariaModel</p>
* <p>Description: Classe Model che rappresenta il SedeGiudiziaria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

import f3b.model.GenericModel;

public class SedeGiudiziariaModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -183024839513364840L;

	private String mCodSedeGiudiziaria;
	private String mDescrSedeGiudiziaria;
	private String mDescrizione;
	private Date mDataCaricamentoRege;
	private String mCodComune;
	private String mDescrComune;

	// COSTRUTTORE DI DEFAULT
	public SedeGiudiziariaModel() {
		this.mCodSedeGiudiziaria = "";
		this.mDescrSedeGiudiziaria = "";
		this.mDescrizione = "";
		this.mDataCaricamentoRege = null;
		this.mCodComune = "";
		this.mDescrComune = "";
	}

	// COSTRUTTORE DI COPIA
	public SedeGiudiziariaModel(SedeGiudiziariaModel aModel) {
		this.mCodSedeGiudiziaria = aModel.mCodSedeGiudiziaria;
		this.mDescrSedeGiudiziaria = aModel.mDescrSedeGiudiziaria;
		this.mDescrizione = aModel.mDescrizione;
		this.mDataCaricamentoRege = aModel.mDataCaricamentoRege;
		this.mCodComune = aModel.mCodComune;
		this.mDescrComune = aModel.mDescrComune;
	}

	// COSTRUTTORE MODEL
	public SedeGiudiziariaModel(String aCodSedeGiudiziaria, String aDescrSedeGiudiziaria, String aDescrizione,
			Date aDataCaricamentoRege, String aCodComune, String aDescrComune) {
		this.mCodSedeGiudiziaria = aCodSedeGiudiziaria;
		this.mDescrSedeGiudiziaria = aDescrSedeGiudiziaria;
		this.mDescrizione = aDescrizione;
		this.mDataCaricamentoRege = aDataCaricamentoRege;
		this.mCodComune = aCodComune;
		this.mDescrComune = aDescrComune;
	}

	//
	// METODI GET()
	//

	public String getCodSedeGiudiziaria() {
		return mCodSedeGiudiziaria;
	}

	public String getDescrSedeGiudiziaria() {
		return mDescrSedeGiudiziaria;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	public Date getDataCaricamentoRege() {
		return mDataCaricamentoRege;
	}

	public String getCodComune() {
		return mCodComune;
	}

	public String getDescrComune() {
		return mDescrComune;
	}

	//
	// METODI SET()
	//

	public void setCodSedeGiudiziaria(String aValore) {
		mCodSedeGiudiziaria = aValore;
	}

	public void setDescrSedeGiudiziaria(String aValore) {
		mDescrSedeGiudiziaria = aValore;
	}

	public void setDescrizione(String aValore) {
		mDescrizione = aValore;
	}

	public void setDataCaricamentoRege(Date aValore) {
		mDataCaricamentoRege = aValore;
	}

	public void setCodComune(String aValore) {
		mCodComune = aValore;
	}

	public void setDescrComune(String aValore) {
		mDescrComune = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mCodSedeGiudiziaria + " - " + mDescrSedeGiudiziaria + " - " + mDescrizione + " - "
				+ mDataCaricamentoRege + " - " + mCodComune;

		return lStr;
	}

}