package siap.sius.cancelleriaassegnataria.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioCancelleriaAssegnataria
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di CancelleriaAssegnataria
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
public class ActLoadDettaglioCancelleriaAssegnataria extends ActionSiap
		implements ICostantiCancelleriaAssegnataria {

	public String processRequest() throws Exception {

		gestioneRitorno();
		ricercaCancelleriaAssegnataria();

		return PG_LOAD_DETTAGLIOCANCELLERIAASSEGNATARIA;
	}

	@SuppressWarnings("rawtypes")
	public void ricercaCancelleriaAssegnataria() throws F3BException {

		// Riempie il model di ricerca
		CancelleriaAssegnatariaModel lCanMod = new CancelleriaAssegnatariaModel();
		lCanMod.setCodCancelleriaAssegnataria(getRequestStringParameter(CAMPO_COD_CANCELLERIA_ASSEGNATARIA));
		lCanMod.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));

		// Ricerca
		ICancelleriaAssegnataria lCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
		Vector lLista = lCtrl.ExRicercaCancelleriaAssegnataria(lCanMod);
		if (lLista == null || lLista.size() == 0) {
			throw new F3BException(F3BException.EX_NOT_FOUND, "Nessun Elemento trovato");
		}

		lCanMod = (CancelleriaAssegnatariaModel) lLista.get(0);
		setRequestAttribute("cancelleriaassegnataria", lCanMod);
	}

}