package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaInfoRiabilitazione extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Decodifica TIPO_AUTORITA (DESTINATARIO)
		Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
		Option lOption = new Option(lTipoAutorita);
		setRequestAttribute("TipiAutorita", "" + lOption);
		String[] lStringFilter = { "-", "92", "59", "60", "61", "28", "58", "19" };
		lOption.setFilter(lStringFilter);
		setRequestAttribute("TipiAutoritaPolizia", "" + lOption);

		return PG_LOAD_RICHIESTAINFORIABILITAZIONE; // restituisce la jsp di VIEW
	}

}