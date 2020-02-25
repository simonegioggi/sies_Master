package siap.siep.richiesta.action;


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
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;




/**
 * <p>Title: ActLoadInserisciEsitoEspulsione</p>
 * <p>Description: Classe Action per la load inserisci della richiesta esito espulsione</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciEsitoEspulsione extends ActionSiap
                                                    implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {  
    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    this.isEventoNonValidato();

    if(isFascicoloNonValidato())
        return IWebConstants.PG_MESSAGE; 
     
    this.isFascicoloSiepDiCompetenza();

    if (isFascicoloArchiviatoDefinito())
        return IWebConstants.PG_MESSAGE;

//Posizione giuridica
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

    if (notEsistePosizioneGiuridica(lPos))
      return IWebConstants.PG_MESSAGE;

    setRequestAttribute("posizioneluogoaltra", lPos);
  
//Controllo Esistenza pena residua non validata per quel fascicolo
    PenaResiduaModel lPenaResMod = new PenaResiduaModel();
    IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

    if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
      return IWebConstants.PG_MESSAGE;

    if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
      setRequestAttribute("dataeditabile", "S");

    setRequestAttribute("penaresidua", lPenaResMod);


//ricerca magistrato firmatario
    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
    if (lMagMod != null)
       setRequestAttribute("magistratocompetente", lMagMod);

 //Autorità esterna
     Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
     setRequestAttribute("autorita", "" + lOption);

    return  PG_LOAD_ESITO_ESPULSIONE;
  }

}