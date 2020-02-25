package siap.sige.scadenzario.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sige.scadenzario.controller.IScadenzarioSige;
import siap.sige.scadenzario.model.ScadenzarioSigeModel;
import siap.sige.util.SIGELookupRemote;
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
public class ActVistoScadenzarioSige extends ActionSiap implements ICostantiScadenzarioSige {

	public String processRequest() throws F3BException {

		String tipo = getRequestStringParameter("T");
		String anni = getRequestStringParameter("A");
		String mesi = getRequestStringParameter("M");
		String giorni = getRequestStringParameter("G");
		String tipoScadenzario = getRequestStringParameter("TS");
		String descTipoScadenzario = getRequestStringParameter("DS");

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.scadenzario.action.ActRicercaScadenzarioSige";
		lPage += "&tipo=" + tipo + "&" + CAMPO_ANNI_SCADENZA + "=" + anni + "&" + CAMPO_MESI_SCADENZA + "="
				+ mesi + "&" + CAMPO_GIORNI_SCADENZA + "=" + giorni + "&" + CAMPO_COD_TIPO_SCADENZARIO + "="
				+ tipoScadenzario + "&" + CAMPO_DESC_TIPO_SCADENZARIO + "=" + descTipoScadenzario;
		ScadenzarioSigeModel lScaMod = new ScadenzarioSigeModel();

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SCADENZARIO_SIGE);

		// riempie il model

		lScaMod.setIdScadenzarioSige(lId);
		lScaMod.setFlagVisto("S");
		lScaMod.setDataVisto(DateUtils.getSysDate());
		lScaMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lScaMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lScaMod.setDataAggiornamento(lScaMod.getDataVisto());

		// chiama il controller
		IScadenzarioSige lCtrl = SIGELookupRemote.getScadenzarioSigeRemote();
		lCtrl.ExSetVistoScadenzario(lScaMod);
		return lPage;
	}

}