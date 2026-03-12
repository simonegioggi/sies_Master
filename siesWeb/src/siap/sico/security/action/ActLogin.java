package siap.sico.security.action;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.security.model.FunctionModel;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.security.ICostantiFunzioni;
import siap.sico.security.controller.ISecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.utenzaAdn.util.UtenzaAdnUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPaBatch.controller.IBatchPagopa;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;
import siap.siep.util.SIEPLookupRemote;

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

		// 2024.01.17 La sessione va invalidata e ricreata altrimenti
		// se l'utente si limita a chiudere la pagina principale e a cambiare
		// utenza resta attiva la vecchia sessione che contiene i dati del precedente
		// utente se non rimossi
		if (getRequest().getSession(false) != null) {
			invalidateSession();
			// si crea e registra la nuova sessione
			super.setReqSes(getRequest(), getRequest().getSession());
		}

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
		String ip = getRequest().getRemoteAddr();
		um.setIP(ip);
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
		boolean isUtenteAssociato = is.getUtenteAssociato(um.getUserId(), um.getUserAdn());
		if (!isUtenteAssociato) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			String thisServer = getRequest().getServerName();
			int thisServerPort = getRequest().getServerPort();
			String thisServerProtocol = getRequest().getScheme();
			String s = thisServerProtocol + "://" + thisServer + ":" + thisServerPort + "/";
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Utenza SIES " + um.getUserId() + " non più associata all'utenza ADN " + um.getUserAdn());
			rt.setAction(s);
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			return IWebConstants.PG_MESSAGE;
		}

		// MEV_2023-33 - Se amministratore di sistema verifico l'ultima esecuzione del batch
		// Si estende a tutte le utenze SIEP
		if (um.getUserProfile().getProfileId().intValue() == 99
				|| um.getUserProfile().getProfileId().intValue() == 90
				|| um.getUserProfile().getProfileId().intValue() == 4
				|| um.getUserProfile().getProfileId().intValue() == 40
				|| um.getUserProfile().getProfileId().intValue() == 50) {
			try {
				IBatchPagopa lCtrlBatch = SIEPLookupRemote.getBatchPagopaPagopaRemote();
				BatchPagopaModel lancioBatch = lCtrlBatch.getLastEsecuzioneBatch();
				siesLogger.debug("Ultimo lancio " + lancioBatch);
				if ((lancioBatch.getErroreEsecuzione() != null
						&& lancioBatch.getErroreEsecuzione().length() > 0)
						|| (lancioBatch.getNumErroriInvocazione() != null
								&& lancioBatch.getNumErroriInvocazione().intValue() > 0)) {
					setSessionAttribute("ErroreBatchPagoPa", "S");
					setSessionAttribute("BatchPagoPa", lancioBatch);
				}
			} catch (Exception e) {
				siesLogger.error("Errore in fase di verifica dell'ultimo run del batch pagopas", e);
			}
		}
		// MEV_2023-33 - FINE

		// Disponibile per tutta la durata della sessione utente
		setSessionAttribute(SESSION_UTENTE_CONNESSO, um);
		setSessionAttribute(FUN_RADICE_MENU_VRT, fmMenu);
		setSessionAttribute(FUN_RADICE_MENU_ORZ, new FunctionModel());
		setSessionAttribute(FUN_RADICE_MENU_SR, funzioniMenuSceltaRapida);
		setSessionAttribute(FUN_ANTENATE, new LinkedList());

		// Update Utente :set Time di ultimo Login
		is.Utente_setOraLogin(um.getUserId(), ip);

		siesLogger.info("INDIRIZZO IP: " + um.getIP());
		siesLogger.info("ENTRO IN SIES!");

		// pagina di ritorno
		return IWebConstants.PAGE_OPEN_FRAMESET;
	}

}