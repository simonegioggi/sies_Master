package siap.regesies.regesentenza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesentenza.controller.IImportaDati;
import siap.regesies.regesentenza.model.EsitoImportModel;
import siap.regesies.regesentenza.model.ProvvedimentoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/*********************************************************
 * <p>
 * Title: ActIntegraFascicolo
 * </p>
 * <p>
 * Description: Importa i dati in SIEP selezionati da Rege
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 ********************************************************/
public class ActIntegraFascicolo extends ActionRegeSiap implements ICostantiRegeSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		ProvvedimentoModel lProvvedimento = getProvvedimentoRegeInSession();
		lProvvedimento.setUtente(getUtenteConnesso().getUserId());
		lProvvedimento.setUfficio(getCodUfficioUtenteConnesso());
		lProvvedimento.setDataInserimento(DateUtils.getSysDate());

		EsitoImportModel lEsito = new EsitoImportModel();
		lEsito.setProvvedimento(lProvvedimento);

		// Importa i dati in SIep
		IImportaDati lCtrl = RegeSiesLookupRemote.getImportaDati();
		BigDecimal lKey = lCtrl.ExIntegraFascicoloSiep(lEsito);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("Chiave Fascicolo = " + lKey);

		setRequestAttribute("esito", lCtrl.getEsito());
		setRequestAttribute("KeyFascicolo", lKey);

		String lPage = PG_ESITO_INTEGRAZIONE;
		return lPage;
	}

}