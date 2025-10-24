package siap.sico.decodifiche.action;

import java.util.Vector;

//import siap.sico.decodifiche.controller.ComuneController;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActVisualizzaComuni extends ActionSiap implements ICostantiComune {

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

		// STUB: 2003/02/17 - ComuneController lCCon = new ComuneController();
		IComune lCCon = SICOLookupRemote.getComuneRemote();

		/*
		 * Esempio di catch RemoteException - 20030218
		 * 
		 * Vector lCom;
		 * 
		 * try { lCom = lCCon.ExGetListaComuni(lCMod); } catch( java.rmi.RemoteException rex ) { throw new
		 * SICOException( SICOException.USER_MESSAGE, "Errore RMI verso il controller ! - [" + rex + "]" ); }
		 */
		Vector lCom = lCCon.ExGetListaComuni(lCMod);

		setRequestAttribute("ListaComuni", lCom);

		return PG_RICERCACOMUNE;
	}

}