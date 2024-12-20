package siap.sius.permesso.action;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * ActLoadRicercaSoggettiConLicenza - Azione di caricamento della form di ricerca dei Soggetti con
 * Procedimenti di sorveglianza relativi a Licenza
 * 
 * @version 1.0
 */
public class ActLoadRicercaSoggettiConLicenza extends ActionSiap implements ICostantiPermesso {

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
		} else if (strCodTipoUfficio.equals("TDSM")) {
			setRequestAttribute("TipoUfficioConnesso", "TDSM");
			strTipoUfficioRichiesto = "UDSM";
		} else if (strCodTipoUfficio.equals("UDSM")) {
			setRequestAttribute("TipoUfficioConnesso", "UDSM");
			strTipoUfficioRichiesto = "TDSM";
		}

		String strCodUfficioRichiesto = (lUctrl.getUfficioUDSTDS(strCodDistretto, strTipoUfficioRichiesto,
				strCodComune)).getCodUfficio();

		setRequestAttribute("CodUDSTDS", strCodUfficioRichiesto);

		// Imposta Tipo Licenza.
		lOption = new Option(DecodificheManager.getInstance().getTipoLicenza(), "LC", 75);
		// 20110520 PM - S'impostano solo : Licenza, Licenza Internati.
		// MEV_2023-35: aggiungo Licenza pene sostitutive (LP)
		String[] lTipoLicenze = { "LC", "LI", "LP" };
		lOption.setFilter(lTipoLicenze);
		setRequestAttribute("licenza", "" + lOption);

		// restituisce la jsp di VIEW
		return PG_LOAD_RICERCASOGGETTICONLICENZA;
	}

}