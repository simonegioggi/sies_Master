package siap.siep.modulocumulo.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils;
import siap.siep.util.SIEPLookupRemote;


/*****************************************************************************
 * Azione di Ricerca Delle richieste al G.E. Amnistia/Indulto/depenalizzazione/Incostituzionalita'
 * 
 * Recupera i provvedimenti di  associati a un certo titolo
 * 
 * @since MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
 *****************************************************************************/
public class ActRicercaRichBenGE extends ActionModuloCumulo implements ICostantiComputiCumulo {
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
    public String processRequest() throws F3BException {
        siesLogger.debug("ActRicercaRichBenGE...");
        if (this.isSessionAttributeNullObj("fascicolo"))
        {
          return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
        }
        
        //==========================================================================
        // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
        // di DettaglioTitoloCumulato.jsp
        //==========================================================================
        super.getDatiIstruttoria();
        TitoloCumulatoModel lTitolo = super.getDatiTitoloCumulato();
        
        //=====================================================================================
        // Ricerca dei provvedimenti 
        //=====================================================================================
        IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
        
        Vector<String> aVectTipoProvv = new Vector<String>(); //tutti
        Vector<String> aVectCodMotivo = new Vector<String>(StatoEsecuzioneCumuloUtils.aCodRichiesteBeneficiAlGE);

        Vector<StatoEsecTitoloCumulatoModel> lVectStati = null;
        lVectStati = lCtrlStato.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(
                lTitolo.getIdTitoloCumulato(), "01", aVectTipoProvv, aVectCodMotivo);
        
        siesLogger.debug("lVect.size() = "+lVectStati.size());
        
        // Passo alla form i dati trovati
        setRequestAttribute("ListaRichiesteBeneficiGE", lVectStati);
        
        return PG_ELENCO_RICH_BENEFICI_GE;
      }    
}
