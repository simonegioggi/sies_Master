package siap.sius.depositosentenza.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioDataDepositoSentenza
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio Data Deposito Sentenza
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioDataDepositoSentenza extends ActionSius implements ICostantiDepositoSentenza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// Bottone di ritorno
		setLinkRitorno();

		// Recupero dell'ID del Documento Allegato dalla request.
		String lIdDocAll = getRequestStringParameter(CAMPO_ID_DOCUMENTO_ALLEGATO);

		// Lettura del Documento Allegato.
		IDocumentoAllegato lCtrlDA = SIUSLookupRemote.getDocumentoAllegatoRemote();
		DocumentoAllegatoModel lDocAllMod = lCtrlDA
				.ExRicercaDocumentoAllegatoByKey(new BigDecimal(lIdDocAll));
		if (lDocAllMod == null || lDocAllMod.getIdDocumentoAllegato() == null)
			throw new F3BException(F3BException.EX_NOT_FOUND, "Errore nella lettura del Documento Allegato");

		setRequestAttribute("documentoAllegato", lDocAllMod);

		// Lettura del Deposito Sentenza.
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				">>>>> ActLoadDettaglioDataDepositoSentenza - Prima della Lettura del Deposito Sentenza.");
		IDepositoSentenza lCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();
		DepositoSentenzaModel llDepMod = lCtrl.ExRicercaDepositoSentenzaByEvento(lDocAllMod.getEveIdEvento());
		if (llDepMod == null || llDepMod.getIdDepositoSentenza() == null) {
			throw new F3BException(F3BException.EX_NOT_FOUND, "Errore nella lettura della Sentenza");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>> IdDepositoSentenza = " + llDepMod.getIdDepositoSentenza());
		setRequestAttribute("depositosentenza", llDepMod);
		// Il Deposito Sentenza si mette in sessione.
		setSessionAttribute("lDepositoSentenza", llDepMod);

		// Lettura delle notifiche.
		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>> IdEventoGenerato = " + llDepMod.getIdEventoGenerato());
		Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(llDepMod.getIdEventoGenerato());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>> Numero notifiche = " + lVect.size());
		setRequestAttribute("notifiche", lVect);

		// Lettura Evento Provvedimento
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel lEve = lCtrlEve.ExRicercaEventoByKey(lDocAllMod.getEveIdEvento());
		setRequestAttribute("evento", lEve);

		// Modificabilità, Stampabilità e Trasferibile
		String lModificabile = "NO";
		String lStampabile = "NO";
		String lTrasferibile = "NO";

		if (IsFascicoloSiusModificabile()) {
			// Stampabilità
			if (lDocAllMod.getFlagDocumentoRegistrato() == null
					|| lDocAllMod.getFlagDocumentoRegistrato().compareTo("N") == 0) {
				// Modificabilità e Stampabilità coincidono
				lStampabile = "SI";
				lModificabile = "SI";
			} else
				lTrasferibile = "SI";
		}

		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Stampabile", lStampabile);
		setRequestAttribute("Trasferibile", lTrasferibile);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_LOAD_DETTAGLIO_DEPOSITO_SENTENZA;
	}

}