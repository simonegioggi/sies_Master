package siap.sige.sentenza.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.web.ActionSige;

public class ActLoadInserisciSentenzaDecreto extends ActionSige implements ICostantiFasSigeSentenza {

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

		// UtenteModel lUteMod = new
		// UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

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

		// MEV 15
		// In fase di iscrizione del titolo esecutivo, selezionando il tipo
		// "Decreto Penale", tra le Autorità Emittenti devono essere presenti
		// solo i valori contenenti:GIP,GUP e Pretura.
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		String[] lFilter2 = { "-", "GIP", "GIPM", "GIPMI", "GIPP", "GIPPSD", "GUP", "GUPM", "GUPMI", "PT",
				"PTC" };
		lOption.setFilter(lFilter2);
		setRequestAttribute("autoritaEmiDecretoPenale", "" + lOption);

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

		// PARTE RELATIVA ALL'ISCRIZIONE SENTENZA STRANIERA
		// Imposta l'autorita Rif.
		// 28/06/2010 Sostituzione Elenco Autorità Emittenti
		// lOption = new Option( DecodificheManager.getInstance().getTipoUfficioS(), "-");
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		setRequestAttribute("autoritaEmi2", "" + lOption);

		// PARTE RELATIVA ALL'ISCRIZIONE DECRETO
		// Imposta la decisione cassazione (già impostata)
		// lOption = new Option( DecodificheManager.getInstance().getTipoDecisioneCassazione(), "-");
		// setRequestAttribute("tipoDecisioneCassazione", "" + lOption );

		// Imposta l' autorita Rif. (già impostata)
		// lOption = new Option( DecodificheManager.getInstance().getTipoUfficioS(), "-");
		// setRequestAttribute("autoritaEmi2", "" + lOption );

		// PARTI COMUNI
		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_LOAD_INSERISCISENTENZADECRETO; // restituisce la jsp di VIEW
	}

}