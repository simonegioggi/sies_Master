package siap.sius.fascicolo.action;

import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
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

	boolean existOrdinanzaApplicazioneProvvisoria = false;

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

		// Ricerco evento del fascicolo:
		// Ordinanza Affidamento in Prova al Servizio Sociale (Art. 47 O.P. - Art. 678 comma 1-ter
		// c.p.p.) - Applica provvisoriamente
		EventoModel em = new EventoModel();
		IEvento ie = SICOLookupRemote.getEventoRemote();
		if (!isRequestParameterNullObj("IdEvento")) {
			em = ie.ExRicercaEventoByKey(getRequestBigDecimalParameter("IdEvento"));
			controllaEvento(em, true);
		} else {
			Vector<?> v = ie.ExRicercaEventoByFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius(),
					null);
			for (int i = 0; i < v.size(); i++) {
				em = (EventoModel) v.elementAt(i);
				controllaEvento(em, false);
				if (existOrdinanzaApplicazioneProvvisoria)
					break;
			}
		}
		if (!existOrdinanzaApplicazioneProvvisoria)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione consentita solo se sul Procedimento sia stata emessa un'ordinanza di "
							+ "Applicazione Provvisoria M.A. con esito 'Applica provvisoriamente' "
							+ "depositata e validata!");

		// aggiorno dati sulla tabella "deposito_ordinanza_pc" (colonne "DATA_ESECUTIVITA" e "NOTE_ATTI")
		IDepositoOrdinanzaPc idopc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepositoOrdinanzaPcModel dopcm = idopc.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(
				fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), "AM");
		DepositoOrdinanzaPcModel dopcmMA = idopc.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(
				fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), "MA");
		boolean existConfermaDecisioneMR = false;
		if (!Utils.isNullObj(dopcmMA))
			existConfermaDecisioneMR = true;
		// DepositoOrdinanzaPcModel dopcm = new DepositoOrdinanzaPcModel();
		// dopcm.setGenPridGeneraleProcedimento(
		// fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		// Vector<?> depositoOrdinanzaVector = idopc.ExRicercaDepositoOrdinanzaPc(dopcm);
		// setRequestAttribute("depositoOrdinanzaVector", depositoOrdinanzaVector);
		// controllo consistenza della data esecutivita': se non esiste allora la gestisco
		if ((!Utils.isPresent(dopcm.getDataEsecutivita()) || !isRequestParameterNullObj("provenienza"))
				&& !existConfermaDecisioneMR) {
			dopcm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			dopcm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			dopcm.setDataAggiornamento(DateUtils.getSysDate());
			dopcm.setNoteAtti(null);
			if ("cancella".equals(getRequestStringParameter("provenienza"))) {
				dopcm.setDataEsecutivita(null);
				idopc.ExModificaDepositoOrdinanzaPc(dopcm);
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
		setRequestAttribute("Upload", "NO");
		setRequestAttribute("ListaTemplate", "SIUS_OR_0270");
		setRequestAttribute("existConfermaDecisioneMR", "" + existConfermaDecisioneMR);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return retPage;
	}

	private boolean controllaEvento(EventoModel em, boolean existIdEvento) throws F3BException {

		if (existIdEvento) {
			existOrdinanzaApplicazioneProvvisoria = true;
			setRequestAttribute("eventoModel", em);
		} else {
			if ("0270".equals(em.getCodEsito()) && "S".equals(em.getFlagDocumentoRegistrato())
					&& em.getNumAllValidati() > 0) {
				existOrdinanzaApplicazioneProvvisoria = true;
				setRequestAttribute("eventoModel", em);
				INotifica in = SIEPLookupRemote.getNotificaRemote();
				Date maxDataAvvenutaNotifica = in.ExRicercaDataNotifica(em.getIdEvento());
				String mdan = "";
				if (maxDataAvvenutaNotifica != null)
					mdan = DateUtils.getDateToString(maxDataAvvenutaNotifica, "dd/MM/yyyy");
				setRequestAttribute("maxDataAvvenutaNotifica", mdan);
				siesLogger.debug("maxDataAvvenutaNotifica = " + mdan);
			}
		}
		return existOrdinanzaApplicazioneProvvisoria;
	}

}