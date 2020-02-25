package siap.sius.udienza.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActElencoUdienzeDarData
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Udienze a partire da una data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActElencoUdienzeDaData extends ActionSiap implements ICostantiUdienza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Popola il model UdienzaModel con la data prelevata dalla
		// request.
		UdienzaModel lUdienzaMod = new UdienzaModel();
		lUdienzaMod.setDataUdienza(getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
				CAMPO_GIORNO_DATA_UDIENZA));

		// Chiama la RemoteInterfacce del controller udienza.
		IUdienza lCtrlUdienza = SIUSLookupRemote.getUdienzaRemote();

		// La ricerca è sempre filtrata per ufficio
		lUdienzaMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		// Invoca il metodo della ricerca udienza per data.
		// Come argomento al metdod del controllo si passa il numero di
		// occorrenze che si desidera visuliazzare, in questo caso le prime 30.
		Vector lUdienze = new Vector();

		// Luigi 21-2-05 TUTTE !
		// Viene passato anche il Num di Proce per Udienza
		lUdienze = lCtrlUdienza.ExRicercaUdienzaNumProc(lUdienzaMod);
		setRequestAttribute("num_proc", "SI");

		// lUdienze = lCtrlUdienza.ExRicercaUdienza( lUdienzaMod, 30 );

		// Imposta l'elenco delle udienze nella request.
		setRequestAttribute("udienze", lUdienze);

		// Ritorna la View Jsp
		return PG_ELENCOUDIENZE;
	}

}