package siap.sius.misuresicurezzarichiestaatti.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import f3b.util.DateUtils;
import f3b.web.html.Option;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciInfDSMUOSM extends ActionSiap
		implements ICostantiRichiestaAtti, ICostantiMisureSicurezza {

	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// LISTA UFFICI 1

		Collection lCol = DecodificheManager.getInstance().getTipoAutorita();

		List list = new ArrayList();
		list.addAll(lCol);
		// Collections.sort(list, Collections.reverseOrder());
		Collections.reverse(list);

		String[] lStringFilter = { "B0", "B1", "34" };
		// Option lOption = new Option( lCol );
		Option lOption = new Option(list);
		lOption.setFilter(lStringFilter);

		setRequestAttribute("TipiIstituti", "" + lOption);

		return PG_LOAD_RICHIESTAINFDSMUOSM; // restituisce la jsp di VIEW
	}

}