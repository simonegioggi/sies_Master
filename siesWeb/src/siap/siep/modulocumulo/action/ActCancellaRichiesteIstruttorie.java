package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;

/**
* <p>Title: ActCancellaRichiesteIstruttorie</p>
 <p>Description: Classe Action per la Cancellazione di Istruttoria / Richiesta </p>
* <p>				legati ad una Istruttoria CUMULO	</p>
* @version 1.0
*/

public class ActCancellaRichiesteIstruttorie extends ActionSiap implements ICostantiModuloCumulo
{
  public String processRequest() throws Exception
  {

	  String motivazioni =null;
	  BigDecimal lId = this.getRequestBigDecimalParameter("IdEvento");
	
	  BigDecimal lIdIstru = getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

	  // riempie il model
	  EventoModel lEveModRic = new EventoModel();
	  CampoNotaModel lCampoMod = new CampoNotaModel();
	  IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
	
	  IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
	  lEveModRic = lCtrlEvento.ExRicercaEventoByKey(lId);
	
	  if(lEveModRic != null)
	  {
		  if(lEveModRic.getFlagDocumentoRegistrato() == null || (lEveModRic.getFlagDocumentoRegistrato().equals("") || lEveModRic.getFlagDocumentoRegistrato().equals("N"))) 
	      {
			// provvedimenti non validati cancellazione fisica
			  lCtrl.ExCancellaEventoConStoreProcedure(lEveModRic);
	      }
	      else // provvedimenti validati, in questo caso c'e' una cancellazione logica
	      {
	    	  motivazioni = this.getRequestStringParameter("motivazioni");
	
	    	  lEveModRic.setFlagDocumentoRegistrato("A");
	    	  lCampoMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	    	  lCampoMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	    	  lCampoMod.setDataInserimento(DateUtils.getSysDate());
	    	  lCampoMod.setEveIdEvento(lId);
	    	  lCampoMod.setDescr(motivazioni);
	
	    	  lCtrl.ExAggiornaEventoInserisciCampoNota(lEveModRic, lCampoMod);
	      	}
	
	    }
	
    String lPage;
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +"=siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteIstruttorie&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+lIdIstru.toString();
	      
    return lPage ;
  }
  
}
