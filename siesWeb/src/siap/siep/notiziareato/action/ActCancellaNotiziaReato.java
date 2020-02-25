package siap.siep.notiziareato.action;


/**
* <p>Title: ActCancellaNotiziaReato</p>
* <p>Description: Classe Action per la cancellazione della Notizia di Reato</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notiziareato.controller.NotiziaReatoController;
import siap.siep.notiziareato.model.NotiziaReatoModel;
import siap.web.ISIAPCostantiWeb;
import f3b.security.model.ProfileModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


public class ActCancellaNotiziaReato extends ActionSiap implements ICostantiNotiziaReato
{

  public String processRequest() throws F3BException
  {
    // istanzia il Model
    NotiziaReatoModel lNotMod = new NotiziaReatoModel ();
    
    UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute("UtenteConnesso");
	ProfileModel lProfilo =(ProfileModel) lUtenteMod.getUserProfile();

    // riempie il model
    String lId = getRequestStringParameter(CAMPO_ID_NOTIZIA_REATO);
    lNotMod.setIdNotiziaReato(new BigDecimal(lId));

    // chiama il controller
    //INotiziaReato lCtrl = SIEPLookupRemote.getNotiziaReatoRemote();
    NotiziaReatoController lCtrl = new NotiziaReatoController();
    lCtrl.ExCancellaNotiziaReato(lNotMod);

    String lPage = "";
    // Apre la pagina di dettaglio del procedimento
//  SE PROVENGO DALLA RICHIESTA CODICE CUI DELLE NOTIZIE DI REATO, TORNO SULL'ISTRUTTORIA
    if(!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)){
    	if(getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE).indexOf("RichiestaCodiceCui")>0){
    		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +"="
    			+ getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE);
    	}
    }
    else{
//	ALTRIMENTI REINDIRIZZO SUL DETTAGLIO  
    	if(lProfilo.isSige())
    	{
    		//lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep();    	
    	
    	}
    	else{
    		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep();    	
        	
    	}
    
    }
    
  // ANGELA 27.05.2009-Inizio
 // Si ricava il profilo dell'utente connesso
 // verifica se il profilo è sige o sius o siep
    if (lPage.length()==0){
    	

    	setRequestAttribute("profilo", lProfilo.getProfileId());
    
    	if(lProfilo.isSige())
    	{
    		lPage=IWebConstants.PG_MAIN + "?" +IWebConstants.ACTION_FIELD + "=siap.sige.richiestaatti.action.ActLoadInserisciRichiestaCui";
    	}
    }
    // ANGELA Fine
    
    return lPage;
  }
}
