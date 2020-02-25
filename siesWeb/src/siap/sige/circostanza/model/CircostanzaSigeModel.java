package siap.sige.circostanza.model;

/**
* <p>Title: CircostanzaSigeModel</p>
*<p
* </p>
* <p>Description: Classe Model specializzazione del CircostanzaModel, rappresenta la Circostanza legata 
* ad una Sentenza e ad un Fascicolo Sige.
* La classe rappresenta quindi oltre al Reato anche la relazione alla 
* Sentenza-FascicoloSige attraverso il suo id.
* *</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Agile</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.circostanza.model.CircostanzaModel;

public class CircostanzaSigeModel extends CircostanzaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6890286644417214299L;

	private BigDecimal mFasSigeSenId;

	// COSTRUTTORE DI DEFAULT
	public CircostanzaSigeModel() {
		super();
		mFasSigeSenId = null;
	}

	public CircostanzaSigeModel(CircostanzaModel aModel) {
		super(aModel);
		mFasSigeSenId = null;
	}

	// COSTRUTTORE DI COPIA
	public CircostanzaSigeModel(CircostanzaSigeModel aModel) {
		super(aModel);
		mFasSigeSenId = aModel.mFasSigeSenId;
	}

	// COSTRUTTORE MODEL
	public CircostanzaSigeModel(BigDecimal aIdCircostanza, BigDecimal aFasSigeSenId) {
		this();
		setIdCircostanza(aIdCircostanza);
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