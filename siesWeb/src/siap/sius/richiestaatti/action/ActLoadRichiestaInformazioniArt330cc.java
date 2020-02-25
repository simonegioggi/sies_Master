package siap.sius.richiestaatti.action;

/**
* <p>Title: ActLoadRichiestaInformazioniArt330cc</p>
* <p>Description: Classe Action per la load di RichiestaInformazioniArt330cc</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaInformazioniArt330cc extends ActionSiap implements ICostantiRichiestaAtti {

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

		setRequestAttribute("codTipoUfficioS", "PMM");
		setRequestAttribute("descTipoUfficioS", DecodificheUtils.getDescbyCode(lCol, "PMM"));

		return PG_LOAD_RICHIESTAINFORMAZIONIART330CC; // restituisce la jsp di VIEW
	}

}