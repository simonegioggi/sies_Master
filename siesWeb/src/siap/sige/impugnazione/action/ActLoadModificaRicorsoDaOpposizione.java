package siap.sige.impugnazione.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.web.html.Option;

public class ActLoadModificaRicorsoDaOpposizione extends ActionSige implements ICostantiImpugnazioneSige {
	public String processRequest() throws Exception {
		BigDecimal idImpugnazione = super.getRequestBigDecimalParameter(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE);
	    IImpugnazioneSige impuCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
	    ImpugnazioneSigeModel impugnazione=impuCtrl.ExRicercaImpugnazioneByKey(idImpugnazione);
	    super.setRequestAttribute("impugnazione", impugnazione);
	    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
	    String[] lFilterCSS = {"CSS"};
	    lOption.setFilter( lFilterCSS );
	    setRequestAttribute("ListaUffici", ""+ lOption);
	    
	    setRequestAttribute("ProvenienteDa", "Opposizione N. " + impugnazione.getAnnoS7() + "/" + impugnazione.getProgrS7());
	    
	    return PG_LOAD_RICORSO_CONVERTITO;
	}
}
