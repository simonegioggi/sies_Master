package siap.sius.ulterioreistanza.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.ulterioreistanza.controller.IUlterioreIstanza;
import siap.sius.ulterioreistanza.model.UlterioreIstanzaModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActLoadInserisciUlterioreIstanza - Classe Action per la load Modifica di UlterioreIstanza
 *
 * @version 1.0
 */
public class ActLoadModificaUlterioreIstanza extends ActionSius implements ICostantiUlterioreIstanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Caricamento della form di modifica.
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// Esegue controllo di eseistenza del fascicolo in sessione.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Selezionare il procedimento SIUS.");

		// Recupera i dati dell'ulteriore istanza interessata alla modifica dal dbase.
		IUlterioreIstanza lCtrl = SIUSLookupRemote.getUlterioreIstanzaRemote();
		UlterioreIstanzaModel lUltIstMod = lCtrl.ExRicercaUlterioreIstanzaByKey(
				getRequestBigDecimalParameter(ICostantiUlterioreIstanza.CAMPO_ID_ULTERIORE_ISTANZA));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + "#### Contenuto di UltIstMod : " + lUltIstMod);

		// Imposta Tipo Atto.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAtto());
		lOption.setSelected(lUltIstMod.getCodTipoAtto()); // Si posizione sul valore corrente
		setRequestAttribute("tipoAtto", "" + lOption);

		// Imposta Mittente Atto.
		lOption = new Option(DecodificheManager.getInstance().getMittenteAtto(), 36);
		lOption.setSelected(lUltIstMod.getCodTipoMittenteAtto()); // Si posiziona sul valore corrente.
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

		// Si posiziona sul valore corrente
		lOption.setSelected(lUltIstMod.getCodOggettoProcedimento());
		setRequestAttribute("contenuto", "" + lOption); // Imposta l'elenco dei contenuti.

		// Imposta l'Ulteriore Istanza Model in request.
		setRequestAttribute("ulterioreIstanza", lUltIstMod);

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

		// Redirect per tornare indietro
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