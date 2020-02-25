package siap.sius.statistiche.model;

//import java.math.BigDecimal;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.impugnazione.model.ImpugnazioneModel;

/**
 * Questo model rappresenta una specializzazione di EveFasGepSogModel, aggregato di più model. Questo aggiunge
 * ai model già presenti 2 model rappresentativi del Provvedimento SIUS: Il Provvedimento può essere un
 * Decreto o un'Ordinanza. Utilizzato per contenere i risultati di ricerche su dati strutturati SIUS.
 * 
 * @author Lesposito
 *
 */
public class EveFasGepSogProvModel extends EveFasGepSogModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2486471154113172836L;

	private DepositoOrdinanzaPcModel mDepOrdinanza = null;
	private DepositoSentenzaModel mDepSentenza = null;
	private DepositoDecretoModel mDepDecreto = null;
	private ImpugnazioneModel mImpugnazione = null;
	private MagistratoModel mMagistrato = null;

	// COSTRUTTORE DI DEFAULT
	public EveFasGepSogProvModel() {
		super();
		mDepOrdinanza = null;
		mDepSentenza = null;
		mDepDecreto = null;
		mImpugnazione = null;
	}

	public EveFasGepSogProvModel(EveFasGepSogModel aAncestor) {
		super(aAncestor);
		mDepOrdinanza = null;
		mDepSentenza = null;
		mDepDecreto = null;
		mImpugnazione = null;
	}

	public EveFasGepSogProvModel(EveFasGepSogModel aAncestor, DepositoOrdinanzaPcModel aOrdinanza) {
		this(aAncestor);
		if (aOrdinanza != null)
			mDepOrdinanza = new DepositoOrdinanzaPcModel(aOrdinanza);
	}

	public EveFasGepSogProvModel(EveFasGepSogModel aAncestor, DepositoSentenzaModel aOrdinanza) {
		this(aAncestor);
		if (aOrdinanza != null)
			mDepSentenza = new DepositoSentenzaModel(aOrdinanza);
	}

	public EveFasGepSogProvModel(EveFasGepSogModel aAncestor, DepositoDecretoModel aDecreto) {
		this(aAncestor);
		if (aDecreto != null)
			mDepDecreto = new DepositoDecretoModel(aDecreto);
	}

	public EveFasGepSogProvModel(EveFasGepSogModel aAncestor, ImpugnazioneModel aImpugnazione) {
		this(aAncestor);
		if (aImpugnazione != null)
			mImpugnazione = new ImpugnazioneModel(aImpugnazione);
	}

	// METODI GET()
	//

	public DepositoOrdinanzaPcModel getDepositoOrdinanzaPc() {
		return mDepOrdinanza;
	}

	public DepositoSentenzaModel getDepositoSentenza() {
		return mDepSentenza;
	}

	public DepositoDecretoModel getDepositoDecreto() {
		return mDepDecreto;
	}

	public ImpugnazioneModel getImpugnazione() {
		return mImpugnazione;
	}

	public MagistratoModel getMagistrato() {
		return mMagistrato;
	}

	// METODI SET()
	public void setDepositoOrdinanzaPc(DepositoOrdinanzaPcModel aValore) {
		mDepOrdinanza = aValore;
	}

	public void setDepositoSentenza(DepositoSentenzaModel aValore) {
		mDepSentenza = aValore;
	}

	public void setDepositoDecreto(DepositoDecretoModel aValore) {
		mDepDecreto = aValore;
	}

	public void setImpugnazione(ImpugnazioneModel aValore) {
		mImpugnazione = aValore;
	}

	public void setMagistrato(MagistratoModel aValore) {
		mMagistrato = aValore;
	}

}