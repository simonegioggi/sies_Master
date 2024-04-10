package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * Metodo per il trasferimento del provvedimento di 'Trasmissione Atti Conversione Pene Pecuniarie' verso il
 * magistrato di sorveglianza (UDS o UDSM)
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadTrasferisciAttiConversione extends ActionSiap implements ICostantiRateizzazionePP {

	public String processRequest() throws Exception {

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = new EventoNotificaModel();
		enm = ie.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		Option o = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		o.setFilter(new String[] { "-", "UDS", "UDSM" });
		o.setSelected(enm.getNotifiche()[0].getUfficio().getCodTipoUfficio());
		setRequestAttribute("tipoUDS", "" + o);

		// pagina di ritorno
		return PG_LOAD_TRASFERISCI_ATTI_CONVERSIONE;
	}

}