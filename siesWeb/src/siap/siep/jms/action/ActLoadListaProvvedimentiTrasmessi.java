package siap.siep.jms.action;

import java.util.Collection;
import java.util.Iterator;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * ActLoadListaProvvedimentiTrasmessi - Classe Action per la load della Lista Provvedimenti Trasmessi
 *
 * @version 1.0
 */
public class ActLoadListaProvvedimentiTrasmessi extends ActionSiap implements ICostantiSiepJMS {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		this.setLinkRitorno();

		// Imposta Tipo Ufficio.
		// Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSius());

		// A.S. 18/05/2015 su richiesta di Michele/Nunzia
		// Per SIEP la descrizione UDSM cambia da "Ufficio di Sorveglianza presso il Tribunale per minorenni"
		// in "Magistrato di Sorveglianza per i minorenni"
		// Collection lUffici = new Vector();
		// lUffici = DecodificheManager.getInstance().getTipoUfficio();
		// for (int i = 0; i < lUffici.size(); i++) {
		// lUffici.remove(
		// new DecodificheModel("UDSM", "Ufficio di Sorveglianza presso il Tribunale per minorenni",
		// "TIPO_UFFICIO", "", "T", "", "", "", ""));
		// }
		// lUffici.add(new DecodificheModel("UDSM", "Magistrato di Sorveglianza per i minorenni",
		// "TIPO_UFFICIO",
		// "", "T", "", "", "", ""));
		// Ticket#202501220131: modificato il caricamento della combo degli uffici che
		// modifica anche la cache cambiando l'ordine degli uffici ed il nome da UDS a MDS
		IDecodifiche id = SICOLookupRemote.getDecodificheRemote();
		Collection uffici = id
				.ExRicercaDecodifiche(new DecodificheModel("", "", "TIPO_UFFICIO", "", "", "", "", "", ""));
		Iterator i = uffici.iterator();
		while (i.hasNext()) {
			DecodificheModel dm = (DecodificheModel) i.next();
			if ("UDSM".equalsIgnoreCase(dm.getCode())) {
				dm.setDescription("Magistrato di Sorveglianza per i minorenni");
				break;
			}
		}
		Option lOption = new Option(uffici);
		lOption.setFilter(new String[] { "-", "UDS", "TDS", "PM", "PGCAP", "PMM", "TDSM", "UDSM" });

		setRequestAttribute("tipoUfficio", "" + lOption);

		return PG_LOAD_LISTAPROVVEDIMENTITRASMESSI; // restituisce la jsp di VIEW
	}

}