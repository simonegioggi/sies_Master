package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * 
 * @author 
 *
 */

public class ActAssociaRIaFascicoloSIEP extends ActionNuovaIstanza implements ICostantiNuovaIstanza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	
	public String processRequest() throws Exception
	 {
		
		BigDecimal idFascicolo = this.getRequestBigDecimalParameter("idFascicolo");
		
		FascicoloSiepModel lRIMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lRIMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lRIMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lRIMod.setFasSieIdFascicoloSiep(idFascicolo);
		
	    INuovaIstanza lCtrl2 = SIEPLookupRemote.getNuovaIstanzaRemote();

	    //preparo il model dell'ISTANZA per la duplicazione
	    Collection<NuovaIstanzaModel>  collNIMod = lCtrl2.ExRicercaNuovaIstanzaNonAnnullataByIdFascicolo(lRIMod.getIdFascicoloSiep());
	    if (collNIMod.size() != 1)
	    	throw new F3BException(F3BException.USER_MESSAGE, "Attenzione : Associazione impossibile per questo registro! Sono presenti più istanze.");
	    // Deve esserci una sola nuova istanza per un registro classe 90000.
	    // Per il momento il blocco viene messo qui (e non in ActLoad) in attesa di ulteriori chiarimenti sulla possibilità
	    // di più istanze.
	    
	    NuovaIstanzaModel lNuoMod = (NuovaIstanzaModel) collNIMod.toArray()[0 ];
		
	    //preparo il model dell'EVENTO per la duplicazione
	    IEvento lCtrEve = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrEve.ExRicercaEventoByKey(lNuoMod.getEveIdEvento());

	    lRIMod = lCtrl2.ExAssociaNuovaIstanza(idFascicolo, lRIMod, lEveMod, lNuoMod);

	    FascicoloSiepModel lFascMod = new FascicoloSiepModel();
	    IFascicoloSiep lCtrl1 = SIEPLookupRemote.getFascicoloSiepRemote();
	    
	    lFascMod=lCtrl1.ExRicercaFascicoloByKey(idFascicolo);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("MODEL MODIFICATO: " + lFascMod);
	    
		setRequestAttribute("fascicolo", lFascMod);
	    setSessionAttribute("fascicolo",lFascMod);
	    setSessionAttribute("soggetto",lFascMod.getSoggetto());
	    setSessionAttribute("sentenza",lFascMod.getSentenza());
	    
	    String lPage="";
	    lPage = IWebConstants.PG_MAIN + "?" + 
	    	IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo";
	    //lPage += "&" + "IdEvento" + "=" + lNuoRetMod.getEveIdEvento().toString();
	    //lPage += "&" + "TipoVis" + "=Inoltro";

	    return lPage;
	 }
	
	
	

}