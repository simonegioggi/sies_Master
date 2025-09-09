package siap.sige.tenore.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.dao.EventoDAO;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.datiprovsige.dao.DatiProvvedimentoSigeDAO;
import siap.sige.datiprovsige.model.DatiProvvedimentoSigeModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.dao.FasSigeSentenzaDAO;
import siap.sige.sentenza.model.FasSigeSentenzaModel;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.tenore.dao.TenoreSentenzaReatoDAO;
import siap.sige.tenore.dao.TenoreSigeDAO;
import siap.sige.tenore.dao.TenoreSigeSqlDAO;
import siap.sige.tenore.model.TenoreSentenzaReatoModel;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: TenoreSigeController
 * </p>
 * <p>
 * Description: Classe Controller per TenoreSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TenoreSigeController extends GenericController implements ITenoreSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public Vector ExRicercaTenoreSige(TenoreSigeModel aTenoreSige) throws F3BException {

		Connection lConn = null;
		Vector lTenoreSigi = new Vector();
		TenoreSigeDAO lTenDao = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSigeDAO(lConn);
			lTenDao.selCondizione(aTenoreSige);
			lTenDao.start();
			while (lTenDao.next()) {
				lTenoreSigi.add(lTenDao.getModel());
			}
			if (lTenoreSigi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreSige: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreSige: " + e);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenoreSigi;
	}

	/**
	 * Ricerca degli Oggetti della Richiesta_Sige.
	 *
	 * @param aIdRichiesta
	 * @return
	 * @throws F3BException
	 */
	public Vector<TenoreSigeModel> ExRicercaTenoreByRichiesta(BigDecimal aIdRichiesta) throws F3BException {

		Connection lConn = null;
		Vector<TenoreSigeModel> lTenoriSige = null;
		TenoreSigeSqlDAO lTenDao = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSigeSqlDAO(lConn);
			lTenDao.ricercaTenoriByRichiesta(aIdRichiesta);
			lTenoriSige = new Vector<TenoreSigeModel>(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta DAOException: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta Exception: " + e);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenoriSige;
	}

	/**
	 * 02/12/2009 Ricerca degli Oggetti della Richiesta_Sige.
	 *
	 * @param lConn
	 * @param aIdRichiesta
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaTenoreByRichiesta(BigDecimal aIdRichiesta, Connection aConn) throws F3BException {

		Vector lTenoriSige = null;
		TenoreSigeSqlDAO lTenDao = null;
		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		Connection lConn = null;

		try {
			if (aConn != null)
				lConn = aConn;
			else
				lConn = getDBConnection();
			lTenDao = new TenoreSigeSqlDAO(lConn);
			lTenDao.ricercaTenoriByRichiesta(aIdRichiesta);
			lTenoriSige = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta DAOException: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta Exception: " + e);
		} finally {
			cleanup(lTenDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			if (aConn == null)
				cleanup(lConn);
		}
		return lTenoriSige;
	}

	public Vector<TenoreSigeModel> ExRicercaTenoreById(BigDecimal aITenoreSige) throws F3BException {

		Connection lConn = null;
		Vector<TenoreSigeModel> lTenoriSige = null;
		TenoreSigeSqlDAO lTenDao = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSigeSqlDAO(lConn);
			lTenDao.ricercaTenoriById(aITenoreSige);
			lTenoriSige = new Vector<TenoreSigeModel>(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreById DAOException: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreById Exception: " + e);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenoriSige;
	}

	public Vector<TenoreSigeEstesoModel> ExRicercaTenoreEstesoById(BigDecimal aITenoreSige)
			throws F3BException {

		Vector<TenoreSigeModel> lTenoriSige = null;
		Vector<TenoreSigeEstesoModel> lTenoriEstesi = new Vector<>();
		IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
		IFasSigeSentenza lFSSCtrl = SIGELookupRemote.getFasSigeSentenzaRemote(); // 20/01/2010

		try {
			// Ricerca dei Tenori Sige
			lTenoriSige = ExRicercaTenoreById(aITenoreSige);
			for (TenoreSigeModel lTenore : lTenoriSige) {
				SentenzaModel lSentenza = ricercaSentenzaByKey(lTenore.getIdSentenza());
				// Reato può anche non essere definito
				ReatoModel lReato = null;
				if (lTenore.getIdReato() != null)
					lReato = lReaCtrl.ExRicercaReatoByKey(lTenore.getIdReato());
				// Creazione del Model Aggregato ed aggiunta all'elenco Risultato
				TenoreSigeEstesoModel lTenoreEsteso = new TenoreSigeEstesoModel(lTenore, lSentenza, lReato);

				// 20/01/2010 Si legge FasSigeSentenzaModel.
				FasSigeSentenzaModel lFasSigeSentenza = new FasSigeSentenzaModel();
				lFasSigeSentenza.setSenIdSentenza(lTenore.getIdSentenza());
				lFasSigeSentenza.setFasIdFascicoloSige(lTenore.getFasIdFascicoloSige());
				Vector lFSSentenza = lFSSCtrl.ExRicercaFSSentenza(lFasSigeSentenza);
				if (lFSSentenza != null && lFSSentenza.size() > 0)
					lTenoreEsteso.setSentenzaSige((SentenzaSigeModel) lFSSentenza.get(0));

				lTenoriEstesi.add(lTenoreEsteso);
			}
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreEstesoById Exception: " + e);
		}
		return lTenoriEstesi;
	}

	/**
	 * Effettua la ricerca dei Tenori legati ad un Fascicolo o ad una Richiesta o ad un Provvedimento in base
	 * a quale di questi id sia valorizzato nel TenoreModel passato.
	 *
	 * @param aTenore
	 * @return
	 * @throws F3BException
	 */
	public Vector<TenoreSigeModel> ExRicercaTenori(TenoreSigeModel aTenore) throws F3BException {

		Connection lConn = null;
		Vector<TenoreSigeModel> lTenoriSige = new Vector<>();
		TenoreSigeSqlDAO lTenDao = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSigeSqlDAO(lConn);
			lTenDao.ricercaTenoriSige(aTenore);
			// 07/04/2010 Caricamento in ciclo per effettuare la decodifica del TenoreModel
			// lTenoriSige = new Vector ( lTenDao.getModels());
			lTenDao.start();
			TenoreSigeModel lTenore = null;
			lTenoriSige = new Vector();
			while (lTenDao.next()) {
				lTenore = (TenoreSigeModel) lTenDao.getModel();
				lTenore.decodifica();
				lTenoriSige.add(lTenore);
			}
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreSigeController.ExRicercaTenori DAOException: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenori Exception: " + e);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenoriSige;
	}

	/**
	 * Effettua la ricerca dei Tenori Attivi (cioè con DATA FINE a null) legati ad un Fascicolo o ad una
	 * Richiesta o ad un Provvedimento in base a quale di questi id sia valorizzato nel TenoreModel passato.
	 *
	 * @param aTenore
	 * @return
	 * @throws F3BException
	 */
	public Vector<TenoreSigeModel> ExRicercaTenoriAttivi(TenoreSigeModel aTenore) throws F3BException {

		Connection lConn = null;
		Vector<TenoreSigeModel> lTenoriSige = null;
		TenoreSigeSqlDAO lTenDao = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSigeSqlDAO(lConn);
			lTenDao.ricercaTenoriSigeAttivi(aTenore);
			lTenoriSige = new Vector(lTenDao.getModels());
			for (int i = 0; i < lTenoriSige.size(); i++) {
				TenoreSigeModel lTenore = lTenoriSige.get(i);
				lTenore.decodifica();
				// lTenoriSige.setElementAt(lTenore, i);
			}
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoriAttivi DAOException: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoriAttivi Exception: " + e);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenoriSige;
	}

	/**
	 * La funzione effettua la ricerca degli Oggetti legati ad una richiesta SIGE. Per ognuno degli Oggetti
	 * trovati viene restituito TenoreSigeEstesoModel, ovvero il model aggregato di Tenore, Sentenza e Reato.
	 *
	 * @param aIdRichiesta
	 * @return Vector di TenoreSigeEstesoModel
	 * @throws F3BException
	 */
	public Vector<TenoreSigeEstesoModel> ExRicercaTenoreEstesoByRichiesta(BigDecimal aIdRichiesta)
			throws F3BException {

		Vector<TenoreSigeModel> lTenoriSige = null;
		Vector<TenoreSigeEstesoModel> lTenoriEstesi = new Vector<>();
		IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
		IFasSigeSentenza lFSSCtrl = SIGELookupRemote.getFasSigeSentenzaRemote(); // 20/01/2010

		try {
			// Ricerca dei Tenori Sige
			lTenoriSige = ExRicercaTenoreByRichiesta(aIdRichiesta);

			// Si itera sull'elenco dei Tenori per trovare Sentenza e Reato
			Iterator itx = lTenoriSige.iterator();
			while (itx.hasNext()) {
				TenoreSigeModel lTenore = (TenoreSigeModel) itx.next();
				SentenzaModel lSentenza = ricercaSentenzaByKey(lTenore.getIdSentenza());
				ReatoModel lReato = lReaCtrl.ExRicercaReatoByKey(lTenore.getIdReato());
				// Creazione del Model Aggregato ed aggiunta all'elenco Risultato
				TenoreSigeEstesoModel lTenoreEsteso = new TenoreSigeEstesoModel(lTenore, lSentenza, lReato);

				// 20/01/2010 Si legge SentenzaSigeModel.
				FasSigeSentenzaModel lFasSigeSentenza = new FasSigeSentenzaModel();
				lFasSigeSentenza.setSenIdSentenza(lTenore.getIdSentenza());
				lFasSigeSentenza.setFasIdFascicoloSige(lTenore.getFasIdFascicoloSige());
				Vector lFSSentenza = lFSSCtrl.ExRicercaFSSentenza(lFasSigeSentenza);
				if (lFSSentenza != null && lFSSentenza.size() > 0)
					lTenoreEsteso.setSentenzaSige((SentenzaSigeModel) lFSSentenza.get(0));

				lTenoriEstesi.add(lTenoreEsteso);
			}
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta Exception: " + e);
		}
		return lTenoriEstesi;
	}

	/**
	 * 02/12/2009 La funzione effettua la ricerca degli Oggetti legati ad una richiesta SIGE. Per ognuno degli
	 * Oggetti trovati viene restituito TenoreSigeEstesoModel, ovvero il model aggregato di Tenore, Sentenza e
	 * Reato.
	 *
	 * @param aIdRichiesta
	 * @return Vector di TenoreSigeEstesoModel
	 * @throws F3BException
	 */
	public Vector<TenoreSigeEstesoModel> ExRicercaTenoreEstesoByRichiesta(BigDecimal aIdRichiesta,
			Connection lConn) throws F3BException {

		Vector lTenoriSige = null;
		Vector lTenoriEstesi = new Vector();
		// Modifica del 23/11/2016 MEV_15_S4
		// Vettore contenente le Sentenze legate al Tenore Sige (Oggetto)
		Vector lSentenzeTenori = null;
		IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
		IFasSigeSentenza lFSSCtrl = SIGELookupRemote.getFasSigeSentenzaRemote(); // 20/01/2010

		try {
			// Ricerca dei Tenori Sige
			lTenoriSige = ExRicercaTenoreByRichiesta(aIdRichiesta, lConn);

			// Si itera sull'elenco dei Tenori per trovare Sentenza e Reato
			Iterator itx = lTenoriSige.iterator();
			// ticket per stampa copertina sige --> Ticket#20191029016 + Ticket#20191031016
			List<BigDecimal> idTen = new ArrayList<>();
			while (itx.hasNext()) {
				TenoreSigeModel lTenore = (TenoreSigeModel) itx.next();
				if (!idTen.contains(lTenore.getIdTenoreSige())) {
					SentenzaModel lSentenza = ricercaSentenzaByKey(lTenore.getIdSentenza());
					ReatoModel lReato = lReaCtrl.ExRicercaReatoByKey(lTenore.getIdReato());
					// Creazione del Model Aggregato ed aggiunta all'elenco Risultato
					TenoreSigeEstesoModel lTenoreEsteso = new TenoreSigeEstesoModel(lTenore, lSentenza,
							lReato);

					// 20/01/2010 Si legge FasSigeSentenzaModel.
					FasSigeSentenzaModel lFasSigeSentenza = new FasSigeSentenzaModel();
					lFasSigeSentenza.setSenIdSentenza(lTenore.getIdSentenza());
					lFasSigeSentenza.setFasIdFascicoloSige(lTenore.getFasIdFascicoloSige());
					Vector lFSSentenza = lFSSCtrl.ExRicercaFSSentenza(lFasSigeSentenza);
					if (lFSSentenza != null && lFSSentenza.size() > 0) {
						lTenoreEsteso.setSentenzaSige((SentenzaSigeModel) lFSSentenza.get(0));
					}

					// Modifica del 23/11/2016 MEV_15_S4
					// Per ogni Tenore Sige recupero le Sentenze ad esso associate
					Vector lSentenzeTenore = ExRicercaSentenzeByRichiestaAndIdTenoreSige(aIdRichiesta,
							lTenore.getIdTenoreSige(), lConn);
					Iterator its = lSentenzeTenore.iterator();
					lSentenzeTenori = new Vector();
					while (its.hasNext()) {
						TenoreSigeModel lTenoreSen = (TenoreSigeModel) its.next();
						SentenzaModel lSent = ricercaSentenzaByKey(lTenoreSen.getIdSentenza());
						// Escludo dal vettore la Sentenza "lSentenza" che è stata recuperata precedentemente
						if (lSent != null && lSentenza != null
								&& !lSent.getIdSentenza().equals(lSentenza.getIdSentenza())) {
							lSentenzeTenori.add(lSent);
						}
					}
					// aggiungo al Tenore le sentenze recuperate
					lTenoreEsteso.setSentenzaSigeVector(lSentenzeTenori);

					lTenoriEstesi.add(lTenoreEsteso);
				}

				idTen.add(lTenore.getIdTenoreSige());
			}
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta Exception: " + e);
		}
		return lTenoriEstesi;
	}

	/**
	 * La funzione effettua la ricerca degli Oggetti attivi. Fascicolo o ad una Richiesta o ad un
	 * Provvedimento in base a quale di questi id sia valorizzato nel TenoreModel passato. Per ognuno degli
	 * Oggetti trovati viene restituito TenoreSigeEstesoModel, ovvero il model aggregato di Tenore, Sentenza e
	 * Reato.
	 *
	 * @param aIdRichiesta
	 * @return Vector di TenoreSigeEstesoModel
	 * @throws F3BException
	 */
	public Vector<TenoreSigeEstesoModel> ExRicercaTenoriEstesiAttivi(TenoreSigeModel aTenore)
			throws F3BException {

		Vector<TenoreSigeModel> lTenoriSige = null;
		Vector<TenoreSigeEstesoModel> lTenoriEstesi = new Vector<>();
		IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
		IFasSigeSentenza lFSSCtrl = SIGELookupRemote.getFasSigeSentenzaRemote(); // 20/01/2010

		try {
			// Ricerca dei Tenori Sige
			lTenoriSige = ExRicercaTenoriAttivi(aTenore);

			// Si itera sull'elenco dei Tenori per trovare Sentenza e Reato
			Iterator itx = lTenoriSige.iterator();
			while (itx.hasNext()) {
				TenoreSigeModel lTenore = (TenoreSigeModel) itx.next();
				lTenore.decodifica();
				SentenzaModel lSentenza = ricercaSentenzaByKey(lTenore.getIdSentenza());
				ReatoModel lReato = lReaCtrl.ExRicercaReatoByKey(lTenore.getIdReato());

				// Creazione del Model Aggregato ed aggiunta all'elenco Risultato
				TenoreSigeEstesoModel lTenoreEsteso = new TenoreSigeEstesoModel(lTenore, lSentenza, lReato);

				// 20/01/2010 Si legge FasSigeSentenzaModel.
				FasSigeSentenzaModel lFasSigeSentenza = new FasSigeSentenzaModel();
				lFasSigeSentenza.setSenIdSentenza(lTenore.getIdSentenza());
				lFasSigeSentenza.setFasIdFascicoloSige(lTenore.getFasIdFascicoloSige());
				Vector lFSSentenza = lFSSCtrl.ExRicercaFSSentenza(lFasSigeSentenza);
				if (lFSSentenza != null && lFSSentenza.size() > 0)
					lTenoreEsteso.setSentenzaSige((SentenzaSigeModel) lFSSentenza.get(0));

				lTenoriEstesi.add(lTenoreEsteso);
			}
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoriEstesiAttivi Exception: " + e);
		}
		return lTenoriEstesi;
	}

	/**
	 * Ricerca la Sentenza dal suo ID.
	 *
	 * @param aSentenzaKey
	 * @return
	 * @throws F3BException
	 */
	public SentenzaModel ricercaSentenzaByKey(BigDecimal aSentenzaKey) throws F3BException {

		Connection lConn = null;
		SentenzaSqlDAO lSenDao = null;
		SentenzaModel lSen = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaSqlDAO(lConn);
			lSenDao.ricercaSentenzaBykey(aSentenzaKey);
			lSen = new SentenzaModel((SentenzaModel) lSenDao.getModelByKey());
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreSigeController.ricercaSentenzaByKey DAOException: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ricercaSentenzaByKey Exception: " + e);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}
		return lSen;
	}

	/**
	 * La funzione effettua l'inserimento di un Oggetto (TENORE_SIGE) riferito ad una Richiesta. Per
	 * effettuare l'inserimento viene richiamata la funzione ExInserisciOggetto(...) (vedi) cui viene passata
	 * la connessione oltre i dati di scambio.
	 *
	 * @param aTenori
	 * @throws F3BException
	 */
	public void ExInserisciOggettoXRichiesta(TenoreSigeModel[] aTenori) throws F3BException {

		Connection lConn = null;
		if (aTenori == null)
			throw new F3BException(
					"TenoreSigeController.ExInserisciOggettoXRichiesta: Tenore da inserire null !!");
		int lNum = aTenori.length;
		if (lNum > 0) {
			try {
				// Si ricava la connessione al DB
				lConn = getDBConnection();

				// Si richiama la funzione di inserimento
				ExInserisciOggetto(aTenori, lConn);

				commit(lConn);
			} catch (F3BException fe) {
				rollback(lConn);
				throw fe;
			} catch (Exception e) {
				rollback(lConn);
				throw new F3BException("TenoreSigeController.ExInserisciOggettoXRichiesta: " + e);
			} finally {
				cleanup(lConn);
			}
		} // endif
	}

	/**
	 * La funzione effettua la modifica degli Oggetti in TENORE_SIGE e nella tabella di relazione
	 * TENORE_SENTENZA_REATO tutte le associazioni Oggetto-Sentenza-Reato. I dati con i record da modificare
	 * sono contenuti nell'array di TenoreSigeModel scambiato come parametro di ingresso.
	 *
	 * @param aIdRichiestaSige
	 * @param aTenori
	 * @throws F3BException
	 */
	// 20190514 [SG]: aggiunto parametro di passaggio
	public void ExModificaOggettoXRichiesta(BigDecimal aIdRichiestaSige, TenoreSigeModel[] aTenori,
			String codContenutoOld) throws F3BException {

		if (aTenori == null)
			throw new F3BException(
					"TenoreSigeController.ExModificaOggettoXRichiesta: Tenore da modificare null !!");
		int lNum = aTenori.length;
		if (lNum > 0) {
			Vector lElencoTenoriSige = null;
			Connection lConn = null;
			TenoreSigeDAO lTenDao = null;
			TenoreSentenzaReatoDAO lTenSenReaDAO = null;
			// 20190514 [SG]: aggiunto dao
			TenoreSigeSqlDAO lTenSqlDao = null;

			try {
				// Si ricava la connessione al DB
				lConn = getDBConnection();

				lTenDao = new TenoreSigeDAO(lConn);
				lTenSenReaDAO = new TenoreSentenzaReatoDAO(lConn);
				// 20190514 [SG]: impostato dao
				lTenSqlDao = new TenoreSigeSqlDAO(lConn);

				// Cancellazione generale: effettua l'eliminazione dell'associazione
				// di tutti i Titoli Esecuti/Oggetti legati alla Richiesta Sige.
				if (aIdRichiestaSige != null) {
					// Recupero i Tenori legati alla richiesta Sige
					// lTenDao.selCondizioneIdRichiestaSige(aIdRichiestaSige);
					// lElencoTenoriSige = new Vector(lTenDao.getModels());
					// for (int i = 0; i < lElencoTenoriSige.size(); i++) {
					// TenoreSigeModel lTenore = (TenoreSigeModel) lElencoTenoriSige.get(i);
					// // Cancellazione dei record nella tabella TENORE_SENTENZA_REATO in relazione
					// // con il tenore da cancellare
					// lTenSenReaDAO.selCondizioneDelete(lTenore.getIdTenoreSige());
					// lTenSenReaDAO.delete();
					// lTenSenReaDAO.stop();
					// // Cancellazione del Tenore nella tabella TENORE_SIGE
					// lTenDao.selCondizioneDelete(lTenore.getIdTenoreSige());
					// lTenDao.delete();
					// lTenDao.stop();
					// }
					// 20190514 [SG]: cambiata gestione cancello solo singolo oggetto e non tutti
					lTenSqlDao.ricercaTenoriByRichiesta(aIdRichiestaSige, codContenutoOld);
					lElencoTenoriSige = new Vector(lTenSqlDao.getModels());
					for (int i = 0; i < lElencoTenoriSige.size(); i++) {
						// Cancellazione dei record nella tabella TENORE_SENTENZA_REATO in relazione
						// con il tenore da cancellare
						lTenSenReaDAO.selCondizioneDelete(
								((TenoreSigeModel) lElencoTenoriSige.get(i)).getIdTenoreSige());
						lTenSenReaDAO.delete();
						lTenSenReaDAO.stop();
						// Cancellazione del Tenore nella tabella TENORE_SIGE
						lTenDao.selCondizioneDelete(
								((TenoreSigeModel) lElencoTenoriSige.get(i)).getIdTenoreSige());
						lTenDao.delete();
						lTenDao.stop();
					}
				}

				// Si richiama la funzione di inserimento
				ExInserisciOggetto(aTenori, lConn);

				commit(lConn);
			} catch (F3BException fe) {
				rollback(lConn);
				throw fe;
			} catch (Exception e) {
				rollback(lConn);
				throw new F3BException("TenoreSigeController.ExModificaOggettoXRichiesta: " + e);
			} finally {
				cleanup(lTenDao);
				cleanup(lTenSenReaDAO);
				// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
				cleanup(lTenSqlDao);
				cleanup(lConn);
			}
		} // endif
	}

	/**
	 * La funzione effettua l'inserimento degli Oggetti in TENORE_SIGE e nella tabella di relazione
	 * TENORE_SENTENZA_REATO tutte le associazioni Oggetto-Sentenza-Reato. I dati con i record da inserire
	 * sono contenuti nell'array di TenoreSigeModel scambiato come parametro di ingresso.
	 *
	 * @param aTenori
	 * @param lConn
	 *            : connection
	 * @throws F3BException
	 */
	public void ExInserisciOggetto(TenoreSigeModel[] aTenori, Connection aConn) throws F3BException {

		TenoreSigeDAO lTenDao = null;
		TenoreSentenzaReatoDAO lTenSenReaDAO = null;
		if (aTenori == null)
			throw new F3BException("TenoreSigeController.ExInserisciOggetto: Tenore da inserire null !!");
		int lNum = aTenori.length;
		if (lNum > 0) {
			try {
				lTenDao = new TenoreSigeDAO(aConn);
				lTenSenReaDAO = new TenoreSentenzaReatoDAO(aConn);
				BigDecimal lKeyTenore = new BigDecimal(0);
				// Inserimento del Tenore in TENORE_SIGE e delle N relazioni in TENORE_SENTENZA_REATO
				for (int i = 0; i < lNum; i++) {
					// inserimento di un Oggetto in TENORE_SIGE
					if (aTenori[i].getFlagOggetto().equals("") || (aTenori[i].getFlagOggetto() != null
							&& aTenori[i].getFlagOggetto().equals("S"))) {
						aTenori[i].setDataFine(null); // 25/05/2010 Per ripristino Oggetti della Richiesta
														// SIGE.
						lTenDao.setDAOFromModel(aTenori[i]);
						lKeyTenore = lTenDao.insert();
						lTenDao.stop();
					}

					// inserimento nella tabella TENORE_SENTENZA_REATO
					aTenori[i].setIdTenoreSige(lKeyTenore);
					lTenSenReaDAO.setDAOFromTenoreModel(aTenori[i]);
					lTenSenReaDAO.insert();
					lTenSenReaDAO.stop();
				}
			} catch (DAOException ex) {
				throw new F3BException("TenoreSigeController.ExInserisciOggetto: " + ex);
			} catch (Exception sqe) {
				throw new F3BException("TenoreSigeController.ExInserisciOggetto: " + sqe);
			} finally {
				cleanup(lTenDao);
				cleanup(lTenSenReaDAO);
			}
		} // endif
	}

	// @emma 09072018 intervento post COLLAUDO 11.2 (aggiungo idProvv in input)
	public void ExInserisciEsitiOggetto(TenoreSigeModel aTenore, TenoreSentenzaReatoModel[] aTenori,
			DatiProvvedimentoSigeModel[] aDatiProv, AnnotazioneManualeModel aAnnotazioneManuale,
			Vector aListaRichieste, BigDecimal aIdProvvedimento) throws F3BException {

		TenoreSigeDAO lTenDao = null;
		TenoreSentenzaReatoDAO lTenSenReaDAO = null;
		DatiProvvedimentoSigeDAO lDatiProvDao = null;
		AnnotazioneManualeDAO lAnnDao = null;
		EventoDAO lEventoDAO = null;
		Connection lConn = null;

		try {
			// Si ricava la connessione al DB
			lConn = getDBConnection();

			// Inizializzazione
			lTenDao = new TenoreSigeDAO(lConn);
			lTenSenReaDAO = new TenoreSentenzaReatoDAO(lConn);
			lDatiProvDao = new DatiProvvedimentoSigeDAO(lConn);

			// Aggiornamento del TENORE_SIGE
			lTenDao.setDAOFromModelForUpdateEsito(aTenore);
			lTenDao.update();
			lTenDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID TENORE_SIGE AGGIORNATO->" + aTenore.getIdTenoreSige());

			// Eventuale aggiornamento esiti in TENORE_SENTENZA_REATO
			if (aTenori != null) {
				// @emma 23072018 intervento post COLLAUDO 11.2 inizio intervento post-collaudo
				ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
				/* Vector<TenoreSigeModel> lTenoriEstesi = */lTenCtrl
						.ExRicercaTenoreByProvvedimento(aIdProvvedimento, lConn);
				// 20190521: [EC] gestione esiti distinti per reato/sentenza e per oggetto
				// for (int a = 0; a < lTenoriEstesi.size(); a++) {
				// TenoreSigeModel myTen = (TenoreSigeModel) lTenoriEstesi.get(a);
				// myTen.setCodEsitoSige("0000");
				// lTenDao.setDAOFromModelForUpdateEsito(myTen);
				// lTenDao.update();
				// lTenDao.stop();
				// }
				// fine intervento post-collaudo

				for (int i = 0; i < aTenori.length; i++) {
					lTenSenReaDAO.setDAOFromModelForUpdateEsiti(aTenori[i]);
					lTenSenReaDAO.update();
					lTenSenReaDAO.stop();
				}
			}

			// Cancellazione dei dati preesistenti in DATI_PROVVEDIMENTO_SIGE
			// (caso di modifica).
			lDatiProvDao.setCondizioneDelete(aTenore.getIdTenoreSige());
			lDatiProvDao.delete();
			lDatiProvDao.stop();

			// Eventuale inserimento dati in DATI_PROVVEDIMENTO_SIGE
			if (aDatiProv != null)
				for (int i = 0; i < aDatiProv.length; i++) {
					lDatiProvDao.setDAOFromModel(aDatiProv[i]);
					lDatiProvDao.insert();
					lDatiProvDao.stop();
				}

			// Caso Indulto/Amnistia
			if (aAnnotazioneManuale != null) {
				// Inizializzazione DAO
				lAnnDao = new AnnotazioneManualeDAO(lConn);

				// Cancellazione dell'Annotazione Manuale presistente
				// (caso di modifica).
				// lAnnDao.setCondizioneLinkEvento(aAnnotazioneManuale.getEveIdEvento());
				// lAnnDao.setCondizioneSenIdSentenza(aAnnotazioneManuale.getSenIdSentenza());
				lAnnDao.setCondizioneSenIdSentenzaTenIdTenore(aAnnotazioneManuale.getSenIdSentenza(),
						aAnnotazioneManuale.getTenIdTenoreSige());
				lAnnDao.delete();
				lAnnDao.stop();

				// Inserimento Annotazione Manuale riferita all'Ordinanza
				/*
				 * MEV9 Se è uno dei seguenti codici, Metto sempre R ("Rigetta") e metto sempre il valore dei
				 * quantun positivo ("+") , per avere un corretto calcolo della pena residua anche se
				 * l'operatore sbaglia a mettere i quantum
				 */
				if (aTenore.getCodEsitoSige().equals("0142") || aTenore.getCodEsitoSige().equals("0143")
						|| aTenore.getCodEsitoSige().equals("0146")
						|| aTenore.getCodEsitoSige().equals("0147")
						|| aTenore.getCodEsitoSige().equals("0148")) {
					aAnnotazioneManuale.setFlagConforme("R");
					aAnnotazioneManuale.setFlagPiuMeno("+");
				}

				if (aTenore.getCodEsitoSige().equals("0145")) {
					aAnnotazioneManuale.setFlagConforme("I");
					aAnnotazioneManuale.setFlagPiuMeno("+");
				}
				// End MEV9
				lAnnDao.setDAOFromModel(aAnnotazioneManuale);
				BigDecimal lKey = null;
				lKey = lAnnDao.insert();
				aAnnotazioneManuale.setIdAnnotazioneManuale(lKey);
				lAnnDao.stop();
				cleanup(lAnnDao);

				// MEV9 - NON VIENE più eseguita l'aggiornamento delle richieste con il legame all'ordinanza

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"NON VIENE più eseguita la modifica delle richieste <<Aggiorno le richieste in n.ro di : "
								+ aListaRichieste.size());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"NON VIENE più eseguito il legame tra le AnnotazioniManuali della Richiesta con l'ordinanza");

				// Aggiornamento di eventuali Richieste del PM al GE
				// che vengono legate all'Annotazione inserita.
				// Luigi 15-10-2010
				/*
				 * if (aListaRichieste != null) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				 * istanza siesLogger al posto di LogF3B.getLogger()
				 * siesLogger.debug("Aggiorno le richieste in n.ro di : "+aListaRichieste.size()); for (int
				 * i=0; i<aListaRichieste.size();i++) { AnnotazioneManualeModel lAnnModRichiesta =
				 * (AnnotazioneManualeModel) aListaRichieste.elementAt(i); // [FT] - 03/08/2016 - MAC_LOG -
				 * Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				 * siesLogger.debug("");
				 * lAnnDao.setCondizioneUpdate(lAnnModRichiesta.getIdAnnotazioneManuale());
				 * lAnnDao.setAnnoIdAnnotazioneManuale(aAnnotazioneManuale.getIdAnnotazioneManuale());
				 * lAnnDao.setDataAggiornamento(aAnnotazioneManuale.getDataInserimento());
				 * lAnnDao.setCodUfficioAggiornamento(aAnnotazioneManuale.getCodUfficioInserimento());
				 * lAnnDao.setCodOperatoreAggiornamento(aAnnotazioneManuale.getCodOperatoreInserimento());
				 * lAnnDao.update(); lAnnDao.stop(); } }
				 */
				// Aggiornamento del COD_MOTIVO dell'EVENTO con il valore
				// richiesto da SIEP: 0284
				lEventoDAO = new EventoDAO(lConn);
				lEventoDAO.setCodMotivo("0284");
				lEventoDAO.setCodOperatoreAggiornamento(aAnnotazioneManuale.getCodOperatoreInserimento());
				lEventoDAO.setDataAggiornamento(aAnnotazioneManuale.getDataInserimento());
				lEventoDAO.setCodUfficioAggiornamento(aAnnotazioneManuale.getCodUfficioInserimento());
				// Si inserisce nell'Evento il riferimento all'eventuale Fascicolo SIEP
				if (aAnnotazioneManuale.getFasSieIdFascicoloSiep() != null)
					lEventoDAO.setFasSieIdFascicoloSiep(aAnnotazioneManuale.getFasSieIdFascicoloSiep());
				// MEV9
				if (aTenore.getCodEsitoSige().equals("0142") || aTenore.getCodEsitoSige().equals("0143")
						|| aTenore.getCodEsitoSige().equals("0146")
						|| aTenore.getCodEsitoSige().equals("0147")
						|| aTenore.getCodEsitoSige().equals("0148")) {
					lEventoDAO.setCodEsito("R");
				}

				if (aTenore.getCodEsitoSige().equals("0145")) {
					lEventoDAO.setCodEsito("I");
				}
				// End MEV9
				lEventoDAO.selCondizioneUpdate(aAnnotazioneManuale.getEveIdEvento());
				lEventoDAO.update();
				lEventoDAO.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TenoreSigeController.ExInserisciEsitiOggetto: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("TenoreSigeController.ExInserisciEsitiOggetto: " + sqe);
		} finally {
			cleanup(lTenDao);
			cleanup(lDatiProvDao);
			cleanup(lTenSenReaDAO);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lAnnDao);
			cleanup(lEventoDAO);
			cleanup(lConn);
		}
	}

	/**
	 * La funzione effettua l'inserimento di un elenco di oggetti. Se aIdFascicoloSige non è null vengono
	 * storicizzati eventuali oggetti già presenti nella tabella TENORE_SIGE e legati a quel Fascicolo SIGE.
	 *
	 * @param aTenori
	 * @param aIdFascicoloSige
	 * @param aConn
	 * @throws F3BException
	 */
	public void ExInserisciOggetti(Vector aTenori, BigDecimal aIdFascicoloSige, Connection aConn)
			throws F3BException {

		TenoreSigeDAO lTenDao = null;
		TenoreSigeModel lTenore = null;

		if (aTenori == null)
			throw new F3BException("TenoreSigeController.ExInserisciOggetto: Tenore da inserire null !!");
		int lNum = aTenori.size();
		if (lNum > 0) {
			try {
				lTenDao = new TenoreSigeDAO(aConn);

				lTenore = (TenoreSigeModel) aTenori.get(0);

				// Storicizzazione eventuali oggetti collegati al Fascicolo SIGE
				if (aIdFascicoloSige != null) {
					lTenDao.setDAOPerStoricizzare(lTenore);
					lTenDao.setCondizioneFasSigePerStoricizzazione(aIdFascicoloSige);
					lTenDao.update();
					lTenDao.stop();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Effettuata storicizzazione oggetti");
				}

				// 20190508 [SG]: invece che storicizzare cancello e reinserisco
				// Storicizzazione eventuali oggetti collegati al Fascicolo SIGE
				// if (aIdFascicoloSige != null) {
				// // lTenDao.setDAOPerStoricizzare(lTenore);
				// // lTenDao.setCondizioneFasSigePerStoricizzazione(aIdFascicoloSige);
				// // lTenDao.update();
				// lTenSigeDao = new TenoreSigeSqlDAO(aConn);
				// lTenore = new TenoreSigeModel();
				// lTenore.setFasIdFascicoloSige(aIdFascicoloSige);
				// lTenSigeDao.ricercaTenoriSige(lTenore);
				// Vector lElencoTenoriSige = new Vector(lTenSigeDao.getModels());
				// lTenSenReaDAO = new TenoreSentenzaReatoDAO(aConn);
				// for (int i = 0; i < lElencoTenoriSige.size(); i++) {
				// TenoreSigeModel tsm = (TenoreSigeModel) lElencoTenoriSige.get(i);
				// // Cancellazione dei record nella tabella TENORE_SENTENZA_REATO in relazione
				// // con il tenore da cancellare
				// lTenSenReaDAO.selCondizioneDelete(tsm.getIdTenoreSige());
				// lTenSenReaDAO.delete();
				// lTenSenReaDAO.stop();
				// // Cancellazione del Tenore nella tabella TENORE_SIGE
				// lTenDao.selCondizioneDelete(tsm.getIdTenoreSige());
				// lTenDao.delete();
				// lTenDao.stop();
				// }
				// // for (int i = 0; i < lNum; i++) {
				// // lTenore = (TenoreSigeModel) aTenori.get(i);
				// // lTenDao.selCondizioneFasSige(aIdFascicoloSige);
				// // lTenDao.delete();
				// // lTenDao.stop();
				// cleanup(lTenDao);
				// cleanup(lTenSigeDao);
				// cleanup(lTenSenReaDAO);
				// // }
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// // LogF3B.getLogger()
				// // siesLogger.debug("Effettuata storicizzazione oggetti");
				// }

				// 20190508 [SG]: controllo se esiste unico elemento nuovo senza idRichiestaSige
				// if (lNum == 1 && ((TenoreSigeModel) aTenori.get(0)).getRicSigIdRichiestaSige() == null) {
				// FascicoloSigeSqlDAO lFasSqlDao = null;
				// lFasSqlDao = new FascicoloSigeSqlDAO(aConn);
				// lFasSqlDao.ricercaFascicoloSigeByKey(aIdFascicoloSige);
				// FascicoloSigeModel fsm = (FascicoloSigeModel) lFasSqlDao.getModelByKey();
				// if (fsm != null)
				// ((TenoreSigeModel) aTenori.get(0))
				// .setRicSigIdRichiestaSige(fsm.getRicIdRichiestaSige());
				// }

				Vector lElencoTenori = new Vector(aTenori);

				// Inserimento dell'oggetto
				ExInserisciOggetto((TenoreSigeModel[]) lElencoTenori.toArray(new TenoreSigeModel[0]), aConn);
			} catch (F3BException fe) {
				throw fe;
			} catch (Exception sqe) {
				throw new F3BException("TenoreSigeController.ExInserisciOggetti: " + sqe);
			} finally {
				// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
				cleanup(lTenDao);
			}
		} // endif
	}

	/**
	 * La funzione effettua la cancellazione di tutti gli Oggetti, legati alla Richiesta SIGE,e tutti i Titoli
	 * Esecutivi ad esso associati se è valorizzato il parametro aIdRichiestaSige (cancellazione generale)
	 * oppure, se viene passato il parametro aIdSentenza, la cancellazione dell'Oggetto con il relativo Titolo
	 * Esecutivo. In questo secondo caso, se non esistono altri Titoli Esecutivi associati all'Oggetto si
	 * procede alla cancellazione dello stesso. Prima di poter cancellare i record sulla tabella TENORE_SIGE
	 * devono essere prima cancellati i record di relazione nella tabella TENORE_SENTENZA_REATO
	 *
	 * @param aIdRichiestaSige
	 * @param aIdTenoreSige
	 * @param aIdSentenza
	 * @throws F3BException
	 */
	public void ExCancellaTenoreSige(BigDecimal aIdRichiestaSige, BigDecimal aIdTenoreSige,
			BigDecimal aIdSentenza) throws F3BException {

		Connection lConn = null;
		TenoreSigeDAO lTenDao = null;
		TenoreSentenzaReatoDAO lTenSenReaDAO = null;
		Vector lElencoTenoriSige = null;

		try {

			lConn = getDBConnection();
			lTenDao = new TenoreSigeDAO(lConn);
			lTenSenReaDAO = new TenoreSentenzaReatoDAO(lConn);

			// Cancellazione generale: effettua l'eliminazione dell'associazione
			// di tutti i Titoli Esecuti/Oggetti legati alla Richiesta Sige.
			if (aIdRichiestaSige != null) {
				// Recupero i Tenori legati alla richiesta Sige
				lTenDao.selCondizioneIdRichiestaSige(aIdRichiestaSige);
				lElencoTenoriSige = new Vector(lTenDao.getModels());
				for (int i = 0; i < lElencoTenoriSige.size(); i++) {
					TenoreSigeModel lTenore = (TenoreSigeModel) lElencoTenoriSige.get(i);
					// Cancellazione dei record nella tabella TENORE_SENTENZA_REATO in relazione
					// con il tenore da cancellare
					lTenSenReaDAO.selCondizioneDelete(lTenore.getIdTenoreSige());
					lTenSenReaDAO.delete();
					lTenSenReaDAO.stop();
					// Cancellazione del Tenore nella tabella TENORE_SIGE
					lTenDao.selCondizioneDelete(lTenore.getIdTenoreSige());
					lTenDao.delete();
					lTenDao.stop();
				}
			} else {
				// Cancellazione per singolo Titolo Esecutivo
				lTenSenReaDAO.selCondizioneDeleteTitoloEsecutivo(aIdTenoreSige, aIdSentenza);
				lTenSenReaDAO.delete();
				lTenSenReaDAO.stop();

				// prima di procedere alla cancellazione dell'Oggetto sulla tabella TENORE_SIGE
				// bisogna verificare che non siano presenti altri Titoli Esecutivi ad esso associati
				BigDecimal countRisultati = getCountSentenzeByIdTenoreSige(aIdTenoreSige);
				if (countRisultati.intValue() == 1) {
					// Cancellazione del record TENORE_SIGE individuato dalla chiave aIdTenoreSige
					lTenDao.selCondizioneDelete(aIdTenoreSige);
					lTenDao.delete();
					lTenDao.stop();
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("TenoreSigeController.ExCancellaTenoreSige: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("TenoreSigeController.ExCancellaTenoreSige : " + e);
		} finally {
			cleanup(lTenDao);
			cleanup(lTenSenReaDAO);
			cleanup(lConn);
		}
	}

	/**
	 * Controlla se esiste una Sentenza legata ai Tenori per la quale non è stata definita la DATA DI
	 * IRREVOCABILITA. Se questo accade viene lanciata una eccezione.
	 *
	 * @param aTenori
	 * @throws F3BException
	 */
	public void ExControlloDataIrrevocabilita(Vector aTenori) throws F3BException {

		Connection lConn = null;
		FasSigeSentenzaDAO lFasSigeSenDAO = null;
		TenoreSigeModel lTenore = null;
		SentenzaSigeModel SenSige = null;
		if (aTenori != null && aTenori.size() > 0) {
			try {
				// Inizializzazione
				lConn = getDBConnection();
				lFasSigeSenDAO = new FasSigeSentenzaDAO(lConn);
				SenSige = new SentenzaSigeModel();

				// Iterazione su elenco tenori
				Iterator itx = aTenori.iterator();
				while (itx.hasNext()) {
					lTenore = (TenoreSigeModel) itx.next();

					// Valorizzazione dei parametri di ricerca
					SenSige.setFasIdFascicoloSige(lTenore.getFasIdFascicoloSige());
					SenSige.setIdSentenza(lTenore.getIdSentenza());
					lFasSigeSenDAO.setCondizioneDataIrrevocabilitaNull(SenSige);
					if (lFasSigeSenDAO.getModelByKey() != null)
						throw new F3BException(F3BException.USER_MESSAGE,
								"Data di irrevocabilità non definita per tutte le sentenze!");
				}
			} catch (F3BException fe) {
				throw fe;
			} catch (Exception sqe) {
				throw new F3BException("TenoreSigeController.ExControlloDataIrrevocabilita: " + sqe);
			} finally {
				// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
				cleanup(lConn);
				cleanup(lFasSigeSenDAO);
			}
		} // endif
	}

	/**
	 * Ricerca degli Oggetti del Provvedimento Sige.
	 *
	 * @param aIdProvvedimento
	 * @param lConn
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaTenoreByProvvedimento(BigDecimal aIdProvvedimento, Connection aConn)
			throws F3BException {

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		Connection lConn = null;
		Vector lTenoriSige = null;
		TenoreSigeSqlDAO lTenDao = null;

		try {
			if (aConn != null)
				lConn = aConn;
			else
				lConn = getDBConnection();
			lTenDao = new TenoreSigeSqlDAO(lConn);
			lTenDao.ricercaTenoriByProvvedimento(aIdProvvedimento);
			lTenoriSige = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TenoreSigeController.ExRicercaTenoreByProvvedimento DAOException: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByProvvedimento Exception: " + e);
		} finally {
			cleanup(lTenDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			if (aConn == null)
				cleanup(lConn);
		}
		return lTenoriSige;
	}

	/**
	 * La funzione effettua l' aggiornamento dei tenori SIGE collegati ad un provvedimento che si sta
	 * annullando.
	 *
	 * @param lProSige
	 * @param lConn
	 * @throws F3BException
	 */
	public boolean ExAggiornaTenoriPerAnnullamento(ProvvedimentoSigeModel lProSige, Connection aConn)
			throws F3BException {

		TenoreSigeDAO lTenDao = null;
		boolean lDataFineNull = false;

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		Connection lConn = null;

		try {
			if (aConn != null)
				lConn = aConn;
			else
				lConn = getDBConnection();

			// Inizializzazione
			lTenDao = new TenoreSigeDAO(lConn);

			// 13/01/2011 Se si sta cancellando un provvedimento con TENORE_SIGE valido/i (quindi con
			// DATA_FINE = null)
			// occorrerà operare il ripristino dei tenori del provvedimento precedente. A tale scopo bisogna
			// leggere un TENORE_SIGE prima di cancellarlo/i.
			lTenDao.selCondizioneIdProvvedimento(lProSige.getIdProvvedimentoSige());
			lTenDao.start();
			if (lTenDao.next()) {
				TenoreSigeModel lTenMod = (TenoreSigeModel) lTenDao.getModel();
				if (lTenMod != null && lTenMod.getIdTenoreSige() != null && lTenMod.getDataFine() == null)
					lDataFineNull = true;
			}
			lTenDao.stop();

			// 04/01/2011 lTenDao.setCodEsitoSige(null);
			lTenDao.setDataFine(lProSige.getDataAggiornamento());
			lTenDao.setCodOperatoreAggiornamento(lProSige.getCodOperatoreInserimento());
			lTenDao.setCodUfficioAggiornamento(lProSige.getCodUfficioInserimento());
			lTenDao.setDataAggiornamento(lProSige.getDataInserimento());

			// Aggiornamento dei TENORE_SIGE individuato dalla chiave del Provvedimento
			lTenDao.selCondizioneIdProvvedimento(lProSige.getIdProvvedimentoSige());

			lTenDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("TenoreSigeController.ExAggiornaTenoriPerAnnullamento: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("TenoreSigeController.ExAggiornaTenoriPerAnnullamento : " + e);
		} finally {
			cleanup(lTenDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			if (aConn == null)
				cleanup(lConn);
		}
		return lDataFineNull;
	}

	/**
	 * Ricerca il numero di Sentenze associate ad un Oggetto (Tenore Sige)
	 *
	 * @param aIdTenoreSige
	 * @return BigDecimal
	 * @throws F3BException
	 */
	public BigDecimal getCountSentenzeByIdTenoreSige(BigDecimal aIdTenoreSige) throws F3BException {

		Connection lConn = null;
		TenoreSigeSqlDAO lTenoreSigeDao = null;
		BigDecimal lCount = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lTenoreSigeDao = new TenoreSigeSqlDAO(lConn);
			lTenoreSigeDao.getCountSentenzeByIdTenoreSige(aIdTenoreSige);
			lTenoreSigeDao.start();
			lTenoreSigeDao.next();
			lCount = lTenoreSigeDao.getBigDecimal("HowManyRecords");
			lTenoreSigeDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TenoreSigeController.ExGetNumSentenzeByIdTenoreSige DAOException: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExGetNumSentenzeByIdTenoreSige Exception: " + e);
		} finally {
			cleanup(lTenoreSigeDao);
			cleanup(lConn);
		}
		return lCount;
	}

	@Override
	public Vector<TenoreSigeEstesoModel> ExRicercaTenoriEstesiByIdProvvedimento(BigDecimal idProvvedimento)
			throws F3BException {

		Vector<TenoreSigeEstesoModel> lTenoriEstesi = new Vector<>();

		try {
			IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
			IFasSigeSentenza lFSSCtrl = SIGELookupRemote.getFasSigeSentenzaRemote(); // 20/01/2010
			// Ricerca dei Tenori Sige
			Vector<TenoreSigeModel> lTenoriSige = ExRicercaTenoreByIdProvvedimento(idProvvedimento);

			// 20190508 [SG]: aggiungo tenori legati al procedimento
			// BigDecimal bd = null;
			// if (lTenoriSige != null && lTenoriSige.size() > 0
			// && lTenoriSige.firstElement().getRicSigIdRichiestaSige() != null)
			// bd = lTenoriSige.firstElement().getRicSigIdRichiestaSige();
			// Vector<BigDecimal> vbd = new Vector<BigDecimal>();
			// for (TenoreSigeModel tsm : lTenoriSige)
			// vbd.add(tsm.getIdTenoreSige());
			// Vector<TenoreSigeModel> v = null;
			// if (bd != null)
			// v = ExRicercaTenoreByRichiesta(bd);
			// if (v != null) {
			// for (TenoreSigeModel tsm : v) {
			// if (!vbd.contains(tsm.getIdTenoreSige()))
			// lTenoriSige.add(tsm);
			// }
			// }

			for (TenoreSigeModel lTenore : lTenoriSige) {
				SentenzaModel lSentenza = ricercaSentenzaByKey(lTenore.getIdSentenza());
				ReatoModel lReato = lReaCtrl.ExRicercaReatoByKey(lTenore.getIdReato());
				// Creazione del Model Aggregato ed aggiunta all'elenco Risultato
				TenoreSigeEstesoModel lTenoreEsteso = new TenoreSigeEstesoModel(lTenore, lSentenza, lReato);
				// 20/01/2010 Si legge SentenzaSigeModel.
				FasSigeSentenzaModel lFasSigeSentenza = new FasSigeSentenzaModel();
				lFasSigeSentenza.setSenIdSentenza(lTenore.getIdSentenza());
				lFasSigeSentenza.setFasIdFascicoloSige(lTenore.getFasIdFascicoloSige());
				Vector<SentenzaSigeModel> lFSSentenza = lFSSCtrl.ExRicercaFSSentenza(lFasSigeSentenza);
				if (lFSSentenza != null && lFSSentenza.size() > 0)
					lTenoreEsteso.setSentenzaSige(lFSSentenza.get(0));
				lTenoriEstesi.add(lTenoreEsteso);
			}
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta Exception: " + e);
		}
		return lTenoriEstesi;
	}

	/**
	 * Ricerca degli Oggetti della Richiesta_Sige.
	 *
	 * @param aIdRichiesta
	 * @return
	 * @throws F3BException
	 */
	public Vector<TenoreSigeModel> ExRicercaTenoreByIdProvvedimento(BigDecimal idProvvedimento)
			throws F3BException {

		Connection lConn = null;
		Vector<TenoreSigeModel> lTenoriSige = null;
		TenoreSigeSqlDAO lTenDao = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSigeSqlDAO(lConn);
			lTenDao.ricercaTenoriByProvvedimento(idProvvedimento);
			lTenoriSige = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta DAOException: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta Exception: " + e);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenoriSige;
	}

	@Override
	public Vector<TenoreSigeEstesoModel> ExRicercaTenoriByCodOggettoSigeAndIdProvvedimento(String codOggetto,
			BigDecimal idProvvedimento) throws F3BException {

		Connection lConn = null;
		Vector<TenoreSigeModel> lTenoriSige = null;
		TenoreSigeSqlDAO lTenDao = null;
		Vector<TenoreSigeEstesoModel> lTenoriEstesi = new Vector<>();

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSigeSqlDAO(lConn);
			lTenDao.ricercaTenoriByProvvedimento(idProvvedimento);
			lTenDao.ricercaTenoriByProvvedimentoAndCodOggetto(codOggetto, idProvvedimento);
			lTenoriSige = new Vector(lTenDao.getModels());

			IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
			IFasSigeSentenza lFSSCtrl = SIGELookupRemote.getFasSigeSentenzaRemote(); // 20/01/2010

			for (TenoreSigeModel lTenore : lTenoriSige) {
				SentenzaModel lSentenza = ricercaSentenzaByKey(lTenore.getIdSentenza());
				ReatoModel lReato = lReaCtrl.ExRicercaReatoByKey(lTenore.getIdReato());
				// Creazione del Model Aggregato ed aggiunta all'elenco Risultato
				TenoreSigeEstesoModel lTenoreEsteso = new TenoreSigeEstesoModel(lTenore, lSentenza, lReato);

				// 20/01/2010 Si legge SentenzaSigeModel.
				FasSigeSentenzaModel lFasSigeSentenza = new FasSigeSentenzaModel();
				lFasSigeSentenza.setSenIdSentenza(lTenore.getIdSentenza());
				lFasSigeSentenza.setFasIdFascicoloSige(lTenore.getFasIdFascicoloSige());
				Vector<SentenzaSigeModel> lFSSentenza = lFSSCtrl.ExRicercaFSSentenza(lFasSigeSentenza);
				if (lFSSentenza != null && lFSSentenza.size() > 0)
					lTenoreEsteso.setSentenzaSige(lFSSentenza.get(0));

				lTenoriEstesi.add(lTenoreEsteso);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TenoreSigeController.ExRicercaTenoriByCodOggettoSigeAndIdProvvedimento DAOException: "
							+ daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"TenoreSigeController.ExRicercaTenoriByCodOggettoSigeAndIdProvvedimento Exception: " + e);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenoriEstesi;
	}

	/**
	 * Ricerca le Sentenze legate al Tenore Sige.
	 *
	 * @param aIdRichiesta
	 * @param aIdTenoreSige
	 * @param lConn
	 * @return vettore contenente le sentenze legate al Tenore Sige
	 * @throws F3BException
	 */
	public Vector ExRicercaSentenzeByRichiestaAndIdTenoreSige(BigDecimal aIdRichiesta,
			BigDecimal aIdTenoreSige, Connection aConn) throws F3BException {

		Vector lSentenzeTenoriSige = null;
		TenoreSigeSqlDAO lTenDao = null;

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		Connection lConn = null;

		try {
			if (aConn != null)
				lConn = aConn;
			else
				lConn = getDBConnection();
			lTenDao = new TenoreSigeSqlDAO(lConn);
			lTenDao.ricercaSentenzeByRichiestaAndIdTenoreSige(aIdRichiesta, aIdTenoreSige);
			lSentenzeTenoriSige = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TenoreSigeController.ExRicercaSentenzeByRichiestaAndIdTenoreSige DAOException: "
							+ daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"TenoreSigeController.ExRicercaSentenzeByRichiestaAndIdTenoreSige Exception: " + e);
		} finally {
			cleanup(lTenDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			if (aConn == null)
				cleanup(lConn);
		}
		return lSentenzeTenoriSige;
	}

	/**
	 * La funzione effettua la ricerca degli Oggetti legati ad una richiesta SIGE. Per ognuno degli Oggetti
	 * trovati viene restituito TenoreSigeEstesoModel, ovvero il model aggregato di Tenore, Sentenza e Reato.
	 *
	 * @param aIdRichiesta
	 * @return Vector di TenoreSigeEstesoModel
	 * @throws F3BException
	 */
	public Vector<TenoreSigeEstesoModel> ExRicercaTenoreEstesoByRichiesta(BigDecimal aIdRichiesta,
			String codContenuto) throws F3BException {

		Vector<TenoreSigeModel> lTenoriSige = null;
		Vector<TenoreSigeEstesoModel> lTenoriEstesi = new Vector<>();

		try {
			IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
			IFasSigeSentenza lFSSCtrl = SIGELookupRemote.getFasSigeSentenzaRemote(); // 20/01/2010
			// Ricerca dei Tenori Sige
			lTenoriSige = ExRicercaTenoreByRichiesta(aIdRichiesta, codContenuto);

			// Si itera sull'elenco dei Tenori per trovare Sentenza e Reato
			Iterator itx = lTenoriSige.iterator();
			while (itx.hasNext()) {
				TenoreSigeModel lTenore = (TenoreSigeModel) itx.next();
				SentenzaModel lSentenza = ricercaSentenzaByKey(lTenore.getIdSentenza());
				ReatoModel lReato = lReaCtrl.ExRicercaReatoByKey(lTenore.getIdReato());
				// Creazione del Model Aggregato ed aggiunta all'elenco Risultato
				TenoreSigeEstesoModel lTenoreEsteso = new TenoreSigeEstesoModel(lTenore, lSentenza, lReato);

				// 20/01/2010 Si legge SentenzaSigeModel.
				FasSigeSentenzaModel lFasSigeSentenza = new FasSigeSentenzaModel();
				lFasSigeSentenza.setSenIdSentenza(lTenore.getIdSentenza());
				lFasSigeSentenza.setFasIdFascicoloSige(lTenore.getFasIdFascicoloSige());
				Vector lFSSentenza = lFSSCtrl.ExRicercaFSSentenza(lFasSigeSentenza);
				if (lFSSentenza != null && lFSSentenza.size() > 0)
					lTenoreEsteso.setSentenzaSige((SentenzaSigeModel) lFSSentenza.get(0));

				lTenoriEstesi.add(lTenoreEsteso);
			}
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta Exception: " + e);
		}
		return lTenoriEstesi;
	}

	/**
	 * Ricerca degli Oggetti della Richiesta_Sige.
	 *
	 * @param aIdRichiesta
	 * @return
	 * @throws F3BException
	 */
	public Vector<TenoreSigeModel> ExRicercaTenoreByRichiesta(BigDecimal aIdRichiesta, String codContenuto)
			throws F3BException {

		Connection lConn = null;
		Vector<TenoreSigeModel> lTenoriSige = null;
		TenoreSigeSqlDAO lTenDao = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSigeSqlDAO(lConn);
			lTenDao.ricercaTenoriByRichiesta(aIdRichiesta, codContenuto);
			lTenoriSige = new Vector<TenoreSigeModel>(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta DAOException: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("TenoreSigeController.ExRicercaTenoreByRichiesta Exception: " + e);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenoriSige;
	}

}