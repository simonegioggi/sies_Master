package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaRogatoria extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// LISTA DESTINATARIO 1
		Collection lCol = DecodificheManager.getInstance().getTipoUfficio();
		String[] lStringFilter = { "-", "UDS" };
		Option lOption = new Option(lCol);
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);

		// LISTA DESTINATARIO 2
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		Option lOption2 = new Option(lTipoIstituto, 30);
		setRequestAttribute("TipiIstituti2", "" + lOption2);

		return PG_LOAD_RICHIESTAROGATORIA; // restituisce la jsp di VIEW
	}

}