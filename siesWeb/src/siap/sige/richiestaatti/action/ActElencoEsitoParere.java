package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.util.SIGELookupRemote;
import siap.sius.SIUSException;

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

public class ActElencoEsitoParere extends ActRicercaFSigePuntuale implements ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		this.setLinkRitorno();

		super.processRequest();

		BigDecimal lIdFascicolo = null;

		// Recupero del FascicoloSige. Se non in sessione solleva un errore di eccezione.
		if (isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Procedimento non selezionato");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");
		lIdFascicolo = lFasEsteso.getFascicoloSige().getIdFascicoloSige();

		IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();

		String lTipiProvv = "'52'"; // CODICE PROVVEDIMENTO PARERE
		String lCodProvvSige = "'11'";
		Vector lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvvProvvSige(lIdFascicolo, lTipiProvv,
				lCodProvvSige);
		setRequestAttribute("provvedimenti", lVect);

		// ************************************************************* CONTINUARE
		// FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
		// IEvento mCtrl = SICOLookupRemote.getEventoRemote();
		// Vector lVect =
		// mCtrl.ExRicercaEventoByFascEsitoParereInamm(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),COD_EVENTO_PROVVEDIMENTO);
		// setRequestAttribute("provvedimenti", lVect);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");
		return PG_ELENCOESITOPARERE;
	}

}