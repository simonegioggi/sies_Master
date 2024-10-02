package siap.sius.depositoordinanzapc.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

/**
 * ActLoadFSPApplicazioneProvvisoriaMA - funzione di pre caricamento
 * 
 * @author d.fiorletta
 * @since MEV_2019-09
 */
public class ActLoadFSPApplicazioneProvvisoriaMA extends ActLoadRicercaFSPuntuale {

	public String processRequest() throws Exception {

		// MEV_2024-092: cambio messaggio da Provvisoria M.A. a Misure Alternative Dl 123/2018
		setRequestAttribute("functionName", "Applicazione Misure Alternative Dl 123/2018");
		setRequestAttribute("nextAction", "siap.sius.depositoordinanzapc.action.ActLoadEmissioneApplicazioneProvvisoriaMA");
		String lPage = super.processRequest();
		return lPage;
	}

}