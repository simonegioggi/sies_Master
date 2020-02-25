package siap.sico.evento.action;


/**
 * <p>Title: ActModificaMagistratoFirmatarioValidazione</p>
 * <p>Description: Classe Action per la modifica del magistrato e della data emissione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.dettaglioprovvedimento.controller.DettaglioProvvedimentoManager;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import f3b.util.F3BException;
import f3b.web.Action;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActModificaMagistratoFirmatarioValidazione extends ActionSiap implements ICostantiEvento
{
	/**
	 * Azione di Modifica per Magistrato e data Emissione
	 * @return 
	 * @throws F3BException
	 */
	public String processRequest() throws Exception
	{

		String lIdEvento = getRequestStringParameter(CAMPO_ID_EVENTO);
		
		NotificaModel lNotMod = new NotificaModel();
		
	    if(!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) )
	    {
	    	setRequestAttribute("IdIstruttoriaCumulo", getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
	    }
		
		//ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = new EventoModel();
		lEveMod = lCtrlEvento.ExRicercaEventoByKey(new BigDecimal(lIdEvento));

		// carica nel model i campi da cambiare nell'evento
		lEveMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE,CAMPO_MESE_DATA_EMISSIONE,CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAGISTRATO));
		
		// carica nel model i campi da cambiare nelle Notifiche
		lNotMod.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO, ICostantiNotifica.CAMPO_MESE_DATA_INVIO,ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
	
		// Modifica l'Evento e le Notifiche
		lCtrlEvento.ExModificaEventoNotifiche(lEveMod, lNotMod);
		
		// Cerca nella tabella dettaglio provvedimento l'upload da richiamare per la validazione
		//Interrogo il DecodificheManager per avere l'Action d'Upload giusta
		String lActionUpload = DettaglioProvvedimentoManager.getInstance().getActionUpload(lEveMod);
	
		// Chiamata alla Action di Upload
		//RedirectTo lRedirigiUpload = new RedirectTo();
		//lRedirigiUpload.setPage(IWebConstants.PG_MAIN);
		//lRedirigiUpload.setAction(lActionUpload);
		
		// Parametri da passare alle Action di Upload
		//lRedirigiUpload.setParameter(ICostantiEvento.CAMPO_BLOB, "");
		//lRedirigiUpload.setParameter(ICostantiEvento.CAMPO_VALIDA, "1");
		
		// Setta il valore dell'azione di ritorno dopo la validazione
    	String lAzioneRitorno = "siap.siep.ordineesecuzione.action.ActRicercaProvvedimenti";
    	if( !this.isRequestParameterNullObj("TornaNonValidati") )
    	{
    		lAzioneRitorno = "siap.sico.evento.action.ActRicercaProvvedimentiNonValidati";
    	}
    
    	// Parametri statici da passare alla pagina di message per il Redirect 
		RedirectTo lRedirigiBack = new RedirectTo();
		lRedirigiBack.setPage(IWebConstants.PG_MAIN);
		lRedirigiBack.setAction(lAzioneRitorno);
		
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigiBack);
		 
		// NON E' stato possibile usare la Redirigi perchè la request originale è di tipo
		// MULTIPART e quindi non tutti parametri possono passare per la GET
		String lPage = null;
		Action actionObj = this.get(lActionUpload);
		actionObj.setReqSes(this.getRequest(), this.getSession());
	    
		lPage = actionObj.processRequest();
		
		return "" + lPage;
	}
}
