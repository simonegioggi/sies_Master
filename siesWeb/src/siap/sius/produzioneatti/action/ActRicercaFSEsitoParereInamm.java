package siap.sius.produzioneatti.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.provvedimento.action.ICostantiProvvedimento;

/**
 * <p>
 * Title: ActRicercaFSProvvedimenti
 * </p>
 * <p>
 * Description: Azione per la ricerca dei Provvedimenti legati al fascicolo SIUS a partire da ANNO e PROGR del
 * Fascicolo.
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @version 1.0
 */

public class ActRicercaFSEsitoParereInamm extends ActRicercaFSPuntuale
		implements ICostantiProvvedimento, ICostantiFascicoloSius, ICostantiProduzioneAtti {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		this.setLinkRitorno();

		// genny 26/03/2004 commento
		super.processRequest();

		// Recupero del FascicoloSiusGP. Se non in sessione solleva un errore di eccezione.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Procedimento non selezionato");

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		IEvento mCtrl = SICOLookupRemote.getEventoRemote();

		Vector lVect = mCtrl.ExRicercaEventoByFascEsitoParereInamm(
				lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(), COD_EVENTO_PROVVEDIMENTO);
		setRequestAttribute("provvedimenti", lVect);
		// this.setFunctionsAvailableToRequest("siap.sius.provvedimento.action.ActRicercaEsitoParereInamm");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");
		return PG_ELENCOESITOPAREREINAMM;
	}

}