package siap.siep.pagoPA.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaModel;
import siap.siep.SIEPException;
import siap.siep.pagoPA.dao.CivilmenteObbligatoDAO;
import siap.siep.pagoPA.dao.CivilmenteObbligatoSqlDAO;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;

/**
 * MEV_2023-13 
 * Title: CivilmenteObbligatoController 
 * Description: Classe Controller per la gestione del Civilmente Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public class CivilmenteObbligatoController extends SiapController implements ICivilmenteObbligato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public Vector<CivilmenteObbligatoModel> ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(
			BigDecimal fasSieIdFascicoloSiep) throws F3BException {

		Connection c = null;
		Vector<CivilmenteObbligatoModel> coms = new Vector<>();

		CivilmenteObbligatoSqlDAO cosdao = null;
		ResidenzaSqlDAO rsdao = null;

		try {
			c = getDBConnection();
			cosdao = new CivilmenteObbligatoSqlDAO(c);
			cosdao.ricercaCivilmenteObbligatiByFasSieIdFascicoloSiep(fasSieIdFascicoloSiep);
			cosdao.start();

			while (cosdao.next()) {
				CivilmenteObbligatoModel com = (CivilmenteObbligatoModel) cosdao.getModel();
				// Residenza/Domicilio
				rsdao = new ResidenzaSqlDAO(c);
				rsdao.ricercaDomicilioCorrenteByIdCivilmenteObbligato(com.getIdCivilmenteObbligato());
				com.setResidenza((ResidenzaModel) rsdao.getModelByKey());

				coms.add(com);
			}
			cosdao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CivilmenteObbligatoController.ExricercaCivilmenteObbligatiByFasSieIdFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"CivilmenteObbligatoController.ExricercaCivilmenteObbligatiByFasSieIdFascicoloSiep: "
							+ e);
		} finally {
			cleanup(cosdao);
			cleanup(rsdao);
			cleanup(c);
		}

		return coms;
	}

	public CivilmenteObbligatoModel ExInserisciCivilmenteObbligato(CivilmenteObbligatoModel com)
			throws F3BException {

		CivilmenteObbligatoModel comRet = null;
		ResidenzaDAO rdao = null;
		CivilmenteObbligatoDAO codao = null;
		Connection c = null;

		try {
			c = getDBConnection();

			comRet = new CivilmenteObbligatoModel(com);
			codao = new CivilmenteObbligatoDAO(c);

			// Inserimento Civilmente Obbligato sulla tabella CIVILMENTE_OBBLIGATO
			codao.setDAOFromModel(comRet);

			BigDecimal bd = null;
			bd = codao.insert();
			comRet.setIdCivilmenteObbligato(bd);

			// Inserimento sulla tabella RESIDENZA
			rdao = new ResidenzaDAO(c);
			comRet.getResidenza().setIdCivilmenteObbligato(bd);
			rdao.setDAOFromModel(comRet.getResidenza());
			rdao.insert();

			commit(c);
		} catch (F3BException fe) {
			rollback(c);
			throw fe;
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("CivilmenteObbligatoController.ExInserisciCivilmenteObbligato : " + ex);
		} catch (Exception e) {
			rollback(c);
			throw new F3BException("CivilmenteObbligatoController.ExInserisciCivilmenteObbligato : " + e);
		} finally {
			cleanup(codao);
			cleanup(rdao);
			cleanup(c);
		}

		return comRet;
	}

	public CivilmenteObbligatoModel ExRicercaCivilmenteObbligatoByKey(BigDecimal idCivilmenteObbligato)
			throws F3BException {

		Connection c = null;

		CivilmenteObbligatoModel com = new CivilmenteObbligatoModel();
		CivilmenteObbligatoSqlDAO cosdao = null;
		ResidenzaSqlDAO rsdao = null;

		try {

			c = getDBConnection();

			cosdao = new CivilmenteObbligatoSqlDAO(c);
			cosdao.ricercaCivilmenteObbligatoByKey(idCivilmenteObbligato);
			cosdao.start();
			while (cosdao.next()) {
				com = new CivilmenteObbligatoModel((CivilmenteObbligatoModel) cosdao.getModel());
				// Residenza/Domicilio
				rsdao = new ResidenzaSqlDAO(c);
				rsdao.ricercaDomicilioCorrenteByIdCivilmenteObbligato(com.getIdCivilmenteObbligato());
				com.setResidenza((ResidenzaModel) rsdao.getModelByKey());
			}
			cosdao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(
					"CivilmenteObbligatoController.ExRicercaCivilmenteObbligatoByKey: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(cosdao);
			cleanup(rsdao);
			cleanup(c);
		}

		return com;
	}

	public void ExModificaCivilmenteObbligato(CivilmenteObbligatoModel com) throws F3BException {

		Connection c = null;

		ResidenzaDAO rdao = null;
		CivilmenteObbligatoDAO codao = null;

		try {
			// EventoNotificaModel lEve = new EventoNotificaModel();

			// Prende una connessione in transazione.
			c = getDBTransaction();

			// Modifica Civilmente Obbligato
			// Aggiornamento sulla tabella Civilmente_Obbligato
			codao = new CivilmenteObbligatoDAO(c);
			codao.setDAOFromModelForUpdate(com);
			codao.update();
			codao.stop();

			rdao = new ResidenzaDAO(c);
			BigDecimal idResidenza = com.getResidenza().getIdResidenza();
			if (idResidenza != null) {
				// aggiorno la residenza
				rdao.setDAOFromModelForUpdate(com.getResidenza());
				rdao.update();
			} else {
				// inserisco la residenza
				rdao.setDAOFromModel(com.getResidenza());
				BigDecimal lSequence = rdao.insert();
				com.getResidenza().setIdResidenza(lSequence);
			}

			commit(c);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(c);
			throw new SIEPException("CivilmenteObbligatoController.ExModificaCivilmenteObbligato: " + daoEx);
		} catch (Exception e) {
			rollback(c);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new SIEPException("CivilmenteObbligatoController.ExModificaCivilmenteObbligato: " + e);
		} finally {
			cleanup(rdao);
			cleanup(codao);
			cleanup(c);
		}
	}

	public void ExCancellaCivilmenteObbligato(BigDecimal idCivilmenteObbligato) throws F3BException {

		Connection c = null;

		CivilmenteObbligatoDAO codao = null;
		ResidenzaDAO rdao = null;

		try {
			c = getDBTransaction();

			// Cancellazione della Residenza del Civilmente Obbligato
			// tabella RESIDENZA
			rdao = new ResidenzaDAO(c);
			rdao.selPerIdCivilmenteObbligato(idCivilmenteObbligato);
			rdao.delete();
			rdao.stop();

			// Cancellazione Civilmente Obbligato
			// tabella Civilmente_Obbligato
			codao = new CivilmenteObbligatoDAO(c);
			codao.selCondizioneDeleteByKey(idCivilmenteObbligato);
			codao.delete();
			codao.stop();

			commit(c);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CivilmenteObbligatoController.ExCancellaCivilmenteObbligato: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(codao);
			cleanup(rdao);
			cleanup(c);
		}
	}

}