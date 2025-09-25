package siap.sius.fascicolo.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.controller.IGeneraleProcedimento;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_9: aggiunta action di cancellazione dati
 *
 * @author Gioggi
 */
public class ActCancellaRestituzioneAttiPresidente extends ActionSius implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		gestioneRitorno();

		FascicoloGPModel fgpm = null;

		// il Fascicolo è in sessione
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dati del Fascicolo non in sessione!");
		fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		if (fgpm == null || fgpm.getFascicoloSiusModel() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo non trovato!");

		if (fgpm.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase(COD_UNIFICATO))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita su Procedimento Unificato!");

		if (fgpm.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase(COD_EMESSO_PROVVEDIMENTO))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita su Procedimento con Provvedimento!");

		if (getCodUfficioUtenteConnesso().compareTo(fgpm.getFascicoloSiusModel().getChiaveUfficio()) != 0)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita per Procedimento di altro ufficio!");

		// possibile inserire Restituzione Procedimento e quindi lock
		// Lock per evitare più definizioni contemporanee del Fascicolo
		LockModel lm = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIUS",
				fgpm.getFascicoloSiusModel().getIdFascicoloSius().toString(), getCodUtenteConnesso(),
				getSession().getId());
		if (lm != null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Il " + lm.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi!");

		// aggiorno dati sulla tabella Generale_Procedimento
		IGeneraleProcedimento igp = SIUSLookupRemote.getGeneraleProcedimentoRemote();
		GeneraleProcedimentoModel gpm = fgpm.getGeneraleProcedimentoModel();
		gpm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		gpm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		gpm.setDataAggiornamento(DateUtils.getSysDate());
		gpm.setDescrRestituzione(null);
		gpm.setDataRestituzione(null);
		igp.ExModificaDatiRestituzioneGeneraleProcedimento(gpm);

		// aggiorno dati sulla tabella FASCICOLO_SIUS
		IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
		FascicoloSiusModel fsm = new FascicoloSiusModel();
		fsm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		fsm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		fsm.setDataAggiornamento(DateUtils.getSysDate());
		fsm.setCodStatoFascicolo(ICostantiFascicoloSius.COD_EMESSO_DECRETO_DESIGNAZIONE);
		fsm.setIdFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		ifs.aggiornaStatoFascicoloSius(fsm);

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Cancellazione Avvenuta Correttamente!");

		// Prepara la "pagina" di destinAction
		RedirectTo rt = new RedirectTo();
		rt.setPage(IWebConstants.PG_MAIN);
		rt.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo");
		rt.setParameter(CAMPO_ID_FASCICOLO_SIUS,
				fgpm.getFascicoloSiusModel().getIdFascicoloSius().toString());
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_MESSAGE; /* rt.toString(); */
	}

}