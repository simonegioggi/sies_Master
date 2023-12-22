package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaRelazioneComportamentale extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Decodifica destinatari
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		Option lOption = new Option(lTipoIstituto, 35);
		// Ticket#20231213016 - SIUS - Residenze per l'esecuzione delle misure di sicurezza (REMS)
		// aggiunta voce "I0" REMS
		String[] lStringFilter = { "-", "01", "02", "03", "04", "05", "06", "09", "14", "15", "16", "17",
				"41", "43", "44", "45", "46", "47", "48", "50", "51", "21", "40", "19", "20", "33", "28",
				"58", "59", "60", "61", "I0" };
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);
		setRequestAttribute("TipiIstituti2", "" + lOption);

		// restituisce la jsp di VIEW
		return PG_LOAD_RICHIESTARELAZIONECOMPORTAMENTALE;
	}

}