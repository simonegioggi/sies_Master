package siap.sius.depositodecreto.action;


import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ActRicercaDepositoOrdinanzaPcByAnnoNumUff;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActRicercaDepositoDecretoByAnnoNumUff</p>
* <p>Description: Classe Action per la ricerca di DepositoDecreto 
* da Anno, Num e codice Ufficio di Inserimento.</p>
* <p>La classe è stata ottenuta specializzando la ActRicercaDepositoOrdinanzaPc 
* per riutilizzare la stessa funzione per la ricerca dei dati da inserire in session.</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActRicercaDepositoDecretoByAnnoNumUff extends ActRicercaDepositoOrdinanzaPcByAnnoNumUff 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	  RedirectTo lRedirectTo = null;
	  DepositoDecretoModel lDecretoMod = new DepositoDecretoModel();
		
	  lDecretoMod.setAnnoS72( getRequestBigDecimalParameter( ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_S3) );
	  lDecretoMod.setNumS72( getRequestBigDecimalParameter( ICostantiDepositoOrdinanzaPc.CAMPO_NUM_S3) );
	  lDecretoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		 
	  // Ricerca del Decreto
	  IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
  	  lDecretoMod = lCtrl.ExRicercaDepositoDecretoByAnnoNumUfficio(lDecretoMod)  ;
  	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  	  siesLogger.debug("Decreto: " + lDecretoMod);
		 
  	  if (lDecretoMod.getIdEventoGenerato() != null)
  	  {
  		  inserimentoDatiInSessione(lDecretoMod.getIdEventoGenerato());
		 
		  // Costruzione della pagina di redirect
		  lRedirectTo = new RedirectTo();
		  lRedirectTo.setPage(IWebConstants.PG_MAIN);
		  lRedirectTo.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lDecretoMod.getIdEventoGenerato().toString() );
		  // Dettaglio Decreto     
		  lRedirectTo.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito");

		 // Passaggio dei parametri inerenti il bottone di ritorno
		 if(!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
			 lRedirectTo.setParameter( IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO) );
		 else if(!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			 lRedirectTo.setParameter( IWebConstants.FLAG_RITORNO, getRequestStringParameter(IWebConstants.FLAG_RITORNO) );
  	  }
  	  else
		throw new F3BException (F3BException.USER_MESSAGE, "Errore nei dati: manca ID Evento nel Decreto!");
		 
		 return lRedirectTo.toString();
	 }

  
}