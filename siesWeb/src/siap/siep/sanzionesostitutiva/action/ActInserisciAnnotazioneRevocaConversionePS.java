package siap.siep.sanzionesostitutiva.action;

/**
 * <p>Title: ActInserisciAnnotazioneRevocaConversionePS</p>
 * <p>Description: Annotazione in SIEP di un provvedimento SIUS di revoca/conversione di pena sostitutiva (Riforma Cartabia)</p>
 * @author SG
 * @since 20261124-MEV_2026-124_SG
 */
public class ActInserisciAnnotazioneRevocaConversionePS extends ActInserisciAnnotazioneProvvedimentoPS {

	protected String getActionDettaglio() {
		return "siap.siep.sanzionesostitutiva.action.ActDettaglioAnnotazioneRevocaConversionePS";
	}
}
