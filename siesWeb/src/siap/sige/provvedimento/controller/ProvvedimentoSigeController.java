package siap.sige.provvedimento.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.util.xml.TreeModel;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.util.report.ReportGenerator;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.sige.SIGEException;
import siap.sige.datiprovsige.dao.DatiProvvedimentoSigeDAO;
import siap.sige.documentoallegato.controller.IDocumentoAllegato;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.dao.FascicoloSigeDAO;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.fogliocomplementare.model.FoglioComplementareModel;
import siap.sige.impugnazione.action.ICostantiImpugnazioneSige;
import siap.sige.impugnazione.dao.ImpugnazioneSigeSqlDAO;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.motivazioneprovvedimento.dao.MotivazioneProvvedimentoSigeDAO;
import siap.sige.motivazioneprovvedimento.dao.MotivazioneProvvedimentoSigeSqlDAO;
import siap.sige.motivazioneprovvedimento.model.MotivazioneProvvedimentoSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.dao.ProvvedimentoSigeDAO;
import siap.sige.provvedimento.dao.ProvvedimentoSigeSqlDAO;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.stampa.action.ICostantiStampaSige;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.dao.TenoreSentenzaReatoDAO;
import siap.sige.tenore.dao.TenoreSigeDAO;
import siap.sige.tenore.dao.TenoreSigeSqlDAO;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.dao.UdienzaSigeSqlDAO;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaparti.dao.UdienzaPartiDAO;
import siap.sige.udienzaprocedimento.dao.UdienzaProcedimentoSigeDAO;
import siap.sige.udienzaprocedimento.dao.UdienzaProcedimentoSigeSqlDAO;
import siap.sige.udienzaprocedimento.model.UdienzaProcSigeUdienzaModel;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sius.documentoallegato.dao.DocumentoAllegatoSqlDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;

