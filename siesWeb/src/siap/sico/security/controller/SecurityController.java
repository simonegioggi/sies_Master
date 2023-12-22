package siap.sico.security.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.security.SecurityException;
import f3b.security.model.FunctionModel;
import f3b.security.model.ProfileModel;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.controller.SiapController;
import siap.sico.security.ICostantiFunzioni;
import siap.sico.security.dao.SecuritySqlDAO;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.dao.UtenteSqlDAO;
import siap.sico.utente.model.UtenteModel;

/**
 * <p>
 * Title: SecurityController
 * </p>
 * <p>
 * Description: Classe che implementa tutti i metodi della sicurezza
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SecurityController extends SiapController implements ISecurity {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public SecurityController() {
	}

	/**
	 * ExLogin - Effettua la login dell'Utente in un ufficio
	 *
	 * @param aUtente
	 * @param aUfficio
	 * @return Utente riconosciuto
	 * @throws F3BException
	 */
	public UtenteModel ExLogin(UtenteModel aUtente, UfficioModel aUfficio) throws F3BException {

		// verifica che l'utente sia valido (definito per l'ufficio richiesto e in corso di validità)
		UtenteModel lUtente = getUtenteValido(aUtente, aUfficio);

		// Verifica la password [** Controllare che la password non sia scaduta **]
		String lPwdCrpt = Utils.cryptPassword(aUtente.getPwd());
		String lPswDB = lUtente.getPwd();
		if (lPswDB == null)
			lPswDB = Utils.cryptPassword("");

		boolean lCmp = (lPswDB).equals(lPwdCrpt);
		if (!lCmp)
			throw new SecurityException(SecurityException.USER_MESSAGE, "Password errata");

		// carica il profilo dell'utente
		ProfileModel lProfiloUtente = getProfiloByCodiceUtente(lUtente.getUserId());
		lUtente.setUserProfile(lProfiloUtente);

		// carica l'ufficio di appartenenza dell'utente
		UfficioModel lUfficioUtente = getUfficioUtente(lUtente);
		lUtente.setUfficioUtente(lUfficioUtente);

		// Update Utente :set Time di ultimo Login
		Utente_setOraLogin(lUtente.getUserId(), aUtente.getIP());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("SecurityController.ExLogin FINE");

		// ritorna il dettaglio dell'utente da mettere in sessione
		return lUtente;
	}

	/**
	 * Login del solo utente
	 *
	 * @param aUtente
	 * @return utente riconosciuto
	 * @throws F3BException
	 */
	public UtenteModel ExLogin(UtenteModel aUtente) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("SecurityController.ExLogin INIZIO");

		// verifica che l'utente sia valido (definito per l'ufficio richiesto e in corso di validità)
		UtenteModel lUtente = getUtenteValido(aUtente);

		// Verifica la password [** Controllare che la password non sia scaduta **]
		String lPwdCrpt = Utils.cryptPassword(aUtente.getPwd());
		String lPswDB = lUtente.getPwd();
		if (lPswDB == null)
			lPswDB = Utils.cryptPassword("");

		boolean lCmp = (lPswDB).equals(lPwdCrpt);
		if (!lCmp)
			throw new SecurityException(SecurityException.USER_MESSAGE, "Password errata");

		// carica il profilo dell'utente
		ProfileModel lProfiloUtente = getProfiloByCodiceUtente(lUtente.getUserId());
		lUtente.setUserProfile(lProfiloUtente);

		// carica l'ufficio di appartenenza dell'utente
		UfficioModel lUfficioUtente = getUfficioUtente(lUtente);
		lUtente.setUfficioUtente(lUfficioUtente);

		// Update Utente :set Time di ultimo Login
		Utente_setOraLogin(lUtente.getUserId(), aUtente.getIP());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("SecurityController.ExLogin FINE");

		// ritorna il dettaglio dell'utente da mettere in sessione
		return lUtente;
	}

	public void ExLogout(UtenteModel aUtente) throws SecurityException {
	}

	/**
	 * Restituisce il profilo per un dato utente
	 *
	 * @param aCodUtente
	 * @return
	 * @throws F3BException
	 */
	private ProfileModel getProfiloByCodiceUtente(String aCodUtente) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("SecurityController.getProfiloByCodiceUtente INIZIO");

		Connection lConn = null;
		SecuritySqlDAO lDao = null;

		ProfileModel lProfilo = null;

		try {
			lConn = getDBConnection();

			lDao = new SecuritySqlDAO(lConn);

			lDao.ricercaProfiloByCodiceUtente(aCodUtente);

			lDao.start();
			if (lDao.next())
				lProfilo = lDao.getProfiloModel();
			else
				throw new SecurityException(SecurityException.USER_MESSAGE, "Utente senza un profilo valido");

			lDao.stop();
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SecurityException("SecurityController.getProfiloByCodiceUtente: Errore : " + ex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("SecurityController.getProfiloByCodiceUtente FINE");

		return lProfilo;
	}

	/**
	 * Load il menu delle funzioni figlie del tipo menu specificato nel model aFunzionePadre.
	 *
	 * @param aProfiloUtente
	 * @param aFunzionePadre
	 * @return FunctionModel
	 * @throws F3BException
	 */
	public FunctionModel ExLoadFunzioniMenu(ProfileModel aProfiloUtente, FunctionModel aFunzionePadre)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("SecurityController.ExLoadFunctionMenu INIZIO");

		ArrayList lFunzioniFiglie = getFunFiglieByCodProfiloTipoVis(aProfiloUtente.getProfileId(),
				aFunzionePadre.getFunctionId(), ICostantiFunzioni.FUNZIONE_MENU);

		aFunzionePadre.setDaughtersFunctions(lFunzioniFiglie);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("SecurityController.ExLoadFunctionMenu FINE");

		return aFunzionePadre;
	}

	/**
	 * Load il menu delle funzioni di scelta rapida
	 *
	 * @param aProfiloUtente
	 * @param aFunzionePadre
	 * @return FunctionModel
	 * @throws F3BException
	 */
	public FunctionModel ExLoadFunzioniMenuSceltaRapida(ProfileModel aProfiloUtente,
			FunctionModel aFunzionePadre) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("SecurityController.ExLoadFunzioniMenuSceltaRapida INIZIO");

		ArrayList lFunzioniFiglie = getFunFiglieByCodProfiloTipoVis(aProfiloUtente.getProfileId(),
				aFunzionePadre.getFunctionId(), ICostantiFunzioni.FUNZIONE_MSR);

		aFunzionePadre.setDaughtersFunctions(lFunzioniFiglie);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("SecurityController.ExLoadFunzioniMenuSceltaRapida FINE");

		return aFunzionePadre;
	}

	/**
	 * Trova la funzione dal nome passato e dal profilo utente
	 *
	 * @param aNomeAzione
	 * @param aCodProfilo
	 * @return La funzione selezionata
	 * @throws F3BException
	 */
	public FunctionModel getFunzioneByAzioneCodProfilo(String aNomeAzione, BigDecimal aCodProfilo)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("SecurityController.getFunzioneByAzioneCodProfilo");

		Connection lConn = null;
		SecuritySqlDAO lDao = null;

		FunctionModel lFun = null;

		try {
			lConn = getDBConnection();

			lDao = new SecuritySqlDAO(lConn);

			lDao.ricercaFunzioneByAzioneCodiceProfilo(aNomeAzione, aCodProfilo);
			lDao.start();

			if (lDao.next()) {
				lFun = new FunctionModel();

				lFun.setFunctionId(lDao.getIdFunzione());
				lFun.setFunctionType(lDao.getCodTipoFunzione());
				lFun.setNameAction(lDao.getAzioneContestoJava());
				// MEV10-s3: aggiunto recupero info
				lFun.setDescription(lDao.getDescrFunzione());
			}

			lDao.stop();
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SecurityException("SecurityController.getFunzioneByAzioneCodProfilo: Errore : " + ex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lFun;
	}

	/**
	 * Restituisce le funzioni figlie
	 *
	 * @param aCodProfilo
	 * @param aIdFunzPadre
	 * @return Lista di funzioni figlie
	 * @throws F3BException
	 */
	public ArrayList getFunFiglieByCodProfilo(BigDecimal aCodProfilo, BigDecimal aIdFunzPadre)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("SecurityController.getFunFiglieByCodProfilo");

		Connection lConn = null;
		SecuritySqlDAO lDao = null;

		ArrayList lList = new ArrayList();

		try {
			lConn = getDBConnection();

			lDao = new SecuritySqlDAO(lConn);

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.info("Sono in getFunFiglieByCodProfilo");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.info("SIESSwitch = " + SIESSwitch.isObscureFunctionOn());

			/*
			 * //Gdv - temporaneo per l'oscuramento di alcune funzioni work in progress
			 * if(SIESSwitch.isObscureFunctionOn()) { int lFirst = SIESSwitch.getObscureFirst(); int lLast =
			 * SIESSwitch.getObscureLast();
			 * lDao.ricercaFunzioniFiglieByCodiceProfiloConOscuramento(aCodProfilo,
			 * aIdFunzPadre,lFirst,lLast); } else{//fine modifica GDV per oscuramento funzioni
			 */
			lDao.ricercaFunzioniFiglieByCodiceProfilo(aCodProfilo, aIdFunzPadre);
			/* }//GDV */

			lDao.start();

			while (lDao.next()) {
				lList.add(lDao.getFunzioneModel());
			}

			lDao.stop();
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SecurityException("SecurityController.getFunFiglieByCodProfilo: Errore : " + ex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lList;
	}

	/**
	 * Restituisce le FUnzioni Figlie per il profilo utente
	 *
	 * @param aCodProfilo
	 * @param aIdFunzPadre
	 * @param aTipoVisualizzazione
	 * @return list di funzioni figlie
	 * @throws F3BException
	 */
	public ArrayList getFunFiglieByCodProfiloTipoVis(BigDecimal aCodProfilo, BigDecimal aIdFunzPadre,
			String aTipoVisualizzazione) throws F3BException {

		Connection lConn = null;
		SecuritySqlDAO lDao = null;

		ArrayList lList = new ArrayList();

		try {
			lConn = getDBConnection();

			lDao = new SecuritySqlDAO(lConn);

			/*
			 * //Gdv - temporaneo per l'oscuramento di alcune funzioni work in progress
			 * if(SIESSwitch.isObscureFunctionOn()) { int lFirst = SIESSwitch.getObscureFirst(); int lLast =
			 * SIESSwitch.getObscureLast();
			 * lDao.ricercaFunzioniByCodProfiloTipoVisualConOscuramento(aCodProfilo, aIdFunzPadre,
			 * aTipoVisualizzazione,lFirst,lLast); } else{//fine modifica GDV per oscuramento funzioni
			 */

			lDao.ricercaFunzioniByCodProfiloTipoVisual(aCodProfilo, aIdFunzPadre, aTipoVisualizzazione);
			/* }//gdv */

			lDao.start();

			while (lDao.next()) {
				lList.add(lDao.getFunzioneModel());
			}

			lDao.stop();
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SecurityException("SecurityController.getFunFiglieByCodProfiloTipoVis: Errore : " + ex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lList;
	}

	/**
	 * Verifica la validità di un utente
	 *
	 * @param aUtente
	 * @param aUfficio
	 * @return Utente
	 * @throws F3BException
	 */
	public UtenteModel getUtenteValido(UtenteModel aUtente, UfficioModel aUfficio) throws F3BException {

		Connection lConn = null;
		SecuritySqlDAO lDao = null;
		UtenteModel lUtente = null;
		try {
			lConn = getDBConnection();
			lDao = new SecuritySqlDAO(lConn);

			lDao.ricercaUtenteValidoByCodiceUtenteUfficio(aUtente.getUserId(), aUfficio.getCodTipoUfficio(),
					aUfficio.getCodComune());

			lDao.start();
			if (lDao.next())
				lUtente = lDao.getUtenteModel();
			else
				throw new SecurityException(SecurityException.USER_MESSAGE,
						"Utente non valido per l'ufficio richiesto");

			lDao.stop();
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SecurityException("SecurityController.getUtenteValido: Errore : " + ex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lUtente;
	}

	/**
	 * Ricerca Ufficio dell'Utente
	 *
	 * @param aUtente
	 * @return
	 * @throws F3BException
	 */
	public UfficioModel getUfficioUtente(UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		SecuritySqlDAO lDao = null;
		UfficioModel lUfficioUtente = null;

		UfficioSqlDAO lUffSqlDao = null;

		try {
			lConn = getDBConnection();
			lDao = new SecuritySqlDAO(lConn);
			lDao.ricercaUfficioByCodiceUtente(aUtente.getUserId());
			lDao.start();
			if (lDao.next())
				lUfficioUtente = lDao.getUfficioModel();
			else
				throw new SecurityException(SecurityException.USER_MESSAGE,
						"L'utente non appartiene a nessun ufficio");

			lDao.stop();

			// ========================================================================
			// New!! recupera anche gli eventuali uffici accorpati sull'ufficio dell'utente
			// in quanto l'utente è automaticamente competente anche su questi uffici
			// ========================================================================
			List lUfficiAccorpati = new ArrayList();

			lUffSqlDao = new UfficioSqlDAO(lConn);
			lUffSqlDao.listaUfficiAccorpati(null, lUfficioUtente.getCodUfficio());
			lUffSqlDao.start();

			while (lUffSqlDao.next()) {
				UfficioAccorpatoModel lUffAcc = (UfficioAccorpatoModel) lUffSqlDao.getUfficioAccorpatoModel();

				lUfficiAccorpati.add(lUffAcc);

			}
			lUffSqlDao.stop();

			lUfficioUtente.setUfficiAccorpati(lUfficiAccorpati);

		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SecurityException("SecurityController.getUfficioByCodiceUtente: Errore : " + ex);
		} finally {
			cleanup(lDao);
			cleanup(lUffSqlDao);
			cleanup(lConn);
		}
		return lUfficioUtente;
	}

	/**
	 * Set dell'ora di Login
	 *
	 * @param aCodUtente
	 * @param aIP
	 * @throws F3BException
	 */
	public void Utente_setOraLogin(String aCodUtente, String aIP) throws F3BException {

		Connection lConn = null;
		UtenteSqlDAO lDao = null;
		try {
			lConn = getDBConnection();
			lDao = new UtenteSqlDAO(lConn);
			lDao.setUtente_DataLogin(aCodUtente, aIP);

			lDao.start();
			lDao.stop();
			commit(lConn);
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SecurityException("SecurityController.getUtenteValido: Errore : " + ex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
	}

	/**
	 * Verifica la Validità di un utente
	 *
	 * @param aUtente
	 * @return
	 * @throws F3BException
	 */
	public UtenteModel getUtenteValido(UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		SecuritySqlDAO lDao = null;
		UtenteModel lUtente = null;

		try {
			lConn = getDBConnection();
			lDao = new SecuritySqlDAO(lConn);
			lDao.ricercaUtenteValidoByCodiceUtente(aUtente.getUserId());

			lDao.start();
			if (lDao.next())
				lUtente = lDao.getUtenteModel();
			else
				throw new SecurityException(SecurityException.USER_MESSAGE, "L'Utente non esiste");

			lDao.stop();
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SecurityException("SecurityController.getUtenteValido: Errore : " + ex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lUtente;
	}

}