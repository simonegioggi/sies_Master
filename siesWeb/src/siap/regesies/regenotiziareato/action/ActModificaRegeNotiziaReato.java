package siap.regesies.regenotiziareato.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regenotiziareato.controller.IRegeNotiziaReato;
import siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaRegeNotiziaReato
 * </p>
 * <p>
 * Description: Classe Action per la modifica di RegeNotiziaReato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
public class ActModificaRegeNotiziaReato extends ActionRegeSiap implements ICostantiRegeNotiziaReato {

	/**
	 * Azione di Modifica del RegeNotiziaReato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// String lId = getRequestStringParameter(CAMPO_ID_FILE);

		RegeNotiziaReatoModel lNotMod = new RegeNotiziaReatoModel();

		lNotMod.setIdFile(getRequestStringParameter(CAMPO_ID_FILE));
		lNotMod.setProgrNotizia(getRequestStringParameter(CAMPO_PROGR_NOTIZIA));
		lNotMod.setDataPervenimento(getRequestDateParameter(CAMPO_ANNO_DATA_PERVENIMENTO,
				CAMPO_MESE_DATA_PERVENIMENTO, CAMPO_GIORNO_DATA_PERVENIMENTO));
		lNotMod.setAcquisizioneDiretta(getRequestStringParameter(CAMPO_ACQUISIZIONE_DIRETTA));
		lNotMod.setDataFatto(getRequestDateParameter(CAMPO_ANNO_DATA_FATTO, CAMPO_MESE_DATA_FATTO,
				CAMPO_GIORNO_DATA_FATTO));
		// lNotMod.setCodFonte( getRequestStringParameter( CAMPO_COD_FONTE) );
		// lNotMod.setTipoFonte( getRequestStringParameter( CAMPO_TIPO_FONTE) );

		String lComune = getRequestStringParameter(CAMPO_COD_COMUNE_FONTE);

		ComuneModel lComuneMod = this.getCodComuneByDescr(lComune);
		lNotMod.setCodComuneFonte(lComuneMod.getCodComune());

		if (this.isIntParameter(CAMPO_NUM_REG_AUTORITA))
			lNotMod.setNumRegAutorita(getRequestStringParameter(CAMPO_NUM_REG_AUTORITA));

		lNotMod.setLuogoProvenienza(getRequestStringParameter(CAMPO_LUOGO_PROVENIENZA));
		lNotMod.setDataAcquisizione(getRequestDateParameter(CAMPO_ANNO_DATA_ACQUISIZIONE,
				CAMPO_MESE_DATA_ACQUISIZIONE, CAMPO_GIORNO_DATA_ACQUISIZIONE));
		lNotMod.setNumeroRicevuta(getRequestStringParameter(CAMPO_NUMERO_RICEVUTA));
		lNotMod.setDescrizioneFonte(getRequestStringParameter(CAMPO_DESCRIZIONE_FONTE));

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lNotMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		lNotMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
		lNotMod.setDataAggiornamento(DateUtils.getSysDate());

		// chiama il controller
		IRegeNotiziaReato lCtrl = RegeSiesLookupRemote.getRegeNotiziaReatoRemote();
		RegeNotiziaReatoModel lNotRet = lCtrl.ExModificaRegeNotiziaReato(lNotMod);

		// setRequestAttribute("regenotiziareato", lNotRet);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.regesies.regenotiziareato.action.ActDettaglioRegeNotiziaReato&" + CAMPO_ID_FILE + "="
				+ lNotRet.getIdFile() + "&" + CAMPO_PROGR_NOTIZIA + "=" + lNotRet.getProgrNotizia();

		return lPage;
	}

}