/**
 * <p>
 * Title: ProvvedimentoSigeController
 * </p>
 * <p>
 * Description: Classe Controller per ProvvedimentoSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProvvedimentoSigeController extends GenericController implements IProvvedimentoSige {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Ricerca un Provvedimento Definitorio per il Fascicolo SIGE.
	 * <p>
	 * 
	 * @param aKey
	 *            id di Provvedimento SIGE.
	 * @return ProvvedimentoSigeEventoModel della verifica.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public ProvvedimentoSigeEventoModel ExRicercaProvvedimentoDefinitorioByIdFascicolo(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		MotivazioneProvvedimentoSigeSqlDAO lMPSDao = null;
		ProvvedimentoSigeEventoModel lProvvEveModel = null;
		Vector lMotivazioni = null;
		// AGGIUNGO L'ESTRAZIONE DELLE IMPUGNAZIONI IN MODO DA AGGANCIARLE AL PROVVEDIMENTO
		ImpugnazioneSigeSqlDAO impSqlDao = null;

		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			lProvSqlDao.ricercaProvvedimentoDefinitorioByIdFasSige(aKey);
			lProvSqlDao.start();

			if (lProvSqlDao.next()) {
				lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(lProvvEveModel.getProvvedimento().getIdEventoGenerato());

				EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
				if (lEveMod == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Errore! Evento non trovato per il provvedimento : "
									+ lProvvEveModel.getProvvedimento().getIdProvvedimentoSige());

				// Individuazione della presenza di documenti allegati
				int lNumAllegati = -1;
				int lNumValidati = -1;
				lNumAllegati = lEveSqlDao.getNumDocumentiAllegati(lEveMod.getIdEvento(), null);
				lEveMod.setNumAllegati(lNumAllegati);
				if (lNumAllegati > 0) {
					lNumValidati = lEveSqlDao.getNumAllegatiValidati(lEveMod.getIdEvento());
					lEveMod.setNumAllValidati(lNumValidati);
				}

				lProvvEveModel.getEventoNotifica().setEvento(lEveMod);

				// Lettura Eventuali Motivi Provvedimento Sige.
				lMPSDao = new MotivazioneProvvedimentoSigeSqlDAO(lConn);
				lMPSDao.ricercaMotivazioneDecretoInammissibilitaByIdProvSige(
						lProvvEveModel.getProvvedimento().getIdProvvedimentoSige());
				lMotivazioni = new Vector(lMPSDao.getModels());
				if (lMotivazioni != null)
					lProvvEveModel.setMotiviProvvedSige(lMotivazioni);

				// @emma- inizio 10/11/2016 aggiungo l'estrazione delle impugnazioni
				List<ImpugnazioneSigeModel> impugnazioni = new ArrayList<ImpugnazioneSigeModel>();
				impSqlDao = new ImpugnazioneSigeSqlDAO(lConn);
				impSqlDao.ricercaImpugnazioniByIdProvvTipoImp(lProvModel.getIdProvvedimentoSige(), "01");
				impugnazioni.addAll(impSqlDao.getModels());
				impSqlDao.ricercaImpugnazioniByIdProvvTipoImp(lProvModel.getIdProvvedimentoSige(), "04");
				impugnazioni.addAll(impSqlDao.getModels());
				lProvvEveModel.setImpugnazioni(impugnazioni);
				// @emma- fine
			}
			lProvSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentoDefinitorioByIdFascicolo : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentoDefinitorioByIdFascicolo : " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lMPSDao);
			cleanup(lConn);
		}
		return lProvvEveModel;
	}

	/**
	 * Inserisce Provvedimento, Evento e Notifiche.
	 * <p>
	 * 
	 * @param aKey
	 *            id di Provvedimento SIGE.
	 * @return ProvvedimentoSigeEventoModel della verifica.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public ProvvedimentoSigeEventoModel ExInserisciProvvEveNotifica(
			ProvvedimentoSigeEventoModel lProvEveModel, EventoNotificaModel aEventoNotifiche, Vector lTenori,
			String aCodTipoGiudizio) throws Exception {

		// Models da inserire e valorizzare in return.
		ProvvedimentoSigeEventoModel lProvvSigeEvento = new ProvvedimentoSigeEventoModel();
		ProvvedimentoSigeModel lProvvedimento = new ProvvedimentoSigeModel(lProvEveModel.getProvvedimento());
		EventoModel lEvento = new EventoModel(lProvEveModel.getEventoNotifica().getEvento());

		Connection lConn = null;
		ProvvedimentoSigeDAO lProvDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			lConn = getDBConnection();
			lProvDao = new ProvvedimentoSigeDAO(lConn);
			lEveDao = new EventoDAO(lConn);

			// Inserimento Evento e Notifiche
			IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNot = lEveCtrl.ExInserisciEventoNotifica(aEventoNotifiche, lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("ExInserisciEventoProvvedimento : Inserito EventoAA -> "
					+ lEveNot.getEvento().getIdEvento());
			lProvvedimento.setIdEventoGenerato(lEveNot.getEvento().getIdEvento());

			// Inserimento Provvedimento SIGE con l'IdEventoGenerato poco prima.
			lProvDao = new ProvvedimentoSigeDAO(lConn);
			lProvvedimento.setIdEventoGenerato(lEveNot.getEvento().getIdEvento());
			lProvDao.setDAOFromModel(lProvvedimento);
			BigDecimal lIdProvvedimento = lProvDao.insert();
			lProvDao.stop();
			lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciProvvedimento : Inserito Provvedimento -> " + lIdProvvedimento);

			// Valorizzazione campi Tenori
			lTenori = aggiornaListaTenori(lTenori, lProvvedimento);

			ITenoreSige lCtrlTen = SIGELookupRemote.getTenoreSigeRemote();

			// Controllo su data irrevocabilità Sentenze
			lCtrlTen.ExControlloDataIrrevocabilita(lTenori);

			// Se valorizzato va aggiornato il Tipo Giudizio sul Fascicolo
			if (aCodTipoGiudizio != null)
				aggiornaTipoGiudizioFascicolo(lProvvedimento, aCodTipoGiudizio, lConn);

			// 15-01-2010 Impostazione Codice Esito Oggetto SIGE (Per Ordinanza di NDP/NLP non si validano gli
			// oggetti).
			if (lProvvedimento.getCodTipoProvvedimentoSige()
					.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_NDPNLP) == 0)
				this.aggiornaCodEsitoTenore(lProvvedimento.getCodTipoProvvedimentoSige(), lTenori, lConn);

			// Aggiornamento Tenori demandata alla funzione TenoreSigeController.ExInserisciOggetti.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciProvvedimento : Fase di chiusura per il Tenore");
			lCtrlTen.ExInserisciOggetti(lTenori, lProvvedimento.getFasIdFascicoloSige(), lConn);

			// COMMIT
			commit(lConn);
		}

		catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciProvvedimento : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciProvvedimento : " + e);
		} finally {
			cleanup(lProvDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}
		lProvvSigeEvento.setProvvedimento(lProvvedimento);
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.setEvento(lEvento);
		lProvvSigeEvento.setEventoNotifica(lEveNot);

		return lProvvSigeEvento;

	}

	/**
	 * Esegue l'inserimento del Provvedimento SIGE. e (da decidere) l'aggiornamento del Fascicolo SIGE.
	 * <p>
	 * Description: Funzione per l'inserimento del Provvedimento SIGE.
	 * </p>
	 * Le tabelle coinvolte sono:
	 * <p>
	 * PROVVEDIMENTO_SIGE : viene inserito il nuovo record (ordinanza/decreto);
	 * <p>
	 * EVENTO : viene inserito un nuovo record;
	 * <p>
	 * TENORE : vengono chiusi i tenori attivi (data_fine) ed inseriti i nuovi tenori;
	 * <p>
	 * FASCICOLO_SIGE : ?? update dello stato del FASCICOLO.
	 * 
	 * @param IdFascicoloSige
	 * @param ProvvedimentoSigeEventoModel
	 * @param Vector
	 *            di Tenori
	 * @throws F3BException
	 * @return ProvvedimentoSigeEventoModel
	 */
	public ProvvedimentoSigeEventoModel ExInserisciProvvedimento(ProvvedimentoSigeEventoModel lProvEveModel,
			Vector lTenori, String aCodTipoGiudizio) throws Exception {

		// Models da inserire e valorizzare in return.
		ProvvedimentoSigeEventoModel lProvvSigeEvento = new ProvvedimentoSigeEventoModel();
		ProvvedimentoSigeModel lProvvedimento = new ProvvedimentoSigeModel(lProvEveModel.getProvvedimento());
		EventoModel lEvento = new EventoModel(lProvEveModel.getEventoNotifica().getEvento());

		Connection lConn = null;
		ProvvedimentoSigeDAO lProvDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			lConn = getDBConnection();
			lProvDao = new ProvvedimentoSigeDAO(lConn);
			lEveDao = new EventoDAO(lConn);

			// Si Setta l'Anno e il progressivo...
			lEveSqlDao = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEveSqlDao.getProgressivo(lEvento);
			lEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			// Inserimento Evento.
			lEveDao.setDAOFromModel(lEvento);
			BigDecimal lIdEvento = lEveDao.insert();
			lEveDao.stop();
			lEvento.setIdEvento(lIdEvento);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciProvvedimento : Inserito Evento -> " + lIdEvento);

			lProvvedimento.setIdEventoGenerato(lIdEvento);

			// Inserimento Provvedimento.
			lProvDao.setDAOFromModel(lProvvedimento);
			BigDecimal lIdProvvedimento = lProvDao.insert();
			lProvDao.stop();

			lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciProvvedimento : Inserito Provvedimento -> " + lIdProvvedimento);

			// Valorizzazione campi Tenori
			lTenori = aggiornaListaTenori(lTenori, lProvvedimento);

			ITenoreSige lCtrlTen = SIGELookupRemote.getTenoreSigeRemote();

			// Controllo su data irrevocabilità Sentenze
			lCtrlTen.ExControlloDataIrrevocabilita(lTenori);

			// Se valorizzato va aggiornato il Tipo Giudizio sul Fascicolo
			if (aCodTipoGiudizio != null)
				aggiornaTipoGiudizioFascicolo(lProvvedimento, aCodTipoGiudizio, lConn);

			// 15-01-2010 Impostazione Codice Esito Oggetto SIGE (Per Ordinanza di NDP/NLP non si validano gli
			// oggetti).
			if (lProvvedimento.getCodTipoProvvedimentoSige()
					.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_NDPNLP) == 0)
				this.aggiornaCodEsitoTenore(lProvvedimento.getCodTipoProvvedimentoSige(), lTenori, lConn);

			// 03-01-2011 Impostazione Codice Esito Oggetto SIGE (Per Ordinanza di Conflitto di Competenza).
			if ((lProvvedimento.getCodTipoProvvedimentoSige()
					.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_CONFLITTO_COMPETENZA) != 0)
					&& ((TenoreSigeModel) lTenori.get(0)).getCodEsitoSige() != null
					&& ((TenoreSigeModel) lTenori.get(0)).getCodEsitoSige().compareTo("0502") == 0)
				this.aggiornaCodEsitoTenore(lProvvedimento.getCodTipoProvvedimentoSige(), lTenori, lConn);

			// 20190508 [SG]: controllo situazione tenori: se non li ho toccati allora evito l'inserimento
			// boolean isChanged = false;
			// if (lTenori != null) {
			// Iterator itx = lTenori.iterator();
			// while (itx.hasNext()) {
			// TenoreSigeModel tsm = (TenoreSigeModel) itx.next();
			// if (tsm.getProvIdProvvedimentoSige() != null) {
			// isChanged = true;
			// break;
			// }
			// }
			// }
			// if (isChanged) {
			// Aggiornamento Tenori demandata alla funzione TenoreSigeController.ExInserisciOggetti.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciProvvedimento : Fase di chiusura per il Tenore");
			lCtrlTen.ExInserisciOggetti(lTenori, lProvvedimento.getFasIdFascicoloSige(), lConn);
			// }

			// COMMIT
			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciProvvedimento : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciProvvedimento : " + e);
		} finally {
			cleanup(lProvDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}
		lProvvSigeEvento.setProvvedimento(lProvvedimento);
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.setEvento(lEvento);
		lProvvSigeEvento.setEventoNotifica(lEveNot);

		return lProvvSigeEvento;
	}

	private Vector<TenoreSigeModel> aggiornaListaTenori(Vector<TenoreSigeModel> aListaTenori,
			ProvvedimentoSigeModel aProvvedimento) {

		// 20190508 [SG]: imposto IdRichiestaSige sempre sui nuovi tenori
		// BigDecimal bd = null;
		// for (TenoreSigeModel tsm : aListaTenori) {
		// if (tsm.getRicSigIdRichiestaSige() != null) {
		// bd = tsm.getRicSigIdRichiestaSige();
		// break;
		// }
		// }
		if (aListaTenori != null) {
			for (TenoreSigeModel lTenore : aListaTenori) {
				lTenore.setCodOperatoreInserimento(aProvvedimento.getCodOperatoreInserimento());
				lTenore.setCodUfficioInserimento(aProvvedimento.getCodUfficioInserimento());
				lTenore.setDataInserimento(aProvvedimento.getDataInserimento());
				lTenore.setData(aProvvedimento.getDataInserimento());
				lTenore.setFasIdFascicoloSige(aProvvedimento.getFasIdFascicoloSige());
				// 20190508 [SG]: cambiata impostazione proprietà
				// lTenore.setRicSigIdRichiestaSige(null);
				// if (lTenore.getRicSigIdRichiestaSige() == null)
				lTenore.setProvIdProvvedimentoSige(aProvvedimento.getIdProvvedimentoSige());
				lTenore.setRicSigIdRichiestaSige(null);
				// lTenore.setCodOperatoreAggiornamento(null);
				// lTenore.setCodUfficioAggiornamento(null);
				// lTenore.setDataAggiornamento(null);
				// if (bd != null && lTenore.getRicSigIdRichiestaSige() == null)
				// lTenore.setRicSigIdRichiestaSige(bd);
			}
		}

		return aListaTenori;
	}

	private Vector aggiornaListaTenoriSospensione(Vector aListaTenori,
			ProvvedimentoSigeModel aProvvedimento) {

		if (aListaTenori != null) {
			Iterator itx = aListaTenori.iterator();
			while (itx.hasNext()) {
				TenoreSigeModel lTenore = (TenoreSigeModel) itx.next();

				lTenore.setCodOperatoreInserimento(aProvvedimento.getCodOperatoreInserimento());
				lTenore.setCodUfficioInserimento(aProvvedimento.getCodUfficioInserimento());
				lTenore.setDataInserimento(aProvvedimento.getDataInserimento());
				lTenore.setData(aProvvedimento.getDataInserimento());
				lTenore.setFasIdFascicoloSige(aProvvedimento.getFasIdFascicoloSige());
				lTenore.setProvIdProvvedimentoSige(aProvvedimento.getIdProvvedimentoSige());
				lTenore.setRicSigIdRichiestaSige(null);
				lTenore.setCodEsitoSige("0500");
			}
		} // endif

		return aListaTenori;

	}

	private void aggiornaTipoGiudizioFascicolo(ProvvedimentoSigeModel aProvvedimento, String aCodTipoGiudizio,
			Connection aConn) throws Exception {

		// Valorizzazione del FascicoloSigeModel
		FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
		lFascicolo.setIdFascicoloSige(aProvvedimento.getFasIdFascicoloSige());
		lFascicolo.setCodTipoGiudizio(aCodTipoGiudizio);
		lFascicolo.setCodOperatoreAggiornamento(aProvvedimento.getCodOperatoreInserimento());
		lFascicolo.setCodUfficioAggiornamento(aProvvedimento.getCodUfficioInserimento());
		lFascicolo.setDataAggiornamento(aProvvedimento.getDataInserimento());

		// Update del Tipo Giudizio del Fascicolo
		FascicoloSigeDAO lFasDAO = new FascicoloSigeDAO(aConn);
		lFasDAO.setDAOFromModelForUpdateTipoGiudizio(lFascicolo);
		lFasDAO.update();
		cleanup(lFasDAO);
	}

	public void ExInserisciProvvedimentoSige(ProvvedimentoSigeModel aProvvedimentoSige) throws F3BException {

		Connection lConn = null;
		ProvvedimentoSigeDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProvvedimentoSigeDAO(lConn);
			lProDao.setDAOFromModel(aProvvedimentoSige);
			lProDao.insert();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciProvvedimentoSige: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciProvvedimentoSige: " + e);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
	}

	/**
	 * Description: Funzione di ricerca dei provvedimenti SIGE validati per consentirne il Deposito.
	 * </p>
	 * I parametri di ricerca (Ordinanza/Decreto, Data Emissione,... etc.) vengono impostati nel parametro
	 * ProvvedimentoSigeModel)
	 * 
	 * @param ProvvedimentoSigeModel
	 * @throws F3BException
	 * @return Vector di ProvvedimentoSigeEventoModel
	 */
	public Vector ExRicercaProvvedimentoDaDepositare(ProvvedimentoSigeModel aProvvedimentoSige)
			throws F3BException {

		Connection lConn = null;
		Vector lProvvedimentiSige = new Vector();
		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		ProvvedimentoSigeEventoModel lProvvEveModel = null;
		TenoreSigeSqlDAO lTenSqlDao = null;

		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			lProvSqlDao.ricercaProvvedimentoDaDepositare(aProvvedimentoSige);
			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

				// Ricerca tenori legati al provvedimento
				TenoreSigeModel lTenore = new TenoreSigeModel();
				lTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());

				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());
				lTenSqlDao = new TenoreSigeSqlDAO(lConn);
				lTenSqlDao.ricercaTenoriSige(aTenore);
				Vector lTenoriSige = new Vector(lTenSqlDao.getModels());

				lProvvEveModel.setTenoriEstesi(lTenoriSige);

				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lProvvedimentiSige.add(lProvvEveModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentoDaDepositare: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException("ProvvedimentoSigeController.ExRicercaProvvedimentoDaDepositare: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
		return lProvvedimentiSige;
	}

	public void ExModificaProvvedimentoSige(ProvvedimentoSigeModel aProvvedimentoSige,
			MotivazioneProvvedimentoSigeModel[] aMotivazioni) throws F3BException {

		Connection lConn = null;
		ProvvedimentoSigeDAO lProDao = null;
		EventoDAO lEveDao = null;
		MotivazioneProvvedimentoSigeDAO lMPSDao = null;
		AnnotazioneManualeDAO lAnnDao = null;
		AnnotazioneManualeSqlDAO lAnnSqlDao = null;

		if (aProvvedimentoSige == null || aProvvedimentoSige.getIdProvvedimentoSige() == null)
			throw new F3BException(
					"ProvvedimentoSigeController.ExModificaProvvedimentoSige: ID Provvedimento non valorizzato !");

		try {
			lConn = getDBConnection();
			// Aggiornamento Provvedimento Sige
			lProDao = new ProvvedimentoSigeDAO(lConn);
			lProDao.setDAOFromModelForUpdate(aProvvedimentoSige);
			lProDao.update();

			// Aggiornamento della Data di Emissione nell'Evento collegato
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDataEmissione(aProvvedimentoSige.getDataEmissione());
			lEveDao.setDataAggiornamento(aProvvedimentoSige.getDataAggiornamento());
			lEveDao.setCodUfficioAggiornamento(aProvvedimentoSige.getCodUfficioAggiornamento());
			lEveDao.setCodOperatoreAggiornamento(aProvvedimentoSige.getCodOperatoreAggiornamento());
			lEveDao.selCondizioneUpdate(aProvvedimentoSige.getIdEventoGenerato());
			lEveDao.update();

			// Se esiste una Annotazione Manuale per il Fascicolo corrente
			// con 'COD_TIPO_ANNOTAZIONE' = '002' o '003' eseguo
			// l'aggiornamento della DATA_GE sulla tabella ANNOTAZIONE_MANUALE;
			// tale data sarà uguale alla Data Emissione del Provvedimento Sige modificato
			lAnnSqlDao = new AnnotazioneManualeSqlDAO(lConn);
			lAnnSqlDao.ricercaAnnotazioneManualeIndultoAmnistiaByIdEvento(
					aProvvedimentoSige.getIdEventoGenerato());

			AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) (lAnnSqlDao.getModelByKey());
			if (lAnnMod != null) {
				lAnnDao = new AnnotazioneManualeDAO(lConn);

				lAnnMod.setDataGE(aProvvedimentoSige.getDataEmissione());
				lAnnMod.setDataAggiornamento(aProvvedimentoSige.getDataAggiornamento());
				lAnnMod.setCodUfficioAggiornamento(aProvvedimentoSige.getCodUfficioAggiornamento());
				lAnnMod.setCodOperatoreAggiornamento(aProvvedimentoSige.getCodOperatoreAggiornamento());

				lAnnDao.setDAOFromModelForUpdate(lAnnMod);
				lAnnDao.setCondizioneUpdate(lAnnMod.getIdAnnotazioneManuale());
				lAnnDao.update();
				cleanup(lAnnDao);
			}

			// Eventuali Motivazioni
			if (aMotivazioni != null) {
				// Cancellazione Motivazioni attuali
				lMPSDao = new MotivazioneProvvedimentoSigeDAO(lConn);
				lMPSDao.setCondizioneByIdProvvedimento(aProvvedimentoSige.getIdProvvedimentoSige());
				lMPSDao.delete();
				lMPSDao.stop();

				// Inserimento nuove Motivazioni
				int lSize = aMotivazioni.length;
				for (int lIndex = 0; lIndex < lSize; lIndex++) {
					// Imposta nel model il progressivo motivazione lIndex + 1.
					aMotivazioni[lIndex].setProgrMotivazione(new BigDecimal((double) lIndex + 1));
					lMPSDao.setDAOFromModel(aMotivazioni[lIndex]);
					lMPSDao.setProSigIdProvvedSige(aProvvedimentoSige.getIdProvvedimentoSige());
					lMPSDao.insert();
					lMPSDao.stop();
				}
				cleanup(lMPSDao);
			}

			commit(lConn);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExModificaProvvedimentoSige: " + e);
		} finally {
			cleanup(lProDao);
			cleanup(lAnnSqlDao);
			cleanup(lConn);
		}
	}

	/**
	 * Cancellazione Provvedimento SIGE e tutti i dati ad esso collegato. Tabelle coinvolte:
	 * PROVVEDIMENTO_SIGE, EVENTO, DATI_PROVVEDIMENTO_SIGE, TENORE_SENTENZA_REATO, TENORE_SIGE,
	 * MOTIVAZIONE_PROVVED_SIGE.
	 * 
	 * @param ProvvedimentoSigeModel
	 *            aProvModel
	 * @throws F3BException
	 */

	public void ExCancellaProvvedimentoSige(ProvvedimentoSigeModel aProvModel) throws F3BException {

		Connection lConn = null;
		ProvvedimentoSigeDAO lProDao = null;
		EventoDAO lEveDAO = null;
		DatiProvvedimentoSigeDAO lDatiProvDAO = null;
		TenoreSentenzaReatoDAO lTenSenReaDAO = null;
		TenoreSigeDAO lTenDAO = null;
		NotificaDAO lNotDAO = null;
		CampoNotaDAO lCampoNotaDao = null;
		AnnotazioneManualeDAO lAnnDao = null;
		MotivazioneProvvedimentoSigeDAO lMPSDao = null;
		UdienzaProcedimentoSigeDAO udiProcDao = null;
		ProvvedimentoSigeModel lProvvedimento = null;
		// TenoreSigeSqlDAO lTenSqlDao = null;

		try {
			// inizializzazione della connessione
			lConn = getDBConnection();
			// Si effettua una ricerca preliminare del record PROVVEDIMENTO_SIGE da cancellare
			lProDao = new ProvvedimentoSigeDAO(lConn);
			lProDao.setIdProvvedimentoSige(aProvModel.getIdProvvedimentoSige());
			lProDao.selByKey();
			lProvvedimento = (ProvvedimentoSigeModel) lProDao.getModelByKey();

			if (lProvvedimento == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile trovare il Provvedimento da cancellare !");
			if (lProvvedimento.getIdEventoGenerato() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Manca ID Evento collegato!");

			// cancellazione Notifiche collegate
			lNotDAO = new NotificaDAO(lConn);
			lNotDAO.setCondizioneEvento(lProvvedimento.getIdEventoGenerato());
			lNotDAO.delete();

			// cancellazione Campi Note collegati
			lCampoNotaDao = new CampoNotaDAO(lConn);
			lCampoNotaDao.setCondizioneEvento(lProvvedimento.getIdEventoGenerato());
			lCampoNotaDao.delete();

			// Cancellazione DATI_PROVVEDIMENTO_SIGE collegati attraverso i tenori
			lDatiProvDAO = new DatiProvvedimentoSigeDAO(lConn);
			lDatiProvDAO.selCondizioneDeleteProvvedimento(lProvvedimento.getIdProvvedimentoSige());
			lDatiProvDAO.delete();
			lDatiProvDAO.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancellazione DATI_PROVVEDIMENTO_SIGE");

			// 20190508 [SG]: nuova gestione cancellazione ordinanza
			// elimino solo i tenori con IdProvvedimentoSige valorizzato
			// lTenSqlDao = new TenoreSigeSqlDAO(lConn);
			// lTenSqlDao.ricercaTenoriByProvvedimento(aProvModel.getIdProvvedimentoSige());
			// Vector<TenoreSigeModel> lTenoriSige = new Vector(lTenSqlDao.getModels());
			// boolean existOggettoProcedimento = false;
			// boolean existTenoreNonUnico = false;
			// if (lTenoriSige != null && lTenoriSige.size() > 0
			// && lTenoriSige.firstElement().getRicSigIdRichiestaSige() != null)
			// existOggettoProcedimento = true;
			// if (!existOggettoProcedimento) {
			// Cancellazione record in tabella di relazione TENORE_SENTENZA_REATO collegati attraverso
			// tenori
			lTenSenReaDAO = new TenoreSentenzaReatoDAO(lConn);
			lTenSenReaDAO.selCondizioneDeleteProvvedimento(lProvvedimento.getIdProvvedimentoSige());
			lTenSenReaDAO.delete();
			lTenSenReaDAO.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DELETE TENORE_SENTENZA_REATO");

			// 13/01/2011 Se si sta cancellando un provvedimento con TENORE_SIGE valido/i (quindi con
			// DATA_FINE = null)
			// occorrera' operare il ripristino dei tenori del provvedimento precedente. A tale scopo
			// bisogna leggere un TENORE_SIGE prima di cancellarlo/i.
			boolean lDataFineNull = false;
			TenoreSigeModel lTenCanc = new TenoreSigeModel();
			lTenDAO = new TenoreSigeDAO(lConn);
			lTenDAO.selCondizioneIdProvvedimento(lProvvedimento.getIdProvvedimentoSige());
			lTenDAO.start();
			if (lTenDAO.next()) {
				lTenCanc = (TenoreSigeModel) lTenDAO.getModel();
				if (lTenCanc != null && lTenCanc.getIdTenoreSige() != null && lTenCanc.getDataFine() == null)
					lDataFineNull = true;
			}
			lTenDAO.stop();

			// Cancellazione record tenori collegati in TENORE_SIGE
			lTenDAO = new TenoreSigeDAO(lConn);
			lTenDAO.selCondizioneIdProvvedimento(lProvvedimento.getIdProvvedimentoSige());
			lTenDAO.delete();
			lTenDAO.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DELETE TENORE_SIGE");
			// 13/01/2011 condizionato l'aggiornamento dei TENORE_SIGE se quelli appena cancellati avevano
			// DATA_FINE = null.
			if (lDataFineNull) {
				// 20/07/2009 Ripristino records tenori storicizzati al momento dall'emissione
				// del provvedimento che si sta cancellando.
				lTenDAO = new TenoreSigeDAO(lConn);
				// 04/01/2011 Non va ripulito il CodEsitoSige
				// lTenDAO.setCodEsitoSige(null);
				lTenDAO.setDataFine(null);
				lTenDAO.setNote("");
				lTenDAO.setDataAggiornamento(aProvModel.getDataAggiornamento());
				lTenDAO.setCodOperatoreAggiornamento(aProvModel.getCodOperatoreAggiornamento());
				lTenDAO.setCodUfficioAggiornamento(aProvModel.getCodUfficioAggiornamento());
				lTenDAO.setDAOUpdateTenoriForDeleteProvvedimento(lProvvedimento);
				lTenDAO.update();
				lTenDAO.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("UPDATE TENORE_SIGE (RIPRISTINO)");
				// 24/01/2011 Modifica Data Aggiornamento dei records tenori storicizzati al momento
				// dall'emissione del provvedimento
				// che si sta cancellando.
			} else if (lTenCanc.getDataAggiornamento() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("UPDATE TENORE_SIGE (Ricollegamento DATA_FINE)");
				lTenDAO = new TenoreSigeDAO(lConn);
				lTenDAO.setDataFine(lTenCanc.getDataFine());
				lTenDAO.setDataAggiornamento(lProvvedimento.getDataAggiornamento());
				lTenDAO.setCodOperatoreAggiornamento(lProvvedimento.getCodOperatoreAggiornamento());
				lTenDAO.setCodUfficioAggiornamento(lProvvedimento.getCodUfficioAggiornamento());
				lTenDAO.setDAOUpdateTenoriForDeleteProvvedimento(lProvvedimento);
				lTenDAO.update();
				lTenDAO.stop();
			}
			// } else {
			// if (lTenoriSige != null && lTenoriSige.size() > 0) {
			// for (TenoreSigeModel tsm : lTenoriSige) {
			// if (tsm.getRicSigIdRichiestaSige() != null
			// && tsm.getProvIdProvvedimentoSige() == null) {
			// existTenoreNonUnico = true;
			// break;
			// }
			// }
			// }
			// if (existTenoreNonUnico) {
			// Cancellazione record in tabella di relazione TENORE_SENTENZA_REATO collegati attraverso
			// tenori
			// lTenSenReaDAO = new TenoreSentenzaReatoDAO(lConn);
			// lTenSenReaDAO.selCondizioneDeleteProvvedimento(lProvvedimento.getIdProvvedimentoSige());
			// lTenSenReaDAO.delete();
			// lTenSenReaDAO.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("DELETE TENORE_SENTENZA_REATO");
			// Cancellazione record tenori collegati in TENORE_SIGE
			// lTenDAO = new TenoreSigeDAO(lConn);
			// lTenDAO.selCondizioneIdProvvedimento(lProvvedimento.getIdProvvedimentoSige());
			// lTenDAO.delete();
			// lTenDAO.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("DELETE TENORE_SIGE");
			// } else {
			// update dei tenori aggiornando a null il campo id_provv_sige
			// Cancellazione record tenori collegati in TENORE_SIGE
			// lTenDAO = new TenoreSigeDAO(lConn);
			// lProvvedimento.setDataAggiornamento(new Date());
			// lProvvedimento.setCodOperatoreAggiornamento(aProvModel.getCodOperatoreAggiornamento());
			// lProvvedimento.setCodUfficioAggiornamento(aProvModel.getCodUfficioAggiornamento());
			// lTenDAO.setDAOForDeleteProvvedimento(lProvvedimento);
			// lTenDAO.update();
			// lTenDAO.stop();
			// }
			// }

			// Eventuale Cancellazione di Annotazione Manuale legata all'Evento
			lAnnDao = new AnnotazioneManualeDAO(lConn);

			// Cancellazione dei riferimenti all'Annotazione da cancellare
			lAnnDao.setCondizioneUpdateLinkAnnotazionebyIdEvento(lProvvedimento.getIdEventoGenerato());
			lAnnDao.setAnnoIdAnnotazioneManuale(null);
			lAnnDao.setDataAggiornamento(aProvModel.getDataAggiornamento());
			lAnnDao.setCodUfficioAggiornamento(aProvModel.getCodUfficioAggiornamento());
			lAnnDao.setCodOperatoreAggiornamento(aProvModel.getCodOperatoreAggiornamento());
			lAnnDao.update();
			lAnnDao.stop();

			// Cancellazione
			lAnnDao.setCondizioneLinkEvento(lProvvedimento.getIdEventoGenerato());
			lAnnDao.delete();
			lAnnDao.stop();

			// Eventuale Cancellazione dei Motivi Provvedimento (in caso di Decreto di Inammissibilita').
			if (lProvvedimento.getCodTipoProvvedimentoSige() != null
					&& (lProvvedimento.getCodTipoProvvedimentoSige()
							.compareTo(ICostantiProvvedimentoSige.COD_DECRETO_INAMMISSIBILITA) == 0
							|| lProvvedimento.getCodTipoProvvedimentoSige()
									.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_INCOMPETENZA) == 0)) {
				lMPSDao = new MotivazioneProvvedimentoSigeDAO(lConn);
				lMPSDao.setCondizioneByIdProvvedimento(lProvvedimento.getIdProvvedimentoSige());
				lMPSDao.delete();
				lMPSDao.stop();
			}

			// cancellazione del PROVVEDIMENTO_SIGE
			lProDao = new ProvvedimentoSigeDAO(lConn);
			lProDao.selCondizioneByKey(lProvvedimento.getIdProvvedimentoSige());
			lProDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancellazione PROVVEDIMENTO_SIGE");

			DocumentoAllegatoDAO daDao = new DocumentoAllegatoDAO(lConn);
			daDao.setCondizioneByEve(lProvvedimento.getIdEventoGenerato());
			daDao.delete();
			daDao.stop();

			// MERGE v10: cancellazione preventiva
			if (Utils.isPresent(lProvvedimento) && Utils.isPresent(lProvvedimento.getIdEventoGenerato())) {
				Connection connection = null;
				UdienzaPartiDAO udienzaPartiDAO = null;
				UdienzaProcedimentoSigeSqlDAO lUdiSqlDao = null;
				try {
					// inizializzazione della connessione
					connection = getDBConnection();
					lUdiSqlDao = new UdienzaProcedimentoSigeSqlDAO(connection);
					lUdiSqlDao.ricercaUdienzaProcedimentoByEve(lProvvedimento.getIdEventoGenerato());
					UdienzaProcedimentoSigeModel lUdiMod = (UdienzaProcedimentoSigeModel) lUdiSqlDao
							.getModelByKey();
					if (Utils.isPresent(lUdiMod) && Utils.isPresent(lUdiMod.getIdUdienzaProcedimentoSige())) {
						udienzaPartiDAO = new UdienzaPartiDAO(connection);
						udienzaPartiDAO.setCondizioneByIdUdienzaProcedimentoSige(
								lUdiMod.getIdUdienzaProcedimentoSige());
						udienzaPartiDAO.delete();
						udienzaPartiDAO.stop();
						commit(connection);
					}
				} catch (F3BException fEx) {
					fEx.printStackTrace();
					rollback(connection);
					throw fEx;
				} catch (DAOException daoEx) {
					daoEx.printStackTrace();
					rollback(connection);
					throw new F3BException(
							"ProvvedimentoSigeController.ExCancellaProvvedimentoSige: " + daoEx);
				} catch (Exception sqe) {
					sqe.printStackTrace();
					rollback(connection);
					throw new F3BException(
							"ProvvedimentoSigeController.ExCancellaProvvedimentoSige -> " + sqe);
				} finally {
					cleanup(lUdiSqlDao);
					cleanup(udienzaPartiDAO);
					cleanup(connection);
				}
			}

			udiProcDao = new UdienzaProcedimentoSigeDAO(lConn);
			udiProcDao.setCondizioneByIdEvento(lProvvedimento.getIdEventoGenerato());
			udiProcDao.delete();
			udiProcDao.stop();

			// Cancellazione dell'EVENTO collegato
			lEveDAO = new EventoDAO(lConn);
			lEveDAO.selCondizioneUpdate(lProvvedimento.getIdEventoGenerato());
			lEveDAO.delete();
			lEveDAO.stop();

			commit(lConn);
		} catch (

		F3BException fEx) {
			fEx.printStackTrace();
			rollback(lConn);
			throw fEx;
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExCancellaProvvedimentoSige: " + daoEx);
		} catch (Exception sqe) {
			sqe.printStackTrace();
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExCancellaProvvedimentoSige -> " + sqe);
		} finally {
			cleanup(lProDao);
			cleanup(lNotDAO);
			cleanup(lCampoNotaDao);
			cleanup(lEveDAO);
			cleanup(lDatiProvDAO);
			cleanup(lTenSenReaDAO);
			cleanup(lAnnDao);
			cleanup(lTenDAO);
			cleanup(lMPSDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca dell'unico provvedimento di tipo "definitorio" relativo al Fascicolo SIGE specificato dal suo
	 * ID. Se il provvedimento non esiste la funzione restituisce null.
	 * 
	 * @param aIdFasSige
	 * @return ProvvedimentoSigeModel
	 * @throws F3BException
	 */
	public ProvvedimentoSigeModel ExRicercaProvDefinitorioByFasc(BigDecimal aIdFasSige) throws F3BException {
		Connection lConn = null;
		ProvvedimentoSigeDAO lProvDAO = null;
		ProvvedimentoSigeModel lProvvedimento = null;

		try {
			lConn = getDBConnection();
			lProvDAO = new ProvvedimentoSigeDAO(lConn);
			lProvDAO.selCondizioneProvDefinitorioByFasc(aIdFasSige);
			lProvvedimento = (ProvvedimentoSigeModel) lProvDAO.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvDefinitorioByFasc DAOException:  " + daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvDefinitorioByFasc Exception:  " + e);
		} finally {
			cleanup(lProvDAO);
			cleanup(lConn);
		}
		return lProvvedimento;
	}

	/**
	 * Ricerca il Provvedimento ed Evento collegato attraverso la chiave IdProvvedimento.
	 * 
	 * @param aIdFasSige
	 * @return
	 * @throws F3BException
	 */
	public ProvvedimentoSigeEventoModel ExRicercaProvvedimentoById(BigDecimal aIdProvvedimento)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExRicercaProvvedimentoById: inizio");

		Connection lConn = null;
		ProvvedimentoSigeDAO lProvDAO = null;
		ProvvedimentoSigeModel lProvvedimento = null;
		ProvvedimentoSigeModel lProvvGenerato = null;
		EventoDAO lEvDAO = null;
		ProvvedimentoSigeEventoModel lProvvEveModel = null;
		ProvvedimentoSigeEventoModel lProvvGeneratoEveModel = null;
		UfficioSqlDAO lUDao = null;
		Vector lMotivazioni = null;
		ImpugnazioneSigeSqlDAO impSqlDao = null;

		try {
			lConn = getDBConnection();
			// Ricerca Provvedimento SIGE
			lProvDAO = new ProvvedimentoSigeDAO(lConn);
			lProvDAO.setIdProvvedimentoSige(aIdProvvedimento);
			lProvDAO.selByKey();
			lProvvedimento = (ProvvedimentoSigeModel) lProvDAO.getModelByKey();
			// NUOVA INFRASTRUTTURA: anticipato il controllo su consistenza
			// oggetto "ProvvedimentoSigeModel"
			if (lProvvedimento != null) {
				lProvvedimento.decodifica();

				// Lettura Eventuale Ufficio Destinatario.
				if (lProvvedimento.getCodUfficioDestinatario() != null) {
					lUDao = new UfficioSqlDAO(lConn);
					lUDao.getDescTipoUffByCodUfficio(lProvvedimento.getCodUfficioDestinatario());
					lUDao.start();
					while (lUDao.next())
						lProvvedimento.setDescrUfficioDestinatario(lUDao.getString("DESC_UFFICIO"));
					lUDao.stop();
				}

				// Lettura Eventuale Ufficio Destinatario.
				if (lProvvedimento.getCodUffCompCorteSuprema() != null) {
					lUDao = new UfficioSqlDAO(lConn);
					lUDao.getDescTipoUffByCodUfficio(lProvvedimento.getCodUffCompCorteSuprema());
					lUDao.start();
					while (lUDao.next())
						lProvvedimento.setDescrUffCompCorteSuprema(lUDao.getString("DESC_UFFICIO"));
					lUDao.stop();
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("ricerca provv effettuata");

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("provvedimento: " + lProvvedimento);
				lProvvEveModel = new ProvvedimentoSigeEventoModel(lProvvedimento);

				// Ricerca Evento Notifica
				lEvDAO = new EventoDAO(lConn);
				if (lProvvEveModel != null && lProvvEveModel.getProvvedimento() != null
						&& lProvvEveModel.getProvvedimento().getIdEventoGenerato() != null) {
					IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
					EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
							lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

					lProvvEveModel.setEventoNotifica(lEveNotifica);
				}
				// cleanup(lEvDAO);

				// Lettura Eventuali Motivi Provvedimento Sige.
				lMotivazioni = ricercaMotivazioni(lProvvedimento.getIdProvvedimentoSige(),
						lProvvedimento.getCodTipoProvvedimentoSige(), lConn);
				if (lMotivazioni != null && lProvvEveModel != null)
					lProvvEveModel.setMotiviProvvedSige(lMotivazioni);

				impSqlDao = new ImpugnazioneSigeSqlDAO(lConn);
				impSqlDao.ricercaImpugnazioniByIdProvvTipoImp(lProvvedimento.getIdProvvedimentoSige(),
						ICostantiImpugnazioneSige.COD_TIPO_RICORSO);
				Vector<ImpugnazioneSigeModel> ricorsi = new Vector<ImpugnazioneSigeModel>(
						impSqlDao.getModels());
				impSqlDao.ricercaImpugnazioniByIdProvvTipoImp(lProvvedimento.getIdProvvedimentoSige(),
						ICostantiImpugnazioneSige.COD_TIPO_OPPOSIZIONE);
				Vector<ImpugnazioneSigeModel> opposizioni = new Vector<ImpugnazioneSigeModel>(
						impSqlDao.getModels());
				for (ImpugnazioneSigeModel opposizione : opposizioni) {
					opposizione.setProvvedimentoSige(lProvvEveModel);
					// recupero il Provvedimento Sige Generato
					if (opposizione.getIdProvvedimentoGenerato() != null) {
						// Ricerca Provvedimento SIGE
						// lProvDAO = new ProvvedimentoSigeDAO(lConn);
						lProvDAO.setIdProvvedimentoSige(opposizione.getIdProvvedimentoGenerato());
						lProvDAO.selByKey();
						lProvvGenerato = (ProvvedimentoSigeModel) lProvDAO.getModelByKey();
						lProvvGenerato.decodifica();

						if (lProvvGenerato != null) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("provvedimento generato: " + lProvvGenerato);
							lProvvGeneratoEveModel = new ProvvedimentoSigeEventoModel(lProvvGenerato);

							// Ricerca Evento Notifica
							lEvDAO = new EventoDAO(lConn);
							if (lProvvGeneratoEveModel != null
									&& lProvvGeneratoEveModel.getProvvedimento() != null
									&& lProvvGeneratoEveModel.getProvvedimento()
											.getIdEventoGenerato() != null) {
								IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
								EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
										lProvvGeneratoEveModel.getProvvedimento().getIdEventoGenerato(),
										lConn);

								lProvvGeneratoEveModel.setEventoNotifica(lEveNotifica);
							}
						}

						opposizione.setProvvedimentoSigeGenerato(lProvvGeneratoEveModel);
					}

				} // for (ImpugnazioneSigeModel opposizione : opposizioni){

				for (ImpugnazioneSigeModel ricorso : ricorsi) {
					ricorso.setProvvedimentoSige(lProvvEveModel);

					// recupero il Provvedimento Sige Generato
					if (ricorso.getIdProvvedimentoGenerato() != null) {
						// Ricerca Provvedimento SIGE
						// lProvDAO = new ProvvedimentoSigeDAO(lConn);
						lProvDAO.setIdProvvedimentoSige(ricorso.getIdProvvedimentoGenerato());
						lProvDAO.selByKey();
						lProvvGenerato = (ProvvedimentoSigeModel) lProvDAO.getModelByKey();
						lProvvGenerato.decodifica();

						if (lProvvGenerato != null) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("provvedimento generato: " + lProvvGenerato);
							lProvvGeneratoEveModel = new ProvvedimentoSigeEventoModel(lProvvGenerato);

							// Ricerca Evento Notifica
							lEvDAO = new EventoDAO(lConn);
							if (lProvvGeneratoEveModel != null
									&& lProvvGeneratoEveModel.getProvvedimento() != null
									&& lProvvGeneratoEveModel.getProvvedimento()
											.getIdEventoGenerato() != null) {
								IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
								EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
										lProvvGeneratoEveModel.getProvvedimento().getIdEventoGenerato(),
										lConn);

								lProvvGeneratoEveModel.setEventoNotifica(lEveNotifica);
							}
						}

						ricorso.setProvvedimentoSigeGenerato(lProvvGeneratoEveModel);
					}
				}

				lProvvEveModel.setRicorsi(ricorsi);
				lProvvEveModel.setOpposizioni(opposizioni);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentoById DAOException:  " + daoEx);
		} catch (Exception e) {
			throw new F3BException("ProvvedimentoSigeController.ExRicercaProvvedimentoById Exception:  " + e);
		} finally {
			cleanup(lProvDAO);
			cleanup(impSqlDao);
			cleanup(lConn);
			cleanup(lEvDAO);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExRicercaProvvedimentoById: fine");

		return lProvvEveModel;
	}

	/**
	 * Ricerca del provvedimento legato ad uno specifico TENORE_SIGE. Se il provvedimento non esiste la
	 * funzione restituisce una Exception.
	 * 
	 * @param aIdTenoreSige
	 * @return ProvvedimentoSigeModel
	 * @throws F3BException
	 */
	public ProvvedimentoSigeModel ExRicercaProvedimentoByIdTenore(BigDecimal aIdTenoreSige)
			throws F3BException {
		Connection lConn = null;
		ProvvedimentoSigeDAO lProvDAO = null;
		ProvvedimentoSigeModel lProvvedimento = null;

		try {
			lConn = getDBConnection();
			lProvDAO = new ProvvedimentoSigeDAO(lConn);
			lProvDAO.selCondizioneByIdTenore(aIdTenoreSige);
			lProvvedimento = (ProvvedimentoSigeModel) lProvDAO.getModelByKey();
			if (lProvvedimento == null)
				throw new F3BException(F3BException.EX_NOT_FOUND, "Provvedimento assente !");
			lProvvedimento.decodifica();
		} catch (F3BException daoEx) {
			throw daoEx;
		} catch (Exception e) {
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvDefinitorioByFasc Exception:  " + e);
		} finally {
			cleanup(lProvDAO);
			cleanup(lConn);
		}
		return lProvvedimento;
	}

	/**
	 * Esecuzione stampa Ordinanza.
	 * <p>
	 * 
	 * @param aIdFascicolo
	 * @param lProvEvento
	 * @param lTipoUfficio
	 * @param aUtenteModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaProvvedimento(EventoModel lEvento, BigDecimal aIdFascicolo,
			String lTipoUfficio, UtenteModel aUtenteModel) throws F3BException {
		ByteArrayOutputStream lByteArrayOut = null;

		IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();
		// Riempie l'Array contenente le tipologie di dati da prelevare
		int[] aTipoDati = { ICostantiStampaSige.TREE_SOGGETTO, ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO,
				ICostantiStampaSige.TREE_PROVVEDIMENTO, ICostantiStampaSige.TREE_FASCICOLOSIEP,
				ICostantiStampaSige.TREE_SENTENZA, ICostantiStampaSige.TREE_AVVOCATO,
				ICostantiStampaSige.TREE_LUOGODET, ICostantiStampaSige.TREE_MAGISTRATO,
				ICostantiStampaSige.TREEs_PROVVEDIMENTI, ICostantiStampaSige.TREE_TIT_ESE_REF,
				ICostantiStampaSige.TREE_UDIENZA };
		int aTipoStampa = ICostantiStampaSige.STAMPA_PROVVEDIMENTO;

		TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa(aIdFascicolo, aTipoDati, aTipoStampa, lTipoUfficio,
				null);

		// ReportGenerator lReport = new ReportGenerator();
		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

		String lIdTemplate = lEvento.getTemIdTemplate();
		String lNomeTemplate = "";
		if (lIdTemplate.length() == 0)
			lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIGE_OR_001");
		else
			lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>>> Generato il Documento .");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("EVENTO >>> " + lEvento.toString());
		// Si imposta il ByteArrayInput ovverro il doc generato nell'evento
		// precisamente nel attributo DocBlobIn.
		lEvento.setDocBlobIn(lByteArrayInput);

		// Inserisce il documento generato nel model di ritorno
		// In esso inserisce il Nome del template di ritorno
		// e il documento generato.

		Connection lConn = null;
		EventoDAO lEveDao = null;

		try {
			// Preleva connessione dal Db
			lConn = getDBConnection();

			// Prepara un EventoDAO
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(lEvento);

			// Seleziona le condizioni di Update
			lEveDao.selCondizioneUpdate(lEvento.getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);
			throw new F3BException("ProvvedimentoSigeController.ExStampaProvvedimento: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + ex);
			throw new F3BException("ProvvedimentoSigeController.ExStampaProvvedimento: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Inserisce la data di deposito del provvedimento, aggiorna l'evento e inserisce una notifica per ogni
	 * destinatario.
	 * 
	 * @param aFasSige
	 * @param aProvvedimentoSige
	 * @param aEveNot
	 * @param lCheck
	 * @return ProvvedimentoSigeEventoModel
	 * @throws F3BException
	 */
	public DocumentoAllegatoModel ExInserisciDataDeposito(FascicoloSigeModel aFasSige,
			ProvvedimentoSigeEventoModel aProvvedimento, EventoNotificaModel aEveNot, String[] lCheck)
			throws F3BException {
		Connection lConn = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		ProvvedimentoSigeDAO lProvDao = null;
		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDao = null;
		DocumentoAllegatoModel lDocAMod = null;
		FascicoloSigeDAO lFasSigeDao = null;

		ProvvedimentoSigeModel lProvMod = new ProvvedimentoSigeModel(aProvvedimento.getProvvedimento());

		try {
			// Update di Provvedimento.
			lConn = getDBTransaction();
			lProvDao = new ProvvedimentoSigeDAO(lConn);

			// Il campo Chiave_Progr viene valorizzato con l'ultimo valore presente + 1, contestualmente al
			// tipo provvedimento.
			// Trovo il valore da assegnare al progressivo CHIAVE_PROGR.
			if (lProvMod.getChiaveProgr() == null) {
				// Si valorizza l'Anno Corrente perche' il progressivo e' riferito all'anno
				lProvMod.setChiaveAnno(new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));

				lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
				lProvSqlDao.getProgressivo(lProvMod);

				lProvSqlDao.start();

				BigDecimal lBigDec = new BigDecimal(0);
				if (lProvSqlDao.next() && (lProvSqlDao.getBigDecimal("aMAX") != null))
					lBigDec = lProvSqlDao.getBigDecimal("aMAX");
				lProvSqlDao.stop();

				if (lBigDec == null)
					lBigDec = new BigDecimal(0);

				// Setto 'Chiave_Progr' del Model di Provvedimento con il MAX + 1
				lProvMod.setChiaveProgr(new BigDecimal(lBigDec.intValue() + 1));

				// Aggiornamento ANNO e PROGR solo al primo inserimento
				lProvDao.setChiaveAnno(lProvMod.getChiaveAnno());
				lProvDao.setChiaveProgr(lProvMod.getChiaveProgr());
			}

			// Setto il DAO dal Model di Provvedimento per l'Update

			// Effettuo l'inserimento data deposito in ProvvedimentoModel; carico i dati da aggiornare.
			lProvDao.setCodUfficioAggiornamento(lProvMod.getCodUfficioAggiornamento());
			lProvDao.setCodOperatoreAggiornamento(lProvMod.getCodOperatoreAggiornamento());
			lProvDao.setDataAggiornamento(lProvMod.getDataAggiornamento());
			lProvDao.setDataDeposito(lProvMod.getDataDeposito());
			lProvDao.selCondizioneByKey(lProvMod.getIdProvvedimentoSige());

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Fase di aggiornamento del Provvedimento SIGE (Data Deposito)");
			lProvDao.update();

			// Update di Evento.
			EventoNotificaModel lEveNot = new EventoNotificaModel(aEveNot);
			EventoModel lEveMod = new EventoModel(lEveNot.getEvento());
			// STUB: Se poi bisogna trasferire l'evento tocca settare i flag x SIEP
			lEveMod.setFlagVideoSiep("S");
			lEveMod.setFlagStampaSiep("S");

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(lEveMod);
			lEveDao.update();

			// Fase di Insert del Documento Allegato.

			// Occorre cancellare eventuali DocumentiAllegati preesistenti
			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setCondizioneDelete(lEveMod.getIdEvento(), lProvMod.getCodTipoProvvedimento());
			lDocAllDao.delete();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>> Cancellati doc allegati collegati a Evento "+lEveMod.getIdEvento());

			// Setto il NumeroProgressivo del Model di DocumentoAllegato con il MAX + 1 (per Uff. Inserimento
			// ed IdEvento).
			lDocAMod = new DocumentoAllegatoModel();
			lDocAMod.setCodUfficioInserimento(lProvMod.getCodUfficioAggiornamento());
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAllSqlDao = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal lBigDecAll = lDocAllSqlDao.getProgressivo(lDocAMod);

			lDocAMod.setNumeroProgressivo(new BigDecimal(lBigDecAll.intValue() + 1));
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAMod.setDataEmissione(lProvMod.getDataDeposito());
			lDocAMod.setCodTipoDocumento(lProvMod.getCodTipoProvvedimento()); // Codifica di
																				// COD_TIPO_DOCUMENTO_ALLEGATO
																				// = Deposito Provvedimento
			lDocAMod.setFlagDocumentoRegistrato("N");
			// lDocAMod.setDocBlobIn();
			lDocAMod.setCodUfficioInserimento(lProvMod.getCodUfficioAggiornamento());
			lDocAMod.setCodOperatoreInserimento(lProvMod.getCodOperatoreAggiornamento());
			lDocAMod.setDataInserimento(lProvMod.getDataAggiornamento());
			if (aProvvedimento.getProvvedimento().getCodTipoProvvedimento().trim()
					.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA) == 0)
				lDocAMod.setTemIdTemplate("SIGE_OR_002");
			else if (aProvvedimento.getProvvedimento().getCodTipoProvvedimento().trim()
					.compareTo(ICostantiProvvedimentoSige.COD_DECRETO_GENERICO) == 0)
				lDocAMod.setTemIdTemplate("SIGE_DE_004");
			else if (aProvvedimento.getProvvedimento().getCodTipoProvvedimento().trim()
					.compareTo(ICostantiProvvedimentoSige.COD_ISTRUTTORIE) == 0)
				lDocAMod.setTemIdTemplate("SIGE_IS_011");
			else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Errore! tipo provvedimento privo di Template : "
								+ aProvvedimento.getProvvedimento().getCodTipoProvvedimento());

			lDocAllDao.setDAOFromModel(lDocAMod);
			lDocAMod.setIdDocumentoAllegato(lDocAllDao.insert());

			// Cancellazione delle notifiche preesistenti per le notifiche selezionate.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Cancellazione Notifiche ");
			lNotDao = new NotificaDAO(lConn);
			if (lCheck != null) {
				for (int z = 0; z < lCheck.length; z++) {
					lNotDao.start();
					lNotDao.setCondizioneUpdate((BigDecimal) new BigDecimal(lCheck[z].toUpperCase()));
					lNotDao.delete();
					lNotDao.stop();
				}
			}
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Fine Cancellazione Notifiche. ");

			// Insert delle Notifiche.
			lAutDao = new AutoritaEsternaDAO(lConn);

			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (lEveNot.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + lEveNot.getNotifiche().length + " notifiche");

				while (count < lEveNot.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + lEveNot.getNotifiche()[count]);

					if (lEveNot.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(lEveNot.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(lEveNot.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					lNotDao.setDAOFromModel(lEveNot.getNotifiche()[count]);

					lNotDao.insert();
					lNotDao.stop();

					count++;
				}
			}

			// Aggiorno il fascicolo con stato_fascicolo = 07
			lFasSigeDao = new FascicoloSigeDAO(lConn);
			lFasSigeDao.setDAOFromModelForUpdate(aFasSige);
			// Lo stato del fascicolo cambia se il Provvedimento e' di tipo definitorio
			if (aProvvedimento.getProvvedimento().getDefinitorio().compareToIgnoreCase("S") == 0) {
				lFasSigeDao.setCodStatoFascicolo(ICostantiFascicoloSige.COD_EMESSO_PROVVEDIMENTO);
				lFasSigeDao.setDataDefinizione(lProvMod.getDataDeposito());
				lFasSigeDao.update();
				lFasSigeDao.stop();
			}

			commit(lConn);
		}

		catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciDataDeposito: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciDataDeposito: " + ex);
		} finally {
			cleanup(lProvDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lDocAllDao);
			cleanup(lDocAllSqlDao);
			cleanup(lProvDao);

			cleanup(lConn);
		}
		// Restituito il model del documento allegato inserito.
		return lDocAMod;
	}

	/**
	 * Ricerca il Provvedimento ed Evento collegato attraverso la chiave IdProvvedimento.
	 * 
	 * @param aIdFasSige
	 * @return
	 * @throws F3BException
	 */
	public ProvvedimentoSigeEventoModel ExRicercaProvvedimentoByIdEvento(BigDecimal aIdEvento)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExRicercaProvvedimentoByIdEvento: inizio");

		Connection lConn = null;
		ProvvedimentoSigeDAO lProvDAO = null;
		ProvvedimentoSigeModel lProvvedimento = null;
		EventoDAO lEvDAO = null;
		ProvvedimentoSigeEventoModel lProvvEveModel = null;
		MotivazioneProvvedimentoSigeSqlDAO lMPSDao = null;
		Vector lMotivazioni = null;
		ImpugnazioneSigeSqlDAO impSqlDao = null;
		try {
			lConn = getDBConnection();
			lProvDAO = new ProvvedimentoSigeDAO(lConn);
			lProvDAO.setIdEventoGenerato(aIdEvento);
			lProvDAO.selCondizioneByIdEvento(aIdEvento);
			lProvvedimento = (ProvvedimentoSigeModel) lProvDAO.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ricerca provv effettuata");

			if (lProvvedimento != null) {
				lProvvedimento.decodifica();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("provvedimento: " + lProvvedimento);
				lProvvEveModel = new ProvvedimentoSigeEventoModel(lProvvedimento);

				if (lProvvEveModel != null && lProvvEveModel.getProvvedimento() != null
						&& lProvvEveModel.getProvvedimento().getIdEventoGenerato() != null) {
					lEvDAO = new EventoDAO(lConn);

					IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
					EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
							lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);
					lProvvEveModel.setEventoNotifica(lEveNotifica);
					cleanup(lEvDAO);
				}
				// Lettura Eventuali Motivi Provvedimento Sige.
				lMotivazioni = ricercaMotivazioni(lProvvedimento.getIdProvvedimentoSige(),
						lProvvedimento.getCodTipoProvvedimentoSige(), lConn);
				if (lMotivazioni != null && lProvvEveModel != null)
					lProvvEveModel.setMotiviProvvedSige(lMotivazioni);

				impSqlDao = new ImpugnazioneSigeSqlDAO(lConn);
				impSqlDao.ricercaImpugnazioniByIdProvvTipoImp(lProvvedimento.getIdProvvedimentoSige(), "01");
				Vector<ImpugnazioneSigeModel> ricorsi = new Vector<ImpugnazioneSigeModel>(
						impSqlDao.getModels());
				impSqlDao.ricercaImpugnazioniByIdProvvTipoImp(lProvvedimento.getIdProvvedimentoSige(), "04");
				Vector<ImpugnazioneSigeModel> opposizioni = new Vector<ImpugnazioneSigeModel>(
						impSqlDao.getModels());

				lProvvEveModel.setRicorsi(ricorsi);
				lProvvEveModel.setOpposizioni(opposizioni);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentoByIdEvento DAOException:  " + daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentoByIdEvento Exception:  " + e);
		} finally {
			cleanup(lProvDAO);
			cleanup(lMPSDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExRicercaProvvedimentoByIdEvento: fine");

		return lProvvEveModel;
	}

	/**
	 * Stampa il documento allegato al Provvedimento
	 * 
	 * @param aIdFascicoloSige
	 * @param aDAMod
	 * @param aCodUff
	 * @return lByteArrayOut
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumentoAllegato(BigDecimal aIdFascicolo,
			DocumentoAllegatoModel aDAMod, String aCodUff, UtenteModel aUtenteModel, BigDecimal idEvento)
			throws F3BException {
		Connection lConn = null;
		DocumentoAllegatoDAO lDADao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			// Generazione documento di stampa
			IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();
			// lByteArrayOut = lCtrlSt.ExPreStampaAllegato(aIdFascicoloSius, aDAMod, aCodUff, aUtenteModel );

			// Riempie l'Array contenente le tipologie di dati da prelevare
			int[] aTipoDati = { ICostantiStampaSige.TREE_SOGGETTO,
					ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO, ICostantiStampaSige.TREE_PROVVEDIMENTO,
					ICostantiStampaSige.TREE_FASCICOLOSIEP, ICostantiStampaSige.TREE_SENTENZA,
					ICostantiStampaSige.TREE_AVVOCATO, ICostantiStampaSige.TREE_LUOGODET,
					ICostantiStampaSige.TREE_MAGISTRATO, ICostantiStampaSige.TREEs_PROVVEDIMENTI,
					ICostantiStampaSige.TREE_TIT_ESE_REF, ICostantiStampaSige.TREE_UDIENZA };
			int aTipoStampa = ICostantiStampaSige.STAMPA_ALLEGATO;

			TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa(aIdFascicolo, aTipoDati, aTipoStampa, aCodUff,
					idEvento);

			// ReportGenerator lReport = new ReportGenerator();
			ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

			String lNomeTemplate = "";
			if (aDAMod.getCodTipoDocumento().trim()
					.compareTo(ICostantiProvvedimentoSige.COD_DECRETO_GENERICO) == 0)
				lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIGE_DE_004");
			else if (aDAMod.getCodTipoDocumento().trim()
					.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA) == 0)
				lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIGE_OR_002");
			else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Errore! tipo Documento Allegato privo di Template : "
								+ aDAMod.getCodTipoDocumento());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DOCUMENTO_ALLEGATO >>> " + aDAMod.toString());

			aDAMod.setDocBlobIn(lByteArrayInput);

			lConn = getDBConnection();
			lDADao = new DocumentoAllegatoDAO(lConn);
			lDADao.setDAOFromModelForUpdateBlob(aDAMod);

			lDADao.setCondizioneUpdate(aDAMod.getIdDocumentoAllegato());
			lDADao.update();
			commit(lConn);

		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqe);
			throw new F3BException("ProvvedimentoController.ExStampaDocumentoAllegato: " + sqe);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException("ProvvedimentoController.ExStampaDocumentoAllegato: " + sqe);
		} finally {
			cleanup(lDADao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Esegue la cancellazione di un record di Provvedimento SIGE.
	 * <p>
	 * 
	 * @param aProvvedimento
	 *            : model ProvvedimentoSige in cui siano valorizzati almeno i campi IDProvvedimentoSige e
	 *            IDEventoGenerato.
	 * @throws Exception
	 */

	public void ExCancellaProvvedimentoSige(ProvvedimentoSigeModel aProvvedimento, Connection aConn)
			throws Exception {

		ProvvedimentoSigeDAO lProvDao = null;
		TenoreSentenzaReatoDAO lTenSenReaDAO = null;
		TenoreSigeDAO lTenDao = null;
		EventoDAO lEveDao = null;

		if (aProvvedimento == null)
			throw new SIGEException("Provvedimento nullo");

		BigDecimal lIdProvvedimento = aProvvedimento.getIdProvvedimentoSige();
		if (lIdProvvedimento == null)
			throw new SIGEException("ID Provvedimento nullo");

		BigDecimal lIdEvento = aProvvedimento.getIdEventoGenerato();
		if (lIdEvento == null)
			throw new SIGEException("ID Evento nullo");

		try {
			// 20/07/2009 Cancellazione record in tabella di relazione TENORE_SENTENZA_REATO collegati
			// attraverso tenori
			lTenSenReaDAO = new TenoreSentenzaReatoDAO(aConn);
			lTenSenReaDAO.selCondizioneDeleteProvvedimento(aProvvedimento.getIdProvvedimentoSige());
			lTenSenReaDAO.delete();
			lTenSenReaDAO.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DELETE TENORE_SENTENZA_REATO");

			// 13/01/2011 Se si sta cancellando un provvedimento con TENORE_SIGE valido/i (quindi con
			// DATA_FINE = null)
			// occorrera' operare il ripristino dei tenori del provvedimento precedente. A tale scopo bisogna
			// leggere un TENORE_SIGE prima di cancellarlo/i.
			boolean lDataFineNull = false;
			TenoreSigeModel lTenCanc = new TenoreSigeModel();
			lTenDao = new TenoreSigeDAO(aConn);
			lTenDao.selCondizioneIdProvvedimento(aProvvedimento.getIdProvvedimentoSige());
			lTenDao.start();
			if (lTenDao.next()) {
				lTenCanc = (TenoreSigeModel) lTenDao.getModel();
				if (lTenCanc != null && lTenCanc.getIdTenoreSige() != null && lTenCanc.getDataFine() == null)
					lDataFineNull = true;
			}
			lTenDao.stop();

			// 20/07/2009 Cancellazione record tenori collegati in TENORE_SIGE
			lTenDao = new TenoreSigeDAO(aConn);
			lTenDao.selCondizioneIdProvvedimento(aProvvedimento.getIdProvvedimentoSige());
			lTenDao.delete();
			lTenDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DELETE TENORE_SIGE");

			// 13/01/2011 condizionato l'aggiornamento dei TENORE_SIGE se quelli appena cancellati avevano
			// DATA_FINE = null.
			if (lDataFineNull == true) {
				// 20/07/2009 Ripristino records tenori storicizzati al momento dall'emissione del
				// provvedimento
				// che si sta cancellando.
				lTenDao = new TenoreSigeDAO(aConn);
				// 04/01/2011 lTenDao.setCodEsitoSige(null);
				lTenDao.setDataFine(null);
				lTenDao.setNote("");
				lTenDao.setDataAggiornamento(aProvvedimento.getDataAggiornamento());
				lTenDao.setCodOperatoreAggiornamento(aProvvedimento.getCodOperatoreAggiornamento());
				lTenDao.setCodUfficioAggiornamento(aProvvedimento.getCodUfficioAggiornamento());
				lTenDao.setDAOUpdateTenoriForDeleteProvvedimento(aProvvedimento);
				lTenDao.update();
				lTenDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("UPDATE TENORE_SIGE (RIPRISTINO)");
				// 24/01/2011 Modifica Data Aggiornamento dei records tenori storicizzati al momento
				// dall'emissione del provvedimento
				// che si sta cancellando.
			} else if (lTenCanc.getDataAggiornamento() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("UPDATE TENORE_SIGE (Ricollegamento DATA_FINE)");
				lTenDao = new TenoreSigeDAO(aConn);
				lTenDao.setDataFine(lTenCanc.getDataFine());
				lTenDao.setDataAggiornamento(aProvvedimento.getDataAggiornamento());
				lTenDao.setCodOperatoreAggiornamento(aProvvedimento.getCodOperatoreAggiornamento());
				lTenDao.setCodUfficioAggiornamento(aProvvedimento.getCodUfficioAggiornamento());
				lTenDao.setDAOUpdateTenoriForDeleteProvvedimento(aProvvedimento);
				lTenDao.update();
				lTenDao.stop();
			}

			// cancellazione Provvedimento
			lProvDao = new ProvvedimentoSigeDAO(aConn);
			lProvDao.selCondizioneByKey(aProvvedimento.getIdProvvedimentoSige());
			lProvDao.delete();

			// Istanzia EventoDAO
			lEveDao = new EventoDAO(aConn);

			// Cancellazione dei riferimenti in tabella Evento al record cancellato
			EventoModel lEvento = new EventoModel();
			lEvento.setCodOperatoreAggiornamento(aProvvedimento.getCodOperatoreAggiornamento());
			lEvento.setCodUfficioAggiornamento(aProvvedimento.getCodUfficioAggiornamento());
			lEvento.setDataAggiornamento(aProvvedimento.getDataAggiornamento());
			lEvento.setIdEvento(lIdEvento);
			// Cancellazione ai riferimenti tramite EVE_ID_EVENTO E EVE_ID_EVENTO_REVOCA
			lEveDao.updateDAOFromModelForResetRifEve(lEvento);

			// cancellazione Evento collegato al Decreto
			lEveDao.selCondizioneUpdate(lIdEvento);
			lEveDao.delete();
			lEveDao.stop();
		} catch (Exception e) {
			throw e;
		} finally {
			cleanup(lTenDao);
			cleanup(lProvDao);
			cleanup(lEveDao);
		}
		return;
	}

	/**
	 * 
	 * @param aEventoNotifiche
	 * @param aProvvedimento
	 * @return
	 * @throws F3BException
	 */
	public ProvvedimentoSigeModel ExInserisciEventoNotificaProv(EventoNotificaModel aEventoNotifiche,
			ProvvedimentoSigeModel aProvvedimento) throws F3BException {
		// Provvedimento inserito
		ProvvedimentoSigeModel lProvvedimento = new ProvvedimentoSigeModel(aProvvedimento);

		Connection lConn = null;
		ProvvedimentoSigeDAO lProvDao = null;

		try {
			lConn = getDBConnection();
			lProvDao = new ProvvedimentoSigeDAO(lConn);

			// Inserimento Evento e Notifiche
			IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNot = lEveCtrl.ExInserisciEventoNotifica(aEventoNotifiche, lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciEventoProvvedimento : Inserito Evento -> " + lEveNot.getEvento());

			lProvvedimento.setIdEventoGenerato(lEveNot.getEvento().getIdEvento());

			// Inserimento Provvedimento.
			lProvDao.setDAOFromModel(lProvvedimento);
			BigDecimal lIdProvvedimento = lProvDao.insert();
			lProvDao.stop();
			lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciEventoNotificaProv : Inserito Provvedimento -> " + lIdProvvedimento);

			// COMMIT
			commit(lConn);
		}

		catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciEventoNotificaProv : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciEventoNotificaProv -> " + e);
		} finally {
			cleanup(lProvDao);
			cleanup(lConn);
		}
		return lProvvedimento;
	}

	/**
	 * Description: Funzione di ricerca dei provvedimenti SIGE.
	 * </p>
	 * Parametro di ricerca: Id_FascicoloSige.
	 * 
	 * @param aIdFasSige
	 * @throws F3BException
	 * @return Vector di ProvvedimentoSigeEventoModel
	 */
	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvedimentiSigePerIdFasSige(BigDecimal aIdFasSige)
			throws F3BException {
		Connection lConn = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		TenoreSigeSqlDAO lTenSqlDao = null;

		ProvvedimentoSigeEventoModel lProvvEveModel = null;

		Vector<ProvvedimentoSigeEventoModel> lProvvedimentiSige = new Vector<ProvvedimentoSigeEventoModel>();
		Vector<TenoreSigeEstesoModel> lTenoriSige = null;

		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			lProvSqlDao.ricercaProvvedimentiSigePerIdFasSige(aIdFasSige);
			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(lProvvEveModel.getProvvedimento().getIdEventoGenerato());

				EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
				if (lEveMod == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Errore! Evento non trovato per il provvedimento : "
									+ lProvvEveModel.getProvvedimento().getIdProvvedimentoSige());

				// Individuazione della presenza di documenti allegati
				int lNumAllegati = -1;
				int lNumValidati = -1;
				lNumAllegati = lEveSqlDao.getNumDocumentiAllegati(lEveMod.getIdEvento(), null);
				lEveMod.setNumAllegati(lNumAllegati);
				if (lNumAllegati > 0) {
					lNumValidati = lEveSqlDao.getNumAllegatiValidati(lEveMod.getIdEvento());
					lEveMod.setNumAllValidati(lNumValidati);
				}

				lProvvEveModel.getEventoNotifica().setEvento(lEveMod);

				// Ricerca tenori legati al provvedimento
				TenoreSigeModel lTenore = new TenoreSigeModel();
				lTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());

				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());
				lTenSqlDao = new TenoreSigeSqlDAO(lConn);
				lTenSqlDao.ricercaTenoriSige(aTenore);
				lTenoriSige = new Vector(lTenSqlDao.getModels());

				lProvvEveModel.setTenoriEstesi(lTenoriSige);

				lProvvedimentiSige.add(lProvvEveModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentiSigePerIdFasSige: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentiSigePerIdFasSige: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
		return lProvvedimentiSige;
	}

	/**
	 * Description: Funzione di ricerca dei provvedimenti SIGE.
	 * </p>
	 * Parametro di ricerca: Id_FascicoloSige.
	 * 
	 * @param aIdFasSige
	 * @throws F3BException
	 * @return Vector di ProvvedimentoSigeEventoModel
	 */
	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvedimentiSigePerIdFasSigePerIdSoggetto(
			BigDecimal aIdSoggettoSige, String codTipoProvvedimento) throws F3BException {
		Connection lConn = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		TenoreSigeSqlDAO lTenSqlDao = null;

		ProvvedimentoSigeEventoModel lProvvEveModel = null;

		Vector<ProvvedimentoSigeEventoModel> lProvvedimentiSige = new Vector<ProvvedimentoSigeEventoModel>();
		Vector<TenoreSigeEstesoModel> lTenoriSige = null;

		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			lProvSqlDao.ricercaProvvedimentiSigePerIdFasSigePerIdSoggetto(aIdSoggettoSige,
					codTipoProvvedimento);
			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(lProvvEveModel.getProvvedimento().getIdEventoGenerato());

				EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
				if (lEveMod == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Errore! Evento non trovato per il provvedimento : "
									+ lProvvEveModel.getProvvedimento().getIdProvvedimentoSige());

				// Individuazione della presenza di documenti allegati
				int lNumAllegati = -1;
				int lNumValidati = -1;
				lNumAllegati = lEveSqlDao.getNumDocumentiAllegati(lEveMod.getIdEvento(), null);
				lEveMod.setNumAllegati(lNumAllegati);
				if (lNumAllegati > 0) {
					lNumValidati = lEveSqlDao.getNumAllegatiValidati(lEveMod.getIdEvento());
					lEveMod.setNumAllValidati(lNumValidati);
				}

				lProvvEveModel.getEventoNotifica().setEvento(lEveMod);

				// Ricerca tenori legati al provvedimento
				TenoreSigeModel lTenore = new TenoreSigeModel();
				lTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());

				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());
				lTenSqlDao = new TenoreSigeSqlDAO(lConn);
				lTenSqlDao.ricercaTenoriSige(aTenore);
				lTenoriSige = new Vector(lTenSqlDao.getModels());

				lProvvEveModel.setTenoriEstesi(lTenoriSige);

				lProvvedimentiSige.add(lProvvEveModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentiSigePerIdFasSige: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentiSigePerIdFasSige: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
		return lProvvedimentiSige;
	}

	/**
	 * Description: Funzione di ricerca dei provvedimenti di sospensione per un provvedimento SIGE.
	 * </p>
	 * Parametro di ricerca: Provv_Id_Provvedimentosige.
	 * 
	 * @param aProvvId
	 * @throws F3BException
	 * @return Vector di ProvvedimentoSigeEventoModel
	 */
	public Vector ExRicercaOrdinanzaSospensioneSigeByProvvId(BigDecimal aProvvId) throws F3BException {
		Connection lConn = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		ProvvedimentoSigeModel lProvvModel = null;

		Vector lProvvedimentiSige = new Vector();

		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			lProvSqlDao.ricercaProvvedimentoSigeByKeySospensione(aProvvId);
			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				lProvvModel = new ProvvedimentoSigeModel();
				lProvvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvvModel.decodifica();
				lProvvedimentiSige.add(lProvvModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaOrdinanzaSospensioneSigeByProvvId: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaOrdinanzaSospensioneSigeByProvvId: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lConn);
		}
		return lProvvedimentiSige;
	}

	/**
	 * Description: Funzione di ricerca dei provvedimenti SIGE.
	 * </p>
	 * Parametro di ricerca: Id_FascicoloSige.
	 * 
	 * @param aIdFasSige
	 * @param aTipiProvvedimento
	 *            : e' una stringa del tipo "('02','03')" per consentire
	 * @throws F3BException
	 * @return Vector di ProvvedimentoSigeEventoModel
	 */
	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvSigePerIdFasSigeTipiProvv(BigDecimal aIdFasSige,
			String aTipiProvvedimento) throws F3BException {

		Connection lConn = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		TenoreSigeSqlDAO lTenSqlDao = null;
		ImpugnazioneSigeSqlDAO impSqlDao = null;

		ProvvedimentoSigeEventoModel lProvvEveModel = null;

		Vector<ProvvedimentoSigeEventoModel> lProvvedimentiSige = new Vector<ProvvedimentoSigeEventoModel>();

		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			lProvSqlDao.ricercaProvvSigePerIdFasSigeTipiProvv(aIdFasSige, aTipiProvvedimento);
			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(lProvvEveModel.getProvvedimento().getIdEventoGenerato());

				EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
				if (lEveMod == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Errore! Evento non trovato per il provvedimento : "
									+ lProvvEveModel.getProvvedimento().getIdProvvedimentoSige());

				// Individuazione della presenza di documenti allegati
				int lNumAllegati = -1;
				int lNumValidati = -1;
				lNumAllegati = lEveSqlDao.getNumDocumentiAllegati(lEveMod.getIdEvento(), null);
				lEveMod.setNumAllegati(lNumAllegati);
				if (lNumAllegati > 0) {
					lNumValidati = lEveSqlDao.getNumAllegatiValidati(lEveMod.getIdEvento());
					lEveMod.setNumAllValidati(lNumValidati);
				}

				lProvvEveModel.getEventoNotifica().setEvento(lEveMod);

				// Ricerca tenori legati al provvedimento
				TenoreSigeModel lTenore = new TenoreSigeModel();
				lTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());

				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());
				lTenSqlDao = new TenoreSigeSqlDAO(lConn);
				lTenSqlDao.ricercaTenoriSige(aTenore);
				Vector<TenoreSigeEstesoModel> lTenoriSige = new Vector<TenoreSigeEstesoModel>(
						lTenSqlDao.getModels());

				// 20190508 [SG]: recupero tenori legati al procedimento
				// if (lTenoriSige != null && lTenoriSige.size() == 0) {
				// TenoreSigeModel tsm = new TenoreSigeModel();
				// tsm.setFasIdFascicoloSige(aIdFasSige);
				// lTenSqlDao.ricercaTenoriSige(tsm);
				// } else
				// lTenSqlDao.ricercaTenoriSigeByIdfascicoloIdProvvedimento(aIdFasSige);
				// lTenoriSige.addAll(lTenSqlDao.getModels());

				lProvvEveModel.setTenoriEstesi(lTenoriSige);

				UdienzaProcedimentoSigeSqlDAO lUdiSqlDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
				lUdiSqlDao.ricercaUdienzaProcedimentoByEve(lEveNotifica.getEvento().getIdEvento());
				UdienzaProcedimentoSigeModel lUdiMod = (UdienzaProcedimentoSigeModel) lUdiSqlDao
						.getModelByKey();

				/*
				 * ISSUE MEV : Gestito il recupero dell'Udienza Sige nel caso di
				 * "Ordinanza Conflitto di Competenza" Numero MEV : 15_S4 Autore : sessa Data : 29/gen/2016
				 * Branch : MEV_15_S4
				 */
				if (lUdiMod != null || (lProvModel != null && lProvModel.getUdiIdUdienzaSige() != null)) {
					BigDecimal lIdUdienza = null;
					if (lUdiMod != null) {
						lIdUdienza = lUdiMod.getUdiIdUdienzaSige();
					} else if (lProvModel.getUdiIdUdienzaSige() != null
							&& lProvModel.getUdiIdUdienzaSige() != null) {
						lIdUdienza = lProvModel.getUdiIdUdienzaSige();
					}
					// ***** FINE INTERVENTO MEV_15_S4 *****//
					UdienzaSigeSqlDAO lUdienzaSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
					lUdienzaSigeSqlDao.ricercaUdienzaSigeByKey(lIdUdienza);
					UdienzaSigeModel udienzaSige = (UdienzaSigeModel) lUdienzaSigeSqlDao.getModelByKey();
					lProvvEveModel.getProvvedimento().setUdienzaSige(udienzaSige);
				}

				IDocumentoAllegato lCtrlDoc = SIGELookupRemote.getDocumentoAllegatoController();
				Vector<siap.sige.documentoallegato.model.DocumentoAllegatoModel> lDocs = lCtrlDoc
						.ExRicercaSollecitoByIdEvento(lEveNotifica.getEvento().getIdEvento());
				if (lDocs.size() > 0) {
					siap.sige.documentoallegato.model.DocumentoAllegatoModel sollecito = lDocs.elementAt(0);
					lProvvEveModel.setSollecito(sollecito);
				}

				impSqlDao = new ImpugnazioneSigeSqlDAO(lConn);
				impSqlDao.ricercaImpugnazioniByIdProvvTipoImp(lProvModel.getIdProvvedimentoSige(), "01");
				Vector<ImpugnazioneSigeModel> ricorsi = new Vector<ImpugnazioneSigeModel>(
						impSqlDao.getModels());
				impSqlDao.ricercaImpugnazioniByIdProvvTipoImp(lProvModel.getIdProvvedimentoSige(), "04");
				Vector<ImpugnazioneSigeModel> opposizioni = new Vector<ImpugnazioneSigeModel>(
						impSqlDao.getModels());

				lProvvEveModel.setRicorsi(ricorsi);
				lProvvEveModel.setOpposizioni(opposizioni);
				lProvvedimentiSige.add(lProvvEveModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvSigePerIdFasSigeTipiProvv: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvSigePerIdFasSigeTipiProvv: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
		return lProvvedimentiSige;
	}

	/**
	 * Description: Funzione di ricerca dei provvedimenti SIGE.
	 * </p>
	 * Parametro di ricerca: Id_FascicoloSige.
	 * 
	 * @param aIdFasSige
	 * @param aTipiProvvedimento
	 *            : e' una stringa del tipo "('02','03')" per consentire
	 * @param aCodTipoProvvSige
	 *            : e' una stringa
	 * @throws F3BException
	 * @return Vector di ProvvedimentoSigeEventoModel
	 */
	public Vector ExRicercaProvvSigePerIdFasSigeTipiProvvProvvSige(BigDecimal aIdFasSige,
			String aTipiProvvedimento, String aCodTipoProvvSige) throws F3BException {
		Connection lConn = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		TenoreSigeSqlDAO lTenSqlDao = null;

		ProvvedimentoSigeEventoModel lProvvEveModel = null;

		Vector lProvvedimentiSige = new Vector();
		Vector lTenoriSige = null;

		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			lProvSqlDao.ricercaProvvSigePerIdFasSigeTipiProvvProvvSige(aIdFasSige, aTipiProvvedimento,
					aCodTipoProvvSige);
			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(lProvvEveModel.getProvvedimento().getIdEventoGenerato());

				EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
				if (lEveMod == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Errore! Evento non trovato per il provvedimento : "
									+ lProvvEveModel.getProvvedimento().getIdProvvedimentoSige());

				// Individuazione della presenza di documenti allegati
				int lNumAllegati = -1;
				int lNumValidati = -1;
				lNumAllegati = lEveSqlDao.getNumDocumentiAllegati(lEveMod.getIdEvento(), null);
				lEveMod.setNumAllegati(lNumAllegati);
				if (lNumAllegati > 0) {
					lNumValidati = lEveSqlDao.getNumAllegatiValidati(lEveMod.getIdEvento());
					lEveMod.setNumAllValidati(lNumValidati);
				}

				lProvvEveModel.getEventoNotifica().setEvento(lEveMod);

				// Ricerca tenori legati al provvedimento
				TenoreSigeModel lTenore = new TenoreSigeModel();
				lTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());

				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());
				lTenSqlDao = new TenoreSigeSqlDAO(lConn);
				lTenSqlDao.ricercaTenoriSige(aTenore);
				lTenoriSige = new Vector(lTenSqlDao.getModels());

				lProvvEveModel.setTenoriEstesi(lTenoriSige);

				lProvvedimentiSige.add(lProvvEveModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvSigePerIdFasSigeTipiProvv: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvSigePerIdFasSigeTipiProvv: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
		return lProvvedimentiSige;
	}

	/**
	 * Aggiorna l'EVENTO nel campo FLAG_DOCUMENTO_REGISTRATO ( con 'A' ). Inserisce le motivazioni
	 * dell'annullamento in CAMPO_NOTA. Aggiorna eventualmente il FASCICOLO_SIGE nel campo COD_STATO_FASCICOLO
	 * ( con '02' = "Iscritto").
	 * 
	 * @param aCampoNota
	 * @param aIdFascicoloSige
	 * @param lTenori
	 * @throws F3BException
	 */
	public void ExAnnullaProvvedimento(CampoNotaModel aCampoNota, BigDecimal aIdFascicoloSige,
			ProvvedimentoSigeEventoModel aProvvSige, String codiceStatoFascicolo) throws F3BException {
		Connection lConn = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		CampoNotaDAO lCampoNotaDao = null;
		UdienzaProcedimentoSigeDAO lUdiProDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdiProSqlDao = null;
		TenoreSigeDAO lTenDao = null;
		ProvvedimentoSigeDAO provvDao = null;
		try {

			lConn = getDBTransaction();

			// Inserimento nuovo record CampoNota solo se la nota e' stata valorizzata.
			if (aCampoNota.getDescr() != null && aCampoNota.getDescr().trim().length() > 0) {

				lCampoNotaDao = new CampoNotaDAO(lConn);
				aCampoNota.setProgressivo(new BigDecimal(1));
				lCampoNotaDao.setDAOFromModel(aCampoNota);
				BigDecimal lIdCampoNota = lCampoNotaDao.insert();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Inserito Campo_NOTA : " + lIdCampoNota);
			}
			// Update Evento
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDataAggiornamento(aCampoNota.getDataInserimento());
			lEveDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
			lEveDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
			lEveDao.setFlagDocumentoRegistrato("A");

			lEveDao.selCondizioneUpdate(aCampoNota.getEveIdEvento());
			lEveDao.update();
			lEveDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Aggiornato Evento : " + aCampoNota.getEveIdEvento());

			// Nel caso in cui il Provvedimento sia di Revoca si eliminano i riferimenti in tabella Evento
			// al record annullato attraverso ripulendo EVE_ID_EVENTO_REVOCA.

			// Preparazione del model Evento
			EventoModel lEvento = new EventoModel();
			lEvento.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
			lEvento.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
			lEvento.setDataAggiornamento(aCampoNota.getDataInserimento());
			lEvento.setIdEvento(aCampoNota.getEveIdEvento());

			// Cancellazione riferimenti tramite EVE_ID_EVENTO_REVOCA
			lEveDao.setDAOFromModelForResetRifEveIdEventoRevoca(lEvento);
			lEveDao.update();
			lEveDao.stop();

			// Ricerca Evento modificato
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aCampoNota.getEveIdEvento());
			lEvento = (EventoModel) lEveSqlDAO.getModelByKey();

			if (lEvento == null || lEvento.getIdEvento() == null)
				throw new SIGEException("ERRORE in fase di Annullamento Provvedimento: ID Evento nullo");

			// Impostazione dati del Provvedimento SIGE per aggiornamento Tenori.
			ProvvedimentoSigeModel lProSige = new ProvvedimentoSigeModel(aProvvSige.getProvvedimento());
			lProSige.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
			lProSige.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
			lProSige.setDataAggiornamento(aCampoNota.getDataInserimento());
			lProSige.setDataDeposito(null);

			// Ricerca tenori del Provvedimento (devono corrispondere ai tenori attivi).
			TenoreSigeModel lTenore = new TenoreSigeModel();
			lTenore.setFasIdFascicoloSige(aIdFascicoloSige);
			ITenoreSige lCtrlTen = SIGELookupRemote.getTenoreSigeRemote();
			Vector lTenori = lCtrlTen.ExRicercaTenoreByProvvedimento(
					aProvvSige.getProvvedimento().getIdProvvedimentoSige(), lConn);

			// Controllo su data irrevocabilità Sentenze
			lCtrlTen.ExControlloDataIrrevocabilita(lTenori);

			// Aggiornamento Tenori effettuato dalla funzione
			// TenoreSigeController.ExAggiornaTenoriPerAnnullamento.
			boolean lDataFineNull = lCtrlTen.ExAggiornaTenoriPerAnnullamento(lProSige, lConn);

			// 13/01/2011 condizionato l'aggiornamento dei TENORE_SIGE se quelli appena cancellati avevano
			// DATA_FINE = null.
			if (lDataFineNull) {
				// 30/07/2009 Ripristino records tenori storicizzati al momento dall'emissione del
				// provvedimento
				// che si sta annullando.
				lTenDao = new TenoreSigeDAO(lConn);
				// 04/01/2011 lTenDao.setCodEsitoSige(null);
				lTenDao.setDataFine(null);
				lTenDao.setNote("");
				lTenDao.setDataAggiornamento(aCampoNota.getDataInserimento());
				lTenDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreAggiornamento());
				lTenDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				lTenDao.setDAOUpdateTenoriForDeleteProvvedimento(lProSige);
				lTenDao.update();
				lTenDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("UPDATE TENORE_SIGE (RIPRISTINO)");
			}

			// @emma 10/11/2016
			// se si sta annullando un provvedimento, controllare se il provvedimento definitorio del
			// fascicolo ha delle impugnazioni che hanno esito COD_ESITO_ANNULLA_CON_RINVIO="02", oppure
			// con esito COD_ESITO_CONVERTE_RICORSO_IN_OPPOSIZIONE="12" oppure con esito
			// COD_ESITO_ACCOGLIE_FISSA_UDIENZA="10"
			// lo stato del procedimento deve tornare su emessoProvvedimento ovvero "07"

			// faccio il controllo solo se lo stato del fascicolo è COD_ACCOGLIE_FISSA_UDIENZA(14)
			// oppure COD_ANNULLA_CON_RINVIO(15) oppure COD_RICORSO_CONVERTITO_OPPOSIZIONE(16)
			if (codiceStatoFascicolo.equals(ICostantiFascicoloSige.COD_ACCOGLIE_FISSA_UDIENZA)
					|| codiceStatoFascicolo.equals(ICostantiFascicoloSige.COD_ANNULLA_CON_RINVIO)
					|| codiceStatoFascicolo
							.equals(ICostantiFascicoloSige.COD_RICORSO_CONVERTITO_OPPOSIZIONE)) {
				boolean isStatoFascToChange = false;
				String[] esitiImp = new String[] { ICostantiImpugnazioneSige.COD_ESITO_ANNULLA_CON_RINVIO,
						ICostantiImpugnazioneSige.COD_ESITO_ACCOGLIE_FISSA_UDIENZA,
						ICostantiImpugnazioneSige.COD_ESITO_CONVERTE_RICORSO_IN_OPPOSIZIONE };

				FascicoloSigeUtils lFasUtil = new FascicoloSigeUtils();
				ProvvedimentoSigeEventoModel provvDefin = lFasUtil
						.getProvvDefinitorioConDepositoValidatoByFascicolo(aIdFascicoloSige);
				if (provvDefin != null && !provvDefin.getImpugnazioni().isEmpty()) {
					List<ImpugnazioneSigeModel> impugn = provvDefin.getImpugnazioni();
					for (Iterator iterator = impugn.iterator(); iterator.hasNext();) {
						ImpugnazioneSigeModel impugnazioneSigeModel = (ImpugnazioneSigeModel) iterator.next();
						if (Arrays.binarySearch(esitiImp,
								impugnazioneSigeModel.getCodTenoreDecisione()) >= 0) {
							isStatoFascToChange = true;
							break;
						}
					}
				}

				// // Recupero le impugnazioni dall'oggetto ProvvedimentoSigeEventoModel aProvvSige
				// Collection<ImpugnazioneSigeModel> opposizioni = aProvvSige.getOpposizioni();
				// opposizioni.addAll(aProvvSige.getRicorsi());
				// for (Iterator iterator = opposizioni.iterator(); iterator
				// .hasNext();) {
				// ImpugnazioneSigeModel impugnazioneSigeModel = (ImpugnazioneSigeModel) iterator
				// .next();
				// if(Arrays.binarySearch(esitiImp, impugnazioneSigeModel.getCodTenoreDecisione()) >= 0){
				// isStatoFascToChange =true;
				// break;
				// }
				// }

				// devo procedere con l'aggiornamento dello stato se isStatoFascToChange = true
				if (isStatoFascToChange) {
					// Aggiorno lo STATO_FASCICOLO con "07".
					aggiornaStatoFascicolo(aCampoNota, aIdFascicoloSige, lConn, lProSige, "07");
				}

			}
			// @emma -fine

			// Conteggio dei Provvedimenti riferiti al Fascicolo SIGE dopo l'annullamento.
			ProvvedimentoSigeSqlDAO lProvSqlDAO = new ProvvedimentoSigeSqlDAO(lConn);
			int lNumProvSIGEDepositati = lProvSqlDAO.getNumProvSIGEDEpositati(aIdFascicoloSige);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Num Provv. Depositati del Fascicolo SIGE: " + lNumProvSIGEDepositati);

			if (lNumProvSIGEDepositati < 1) {
				// Aggiorno lo STATO_FASCICOLO con "02".
				FascicoloSigeDAO lFasSigeDao = new FascicoloSigeDAO(lConn);
				lFasSigeDao.setCodStatoFascicolo("02");
				lFasSigeDao.setDataDefinizione(null);
				lFasSigeDao.setDataAggiornamento(aCampoNota.getDataInserimento());
				lFasSigeDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				lFasSigeDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				// solo nel caso di Definizione Manuale lo stato del Fascicolo Sige,
				// dopo l'annullamento, viene settato a '02' (iscritto)
				if (lProSige != null && lProSige.getCodTipoProvvedimentoSige() != null
						&& lProSige.getCodTipoProvvedimentoSige().equals("62")) {
					lFasSigeDao.setCondizioneUpdate(aIdFascicoloSige);
				} else {
					lFasSigeDao.setCondizioneUpdateStatoFascicolo(aIdFascicoloSige, "07");
				}
				lFasSigeDao.update();
				lFasSigeDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Aggiornamento Stato Fascicolo SIGE: " + aIdFascicoloSige);
				cleanup(lFasSigeDao);
			}

			// ------------------------------------------------------------------------
			// Gestione Aggiornamento Udienza Procedimento
			// Questa parte di codice viene eseguita esclusivamente quando si tratta
			// un'evento / ordinanza rinvio udienza ( 0603 )
			// ------------------------------------------------------------------------

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("**** Fase di Manipolazione Udienza Procedimento **** ");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("**** Valore EventoModel lEvento " + lEvento);

			if (lEvento.getCodEsito().equals("0603")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Trattasi di Udienza Procedimento ****");
				// Aggiornamento ultimo record rinvio udienza legato
				// all'evento
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Update Udienza Procedimento****");
				lUdiProDao = new UdienzaProcedimentoSigeDAO(lConn);
				lUdiProDao.setCondizioneByIdEvento(lEvento.getIdEvento());
				lUdiProDao.setCodOperatoreAggiornamento(lEvento.getCodOperatoreAggiornamento());
				lUdiProDao.setCodUfficioAggiornamento(lEvento.getCodUfficioAggiornamento());
				lUdiProDao.setFlagRinviata("A");
				lUdiProDao.update();
				lUdiProDao.stop();
				// ---//

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Select Udienza Procedimento SQL DAO ****");
				// Lettura dell'ultimo Record, attraverso l'aIdFascicoloSige
				UdienzaProcSigeUdienzaModel lUdiProUdi = null;
				lUdiProSqlDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
				lUdiProSqlDao.ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(aIdFascicoloSige,
						"'R','M'");
				lUdiProSqlDao.start();

				if (lUdiProSqlDao.next())
					lUdiProUdi = (UdienzaProcSigeUdienzaModel) lUdiProSqlDao.getModelConUdienza();

				lUdiProSqlDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** II Update Udienza Procedimento ****");
				// Update UDIENZA_PROCEDIMENTO.
				lUdiProDao.setFlagRinviata("F");
				lUdiProDao.setUdiIdUdienzaRinvio(null);
				lUdiProDao.setCondizioneUpdate(
						lUdiProUdi.getUdienzaProcedimento().getIdUdienzaProcedimentoSige());
				lUdiProDao.update();
				lUdiProDao.stop();

			}

			provvDao = new ProvvedimentoSigeDAO(lConn);
			lProSige.setDataAggiornamento(new Date());
			lProSige.setDataDeposito(null);
			lProSige.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
			lProSige.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
			provvDao.setDAOFromModelForUpdate(lProSige);
			provvDao.selCondizioneByKey(lProSige.getIdProvvedimentoSige());
			provvDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExAnnullaProvvedimento : " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExAnnullaProvvedimento : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lCampoNotaDao);
			cleanup(lUdiProDao);
			cleanup(lUdiProSqlDao);
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return;
	}

	/**
	 * Aggiorna lo stato del fascicolo sige con il codice passato in imput
	 * 
	 * @param aCampoNota
	 * @param aIdFascicoloSige
	 * @param lConn
	 * @param lProSige
	 * @param codiceStatoFascicolo
	 * @throws DAOException
	 * @throws F3BException
	 */
	private void aggiornaStatoFascicolo(CampoNotaModel aCampoNota, BigDecimal aIdFascicoloSige,
			Connection lConn, ProvvedimentoSigeModel lProSige, String codiceStatoFascicolo)
			throws DAOException, F3BException {
		FascicoloSigeDAO lFasSigeDao = new FascicoloSigeDAO(lConn);
		lFasSigeDao.setCodStatoFascicolo(codiceStatoFascicolo);
		lFasSigeDao.setDataDefinizione(null);
		lFasSigeDao.setDataAggiornamento(aCampoNota.getDataInserimento());
		lFasSigeDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
		lFasSigeDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
		lFasSigeDao.setCondizioneUpdate(aIdFascicoloSige);
		lFasSigeDao.update();
		lFasSigeDao.stop();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("Aggiornamento Stato Fascicolo SIGE: " + aIdFascicoloSige);
		cleanup(lFasSigeDao);
	}

	/**
	 * Esegue l'inserimento del Provvedimento SIGE. e (da decidere) l'aggiornamento del Fascicolo SIGE.
	 * <p>
	 * Description: Funzione per l'inserimento del Provvedimento SIGE.
	 * </p>
	 * Le tabelle coinvolte sono:
	 * <p>
	 * PROVVEDIMENTO_SIGE : viene inserito il nuovo record (ordinanza/decreto);
	 * <p>
	 * EVENTO : viene inserito un nuovo record;
	 * <p>
	 * TENORE : vengono chiusi i tenori attivi (data_fine) ed inseriti i nuovi tenori;
	 * <p>
	 * FASCICOLO_SIGE : ?? update dello stato del FASCICOLO.
	 * 
	 * @param IdFascicoloSige
	 * @param ProvvedimentoSigeEventoModel
	 * @param Vector
	 *            di Tenori
	 * @throws F3BException
	 * @return ProvvedimentoSigeEventoModel
	 */
	public ProvvedimentoSigeEventoModel ExInserisciProvvedimento(ProvvedimentoSigeEventoModel lProvEveModel,
			Vector lTenori, String aCodTipoGiudizio, MotivazioneProvvedimentoSigeModel[] lMotivazioni)
			throws Exception {
		// Models da inserire e valorizzare in return.
		ProvvedimentoSigeEventoModel lProvvSigeEvento = new ProvvedimentoSigeEventoModel();
		ProvvedimentoSigeModel lProvvedimento = new ProvvedimentoSigeModel(lProvEveModel.getProvvedimento());
		EventoModel lEvento = new EventoModel(lProvEveModel.getEventoNotifica().getEvento());

		Connection lConn = null;
		ProvvedimentoSigeDAO lProvDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		MotivazioneProvvedimentoSigeDAO lMPSDao = null;

		try {
			lConn = getDBConnection();
			lProvDao = new ProvvedimentoSigeDAO(lConn);
			lEveDao = new EventoDAO(lConn);

			// Si Setta l'Anno e il progressivo...
			lEveSqlDao = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEveSqlDao.getProgressivo(lEvento);
			lEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			// Inserimento Evento.
			lEveDao.setDAOFromModel(lEvento);
			BigDecimal lIdEvento = lEveDao.insert();
			lEveDao.stop();
			lEvento.setIdEvento(lIdEvento);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciProvvedimento : Inserito Evento -> " + lIdEvento);

			lProvvedimento.setIdEventoGenerato(lIdEvento);

			// Inserimento Provvedimento.
			lProvDao.setDAOFromModel(lProvvedimento);
			BigDecimal lIdProvvedimento = lProvDao.insert();
			lProvDao.stop();

			lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciProvvedimento : Inserito Provvedimento -> " + lIdProvvedimento);

			// Inserimento Motivi del Provvedimento.
			lMPSDao = new MotivazioneProvvedimentoSigeDAO(lConn);

			// Effettua l'inserimento delle Motivazioni
			int lSize = lMotivazioni.length;
			for (int lIndex = 0; lIndex < lSize; lIndex++) {
				// Imposta nel model il progressivo motivazione lIndex + 1.
				lMotivazioni[lIndex].setProgrMotivazione(new BigDecimal((double) lIndex + 1));
				lMPSDao.setDAOFromModel(lMotivazioni[lIndex]);
				lMPSDao.setProSigIdProvvedSige(lIdProvvedimento);
				lMPSDao.insert();
				lMPSDao.stop();
			}

			// Valorizzazione campi Tenori
			lTenori = aggiornaListaTenori(lTenori, lProvvedimento);

			// Controllo su data irrevocabilità Sentenze
			ITenoreSige lCtrlTen = SIGELookupRemote.getTenoreSigeRemote();
			lCtrlTen.ExControlloDataIrrevocabilita(lTenori);

			// Se valorizzato va aggiornato il Tipo Giudizio sul Fascicolo
			if (aCodTipoGiudizio != null)
				aggiornaTipoGiudizioFascicolo(lProvvedimento, aCodTipoGiudizio, lConn);

			// 05-06-2009 Impostazione Codice Esito Oggetto SIGE (Per Decreto di Inammissibilita',
			// Ordinanza Incompetenza e Ordinanza NDP/NLP non si validano gli oggetti).
			if (lProvvedimento.getCodTipoProvvedimentoSige()
					.compareTo(ICostantiProvvedimentoSige.COD_DECRETO_INAMMISSIBILITA) == 0
					|| lProvvedimento.getCodTipoProvvedimentoSige()
							.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_INCOMPETENZA) == 0
					|| lProvvedimento.getCodTipoProvvedimentoSige()
							.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_NDPNLP) == 0)
				this.aggiornaCodEsitoTenore(lProvvedimento.getCodTipoProvvedimentoSige(), lTenori, lConn);

			// Aggiornamento Tenori demandata alla funzione TenoreSigeController.ExInserisciOggetti.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciProvvedimento : Fase di chiusura per il Tenore");
			lCtrlTen.ExInserisciOggetti(lTenori, lProvvedimento.getFasIdFascicoloSige(), lConn);

			// COMMIT
			commit(lConn);
		}

		catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciProvvedimento : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciProvvedimento : " + e);
		} finally {
			cleanup(lProvDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lMPSDao);
			cleanup(lConn);
		}
		lProvvSigeEvento.setProvvedimento(lProvvedimento);
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.setEvento(lEvento);
		lProvvSigeEvento.setEventoNotifica(lEveNot);

		return lProvvSigeEvento;
	}

	/**
	 * Esegue l'inserimento del Provvedimento SIGE di Sospensione di altra ordinanza
	 * <p>
	 * Description: Funzione per l'inserimento del Provvedimento SIGE di Sospensione
	 * </p>
	 * Le tabelle coinvolte sono:
	 * <p>
	 * PROVVEDIMENTO_SIGE : viene inserito il nuovo record (ordinanza);
	 * <p>
	 * EVENTO : viene inserito un nuovo record;
	 * <p>
	 * TENORE : vengono inseriti i nuovi tenori senza storicizzare i precedenti;
	 * <p>
	 * FASCICOLO_SIGE : ?? update dello stato del FASCICOLO.
	 * 
	 * @param IdFascicoloSige
	 * @param ProvvedimentoSigeEventoModel
	 * @param Vector
	 *            di Tenori
	 * @throws F3BException
	 * @return ProvvedimentoSigeEventoModel
	 */
	public ProvvedimentoSigeEventoModel ExInserisciProvvSospensione(
			ProvvedimentoSigeEventoModel lProvEveModel, Vector lTenori, String aCodTipoGiudizio,
			MotivazioneProvvedimentoSigeModel[] lMotivazioni) throws Exception {
		// Models da inserire e valorizzare in return.
		ProvvedimentoSigeEventoModel lProvvSigeEvento = new ProvvedimentoSigeEventoModel();
		ProvvedimentoSigeModel lProvvedimento = new ProvvedimentoSigeModel(lProvEveModel.getProvvedimento());
		EventoModel lEvento = new EventoModel(lProvEveModel.getEventoNotifica().getEvento());

		Connection lConn = null;
		ProvvedimentoSigeDAO lProvDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		MotivazioneProvvedimentoSigeDAO lMPSDao = null;

		try {
			lConn = getDBConnection();
			lProvDao = new ProvvedimentoSigeDAO(lConn);

			// Inserimento Evento e Notifiche
			IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNot = lEveCtrl
					.ExInserisciEventoNotifica(lProvEveModel.getEventoNotifica(), lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciEventoProvvedimento : Inserito Evento -> " + lEveNot.getEvento());

			lProvvedimento.setIdEventoGenerato(lEveNot.getEvento().getIdEvento());

			// lProvvedimento.setIdEventoGenerato(lIdEvento);

			// Inserimento Provvedimento.
			lProvDao.setDAOFromModel(lProvvedimento);
			BigDecimal lIdProvvedimento = lProvDao.insert();
			lProvDao.stop();

			lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciProvvSospensione : Inserito Provvedimento -> " + lIdProvvedimento);

			// Inserimento Motivi del Provvedimento.
			lMPSDao = new MotivazioneProvvedimentoSigeDAO(lConn);

			// Effettua l'inserimento delle Motivazioni
			int lSize = lMotivazioni.length;
			for (int lIndex = 0; lIndex < lSize; lIndex++) {
				// Imposta nel model il progressivo motivazione lIndex + 1.
				lMotivazioni[lIndex].setProgrMotivazione(new BigDecimal((double) lIndex + 1));
				lMPSDao.setDAOFromModel(lMotivazioni[lIndex]);
				lMPSDao.setProSigIdProvvedSige(lIdProvvedimento);
				lMPSDao.insert();
				lMPSDao.stop();
			}

			// Valorizzazione campi Tenori in caso di Ordinanza di sospensione
			lTenori = aggiornaListaTenoriSospensione(lTenori, lProvvedimento);

			// Controllo su data irrevocabilità Sentenze
			ITenoreSige lCtrlTen = SIGELookupRemote.getTenoreSigeRemote();
			lCtrlTen.ExControlloDataIrrevocabilita(lTenori);

			// Se valorizzato va aggiornato il Tipo Giudizio sul Fascicolo
			if (aCodTipoGiudizio != null)
				aggiornaTipoGiudizioFascicolo(lProvvedimento, aCodTipoGiudizio, lConn);

			// Aggiornamento Tenori demandata alla funzione TenoreSigeController.ExInserisciOggetti.
			// La chiusura viene evitata se non si passa l'id fascicolo

			lCtrlTen.ExInserisciOggetti(lTenori, null, lConn);

			// COMMIT
			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciProvvSospensione : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciProvvSospensione : " + e);
		} finally {
			cleanup(lProvDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lMPSDao);
			cleanup(lConn);
		}
		lProvvSigeEvento.setProvvedimento(lProvvedimento);
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.setEvento(lEvento);
		lProvvSigeEvento.setEventoNotifica(lEveNot);

		return lProvvSigeEvento;
	}

	private Vector aggiornaCodEsitoTenore(String aCodTipoProvvedimento, Vector aListaTenori, Connection aConn)
			throws Exception {
		TenoreSigeSqlDAO lTenSqlDAO = new TenoreSigeSqlDAO(aConn);
		if (aListaTenori != null) {
			for (int j = 0; j < aListaTenori.size(); j++) {
				TenoreSigeModel lTenore = (TenoreSigeModel) aListaTenori.elementAt(j);

				lTenore.setCodEsitoSige(
						lTenSqlDAO.getCodEsitoTenoreSige(aCodTipoProvvedimento, lTenore.getCodOggettoSige()));

				aListaTenori.setElementAt(lTenore, j);
			}
		} // endif

		cleanup(lTenSqlDAO);
		return aListaTenori;
	}

	/**
	 * Inserisce la data di deposito del provvedimento, aggiorna l'evento e inserisce una notifica per ogni
	 * destinatario.
	 * 
	 * @param aProvvedimento
	 * @param aEveNot
	 * @return DocumentoAllegatoModel
	 * @throws F3BException
	 */
	public DocumentoAllegatoModel ExModificaDataDeposito(ProvvedimentoSigeModel aProvvedimento,
			EventoNotificaModel aEveNot, String[] lCheck, FascicoloSigeModel aFasSige) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		NotificaDAO lNotDaoCanc = null;
		AutoritaEsternaDAO lAutDao = null;
		ProvvedimentoSigeDAO lProSigeDao = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDao = null;
		DocumentoAllegatoModel lDocAMod = null;
		FascicoloSigeDAO lFasSigeDao = null;

		// ProvvedimentoSigeModel lProSigeMod = new ProvvedimentoSigeModel(aProvvedimento);

		try {
			lConn = getDBTransaction();

			// Setto il DAO dal Model di DepositoDecreto per l'Update
			lProSigeDao = new ProvvedimentoSigeDAO(lConn);

			// Effettuo l'inserimento data deposito in ProvvedimentoSige; carico i dati da aggiornare.
			lProSigeDao.setCodUfficioAggiornamento(aProvvedimento.getCodUfficioAggiornamento());
			lProSigeDao.setCodOperatoreAggiornamento(aProvvedimento.getCodOperatoreAggiornamento());
			lProSigeDao.setDataAggiornamento(aProvvedimento.getDataAggiornamento());
			lProSigeDao.setDataDeposito(aProvvedimento.getDataDeposito());
			lProSigeDao.selCondizioneByKey(aProvvedimento.getIdProvvedimentoSige());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fase di aggiornamento per ProvvedimentoSige (Data Deposito)");
			lProSigeDao.update();

			// Update di Evento.
			EventoNotificaModel lEveNot = new EventoNotificaModel(aEveNot);
			EventoModel lEveMod = new EventoModel(lEveNot.getEvento());
			// STUB: Se poi bisogna trasferire l'evento tocca settare i flag x SIEP
			lEveMod.setFlagVideoSiep("S");
			lEveMod.setFlagStampaSiep("S");

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(lEveMod);
			lEveDao.update();

			// Fase di Insert del Documento Allegato.

			// Occorre cancellare eventuali DocumentiAllegati preesistenti
			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setCondizioneDelete(lEveMod.getIdEvento(), "03");
			lDocAllDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellati doc allegati collegati a Evento " + lEveMod.getIdEvento());

			// Setto il NumeroProgressivo del Model di DocumentoAllegato con il MAX + 1 (per Uff. Inserimento
			// ed IdEvento).
			lDocAMod = new DocumentoAllegatoModel();
			lDocAMod.setCodUfficioInserimento(aProvvedimento.getCodUfficioAggiornamento());
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAllSqlDao = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal lBigDecAll = lDocAllSqlDao.getProgressivo(lDocAMod);

			lDocAMod.setNumeroProgressivo(new BigDecimal(lBigDecAll.intValue() + 1));
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAMod.setDataEmissione(aProvvedimento.getDataDeposito());
			lDocAMod.setCodTipoDocumento(aProvvedimento.getCodTipoProvvedimento()); // Codifica di
																					// COD_TIPO_DOCUMENTO_ALLEGATO
																					// = Decreto/Ordinanza
			lDocAMod.setFlagDocumentoRegistrato("N");
			// lDocAMod.setDocBlobIn();
			lDocAMod.setCodUfficioInserimento(aProvvedimento.getCodUfficioAggiornamento());
			lDocAMod.setCodOperatoreInserimento(aProvvedimento.getCodOperatoreAggiornamento());
			lDocAMod.setDataInserimento(aProvvedimento.getDataAggiornamento());
			lDocAMod.setTemIdTemplate("SIUS_DE_500");
			// lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setDAOFromModel(lDocAMod);
			lDocAMod.setIdDocumentoAllegato(lDocAllDao.insert());

			// Update delle Notifiche.
			lAutDao = new AutoritaEsternaDAO(lConn);

			BigDecimal lKeyAutorita = null;
			int count = 0;
			if (lEveNot.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + lEveNot.getNotifiche().length + " notifiche");

				while (count < lEveNot.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + lEveNot.getNotifiche()[count]);

					if (lEveNot.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(lEveNot.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(lEveNot.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					lNotDao = new NotificaDAO(lConn);
					lNotDao.setDAOFromModelForUpdate(lEveNot.getNotifiche()[count]);
					lNotDao.update();

					count++;
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Modificate Notifiche per Evento" + lEveMod.getIdEvento());

				// Cancellazione delle notifiche preesistenti per le notifiche selezionate.
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(">>>>>>> Cancellazione Notifiche ");
				lNotDaoCanc = new NotificaDAO(lConn);
				if (lCheck != null) {
					for (int z = 0; z < lCheck.length; z++) {
						lNotDaoCanc.start();
						lNotDaoCanc.setCondizioneUpdate((BigDecimal) new BigDecimal(lCheck[z].toUpperCase()));
						lNotDaoCanc.delete();
						lNotDaoCanc.stop();
					}
				}
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(">>>>>>> Fine Cancellazione Notifiche. ");
			}

			/*
			 * ISSUE MEV : Viene aggiornata la Data Definizione del Fascicolo Sige Numero MEV : 15_S4 Autore :
			 * sessa Data : 01/feb/2016 Branch : MEV_15_S4
			 */
			lFasSigeDao = new FascicoloSigeDAO(lConn);
			lFasSigeDao.setDAOFromModelForUpdate(aFasSige);
			lFasSigeDao.setDataDefinizione(aProvvedimento.getDataDeposito());
			lFasSigeDao.update();
			lFasSigeDao.stop();
			// ***** FINE INTERVENTO MEV_15_S4 *****//

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("DepositoDecertoController.ExModificaDataDeposito: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("DepositoDecretoController.ExModificaDataDeposito: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lNotDaoCanc);
			cleanup(lAutDao);
			cleanup(lDocAllDao);
			cleanup(lDocAllSqlDao);

			cleanup(lConn);
		}
		// Restituito il model del documento allegato inserito.
		return lDocAMod;
	}

	/************************************************************
	 * aggiorna l'evento, inserisce una notifica per ogni destinatario e aggiorna CampoNota.
	 * 
	 * @param aProvvedimento
	 * @param aEveNot
	 * @throws F3BException
	 */
	public void ExModificaProvvSigeEveNotifica(ProvvedimentoSigeModel aProvvedimento,
			EventoNotificaModel aEveNot, String[] lCheck) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		NotificaDAO lNotDaoCanc = null;
		AutoritaEsternaDAO lAutDao = null;
		ProvvedimentoSigeDAO lProSigeDao = null;
		CampoNotaDAO lCampoNotaDao = null;

		// ProvvedimentoSigeModel lProSigeMod = new ProvvedimentoSigeModel(aProvvedimento);

		try {
			lConn = getDBTransaction();

			// Setto il DAO dal Model di DepositoDecreto per l'Update
			lProSigeDao = new ProvvedimentoSigeDAO(lConn);

			// Effettuo l'inserimento data deposito in ProvvedimentoSige; carico i dati da aggiornare.
			lProSigeDao.setCodUfficioAggiornamento(aProvvedimento.getCodUfficioAggiornamento());
			lProSigeDao.setCodOperatoreAggiornamento(aProvvedimento.getCodOperatoreAggiornamento());
			lProSigeDao.setDataAggiornamento(aProvvedimento.getDataAggiornamento());
			lProSigeDao.setDataDeposito(aProvvedimento.getDataDeposito());
			lProSigeDao.setNote(aProvvedimento.getNote());
			lProSigeDao.setCodUfficioDestinatario(aProvvedimento.getCodUfficioDestinatario());
			lProSigeDao.selCondizioneByKey(aProvvedimento.getIdProvvedimentoSige());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fase di aggiornamento per ProvvedimentoSige (Data Deposito)");
			lProSigeDao.update();

			// Update di Evento.
			EventoNotificaModel lEveNot = new EventoNotificaModel(aEveNot);
			EventoModel lEveMod = new EventoModel(lEveNot.getEvento());
			// STUB: Se poi bisogna trasferire l'evento tocca settare i flag x SIEP
			lEveMod.setFlagVideoSiep("S");
			lEveMod.setFlagStampaSiep("S");

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(lEveMod);
			lEveDao.update();

			// Update delle Notifiche.
			lAutDao = new AutoritaEsternaDAO(lConn);

			BigDecimal lKeyAutorita = null;
			int count = 0;
			if (lEveNot.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + lEveNot.getNotifiche().length + " notifiche");

				while (count < lEveNot.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + lEveNot.getNotifiche()[count]);

					if (lEveNot.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(lEveNot.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(lEveNot.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					lNotDao = new NotificaDAO(lConn);
					lNotDao.setDAOFromModelForUpdate(lEveNot.getNotifiche()[count]);
					lNotDao.update();

					count++;
				}

				if (lEveNot.getCampoNote() != null) {
					lCampoNotaDao = new CampoNotaDAO(lConn);
					lCampoNotaDao.setDescr(lEveNot.getCampoNote()[0].getDescr());
					lCampoNotaDao.setCondizioneEvento(lEveNot.getEvento().getIdEvento());
					lCampoNotaDao.update();
				}

				// Cancellazione delle notifiche preesistenti per le notifiche selezionate.
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(">>>>>>> Cancellazione Notifiche ");

				lNotDaoCanc = new NotificaDAO(lConn);
				if (lCheck != null) {
					for (int z = 0; z < lCheck.length; z++) {
						lNotDaoCanc.start();
						lNotDaoCanc.setCondizioneUpdate((BigDecimal) new BigDecimal(lCheck[z].toUpperCase()));
						lNotDaoCanc.delete();
						lNotDaoCanc.stop();
					}
				}
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("ExModificaProvvSigeEveNotifica: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("ExModificaProvvSigeEveNotifica: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lNotDaoCanc);
			cleanup(lAutDao);
			cleanup(lCampoNotaDao);

			cleanup(lConn);
		}
	}

	/**
	 * Ricerca delle Motivazioni Provvedimento SIGE legate ad uno specifico Provvedimento.
	 * 
	 * @param aIdProvvedimentoSige
	 *            : ID del Provvedimento SIGE
	 * @param aCodTipoProv
	 *            : codice Tipo Provvedimento
	 * @param aConn
	 *            : connessione
	 * @return
	 * @throws Exception
	 */
	private Vector ricercaMotivazioni(BigDecimal aIdProvvedimentoSige, String aCodTipoProv, Connection aConn)
			throws Exception {
		Vector lMotivazioni = null;

		// Lettura Motivi Provvedimento Sige.
		MotivazioneProvvedimentoSigeSqlDAO lMPSDao = new MotivazioneProvvedimentoSigeSqlDAO(aConn);

		// Switch sul tipo di Inammissibilità
		if (aCodTipoProv.equalsIgnoreCase(ICostantiProvvedimentoSige.COD_ORDINANZA_INCOMPETENZA)
				|| aCodTipoProv.equalsIgnoreCase(ICostantiProvvedimentoSige.COD_ORDINANZA_NDPNLP))
			lMPSDao.ricercaMotivazioneProvvedSigeByIdProvv(aIdProvvedimentoSige);
		else
			lMPSDao.ricercaMotivazioneDecretoInammissibilitaByIdProvSige(aIdProvvedimentoSige);
		lMotivazioni = new Vector(lMPSDao.getModels());
		cleanup(lMPSDao);

		return lMotivazioni;
	}

	/**
	 * Description: Funzione di ricerca dei provvedimenti SIGE per i quali e' possibile compilare il Foglio
	 * Complementare.
	 * </p>
	 * Parametro di ricerca: Id_FascicoloSige.
	 * 
	 * @param aIdFasSige
	 * @param aTipiProvvedimento
	 *            : e' una stringa del tipo "('02','03')" per consentire
	 * @param aTipoEvento
	 * @throws F3BException
	 * @return Vector di ProvvedimentoSigeEventoModel
	 */
	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvSigeXCFC(BigDecimal aIdFasSige,
			String aTipiProvvedimento, String aTipoEvento) throws F3BException {
		Connection lConn = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		TenoreSigeSqlDAO lTenSqlDao = null;

		ProvvedimentoSigeEventoModel lProvvEveModel = null;

		Vector<ProvvedimentoSigeEventoModel> lProvvedimentiSige = new Vector<ProvvedimentoSigeEventoModel>();
		Vector lTenoriSige = null;
		IDocumentoAllegato docController = SIGELookupRemote.getDocumentoAllegatoController();

		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			lProvSqlDao.ricercaProvvSigeXCFC(aIdFasSige, aTipiProvvedimento, aTipoEvento);
			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(lProvvEveModel.getProvvedimento().getIdEventoGenerato());

				EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
				if (lEveMod == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Errore! Evento non trovato per il provvedimento : "
									+ lProvvEveModel.getProvvedimento().getIdProvvedimentoSige());

				// Individuazione presenza Foglio Complementare allegato
				int lNumAllegatoFC = -1;
				lNumAllegatoFC = lEveSqlDao.getNumDocumentiAllegati(lEveMod.getIdEvento(), "06");
				lEveMod.setNumAllegati(lNumAllegatoFC);

				// Individuazione della presenza di documenti allegati
				int lNumAllegati = -1;
				int lNumValidati = -1;
				lNumAllegati = lEveSqlDao.getNumDocumentiAllegati(lEveMod.getIdEvento(), null);
				// lEveMod.setNumAllegati(lNumAllegati);
				if (lNumAllegati > 0) {
					lNumValidati = lEveSqlDao.getNumAllegatiValidati(lEveMod.getIdEvento());
					lEveMod.setNumAllValidati(lNumValidati);
				}

				lProvvEveModel.getEventoNotifica().setEvento(lEveMod);

				// Ricerca tenori legati al provvedimento
				TenoreSigeModel lTenore = new TenoreSigeModel();
				lTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());

				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());
				lTenSqlDao = new TenoreSigeSqlDAO(lConn);
				lTenSqlDao.ricercaTenoriSige(aTenore);
				lTenoriSige = new Vector(lTenSqlDao.getModels());

				lProvvEveModel.setTenoriEstesi(lTenoriSige);

				siap.sige.documentoallegato.model.DocumentoAllegatoModel fc = docController
						.ExRicercaFCByKeyEvento(lProvvEveModel.getProvvedimento().getIdEventoGenerato());
				lProvvEveModel.setFoglioComplementare(fc);

				lProvvedimentiSige.add(lProvvEveModel);
			} // end while
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("ProvvedimentoSigeController.ExRicercaProvvSigeXCFC: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException("ProvvedimentoSigeController.ExRicercaProvvSigeXCFC: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}

		return lProvvedimentiSige;
	}

	/**
	 * Ricerca Altri Provvedimento collegati al Fascicolo Sige.
	 * 
	 * @param aIdFasSige
	 *            chiave Fascicolo Sige
	 * @param aTipoEvento
	 *            stringa del tipo ('01','05'). Rappresenta i COD_TIPO_EVENTO esclusi dalla query
	 * @return vettore contenente l'elenco dei provvedimenti che soddisfano la ricerca
	 * @throws F3BException
	 */
	public Vector<ProvvedimentoSigeEventoModel> ExRicercaAltriProvvByFascicoloSige(BigDecimal aIdFasSige,
			String aTipoEvento) throws F3BException {

		Connection lConn = null;
		ProvvedimentoSigeSqlDAO lProvSqlDao = null;

		Vector<ProvvedimentoSigeEventoModel> lProvvedimentiSige = new Vector<ProvvedimentoSigeEventoModel>();

		try {
			lConn = getDBConnection();
			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);

			lProvSqlDao.ricercaAltriProvvByFascicoloSige(aIdFasSige, aTipoEvento);
			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				ProvvedimentoSigeEventoModel lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);
				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lProvvedimentiSige.add(lProvvEveModel);

			}

		} catch (DAOException daoEx) {
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaAltriProvvByFascicoloSige DAOException:  " + daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaAltriProvvByFascicoloSige Exception:  " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lConn);
		}

		return lProvvedimentiSige;
	}

	@Override
	public Vector<ProvvedimentoSigeEventoModel> ExRicercaDecretiDaDepositare(
			ProvvedimentoSigeModel aProvvedimentoSige) throws F3BException {
		Connection lConn = null;
		Vector<ProvvedimentoSigeEventoModel> lProvvedimentiSige = new Vector<ProvvedimentoSigeEventoModel>();
		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		ProvvedimentoSigeEventoModel lProvvEveModel = null;
		TenoreSigeSqlDAO lTenSqlDao = null;

		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			// lProvSqlDao.ricercaProvvedimentoDaDepositare(aProvvedimentoSige);
			lProvSqlDao.ricercaDecretiDaDepositare(aProvvedimentoSige);

			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

				// Ricerca tenori legati al provvedimento
				TenoreSigeModel lTenore = new TenoreSigeModel();
				lTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());

				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());
				lTenSqlDao = new TenoreSigeSqlDAO(lConn);
				lTenSqlDao.ricercaTenoriSige(aTenore);
				Vector lTenoriSige = new Vector(lTenSqlDao.getModels());

				lProvvEveModel.setTenoriEstesi(lTenoriSige);

				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lProvvedimentiSige.add(lProvvEveModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentoDaDepositare: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException("ProvvedimentoSigeController.ExRicercaProvvedimentoDaDepositare: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
		return lProvvedimentiSige;
	}

	@Override
	public Vector<ProvvedimentoSigeEventoModel> ExRicercaOrdinanzeDaDepositare(
			ProvvedimentoSigeModel aProvvedimentoSige) throws F3BException {
		Connection lConn = null;
		Vector<ProvvedimentoSigeEventoModel> lProvvedimentiSige = new Vector<ProvvedimentoSigeEventoModel>();
		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		ProvvedimentoSigeEventoModel lProvvEveModel = null;
		TenoreSigeSqlDAO lTenSqlDao = null;

		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			// lProvSqlDao.ricercaProvvedimentoDaDepositare(aProvvedimentoSige);
			// lProvSqlDao.ricercaDecretiDaDepositare(aProvvedimentoSige);
			lProvSqlDao.ricercaOrdinanzeDaDepositare(aProvvedimentoSige);

			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

				// Ricerca tenori legati al provvedimento
				TenoreSigeModel lTenore = new TenoreSigeModel();
				lTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());

				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());
				lTenSqlDao = new TenoreSigeSqlDAO(lConn);
				lTenSqlDao.ricercaTenoriSige(aTenore);
				Vector lTenoriSige = new Vector(lTenSqlDao.getModels());

				lProvvEveModel.setTenoriEstesi(lTenoriSige);

				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lProvvedimentiSige.add(lProvvEveModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentoDaDepositare: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException("ProvvedimentoSigeController.ExRicercaProvvedimentoDaDepositare: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
		return lProvvedimentiSige;
	}

	@Override
	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvSigeFCByIdFascicolo(BigDecimal idFascicolo)
			throws F3BException {

		String lTipiProvv = "'" + ICostantiProvvedimentoSige.COD_DECRETO_GENERICO + "','"
				+ ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA + "'";
		Vector<ProvvedimentoSigeEventoModel> lVectProv = ExRicercaProvvSigeXCFC(idFascicolo, lTipiProvv,
				ICostantiProvvedimentoSige.COD_EVENTO_PROVVEDIMENTO);

		Enumeration<ProvvedimentoSigeEventoModel> e = lVectProv.elements();
		IDocumentoAllegato docController = SIGELookupRemote.getDocumentoAllegatoController();
		while (e.hasMoreElements()) {
			ProvvedimentoSigeEventoModel provv = e.nextElement();
			siap.sige.documentoallegato.model.DocumentoAllegatoModel fc = docController
					.ExRicercaFCByKeyEvento(provv.getProvvedimento().getIdEventoGenerato());
			provv.setFoglioComplementare(fc);

		}
		return lVectProv;
	}

	@Override
	public Vector<FoglioComplementareModel> ExRicercaFogliComplementariByFascicolo(BigDecimal idFascicolo,
			String lTipiProvv) throws F3BException {
		Vector<ProvvedimentoSigeEventoModel> provvedimenti = this.ExRicercaProvvSigeXCFC(idFascicolo,
				lTipiProvv, "01");
		Vector<FoglioComplementareModel> fogli = new Vector<FoglioComplementareModel>();

		Iterator<ProvvedimentoSigeEventoModel> it = provvedimenti.iterator();
		while (it.hasNext()) {
			ProvvedimentoSigeEventoModel provvedimento = it.next();
			IDocumentoAllegato ctrlDoc = SIGELookupRemote.getDocumentoAllegatoController();
			Vector<siap.sige.documentoallegato.model.DocumentoAllegatoModel> fogliComplementari = ctrlDoc
					.ExRicercaFogliComplementariByIdEvento(
							provvedimento.getProvvedimento().getIdEventoGenerato());
			Iterator<siap.sige.documentoallegato.model.DocumentoAllegatoModel> it2 = fogliComplementari
					.iterator();
			if (fogliComplementari.size() == 0) {
				FoglioComplementareModel foglio = new FoglioComplementareModel();
				foglio.setProvvedimento(provvedimento);
				fogli.add(foglio);
				continue;
			}
			boolean isValid = false;
			while (it2.hasNext()) {
				FoglioComplementareModel foglio = new FoglioComplementareModel();
				foglio.setFc(it2.next());
				foglio.setProvvedimento(provvedimento);
				fogli.add(foglio);
				if (!"A".equalsIgnoreCase(foglio.getFc().getFlagDocumentoRegistrato()))
					isValid = true;

			}

			if (!isValid) {
				FoglioComplementareModel foglio = new FoglioComplementareModel();
				foglio.setProvvedimento(provvedimento);
				fogli.add(foglio);
			}
		}

		return fogli;
	}

	@Override
	public BigDecimal ExCountOpposizioniAccolteByIdProvvedimento(BigDecimal idProvedimento)
			throws F3BException {

		Connection lConn = null;
		// Vector<ProvvedimentoSigeEventoModel> lProvvedimentiSige = new
		// Vector<ProvvedimentoSigeEventoModel>();
		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		BigDecimal numOpposizioniAccolte = null;

		try {
			lConn = getDBConnection();
			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			numOpposizioniAccolte = lProvSqlDao.countOpposizioniAccolteByIdProvvedimento(idProvedimento);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExCountOpposizioniAccolteByIdProvvedimento: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException(
					"ProvvedimentoSigeController.ExCountOpposizioniAccolteByIdProvvedimento: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lConn);
		}
		return numOpposizioniAccolte;
	}

	@Override
	public DocumentoAllegatoModel ExInserisciDataDepositoFissazioneUdienza(FascicoloSigeModel aFasSige,
			ProvvedimentoSigeEventoModel aProvvedimento, EventoNotificaModel aEveNot, String[] lCheck)
			throws F3BException {
		Connection lConn = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		ProvvedimentoSigeDAO lProvDao = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDao = null;
		DocumentoAllegatoModel lDocAMod = null;
		FascicoloSigeDAO lFasSigeDao = null;

		ProvvedimentoSigeModel lProvMod = new ProvvedimentoSigeModel(aProvvedimento.getProvvedimento());

		try {
			// Update di Provvedimento.
			lConn = getDBTransaction();
			lProvDao = new ProvvedimentoSigeDAO(lConn);

			// Il campo Chiave_Progr viene valorizzato con l'ultimo valore presente + 1, contestualmente al
			// tipo provvedimento.
			// Trovo il valore da assegnare al progressivo CHIAVE_PROGR.

			// Setto il DAO dal Model di Provvedimento per l'Update

			// Effettuo l'inserimento data deposito in ProvvedimentoModel; carico i dati da aggiornare.
			lProvDao.setCodUfficioAggiornamento(lProvMod.getCodUfficioAggiornamento());
			lProvDao.setCodOperatoreAggiornamento(lProvMod.getCodOperatoreAggiornamento());
			lProvDao.setDataAggiornamento(lProvMod.getDataAggiornamento());
			lProvDao.setDataDeposito(lProvMod.getDataDeposito());
			lProvDao.selCondizioneByKey(lProvMod.getIdProvvedimentoSige());

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Fase di aggiornamento del Provvedimento SIGE (Data Deposito)");
			lProvDao.update();

			// Update di Evento.
			EventoNotificaModel lEveNot = new EventoNotificaModel(aEveNot);
			EventoModel lEveMod = new EventoModel(lEveNot.getEvento());
			// STUB: Se poi bisogna trasferire l'evento tocca settare i flag x SIEP
			lEveMod.setFlagVideoSiep("S");
			lEveMod.setFlagStampaSiep("S");

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(lEveMod);
			lEveDao.update();

			// Fase di Insert del Documento Allegato.

			// Occorre cancellare eventuali DocumentiAllegati preesistenti
			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setCondizioneDelete(lEveMod.getIdEvento(), lProvMod.getCodTipoProvvedimento());
			lDocAllDao.delete();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>> Cancellati doc allegati collegati a Evento "+lEveMod.getIdEvento());

			// Setto il NumeroProgressivo del Model di DocumentoAllegato con il MAX + 1 (per Uff. Inserimento
			// ed IdEvento).
			lDocAMod = new DocumentoAllegatoModel();
			lDocAMod.setCodUfficioInserimento(lProvMod.getCodUfficioAggiornamento());
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAllSqlDao = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal lBigDecAll = lDocAllSqlDao.getProgressivo(lDocAMod);

			lDocAMod.setNumeroProgressivo(new BigDecimal(lBigDecAll.intValue() + 1));
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAMod.setDataEmissione(lProvMod.getDataDeposito());
			lDocAMod.setCodTipoDocumento(lProvMod.getCodTipoProvvedimento()); // Codifica di
																				// COD_TIPO_DOCUMENTO_ALLEGATO
																				// = Deposito Provvedimento
			lDocAMod.setFlagDocumentoRegistrato("N");
			// lDocAMod.setDocBlobIn();
			lDocAMod.setCodUfficioInserimento(lProvMod.getCodUfficioAggiornamento());
			lDocAMod.setCodOperatoreInserimento(lProvMod.getCodOperatoreAggiornamento());
			lDocAMod.setDataInserimento(lProvMod.getDataAggiornamento());
			if (aProvvedimento.getProvvedimento().getCodTipoProvvedimento().trim()
					.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA) == 0)
				lDocAMod.setTemIdTemplate("SIGE_OR_002");
			else if (aProvvedimento.getProvvedimento().getCodTipoProvvedimento().trim()
					.compareTo(ICostantiProvvedimentoSige.COD_DECRETO_GENERICO) == 0)
				lDocAMod.setTemIdTemplate("SIGE_DE_004");
			else if (aProvvedimento.getProvvedimento().getCodTipoProvvedimento().trim()
					.compareTo(ICostantiProvvedimentoSige.COD_ISTRUTTORIE) == 0)
				lDocAMod.setTemIdTemplate("SIGE_IS_011");
			else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Errore! tipo provvedimento privo di Template : "
								+ aProvvedimento.getProvvedimento().getCodTipoProvvedimento());

			lDocAllDao.setDAOFromModel(lDocAMod);
			lDocAMod.setIdDocumentoAllegato(lDocAllDao.insert());

			// Cancellazione delle notifiche preesistenti per le notifiche selezionate.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Cancellazione Notifiche ");
			lNotDao = new NotificaDAO(lConn);
			if (lCheck != null) {
				for (int z = 0; z < lCheck.length; z++) {
					lNotDao.start();
					lNotDao.setCondizioneUpdate((BigDecimal) new BigDecimal(lCheck[z].toUpperCase()));
					lNotDao.delete();
					lNotDao.stop();
				}
			}
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Fine Cancellazione Notifiche. ");

			// Insert delle Notifiche.
			lAutDao = new AutoritaEsternaDAO(lConn);

			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (lEveNot.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + lEveNot.getNotifiche().length + " notifiche");

				while (count < lEveNot.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + lEveNot.getNotifiche()[count]);

					if (lEveNot.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(lEveNot.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(lEveNot.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					lNotDao.setDAOFromModel(lEveNot.getNotifiche()[count]);

					lNotDao.insert();
					lNotDao.stop();

					count++;
				}
			}

			// Aggiorno il fascicolo con stato_fascicolo = 07
			lFasSigeDao = new FascicoloSigeDAO(lConn);
			lFasSigeDao.setDAOFromModelForUpdate(aFasSige);
			// Lo stato del fascicolo cambia se i Provvedimento e' di tipo definitorio
			if (aProvvedimento.getProvvedimento().getDefinitorio().compareToIgnoreCase("S") == 0) {
				lFasSigeDao.setCodStatoFascicolo(ICostantiFascicoloSige.COD_EMESSO_PROVVEDIMENTO);
				lFasSigeDao.setDataDefinizione(lProvMod.getDataDeposito());
				lFasSigeDao.update();
				lFasSigeDao.stop();
			}

			commit(lConn);
		}

		catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciDataDeposito: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExInserisciDataDeposito: " + ex);
		} finally {
			cleanup(lProvDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lDocAllDao);
			cleanup(lDocAllSqlDao);
			cleanup(lProvDao);

			cleanup(lConn);
		}
		// Restituito il model del documento allegato inserito.
		return lDocAMod;
	}

	@Override
	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvedimentiUdienzeByIdFascicolo(
			BigDecimal idFascicolo) throws F3BException {
		Connection lConn = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		TenoreSigeSqlDAO lTenSqlDao = null;

		ProvvedimentoSigeEventoModel lProvvEveModel = null;

		Vector<ProvvedimentoSigeEventoModel> lProvvedimentiSige = new Vector<ProvvedimentoSigeEventoModel>();
		Vector lTenoriSige = null;

		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);

			lProvSqlDao.ricercaProvvedimentiUdienzeByIdFascicolo(idFascicolo);
			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(lProvvEveModel.getProvvedimento().getIdEventoGenerato());

				EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
				if (lEveMod == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Errore! Evento non trovato per il provvedimento : "
									+ lProvvEveModel.getProvvedimento().getIdProvvedimentoSige());

				// Individuazione della presenza di documenti allegati
				int lNumAllegati = -1;
				int lNumValidati = -1;
				lNumAllegati = lEveSqlDao.getNumDocumentiAllegati(lEveMod.getIdEvento(), null);
				lEveMod.setNumAllegati(lNumAllegati);
				if (lNumAllegati > 0) {
					lNumValidati = lEveSqlDao.getNumAllegatiValidati(lEveMod.getIdEvento());
					lEveMod.setNumAllValidati(lNumValidati);
				}

				lProvvEveModel.getEventoNotifica().setEvento(lEveMod);

				// Ricerca tenori legati al provvedimento
				TenoreSigeModel lTenore = new TenoreSigeModel();
				lTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());

				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());
				lTenSqlDao = new TenoreSigeSqlDAO(lConn);
				lTenSqlDao.ricercaTenoriSige(aTenore);
				lTenoriSige = new Vector(lTenSqlDao.getModels());

				lProvvEveModel.setTenoriEstesi(lTenoriSige);

				lProvvedimentiSige.add(lProvvEveModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentiSigePerIdFasSige: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentiSigePerIdFasSige: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
		return lProvvedimentiSige;
	}

	@Override
	public ProvvedimentoSigeModel ExRicercaOrdinanzaRinvioUdienzaDaValidareByFascicolo(BigDecimal idFascicolo)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExRicercaProvvedimentoByIdEvento: inizio");

		Connection lConn = null;
		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		ProvvedimentoSigeModel lProvvedimento = null;

		try {
			lConn = getDBConnection();
			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			lProvSqlDao.ricercaOrdinanzaRinvioUdienzaDaValidareByFascicolo(idFascicolo);
			lProvSqlDao.start();
			if (lProvSqlDao.next()) {
				lProvvedimento = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
			}
			lProvSqlDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaOrdinanzaRinvioUdienzaDaValidareByFascicolo DAOException:  "
							+ daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaOrdinanzaRinvioUdienzaDaValidareByFascicolo Exception:  "
							+ e);
		} finally {
			cleanup(lProvSqlDao);

			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExRicercaProvvedimentoByIdEvento: fine");

		return lProvvedimento;
	}

	@Override
	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvedimentiSigePerOpposizioni(
			BigDecimal aIdFasSige) throws F3BException {
		Connection lConn = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		TenoreSigeSqlDAO lTenSqlDao = null;

		ProvvedimentoSigeEventoModel lProvvEveModel = null;

		Vector<ProvvedimentoSigeEventoModel> lProvvedimentiSige = new Vector<ProvvedimentoSigeEventoModel>();
		try {
			lConn = getDBConnection();

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
			lProvSqlDao.ricercaProvvedimentiSigePerOpposizioni(aIdFasSige);
			lProvSqlDao.start();
			while (lProvSqlDao.next()) {
				lProvvEveModel = new ProvvedimentoSigeEventoModel();
				ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
				lProvModel.decodifica();
				lProvvEveModel.setProvvedimento(lProvModel);

				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				EventoNotificaModel lEveNotifica = lCtrlEve.ExRicercaEventoNotificaByKey(
						lProvvEveModel.getProvvedimento().getIdEventoGenerato(), lConn);

				lProvvEveModel.setEventoNotifica(lEveNotifica);

				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(lProvvEveModel.getProvvedimento().getIdEventoGenerato());

				EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
				if (lEveMod == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Errore! Evento non trovato per il provvedimento : "
									+ lProvvEveModel.getProvvedimento().getIdProvvedimentoSige());

				// Individuazione della presenza di documenti allegati
				int lNumAllegati = -1;
				int lNumValidati = -1;
				lNumAllegati = lEveSqlDao.getNumDocumentiAllegati(lEveMod.getIdEvento(), null);
				lEveMod.setNumAllegati(lNumAllegati);
				if (lNumAllegati > 0) {
					lNumValidati = lEveSqlDao.getNumAllegatiValidati(lEveMod.getIdEvento());
					lEveMod.setNumAllValidati(lNumValidati);
				}

				lProvvEveModel.getEventoNotifica().setEvento(lEveMod);

				// Ricerca tenori legati al provvedimento
				TenoreSigeModel lTenore = new TenoreSigeModel();
				lTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());

				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setProvIdProvvedimentoSige(lProvModel.getIdProvvedimentoSige());
				lTenSqlDao = new TenoreSigeSqlDAO(lConn);
				lTenSqlDao.ricercaTenoriSige(aTenore);
				Vector lTenoriSige = new Vector(lTenSqlDao.getModels());

				lProvvEveModel.setTenoriEstesi(lTenoriSige);

				lProvvedimentiSige.add(lProvvEveModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentiSigePerIdFasSige: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentiSigePerIdFasSige: " + e);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
		return lProvvedimentiSige;
	}

	@Override
	public ProvvedimentoSigeEventoModel ExModificaDecretoInamissibilita(
			ProvvedimentoSigeEventoModel lProvEveModel, Vector<TenoreSigeModel> lTenori,
			String aCodTipoGiudizio, MotivazioneProvvedimentoSigeModel[] lMotivazioni) throws Exception {

		Connection lConn = null;
		try {
			lConn = getDBConnection();
			this.ExCancellaProvvedimentoSige(lProvEveModel.getProvvedimento());
			lProvEveModel = ExInserisciProvvedimento(lProvEveModel, lTenori, aCodTipoGiudizio, lMotivazioni);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentiSigePerIdFasSige: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentiSigePerIdFasSige: " + e);
		} finally {
			cleanup(lConn);
		}
		return lProvEveModel;
	}

}