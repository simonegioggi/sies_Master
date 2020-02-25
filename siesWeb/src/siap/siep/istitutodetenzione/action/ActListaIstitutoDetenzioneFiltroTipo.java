package siap.siep.istitutodetenzione.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

public class ActListaIstitutoDetenzioneFiltroTipo extends ActionSiap implements ICostantiIstitutoDetenzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		IstitutoDetenzioneModel lIstDetMod = new IstitutoDetenzioneModel();
		Vector lComuni = null;
		lIstDetMod.setCodTipoIstituto(this.getRequestStringParameter(CAMPO_COD_TIPO_ISTITUTO));
		if ((!this.getRequestStringParameter("descComune").equals(""))
				&& (this.getRequestStringParameter("descComune") != null)) {
			lIstDetMod.setDescrComune(this.getRequestStringParameter("descComune"));
		}
		if ((!this.getRequestStringParameter("codDistretto").equals(""))
				&& (this.getRequestStringParameter("codDistretto") != null)) {
			lIstDetMod.setCodDistretto(this.getRequestStringParameter("codDistretto"));
		}

		IIstitutoDetenzione lIstituto = SIEPLookupRemote.getIstitutoDetenzioneRemote();
		try {
			lComuni = lIstituto.ExRicercaIstitutoDetenzionePerDistretto(lIstDetMod);
		} catch (Exception e) {
		}

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("field2", getRequestStringParameter("field2"));
		setRequestAttribute("fieldname", getRequestStringParameter("fieldname"));
		setRequestAttribute("ListaComuni", lComuni);

		// Parametro per ora passato solo dalla LoadInserisciPosizioneGiuridica
		if (!isRequestParameterNullObj("LoadDescEstesa")) {
			setRequestAttribute("LoadDescEstesa", getRequestStringParameter("LoadDescEstesa"));
		}

		return PG_RICERCA_ISTITUTO_LISTA; // restituisce la jsp di VIEW
	}

}