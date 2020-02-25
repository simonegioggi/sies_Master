package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
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
 * <p>Title: ActRipristino</p>
 * <p>Description: Classe Action per il padre del Ripristino</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActRipristino extends ActMisuraAlternativa implements ICostantiMisuraAlternativa
{
  protected String getRipristino() throws F3BException
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
    if(lMagMod!= null )
    setRequestAttribute("magistratocompetente", lMagMod);

    //Autorità esterna E
    Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

    //Autorità esterna C
    Option lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutoritaC", "" + lOptionAutoritaC);

    UtenteModel lUtenteConnesso = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    String StrCodiceDistrettoUtente = lUtenteConnesso.getUfficioUtente().getDescrComune();

    setRequestAttribute("distretto", StrCodiceDistrettoUtente);

    return "";
  }

  protected String getChangeMotivo(String aCodMotivo) throws F3BException
  {
    String lCodMotivoCambiato = null;
    if (aCodMotivo.equals("0014"))
      lCodMotivoCambiato = "0304";
    else
    if (aCodMotivo.equals("0015"))
      lCodMotivoCambiato = "0305";
    else
    if (aCodMotivo.equals("0086"))
      lCodMotivoCambiato = "0306";
    else
    if (aCodMotivo.equals("0016"))
      lCodMotivoCambiato = "0307";
    else
    if (aCodMotivo.equals("0087"))
      lCodMotivoCambiato = "0308";
    else
    if (aCodMotivo.equals("0089"))
      lCodMotivoCambiato = "0309";
    else
    if (aCodMotivo.equals("0091"))
      lCodMotivoCambiato = "0310";
    else
    if (aCodMotivo.equals("0088"))
      lCodMotivoCambiato = "0410";
    else
    if (aCodMotivo.equals("0196"))
    	lCodMotivoCambiato = "0311";
    // 17/11/2010	Stampa Ripristino Espiazione Pena presso Domicilio   05/10/2011 Anche Espiazione Pena presso Domicilio da UDS.
    if (aCodMotivo.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO) || 
    		aCodMotivo.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO_DAUDS))
    	lCodMotivoCambiato = "0475";

    return lCodMotivoCambiato;
  }
}