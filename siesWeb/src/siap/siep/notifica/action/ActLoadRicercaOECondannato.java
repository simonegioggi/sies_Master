package siap.siep.notifica.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;


/**
* <p>Title: ActLoadRicercaOECondannato</p>
* <p>Description: Classe Action per la ricerca di Scadenzario Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaOECondannato extends ActionSiap 
                                        implements ICostantiNotifica
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

    EventoNotificaModel lEveMod = new EventoNotificaModel();

/*
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    lEveMod = lCtrl.ExRicercaEventoNotificaCondannatoByKey(lFascMod.getIdFascicoloSiep());
*/

    IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();

/*    
    EventoNotificaModel lEveNotIrreperibilita = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(),"8BIS");
    if( lEveNotIrreperibilita != null && lEveNotIrreperibilita.getEvento() != null && lEveNotIrreperibilita.getEvento().getIdEvento() != null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Esiste un Decreto di Irreperibilità. Impossibile annotare la notifica al condannato.");
*/
    
    lEveMod = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(), "LS");

    if(lEveMod == null || lEveMod.getEvento() == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessun OE con Sospensione.");
/*
    if(lEveMod.getNotifiche() == null || lEveMod.getNotifiche().length == 0)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessuna Notifica Associata all'OE con Sospensione.");
/
    /******************************* Posizione Giuridica **********************************/
    IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaModel lPosizione  = lCtrlPosGiu.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
    if (lPosizione == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

    if( "02".equals(lPosizione.getCodPosizioneGiuridica()) ||  "04".equals(lPosizione.getCodPosizioneGiuridica()))
      throw new SIEPException(SIEPException.USER_MESSAGE, "La Posizione Giuridica Detenuto per questa causa in regime di arresti domiciliari non permette di annotare la notifica al Condannato.");
      
    setRequestAttribute("eventonotifica", lEveMod);
    
    int conta = 0;
//    IScadenzario lCtrlSca = SIEPLookupRemote.getScadenzarioRemote();
//    ScadenzarioModel lScaMod = new ScadenzarioModel();
    String lEsisteNotifica="N";
    while (conta < lEveMod.getNotifiche().length)
    {
      NotificaModel lNotMod = new NotificaModel(lEveMod.getNotifiche()[conta]);
      if(lNotMod != null && lNotMod.getCodTipoNotifica() != null && lNotMod.getCodTipoNotifica().equals("E"))
      {
//        lScaMod = lCtrlSca.ExRicercaScadenzarioCorrenteByIdFascicoloIdNotifica(lFascMod.getIdFascicoloSiep(),lNotMod.getIdNotifica(),"01");
        setRequestAttribute("notifica", lNotMod);
        lEsisteNotifica ="S";
      }
      conta++;
    }
    
    if("N".equals(lEsisteNotifica))
    {  	
    	//in caso di mancata notifica -- Dario - Paolo - 18/03/09
 	
    	  NotificaModel lNotMod = new NotificaModel();
    	  lNotMod.setCodTipoNotifica("E");
    	  lNotMod.setDataInvio(lEveMod.getEvento().getDataEmissione());
    	  lNotMod.setEveIdEvento(lEveMod.getEvento().getIdEvento());
    	  lNotMod.setCodEsito("-");
    	  lNotMod.setCodOperatoreInserimento(lEveMod.getEvento().getCodOperatoreInserimento());
    	  lNotMod.setDataInserimento(DateUtils.getSysDate());
    	  lNotMod.setCodUfficioInserimento(lEveMod.getEvento().getCodUfficioInserimento());
   	      lNotMod.setIstDetIdIstitutoDetenzione("");
  	      lNotMod.setAutoritaEsterna(null);   	
  	      INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
  	      lNotMod = lCtrlNot.ExInserisciNotifica(lNotMod);
  	      setRequestAttribute("notifica", lNotMod);
    	  	
  	     
        //throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessuna Notifica Associata all'OE con Sospensione.");
    }
    
//    setRequestAttribute("scadenzario", lScaMod);

    Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),"-");
    setRequestAttribute("autoritaEsternaDelegata", "" + lOption);

		return PG_LOAD_DETTAGLIO_NOTIFICA_CONDANNATO;  //restituisce la jsp di VIEW
	 }
}