package siap.sige.fascicolo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;

/**
 * ActInserisciFascicolo - Classe Azione di inserimento del Fascicolo SIGE
 * 
 * @version 1.0
 */
public class ActModificaFascicolo extends ActInserisciFascicolo implements ICostantiFascicoloSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		if (isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new F3BException(F3BException.USER_MESSAGE,
					"Dati del Procedimento SIGE non in sessione !!");

		// Lettura del Fascicolo in sessione
		FascicoloSigeEstesoModel lFascicoloEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		// Controllo Dati
		if (lFascicoloEsteso.getFascicoloSige() == null
				|| lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "ID Procedimento SIGE assente !!");
		if (lFascicoloEsteso.getFascicoloSige() == null
				|| lFascicoloEsteso.getFascicoloSige().getRicIdRichiestaSige() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "ID Richiesta SIGE assente !!");

		BigDecimal lIdFascicoloSige = lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige();
		BigDecimal lIdRichiestaSige = lFascicoloEsteso.getFascicoloSige().getRicIdRichiestaSige();

		if (IsFascicoloSigeModificabile()) {
			// Lettura dei dati dalla form
			mFascicolo = leggiDatiFascicolo();
			mRichiesta = leggiDatiRichiesta();
		} else {
			// Se non modificabile si consente la modifica del solo campo NOTE
			mFascicolo = lFascicoloEsteso.getFascicoloSige();
			mRichiesta = lFascicoloEsteso.getRichiestaSige();
			mFascicolo.setNote(getRequestStringParameter(CAMPO_NOTE));
		}

		// Valorizzazione ulteriori dati
		mFascicolo.setIdFascicoloSige(lIdFascicoloSige);
		mFascicolo.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		mFascicolo.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		mFascicolo.setDataAggiornamento(DateUtils.getSysDate());

		mRichiesta.setIdRichiestaSige(lIdRichiestaSige);
		mRichiesta.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		mRichiesta.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		mRichiesta.setDataAggiornamento(DateUtils.getSysDate());

		// Viene istanziato il controller per attuare la modifica del Fascicolo SIGE
		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		lCtrl.ExModificaFascicoloSige(mFascicolo, mRichiesta);

		// Prepara la "pagina" di destinazione
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.fascicolo.action.ActLoadDettaglioFascicolo");
		lRedirigi.setParameter(CAMPO_ID_FASCICOLO_SIGE,
				(lIdFascicoloSige != null ? lIdFascicoloSige.toString() : ""));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");
		return lRedirigi.toString();
	}

}