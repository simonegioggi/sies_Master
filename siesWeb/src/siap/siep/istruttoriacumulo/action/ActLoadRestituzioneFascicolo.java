package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.messaggio.controller.IMessaggio;

import siap.sico.ufficio.model.UfficioModel;
import siap.siep.modulocumulo.action.ActionModuloCumulo;

/**
* <p>Title: ActLoadRestituzioneFascicolo</p>
* <p>Description: Classe Action per la load Restituzione Atti Ricevuti</p>
*/

public class ActLoadRestituzioneFascicolo extends ActionModuloCumulo implements ICostantiIstruttoriaCumulo
{
  public String processRequest() throws F3BException
  {
	  //==========================================================================
      // Recupero i dati di Istruttoria (serve per tornare sull'elenco fascicoli) 
      //==========================================================================    
    if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO))
      super.getDatiIstruttoria();
      
	  //===================================================================================
	  // Recupero il fascicolo da restituire e il relativo messaggio di trasmissione
	  //====================================================================================
	    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
	    
	    MessaggioModel lMessa = null;
        IMessaggio lCtrl = JMSLookupRemote.getMessaggioRemote();
        lMessa = (MessaggioModel) lCtrl.ExRicercaMessaggioByKey(lIdMess);
        
		UfficioModel lUfficio = null;
        if(lMessa!=null && lMessa.getIdMessaggio()!=null)
        {
        	if(lMessa.getCodUfficioMittente()!=null)
        	{	
        		lUfficio = (UfficioModel)getUfficioByCodUfficio(lMessa.getCodUfficioMittente());
        	}	
        }
        
        this.setRequestAttribute("idMessaggio", ""+lIdMess);
        this.setRequestAttribute("messaggio", lMessa);
        this.setRequestAttribute("UfficioDestinatario", lUfficio);
        this.setRequestAttribute(IWebConstants.GOTO_PAGE, getRequestStringParameter(IWebConstants.GOTO_PAGE));

        return PG_RESTITUZIONE_FASCICOLO;
  }
}
