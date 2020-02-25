package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
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
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciRipristinoDetCarc</p>
* <p>Description: Classe Action per la load inserisci di Ripristino Detenzione in carcere</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciRipristinoDetCarc extends ActionSiap implements ICostantiMisuraAlternativa
{
 public String processRequest() throws F3BException
  {

    if (this.isSessionAttributeNullObj("fascicolo"))
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    if(isFascicoloNonValidato())
      return IWebConstants.PG_MESSAGE;

    isFascicoloSiepDiCompetenza();

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

   //REGOLE PER DIFFERENZIARE L'USO DELLA FUNZIONE
   String lMessaggio = null;
   String lMessaggioProsegue = null;
   if("20".equals(lPosizione)) //Evaso
   {
     lMessaggio = "Attenzione : Occorre registrare il ripristino dell'esecuzione dalla funzione interruzioni ripristino esecuzione.";
   }
   else if(  "07".equals(lPosizione) || "10".equals(lPosizione) //libero
          || "16".equals(lPosizione)  //libero in differimento
          || "17".equals(lPosizione)  //libero in differimeto provvisorio
          || "26".equals(lPosizione)) //espulso
  {
    lMessaggio = "Attenzione : Occorre utilizzare la funzione verbale di arresto.";
  }
  else if("13".equals(lPosizione) || "54".equals(lPosizione)) //Affidamento in prova e Affidamento in prova provvisorio
  {
    lMessaggioProsegue = "Attenzione : Se vi è stato provvedimento di revoca dell'affidamento in prova verificare "+
                         "prima se si è annotata la revoca con l'apposita funzione.";
  }
  else if("29".equals(lPosizione)) //detenzione domiciliare provvisoria
  {
    lMessaggioProsegue = "Attenzione : Se vi è stato provvedimento di revoca della Detenzione Domiciliare Provvisoria "+
                         "verificare prima se si è annotata la revoca con l'apposita funzione.";
  }


   if (this.isRequestParameterNullObj("warning_2"))
   {
     if (lMessaggioProsegue != null)
     {
       //set goto page set flag misura
       setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
       setRequestAttribute( IWebConstants.MESSAGE_TEXT, lMessaggioProsegue);

       return "/jsp/files/siap/siep/misuraalternativa/WarningMisura.jsp";
     }
   }

   if(lMessaggio != null)
   {
     RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage(IWebConstants.PG_MAIN);
     setRequestAttribute(IWebConstants.MESSAGE_TEXT, lMessaggio);
                         lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" +
                         ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
     setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
     return IWebConstants.PG_MESSAGE;
   }

    Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutoritaArresto());
    setRequestAttribute("tipoAutorita", "" + lOptionAutorita );

    return PG_LOAD_INSERISCI_MA_RIPRISTINO_DET_CARC;  //restituisce la jsp di VIEW

   }
}