package siap.siep.misurasicurezza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.jms.ICostantiJMS;
import siap.jms.messaggio.dao.MessaggioDAO;
import siap.jms.messaggio.dao.MessaggioSqlDAO;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.dao.CampoNotaSqlDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.note.dao.NoteDAO;
import siap.sico.note.model.NoteModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiepDAO;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.SIEPException;
import siap.siep.agdgfascicolosiep.dao.AgdgFascicoloSiepDAO;
import siap.siep.agdgfascicolosiep.dao.AgdgFascicoloSiepSqlDAO;
import siap.siep.agdgfascicolosiep.model.AgdgFascicoloSiepModel;
import siap.siep.altracausa.dao.AltraCausaDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.altrigradigiudizio.dao.AltriGradiGiudizioDAO;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import siap.siep.annotazioneesitotrasmissione.dao.AnnotazioneEsitoTrasmissioneDAO;
import siap.siep.annotazioneesitotrasmissione.dao.AnnotazioneEsitoTrasmissioneSqlDAO;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.archiviazione.dao.ArchiviazioneDAO;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepDAO;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza;
import siap.siep.misurasicurezza.dao.FascMsToFascSiepDAO;
import siap.siep.misurasicurezza.dao.FascMsToFascSiepSqlDAO;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaDAO;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaFascicoloModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaNotificataModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sentenza.dao.SentenzaDAO;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenzariunita.dao.SentenzaRiunitaDAO;
import siap.siep.sentenzariunita.dao.SentenzaRiunitaFascSiepDAO;
import siap.siep.sentenzariunita.dao.SentenzaRiunitaFascSiepSqlDAO;
import siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import siap.siep.sollecitoesitotrasmissione.dao.SollecitoEsitoTrasmissioneDAO;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoSqlDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.dao.VerbaleDAO;
import siap.siep.verbale.model.VerbaleModel;
import siap.sige.misurasicurezza.dao.MisuraSicurezzaSenSigeDAO;
import siap.sige.misurasicurezza.model.MisuraSicurezzaSigeModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.rifasiep.dao.RiferimentoFascicoloSiepSqlDAO;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: MisuraSicurezzaController
 * </p>
 * <p>
 * Description: Classe Controller per MisuraSicurezza
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
public class MisuraSicurezzaController extends SiapController implements IMisuraSicurezza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public MisuraSicurezzaModel ExInserisciMisuraSicurezza(MisuraSicurezzaModel aMisuraSicurezza)
			throws F3BException {

		Connection lConn = null;

		MisuraSicurezzaDAO lMisDao = null;
		MisuraSicurezzaSqlDAO lMisSqlDao = null;
		MisuraSicurezzaModel lMisMod = null;
		try {
			lConn = getDBConnection();

			lMisMod = new MisuraSicurezzaModel(aMisuraSicurezza);
			lMisDao = new MisuraSicurezzaDAO(lConn);

			// Gestione del Reg38
			lMisSqlDao = new MisuraSicurezzaSqlDAO(lConn);

			BigDecimal lProgr = lMisSqlDao.getMaxNumReg38(lMisMod.getAnnoReg38(),
					lMisMod.getCodUfficioInserimento());
			lMisMod.setNumReg38(new BigDecimal(lProgr.intValue() + 1));

			lMisDao.setDAOFromModel(lMisMod);
			BigDecimal lKey = null;
			lKey = lMisDao.insert();
			lMisMod.setIdMisuraSicurezza(lKey);

			// Misura Sicurezza SIGE
			if (aMisuraSicurezza instanceof MisuraSicurezzaSigeModel) {
				// Inserimento record di relazione in MISURA_SICUREZZA_SENTENZA_SIGE
				MisuraSicurezzaSenSigeDAO lMisSicSigeDAO = new MisuraSicurezzaSenSigeDAO(lConn);
				lMisSicSigeDAO.setIdMisuraSicurezza(lKey);
				lMisSicSigeDAO
						.setFasSigeSenId(((MisuraSicurezzaSigeModel) aMisuraSicurezza).getFasSigeSenId());
				lMisSicSigeDAO.insert();
				lMisSicSigeDAO.stop();
				cleanup(lMisSicSigeDAO);
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"MisuraSicurezzaController.ExInserisciMisuraSicurezza: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lMisSqlDao);

			cleanup(lConn);
		}

		return lMisMod;
	}

	public Vector ExRicercaMisuraSicurezza(MisuraSicurezzaModel aMisuraSicurezza) throws F3BException {

		Connection lConn = null;

		Vector lMisuraSicurezza = new Vector();
		MisuraSicurezzaSqlDAO lMisDao = null;
		EventoSqlDAO lEventoDao = null;

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDao.ricercaMisuraSicurezza(aMisuraSicurezza);
			lMisDao.start();
			MisuraSicurezzaModel misSicMod = null;
			EventoModel eventoModel = null;
			lEventoDao = new EventoSqlDAO(lConn);

			while (lMisDao.next()) {
				misSicMod = (MisuraSicurezzaModel) lMisDao.getModel();
				if (misSicMod.getEveIdEvento() != null) {
					lEventoDao.ricercaEventoByKey(misSicMod.getEveIdEvento());
					eventoModel = (EventoModel) lEventoDao.getModelByKey();
					misSicMod.setEvento(eventoModel);
				}

				lMisuraSicurezza.add(misSicMod);

			}

			lMisDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezza: Non posso leggere : " + daoEx);
		} catch (Exception sqe) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezza: Non posso leggere  -> " + sqe);
		} finally {
			cleanup(lMisDao);
			cleanup(lEventoDao);

			cleanup(lConn);
		}
		return lMisuraSicurezza;
	}

	public Vector ExRicercaMisuraSicurezzaEstesa(MisuraSicurezzaModel aMisuraSicurezza) throws F3BException {

		Connection lConn = null;

		Vector lMisuraSicurezzi = new Vector();
		MisuraSicurezzaSqlDAO lMisDao = null;

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDao.ricercaMisuraSicurezzaEstesa(aMisuraSicurezza);
			lMisuraSicurezzi = new Vector(lMisDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaEstesa: Non posso leggere : " + daoEx);
		} catch (Exception sqe) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaEstesa: Non posso leggere  -> " + sqe);
		} finally {
			cleanup(lMisDao);

			cleanup(lConn);
		}
		return lMisuraSicurezzi;
	}

	public List ExRicercaFascicoliMisuraSicurezza(String aCodUfficioUtenteConnesso, int aPage)
			throws F3BException {

		Connection lConn = null;

		MisuraSicurezzaSqlDAO lMisFasDao = null;
		MisuraSicurezzaSqlDAO lMisDao = null;

		List lFascicoliMisure = new ArrayList();

		try {
			lConn = getDBConnection();

			lMisFasDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisFasDao.ricercaFascicoliMisuraSicurezza(aCodUfficioUtenteConnesso);
			lMisFasDao.startPage(aPage);

			lMisDao = new MisuraSicurezzaSqlDAO(lConn);

			while (lMisFasDao.next()) {
				MisuraSicurezzaModel lMisMod = (MisuraSicurezzaModel) lMisFasDao.getModelMisuraFascicoli();

				if (lMisMod != null && lMisMod.getIdFascicoloSiep() != null) {
					MisuraSicurezzaFascicoloModel lMisFasNuovoMod = new MisuraSicurezzaFascicoloModel();

					FascicoloSiepModel lFaMod = new FascicoloSiepModel();
					lFaMod.setIdFascicoloSiep(lMisMod.getIdFascicoloSiep());
					lFaMod.setChiaveAnno(lMisMod.getChiaveAnno());
					lFaMod.setChiaveProgr(lMisMod.getChiaveProgr());
					lFaMod.setFlagValidato(lMisMod.getFlagValidato());

					lMisFasNuovoMod.setFascicoloSiep(lFaMod);

					lMisDao.ricercaMisuraSicurezzaByIdFascicolo(lMisMod.getIdFascicoloSiep());
					List lListMisure = (List) lMisDao.getModels();

					lMisFasNuovoMod.setMisureSicurezza(lListMisure);

					lFascicoliMisure.add(lMisFasNuovoMod);
				}
			}
			lMisFasDao.stop();

			if (lFascicoliMisure.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("MisuraSicurezzaController.ExRicercaFascicoliMisuraSicurezza: " + daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lMisFasDao);

			cleanup(lConn);
		}

		return lFascicoliMisure;
	}

	public BigDecimal ExGetCountFascicoliMisuraSicurezza(String aCodUfficioUtenteConnesso)
			throws F3BException {

		BigDecimal lCount = new BigDecimal(0);

		Connection lConn = null;

		MisuraSicurezzaSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();

			lSqlDao = new MisuraSicurezzaSqlDAO(lConn);
			lSqlDao.getCountFascicoliMisuraSicurezza(aCodUfficioUtenteConnesso);

			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"MisuraSicurezzaController.ExGetCountFascicoliMisuraSicurezza: " + daoEx);
		} finally {
			cleanup(lSqlDao);

			cleanup(lConn);
		}

		return lCount;
	}

	public MisuraSicurezzaModel ExRicercaMisuraSicurezzaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MisuraSicurezzaSqlDAO lMisDao = null;
		MisuraSicurezzaModel lMisMod;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDao.ricercaMisuraSicurezzaByKey(aKey);
			lMisMod = (MisuraSicurezzaModel) lMisDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisMod;
	}

	// 06-03-2015 - Ritorna un Vector e non piu' il Model
	// public MisuraSicurezzaModel ExRicercaMisuraSicurezzaByEventoKey(BigDecimal aKey) throws F3BException
	public Vector ExRicercaMisuraSicurezzaByEventoKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MisuraSicurezzaSqlDAO lMisDao = null;
		// MisuraSicurezzaModel lMisMod;
		Vector MisSicVec = new Vector();

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDao.ricercaMisuraSicurezzaByEventoKey(aKey);
			// lMisMod = (MisuraSicurezzaModel) lMisDao.getModelByKey();
			MisSicVec = new Vector(lMisDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaByEventoKey: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return MisSicVec;
	}
	// END AMBROS

	public MisuraSicurezzaModel ExModificaMisuraSicurezza(MisuraSicurezzaModel aMisuraSicurezza)
			throws F3BException {

		Connection lConn = null;
		MisuraSicurezzaDAO lMisDao = null;
		MisuraSicurezzaModel lMisMod = new MisuraSicurezzaModel(aMisuraSicurezza);

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraSicurezzaDAO(lConn);
			lMisDao.setDAOFromModelForUpdate(aMisuraSicurezza);
			lMisDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("MisuraSicurezzaController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisMod;
	}

	public void ExCancellaMisuraSicurezza(MisuraSicurezzaModel aMisuraSicurezza) throws F3BException {

		Connection lConn = null;
		MisuraSicurezzaDAO lMisDao = null;

		try {
			lConn = getDBConnection();

			// Misura di Sicurezza SIGE
			if (aMisuraSicurezza instanceof MisuraSicurezzaSigeModel) {
				// Cancellazione record di relazione in MISURA_SICUREZZA_SENTENZA_SIGE
				MisuraSicurezzaSenSigeDAO lMisSicSigeDAO = new MisuraSicurezzaSenSigeDAO(lConn);
				lMisSicSigeDAO.setCondizioneDeleteMisuraSicurezza(aMisuraSicurezza.getIdMisuraSicurezza());
				lMisSicSigeDAO.delete();
				lMisSicSigeDAO.stop();
				cleanup(lMisSicSigeDAO);
			}

			lMisDao = new MisuraSicurezzaDAO(lConn);
			lMisDao.setCondizioneUpdate(aMisuraSicurezza.getIdMisuraSicurezza());
			lMisDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExCancellaMisuraSicurezza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
	}

	public List ExRicercaMisuraSicurezzaByIdFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		MisuraSicurezzaSqlDAO lMisDao = null;
		List lMisure = new ArrayList();

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraSicurezzaSqlDAO(lConn);

			lMisDao.ricercaMisuraSicurezzaByIdFascicolo(aKey);

			lMisure = new Vector(lMisDao.getModels());

			if (lMisure.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Procedimento Privo di Misure di Sicurezza");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisure;
	}

	// 10-11-2015 - Ricerca che NON Rialncia l'eccezione
	public List ExRicercaMisuraSicurezzaByIdFascicoloNONRilancia(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		MisuraSicurezzaSqlDAO lMisDao = null;
		List lMisure = new ArrayList();

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDao.ricercaMisuraSicurezzaByIdFascicolo(aKey);
			lMisure = new Vector(lMisDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisure;
	}

	public String ExInserisciMisuraSicurezzaWithoutSequence(ArrayList aMisure, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		MisuraSicurezzaDAO lMisDao = null;
		MisuraSicurezzaModel lMisMod = null;
		try {

			lMisDao = new MisuraSicurezzaDAO(lConn);

			if (aMisure != null && aMisure.size() > 0) {
				for (int i = 0; i < aMisure.size(); i++) {
					lMisMod = (MisuraSicurezzaModel) aMisure.get(i);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Misura Sicurezza da inserire = " + lMisMod);
					if (lMisMod != null && lMisMod.getIdMisuraSicurezza() != null) {
						lMisDao.setDAOFromModel(lMisMod);
						lMisDao.setWithoutSequence(true);
						lMisDao.insert();
						lMisDao.stop();
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire le Misure Sicurezza! ");
			}
		} finally {
			cleanup(lMisDao);
		}
		return lCodEsito;
	}

	/**
	 *
	 * @param aFascMStoFascSIEP
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	public String ExInserisciFascMStoFascSIEPWithoutSequence(ArrayList aFascMStoFascSIEP, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		FascMsToFascSiepDAO lFascMSDao = null;

		FascMsToFascSiepModel lFascMSModel = null;

		try {

			lFascMSDao = new FascMsToFascSiepDAO(lConn);

			if (aFascMStoFascSIEP != null && aFascMStoFascSIEP.size() > 0) {
				for (int i = 0; i < aFascMStoFascSIEP.size(); i++) {
					lFascMSModel = (FascMsToFascSiepModel) aFascMStoFascSIEP.get(i);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("FascMAto da inserire = " + lFascMSModel);

					if (lFascMSModel != null && lFascMSModel.getIdFascMsToFascSiep() != null) {
						lFascMSDao.setDAOFromModel(lFascMSModel);
						lFascMSDao.setWithoutSequence(true);
						lFascMSDao.insert();
						lFascMSDao.stop();
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire Collegamenti Fascicoli Misure di Sicurezza! ");
			}
		} finally {
			cleanup(lFascMSDao);
		}
		return lCodEsito;
	}

	/**
	 * Effettua l'upload del blob e la validazione del provvedimento di trasmissione per competenza misure di
	 * sicurezza
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaTrasmissioneCompetenza(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		EventoSqlDAO lEveSqlDao = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data emissione provvedimento (decreto)
			// ========================================================================
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			if ("5404".equals(lEveModel.getCodMotivo())) {
				lStatoProcMod.setCodStatoProcedimento("0437"); // x Competenza
			} else if ("5416".equals(lEveModel.getCodMotivo())) {
				lStatoProcMod.setCodStatoProcedimento("0450"); // x Ai fini dell'esecuzione
			}
			// lStatoProcMod.setCodStatoProcedimento ("0437"); // Da stabilire new
			lStatoProcMod.setData(lEveModel.getDataEmissione());

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoProcDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoProcDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoProcDao.delete();

			// - Inserisce
			lStatoProcDao.setDAOFromModel(lStatoProcMod);
			lStatoProcDao.insert();
			lStatoProcDao.stop();

			// ========================================================================
			// Aggiorno lo NOME_PROCEDIMENTO [eventualmente]
			// ========================================================================

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"MisuraSicurezzaController.ExUpdateValidaTrasmissioneCompetenza : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraSicurezzaController.ExUpdateValidaTrasmissioneCompetenza : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lStatoProcDao);
			cleanup(lEveDaoBlob);

			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * @param aFascMsToFascSiepModel
	 * @return
	 * @throws F3BException
	 */
	public Vector<FascMsToFascSiepModel> ExRicercaFascMsToFascSiepByFascSiep(
			FascMsToFascSiepModel aFascMsToFascSiepModel) throws F3BException {

		Connection lConn = null;

		FascMsToFascSiepSqlDAO lFasMsToFascSiepSqlDao = null;

		Vector<FascMsToFascSiepModel> lLista = new Vector<>();

		try {
			lConn = getDBConnection();

			lFasMsToFascSiepSqlDao = new FascMsToFascSiepSqlDAO(lConn);
			lFasMsToFascSiepSqlDao.ricercaFascMsToFascSiep(aFascMsToFascSiepModel);

			lLista = new Vector<FascMsToFascSiepModel>(lFasMsToFascSiepSqlDao.getModels());

			lFasMsToFascSiepSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("MisuraSicurezzaController.ExRicercaFascMsToFascSiepByFascSiep: ", daoEx);
			throw new F3BException("MisuraSicurezzaController.ExRicercaFascMsToFascSiepByFascSiep: " + daoEx);
		} finally {
			cleanup(lFasMsToFascSiepSqlDao);

			cleanup(lConn);
		}

		return lLista;
	}

	/**
	 * @param aIdFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public Vector<FascMsToFascSiepModel> ExRicercaFascicoliCollegati(BigDecimal aIdFascicoloSiep)
			throws F3BException {

		Connection lConn = null;

		FascMsToFascSiepSqlDAO lFasMsToFascSiepSqlDao = null;

		Vector<FascMsToFascSiepModel> lLista = new Vector<>();

		try {
			lConn = getDBConnection();

			lFasMsToFascSiepSqlDao = new FascMsToFascSiepSqlDAO(lConn);
			lFasMsToFascSiepSqlDao.ricercaCollegamentiSiep(aIdFascicoloSiep);

			lLista = new Vector<FascMsToFascSiepModel>(lFasMsToFascSiepSqlDao.getModels());

			lFasMsToFascSiepSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("MisuraSicurezzaController.ExRicercaFascicoliCollegati: ", daoEx);
			throw new F3BException("MisuraSicurezzaController.ExRicercaFascicoliCollegati: " + daoEx);
		} finally {
			cleanup(lFasMsToFascSiepSqlDao);

			cleanup(lConn);
		}

		return lLista;
	}

	/**
	 * Metodo per effettuare la ricerca dei Messaggi di trasmissione per competenza delle Misure di sicurezza
	 *
	 * @param aDeliveryMode
	 *            Indica se ricercare i messaggi inviati o quelli ricevuti
	 * @param aCodTipoMessaggio
	 * @param aCodTipoOperazione
	 * @param aCodEsito
	 * @param aFlagVisto
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @param aCodUfficioMitt
	 * @param aCodUfficioDest
	 * @param aDataTrasmissioneDal
	 * @param aDataTrasmissioneAl
	 * @return
	 * @throws Exception
	 */
	public BigDecimal ExCountRicercaMessaggi(String aDeliveryMode, String aCodTipoMessaggio
	// , String aCodTipoOperazione
			, Vector<String> aListaTipoOperazione
			// , String aCodEsito
			, Vector<String> aListaEsiti, String aFlagVisto, BigDecimal aChiaveAnnoSiep,
			BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep, String aCodUfficioMitt,
			String aCodUfficioDest, Date aDataTrasmissioneDal, Date aDataTrasmissioneAl) throws Exception {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		MessaggioSqlDAO lMesSqlDao = null;

		try {
			lConn = getDBConnection();

			lMesSqlDao = new MessaggioSqlDAO(lConn);

			lMesSqlDao.getCountRicercaMessaggioRicercaConFiltri(aDeliveryMode, aCodTipoMessaggio
			// , aCodTipoOperazione
					, aListaTipoOperazione
					// , aCodEsito
					, aListaEsiti, aFlagVisto, aChiaveAnnoSiep, aChiaveProgrSiep, aChiaveUfficioSiep,
					aCodUfficioMitt, null // Utente Mitt
					, aCodUfficioDest, aDataTrasmissioneDal, aDataTrasmissioneAl, null, null);

			lMesSqlDao.start();
			lMesSqlDao.next();
			lCount = lMesSqlDao.getBigDecimal("HowManyRecords");
			lMesSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MessaggioController.ExCountRicercaMessaggiRicevuti: " + daoEx);
		} finally {
			cleanup(lMesSqlDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lCount;
	}

	/**
	 * Effettua la ricerca paginata dei messaggi ricevuti (01 - Richiesta) applicando i filtro indicati
	 *
	 * @param aDeliveryMode
	 *            - Indica se ricercare i messaggi inviati o quelli ricevuti
	 * @param aCodTipoOperazione
	 * @param aCodEsito
	 * @param aCodUfficioMitt
	 * @param aFlagVisto
	 * @param aChiaveAnnoSiep
	 * @param aChiaveProgrSiep
	 * @param aChiaveUfficioSiep
	 * @param aCodUfficioMitt
	 * @param aCodUfficioDest
	 * @param aDataTrasmissioneDal
	 *            - DATA_INVIO
	 * @param aDataTrasmissioneAl
	 *            - DATA_INVIO
	 * @param aPage
	 *            (pagina di risultati da estrarre)
	 * @return
	 * @throws F3BException
	 *
	 * @since NOV/2013 x gestione Misure di Sicurezza
	 */
	public Vector<MessaggioModel> ExRicercaMessaggi(String aDeliveryMode, String aCodTipoMessaggio
	// , String aCodTipoOperazione
			, Vector<String> aListaTipoOperazione
			// , String aCodEsito
			, Vector<String> aListaEsiti, String aFlagVisto, BigDecimal aChiaveAnnoSiep,
			BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep, String aCodUfficioMitt,
			String aCodUfficioDest, Date aDataTrasmissioneDal, Date aDataTrasmissioneAl, int aPage)
			throws F3BException {

		Connection lConn = null;

		MessaggioSqlDAO lMessaggioSqlDAO = null;

		Vector<MessaggioModel> lLista = new Vector<>();

		try {
			lConn = getDBConnection();

			lMessaggioSqlDAO = new MessaggioSqlDAO(lConn);

			lMessaggioSqlDAO.ricercaMessaggioRicercaConFiltri(aDeliveryMode, aCodTipoMessaggio,
					aListaTipoOperazione
					// , aCodEsito
					, aListaEsiti, aFlagVisto, aChiaveAnnoSiep, aChiaveProgrSiep, aChiaveUfficioSiep,
					aCodUfficioMitt, null // Utente Mitt
					, aCodUfficioDest // Ufficio Dest
					, aDataTrasmissioneDal, aDataTrasmissioneAl, null, null, null, null // aChiaveAnnoFasCumulante
					, null // aChiaveProgrFasCumulante
					, null // aChiaveUfficioFasCumulante
					, aPage);

			lLista = new Vector<MessaggioModel>(lMessaggioSqlDAO.getModels());

			lMessaggioSqlDAO.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("MisuraSicurezzaController.ExRicercaMessaggiRicevuti: ", daoEx);
			throw new F3BException("MisuraSicurezzaController.ExRicercaMessaggiRicevuti: " + daoEx);
		} finally {
			cleanup(lMessaggioSqlDAO);

			cleanup(lConn);
		}

		return lLista;
	}

	public Vector<MessaggioModel> ExRicercaSollecitiByIdRich(String aDeliveryMode, String aCodTipoMessaggio,
			String aCodTipoOperazione, BigDecimal aIdMessRichiesta, String aFlagVisto, String aCodUfficioDest)
			throws F3BException {

		Connection lConn = null;

		MessaggioSqlDAO lMessaggioSqlDAO = null;

		Vector<MessaggioModel> lLista = new Vector<>();

		Vector<String> lListaTipoOperazione = new Vector<>();
		lListaTipoOperazione.add(aCodTipoOperazione);

		try {
			lConn = getDBConnection();

			lMessaggioSqlDAO = new MessaggioSqlDAO(lConn);

			lMessaggioSqlDAO.ricercaMessaggioRicercaConFiltri(aDeliveryMode, aCodTipoMessaggio,
					lListaTipoOperazione, null, aFlagVisto, null, null, null, null, null // Utente Mitt
					, aCodUfficioDest // Ufficio Dest
					, null, null, null, null, aIdMessRichiesta, null // aChiaveAnnoFasCumulante
					, null // aChiaveProgrFasCumulante
					, null // aChiaveUfficioFasCumulante
					, 0);

			lLista = new Vector<MessaggioModel>(lMessaggioSqlDAO.getModels());

			lMessaggioSqlDAO.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("MisuraSicurezzaController.ExRicercaSollecitiByIdRich: ", daoEx);
			throw new F3BException("MisuraSicurezzaController.ExRicercaSollecitiByIdRich: " + daoEx);
		} finally {
			cleanup(lMessaggioSqlDAO);

			cleanup(lConn);
		}

		return lLista;
	}

	/**
	 * Effettua l'inserimento di un fascicolo di Esecuzione delle Misure di Sicurezza in classe IV ribaltando
	 * i dato del fascicolo su cui sono iscritte le misure, di propria competenza o preso in carico.
	 *
	 * Dati Inseriri Soggetto - sempre duplicato Sentenza - NO se stesso Ufficio, Duplicata se di altro
	 * ufficio SENTENZE_RIUNITE ALTRI_GRADI_GIUDIZIO Residena - Domicilio - Avvocati - Pena Complessiva - NO
	 * Misure di Sicurezza in sentenza - Reati e Circostanze - NO
	 *
	 * Eventuali provvedimenti in copia da definire
	 *
	 * @param aFascicolo
	 *            - fascicolo da inserire
	 * @param aDettaglioFasOrig
	 *            - DatteglioFascicolo con dati da ribaltare
	 *
	 * @author d.fiorletta
	 * @since 12/2013
	 */
	public void ExInserisciFascicoloClasseIV(FascicoloSiepModel aFascicoloSiep,
			DettaglioFascicoloModel aDettaglioFasOrig, BigDecimal aIdMessaggio, Date aDataCumulo)
			throws F3BException {

		siesLogger.debug("Inizio inserimento fascicolo di classe IV... ");

		Connection lConn = null;

		SoggettoDAO lSoggDao = null;
		FascicoloSiepSqlDAO lFascSiepSqlDao = null;
		FascicoloSiepDAO lFascSiepDao = null;
		MisuraSicurezzaDAO lMisuraSicurezzaDao = null;
		FascMsToFascSiepDAO lFascMsToFascSiepDao = null;
		ResidenzaDAO lResidenzaDao = null;
		ResidenzaFascicoloSiepDAO lResidenzaFascicoloSiepDAO = null;
		AvvocatoFascicoloSiepDAO lAvvFasDao = null;
		// ReatoDAO lReaDao = null;
		// CircostanzaDAO lCirDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		// PenaComplessivaDAO lPenaComplessivaDao = null;
		// SanzioneSostitutivaDAO lSanzioneSostitutivaDAO = null;
		SentenzaSqlDAO lSenSqlDao = null;
		SentenzaDAO lSenDao = null;
		LuogoDetenzioneDAO lLuogoDetDao = null;
		PenaResiduaDAO lPenaResiduaDao = null;
		AltraCausaDAO lAltraCausaDao = null;
		SentenzaRiunitaFascSiepSqlDAO lSentenzaRiunitaFascSiepSqlDao = null;
		SentenzaRiunitaFascSiepDAO lSentenzaRiunitaFascSiepDao = null;
		SentenzaRiunitaDAO lSentenzaRiunitaDao = null;
		AgdgFascicoloSiepSqlDAO lAgdgFascicoloSiepSqlDao = null;
		AgdgFascicoloSiepDAO lAgdgFascicoloSiepDao = null;
		AltriGradiGiudizioDAO lAltriGradiDAO = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		// INIZIO MEV_39 (in fase di inserimento di un procedimento di classe IV deve essere previsto anche
		// l'inserimento nello scadenzario_siep con il nuovo codice_tipo_scadenzario = 20)
		ScadenzarioDAO lScaDao = null;
		// FINE MEV_39

		try {
			lConn = getDBConnection();

			// =====================================
			// Recupero CHIAVE_PROGR da Utilizzare
			// =====================================
			lFascSiepSqlDao = new FascicoloSiepSqlDAO(lConn);
			lFascSiepSqlDao.getProgressivoFascicoloSiep(aFascicoloSiep);
			lFascSiepSqlDao.start();
			int lMaxProgr = 0;
			if (lFascSiepSqlDao.next() && (lFascSiepSqlDao.getInt("aMAX") > 0))
				lMaxProgr = lFascSiepSqlDao.getInt("aMAX");
			lFascSiepSqlDao.stop();
			siesLogger.debug("lMaxProgr = " + lMaxProgr);

			if (aFascicoloSiep.getChiaveProgr() == null) {
				// Assegnazione automatica
				siesLogger.debug("Assegnazione automatica recupero il progressivo... ");

				// lFascSiepSqlDao = new FascicoloSiepSqlDAO(lConn);
				// lFascSiepSqlDao.getProgressivoFascicoloSiep(aFascicoloSiep);
				// lFascSiepSqlDao.start();
				// int lMaxProgr = 0;
				// if (lFascSiepSqlDao.next() && (lFascSiepSqlDao.getInt("aMAX") > 0))
				// lMaxProgr = lFascSiepSqlDao.getInt("aMAX");
				// lFascSiepSqlDao.stop();

				int lTipoProgr = aFascicoloSiep.getTipoProgressivo();
				if (lMaxProgr == 0) {
					aFascicoloSiep.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
				} else {
					aFascicoloSiep.setChiaveProgr(new BigDecimal(lMaxProgr + 1));
				}
				siesLogger.debug("Progressivo di inserimento = " + aFascicoloSiep.getChiaveProgr());
			} else {
				// Assegnazione manuale devo controllare che non esista a sistema un
				// fascicolo con stessa numerazione e che la numerazione assegnata non
				// sia superiore a quella dell'ultimo fascicolo dell'anno corrente per
				// Evitare buchi di numerazione
				siesLogger.debug("Assegnazione manuale: verifica che il progressivo non sia gia' a sistema");

				lFascSiepSqlDao = new FascicoloSiepSqlDAO(lConn);
				lFascSiepSqlDao.ricercaFascicoloByProgrAnnoCodUfficio(aFascicoloSiep);
				lFascSiepSqlDao.start();
				if (lFascSiepSqlDao.next()) {
					siesLogger.error("Il fascicolo " + aFascicoloSiep.getChiaveAnno() + "/"
							+ aFascicoloSiep.getChiaveProgr() + " e' gia' presente a sistema");
					throw new SIEPException(F3BException.USER_MESSAGE,
							"Il fascicolo " + aFascicoloSiep.getChiaveAnno() + "/"
									+ aFascicoloSiep.getChiaveProgr() + " e' gia' presente a sistema");
				}

				if (aFascicoloSiep.getChiaveAnno().intValue() == Integer
						.parseInt(DateUtils.getSysDate("yyyy"))) {
					if (aFascicoloSiep.getChiaveProgr().intValue() > lMaxProgr && lMaxProgr > 0) {
						throw new SIEPException(F3BException.USER_MESSAGE,
								"Impossibile inserire il fascicolo! Il numero del procedimento e' superiore all'ultimo assegnato dal sistema");
					}
				}
			}

			// ========================================================================
			// Duplicazione Soggetto - Sempre in copia, ogni fascicolo ha il SUO soggetto
			// ========================================================================
			siesLogger.debug("Duplicazione Soggetto...");

			SoggettoModel lSoggetto = new SoggettoModel(aDettaglioFasOrig.getFascicoloSiep().getSoggetto());

			lSoggetto.setDataInserimento(aFascicoloSiep.getDataInserimento());
			lSoggetto.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			lSoggetto.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			lSoggetto.setDataAggiornamento(null);
			lSoggetto.setCodOperatoreAggiornamento(null);
			lSoggetto.setCodUfficioAggiornamento(null);
			lSoggetto.setKeySoggNsc(null);
			lSoggetto.setFlagPresenzaFascicolo(null);
			lSoggDao = new SoggettoDAO(lConn);
			lSoggDao.setDAOFromModel(lSoggetto);
			BigDecimal lkeySoggIV = lSoggDao.insert();
			aFascicoloSiep.setSogIdSoggetto(lkeySoggIV);

			// ========================================================================
			// Inserimento Sentenza con eventuale Duplicazione: se di altro ufficio
			// va sempre duplicata. Ogni ufficio gestisce le proprie sentenze.
			// ========================================================================
			siesLogger.debug("Inserimento Sentenza con eventuale Duplicazione");
			SentenzaModel lSentenza = new SentenzaModel(aDettaglioFasOrig.getFascicoloSiep().getSentenza());

			// Per fare il controllo devo verificare che non sia presente la sentenza
			// per l'ufficio corrente
			lSentenza.setDataInserimento(aFascicoloSiep.getDataInserimento());
			lSentenza.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			lSentenza.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			lSentenza.setDataAggiornamento(null);
			lSentenza.setCodOperatoreAggiornamento(null);
			lSentenza.setCodUfficioAggiornamento(null);

			// verifica se per l'ufficio corrente e' gia' presente la sentenza (cod uffinserimento + altri
			// dati)
			// ricercaSentenzaDuplicataCassazione() stesso metodo utilizzato per la
			// funzione di iscrizione sentenza
			lSenSqlDao = new SentenzaSqlDAO(lConn);
			lSenSqlDao.ricercaSentenzaDuplicataCassazione(lSentenza);
			Vector<SentenzaModel> lListaSentenze = new Vector<SentenzaModel>(lSenSqlDao.getModels());
			if (lListaSentenze.size() > 0) {
				siesLogger.debug("Sentenza gia' a sistema la aggancio");
				aFascicoloSiep.setSenIdSentenza(lSentenza.getIdSentenza());
			} else {
				siesLogger.debug("La Sentenza non e' a sistema per l'ufficio corrente la inserisco in copia");

				lSenDao = new SentenzaDAO(lConn);
				lSenDao.setDAOFromModel(lSentenza);
				BigDecimal lKeySentenza = lSenDao.insert();

				aFascicoloSiep.setSenIdSentenza(lKeySentenza);

				siesLogger.debug("lKeySentenza = " + lKeySentenza);
				// In questo caso devo duplicare anche ALTRI_GRADI_GIUDIZIO e SENTENZA_RIUNITA
				// che sono attributi della sentenza, ma solo se legati al fascicolo di
				// origine.
				// Non lo posso fare qua, ma solo dopo l'inserimento del fascicolo quando
				// inserisco AGDG_FASCICOLO_SIEP e SENTENZARIUNITA_FASC_SIEP
			}

			// ================================
			// Inserimento Fascicolo SIEP
			// ================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserimento Fascicolo di classe IV...");
			lFascSiepDao = new FascicoloSiepDAO(lConn);
			lFascSiepDao.setDAOFromModel(aFascicoloSiep);
			BigDecimal lkeyFasIV = lFascSiepDao.insert();
			aFascicoloSiep.setIdFascicoloSiep(lkeyFasIV);

			// ========================================================================
			// SENTENZA_RIUNITA: e' un attributo della SENTENZA e va presa in
			// considerazione solo se legata al fascicolo di origine tramite la tabella
			// SENTENZARIUNITA_FASC_SIEP.
			// n.b. Se ho duplicato la sentenza devo duplicare anche SENTENZA_RIUNITA
			// e aggiungere SENTENZARIUNITA_FASC_SIEP
			// In caso contrario basta aggiungere SENTENZARIUNITA_FASC_SIEP
			// ========================================================================
			// Recupero le sentenze riunite associate al fascicolo di origine
			// n.b. non tutte quelle associate alla sentenza ma solo quelle associate
			// al fascicolo
			BigDecimal idFascicoloOrig = aDettaglioFasOrig.getFascicoloSiep().getIdFascicoloSiep();

			siesLogger.debug("Acquisizione Sentenze Riunite...");
			Vector<SentenzaRiunitaFascSiepModel> lListaSentenzeRiunite = new Vector<>();
			lSentenzaRiunitaFascSiepSqlDao = new SentenzaRiunitaFascSiepSqlDAO(lConn);

			lSentenzaRiunitaFascSiepSqlDao.ricercaSentenzaRiunitaFascSiepByIdFasciolo(idFascicoloOrig);

			lListaSentenzeRiunite = new Vector<SentenzaRiunitaFascSiepModel>(
					lSentenzaRiunitaFascSiepSqlDao.getModels());

			siesLogger.debug("Sentenze Riunite trovate: " + lListaSentenzeRiunite.size());

			Iterator lSentRiunIter = lListaSentenzeRiunite.iterator();
			while (lSentRiunIter.hasNext()) {
				SentenzaRiunitaFascSiepModel lSenRiuFasSiepModel = null;
				lSenRiuFasSiepModel = (SentenzaRiunitaFascSiepModel) lSentRiunIter.next();

				SentenzaRiunitaModel lSentRiunModel = lSenRiuFasSiepModel.getSentenzaRiunitaModel();
				// Il fascicolo origine punta una sentenza riunita, la faccio puntare anche
				// dal fascicolo di classe IV
				// Se aFascicoloSiep.setSenIdSentenza =
				if (aFascicoloSiep.getSenIdSentenza().compareTo(lSentRiunModel.getSenIdSentenza()) != 0) {
					siesLogger.debug("La sentenza e' stata duplicata [" + aFascicoloSiep.getSenIdSentenza()
							+ " vs " + lSentRiunModel.getSenIdSentenza() + "]");
					siesLogger.debug("Duplico SENTENZA_RIUNITA...");
					// Il nuovo fascicolo punta una sentenza diversa da quella del fascicolo di
					// origine. Ho quindi duplicato la sentenza e devo duplicare anche
					// SENTENZA_RIUNITA agganciandola alla nuova sentenza
					lSentRiunModel.setSenIdSentenza(aFascicoloSiep.getSenIdSentenza());
					lSentRiunModel.setDataInserimento(aFascicoloSiep.getDataInserimento());
					lSentRiunModel.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lSentRiunModel.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lSentRiunModel.setDataAggiornamento(null);
					lSentRiunModel.setCodOperatoreAggiornamento(null);
					lSentRiunModel.setCodUfficioAggiornamento(null);
					lSentenzaRiunitaDao = new SentenzaRiunitaDAO(lConn);
					lSentenzaRiunitaDao.setDAOFromModel(lSentRiunModel);
					BigDecimal lIdSentenzaRiunita = lSentenzaRiunitaDao.insert();
					lSentenzaRiunitaDao.stop();
					siesLogger.debug(" lIdSentenzaRiunita = " + lIdSentenzaRiunita);
					siesLogger.debug(" Inserisco SENTENZARIUNITA_FASC_SIEP...");
					// Inserisco su SENTENZARIUNITA_FASC_SIEP
					lSenRiuFasSiepModel = new SentenzaRiunitaFascSiepModel();
					lSenRiuFasSiepModel.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lSenRiuFasSiepModel.setSenRiuIdSentenzaRiunita(lIdSentenzaRiunita);

					lSentenzaRiunitaFascSiepDao = new SentenzaRiunitaFascSiepDAO(lConn);
					lSentenzaRiunitaFascSiepDao.setDAOFromModel(lSenRiuFasSiepModel);
					BigDecimal lId = lSentenzaRiunitaFascSiepDao.insert();
					siesLogger.debug("lId = " + lId);
					lSentenzaRiunitaFascSiepDao.stop();
				} else {
					// Il nuovo fascicolo punta la stessa sentenza del fascicolo di origine.
					// SENTENZA_RIUNITA dovrebbe gia' essere a sistema ma devo registrare
					// SENTENZARIUNITA_FASC_SIEP per collegare la sentenza riunita al nuovo
					// fascicolo.
					// Inserisco su SENTENZARIUNITA_FASC_SIEP
					siesLogger.debug("Sentenza non duplicata, scrivo solo SENTENZARIUNITA_FASC_SIEP");
					lSenRiuFasSiepModel = new SentenzaRiunitaFascSiepModel();
					lSenRiuFasSiepModel.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lSenRiuFasSiepModel.setSenRiuIdSentenzaRiunita(lSentRiunModel.getIdSentenzaRiunita());

					lSentenzaRiunitaFascSiepDao = new SentenzaRiunitaFascSiepDAO(lConn);
					lSentenzaRiunitaFascSiepDao.setDAOFromModel(lSenRiuFasSiepModel);
					BigDecimal lId = lSentenzaRiunitaFascSiepDao.insert();
					siesLogger.debug("lId = " + lId);
					lSentenzaRiunitaFascSiepDao.stop();
				}
			}

			// ========================================================================
			// Altri gradi di Giudizio
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Acquisizione Altri Gradi Di Giudizio...");
			lAgdgFascicoloSiepSqlDao = new AgdgFascicoloSiepSqlDAO(lConn);
			lAgdgFascicoloSiepSqlDao.ricercaAgdgFascicoloSiepByIdFasSiep(idFascicoloOrig);
			Vector lAltriGradiGiudizio = new Vector(lAgdgFascicoloSiepSqlDao.getModels());

			siesLogger.debug("Altri Gradi Di Giudizio trovati: " + lAltriGradiGiudizio.size());
			Iterator lIterAGDG = lAltriGradiGiudizio.iterator();

			while (lIterAGDG.hasNext()) {
				AgdgFascicoloSiepModel lAGDGFasSiepModel = (AgdgFascicoloSiepModel) lIterAGDG.next();

				AltriGradiGiudizioModel lAltriGradiModel = lAGDGFasSiepModel.getAltriGradiGiudizioModel();

				if (aFascicoloSiep.getSenIdSentenza().compareTo(lAltriGradiModel.getSenIdSentenza()) != 0) {
					siesLogger.debug("La sentenza e' stata duplicata [" + aFascicoloSiep.getSenIdSentenza()
							+ " vs " + lAltriGradiModel.getSenIdSentenza() + "]");
					siesLogger.debug("Duplico ALTRI_GRADI_GIUDIZIO...");

					// Duplico ALTRI_GRADI_GIUDIZIO
					lAltriGradiModel.setSenIdSentenza(aFascicoloSiep.getSenIdSentenza());
					lAltriGradiModel.setDataInserimento(aFascicoloSiep.getDataInserimento());
					lAltriGradiModel.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lAltriGradiModel.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lAltriGradiModel.setDataAggiornamento(null);
					lAltriGradiModel.setCodOperatoreAggiornamento(null);
					lAltriGradiModel.setCodUfficioAggiornamento(null);
					lAltriGradiDAO = new AltriGradiGiudizioDAO(lConn);
					lAltriGradiDAO.setDAOFromModel(lAltriGradiModel);
					BigDecimal lIdAGDG = lAltriGradiDAO.insert();
					lAltriGradiDAO.stop();

					siesLogger.debug(" lIdAGDG = " + lIdAGDG);
					siesLogger.debug(" Inserisco AGDG_FASCICOLO_SIEP...");

					// Inserisco AGDG_FASCICOLO_SIEP
					lAGDGFasSiepModel = new AgdgFascicoloSiepModel();
					lAGDGFasSiepModel.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lAGDGFasSiepModel.setAgdgIdAltrigradigiudizio(lIdAGDG);
					lAgdgFascicoloSiepDao = new AgdgFascicoloSiepDAO(lConn);
					lAgdgFascicoloSiepDao.setDAOFromModel(lAGDGFasSiepModel);
					BigDecimal lId = lAgdgFascicoloSiepDao.insert();
					lAgdgFascicoloSiepDao.stop();
					siesLogger.debug("lId = " + lId);
				} else {
					// Inserisco AGDG_FASCICOLO_SIEP
					siesLogger.debug("Sentenza non duplicata, scrivo solo AGDG_FASCICOLO_SIEP");
					lAGDGFasSiepModel = new AgdgFascicoloSiepModel();
					lAGDGFasSiepModel.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lAGDGFasSiepModel.setAgdgIdAltrigradigiudizio(lAltriGradiModel.getIdAltrigradigiudizio());

					lAgdgFascicoloSiepDao = new AgdgFascicoloSiepDAO(lConn);
					lAgdgFascicoloSiepDao.setDAOFromModel(lAGDGFasSiepModel);
					BigDecimal lId = lAgdgFascicoloSiepDao.insert();
					lAgdgFascicoloSiepDao.stop();
					siesLogger.debug("lId = " + lId);
				}
			}

			// ================================
			// Duplicazione Residenza
			// ================================
			if (aDettaglioFasOrig.getResidenza() != null) {
				lResidenzaDao = new ResidenzaDAO(lConn);
				ResidenzaModel lResMod = new ResidenzaModel(aDettaglioFasOrig.getResidenza());

				lResMod.setSogIdSoggetto(aFascicoloSiep.getSogIdSoggetto());

				lResMod.setDataInserimento(aFascicoloSiep.getDataInserimento());
				lResMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
				lResMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
				lResMod.setDataAggiornamento(null);
				lResMod.setCodOperatoreAggiornamento(null);
				lResMod.setCodUfficioAggiornamento(null);
				lResidenzaDao.setDAOFromModel(lResMod);
				BigDecimal lkeyResIV = lResidenzaDao.insert();
				lResidenzaDao.stop();

				// collego la residenza al nuovo fascicolo
				lResidenzaFascicoloSiepDAO = new ResidenzaFascicoloSiepDAO(lConn);
				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lResFasMod.setResIdResidenza(lkeyResIV);
				lResFasMod.setDataInizioValidita(aFascicoloSiep.getDataInserimento()); // FIXME da verificare
				lResFasMod.setDataFineValidita(null);
				lResidenzaFascicoloSiepDAO.setDAOFromModel(lResFasMod);
				lResidenzaFascicoloSiepDAO.insert();
				lResidenzaFascicoloSiepDAO.stop();
			}

			// ================================
			// Duplicazione Domicilio
			// ================================
			if (aDettaglioFasOrig.getDomicilio() != null) {
				lResidenzaDao = new ResidenzaDAO(lConn);
				ResidenzaModel lResMod = new ResidenzaModel(aDettaglioFasOrig.getDomicilio());

				lResMod.setSogIdSoggetto(aFascicoloSiep.getSogIdSoggetto());

				lResMod.setDataInserimento(aFascicoloSiep.getDataInserimento());
				lResMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
				lResMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
				lResMod.setDataAggiornamento(null);
				lResMod.setCodOperatoreAggiornamento(null);
				lResMod.setCodUfficioAggiornamento(null);

				lResidenzaDao.setDAOFromModel(lResMod);
				BigDecimal lkeyResIV = lResidenzaDao.insert();
				lResidenzaDao.stop();

				// collego la residenza al nuovo fascicolo
				lResidenzaFascicoloSiepDAO = new ResidenzaFascicoloSiepDAO(lConn);

				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lResFasMod.setResIdResidenza(lkeyResIV);
				lResFasMod.setDataInizioValidita(aFascicoloSiep.getDataInserimento()); // FIXME da verificare
				lResFasMod.setDataFineValidita(null);

				lResidenzaFascicoloSiepDAO.setDAOFromModel(lResFasMod);
				lResidenzaFascicoloSiepDAO.insert();
				lResidenzaFascicoloSiepDAO.stop();
			}

			// ================================
			// Duplicazione Avvocati
			// ================================
			if (aDettaglioFasOrig.getAvvocatiSIEP() != null) {
				siesLogger.debug("Duplicazione Avvocati...");
				List lLisAvv = aDettaglioFasOrig.getAvvocatiSIEP();
				lAvvFasDao = new AvvocatoFascicoloSiepDAO(lConn);
				for (int i = 0; i < lLisAvv.size(); i++) {
					AvvocatoFascicoloSiepModel lAvvFasMod = (AvvocatoFascicoloSiepModel) lLisAvv.get(i);
					lAvvFasMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					// lAvvFasMod.setDataInizioValidita (aValore); lascio quella del fascicolo di provenienza
					lAvvFasMod.setAvvIdAvvocatoFascicoloSost(null);
					lAvvFasMod.setEveIdEvento(null);
					lAvvFasMod.setDataInserimento(aFascicoloSiep.getDataInserimento());
					lAvvFasMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lAvvFasMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lAvvFasMod.setDataAggiornamento(null);
					lAvvFasMod.setCodOperatoreAggiornamento(null);
					lAvvFasMod.setCodUfficioAggiornamento(null);
					lAvvFasDao.setDAOFromModel(lAvvFasMod);
					lAvvFasDao.insert();
				}
			}

			// ================================
			// Duplicazione Pena Complessiva/Sanzione sostitutiva
			// NO. Su indicazione di MT
			// ================================
			// if (aDettaglioFasOrig.getPenaComplessivaSanzioneSostitutiva() != null) {
			// PenaComplessivaModel lPenaComplessivaModel =
			// aDettaglioFasOrig.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva();
			// SanzioneSostitutivaModel lSanzioneSostModel =
			// aDettaglioFasOrig.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva();
			//
			// if (lPenaComplessivaModel!=null && lPenaComplessivaModel.getIdPenaComplessiva()!=null) {
			// lPenaComplessivaModel.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			//
			// lPenaComplessivaModel.setDataInserimento (aFascicoloSiep.getDataInserimento());
			// lPenaComplessivaModel.setCodOperatoreInserimento (aFascicoloSiep.getCodOperatoreInserimento());
			// lPenaComplessivaModel.setCodUfficioInserimento (aFascicoloSiep.getCodUfficioInserimento());
			// lPenaComplessivaModel.setDataAggiornamento (null);
			// lPenaComplessivaModel.setCodOperatoreAggiornamento (null);
			// lPenaComplessivaModel.setCodUfficioAggiornamento (null);
			//
			// lPenaComplessivaDao = new PenaComplessivaDAO(lConn);
			// lPenaComplessivaDao.setDAOFromModel(lPenaComplessivaModel);
			// BigDecimal idPc = lPenaComplessivaDao.insert();
			// lPenaComplessivaDao.stop();
			//
			// if (lSanzioneSostModel!=null && lSanzioneSostModel.getIdSanzioneSostitutiva()!=null) {
			// lSanzioneSostModel.setPenComIdPenaComplessiva(idPc);
			// lSanzioneSostModel.setDataInserimento (aFascicoloSiep.getDataInserimento());
			// lSanzioneSostModel.setCodOperatoreInserimento (aFascicoloSiep.getCodOperatoreInserimento());
			// lSanzioneSostModel.setCodUfficioInserimento (aFascicoloSiep.getCodUfficioInserimento());
			// lSanzioneSostModel.setDataAggiornamento (null);
			// lSanzioneSostModel.setCodOperatoreAggiornamento (null);
			// lSanzioneSostModel.setCodUfficioAggiornamento (null);
			//
			// lSanzioneSostitutivaDAO = new SanzioneSostitutivaDAO(lConn);
			// lSanzioneSostitutivaDAO.setDAOFromModel(lSanzioneSostModel);
			// lSanzioneSostitutivaDAO.insert();
			// lSanzioneSostitutivaDAO.stop();
			// }
			// }
			// }

			// ================================
			// Duplicazione Posizione Giuridica
			// ===============================
			PosizioneGiuridicaModel lPosMod = null;
			if (aDettaglioFasOrig.getPosizioneGiuridica() != null) {
				siesLogger.debug("Duplicazione Posizione Giuridica...");
				lPosMod = aDettaglioFasOrig.getPosizioneGiuridica();
				lPosMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lPosMod.setIdEventoRiferimento(null);
				lPosMod.setDataInserimento(aFascicoloSiep.getDataInserimento());
				lPosMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
				lPosMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
				lPosMod.setDataAggiornamento(null);
				lPosMod.setCodOperatoreAggiornamento(null);
				lPosMod.setCodUfficioAggiornamento(null);
				lPosDao = new PosizioneGiuridicaDAO(lConn);
				lPosDao.setDAOFromModel(lPosMod);
				BigDecimal idPos = lPosDao.insert();
				lPosMod.setIdPosizioneGiuridica(idPos);
			}

			// ==============================================
			// Duplicazione LUOGO DETENZIONE
			// ==============================================
			if (aDettaglioFasOrig.getLuogoDetenzione() != null) {
				siesLogger.debug("Duplicazione Luogo di Detenzione...");
				LuogoDetenzioneModel lLuogoDet = aDettaglioFasOrig.getLuogoDetenzione();
				lLuogoDet.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lLuogoDet.setFasSiuIdFascicoloSius(null);
				if (lPosMod != null && lPosMod.getIdPosizioneGiuridica() != null)
					lLuogoDet.setPosGiuIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
				else
					lLuogoDet.setPosGiuIdPosizioneGiuridica(null);

				lLuogoDet.setDataInserimento(aFascicoloSiep.getDataInserimento());
				lLuogoDet.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
				lLuogoDet.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
				lLuogoDet.setDataAggiornamento(null);
				lLuogoDet.setCodOperatoreAggiornamento(null);
				lLuogoDet.setCodUfficioAggiornamento(null);
				lLuogoDetDao = new LuogoDetenzioneDAO(lConn);
				lLuogoDetDao.setDAOFromModel(lLuogoDet);
				lLuogoDetDao.insert();
			}

			// ==============================================
			// Duplicazione ALTRA_CAUSA
			// ==============================================
			if ("S".equalsIgnoreCase(aFascicoloSiep.getFlagAltraCausa())) {
				siesLogger.debug("Duplicazione AltraCausa...");
				AltraCausaModel lAltraCausaModel = aDettaglioFasOrig.getAltraCausa();
				if (lAltraCausaModel != null) {

					lAltraCausaDao = new AltraCausaDAO(lConn);
					lAltraCausaModel.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lAltraCausaModel.setDataInserimento(aFascicoloSiep.getDataInserimento());
					lAltraCausaModel.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lAltraCausaModel.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lAltraCausaModel.setDataAggiornamento(null);
					lAltraCausaModel.setCodOperatoreAggiornamento(null);
					lAltraCausaModel.setCodUfficioAggiornamento(null);
					lAltraCausaDao.setDAOFromModel(lAltraCausaModel);
					lAltraCausaDao.insert();
				}
			}

			// ==============================================
			// Duplicazione Pena Residua
			// ==============================================
			if (aDettaglioFasOrig.getPenaResidua() != null) {
				lPenaResiduaDao = new PenaResiduaDAO(lConn);
				//
				PenaResiduaModel lPenResModel = aDettaglioFasOrig.getPenaResidua();
				//
				lPenResModel.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lPenResModel.setEveIdEvento(null);
				lPenResModel.setMisAltIdMisuraAlternativa(null);
				//
				lPenResModel.setDataInserimento(aFascicoloSiep.getDataInserimento());
				lPenResModel.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
				lPenResModel.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
				lPenResModel.setDataAggiornamento(null);
				lPenResModel.setCodOperatoreAggiornamento(null);
				lPenResModel.setCodUfficioAggiornamento(null);
				//
				lPenaResiduaDao.setDAOFromModel(lPenResModel);
				lPenaResiduaDao.insert();
			}

			// ===================================
			// Duplicazione Reati e Circostanze
			// NO su indicazione di MT
			// ===================================
			// Insert Reato e le circostanze sempre e solo da tabella REATO
			// if (aDettaglioFasOrig.getReatiCircostanze() != null) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Duplicazione Reati...");
			//
			// lReaDao = new ReatoDAO(lConn);
			// List lLisReaCir = aDettaglioFasOrig.getReatiCircostanze();
			//
			// // ciclo su questa lista
			// for (int i=0; i<=lLisReaCir.size()-1;i++)
			// {
			// // prendo il reato per scriverlo
			// ReatoModel lReaMod = ((ReatoCircostanzaModel) lLisReaCir.get(i)).getReato();
			// // scrivo reato
			// lReaMod.setFasSieIdFascicoloSiep (aFascicoloSiep.getIdFascicoloSiep());
			//
			// lReaMod.setDataInserimento (aFascicoloSiep.getDataInserimento());
			// lReaMod.setCodOperatoreInserimento (aFascicoloSiep.getCodOperatoreInserimento());
			// lReaMod.setCodUfficioInserimento (aFascicoloSiep.getCodUfficioInserimento());
			// lReaMod.setDataAggiornamento (null);
			// lReaMod.setCodOperatoreAggiornamento (null);
			// lReaMod.setCodUfficioAggiornamento (null);
			//
			// lReaDao.setDAOFromModel(lReaMod);
			// lReaDao.insert();
			//
			// // prendo la lista delle circostanze affogate nella tabella reati
			// ReatoModel[] lLisCir = ((ReatoCircostanzaModel) lLisReaCir.get(i)).getCircostanze();
			// // Ciclo sulle circostanze affogate nella tabella REATI
			// for (int j=0; j<=lLisCir.length-1;j++)
			// {
			// // prendo la circostanza per scriverla
			// lReaMod = lLisCir[j];
			// // scrivo la circostanza
			// lReaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			//
			// lReaMod.setDataInserimento (aFascicoloSiep.getDataInserimento());
			// lReaMod.setCodOperatoreInserimento (aFascicoloSiep.getCodOperatoreInserimento());
			// lReaMod.setCodUfficioInserimento (aFascicoloSiep.getCodUfficioInserimento());
			// lReaMod.setDataAggiornamento (null);
			// lReaMod.setCodOperatoreAggiornamento (null);
			// lReaMod.setCodUfficioAggiornamento (null);
			//
			// lReaDao.setDAOFromModel(lReaMod);
			// lReaDao.insert();
			// }
			// }
			// }

			// ===================================
			// Duplicazione Circostanze
			// NO su indicazione di MT
			// ===================================
			// if (aDettaglioFasOrig.getCircostanze() != null) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Duplicazione Circostanze...");
			//
			// List lLisCir = aDettaglioFasOrig.getCircostanze();
			// lCirDao = new CircostanzaDAO(lConn);
			// for (int i=0; i<= lLisCir.size()-1;i++)
			// {
			// CircostanzaModel lCirMod = (CircostanzaModel) lLisCir.get(i);
			// lCirMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			//
			// lCirMod.setDataInserimento (aFascicoloSiep.getDataInserimento());
			// lCirMod.setCodOperatoreInserimento (aFascicoloSiep.getCodOperatoreInserimento());
			// lCirMod.setCodUfficioInserimento (aFascicoloSiep.getCodUfficioInserimento());
			// lCirMod.setDataAggiornamento (null);
			// lCirMod.setCodOperatoreAggiornamento (null);
			// lCirMod.setCodUfficioAggiornamento (null);
			//
			// lCirDao.setDAOFromModel(lCirMod);
			// lCirDao.insert();
			// }
			// }

			// ========================================================================
			// Duplicazione Misure di Sicurezza
			// n.b. solo quelle in sentenza. Per quelle in cumulo non e' possibile
			// essendo iscritte a testo libero su PENA_CUMULO
			// ========================================================================

			siesLogger.debug("Duplicazione Misure di Sicurezza...");
			List lListaMisure = aDettaglioFasOrig.getMisureSicurezza();
			PenaCumuloModel lPenaCumulo = aDettaglioFasOrig.getPenaCumulo();
			if (lPenaCumulo != null && lPenaCumulo.getMisuraSicurezza() != null
					&& lPenaCumulo.getMisuraSicurezza().length() > 0) {
				// Non iscrivo nulla. Le MS in cumulo sono a testo libero. Sara' compito dell'utente
				// inserirle a mano
			} else if (lListaMisure != null && lListaMisure.size() > 0) {
				for (int i = 0; i < lListaMisure.size(); i++) {
					MisuraSicurezzaModel lMisura = (MisuraSicurezzaModel) lListaMisure.get(i);
					MisuraSicurezzaModel lMisuraNew = new MisuraSicurezzaModel(lMisura);
					lMisuraNew.setFasSieIdFascicoloSiep(lkeyFasIV); // Collegata el fascicolo di classe IV
					lMisuraNew.setEveIdEvento(null);
					lMisuraNew.setFasSiuIdFascicoloSius(null);
					lMisuraNew.setDataInserimento(aFascicoloSiep.getDataInserimento());
					lMisuraNew.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lMisuraNew.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lMisuraNew.setDataAggiornamento(null);
					lMisuraNew.setCodOperatoreAggiornamento(null);
					lMisuraNew.setCodUfficioAggiornamento(null);
					lMisuraSicurezzaDao = new MisuraSicurezzaDAO(lConn);
					lMisuraSicurezzaDao.setDAOFromModel(lMisuraNew);
					lMisuraSicurezzaDao.insert();
					lMisuraSicurezzaDao.stop();
				}
			}

			// =====================================================
			// Collego il fascicolo di Origine al Nuovo fascicolo
			// =====================================================

			siesLogger.debug("Collego il fascicolo di Origine al Nuovo fascicolo");
			FascMsToFascSiepModel lFascMsToFascSiepModel = new FascMsToFascSiepModel();
			lFascMsToFascSiepModel.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			lFascMsToFascSiepModel.setChiaveAnnoSiep(aFascicoloSiep.getChiaveAnno());
			lFascMsToFascSiepModel.setChiaveProgrSiep(aFascicoloSiep.getChiaveProgr());
			lFascMsToFascSiepModel.setChiaveUfficioSiep(aFascicoloSiep.getChiaveUfficio());

			lFascMsToFascSiepModel
					.setCodTipoRelazioneMS(ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_IN_ESECUZIONE_DI);

			// Eventuale data Ultimo Cumulo sul fascicolo di Origine
			if (aDataCumulo != null)
				lFascMsToFascSiepModel.setDataCumulo(aDataCumulo);

			// Fascicolo collegato, quello da cui nasce il fascicolo di esecuzione MS
			lFascMsToFascSiepModel
					.setFasSieIdFascicoloCollegato(aDettaglioFasOrig.getFascicoloSiep().getIdFascicoloSiep());
			lFascMsToFascSiepModel
					.setChiaveAnnoSiepCollegato(aDettaglioFasOrig.getFascicoloSiep().getChiaveAnno());
			lFascMsToFascSiepModel
					.setChiaveProgrSiepCollegato(aDettaglioFasOrig.getFascicoloSiep().getChiaveProgr());
			lFascMsToFascSiepModel
					.setChiaveUfficioSiepCollegato(aDettaglioFasOrig.getFascicoloSiep().getChiaveUfficio());

			// Lego l'iscrizione al messaggio di Richiesta se presente
			lFascMsToFascSiepModel.setMesIdMessaggio(aIdMessaggio);

			lFascMsToFascSiepModel.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			lFascMsToFascSiepModel.setDataInserimento(aFascicoloSiep.getDataInserimento());
			lFascMsToFascSiepModel.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());

			lFascMsToFascSiepModel.setCodOperatoreAggiornamento(null);
			lFascMsToFascSiepModel.setDataAggiornamento(null);
			lFascMsToFascSiepModel.setCodUfficioAggiornamento(null);

			lFascMsToFascSiepDao = new FascMsToFascSiepDAO(lConn);
			lFascMsToFascSiepDao.setDAOFromModel(lFascMsToFascSiepModel);
			lFascMsToFascSiepDao.insert();
			lFascMsToFascSiepDao.stop();

			// Se stesso ufficio inserisco anche il record per il fascicolo di origine
			if (aFascicoloSiep.getChiaveUfficio()
					.equals(aDettaglioFasOrig.getFascicoloSiep().getChiaveUfficio())) {
				lFascMsToFascSiepModel = new FascMsToFascSiepModel();

				// fascicolo di origine
				lFascMsToFascSiepModel
						.setFasSieIdFascicoloSiep(aDettaglioFasOrig.getFascicoloSiep().getIdFascicoloSiep());
				lFascMsToFascSiepModel
						.setChiaveAnnoSiep(aDettaglioFasOrig.getFascicoloSiep().getChiaveAnno());
				lFascMsToFascSiepModel
						.setChiaveProgrSiep(aDettaglioFasOrig.getFascicoloSiep().getChiaveProgr());
				lFascMsToFascSiepModel
						.setChiaveUfficioSiep(aDettaglioFasOrig.getFascicoloSiep().getChiaveUfficio());

				// Tipo di relazine
				lFascMsToFascSiepModel
						.setCodTipoRelazioneMS(ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_ISCRITTO_AL);

				// fascicolo di esecuzione
				lFascMsToFascSiepModel.setFasSieIdFascicoloCollegato(aFascicoloSiep.getIdFascicoloSiep());
				lFascMsToFascSiepModel.setChiaveAnnoSiepCollegato(aFascicoloSiep.getChiaveAnno());
				lFascMsToFascSiepModel.setChiaveProgrSiepCollegato(aFascicoloSiep.getChiaveProgr());
				lFascMsToFascSiepModel.setChiaveUfficioSiepCollegato(aFascicoloSiep.getChiaveUfficio());

				lFascMsToFascSiepModel
						.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
				lFascMsToFascSiepModel.setDataInserimento(aFascicoloSiep.getDataInserimento());
				lFascMsToFascSiepModel.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());

				lFascMsToFascSiepModel.setCodOperatoreAggiornamento(null);
				lFascMsToFascSiepModel.setDataAggiornamento(null);
				lFascMsToFascSiepModel.setCodUfficioAggiornamento(null);

				lFascMsToFascSiepDao.setDAOFromModel(lFascMsToFascSiepModel);
				lFascMsToFascSiepDao.insert();
				lFascMsToFascSiepDao.stop();
			}

			// STATO PROCEDIMENTO
			StatoProcedimentoModel lStat = new StatoProcedimentoModel();
			lStat.setFasSieIdFascicoloSiep(lkeyFasIV);
			lStat.setProgressivo(new BigDecimal(1));
			lStat.setDataInserimento(DateUtils.getSysDate());
			lStat.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			lStat.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			lStat.setCodStatoProcedimento("0108"); // Iscritto
			lStatoProcDao = new StatoProcedimentoDAO(lConn);
			lStatoProcDao.setDAOFromModel(lStat);
			lStatoProcDao.insert();

			// INIZIO MEV_39
			// in fase di inserimento di un procedimento di classe IV deve essere previsto anche
			// l'inserimento nello scadenzario_siep con il nuovo codice_tipo_scadenzario = 20)
			lScaDao = new ScadenzarioDAO(lConn);

			// recupero la pena residua relativa al fascicolo origine di CLASSE I
			// si procede con l'inserimento in scadenzario solo se è presente, se è validata
			// se se esiste una data_fine (queste informazioni infatti servono per valorizzare principalmente
			// i campi DATA_INIZIO_SCADENZA e DATA_FINE_SCADENZA per il record da inserire in
			// SCADENZARIO_SIEP)
			if (aDettaglioFasOrig.getPenaResidua() != null && lPosMod.getCodPosizioneGiuridica() != null) {
				PenaResiduaModel lPenResModel = aDettaglioFasOrig.getPenaResidua();
				if (lPenResModel != null && lPenResModel.getFlagValidato() != null
						&& "S".equals(lPenResModel.getFlagValidato()) && lPenResModel.getDataFine() != null) {
					String[] posizioneGiuridica = { "07", "10", "16", "17", "20", "26", "30", "46", "47" };
					// inoltre si prosegue con l'inserimento in scadenzario solo per determinati codici della
					// posizione giuridica (diversi da quelli nell'array)
					// (ho visto lo stesso controllo previsto alla riga 4096 della classe
					// OrdineEsecuzioneController)
					if (Arrays.binarySearch(posizioneGiuridica, lPosMod.getCodPosizioneGiuridica()) < 0) {
						if (lPenResModel.getDataInizio() != null) {
							ScadenzarioModel lScaMod = new ScadenzarioModel();
							// inserisce scadenzario
							lScaMod.setCodTipoScadenzario("20"); // Inizio_Misura
							lScaMod.setDataInizioScadenza(lPenResModel.getDataInizio());
							lScaMod.setDataFineScadenza(lPenResModel.getDataFine());
							// devo inserire idFAsc di classe IV
							lScaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
							lScaMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
							lScaMod.setDataInserimento(DateUtils.getSysDate());
							lScaMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
							lScaMod.setIdFascicoloSiepOrigine(
									aDettaglioFasOrig.getFascicoloSiep().getIdFascicoloSiep()); // fascicolo
																								// di CLasse I
							lScaDao.setDAOFromModel(lScaMod);
							lScaDao.insert();
							lScaDao.stop();
						}
					}
				}
			} // se non ho la pena residua???FIXME
			else {
				// si procede con l'inserimento in scadenzario_siep di un record con tipo_Scadenzario=20
				// ma i campi DATA_INIZIO_SCADENZA e DATA_FINE_SCADENZA non posso recuperarli dalla
				// PenaResidua, pertanto in dataInizioScadenza inserisco sysdate e data_fine_scadenza null
				ScadenzarioModel lScaMod = new ScadenzarioModel();
				// inserisce scadenzario
				lScaMod.setCodTipoScadenzario("20"); // Inizio_Misura
				lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
				// devo inserire idFAsc di classe IV
				lScaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lScaMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
				lScaMod.setDataInserimento(DateUtils.getSysDate());
				lScaMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
				// fascicolo di CLasse I
				lScaMod.setIdFascicoloSiepOrigine(aDettaglioFasOrig.getFascicoloSiep().getIdFascicoloSiep());
				lScaDao.setDAOFromModel(lScaMod);
				lScaDao.insert();
				lScaDao.stop();
			}
			// FINE MEV_39

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("MisuraSicurezzaController.ExInserisciFascicoloClasseIV: ", daoEx);
			throw new F3BException("MisuraSicurezzaController.ExInserisciFascicoloClasseIV: " + daoEx);
		} finally {
			cleanup(lSoggDao);
			cleanup(lFascSiepSqlDao);
			cleanup(lFascSiepDao);
			cleanup(lMisuraSicurezzaDao);
			cleanup(lFascMsToFascSiepDao);
			cleanup(lResidenzaDao);
			cleanup(lResidenzaFascicoloSiepDAO);
			cleanup(lAvvFasDao);
			// cleanup(lReaDao);
			// cleanup(lCirDao);
			cleanup(lPosDao);
			// cleanup(lPenaComplessivaDao);
			// cleanup(lSanzioneSostitutivaDAO);
			cleanup(lSenSqlDao);
			cleanup(lSenDao);
			cleanup(lLuogoDetDao);
			cleanup(lPenaResiduaDao);
			cleanup(lAltraCausaDao);
			cleanup(lSentenzaRiunitaFascSiepSqlDao);
			cleanup(lSentenzaRiunitaFascSiepDao);
			cleanup(lSentenzaRiunitaDao);
			cleanup(lAgdgFascicoloSiepSqlDao);
			cleanup(lAgdgFascicoloSiepDao);
			cleanup(lAltriGradiDAO);
			cleanup(lStatoProcDao);
			// MEV_39
			cleanup(lScaDao);

			cleanup(lConn);
		}
	}

	public BigDecimal ExInserisciAnnotazioneEsito(EventoNotificaModel aEventoNot,
			AnnotazioneEsitoTrasmissioneModel aAnnotaModel) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Inizio inserimento annotazione...");

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEventoSqlDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		AnnotazioneEsitoTrasmissioneDAO lAnnotaDao = null;

		BigDecimal lKeyEvento = null;
		try {
			lConn = getDBConnection();

			// Setto l'anno e il progressivo...
			lEventoSqlDao = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEventoSqlDao.getProgressivo(aEventoNot.getEvento());
			aEventoNot.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			// =============================================
			// Insert dell'evento
			// =============================================
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEventoNot.getEvento());
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// =============================================
			// Insert AnnotazioneEsito
			// =============================================
			lAnnotaDao = new AnnotazioneEsitoTrasmissioneDAO(lConn);
			aAnnotaModel.setEveIdEvento(lKeyEvento);
			lAnnotaDao.setDAOFromModel(aAnnotaModel);
			lAnnotaDao.insert();
			lAnnotaDao.stop();

			// =============================================
			// Insert Note
			// =============================================
			if (aEventoNot.getCampoNote() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : "
						+ aEventoNot.getCampoNote().length);
				int count = 0;
				lCampoNotaDao = new CampoNotaDAO(lConn);
				while (count < aEventoNot.getCampoNote().length) {
					aEventoNot.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					aEventoNot.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEventoNot.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}

			commit(lConn);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("MisuraSicurezzaController.ExInserisciAnnotazioneEsito: ", e);
			throw new F3BException("MisuraSicurezzaController.ExInserisciAnnotazioneEsito: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lEventoSqlDao);
			cleanup(lCampoNotaDao);
			cleanup(lAnnotaDao);

			cleanup(lConn);
		}
		return lKeyEvento;
	}

	/**
	 * MEtodo di validazione del provvedimeto di Annotazione Esito Trasmissione
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaAnnotazioneEsito(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		EventoSqlDAO lEveSqlDao = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		EventoDAO lEveDaoBlob = null;
		AnnotazioneEsitoTrasmissioneSqlDAO lAnnotazSqlDao = null;
		FascMsToFascSiepDAO lFascMsToFascSiepDao = null;
		FascMsToFascSiepSqlDAO lFasMsSqlDao = null;
		
		// MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni
		MessaggioSqlDAO lMessaggioSqlDao = null;
		MessaggioDAO    lMessaggioDao = null;

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data emissione provvedimento (decreto)
			// ========================================================================
			// StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			//
			// lStatoProcMod.setProgressivo(new BigDecimal(1));
			// lStatoProcMod.setCodStatoProcedimento("0437"); // Da stabilire new
			//
			// lStatoProcMod.setData(lEveModel.getDataEmissione());
			//
			// lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			//
			// lStatoProcMod.setCodOperatoreInserimento (aEvento.getCodOperatoreAggiornamento());
			// lStatoProcMod.setDataInserimento (aEvento.getDataAggiornamento());
			// lStatoProcMod.setCodUfficioInserimento (aEvento.getCodUfficioAggiornamento());
			//
			//
			// lStatoProcDao = new StatoProcedimentoDAO(lConn);
			//
			// // - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			// lStatoProcDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			// lStatoProcDao.delete();
			//
			// // - Inserisce
			// lStatoProcDao.setDAOFromModel(lStatoProcMod);
			// lStatoProcDao.insert();
			// lStatoProcDao.stop();

			// ========================================================================
			// Aggiorno lo NOME_PROCEDIMENTO [eventualmente]
			// ========================================================================

			// ========================================================================
			// Aggiorno lo FASC_MS_TO_FASC_SIEP [eventualmente]
			// ========================================================================
			lAnnotazSqlDao = new AnnotazioneEsitoTrasmissioneSqlDAO(lConn);
			AnnotazioneEsitoTrasmissioneModel lAnnotazModel = null;

			lAnnotazSqlDao.ricercaAnnotazioneEsitoTrasmissioneByIdEvento(lEveModel.getIdEvento());
			lAnnotazModel = (AnnotazioneEsitoTrasmissioneModel) lAnnotazSqlDao.getModelByKey();

			if (lAnnotazModel.getCodEsito().equals(ICostantiJMS.ISCRITTO_CLASSE_IV)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Collego il fascicolo di Origine al Nuovo fascicolo");
				// Il collegamento va fatto solo se non e' gia' presente infatti il collegamento
				// viene iscritto in automatico in fase di scarico della risposta jms di
				// avvenuta iscrizione.

				lFasMsSqlDao = new FascMsToFascSiepSqlDAO(lConn);

				FascMsToFascSiepModel lFascMsToFascSiepModelRicerca = new FascMsToFascSiepModel();

				lFascMsToFascSiepModelRicerca.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

				lFascMsToFascSiepModelRicerca
						.setCodTipoRelazioneMS(ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_ISCRITTO_AL);

				lFascMsToFascSiepModelRicerca.setChiaveAnnoSiepCollegato(lAnnotazModel.getChiaveAnno());
				lFascMsToFascSiepModelRicerca.setChiaveProgrSiepCollegato(lAnnotazModel.getChiaveProgr());
				lFascMsToFascSiepModelRicerca.setChiaveUfficioSiepCollegato(lAnnotazModel.getChiaveUfficio());

				lFasMsSqlDao.ricercaFascMsToFascSiep(lFascMsToFascSiepModelRicerca);

				Vector lListaCollegamenti = new Vector(lFasMsSqlDao.getModels());

				if (lListaCollegamenti != null && lListaCollegamenti.size() > 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Il collegamento e' gia' registrato sulla FASC_MS_TO_FASC_SIEP");
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"Il collegamento non e' presente procedo alla registrazione sulla FASC_MS_TO_FASC_SIEP");
					FascMsToFascSiepModel lFascMsToFascSiepModel = new FascMsToFascSiepModel();

					// Fascicolo di origine
					lFascMsToFascSiepModel.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					lFascMsToFascSiepModel.setChiaveAnnoSiep(aFascicolo.getChiaveAnno());
					lFascMsToFascSiepModel.setChiaveProgrSiep(aFascicolo.getChiaveProgr());
					lFascMsToFascSiepModel.setChiaveUfficioSiep(aFascicolo.getChiaveUfficio());

					lFascMsToFascSiepModel.setCodTipoRelazioneMS(
							ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_ISCRITTO_AL);

					// Nuovo Fascicolo di classe IV
					lFascMsToFascSiepModel.setFasSieIdFascicoloCollegato(null);
					lFascMsToFascSiepModel.setChiaveAnnoSiepCollegato(lAnnotazModel.getChiaveAnno());
					lFascMsToFascSiepModel.setChiaveProgrSiepCollegato(lAnnotazModel.getChiaveProgr());
					lFascMsToFascSiepModel.setChiaveUfficioSiepCollegato(lAnnotazModel.getChiaveUfficio());

					// Lego l'iscrizione al messaggio di Richiesta se presente
					lFascMsToFascSiepModel.setMesIdMessaggio(lAnnotazModel.getMesIdMessaggioRichiesta());
					lFascMsToFascSiepModel.setEveIdEvento(aEvento.getIdEvento());

					lFascMsToFascSiepModel.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lFascMsToFascSiepModel.setDataInserimento(aEvento.getDataAggiornamento());
					lFascMsToFascSiepModel.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

					lFascMsToFascSiepModel.setCodOperatoreAggiornamento(null);
					lFascMsToFascSiepModel.setDataAggiornamento(null);
					lFascMsToFascSiepModel.setCodUfficioAggiornamento(null);

					lFascMsToFascSiepDao = new FascMsToFascSiepDAO(lConn);
					lFascMsToFascSiepDao.setDAOFromModel(lFascMsToFascSiepModel);
					lFascMsToFascSiepDao.insert();
					lFascMsToFascSiepDao.stop();
				}
			}
			// MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni
			else if (   lAnnotazModel.getOggettoTrasmissione()!=null
			         && (   lAnnotazModel.getOggettoTrasmissione().equals(ICostantiJMS.TRASFERIMENTO_COMPETENZA)
			             || lAnnotazModel.getOggettoTrasmissione().equals(ICostantiJMS.SEGUITO_ATTI_TRASFERIMENTO_COMPETENZA)
			             //|| lAnnotazModel.getOggettoTrasmissione().equals(ICostantiJMS.SEGUITO_ATTI_TRASFERIMENTO_COMPETENZA)
			             // verificare altri codici tipo annoazione esito comunicazione alle procure
			            )
			        ) 
			{
			  siesLogger.debug("MEV_2025-48: verifico se stao annotando l'esito da un messaggio Cumulo");
			  // Se sto annotando l'esito di 
			  // 00067 - ESITO TRASFERIMENTO COMPETENZA
			  // 00079 - COMUNICAZIONE CUMULO PROCURE COMPETENTI
			  // 00080 - ESITO SEGUITO ATTI
		      if (lAnnotazModel.getMesIdMessaggioEsito()!=null) {
		          lMessaggioSqlDao = new MessaggioSqlDAO(lConn);
		          lMessaggioSqlDao.ricercaMessaggioByKey(lAnnotazModel.getMesIdMessaggioEsito());
		          
		          MessaggioModel lMsgEsito = (MessaggioModel) lMessaggioSqlDao.getModelByKey();
		          if (lMsgEsito!=null)
		              siesLogger.debug("Msg: "+lMsgEsito.getIdMessaggio()
		                                +" - "+lMsgEsito.getCodTipoOperazione()
		                                +" - "+lMsgEsito.getCodEsito()
		                                +" - "+lMsgEsito.getFlagVisto());
		          
		          if (   lMsgEsito!= null 
		              && "N".equals(lMsgEsito.getFlagVisto())
		              && (   lMsgEsito.getCodTipoOperazione().equals(ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA)
		                  || lMsgEsito.getCodTipoOperazione().equals(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI)
		                  || lMsgEsito.getCodTipoOperazione().equals(ICostantiJMS.ESITO_SEGUITO_ATTI)
		                 )
		             ) 
		          {
		              siesLogger.debug("MSG Esito Cumulo non ancora marcato, lo marco come visto...");
		              // lMessaggioDao
		              lMessaggioDao = new MessaggioDAO(lConn);
		              lMessaggioDao.setFlagVisto("S");
		              lMessaggioDao.setCondizioneUpdate(lMsgEsito.getIdMessaggio());
		              lMessaggioDao.update();
		              lMessaggioDao.stop();
		          }
		      }
		    }
			// MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni - FINE

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			commit(lConn);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraSicurezzaController.ExUpdateValidaAnnotazioneEsito : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lStatoProcDao);
			cleanup(lEveDaoBlob);
			cleanup(lAnnotazSqlDao);
			cleanup(lFascMsToFascSiepDao);
			cleanup(lFasMsSqlDao);
			
		     // MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni
			cleanup(lMessaggioSqlDao);
			cleanup(lMessaggioDao);

			cleanup(lConn);
		}

		return lEveMod;
	}

	// Recupero le Misure di Sicurezza e i Riferimenti Titolo Esecutivo associati alla stessa
	public Vector ExRicercaMisuraSicurezzaAndRifTitoloEsec(MisuraSicurezzaModel aMisuraSicurezza)
			throws F3BException {

		Connection lConn = null;

		Vector lMisuraSicurezza = new Vector();
		MisuraSicurezzaSqlDAO lMisDao = null;
		RiferimentoFascicoloSiepSqlDAO rifFascSiepDao = null;
		FascicoloSiepSqlDAO fascSiepDao = null;

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDao.ricercaMisuraSicurezza(aMisuraSicurezza);
			lMisDao.start();

			MisuraSicurezzaModel misSicMod = null;
			rifFascSiepDao = new RiferimentoFascicoloSiepSqlDAO(lConn);
			fascSiepDao = new FascicoloSiepSqlDAO(lConn);

			while (lMisDao.next()) {
				RiferimentoFascicoloSiepModel rifFascSiepMod = null;
				FascicoloSiepModel fascSiepMod = null;

				misSicMod = (MisuraSicurezzaModel) lMisDao.getModel();

				BigDecimal rifAltroTitoloEsec = misSicMod.getFasSieIdFascicoloSiepRif();
				BigDecimal rifTitoloEsecPrinc = misSicMod.getSenIdSentenza();

				// Recupero il riferimento al titolo esecutivo (Altro Titolo Esecutivo)
				if (rifAltroTitoloEsec != null) {
					rifFascSiepDao.ricercaRiferimentoFascicoloSiepByKey(rifAltroTitoloEsec);
					rifFascSiepMod = (RiferimentoFascicoloSiepModel) rifFascSiepDao.getModelByKey();
				}
				// Recupero il riferimento al titolo esecutivo (Titolo Esecutivo Principale)
				if (rifTitoloEsecPrinc != null) {
					fascSiepDao.ricercaFascicoloByIDSentenza(rifTitoloEsecPrinc);
					fascSiepDao.start();
					if (fascSiepDao.next()) {
						fascSiepMod = (FascicoloSiepModel) fascSiepDao.getModelsPerIdSentenza();
						fascSiepDao.stop();
					}
				}

				misSicMod.setRiferimentoFascicoloSiep(rifFascSiepMod);
				misSicMod.setFascicoloSiep(fascSiepMod);
				lMisuraSicurezza.add(misSicMod);
			}

			lMisDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaAndRifTitoloEsec: Non posso leggere : "
							+ daoEx);
		} catch (Exception sqe) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaAndRifTitoloEsec: Non posso leggere  -> "
							+ sqe);
		} finally {
			cleanup(lMisDao);
			cleanup(rifFascSiepDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(fascSiepDao);

			cleanup(lConn);
		}
		return lMisuraSicurezza;
	}

	public List ExRicercaMisuraSicurezzaByIdFascicoloSIUS(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MisuraSicurezzaSqlDAO lMisDao = null;
		List lMisure = new ArrayList();

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDao.ricercaMisuraSicurezzaByIdFascicoloSIUS(aKey);
			lMisure = new Vector(lMisDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaByIdFascicoloSIUS: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisure;
	}

	public List<MisuraSicurezzaModel> ExRicercaMisuraSicurezzaByIdEvento(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		MisuraSicurezzaSqlDAO lMisDao = null;
		List<MisuraSicurezzaModel> lMisure = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDao.ricercaMisuraSicurezzaByIdEvento(aKey);
			lMisure = new Vector<MisuraSicurezzaModel>(lMisDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaByIdFascicoloSIUS: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisure;
	}

	// 10-02-2014
	public List ExRicercaMisuraSicurezzaByIdFascicoloOrd(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		MisuraSicurezzaSqlDAO lMisDao = null;
		List lMisure = new ArrayList();

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraSicurezzaSqlDAO(lConn);

			lMisDao.ricercaMisuraSicurezzaByIdFascicoloOrd(aKey);

			lMisure = new Vector(lMisDao.getModels());

			if (lMisure.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Procedimento Privo di Misure di Sicurezza");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisure;
	} // chiude ExRicercaMisuraSicurezzaByIdFascicoloOrd

	// 17-12-2014
	public List ExRicercaTutteMisureSicurezzaByIdFascicoloOrd(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		MisuraSicurezzaSqlDAO lMisDao = null;
		List lMisure = new ArrayList();
		try {
			lConn = getDBConnection();
			lMisDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDao.ricercaTutteMisureSicurezzaByIdFascicoloOrd(aKey);
			lMisure = new Vector(lMisDao.getModels());

			if (lMisure.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Procedimento Privo di Misure di Sicurezza");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaTutteMisureSicurezzaByIdFascicoloOrd: " + daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisure;
	}

	/**
	*
	*/
	public BigDecimal ExInserisciSollecitoEsito(EventoNotificaModel aEventoNot,
			SollecitoEsitoTrasmissioneModel aSollecitoModel) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Inizio inserimento sollecito...");

		Connection lConn = null;

		EventoSqlDAO lEventoSqlDao = null;
		EventoDAO lEveDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		SollecitoEsitoTrasmissioneDAO lSollecitoDAO = null;

		BigDecimal lKeyEvento = null;
		try {
			lConn = getDBConnection();

			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);

			// Setto l'anno e il progressivo...
			lEventoSqlDao = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEventoSqlDao.getProgressivo(aEventoNot.getEvento());
			aEventoNot.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			// =============================================
			// Insert dell'evento
			// =============================================
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEventoNot.getEvento());
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// =============================================
			// Insert delle notifiche
			// =============================================
			if (aEventoNot.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + aEventoNot.getNotifiche().length + " notifiche");
				int count = 0;
				while (count < aEventoNot.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + aEventoNot.getNotifiche()[count]);

					if (aEventoNot.getNotifiche()[count] != null) {

						if (aEventoNot.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao.setRicercaByAutSede(
									aEventoNot.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();
							BigDecimal lKeyAutorita = null;
							if (lAutMod == null) {
								lAutDao.setDAOFromModel(
										aEventoNot.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
								aEventoNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEventoNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						aEventoNot.getNotifiche()[count].setEveIdEvento(lKeyEvento);

						lNotDao.setDAOFromModel(aEventoNot.getNotifiche()[count]);
						lNotDao.insert();
						lNotDao.stop();

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserito evento" + lKeyEvento);
					}
					count++;
				}
			}

			// =============================================
			// Insert Note
			// =============================================
			if (aEventoNot.getCampoNote() != null) {
				// /// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// /siesLogger.debug("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : "
				// + aEventoNot.getCampoNote().length);
				int count = 0;
				lCampoNotaDao = new CampoNotaDAO(lConn);
				while (count < aEventoNot.getCampoNote().length) {
					aEventoNot.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					aEventoNot.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEventoNot.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}

			// =============================================
			// Insert dell'evento
			// =============================================
			aSollecitoModel.setEveIdEvento(lKeyEvento);

			lSollecitoDAO = new SollecitoEsitoTrasmissioneDAO(lConn);
			lSollecitoDAO.setDAOFromModel(aSollecitoModel);
			BigDecimal lKeySollecito = lSollecitoDAO.insert();
			lSollecitoDAO.stop();
			aSollecitoModel.setIdSollecito(lKeySollecito);

			// rollback(lConn);
			commit(lConn);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("MisuraSicurezzaController.ExInserisciSollecitoEsito: ", e);
			throw new F3BException("MisuraSicurezzaController.ExInserisciSollecitoEsito: " + e);
		} finally {
			cleanup(lEventoSqlDao);
			cleanup(lEveDao);
			cleanup(lCampoNotaDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSollecitoDAO);

			cleanup(lConn);
		}
		return lKeyEvento;
	}

	/**
	 * MEtodo di validazione del provvedimeto di Sollecito Esito Trasmissione
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaSollecitoEsito(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		EventoSqlDAO lEveSqlDao = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			// EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data emissione provvedimento (decreto)
			// ========================================================================
			// StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			//
			// lStatoProcMod.setProgressivo(new BigDecimal(1));
			// lStatoProcMod.setCodStatoProcedimento("0437"); // Da stabilire new
			//
			// lStatoProcMod.setData(lEveModel.getDataEmissione());
			//
			// lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			//
			// lStatoProcMod.setCodOperatoreInserimento (aEvento.getCodOperatoreAggiornamento());
			// lStatoProcMod.setDataInserimento (aEvento.getDataAggiornamento());
			// lStatoProcMod.setCodUfficioInserimento (aEvento.getCodUfficioAggiornamento());
			//
			//
			// lStatoProcDao = new StatoProcedimentoDAO(lConn);
			//
			// // - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			// lStatoProcDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			// lStatoProcDao.delete();
			//
			// // - Inserisce
			// lStatoProcDao.setDAOFromModel(lStatoProcMod);
			// lStatoProcDao.insert();
			// lStatoProcDao.stop();

			// ========================================================================
			// Aggiorno lo NOME_PROCEDIMENTO [eventualmente]
			// ========================================================================

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			commit(lConn);

		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("MisuraSicurezzaController.ExUpdateValidaSollecitoEsito : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lStatoProcDao);
			cleanup(lEveDaoBlob);

			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * MEtodo di validazione del provvedimeto di Annotazione Designazione Istituto Detenzione da parte del DAP
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaAnnotazioneDesignazioneIst(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		EventoVerbaleModel lEveVer = new EventoVerbaleModel();

		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDaoBlob = null;
		// LuogoDetenzioneDAO lLuoDao = null;
		// LuogoDetenzioneModel lLuoMod = new LuogoDetenzioneModel();
		// PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		// PosizioneGiuridicaDAO lPosDao = null;
		MisuraSicurezzaDAO lMisDao = null;

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Recupero l'EVENTO e il VERBALE , quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoVerbaleByIdEve(aEvento.getIdEvento());

			lEveSqlDao.start();
			if (lEveSqlDao.next())
				lEveVer = lEveSqlDao.getModelIstituto();
			lEveSqlDao.stop();

			// Misure Sicurezza
			// 18-02-2015 - lego Istituto detenzione alla Misura Sicurezza Detentiva
			List lListMis = new ArrayList();
			MisuraSicurezzaModel lMisSicMod = null;
			MisuraSicurezzaModel lMisSicdet = null;
			IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
			lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(aFascicolo.getIdFascicoloSiep());

			// prendo solo l'ultima MISURA DETENTIVA
			if (lListMis != null && lListMis.size() > 0) {
				for (int k = 0; k < lListMis.size(); k++) {
					lMisSicMod = (MisuraSicurezzaModel) lListMis.get(k);
					if (lMisSicMod != null && lMisSicMod.getIdMisuraSicurezza() != null
							&& lMisSicMod.getCodNatura() != null) {
						if (lMisSicMod.getCodNatura().compareTo("01") == 0) { // ultima
							lMisSicdet = (MisuraSicurezzaModel) lListMis.get(k);
						}
					}
				}
			} else {
				throw new F3BException(F3BException.USER_MESSAGE,
						"ERRORE: Procedimento Privo di Misura Sicurezza di tipo Detentivo");
			}

			lMisDao = new MisuraSicurezzaDAO(lConn);

			if (lEveVer != null && lEveVer.getVerbale() != null
					&& lEveVer.getVerbale().getIstDetIdIstitutoDetenzione() != null) {
				lMisSicdet
						.setIstDetIdIstitutoDetenzione(lEveVer.getVerbale().getIstDetIdIstitutoDetenzione());
				lMisSicdet.setDataAggiornamento(aEvento.getDataAggiornamento());
				lMisSicdet.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lMisSicdet.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lMisDao.setDAOFromModelForUpdate(lMisSicdet);
				lMisDao.update();
				lMisDao.stop();
			}

			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data emissione provvedimento (decreto)
			// ========================================================================
			// StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			//
			// lStatoProcMod.setProgressivo(new BigDecimal(1));
			// lStatoProcMod.setCodStatoProcedimento("0437"); // Da stabilire new
			//
			// lStatoProcMod.setData(lEveModel.getDataEmissione());
			//
			// lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			//
			// lStatoProcMod.setCodOperatoreInserimento (aEvento.getCodOperatoreAggiornamento());
			// lStatoProcMod.setDataInserimento (aEvento.getDataAggiornamento());
			// lStatoProcMod.setCodUfficioInserimento (aEvento.getCodUfficioAggiornamento());
			//
			//
			// lStatoProcDao = new StatoProcedimentoDAO(lConn);
			//
			// // - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			// lStatoProcDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			// lStatoProcDao.delete();
			//
			// // - Inserisce
			// lStatoProcDao.setDAOFromModel(lStatoProcMod);
			// lStatoProcDao.insert();
			// lStatoProcDao.stop();

			// ========================================================================
			// Aggiorno Posizione Giuridica Presente e ne Inserisco una NUOVA
			// ========================================================================

			// -----------------------------------------------------------------------------------------------------
			// 18-02-2015 - Non viene pia' modificata POS GIU e viene legato l'istituto alla Misura di
			// sicurezza
			// -----------------------------------------------------------------------------------------------------

			/*
			 * lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			 *
			 * lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			 * PosizioneGiuridicaModel lPosModCorr = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();
			 * BigDecimal lIdPosizioneGiuridica = null;
			 *
			 * // (per il momento NON SI AGGIORNA la POS GIU per decisione di M.T. del 15-12-2014 if
			 * (lPosModCorr != null && lPosModCorr.getIdPosizioneGiuridica() != null ) { lIdPosizioneGiuridica
			 * = lPosModCorr.getIdPosizioneGiuridica(); }
			 *
			 * String lCodPosizione = ""; lPosDao = new PosizioneGiuridicaDAO(lConn); if (lPosModCorr != null
			 * && lPosModCorr.getDataFine() == null) {
			 * lPosDao.setDataFine(lEveVer.getEvento().getDataEmissione());
			 * lPosDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			 * lPosDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			 * lPosDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			 * lPosDao.setCondizioneUpdate(lPosModCorr.getIdPosizioneGiuridica()); lCodPosizione =
			 * lPosModCorr.getCodPosizioneGiuridica(); lPosDao.update(); lPosDao.stop(); } if (lCodPosizione
			 * != null) { lPosDao.setCodPosizioneGiuridica(lCodPosizione);
			 * lPosDao.setDataInizio(lEveVer.getEvento().getDataEmissione());
			 * lPosDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			 * lPosDao.setDataInserimento(aEvento.getDataAggiornamento());
			 * lPosDao.setCodPosizioneProcessuale("-");
			 * lPosDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			 * lPosDao.setFasSieIdFascicoloSiep(lEveVer.getEvento().getFasSieIdFascicoloSiep());
			 * lPosDao.setIdEventoRiferimento(aEvento.getIdEvento());
			 *
			 * lIdPosizioneGiuridica = lPosDao.insert(); lPosDao.stop(); }
			 *
			 * //======================================================================== // Aggiorno il
			 * Luogo_Detenzione //========================================================================
			 *
			 * ILuogoDetenzione lCtrlLD = SIEPLookupRemote.getLuogoDetenzioneRemote();
			 *
			 * lLuoMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			 * lLuoMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			 * lLuoMod.setDataInserimento(aEvento.getDataAggiornamento());
			 * lLuoMod.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
			 * lLuoMod.setIstDetIdIstitutoDetenzione(lEveVer.getVerbale().getIstDetIdIstitutoDetenzione());
			 * lLuoMod.setPosGiuIdPosizioneGiuridica(lIdPosizioneGiuridica);
			 *
			 * lLuoDao = new LuogoDetenzioneDAO(lConn); lLuoDao.setDAOFromModel(lLuoMod); BigDecimal lKey =
			 * null; lKey = lLuoDao.insert(); lLuoDao.stop();
			 */

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			commit(lConn);
		} catch (F3BException fex) {
			rollback(lConn);
			fex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("-----------> F3BException: " + fex, fex);
			throw new F3BException(
					"MisuraSicurezzaController.ExUpdateValidaAnnotazioneDesignazioneIst: " + fex);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex, ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(
					"MisuraSicurezzaController.ExUpdateValidaAnnotazioneDesignazioneIst : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lEveDaoBlob);
			// cleanup(lLuoDao);
			// cleanup(lPosDao);
			// cleanup(lPosSqlDAO);
			cleanup(lMisDao);

			cleanup(lConn);
		}

		return lEveMod;
	}

	public BigDecimal ExInserisciProcedimentoMisuraProvvisoriaeoFuoriSenteneza(SentenzaModel aSenMod,
			SoggettoModel aSogMod, MisuraSicurezzaModel aMisMod, FascicoloSiepModel aFascMod, String aTipo,
			BigDecimal aIdOrd, BigDecimal aIdFascSius) throws F3BException {

		Connection lConn = null;
		SentenzaDAO lSenDao = null;
		SoggettoDAO lSogDao = null;
		MisuraSicurezzaDAO lMisDao = null;
		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasDaoSql = null;
		StatoProcedimentoDAO lStatoDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;
		DepositoOrdinanzaPcDAO lDepoDao = null;
		FascicoloGPSqlDAO lFasSiusDAO = null;
		FascicoloSiusDAO lFDAO = null;
		NoteDAO lNoteDao = null;
		// MEV_39
		FascMsToFascSiepDAO lFascMsToFascSiepDao = null;
		EventoSqlDAO lEveDao = null;

		BigDecimal lKeyFascicolo = null;
		try {
			lConn = getDBTransaction();
			lSenDao = new SentenzaDAO(lConn);
			lSogDao = new SoggettoDAO(lConn);
			lMisDao = new MisuraSicurezzaDAO(lConn);
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDaoSql = new FascicoloSiepSqlDAO(lConn);
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);

			lDepoDao = new DepositoOrdinanzaPcDAO(lConn);
			lFasSiusDAO = new FascicoloGPSqlDAO(lConn);
			lFDAO = new FascicoloSiusDAO(lConn);
			lNoteDao = new NoteDAO(lConn);

			// Sentenza
			BigDecimal lKeySentenza = null;
			if (aSenMod != null) {
				lSenDao.setDAOFromModel(aSenMod);
				lKeySentenza = lSenDao.insert();
				lSenDao.stop();
			}

			// Soggetto
			BigDecimal lKeySoggetto = null;
			if (aSogMod != null) {
				lSogDao.setDAOFromModel(aSogMod);
				lKeySoggetto = lSogDao.insert();
				lSogDao.stop();
			}

			// Fascicolo
			if (aFascMod != null) {
				// Cerco il Progressivo rispettivamente al tipo progressivo impostato
				// solo se la classe e' impostata
				lFasDaoSql.getProgressivoFascicoloSiep(aFascMod);
				lFasDaoSql.start();
				int lMaxProgr = 0;

				if (lFasDaoSql.next() && (lFasDaoSql.getInt("aMAX") > 0))
					lMaxProgr = lFasDaoSql.getInt("aMAX");

				lFasDaoSql.stop();

				if (aFascMod.getChiaveProgr() != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"Assegnazione manuale: verifica che il progressivo non sia gia' a sistema");

					lFasDaoSql.ricercaFascicoloByProgrAnnoCodUfficio(aFascMod);
					lFasDaoSql.start();
					if (lFasDaoSql.next()) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Il fascicolo " + aFascMod.getChiaveAnno() + "/"
								+ aFascMod.getChiaveProgr() + " e' gia' presente a sistema");
						throw new SIEPException(F3BException.USER_MESSAGE,
								"Il fascicolo " + aFascMod.getChiaveAnno() + "/" + aFascMod.getChiaveProgr()
										+ " e' gia' presente a sistema");
					}

					if (aFascMod.getChiaveAnno().intValue() == Integer
							.parseInt(DateUtils.getSysDate("yyyy"))) {
						if (aFascMod.getChiaveProgr().intValue() > lMaxProgr && lMaxProgr > 0) {
							throw new SIEPException(F3BException.USER_MESSAGE,
									"Impossibile inserire il fascicolo! Il numero del procedimento e' superiore all'ultimo assegnato dal sistema");
						}
					}
				} else {
					// Setto la ChiaveProgressivo del Model con il MAX + 1 a seconda del tipo...
					int lTipoProgr = aFascMod.getTipoProgressivo();
					if (lMaxProgr == 0) {
						aFascMod.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
					} else {
						aFascMod.setChiaveProgr(new BigDecimal(lMaxProgr + 1));
					}
				}

				if (lKeySoggetto != null)
					aFascMod.setSogIdSoggetto(lKeySoggetto);

				if (lKeySentenza != null)
					aFascMod.setSenIdSentenza(lKeySentenza);

				lFasDao.setDAOFromModel(aFascMod);
				lKeyFascicolo = lFasDao.insert();
				lFasDao.stop();
			}

			// SOLO per ISCR. MISURA FUORI SENTENZA: Marca Ordinanza e lega con Fascicolo SIUS
			if (aTipo.compareTo("FUORI_SENTENZA") == 0 && aIdOrd != null && aIdFascSius != null) {
				// Ordinanza
				IDepositoOrdinanzaPc CtrlDepo = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				DepositoOrdinanzaPcModel lDepoMod = new DepositoOrdinanzaPcModel();
				lDepoMod = CtrlDepo.ExRicercaDepositoOrdinanzaPcByKey(aIdOrd);
				if (lDepoMod != null && lDepoMod.getIdDepositoOrdinanzaPc() != null) {
					lDepoMod.setFlagElaborato("S");
				} else {
					throw new SIEPException(F3BException.USER_MESSAGE,
							"Ordinanza da marcare NON trovata (FLAG_ELABORATO = 'S'");
				}

				lDepoDao.setDAOFromModelForUpdate(lDepoMod);
				lDepoDao.update();
				lDepoDao.stop();

				// ricerca Fascicolo SIUS
				FascicoloGPModel lFasGPMod = new FascicoloGPModel();
				lFasSiusDAO.ricercaFascicoloByKey(aIdFascSius);
				lFasGPMod = (FascicoloGPModel) lFasSiusDAO.getModelByKey();

				if (lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null
						&& lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() != null) {

					lFasGPMod.getFascicoloSiusModel().setFasSieIdFascicoloSiep(lKeyFascicolo);
					lFasGPMod.getFascicoloSiusModel()
							.setCodUfficioAggiornamento(aFascMod.getCodUfficioInserimento());
					lFasGPMod.getFascicoloSiusModel()
							.setCodOperatoreAggiornamento(aFascMod.getCodOperatoreInserimento());
					lFasGPMod.getFascicoloSiusModel().setDataAggiornamento(aFascMod.getDataInserimento());
				} else {
					throw new SIEPException(F3BException.USER_MESSAGE, "Fascicolo_SIUS NON trovato ");
				}

				// MEV_39 INZIO
				// =====================================================
				// Collego il fascicolo di Origine DI classe I al Nuovo fascicolo
				// =====================================================
				BigDecimal fascSiepClasseI = null;

				if (lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null
						&& lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() != null) {

					BigDecimal idSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

					fascSiepClasseI = lFasSiusDAO.ricercaFasciIdFascSIEP(idSius);

					// 09/04/2018 [EC]
					// VERIFICARE SE PER IL FASCICOLO DI CLASSE IV NATO DA PROCEDIMENTO DI FUORI SENTENZA
					// DEVE ESSERE PREVISTO IL COLLEGAMENTO CON IL PROCEDIMENTO DI CLASSE I DA CUI è NATO IL
					// PROCEDIMENTO SIUS DI FUORI SENTENZA. se si occorre inserire il collegamento nella
					// tabella FASC_MS_TO_FASC_SIEP (OK FATTO!)

					// SE NON VA FATTO ELIMINARE IL PEZZO SOTTOSTANTE! (PER if(fascSiepClasseI != null))
				}
				if (fascSiepClasseI != null) {
					lFasDaoSql.ricercaFascicoloByKey(fascSiepClasseI);
					FascicoloSiepModel lFasModClasseI = (FascicoloSiepModel) lFasDaoSql.getModelByKey();

					// lFasDaoSql.ricercaFascicoloByKey(lKeyFascicolo);
					// FascicoloSiepModel lFasModClasseIV = (FascicoloSiepModel) lFasDaoSql.getModelByKey();

					siesLogger.debug("Collego il fascicolo di Origine al Nuovo fascicolo");
					FascMsToFascSiepModel lFascMsToFascSiepModel = new FascMsToFascSiepModel();
					lFascMsToFascSiepModel.setFasSieIdFascicoloSiep(lKeyFascicolo);
					lFascMsToFascSiepModel.setChiaveAnnoSiep(aFascMod.getChiaveAnno());
					lFascMsToFascSiepModel.setChiaveProgrSiep(aFascMod.getChiaveProgr());
					lFascMsToFascSiepModel.setChiaveUfficioSiep(aFascMod.getChiaveUfficio());
					// errore segnalato post collaudo 11.3
					lFascMsToFascSiepModel.setCodTipoRelazioneMS(
							ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_IN_ESECUZIONE_DI);

					// Fascicolo collegato, quello da cui nasce il fascicolo di esecuzione MS
					lFascMsToFascSiepModel.setFasSieIdFascicoloCollegato(lFasModClasseI.getIdFascicoloSiep());
					lFascMsToFascSiepModel.setChiaveAnnoSiepCollegato(lFasModClasseI.getChiaveAnno());
					lFascMsToFascSiepModel.setChiaveProgrSiepCollegato(lFasModClasseI.getChiaveProgr());
					lFascMsToFascSiepModel.setChiaveUfficioSiepCollegato(lFasModClasseI.getChiaveUfficio());

					lFascMsToFascSiepModel.setCodOperatoreInserimento(aFascMod.getCodOperatoreInserimento());
					lFascMsToFascSiepModel.setDataInserimento(aFascMod.getDataInserimento());
					lFascMsToFascSiepModel.setCodUfficioInserimento(aFascMod.getCodUfficioInserimento());

					lFascMsToFascSiepModel.setCodOperatoreAggiornamento(null);
					lFascMsToFascSiepModel.setDataAggiornamento(null);
					lFascMsToFascSiepModel.setCodUfficioAggiornamento(null);

					lFascMsToFascSiepDao = new FascMsToFascSiepDAO(lConn);
					lFascMsToFascSiepDao.setDAOFromModel(lFascMsToFascSiepModel);
					lFascMsToFascSiepDao.insert();
					lFascMsToFascSiepDao.stop();

					// mev 39: intervento post collaudo 11.3
					// devo creare il collegato anche sul classe I (collegatmento tra I e IV oltre che IV e I)
					lFascMsToFascSiepModel.setFasSieIdFascicoloSiep(fascSiepClasseI);
					lFascMsToFascSiepModel.setChiaveAnnoSiep(lFasModClasseI.getChiaveAnno());
					lFascMsToFascSiepModel.setChiaveProgrSiep(lFasModClasseI.getChiaveProgr());
					lFascMsToFascSiepModel.setChiaveUfficioSiep(lFasModClasseI.getChiaveUfficio());
					lFascMsToFascSiepModel.setCodTipoRelazioneMS(
							ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_ISCRITTO_AL);
					// dati del classe IV
					lFascMsToFascSiepModel.setFasSieIdFascicoloCollegato(lKeyFascicolo);
					lFascMsToFascSiepModel.setChiaveAnnoSiepCollegato(aFascMod.getChiaveAnno());
					lFascMsToFascSiepModel.setChiaveProgrSiepCollegato(aFascMod.getChiaveProgr());
					lFascMsToFascSiepModel.setChiaveUfficioSiepCollegato(aFascMod.getChiaveUfficio());
					// fine dati classe IV
					lFascMsToFascSiepDao = new FascMsToFascSiepDAO(lConn);
					lFascMsToFascSiepDao.setDAOFromModel(lFascMsToFascSiepModel);
					lFascMsToFascSiepDao.insert();
					lFascMsToFascSiepDao.stop();

					// mev 39 : le fuori sentenza agganciate ad un classe I devono andare in scadenziario
					// tenendo conto della Pena Residua del classe I

					DettaglioFascicoloModel aDettaglioFasOrig = null;
					IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
					aDettaglioFasOrig = lCtrlFas.ExDettaglioFascicoloSiep(fascSiepClasseI);
					PosizioneGiuridicaModel lPosMod = aDettaglioFasOrig.getPosizioneGiuridica();
					ScadenzarioDAO lScaDao = new ScadenzarioDAO(lConn);
					if (aDettaglioFasOrig.getPenaResidua() != null
							&& lPosMod.getCodPosizioneGiuridica() != null) {
						PenaResiduaModel lPenResModel = aDettaglioFasOrig.getPenaResidua();
						if (lPenResModel != null && lPenResModel.getFlagValidato() != null
								&& "S".equals(lPenResModel.getFlagValidato())
								&& lPenResModel.getDataFine() != null) {
							String[] posizioneGiuridica = { "07", "10", "16", "17", "20", "26", "30", "46",
									"47" };
							// inoltre si prosegue con l'inserimento in scadenzario solo per determinati
							// codici della
							// posizione giuridica (diversi da quelli nell'array)
							// (ho visto lo stesso controllo previsto alla riga 4096 della classe
							// OrdineEsecuzioneController)
							if (Arrays.binarySearch(posizioneGiuridica,
									lPosMod.getCodPosizioneGiuridica()) < 0) {
								if (lPenResModel.getDataInizio() != null) {
									ScadenzarioModel lScaMod = new ScadenzarioModel();
									// inserisce scadenzario
									lScaMod.setCodTipoScadenzario("20"); // Inizio_Misura
									lScaMod.setDataInizioScadenza(lPenResModel.getDataInizio());
									lScaMod.setDataFineScadenza(lPenResModel.getDataFine());
									// devo inserire idFAsc di classe IV
									lScaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
									lScaMod.setCodOperatoreInserimento(aFascMod.getCodOperatoreInserimento());
									lScaMod.setDataInserimento(DateUtils.getSysDate());
									lScaMod.setCodUfficioInserimento(aFascMod.getCodUfficioInserimento());
									lScaMod.setIdFascicoloSiepOrigine(
											aDettaglioFasOrig.getFascicoloSiep().getIdFascicoloSiep()); // fascicolo
																										// di
																										// CLasse
																										// I
									lScaDao.setDAOFromModel(lScaMod);
									lScaDao.insert();
									lScaDao.stop();
								}
							}
						}
					} // se non ho la pena residua???FIXME
					else {
						// si procede con l'inserimento in scadenzario_siep di un record con
						// tipo_Scadenzario=20
						// ma i campi DATA_INIZIO_SCADENZA e DATA_FINE_SCADENZA non posso recuperarli dalla
						// PenaResidua, pertanto in dataInizioScadenza inserisco sysdate e data_fine_scadenza
						// null
						ScadenzarioModel lScaMod = new ScadenzarioModel();
						// inserisce scadenzario
						lScaMod.setCodTipoScadenzario("20"); // Inizio_Misura
						lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
						// devo inserire idFAsc di classe IV
						lScaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lScaMod.setCodOperatoreInserimento(aFascMod.getCodOperatoreInserimento());
						lScaMod.setDataInserimento(DateUtils.getSysDate());
						lScaMod.setCodUfficioInserimento(aFascMod.getCodUfficioInserimento());
						// fascicolo di CLasse I
						lScaMod.setIdFascicoloSiepOrigine(fascSiepClasseI);
						lScaDao.setDAOFromModel(lScaMod);
						lScaDao.insert();
						lScaDao.stop();
					}
					///////////////////////////////////////////////////////////////////////////////////////////////////////////

					aMisMod.setFasSieIdFascicoloSiepRif(fascSiepClasseI);
				}
				// MEV_39 FINE

				// 20190606 [SG]: codice commentato e portato fuori dall'IF
				// // Misura Sicurezza
				// if (lKeyFascicolo != null)
				// aMisMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
				// if (lKeySentenza != null)
				// aMisMod.setSenIdSentenza(lKeySentenza);
				// lMisDao.setDAOFromModel(aMisMod);
				// // BigDecimal lKeyMis = null;
				// /* lKeyMis = */lMisDao.insert();
				// lMisDao.stop();

				lFDAO.setDAOFromModelForAssegnaTitoloEsecutivo(lFasGPMod.getFascicoloSiusModel());
				lFDAO.update();
				lFDAO.stop();

				// NOTE (legame titolo esecutivo,Nota di Inserimento Titolo Esecutivo.)
				NoteModel lNoteMod = new NoteModel();
				// BigDecimal lKeyRes = null;
				lNoteMod.setData(DateUtils.getSysDate());
				lNoteMod.setDescrizione("INSERITO TITOLO ESECUTIVO " + aFascMod.getChiaveAnno() + "/"
						+ aFascMod.getChiaveProgr() + " " + aSenMod.getDescrUfficioInserimento()
						+ " a seguito di Iscrizione Procedimento Applicazione Misura di Sicurezza Disposta Fuori Sentenza");

				lNoteMod.setCodOperatoreInserimento(aFascMod.getCodOperatoreInserimento());
				lNoteMod.setDataInserimento(aFascMod.getDataInserimento());
				lNoteMod.setCodUfficioInserimento(aFascMod.getCodUfficioInserimento());
				lNoteMod.setFasSiuIdFascicoloSius(aIdFascSius);
				lNoteMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
				lNoteDao.setDAOFromModel(lNoteMod);
				/* lKeyRes = */lNoteDao.insert();

				// ANOMALIA SEGNALATA DA MICHELE IN FASE DI TEST SULLA 11.3 (NON APPAIONO IL PROC. DI
				// SORVEGLIANZA SUI PROC. NATI FUORI SENTENZA)
				// legare anche fascicolo IV e fascicolo sius sulla tabella evento
				if (fascSiepClasseI != null) {
					// devo recuperare l'evento relativo al siep di classe I e al sius collegato e spostarlo
					// su siep classe IV
					Vector lEventi = new Vector();
					lEveDao = new EventoSqlDAO(lConn);
					EventoModel evM = new EventoModel();
					evM.setFasSieIdFascicoloSiep(fascSiepClasseI);
					evM.setFasSiuIdFascicoloSius(aIdFascSius);
					lEveDao.ricercaEvento(evM);
					lEventi = new Vector(lEveDao.getModels());
					if (lEventi.size() == 1) {
						EventoModel ee = (EventoModel) lEventi.get(0);
						EventoDAO lEveDaoU = new EventoDAO(lConn);
						lEveDaoU.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lEveDaoU.selCondizioneUpdate(ee.getIdEvento());
						lEveDaoU.update();
					}
				}
			} // Chiude if(aTipo.compareTo("FUORI_SENTENZA")==0

			// 20190606 [SG]: codice portato fuori dall'IF poichè deve valere sempre
			// anche per ISCR. MISURA FUORI SENTENZA dove viene valorizzato:
			// aMisMod.setFasSieIdFascicoloSiepRif(fascSiepClasseI);
			// Misura Sicurezza
			if (lKeyFascicolo != null)
				aMisMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
			if (lKeySentenza != null)
				aMisMod.setSenIdSentenza(lKeySentenza);
			lMisDao.setDAOFromModel(aMisMod);
			lMisDao.insert();
			lMisDao.stop();

			// STATO_PROCEDIMENTO
			// ========================================================================
			// Fascicolo nasce ora, quindi metto cod = 0108 e progressivo = 1
			// ========================================================================
			StatoProcedimentoModel lProcModel = new StatoProcedimentoModel();

			lProcModel.setCodStatoProcedimento("0108");
			lProcModel.setProgressivo(new BigDecimal(1));

			lProcModel.setCodOperatoreInserimento(aFascMod.getCodOperatoreInserimento());
			lProcModel.setDataInserimento(aFascMod.getDataInserimento());
			lProcModel.setCodUfficioInserimento(aFascMod.getCodUfficioInserimento());
			lProcModel.setFasSieIdFascicoloSiep(lKeyFascicolo);

			lStatoDao.setDAOFromModel(lProcModel);
			lStatoDao.insert();
			lStatoDao.stop();

			commit(lConn);
		} catch (SIEPException fex) {
			rollback(lConn);
			fex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("-----------> F3BException: " + fex, fex);
			throw fex;
		} catch (F3BException fex) {
			rollback(lConn);
			fex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("-----------> F3BException: " + fex, fex);
			throw new F3BException(
					"MisuraSicurezzaController.ExInserisciProcedimentoMisuraProvvisoriaeoFuoriSenteneza: "
							+ fex);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex, ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(
					"MisuraSicurezzaController.ExInserisciProcedimentoMisuraProvvisoriaeoFuoriSenteneza : "
							+ ex);
		} finally {
			cleanup(lSenDao);
			cleanup(lSogDao);
			cleanup(lMisDao);
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lStatoDao);
			cleanup(lStatoSqlDao);
			cleanup(lDepoDao);
			cleanup(lFasSiusDAO);
			cleanup(lFDAO);
			cleanup(lNoteDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFascMsToFascSiepDao);
			cleanup(lEveDao);

			cleanup(lConn);
		}
		return lKeyFascicolo;
	} // CHIUDE ExInserisciProcedimentoMisuraProvvisoriaeoFuoriSenteneza()

	public Vector ExRicercaMisuraSicurezzaByMisIdMisuraSicurezza(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MisuraSicurezzaSqlDAO lMisDao = null;
		Vector lMisVec = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDao.ricercaMisuraSicurezzaByKeyMisuraCollegata(aKey);
			lMisVec = new Vector(lMisDao.getModels());
		} catch (F3BException fex) {
			fex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("-----------> F3BException: " + fex, fex);
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaByMisIdMisuraSicurezza: Non posso leggere : "
							+ fex);
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("----------> DAOException: " + daoEx, daoEx);
			throw new F3BException(
					"MisuraSicurezzaController.ExRicercaMisuraSicurezzaByMisIdMisuraSicurezza: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
		return lMisVec;
	}

	/**
	 * Ricerca il CodiceTipoMisura
	 *
	 * @param aIdEvento
	 * @throws F3BException
	 */
	public String ExRicercaCodTipoMisuraByIdEvento(BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;
		MisuraSicurezzaSqlDAO lMSSqlDao = null;
		String lCodTipoMisura = null;

		try {
			lConn = getDBConnection();
			lMSSqlDao = new MisuraSicurezzaSqlDAO(lConn);
			lMSSqlDao.ricercaCodTipoMisurabyIdEvento(aIdEvento);
			lMSSqlDao.start();

			if (lMSSqlDao.next())
				lCodTipoMisura = lMSSqlDao.getString("CODTIPOMISURA");

			lMSSqlDao.stop();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoex);
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"MisuraSicurezzaController.ExRicercaCodTipoMisuraByIdEvento: " + daoex);
		} finally {
			cleanup(lMSSqlDao);
			cleanup(lConn);
		}

		return lCodTipoMisura;
	}

	public boolean ExEsistonoFascicoliClasseIVAnno(UfficioModel aUfficio, BigDecimal aAnno)
			throws F3BException {

		boolean lEsisteFascicolo = true;

		Connection lConn = null;
		FascicoloSiepSqlDAO lFasSqlDao = null;

		FascicoloSiepModel lFascicoloSiep = new FascicoloSiepModel();

		lFascicoloSiep.setChiaveAnno(aAnno);
		lFascicoloSiep.setChiaveUfficio(aUfficio.getCodUfficio());
		lFascicoloSiep.setTipoProgressivo(4);

		try {
			lConn = getDBConnection();

			lFasSqlDao = new FascicoloSiepSqlDAO(lConn);

			lFasSqlDao.getProgressivoFascicoloSiep(lFascicoloSiep);
			lFasSqlDao.start();
			// int lMaxProgr = 0;

			if (lFasSqlDao.next() && (lFasSqlDao.getInt("aMAX") > 0))
				lEsisteFascicolo = true;
			else
				lEsisteFascicolo = false;

			lFasSqlDao.stop();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoex);
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"MisuraSicurezzaController.ExEsistonoFascicoliClasseIVAnno: " + daoex);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"MisuraSicurezzaController.ExEsistonoFascicoliClasseIVAnno: " + ex);
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lConn);
		}
		return lEsisteFascicolo;
	}

	/**
	 * Maggio 2015 Aggiorna Evento, Cancella e Reinserisce le notifiche
	 *
	 * @param EventoNotificaModel
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExAggiornaEventoNotificheXComunicazioneMS(EventoNotificaModel aEvento)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDaoDel = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		// MEV_39: aggiunte variabili
		CampoNotaDAO campoNotaDaoDel = null;
		CampoNotaDAO campoNotaDao = null;

		EventoModel lEveMod = null;
		EventoNotificaModel lRetModel = new EventoNotificaModel();

		try {
			lConn = getDBTransaction();
			// EVENTO
			lEveDao = new EventoDAO(lConn);
			lEveMod = new EventoModel(aEvento.getEvento());

			lEveDao.setDAOFromModelForUpdate(lEveMod);
			lEveDao.setIdEvento(lEveMod.getIdEvento());

			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// NOTIFICHE
			BigDecimal lKeyEvento = lEveMod.getIdEvento();
			lRetModel.getEvento().setIdEvento(lKeyEvento);

			// *********** Cancella le NOTIFICHE associate al EVENTO ******************
			lNotDaoDel = new NotificaDAO(lConn);
			lNotDaoDel.setCondizioneEvento(lKeyEvento);
			lNotDaoDel.delete();
			lNotDaoDel.stop();

			// *********** Inserisce le NOTIFICHE NUOVE associate al EVENTO ******************
			BigDecimal lKeyAutorita = null;
			int count = 0;
			while (count < aEvento.getNotifiche().length) {
				if (aEvento.getNotifiche()[count] != null) {
					if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao = new AutoritaEsternaDAO(lConn);
						lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();
						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					lNotDao = new NotificaDAO(lConn);
					aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);
					lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// MEV_39: Aggiornamento delle eventuali note aggiuntive
			// prima cancello, poi inserisco
			campoNotaDaoDel = new CampoNotaDAO(lConn);
			campoNotaDaoDel.setCondizioneEvento(lKeyEvento);
			campoNotaDaoDel.delete();
			campoNotaDaoDel.stop();
			if (aEvento.getCampoNote() != null) {
				count = 0;
				campoNotaDao = new CampoNotaDAO(lConn);
				while (count < aEvento.getCampoNote().length) {
					aEvento.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					campoNotaDao.setDAOFromModel(aEvento.getCampoNote()[count]);
					campoNotaDao.insert();
					campoNotaDao.stop();
					count++;
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"MisuraSicurezzaController.ExAggiornaEventoNotificheXComunicazioneMS: Non posso inserire: "
							+ ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("MisuraSicurezzaController.ExAggiornaEventoNotificheXComunicazioneMS: ", ex);
			rollback(lConn);
			throw new F3BException(
					"MisuraSicurezzaController.ExAggiornaEventoNotificheXComunicazioneMS: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lNotDaoDel);
			cleanup(lAutDao);
			// MEV_39: aggiunte variabili
			cleanup(campoNotaDaoDel);
			cleanup(campoNotaDao);
			cleanup(lConn);
		}
		return lRetModel;
	} // CHIUDE ExAggiornaEventoNotificheXComunicazioneMS()

	/**
	 * Giugno 2015 Aggiorna Evento, Cancella e Reinserisce le notifiche
	 *
	 * @param EventoNotificaModel
	 * @param ArchiviazioneModel
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExAggiornaEventoNotificheXArchiviazioneMS(EventoNotificaModel aEvento,
			ArchiviazioneModel lArcMod) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDaoDel = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		ArchiviazioneDAO lArcDao = null;

		EventoModel lEveMod = null;
		EventoNotificaModel lRetModel = new EventoNotificaModel();

		try {
			lConn = getDBTransaction();
			// EVENTO
			lEveDao = new EventoDAO(lConn);
			lEveMod = new EventoModel(aEvento.getEvento());

			lEveDao.setDAOFromModelForUpdate(lEveMod);

			// 13-11-2015 mancano nella 'setDAOFromModelForUpdate'
			lEveDao.setAnnoProtocollo(lEveMod.getAnnoProtocollo());
			lEveDao.setProgrProtocollo(lEveMod.getProgrProtocollo());
			//
			lEveDao.setIdEvento(lEveMod.getIdEvento());

			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// ARCHIVIAZIONE
			lArcDao = new ArchiviazioneDAO(lConn);

			lArcDao.setDAOFromModelForUpdate(lArcMod);
			// lArcDao.selByKey();
			lArcDao.update();
			lArcDao.stop();

			// NOTIFICHE
			BigDecimal lKeyEvento = lEveMod.getIdEvento();
			lRetModel.getEvento().setIdEvento(lKeyEvento);

			// *********** Cancella le NOTIFICHE associate al EVENTO ******************
			lNotDaoDel = new NotificaDAO(lConn);

			lNotDaoDel.setCondizioneEvento(lKeyEvento);
			lNotDaoDel.delete();
			lNotDaoDel.stop();

			// *********** Inserisce le NOTIFICHE NUOVE associate al EVENTO ******************

			BigDecimal lKeyAutorita = null;
			int count = 0;

			while (count < aEvento.getNotifiche().length) {
				if (aEvento.getNotifiche()[count] != null) {
					if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao = new AutoritaEsternaDAO(lConn);

						lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					lNotDao = new NotificaDAO(lConn);

					aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"MisuraSicurezzaController.ExAggiornaEventoNotificheXArchiviazioneMS: Non posso inserire: "
							+ ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("MisuraSicurezzaController.ExAggiornaEventoNotificheXArchiviazioneMS: ", ex);
			rollback(lConn);
			throw new F3BException(
					"MisuraSicurezzaController.ExAggiornaEventoNotificheXArchiviazioneMS: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lNotDaoDel);
			cleanup(lAutDao);
			cleanup(lArcDao);
			cleanup(lConn);
		}
		return lRetModel;
	} // CHIUDE ExAggiornaEventoNotificheXArchiviazioneMS()

	/**
	 * Giugno 2015 Aggiorna evento, CampoNota
	 *
	 * @param EventoNotificaModel
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExAggiornaEventoCampoNotaXRichiestaDapMS(EventoNotificaModel aEvento,
			String aDescr) throws F3BException {

		Connection lConn = null;
		CampoNotaSqlDAO lCamSqlDao = null;
		CampoNotaModel lCamMod = null;

		EventoDAO lEveDao = null;
		CampoNotaDAO lCmpDao = null;

		EventoModel lEveMod = null;
		EventoNotificaModel lRetModel = new EventoNotificaModel();

		try {
			lConn = getDBTransaction();
			// Vedo Se esiste il CampoNota
			lCamSqlDao = new CampoNotaSqlDAO(lConn);

			lCamSqlDao.ricercaCampoNotaByKeyEvento(aEvento.getEvento().getIdEvento());
			lCamMod = (CampoNotaModel) lCamSqlDao.getModelByKey();
			// EVENTO
			lEveDao = new EventoDAO(lConn);
			lEveMod = new EventoModel(aEvento.getEvento());

			lEveDao.setDAOFromModelForUpdate(lEveMod);
			lEveDao.setIdEvento(lEveMod.getIdEvento());

			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			BigDecimal lKeyEvento = lEveMod.getIdEvento();
			lRetModel.getEvento().setIdEvento(lKeyEvento);

			// CAMPONOTA
			lCmpDao = new CampoNotaDAO(lConn);
			if (lCamMod != null && lCamMod.getIdCampoNota() != null) {
				if (aDescr.compareTo("") != 0) {
					// UPDATE
					lCamMod.setCodOperatoreAggiornamento(aEvento.getEvento().getCodOperatoreAggiornamento());
					lCamMod.setCodUfficioAggiornamento(aEvento.getEvento().getCodUfficioAggiornamento());
					lCamMod.setDataAggiornamento(DateUtils.getSysDate());
					lCamMod.setDescr(aDescr);

					lCmpDao.setDAOFromModelForUpdate(lCamMod);
					lCmpDao.update();
					lCmpDao.stop();
				} else {
					// DELETE
					lCmpDao.setDAOFromModelForUpdate(lCamMod);
					lCmpDao.delete();
					lCmpDao.stop();
				}
			} else {
				if (aDescr.compareTo("") != 0) {
					// INSERT
					CampoNotaModel lCampMod = new CampoNotaModel();
					lCampMod.setCodOperatoreInserimento(aEvento.getEvento().getCodOperatoreInserimento());
					lCampMod.setCodUfficioInserimento(aEvento.getEvento().getCodUfficioInserimento());
					lCampMod.setDataInserimento(DateUtils.getSysDate());
					lCampMod.setEveIdEvento(lKeyEvento);
					lCampMod.setDescr(aDescr);
					lCampMod.setFasSieIdFascicoloSiep(aEvento.getEvento().getFasSieIdFascicoloSiep());
					lCampMod.setProgressivo(new BigDecimal(1));

					lCmpDao.setDAOFromModel(lCampMod);
					lCmpDao.insert();
					lCmpDao.stop();
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"MisuraSicurezzaController.ExAggiornaEventoCampoNotaXRichiestaDapMS: Non posso inserire: "
							+ ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("MisuraSicurezzaController.ExAggiornaEventoCampoNotaXRichiestaDapMS: ", ex);
			rollback(lConn);
			throw new F3BException(
					"MisuraSicurezzaController.ExAggiornaEventoCampoNotaXRichiestaDapMS: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lCamSqlDao);
			cleanup(lCmpDao);
			cleanup(lConn);
		}

		return lRetModel;
	} // CHIUDE ExAggiornaEventoCampoNotaXRichiestaDapMS()

	/**
	 * Giugno 2015 Aggiorna evento e Verbale
	 *
	 * @param EventoModel
	 * @param VerbaleModel
	 * @return EventoModel
	 * @throws F3BException
	 */
	public EventoModel ExAggiornaEventoVerbaleXDesignazioneIstitutoMS(EventoModel aEvento,
			VerbaleModel aVerbale) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		VerbaleDAO lVerDao = null;
		EventoModel lEveMod = null;
		VerbaleModel lVerMod = null;

		try {
			lConn = getDBTransaction();
			// EVENTO
			lEveDao = new EventoDAO(lConn);
			lEveMod = new EventoModel(aEvento);

			lEveDao.setDAOFromModelForUpdate(lEveMod);
			// lEveDao.setIdEvento(lEveMod.getIdEvento());
			// lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// VERBALE
			lVerDao = new VerbaleDAO(lConn);
			lVerMod = new VerbaleModel(aVerbale);

			lVerDao.setDAOFromModelForUpdate(lVerMod);
			// lVerDao.selByKey();
			lVerDao.update();
			lVerDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"MisuraSicurezzaController.ExAggiornaEventoVerbaleXDesignazioneIstitutoMS: Non posso inserire: "
							+ ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("MisuraSicurezzaController.ExAggiornaEventoVerbaleXDesignazioneIstitutoMS: ",
					ex);
			rollback(lConn);
			throw new F3BException(
					"MisuraSicurezzaController.ExAggiornaEventoVerbaleXDesignazioneIstitutoMS: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lVerDao);
			cleanup(lConn);
		}

		return lEveMod;
	} // CHIUDE ExAggiornaEventoVerbaleXDesignazioneIstitutoMS()

	/**
	 * MEV_39: aggiunto metodo di ricerca
	 */
	public FascMsToFascSiepModel ExRicercaDatiFascColl(String codUfficioUtenteConnesso, String lId,
			BigDecimal keyFas) throws F3BException {

		Connection lConn = null;
		FascMsToFascSiepModel model = new FascMsToFascSiepModel();
		FascMsToFascSiepSqlDAO dao = null;

		try {
			lConn = getDBConnection();
			dao = new FascMsToFascSiepSqlDAO(lConn);
			dao.ricercaDatiFascColl(codUfficioUtenteConnesso, lId, keyFas);
			model = (FascMsToFascSiepModel) dao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("MisuraSicurezzaController.ExRicercaDatiFascColl : " + daoEx);
		} finally {
			cleanup(dao);
			cleanup(lConn);
		}

		// valore di ritorno
		return model;
	}

	/**
	 * MEV_39: aggiunto metodo di ricerca
	 */
	public List ExRicercaMSNotificateByIdFasc(BigDecimal idFascicoloSiep, String tipoRicerca,
			BigDecimal idEventoRestituzione) throws F3BException {

		Connection lConn = null;
		MisuraSicurezzaSqlDAO mssDAO = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		List l = new ArrayList();

		try {
			lConn = getDBConnection();
			mssDAO = new MisuraSicurezzaSqlDAO(lConn);
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			mssDAO.ricercaMSNotificateByIdFascIdEve(idFascicoloSiep, tipoRicerca, idEventoRestituzione);
			mssDAO.start();
			while (mssDAO.next()) {
				MisuraSicurezzaNotificataModel msnm = (MisuraSicurezzaNotificataModel) mssDAO
						.getModelMSNotificate();
				if (msnm != null) {
					if ("-".equals(msnm.getCodTipoAutorita()) && msnm.getIdIstitutoDetenzione() != null)
						msnm.setCodTipoAutorita(msnm.getIdIstitutoDetenzione());
					if ("-".equals(msnm.getDescrTipoAutorita()) && msnm.getIdIstitutoDetenzione() != null) {
						lIstDao.ricercaIstitutoDetenzioneByKey(msnm.getIdIstitutoDetenzione());
						IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
						msnm.setDescrTipoAutorita(
								"Istituto Detenzione - " + lIstituto.getDescrTipoIstituto());
					}
					// collaudo 11.3 aggiunto controllo che aggiungo se e solo se esiste autorita'
					if (!"-".equals(msnm.getCodTipoAutorita()))
						l.add(msnm);
				}
			}
			mssDAO.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("MisuraSicurezzaController.ExRicercaMSNotificateByIdFascIdEve : " + daoEx);
		} finally {
			cleanup(mssDAO);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lIstDao);
			cleanup(lConn);
		}

		// valore di ritorno
		return l;
	}

} // Chiude Controller