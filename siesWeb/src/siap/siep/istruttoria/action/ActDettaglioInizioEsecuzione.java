	package siap.siep.istruttoria.action;
	
	import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
	
	/**
	* <p>Title: ActDettaglioInizioEsecuzione</p>
	* <p>Description: Classe Action per la load dettaglio Stampa Inizio Esecuzione</p>
	* <p>Copyright: Copyright (c) 2002</p>
	* <p>Company: Bull</p>
	* @version 1.0
	*/
	
	public class ActDettaglioInizioEsecuzione extends ActionSiap
	                                          implements ICostantiIstruttoria
	{
	  public String processRequest() throws Exception
	  {
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");

	    UfficioModel lUfficio = getUfficioUtenteConnesso();
	    setRequestAttribute("ufficio", lUfficio);
	    //a7/rr/168
	    SentenzaModel sentenza = new SentenzaModel(lFascicoloModel.getSentenza());
	    setRequestAttribute("sentenza", sentenza);
	    
	    String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
	    EventoNotificaModel lEve = new EventoNotificaModel();
	    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
	    lEve = lCtrl.ExRicercaEventoNotificaByKey(new BigDecimal(lId));
	    setRequestAttribute("evento", lEve.getEvento());
     	setRequestAttribute("eventonotifica",lEve);
	
	    return  ICostantiIstruttoria.PG_DETTAGLIO_INIZIO_ESECUZIONE;
	  }
	}
