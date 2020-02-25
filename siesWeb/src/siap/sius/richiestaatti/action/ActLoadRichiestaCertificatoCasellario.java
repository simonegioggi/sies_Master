package siap.sius.richiestaatti.action;

/**
* <p>Title: ActLoadRichiestaCertificatoCasellario</p>
* <p>Description: Classe Action per la load di RichiestaCarichiPendenti</p>
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

public class ActLoadRichiestaCertificatoCasellario extends ActionSiap implements ICostantiRichiestaAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Decodifica DESTINATARIO
		Collection lCol = (DecodificheManager.getInstance()).getTipoUfficioS();
		// Imposta la sede giudiziaria prelevata dal soggetto in sessione
		String lSede = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel()
				.getSoggetto().getDescrComuneCasellario();

		setRequestAttribute("codTipoUfficioS", "PM");
		setRequestAttribute("descTipoUfficioS", DecodificheUtils.getDescbyCode(lCol, "PM"));
		setRequestAttribute("sedeProcura", lSede);

		return PG_LOAD_RICHIESTACERTIFICATOCASELLARIO; // restituisce la jsp di VIEW
	}

}