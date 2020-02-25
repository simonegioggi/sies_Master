package siap.sius.richiestaatti.action;

/**
* <p>Title: ActLoadRichiestaCarichiPendenti</p>
* <p>Description: Classe Action per la load di RichiestaCarichiPendenti</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaCarichiPendenti extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Decodifica PM
		Collection lCol = (DecodificheManager.getInstance()).getTipoUfficioS();

		setRequestAttribute("codTipoUfficioS", "PM");
		setRequestAttribute("descTipoUfficioS", DecodificheUtils.getDescbyCode(lCol, "PM"));

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioMinor());
		lOption.setFilter(new String[] { "PM", "PMM" });
		setRequestAttribute("uffici", "" + lOption);

		return PG_LOAD_RICHIESTACARICHIPENDENTI; // restituisce la jsp di VIEW
	}

}