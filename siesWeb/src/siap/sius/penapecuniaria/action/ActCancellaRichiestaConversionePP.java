package siap.sius.penapecuniaria.action;


/**
* <p>Title: ActCancellaBeneficio</p>
* <p>Description: Classe Action per la cancellazione di Beneficio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaRichiestaConversionePP extends ActionSius implements ICostantiSiusPenaPecuniaria
{
	/**
	* Azione di Cancellazione della Richiesta Conversione
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione
	* @throws F3BException
	*/
	public String processRequest() throws F3BException
	{
		IRichiestaConversione lCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
		lRicMod.setIdRichiestaConversione ( getRequestBigDecimalParameter ( CAMPO_ID_RICHIESTA_CONVERSIONE) );
		lRicMod.setFasSiuIdFascicoloSius( getRequestBigDecimalParameter ( CAMPO_FAS_SIU_ID_FASCICOLO_SIUS) );
		lCtrl.ExCancellaRichiestaConversione(lRicMod);
		
		String lPage="";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.penapecuniaria.action.ActRicercaSiusRichiestaConversione" ;
		return lPage; //restituisce la jsp di VIEW
	}
}