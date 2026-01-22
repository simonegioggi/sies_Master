package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.calcolopenadl92.controller.ICalcoloPenaDL92;
import siap.siep.calcolopenadl92.model.CalcoloPenaDL92ModelDB;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActLoadStoricoCalcoloPenaDL92 - Classe Action per il caricamento della lista con lo storico
 *
 * @since MEV-2026_1
 */
public class ActLoadStoricoCalcoloPenaDL92 extends ActionSiap implements ICostantiCalcoloPena {
    
    public String processRequest() throws Exception {
        
        BigDecimal lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
        ICalcoloPenaDL92 lCalcDL92Ctrl = SIEPLookupRemote.getCalcoloPenaDL92();
        Vector<CalcoloPenaDL92ModelDB> lStoricoCalcoli = lCalcDL92Ctrl.ExRicercaCalcoloPenaDL92ByIdFas(lFascID);
        setRequestAttribute("storicoCalcoli", lStoricoCalcoli);
        
        return PG_LOAD_STORICO_CALCPENA_DL92;
    }
}
