package siap.sius.depositoordinanzapc.action;

import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ActLoadEmissioneDecreto;

public class ActLoadEmissioneApplicazioneProvvisoriaMA extends ActLoadEmissioneDecreto {
	public String processRequest() throws Exception {

		String lPage = super.processRequest();
		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");				
		
		// TODO	aggiungere controllo sulla presenza del Decreto di Designazione
		// 02-0610 flag REGISTRATO = 'S'
		
		setRequestAttribute("flagOrdinanza", "ordinanza");
		return lPage;
	}

}
