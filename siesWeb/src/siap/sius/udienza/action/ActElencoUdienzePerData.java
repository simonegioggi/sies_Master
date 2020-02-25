package siap.sius.udienza.action;

import java.util.Vector;

import f3b.util.DateUtils;
import siap.sico.web.ActionSiap;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;

public class ActElencoUdienzePerData extends ActionSiap implements ICostantiUdienza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Popola il model UdienzaModel con la data prelevata dalla
		// di sistema.
		UdienzaModel lUdienzaMod = new UdienzaModel();
		lUdienzaMod.setDataUdienza(DateUtils.getSysDate());

		// Chiama la RemoteInterfacce del controller udienza.
		IUdienza lCtrlUdienza = SIUSLookupRemote.getUdienzaRemote();

		// La ricerca è sempre filtrata per ufficio
		lUdienzaMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		// Invoca il metodo della ricerca udienza per data.
		// Come argomento al metdod del controllo si passa il numero di
		// occorrenze che si desidera visuliazzare, in questo caso le prime 30.
		Vector lUdienze = new Vector();
		lUdienze = lCtrlUdienza.ExRicercaUdienza(lUdienzaMod, 30);

		// Imposta l'elenco delle udienze nella request.
		setRequestAttribute("udienze", lUdienze);

		// Ritorna la View Jsp
		return PG_ELENCOUDIENZE;
	}

}