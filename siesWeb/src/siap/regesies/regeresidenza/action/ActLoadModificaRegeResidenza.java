package siap.regesies.regeresidenza.action;

import siap.regesies.regeresidenza.controller.IRegeResidenza;
import siap.regesies.regeresidenza.model.RegeResidenzaModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaRegeResidenza
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di RegeResidenza
 * </p>
 */
public class ActLoadModificaRegeResidenza extends ActionSiap implements ICostantiRegeResidenza {

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_ID_FILE);
		String lProgr = getRequestStringParameter(CAMPO_COD_TIPO_RESIDENZA);

		IRegeResidenza lCtrl = RegeSiesLookupRemote.getRegeResidenzaRemote();
		RegeResidenzaModel llRegMod = lCtrl.ExRicercaRegeResidenzaByKey(lId, lProgr);
		setRequestAttribute("regeresidenza", llRegMod);

		Option lOption = new Option(DecodificheManager.getInstance().getNazioni(), llRegMod.getCodStato());
		setRequestAttribute("nazioni", "" + lOption);

		return PG_LOAD_MODIFICA_REGERESIDENZA; // restituisce la jsp di VIEW
	}

}