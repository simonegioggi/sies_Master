package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaPSconCedMinisteroInterni extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Decodifica DESTINATARIO
		Collection lCol = (DecodificheManager.getInstance()).getTipoAutorita();
		String[] lStringFilter = { "-", "19", "20" };
		Option lOption = new Option(lCol);
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti1", "" + lOption);
		setRequestAttribute("codTipoUfficioS", "19");
		setRequestAttribute("descTipoUfficioS", DecodificheUtils.getDescbyCode(lCol, "19"));

		return PG_LOAD_RICHIESTAPSCONCEDMINISTEROINTERNI; // restituisce la jsp di VIEW
	}

}