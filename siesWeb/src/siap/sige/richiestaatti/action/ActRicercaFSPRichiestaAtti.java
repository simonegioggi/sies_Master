package siap.sige.richiestaatti.action;

import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;

public class ActRicercaFSPRichiestaAtti extends ActRicercaFSigePuntuale implements ICostantiRichiestaAtti {

	public String processRequest() throws Exception {

//		String lPage = new String();

		// Passando il parametro noQuery non effettua nuovamente la ricerca
		if (isRequestParameterNullObj("noQuery"))
			super.processRequest();
    
 // 30//11/2018 aggiungo blocco su richiesta Nunzia (email del 29/11/2018 -Unificazione procedimento.docx)
    if (IsFascicoloUnificato())
		throw new SIGEException(SIGEException.USER_MESSAGE,
				"Non è possibile effettuare una Richiesta Atti per questo Procedimento!");
    
		// Bottone di ritorno
		setLinkRitorno();

		// Ritorna la JSP di view dell'elenco stampe.
		return PG_ELENCOSTAMPEDOCISTRUTTORI;
	}

}