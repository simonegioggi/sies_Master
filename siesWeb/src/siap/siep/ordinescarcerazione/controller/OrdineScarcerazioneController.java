package siap.siep.ordinescarcerazione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.dao.FungibilitaDAO;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.nomeprovvedimento.model.NomeProvvedimentoModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoSqlDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.dao.TenoreDAO;

/**
 * <p>
 * Title: OrdineScarcerazioneController
 * </p>
 * <p>
 * Description: OrdineScarcerazioneController
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OrdineScarcerazioneController extends SiapController implements IOrdineScarcerazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisce/Aggiorna l'evento. Inserisce le notifiche. Inserisce le note.
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaEventoNotifica(EventoNotificaModel aEvento)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExInserisciOModificaEventoNotifica");

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		FungibilitaDAO lFunDao = null;
		PenaResiduaSqlDAO lPenaResSqlDAO = null;
		PenaResiduaDAO lPenaResDAO = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);

			// Cerca L'Evento se presente (By IdFascicolo)
			lSqlDAO = new EventoSqlDAO(lConn);
			lSqlDAO.ricercaEventoTipoCodMotProvNonValidato(aEvento.getEvento());
			EventoModel lEvePresente = (EventoModel) lSqlDAO.getModelByKey();

			BigDecimal lKeyEvento = null;
			if (lEvePresente == null) {// Se non presente lo inserisce
				// Setto l'anno e il progressivo...
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
				aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));
				lEveDao.setDAOFromModel(aEvento.getEvento());
				lKeyEvento = lEveDao.insert();
				lEveRet.getEvento().setIdEvento(lKeyEvento);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" ###### Inserito evento id = " + lKeyEvento);
			} else {// Se presente lo aggiorna
				lKeyEvento = lEvePresente.getIdEvento();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

				EventoModel lEveMod = aEvento.getEvento();
				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				lEveDao.setCodMotivo(aEvento.getEvento().getCodMotivo());
				lEveDao.setDataEmissione(lEveMod.getDataEmissione());
				lEveDao.setDataTrasmissioneAtti(lEveMod.getDataTrasmissioneAtti());

				lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
				lEveDao.setCodLuogoEmittente(lEveMod.getCodLuogoEmittente());
				lEveDao.setCodUfficioEmittente(lEveMod.getCodUfficioEmittente());
				lEveDao.setCodMagistrato(lEveMod.getCodMagistrato());
				lEveDao.setAnnoProtocollo(lEveMod.getAnnoProtocollo());
				lEveDao.setEveIdEvento(aEvento.getEvento().getEveIdEvento());
				lEveDao.setIdEvento(lKeyEvento);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" ###### Modificato evento id = " + lKeyEvento);

				// *********** Cancella le NOTIFICHE associate al EVENTO ******************
				lNotDao.setCondizioneEvento(lKeyEvento);
				lNotDao.delete();
				lNotDao.stop();
			}

			BigDecimal lKeyAutorita = null;
			int count = 0;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Sono presenti " + aEvento.getNotifiche().length + " notifiche");
			while (count < aEvento.getNotifiche().length) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Notifica[" + count + "] = " + aEvento.getNotifiche()[count]);
				if (aEvento.getNotifiche()[count] != null) {
					if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();
						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di mLog
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);
					lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("Inserito evento" + lKeyEvento);
				}
				count++;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("+++++count" + count);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("+++++Inizio Inserimento Note");
			// Inserimento delle eventuali note aggiuntive.
			if (aEvento.getCampoNote() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : "
						+ aEvento.getCampoNote().length);
				count = 0;
				while (count < aEvento.getCampoNote().length) {
					aEvento.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					aEvento.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEvento.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}

			// ========================================================================
			// Nel caso di OS per LA, le LA agganciate/appoggiate sull'ordinanza
			// vengono spostate sul provvedimento dell'esecuzione.
			// Inoltre la PR viene agganciata subito all'evento. Viene recuperata
			// l'ultima PR non validata, nell'ipotesi che sia quella calcolata in
			// fase di iscrizione dell'ordinanza.
			// ========================================================================
			if ("0081".equals(aEvento.getEvento().getCodMotivo()) // Nuova scadenza pena a seguito concessione
																	// Liberazione Anticipata - condannato
																	// detenuto
					|| "0083".equals(aEvento.getEvento().getCodMotivo()) // Nuova scadenza pena a seguito
																			// concessione Liberazione
																			// Anticipata - condannato in
																			// misura alternativa
					|| "5491".equals(aEvento.getEvento().getCodMotivo()) // Nuova scadenza pena a seguito
																			// concessione risarcimento danni
																			// D.L. 92/2014 - condannato
																			// detenuto
					|| "5492".equals(aEvento.getEvento().getCodMotivo()) // Nuova scadenza pena a seguito
																			// concessione risarcimento danni
																			// D.L. 92/2014 - condannato in
																			// misura alternativa
					// inizio ticket 20190805017 - Mancata attribuzione dei giorni di detrazione rimedi
					// risarcitori
					|| "9254".equals(aEvento.getEvento().getCodMotivo()) // Nuova scadenza pena a seguito
																			// concessione reclamo
																			// risarcimento danni
																			// D.L. 92/2014 - condannato in
																			// misura alternativa
					|| "9154".equals(aEvento.getEvento().getCodMotivo()) // Nuova scadenza pena a seguito
																			// concessione reclamo
																			// risarcimento danni
																			// D.L. 92/2014 - condannato in
																			// misura alternativa
			// inizio ticket 20190805017

			) {
				lFunDao = new FungibilitaDAO(lConn);

				lFunDao.setEveIdEvento(aEvento.getEvento().getIdEvento()); // id provvedimento OS
				lFunDao.setCondizioneUpdateEveIdEvento(aEvento.getEvento().getEveIdEvento()); // Id Ordinanza
				lFunDao.update();

				// Ricerco l'ultima pena residua e la aggancio all'evento se non validata
				// n.b. dovrebbe essere quella calcolata in fase di registrazione dell'LA
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Ricerco l'ultima PR ");
				lPenaResSqlDAO = new PenaResiduaSqlDAO(lConn);
				PenaResiduaModel lPenResMod = null;
				lPenaResSqlDAO.ricercaPenaResiduaCorrenteByFascicoloSiep(
						aEvento.getEvento().getFasSieIdFascicoloSiep());
				lPenResMod = (PenaResiduaModel) lPenaResSqlDAO.getModelByKey();
				if (lPenResMod != null && lPenResMod.getIdPenaResidua() != null
						&& lPenResMod.getEveIdEvento() == null // non deve essere legata ad alcun evento
				) { // Aggancio la pena Residua all'evento
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Aggancio la PR ");
					lPenaResDAO = new PenaResiduaDAO(lConn);
					lPenaResDAO.setEveIdEvento(aEvento.getEvento().getIdEvento());
					lPenaResDAO.setCondizioneUpdate(lPenResMod.getIdPenaResidua());
					lPenaResDAO.update();
					lPenaResDAO.stop();
				}
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			daoEx.printStackTrace();
			rollback(lConn);
			throw new F3BException(
					"OrdineScarcerazioneController.ExInserisciOModificaEventoNotifica: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineScarcerazioneController.ExInserisciOModificaEventoNotifica: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lFunDao);
			cleanup(lPenaResSqlDAO);
			cleanup(lPenaResDAO);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Effettua la validazione dell'Ordine di Scarcerazione per nuova scadenza pena nel caso delle decisioni
	 * del GE Amnistia/Indulto - Depenalizzazione - Incostituzionalità
	 *
	 * @param aEvento
	 *            - Ordine di scarcerazione
	 * @param aFascicolo
	 * @return EventoModel
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaOrdineScarcerazione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		AnnotazioneManualeSqlDAO lAnnSqlDao = null;
		AnnotazioneManualeDAO lAnnDao = null;
		FungibilitaDAO lFunDao = null;
		MisuraAlternativaSqlDAO lMisAltSqlDao = null;
		MisuraAlternativaDAO lMisAltDao = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);

			String lCodMotivo = "";
			lCodMotivo = aEvento.getCodMotivo();

			// NOME_PROVVEDIMENTO
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			if (lCodMotivo.equals("0159")) // Amnistia
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP071");
			}
			if (lCodMotivo.equals("0160")) // Indulto
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP072");
			}
			if (lCodMotivo.equals("0175")) // Indulto (Revoca)
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP073");
			}
			if (lCodMotivo.equals("0174")) // Amnistia (Revoca)
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP074");
			}
			if (lCodMotivo.equals("0158")) // Depenalizzazione/Incostituzionalità
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP075");
			}
			if (lCodMotivo.equals("0161")) // Determinazione pena a seguito di applicazione beneficio
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP076");
			}
			if (lCodMotivo.equals("0162")) // Rideterminazione pena
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP077");
			}
			if (lCodMotivo.equals("0163")) // Determinazione pena a seguito di revoca beneficio
			{
				lNomProvvDAO.setCodNomeProvvedimento("NP078");
			}

			lNomProvvDAO.setEveIdEvento(aEvento.getIdEvento());

			// [FT] - 13/07/2026 - Ticket #20260713011 - Cancellazione provvedimenti / validazione bloccata.
			// Stesso difetto individuato in ExUpdateValidaOSLibAnt e ExUpdateValidaOS: la insert su
			// NOME_PROVVEDIMENTO non era idempotente e falliva con ORA-00001 (vincolo di unicita' su
			// EVE_ID_EVENTO) in caso di rivalidazione dello stesso evento, con conseguente rollback
			// silenzioso della transazione. Si applica lo stesso pattern "cancella poi inserisci" gia'
			// usato per STATO_PROCEDIMENTO poco piu' sotto in questo stesso metodo.
			lNomProvvDAO.setCondizioneByEveIdEvento(aEvento.getIdEvento());
			lNomProvvDAO.delete();

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// STATO_PROCEDIMENTO
			// cancellazione
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoDao.setCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
			lStatoDao.delete();

			// inserimento
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			if (lCodMotivo.equals("0161") || lCodMotivo.equals("0162") || lCodMotivo.equals("0163")) {
				lStatoProcMod.setCodStatoProcedimento("0072");
			} else {
				lStatoProcMod.setCodStatoProcedimento("0021");
			}
			lStatoProcMod.setData(aEvento.getDataEmissione());
			lStatoProcMod.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			// String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// Cerca l'ultima PENA_RESIDUA per fascicolo
			PenaResiduaModel lPenResMod = new PenaResiduaModel();

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// SCADENZARIO
			if (!lCodMotivo.equals("0161") && !lCodMotivo.equals("0162") && !lCodMotivo.equals("0163")) {
				// boolean lFlagLibero = false;
				// if (lCodPosizione != null && (lCodPosizione.equals("07") || lCodPosizione.equals("10"))) //
				// LIBERO
				// lFlagLibero = true;

				boolean lFlagAltraCausa = false;
				if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) // ALTRA
																											// CAUSA
					lFlagAltraCausa = true;

				// Se non è libero oppure è libero ma detenuto per altra causa
				// modifica/inserisce lo scadenzario fine pena
				lScaDao = new ScadenzarioDAO(lConn);
				lScaSqlDao = new ScadenzarioSqlDAO(lConn);
				ScadenzarioModel lScaMod = new ScadenzarioModel();
				if (!lPosMod.isLibero() || lFlagAltraCausa) {
					// Cerca l'ultimo scadenzario fine pena per quel fascicolo
					lScaSqlDao.ricercaScadenzarioFinePenaVaneRicerche(aFascicolo.getIdFascicoloSiep());
					lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

					if (lScaMod != null) {
						// aggiorna scadenzario
						lScaDao.setDataFineScadenza(lPenResMod.getDataFine());
						lScaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
						lScaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lScaDao.setDataAggiornamento(aEvento.getDataAggiornamento());
						lScaDao.setFlagVisto("N");

						lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
						lScaDao.update();
						lScaDao.stop();
					}
				}
			}
			// ANNOTAZIONE MANUALE
			lAnnSqlDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnDao = new AnnotazioneManualeDAO(lConn);
			lFunDao = new FungibilitaDAO(lConn);

			lAnnSqlDao.ricercaAnnotazioneManualeByIdEvento(aEvento.getEveIdEvento());
			Vector lListAnno = new Vector(lAnnSqlDao.getModels());

			for (Iterator i = lListAnno.iterator(); i.hasNext();) {
				AnnotazioneManualeModel lAnnMan = (AnnotazioneManualeModel) i.next();

				boolean lFlagAppProvv = false;
				if (lAnnMan.getFlagAppProvvisoria() != null && !lAnnMan.getFlagAppProvvisoria().equals("")
						&& !lAnnMan.getFlagAppProvvisoria().equals("-")) {
					lFlagAppProvv = true; // FLAG_APP_PROVVISORIA è 'A' oppure 'R'
				}

				// VALIDA ANNOTAZIONE MANUALE
				lAnnDao.setFlagValidato("S");
				lAnnDao.setCondizioneUpdate(lAnnMan.getIdAnnotazioneManuale());
				lAnnDao.update();
				lAnnDao.stop();

				// VALIDA EVENTO PROVVEDIMENTO
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.selCondizioneUpdate(aEvento.getEveIdEvento());
				lEveDao.update();
				lEveDao.stop();

				// valida evento 03
				if (!lCodMotivo.equals("0161") && !lCodMotivo.equals("0162") && !lCodMotivo.equals("0163")) {
					lEveSqlDao = new EventoSqlDAO(lConn);
					lEveSqlDao.ricercaEventoByKey(aEvento.getEveIdEvento());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("ricerca evento");
					EventoModel lEveProvv = (EventoModel) lEveSqlDao.getModelByKey();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("EVENTO 04" + lEveProvv);

					EventoModel EveOrd = new EventoModel();
					EveOrd.setCodTipoEvento("01");
					EveOrd.setCodTipoProvvedimento("03");
					EveOrd.setCodMotivo(lEveProvv.getCodMotivo());
					EveOrd.setFasSieIdFascicoloSiep(lEveProvv.getFasSieIdFascicoloSiep());

					lEveSqlDao.ricercaEventoNonRegistrato(EveOrd);
					EventoModel EveOrdRic = (EventoModel) lEveSqlDao.getModelByKey();
					if (EveOrdRic != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("EVENTO 03" + EveOrdRic);

						EveOrdRic.setFlagDocumentoRegistrato("S");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("MODEL CHE UPDATA" + EveOrdRic);

						lEveDao.setDAOFromModelForUpdate(EveOrdRic);
						lEveDao.update();
						lEveDao.stop();
					}
				}
				// VALIDA FUNGIBILITA
				// ** se l'annotazione è di tipo A o R
				// ** cancella l'eventuale FUNGIBILITA associata
				if (lAnnMan.getFunIdFungibilita() != null) {
					lFunDao.setCondizioneUpdate(lAnnMan.getFunIdFungibilita());

					if (!lFlagAppProvv) {
						lFunDao.setFlagValidato("S");
						lFunDao.update();
					} else {
						lAnnDao.setCondizioneUpdate(lAnnMan.getIdAnnotazioneManuale());
						lAnnDao.setFunIdFungibilita(null);
						lAnnDao.update();
						lAnnDao.stop();

						lFunDao.delete();
					}

					lFunDao.stop();
				}

				// CANCELLA PENA RESIDUA
				// ** se l'annotazione è di tipo A o R
				// ** cancella la PENA RESIDUA associata
				lPenResDao = new PenaResiduaDAO(lConn);

				if (lAnnMan.getPenResIdPenaResidua() != null) {
					if (lFlagAppProvv) {
						lAnnDao.setCondizioneUpdate(lAnnMan.getIdAnnotazioneManuale());
						lAnnDao.setPenResIdPenaResidua(null);
						lAnnDao.update();
						lAnnDao.stop();

						lPenResDao.setCondizioneUpdate(lAnnMan.getPenResIdPenaResidua());
						lPenResDao.delete();
						lPenResDao.stop();
					}
				}

				// VALIDA ULTIMA PENA RESIDUA
				lPenResDao.setCondizioneUpdate(lPenResMod.getIdPenaResidua());
				lPenResDao.setFlagValidato("S");
				lPenResDao.update();
				lPenResDao.stop();
			}

			// SE IN MISURA ALTERNATIVA
			if (lPosMod.isMisAlt()) {
				// *** Commentata da Rework 15/03/2005 per farlo uguale a OS per LA
				/*
				 * lMisAltSqlDao.ricercaMisuraAlternativaCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep()
				 * ) ; MisuraAlternativaModel lMisAltMod = (MisuraAlternativaModel)
				 * lMisAltSqlDao.getModelByKey(); // Cerca la penultima PENA_RESIDUA PenaResiduaModel
				 * lPenultimaPenResMod = new PenaResiduaModel();
				 * lPenResSqlDao.ricercaPenaResiduaByIdFascicoloDataDesc(aFascicolo.getIdFascicoloSiep());
				 * lPenResSqlDao.start(); if (lPenResSqlDao.next() && lPenResSqlDao.next())
				 * lPenultimaPenResMod = (PenaResiduaModel) lPenResSqlDao.getModel(); lPenResSqlDao.stop(); if
				 * (lMisAltMod != null && lPenultimaPenResMod != null) { if (lMisAltMod.getDataFineMisura() !=
				 * null && lPenultimaPenResMod.getDataFine() != null) { if
				 * (lMisAltMod.getDataFineMisura().compareTo(lPenultimaPenResMod.getDataFine()) == 0) {
				 * lMisAltDao = new MisuraAlternativaDAO(lConn);
				 * lMisAltDao.setCondizioneUpdate(lMisAltMod.getIdMisuraAlternativa()); //DATA_FINE ULTIMA
				 * PENA_RESIDUA lMisAltDao.setDataFineMisura(lPenResMod.getDataFine()); lMisAltDao.update();
				 * lMisAltDao.stop(); } } }
				 */
				lMisAltSqlDao = new MisuraAlternativaSqlDAO(lConn);
				lMisAltDao = new MisuraAlternativaDAO(lConn);
				// lMisAltSqlDao.ricercaMisuraAlternativaByFascicoloOrdinanza(aFascicolo.getIdFascicoloSiep());
				lMisAltSqlDao.ricercaMisuraAlternativaCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisAltSqlDao.getModelByKey();

				if (lMisModel != null) {
					// Se la DATA_FINE_MISURA è successiva alla
					// DATA_FINE_PENA aggiorna la
					if (lMisModel.getDataFineMisura() != null && lPenResMod != null
							&& lPenResMod.getDataFine() != null
							&& lMisModel.getDataFineMisura().after(lPenResMod.getDataFine())) {
						lMisModel.setDataFineMisura(lPenResMod.getDataFine());

						lMisModel.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
						lMisModel.setDataInserimento(aEvento.getDataAggiornamento());
						lMisModel.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
						lMisAltDao.setDAOFromModelForUpdate(lMisModel);
						lMisAltDao.update();
						lMisAltDao.stop();

						// inserisco duplico occorrenza MA
						lMisModel.setEveIdEvento(aEvento.getIdEvento());
						lMisAltDao.setDAOFromModel(lMisModel);
						lMisAltDao.insert();
					}
				}
			}

			commit(lConn);

			// ------- EVENTO--------
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			rollback(lConnBlob);
			daoEx.printStackTrace();
			// Ticket#20260713011 - S2: messaggio utente esplicito invece dell'eccezione tecnica grezza
			// (F3BException.USER_MESSAGE viene mostrato all'utente in modo leggibile da ErrorPage.jsp,
			// il dettaglio tecnico resta comunque tracciato nei log sopra).
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile completare la validazione del provvedimento: l'operazione non e' stata registrata. "
					+ "Verificare che il provvedimento non sia gia' stato validato in precedenza e riprovare. "
					+ "Se il problema persiste contattare l'assistenza tecnica.");
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			// Ticket#20260713011 - S2: messaggio utente esplicito instead of raw system exception
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile completare la validazione del provvedimento a causa di un errore di sistema. "
					+ "Riprovare piu' tardi; se il problema persiste contattare l'assistenza tecnica.");
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lStatoDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lAnnSqlDao);
			cleanup(lAnnDao);
			cleanup(lFunDao);
			cleanup(lMisAltSqlDao);
			cleanup(lMisAltDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return aEvento;
	}

	/**
	 * Si inserisce la Misura alternativa... e la sua risposta
	 *
	 * @param aMisura
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciMAeOrdineScarcerazione(MisuraAlternativaAggregatoModel aMisura,
			MisuraAlternativaAggregatoModel aEvento) throws F3BException {

		Connection lConn = null;

		MisuraAlternativaDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = null;
		EventoDAO lEveDAO = null;
		NotificaDAO lNotDAO = null;
		DepositoOrdinanzaPcDAO lDepOrdDAO = null;
		AutoritaEsternaDAO lAutDao = null;
		TenoreDAO lTenDAO = null;
		FungibilitaDAO lFunDao = null;
		PenaResiduaDAO lPenResDao = null;

		EventoNotificaModel lEveRet = null;

		try {
			lConn = getDBTransaction();

			if (aMisura != null) { // Non Esiste una Misura alternativa da SIUS e la inseriamo...

				EventoNotificaModel lEventoNot = aMisura.getEventoNotifica();
				// inserimento evento
				EventoModel lEveMod = new EventoModel(aMisura.getEventoNotifica().getEvento());

				lEveDAO = new EventoDAO(lConn);
				lAutDao = new AutoritaEsternaDAO(lConn);
				lNotDAO = new NotificaDAO(lConn);
				lEveDAO.setDAOFromModel(lEveMod);
				BigDecimal lKeyEvento = null;
				lKeyEvento = lEveDAO.insert();
				BigDecimal lKeyAutorita = null;
				int count = 0;

				while (count < lEventoNot.getNotifiche().length) {

					if (lEventoNot.getNotifiche()[count] != null) {
						if (lEventoNot.getNotifiche()[count].getAutoritaEsterna() != null) {

							lAutDao.setRicercaByAutSede(
									lEventoNot.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								lAutDao.setDAOFromModel(aMisura.getEventoNotifica().getNotifiche()[count]
										.getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								lEventoNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								lEventoNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						lEventoNot.getNotifiche()[count].setEveIdEvento(lKeyEvento);

						lNotDAO.setDAOFromModel(lEventoNot.getNotifiche()[count]);
						lNotDAO.insert();
						lNotDAO.stop();

					}
					count++;
				}
				BigDecimal lKeyDepOrd = null;

				// insert Deposito Ordinanza
				lDepOrdDAO = new DepositoOrdinanzaPcDAO(lConn);
				DepositoOrdinanzaPcModel lDepPCMod = aMisura.getDepositoOrdinanzaPc();
				lDepPCMod.setIdEventoGenerato(lKeyEvento);

				lDepOrdDAO.setDAOFromModel(lDepPCMod);
				lKeyDepOrd = lDepOrdDAO.insert();

				// insert tenore
				lTenDAO = new TenoreDAO(lConn);

				lTenDAO.setDepOpidDepositoOrdinanzaPc(lKeyDepOrd);

				lTenDAO.setDAOFromModel((aMisura.getTenori())[0]);
				/*
				 * BigDecimal lKeyTenore = null; lKeyTenore =
				 */lTenDAO.insert();

				lMisMod = new MisuraAlternativaModel(aMisura.getMisuraAlternativa());

				// insert misura alternativa
				lMisMod.setEveIdEvento(lKeyEvento);

				lMisDao = new MisuraAlternativaDAO(lConn);
				lMisDao.setDAOFromModel(lMisMod);
				/*
				 * BigDecimal lKeyMA = null; lKeyMA =
				 */lMisDao.insert();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("inserita" + lMisMod);
			} // Fine inserimento Misura alternativa

			// Inserisco l'Evento di Ordine di scarcerazione
			lEveRet = this.inserisciOModificaMANotifica(aEvento.getEventoNotifica(), aEvento.getTipoMisura(),
					lConn);

			// Inserisci Pena Residua
			if (aEvento.getPenaResidua() != null) { // Se Esiste una pena residua per l'Evento in questione
													// non validato si cancella...
				lPenResDao = new PenaResiduaDAO(lConn);
				lPenResDao.setCondizioneUpdateByEventoNonValidato(lEveRet.getEvento().getIdEvento());
				lPenResDao.delete();
				lPenResDao.stop();

				aEvento.getPenaResidua().setEveIdEvento(lEveRet.getEvento().getIdEvento());
				lPenResDao.setDAOFromModel(aEvento.getPenaResidua());
				lPenResDao.insert();
			}

			if (aEvento.getFungibilita() != null) { // Inserisci Fungibilità
				lFunDao = new FungibilitaDAO(lConn);
				aEvento.getFungibilita().setEveIdEvento(lEveRet.getEvento().getIdEvento());
				lFunDao.setDAOFromModel(aEvento.getFungibilita());
				lFunDao.insert();
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("OrdineScarcerazioneController.ExInserisciMAeOrdineScarcerazione: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + sqe);
			throw new F3BException("OrdineScarcerazioneController.ExInserisciMAeOrdineScarcerazione: " + sqe);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			throw new F3BException("OrdineScarcerazioneController.ExInserisciMAeOrdineScarcerazione: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lEveDAO);
			cleanup(lNotDAO);
			cleanup(lDepOrdDAO);
			cleanup(lAutDao);
			cleanup(lTenDAO);
			cleanup(lFunDao);
			cleanup(lPenResDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * inserisciOModificaMANotifica
	 *
	 * @param aEvento
	 * @param tipoMisura
	 * @param aConn
	 * @return EventoNotificaModel inserito
	 * @throws F3BException
	 */
	protected EventoNotificaModel inserisciOModificaMANotifica(EventoNotificaModel aEvento, String tipoMisura,
			Connection aConn) throws Exception {

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;

		EventoNotificaModel lEveRet = null;

		try {
			lEveRet = new EventoNotificaModel(aEvento);
			lEveDao = new EventoDAO(aConn);
			lAutDao = new AutoritaEsternaDAO(aConn);
			lNotDao = new NotificaDAO(aConn);
			lCampoNotaDao = new CampoNotaDAO(aConn);

			lSqlDAO = new EventoSqlDAO(aConn);

			lSqlDAO.ricercaEventoMANonRegistratoMAByFascicoloSiep(
					aEvento.getEvento().getFasSieIdFascicoloSiep(), aEvento.getEvento().getEveIdEvento());

			EventoModel lEveModel = (EventoModel) lSqlDAO.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("evento non reg" + lEveModel);
			BigDecimal lKeyEvento = null;
			if (lEveModel == null) // Se non presente lo inserisce
			{
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
				aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				lEveDao.setDAOFromModel(aEvento.getEvento());

				lKeyEvento = lEveDao.insert();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" ###### Inserito evento id = " + lKeyEvento);
			} else // Se presente lo aggiorna
			{
				lKeyEvento = lEveModel.getIdEvento();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

				EventoModel lEveMod = aEvento.getEvento();
				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				lEveDao.setCodMotivo(aEvento.getEvento().getCodMotivo());
				lEveDao.setDataEmissione(lEveMod.getDataEmissione());
				lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
				lEveDao.setCodLuogoEmittente(lEveMod.getCodLuogoEmittente());
				lEveDao.setCodUfficioEmittente(lEveMod.getCodUfficioEmittente());
				lEveDao.setCodMagistrato(lEveMod.getCodMagistrato());
				lEveDao.setAnnoProtocollo(lEveMod.getAnnoProtocollo());

				lEveDao.setIdEvento(lKeyEvento);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
				// ************************************************************************

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" ###### Modificato evento id = " + lKeyEvento);
				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();

			}

			BigDecimal lKeyAutorita = null;
			int count = 0;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Presenti " + aEvento.getNotifiche().length + " notifiche");

			while (count < aEvento.getNotifiche().length) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Notifica[" + count + "] = " + aEvento.getNotifiche()[count]);
				if (aEvento.getNotifiche()[count] != null) {
					if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("autorità esterna@@@@@@@@@@@@@@@"
								+ aEvento.getNotifiche()[count].getAutoritaEsterna());
						lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();
						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di mLog
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("Inserito evento" + lKeyEvento);
				}
				count++;
			}

			// Inserimento delle eventuali note aggiuntive.
			if (aEvento.getCampoNote() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : "
						+ aEvento.getCampoNote().length);
				count = 0;
				while (count < aEvento.getCampoNote().length) {
					aEvento.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					aEvento.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEvento.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}
			// ----NO commmit ---commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw daoEx;
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			throw ex;
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
		}

		return lEveRet;
	}

	public EventoModel ExUpdateValidaOS(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		NomeProvvedimentoDAO lNomProvDao = null;
		FungibilitaDAO lFungiDAO = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();
			lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			// lEveSql = new EventoSqlDAO(lConn);
			// * Cerca L'EVENTO *
			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
			}
			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			// Vector lNotifiche = new Vector(lNotEveDao.getModels());

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// CERCA PENA RESIDUA
			lPenResDao = new PenaResiduaDAO(lConn);
			PenaResiduaModel lPenResMod = new PenaResiduaModel();
			// Controllo se IdEvento di PenaResidua è uguale a null, se è uguale a null Aggiorno PenaResidua
			// altrimenti Inserisco PenaResidua(sempre con l'evento corrente)
			IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			lPenResMod = lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			// lStatoProcMod.setProgressivo(new BigDecimal(1));

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			// ** POSIZIONE_GIURIDICA **
			// boolean lCodStatoAggiornato = false;
			// int lIntPos = Integer.parseInt(lCodPosizione);

			// Aggiorna PENA_RESIDUA
			if (lPenResMod.getEveIdEvento() == null) {
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
				lPenResDao.setDataFine(lPenResMod.getDataFine());
				lPenResDao.setEveIdEvento(aEvento.getIdEvento());
				lPenResDao.setFlagValidato(aEvento.getFlagDocumentoRegistrato());
				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				lPenResMod.setFlagValidato(aEvento.getFlagDocumentoRegistrato());
				// lEveSql.ricercaOrdineEsecuzioneByIdFascicoloDescrInserimento(aFascicolo.getIdFascicoloSiep());
				// EventoModel lEventoMod = (EventoModel) lEveSql.getModelByKey();
				lPenResMod.setEveIdEvento(aEvento.getIdEvento());
				lPenResMod.setDataInserimento(DateUtils.getSysDate());
				IPenaResidua lCtrlPen = SIEPLookupRemote.getPenaResiduaRemote();
				/* PenaResiduaModel llPenModRet = */lCtrlPen.ExInserisciPenaResidua(lPenResMod);
			}

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// ricerca dello stato procedimento per verificare se provengo
			// dal codice 0229 ossia dal ripristino detenzione in carcere
			lStatoSqlDao.ricercaStatoProcedimentoByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			StatoProcedimentoModel lStaProcMod = (StatoProcedimentoModel) lStatoSqlDao.getModelByKey();
			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();
			// Inserisci STATO Del PROCEDIMENTO
			if (lStaProcMod != null && "0229".equals(lStaProcMod.getCodStatoProcedimento())) {
				lStatoProcMod.setCodStatoProcedimento("0229");
				lStatoProcMod.setData(lEveApp.getDataEmissione());
				lStatoProcMod.setProgressivo(new BigDecimal(1));
				lStatoDao.setDAOFromModel(lStatoProcMod);
				lStatoDao.insert();
				lStatoDao.stop();
			} else if (!lPosMod.isMisAlt()) {
				// commentato da serena su modifiche viviana 20/07/04

				/*
				 * for (int ins = 1; ins < 3; ins++) { lStatoProcMod.setProgressivo(new BigDecimal(ins));
				 * lStatoDao.setDAOFromModel(lStatoProcMod); lStatoDao.insert(); lStatoDao.stop();
				 *
				 * if (ins == 1) { lStatoProcMod.setCodStatoProcedimento("0006"); } else {
				 */
				lStatoProcMod.setCodStatoProcedimento("0021");
				lStatoProcMod.setProgressivo(new BigDecimal(1));
				lStatoProcMod.setData(lEveApp.getDataEmissione());
				lStatoDao.setDAOFromModel(lStatoProcMod);

				lStatoDao.insert();
				lStatoDao.stop();

				lStatoProcMod.setCodStatoProcedimento("0010");
				lStatoProcMod.setData(lPenResMod.getDataFine());
				lStatoProcMod.setProgressivo(new BigDecimal(2));
				lStatoDao.setDAOFromModel(lStatoProcMod);
				lStatoDao.insert();
				lStatoDao.stop();
				// }
				// }
			} else {
				lStatoProcMod.setCodStatoProcedimento("0021");
				lStatoProcMod.setData(lEveApp.getDataEmissione());
				lStatoProcMod.setProgressivo(new BigDecimal(1));
				lStatoDao.setDAOFromModel(lStatoProcMod);
				lStatoDao.insert();
				lStatoDao.stop();
			}
			/*************************************************
			 * FINE
			 **************************************************************/

			/************************************************
			 * NOME PROVVEDIMENTO
			 ************************************************/
			NomeProvvedimentoModel lNomProvMod = new NomeProvvedimentoModel();
			lNomProvDao = new NomeProvvedimentoDAO(lConn);

			lNomProvMod.setCodNomeProvvedimento("NP022");

			lNomProvMod.setEveIdEvento(aEvento.getIdEvento());

			// [FT] - 13/07/2026 - Ticket #20260713011 - Cancellazione provvedimenti / validazione bloccata.
			// Stesso difetto individuato in ExUpdateValidaOSLibAnt: la insert su NOME_PROVVEDIMENTO non era
			// idempotente e falliva con ORA-00001 (vincolo di unicita' su EVE_ID_EVENTO) in caso di
			// rivalidazione dello stesso evento, con conseguente rollback silenzioso della transazione.
			// Si applica lo stesso pattern "cancella poi inserisci" gia' usato qui sopra per STATO_PROCEDIMENTO.
			lNomProvDao.setCondizioneByEveIdEvento(aEvento.getIdEvento());
			lNomProvDao.delete();

			lNomProvDao.setDAOFromModel(lNomProvMod);
			lNomProvDao.insert();
			/*********************************************
			 * FINE NOME PROVVEDIMENTO
			 **********************************************/

			/********************************************
			 * POSIZIONE GIURIDICA
			 **************************************************/

			// Aggiorna POSIZIONE_GIURIDICA

			lPosSqlDao.inserimentoAggiornamentoPosizioneGiuridica(lConn, lCodPosizione, lPosMod,
					DateUtils.getSysDate(), aEvento, aFascicolo.getIdFascicoloSiep(), aEvento.getIdEvento(),
					"S");

			/*
			 * lPosDao = new PosizioneGiuridicaDAO(lConn);
			 *
			 * lPosDao.setDataFine(DateUtils.getSysDate());
			 * lPosDao.setDataAggiornamento(DateUtils.getSysDate());
			 * lPosDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			 * lPosDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			 * lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());
			 *
			 * lPosDao.update(); lPosDao.stop();
			 *
			 * //inserisco la nuova posizione giuridica lPosDao.setCodPosizioneGiuridica(lCodPosizione);
			 * lPosDao.setCodPosizioneProcessuale("-");
			 * lPosDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			 * lPosDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			 * lPosDao.setDataInserimento(DateUtils.getSysDate());
			 * lPosDao.setDataInizio(DateUtils.getSysDate());
			 * lPosDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			 * lPosDao.setIdEventoRiferimento(aEvento.getIdEvento());
			 *
			 * lPosDao.insert(); lPosDao.stop();
			 */

			/********************************************
			 * FINE POSIZIONE GIURIDICA
			 ***********************************************/

			/*
			 * Aggiorna PENA_RESIDUA lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			 * lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			 * PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey(); lPenResDao =
			 * new PenaResiduaDAO(lConn); lPenResDao.setFlagValidato(aEvento.getFlagDocumentoRegistrato());
			 * lPenResDao.setEveIdEvento(aEvento.getIdEvento());
			 * lPenResDao.setCondizioneUpdate(lPenResMod.getIdPenaResidua()); lPenResDao.update();
			 * lPenResDao.stop();
			 */

			// SCADENZARIO FINE PENA

			// commentato da serena su modifiche viviana 20/07/04
			/*
			 * if (lPenResMod != null && lPenResMod.getFlagValidato().equals("S") && lPenResMod.getDataFine()
			 * != null) { ScadenzarioModel lScaMod = new ScadenzarioModel(); lScaDao = new
			 * ScadenzarioDAO(lConn); ScadenzarioSqlDAO lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			 * lScaSqlDao.ricercaScadenzarioByIdFascicolo(aFascicolo.getIdFascicoloSiep()); lScaMod =
			 * (ScadenzarioModel) lScaSqlDao.getModelByKey(); if (lScaMod != null) { //aggiorna scadenzario
			 * lScaDao.setDataFineScadenza(lPenResMod.getDataFine());
			 * lScaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			 * lScaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			 * lScaDao.setDataAggiornamento(DateUtils.getSysDate());
			 * //lScaModAgg.setIdScadenzario(lScaMod.getIdScadenzario());
			 * lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario()); lScaDao.update(); lScaDao.stop(); }
			 * else { lScaMod = new ScadenzarioModel(); //inserisce scadenzario
			 * lScaMod.setCodTipoScadenzario("02"); lScaMod.setDataFineScadenza(lPenResMod.getDataFine());
			 * lScaMod.setCodOperatoreInserimento(aFascicolo.getCodOperatoreInserimento());
			 * lScaMod.setDataInserimento(DateUtils.getSysDate());
			 * lScaMod.setCodUfficioInserimento(aFascicolo.getCodOperatoreInserimento());
			 * lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			 * lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio()); lScaDao.setDAOFromModel(lScaMod);
			 * lScaDao.insert(); lScaDao.stop(); } }
			 */
			// Se c'è Fungibilità -->Aggiorno nella TABELLA FUNGIBILITA' FLAG_VALIDATO = S

			// FungibilitaModel lFungiMod = new FungibilitaModel();
			// FungibilitaSqlDAO lFungiSql = new FungibilitaSqlDAO(lConn);
			// lFungiSql.ricercaFungibilitaByKeyEvento(aEvento.getIdEvento());
			// lFungiMod = (FungibilitaModel) lFungiSql.getModelByKey();
			FungibilitaModel lFungiMod = new FungibilitaModel();
			IFungibilita lFungiCtrl = SIEPLookupRemote.getFungibilitaRemote();
			lFungiMod = lFungiCtrl.ExRicercaFungibilitaByKeyEvento(aEvento.getIdEvento());

			lFungiDAO = new FungibilitaDAO(lConn);

			if (lFungiMod != null && lFungiMod.getFlagValidato().equals("N")) {
				lFungiMod.setDataAggiornamento(DateUtils.getSysDate());
				lFungiMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lFungiMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lFungiMod.setFlagValidato("S");
				lFungiDAO.setDAOFromModelForUpdate(lFungiMod);
				lFungiDAO.update();
				lFungiDAO.stop();

			}

			commit(lConn);

			// ------- EVENTO--------
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			rollback(lConnBlob);
			daoEx.printStackTrace();
			// Ticket#20260713011 - S2: messaggio utente esplicito invece dell'eccezione tecnica grezza
			// (F3BException.USER_MESSAGE viene mostrato all'utente in modo leggibile da ErrorPage.jsp,
			// il dettaglio tecnico resta comunque tracciato nei log sopra).
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile completare la validazione del provvedimento: l'operazione non e' stata registrata. "
					+ "Verificare che il provvedimento non sia gia' stato validato in precedenza e riprovare. "
					+ "Se il problema persiste contattare l'assistenza tecnica.");
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			// Ticket#20260713011 - S2: messaggio utente esplicito instead of raw system exception
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile completare la validazione del provvedimento a causa di un errore di sistema. "
					+ "Riprovare piu' tardi; se il problema persiste contattare l'assistenza tecnica.");
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lScaDao);
			cleanup(lNomProvDao);
			cleanup(lFungiDAO);
			cleanup(lStatoSqlDao);
			cleanup(lConn);

			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * Effettua la validazione degli Ordini di Scarcerazione per nuova scadenza pena a seguito di concessione
	 * LA. Vengono validati: - L'ordine di Scarcerazione - L'ordinanza di concessione (e (DepositoOrdinanzaPC)
	 * collegata all'OS - Le LA collegate all'ordinanza (FLAG_ELABORATO a S) - L'ultima pena residua trovata
	 * (validata o meno :-( ) Vengono aggiornati: - stato procedimento - nome procedimento - La misura
	 * alternativa (data fine) se il condannato è in misura - Lo scadenzario fine pena
	 *
	 * @param aEvento
	 *            - Ordine di scarcerazione da validare
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaOSLibAnt(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScadeDao = null;
		NomeProvvedimentoDAO lNomProvDao = null;
		EventoSqlDAO lEveSql = null;
		FungibilitaDAO lFunDao = null;
		FungibilitaSqlDAO lFunSqlDao = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		MisuraAlternativaDAO lMisDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		MisuraAlternativaModel lMisModel = null;
		DepositoOrdinanzaPcSqlDAO lDepSql = null;
		DepositoOrdinanzaPcDAO lDepDAO = null;
		DepositoOrdinanzaPcModel lDepMod = null;
		LicenzaLibanticipataSqlDAO lLibSql = null;
		LicenzaLibanticipataDAO lLibDAO = null;
		LicenzaLibAnticipataModel lLibMod = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;
		EventoDAO lEventoDao = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			lEveSql = new EventoSqlDAO(lConn);
			// * Cerca L'EVENTO *
			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				lEveApp.setEveIdEvento(lEveDao.getEveIdEvento());
			}

			// ========================================================================
			// AGGIORNA LIBERAZIONE ANTICIPATA.
			// - valida l'Ordinanza di concessione
			// - il depositoOrdinanzaPC
			// - la LA ponendo il flag_elaborato a S
			// ========================================================================
			lLibSql = new LicenzaLibanticipataSqlDAO(lConn);
			lLibDAO = new LicenzaLibanticipataDAO(lConn);
			lLibMod = new LicenzaLibAnticipataModel();
			lDepSql = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDAO = new DepositoOrdinanzaPcDAO(lConn);
			lDepMod = new DepositoOrdinanzaPcModel();

			// lLibSql.ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP(aFascicolo.getIdFascicoloSiep(),
			// "E");
			// Ricerca le LA collegate all'Ordinanza puntata dall'Ordine di Scarcerazione
			lLibSql.ricercaLicenzaLibanticipataByEve(lEveApp.getEveIdEvento());

			Vector lLibVect = new Vector(lLibSql.getModels());
			lEventoDao = new EventoDAO(lConn);

			for (int i = 0; i < lLibVect.size(); i++) {
				lLibMod = (LicenzaLibAnticipataModel) lLibVect.get(i);

				// Cerca l'evento legato alla Liberazione Anticipata (è un'ordinanza)
				EventoModel lEvePresente = null;
				if (lLibMod.getEveIdEvento() != null) {
					lEveSql.ricercaEventoByKey(lLibMod.getEveIdEvento());
					lEvePresente = (EventoModel) lEveSql.getModelByKey();
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("lEvePresente--->" + lEvePresente);

				// Valido l'ordinanza - e il DepositoOrdinanzaPC
				if (lEvePresente != null) {
					lEventoDao.setIdEvento(lEvePresente.getIdEvento());
					lEventoDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEventoDao.setDataAggiornamento(aEvento.getDataAggiornamento());
					lEventoDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEventoDao.setFlagDocumentoRegistrato("S");
					lEventoDao.selByKey();
					lEventoDao.update();
					lEventoDao.stop();

					lDepSql.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEvePresente.getIdEvento());
					lDepMod = (DepositoOrdinanzaPcModel) lDepSql.getModelByKey();

					if (lDepMod != null) {
						lDepDAO.setIdDepositoOrdinanzaPc(lDepMod.getIdDepositoOrdinanzaPc());
						lDepDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lDepDAO.setDataAggiornamento(aEvento.getDataAggiornamento());
						lDepDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
						lDepDAO.setFlagElaborato("S");
						lDepDAO.selByKey();
						lDepDAO.update();
						lDepDAO.stop();
					}
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("liberazione anticipata-->" + lLibMod);
				// Valida la LA
				lLibMod.setFlagElaborato("S");
				lLibMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lLibMod.setDataAggiornamento(aEvento.getDataAggiornamento());
				lLibMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

				lLibDAO.setDAOFromModelForUpdate(lLibMod);
				lLibDAO.update();
				lLibDAO.stop();
			}

			lEventoDao.stop();
			lLibDAO.stop();
			lDepDAO.stop();

			// ========================================================================
			// Aggiorna PENA_RESIDUA
			// Since 4.0upd02. La pena da aggiornare viene agganciata subito all'evento
			// quindi va ricercata quella collegata all'OS.
			// Se non presente si ricerca l'ultimo a sistema purchè non collegata ad
			// alcun evento (vecchia versione).
			// ========================================================================
			lPenResDao = new PenaResiduaDAO(lConn);
			PenaResiduaModel lPenResMod = new PenaResiduaModel();
			// Ricerco la pena residua legata all'evento se esiste
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEvento.getIdEvento());
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			if (lPenResMod != null && lPenResMod.getIdPenaResidua() != null) {
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());

				lPenResDao.setFlagValidato(aEvento.getFlagDocumentoRegistrato());

				lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPenResDao.setDataAggiornamento(aEvento.getDataAggiornamento());

				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				// Controllo se IdEvento di PenaResidua è uguale a null, se è uguale a null Aggiorno
				// PenaResidua altrimenti Inserisco PenaResidua(sempre con l'evento corrente)
				IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
				lPenResMod = lCtrl
						.ExRicercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());

				if (lPenResMod.getEveIdEvento() == null) {
					lPenResDao.setEveIdEvento(aEvento.getIdEvento());

					lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
					lPenResDao.setDataFine(lPenResMod.getDataFine());
					lPenResDao.setFlagValidato(aEvento.getFlagDocumentoRegistrato());
					lPenResDao.selByKey();
					lPenResDao.update();
					lPenResDao.stop();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("§§§Sto nell'aggiornamento!!");
				} else {
					lPenResMod.setEveIdEvento(aEvento.getIdEvento());
					lPenResMod.setFlagValidato(aEvento.getFlagDocumentoRegistrato());
					lPenResMod.setDataInserimento(DateUtils.getSysDate());

					IPenaResidua lCtrlPen = SIEPLookupRemote.getPenaResiduaRemote();
					// Attenzione alla transazione (dalla v. 10 su VSS 05/03/2004) Vedi anche ExUpdateValidaOS
					/* PenaResiduaModel llPenModRet = */lCtrlPen.ExInserisciPenaResidua(lPenResMod);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("§§§Sto nell'inserisci!!");
				}
			}

			// ========================================================================
			// Aggiorna STATO_PROCEDIMENTO
			// 0020 - Emesso Ordine di Scarcerazione per Concessione Liberazione Anticipata il
			// 0010 - Pena in Esecuzione Fino al
			// ========================================================================
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			// Inserisci STATO Del PROCEDIMENTO
			// ** POSIZIONE_GIURIDICA **
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			// int lIntPos = Integer.parseInt(lCodPosizione);
			// if (lIntPos == 03 || lIntPos == 12 || lIntPos == 14 || lIntPos == 04 ||
			// aFascicolo.getFlagAltraCausa().equals("S"))
			// {
			lStatoProcMod.setData(lEveApp.getDataEmissione());

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoProcMod.setCodStatoProcedimento("0020");
			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			lStatoProcMod.setData(lPenResMod.getDataFine());
			lStatoProcMod.setCodStatoProcedimento("0010");
			lStatoProcMod.setProgressivo(new BigDecimal(2));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// }
			// else
			/*
			 * { lStatoProcMod.setCodStatoProcedimento("0021"); lStatoProcMod.setProgressivo(new
			 * BigDecimal(1)); lStatoDao.setDAOFromModel(lStatoProcMod); lStatoDao.insert(); lStatoDao.stop();
			 * }
			 */
			/*************************************************
			 * FINE
			 **************************************************************/
			/************************************************
			 * NOME PROVVEDIMENTO
			 ************************************************/
			NomeProvvedimentoModel lNomProvMod = new NomeProvvedimentoModel();
			lNomProvDao = new NomeProvvedimentoDAO(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("CODMOTIVO = " + lEveDao.getCodMotivo());

			if (lEveDao.getCodMotivo().equals("0081"))
				lNomProvMod.setCodNomeProvvedimento("NP048");
			else if (lEveDao.getCodMotivo().equals("0083"))
				lNomProvMod.setCodNomeProvvedimento("NP049");

			lNomProvMod.setEveIdEvento(aEvento.getIdEvento());

			// [FT] - 13/07/2026 - Ticket #20260713011 - Cancellazione provvedimenti / validazione bloccata.
			// La insert su NOME_PROVVEDIMENTO non era idempotente: in caso di rivalidazione dello stesso
			// evento (es. doppio click su "Conferma" mentre la pagina risultava bloccata, o ripetizione
			// della validazione) l'insert falliva con ORA-00001 (violazione vincolo di unicita' su
			// EVE_ID_EVENTO), la transazione veniva annullata (rollback) e l'utente non riceveva alcun
			// messaggio di errore (la casella di validazione restava semplicemente grigia).
			// Si applica qui lo stesso pattern "cancella poi inserisci" gia' utilizzato sopra per
			// STATO_PROCEDIMENTO, rendendo l'operazione ripetibile senza errori.
			lNomProvDao.setCondizioneByEveIdEvento(aEvento.getIdEvento());
			lNomProvDao.delete();

			lNomProvDao.setDAOFromModel(lNomProvMod);
			lNomProvDao.insert();

			// ========================================================================
			// AGGIORNA MISURA ALTERNATIVA (data fine misura
			// ========================================================================
			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			if (lPosMod.isMisAlt()) {
				lMisDAO = new MisuraAlternativaDAO(lConn);
				lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
				// lMisSqlDAO.ricercaMisuraAlternativaByFascicoloOrdinanza(aFascicolo.getIdFascicoloSiep());
				lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				lMisModel = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

				if (lMisModel != null) {
					// Se la DATA_FINE_MISURA è successiva alla
					// DATA_FINE_PENA aggiorna la
					if (lMisModel.getDataFineMisura() != null && lPenResMod != null
							&& lPenResMod.getDataFine() != null
							&& lMisModel.getDataFineMisura().after(lPenResMod.getDataFine())) {
						lMisModel.setDataFineMisura(lPenResMod.getDataFine());

						lMisModel.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
						lMisModel.setDataInserimento(aEvento.getDataAggiornamento());
						lMisModel.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

						lMisDAO.setDAOFromModelForUpdate(lMisModel);
						lMisDAO.update();
						lMisDAO.stop();

						// inserisco duplico occorrenza MA agganciandola all'ordine di scarcerazione
						lMisModel.setEveIdEvento(aEvento.getIdEvento());
						lMisDAO.setDAOFromModel(lMisModel);
						lMisDAO.insert();
					}
				}
			}

			/*
			 * Aggiorna PENA_RESIDUA lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			 * lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			 * PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey(); lPenResDao =
			 * new PenaResiduaDAO(lConn); lPenResDao.setFlagValidato(aEvento.getFlagDocumentoRegistrato());
			 * lPenResDao.setEveIdEvento(aEvento.getIdEvento());
			 * lPenResDao.setCondizioneUpdate(lPenResMod.getIdPenaResidua()); lPenResDao.update();
			 * lPenResDao.stop();
			 */

			// SCADENZARIO FINE PENA

			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			ScadenzarioModel lScaMod = new ScadenzarioModel();
			if (!lPosMod.isMisAlt()) {
				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("02",
						aFascicolo.getIdFascicoloSiep());
				lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("aggiorna scadenzario--->" + lScaMod);

				if (lScaMod != null) {
					// aggiorna scadenzario
					lScaDao.setDataFineScadenza(lPenResMod.getDataFine());
					lScaDao.setDataInizioScadenza(lPenResMod.getDataInizio());
					lScaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDao.setDataAggiornamento(DateUtils.getSysDate());
					lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDao.update();
					lScaDao.stop();

				} else {

					lScaMod = new ScadenzarioModel();
					// inserisce scadenzario
					lScaMod.setCodTipoScadenzario("02");
					lScaMod.setDataFineScadenza(lPenResMod.getDataFine());
					lScaMod.setCodOperatoreInserimento(aFascicolo.getCodOperatoreInserimento());
					lScaMod.setDataInserimento(DateUtils.getSysDate());
					lScaMod.setCodUfficioInserimento(aFascicolo.getCodUfficioInserimento());
					lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio());
					lScaDao.setDAOFromModel(lScaMod);
					lScaDao.insert();
					lScaDao.stop();

				}
			} else // mis_alt
			{
				ScadenzarioModel lScaModMis = new ScadenzarioModel();

				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("13",
						aFascicolo.getIdFascicoloSiep());
				lScaModMis = (ScadenzarioModel) lScaSqlDao.getModelByKey();

				if (lScaModMis != null) {
					// aggiorna scadenzario
					lScaDao.setDataFineScadenza(lPenResMod.getDataFine());
					lScaDao.setDataInizioScadenza(lPenResMod.getDataInizio());
					lScaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDao.setDataAggiornamento(DateUtils.getSysDate());
					lScaDao.setCondizioneUpdate(lScaModMis.getIdScadenzario());
					lScaDao.update();
					lScaDao.stop();

				} else {

					lScaMod = new ScadenzarioModel();
					// inserisce scadenzario
					lScaMod.setCodTipoScadenzario("13");
					lScaMod.setDataFineScadenza(lPenResMod.getDataFine());
					lScaMod.setCodOperatoreInserimento(aFascicolo.getCodOperatoreInserimento());
					lScaMod.setDataInserimento(DateUtils.getSysDate());
					lScaMod.setCodUfficioInserimento(aFascicolo.getCodUfficioInserimento());
					lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio());
					lScaDao.setDAOFromModel(lScaMod);
					lScaDao.insert();
					lScaDao.stop();
				}
			}

			/*
			 * FungibilitaModel lFungiMod = new FungibilitaModel(); IFungibilita lFungiCtrl =
			 * SIEPLookupRemote.getFungibilitaRemote(); lFungiMod =
			 * lFungiCtrl.ExRicercaFungibilitaByKeyEvento(aEvento.getIdEvento()); lFungiDAO = new
			 * FungibilitaDAO(lConn); if (lFungiMod != null && lFungiMod.getFlagValidato().equals("N")) {
			 * lFungiMod.setDataAggiornamento(DateUtils.getSysDate());
			 * lFungiMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			 * lFungiMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			 * lFungiMod.setFlagValidato("S"); lFungiDAO.setDAOFromModelForUpdate(lFungiMod);
			 * lFungiDAO.update(); lFungiDAO.stop(); }
			 */

			// ========================================================================
			//
			// ========================================================================
			lFunDao = new FungibilitaDAO(lConn);
			lFunSqlDao = new FungibilitaSqlDAO(lConn);

			FungibilitaModel lFunMod = new FungibilitaModel();

			lFunSqlDao.ricercaFungibilitaDescByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lFunMod = (FungibilitaModel) lFunSqlDao.getModelByKey();
			if (lFunMod != null) {
				lFunDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lFunDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lFunDao.setDataAggiornamento(aEvento.getDataAggiornamento());
				lFunDao.setFlagValidato("S");
				lFunDao.setEveIdEvento(aEvento.getIdEvento());

				lFunDao.setCondizioneUpdate(lFunMod.getIdFungibilita());
				lFunDao.update();
				lFunDao.stop();
			}

			commit(lConn);

			// ------- EVENTO--------
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			rollback(lConnBlob);
			daoEx.printStackTrace();
			// Ticket#20260713011 - S2: messaggio utente esplicito invece dell'eccezione tecnica grezza
			// (F3BException.USER_MESSAGE viene mostrato all'utente in modo leggibile da ErrorPage.jsp,
			// il dettaglio tecnico resta comunque tracciato nei log sopra).
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile completare la validazione del provvedimento: l'operazione non e' stata registrata. "
					+ "Verificare che il provvedimento non sia gia' stato validato in precedenza e riprovare. "
					+ "Se il problema persiste contattare l'assistenza tecnica.");
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			// Ticket#20260713011 - S2: messaggio utente esplicito instead of raw system exception
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile completare la validazione del provvedimento a causa di un errore di sistema. "
					+ "Riprovare piu' tardi; se il problema persiste contattare l'assistenza tecnica.");
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lScadeDao); // sca
			cleanup(lEveSql);
			cleanup(lLibSql);
			cleanup(lLibDAO);
			cleanup(lDepSql);
			cleanup(lDepDAO);
			cleanup(lEveDaoBlob);
			cleanup(lEventoDao);
			cleanup(lMisDAO);
			cleanup(lMisSqlDAO);
			cleanup(lNomProvDao);
			cleanup(lFunDao);
			cleanup(lFunSqlDao);
			cleanup(lConn);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	public EventoNotificaModel ExInserisciOModificaNotifica(EventoNotificaModel aEvento) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;

		BigDecimal lKeyEvento = null;
		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);

			lSqlDAO = new EventoSqlDAO(lConn);
			lSqlDAO.ricercaEventoNonRegistrato(aEvento.getEvento());
			EventoModel lEve = (EventoModel) lSqlDAO.getModelByKey();
			if (lEve != null) {
				lKeyEvento = lEve.getIdEvento();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("lEveRet--->" + lEve);
				aEvento.getEvento().setIdEvento(lKeyEvento);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("aEvento--->" + aEvento);

				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setDAOFromModelForUpdate(aEvento.getEvento());
				lEveDao.update();
				lEveDao.stop();
				lEveRet.getEvento().setIdEvento(lKeyEvento);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("");

			} else {
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
				aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				lEveDao.setDAOFromModel(aEvento.getEvento());

				lKeyEvento = lEveDao.insert();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" ###### Modificato evento id = " + lKeyEvento);
			}
			// ************************************************************************
			lNotDao.setCondizioneEvento(lKeyEvento);

			lNotDao.delete();
			lNotDao.stop();

			BigDecimal lKeyAutorita = null;
			int count = 0;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Presenti " + aEvento.getNotifiche().length + " notifiche");

			while (count < aEvento.getNotifiche().length) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Notifica[" + count + "] = " + aEvento.getNotifiche()[count]);

				if (aEvento.getNotifiche()[count] != null) {

					if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di mLog
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("Inserito evento" + lKeyEvento);
				}

				count++;
			}

			// Inserimento delle eventuali note aggiuntive.
			if (aEvento.getCampoNote() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : "
						+ aEvento.getCampoNote().length);
				count = 0;
				while (count < aEvento.getCampoNote().length) {
					aEvento.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					aEvento.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEvento.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("OrdineScarcerazioneController.ExInserisciOModificaNotifica: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineScarcerazioneController.ExInserisciOModificaNotifica: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);

			cleanup(lConn);
		}

		return lEveRet;
	}

}