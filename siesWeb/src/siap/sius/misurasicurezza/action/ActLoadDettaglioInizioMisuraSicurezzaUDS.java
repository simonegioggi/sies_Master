package siap.sius.misurasicurezza.action;
/**
* <p>Title: ActLoadDettaglioInizioMisuraSicurezza</p>
* <p>Description: Classe Action per la load dettaglio di PeriodoAltraMisura</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;



public class ActLoadDettaglioInizioMisuraSicurezzaUDS extends ActionSius 
implements ICostantiSiusMisuraSicurezza
	{
	 /*****************************************************************************
	  * Azione di caricamento della pagina di Dettaglio dei dati. 
	  * 
	  * @return Nome della pagina JSP da visualizzare
	  * @throws F3BException
	  *****************************************************************************/
	  public String processRequest() throws F3BException {

	    // Recupera la key del record da Visualizzare 
	    BigDecimal lIdPeriodoAltraMisura     = getRequestBigDecimalParameter ( ICostantiSiusMisuraSicurezza.CAMPO_ID_PERIODO_ALTRA_MISURA) ;

	    // Recupera i dati del record da modificare  
	    IPeriodoAltraMisura lCtrl = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
	    PeriodoAltraMisuraModel lPerMod = lCtrl.ExRicercaPeriodoAltraMisuraById( lIdPeriodoAltraMisura);

	    //===========================================================
	    // Restituisce la msg se i dati non sono stati recuperati. 
	    //===========================================================
	    if (lPerMod==null){ 
	      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
	      // Specificare eventualmente la jump page dove verrà ridirezionata la 
	      // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
	      // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
	      // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
	      //      della root_dir es /siap/frame.htm  
	    setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 
	      return IWebConstants.PG_MESSAGE;
	    }

	    //==================================================== 
	    // Passa il Model alla componente di visualizzazione  
	    //==================================================== 
	    setRequestAttribute("PeriodoAltraMisura", lPerMod);

	    //=========================================================
	    // Restituisce la pagina di Visualizzazione del Dettaglio.
	    //=========================================================
	    // Imposta Modalità.
	    setRequestAttribute("modalita", "D");
	    
	     //model Istituto Detenzione
	    IstitutoDetenzioneModel lIstMod = null;
	    if (lPerMod != null && lPerMod.getIstDetIdIstitutoDetenzione() != null && !lPerMod.getIstDetIdIstitutoDetenzione().equals("-"))
	    {
	    	IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
	    	lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lPerMod.getIstDetIdIstitutoDetenzione());
	    }
	      
	    setRequestAttribute("istitutodetenzione", lIstMod);
	    return PG_LOAD_DETTAGLIOINIZIOMISURASICUREZZA;
	  }
}
