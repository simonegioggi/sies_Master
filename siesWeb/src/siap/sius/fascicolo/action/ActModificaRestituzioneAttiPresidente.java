package siap.sius.fascicolo.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
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
 * MEV_2019-09: aggiunta action di modifica dati
 *
 * @author Gioggi
 */
public class ActModificaRestituzioneAttiPresidente extends ActionSius implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		gestioneRitorno();

		// String retPage = PG_LOAD_GESTIONE_RESTITUZIONE_ATTI_PRESIDENTE;
		FascicoloGPModel fgpm = null;

		// il Fascicolo è in sessione
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dati del Fascicolo non in sessione!");
		fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		if (fgpm == null || fgpm.getFascicoloSiusModel() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo non trovato!");

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
		if (!isRequestParameterNullEmptyObj(CAMPO_NOTE))
			gpm.setDescrRestituzione(getRequestStringParameter(CAMPO_NOTE));
		else
			gpm.setDescrRestituzione(null);
		gpm.setDataRestituzione(getRequestDateParameter(CAMPO_ANNO_DATA_RESTITUZIONE,
				CAMPO_MESE_DATA_RESTITUZIONE, CAMPO_GIORNO_DATA_RESTITUZIONE));
		igp.ExModificaDatiRestituzioneGeneraleProcedimento(gpm);

		// se siamo in inserimento allora salvo e modifico stato fascicolo
		// aggiorno dati sulla tabella FASCICOLO_SIUS
		if (COD_EMESSO_DECRETO_DESIGNAZIONE.equals(fgpm.getFascicoloSiusModel().getCodStatoFascicolo())) {
			IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloSiusModel fsm = new FascicoloSiusModel();
			fsm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			fsm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			fsm.setDataAggiornamento(DateUtils.getSysDate());
			fsm.setCodStatoFascicolo(ICostantiFascicoloSius.COD_ATTI_RESTITUITI_PRESIDENTE);
			fsm.setIdFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
			ifs.aggiornaStatoFascicoloSius(fsm);

			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserimento Avvenuto Correttamente!");

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
			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("dataRestituzioneStr", Utils.isNullObj(gpm.getDataRestituzione()) ? null
				: DateUtils.getDateToString(gpm.getDataRestituzione(), "dd/MM/yyyy"));
		setRequestAttribute("descrRestituzione", gpm.getDescrRestituzione());

		setRequestAttribute("modalita", "dettaglio");

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Avvenuto Correttamente!");
		RedirectTo rt = new RedirectTo();
		rt.setPage(IWebConstants.PG_MAIN);
		rt.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo");
		rt.setParameter(CAMPO_ID_FASCICOLO_SIUS,
				fgpm.getFascicoloSiusModel().getIdFascicoloSius().toString());
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return IWebConstants.PG_MESSAGE; // return retPage;
	}

}