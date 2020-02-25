package siap.siep.sanzionesostitutiva.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
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
import f3b.web.html.Option;


/**
 * <p>Title: ActLoadRichiestaRevSS</p>
 * <p>Description: Classe Action per la load inserisci Richiesta Revoca Sanzione Sostitutiva</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 1.0
 */

public class ActLoadRichiestaRevSS extends ActionSiap implements ICostantiSanzioneSostitutiva
{
  public String processRequest() throws F3BException
  {
	  
	  if (this.isSessionAttributeNullObj("fascicolo"))
		  return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
	  
	  FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

	  if(isFascicoloNonValidato())
	      return IWebConstants.PG_MESSAGE;
	  
	  isFascicoloSiepDiCompetenza();
	  this.isEventoNonValidato();

	  //Posizione giuridica
	  PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
	  IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
	  lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

	  if (notEsistePosizioneGiuridica(lPos))
		  return IWebConstants.PG_MESSAGE;

	  setRequestAttribute("posizioneluogoaltra", lPos);
	 
	  // Controllo Esistenza pena residua per quel fascicolo
	  PenaResiduaModel lPenaResMod = new PenaResiduaModel();
	  IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
	  lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

	  if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
		  return IWebConstants.PG_MESSAGE;

      //ricerco le sanzione sostitutive
      ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
      SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),"S");

      // Inserisco la SS residua nel model della PR
      if(lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null)
      {
          lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),"N"); 	
      }
      
      lPenaResMod.setSanzSostResidua(lSSResiduaModel);
  
	  setRequestAttribute("penaresidua", lPenaResMod);    
      
   
	  //Magistrato 
      IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
      MagistratoCompetenteMagistratoModel  lMagi = lMagCtrl.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
      setRequestAttribute("magistrato", lMagi);
	  
      //autorità del ge
      Option lOptionAE = new Option(DecodificheManager.getInstance().getTipoUfficio());
      lOptionAE.setFilter( new String[] {"CAP","DIB","GUP","GIP","CAS","CASAP","TRIBSD"} );
      setRequestAttribute("autoritaGe", "" + lOptionAE);

//    setto il campo codice motivo 
	  Option lOption = new Option( DecodificheManager.getInstance().getMotivoRevocaSS());
	  lOption.setFilter( new String[] {"0261"} ); 
      setRequestAttribute("motivoProvv" ,"" + lOption);	
      
	  return PG_LOAD_INSERISCI_RICHIESTA_REVOCA;	  
  }
}