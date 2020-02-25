package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaCumulo extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Decodifica DESTINATARIO
		Collection lCol = (DecodificheManager.getInstance()).getTipoUfficio();
		String[] lStringFilter = { "-", "PM", "PGCAP", "PMM" };
		Option lOption = new Option(lCol);
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);

		// setRequestAttribute("codTipoUfficioS","PM");
		// setRequestAttribute("descTipoUfficioS",DecodificheUtils.getDescbyCode(lCol,"PM"));

		return PG_LOAD_RICHIESTACUMULO; // restituisce la jsp di VIEW
	}

}