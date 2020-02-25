package siap.sige.fogliocomplementare.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadFoglioComplementare extends ActLoadRicercaFSigePuntuale {
		
	public String processRequest () throws Exception{
		FascicoloSigeEstesoModel lFascicoloEsteso = (FascicoloSigeEstesoModel)super.getRequest().getSession().getAttribute("FascicoloSigeEsteso");
    	 
    	 if (lFascicoloEsteso != null) {
    		 RedirectTo lPage = new RedirectTo();
    	        lPage.setPage(IWebConstants.PG_MAIN);
    	        lPage.setAction("siap.sige.fogliocomplementare.action.ActRicercaFoglioComplementare");
    	        return lPage.toString();
    	}
    	setRequestAttribute("functionName", "Compilazione Foglio Complementare");
    	setRequestAttribute("nextAction", "siap.sige.fogliocomplementare.action.ActRicercaFoglioComplementare");
    	    
    	// Si invoca il metodo della superclasse.
    	String lPage = super.processRequest();
    	return lPage;
     }
}
