package siap.siep.verbale.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.altracausa.dao.AltraCausaDAO;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
//import siap.siep.fascicolo.model.FascicoloSiepModel;
//import siap.siep.istruttoria.action.ICostantiIstruttoria;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.statoprocedimento.controller.IStatoProcedimento;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
//import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.dao.EventoVerbaleSqlDAO;
import siap.siep.verbale.dao.VerbaleDAO;
import siap.siep.verbale.dao.VerbaleSqlDAO;
import siap.siep.verbale.model.VerbaleDataInizioModel;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.dao.EsecuzioneMisuraAlternativaDAO;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.fascicolo.dao.FascicoloSiusSqlDAO;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.scadenzario.dao.ScadenzarioSiusDAO;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: VerbaleController
 * </p>
 * <p>
 * Description: Classe Controller per Verbale
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
public class VerbaleController extends SiapController implements IVerbale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public VerbaleModel ExInserisciVerbale(BigDecimal aKeyFasc, VerbaleModel aVerbale,
			PosizioneGiuridicaModel aPosMod, LuogoDetenzioneModel aLuoDetMod, NotificaModel aNotMod,
			ScadenzarioModel aScaMod, PenaResiduaModel aPenaResidua) throws F3BException {

		Connection lConn = null;

		VerbaleDAO lVerDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		StatoProcedimentoDAO lStaProDao = null;
		ScadenzarioDAO lScaDao = null;

		PenaResiduaDAO lPenDao = null;
		PenaResiduaSqlDAO lPenResDao = null;

		LicenzaLibanticipataSqlDAO lLicSqlDao = null;

		EventoDAO lEveDao = null;

		VerbaleModel lVerMod = null;
		EventoNotificaModel lEveNot = null;

		try {
			lConn = getDBTransaction();

			// ricerco Evento per aggiornare la notifica
			lEveNot = ExRicercaEventoVerbale(aKeyFasc,
					"Nessun Documento registrato in relazione al verbale d'arresto");

			// =====================================================
			// Inserisco l'evento relativo al verbale (Validato)
			// =====================================================
			lEveDao = new EventoDAO(lConn);

			lEveDao.setCodTipoEvento("07"); // Verbale
			lEveDao.setCodTipoProvvedimento("16"); // Verbale di arresto
			lEveDao.setCodMotivo("0312"); // Verbale Arresto
			lEveDao.setFlagDocumentoRegistrato("S");
			lEveDao.setFlagVideoSiep("S");
			lEveDao.setFlagStampaSiep("S");

			if (lEveNot.getEvento() != null)
				lEveDao.setEveIdEvento(lEveNot.getEvento().getIdEvento());

			lEveDao.setFasSieIdFascicoloSiep(aKeyFasc);
			lEveDao.setCodOperatoreInserimento(aVerbale.getCodOperatoreInserimento());
			lEveDao.setCodUfficioInserimento(aVerbale.getCodUfficioInserimento());
			lEveDao.setDataInserimento(aVerbale.getDataInserimento());

			lEveDao.setDataEmissione(aVerbale.getDataEmissione());
			lEveDao.setCodEsito("-");
			lEveDao.setCodMagistrato("-");
			lEveDao.setCodLuogoDestinatario("-");
			lEveDao.setCodTipoUfficioDestinatario("-");
			if (lEveNot.getEvento() != null && lEveNot.getEvento().getCodLuogoEmittente() != null)
				lEveDao.setCodLuogoEmittente(lEveNot.getEvento().getCodLuogoEmittente());
			else
				lEveDao.setCodLuogoEmittente("-");
			lEveDao.setCodUfficioEmittente(aVerbale.getCodUfficioInserimento());

			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// ===============================================
			// Inserisco il Verbale
			// ===============================================
			lVerMod = new VerbaleModel(aVerbale);
			lVerMod.setEveIdEvento(lKeyEvento);
			lVerDao = new VerbaleDAO(lConn);
			lVerDao.setDAOFromModel(lVerMod);
			BigDecimal lKey = null;
			lKey = lVerDao.insert();
			lVerDao.stop();

			// ========================================================================
			// Aggiornamento Data_Fine vecchia posizione giuridica e inserimento della
			// nuova
			// ========================================================================
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao = new PosizioneGiuridicaDAO(lConn);

			PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel(aPosMod);

			BigDecimal lIdFasc = lPosMod.getFasSieIdFascicoloSiep();
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lIdFasc);

			PosizioneGiuridicaModel lPosizMod = null;
			lPosizMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			if (lPosizMod != null) {
				// update
				lPosDao.setIdPosizioneGiuridica(lPosizMod.getIdPosizioneGiuridica());
				lPosDao.setDataFine(lPosMod.getDataInizio());

				lPosDao.setCodOperatoreAggiornamento(lPosMod.getCodOperatoreInserimento());
				lPosDao.setCodUfficioAggiornamento(lPosMod.getCodUfficioInserimento());
				lPosDao.setDataAggiornamento(lPosMod.getDataInserimento());

				lPosDao.selByKey();
				lPosDao.update();
				lPosDao.stop();
			}

			aPosMod.setIdEventoRiferimento(lKeyEvento);

			// insert
			lPosDao.setDAOFromModel(aPosMod);
			BigDecimal lKeyPosGiu = null;
			lKeyPosGiu = lPosDao.insert();
			lPosDao.stop();

			// ====================================================
			// Inserimento Luogo di Detenzione
			// ====================================================
			lLuoDetDao = new LuogoDetenzioneDAO(lConn);
			LuogoDetenzioneModel lLuoDetMod = new LuogoDetenzioneModel(aLuoDetMod);
			lLuoDetMod.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);
			lLuoDetDao.setDAOFromModel(lLuoDetMod);
			lLuoDetDao.insert();
			lLuoDetDao.stop();

			// ****************************Aggiorna Notifica***************************************/
			NotificaDAO lNotDAO = new NotificaDAO(lConn);
			NotificaModel lNotificaMod = new NotificaModel(aNotMod);

			if (lEveNot != null && lEveNot.getNotifiche() != null && lEveNot.getNotifiche().length > 0) {
				lNotDAO.setCodEsito(lNotificaMod.getCodEsito());
				lNotDAO.setIdNotifica(lEveNot.getNotifiche()[0].getIdNotifica());

				lNotDAO.setCondizioneUpdate(lEveNot.getNotifiche()[0].getIdNotifica());

				lNotDAO.setCodiceOperatoreAggiornamento(lNotificaMod.getCodiceOperatoreAggiornamento());
				lNotDAO.setCodUfficioAggiornamento(lNotificaMod.getCodUfficioAggiornamento());
				lNotDAO.setDataAggiornamento(lNotificaMod.getDataAggiornamento());

				lNotDAO.update();
				lNotDAO.stop();
			}

			/***************************** Fine Aggiorna Notifica ********************************/
			/***************************** Scadenzario *******************************************/

			IScadenzario lCtrlSca = SIEPLookupRemote.getScadenzarioRemote();
			Vector lVect = lCtrlSca.ExRicercaScadenzarioVerbaleArresto(aScaMod);
			Iterator lIter = lVect.iterator();
			while (lIter.hasNext()) {
				ScadenzarioModel lScadmodel = (ScadenzarioModel) lIter.next();
				lScaDao = new ScadenzarioDAO(lConn);
				lScaDao.setCondizioneUpdate(lScadmodel.getIdScadenzario());
				lScaDao.delete();
			}
			/**************************** Fine Scadenzario ***************************************/
			/**************************** Stato Procedimento *************************************/
			IStatoProcedimento lCtrlStaProc = SIEPLookupRemote.getStatoProcedimentoRemote();
			Vector lVectPro = lCtrlStaProc.ExRicercaStatoProcedimentoByFascicoloSiep(aKeyFasc);

			Iterator lIterPro = lVectPro.iterator();

			while (lIterPro.hasNext()) {
				lStaProDao = new StatoProcedimentoDAO(lConn);
				StatoProcedimentoModel lStaModel = (StatoProcedimentoModel) lIterPro.next();
				lStaProDao.setCondizioneUpdate(lStaModel);
				lStaProDao.delete();
				lStaProDao.stop();
			}

			// insert
			lStaProDao = new StatoProcedimentoDAO(lConn);
			StatoProcedimentoModel lStaProMod = new StatoProcedimentoModel();

			for (int ins = 1; ins < 3; ins++) {
				lStaProMod.setCodOperatoreInserimento(lVerMod.getCodOperatoreInserimento());
				lStaProMod.setProgressivo(new BigDecimal(ins));
				if (ins == 2) {
					lStaProMod.setCodStatoProcedimento("0004");
				} else {
					lStaProMod.setCodStatoProcedimento("0003");
				}
				lStaProMod.setDataInserimento(lVerMod.getDataInserimento());
				lStaProMod.setCodUfficioInserimento(lVerMod.getCodUfficioInserimento());
				lStaProMod.setData(lVerMod.getDataEmissione());
				lStaProMod.setFasSieIdFascicoloSiep(aKeyFasc);

				lStaProDao.setDAOFromModel(lStaProMod);
				lStaProDao.insert();
				lStaProDao.stop();
			}

			/**************************** Fine Stato Procedimento ********************************/

			/**************************** Pena Residua *******************************************/
			// ========================================================================
			// Aggiornamento/Inserimento della pena residua con eventuali giorni di
			// Licenza Liberazione Anticipata
			// ========================================================================
			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenDao = new PenaResiduaDAO(lConn);

			aPenaResidua.setEveIdEvento(lKeyEvento);
			aPenaResidua.setFlagPenaSospesa(null);

			// ========================================================================
			// CALCOLO LIBERAZIONE ANTICIPATA, CERCA IL TOTALE GIORNI LIB ANTICIPATA
			// N.B. cerca quelli con FLAG_ELABORATO ad N o NULL per i quali non è ancora stato
			// effettuato il calcolo della pena.
			// ========================================================================
			/*
			 * lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
			 *
			 * lLicSqlDao.ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP(aKeyFasc, "N");
			 *
			 * List lLicenze = new ArrayList(lLicSqlDao.getModels());
			 *
			 * int lTotGiorni = 0; LicenzaLibAnticipataModel lLicModel; if(lLicenze != null &&
			 * !lLicenze.isEmpty()) { Iterator lIterLib = lLicenze.iterator();
			 *
			 * while(lIterLib.hasNext()) { lLicModel = (LicenzaLibAnticipataModel)lIterLib.next();
			 *
			 * lTotGiorni += lLicModel.getNumeroGiorni().intValue(); } }
			 */

			// PenaResiduaModel lPenMod = new PenaResiduaModel(aPenaResidua);
			// Date lDataFine = lPenMod.getDataFine();
			// if(lDataFine == null)
			// {
			// lDataFine = lPenMod.getDataFinePresunta();
			// lPenMod.setDataFine(lDataFine);
			// }

			// if(lPenMod != null && lTotGiorni != 0 && lDataFine != null)
			// {
			// // CALCOLA LA NUOVA PENA RESIDUA (quantum e date) con la nuova data fine
			// // ottenuta sottraendo i gg di liberazione anticipata
			// Date lDataFineRicalcolata = DateUtils.moveDateTo( lDataFine, Calendar.DAY_OF_MONTH,
			// -(lTotGiorni) );
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Data Fine Ricalcolata : "+lDataFineRicalcolata);

			/*
			 * -- NON TOCCA PIU' I QUANTUM lPenMod =
			 * PenaResiduaUtil.calcolaPenaNuovaDataFine(lDataFineRicalcolata, lPenMod, true); // [FT] -
			 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() siesLogger.debug("Nuova Pena Redidua : "+lPenMod);
			 *
			 * lPenMod.setDataFinePresunta(lPenMod.getDataFine());
			 */
			/*  */ // lPenMod.setDataFine(lDataFineRicalcolata); /* */
			/*  */ // lPenMod.setDataFinePresunta(lDataFineRicalcolata); /* */

			// Aggiorna i giorni di Lib Anticipata computati non elaborati ad E
			// lLicSqlDao.updateFlagElaboratoByIdFascicolo(aKeyFasc, "N", "E");

			// Aggiorna le date e i quantum della nuova pena residua
			/*
			 * aPenaResidua.setDataInizio (lPenMod.getDataInizio()); aPenaResidua.setDataInizioArresto
			 * (lPenMod.getDataInizioArresto()); aPenaResidua.setDataFineReclusione
			 * (lPenMod.getDataFineReclusione()); aPenaResidua.setDataFine (lPenMod.getDataFine());
			 * aPenaResidua.setDataFinePresunta (lPenMod.getDataFinePresunta());
			 *
			 * aPenaResidua.setNumAnniReclusione (lPenMod.getNumAnniReclusione());
			 * aPenaResidua.setNumMesiReclusione (lPenMod.getNumMesiReclusione());
			 * aPenaResidua.setNumGiorniReclusione (lPenMod.getNumGiorniReclusione());
			 *
			 * aPenaResidua.setNumAnniArresto (lPenMod.getNumAnniArresto()); aPenaResidua.setNumMesiArresto
			 * (lPenMod.getNumMesiArresto()); aPenaResidua.setNumGiorniArresto
			 * (lPenMod.getNumGiorniArresto());
			 */
			// }

			// aggiorna usando lo stesso model che viene passato
			if (aPenaResidua.getFlagValidato().equals("N")) {
				// Nel caso di ergastolo la pena viene validata
				if (aPenaResidua.getFlagErgastolo() != null && !aPenaResidua.getFlagErgastolo().equals("N"))
					aPenaResidua.setFlagValidato("S");
				else
					aPenaResidua.setFlagValidato("N");

				lPenDao.setDAOFromModelForUpdate(aPenaResidua);
				lPenDao.update();
			} else if (aPenaResidua.getFlagValidato().equals("S")) // fine aggiornamento inizio inserimento
			{
				// Nel caso di ergastolo la pena viene validata
				if (aPenaResidua.getFlagErgastolo() != null && !aPenaResidua.getFlagErgastolo().equals("N"))
					aPenaResidua.setFlagValidato("S");
				else
					aPenaResidua.setFlagValidato("N");

				lPenDao.setDAOFromModel(aPenaResidua);
				lPenDao.insert();
			}

			// ========================================================================
			// Aggiorna i giorni di Lib Anticipata computati non elaborati ad E
			// ========================================================================
			if (aPenaResidua.getFlagErgastolo() == null || aPenaResidua.getFlagErgastolo().equals("N")) {
				lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
				lLicSqlDao.updateFlagElaboratoByIdFascicolo(aKeyFasc, "N", "E");
			}
			/**************************** Fine Pena Residua **************************************/

			lVerMod.setIdVerbale(lKey);
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleController.ExInserisciVerbale: " + ex);
		} catch (F3BException fex) {
			rollback(lConn);

			fex.printStackTrace();

			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				throw fex;
			} else {
				throw new F3BException("VerbaleController.ExInserisciVerbale: " + fex);
			}
		} catch (Exception es) {
			rollback(lConn);
			es.printStackTrace();
			siesLogger.debug("SQLException: " + es);
			throw new F3BException("VerbaleController.ExInserisciVerbale: " + es);
		} finally {
			cleanup(lVerDao);
			cleanup(lPosSqlDao);
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lStaProDao);
			cleanup(lScaDao);
			cleanup(lPenDao);
			cleanup(lPenResDao);
			cleanup(lLicSqlDao);
			cleanup(lEveDao);

			cleanup(lConn);
		}

		return lVerMod;
	}

	public void ExRegistraPenaVerbaleArresto(PenaResiduaModel aPenaResidua) throws F3BException {
		Connection lConn = null;

		PenaResiduaDAO lPenDao = null;
		LicenzaLibanticipataSqlDAO lLicSqlDao = null;

		// PenaResiduaModel lPenMod = new PenaResiduaModel(aPenaResidua);

		try {
			lConn = getDBTransaction();

			lPenDao = new PenaResiduaDAO(lConn);

			lPenDao.setDAOFromModelForUpdate(aPenaResidua);

			lPenDao.update();

			// Aggiorna i giorni di Lib Anticipata computati da E ad S
			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicSqlDao.updateFlagElaboratoByIdFascicolo(aPenaResidua.getFasSieIdFascicoloSiep(), "E", "S");

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);

			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleArrestoController.ExRegistraPenaVerbaleArresto: " + ex);
		} catch (Exception ex) {
			rollback(lConn);

			siesLogger.debug("Exception: " + ex);
			throw new F3BException("VerbaleArrestoController.ExRegistraPenaVerbaleArresto: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lLicSqlDao);

			cleanup(lConn);
		}
	}

	public VerbaleModel ExInserisciVerbaleSottoscrizione(BigDecimal aKeyFasc, VerbaleModel aVerbale)
			throws F3BException {
		Connection lConn = null;

		VerbaleDAO lVerDao = null;
		VerbaleModel lVerMod = null;
		MisuraAlternativaDAO lMisAltDao = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = null;
		EventoSqlDAO lEveDao = null;
		EventoModel lEveMod = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSql = null;
		LuogoDetenzioneDAO lLuogoDao = null;
		LuogoDetenzioneSqlDAO lLuogoSql = null;
		EventoDAO lEventoDao = null;
		UfficioSqlDAO lUffSqlDao = null;
		// 25012019 - PROBLEMA DI RIDETERMINAZIONE PENA (Errore Siep Rideterminazione pena Altro -
		// SEGNALAZIONE URGENTE email Maffucci 18/01/2019)
		FascicoloSiepDAO lFascDao = null;

		try {
			lConn = getDBTransaction();

			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lVerDao = new VerbaleDAO(lConn);
			lMisAltDao = new MisuraAlternativaDAO(lConn);
			lEveDao = new EventoSqlDAO(lConn);
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lLuogoDao = new LuogoDetenzioneDAO(lConn);
			lLuogoSql = new LuogoDetenzioneSqlDAO(lConn);
			lPosSql = new PosizioneGiuridicaSqlDAO(lConn);
			lUffSqlDao = new UfficioSqlDAO(lConn);
			// 25012019 - PROBLEMA DI RIDETERMINAZIONE PENA (Errore Siep Rideterminazione pena Altro -
			// SEGNALAZIONE URGENTE email Maffucci 18/01/2019)
			lFascDao = new FascicoloSiepDAO(lConn);

			/*
			 * modifica 13-02-06 - effettuata da Dario -- Richiesta da Viviana si cerca l'ultima concessione
			 * per usarla nella registrazione data inizio misura!!!
			 */
			String[] Natura = { "CO", "DD" };
			String[] TipoMisura = null;
			String[] Decisione = null;
			lMisDao.ricercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisionePerVerbale(aKeyFasc,
					Decisione, Natura, TipoMisura);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();
			BigDecimal lKeyEvento = null;
			if (lMisMod != null && lMisMod.getEveIdEvento() != null) {
				lEveDao.ricercaEventoByKey(lMisMod.getEveIdEvento());
				lEveMod = (EventoModel) lEveDao.getModelByKey();
			}

			if (lEveMod != null && lEveMod.getCodMotivo() != null && lEveMod.getCodTipoProvvedimento() != null
					&& (lEveMod.getCodTipoProvvedimento().equals("03") // Ordinanza
							|| lEveMod.getCodTipoProvvedimento().equals("02")) // Decreto
					&& (lEveMod.getCodMotivo().equals("0001") || lEveMod.getCodMotivo().equals("0002")
							|| lEveMod.getCodMotivo().equals("0003") || lEveMod.getCodMotivo().equals("0004")
							|| lEveMod.getCodMotivo().equals("0005") || lEveMod.getCodMotivo().equals("0010")
							|| lEveMod.getCodMotivo().equals("0013") || lEveMod.getCodMotivo().equals("2245")
							|| lEveMod.getCodMotivo().equals("2005") // Ammissione provvisoria a detenzione
																		// domiciliare
							|| lEveMod.getCodMotivo().equals("2006") // Ammissione provvisoria ad Affidamento
																		// in Prova Affidamento Terapeutico
							|| lEveMod.getCodMotivo().equals("2008") // Ammissione provvisoria ad Affidamento
																		// in Prova DL 146 2013 (since
																		// 24/01/2014)
							|| lEveMod.getCodMotivo().equals("2630") // 29/09/2010 Espiazione Pena presso
																		// Domicilio
							|| lEveMod.getCodMotivo().equals("0011")
							// 20191120 [SG]: aggiunto codice per gestione ticket
							// Ticket#20191114019 — SIES - mancata registrazione data inizio misura
							// Ticket#20191112019 — 2019/11 Ancona Procura Minori non fa caricare inizio
							// misura Esecuzione presso domicilio della pena detentiva ( TdS )
							|| lEveMod.getCodMotivo().equals("0610"))) {
				// inserisco un evento
				lEventoDao = new EventoDAO(lConn);

				lEventoDao.setCodTipoEvento("07");

				if (lEveMod.getCodMotivo().equals("0005") || lEveMod.getCodMotivo().equals("0010")
						|| lEveMod.getCodMotivo().equals("0013") || lEveMod.getCodMotivo().equals("0004")
						|| lEveMod.getCodMotivo().equals("0011")) {
					lEventoDao.setCodTipoProvvedimento("16");
				} else {
					lEventoDao.setCodTipoProvvedimento("18");
				}

				lEventoDao.setCodMotivo("0314");
				lEventoDao.setFlagDocumentoRegistrato("S");
				lEventoDao.setFlagVideoSiep("S");
				lEventoDao.setFlagStampaSiep("S");

				if (lEveMod.getIdEvento() != null)
					lEventoDao.setEveIdEvento(lEveMod.getIdEvento());

				lEventoDao.setFasSieIdFascicoloSiep(aKeyFasc);
				lEventoDao.setCodOperatoreInserimento(aVerbale.getCodOperatoreInserimento());
				lEventoDao.setCodUfficioInserimento(aVerbale.getCodUfficioInserimento());
				lEventoDao.setDataInserimento(aVerbale.getDataInserimento());

				lEventoDao.setDataEmissione(aVerbale.getDataEmissione());
				lEventoDao.setCodEsito("-");
				lEventoDao.setCodMagistrato("-");
				lEventoDao.setCodLuogoDestinatario("-");
				lEventoDao.setCodTipoUfficioDestinatario("-");

				// cerco la sede dell'ufficio d'inserimento per caricare il luogo emittente
				UfficioModel lUffMod = new UfficioModel();
				if (aVerbale != null) {
					lUffSqlDao.ricercaUfficioByCod(aVerbale.getCodUfficioInserimento());
					lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
				}

				if (lUffMod != null && lUffMod.getCodComune() != null)
					lEventoDao.setCodLuogoEmittente(lUffMod.getCodComune());
				else
					lEventoDao.setCodLuogoEmittente("-");

				lEventoDao.setCodUfficioEmittente(aVerbale.getCodUfficioInserimento());

				lKeyEvento = lEventoDao.insert();
				lEventoDao.stop();

				lVerMod = new VerbaleModel(aVerbale);
				lVerMod.setEveIdEvento(lKeyEvento);
				lVerDao = new VerbaleDAO(lConn);
				lVerDao.setDAOFromModel(lVerMod);
				BigDecimal lKey = null;
				lKey = lVerDao.insert();
				lVerDao.stop();

				// Aggiorna la data inizio Misura
				lMisMod.setDataInizioMisura(aVerbale.getDataEmissione());
				lMisMod.setDataAggiornamento(DateUtils.getSysDate());
				lMisMod.setCodOperatoreAggiornamento(lVerMod.getCodOperatoreInserimento());
				lMisMod.setCodUfficioAggiornamento(lVerMod.getCodUfficioInserimento());
				lMisAltDao.setDAOFromModelForUpdate(lMisMod);

				lMisAltDao.update();
				lMisAltDao.stop();

				lVerMod.setIdVerbale(lKey);

			} else {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non esiste l'Ordinanza/Decreto del TDS/MDS.");
			}

			// =============================================
			// Modifica Posizione o Inserimento Giuridica
			// =============================================
			PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
			lPosSql.ricercaPosGiuCorrenteByIdFascicolo(aKeyFasc);
			lPos = (PosizioneGiuridicaModel) lPosSql.getModelByKey();

			if (lPos != null && lPos.getCodPosizioneGiuridica() != null && (lPos.isLibero())) {
				if (lPos.getDataFine() == null) {

					// update
					lPosDao.setIdPosizioneGiuridica(lPos.getIdPosizioneGiuridica());
					lPosDao.setCodOperatoreAggiornamento(lVerMod.getCodOperatoreInserimento());
					lPosDao.setCodUfficioAggiornamento(lVerMod.getCodUfficioInserimento());
					lPosDao.setDataAggiornamento(DateUtils.getSysDate());
					lPosDao.setDataFine(lVerMod.getDataEmissione());

					lPosDao.selByKey();
					lPosDao.update();
					lPosDao.stop();
				}

				if (lEveMod.getCodMotivo().equals("0001") || lEveMod.getCodMotivo().equals("0002")
						|| lEveMod.getCodMotivo().equals("0003")) {
					lPosDao.setCodPosizioneGiuridica("13");
				}
				if (lEveMod.getCodMotivo().equals("0004")) {
					lPosDao.setCodPosizioneGiuridica("14");
				}
				if (lEveMod.getCodMotivo().equals("0005") || lEveMod.getCodMotivo().equals("0010")
						|| lEveMod.getCodMotivo().equals("0013") || lEveMod.getCodMotivo().equals("0011")) {
					lPosDao.setCodPosizioneGiuridica("12");
				}
				if (lEveMod.getCodMotivo().equals("2245")) {
					lPosDao.setCodPosizioneGiuridica("27");
				}
				// 29/09/2010 Espiazione Pena presso Domicilio.
				if (lEveMod.getCodMotivo().equals("2630")) {
					lPosDao.setCodPosizioneGiuridica("50");
				}
				if (lEveMod.getCodMotivo().equals("2005")) {
					lPosDao.setCodPosizioneGiuridica("29"); // Detenzione domiciliare provvisoria
				}
				if (lEveMod.getCodMotivo().equals("2006") || lEveMod.getCodMotivo().equals("2008")) {
					lPosDao.setCodPosizioneGiuridica("54"); // AFFIDAMENTO in prova ammiss provvisoria
				}
				// 20191120 [SG]: aggiunto codice per gestione ticket
				// Ticket#20191114019 — SIES - mancata registrazione data inizio misura
				// Ticket#20191112019 — 2019/11 Ancona Procura Minori non fa caricare inizio
				// misura Esecuzione presso domicilio della pena detentiva ( TdS )
				if (lEveMod.getCodMotivo().equals("0610")) {
					lPosDao.setCodPosizioneGiuridica("50");
				}

				// insert
				if (lKeyEvento != null)
					lPosDao.setIdEventoRiferimento(lKeyEvento);

				lPosDao.setFasSieIdFascicoloSiep(aKeyFasc);
				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setCodOperatoreInserimento(lVerMod.getCodOperatoreInserimento());
				lPosDao.setCodUfficioInserimento(lVerMod.getCodUfficioInserimento());
				lPosDao.setDataInserimento(DateUtils.getSysDate());
				lPosDao.setDataInizio(lVerMod.getDataEmissione());

				// lPosDao.setDAOFromModel(lPos);
				BigDecimal lKeyPosGiu = null;
				lKeyPosGiu = lPosDao.insert();
				lPosDao.stop();

				// 25012019 - PROBLEMA DI RIDETERMINAZIONE PENA (Errore Siep Rideterminazione pena Altro -
				// SEGNALAZIONE URGENTE email Maffucci 18/01/2019)
				// aggiorno il FLAG_ALTRA_CAUSA = 'N' sulla tabella FASCICOLO_SIEP
				lFascDao.selCondizioneUpdate(aKeyFasc);
				lFascDao.setFlagAltraCausa("N");
				lFascDao.setCodUfficioAggiornamento(lVerMod.getCodUfficioInserimento());
				lFascDao.setCodOperatoreAggiornamento(lVerMod.getCodOperatoreInserimento());
				lFascDao.setDataAggiornamento(DateUtils.getSysDate());
				lFascDao.update();
				lFascDao.stop();

				// =========================================================================
				//
				// =========================================================================
				if (lVerMod != null && lVerMod.getIstDetIdIstitutoDetenzione() != null
						&& !lVerMod.getIstDetIdIstitutoDetenzione().equals("-")) {
					LuogoDetenzioneModel lLuoDet = null;
					lLuogoSql.ricercaLuogoDetenzioneCorrenteByFascicoloSiep(aKeyFasc);
					lLuoDet = (LuogoDetenzioneModel) lLuogoSql.getModelByKey();

					if (lLuoDet != null && lLuoDet.getIdLuogoDetenzione() != null) {
						lLuogoDao.setCondizioneUpdate(lLuoDet.getIdLuogoDetenzione());
						lLuogoDao.setCodOperatoreAggiornamento(lVerMod.getCodOperatoreInserimento());
						lLuogoDao.setCodUfficioAggiornamento(lVerMod.getCodUfficioInserimento());
						lLuogoDao.setDataAggiornamento(DateUtils.getSysDate());
						lLuogoDao.setDataFineDetenzione(DateUtils.getSysDate());
						lLuogoDao.update();
						lLuogoDao.stop();

					}

					lLuogoDao.setIstDetIdIstitutoDetenzione(lVerMod.getIstDetIdIstitutoDetenzione());
					lLuogoDao.setCodUfficioInserimento(lVerMod.getCodUfficioInserimento());
					lLuogoDao.setDataInserimento(DateUtils.getSysDate());
					lLuogoDao.setCodOperatoreInserimento(lVerMod.getCodOperatoreInserimento());
					lLuogoDao.setFasSieIdFascicoloSiep(aKeyFasc);
					lLuogoDao.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);
					lLuogoDao.setDataInizioDetenzione(DateUtils.getSysDate());
					lLuogoDao.insert();
					lLuogoDao.stop();
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleController.ExInserisciVerbaleSottoscrizione: " + ex);
		} catch (F3BException fex) {
			rollback(lConn);

			fex.printStackTrace();

			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				throw fex;
			} else {
				throw new F3BException("VerbaleController.ExInserisciVerbaleSottoscrizione: " + fex);
			}
		}

		catch (Exception es) {
			rollback(lConn);
			es.printStackTrace();
			siesLogger.debug("SQLException: " + es);
			throw new F3BException("VerbaleController.ExInserisciVerbaleSottoscrizione: " + es);
		} finally {
			cleanup(lVerDao);
			cleanup(lMisAltDao);
			cleanup(lMisDao);
			cleanup(lEveDao);
			cleanup(lPosDao);
			cleanup(lPosSql);
			cleanup(lLuogoDao);
			cleanup(lLuogoSql);
			cleanup(lEventoDao);
			cleanup(lUffSqlDao);

			cleanup(lConn);
		}

		return lVerMod;
	}

	/**
	 * Inserisce il verbale vane ricerche
	 * 
	 * @param aKeyFasc
	 * @param aVerbale
	 * @param aNotMod
	 * @param aScaMod
	 * @return
	 * @throws F3BException
	 */
	public VerbaleModel ExInserisciVerbaleVaneRicerche(BigDecimal aKeyFasc, VerbaleModel aVerbale,
			NotificaModel aNotMod, ScadenzarioModel aScaMod, EventoNotificaModel aEveNot)
			throws F3BException {
		Connection lConn = null;

		VerbaleDAO lVerDao = null;
		StatoProcedimentoDAO lStaProDao = null;
		ScadenzarioDAO lScaDao = null;
		EventoDAO lEveDao = null;
		EventoVerbaleSqlDAO lEveVerSql = null;

		VerbaleModel lVerMod = null;
		EventoNotificaModel lEveNot = null;

		try {
			lConn = getDBTransaction();

			// ricerco
			if (aEveNot != null && aEveNot.getEvento() != null && aEveNot.getEvento().getIdEvento() != null) {
				// se provengo dalle omesse notifica
				lEveNot = aEveNot;
			} else {
				// lEveNot = ExRicercaEventoVerbale(aKeyFasc, "Nessun Documento registrato in relazione al
				// verbale d'arresto");
				lEveNot = ExRicercaEventoOE(aKeyFasc,
						"Nessun Documento registrato in relazione al verbale d'arresto");
			}

			// inserisco un evento
			lEveDao = new EventoDAO(lConn);

			lEveDao.setCodTipoEvento("07");
			lEveDao.setCodTipoProvvedimento("17");
			lEveDao.setCodMotivo("0313");

			lEveDao.setFlagDocumentoRegistrato("S");
			lEveDao.setFlagVideoSiep("S");
			lEveDao.setFlagStampaSiep("S");

			if (lEveNot.getEvento() != null) {
				lEveDao.setEveIdEvento(lEveNot.getEvento().getIdEvento());
			}

			lEveDao.setFasSieIdFascicoloSiep(aKeyFasc);
			lEveDao.setCodOperatoreInserimento(aVerbale.getCodOperatoreInserimento());
			lEveDao.setCodUfficioInserimento(aVerbale.getCodUfficioInserimento());
			lEveDao.setDataInserimento(aVerbale.getDataInserimento());

			lEveDao.setDataEmissione(aVerbale.getDataEmissione());
			lEveDao.setCodEsito("-");
			lEveDao.setCodMagistrato("-");
			lEveDao.setCodLuogoDestinatario("-");
			lEveDao.setCodTipoUfficioDestinatario("-");
			if (lEveNot.getEvento() != null && lEveNot.getEvento().getCodLuogoEmittente() != null) {
				lEveDao.setCodLuogoEmittente(lEveNot.getEvento().getCodLuogoEmittente());
			} else {
				lEveDao.setCodLuogoEmittente("-");
			}
			lEveDao.setCodUfficioEmittente(aVerbale.getCodUfficioInserimento());

			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			lVerMod = new VerbaleModel(aVerbale);
			lVerMod.setEveIdEvento(lKeyEvento);
			lVerDao = new VerbaleDAO(lConn);
			lVerDao.setDAOFromModel(lVerMod);
			BigDecimal lKey = null;
			lKey = lVerDao.insert();
			lVerDao.stop();

			// qui altre insert
			/**************************** Aggiorna Notifica ***************************************/
			NotificaDAO lNotDAO = new NotificaDAO(lConn);
			NotificaModel lNotificaMod = new NotificaModel(aNotMod);
			if (lEveNot != null && lEveNot.getNotifiche() != null && lEveNot.getNotifiche().length > 0) {
				lNotDAO.setCodEsito(lNotificaMod.getCodEsito());
				lNotDAO.setCodiceOperatoreAggiornamento(lNotificaMod.getCodiceOperatoreAggiornamento());
				lNotDAO.setCodUfficioAggiornamento(lNotificaMod.getCodUfficioAggiornamento());
				lNotDAO.setDataAggiornamento(lNotificaMod.getDataAggiornamento());
				lNotDAO.setIdNotifica(lEveNot.getNotifiche()[0].getIdNotifica());
				lNotDAO.setCondizioneUpdate(lEveNot.getNotifiche()[0].getIdNotifica());
				lNotDAO.update();
				lNotDAO.stop();
			}

			/***************************** Fine Aggiorna Notifica ********************************/
			/***************************** Scadenzario *******************************************/
			// PARAMETRO
			ParametroModel lParMod = new ParametroModel();

			lParMod.setNomeParametro("VANE RICERCHE PERVENUTO");
			// UFFICIO CONNESSO
			lParMod.setCodUfficioValidita(lVerMod.getCodUfficioInserimento());

			Vector lVectPar = null;
			IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
			lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

			Iterator lIterPar = lVectPar.iterator();

			Date lSommaAnni = null;
			Date lSommaMesi = null;
			Date lFineScadenza = null;

			while (lIterPar.hasNext()) {
				ParametroModel lParModel = (ParametroModel) lIterPar.next();
				lSommaAnni = DateUtils.moveDateTo(lVerMod.getDataEmissione(), java.util.Calendar.YEAR,
						lParModel.getAnni().intValue());
				lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
						lParModel.getMesi().intValue());
				lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
						lParModel.getGiorni().intValue());
			}

			// SCADENZARIO
			IScadenzario lCtrlSca = SIEPLookupRemote.getScadenzarioRemote();
			Vector lVect = lCtrlSca.ExRicercaScadenzarioVerbaleArresto(aScaMod);

			// l'iterazione potrebbe essere spostata all'interno del metodo del controller di Scadenzario
			Iterator lIterSca = lVect.iterator();

			while (lIterSca.hasNext()) {
				ScadenzarioModel lScadmodel = (ScadenzarioModel) lIterSca.next();
				lScaDao = new ScadenzarioDAO(lConn);
				lScadmodel.setDataFineScadenza(lFineScadenza);
				// AMBROSINO
				lScadmodel.setCodStatoNotifica("PP");
				// --
				lScaDao.setDAOFromModelForUpdate(lScadmodel);
				lScaDao.update();
			}
			/**************************** Fine Scadenzario ***************************************/
			/**************************** Stato Procedimento *************************************/
			// RICERCA E PRELEVAMENTO DATA
			// StatoProcedimentoModel lStaProcedimentoMod = new StatoProcedimentoModel();

			/*
			 * IStatoProcedimento lCtrlStatoProc = SIEPLookupRemote.getStatoProcedimentoRemote(); Vector
			 * lVectProcedimento = lCtrlStatoProc.ExRicercaStatoProcedimentoByFascicoloSiepStato(aKeyFasc);
			 *
			 * Date lDataPrelevata = null;
			 *
			 * Iterator lIterStatoPro = lVectProcedimento.iterator();
			 *
			 * while (lIterStatoPro.hasNext()) { lStaProDao = new StatoProcedimentoDAO(lConn);
			 * StatoProcedimentoModel lStaModel = (StatoProcedimentoModel) lIterStatoPro.next();
			 * lDataPrelevata = lStaModel.getData(); }
			 */

			// RICERCA E CANCELLAZIONE
			IStatoProcedimento lCtrlStaProc = SIEPLookupRemote.getStatoProcedimentoRemote();
			Vector lVectPro = lCtrlStaProc.ExRicercaStatoProcedimentoByFascicoloSiep(aKeyFasc);

			Iterator lIterPro = lVectPro.iterator();

			while (lIterPro.hasNext()) {
				lStaProDao = new StatoProcedimentoDAO(lConn);
				StatoProcedimentoModel lStaModel = (StatoProcedimentoModel) lIterPro.next();
				lStaProDao.setCondizioneUpdate(lStaModel);
				lStaProDao.delete();
				lStaProDao.stop();
			}

			// modifica fatta sotto segnalazione di valentina scarpa -- 31-03-2008 -- dario
			// si è aggiunto la revoca simeone ed il provvedimento di cumulo, coma da richiesta!!!!

			String lFlagOE = "N";
			String lFlagRS = "N";
			String lFlagCUM = "N";
			Date lDataPrelevata = null;
			lEveVerSql = new EventoVerbaleSqlDAO(lConn);
			lEveVerSql.ricercaEventoOELSRSCUM(aKeyFasc);
			EventoModel lEveModOELSRSCUM = (EventoModel) lEveVerSql.getModelByKey();

			if (lEveModOELSRSCUM != null) {
				if (lEveModOELSRSCUM != null && (lEveModOELSRSCUM.getCodMotivo().equals("0057"))
						|| lEveModOELSRSCUM.getCodMotivo().equals("0058")
						|| lEveModOELSRSCUM.getCodMotivo().equals("0059")
						|| lEveModOELSRSCUM.getCodMotivo().equals("0060")
						|| lEveModOELSRSCUM.getCodMotivo().equals("0246")) {
					lFlagOE = "S";
				} else if (lEveModOELSRSCUM != null && (lEveModOELSRSCUM.getCodMotivo().equals("0078"))
						|| lEveModOELSRSCUM.getCodMotivo().equals("0079")
						|| lEveModOELSRSCUM.getCodMotivo().equals("0080")) {
					lFlagRS = "S";
				} else if (lEveModOELSRSCUM != null && (lEveModOELSRSCUM.getCodMotivo().equals("0222"))
						|| lEveModOELSRSCUM.getCodMotivo().equals("0223")
						|| lEveModOELSRSCUM.getCodMotivo().equals("0224")
						|| lEveModOELSRSCUM.getCodMotivo().equals("0277")
						|| lEveModOELSRSCUM.getCodMotivo().equals("0225"))// 0225 per gestire un'eventuale
																			// pregresso
				{
					lFlagCUM = "S";
				}
				lDataPrelevata = lEveModOELSRSCUM.getDataEmissione();

			}

			// insert
			lStaProDao = new StatoProcedimentoDAO(lConn);
			StatoProcedimentoModel lStaProMod = new StatoProcedimentoModel();

			for (int ins = 1; ins < 3; ins++) {
				lStaProMod.setCodOperatoreInserimento(lVerMod.getCodOperatoreInserimento());
				lStaProMod.setProgressivo(new BigDecimal(ins));
				if (ins == 1) {

					if (lFlagOE.equals("S"))
						lStaProMod.setCodStatoProcedimento("0057");// o.e
					else if (lFlagRS.equals("S"))
						lStaProMod.setCodStatoProcedimento("0120"); // r.s.
					else if (lFlagCUM.equals("S"))
						lStaProMod.setCodStatoProcedimento("0057"); // cumulo -- stesso stato di ordine
																	// esecuzione
					else
						lStaProMod.setCodStatoProcedimento("0011"); // o.e.s.

					lStaProMod.setData(lDataPrelevata);
				} else {
					lStaProMod.setCodStatoProcedimento("0002");// vane ricerche
					lStaProMod.setData(lVerMod.getDataEmissione());
				}
				lStaProMod.setDataInserimento(lVerMod.getDataInserimento());
				lStaProMod.setCodUfficioInserimento(lVerMod.getCodUfficioInserimento());
				lStaProMod.setFasSieIdFascicoloSiep(aKeyFasc);

				lStaProDao.setDAOFromModel(lStaProMod);
				lStaProDao.insert();
				lStaProDao.stop();
			}

			/**************************** Fine Stato Procedimento ********************************/

			commit(lConn);
			lVerMod.setIdVerbale(lKey);

		} catch (DAOException ex) {
			rollback(lConn);

			ex.printStackTrace();

			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleController.ExInserisciVerbaleVaneRicerche: " + ex);
		} catch (F3BException fex) {
			rollback(lConn);

			fex.printStackTrace();

			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				throw fex;
			} else {
				throw new F3BException("VerbaleController.ExInserisciVerbaleVaneRicerche: " + fex);
			}
		}

		catch (Exception es) {
			rollback(lConn);

			es.printStackTrace();

			siesLogger.debug("SQLException: " + es);

			throw new F3BException("VerbaleController.ExInserisciVerbale: " + es);
		} finally {
			cleanup(lVerDao);
			cleanup(lStaProDao);
			cleanup(lScaDao);
			cleanup(lEveDao);
			cleanup(lEveVerSql);

			cleanup(lConn);
		}

		return lVerMod;
	}

	public Vector ExRicercaVerbale(VerbaleModel aVerbale) throws F3BException {
		Connection lConn = null;
		Vector lVerbali = new Vector();
		VerbaleSqlDAO lVerDao = null;

		try {
			lConn = getDBConnection();
			lVerDao = new VerbaleSqlDAO(lConn);
			lVerDao.ricercaVerbale(aVerbale);
			lVerbali = new Vector(lVerDao.getModels());
			if (lVerbali.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("VerbaleController.ExRicercaVerbale: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lVerDao);
			cleanup(lConn);
		}
		return lVerbali;
	}

	public VerbaleModel ExRicercaVerbaleByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		VerbaleSqlDAO lVerDao = null;
		VerbaleModel lVerMod;

		try {
			lConn = getDBConnection();
			lVerDao = new VerbaleSqlDAO(lConn);
			lVerDao.ricercaVerbaleByKey(aKey);
			lVerMod = (VerbaleModel) lVerDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("VerbaleController.ExRicercaVerbale: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lVerDao);
			cleanup(lConn);
		}
		return lVerMod;
	}

	public VerbaleModel ExRicercaVerbaleObblighiByIdEvento(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		VerbaleSqlDAO lVerDao = null;
		VerbaleModel lVerMod;
		try {
			lConn = getDBConnection();
			lVerDao = new VerbaleSqlDAO(lConn);
			lVerDao.ricercaVerbaleObblighiByIdEvento(aKey);
			lVerMod = (VerbaleModel) lVerDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"VerbaleController.ExRicercaVerbaleObblighiByIdEvento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lVerDao);
			cleanup(lConn);
		}
		return lVerMod;
	}

	public VerbaleModel ExModificaVerbale(VerbaleModel aVerbale) throws F3BException {
		Connection lConn = null;
		VerbaleDAO lVerDao = null;
		VerbaleModel lVerMod = new VerbaleModel(aVerbale);

		try {
			lConn = getDBConnection();

			lVerDao = new VerbaleDAO(lConn);
			lVerDao.setDAOFromModelForUpdate(aVerbale);
			lVerDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lVerDao);
			cleanup(lConn);
		}
		return lVerMod;
	}

	/**
	 * Effettua l'aggiornamento del record Pena_residua nel caso di ricalcolo legato a un verbale di
	 * sottoscrizione. Se il record Pena residua è già validato, ne inserisce uno nuovo, altrimenti lo
	 * aggiorna. Aggiorna tutte le LA a sistema con flag da N o null a E.
	 * 
	 */
	public PenaResiduaModel ExAggiornaPenaVerbale(PenaResiduaModel aPenaResidua) throws F3BException {
		Connection lConn = null;

		PenaResiduaDAO lPenDao = null;
		LicenzaLibanticipataSqlDAO lLibAntSqlDao = null;

		PenaResiduaModel lPenMod = null;

		try {
			lConn = getDBTransaction();

			lPenDao = new PenaResiduaDAO(lConn);

			// aggiorna usando lo stesso model che viene passato
			if (aPenaResidua.getFlagValidato().equals("N")) {
				lPenDao.setDAOFromModelForUpdate(aPenaResidua);
				lPenDao.update();
				lPenMod = aPenaResidua;
			} else if (aPenaResidua.getFlagValidato().equals("S")) // fine aggiornamento inizio inserimento
			{
				aPenaResidua.setFlagValidato("N");
				lPenDao.setDAOFromModel(aPenaResidua);
				BigDecimal lKey = null;
				lKey = lPenDao.insert();
				lPenMod = new PenaResiduaModel(aPenaResidua);
				lPenMod.setIdPenaResidua(lKey);
			}
			// fine inserimento

			// Aggiorna le LA
			lLibAntSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLibAntSqlDao.updateFlagElaboratoByIdFascicolo(aPenaResidua.getFasSieIdFascicoloSiep(), "N", "E");

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleController.ExAggiornaPenaVerbale: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lLibAntSqlDao);

			cleanup(lConn);
		}

		return lPenMod;
	}

	public void ExCancellaVerbale(VerbaleModel aVerbale, Connection lConn) throws F3BException {
		// Connection lConn = null;
		VerbaleDAO lVerDao = null;

		try {
			// lConn = getDBConnection();
			lVerDao = new VerbaleDAO(lConn);
			lVerDao.setCondizioneUpdate(aVerbale.getIdVerbale());
			lVerDao.delete();
			// commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("VerbaleController.ExCancellaVerbale: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lVerDao);
			// cleanup(lConn);
		}
	}

	public EventoNotificaModel ExRicercaEventoVerbale(BigDecimal aKeyFasc, String userMsg)
			throws F3BException {
		Connection lConn = null;

		NotificaSqlDAO lNotDao = null;
		EventoVerbaleSqlDAO lEveVerDao = null;

		EventoModel lEveMod = null;
		NotificaModel lNotMod = null;
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		List lEventi = new ArrayList();
		List lNotifica = new ArrayList();

		try {
			lConn = getDBConnection();

			// lEveMod = new EventoModel();
			lEveVerDao = new EventoVerbaleSqlDAO(lConn);

			lEveVerDao.ricercaEventoVerbale(aKeyFasc);

			lEventi = new Vector(lEveVerDao.getModels());

			if (lEventi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, userMsg);
			} else {
				lEveMod = (EventoModel) lEventi.get(0);

				// notifica
				lNotDao = new NotificaSqlDAO(lConn);
				lNotDao.ricercaNotificaEsecuzioneByEvento(lEveMod.getIdEvento());
				lNotifica = new Vector(lNotDao.getModels());

				if (lNotifica.size() == 0) {
					// ** REWORK 30/09/2004 DL -- NON DEVONO ESSERCI PER FORZA
					// throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Notifica per il Fascicolo
					// Selezionato");
				} else {
					lNotMod = (NotificaModel) lNotifica.get(0);
					NotificaModel[] lNotifiche = new NotificaModel[1];
					lNotifiche[0] = lNotMod;

					lEveNotMod.setEvento(lEveMod);
					lEveNotMod.setNotifiche(lNotifiche);
				}
			}
		} catch (DAOException ex) {
			rollback(lConn);

			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleController.ExRicercaEventoVerbale : " + ex);
		} finally {
			cleanup(lEveVerDao);
			cleanup(lNotDao);

			cleanup(lConn);
		}

		return lEveNotMod;
	}

	// AMBROSINO 07-02-2011

	public EventoNotificaModel ExRicercaEventoOE(BigDecimal aKeyFasc, String userMsg) throws F3BException {
		// siesLogger.debug(" -- -- - AMBROSINO --- ExRicercaEventoOE ");
		Connection lConn = null;

		NotificaSqlDAO lNotDao = null;
		EventoVerbaleSqlDAO lEveVerDao = null;

		EventoModel lEveMod = null;
		NotificaModel lNotMod = null;
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		List lEventi = new ArrayList();
		List lNotifica = new ArrayList();

		try {
			lConn = getDBConnection();

			// lEveMod = new EventoModel();
			lEveVerDao = new EventoVerbaleSqlDAO(lConn);

			lEveVerDao.ricercaEventoOE(aKeyFasc);

			lEventi = new Vector(lEveVerDao.getModels());

			if (lEventi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, userMsg);
			} else {
				lEveMod = (EventoModel) lEventi.get(0);

				// notifica
				lNotDao = new NotificaSqlDAO(lConn);
				lNotDao.ricercaNotificaEsecuzioneByEvento(lEveMod.getIdEvento());
				lNotifica = new Vector(lNotDao.getModels());

				if (lNotifica.size() == 0) {
					// ** REWORK 30/09/2004 DL -- NON DEVONO ESSERCI PER FORZA
					// throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Notifica per il Fascicolo
					// Selezionato");
				} else {
					lNotMod = (NotificaModel) lNotifica.get(0);
					NotificaModel[] lNotifiche = new NotificaModel[1];
					lNotifiche[0] = lNotMod;

					lEveNotMod.setNotifiche(lNotifiche);
				}
			}
			lEveNotMod.setEvento(lEveMod);
		} catch (DAOException ex) {
			rollback(lConn);

			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleController.ExRicercaEventoVerbale : " + ex);
		} finally {
			cleanup(lEveVerDao);
			cleanup(lNotDao);

			cleanup(lConn);
		}

		return lEveNotMod;
	}

	// END AMBROSINO 07-02-2011

	/**
	 * Ricerca un Verbale Generico collegato all'evento passato in input
	 *
	 * @param aIdEvento
	 * @return
	 * @throws F3BException
	 */
	public VerbaleModel ExRicercaVerbaleByIdEvento(BigDecimal aIdEvento) throws F3BException {
		Connection lConn = null;
		VerbaleModel lVerbMod = null;
		VerbaleSqlDAO lVerDao = null;
		try {
			lConn = getDBConnection();
			lVerDao = new VerbaleSqlDAO(lConn);
			lVerbMod = new VerbaleModel();

			lVerDao.ricercaVerbaleByIdEvento(aIdEvento);
			lVerbMod = (VerbaleModel) lVerDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"VerbaleController.ExRicercaVerbaleByIdEvento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lVerDao);
			cleanup(lConn);
		}
		return lVerbMod;
	}

	// ricerca ExRicercaVerbale ByIdEvento per codtipo
	public VerbaleModel ExRicercaVerbaleByCodTipoIdEvento(BigDecimal aKey, String aCodTipo)
			throws F3BException {
		Connection lConn = null;

		VerbaleSqlDAO lVerDao = null;

		VerbaleModel lVerbMod = null;

		try {
			lConn = getDBConnection();

			lVerDao = new VerbaleSqlDAO(lConn);
			lVerbMod = new VerbaleModel();

			if (aKey != null) {
				lVerDao.ricercaVerbaleByCodTipoIdEvento(aKey, aCodTipo);
				lVerbMod = (VerbaleModel) lVerDao.getModelByKey();
			}
		} catch (DAOException daoEx) {
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("VerbaleController.ExRicercaVerbaleByCodTipoIdEvento: " + daoEx);
		} finally {
			cleanup(lVerDao);

			cleanup(lConn);
		}

		return lVerbMod;
	}

	// ricerca ExRicercaVerbale IdFascicolo per cod tipo = 03
	public VerbaleModel ExRicercaVerbaleByIdFascicolo(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		VerbaleModel lVerbMod = null;
		VerbaleSqlDAO lVerDao = null;
		try {
			lConn = getDBConnection();
			lVerDao = new VerbaleSqlDAO(lConn);
			lVerbMod = new VerbaleModel();

			if (aKey != null) {
				lVerDao.ricercaVerbaleByIdFascicolo(aKey);
				lVerbMod = (VerbaleModel) lVerDao.getModelByKey();
			}

		} catch (DAOException daoEx) {
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"VerbaleController.ExRicercaVerbaleByIdFascicolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lVerDao);
			cleanup(lConn);
		}
		return lVerbMod;
	}

	// Inserisce i dati per la misura alternativa
	public void ExCancellaDataInizioMisuraAlternativa(BigDecimal aKeyFasc, VerbaleModel aVerbale)
			throws F3BException {

		Connection lConn = null;
		FascicoloSiusSqlDAO lFasDao = null;
		FascicoloSiusModel lFasMod = null;
		EsecuzioneMisuraAlternativaDAO lEseDao = null;
		ScadenzarioSiusDAO lScaDao = null;

		try {
			lConn = getDBTransaction();
			ExCancellaVerbale(aVerbale, lConn);
			lFasDao = new FascicoloSiusSqlDAO(lConn);
			lFasDao.getOrigineCodOggetto(aKeyFasc, "U004");
			lFasMod = (FascicoloSiusModel) lFasDao.getModelByKey();

			if (lFasMod == null)
				siesLogger.debug("************************************* non trovato ");
			else {
				// Preleva i dati della misura alternativa
				IEsecuzioneMA lEMACtrl = SIUSLookupRemote.getEsecuzioneMARemote();
				EsecuzioneMisuraAlternativaModel lEMAModel = lEMACtrl
						.ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo(lFasMod.getIdFascicoloSius());
				if (lEMAModel != null && lEMAModel.getIdEsecuzioneMisuraAlternati() != null) {

					lEseDao = new EsecuzioneMisuraAlternativaDAO(lConn);
					lEMAModel.setCodUfficioAggiornamento(aVerbale.getCodUfficioInserimento());
					lEMAModel.setCodOperatoreAggiornamento(aVerbale.getCodOperatoreInserimento());
					lEMAModel.setDataAggiornamento(DateUtils.getSysDate());
					lEMAModel.setDataInizioMisura(null);
					lEseDao.setDAOFromModel(lEMAModel);
					lEseDao.setCondizioneUpdate(lEMAModel.getIdEsecuzioneMisuraAlternati());
					lEseDao.update();
					lEseDao.stop();
				}
				lScaDao = new ScadenzarioSiusDAO(lConn);
				lScaDao.setCodUfficioAggiornamento(aVerbale.getCodUfficioInserimento());
				lScaDao.setCodOperatoreAggiornamento(aVerbale.getCodOperatoreInserimento());
				lScaDao.setDataAggiornamento(DateUtils.getSysDate());
				lScaDao.setDataFineScadenza(null);
				lScaDao.setCondizioniByIdFascicoloTipo(aKeyFasc, "70");
				lScaDao.update();
				lScaDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException(
					"VerbaleController.ExCancellaDataInizioMisuraAlternativa: Non posso inserire: " + ex);
		} finally {
			cleanup(lScaDao);
			cleanup(lFasDao); // sca

			cleanup(lEseDao);
			cleanup(lConn);
		}
		return;
	}

	// Inserisce i dati per la misura alternativa
	public VerbaleModel ExInserisciDataInizioMisuraAlternativa(BigDecimal aKeyFasc, VerbaleModel aVerbale)
			throws F3BException {
		Connection lConn = null;
		VerbaleDAO lVerDao = null;
		VerbaleModel lVerMod = new VerbaleModel(aVerbale);
		FascicoloSiusSqlDAO lFasDao = null;
		FascicoloSiusModel lFasMod = null;
		EsecuzioneMisuraAlternativaDAO lEseDao = null;
		ScadenzarioSiusDAO lScaDao = null;

		try {
			lConn = getDBTransaction();
			lVerDao = new VerbaleDAO(lConn);
			lVerDao.setDAOFromModel(lVerMod);

			// BigDecimal lKey = null;
			/* lKey = */lVerDao.insert();
			lVerDao.stop();

			lFasDao = new FascicoloSiusSqlDAO(lConn);
			lFasDao.getOrigineCodOggetto(aKeyFasc, "U004");
			lFasMod = (FascicoloSiusModel) lFasDao.getModelByKey();

			if (lFasMod == null)
				siesLogger.debug("************************************* non trovato ");
			else {
				// Preleva i dati della misura alternativa
				IEsecuzioneMA lEMACtrl = SIUSLookupRemote.getEsecuzioneMARemote();
				EsecuzioneMisuraAlternativaModel lEMAModel = lEMACtrl
						.ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo(lFasMod.getIdFascicoloSius());

				if (lEMAModel != null && lEMAModel.getIdEsecuzioneMisuraAlternati() != null) {

					lEseDao = new EsecuzioneMisuraAlternativaDAO(lConn);
					lEMAModel.setCodUfficioAggiornamento(aVerbale.getCodUfficioInserimento());
					lEMAModel.setCodOperatoreAggiornamento(aVerbale.getCodOperatoreInserimento());
					lEMAModel.setDataAggiornamento(DateUtils.getSysDate());
					lEMAModel.setDataInizioMisura(aVerbale.getDataEmissione());
					lEseDao.setDAOFromModel(lEMAModel);
					lEseDao.setCondizioneUpdate(lEMAModel.getIdEsecuzioneMisuraAlternati());
					lEseDao.update();
					lEseDao.stop();
					siesLogger.debug("*********** 11111111111111111111 trovata!!!"
							+ lEMAModel.getIdEsecuzioneMisuraAlternati().toString());
				} else {
					siesLogger.debug("*********** Errore Esecuzione misura alternartiva non trovata!!!");
				}

				lScaDao = new ScadenzarioSiusDAO(lConn);
				lScaDao.setCodUfficioAggiornamento(aVerbale.getCodUfficioInserimento());
				lScaDao.setCodOperatoreAggiornamento(aVerbale.getCodOperatoreInserimento());
				lScaDao.setDataAggiornamento(DateUtils.getSysDate());
				lScaDao.setDataFineScadenza(aVerbale.getDataEmissione());
				lScaDao.setCondizioniByIdFascicoloTipo(aKeyFasc, "70");
				lScaDao.update();
				lScaDao.stop();

			}
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException(
					"VerbaleController.ExInserisciDataInizioMisuraAlternativa: Non posso inserire: " + ex);
		} finally {
			cleanup(lScaDao);
			cleanup(lEseDao);
			cleanup(lVerDao);
			cleanup(lFasDao);

			cleanup(lConn);
		}
		return lVerMod;
	}

	// Inserisce i dati per la misura alternativa
	public VerbaleModel ExInserisciDataInizioMisuraAlternativaUDS(BigDecimal aKeyFasc, VerbaleModel aVerbale)
			throws F3BException {
		Connection lConn = null;
		VerbaleDAO lVerDao = null;
		VerbaleModel lVerMod = new VerbaleModel(aVerbale);
		FascicoloSiusSqlDAO lFasDao = null;
		// FascicoloSiusModel lFasMod = null;
		EsecuzioneMisuraAlternativaDAO lEseDao = null;
		ScadenzarioSiusDAO lScaDao = null;

		try {
			lConn = getDBTransaction();
			lVerDao = new VerbaleDAO(lConn);
			lVerDao.setDAOFromModel(lVerMod);

			// BigDecimal lKey = null;
			/* lKey = */lVerDao.insert();
			lVerDao.stop();

			// Preleva i dati della misura alternativa
			IEsecuzioneMA lEMACtrl = SIUSLookupRemote.getEsecuzioneMARemote();
			EsecuzioneMisuraAlternativaModel lEMAModel = lEMACtrl
					.ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo(aKeyFasc);

			if (lEMAModel != null && lEMAModel.getIdEsecuzioneMisuraAlternati() != null) {
				lEseDao = new EsecuzioneMisuraAlternativaDAO(lConn);
				lEMAModel.setCodUfficioAggiornamento(aVerbale.getCodUfficioInserimento());
				lEMAModel.setCodOperatoreAggiornamento(aVerbale.getCodOperatoreInserimento());
				lEMAModel.setDataAggiornamento(DateUtils.getSysDate());
				lEMAModel.setDataInizioMisura(aVerbale.getDataEmissione());
				lEseDao.setCondizioneUpdate(lEMAModel.getIdEsecuzioneMisuraAlternati());
				lEseDao.setDAOFromModel(lEMAModel);
				lEseDao.update();
				lEseDao.stop();
			} else {
				siesLogger.debug("*********** Errore Esecuzione misura alternartiva non trovata!!!");
			}

			lScaDao = new ScadenzarioSiusDAO(lConn);
			lScaDao.setCodUfficioAggiornamento(aVerbale.getCodUfficioInserimento());
			lScaDao.setCodOperatoreAggiornamento(aVerbale.getCodOperatoreInserimento());
			lScaDao.setDataAggiornamento(DateUtils.getSysDate());
			lScaDao.setDataFineScadenza(aVerbale.getDataEmissione());
			lScaDao.setCondizioniByIdFascicoloTipo(aKeyFasc, "70");
			lScaDao.update();
			lScaDao.stop();

			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException(
					"VerbaleController.ExInserisciDataInizioMisuraAlternativa: Non posso inserire: " + ex);
		} finally {
			cleanup(lScaDao);
			cleanup(lFasDao);
			cleanup(lEseDao);
			cleanup(lVerDao);
			cleanup(lConn);
		}
		return lVerMod;
	}

	/**
	 *
	 * @param aKeyFasc
	 * @param aVerbale
	 * @param aPosMod
	 * @param aLuoDetMod
	 * @return
	 * @throws F3BException
	 */
	public VerbaleModel ExInserisciRipristinoDetCarc(BigDecimal aKeyFasc, VerbaleModel aVerbale,
			PosizioneGiuridicaModel aPosMod, LuogoDetenzioneModel aLuoDetMod) throws F3BException {
		Connection lConn = null;

		VerbaleDAO lVerDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		ScadenzarioDAO lScaDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		StatoProcedimentoDAO lStaProDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		EventoDAO lEveDao = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaDAO lMisDAO = null;
		VerbaleModel lVerMod = null;

		try {
			lConn = getDBTransaction();
			// =========================================
			// aggiorno la misura alternativa con flag_situazione ad N solo se si tratta
			// della datenzione domiciliare a termine-->concessione;proroga;proroga provvisoria
			// =========================================
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDAO = new MisuraAlternativaDAO(lConn);
			lMisDao.ricercaMisuraAlternativaByIdFascicolo(aKeyFasc);
			MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

			if (lMisMod != null
					&& ("0011".equals(lMisMod.getCodTipoMisura()) || "0197".equals(lMisMod.getCodTipoMisura())
							|| "2340".equals(lMisMod.getCodTipoMisura()))) {
				lMisDAO.setDAOFromModelForUpdate(lMisMod);
				lMisDAO.setFlagSituazione("N");
				lMisDAO.update();
				lMisDAO.stop();
			}
			// =========================================
			// Inserisco l'evento relativo al verbale
			// =========================================
			lEveDao = new EventoDAO(lConn);

			lEveDao.setCodTipoEvento("07"); // Verbale
			lEveDao.setCodTipoProvvedimento("16"); // Verbale di arresto
			lEveDao.setCodMotivo("0427"); // Ripristino detenzione in carcere
			lEveDao.setFlagDocumentoRegistrato("S");
			lEveDao.setFlagVideoSiep("S");
			lEveDao.setFlagStampaSiep("S");

			lEveDao.setFasSieIdFascicoloSiep(aKeyFasc);
			lEveDao.setCodOperatoreInserimento(aVerbale.getCodOperatoreInserimento());
			lEveDao.setCodUfficioInserimento(aVerbale.getCodUfficioInserimento());
			lEveDao.setDataInserimento(aVerbale.getDataInserimento());

			lEveDao.setDataEmissione(aVerbale.getDataEmissione());
			lEveDao.setCodEsito("-");
			lEveDao.setCodMagistrato("-");
			lEveDao.setCodLuogoDestinatario("-");
			lEveDao.setCodTipoUfficioDestinatario("-");
			lEveDao.setCodLuogoEmittente("-");
			lEveDao.setCodUfficioEmittente(aVerbale.getCodUfficioInserimento());

			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// ===============================================
			// Inserisco il Verbale
			// ===============================================
			lVerMod = new VerbaleModel(aVerbale);
			lVerMod.setEveIdEvento(lKeyEvento);
			lVerDao = new VerbaleDAO(lConn);
			lVerDao.setDAOFromModel(lVerMod);
			BigDecimal lKey = null;
			lKey = lVerDao.insert();
			lVerDao.stop();

			// ===============================================
			// Cancello lo scadenzario Detenzione Domiciliare a termine
			// ===============================================
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);
			String[] lTipo = { "14" };
			lScaSqlDao.ricercaScadenzarioPerTipoScadenzario(lTipo, aKeyFasc);
			ScadenzarioModel lScaModDet = (ScadenzarioModel) lScaSqlDao.getModelByKey();
			if (lScaModDet != null) {
				lScaDao.setCondizioneDelete(lScaModDet.getIdScadenzario());
				lScaDao.delete();
				lScaDao.stop();
			}

			// ========================================================================
			// Aggiornamento Data_Fine vecchia posizione giuridica e inserimento della
			// nuova
			// ========================================================================
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			BigDecimal lKeyPosGiu = null;
			if (aPosMod != null) {
				PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel(aPosMod);

				BigDecimal lIdFasc = lPosMod.getFasSieIdFascicoloSiep();
				lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lIdFasc);

				PosizioneGiuridicaModel lPosizMod = null;
				lPosizMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

				if (lPosizMod != null) {
					// update
					lPosDao.setIdPosizioneGiuridica(lPosizMod.getIdPosizioneGiuridica());
					lPosDao.setDataFine(lPosMod.getDataInizio());

					lPosDao.setCodOperatoreAggiornamento(lPosMod.getCodOperatoreInserimento());
					lPosDao.setCodUfficioAggiornamento(lPosMod.getCodUfficioInserimento());
					lPosDao.setDataAggiornamento(lPosMod.getDataInserimento());

					lPosDao.selByKey();
					lPosDao.update();
					lPosDao.stop();
				}
				// insert
				if (lKeyEvento != null)
					aPosMod.setIdEventoRiferimento(lKeyEvento);
				lPosDao.setDAOFromModel(aPosMod);
				lKeyPosGiu = lPosDao.insert();
				lPosDao.stop();
			}
			// ===============================================
			// Pena Residua
			// ===============================================
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			PenaResiduaModel lPenResMod = new PenaResiduaModel();

			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aKeyFasc);
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			if (lPenResMod.getEveIdEvento() == null) {
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
				lPenResDao.setEveIdEvento(lKeyEvento);
				lPenResDao.setFlagValidato("S");
				lPenResDao.setCodOperatoreAggiornamento(aVerbale.getCodOperatoreInserimento());
				lPenResDao.setCodUfficioAggiornamento(aVerbale.getCodUfficioInserimento());
				lPenResDao.setDataAggiornamento(DateUtils.getSysDate());
				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				lPenResDao.setDAOFromModel(lPenResMod);
				lPenResDao.setFlagValidato("S");
				lPenResDao.setEveIdEvento(lKeyEvento);
				lPenResDao.setDataInserimento(DateUtils.getSysDate());
				lPenResDao.setCodOperatoreInserimento(aVerbale.getCodOperatoreInserimento());
				lPenResDao.setCodUfficioInserimento(aVerbale.getCodUfficioInserimento());
				lPenResDao.insert();
				lPenResDao.stop();
			}

			// ====================================================
			// Inserimento Luogo di Detenzione
			// ====================================================
			lLuoDetDao = new LuogoDetenzioneDAO(lConn);
			if (lKeyPosGiu != null) {
				aLuoDetMod.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);
				lLuoDetDao.setDAOFromModel(aLuoDetMod);
				// BigDecimal lKeyLuoDet = null;
				/* lKeyLuoDet = */lLuoDetDao.insert();
				lLuoDetDao.stop();
			}

			/**************************** Stato Procedimento *************************************/
			IStatoProcedimento lCtrlStaProc = SIEPLookupRemote.getStatoProcedimentoRemote();
			Vector lVectPro = lCtrlStaProc.ExRicercaStatoProcedimentoByFascicoloSiep(aKeyFasc);

			Iterator lIterPro = lVectPro.iterator();
			lStaProDao = new StatoProcedimentoDAO(lConn);
			while (lIterPro.hasNext()) {
				StatoProcedimentoModel lStaModel = (StatoProcedimentoModel) lIterPro.next();
				lStaProDao.setCondizioneUpdate(lStaModel);
				lStaProDao.delete();
				lStaProDao.stop();
			}

			// insert
			StatoProcedimentoModel lStaProMod = new StatoProcedimentoModel();

			lStaProMod.setCodOperatoreInserimento(lVerMod.getCodOperatoreInserimento());
			lStaProMod.setProgressivo(new BigDecimal(1));
			lStaProMod.setCodStatoProcedimento("0229");
			lStaProMod.setDataInserimento(lVerMod.getDataInserimento());
			lStaProMod.setCodUfficioInserimento(lVerMod.getCodUfficioInserimento());
			lStaProMod.setData(lVerMod.getDataEmissione());
			lStaProMod.setFasSieIdFascicoloSiep(aKeyFasc);
			lStaProDao.setDAOFromModel(lStaProMod);
			lStaProDao.insert();
			lStaProDao.stop();

			/**************************** Fine Stato Procedimento ********************************/

			lVerMod.setIdVerbale(lKey);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleController.ExInserisciRipristinoDetCarc: " + ex);
		} catch (F3BException fex) {
			rollback(lConn);

			fex.printStackTrace();

			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				throw fex;
			} else {
				throw new F3BException("VerbaleController.ExInserisciRipristinoDetCarc: " + fex);
			}
		} catch (Exception es) {
			rollback(lConn);
			es.printStackTrace();
			siesLogger.debug("SQLException: " + es);
			throw new F3BException("VerbaleController.ExInserisciRipristinoDetCarc: " + es);
		} finally {
			cleanup(lVerDao);
			cleanup(lPosSqlDao);
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lStaProDao);
			cleanup(lEveDao);
			cleanup(lScaSqlDao);
			cleanup(lScaDao);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lMisDao);
			cleanup(lMisDAO);

			cleanup(lConn);
		}

		return lVerMod;
	}

	/**
	 * Effettua l'inserimento del Verbale di Notifica scadenza pena altra causa proveniente dal carcere. Viene
	 * inserito un EVENTO verbale, un VERBALE, la PENA_RESIDUA aggiornata
	 * 
	 * @param aKeyFasc
	 * @param aVerbale
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public VerbaleModel ExInserisciNotificaCarcere(BigDecimal aKeyFasc, VerbaleModel aVerbale,
			PenaResiduaModel aPenaResidua) throws F3BException {
		Connection lConn = null;

		VerbaleDAO lVerDao = null;

		PenaResiduaDAO lPenDao = null;
		PenaResiduaSqlDAO lPenResDao = null;

		EventoDAO lEveDao = null;

		VerbaleModel lVerMod = null;

		EventoNotificaModel lEveNot = null;

		AltraCausaSqlDAO lAltraCausaSqlDao = null;
		AltraCausaDAO lAltraCausaDao = null;

		try {
			lConn = getDBTransaction();

			// In realtà il metodo recupera l'ultimo evento validato e restituisce
			// il Model caricato solo se tale evento ha un destinatario per l'esecuzione
			// In questo caso il Verbale viene legato all'evento
			lEveNot = ExRicercaEventoVerbale(aKeyFasc,
					"Nessun Documento registrato in relazione alla Notifica Carcere");

			// =========================================
			// Inserisco l'evento relativo al verbale
			// =========================================
			lEveDao = new EventoDAO(lConn);

			lEveDao.setEveIdEvento(lEveNot.getEvento().getIdEvento());

			lEveDao.setCodTipoEvento("07"); // Verbale
			lEveDao.setCodTipoProvvedimento("16"); // Verbale di arresto
			lEveDao.setCodMotivo("0470"); // Notifica Carcere
			lEveDao.setFlagDocumentoRegistrato("S");
			lEveDao.setFlagVideoSiep("N");
			lEveDao.setFlagStampaSiep("N");

			lEveDao.setFasSieIdFascicoloSiep(aKeyFasc);
			lEveDao.setCodOperatoreInserimento(aVerbale.getCodOperatoreInserimento());
			lEveDao.setCodUfficioInserimento(aVerbale.getCodUfficioInserimento());
			lEveDao.setDataInserimento(aVerbale.getDataInserimento());

			// lEveDao.setDataEmissione(aVerbale.getDataEmissione());
			lEveDao.setCodEsito("-");
			lEveDao.setCodMagistrato("-");
			lEveDao.setCodLuogoDestinatario("-");
			lEveDao.setCodTipoUfficioDestinatario("-");
			lEveDao.setCodLuogoEmittente("-");
			lEveDao.setCodUfficioEmittente(aVerbale.getCodUfficioInserimento());

			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// ===============================================
			// Inserisco il Verbale
			// ===============================================
			lVerMod = new VerbaleModel(aVerbale);
			lVerMod.setEveIdEvento(lKeyEvento);
			lVerDao = new VerbaleDAO(lConn);
			lVerDao.setDAOFromModel(lVerMod);
			BigDecimal lKey = null;
			lKey = lVerDao.insert();
			lVerDao.stop();

			/**************************** Pena Residua *******************************************/
			// ========================================================================
			// Aggiornamento/Inserimento della pena residua con eventuali giorni di
			// Licenza Liberazione Anticipata
			// ========================================================================
			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenDao = new PenaResiduaDAO(lConn);

			aPenaResidua.setEveIdEvento(lKeyEvento);
			aPenaResidua.setFlagPenaSospesa(null);

			// aggiorna usando lo stesso model che viene passato
			if (aPenaResidua.getFlagValidato().equals("N")) {
				// Nel caso di ergastolo la pena viene validata
				if (aPenaResidua.getFlagErgastolo() != null && !aPenaResidua.getFlagErgastolo().equals("N"))
					aPenaResidua.setFlagValidato("S");
				else
					aPenaResidua.setFlagValidato("N");

				lPenDao.setDAOFromModelForUpdate(aPenaResidua);
				lPenDao.update();
			} else if (aPenaResidua.getFlagValidato().equals("S")) // fine aggiornamento inizio inserimento
			{
				// Nel caso di ergastolo la pena viene validata
				if (aPenaResidua.getFlagErgastolo() != null && !aPenaResidua.getFlagErgastolo().equals("N"))
					aPenaResidua.setFlagValidato("S");
				else
					aPenaResidua.setFlagValidato("N");

				lPenDao.setDAOFromModel(aPenaResidua);
				// BigDecimal lKeyPena = null;
				/* lKeyPena = */lPenDao.insert();
			}

			/**************************** Fine Pena Residua **************************************/

			// NEW Aggiorno anche la data fine altra causa come comunicato dal Carcere
			// Date lNuovoFineAltraCausa =
			if (aPenaResidua.getDataInizio() != null) {
				lAltraCausaSqlDao = new AltraCausaSqlDAO(lConn);
				lAltraCausaSqlDao.ricercaAltraCausaByIdFascicolo(aKeyFasc);
				AltraCausaModel lAltraCausaModel = null;
				lAltraCausaModel = (AltraCausaModel) lAltraCausaSqlDao.getModelByKey();
				lAltraCausaSqlDao.stop();

				if (lAltraCausaModel != null) {
					// n.b. la data fine pena altra causa non viene salvata
					Date lNuovoFineAltraCausa = aPenaResidua.getDataInizio();
					lNuovoFineAltraCausa = DateUtils.moveDateTo(lNuovoFineAltraCausa, GregorianCalendar.DATE,
							-1);
					lAltraCausaDao = new AltraCausaDAO(lConn);
					lAltraCausaDao.setDataScadenza(lNuovoFineAltraCausa);
					lAltraCausaDao.setCondizioneUpdate(lAltraCausaModel.getIdAltraCausa());
					lAltraCausaDao.update();
					lAltraCausaDao.stop();
				}
			}

			lVerMod.setIdVerbale(lKey);
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleController.ExInserisciNotificaCarcere: " + ex);
		} catch (F3BException fex) {
			rollback(lConn);

			fex.printStackTrace();

			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				throw fex;
			} else {
				throw new F3BException("VerbaleController.ExInserisciNotificaCarcere: " + fex);
			}
		} catch (Exception es) {
			rollback(lConn);
			es.printStackTrace();
			siesLogger.debug("SQLException: " + es);
			throw new F3BException("VerbaleController.ExInserisciNotificaCarcere: " + es);
		} finally {
			cleanup(lVerDao);
			cleanup(lPenDao);
			cleanup(lPenResDao);
			cleanup(lEveDao);

			cleanup(lAltraCausaSqlDao);
			cleanup(lAltraCausaDao);

			cleanup(lConn);
		}

		return lVerMod;
	}

	public VerbaleDataInizioModel ExRicercaVerbaleByIdFascicoloSiep(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		VerbaleDataInizioModel lVerbMod = null;
		VerbaleSqlDAO lVerDao = null;
		try {
			lConn = getDBConnection();
			lVerDao = new VerbaleSqlDAO(lConn);
			lVerbMod = new VerbaleDataInizioModel();
			if (aKey != null) {
				lVerDao.ricercaVerbaleByIdFascicoloSiep(aKey);
				lVerDao.start();
				while (lVerDao.next()) {
					lVerbMod = (VerbaleDataInizioModel) lVerDao.getModelVerb();
				}
			}
		}

		catch (DAOException daoEx) {
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"VerbaleController.ExRicercaVerbaleByIdFascicolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lVerDao);
			cleanup(lConn);
		}

		return lVerbMod;
	}

	// Ambrosino 30/07/2010
	public EventoModel ExInserisciVariazioneVerbaleSottoscrizione(BigDecimal aKeyFasc, VerbaleModel aVerbale)
			throws F3BException {
		Connection lConn = null;

		VerbaleDAO lVerDao = null;
		// VerbaleModel lVerMod = null;
		MisuraAlternativaDAO lMisAltDao = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaModel lMisMod = null;
		EventoSqlDAO lEveDao = null;
		EventoModel lEveMod = null;
		EventoDAO lEventoDao = null;
		UfficioSqlDAO lUffSqlDao = null;

		CampoNotaDAO lCampoNotaDao = null;
		EventoModel lEveModRet = new EventoModel();

		try {
			lConn = getDBTransaction();

			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lVerDao = new VerbaleDAO(lConn);
			lMisAltDao = new MisuraAlternativaDAO(lConn);
			lEveDao = new EventoSqlDAO(lConn);
			lUffSqlDao = new UfficioSqlDAO(lConn);

			lCampoNotaDao = new CampoNotaDAO(lConn);

			/*
			 * modifica 13-02-06 - effettuata da Dario -- Richiesta da Viviana si cerca l'ultima concessione
			 * per usarla nella registrazione data inizio misura!!!
			 */
			String[] Natura = { "CO", "DD" };
			String[] TipoMisura = null;
			String[] Decisione = null;
			lMisDao.ricercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisionePerVerbale(aKeyFasc,
					Decisione, Natura, TipoMisura);
			lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

			BigDecimal lKeyEvento = null;
			if (lMisMod != null && lMisMod.getEveIdEvento() != null) {
				lEveDao.ricercaEventoByKey(lMisMod.getEveIdEvento());
				lEveMod = (EventoModel) lEveDao.getModelByKey();
			}

			if (lEveMod != null && lEveMod.getCodMotivo() != null && lEveMod.getCodTipoProvvedimento() != null
					&& (lEveMod.getCodTipoProvvedimento().equals("03") // Ordinanza
							|| lEveMod.getCodTipoProvvedimento().equals("02") // Decreto
					) && (lEveMod.getCodMotivo().equals("2005") || // Ammissione provvisoria a detenzione
																	// domiciliare
							lEveMod.getCodMotivo().equals("2006") || // Ammissione provvisoria a affidamento
																		// in prova
							lEveMod.getCodMotivo().equals("2008") || // Ammissione provvisoria a affidamento
																		// in prova
							lEveMod.getCodMotivo().equals("0001") || // Affidamento in prova
							lEveMod.getCodMotivo().equals("0002") || // Affidamento in prova
							lEveMod.getCodMotivo().equals("0003") || // Affidamento in prova
							lEveMod.getCodMotivo().equals("0004") || // Semiliberta
							lEveMod.getCodMotivo().equals("0005") || // Detenzione Domiciliare
							lEveMod.getCodMotivo().equals("0010") || // Detenzione Domiciliare
							lEveMod.getCodMotivo().equals("0013") || // Detenzione Domiciliare
							lEveMod.getCodMotivo().equals("0011") || // Concessione Detenzione Domiciliare a
																		// Termine
							lEveMod.getCodMotivo().equals("2630") || // Esecuzione presso domicilio della pena
																		// detentiva
							lEveMod.getCodMotivo().equals("2397") || // Esecuzione presso domicilio della pena
																		// detentiva
							lEveMod.getCodMotivo().equals("2245") // Concessione Sospensione Condizionata
																	// esecuzione parte finale pena detentiva
							// 20191120 [SG]: aggiunto codice per gestione ticket
							// Ticket#20191114019 — SIES - mancata registrazione data inizio misura
							// Ticket#20191112019 — 2019/11 Ancona Procura Minori non fa caricare inizio
							// misura Esecuzione presso domicilio della pena detentiva ( TdS )
							|| lEveMod.getCodMotivo().equals("0610"))) {
				// inserisco un Evento
				lEventoDao = new EventoDAO(lConn);
				lEventoDao.setCodTipoEvento("01"); // Provvedimento
				lEventoDao.setCodMotivo("5414"); // Sottiscrizione Obblighi
				lEventoDao.setCodTipoProvvedimento("25"); // Annotazione

				lEventoDao.setFlagDocumentoRegistrato("S");
				lEventoDao.setFlagVideoSiep("S");
				lEventoDao.setFlagStampaSiep("S");

				if (lEveMod.getIdEvento() != null)
					lEventoDao.setEveIdEvento(lEveMod.getIdEvento());

				lEventoDao.setFasSieIdFascicoloSiep(aKeyFasc);

				lEventoDao.setCodOperatoreInserimento(aVerbale.getCodOperatoreInserimento());
				lEventoDao.setCodUfficioInserimento(aVerbale.getCodUfficioInserimento());
				lEventoDao.setDataInserimento(aVerbale.getDataInserimento());

				lEventoDao.setCodEsito("-");
				lEventoDao.setCodMagistrato("-");
				lEventoDao.setCodLuogoDestinatario("-");
				lEventoDao.setCodTipoUfficioDestinatario("-");
				// -------------------------------------------------------------------
				// Da Chiarire cosa mettere dentro data emissione?
				// Per ora cè la Data Richiesta di Variazione
				lEventoDao.setDataEmissione(aVerbale.getDataPervenimento());
				// -------------------------------------------------------------------
				// lEventoDao.setDataRichiesta(aVerbale.getDataPervenimento());

				// cerco la sede dell'ufficio d'inserimento per caricare il luogo emittente
				UfficioModel lUffMod = new UfficioModel();
				if (aVerbale != null) {
					lUffSqlDao.ricercaUfficioByCod(aVerbale.getCodUfficioInserimento());
					lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
				}

				if (lUffMod != null && lUffMod.getCodComune() != null)
					lEventoDao.setCodLuogoEmittente(lUffMod.getCodComune());
				else
					lEventoDao.setCodLuogoEmittente("-");

				lEventoDao.setCodUfficioEmittente(aVerbale.getCodUfficioInserimento());
				lKeyEvento = lEventoDao.insert();
				lEventoDao.stop();

				// AMBROSINO : 08/2010 - del model di ritorno mi serve soltanto IdEvento
				lEveModRet.setIdEvento(lKeyEvento);

				// Campo Nota per Motivazioni

				// Campo Notizie di reato (viene inserito nella tabella CAMPO_NOTA solo se è stato compilato)

				String lTestoNote = (aVerbale.getNote());
				if (!lTestoNote.trim().equals("")) {
					CampoNotaModel lCampoNotaMod = new CampoNotaModel();
					lCampoNotaMod.setDescr(lTestoNote);
					lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());
					lCampoNotaMod.setCodOperatoreInserimento(aVerbale.getCodOperatoreInserimento());
					lCampoNotaMod.setCodUfficioInserimento(aVerbale.getCodUfficioInserimento());
					lCampoNotaMod.setFasSieIdFascicoloSiep(aKeyFasc);
					lCampoNotaMod.setEveIdEvento(lKeyEvento);
					lCampoNotaMod.setProgressivo(new BigDecimal(1));

					lCampoNotaDao.setDAOFromModel(lCampoNotaMod);
					BigDecimal lIdCampoNota = lCampoNotaDao.insert();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserito Campo_NOTA : " + lIdCampoNota);

				}

				// modifica Misura Alternativa

				lMisMod.setDataAggiornamento(DateUtils.getSysDate());
				lMisMod.setCodOperatoreAggiornamento(aVerbale.getCodOperatoreInserimento());
				lMisMod.setCodUfficioAggiornamento(aVerbale.getCodUfficioInserimento());
				// AMBROSINO 01/10/2010 - Prendo le note e ci aggiungo un'annotazione con la
				// Data inizio Misura da variare, altrimente non se ne avrebbe più traccia

				String DataInizioMisura = DateUtils.getDateToString(lMisMod.getDataInizioMisura(),
						"dd-MM-yyyy");
				String lNoteMis = lMisMod.getNote();
				lMisMod.setNote(lNoteMis + " (** era stata erroneamente indicata la data inizio misura in "
						+ DataInizioMisura + " ");
				// Nuova data Inizio Misura Alternativa
				lMisMod.setDataInizioMisura(aVerbale.getDataEmissione());

				lMisAltDao.setDAOFromModelForUpdate(lMisMod);
				lMisAltDao.update();
				lMisAltDao.stop();

			} else {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non esiste l'Ordinanza/Decreto del TDS/MDS.");
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleController.ExInserisciVerbaleSottoscrizione: " + ex);
		} catch (F3BException fex) {
			rollback(lConn);

			fex.printStackTrace();

			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				throw fex;
			} else {
				throw new F3BException("VerbaleController.ExInserisciVerbaleSottoscrizione: " + fex);
			}
		}

		catch (Exception es) {
			rollback(lConn);
			es.printStackTrace();
			siesLogger.debug("SQLException: " + es);
			throw new F3BException("VerbaleController.ExInserisciVerbaleSottoscrizione: " + es);
		} finally {
			cleanup(lVerDao);
			cleanup(lMisAltDao);
			cleanup(lMisDao);
			cleanup(lEveDao);
			cleanup(lEventoDao);
			cleanup(lUffSqlDao);
			cleanup(lCampoNotaDao);

			cleanup(lConn);
		}

		return lEveModRet;
	}

	/**
	 * Effettua l'inserimento dell'Annotazione Designazione Istituto da parte del DAP nella'ambito della
	 * esecuzione Misure Sicurezza. La Dinamica della designazione dell'Istituto è simile a quella del
	 * pervenimento ed inserimento Verbale, quindi viene utilizzata la tabella VERBALE. Viene inserito un
	 * EVENTO provvedimento Annotazione , un VERBALE, (la PENA_RESIDUA aggiornata ??)
	 * 
	 * @param aEvento
	 * @param aVerbale
	 * @param aPenaResidua
	 *            ?
	 * @return IdEvento
	 * @throws F3BException
	 */
	public BigDecimal ExInserisciEventoVerbale(EventoModel aEvento, VerbaleModel aVerbale)
			throws F3BException {
		BigDecimal lIdEvento = null;
		Connection lConn = null;
		VerbaleDAO lVerDao = null;
		EventoDAO lEveDao = null;

		VerbaleModel lVerMod = null;
		EventoModel lEveMod = null;
		try {
			lConn = getDBTransaction();

			// ===============================================
			// Inserisco l'Evento
			// ===============================================
			lEveMod = new EventoModel(aEvento);
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(lEveMod);

			lIdEvento = lEveDao.insert();
			lEveDao.stop();

			// ===============================================
			// Inserisco il Verbale
			// ===============================================
			lVerMod = new VerbaleModel(aVerbale);
			lVerMod.setEveIdEvento(lIdEvento);
			lVerDao = new VerbaleDAO(lConn);
			lVerDao.setDAOFromModel(lVerMod);

			// BigDecimal lKey = null;
			/* lKey = */lVerDao.insert();
			lVerDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("VerbaleController.ExInserisciEventoVerbale: " + ex);
		} catch (F3BException fex) {
			rollback(lConn);

			fex.printStackTrace();

			if (fex.getErrorCode() == F3BException.USER_MESSAGE) {
				throw fex;
			} else {
				throw new F3BException("VerbaleController.ExInserisciEventoVerbale: " + fex);
			}
		} catch (Exception es) {
			rollback(lConn);
			es.printStackTrace();
			siesLogger.debug("SQLException: " + es);
			throw new F3BException("VerbaleController.ExInserisciEventoVerbale: " + es);
		} finally {
			cleanup(lVerDao);
			cleanup(lEveDao);

			cleanup(lConn);
		}
		return lIdEvento;
	}

} // Chiude Controller