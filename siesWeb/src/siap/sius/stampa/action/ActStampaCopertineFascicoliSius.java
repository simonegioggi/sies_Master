package siap.sius.stampa.action;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_65: aggiunta action per gestire nuova funzionalita'
 * 
 * @author Gioggi
 * @version 1.0
 */
public class ActStampaCopertineFascicoliSius extends ActionSiap implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio.");

		FascicoloSiusModel fsm = new FascicoloSiusModel();

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
			fsm.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
			fsm.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
			fsm.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
			fsm.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));

		if (!isRequestParameterNullObj("dataIniziale")
				&& Utils.isPresent(getRequestStringParameter("dataIniziale")))
			fsm.setDataIscrizioneIniziale(getRequestDateParameter("dataIniziale", "dd/MM/yyyy"));

		if (!isRequestParameterNullObj("dataFinale")
				&& Utils.isPresent(getRequestStringParameter("dataFinale")))
			fsm.setDataIscrizioneFinale(getRequestDateParameter("dataFinale", "dd/MM/yyyy"));

		if (!isRequestParameterNullObj("competenza")) {
			if ("0".equals(getRequestStringParameter("competenza"))) {
				// Visualizza solo i procedimenti dall'Ufficio
				String codUfficio = getCodUfficioUtenteConnesso();
				fsm.setChiaveUfficio(codUfficio);
				fsm.setCodUfficioInserimento(codUfficio);
				fsm.setDescrTipoUfficio(
						getUfficioByCodUfficio(getCodUfficioUtenteConnesso()).getDescrTipoUfficio());
				fsm.setDescrComuneUfficio(
						getUfficioByCodUfficio(getCodUfficioUtenteConnesso()).getDescrComune());
			} else
				// Visualizza solo i procedimenti dall'Utente
				fsm.setCodOperatoreInserimento(getUtenteConnesso().getUserId());
		}

		if (!isRequestParameterNullObj("codCancelleria"))
			fsm.setCodCancelleria(getRequestStringParameter("codCancelleria"));

		UtenteModel um = getUtenteConnesso();

		String lPageReturn = IWebConstants.PG_DOWNLOAD;

		// Inizializzazione numero di pagina
		String pagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			pagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		try {
			IStampaSius iss = SIUSLookupRemote.getStampaRemote();
			ByteArrayOutputStream report = iss.ExStampaCopertineFascicoliSius(fsm, um,
					Integer.parseInt(pagina));
			setRequestAttribute("report", report);
		} catch (F3BException ex) {
			siesLogger.debug(" * * * F3BException " + ex);
			lPageReturn = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Nessun procedimento nell'intervallo selezionato!");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: fine.");

		// stampa copertine
		return lPageReturn;
	}

}