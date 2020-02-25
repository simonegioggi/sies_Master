package siap.sige.udienza.action;

import java.util.Date;
import java.util.Vector;

import siap.sige.udienza.controller.IUdienzaSigeRuolo;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActElencoUdienzeDaData
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Udienze a partire da una data, per tutto l'anno
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActElencoUdienzeDaData extends ActionSige implements ICostantiUdienzaSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Popola il model UdienzaModel con la data prelevata dalla request.
		UdienzaSigeModel lUdienzaMod = new UdienzaSigeModel();

		Date[] lRangeDateUdienza = getRequestDateParameters(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA, ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA);
		lUdienzaMod.setDateUdienze(lRangeDateUdienza);

		String lTipoRito = getRequestStringParameter("tipoRito");
		// Imposta il Tipo rito (Collegiale o Monocratico).
		setRequestAttribute("tipoRito", lTipoRito);

		// Chiama la RemoteInterface del controller udienza.
		IUdienzaSigeRuolo lCtrlUdienza = SIGELookupRemote.getUdienzaSigeRuoloRemote();

		// La ricerca è sempre filtrata per ufficio
		lUdienzaMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		Vector lUdienze = new Vector();

    lUdienze = lCtrlUdienza.ExRicercaUdienzaSigePerRuolo(lUdienzaMod, lTipoRito);

		// Imposta l'elenco delle udienze nella request.
		setRequestAttribute("udienze", lUdienze);

		// Ritorna la View Jsp
		return PG_ELENCOUDIENZE;
	}

}