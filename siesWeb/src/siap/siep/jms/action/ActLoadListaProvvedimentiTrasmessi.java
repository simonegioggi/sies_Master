package siap.siep.jms.action;

import java.util.Collection;
import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadListaProvvedimentiTrasmessi
 * </p>
 * <p>
 * Description: Classe Action per la load della Lista Provvedimenti Trasmessi
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadListaProvvedimentiTrasmessi extends ActionSiap implements ICostantiSiepJMS {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		this.setLinkRitorno();

		// Imposta Tipo Ufficio.
		// Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSius());

		// A.S. 18/05/2015 su richiesta di Michele/Nunzia
		// Per SIEP la descrizione UDSM cambia da "Ufficio di Sorveglianza presso il Tribunale per minorenni"
		// in "Magistrato di Sorveglianza per i minorenni"
		Collection lUffici = new Vector();
		lUffici = DecodificheManager.getInstance().getTipoUfficio();
		for (int i = 0; i < lUffici.size(); i++) {
			lUffici.remove(
					new DecodificheModel("UDSM", "Ufficio di Sorveglianza presso il Tribunale per minorenni",
							"TIPO_UFFICIO", "", "T", "", "", "", ""));
		}
		lUffici.add(new DecodificheModel("UDSM", "Magistrato di Sorveglianza per i minorenni", "TIPO_UFFICIO",
				"", "T", "", "", "", ""));
		Option lOption = new Option(lUffici);
		lOption.setFilter(new String[] { "-", "UDS", "TDS", "PM", "PGCAP", "PMM", "TDSM", "UDSM" });

		setRequestAttribute("tipoUfficio", "" + lOption);

		return PG_LOAD_LISTAPROVVEDIMENTITRASMESSI; // restituisce la jsp di VIEW
	}

}