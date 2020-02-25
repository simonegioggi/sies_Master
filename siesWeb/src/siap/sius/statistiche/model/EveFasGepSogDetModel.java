package siap.sius.statistiche.model;

import siap.sico.magistrato.model.MagistratoModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;

/**
 * Questo model rappresenta una specializzazione di EveFasGepSogModel, aggregato di più model. Questo aggiunge
 * ai model già presenti 2 model rappresentativi del Provvedimento SIUS: Il Provvedimento può essere un
 * Decreto o un'Ordinanza. Utilizzato per contenere i risultati di ricerche su dati strutturati SIUS.
 * 
 * @author Lesposito
 *
 */
public class EveFasGepSogDetModel extends EveFasGepSogModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -495692456100790719L;

	private IstitutoDetenzioneModel mIstitutoDetenzione = null;

	// #: 20131228 - da verificare se incapsularlo in altra classe model o cambiare nonme
	// di questa classe aggiungendo mag
	private MagistratoModel mMagistrato = null;

	// COSTRUTTORE DI DEFAULT
	public EveFasGepSogDetModel() {
		super();
		mIstitutoDetenzione = null;
		mMagistrato = null;
	}

	public EveFasGepSogDetModel(EveFasGepSogModel aAncestor) {
		super(aAncestor);
		mIstitutoDetenzione = null;
		mMagistrato = null;
	}

	public EveFasGepSogDetModel(EveFasGepSogModel aAncestor, IstitutoDetenzioneModel aIstitutoDetenzione) {
		super(aAncestor);
		if (aIstitutoDetenzione != null)
			mIstitutoDetenzione = new IstitutoDetenzioneModel(aIstitutoDetenzione);
	}

	public EveFasGepSogDetModel(EveFasGepSogModel aAncestor, IstitutoDetenzioneModel aIstitutoDetenzione,
			MagistratoModel aMagistrato) {
		super(aAncestor);
		if (aIstitutoDetenzione != null)
			mIstitutoDetenzione = new IstitutoDetenzioneModel(aIstitutoDetenzione);
		if (aMagistrato != null)
			mMagistrato = new MagistratoModel(aMagistrato);
	}

	// METODI GET()
	public IstitutoDetenzioneModel getIstitutoDetenzione() {
		return mIstitutoDetenzione;
	}

	public MagistratoModel getMagistrato() {
		return mMagistrato;
	}

	// METODI SET()
	public void setIstitutoDetenzione(IstitutoDetenzioneModel aValore) {
		mIstitutoDetenzione = aValore;
	}

	public void setMagistrato(MagistratoModel aValore) {
		mMagistrato = aValore;
	}

}