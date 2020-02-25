package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadTrasferisciProvvedimentoDS
 * </p>
 * <p>
 * Description: Trasferisce l' istanza verso il tribunale di sorveglianza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActLoadTrasferisciProvvedimentoDS extends ActionSiap implements ICostantiSospensione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Insieme degli uffici destinatari
		// MEV_66: aggiunti campi nella combo di selezione
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "-");
		Collection destinatari = DecodificheManager.getInstance().getTipoUfficio();
		Iterator i = destinatari.iterator();
		while (i.hasNext()) {
			DecodificheModel dm = (DecodificheModel) i.next();
			if ("UDSM".equalsIgnoreCase(dm.getCode())) {
				// Per SIEP la descrizione UDSM cambia da
				// "Ufficio di Sorveglianza presso il Tribunale per minorenni"
				// in "Magistrato di Sorveglianza per i minorenni"
				dm.setDescription("Magistrato di Sorveglianza per i minorenni");
				break;
			}
		}
		Option lOption = new Option(destinatari);
		String[] lFiltro = new String[9];
		lFiltro[0] = "TDS";
		lFiltro[1] = "UDS";
		lFiltro[2] = "UDSM";
		lFiltro[3] = "TDSM";
		lFiltro[4] = "GIPM";
		lFiltro[5] = "GP";
		lFiltro[6] = "GUPM";
		lFiltro[7] = "CAPSM";
		lFiltro[8] = "DIBM";
		// FINE MEV_66

		lOption.setFilter(lFiltro);

		setRequestAttribute("uffici", "" + lOption);
		setRequestAttribute("IDEvento", lEveId.toString());

		return PG_LOAD_TRASFERISCI_PROVVEDIMENTO_DS;
	}

}