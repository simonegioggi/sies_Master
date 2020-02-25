package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadTrasferisciProvvedimentoL78del2013
 * </p>
 * <p>
 * Description: Trasferisce l' istanza relativa all'ordine esecuzione/ comunicazione L 78/2013 verso gli
 * uffici di sorveglianza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActLoadTrasferisciProvvedimentoL78del2013 extends ActionSiap
		implements ICostantiOrdineEsecuzione {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Insieme degli uffici destinatari
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "UDS");

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
		lOption.setFilter(new String[] { "-", "UDS", "TDS", "TDSM", "UDSM" });

		if (!isRequestParameterNullObj("sedetribunale"))
			setRequestAttribute("sedetribunale", this.getRequestStringParameter("sedetribunale"));

		if (!isRequestParameterNullObj("titolo"))
			setRequestAttribute("titolo", this.getRequestStringParameter("titolo"));

		setRequestAttribute("uffici", "" + lOption);
		setRequestAttribute("IDEvento", lEveId.toString());

		return PG_LOAD_TRASFERISCI_PROVVEDIMENTO_L78_2013;
	}

}