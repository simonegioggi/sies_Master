package siap.sige.statistiche.model;

import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;

/**
 * Questo model rappresenta una specializzazione di EveFasGepSogModel, aggregato di più model. Questo aggiunge
 * ai model già presenti 2 model rappresentativi del Provvedimento SIGE: Il Provvedimento può essere un
 * Decreto o un'Ordinanza. Utilizzato per contenere i risultati di ricerche su dati strutturati SIGE.
 */
public class EveFasGepSogProvModel extends EveFasGepSogModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 623751588312844121L;

	private DepositoOrdinanzaPcModel mDepOrdinanza = null;
	private DepositoDecretoModel mDepDecreto = null;
	private ProvvedimentoSigeModel mProvvedimentoSige = null;
	private UdienzaSigeModel mUdienzaSige = null;

	// COSTRUTTORE DI DEFAULT
	public EveFasGepSogProvModel() {
		super();
		mDepOrdinanza = null;
		mDepDecreto = null;
		mProvvedimentoSige = null;
		mUdienzaSige = null;
	}

	public EveFasGepSogProvModel(EveFasGepSogModel aAncestor) {
		super(aAncestor);
		mDepOrdinanza = null;
		mDepDecreto = null;
		mProvvedimentoSige = null;
		mUdienzaSige = null;
	}

	public EveFasGepSogProvModel(EveFasGepSogModel aAncestor, DepositoOrdinanzaPcModel aOrdinanza) {
		this(aAncestor);
		if (aOrdinanza != null)
			mDepOrdinanza = new DepositoOrdinanzaPcModel(aOrdinanza);

	}

	public EveFasGepSogProvModel(EveFasGepSogModel aAncestor, DepositoDecretoModel aDecreto) {
		this(aAncestor);
		if (aDecreto != null)
			mDepDecreto = new DepositoDecretoModel(aDecreto);
	}

	public EveFasGepSogProvModel(EveFasGepSogModel aAncestor, ProvvedimentoSigeModel aProvvSige,
			UdienzaSigeModel aUdienzaSige) {
		this(aAncestor);
		if (aProvvSige != null)
			mProvvedimentoSige = new ProvvedimentoSigeModel(aProvvSige);
		if (aUdienzaSige != null)
			mUdienzaSige = new UdienzaSigeModel(aUdienzaSige);
	}

	// METODI GET()
	public DepositoOrdinanzaPcModel getDepositoOrdinanzaPc() {
		return mDepOrdinanza;
	}

	public DepositoDecretoModel getDepositoDecreto() {
		return mDepDecreto;
	}

	public ProvvedimentoSigeModel getProvvedimentoSige() {
		return mProvvedimentoSige;
	}

	public UdienzaSigeModel getUdienzaSige() {
		return mUdienzaSige;
	}

	// METODI SET()
	public void setDepositoOrdinanzaPc(DepositoOrdinanzaPcModel aValore) {
		mDepOrdinanza = aValore;
	}

	public void setDepositoDecreto(DepositoDecretoModel aValore) {
		mDepDecreto = aValore;
	}

	public void setProvvedimentoSige(ProvvedimentoSigeModel aValore) {
		mProvvedimentoSige = aValore;
	}

	public void setUdienzaSige(UdienzaSigeModel aValore) {
		mUdienzaSige = aValore;
	}

}