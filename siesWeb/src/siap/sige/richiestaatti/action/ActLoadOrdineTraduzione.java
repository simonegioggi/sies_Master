package siap.sige.richiestaatti.action;


import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadOrdineTraduzione extends ActRicercaFSigePuntuale
implements ICostantiRichiestaAtti, ICostantiFascicoloSige
{
	public String processRequest() throws Exception
	  {
	    String lRetPage = null;

	    super.processRequest();

	    // Fascicolo Sige Esteso in sessione.
	    FascicoloSigeEstesoModel lFasEsteso = this.getFascicoloSigeEstesoInSessione();
	    
	 

	    // Si passa alla Action successiva per la ricerca.
	    RedirectTo lPage = new RedirectTo();
	    lPage.setPage(IWebConstants.PG_MAIN);
	    lPage.setAction("siap.sige.richiestaatti.action.ActLoadInserisciOrdineTraduzione");
	    lPage.setParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE, lFasEsteso.getFascicoloSige().getIdFascicoloSige().toString());
	    lRetPage = lPage.toString();

	    return lRetPage;
	  }
}