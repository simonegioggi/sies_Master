package siap.siep.modulocumulo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.controller.SiapController;
import siap.siep.modulocumulo.dao.BeneficioCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ComputiCumuloSqlDAO;
import siap.siep.modulocumulo.dao.LibAnticipataCumuloSqlDAO;
import siap.siep.modulocumulo.dao.MisuraSicurezzaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PeriodoLibAntCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ProvvedimentoGeSorvCumDAO;
import siap.siep.modulocumulo.dao.ProvvedimentoGeSorvCumSqlDAO;
import siap.siep.modulocumulo.dao.ReatoCumuloSqlDAO;
import siap.siep.modulocumulo.dao.RichPMBeneficioCumDAO;
import siap.siep.modulocumulo.dao.RichPMMisSicCumSqlDAO;
import siap.siep.modulocumulo.dao.RichPMMisSicurCumDAO;
import siap.siep.modulocumulo.dao.RichPMPenAccCumDAO;
import siap.siep.modulocumulo.dao.RichPMPenAccCumSqlDAO;
import siap.siep.modulocumulo.dao.RichPMReatoCumDAO;
import siap.siep.modulocumulo.dao.RichPMSanzioneSostCumDAO;
import siap.siep.modulocumulo.dao.RichPMStatoEsecCumDAO;
import siap.siep.modulocumulo.dao.RichPMTitoloCumDAO;
import siap.siep.modulocumulo.dao.RichPMTitoloCumSqlDAO;
import siap.siep.modulocumulo.dao.RichiesteInviateCumDAO;
import siap.siep.modulocumulo.dao.RichiesteInviateCumSqlDAO;
import siap.siep.modulocumulo.dao.RichiestePmInCumuloDAO;
import siap.siep.modulocumulo.dao.RichiestePmInCumuloSqlDAO;
import siap.siep.modulocumulo.dao.SanzioneSostitutivaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.StatoEsecTitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.PeriodoLibAntCumuloModel;
import siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel;
import siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.RichPMBeneficioCumModel;
import siap.siep.modulocumulo.model.RichPMMisSicCumModel;
import siap.siep.modulocumulo.model.RichPMPenAccCumModel;
import siap.siep.modulocumulo.model.RichPMReatoCumModel;
import siap.siep.modulocumulo.model.RichPMSanSostCumModel;
import siap.siep.modulocumulo.model.RichPMStatoEsecCumModel;
import siap.siep.modulocumulo.model.RichPMTitoloCumModel;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils;

