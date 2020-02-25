package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.util.F3BException;

import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActDettaglioAltreSanzioni extends ActionModuloCumulo implements ICostantiModuloCumulo
{
  public String processRequest() throws F3BException
  {
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
    DatiFinaliCumuloAggregatoModel lDatiFinaliAgg  = super.getDatiFinaliCumuloAggregato();  

    
    //=============================================================
    // Recupera le Misure di sicurezza selezionate x i Dati Finali
    //=============================================================
    Vector <MisuraSicurezzaCumuloModel> lElencoMisure = new Vector <MisuraSicurezzaCumuloModel>();
    IMisuraSicurezzaCumulo lCtrlMisura = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
    
    lElencoMisure = lCtrlMisura.ExRicercaMisureSicurezzaCumuloByIdIstruttoria (lIstruttoriaModel.getIdIstruttoriaCumulo(),true);

    setRequestAttribute("ListaMisureSicurezza", lElencoMisure);   
    
    if (lDatiFinaliAgg.getDatiFinaliCumulo().getFasSieIdFascicoloSiepMs()!=null)
    {
      IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
      
      FascicoloSiepModel lFascModel = lCtrlFasc.ExRicercaFascicoloByKey (lDatiFinaliAgg.getDatiFinaliCumulo().getFasSieIdFascicoloSiepMs());
      
      setRequestAttribute("fascicoloEsecMS", lFascModel);  
    }
    
    //=============================================================
    // Recupera le Pene Accessorie selezionate x i Dati Finali
    //=============================================================
    Vector <PenaAccessoriaCumuloModel> lElencoPeneAcc = new Vector <PenaAccessoriaCumuloModel>();
    IPenaAccessoriaCumulo lCtrlPA = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
    
    lElencoPeneAcc = lCtrlPA.ExRicercaPenaAccessoriaCumuloByIdIstruttoria (lIstruttoriaModel.getIdIstruttoriaCumulo(),true);

    setRequestAttribute("ListaPeneAccessorie", lElencoPeneAcc);   
    
    
    return PG_LOAD_DETTAGLIO_ALTRE_SANZIONI;
  }  
}
