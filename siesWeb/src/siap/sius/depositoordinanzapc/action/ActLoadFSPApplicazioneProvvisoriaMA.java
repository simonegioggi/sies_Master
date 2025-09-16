package siap.sius.depositoordinanzapc.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

/**
 * ActLoadFSPApplicazioneProvvisoriaMA - Classe per il caricamento dell'applicazione provvisoria di MA
 *
 * @author d.fiorletta
 * @since MEV_9
 */
public class ActLoadFSPApplicazioneProvvisoriaMA extends ActLoadRicercaFSPuntuale {

	public String processRequest() throws Exception {

		setRequestAttribute("functionName", "Applicazione Misure Alternative DL 123/2018");
		setRequestAttribute("nextAction", "siap.sius.depositoordinanzapc.action.ActLoadEmissioneApplicazioneProvvisoriaMA");
		String lPage = super.processRequest();
		return lPage;
	}

}