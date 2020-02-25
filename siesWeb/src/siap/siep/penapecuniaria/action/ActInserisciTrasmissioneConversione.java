package siap.siep.penapecuniaria.action;


/**
* <p>Title: ActInserisciRichiestaTrasmissione/p>
* <p>Description: Classe Action per l'inserimento della Trasmissione RichiestaConversione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


public class ActInserisciTrasmissioneConversione extends ActionSiap 
                                              implements ICostantiPenaPecuniaria 
{

 /*****************************************************************************
  * Azione di Inserimento della Trasmissione della RichiestaConversione
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  ****************************************************************************/
  public String processRequest() throws F3BException 
  {
      //========================================================================== 
    // Recupero i dati presenti in maschera 
    // n.b. eliminare o commentare i campi non presenti in maschera 
    //      es: chiave della tabella, date_ins, foreignkey... 
    //========================================================================== 
	  
// creazione dei Model Evento e Notifica
   EventoNotificaModel lEve = new EventoNotificaModel();
   NotificaModel lNotifiche[] = new NotificaModel[2];

// ricevo dalla maschera l'Ufficio di Sorveglianza
   if (!this.isRequestParameterNullObj(ICostantiPenaPecuniaria.CAMPO_COD_SEDE_UDS)
 	    && getRequestStringParameter(ICostantiPenaPecuniaria.CAMPO_COD_SEDE_UDS) != null 
 	    && !getRequestStringParameter(ICostantiPenaPecuniaria.CAMPO_COD_SEDE_UDS).equals(""))
   {
	   // se cè qualcosa nel campo preparo un record Notifica
	   String lUDS = this.getCodUfficioByCodTipoUfficioDescrComune
	   		(getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO), getRequestStringParameter(ICostantiPenaPecuniaria.CAMPO_COD_SEDE_UDS));
		NotificaModel lNotModUDS = new NotificaModel();
		lNotModUDS.setCodEsito("-");
		lNotModUDS.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lNotModUDS.setDataInserimento(DateUtils.getSysDate());
		lNotModUDS.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lNotModUDS.setCodTipoNotifica("E");
		lNotModUDS.setDataInvio(getRequestDateParameter( ICostantiNotifica.CAMPO_ANNO_DATA_INVIO, ICostantiNotifica.CAMPO_MESE_DATA_INVIO,  ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO ));
		lNotModUDS.setUffCodUfficio(lUDS);
//		carico la Notifica UDS nella lista delle Notifiche
		lNotifiche[0] = lNotModUDS;
   }


	
   if (!isRequestParameterNullObj("notificaufficio") && getRequestStringParameter("notificaufficio").equals("01"))
   {							   
	   //	 per la Notifica Ufficio Recupero Crediti viene preparato il Model dell'autorità esterna
	   //	 con codice sede (CAMPO_COD_LUOGO_EMITTENTE) e codice autorita (CAMPO_COD_TIPO_AUTORITA_EMITTENTE)
	   //	 preparazione record per la Notifica Ufficio Recupero Crediti
	   
	 	NotificaModel lNotModUffCre = new NotificaModel();
	 	lNotModUffCre.setCodEsito("-");
	 	lNotModUffCre.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	 	lNotModUffCre.setDataInserimento(DateUtils.getSysDate());
	 	lNotModUffCre.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	 	lNotModUffCre.setCodTipoNotifica("E");
	 	lNotModUffCre.setDataInvio(getRequestDateParameter( ICostantiNotifica.CAMPO_ANNO_DATA_INVIO, ICostantiNotifica.CAMPO_MESE_DATA_INVIO,  ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO ));	
	
	    AutoritaEsternaModel lAut = new AutoritaEsternaModel();
	    String lDestinatari = getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
	    lAut.setCodTipoAutorita(lDestinatari);
	    //String lSedeDestinatari = getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE);
	    //ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedeDestinatari) );
	    lAut.setCodSede( getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));
	    lAut.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	    lAut.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	    lAut.setDataInserimento(DateUtils.getSysDate());
	
	    //	 carico l'autorita esterna dentro la notifica 
	    lNotModUffCre.setAutoritaEsterna(lAut);

	    //	carico la Notifica Ufficio Recupero Crediti nella lista delle Notifiche
	    lNotifiche[1] = lNotModUffCre;
	  
   }

   lEve.setNotifiche(lNotifiche);
    // carico la lista delle notifiche nell'evento
	
//  Tipo Evento = Richiesta (poiché in sostanza l'evento che si trasmette 
//    corrisponde ad una Richiesta di conversione delle pene pecuniarie
	lEve.getEvento().setCodTipoEvento("02"); 
	lEve.getEvento().setCodTipoProvvedimento("31"); //Tipo Provvedimento = Trasmissione Atti

	if(this.isRequestChecked("ritrasmissione")){
		lEve.getEvento().setCodMotivo("0934");
	}else{
		lEve.getEvento().setCodMotivo("0934");
	}

	FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
	lEve.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );
	Date lDataEmissione = getRequestDateParameter( ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,  ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,  ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE );
	lEve.getEvento().setDataEmissione( lDataEmissione );

   UfficioModel lUff = this.getUfficioUtenteConnesso();
   lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
   lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
   lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
   lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
   lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
   lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
   lEve.getEvento().setCodEsito("-");
   lEve.getEvento().setCodLuogoDestinatario("-");
   lEve.getEvento().setCodUfficioDestinatario("-");	   
   lEve.getEvento().setCodTipoUfficioDestinatario("-");
   lEve.getEvento().setFlagStampaSiep("S");
   lEve.getEvento().setFlagVideoSiep("S");
   lEve.getEvento().setCodMagistrato(calcolaMagistrato());

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
    "=siap.siep.penapecuniaria.action.ActDettaglioTrasmissioneConversione&" +
    ICostantiEvento.CAMPO_ID_EVENTO + "=" +
    lRetModel.getEvento().getIdEvento() + "&modalita=I";

	 return lPage;
   
  }
}