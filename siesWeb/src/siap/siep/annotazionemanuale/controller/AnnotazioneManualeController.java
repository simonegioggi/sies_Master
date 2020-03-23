package siap.siep.annotazionemanuale.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.dao.EventoStoreProcedurePulisciDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaSigeModel;
import siap.siep.annotazionemanuale.model.AnnotazioneReatoModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.dao.FungibilitaDAO;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.reato.model.ReatoModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoSqlDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.provvedimento.dao.ProvvedimentoSigeDAO;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;

/**
 * <p>
 * Title: AnnotazioneManualeController
 * </p>
 * <p>
 * Description: Classe Controller per AnnotazioneManuale
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
public class AnnotazioneManualeController extends SiapController implements IAnnotazioneManuale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisce l'evento di annotazione di Rideterminazione pena altro con i quantum di computo
	 * (annotazione_manuale) ed eventualmente il provvedimento altra autorità.
	 *
	 * @param aEvento
	 * @param aListaAnnotazioni
	 * @param aCampoNote
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExInserisciEventoAnnotazioni(EventoModel aEvento, Vector aListaAnnotazioni,
			CampoNotaModel aCampoNote, EventoModel aEveAltraAutorita,
			LicenzaLibAnticipataModel aLicLibAntModel) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDAO = null;
		AnnotazioneManualeDAO lAnnDao = null;
		LicenzaLibanticipataDAO lLicLibDao = null;
		CampoNotaDAO lCampoNoteDAO = null;

		EventoModel lEveModel = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// =====================================================
			// Inserisco/Aggancio il provvedimento altra Autorità
			// =====================================================
			if (aEveAltraAutorita != null) {
				if (aEveAltraAutorita.getIdEvento() == null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Provv Altra Autorità non a sistema lo inserisco");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserisco il provvedimento altra autorità...");
					lEveDAO = new EventoDAO(lConn);
					lEveDAO.setDAOFromModel(aEveAltraAutorita);
					BigDecimal lIdEventoAA = lEveDAO.insert();
					lEveDAO.stop();

					aEvento.setEveIdEvento(lIdEventoAA);
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Provv Altra Autorità già a sistema lo aggancia");
					aEvento.setEveIdEvento(aEveAltraAutorita.getIdEvento());
				}
			}

			// =====================================
			// Inserisco l'evento
			// =====================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco l'evento...");
			lEveDAO = new EventoDAO(lConn);
			lEveDAO.setDAOFromModel(aEvento);
			BigDecimal lIdEvento = lEveDAO.insert();
			lEveDAO.stop();

			lEveModel.setIdEvento(lIdEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("... evento inserito: id " + lIdEvento);

			// =====================================
			// Inserisco le annotazioni manuali
			// =====================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco le annotazioni...");
			lAnnDao = new AnnotazioneManualeDAO(lConn);
			for (int i = 0; i < aListaAnnotazioni.size(); i++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Annotazione " + i);
				AnnotazioneManualeModel lAnnoMod = (AnnotazioneManualeModel) aListaAnnotazioni.elementAt(i);
				lAnnoMod.setEveIdEvento(lIdEvento);

				lAnnDao.setDAOFromModel(lAnnoMod);
				lAnnDao.insert();
				lAnnDao.stop();
			}

			// =====================================
			// Inserisco le LA se presenti
			// =====================================
			if (aLicLibAntModel != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserisco le LA...");
				lLicLibDao = new LicenzaLibanticipataDAO(lConn);

				aLicLibAntModel.setEveIdEvento(lIdEvento);

				lLicLibDao.setDAOFromModel(aLicLibAntModel);
				lLicLibDao.insert();
				lLicLibDao.stop();
			}

			// =====================================
			// Inserisco il campo note
			// =====================================
			if (aCampoNote != null && aCampoNote.getDescr() != null && !aCampoNote.getDescr().equals("")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserisco il campo note....");
				aCampoNote.setEveIdEvento(lIdEvento);

				lCampoNoteDAO = new CampoNotaDAO(lConn);
				lCampoNoteDAO.setDAOFromModel(aCampoNote);
				lCampoNoteDAO.insert();
				lCampoNoteDAO.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("AnnotazioneManualeController.ExInserisciEventoAnnotazioni: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new F3BException("AnnotazioneManualeController.ExInserisciEventoAnnotazioni: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lEveDAO);
			cleanup(lCampoNoteDAO);
			cleanup(lLicLibDao);

			cleanup(lConn);
		}

		return lEveModel;
	}

	// AMBROSINO - Inserimento Avvenuto pagamento PP
	public EventoModel ExInserisciEventoAnnotazioniCampoNota(EventoModel aEvento, Vector aListaAnnotazioni,
			Vector aListaCampoNote, EventoModel aEveAltraAutorita) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDAO = null;
		AnnotazioneManualeDAO lAnnDao = null;
		CampoNotaDAO lCampoNoteDAO = null;

		EventoModel lEveModel = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// =====================================================
			// Inserisco/Aggancio il provvedimento altra Autorità
			// =====================================================
			if (aEveAltraAutorita != null) {
				if (aEveAltraAutorita.getIdEvento() == null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Provv Altra Autorità non a sistema lo Inserisco");
					lEveDAO = new EventoDAO(lConn);
					lEveDAO.setDAOFromModel(aEveAltraAutorita);
					BigDecimal lIdEventoAA = lEveDAO.insert();
					lEveDAO.stop();

					aEvento.setEveIdEvento(lIdEventoAA);
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Provv Altra Autorità già a sistema lo aggancia");
					aEvento.setEveIdEvento(aEveAltraAutorita.getIdEvento());
				}
			}

			// =====================================
			// Inserisco l'evento
			// =====================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco l'evento Computo Ufficio ...");
			lEveDAO = new EventoDAO(lConn);
			lEveDAO.setDAOFromModel(aEvento);
			BigDecimal lIdEvento = lEveDAO.insert();
			lEveDAO.stop();

			lEveModel.setIdEvento(lIdEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("... evento Computo inserito: id " + lIdEvento);

			// =====================================
			// Inserisco le annotazioni manuali
			// =====================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco le annotazioni...");
			lAnnDao = new AnnotazioneManualeDAO(lConn);
			for (int i = 0; i < aListaAnnotazioni.size(); i++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Annotazione " + i);
				AnnotazioneManualeModel lAnnoMod = (AnnotazioneManualeModel) aListaAnnotazioni.elementAt(i);
				lAnnoMod.setEveIdEvento(lIdEvento);

				lAnnDao.setDAOFromModel(lAnnoMod);
				lAnnDao.insert();
				lAnnDao.stop();
			}

			// =====================================
			// Inserisco il campo note
			// =====================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco il/i CampoNota..");
			for (int i = 0; i < aListaCampoNote.size(); i++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("CampoNota.... " + i);
				CampoNotaModel lCNoteMod = (CampoNotaModel) aListaCampoNote.elementAt(i);
				if (lCNoteMod != null && lCNoteMod.getDescr() != null && !lCNoteMod.getDescr().equals("")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserisco il campo note....");
					lCNoteMod.setEveIdEvento(lIdEvento);

					lCampoNoteDAO = new CampoNotaDAO(lConn);
					lCampoNoteDAO.setDAOFromModel(lCNoteMod);
					lCampoNoteDAO.insert();
					lCampoNoteDAO.stop();
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("AnnotazioneManualeController.ExInserisciEventoAnnotazioni: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new F3BException("AnnotazioneManualeController.ExInserisciEventoAnnotazioni: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lEveDAO);
			cleanup(lCampoNoteDAO);

			cleanup(lConn);
		}

		return lEveModel;
	}

	/**
	 * <p>
	 * Inserisce l'evento e l'annotazione manuale agganciandogliela. Invocata nel caso di inserimento:
	 * </p>
	 * Richieste e decisioni del GE<br>
	 * - depenalizazione<br>
	 * - incostituzionalità<br>
	 * - Amnistia/Indulto<br>
	 * Rideterminazione pena<br>
	 * - Presofferto e fungibilità<br>
	 *
	 * <p>
	 * !!! Aggancia all'evento anche tutte le annotazioni trovate a sistema per lo stesso fascicolo, non
	 * ancora validate, e dello stesso tipo di quella che si sta inserendo ma con FLAG_APP_PROVVISORIA='-' (?)
	 * <p>
	 *
	 * @param aAnnotazioneManuale
	 *            - annotazione
	 * @param aEvento
	 *            Evento della richiesta
	 * @return
	 */
	public AnnotazioneManualeModel ExInserisciAnnotazioneManualeEvento(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEvento) throws F3BException {

		return ExInserisciAnnotazioneManualeEvento(aAnnotazioneManuale, aEvento, null);
	}

	public AnnotazioneManualeModel ExInserisciAnnotazioneManualeEvento(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEvento,
			BigDecimal aIdAnnotazioneManuale) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeDAO lAnnDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDao = null;

		AnnotazioneManualeModel lAnnMod = null;
		EventoModel lEve = new EventoModel();

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Cerco se esiste un evento dello stesso tipo di quello passato passato
			// in input, ma non validato. Se esiste lo aggiorno, altrimenti ne inserisco
			// uno nuovo.
			// ========================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoTipoCodMotProvNonValidato(aEvento);

			lEve = (EventoModel) lEveSqlDao.getModelByKey();

			aEvento.setCodEsito("-");

			aEvento.setCodTipoUfficioDestinatario("-");
			aEvento.setCodLuogoDestinatario("-");
			if (aEvento.getCodMotivo().equals("0210") // Depenalizzazione
					|| aEvento.getCodMotivo().equals("0211") // Incostituzionalità
					|| aEvento.getCodMotivo().equals("0122") // Aministia/Indulto
			) {
				aEvento.setFlagStampaSiep("N");
				aEvento.setFlagVideoSiep("N");
			}

			BigDecimal lEveIdEvento = null;

			lEveDao = new EventoDAO(lConn);
			if (lEve != null) { // Evento già presente lo aggiorno
				aEvento.setIdEvento(lEve.getIdEvento());
				aEvento.setCodUfficioAggiornamento(aAnnotazioneManuale.getCodUfficioInserimento());
				aEvento.setCodOperatoreAggiornamento(aAnnotazioneManuale.getCodOperatoreInserimento());
				aEvento.setDataAggiornamento(aAnnotazioneManuale.getDataInserimento());

				lEveDao.setDAOFromModelForUpdate(aEvento);
				lEveDao.update();
				lEveDao.stop();

				lEveIdEvento = lEve.getIdEvento();
				aAnnotazioneManuale.setEveIdEvento(lEveIdEvento);
			} else { // L'evento non esiste lo inserisco
				aEvento.setCodUfficioInserimento(aAnnotazioneManuale.getCodUfficioInserimento());
				aEvento.setCodOperatoreInserimento(aAnnotazioneManuale.getCodOperatoreInserimento());
				aEvento.setDataInserimento(aAnnotazioneManuale.getDataInserimento());

				lEveDao.setDAOFromModel(aEvento);
				BigDecimal lKeyEvento = null;
				lKeyEvento = lEveDao.insert();
				lEveDao.stop();

				lEveIdEvento = lKeyEvento;
				aAnnotazioneManuale.setEveIdEvento(lEveIdEvento);
			}

			lAnnDao = new AnnotazioneManualeDAO(lConn);

			// Validazione dell'Annotazione di riferimento (Ordinanza GE)
			if (aIdAnnotazioneManuale != null) {
				lAnnDao.setCondizioneUpdate(aIdAnnotazioneManuale);
				lAnnDao.setFlagValidato("S");
				lAnnDao.setAnnoGe(aAnnotazioneManuale.getAnnoGe());
				lAnnDao.setNumeroGe(aAnnotazioneManuale.getNumeroGe());
				lAnnDao.setCodOperatoreAggiornamento(aAnnotazioneManuale.getCodOperatoreInserimento());
				lAnnDao.setCodUfficioAggiornamento(aAnnotazioneManuale.getCodUfficioInserimento());
				lAnnDao.setDataAggiornamento(aAnnotazioneManuale.getDataInserimento());

				lAnnDao.update();
				lAnnDao.stop();
			}

			// ========================================================================
			// Inserisco l'annotazione manuale agganciandola all'evento di richiesta
			// ========================================================================
			lAnnMod = new AnnotazioneManualeModel(aAnnotazioneManuale);

			// lAnnMod.setPenResIdPenaResidua(aPenaResidua.getIdPenaResidua());

			lAnnDao.setDAOFromModel(aAnnotazioneManuale);
			BigDecimal lKey = null;
			lKey = lAnnDao.insert();
			lAnnDao.stop();

			// Update su Annotazione Manuale di idEvento
			/*
			 * Aggancia tutte le annotazioni dello stesso tipo di quella che si sta inserendo , non validate,
			 * all'evento corrente. In questo modo se sono presenti più annotazioni (aggiungi) vengono
			 * agganciate ad un unico evento di richiesta , l'ultimo inserito. L'unico problema è la
			 * condizione su FLAG_APP_PROVVISORIA=-
			 *
			 * UPDATE ANNOTAZIONE_MANUALE SET EVE_ID_EVENTO=? WHERE FAS_SIE_ID_FASCICOLO_SIEP=? AND
			 * COD_TIPO_ANNOTAZIONE='?' AND FLAG_APP_PROVVISORIA='-' AND FLAG_VALIDATO='N'
			 */

			lAnnMod.setIdAnnotazioneManuale(lKey);

			lAnnDao.setCondizioneCodTipoNonValidataByIdFascicolo(
					aAnnotazioneManuale.getFasSieIdFascicoloSiep(),
					aAnnotazioneManuale.getCodTipoAnnotazione());
			lAnnDao.setEveIdEvento(lEveIdEvento);
			lAnnDao.update();
			lAnnDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("AnnotazioneManualeController.ExInserisciAnnotazioneManualeEvento: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new F3BException("AnnotazioneManualeController.ExInserisciAnnotazioneManualeEvento: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);

			cleanup(lConn);
		}

		return lAnnMod;
	}

	// MEV 9
	public AnnotazioneManualeModel ExInserisciAnnotazioneManualeEventoUpd(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEvento,
			BigDecimal aIdAnnotazioneRichiesta, BigDecimal aIdAnnotazioneManuale) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeDAO lAnnDao = null;
		AnnotazioneManualeDAO lAnnRichDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDao = null;

		AnnotazioneManualeModel lAnnMod = null;
		EventoModel lEve = new EventoModel();

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Cerco se esiste un evento dello stesso tipo di quello passato passato
			// in input, ma non validato. Se esiste lo aggiorno, altrimenti ne inserisco
			// uno nuovo.
			// ========================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoTipoCodMotProvNonValidato(aEvento);

			lEve = (EventoModel) lEveSqlDao.getModelByKey();

			aEvento.setCodTipoUfficioDestinatario("-");
			aEvento.setCodLuogoDestinatario("-");
			if (aEvento.getCodMotivo().equals("0210") // Depenalizzazione
					|| aEvento.getCodMotivo().equals("0211") // Incostituzionalità
					|| aEvento.getCodMotivo().equals("0122") // Aministia/Indulto
			) {
				aEvento.setFlagStampaSiep("N");
				aEvento.setFlagVideoSiep("N");
				aEvento.setCodEsito("-");
			}

			BigDecimal lEveIdEvento = null;

			lEveDao = new EventoDAO(lConn);
			if (lEve != null) { // Evento già presente lo aggiorno
				aEvento.setIdEvento(lEve.getIdEvento());
				aEvento.setCodUfficioAggiornamento(aAnnotazioneManuale.getCodUfficioInserimento());
				aEvento.setCodOperatoreAggiornamento(aAnnotazioneManuale.getCodOperatoreInserimento());
				aEvento.setDataAggiornamento(aAnnotazioneManuale.getDataInserimento());

				lEveDao.setDAOFromModelForUpdate(aEvento);
				lEveDao.update();
				lEveDao.stop();

				lEveIdEvento = lEve.getIdEvento();
				aAnnotazioneManuale.setEveIdEvento(lEveIdEvento);
			} else { // L'evento non esiste lo inserisco
				aEvento.setCodUfficioInserimento(aAnnotazioneManuale.getCodUfficioInserimento());
				aEvento.setCodOperatoreInserimento(aAnnotazioneManuale.getCodOperatoreInserimento());
				aEvento.setDataInserimento(aAnnotazioneManuale.getDataInserimento());

				lEveDao.setDAOFromModel(aEvento);
				BigDecimal lKeyEvento = null;
				lKeyEvento = lEveDao.insert();
				lEveDao.stop();

				lEveIdEvento = lKeyEvento;
				aAnnotazioneManuale.setEveIdEvento(lEveIdEvento);
			}

			lAnnDao = new AnnotazioneManualeDAO(lConn);

			// Validazione dell'Annotazione di riferimento (Ordinanza GE)
			if (aIdAnnotazioneManuale != null) {
				lAnnDao.setCondizioneUpdate(aIdAnnotazioneManuale);
				lAnnDao.setFlagValidato("S");
				lAnnDao.setAnnoGe(aAnnotazioneManuale.getAnnoGe()); // Anno Ordinanza SIGE
				lAnnDao.setNumeroGe(aAnnotazioneManuale.getNumeroGe()); // Numeroordinanza SIGE
				lAnnDao.setCodOperatoreAggiornamento(aAnnotazioneManuale.getCodOperatoreInserimento());
				lAnnDao.setCodUfficioAggiornamento(aAnnotazioneManuale.getCodUfficioInserimento());
				lAnnDao.setDataAggiornamento(aAnnotazioneManuale.getDataInserimento());

				// 07-2015 - MEV 29 punto 11 - Anno e Numero Procedimento SIGE
				lAnnDao.setAnnoSige(aAnnotazioneManuale.getChiaveAnnoSige());
				lAnnDao.setNumeroSige(aAnnotazioneManuale.getChiaveNumeroSige());

				lAnnDao.update();
				lAnnDao.stop();
			}

			// ========================================================================
			// Inserisco l'annotazione manuale agganciandola all'evento di richiesta
			// ========================================================================
			lAnnMod = new AnnotazioneManualeModel(aAnnotazioneManuale);

			// lAnnMod.setPenResIdPenaResidua(aPenaResidua.getIdPenaResidua());

			lAnnDao.setDAOFromModel(aAnnotazioneManuale);
			BigDecimal lKey = null;
			lKey = lAnnDao.insert();
			lAnnDao.stop();

			lAnnMod.setIdAnnotazioneManuale(lKey);

			lAnnDao.setCondizioneCodTipoNonValidataByIdFascicolo(
					aAnnotazioneManuale.getFasSieIdFascicoloSiep(),
					aAnnotazioneManuale.getCodTipoAnnotazione());
			lAnnDao.setEveIdEvento(lEveIdEvento);
			lAnnDao.update();
			lAnnDao.stop();

			// MEV9 -- Lego l'ANNMAN della Richiesta all'ANNMAN del provvedimento con UPDATE

			lAnnRichDao = new AnnotazioneManualeDAO(lConn);

			if (aIdAnnotazioneRichiesta != null) {
				lAnnRichDao.setCondizioneUpdate(aIdAnnotazioneRichiesta);
				lAnnRichDao.setAnnoIdAnnotazioneManuale(lKey);
				lAnnRichDao.setCodOperatoreAggiornamento(aAnnotazioneManuale.getCodOperatoreInserimento());
				lAnnRichDao.setCodUfficioAggiornamento(aAnnotazioneManuale.getCodUfficioInserimento());
				lAnnRichDao.setDataAggiornamento(aAnnotazioneManuale.getDataInserimento());

				lAnnRichDao.update();
				lAnnRichDao.stop();

			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"AnnotazioneManualeController.ExInserisciAnnotazioneManualeEventoUpd: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"AnnotazioneManualeController.ExInserisciAnnotazioneManualeEventoUpd: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lAnnRichDao);

			cleanup(lConn);
		}

		return lAnnMod;
	}
	// End MEV 9

	/**
	 * Inserisce l'annotazione Manuale, l'Evento e le notifiche
	 *
	 * @param AnnotazioneManualeModel
	 *            model dell'annotazione manuale
	 * @param EventoNotificaModel
	 *            model contenente l'evento e le notifiche
	 * @return il model dell'annotazione con valorizzato l'id di inserimento
	 */
	public AnnotazioneManualeModel ExInserisciAnnotazioneManualeEventoNotifica(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoNotificaModel aEventoNotifica)
			throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeDAO lAnnDao = null;
		AutoritaEsternaDAO lAutDao = null;
		NotificaDAO lNotDao = null;
		EventoDAO lEveDao = null;

		AnnotazioneManualeModel lAnnMod = null;

		try {
			lConn = getDBTransaction();

			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lAnnDao = new AnnotazioneManualeDAO(lConn);

			// ==================================================
			// Inserisco l'evento EVENTO
			// ==================================================
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEventoNotifica.getEvento());
			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// ==================================================
			// Inserisco le NOTIFICHE
			// ==================================================
			BigDecimal lKeyAutorita = null;
			int count = 0;

			while (count < aEventoNotifica.getNotifiche().length) {
				if (aEventoNotifica.getNotifiche()[count] != null) {
					if (aEventoNotifica.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(
								aEventoNotifica.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(
									aEventoNotifica.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEventoNotifica.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEventoNotifica.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					aEventoNotifica.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					lNotDao.setDAOFromModel(aEventoNotifica.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// ===========================
			// Inserisco le ANNOTAZIONI
			// ===========================
			aAnnotazioneManuale.setEveIdEvento(lKeyEvento);
			lAnnMod = new AnnotazioneManualeModel(aAnnotazioneManuale);

			lAnnDao.setDAOFromModel(aAnnotazioneManuale);
			BigDecimal lKey = null;
			lKey = lAnnDao.insert();
			lAnnMod.setIdAnnotazioneManuale(lKey);

			lAnnDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"AnnotazioneManualeController.ExInserisciAnnotazioneManualeEventoNotifica: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"AnnotazioneManualeController.ExInserisciAnnotazioneManualeEventoNotifica: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lEveDao);
			cleanup(lAutDao);
			cleanup(lNotDao);

			cleanup(lConn);
		}

		return lAnnMod;
	}

	/**
	 * ************************************************************************** Inserisce le decisioni del
	 * GE (depenalizzazione, incostituzionalità, amnistia/indulto). Inserisce il provvedimento Inserisce
	 * l'annotazione Inserisce/Aggiorna l'Ordinanza del GE Inserisce l'annotazione dell'ordinanza Aggiorna
	 * Tutte le annotazioni dello stesso tipo con decisione agganciandole al provvedimento corrente.
	 *
	 * Aggiorna Tutte la richieste dello stesso tipo validandole
	 *
	 * @param aAnnotazioneManuale
	 * @param aEveOrdinanzaMod
	 * @param aEveProvvedimentoMod
	 * @param aAnnotazioneOrdinanza
	 * @param aListaRichieste
	 */
	public AnnotazioneManualeModel ExInserisciAnnotazioneManualeProvvedimentoRichiesta(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEveOrdinanzaMod,
			EventoModel aEveProvvedimentoMod, AnnotazioneManualeModel aAnnotazioneOrdinanza,
			Vector aListaRichieste) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeDAO lAnnDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDao = null;

		AnnotazioneManualeModel lAnnMod = null;
		EventoModel lEveProvvedimentoMod = new EventoModel();
		EventoModel lEveOrdinanzaMod = new EventoModel();

		try {
			lConn = getDBTransaction();
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lAnnDao = new AnnotazioneManualeDAO(lConn);

			// chiave del'Ordinanza serve per legarla con il provvedimento
			BigDecimal lKeyOrdinanza = null;

			// ========================================================================
			// Ricerco l'ORDINANZA del GE, se non presente la inserisco altrimenti la
			// aggiorno. Inserisco l'annotazione relativa
			// ========================================================================
			lEveSqlDao.ricercaEventoTipoCodMotProvNonValidato(aEveOrdinanzaMod);

			lEveOrdinanzaMod = (EventoModel) lEveSqlDao.getModelByKey();

			// AMBROS a8-rr-222
			if (!aEveOrdinanzaMod.getCodMotivo().equals("0284")) {
				aEveOrdinanzaMod.setCodEsito("-");
			}
			// aEveOrdinanzaMod.setCodEsito("-");
			// END AMBROS

			aEveOrdinanzaMod.setCodLuogoDestinatario("-");
			aEveOrdinanzaMod.setCodTipoUfficioDestinatario("-");

			if (lEveOrdinanzaMod == null) { // Manca l'evento Ordinanza, lo inserisco
				aEveOrdinanzaMod.setCodUfficioInserimento(aAnnotazioneManuale.getCodUfficioInserimento());
				aEveOrdinanzaMod.setCodOperatoreInserimento(aAnnotazioneManuale.getCodOperatoreInserimento());
				aEveOrdinanzaMod.setDataInserimento(DateUtils.getSysDate());

				aEveOrdinanzaMod.setDataEmissione(aAnnotazioneOrdinanza.getDataGE());

				lEveDao.setDAOFromModel(aEveOrdinanzaMod);
				BigDecimal lKeyEvento = null;
				lKeyEvento = lEveDao.insert();

				lKeyOrdinanza = lKeyEvento;

				lEveDao.stop();

				aAnnotazioneOrdinanza.setEveIdEvento(lKeyEvento);

				// Inserimento Annotazione Manuale riferita a Ordinanza
				lAnnDao.setDAOFromModel(aAnnotazioneOrdinanza);
				lAnnDao.insert();
				lAnnDao.stop();
				// BigDecimal lKey = null;
				// lKey = lAnnDao.insert();
				// lAnnMod.setIdAnnotazioneManuale(lKey);
			} else { // Ordinanza già presente, aggancio l'annotazione all'ordinanza
				aEveOrdinanzaMod.setIdEvento(lEveOrdinanzaMod.getIdEvento());
				aEveOrdinanzaMod.setCodUfficioAggiornamento(aAnnotazioneManuale.getCodUfficioInserimento());
				aEveOrdinanzaMod
						.setCodOperatoreAggiornamento(aAnnotazioneManuale.getCodOperatoreInserimento());
				aEveOrdinanzaMod.setDataAggiornamento(DateUtils.getSysDate());

				aEveOrdinanzaMod.setDataEmissione(aAnnotazioneOrdinanza.getDataGE());

				lEveDao.setDAOFromModelForUpdate(aEveOrdinanzaMod);
				lEveDao.update();
				lEveDao.stop();

				aAnnotazioneOrdinanza.setEveIdEvento(lEveOrdinanzaMod.getIdEvento());

				// Modifica Annotazione Manuale riferita a Ordinanza
				lAnnDao.setDAOFromModelForUpdate(aAnnotazioneOrdinanza);
				lAnnDao.update();
				lAnnDao.stop();
				lKeyOrdinanza = lEveOrdinanzaMod.getIdEvento();
			}
			lEveSqlDao.stop();

			// ========================================================================
			// Ricerco l'evento del PROVVEDIMENTO, se non presente lo inserisco
			// altrimenti lo aggiorno.
			// ========================================================================
			lEveSqlDao.ricercaEventoTipoCodMotProvNonValidato(aEveProvvedimentoMod);

			lEveProvvedimentoMod = (EventoModel) lEveSqlDao.getModelByKey();
			// AMBROS a8-rr-222
			if (!aEveProvvedimentoMod.getCodMotivo().equals("0284")) {
				aEveProvvedimentoMod.setCodEsito("-");
			}
			// aEveProvvedimentoMod.setCodEsito("-");
			// END AMBROS
			aEveProvvedimentoMod.setCodLuogoDestinatario("-");
			aEveProvvedimentoMod.setCodTipoUfficioDestinatario("-");
			// aEveProvvedimentoMod.setFlagVideoSiep("N");
			// aEveProvvedimentoMod.setFlagStampaSiep("N");

			BigDecimal lEveIdEvento = null;

			if (lEveProvvedimentoMod != null) { // Provvedimento presente lo aggiorno
				aEveProvvedimentoMod.setIdEvento(lEveProvvedimentoMod.getIdEvento());

				aEveProvvedimentoMod
						.setCodUfficioAggiornamento(aAnnotazioneManuale.getCodUfficioInserimento());
				aEveProvvedimentoMod
						.setCodOperatoreAggiornamento(aAnnotazioneManuale.getCodOperatoreInserimento());
				aEveProvvedimentoMod.setDataAggiornamento(DateUtils.getSysDate());

				// STUB 06/09/2006 valorizzata Data emissione conforme all'ordinanza
				aEveProvvedimentoMod.setDataEmissione(aAnnotazioneOrdinanza.getDataGE());

				lEveDao.setDAOFromModelForUpdate(aEveProvvedimentoMod);
				lEveDao.update();
				lEveDao.stop();

				lEveIdEvento = lEveProvvedimentoMod.getIdEvento();
				aAnnotazioneManuale.setEveIdEvento(lEveIdEvento);
			} else { // Provvedimento non presente lo inserisco
				aEveProvvedimentoMod.setCodUfficioInserimento(aAnnotazioneManuale.getCodUfficioInserimento());
				aEveProvvedimentoMod
						.setCodOperatoreInserimento(aAnnotazioneManuale.getCodOperatoreInserimento());
				aEveProvvedimentoMod.setDataInserimento(DateUtils.getSysDate());

				// STUB 06/09/2006 valorizzata Data emissione conforme all'ordinanza
				aEveProvvedimentoMod.setDataEmissione(aAnnotazioneOrdinanza.getDataGE());

				lEveDao.setDAOFromModel(aEveProvvedimentoMod);
				BigDecimal lKeyEvento = null;
				lKeyEvento = lEveDao.insert();
				lEveDao.stop();

				lEveIdEvento = lKeyEvento;
				aAnnotazioneManuale.setEveIdEvento(lEveIdEvento);
			}

			// ==================================
			// Inserimento Annotazione Manuale
			// ==================================
			lAnnMod = new AnnotazioneManualeModel(aAnnotazioneManuale);
			lAnnDao.setDAOFromModel(aAnnotazioneManuale);
			BigDecimal lKey = null;
			lKey = lAnnDao.insert();
			lAnnMod.setIdAnnotazioneManuale(lKey);
			lAnnDao.stop();

			// ========================================================================
			// Update su Annotazione Manuale di idEvento
			// Aggiorna l'idEvento di tutte le annotazioni a sistema dello stesso tipo
			// di quelle passate in input non ancora validate e che non sono una
			// richiesta.
			// Di fatto le aggancia al Provvedimento corrente.
			// n.b. sono già agganciate al provvedimento corrente
			// inutile nel caso di Amnistia indulto, verificare gli altri casi
			// ========================================================================
			lAnnDao.setCondizioneCodTipoNonValidataByIdFascicolo(
					aAnnotazioneManuale.getFasSieIdFascicoloSiep(),
					aAnnotazioneManuale.getCodTipoAnnotazione());
			lAnnDao.setEveIdEvento(lEveIdEvento);
			lAnnDao.update();

			lAnnDao.stop();

			// ============================
			// Upadate Ordinanza
			// l'ordinanza punta al Provv corrispondente
			// ============================
			aEveOrdinanzaMod.setIdEvento(lKeyOrdinanza);
			aEveOrdinanzaMod.setCodUfficioAggiornamento(aAnnotazioneManuale.getCodUfficioInserimento());
			aEveOrdinanzaMod.setCodOperatoreAggiornamento(aAnnotazioneManuale.getCodOperatoreInserimento());
			aEveOrdinanzaMod.setDataAggiornamento(DateUtils.getSysDate());
			aEveOrdinanzaMod.setEveIdEvento(lEveIdEvento);
			lEveDao.setDAOFromModelForUpdate(aEveOrdinanzaMod);
			lEveDao.update();
			lEveDao.stop();

			// Update Annotazioni Manuali inserite da Richieste GE (PROVVEDIMENTO)
			// lAnnDao.setCondizioneConSenzaRichiestaByIdFascicolo(aAnnotazioneManuale.getFasSieIdFascicoloSiep());
			// ========================================================================
			// Valida tutte le annotazioni manuali con richiesta al GE
			// (FLAG_APP_PROVVISORIA='R' or 'A') dello stesso tipo sullo stesso fascicolo
			// Da modificare nella nuova versione. L'utente sceglie quali richieste
			// ========================================================================
			lAnnDao.setCondizioneConSenzaRichiestaByIdFascicoloTipoAnnotazione(
					aAnnotazioneManuale.getFasSieIdFascicoloSiep(),
					aAnnotazioneManuale.getCodTipoAnnotazione());
			lAnnDao.setFlagValidato("S");
			lAnnDao.update();
			lAnnDao.stop();

			// ========================================================================
			// new aggiorno l'anno_id_annotazione_manuale delle richieste selezionate
			// dall'utente sulla form che in questo modo vengono agganciate
			// all'annotazione della decisione
			// n.b. per ora solo nel caso di amnistia indulto
			// ========================================================================
			if (aAnnotazioneManuale.getCodTipoAnnotazione() != null
					&& (aAnnotazioneManuale.getCodTipoAnnotazione().equals("002")
							|| aAnnotazioneManuale.getCodTipoAnnotazione().equals("003")
							// MEV 37 -Inizio
							|| aAnnotazioneManuale.getCodTipoAnnotazione().equals("004")
							|| aAnnotazioneManuale.getCodTipoAnnotazione().equals("017")
					// MEV 37 -Fine
					)) { // aggiorno le richieste
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
				siesLogger.debug("Aggiorno le richieste: " + aListaRichieste.size());
				for (int i = 0; i < aListaRichieste.size(); i++) {
					AnnotazioneManualeModel lAnnModRichiesta = (AnnotazioneManualeModel) aListaRichieste
							.elementAt(i);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("");
					lAnnDao.setCondizioneUpdate(lAnnModRichiesta.getIdAnnotazioneManuale());
					lAnnDao.setAnnoIdAnnotazioneManuale(lAnnMod.getIdAnnotazioneManuale());
					lAnnDao.update();
				}
				lAnnDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"AnnotazioneManualeController.ExInserisciAnnotazioneManualeProvvedimentoRichiesta: "
							+ ex);
		} catch (Exception ex) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"AnnotazioneManualeController.ExInserisciAnnotazioneManualeProvvedimentoRichiesta: "
							+ ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);

			cleanup(lConn);
		}

		return lAnnMod;
	}

	public String ExInserisciAnnManualiWithoutSequence(ArrayList aAnnManuali, Connection lConn)
			throws F3BException {

		AnnotazioneManualeDAO lAnnDao = null;

		AnnotazioneManualeModel lAnnManuale = null;
		String lCodEsito = "00000";

		try {
			for (int j = 0; j < aAnnManuali.size(); j++) {
				// Controllo sull'Action che esista almeno una Annotazione.
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.warn("CLASSE * * * = " + aAnnManuali.get(j).getClass().getName());
				lAnnManuale = new AnnotazioneManualeModel((AnnotazioneManualeModel) aAnnManuali.get(j));

				// Inserimento prima Annotazione
				lAnnDao = new AnnotazioneManualeDAO(lConn);
				lAnnDao.setDAOFromModel(lAnnManuale);
				lAnnDao.setWithoutSequence(true);

				lAnnDao.insert();
				lAnnDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.warn("Annotazione Manuale gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire l'Annotazione! ");
			}
		} finally {
			cleanup(lAnnDao);
		}
		return lCodEsito;
	}

	public AnnotazioneManualeModel ExInserisciAnnotazioneManuale(AnnotazioneManualeModel aAnnotazioneManuale)
			throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeDAO lAnnDao = null;
		AnnotazioneManualeModel lAnnMod = null;

		try {
			lConn = getDBConnection();
			lAnnMod = new AnnotazioneManualeModel(aAnnotazioneManuale);
			lAnnDao = new AnnotazioneManualeDAO(lConn);
			lAnnDao.setDAOFromModel(aAnnotazioneManuale);

			BigDecimal lKey = null;
			lKey = lAnnDao.insert();
			commit(lConn);
			lAnnMod.setIdAnnotazioneManuale(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"AnnotazioneManualeController.ExInserisciAnnotazioneManuale: Non posso inserire: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	/**
	*
	*/
	public Vector ExRicercaAnnotazioneManualeGenerico(AnnotazioneManualeModel aAnnotazioneManuale)
			throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnotazioneManuali = new Vector();

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeGenerico(aAnnotazioneManuale);
			lAnnotazioneManuali = new Vector(lAnnDao.getModels());
			if (lAnnotazioneManuali.size() == 0) {
				throw new F3BException(F3BException.EX_NOT_FOUND, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManualeGenerico: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
		return lAnnotazioneManuali;
	}

	/**
	 * Effettua una ricerca nella tabella ANNOTAZIONE_MANUALE dei record con FLAG_APP_PROVVISORIA = 'R' o 'A'
	 * e filtrati per Fascicolo SIEP, FLAG_VALIDATO, COD_TIPO_ANNOTAZIONE passati attraverso
	 * aAnnotazioneManuale.
	 */
	public Vector ExRicercaRichieste(AnnotazioneManualeModel aAnnotazioneManuale) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnotazioneManuali = new Vector();

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaRichieste(aAnnotazioneManuale);
			lAnnotazioneManuali = new Vector(lAnnDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaRichieste: Non posso leggere : " + daoEx);
		} catch (Exception sqe) {
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManualeGenerico: Non posso leggere -> "
							+ sqe);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
		return lAnnotazioneManuali;
	}

	public Vector ExRicercaAnnotazioneManuale(AnnotazioneManualeModel aAnnotazioneManuale)
			throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnotazioneManuali = new Vector();

		try {
			lConn = getDBConnection();
			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManuale(aAnnotazioneManuale);
			lAnnotazioneManuali = new Vector(lAnnDao.getModels());
			if (lAnnotazioneManuali.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManuale: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
		return lAnnotazioneManuali;
	}

	/**
	 * Ricerca TUTTE le annotazioni manuali legate al fascicolo indipendentemente dallo stato di validazione e
	 * dal tipo di annotazione
	 *
	 * @param aKey
	 *            = id del fascicolo
	 * @return vettore di AnnotazioneManualeModel
	 * @throws F3BException
	 *             se annotazione non trovata o errore
	 */
	public Vector ExRicercaAnnotazioneManualeByIdFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnotazioneManuali = new Vector();

		try {
			lConn = getDBConnection();
			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeByIdFascicolo(aKey);
			lAnnotazioneManuali = new Vector(lAnnDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManuale: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
		return lAnnotazioneManuali;
	}

	/**
	 * Ricerca TUTTE le annotazioni manuali legate all'evento indipendentemente dallo stato di validazione e
	 * dal tipo di annotazione
	 *
	 * @param aKey
	 *            = id dell'evento
	 * @return vettore di AnnotazioneManualeModel
	 * @throws F3BException
	 *             se annotazione non trovata o errore
	 */
	public Vector ExRicercaAnnotazioneManualeByIdEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnotazioneManuali = new Vector();

		try {
			lConn = getDBConnection();
			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeByIdEvento(aKey);
			lAnnotazioneManuali = new Vector(lAnnDao.getModels());
			if (lAnnotazioneManuali.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManualeByIdEvento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
		return lAnnotazioneManuali;
	}

	public AnnotazioneManualeModel ExRicercaAnnotazioneManualeByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		AnnotazioneManualeModel lAnnMod;

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeByKey(aKey);
			lAnnMod = (AnnotazioneManualeModel) lAnnDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManualeByKey: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	public AnnotazioneManualeModel ExRicercaAnnotazioneManualeByIdReato(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		AnnotazioneManualeModel lAnnMod;

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeByIdReato(aKey);
			lAnnMod = (AnnotazioneManualeModel) lAnnDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManuale: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	public Vector ExRicercaAnnotazioniManualiByIdReato(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnMod = null;

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeByIdReato(aKey);
			lAnnMod = new Vector(lAnnDao.getModels());

			if (lAnnMod == null || lAnnMod.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Annotazione trovata");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManuale: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	public Vector ExRicercaAnnotazioneManualeNoErrorByIdReato(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnMod = null;

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeByIdReato(aKey);
			lAnnMod = new Vector(lAnnDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManualeNoErrorByIdReato: " + daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	/**
	 * Ricerca tutte le annotazioni manuali con FLAG_APP_PROVVISORIA<>A e R associate al reato validate o meno
	 *
	 * @param aKey
	 *            id del reato
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAnnotazioniManualiNonRichiesteByIdReato(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnMod = null;

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);

			lAnnDao.ricercaAnnotazioneManualeNonRichiesteByIdReato(aKey);
			lAnnMod = new Vector(lAnnDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManuale: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	public Vector ExRicercaAnnotazioniManuali_non_richieste_ByIdFascicolo(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnMod = null;

		try {
			lConn = getDBConnection();
			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManuale_non_richieste_ByIdFascicolo(aKey);
			lAnnMod = new Vector(lAnnDao.getModels());

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManuale: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
		return lAnnMod;
	}

	/**
	 * Ricerca le annotazioni manuali per il fasscicolo specificato e il tipo. Scarta le richieste al GE (A e
	 * R) ORDER BY DATA_INSERIMENTO
	 *
	 * @param aIdFascicolo
	 * @param aCodTipoAnnotazione
	 * @param aFlagValidazione
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAnnotazioniManualiByIdFascicoloNonRichiesteTipoAnn(BigDecimal aIdFascicolo,
			String aCodTipoAnnotazione, String aFlagValidazione) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnSqlDao = null;
		Vector lAnnMod = null;

		try {
			lConn = getDBConnection();

			lAnnSqlDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnSqlDao.ricercaAnnotazioneManualeByIdFascicoloNonRichiesteTipoAnn(aIdFascicolo,
					aCodTipoAnnotazione, aFlagValidazione);
			lAnnMod = new Vector(lAnnSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioniManualiByIdFascicoloNonRichiesteTipoAnn: "
							+ daoEx);
		} finally {
			cleanup(lAnnSqlDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	/**
	 * Recupera tutte le annotazioni manuali legate a Richieste con ancticipazione ma non ancora validate
	 *
	 * @param aKey
	 * @throws DAOException
	 */
	public Vector ExRicercaAnnotazioneManualeRichiesteAnticipazioneAministiaIndultoByIdFascicolo(
			BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnMod = null;

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeRichiesteAnticipazioneAministiaIndultoByIdFascicolo(aKey);
			lAnnMod = new Vector(lAnnDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManualeRichiesteAnticipazioneAministiaIndultoByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	/**
	 * Ricerca le annotazioni di tipo Amnistia o Indulto lagate a Richieste ('R') <b>non validate</b> senza
	 * anticipazione
	 *
	 * @param aKey
	 *            idFascicolo
	 */
	public Vector ExRicercaAnnotazioneManualeRichiesteAministiaIndultoByIdFascicolo(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnMod = null;

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeRichiesteAministiaIndultoByIdFascicolo(aKey);
			lAnnMod = new Vector(lAnnDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManualeRichiesteAministiaIndultoByIdFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	/**
	 * Aggiorna l'annotazione manuale
	 *
	 * @param
	 */
	public AnnotazioneManualeModel ExModificaAnnotazioneManuale(AnnotazioneManualeModel aAnnotazioneManuale)
			throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeDAO lAnnDao = null;
		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel(aAnnotazioneManuale);

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeDAO(lConn);
			lAnnDao.setDAOFromModelForUpdate(aAnnotazioneManuale);
			lAnnDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"AnnotazioneManualeController.ExModificaAnnotazioneManuale: Non posso inserire: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	public void ExCancellaAnnotazioneManuale(AnnotazioneManualeModel aAnnotazioneManuale)
			throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeDAO lAnnDao = null;

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeDAO(lConn);
			lAnnDao.setCondizioneUpdate(aAnnotazioneManuale.getIdAnnotazioneManuale());
			lAnnDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExCancellaAnnotazioneManuale: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
	}

	/**
	*
	*/
	public AnnotazioneManualeModel ExRicercaAnnotazioniManualiByIdEvento(BigDecimal aKeyEvento)
			throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		AnnotazioneManualeModel lAnnMod = null;

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);

			lAnnDao.ricercaAnnotazioneManualeByIdEvento(aKeyEvento);
			lAnnMod = (AnnotazioneManualeModel) (lAnnDao.getModelByKey());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioniManualiByIdEvento: " + daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	/**
	 * Recupera l'ultima Ordinanza (validata o meno) e l'annotazione manuale associata (una sola)
	 *
	 * @param aKeyFascicolo
	 *            id fascicolo
	 */
	public AnnotazioneOrdinanzaModel ExRicercaUltimaAnnotazioneManualeOrdinanzaByIdFascicolo(
			BigDecimal aKeyFascicolo) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveSqlDao = null;
		AnnotazioneManualeSqlDAO lAnnSqlDao = null;

		AnnotazioneOrdinanzaModel lAnnOrdMod = null;

		try {
			lConn = getDBConnection();

			lEveSqlDao = new EventoSqlDAO(lConn);

			lEveSqlDao.ricercaEventoOrdinanzaAnnotazioneManuale(aKeyFascicolo);

			EventoModel lEveMod = (EventoModel) (lEveSqlDao.getModelByKey());

			if (lEveMod != null) {
				lAnnOrdMod = new AnnotazioneOrdinanzaModel();
				lAnnOrdMod.setEvento(lEveMod);

				lAnnSqlDao = new AnnotazioneManualeSqlDAO(lConn);
				lAnnSqlDao.ricercaAnnotazioneManualeByIdEvento(lEveMod.getIdEvento());
				AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) (lAnnSqlDao.getModelByKey());

				lAnnOrdMod.setAnnotazioneManuale(lAnnMod);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaUltimaAnnotazioneManualeOrdinanzaByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lAnnSqlDao);

			cleanup(lConn);
		}

		return lAnnOrdMod;
	}

	public AnnotazioneOrdinanzaModel ExRicercaUltimaAnnotazioneManualeOrdinanzaByIdFascicolo(
			EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveSqlDao = null;
		AnnotazioneManualeSqlDAO lAnnSqlDao = null;

		AnnotazioneOrdinanzaModel lAnnOrdMod = null;

		try {
			lConn = getDBConnection();

			lEveSqlDao = new EventoSqlDAO(lConn);

			lEveSqlDao.ricercaEventoTipoCodMotProvNonValidato(aEvento);

			EventoModel lEveMod = (EventoModel) (lEveSqlDao.getModelByKey());

			if (lEveMod != null) {
				lAnnOrdMod = new AnnotazioneOrdinanzaModel();
				lAnnOrdMod.setEvento(lEveMod);

				lAnnSqlDao = new AnnotazioneManualeSqlDAO(lConn);
				lAnnSqlDao.ricercaAnnotazioneManualeByIdEvento(lEveMod.getIdEvento());
				AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) (lAnnSqlDao.getModelByKey());

				lAnnOrdMod.setAnnotazioneManuale(lAnnMod);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaUltimaAnnotazioneManualeOrdinanzaByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lAnnSqlDao);

			cleanup(lConn);
		}

		return lAnnOrdMod;
	}

	/**
	 * Ricerca Ordinanza Sige (PROVVEDIMENTO_SIGE) validata legato ad un Fascicolo SIEP e ad una
	 * ANNOTAZIONE_MANUALE.
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */

	public AnnotazioneOrdinanzaSigeModel ExRicercannotazioneManualeOrdinanzaSigeByIdFascicolo(
			EventoModel aEvento) throws F3BException {

		Connection lConn = null;

		// DAO utilizzati per le ricerche nelle 3 tabelle
		EventoSqlDAO lEveSqlDao = null;
		AnnotazioneManualeSqlDAO lAnnSqlDao = null;
		ProvvedimentoSigeDAO lProvDAO = null;

		// Aggregato EVENTO-PROVVEDIMENTO_SIGE-ANNOTAZIONE_MANUALE
		AnnotazioneOrdinanzaSigeModel lAnnOrdMod = null;

		BigDecimal lIdEvento = null;

		try {
			lConn = getDBConnection();

			// Ricerca ID_EVENTO
			lEveSqlDao = new EventoSqlDAO(lConn);
			lIdEvento = lEveSqlDao.ricercaIdEventoProvSigeAnnMan(aEvento);

			if (lIdEvento != null) {
				// Ricerca dell'Evento
				lEveSqlDao.ricercaEventoByKey(lIdEvento);
				EventoModel lEveMod = (EventoModel) (lEveSqlDao.getModelByKey());

				if (lEveMod != null) {
					lAnnOrdMod = new AnnotazioneOrdinanzaSigeModel();
					lAnnOrdMod.setEvento(lEveMod);

					// Ricerca Annotazione_manuale
					lAnnSqlDao = new AnnotazioneManualeSqlDAO(lConn);
					lAnnSqlDao.ricercaAnnotazioneManualeByIdEvento(lEveMod.getIdEvento());
					AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) (lAnnSqlDao.getModelByKey());
					lAnnOrdMod.setAnnotazioneManuale(lAnnMod);

					// Ricerca Provvedimento_Sige
					lProvDAO = new ProvvedimentoSigeDAO(lConn);
					lProvDAO.setIdEventoGenerato(lIdEvento);
					lProvDAO.selCondizioneByIdEvento(lIdEvento);
					ProvvedimentoSigeModel lProvvedimento = (ProvvedimentoSigeModel) lProvDAO.getModelByKey();
					lProvvedimento.decodifica();
					lAnnOrdMod.setProvSige(lProvvedimento);
				}
			}
		} catch (Exception sqe) {
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercannotazioneManualeOrdinanzaSigeByIdFascicolo: "
							+ sqe);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lAnnSqlDao);
			cleanup(lProvDAO);

			cleanup(lConn);
		}

		return lAnnOrdMod;
	}

	/**
	 * Ricerca Ordinanza Sige (PROVVEDIMENTO_SIGE, ANNOTAZIONE_MANUALE, EVENTO) a partire dall'ID
	 * dell'AnnotazioneManuale..
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public AnnotazioneOrdinanzaSigeModel ExRicercannotazioneManualeOrdinanzaSigeByIdAnnMan(
			BigDecimal aIdAnnotazioneManuale) throws F3BException {

		Connection lConn = null;

		// DAO utilizzati per le ricerche nelle 3 tabelle
		EventoSqlDAO lEveSqlDao = null;
		AnnotazioneManualeSqlDAO lAnnSqlDao = null;
		ProvvedimentoSigeDAO lProvDAO = null;

		// Aggregato EVENTO-PROVVEDIMENTO_SIGE-ANNOTAZIONE_MANUALE
		AnnotazioneOrdinanzaSigeModel lAnnOrdMod = null;

		try {
			lConn = getDBConnection();
			if (aIdAnnotazioneManuale != null) {
				// Ricerca Annotazione_manuale
				lAnnSqlDao = new AnnotazioneManualeSqlDAO(lConn);
				lAnnSqlDao.ricercaAnnotazioneManualeByKey(aIdAnnotazioneManuale);
				AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) (lAnnSqlDao.getModelByKey());

				if (lAnnMod != null && lAnnMod.getEveIdEvento() != null) {
					// Ricerca dell'Evento
					lEveSqlDao = new EventoSqlDAO(lConn);
					lEveSqlDao.ricercaEventoByKey(lAnnMod.getEveIdEvento());
					EventoModel lEveMod = (EventoModel) (lEveSqlDao.getModelByKey());

					if (lEveMod != null) {
						// Ricerca Provvedimento_Sige
						lProvDAO = new ProvvedimentoSigeDAO(lConn);
						lProvDAO.setIdEventoGenerato(lAnnMod.getEveIdEvento());
						lProvDAO.selCondizioneByIdEvento(lAnnMod.getEveIdEvento());
						ProvvedimentoSigeModel lProvvedimento = (ProvvedimentoSigeModel) lProvDAO
								.getModelByKey();
						if (lProvvedimento != null) {
							lProvvedimento.decodifica();

							// Creazione dell'aggregato risultato della ricerca
							lAnnOrdMod = new AnnotazioneOrdinanzaSigeModel();
							lAnnOrdMod.setEvento(lEveMod);
							lAnnOrdMod.setAnnotazioneManuale(lAnnMod);
							lAnnOrdMod.setProvSige(lProvvedimento);
						}
					}
				}
			}
		} catch (Exception sqe) {
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercannotazioneManualeOrdinanzaSigeByIdAnnMan: " + sqe);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lAnnSqlDao);
			cleanup(lProvDAO);

			cleanup(lConn);
		}

		return lAnnOrdMod;
	}

	/**
	 * Ricerca tutte le annotazioni e i reati associati legati all'ultimo Provvedimento (04) o Richiesta (26)
	 * con codice motivo passato in input.
	 *
	 * @param aKeyFascicolo
	 *            id del fascicolo @param aCodMotivo - Se specificato richerca l'evento con codice motivo
	 *            passato in input. Se non specificato... @return Vettore di AnnotazioneReatoModel @throws
	 */
	public Vector ExRicercaUltimeAnnotazioniManualiReatiByIdFascicolo(BigDecimal aKeyFascicolo,
			String aCodMotivo) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		AnnotazioneManualeSqlDAO lAnnSqlDao = null;
		ReatoSqlDAO lReaSqlDao = null;

		Vector lListAnnoRea = new Vector();

		try {
			lConn = getDBConnection();

			lEveSqlDao = new EventoSqlDAO(lConn);

			EventoModel lEveMod = null;

			// ========================================================================
			//
			//
			// ========================================================================
			if (aCodMotivo != null && !aCodMotivo.equals("")) { // Ricerco un provvedimento o una richiesta
																// con il codice motivo passato in input
				lEveMod = new EventoModel();

				lEveMod.setCodTipoEvento("01");
				lEveMod.setCodTipoProvvedimento("04");
				lEveMod.setCodMotivo(aCodMotivo);
				lEveMod.setFasSieIdFascicoloSiep(aKeyFascicolo);
				// Si ricerca sia per CodTipoProvvedimento 04 che 26. Luigi 29-09-2005
				lEveSqlDao.ricercaEventoTipoMotProvEveDataEmis(lEveMod, "26");
				lEveMod = (EventoModel) (lEveSqlDao.getModelByKey());
			} else { //
				lEveSqlDao.ricercaEventoProvvedimentoAnnotazioneManuale(aKeyFascicolo);
				lEveMod = (EventoModel) (lEveSqlDao.getModelByKey());
			}

			// ========================================================================
			// Ricerco le annotazioni legate all'evento
			// ========================================================================
			if (lEveMod != null) {
				lAnnSqlDao = new AnnotazioneManualeSqlDAO(lConn);

				lAnnSqlDao.ricercaAnnotazioneManualeByIdEvento(lEveMod.getIdEvento());
				Vector lListAnno = new Vector(lAnnSqlDao.getModels());

				lReaSqlDao = new ReatoSqlDAO(lConn);

				ReatoModel lRea = null;
				Iterator lIter = lListAnno.iterator();
				while (lIter.hasNext()) {
					AnnotazioneManualeModel lAnnMan = (AnnotazioneManualeModel) lIter.next();

					if (lAnnMan.getReaIdReato() != null) {
						lReaSqlDao.ricercaReatoByKey(lAnnMan.getReaIdReato());
						lRea = (ReatoModel) (lReaSqlDao.getModelByKey());
					} else
						lRea = null;

					lListAnnoRea.add(new AnnotazioneReatoModel(lAnnMan, lRea));
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaUltimeAnnotazioniManualiReatiByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lAnnSqlDao);
			cleanup(lReaSqlDao);

			cleanup(lConn);
		}

		return lListAnnoRea;
	}

	/**
	 * Produce il Documento di stampa per Annotazioni Manuali
	 *
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumentoXAnnotazioni(EventoNotificaModel aEvento,
			UtenteModel aUtenteModel) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;
		try {
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveMod = lCtrlEve
					.ExRicercaEventoNotificaByKey(aEvento.getEvento().getIdEvento());

			lEveMod.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());
			lEveMod.setNomeTemplate(aEvento.getNomeTemplate());
			lEveMod.getEvento().setTemIdTemplate(aEvento.getNomeTemplate());
			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			// TreeModel lTree = lStampa.prelevaDatiEventoSiep(lEveMod);

			TreeModel lTree = lStampa.prelevaDatiEventoSiepXAnnotazioni(lEveMod, aUtenteModel);

			ReportGenerator lReport = new ReportGenerator();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Chiave = " + aEvento.getNomeTemplate());
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("EVENTO >>> " + aEvento.getEvento().toString());

			aEvento.getEvento().setDocBlobIn(lByteArrayInput);
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("AnnotazioneManualeController.ExStampaDocumentoXAnnotazioni: " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Eccezione ", ex);
			throw new F3BException("AnnotazioneManualeController.ExStampaDocumentoXAnnotazioni: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Upload Pene espiate Senza titolo: - Presofferto stesso reato - Fungibilità Misura Cautelare Valida
	 * l'ordinanza (quale?) Aggiorna lo scadenzario (se non libero) Se in Misura Alternativa - aggiorna la
	 * data fine misura - aggiorna nome provvedimento Se Libero o detenuto questa causa - valida le
	 * annotazioni manuali - valida pena residua - valida la fungibilità, ma solo se direttamente puntata
	 * dalla annotazione (cioè mai) Effettua la validazione dell'evento Aggiorna STATO_PROCEDIMENTO
	 *
	 * @param aEvento
	 * @param codicePosizione
	 * @param aFascicolo
	 * @since release 1.2.1 (VSS version 33)
	 * @return
	 */
	public EventoModel ExUpdatePeneEspiateSenzaTitolo(EventoModel aEvento, String codicePosizione,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;
		// Connection lConnBlob = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		MisuraAlternativaDAO lMisDAO = null;
		AnnotazioneManualeDAO lAnnManualeDAO = null;
		AnnotazioneManualeSqlDAO lAnnManuSqlDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		FungibilitaSqlDAO lFunSql = null;
		FungibilitaDAO lFunDAO = null;
		ScadenzarioSqlDAO lScaSqlDAO = null;
		ScadenzarioDAO lScaDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		// MisuraAlternativaModel lMisMod = null;

		try {
			lConn = getDBTransaction();

			// ====================================================
			// Recupero l'evento da validare:
			// mi servono la Data emissione e il codice motivo
			// ====================================================
			lEveDao = new EventoDAO(lConn);

			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				lEveApp.setCodMotivo(lEveDao.getCodMotivo());
			}

			// =========================================================
			// Aggiorna l'evento di tipo ordinanza (quale?)(lo valida)
			// =========================================================
			EventoModel lEveOrdMod = new EventoModel();

			lEveOrdMod.setCodTipoEvento("01");
			lEveOrdMod.setCodTipoProvvedimento("03");
			lEveOrdMod.setCodMotivo(lEveApp.getCodMotivo());
			lEveOrdMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoTipoCodMotProvNonValidato(lEveOrdMod);

			EventoModel lEveOrdModRet = (EventoModel) (lEveSqlDao.getModelByKey());

			if (lEveOrdModRet != null) {
				lEveDao.setIdEvento(lEveOrdModRet.getIdEvento());
				lEveDao.selByKey();
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.update();
				lEveDao.stop();
			}

			// ======================================================
			// Recupero l'ultima pena residua
			// ======================================================
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByIdFascicoloDataDesc(aFascicolo.getIdFascicoloSiep());
			Vector lpenaresidua = new Vector(lPenResSqlDao.getModels());
			PenaResiduaModel lUltimaPenaResidua = (PenaResiduaModel) lpenaresidua.get(0);

			// =======================================================================
			// Aggiorno lo scadenzario FinePena/VaneRicerche con la nuova data fine
			// se non libero e non in affidamento in prova
			// =======================================================================
			if (!(codicePosizione.equals("07") || codicePosizione.equals("10") || codicePosizione.equals("16")
					|| codicePosizione.equals("17") || codicePosizione.equals("46")
					|| codicePosizione.equals("47") || codicePosizione.equals("20")
					|| codicePosizione.equals("26") || codicePosizione.equals("30"))
					&& !codicePosizione.equals("13") // Espiazione Pena in Regime di
														// Affidamento in Prova
			) {
				ScadenzarioModel lScaMod = null;

				ScadenzarioModel lScaModSet = new ScadenzarioModel();
				lScaModSet.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

				lScaSqlDAO = new ScadenzarioSqlDAO(lConn);
				lScaSqlDAO.ricercaScadenzarioFinePenaVaneRicerche(aFascicolo.getIdFascicoloSiep());
				lScaMod = (ScadenzarioModel) lScaSqlDAO.getModelByKey();

				if (lScaMod != null) {
					// aggiorna scadenzario
					lScaMod.setFlagVisto("N");

					lScaDAO = new ScadenzarioDAO(lConn);
					lScaDAO.setDataFineScadenza(lUltimaPenaResidua.getDataFine());

					lScaDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDAO.setDataAggiornamento(aEvento.getDataAggiornamento());

					// lScaModAgg.setIdScadenzario(lScaMod.getIdScadenzario());
					lScaDAO.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDAO.update();
					lScaDAO.stop();
				}
			}

			// ========================================================================
			// Se soggetto in misura alternativa, aggiorna la data fine misura con
			// la nuova data fine pena, e duplica la mis alt agganciandola all'evento
			// corrente
			// ========================================================================
			PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
			lPosMod.setCodPosizioneGiuridica(codicePosizione);
			if (lPosMod.isMisAlt()) {
				lMisDAO = new MisuraAlternativaDAO(lConn);
				lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);

				// Recupera l'ultima Misura Alternativa (data decisione)
				lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				MisuraAlternativaModel lMisMod = null;

				lMisMod = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

				if (lMisMod != null) {
					// Se la DATA_FINE_MISURA è successiva alla
					// DATA_FINE_PENA aggiorna la data fine misura
					if (lMisMod.getDataFineMisura() != null && lUltimaPenaResidua != null
							&& lUltimaPenaResidua.getDataFine() != null
							&& lMisMod.getDataFineMisura().after(lUltimaPenaResidua.getDataFine())) {
						lMisMod.setDataFineMisura(lUltimaPenaResidua.getDataFine());

						lMisMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lMisMod.setDataAggiornamento(aEvento.getDataAggiornamento());
						lMisMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

						lMisDAO.setDAOFromModelForUpdate(lMisMod);
						lMisDAO.update();
						lMisDAO.stop();

						// inserisco duplico occorrenza MA
						lMisMod.setEveIdEvento(aEvento.getIdEvento());
						lMisDAO.setDAOFromModel(lMisMod);
						lMisDAO.insert();
					}
				}

				// Inserisco nome provvedimento
				lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
				lNomProvvDAO.setCodNomeProvvedimento("NP090"); // Decreto di Computo Custodia Cautelare e
																// Delle Pene Espiate Senza Titolo Art.657 Cpp
				lNomProvvDAO.setEveIdEvento(aEvento.getIdEvento());

				lNomProvvDAO.insert();
				lNomProvvDAO.stop();
			}

			// ========================================================================
			// Aggiorna le annotazioni manuali
			// ========================================================================
			lAnnManualeDAO = new AnnotazioneManualeDAO(lConn);

			// Recupero la Annotazioni Manuali legate all'evento e le valido
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ricerca_annotazione_manuale_per_evento" + aEvento.getIdEvento());
			lAnnManuSqlDAO = new AnnotazioneManualeSqlDAO(lConn);
			lAnnManuSqlDAO.ricercaAnnotazioneManualeByIdEvento(aEvento.getIdEvento());

			Vector lAnnVec = new Vector(lAnnManuSqlDAO.getModels());
			AnnotazioneManualeModel lAnnManMod = null;

			lPenResDao = new PenaResiduaDAO(lConn);
			for (int i = 0; i < lAnnVec.size(); i++) {
				lAnnManMod = (AnnotazioneManualeModel) lAnnVec.get(i);

				lAnnManMod.setFlagValidato("S");
				lAnnManMod.setDataAggiornamento(aEvento.getDataAggiornamento());
				lAnnManMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lAnnManMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

				lAnnManualeDAO.setDAOFromModelForUpdate(lAnnManMod);
				lAnnManualeDAO.update();
				lAnnManualeDAO.stop();

				// ======================================================================
				// Aggiorno la riga della tabella misure_cautelare_bdmc
				// legata all'annotazione in esame
				// ======================================================================

				MisuraCautelareBdmcModel lMisCautBdmc = new MisuraCautelareBdmcModel();
				lMisCautBdmc.setIdAnnotazioneManuale(lAnnManMod.getIdAnnotazioneManuale());
				lMisCautBdmc.setFlagStato("I");
				IMisuraCautelareBdmc lCtrMisCauBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
				Vector misCautBdmc = lCtrMisCauBdmc.ExRicercaMisuraCautelareBdmc(lMisCautBdmc);

				if (misCautBdmc != null && misCautBdmc.size() == 1) {
					MisuraCautelareBdmcModel lMisBdmc = (MisuraCautelareBdmcModel) misCautBdmc.get(0);
					lMisBdmc.setEveIdEvento(lAnnManMod.getEveIdEvento());
					lMisBdmc.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lMisBdmc.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lMisBdmc.setDataAggiornamento(aEvento.getDataAggiornamento());
					lMisBdmc.setFlagStato("V");
					lMisBdmc.setStatoTrasmissioneVal("N");
					IMisuraCautelareBdmc lCtrMisBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
					lCtrMisBdmc.ExModificaMisuraCautelareBdmcNoCommit(lConn, lMisBdmc, aEvento);

				} else {
					if (misCautBdmc != null && misCautBdmc.size() != 0)
						throw new F3BException(
								"E' stata riscontrata un'incongruenza dei dati legati alla tabella Annotazione_Manuale e tabella per tramissione dati a BDMC");
				}

				// ======================================================================
				// Valida l'eventuale Fungibilità puntata dalle Annotazioni
				// non funziona!!!! le annotazioni non puntano la fungibilità
				// ======================================================================
				FungibilitaModel lModelFung = new FungibilitaModel();
				if (lAnnManMod.getFunIdFungibilita() != null) {
					lFunSql = new FungibilitaSqlDAO(lConn);
					lFunSql.ricercaFungibilitaByKey(lAnnManMod.getFunIdFungibilita());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("ricerca fungibilità per annotazione manuale "
							+ lAnnManMod.getFunIdFungibilita());

					lModelFung = (FungibilitaModel) lFunSql.getModelByKey();

					lFunDAO = new FungibilitaDAO(lConn);
					if (lModelFung != null) {
						lModelFung.setFlagValidato("S");
						lModelFung.setDataAggiornamento(aEvento.getDataAggiornamento());
						lModelFung.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
						lModelFung.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

						lFunDAO.setDAOFromModelForUpdate(lModelFung);
						lFunDAO.update();
						lFunDAO.stop();
					}
				}
			}

			// ============================================================
			// Valido la pena residua se non già validata
			// ============================================================
			lUltimaPenaResidua.setEveIdEvento(aEvento.getIdEvento());
			lUltimaPenaResidua.setFlagValidato("S");

			lUltimaPenaResidua.setDataAggiornamento(aEvento.getDataAggiornamento());
			lUltimaPenaResidua.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lUltimaPenaResidua.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

			lPenResSqlDao.inserisciOModificaPenaResidua(lUltimaPenaResidua);

			// ========================================================================
			// Aggiorna STATO_PROCEDIMENTO
			// ========================================================================
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodStatoProcedimento("0128"); // Emesso Decreto di Computo Custodia Cautelare
			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveApp.getDataEmissione());

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoDao.setDAOFromModel(lStatoProcMod);

			lStatoDao.insert();
			lStatoDao.stop();

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConn);
			// commit(lConnBlob);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("AnnotazioneManualeController.ExUpdatePeneEspiateSenzaTitolo : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("AnnotazioneManualeController.ExUpdatePeneEspiateSenzaTitolo : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveDaoBlob);
			cleanup(lEveSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lMisSqlDAO);
			cleanup(lMisDAO);
			cleanup(lAnnManualeDAO);
			cleanup(lAnnManuSqlDAO);
			cleanup(lNomProvvDAO);
			cleanup(lFunSql);
			cleanup(lFunDAO);
			cleanup(lScaSqlDAO);
			cleanup(lScaDAO);
			cleanup(lStatoDao);

			cleanup(lConn);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * Effettua la validazione del provvedimento di computo fungibilita Pena Detentiva per altro reato
	 *
	 * @param aEvento
	 * @param codicePosizione
	 *            - Codice Posiziona Giuridica
	 * @param aFascicolo
	 */
	public EventoModel ExUpdateFungibilita(EventoModel aEvento, String codicePosizione,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;
		// Connection lConnBlob = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		AnnotazioneManualeDAO lAnnManualeDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		FungibilitaSqlDAO lFunSql = null;
		FungibilitaDAO lFunDAO = null;
		ScadenzarioSqlDAO lScaSqlDAO = null;
		ScadenzarioDAO lScaDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		MisuraAlternativaSqlDAO lMisAltSqlDao = null;
		MisuraAlternativaDAO lMisAltDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		// AnnotazioneManualeModel lAnnMod = null;

		try {
			lConn = getDBTransaction();

			// ==============================
			// Recupero evento da validare
			// ==============================
			lEveDao = new EventoDAO(lConn);
			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();
			lEveDao.start();

			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				lEveApp.setCodMotivo(lEveDao.getCodMotivo());
			}

			// ========================================================================
			// Ricerca, se presente, un evento Ordinanza con lo stesso codice motivo
			// e lo valida (attenzione questo evento non è mai presente)
			// ========================================================================
			EventoModel lEveOrdMod = new EventoModel();

			lEveOrdMod.setCodTipoEvento("01");
			lEveOrdMod.setCodTipoProvvedimento("03");
			lEveOrdMod.setCodMotivo(lEveApp.getCodMotivo());
			lEveOrdMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoTipoCodMotProvNonValidato(lEveOrdMod);

			EventoModel lEveOrdModRet = (EventoModel) (lEveSqlDao.getModelByKey());

			if (lEveOrdModRet != null) {
				lEveDao.setIdEvento(lEveOrdModRet.getIdEvento());
				lEveDao.selByKey();
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.update();
				lEveDao.stop();
			}

			// ======================================================
			// Nome Provvedimento
			// ======================================================
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP090");
			lNomProvvDAO.setEveIdEvento(aEvento.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ========================================================================
			// Recupero l'ultima PR per aggiornare la data fine pena dello scadenzario
			// e validarla
			// ========================================================================
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByIdFascicoloDataDesc(aFascicolo.getIdFascicoloSiep());
			Vector lpenaresidua = new Vector(lPenResSqlDao.getModels());
			PenaResiduaModel lUltimaPenaResidua = (PenaResiduaModel) lpenaresidua.get(0);

			// ======================================================
			// Aggiornamento Scadenzario se
			// Libero - non detenuto altra causa
			// ======================================================
			lScaSqlDAO = new ScadenzarioSqlDAO(lConn);
			lScaDAO = new ScadenzarioDAO(lConn);
			if (aFascicolo.getFlagAltraCausa() != null && !aFascicolo.getFlagAltraCausa().equals("S")
					&& (codicePosizione.equals("07") || codicePosizione.equals("10")
							|| codicePosizione.equals("16") || codicePosizione.equals("17")
							|| codicePosizione.equals("46") || codicePosizione.equals("47")
							|| codicePosizione.equals("20") || codicePosizione.equals("26")
							|| codicePosizione.equals("30"))) {
				ScadenzarioModel lScaMod = null;
				ScadenzarioModel lScaModSet = new ScadenzarioModel();
				lScaModSet.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

				lScaSqlDAO.ricercaScadenzarioFinePenaVaneRicerche(aFascicolo.getIdFascicoloSiep());
				lScaMod = (ScadenzarioModel) lScaSqlDAO.getModelByKey();

				if (lScaMod != null) {
					// aggiorna scadenzario
					lScaDAO.setDataFineScadenza(lUltimaPenaResidua.getDataFine());

					lScaDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDAO.setDataAggiornamento(aEvento.getDataAggiornamento());
					lScaMod.setFlagVisto("N");
					// lScaModAgg.setIdScadenzario(lScaMod.getIdScadenzario());
					lScaDAO.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDAO.update();
					lScaDAO.stop();
				}
			}

			// ======================================
			// Recupero di annotazioni manuali
			// ======================================
			lAnnManualeDAO = new AnnotazioneManualeDAO(lConn);
			AnnotazioneManualeSqlDAO lAnnManuSqlDAO = new AnnotazioneManualeSqlDAO(lConn);
			lAnnManuSqlDAO.ricercaAnnotazioneManualeByIdEvento(aEvento.getIdEvento());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ricerca_annotazione_manuale_per_evento" + aEvento.getIdEvento());
			Vector lAnnVec = new Vector(lAnnManuSqlDAO.getModels());
			AnnotazioneManualeModel lAnnManMod = null;
			// AnnotazioneManualeModel lAnnAR = null;

			lPenResDao = new PenaResiduaDAO(lConn);
			for (int i = 0; i < lAnnVec.size(); i++) {
				lAnnManMod = (AnnotazioneManualeModel) lAnnVec.get(i);
				lAnnManMod.setFlagValidato("S");
				lAnnManMod.setDataAggiornamento(aEvento.getDataAggiornamento());
				lAnnManMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lAnnManMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lAnnManualeDAO.setDAOFromModelForUpdate(lAnnManMod);
				lAnnManualeDAO.update();
				lAnnManualeDAO.stop();

				// ======================================================================
				// Aggiorno la riga della tabella misure_cautelare_bdmc
				// legata all'annotazione in esame
				// ======================================================================

				MisuraCautelareBdmcModel lMisCautBdmc = new MisuraCautelareBdmcModel();
				lMisCautBdmc.setIdAnnotazioneManuale(lAnnManMod.getIdAnnotazioneManuale());
				lMisCautBdmc.setFlagStato("I");
				IMisuraCautelareBdmc lCtrMisCauBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
				Vector misCautBdmc = lCtrMisCauBdmc.ExRicercaMisuraCautelareBdmc(lMisCautBdmc);
				if (misCautBdmc != null && misCautBdmc.size() == 1) {
					MisuraCautelareBdmcModel lMisBdmc = (MisuraCautelareBdmcModel) misCautBdmc.get(0);
					lMisBdmc.setEveIdEvento(lAnnManMod.getEveIdEvento());
					lMisBdmc.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lMisBdmc.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lMisBdmc.setDataAggiornamento(aEvento.getDataAggiornamento());
					lMisBdmc.setFlagStato("V");
					lMisBdmc.setStatoTrasmissioneVal("N");
					IMisuraCautelareBdmc lCtrMisBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
					lCtrMisBdmc.ExModificaMisuraCautelareBdmcNoCommit(lConn, lMisBdmc, aEvento);

				} else {
					if (misCautBdmc != null && misCautBdmc.size() != 0)
						throw new F3BException(
								"E' stata riscontrata un'incongruenza dei dati legati alla tabella Annotazione_Manuale e tabella per tramissione dati a BDMC");
				}

				FungibilitaModel lModelFung = new FungibilitaModel();
				if (lAnnManMod.getFunIdFungibilita() != null) {
					lFunSql.ricercaFungibilitaByKey(lAnnManMod.getFunIdFungibilita());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("ricerca fungibilità per annotazione manuale "
							+ lAnnManMod.getFunIdFungibilita());

					lModelFung = (FungibilitaModel) lFunSql.getModelByKey();
					lFunSql = new FungibilitaSqlDAO(lConn);
					lFunDAO = new FungibilitaDAO(lConn);
					if (lModelFung != null) {
						lModelFung.setFlagValidato("S");
						lModelFung.setDataAggiornamento(aEvento.getDataAggiornamento());
						lModelFung.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
						lModelFung.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

						lFunDAO.setDAOFromModelForUpdate(lModelFung);
						lFunDAO.update();
						lFunDAO.stop();
					}
				}
			}

			// ============================================================
			// Valido la pena residua se non già validata
			// ============================================================
			lUltimaPenaResidua.setEveIdEvento(aEvento.getIdEvento());
			lUltimaPenaResidua.setFlagValidato("S");

			lUltimaPenaResidua.setDataAggiornamento(aEvento.getDataAggiornamento());
			lUltimaPenaResidua.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lUltimaPenaResidua.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

			lPenResSqlDao.inserisciOModificaPenaResidua(lUltimaPenaResidua);

			/*
			 * lUltimaPenaResidua.setEveIdEvento(aEvento.getIdEvento());
			 * lUltimaPenaResidua.setFlagValidato("S"); lUltimaPenaResidua.setDataAggiornamento
			 * (aEvento.getDataAggiornamento()); lUltimaPenaResidua.setCodUfficioAggiornamento
			 * (aEvento.getCodUfficioAggiornamento()); lUltimaPenaResidua.setCodOperatoreAggiornamento
			 * (aEvento.getCodOperatoreAggiornamento());
			 * lPenResDao.setDAOFromModelForUpdate(lUltimaPenaResidua); lPenResDao.update();
			 * lPenResDao.stop();
			 */

			// ========================================================================
			// Commentata il 19/07/2007 perchè non si capisce cosa faccia,
			// e comunque va in update di tutti i record PR e FUNG di tutti i fascicoli
			// del DB
			// ========================================================================
			// lAnnManuSqlDAO.ricercaAnnotazioneManualeFlagAppProvByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("ricerca annotazione manuale A e R per fascicolo");
			//
			// Vector lAnnVectAR = new Vector(lAnnManuSqlDAO.getModels());
			// if (lAnnVectAR.size() > 0)
			// {
			// lAnnAR = (AnnotazioneManualeModel) lAnnVectAR.get(0);
			// if (lAnnAR.getPenResIdPenaResidua() != null)
			// {
			// PenaResiduaModel lPenModDel = new PenaResiduaModel();
			// lPenModDel.setIdPenaResidua(lAnnAR.getPenResIdPenaResidua());
			// lPenResDao.setCondizione(lPenModDel);
			// lPenResDao.delete();
			// }
			// if (lAnnAR.getFunIdFungibilita() != null)
			// {
			// FungibilitaModel lFunModDel = new FungibilitaModel();
			// lFunModDel.setIdFungibilita(lAnnAR.getFunIdFungibilita());
			// lFunDAO.setCondizione(lFunModDel);
			// lFunDAO.delete();
			// }
			// }

			// ==============================
			// STATO_PROCEDIMENTO
			// 0128 - Emesso Decreto di Computo Custodia Cautelare
			// ==============================
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoProcMod.setCodStatoProcedimento("0128");
			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveApp.getDataEmissione());
			lStatoDao.setDAOFromModel(lStatoProcMod);

			lStatoDao.insert();
			lStatoDao.stop();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();

			// =====================================================
			// SE IN MISURA ALTERNATIVA - aggiorno il fine misura
			// =====================================================
			if (lPosMod.isMisAlt()) {
				lMisAltSqlDao = new MisuraAlternativaSqlDAO(lConn);
				lMisAltDao = new MisuraAlternativaDAO(lConn);
				// lMisAltSqlDao.ricercaMisuraAlternativaByFascicoloOrdinanza(aFascicolo.getIdFascicoloSiep());
				lMisAltSqlDao.ricercaMisuraAlternativaCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisAltSqlDao.getModelByKey();

				if (lMisModel != null) {
					// Se la DATA_FINE_MISURA è successiva alla
					// DATA_FINE_PENA aggiorna la
					if (lMisModel.getDataFineMisura() != null && lUltimaPenaResidua != null
							&& lUltimaPenaResidua.getDataFine() != null
							&& lMisModel.getDataFineMisura().after(lUltimaPenaResidua.getDataFine())) {
						lMisModel.setDataFineMisura(lUltimaPenaResidua.getDataFine());

						lMisModel.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lMisModel.setDataAggiornamento(aEvento.getDataAggiornamento());
						lMisModel.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

						lMisAltDao.setDAOFromModelForUpdate(lMisModel);
						lMisAltDao.update();
						lMisAltDao.stop();

						// inserisco duplico occorrenza MA collegandola al provvedimneto di computo
						lMisModel.setEveIdEvento(aEvento.getIdEvento());
						lMisAltDao.setDAOFromModel(lMisModel);
						lMisAltDao.insert();
					}
				}
			}

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("AnnotazioneManualeController.ExUpdateFungibilita : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			rollback(lConn);
			// rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException("AnnotazioneManualeController.ExUpdateFungibilita : " + ex);
		} finally {
			cleanup(lFunDAO);
			cleanup(lAnnManualeDAO);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lPosSqlDAO);
			cleanup(lNomProvvDAO);
			cleanup(lScaSqlDAO);
			cleanup(lEveDaoBlob);
			cleanup(lFunSql);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lScaDAO);
			cleanup(lStatoDao);
			cleanup(lMisAltSqlDao);
			cleanup(lMisAltDao);

			cleanup(lConn);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * Effettua la Validazione del Provvedimento/Comunicazione di Rideterminazione Pena - Altro - - -
	 *
	 * @param
	 * @param
	 */
	public EventoModel ExUpdateRideterminazionePenaAltro(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		AnnotazioneManualeDAO lAnnManualeDAO = null;
		AnnotazioneManualeSqlDAO lAnnManuSqlDAO = null;
		ScadenzarioSqlDAO lScaSqlDAO = null;
		ScadenzarioDAO lScaDAO = null;
		// StatoProcedimentoDAO lStatoDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisAltSqlDao = null;
		MisuraAlternativaDAO lMisAltDao = null;
		FungibilitaSqlDAO lFunSql = null;
		FungibilitaDAO lFunDAO = null;
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);
			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());

				lEveApp.setCodMotivo(lEveDao.getCodMotivo());
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();

			// Cerca PENA RESIDUA LEGATA ALL'EVENTO
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEvento.getIdEvento());
			PenaResiduaModel lPenaresidua = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// Valido la pena residua legata all'evento
			lPenResDao = new PenaResiduaDAO(lConn);
			if (lPenaresidua != null && lPenaresidua.getIdPenaResidua() != null) {
				lPenaresidua.setFlagValidato("S");
				if (lPenaresidua.getDataFinePresunta() != null) {
					lPenaresidua.setDataFine(lPenaresidua.getDataFinePresunta());
				}
				lPenaresidua.setDataAggiornamento(aEvento.getDataAggiornamento());
				lPenaresidua.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPenaresidua.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPenResDao.setDAOFromModelForUpdate(lPenaresidua);
				lPenResDao.update();
				lPenResDao.stop();
			}

			// =========================================================
			// Aggiorno lo SCADENZARIO avendo modificato il fine pena
			// =========================================================
			lScaSqlDAO = new ScadenzarioSqlDAO(lConn);
			lScaDAO = new ScadenzarioDAO(lConn);

			ScadenzarioModel lScaMod = null;

			if (lPosMod.isLibero()) {
				// PARAMETRO
				ParametroModel lParMod = new ParametroModel();

				lParMod.setNomeParametro("VANE RICERCHE");
				lParMod.setCodUfficioValidita(aEvento.getCodUfficioAggiornamento());

				Vector lVectPar = null;
				IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
				lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

				Iterator lIter = lVectPar.iterator();
				Date lSommaAnni = null;
				Date lSommaMesi = null;
				Date lFineScadenza = null;

				if (lIter.hasNext()) {
					ParametroModel lParModel = (ParametroModel) lIter.next();
					lSommaAnni = DateUtils.moveDateTo(lEveApp.getDataEmissione(), java.util.Calendar.YEAR,
							lParModel.getAnni().intValue());
					lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
							lParModel.getMesi().intValue());
					lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
							lParModel.getGiorni().intValue());
				}

				lScaSqlDAO.ricercaScadenzarioByTipoScadenzarioIdFascicolo("03",
						aFascicolo.getIdFascicoloSiep()); // VANE RICERCHE
				lScaMod = (ScadenzarioModel) lScaSqlDAO.getModelByKey();
				// aggiorna scadenzario
				if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
					lScaDAO.setDataInizioScadenza(lEveApp.getDataEmissione());
					lScaDAO.setDataFineScadenza(lFineScadenza);
					lScaDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDAO.setDataAggiornamento(aEvento.getDataAggiornamento());
					lScaMod.setFlagVisto("N");

					lScaDAO.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDAO.update();
					lScaDAO.stop();
				}
			} else // diverso da libero
			{
				lScaSqlDAO.ricercaScadenzarioByTipoScadenzarioIdFascicolo("02",
						aFascicolo.getIdFascicoloSiep()); // FINE PENA
				lScaMod = (ScadenzarioModel) lScaSqlDAO.getModelByKey();
				// aggiorna scadenzario
				if (lScaMod != null && lScaMod.getIdScadenzario() != null && lPenaresidua != null
						&& lPenaresidua.getDataFine() != null) {
					lScaDAO.setDataFineScadenza(lPenaresidua.getDataFine());
					lScaDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDAO.setDataAggiornamento(aEvento.getDataAggiornamento());
					lScaMod.setFlagVisto("N");

					lScaDAO.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDAO.update();
					lScaDAO.stop();
				}
			}

			// =================================
			// Valida l'annotazione manuale
			// =================================

			lAnnManuSqlDAO = new AnnotazioneManualeSqlDAO(lConn);
			lAnnManuSqlDAO.ricercaAnnotazioneManualeByIdEvento(aEvento.getIdEvento());
			AnnotazioneManualeModel lAnnManMod = (AnnotazioneManualeModel) lAnnManuSqlDAO.getModelByKey();

			if (lAnnManMod != null && lAnnManMod.getIdAnnotazioneManuale() != null && lPenaresidua != null
					&& lPenaresidua.getIdPenaResidua() != null) {
				lAnnManualeDAO = new AnnotazioneManualeDAO(lConn);
				lAnnManMod.setFlagValidato("S");
				lAnnManMod.setPenResIdPenaResidua(lPenaresidua.getIdPenaResidua());

				lAnnManMod.setDataAggiornamento(aEvento.getDataAggiornamento());
				lAnnManMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lAnnManMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

				lAnnManualeDAO.setDAOFromModelForUpdate(lAnnManMod);
				lAnnManualeDAO.update();
				lAnnManualeDAO.stop();
			}

			// ========================================================================
			// Validazione fungibilità
			// Problema!! In fase di inserimento l'annotazione NON viene legata alla
			// fungibilità per cui quest'ultima non viene mai validata
			// ========================================================================
			lFunSql = new FungibilitaSqlDAO(lConn);
			lFunDAO = new FungibilitaDAO(lConn);

			FungibilitaModel lModelFung = new FungibilitaModel();
			if (lAnnManMod != null && lAnnManMod.getFunIdFungibilita() != null) {
				lFunSql.ricercaFungibilitaByKey(lAnnManMod.getFunIdFungibilita());

				lModelFung = (FungibilitaModel) lFunSql.getModelByKey();

				if (lModelFung != null) {
					lModelFung.setFlagValidato("S");
					lModelFung.setDataAggiornamento(aEvento.getDataAggiornamento());
					lModelFung.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lModelFung.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

					lFunDAO.setDAOFromModelForUpdate(lModelFung);
					lFunDAO.update();
					lFunDAO.stop();
				}
			}

			// ========================================================================
			// SE IN MISURA ALTERNATIVA, aggiorna la data fine misura con la nuova
			// data fine pena
			// ========================================================================
			if (lPosMod.isMisuraAlternativa() || lPosMod.isMisSosp()) {
				lMisAltSqlDao = new MisuraAlternativaSqlDAO(lConn);
				lMisAltDao = new MisuraAlternativaDAO(lConn);

				lMisAltSqlDao.ricercaMisuraAlternativaCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisAltSqlDao.getModelByKey();

				if (lMisModel != null) {
					if (lPenaresidua != null && lPenaresidua.getDataFine() != null) {
						lMisAltDao.setDAOFromModelForUpdate(lMisModel);
						lMisAltDao.setDataFineMisura(lPenaresidua.getDataFine());

						lMisAltDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lMisAltDao.setDataAggiornamento(aEvento.getDataAggiornamento());
						lMisAltDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

						lMisAltDao.update();
						lMisAltDao.stop();

					}
				}
			}

			// ** STATO_PROCEDIMENTO **/
			/*
			 * lStatoDao = new StatoProcedimentoDAO(lConn); STATO_PROCEDIMENTO da definire // - Cancella
			 * eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			 * lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep()); lStatoDao.delete();
			 * StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			 *
			 * lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			 * lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			 * lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			 * lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			 *
			 * lStatoProcMod.setCodStatoProcedimento("0128"); lStatoProcMod.setProgressivo(new BigDecimal(1));
			 * lStatoProcMod.setData(lEveApp.getDataEmissione()); lStatoDao.setDAOFromModel(lStatoProcMod);
			 *
			 * lStatoDao.insert(); lStatoDao.stop();
			 */

			// ======================
			// NOME PROVVEDIMENTO
			// ======================
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP208"); // Rideterminazione pena - altro
			lNomProvvDAO.setEveIdEvento(aEvento.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException(
					"AnnotazioneManualeController.ExUpdateRideterminazionePenaAltro : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			rollback(lConn);
			// rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException("AnnotazioneManualeController.ExUpdateRideterminazionePenaAltro : " + ex);
		} finally {
			cleanup(lAnnManualeDAO);
			cleanup(lAnnManuSqlDAO);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lPosSqlDAO);
			cleanup(lNomProvvDAO);
			cleanup(lScaSqlDAO);
			cleanup(lScaDAO);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			// cleanup(lStatoDao);
			cleanup(lMisAltSqlDao);
			cleanup(lMisAltDao);
			cleanup(lFunSql);
			cleanup(lFunDAO);
			cleanup(lEveDaoBlob);
			cleanup(lConn);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * Effettua la cancellazione delle annotazioni manuali <b>non ancora validate</b> dello stesso tipo di
	 * quelle passate in input. In funzione del tipo di annotazione cancella anche l'evento associato, e in
	 * caso di decisioni del GE anche l'ordinanza e relativa annotazione associata.
	 *
	 *
	 * @param aIdAnnotazioneManuale
	 * @param aFlagRichieste
	 *            - indica se trattasi di richieste al GE o decisioni/ computi. aFlagRichieste = true se
	 *            trattasi di richieste, false altrimenti
	 */
	public AnnotazioneManualeModel ExCancellaAnnotazioneManualeComputo(BigDecimal aIdAnnotazioneManuale,
			boolean aFlagRichieste) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeDAO lAnnDao = null;
		AnnotazioneManualeSqlDAO lAnnSqlDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoStoreProcedurePulisciDAO lEventoProc = null;

		AnnotazioneManualeModel lAnnMan = null;

		try {
			lConn = getDBTransaction();

			lAnnDao = new AnnotazioneManualeDAO(lConn);

			// Cancella l'Annotazioni Manuale
			// ========================================================================
			// Recupera l'annotazione passata in input
			// ========================================================================
			lAnnDao.setIdAnnotazioneManuale(aIdAnnotazioneManuale);
			lAnnDao.selByKey();
			lAnnMan = (AnnotazioneManualeModel) lAnnDao.getModelByKey();

			if (lAnnMan == null)
				throw new F3BException("Dati non trovati. Impossibile cancellare");

			// ========================================================================
			// Nel caso di Decisioni del GE amnistia/indulto, prima di cancellare tutte
			// le annotazioni legate alla richiesta corrente, devo aggiornare il
			// campo anno_id_annotazione_manuale delle Richieste che sono state
			// collegate alle decisioni correnti.
			// ========================================================================
			if (!aFlagRichieste && lAnnMan.getCodTipoAnnotazione() != null
					&& (lAnnMan.getCodTipoAnnotazione().equals("002") // indulto
							|| lAnnMan.getCodTipoAnnotazione().equals("003") // amnistia
					)) {
				// recupero le annotazioni legate all'evento corrente che verranno
				// cancellate
				Vector lListaDecisioni = new Vector();
				lAnnSqlDao = new AnnotazioneManualeSqlDAO(lConn);
				lAnnSqlDao.ricercaAnnotazioneManualeByIdEvento(lAnnMan.getEveIdEvento());
				lListaDecisioni = new Vector(lAnnSqlDao.getModels());
				lAnnSqlDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("");
				// Per ogni Richiesta che punta una Decisione che sta per essere cancellata
				// elimino il collegamento
				for (int i = 0; i < lListaDecisioni.size(); i++) {
					AnnotazioneManualeModel lAnnModDecisione = (AnnotazioneManualeModel) lListaDecisioni
							.elementAt(i);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("");

					lAnnDao.setCondizioneLinkDecisione(lAnnModDecisione.getIdAnnotazioneManuale());
					lAnnDao.setAnnoIdAnnotazioneManuale(null);
					lAnnDao.update();
					lAnnDao.stop();
				}
			}

			lEventoProc = new EventoStoreProcedurePulisciDAO(lConn);

			// ========================================================================
			// Cancella tutte le annotazioni dello stesso tipo non ancora validate
			// ========================================================================
			if (!aFlagRichieste) {
				lAnnDao.setCondizioneCodTipoNonValidataByIdFascicolo(lAnnMan.getFasSieIdFascicoloSiep(),
						lAnnMan.getCodTipoAnnotazione());
			} else {
				// Cancello l'Evento NON validato (ce ne è uno soltanto
				// perchè non possono emettere nuovi eventi se presente uno non validato)
				// legato all'ANNOTAZIONE_MANUALE tramite
				// l'ANN_ID_ANNOTAZIONE_MANUALE su EVENTO
				lEveDao = new EventoDAO(lConn);
				lEveDao.selCondizioneAnnIdAnnotazioneManualeNonValidati(aIdAnnotazioneManuale);
				lEveDao.start();
				if (lEveDao.next()) {
					lEventoProc.setIdEvento(lEveDao.getIdEvento());
					lEventoProc.execute();
					lEventoProc.stop();
				}
				lEveDao.stop();

				lAnnDao.setCondizioneConSenzaRichiestaByIdFascicoloTipoAnnotazione(
						lAnnMan.getFasSieIdFascicoloSiep(), lAnnMan.getCodTipoAnnotazione());
			}

			lAnnDao.delete();
			lAnnDao.stop();

			// ========================================================================
			// Se trattasi di decisioni del GE, cancello anche l'ordinanza e la relativa
			// annotazione.
			// n.b. non esiste un legame fisico con l'ordinanza per cui devo cercare
			// l'ultima ordinanza non validata del tipo associabile all'annotazione
			// ========================================================================
			if (!aFlagRichieste && lAnnMan.getCodTipoAnnotazione() != null
					&& (lAnnMan.getCodTipoAnnotazione().equals("002") // indulto
							|| lAnnMan.getCodTipoAnnotazione().equals("003") // amnistia
							|| lAnnMan.getCodTipoAnnotazione().equals("004") // Depenalizzazione
							|| lAnnMan.getCodTipoAnnotazione().equals("013") // Incostituzionalità
							// MEV 37 - Inizio
							|| lAnnMan.getCodTipoAnnotazione().equals("017") // Illecito Amministrativo
					// MEV 37 - Fine
					)) {
				// Cerca l'ordinanza
				EventoModel lEveMod = new EventoModel();

				lEveMod.setCodTipoEvento("01");
				lEveMod.setCodTipoProvvedimento("03");

				String lMotivoProvvedimento = "";
				if (lAnnMan.getCodTipoAnnotazione().equals("002")
						|| lAnnMan.getCodTipoAnnotazione().equals("003")) {
					lMotivoProvvedimento = "0284";
				} else if (lAnnMan.getCodTipoAnnotazione().equals("004")
						// MEV 37 - Inizio
						|| lAnnMan.getCodTipoAnnotazione().equals("017"))
				// MEV 37 - Fine
				{
					lMotivoProvvedimento = "0285";
				} else if (lAnnMan.getCodTipoAnnotazione().equals("013")) {
					lMotivoProvvedimento = "0286";
				}

				lEveMod.setCodMotivo(lMotivoProvvedimento);
				lEveMod.setFasSieIdFascicoloSiep(lAnnMan.getFasSieIdFascicoloSiep());

				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoTipoCodMotProvNonValidato(lEveMod);
				EventoModel lEveModOrd = (EventoModel) (lEveSqlDao.getModelByKey());

				// Cancella l'ordinanza
				if (lEveModOrd != null && lEveModOrd.getIdEvento() != null) {
					lEventoProc.setIdEvento(lEveModOrd.getIdEvento());

					lEventoProc.execute();
					lEventoProc.stop();
				}
			}

			// ========================================================================
			// Cancella il provvedimento associato alle annotazioni manuali
			// ========================================================================
			lEventoProc.setIdEvento(lAnnMan.getEveIdEvento());

			lEventoProc.execute();
			lEventoProc.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);

			daoEx.printStackTrace();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExCancellaAnnotazioneManualeComputo: " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);

			ex.printStackTrace();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new F3BException("AnnotazioneManualeController.ExCancellaAnnotazioneManualeComputo: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lAnnSqlDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lEventoProc);

			cleanup(lConn);
		}

		return lAnnMan;
	}

	/**
	 * Aggiorna il flagComputabile sull'annotazione
	 *
	 * Utilizzata per le decisioni del GE
	 *
	 * @param aIdAnnotazione
	 * @throws F3BException
	 */
	public void ExUpdateAnnotazioneFlagComputabile(BigDecimal aIdAnnotazione) throws F3BException {

		Connection lConn = null;

		AnnotazioneManualeDAO lAnnDao = null;

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeDAO(lConn);
			lAnnDao.setCondizioneUpdate(aIdAnnotazione);
			// lAnnDao.setCondizioneLinkEvento(aIdAnnotazione);
			lAnnDao.setFlagComputabile("S");
			lAnnDao.update();
			lAnnDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"AnnotazioneManualeController.ExUpdateAnnotazioneFlagComputabile: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

	}

	/**
	 * Funzione per la validazione diretta del Provvedimento di Rideterminazione Pena - Altro nella nuova
	 * versione 4.0. - Validazione Evento - Validazione delle annotazioni manuali associate - Validazione
	 * della pena residua - validazione dell'eventuale fungibilità - Aggiornamento dello scadenzario -
	 * Eventuale aggiornamento data fine misura
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @param aConn
	 *            = eventuale connessione si cui lavorare. Se null ne crea una sua andando in commit o
	 *            rollback. Se <> null lavora sulla connessione in input senza committare. Serve per
	 *            consentire la validazione contestuale ai provvedimenti correlati (OE, OECS, OS, CNRP)
	 */
	public EventoModel ExValidaRideterminazionePenaAltro(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			Connection aConn) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		AnnotazioneManualeDAO lAnnManualeDAO = null;
		AnnotazioneManualeSqlDAO lAnnManuSqlDAO = null;
		ScadenzarioSqlDAO lScaSqlDAO = null;
		ScadenzarioDAO lScaDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		MisuraAlternativaSqlDAO lMisAltSqlDao = null;
		MisuraAlternativaDAO lMisAltDao = null;
		FungibilitaSqlDAO lFunSql = null;
		FungibilitaDAO lFunDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("start");
		try {
			if (aConn != null) {
				lConn = aConn;
			} else {
				lConn = getDBTransaction();
			}

			// Recupero la Data emissione dell'evento da validare (serve allo scadenzario)
			lEveDao = new EventoDAO(lConn);
			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();
			EventoModel lEveApp = (EventoModel) lEveDao.getModelByKey();
			lEveDao.stop();

			// Valido l'evento
			lEveDao.setFlagDocumentoRegistrato(aEvento.getFlagDocumentoRegistrato());

			lEveDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lEveDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lEveDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());

			lEveDao.update();
			lEveDao.stop();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();

			// Cerca PENA RESIDUA LEGATA ALL'EVENTO
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEvento.getIdEvento());
			PenaResiduaModel lPenaresidua = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// Valido la pena residua legata all'evento
			lPenResDao = new PenaResiduaDAO(lConn);
			if (lPenaresidua != null && lPenaresidua.getIdPenaResidua() != null) {
				lPenaresidua.setFlagValidato("S");

				lPenaresidua.setDataAggiornamento(aEvento.getDataAggiornamento());
				lPenaresidua.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPenaresidua.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

				lPenResDao.setDAOFromModelForUpdate(lPenaresidua);
				lPenResDao.update();
				lPenResDao.stop();
			}

			// =================================
			// Valida le annotazioni manuali
			// =================================
			lAnnManuSqlDAO = new AnnotazioneManualeSqlDAO(lConn);
			lAnnManuSqlDAO.ricercaAnnotazioneManualeByIdEvento(aEvento.getIdEvento());
			Vector lListaAnnotazioni = new Vector(lAnnManuSqlDAO.getModels());
			lAnnManuSqlDAO.stop();

			for (int i = 0; i < lListaAnnotazioni.size(); i++) {
				AnnotazioneManualeModel lAnnManMod = (AnnotazioneManualeModel) lListaAnnotazioni.elementAt(i);

				lAnnManualeDAO = new AnnotazioneManualeDAO(lConn);
				lAnnManMod.setFlagValidato("S");
				lAnnManMod.setPenResIdPenaResidua(lPenaresidua.getIdPenaResidua());

				lAnnManMod.setDataAggiornamento(aEvento.getDataAggiornamento());
				lAnnManMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lAnnManMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

				lAnnManualeDAO.setDAOFromModelForUpdate(lAnnManMod);
				lAnnManualeDAO.update();
				lAnnManualeDAO.stop();
			}

			// =========================================================
			// Aggiorno lo SCADENZARIO avendo modificato il fine pena
			// =========================================================
			lScaSqlDAO = new ScadenzarioSqlDAO(lConn);
			lScaDAO = new ScadenzarioDAO(lConn);

			ScadenzarioModel lScaMod = null;

			if (lPosMod.isLibero()) {
				// PARAMETRO
				ParametroModel lParMod = new ParametroModel();

				lParMod.setNomeParametro("VANE RICERCHE");
				lParMod.setCodUfficioValidita(aEvento.getCodUfficioAggiornamento());

				Vector lVectPar = null;
				IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
				lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

				Iterator lIter = lVectPar.iterator();
				Date lSommaAnni = null;
				Date lSommaMesi = null;
				Date lFineScadenza = null;

				if (lIter.hasNext()) {
					ParametroModel lParModel = (ParametroModel) lIter.next();
					lSommaAnni = DateUtils.moveDateTo(lEveApp.getDataEmissione(), java.util.Calendar.YEAR,
							lParModel.getAnni().intValue());
					lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
							lParModel.getMesi().intValue());
					lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
							lParModel.getGiorni().intValue());
				}

				lScaSqlDAO.ricercaScadenzarioByTipoScadenzarioIdFascicolo("03",
						aFascicolo.getIdFascicoloSiep()); // VANE RICERCHE
				lScaMod = (ScadenzarioModel) lScaSqlDAO.getModelByKey();

				// aggiorna scadenzario
				if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
					lScaDAO.setDataInizioScadenza(lEveApp.getDataEmissione());
					lScaDAO.setDataFineScadenza(lFineScadenza);
					lScaDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDAO.setDataAggiornamento(aEvento.getDataAggiornamento());
					lScaMod.setFlagVisto("N");

					lScaDAO.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDAO.update();
					lScaDAO.stop();
				}
			} else {
				// diverso da libero
				lScaSqlDAO.ricercaScadenzarioByTipoScadenzarioIdFascicolo("02",
						aFascicolo.getIdFascicoloSiep()); // FINE PENA
				lScaMod = (ScadenzarioModel) lScaSqlDAO.getModelByKey();

				// aggiorna scadenzario
				if (lScaMod != null && lScaMod.getIdScadenzario() != null && lPenaresidua != null
						&& lPenaresidua.getDataFine() != null) {
					lScaDAO.setDataFineScadenza(lPenaresidua.getDataFine());
					lScaDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDAO.setDataAggiornamento(aEvento.getDataAggiornamento());
					lScaMod.setFlagVisto("N");

					lScaDAO.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDAO.update();
					lScaDAO.stop();
				}
			}

			// ========================================================================
			// Validazione fungibilità
			// n.b. la fungibilità viene già validata sulla conferma del calcolo pena
			//
			// =======================================================================
			// !!!! Da verificare come viene gestita la fungibilità dal calcolo della pena
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cerco l'eventuale fungibilità");
			lFunSql = new FungibilitaSqlDAO(lConn);
			lFunDAO = new FungibilitaDAO(lConn);

			FungibilitaModel lModelFung = new FungibilitaModel();
			lFunSql.ricercaFungibilitaByKeyEvento(aEvento.getIdEvento());

			lModelFung = (FungibilitaModel) lFunSql.getModelByKey();

			if (lModelFung != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Valido la fungibilità: " + lModelFung.getIdFungibilita());
				lModelFung.setFlagValidato("S");
				lModelFung.setDataAggiornamento(aEvento.getDataAggiornamento());
				lModelFung.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lModelFung.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

				lFunDAO.setDAOFromModelForUpdate(lModelFung);
				lFunDAO.update();
				lFunDAO.stop();
			}

			// ========================================================================
			// SE IN MISURA ALTERNATIVA, aggiorna la data fine misura con la nuova
			// data fine pena
			// ========================================================================
			if (lPosMod.isMisuraAlternativa() || lPosMod.isMisSosp()) {
				lMisAltSqlDao = new MisuraAlternativaSqlDAO(lConn);
				lMisAltDao = new MisuraAlternativaDAO(lConn);

				// Paolo Cherubini 17-06-2011
				// sostituisco il metodo per ovviare al fatto che la misura alternativa viene sempre caricata
				// invece va caricata solo quando è legata ad un evento non annullato
				// lMisAltSqlDao.ricercaMisuraAlternativaCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				lMisAltSqlDao.ricercaMisuraAlternativaByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				// fine Paolo

				MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisAltSqlDao.getModelByKey();

				if (lMisModel != null) {
					if (lPenaresidua != null && lPenaresidua.getDataFine() != null) {
						lMisAltDao.setDAOFromModelForUpdate(lMisModel);
						lMisAltDao.setDataFineMisura(lPenaresidua.getDataFine());

						lMisAltDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lMisAltDao.setDataAggiornamento(aEvento.getDataAggiornamento());
						lMisAltDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

						lMisAltDao.update();
						lMisAltDao.stop();

					}
				}
			}

			// ======================
			// NOME PROVVEDIMENTO
			// ======================
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP208"); // Rideterminazione pena - altro
			lNomProvvDAO.setEveIdEvento(aEvento.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ========================================================================
			// ANNA ottobre 2010
			// Aggiorna i giorni di Lib Anticipata computati non elaborati ad E
			// ========================================================================
			if (lPenaresidua.getFlagErgastolo() == null || lPenaresidua.getFlagErgastolo().equals("N")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("DEVO AGGIORNARE IL FLAG DI LA Elaborata");
				LicenzaLibanticipataSqlDAO lLicSqlDao = null;

				DatiOperazioneModel lOperMod = new DatiOperazioneModel(aEvento.getCodOperatoreAggiornamento(),
						DateUtils.getSysDate(), aEvento.getCodUfficioAggiornamento(), "");

				lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("DEVO AGGIORNARE quindi lPenaresidua.getEveIdEvento()="
						+ lPenaresidua.getEveIdEvento());
				lLicSqlDao.updateFlagElaboratoByEveIdEvento(lPenaresidua.getEveIdEvento(), "E", lOperMod);
				// updateFlagElaboratoByIdFascicolo(lPenaresidua.getFasSieIdFascicoloSiep(), "N", "E");
				cleanup(lLicSqlDao);

			}
			// ========================================================================
			// fine ANNA ottobre 2010
			// ========================================================================

			// ========================================================================
			// MEV 29. Il provvedimento altra autorità viene inserito
			// non validato per cui devo validarlo qui
			// ========================================================================
			if (lEveApp.getEveIdEvento() != null) {
				lEveDao = new EventoDAO(lConn);
				lEveDao.setIdEvento(lEveApp.getEveIdEvento());
				lEveDao.selByKey();
				EventoModel lEveAltraAutorita = (EventoModel) lEveDao.getModelByKey();
				lEveDao.stop();

				if (lEveAltraAutorita.getFlagDocumentoRegistrato() == null
						|| lEveAltraAutorita.getFlagDocumentoRegistrato().equalsIgnoreCase("N")) {
					lEveDao.setFlagDocumentoRegistrato("S");

					lEveDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveDao.setDataAggiornamento(aEvento.getDataAggiornamento());

					lEveDao.selCondizioneUpdate(lEveAltraAutorita.getIdEvento());
					lEveDao.update();
					lEveDao.stop();
				}
			}

			// ========================================================================
			// Aggiornamento stato procedimento
			// MEV 29 - 06/2015 aggiunta gestione stato procedimento
			// Punto 8 - RIDETERMINAZIONE PENA – MANCATA GESTIONE STATO PROCEDIMENTO
			// ========================================================================
			// Solo in caso di validazione diretta effettuo qui l'eventuale aggiornamento
			// dello stato di esecuzione, altrimenti tale aggiornamento viene effettuato
			// dai controller chiamanti
			// Recupero la Pena residua, mi serve il fine pena per lo stato procedimento
			if (aConn == null) {
				// Attenzione! il modulo viene richamato anche per la validazione diretta
				// degli scomputi permesso
				String lCodStatoProcedimento = null;
				if (lEveApp.getCodMotivo().equals("0958") || lEveApp.getCodMotivo().equals("0996")) {
					// Scomputo permesso (0958, 0996)
					lCodStatoProcedimento = "0454"; // Pena rideterminata a seguito Scomputo Permesso il
				} else if (lEveApp.getCodMotivo().equals("0994") || lEveApp.getCodMotivo().equals("0997")) {
					// Reclamo su Scomputo Permesso (0994,0997)
					lCodStatoProcedimento = "0455"; // Pena rideterminata a seguito Reclamo Scomputo Permesso
													// il
				} else {
					lCodStatoProcedimento = "0453"; // Rideterminazione Pena il
				}

				// Se la pena legata all'evento è in decorrenza (fine pena <> null)
				// e lo stato procedimento attuale prevede la dicitura
				// "0010" = Pena in Esecuzione Fino al
				// Allora devo aggiornare lo stato procedimento per tenere allinea
				// Ergastolo???????

				if (lPenaresidua != null && lPenaresidua.getDataFine() != null
						&& !lPenaresidua.isErgastolo()) {
					// Recupero lo stato procedimento attuale per verificare se prevede
					// l'informazione: Pena in Esecuzione Fino al
					lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);
					lStatoSqlDao.ricercaStatoProcedimentoByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					Vector lElencoStatoProc = new Vector(lStatoSqlDao.getModels());

					for (int i = 0; i < lElencoStatoProc.size(); i++) {
						StatoProcedimentoModel lStatoModel = (StatoProcedimentoModel) lElencoStatoProc
								.elementAt(i);
						if ("0010".equals(lStatoModel.getCodStatoProcedimento())
								&& !DateUtils.isEquals(lStatoModel.getData(), lPenaresidua.getDataFine())) {
							// Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
							lStatoDao = new StatoProcedimentoDAO(lConn);
							lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
							lStatoDao.delete();

							// Inserisce lo stato procedimento
							StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

							lStatoProcMod.setProgressivo(new BigDecimal(1));
							lStatoProcMod.setCodStatoProcedimento(lCodStatoProcedimento);
							lStatoProcMod.setData(lEveApp.getDataEmissione());

							lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

							lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
							lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
							lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

							lStatoDao.setDAOFromModel(lStatoProcMod);
							lStatoDao.insert();
							lStatoDao.stop();

							// Pena in Esecuzione Fino al
							lStatoProcMod.setProgressivo(new BigDecimal(2));
							lStatoProcMod.setCodStatoProcedimento("0010"); // Pena in Esecuzione Fino al
							lStatoProcMod.setData(lPenaresidua.getDataFine());

							lStatoDao.setDAOFromModel(lStatoProcMod);
							lStatoDao.insert();
							lStatoDao.stop();

						}
					}
				}
			}

			if (aConn == null) {
				commit(lConn);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);

			daoEx.printStackTrace();

			throw new F3BException(
					"AnnotazioneManualeController.ExUpdateRideterminazionePenaAltro : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("AnnotazioneManualeController.ExUpdateRideterminazionePenaAltro : " + ex);
		} finally {
			cleanup(lAnnManualeDAO);
			cleanup(lAnnManuSqlDAO);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lPosSqlDAO);
			cleanup(lNomProvvDAO);
			cleanup(lScaSqlDAO);
			cleanup(lScaDAO);
			cleanup(lEveDao);
			// cleanup(lStatoDao);
			cleanup(lMisAltSqlDao);
			cleanup(lMisAltDao);
			cleanup(lFunSql);
			cleanup(lFunDAO);
			cleanup(lStatoDao);
			cleanup(lStatoSqlDao);

			if (aConn == null) {
				cleanup(lConn);
			}
		}

		return lEveMod;
	}

	@Override
	public AnnotazioneManualeModel ExRicercaAnnotazioneManualeByIdSentenza(BigDecimal aIdSentenza)
			throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;

		AnnotazioneManualeModel annotazioneManuale = null;

		try {
			lConn = getDBConnection();
			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeByIdSentenza(aIdSentenza);
			lAnnDao.start();
			if (lAnnDao.next())
				annotazioneManuale = (AnnotazioneManualeModel) lAnnDao.getModel();

			lAnnDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManualeByIdEvento: Non posso leggere : "
							+ daoEx);
		} catch (NullPointerException ne) {
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
		return annotazioneManuale;
	}

	@Override
	public AnnotazioneManualeModel ExRicercaAnnotazioneManualeByIdSentenzaIdTenoreSige(BigDecimal aIdSentenza,
			BigDecimal aIdTenoreSige) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		AnnotazioneManualeModel annotazioneManuale = null;

		try {
			lConn = getDBConnection();
			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeByIdSentenzaIdTenore(aIdSentenza, aIdTenoreSige);
			lAnnDao.start();
			if (lAnnDao.next())
				annotazioneManuale = (AnnotazioneManualeModel) lAnnDao.getModel();

			lAnnDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManualeByIdEvento: Non posso leggere : "
							+ daoEx);
		} catch (NullPointerException ne) {
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
		return annotazioneManuale;
	}

	@Override
	public Vector ExRicercaRichiesteTenoriSige(AnnotazioneManualeModel aAnnotazioneManuale)
			throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		Vector lAnnotazioneManuali = new Vector();

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaRichiesteTenoriSige(aAnnotazioneManuale);
			lAnnotazioneManuali = new Vector(lAnnDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaRichieste: Non posso leggere : " + daoEx);
		} catch (Exception sqe) {
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManualeGenerico: Non posso leggere -> "
							+ sqe);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
		return lAnnotazioneManuali;
	}

	@Override
	public void ExAggiornaFlagSelQuantum(String ids, String flag) throws F3BException {

		Connection lConn = null;
		AnnotazioneManualeSqlDAO lAnnDao = null;
		AnnotazioneManualeDAO lDao = null;
		Vector lAnnotazioneManuali = new Vector();

		try {
			lConn = getDBConnection();

			lAnnDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao.ricercaAnnotazioneManualeByIdsAnnMan(ids);
			lAnnotazioneManuali = new Vector(lAnnDao.getModels());

			for (Object annotazione : lAnnotazioneManuali) {
				lDao = new AnnotazioneManualeDAO(lConn);
				AnnotazioneManualeModel annotazioneModel = (AnnotazioneManualeModel) annotazione;
				annotazioneModel.setFlagSelQuantum(flag);
				lDao.setCondizioneUpdate(annotazioneModel.getIdAnnotazioneManuale());
				lDao.setDAOFromModelForUpdate(annotazioneModel);
				lDao.update();
				lDao.stop();
			}
			lConn.commit();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaRichieste: Non posso leggere : " + daoEx);
		} catch (Exception sqe) {
			throw new F3BException(
					"AnnotazioneManualeController.ExRicercaAnnotazioneManualeGenerico: Non posso leggere -> "
							+ sqe);
		} finally {
			cleanup(lAnnDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDao);
			cleanup(lConn);
		}
	}

}