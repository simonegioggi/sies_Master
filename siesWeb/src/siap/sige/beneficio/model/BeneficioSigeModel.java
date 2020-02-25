package siap.sige.beneficio.model;

/**
* <p>Title: BeneficioSigeModel</p>
*<p
* </p>
* <p>Description: Classe Model specializzazione del BeneficioModel, 
* rappresenta il Beneficio
* ad una Sentenza e ad un Fascicolo Sige.
* La classe rappresenta quindi oltre al Beneficio 
* anche la relazione alla 
* Sentenza-FascicoloSige attraverso il suo id.
* *</p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: Agile</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.beneficio.model.BeneficioModel;

public class BeneficioSigeModel extends BeneficioModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1807121186042233488L;

	private BigDecimal mFasSigeSenId;

	// COSTRUTTORE DI DEFAULT
	public BeneficioSigeModel() {
		super();
		mFasSigeSenId = null;
	}

	public BeneficioSigeModel(BeneficioModel aModel) {
		super(aModel);
		mFasSigeSenId = null;
	}

	public BeneficioSigeModel(BeneficioModel aModel, BigDecimal aIdFasSigeSen) {
		this(aModel);
		mFasSigeSenId = aIdFasSigeSen;
	}

	// COSTRUTTORE DI COPIA
	public BeneficioSigeModel(BeneficioSigeModel aModel) {
		super(aModel);
		mFasSigeSenId = aModel.mFasSigeSenId;
	}

	// COSTRUTTORE MODEL
	public BeneficioSigeModel(BigDecimal aIdBeneficio, BigDecimal aFasSigeSenId) {
		this();
		this.setIdBeneficio(aIdBeneficio);
		mFasSigeSenId = aFasSigeSenId;
	}

	//
	// METODI GET()
	//
	public BigDecimal getFasSigeSenId() {
		return mFasSigeSenId;
	}

	//
	// METODI SET()
	//
	public void setFasSigeSenId(BigDecimal aValore) {
		mFasSigeSenId = aValore;
	}

}