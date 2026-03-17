package siap.siep.sanzionesostitutiva.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.stampa.controller.StampaSSController;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.pagoPA.dao.BollettinoPagopaDAO;
import siap.siep.pagoPA.dao.BollettinoPagopaSqlDAO;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.rateizzazionepp.dao.RateizzazionePPDAO;
import siap.siep.rateizzazionepp.dao.RateizzazionePPSqlDAO;
import siap.siep.rateizzazionepp.dao.RicercaStatoPagamentiSqlDao;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostResiduaDAO;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostResiduaSqlDAO;
import siap.siep.sanzionesostitutiva.model.RicercaStatoPagamentiModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.scadenzario.util.ScadenzarioUtils;
import siap.siep.sospensione.dao.SospensioneDAO;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.dao.VerbaleDAO;
import siap.siep.verbale.model.VerbaleModel;

/**
 * Controller della Sanzioni Sostitutive
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SanzioneSostitutivaController extends SiapController implements ISanzioneSostitutiva {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Metodo per registrare l'annotazione dell'avvenuta espulsione. Inserisce un evento Verbale, il verbale e
	 * l'evento di Comunicazione/Annotazione Inserisce la PENA_RESIDUA il record SOSPENSIONE.
	 *
	 * @param aEvVerbale
	 * @param aEvComunicazione
	 * @param aVerbale
	 * @param aPenaResiduaMod
	 *            - Pena residua calcolata al momento della sospensione
	 * @param aSospMod
	 *            - pena espiata e pena residua
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciAnnotazioneEspulsione(EventoModel aEvVerbale,
			EventoNotificaModel aEvNotComunicazione, VerbaleModel aVerbaleMod,
			PenaResiduaModel aPenaResiduaMod, SospensioneModel aSospMod) throws F3BException {

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		VerbaleDAO lVerbaleDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		SospensioneDAO lSospDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvNotComunicazione);

		try {
			lConn = getDBConnection();

			// =========================================
			// Inserisco l'evento verbale
			// =========================================
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setDAOFromModel(aEvVerbale);
			BigDecimal lIdEventoVerbale = lEventoDao.insert();
			lEventoDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lIdEventoVerbale = " + lIdEventoVerbale);

			// =========================================
			// Inserisco il Verbale
			// =========================================
			aVerbaleMod.setEveIdEvento(lIdEventoVerbale);
			lVerbaleDao = new VerbaleDAO(lConn);
			lVerbaleDao.setDAOFromModel(aVerbaleMod);
			BigDecimal lIdVerbale = lVerbaleDao.insert();
			lVerbaleDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lIdVerbale = " + lIdVerbale);

			// =========================================
			// Inserisco la Comunicazione/Annotazione
			// =========================================
			lEventoDao = new EventoDAO(lConn);
			aEvNotComunicazione.getEvento().setEveIdEvento(lIdEventoVerbale); // Collego l'evento al verbale
			lEventoDao.setDAOFromModel(aEvNotComunicazione.getEvento());
			BigDecimal lIdEventoComunicazione = lEventoDao.insert();
			lEventoDao.stop();
			lEveRet.getEvento().setIdEvento(lIdEventoComunicazione);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lIdEventoComunicazione = " + lIdEventoComunicazione);

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (aEvNotComunicazione != null && aEvNotComunicazione.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + aEvNotComunicazione.getNotifiche().length + " notifiche");

				while (count < aEvNotComunicazione.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("count = " + count);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.debug("Notifica[" + count + "] = " + aEvNotComunicazione.getNotifiche()[count]);

					if (aEvNotComunicazione.getNotifiche()[count] != null) {
						// Inserisco autorità esterna se non presente
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserisco autorità esterna se non presente");
						if (aEvNotComunicazione.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao = new AutoritaEsternaDAO(lConn);

							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Autorita = "
									+ aEvNotComunicazione.getNotifiche()[count].getAutoritaEsterna());
							lAutDao.setRicercaByAutSede(
									aEvNotComunicazione.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("Inserisco AUTORITA");
								lAutDao.setDAOFromModel(
										aEvNotComunicazione.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
								aEvNotComunicazione.getNotifiche()[count]
										.setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("AUTORITA presente");
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEvNotComunicazione.getNotifiche()[count]
										.setAutEstIdAutoritaEsterna(lKeyAutorita);
							}

							lAutDao.stop();
						}

						//
						aEvNotComunicazione.getNotifiche()[count].setEveIdEvento(lIdEventoComunicazione);

						lNotDao = new NotificaDAO(lConn);

						lNotDao.setDAOFromModel(aEvNotComunicazione.getNotifiche()[count]);
						BigDecimal lKeyNotifica = lNotDao.insert();
						lNotDao.stop();

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserita Notifica" + lKeyNotifica);
					}
					count++;
				}
			}

			// ========================================================================
			// Inserisco la PENA_RESIDUA.
			// n.b. se presente a sistema pena residua non validata e non collegata ad
			// alcun evento, la cancello. Situazione possibile se l'espulsione è
			// il primo evento ed è stato effettuato il primo calcolo della pena
			// ========================================================================
			if (aPenaResiduaMod != null) {
				// Elimino la pena residua non validata rimasta appesa se esiste
				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
				lPenResSqlDao.ricercaPenaResiduaFlagNonValidatoDesc(aEvVerbale.getFasSieIdFascicoloSiep());
				PenaResiduaModel lUltimaPenaNonValidata = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
				lPenResSqlDao.stop();

				if (lUltimaPenaNonValidata != null && lUltimaPenaNonValidata.getIdPenaResidua() != null
						&& lUltimaPenaNonValidata.getEveIdEvento() == null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Trovata pena residua (" + lUltimaPenaNonValidata.getIdPenaResidua()
							+ ") non validata non agganciata da alcun evento. La cancello.");
					lPenResDao = new PenaResiduaDAO(lConn);
					lPenResDao.setCondizioneUpdate(lUltimaPenaNonValidata.getIdPenaResidua());
					lPenResDao.delete();
					lPenResDao.stop();
				}

				lPenResDao = new PenaResiduaDAO(lConn);
				aPenaResiduaMod.setEveIdEvento(lIdEventoComunicazione);
				lPenResDao.setDAOFromModel(aPenaResiduaMod);
				BigDecimal lIdPenRes = lPenResDao.insert();
				lPenResDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lIdPenRes = " + lIdPenRes);

				// =======================================================
				// Inserisco la SOSPENSIONE
				// =======================================================
				if (aSospMod != null) {
					lSospDao = new SospensioneDAO(lConn);
					aSospMod.setPenResIdPenaResidua(lIdPenRes);
					lSospDao.setDAOFromModel(aSospMod);
					BigDecimal lIdSosp = lSospDao.insert();
					lSospDao.stop();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lIdSosp = " + lIdSosp);
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exInserisciAnnotazioneEspulsione: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException("SanzioneSostitutivaController.exInserisciAnnotazioneEspulsione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lVerbaleDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lSospDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Effettua la validazione della Cominucazione Scadenza Termini Espulsione e contestualmente del Verbale
	 * di Avvenuta Espulsione - Valida la Pena Residua - Modifica la posizione giuridica (26 - Espulso) -
	 * modifica lo stato procedimento (0240) - aggiorna lo scadenzario (15 - Espulsione)
	 *
	 * @param aEvComunicazione
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateAnnotazioneEspulsione(EventoModel aEvComunicazione) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Validazione Comunicazione Espulsione");

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		SospensioneSqlDAO lSospSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		ScadenzarioDAO lScaDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveRet = new EventoModel(aEvComunicazione);

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvComunicazione.getIdEvento());

			EventoModel lEveModelCom = (EventoModel) lEveSqlDAO.getModelByKey();
			lEveSqlDAO.stop();

			// ====================================
			// Recupero e valido l'evento verbale
			// ====================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero e valido l'evento verbale");
			lEveSqlDAO.ricercaEventoByKey(lEveModelCom.getEveIdEvento());
			EventoModel lEveVerbale = (EventoModel) lEveSqlDAO.getModelByKey();

			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setIdEvento(lEveVerbale.getIdEvento());
			lEventoDao.setFlagDocumentoRegistrato("S");

			lEventoDao.setDataAggiornamento(aEvComunicazione.getDataAggiornamento());
			lEventoDao.setCodOperatoreAggiornamento(aEvComunicazione.getCodOperatoreAggiornamento());
			lEventoDao.setCodUfficioAggiornamento(aEvComunicazione.getCodUfficioAggiornamento());

			lEventoDao.selByKey();
			lEventoDao.update();
			lEventoDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Evento Verbale (" + lEveVerbale.getIdEvento() + ") Validato");

			// ===================================
			// Recupero e valido la pena residua
			// ===================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero e valido la pena residua");
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lEveModelCom.getIdEvento());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Residua da validare " + lPenResMod.getIdPenaResidua());

			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			lPenResDao.setFlagValidato("S");
			lPenResDao.setDataAggiornamento(aEvComunicazione.getDataAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(aEvComunicazione.getCodUfficioAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(aEvComunicazione.getCodOperatoreAggiornamento());

			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" update Pena Residua avvenuto");

			// ========================================================================
			// Modifico la Posizione Giuridica
			// Aggiorno la data fine della vecchia Posizione Giuridica con la
			// data dell'espulsione e inserisco la nuova posizione
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Modifico la Posizione Giuridica");
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosizioneGiuridicaByIdFascicoloDesc(lEveModelCom.getFasSieIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());

			lPosDao.setDataFine(lEveVerbale.getDataEspulsioneSanzSost());

			lPosDao.selByKey();
			lPosDao.update();
			lPosDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornata vecchia Posizione giuridica " + lPosMod.getIdPosizioneGiuridica());

			// ========================================================================
			// Inserisco Nuova Posizione Giuridica (data_inizio = data espulsione)
			// agganciandola all'evento che sto validando
			// ========================================================================
			PosizioneGiuridicaModel lPosizione = new PosizioneGiuridicaModel();

			lPosizione.setCodPosizioneGiuridica("26"); // Espulso
			lPosizione.setDataInizio(lEveVerbale.getDataEspulsioneSanzSost());

			lPosizione.setCodOperatoreInserimento(aEvComunicazione.getCodOperatoreAggiornamento());
			lPosizione.setDataInserimento(aEvComunicazione.getDataAggiornamento());
			lPosizione.setCodUfficioInserimento(aEvComunicazione.getCodUfficioAggiornamento());

			lPosizione.setCodPosizioneProcessuale("-");
			lPosizione.setFasSieIdFascicoloSiep(lEveModelCom.getFasSieIdFascicoloSiep());
			lPosizione.setIdEventoRiferimento(aEvComunicazione.getIdEvento());

			lPosDao.setDAOFromModel(lPosizione);
			lPosDao.insert();
			lPosDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserita nuova posizione giuridica ");

			// ====================================
			// Modifico lo stato del procedimento
			// ====================================
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data espulsione
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento stato procedimento");
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setFasSieIdFascicoloSiep(lEveModelCom.getFasSieIdFascicoloSiep());

			lStatoProcMod.setData(lEveVerbale.getDataEspulsioneSanzSost());
			lStatoProcMod.setCodStatoProcedimento("0240"); // Eseguito provvedimento di espulsione
															// L.30.07.2002 n. 189 il

			lStatoProcMod.setCodOperatoreInserimento(aEvComunicazione.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvComunicazione.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvComunicazione.getCodUfficioAggiornamento());

			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(lEveModelCom.getFasSieIdFascicoloSiep());
			lStatoDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancellato old stato");
			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserito nuovo stato");

			// ========================================================================
			// Aggiorna tabella nome_provvedimento
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiorna tabella nome_provvedimento");
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento("NP219"); // Comunicazione Scadenza Termini Espulsione
															// L.30.07.2002 n. 189
			lNomProvvDAO.setEveIdEvento(aEvComunicazione.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiorna tabella nome_provvedimento avvenuto");

			// ================================
			// Inserisco lo scadenzario
			// ================================
			// Recupero la sospensione per data inizio e data fine sospensione
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento scadenzario");
			lSospSqlDao = new SospensioneSqlDAO(lConn);
			lSospSqlDao.ricercaSospensioneByIdPenaResidua(lPenResMod.getIdPenaResidua());
			SospensioneModel lSospMod = (SospensioneModel) lSospSqlDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recuperata sospensione " + lSospMod.getIdSospensione());

			// ========================================================================
			// Cancella tutti gli scadenzari di tipo LEGGE SIMEONE ( Tipo = 01 )
			// , come indicato nel Vision SIEP-Revisione Scadenzario Simeone
			// ========================================================================
			lScaDao = new ScadenzarioDAO(lConn);
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(lEveModelCom.getFasSieIdFascicoloSiep(),
					"01");
			lScaDao.delete();
			lScaDao.stop();

			// ===================================
			lScaDao = new ScadenzarioDAO(lConn);
			lScaDao.setCodTipoScadenzario("15"); // Espulsione

			lScaDao.setDataInizioScadenza(lSospMod.getDataInizio());
			lScaDao.setDataFineScadenza(lSospMod.getDataFine());

			lScaDao.setFasSieIdFascicoloSiep(lEveModelCom.getFasSieIdFascicoloSiep());
			lScaDao.setEveIdEvento(aEvComunicazione.getIdEvento());
			lScaDao.setCodStatoNotifica("N");

			lScaDao.setCodOperatoreInserimento(aEvComunicazione.getCodOperatoreAggiornamento());
			lScaDao.setCodUfficioInserimento(aEvComunicazione.getCodUfficioAggiornamento());
			lScaDao.setDataInserimento(aEvComunicazione.getDataAggiornamento());

			lScaDao.insert();
			lScaDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserito nuovo scadenzario");

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento Blob");
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvComunicazione);

			lEveDaoBlob.selCondizioneUpdate(aEvComunicazione.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Blob Aggiornato");

			// -----------------------
			commit(lConn);
			// commit(lConnBlob);
			// rollback(lConn);
			// rollback(lConnBlob);

		} catch (DAOException ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			throw new F3BException("SanzioneSostitutivaController.exUpdateAnnotazioneEspulsione: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException("SanzioneSostitutivaController.exUpdateAnnotazioneEspulsione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lEveSqlDAO);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lSospSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lPosDao);
			cleanup(lStatoDao);
			cleanup(lNomProvvDAO);
			cleanup(lScaDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveRet;
	}

	/**
	 * Inserisce Evento Annotazione Mancata Espulsione e Notifiche Comunicazione Sollecito
	 *
	 * @param aEvNotModel
	 * @param aVerbaleMod
	 *            - Verbale con i dati della nato mancata espulsione
	 * @param aCampoNota
	 *            - Eventuale nota scritta dall'utente
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciMancataEspulsione(EventoNotificaModel aEvNotModel,
			VerbaleModel aVerbaleMod, CampoNotaModel aCampoNota) throws F3BException {

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		VerbaleDAO lVerbaleDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvNotModel);

		try {
			lConn = getDBConnection();

			// =========================================
			// Inserisco l'evento
			// =========================================
			// lEventoDao = new EventoDAO(lConn);
			// lEventoDao.setDAOFromModel(aEvVerbale);
			// BigDecimal lIdEventoVerbale = lEventoDao.insert();
			// lEventoDao.stop();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("lIdEventoVerbale = "+lIdEventoVerbale);

			// =========================================
			// Inserisco la Comunicazione/Annotazione
			// =========================================
			lEventoDao = new EventoDAO(lConn);
			// aEvNotModel.getEvento().setEveIdEvento(lIdEventoVerbale); // Collego l'evento al verbale
			lEventoDao.setDAOFromModel(aEvNotModel.getEvento());
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();
			lEveRet.getEvento().setIdEvento(lIdEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lIdEvento = " + lIdEvento);

			// =========================================
			// Inserisco il Verbale
			// =========================================
			aVerbaleMod.setEveIdEvento(lIdEvento);
			lVerbaleDao = new VerbaleDAO(lConn);
			lVerbaleDao.setDAOFromModel(aVerbaleMod);
			BigDecimal lIdVerbale = lVerbaleDao.insert();
			lVerbaleDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lIdVerbale = " + lIdVerbale);

			// =========================================
			// Inserisco eventuale Campo Nota
			// =========================================
			if (aCampoNota != null) {
				lCampoNotaDao = new CampoNotaDAO(lConn);

				aCampoNota.setEveIdEvento(lIdEvento);

				lCampoNotaDao.setDAOFromModel(aCampoNota);

				lCampoNotaDao.insert();
				lCampoNotaDao.stop();

			}

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (aEvNotModel != null && aEvNotModel.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + aEvNotModel.getNotifiche().length + " notifiche");

				while (count < aEvNotModel.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("count = " + count);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + aEvNotModel.getNotifiche()[count]);

					if (aEvNotModel.getNotifiche()[count] != null) {
						// Inserisco autorità esterna se non presente
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserisco autorità esterna se non presente");
						if (aEvNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao = new AutoritaEsternaDAO(lConn);

							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug(
									"Autorita = " + aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
							lAutDao.setRicercaByAutSede(
									aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("Inserisco AUTORITA");
								lAutDao.setDAOFromModel(
										aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("AUTORITA presente");
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}

							lAutDao.stop();
						}

						//
						aEvNotModel.getNotifiche()[count].setEveIdEvento(lIdEvento);

						lNotDao = new NotificaDAO(lConn);

						lNotDao.setDAOFromModel(aEvNotModel.getNotifiche()[count]);
						BigDecimal lKeyNotifica = lNotDao.insert();
						lNotDao.stop();

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserita Notifica " + lKeyNotifica);
					}

					count++;
				}
			}

			// ========================================================================
			// Inserisco la PENA_RESIDUA.
			// n.b. La funzione non prevede il ricalcolo della pena
			// n.b. se presente a sistema pena residua non validata e non collegata ad
			// alcun evento, la aggancio. Altrimenti duplico l'ultima pena validata.
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserimento Pena Residua");
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaFlagNonValidatoDesc(
					aEvNotModel.getEvento().getFasSieIdFascicoloSiep());
			PenaResiduaModel lUltimaPenaNonValidata = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			lPenResSqlDao.stop();

			if (lUltimaPenaNonValidata != null && lUltimaPenaNonValidata.getIdPenaResidua() != null
					&& lUltimaPenaNonValidata.getEveIdEvento() == null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trovata pena residua (" + lUltimaPenaNonValidata.getIdPenaResidua()
						+ ") non validata non agganciata da alcun evento. La aggancio all'evento corrente");
				lPenResDao = new PenaResiduaDAO(lConn);

				lPenResDao.setEveIdEvento(lIdEvento);

				lPenResDao.setCodOperatoreInserimento(aEvNotModel.getEvento().getCodOperatoreInserimento());
				lPenResDao.setCodUfficioInserimento(aEvNotModel.getEvento().getCodUfficioInserimento());
				lPenResDao.setDataInserimento(aEvNotModel.getEvento().getDataInserimento());

				lPenResDao.setCondizioneUpdate(lUltimaPenaNonValidata.getIdPenaResidua());
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				// Cerco l'ultima validata per duplicarla
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nessuna pena non validata, duplico l'ultima pena validata");
				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepDataDesc(
						aEvNotModel.getEvento().getFasSieIdFascicoloSiep());
				PenaResiduaModel lUltimaPenaValidata = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
				lPenResSqlDao.stop();

				lUltimaPenaValidata.setIdPenaResidua(null);
				lUltimaPenaValidata.setEveIdEvento(lIdEvento);
				lUltimaPenaValidata.setFlagValidato("N");

				lUltimaPenaValidata
						.setCodOperatoreInserimento(aEvNotModel.getEvento().getCodOperatoreInserimento());
				lUltimaPenaValidata
						.setCodUfficioInserimento(aEvNotModel.getEvento().getCodUfficioInserimento());
				lUltimaPenaValidata.setDataInserimento(aEvNotModel.getEvento().getDataInserimento());

				// Inserisco
				lPenResDao = new PenaResiduaDAO(lConn);
				lPenResDao.setDAOFromModel(lUltimaPenaValidata);
				BigDecimal lIdPenRes = lPenResDao.insert();
				lPenResDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Pena Residua lIdPenRes = " + lIdPenRes);
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exInserisciMancataEspulsione: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException("SanzioneSostitutivaController.exInserisciMancataEspulsione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lVerbaleDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lCampoNotaDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Effettua la validazione della Annotazione Mancata Espulsione
	 *
	 * @param aEvAnnotazione
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateMancataEspulsione(EventoModel aEvAnnotazione) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Validazione Annotazione Mancata Espulsione");

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveRet = new EventoModel(aEvAnnotazione);

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvAnnotazione.getIdEvento());

			EventoModel lEveModelCom = (EventoModel) lEveSqlDAO.getModelByKey();
			lEveSqlDAO.stop();

			// ===================================
			// Recupero e valido la pena residua
			// ===================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero e valido la pena residua");
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lEveModelCom.getIdEvento());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Residua da validare " + lPenResMod.getIdPenaResidua());

			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			lPenResDao.setFlagValidato("S");
			lPenResDao.setDataAggiornamento(aEvAnnotazione.getDataAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(aEvAnnotazione.getCodUfficioAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(aEvAnnotazione.getCodOperatoreAggiornamento());

			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" update Pena Residua avvenuto");

			// ====================================
			// Modifico lo stato del procedimento
			// ====================================
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data emissione provvedimento (decreto)
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento stato procedimento");
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setFasSieIdFascicoloSiep(lEveModelCom.getFasSieIdFascicoloSiep());

			lStatoProcMod.setData(lEveModelCom.getDataEmissione());
			lStatoProcMod.setCodStatoProcedimento("0241"); // Mancata Espulsione Straniero a Titolo di
															// Sanzione Sostitutiva Alla Detenzione

			lStatoProcMod.setCodOperatoreInserimento(aEvAnnotazione.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvAnnotazione.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvAnnotazione.getCodUfficioAggiornamento());

			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(lEveModelCom.getFasSieIdFascicoloSiep());
			lStatoDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancellato old stato");
			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserito nuovo stato");

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento Blob");
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvAnnotazione);

			lEveDaoBlob.selCondizioneUpdate(aEvAnnotazione.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Blob Aggiornato");

			// -----------------------
			commit(lConn);
			// commit(lConnBlob);
		} catch (DAOException ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			throw new F3BException("SanzioneSostitutivaController.exUpdateMancataEspulsione: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException("SanzioneSostitutivaController.exUpdateMancataEspulsione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lEveSqlDAO);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lStatoDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveRet;
	}

	/**
	 * Inserisce Evento Richieste Revoca Espulsione e Notifiche al GE
	 *
	 * @param aEvNotModel
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciRichiestaRevocaEspulsione(EventoNotificaModel aEvNotModel)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		NotificaDAO lNotDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvNotModel);

		try {
			lConn = getDBConnection();

			// =========================================
			// Inserisco la Richiesta
			// =========================================
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setDAOFromModel(aEvNotModel.getEvento());
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();
			lEveRet.getEvento().setIdEvento(lIdEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lIdEvento = " + lIdEvento);

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;

			if (aEvNotModel != null && aEvNotModel.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + aEvNotModel.getNotifiche().length + " notifiche");

				while (count < aEvNotModel.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("count = " + count);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + aEvNotModel.getNotifiche()[count]);

					if (aEvNotModel.getNotifiche()[count] != null) {
						//
						aEvNotModel.getNotifiche()[count].setEveIdEvento(lIdEvento);

						lNotDao = new NotificaDAO(lConn);

						lNotDao.setDAOFromModel(aEvNotModel.getNotifiche()[count]);
						BigDecimal lKeyNotifica = lNotDao.insert();
						lNotDao.stop();

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserita Notifica " + lKeyNotifica);
					}

					count++;
				}
			}

			// ========================================================================
			// Inserisco la PENA_RESIDUA.
			// n.b. La funzione non prevede il ricalcolo della pena
			// n.b. se presente a sistema pena residua non validata e non collegata ad
			// alcun evento, la aggancio. Altrimenti duplico l'ultima pena validata.
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserimento Pena Residua");
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaFlagNonValidatoDesc(
					aEvNotModel.getEvento().getFasSieIdFascicoloSiep());
			PenaResiduaModel lUltimaPenaNonValidata = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			lPenResSqlDao.stop();

			if (lUltimaPenaNonValidata != null && lUltimaPenaNonValidata.getIdPenaResidua() != null
					&& lUltimaPenaNonValidata.getEveIdEvento() == null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trovata pena residua (" + lUltimaPenaNonValidata.getIdPenaResidua()
						+ ") non validata non agganciata da alcun evento. La aggancio all'evento corrente");
				lPenResDao = new PenaResiduaDAO(lConn);

				lPenResDao.setEveIdEvento(lIdEvento);

				lPenResDao.setCodOperatoreInserimento(aEvNotModel.getEvento().getCodOperatoreInserimento());
				lPenResDao.setCodUfficioInserimento(aEvNotModel.getEvento().getCodUfficioInserimento());
				lPenResDao.setDataInserimento(aEvNotModel.getEvento().getDataInserimento());

				lPenResDao.setCondizioneUpdate(lUltimaPenaNonValidata.getIdPenaResidua());
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				// Cerco l'ultima validata per duplicarla
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nessuna pena non validata, duplico l'ultima pena validata");
				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepDataDesc(
						aEvNotModel.getEvento().getFasSieIdFascicoloSiep());
				PenaResiduaModel lUltimaPenaValidata = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
				lPenResSqlDao.stop();

				lUltimaPenaValidata.setIdPenaResidua(null);
				lUltimaPenaValidata.setEveIdEvento(lIdEvento);
				lUltimaPenaValidata.setFlagValidato("N");

				lUltimaPenaValidata
						.setCodOperatoreInserimento(aEvNotModel.getEvento().getCodOperatoreInserimento());
				lUltimaPenaValidata
						.setCodUfficioInserimento(aEvNotModel.getEvento().getCodUfficioInserimento());
				lUltimaPenaValidata.setDataInserimento(aEvNotModel.getEvento().getDataInserimento());

				// Inserisco
				lPenResDao = new PenaResiduaDAO(lConn);
				lPenResDao.setDAOFromModel(lUltimaPenaValidata);
				BigDecimal lIdPenRes = lPenResDao.insert();
				lPenResDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Pena Residua lIdPenRes = " + lIdPenRes);
			}

			// rollback(lConn);
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"SanzioneSostitutivaController.exInserisciRichiestaRevocaEspulsione: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException(
					"SanzioneSostitutivaController.exInserisciRichiestaRevocaEspulsione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lNotDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Effettua la validazione della Richiesta Revoca SS
	 *
	 * @param aEvRichiesta
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateRichiestaRevocaSS(EventoModel aEvRichiesta) throws F3BException {

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveRet = new EventoModel(aEvRichiesta);

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvRichiesta.getIdEvento());

			EventoModel lEveModelRich = (EventoModel) lEveSqlDAO.getModelByKey();
			lEveSqlDAO.stop();

			// ===================================
			// Recupero e valido la pena residua
			// ===================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero e valido la pena residua");
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lEveModelRich.getIdEvento());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Residua da validare " + lPenResMod.getIdPenaResidua());

			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			lPenResDao.setFlagValidato("S");
			lPenResDao.setDataAggiornamento(aEvRichiesta.getDataAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(aEvRichiesta.getCodUfficioAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(aEvRichiesta.getCodOperatoreAggiornamento());

			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" update Pena Residua avvenuto");

			// ====================================
			// Modifico lo stato del procedimento
			// ====================================
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento stato procedimento");

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setFasSieIdFascicoloSiep(lEveModelRich.getFasSieIdFascicoloSiep());

			lStatoProcMod.setData(lEveModelRich.getDataEmissione());
			lStatoProcMod.setCodStatoProcedimento("0252");

			lStatoProcMod.setCodOperatoreInserimento(aEvRichiesta.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvRichiesta.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvRichiesta.getCodUfficioAggiornamento());

			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(lEveModelRich.getFasSieIdFascicoloSiep());
			lStatoDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancellato old stato");
			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserito nuovo stato");

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento Blob");
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvRichiesta);

			lEveDaoBlob.selCondizioneUpdate(aEvRichiesta.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Blob Aggiornato");

			// -----------------------
			commit(lConn);
			// commit(lConnBlob);
		} catch (DAOException ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			throw new F3BException("SanzioneSostitutivaController.exUpdateRichiestaRevocaSS: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException("SanzioneSostitutivaController.exUpdateRichiestaRevocaSS: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lEveSqlDAO);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lStatoDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveRet;
	}

	/**
	 * Effettua la validazione della Richiesta Revoca Espulsione
	 *
	 * @param aEvRichiesta
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateRichiestaRevocaEspulsione(EventoModel aEvRichiesta) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Validazione Richiesta Revoca Espulsione");

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveRet = new EventoModel(aEvRichiesta);

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvRichiesta.getIdEvento());

			EventoModel lEveModelRich = (EventoModel) lEveSqlDAO.getModelByKey();
			lEveSqlDAO.stop();

			// ===================================
			// Recupero e valido la pena residua
			// ===================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero e valido la pena residua");
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lEveModelRich.getIdEvento());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Residua da validare " + lPenResMod.getIdPenaResidua());

			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			lPenResDao.setFlagValidato("S");
			lPenResDao.setDataAggiornamento(aEvRichiesta.getDataAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(aEvRichiesta.getCodUfficioAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(aEvRichiesta.getCodOperatoreAggiornamento());

			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" update Pena Residua avvenuto");

			// ====================================
			// Modifico lo stato del procedimento
			// ====================================
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data emissione provvedimento (decreto)
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento stato procedimento");
			String lStatoProcedimento = "";
			if (lEveModelRich.getCodMotivo().equals("0932")) {
				lStatoProcedimento = "0246"; // Trasmessa richiesta di revoca espulsione per irreperibilità in
												// data
			} else if (lEveModelRich.getCodMotivo().equals("0933")) {
				lStatoProcedimento = "0247"; // Trasmessa richiesta di revoca espulsione per sanzione
												// applicata a cittadino comunitario
			} else if (lEveModelRich.getCodMotivo().equals("0938")) {
				lStatoProcedimento = "0250"; // Trasmessa richiesta di revoca espulsione per rientro nel
												// territorio dello stato senza autorizzazione
			} else if (lEveModelRich.getCodMotivo().equals("0939")) {
				lStatoProcedimento = "0251"; // Trasmessa richiesta di revoca espulsione
			}

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setFasSieIdFascicoloSiep(lEveModelRich.getFasSieIdFascicoloSiep());

			lStatoProcMod.setData(lEveModelRich.getDataEmissione());
			lStatoProcMod.setCodStatoProcedimento(lStatoProcedimento);

			lStatoProcMod.setCodOperatoreInserimento(aEvRichiesta.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvRichiesta.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvRichiesta.getCodUfficioAggiornamento());

			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(lEveModelRich.getFasSieIdFascicoloSiep());
			lStatoDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancellato old stato");
			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserito nuovo stato");

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento Blob");
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvRichiesta);

			lEveDaoBlob.selCondizioneUpdate(aEvRichiesta.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Blob Aggiornato");

			// -----------------------
			commit(lConn);
			// commit(lConnBlob);
			// rollback(lConn);
			// rollback(lConnBlob);

		} catch (DAOException ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			throw new F3BException("SanzioneSostitutivaController.exUpdateRichiestaRevocaEspulsione: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException("SanzioneSostitutivaController.exUpdateRichiestaRevocaEspulsione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lEveSqlDAO);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lStatoDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveRet;
	}

	/**
	 * Inserisce la comunicazione per il nuovo residuo pena
	 *
	 * @param aEvNotModel
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciComunicazioneNuovoResiduoPena(EventoNotificaModel aEvNotModel)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvNotModel);

		try {
			lConn = getDBConnection();

			// =========================================
			// Inserisco la Comunicazione
			// =========================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserimento la Comunicazione");
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setDAOFromModel(aEvNotModel.getEvento());
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();
			lEveRet.getEvento().setIdEvento(lIdEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lIdEvento = " + lIdEvento);

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserimento notifiche");
			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (aEvNotModel != null && aEvNotModel.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + aEvNotModel.getNotifiche().length + " notifiche");

				while (count < aEvNotModel.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("count = " + count);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + aEvNotModel.getNotifiche()[count]);

					if (aEvNotModel.getNotifiche()[count] != null) {
						// Inserisco autorità esterna se non presente
						if (aEvNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Inserisco autorità esterna se non presente");
							lAutDao = new AutoritaEsternaDAO(lConn);

							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug(
									"Autorita = " + aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
							lAutDao.setRicercaByAutSede(
									aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("Inserisco AUTORITA");
								lAutDao.setDAOFromModel(
										aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("AUTORITA presente");
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}

							lAutDao.stop();
						}

						// Inserisco la notifica
						aEvNotModel.getNotifiche()[count].setEveIdEvento(lIdEvento);

						lNotDao = new NotificaDAO(lConn);

						lNotDao.setDAOFromModel(aEvNotModel.getNotifiche()[count]);
						BigDecimal lKeyNotifica = lNotDao.insert();
						lNotDao.stop();

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserita Notifica " + lKeyNotifica);
					}

					count++;
				}
			}

			// ========================================================================
			// Inserisco la PENA_RESIDUA.
			// n.b. La funzione non prevede il ricalcolo della pena ma solo la sua comunicazione
			// n.b. se presente a sistema pena residua non validata e non collegata ad
			// alcun evento, la aggancio. Altrimenti duplico l'ultima pena validata.
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserimento Pena Residua");
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaFlagNonValidatoDesc(
					aEvNotModel.getEvento().getFasSieIdFascicoloSiep());
			PenaResiduaModel lUltimaPenaNonValidata = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			lPenResSqlDao.stop();

			if (lUltimaPenaNonValidata != null && lUltimaPenaNonValidata.getIdPenaResidua() != null
					&& lUltimaPenaNonValidata.getEveIdEvento() == null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trovata pena residua (" + lUltimaPenaNonValidata.getIdPenaResidua()
						+ ") non validata non agganciata da alcun evento. La aggancio all'evento corrente");
				lPenResDao = new PenaResiduaDAO(lConn);

				lPenResDao.setEveIdEvento(lIdEvento);

				lPenResDao.setCodOperatoreInserimento(aEvNotModel.getEvento().getCodOperatoreInserimento());
				lPenResDao.setCodUfficioInserimento(aEvNotModel.getEvento().getCodUfficioInserimento());
				lPenResDao.setDataInserimento(aEvNotModel.getEvento().getDataInserimento());

				lPenResDao.setCondizioneUpdate(lUltimaPenaNonValidata.getIdPenaResidua());
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				// Cerco l'ultima validata per duplicarla
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nessuna pena non validata, duplico l'ultima pena validata");
				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepDataDesc(
						aEvNotModel.getEvento().getFasSieIdFascicoloSiep());
				PenaResiduaModel lUltimaPenaValidata = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
				lPenResSqlDao.stop();

				lUltimaPenaValidata.setIdPenaResidua(null);
				lUltimaPenaValidata.setEveIdEvento(lIdEvento);
				lUltimaPenaValidata.setFlagValidato("N");

				lUltimaPenaValidata
						.setCodOperatoreInserimento(aEvNotModel.getEvento().getCodOperatoreInserimento());
				lUltimaPenaValidata
						.setCodUfficioInserimento(aEvNotModel.getEvento().getCodUfficioInserimento());
				lUltimaPenaValidata.setDataInserimento(aEvNotModel.getEvento().getDataInserimento());

				// Inserisco
				lPenResDao = new PenaResiduaDAO(lConn);
				lPenResDao.setDAOFromModel(lUltimaPenaValidata);
				BigDecimal lIdPenRes = lPenResDao.insert();
				lPenResDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Pena Residua lIdPenRes = " + lIdPenRes);
			}

			// rollback(lConn);
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"SanzioneSostitutivaController.exInserisciComunicazioneNuovoResiduoPena: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException(
					"SanzioneSostitutivaController.exInserisciComunicazioneNuovoResiduoPena: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Effettua la validazione della Comunicazione Nuovo Residuo Pena
	 *
	 * @param aEvComunicazione
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateComunicazioneNuovoResiduoPena(EventoModel aEvComunicazione)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Validazione Comunicazione Nuovo Residuo Pena");

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveRet = new EventoModel(aEvComunicazione);

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvComunicazione.getIdEvento());

			EventoModel lEveModelCom = (EventoModel) lEveSqlDAO.getModelByKey();
			lEveSqlDAO.stop();

			// ===================================
			// Recupero e valido la pena residua
			// ===================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero e valido la pena residua");
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lEveModelCom.getIdEvento());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Residua da validare " + lPenResMod.getIdPenaResidua());

			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			lPenResDao.setFlagValidato("S");
			lPenResDao.setDataAggiornamento(aEvComunicazione.getDataAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(aEvComunicazione.getCodUfficioAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(aEvComunicazione.getCodOperatoreAggiornamento());

			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" update Pena Residua avvenuto");

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento Blob");
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvComunicazione);

			lEveDaoBlob.selCondizioneUpdate(aEvComunicazione.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Blob Aggiornato");

			// -----------------------
			commit(lConn);
			// commit(lConnBlob);
			// rollback(lConn);
			// rollback(lConnBlob);
		} catch (DAOException ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			throw new F3BException(
					"SanzioneSostitutivaController.exUpdateComunicazioneNuovoResiduoPena: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException(
					"SanzioneSostitutivaController.exUpdateComunicazioneNuovoResiduoPena: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lEveSqlDAO);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveRet;
	}

	/**
	 * Effettua l'inserimento dell'OE a seguito revoca/conversione SS su fascicolo con cumulo. Inserisce la
	 * pena residua rideterminata
	 *
	 * @param aEvNotModel
	 * @param aPenResMod
	 *            pena rideterminata
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciOENuovoResiduoPena(EventoNotificaModel aEvNotModel,
			PenaResiduaModel aPenResMod) throws F3BException {

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaDAO lPenResDao = null;
		// PenaResiduaSqlDAO lPenResSqlDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvNotModel);

		try {
			lConn = getDBConnection();

			// =========================================
			// Inserisco l'OE
			// =========================================
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setDAOFromModel(aEvNotModel.getEvento());
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();
			lEveRet.getEvento().setIdEvento(lIdEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lIdEvento = " + lIdEvento);

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			lAutDao = new AutoritaEsternaDAO(lConn);
			BigDecimal lKeyAutorita = null;

			if (aEvNotModel != null && aEvNotModel.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + aEvNotModel.getNotifiche().length + " notifiche");

				while (count < aEvNotModel.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("count = " + count);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + aEvNotModel.getNotifiche()[count]);

					if (aEvNotModel.getNotifiche()[count] != null) {
						// Se è stata specificata anche l'autorità esterna per l'avvocato,
						// recupero l'id da inserire nella notifica
						// n.b. se autorità non presente la creo
						if (aEvNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
							// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
							// già l'autorità esterna specificata nella form (dalla form ho solo
							// codice e sede)
							lAutDao.setRicercaByAutSede(
									aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
													// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
													// istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.debug("Ins Aut Est = "
										+ aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
								lAutDao.setDAOFromModel(
										aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						// ===========================================
						aEvNotModel.getNotifiche()[count].setEveIdEvento(lIdEvento);

						lNotDao = new NotificaDAO(lConn);

						lNotDao.setDAOFromModel(aEvNotModel.getNotifiche()[count]);
						BigDecimal lKeyNotifica = lNotDao.insert();
						lNotDao.stop();

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserita Notifica " + lKeyNotifica);
					}

					count++;
				}
			}

			// ========================================================================
			// Inserisco la pena residua
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserimento Pena Residua");
			lPenResDao = new PenaResiduaDAO(lConn);

			aPenResMod.setEveIdEvento(lIdEvento);

			lPenResDao.setDAOFromModel(aPenResMod);
			BigDecimal lIdPenRes = lPenResDao.insert();
			lPenResDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Residua lIdPenRes = " + lIdPenRes);

			// //========================================================================
			// // Recupero la PENA_RESIDUA collegata all'annotazione che ha già
			// // rideterminato la pena
			// // n.b. La funzione non prevede il ricalcolo della pena
			// //========================================================================
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Inserimento Pena Residua");
			// lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			// lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEvNotModel.getEvento().getEveIdEvento());
			// PenaResiduaModel lPenaGiàRideterminata = (PenaResiduaModel)lPenResSqlDao.getModelByKey();
			// lPenResSqlDao.stop();
			//
			// if ( lPenaGiàRideterminata!=null
			// && lPenaGiàRideterminata.getIdPenaResidua()!=null
			// )
			// {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Recuperata pena residua ("+lPenaGiàRideterminata.getIdPenaResidua()+") non
			// validata non agganciata da alcun evento. La aggancio all'evento corrente");
			// lPenResDao = new PenaResiduaDAO(lConn);
			//
			// lPenaGiàRideterminata.setIdPenaResidua(null);
			// lPenaGiàRideterminata.setEveIdEvento(lIdEvento);
			// lPenaGiàRideterminata.setFlagValidato("N");
			//
			// lPenaGiàRideterminata.setCodOperatoreInserimento
			// (aEvNotModel.getEvento().getCodOperatoreInserimento());
			// lPenaGiàRideterminata.setCodUfficioInserimento
			// (aEvNotModel.getEvento().getCodUfficioInserimento());
			// lPenaGiàRideterminata.setDataInserimento (aEvNotModel.getEvento().getDataInserimento());
			//
			// // Inserisco
			// lPenResDao = new PenaResiduaDAO(lConn);
			// lPenResDao.setDAOFromModel(lPenaGiàRideterminata);
			// BigDecimal lIdPenRes = lPenResDao.insert();
			// lPenResDao.stop();
			//
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Pena Residua lIdPenRes = "+lIdPenRes);
			// }
			// else
			// {
			// //
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Nessuna pena trovata su annotazione");
			// }

			// rollback(lConn);
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exInserisciOENuovoResiduoPena: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException("SanzioneSostitutivaController.exInserisciOENuovoResiduoPena: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lPenResDao);
			// cleanup(lPenResSqlDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Effettua la validazione dell'OE a seguito revoca/conversione SS su fascicolo con cumulo
	 *
	 * @param aEventoModel
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateOENuovoResiduoPena(EventoModel aEventoModel) throws F3BException {

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScadeSqlDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveRet = new EventoModel(aEventoModel);

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEventoModel.getIdEvento());

			EventoModel lEveModel = (EventoModel) lEveSqlDAO.getModelByKey();
			lEveSqlDAO.stop();

			// ===================================
			// Recupero e valido la pena residua
			// ===================================

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lEveModel.getIdEvento());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			lPenResDao.setFlagValidato("S");
			lPenResDao.setDataAggiornamento(aEventoModel.getDataAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(aEventoModel.getCodUfficioAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(aEventoModel.getCodOperatoreAggiornamento());

			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			// ====================================
			// Modifico lo stato del procedimento
			// ====================================
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data emissione provvedimento (decreto)
			// ========================================================================

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setFasSieIdFascicoloSiep(lEveModel.getFasSieIdFascicoloSiep());

			lStatoProcMod.setData(lEveModel.getDataEmissione());
			if (lEveModel.getCodMotivo().equals("0397")) {
				lStatoProcMod.setCodStatoProcedimento("0236"); // Emesso Ordine di Esecuzione con Arresto in
																// seguito a Revoca Sanzione Sostitutiva il
			} else if (lEveModel.getCodMotivo().equals("0398")) {
				lStatoProcMod.setCodStatoProcedimento("0237"); // Emesso Ordine di Esecuzione in Carcere in
																// seguito a Revoca Sanzione Sostitutiva il
			} else if (lEveModel.getCodMotivo().equals("0399")) {
				lStatoProcMod.setCodStatoProcedimento("0238"); // Emesso ordine di esecuzione per
																// Rideterminazione Pena in seguito a Revoca
																// Sanzione Sostitutiva il
			} else if (lEveModel.getCodMotivo().equals("0935")) {
				lStatoProcMod.setCodStatoProcedimento("0249"); // Emesso ordine di esecuzione per
																// Rideterminazione Pena espiazione in Misura
																// Alternativa in seguito a Revoca Sanzione
																// Sostitutiva il
			}

			lStatoProcMod.setCodOperatoreInserimento(aEventoModel.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEventoModel.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEventoModel.getCodUfficioAggiornamento());

			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(lEveModel.getFasSieIdFascicoloSiep());
			lStatoDao.delete();

			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// ========================================================================
			// Modifico la Posizione Giuridica
			// Aggiorno la data fine della vecchia Posizione Giuridica con la
			// data provvedimento (data emissione) e inserisco la nuova posizione
			// n.b. la Pos giu cambia solo se 07 (in 10) altrimenti resta invariata
			// ========================================================================

			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosizioneGiuridicaByIdFascicoloDesc(lEveModel.getFasSieIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			if (lPosMod.getCodPosizioneGiuridica().equals("07")) {
				lPosDao = new PosizioneGiuridicaDAO(lConn);
				lPosDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());

				lPosDao.setDataFine(lEveModel.getDataEmissione());

				lPosDao.selByKey();
				lPosDao.update();
				lPosDao.stop();

				// ========================================================================
				// Inserisco Nuova Posizione Giuridica (data_inizio = data espulsione)
				// agganciandola all'evento che sto validando
				// ========================================================================
				PosizioneGiuridicaModel lPosizione = new PosizioneGiuridicaModel();

				lPosizione.setCodPosizioneGiuridica("10"); // Libero (dopo OE)

				lPosizione.setDataInizio(lEveModel.getDataEmissione());

				lPosizione.setCodOperatoreInserimento(aEventoModel.getCodOperatoreAggiornamento());
				lPosizione.setDataInserimento(aEventoModel.getDataAggiornamento());
				lPosizione.setCodUfficioInserimento(aEventoModel.getCodUfficioAggiornamento());

				lPosizione.setCodPosizioneProcessuale("-");
				lPosizione.setFasSieIdFascicoloSiep(lEveModel.getFasSieIdFascicoloSiep());
				lPosizione.setIdEventoRiferimento(lEveModel.getIdEvento());

				lPosDao.setDAOFromModel(lPosizione);
				lPosDao.insert();
				lPosDao.stop();
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"La Posizione Giuridica " + lPosMod.getCodPosizioneGiuridica() + " resta invariata");
			}

			// SCADENZARIO

			// AMBROSINO 08-02-2011 Scrivi SCADENZARIO solo se se non esiste Data Inizio/Fine Pena residua
			// (oppure la POSIZIONE_GIURIDICA LIBERO )

			if (lPenResMod.getDataFine() == null) {

				ParametroModel lParMod = new ParametroModel();

				lParMod.setNomeParametro("VANE RICERCHE");
				lParMod.setCodUfficioValidita(aEventoModel.getCodUfficioAggiornamento());

				Vector lVectPar = null;
				IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
				lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

				ScadenzarioModel lScaMod = new ScadenzarioModel();

				// a6-rr-238 - AMBROSINO Cerco NOTIFICA per prendere data trasmissione

				lNotEveDao = new NotificaEventoSqlDAO(lConn);
				lNotEveDao.ricercaNotificaByEvento(aEventoModel.getIdEvento());
				Vector lNotifiche = new Vector(lNotEveDao.getModels());
				NotificaModel lNotifica = (NotificaModel) lNotifiche.get(0);

				lScaMod.setFasSieIdFascicoloSiep(lPenResMod.getFasSieIdFascicoloSiep());
				lScaMod.setDataInizioScadenza(lNotifica.getDataInvio());

				Iterator lIter = lVectPar.iterator();
				Date lSommaAnni = null;
				Date lSommaMesi = null;
				Date lFineScadenza = null;

				if (lIter.hasNext()) {
					ParametroModel lParModel = (ParametroModel) lIter.next();
					lSommaAnni = DateUtils.moveDateTo(lScaMod.getDataInizioScadenza(),
							java.util.Calendar.YEAR, lParModel.getAnni().intValue());
					lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
							lParModel.getMesi().intValue());
					lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
							lParModel.getGiorni().intValue());
				}

				// MODIFICA
				lScaMod.setCodTipoScadenzario("03"); // Vane ricerche
				Vector lScadenzarii = null;
				lScadeSqlDao = new ScadenzarioSqlDAO(lConn);

				// cerca un scadenzario per id fascicolo e per tipo scadenzario

				lScadeSqlDao.ricercaScadenzarioVerbaleArresto(lScaMod);
				lScadenzarii = new Vector(lScadeSqlDao.getModels());

				lScaDao = new ScadenzarioDAO(lConn);

				if (lScadenzarii.size() == 0) {
					lScaMod.setFlagVisto("N");
					lScaMod.setDataFineScadenza(lFineScadenza);
					lScaMod.setCodOperatoreInserimento(aEventoModel.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioInserimento(aEventoModel.getCodUfficioAggiornamento());
					lScaMod.setDataInserimento(aEventoModel.getDataAggiornamento());
					// a6-rr-238
					lScaMod.setCodStatoNotifica("NP");
					lScaMod.setEveIdEvento(aEventoModel.getIdEvento());

					lScaDao.setDAOFromModel(lScaMod);
					// BigDecimal lKeyScad = null;
					/* lKeyScad = */lScaDao.insert();
				} else {
					ScadenzarioModel lScaModID = (ScadenzarioModel) lScadenzarii.get(0);
					lScaMod.setIdScadenzario(lScaModID.getIdScadenzario());
					lScaMod.setDataFineScadenza(lFineScadenza);
					lScaMod.setCodOperatoreAggiornamento(aEventoModel.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioAggiornamento(aEventoModel.getCodUfficioAggiornamento());
					lScaMod.setDataAggiornamento(aEventoModel.getDataAggiornamento());
					// a6-rr-238
					lScaMod.setCodStatoNotifica("NP");
					// AMBROSINO 30/06/2011
					lScaMod.setEveIdEvento(aEventoModel.getIdEvento());

					lScaDao.setDAOFromModelForUpdate(lScaMod);
					lScaDao.update();
				}
			}

			// END AMBROSINO

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento Blob");
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEventoModel);

			lEveDaoBlob.selCondizioneUpdate(aEventoModel.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Blob Aggiornato");

			// -----------------------
			commit(lConn);
			// commit(lConnBlob);
			// rollback(lConn);
			// rollback(lConnBlob);
		} catch (DAOException ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			throw new F3BException("SanzioneSostitutivaController.exUpdateOENuovoResiduoPena: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// rollback(lConnBlob);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException("SanzioneSostitutivaController.exUpdateOENuovoResiduoPena: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lEveSqlDAO);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lStatoDao);
			cleanup(lPosSqlDao);
			cleanup(lPosDao);
			cleanup(lScadeSqlDao);
			cleanup(lScaDao);
			cleanup(lNotEveDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveRet;
	}

	/**
	 * Restituisce l'ultima Sanzione Sostitutiva Residua per il fascicolo passato in input se esiste
	 *
	 * @param aIdFascicoloSiep
	 * @param aFlagValidata
	 *            . Se 'S' recupera l'ultima validata, se 'N' l'ultima non validata, se null l'ultima in
	 *            assoluto
	 * @return SanzioneSostResiduaModel o null se non presente una SS residua
	 * @throws F3BException
	 */
	public SanzioneSostResiduaModel getUltimaSSResidua(BigDecimal aIdFascicoloSiep, String aFlagValidata)
			throws F3BException {

		Connection lConn = null;

		SanzioneSostResiduaSqlDAO lSSSqlDAO = null;

		SanzioneSostResiduaModel lSSResiduaModel = null;

		try {
			lConn = getDBConnection();

			lSSSqlDAO = new SanzioneSostResiduaSqlDAO(lConn);

			lSSSqlDAO.ricercaUltimaSanzioneSostResiduaByIdFasc(aIdFascicoloSiep, aFlagValidata);

			lSSResiduaModel = (SanzioneSostResiduaModel) lSSSqlDAO.getModelByKey();
		} catch (DAOException ex) {
			throw new F3BException("SanzioneSostitutivaController.getUltimaSSResidua: " + ex);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException("SanzioneSostitutivaController.getUltimaSSResidua: " + ex);
		} finally {
			cleanup(lSSSqlDAO);

			cleanup(lConn);
		}

		return lSSResiduaModel;
	}

	/**
	 * Restituisce la SS residua collegata alla pena residua passata in input
	 *
	 * @param aIdPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public SanzioneSostResiduaModel getSSByIdPenaResidua(BigDecimal aIdPenaResidua) throws F3BException {

		Connection lConn = null;

		SanzioneSostResiduaSqlDAO lSSSqlDAO = null;

		SanzioneSostResiduaModel lSSResiduaModel = null;

		try {
			lConn = getDBConnection();

			lSSSqlDAO = new SanzioneSostResiduaSqlDAO(lConn);

			lSSSqlDAO.ricercaSanzioneSostResiduaByIdPenRes(aIdPenaResidua);

			lSSResiduaModel = (SanzioneSostResiduaModel) lSSSqlDAO.getModelByKey();

			lSSSqlDAO.stop();
		} catch (DAOException ex) {
			throw new F3BException("SanzioneSostitutivaController.getSSByIdPenaResidua: " + ex);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Eccezione Generica");
			throw new F3BException("SanzioneSostitutivaController.getSSByIdPenaResidua: " + ex);
		} finally {
			cleanup(lSSSqlDAO);

			cleanup(lConn);
		}
		return lSSResiduaModel;
	}

	public ByteArrayOutputStream exStampaSS(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			IEvento lEveCntrl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveMod = lEveCntrl
					.ExRicercaEventoNotificaByKey(aEvento.getEvento().getIdEvento());

			lEveMod.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());
			// QUI setto l'id del template con il nemo vero e proprio
			lEveMod.getEvento().setTemIdTemplate(lNomeTemplate);

			StampaSSController lStampaSS = new StampaSSController();
			TreeModel lTree = lStampaSS.prelevaDatiSanzioniSostitutive(lEveMod, aUtente);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

			ReportGenerator lReport = new ReportGenerator();
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			aEvento.getEvento().setDocBlobIn(lByteArrayInput);
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(this.getClass().getName() + ".exStampaSS: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	// 25/03/2008
	/**
	 * Ricerca Sanzioni Sost. Residua tramite chiave Fascicolo
	 *
	 * @param aIdFasicolo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaSanzioneSostResiduaByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;

		Vector lSSResidua = new Vector();
		SanzioneSostResiduaSqlDAO lSSRDao = null;

		try {
			lConn = getDBConnection();

			lSSRDao = new SanzioneSostResiduaSqlDAO(lConn);
			lSSRDao.ricercaSanzioneSostResiduaByIdFascicolo(aIdFascicolo);
			lSSResidua = new Vector(lSSRDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SanzioneSostitutivaController.ExRicercaSanzioneSostResiduaByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lSSRDao);
			cleanup(lConn);
		}
		return lSSResidua;
	}

	/**
	 * Inserisci i records di Sanzione Sost. Residua per JMS senza assegnare la sequence
	 *
	 * @param aSanzioneSostResidua
	 * @param lConn
	 * @return lCodEsito
	 * @throws F3BException
	 */
	public String ExInserisciSanzioniSostResidueWithoutSequence(ArrayList aSanzioneSostResidua,
			Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		SanzioneSostResiduaDAO lSSResDao = null;
		SanzioneSostResiduaModel lSSResMod = null;

		try {
			lSSResDao = new SanzioneSostResiduaDAO(lConn);

			if (aSanzioneSostResidua != null && aSanzioneSostResidua.size() > 0) {
				for (int i = 0; i < aSanzioneSostResidua.size(); i++) {
					lSSResMod = (SanzioneSostResiduaModel) aSanzioneSostResidua.get(i);
					if (lSSResMod != null) {
						if (lSSResMod.getIdSanzioneSostResidua() != null) {
							lSSResDao.setDAOFromModel(lSSResMod);
							lSSResDao.setWithoutSequence(true);
							lSSResDao.insert();
							lSSResDao.stop();
						}
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Sanzione Sost. Residua gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire la Sanzione Sost. Residua! ");
			}
		} finally {
			cleanup(lSSResDao);
		}
		return lCodEsito;
	}

	/**
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciOrdineIngiunzione(EventoNotificaModel aEvNotModel,
			String[] lArrayIdRate) throws F3BException {
		Connection lConn = null;

		EventoDAO lEventoDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;

		RateizzazionePPDAO lRateDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvNotModel);

		try {
			lConn = getDBConnection();

			// =========================================
			// Inserisco l'OE
			// =========================================
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setDAOFromModel(aEvNotModel.getEvento());
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();
			lEveRet.getEvento().setIdEvento(lIdEvento);
			siesLogger.debug("lIdEvento = " + lIdEvento);

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			lAutDao = new AutoritaEsternaDAO(lConn);
			BigDecimal lKeyAutorita = null;

			if (aEvNotModel != null && aEvNotModel.getNotifiche() != null) {
				siesLogger.debug("Presenti " + aEvNotModel.getNotifiche().length + " notifiche");

				while (count < aEvNotModel.getNotifiche().length) {
					siesLogger.debug("count = " + count);
					siesLogger.debug("Notifica[" + count + "] = " + aEvNotModel.getNotifiche()[count]);

					if (aEvNotModel.getNotifiche()[count] != null) {
						// Se è stata specificata anche l'autorità esterna per l'avvocato,
						// recupero l'id da inserire nella notifica
						// n.b. se autorità non presente la creo
						if (aEvNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
							// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
							// già l'autorità esterna specificata nella form (dalla form ho solo
							// codice e sede)
							lAutDao.setRicercaByAutSede(
									aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								siesLogger.debug("Ins Aut Est = "
										+ aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
								lAutDao.setDAOFromModel(
										aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						// ===========================================
						aEvNotModel.getNotifiche()[count].setEveIdEvento(lIdEvento);

						lNotDao = new NotificaDAO(lConn);

						lNotDao.setDAOFromModel(aEvNotModel.getNotifiche()[count]);
						BigDecimal lKeyNotifica = lNotDao.insert();
						lNotDao.stop();

						siesLogger.debug("Inserita Notifica " + lKeyNotifica);
					}

					count++;
				}
			}

			// =========================================================
			// Aggiorno le rate collegandole all'evento
			// =========================================================
			lRateDao = new RateizzazionePPDAO(lConn);
			for (int i = 0; i < lArrayIdRate.length; i++) {
				String idRata = lArrayIdRate[i];

				siesLogger.debug("idRata = " + idRata);

				lRateDao.setEveIdEvento(lIdEvento);
				lRateDao.selCondizioneUpdate(new BigDecimal(idRata));
				lRateDao.update();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exInserisciOrdineIngiunzione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exInserisciOrdineIngiunzione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lRateDao);

			// cleanup(lPenResDao);
			// cleanup(lPenResSqlDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 *
	 */
	public EventoModel exUpdateOrdineIngiunzione(EventoModel aEvento) throws F3BException {

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		StatoProcedimentoDAO lStatoDao = null;

		EventoDAO lEveDaoBlob = null;

		EventoModel lEveRet = new EventoModel(aEvento);

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvento.getIdEvento());

			EventoModel lEveModelRich = (EventoModel) lEveSqlDAO.getModelByKey();
			lEveSqlDAO.stop();

			// ====================================
			// Modifico lo stato del procedimento
			// ====================================
			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// ========================================================================
			siesLogger.debug("Aggiornamento stato procedimento");

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setFasSieIdFascicoloSiep(lEveModelRich.getFasSieIdFascicoloSiep());

			lStatoProcMod.setData(lEveModelRich.getDataEmissione());
			lStatoProcMod.setCodStatoProcedimento("0336");

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(lEveModelRich.getFasSieIdFascicoloSiep());
			lStatoDao.delete();
			siesLogger.debug("Cancellato old stato");
			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();
			siesLogger.debug("Inserito nuovo stato");

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			siesLogger.debug("Aggiornamento Blob");
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			siesLogger.debug("Blob Aggiornato");

			// -----------------------
			commit(lConn);
			// commit(lConnBlob);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exUpdateOrdineIngiunzione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exUpdateOrdineIngiunzione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lEveSqlDAO);
			cleanup(lStatoDao);

			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveRet;
	}

	public void exAggiornaNotificheOrdineIngiunzione(EventoModel aEvento,
			Vector<NotificaModel> aListaNotDaAggiornare) throws F3BException {

		Connection lConn = null;

		NotificaDAO lNotDAO = null;
		AutoritaEsternaDAO lAutDao = null;

		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;

		RateizzazionePPSqlDAO lRateSqlDao = null;

		BollettinoPagopaSqlDAO lBollSqlDao = null;
		BollettinoPagopaDAO lBollDao = null;

		Vector lVectNot = new Vector();

		try {
			lConn = getDBTransaction();

			lNotDAO = new NotificaDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);

			for (int i = 0; i < aListaNotDaAggiornare.size(); i++) {
				NotificaModel lNotModel = aListaNotDaAggiornare.elementAt(i);

				BigDecimal lKeyAutDeleg = null;
				if (lNotModel.getAutoritaEsternaDelegata() != null) {
					lAutDao = new AutoritaEsternaDAO(lConn);

					lAutDao.setRicercaByAutSede(lNotModel.getAutoritaEsternaDelegata());
					AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
					lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

					if (lAutMod == null) {
						// Autorità non presente, la inserisco
						lAutDao.setDAOFromModel(lNotModel.getAutoritaEsternaDelegata());
						lKeyAutDeleg = lAutDao.insert();
						lAutDao.stop();

						lNotModel.getAutoritaEsternaDelegata().setIdAutoritaEsterna(lKeyAutDeleg);
					} else {
						// Autorità già presente a sistema la aggancio alla notifixa
						lKeyAutDeleg = lAutMod.getIdAutoritaEsterna();

						// Il campo descrizione rappresenta l'indirizzo in maschera
						// della 'Autorita' che ha effettuato la notifica'
						lAutDao.setDescrizione(lNotModel.getAutoritaEsternaDelegata().getDescrizione());

						lAutDao.setCondizioneUpdate(lKeyAutDeleg);
						lAutDao.update();
						lAutDao.stop();

						lNotModel.getAutoritaEsternaDelegata().setIdAutoritaEsterna(lKeyAutDeleg); // ????
					}
				}

				// n.b. chi ha effettuato la notifica viene scritto su AutEstIdAutoritaEstDeleg mentre il
				// delegato iniziale su AutEstIdAutoritaEsterna
				lNotDAO.setAutEstIdAutoritaEstDeleg(lKeyAutDeleg);
				lNotDAO.setDataAvvenutaNotifica(lNotModel.getDataAvvenutaNotifica());
				lNotDAO.setCodEsito(lNotModel.getCodEsito());

				lNotDAO.setCodiceOperatoreAggiornamento(lNotModel.getCodiceOperatoreAggiornamento());
				lNotDAO.setCodUfficioAggiornamento(lNotModel.getCodUfficioAggiornamento());
				lNotDAO.setDataAggiornamento(lNotModel.getDataAggiornamento());

				lNotDAO.setCondizioneUpdate(lNotModel.getIdNotifica());

				lNotDAO.update();
				lNotDAO.stop();

				lVectNot.add(lNotModel);
				siesLogger.debug("lNotModel.getCodTipoNotifica() = " + lNotModel.getCodTipoNotifica());
				lScaSqlDao = new ScadenzarioSqlDAO(lConn);
				if ("E".equals(lNotModel.getCodTipoNotifica())) {
					siesLogger.debug("Notifica al condannato. Attivo/Cancello lo scadenzario");

					// 2023.11.20 - Cancello eventuali scadenzario 30 collegato ad altri eventi
					lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("30",
							aEvento.getFasSieIdFascicoloSiep());
					Vector<ScadenzarioModel> listaScadenzari = new Vector<ScadenzarioModel>(
							lScaSqlDao.getModels());
					siesLogger.debug("listaScadenzari.size() = " + listaScadenzari.size());
					for (ScadenzarioModel scadenzarioPrecedente : listaScadenzari) {
						if (scadenzarioPrecedente.getEveIdEvento().compareTo(aEvento.getIdEvento()) != 0
								&& "N".equals(scadenzarioPrecedente.getFlagVisto())) {
							siesLogger.debug("Annullo lo scadenzario con id = "
									+ scadenzarioPrecedente.getIdScadenzario());
							// lScaDao.setFlagVisto("S");
							// lScaDao.setDataAggiornamento(DateUtils.getSysDate());
							// lScaDao.setCodOperatoreAggiornamento(lNotModel.getCodOperatoreInserimento());
							// lScaDao.setCodUfficioAggiornamento(lNotModel.getCodUfficioInserimento());
							lScaDao.setCondizioneUpdate(scadenzarioPrecedente.getIdScadenzario());
							lScaDao.delete();
							// lScaDao.update();
							lScaDao.stop();
						}
					}
					// 2023.11.20 -

					Date dataScadenzaPrimaRata = null;

					if (lNotModel.getDataAvvenutaNotifica() == null) {
						siesLogger.debug("Notifica al condannato Rimossa, Cancello lo scadenzario");

						// 2023.11.20 - Dovrei ripristinare lo scadenzario precedente. Storicizzazione
						// attualmente non prevista

						lScaSqlDao.ricercaScadenzarioByIdEvento(aEvento.getIdEvento());
						ScadenzarioModel lScadenzarioAttuale = (ScadenzarioModel) lScaSqlDao.getModelByKey();

						if (lScadenzarioAttuale != null) {
							lScaDao.setCondizioneDelete(lScadenzarioAttuale.getIdScadenzario());
							lScaDao.delete();
							lScaDao.stop();
						}

					} else {
						siesLogger.debug(
								"Notifica al condannato Inserita/Modificata Attivo/Modifico lo scadenzario");

						// lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo ("30",
						// aEvento.getFasSieIdFascicoloSiep());
						lScaSqlDao.ricercaScadenzarioByIdEvento(aEvento.getIdEvento());
						ScadenzarioModel lScadenzarioAttuale = (ScadenzarioModel) lScaSqlDao.getModelByKey();

						// Notifica al condannato, devo attivare lo scadenzario
						lRateSqlDao = new RateizzazionePPSqlDAO(lConn);
						RateizzazionePPModel primaRata = null;

						lRateSqlDao
								.ricercaRateizzazionePPByIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
						Vector<RateizzazionePPModel> listaRate = new Vector<RateizzazionePPModel>(
								lRateSqlDao.getModels());
						for (RateizzazionePPModel rata : listaRate) {
							if (rata.getProgressivoRata().compareTo(new BigDecimal(1)) == 0) {
								primaRata = rata;
								break;
							}
						}

						dataScadenzaPrimaRata = DateUtils.moveDateTo(lNotModel.getDataAvvenutaNotifica(),
								Calendar.DAY_OF_MONTH, primaRata.getScadenzaGiorni().intValue());

						if (lScadenzarioAttuale == null) {
							siesLogger.debug("Notifica al condannato: scadenzario assente lo attivo");
							ScadenzarioModel lScadModel = new ScadenzarioModel();
							lScadModel.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
							lScadModel.setEveIdEvento(aEvento.getIdEvento());

							lScadModel.setCodTipoScadenzario("30");
							lScadModel.setDataInizioScadenza(lNotModel.getDataAvvenutaNotifica());
							lScadModel.setDataFineScadenza(dataScadenzaPrimaRata);

							lScadModel
									.setCodOperatoreInserimento(lNotModel.getCodiceOperatoreAggiornamento());
							lScadModel.setCodUfficioInserimento(lNotModel.getCodUfficioAggiornamento());
							lScadModel.setDataInserimento(lNotModel.getDataAggiornamento());

							lScadModel.setFlagVisto("N");

							lScaDao.setDAOFromModel(lScadModel);
							lScaDao.insert();
							lScaDao.stop();
						} else if (DateUtils.isEquals(lScadenzarioAttuale.getDataInizioScadenza(),
								lNotModel.getDataAvvenutaNotifica())) {
							siesLogger.debug(
									"Notifica al condannato: scadenzario gia' attivo con stessa data. non faccio nulla");
						} else {
							siesLogger.debug(
									"Notifica al condannato: scadenzario gia' attivo ma con data differente lo aggiorno");
							lScaDao.setDataInizioScadenza(lNotModel.getDataAvvenutaNotifica());
							lScaDao.setDataFineScadenza(dataScadenzaPrimaRata);

							lScaDao.setCodOperatoreAggiornamento(lNotModel.getCodiceOperatoreAggiornamento());
							lScaDao.setCodUfficioAggiornamento(lNotModel.getCodUfficioAggiornamento());
							lScaDao.setDataAggiornamento(lNotModel.getDataAggiornamento());

							lScaDao.setCondizioneUpdate(lScadenzarioAttuale.getIdScadenzario());
							lScaDao.update();
							lScaDao.stop();
						}
					}

					// MEV_2023-33: le date di scadenza delle rate successive vanno subito calcolate in fase
					// di
					// registrazione della notifica e non a seguito dall'avvenuto pagamento della
					// prima rata
					// Data scadenza ulteriori rata = ultimo del mese successivo alla scadenza della rata
					// precedente
					// Se presenti i bollettini aggiorno la data di scadenza
					lBollSqlDao = new BollettinoPagopaSqlDAO(lConn);
					// lBollSqlDao.ricercaBollettinoPagopaByFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep(),
					// null);
					lBollSqlDao.ricercaBollettinoPagopaByIdEvento(aEvento.getIdEvento());
					Vector<BollettinoPagopaModel> lListaBollettini = new Vector<BollettinoPagopaModel>(
							lBollSqlDao.getModels());

					lBollDao = new BollettinoPagopaDAO(lConn);
					if (lListaBollettini.size() > 0) {
						// n.b. per ora si procede all'aggiornamento delle scadenze indipendentemente se il
						// bollettino sia stato generato o meno o già pagato
						Date dataScadenzaRataSuccessiva = dataScadenzaPrimaRata;
						for (BollettinoPagopaModel lBoll : lListaBollettini) {
							siesLogger.debug("ProgRata = " + lBoll.getProgRata() + ", dataScadenza "
									+ dataScadenzaRataSuccessiva);

							lBollDao.setDataScadenza(dataScadenzaRataSuccessiva);
							lBollDao.setCondizioneUpdate(lBoll.getIdBollettinoPagopa());
							lBollDao.update();

							if (dataScadenzaRataSuccessiva != null)
								dataScadenzaRataSuccessiva = DateUtils
										.calcolaUtimoDelProxMese(dataScadenzaRataSuccessiva);
						}
					}

					/*
					 * if (lListaBollettini.size() > 0) { boolean isBollettiniGenerati = false; boolean
					 * isBollettiniPagati = false; // boolean isDataScadenzaCalcolata = false; for
					 * (BollettinoPagopaModel lBoll : lListaBollettini) { if (lBoll.getIuv() != null)
					 * isBollettiniGenerati = true; if (lBoll.getDataAvvPagamento() != null)
					 * isBollettiniPagati = true; // if (lBoll.getDataScadenza() != null) //
					 * isDataScadenzaCalcolata = true; }
					 *
					 * if (isBollettiniGenerati) { //if (!isBollettiniPagati && !isDataScadenzaCalcolata) { if
					 * (!isBollettiniPagati ) { // Calcolo la data scadenza e la aggiornao lBollDao = new
					 * BollettinoPagopaDAO(lConn); lBollDao.setDataScadenza(dataScadenzaPrimaRata);
					 * lBollDao.selCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
					 * lBollDao.update(); } else { // se il primo bollettino è stato già pagato allora le date
					 * dei successivi // sono state // calcolate in base al pagamento della prima rata. NON HA
					 * SENSO // MODIFICARE LE SCADENZA // Al più si potrebbe modificare SOLO la data scadenza
					 * del PRIMO // bollettino pagato } } else { // la potrei aggiornare comunque lBollDao =
					 * new BollettinoPagopaDAO(lConn); lBollDao.setDataScadenza(dataScadenzaPrimaRata);
					 * lBollDao.selCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
					 * lBollDao.update(); } }
					 */

				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			rollback(lConn);
			throw new F3BException(
					"SanzioneSostitutivaController.exAggiornaNotificheOrdineIngiunzione: daoEx --> " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(lConn);
			throw new F3BException(
					"SanzioneSostitutivaController.exAggiornaNotificheOrdineIngiunzione: ex --> " + ex);
		} finally {
			cleanup(lNotDAO);
			cleanup(lAutDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lRateSqlDao);
			cleanup(lBollSqlDao);
			cleanup(lBollDao);

			cleanup(lConn);
		}
	}

	/**
	 * Metodo per la modifica dell'ordine di ingiunzione. Effettua delete/insert
	 */
	public EventoNotificaModel exModificaOrdineIngiunzione(EventoNotificaModel aEvNotModel,
			String[] lArrayIdRate) throws F3BException {

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;

		RateizzazionePPDAO lRateDao = null;
		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvNotModel);

		try {
			lConn = getDBConnection();

			// Cancello preventivamente tutti i dati
			// Sgancio le rate dall'evento
			lRateDao = new RateizzazionePPDAO(lConn);
			lRateDao.setEveIdEvento(null);
			lRateDao.selCondizioneByIdEvento(aEvNotModel.getEvento().getIdEvento());
			lRateDao.update();

			lNotDao = new NotificaDAO(lConn);
			lNotDao.setCondizioneEvento(aEvNotModel.getEvento().getIdEvento());
			lNotDao.delete();

			lEventoDao = new EventoDAO(lConn);
			lEventoDao.selCondizioneUpdate(aEvNotModel.getEvento().getIdEvento());
			lEventoDao.delete();

			// =========================================
			// Inserisco l'OE
			// =========================================
			lEventoDao.setDAOFromModel(aEvNotModel.getEvento());
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();
			lEveRet.getEvento().setIdEvento(lIdEvento);
			siesLogger.debug("lIdEvento = " + lIdEvento);

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			lAutDao = new AutoritaEsternaDAO(lConn);
			BigDecimal lKeyAutorita = null;

			if (aEvNotModel != null && aEvNotModel.getNotifiche() != null) {
				siesLogger.debug("Presenti " + aEvNotModel.getNotifiche().length + " notifiche");

				while (count < aEvNotModel.getNotifiche().length) {
					siesLogger.debug("count = " + count);
					siesLogger.debug("Notifica[" + count + "] = " + aEvNotModel.getNotifiche()[count]);

					if (aEvNotModel.getNotifiche()[count] != null) {
						// Se è stata specificata anche l'autorità esterna per l'avvocato,
						// recupero l'id da inserire nella notifica
						// n.b. se autorità non presente la creo
						if (aEvNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
							// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
							// già l'autorità esterna specificata nella form (dalla form ho solo
							// codice e sede)
							lAutDao.setRicercaByAutSede(
									aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								lAutDao.setDAOFromModel(
										aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						// ===========================================
						aEvNotModel.getNotifiche()[count].setEveIdEvento(lIdEvento);

						lNotDao = new NotificaDAO(lConn);

						lNotDao.setDAOFromModel(aEvNotModel.getNotifiche()[count]);
						BigDecimal lKeyNotifica = lNotDao.insert();
						lNotDao.stop();

						siesLogger.debug("Inserita Notifica " + lKeyNotifica);
					}

					count++;
				}
			}

			// =========================================================
			// Aggiorno le rate collegandole all'evento
			// =========================================================
			lRateDao = new RateizzazionePPDAO(lConn);
			for (int i = 0; i < lArrayIdRate.length; i++) {
				String idRata = lArrayIdRate[i];

				siesLogger.debug("idRata = " + idRata);

				lRateDao.setEveIdEvento(lIdEvento);
				lRateDao.selCondizioneUpdate(new BigDecimal(idRata));
				lRateDao.update();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exModificaOrdineIngiunzione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exModificaOrdineIngiunzione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lRateDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * MEV_2023-33 Metodo per la ricerca (paginata) dei fascicoli SIEP dell'ufficio per stato pagamento
	 */
	public BigDecimal ExGetCountRicercaFascicoliPerStatoPagamento(FascicoloSiepModel aFascicolo,
			String aTipoRicerca) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		RicercaStatoPagamentiSqlDao ricercaStatoPagamentiSqlDao = null;
		try {
			lConn = getDBConnection();

			ricercaStatoPagamentiSqlDao = new RicercaStatoPagamentiSqlDao(lConn);

			ricercaStatoPagamentiSqlDao.getCountRicercaFascicoliPerStatoPagamento(aFascicolo, aTipoRicerca);
			ricercaStatoPagamentiSqlDao.start();
			ricercaStatoPagamentiSqlDao.next();
			lCount = ricercaStatoPagamentiSqlDao.getBigDecimal("HowManyRecords");
			ricercaStatoPagamentiSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"SanzioneSostitutivaController.ExGetCountRicercaFascicoliPerStatoPagamento: errore "
							+ daoEx);
		} finally {
			cleanup(ricercaStatoPagamentiSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * MEV_2023-33 Metodo per la ricerca (paginata) dei fascicoli SIEP dell'ufficio per stato pagamento
	 */
	public Vector ExRicercaFascicoliPerStatoPagamentoPaged(FascicoloSiepModel aFasMod, int aPagina,
			String aTipoRicera) throws F3BException {

		siesLogger.debug("ExRicercaFascicoloStatoPagamentoPaged");
		Connection lConn = null;
		RicercaStatoPagamentiSqlDao ricercaStatoPagamentiSqlDao = null;
		Vector elencoFascicoli = new Vector();

		try {
			lConn = getDBConnection();

			ricercaStatoPagamentiSqlDao = new RicercaStatoPagamentiSqlDao(lConn);

			ricercaStatoPagamentiSqlDao.ricercaFascicoliPerStatoPagamento(aFasMod, aPagina, aTipoRicera);

			elencoFascicoli = new Vector(ricercaStatoPagamentiSqlDao.getModels());

		} catch (DAOException ex) {
			throw new F3BException(
					"SanzioneSostitutivaController.ExRicercaFascicoliPerStatoPagamentoPaged: " + ex);
		} catch (Exception ex) {
			throw new F3BException(
					"SanzioneSostitutivaController.ExRicercaFascicoliPerStatoPagamentoPaged: " + ex);
		} finally {
			cleanup(ricercaStatoPagamentiSqlDao);

			cleanup(lConn);
		}

		return elencoFascicoli;
	}

	/**
	 * Inserise l'evento e la notifica
	 *
	 * @since MEV_2023-33
	 */
	public EventoNotificaModel exInserisciNotaTrasmissione(EventoNotificaModel aEvNotModel)
			throws F3BException {
		Connection lConn = null;

		EventoDAO lEventoDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvNotModel);

		try {
			lConn = getDBConnection();

			// =========================================
			// Inserisco la Nota di trasmissione
			// =========================================
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setDAOFromModel(aEvNotModel.getEvento());
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();

			lEveRet.getEvento().setIdEvento(lIdEvento);
			siesLogger.debug("lIdEvento = " + lIdEvento);

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			lAutDao = new AutoritaEsternaDAO(lConn);
			BigDecimal lKeyAutorita = null;

			while (count < aEvNotModel.getNotifiche().length) {
				siesLogger.debug("count = " + count);
				siesLogger.debug("Notifica[" + count + "] = " + aEvNotModel.getNotifiche()[count]);

				if (aEvNotModel.getNotifiche()[count] != null) {
					// Se è stata specificata anche l'autorità esterna per l'avvocato,
					// recupero l'id da inserire nella notifica
					// n.b. se autorità non presente la creo
					if (aEvNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
						// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
						// già l'autorità esterna specificata nella form (dalla form ho solo
						// codice e sede)
						lAutDao.setRicercaByAutSede(aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
							lAutDao.setDAOFromModel(aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					// ===========================================
					aEvNotModel.getNotifiche()[count].setEveIdEvento(lIdEvento);

					lNotDao = new NotificaDAO(lConn);

					lNotDao.setDAOFromModel(aEvNotModel.getNotifiche()[count]);
					BigDecimal lKeyNotifica = lNotDao.insert();
					lNotDao.stop();

					siesLogger.debug("Inserita Notifica " + lKeyNotifica);
				}

				count++;
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exInserisciNotaTrasmissione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exInserisciNotaTrasmissione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lNotDao);
			cleanup(lAutDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Funzione per ma lodifica della nota di trasmissione Effettua una delete insert dei dati
	 */
	public EventoNotificaModel exModificaNotaTrasmissione(EventoNotificaModel aEvNotModel)
			throws F3BException {
		Connection lConn = null;

		EventoDAO lEventoDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvNotModel);

		try {
			lConn = getDBConnection();

			lNotDao = new NotificaDAO(lConn);
			lNotDao.setCondizioneEvento(aEvNotModel.getEvento().getIdEvento());
			lNotDao.delete();

			lEventoDao = new EventoDAO(lConn);
			lEventoDao.selCondizioneUpdate(aEvNotModel.getEvento().getIdEvento());
			lEventoDao.delete();

			// =========================================
			// Inserisco la Nota di trasmissione
			// =========================================
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setDAOFromModel(aEvNotModel.getEvento());
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();

			lEveRet.getEvento().setIdEvento(lIdEvento);
			siesLogger.debug("lIdEvento = " + lIdEvento);

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			lAutDao = new AutoritaEsternaDAO(lConn);
			BigDecimal lKeyAutorita = null;

			while (count < aEvNotModel.getNotifiche().length) {
				siesLogger.debug("count = " + count);
				siesLogger.debug("Notifica[" + count + "] = " + aEvNotModel.getNotifiche()[count]);

				if (aEvNotModel.getNotifiche()[count] != null) {
					// Se è stata specificata anche l'autorità esterna per l'avvocato,
					// recupero l'id da inserire nella notifica
					// n.b. se autorità non presente la creo
					if (aEvNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
						// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
						// già l'autorità esterna specificata nella form (dalla form ho solo
						// codice e sede)
						lAutDao.setRicercaByAutSede(aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
							lAutDao.setDAOFromModel(aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					// ===========================================
					aEvNotModel.getNotifiche()[count].setEveIdEvento(lIdEvento);

					lNotDao = new NotificaDAO(lConn);

					lNotDao.setDAOFromModel(aEvNotModel.getNotifiche()[count]);
					BigDecimal lKeyNotifica = lNotDao.insert();
					lNotDao.stop();

					siesLogger.debug("Inserita Notifica " + lKeyNotifica);
				}

				count++;
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exInserisciNotaTrasmissione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exInserisciNotaTrasmissione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lNotDao);
			cleanup(lAutDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Metodo di validazione della nota di trasmissione
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 * @since MEV_2023-33
	 */
	public EventoModel exUpdateNotaTrasmissione(EventoModel aEvento) throws F3BException {

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		StatoProcedimentoDAO lStatoDao = null;

		EventoDAO lEveDaoBlob = null;

		EventoModel lEveRet = new EventoModel(aEvento);

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvento.getIdEvento());

			EventoModel lEveModelRich = (EventoModel) lEveSqlDAO.getModelByKey();
			String idEvento = Utils.isNullObj(lEveModelRich.getIdEvento()) ? " EVENTO NULLO"
					: "" + lEveModelRich.getIdEvento();
			siesLogger.debug("ID_EVENTO = " + idEvento);
			lEveSqlDAO.stop();

			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// ========================================================================
			siesLogger.debug("Aggiornamento stato procedimento");

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setFasSieIdFascicoloSiep(lEveModelRich.getFasSieIdFascicoloSiep());

			lStatoProcMod.setData(lEveModelRich.getDataEmissione());
			lStatoProcMod.setCodStatoProcedimento("0363");

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(lEveModelRich.getFasSieIdFascicoloSiep());
			lStatoDao.delete();
			siesLogger.debug("Cancellato old stato");
			// - Inserisce
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();
			siesLogger.debug("Inserito nuovo stato");
			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			siesLogger.debug("Aggiornamento Blob");
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			siesLogger.debug("Blob Aggiornato");

			// -----------------------
			commit(lConn);
			// commit(lConnBlob);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exUpdateNotaTrasmissione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Exception", ex);
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exUpdateNotaTrasmissione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lEveSqlDAO);
			cleanup(lStatoDao);

			cleanup(lConn);
			cleanup(lEveDaoBlob);
		}

		return lEveRet;
	}

	/**
	 *
	 * @param aEvNotModel
	 * @param lArrayIdRate
	 * @return
	 * @throws F3BException
	 * @since MEV_2023-33
	 */
	public EventoNotificaModel exInserisciProvvedimentoEstinzione(EventoNotificaModel aEvNotModel)
			throws F3BException {
		Connection lConn = null;

		EventoDAO lEventoDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvNotModel);

		try {
			lConn = getDBConnection();

			// =========================================
			// Inserisco l'evento
			// =========================================
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setDAOFromModel(aEvNotModel.getEvento());
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();
			lEveRet.getEvento().setIdEvento(lIdEvento);
			siesLogger.debug("lIdEvento = " + lIdEvento);

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			lAutDao = new AutoritaEsternaDAO(lConn);
			BigDecimal lKeyAutorita = null;

			if (aEvNotModel != null && aEvNotModel.getNotifiche() != null) {
				siesLogger.debug("Presenti " + aEvNotModel.getNotifiche().length + " notifiche");

				while (count < aEvNotModel.getNotifiche().length) {
					siesLogger.debug("count = " + count);
					siesLogger.debug("Notifica[" + count + "] = " + aEvNotModel.getNotifiche()[count]);

					if (aEvNotModel.getNotifiche()[count] != null) {
						// Se è stata specificata anche l'autorità esterna per l'avvocato,
						// recupero l'id da inserire nella notifica
						// n.b. se autorità non presente la creo
						if (aEvNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
							// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
							// già l'autorità esterna specificata nella form (dalla form ho solo
							// codice e sede)
							lAutDao.setRicercaByAutSede(
									aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								siesLogger.debug("Ins Aut Est = "
										+ aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
								lAutDao.setDAOFromModel(
										aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						// ===========================================
						aEvNotModel.getNotifiche()[count].setEveIdEvento(lIdEvento);

						lNotDao = new NotificaDAO(lConn);

						lNotDao.setDAOFromModel(aEvNotModel.getNotifiche()[count]);
						BigDecimal lKeyNotifica = lNotDao.insert();
						lNotDao.stop();

						siesLogger.debug("Inserita Notifica " + lKeyNotifica);
					}

					count++;
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exInserisciOrdineIngiunzione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exInserisciOrdineIngiunzione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lNotDao);
			cleanup(lAutDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Metodo per la modifica del provvedimento di Estinzione della Pena Pecuniaria
	 *
	 * @param aEvNotModel
	 * @return
	 * @throws F3BException
	 * @since MEV_2023-33
	 */
	public EventoNotificaModel exModificaProvvedimentoEstinzione(EventoNotificaModel aEvNotModel)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEventoDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvNotModel);

		try {
			lConn = getDBConnection();

			lNotDao = new NotificaDAO(lConn);
			lNotDao.setCondizioneEvento(aEvNotModel.getEvento().getIdEvento());
			lNotDao.delete();

			lEventoDao = new EventoDAO(lConn);
			lEventoDao.selCondizioneUpdate(aEvNotModel.getEvento().getIdEvento());
			lEventoDao.delete();

			// =========================================
			// Inserisco il provvedimento
			// =========================================
			lEventoDao.setDAOFromModel(aEvNotModel.getEvento());
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();
			lEveRet.getEvento().setIdEvento(lIdEvento);
			siesLogger.debug("lIdEvento = " + lIdEvento);

			// =========================================================
			// Inserisco le Notifiche collegate all'evento se presenti
			// =========================================================
			int count = 0;
			lAutDao = new AutoritaEsternaDAO(lConn);
			BigDecimal lKeyAutorita = null;

			if (aEvNotModel != null && aEvNotModel.getNotifiche() != null) {
				siesLogger.debug("Presenti " + aEvNotModel.getNotifiche().length + " notifiche");

				while (count < aEvNotModel.getNotifiche().length) {
					siesLogger.debug("count = " + count);
					siesLogger.debug("Notifica[" + count + "] = " + aEvNotModel.getNotifiche()[count]);

					if (aEvNotModel.getNotifiche()[count] != null) {
						// Se è stata specificata anche l'autorità esterna per l'avvocato,
						// recupero l'id da inserire nella notifica
						// n.b. se autorità non presente la creo
						if (aEvNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
							// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
							// già l'autorità esterna specificata nella form (dalla form ho solo
							// codice e sede)
							lAutDao.setRicercaByAutSede(
									aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								lAutDao.setDAOFromModel(
										aEvNotModel.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEvNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						// ===========================================
						aEvNotModel.getNotifiche()[count].setEveIdEvento(lIdEvento);

						lNotDao = new NotificaDAO(lConn);

						lNotDao.setDAOFromModel(aEvNotModel.getNotifiche()[count]);
						BigDecimal lKeyNotifica = lNotDao.insert();
						lNotDao.stop();

						siesLogger.debug("Inserita Notifica " + lKeyNotifica);
					}

					count++;
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exModificaProvvedimentoEstinzione: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Eccezione Generica", ex);
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.exModificaProvvedimentoEstinzione: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lNotDao);
			cleanup(lAutDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Metodo per la stampa Excel del risutato delle ricerche Stato Pagamento
	 */
	public void ExCreateExcelStatoPagamenti(Vector<RicercaStatoPagamentiModel> listaStatoPagamenti,
			HSSFWorkbook wb, UfficioModel ufficio, FascicoloSiepModel aFasMod, String aTipoRicera)
			throws F3BException {

		HSSFCellStyle csNull = wb.createCellStyle();

		// Create a new font and alter it.
		HSSFFont fontBold = wb.createFont();
		fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// stile per celle col bordo
		HSSFCellStyle cs = wb.createCellStyle();
		cs = getBordo4Lati(wb);

		HSSFCellStyle csNullBold = wb.createCellStyle();
		csNullBold.setFont(fontBold);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = wb.createCellStyle();
		csBold = getBordo4Lati(wb);
		csBold.setFont(fontBold);

		HSSFCellStyle csCenter = wb.createCellStyle();
		csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCellStyle csRight = wb.createCellStyle();
		csRight = getBordo4Lati(wb);
		csRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		HSSFCellStyle csBoldCenter = wb.createCellStyle();
		csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(fontBold);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCellStyle csEuroFormat = wb.createCellStyle();
		// HSSFDataFormat dataFormat = wb.createDataFormat();
		// csEuroFormat.setDataFormat(dataFormat.getFormat("0.00"));
		short builtinFormatIndex = 4; // 4: "#,##0.00"
		csEuroFormat.setDataFormat(builtinFormatIndex);
		csEuroFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		csEuroFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		csEuroFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		csEuroFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		// primo foglio
		HSSFSheet sheet = wb.createSheet("Stato Pagamenti");
		sheet.setColumnWidth(0, (40 * 256));

		int numCol = 0;
		int sizeCol = 21;
		sheet.setColumnWidth(numCol++, (sizeCol * 256));
		sheet.setColumnWidth(numCol++, (sizeCol * 256));
		sheet.setColumnWidth(numCol++, (sizeCol * 256));
		sheet.setColumnWidth(numCol++, (sizeCol * 256));
		sheet.setColumnWidth(numCol++, (sizeCol * 256));
		sheet.setColumnWidth(numCol++, (sizeCol * 256));
		sheet.setColumnWidth(numCol++, (sizeCol * 256));
		sheet.setColumnWidth(numCol++, (sizeCol * 256));

		int nRow = 0;

		// Intestazione del foglio excel
		// Prima Riga
		HSSFRow row = sheet.createRow(nRow++);
		String value = (ufficio.getDescrTipoUfficio().toUpperCase() + " DI "
				+ ufficio.getDescrComune().toUpperCase());
		setCell(row, 0, value, csNull);

		// Seconda Riga
		value = "Tel. " + StringUtils.toStringJSP(ufficio.getTelefono()) + " - Fax "
				+ StringUtils.toStringJSP(ufficio.getFax());
		row = sheet.createRow(nRow++);
		setCell(row, 0, value, csNull);

		nRow++;
		nRow++;

		value = "Elenco Procedimenti Pene Pecuniarie In Base a Stato Pagamenti ";
		row = sheet.createRow(nRow++);
		setCell(row, 0, value, csNull);

		nRow++;

		// ===========================
		// Criteri di ricerca
		// ===========================
		// Anno e Numero
		row = sheet.createRow(nRow++);
		setCell(row, 0, "Anno/Numero Iniziale ", csNullBold);
		value = StringUtils.toStringJSP(aFasMod.getChiaveAnnoIniziale(), "____") + " / "
				+ StringUtils.toStringJSP(aFasMod.getChiaveProgrIniziale(), "______");
		setCell(row, 1, value, csNull);

		setCell(row, 2, "Anno/Numero Finale ", csNullBold);
		value = StringUtils.toStringJSP(aFasMod.getChiaveAnnoFinale(), "____") + " / "
				+ StringUtils.toStringJSP(aFasMod.getChiaveProgrFinale(), "______");
		setCell(row, 3, value, csNull);

		// Data Iscrizione
		row = sheet.createRow(nRow++);
		setCell(row, 0, "Data Iscrizione Iniziale ", csNullBold);
		value = StringUtils.toStringJSP(
				DateUtils.getDateToString(aFasMod.getDataIscrizioneIniziale(), "dd.MM.yyyy"), " ");
		setCell(row, 1, value, csNull);

		setCell(row, 2, "Data Iscrizione Finale ", csNullBold);
		value = StringUtils
				.toStringJSP(DateUtils.getDateToString(aFasMod.getDataIscrizioneFinale(), "dd.MM.yyyy"), " ");
		setCell(row, 3, value, csNull);

		// Tipologia Statistica
		String descTipoRicerca = "";
		if (aTipoRicera.equals(ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_INTERAMENTE_PAGATO))
			descTipoRicerca = "Procedimenti con pena pecuniaria totalmente pagata";
		else if (aTipoRicera.equals(ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_RETEIZZATO_NON_PAGATO))
			descTipoRicerca = "Procedimenti con pagamento rateizzato con rate non pagate";
		else if (aTipoRicera.equals(ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_UNICA_RATA_NON_PAGATO))
			descTipoRicerca = "Procedimenti con pagamento in unica soluzione non pagata";
		row = sheet.createRow(nRow++);
		setCell(row, 0, "Tipologia Statistica ", csNullBold);
		setCell(row, 1, StringUtils.toStringJSP(descTipoRicerca, " "), csNull);

		nRow++;
		nRow++;

		// Intestazione tabella RIsultati
		row = sheet.createRow(nRow++);
		setCell(row, 0, "Numero SIEP", csBoldCenter);
		setCell(row, 1, "Data Iscrizione", csBoldCenter);
		setCell(row, 2, "Cognome", csBoldCenter);
		setCell(row, 3, "Nome", csBoldCenter);
		setCell(row, 4, "Tipo Pagamento", csBoldCenter);
		setCell(row, 5, "Importo da Pagare", csBoldCenter);
		setCell(row, 6, "Importo Pagato", csBoldCenter);
		setCell(row, 7, "Data Ultima Scadenza", csBoldCenter);

		// Ciclo sui record
		for (RicercaStatoPagamentiModel record : listaStatoPagamenti) {
			String tipoRat = "";
			if ("U".equals(record.getTipoRateizzazione()))
				tipoRat = "Unica Soluzione";
			else if ("R".equals(record.getTipoRateizzazione()))
				tipoRat = "Rateizzato";

			row = sheet.createRow(nRow++);

			setCell(row, 0, StringUtils.toStringJSP(record.getChiaveAnno()) + " / "
					+ StringUtils.toStringJSP(record.getChiaveProgr()), cs);
			setCell(row, 1, StringUtils.toStringJSP(
					DateUtils.getDateToString(record.getDataIscrizione(), "dd-MM-yyyy")), csCenter);
			setCell(row, 2, StringUtils.toStringJSP(record.getCognome()), cs);
			setCell(row, 3, StringUtils.toStringJSP(record.getNome()), cs);
			setCell(row, 4, StringUtils.toStringJSP(tipoRat), csCenter);
			// setCell(row, 5, StringUtils.toEuroFormat(record.getImportoDaPagare()),
			// csEuroFormat);csEuroFormat
			// setCell(row, 6, StringUtils.toEuroFormat(record.getImportoPagato()), csEuroFormat);
			setNumericCell(row, 5,
					record.getImportoDaPagare() != null ? record.getImportoDaPagare().doubleValue() : null,
					csEuroFormat);
			setNumericCell(row, 6,
					record.getImportoPagato() != null ? record.getImportoPagato().doubleValue() : null,
					csEuroFormat);
			setCell(row, 7,
					StringUtils.toStringJSP(
							DateUtils.getDateToString(record.getDataUltimaScadenza(), "dd-MM-yyyy"), " "),
					csCenter);
		}
	}

	private HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		return cs;
	}

	private HSSFCell setCell(HSSFRow row, int nCol, String value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

	private HSSFCell setNumericCell(HSSFRow row, int nCol, Double value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

	/**
	 * Metodo per la stampa Excel del risutato delle ricerche "Scadenzario Stato Pagamenti Pena Pecuniaria"
	 */
	public void ExCreateExcelScadenzariPP(Vector<ScadenzarioModel> listaScadenzari, HSSFWorkbook wb,
			UfficioModel ufficio, ScadenzarioModel aScadMod) throws F3BException {

		HSSFCellStyle csNull = wb.createCellStyle();

		// Create a new font and alter it.
		HSSFFont fontBold = wb.createFont();
		fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// stile per celle col bordo
		HSSFCellStyle cs = wb.createCellStyle();
		cs = getBordo4Lati(wb);

		HSSFCellStyle csNullBold = wb.createCellStyle();
		csNullBold.setFont(fontBold);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = wb.createCellStyle();
		csBold = getBordo4Lati(wb);
		csBold.setFont(fontBold);

		HSSFCellStyle csCenter = wb.createCellStyle();
		csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCellStyle csRight = wb.createCellStyle();
		csRight = getBordo4Lati(wb);
		csRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		HSSFCellStyle csBoldCenter = wb.createCellStyle();
		csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(fontBold);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csBoldCenter.setWrapText(true);
		csBoldCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		HSSFCellStyle csBoldRight = wb.createCellStyle();
		// csBoldRight = getBordo4Lati(wb);
		csBoldRight.setFont(fontBold);
		csBoldRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		HSSFCellStyle csEuroFormat = wb.createCellStyle();
		// HSSFDataFormat dataFormat = wb.createDataFormat();
		// csEuroFormat.setDataFormat(dataFormat.getFormat("0.00"));
		short builtinFormatIndex = 4; // 4: "#,##0.00"
		csEuroFormat.setDataFormat(builtinFormatIndex);
		csEuroFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		csEuroFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		csEuroFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		csEuroFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		// primo foglio
		HSSFSheet sheet = wb.createSheet("Scadenzario Pagamenti");
		sheet.setColumnWidth(0, (40 * 256));

		int numCol = 0;
		int sizeCol = 21;
		sheet.setColumnWidth(numCol++, (15 * 256)); // N° SIEP
		sheet.setColumnWidth(numCol++, (sizeCol * 256)); // Cognome
		sheet.setColumnWidth(numCol++, (sizeCol * 256)); // Nome
		sheet.setColumnWidth(numCol++, (sizeCol * 256)); // Luogo Nascita
		sheet.setColumnWidth(numCol++, (15 * 256)); // Data Nascita
		sheet.setColumnWidth(numCol++, (15 * 256)); // Data Notifica
		sheet.setColumnWidth(numCol++, (21 * 256)); // Data Scadenza richiesta retizzazione
		sheet.setColumnWidth(numCol++, (17 * 256)); // Data Scadenza primo pagamento
		sheet.setColumnWidth(numCol++, (23 * 256)); // N.ro giorni
		sheet.setColumnWidth(numCol++, (18 * 256)); // Importo rate o unica soluzione
		sheet.setColumnWidth(numCol++, (10 * 256)); // Rata
		sheet.setColumnWidth(numCol++, (20 * 256)); // Stato pagamento
		sheet.setColumnWidth(numCol++, (25 * 256)); // Stato scadenza

		int nRow = 0;

		// Intestazione del foglio excel
		// Prima Riga
		HSSFRow row = sheet.createRow(nRow++);
		String value = (ufficio.getDescrTipoUfficio().toUpperCase() + " DI "
				+ ufficio.getDescrComune().toUpperCase());
		setCell(row, 0, value, csNull);

		// Seconda Riga
		value = "Tel. " + StringUtils.toStringJSP(ufficio.getTelefono()) + " - Fax "
				+ StringUtils.toStringJSP(ufficio.getFax());
		row = sheet.createRow(nRow++);
		setCell(row, 0, value, csNull);

		nRow++;
		nRow++;

		value = "Elenco Procedimenti Pene Pecuniarie In Base alla scadenza rate";
		row = sheet.createRow(nRow++);
		setCell(row, 0, value, csNullBold);

		nRow++;

		row = sheet.createRow(nRow++);
		setCell(row, 0, "Data Estrazione ", csNullBold);
		setCell(row, 1, DateUtils.getDateToString(new Date(), "dd-MM-yyyy"), csBoldRight);

		// ===========================
		// Criteri di ricerca
		// ===========================
		// Anno e Numero
		row = sheet.createRow(nRow++);

		setCell(row, 0, "Fascicolo ", csNullBold);
		value = StringUtils.toStringJSP(aScadMod.getChiaveAnnoIniziale(), "____") + " / "
				+ StringUtils.toStringJSP(aScadMod.getChiaveProgrIniziale(), "______");
		setCell(row, 1, value, csBoldRight);

		// Tipo estrazione
		String descTipoEstrazione = "";

		siesLogger.debug("aScadMod.getTipoRic() = " + aScadMod.getTipoRic());
		if ("Tutti".equals(aScadMod.getTipoRic())) {
			descTipoEstrazione += "tutti";
		} else if ("sette".equals(aScadMod.getTipoRic())) {
			descTipoEstrazione += "in scadenza entro";
			if (aScadMod.getNumAnni() != null && aScadMod.getNumAnni().intValue() > 0)
				descTipoEstrazione += " Anni " + aScadMod.getNumAnni().intValue();
			if (aScadMod.getNumMesi() != null && aScadMod.getNumMesi().intValue() > 0)
				descTipoEstrazione += " Mesi " + aScadMod.getNumMesi().intValue();
			if (aScadMod.getNumGiorni() != null && aScadMod.getNumGiorni().intValue() > 0)
				descTipoEstrazione += " Giorni " + aScadMod.getNumGiorni().intValue();
		} else if ("oggi".equals(aScadMod.getTipoRic())) {
			descTipoEstrazione += "In Scadenza Oggi";
		} else if ("scaduto".equals(aScadMod.getTipoRic())) {
			descTipoEstrazione += "Scaduti";
		}

		row = sheet.createRow(nRow++);
		setCell(row, 0, "Tipo Estrazione ", csNullBold);
		setCell(row, 1, StringUtils.toStringJSP(descTipoEstrazione, " "), csBoldRight);

		nRow++;
		nRow++;

		// Intestazione tabella RIsultati
		row = sheet.createRow(nRow++);
		setCell(row, 0, "Numero SIEP", csBoldCenter);
		setCell(row, 1, "Cognome", csBoldCenter);
		setCell(row, 2, "Nome", csBoldCenter);
		setCell(row, 3, "Luogo Nascita", csBoldCenter);
		setCell(row, 4, "Data Nascita", csBoldCenter);
		setCell(row, 5, "Data Notifica", csBoldCenter);
		setCell(row, 6, "Data Scadenza richiesta retizzazione", csBoldCenter);
		setCell(row, 7, "Data Scadenza primo pagamento", csBoldCenter);
		setCell(row, 8, "N.ro giorni", csBoldCenter);
		setCell(row, 9, "Importo rate o unica soluzione", csBoldCenter);
		setCell(row, 10, "Rata", csBoldCenter);
		setCell(row, 11, "Stato pagamento", csBoldCenter);
		setCell(row, 12, "Stato Scadenza", csBoldCenter);

		csCenter.setWrapText(true);
		// Ciclo sui record
		for (ScadenzarioModel lSca : listaScadenzari) {
			FascicoloSiepModel lFas = lSca.getFascicoloModel();
			SoggettoModel lSog = lFas.getSoggetto();
			BollettinoPagopaModel lBollettino = lSca.getBollettinoModel();

			String dataScadRateizzazione = "";
			if ("U".equals(lBollettino.getTipoRateizzazione())) {
				dataScadRateizzazione = DateUtils.getDateToString(
						DateUtils.moveDateTo(lSca.getDataInizioScadenza(), Calendar.DAY_OF_MONTH, 20),
						"dd/MM/yyyy");
			}

			String giorniStr = "";
			if (lBollettino.getDataScadenza() != null) {
				if (DateUtils.isGreater(lBollettino.getDataScadenza(), DateUtils.getSysDate()))
					giorniStr = ScadenzarioUtils.getDifferenza(lBollettino.getDataScadenza(),
							DateUtils.getSysDate());
				else
					giorniStr = ScadenzarioUtils.getDifferenza(DateUtils.getSysDate(),
							lBollettino.getDataScadenza());
			}

			String statoScadenza = "";
			// if ("Tutti".equals(aScadMod.getTipoRic())) {
			if (lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue() == 0)
				statoScadenza = "In scadenza Oggi";
			else if (lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue() <= 7
					&& lSca.getGiorniResidui().intValue() > 0)
				statoScadenza = "In scadenza entro 7 giorni";
			else if (lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue() < 0)
				statoScadenza = "Scaduto";
			else
				statoScadenza = "";
			// }

			row = sheet.createRow(nRow++);

			setCell(row, 0, StringUtils.toStringJSP(lFas.getChiaveAnno()) + " / "
					+ StringUtils.toStringJSP(lFas.getChiaveProgr()), cs);
			setCell(row, 1, StringUtils.toStringJSP(lSog.getCognome()), cs);
			setCell(row, 2, StringUtils.toStringJSP(lSog.getNome()), cs);
			setCell(row, 3, StringUtils.toStringJSP(lSog.getDescrComuneNascita()), csCenter);

			setCell(row, 4,
					StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(), "dd-MM-yyyy")),
					csCenter);
			setCell(row, 5, StringUtils.toStringJSP(
					DateUtils.getDateToString(lSca.getDataInizioScadenza(), "dd-MM-yyyy")), csCenter);
			setCell(row, 6, StringUtils.toStringJSP(dataScadRateizzazione), csCenter);
			setCell(row, 7,
					StringUtils.toStringJSP(
							DateUtils.getDateToString(lBollettino.getDataScadenza(), "dd-MM-yyyy")),
					csCenter);

			setCell(row, 8, StringUtils.toStringJSP(giorniStr), csCenter);

			setNumericCell(row, 9,
					lBollettino.getImportoRata() != null ? lBollettino.getImportoRata().doubleValue() : null,
					csEuroFormat);

			setCell(row, 10, StringUtils.toStringJSP(lBollettino.getProgRata(), "") + "/"
					+ StringUtils.toStringJSP(lBollettino.getNumeroRate(), ""), csCenter);

			if ("PN".equals(lBollettino.getStatoPagamento()))
				setCell(row, 11, StringUtils.toStringJSP("Non Pagato"), csCenter);
			else
				setCell(row, 11, StringUtils.toStringJSP("Pagato"), csCenter);

			// statoScadenza
			setCell(row, 12, StringUtils.toStringJSP(statoScadenza), cs);
		}
	}

}
