package siap.sige.penaaccessoria.model;

/**
* <p>Title: PenaCompSigeModel</p>
* <p>Description: Classe Model che rappresenta la relazione tra PenaComplessiva e SentenzaSige.
*</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.penaaccessoria.model.PenaAccessoriaModel;

public class PenaAccSigeModel extends PenaAccessoriaModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -9152941785808818591L;
	private BigDecimal mFasSigeSenId;

	// COSTRUTTORE DI DEFAULT
	public PenaAccSigeModel() {
		super();
		mFasSigeSenId = null;
	}

	public PenaAccSigeModel(PenaAccessoriaModel aModel) {
		super(aModel);
		mFasSigeSenId = null;
	}

	// COSTRUTTORE DI COPIA
	public PenaAccSigeModel(PenaAccSigeModel aModel) {
		super(aModel);
		mFasSigeSenId = aModel.mFasSigeSenId;
	}

	// COSTRUTTORE MODEL
	public PenaAccSigeModel(BigDecimal aIdPenaAccessoria, BigDecimal aFasSigeSenId) {
		this();
		setIdPenaAccessoria(aIdPenaAccessoria);
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
