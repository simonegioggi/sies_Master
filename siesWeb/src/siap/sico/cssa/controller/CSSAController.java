package siap.sico.cssa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.cssa.dao.CSSASqlDAO;
import siap.sico.cssa.model.CSSAModel;

/**
 * <p>
 * Title: CSSAController
 * </p>
 * <p>
 * Description: Controller per la gestione accesso ai dati del CSSA.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CSSAController extends SiapController implements ICSSA {

	/**
	 * Ritorna l'elenco dei CSSA.
	 * <p>
	 *
	 * @return elenco dei CSSA.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ListaCSSA() throws F3BException {

		Connection lConn = null;
		CSSASqlDAO lCDao = null;
		Vector lCSSA = new Vector();

		try {
			lConn = getDBConnection();
			lCDao = new CSSASqlDAO(lConn);
			lCDao.listaCSSA();
			lCDao.start();

			CSSAModel lUmod = new CSSAModel();

			while (lCDao.next()) {
				// Decodifica
				lUmod = new CSSAModel((CSSAModel) lCDao.getModel());
				// Add al vettore
				lCSSA.add(lUmod);
				// fine while
			}
			lCDao.stop();

			if (lCSSA.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("CSSAController.ListaCSSA: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCDao);
			cleanup(lConn);
		}
		return lCSSA;
	}

	/**
	 * Ritorna l'elenco dei USSM.
	 * <p>
	 *
	 * @return elenco dei USSM.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ListaUSSM() throws F3BException {

		Connection lConn = null;
		CSSASqlDAO lCDao = null;
		Vector lCSSA = new Vector();

		try {
			lConn = getDBConnection();
			lCDao = new CSSASqlDAO(lConn);
			lCDao.listaUSSM();
			lCDao.start();

			CSSAModel lUmod = new CSSAModel();

			while (lCDao.next()) {
				// Decodifica
				lUmod = new CSSAModel((CSSAModel) lCDao.getModel());
				// Add al vettore
				lCSSA.add(lUmod);
				// fine while
			}
			lCDao.stop();

			if (lCSSA.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SICOException("CSSAController.ListaCSSA: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCDao);
			cleanup(lConn);
		}
		return lCSSA;
	}

	/**
	 * Ricerca CSSA by DescrComune.
	 * <p>
	 *
	 * @param aDescrComune
	 *            descrizione del comune.
	 * @return lCSSAMod model CSSA con i relativi dati.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public CSSAModel getCSSAByDescrComune(String aDescrComune) throws F3BException {

		Connection lConn = null;

		CSSASqlDAO lCDao = null;
		CSSAModel lCSSAMod = null;

		try {
			lConn = getDBConnection();
			lCDao = new CSSASqlDAO(lConn);

			lCDao.selCSSAByDescrComune(aDescrComune);
			lCSSAMod = (CSSAModel) lCDao.getModelByKey();

			if (lCSSAMod == null)
				throw new SICOException(SICOException.USER_MESSAGE, "CSSA inesistente");
		} catch (DAOException daoEx) {
			throw new SICOException("CSSAController.getCSSAByDescrComune: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCDao);
			cleanup(lConn);
		}

		return lCSSAMod;
	}

	/**
	 * Ricerca CSSA by DescrComune.
	 * <p>
	 *
	 * @param aDescrComune
	 *            descrizione del comune.
	 * @return lCSSAMod model per USSM con i relativi dati.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public CSSAModel getUSSMByDescrComune(String aDescrComune) throws F3BException {

		Connection lConn = null;

		CSSASqlDAO lCDao = null;
		CSSAModel lCSSAMod = null;

		try {
			lConn = getDBConnection();
			lCDao = new CSSASqlDAO(lConn);

			lCDao.selUSSMByDescrComune(aDescrComune);
			lCSSAMod = (CSSAModel) lCDao.getModelByKey();

			if (lCSSAMod == null)
				throw new SICOException(SICOException.USER_MESSAGE, "USSM inesistente");
		} catch (DAOException daoEx) {
			throw new SICOException("CSSAController.getCSSAByDescrComune: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCDao);
			cleanup(lConn);
		}

		return lCSSAMod;
	}

	public CSSAModel ExRicercaCSSAByIdFascicoloVerbaleNonFirmato(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;

		CSSASqlDAO lCDao = null;
		CSSAModel lCSSAMod = null;

		try {
			lConn = getDBConnection();
			lCDao = new CSSASqlDAO(lConn);

			lCDao.ricercaCSSAByIdFascicoloVerbaleNonFirmato(aIdFascicolo);
			lCSSAMod = (CSSAModel) lCDao.getModelByKey();

			if (lCSSAMod == null)
				throw new SICOException(SICOException.USER_MESSAGE, "CSSA inesistente");
		} catch (DAOException daoEx) {
			throw new SICOException(
					"CSSAController.ExRicercaCSSAByIdFascicolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCDao);
			cleanup(lConn);
		}

		return lCSSAMod;
	}

	// Ricerca by Fascicolo
	public CSSAModel ExRicercaCSSAByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;

		CSSASqlDAO lCDao = null;
		CSSAModel lCSSAMod = null;

		try {
			lConn = getDBConnection();
			lCDao = new CSSASqlDAO(lConn);

			lCDao.ricercaCSSAByIdFascicolo(aIdFascicolo);
			lCSSAMod = (CSSAModel) lCDao.getModelByKey();

			if (lCSSAMod == null)
				throw new SICOException(SICOException.USER_MESSAGE, "CSSA inesistente");
		} catch (DAOException daoEx) {
			throw new SICOException(
					"CSSAController.ExRicercaCSSAByIdFascicolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCDao);
			cleanup(lConn);
		}

		return lCSSAMod;
	}

	/**
	 * Ricerca CSSA by Key
	 * <p>
	 *
	 * @param aIdCSSA
	 *            codice
	 * @return lCSSAMod
	 * @throws F3BException
	 */
	public CSSAModel getCSSAByKey(BigDecimal aIdCSSA) throws F3BException {

		Connection lConn = null;

		CSSASqlDAO lCDao = null;
		CSSAModel lCSSAMod = null;

		try {
			lConn = getDBConnection();
			lCDao = new CSSASqlDAO(lConn);

			lCDao.selModelCssabyKey(aIdCSSA);
			lCSSAMod = (CSSAModel) lCDao.getModelByKey();

			if (lCSSAMod == null)
				throw new SICOException(SICOException.USER_MESSAGE, "CSSA inesistente");
		} catch (DAOException daoEx) {
			throw new SICOException("CSSAController.getCodCSSAByDescrComune: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCDao);
			cleanup(lConn);
		}

		return lCSSAMod;
	}

	public Vector ExGetListaComuniCssa(CSSAModel lModel) throws F3BException {

		Connection lConn = null;
		CSSASqlDAO lDao = null;

		Vector lCollComuni = new Vector();

		try {
			lConn = getDBConnection();

			lDao = new CSSASqlDAO(lConn);
			lDao.selCondizioniCssaLike(lModel);
			lCollComuni = new Vector(lDao.getModels());
			if (lCollComuni.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoex) {
			throw new SICOException(
					"ComuneController.ExGetListaComuniCssa: Non posso leggere i comuni : " + daoex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lCollComuni;
	}

	public Vector ExGetListaComuniCssaMinor(CSSAModel lModel) throws F3BException {

		Connection lConn = null;
		CSSASqlDAO lDao = null;

		Vector lCollComuni = new Vector();

		try {
			lConn = getDBConnection();

			lDao = new CSSASqlDAO(lConn);
			lDao.selCondizioniCssaMinorLike(lModel);
			lCollComuni = new Vector(lDao.getModels());
			if (lCollComuni.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoex) {
			throw new SICOException(
					"ComuneController.ExGetListaComuniCssa: Non posso leggere i comuni : " + daoex);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lCollComuni;
	}

	/**
	 * MEV10-s3: aggiunto metodo per estrarre il CSSA
	 */
	public CSSAModel getCSSAByDescrComuneETipo(String aDescrComune, String aTipo) throws F3BException {

		Connection lConn = null;
		CSSASqlDAO lCDao = null;
		CSSAModel lCSSAMod = null;

		try {
			lConn = getDBConnection();
			lCDao = new CSSASqlDAO(lConn);

			lCDao.selCSSAByDescrComuneETipo(aDescrComune, aTipo);
			lCSSAMod = (CSSAModel) lCDao.getModelByKey();

			if (lCSSAMod == null)
				throw new SICOException(SICOException.USER_MESSAGE, "CSSA inesistente");
		} catch (DAOException daoEx) {
			throw new SICOException("CSSAController.getCSSAByDescrComune: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCDao);
			cleanup(lConn);
		}

		// valore di ritorno
		return lCSSAMod;
	}

}