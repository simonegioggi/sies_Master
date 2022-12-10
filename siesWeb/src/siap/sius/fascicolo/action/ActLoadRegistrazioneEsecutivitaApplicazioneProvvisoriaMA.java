package siap.sius.fascicolo.action;

import java.util.Date;
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
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_9: aggiunta action di caricamento dati
 *
 * @author Gioggi
 */
public class ActLoadRegistrazioneEsecutivitaApplicazioneProvvisoriaMA extends ActionSius
		implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// variabile di classe
	private FascicoloSiepModel fsm = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		gestioneRitorno();

		String retPage = null; // pagina di input
		FascicoloGPModel fgpm = null;
		boolean fascicoloInSessione = false;

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO)) {
			fgpm = ricercaFascicolo();
		} else {
			// il Fascicolo è in sessione
			if (isSessionAttributeNullObj("fascicoloSiusGP"))
				throw new SIUSException(SIUSException.USER_MESSAGE, "Dati del Fascicolo non in sessione!");
			fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
			fascicoloInSessione = true;
		}
		if (fgpm.getTenori() == null || fgpm.getTenori().length == 0) {
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Esecutivita' Ordinanza Applicazione Provvisoria M.A. non consentita con campo Oggetto vuoto!");
		}

		retPage = analisiStatoFascicolo(fgpm);

		if (!fascicoloInSessione) {
			setSessionAttribute("fascicoloSiusGP", fgpm);
			setSessionAttribute("fascicolo", fsm);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return retPage;
	}

	// Il Fascicolo viene cercato nel DB attraverso le chiavi ANNO e PROG
	private FascicoloGPModel ricercaFascicolo() throws Exception {

		if (isRequestParameterNullObj(CAMPO_CHIAVE_ANNO) || isRequestParameterNullObj(CAMPO_CHIAVE_PROGR))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Assenti ANNO/PROG!");

		FascicoloGPModel fgpm = null;
		IFascicoloSius ifss = SIUSLookupRemote.getFascicoloSiusRemote();
		fgpm = ifss.ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(
				getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
				getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), getCodUfficioUtenteConnesso());

		// Si cerca il fascicolo SIEP da mettere in sessione
		if (fgpm != null && fgpm.getFascicoloSiusModel() != null
				&& fgpm.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
			IFascicoloSiep ifsp = SIEPLookupRemote.getFascicoloSiepRemote();
			fsm = ifsp
					.ExRicercaFascicoloByKeyNoError(fgpm.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		}

		return fgpm;
	}

	/**
	 * La funzione analizza il Fascicolo SIUS ed in base allo stato prepara la form da presentare. I casi
	 * sono: 1) STATO = COD_UNIFICATO, COD_EMESSO_PROVVEDIMENTOO : viene lanciata un'eccezione, l'operazione
	 * non può essere eseguita. 2) STATO = COD_DEFINITO : il fascicolo è già in stato definito, viene
	 * visualizzato il dettaglio della definizione. 3) Negli altri casi viene preparata la form di input per
	 * la Esecutivita' Ordinanza Applicazione Provvisoria M.A. del procedimento.
	 *
	 * @param fgpm
	 * @return String pagina di input o di dettaglio
	 * @throws Exception
	 */
	private String analisiStatoFascicolo(FascicoloGPModel fgpm) throws Exception {

		String retPage = PG_LOAD_ESECUTIVITA_ORDINANZA_APPLICAZIONE_PROVVISORIA_MA;
		if (fgpm == null || fgpm.getFascicoloSiusModel() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo non trovato!");

		// Ricerco evento del fascicolo:
		// Ordinanza Affidamento in Prova al Servizio Sociale (Art. 47 O.P. - Art. 678 comma 1-ter
		// c.p.p.) - Applica provvisoriamente
		IEvento ie = SICOLookupRemote.getEventoRemote();
		Vector<?> v = ie.ExRicercaEventoByFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius(),
				null);
		boolean existOrdinanzaApplicazioneProvvisoria = false;
		for (int i = 0; i < v.size(); i++) {
			EventoModel em = (EventoModel) v.elementAt(i);
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
				break;
			}
		}
		if (!existOrdinanzaApplicazioneProvvisoria)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione consentita solo se sul Procedimento sia stata emessa un'ordinanza di "
							+ "Applicazione Provvisoria M.A. con esito 'Applica provvisoriamente' "
							+ "depositata e validata!");

		if (fgpm.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase(COD_UNIFICATO))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita su Procedimento Unificato!");

		if (fgpm.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase(COD_EMESSO_PROVVEDIMENTOO)
				&& !existOrdinanzaApplicazioneProvvisoria)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita su Procedimento con Provvedimento!");

		if (getCodUfficioUtenteConnesso().compareTo(fgpm.getFascicoloSiusModel().getChiaveUfficio()) != 0)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita per Procedimento di altro ufficio!");

		// controllo consistenza della data esecutivita'
		IDepositoOrdinanzaPc idopc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepositoOrdinanzaPcModel dopcm = idopc.ExRicercaDepositoOrdinanzaPcByGenProc(
				fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		if (!Utils.isNullObj(dopcm) && Utils.isPresent(dopcm.getDataEsecutivita())) {
			if (isRequestParameterNullObj("provenienza")) {
				// Prepara la "pagina" di destinAction
				RedirectTo rt = new RedirectTo();
				rt.setPage(IWebConstants.PG_MAIN);
				rt.setAction(
						"siap.sius.fascicolo.action.ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(getClass().getName() + ".processRequest: fine");

				// valore di ritorno
				return rt.toString();
			} else {
				setRequestAttribute("dataEsecutivita",
						DateUtils.getDateToString(dopcm.getDataEsecutivita(), "dd/MM/yyyy"));
				setRequestAttribute("noteAtti", dopcm.getNoteAtti());
				setRequestAttribute("provenienza", "modifica");
			}
		}

		// possibile inserire Restituzione Procedimento e quindi lock
		// Lock per evitare più definizioni contemporanee del Fascicolo
		LockModel lm = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIUS",
				fgpm.getFascicoloSiusModel().getIdFascicoloSius().toString(), getCodUtenteConnesso(),
				getSession().getId());
		if (lm != null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Il " + lm.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi!");

		// valore di ritorno
		return retPage;
	}

}