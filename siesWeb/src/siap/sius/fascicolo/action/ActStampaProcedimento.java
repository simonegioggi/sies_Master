package siap.sius.fascicolo.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActStampaProcedimento extends ActionSiap implements ICostantiFascicoloSius {

	public String processRequest() throws Exception {

		BigDecimal lFasKey = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIUS);
		String lTipoUfficio = getUfficioUtenteConnesso().getCodUfficio();

		// Lookup.
		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		ByteArrayOutputStream lReport = lCtrl.ExStampaProcedimento(lFasKey, lTipoUfficio,
				super.getUtenteConnesso());

		// Prepara la pagina di destinazione.
		if (lReport != null)
			setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}