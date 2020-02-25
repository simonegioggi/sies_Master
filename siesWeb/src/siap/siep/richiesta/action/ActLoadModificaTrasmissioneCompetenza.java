package siap.siep.richiesta.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;


/**
 * <p>Title: ActLoadModificaTrasmissioneCompetenza</p>
 * <p>Description: Classe Action per la load modifica della Trasmissione per Competenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 * @deprecated 19/01/2016 non abilitata sulla tabella FUNZIONE per cui mai testata
 *                        in esercizio. Andrebbe registrata come funzione figlia della
 *                        funzione di DETTAGLIO
 */
public class ActLoadModificaTrasmissioneCompetenza extends ActSIESDettaglioProvvedimento implements ICostantiRichiesta
{
  public String processRequest() throws Exception{    
    //Controllo che non si stia lavorando su una entità in modifica ad altri
    LockModel lck = lockIfNotLocked("richiesta", getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO), getCodUtenteConnesso());
    if (lck != null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
      return IWebConstants.PG_MESSAGE;
    }
    
    boolean proceed = true;
    if (isFascicoloArchiviatoDefinito())
    {
      proceed = false;
      // setta la risposta nella request
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Modificare la Richiesta");
    }
    /*if (!isFascicoloNonValidato()){
      proceed = false;
      // setta la risposta nella request
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il fascicolo è stato validato! Impossibile Effettuare la modifica");
    }*/
    
    if (proceed)
    {
      FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");   
      
      // id dell'evento inserito
      BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
      PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento,lFascMod.getIdFascicoloSiep());
      setRequestAttribute("penaresidua", llPenMod);
      
      // ricerca evento notifica
      IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
      EventoNotificaModel lEveMod = new EventoNotificaModel();
      lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
      this.setRequestAttribute("eventonotifica", lEveMod);
       
      //se l'evento è stato validato non permetto la modifica
      if(lEveMod.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S")){
        setRequestAttribute(IWebConstants.MESSAGE_TEXT, 
            "Il documento è stato validato!<BR>Impossibile procedere con la Modifica");
        
        //Prepara la "pagina" di destinAction
        RedirectTo lRedirigi = new RedirectTo();
        lRedirigi.setPage(IWebConstants.PG_MAIN);
        lRedirigi.setAction("siap.siep.richiesta.action.ActDettaglioTrasmissioneCompetenza&" + 
                            ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEvento);
        
        return IWebConstants.PG_MESSAGE;     
      }

      // Ricerca Magistrato
      IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
      MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
      setRequestAttribute("magistrato", lMagMod);

      /* // notifiche
       for(int i=0;i<lEveMod.getNotifiche().length;i++){
      NotificaModel lNotMod = new NotificaModel();
      lNotMod = lEveMod.getNotifiche()[i];
      
       if(lNotMod != null){       
         setRequestAttribute("noteautoritaEsterna", lNotMod.getNote());
      }     
       }*/
         
      //PREPARO I CAMPI COMPETENZA  
      ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote(); 
      CompetenzaModel mComp = lCompCtrl.ExRicercaCompetenzaByEveIdEvento(lIdEvento);  
           
      if(mComp!=null)  //???
      {      
        //verifico il tipo di inserimento     
        boolean insManuale=true;
      
        if (mComp.getFasSieIdFascicoloSiep()==null) insManuale=false;
        
        if(insManuale){
          FascicoloSiepModel mFascComp = new FascicoloSiepModel();      
          mFascComp.setChiaveUfficio(mComp.getChiaveUfficio());
          mFascComp.setChiaveAnno(mComp.getChiaveAnno());
          mFascComp.setChiaveProgr(mComp.getChiaveProgr());
          
          IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
          FascicoloSiepModel findedFasc = lCtrlFas.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(mFascComp);
    
          ISentenza lCrtlSentenza = SIEPLookupRemote.getSentenzaRemote();
          SentenzaModel aSent = lCrtlSentenza.ExRicercaSentenzaByKey(findedFasc.getSenIdSentenza());
           
          setRequestAttribute("fascCompetenza", findedFasc);
          setRequestAttribute("sentCompetenza", aSent);
        }
        else{
          setRequestAttribute("fascCompetenza", null);
          setRequestAttribute("sentCompetenza", null);
        }
        setRequestAttribute("competenza", mComp);
      }
    
  
      // Posizione giuridica
      PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
      IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
      lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());
  
      if (lPos == null || lPos.getPosizioneGiuridica() == null){
        RedirectTo lRedirigi = new RedirectTo();
        lRedirigi.setPage(IWebConstants.PG_MAIN);
        setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
        lRedirigi.setAction( "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&" +
        ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
        setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
        return IWebConstants.PG_MESSAGE;
      }
    
      setRequestAttribute("posizioneluogoaltra", lPos);
      setRequestAttribute("lcodicePosizione", lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());
      //fine posizione giuridica
    
      //Pena Residua
      PenaResiduaModel lPenaResMod = new PenaResiduaModel();
      IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
      lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
        
      //tipo richiesta      
      Option lOption = new Option(DecodificheManager.getInstance().getTipoRichiestaTrasmComp(), lEveMod.getEvento().getCodTipoProvvedimento());
      setRequestAttribute("richiesta", "" + lOption);
      
      //richiesta oggetto
      lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentiRichGen(), lEveMod.getEvento().getCodMotivo()); 
      //lOption.setFilter( new String[] {"5134", "5132", "5133"}); 06/04/2010 Revisione Codici Motivo per Pene Accessorie.
      lOption.setFilter( new String[] {"5404", "5402", "5403"});
      setRequestAttribute("oggetto","" + lOption);      
               
      //TIPO PROVVEDIMENTO      
      Option lTipoProvv =  new Option(DecodificheManager.getInstance().getTipoProvvedimenti(), mComp.getCodTipoProvvedimento());
      lTipoProvv.setFilter( new String[] {"-", "01", "02"});
      setRequestAttribute("tipoprovvedimento",""+ lTipoProvv);
      
      //UFFICIO EMITTENTE
      lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), mComp.getCodTipoAutoritaEmittente());      
      setRequestAttribute("autoritaEmi", "" + lOption );
      
      // ALTRO DESTINATARIO
      Option lAEOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
      lEveMod.getNotifiche()[0].getNote());   
      setRequestAttribute("autoritaEsternaN", "" + lAEOption);
        
      //UFFICIO PM
      lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM(), mComp.getCodTipoAutoritaComp());      
      setRequestAttribute("ufficioPM", "" + lOption );
        
      setRequestAttribute("penaresidua", lPenaResMod);
      setRequestAttribute("titolo","MODIFICA TRASMISSIONE PER COMPETENZA"); 
      setRequestAttribute("modalita", "M");
    
    
      return PG_LOAD_MODIFICA_TRASMISSIONE_COMP;
    }
    else
    {
      //Prepara la "pagina" di destinAction
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" +
        ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
    }
  }
}