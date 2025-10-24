package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaConfermaDisponibilitaSERT extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		Collection lCol = (DecodificheManager.getInstance()).getTipoAutorita();
		Option lOption = new Option(lCol);

		// DESTINATARIO 1
		// Imposta array per le condizioni di filtro del destinatario 1
		String[] lFiltro1 = { "-", "21" };
		lOption.setFilter(lFiltro1);
		setRequestAttribute("TipoUfficioS1", lOption.toString());

		// DESTINATARIO 2
		// Imposta array per le condizioni di filtro del destinatario 2
		String[] lFiltro2 = { "-", "29" };
		lOption.setFilter(lFiltro2);
		setRequestAttribute("TipoUfficioS2", lOption.toString());

		return PG_LOAD_RICHIESTACONFERMADISPONIBILITASERT; // restituisce la jsp di VIEW
	}

}