package siap.sius.scadenzario.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActVistoScadenzario
 * </p>
 * <p>
 * Description: Classe Action per la valorizzazione a Visto dello Scadenzario
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
public class ActVistoScadenzarioSius extends ActionSiap implements ICostantiScadenzarioSius {

	public String processRequest() throws F3BException {

		String tipo = getRequestStringParameter("T");
		String anni = getRequestStringParameter("A");
		String mesi = getRequestStringParameter("M");
		String giorni = getRequestStringParameter("G");
		String tipoScadenzario = getRequestStringParameter("TS");
		String descTipoScadenzario = getRequestStringParameter("DS");

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.scadenzario.action.ActRicercaScadenzarioSius";
		lPage += "&tipo=" + tipo + "&" + CAMPO_ANNI_SCADENZA + "=" + anni + "&" + CAMPO_MESI_SCADENZA + "="
				+ mesi + "&" + CAMPO_GIORNI_SCADENZA + "=" + giorni + "&" + CAMPO_COD_TIPO_SCADENZARIO + "="
				+ tipoScadenzario + "&" + CAMPO_DESC_TIPO_SCADENZARIO + "=" + descTipoScadenzario;
		ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel();

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SCADENZARIO);

		// riempie il model

		lScaMod.setIdScadenzarioSius(lId);
		lScaMod.setFlagVisto("S");
		lScaMod.setDataVisto(DateUtils.getSysDate());
		lScaMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lScaMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lScaMod.setDataAggiornamento(lScaMod.getDataVisto());

		// chiama il controller
		IScadenzarioSius lCtrl = SIUSLookupRemote.getScadenzarioRemote();
		lCtrl.ExSetVistoScadenzario(lScaMod);
		return lPage;
	}

}