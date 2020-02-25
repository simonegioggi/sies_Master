package siap.sige.magistrato.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sige.SIGEException;
import siap.sige.magistrato.dao.MagistratoDAO;
import siap.sige.magistrato.dao.MagistratoSqlDAO;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratosezione.dao.MagistratoSezioneDAO;
import siap.sige.magistratosezione.dao.MagistratoSezioneSqlDAO;
import siap.sige.magistratosezione.model.MagistratoSezioneModel;
import siap.sige.udienza.dao.UdienzaSigeSqlDAO;
import f3b.dao.DAOException;
import f3b.model.DecodeModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MagistratoController
 * </p>
 * <p>
 * Description: Classe Controller per Magistrato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MagistratoController extends SiapController implements IMagistrato {

	/**
	 * Esegue inserimento del magistrato in ambito SIGE, inserendo le relazioni con le sezioni di
	 * appartenenza.
	 * 
	 * @param aMagistrato
	 *            MagistratoModel opportunamente popolato
	 * @return
	 * @throws F3BException
	 *             propaga errori di eccezione
	 */
	public MagistratoModel ExInserisciMagistrato(MagistratoModel aMagistrato) throws F3BException {
		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		MagistratoModel lMagMod = null;
		MagistratoSezioneDAO lMagSezDao = null;

		try {
			lConn = getDBConnection();
			lMagMod = new MagistratoModel(aMagistrato);
			lMagDao = new MagistratoDAO(lConn);
			lMagDao.setDAOFromModel(aMagistrato);

			BigDecimal lKey = null;
			lKey = lMagDao.insert();

			//
			// Esegue inserimento delle relazioni alle sezioni.
			//
			lMagSezDao = new MagistratoSezioneDAO(lConn);
			if (aMagistrato.getMagistratoSezioni() != null) {
				int lSize = aMagistrato.getMagistratoSezioni().length;
				for (int i = 0; i < lSize; i++) {
					lMagSezDao.setDAOFromModel(aMagistrato.getMagistratoSezioni()[i]);
					lMagSezDao.insert();
					lMagSezDao.stop();
				}
			}

			commit(lConn);

			if (lKey == null)
				lMagMod.setCodMagistrato(aMagistrato.getCodMagistrato());
			else
				lMagMod.setCodMagistrato(lKey.toString());
		} catch (DAOException ex) {
			rollback(lConn);
			if (ex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SICOException(SICOException.USER_MESSAGE,
						"Inserimento impossibile: Magistrato già esistente.");
			throw new F3BException("MagistratoController.ExInserisci: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExInserisciMagistrato: " + ex);
		} finally {
			cleanup(lMagSezDao);
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagMod;
	}

	/**
	 * Esegue inserimento del magistrato in ambito SIGE, inserendo le relazioni con le sezioni di
	 * appartenenza. Esegue inserimento di più sezioni per uno stesso Magistrato
	 * 
	 * @param aMagistrato
	 *            MagistratoModel opportunamente popolato
	 * @return
	 * @throws F3BException
	 *             propaga errori di eccezione
	 */
	public MagistratoModel ExInserisciMagistratoMultiSezione(MagistratoModel aMagistrato,
			String provenienzaInsMod) throws F3BException {
		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		MagistratoModel lMagMod = null;
		MagistratoSezioneDAO lMagSezDao = null;
		MagistratoSqlDAO lMagSqlDao = null;

		// precedenteSezione=false: per il presente magistrato tutti i periodi sono chiusi nella sezione
		// assegnata considerata
		// segue e' possibile inserire il nuovo periodo
		boolean precedenteSezione = false;
		// magistratoRegistrato=false: il magistrato non è presente ==> deve essere inserito
		boolean magistratoRegistrato = false;
		BigDecimal lKey = null;
		MagistratoSezioneModel magistratoSezione = null;

		try {
			lConn = getDBConnection();
			lMagMod = new MagistratoModel(aMagistrato);
			lMagDao = new MagistratoDAO(lConn);
			lMagDao.setDAOFromModel(aMagistrato);

			// Inizio controllo collegamento ad UDIENZA.
			// trovo la sezione del magistrato
			// MagistratoSezioneModel magistratoSezioneModelValido = null;
			// UdienzaSigeSqlDAO lUdiSigeSqlDao = new UdienzaSigeSqlDAO(lConn);

			// Inizio Controllo Magistrati considerando
			// In fase di inserimento deve essere possibile inserire per uno stesso Magistrato più sezioni,
			// le sezioni devono essere diverse cioe' la precedente sezione assegnata deve avere
			// la data di fine_assegnazione impostata e <= alla data inizio_assegnazione nuova
			// String lCognome = null;
			// String lSezione = null;
			// String lUffAppa = null;
			MagistratoSezioneModel[] lVectmagistratiInSezione = null;
			ArrayList lMagistrati = new ArrayList();
			MagistratoSezioneSqlDAO lMagSezSqlDao = null;
			// IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
			// lCognome = aMagistrato.getCognome();
			// lUffAppa = aMagistrato.getCodUfficioInserimento();

			// verifico se esiste il magistrato
			lMagSqlDao = new MagistratoSqlDAO(lConn);
			lMagSqlDao.ricercaMagistratoByCodUfficioCodMagistrato(aMagistrato.getCodUfficioAppartenenza(),
					aMagistrato.getCodMagistrato());
			lMagistrati = new ArrayList(lMagSqlDao.getModels());

			if (lMagistrati.size() > 0) {
				// magistrato è presente nella base dati
				magistratoRegistrato = true;
				MagistratoSezioneModel[] magistratoSezioneModels = aMagistrato.getMagistratoSezioni();
				if (magistratoSezioneModels != null) {
					Date dataInizioAssegnazione = magistratoSezioneModels[0].getDataInizioAssegnazione();
					// if (magistratoSezioneModels[0].getSezIdSezione() != null)
					// lSezione = magistratoSezioneModels[0].getSezIdSezione().toString();
					lMagSezSqlDao = new MagistratoSezioneSqlDAO(lConn);
					lMagSezSqlDao.ricercaMagistratoSezioneByCodMagistrato(lMagMod.getCodMagistrato());
					Collection lColl = new ArrayList();
					lColl = lMagSezSqlDao.getModels();
					lVectmagistratiInSezione = (MagistratoSezioneModel[]) lColl
							.toArray(new MagistratoSezioneModel[0]);
					for (int i = 0; i < lVectmagistratiInSezione.length; i++) {
						// confronto i periodi inizio, fine assegnazione con il periodo inserito
						magistratoSezione = lVectmagistratiInSezione[i];
						if (magistratoSezione.getDataFineAssegnazione() != null
								&& dataInizioAssegnazione != null)
							if (DateUtils.isGreater(magistratoSezione.getDataFineAssegnazione(),
									dataInizioAssegnazione)) {
								// Confronta le due date e restituisce true se DateA>DateB
								// per il presente magistrato esiste un periodo di sezione assegnata
								// non ancora chiusa segue non e' possibile inserirne un'altra
								precedenteSezione = true;
								break;
							}
					}
				}
			}
			// Fine Controllo Magistrati

			lMagSezDao = new MagistratoSezioneDAO(lConn);

			if (!precedenteSezione && !magistratoRegistrato) {
				// insert nelle tabelle MAGISTRATO "I" proviene da actInserisciMagistrato ==> insert
				if (provenienzaInsMod.equalsIgnoreCase("I"))
					lKey = lMagDao.insert();

				// Esegue inserimento delle relazioni alle sezioni MAGISTRATO_SEZIONE
				lMagSezDao = new MagistratoSezioneDAO(lConn);
				if (aMagistrato.getMagistratoSezioni() != null) {
					int lSize = aMagistrato.getMagistratoSezioni().length;
					for (int i = 0; i < lSize; i++) {
						// inizio modifica FLG_VALIDO_SN da 'S' a 'N'
						// FlagValidoSN se impostato a 'S' indica ULTIMA SEZIONE INSERITA ALTRIMENTI 'N'
						ExModificaMagistratoMultiSezioneFlgValidoSN(aMagistrato.getMagistratoSezioni()[i],
								aMagistrato.getCodMagistrato());
						// fine modifica FLG_VALIDO_SN
						lMagSezDao.setDAOFromModel(aMagistrato.getMagistratoSezioni()[i]);
						lMagSezDao.insert();
						lMagSezDao.stop();
					}
				}
			} else if (!precedenteSezione) {
				// insert nella tabella MAGISTRATO_SEZIONE (no nella tabella MAGISTRATO perchè esiste)
				//
				// Esegue inserimento delle relazioni alle sezioni.
				//
				lMagSezDao = new MagistratoSezioneDAO(lConn);
				if (aMagistrato.getMagistratoSezioni() != null) {
					int lSize = aMagistrato.getMagistratoSezioni().length;
					for (int i = 0; i < lSize; i++) {
						// inizio modifica FLG_VALIDO_SN da 'S' a 'N'
						// FlagValidoSN se impostato a 'S' indica ULTIMA SEZIONE INSERITA ALTRIMENTI 'N'
						ExModificaMagistratoMultiSezioneFlgValidoSN(aMagistrato.getMagistratoSezioni()[i],
								aMagistrato.getCodMagistrato());
						// fine modifica FLG_VALIDO_SN
						lMagSezDao.setDAOFromModel(aMagistrato.getMagistratoSezioni()[i]);
						lMagSezDao.insert();
						lMagSezDao.stop();
					}
				}
			} else {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Il precedente periodo Assegnazione non è concluso.");
			}

			commit(lConn);

			if (lKey == null)
				lMagMod.setCodMagistrato(aMagistrato.getCodMagistrato());
			else
				lMagMod.setCodMagistrato(lKey.toString());
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException ex) {

			rollback(lConn);
			if (ex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SICOException(SICOException.USER_MESSAGE,
						"Inserimento impossibile: Magistrato già esistente.");
			throw new F3BException("MagistratoController.ExInserisci: " + ex);

		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExInserisciMagistrato: " + ex);
		} finally {
			cleanup(lMagSezDao);
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagMod;
	}

	/**
	 * Esegue la modifica dei dati di un Magistrato.
	 * 
	 * @param aMagistrato
	 *            Dati del magistrato.
	 * @return i dati modificati al magistrato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExModificaMagistratoMultiSezioneFlgValidoSN(MagistratoSezioneModel aMagistratoSezione,
			String codMagistrato) throws F3BException {
		Connection lConn = null;
		MagistratoSezioneDAO lMagSezDao = null;

		try {
			lConn = getDBConnection();
			lMagSezDao = new MagistratoSezioneDAO(lConn);
			lMagSezDao.setDAOFromModelForUpdateFlgValidoSN(aMagistratoSezione);
			lMagSezDao.setCondizioneUpdateMultiSezioneFlgValidoSN(codMagistrato, "S");
			lMagSezDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExModificaMagistrato: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExModificaMagistrato: " + ex);
		} finally {
			cleanup(lMagSezDao);
			cleanup(lConn);
		}

	}

	/**
	 * Esegue la ricerca dei Magistrati
	 * 
	 * @param aMagistrato
	 *            dati ri ricerca.
	 * @return elenco delle occorrenze.
	 * @throws F3BException
	 *             propga errore di eccezione.
	 */
	public ArrayList<MagistratoModel> ExRicercaMagistrato(MagistratoModel aMagistrato, int page)
			throws F3BException {
		Connection lConn = null;
		ArrayList<MagistratoModel> lMagistrati = new ArrayList<MagistratoModel>();
		MagistratoSqlDAO lMagDao = null;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);
			lMagDao.ricercaMagistratoPaged(aMagistrato, page);
			lMagDao.start();

			while (lMagDao.next()) {
				lMagistrati.add((MagistratoModel) lMagDao.getModel());
			}

			if (lMagistrati.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIGEException("MagistratoController.ExRicercaMagistrato: " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagistrati;
	}

	/**
	 * Esegue la ricerca dei Magistrati
	 * 
	 * @param aMagistrato
	 *            dati ri ricerca.
	 * @return elenco delle occorrenze.
	 * @throws F3BException
	 *             propga errore di eccezione.
	 */
	public MagistratoSezioneModel ExRicercaMagistratoMultiSezione(MagistratoModel aMagistrato)
			throws F3BException {
		Connection lConn = null;
		// ArrayList lMagistrati = new ArrayList();
		MagistratoSezioneSqlDAO lMagSezSqlDao = null;
		MagistratoSezioneModel magistratoSezioneModel = null;

		try {
			lConn = getDBConnection();
			lMagSezSqlDao = new MagistratoSezioneSqlDAO(lConn);
			lMagSezSqlDao.ricercaMagistratoSezioneByCodMagistratoFlgValidoSN(aMagistrato.getCodMagistrato());

			// Collection lColl = new ArrayList();
			/* lColl = */lMagSezSqlDao.getModels();

		} catch (DAOException daoEx) {
			throw new SIGEException("MagistratoController.ExRicercaMagistrato: " + daoEx);
		} finally {
			cleanup(lMagSezSqlDao);
			cleanup(lConn);
		}

		return magistratoSezioneModel;
	}

	/**
	 * Numero dei record occorsi.
	 * 
	 * @param aMagistrato
	 * @return
	 * @throws F3BException
	 */
	public int ExGetNumRicercaMagistrato(MagistratoModel aMagistrato) throws F3BException {
		Connection lConn = null;
		MagistratoSqlDAO lMagDao = null;

		int lNum = 0;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);
			lMagDao.ricercaMagistrato(aMagistrato);
			lNum = lMagDao.getNumRowsSelected().intValue();
		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExRicercaCuratore: " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return lNum;
	}

	/**
	 * Numero dei record occorsi.
	 * 
	 * @param aMagistrato
	 * @return
	 * @throws F3BException
	 */
	public int ExGetNumRicercaMagistratoSezioneValida(MagistratoModel aMagistrato) throws F3BException {
		Connection lConn = null;
		MagistratoSezioneSqlDAO lMagSezSqlDao = null;
		int lNum = 0;

		try {
			lConn = getDBConnection();
			lMagSezSqlDao = new MagistratoSezioneSqlDAO(lConn);
			if (aMagistrato.getMagistratoSezioni() != null) {
				if (aMagistrato.getMagistratoSezioni()[0].getSezIdSezione() != null) {
					lMagSezSqlDao.ricercaMagistratoSezioneByCodMagistratoFlgValidoSN(
							aMagistrato.getCodMagistrato(),
							aMagistrato.getMagistratoSezioni()[0].getSezIdSezione().toString());
				} else {
					lMagSezSqlDao.ricercaMagistratoSezioneByCodMagistratoFlgValidoSN(
							aMagistrato.getCodMagistrato(), null);
				}
				// lMagDao.ricercaMagistrato(aMagistrato);
				lNum = lMagSezSqlDao.getNumRowsSelected().intValue();
			}
		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExRicercaCuratore: " + daoEx);
		} finally {
			cleanup(lMagSezSqlDao);
			cleanup(lConn);
		}
		return lNum;
	}

	/**
	 * Elenco dei magistrati per ufficio per utilizzo Combo Box
	 * 
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 */
	public Collection ExElencoCbxMagistratiByCodUfficio(String aCodUfficio) throws F3BException {
		Connection lConn = null;
		MagistratoSqlDAO lMagSqlDao = null;
		ArrayList lMagDecMods = new ArrayList();

		try {
			lConn = getDBConnection();
			lMagSqlDao = new MagistratoSqlDAO(lConn);
			// [EC] richiesto da Nunzia il 30/05/2019 devono essere selezionati soltanto i magistrati ancora attivi
			lMagSqlDao.ricercaMagistratoByCodUfficioAncoraValidi(aCodUfficio);
			lMagSqlDao.start();

			while (lMagSqlDao.next()) {
				String lCod = ((MagistratoModel) lMagSqlDao.getModel()).getCodMagistrato();
				String lCognome = ((MagistratoModel) lMagSqlDao.getModel()).getCognome();
				String lNome = ((MagistratoModel) lMagSqlDao.getModel()).getNome();

				lMagDecMods.add(new DecodeModel(lCod, lCognome + " " + lNome + " - " + lCod));
			}

			lMagSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MagistratoController.ExElencoCbxMagistratiByCodUfficio : Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMagSqlDao);
			cleanup(lConn);
		}

		return lMagDecMods;
	}

	/**
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 */
	public Collection ExElencoCbxMagByCodComuneCodTipoUff(String aCodComune, String aCodTipoUfficio)
			throws F3BException {
		Connection lConn = null;
		MagistratoSqlDAO lMagDao = null;
		ArrayList lMagDecMods = new ArrayList();

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);
			lMagDao.ricercaMagByCodComuneCodTipoUff(aCodComune, aCodTipoUfficio);

			lMagDao.start();

			while (lMagDao.next()) {
				String lCod = ((MagistratoModel) lMagDao.getModel()).getCodMagistrato();
				String lCognome = ((MagistratoModel) lMagDao.getModel()).getCognome();
				String lNome = ((MagistratoModel) lMagDao.getModel()).getNome();

				lMagDecMods.add(new DecodeModel(lCod, lCognome + " " + lNome + " - " + lCod));
			}

			lMagDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MagistratoController.ExElencoCbxMagByCodComuneCodTipoUff : Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagDecMods;
	}

	/*
	 * public Vector ExRicercaMagistratoPaged ( MagistratoModel aMagistrato,int aPage ) throws F3BException {
	 * Connection lConn = null; Vector lMagistrati = new Vector(); MagistratoSqlDAO lMagDao = null;
	 * 
	 * try { lConn = getDBConnection(); lMagDao = new MagistratoSqlDAO(lConn);
	 * lMagDao.ricercaMagistratoPaged(aMagistrato,aPage); lMagistrati = new Vector(lMagDao.getModels());
	 * 
	 * if ( lMagistrati.size() == 0 ) throw new
	 * F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato");
	 * 
	 * } catch (DAOException daoEx) { throw new F3BException("MagistratoController.ExRicercaMagistratoPaged: "
	 * + daoEx); } catch (SQLException sqe) { throw new
	 * F3BException("MagistratoController.ExRicercaMagistratoPaged: " + sqe); } finally { cleanup(lMagDao);
	 * cleanup(lConn); }
	 * 
	 * return lMagistrati; }
	 */

	/**
	 * @param aKey
	 * @return
	 * @throws F3BException
	 * 
	 *             public MagistratoModel ExRicercaMagistratoByKey ( BigDecimal aKey ) throws F3BException {
	 *             Connection lConn = null; MagistratoSqlDAO lMagDao = null; MagistratoModel lMagMod;
	 * 
	 *             try { lConn = getDBConnection(); lMagDao = new MagistratoSqlDAO(lConn);
	 *             lMagDao.ricercaMagistratoByKey(aKey); lMagMod = (MagistratoModel)lMagDao.getModelByKey(); }
	 *             catch (DAOException daoEx) { throw new
	 *             F3BException("MagistratoController.ExRicercaMagistratoByKey: " + daoEx); } catch
	 *             (SQLException sqe) { throw new F3BException("MagistratoController.ExRicercaMagistratoByKey:
	 *             " + sqe); } finally { cleanup(lMagDao); cleanup(lConn); }
	 * 
	 *             return lMagMod; }
	 */

	/**
	 * 
	 * 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un ufficio
	 * differente da quello in cui ha delle udienze poichè trasferito
	 * 
	 * Metodo che segue la ricerca del magistrato, attraverso il codice.
	 * 
	 * @param aCod
	 * @param codUfficioAppartenenza
	 * @return
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MagistratoModel ExRicercaMagistratoByCod(String aCod, String codUfficioAppartenenza)
			throws F3BException {

		Connection lConn = null;
		MagistratoSqlDAO lMagSqlDao = null;
		MagistratoSezioneSqlDAO lMagSezSqlDao = null;
		MagistratoModel lMagMod;

		lConn = getDBConnection();
		lMagSqlDao = new MagistratoSqlDAO(lConn);

		try { // 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
				// ufficio differente da quello in cui ha delle udienze poichè trasferito
			lMagSqlDao.ricercaMagistratoByCod(aCod, codUfficioAppartenenza);
			lMagMod = (MagistratoModel) lMagSqlDao.getModelByKey();

			// Riscrivere per il recupero completo dei dati.
			if(lMagMod != null){
			lMagSezSqlDao = new MagistratoSezioneSqlDAO(lConn);
			lMagSezSqlDao.ricercaMagistratoSezioneByCodMagistrato(lMagMod.getCodMagistrato());
			Collection lColl = new ArrayList();
			lColl = lMagSezSqlDao.getModels();
			lMagMod.setMagistratoSezioni(
					(MagistratoSezioneModel[]) lColl.toArray(new MagistratoSezioneModel[0]));
			}
		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExRicercaMagistratoByCod: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("MagistratoController.ExRicercaMagistratoByCod: " + ex);
		} finally {
			cleanup(lMagSqlDao);
			cleanup(lMagSezSqlDao);
			cleanup(lConn);
		}

		return lMagMod;
	}

	/*
	 * public MagistratoModel ExRicercaMagistratoByFascicolo ( BigDecimal aFascicolo ) throws F3BException {
	 * Connection lConn = null; MagistratoSqlDAO lMagDao = null; MagistratoModel lMagMod;
	 * 
	 * try { lConn = getDBConnection(); lMagDao = new MagistratoSqlDAO(lConn);
	 * 
	 * lMagDao.ricercaMagistratoByFascicolo(aFascicolo); lMagMod = (MagistratoModel)lMagDao.getModelByKey(); }
	 * catch (DAOException daoEx) { throw new
	 * F3BException("MagistratoController.ExRicercaMagistratoByFascicolo: " + daoEx); } catch (SQLException
	 * sqe) { throw new F3BException("MagistratoController.ExRicercaMagistratoByFascicolo: " + sqe); } finally
	 * { cleanup(lMagDao); cleanup(lConn); } return lMagMod; }
	 */
	/**
	 *
	 * <p>
	 * 
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 */
	/*
	 * public Vector ExRicercaMagistratoByCodUfficio ( String aCodUfficio ) throws F3BException { Connection
	 * lConn = null; MagistratoSqlDAO lMagDao = null; Vector lMagMods = null;
	 * 
	 * try { lConn = getDBConnection(); lMagDao = new MagistratoSqlDAO(lConn);
	 * 
	 * lMagDao.ricercaMagistratoByCodUfficio( aCodUfficio ); lMagMods = new Vector( lMagDao.getModels() ); }
	 * catch (DAOException daoEx) { throw new
	 * F3BException("MagistratoController.ExRicercaMagistratoByCodUfficio: " + daoEx); } catch (SQLException
	 * sqe) { throw new F3BException("MagistratoController.ExRicercaMagistratoByCodUfficio: " + sqe); }
	 * finally { cleanup(lMagDao); cleanup(lConn); }
	 * 
	 * return lMagMods; }
	 */
	/**
	 *
	 * <p>
	 * 
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 */
	/*
	 * public Vector ExElencoCbxMagistratiByCodUfficio ( String aCodUfficio ) throws F3BException { Connection
	 * lConn = null; MagistratoSqlDAO lMagDao = null; Vector lMagDecMods = new Vector();
	 * 
	 * try { lConn = getDBConnection(); lMagDao = new MagistratoSqlDAO(lConn);
	 * 
	 * lMagDao.ricercaMagistratoByCodUfficio( aCodUfficio ); lMagDao.start();
	 * 
	 * // da Considerare l'implematazione di metodi getDecodeModel nel genricDAO. // .... penserò. Paolo
	 * while( lMagDao.next() ) { String lCod = ((MagistratoModel)lMagDao.getModel()).getCodMagistrato();
	 * String lCognome = ((MagistratoModel)lMagDao.getModel()).getCognome(); String lNome =
	 * ((MagistratoModel)lMagDao.getModel()).getNome();
	 * 
	 * // STUB 03/12/2004 lMagDecMods.add(new DecodeModel( lCod, lCod + " - " + lCognome + " " + lNome ) );
	 * lMagDecMods.add(new DecodeModel( lCod, lCognome + " " + lNome + " - " + lCod ) ); }
	 * 
	 * lMagDao.stop();
	 * 
	 * //lMagMods = new Vector( lMagDao.getModels() ); } catch (DAOException daoEx) { throw new
	 * F3BException("MagistratoController.ExRicercaMagistratoByCodUfficio: " + daoEx); } catch (SQLException
	 * sqe) { throw new
	 * F3BException("MagistratoController.ExRicercaMagistratoByCodUfficio: Non posso leggere  : " + sqe); }
	 * finally { cleanup(lMagDao); cleanup(lConn); }
	 * 
	 * return lMagDecMods; }
	 */
	// RICERCA LISTA MAGISTRATO
	/*
	 * public Vector ExRicercaMagistratoByCognome (String aCognome , String aUfficio) throws F3BException {
	 * Connection lConn = null; Vector lMagistrati = new Vector(); MagistratoWMagistratoSqlDAO lMaDao = null;
	 * 
	 * 
	 * try { lConn = getDBConnection(); lMaDao = new MagistratoWMagistratoSqlDAO(lConn);
	 * lMaDao.ricercaMagistratoByCognome(aCognome,aUfficio); lMagistrati = new Vector(lMaDao.getModels()); if
	 * ( lMagistrati.size() == 0 ) { throw new
	 * F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato"); }
	 * 
	 * } catch (DAOException daoEx) { throw new F3BException("WMagistratoController.ExRicercaWMagistrato: " +
	 * daoEx); } catch (SQLException sqe) { throw new
	 * F3BException("WMagistratoController.ExRicercaWMagistrato: " + sqe); } finally { cleanup(lMaDao);
	 * cleanup(lConn); } return lMagistrati; }
	 */

	/**
	 *
	 * <p>
	 * 
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 */
	/*
	 * public Vector ExElencoCbxMagByCodComuneCodTipoUff ( String aCodComune, String aCodTipoUfficio ) throws
	 * F3BException { Connection lConn = null; MagistratoSqlDAO lMagDao = null; Vector lMagDecMods = new
	 * Vector();
	 * 
	 * try { lConn = getDBConnection(); lMagDao = new MagistratoSqlDAO(lConn);
	 * lMagDao.ricercaMagByCodComuneCodTipoUff( aCodComune, aCodTipoUfficio );
	 * 
	 * lMagDao.start();
	 * 
	 * // da Considerare l'implematazione di metodi getDecodeModel nel genricDAO. // .... penserò. Paolo
	 * while( lMagDao.next() ) { String lCod = ((MagistratoModel)lMagDao.getModel()).getCodMagistrato();
	 * String lCognome = ((MagistratoModel)lMagDao.getModel()).getCognome(); String lNome =
	 * ((MagistratoModel)lMagDao.getModel()).getNome();
	 * 
	 * // STUB 03/12/2004 lMagDecMods.add(new DecodeModel( lCod, lCod + " - " + lCognome + " " + lNome ) );
	 * lMagDecMods.add(new DecodeModel( lCod, lCognome + " " + lNome + " - " + lCod ) ); }
	 * 
	 * lMagDao.stop();
	 * 
	 * //lMagMods = new Vector( lMagDao.getModels() ); } catch (DAOException daoEx) { throw new
	 * F3BException("MagistratoController.ExRicercaMagistratoByCodUfficio: " + daoEx); } catch (SQLException
	 * sqe) { throw new F3BException("MagistratoController.ExRicercaMagistratoByCodUfficio: " + sqe); }
	 * finally { cleanup(lMagDao); cleanup(lConn); }
	 * 
	 * return lMagDecMods; }
	 */

	/**
	 * Esegue la modifica dei dati di un Magistrato.
	 * <p>
	 * 
	 * @param aMagistrato
	 *            Dati del magistrato.
	 * @return i dati modificati al magistrato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MagistratoModel ExModificaMagistrato(MagistratoModel aMagistrato) throws F3BException {
		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		MagistratoSezioneDAO lMagSezDao = null;
		MagistratoModel lMagMod = new MagistratoModel(aMagistrato);

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoDAO(lConn);
			lMagDao.setDAOFromModelForUpdate(aMagistrato);
			lMagDao.update();

			//
			// Si esegue cancellazione delle relazioni Magistrato Sezione
			// prima di eseguire nuovo inserimento delle sezioni riassociate.
			//
			lMagSezDao = new MagistratoSezioneDAO(lConn);
			lMagSezDao.setCondizioneByCodMag(aMagistrato.getCodMagistrato());
			lMagSezDao.delete();
			lMagSezDao.stop();

			//
			// Esegue inserimento delle nuove relazioni alle sezioni.
			//
			lMagSezDao = new MagistratoSezioneDAO(lConn);
			if (aMagistrato.getMagistratoSezioni() != null) {
				int lSize = aMagistrato.getMagistratoSezioni().length;
				for (int i = 0; i < lSize; i++) {
					lMagSezDao.setDAOFromModel(aMagistrato.getMagistratoSezioni()[i]);
					lMagSezDao.insert();
					lMagSezDao.stop();
				}
			}

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExModificaMagistrato: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExModificaMagistrato: " + ex);
		} finally {
			cleanup(lMagSezDao);
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return lMagMod;
	}

	/**
	 * Esegue la modifica dei dati di un Magistrato.
	 * <p>
	 * 
	 * @param aMagistrato
	 *            Dati del magistrato.
	 * @return i dati modificati al magistrato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MagistratoModel ExModificaMagistratoMultiSezione(MagistratoModel aMagistrato) throws F3BException {
		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		MagistratoModel lMagMod = new MagistratoModel(aMagistrato);
		// MagistratoSqlDAO lMagSqlDao = null;

		// Gestione multi sezione
		// non cancello ma chiudo la precedente sezione dove imposto la "Data Fine Assegnazione" alla data
		// odierna
		// la nuova sezione avrà "Data Inizio Assegnazione" alla data odierna
		UdienzaSigeSqlDAO lUdiSigeSqlDao = null;
		MagistratoSezioneDAO lMagSezDao = null;
		MagistratoSezioneModel[] magistratoSezioniVett = null;
		MagistratoSezioneModel magistratoSezioneModel = null;
		magistratoSezioniVett = aMagistrato.getMagistratoSezioni();
		int ultimaSezioneModificabile = magistratoSezioniVett.length - 1;
		magistratoSezioneModel = magistratoSezioniVett[ultimaSezioneModificabile];
		MagistratoSezioneModel lMagSezMod = new MagistratoSezioneModel(magistratoSezioneModel);

		try {
			lConn = getDBConnection();

			lMagDao = new MagistratoDAO(lConn);

			// modifico i dati del magistrato
			lMagDao.setDAOFromModelForUpdate(aMagistrato);
			lMagDao.update();

			// Controllo collegamento ad UDIENZA.
			lUdiSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
			// ufficio differente da quello in cui ha delle udienze poichè trasferito
			lUdiSigeSqlDao.countMagUdiCodMagistrato(aMagistrato.getCodMagistrato(),
					aMagistrato.getCodUfficioAppartenenza());
			lUdiSigeSqlDao.start();
			lUdiSigeSqlDao.next();
			// 20170914: [SG] aggiunto ulteriore controllo se trasferito
			// Preleva la count e verifica che non sia == a zero.
			if (lUdiSigeSqlDao.getInt("COUNT") != 0 && !"T".equals(aMagistrato.getFlagStato()))
				throw new SIGEException(SICOException.USER_MESSAGE,
						"Modifica impossibile: Il Magistrato è associato ad una Udienza.");

			lMagSezDao = new MagistratoSezioneDAO(lConn);
			// non cancello ma chiudo la precedente sezione dove imposto la "Data Fine Assegnazione" alla data
			// odierna
			lMagSezMod.setDataFineAssegnazione(DateUtils.getSysDate());
			lMagSezDao.setDAOFromModelForUpdate(lMagSezMod);
			lMagSezDao.setCondizioneUpdateMultiSezioneFlgValidoSN(aMagistrato.getCodMagistrato(), "S");

			// [EC] aggiungo il parametro di idsezione da modificare
			if (aMagistrato.getMagistratoSezioni() != null && aMagistrato.getMagistratoSezioni().length > 0
					&& aMagistrato.getMagistratoSezioni()[0].getSezIdSezione() != null) {
				// potrebbe selezionare più di una sezione per volta sulla pagina
				lMagSezDao.setCondizioneForUpdateBySezioni(aMagistrato.getMagistratoSezioni());
			}

			lMagSezDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException(
					"MagistratoController.ExModificaMagistratoMultiSezione: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExModificaMagistratoMultiSezione: " + ex);
		} finally {
			cleanup(lMagSezDao);
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return lMagMod;
	}

	/**
	 * Esegue la modifica dei dati di un Magistrato.
	 * <p>
	 * 
	 * @param aMagistrato
	 *            Dati del magistrato.
	 * @return i dati modificati al magistrato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MagistratoModel ExModificaMagistratoMultiSezioneValida(MagistratoModel aMagistrato)
			throws F3BException {
		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		MagistratoModel lMagMod = new MagistratoModel(aMagistrato);
		// MagistratoSqlDAO lMagSqlDao = null;

		// Gestione multi sezione
		// non cancello ma chiudo la precedente sezione dove imposto la "Data Fine Assegnazione" alla data
		// odierna
		// la nuova sezione avrà "Data Inizio Assegnazione" alla data odierna
		// UdienzaSigeSqlDAO lUdiSigeSqlDao = null;
		MagistratoSezioneDAO lMagSezDao = null;
		// MagistratoSezioneModel magistratoSezioneModel = null;

		try {
			lConn = getDBConnection();

			lMagSezDao = new MagistratoSezioneDAO(lConn);
			lMagDao = new MagistratoDAO(lConn);

			// modifico i dati del magistrato
			lMagDao.setDAOFromModelForUpdate(aMagistrato);
			lMagDao.update();

			// modifico la sezione del magistrato
			MagistratoSezioneModel lMagSezMod = new MagistratoSezioneModel(
					aMagistrato.getMagistratoSezioni()[0]);
			lMagSezDao.setDAOFromModelForUpdate(lMagSezMod);
			lMagSezDao.setCondizioneUpdateMultiSezioneFlgValidoSN(aMagistrato.getCodMagistrato(), "S");

			// [EC] aggiungo il parametro di idsezione da modificare
			if (aMagistrato.getMagistratoSezioni() != null && aMagistrato.getMagistratoSezioni().length > 0
			// 20171117 [EC] errore allegato SIGE 10.1.5.B.docx
					&& aMagistrato.getMagistratoSezioni()[0].getSezIdSezione() != null) {
				// potrebbe selezionare più di una sezione per volta sulla pagina
				lMagSezDao.setCondizioneForUpdateBySezioni(aMagistrato.getMagistratoSezioni());
			}

			lMagSezDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException(
					"MagistratoController.ExModificaMagistratoMultiSezione: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExModificaMagistratoMultiSezione: " + ex);
		} finally {
			cleanup(lMagSezDao);
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return lMagMod;
	}

	/**
	 * Esegue la modifica dei dati di un Magistrato.
	 * 
	 * @param aMagistrato
	 *            Dati del magistrato.
	 * @return i dati modificati al magistrato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public boolean controlloCollegamentoUdienza(MagistratoModel aMagistrato) throws F3BException {
		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		// MagistratoModel lMagMod = new MagistratoModel(aMagistrato);
		UdienzaSigeSqlDAO lUdiSigeSqlDao = null;
		MagistratoSezioneDAO lMagSezDao = null;
		MagistratoSezioneModel magistratoSezioneModel = null;
		boolean sezioneAssociataUdienza = false; // non è valida ==> NO MODIFICA

		try {
			lConn = getDBConnection();

			// Inizio controllo collegamento ad UDIENZA.
			lUdiSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
			// ufficio differente da quello in cui ha delle udienze poichè trasferito

			// 20171012: [EC] INTRODUCO IL CONTROLLO SULLA PRESENZA DELLE SEZIONI
			if (aMagistrato.getMagistratoSezioni() == null) {
				lUdiSigeSqlDao.countMagUdiCodMagistrato(aMagistrato.getCodMagistrato(),
						aMagistrato.getCodUfficioAppartenenza());
				lUdiSigeSqlDao.start();
				lUdiSigeSqlDao.next();
				// Preleva la count e verifica che sia == a zero.
				if (lUdiSigeSqlDao.getInt("COUNT") != 0)
					sezioneAssociataUdienza = true;
			}

			if (aMagistrato.getMagistratoSezioni() != null) {
				int lSize = aMagistrato.getMagistratoSezioni().length;
				for (int i = 0; i < lSize; i++) {
					// FlagValidoSN se impostato a 'S' indica ULTIMA SEZIONE INSERITA ALTRIMENTI 'N'
					magistratoSezioneModel = aMagistrato.getMagistratoSezioni()[i];
				}

				// lUdiSigeSqlDao.countMagUdiCodMagistratoSezioneValida(aMagistrato.getCodMagistrato(),
				// magistratoSezioneModelValido.getDataFineAssegnazione().toString());

				// 20171012: [EC] aggiunto parametro idSezione
				lUdiSigeSqlDao.countMagUdiCodMagistratoSezioneValida(aMagistrato.getCodMagistrato(),
						magistratoSezioneModel.getDataFineAssegnazione(),
						aMagistrato.getCodUfficioAppartenenza(), aMagistrato.getMagistratoSezioni());
				lUdiSigeSqlDao.start();
				lUdiSigeSqlDao.next();
				// Preleva la count e verifica che sia == a zero.
				if (lUdiSigeSqlDao.getInt("COUNT") != 0)
					sezioneAssociataUdienza = true;
				// Fine controllo collegamento ad UDIENZA.
			}
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException(
					"MagistratoController.ExModificaMagistratoMultiSezione: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExModificaMagistratoMultiSezione: " + ex);
		} finally {
			cleanup(lMagSezDao);
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return sezioneAssociataUdienza;
	}

	/**
	 * Esegue cancellazione del magistrato.
	 * <p>
	 * 
	 * @param aMagistrato
	 * @throws F3BException
	 */
	public void ExCancellaMagistrato(String aCodMagistrato, String aCodUff) throws F3BException {
		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		MagistratoSezioneDAO lMagSezDao = null;
		UdienzaSigeSqlDAO lUdiSigeSqlDao = null;
		// 06/04/2007 Controllo cancellazione Magistrato solo se non assegnatario di procedimenti.
		// MagistratoRelatoreSqlDAO lMagRelSqlDao = null;
		// MagistratoCompetenteSqlDAO lMagCompSqlDao = null;

		try {
			lConn = getDBConnection();

			//
			// NOTE 20080817 :
			// Si commentano le parti che eseguono controlli d'integrità, ossia se il magistrato
			// è assegnatario di procedimenti. Tale esclusione è esguita per ulteriore approfondimento
			// con l'analista e perchè non sembrano contestualizzate.
			// Tuttavia, temporaneamente si implementa la rimozione dei riferimenti alle sezioni, senza
			// alcun tipo di controllo.
			//

			// Controllo collegamento ad UDIENZA. L'analista ci ha ripensato !!
			lUdiSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
			// ufficio differente da quello in cui ha delle udienze poichè trasferito
			lUdiSigeSqlDao.countMagUdiCodMagistrato(aCodMagistrato, aCodUff);
			lUdiSigeSqlDao.start();
			lUdiSigeSqlDao.next();

			// Preleva la count e verifica che sia == a zero.
			if (lUdiSigeSqlDao.getInt("COUNT") != 0)
				throw new SIGEException(SICOException.USER_MESSAGE,
						"Cancellazione impossibile: Il Magistrato è associato ad una Udienza.");

			/*
			 * // 06/04/2007 Controllo cancellazione Magistrato solo se non assegnatario di procedimenti.
			 * lMagRelSqlDao = new MagistratoRelatoreSqlDAO(lConn);
			 * lMagRelSqlDao.countMagRelByCodMagistrato(aCodMagistrato); lMagRelSqlDao.start();
			 * lMagRelSqlDao.next(); // Preleva la count e verifica che sia == a zero. if(
			 * lMagRelSqlDao.getInt( "COUNT" ) != 0 ) throw new SICOException(SICOException.USER_MESSAGE,
			 * "Cancellazione impossibile: Il Magistrato è assegnatario di procedimenti.");
			 * 
			 * lMagCompSqlDao = new MagistratoCompetenteSqlDAO(lConn);
			 * lMagCompSqlDao.countMagCompByCodMagistrato(aCodMagistrato); lMagCompSqlDao.start();
			 * lMagCompSqlDao.next(); // Preleva la count e verifica che sia == a zero. if(
			 * lMagCompSqlDao.getInt( "COUNT" ) != 0 ) throw new SICOException(SICOException.USER_MESSAGE,
			 * "Cancellazione impossibile: Il Magistrato è assegnatario di procedimenti.");
			 */
			// Rimozione delle sezioni
			lMagSezDao = new MagistratoSezioneDAO(lConn);
			lMagSezDao.setCondizioneByCodMag(aCodMagistrato);
			lMagSezDao.delete();

			// Rimozione del magistrato
			lMagDao = new MagistratoDAO(lConn);
			lMagDao.setCondizioneUpdate(aCodMagistrato, aCodUff);
			lMagDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Magistrato collegato ad altri dati. Impossibile effettuare la Cancellazione!");
			throw new SIGEException("MagistratoController.ExCancellaMagistrato: " + daoEx);
		} catch (SIGEException SigeEx) {
			rollback(lConn);
			throw SigeEx;
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIGEException("MagistratoController.ExCancellaMagistrato: " + ex);
		} finally {
			cleanup(lMagDao);
			cleanup(lMagSezDao);
			cleanup(lUdiSigeSqlDao);
			// cleanup(lMagRelSqlDao);
			// cleanup(lMagCompSqlDao);
			cleanup(lConn);
		}
	}

	public Vector ExRicercaMagistratoByCognomeSezione(String aCognome, String aSezione, String aUfficio)
			throws F3BException {
		Connection lConn = null;
		Vector lMagistrati = new Vector();
		// MagistratoWMagistratoSqlDAO lMaDao = null;
		MagistratoSqlDAO lMaDao = null;

		try {
			lConn = getDBConnection();
			lMaDao = new MagistratoSqlDAO(lConn);

			lMaDao.ricercaMagistratoByCognomeSezione(aCognome, aSezione, aUfficio);
			// lMagistrati = new Vector(lMaDao.getModels());
			lMaDao.start();
			while (lMaDao.next())
				lMagistrati.add(lMaDao.getWModel());
			lMaDao.stop();

			if (lMagistrati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}

		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExRicercaMagistratoByCognomeSezione: " + daoEx);
		} catch (F3BException Ex) {
			throw Ex;
		} finally {
			cleanup(lMaDao);
			cleanup(lConn);
		}
		return lMagistrati;
	}

	/**
	 * Esegue la modifica dei dati di un Magistrato, per il quale non è presente alcuna sezione.
	 * 
	 * @param aMagistrato
	 *            Dati del magistrato.
	 * @return i dati modificati al magistrato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MagistratoModel ExModificaMagistratoNoSezioni(MagistratoModel aMagistrato) throws F3BException {
		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		MagistratoModel lMagMod = new MagistratoModel(aMagistrato);

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoDAO(lConn);
			lMagDao.setDAOFromModelForUpdate(aMagistrato);
			lMagDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExModificaMagistrato: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExModificaMagistrato: " + ex);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return lMagMod;
	}

	@Override
	public Vector<MagistratoModel> ExRicercaMagistratoByIdSezioneUffApp(BigDecimal idSezione, String lUffAppa)
			throws F3BException {
		Connection lConn = null;
		Vector<MagistratoModel> lMagistrati = new Vector<MagistratoModel>();
		// MagistratoWMagistratoSqlDAO lMaDao = null;
		MagistratoSqlDAO lMaDao = null;

		try {
			lConn = getDBConnection();
			lMaDao = new MagistratoSqlDAO(lConn);

			lMaDao.ricercaMagistratoByIdSezioneUffApp(idSezione, lUffAppa);
			// lMagistrati = new Vector(lMaDao.getModels());
			lMaDao.start();
			while (lMaDao.next())
				lMagistrati.add((MagistratoModel) lMaDao.getWModel());

			lMaDao.stop();

			if (lMagistrati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}

		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExRicercaMagistratoByIdSezioneUffApp: " + daoEx);
		} catch (F3BException Ex) {
			throw Ex;
		} finally {
			cleanup(lMaDao);
			cleanup(lConn);
		}
		return lMagistrati;
	}

	// 20170919: [SG] aggiunta query per controllo preventivo
	// 20171012: [EC] mi faccio tornare la lista degli uffici dove è attivo
	public Vector<String> controllaMagistratoAttivoAltriUffici(MagistratoModel lMagMod) throws F3BException {

		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		UdienzaSigeSqlDAO lUdiSigeSqlDao = null;
		MagistratoSezioneDAO lMagSezDao = null;
		// boolean sezioneAssociataUdienza = false; // non è valida ==> NO MODIFICA

		Vector<String> ufficiAttiviperMagistrato = new Vector<String>();

		try {
			lConn = getDBConnection();

			// Inizio controllo collegamento ad UDIENZA.
			lUdiSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			// 20170919: [SG] aggiunta query per controllo preventivo
			lUdiSigeSqlDao.controllaMagistratoAttivoAltriUffici(lMagMod.getCodMagistrato(),
					lMagMod.getCodUfficioAppartenenza());
			lUdiSigeSqlDao.start();

			while (lUdiSigeSqlDao.next())
				ufficiAttiviperMagistrato.add((String) lUdiSigeSqlDao.getString("descrUfficio"));

			lUdiSigeSqlDao.next();

		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.controllaMagistratoAttivoAltriUffici: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.controllaMagistratoAttivoAltriUffici: " + ex);
		} finally {
			cleanup(lMagSezDao);
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return ufficiAttiviperMagistrato;
	}

	/*
	 * public String ExInserisciMagistratoWithoutSequence(MagistratoModel aMagistrato, Connection lConn)
	 * throws F3BException {
	 * 
	 * String lCodEsito = "00000"; MagistratoDAO lMagDao = null; MagistratoModel lMagMod = null; try { lMagDao
	 * = new MagistratoDAO(lConn);
	 * 
	 * if (aMagistrato != null && aMagistrato.getCodMagistrato()!= null) { // [FT] - 03/08/2016 - MAC_LOG -
	 * Utilizzo la variabile di istanza siesLogger al posto di mLog
	 * siesLogger.info(" Magistrato da inserire = " + aMagistrato); lMagDao.setDAOFromModel(aMagistrato);
	 * lMagDao.setWithoutSequence(true); lMagDao.insert(); lMagDao.stop(); } } catch (DAOException ex) { if
	 * (ex.UNIQUE_CONSTRAINT_VIOLATED) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
	 * siesLogger al posto di mLog siesLogger.warn("Magistrato gia' presente..."); lCodEsito = "00001"; } else
	 * { lCodEsito = "01400"; // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
	 * posto di mLog siesLogger.warn(F3BException.USER_MESSAGE + " Impossibile inserire il Magistrato! "); } }
	 * catch (SQLException sqe) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
	 * al posto di mLog siesLogger.warn("SQLException: " + sqe); } finally { cleanup(lMagDao); } return
	 * lCodEsito; }
	 */

	/**
	 * 
	 * 20171013: [EC] aggiunto metodo
	 * 
	 * Metodo che segue la ricerca del magistrato, attraverso il codice ed ufficio appartenenza
	 * 
	 * @param aCod
	 * @param codUfficioAppartenenza
	 * @return
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MagistratoModel ExRicercaMagistratoByCodEdUfficioAppartenenza(String aCod,
			String codUfficioAppartenenza) throws F3BException {

		Connection lConn = null;
		MagistratoSqlDAO lMagSqlDao = null;
		MagistratoSezioneSqlDAO lMagSezSqlDao = null;
		MagistratoModel lMagMod;

		lConn = getDBConnection();
		lMagSqlDao = new MagistratoSqlDAO(lConn);

		try {

			lMagSqlDao.ricercaMagistratoByCod(aCod, codUfficioAppartenenza);
			lMagMod = (MagistratoModel) lMagSqlDao.getModelByKey();

			lMagSezSqlDao = new MagistratoSezioneSqlDAO(lConn);
			lMagSezSqlDao.ricercaMagistratoSezioneByCodMagistrato(lMagMod.getCodMagistrato(),
					codUfficioAppartenenza);
			Collection lColl = new ArrayList();
			lColl = lMagSezSqlDao.getModels();
			lMagMod.setMagistratoSezioni(
					(MagistratoSezioneModel[]) lColl.toArray(new MagistratoSezioneModel[0]));
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MagistratoController.ExRicercaMagistratoByCodEdUfficioAppartenenza: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"MagistratoController.ExRicercaMagistratoByCodEdUfficioAppartenenza: " + ex);
		} finally {
			cleanup(lMagSqlDao);
			cleanup(lMagSezSqlDao);
			cleanup(lConn);
		}

		return lMagMod;
	}

	
	/**
	 * 
	 * 20190104: [EC] aggiunto metodo
	 * 
	 * Metodo che segue la ricerca del magistrato attraverso il proprio codice 
	 * 
	 * @param aCod
	 * @param codUfficioAppartenenza
	 * @return
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MagistratoModel ExRicercaMagistratoByCodETipoUfficio(String codMag,
			String codTipoUfficio, String comuneUfficio) throws F3BException {

		Connection lConn = null;
		MagistratoSqlDAO lMagSqlDao = null;
		MagistratoSezioneSqlDAO lMagSezSqlDao = null;
		MagistratoModel lMagMod;

		lConn = getDBConnection();
		lMagSqlDao = new MagistratoSqlDAO(lConn);

		try {

			lMagSqlDao.ExRicercaMagistratoByCodETipoUfficio(codMag, codTipoUfficio, comuneUfficio);
			lMagMod = (MagistratoModel) lMagSqlDao.getModelByKey();
			
		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExRicercaMagistratoByCodETipoUfficio: "
					+ daoEx);
		} catch (Exception ex) {
			throw new F3BException("MagistratoController.ExRicercaMagistratoByCodETipoUfficio: "
					+ ex);
		} finally {
			cleanup(lMagSqlDao);
			cleanup(lMagSezSqlDao);
			cleanup(lConn);
		}

		return lMagMod;
	}	

}