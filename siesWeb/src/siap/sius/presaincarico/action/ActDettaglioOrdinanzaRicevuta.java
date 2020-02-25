package siap.sius.presaincarico.action;

import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActDettaglioOrdinanzaRicevuta
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
public class ActDettaglioOrdinanzaRicevuta extends ActionSiap implements ICostantiPresaincarico {

	public String processRequest() throws Exception {

		gestioneRitorno();
		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("caricoordinanza",
				getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
		if (lck != null) {

			// String lPage = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La presa in carico di questa Ordinanza è in gestione ad un altro utente! <BR>Riprovare più tardi!");
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

		BigDecimal lIdMessage = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

		setRequestAttribute("Messaggio", lMess);

		ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

		FascicoloGPModel lFasGPModel = new FascicoloGPModel();
		if (lParser.getFascicoloGPSius() != null)
			lFasGPModel = lParser.getFascicoloGPSius();
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione del Procedimento SIUS. <BR>Rivolgersi all'amministratore di sistema! ");

		if (lParser.getSoggetto() != null)
			lFasGPModel.getFascicoloSiusModel().setSoggetto(lParser.getSoggetto());
		else if (lParser.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione del Soggetto. <BR>Rivolgersi all'amministratore di sistema!");

		setRequestAttribute("fascicoloSiusGP", lFasGPModel);

		// Per un'ordinanza di AMS metto in sessione la Misura Applicata
		if (lParser.getMisuraSicurezza() != null) {
			MisuraSicurezzaModel aMSMod = lParser.getMisuraSicurezza();
			setRequestAttribute("misuraSicurezzaApplicata", aMSMod);
		}

		// EVENTO
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		if (lParser.getEvento() != null)
			lEveNot = lParser.getEvento();
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione dell'Evento. <BR>Rivolgersi all'amministratore di sistema!");

		setRequestAttribute("eventoNotifica", lEveNot);

		// Mette in sessione il model di fascicolo inviato da Siep.
		if (lParser.getFascicolo() != null)
			setSessionAttribute("fascicolo", lParser.getFascicolo());

		return PG_DETTAGLIO_ORDINANZA_RICEVUTA;
	}

}