package siap.siep.agdgfascicolosiep.model;

/**
* <p>Title: AgdgFascicoloSiepModel</p>
* <p>Description: Classe Model che rappresenta il AgdgFascicoloSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import f3b.model.GenericModel;

public class AgdgFascicoloSiepModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 4637252625477238851L;

	private BigDecimal mIdAgdgFascicoloSiep;
	private BigDecimal mAgdgIdAltrigradigiudizio;
	private BigDecimal mFasSieIdFascicoloSiep;
	private AltriGradiGiudizioModel mAltriGradiGiudizioModel;

	// COSTRUTTORE DI DEFAULT
	public AgdgFascicoloSiepModel() {
		this.mIdAgdgFascicoloSiep = null;
		this.mAgdgIdAltrigradigiudizio = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mAltriGradiGiudizioModel = null;
	}

	// COSTRUTTORE DI COPIA
	public AgdgFascicoloSiepModel(AgdgFascicoloSiepModel aModel) {
		this.mIdAgdgFascicoloSiep = aModel.mIdAgdgFascicoloSiep;
		this.mAgdgIdAltrigradigiudizio = aModel.mAgdgIdAltrigradigiudizio;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mAltriGradiGiudizioModel = aModel.mAltriGradiGiudizioModel;
	}

	// COSTRUTTORE MODEL
	public AgdgFascicoloSiepModel(BigDecimal aIdAgdgFascicoloSiep, BigDecimal aAgdgIdAltrigradigiudizio,
			BigDecimal aFasSieIdFascicoloSiep, AltriGradiGiudizioModel aAltriGradiGiudizioModel) {
		this.mIdAgdgFascicoloSiep = aIdAgdgFascicoloSiep;
		this.mAgdgIdAltrigradigiudizio = aAgdgIdAltrigradigiudizio;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mAltriGradiGiudizioModel = aAltriGradiGiudizioModel;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdAgdgFascicoloSiep() {
		return mIdAgdgFascicoloSiep;
	}

	public BigDecimal getAgdgIdAltrigradigiudizio() {
		return mAgdgIdAltrigradigiudizio;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public AltriGradiGiudizioModel getAltriGradiGiudizioModel() {
		return mAltriGradiGiudizioModel;
	}

	//
	// METODI SET()
	//

	public void setIdAgdgFascicoloSiep(BigDecimal aValore) {
		mIdAgdgFascicoloSiep = aValore;
	}

	public void setAgdgIdAltrigradigiudizio(BigDecimal aValore) {
		mAgdgIdAltrigradigiudizio = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setAltriGradiGiudizioModel(AltriGradiGiudizioModel aValore) {
		mAltriGradiGiudizioModel = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "" + mIdAgdgFascicoloSiep + " - " + mAgdgIdAltrigradigiudizio + " - " + mFasSieIdFascicoloSiep;

		return lStr;
	}

}