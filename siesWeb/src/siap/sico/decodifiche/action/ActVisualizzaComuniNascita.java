package siap.sico.decodifiche.action;

import java.util.Vector;

//import siap.sico.decodifiche.controller.ComuneController;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActVisualizzaComuniNascita extends ActionSiap implements ICostantiComune {

	public String processRequest() throws F3BException {

		ComuneModel lCMod = new ComuneModel();

		if (getRequestStringParameter("comune").length() > 0) {
			// Ticket#202510210160 - SIES: Anomalia nomi comuni - PROV. BOLZANO
			// uppercase trasforma VILLNÖß in VILLNOSS e non lo trova
			lCMod.setDescrizione(getRequestStringParameter("comune")/*.toUpperCase()*/);
			setRequestAttribute("comune", lCMod.getDescrizione());
		} else
			setRequestAttribute("comune", " ");

		lCMod.setCodProvincia(getRequestStringParameter("codProv"));
		// Riempito il model

		IComune lCCon = SICOLookupRemote.getComuneRemote();

		Vector lCom = lCCon.ExGetListaComuniNascita(lCMod);

		setRequestAttribute("ListaComuni", lCom);

		return PG_RICERCACOMUNENASCITA;
	}

}