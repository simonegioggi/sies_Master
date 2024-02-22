package siap.sius.presaincarico.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.web.IWebConstants;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.web.ActionSiap;
import siap.siepe.relazione.model.RelazioneModel;
import siap.sius.SIUSException;

/**
 * Title: ActVisualizzaRelazioneSiepeRicevuta
 * Description: Carica il Documento BLOB dal TreeModel di Attività Ricevuta
 * 
 * @version 1.0
 */

public class ActVisualizzaRelazioneSiepeRicevuta extends ActionSiap implements ICostantiPresaincarico {

	/**
	 * Azione di Visualizzazione della Rrlazione SIEPE Ricevuta afferente ad una richiesta o attività
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	public String processRequest() throws Exception {

		gestioneRitorno();

		BigDecimal lIdMessage = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

		ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

		// Esegue la lettura della Richiesta ricevuta.
		RelazioneModel lRelazioneRicevuta = new RelazioneModel();

		// Se il contenuto nel parse della richiesta è diverso da null
		// si esgue la lettura e la relativa istanza viene associata all'istanza
		// del model.
		if (lParser.getRelazione() != null)
			lRelazioneRicevuta = lParser.getRelazione();
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Errore nella Ricezione del Documento relativo alla Richiesta.");

		// Preleva il contenuto del BLOB, presente nel model di pertinenza.

		// Inizializza lo Streamer.
		ByteArrayOutputStream lReport = new ByteArrayOutputStream();
		// Verifica ed eventualmente recupera dal model il Document, il quale
		// viene scritto nello Streamer.
		if (lRelazioneRicevuta.getDocPerTrasferimento() != null
				&& lRelazioneRicevuta.getDocPerTrasferimento().length > 1)
			lReport.write(lRelazioneRicevuta.getDocPerTrasferimento());
		// Se lo Stream è vuoto lancia errore di eccezione.
		if (lReport.size() == 0)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Documento Associato");

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}