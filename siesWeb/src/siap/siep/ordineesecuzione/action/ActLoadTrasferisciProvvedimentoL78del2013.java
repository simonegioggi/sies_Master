package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * ActLoadTrasferisciProvvedimentoL78del2013 - Trasferisce l' istanza relativa all'ordine esecuzione/
 * comunicazione L 78/2013 verso gli uffici di sorveglianza
 *
 * @version 1.0
 */
public class ActLoadTrasferisciProvvedimentoL78del2013 extends ActionSiap
		implements ICostantiOrdineEsecuzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Insieme degli uffici destinatari
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "UDS");

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