package siap.siep.modulocumulo.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;


/**
 * Action che effettua il caricamento della lista delle MS caricate sui vari titoli
 * consentendo all'utente di selezionare quale MS vuole includere nel cumulo e 
 * se vuole generare subito il procedimento di classe IV.
 * 
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadSelezionaMisuraSicurezzaDatiFinali extends ActionModuloCumulo implements ICostantiModuloCumulo
{  
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaMdel = super.getDatiIstruttoria();
    super.getDatiFinaliCumuloAggregato();  

    //==========================================================================
    // Effettua la ricerca delle MisuraSicurezzaCumuloModel con i dati del Titolo
    // relativo collegato
    //==========================================================================
    Vector <MisuraSicurezzaCumuloModel> lElencoMisure = new Vector <MisuraSicurezzaCumuloModel>();
    IMisuraSicurezzaCumulo lCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
    
    lElencoMisure = lCtrl.ExRicercaMisureSicurezzaCumuloByIdIstruttoria (lIstruttoriaMdel.getIdIstruttoriaCumulo(),false);
   
    setRequestAttribute("ElencoMisureSicurezzaInIstruttoria", lElencoMisure);

    
    // Uffici accorpati per Ufficio corrente
//    IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
//    Vector lUffAcc = lUffCtrl.ListaUfficiAccorpati("PM", getCodUfficioUtenteConnesso());
//    setRequestAttribute("elencoUfficiAccorpati", lUffAcc);    
    
    
    //==========================================================================
    // Recupero se presenti eventuali procedimenti di Classe IV dell'ufficio
    // a carico dello stesso soggetto E legati a uno dei procedimenti cumulati.
    //
    // Ovvero: deve essere un classe IV caricato in cumulo oppure un classe IV
    //         legato a un procedimento caricato in cumulo
    //==========================================================================
    Vector <ProcedimentoCumulatoModel> lProcedimentiClasseIV = new Vector <ProcedimentoCumulatoModel>();
    IDatiFinaliCumulo lCtrlDatiFinali = SIEPLookupRemote.getDatiFinaliCumuloRemote();
    
    lProcedimentiClasseIV = lCtrlDatiFinali.ExRicercaProcedimentiClasseIVPerRibaltamentoByIdIstru (lIstruttoriaMdel.getIdIstruttoriaCumulo()
                                                                                                  ,getCodUfficioUtenteConnesso());
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Procedimenti Classe IV trovati: "+lProcedimentiClasseIV.size());
    setRequestAttribute("ElencoProcedimentiMS", lProcedimentiClasseIV);
    
    
    return PG_LOAD_SELEZIONA_MISURE_SICUREZZA;
  } 
}