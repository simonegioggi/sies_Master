package siap.sius.fascicolo.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
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
 * Description: Azione adibita all'operazione di Definizione del Procedimento SIUS. Se il Fascicolo risulta
 * già Definito viene presentato il Dettaglio della Definizione. Se il Fascicolo è in uno degli stati:
 * Unificato o Emesso Provvedimento, viene lanciata una Eccezione di Warning, negli altri casi viene 
 * presentata la form di input per la Definizione.
 *
 * @version 1.0
 */
public class ActLoadDefinizioneProcedimento extends ActionSius implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private FascicoloSiepModel mFasSIEP = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		this.gestioneRitorno();

		String lRetPage = null; // pagina di input
		FascicoloGPModel lFasGPMod = null;
		boolean fascicoloInSessione = false;

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO))
			lFasGPMod = ricercaFascicolo();
		else {
			// il Fascicolo è in sessione
			if (isSessionAttributeNullObj("fascicoloSiusGP"))
				throw new SIUSException(SIUSException.USER_MESSAGE, "Dati del Fascicolo non in sessione!");
			lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
			fascicoloInSessione = true;
		}
		if (lFasGPMod.getTenori() == null || lFasGPMod.getTenori().length == 0) {
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Definizione Procedimento non consentita con campo Oggetto vuoto!");
		}

		lRetPage = analisiStatoFascicolo(lFasGPMod);

		if (!fascicoloInSessione) {
			setSessionAttribute("fascicoloSiusGP", lFasGPMod);
			setSessionAttribute("fascicolo", mFasSIEP);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRetPage;
	}

	// Il Fascicolo viene cercato nel DB attraverso le chiavi ANNO e PROG
	private FascicoloGPModel ricercaFascicolo() throws Exception {

		if (isRequestParameterNullObj(CAMPO_CHIAVE_ANNO) || isRequestParameterNullObj(CAMPO_CHIAVE_PROGR))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Assenti ANNO/PROG !");

		FascicoloGPModel lFasGPMod = null;
		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lFasGPMod = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(
				getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
				getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), getCodUfficioUtenteConnesso());

		// Si cerca il fascicolo SIEP da mettere in sessione
		if (lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null
				&& lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
			IFascicoloSiep lCtrlSIEP = SIEPLookupRemote.getFascicoloSiepRemote();
			mFasSIEP = lCtrlSIEP.ExRicercaFascicoloByKeyNoError(
					lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		}

		return lFasGPMod;
	}

	/**
	 * La funzione analizza il Fascicolo SIUS ed in base allo stato prepara la form da presentare. I casi
	 * sono: 1) STATO = COD_UNIFICATO, COD_EMESSO_PROVVEDIMENTO : viene lanciata un'eccezione, l'operazione
	 * non può essere eseguita. 2) STATO = COD_DEFINITO : il fascicolo è già in stato definito, viene
	 * visualizzato il dettaglio della definizione. 3) Negli altri casi viene preparata la form di input per
	 * la definizione del procedimento.
	 *
	 * @param aFasGPMod
	 * @return String pagina di input o di dettaglio
	 * @throws Exception
	 */
	private String analisiStatoFascicolo(FascicoloGPModel aFasGPMod) throws Exception {

		String lRetPage = PG_LOAD_DEFINIZIONE_PROCEDIMENTO;
		String lcodTipoDefinizione = null;
		String lmodalita = null;
		if (aFasGPMod == null || aFasGPMod.getFascicoloSiusModel() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo non trovato!");

		if (aFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase(COD_UNIFICATO))

			// passo 4a
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita su Procedimento Unificato!");

		if (aFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo()
				.equalsIgnoreCase(COD_EMESSO_PROVVEDIMENTO))
			// passo 4a
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita su Procedimento con Provvedimento!");

		if (getCodUfficioUtenteConnesso()
				.compareTo(aFasGPMod.getFascicoloSiusModel().getChiaveUfficio()) != 0)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione non consentita per Procedimento di altro ufficio !");

		// Costruzione dell'Option filtrata dal Codice tipo Ufficio (UDS)
		// MERGE v10 COLLAUDO: modifica per favorire anche UDSM
		String codTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		if ("UDSM".equals(codTipoUfficio))
			codTipoUfficio = "UDS";
		Option lOption = new Option(DecodificheUtils.getDecodificheFiltrateByCodAlt(
				DecodificheManager.getInstance().getTipoDefinizione(), codTipoUfficio));

		if (aFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().equalsIgnoreCase(COD_DEFINITO)) {
			// passo 4b) Il Procedimento è già definito, si richiama il dettaglio
			IGeneraleProcedimento lGenProcCtrl = SIUSLookupRemote.getGeneraleProcedimentoRemote();
			GeneraleProcedimentoModel lGenProc = lGenProcCtrl.ExRicercaGeneraleProcedimentoByFascicolo(
					aFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

			lcodTipoDefinizione = lGenProc.getTipoDefinizione();
			lOption.setSelected(lcodTipoDefinizione);
			lmodalita = "dettaglio";
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Dettaglio : " + lcodTipoDefinizione);

			setRequestAttribute("descrizione", lGenProc.getDescrDefinizione());
			setRequestAttribute("data_definizione", lGenProc.getDataDefinizione());
		} else {
			// possibile inserire Definizione Procedimento e quindi lock
			// Lock per evitare più definizioni contemporanee del Fascicolo
			LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIUS",
					aFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString(), getCodUtenteConnesso(),
					getSession().getId());
			if (lck != null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Il  " + lck.getEntity()
						+ " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
			lmodalita = "inserimento";
		}

		setRequestAttribute("TipoDefinizione", "" + lOption);
		setRequestAttribute("modalita", lmodalita);

		return lRetPage;
	}

}