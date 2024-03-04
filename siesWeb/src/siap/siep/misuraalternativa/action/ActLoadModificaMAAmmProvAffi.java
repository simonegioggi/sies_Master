package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;

public class ActLoadModificaMAAmmProvAffi extends ActAmmissioneProvvisoria {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
	
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		
		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misuraalternativa.action.ActLoadInserisciMAAmmProvAffi&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + idEvento + "&tipoOperazione=MODIFICA";
		return lPage;
	}
}
