package siap.sico.cssa.action;

import java.util.Vector;

import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActListaCSSAFiltroComune extends ActionSiap implements ICostantiCSSA {

	public String processRequest() throws F3BException {

		String ret = PG_RICERCA_CSSALISTA;

		String typename = "";
		if (!isRequestParameterNullObj("typename")) {
			typename = getRequestStringParameter("typename");
		}

		CSSAModel lCssaMod = new CSSAModel();
		if (getRequestStringParameter(ICostantiCSSA.CAMPO_COD_COMUNE).length() > 0) {
			lCssaMod.setComune(getRequestStringParameter(ICostantiCSSA.CAMPO_COD_COMUNE).toUpperCase());
		}

		ICSSA lCCon = SICOLookupRemote.getCSSARemote();
		Vector lCom = null;
		try {
			if ("USSM".equals(typename)) {
				ret += "?NomeFunzione=Elenco Uffici di Esecuzione Penale Esterna per Minorenni";
				lCom = lCCon.ExGetListaComuniCssaMinor(lCssaMod);
			} else {
				lCom = lCCon.ExGetListaComuniCssa(lCssaMod);
			}
		} catch (Exception e) {
			// nessun elemento trovato
		}

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("ListaComuni", lCom);

		return ret; // restituisce la jsp di VIEW
	}

}