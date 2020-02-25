package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;
import java.util.List;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.web.ActionSiap;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: ActDettaglioNuovaIstanzaRicevuta
 * </p>
 * <p>
 * Description:
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
@SuppressWarnings("rawtypes")
public class ActDettaglioNuovaIstanzaRicevuta extends ActionSiap implements ICostantiNuovaIstanza {
	public String processRequest() throws Exception {

		// DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla
		// tabella DI MESSAGGIO.
		gestioneRitorno();

		BigDecimal lIdMessage = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

		setRequestAttribute("Messaggio", lMess);

		ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

		// setRequestAttribute("fascicoloSiusGP", lParser.getFascicoloGPSius());
		setRequestAttribute("fascicolo", lParser.getFascicolo());
		setRequestAttribute("nuovaistanza", lParser.getNuovaIstanza());

		// Evento e Tenore
		EventoNotificaModel lEveNot = lParser.getEvento();
		List lTenori = lParser.getTenori();

		if (lTenori != null && lEveNot != null && lEveNot.getEvento() != null) {
			for (int i = 0; i < lTenori.size(); i++) {
				TenoreModel lTenore = (TenoreModel) lTenori.get(i);
				if (lTenore.getCodOggettoTenore().equals(lEveNot.getEvento().getCodMotivo())) {
					setRequestAttribute("tenoreEsito", lTenore);
					break;
				}
			}
		}

		setRequestAttribute("eventoNotifica", lEveNot);

		return PG_DETTAGLIO_MESSAGGIO_RICEVUTO;
	}

}