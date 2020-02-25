package siap.sige.sentenza.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.web.html.Option;

public class ActLoadInserisciAltriTitoli extends ActionSige implements ICostantiFasSigeSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		// PARTE RELATIVA ALL'ISCRIZIONE SENTENZA
		// Imposta la decisione cassazione
		Option lOption = new Option(DecodificheManager.getInstance().getTipoDecisioneCassazione(), "-");
		setRequestAttribute("tipoDecisioneCassazione", "" + lOption);

		// UtenteModel lUteMod = new UtenteModel(
		// (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		// Imposta i provvedimenti Rif.
		lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimentiRif(), "-");
		setRequestAttribute("tipoProvvedimentiRif", "" + lOption);

		// Imposta le autorità.
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		String[] lFilter = { "-", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIPM", "GIP", "GIPP",
				"GIPPSD", "GIPMI", "GP", "GUPM", "GUPMI", "GUP", "PT", "PTC", "PTCSD", "TRIBSD", "CAPSM",
				"TMI", "DIB", "DIBM" };
		// String[] lFilter = {"-", "CAP","CAS","CAPMI","CAPMID","CSS","GIPM","GIP","GIPP"};
		lOption.setFilter(lFilter);
		setRequestAttribute("autoritaEmi", "" + lOption);
		setRequestAttribute("autoritaProvRif", "" + lOption);

		Option lOptionCumulo = new Option(DecodificheManager.getInstance().getListaTipoProvvCumulo(), "-");
		setRequestAttribute("listaTipoProvvCumulo", "" + lOptionCumulo);

		Option lOptionOrdinanza = new Option(DecodificheManager.getInstance().getListaTipoProvvOrdinanza(),
				"-");
		setRequestAttribute("listaTipoProvvOrdinanza", "" + lOptionOrdinanza);

		Option lOptionDecretoArchiviazione = new Option(DecodificheManager.getInstance()
				.getListaTipoProvvDecretoArchiviazione(), "-");
		setRequestAttribute("listaTipoProvvDecretoArchiviazione", "" + lOptionDecretoArchiviazione);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		String[] lFilter2 = { "-", "GIP", "GIPM", "GIPMI", "GIPP", "GIPPSD", "GUP", "GUPM", "GUPMI", "PT",
				"PTC" };
		lOption.setFilter(lFilter2);
		setRequestAttribute("autoritaEmiDecretoPenale", "" + lOption);

		// L'elenco Autorità Emittente (solo per l'ordinanza) deve contenere i seguenti valori
		// Si Imposta gli uffici G.E. (Giudice dell'Esecuzione).
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		String[] lFilter3 = { "-", "CAP", "CAS", "CASAP", "GIP", "GIPM", "GP", "GUP", "GUPM", "DIB", "DIBM",
				"TRIBSD" };
		lOption.setFilter(lFilter3);
		setRequestAttribute("autoritaEmiOrdinanza", "" + lOption);

		// Modifica del 29/11/2016 MEV_15_S4 (richiesta di Michele)
		// L'elenco Autorità Emittente (solo per il cumulo) deve contenere i seguenti valori:
		// PM - PMM - PGCAP
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		String[] lFilter4 = { "-", "PM", "PMM", "PGCAP" };
		lOption.setFilter(lFilter4);
		setRequestAttribute("autoritaEmiCumulo", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getFlagSN(), "N");
		setRequestAttribute("flagSN", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoRitoSentenza(), "-");
		setRequestAttribute("tipoRito1", "" + lOption);
		setRequestAttribute("tipoRito2", "" + lOption);

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		setRequestAttribute("autoritaEmi2", "" + lOption);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_LOAD_INSERISCIALTRITITOLI; // restituisce la jsp di VIEW
	}

}