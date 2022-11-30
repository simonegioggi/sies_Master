package siap.sius.fascicolo.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_9: aggiunta action di inserimento dati
 *
 * @author Gioggi
 */
public class ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA extends ActionSius
		implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		gestioneRitorno();

		String retPage = PG_DETTAGLIO_ESECUTIVITA_ORDINANZA_APPLICAZIONE_PROVVISORIA_MA;
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

		// aggiorno dati sulla tabella "deposito_ordinanza_pc" (colonne "DATA_ESECUTIVITA" e "NOTE_ATTI")
		IDepositoOrdinanzaPc idopc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepositoOrdinanzaPcModel dopcm = idopc.ExRicercaDepositoOrdinanzaPcByGenProc(
				fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		dopcm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		dopcm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		dopcm.setDataAggiornamento(DateUtils.getSysDate());
		if (!isRequestParameterNullEmptyObj(CAMPO_NOTE))
			dopcm.setNoteAtti(getRequestStringParameter(CAMPO_NOTE));
		else
			dopcm.setNoteAtti(null);
		dopcm.setDataEsecutivita(getRequestDateParameter(CAMPO_ANNO_DATA_ESECUTIVITA,
				CAMPO_MESE_DATA_ESECUTIVITA, CAMPO_GIORNO_DATA_ESECUTIVITA));
		idopc.ExModificaDepositoOrdinanzaPc(dopcm);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return retPage;
	}

}