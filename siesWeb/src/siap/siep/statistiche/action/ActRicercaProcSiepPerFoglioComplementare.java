package siap.siep.statistiche.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActRicercaProcSiepPerFoglioComplementare extends ActionSiap 
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
		  // Dettaglio Foglio Complementare     
		  lRedirectTo.setAction("siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp");

		 // Passaggio dei parametri inerenti il bottone di ritorno
		 if(!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
			 lRedirectTo.setParameter( IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO) );
		 else if(!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			 lRedirectTo.setParameter( IWebConstants.FLAG_RITORNO, getRequestStringParameter(IWebConstants.FLAG_RITORNO) );
  	  }
  	  else
		throw new F3BException (F3BException.USER_MESSAGE, "Errore nei dati: manca ID Evento nel Procedimento!");
		 
		 return lRedirectTo.toString();
	 }

	protected void inserimentoDatiInSessione(BigDecimal aIdEvento) throws F3BException
	{
		// Ricerca dell'Evento
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEvento = lEveCtrl.ExRicercaEventoByKey (aIdEvento);
		if (lEvento.getFasSieIdFascicoloSiep() == null)
			 throw new F3BException (F3BException.USER_MESSAGE, "Errore nei dati: manca ID Fascicolo SIEP nell'Evento!");
		    
		// Ricerca del Fascicolo SIEP
		IFascicoloSiep lCtrlFSiep = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel lFasSiepMod = lCtrlFSiep.ExRicercaFascicoloByKey(lEvento.getFasSieIdFascicoloSiep());
		
		//Metto in sessione il fascicolo SIEP per consentire le funzionalità annesse.
		setSessionAttribute("fascicolo", lFasSiepMod);
	}
	
}