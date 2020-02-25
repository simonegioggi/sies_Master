package siap.siep.decretoordinanza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

import siap.controller.SiapController;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.dao.EventoStoreProcedurePulisciDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.CalendarUtil;

import siap.siep.SIEPException;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.decretoordinanza.dao.DecretoOrdinanzaSiepDAO;
import siap.siep.decretoordinanza.dao.DecretoOrdinanzaSiepSqlDAO;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaDAO;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.penacomplessiva.dao.PenaComplessivaSqlDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sospensione.dao.SospensioneDAO;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;

import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.model.TenoreModel;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: DecretoOrdinanzaSiepController
 * </p>
 * <p>
 * Description: Classe Controller per DecretoOrdinanzaSiep
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
public class DecretoOrdinanzaSiepController extends SiapController implements IDecretoOrdinanzaSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisce/Aggiorna l'evento e le notifiche associate
	 */
	public EventoNotificaModel ExInserisciOModificaEventoNotifica(EventoNotificaModel aEvento)
			throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
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
			if (lEvePresente == null) // Se non presente lo inserisce
			{
				// Setto l'anno e il progressivo...
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
				aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));
				lEveDao.setDAOFromModel(aEvento.getEvento());
				lKeyEvento = lEveDao.insert();
				lEveRet.getEvento().setIdEvento(lKeyEvento);
			} else // Se presente lo aggiorna
			{
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
				lEveDao.setEveIdEvento(aEvento.getEvento().getEveIdEvento());
				lEveDao.setIdEvento(lKeyEvento);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				// *********** Cancella le NOTIFICHE associate al EVENTO ******************
				lNotDao.setCondizioneEvento(lKeyEvento);
				lNotDao.delete();
				lNotDao.stop();
			}

			// ========================================================================
			// Inserisce le notifiche
			// ========================================================================
			BigDecimal lKeyAutorita = null;
			int count = 0;
			while (count < aEvento.getNotifiche().length) {
				if (aEvento.getNotifiche()[count] != null) {
					if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
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
					aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);
					lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();

				}
				count++;
			}

			// Inserimento delle eventuali note aggiuntive.
			if (aEvento.getCampoNote() != null) {
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

			rollback(lConn);
			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciOModificaEventoNotifica: "
					+ daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();

			rollback(lConn);
			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciOModificaEventoNotifica: " + ex);
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

	/**
	 * Inserisce/Aggiorna: decreto ordinanza siep evento correlato pena residua sospenzione (solo se....
	 *
	 */
	public DecretoOrdinanzaSiepModel ExInserisciDecretoOrdinanzaSiep(
			DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep, EventoModel aEvento,
			CalcoloPenaModel aCalcoloPenaMod) throws F3BException {
		Connection lConn = null;

		DecretoOrdinanzaSiepDAO lDecDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		// PenaResiduaDAO lPenDao = null;
		PenaResiduaSqlDAO lPenSqlDao = null;
		SospensioneDAO lSosDao = null;
		PenaComplessivaSqlDAO lPenCompSqlDao = null;

		DecretoOrdinanzaSiepModel lDecMod = null;

		try {
			lConn = getDBTransaction();

			// INSERIMENTO O AGGIORNAMENTO DECRETO_ORDINANZA_SIEP
			lDecMod = new DecretoOrdinanzaSiepModel(aDecretoOrdinanzaSiep);

			lDecDao = new DecretoOrdinanzaSiepDAO(lConn);

			// cerca l'eventuale record da aggiornare
			DecretoOrdinanzaSiepModel lDecModPresente = null;
			if (lDecMod.getIdDecretoOrdinanzaSiep() != null) {
				lDecDao.setCondizioneByIdDecretoOrdinanzaFlagNonElaborato(lDecMod.getIdDecretoOrdinanzaSiep());
				lDecModPresente = (DecretoOrdinanzaSiepModel) lDecDao.getModelByKey();
			}

			if (lDecModPresente == null) {
				lDecDao.setDAOFromModel(lDecMod);
				BigDecimal lKeyDecOrd = null;
				lKeyDecOrd = lDecDao.insert();

				lDecMod.setIdDecretoOrdinanzaSiep(lKeyDecOrd);
			} else {
				lDecDao.setDAOFromModelForUpdateSospensione(lDecMod); // !!Problema delle date aggiornamento!!
				// lDecDao.selByKey();
				lDecDao.update();

				lDecMod.setIdEventoGenerato(lDecModPresente.getIdEventoGenerato());
			}

			lDecDao.stop();

			// ========================================
			// INSERIMENTO O AGGIORNAMENTO EVENTO
			// ========================================
			lEveDao = new EventoDAO(lConn);
			lEveSqlDAO = new EventoSqlDAO(lConn);

			EventoModel lEveMod = null;
			if (lDecMod.getIdEventoGenerato() != null) {
				lEveSqlDAO.ricercaEventoNonRegistratoByKey(lDecMod.getIdEventoGenerato());
				lEveMod = (EventoModel) lEveSqlDAO.getModelByKey();
			}

			BigDecimal lKeyEvento = null;
			// Se non presente oppure presente non validato
			if (lEveMod == null) {
				// Setto l'anno e il progressivo...
				BigDecimal lProgr = lEveSqlDAO.getProgressivo(aEvento);
				aEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				// Setto ID_DECRETO_ORDINANZA_SIEP
				aEvento.setDecIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());

				lEveDao.setDAOFromModel(aEvento);
				lKeyEvento = lEveDao.insert();
				aEvento.setIdEvento(lKeyEvento);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ###### Inserito evento id = " + lKeyEvento);
			} else // Se presente lo aggiorna
			{
				lKeyEvento = lEveMod.getIdEvento();

				lEveDao.setIdEvento(lKeyEvento);

				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				// lEveDao.setFlagDocumentoRegistrato("N");

				lEveDao.setCodTipoEvento(aEvento.getCodTipoEvento());
				lEveDao.setCodTipoProvvedimento(aEvento.getCodTipoProvvedimento());
				lEveDao.setCodMotivo(aEvento.getCodMotivo());
				lEveDao.setCodUfficioEmittente(aEvento.getCodUfficioEmittente());
				lEveDao.setCodLuogoEmittente(aEvento.getCodLuogoEmittente());
				lEveDao.setDataEmissione(aEvento.getDataEmissione());
				lEveDao.setDataTrasmissioneAtti(aEvento.getDataTrasmissioneAtti());
				lEveDao.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
				lEveDao.setFlagVideoSiep(aEvento.getFlagVideoSiep());
				lEveDao.setFlagStampaSiep(aEvento.getFlagStampaSiep());
				lEveDao.setDecIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());

				// !!Problema delle date aggiornamento!!
				lEveDao.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lEveDao.setDataInserimento(aEvento.getDataInserimento());
				lEveDao.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
				lEveDao.setCodOperatoreAggiornamento(null);
				lEveDao.setDataAggiornamento(null);
				lEveDao.setCodUfficioAggiornamento(null);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ###### Modificato evento id = " + lKeyEvento);

				/*
				 * //*********** Cancella le NOTIFICHE associate al EVENTO ******************
				 * lNotDao.setCondizioneEvento(lKeyEvento); lNotDao.delete(); lNotDao.stop();
				 */
			}

			// ==============================================================
			// AGGIORNAMENTO DECRETO_ORDINANZA_SIEP CON ID_EVENTO generato
			// ==============================================================
			lDecDao.setIdEventoGenerato(lKeyEvento);
			lDecDao.setIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep()); // Controllare che non sia
																					// null
			lDecDao.selByKey();
			lDecDao.update();
			lDecDao.stop();

			/*
			 * //-- cancella pena residua non validata e relative sospensioni lPenDao = new
			 * PenaResiduaDAO(lConn);
			 * lPenDao.setCondizioneUpdateNonValidato(lDecMod.getFasSieIdFascicoloSiep()); List
			 * lListPosNonValidate = new ArrayList( lPenDao.getModels() ); lSosDao = new
			 * SospensioneDAO(lConn); for (Iterator i = lListPosNonValidate.iterator(); i.hasNext(); ) {
			 * PenaResiduaModel lPenaResidua = (PenaResiduaModel)i.next();
			 * lSosDao.setCondizioneIdPenaResidua(lPenaResidua.getIdPenaResidua()); lSosDao.delete();
			 * lSosDao.stop(); lPenDao.setCondizioneUpdate(lPenaResidua.getIdPenaResidua()); lPenDao.delete();
			 * lPenDao.stop(); }
			 */

			// ==============================
			// CALCOLO PENA RESIDUA
			// ==============================
			// Cerca la Posizione Giurica
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);

			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lDecMod.getFasSieIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			if (lPosMod == null || lPosMod.getCodPosizioneGiuridica() == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

			// Cerca la Pena Complessiva
			lPenCompSqlDao = new PenaComplessivaSqlDAO(lConn);

			lPenCompSqlDao.ricercaPenaComplessivaByIdFascicolo(lDecMod.getFasSieIdFascicoloSiep());
			PenaComplessivaModel lPenMod = (PenaComplessivaModel) lPenCompSqlDao.getModelByKey();

			if (lPenMod == null || lPenMod.getCodTipoPenaDetentiva() == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Inserire prima la Pena Complessiva. Impossibile eseguire la richiesta.");

			String lFlagErgastolo = "N";
			// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
			if (lPenMod.getCodTipoPenaDetentiva() != null
					&& lPenMod.getCodTipoPenaDetentiva() != ""
					&& (lPenMod.getCodTipoPenaDetentiva().equals("03") || lPenMod.getCodTipoPenaDetentiva()
							.equals("04"))) {
				lFlagErgastolo = "S";
			}

			// INSERIMENTO PENA RESIDUA
			lPenSqlDao = new PenaResiduaSqlDAO(lConn);

			// Cerca l'ultima pena residua validata...
			lPenSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(lDecMod
					.getFasSieIdFascicoloSiep());
			PenaResiduaModel lUltimaPenaResidua = (PenaResiduaModel) lPenSqlDao.getModelByKey();
			// ...se non la trova cerca l'ultima in assoluto
			if (lUltimaPenaResidua == null) {
				lPenSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lDecMod.getFasSieIdFascicoloSiep());
				lUltimaPenaResidua = (PenaResiduaModel) lPenSqlDao.getModelByKey();
			}

			lPenSqlDao.stop();

			if (lUltimaPenaResidua == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Pena Residua non trovata, impossibile procedere.");

			// Non serve a nulla
			// lPenSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidataSospesa(lDecMod.getFasSieIdFascicoloSiep());
			// PenaResiduaModel lPenaResiduaValidataSospesa = (PenaResiduaModel) lPenSqlDao.getModelByKey();

			PenaResiduaModel lPenaResiduaNuova = new PenaResiduaModel();
			CalendarModel lPenaEspiataSosp = new CalendarModel();

			if (lFlagErgastolo.equals("N")) {
				// Pena Ricalcolata sul
				PenaResiduaModel lPenaResiduaIniziale = aCalcoloPenaMod.getPenaDaEspiare(
						lUltimaPenaResidua.getDataInizio(), null, "all");
				aCalcoloPenaMod.calcolaPenaDaSospensione(lPenaResiduaIniziale,
						lDecMod.getDataSospensioneEsecuzione());
				lPenaResiduaNuova = aCalcoloPenaMod.getPenaResiduaRicalcolata();
				lPenaEspiataSosp = aCalcoloPenaMod.getPenaEspiata();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lPenaResiduaNuova : " + lPenaResiduaNuova);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lPenaEspiataSosp : " + lPenaEspiataSosp);

				// Vengono settati quei parametri
				// che non vengono gestiti nel CalcoloPenaModel
				// lPenaResiduaNuova.setIdPenaResidua( lUltimaPenaResidua.getIdPenaResidua() );
				lPenaResiduaNuova.setFasSieIdFascicoloSiep(lDecMod.getFasSieIdFascicoloSiep());
				lPenaResiduaNuova.setDiesAQuo(lUltimaPenaResidua.getDiesAQuo());
				lPenaResiduaNuova.setFlagErgastolo(lUltimaPenaResidua.getFlagErgastolo());
				lPenaResiduaNuova.setFlagValidato("N");
				lPenaResiduaNuova.setFlagPenaSospesa("S");
				lPenaResiduaNuova.setEveIdEvento(lKeyEvento);
			} else {
				// Se Ergastolo non ricalcolo la pena, ma copio i dati dell'ultimo record
				lPenaResiduaNuova = lUltimaPenaResidua;

				lPenaResiduaNuova.setMisAltIdMisuraAlternativa(null);
				lPenaResiduaNuova.setEveIdEvento(lKeyEvento);
				lPenaResiduaNuova.setFlagPenaSospesa("S");
				lPenaResiduaNuova.setFlagValidato("N");
			}

			// Gestione della Data Inserimento
			lPenaResiduaNuova.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
			lPenaResiduaNuova.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
			lPenaResiduaNuova.setDataInserimento(aEvento.getDataInserimento());
			lPenaResiduaNuova.setCodOperatoreAggiornamento(null);
			lPenaResiduaNuova.setDataAggiornamento(null);
			lPenaResiduaNuova.setCodUfficioAggiornamento(null);

			if (lFlagErgastolo.equals("S")) {
				lPenaResiduaNuova.setDataFine(DateUtils.getDate(9999, 12, 31));
				if (lPenMod.getCodTipoPenaDetentiva().equals("03")) {
					lPenaResiduaNuova.setFlagErgastolo("S");
				} else if (lPenMod.getCodTipoPenaDetentiva().equals("04")) {
					lPenaResiduaNuova.setFlagErgastolo("D");
				}
			}

			// lPenDao = new PenaResiduaDAO(lConn);
			// lPenDao.setDAOFromModel(lPenaResiduaNuova);
			// BigDecimal lKeyPena = lPenDao.insert();

			lPenaResiduaNuova = lPenSqlDao.inserisciOModificaPenaResidua(lPenaResiduaNuova);
			BigDecimal lKeyPena = lPenaResiduaNuova.getIdPenaResidua();

			// ============================
			// INSERIMENTO SOSPENSIONE
			// ============================
			if (!lPosMod.isLibero() && lUltimaPenaResidua.getDataInizio() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Prepara SopensioneModel");

				lSosDao = new SospensioneDAO(lConn);

				SospensioneModel lSospensione = new SospensioneModel();
				lSospensione.setDataInizio(lDecMod.getDataSospensioneEsecuzione());

				if (lFlagErgastolo.equals("N")) {
					lSospensione.setNumAnniPenaResiduaReclus(lPenaResiduaNuova.getNumAnniReclusione());
					lSospensione.setNumMesiPenaResiduaReclus(lPenaResiduaNuova.getNumMesiReclusione());
					lSospensione.setNumGiorniPenaResiduaReclus(lPenaResiduaNuova.getNumGiorniReclusione());
					lSospensione.setNumAnniPenaResiduaArres(lPenaResiduaNuova.getNumAnniArresto());
					lSospensione.setNumMesiPenaResiduaArres(lPenaResiduaNuova.getNumMesiArresto());
					lSospensione.setNumGiorniPenaResiduaArres(lPenaResiduaNuova.getNumGiorniArresto());

					lSospensione.setAmmendaResidua(lPenaResiduaNuova.getImportoAmmenda());
					lSospensione.setMultaResidua(lPenaResiduaNuova.getImportoMulta());

					lSospensione.setNumAnniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumAnni()));
					lSospensione.setNumMesiPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumMesi()));
					lSospensione.setNumGiorniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumGiorni()));
				} else {
					// Calcolo la pena espiata come intervallo tra la data inizio e la data
					// di sospensione (considerato come giorno espiato)
					CalendarModel lCalPenaEspiataCalcoloErg = new CalendarModel();
					CalendarUtil lCalUtil = new CalendarUtil();

					lCalPenaEspiataCalcoloErg.setDataInizio(lUltimaPenaResidua.getDataInizio());
					lCalPenaEspiataCalcoloErg.setDataFine(lDecMod.getDataSospensioneEsecuzione());

					lCalPenaEspiataCalcoloErg = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiataCalcoloErg);
					lCalPenaEspiataCalcoloErg = lCalUtil.ricalcolaGAM(lCalPenaEspiataCalcoloErg);

					lSospensione
							.setNumAnniPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumAnni()));
					lSospensione
							.setNumMesiPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumMesi()));
					lSospensione.setNumGiorniPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg
							.getNumGiorni()));
				}

				lSospensione.setPenResIdPenaResidua(lKeyPena);
				lSospensione.setFasSieIdFascicoloSiep(aDecretoOrdinanzaSiep.getFasSieIdFascicoloSiep());
				lSospensione.setNumGiorniLibanticipata(new BigDecimal(aCalcoloPenaMod
						.getLiberazioneAnticipata()));

				lSospensione.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lSospensione.setDataInserimento(aEvento.getDataInserimento());
				lSospensione.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());

				lSosDao.setDAOFromModel(lSospensione);
				lSosDao.insert();
				lSosDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);

			ex.printStackTrace();

			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciDecretoOrdinanzaSiep: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);

			sqe.printStackTrace();

			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciDecretoOrdinanzaSiep: " + sqe);
		} catch (SIEPException siepEx) {
			rollback(lConn);

			siepEx.printStackTrace();

			throw siepEx;
		} catch (Exception ex) {
			rollback(lConn);

			ex.printStackTrace();

			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciDecretoOrdinanzaSiep: " + ex);
		} finally {
			cleanup(lDecDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lPosSqlDao);
			// cleanup(lPenDao);
			cleanup(lSosDao);
			cleanup(lPenSqlDao);
			cleanup(lPenCompSqlDao);

			cleanup(lConn);
		}

		return lDecMod;
	}

	/**
	 * 
	 * @param aDecOrdSiepMod
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	public String ExInserisciDecretoOrdinanzaWithoutSequence(DecretoOrdinanzaSiepModel aDecOrdSiepMod,
			Connection lConn) throws F3BException {
		String lCodEsito = "00000";

		DecretoOrdinanzaSiepDAO lDecOrdDAO = null;

		try {
			lDecOrdDAO = new DecretoOrdinanzaSiepDAO(lConn);

			// Inserisco DecretoOrdinanzaSiep
			lDecOrdDAO.setDAOFromModel(aDecOrdSiepMod);
			lDecOrdDAO.setWithoutSequence(true);
			lDecOrdDAO.insert();
			lDecOrdDAO.stop();

			siesLogger.debug("=========> DECRETO_ORDINANZA_SIEP_SCRITTO scritto ----->");
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				siesLogger.warn("DecretoOrdinanzaSIEP gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				siesLogger.error("Errore in fase di inserimento DecretoOrdinanzaSiep con id:"
						+ aDecOrdSiepMod.getIdDecretoOrdinanzaSiep(), ex);
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire Record DecretoOrdinanzaSiep! ");
			}
		} finally {
			cleanup(lDecOrdDAO);
		}

		return lCodEsito;
	}

	/**
   *
   */
	public DecretoOrdinanzaSiepModel ExInserisciDecretoOrdinanzaSiepRipristino(
			DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep, EventoModel aEvento,
			boolean aInterruzioneNonValida) throws F3BException {
		Connection lConn = null;

		PenaResiduaDAO lPenDao = null;
		PenaResiduaSqlDAO lPenSqlDao = null;
		SospensioneDAO lSosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		DecretoOrdinanzaSiepDAO lDecDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		SospensioneDAO lSospDao = null;
		PenaComplessivaSqlDAO lPenCompSqlDao = null;

		EventoStoreProcedurePulisciDAO lEveProcDao = null;

		DecretoOrdinanzaSiepModel lDecMod = null;

		/*
		 * Il metodo esegue due operazioni diverse, a seconda del Flag aInterruzioneValida: - Data
		 * interruzione non valida - Data interruzione valida Selezionato dall'utente sulla maschera
		 */

		try {
			lConn = getDBTransaction();

			lPenSqlDao = new PenaResiduaSqlDAO(lConn);
			lSosDao = new SospensioneDAO(lConn);
			lPenDao = new PenaResiduaDAO(lConn);

			// CERCA L'ULTIMO EVENTO DI TIPO
			EventoModel lEveModPartenza = new EventoModel();

			lEveModPartenza.setCodTipoEvento("01");

			// lEveModPartenza.setCodTipoProvvedimento("04");
			// Cambiato il codice Tipo Provvedimento.
			// Comunque si mantiene la compatibilità con i vecchi codici. Luigi 11-10-2005
			String[] lTipoProv = { "04", "12", "04", "25" };
			String[] lMotivi = { "0267", "0267", "0270", "0270" };
			lEveModPartenza.setFasSieIdFascicoloSiep(aDecretoOrdinanzaSiep.getFasSieIdFascicoloSiep());

			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoTipoProvTipoMot(lEveModPartenza, lTipoProv, lMotivi);
			// lEveSqlDAO.ricercaEventoPerMotivo(lMotivi, lEveModPartenza);
			lEveModPartenza = (EventoModel) lEveSqlDAO.getModelByKey();

			if (lEveModPartenza == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"La pena non è interrotta. Impossibile eseguire la richiesta.");

			// ===========================
			// CERCA LA PENA RESIDUA
			// ===========================
			lPenSqlDao.ricercaPenaResiduaByKeyEvento(lEveModPartenza.getIdEvento());
			PenaResiduaModel lPenaResiduaValidata = (PenaResiduaModel) lPenSqlDao.getModelByKey();

			if (lPenaResiduaValidata == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Inserire prima la Pena Residua e validarla. Impossibile eseguire la richiesta.");

			BigDecimal lIdPenaResidua = lPenaResiduaValidata.getIdPenaResidua();

			// ========================================================================
			//
			// ========================================================================
			if (aInterruzioneNonValida) {
				/**
				 * L'interruzione precedentemente registrata va considerata nulla (un errore di
				 * comunicazione). La pena residua va ripristinata ai valori che aveva prima dell'inserimento
				 * dell'interruzione. E' necessario: - eliminare l'evento di interruzione - eliminare la pena
				 * residua e la sospensione collegate all'evento - ripristinare la posizione giuridica -
				 * recuperare la pena residua presente prima dell'interruzione
				 */
				if (lEveModPartenza.getCodMotivo() != null && lEveModPartenza.getCodMotivo().equals("0267")) { // 0267
																												// =
																												// Interruzione
																												// per
																												// avvenuta
																												// Evasione
																												// CERCA
																												// POSIZIONE
																												// GIURIDICA
																												// LEGATA
																												// ALL'EVENTO
					lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);

					lPosSqlDao.ricercaPosizioneGiuridicaByIdEvento(lEveModPartenza.getIdEvento());
					PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

					if (lPosMod == null || lPosMod.getCodPosizioneGiuridica() == null)
						throw new SIEPException(SIEPException.USER_MESSAGE,
								"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

					// CANCELLA POSIZIONE GIURIDICA
					lPosDao = new PosizioneGiuridicaDAO(lConn);
					lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());
					lPosDao.delete();
					lPosDao.stop();

					// CERCA POSIZIONE GIURIDICA PRECEDENTE
					lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aDecretoOrdinanzaSiep
							.getFasSieIdFascicoloSiep());
					lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

					if (lPosMod == null || lPosMod.getCodPosizioneGiuridica() == null)
						throw new SIEPException(SIEPException.USER_MESSAGE,
								"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

					// RIPRISTINA POSIZIONE GIURIDICA PRECEDENTE
					lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());
					lPosDao.setDataFine(null);
					lPosDao.update();
					lPosDao.stop();
				}

				// DELETE EVENTO CON STORE PROCEDURE ( PENA_RESIDUA, SOSPENSIONE )
				lEveProcDao = new EventoStoreProcedurePulisciDAO(lConn);
				lEveProcDao.setIdEvento(lEveModPartenza.getIdEvento());
				lEveProcDao.execute();

				// CERCA LA PENA RESIDUA precedente all'inserimento della sospensione
				lPenSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(aDecretoOrdinanzaSiep
						.getFasSieIdFascicoloSiep());
				lPenaResiduaValidata = (PenaResiduaModel) lPenSqlDao.getModelByKey();

				if (lPenaResiduaValidata == null)
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"Inserire prima la Pena Residua e validarla. Impossibile eseguire la richiesta.");

				lIdPenaResidua = lPenaResiduaValidata.getIdPenaResidua();
			} else {
				/**
				 * Interruzione valida, devo recuperare la sospensione per calcolare la pena residua da
				 * espiare (registrata sul record SOSPENSIONE)
				 */
				// CERCA SOSPENSIONE
				lSosDao.setCondizioneIdPenaResidua(lPenaResiduaValidata.getIdPenaResidua());
				SospensioneModel lSospMod = (SospensioneModel) lSosDao.getModelByKey();
				lSosDao.stop();

				if (lSospMod == null)
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"Dati di sospensione non trovati. Impossibile eseguire la richiesta.");

				// CALCOLO QUANTUM DI INTERRUZIONE (AAMMGG)
				CalendarModel lCalInterruzione = new CalendarModel();
				CalendarUtil lCalUtil = new CalendarUtil();

				lCalInterruzione.setDataInizio(lSospMod.getDataInizio());
				lCalInterruzione.setDataFine(aDecretoOrdinanzaSiep.getDataFineInterruzione());

				lCalInterruzione = lCalUtil.CalcolaNumGiorniMesiAnni(lCalInterruzione, false);
				lCalInterruzione = lCalUtil.ricalcolaGAM(lCalInterruzione);

				// *************************************************************
				// CALCOLO NUOVA PENA RESIDUA
				ICalcoloPena lCalPenCtrl = SIEPLookupRemote.getCalcoloPenaRemote();

				PenaResiduaModel lPenaResSosp = new PenaResiduaModel();
				lPenaResSosp.setNumAnniReclusione(lSospMod.getNumAnniPenaResiduaReclus());
				lPenaResSosp.setNumMesiReclusione(lSospMod.getNumMesiPenaResiduaReclus());
				lPenaResSosp.setNumGiorniReclusione(lSospMod.getNumGiorniPenaResiduaReclus());
				lPenaResSosp.setNumAnniArresto(lSospMod.getNumAnniPenaResiduaArres());
				lPenaResSosp.setNumMesiArresto(lSospMod.getNumMesiPenaResiduaArres());
				lPenaResSosp.setNumGiorniArresto(lSospMod.getNumGiorniPenaResiduaArres());

				// Cerca la Pena Complessiva
				lPenCompSqlDao = new PenaComplessivaSqlDAO(lConn);

				lPenCompSqlDao.ricercaPenaComplessivaByIdFascicolo(aDecretoOrdinanzaSiep
						.getFasSieIdFascicoloSiep());
				PenaComplessivaModel lPenMod = (PenaComplessivaModel) lPenCompSqlDao.getModelByKey();

				if (lPenMod == null || lPenMod.getCodTipoPenaDetentiva() == null)
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"Inserire prima la Pena Complessiva. Impossibile eseguire la richiesta.");

				String lFlagErgastolo = "N";
				// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
				if (lPenMod.getCodTipoPenaDetentiva() != null
						&& lPenMod.getCodTipoPenaDetentiva() != ""
						&& (lPenMod.getCodTipoPenaDetentiva().equals("03") || lPenMod
								.getCodTipoPenaDetentiva().equals("04"))) {
					lFlagErgastolo = "S";
				}

				// Ricalcolo le data della pena residua utilizzando come data inizio
				// data fine interruzione
				Date lDataFinePena = null;
				Date lDataFineReclusione = null;
				Date lDataInizioArresto = null;
				if (lFlagErgastolo.equals("N")) {
					Vector lDateFine = lCalPenCtrl.exCalcolaDataFinePena(
							aDecretoOrdinanzaSiep.getDataFineInterruzione(), lPenaResSosp, true);
					if (lDateFine == null || lDateFine.isEmpty())
						throw new SIEPException(SIEPException.USER_MESSAGE,
								"Errore nel Calcolo della pena. Impossibile eseguire la richiesta.");

					lDataFinePena = (Date) lDateFine.get(0);
					if (lDateFine.size() == 2) {
						lDataFineReclusione = (Date) lDateFine.get(0);
						lDataInizioArresto = DateUtils.getDayAfter(lDataFineReclusione);
						lDataFinePena = (Date) lDateFine.get(1);
					}
					if (lDateFine.size() == 1) {
						lDataFinePena = (Date) lDateFine.get(0);
					}
				} else {
					lDataFinePena = DateUtils.getDate(9999, 12, 31);
				}

				// INSERIMENTO PENA RESIDUA
				PenaResiduaModel lPenaResiduaNuova = new PenaResiduaModel();

				lPenaResiduaNuova.setDataInizio(aDecretoOrdinanzaSiep.getDataFineInterruzione());
				lPenaResiduaNuova.setDataFine(lDataFinePena);
				lPenaResiduaNuova.setDataFineReclusione(lDataFineReclusione);
				lPenaResiduaNuova.setDataInizioArresto(lDataInizioArresto);
				// lPenaResiduaNuova.setDataFinePresunta(new Date());

				lPenaResiduaNuova.setNumAnniReclusione(lSospMod.getNumAnniPenaResiduaReclus());
				lPenaResiduaNuova.setNumMesiReclusione(lSospMod.getNumMesiPenaResiduaReclus());
				lPenaResiduaNuova.setNumGiorniReclusione(lSospMod.getNumGiorniPenaResiduaReclus());
				lPenaResiduaNuova.setNumAnniArresto(lSospMod.getNumAnniPenaResiduaArres());
				lPenaResiduaNuova.setNumMesiArresto(lSospMod.getNumMesiPenaResiduaArres());
				lPenaResiduaNuova.setNumGiorniArresto(lSospMod.getNumGiorniPenaResiduaArres());

				lPenaResiduaNuova.setDiesAQuo("N");
				lPenaResiduaNuova.setFasSieIdFascicoloSiep(aDecretoOrdinanzaSiep.getFasSieIdFascicoloSiep());
				if (lPenMod.getCodTipoPenaDetentiva().equals("03")) {
					lPenaResiduaNuova.setFlagErgastolo("S");
				} else if (lPenMod.getCodTipoPenaDetentiva().equals("04")) {
					lPenaResiduaNuova.setFlagErgastolo("D");
				} else {
					lPenaResiduaNuova.setFlagErgastolo("N");
				}
				lPenaResiduaNuova.setFlagValidato("N");
				// lPenaResiduaNuova.setFlagPenaSospesa("I");
				lPenaResiduaNuova.setImportoAmmenda(new BigDecimal(0));
				lPenaResiduaNuova.setImportoMulta(new BigDecimal(0));
				/**
				 * TODO perchè vengono azzerati importo e ammenda?
				 */
				lPenaResiduaNuova.setDataInserimento(aDecretoOrdinanzaSiep.getDataInserimento());
				lPenaResiduaNuova.setCodOperatoreInserimento(aDecretoOrdinanzaSiep
						.getCodOperatoreInserimento());
				lPenaResiduaNuova.setCodUfficioInserimento(aDecretoOrdinanzaSiep.getCodUfficioInserimento());

				lPenDao.setDAOFromModel(lPenaResiduaNuova);
				BigDecimal lKeyPena = lPenDao.insert();
				lPenDao.stop();

				lPenaResiduaNuova.setIdPenaResidua(lKeyPena);

				lIdPenaResidua = lKeyPena;

				// ======================================================================
				// INSERIMENTO FINE SOSPENSIONE
				// ======================================================================
				lSosDao = new SospensioneDAO(lConn);

				SospensioneModel lSospensione = new SospensioneModel();

				// lSospensione.setDataInizio( new Date() ); // ???
				lSospensione.setDataFine(aDecretoOrdinanzaSiep.getDataFineInterruzione());

				lSospensione.setNumAnniInterruzione(new BigDecimal(lCalInterruzione.getNumAnni()));
				lSospensione.setNumMesiInterruzione(new BigDecimal(lCalInterruzione.getNumMesi()));
				lSospensione.setNumGiorniInterruzione(new BigDecimal(lCalInterruzione.getNumGiorni()));

				lSospensione.setNumAnniPenaResiduaReclus(lPenaResiduaNuova.getNumAnniReclusione());
				lSospensione.setNumMesiPenaResiduaReclus(lPenaResiduaNuova.getNumMesiReclusione());
				lSospensione.setNumGiorniPenaResiduaReclus(lPenaResiduaNuova.getNumGiorniReclusione());
				lSospensione.setNumAnniPenaResiduaArres(lPenaResiduaNuova.getNumAnniArresto());
				lSospensione.setNumMesiPenaResiduaArres(lPenaResiduaNuova.getNumMesiArresto());
				lSospensione.setNumGiorniPenaResiduaArres(lPenaResiduaNuova.getNumGiorniArresto());

				lSospensione.setAmmendaResidua(lPenaResiduaNuova.getImportoAmmenda());
				lSospensione.setMultaResidua(lPenaResiduaNuova.getImportoMulta());

				lSospensione.setPenResIdPenaResidua(lKeyPena);
				lSospensione.setFasSieIdFascicoloSiep(aDecretoOrdinanzaSiep.getFasSieIdFascicoloSiep());
				// lSospensione.setFlagInterruzione("S");

				lSospensione.setCodOperatoreInserimento(aDecretoOrdinanzaSiep.getCodOperatoreInserimento());
				lSospensione.setDataInserimento(aDecretoOrdinanzaSiep.getDataInserimento());
				lSospensione.setCodUfficioInserimento(aDecretoOrdinanzaSiep.getCodUfficioInserimento());

				lSosDao.setDAOFromModel(lSospensione);
				lSosDao.insert();
				lSosDao.stop();
			}

			/******************************************************************************/

			// INSERIMENTO O AGGIORNAMENTO DECRETO_ORDINANZA_SIEP
			lDecMod = new DecretoOrdinanzaSiepModel(aDecretoOrdinanzaSiep);

			lDecDao = new DecretoOrdinanzaSiepDAO(lConn);

			// cerca l'eventuale record da aggiornare
			lDecDao.setCondizioneByIdFascicoloSiepFlagNonElaborato(aDecretoOrdinanzaSiep
					.getFasSieIdFascicoloSiep());
			DecretoOrdinanzaSiepModel lDecModPresente = (DecretoOrdinanzaSiepModel) lDecDao.getModelByKey();

			if (lDecModPresente == null) {
				lDecDao.setDAOFromModel(lDecMod);

				BigDecimal lKeyDecOrd = null;
				lKeyDecOrd = lDecDao.insert();

				lDecMod.setIdDecretoOrdinanzaSiep(lKeyDecOrd);
			} else {
				lDecMod.setCodOperatoreAggiornamento(aDecretoOrdinanzaSiep.getCodOperatoreInserimento());
				lDecMod.setCodUfficioAggiornamento(aDecretoOrdinanzaSiep.getCodUfficioInserimento());
				lDecMod.setDataAggiornamento(aDecretoOrdinanzaSiep.getDataInserimento());

				lDecDao.setDAOFromModelForUpdate(lDecMod);
				lDecDao.update();

				lDecMod.setIdDecretoOrdinanzaSiep(lDecModPresente.getIdDecretoOrdinanzaSiep());
			}

			lDecDao.stop();

			// INSERIMENTO O AGGIORNAMENTO EVENTO
			lEveDao = new EventoDAO(lConn);
			// lEveSqlDAO = new EventoSqlDAO(lConn);

			EventoModel lEveRic = new EventoModel();
			lEveRic.setCodTipoEvento("01");
			lEveRic.setCodTipoProvvedimento("04");
			lEveRic.setCodMotivo("0272");
			lEveRic.setFasSieIdFascicoloSiep(aDecretoOrdinanzaSiep.getFasSieIdFascicoloSiep());

			lEveSqlDAO.ricercaEventoTipoEveTipoProvMot(lEveRic, "N");
			EventoModel lEveMod = (EventoModel) lEveSqlDAO.getModelByKey();

			BigDecimal lKeyEvento = null;
			// Se non presente oppure presente non validato
			if (lEveMod == null) {
				// Setto l'anno e il progressivo...
				BigDecimal lProgr = lEveSqlDAO.getProgressivo(aEvento);
				aEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				// Setto ID_DECRETO_ORDINANZA_SIEP
				aEvento.setDecIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());

				lEveDao.setDAOFromModel(aEvento);
				lKeyEvento = lEveDao.insert();
				aEvento.setIdEvento(lKeyEvento);

			} else // Se presente lo aggiorna
			{
				lKeyEvento = lEveMod.getIdEvento();

				lEveDao.setIdEvento(lKeyEvento);

				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				// lEveDao.setFlagDocumentoRegistrato("N");

				lEveDao.setCodTipoEvento(aEvento.getCodTipoEvento());
				lEveDao.setCodTipoProvvedimento(aEvento.getCodTipoProvvedimento());
				lEveDao.setCodMotivo(aEvento.getCodMotivo());
				lEveDao.setCodUfficioEmittente(aEvento.getCodUfficioEmittente());
				lEveDao.setCodLuogoEmittente(aEvento.getCodLuogoEmittente());
				lEveDao.setDataEmissione(aEvento.getDataEmissione());
				lEveDao.setDataTrasmissioneAtti(aEvento.getDataTrasmissioneAtti());
				lEveDao.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
				lEveDao.setFlagVideoSiep(aEvento.getFlagVideoSiep());
				lEveDao.setFlagStampaSiep(aEvento.getFlagStampaSiep());
				lEveDao.setDecIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());

				// !!Problema delle date aggiornamento!!
				lEveDao.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lEveDao.setDataInserimento(aEvento.getDataInserimento());
				lEveDao.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
				lEveDao.setCodOperatoreAggiornamento(null);
				lEveDao.setDataAggiornamento(null);
				lEveDao.setCodUfficioAggiornamento(null);

				lEveDao.selCondizioneUpdate(lKeyEvento);
				lEveDao.update();
				lEveDao.stop();

				/*
				 * //*********** Cancella le NOTIFICHE associate al EVENTO ******************
				 * lNotDao.setCondizioneEvento(lKeyEvento); lNotDao.delete(); lNotDao.stop();
				 */
			}
			/******************************************************************************/

			// AGGIORNAMENTO DECRETO_ORDINANZA_SIEP CON ID_EVENTO generato
			lDecDao.setIdEventoGenerato(lKeyEvento);
			lDecDao.setCondizioneUpdate(lDecMod.getIdDecretoOrdinanzaSiep());
			lDecDao.update();
			lDecDao.stop();

			// AGGIORNAMENTO PENA RESIDUA
			lPenDao.setCondizioneUpdate(lIdPenaResidua);
			lPenDao.setEveIdEvento(lKeyEvento);
			lPenDao.update();
			lPenDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);

			ex.printStackTrace();

			throw new F3BException(
					"DecretoOrdinanzaSiepController.ExInserisciDecretoOrdinanzaSiepRipristino: " + ex);
		} catch (SIEPException siepEx) {
			rollback(lConn);

			siepEx.printStackTrace();

			throw siepEx;
		} catch (Exception ex) {
			rollback(lConn);

			ex.printStackTrace();

			throw new F3BException(
					"DecretoOrdinanzaSiepController.ExInserisciDecretoOrdinanzaSiepRipristino: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lPenSqlDao);
			cleanup(lSosDao);
			cleanup(lPosSqlDao);
			cleanup(lPosDao);
			cleanup(lDecDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lSospDao);
			cleanup(lPenCompSqlDao);

			cleanup(lEveProcDao);

			cleanup(lConn);
		}

		return lDecMod;
	}

	public EventoModel ExUpdateValidaOrdineEsecuzioneRevoca(EventoModel aEvento) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;

		NomeProvvedimentoDAO lNomProvvDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		// PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;

		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;

		DecretoOrdinanzaSiepDAO lDecDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecSql = null;

		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			EventoSqlDAO lEveSqlDAO = new EventoSqlDAO(lConn);

			// ===============================================
			// Recupero l'ordine di esecuzione da validare
			// ===============================================
			lEveSqlDAO.ricercaEventoByKey(aEvento.getIdEvento());

			EventoModel lEveModel = (EventoModel) lEveSqlDAO.getModelByKey();

			// ===============================
			// Inserisce NOME_PROVVEDIMENTO
			// ===============================
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lNomProvvDAO.setCodNomeProvvedimento("NP112");

			lNomProvvDAO.setEveIdEvento(aEvento.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ===============================
			// STATO_PROCEDIMENTO
			// ===============================
			// cancellazione
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoDao.setCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
			lStatoDao.delete();

			// inserimento
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setCodStatoProcedimento("0001");
			lStatoProcMod.setData(aEvento.getDataEmissione());
			lStatoProcMod.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();

			// =============================================
			// Cerca l'ultima PENA_RESIDUA per fascicolo
			// in teoria è la pena legata all'ordinanza di revoca
			// =============================================
			PenaResiduaModel lPenResMod = new PenaResiduaModel();

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResDao.setCondizioneUpdate(lPenResMod.getIdPenaResidua());
			lPenResDao.setFlagValidato("S");
			lPenResDao.setFlagPenaSospesa("S");
			lPenResDao.update();
			lPenResDao.stop();

			// ========================================================================
			// Insert nuova Pena copia di quella precedente ma agganciata all'ordine
			// di Esecuzione
			// ========================================================================
			lPenResDao.setFlagPenaSospesa("N");
			lPenResDao.setFlagValidato("S");
			lPenResDao.setEveIdEvento(aEvento.getIdEvento());
			lPenResDao.insert();
			lPenResDao.stop();

			// ================================================
			// Aggiorna DECRETO_ORDINANZA_SIEP validandolo
			// ================================================
			lDecSql = new DecretoOrdinanzaSiepSqlDAO(lConn);
			lDecSql.ricercaDecretoOrdinanzaSiepByIdEvento(lEveModel.getEveIdEvento());

			DecretoOrdinanzaSiepModel lDecMod = (DecretoOrdinanzaSiepModel) lDecSql.getModelByKey();

			lDecDao = new DecretoOrdinanzaSiepDAO(lConn);

			lDecDao.setIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());

			lDecDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lDecDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lDecDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

			lDecDao.setFlagElaborato("S");
			lDecDao.selByKey();
			lDecDao.update();
			lDecDao.stop();

			// AMBROSINO 09-02-2011 - NOTIFICHE - le prendo per la data inizioscadenzario

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			Vector lNotifiche = new Vector(lNotEveDao.getModels());
			NotificaModel lNotifica = (NotificaModel) lNotifiche.get(0);

			// SCADENZARIO
			// Cerca l'ultimo scadenzario fine pena per quel fascicolo
			// assume che se ce n'è, è uno
			ParametroModel lParMod = new ParametroModel();
			lParMod.setNomeParametro("VANE RICERCHE");
			lParMod.setCodUfficioValidita(aEvento.getCodUfficioAggiornamento());

			Vector lVectPar = null;
			IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
			lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

			ScadenzarioModel lScaMod = new ScadenzarioModel();
			lScaMod.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());

			// lScaMod.setDataInizioScadenza(aEvento.getDataTrasmissioneAtti());
			lScaMod.setDataInizioScadenza(lNotifica.getDataInvio());

			Iterator lIter = lVectPar.iterator();
			Date lSommaAnni = null;
			Date lSommaMesi = null;
			Date lFineScadenza = null;
			if (lIter.hasNext()) {
				ParametroModel lParModel = (ParametroModel) lIter.next();
				lSommaAnni = DateUtils.moveDateTo(lScaMod.getDataInizioScadenza(), java.util.Calendar.YEAR,
						lParModel.getAnni().intValue());
				lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH, lParModel.getMesi()
						.intValue());
				lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH, lParModel
						.getGiorni().intValue());
			}

			// MODIFICA
			lScaMod.setCodTipoScadenzario("03");
			lScaMod.setFlagVisto("N");
			lScaMod.setDataFineScadenza(lFineScadenza);

			Vector lScadenzarii = null;
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			// cerca un scadenzario per id fascicolo e per tipo scadenzario
			lScaSqlDao.ricercaScadenzarioVerbaleArresto(lScaMod);
			lScadenzarii = new Vector(lScaSqlDao.getModels());

			if (lScaMod.getDataInizioScadenza() != null && lScaMod.getDataFineScadenza() != null) {
				lScaDao = new ScadenzarioDAO(lConn);
				if (lScadenzarii.size() == 0) {
					lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lScaMod.setDataInserimento(aEvento.getDataAggiornamento());
					// a6-rr-238 -11-2010
					lScaMod.setCodStatoNotifica("NP");
					lScaMod.setEveIdEvento(aEvento.getIdEvento());

					lScaDao.setDAOFromModel(lScaMod);
					// BigDecimal lKeyScad = null;
					/* lKeyScad = */lScaDao.insert();
				} else {
					ScadenzarioModel lScaModID = (ScadenzarioModel) lScadenzarii.get(0);
					lScaMod.setIdScadenzario(lScaModID.getIdScadenzario());
					lScaMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaMod.setDataAggiornamento(aEvento.getDataAggiornamento());
					// a6-rr-238 -11-2010
					lScaMod.setCodStatoNotifica("NP");
					lScaMod.setEveIdEvento(aEvento.getIdEvento());

					lScaDao.setDAOFromModelForUpdate(lScaMod);
					lScaDao.update();
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
			rollback(lConn);
			rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException(
					"ExUpdateValidaOrdineScarcerazione.ExUpdateValidaOrdineEsecuzioneRevoca : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException(
					"ExUpdateValidaOrdineScarcerazione.ExUpdateValidaOrdineEsecuzioneRevoca : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lStatoDao);
			// cleanup(lPosSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lDecDao);
			cleanup(lDecSql);
			cleanup(lNotEveDao);

			cleanup(lConn);

			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return aEvento;
	}

	/*****************************************************************************
	 * Inserisce o modifica i dati del descreto ordinanza siep in caso di revoca di una sospensione
	 *
	 *
	 ************************************************************************** */
	public DecretoOrdinanzaSiepModel ExInserisciRevocaDecretoOrdinanzaSiep(
			DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep, EventoModel aEvento) throws F3BException {
		Connection lConn = null;

		DecretoOrdinanzaSiepDAO lDecDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		// PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaDAO lPenDao = null;
		PenaResiduaSqlDAO lPenSqlDao = null;
		// SospensioneDAO lSosDao = null;
		// PenaComplessivaSqlDAO lPenCompSqlDao = null;

		DecretoOrdinanzaSiepModel lDecMod = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("DecretoOrdinanzaSiep -> " + aDecretoOrdinanzaSiep);
		try {
			lConn = getDBTransaction();

			// ========================================================================
			// INSERIMENTO O AGGIORNAMENTO DECRETO_ORDINANZA_SIEP
			// ========================================================================
			lDecMod = new DecretoOrdinanzaSiepModel(aDecretoOrdinanzaSiep);

			lDecDao = new DecretoOrdinanzaSiepDAO(lConn);

			// cerca l'eventuale record da aggiornare
			DecretoOrdinanzaSiepModel lDecModPresente = null;
			if (lDecMod.getIdDecretoOrdinanzaSiep() != null) {
				lDecDao.setCondizioneByIdDecretoOrdinanzaFlagNonElaborato(lDecMod.getIdDecretoOrdinanzaSiep());
				lDecModPresente = (DecretoOrdinanzaSiepModel) lDecDao.getModelByKey();
			}

			if (lDecModPresente == null) {
				lDecDao.setDAOFromModel(lDecMod);
				BigDecimal lKeyDecOrd = null;
				lKeyDecOrd = lDecDao.insert();

				lDecMod.setIdDecretoOrdinanzaSiep(lKeyDecOrd);
			} else {
				lDecDao.setDAOFromModelForUpdateSospensione(lDecMod); // !!Problema delle date aggiornamento!!
				// lDecDao.selByKey();
				lDecDao.update();

				lDecMod.setIdEventoGenerato(lDecModPresente.getIdEventoGenerato());
			}

			lDecDao.stop();

			// ========================================================================
			// INSERIMENTO O AGGIORNAMENTO EVENTO
			// ========================================================================
			lEveDao = new EventoDAO(lConn);
			lEveSqlDAO = new EventoSqlDAO(lConn);

			EventoModel lEveMod = null;
			if (lDecMod.getIdEventoGenerato() != null) {
				lEveSqlDAO.ricercaEventoNonRegistratoByKey(lDecMod.getIdEventoGenerato());
				lEveMod = (EventoModel) lEveSqlDAO.getModelByKey();
			}

			BigDecimal lKeyEvento = null;
			// Se non presente oppure presente non validato
			if (lEveMod == null) {
				// Setto l'anno e il progressivo...
				BigDecimal lProgr = lEveSqlDAO.getProgressivo(aEvento);
				aEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				// Setto ID_DECRETO_ORDINANZA_SIEP
				aEvento.setDecIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());

				lEveDao.setDAOFromModel(aEvento);
				lKeyEvento = lEveDao.insert();
				aEvento.setIdEvento(lKeyEvento);

			} else // Se presente lo aggiorna
			{
				lKeyEvento = lEveMod.getIdEvento();

				lEveDao.setIdEvento(lKeyEvento);

				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				// lEveDao.setFlagDocumentoRegistrato("N");

				lEveDao.setCodTipoEvento(aEvento.getCodTipoEvento());
				lEveDao.setCodTipoProvvedimento(aEvento.getCodTipoProvvedimento());
				lEveDao.setCodMotivo(aEvento.getCodMotivo());
				lEveDao.setCodUfficioEmittente(aEvento.getCodUfficioEmittente());
				lEveDao.setCodLuogoEmittente(aEvento.getCodLuogoEmittente());
				lEveDao.setDataEmissione(aEvento.getDataEmissione());
				lEveDao.setDataTrasmissioneAtti(aEvento.getDataTrasmissioneAtti());
				lEveDao.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
				lEveDao.setFlagVideoSiep(aEvento.getFlagVideoSiep());
				lEveDao.setFlagStampaSiep(aEvento.getFlagStampaSiep());
				lEveDao.setDecIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());

				// !!Problema delle date aggiornamento!!
				lEveDao.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lEveDao.setDataInserimento(aEvento.getDataInserimento());
				lEveDao.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
				lEveDao.setCodOperatoreAggiornamento(null);
				lEveDao.setDataAggiornamento(null);
				lEveDao.setCodUfficioAggiornamento(null);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				/*
				 * //*********** Cancella le NOTIFICHE associate al EVENTO ******************
				 * lNotDao.setCondizioneEvento(lKeyEvento); lNotDao.delete(); lNotDao.stop();
				 */
			}

			// AGGIORNAMENTO DECRETO_ORDINANZA_SIEP CON ID_EVENTO generato
			lDecDao.setIdEventoGenerato(lKeyEvento);
			lDecDao.setIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep()); // Controllare che non sia
																					// null
			lDecDao.selByKey();
			lDecDao.update();
			lDecDao.stop();

			// ========================================================================
			// CALCOLO PENA RESIDUA
			// ========================================================================
			/*
			 * //-- cancella pena residua non validata e relative sospensioni lPenDao = new
			 * PenaResiduaDAO(lConn);
			 * lPenDao.setCondizioneUpdateNonValidato(lDecMod.getFasSieIdFascicoloSiep()); List
			 * lListPosNonValidate = new ArrayList( lPenDao.getModels() ); lSosDao = new
			 * SospensioneDAO(lConn); for (Iterator i = lListPosNonValidate.iterator(); i.hasNext(); ) {
			 * PenaResiduaModel lPenaResidua = (PenaResiduaModel)i.next();
			 * lSosDao.setCondizioneIdPenaResidua(lPenaResidua.getIdPenaResidua()); lSosDao.delete();
			 * lSosDao.stop(); lPenDao.setCondizioneUpdate(lPenaResidua.getIdPenaResidua()); lPenDao.delete();
			 * lPenDao.stop(); }
			 */
			/*
			 * // Cerca la Posizione Giurica lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			 * lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lDecMod.getFasSieIdFascicoloSiep());
			 * PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel)lPosSqlDao.getModelByKey();
			 * if(lPosMod == null || lPosMod.getCodPosizioneGiuridica() == null) throw new
			 * SIEPException(SIEPException.USER_MESSAGE,
			 * "Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta."); // Cerca la Pena
			 * Complessiva lPenCompSqlDao = new PenaComplessivaSqlDAO(lConn);
			 * lPenCompSqlDao.ricercaPenaComplessivaByIdFascicolo(lDecMod.getFasSieIdFascicoloSiep());
			 * PenaComplessivaModel lPenMod = (PenaComplessivaModel) lPenCompSqlDao.getModelByKey();
			 * if(lPenMod == null || lPenMod.getCodTipoPenaDetentiva() == null) throw new
			 * SIEPException(SIEPException.USER_MESSAGE,
			 * "Inserire prima la Pena Complessiva. Impossibile eseguire la richiesta."); String
			 * lFlagErgastolo = "N"; // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
			 * diurno if( lPenMod.getCodTipoPenaDetentiva() != null && lPenMod.getCodTipoPenaDetentiva() != ""
			 * && ( lPenMod.getCodTipoPenaDetentiva().equals("03") ||
			 * lPenMod.getCodTipoPenaDetentiva().equals("04") ) ) { lFlagErgastolo = "S"; }
			 */
			// INSERIMENTO PENA RESIDUA
			// viene effettuata una copia dei dati già a sistema (pena residua sospesa
			// validata) e legata all'evento corrente. Non viene rieffettuato il calcolo,
			// la pena residua resta quella calcolata al momento della Concessione.

			lPenSqlDao = new PenaResiduaSqlDAO(lConn);
			/*
			 * lPenSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(lDecMod.getFasSieIdFascicoloSiep
			 * ()); PenaResiduaModel lPenaResiduaValidata = (PenaResiduaModel) lPenSqlDao.getModelByKey();
			 * lPenSqlDao.stop();
			 */
			lPenSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidataSospesa(lDecMod
					.getFasSieIdFascicoloSiep());
			PenaResiduaModel lPenaResiduaValidataSospesa = (PenaResiduaModel) lPenSqlDao.getModelByKey();

			if (lPenaResiduaValidataSospesa == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"La pena non è sospesa. Impossibile eseguire la richiesta.");
			// effettuo una copia della pena residua sospesa e la inserisco come nuovo
			// record con flag_sospeso = N
			PenaResiduaModel lPenaResiduaNuova = new PenaResiduaModel(lPenaResiduaValidataSospesa);

			lPenaResiduaNuova.setFlagPenaSospesa("N");
			lPenaResiduaNuova.setFlagValidato("N");
			lPenaResiduaNuova.setEveIdEvento(lKeyEvento);
			lPenaResiduaNuova.setFasSieIdFascicoloSiep(lDecMod.getFasSieIdFascicoloSiep());

			// Gestione della Data Inserimento
			lPenaResiduaNuova.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
			lPenaResiduaNuova.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
			lPenaResiduaNuova.setDataInserimento(aEvento.getDataInserimento());
			lPenaResiduaNuova.setCodOperatoreAggiornamento(null);
			lPenaResiduaNuova.setDataAggiornamento(null);
			lPenaResiduaNuova.setCodUfficioAggiornamento(null);

			/*
			 * if(lFlagErgastolo.equals("S")) { lPenaResiduaNuova.setDataFine(DateUtils.getDate(9999, 12,
			 * 31)); lPenaResiduaNuova.setFlagErgastolo("S"); }
			 */
			lPenDao = new PenaResiduaDAO(lConn);
			lPenDao.setDAOFromModel(lPenaResiduaNuova);
			/* BigDecimal lKeyPena = */lPenDao.insert();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);

			ex.printStackTrace();

			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciRevocaDecretoOrdinanzaSiep: "
					+ ex);
		} catch (SIEPException siepEx) {
			rollback(lConn);

			siepEx.printStackTrace();

			throw siepEx;
		} catch (Exception ex) {
			rollback(lConn);

			ex.printStackTrace();

			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciRevocaDecretoOrdinanzaSiep: "
					+ ex);
		} finally {
			cleanup(lDecDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			// cleanup(lPosSqlDao);
			cleanup(lPenDao);
			cleanup(lPenSqlDao);
			// cleanup(lSosDao);
			// cleanup(lPenSqlDao);

			cleanup(lConn);
		}

		return lDecMod;
	}

	public Vector ExRicercaDecretoOrdinanzaSiep(DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep)
			throws F3BException {
		Connection lConn = null;

		Vector lDecretoOrdinanzaSiei = new Vector();
		DecretoOrdinanzaSiepSqlDAO lDecDao = null;

		try {
			lConn = getDBConnection();
			lDecDao = new DecretoOrdinanzaSiepSqlDAO(lConn);
			lDecDao.ricercaDecretoOrdinanzaSiep(aDecretoOrdinanzaSiep);
			lDecretoOrdinanzaSiei = new Vector(lDecDao.getModels());
			if (lDecretoOrdinanzaSiei.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("DecretoOrdinanzaSiepController.ExRicercaDecretoOrdinanzaSiep: " + daoEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecretoOrdinanzaSiei;
	}

	public DecretoOrdinanzaSiepModel ExRicercaDecretoOrdinanzaSiepByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		DecretoOrdinanzaSiepSqlDAO lDecDao = null;
		DecretoOrdinanzaSiepModel lDecMod;

		try {
			lConn = getDBConnection();

			lDecDao = new DecretoOrdinanzaSiepSqlDAO(lConn);
			lDecDao.ricercaDecretoOrdinanzaSiepByKey(aKey);
			lDecMod = (DecretoOrdinanzaSiepModel) lDecDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("DecretoOrdinanzaSiepController.ExRicercaDecretoOrdinanzaSiepByKey: "
					+ daoEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecMod;
	}

	/**
	 * 
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public Vector<DecretoOrdinanzaSiepModel> ExRicercaDecretoOrdinanzaSiepByIdFasc(BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;

		DecretoOrdinanzaSiepSqlDAO lDecSqlDao = null;
		Vector<DecretoOrdinanzaSiepModel> lListaDecMod = null;

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecretoOrdinanzaSiepSqlDAO(lConn);

			lDecSqlDao.ricercaDecretoOrdinanzaSiepByFascicoloSiepDesc(aIdFascicolo);

			lListaDecMod = new Vector<DecretoOrdinanzaSiepModel>(lDecSqlDao.getModels());

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("DecretoOrdinanzaSiepController.ExRicercaDecretoOrdinanzaSiepByIdFasc: "
					+ daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lListaDecMod;
	}

	// RICERCA DECRETO PER CODICE_OGGETTO PROCEDIEMENTO

	public DecretoOrdinanzaSiepModel ExRicercaDecretoOrdinanzaSiepByOggettoProcedimento(String[] aOggetto,
			FascicoloSiepModel aFascicolo) throws F3BException {
		Connection lConn = null;

		DecretoOrdinanzaSiepSqlDAO lDecDao = null;
		DecretoOrdinanzaSiepModel lDecMod;

		try {
			lConn = getDBConnection();

			lDecDao = new DecretoOrdinanzaSiepSqlDAO(lConn);
			lDecDao.ricercaDecretoOrdinanzaSiepByOggettoProcedimento(aOggetto, aFascicolo);
			lDecMod = (DecretoOrdinanzaSiepModel) lDecDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"DecretoOrdinanzaSiepController.ExRicercaDecretoOrdinanzaSiepByOggettoProcedimento: "
							+ daoEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecMod;
	}

	public DecretoOrdinanzaSiepModel ExRicercaUltimaDecretoOrdinanzaSiepByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;

		DecretoOrdinanzaSiepSqlDAO lDecDao = null;
		DecretoOrdinanzaSiepModel lDecMod;

		try {
			lConn = getDBConnection();

			lDecDao = new DecretoOrdinanzaSiepSqlDAO(lConn);

			// lDecDao.ricercaDecretoOrdinanzaSiepByFascicoloSiepDesc(aIdFascicolo);
			lDecDao.ricercaDecretoOrdinanzaSiepByFascicoloSiepFlagElaborato(aIdFascicolo);
			lDecMod = (DecretoOrdinanzaSiepModel) lDecDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"DecretoOrdinanzaSiepController.ExRicercaUltimaDecretoOrdinanzaSiepByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lDecDao);

			cleanup(lConn);
		}

		return lDecMod;
	}

	public DecretoOrdinanzaSiepModel ExRicercaUltimaDecretoOrdinanzaSiepByIdEvento(BigDecimal aIdEvento)
			throws F3BException {
		Connection lConn = null;

		DecretoOrdinanzaSiepSqlDAO lDecDao = null;
		DecretoOrdinanzaSiepModel lDecMod;

		try {
			lConn = getDBConnection();

			lDecDao = new DecretoOrdinanzaSiepSqlDAO(lConn);

			lDecDao.ricercaDecretoOrdinanzaSiepByIdEvento(aIdEvento);
			lDecMod = (DecretoOrdinanzaSiepModel) lDecDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"DecretoOrdinanzaSiepController.ExRicercaUltimaDecretoOrdinanzaSiepByIdEvento: " + daoEx);
		} finally {
			cleanup(lDecDao);

			cleanup(lConn);
		}

		return lDecMod;
	}

	public DecretoOrdinanzaSiepModel ExRicercaUltimaDecretoOrdinanzaSiepByDecretoOrdinanzaIdFascicolo(
			BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecSqlDao = null;

		DecretoOrdinanzaSiepModel lDecMod = null;

		try {
			lConn = getDBConnection();

			lEveSqlDao = new EventoSqlDAO(lConn);

			lEveSqlDao.ricercaEventoDecretoOrdinanzaNonRegistrato(aIdFascicolo);
			EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
			lEveSqlDao.stop();

			// Se l'evento non trovato il metodo ritorna
			if (lEveMod == null)
				return null;

			lDecSqlDao = new DecretoOrdinanzaSiepSqlDAO(lConn);

			lDecSqlDao.ricercaDecretoOrdinanzaSiepByIdEvento(lEveMod.getIdEvento());
			lDecMod = (DecretoOrdinanzaSiepModel) lDecSqlDao.getModelByKey();
			lDecSqlDao.stop();
		} catch (DAOException ex) {
			rollback(lConn);

			throw new F3BException(
					"DecretoOrdinanzaSiepController.ExRicercaUltimaDecretoOrdinanzaSiepByDecretoOrdinanzaIdFascicolo: "
							+ ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lDecSqlDao);

			cleanup(lConn);
		}

		return lDecMod;
	}

	/**
	 * Inserisce/Modifica - il decreto_ordinanza_siep - l'evento (legato al decreto/ordinanza) - calcola e
	 * inserisce la pena residua - inserisce la sospensione calcolando l'eventuale pena espiata
	 * 
	 * @param aDecreto
	 * @param aEvento
	 * @param aTipo
	 *            - I = Interruzione, E = Espulsione, D = Differimento, S = Sospensione (??? mai invocata con
	 *            questo parametro)
	 * @return
	 * @throws F3BException
	 */
	public DecretoOrdinanzaSiepModel ExInserisciOModificaDecretoSospensione(
			DecretoOrdinanzaSiepModel aDecreto, EventoModel aEvento, String atipo,
			CalcoloPenaModel aCalcoloPenaModel) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		DecretoOrdinanzaSiepSqlDAO lDecSqlDAO = null;
		DecretoOrdinanzaSiepDAO lDecDAO = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		SospensioneSqlDAO lSospSqlDAO = null;
		SospensioneDAO lSospDAO = null;
		SospensioneModel lSosMod = null;
		PenaResiduaDAO lPenResDao = null;
		Vector lPeneResidue = null;
		PenaComplessivaSqlDAO lPenCompSqlDao = null;
		;
		DecretoOrdinanzaSiepModel lDecMod = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		EventoModel lEveRet = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);

			lDecMod = new DecretoOrdinanzaSiepModel(aDecreto);

			// ========================================================================
			// Recupera posizione giuridica corrente
			// ========================================================================
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lDecMod.getFasSieIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			// ========================================================================
			// Recupera il decreto ordinanza (se presente) ed effettuo l'insert o l'update
			// ========================================================================
			lDecSqlDAO = new DecretoOrdinanzaSiepSqlDAO(lConn);

			DecretoOrdinanzaSiepModel lDecModPresente = null;

			/** TODO cosa cambia???? */
			/**
			 * TODO l'id del decreto ordinanza lo posso recuperare da aDecreto.getIdDecretoOrdinanzaSiep()
			 * almeno per il Differimento e entrare in chiave
			 */

			// Verifico se presente decreto di ordinanza ?????????????????????????????
			if (atipo.equals("I")) {
				// lDecSqlDAO.ricercaDecretoOrdinanzaSiepInterruzioneByFascicoloSiepFlagElaborato(aEvento.getFasSieIdFascicoloSiep());
				lDecSqlDAO.ricercaDecretoOrdinanzaSiepByFascicoloSiepFlagElaborato(aEvento
						.getFasSieIdFascicoloSiep());
			} else {
				lDecSqlDAO.ricercaDecretoOrdinanzaSiepByFascicoloSiepFlagElaborato(aEvento
						.getFasSieIdFascicoloSiep());
			}

			lDecModPresente = (DecretoOrdinanzaSiepModel) lDecSqlDAO.getModelByKey();
			lDecSqlDAO.stop();

			// ========================================================================
			// Inserisce/aggiorna il decreto_ordinanza_siep
			// ========================================================================
			lDecDAO = new DecretoOrdinanzaSiepDAO(lConn);
			if (lDecModPresente == null) {
				lDecMod.setFlagElaborato("N");
				lDecMod.setCodEsito("-");

				lDecDAO.setDAOFromModel(lDecMod);
				BigDecimal lKeyDecOrd = null;
				lKeyDecOrd = lDecDAO.insert();
				lDecDAO.stop();

				lDecMod.setIdDecretoOrdinanzaSiep(lKeyDecOrd);
			} else {
				lDecMod.setCodOperatoreAggiornamento(lDecMod.getCodOperatoreInserimento());
				lDecMod.setCodUfficioAggiornamento(lDecMod.getCodUfficioInserimento());
				lDecMod.setDataAggiornamento(lDecMod.getDataInserimento());

				lDecMod.setIdDecretoOrdinanzaSiep(lDecModPresente.getIdDecretoOrdinanzaSiep());

				lDecDAO.setDAOFromModelForUpdate(lDecMod); // !!Problema delle date aggiornamento!!
				lDecDAO.update();

				lDecDAO.stop();

				lDecMod.setIdEventoGenerato(lDecModPresente.getIdEventoGenerato()); // ????
			}

			// ========================================================================
			// INSERIMENTO O AGGIORNAMENTO EVENTO
			// - setCodTipoEvento("01")
			// - setCodMotivo(stesso motivo in input)
			// ========================================================================
			// Verifico se esiste già a sistema un evento
			lEveDao = new EventoDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);

			EventoModel lEveRic = new EventoModel(); // Evento per la ricerca
			lEveRic.setCodMotivo(lEveRet.getCodMotivo());

			if (atipo.equals("I") || atipo.equals("D")) // interruzione/differimento
				lEveRic.setCodTipoProvvedimento("02"); // Decreto

			if (atipo.equals("S")) // sospensione
				lEveRic.setCodTipoProvvedimento("04"); // Provvedimento

			lEveRic.setCodTipoEvento("01"); // Provvedimento
			lEveRic.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
			if (atipo.equals("I")) { // ?????????????????
				lSqlDAO.ricercaEventoDecretoOrdinanzaInterruzioneNonRegistrato(aEvento
						.getFasSieIdFascicoloSiep());
			}

			if (atipo.equals("S") || atipo.equals("E")) {
				lSqlDAO.ricercaEventoTipoCodMotProvNonValidato(lEveRic);
			}

			if (atipo.equals("D")) {
				String[] tipoProvv = { "02", "03" };
				lSqlDAO.ricercaEventoTipoCodMotiviProvNonValidato(lEveRic, tipoProvv);
			}

			EventoModel lEveModel = (EventoModel) lSqlDAO.getModelByKey();

			// EventoModel lEveMod = null;
			// lSqlDAO.ricercaEventoNonRegistratoByKey(lDecMod.getIdEventoGenerato());
			// lEveMod = (EventoModel) lSqlDAO.getModelByKey();

			BigDecimal lKeyEvento = null;
			// Se non presente oppure presente non validato
			if (lEveModel == null) {
				// Setto il progressivo protocollo (l'anno c'è l'ho)
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento);
				aEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				// Setto ID_DECRETO_ORDINANZA_SIEP
				aEvento.setDecIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());

				lEveDao.setDAOFromModel(aEvento);
				lKeyEvento = lEveDao.insert();

				aEvento.setIdEvento(lKeyEvento);

			} else // Se presente lo aggiorna
			{
				lKeyEvento = lEveModel.getIdEvento();

				lEveDao.setIdEvento(lKeyEvento);

				// ********** Campi aggiornabili su EVENTO *******************************
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				// lEveDao.setFlagDocumentoRegistrato("N");

				lEveDao.setCodTipoEvento(aEvento.getCodTipoEvento());
				lEveDao.setCodTipoProvvedimento(aEvento.getCodTipoProvvedimento());
				lEveDao.setCodMotivo(aEvento.getCodMotivo());
				lEveDao.setCodUfficioEmittente(aEvento.getCodUfficioEmittente());
				lEveDao.setCodLuogoEmittente(aEvento.getCodLuogoEmittente());
				lEveDao.setDataEmissione(aEvento.getDataEmissione());
				lEveDao.setDataTrasmissioneAtti(aEvento.getDataTrasmissioneAtti());
				lEveDao.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
				lEveDao.setFlagVideoSiep(aEvento.getFlagVideoSiep());
				lEveDao.setFlagStampaSiep(aEvento.getFlagStampaSiep());
				lEveDao.setDecIdDecretoOrdinanzaSiep(lDecMod.getIdDecretoOrdinanzaSiep());

				// !!Problema delle date aggiornamento!!
				lEveDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreInserimento());
				lEveDao.setCodUfficioAggiornamento(aEvento.getCodUfficioInserimento());
				lEveDao.setDataAggiornamento(aEvento.getDataInserimento());

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
			}

			// ========================================================================
			// AGGIORNAMENTO DECRETO_ORDINANZA_SIEP CON ID_EVENTO generato
			// ========================================================================
			lDecMod.setIdEventoGenerato(lKeyEvento);
			lDecDAO.setDAOFromModelForUpdate(lDecMod); // !!Problema delle date aggiornamento!!
			lDecDAO.update();
			lDecDAO.stop();

			// ========================================================================
			// CALCOLO e INSERIMENTO PENA RESIDUA
			// viene calcolata la nuova pena residua a partire dall'ultima pena residua
			// a sistema (possibilmente validata)
			// Se non libero vengono eliminate tutte le pene residue non validate e
			// sospensioni collegate.
			// Viene inserito un NUOVO record collegato all'evento e al fascicolo
			// n.b. non viene effettuato update
			// ========================================================================
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

			// Cerca l'ultima pena residua validata...
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(lDecMod
					.getFasSieIdFascicoloSiep());
			PenaResiduaModel lUltimaPenaResidua = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			// ...se non la trova cerca l'ultima in assoluto
			if (lUltimaPenaResidua == null) {
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lDecMod.getFasSieIdFascicoloSiep());
				lUltimaPenaResidua = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			}
			lPenResSqlDao.stop();

			if (lUltimaPenaResidua == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Pena Residua non trovata, impossibile procedere.");

			PenaResiduaModel lPenResMod = new PenaResiduaModel();

			lSospDAO = new SospensioneDAO(lConn);
			lSospSqlDAO = new SospensioneSqlDAO(lConn);
			;

			// ========================================================================
			// Se non libero Cancella i record pena residua non validati e le
			// sospensioni collegate (perchè?)
			// ========================================================================
			if (!lPosMod.isLibero()) { // Cancella i record pena residua non validati e le sospensioni
										// collegate
				lPenResSqlDao.ricercaPenaResiduaFlagValidato(aEvento.getFasSieIdFascicoloSiep());
				lPeneResidue = new Vector(lPenResSqlDao.getModels());
				if (lPeneResidue.size() > 0) {
					for (int i = 0; i < lPeneResidue.size(); i++) {
						lPenResMod = (PenaResiduaModel) lPeneResidue.get(i);

						lSospSqlDAO.ricercaSospensioneByIdPenaResidua(lPenResMod.getIdPenaResidua());
						lSosMod = (SospensioneModel) lSospSqlDAO.getModelByKey();
						if (lSosMod != null) {
							lSospDAO.setCondizioneUpdate(lSosMod.getIdSospensione());
							lSospDAO.delete();
						}

						lPenResDao.setCondizioneUpdate(lPenResMod.getIdPenaResidua());
						lPenResDao.delete();
					}
				}
			}

			// ========================================================================
			// Recupero la Pena Complessiva per verificare se trattasi di ergastolo
			// ========================================================================
			lPenCompSqlDao = new PenaComplessivaSqlDAO(lConn);

			lPenCompSqlDao.ricercaPenaComplessivaByIdFascicolo(lDecMod.getFasSieIdFascicoloSiep());
			PenaComplessivaModel lPenMod = (PenaComplessivaModel) lPenCompSqlDao.getModelByKey();

			// Verifico il tipo di pena (se ergastolo)
			String lFlagErgastolo = "N";
			if (lPenMod.getCodTipoPenaDetentiva() != null
					&& lPenMod.getCodTipoPenaDetentiva() != ""
					&& (lPenMod.getCodTipoPenaDetentiva().equals("03") || lPenMod.getCodTipoPenaDetentiva()
							.equals("04"))) {
				lFlagErgastolo = "S";
			}

			// ========================================================================
			// INSERIMENTO PENA RESIDUA NUOVA -- solo se non è in Ergastolo altrimenti
			// calcola solo l'espiato
			// ========================================================================
			PenaResiduaModel lPenaResiduaNuova = new PenaResiduaModel();
			CalendarModel lPenaEspiataSosp = new CalendarModel();

			// 0268 - INTERR - consegna temporanea art. 709 comma 1c.p.p.
			// 0269 - INTERR - esecuzione penale all'estero della condanna ex art. 742 c.p.p.
			if (lFlagErgastolo.equals("N")
					&& (!aEvento.getCodMotivo().equals("0268") && !aEvento.getCodMotivo().equals("0269"))) {
				// Pena Ricalcolata sul
				PenaResiduaModel lPenaResiduaIniziale = aCalcoloPenaModel.getPenaDaEspiare(
						lUltimaPenaResidua.getDataInizio(), null, "all");

				if (atipo.equals("I"))
					aCalcoloPenaModel.calcolaPenaDaSospensione(lPenaResiduaIniziale,
							lDecMod.getDataInterruzionePena());
				if (atipo.equals("D"))
					aCalcoloPenaModel.calcolaPenaDaSospensione(lPenaResiduaIniziale,
							lDecMod.getDataDifferimento());
				if (atipo.equals("S") || atipo.equals("E"))
					aCalcoloPenaModel.calcolaPenaDaSospensione(lPenaResiduaIniziale,
							lDecMod.getDataEspulsione());

				lPenaResiduaNuova = aCalcoloPenaModel.getPenaResiduaRicalcolata();
				lPenaEspiataSosp = aCalcoloPenaModel.getPenaEspiata();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lPenaResiduaNuova : " + lPenaResiduaNuova);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lPenaEspiataSosp : " + lPenaEspiataSosp);

				lPenaResiduaNuova.setFlagValidato("N");
				if (!aEvento.getCodMotivo().equals("0366")) {
					// non inserisco la pena come interrotta nel caso di Scarcerazione
					// provvisoria per indulto
					lPenaResiduaNuova.setFlagPenaSospesa(atipo);
				}
				lPenaResiduaNuova.setEveIdEvento(lKeyEvento);
			} else {
				// Ergastolo o
				// 0268 = consegna temporanea art. 709 comma 1c.p.p.
				// 0269 = esecuzione penale all'estero della condanna ex art. 742 c.p.p.
				lPenaResiduaNuova = lUltimaPenaResidua;

				lPenaResiduaNuova.setMisAltIdMisuraAlternativa(null);
				lPenaResiduaNuova.setEveIdEvento(lKeyEvento);

				if (aEvento.getCodMotivo().equals("0268") || aEvento.getCodMotivo().equals("0269")) {
					lPenaResiduaNuova.setFlagPenaSospesa(null);

					lPenaResiduaNuova.setDataInizio(lUltimaPenaResidua.getDataInizio());
					lPenaResiduaNuova.setDataFine(lUltimaPenaResidua.getDataFine());
				} else {
					// Ergastolo
					lPenaResiduaNuova.setFlagPenaSospesa(atipo);

					lPenaResiduaNuova.setDataInizio(null);
					lPenaResiduaNuova.setDataFine(null);
				}

				lPenaResiduaNuova.setFlagValidato("N");
			}

			// Gestione della Data Inserimento
			lPenaResiduaNuova.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
			lPenaResiduaNuova.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
			lPenaResiduaNuova.setDataInserimento(aEvento.getDataInserimento());

			lPenaResiduaNuova.setCodOperatoreAggiornamento(null);
			lPenaResiduaNuova.setDataAggiornamento(null);
			lPenaResiduaNuova.setCodUfficioAggiornamento(null);

			lPenaResiduaNuova.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());

			if (lFlagErgastolo.equals("S")) {
				// lPenaResiduaNuova.setDataFine(DateUtils.getDate(9999, 12, 31));
				if (lPenMod.getCodTipoPenaDetentiva().equals("03")) {
					lPenaResiduaNuova.setFlagErgastolo("S");
				} else if (lPenMod.getCodTipoPenaDetentiva().equals("04")) {
					lPenaResiduaNuova.setFlagErgastolo("D");
				}
			} else {
				lPenaResiduaNuova.setFlagErgastolo("N");
			}

			// lPenaResiduaNuova.setImportoAmmenda(new BigDecimal(0));
			// lPenaResiduaNuova.setImportoMulta(new BigDecimal(0));
			lPenaResiduaNuova.setDiesAQuo("S");

			if (atipo.equals("I")) {
				if (aEvento.getCodMotivo().equals("0268") || aEvento.getCodMotivo().equals("0269")
						|| aEvento.getCodMotivo().equals("0366")) {
					lPenaResiduaNuova.setFlagPenaSospesa(null);
				} else {
					lPenaResiduaNuova.setFlagPenaSospesa("I");
				}
			}

			if (atipo.equals("D")) {
				lPenaResiduaNuova.setFlagPenaSospesa("D");
			}

			if (atipo.equals("S") || atipo.equals("E")) {
				lPenaResiduaNuova.setFlagPenaSospesa("S");
			}

			lPenResDao.setDAOFromModel(lPenaResiduaNuova);
			BigDecimal lKeyPena = lPenResDao.insert();

			// ========================================================================
			// INSERIMENTO SOSPENSIONE (e calcolo pena espiata)
			// ========================================================================
			CalendarModel lCalPenaEspiata = new CalendarModel();
			CalendarUtil lCalUtil = new CalendarUtil();

			if (!aEvento.getCodMotivo().equals("0268") && !aEvento.getCodMotivo().equals("0269")) {

				// Se è in Ergastolo viene calcolata la pena espiata nel vecchio modo
				if (lFlagErgastolo.equals("S")) {
					lCalPenaEspiata.setDataInizio(lUltimaPenaResidua.getDataInizio());
					if (atipo.equals("D"))
						lCalPenaEspiata.setDataFine(lDecMod.getDataDifferimento());
					if (atipo.equals("I"))
						lCalPenaEspiata.setDataFine(lDecMod.getDataInterruzionePena());
					if (atipo.equals("S") || atipo.equals("E"))
						lCalPenaEspiata.setDataFine(lDecMod.getDataEspulsione());

					lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata);
					lCalPenaEspiata = lCalUtil.ricalcolaGAM(lCalPenaEspiata);
				} else {
					lCalPenaEspiata = lPenaEspiataSosp;
				}

				SospensioneModel lSospensione = new SospensioneModel();

				if (atipo.equals("I"))
					lSospensione.setDataInizio(aDecreto.getDataInterruzionePena());
				if (atipo.equals("D"))
					lSospensione.setDataInizio(aDecreto.getDataDifferimento());
				if (atipo.equals("S") || atipo.equals("E"))
					lSospensione.setDataInizio(aDecreto.getDataEspulsione());

				lSospensione.setNumAnniPenaEspiata(new BigDecimal(lCalPenaEspiata.getNumAnni()));
				lSospensione.setNumMesiPenaEspiata(new BigDecimal(lCalPenaEspiata.getNumMesi()));
				lSospensione.setNumGiorniPenaEspiata(new BigDecimal(lCalPenaEspiata.getNumGiorni()));

				lSospensione.setNumAnniPenaResiduaReclus(lPenaResiduaNuova.getNumAnniReclusione());
				lSospensione.setNumMesiPenaResiduaReclus(lPenaResiduaNuova.getNumMesiReclusione());
				lSospensione.setNumGiorniPenaResiduaReclus(lPenaResiduaNuova.getNumGiorniReclusione());

				lSospensione.setNumAnniPenaResiduaArres(lPenaResiduaNuova.getNumAnniArresto());
				lSospensione.setNumMesiPenaResiduaArres(lPenaResiduaNuova.getNumMesiArresto());
				lSospensione.setNumGiorniPenaResiduaArres(lPenaResiduaNuova.getNumGiorniArresto());

				lSospensione.setAmmendaResidua(lPenaResiduaNuova.getImportoAmmenda());
				lSospensione.setMultaResidua(lPenaResiduaNuova.getImportoMulta());

				lSospensione.setPenResIdPenaResidua(lKeyPena);
				lSospensione.setFasSieIdFascicoloSiep(aDecreto.getFasSieIdFascicoloSiep());
				lSospensione.setNumGiorniLibanticipata(new BigDecimal(aCalcoloPenaModel
						.getLiberazioneAnticipata()));

				lSospensione.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lSospensione.setDataInserimento(aEvento.getDataInserimento());
				lSospensione.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());

				if (atipo.equals("I"))
					lSospensione.setFlagInterruzione("S");

				if (atipo.equals("E"))
					lSospensione.setDataFine(DateUtils.moveDateTo(lDecMod.getDataEspulsione(), Calendar.YEAR,
							10));

				lSospDAO.setDAOFromModel(lSospensione);
				/* BigDecimal lKeySosp = */lSospDAO.insert();

				lSospDAO.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {

			rollback(lConn);
			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciOModificaDecretoSospensione: "
					+ daoEx);
		} catch (SQLException sqe) {

			rollback(lConn);
			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciOModificaDecretoSospensione: "
					+ sqe);
		} catch (SIEPException siepEx) {
			rollback(lConn);

			siepEx.printStackTrace();

			throw siepEx;
		} catch (Exception ex) {
			ex.printStackTrace();

			rollback(lConn);
			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciOModificaDecretoSospensione: "
					+ ex);
		} finally {
			cleanup(lPenCompSqlDao);
			cleanup(lPenResDao);
			cleanup(lPosSqlDao);
			cleanup(lSospDAO);
			cleanup(lSospSqlDAO);
			cleanup(lPenResSqlDao);
			cleanup(lDecDAO);
			cleanup(lDecSqlDAO);
			cleanup(lSqlDAO);
			cleanup(lEveDao);

			cleanup(lConn);
		}

		return lDecMod;
	}

	public DecretoOrdinanzaSiepModel ExModificaDecretoOrdinanzaSiep(
			DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep) throws F3BException {
		Connection lConn = null;

		DecretoOrdinanzaSiepDAO lDecDao = null;
		DecretoOrdinanzaSiepModel lDecMod = new DecretoOrdinanzaSiepModel(aDecretoOrdinanzaSiep);

		try {
			lConn = getDBConnection();

			lDecDao = new DecretoOrdinanzaSiepDAO(lConn);

			lDecDao.setDAOFromModelForUpdate(aDecretoOrdinanzaSiep);
			lDecDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("DecretoOrdinanzaSiepController.ExModificaDecretoOrdinanzaSiep: " + ex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecMod;
	}

	public void ExCancellaDecretoOrdinanzaSiep(DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep)
			throws F3BException {
		Connection lConn = null;

		DecretoOrdinanzaSiepDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecretoOrdinanzaSiepDAO(lConn);

			lDecDao.setCondizioneUpdate(aDecretoOrdinanzaSiep.getIdDecretoOrdinanzaSiep());
			lDecDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("DecretoOrdinanzaSiepController.ExCancellaDecretoOrdinanzaSiep: " + daoEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}
	}

	/**
	 * Inserisce/Modifica
	 * 
	 * @param aDecreto
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExInserisciProvvedimentoGenerico(EventoModel aEvento, CampoNotaModel aCampo,
			DepositoDecretoModel aDecreto, DepositoOrdinanzaPcModel aOrdinanza, TenoreModel aTenore)
			throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		DepositoOrdinanzaPcDAO lDepOrdDAO = null;
		DepositoDecretoDAO lDepDAO = null;
		TenoreDAO lTenDAO = null;
		CampoNotaDAO lCamDao = null;

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);
			lDepOrdDAO = new DepositoOrdinanzaPcDAO(lConn);
			lDepDAO = new DepositoDecretoDAO(lConn);
			lTenDAO = new TenoreDAO(lConn);
			lCamDao = new CampoNotaDAO(lConn);

			// Evento
			// Setto il progressivo protocollo (l'anno c'è l'ho)
			BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento);
			aEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));
			lEveDao.setDAOFromModel(aEvento);
			BigDecimal lKeyEvento = lEveDao.insert();
			aEvento.setIdEvento(lKeyEvento);

			// Campo Note
			lCamDao.setDAOFromModel(aCampo);
			lCamDao.setEveIdEvento(lKeyEvento);
			lCamDao.insert();
			lCamDao.stop();

			BigDecimal lKeyDepDec = null;
			BigDecimal lKeyDepOrd = null;
			if (aOrdinanza != null) {
				// Deposito Ordinanza
				DepositoOrdinanzaPcModel lDepPCMod = new DepositoOrdinanzaPcModel(aOrdinanza);
				lDepPCMod.setIdEventoGenerato(lKeyEvento);
				lDepOrdDAO.setDAOFromModel(lDepPCMod);
				lKeyDepOrd = lDepOrdDAO.insert();
			} else if (aDecreto != null) {
				// DEPOSITO DECRETO
				DepositoDecretoModel lDepDecretoMod = new DepositoDecretoModel(aDecreto);
				lDepDecretoMod.setIdEventoGenerato(lKeyEvento);
				lDepDAO.setDAOFromModel(lDepDecretoMod);
				lKeyDepDec = lDepDAO.insert();
			}

			// TENORE
			if (lKeyDepDec != null) {
				aTenore.setDepDecIdDepositoDecreto(lKeyDepDec);
			} else if (lKeyDepOrd != null) {
				aTenore.setDepOpidDepositoOrdinanzaPc(lKeyDepOrd);
			}

			lTenDAO.setDAOFromModel(aTenore);

			// BigDecimal lKeyTenore = null;
			/* lKeyTenore = */lTenDAO.insert();

			commit(lConn);
		} catch (DAOException daoEx) {

			rollback(lConn);
			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciProvvedimentoGenerico: "
					+ daoEx);
		} catch (SIEPException siepEx) {
			rollback(lConn);

			siepEx.printStackTrace();

			throw siepEx;
		} catch (Exception ex) {
			ex.printStackTrace();

			rollback(lConn);
			throw new F3BException("DecretoOrdinanzaSiepController.ExInserisciProvvedimentoGenerico: " + ex);
		} finally {
			cleanup(lSqlDAO);
			cleanup(lEveDao);
			cleanup(lDepOrdDAO);
			cleanup(lDepDAO);
			cleanup(lTenDAO);
			cleanup(lCamDao);

			cleanup(lConn);
		}

		return aEvento;
	}

	/**
	 * @ExUpdateValidaProvvedimentoGenerico
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */

	public EventoModel ExUpdateValidaProvvedimentoGenerico(EventoModel aEvento, String lCodStato)
			throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;

		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			EventoSqlDAO lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDAO.getModelByKey();

			// STATO_PROCEDIMENTO
			if (lCodStato != null) {
				// cancellazione
				lStatoDao = new StatoProcedimentoDAO(lConn);
				lStatoDao.setCondizioneByIdFascicolo(lEveModel.getFasSieIdFascicoloSiep());
				lStatoDao.delete();

				// inserimento
				StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
				lStatoProcMod.setProgressivo(new BigDecimal(1));

				lStatoProcMod.setCodStatoProcedimento(lCodStato);
				lStatoProcMod.setData(lEveModel.getDataEmissione());
				lStatoProcMod.setFasSieIdFascicoloSiep(lEveModel.getFasSieIdFascicoloSiep());

				lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
				lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

				lStatoDao.setDAOFromModel(lStatoProcMod);
				lStatoDao.insert();
			}

			// PENA RESIDUA
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

			// Cerca l'ultima pena residua validata...
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(aEvento
					.getFasSieIdFascicoloSiep());
			PenaResiduaModel lUltimaPenaResidua = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			// ...se non la trova cerca l'ultima in assoluto
			if (lUltimaPenaResidua == null) {
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
				lUltimaPenaResidua = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			}
			lPenResSqlDao.stop();

			if (lUltimaPenaResidua != null && lUltimaPenaResidua.getEveIdEvento() == null) { // Se la pena non
																								// è
																								// agganciata
																								// ad alcun
																								// evento, la
																								// aggancia
																								// all'evento
																								// corrente
				lPenResDao.setIdPenaResidua(lUltimaPenaResidua.getIdPenaResidua());
				lPenResDao.setEveIdEvento(aEvento.getIdEvento());
				lPenResDao.setFlagValidato("S");
				lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPenResDao.setDataAggiornamento(DateUtils.getSysDate());
				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			} else { // se invece la pena è agganciata ad altro evento, al duplico
				lPenResDao.setDAOFromModel(lUltimaPenaResidua);
				lPenResDao.setFlagValidato("S");
				lPenResDao.setEveIdEvento(aEvento.getIdEvento());
				lPenResDao.setDataInserimento(DateUtils.getSysDate());
				lPenResDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lPenResDao.insert();
				lPenResDao.stop();
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
			rollback(lConn);
			rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("ExUpdateValidaOrdineScarcerazione.ExUpdateValidaProvvedimentoGenerico : "
					+ daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("ExUpdateValidaOrdineScarcerazione.ExUpdateValidaProvvedimentoGenerico : "
					+ ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lStatoDao);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);

			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return aEvento;
	}

	// 01-09-2015 MEV_2 - Misure Sicurezza STEP2-
	public EventoModel ExInserisciProvvedimentoDecisioneCassazioneRiesame(EventoModel aEvento,
			EventoModel aEventoOrdDec, DecretoOrdinanzaSiepModel aDecretoOrdinanzaSiep,
			MisuraSicurezzaModel aMisuraSicurezza) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoDAO lEveOrdDao = null;
		EventoSqlDAO lSqlDAO = null;
		DecretoOrdinanzaSiepDAO lDecDao = null;
		MisuraSicurezzaDAO lMisDao = null;
		MisuraSicurezzaSqlDAO lMisSqlDao = null;

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);
			lEveOrdDao = new EventoDAO(lConn);
			lDecDao = new DecretoOrdinanzaSiepDAO(lConn);

			// Evento 1 - Ordinanza/Decreto Cassazione/Riesame
			lEveOrdDao.setDAOFromModel(aEventoOrdDec);
			BigDecimal lKeyEveOrd = lEveOrdDao.insert();
			aEventoOrdDec.setIdEvento(lKeyEveOrd);

			// Evento 2 - Provvedimento SIEP (Annotazione)
			aEvento.setEveIdEvento(lKeyEveOrd);
			lEveDao.setDAOFromModel(aEvento);
			BigDecimal lKeyEvento = lEveDao.insert();
			aEvento.setIdEvento(lKeyEvento);

			// DecretoOrdinanzaSIEP
			aDecretoOrdinanzaSiep.setIdEventoGenerato(lKeyEveOrd);
			lDecDao.setDAOFromModel(aDecretoOrdinanzaSiep);
			lDecDao.insert();

			// Misura Sicurezza:
			if (aEventoOrdDec.getCodEsito().compareTo("0051") == 0 || // esecuzione Misura
					aEventoOrdDec.getCodEsito().compareTo("0053") == 0) // proroga misura
			{
				// UPDATE
				if (aMisuraSicurezza != null && aMisuraSicurezza.getLuogoEsecuzioneMisura() != null) {
					lMisSqlDao = new MisuraSicurezzaSqlDAO(lConn);
					lMisSqlDao.ricercaMisuraSicurezzaByIdFascicoloOrd(aEvento.getFasSieIdFascicoloSiep());
					Vector lVecMis = new Vector(lMisSqlDao.getModels());
					Iterator iterVec = lVecMis.iterator();
					while (iterVec.hasNext()) {
						lMisDao = new MisuraSicurezzaDAO(lConn);
						MisuraSicurezzaModel lMis = (MisuraSicurezzaModel) iterVec.next();
						if (lMis != null && lMis.getIdMisuraSicurezza() != null) {
							lMis.setLuogoEsecuzioneMisura(aMisuraSicurezza.getLuogoEsecuzioneMisura());
							lMis.setCodOperatoreAggiornamento(aMisuraSicurezza.getCodOperatoreInserimento());
							lMis.setCodUfficioAggiornamento(aMisuraSicurezza.getCodUfficioInserimento());
							lMis.setDataAggiornamento(aMisuraSicurezza.getDataInserimento());
							lMisDao.setDAOFromModelForUpdate(lMis);
							lMisDao.update();
							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							// siesLogger.debug(" --XX-- DecretoOrdinanzaSiepController : Misura Modificata LuogoEsecuzMisura = "+lMis
							// );
						}
					}
				}
				// else
				// throw new SIEPException(SIEPException.USER_MESSAGE,
				// "ERRORE su UPDATE - Codice Esito NON CONGRUO con il dato MisuraSicurezzaModel");

			} else if (aEventoOrdDec.getCodEsito().compareTo("0057") == 0
					|| aEventoOrdDec.getCodEsito().compareTo("0189") == 0
					|| aEventoOrdDec.getCodEsito().compareTo("0403") == 0) {
				// INSERT Nuova Misura e UPDATE della/e Vecchia Misura
				if (aMisuraSicurezza != null && aMisuraSicurezza.getCodTipo() != null) {
					BigDecimal lKey = null;
					lMisDao = new MisuraSicurezzaDAO(lConn);
					aMisuraSicurezza.setEveIdEvento(lKeyEvento);
					lMisDao.setDAOFromModel(aMisuraSicurezza);
					lKey = lMisDao.insert();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug(" --XX-- DecretoOrdinanzaSiepController : Misura Inserita = "+aMisuraSicurezza
					// );

					lMisSqlDao = new MisuraSicurezzaSqlDAO(lConn);
					lMisSqlDao.ricercaMisuraSicurezzaByIdFascicoloOrd(aEvento.getFasSieIdFascicoloSiep());
					Vector lVecMis = new Vector(lMisSqlDao.getModels());
					Iterator iterVec = lVecMis.iterator();
					while (iterVec.hasNext()) {
						lMisDao = new MisuraSicurezzaDAO(lConn);
						MisuraSicurezzaModel lMis = (MisuraSicurezzaModel) iterVec.next();
						if (lMis != null && lMis.getIdMisuraSicurezza() != null) {
							if (lMis.getIdMisuraSicurezza().compareTo(lKey) != 0) { // NON devo fare Update
																					// della Misura appena
																					// inserita (lkey)
								lMis.setMisIdMisuraSicurezza(lKey);
								lMisDao.setDAOFromModelForUpdate(lMis);
								lMisDao.update();
								// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
								// siesLogger al posto di LogF3B.getLogger()
								// siesLogger.debug(" --XX-- DecretoOrdinanzaSiepController : Misura Old Modificata MisIdMisura = "+lMis
								// );
							}
						}
					}
				} else
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"ERRORE su INSERT - Codice Esito NON CONGRUO con il dato MisuraSicurezzaModel");
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAO EXCEPTION  ---> ", daoEx);
			throw new F3BException(
					"DecretoOrdinanzaSiepController.ExInserisciProvvedimentoDecisioneCassazioneRiesame: "
							+ daoEx);
		} catch (SIEPException siepEx) {
			rollback(lConn);
			// siepEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("siepEX EXCEPTION  ---> ", siepEx);
			throw new F3BException(
					"DecretoOrdinanzaSiepController.ExInserisciProvvedimentoDecisioneCassazioneRiesame: "
							+ siepEx);
		} catch (Exception ex) {
			// ex.printStackTrace();
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception --->: ", ex);
			throw new F3BException(
					"DecretoOrdinanzaSiepController.ExInserisciProvvedimentoDecisioneCassazioneRiesame: "
							+ ex);
		} finally {
			cleanup(lSqlDAO);
			cleanup(lEveDao);
			cleanup(lEveOrdDao);
			cleanup(lDecDao);
			cleanup(lMisDao);
			cleanup(lMisSqlDao);

			cleanup(lConn);
		}

		return aEvento;

	} // CHIUDE ExInserisciProvvedimentoDecisioneCassazioneRiesame()

	public DecretoOrdinanzaSiepModel ExRicercaDecretoOrdinanzaSiepByIdEventoSemplice(BigDecimal aIdEvento)
			throws F3BException {
		Connection lConn = null;

		DecretoOrdinanzaSiepSqlDAO lDecDao = null;
		DecretoOrdinanzaSiepModel lDecMod;

		try {
			lConn = getDBConnection();

			lDecDao = new DecretoOrdinanzaSiepSqlDAO(lConn);

			lDecDao.ricercaDecretoOrdinanzaSiepByIdEventoSemplice(aIdEvento);
			lDecMod = (DecretoOrdinanzaSiepModel) lDecDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException --->: ", daoEx);
			throw new F3BException(
					"DecretoOrdinanzaSiepController.ExRicercaDecretoOrdinanzaSiepByIdEventoSemplice: "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception Generica --->: ", ex);
			throw new F3BException(
					"DecretoOrdinanzaSiepController.ExRicercaDecretoOrdinanzaSiepByIdEventoSemplice: " + ex);
		} finally {
			cleanup(lDecDao);

			cleanup(lConn);
		}

		return lDecMod;
	}

	// 01-09-2015 MEV_2 - Misure Sicurezza STEP2-
	public void ExValidaAnnotazioneDecisioneGiudiceCassazioneRiesame(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {
		Connection lConn = null;

		FascicoloSiepDAO lFascDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDaoBlob = null;
		EventoSqlDAO lEveGiuSqlDao = null;
		EventoDAO lEveGiuDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecSqlDao = null;
		DecretoOrdinanzaSiepDAO lDecDao = null;
		MisuraSicurezzaSqlDAO lMisSqlDao = null;
		MisuraSicurezzaDAO lMisDAO = null;

		try {
			lConn = getDBTransaction();

			// ** Cerac EVENTO Annotazione TipoProv= 25 **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// String lMotivo = "";
			// String lStatoProcMod = "";

			// SETTA LO STATO PROCEDIMENTO
			/*
			 * if (lEveModel != null && lEveModel.getIdEvento()!= null && lEveModel.getCodMotivo() != null) {
			 * lMotivo=lEveModel.getCodMotivo(); lStatoProcMod = "0512"; // annotazione Decisione Sorveglianza
			 * if (lStatoProcMod.compareTo("") != 0) { BigDecimal lKeyEvento = lEveModel.getIdEvento();
			 * 
			 * // InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
			 * lStatoProcMod, lKeyEvento); } }
			 */
			// --------- MISURA SICUREZZA -----------
			//
			lMisSqlDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDAO = new MisuraSicurezzaDAO(lConn);

			MisuraSicurezzaModel lMisMod = null;
			MisuraSicurezzaModel lMisOldMod = null;

			Vector MisSicVec = null;
			Vector OldMisSicVec = null;

			// Nuova Misura
			lMisSqlDao.ricercaMisuraSicurezzaByEventoKey(aEvento.getIdEvento());
			MisSicVec = new Vector(lMisSqlDao.getModels());
			lMisSqlDao.stop();

			BigDecimal lNewMisKey = null;
			if (MisSicVec != null && MisSicVec.size() > 0) {
				Iterator Itx1 = MisSicVec.iterator();
				while (Itx1.hasNext()) {
					lMisMod = (MisuraSicurezzaModel) Itx1.next();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.info("--XX-- Nuova Misura Trovata = " + lMisMod);
					if (lMisMod != null && lMisMod.getIdMisuraSicurezza() != null) {
						// prendo IdMisura della Nuova MIS.SIC
						lNewMisKey = lMisMod.getIdMisuraSicurezza();
					}

					if (lNewMisKey != null) {
						// Vecchia Misura
						lMisSqlDao.ricercaMisuraSicurezzaByKeyMisuraCollegata(lNewMisKey);
						OldMisSicVec = new Vector(lMisSqlDao.getModels());
						lMisSqlDao.stop();

						if (OldMisSicVec != null && OldMisSicVec.size() > 0) {
							Iterator Itx2 = OldMisSicVec.iterator();
							while (Itx2.hasNext()) {
								lMisOldMod = (MisuraSicurezzaModel) Itx2.next();
								// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
								// siesLogger al posto di LogF3B.getLogger()
								// siesLogger.info("--XX-- Vecchia Misura - " + lMisOldMod);
								if (lMisOldMod != null && lMisOldMod.getIdMisuraSicurezza() != null) {
									lMisOldMod.setDataAggiornamento(aEvento.getDataAggiornamento());
									lMisOldMod.setCodUfficioAggiornamento(aEvento
											.getCodUfficioAggiornamento());
									lMisOldMod.setCodOperatoreAggiornamento(aEvento
											.getCodOperatoreAggiornamento());
									lMisOldMod.setDataFineValidita(lEveModel.getDataEmissione());

									lMisDAO.setDAOFromModelForUpdate(lMisOldMod);
									lMisDAO.update();
									lMisDAO.stop();
								}
							}
						}
					}
				}
			}

			// ------- AGGIORNA EVENTO ANNOTAZIONE (TipoProvv 25) --------
			lEveDaoBlob = new EventoDAO(lConn);

			lEveModel.setDataAggiornamento(aEvento.getDataAggiornamento());
			lEveModel.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lEveModel.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lEveModel.setFlagDocumentoRegistrato("S");
			lEveDaoBlob.setDAOFromModelForUpdateBlob(lEveModel);

			lEveDaoBlob.selCondizioneUpdate(lEveModel.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			// ------- RICERCA e AGGIORNA EVENTO PROVVEDIMENTO CASSAZIONE/RIESAME (TipoProvv 02/03) --------
			lEveGiuSqlDao = new EventoSqlDAO(lConn);
			lEveGiuSqlDao.ricercaEventoByKey(lEveModel.getEveIdEvento());

			EventoModel lEveGiuModel = (EventoModel) lEveGiuSqlDao.getModelByKey();
			if (lEveGiuModel != null && lEveGiuModel.getIdEvento() != null) {
				// LogF3B.getLogger("--XX-- DecretoordinanzaSiepController EvendtoOrdinanza = "+lEveGiuModel);
				lEveGiuDao = new EventoDAO(lConn);

				lEveGiuModel.setFlagDocumentoRegistrato("S");
				lEveGiuModel.setDataAggiornamento(aEvento.getDataAggiornamento());
				lEveGiuModel.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lEveGiuModel.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

				lEveGiuDao.setDAOFromModelForUpdateBlob(lEveGiuModel);

				lEveGiuDao.selCondizioneUpdate(lEveGiuModel.getIdEvento());
				lEveGiuDao.update();
				lEveGiuDao.stop();
			}

			// -- -- -- -- -- -- -- -- -- -- -- -- --
			// 18/11/2014 Si setta FLAG_ELABORATO = "S" di DECRETO_ORDINANZA_SIEP

			lDecSqlDao = new DecretoOrdinanzaSiepSqlDAO(lConn);
			DecretoOrdinanzaSiepModel lDecMod = null;

			lDecSqlDao.ricercaDecretoOrdinanzaSiepByIdEventoSemplice(lEveGiuModel.getIdEvento());
			lDecMod = (DecretoOrdinanzaSiepModel) lDecSqlDao.getModelByKey();

			if (lDecMod != null && lDecMod.getIdDecretoOrdinanzaSiep() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("--XX-- DecretoordinanzaSiepController lDepODecreto legato all'Ordinanza: "
						+ lDecMod.getIdDecretoOrdinanzaSiep());

				lDecDao = new DecretoOrdinanzaSiepDAO(lConn);

				lDecMod.setFlagElaborato("S");

				lDecMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lDecMod.setDataAggiornamento(aEvento.getDataAggiornamento());
				lDecMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

				lDecDao.setDAOFromModelForUpdate(lDecMod);

				lDecDao.update();
				lDecDao.stop();
			}

			// ---------------------
			commit(lConn);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();

			throw new F3BException(
					"DecretoordinanzaSiepController.ExValidaAnnotazioneDecisioneGiudiceCassazioneRiesame : "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex, ex);
			rollback(lConn);
			ex.printStackTrace();

			throw new F3BException(
					"DecretoordinanzaSiepController.ExValidaAnnotazioneDecisioneGiudiceCassazioneRiesame : "
							+ ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lEveDaoBlob);
			cleanup(lFascDao);
			cleanup(lDecSqlDao);
			cleanup(lDecDao);
			cleanup(lEveGiuSqlDao);
			cleanup(lEveGiuDao);
			cleanup(lMisSqlDao);
			cleanup(lMisDAO);
			cleanup(lConn);

		}

	}// Chiude ExValidaAnnotazioneDecisioneGiudiceCassazioneRiesame()

	public Vector ExRicercaDecretoOrdinanzaSiepGiudiceCassazione(BigDecimal aIdFas, String[] aCodici)
			throws F3BException {
		Connection lConn = null;

		DecretoOrdinanzaSiepSqlDAO lDecSqlDao = null;
		Vector lProvv = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecretoOrdinanzaSiepSqlDAO(lConn);

			lDecSqlDao.RicercaDecretoOrdinanzaSiepGiudiceCassazione(aIdFas, aCodici);
			lDecSqlDao.start();
			while (lDecSqlDao.next()) {
				DecretoOrdinanzaSiepModel lDecMod = (DecretoOrdinanzaSiepModel) lDecSqlDao.getModel();
				lProvv.add(lDecMod);
			}
			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DecretoordinanzaSiepController.ExRicercaDecretoOrdinanzaSiepGiudiceCassazione : "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex, ex);
			throw new F3BException(
					"DecretoordinanzaSiepController.ExRicercaDecretoOrdinanzaSiepGiudiceCassazione : " + ex);
		} finally {
			cleanup(lConn);
			cleanup(lDecSqlDao);
		}

		return lProvv;
	} // Chiude ExRicercaDecretoOrdinanzaSiepGiudiceCassazione

} // CHIUDE Controller