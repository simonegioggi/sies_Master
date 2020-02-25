package siap.sige.attiinarchivio.action;

import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


public class ActLoadFSPAttiInArchivio extends ActLoadRicercaFSigePuntuale {
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    
	FascicoloSigeEstesoModel lFascicoloEsteso = (FascicoloSigeEstesoModel)super.getRequest().getSession().getAttribute("FascicoloSigeEsteso");
	
	 // 30//11/2018 aggiungo blocco su richiesta Nunzia (email del 29/11/2018 - Unificazione procedimento.docx)
	 if (lFascicoloEsteso != null && IsFascicoloUnificato())
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Non è possibile procedere con la funzione Atti in Archivio per questo Procedimento!");
	 
	if (lFascicoloEsteso != null) {
		RedirectTo lPage = new RedirectTo();
        lPage.setPage(IWebConstants.PG_MAIN);
        lPage.setAction("siap.sige.attiinarchivio.action.ActLoadDettaglioAttiInArchivio");
        return lPage.toString();
	}
	
	setRequestAttribute("functionName", "Data Invio Atti in Archivio");
    setRequestAttribute("nextAction", "siap.sige.attiinarchivio.action.ActRicercaFSPAttiInArchivio");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
  
	protected boolean IsFascicoloUnificato() throws F3BException {
		FascicoloSigeEstesoModel lFascicoloEsteso = (FascicoloSigeEstesoModel)super.getRequest().getSession().getAttribute("FascicoloSigeEsteso");
				
		boolean lRet = false;

		if (lFascicoloEsteso != null && lFascicoloEsteso.getFascicoloSige().getCodStatoFascicolo().equalsIgnoreCase("05")	
				&& getCodUfficioUtenteConnesso().equalsIgnoreCase(lFascicoloEsteso.getFascicoloSige().getChiaveUfficio()))
			lRet = true;

		return lRet;
	}
}