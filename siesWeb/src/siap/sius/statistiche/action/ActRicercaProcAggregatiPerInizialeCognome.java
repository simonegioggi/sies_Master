package siap.sius.statistiche.action;

import java.util.Collection;
import java.util.Date;

import siap.sico.web.ActionSiap;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.ProcAggregatiCognomeModel;
import siap.sius.statistiche.model.RicercaAggregatiCognomeModel;
import siap.sius.util.SIUSLookupRemote;

public class ActRicercaProcAggregatiPerInizialeCognome extends ActionSiap
        implements ICostantiStatistiche {

    public String processRequest() throws Exception {
        
        Date                                    lDataInizio     = null;
        Date                                    lDataFine       = null;
        RicercaAggregatiCognomeModel            lRicercaModel   = null;
        IStatisticheSius                        lCtrl           = null;
        Collection<ProcAggregatiCognomeModel>   lElenco         = null;
        
        this.setLinkRitorno();

        lDataInizio = getRequestDateParameter(  ICostantiStatistiche.CAMPO_ANNO_INIZIALE, 
                                                ICostantiStatistiche.CAMPO_MESE_INIZIALE, 
                                                ICostantiStatistiche.CAMPO_GIORNO_INIZIALE);

        lDataFine = getRequestDateParameter(    ICostantiStatistiche.CAMPO_ANNO_FINALE, 
                                                ICostantiStatistiche.CAMPO_MESE_FINALE, 
                                                ICostantiStatistiche.CAMPO_GIORNO_FINALE);
        
        lRicercaModel = new RicercaAggregatiCognomeModel();
        
        lRicercaModel.setDataInizio(lDataInizio);
        lRicercaModel.setDataFine(lDataFine);
        lRicercaModel.setUtenteConnesso(getUtenteConnesso());
        
        lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
        lElenco = lCtrl.ExRicercaProcAggregati1Lettera(lRicercaModel);
        
        setSessionAttribute("ricercaProcedimenti", lRicercaModel);
        setRequestAttribute("elencoProcedimenti", lElenco);

        return PG_RICERCA_PROC_AGGREGATI_INIZIALE_COGNOME;
    }
}
