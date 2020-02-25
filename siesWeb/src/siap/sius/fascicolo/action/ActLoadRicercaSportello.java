package siap.sius.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRicercaSportello
 * </p>
 * <p>
 * Description: Azione di caricamento della form di ricerca di sportello dei Soggetti con Procedimenti di
 * Sorveglianza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 */
public class ActLoadRicercaSportello extends ActionSiap implements ICostantiFascicoloSius {

	public String processRequest() throws F3BException {

		Option lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
		setRequestAttribute("nazioni", "" + lOption);

		// Recupero del codice Ufficio di Sorveglianza (se è connesso l'utente TDS)
		// oppure del codice del Tribunale di Sorveglianza ( se è connesso l'utente UDS)
		IUfficio lUctrl = SICOLookupRemote.getUfficioRemote();

		String strCodDistretto = getUfficioUtenteConnesso().getCodDistretto();
		String strCodComune = getUfficioUtenteConnesso().getCodComune();
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String strTipoUfficioRichiesto = new String();
		// MERGE v10 COLLAUDO: aggiunte casistiche
		if (strCodTipoUfficio.equals("TDS")) {
			setRequestAttribute("TipoUfficioConnesso", "TDS");
			strTipoUfficioRichiesto = "UDS";
		} else if (strCodTipoUfficio.equals("UDS")) {
			setRequestAttribute("TipoUfficioConnesso", "UDS");
			strTipoUfficioRichiesto = "TDS";
		} else if (strCodTipoUfficio.equals("UDSM")) {
			setRequestAttribute("TipoUfficioConnesso", "UDSM");
			strTipoUfficioRichiesto = "TDSM";
		} else if (strCodTipoUfficio.equals("TDSM")) {
			setRequestAttribute("TipoUfficioConnesso", "TDSM");
			strTipoUfficioRichiesto = "UDSM";
		} else {
			setRequestAttribute("TipoUfficioConnesso", strCodTipoUfficio);
			strTipoUfficioRichiesto = "UDS";
		}

		UfficioModel lUfficio = lUctrl.getUfficioUDSTDS(strCodDistretto, strTipoUfficioRichiesto,
				strCodComune);
		String strCodUfficioRichiesto = lUfficio.getCodUfficio();

		// Se è collegato un UDS distaccato dal proprio TDS (comuni diversi), non si valorizza il
		// codiceUfficioRichiesto.
		if (strCodComune.compareTo(lUfficio.getCodComune().toString()) == 0)
			setRequestAttribute("CodUDSTDS", strCodUfficioRichiesto);

		// Imposta Contenuto.
		lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimento(), 75);
		setRequestAttribute("contenuto", "" + lOption);

		return PG_LOAD_RICERCA_SPORTELLO; // restituisce la jsp di VIEW
	}

}