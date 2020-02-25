package siap.sige.titoloesecutivo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.note.dao.NoteDAO;
import siap.sico.note.model.NoteModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSigeDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaFascicoloSigeModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sige.beneficio.dao.BeneficioSenSigeDAO;
import siap.sige.circostanza.dao.CircostanzaSenSigeDAO;
import siap.sige.fascicolo.dao.FascicoloSigeDAO;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.penaaccessoria.dao.PenaAccSenSigeDAO;
import siap.sige.penacomplessiva.dao.PenaCompSenSigeDAO;
import siap.sige.reato.dao.ReatoSentenzaSigeDAO;
import siap.sige.richiesta.dao.RichiestaSigeDAO;
import siap.sige.richiesta.model.RichiestaSigeModel;
import siap.sige.sentenza.dao.FasSigeSentenzaDAO;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sius.SIUSException;
import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
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
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TitoloEsecutivoController extends GenericController implements ITitoloEsecutivo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public FascicoloSigeEstesoModel ExAssegnaTitoloEsecutivo(FascicoloSigeEstesoModel aFascicoloSigeEsteso,
			FascicoloSiepModel lFasSiepMod, FascicoloSiepModel lFasSiepOld) throws F3BException {
		Connection lConn = null;

		FascicoloSigeDAO lFasSigeDao = null;
		FasSigeSentenzaDAO lFasSigeSentenzaDao = null;

		try {
			lConn = getDBTransaction();

			lFasSigeDao = new FascicoloSigeDAO(lConn);
			lFasSigeSentenzaDao = new FasSigeSentenzaDAO(lConn);

			// Set del DAO e aggiornamento del FascicoloSige.
			lFasSigeDao.setDAOFromModelForAssegnaTitoloEsecutivo(aFascicoloSigeEsteso);
			lFasSigeDao.update();
			lFasSigeDao.stop();

			// Update sulla tabella FAS_SIGE_SENTENZA
			lFasSigeSentenzaDao.setDAOFromModelForAssegnaTitoloEsecutivo(aFascicoloSigeEsteso, lFasSiepMod);
			lFasSigeSentenzaDao.update();
			lFasSigeSentenzaDao.stop();

			// Inserimento/Correzione delle Residenze.
			this.insResidenzeSige(aFascicoloSigeEsteso, lConn);

			// Inserimento Note.
			this.inserimentoNote(aFascicoloSigeEsteso, lFasSiepMod, lFasSiepOld, lConn);

			// COMMIT
			commit(lConn);

		} catch (SQLException sqe) {
			rollback(lConn);
			throw new F3BException("TitoloEsecutivoController.ExAssegnaTitoloEsecutivo: " + sqe);
		} finally {
			cleanup(lFasSigeDao);
			cleanup(lFasSigeSentenzaDao);
			cleanup(lConn);
		}

		return aFascicoloSigeEsteso;
	}

	/**
	 * Metodo di correzione delle RESIDENZA_FASCICOLO_SIGE a partire da aFascicoloSigeEsteso, per effetto di
	 * modifica Soggetto.
	 * <p>
	 * 
	 * @param aFascicoloSigeEsteso
	 * @param lConn
	 * @throws F3BException
	 */
	private void insResidenzeSige(FascicoloSigeEstesoModel aFascicoloSigeEsteso, Connection lConn)
			throws F3BException {
		ResidenzaDAO lResDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		ResidenzaFascicoloSigeDAO lResFasSigeDao = null;

		try {
			lResDao = new ResidenzaDAO(lConn);
			lResSqlDao = new ResidenzaSqlDAO(lConn);
			lResFasSigeDao = new ResidenzaFascicoloSigeDAO(lConn);

			// Puntamento alle eventuali Residenze riferite al Procedimento SIGE.
			lResSqlDao.ricercaResidenzaByFascicoloSige(aFascicoloSigeEsteso.getFascicoloSige()
					.getIdFascicoloSige());

			// Lettura dei dati delle residenze.
			lResSqlDao.start();

			ResidenzaModel lResMod = null;
			ResidenzaFascicoloSigeModel lResFasMod = null;
			while (lResSqlDao.next()) {
				lResMod = (ResidenzaModel) lResSqlDao.getModel();
				lResFasMod = lResSqlDao.getModelResidenzaFascicoloSige();
				BigDecimal lKeyRes = null;

				// Si duplicano le residenze per consentire l'associazione al nuovo soggetto.
				if (lResMod != null) {
					lResMod.setCodOperatoreInserimento(aFascicoloSigeEsteso.getFascicoloSige()
							.getCodOperatoreAggiornamento());
					lResMod.setDataInserimento(aFascicoloSigeEsteso.getFascicoloSige().getDataAggiornamento());
					lResMod.setCodUfficioInserimento(aFascicoloSigeEsteso.getFascicoloSige()
							.getCodUfficioAggiornamento());
					lResMod.setSogIdSoggetto(aFascicoloSigeEsteso.getFascicoloSige().getSogIdSoggetto());
					lResDao.setDAOFromModel(lResMod);
					lKeyRes = lResDao.insert();
					lResDao.stop();
				}
				// Aggiornamento della ResidenzaFascicoloSige.
				if (lResFasMod != null) {
					lResFasMod.setResIdResidenza(lKeyRes);
					lResFasSigeDao.setDAOFromModelForUpdate(lResFasMod);
					lResFasSigeDao.setCondizioneFasicoloResidenza(aFascicoloSigeEsteso.getFascicoloSige()
							.getIdFascicoloSige(), lKeyRes);
					lResFasSigeDao.update();
					lResFasSigeDao.stop();
				}
			}

			lResSqlDao.stop();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE, "TitoloEsecutivoController.insResidenzeSige: "
					+ daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {

			cleanup(lResDao);
			cleanup(lResSqlDao);
			cleanup(lResFasSigeDao);

		}
	}

	/**
	 * Metodo di inserimento delle NOTE per la RIDEFINIZIONE TITOLO ESECUTIVO.
	 * <p>
	 * 
	 * @param aFascicoloSigeEsteso
	 * @param lFasSiepMod
	 * @param lFasSiepOld
	 * @param lConn
	 * @throws F3BException
	 */
	private void inserimentoNote(FascicoloSigeEstesoModel aFascicoloSigeEsteso,
			FascicoloSiepModel lFasSiepMod, FascicoloSiepModel lFasSiepOld, Connection lConn)
			throws F3BException {
		NoteDAO lNoteDao = null;
		try {
			lNoteDao = new NoteDAO(lConn);
			// BigDecimal lKeyRes = null;

			// Caricamento della Nota di Ridefinizione Titolo Esecutivo.
			NoteModel lNoteMod = new NoteModel();

			lNoteMod.setData(DateUtils.getSysDate());
			lNoteMod.setDescrizione("RIDEFINITO TITOLO ESECUTIVO DAL " + lFasSiepOld.getChiaveAnno() + "/"
					+ lFasSiepOld.getChiaveProgr() + " " + lFasSiepOld.getDescrTipoUfficio() + " di "
					+ lFasSiepOld.getDescrComuneUfficio() + " AL " + lFasSiepMod.getChiaveAnno() + "/"
					+ lFasSiepMod.getChiaveProgr() + " " + lFasSiepMod.getDescrTipoUfficio() + " di "
					+ lFasSiepMod.getDescrComuneUfficio());
			lNoteMod.setCodOperatoreInserimento(aFascicoloSigeEsteso.getFascicoloSige()
					.getCodOperatoreAggiornamento());
			lNoteMod.setDataInserimento(aFascicoloSigeEsteso.getFascicoloSige().getDataAggiornamento());
			lNoteMod.setCodUfficioInserimento(aFascicoloSigeEsteso.getFascicoloSige()
					.getCodUfficioAggiornamento());
			lNoteMod.setFasSigeIdFascicoloSige(aFascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige());
			lNoteMod.setFasSieIdFascicoloSiep(lFasSiepMod.getIdFascicoloSiep());

			lNoteDao.setDAOFromModel(lNoteMod);
			/* lKeyRes = */lNoteDao.insert();
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
	 * Metodo di inserimento delle NOTE per la DEASSEGNAZIONE TITOLO ESECUTIVO.
	 * <p>
	 * 
	 * @param aFascicoloSigeEsteso
	 * @param lConn
	 * @throws F3BException
	 */
	private void inserimentoNoteDeassegnaTitoloEsecutivo(FascicoloSigeEstesoModel aFascicoloSigeEsteso,
			String chiaveAnnoSiep, String chiaveProgSiep, Connection lConn) throws F3BException {
		NoteDAO lNoteDao = null;
		try {
			lNoteDao = new NoteDAO(lConn);
			// BigDecimal lKeyRes = null;

			// Caricamento della Nota di Ridefinizione Titolo Esecutivo.
			NoteModel lNoteMod = new NoteModel();

			lNoteMod.setData(DateUtils.getSysDate());

			// Data de assegnazione||’ Deassegnato Titolo Esecutivo ‘||anno/numero Siep||’ ‘||descrizione
			// ufficio emittente||’ di ‘||sede.
			lNoteMod.setDescrizione(" DEASSEGNATO TITOLO ESECUTIVO " + chiaveAnnoSiep + "/" + chiaveProgSiep
					+ " " + aFascicoloSigeEsteso.getFascicoloSiep().getDescrTipoUfficio() + " di "
					+ aFascicoloSigeEsteso.getFascicoloSiep().getDescrComuneUfficio());
			lNoteMod.setCodOperatoreInserimento(aFascicoloSigeEsteso.getFascicoloSige()
					.getCodOperatoreAggiornamento());
			lNoteMod.setDataInserimento(aFascicoloSigeEsteso.getFascicoloSige().getDataAggiornamento());
			lNoteMod.setCodUfficioInserimento(aFascicoloSigeEsteso.getFascicoloSige()
					.getCodUfficioAggiornamento());
			lNoteMod.setFasSigeIdFascicoloSige(aFascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige());
			lNoteMod.setFasSieIdFascicoloSiep(aFascicoloSigeEsteso.getFascicoloSiep().getIdFascicoloSiep());

			lNoteDao.setDAOFromModel(lNoteMod);
			/* lKeyRes = */lNoteDao.insert();
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

	public FascicoloSigeEstesoModel ExDeassegnaTitoloEsecutivo(FascicoloSigeEstesoModel aFascicoloSigeEsteso,
			String chiaveAnnoSiep, String chiaveProgSiep) throws F3BException {
		Connection lConn = null;

		FascicoloSigeDAO lFasSigeDao = null;
		FasSigeSentenzaDAO lFasSigeSentenzaDao = null;
		RichiestaSigeDAO lRichiestaSigeDao = null;
		RichiestaSigeModel lRicMod = null;
		Vector lFasSentenze = null;
		FasSigeSentenzaDAO lFasDao = null;
		PenaCompSenSigeDAO lPenComSenSigeDAO = null;
		PenaAccSenSigeDAO lPenaAccSigeDAO = null;
		ReatoSentenzaSigeDAO lReaSenDAO = null;
		CircostanzaSenSigeDAO lCircostanzaSigeDAO = null;
		BeneficioSenSigeDAO lBeneficioSigeDAO = null;

		try {

			lConn = getDBTransaction();

			lFasSigeDao = new FascicoloSigeDAO(lConn);
			lFasSigeSentenzaDao = new FasSigeSentenzaDAO(lConn);

			// Set del DAO e aggiornamento del FascicoloSige.
			lFasSigeDao.setDAOFromModelForAssegnaTitoloEsecutivo(aFascicoloSigeEsteso);
			lFasSigeDao.update();
			lFasSigeDao.stop();

			// Update sulla tabella FAS_SIGE_SENTENZA
			lFasSigeSentenzaDao.setDAOFromModelForAssegnaTitoloEsecutivo(aFascicoloSigeEsteso);
			lFasSigeSentenzaDao.update();
			lFasSigeSentenzaDao.stop();

			// Modifica del 01/12/2016 MEV_15_S4
			// In fase di "Deassegnazione Titolo Esecutivo" viene eliminato ogni
			// riferimento al Fascicolo Siep collegato
			// Ricerca della Richiesta associata al Fascicolo Sige
			if (aFascicoloSigeEsteso.getFascicoloSige() != null
					&& aFascicoloSigeEsteso.getFascicoloSige().getRicIdRichiestaSige() != null) {
				lRichiestaSigeDao = new RichiestaSigeDAO(lConn);
				lRichiestaSigeDao.setIdRichiestaSige(aFascicoloSigeEsteso.getFascicoloSige()
						.getRicIdRichiestaSige());
				lRichiestaSigeDao.selByKey();
				lRicMod = (RichiestaSigeModel) lRichiestaSigeDao.getModelByKey();

				if (lRicMod != null) {
					// Aggiornamento Richiesta settando a null il campo FAS_SIE_ID_FASCICOLO_SIEP
					lRicMod.setFasSieIdFascicoloSiep(null);
					lRicMod.setCodOperatoreAggiornamento(aFascicoloSigeEsteso.getFascicoloSige()
							.getCodOperatoreAggiornamento());
					lRicMod.setCodUfficioAggiornamento(aFascicoloSigeEsteso.getFascicoloSige()
							.getCodUfficioAggiornamento());
					lRicMod.setDataAggiornamento(aFascicoloSigeEsteso.getFascicoloSige()
							.getDataAggiornamento());

					lRichiestaSigeDao.setDAOFromModelForUpdateDeassegnazione(lRicMod);
					lRichiestaSigeDao.update();
				}
			}

			// Modifica del 13/12/2016 MEV_15_S4
			// In fase di "Deassegnazione Titolo Esecutivo" bisogna eliminare il "Titolo
			// Esecutivo di Competenza" (Sentenza N. del ....)
			// sulla tabella FAS_SIGE_SENTENZA e sulle tabelle collegate
			// Si ricerca la sentenza di competenza associata al fascicolo Sige
			SentenzaSigeModel aFasSigeSentenza = new SentenzaSigeModel();
			aFasSigeSentenza.setFasIdFascicoloSige(aFascicoloSigeEsteso.getFascicoloSige()
					.getIdFascicoloSige());
			aFasSigeSentenza.setFlagCompetenza("S");
			lFasSentenze = ExRicercaFasSigeSentenza(aFasSigeSentenza);
			if (lFasSentenze != null) {
				Iterator itx = lFasSentenze.iterator();
				SentenzaSigeModel lFasSigeSen = null;
				while (itx.hasNext()) {
					lFasSigeSen = (SentenzaSigeModel) itx.next();
				}

				if (lFasSigeSen != null && lFasSigeSen.getIdSentenza() != null) {
					// Cancellazione dei riferimenti nella tabella PENA_COMPLESSIVA_SENTENZA_SIGE
					lPenComSenSigeDAO = new PenaCompSenSigeDAO(lConn);
					lPenComSenSigeDAO.setCondizioneIdFasSigeSen(lFasSigeSen.getIdFasSigeSentenza());
					lPenComSenSigeDAO.delete();

					// Cancellazione dei riferimenti nella tabella PENA_ACCESSORIA_SENTENZA_SIGE
					lPenaAccSigeDAO = new PenaAccSenSigeDAO(lConn);
					lPenaAccSigeDAO.setCondizioneIdFasSigeSen(lFasSigeSen.getIdFasSigeSentenza());
					lPenaAccSigeDAO.delete();

					// Cancellazione dei riferimenti nella tabella REATO_SENTENZA_SIGE
					lReaSenDAO = new ReatoSentenzaSigeDAO(lConn);
					lReaSenDAO.setCondizioneIdFasSigeSen(lFasSigeSen.getIdFasSigeSentenza());
					lReaSenDAO.delete();

					// Cancellazione dei riferimenti nella tabella CIRCOSTANZA_SENTENZA_SIGE
					lCircostanzaSigeDAO = new CircostanzaSenSigeDAO(lConn);
					lCircostanzaSigeDAO.setCondizioneIdFasSigeSen(lFasSigeSen.getIdFasSigeSentenza());
					lCircostanzaSigeDAO.delete();

					// Cancellazione dei riferimenti nella tabella BENEFICIO_SENTENZA_SIGE
					lBeneficioSigeDAO = new BeneficioSenSigeDAO(lConn);
					lBeneficioSigeDAO.setCondizioneIdFasSigeSen(lFasSigeSen.getIdFasSigeSentenza());
					lBeneficioSigeDAO.delete();

					// Cancellazione del record in FAS_SIGE_SENTENZA
					lFasDao = new FasSigeSentenzaDAO(lConn);
					lFasDao.setCondizioneUpdate(lFasSigeSen.getIdFasSigeSentenza());
					lFasDao.delete();
					lFasDao.stop();
				}

			}

			// Inserimento Note.
			this.inserimentoNoteDeassegnaTitoloEsecutivo(aFascicoloSigeEsteso, chiaveAnnoSiep,
					chiaveProgSiep, lConn);

			// COMMIT
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TitoloEsecutivoController.ExDeassegnaTitoloEsecutivo:  : " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("TitoloEsecutivoController.ExDeassegnaTitoloEsecutivo: " + sqe);
		} finally {
			cleanup(lConn);
			cleanup(lRichiestaSigeDao);
			cleanup(lFasSigeDao);
			cleanup(lFasSigeSentenzaDao);
			cleanup(lPenComSenSigeDAO);
			cleanup(lPenaAccSigeDAO);
			cleanup(lReaSenDAO);
			cleanup(lCircostanzaSigeDAO);
			cleanup(lBeneficioSigeDAO);
			cleanup(lFasDao);
		}

		return aFascicoloSigeEsteso;
	}

	/**
	 * La funzione effettua ricerca nella sola tabella FAS_SIGE_SENTENZA.
	 */
	public Vector ExRicercaFasSigeSentenza(SentenzaSigeModel aFasSigeSentenza) throws F3BException {
		Connection lConn = null;
		Vector lFasSigeSentenze = new Vector();
		FasSigeSentenzaDAO lFasSenDao = null;

		try {
			lConn = getDBConnection();
			lFasSenDao = new FasSigeSentenzaDAO(lConn);
			lFasSenDao.setCondizione(aFasSigeSentenza);
			lFasSigeSentenze = new Vector(lFasSenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("TitoloEsecutivoController.ExRicercaFasSigeSentenza: " + daoEx);
		} catch (Exception sqe) {
			throw new F3BException("TitoloEsecutivoController.ExRicercaFasSigeSentenza -> " + sqe);
		} finally {
			cleanup(lFasSenDao);
			cleanup(lConn);
		}

		return lFasSigeSentenze;
	}

}