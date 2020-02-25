package siap.sico.assistentegiudiziario.action;

import java.util.Collection;
import java.util.Vector;

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
// per la decodifica
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: ActRicercaAssistenteGiudiziario
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di AssistenteGiudiziario
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaAssistenteGiudiziario extends ActionSiap implements ICostantiAssistenteGiudiziario {

	public String processRequest() throws F3BException {

		String lReturnPage = ""; // pagina jsp di ritorno

		// Collection di decodifica di Flag_stato
		Collection lCol = (DecodificheManager.getInstance()).getFlagStato();

		// Viene istanziato i l model e valorizzati i criteri di ricerca
		AssistenteGiudiziarioModel lAssMod = new AssistenteGiudiziarioModel();

		lAssMod.setCognome(
				StringUtils.convertSqlString(getRequestStringParameter(CAMPO_COGNOME).toUpperCase()));
		lAssMod.setNome(StringUtils.convertSqlString(getRequestStringParameter(CAMPO_NOME).toUpperCase()));
		// La ricerca è sempre filtrata per ufficio
		lAssMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		IAssistenteGiudiziario lCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
		Vector lVect = lCtrl.ExRicercaAssistenteGiudiziario(lAssMod);
		setRequestAttribute("assistentegiudiziario", lVect);

		if (lVect.size() == 1) { // unico AssistenteGiudiziario
			AssistenteGiudiziarioModel lAssistente = new AssistenteGiudiziarioModel(
					(AssistenteGiudiziarioModel) lVect.firstElement());

			// Decodifica di Flag_stato
			lAssistente.setFlagStato(DecodificheUtils.getDescbyCode(lCol, lAssistente.getFlagStato()));

			// Inserimento del model AssistenteGiudiziario nella request
			setRequestAttribute("assistentegiudiziario", lAssistente);
			setFunctionsAvailableToRequest(
					"siap.sico.assistentegiudiziario.action.ActLoadDettaglioAssistenteGiudiziario");
			lReturnPage = PG_LOAD_DETTAGLIOASSISTENTEGIUDIZIARIO;
		} else { // lista di Assistenti
			setRequestAttribute("assistenti", lVect);
			setRequestAttribute("flags", lCol);
			lReturnPage = PG_RICERCAASSISTENTEGIUDIZIARIO;
		}
		return lReturnPage;
	}

}