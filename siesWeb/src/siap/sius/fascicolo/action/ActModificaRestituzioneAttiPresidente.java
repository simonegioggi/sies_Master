package siap.sius.fascicolo.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.controller.IGeneraleProcedimento;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_9: aggiunta action di modifica dati
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

		String retPage = PG_LOAD_GESTIONE_RESTITUZIONE_ATTI_PRESIDENTE;
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
		setRequestAttribute("dataRestituzioneStr", Utils.isNullObj(gpm.getDataRestituzione()) ? null
				: DateUtils.getDateToString(gpm.getDataRestituzione(), "dd/MM/yyyy"));
		setRequestAttribute("descrRestituzione", gpm.getDescrRestituzione());

		setRequestAttribute("modalita", "dettaglio");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return retPage;
	}

}