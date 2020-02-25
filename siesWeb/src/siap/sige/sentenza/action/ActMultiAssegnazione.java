package siap.sige.sentenza.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActMultiAssegnazione
 * </p>
 * <p>
 * Description: Classe Azione di assegnazione di piu Sentenze ad un Fascicolo SIGE.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 */
public class ActMultiAssegnazione extends ActionSige implements ICostantiFasSigeSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Interfaccia del Controller Sentenza Sige
	private IFasSigeSentenza mCtrl = null;

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		// Viene istanziato il controller per attuare l'assegnazione della Sentenza al Fascicolo SIGE
		mCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();

		FascicoloSigeModel lFascicolo = null;
		SentenzaSigeModel lFasSigeSen = new SentenzaSigeModel();

		// I dati del Fascicolo Sige sono in sessione
		lFascicolo = getFascicoloSigeInSessione();
		if (lFascicolo == null || lFascicolo.getIdFascicoloSige() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Mancano i dati del Fascicolo in sessione !");
		lFasSigeSen.setFasIdFascicoloSige(lFascicolo.getIdFascicoloSige());

		String[] lCodiciTipoProcedimentoSiep = null;
		if (!isRequestParameterNullObj("tipoProcedimentoSiep"))
			lCodiciTipoProcedimentoSiep = this.getRequestStringParameters("tipoProcedimentoSiep");
		else
			throw new F3BException(F3BException.USER_MESSAGE, "Selezionare almeno un procedimento!");

		// Lettura del fascicolo SIEP
		FascicoloSiepModel lFasSiep = null;
		for (int i = 0; i < lCodiciTipoProcedimentoSiep.length; i++) {
			String idFascicoloSiep = lCodiciTipoProcedimentoSiep[i];
			lFasSiep = new FascicoloSiepModel();
			IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasSiep = lCtrl.ExRicercaFascicoloByKey(new BigDecimal(idFascicoloSiep));
			// Eventuale ID Fascicolo SIEP
			lFasSigeSen.setFasSieIdFascicoloSiep(new BigDecimal(idFascicoloSiep));
			lFasSigeSen.setIdFasSigeSentenza(lFasSiep.getSenIdSentenza());
			lFasSigeSen.setIdSentenza(lFasSiep.getSenIdSentenza());
			// Valorizzazione dati di sistema
			lFasSigeSen.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lFasSigeSen.setCodOperatoreInserimento(getCodUtenteConnesso());
			lFasSigeSen.setDataInserimento(DateUtils.getSysDate());
			lFasSigeSen.setCodUfficioAggiornamento(lFasSiep.getCodUfficioInserimento());
			lFasSigeSen.setCodOperatoreAggiornamento(lFasSiep.getCodOperatoreInserimento());
			lFasSigeSen.setDataAggiornamento(lFasSiep.getDataInserimento());
			lFasSigeSen.setDataIrrevocabilita(lFasSiep.getDataIrrevocabilita());
			lFasSigeSen.setFlagCompetenza("N");
			boolean risultato = false;
			Vector lVect = null;
			if (lFasSiep != null && lFasSiep.getSenIdSentenza() != null) {
				lVect = mCtrl.ExRicercaFascicoloSigeSentenzaAssociata(lFasSiep.getSenIdSentenza(),
						lFascicolo.getIdFascicoloSige());
			}
			if (lVect != null && lVect.size() > 0) {
				risultato = true;
			}
			if (!risultato) {
				lFasSigeSen = mCtrl.ExAssegnaSentenzaFascicoloSige(lFasSigeSen);
			}
		}

		// Prepara la "pagina" di destinazione
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.fascicolo.action.ActLoadDettaglioFascicolo");
		lRedirigi.setParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE, lFascicolo
				.getIdFascicoloSige().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");
		return lRedirigi.toString();
	}

}