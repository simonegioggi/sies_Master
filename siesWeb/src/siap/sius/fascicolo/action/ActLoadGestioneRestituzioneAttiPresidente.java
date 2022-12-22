package siap.sius.fascicolo.action;

import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.controller.IGeneraleProcedimento;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_9: aggiunta action di caricamento dati
 *
 * @author Gioggi
 */
public class ActLoadGestioneRestituzioneAttiPresidente extends ActionSius implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// variabile di classe
	private FascicoloSiepModel fsm = null;
	private Date dataRestituzione = null;
	private boolean isReadOnly = false;

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
					"Restituzione Procedimento non consentita con campo Oggetto vuoto!");
		}

		// ricerco dati sulla tabella Generale_Procedimento
		IGeneraleProcedimento igp = SIUSLookupRemote.getGeneraleProcedimentoRemote();
		GeneraleProcedimentoModel gpm = igp
				.ExRicercaGeneraleProcedimentoByFascicolo(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		dataRestituzione = gpm.getDataRestituzione();

		retPage = analisiStatoFascicolo(fgpm);

		if (!fascicoloInSessione) {
			setSessionAttribute("fascicoloSiusGP", fgpm);
			setSessionAttribute("fascicolo", fsm);
		}

		if (Utils.isNullObj(dataRestituzione) && COD_EMESSO_DECRETO_DESIGNAZIONE
				.equals(fgpm.getFascicoloSiusModel().getCodStatoFascicolo())) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			rt.setAction("siap.sius.fascicolo.action.ActLoadModificaRestituzioneAttiPresidente");
			rt.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS,
					fgpm.getFascicoloSiusModel().getIdFascicoloSius().toString());
			// valore di ritorno
			return rt.toString();
		} else {
			setRequestAttribute("dataRestituzioneStr",
					DateUtils.getDateToString(dataRestituzione, "dd/MM/yyyy"));
			setRequestAttribute("descrRestituzione", gpm.getDescrRestituzione());
			if (isReadOnly)
				setRequestAttribute("modalita", "readOnly");
			else
				setRequestAttribute("modalita", "dettaglio");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
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

		// valore di ritorno
		return fgpm;
	}

	/**
	 * La funzione analizza il Fascicolo SIUS ed in base allo stato prepara la form da presentare. I casi
	 * sono: 1) STATO = COD_UNIFICATO, COD_EMESSO_PROVVEDIMENTOO : viene lanciata un'eccezione, l'operazione
	 * non può essere eseguita. 2) STATO = COD_DEFINITO : il fascicolo è già in stato definito, viene
	 * visualizzato il dettaglio della definizione. 3) Negli altri casi viene preparata la form di input per
	 * la Gestione Restituzione atti al Presidente del procedimento.
	 *
	 * @param fgpm
	 * @return String pagina di input o di dettaglio
	 * @throws Exception
	 */
	private String analisiStatoFascicolo(FascicoloGPModel fgpm) throws Exception {

		String retPage = PG_LOAD_GESTIONE_RESTITUZIONE_ATTI_PRESIDENTE;
		if (fgpm == null || fgpm.getFascicoloSiusModel() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo non trovato!");

		if (!(COD_ATTI_RESTITUITI_PRESIDENTE.equals(fgpm.getFascicoloSiusModel().getCodStatoFascicolo())
				|| COD_EMESSO_DECRETO_DESIGNAZIONE
						.equals(fgpm.getFascicoloSiusModel().getCodStatoFascicolo()))
				&& Utils.isNullObj(dataRestituzione))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione consentita solo su Procedimento in stato di 'Restituiti Atti al Presidente'"
							+ " oppure 'Emesso Decreto Designazione'!");

		if (fgpm.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase(COD_UNIFICATO))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita su Procedimento Unificato!");

		if (fgpm.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase(COD_EMESSO_PROVVEDIMENTOO)
				&& Utils.isNullObj(dataRestituzione))
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

		if (!(COD_ATTI_RESTITUITI_PRESIDENTE.equals(fgpm.getFascicoloSiusModel().getCodStatoFascicolo())
				|| COD_EMESSO_DECRETO_DESIGNAZIONE
						.equals(fgpm.getFascicoloSiusModel().getCodStatoFascicolo()))
				&& !Utils.isNullObj(dataRestituzione))
			isReadOnly = true;

		// valore di ritorno
		return retPage;
	}

}