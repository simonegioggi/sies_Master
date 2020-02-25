package siap.regesies.regecircostanza.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regecircostanza.controller.IRegeCircostanza;
import siap.regesies.regecircostanza.model.RegeCircostanzaModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaRegeCircostanza
 * </p>
 * <p>
 * Description: Classe Action per la modifica di RegeCircostanza
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
public class ActModificaRegeCircostanza extends ActionRegeSiap implements ICostantiRegeCircostanza {

	/**
	 * Azione di Modifica del RegeCircostanza
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// String lId = getRequestStringParameter(CAMPO_ID_FILE);

		RegeCircostanzaModel lRegMod = new RegeCircostanzaModel();

		lRegMod.setIdFile(getRequestStringParameter(CAMPO_ID_FILE));
		lRegMod.setProgrCircostanza(getRequestIntParameter(CAMPO_PROGR_CIRCOSTANZA));
		lRegMod.setCodFonte(getRequestStringParameter(CAMPO_COD_FONTE));

		if (isIntParameter(CAMPO_ANNO_FONTE))
			lRegMod.setAnnoFonte(getRequestIntParameter(CAMPO_ANNO_FONTE));

		lRegMod.setNumeroFonte(getRequestStringParameter(CAMPO_NUMERO_FONTE));
		lRegMod.setCodSottonumerazione(getRequestStringParameter(CAMPO_COD_SOTTONUMERAZIONE));
		lRegMod.setComma(getRequestStringParameter(CAMPO_COMMA));
		lRegMod.setLettera(getRequestStringParameter(CAMPO_LETTERA));
		lRegMod.setNumero(getRequestStringParameter(CAMPO_NUMERO));
		lRegMod.setArticolo(getRequestStringParameter(CAMPO_ARTICOLO));
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lRegMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		lRegMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
		lRegMod.setDataAggiornamento(DateUtils.getSysDate());

		// chiama il controller
		IRegeCircostanza lCtrl = RegeSiesLookupRemote.getRegeCircostanzaRemote();
		RegeCircostanzaModel lCircRet = lCtrl.ExModificaRegeCircostanza(lRegMod);

		setRequestAttribute("regecircostanza", lCircRet);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.regesies.regecircostanza.action.ActDettaglioRegeCircostanza&" + CAMPO_ID_FILE + "="
				+ lCircRet.getIdFile() + "&" + CAMPO_PROGR_CIRCOSTANZA + "=" + lCircRet.getProgrCircostanza();

		return lPage;
	}

}