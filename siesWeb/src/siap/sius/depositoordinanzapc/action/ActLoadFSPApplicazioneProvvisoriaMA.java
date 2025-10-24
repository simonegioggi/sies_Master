package siap.sius.depositoordinanzapc.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

/**
 * ActLoadFSPApplicazioneProvvisoriaMA - Classe per il caricamento della action di Emissione Applicazione MA DL
 * 123/2018
 *
 * @author d.fiorletta
 * @since MEV_2019-09
 */
public class ActLoadFSPApplicazioneProvvisoriaMA extends ActLoadRicercaFSPuntuale {

	public String processRequest() throws Exception {

		setRequestAttribute("functionName", "Applicazione M.A. DL 123/2018");
		setRequestAttribute("nextAction",
				"siap.sius.depositoordinanzapc.action.ActLoadEmissioneApplicazioneProvvisoriaMA");
		String lPage = super.processRequest();
		return lPage;
	}

}