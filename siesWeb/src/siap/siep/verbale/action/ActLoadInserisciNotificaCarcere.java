package siap.siep.verbale.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;



/**
* <p>Title: ActLoadInserisciNotificaCarcere</p>
* <p>Description: Classe Action per la load inserisci di Notifica carcere</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciNotificaCarcere extends ActionSiap implements ICostantiVerbale
{
  /**
   * La funzione consente di registrare la Notifica DEL Carcere in cui comunica
   * all'esecuzione la data di fine pena altra causa. Tale data +1g viene utilizzata
   * per determinare la data inizio pena questa causa e quindi decorrenza/scadenza.
   * 
   * La funzione era aperta solo per posizione:
   * 24 - Espiazione Pena Definitiva in Carcere
   * E' stata aperta per tutte le posizioni altra causa in seguito MEV A7RR308
   * v. 3.0
   */
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

    this.isFascicoloSiepDiCompetenza();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    if(isFascicoloNonValidato())
      return IWebConstants.PG_MESSAGE;

    if (isFascicoloArchiviatoDefinito())
     return IWebConstants.PG_MESSAGE;

    this.isEventoNonValidato();

    PenaResiduaModel lPenMod = new PenaResiduaModel();
    IPenaResidua IPenRes=SIEPLookupRemote.getPenaResiduaRemote();
    lPenMod = IPenRes.ExRicercaPenaResiduaUltimaByDate_IgnoraValidazione(lFascMod.getIdFascicoloSiep());

    if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenMod))
      return IWebConstants.PG_MESSAGE;

    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

    if (notEsistePosizioneGiuridica(lPos))
      return IWebConstants.PG_MESSAGE;

    String lPosizione = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica();
   
    String msgTxt = "";
    
    //Controllo Evento Ordine Esecuzione
    if(   lPosizione != null && lPos.getPosizioneGiuridica().isLibero()
       && (lFascMod.getFlagAltraCausa()!=null && lFascMod.getFlagAltraCausa().equals("S"))
      )
    {
      // CodTipoPosGiuridica per altra causa deve essere: Espiazione Pene Definitiva in Carcere (24)
      // MEV - A7RR308, sblocco anche per le altre posizioni
//      if ((lPos.getAltraCausa().getCodTipoPosGiuridica().equals("24"))) 
//     { 
        IVerbale lEve = SIEPLookupRemote.getVerbaleRemote();
        lEve.ExRicercaEventoVerbale(lFascMod.getIdFascicoloSiep(), "Nessun Documento registrato in relazione alla Notifica Carcere");
//      }
//      else
//      { 
//        msgTxt = "Non è possibile inserire Notifica Carcere, il soggetto non è in Espiazione Pena Definitiva in Carcere per altra causa.";
//      }
    }
    else
    {
      if(!(lFascMod.getFlagAltraCausa()!=null && lFascMod.getFlagAltraCausa().equals("S")))
        msgTxt = "Non è possibile inserire Notifica Carcere, il soggetto non risulta Detenuto per Altra Causa.";
      else
        msgTxt = "Non è possibile inserire Notifica Carcere per questa Posizione Giuridica";
    }
   
    if (msgTxt.length() > 0) {
      
      RedirectTo lRedirigi = new RedirectTo();

      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  msgTxt);
      lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
                          ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }



    // Imposta Modalità.
    setRequestAttribute("modalita", "I");

    return PG_LOAD_INSERISCINOTIFICA_CARCERE;  //restituisce la jsp di VIEW

  }

}