package siap.sius.misuresicurezzarichiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;

public class ActLoadRichiestaRelazioneSocioFam extends ActionSiap
		implements ICostantiRichiestaAtti, ICostantiMisureSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// UEPE
		Collection lCol = DecodificheManager.getInstance().getTipoAutorita();

		Option lOptionU = new Option(lCol);
		String[] lStringFilterU = { "-", "40" };
		lOptionU.setFilter(lStringFilterU);

		setRequestAttribute("codTipoUfficioS", "40");
		// setRequestAttribute("descTipoUfficioS",DecodificheUtils.getDescbyCode(lCol,"40"));
		setRequestAttribute("descTipoUfficioS", "" + lOptionU);

		// Decodifica destinatari
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		Option lOption = new Option(lTipoIstituto, 35);
		String[] lStringFilter = { "-", "01", "02", "03", "04", "05", "06", "09", "14", "15", "16", "17",
				"41", "43", "44", "45", "46", "47", "48", "50", "51", "21", "40", "19", "20", "33", "28",
				"58", "59", "60", "61" };
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);

		return PG_LOAD_RICHIESTARELAZIONESOCIOFAM; // restituisce la jsp di VIEW
	}

}