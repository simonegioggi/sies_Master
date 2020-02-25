package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadTrasferisciRichiestaGenerica
 * </p>
 * <p>
 * Description: Trasferisce la richiesta generica verso il destinatario prescelto
 * </p>
 * <p>
 * MEV_39
 * </p>
 * 
 * @author Gioggi
 * @version 1.0
 */
public class ActLoadTrasferisciRichiestaGenerica extends ActionSiap implements ICostantiRichiesta {

	@SuppressWarnings({ "rawtypes", "unchecked" })
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

		Collection uffici = new Vector();
		uffici = DecodificheManager.getInstance().getTipoUfficio();
		for (int i = 0; i < uffici.size(); i++) {
			uffici.remove(new DecodificheModel("UDSM",
					"Ufficio di Sorveglianza presso il Tribunale per minorenni", "TIPO_UFFICIO", "", "T", "",
					"", "", ""));
		}
		uffici.add(new DecodificheModel("UDSM", "Magistrato di Sorveglianza per i minorenni", "TIPO_UFFICIO",
				"", "T", "", "", "", ""));
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