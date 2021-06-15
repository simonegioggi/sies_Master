package siap.sico.utente.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.controller.SiapController;
import siap.sico.profilo.controller.ProfiloController;
import siap.sico.profilo.model.ProfiloModel;
import siap.sico.security.controller.SecurityController;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.dao.UfficioUtenteDao;
import siap.sico.utente.dao.UtenteDAO;
import siap.sico.utente.dao.UtenteSqlDAO;
import siap.sico.utente.model.UtenteModel;
import siap.sico.utente.model.UtenteViewModel;
import siap.sico.utenzaAdn.dao.AssocUtenteSiesAdnDAO;

/**
 * <p>
 * Title: UtenteController
 * </p>
 * <p>
 * Description: Classe Controller per Utente
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UtenteController extends SiapController implements IUtente {

	public UtenteModel ExInserisciUtente(UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		UtenteDAO lUteDao = null;
		UtenteModel lUteMod = null;
		UtenteSqlDAO lUteSDao = null;

		try {
			lConn = getDBTransaction();
			// lUteMod = new UtenteModel(aUtente);
			lUteDao = new UtenteDAO(lConn);
			lUteSDao = new UtenteSqlDAO(lConn);
			lUteDao.setDAOFromModel(aUtente);
			// BigDecimal lKey = null;
			/* lKey = */lUteDao.insert();
			lUteSDao.setUtente_Profilo(aUtente, aUtente.getUserProfile().getProfileId());
			lUteSDao.start();
			lUteSDao.stop();
			lUteSDao.setUtente_Ufficio(aUtente, aUtente.getUfficioUtente().getCodUfficio());
			lUteSDao.start();
			lUteSDao.stop();
			commit(lConn);
			// lUteMod.setIdUtente(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("UtenteController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lUteDao);
			cleanup(lUteSDao);
			cleanup(lConn);
		}
		return lUteMod;
	}

	public Vector ExRicercaUtente(UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		Vector lUtenti = new Vector();
		UtenteSqlDAO lUteDao = null;

		try {
			lConn = getDBConnection();
			lUteDao = new UtenteSqlDAO(lConn);
			lUteDao.ricercaUtente(aUtente);
			lUtenti = new Vector(lUteDao.getModels());

			if (lUtenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("UtenteController.ExRicercaUtente: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUteDao);
			cleanup(lConn);
		}
		return lUtenti;
	}

	public Vector ExListaUtentiAttivi(int aPage) throws F3BException {

		Connection lConn = null;
		Vector lUtenti = new Vector();
		UtenteSqlDAO lUteDao = null;
		ProfiloController lPrfCnt = new ProfiloController();
		SecurityController lSecCnt = new SecurityController();

		try {
			lConn = getDBConnection();
			lUteDao = new UtenteSqlDAO(lConn);
			lUteDao.listaUtentiAttivi(aPage);
			lUtenti = new Vector(lUteDao.getModels());
			UtenteModel lUt;

			for (int i = 0; i < lUtenti.size(); i++) {
				lUt = (UtenteModel) lUtenti.get(i);
				ProfileModel lprf = new ProfileModel();
				lUt.setUfficioUtente(new UfficioModel(lSecCnt.getUfficioUtente(lUt)));
				lprf.setDescription((new ProfiloModel(lPrfCnt.ExRicercaProfiloByCodUtente(lUt.getUserId())))
						.getDescrizione());
				lUt.setUserProfile(lprf);

			}
			if (lUtenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("UtenteController.ExRicercaUtente: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUteDao);
			cleanup(lConn);
		}
		return lUtenti;
	}

	/**
	 * Metodo che ritorna la lista degli Utenti Attivi, suddivisi per pagina e prelevati dalla tabella di
	 * View.
	 * <p>
	 *
	 * @param aPage
	 *            int numero di pagina
	 * @throws F3BException
	 *             propagazione errore di eccezione.
	 * @return Vector elenco degli utenti Attivi.
	 */
	public Vector ExListaUtentiAttiviFromView(int aPage, String aDistretto) throws F3BException {

		return listaUtentiAttiviFromView(aPage, null, aDistretto);
	}

	/**
	 * Metodo che ritorna la lista degli utenti attivi, filtrati per ufficio e paginati. I dati sono prelevati
	 * dalla tabella di View.
	 * <p>
	 *
	 * @param aPage
	 *            int Paginazione
	 * @param aUfficio
	 *            String codice dell'ufficio per cui filtrare
	 * @throws F3BException
	 *             propaga errore di eccezione
	 * @return Vector elenco degli utenti attivi.
	 */
	public Vector ExListaUtentiAttiviFromViewPerUfficio(int aPage, String aUfficio) throws F3BException {

		return listaUtentiAttiviFromView(aPage, aUfficio, null);
	}

	/**
	 * Metodo che ritorna la lista degli utenti non attivi e paginati. I dati sono prelevati dalla tabella di
	 * View.
	 * <p>
	 *
	 * @param aPage
	 *            int Paginazione
	 * @throws F3BException
	 *             propaga errore di eccezione
	 * @return Vector elenco utenti non attivi.
	 */
	public Vector ExListaUtentiNonAttiviFromView(int aPage, String aDistretto) throws F3BException {

		return listaUtentiNonAttiviFromView(aPage, null, aDistretto);
	}

	/**
	 * Metodo che ritorna la lista degli utenti non attivi filtrati per ufficio
	 * <p>
	 *
	 * @param aPage
	 *            int numero di pagina
	 * @param aUfficio
	 *            String codice ufficio
	 * @throws F3BException
	 *             propaga errore di eccezione
	 * @return Vector elenco utenti non attivi
	 */
	public Vector ExListaUtentiNonAttiviFromViewPerUfficio(int aPage, String aUfficio) throws F3BException {

		return listaUtentiNonAttiviFromView(aPage, aUfficio, null);
	}

	/**
	 * Metodo visibile all'interno del controller, richiede l'elenco degli utenti Attivi, filtrati per
	 * ufficio. Se il parametro ufficio è pari a null, ritorna l'elenco degli utenti attivi per tutti gli
	 * uffici.
	 * <p>
	 *
	 * @param aPage
	 *            int numero di pagina
	 * @param aUfficio
	 *            String codice ufficio
	 * @throws F3BException
	 *             propaga errore di eccezione
	 * @return Vector elenco utenti
	 */
	protected Vector listaUtentiAttiviFromView(int aPage, String aUfficio, String aDistretto)
			throws F3BException {

		Connection lConn = null;
		Vector lUtenti = new Vector();
		UtenteSqlDAO lUteDao = null;

		try {
			lConn = getDBConnection();
			lUteDao = new UtenteSqlDAO(lConn);
			lUteDao.ListaUtentiAttiviFromView(aPage, aUfficio, aDistretto);
			lUtenti = new Vector();
			lUteDao.start();
			UtenteViewModel lUt;

			while (lUteDao.next()) {
				lUt = lUteDao.getModelFromView();
				lUtenti.add(lUt);
			}

			lUteDao.stop();

			if (lUtenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UtenteController.ExListaUtentiAttiviFromView: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUteDao);
			cleanup(lConn);
		}
		return lUtenti;
	}

	/**
	 * Metodo visibile all'interno del controller, richiede l'elenco degli utenti Non Attivi, filtrati per
	 * ufficio. Se il parametro ufficio è pari a null, ritorna l'elenco degli utenti Non Attivi per tutti gli
	 * uffici.
	 * <p>
	 *
	 * @param aPage
	 *            int numero di pagina
	 * @param aUfficio
	 *            String codice ufficio
	 * @throws F3BException
	 *             propaga errore di eccezione
	 * @return Vector elenco utenti
	 */
	protected Vector listaUtentiNonAttiviFromView(int aPage, String aUfficio, String aDistretto)
			throws F3BException {

		Connection lConn = null;
		Vector lUtenti = new Vector();
		UtenteSqlDAO lUteDao = null;

		try {
			lConn = getDBConnection();
			lUteDao = new UtenteSqlDAO(lConn);
			lUteDao.ListaUtentiNonAttiviFromView(aPage, aUfficio, aDistretto);
			lUtenti = new Vector();
			lUteDao.start();
			UtenteViewModel lUt;

			while (lUteDao.next()) {
				lUt = lUteDao.getModelFromView();
				lUtenti.add(lUt);
			}

			lUteDao.stop();

			if (lUtenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UtenteController.ExListaUtentiNonAttiviFromView: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUteDao);
			cleanup(lConn);
		}
		return lUtenti;
	}

	public Vector ExListaUtentiNonAttivi(int aPage) throws F3BException {

		Connection lConn = null;
		Vector lUtenti = new Vector();
		UtenteSqlDAO lUteDao = null;
		ProfiloController lPrfCnt = new ProfiloController();
		SecurityController lSecCnt = new SecurityController();

		try {
			lConn = getDBConnection();
			lUteDao = new UtenteSqlDAO(lConn);
			lUteDao.listaUtentiNonAttivi(aPage);
			lUtenti = new Vector(lUteDao.getModels());
			UtenteModel lUt;
			for (int i = 0; i < lUtenti.size(); i++) {
				lUt = (UtenteModel) lUtenti.get(i);
				ProfileModel lprf = new ProfileModel();
				lUt.setUfficioUtente(new UfficioModel(lSecCnt.getUfficioUtente(lUt)));
				lprf.setDescription((new ProfiloModel(lPrfCnt.ExRicercaProfiloByCodUtente(lUt.getUserId())))
						.getDescrizione());
				lUt.setUserProfile(lprf);

			}
			if (lUtenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("UtenteController.ExRicercaUtente: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUteDao);
			cleanup(lConn);
		}
		return lUtenti;
	}

	public UtenteModel ExRicercaUtenteByKey(String aKey) throws F3BException {

		Connection lConn = null;
		UtenteSqlDAO lUteDao = null;
		UtenteModel lUteMod;
		ProfiloController lPrfCnt = new ProfiloController();
		SecurityController lSecCnt = new SecurityController();

		try {
			lConn = getDBConnection();
			lUteDao = new UtenteSqlDAO(lConn);
			lUteDao.ricercaUtenteByKey(aKey);
			lUteMod = (UtenteModel) lUteDao.getModelByKey();
			if (lUteMod == null)
				return null;
			ProfileModel lprf = new ProfileModel();
			lUteMod.setUfficioUtente(new UfficioModel(lSecCnt.getUfficioUtente(lUteMod)));
			ProfiloModel ltmp = new ProfiloModel(lPrfCnt.ExRicercaProfiloByCodUtente(lUteMod.getUserId()));
			lprf.setDescription(ltmp.getDescrizione());
			lprf.setProfileId(ltmp.getCodProfilo());
			lUteMod.setUserProfile(lprf);
		} catch (DAOException daoEx) {
			throw new F3BException("UtenteController.ExRicercaUtente: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUteDao);
			cleanup(lConn);
		}
		return lUteMod;
	}

	public UtenteModel ExModificaUtente(UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		UtenteDAO lUteDao = null;
		UtenteModel lUteMod = new UtenteModel(aUtente);
		UtenteSqlDAO lUteSDao = null;

		try {
			lConn = getDBConnection();
			lUteDao = new UtenteDAO(lConn);
			lUteDao.setDataFineValidita(aUtente.getDataFineValidita());
			lUteDao.setCognome(aUtente.getCognome());
			lUteDao.setNome(aUtente.getNome());
			lUteDao.setTelefono(aUtente.getTelefono());
			lUteDao.setEmail(aUtente.getEmail());
			lUteDao.setFax(aUtente.getFax());
			lUteDao.setCondizioneUpdate(aUtente.getUserId());
			lUteDao.update();
			lUteSDao = new UtenteSqlDAO(lConn);
			lUteSDao.updateProfiloByCodUtente(aUtente.getUserId(), aUtente.getUserProfile().getProfileId());
			lUteSDao.start();
			lUteSDao.stop();
			lUteSDao.updateUfficioByCodUtente(aUtente.getUserId(),
					aUtente.getUfficioUtente().getCodUfficio());
			lUteSDao.start();
			lUteSDao.stop();
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("UtenteController.ExModifica: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"UtenteController.ExModificaUtente: Non posso inserire il soggetti : " + ex);
		} finally {
			cleanup(lUteDao);
			cleanup(lUteSDao);

			cleanup(lConn);
		}
		return lUteMod;
	}

	public void ExModificaPassword(UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		UtenteDAO lUteDao = null;
		UtenteSqlDAO lUteSDao = null;
		// MEV INTEGRAZIONE SIES ADN: aggiunta variabile
		AssocUtenteSiesAdnDAO ausaDAO = null;

		try {
			lConn = getDBConnection();
			lUteDao = new UtenteDAO(lConn);
			lUteDao.setPwd(Utils.cryptPassword(aUtente.getPwd()));
			lUteDao.setCondizioneUpdate(aUtente.getUserId());
			lUteDao.update();
			// MEV INTEGRAZIONE SIES ADN: sia che provengo da forza cambio psw che da modifica psw,
			// poichè imposto da amministratore di sistema o di ufficio, allora eseguo anche l'operazione
			// di deassociazione utente_sies_adn
			ausaDAO = new AssocUtenteSiesAdnDAO(lConn);
			ausaDAO.setCondizioneByUteCodUtente(aUtente.getUserId());
			ausaDAO.delete();
			// FINE MEV INTEGRAZIONE SIES ADN
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("UtenteController.ExModifica: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"UtenteController.ExModificaUtente: Non posso inserire il soggetti : " + ex);
		} finally {
			cleanup(lUteDao);
			cleanup(lUteSDao);

			cleanup(lConn);
		}
	}

	public void ExCancellaUtente(UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		UtenteDAO lUteDao = null;
		UtenteSqlDAO lUteSDao = null;

		try {
			lConn = getDBConnection();
			lUteSDao = new UtenteSqlDAO(lConn);
			lUteSDao.setUtente_DataFine(aUtente);
			lUteSDao.start();
			lUteSDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("UtenteController.ExCancellaUtente: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("UtenteController.ExCancellaUtente: Non posso leggere  : " + ex);
		} finally {
			cleanup(lUteDao);
			cleanup(lUteSDao);

			cleanup(lConn);
		}
	}

	public Vector ExRicercaUtentiPerUfficio(String codUfficio, String aCodDistretto, String cognome,
			String nome, String codUt) throws F3BException {

		Vector lUtenti = null;
		UfficioUtenteDao lUffDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();
			lUffDao = new UfficioUtenteDao(lConn);
			lUffDao.ricercaUtentiPerUfficio(codUfficio, aCodDistretto, cognome, nome, codUt);
			lUtenti = new Vector(lUffDao.getModels());

			if (lUtenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (SQLException sqe) {
			throw new F3BException("UtenteController.ExRicercaUtentiPerUfficio: Non posso leggere  : " + sqe);
		} finally {
			cleanup(lUffDao);
			cleanup(lConn);
		}

		return lUtenti;
	}

	/**
	 * Effettua la ricerca di un utente su un ufficio per nome e cognome. Tutti i campi sono obbligatori. La
	 * ricerca è case sensitive.
	 */
	public Vector ExRicercaUtentePerUfficioCognomeNome(String codUfficio, String cognome, String nome)
			throws F3BException {

		Vector lUtenti = null;
		UfficioUtenteDao lUffDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();
			lUffDao = new UfficioUtenteDao(lConn);
			lUffDao.ricercaUtentePerUfficioCognomeNome(codUfficio, cognome, nome);
			lUtenti = new Vector();
			lUffDao.start();
			UtenteViewModel lUt;

			while (lUffDao.next()) {
				lUt = lUffDao.getModelFromView();
				lUtenti.add(lUt);
			}
			lUffDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UtenteController.ExRicercaUtentePerUfficioCognomeNome:: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUffDao);
			cleanup(lConn);
		}

		return lUtenti;
	}

	/**
	 * Metodo che esegue il conteggio degli utenti Attivi.
	 * <p>
	 *
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return BigDecimal ritorna il totale degli utenti Attivi
	 */
	public BigDecimal ExGetCountUtentiAttivi(String aDistretto) throws F3BException {

		return getCountUtentiAttivi(null, aDistretto);
	}

	/**
	 * Metodo che esegue il conteggio deli utenti attivi, opportunamente filtrati per il codice ufficio.
	 * <p>
	 *
	 * @param aUfficio
	 *            String Codice ufficio
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return BigDecimal totale degli utemnti attivi.
	 */
	public BigDecimal ExGetCountUtentiAttiviPerUfficio(String aUfficio) throws F3BException {

		return getCountUtentiAttivi(aUfficio, null);
	}

	/**
	 * Metodo che ritorna il numero totale degli utenti non attivi, per tutti gli uffici.
	 * <p>
	 *
	 * @throws F3BException
	 *             propaga l'errore di eccezione
	 * @return BigDecimal ritorna il numero degli utenti non attivi
	 */
	public BigDecimal ExGetCountUtentiNonAttivi(String aDistretto) throws F3BException {

		return getCountUtentiNonAttivi(null, aDistretto);
	}

	/**
	 * Metodo che ritorna il numero totale degli utenti non attivi, opportunamente filtrati per Ufficio.
	 * <p>
	 *
	 * @param aUfficio
	 *            String codice ufficio per il quale filtrare la lista degli utenti Attivi.
	 * @throws F3BException
	 *             Proaga erroi di eccezione.
	 * @return BigDecimal ritorna il numero degli utenti non attivi.
	 */
	public BigDecimal ExGetCountUtentiNonAttiviPerUfficio(String aUfficio) throws F3BException {

		return getCountUtentiNonAttivi(aUfficio, null);
	}

	/**
	 * Metodo visibile interno al controller, che esegue l'interrogazione del numero degli utentei Non Attivi,
	 * per un ufficio. Se il parametro ufficio è pari a null, ritorna il numero di utenti Non Attivi per tutti
	 * gli uffici. taòle
	 * <p>
	 *
	 * @param aUfficio
	 *            String codice uffcio
	 * @throws F3BException
	 *             propaga errore di eccezione
	 * @return BigDecimal ritorna il numero degli utenti non attivi.
	 */
	protected BigDecimal getCountUtentiNonAttivi(String aUfficio, String aDistretto) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		UtenteSqlDAO lUtenteSqlDAO = null;
		try {
			lConn = getDBConnection();
			lUtenteSqlDAO = new UtenteSqlDAO(lConn);
			lUtenteSqlDAO.getCountUtentiNonAttivi(aUfficio, aDistretto);
			lUtenteSqlDAO.start();
			lUtenteSqlDAO.next();
			lCount = lUtenteSqlDAO.getBigDecimal("HowManyRecords");
			lUtenteSqlDAO.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("UtenteController.ExRicercaUtente: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUtenteSqlDAO);
			cleanup(lConn);
		}

		return lCount;

	}

	/**
	 * Metodo visibile interno al controller, che esegue l'interrogazione del numero degli utenti Attivi, per
	 * un ufficio. Se il parametro ufficio è pari a null, ritorna il numero di utentu Attivi per tutti gli
	 * uffici.
	 *
	 * @param aUfficio
	 *            String codice uffcio
	 * @throws F3BException
	 *             propaga errore di eccezione
	 * @return BigDecimal ritorna il numero degli utenti non attivi.
	 */
	protected BigDecimal getCountUtentiAttivi(String aUfficio, String aDistretto) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		UtenteSqlDAO lUtenteSqlDAO = null;
		try {
			lConn = getDBConnection();
			lUtenteSqlDAO = new UtenteSqlDAO(lConn);
			lUtenteSqlDAO.getCountUtentiAttivi(aUfficio, aDistretto);
			lUtenteSqlDAO.start();
			lUtenteSqlDAO.next();
			lCount = lUtenteSqlDAO.getBigDecimal("HowManyRecords");
			lUtenteSqlDAO.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("UtenteController.getCountUtentiAttivi: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUtenteSqlDAO);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Metodo che restituisce la lista degli utenti Attivi per ufficio
	 * <p>
	 *
	 * @param aUfficio
	 *            String codice uffcio
	 * @throws F3BException
	 *             propaga errore di eccezione
	 * @return Vector ritorna il numero degli utenti non attivi.
	 */
	public Vector ExRicercaUtentiAttiviPerUfficio(String codUfficio, String aCognome) throws F3BException {

		Vector lUtenti = null;
		UfficioUtenteDao lUffDao = null;
		Connection lConn = null;

		try {

			lConn = getDBConnection();
			lUffDao = new UfficioUtenteDao(lConn);
			lUffDao.ricercaUtentiAttiviPerUfficio(codUfficio, aCognome);
			lUtenti = new Vector(lUffDao.getModels());

			if (lUtenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UtenteController.ExRicercaUtentiAttiviPerUfficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUffDao);
			cleanup(lConn);
		}
		return lUtenti;
	}

	public UtenteModel ExModificaUtenteDatiAccessoNSC(UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		UtenteDAO lUteDao = null;

		String pwdDecod = "";
		if (aUtente.getPwdNSC() != null && !aUtente.getPwdNSC().equals("")) {
			pwdDecod = Utils.pwdNSCEncode(aUtente.getPwdNSC());
		}
		aUtente.setPwdNSC(pwdDecod);
		UtenteModel lUteMod = new UtenteModel(aUtente);

		try {
			lConn = getDBConnection();
			lUteDao = new UtenteDAO(lConn);
			lUteDao.setUseridNSC(aUtente.getUseridNSC());
			lUteDao.setPwdNSC(aUtente.getPwdNSC());
			lUteDao.setCodOperatoreAggiornamento(aUtente.getCodOperatoreAggiornamento());
			lUteDao.setDataAggiornamento(aUtente.getDataAggiornamento());
			lUteDao.setCondizioneUpdate(aUtente.getUserId());
			lUteDao.update();
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException(
					"UtenteController.ExModificaUtenteDatiAccessoNSC: Non posso modificare utente: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"UtenteController.ExModificaUtenteDatiAccessoNSC: Non posso modificare utente: " + ex);
		} finally {
			cleanup(lUteDao);
			cleanup(lConn);
		}
		return lUteMod;
	}

	public UtenteModel ExResetDatiAccessoNSC(UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		UtenteDAO lUteDao = null;
		aUtente.setUseridNSC(null);
		aUtente.setPwdNSC(null);
		UtenteModel lUteMod = new UtenteModel(aUtente);

		try {
			lConn = getDBConnection();
			lUteDao = new UtenteDAO(lConn);
			lUteDao.setCodOperatoreAggiornamento(aUtente.getCodOperatoreAggiornamento());
			lUteDao.setDataAggiornamento(DateUtils.getSysDate());
			lUteDao.setCondizioneUpdate(aUtente.getUserId());
			lUteDao.update();
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException(
					"UtenteController.ExResetDatiAccessoNSC: Non posso modificare utente: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"UtenteController.ExResetDatiAccessoNSC: Non posso modificare utente: " + ex);
		} finally {
			cleanup(lUteDao);
			cleanup(lConn);
		}
		return lUteMod;
	}

}