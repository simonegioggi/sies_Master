package siap.sius.presaincarico.action;

import java.math.BigDecimal;
import java.util.List;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActDettaglioRicorsoRicevuto
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
public class ActDettaglioRicorsoRicevuto extends ActionSiap implements ICostantiPresaincarico {

	public String processRequest() throws Exception {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("caricoricorso",
				getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
		if (lck != null) {

			// String lPage = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La presa in carico di questo Ricorso è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			// Prepara la "pagina" di destinazione
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.sius.presaincarico.action.ActLoadRicercaAttiSius");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}
		// SIAPReceiver.getInstance();

		// DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla
		// tabella DI MESSAGGIO.

		BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

		this.setRequestAttribute("Messaggio", lMess);

		ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

		// Parsing del Ricorso.
		ImpugnazioneModel lImpModel = new ImpugnazioneModel();
		if (lParser.getImpugnazione() != null)
			lImpModel = lParser.getImpugnazione();
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione dell'Impugnazione. <BR>Rivolgersi all'amministratore di sistema!");

		// Parsing del Fascicolo SIUS.
		FascicoloGPModel lFasGPModel = new FascicoloGPModel();
		if (lParser.getFascicoloGPSius() != null)
			lFasGPModel = lParser.getFascicoloGPSius();
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione del Procedimento SIUS. <BR>Rivolgersi all'amministratore di sistema!");

		if (lParser.getSoggetto() != null)
			lFasGPModel.getFascicoloSiusModel().setSoggetto(lParser.getSoggetto());
		else if (lParser.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione del Soggetto. <BR>Rivolgersi all'amministratore di sistema!");

		this.setRequestAttribute("fascicoloSiusGP", lFasGPModel);
		this.setRequestAttribute("impugnazione", lImpModel);

		// Evento e Tenore
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		if (lParser.getEvento() != null)
			lEveNot = lParser.getEvento();
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione dell'Evento. <BR>Rivolgersi all'amministratore di sistema!");

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

		this.setRequestAttribute("eventoNotifica", lEveNot);

		// Mette in sessione il model di fascicolo inviato da Siep.
		if (lParser.getFascicolo() != null)
			this.setSessionAttribute("fascicolo", lParser.getFascicolo());

		return PG_DETTAGLIO_RICORSO_RICEVUTO;
	}

}