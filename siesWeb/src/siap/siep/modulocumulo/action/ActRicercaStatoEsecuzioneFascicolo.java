package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.util.F3BException;

import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action preposta al caricamento dello stato di esecuzione completo del Fascicolo
 * assorbito in cumulo.
 * n.b. lavora sul FASCIOLO_SIEP preseo in carico
 * 
 * Restituisce una popup con lo stato esecuzione originario da cui l'utente può 
 * selezionare gli EVENTI da caricare in istruttoria.
 * 
 * Se un evento è già in istruttori va marcato opportunamente.
 * 
 * @author d.fiorletta
 *
 */
public class ActRicercaStatoEsecuzioneFascicolo extends ActionModuloCumulo implements ICostantiStatoEsecuzioneCumulo
{

  public String processRequest() throws F3BException {
    //==========================================================================
    // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    TitoloCumulatoModel lTitoloModel = super.getDatiTitoloCumulato();

    //==========================================================================
    // Recupero lo stato esecuzione del fascicolo originario
    //==========================================================================
    ITitoloCumulato lTitoloCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();
    ProcedimentoCumulatoModel lProcCumModel = lTitoloCtrl.ExRicercaProcedimentoCumulatoByIdTitolo (lTitoloModel.getIdTitoloCumulato());
    Vector <MisuraAlternativaAggregatoModel> lListaEventi = null;
    if (lProcCumModel!=null && lProcCumModel.getIdFascicoloSiepOrigine()!=null){
      lListaEventi = lTitoloCtrl.ExRicercaEventiPerStatoEsecuzioneByFascicoloSiepPaged (lProcCumModel.getIdFascicoloSiepOrigine(),0);
    }
    setRequestAttribute("StatoEsecuzioneFascicolo", lListaEventi);
    
    //==========================================================================
    // Recupero lo stato esecuzione Caricato in istruttoria
    //==========================================================================
    IStatoEsecTitoloCumulato lCtrlSET = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    Vector <StatoEsecTitoloCumulatoModel> lListaEventiSET= lCtrlSET.ExRicercaStatoEsecTitoloCumulatoByIdTitolo(lTitoloModel.getIdTitoloCumulato());
    setRequestAttribute("StatoEsecuzioneCaricato", lListaEventiSET); 
    
    //Passo i dati alla popup
    
    return PG_POPUP_STATO_ESEC_FASCICOLO;
  }
  
}
