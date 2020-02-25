package siap.siep.sentenza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.security.model.ProfileModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadDettaglioSentenza extends ActionSiap implements ICostantiSentenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

    // Parse della request
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SENTENZA);
        
    

    if(!this.isRequestAttributeNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
    }

    // paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
    if(!this.isRequestParameterNullObj("lTipoFunzione"))
    {
     this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }


    // Riempie il model
    SentenzaModel lSmod = new SentenzaModel();
    lSmod.setIdSentenza(lId);
    lSmod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

    //SentenzaController lCtrl = new SentenzaController();
    ISentenza lCtrl = SIEPLookupRemote.getSentenzaRemote();

    SentenzaModel lSen = lCtrl.ExRicercaSentenzaByKey(lSmod.getIdSentenza());

    // Inserisce in session la sentenza model.
    setSessionAttribute("sentenza",lSen );
    setSessionAttribute("isSentenza", "true");
    setRequestAttribute("sentenza",lSen );
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("Sentenza ======> " + lSen);
    // verifica se il profilo è SIGE
    ProfileModel lProfilo =(ProfileModel) lUtenteMod.getUserProfile();
    
    // Modificabilità viene passato nella request solo se utente SIGE
    if (lProfilo.isSige()){
    // Modificabilità
    String lModificabile = "NO";
    if (lSen.getCodUfficioInserimento().compareTo(getCodUfficioUtenteConnesso()) == 0)
    	lModificabile = "SI";
    setRequestAttribute("Modificabile",lModificabile );
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug("Sentenza modificabile : " + lModificabile);

    }

    String lPage = "";

    if (lSen.getCodTipoProvvedimento().equals("02")) {//DECRETO
      //lPage = PG_DETTAGLIODECRETO;
    	// Costruzione della pagina di redirect
    	  //String lIdEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
        RedirectTo lRedirectTo = new RedirectTo();
    	  lRedirectTo.setPage(IWebConstants.PG_MAIN);
    	  //lRedirectTo.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento );
    	 
    	  // Passaggio dei parametri inerenti il bottone di ritorno
    	  if(!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
    		  lRedirectTo.setParameter( IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO) );
    	  else if(!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
    		  lRedirectTo.setParameter( IWebConstants.FLAG_RITORNO, getRequestStringParameter(IWebConstants.FLAG_RITORNO) );
      
    	  lRedirectTo.setAction("siap.siep.sentenza.action.ActLoadDettaglioDecreto");

     	  lPage = lRedirectTo.toString();
    }
    else if (lSen.getCodTipoProvvedimento().equals("05")
    		||lSen.getCodTipoProvvedimento().equals("63")		//tipo di Provvedimento=decreto di archiviazione
    		||lSen.getCodTipoProvvedimento().equals("03")		//tipo di Provvedimento=ordinanza
    		||lSen.getCodTipoProvvedimento().equals("13"))  { 	//tipo di Provvedimento=cumulo
    /* inizio modifica marzo 2010  per SENTENZA STRANIERA */	
    //  lPage = PG_DETTAGLIOSENTENZASTRANIERA;

    	// Costruzione della pagina di redirect
  	  //String lIdEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
      RedirectTo lRedirectTo = new RedirectTo();
  	  lRedirectTo.setPage(IWebConstants.PG_MAIN);
  	  //lRedirectTo.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento );
  	 
  	  // Passaggio dei parametri inerenti il bottone di ritorno
  	  if(!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
  		  lRedirectTo.setParameter( IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO) );
  	  else if(!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
  		  lRedirectTo.setParameter( IWebConstants.FLAG_RITORNO, getRequestStringParameter(IWebConstants.FLAG_RITORNO) );
    
  	if (lSen.getCodTipoProvvedimento().equals("63"))  { 	//tipo di Provvedimento=decreto di archiviazione
  		setRequestAttribute("descTipoProvvedimento","Decreto di Archiviazione");
  	} else if (lSen.getCodTipoProvvedimento().equals("03"))  { 	//tipo di Provvedimento=ordinanza
  	  		setRequestAttribute("descTipoProvvedimento","Ordinanza");
  	} else if (lSen.getCodTipoProvvedimento().equals("13"))  { 	//tipo di Provvedimento=cumulo
  	  		setRequestAttribute("descTipoProvvedimento","Cumulo");
  	} 
  	
  	
  	  lRedirectTo.setAction("siap.siep.sentenza.action.ActLoadDettaglioSentenzaStraniera");
	    	
   	  lPage = lRedirectTo.toString();
  	
    } 	
    /* fine modifica marzo 2010 */	
    else
      lPage = PG_DETTAGLIOSENTENZA; //SENTENZA

    //Passa la action di destinazione
  /* Sostituita
     if (!isRequestParameterNullObj("TornaQui"))
    { this.setRequestAttribute("TornaQui", this.getRequestStringParameter("TornaQui"));}
    */
    gestioneRitorno();

    if(!this.isRequestParameterNullObj("NomeAzione"))
    {
      this.setRequestAttribute("NomeAzione",this.getRequestStringParameter("NomeAzione"));
    }


    return lPage;
  }
}