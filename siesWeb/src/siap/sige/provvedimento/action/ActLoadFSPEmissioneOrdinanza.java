package siap.sige.provvedimento.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import f3b.web.IWebConstants;

public class ActLoadFSPEmissioneOrdinanza extends ActLoadRicercaFSigePuntuale
implements ICostantiProvvedimentoSige
{
	public String processRequest() throws Exception
	{
		
		String lPage = "";
		  
	    //==========================================================================
	    // Verifico che il parametro CAMPO_ID_FASCICOLO_SIGE sia stato passato sulla
	    // request o presente in sessione.
	    // Questo controllo è necessario in quanto questa funzione
	    // può essere richiamata anche dal menù di scelta rapida
	    //==========================================================================
	    if(!this.isRequestParameterNullObj(CAMPO_ID_FASCICOLO_SIGE) || !this.isSessionAttributeNullObj("FascicoloSigeEsteso")) {
	    	lPage = IWebConstants.PG_MAIN
					 + "?"
					 + IWebConstants.ACTION_FIELD
					 + "=siap.sige.provvedimento.action.ActLoadEmissioneOrdinanza&ritorno=ok";
	    } else {
	        // Se non ho l'id fascicolo ne sulla request ne in sessione restituisco la
	        // pagina di ricerca fascicolo
	    	// Imposta alla JSP il nome della funzione e l'azione
	        // da chiamare alla conferma.
	        setRequestAttribute("functionName", "Emissione Ordinanza");
	        setRequestAttribute("nextAction", "siap.sige.provvedimento.action.ActLoadEmissioneOrdinanza");
	        // Si invoca il metodo della superclasse.
	        lPage = super.processRequest();
	    }
	    
	    return lPage;

	  }		
}