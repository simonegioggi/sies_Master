package siap.sige.penacomplessiva.model;

/**
* <p>Title: PenaCompSigeModel</p>
* <p>Description: Classe Model che rappresenta la relazione tra PenaComplessiva e SentenzaSige.
*</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.penacomplessiva.model.PenaComplessivaModel;

public class PenaCompSigeModel extends PenaComplessivaModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -8890024268239362090L;
	private BigDecimal mFasSigeSenId;

	// COSTRUTTORE DI DEFAULT
	public PenaCompSigeModel() {
		super();
		mFasSigeSenId = null;
	}

	public PenaCompSigeModel(PenaComplessivaModel aModel) {
		super(aModel);
		mFasSigeSenId = null;
	}

	// COSTRUTTORE DI COPIA
	public PenaCompSigeModel(PenaCompSigeModel aModel) {
		super(aModel);
		mFasSigeSenId = aModel.mFasSigeSenId;
	}

	// COSTRUTTORE MODEL
	public PenaCompSigeModel(BigDecimal aIdPenaComplessiva, BigDecimal aFasSigeSenId) {
		this();
		setIdPenaComplessiva(aIdPenaComplessiva);
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
