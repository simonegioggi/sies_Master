package siap.siep.notifica.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
//import siap.sico.evento.controller.IEventoSimeone;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.scadenzario.action.ICostantiScadenzario;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
//import f3b.web.IWebConstants;
//import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciSolleciti</p>
* <p>Description: Classe Action per la load Solleciti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciSollecitiVerbVaneRicerche extends ActionSiap implements ICostantiNotifica,ICostantiScadenzario
{
public String processRequest() throws F3BException
  {

			BigDecimal lIdFas = null;
			if (!this.isRequestParameterNullObj(ICostantiScadenzario.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP)) 
			{	 
					lIdFas = getRequestBigDecimalParameter(ICostantiScadenzario.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP);
			}
			
		    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		    DettaglioFascicoloModel lDettaglio = null;
		    lDettaglio = lCtrl.ExDettaglioFascicoloSiep(lIdFas);
		
		    if(lDettaglio == null)
		      throw new F3BException( F3BException.USER_MESSAGE, "Fascicolo non presente" );
		    FascicoloSiepModel lFascMod = lDettaglio.getFascicoloSiep();
		
		    //Inserisce nella session il fascicolo (contenente Soggetto e Sentenza)
		    setSessionAttribute("fascicolo", lFascMod);

 /*  
			if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
			{
		        RedirectTo lRedirigi = new RedirectTo();
		        lRedirigi.setPage(IWebConstants.PG_MAIN);
		         setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il Procedimento N." +lFascMod.getChiaveAnno()+"/"+ lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
		                            lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
		                            ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		        setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		         return IWebConstants.PG_MESSAGE;
			}

			this.isFascicoloSiepDiCompetenza();

			if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO"))
			{
		        RedirectTo lRedirigi = new RedirectTo();
		        lRedirigi.setPage(IWebConstants.PG_MAIN);
		         setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il Procedimento N." +lFascMod.getChiaveAnno()+"/"+ lFascMod.getChiaveProgr() + " Il fascicolo risulta Definito. Impossibile procedere!");
		                            lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
		                            ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		        setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		         return IWebConstants.PG_MESSAGE;
			}
		
			lIdFas = lFascMod.getIdFascicoloSiep();

	  EventoNotificaModel lEveNot = new EventoNotificaModel();
	  IEventoSimeone lCtrl1 = SICOLookupRemote.getEventoSimeoneRemote();
/*
 */
	  
      // Paolo Cherubini 07/06/2011 accedo direttamente per id_Evento 
	  // ho commentato tutta la parte sopra
      EventoNotificaModel lEveNot = new EventoNotificaModel();
	  BigDecimal lIdEve =	getRequestBigDecimalParameter(ICostantiScadenzario.CAMPO_EVE_ID_EVENTO);
      IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
 	  lEveNot = lCtrlEve.ExRicercaEventoNotificaByKey(lIdEve);
	  
	  if(lEveNot == null || lEveNot.getEvento() == null)
        throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessun OE ");

// AMBROSINO 17/10/2011 - Su segnalazione ,per quei procedimenti vecchi MIGRATI 
//						- facciamo la INSERT di una notifica Fittizia;	  
      if(lEveNot.getNotifiche() == null || lEveNot.getNotifiche().length == 0)
      {	  
    	 //  throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessuna Notifica Associata all'OE ");

    	  /* Paolo Cherubini 15 novembre 2011 spostata in basso vedi note 
    	    NotificaModel lNot = new NotificaModel();
	  		lNot.setCodTipoNotifica("E");
			lNot.setDataInvio(lEveNot.getEvento().getDataEmissione());
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lNot.setEveIdEvento(lIdEve);
    	  
			INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
			NotificaModel lRetModel = lCtrlNot.ExInserisciNotifica(lNot);

		    lEveNot = null;
	 	    lEveNot = lCtrlEve.ExRicercaEventoNotificaByKey(lIdEve);		*/	
			
      }  

      NotificaModel lNotifica = null;
      for(int i=0;i<lEveNot.getNotifiche().length;i++)
      {
         NotificaModel lNotMod = new NotificaModel();
         lNotMod =lEveNot.getNotifiche()[i];
         if(lNotMod.getCodTipoNotifica().equals("E"))
         {
          lNotifica = new NotificaModel(lNotMod);
         }
      }

      // Paolo Cherubini 15 novembre 2011 se non esiste notifica di tipo E ne creiamo una fittizia
      if(lNotifica == null || lNotifica.getIdNotifica() == null)
      {
			NotificaModel lNot = new NotificaModel();
			lNot.setCodTipoNotifica("E");
			lNot.setDataInvio(lEveNot.getEvento().getDataEmissione());
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lNot.setEveIdEvento(lIdEve);
			 
			INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
			NotificaModel lRetModel = lCtrlNot.ExInserisciNotifica(lNot);
			lNotifica = new NotificaModel(lRetModel);
      } // fine Paolo Cherubini 15 novembre 2011
      
      
       setRequestAttribute("evento",lEveNot.getEvento());
       setRequestAttribute("notifica", lNotifica);

      Option lOptionAutorita = null;
      Option lOptionAutoritaAltra = new Option(DecodificheManager.getInstance().getTipoAutorita());

      if(lNotifica != null && lNotifica.getAutoritaEsterna() != null &&
         lNotifica.getAutoritaEsterna().getCodTipoAutorita() != null && !lNotifica.getAutoritaEsterna().getCodTipoAutorita().equals("22"))
      {
       lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(),lNotifica.getAutoritaEsterna().getCodTipoAutorita());

      }else
      {
       lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
      }

       setRequestAttribute("tipoAutoritaAltra", "" + lOptionAutoritaAltra );
       setRequestAttribute("tipoAutorita", "" + lOptionAutorita );

       return PG_LOAD_SOLLECITI_VANE_RICERCHE;  //restituisce la jsp di VIEW

  }
}