package siap.sius.misuresicurezzarichiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;

public class ActLoadInserisciInformazioniINPS extends ActionSiap
		implements ICostantiRichiestaAtti, ICostantiMisureSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// INPS
		Collection lColU = DecodificheManager.getInstance().getTipoAutorita();

		Option lOptionU = new Option(lColU);
		String[] lStringFilterU = { "B2" };
		lOptionU.setFilter(lStringFilterU);

		setRequestAttribute("codTipoUfficioS", "B2");
		setRequestAttribute("descTipoUfficioS", "" + lOptionU);

		return PG_LOAD_RICHIESTAINPS; // restituisce la jsp di VIEW
	}

}