/**
 * RichiestePmInCumuloController - Classe Controller per RichiestePmInCumulo
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RichiestePmInCumuloController extends SiapController implements IRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua l'inserimento di un RichiestePmInCumulo a partire dai dati contenuti nel Model
	 *
	 * @param aRichiestePmInCumulo
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo(
			RichiestePmInCumuloModel aRichiestePmInCumulo) throws F3BException {

		Connection lConn = null;
		RichiestePmInCumuloDAO lRicDao = null;
		RichiestePmInCumuloModel lRicMod = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicDao.setDAOFromModel(aRichiestePmInCumulo);
			BigDecimal lSequence = lRicDao.insert();
			commit(lConn);
			lRicMod = new RichiestePmInCumuloModel(aRichiestePmInCumulo);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestePmInCumulo(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("RichiestePmInCumuloController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}

		return lRicMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati RichiestePmInCumulo
	 *
	 * @param aRichiestePmInCumulo
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaRichiestePmInCumulo(RichiestePmInCumuloModel aRichiestePmInCumulo)
			throws F3BException {

		Connection lConn = null;
		Vector lRichiestePmInCumuli = new Vector();
		RichiestePmInCumuloDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicDao.setCondizioni(aRichiestePmInCumulo);
			lRicDao.setOrderBy();
			lRicDao.start();
			while (lRicDao.next()) {
				lRichiestePmInCumuli.add(lRicDao.getModel());
			}
			lRicDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaRichiestePmInCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}

		return lRichiestePmInCumuli;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public RichiestePmInCumuloModel ExRicercaRichiestePmInCumuloById(BigDecimal aIdRichiestePmInCumulo)
			throws F3BException {

		Connection lConn = null;
		RichiestePmInCumuloSqlDAO lRichiestePmInCumuloSqlDao = null;
		RichiesteInviateCumSqlDAO lRicInvSqlDao = null;
		ProvvedimentoGeSorvCumSqlDAO lProvvSqlDao = null;
		RichPMPenAccCumSqlDAO lRicPASqlDao = null;
		RichPMMisSicCumSqlDAO lRicMSSqlDao = null;

		RichiestePmInCumuloModel lRichiestePmInCumuloMod = new RichiestePmInCumuloModel();

		try {
			lConn = getDBConnection();
			lRichiestePmInCumuloSqlDao = new RichiestePmInCumuloSqlDAO(lConn);
			lRichiestePmInCumuloSqlDao.ricercaRichiestePmInCumuloByKey(aIdRichiestePmInCumulo);
			lRichiestePmInCumuloMod = (RichiestePmInCumuloModel) lRichiestePmInCumuloSqlDao.getModelByKey();

			// Ricerca di una Eventuale Richiesta_Inviata
			lRicInvSqlDao = new RichiesteInviateCumSqlDAO(lConn);
			RichiesteInviateCumModel lRicInvMod = null;
			if (lRichiestePmInCumuloMod != null
					&& lRichiestePmInCumuloMod.getRicIdRichiesteInviateCum() != null) {
				lRicInvSqlDao.ricercaRichiesteInviateCumByKey(
						lRichiestePmInCumuloMod.getRicIdRichiesteInviateCum());
				lRicInvMod = (RichiesteInviateCumModel) lRicInvSqlDao.getModelByKey();

				if (lRicInvMod != null && lRicInvMod.getIdRichiesteInviateCum() != null)
					lRichiestePmInCumuloMod.setRichiesteInviateCum(lRicInvMod);
			}
			// Ricerca di una Eventuale Decisione di GE/SORV
			lProvvSqlDao = new ProvvedimentoGeSorvCumSqlDAO(lConn);
			ProvvedimentoGeSorvCumModel lProvvMod = null;
			lProvvSqlDao.ricercaProvvedimentoGeSorvCumByIdRichiesta(
					lRichiestePmInCumuloMod.getIdRichiestePmInCumulo());
			lProvvMod = (ProvvedimentoGeSorvCumModel) lProvvSqlDao.getModelByKey();

			if (lProvvMod != null && lProvvMod.getIdProvvedimentoGeSorvCum() != null)
				lRichiestePmInCumuloMod.setDecisioneGeSorvCum(lProvvMod);

			// Ricerca di Eventuali Condoni scaricati in Decisione sulle tabelle di Relazione (Solo per
			// Richiesta Applicazione Benefici)
			lRicPASqlDao = new RichPMPenAccCumSqlDAO(lConn);
			lRicMSSqlDao = new RichPMMisSicCumSqlDAO(lConn);

			if (lRichiestePmInCumuloMod != null && lRichiestePmInCumuloMod.getCodTipoAnnotazione() != null
					&& (lRichiestePmInCumuloMod.getCodTipoAnnotazione().equals("002")
							|| lRichiestePmInCumuloMod.getCodTipoAnnotazione().equals("003"))) {
				// RichPm_MisSicur_Cum
				lRicMSSqlDao.ricercaRichPmMisSicCumByRichIdRich(aIdRichiestePmInCumulo);
				Vector<RichPMMisSicCumModel> VecMS = new Vector<RichPMMisSicCumModel>(
						lRicMSSqlDao.getModels());

				if (VecMS != null && VecMS.size() > 0)
					lRichiestePmInCumuloMod.setListaRichPmMisuraSicurezzaCum(VecMS);

				// RichPm_PenAcc_Cum
				lRicPASqlDao.ricercaRichPmPenAccCumByRichIdRich(aIdRichiestePmInCumulo);
				Vector<RichPMPenAccCumModel> VecPA = new Vector<RichPMPenAccCumModel>(
						lRicPASqlDao.getModels());

				if (VecPA != null && VecPA.size() > 0)
					lRichiestePmInCumuloMod.setListaRichPmPenaAccessoriaCum(VecPA);
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaRichiestePmInCumuloById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRichiestePmInCumuloSqlDao);
			cleanup(lRicInvSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lProvvSqlDao);
			cleanup(lRicPASqlDao);
			cleanup(lRicMSSqlDao);
			cleanup(lConn);
		}

		return lRichiestePmInCumuloMod;
	}

	/**********************************************************************************
	 * Metodo che modifica i dati delle RichiestePmInCumulo Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aRichiestePmInCumulo
	 *            Model con i nuovi valori
	 * @throws F3BException
	 **********************************************************************************/
	public void ExModificaRichiestePmInCumulo(RichiestePmInCumuloModel aRichiestePmInCumulo)
			throws F3BException {

		Connection lConn = null;
		RichiestePmInCumuloDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicDao.setDAOFromModelForUpdate(aRichiestePmInCumulo);
			lRicDao.selCondizioneUpdate(aRichiestePmInCumulo.getIdRichiestePmInCumulo());
			lRicDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.debug("DAOException: ", ex);
			throw new F3BException(
					"RichiestePmInCumuloController.ExModificaRichiestePmInCumulo: Non posso inserire/Modificare: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
	}

	/**********************************************************************************
	 * Metodo che modifica i dati delle RichiestePmInCumulo e della relativa decisione Viene fatto l'update di
	 * tutti i campi del record recuperando i valori dal Model Se mancano dati nel model i corrispondenti
	 * valori della tabella verranno impostati a null
	 *
	 * @param aRichiestePmInCumulo
	 *            Model con i nuovi valori
	 * @throws F3BException
	 **********************************************************************************/
	public void ExModificaRichiestaEProvvPmInCumulo(RichiestePmInCumuloModel aRichiestePmInCumulo,
			String[] listaIdMisSic, String[] listaIdPeneAcc) throws F3BException {

		Connection lConn = null;
		RichiestePmInCumuloDAO lRicDao = null;
		ProvvedimentoGeSorvCumDAO lProvDao = null;
		ProvvedimentoGeSorvCumSqlDAO lProvSqlDao = null;
		RichPMPenAccCumDAO lRicPADao = null;
		RichPMMisSicurCumDAO lRicMSDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicDao.setDAOFromModelForUpdate(aRichiestePmInCumulo);
			lRicDao.selCondizioneUpdate(aRichiestePmInCumulo.getIdRichiestePmInCumulo());
			lRicDao.update();

			lProvSqlDao = new ProvvedimentoGeSorvCumSqlDAO(lConn);
			lProvDao = new ProvvedimentoGeSorvCumDAO(lConn);
			ProvvedimentoGeSorvCumModel lProvGeSorv = aRichiestePmInCumulo.getDecisioneGeSorvCum();

			// Se esiste un provvedimento per la richiesta va aggiornato.
			lProvSqlDao.ricercaProvvedimentoGeSorvCumByIdRichiesta(
					aRichiestePmInCumulo.getIdRichiestePmInCumulo());
			lProvSqlDao.start();
			if (lProvSqlDao.next()) {
				ProvvedimentoGeSorvCumModel lProvOld = (ProvvedimentoGeSorvCumModel) lProvSqlDao
						.getModelByKey();
				lProvGeSorv.setCodUfficioAggiornamento(aRichiestePmInCumulo.getCodUfficioAggiornamento());
				lProvGeSorv.setCodOperatoreAggiornamento(aRichiestePmInCumulo.getCodOperatoreAggiornamento());
				lProvGeSorv.setDataAggiornamento(aRichiestePmInCumulo.getDataAggiornamento());
				lProvDao.setDAOFromModelForUpdate(lProvGeSorv);
				lProvDao.selCondizioneUpdate(lProvOld.getIdProvvedimentoGeSorvCum());
				lProvDao.update();
			} else {
				lProvGeSorv.setRicIdRichiestePmInCumulo(aRichiestePmInCumulo.getIdRichiestePmInCumulo());
				lProvGeSorv.setCodUfficioInserimento(aRichiestePmInCumulo.getCodUfficioAggiornamento());
				lProvGeSorv.setCodOperatoreInserimento(aRichiestePmInCumulo.getCodOperatoreAggiornamento());
				lProvGeSorv.setDataInserimento(aRichiestePmInCumulo.getDataAggiornamento());
				lProvGeSorv.toString();

				lProvDao.setDAOFromModel(lProvGeSorv);
				lProvDao.insert();
			}

			lRicPADao = new RichPMPenAccCumDAO(lConn);
			lRicMSDao = new RichPMMisSicurCumDAO(lConn);

			// Modifica tabella Relazione RICHIESTE_PM_IN_CUMULO / PENA_ACCESSORIA_CUMULO
			// Prima passata: tutti i flag a N
			lRicMSDao.setFlagCondono("N");
			lRicMSDao.selCondizioneUpdate(aRichiestePmInCumulo.getIdRichiestePmInCumulo());
			lRicMSDao.update();
			lRicMSDao.stop();
			// Seconda passata: Solo x quelli condonati, flag a S
			if (listaIdMisSic != null && listaIdMisSic.length > 0) {
				for (int im = 0; im < listaIdMisSic.length; im++) {
					lRicMSDao.setFlagCondono("S");
					lRicMSDao.selCondizioneUpdateFlagCondono(aRichiestePmInCumulo.getIdRichiestePmInCumulo(),
							new BigDecimal(listaIdMisSic[im]));
					lRicMSDao.update();
					lRicMSDao.stop();
				}
			}

			// Modifica tabella Relazione RICHIESTE_PM_IN_CUMULO / PENA_ACCESSORIA_CUMULO
			// Prima passata: tutti i flag a N
			lRicPADao.setFlagCondono("N");
			lRicPADao.selCondizioneUpdate(aRichiestePmInCumulo.getIdRichiestePmInCumulo());
			lRicPADao.update();
			lRicPADao.stop();
			// Seconda passata: Solo x quelli condonati, flag a S
			if (listaIdPeneAcc != null && listaIdPeneAcc.length > 0) {
				for (int ip = 0; ip < listaIdPeneAcc.length; ip++) {
					lRicPADao.setFlagCondono("S");
					lRicPADao.selCondizioneUpdateFlagCondono(aRichiestePmInCumulo.getIdRichiestePmInCumulo(),
							new BigDecimal(listaIdPeneAcc[ip]));
					lRicPADao.update();
					lRicPADao.stop();
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.debug("DAOException: ", ex);
			throw new F3BException(
					"RichiestePmInCumuloController.ExModificaRichiestaEProvvPmInCumulo: Non posso inserire/Modificare: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lProvDao);
			cleanup(lProvSqlDao);
			cleanup(lRicPADao);
			cleanup(lRicMSDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aRichiestePmInCumulo
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaRichiestePmInCumulo(RichiestePmInCumuloModel aRichiestePmInCumulo)
			throws F3BException {

		Connection lConn = null;
		RichiestePmInCumuloDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicDao.selCondizioneUpdate(aRichiestePmInCumulo.getIdRichiestePmInCumulo());
			lRicDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExCancellaRichiestePmInCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record e dei Record di Tutte le Tabelle di Correlazione
	 *
	 * @param Key
	 *            RichiestePmInCumulo
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaRichiestePmInCumuloFull(BigDecimal aIdRich) throws F3BException {

		Connection lConn = null;
		RichiestePmInCumuloDAO lRicDao = null;
		RichPMTitoloCumDAO lRicPmTitoDao = null;
		RichPMMisSicurCumDAO lRicPmMisSicDao = null;
		RichPMPenAccCumDAO lRicPmPenAccDao = null;
		RichPMReatoCumDAO lRicPmReaDao = null;
		RichPMStatoEsecCumDAO lRicPmSECDao = null;
		RichPMBeneficioCumDAO lRicBenCumDao = null;
		RichPMSanzioneSostCumDAO lRicSSCumDao = null;
		ProvvedimentoGeSorvCumDAO lProvvDao = null;

		try {
			lConn = getDBConnection();
			//
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lProvvDao = new ProvvedimentoGeSorvCumDAO(lConn);
			lRicPmTitoDao = new RichPMTitoloCumDAO(lConn);
			lRicPmMisSicDao = new RichPMMisSicurCumDAO(lConn);
			lRicPmPenAccDao = new RichPMPenAccCumDAO(lConn);
			lRicPmReaDao = new RichPMReatoCumDAO(lConn);
			lRicPmSECDao = new RichPMStatoEsecCumDAO(lConn);
			// lRicBenCumDao = new RichPMBeneficioCumDAO(lConn);
			lRicBenCumDao = new RichPMBeneficioCumDAO(lConn);
			lRicSSCumDao = new RichPMSanzioneSostCumDAO(lConn);

			// Tabella PROVVEDIMENTO_GE_SORV_CUM
			lProvvDao.selCondizioneByIdRichiesta(aIdRich);
			lProvvDao.delete();
			lProvvDao.stop();

			// Tabella RICHPM_STATO_ESEC_CUM
			lRicPmSECDao.selCondizioneUpdate(aIdRich);
			lRicPmSECDao.delete();
			lRicPmSECDao.stop();

			// Tabella RICHPM_TITOLO_CUM
			lRicPmTitoDao.selCondizioneUpdate(aIdRich);
			lRicPmTitoDao.delete();
			lRicPmTitoDao.stop();

			// Tabella RICHPM_REATO_CUM
			lRicPmReaDao.selCondizioneUpdate(aIdRich);
			lRicPmReaDao.delete();
			lRicPmReaDao.stop();

			// Tabella RICHPM_MISSICUR_CUM
			lRicPmMisSicDao.selCondizioneUpdate(aIdRich);
			lRicPmMisSicDao.delete();
			lRicPmMisSicDao.stop();

			// Tabella RICHPM_BENEFICIO_CUM
			// lRicBenCumDao.selCondizioneUpdate(aIdRich);
			// lRicBenCumDao.delete();
			// lRicBenCumDao.stop();

			// Tabella RICHPM_BENEFICIO_CUM
			lRicBenCumDao.selCondizioneUpdate(aIdRich);
			lRicBenCumDao.delete();
			lRicBenCumDao.stop();

			// Tabella RICHPM_PENACC_CUM
			lRicPmPenAccDao.selCondizioneUpdate(aIdRich);
			lRicPmPenAccDao.delete();
			lRicPmPenAccDao.stop();

			// Tabella RICHPM_SANZIONE_SOST_CUM
			lRicSSCumDao.selCondizioneUpdate(aIdRich);
			lRicSSCumDao.delete();
			lRicSSCumDao.stop();

			// Tabella Principale: RICHIESTE_PM_IN_CUMULO
			lRicDao.selCondizioneUpdate(aIdRich);
			lRicDao.delete();
			lRicDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExCancellaRichiestePmInCumuloFull: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmTitoDao);
			cleanup(lRicPmMisSicDao);
			cleanup(lRicPmPenAccDao);
			cleanup(lRicPmReaDao);
			cleanup(lRicPmSECDao);
			cleanup(lRicBenCumDao);
			cleanup(lProvvDao);
			cleanup(lRicSSCumDao);
			cleanup(lConn);
		}
	}

	/**************************************************************************************************************
	 * Effettua l'aggiornamento della RichiestaPm, cancellando tutti i dati afferenti alle decisioni (G.E. o
	 * SORV.)
	 *
	 * @param aRichiestePmInCumulo
	 * @throws F3BException
	 **************************************************************************************************************/
	public void ExCancellaDecisioneDellaRichiesta(BigDecimal aIdRich) throws F3BException {

		Connection lConn = null;
		ProvvedimentoGeSorvCumDAO lProvvDao = null;
		RichPMMisSicurCumDAO lRicPmMisSicDao = null;
		RichPMPenAccCumDAO lRicPmPenAccDao = null;

		try {
			lConn = getDBConnection();

			// Eventuali Relazioni : Tabella RICHPM_MISSICUR_CUM
			lRicPmMisSicDao = new RichPMMisSicurCumDAO(lConn);
			lRicPmMisSicDao.setFlagCondono(null);
			lRicPmMisSicDao.selCondizioneUpdate(aIdRich);
			lRicPmMisSicDao.update();
			lRicPmMisSicDao.stop();

			// Eventuali Relazioni : Tabella RICHPM_PENACC_CUM
			lRicPmPenAccDao = new RichPMPenAccCumDAO(lConn);
			lRicPmPenAccDao.setFlagCondono(null);
			lRicPmPenAccDao.selCondizioneUpdate(aIdRich);
			lRicPmPenAccDao.update();
			lRicPmPenAccDao.stop();

			// Cancella Provvedimento : Tabella PROVVEDIMENTO_GE_SORV_CUM
			lProvvDao = new ProvvedimentoGeSorvCumDAO(lConn);
			lProvvDao.selCondizioneByIdRichiesta(aIdRich);
			lProvvDao.delete();
			lProvvDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExCancellaDecisioneDellaRichiesta: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicPmMisSicDao);
			cleanup(lRicPmPenAccDao);
			cleanup(lProvvDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aRichiestePmInCumulo
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountRichiestePmInCumulo(RichiestePmInCumuloModel aRichiestePmInCumulo)
			throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		RichiestePmInCumuloSqlDAO lRichiestePmInCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestePmInCumuloSqlDao = new RichiestePmInCumuloSqlDAO(lConn);
			lRichiestePmInCumuloSqlDao.getCountRichiestePmInCumulo(aRichiestePmInCumulo);
			lRichiestePmInCumuloSqlDao.start();
			lRichiestePmInCumuloSqlDao.next();
			lCount = lRichiestePmInCumuloSqlDao.getBigDecimal("HowManyRecords");
			lRichiestePmInCumuloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExGetCountRichiestePmInCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRichiestePmInCumuloSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aRichiestePmInCumulo
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaRichiestePmInCumuloPaged(RichiestePmInCumuloModel aRichiestePmInCumulo, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lRichiestePmInCumuli = new Vector();
		RichiestePmInCumuloSqlDAO lRichiestePmInCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestePmInCumuloSqlDao = new RichiestePmInCumuloSqlDAO(lConn);
			lRichiestePmInCumuloSqlDao.ricercaRichiestePmInCumuloPaged(aRichiestePmInCumulo, aPage);
			lRichiestePmInCumuli = new Vector(lRichiestePmInCumuloSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaRichiestePmInCumuloPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRichiestePmInCumuloSqlDao);
			cleanup(lConn);
		}
		return lRichiestePmInCumuli;
	}

	/*****************************************************************************
	 * Effettua la ricerca per IdIstruttoria_Cumulo:
	 *
	 * @param akey
	 *            valore della for.key Istr_Id_Istruttoria_Cumulo
	 * @return il Vector con le RichiesteGE dell'Istruttoria
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<RichiestePmInCumuloModel> ExRicercaRichiestePmInCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria, String QualiRichieste) throws F3BException {

		Connection lConn = null;
		Vector<RichiestePmInCumuloModel> lRichiestePmInCumulo = new Vector<>();

		RichiestePmInCumuloSqlDAO lRicSqlDao = null;
		RichPMTitoloCumSqlDAO lRicTitSqlDao = null;
		TitoloCumulatoSqlDAO lTitSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichiestePmInCumuloSqlDAO(lConn);

			if ("tutte".equals(QualiRichieste))
				lRicSqlDao.ricercaRichiestePmInCumuloByIdIstruttoria(aIdIstruttoria);
			else if ("dainviare".equals(QualiRichieste))
				lRicSqlDao.ricercaRichiestePmInCumuloDaInviareByIdIstruttoria(aIdIstruttoria);

			lRichiestePmInCumulo = new Vector<RichiestePmInCumuloModel>(lRicSqlDao.getModels());

			// Ricerca dei titoli collegati alla richiesta
			if (lRichiestePmInCumulo != null && lRichiestePmInCumulo.size() > 0) {
				lRicTitSqlDao = new RichPMTitoloCumSqlDAO(lConn);
				lTitSqlDao = new TitoloCumulatoSqlDAO(lConn);
				int i = 0;

				Iterator itx = lRichiestePmInCumulo.iterator();
				while (itx.hasNext()) {
					i = 0;
					RichiestePmInCumuloModel lRicMod = (RichiestePmInCumuloModel) itx.next();
					lRicTitSqlDao.ricercaRichPmTitoloCumByRichIdRich(lRicMod.getIdRichiestePmInCumulo());
					lRicTitSqlDao.start();
					while (lRicTitSqlDao.next()) {
						i = i + 1;
						RichPMTitoloCumModel lRicTitMod = (RichPMTitoloCumModel) lRicTitSqlDao.getModel();
						lTitSqlDao.ricercaTitoloCumulatoByKey(lRicTitMod.getTitIdTitoloCumulato());
						TitoloCumulatoModel lTitMod = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();
						if (lTitMod != null && lTitMod.getIdTitoloCumulato() != null) {
							if (i == 1) {
								lRicMod.setAnnoSentenza(lTitMod.getAnnoSentenza().toString());
								lRicMod.setNumeroSentenza(lTitMod.getNumeroSentenza());
							}
						}
					}

					lRicMod.setAltri(Integer.toString(i - 1));
				}
			}
			lRicSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaRichiestePmInCumuloByIdIstruttoria: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lRicTitSqlDao);
			cleanup(lTitSqlDao);
			cleanup(lConn);
		}

		return lRichiestePmInCumulo;
	}

	/*****************************************************************************
	 * Effettua la ricerca per IdIstruttoria_Cumulo:
	 *
	 * @param akey
	 *            valore della for.key Istr_Id_Istruttoria_Cumulo
	 * @param aCodTipoRic
	 *            Tipo richiesta (al GE, alla SORV, ...)
	 * @return il Vector con le RichiesteGE dell'Istruttoria
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<RichiestePmInCumuloModel> ExRicercaRichiestePmInCumuloByIdIstruttoriaTipoRichiesta(
			BigDecimal aIdIstruttoria, String aCodTipoRic, String QualiRichieste) throws F3BException {

		Connection lConn = null;
		Vector<RichiestePmInCumuloModel> lRichiestePmInCumulo = new Vector<>();
		ProvvedimentoGeSorvCumModel lProvvMod = null;

		RichiestePmInCumuloSqlDAO lRicSqlDao = null;
		RichPMTitoloCumSqlDAO lRicTitSqlDao = null;
		TitoloCumulatoSqlDAO lTitSqlDao = null;
		ProvvedimentoGeSorvCumSqlDAO lProvvSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichiestePmInCumuloSqlDAO(lConn);

			if ("tutte".equals(QualiRichieste))
				lRicSqlDao.ricercaRichiestePmInCumuloByIdIstruttoria(aIdIstruttoria, aCodTipoRic);
			else if ("dainviare".equals(QualiRichieste))
				lRicSqlDao.ricercaRichiestePmInCumuloDaInviareByIdIstruttoria(aIdIstruttoria, aCodTipoRic);

			lRichiestePmInCumulo = new Vector<RichiestePmInCumuloModel>(lRicSqlDao.getModels());

			// Ricerca dei titoli collegati alla richiesta
			if (lRichiestePmInCumulo != null && lRichiestePmInCumulo.size() > 0) {
				lRicTitSqlDao = new RichPMTitoloCumSqlDAO(lConn);
				lTitSqlDao = new TitoloCumulatoSqlDAO(lConn);
				lProvvSqlDao = new ProvvedimentoGeSorvCumSqlDAO(lConn);

				int i = 0;

				Iterator itx = lRichiestePmInCumulo.iterator();
				while (itx.hasNext()) {
					i = 0;
					RichiestePmInCumuloModel lRicMod = (RichiestePmInCumuloModel) itx.next();
					lRicTitSqlDao.ricercaRichPmTitoloCumByRichIdRich(lRicMod.getIdRichiestePmInCumulo());
					lRicTitSqlDao.start();
					while (lRicTitSqlDao.next()) {
						i = i + 1;
						RichPMTitoloCumModel lRicTitMod = (RichPMTitoloCumModel) lRicTitSqlDao.getModel();
						lTitSqlDao.ricercaTitoloCumulatoByKey(lRicTitMod.getTitIdTitoloCumulato());
						TitoloCumulatoModel lTitMod = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();
						if (lTitMod != null && lTitMod.getIdTitoloCumulato() != null) {
							if (i == 1) {
								lRicMod.setAnnoSentenza(!Utils.isNullObj(lTitMod.getAnnoSentenza())
										? lTitMod.getAnnoSentenza().toString()
										: "");
								lRicMod.setNumeroSentenza(lTitMod.getNumeroSentenza());
							}
						}
					}

					lRicMod.setAltri(Integer.toString(i - 1));

					// Ricerca Eventuali Decisioni SORV o GE
					lProvvSqlDao
							.ricercaProvvedimentoGeSorvCumByIdRichiesta(lRicMod.getIdRichiestePmInCumulo());
					lProvvMod = (ProvvedimentoGeSorvCumModel) lProvvSqlDao.getModelByKey();

					if (lProvvMod != null && lProvvMod.getIdProvvedimentoGeSorvCum() != null)
						lRicMod.setDecisioneGeSorvCum(lProvvMod);

					lProvvSqlDao.stop();

				}
			}
			lRicSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaRichiestePmInCumuloByIdIstruttoriaTipoRichiesta: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lRicTitSqlDao);
			cleanup(lTitSqlDao);
			cleanup(lProvvSqlDao);
			cleanup(lConn);
		}

		return lRichiestePmInCumulo;
	}

	/*****************************************************************************
	 * Effettua l'inserimento di un RichiestePmInCumulo a partire dai dati contenuti nel Model; Inserisce
	 * anche le seguenti Tab. di Conversione
	 *
	 * @param aRichiestePmInCumulo
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_ApplicazioneBenefici(
			RichiestePmInCumuloModel aRichPmInCum, Vector<TitoloCumulatoModel> VecTitoli)
			throws F3BException {

		siesLogger.debug("--XX-- >>>> Start ExInserisciRichiestePmInCumulo_GE_ApplicazioneBenefici ");

		Connection lConn = null;
		RichiestePmInCumuloDAO lRicDao = null;
		RichPMTitoloCumDAO lRicPmTitoDao = null;
		RichPMMisSicurCumDAO lRicPmMisSicDao = null;
		RichPMPenAccCumDAO lRicPmPenAccDao = null;
		RichPMReatoCumDAO lRicPmReaDao = null;

		RichiestePmInCumuloModel lRicMod = null;
		BigDecimal lKeyRich = null;
		BigDecimal lKeyTito = null;
		MisuraSicurezzaCumuloModel lMisMod = null;
		PenaAccessoriaCumuloModel lPenAccMod = null;
		ReatoCircostanzaCumuloModel lReaMod = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicDao.setDAOFromModel(aRichPmInCum);
			lKeyRich = lRicDao.insert();
			lRicDao.stop();

			// siesLogger.debug("--XX-- >>>> Inserito RichPM_in_Cumulo - id = "+lKeyRich);

			lRicPmTitoDao = new RichPMTitoloCumDAO(lConn);
			lRicPmMisSicDao = new RichPMMisSicurCumDAO(lConn);
			lRicPmPenAccDao = new RichPMPenAccCumDAO(lConn);
			lRicPmReaDao = new RichPMReatoCumDAO(lConn);

			Iterator itx = VecTitoli.iterator();
			while (itx.hasNext()) {
				TitoloCumulatoModel lTitoloMod = (TitoloCumulatoModel) itx.next();
				if (lTitoloMod != null && lTitoloMod.getIdTitoloCumulato() != null) {
					lKeyTito = lTitoloMod.getIdTitoloCumulato();
					// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / TITOLO_CUMULATO
					lRicPmTitoDao.setRichIdRichiestePMinCumulo(lKeyRich);
					lRicPmTitoDao.setTitIdTitoloCumulato(lKeyTito);
					lRicPmTitoDao.setFlagInteroCumulo(aRichPmInCum.getFlagInteroCumulo()); // 09/05/2019 MEV70
					lRicPmTitoDao.insert();
				}

				if (lTitoloMod.getMisureSicurezzaCumulo() != null
						&& lTitoloMod.getMisureSicurezzaCumulo().size() > 0) {
					Iterator itM = lTitoloMod.getMisureSicurezzaCumulo().iterator();
					while (itM.hasNext()) {
						// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / MISURE_SICUREZZA_CUMULO
						lMisMod = (MisuraSicurezzaCumuloModel) itM.next();
						if (lMisMod != null && lMisMod.getIdMisuraSicurezzaCumulo() != null) {
							lRicPmMisSicDao.setMisIdMisSicurCumulo(lMisMod.getIdMisuraSicurezzaCumulo());
							lRicPmMisSicDao.setRichIdRichiestePMinCumulo(lKeyRich);
							lRicPmMisSicDao.insert();
						}
					}
				}

				if (lTitoloMod.getPeneAccessorieCumulo() != null
						&& lTitoloMod.getPeneAccessorieCumulo().size() > 0) {
					Iterator itp = lTitoloMod.getPeneAccessorieCumulo().iterator();
					while (itp.hasNext()) {
						// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / PENE_ACCESSORIE_CUMULO
						lPenAccMod = (PenaAccessoriaCumuloModel) itp.next();
						if (lPenAccMod != null && lPenAccMod.getIdPenaAccessoriaCumulo() != null) {
							lRicPmPenAccDao.setPenIdPenAccCumulo(lPenAccMod.getIdPenaAccessoriaCumulo());
							lRicPmPenAccDao.setRichIdRichiestePMinCumulo(lKeyRich);
							lRicPmPenAccDao.insert();
						}
					}
				}

				if (lTitoloMod.getReatoCircostanzaCumulo() != null
						&& lTitoloMod.getReatoCircostanzaCumulo().size() > 0) {
					Iterator itr = lTitoloMod.getReatoCircostanzaCumulo().iterator();
					while (itr.hasNext()) {
						// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / REATO_CUMULO
						lReaMod = (ReatoCircostanzaCumuloModel) itr.next();
						if (lReaMod != null && lReaMod.getReatoCum() != null
								&& lReaMod.getReatoCum().getIdReatoCum() != null) {
							lRicPmReaDao.setReaIdReatoCumulo(lReaMod.getReatoCum().getIdReatoCum());
							lRicPmReaDao.setRichIdRichiestePMinCumulo(lKeyRich);
							lRicPmReaDao.insert();
						}
					}
				}
			}

			lRicMod = new RichiestePmInCumuloModel(aRichPmInCum);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestePmInCumulo(lKeyRich);

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiestePmInCumulo_GE_ApplicazioneBenefici: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmTitoDao);
			cleanup(lRicPmMisSicDao);
			cleanup(lRicPmPenAccDao);
			cleanup(lRicPmReaDao);
			cleanup(lConn);
		}

		return lRicMod;
	} // Chiude ExInserisciRichiestePmInCumulo_GE_ApplicazioneBenefici

	/*****************************************************************************
	 * Effettua l'inserimento di una RichiestePmInCumulo a partire dai dati contenuti nel Model; (Richiesta di
	 * REVOCA Benefici) Inserisce anche la Tab. di Relazione RichPm_Beneficio_Cum (RICHIESTE_PM_IN_CUMULO con
	 * BENEFICIO_CUMULATO) oppure Tab. di Relazione RichPm_StatoEsec_Cum (RICHIESTE_PM_IN_CUMULO con
	 * STATO_ESEC_TITOLO_CUMULATO)
	 *
	 * @param aRichiestePmInCumulo
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_RevocaBenefici(
			RichiestePmInCumuloModel aRichPmInCum, Vector<String> ListaIdBenefici, BigDecimal lIdProvv)
			throws F3BException {

		siesLogger.debug("--XX-- >>>> Start ExInserisciRichiestePmInCumulo_GE_RevocaBenefici ");
		Connection lConn = null;

		RichiestePmInCumuloModel lRicMod = null;
		BigDecimal lKeyRich = null;

		RichiestePmInCumuloDAO lRicDao = null;
		RichPMTitoloCumDAO lRicPmDao = null;
		RichPMBeneficioCumDAO lRicBenDao = null;
		RichPMStatoEsecCumDAO lRicStatoEsecDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicBenDao = new RichPMBeneficioCumDAO(lConn);
			lRicPmDao = new RichPMTitoloCumDAO(lConn);
			lRicStatoEsecDao = new RichPMStatoEsecCumDAO(lConn);

			// Inserimento Richiesta di Revoca
			lRicDao.setDAOFromModel(aRichPmInCum);
			lKeyRich = lRicDao.insert();
			lRicDao.stop();

			// tabella Relazione RICHIESTE_PM_IN_CUMULO / BENEFICIO_CUMULO
			if (ListaIdBenefici != null && ListaIdBenefici.size() > 0) {
				for (int i = 0; i < ListaIdBenefici.size(); i++) {
					lRicBenDao.setRichIdRichiestePMinCumulo(lKeyRich);
					lRicBenDao.setBenIdBeneficioCumulo(new BigDecimal(ListaIdBenefici.get(i)));
					lRicBenDao.insert();
				}
			}

			// tabella Relazione RICHIESTE_PM_IN_CUMULO / STATO_ESEC_TITOLO_CUMULATO
			if (lIdProvv != null) {
				lRicStatoEsecDao.setRichIdRichiestePMinCumulo(lKeyRich);
				lRicStatoEsecDao.setStatoIdStatoEsecTitCumulo(lIdProvv);
				lRicStatoEsecDao.insert();
			}

			// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / TITOLO_CUMULATO associato al Beneficio
			lRicPmDao.setRichIdRichiestePMinCumulo(lKeyRich);
			lRicPmDao.setTitIdTitoloCumulato(aRichPmInCum.getTitIdTitoloCumulato());
			lRicPmDao.insert();

			lRicMod = new RichiestePmInCumuloModel(aRichPmInCum);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestePmInCumulo(lKeyRich);

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiestePmInCumulo_GE_RevocaBenefici: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmDao);
			cleanup(lRicBenDao);
			cleanup(lRicStatoEsecDao);
			cleanup(lConn);
		}

		return lRicMod;
	} // Chiude ExInserisciRichiestePmInCumulo_GE_RevocaBenefici()

	public Vector<RichPMTitoloCumModel> ExRicercaRichPMTitoliCum(BigDecimal aRichIdRichiesta)
			throws F3BException {

		Connection lConn = null;
		Vector<RichPMTitoloCumModel> lVec = null;
		// RichiestePmInCumuloModel lRichiestePmInCumuloMod = new RichiestePmInCumuloModel();
		RichPMTitoloCumSqlDAO lRicSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichPMTitoloCumSqlDAO(lConn);
			lRicSqlDao.ricercaRichPmTitoloCumByRichIdRich(aRichIdRichiesta);

			lVec = new Vector<RichPMTitoloCumModel>(lRicSqlDao.getModels());

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaRichPMTitoliCum: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lConn);
		}

		return lVec;
	}

	public Vector<TitoloCumulatoModel> ExRicercaAltriDatiRichiestaGE(
			Vector<RichPMTitoloCumModel> VecRichTitoli) throws F3BException {

		Connection lConn = null;
		Vector<TitoloCumulatoModel> lVec = new Vector<>();

		TitoloCumulatoSqlDAO lTitSqlDao = null;
		MisuraSicurezzaCumuloSqlDAO lMisSqlDao = null;
		PenaAccessoriaCumuloSqlDAO lPenaAccSqlDao = null;
		ReatoCumuloSqlDAO lReaSqlDao = null;

		try {
			lConn = getDBConnection();

			lTitSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lMisSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
			lPenaAccSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
			lReaSqlDao = new ReatoCumuloSqlDAO(lConn);

			Iterator<RichPMTitoloCumModel> itx = VecRichTitoli.iterator();
			while (itx.hasNext()) {
				RichPMTitoloCumModel lRicmod = itx.next();

				// Titolo
				TitoloCumulatoModel lTitMod = null;
				lTitSqlDao.ricercaTitoloCumulatoByKey(lRicmod.getTitIdTitoloCumulato());
				lTitMod = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();

				// Misure di Sicurezza
				lMisSqlDao.ricercaMisuraSicurezzaCumuloByIdTitoloCumRichGE(lRicmod.getTitIdTitoloCumulato(),
						lRicmod.getRicIdRichiestePmInCumulo());
				Vector<MisuraSicurezzaCumuloModel> VecMis = new Vector(lMisSqlDao.getModels());
				if (VecMis != null && VecMis.size() > 0)
					lTitMod.setMisureSicurezzaCumulo(VecMis);

				// Pena Accessoria
				lPenaAccSqlDao.ricercaPenaAccessoriaCumuloByTitoloCumRichGE(lRicmod.getTitIdTitoloCumulato(),
						lRicmod.getRicIdRichiestePmInCumulo());
				Vector<PenaAccessoriaCumuloModel> VecPenAcc = new Vector(lPenaAccSqlDao.getModels());
				if (VecPenAcc != null && VecPenAcc.size() > 0)
					;
				lTitMod.setPeneAccessorieCumulo(VecPenAcc);

				// Reati
				lReaSqlDao.ricercaReatiCumNoCircostanzaByIdTitoloRichGE(lRicmod.getTitIdTitoloCumulato(),
						lRicmod.getRicIdRichiestePmInCumulo());
				Vector<ReatoCumuloModel> lReati = new Vector<ReatoCumuloModel>(lReaSqlDao.getModels());

				Vector<ReatoCircostanzaCumuloModel> VecReati = new Vector<>();

				Iterator lItx = lReati.iterator();
				while (lItx.hasNext()) {
					ReatoCircostanzaCumuloModel ReaModel = new ReatoCircostanzaCumuloModel();
					ReaModel.setReatoCum((ReatoCumuloModel) lItx.next());

					lReaSqlDao.ricercaCircostanzeReatoCumByReatoTitoloCum(
							ReaModel.getReatoCum().getProgrReato(), lRicmod.getTitIdTitoloCumulato());
					List lCircostanze = new ArrayList(lReaSqlDao.getModels());
					ReaModel.setCircostanzeCum(
							(ReatoCumuloModel[]) lCircostanze.toArray(new ReatoCumuloModel[0]));

					VecReati.add(ReaModel);
				}

				if (VecReati != null && VecReati.size() > 0)
					;
				lTitMod.setReatoCircostanzaCumulo(VecReati);

				// Appendo il TitoloModel (con tutt le Entità correlate)
				lVec.add(lTitMod);

			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaAltriDatiRichiestaGE: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lTitSqlDao);
			cleanup(lMisSqlDao);
			cleanup(lPenaAccSqlDao);
			cleanup(lReaSqlDao);
			cleanup(lConn);
		}

		return lVec;
	}

	public Vector<TitoloCumulatoModel> ExRicercaAggregatiAlTitolo(Vector<TitoloCumulatoModel> VecTitoliInput)
			throws F3BException {

		siesLogger.debug("--XX--    >>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<  INIZIO");

		Connection lConn = null;
		Vector<TitoloCumulatoModel> VecTitoliOutput = new Vector<>();

		MisuraSicurezzaCumuloSqlDAO lMisSqlDao = null;
		PenaAccessoriaCumuloSqlDAO lPenaAccSqlDao = null;
		ReatoCumuloSqlDAO lReaSqlDao = null;

		try {
			lConn = getDBConnection();

			lMisSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
			lPenaAccSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
			lReaSqlDao = new ReatoCumuloSqlDAO(lConn);

			Iterator<TitoloCumulatoModel> itx = VecTitoliInput.iterator();
			while (itx.hasNext()) {
				TitoloCumulatoModel lTitMod = itx.next();

				// Misure di Sicurezza
				lMisSqlDao.ricercaMisuraSicurezzaCumuloByIdTitoloCum(lTitMod.getIdTitoloCumulato());
				Vector<MisuraSicurezzaCumuloModel> VecMis = new Vector<MisuraSicurezzaCumuloModel>(
						lMisSqlDao.getModels());
				if (VecMis != null && VecMis.size() > 0)
					lTitMod.setMisureSicurezzaCumulo(VecMis);

				// Pena Accessoria
				lPenaAccSqlDao.ricercaPenaAccessoriaCumuloByTitoloCum(lTitMod.getIdTitoloCumulato());
				Vector<PenaAccessoriaCumuloModel> VecPenAcc = new Vector<PenaAccessoriaCumuloModel>(
						lPenaAccSqlDao.getModels());
				if (VecPenAcc != null && VecPenAcc.size() > 0)
					;
				lTitMod.setPeneAccessorieCumulo(VecPenAcc);

				// Reati
				lReaSqlDao.ricercaReatiCumNoCircostanzaByIdTitolo(lTitMod.getIdTitoloCumulato());
				Vector<ReatoCumuloModel> lReati = new Vector<ReatoCumuloModel>(lReaSqlDao.getModels());

				Vector<ReatoCircostanzaCumuloModel> VecReati = new Vector<>();

				Iterator lItx = lReati.iterator();
				while (lItx.hasNext()) {
					ReatoCircostanzaCumuloModel ReaModel = new ReatoCircostanzaCumuloModel();
					ReaModel.setReatoCum((ReatoCumuloModel) lItx.next());

					lReaSqlDao.ricercaCircostanzeReatoCumByReatoTitoloCum(
							ReaModel.getReatoCum().getProgrReato(), lTitMod.getIdTitoloCumulato());
					List lCircostanze = new ArrayList(lReaSqlDao.getModels());
					ReaModel.setCircostanzeCum(
							(ReatoCumuloModel[]) lCircostanze.toArray(new ReatoCumuloModel[0]));

					VecReati.add(ReaModel);
				}

				if (VecReati != null && VecReati.size() > 0)
					;
				lTitMod.setReatoCircostanzaCumulo(VecReati);

				// Appendo il TitoloModel (con tutt le Entità correlate)
				VecTitoliOutput.add(lTitMod);
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaAggregatiAlTitolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMisSqlDao);
			cleanup(lPenaAccSqlDao);
			cleanup(lReaSqlDao);

			cleanup(lConn);
		}

		return VecTitoliOutput;
	} // Chiude ExRicercaAggregatiAlTitolo()

	/**
	 * Inserisce le RICHIESTE_INVIATE_CUM e i dati della RICHIESTE_PM_IN_CUMULO collegate
	 *
	 *
	 * La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequnece. il valore della
	 * Primary_Key è già preimpostato; metodo usato nella funzione di presa in carico, per scaricare Tutti i
	 * dati del Fascicolo sulla nuova Base dati.
	 *
	 * @param VecRichInv
	 * @param lConn
	 * @return
	 */
	public String ExInserisciRichiesteInviateCumWithoutSequence(Vector<RichiesteInviateCumModel> VecRichInv,
			Connection lConn) throws F3BException {

		String EsitodiRitorno = "00000";
		RichiesteInviateCumDAO lRicInvDao = null;
		RichiesteInviateCumModel lRicMod = null;

		try {
			lRicInvDao = new RichiesteInviateCumDAO(lConn);
			if (VecRichInv != null && VecRichInv.size() > 0) {
				Iterator ItxRI = VecRichInv.iterator();
				while (ItxRI.hasNext()) {
					lRicMod = (RichiesteInviateCumModel) ItxRI.next();
					if (lRicMod != null && lRicMod.getIdRichiesteInviateCum() != null) {
						try {
							lRicInvDao.setDAOFromModel(lRicMod);
							lRicInvDao.setIdRichiesteInviateCum(lRicMod.getIdRichiesteInviateCum());
							lRicInvDao.setWithoutSequence(true);
							lRicInvDao.insert();
							lRicInvDao.stop();
						} catch (DAOException daoEx) {
							if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
								siesLogger.warn("Richiesta_Inviata_Cum gia' presente...>"
										+ lRicMod.getIdRichiesteInviateCum() + "<");
								EsitodiRitorno = "00001";
							} else {
								throw daoEx;
							}
						}
					}

					// Richieste_Pm_In_Cumulo
					if (lRicMod != null && lRicMod.getListaRichiestePMinCumulo() != null
							&& lRicMod.getListaRichiestePMinCumulo().size() > 0) {
						Vector<RichiestePmInCumuloModel> VecRichPM = new Vector<>(
								lRicMod.getListaRichiestePMinCumulo());

						EsitodiRitorno = ExInserisciRichiestePmInCumuloWithoutSequence(VecRichPM, lConn);
					}
				}
			}
		} catch (DAOException daoEx) {
			EsitodiRitorno = "01400";
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire Richiesta_Inviata_Cum ! ");
		} finally {
			cleanup(lRicInvDao);
		}

		return EsitodiRitorno;
	} // CHIUDE ExInserisciRichiesteInviateCumWithoutSequence()

	/**
	 * Inserisce le RICHIESTE_PM_IN_CUMULO ed eventuale scarico decisione
	 *
	 * @param
	 * @param
	 * @return
	 */
	public String ExInserisciRichiestePmInCumuloWithoutSequence(Vector<RichiestePmInCumuloModel> VecRichPM,
			Connection lConn) throws F3BException {

		String EsitodiRitorno = "00000";
		RichiestePmInCumuloDAO lRicPMDao = null;
		RichiestePmInCumuloModel lRicPmMod = null;
		ProvvedimentoGeSorvCumDAO lProvvDao = null;
		String lNomeTab = "";

		try {
			lRicPMDao = new RichiestePmInCumuloDAO(lConn);
			lProvvDao = new ProvvedimentoGeSorvCumDAO(lConn);

			Iterator ItxRpm = VecRichPM.iterator();
			while (ItxRpm.hasNext()) {
				lRicPmMod = (RichiestePmInCumuloModel) ItxRpm.next();
				if (lRicPmMod.getIdRichiestePmInCumulo() != null) {
					lNomeTab = "RICHIESTE_PM_IN_CCUMULO";
					try {
						lRicPMDao.setDAOFromModel(lRicPmMod);
						lRicPMDao.setWithoutSequence(true);
						lRicPMDao.insert();
						lRicPMDao.stop();
					} catch (DAOException daoEx) {
						if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.warn("Richiesta_Pm_In_Cumulo gia' presente...>"
									+ lRicPmMod.getIdRichiestePmInCumulo() + "<");
							EsitodiRitorno = "00001";
						} else {
							throw daoEx;
						}
					}
				}

				// Inserisco lo scarico delle decisioni se presente
				// Tabella PROVVEDIMENTO_GE_SORV_CUM
				if (lRicPmMod.getDecisioneGeSorvCum() != null
						&& lRicPmMod.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null) {
					lNomeTab = "PROVVEDIMENTO_GE_SORV_CUM";
					try {
						lProvvDao.setDAOFromModel(lRicPmMod.getDecisioneGeSorvCum());
						lProvvDao.setWithoutSequence(true);
						lProvvDao.insert();
						lProvvDao.stop();
					} catch (DAOException daoEx) {
						if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.warn("Tabella PROVVEDIMENTO_GE_SORV_CUM (id: "
									+ lRicPmMod.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum()
									+ ") già presente per Id_Richiesta = >"
									+ lRicPmMod.getIdRichiestePmInCumulo() + "<");
							EsitodiRitorno = "00001";
						} else {
							throw daoEx;
						}
					}
				}
			}
		} catch (DAOException daoEx) {
			EsitodiRitorno = "01400";
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire " + lNomeTab + "! ");
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiestePmInCumuloWithoutSequence: " + ex);
		} finally {
			cleanup(lRicPMDao);
			cleanup(lProvvDao);
		}

		return EsitodiRitorno;
	}

	/*****************************************************************************
	 * Effettua l'inserimento di una Richieste_Inviate_Cum a partire dai dati contenuti nel Model, e la lega a
	 * tutte le Richieste_PM_in_Cumulo coinvolte
	 *
	 * @param RichiesteInviateCum
	 *            Model (con i dati da inserire)
	 * @param String[]
	 *            IdRichCollegate (contiene gli ID delle RichiestePmInCumulo da "legare" alla Richiesta)
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public RichiesteInviateCumModel ExInserisciRichiesteInviateCumulo(RichiesteInviateCumModel aRichInviate,
			String[] lIdRichCollegate) throws F3BException {

		siesLogger.debug("--XX-- >>>> Sono nel  RichiesteInviateCumController - INIZIO");

		Connection lConn = null;
		RichiesteInviateCumDAO lRicInvDao = null;
		RichiestePmInCumuloDAO lRicPMDao = null;
		RichiestePmInCumuloSqlDAO lRicPMSqlDao = null;

		RichiesteInviateCumModel lRicMod = null;
		RichiestePmInCumuloModel lRicPmMod = null;

		try {
			lConn = getDBConnection();

			// Insert Richieste_Inviate_Cum
			lRicInvDao = new RichiesteInviateCumDAO(lConn);
			lRicInvDao.setDAOFromModel(aRichInviate);
			BigDecimal lSequence = lRicInvDao.insert();

			lRicPMDao = new RichiestePmInCumuloDAO(lConn);
			lRicPMSqlDao = new RichiestePmInCumuloSqlDAO(lConn);

			if (lIdRichCollegate != null && lIdRichCollegate.length > 0) {
				for (int i = 0; i < lIdRichCollegate.length; i++) {
					lRicPMSqlDao.ricercaRichiestePmInCumuloByKey(new BigDecimal(lIdRichCollegate[i]));
					lRicPmMod = null;
					lRicPmMod = (RichiestePmInCumuloModel) lRicPMSqlDao.getModelByKey();

					// Update Richieste_PM_in_Cumulo
					if (lRicPmMod != null && lRicPmMod.getIdRichiestePmInCumulo() != null) {
						lRicPmMod.setRicIdRichiesteInviateCum(lSequence);
						lRicPmMod.setCodOperatoreAggiornamento(aRichInviate.getCodOperatoreInserimento());
						lRicPmMod.setCodUfficioAggiornamento(aRichInviate.getCodUfficioAggiornamento());
						lRicPmMod.setDataAggiornamento(aRichInviate.getDataInserimento());

						lRicPMDao.setDAOFromModelForUpdate(lRicPmMod);
						lRicPMDao.selCondizioneUpdate(lRicPmMod.getIdRichiestePmInCumulo());
						lRicPMDao.update();
						lRicPMDao.stop();

						// siesLogger.debug(" Ricxhiesta_PM_In_Cumulo Modificata - Id_Ric =
						// "+lRicPmMod.getIdRichiestePmInCumulo() );
					}

				}
			}

			commit(lConn);

			lRicMod = new RichiesteInviateCumModel(aRichInviate);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiesteInviateCum(lSequence);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiesteDelPmInviateCumulo: Non posso inserire: "
							+ daoEx);
		} finally {
			cleanup(lRicInvDao);
			cleanup(lRicPMDao);
			cleanup(lRicPMSqlDao);
			cleanup(lConn);
		}

		return lRicMod;
	} // Chiude ExInserisciRichiesteDelPmInviateCumulo

	/*****************************************************************************
	 * TAB RICHIESTE_INVIATE_CUM Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public RichiesteInviateCumModel ExRicercaRichiesteInviateCumuloById(BigDecimal aIdRichiesteInviateCum)
			throws F3BException {

		Connection lConn = null;

		RichiesteInviateCumModel lRichInvMod = new RichiesteInviateCumModel();

		RichiesteInviateCumSqlDAO lRicInvSqlDao = null;
		RichiestePmInCumuloSqlDAO lRicPMSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicInvSqlDao = new RichiesteInviateCumSqlDAO(lConn);
			lRicPMSqlDao = new RichiestePmInCumuloSqlDAO(lConn);

			// Ricerca RICHIESTE_INVIATE_CUM
			lRicInvSqlDao.ricercaRichiesteInviateCumByKey(aIdRichiesteInviateCum);
			lRichInvMod = (RichiesteInviateCumModel) lRicInvSqlDao.getModelByKey();

			// Ricerca RICHIESTE_PM_IN_CUMULO
			if (lRichInvMod != null && lRichInvMod.getIdRichiesteInviateCum() != null) {
				lRicPMSqlDao
						.ricercaRichiestePmInCumuloByIdRichInviateCum(lRichInvMod.getIdRichiesteInviateCum());
				Vector<RichiestePmInCumuloModel> listaRichieste = new Vector<RichiestePmInCumuloModel>(
						lRicPMSqlDao.getModels());

				if (listaRichieste != null && listaRichieste.size() > 0) {
					lRichInvMod
							.setListaRichiestePMinCumulo(this.TrovaTitoliperRichiesta(listaRichieste, lConn));
				}
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaRichiesteInviateCumuloById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicInvSqlDao);
			cleanup(lRicPMSqlDao);
			cleanup(lConn);
		}

		return lRichInvMod;
	}

	/*****************************************************************************
	 * TAB RICHIESTE_INVIATE_CUM Effettua la ricerca per IdIstruttoria
	 *
	 * @param Foreign_key
	 *            ISTR_ID_ISTRUTTORIA_CUMULO
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<RichiesteInviateCumModel> ExRicercaRichiesteInviateCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria) throws F3BException {

		Connection lConn = null;

		Vector<RichiesteInviateCumModel> lVecRic = null;
		RichiesteInviateCumModel lRichInvMod = null;

		RichiesteInviateCumSqlDAO lRicInvSqlDao = null;
		RichiestePmInCumuloSqlDAO lRicPMSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicInvSqlDao = new RichiesteInviateCumSqlDAO(lConn);
			lRicPMSqlDao = new RichiestePmInCumuloSqlDAO(lConn);

			// Ricerca RICHIESTE_INVIATE_CUM
			lRicInvSqlDao.ricercaRichiesteInviateCumByIdIstruttoria(aIdIstruttoria);
			lVecRic = new Vector<RichiesteInviateCumModel>(lRicInvSqlDao.getModels());

			if (lVecRic != null && lVecRic.size() > 0) {
				Iterator itx = lVecRic.iterator();
				while (itx.hasNext()) {
					lRichInvMod = (RichiesteInviateCumModel) itx.next();

					// Ricerca RICHIESTE_PM_IN_CUMULO
					if (lRichInvMod != null && lRichInvMod.getIdRichiesteInviateCum() != null) {
						lRicPMSqlDao.ricercaRichiestePmInCumuloByIdRichInviateCum(
								lRichInvMod.getIdRichiesteInviateCum());
						Vector<RichiestePmInCumuloModel> listaRichieste = new Vector<RichiestePmInCumuloModel>(
								lRicPMSqlDao.getModels());

						if (listaRichieste != null && listaRichieste.size() > 0) {
							lRichInvMod.setListaRichiestePMinCumulo(
									this.TrovaTitoliperRichiesta(listaRichieste, lConn));
						}
					}
				}
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaRichiesteInviateCumuloByIdIstruttoria: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicInvSqlDao);
			cleanup(lRicPMSqlDao);
			cleanup(lConn);
		}

		return lVecRic;
	} // Chiude ExRicercaRichiesteInviateCumuloByIdIstruttoria()

	public Vector<RichiesteInviateCumModel> ExRicercaRichiesteInviateCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria, String aCodTipoRichiesta) throws F3BException {

		Connection lConn = null;

		Vector<RichiesteInviateCumModel> lVecRic = null;
		RichiesteInviateCumModel lRichInvMod = null;

		RichiesteInviateCumSqlDAO lRicInvSqlDao = null;
		RichiestePmInCumuloSqlDAO lRicPMSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicInvSqlDao = new RichiesteInviateCumSqlDAO(lConn);
			lRicPMSqlDao = new RichiestePmInCumuloSqlDAO(lConn);

			// Ricerca RICHIESTE_INVIATE_CUM
			// lRicInvSqlDao.ricercaRichiesteInviateCumByIdIstruttoria(aIdIstruttoria);
			lRicInvSqlDao.ricercaRichiesteInviateCumByIdIstruttoriaTipoRichiesta(aIdIstruttoria,
					aCodTipoRichiesta);
			lVecRic = new Vector<RichiesteInviateCumModel>(lRicInvSqlDao.getModels());

			if (lVecRic != null && lVecRic.size() > 0) {
				Iterator itx = lVecRic.iterator();
				while (itx.hasNext()) {
					lRichInvMod = (RichiesteInviateCumModel) itx.next();

					// Ricerca RICHIESTE_PM_IN_CUMULO
					if (lRichInvMod != null && lRichInvMod.getIdRichiesteInviateCum() != null) {
						lRicPMSqlDao.ricercaRichiestePmInCumuloByIdRichInviateCum(
								lRichInvMod.getIdRichiesteInviateCum());
						Vector<RichiestePmInCumuloModel> listaRichieste = new Vector<RichiestePmInCumuloModel>(
								lRicPMSqlDao.getModels());

						if (listaRichieste != null && listaRichieste.size() > 0) {
							lRichInvMod.setListaRichiestePMinCumulo(
									this.TrovaTitoliperRichiesta(listaRichieste, lConn));
						}
					}
				}
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaRichiesteInviateCumuloByIdIstruttoria: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicInvSqlDao);
			cleanup(lRicPMSqlDao);
			cleanup(lConn);
		}

		return lVecRic;
	} // Chiude ExRicercaRichiesteInviateCumuloByIdIstruttoria()

	/**
	 * Per ogni richiesta del vettore listaRichieste recupera l'elenco dei titoli collegati
	 *
	 * @param listaRichieste
	 * @param aConn
	 * @return
	 * @throws F3BException
	 */
	private Vector<RichiestePmInCumuloModel> TrovaTitoliperRichiesta(
			Vector<RichiestePmInCumuloModel> listaRichieste, Connection aConn) throws F3BException {

		RichPMTitoloCumSqlDAO lRicTitSqlDao = null;
		TitoloCumulatoSqlDAO lTitSqlDao = null;

		try {

			// Ricerca dei titoli collegati alla richiesta
			lRicTitSqlDao = new RichPMTitoloCumSqlDAO(aConn);
			lTitSqlDao = new TitoloCumulatoSqlDAO(aConn);
			int i = 0;

			Iterator itxR = listaRichieste.iterator();
			while (itxR.hasNext()) {
				i = 0;
				// Recupera elenco dei titoli collegati alla richiesta RICHPM_TITOLO_CUM
				RichiestePmInCumuloModel lRicMod = (RichiestePmInCumuloModel) itxR.next();
				lRicTitSqlDao.ricercaRichPmTitoloCumByRichIdRich(lRicMod.getIdRichiestePmInCumulo());
				lRicTitSqlDao.start();

				// Per ogni RICHPM_TITOLO_CUM recupera i dati del titolo
				while (lRicTitSqlDao.next()) {
					i = i + 1;
					RichPMTitoloCumModel lRicTitMod = (RichPMTitoloCumModel) lRicTitSqlDao.getModel();
					lTitSqlDao.ricercaTitoloCumulatoByKey(lRicTitMod.getTitIdTitoloCumulato());
					TitoloCumulatoModel lTitMod = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();

					lRicMod.addTitolo(lTitMod);

					if (lTitMod != null && lTitMod.getIdTitoloCumulato() != null) {
						if (i == 1) {
							lRicMod.setAnnoSentenza(lTitMod.getAnnoSentenza().toString());
							lRicMod.setNumeroSentenza(lTitMod.getNumeroSentenza());
						}
					}
				}

				lRicMod.setAltri(Integer.toString(i - 1));
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.TrovaTitoliperRichiesta: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRicTitSqlDao);
			cleanup(lTitSqlDao);
		}

		return listaRichieste;
	} // Chiude TrovaTitoliperRichiesta()

	public void ExUpdateValidaRichiesteInviateCumulo(RichiesteInviateCumModel aRichiesteInv)
			throws F3BException {

		Connection lConn = null;
		RichiesteInviateCumDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiesteInviateCumDAO(lConn);

			lRicDao.setDAOFromModelForUploadBlob(aRichiesteInv);
			lRicDao.selCondizioneUpdate(aRichiesteInv.getIdRichiesteInviateCum());
			lRicDao.update();
			lRicDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"RichiestePmInCumuloController.ExUpdateValidaRichiesteInviateCumulo: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
	} // Chiude ExUpdateValidaRichiesteInviateCumulo()

	public void ExCancellaRichiesteInviateCumuloFull(BigDecimal aIdRichiestaInv, String CodUff, String CodOp)
			throws F3BException {

		siesLogger.debug("--XX-- >>>> - INIZIO ExCancellaRichiesteInviateCumuloFull");

		Connection lConn = null;
		RichiesteInviateCumDAO lRicInvDao = null;
		RichiestePmInCumuloDAO lRicPMDao = null;
		RichiestePmInCumuloSqlDAO lRicPMSqlDao = null;

		try {
			lConn = getDBConnection();

			lRicInvDao = new RichiesteInviateCumDAO(lConn);
			lRicPMSqlDao = new RichiestePmInCumuloSqlDAO(lConn);
			lRicPMDao = new RichiestePmInCumuloDAO(lConn);

			// Ricerca di Titte le Richieste Aggregate Nell'Invio da Cancellare
			lRicPMSqlDao.ricercaRichiestePmInCumuloByIdRichInviateCum(aIdRichiestaInv);
			Vector<RichiestePmInCumuloModel> VecRichieste = new Vector<RichiestePmInCumuloModel>(
					lRicPMSqlDao.getModels());

			Iterator itx = VecRichieste.iterator();
			while (itx.hasNext()) {
				RichiestePmInCumuloModel lRicMod = (RichiestePmInCumuloModel) itx.next();
				lRicMod.setRicIdRichiesteInviateCum(null);

				lRicMod.setDataAggiornamento(DateUtils.getSysDate());
				lRicMod.setCodUfficioAggiornamento(CodUff);
				lRicMod.setCodOperatoreAggiornamento(CodOp);

				lRicPMDao.setDAOFromModelForUpdate(lRicMod);
				lRicPMDao.selCondizioneUpdate(lRicMod.getIdRichiestePmInCumulo());
				lRicPMDao.update();
			}

			// Delete RichiesteInviate
			lRicInvDao.selCondizioneUpdate(aIdRichiestaInv);
			lRicInvDao.delete();
			lRicInvDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExCancellaRichiesteInviateCumuloFull: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicInvDao);
			cleanup(lRicPMSqlDao);
			cleanup(lRicPMDao);
			cleanup(lConn);
		}
	} // Chiude ExCancellaRichiesteInviateCumuloFull()

	/**
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream
	 *
	 * @param aRichiesta
	 *            Inviata
	 * @return Array con il Documento recuperato dal DB
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExGetDocumento(RichiesteInviateCumModel aRichiesteInv) throws F3BException {

		siesLogger.debug("--XX-- Start ExGetDocumento ------>");

		Connection lConn = null;

		RichiesteInviateCumDAO lRicDao = null;
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiesteInviateCumDAO(lConn);

			lRicDao.setIdRichiesteInviateCum(aRichiesteInv.getIdRichiesteInviateCum());
			lRicDao.selByKey();

			lRicDao.start(1);
			siesLogger.debug("--XX-- Dopo RicDao Start ");
			if (lRicDao.next())
				lByteArrayOut = lRicDao.getDocBlob();

			lRicDao.stop();

			if ((lByteArrayOut == null) || (lByteArrayOut.size() == 0))
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Documento Associato");

		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"RichiestePmInCumuloController.ExGetDocumento: Errore Dati NON trovati " + ex);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"RichiestePmInCumuloController.ExGetDocumento: Errore Dati NON trovati " + ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	public Vector<TitoloCumulatoModel> ExCaricaLibAntDelTitolo(Vector<TitoloCumulatoModel> VecTitoliInput)
			throws F3BException {

		Connection lConn = null;
		Vector<TitoloCumulatoModel> VecTitoliOutput = new Vector<>();

		StatoEsecTitoloCumulatoSqlDAO lSETCSqlDao = null;
		LibAnticipataCumuloSqlDAO lLibAntSqlDao = null;
		PeriodoLibAntCumuloSqlDAO lPeriodoLibAntSqlDao = null;

		try {
			lConn = getDBConnection();

			lSETCSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lLibAntSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
			lPeriodoLibAntSqlDao = new PeriodoLibAntCumuloSqlDAO(lConn);

			Iterator<TitoloCumulatoModel> itx = VecTitoliInput.iterator();
			while (itx.hasNext()) {
				TitoloCumulatoModel lTitMod = itx.next();

				// Stato Esecuzione
				// List<String> listCodiciLA = new ArrayList<String>();
				// listCodiciLA.addAll(StatoEsecuzioneCumuloUtils.aCodLibAnticipata);
				// lSETCSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(lTitMod.getIdTitoloCumulato(),
				// listCodiciLA );

				// 25/07/2018 Stato Esecuzione con le sole L.A. concesse.
				List<String> listCodiciLAConc = new ArrayList<>();
				List<String> listCodiciLAConcBis = new ArrayList<>();
				List<String> listCodEsiLAConc = new ArrayList<>();
				List<String> listCodEsiLAConcBis = new ArrayList<>();
				listCodiciLAConc.addAll(StatoEsecuzioneCumuloUtils.aCodLibAnticipataConc);
				listCodiciLAConcBis.addAll(StatoEsecuzioneCumuloUtils.aCodLibAnticipataConcBis);
				listCodEsiLAConc.addAll(StatoEsecuzioneCumuloUtils.aCodEsiLibAntipataConc);
				listCodEsiLAConcBis.addAll(StatoEsecuzioneCumuloUtils.aCodEsiLibAntipataConcBis);

				lSETCSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(lTitMod.getIdTitoloCumulato(),
						listCodiciLAConc, listCodEsiLAConc, listCodiciLAConcBis, listCodEsiLAConcBis);
				Vector<StatoEsecTitoloCumulatoModel> lVectStati = new Vector<StatoEsecTitoloCumulatoModel>(
						lSETCSqlDao.getModels());

				if (lVectStati != null && lVectStati.size() > 0) {
					for (int y = 0; y < lVectStati.size(); y++) {
						// ===================================================================
						// Recupero delle eventuali Liberazioni Anticipate e Relativi Periodi
						// ===================================================================
						StatoEsecTitoloCumulatoModel lStato = lVectStati.get(y);
						lLibAntSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
						lPeriodoLibAntSqlDao = new PeriodoLibAntCumuloSqlDAO(lConn);

						// 25/07/2018 Lista di valori di FLAG_CONCESSO da filtrare
						List<String> listFlagConcesso = new ArrayList<>();
						listFlagConcesso.addAll(StatoEsecuzioneCumuloUtils.aFlagConc);

						lLibAntSqlDao.ricercaLibAnticipataCumuloByIdStatoEsec(
								lStato.getIdStatoEsecTitoloCumulato(), listFlagConcesso);
						Vector<LibAnticipataCumuloModel> lListaLiberazioni = new Vector(
								lLibAntSqlDao.getModels());

						if (lListaLiberazioni != null && lListaLiberazioni.size() > 0) {
							for (int j = 0; j < lListaLiberazioni.size(); j++) {
								LibAnticipataCumuloModel lLibAntCumMod = lListaLiberazioni.elementAt(j);

								// Ricerca eventuali Periodi di Liberazione Anticipata
								lPeriodoLibAntSqlDao.ricercaPeriodoLibAntCumuloByLibIdLibAntCum(
										lLibAntCumMod.getIdLibAnticipataCumulo());
								Vector<PeriodoLibAntCumuloModel> lListaPeriodiLibAnt = new Vector(
										lPeriodoLibAntSqlDao.getModels());
								if (lListaPeriodiLibAnt != null && lListaPeriodiLibAnt.size() > 0) {
									lLibAntCumMod.setListaPeriodiLibAnticipate(lListaPeriodiLibAnt);
								}
								lListaLiberazioni.set(j, lLibAntCumMod);
								lPeriodoLibAntSqlDao.stop();
							} // end for j

							lStato.setListaLiberazioniAnticipate(lListaLiberazioni);
						}

						lLibAntSqlDao.stop();
					} // end for y
					lTitMod.setStatoEsecuzioneTitoloCumulato(lVectStati);

					// Preparazione Vettore di Output.
					VecTitoliOutput.add(lTitMod);
				}
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExCaricaLibAntDelTitolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSETCSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lLibAntSqlDao);
			cleanup(lPeriodoLibAntSqlDao);
			cleanup(lConn);
		}

		return VecTitoliOutput;
	}

	public Vector<TitoloCumulatoModel> ExRicercaTitoliDiLibAntPerRichiesta(
			Vector<RichPMTitoloCumModel> VecTitoliPerRichiesta) throws F3BException {

		Connection lConn = null;
		Vector<TitoloCumulatoModel> VecTitoliOutput = new Vector<>();

		TitoloCumulatoSqlDAO lTitSqlDao = null;
		StatoEsecTitoloCumulatoSqlDAO lSETCSqlDao = null;
		LibAnticipataCumuloSqlDAO lLibAntSqlDao = null;
		PeriodoLibAntCumuloSqlDAO lPeriodoLibAntSqlDao = null;

		try {
			lConn = getDBConnection();

			lTitSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lSETCSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lLibAntSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
			lPeriodoLibAntSqlDao = new PeriodoLibAntCumuloSqlDAO(lConn);

			Iterator<RichPMTitoloCumModel> itx = VecTitoliPerRichiesta.iterator();
			while (itx.hasNext()) {
				RichPMTitoloCumModel lRichPMTit = itx.next();
				// Lettura Titolo
				lTitSqlDao.ricercaTitoloCumulatoByKey(lRichPMTit.getTitIdTitoloCumulato());
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();

				// Stato Esecuzione
				List<String> listCodiciLA = new ArrayList<>();
				listCodiciLA.addAll(StatoEsecuzioneCumuloUtils.aCodLibAnticipata);

				lSETCSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(lRichPMTit.getTitIdTitoloCumulato(),
						listCodiciLA, lRichPMTit.getRicIdRichiestePmInCumulo());
				Vector<StatoEsecTitoloCumulatoModel> lVectStati = new Vector<StatoEsecTitoloCumulatoModel>(
						lSETCSqlDao.getModels());

				if (lVectStati != null && lVectStati.size() > 0) {
					for (int y = 0; y < lVectStati.size(); y++) {
						// ===================================================================
						// Recupero delle eventuali Liberazioni Anticipate e Relativi Periodi
						// ===================================================================
						StatoEsecTitoloCumulatoModel lStato = lVectStati.get(y);
						lLibAntSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
						lPeriodoLibAntSqlDao = new PeriodoLibAntCumuloSqlDAO(lConn);

						lLibAntSqlDao.ricercaLibAnticipataCumuloByIdStatoEsec(
								lStato.getIdStatoEsecTitoloCumulato());
						Vector<LibAnticipataCumuloModel> lListaLiberazioni = new Vector(
								lLibAntSqlDao.getModels());

						if (lListaLiberazioni != null && lListaLiberazioni.size() > 0) {
							for (int j = 0; j < lListaLiberazioni.size(); j++) {
								LibAnticipataCumuloModel lLibAntCumMod = lListaLiberazioni.elementAt(j);

								// Ricerca eventuali Periodi di Liberazione Anticipata
								lPeriodoLibAntSqlDao.ricercaPeriodoLibAntCumuloByLibIdLibAntCum(
										lLibAntCumMod.getIdLibAnticipataCumulo());
								Vector<PeriodoLibAntCumuloModel> lListaPeriodiLibAnt = new Vector(
										lPeriodoLibAntSqlDao.getModels());
								if (lListaPeriodiLibAnt != null && lListaPeriodiLibAnt.size() > 0) {
									lLibAntCumMod.setListaPeriodiLibAnticipate(lListaPeriodiLibAnt);
								}
								lListaLiberazioni.set(j, lLibAntCumMod);
								lPeriodoLibAntSqlDao.stop();
							} // end for j

							lStato.setListaLiberazioniAnticipate(lListaLiberazioni);
						}

						lLibAntSqlDao.stop();
					} // end for y
				}
				lTitolo.setStatoEsecuzioneTitoloCumulato(lVectStati);

				// Preparazione Vettore di Output.
				VecTitoliOutput.add(lTitolo);
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaTitoliDiLibAntPerRichiesta: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSETCSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lTitSqlDao);
			cleanup(lLibAntSqlDao);
			cleanup(lPeriodoLibAntSqlDao);

			cleanup(lConn);
		}

		return VecTitoliOutput;
	}

	/**
	 * Ricerca le LibAnticipataCumulo afferenti a un TitoloCumulato
	 *
	 * @param aKey
	 *            - Chiave Titolo cumulato
	 * @return Vettore di Lib_Anticipata_Cumulo
	 * @throws F3BException
	 */
	public Vector<LibAnticipataCumuloModel> ExRicercaLibAntCumByTitoloCum(BigDecimal aTitoloKey)
			throws F3BException {

		Connection lConn = null;
		LibAnticipataCumuloSqlDAO lLibAntCumSqlDao = null;
		Vector<LibAnticipataCumuloModel> lListLibAntCum = null;

		try {
			lConn = getDBConnection();
			lLibAntCumSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
			lLibAntCumSqlDao.ricercaLibAnticipataCumuloByIdTitolo(aTitoloKey);
			lListLibAntCum = new Vector(lLibAntCumSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestePmInCumuloController.ExRicercaLibAntCumByTitoloCum: " + daoEx);
		} finally {
			cleanup(lLibAntCumSqlDao);
			cleanup(lConn);
		}

		return lListLibAntCum;
	}

	/**************************************************************************************
	 * Si Effettua l'inserimento di una RICHIESTE_PM_IN_CUMULO a partire dal Model; Si Inserisce inoltre la
	 * relazione con StatoEsecTitCumulato in RICHPM_STATO_ESEC_CUM
	 *
	 * @param aRichiestePmInCumulo
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 **************************************************************************************/
	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_SORV_RevocaLA(
			RichiestePmInCumuloModel aRichPmInCum, Vector<TitoloCumulatoModel> VecTitoli)
			throws F3BException {

		siesLogger.debug("--XX-- >>>> Start ExInserisciRichiestePmInCumulo_SORV_RevocaLA ");

		Connection lConn = null;
		RichiestePmInCumuloDAO lRicDao = null;
		RichiestePmInCumuloModel lRicMod = null;
		BigDecimal lKeyRich = null;
		BigDecimal lKeyTito = null;

		StatoEsecTitoloCumulatoModel lSETCMod = null;

		RichPMTitoloCumDAO lRicPmTitoDao = null;
		RichPMStatoEsecCumDAO lRicPmStatoEsecDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicDao.setDAOFromModel(aRichPmInCum);
			lKeyRich = lRicDao.insert();
			lRicDao.stop();

			// siesLogger.debug("--XX-- >>>> Inserito RichPM_in_Cumulo - id = "+lKeyRich);

			lRicPmTitoDao = new RichPMTitoloCumDAO(lConn);
			lRicPmStatoEsecDao = new RichPMStatoEsecCumDAO(lConn);

			siesLogger.debug("--XX-- >>>> Size VecTitoli = " + VecTitoli.size());

			Iterator itx = VecTitoli.iterator();
			while (itx.hasNext()) {
				TitoloCumulatoModel lTitoloMod = (TitoloCumulatoModel) itx.next();
				if (lTitoloMod != null && lTitoloMod.getIdTitoloCumulato() != null) {
					siesLogger.debug(
							"--XX-- >>>> Inserito Inserimento Relazione RICHIESTE_PM_IN_CUMULO --> TITOLO_CUMULATO (in RICHPM_TITOLO_CUM) - Richiesta = "
									+ lKeyRich + " - Titolo = " + lKeyTito);
					lKeyTito = lTitoloMod.getIdTitoloCumulato();
					// Inserimento Relazione RICHIESTE_PM_IN_CUMULO --> TITOLO_CUMULATO (in RICHPM_TITOLO_CUM)
					lRicPmTitoDao.setRichIdRichiestePMinCumulo(lKeyRich);
					lRicPmTitoDao.setTitIdTitoloCumulato(lKeyTito);
					lRicPmTitoDao.insert();
					// lRicPmTitoDao.stop();
					// siesLogger.debug("--XX-- >>>> Inserito RICHIESTE_PM_IN_CUMULO / TITOLO_CUMULATO -
					// Richiesta = "+lKeyRich+" - Titolo = "+lKeyTito);
				}

				if (lTitoloMod.getStatoEsecuzioneTitoloCumulato() != null
						&& lTitoloMod.getStatoEsecuzioneTitoloCumulato().size() > 0) {
					Iterator itSE = lTitoloMod.getStatoEsecuzioneTitoloCumulato().iterator();
					while (itSE.hasNext()) {
						// Inserimento tabella Relazione STATO_ESEC_TITOLO_CUMULATO / TITOLO_CUMULATO
						lSETCMod = (StatoEsecTitoloCumulatoModel) itSE.next();
						if (lSETCMod != null && lSETCMod.getIdStatoEsecTitoloCumulato() != null) {
							lRicPmStatoEsecDao
									.setStatoIdStatoEsecTitCumulo(lSETCMod.getIdStatoEsecTitoloCumulato());
							lRicPmStatoEsecDao.setRichIdRichiestePMinCumulo(lKeyRich);
							lRicPmStatoEsecDao.insert();
							// lRicPmStatoEsecDao.stop();
						}
					}
				}
			}

			lRicMod = new RichiestePmInCumuloModel(aRichPmInCum);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestePmInCumulo(lKeyRich);

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiestePmInCumulo_SORV_RevocaLA: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmTitoDao);
			cleanup(lRicPmStatoEsecDao);

			cleanup(lConn);
		}

		return lRicMod;
	} // Chiudi ExInserisciRichiestePmInCumulo_SORV_RevocaLA()

	/**************************************************************************************
	 * Ricerca dei Benefici per i quali è stata fatta una Richiesta di Revoca; I Benefici sono trovati
	 * attraverso la Tab. di Relazione RichPM_Beneficio_Cum
	 *
	 * @param aTitoloCum
	 *            Model e Id Richiesta
	 * @return aTitoloCum model arricchito con l'aggiunta della lista dei Benefici
	 * @throws F3BException
	 **************************************************************************************/
	public TitoloCumulatoModel ExRicercaRichPMBeneficioCum(TitoloCumulatoModel aTitoloCum,
			BigDecimal aIdRichiesta) throws F3BException {

		siesLogger.debug("--XX-- >>>> Start ExRicercaRichPMBeneficioCum ");

		Connection lConn = null;

		BeneficioCumuloSqlDAO lBenSqlDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStatEsecSqlDao = null;
		ComputiCumuloSqlDAO lCompSqlDao = null;

		Vector<ComputiCumuloModel> lVecComp = new Vector();
		ComputiCumuloModel lCompMod = null;

		try {
			lConn = getDBConnection();
			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
			lStatEsecSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lCompSqlDao = new ComputiCumuloSqlDAO(lConn);

			// Benefici collegati alla Richiesta
			lBenSqlDao.ricercaBeneficioCumuloByIdTitoloCumRichGE(aTitoloCum.getIdTitoloCumulato(),
					aIdRichiesta);
			Vector<BeneficioCumuloModel> VecBen = new Vector(lBenSqlDao.getModels());
			if (VecBen != null && VecBen.size() > 0)
				aTitoloCum.setBeneficiCumulo(VecBen);

			// Stato_Esecuzione_Titolo_Cumulato
			lStatEsecSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitoloCumRichGE(
					aTitoloCum.getIdTitoloCumulato(), aIdRichiesta);
			lStatEsecSqlDao.start();
			int k = 0;
			while (lStatEsecSqlDao.next()) {
				k = k + 1;
				if (k == 1) {
					StatoEsecTitoloCumulatoModel lStato = (StatoEsecTitoloCumulatoModel) lStatEsecSqlDao
							.getModel();
					if (lStato != null && lStato.getIdStatoEsecTitoloCumulato() != null) {
						lCompSqlDao.ricercaComputiCumuloByIdStatoEsec(lStato.getIdStatoEsecTitoloCumulato());
						lCompSqlDao.start();
						while (lCompSqlDao.next()) {
							lCompMod = (ComputiCumuloModel) lCompSqlDao.getModel();
							if (lCompMod != null && lCompMod.getIdComputiCumulo() != null) {

								if ("002".equals(lCompMod.getCodTipoAnnotazione())
										|| "003".equals(lCompMod.getCodTipoAnnotazione())) {
									lVecComp.addElement(lCompMod);
								}
							}
						}

						lStato.setListaComputi(lVecComp);
						aTitoloCum.setStatoEsecTitoloCumulato(lStato);
					}
				}
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaRichPMBeneficioCum: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lBenSqlDao);
			cleanup(lStatEsecSqlDao);
			cleanup(lCompSqlDao);
			cleanup(lConn);
		}

		return aTitoloCum;
	} // Chiude ExRicercaRichPMBeneficioCum()

	/**************************************************************************************
	 * Si Effettua l'inserimento di una RICHIESTE_PM_IN_CUMULO a partire dal Model; Si Inserisce inoltre la
	 * relazione con MisuraSicurezzaCumulo in RICHPM_MISSICUR_CUM
	 *
	 * @param aRichiestePmInCumulo
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 **************************************************************************************/
	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_SORV_UnificaMS(
			RichiestePmInCumuloModel aRichPmInCum, String[] lListaTitoli, String[] lListaMisureSicurezza)
			throws F3BException {

		siesLogger.debug("--XX-- >>>> Start ExInserisciRichiestePmInCumulo_SORV_UnificaMS ");

		Connection lConn = null;
		RichiestePmInCumuloDAO lRicDao = null;
		RichPMTitoloCumDAO lRicPmTitDao = null;
		RichPMMisSicurCumDAO lRicPmMisSicDao = null;

		RichiestePmInCumuloModel lRicMod = null;
		BigDecimal lKeyRich = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicDao.setDAOFromModel(aRichPmInCum);
			lKeyRich = lRicDao.insert();
			lRicDao.stop();
			lRicPmTitDao = new RichPMTitoloCumDAO(lConn);
			for (int ij = 0; ij < lListaTitoli.length; ij++) {
				// Inserimento Relazione tra RICHIESTE_PM_IN_CUMULO --> TITOLO_CUMULATO (in RICHPM_TIOLO_CUM)
				lRicPmTitDao.setRichIdRichiestePMinCumulo(lKeyRich);
				lRicPmTitDao.setTitIdTitoloCumulato(new BigDecimal(lListaTitoli[ij]));
				lRicPmTitDao.insert();
			}

			lRicPmMisSicDao = new RichPMMisSicurCumDAO(lConn);
			for (int ii = 0; ii < lListaMisureSicurezza.length; ii++) {
				// Inserimento Relazione tra RICHIESTE_PM_IN_CUMULO --> MISURE_SICUREZZA_CUMULO (in
				// RICHPM_MISSICUR_CUM)
				lRicPmMisSicDao.setRichIdRichiestePMinCumulo(lKeyRich);
				lRicPmMisSicDao.setMisIdMisSicurCumulo(new BigDecimal(lListaMisureSicurezza[ii]));
				lRicPmMisSicDao.insert();
			}

			lRicMod = new RichiestePmInCumuloModel(aRichPmInCum);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestePmInCumulo(lKeyRich);

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiestePmInCumulo_SORV_UnificaMS: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmMisSicDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lRicPmTitDao);

			cleanup(lConn);
		}

		return lRicMod;
	} // Chiudi ExInserisciRichiestePmInCumulo_SORV_UnificaMS()

	/*****************************************************************************
	 * Effettua l'inserimento di una RichiestePmInCumulo a partire dai dati contenuti nel Model; (Richiesta di
	 * REVOCA Benefici) Inserisce anche le Tab. di Conversione RichPm_Sanzione_Sost_Cum
	 * (RICHIESTE_PM_IN_CUMULO con SANZIONE_SOST_CUM) e RichPm_Titolo_Cum (RICHIESTE_PM_IN_CUMULO con
	 * TITOLO_CUMULATO)
	 *
	 * @param aRichiestePmInCumulo
	 *            Model con i dati da inserire e ListaIdTitoli con gli Id dei Titoli coinvolti
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_RevocaSS(
			RichiestePmInCumuloModel aRichPmInCum, String[] ListaIdTitoli, String[] ListaIdSanzioni)
			throws F3BException {

		siesLogger.debug("--XX-- >>>> Start ExInserisciRichiestePmInCumulo_GE_RevocaSS ");

		Connection lConn = null;
		RichiestePmInCumuloDAO lRicDao = null;
		RichPMTitoloCumDAO lRicPmTitDao = null;
		RichPMSanzioneSostCumDAO lRicPMSSCumDao = null;

		RichiestePmInCumuloModel lRicMod = null;
		BigDecimal lKeyRich = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicPmTitDao = new RichPMTitoloCumDAO(lConn);
			lRicPMSSCumDao = new RichPMSanzioneSostCumDAO(lConn);

			// Inserimento Richiesta di Revoca
			lRicDao.setDAOFromModel(aRichPmInCum);
			lKeyRich = lRicDao.insert();
			lRicDao.stop();

			if (ListaIdTitoli != null && ListaIdTitoli.length > 0) {
				for (int i = 0; i < ListaIdTitoli.length; i++) {
					// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / TITOLO_CUMULATO
					lRicPmTitDao.setRichIdRichiestePMinCumulo(lKeyRich);
					lRicPmTitDao.setTitIdTitoloCumulato(new BigDecimal(ListaIdTitoli[i]));
					lRicPmTitDao.insert();
					lRicPmTitDao.stop();

					// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / SANZIONE_SOST_CUM
					lRicPMSSCumDao.setRichIdRichiestePMinCumulo(lKeyRich);
					lRicPMSSCumDao.setSSIdSanzioneSostCumulo(new BigDecimal(ListaIdSanzioni[i]));
					lRicPMSSCumDao.insert();
					lRicPMSSCumDao.stop();
				}

				lRicMod = new RichiestePmInCumuloModel(aRichPmInCum);
				lRicMod.setMessage("Inserimento avvenuto correttamente!");
				lRicMod.setIdRichiestePmInCumulo(lKeyRich);
			}

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiestePmInCumulo_GE_RevocaSS: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmTitDao);
			cleanup(lRicPMSSCumDao);
			cleanup(lConn);
		}

		return lRicMod;
	} // Chiude ExInserisciRichiestePmInCumulo_GE_RevocaSS()

	public Vector<TitoloCumulatoModel> ExRicercaTitoli_e_SSCumByRichiestaGE(BigDecimal aIdRichiesta)
			throws F3BException {

		Connection lConn = null;
		Vector<TitoloCumulatoModel> lVec = new Vector<>();

		TitoloCumulatoSqlDAO lTitSqlDao = null;
		RichPMTitoloCumSqlDAO lRicSqlDao = null;
		SanzioneSostitutivaCumuloSqlDAO lSSCumSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichPMTitoloCumSqlDAO(lConn);
			lTitSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lSSCumSqlDao = new SanzioneSostitutivaCumuloSqlDAO(lConn);

			lRicSqlDao.ricercaRichPmTitoloCumByRichIdRich(aIdRichiesta);
			lRicSqlDao.start();

			while (lRicSqlDao.next()) {
				RichPMTitoloCumModel lRicmod = (RichPMTitoloCumModel) lRicSqlDao.getModel();

				// Titolo
				TitoloCumulatoModel lTitMod = null;
				lTitSqlDao.ricercaTitoloCumulatoByKey(lRicmod.getTitIdTitoloCumulato());
				lTitMod = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();

				// Sanzione Sost Cumulo
				lSSCumSqlDao.ricercaSanzioneSostitutivaCumByTitoloCumRichGE(lRicmod.getTitIdTitoloCumulato(),
						lRicmod.getRicIdRichiestePmInCumulo());
				lSSCumSqlDao.start();
				lSSCumSqlDao.next(); // c'è solo una Sanzione Sost
				SanzioneSostitutivaCumuloModel lSSCumModel = (SanzioneSostitutivaCumuloModel) lSSCumSqlDao
						.getModel();
				lSSCumSqlDao.stop();

				if (lSSCumModel != null && lSSCumModel.getIdSanzioneSostitutivaCum() != null)
					lTitMod.setSanzioneSostitutivaCumulo(lSSCumModel);

				// Appendo il TitoloModel (con tutt le Entità correlate)
				lVec.add(lTitMod);
			}

			lRicSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaTitoli_e_SSCumByRichiestaGE: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lTitSqlDao);
			cleanup(lSSCumSqlDao);
			cleanup(lRicSqlDao);
			cleanup(lConn);
		}

		return lVec;
	} // Chiude ExRicercaTitoli_e_SSCumByRichiestaGE

	public Vector<RichPMMisSicCumModel> ExRicercaRichPMMisSicCum(BigDecimal aRichIdRichiesta)
			throws F3BException {

		Connection lConn = null;
		Vector<RichPMMisSicCumModel> lVec = null;
		// RichiestePmInCumuloModel lRichiestePmInCumuloMod = new RichiestePmInCumuloModel();
		RichPMMisSicCumSqlDAO lRicPMMSSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicPMMSSqlDao = new RichPMMisSicCumSqlDAO(lConn);
			lRicPMMSSqlDao.ricercaRichPmMisSicCumByRichIdRich(aRichIdRichiesta);

			lVec = new Vector<RichPMMisSicCumModel>(lRicPMMSSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaRichPMMisSicCum: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRicPMMSSqlDao);
			cleanup(lConn);
		}

		return lVec;
	}

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_SostituzionePenaAccCum(
			RichiestePmInCumuloModel aRichPmInCum, BigDecimal aTitoloKey, String[] listaIdPeneAcc)
			throws F3BException {

		siesLogger.debug("--XX-- >>>> Start ExInserisciRichiestePmInCumulo_GE_SostituzionePenaAccCum ");

		Connection lConn = null;
		RichiestePmInCumuloDAO lRicDao = null;
		RichPMTitoloCumDAO lRicPmTitDao = null;
		RichPMPenAccCumDAO lRicPMPACumDao = null;

		RichiestePmInCumuloModel lRicMod = null;
		BigDecimal lKeyRich = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicPmTitDao = new RichPMTitoloCumDAO(lConn);
			lRicPMPACumDao = new RichPMPenAccCumDAO(lConn);

			// Inserimento Richiesta di Revoca
			lRicDao.setDAOFromModel(aRichPmInCum);
			lKeyRich = lRicDao.insert();
			lRicDao.stop();

			// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / TITOLO_CUMULATO
			lRicPmTitDao.setRichIdRichiestePMinCumulo(lKeyRich);
			lRicPmTitDao.setTitIdTitoloCumulato(aTitoloKey);
			lRicPmTitDao.insert();
			lRicPmTitDao.stop();

			if (listaIdPeneAcc != null && listaIdPeneAcc.length > 0) {
				for (int i = 0; i < listaIdPeneAcc.length; i++) {

					// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / PENA_ACCESSORIA_CUMULO
					lRicPMPACumDao.setRichIdRichiestePMinCumulo(lKeyRich);
					lRicPMPACumDao.setPenIdPenAccCumulo(new BigDecimal(listaIdPeneAcc[i]));
					lRicPMPACumDao.insert();
					lRicPMPACumDao.stop();
				}

				lRicMod = new RichiestePmInCumuloModel(aRichPmInCum);
				lRicMod.setMessage("Inserimento avvenuto correttamente!");
				lRicMod.setIdRichiestePmInCumulo(lKeyRich);
			}

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiestePmInCumulo_GE_SostituzionePenaAccCum: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmTitDao);
			cleanup(lRicPMPACumDao);
			cleanup(lConn);
		}

		return lRicMod;
	} // chiude ExInserisciRichiestePmInCumulo_GE_SostituzionePenaAccCum()

	public TitoloCumulatoModel ExRicercaTitolo_e_PeneAccCumByRichiestaGE(BigDecimal aIdRichiesta)
			throws F3BException {

		TitoloCumulatoModel lTitMod = null;
		Vector<PenaAccessoriaCumuloModel> lVec = new Vector<>();

		Connection lConn = null;
		TitoloCumulatoSqlDAO lTitSqlDao = null;
		RichPMTitoloCumSqlDAO lRicSqlDao = null;
		PenaAccessoriaCumuloSqlDAO lPACumSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichPMTitoloCumSqlDAO(lConn);
			lTitSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lPACumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);

			// Ricerca RichPm_Titolo_Cum by Id_Richiesta_PM_in_Cumulo
			lRicSqlDao.ricercaRichPmTitoloCumByRichIdRich(aIdRichiesta);
			lRicSqlDao.start();
			while (lRicSqlDao.next()) {
				RichPMTitoloCumModel lRicmod = (RichPMTitoloCumModel) lRicSqlDao.getModel();

				// Ricerca Titolo
				lTitSqlDao.ricercaTitoloCumulatoByKey(lRicmod.getTitIdTitoloCumulato());
				lTitMod = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();

				// Ricerca Pena Accessoria Cumulo
				lPACumSqlDao.ricercaPenaAccessoriaCumuloByTitoloCumRichGE(lRicmod.getTitIdTitoloCumulato(),
						lRicmod.getRicIdRichiestePmInCumulo());
				lPACumSqlDao.start();
				while (lPACumSqlDao.next()) {
					PenaAccessoriaCumuloModel lPACumMod = (PenaAccessoriaCumuloModel) lPACumSqlDao.getModel();
					if (lPACumMod != null && lPACumMod.getIdPenaAccessoriaCumulo() != null) {
						lVec.add(lPACumMod);
					}
				}

				lPACumSqlDao.stop();
			}

			// Se esistono Pene_Accessorie, vengono aggregate al Titolo_Cumulato_Model
			if (lVec != null && lVec.size() > 0) {
				lTitMod.setPeneAccessorieCumulo(lVec);
			}

			lRicSqlDao.stop();
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaTitolo_e_PeneAccCumByRichiestaGE: Non posso leggere: "
							+ ex);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lTitSqlDao);
			cleanup(lPACumSqlDao);
			cleanup(lConn);
		}

		return lTitMod;
	} // Chiude ExRicercaTitolo_e_PeneAccCumByRichiestaGE()

	public Vector<TitoloCumulatoModel> ExRicercaTitoli_e_PeneAccCumByRichiestaGE(BigDecimal aIdRichiesta)
			throws F3BException {

		TitoloCumulatoModel lTitMod = null;
		Vector<TitoloCumulatoModel> lVecTito = new Vector<>();

		Connection lConn = null;
		TitoloCumulatoSqlDAO lTitSqlDao = null;
		RichPMTitoloCumSqlDAO lRicSqlDao = null;
		PenaAccessoriaCumuloSqlDAO lPACumSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichPMTitoloCumSqlDAO(lConn);
			lTitSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lPACumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);

			// Ricerca RichPm_Titolo_Cum by Id_Richiesta_PM_in_Cumulo
			lRicSqlDao.ricercaRichPmTitoloCumByRichIdRich(aIdRichiesta);
			lRicSqlDao.start();
			while (lRicSqlDao.next()) {
				RichPMTitoloCumModel lRicmod = (RichPMTitoloCumModel) lRicSqlDao.getModel();

				// Ricerca Titolo by Id_Titolo
				lTitSqlDao.ricercaTitoloCumulatoByKey(lRicmod.getTitIdTitoloCumulato());
				lTitMod = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();

				// Ricerca Pena Accessoria Cumulo by Id_Titolo e Id_richiesa
				Vector<PenaAccessoriaCumuloModel> lVec = new Vector<>();
				lPACumSqlDao.ricercaPenaAccessoriaCumuloByTitoloCumRichGE(lRicmod.getTitIdTitoloCumulato(),
						lRicmod.getRicIdRichiestePmInCumulo());
				lPACumSqlDao.start();
				while (lPACumSqlDao.next()) {
					PenaAccessoriaCumuloModel lPACumMod = (PenaAccessoriaCumuloModel) lPACumSqlDao.getModel();
					if (lPACumMod != null && lPACumMod.getIdPenaAccessoriaCumulo() != null) {
						lVec.add(lPACumMod);
					}
				}

				lPACumSqlDao.stop();

				// Se esistono Pene_Accessorie, vengono aggregate al Titolo_Cumulato_Model
				if (lVec != null && lVec.size() > 0) {
					lTitMod.setPeneAccessorieCumulo(lVec);
				}

				// Add del Titolo al vector
				lVecTito.add(lTitMod);
			}

			lRicSqlDao.stop();
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaTitoli_e_PeneAccCumByRichiestaGE: Non posso leggere: "
							+ ex);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lTitSqlDao);
			cleanup(lPACumSqlDao);
			cleanup(lConn);
		}

		return lVecTito;
	} // Chiude ExRicercaTitoli_e_PeneAccCumByRichiestaGE()

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_RevocaPenaAccCum(
			RichiestePmInCumuloModel aRichPmInCum, String[] lIdTitoli_PeneAcc) throws F3BException {

		siesLogger.debug("--XX-- >>>> Start ExInserisciRichiestePmInCumulo_GE_RevocaPenaAccCum ");

		Connection lConn = null;

		RichiestePmInCumuloDAO lRicDao = null;
		RichPMPenAccCumDAO lRicPmPADao = null;
		RichPMTitoloCumDAO lRicPmTitDao = null;

		RichiestePmInCumuloModel lRicMod = null;
		BigDecimal lKeyRich = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicPmPADao = new RichPMPenAccCumDAO(lConn);
			lRicPmTitDao = new RichPMTitoloCumDAO(lConn);

			// Inserimento Richiesta di Revoca
			lRicDao.setDAOFromModel(aRichPmInCum);
			lKeyRich = lRicDao.insert();
			lRicDao.stop();

			// Ciclo con inserimento tabelle di relazione
			String lIdTitolo = "";
			String lIdPA = "";
			String salvaTitolo = "";
			String Ele = "";
			for (int i = 0; i < lIdTitoli_PeneAcc.length; i++) {
				Ele = lIdTitoli_PeneAcc[i];
				String[] EleSpl = Ele.split(";");

				lIdTitolo = EleSpl[0];
				lIdPA = EleSpl[1];

				if (!lIdTitolo.equals(salvaTitolo)) {
					salvaTitolo = lIdTitolo;

					// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / TITOLO_CUMULATO
					lRicPmTitDao.setRichIdRichiestePMinCumulo(lKeyRich);
					lRicPmTitDao.setTitIdTitoloCumulato(new BigDecimal(lIdTitolo));
					lRicPmTitDao.insert();
					lRicPmTitDao.stop();
				}

				// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / PENA_ACCESSORIA_CUMULO
				lRicPmPADao.setRichIdRichiestePMinCumulo(lKeyRich);
				lRicPmPADao.setPenIdPenAccCumulo(new BigDecimal(lIdPA));
				lRicPmPADao.insert();
				lRicPmPADao.stop();

			}

			lRicMod = new RichiestePmInCumuloModel(aRichPmInCum);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestePmInCumulo(lKeyRich);

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiestePmInCumulo_GE_RevocaPenaAccCum: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmTitDao);
			cleanup(lRicPmPADao);
			cleanup(lConn);
		}

		return lRicMod;
	} // Chiude ExInserisciRichiestePmInCumulo_GE_RevocaPenaAccCum()

	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_GE_ApplicazionePenaAccCum(
			RichiestePmInCumuloModel aRichPmInCum) throws F3BException {

		siesLogger.debug("--XX-- >>>> Start ExInserisciRichiestePmInCumulo_GE_ApplicazionePenaAccCum ");

		Connection lConn = null;
		RichPMTitoloCumDAO lRicPmTitDao = null;
		RichiestePmInCumuloDAO lRicDao = null;

		RichiestePmInCumuloModel lRicMod = null;
		BigDecimal lKeyRich = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicPmTitDao = new RichPMTitoloCumDAO(lConn);

			// Inserimento Richiesta di Applicazione Pena Accessoria
			lRicDao.setDAOFromModel(aRichPmInCum);
			lKeyRich = lRicDao.insert();
			lRicDao.stop();

			// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / TITOLO_CUMULATO
			lRicPmTitDao.setRichIdRichiestePMinCumulo(lKeyRich);
			lRicPmTitDao.setTitIdTitoloCumulato(aRichPmInCum.getTitIdTitoloCumulato());
			lRicPmTitDao.insert();
			lRicPmTitDao.stop();

			lRicMod = new RichiestePmInCumuloModel(aRichPmInCum);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestePmInCumulo(lKeyRich);

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiestePmInCumulo_GE_ApplicazionePenaAccCum: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmTitDao);
			cleanup(lConn);
		}

		return lRicMod;
	}

	public TitoloCumulatoModel ExRicercaTitolo_ByRichiestaGE(BigDecimal aIdRichiesta) throws F3BException {

		TitoloCumulatoModel lTitMod = null;

		Connection lConn = null;
		TitoloCumulatoSqlDAO lTitSqlDao = null;
		RichPMTitoloCumSqlDAO lRicSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichPMTitoloCumSqlDAO(lConn);
			lTitSqlDao = new TitoloCumulatoSqlDAO(lConn);

			// Ricerca RichPm_Titolo_Cum by Id_Richiesta_PM_in_Cumulo
			lRicSqlDao.ricercaRichPmTitoloCumByRichIdRich(aIdRichiesta);
			lRicSqlDao.start();
			while (lRicSqlDao.next()) {
				RichPMTitoloCumModel lRicmod = (RichPMTitoloCumModel) lRicSqlDao.getModel();

				// Ricerca Titolo
				lTitSqlDao.ricercaTitoloCumulatoByKey(lRicmod.getTitIdTitoloCumulato());
				lTitMod = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();

			}

			lRicSqlDao.stop();
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaTitolo_ByRichiestaGE: Non posso leggere: " + ex);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lTitSqlDao);
			cleanup(lConn);
		}

		return lTitMod;
	} // Chiude ExRicercaTitolo_ByRichiestaGE()

	public RichiestePmInCumuloModel ExModificaRichiestePmInCumuloERichPMTitoloCum(
			RichiestePmInCumuloModel aRichiestePmInCumulo) throws F3BException {

		Connection lConn = null;
		RichiestePmInCumuloDAO lRicDao = null;
		RichiestePmInCumuloSqlDAO lRicSqlDao = null;
		RichPMTitoloCumDAO lRicPmTitDao = null;

		RichiestePmInCumuloModel lRicMod = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicSqlDao = new RichiestePmInCumuloSqlDAO(lConn);

			// Ricerca Richiesta
			lRicSqlDao.ricercaRichiestePmInCumuloByKey(aRichiestePmInCumulo.getIdRichiestePmInCumulo());
			lRicMod = (RichiestePmInCumuloModel) lRicSqlDao.getModelByKey();

			// Modifica Richiesta
			lRicDao.setDAOFromModelForUpdate(aRichiestePmInCumulo);
			lRicDao.selCondizioneUpdate(aRichiestePmInCumulo.getIdRichiestePmInCumulo());
			lRicDao.update();

			// Modifica RichPMTitoloCum
			if (!lRicMod.getTitIdTitoloCumulato().equals(aRichiestePmInCumulo.getTitIdTitoloCumulato())) {
				lRicPmTitDao = new RichPMTitoloCumDAO(lConn);

				// Elimino Tabella RICHPM_TITOLO_CUM
				lRicPmTitDao.selCondizioneUpdate(aRichiestePmInCumulo.getIdRichiestePmInCumulo());
				lRicPmTitDao.delete();
				lRicPmTitDao.stop();

				// Rinserisco la Nuova RICPM_TITOLO_CUM
				lRicPmTitDao.setRichIdRichiestePMinCumulo(aRichiestePmInCumulo.getIdRichiestePmInCumulo());
				lRicPmTitDao.setTitIdTitoloCumulato(aRichiestePmInCumulo.getTitIdTitoloCumulato());
				lRicPmTitDao.insert();
				lRicPmTitDao.stop();
			}

			commit(lConn);

			lRicMod.setMessage("Modifica effettuata correttamente!");
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"RichiestePmInCumuloController.ExModificaRichiestePmInCumuloERichPMTitoloCum: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicPmTitDao);
			cleanup(lRicSqlDao);
			cleanup(lRicDao);
			cleanup(lConn);
		}

		return lRicMod;
	} // Chiude ExModificaRichiestePmInCumuloERichPMTitoloCum

	// ======================================================================
	// Inserimento Richiesta (Tipo = Altre Richieste Cod=014) del PM alla SORVEGLIANZA.
	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_RichPMTitoloCum_SORV(
			RichiestePmInCumuloModel aRichPmInCum) throws F3BException {

		siesLogger.debug("--XX-- >>>> Start ExInserisciRichiestePmInCumulo_RichPMTitoloCum_SORV ");

		Connection lConn = null;
		RichPMTitoloCumDAO lRicPmTitDao = null;
		RichiestePmInCumuloDAO lRicDao = null;

		RichiestePmInCumuloModel lRicMod = null;
		BigDecimal lKeyRich = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicPmTitDao = new RichPMTitoloCumDAO(lConn);

			// Inserimento Richiesta di tipo: Altre Richieste (Cod = 014)
			lRicDao.setDAOFromModel(aRichPmInCum);
			lKeyRich = lRicDao.insert();
			lRicDao.stop();

			// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / TITOLO_CUMULATO
			lRicPmTitDao.setRichIdRichiestePMinCumulo(lKeyRich);
			lRicPmTitDao.setTitIdTitoloCumulato(aRichPmInCum.getTitIdTitoloCumulato());
			lRicPmTitDao.insert();
			lRicPmTitDao.stop();

			lRicMod = new RichiestePmInCumuloModel(aRichPmInCum);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestePmInCumulo(lKeyRich);

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiestePmInCumulo_RichPMTitoloCum_SORV: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmTitDao);
			cleanup(lConn);
		}

		return lRicMod;
	} // Chiude ExInserisciRichiestePmInCumulo_RichPMTitoloCum_SORV()

	// Inserimento Richiesta (Tipo = 031 Richieste Revoca M.A.) del PM alla SORVEGLIANZA.
	public RichiestePmInCumuloModel ExInserisciRichiestePmInCumulo_SORV_RevocaMisAlt(
			RichiestePmInCumuloModel aRichPmInCum, BigDecimal aIdStatoEsec) throws F3BException {

		siesLogger.debug("--XX-- >>>> Start ExInserisciRichiestePmInCumulo_SORV_RevocaMisAlt ");

		Connection lConn = null;
		RichPMTitoloCumDAO lRicPmTitDao = null;
		RichPMStatoEsecCumDAO lRicPmStatoCumDao = null;
		RichiestePmInCumuloDAO lRicDao = null;

		RichiestePmInCumuloModel lRicMod = null;
		BigDecimal lKeyRich = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicPmTitDao = new RichPMTitoloCumDAO(lConn);
			lRicPmStatoCumDao = new RichPMStatoEsecCumDAO(lConn);

			// Inserimento Richiesta di tipo: Altre Richieste (Cod = 014)
			lRicDao.setDAOFromModel(aRichPmInCum);
			lKeyRich = lRicDao.insert();
			lRicDao.stop();

			// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / TITOLO_CUMULATO
			lRicPmTitDao.setRichIdRichiestePMinCumulo(lKeyRich);
			lRicPmTitDao.setTitIdTitoloCumulato(aRichPmInCum.getTitIdTitoloCumulato());
			lRicPmTitDao.insert();
			lRicPmTitDao.stop();

			// Inserimento tabella Relazione RICHIESTE_PM_IN_CUMULO / STATO_ESEC_TITOLO_CUMULATO
			lRicPmStatoCumDao.setRichIdRichiestePMinCumulo(lKeyRich);
			lRicPmStatoCumDao.setStatoIdStatoEsecTitCumulo(aIdStatoEsec);
			lRicPmStatoCumDao.insert();
			lRicPmStatoCumDao.stop();

			lRicMod = new RichiestePmInCumuloModel(aRichPmInCum);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestePmInCumulo(lKeyRich);

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExInserisciRichiestePmInCumulo_SORV_RevocaMisAlt: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmTitDao);
			cleanup(lRicPmStatoCumDao);
			cleanup(lConn);
		}

		return lRicMod;
	} // Chiude ExInserisciRichiestePmInCumulo_SORV_RevocaMisAlt()

	public TitoloCumulatoModel ExRicercaTitolo_e_StatoEsecTitoloCumByRichiestaGE(BigDecimal aIdRichiesta)
			throws F3BException {

		TitoloCumulatoModel lTitMod = null;

		Connection lConn = null;

		TitoloCumulatoSqlDAO lTitSqlDao = null;
		RichPMTitoloCumSqlDAO lRicSqlDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStesecTiCumsqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichPMTitoloCumSqlDAO(lConn);
			lTitSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lStesecTiCumsqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);

			// Ricerca RichPm_Titolo_Cum by Id_Richiesta_PM_in_Cumulo
			lRicSqlDao.ricercaRichPmTitoloCumByRichIdRich(aIdRichiesta);
			lRicSqlDao.start();
			while (lRicSqlDao.next()) {
				RichPMTitoloCumModel lRicmod = (RichPMTitoloCumModel) lRicSqlDao.getModel();

				// Ricerca Titolo
				lTitSqlDao.ricercaTitoloCumulatoByKey(lRicmod.getTitIdTitoloCumulato());
				lTitMod = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();

				// Ricerca Stato_Esec_Titolo_Cumulato
				lStesecTiCumsqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(lRicmod.getTitIdTitoloCumulato(),
						null, lRicmod.getRicIdRichiestePmInCumulo());
				lStesecTiCumsqlDao.start();
				while (lStesecTiCumsqlDao.next()) {
					StatoEsecTitoloCumulatoModel lStatEseMod = (StatoEsecTitoloCumulatoModel) lStesecTiCumsqlDao
							.getModel();
					if (lStatEseMod != null && lStatEseMod.getIdStatoEsecTitoloCumulato() != null) {
						lTitMod.setStatoEsecTitoloCumulato(lStatEseMod);
					}
				}
				lStesecTiCumsqlDao.stop();

			}

			lRicSqlDao.stop();
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"RichiestePmInCumuloController.ExRicercaTitolo_e_StatoEsecTitoloCumByRichiestaGE: Non posso leggere: "
							+ ex);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lTitSqlDao);
			cleanup(lStesecTiCumsqlDao);
			cleanup(lConn);
		}

		return lTitMod;
	} // Chiude ExRicercaTitolo_e_StatoEsecTitoloCumByRichiestaGE()

	/*****************************************************************************
	 * Insert di tutte le Tabelle di Relazione per le Richieste PM; Provvedimento_GE_SORV è fatto in modalità
	 * 'NO SEQUENCE', senza utilizzare le sequnce. il valore della Primary_Key è già preimpostato; metodo
	 * usato nella funzione di presa in carico, per scaricare Tutti i dati del Fascicolo sulla nuova Base
	 * dati.
	 *
	 * @param Key
	 *            RichiestePmInCumulo
	 * @throws F3BException
	 ****************************************************************************/
	public String ExinserisciTabellediRelazioneWithoutSequence(RichiestePmInCumuloModel aRichPmInCumMod,
			Connection lConn) throws F3BException {

		siesLogger.info("--XX-- INIZIO ExinserisciTabellediRelazioneWithoutSequence --------------> ");

		String EsitodiRitorno = "00000";
		String NomeTab = "";

		RichiestePmInCumuloDAO lRicDao = null;
		RichPMTitoloCumDAO lRicPmTitoDao = null;
		RichPMMisSicurCumDAO lRicPmMisSicDao = null;
		RichPMPenAccCumDAO lRicPmPenAccDao = null;
		RichPMReatoCumDAO lRicPmReaDao = null;
		RichPMStatoEsecCumDAO lRicPmSECDao = null;
		RichPMBeneficioCumDAO lRicBenCumDao = null;
		RichPMSanzioneSostCumDAO lRicSSCumDao = null;

		try {
			lRicDao = new RichiestePmInCumuloDAO(lConn);
			lRicPmTitoDao = new RichPMTitoloCumDAO(lConn);
			lRicPmMisSicDao = new RichPMMisSicurCumDAO(lConn);
			lRicPmPenAccDao = new RichPMPenAccCumDAO(lConn);
			lRicPmReaDao = new RichPMReatoCumDAO(lConn);
			lRicPmSECDao = new RichPMStatoEsecCumDAO(lConn);
			lRicBenCumDao = new RichPMBeneficioCumDAO(lConn);
			lRicSSCumDao = new RichPMSanzioneSostCumDAO(lConn);

			// Tabella RICHPM_STATO_ESEC_CUM
			if (aRichPmInCumMod.getListaRichPmStatoEsecCum() != null
					&& aRichPmInCumMod.getListaRichPmStatoEsecCum().size() > 0) {
				NomeTab = "RICHPM_STATO_ESEC_CUM";
				RichPMStatoEsecCumModel RicStesecMod = new RichPMStatoEsecCumModel();
				Iterator ItxS = aRichPmInCumMod.getListaRichPmStatoEsecCum().iterator();
				while (ItxS.hasNext()) {
					try {
						RicStesecMod = (RichPMStatoEsecCumModel) ItxS.next();
						lRicPmSECDao.setRichIdRichiestePMinCumulo(RicStesecMod.getRicIdRichiestePmInCumulo());
						lRicPmSECDao.setStatoIdStatoEsecTitCumulo(RicStesecMod.getStatIdStatoEsecCumulo());
						lRicPmSECDao.insert();
						lRicPmSECDao.stop();
					} catch (DAOException daoEx) {
						siesLogger.error("DAOException: ", daoEx);
						if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.warn("Tabella " + NomeTab + " (id: "
									+ RicStesecMod.getRicIdRichiestePmInCumulo() + "-"
									+ RicStesecMod.getStatIdStatoEsecCumulo()
									+ ") già presente per Id_Richiesta = >"
									+ aRichPmInCumMod.getIdRichiestePmInCumulo() + "<");
							EsitodiRitorno = "00001";
						} else {
							throw daoEx;
						}
					}
				}
			}

			// Tabella RICHPM_TITOLO_CUM
			if (aRichPmInCumMod.getListaRichPmTitoloCum() != null
					&& aRichPmInCumMod.getListaRichPmTitoloCum().size() > 0) {
				NomeTab = "RICHPM_TITOLO_CUM";
				RichPMTitoloCumModel RicTitoMod = new RichPMTitoloCumModel();
				Iterator ItxT = aRichPmInCumMod.getListaRichPmTitoloCum().iterator();
				while (ItxT.hasNext()) {
					try {
						RicTitoMod = (RichPMTitoloCumModel) ItxT.next();
						lRicPmTitoDao.setRichIdRichiestePMinCumulo(RicTitoMod.getRicIdRichiestePmInCumulo());
						lRicPmTitoDao.setTitIdTitoloCumulato(RicTitoMod.getTitIdTitoloCumulato());
						lRicPmTitoDao.insert();
						lRicPmTitoDao.stop();
					} catch (DAOException daoEx) {
						siesLogger.error("DAOException: ", daoEx);
						if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.warn(
									"Tabella " + NomeTab + " (id: " + RicTitoMod.getRicIdRichiestePmInCumulo()
											+ "-" + RicTitoMod.getTitIdTitoloCumulato()
											+ ") già presente per Id_Richiesta = >"
											+ aRichPmInCumMod.getIdRichiestePmInCumulo() + "<");
							EsitodiRitorno = "00001";
						} else {
							throw daoEx;
						}
					}
				}
			}

			// Tabella RICHPM_REATO_CUM
			if (aRichPmInCumMod.getListaRichPmReatoCum() != null
					&& aRichPmInCumMod.getListaRichPmReatoCum().size() > 0) {
				NomeTab = "RICHPM_REATO_CUM";
				RichPMReatoCumModel RicReaMod = new RichPMReatoCumModel();
				Iterator ItxR = aRichPmInCumMod.getListaRichPmReatoCum().iterator();
				while (ItxR.hasNext()) {
					try {
						RicReaMod = (RichPMReatoCumModel) ItxR.next();
						lRicPmReaDao.setRichIdRichiestePMinCumulo(RicReaMod.getRicIdRichiestePmInCumulo());
						lRicPmReaDao.setReaIdReatoCumulo(RicReaMod.getReaIdReatoCumulo());
						lRicPmReaDao.insert();
						lRicPmReaDao.stop();
					} catch (DAOException daoEx) {
						siesLogger.error("DAOException: ", daoEx);
						if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.warn("Tabella " + NomeTab + " (id: "
									+ RicReaMod.getRicIdRichiestePmInCumulo() + "-"
									+ RicReaMod.getReaIdReatoCumulo() + ") già presente per Id_Richiesta = >"
									+ aRichPmInCumMod.getIdRichiestePmInCumulo() + "<");
							EsitodiRitorno = "00001";
						} else {
							throw daoEx;
						}
					}
				}
			}

			// Tabella RICHPM_MISSICUR_CUM
			if (aRichPmInCumMod.getListaRichPmMisureSicurezzaCum() != null
					&& aRichPmInCumMod.getListaRichPmMisureSicurezzaCum().size() > 0) {
				NomeTab = "RICHPM_MISSICUR_CUM";
				RichPMMisSicCumModel RicMisSicMod = new RichPMMisSicCumModel();
				Iterator ItxM = aRichPmInCumMod.getListaRichPmMisureSicurezzaCum().iterator();
				while (ItxM.hasNext()) {
					try {
						RicMisSicMod = (RichPMMisSicCumModel) ItxM.next();
						lRicPmMisSicDao
								.setRichIdRichiestePMinCumulo(RicMisSicMod.getRicIdRichiestePmInCumulo());
						lRicPmMisSicDao.setMisIdMisSicurCumulo(RicMisSicMod.getMisIdMisSicCumulo());
						lRicPmMisSicDao.insert();
						lRicPmMisSicDao.stop();
					} catch (DAOException daoEx) {
						siesLogger.error("DAOException: ", daoEx);
						if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.warn("Tabella " + NomeTab + " (id: "
									+ RicMisSicMod.getRicIdRichiestePmInCumulo() + "-"
									+ RicMisSicMod.getMisIdMisSicCumulo()
									+ ") già presente per Id_Richiesta = >"
									+ aRichPmInCumMod.getIdRichiestePmInCumulo() + "<");
							EsitodiRitorno = "00001";
						} else {
							throw daoEx;
						}
					}
				}
			}

			// Tabella RICHPM_BENEFICIO_CUM
			if (aRichPmInCumMod.getListaRichPmBeneficioCum() != null
					&& aRichPmInCumMod.getListaRichPmBeneficioCum().size() > 0) {
				NomeTab = "RICHPM_BENEFICIO_CUM";
				RichPMBeneficioCumModel RicBeneMod = new RichPMBeneficioCumModel();
				Iterator itxB = aRichPmInCumMod.getListaRichPmBeneficioCum().iterator();
				while (itxB.hasNext()) {
					try {
						RicBeneMod = (RichPMBeneficioCumModel) itxB.next();
						lRicBenCumDao.setRichIdRichiestePMinCumulo(RicBeneMod.getRicIdRichiestePmInCumulo());
						lRicBenCumDao.setBenIdBeneficioCumulo(RicBeneMod.getBenIdBeneficioCum());
						lRicBenCumDao.insert();
						lRicBenCumDao.stop();
					} catch (DAOException daoEx) {
						siesLogger.error("DAOException: ", daoEx);
						if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.warn(
									"Tabella " + NomeTab + " (id: " + RicBeneMod.getRicIdRichiestePmInCumulo()
											+ "-" + RicBeneMod.getBenIdBeneficioCum()
											+ ") già presente per Id_Richiesta = >"
											+ aRichPmInCumMod.getIdRichiestePmInCumulo() + "<");
							EsitodiRitorno = "00001";
						} else {
							throw daoEx;
						}
					}
				}
			}

			// Tabella RICHPM_PENACC_CUM
			if (aRichPmInCumMod.getListaRichPmPenaAccessoriaCum() != null
					&& aRichPmInCumMod.getListaRichPmPenaAccessoriaCum().size() > 0) {
				NomeTab = "RICHPM_PENACC_CUM";
				RichPMPenAccCumModel RicPenAccMod = new RichPMPenAccCumModel();
				Iterator itxP = aRichPmInCumMod.getListaRichPmPenaAccessoriaCum().iterator();
				while (itxP.hasNext()) {
					try {
						RicPenAccMod = (RichPMPenAccCumModel) itxP.next();
						lRicPmPenAccDao
								.setRichIdRichiestePMinCumulo(RicPenAccMod.getRicIdRichiestePmInCumulo());
						lRicPmPenAccDao.setPenIdPenAccCumulo(RicPenAccMod.getPenIdPenaAccessoriaCumulo());
						lRicPmPenAccDao.insert();
						lRicPmPenAccDao.stop();
					} catch (DAOException daoEx) {
						siesLogger.error("DAOException: ", daoEx);
						if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.warn("Tabella " + NomeTab + " (id: "
									+ RicPenAccMod.getRicIdRichiestePmInCumulo() + "-"
									+ RicPenAccMod.getPenIdPenaAccessoriaCumulo()
									+ ") già presente per Id_Richiesta = >"
									+ aRichPmInCumMod.getIdRichiestePmInCumulo() + "<");
							EsitodiRitorno = "00001";
						} else {
							throw daoEx;
						}
					}
				}
			}

			// Tabella RICHPM_SANZIONE_SOST_CUM
			if (aRichPmInCumMod.getListaRichPmSanzioneSostCum() != null
					&& aRichPmInCumMod.getListaRichPmSanzioneSostCum().size() > 0) {
				NomeTab = "RICHPM_SANZIONE_SOST_CUM";
				RichPMSanSostCumModel RicSSMod = new RichPMSanSostCumModel();
				Iterator itxS = aRichPmInCumMod.getListaRichPmSanzioneSostCum().iterator();
				while (itxS.hasNext()) {
					try {
						RicSSMod = (RichPMSanSostCumModel) itxS.next();
						lRicSSCumDao.setRichIdRichiestePMinCumulo(RicSSMod.getRicIdRichiestePmInCumulo());
						lRicSSCumDao.setSSIdSanzioneSostCumulo(RicSSMod.getSanIdSanSostCumulo());
						lRicSSCumDao.insert();
						lRicSSCumDao.stop();
					} catch (DAOException daoEx) {
						siesLogger.error("DAOException: ", daoEx);
						if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.warn("Tabella " + NomeTab + " (id: "
									+ RicSSMod.getRicIdRichiestePmInCumulo() + "-"
									+ RicSSMod.getSanIdSanSostCumulo() + ") già presente per Id_Richiesta = >"
									+ aRichPmInCumMod.getIdRichiestePmInCumulo() + "<");
							EsitodiRitorno = "00001";
						} else {
							throw daoEx;
						}
					}
				}
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
				siesLogger.error("Tabella " + NomeTab + " già presente per Id_Richiesta = >"
						+ aRichPmInCumMod.getIdRichiestePmInCumulo() + "<");
				EsitodiRitorno = "00001";
			} else {
				EsitodiRitorno = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire " + NomeTab + " ! ");
			}
		} finally {
			cleanup(lRicDao);
			cleanup(lRicPmTitoDao);
			cleanup(lRicPmMisSicDao);
			cleanup(lRicPmPenAccDao);
			cleanup(lRicPmReaDao);
			cleanup(lRicPmSECDao);
			cleanup(lRicBenCumDao);
			cleanup(lRicSSCumDao);
		}

		return EsitodiRitorno;
	}

} // Chiude il Controller