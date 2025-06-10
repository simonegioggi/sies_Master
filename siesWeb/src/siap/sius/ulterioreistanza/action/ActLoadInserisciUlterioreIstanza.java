package siap.sius.ulterioreistanza.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * ActLoadInserisciUlterioreIstanza - Classe Action per la load inserisci di UlterioreIstanza
 *
 * @version 1.0
 */
public class ActLoadInserisciUlterioreIstanza extends ActionSius implements ICostantiUlterioreIstanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Caricamento della form d'inserimento.
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// Verifica se esiste il fascicolo in Sessione.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Selezionare il procedimento SIUS.");

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		if (lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo()
				.compareTo(ICostantiFascicoloSius.COD_DEFINITO) == 0
				|| lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo()
						.compareTo(ICostantiFascicoloSius.COD_UNIFICATO) == 0
				|| lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo()
						.compareTo(ICostantiFascicoloSius.COD_EMESSO_PROVVEDIMENTOO) == 0)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"L'inserimento di ulteriori istanze " + "non è consentito per i procedimenti definiti!");

		// Crea la lista per il Tipo Atto, ed inserisce in request.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAtto());
		setRequestAttribute("tipoAtto", "" + lOption);

		// Crea la lista per il Mittente Atto, ed inserisce in request.
		lOption = new Option(DecodificheManager.getInstance().getMittenteAtto(), 36);
		setRequestAttribute("mittenteAtto", "" + lOption);

		// Imposta Contenuto.
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		// In funzione del tipo di ufficio, ritorna l'elenco dei contenuti per la combobox
		if (strCodTipoUfficio.equals("TDS"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDS(), 75);
		else if (strCodTipoUfficio.equals("UDS"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDS(), 75);
		else if (strCodTipoUfficio.equals("TDSM"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDSM(), 75);
		else if (strCodTipoUfficio.equals("UDSM"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), 75);
		else
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimento(), 75);

		// Imposta l'elenco dei contenuti.
		setRequestAttribute("contenuto", "" + lOption);

		// Imposta Modalità Inserimento.
		setRequestAttribute("modalita", "I");

		// Redirect per tornare indietro.
		String lTornaQui = StringUtils.urlEncode(IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=" + this.getRequest().getParameter(IWebConstants.ACTION_FIELD));
		setRequestAttribute("TornaQui", lTornaQui);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		// restituisce la jsp di VIEW
		return PG_LOAD_INSERISCIULTERIOREISTANZA;
	}

}