package siap.siep.rateizzazionepp.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadModificaRateizzazione extends ActionSiap 
    implements ICostantiRateizzazionePP 
{
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
    public String processRequest() throws F3BException 
    {
        FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
        
        // Recupero la pena Complessiva da visualizzare (multa e ammenda)
        IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
        DettaglioPenaComplessivaModel lDettMod = lCtrl.ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(lFascMod.getIdFascicoloSiep());
        setRequestAttribute("dettaglioPenaComplessiva", lDettMod);
        
        siesLogger.debug("Sono in load modifica reteizzazione per il fascicolo "+lFascMod.getIdFascicoloSiep());
        Vector <RateizzazionePPModel> listaRateizzazioni = new Vector <RateizzazionePPModel>();
        
        // Ricerca i pagamenti per id Facicolo 
        IRateizzazionePP lRateCTRL = SIEPLookupRemote.getRateizzazionePPRemote();
        listaRateizzazioni = lRateCTRL.exRicercaRateizzazioniByIdFasc(lFascMod.getIdFascicoloSiep());
        
        setRequestAttribute("listaRateizzazioni", listaRateizzazioni);

        setRequestAttribute("modalita", "M");
        
        return PG_LOAD_INSERISCI_RATEIZZAZIONE_PP;
    }
}
