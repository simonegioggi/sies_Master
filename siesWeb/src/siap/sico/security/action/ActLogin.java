package siap.sico.security.action;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.security.model.FunctionModel;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.sico.security.ICostantiFunzioni;
//import siap.sico.security.controller.SecurityController;
import siap.sico.security.controller.ISecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.utenzaAdn.util.UtenzaAdnUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * MEV INTEGRAZIONE SIES ADN: nuova action di login
 *
 * @author sgioggi
 *
 */
public class ActLogin extends ActionSiap implements ICostantiSecurity {

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

		// chiama il controller
		ISecurity is = SICOLookupRemote.getSecurityRemote();
		String userId = StringUtils.convertSqlString(getRequestStringParameter(CAMPO_USER_ID));
		String password = getRequestStringParameter(CAMPO_PASSWORD);
		UtenteModel um = new UtenteModel(userId, password);
		um.setIP(getRequest().getRemoteAddr());
		um = UtenzaAdnUtils.preLogin(um, false);
		FunctionModel fm = new FunctionModel(ICostantiFunzioni.RADICE);
		FunctionModel fmMenu = is.ExLoadFunzioniMenu(um.getUserProfile(), fm);

		FunctionModel funzioneMenuSceltaRapida = null;
		if (um.getUserProfile() != null && um.getUserProfile().isSige())
			funzioneMenuSceltaRapida = new FunctionModel(ICostantiFunzioni.RADICE_SIGE);
		else
			funzioneMenuSceltaRapida = new FunctionModel(ICostantiFunzioni.RADICE);

		FunctionModel funzioniMenuSceltaRapida = is.ExLoadFunzioniMenuSceltaRapida(um.getUserProfile(),
				funzioneMenuSceltaRapida);

		// metto in sessione anche la userAdn
		String usernameADN = getRequestStringParameter("usernameDB");
		um.setUserAdn(usernameADN);

		// Disponibile per tutta la durata della sessione utente
		setSessionAttribute(SESSION_UTENTE_CONNESSO, um);
		setSessionAttribute(FUN_RADICE_MENU_VRT, fmMenu);
		setSessionAttribute(FUN_RADICE_MENU_ORZ, new FunctionModel());
		setSessionAttribute(FUN_RADICE_MENU_SR, funzioniMenuSceltaRapida);
		setSessionAttribute(FUN_ANTENATE, new LinkedList());

		siesLogger.info("ENTRO IN SIES!");
		// return IWebConstants.PG_FRAMESET;
		return IWebConstants.PAGE_OPEN_FRAMESET;
	}

}