package siap.sico.security.action;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import siap.sico.security.ICostantiFunzioni;
//import siap.sico.security.controller.SecurityController;
import siap.sico.security.controller.ISecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.security.model.FunctionModel;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;

public class ActLogin_Old extends ActionSiap implements ICostantiSecurity {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// Rimuove dalla sessione gli oggetti indicati eventualmente presenti
		removeSessionAttribute("soggetto");
		removeSessionAttribute("sentenza");
		removeSessionAttribute("fascicolo");
		removeSessionAttribute("reato");
		removeSessionAttribute("HelpPage");

		removeSessionAttribute(SESSION_UTENTE_CONNESSO);
		removeSessionAttribute(FUN_RADICE_MENU_VRT);
		removeSessionAttribute(FUN_RADICE_MENU_ORZ);
		removeSessionAttribute(FUN_RADICE_MENU_SR);
		removeSessionAttribute(FUN_ANTENATE);

		// prepara il model dell'utente per la login
		String lUserId = StringUtils.convertSqlString(getRequestStringParameter(CAMPO_USER_ID));
		String lPassword = getRequestStringParameter(CAMPO_PASSWORD);

		UtenteModel lModUte = new UtenteModel(lUserId, lPassword);
		lModUte.setIP(getRequest().getRemoteAddr());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("____________________________");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(lModUte.getIP());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("____________________________");
		// prepara il model dell'ufficio per la login
		/*
		 * String lCodTipoUfficio = getRequestStringParameter(CAMPO_TIPO_UFFICIO); String lDescrComune =
		 * getRequestStringParameter(CAMPO_COMUNE);
		 * 
		 * ComuneModel lComMod = new ComuneModel(); lComMod.setDescrizione( lDescrComune.toUpperCase());
		 * IComune lComCtrl = SICOLookupRemote.getComuneRemote(); ComuneModel lComModRitorno = new
		 * ComuneModel(lComCtrl.ExGetCodiceComune(lComMod));
		 * 
		 * UfficioModel lModUff = new UfficioModel(); lModUff.setCodTipoUfficio(lCodTipoUfficio);
		 * lModUff.setCodComune(lComModRitorno.getCodComune());
		 */

		// chiama il controller
		// SecurityController lSctrl = new SecurityController();
		ISecurity lSctrl = SICOLookupRemote.getSecurityRemote();

		// UtenteModel lUtente = lSctrl.ExLogin(lModUte, lModUff);
		UtenteModel lUtente = lSctrl.ExLogin(lModUte);
		FunctionModel lFunRad = new FunctionModel(ICostantiFunzioni.RADICE);
		FunctionModel lFunRadiceMenu = lSctrl.ExLoadFunzioniMenu(lUtente.getUserProfile(), lFunRad);

		FunctionModel lFunRadMenuSceltaRapida = null;
		if (lUtente.getUserProfile() != null && lUtente.getUserProfile().isSige()) {
			lFunRadMenuSceltaRapida = new FunctionModel(ICostantiFunzioni.RADICE_SIGE);
		} else {
			lFunRadMenuSceltaRapida = new FunctionModel(ICostantiFunzioni.RADICE);
		}

		FunctionModel lFunRadiceMenuSceltaRapida = lSctrl.ExLoadFunzioniMenuSceltaRapida(
				lUtente.getUserProfile(), lFunRadMenuSceltaRapida);

		// Disponibile per tutta la durata della sessione utente
		setSessionAttribute(SESSION_UTENTE_CONNESSO, lUtente);
		setSessionAttribute(FUN_RADICE_MENU_VRT, lFunRadiceMenu);
		setSessionAttribute(FUN_RADICE_MENU_ORZ, new FunctionModel());
		setSessionAttribute(FUN_RADICE_MENU_SR, lFunRadiceMenuSceltaRapida);
		setSessionAttribute(FUN_ANTENATE, new LinkedList());

		// Verifico che la password sia diversa dalla username ...
		if (Utils.cryptPassword(lUtente.getUserId()).equals(lUtente.getPwd()) || lUtente.getPwd() == null) {
			// Se si...chiamo la maschera di cambio password obbligatoria
			setRequestAttribute("msg", "Devi obbligatoriamente cambiare la password");

			return PG_CHANGE_PASSWORD;
		} else {
			// return IWebConstants.PG_FRAMESET;
			return IWebConstants.PAGE_OPEN_FRAMESET;
		}
	}

}