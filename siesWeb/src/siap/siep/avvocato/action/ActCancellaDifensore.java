package siap.siep.avvocato.action;

/**
* <p>Title: ActModificaDifensore</p>
* <p>Description: Classe Action per la Cancellazione di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

@SuppressWarnings("rawtypes")
public class ActCancellaDifensore extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Cancellazione Logica e Storicizzazione di un Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO);

		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvSieMod = new Vector();
		Vector lAvvSieModsSIUS = new Vector();

		// ==========================================================================
		// RICERCA AVVOCATO SU FASCICOLI SIES
		// ==========================================================================
		try {
			lAvvSieMod = lCtrl.ExRicercaAvvocatoFascicoloSiepByKeyAvvocato(lId);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" nessun elemento trovato");
		}

		if (lAvvSieMod.size() > 0) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione! E' impossibile cancellare questo difensore perchè ancora assegnato ad alcuni fascicoli");
		}

		// ==========================================================================
		// RICERCA AVVOCATO SU FASCICOLI SIUS
		// ==========================================================================
		siap.sius.avvocato.controller.IAvvocato lCtrlSIUS = SIUSLookupRemote.getAvvocatoRemote();
		// Vector lAvvSieMod = new Vector();
		try {
			lAvvSieModsSIUS = lCtrlSIUS.ExRicercaAvvocatiByFascicolo(lId);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" nessun elemento trovato");
		}

		if (lAvvSieModsSIUS.size() > 0) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione! E' impossibile cancellare questo difensore perchè ancora assegnato ad alcuni fascicoli");
		}

		// ==========================================================================
		//
		// ==========================================================================
		AvvocatoModel lAvvMod = new AvvocatoModel();
		lAvvMod.setIdAvvocato(lId);
		Vector avvocatoPrima = new Vector();

		AvvocatoModel lAvvModPrima = new AvvocatoModel();
		lAvvModPrima.setIdAvvocato(lId);
		avvocatoPrima = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvModPrima);
		lAvvModPrima = (AvvocatoModel) avvocatoPrima.get(0);

		StoricoAvvocatoModel lStoricoModel = new StoricoAvvocatoModel();

		lStoricoModel.setCognome(lAvvModPrima.getCognome());
		lStoricoModel.setNome(lAvvModPrima.getNome());

		lStoricoModel.setCodLuogoNascita(lAvvModPrima.getCodLuogoNascita());
		lStoricoModel.setDataNascita(lAvvModPrima.getDataNascita());
		lStoricoModel.setForo(lAvvModPrima.getForo());
		lStoricoModel.setIndirizzo(lAvvModPrima.getIndirizzo());
		lStoricoModel.setCodComuneResidenza(lAvvModPrima.getCodComuneResidenza());
		lStoricoModel.setTelefono(lAvvModPrima.getTelefono());
		lStoricoModel.setFax(lAvvModPrima.getFax());
		lStoricoModel.setEMail(lAvvModPrima.getEMail());
		lStoricoModel.setCodiceFiscale(lAvvModPrima.getCodiceFiscale());
		lStoricoModel.setProvincia(lAvvModPrima.getProvincia());
		lStoricoModel.setCap(lAvvModPrima.getCap());
		lStoricoModel.setFlagVisualizza(lAvvModPrima.getFlagVisualizza());
		lStoricoModel.setDataSospesoFinoAl(lAvvModPrima.getDataSospensione());
		lStoricoModel.setDataRadiatoDal(lAvvModPrima.getDataRadiazione());
		lStoricoModel.setCodNonAttivita(lAvvModPrima.getCodNonAttivita());
		lStoricoModel.setCodUfficioAppartenenza(lAvvModPrima.getCodUffAppartenenza());
		lStoricoModel.setCodUfficioInserimento(lAvvModPrima.getCodUfficioAggiornamento());
		lStoricoModel.setCodOperatoreInserimento(lAvvModPrima.getCodOperatoreAggiornamento());
		lStoricoModel.setDataInserimento(lAvvModPrima.getDataAggiornamento());
		lStoricoModel.setAvvIdAvvocato(lAvvModPrima.getIdAvvocato());
		lStoricoModel.setFlagCancellato("S");

		lAvvMod.setFlagCancellato("S");
		// chiama il controller
		// AvvocatoController lCtrl = new AvvocatoController();

		// ==========================================================================
		// Effettuo la cancellazione Logica (FLAG_CANCELLATO = S)e la storicizzazione
		// ==========================================================================
		lAvvMod = lCtrl.ExCancellaStoricizzaAvvocato(lAvvMod, lStoricoModel);

		setRequestAttribute("modalita", "M");
		setRequestAttribute("avvocato", lAvvMod);

		setRequestAttribute("flagModifica", "S");

		AvvocatoModel lAvvModRic = new AvvocatoModel();

		if (!this.isRequestParameterNullObj("campoNome")) {
			setRequestAttribute("ICostantiAvvocato.CAMPO_NOME", getRequestStringParameter("campoNome"));
			lAvvModRic.setNome(getRequestStringParameter("campoNome"));
		}

		if (!this.isRequestParameterNullObj("campoCognome")) {
			lAvvModRic.setCognome(getRequestStringParameter("campoCognome"));
			setRequestAttribute("ICostantiAvvocato.CAMPO_COGNOME", getRequestStringParameter("campoCognome"));
		}

		if (!this.isRequestParameterNullObj("campoForo")) {
			lAvvModRic.setForo(getRequestStringParameter("campoForo"));
			setRequestAttribute("ICostantiAvvocato.FORO", getRequestStringParameter("campoForo"));
		}
		lAvvModRic.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());

		Vector lVect = null;
		RedirectTo lRedirigi = new RedirectTo();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" cancella  = " + lAvvModRic.getCognome());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" cancella  = " + lAvvModRic.getNome());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" cancella  = " + lAvvModRic.getForo());

			if ("".equals(lAvvModRic.getCognome()) && "".equals(lAvvModRic.getNome())
					&& "".equals(lAvvModRic.getCognome())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("no ricerca");
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ExRicercaAvvocatoPaged");
				lVect = lCtrl.ExRicercaAvvocatoPaged(lAvvModRic, Integer.parseInt(lPagina));
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" nessun elemento trovato");
		}

		if (lVect == null || lVect.size() == 0) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" lRedirigi");

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Cancellazione Difensore Avvenuta Correttamente!");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadRicercaDifensore");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" concatena");

			String concatena = ICostantiAvvocato.CAMPO_NOME + "=" + getRequestStringParameter("campoNome")
					+ "&" + ICostantiAvvocato.CAMPO_COGNOME + "=" + getRequestStringParameter("campoCognome")
					+ "&" + ICostantiAvvocato.CAMPO_FORO + "=" + getRequestStringParameter("campoForo");
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Cancellazione Difensore Avvenuta Correttamente!");
			lRedirigi.setAction("siap.siep.avvocato.action.ActRicercaDifensore" + "&" + concatena);
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}

}