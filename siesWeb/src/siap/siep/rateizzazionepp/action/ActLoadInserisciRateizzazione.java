package siap.siep.rateizzazionepp.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;


public class ActLoadInserisciRateizzazione extends ActionSiap 
            implements ICostantiRateizzazionePP 
{
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
    public String processRequest() throws F3BException 
    {
        
        // se già presenti dati per il fascicolo allora carico la form del dettaglio 
        FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
        
        // Recupero la pena Complessiva da visualizzare (multa e ammenda)
        IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
        DettaglioPenaComplessivaModel lDettMod = lCtrl.ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(lFascMod.getIdFascicoloSiep());
        setRequestAttribute("dettaglioPenaComplessiva", lDettMod);
        
        //Ricerca i pagamenti per id Facicolo 
        Vector <RateizzazionePPModel> listaRateizzazioni = new Vector <RateizzazionePPModel>();
        IRateizzazionePP lRateCTRL = SIEPLookupRemote.getRateizzazionePPRemote();
        listaRateizzazioni = lRateCTRL.exRicercaRateizzazioniByIdFasc(lFascMod.getIdFascicoloSiep());
        
        if (listaRateizzazioni.size()>0) {
            siesLogger.debug("Presenti già rate per il fascicolo. Carico il dettaglio");
            String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
                    + "=siap.siep.rateizzazionepp.action.ActLoadDettagloRateizzazione";
            return lPage;
        }
        else {
            // Se fascicolo validato non posso consentire l'inserimento 
            if ("S".equals(lFascMod.getFlagValidato())) {
                RedirectTo lRedirigi = new RedirectTo();
                lRedirigi.setPage(IWebConstants.PG_MAIN);
                setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Nessuna Rateizzazione presente. "
                		+ "Impossibile procedere all'inserimento in quanto il procedimento risulta gia' validato. "
                		+ "<BR>Per inserire i dati annullare la validazione del procedimento dal menu' 'Funzioni Amministrative'.");
                lRedirigi.setAction("siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&"
                    + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lFascMod.getIdFascicoloSiep());
                setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
                return IWebConstants.PG_MESSAGE;                
            }
            else {
                setRequestAttribute("modalita", "I");
                return PG_LOAD_INSERISCI_RATEIZZAZIONE_PP;                
            }
        }
    }
}
