package siap.siep.modulocumulo.action;

import java.io.ByteArrayOutputStream;

import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActStampaStatoEsecuzioneTitoloCumulato extends ActionModuloCumulo implements ICostantiStatoEsecuzioneCumulo
{
  public String processRequest() throws F3BException 
  {
    //==========================================================================
    // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    TitoloCumulatoModel lTitoloModel = super.getDatiTitoloCumulato();

    TemplateModel lTemMod = new TemplateModel();
    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();

    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(null, null, "0303",null);
    
    
    //==========================================================================
    FascicoloSiepModel lFascicoloModel = null;
    if (   lTitoloModel.getProcedimentoCumulato()!=null 
        && lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine()!=null
       )
    {
      IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
      lFascicoloModel = lCtrlFasc.ExRicercaFascicoloByKey(lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine());
    }
    
    // Recupero l'ufficio di esecuzione del fascicolo per passarlo al osulo di Stampa
    // nell'oggetto UtenteModel utilizzato solo per veicolare tale informazione
    IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
    UfficioModel lUfficioEsecTitolo = lUffCtrl.ExRicercaUfficioByCod (lFascicoloModel.getChiaveUfficio());    
    
    UtenteModel lUtenteMod = new UtenteModel();
    lUtenteMod.setUfficioUtente (lUfficioEsecTitolo);
    
    //==========================================================================
    IStatoEsecTitoloCumulato lCtrl = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaStatoEsecTitolo (lFascicoloModel, lTemMod.getIdTemplate(), lUtenteMod);

    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;  
  }
}
