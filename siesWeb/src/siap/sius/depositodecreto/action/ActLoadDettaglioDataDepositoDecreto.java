package siap.sius.depositodecreto.action;

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
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioDataDepositoDecreto
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di DepositoDecreto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioDataDepositoDecreto extends ActionSius implements ICostantiDepositoDecreto {

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

		// Lettura del Deposito Decreto.
		IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		DepositoDecretoModel llDecMod = lCtrl.ExRicercaDepositoDecretoByIdEvento(lDocAllMod.getEveIdEvento());
		if (llDecMod == null || llDecMod.getIdDepositoDecreto() == null)
			throw new F3BException(F3BException.EX_NOT_FOUND, "Errore nella lettura del Decreto");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>> IdDepositoDecreto = " + llDecMod.getIdDepositoDecreto());
		setRequestAttribute("depositoDecreto", llDecMod);
		// Il Deposito Decreto si mette in sessione. Luigi 11-10-2007
		setSessionAttribute("lDepositoDecreto", llDecMod);

		// Lettura delle notifiche.
		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(llDecMod.getIdEventoGenerato());
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

		return PG_LOAD_DETTAGLIO_DEPOSITO_DECRETO;
	}

}