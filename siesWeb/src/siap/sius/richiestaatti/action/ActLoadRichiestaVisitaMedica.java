package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaVisitaMedica extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// LISTA DESTINATARIO 1
		Collection lCol = DecodificheManager.getInstance().getTipoAutorita();
		String[] lStringFilter = { "-", "34" };
		Option lOption = new Option(lCol);
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);

		// LISTA DESTINATARIO 2
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		Option lOption2 = new Option(lTipoIstituto, 30);
		setRequestAttribute("TipiIstituti2", "" + lOption2);

		return PG_LOAD_RICHIESTAVISITAMEDICA; // restituisce la jsp di VIEW
	}

}