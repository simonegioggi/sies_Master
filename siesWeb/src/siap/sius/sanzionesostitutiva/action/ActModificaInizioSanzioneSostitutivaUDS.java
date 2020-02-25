package siap.sius.sanzionesostitutiva.action;
/**
* <p>Title: sanzionesostitutiva</p>
* <p>Description: Classe Action per la modifica di PeriodoAltraSanzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

//import siap.sico.evento.model.EventoModel;
//import siap.sius.SIUSException;
//import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
//import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.util.SIUSLookupRemote;
//import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaInizioSanzioneSostitutivaUDS extends ActSanzioneSostitutiva 
implements  ICostantiSanzioneSostitutiva
{

	 /*****************************************************************************
	  * Azione di Modifica del PeriodoAltraSanzione
	  * @return Nome della pagina JSP da visualizzare
	  * al termine dell'elaborazione
	  * @throws F3BException
	  *****************************************************************************/
	  public String processRequest() throws F3BException {
		  
	    // Recupera la key del record da Modifica 
	    BigDecimal lIdPeriodoAltraSanzione     = getRequestBigDecimalParameter ( ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE) ;

	    // Recupera i dati del record da modificare  
	    IPeriodoAltraSanzione lCtrl = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
	    PeriodoAltraSanzioneModel lPerMod = lCtrl.ExRicercaPeriodoAltraSanzioneById( lIdPeriodoAltraSanzione);
	    
	    //===========================================================
	    // Restituisce la msg se i dati non sono stati recuperati. 
	    //===========================================================
	    if (lPerMod==null ){ 
		      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
		      // Specificare eventualmente la jump page dove verrà ridirezionata la 
		      // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
		      // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
		      // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
		      //      della root_dir es /siap/frame.htm  
		      setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 
		      return IWebConstants.PG_MESSAGE;
	    }	  
	    	    
	    //recupero ID fascicolo e dati maschera
	    lPerMod = recuperoDatiMaschera(lPerMod);
	   
		// setto i dati dell'utente che opera la modifica
	    lPerMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
	    lPerMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
	    lPerMod.setDataAggiornamento(DateUtils.getSysDate());
	    

	    // Recupera il controller ed effettua la modifica periodo
	    lCtrl = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
	    lPerMod = lCtrl.ExModificaPeriodoAltraSanzione(lPerMod);
	    
	    //====================================================================== 
	    // Prepara la pagina di destinazione
	    // Viene restituita la pagina di dettaglio con i dati appena inseriti
	    //====================================================================== 
	    String lPage="";
	    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.sanzionesostitutiva.action.ActLoadValidaInizioSanzioneSostitutivaUDS";
	    lPage += "&" + CAMPO_ID_PERIODO_ALTRA_SANZIONE + "=" + lPerMod.getIdPeriodoAltraSanzione().toString();
	    return lPage;
	  }
}
	
