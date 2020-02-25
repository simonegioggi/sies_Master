package siap.sige.fogliocomplementare.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
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
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActRicercaFoglioComplementareByAnnoNumUff extends ActionSige 
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
		  lRedirectTo.setAction("siap.sige.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp");

		 // Passaggio dei parametri inerenti il bottone di ritorno
		 if(!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
			 lRedirectTo.setParameter( IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO) );
		 else if(!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			 lRedirectTo.setParameter( IWebConstants.FLAG_RITORNO, getRequestStringParameter(IWebConstants.FLAG_RITORNO) );
  	  }
  	  else
		throw new F3BException (F3BException.USER_MESSAGE, "Errore nei dati: manca ID Evento nel Documento Allegato");
		 
		 return lRedirectTo.toString();
	 }

	protected void inserimentoDatiInSessione(BigDecimal aIdEvento) throws F3BException
	{
		// Ricerca del Provvedimento Sige
		IProvvedimentoSige lProvvSige = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvvS = lProvvSige.ExRicercaProvvedimentoByIdEvento(aIdEvento);
		if (lProvvS.getProvvedimento().getFasIdFascicoloSige() == null)
			 throw new F3BException (F3BException.USER_MESSAGE, "Errore nei dati: manca ID Fascicolo SIGE nel Provvedimento!");
		    
		// Ricerca del Fascicolo SIGE
		IFascicoloSige lCtrlFSige = SIGELookupRemote.getFascicoloSigeRemote();
		FascicoloSigeEstesoModel lFasSigeMod = lCtrlFSige.ExRicercaEstesaFascicoloSigeByKey(lProvvS.getProvvedimento().getFasIdFascicoloSige());
		
		//Metto in sessione il fascicolo SIGE per consentire le funzionalità annesse.
		setSessionAttribute("FascicoloSigeEsteso", lFasSigeMod);
	}
	
}