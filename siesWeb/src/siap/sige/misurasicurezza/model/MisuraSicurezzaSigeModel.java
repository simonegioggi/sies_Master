package siap.sige.misurasicurezza.model;

/**
* <p>Title: MisuraSicurezzaSigeModel</p>
*<p
* </p>
* <p>Description: Classe Model specializzazione del MisuraSicurezzaModel,
* rappresenta la Misura di Sicurezza legata
* ad una Sentenza e ad un Fascicolo Sige.
* La classe rappresenta quindi oltre alla Misura di Sicurezza
* ma anche la relazione alla
* Sentenza-FascicoloSige attraverso il suo id.
* *</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;

public class MisuraSicurezzaSigeModel extends MisuraSicurezzaModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -5717703241095762427L;
	private BigDecimal mFasSigeSenId;

	// COSTRUTTORE DI DEFAULT
	public MisuraSicurezzaSigeModel() {
		super();
		mFasSigeSenId = null;
	}

	public MisuraSicurezzaSigeModel(MisuraSicurezzaModel aModel) {
		super(aModel);
		mFasSigeSenId = null;
	}

	public MisuraSicurezzaSigeModel(MisuraSicurezzaModel aModel, BigDecimal aIdFasSigeSen) {
		this(aModel);
		mFasSigeSenId = aIdFasSigeSen;
	}

	// COSTRUTTORE DI COPIA
	public MisuraSicurezzaSigeModel(MisuraSicurezzaSigeModel aModel) {
		super(aModel);
		mFasSigeSenId = aModel.mFasSigeSenId;
	}

	// COSTRUTTORE MODEL
	public MisuraSicurezzaSigeModel(BigDecimal aIdMisuraSicurezza, BigDecimal aFasSigeSenId) {
		this();
		this.setIdMisuraSicurezza(aIdMisuraSicurezza);
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
