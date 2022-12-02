package siap.sius.fascicolo.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.util.SICOLookupRemote;
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
		// controllo consistenza della data esecutivita': se non esiste allora la gestisco
		if (!Utils.isPresent(dopcm.getDataEsecutivita()) || !isRequestParameterNullObj("provenienza")) {
			dopcm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			dopcm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			dopcm.setDataAggiornamento(DateUtils.getSysDate());
			dopcm.setNoteAtti(null);
			if ("cancella".equals(getRequestStringParameter("provenienza"))) {
				dopcm.setDataEsecutivita(null);
				idopc.ExModificaDepositoOrdinanzaPc(dopcm);
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
				return rt.toString();
			} else {
				if (!isRequestParameterNullEmptyObj(CAMPO_NOTE))
					dopcm.setNoteAtti(getRequestStringParameter(CAMPO_NOTE));
				dopcm.setDataEsecutivita(getRequestDateParameter(CAMPO_ANNO_DATA_ESECUTIVITA,
						CAMPO_MESE_DATA_ESECUTIVITA, CAMPO_GIORNO_DATA_ESECUTIVITA));
				idopc.ExModificaDepositoOrdinanzaPc(dopcm);
			}
		}

		setRequestAttribute("dataEsecutivita", dopcm.getDataEsecutivita());
		setRequestAttribute("noteAtti", dopcm.getNoteAtti());

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoModel em = new EventoModel();
		if (!isRequestParameterNullObj("IdEvento")) {
			em = ie.ExRicercaEventoByKey(getRequestBigDecimalParameter("IdEvento"));
		} else {
			Vector<?> v = ie.ExRicercaEventoByFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius(),
					null);
			for (int i = 0; i < v.size(); i++) {
				em = (EventoModel) v.elementAt(i);
				if ("0680".equals(em.getCodMotivo()) && "0270".equals(em.getCodEsito())
						&& "S".equals(em.getFlagDocumentoRegistrato()) && em.getNumAllValidati() > 0) {
					break;
				}
			}
		}
		setRequestAttribute("eventoModel", em);
		setRequestAttribute("Upload", "NO");
		setRequestAttribute("ListaTemplate", "SIUS_OR_0270");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return retPage;
	}

}