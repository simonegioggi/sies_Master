package siap.siep.avvocato.action;


/**
* <p>Title: ActCancellavvocatoFascicoloSiep</p>
* <p>Description: Classe Action per la cancellazione dell'Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaAvvocatoFascicoloSiep extends ActionSiap implements ICostantiAvvocato
{
	/**
	* Azione di Cancellazione dello Storico AvvocatoFascicolo SIEP
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione
	* @throws F3BException
	*/
	public String processRequest() throws F3BException
	{
		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		BigDecimal lAvvFasId = getRequestBigDecimalParameter ( "idAvvFascicoloSiep");	
	    lCtrl.ExCancellaAvvocatoFascicoloSiepbyKey(lAvvFasId);	
		String lPage="";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep() ;
		return lPage; //restituisce la jsp di VIEW
	}
}