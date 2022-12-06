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
 * MEV_9: aggiunta action di caricamento dati
 *
 * @author Gioggi
 */
public class ActLoadModificaRestituzioneAttiPresidente extends ActionSius implements ICostantiFascicoloSius {

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

		// ricerco dati sulla tabella Generale_Procedimento
		IGeneraleProcedimento igp = SIUSLookupRemote.getGeneraleProcedimentoRemote();
		GeneraleProcedimentoModel gpm = igp
				.ExRicercaGeneraleProcedimentoByFascicolo(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		if (Utils.isNullObj(gpm.getDataRestituzione())) {
			setRequestAttribute("modalita", "inserimento");
			setRequestAttribute("dataRestituzioneStr", null);
		} else {
			setRequestAttribute("modalita", "modifica");
			setRequestAttribute("dataRestituzioneStr",
					DateUtils.getDateToString(gpm.getDataRestituzione(), "dd/MM/yyyy"));
		}
		setRequestAttribute("descrRestituzione", gpm.getDescrRestituzione());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return retPage;
	}

}