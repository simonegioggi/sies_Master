package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaParereDDADNA extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Decodifica TIPO_UFFICIO (DESTINATARIO)
		Collection lTipoIstituto = DecodificheManager.getInstance().getTipoUfficio();
		Option lOption = new Option(lTipoIstituto);
		String[] lStringFilter = { "-", "DDA", "DNA" };
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiIstituti", "" + lOption);

		return PG_LOAD_RICHIESTAPAREREDDADNA; // restituisce la jsp di VIEW
	}

}
