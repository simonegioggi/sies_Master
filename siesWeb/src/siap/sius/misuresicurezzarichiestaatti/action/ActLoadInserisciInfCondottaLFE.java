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
public class ActLoadInserisciInfCondottaLFE extends ActionSiap
		implements ICostantiRichiestaAtti, ICostantiMisureSicurezza {

	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		String[] lFiltroT = { "-", "92", "59", "60", "61", "28", "58", "19", "72", "73", "74", "70", "71",
				"64", "66", "67", "93", "69", "68", "20", "26" };
		Collection lElencoTipiAutorita = DecodificheManager.getInstance().getTipoAutorita();

		// ( DESTINATARIO 2 - 3 ) Tipi Autorità Filtrati
		Option lOptionT = new Option(lElencoTipiAutorita);
		lOptionT.setFilter(lFiltroT);
		setRequestAttribute("ElencoFiltratoTipiAutorita", "" + lOptionT); // Imposta il valore in request.

		// LISTA UFFICI 1

		Collection lCol = DecodificheManager.getInstance().getTipoAutorita();

		List list = new ArrayList();
		list.addAll(lCol);
		// Collections.sort(list, Collections.reverseOrder());
		Collections.reverse(list);

		String[] lStringFilter = { "B0", "B1", "34", "21" };
		// Option lOption = new Option( lCol );
		Option lOption = new Option(list);
		lOption.setFilter(lStringFilter);

		setRequestAttribute("TipiIstituti", "" + lOption);

		// UEPE
		Collection lColU = DecodificheManager.getInstance().getTipoAutorita();

		Option lOptionU = new Option(lColU);
		String[] lStringFilterU = { "-", "40" };
		lOptionU.setFilter(lStringFilterU);

		setRequestAttribute("codTipoUfficioS", "40");
		setRequestAttribute("descTipoUfficioS", "" + lOptionU);

		return PG_LOAD_RICHIESTALFE; // restituisce la jsp di VIEW
	}

}