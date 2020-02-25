package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
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
 * <p>Title: ActPerditaEfficacia</p>
 * <p>Description: Classe Action per il padre della perdita efficacia</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActPerditaEfficacia extends ActMisuraAlternativa  implements ICostantiMisuraAlternativa
{
  protected String getPerditaEfficacia() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    if(isFascicoloNonValidato())
      return IWebConstants.PG_MESSAGE;

    isFascicoloSiepDiCompetenza();

    if (isFascicoloArchiviatoDefinito())
      return IWebConstants.PG_MESSAGE;

//Posizione giuridica
     PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
     IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
     lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

     if (notEsistePosizioneGiuridica(lPos))
       return IWebConstants.PG_MESSAGE;

     setRequestAttribute("posizioneluogoaltra", lPos);
     setRequestAttribute("lcodicePosizione", lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());

//Controllo Esistenza pena residua per quel fascicolo
     PenaResiduaModel lPenaResMod = new PenaResiduaModel();
     IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
     lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

     if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
       return IWebConstants.PG_MESSAGE;

     if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
       setRequestAttribute("dataeditabile", "S");

     setRequestAttribute("penaresidua", lPenaResMod);

//ricerca magistrato competente
    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
    if (lMagMod != null)
       setRequestAttribute("magistratocompetente", lMagMod);

//Autorità esterna E
    Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

//Autorità esterna N
    Option lOptionAutoritaN = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutoritaN", "" + lOptionAutoritaN);

//Autorità esterna C
    Option lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutoritaC", "" + lOptionAutoritaC);

    return "";
  }
}