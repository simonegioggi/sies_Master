package siap.sige.fascicolo.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sige.SIGEException;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;

/**
 * Title: ActLoadDefinizioneProcedimento Description: Azione adibita all'operazione di Definizione manuale del
 * Procedimento SIGE. Se il Fascicolo risulta già Definito viene presentato il Dettaglio della Definizione.
 * negli altri casi viene presentata la form di input per la Definizione.
 *
 * @version 1.0
 */
public class ActLoadDefinizioneProcedimento extends ActRicercaFSigePuntuale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		gestioneRitorno();

		String lRetPage = null; // pagina di input

		// Si utilizza la classe padre per effettuare la ricerca del Fascicolo SIGE
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO) && !isRequestParameterNullObj(CAMPO_CHIAVE_ANNO))
			super.processRequest();

		lRetPage = analisiStatoFascicolo();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRetPage;
	}

	/**
	 * La funzione analizza il Fascicolo SIGE in sessione ed in base allo stato prepara la form da presentare.
	 * I casi sono: 1) STATO = COD_DEFINITO : il fascicolo è già in stato definito, viene visualizzato il
	 * dettaglio della definizione. 3) Negli altri casi viene preparata la form di input per la definizione
	 * del procedimento.
	 *
	 * @param aFasSiepeEstesoMod
	 * @return String pagina di input o di dettaglio
	 * @throws Exception
	 *             propaga errore di eccezione
	 */
	@SuppressWarnings("rawtypes")
	private String analisiStatoFascicolo() throws Exception {

		String lRetPage = PG_LOAD_DEFINIZIONE_PROCEDIMENTO;
		String lmodalita = null;

		// il Fascicolo si ricava dalla sessione
		FascicoloSigeModel fsm = getFascicoloSigeInSessione();

		// Costruzione dell'Option filtrata dal Codice tipo Ufficio
		// Nota: per il momento si utilizza la codifica "TIPO_DEFINIZIONE" (SIUS), se dovessero necessitare
		// codici specifici si definirà un nuovo dominio di codifica.
		Option lOption = new Option(DecodificheUtils.getDecodificheFiltrateByCodAlt(
				DecodificheManager.getInstance().getTipoDefinizione(),
				getUfficioUtenteConnesso().getCodTipoUfficio()));

		Vector lVect = null;
		ProvvedimentoSigeEventoModel provvSigeEveMod = null;
		IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
		String lTipiProvv = "'" + ICostantiProvvedimentoSige.DEFINIZIONE_MANUALE + "'"; // Definizione Manuale
		lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvv(fsm.getIdFascicoloSige(), lTipiProvv);
		if (lVect != null && lVect.size() > 0) {
			provvSigeEveMod = (ProvvedimentoSigeEventoModel) lVect.firstElement();
		}
		setRequestAttribute("provvedimento", provvSigeEveMod);

		if (fsm.getCodTipoDefinizione() != null) {
			lOption.setSelected(getFascicoloSigeInSessione().getCodTipoDefinizione());
			lmodalita = "dettaglio";

			setRequestAttribute("descrizione", fsm.getDescrDefinizione());
			setRequestAttribute("data_definizione", fsm.getDataDefinizione());
			// Modificabile e Cancellabile se Fascicolo appartiene allo stesso Ufficio dell'operatore
			// e se lo stato del Fascicolo (COD_STATO_FASCICOLO = 02) è ISCRITTO.
			// Quando lo stato del Fascicolo (COD_STATO_FASCICOLO = 01) è DEFINITO
			// vuol dire che è stata eseguita la Validazione, pertanto non è più modificabile.
			if (getCodUfficioUtenteConnesso().equalsIgnoreCase(fsm.getChiaveUfficio())
					&& fsm.getCodStatoFascicolo() != null
					&& fsm.getCodStatoFascicolo().equals(ICostantiFascicoloSige.COD_ISCRITTO)) {
				setRequestAttribute("Modificabile", "SI");
			} else {
				setRequestAttribute("Modificabile", "NO");
			}
		} else if (IsFascicoloSigeModificabile()) {
			// possibile inserire Definizione Procedimento
			// Lock
			lockApplicativoFascicoloSige();
			lmodalita = "inserimento";
		} else {
			// [SG] 20230314: messaggio piu' parlante
			IProvvedimentoSige ips = SIGELookupRemote.getProvvedimentoRemote();
			ProvvedimentoSigeEventoModel psem = ips
					.ExRicercaProvvedimentoDefinitorioByIdFascicolo(fsm.getIdFascicoloSige());
			String testoMSG = "";
			if (psem != null && psem.getProvvedimento() != null) {
				testoMSG = " Esiste un provvedimento di tipo "
						+ psem.getProvvedimento().getDescrTipoProvvedimentoSige() + " del "
						+ DateUtils.getDateToString(psem.getProvvedimento().getDataEmissione(), "dd/MM/yyyy")
						+ "! Procedere con la cancellazione.";
			}
			throw new SIGEException(SIGEException.USER_MESSAGE, "Operazione non consentita!" + testoMSG);
		}

		setRequestAttribute("TipoDefinizione", "" + lOption);
		setRequestAttribute("modalita", lmodalita);

		return lRetPage;
	}

}