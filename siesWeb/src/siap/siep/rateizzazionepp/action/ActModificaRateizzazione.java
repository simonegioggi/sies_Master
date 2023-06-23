package siap.siep.rateizzazionepp.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

public class ActModificaRateizzazione extends ActInserisciRateizzazione
    implements ICostantiRateizzazionePP 
{
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

    public String processRequest() throws F3BException 
    {        
        FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
        
        siesLogger.debug("Sono in Modifica delle rateizzazioni per il fascicolo "+lFascMod.getIdFascicoloSiep());

        Vector <RateizzazionePPModel> aListaRate = this.recuperaRate();        
        
        // Inserimento
        IRateizzazionePP lRateCTRL = SIEPLookupRemote.getRateizzazionePPRemote();
        lRateCTRL.exModificaRateizzazioni (aListaRate, lFascMod.getIdFascicoloSiep());
        
        //  
        String lPage = "";
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
                + "=siap.siep.rateizzazionepp.action.ActLoadDettagloRateizzazione";
        return lPage;
    }

}
