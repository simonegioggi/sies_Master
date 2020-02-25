package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;

/**
 * <p>
 * Title: ActDettaglioProvvedimentoTrasmesso
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
public class ActDettaglioProvvedimentoTrasmesso extends ActionSiap implements ICostantiOrdineEsecuzione {

	public String processRequest() throws Exception {

		setLinkRitorno(); // STUB 13/05/2005
		// SIAPReceiver.getInstance();

		// DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla
		// tabella DI MESSAGGIO.
		BigDecimal lIdMessage = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

		MessaggioModel lMessCorr = lCrtl.ExRicercaMessaggioByCorrelationId(lMess.getIdMessaggio().toString());

		lMess.setMessaggioCorrelato(lMessCorr);

		setRequestAttribute("Messaggio", lMess);

		ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

		if (lParser.getEvento() != null) {
			setRequestAttribute("evento", lParser.getEvento());
		} else if (lParser.getDettaglioFascicoloSiep() != null) {
			setRequestAttribute("evento", (EventoNotificaModel) lParser.getDettaglioFascicoloSiep()
					.getEventi().get(0));
		}

		// setRequestAttribute("fascicolo", lParser.getFascicolo());
		if (lParser.getFascicolo() != null) {
			setSessionAttribute("fascicolo", lParser.getFascicolo());
		} else if (lParser.getDettaglioFascicoloSiep() != null) {
			setSessionAttribute("fascicolo", lParser.getDettaglioFascicoloSiep().getFascicoloSiep());
		}

		// 07/08/2014 MIS. SICUREZZA : Accertamento Pericolosità Sociale
		// Soggetto
		if (lParser.getSoggetto() != null)
			setRequestAttribute("soggetto", lParser.getFascicolo().getSoggetto());
		else if (lParser.getDettaglioFascicoloSiep() != null
				&& lParser.getDettaglioFascicoloSiep().getFascicoloSiep() != null
				&& lParser.getDettaglioFascicoloSiep().getFascicoloSiep().getSoggetto() != null)
			setRequestAttribute("soggetto", lParser.getDettaglioFascicoloSiep().getFascicoloSiep()
					.getSoggetto());
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione del Soggetto. <BR>Rivolgersi all'amministratore di sistema!");

		// Eventuali misure di sicurezza
		if (lParser.getDettaglioFascicoloSiep().getMisureSicurezza() != null)
			setRequestAttribute("misureSicurezza", lParser.getDettaglioFascicoloSiep()
					.getMisureSicurezza());
		// END 07/08/2014 MIS. SICUREZZA

		return PG_DETTAGLIO_MESSAGGIO_TRASMESSO;
	}

}