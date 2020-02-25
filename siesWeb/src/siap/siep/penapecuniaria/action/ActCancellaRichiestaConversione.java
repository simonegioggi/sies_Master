package siap.siep.penapecuniaria.action;


/**
* <p>Title: ActCancellaBeneficio</p>
* <p>Description: Classe Action per la cancellazione di Beneficio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaRichiestaConversione extends ActionSiap implements ICostantiPenaPecuniaria
{
	/**
	* Azione di Cancella della Richiesta Conversione
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione
	* @throws F3BException
	*/
	public String processRequest() throws F3BException
	{
		IRichiestaConversione lCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
		lRicMod.setIdRichiestaConversione      ( getRequestBigDecimalParameter ( CAMPO_ID_RICHIESTA_CONVERSIONE) );
		lCtrl.ExCancellaRichiestaConversione(lRicMod);
		
		String lPage="";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() ;
		return lPage; //restituisce la jsp di VIEW
	}
}