package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * La Action viene richiamatda SOPO il trasferimenti istruttoria CUmulo e deve:
 * - caricare in sessione il fasciolo dell'istruttori su cui sono stati caricati i dati
 * - aprire la sezione "titoli coinvolti" dellistruttoria su cui sono stati trasferiti i dati  
 * 
 * */
public class ActLoadIstruttoriaDopoTrasferimento extends ActionSiap implements ICostantiIstruttoriaCumulo {
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
    public String processRequest() throws F3BException {
        siesLogger.debug("ActLoadIstruttoriaDopoTrasferimento");
        
        
        BigDecimal lIdIstruttoriaCumuloNew = getRequestBigDecimalParameter(CAMPO_ID_ISTRUTTORIA_CUMULO+"_new");
        BigDecimal lIdFascicoloSIEP = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
        
        // Pulisco i dati di sessione del fascicolo corrente (vedi ActCleanWorkSessionSIEP)
        setSessionAttribute("fascicolo",null);
        setSessionAttribute("soggetto",null);
        setSessionAttribute("sentenza",null);

        setSessionAttribute("cumulowiz",null);
        setSessionAttribute("penaresidua",null);
        setSessionAttribute("reato",null);
        
        // Carica il fascicolo in sessione
        IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
        DettaglioFascicoloModel lDettaglio = lCtrl.ExDettaglioFascicoloSiepNew(lIdFascicoloSIEP);

        // Inserisce nella session il fascicolo (contenente Soggetto e Sentenza)
        setSessionAttribute("fascicolo", lDettaglio.getFascicoloSiep());
        setSessionAttribute("soggetto", lDettaglio.getFascicoloSiep().getSoggetto());
        setSessionAttribute("sentenza", lDettaglio.getFascicoloSiep().getSentenza());
        
        // Giro la chiamata alla funzione di visualizazione titoli coinvolti
        String lPage = "";
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
                + "=siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti&" + CAMPO_ID_ISTRUTTORIA_CUMULO
                + "=" + lIdIstruttoriaCumuloNew.toString();
        return lPage;
    }
        
        
}
