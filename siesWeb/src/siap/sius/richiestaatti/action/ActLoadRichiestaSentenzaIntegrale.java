package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaSentenzaIntegrale extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// LISTA UFFICI
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoUfficioS();
		Option lOption = new Option(lTipoIstituto);
		String[] lStringFilter = { "CAP", "CAPSM", "CAS", "DIB", "DIBM", "GIPM", "GUP", "TRIBSD", "GUPM",
				"GP", "GIP", "CASAP", "CSS" };
		lOption.setFilter(lStringFilter);

		setRequestAttribute("TipiIstituti1", "" + lOption);

		return PG_LOAD_RICHIESTASENTENZAINTEGRALE; // restituisce la jsp di VIEW
	}

}