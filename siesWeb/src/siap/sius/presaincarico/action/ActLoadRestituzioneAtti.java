package siap.sius.presaincarico.action;

import siap.sico.web.ActionSiap;

public class ActLoadRestituzioneAtti extends ActionSiap implements ICostantiPresaincarico{	
	public String processRequest() throws Exception{
		return PG_DETTAGLIO_REST_ATTI_COMPETENZA;
	}	
}
