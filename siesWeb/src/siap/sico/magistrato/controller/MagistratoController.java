package siap.sico.magistrato.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.magistrato.dao.MagistratoDAO;
import siap.sico.magistrato.dao.MagistratoSqlDAO;
import siap.sico.magistrato.dao.MagistratoWMagistratoSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.dao.MagistratoCompetenteSqlDAO;
import siap.sius.magistratorelatore.dao.MagistratoRelatoreSqlDAO;
import siap.sius.udienza.dao.UdienzaSqlDAO;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MagistratoController
 * </p>
 * <p>
 * Description: Classe Controller per Magistrato
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
public class MagistratoController extends SiapController implements IMagistrato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 *
	 * <p>
	 * 
	 * @param aMagistrato
	 * @return
	 * @throws F3BException
	 */
	public MagistratoModel ExInserisciMagistrato(MagistratoModel aMagistrato) throws F3BException {
		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		MagistratoModel lMagMod = null;

		try {
			lConn = getDBConnection();
			lMagMod = new MagistratoModel(aMagistrato);
			lMagDao = new MagistratoDAO(lConn);
			lMagDao.setDAOFromModel(aMagistrato);

			BigDecimal lKey = null;
			lKey = lMagDao.insert();

			commit(lConn);
			if (lKey == null) {
				/*
				 * F3BException e; e = new F3BException("chiave nulla ID_Magistrato"); throw e;
				 */
				lMagMod.setCodMagistrato(aMagistrato.getCodMagistrato());
			} else
				lMagMod.setCodMagistrato(lKey.toString());
		} catch (DAOException ex) {
			rollback(lConn);
			if (ex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SICOException(SICOException.USER_MESSAGE,
						"Inserimento impossibile: Magistrato già esistente.");
			throw new F3BException("MagistratoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return lMagMod;
	}

	/**
	 *
	 * <p>
	 * 
	 * @param aMagistrato
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMagistrato(MagistratoModel aMagistrato) throws F3BException {
		Connection lConn = null;
		Vector lMagistrati = new Vector();
		MagistratoSqlDAO lMagDao = null;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);
			lMagDao.ricercaMagistrato(aMagistrato);
			lMagistrati = new Vector(lMagDao.getModels());

			if (lMagistrati.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExRicercaMagistrato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagistrati;
	}

	public Vector ExRicercaMagistratoPaged(MagistratoModel aMagistrato, int aPage) throws F3BException {
		Connection lConn = null;
		Vector lMagistrati = new Vector();
		MagistratoSqlDAO lMagDao = null;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);
			lMagDao.ricercaMagistratoPaged(aMagistrato, aPage);
			lMagistrati = new Vector(lMagDao.getModels());

			if (lMagistrati.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExRicercaMagistratoPaged: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagistrati;
	}

	public BigDecimal ExGetCountMagistratiPaged(MagistratoModel aMagistrato) throws F3BException {
		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;
		MagistratoSqlDAO lMagDao = null;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);
			lMagDao.getCountMagistrati(aMagistrato);
			lMagDao.start();
			lMagDao.next();

			lCount = lMagDao.getBigDecimal("HowManyRecords");
			lMagDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExGetCountMagistratiPaged: " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 *
	 * <p>
	 * 
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
	 *             F3BException("MagistratoController.ExRicercaMagistratoByKey: Non posso leggere : " +
	 *             daoEx); } catch (SQLException sqe) { throw new
	 *             F3BException("MagistratoController.ExRicercaMagistratoByKey: Non posso leggere  : " + sqe);
	 *             } finally { cleanup(lMagDao); cleanup(lConn); }
	 * 
	 *             return lMagMod; }
	 */

	/**
	 *
	 * <p>
	 * 
	 * @param aCod
	 * @return
	 * @throws F3BException
	 */
	public MagistratoModel ExRicercaMagistratoByCod(String aCod) throws F3BException {
		Connection lConn = null;
		MagistratoSqlDAO lMagDao = null;
		MagistratoModel lMagMod;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);

			lMagDao.ricercaMagistratoByCod(aCod);
			lMagMod = (MagistratoModel) lMagDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExRicercaMagistratoByCod: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagMod;
	}

	public MagistratoModel ExRicercaMagistratoByFascicolo(BigDecimal aFascicolo) throws F3BException {
		Connection lConn = null;
		MagistratoSqlDAO lMagDao = null;
		MagistratoModel lMagMod;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);

			lMagDao.ricercaMagistratoByFascicolo(aFascicolo);
			lMagMod = (MagistratoModel) lMagDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MagistratoController.ExRicercaMagistratoByFascicolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return lMagMod;
	}

	/**
	 *
	 * <p>
	 * 
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMagistratoByCodUfficio(String aCodUfficio) throws F3BException {
		Connection lConn = null;
		MagistratoSqlDAO lMagDao = null;
		Vector lMagMods = null;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);

			lMagDao.ricercaMagistratoByCodUfficio(aCodUfficio);
			lMagMods = new Vector(lMagDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MagistratoController.ExRicercaMagistratoByCodUfficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagMods;
	}

	/**
	 *
	 * <p>
	 * 
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 */
	public Vector ExElencoCbxMagistratiByCodUfficio(String aCodUfficio) throws F3BException {
		Connection lConn = null;
		MagistratoSqlDAO lMagDao = null;
		Vector lMagDecMods = new Vector();

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);

			lMagDao.ricercaMagistratoByCodUfficio(aCodUfficio);
			lMagDao.start();

			// MEV10-s3: aggiunto campo vuoto in testa all'elenco
			lMagDecMods.add(new DecodeModel("-", "-"));

			// da Considerare l'implematazione di metodi getDecodeModel nel genricDAO.
			// .... penserò. Paolo
			while (lMagDao.next()) {
				String lCod = ((MagistratoModel) lMagDao.getModel()).getCodMagistrato();
				String lCognome = ((MagistratoModel) lMagDao.getModel()).getCognome();
				String lNome = ((MagistratoModel) lMagDao.getModel()).getNome();

				// STUB 03/12/2004 lMagDecMods.add(new DecodeModel( lCod, lCod + " - " + lCognome + " " +
				// lNome ) );
				lMagDecMods.add(new DecodeModel(lCod, lCognome + " " + lNome + " - " + lCod));
			}

			lMagDao.stop();

			// lMagMods = new Vector( lMagDao.getModels() );
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MagistratoController.ExElencoCbxMagistratiByCodUfficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagDecMods;
	}

	// RICERCA LISTA MAGISTRATO
	public Vector ExRicercaMagistratoByCognome(String aCognome, String aUfficio) throws F3BException {
		Connection lConn = null;
		Vector lMagistrati = new Vector();
		MagistratoWMagistratoSqlDAO lMaDao = null;

		try {
			lConn = getDBConnection();
			lMaDao = new MagistratoWMagistratoSqlDAO(lConn);
			lMaDao.ricercaMagistratoByCognome(aCognome, aUfficio);
			lMagistrati = new Vector(lMaDao.getModels());
			if (lMagistrati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}

		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExRicercaMagistratoByCognome: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lMaDao);
			cleanup(lConn);
		}

		return lMagistrati;
	}

	/**
	 *
	 * <p>
	 * 
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 */
	public Vector ExElencoCbxMagByCodComuneCodTipoUff(String aCodComune, String aCodTipoUfficio)
			throws F3BException {
		Connection lConn = null;
		MagistratoSqlDAO lMagDao = null;
		Vector lMagDecMods = new Vector();

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);
			lMagDao.ricercaMagByCodComuneCodTipoUff(aCodComune, aCodTipoUfficio);

			lMagDao.start();

			// MEV10-s3: aggiunto inserimento riga vuota per tipo ufficio
			if ("UDSM".equals(aCodTipoUfficio) || "TDSM".equals(aCodTipoUfficio))
				lMagDecMods.add(new DecodeModel("-", "-"));

			// da Considerare l'implematazione di metodi getDecodeModel nel genricDAO.
			// .... penserò. Paolo
			while (lMagDao.next()) {
				String lCod = ((MagistratoModel) lMagDao.getModel()).getCodMagistrato();
				String lCognome = ((MagistratoModel) lMagDao.getModel()).getCognome();
				String lNome = ((MagistratoModel) lMagDao.getModel()).getNome();

				// STUB 03/12/2004 lMagDecMods.add(new DecodeModel( lCod, lCod + " - " + lCognome + " " +
				// lNome ) );
				lMagDecMods.add(new DecodeModel(lCod, lCognome + " " + lNome + " - " + lCod));
			}

			lMagDao.stop();

			// lMagMods = new Vector( lMagDao.getModels() );
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MagistratoController.ExRicercaMagistratoByCodUfficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagDecMods;
	}

	/**
	 *
	 * <p>
	 * 
	 * @param aMagistrato
	 * @return
	 * @throws F3BException
	 */
	public MagistratoModel ExModificaMagistrato(MagistratoModel aMagistrato) throws F3BException {
		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		MagistratoModel lMagMod = new MagistratoModel(aMagistrato);

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoDAO(lConn);
			lMagDao.setDAOFromModelForUpdate(aMagistrato);
			lMagDao.update();
			// lMagDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("MagistratoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return lMagMod;
	}

	/**
	 *
	 * <p>
	 * 
	 * @param aMagistrato
	 * @throws F3BException
	 */
	public void ExCancellaMagistrato(String aCodMagistrato, String ACodUff) throws F3BException {
		Connection lConn = null;
		MagistratoDAO lMagDao = null;
		// 06/04/2007 Controllo cancellazione Magistrato solo se non assegnatario di procedimenti.
		MagistratoRelatoreSqlDAO lMagRelSqlDao = null;
		MagistratoCompetenteSqlDAO lMagCompSqlDao = null;
		UdienzaSqlDAO lUdienzaSqlDao = null;

		try {
			lConn = getDBConnection();

			// 06/04/2007 Controllo cancellazione Magistrato solo se non assegnatario di procedimenti.
			lMagRelSqlDao = new MagistratoRelatoreSqlDAO(lConn);
			lMagRelSqlDao.countMagRelByCodMagistrato(aCodMagistrato);
			lMagRelSqlDao.start();
			lMagRelSqlDao.next();
			// Preleva la count e verifica che sia == a zero.
			if (lMagRelSqlDao.getInt("COUNT") != 0)
				throw new SICOException(SICOException.USER_MESSAGE,
						"Cancellazione impossibile: Il Magistrato è assegnatario di procedimenti.");

			lMagCompSqlDao = new MagistratoCompetenteSqlDAO(lConn);
			lMagCompSqlDao.countMagCompByCodMagistrato(aCodMagistrato);
			lMagCompSqlDao.start();
			lMagCompSqlDao.next();
			// Preleva la count e verifica che sia == a zero.
			if (lMagCompSqlDao.getInt("COUNT") != 0)
				throw new SICOException(SICOException.USER_MESSAGE,
						"Cancellazione impossibile: Il Magistrato è assegnatario di procedimenti.");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Controllo collegamento MAGISTRATO ad UDIENZA");

			// Controllo collegamento ad UDIENZA
			lUdienzaSqlDao = new UdienzaSqlDAO(lConn);
			lUdienzaSqlDao.countMagUdiCodMagistrato(aCodMagistrato);
			lUdienzaSqlDao.start();
			lUdienzaSqlDao.next();
			// Preleva la count e verifica che sia == a zero.
			if (lUdienzaSqlDao.getInt("COUNT") != 0)
				throw new SICOException(SICOException.USER_MESSAGE,
						"Cancellazione impossibile: Il Magistrato è associato ad una Udienza.");

			lMagDao = new MagistratoDAO(lConn);
			lMagDao.setCondizioneUpdate(aCodMagistrato, ACodUff);
			lMagDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SICOException(SICOException.USER_MESSAGE,
						"Magistrato collegato ad altri dati. Impossibile effettuare la Cancellazione!");
			throw new F3BException("MagistratoController.ExCancellaMagistrato: " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lMagRelSqlDao);
			cleanup(lMagCompSqlDao);
			cleanup(lUdienzaSqlDao);
			cleanup(lConn);
		}
	}

	public String ExInserisciMagistratoWithoutSequence(MagistratoModel aMagistrato, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		MagistratoDAO lMagDao = null;
		// MagistratoModel lMagMod = null;
		try {
			lMagDao = new MagistratoDAO(lConn);

			if (aMagistrato != null && aMagistrato.getCodMagistrato() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info(" Magistrato da inserire = " + aMagistrato);
				lMagDao.setDAOFromModel(aMagistrato);
				lMagDao.setWithoutSequence(true);
				lMagDao.insert();
				lMagDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.warn("Magistrato gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.warn(F3BException.USER_MESSAGE + " Impossibile inserire il Magistrato! ");
			}
		} finally {
			cleanup(lMagDao);
		}
		return lCodEsito;
	}

	/**
   * 
   */
	public Vector ExRicercaMagistratoByCognomeUfficio(String aCognome, String aUfficio) throws F3BException {
		Connection lConn = null;
		Vector lMagistrati = new Vector();
		MagistratoWMagistratoSqlDAO lMagWSqlDao = null;

		try {
			lConn = getDBConnection();

			lMagWSqlDao = new MagistratoWMagistratoSqlDAO(lConn);

			lMagWSqlDao.ricercaMagistratoByCognomeCodUfficio(aCognome, aUfficio);

			lMagistrati = new Vector(lMagWSqlDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException(
					"MagistratoController.ExRicercaMagistratoByCognomeUfficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMagWSqlDao);
			cleanup(lConn);
		}

		return lMagistrati;
	}

	/**
	 * Ricerca magsitrato per id evento.
	 */
	public MagistratoModel ExRicercaMagistratoByEvento(BigDecimal aIdEvento) throws F3BException {
		Connection lConn = null;
		EventoDAO lEventoDao = null;
		// EventoModel lEventoModel = null;
		MagistratoSqlDAO lMagDao = null;
		MagistratoModel lMagMod;

		try {
			lConn = getDBConnection();
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setIdEvento(aIdEvento);
			lEventoDao.selByKey();
			// lEventoModel = (EventoModel) lEventoSqlDao.getModelByKey();
			lEventoDao.start();
			String lCodMagistrato = null;
			if (lEventoDao.next())
				lCodMagistrato = lEventoDao.getCodMagistrato();

			lMagDao = new MagistratoSqlDAO(lConn);
			lMagDao.ricercaMagistratoByCod(lCodMagistrato);
			lMagMod = (MagistratoModel) lMagDao.getModelByKey();

		} catch (DAOException daoEx) {
			throw new F3BException("MagistratoController.ExRicercaMagistratoByEvento: Non posso leggere : "
					+ daoEx);
		}
		/*
		 * catch (SQLException sqe) { throw new
		 * F3BException("MagistratoController.ExRicercaMagistratoByFascicolo: Non posso leggere  : " + sqe); }
		 */
		catch (Exception ex) {
			throw new F3BException("MagistratoController.ExRicercaMagistratoByEvento: Non posso leggere  : "
					+ ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return lMagMod;
	}

	/**
	 * Ricerca i Magistrati validi (DATA_FINE_VALIDITA == NULL oppure DATA_FINE_VALIDITA > SYSDATE)
	 * 
	 * @param aCodUfficio
	 * @return Elenco Magistrati validi
	 * @throws F3BException
	 */
	public Vector ExElencoCbxMagistratiValidiByCodUfficio(String aCodUfficio) throws F3BException {
		Connection lConn = null;
		MagistratoSqlDAO lMagDao = null;
		Vector lMagDecMods = new Vector();

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoSqlDAO(lConn);

			lMagDao.ricercaMagistratiValidiByCodUfficio(aCodUfficio);
			lMagDao.start();
			
			// MEV10-s3: aggiunto campo vuoto in testa all'elenco
			lMagDecMods.add(new DecodeModel("-", "-"));
						
			while (lMagDao.next()) {
				String lCod = ((MagistratoModel) lMagDao.getModel()).getCodMagistrato();
				String lCognome = ((MagistratoModel) lMagDao.getModel()).getCognome();
				String lNome = ((MagistratoModel) lMagDao.getModel()).getNome();
				lMagDecMods.add(new DecodeModel(lCod, lCognome + " " + lNome + " - " + lCod));
			}

			lMagDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"MagistratoController.ExElencoCbxMagistratiValidiByCodUfficio: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagDecMods;
	}

}