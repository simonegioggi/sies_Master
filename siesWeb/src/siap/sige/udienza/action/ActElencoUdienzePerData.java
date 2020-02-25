package siap.sige.udienza.action;

import java.util.Vector;

import f3b.util.DateUtils;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

public class ActElencoUdienzePerData extends ActionSige implements ICostantiUdienzaSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Popola il model UdienzaModel con la data prelevata dalla
		// di sistema.
		UdienzaSigeModel lUdienzaMod = new UdienzaSigeModel();
		lUdienzaMod.setDataUdienza(DateUtils.getSysDate());

		// Chiama la RemoteInterfacce del controller udienza.
		IUdienzaSige lCtrlUdienza = SIGELookupRemote.getUdienzaSigeRemote();

		// La ricerca è sempre filtrata per ufficio
		lUdienzaMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		// Invoca il metodo della ricerca udienza per data.
		// Come argomento al metdod del controllo si passa il numero di
		// occorrenze che si desidera visuliazzare, in questo caso le prime 30.
		Vector lUdienze = new Vector();
		// lUdienze = lCtrlUdienza.ExRicercaUdienzaSige( lUdienzaMod, 30 );
		lUdienze = lCtrlUdienza.ExRicercaUdienzaSige(lUdienzaMod);

		// Imposta l'elenco delle udienze nella request.
		setRequestAttribute("udienze", lUdienze);

		// Ritorna la View Jsp
		return PG_ELENCOUDIENZE;
	}

}