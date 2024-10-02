package siap.siep.sospensione.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;

/**
 * 
 * 
 * 
 * @since MEV_2019-09-SIEP
 */
public class ActLoadModificaSospensioneDecisioniSorv678 extends ActionSiap {
	public String processRequest() throws F3BException {

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sospensione.action.ActLoadInserisciSospensioneDecisioniSorv678&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + idEvento + "&tipoOperazione=MODIFICA";
		return lPage;
	}
}
