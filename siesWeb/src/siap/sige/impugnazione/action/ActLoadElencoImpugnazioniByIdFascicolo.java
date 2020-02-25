package siap.sige.impugnazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

public class ActLoadElencoImpugnazioniByIdFascicolo extends ActionSige implements ICostantiImpugnazioneSige{
	public String processRequest() throws Exception {
	    super.setLinkRitorno();
		FascicoloSigeEstesoModel fascicoloEsteso=super.getFascicoloSigeEstesoInSessione();
	    BigDecimal idFascicoloSige=fascicoloEsteso.getFascicoloSige().getIdFascicoloSige();
	    IImpugnazioneSige ctrlImp=SIGELookupRemote.getImpugnazioneSigeRemote();
	    
	    String codTipoImpugnazione = (String)super.getSessionAttribute("codTipoImpugnazione");
	    
	    Vector<ImpugnazioneSigeModel> opposizioni = ctrlImp.ExRicercaImpugnazioniFascicoloSige(idFascicoloSige, codTipoImpugnazione);
	    setRequestAttribute("impugnazioni", opposizioni);

	    BigDecimal countImpugnazioni = new BigDecimal ( opposizioni.size());
	    setRequestAttribute("numero_Impugnazioni", countImpugnazioni.toString());
	    return PG_ELENCOIMPUGNAZIONISIGE;
	}
}
