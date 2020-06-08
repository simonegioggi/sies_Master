package siap.sige.magistratoassegnatario.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.dao.MagistratoSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.SIGEException;
import siap.sige.magistratoassegnatario.dao.MagistratoAssegnatarioDAO;
import siap.sige.magistratoassegnatario.dao.MagistratoAssegnatarioSqlDAO;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;

/**
 * <p>
 * Title: MagistratoAssegnatarioController
 * </p>
 * <p>
 * Description: Classe Controller per MagistratoAssegnatario
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
public class MagistratoAssegnatarioController extends SiapController implements IMagistratoAssegnatario {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public MagistratoAssegnatarioModel ExInserisciMagistratoAssegnatario(
			MagistratoAssegnatarioModel aMagistratoAssegnatario) throws F3BException {

		Connection lConn = null;
		MagistratoAssegnatarioDAO lMagDao = null;
		MagistratoAssegnatarioModel lMagChiuso = new MagistratoAssegnatarioModel();

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoAssegnatarioDAO(lConn);

			// Chiusura di eventuale Magistrato già collegato al fascicolo

			lMagChiuso.setFasSigeIdFascicoloSige(aMagistratoAssegnatario.getFasSigeIdFascicoloSige());
			lMagChiuso.setDataFine(aMagistratoAssegnatario.getDataInizio());
			lMagChiuso.setCodOperatoreAggiornamento(aMagistratoAssegnatario.getCodOperatoreInserimento());
			lMagChiuso.setCodUfficioAggiornamento(aMagistratoAssegnatario.getCodUfficioInserimento());
			lMagChiuso.setDataAggiornamento(aMagistratoAssegnatario.getDataInserimento());

			lMagDao.setDAOFromModelForChiusura(lMagChiuso);
			lMagDao.update();
			lMagDao.stop();

			// Inserimento
			lMagDao.setDAOFromModel(aMagistratoAssegnatario);
			lMagDao.insert();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("DAOException: " + ex);
			throw new F3BException("MagistratoAssegnatarioController.ExInserisci: DAOException -> " + ex);
		} catch (Exception e) {
			rollback(lConn);
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + e);
			throw new F3BException(
					"MagistratoAssegnatarioController.ExInserisciMagistratoAssegnatario: " + e);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return aMagistratoAssegnatario;
	}

	/**
	 * Ricerca il Magistrato Assegnatario corrente (ovvero con data fine a null ) assegnato ad un determinato
	 * Fascicolo SIGE.
	 *
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public MagistratoAssegnatarioModel ExRicercaMagAssCorrenteXFascicolo(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;
		MagistratoAssegnatarioDAO lMagDao = null;
		MagistratoAssegnatarioModel lMagAssegnatario = null;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoAssegnatarioDAO(lConn);
			lMagDao.setCondizioneAttivo(aIdFascicolo);
			lMagAssegnatario = (MagistratoAssegnatarioModel) lMagDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MagistratoAssegnatarioController.ExRicercaMagAssCorrenteXFascicolo: " + daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"MagistratoAssegnatarioController.ExRicercaMagAssCorrenteXFascicolo: " + e);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}
		return lMagAssegnatario;
	}

	/**
	 * Esegue la ricerca del magistrato Assegnatario corrente per id fascicolo.
	 * <p>
	 * Se il Magistrato Assegnatario viene trovato si effettua anche la ricerca del Magistrato.
	 * <p>
	 *
	 * @param aKey
	 *            id del fascicolo.
	 * @return model aggregato con i dati del magistrato relatore e quelli del magistrato o dell'esperto.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MagistratoAssegnatarioModel ExRicercaEstesaMagAssCorrenteXFascicolo(BigDecimal aKey)
			throws F3BException {

		MagistratoAssegnatarioModel lMagAss = null;

		// Magistrato Assegnatario
		lMagAss = ExRicercaMagAssCorrenteXFascicolo(aKey);

		if (lMagAss != null) {
			// Magistrato
			if (lMagAss.getMagCodMagistrato() != null) {
				IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
				lMagAss.setMagistrato(lMagCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato()));
			}
		}
		return lMagAss;
	}

	public MagistratoAssegnatarioModel ExInserisciAggiornaMagistratoAssegnatario(
			MagistratoAssegnatarioMagistratoModel aMagistratoAssegnatarioMagistrato) throws F3BException {

		Connection lConn = null;

		MagistratoAssegnatarioDAO lMagAssDao = null;
		MagistratoAssegnatarioModel lMagMod = null;
		MagistratoAssegnatarioModel lNuovoMagMod = null;
		MagistratoSqlDAO lMagDao = null;
		MagistratoAssegnatarioSqlDAO lMagSqlDAO = null;

		try {
			lConn = getDBTransaction();

			// se non è stata richiesta nessuna variazione
			// controllo spostato nella jsp
			// if(aMagistratoAssegnatarioMagistrato.getMagistrato().getCodMagistrato().equals(aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario().getMagCodMagistrato()))
			// {
			// throw new F3BException(F3BException.USER_MESSAGE,"Nessuna Variazione Richiesta");
			// }

			// ricerca magistrato esistenete

			lMagSqlDAO = new MagistratoAssegnatarioSqlDAO(lConn);
			lMagSqlDAO.ricercaMagistratoEsistente(aMagistratoAssegnatarioMagistrato);
			lMagMod = (MagistratoAssegnatarioModel) lMagSqlDAO.getModelByKey();
			lMagAssDao = new MagistratoAssegnatarioDAO(lConn);
			if (lMagMod != null) {
				// chiude il magistrato precedente
				lMagAssDao.setDataFine(DateUtils.getSysDate());
				lMagAssDao.setCodOperatoreAggiornamento(aMagistratoAssegnatarioMagistrato
						.getMagistratoAssegnatario().getCodOperatoreInserimento());
				lMagAssDao.setCodUfficioAggiornamento(aMagistratoAssegnatarioMagistrato
						.getMagistratoAssegnatario().getCodUfficioInserimento());
				lMagAssDao.setDataAggiornamento(DateUtils.getSysDate());
				lMagAssDao.setCondizioneUpdate(lMagMod.getFasSigeIdFascicoloSige(),
						lMagMod.getMagCodMagistrato());
				lMagAssDao.update();
				lMagAssDao.stop();
			}

			lMagAssDao.setDAOFromModel(aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario());
			lMagAssDao.insert();

			lNuovoMagMod = aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);

			if (ex.getMessage().indexOf("MAG_ASS_PK1") != -1)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile reinserire lo stesso Magistrato con stessa data !");

			throw new F3BException(
					"MagistratoAssegnatarioController.ExInserisciAggiornaMagistratoAssegnatario: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + e);
			throw new F3BException(
					"MagistratoAssegnatarioController.ExInserisciAggiornaMagistratoAssegnatario: " + e);
		} finally {
			cleanup(lMagAssDao);
			cleanup(lMagDao);
			cleanup(lMagSqlDAO);
			cleanup(lConn);
		}
		return lNuovoMagMod;
	}

	/**
	 * Ritorna il vettore di MagistratoAssegnatarioModel riferite al Fascicolo SIGE in esame
	 *
	 * @param aKeyFascicolo
	 * @return Vector di ResidenzaAssociataModel
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector ExRicercaMagistratoByProcedimentoSige(BigDecimal aKeyFascicolo, String codUffUtenteConnesso)
			throws F3BException {

		Connection lConn = null;

		Vector lMagistrati = new Vector();

		MagistratoAssegnatarioSqlDAO lDao = null;

		try {
			lConn = getDBConnection();

			lDao = new MagistratoAssegnatarioSqlDAO(lConn);

			lDao.ricercaMagistratoAssegnatarioByFascicolo(aKeyFascicolo, codUffUtenteConnesso);

			lDao.start();

			MagistratoAssegnatarioMagistratoModel lMagAssMagMod = null;
			MagistratoModel lMagMod = null;
			MagistratoAssegnatarioModel lMagAssMod = null;
			while (lDao.next()) {
				lMagAssMagMod = new MagistratoAssegnatarioMagistratoModel();
				// lMagMod = (MagistratoModel)lDao.getModel();
				lMagMod = lDao.getModelMagistrato();
				lMagAssMod = lDao.getModelMagistratoAssegnatario();
				lMagAssMagMod.setMagistrato(lMagMod);
				lMagAssMagMod.setMagistratoAssegnatario(lMagAssMod);
				lMagistrati.add(lMagAssMagMod);

			}
			lDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIGEException(F3BException.USER_MESSAGE,
					"MagistratoAssegnatarioController.ExRicercaMagistratoByProcedimentoSige: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lMagistrati;
	}

	/**
	 * Metodo che aggiorna il record su magistrato_Assegnatario per idFascSIGE
	 *
	 * @param aMagistratoAssegnatarioMagistrato
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public MagistratoAssegnatarioModel ExAggiornaMagistratoAssegnatarioXFascicolo(
			MagistratoAssegnatarioMagistratoModel aMagistratoAssegnatarioMagistrato, String flagBlocco)
			throws F3BException {

		Connection lConn = null;

		MagistratoAssegnatarioDAO lMagAssDao = null;
		MagistratoAssegnatarioModel lMagMod = null;
		MagistratoAssegnatarioModel lNuovoMagMod = null;
		MagistratoSqlDAO lMagDao = null;
		MagistratoAssegnatarioSqlDAO lMagSqlDAO = null;

		try {
			lConn = getDBTransaction();

			// ricerca magistrato esistenete per aggiornare il codProcuratore e idAssistente(cancelliere)

			lMagSqlDAO = new MagistratoAssegnatarioSqlDAO(lConn);
			lMagSqlDAO.ricercaMagistratoEsistente(aMagistratoAssegnatarioMagistrato);
			lMagMod = (MagistratoAssegnatarioModel) lMagSqlDAO.getModelByKey();
			lMagAssDao = new MagistratoAssegnatarioDAO(lConn);
			if (lMagMod != null) {

				// lMagAssDao.setCodOperatoreAggiornamento(aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario().getCodOperatoreInserimento());
				// lMagAssDao.setCodUfficioAggiornamento(aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario().getCodUfficioInserimento());
				// lMagAssDao.setDataAggiornamento(DateUtils.getSysDate());
				if (aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario()
						.getMagCodMagistrato() != null
						&& !"".equals(aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario()
								.getMagCodMagistrato()))
					lMagAssDao.setMagCodMagistrato(aMagistratoAssegnatarioMagistrato
							.getMagistratoAssegnatario().getMagCodMagistrato());
				else
					lMagAssDao.setMagCodMagistrato(lMagMod.getMagCodMagistrato());

				if (aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario().getCodProcuratore() != null)
					lMagAssDao.setCodProcuratore(aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario()
							.getCodProcuratore());
				if (aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario().getIdAssistente() != null)
					lMagAssDao.setCodIdAssistente(
							aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario().getIdAssistente());

				lMagAssDao.setCondizioneUpdateExtend(lMagMod.getFasSigeIdFascicoloSige(),
						lMagMod.getMagCodMagistrato(), flagBlocco);
				lMagAssDao.update();
				lMagAssDao.stop();
				commit(lConn);
			}
			lNuovoMagMod = aMagistratoAssegnatarioMagistrato.getMagistratoAssegnatario();
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: " + ex);

			throw new F3BException(
					"MagistratoAssegnatarioController.ExAggiornaMagistratoAssegnatario: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			siesLogger.error("SQLException: " + e);
			throw new F3BException("MagistratoAssegnatarioController.ExAggiornaMagistratoAssegnatario: " + e);
		} finally {
			cleanup(lMagAssDao);
			cleanup(lMagDao);
			cleanup(lMagSqlDAO);
			cleanup(lConn);
		}
		return lNuovoMagMod;
	}

}