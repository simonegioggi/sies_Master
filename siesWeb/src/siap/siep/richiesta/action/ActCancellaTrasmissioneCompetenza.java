package siap.siep.richiesta.action;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActCancellaTrasmissioneCompetenza extends ActionSiap implements ICostantiRichiesta{

	 public String processRequest() throws Exception
	  {
	    boolean proceed=true;
	    FascicoloSiepModel lFascMod=new FascicoloSiepModel();

	    lFascMod.setIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() );
	    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
	    lFascMod=lCtrl.ExRicercaFascicoloByKey(lFascMod.getIdFascicoloSiep());
/*	    if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")){
	      proceed=false;
	      // setta la risposta nella request
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile effettuare la cancellazione!");
	    }
	    if (lFascMod.getFlagValidato().equalsIgnoreCase("S")){
	      proceed=false;
	      // setta la risposta nella request
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Il fascicolo è stato validato! Impossibile effettuare la cancellazione!");
	    }
*/	    
	    if (proceed){
	    	IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
	    	
	        EventoNotificaModel lEveMod = new EventoNotificaModel();
	        lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
	    	
	        //se l'evento è stato validato non permetto la cancellazione
	    	 if(lEveMod.getEvento().getFlagDocumentoRegistrato()!=null && lEveMod.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S")){
	    		 setRequestAttribute(IWebConstants.MESSAGE_TEXT, 
	    				 "Il documento è stato validato!<BR>Impossibile procedere con la Cancellazione");
	    		 //Prepara la "pagina" di destinAction
	    	      RedirectTo lRedirigi = new RedirectTo();
	    	      lRedirigi.setPage(IWebConstants.PG_MAIN);
	    	      lRedirigi.setAction("siap.siep.richiesta.action.ActDettaglioTrasmissioneCompetenza&" + 
	    	    		  ICostantiEvento.CAMPO_ID_EVENTO + "=" + this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

	    	      return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW    		 
	    	 }
	        
	        
	        IOrdineEsecuzione lCtrlOE = SIEPLookupRemote.getOrdineEsecuzioneRemote();
	        lCtrlOE.ExCancellaEventoConStoreProcedure(lEveMod.getEvento());
	        

	    String lPage="";
	      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() ;
	      return lPage; //restituisce la jsp di VIEW
	  }

	      else
	      {
	        //Prepara la "pagina" di destinAction
	        RedirectTo lRedirigi = new RedirectTo();
	        lRedirigi.setPage(IWebConstants.PG_MAIN);
	        lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" +
	          ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
	        //setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

	        return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
	      }	
	  }
}
