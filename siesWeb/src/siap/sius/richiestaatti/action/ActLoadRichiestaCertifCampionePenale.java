package siap.sius.richiestaatti.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.DateUtils;
import f3b.web.html.Option;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadRichiestaCertifCampionePenale extends ActionSiap implements ICostantiRichiestaAtti {

	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		Collection lCol1 = new ArrayList(DecodificheManager.getInstance().getTipoAutorita());
		// LISTA DESTINATARIO 1
		String[] lStringFilter1 = { "-", "36", "37", "98", "99" };
		Option lOption = new Option(lCol1);
		lOption.setFilter(lStringFilter1);
		setRequestAttribute("TipiIstituti1", "" + lOption);

		// LISTA DESTINATARIO 2
		Collection lCol2 = new ArrayList(DecodificheManager.getInstance().getTipoAutorita());
		String[] lStringFilter2 = { "-", "38", "39" };
		Option lOption2 = new Option(lCol2);
		lOption2.setFilter(lStringFilter2);
		setRequestAttribute("TipiIstituti2", "" + lOption2);

		return PG_LOAD_RICHIESTACERTIFCAMPIONEPENALE; // restituisce la jsp di VIEW
	}

}