package siap.sius.documentoallegato.action;


import org.apache.log4j.Logger;

import siap.sius.depositoordinanzapc.action.ActRicercaDepositoOrdinanzaPcByAnnoNumUff;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActRicercaFoglioComplementareByAnnoNumUff</p>
* <p>Description: Classe Action per la ricerca di Foglio Complementare 
* da Anno, Num e codice Ufficio di Inserimento.</p>
* <p>La classe è stata ottenuta specializzando la ActRicercaDepositoOrdinanzaPc 
* per riutilizzare la stessa funzione per la ricerca dei dati da inserire in session.</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 2.2
*/

public class ActRicercaFoglioComplementareByAnnoNumUff extends ActRicercaDepositoOrdinanzaPcByAnnoNumUff 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	  RedirectTo lRedirectTo = null;
	  
	  DocumentoAllegatoModel lDocAll = new DocumentoAllegatoModel();
	  lDocAll.setAnnoFoglioComplementare(getRequestBigDecimalParameter( ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_S3));
	  lDocAll.setProgrFoglioComplementare(getRequestBigDecimalParameter( ICostantiDepositoOrdinanzaPc.CAMPO_NUM_S3));
	  lDocAll.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
  
	  // Ricerca del DocumentoAllegato
	  IDocumentoAllegato lCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
	  lDocAll = lCtrl.ExRicercaFoglioComplementareByAnnoNumUfficio(lDocAll);

  	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  	  siesLogger.debug("DocumentoAllegato: " + lDocAll);
		 
  	  if (lDocAll.getEveIdEvento() != null)
  	  {
  		  inserimentoDatiInSessione(lDocAll.getEveIdEvento());
		 
		  // Costruzione della pagina di redirect
		  lRedirectTo = new RedirectTo();
		  lRedirectTo.setPage(IWebConstants.PG_MAIN);
		  lRedirectTo.setParameter( ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO, lDocAll.getIdDocumentoAllegato().toString() );
		  // Dettaglio Decreto     
		  lRedirectTo.setAction("siap.sius.provvedimento.action.ActLoadDettaglioCompFoglioComp");

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