package siap.sius.luogodetenzione.action;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
/**
* <p>Title: ActModificaLuogoDetenzione lato SIUS</p>
* <p>Description: Classe Action per la modifica di LuogoDetenzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
// Viene modificato solo la data di fine detenzione
import siap.sico.web.ActionSiap;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;

// E' possibile mo
public class ActModificaLuogoDetenzione extends ActionSiap implements ICostantiLuogoDetenzione {

	/**
	 * Azione di Modifica del LuogoDetenzione
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// String lId = getRequestStringParameter(CAMPO_ID_LUOGO_DETENZIONE);

		// riempie il model
		LuogoDetenzioneModel lLuoMod = new LuogoDetenzioneModel();

		lLuoMod.setIdLuogoDetenzione(getRequestBigDecimalParameter(CAMPO_ID_LUOGO_DETENZIONE));
		lLuoMod.setDataFineDetenzione(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_DETENZIONE,
				CAMPO_MESE_DATA_FINE_DETENZIONE, CAMPO_GIORNO_DATA_FINE_DETENZIONE));
		lLuoMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lLuoMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lLuoMod.setDataAggiornamento(DateUtils.getSysDate());

		// chiama il controller
		ILuogoDetenzione lCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
		lCtrl.ExModificaDataFineLuogoDetenzione(lLuoMod);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.luogodetenzione.action.ActRicercaLuogoDetenzioneProv";
		return lPage;
	}

}