package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaRicerchePoliziaGiudiziaria extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Lista DESTINATARIO
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		Option lOption = new Option(lTipoIstituto, 35);
		String[] lStringFilter = { "28", "58", "59", "60", "61", "19", "32", "35", "20" };
		lOption.setFilter(lStringFilter);

		setRequestAttribute("TipiIstituti1", "" + lOption);

		return PG_LOAD_RICHIESTARICERCHEPOLIZIAGIUDIZIARIA; // restituisce la jsp di VIEW
	}

}