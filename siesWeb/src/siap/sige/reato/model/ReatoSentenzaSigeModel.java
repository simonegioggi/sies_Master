package siap.sige.reato.model;

/**
* <p>Title: ReatoSentenzaSigeModel</p>
* <p>Description: Classe Model che rappresenta il ReatoSentenzaSige,
* tracciato record della tabella REATO_SENTENZA_SIGE.</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.reato.model.ReatoModel;

public class ReatoSentenzaSigeModel extends ReatoModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -5850111488268972277L;
	// private BigDecimal mReaIdReato;
	private BigDecimal mFasSigeSenId;

	// COSTRUTTORE DI DEFAULT
	public ReatoSentenzaSigeModel() {
		super();
		// mReaIdReato = null;
		mFasSigeSenId = null;
	}

	public ReatoSentenzaSigeModel(ReatoModel aModel) {
		super(aModel);
		mFasSigeSenId = null;
	}

	// COSTRUTTORE DI COPIA
	public ReatoSentenzaSigeModel(ReatoSentenzaSigeModel aModel) {
		super(aModel);
		// mReaIdReato = aModel.mReaIdReato;
		mFasSigeSenId = aModel.mFasSigeSenId;
	}

	// COSTRUTTORE MODEL
	public ReatoSentenzaSigeModel(BigDecimal aReaIdReato, BigDecimal aFasSigeSenId) {
		this();
		setIdReato(aReaIdReato);
		// mReaIdReato = aReaIdReato;
		mFasSigeSenId = aFasSigeSenId;
	}

	//
	// METODI GET()
	//

	// public BigDecimal getReaIdReato() { return mReaIdReato; }
	public BigDecimal getFasSigeSenId() {
		return mFasSigeSenId;
	}

	//
	// METODI SET()
	//

	// public void setReaIdReato(BigDecimal aValore ) { mReaIdReato = aValore; }
	public void setFasSigeSenId(BigDecimal aValore) {
		mFasSigeSenId = aValore;
	}

}
