package siap.regesies.regeresidenza.action;

import siap.regesies.regeresidenza.controller.IRegeResidenza;
import siap.regesies.regeresidenza.model.RegeResidenzaModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaRegeResidenza
 * </p>
 * <p>
 * Description: Classe Action per la modifica di RegeResidenza
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
public class ActModificaRegeResidenza extends ActionSiap implements ICostantiRegeResidenza {

	/**
	 * Azione di Modifica del RegeResidenza
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_ID_FILE);
		// riempie il model
		RegeResidenzaModel lRegMod = new RegeResidenzaModel();

		lRegMod.setIdFile(lId);
		lRegMod.setIdFile(getRequestStringParameter(CAMPO_ID_FILE));
		lRegMod.setCodStato(getRequestStringParameter(CAMPO_COD_STATO));
		String lComune = getRequestStringParameter(CAMPO_DESCR_COMUNE);

		ComuneModel lComuneMod = getCodComuneByDescr(lComune);

		lRegMod.setCodComune(lComuneMod.getCodComune());
		lRegMod.setCodProvincia(lComuneMod.getCodProvincia());

		String lCap = getRequestStringParameter(CAMPO_CAP);
		// Se il Cap è valorizzato inserisco quello della maschera altrimenti lo setto dalla tabella Comune
		if (lCap != null && lCap.length() > 0)
			lRegMod.setCap(lCap);
		else
			lRegMod.setCap(lComuneMod.getCap());

		lRegMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO));
		lRegMod.setCodTipoResidenza(getRequestStringParameter(CAMPO_COD_TIPO_RESIDENZA));
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lRegMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		lRegMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
		lRegMod.setDataAggiornamento(DateUtils.getSysDate());
		lRegMod.setDataAggiornamento(DateUtils.getSysDate());
		lRegMod.setDescComuneEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_ESTERO));

		// chiama il controller
		IRegeResidenza lCtrl = RegeSiesLookupRemote.getRegeResidenzaRemote();
		RegeResidenzaModel llRegModRet = lCtrl.ExModificaRegeResidenza(lRegMod);

		setRequestAttribute("regeresidenza", llRegModRet);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.regesies.regeresidenza.action.ActDettaglioRegeResidenza&" + CAMPO_ID_FILE + "="
				+ llRegModRet.getIdFile().toString() + "&" + CAMPO_COD_TIPO_RESIDENZA + "="
				+ llRegModRet.getCodTipoResidenza();
		return lPage;
	}

}