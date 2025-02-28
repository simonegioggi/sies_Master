package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * ActLoadTrasferisciRichiestaGenerica - Trasferisce la richiesta generica verso il destinatario prescelto
 *
 * @since MEV_39
 * @author Gioggi
 * @version 1.0
 */
public class ActLoadTrasferisciRichiestaGenerica extends ActionSiap implements ICostantiRichiesta {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = new EventoNotificaModel();
		enm = iEvento.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		// notifiche
		// for (int i = 0; i < enm.getNotifiche().length; i++) {
		// NotificaModel nm = new NotificaModel();
		// nm = enm.getNotifiche()[i];
		// if (nm != null && nm.getUffCodUfficio() != null
		// && "TDS".equals(nm.getUfficio().getCodTipoUfficio())) {
		// setRequestAttribute("ufficiotds", nm.getUfficio());
		// setRequestAttribute("UfficioDestinatario", nm.getUfficio());
		// } else if (nm != null && nm.getUffCodUfficio() != null && !"T".equals(nm.getCodTipoNotifica())
		// && "UDS".equals(nm.getUfficio().getCodTipoUfficio())) {
		// setRequestAttribute("ufficiouds", nm.getUfficio());
		// setRequestAttribute("UfficioDestinatario", nm.getUfficio());
		// }
		// }

		// Collection uffici = new Vector();
		// uffici = DecodificheManager.getInstance().getTipoUfficio();
		// for (int i = 0; i < uffici.size(); i++) {
		// uffici.remove(new DecodificheModel("UDSM",
		// "Ufficio di Sorveglianza presso il Tribunale per minorenni", "TIPO_UFFICIO", "", "T", "",
		// "", "", ""));
		// }
		// uffici.add(new DecodificheModel("UDSM", "Magistrato di Sorveglianza per i minorenni",
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
		Option option = new Option(uffici);
		option.setFilter(new String[] { "-", "UDS", "TDS", "TDSM", "UDSM" });

		setRequestAttribute("uffici", "" + option);

		// Destinatari UEPE
		// option = new Option(DecodificheManager.getInstance().getTipoUfficioSiepe());
		// setRequestAttribute("UEPE", "" + option);

		// valore di ritorno
		return PG_LOAD_TRASM_RICH_GEN;
	}

}