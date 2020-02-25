package siap.sius.misuresicurezzarichiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadInserisciCartaPrecettiva extends ActionSiap implements ICostantiMisureSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// LISTA UFFICI 1
		Collection lCol = DecodificheManager.getInstance().getTipoAutorita();
		String[] lStringFilter = { "-", "19", "20", "28", "58", "59", "60", "61" };
		Option lOption = new Option(lCol);
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);

		// ( DESTINATARIO 3 ) Elenco di tutti i destinatari
		Collection lElencoTipiAutorita = DecodificheManager.getInstance().getTipoAutorita();
		lOption = new Option(lElencoTipiAutorita);
		setRequestAttribute("ElencoTipiAutorita", "" + lOption); // Imposta il valore in request.

		return PG_LOAD_RICHIESTACARTAPRECETTIVA; // restituisce la jsp di VIEW
	}

}