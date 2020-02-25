package siap.sico.residenza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiepDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiusModel;
import siap.sico.residenza.model.ResidenzaModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ResidenzaController
 * </p>
 * <p>
 * Description: Classe Controller per Residenza
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
public class ResidenzaController extends SiapController implements IResidenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*
	 * public void ExInserisciResidenza (ResidenzaModel aResidenza) throws F3BException { Connection lConn =
	 * null;
	 * 
	 * ResidenzaDAO lResDao = null; ResidenzaSqlDAO lSqlDao = null;
	 * 
	 * try { lConn = getDBTransaction();
	 * 
	 * // 1- Cerca l'ultima occorrenza per quel soggetto eventualmente presente BigDecimal lIdRes = null;
	 * 
	 * lSqlDao = new ResidenzaSqlDAO(lConn);
	 * 
	 * lSqlDao.ricercaMaxIdResidenzaPerIdSoggetto(aResidenza.getSogIdSoggetto()); lSqlDao.start();
	 * if(lSqlDao.next()) { lIdRes = lSqlDao.getIdResidenza(); } lSqlDao.stop();
	 * 
	 * lResDao = new ResidenzaDAO(lConn); // Imposta il parametro del tipo di transazione
	 * //lResDao.setTransaction();
	 * 
	 * // 2- Eventualmente setta la data fine if(lIdRes != null) { lResDao.setIdResidenza(lIdRes);
	 * lResDao.setDataFineValidita(new Date()); lResDao.selByKey(); lResDao.update(); lResDao.stop(); }
	 * 
	 * // 3- Inserisce la nuova occorrenza di residenza lResDao.setDAOFromModel(aResidenza); BigDecimal
	 * lSequence = lResDao.insert();
	 * 
	 * aResidenza.setIdResidenza(lSequence);
	 * 
	 * commit(lConn); } catch (DAOException ex) { rollback(lConn); throw new
	 * F3BException("ResidenzaController.ExInserisci: Non posso inserire: " + ex); } catch (SQLException sqe)
	 * { rollback(lConn); throw new
	 * F3BException("ResidenzaController.ExInserisciResidenza: Non posso inserire : " + sqe); } catch
	 * (Exception ex) { rollback(lConn); throw new
	 * F3BException("ResidenzaController.ExInserisciResidenza: Non posso inserire : " + ex); } finally {
	 * cleanup(lResDao); cleanup(lSqlDao); cleanup(lConn); } }
	 */

	/*
	 * public ResidenzaModel ExInserisciResidenza(ResidenzaModel aResidenza) throws F3BException { Connection
	 * lConn = null;
	 * 
	 * ResidenzaDAO lResDao = null;
	 * 
	 * try { lConn = getDBTransaction();
	 * 
	 * // Storicizza l'ultima occorrenza eventualmente presente BigDecimal lIdRes = null; if(
	 * (aResidenza.getCodTipoResidenza()).equals("R") ) lIdRes =
	 * getIdResidenzaCorrente(aResidenza.getSogIdSoggetto()); else lIdRes =
	 * getIdDomicilioCorrente(aResidenza.getSogIdSoggetto());
	 * 
	 * lResDao = new ResidenzaDAO(lConn); if(lIdRes != null) { lResDao.setIdResidenza(lIdRes);
	 * //lResDao.setDataFineValidita(new Date()); lResDao.selByKey(); lResDao.update(); lResDao.stop(); }
	 * 
	 * // Inserisce la nuova occorrenza di residenza lResDao.setDAOFromModel(aResidenza); BigDecimal lSequence
	 * = lResDao.insert();
	 * 
	 * aResidenza.setIdResidenza(lSequence);
	 * 
	 * commit(lConn); } catch (DAOException ex) { rollback(lConn); throw new
	 * F3BException("ResidenzaController.ExInserisci: Non posso inserire: " + ex); } catch (SQLException sqe)
	 * { rollback(lConn); throw new
	 * F3BException("ResidenzaController.ExInserisciResidenza: Non posso inserire il soggetti : " + sqe); }
	 * finally { cleanup(lResDao); cleanup(lConn); }
	 * 
	 * return aResidenza; }
	 */

	public ResidenzaModel ExInserisciResidenza(ResidenzaModel aResidenza) throws F3BException {
		Connection lConn = null;
		ResidenzaDAO lResDao = null;
		ResidenzaModel lResMod = null;
		try {
			lConn = getDBConnection();
			lResMod = new ResidenzaModel(aResidenza);
			lResDao = new ResidenzaDAO(lConn);
			lResDao.setDAOFromModel(lResMod);
			BigDecimal lKey = null;
			lKey = lResDao.insert();
			commit(lConn);

			lResMod.setIdResidenza(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ResidenzaController.ExInserisciResidenza: " + ex);
		} finally {
			cleanup(lResDao);
			cleanup(lConn);
		}

		return lResMod;
	}

//	private BigDecimal getIdResidenzaCorrente(BigDecimal aIdSoggetto) throws F3BException {
//		Connection lConn = null;
//		BigDecimal lIdRes = null;
//		ResidenzaSqlDAO lSqlDao = null;
//		try {
//			lConn = getDBConnection();
//			lSqlDao = new ResidenzaSqlDAO(lConn);
//			lSqlDao.ricercaMaxIdResidenzaPerIdSoggetto(aIdSoggetto, "R");
//			lSqlDao.start();
//			if (lSqlDao.next()) {
//				lIdRes = lSqlDao.getIdResidenza();
//			}
//			lSqlDao.stop();
//		} catch (DAOException ex) {
//			throw new F3BException("ResidenzaController.ExInserisci: Non posso inserire: " + ex);
//		} finally {
//			cleanup(lSqlDao);
//			cleanup(lConn);
//		}
//
//		return lIdRes;
//	}

//	private BigDecimal getIdDomicilioCorrente(BigDecimal aIdSoggetto) throws F3BException {
//		Connection lConn = null;
//		BigDecimal lIdRes = null;
//		ResidenzaSqlDAO lSqlDao = null;
//		try {
//			lConn = getDBConnection();
//			lSqlDao = new ResidenzaSqlDAO(lConn);
//			lSqlDao.ricercaMaxIdResidenzaPerIdSoggetto(aIdSoggetto, "D");
//			lSqlDao.start();
//			if (lSqlDao.next()) {
//				lIdRes = lSqlDao.getIdResidenza();
//			}
//			lSqlDao.stop();
//
//		} catch (DAOException ex) {
//			throw new F3BException("ResidenzaController.ExInserisci: Non posso inserire: " + ex);
//		} finally {
//			cleanup(lSqlDao);
//			cleanup(lConn);
//		}
//
//		return lIdRes;
//	}

	/**
	 * Ricerca Residenza
	 * 
	 * @param aResidenza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaResidenza(ResidenzaModel aResidenza) throws F3BException {
		Connection lConn = null;
		Vector lResidenze = new Vector();
		ResidenzaSqlDAO lDao = null;
		try {
			lConn = getDBConnection();
			lDao = new ResidenzaSqlDAO(lConn);
			lDao.ricercaResidenza(aResidenza);
			lResidenze = new Vector(lDao.getModels());
			if (lResidenze.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ResidenzaController.ExRicercaResidenza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lResidenze;
	}

	/**
	 * Ricerca Residenze anche pregressi
	 * 
	 * @param aIdFascicolo
	 * @return lResidenze
	 * @throws F3BException
	 */
	public Vector ExRicercaResidenzeByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;
		Vector lResidenze = new Vector();
		ResidenzaSqlDAO lDao = null;
		try {
			lConn = getDBConnection();
			lDao = new ResidenzaSqlDAO(lConn);
			lDao.ricercaResidenzaByFascicolo(aIdFascicolo);
			lDao.start();

			lResidenze = new Vector();

			ResidenzaAssociataModel lResAssMod = null;
			ResidenzaModel lResMod = null;
			ResidenzaFascicoloSiepModel lResFasMod = null;
			while (lDao.next()) {
				lResAssMod = new ResidenzaAssociataModel();

				lResMod = (ResidenzaModel) lDao.getModel();
				lResFasMod = lDao.getModelResidenzaFascicoloSiep();

				lResAssMod.setResidenza(lResMod);
				lResAssMod.setResidenzaFascicoloSiep(lResFasMod);
				lResidenze.add(lResAssMod);
			}

			lDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("ResidenzaController.ExRicercaResidenzeByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lResidenze;
	}

	/**
	 * Ricerca Residenze e Domicili, anche pregressi
	 * 
	 * @param aIdFascicolo
	 * @return lResidenze
	 * @throws F3BException
	 */
	public Vector ExRicercaResidenzeDomiciliByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;
		Vector lResidenze = new Vector();
		ResidenzaSqlDAO lDao = null;
		try {
			lConn = getDBConnection();
			lDao = new ResidenzaSqlDAO(lConn);
			lDao.ricercaResidenzeDomiciliByFascicolo(aIdFascicolo);
			lDao.start();

			lResidenze = new Vector();

			ResidenzaAssociataModel lResAssMod = null;
			ResidenzaModel lResMod = null;
			ResidenzaFascicoloSiepModel lResFasMod = null;
			while (lDao.next()) {
				lResAssMod = new ResidenzaAssociataModel();

				lResMod = (ResidenzaModel) lDao.getModel();
				lResFasMod = lDao.getModelResidenzaFascicoloSiep();

				lResAssMod.setResidenza(lResMod);
				lResAssMod.setResidenzaFascicoloSiep(lResFasMod);
				lResidenze.add(lResAssMod);
			}

			lDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("ResidenzaController.ExRicercaResidenzeDomiciliByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lResidenze;
	}

	/**
	 * Ricerca Residenze e Domicili, anche pregressi
	 * 
	 * @param aIdFascicoloSius
	 * @return lResidenze
	 * @throws F3BException
	 */
	public Vector ExRicercaResidenzeDomiciliByIdFascicoloSius(BigDecimal aIdFascicoloSius)
			throws F3BException {
		Connection lConn = null;
		Vector lResidenze = new Vector();
		ResidenzaSqlDAO lDao = null;
		try {
			lConn = getDBConnection();
			lDao = new ResidenzaSqlDAO(lConn);
			lDao.ricercaResidenzeDomiciliByFascicoloSius(aIdFascicoloSius);
			lDao.start();

			lResidenze = new Vector();

			ResidenzaAssociataModel lResAssMod = null;
			ResidenzaModel lResMod = null;
			ResidenzaFascicoloSiusModel lResFasSius = null;
			while (lDao.next()) {
				lResAssMod = new ResidenzaAssociataModel();

				lResMod = (ResidenzaModel) lDao.getModel();
				lResFasSius = lDao.getModelResidenzaFascicoloSius();

				lResAssMod.setResidenza(lResMod);
				lResAssMod.setResidenzaFascicoloSius(lResFasSius);
				lResidenze.add(lResAssMod);
			}

			lDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("ResidenzaController.ExRicercaResidenzeDomiciliByIdFascicoloSius: "
					+ daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lResidenze;
	}

	public HashMap ExRicercaSiepResidenzeDomiciliByIdSoggetto(BigDecimal aIdSoggetto) throws F3BException {
		Connection lConn = null;

		HashMap lMap = new HashMap();

		ResidenzaSqlDAO lDao = null;

		try {
			lConn = getDBConnection();

			lDao = new ResidenzaSqlDAO(lConn);

			lDao.ricercaSiepResidenzeDomiciliByIdSoggetto(aIdSoggetto);

			lDao.start();

			ResidenzaAssociataModel lResAssMod = null;
			ResidenzaModel lResMod = null;
			ResidenzaFascicoloSiepModel lResFasMod = null;
			while (lDao.next()) {
				lResAssMod = new ResidenzaAssociataModel();

				lResMod = (ResidenzaModel) lDao.getModel();
				lResFasMod = lDao.getModelResidenzaFascicoloSiep();

				lResAssMod.setResidenza(lResMod);
				lResAssMod.setResidenzaFascicoloSiep(lResFasMod);

				if (lResMod.getCodTipoResidenza().equals("R"))
					lMap.put("RES", lResAssMod);
				else
					lMap.put("DOM", lResAssMod);
			}

			lDao.stop();

			if (lMap.isEmpty()) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Nessuna Residenza/Domicilio trovati per questo Soggetto");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ResidenzaController.ExRicercaSiepResidenzeDomiciliByIdSoggetto: " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lMap;
	}

	public ResidenzaModel ExModificaResidenza(ResidenzaModel aResidenza) throws F3BException {
		Connection lConn = null;

		ResidenzaDAO lResDao = null;

		try {
			lConn = getDBConnection();

			lResDao = new ResidenzaDAO(lConn);

			lResDao.setDAOFromModelForUpdate(aResidenza);

			// lResDao.selByKey();

			lResDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ResidenzaController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lResDao);
			cleanup(lConn);
		}

		return aResidenza;
	}

	public void ExCancellaResidenza(ResidenzaModel aResidenza) throws F3BException {
		Connection lConn = null;

		ResidenzaDAO lResDao = null;

		try {
			lConn = getDBConnection();

			lResDao = new ResidenzaDAO(lConn);
			lResDao.setCondizioneUpdate(aResidenza.getIdResidenza());
			lResDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("ResidenzaController.ExCancellaResidenza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lResDao);
			cleanup(lConn);
		}
	}

	public String ExInserisciResidenzaWithoutSequence(ResidenzaModel lResidenza, Connection lConn,
			BigDecimal lKeyFascicolo) throws F3BException {
		ResidenzaDAO lResDao = null;
		ResidenzaFascicoloSiepDAO lResFasDao = null;
		String lCodEsito = "00000";
		try {
			lResDao = new ResidenzaDAO(lConn);
			lResDao.setDAOFromModel(lResidenza);
			lResDao.setWithoutSequence(true);
			lResDao.insert();

			if (lResidenza != null) {
				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel(new Date(), null,
						lResidenza.getIdResidenza(), lKeyFascicolo);

				lResFasDao = new ResidenzaFascicoloSiepDAO(lConn);
				lResFasDao.setDAOFromModel(lResFasMod);
				lResDao.setWithoutSequence(true);
				lResFasDao.insert();
				lResFasDao.stop();
			}
		} catch (DAOException daoex) {
			if (daoex.UNIQUE_CONSTRAINT_VIOLATED) {
				// Inizializzo la chiave del fascicolo con quella inviatami
				// lChiave = lPars.getFascicolo().getIdFascicoloSiep();
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Residenza! ");
			}
		} finally {
			cleanup(lResDao);
			cleanup(lResFasDao);
		}

		return lCodEsito;
	}

	public String ExInserisciResidenzeWithoutSequence(ArrayList aResidenze, Connection lConn,
			BigDecimal lKeyFascicolo) throws F3BException {
//		ResidenzaModel lResMod = new ResidenzaModel();
		ResidenzaDAO lResDao = null;
		ResidenzaFascicoloSiepDAO lResFasDao = null;
		String lCodEsito = "00000";
		try {

			lResDao = new ResidenzaDAO(lConn);
			lResDao.setWithoutSequence(true);
			lResDao.insert();

			if (aResidenze != null && aResidenze.size() > 0) {
				for (int i = 0; i < aResidenze.size(); i++) {
					ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel(new Date(),
							null, ((ResidenzaModel) aResidenze.get(i)).getIdResidenza(), lKeyFascicolo);

					lResFasDao = new ResidenzaFascicoloSiepDAO(lConn);
					lResFasDao.setDAOFromModel(lResFasMod);
					lResDao.setWithoutSequence(true);
					lResFasDao.insert();
					lResFasDao.stop();
				}
			}
		} catch (DAOException daoex) {
			if (daoex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Residenza! ");
			}
		} finally {
			cleanup(lResDao);
			cleanup(lResFasDao);
		}
		return lCodEsito;
	}

	/**
	 * Effettua l'inserimento di un Elenco di una RESIDENZA <br>
	 *
	 * @param aResidenze
	 *            - Elenco Residenze
	 * @param lConn
	 *            - Connection parametro
	 * @return String - Rapporto Operazione
	 */
	public String ExInserisciResidenzeWithoutSequence(ArrayList aResidenze, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		ResidenzaDAO lResidenzaDao = null;
		ResidenzaFascicoloSiepDAO lResidenzaSiepDao = null;
		ResidenzaAssociataModel lResidenzaMod = null;

		try {
			lResidenzaDao = new ResidenzaDAO(lConn);

			if (aResidenze != null && aResidenze.size() > 0) {
				for (int i = 0; i < aResidenze.size(); i++) {
					lResidenzaMod = (ResidenzaAssociataModel) aResidenze.get(i);
					if (lResidenzaMod != null) {
						if (lResidenzaMod.getResidenza() != null
								&& lResidenzaMod.getResidenza().getIdResidenza() != null) {
							lResidenzaDao.setDAOFromModel(lResidenzaMod.getResidenza());
							lResidenzaDao.setWithoutSequence(true);
							lResidenzaDao.insert();
							lResidenzaDao.stop();
						}
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Residenza gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				// throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Residenza! ");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Impossibile inserire la Residenza! ");
			}
		} finally {
			cleanup(lResidenzaDao);
		}
		// 08/01/2007 Residenze SIEP caricate successivamente.
		try {
			lResidenzaSiepDao = new ResidenzaFascicoloSiepDAO(lConn);

			if (aResidenze != null && aResidenze.size() > 0) {
				for (int i = 0; i < aResidenze.size(); i++) {
					lResidenzaMod = (ResidenzaAssociataModel) aResidenze.get(i);
					if (lResidenzaMod != null) {
						if (lResidenzaMod.getResidenzaFascicoloSiep() != null
								&& lResidenzaMod.getResidenzaFascicoloSiep().getResIdResidenza() != null) {
							lResidenzaSiepDao.setDAOFromModel(lResidenzaMod.getResidenzaFascicoloSiep());
							lResidenzaSiepDao.setWithoutSequence(true);
							lResidenzaSiepDao.insert();
							lResidenzaSiepDao.stop();
						}
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Residenza SIEP gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error(ex.getMessage() + " Impossibile inserire la Residenza SIEP! ");
			}
		} finally {
			cleanup(lResidenzaSiepDao);
		}
		return lCodEsito;
	}

	/**
	 * Ricerca Residenza Sige. Nella lista dei domicili vengono caricati anche i domicili del soggetto
	 * associato al Fascicolo SIEP dal quale è stato inserito il Fascicolo Sige.
	 * 
	 * @param aResidenza
	 * @param idSoggettoSiep
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaResidenzaSige(ResidenzaModel aResidenza, BigDecimal idSoggettoSiep)
			throws F3BException {
		Connection lConn = null;
		Vector lResidenze = new Vector();
		ResidenzaSqlDAO lDao = null;
		try {
			lConn = getDBConnection();
			lDao = new ResidenzaSqlDAO(lConn);
			lDao.ricercaResidenzaSige(aResidenza, idSoggettoSiep);
			lResidenze = new Vector(lDao.getModels());
			if (lResidenze.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ResidenzaController.ExRicercaResidenzaSige: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lResidenze;
	}

}