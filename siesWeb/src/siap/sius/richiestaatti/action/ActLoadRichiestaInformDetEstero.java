package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaInformDetEstero extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Decodifica TIPO ISTITUTO (DESTINATARIO 1)
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		Option lOption = new Option(lTipoIstituto, 70);
		String[] lStringFilter = { "-", "81" };
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);

		// Decodifica TIPO ISTITUTO (Destinatario 2)
		Collection lCol = DecodificheManager.getInstance().getTipoAutorita();
		lOption = new Option(lCol, "-");
		// lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti2", "" + lOption);

		// Decodifica SEDE ISTITUTO (DESTINATARIO 1)
		Collection lSedeIstituto = DecodificheManager.getInstance().getNazioni();
		lOption = new Option(lSedeIstituto, 50);
		setRequestAttribute("lSedeIstituto1", "" + lOption);

		return PG_LOAD_RICHIESTAINFORMDETESTERO; // restituisce la jsp di VIEW
	}

}