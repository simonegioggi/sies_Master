package siap.sius.remissionedebito.action;


/**
* <p>Title: ActCancellaRichiestaRemissioneDebito</p>
* <p>Description: Classe Action per la cancellazione di Remissione Debito</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sius.ActionSius;
import siap.sius.remissionedebito.controller.IRichiestaRemissione;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaRichiestaRemissioneDebito extends ActionSius implements ICostantiSiusRemissioneDebito
{
	/**
	* Azione di Cancellazione della Richiesta Remissione
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione
	* @throws F3BException
	*/
	public String processRequest() throws F3BException
	{
		IRichiestaRemissione lCtrl = SIUSLookupRemote.getRichiestaRemissioneRemote();
		RichiestaRemissioneModel lRicMod = new RichiestaRemissioneModel();
		lRicMod.setIdRichiestaRemissione ( getRequestBigDecimalParameter ( CAMPO_ID_RICHIESTA_REMISSIONE) );
		lCtrl.ExCancellaRichiestaRemissione(lRicMod);
		
		String lPage="";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.remissionedebito.action.ActRicercaSiusRichiestaRemissione" ;
		return lPage; //restituisce la jsp di VIEW
	}
}