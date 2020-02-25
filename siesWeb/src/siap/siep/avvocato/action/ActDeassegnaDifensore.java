package siap.siep.avvocato.action;

/**
 * <p>Title: ActDeassegnaDifensore</p>
 * <p>Description: Classe Action per la Deassegnazione di Avvocato Difensore</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
//import siap.siep.avvocato.controller.AvvocatoController;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

@SuppressWarnings("rawtypes")
public class ActDeassegnaDifensore extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione di Deassegnazione di un Avvocato da un fascicolo
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {
		BigDecimal lIdFascicolo = ((FascicoloSiepModel) (getSessionAttribute("fascicolo")))
				.getIdFascicoloSiep();
		BigDecimal lId = getRequestBigDecimalParameter("tipo"); // id avvocato
		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		AvvocatoSiepModel lAvvSiep = lCtrl.ExRicercaAvvocatoFascicoloSiepByIdAvvocatoIdFascicolo(lId,
				lIdFascicolo);

		AvvocatoFascicoloSiepModel lAvvFasc = new AvvocatoFascicoloSiepModel();

		lAvvFasc = lAvvSiep.getAvvocatoFascicoloSiepModel();
		lAvvFasc.setAvvIdAvvocato(lId);
		lAvvFasc.setDataFineValidita(DateUtils.getSysDate());
		lAvvFasc.setFasSieIdFascicoloSiep(lIdFascicolo);
		lAvvFasc.setIdAvvocatoFascicoloSiep(
				lAvvSiep.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep());
		lAvvFasc.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAvvFasc.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lAvvFasc.setDataAggiornamento(DateUtils.getSysDate());
		lAvvFasc.setMotivo("Revoca");
		lAvvFasc = lCtrl.ExDeassegnaAvvocato(lAvvFasc);

		// paolo levo da qui sposto sopra
		// IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		// AvvocatoSiepModel lAvvSiep = lCtrl.ExRicercaAvvocatoFascicoloSiepByIdAvvocatoIdFascicolo(lId,
		// lIdFascicolo);
		// AvvocatoFascicoloSiepModel lAvvModificato = new AvvocatoFascicoloSiepModel();

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
		lAvvFascMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);

		Vector lVect = null;
		try {
			lVect = new Vector();
			lVect = lCtrl.ExRicercaAvvocatiAttualiFascicolo(lAvvMod, lAvvFascMod);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" nessun elemento trovato");

		}

		if (lVect.size() > 0) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Deassegnazione  Difensore Avvenuta Correttamente!");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		} else {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Revoca  Difensore Avvenuta Correttamente!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ lAvvFascMod.getFasSieIdFascicoloSiep());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}

}