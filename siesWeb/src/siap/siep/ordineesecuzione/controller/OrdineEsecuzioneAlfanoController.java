package siap.siep.ordineesecuzione.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.stampa.controller.IStampa;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.nomeprovvedimento.model.NomeProvvedimentoModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: OrdineEsecuzioneAlfanoController
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class OrdineEsecuzioneAlfanoController extends SiapController implements IOrdineEsecuzioneAlfano {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * legge 199/2010 (Decreto Alfano)
	 * 
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaLAlfano(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		NomeProvvedimentoDAO lNomProvDao = null;
		EventoSqlDAO lEveSql = null;
		LicenzaLibanticipataSqlDAO lLicSqlDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

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
				lEveApp.setCodMotivo(lEveDao.getCodMotivo());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
			}

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			Vector lNotifiche = new Vector(lNotEveDao.getModels());

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			// lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveApp.getDataEmissione());

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			// Aggiorna PENA_RESIDUA
			lPenResDao = new PenaResiduaDAO(lConn);
			PenaResiduaModel lPenResMod = new PenaResiduaModel();
			// Controllo se IdEvento di PenaResidua e' uguale a null, se e' uguale a null Aggiorno PenaResidua
			// altrimenti Inserisco
			IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			lPenResMod = lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
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
				lPenResMod.setEveIdEvento(aEvento.getIdEvento());
				lPenResMod.setDataInserimento(DateUtils.getSysDate());
				PenaResiduaDAO lPenDao = new PenaResiduaDAO(lConn);
				lPenDao.setDAOFromModel(lPenResMod);
				lPenDao.insert();
			}

			// ** POSIZIONE_GIURIDICA **
			boolean lCodStatoAggiornato = false;
			if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) // Altra
																										// causa
			{
				lStatoProcMod.setCodStatoProcedimento("0011");
				lCodStatoAggiornato = true;
			}

			int lIntPos = Integer.parseInt(lCodPosizione);
			String lPosizione = null;
			switch (lIntPos) {

			case 1: // Custodia Cautelare Detenuto per questa causa
			{
				lPosizione = "03";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0412");
				break;
			}
			case 2: // Custodia Cautelare Arresti domiciliari per questa causa
			{
				lPosizione = "53";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0412");
				break;
			}
			case 3: // Espiazione Pena in Regime Carcerario per questa causa - Da verificare se trattato!
			{
				lPosizione = "03";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0412");
				break;
			}
			case 4: // Arresti domiciliari
			{
				// lCodPosizione = "04"; 14/10/03 modifica
				lPosizione = "04";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0011");
				break;
			}
			// MEV 10 S3 ******************
			case 70: // Custodia Cautelare per Questa Causa in Regime di Arresti Domiciliari ex art 89 dpr
						// 309/90
			{
				lPosizione = "87";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0412");
				break;
			}
			case 71: // Custodia Cautelare per Questa Causa in Regime di Permanenza in Casa
			{
				lPosizione = "85";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0412");
				break;
			}
			case 72: // Custodia Cautelare per Questa Causa Collocamento in Comunità
			{
				lPosizione = "86";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0412");
				break;
			}
			// *****************************
			case 7:
			case 10: // Libero
			case 46: // Libero
			case 47: // Libero
			case 16: // Libero
			case 17: // Libero
			case 20: // Libero
			case 26: // Libero
			case 30: // Libero
			{
				lPosizione = "10";
				if (!lCodStatoAggiornato)
					// /lStatoProcMod.setCodStatoProcedimento("0011");
					// 13/12/2010 Distinzione del Decreto Sospensione dall'ordine di esecuzione
					if (lEveApp.getCodMotivo().compareTo("0499") == 0)
						lStatoProcMod.setCodStatoProcedimento("0411");
					// MEV 10 S3 gestite le posizioni giuridiche 76 e 77
					else if (lEveApp.getCodMotivo().compareTo("5525") == 0)
						lStatoProcMod.setCodStatoProcedimento("0011");
					else if (lEveApp.getCodMotivo().compareTo("5526") == 0)
						lStatoProcMod.setCodStatoProcedimento("0011");
					else
						lStatoProcMod.setCodStatoProcedimento("0406");
				break;
			}
			default: // altri casi
			{
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0011");
				lPosizione = lCodPosizione;
				break;
			}
			}

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// NOME PROVVEDIMENTO
			NomeProvvedimentoModel lNomProvMod = new NomeProvvedimentoModel();
			lNomProvDao = new NomeProvvedimentoDAO(lConn);

			if (lIntPos == 2 || lIntPos == 4) {
				// /lNomProvMod.setCodNomeProvvedimento("NP016");
				lNomProvMod.setCodNomeProvvedimento("NP234");
			} else {
				// /lNomProvMod.setCodNomeProvvedimento("NP015");
				lNomProvMod.setCodNomeProvvedimento("NP233");
			}

			// Aggiornato per posizione 2. Aggiunto per posizione 1
			if (lIntPos == 1)
				lNomProvMod.setCodNomeProvvedimento("NP235");
			if (lIntPos == 2)
				lNomProvMod.setCodNomeProvvedimento("NP236");

			lNomProvMod.setEveIdEvento(aEvento.getIdEvento());
			lNomProvDao.setDAOFromModel(lNomProvMod);
			lNomProvDao.insert();

			// Aggiorna POSIZIONE_GIURIDICA
			if (lCodPosizione.equals("01") || lCodPosizione.equals("02") || lCodPosizione.equals("03")
					|| lCodPosizione.equals("07") || lCodPosizione.equals("10")
					// MEV 10 S3
					|| lCodPosizione.equals("70") || lCodPosizione.equals("71") || lCodPosizione.equals("72")
					|| (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S"))) {
				lPosDao = new PosizioneGiuridicaDAO(lConn);

				lPosDao.setDataFine(DateUtils.getSysDate());
				lPosDao.setDataAggiornamento(DateUtils.getSysDate());
				lPosDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());

				lPosDao.update();
				lPosDao.stop();

				// Inserimento della nuova posizione giuridica
				lPosDao.setCodPosizioneGiuridica(lPosizione);
				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(DateUtils.getSysDate());
				lPosDao.setDataInizio(DateUtils.getSysDate());
				lPosDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPosDao.setIdEventoRiferimento(aEvento.getIdEvento()); // **

				BigDecimal lIdPos = lPosDao.insert();
				lPosDao.stop();

				if (lCodPosizione.equals("01") || lCodPosizione.equals("03")) { // Per la posizione 01 di
																				// partenza è richiesto il
																				// nuovo luogo detenzione
					lLuoDetDao = new LuogoDetenzioneDAO(lConn); // che è quello relativo all'istituto di
																// notifica
					if (lNotifiche != null && lNotifiche.firstElement() != null) {
						NotificaModel lFirstNot = (NotificaModel) lNotifiche.firstElement();
						if (lFirstNot.getIstDetIdIstitutoDetenzione() != null) {
							lLuoDetDao.setIstDetIdIstitutoDetenzione(lFirstNot
									.getIstDetIdIstitutoDetenzione());
							lLuoDetDao.setPosGiuIdPosizioneGiuridica(lIdPos);
							lLuoDetDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
							lLuoDetDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
							lLuoDetDao.setDataInserimento(DateUtils.getSysDate());
							lLuoDetDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

							lLuoDetDao.insert();
							lLuoDetDao.stop();
						}
					}
				}
			}

			// Aggiorna i giorni di Lib Anticipata computati non elaborati ad E
			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicSqlDao.updateFlagElaboratoByIdFascicolo(aFascicolo.getIdFascicoloSiep(), "E", "S");

			commit(lConn);

			// EVENTO
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			rollback(lConn);
			rollback(lConnBlob);
			daoEx.printStackTrace();
			throw new F3BException("OrdineEsecuzioneAlfanoController.ExUpdateValidaLAlfano : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Exception: " + ex);
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException("OrdineEsecuzioneAlfanoController.ExUpdateValidaLAlfano : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lNomProvDao);
			cleanup(lLicSqlDao);
			cleanup(lEveSql);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * Stampa trasferimento Provvedimento
	 * 
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaTrasmissioneProvvedimento(EventoNotificaModel aEvento,
			UtenteModel aUtente) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;
		try {
			IEvento lEvCrtl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEventoModel = lEvCrtl.ExRicercaEventoNotificaByKey(aEvento.getEvento()
					.getIdEvento());

			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			TreeModel lTree = lStampa.prelevaDatiEventoSiep(lEventoModel, aUtente);

			ReportGenerator lReport = new ReportGenerator();

			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

//			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("EVENTO >>> " + aEvento.getEvento().toString());

			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateUfficioDestinatario(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (Exception Ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + Ex);
			throw new F3BException("OrdineEsecuzioneAlfanoController.ExStampaTrasmissioneProvvedimento: "
					+ Ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * METODO DI INSERIMENTO LEGGE SIMEONE
	 * 
	 * @param aEvento
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaLAlfanoNotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaDAO lPenaResDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);

			// Cerca L'OE se presente (By IdFascicolo)
			lSqlDAO = new EventoSqlDAO(lConn);

			// lSqlDAO.ricercaOrdineEsecuzioneLSNonRegistratoByFascicoloSiep(aEvento.getEvento().getFasSieIdFascicoloSiep());
			lSqlDAO.ricercaDecretoSospensioneLANonRegistratoByFascicoloSiep(aEvento.getEvento()
					.getFasSieIdFascicoloSiep());
			EventoModel lEvePresente = (EventoModel) lSqlDAO.getModelByKey();

			BigDecimal lKeyEvento = null;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info(" Evento da inserire : " + aEvento.getEvento().toString());
			if (lEvePresente == null) // Se non presente lo inserisce
			{
				// Setto l'anno e il progressivo.
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
				aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				lEveDao.setDAOFromModel(aEvento.getEvento());

				lKeyEvento = lEveDao.insert();
				lEveRet.getEvento().setIdEvento(lKeyEvento);
			} else {// Se presente lo aggiorna
				lKeyEvento = lEvePresente.getIdEvento();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

				EventoModel lEveMod = aEvento.getEvento();
				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				// lEveDao.setFlagDocumentoRegistrato("N");
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

				// *********** Cancella le NOTIFICHE associate all' EVENTO **************
				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();
				// **********************************************************************
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
						lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
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

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
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

			lPenaResDao = new PenaResiduaDAO(lConn);

			if (aPenaResidua != null && aPenaResidua.getIdPenaResidua() != null) {
				lPenaResDao.setIdPenaResidua(aPenaResidua.getIdPenaResidua());

				lPenaResDao.setDataFine(aPenaResidua.getDataFine());

				lPenaResDao.selByKey();
				lPenaResDao.update();
				lPenaResDao.stop();
			} else if (aPenaResidua != null) {
				aPenaResidua.setEveIdEvento(lKeyEvento);

				lPenaResDao.setDAOFromModel(aPenaResidua);
				lPenaResDao.insert();
				lPenaResDao.stop();
			}
			commit(lConn);
		}

		catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneAlfanoController.ExInserisciOModificaLSNotifica: "
					+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneAlfanoController.ExInserisciOModificaLSNotifica: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lPenaResDao);
			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * legge 199/2010 (Decreto Alfano)
	 * 
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaL78del2013(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosGiuDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		NomeProvvedimentoDAO lNomProvDao = null;
		EventoSqlDAO lEveSql = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

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
				lEveApp.setCodMotivo(lEveDao.getCodMotivo());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
			}

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
//			Vector lNotifiche = new Vector(lNotEveDao.getModels());

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			lPosGiuDao = new PosizioneGiuridicaDAO(lConn);

			// 10/2013 -- Aggiorno Pos Giu se O.E L78/2013
			if (aEvento.getCodMotivo().equals("1024")) {
				lPosGiuDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
				lPosGiuDao.setDataFine(DateUtils.getSysDate());
				// lPosGiuDao.setDataFine(aEvento.getDataEmissione());
				lPosGiuDao.setCodOperatoreAggiornamento(aFascicolo.getCodOperatoreAggiornamento());
				lPosGiuDao.setCodUfficioAggiornamento(aFascicolo.getCodUfficioAggiornamento());
				lPosGiuDao.setDataAggiornamento(DateUtils.getSysDate());

				lPosGiuDao.selByKey();
				lPosGiuDao.update();

				// Inserisce la nuova occorrenza

				lPosGiuDao = new PosizioneGiuridicaDAO(lConn);

				lPosGiuDao.setCodPosizioneGiuridica("03");
				lPosGiuDao.setCodPosizioneProcessuale("-");
				lPosGiuDao.setCodUfficioInserimento(aFascicolo.getCodUfficioInserimento());
				lPosGiuDao.setCodOperatoreInserimento(aFascicolo.getCodOperatoreInserimento());
				lPosGiuDao.setDataInserimento(DateUtils.getSysDate());
				// lPosGiuDao.setDataInizio(aEvento.getDataEmissione());
				lPosGiuDao.setDataInizio(DateUtils.getSysDate());
				lPosGiuDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPosGiuDao.setIdEventoRiferimento(aEvento.getIdEvento());

				lPosGiuDao.insert();
				lPosGiuDao.stop();

			}

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			// lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveApp.getDataEmissione());

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			// Aggiorna PENA_RESIDUA
			lPenResDao = new PenaResiduaDAO(lConn);
			PenaResiduaModel lPenResMod = new PenaResiduaModel();
			// Controllo se IdEvento di PenaResidua e' uguale a null, se e' uguale a null Aggiorno PenaResidua
			// altrimenti Inserisco
			IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			lPenResMod = lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
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
				lPenResMod.setEveIdEvento(aEvento.getIdEvento());
				lPenResMod.setDataInserimento(DateUtils.getSysDate());
				PenaResiduaDAO lPenDao = new PenaResiduaDAO(lConn);
				lPenDao.setDAOFromModel(lPenResMod);
				lPenDao.insert();
			}

			if (aEvento.getCodMotivo().equals("1024"))
				lStatoProcMod.setCodStatoProcedimento("0509");
			else if (lCodPosizione.equals("70"))
				lStatoProcMod.setCodStatoProcedimento("0551");
			else if (lCodPosizione.equals("71"))
				lStatoProcMod.setCodStatoProcedimento("0549");
			else if (lCodPosizione.equals("72"))
				lStatoProcMod.setCodStatoProcedimento("0550");
			else if (lCodPosizione.equals("02"))
				lStatoProcMod.setCodStatoProcedimento("0508");
			else
				lStatoProcMod.setCodStatoProcedimento("0507");

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();
			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// NOME PROVVEDIMENTO
			NomeProvvedimentoModel lNomProvMod = new NomeProvvedimentoModel();
			lNomProvDao = new NomeProvvedimentoDAO(lConn);

			if (aEvento.getCodMotivo().equals("1024"))
				lNomProvMod.setCodNomeProvvedimento("NP239");
			else
				lNomProvMod.setCodNomeProvvedimento("NP238");

			lNomProvMod.setEveIdEvento(aEvento.getIdEvento());
			lNomProvDao.setDAOFromModel(lNomProvMod);
			lNomProvDao.insert();

			commit(lConn);

			// EVENTO
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			commit(lConnBlob);
		} catch (DAOException daoEx) {
			rollback(lConn);
			rollback(lConnBlob);
			daoEx.printStackTrace();
			throw new F3BException("OrdineEsecuzioneAlfanoController.ExUpdateValidaL78del2013 : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Exception: " + ex);
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException("OrdineEsecuzioneAlfanoController.ExUpdateValidaL78del2013 : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosGiuDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lNomProvDao);
			cleanup(lEveSql);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;

	}

} // Chiude la classe OrdineEsecuzioneAlfanoController