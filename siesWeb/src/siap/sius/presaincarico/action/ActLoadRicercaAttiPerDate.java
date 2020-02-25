package siap.sius.presaincarico.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sius.ActionSius;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRicercaAttiPerDate
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di Messaggi in arrivo
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

public class ActLoadRicercaAttiPerDate extends ActionSius implements ICostantiPresaincarico {
	public String processRequest() throws Exception {
		this.setLinkRitorno();
		// Elenco Destinatari per tipo ufficio CBX.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());

		// MEV10-s3: aggiunto filtro per minorenni --> "PMM" in due punti
		String[] lCodes = new String[] { "-", "PGCAP", "PM", "PMM", "TDS", "UDS", "UEPE", "UEPESS" };
		String filtroMinorenni = super.getFiltroMinorenni();
		if (filtroMinorenni.equalsIgnoreCase("true"))
			lCodes = new String[] { "-", "PGCAP", "PM", "PMM", "TDS", "UDS", "UEPE", "UEPESS", "TDSM", "UDSM" };

		lOption.setFilter(lCodes); // Imposta il filtro di uguaglianza.

		setRequestAttribute("filtroMinorenni", filtroMinorenni);
		setRequestAttribute("tipoUfficio", ("" + lOption).toUpperCase());
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		setRequestAttribute("TipoUfficioConnesso", strCodTipoUfficio);

		return PG_LOAD_RICERCAATTIPERDATE; // restituisce la jsp di VIEW
	}
}