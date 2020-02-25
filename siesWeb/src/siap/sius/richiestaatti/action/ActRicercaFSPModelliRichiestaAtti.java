package siap.sius.richiestaatti.action;

import siap.sius.fascicolo.action.ActRicercaFSPuntuale;

public class ActRicercaFSPModelliRichiestaAtti extends ActRicercaFSPuntuale implements ICostantiRichiestaAtti {

	public String processRequest() throws Exception {

//		String lPage = new String();

		// Prelevo la data camera consiglio dalla sessione
		super.processRequest(); // Processo la request

//		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Il sistema controlla che il fascicolo non sia definito
		// if (this.IsFascicoloSiusModificabile()==false)
		// throw new SIUSException(SIUSException.USER_MESSAGE,ICostantiFascicoloSius.MSG_NON_MODIFICABILE);

		if (isRequestParameterNullObj("viewElenco"))
			super.processRequest();

		// Ritorna la JSP di view dell'elenco stampe.
		return PG_ELENCOSTAMPEMODELLIRICHIESTAATTI;
	}

}