package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActLoadRichiestaIdoneitaProgrTerapeutico extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Decodifica DESTINATARIO 1
		Collection lCol = (DecodificheManager.getInstance()).getTipoAutorita();
		setRequestAttribute("codTipoUfficioS", "21");
		setRequestAttribute("descTipoUfficioS", DecodificheUtils.getDescbyCode(lCol, "21"));

		// Decodifica DESTINATARIO 2
		Collection lCol2 = (DecodificheManager.getInstance()).getTipoAutorita();
		setRequestAttribute("codTipoUfficioS2", "29");
		setRequestAttribute("descTipoUfficioS2", DecodificheUtils.getDescbyCode(lCol2, "29"));

		return PG_LOAD_RICHIESTAIDONEITAPROGRTERAPEUTICO; // restituisce la jsp di VIEW
	}

}