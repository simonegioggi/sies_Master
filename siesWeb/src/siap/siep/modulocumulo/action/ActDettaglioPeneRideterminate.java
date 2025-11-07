package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.util.SIEPLookupRemote;


/**
 * Action per il caricamento della for di dettaglio dei dti delle PENE RIDETERMINATE
 * 
 * Se i dati sono assenti, richiama la funzione di inserimento
 * 
 * @author d.fiorletta
 *
 */
public class ActDettaglioPeneRideterminate extends ActionModuloCumulo implements ICostantiModuloCumulo
{
  public String processRequest() throws F3BException
  {
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
    DatiFinaliCumuloAggregatoModel lDatiAggregati = super.getDatiFinaliCumuloAggregato();
    
    //n.b. solo per debug per visualizzare la Action di Dettaglio nel formato
    //     Ulteriori sanzioni
    if (!isRequestParameterNullObj("parametri"))
      setRequestAttribute("parametri",getRequestStringParameter("parametri"));

    //==========================================================================
    // Recupera le Richieste al GE con anticipazione selezionate x i Dati Finali
    //==========================================================================
    Vector <ComputiCumuloModel> lElencoComputi = new Vector <ComputiCumuloModel>();
    IComputiCumulo lCtrlComputi = SIEPLookupRemote.getComputiCumuloRemote();
    
    lElencoComputi = lCtrlComputi.ExRicercaComputiCumuloByIdIstruttoria (lIstruttoriaModel.getIdIstruttoriaCumulo()
        ,lDatiAggregati.getDatiFinaliCumulo().getIdDatiFinaliCumulo());
    
    setRequestAttribute("ListaRichiesteAlGE", lElencoComputi);   
    
    // MEV_2025-48 - 2.12 Alert su continuazione e revoche benefici
    super.getListaTitContSganciate(null);
    super.getListaTitConRevBenSganciati(null);
    // MEV_2025-48 - 2.12 Alert su continuazione e revoche benefici
    
    if (   lDatiAggregati.getPenaRideterminataCumulo()!=null
        || (   lDatiAggregati.getListaDatiFinaliUlterioriSanzioni()!=null
            && lDatiAggregati.getListaDatiFinaliUlterioriSanzioni().size()>0
           )
       )
    {
      return PG_LOAD_DETTAGLIO_PENE_RIDETERMINATE;
    }
    else {
      // carico la funzione di inserimento
      
      if (!lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA)){
        // Se l'istruttoria non è in stato APERTA non consento l'inserimento, ma 
        // visualizzo solo un messaggio
        throw new SIEPException (SIEPException.USER_MESSAGE, "Dati delle Pene Rideterminate non presenti. L'istruttoria non risulta Aperta, non è possibile inserire ulteriori dati.");
      }      
      
      String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadInserisciPeneRideterminate&" 
          + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +getIdIstruttoria();
      return lPage;
    }
    
    
  }  
}
