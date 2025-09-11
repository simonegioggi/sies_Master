package siap.sius.depositoordinanzapc.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

/**
 * 
 * @author d.fiorletta
 * @since MEV_9
 */
public class ActLoadFSPApplicazioneProvvisoriaMA extends ActLoadRicercaFSPuntuale {
	public String processRequest() throws Exception {
		setRequestAttribute("functionName", "Applicazione Provvisoria M.A.");
		setRequestAttribute("nextAction", "siap.sius.depositoordinanzapc.action.ActLoadEmissioneApplicazioneProvvisoriaMA");
		String lPage = super.processRequest();
		return lPage;
	}
}
