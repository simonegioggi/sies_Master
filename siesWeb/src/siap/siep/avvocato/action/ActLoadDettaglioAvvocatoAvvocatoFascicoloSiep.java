package siap.siep.avvocato.action;
/**
* <p>Title: ActLoadDettaglioAvvocatoAvvocatoFascicoloSiep</p>
* <p>Description: Classe Action per la load dettaglio di Avvocato - Sentenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
//import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
//import f3b.web.RedirectTo;

public class ActLoadDettaglioAvvocatoAvvocatoFascicoloSiep extends ActionSiap implements ICostantiAvvocato
{
  public String processRequest() throws Exception
  {
    if(!this.isRequestAttributeNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
    }
    
    // paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
    if(!this.isRequestParameterNullObj("lTipoFunzione"))
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }
    
    BigDecimal lAvvFasId = null;
    BigDecimal lIdEvento = null;
    if (!isRequestParameterNullObj("idAvvFascicoloSiep")) {
    	lAvvFasId = getRequestBigDecimalParameter("idAvvFascicoloSiep");
    	
    	// PAOLO 5/12/2009 
    	// provo ad inserire bottone di ritorno per elenco storico difensori
    	// nessun bottone per elenco PM 
    	// attenzione a non ricoprire il ritorno per l'OE
        this.gestioneRitorno();

        if(!this.isRequestParameterNullObj("NomeAzione"))
        {
          this.setRequestAttribute("NomeAzione",this.getRequestStringParameter("NomeAzione"));
        }
    	
    }else {
    	lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    	IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    	EventoNotificaModel lNotEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
    	if (lNotEveMod.getNotifiche() !=null && lNotEveMod.getNotifiche().length > 0) {    	
	    	NotificaModel lNot = new NotificaModel(lNotEveMod.getNotifiche()[0]);
	    	if (lNot != null && lNot.getAvvIdAvvocatoFascicoloSiep() != null) {
	    		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
	    		AvvocatoSiepModel lAvvocato = lAvvCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSiep(lNot.getAvvIdAvvocatoFascicoloSiep());
	    		lAvvFasId =lAvvocato.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep();
	    		setRequestAttribute("lEve",lNotEveMod);
	    	}
	    }
     }
    
    // riempie il model
    AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
    lAvvFascMod.setFasSieIdFascicoloSiep(((FascicoloSiepModel)(getSessionAttribute("fascicolo"))).getIdFascicoloSiep());

    if (lAvvFasId != null){
	    IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
	    AvvocatoSiepModel lAvv = lCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSiep(lAvvFasId);
	
	    IIstitutoDetenzione lCtrlIstituto = SIEPLookupRemote.getIstitutoDetenzioneRemote();
	    IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();
	    lIstMod=lCtrlIstituto.ExRicercaIstitutoDetenzioneByKey(lAvv.getAvvocatoFascicoloSiepModel().getIstDetIdIstitutoDetenzione());
	    setRequestAttribute("avvocato", lAvv);
	    setRequestAttribute("lIstMod", lIstMod);
	    setRequestAttribute("modalita", "D");
	    return PG_DETTAGLIO_AVVOCATO_AVVOCATO_FASCICOLO_SIEP;
    }else{  
		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
			"=siap.sico.evento.action.ActDettaglioDocumento&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lIdEvento;
		return lPage; //restituisce la jsp di VIEW
   
    }
    
  }
}