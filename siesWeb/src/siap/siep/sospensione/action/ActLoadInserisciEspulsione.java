package siap.siep.sospensione.action;

/**
 * <p>Title: ActLoadInserisciEspulsione</p>
 * <p>Description: Classe Action per la load inserimento della Sospensione  di Espulsione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

public class ActLoadInserisciEspulsione extends ActionSiap implements ICostantiSospensione
{
  public String processRequest() throws Exception
  {
//Controllo Presenza del Fascicolo in Sessione
    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    if(isFascicoloNonValidato())
      return IWebConstants.PG_MESSAGE;

    isFascicoloSiepDiCompetenza();

    if (isFascicoloArchiviatoDefinito())
      return IWebConstants.PG_MESSAGE;

    this.isEventoNonValidato();

// Posizione Giuridica
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

    if (notEsistePosizioneGiuridica(lPos))
      return IWebConstants.PG_MESSAGE;

    setRequestAttribute("posizioneluogoaltra", lPos);

//posizione giuridiche consentite per la funzione
    if(lPos != null && lPos.getPosizioneGiuridica() != null &&
       !lPos.getPosizioneGiuridica().isMisuraAlternativa() && !"03".equals(lPos.getPosizioneGiuridica().getCodPosizioneGiuridica())        
       && !"02".equals(lPos.getPosizioneGiuridica().getCodPosizioneGiuridica())
       && !"04".equals(lPos.getPosizioneGiuridica().getCodPosizioneGiuridica()))
    {
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Attenzione: posizione giuridica non corretta per questa funzione.");
     lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" +
       ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
     setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

     return IWebConstants.PG_MESSAGE;
    }

// Pena Complessiva
    IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

    if (lPenComMod == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Pena Complessiva non presente. Impossibile eseguire la richiesta.");

//Pena Residua
    PenaResiduaModel lPenMod = new PenaResiduaModel();
    IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
    lPenMod = IPenRes.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());
    //lPenMod = IPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
   
    if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenMod))
      return IWebConstants.PG_MESSAGE;

    setRequestAttribute("penaresidua", lPenMod);

//tipo autorità
    Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(),"20");
    setRequestAttribute("tipoAutorita", "" + lOptionAutorita );

    return PG_LOAD_INSERISCI_SOSPENSIONE_ESPULSIONE;  //restituisce la jsp di VIEW
  }
}