package siap.siep.sanzionesostitutiva.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
 * <p>Title: ActLoadAnnotazioneAffidamento</p>
 * <p>Description: Classe Action per la load inserisci di Annotazione Concessione Affidamento</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 3.0
 */

public class ActLoadAnnotazioneAffidamento extends ActionSiap
{
  public String processRequest() throws F3BException
  {
      if (this.isSessionAttributeNullObj("fascicolo"))
         return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
	  
      FascicoloSiepModel lFascMod =(FascicoloSiepModel)getSessionAttribute("fascicolo");
      
      this.isFascicoloSiepDiCompetenza();

      if(isFascicoloNonValidato())
          return IWebConstants.PG_MESSAGE;
  
      PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
      IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
      lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

      if (notEsistePosizioneGiuridica(lPos))
        return IWebConstants.PG_MESSAGE;
      
      if(!lPos.getPosizioneGiuridica().isLibero() && !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("19"))
      {
          setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Non è possibile effettuare l'Annotazione Affidamento in prova!Posizioni giuridiche permesse: Libero o Espiazione Pena Sostitutiva (Semidetenzione)");
          return IWebConstants.PG_MESSAGE;    
      }
      
   
      PenaResiduaModel lPenaResMod = new PenaResiduaModel();
      IPenaResidua IPenRes=SIEPLookupRemote.getPenaResiduaRemote();
      lPenaResMod = IPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

      if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
           return IWebConstants.PG_MESSAGE;     
   
      ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
      SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),"S");
      
      if(lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null)
      { 
    	 lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),"N");      	  
      }

 
      // Inserisco la SS residua nel model della PR
      lPenaResMod.setSanzSostResidua(lSSResiduaModel); 
      
      if( lPenaResMod == null ||
    	  lPenaResMod.getCodTipoSanzione()== null ||
	     !lPenaResMod.getCodTipoSanzione().equals("S"))
      {	    
          setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Non è possibile effettuare l'Annotazione Affidamento in prova!Sanzione Sostitutiva permessa: Semidetenzione");
          return IWebConstants.PG_MESSAGE; 
      }
      
      setRequestAttribute("lFlagSanzione","S");
      
      String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.misuraalternativa.action.ActMisuraAlternativaGrigliaAffInPro";
      return lPage;
  }
}