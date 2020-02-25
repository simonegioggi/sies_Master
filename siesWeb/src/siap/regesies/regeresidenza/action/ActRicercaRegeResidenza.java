package siap.regesies.regeresidenza.action;

import java.util.Vector;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regeresidenza.controller.IRegeResidenza;
import siap.regesies.regeresidenza.model.RegeResidenzaModel;
import siap.regesies.util.RegeSiesLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaRegeResidenza
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di RegeResidenza
 * </p>
 */
@SuppressWarnings("rawtypes")
public class ActRicercaRegeResidenza extends ActionRegeSiap implements ICostantiRegeResidenza {

	public String processRequest() throws F3BException {

		String lPage = "";

		isProvvedimentoRegeInSession();

		String lIdFile = getRequestStringParameter(CAMPO_ID_FILE);

		IRegeResidenza lCtrl = RegeSiesLookupRemote.getRegeResidenzaRemote();
		Vector lVect = lCtrl.ExRicercaRegeResidenza(lIdFile);

		if (lVect != null && lVect.size() == 1) {
			RegeResidenzaModel lRes = (RegeResidenzaModel) lVect.firstElement();
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.regesies.regeresidenza.action.ActDettaglioRegeResidenza&"
					+ ICostantiRegeResidenza.ID_FILE + "=" + lRes.getIdFile() + "&" + CAMPO_COD_TIPO_RESIDENZA
					+ "=" + lRes.getCodTipoResidenza();
		} else { // HO trovato due occorrenze
			setRequestAttribute("residenze", lVect);
			lPage = ICostantiRegeResidenza.PG_RICERCAREGERESIDENZA;
		}

		return lPage;
	}

}