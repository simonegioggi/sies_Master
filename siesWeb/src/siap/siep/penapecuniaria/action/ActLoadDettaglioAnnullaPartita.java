package siap.siep.penapecuniaria.action;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadDettaglioAnnullaPartita</p>
 * <p>Description: Classe Action per la load del Dettaglio 
 * <p>di Annotazione Annullamento Partita di Credito</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 1.0
 */

public class ActLoadDettaglioAnnullaPartita extends ActionSiap implements ICostantiPenaPecuniaria , ICostantiEvento
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  { 
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("ActLoadDettaglioAnnullaPartita");
	  
	  	if(isRequestParameterNullObj(CAMPO_ID_RICHIESTA_CONVERSIONE))
	  	{
	  		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  		siesLogger.debug("ActLoadDettaglioAnnullaPartita - dentro If");
	  		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  		siesLogger.debug("ActLoadDettaglioAnnullaPartita - Vengo Da Elenco Provvedimenti");
			
		  	RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
		  	IRichiestaConversione lCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
		  	lRicMod.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		  	lRicMod=lCtrl.ExRicercaRichiestaConversioneByIdEvento(lRicMod.getEveIdEvento());
		  	
		  	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		  	siesLogger.debug("lRicMod di IF = " +  lRicMod);
		  	setRequestAttribute("richiestaconversione", lRicMod);
					
	  	}
	  	else
	  	{	
	  		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  		siesLogger.debug("ActLoadDettaglioAnnullaPartita - dentro Else");
	  		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  		siesLogger.debug("ActLoadDettaglioAnnullaPartita - Vengo Da Inserimento Annotazione");
	  	
	  		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
	  		IRichiestaConversione lCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
	  		lRicMod.setIdRichiestaConversione      ( getRequestBigDecimalParameter ( CAMPO_ID_RICHIESTA_CONVERSIONE) );
	  		lRicMod=lCtrl.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione() );

	  		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  		siesLogger.debug("lRicMod di ELSE = " +  lRicMod);
	  		setRequestAttribute("richiestaconversione", lRicMod);
		}
		 
		return PG_LOAD_DETTAGLIO_ANNULLA_PARTITA;	  
  }
}