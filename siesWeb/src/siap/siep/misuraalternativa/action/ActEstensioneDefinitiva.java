package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.model.EventoNotificaModel;
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
 * <p>Title: ActLoadInserisciEstensioneDefAffProva</p>
 * <p>Description: Classe Action per la load inserisci di MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActEstensioneDefinitiva extends ActMisuraAlternativa implements ICostantiMisuraAlternativa
{
  protected String getEstensioneDefinitiva() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    if(isFascicoloNonValidato())
     return IWebConstants.PG_MESSAGE;

    isFascicoloSiepDiCompetenza();

    if (isFascicoloArchiviatoDefinito())
     return IWebConstants.PG_MESSAGE;

 //Posizione Giuridica
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

    if (notEsistePosizioneGiuridica(lPos))
      return IWebConstants.PG_MESSAGE;

    setRequestAttribute("posizioneluogoaltra", lPos);

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

//Autorità esterna C
    Option lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita());
    setRequestAttribute("codiceAutoritaC", "" + lOptionAutoritaC);

    Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), "TDS");
    setRequestAttribute("tipoUfficio", "" + lOption);

    UtenteModel lUtenteConnesso = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    String StrCodiceDistrettoUtente = lUtenteConnesso.getUfficioUtente().getDescrComune();

    setRequestAttribute("distretto", StrCodiceDistrettoUtente);

    return "";
  }

  protected EventoNotificaModel getCodiceMotivoTipoEventoEstensione(String tipoMisura,String codiceMotivo) throws F3BException
  {
    EventoNotificaModel lEveNot = new EventoNotificaModel();
    if( tipoMisura != null && (tipoMisura.equals("AFFIDAMENTO")) )
    {
      lEveNot.getEvento().setCodTipoProvvedimento("12");
      if("0092".equals(codiceMotivo))
       lEveNot.getEvento().setCodMotivo("0391");
      else if("0093".equals(codiceMotivo))
       lEveNot.getEvento().setCodMotivo("0235");
      else if("0020".equals(codiceMotivo))
       lEveNot.getEvento().setCodMotivo("0236");
    }
    else if( tipoMisura != null && (tipoMisura.equals("DETENZIONE")) )
    {
      lEveNot.getEvento().setCodTipoProvvedimento("12");
      if("0102".equals(codiceMotivo))
       lEveNot.getEvento().setCodMotivo("0392");
      else if("0103".equals(codiceMotivo))
       lEveNot.getEvento().setCodMotivo("0237");
      else if("0101".equals(codiceMotivo))
       lEveNot.getEvento().setCodMotivo("0238");
      else if("0095".equals(codiceMotivo))
       lEveNot.getEvento().setCodMotivo("0239");
      else if("0100".equals(codiceMotivo))
       lEveNot.getEvento().setCodMotivo("0240");
    }
    else if( tipoMisura != null && (tipoMisura.equals("SEMILIBERTA")) )
    {
      lEveNot.getEvento().setCodTipoProvvedimento("12");
      lEveNot.getEvento().setCodMotivo("0393");
    }
    else if( tipoMisura != null && (tipoMisura.equals("AFFIDAMENTOCUMULO")) )
    {
      lEveNot.getEvento().setCodTipoProvvedimento("12");
      if("0020".equals(codiceMotivo))
       lEveNot.getEvento().setCodMotivo("0450");
      else if("0092".equals(codiceMotivo))
       lEveNot.getEvento().setCodMotivo("0242");
      else if("0093".equals(codiceMotivo))
       lEveNot.getEvento().setCodMotivo("0243");

    }
    else if( tipoMisura != null && (tipoMisura.equals("DETENZIONECUMULO")) )
    {
      if(codiceMotivo.equals("0095"))
      {
        lEveNot.getEvento().setCodTipoProvvedimento("12");
        lEveNot.getEvento().setCodMotivo("0451");
      }
      else if(codiceMotivo.equals("0100"))
      {
        lEveNot.getEvento().setCodTipoProvvedimento("12");
        lEveNot.getEvento().setCodMotivo("0452");
      }
      else if(codiceMotivo.equals("0103"))
      {
        lEveNot.getEvento().setCodTipoProvvedimento("12");
        lEveNot.getEvento().setCodMotivo("0453");
      }
      else if(codiceMotivo.equals("0101"))
      {
        lEveNot.getEvento().setCodTipoProvvedimento("12");
        lEveNot.getEvento().setCodMotivo("0458");
      }
      else if(codiceMotivo.equals("0102"))
      {
        lEveNot.getEvento().setCodTipoProvvedimento("12");
        lEveNot.getEvento().setCodMotivo("0459");
      }
      else
      {
        lEveNot.getEvento().setCodTipoProvvedimento("04");
        lEveNot.getEvento().setCodMotivo(codiceMotivo);
      }
    }
    else if( tipoMisura != null && (tipoMisura.equals("SEMILIBERTACUMULO")) )
    {
      lEveNot.getEvento().setCodTipoProvvedimento("12");
      lEveNot.getEvento().setCodMotivo("0454");
    }
    else
    {
      lEveNot.getEvento().setCodTipoProvvedimento("04");
      lEveNot.getEvento().setCodMotivo(codiceMotivo);
    }
    return lEveNot;
  }
}