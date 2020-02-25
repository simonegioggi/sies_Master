package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActRicercaDepositoOrdinanzaPcByAnnoNumUff</p>
* <p>Description: Classe Action per la ricerca di DepositoOrdinanzaPc 
* da Anno, Num e codice Ufficio di Inserimento</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 1.0
*/

public class ActRicercaDepositoOrdinanzaPcByAnnoNumUff extends ActionSiap implements ICostantiDepositoOrdinanzaPc
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	  RedirectTo lRedirectTo = null;
	  
 		 DepositoOrdinanzaPcModel lDepMod = new DepositoOrdinanzaPcModel() ;
		
		 lDepMod.setAnnoS3( getRequestBigDecimalParameter( CAMPO_ANNO_S3) );
		 lDepMod.setNumS3( getRequestBigDecimalParameter( CAMPO_NUM_S3) );
		 lDepMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		 
		 // Ricerca dell'Ordinanza
		 IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		 lDepMod = lCtrl.ExRicercaDepositoOrdinanzaPcByAnnoNumUfficio(lDepMod)  ;
		 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 siesLogger.debug("Ordinanza: " + lDepMod);
		 
		 if (lDepMod.getIdEventoGenerato() != null)
		 {
			 inserimentoDatiInSessione(lDepMod.getIdEventoGenerato());
		 
		      // Costruzione della pagina di redirect
		      lRedirectTo = new RedirectTo();
			  lRedirectTo.setPage(IWebConstants.PG_MAIN);
			  lRedirectTo.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lDepMod.getIdEventoGenerato().toString() );
		      // Dettaglio Ordinanza     
	    	  lRedirectTo.setAction("siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");

			  // Passaggio dei parametri inerenti il bottone di ritorno
			  if(!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
				  lRedirectTo.setParameter( IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO) );
			  else if(!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
				  lRedirectTo.setParameter( IWebConstants.FLAG_RITORNO, getRequestStringParameter(IWebConstants.FLAG_RITORNO) );

		 }
		 else
			 throw new F3BException (F3BException.USER_MESSAGE, "Errore nei dati: manca ID Evento nell'Ordinanza!");
		 
		 return lRedirectTo.toString();
	 }

  	protected void inserimentoDatiInSessione(BigDecimal aIdEvento) throws F3BException
  	{
		// Ricerca dell'Evento
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEvento = lEveCtrl.ExRicercaEventoByKey (aIdEvento);
		if (lEvento.getFasSiuIdFascicoloSius()== null)
			 throw new F3BException (F3BException.USER_MESSAGE, "Errore nei dati: manca ID Fascicolo SIUS nell'Evento!");
		    
		// Ricerca del Fascicolo SIUS
		IFascicoloSius lCtrlFSius = SIUSLookupRemote.getFascicoloSiusRemote();
		FascicoloGPModel lFasGPMod = lCtrlFSius.ExRicercaFascicoloByKey(lEvento.getFasSiuIdFascicoloSius());
		
		 //Metto in sessione il fascicolo SIUS per consentire le funzionalità annesse.
		 setSessionAttribute("fascicoloSiusGP", lFasGPMod);
	 	
		 if (lFasGPMod.getFascicoloSiusModel() != null &&  lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep()!= null)
		 {
	      // Metto in sessione il fascicolo SIEP da cui ha origine il Fascicolo SIUS.
	      IFascicoloSiep lCtrlSIEP = SIEPLookupRemote.getFascicoloSiepRemote();
	      FascicoloSiepModel lFasSIEP = lCtrlSIEP.ExRicercaFascicoloByKeyNoError( lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
	      setSessionAttribute("fascicolo", lFasSIEP);
		 }
	  
  	}

}