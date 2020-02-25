package siap.sius.titoloesecutivo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.note.dao.NoteDAO;
import siap.sico.note.model.NoteModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiusDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaFascicoloSiusModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.sius.SIUSException;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.rifasiep.dao.RiferimentoFascicoloSiepSqlDAO;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: TitoloEsecutivoController
 * </p>
 * <p>
 * Description: Classe Controller per TitoloEsecutivo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TitoloEsecutivoController extends SiapController implements ITitoloEsecutivo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public FascicoloGPModel ExAssegnaTitoloEsecutivo(FascicoloGPModel aFascicoloGP,
			FascicoloSiepModel lFasSiepMod) throws F3BException {
		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		LicenzaLibanticipataDAO lLLADao = null; // 18/03/2008

		try {
			lConn = getDBTransaction();

			lFasDao = new FascicoloSiusDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lLLADao = new LicenzaLibanticipataDAO(lConn); // 18/03/2008

			// Set del DAO e aggiornamento del FascicoloSius.
			lFasDao.setDAOFromModelForAssegnaTitoloEsecutivo(aFascicoloGP.getFascicoloSiusModel());
			lFasDao.update();
			lFasDao.stop();

			// Caricamento del model dell'Evento.
			EventoModel aEvento = new EventoModel();
			aEvento.setFasSieIdFascicoloSiep(aFascicoloGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
			aEvento.setFasSiuIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel().getIdFascicoloSius());
			aEvento.setCodOperatoreAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodOperatoreAggiornamento());
			aEvento.setCodUfficioAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodUfficioAggiornamento());
			aEvento.setDataAggiornamento(DateUtils.getSysDate());

			// Set del DAO e aggiornamento dell'Evento.
			lEveDao.setDAOFromModelForUpdateIdFascicoloSius(aEvento);

			lEveDao.selCondizioneUpdateXIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel()
					.getIdFascicoloSius());
			lEveDao.update();
			lEveDao.stop();

			// Set del DAO e aggiornamento delle Notifiche.
			lNotDao.setDAOFromModelForUpdateIdSoggetto(aEvento, aFascicoloGP.getFascicoloSiusModel()
					.getSogIdSoggetto());
			lNotDao.update();
			lNotDao.stop();

			// 18/03/2008 Caricamento del model della LicenzaLibAnticipata.
			LicenzaLibAnticipataModel aLicLibAnticipata = new LicenzaLibAnticipataModel();
			aLicLibAnticipata.setFasSieIdFascicoloSiep(aFascicoloGP.getFascicoloSiusModel()
					.getFasSieIdFascicoloSiep());
			aLicLibAnticipata.setFasSiuIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel()
					.getIdFascicoloSius());
			aLicLibAnticipata.setCodOperatoreAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodOperatoreAggiornamento());
			aLicLibAnticipata.setCodUfficioAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodUfficioAggiornamento());
			aLicLibAnticipata.setDataAggiornamento(DateUtils.getSysDate());

			// 18/03/2008 Set del DAO e aggiornamento della LicenzaLibAnticipata.
			lLLADao.setDAOFromModelForUpdateIdFascicoloSiep(aLicLibAnticipata);

			lLLADao.selCondizioneUpdateXIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel()
					.getIdFascicoloSius());
			lLLADao.update();
			lLLADao.stop();

			// Inserimento/Correzione delle Residenze.
			this.insResidenzeSius(aFascicoloGP, lConn);

			// Inserimento Note.
			this.inserimentoNote(aFascicoloGP, lFasSiepMod, lConn);

			// COMMIT
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TitoloEsecutivoController.ExInserisciTitoloEsecutivo:  : " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lEveDao);
			cleanup(lLLADao); // 18/03/2008
			cleanup(lConn);
		}

		return aFascicoloGP;
	}

	public FascicoloGPModel ExAssegnaTitoloEsecutivo(FascicoloGPModel aFascicoloGP,
			FascicoloSiepModel lFasSiepMod, FascicoloSiepModel lFasSiepOld) throws F3BException {
		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		LicenzaLibanticipataDAO lLLADao = null; // 18/03/2008

		try {
			lConn = getDBTransaction();

			lFasDao = new FascicoloSiusDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lLLADao = new LicenzaLibanticipataDAO(lConn); // 18/03/2008

			// Set del DAO e aggiornamento del FascicoloSius.
			lFasDao.setDAOFromModelForAssegnaTitoloEsecutivo(aFascicoloGP.getFascicoloSiusModel());
			lFasDao.update();
			lFasDao.stop();

			// Caricamento del model dell'Evento.
			EventoModel aEvento = new EventoModel();
			aEvento.setFasSieIdFascicoloSiep(aFascicoloGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
			aEvento.setFasSiuIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel().getIdFascicoloSius());
			aEvento.setCodOperatoreAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodOperatoreAggiornamento());
			aEvento.setCodUfficioAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodUfficioAggiornamento());
			aEvento.setDataAggiornamento(DateUtils.getSysDate());

			// Set del DAO e aggiornamento dell'Evento.
			lEveDao.setDAOFromModelForUpdateIdFascicoloSius(aEvento);

			lEveDao.selCondizioneUpdateXIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel()
					.getIdFascicoloSius());
			lEveDao.update();
			lEveDao.stop();

			// Set del DAO e aggiornamento delle Notifiche.
			lNotDao.setDAOFromModelForUpdateIdSoggetto(aEvento, aFascicoloGP.getFascicoloSiusModel()
					.getSogIdSoggetto());
			lNotDao.update();
			lNotDao.stop();

			// 18/03/2008 Caricamento del model della LicenzaLibAnticipata.
			LicenzaLibAnticipataModel aLicLibAnticipata = new LicenzaLibAnticipataModel();
			aLicLibAnticipata.setFasSieIdFascicoloSiep(aFascicoloGP.getFascicoloSiusModel()
					.getFasSieIdFascicoloSiep());
			aLicLibAnticipata.setFasSiuIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel()
					.getIdFascicoloSius());
			aLicLibAnticipata.setCodOperatoreAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodOperatoreAggiornamento());
			aLicLibAnticipata.setCodUfficioAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodUfficioAggiornamento());
			aLicLibAnticipata.setDataAggiornamento(DateUtils.getSysDate());

			// 18/03/2008 Set del DAO e aggiornamento della LicenzaLibAnticipata.
			lLLADao.setDAOFromModelForUpdateIdFascicoloSiep(aLicLibAnticipata);

			lLLADao.selCondizioneUpdateXIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel()
					.getIdFascicoloSius());
			lLLADao.update();
			lLLADao.stop();

			// Inserimento/Correzione delle Residenze.
			this.insResidenzeSius(aFascicoloGP, lConn);

			// Inserimento Note.
			this.inserimentoNote(aFascicoloGP, lFasSiepMod, lFasSiepOld, lConn);

			// COMMIT
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TitoloEsecutivoController.ExInserisciTitoloEsecutivo:  : " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lEveDao);
			cleanup(lLLADao); // 18/03/2008
			cleanup(lConn);
		}

		return aFascicoloGP;
	}

	public FascicoloGPModel ExDeassegnaTitoloEsecutivo(FascicoloGPModel aFascicoloGP,
			FascicoloSiepModel lFasSiepMod) throws F3BException {
		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		LicenzaLibanticipataDAO lLLADao = null; // 18/03/2008

		try {
			lConn = getDBTransaction();

			lFasDao = new FascicoloSiusDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lLLADao = new LicenzaLibanticipataDAO(lConn); // 18/03/2008

			// Set del DAO e aggiornamento del FascicoloSius.
			lFasDao.setDAOFromModelForAssegnaTitoloEsecutivo(aFascicoloGP.getFascicoloSiusModel());
			lFasDao.update();
			lFasDao.stop();

			// Caricamento del model dell'Evento.
			EventoModel aEvento = new EventoModel();
			aEvento.setFasSieIdFascicoloSiep(null);
			aEvento.setFasSiuIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel().getIdFascicoloSius());
			aEvento.setCodOperatoreAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodOperatoreAggiornamento());
			aEvento.setCodUfficioAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodUfficioAggiornamento());
			aEvento.setDataAggiornamento(DateUtils.getSysDate());

			// Set del DAO e aggiornamento dell'Evento.
			lEveDao.setDAOFromModelForUpdateIdFascicoloSius(aEvento);

			lEveDao.selCondizioneUpdateXIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel()
					.getIdFascicoloSius());
			lEveDao.update();
			lEveDao.stop();

			// Set del DAO e aggiornamento delle Notifiche.
			lNotDao.setDAOFromModelForUpdateIdSoggetto(aEvento, aFascicoloGP.getFascicoloSiusModel()
					.getSogIdSoggetto());
			lNotDao.update();
			lNotDao.stop();

			// 18/03/2008 Caricamento del model della LicenzaLibAnticipata.
			LicenzaLibAnticipataModel aLicLibAnticipata = new LicenzaLibAnticipataModel();
			aLicLibAnticipata.setFasSieIdFascicoloSiep(null);
			aLicLibAnticipata.setFasSiuIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel()
					.getIdFascicoloSius());
			aLicLibAnticipata.setCodOperatoreAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodOperatoreAggiornamento());
			aLicLibAnticipata.setCodUfficioAggiornamento(aFascicoloGP.getFascicoloSiusModel()
					.getCodUfficioAggiornamento());
			aLicLibAnticipata.setDataAggiornamento(DateUtils.getSysDate());

			// 18/03/2008 Set del DAO e aggiornamento della LicenzaLibAnticipata.
			lLLADao.setDAOFromModelForUpdateIdFascicoloSiep(aLicLibAnticipata);

			lLLADao.selCondizioneUpdateXIdFascicoloSius(aFascicoloGP.getFascicoloSiusModel()
					.getIdFascicoloSius());
			lLLADao.update();
			lLLADao.stop();

			// Inserimento/Correzione delle Residenze.
			this.insResidenzeSius(aFascicoloGP, lConn);

			// Inserimento Note.
			// Nessuna nota di inserimento titolo esecutivo
			// Se necessario si metterà nota deinserimento
			this.inserimentoNoteDeassegnazione(aFascicoloGP, lFasSiepMod, lConn);

			// COMMIT
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TitoloEsecutivoController.ExDeassegnaTitoloEsecutivo:  : " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lEveDao);
			cleanup(lLLADao); // 18/03/2008
			cleanup(lConn);
		}

		return aFascicoloGP;
	}

	/**
	 * Metodo di correzione delle RESIDENZA_FASCICOLO_SIUS a partire da aFasGPModel, per effetto di modifica
	 * Soggetto.
	 * <p>
	 * 
	 * @param aFasGPModel
	 * @param lConn
	 * @throws F3BException
	 */
	private void insResidenzeSius(FascicoloGPModel aFasGPModel, Connection lConn) throws F3BException {
		ResidenzaDAO lResDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		ResidenzaFascicoloSiusDAO lResFasSiusDao = null;
		try {
			lResDao = new ResidenzaDAO(lConn);
			lResSqlDao = new ResidenzaSqlDAO(lConn);
			lResFasSiusDao = new ResidenzaFascicoloSiusDAO(lConn);

			// Puntamento alle eventuali Residenze riferite al Procedimento SIUS.
			lResSqlDao.ricercaResidenzeByFascicoloSius(aFasGPModel.getFascicoloSiusModel()
					.getIdFascicoloSius());

			// Lettura dei dati delle residenze.
			lResSqlDao.start();

			ResidenzaModel lResMod = null;
			ResidenzaFascicoloSiusModel lResFasMod = null;
			while (lResSqlDao.next()) {
				lResMod = (ResidenzaModel) lResSqlDao.getModel();
				lResFasMod = lResSqlDao.getModelResidenzaFascicoloSius();
				BigDecimal lKeyRes = null;

				// Si duplicano le residenze per consentire l'associazione al nuovo soggetto.
				if (lResMod != null) {
					lResMod.setCodOperatoreInserimento(aFasGPModel.getFascicoloSiusModel()
							.getCodOperatoreAggiornamento());
					lResMod.setDataInserimento(aFasGPModel.getFascicoloSiusModel().getDataAggiornamento());
					lResMod.setCodUfficioInserimento(aFasGPModel.getFascicoloSiusModel()
							.getCodUfficioAggiornamento());
					lResMod.setSogIdSoggetto(aFasGPModel.getFascicoloSiusModel().getSogIdSoggetto());
					lResDao.setDAOFromModel(lResMod);
					lKeyRes = lResDao.insert();
					lResDao.stop();
				}
				// Aggiornamento della ResidenzaFascicoloSius.
				if (lResFasMod != null) {
					lResFasMod.setResIdResidenza(lKeyRes);
					lResFasSiusDao.setDAOFromModelForUpdate(lResFasMod);
					lResFasSiusDao.setCondizioneFasicoloResidenza(aFasGPModel.getFascicoloSiusModel()
							.getIdFascicoloSius(), lKeyRes);
					lResFasSiusDao.update();
					lResFasSiusDao.stop();
				}
			}

			lResSqlDao.stop();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE, "TitoloEsecutivoController.insResidenzeSius: "
					+ daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {

			cleanup(lResDao);
			cleanup(lResSqlDao);
			cleanup(lResFasSiusDao);

		}
	}

	/**
	 * Metodo di inserimento delle NOTE per l' INSERIMENTO TITOLO ESECUTIVO.
	 * <p>
	 * 
	 * @param aFasGPModel
	 * @param lConn
	 * @throws F3BException
	 */
	private void inserimentoNote(FascicoloGPModel aFasGPModel, FascicoloSiepModel lFasSiepMod,
			Connection lConn) throws F3BException {
		NoteDAO lNoteDao = null;
		try {
			lNoteDao = new NoteDAO(lConn);
//			BigDecimal lKeyRes = null;

			// Caricamento della Nota di Inserimento Titolo Esecutivo.
			NoteModel lNoteMod = new NoteModel();

			lNoteMod.setData(DateUtils.getSysDate());
			lNoteMod.setDescrizione("INSERITO TITOLO ESECUTIVO " + lFasSiepMod.getChiaveAnno() + "/"
					+ lFasSiepMod.getChiaveProgr() + " " + lFasSiepMod.getDescrTipoUfficio() + " di "
					+ lFasSiepMod.getDescrComuneUfficio()
					+ " successivamente all'iscrizione del procedimento");
			lNoteMod.setCodOperatoreInserimento(aFasGPModel.getFascicoloSiusModel()
					.getCodOperatoreAggiornamento());
			lNoteMod.setDataInserimento(aFasGPModel.getFascicoloSiusModel().getDataAggiornamento());
			lNoteMod.setCodUfficioInserimento(aFasGPModel.getFascicoloSiusModel()
					.getCodUfficioAggiornamento());
			lNoteMod.setFasSiuIdFascicoloSius(aFasGPModel.getFascicoloSiusModel().getIdFascicoloSius());
			lNoteMod.setFasSieIdFascicoloSiep(aFasGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

			lNoteDao.setDAOFromModel(lNoteMod);
			/*lKeyRes = */lNoteDao.insert();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE, "TitoloEsecutivoController.inserimentoNote: "
					+ daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lNoteDao);
		}
	}

	/**
	 * Metodo di inserimento delle NOTE per la RIDEFINIZIONE TITOLO ESECUTIVO.
	 * <p>
	 * 
	 * @param aFasGPModel
	 * @param lConn
	 * @throws F3BException
	 */
	private void inserimentoNote(FascicoloGPModel aFasGPModel, FascicoloSiepModel lFasSiepMod,
			FascicoloSiepModel lFasSiepOld, Connection lConn) throws F3BException {
		NoteDAO lNoteDao = null;
		try {
			lNoteDao = new NoteDAO(lConn);
//			BigDecimal lKeyRes = null;

			// Caricamento della Nota di Ridefinizione Titolo Esecutivo.
			NoteModel lNoteMod = new NoteModel();

			lNoteMod.setData(DateUtils.getSysDate());
			lNoteMod.setDescrizione("RIDEFINITO TITOLO ESECUTIVO DAL " + lFasSiepOld.getChiaveAnno() + "/"
					+ lFasSiepOld.getChiaveProgr() + " " + lFasSiepOld.getDescrTipoUfficio() + " di "
					+ lFasSiepOld.getDescrComuneUfficio() + " AL " + lFasSiepMod.getChiaveAnno() + "/"
					+ lFasSiepMod.getChiaveProgr() + " " + lFasSiepMod.getDescrTipoUfficio() + " di "
					+ lFasSiepMod.getDescrComuneUfficio());
			lNoteMod.setCodOperatoreInserimento(aFasGPModel.getFascicoloSiusModel()
					.getCodOperatoreAggiornamento());
			lNoteMod.setDataInserimento(aFasGPModel.getFascicoloSiusModel().getDataAggiornamento());
			lNoteMod.setCodUfficioInserimento(aFasGPModel.getFascicoloSiusModel()
					.getCodUfficioAggiornamento());
			lNoteMod.setFasSiuIdFascicoloSius(aFasGPModel.getFascicoloSiusModel().getIdFascicoloSius());
			lNoteMod.setFasSieIdFascicoloSiep(aFasGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

			lNoteDao.setDAOFromModel(lNoteMod);
			/*lKeyRes = */lNoteDao.insert();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE, "TitoloEsecutivoController.inserimentoNote: "
					+ daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lNoteDao);
		}
	}

	/**
	 * Metodo di inserimento delle NOTE per l' INSERIMENTO TITOLO ESECUTIVO.
	 * <p>
	 * 
	 * @param aFasGPModel
	 * @param lConn
	 * @throws F3BException
	 */
	private void inserimentoNoteDeassegnazione(FascicoloGPModel aFasGPModel, FascicoloSiepModel lFasSiepMod,
			Connection lConn) throws F3BException {
		NoteDAO lNoteDao = null;
		try {
			lNoteDao = new NoteDAO(lConn);
//			BigDecimal lKeyRes = null;

			// Caricamento della Nota di Inserimento Titolo Esecutivo.
			NoteModel lNoteMod = new NoteModel();

			lNoteMod.setData(DateUtils.getSysDate());
			lNoteMod.setDescrizione("DEASSEGNATO TITOLO ESECUTIVO " + lFasSiepMod.getChiaveAnno() + "/"
					+ lFasSiepMod.getChiaveProgr() + " " + lFasSiepMod.getDescrTipoUfficio() + " di "
					+ lFasSiepMod.getDescrComuneUfficio());
			lNoteMod.setCodOperatoreInserimento(aFasGPModel.getFascicoloSiusModel()
					.getCodOperatoreAggiornamento());
			lNoteMod.setDataInserimento(aFasGPModel.getFascicoloSiusModel().getDataAggiornamento());
			lNoteMod.setCodUfficioInserimento(aFasGPModel.getFascicoloSiusModel()
					.getCodUfficioAggiornamento());
			lNoteMod.setFasSiuIdFascicoloSius(aFasGPModel.getFascicoloSiusModel().getIdFascicoloSius());
			lNoteMod.setFasSieIdFascicoloSiep(aFasGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

			lNoteDao.setDAOFromModel(lNoteMod);
			/*lKeyRes = */lNoteDao.insert();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE, "TitoloEsecutivoController.inserimentoNote: "
					+ daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lNoteDao);
		}
	}

	// TODO carmela verificare
	public Vector ExRicercaTitoloEsecutivoByIdFascicoloSius(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		RiferimentoFascicoloSiepSqlDAO lFasDao = null;
		// FascicoloSiusDAO lFasDao = null;
		Vector lRifTitoloEsecutivo = new Vector();
		lRifTitoloEsecutivo.add(new DecodeModel("-", "-"));

		try {
			lConn = getDBConnection();
			lFasDao = new RiferimentoFascicoloSiepSqlDAO(lConn);

			lFasDao.ricercaRifTitoloEsecutivoByIdFascicoloSius(aKey);
			lFasDao.start();

			while (lFasDao.next()) {

				String annoSiep = "";
				String numeroSiep = "";
				String annoNumero = "";

				// TODO carmela
				// Il flag flagRifTitoloEsecutivo deve essere concatenato nella chiave della combo
				// "Riferimento Titolo Esecutivo" per individuare in fase di inserimento della
				// Misura di Sicurezza se trattasi di un Titolo Esecutivo Principale
				// (campo SEN_ID_SENTENZA della tabella MISURA_SICUREZZA) oppure di un Altro Titolo Esecutivo
				// (campo FAS_SIE_ID_FASCICOLO_SIEP_RIF della tabella MISURA_SICUREZZA)

				String lCod = ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
						.getIdRiferimentoFascicoloSiep().toString();

				// Identifica se si tratta di un Titolo Esecutivo Principale o Altro Titolo Esecutivo
				// associato al Procedimento Sius
				String flagRifTitoloEsecutivo = ((RiferimentoFascicoloSiepModel) lFasDao
						.getModelRifTitoloEsec()).getFlagRifTitoloEsecutivo();
				lCod = lCod.concat("/" + flagRifTitoloEsecutivo);

				// Se il titolo esecutivo è svincolato da SIEP, al posto di anno/numero SIEP
				// ci deve essere la scritta (Es. Mis. Sic. oppure anno/MS numero)
				// oppure (Es. Pene Pec. oppure anno/PP numero)
				String flagMS = ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec()).getFlagMS();
				if (flagMS == null || flagMS.equals("") || flagMS.equals("N")) {
					annoSiep = ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
							.getAnnoFascicoloSiep().toString();
					numeroSiep = ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
							.getProgrFascicoloSiep().toString();
					annoNumero = annoSiep + "/" + numeroSiep;
				} else {
					if (((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
							.getAnnoFascicoloSiep() == null
							&& ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
									.getProgrFascicoloSiep() == null) {
						if (flagMS.equals("M")) {
							annoNumero = "Es. Mis. Sic.";
						} else {
							annoNumero = "Es. Pene Pec.";
						}
					} else {
						if (flagMS.equals("M")) {
							annoNumero = ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
									.getAnnoFascicoloSiep()
									+ "/MS "
									+ ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
											.getProgrFascicoloSiep();
						} else {
							annoNumero = ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
									.getAnnoFascicoloSiep()
									+ "/PP "
									+ ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
											.getProgrFascicoloSiep();
						}
					}
				}

				String autoritaEmit = ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
						.getDescrTipoAutoritaEmittente();
				String sedeAutoritaEmit = ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
						.getDescrLuogoEmittente();
				Date dataProvv = ((RiferimentoFascicoloSiepModel) lFasDao.getModelRifTitoloEsec())
						.getDataProvvedimento();
				String dataProvvedimento = DateUtils.getDateToString(dataProvv, "dd/MM/yyyy");

				lRifTitoloEsecutivo.add(new DecodeModel(lCod, annoNumero + " " + autoritaEmit + " "
						+ sedeAutoritaEmit + " del " + dataProvvedimento));
			}

			lFasDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"TitoloEsecutivoController.ExRicercaTitoloEsecutivoByIdFascicoloSius: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lRifTitoloEsecutivo;
	}

}