package siap.sige.richiestaatti.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActDettaglioEstrattoSentenza</p>
* <p>Description: Classe Action per la load dettaglio Lista Atti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActDettaglioListaAtti extends ActSIESDettaglioProvvedimento
{


 public String processRequest() throws Exception
   {
	 	BigDecimal lIdProvvedimento = null;
	 	String CodMotivo=null;
	 	String lRetPage=null;  
	 	
	 	lIdProvvedimento = getRequestBigDecimalParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);
	 	CodMotivo=getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);
	 	
	    // Ricerca Provvedimento dalla chiave
	    IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
	    ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);
	 
 	  
 	 RedirectTo lPage = new RedirectTo();
	 lPage.setPage(IWebConstants.PG_MAIN);
 	  
 	  if (CodMotivo.equals("0049"))
       {	 
 		  	lPage.setAction("siap.sige.richiestaatti.action.ActDettaglioRicSIntegrale");
		    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
		    lRetPage = lPage.toString();
	        
	    }
 	  else if (CodMotivo.equals("0701"))
	    {	 
 		  	lPage.setAction("siap.sige.richiestaatti.action.ActDettaglioRichiestaPM");
		    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
		    lRetPage = lPage.toString();
	        
	    }
 	  else if (CodMotivo.equals("0702"))
	    {	 
 		  	lPage.setAction("siap.sige.richiestaatti.action.ActDettaglioRichiestaFascicoloPenale");
		    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
		    lRetPage = lPage.toString();
	        
	    }
 	  else if (CodMotivo.equals("0044"))
	    {	 
 		  	lPage.setAction("siap.sige.richiestaatti.action.ActDettaglioPosizioneGiuridica");
		    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
		    lRetPage = lPage.toString();
	        
	    }
 	  
 	  else if (CodMotivo.equals("0054") || CodMotivo.equals("0579") || CodMotivo.equals("0580"))
	    {	 
 		  	lPage.setAction("siap.sige.richiestaatti.action.ActDettaglioAccertamentiAnagrafici");
		    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
		    lRetPage = lPage.toString();
	        
	    }
 	  else if (CodMotivo.equals("0557") || CodMotivo.equals("0578"))
	    {	 
 		
		  	lPage.setAction("siap.sige.richiestaatti.action.ActDettaglioRichiestaCui");
		    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
		    lRetPage = lPage.toString();
	        
	    }
 	 else if (CodMotivo.equals("0753") )
	    {	 
		
		  	lPage.setAction("siap.sige.richiestaatti.action.ActLoadDettaglioRichiestaParere");
		    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
		    lRetPage = lPage.toString();
	    }
 	else if (CodMotivo.equals("0704") )
    {	 
	
	  	lPage.setAction("siap.sige.richiestaatti.action.ActDettaglioRicAltraAutorita");
	    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
	    lRetPage = lPage.toString();
    }
 	else if (CodMotivo.equals("0705") )
    {	 
	
	  	lPage.setAction("siap.sige.richiestaatti.action.ActDettaglioRichiestaDelegaIndagini");
	    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
	    lRetPage = lPage.toString();
    }
 	else if (CodMotivo.equals("0708") )
    {	 
	
	  	lPage.setAction("siap.sige.richiestaatti.action.ActDettaglioRichiestaGenerica");
	    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
	    lRetPage = lPage.toString();
    }
 	else if (CodMotivo.equals("0709") )
    {	 
	
	  	lPage.setAction("siap.sige.richiestaatti.action.ActDettaglioRicStatoEsecuzione");
	    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
	    lRetPage = lPage.toString();
    }  
 	else if (CodMotivo.equals("0706") )
    {	 
	
	  	lPage.setAction("siap.sige.richiestaatti.action.ActDettaglioRichiestaCorpoReato");
	    lPage.setParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE, lProvEvento.getProvvedimento().getIdProvvedimentoSige().toString());
	    lRetPage = lPage.toString();
    }  
 	 else
			throw new F3BException(F3BException.USER_MESSAGE, "Istruttoria non gestita: CodMotivo = "+CodMotivo );
	    
	 return  lRetPage;
   }
}