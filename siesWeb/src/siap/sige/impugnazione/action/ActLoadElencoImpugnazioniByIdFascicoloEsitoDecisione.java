package siap.sige.impugnazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadElencoImpugnazioniByIdFascicoloEsitoDecisione extends ActionSige implements ICostantiImpugnazioneSige{
	public String processRequest() throws Exception {
	    setLinkRitorno();
		FascicoloSigeEstesoModel fascicoloEsteso=super.getFascicoloSigeEstesoInSessione();
	    BigDecimal idFascicoloSige=fascicoloEsteso.getFascicoloSige().getIdFascicoloSige();
	    IImpugnazioneSige ctrlImp=SIGELookupRemote.getImpugnazioneSigeRemote();
	    String codTipoImpugnazione = (String)super.getSessionAttribute("codTipoImpugnazione");
	    Vector<ImpugnazioneSigeModel> opposizioni = ctrlImp.ExRicercaImpugnazioniFascicoloSigePerEsitoDecisione (idFascicoloSige, codTipoImpugnazione);
	    setRequestAttribute("impugnazioni", opposizioni);
	    setRequestAttribute("numero_Impugnazioni", String.valueOf(opposizioni.size()));
	    String page = this.getPage(opposizioni);
	    return page;
	}
	
	private String getPage (Vector<ImpugnazioneSigeModel> opposizioni) {
		String page = PG_ELENCOIMPUGNAZIONISIGE;
		if (opposizioni.size() == 0 || opposizioni.size() > 1)
			return page;
	
		
		ImpugnazioneSigeModel opposizione = opposizioni.elementAt(0);	
		BigDecimal idProvvedimento = opposizione.getProvvIdProvvedimentoSige();
		
		if( opposizione.getCodTenoreDecisione() != null && !opposizione.getCodTenoreDecisione().equals("") ){
			return page;
		} else {
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction("siap.sige.impugnazione.action.ActLoadInserisciEsitoImpugnazioneSige");
			lPage.setParameter(CAMPO_ID_IMPUGNAZIONE, opposizione.getIdImpugnazioneSige().toString());
			lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, idProvvedimento.toString());
			lPage.setParameter( IWebConstants.LINK_RITORNO, "20" );  
			page=lPage.toString();
			return page;
		}
	}
	
	
}
