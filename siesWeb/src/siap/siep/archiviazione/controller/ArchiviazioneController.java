package siap.siep.archiviazione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.cssa.dao.CSSASqlDAO;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSimeoneSqlDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.archiviazione.dao.ArchiviazioneDAO;
import siap.siep.archiviazione.dao.ArchiviazioneSqlDAO;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.dao.NotificaDAO;
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

/**
 * <p>
 * Title: ArchiviazioneController
 * </p>
 * <p>
 * Description: Classe Controller per Archiviazione
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
public class ArchiviazioneController extends SiapController implements IArchiviazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public ArchiviazioneModel ExInserisciEventoNotificaArchiviazione(EventoNotificaModel aEveNotMod,
			ArchiviazioneModel aArchiviazione, FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		ArchiviazioneDAO lArcDao = null;
		EventoDAO lEveDao = null;
		EventoSimeoneSqlDAO lEveSqlDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		FascicoloSiepDAO lFasDao = null;
		ArchiviazioneSqlDAO lArcSqlDao = null;

		EventoModel lEveMod = new EventoModel(aEveNotMod.getEvento());
		ArchiviazioneModel lArchMod = new ArchiviazioneModel(aArchiviazione);

		try {
			lConn = getDBTransaction();

			lArcDao = new ArchiviazioneDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lFasDao = new FascicoloSiepDAO(lConn);
			lEveSqlDao = new EventoSimeoneSqlDAO(lConn);
			lArcSqlDao = new ArchiviazioneSqlDAO(lConn);

			// Cerca l'evento
			EventoModel lEvePresente = new EventoModel();
			lEveSqlDao.ricercaEventoByFascicoloSiepDesc(aFascicolo.getIdFascicoloSiep());
			lEveSqlDao.start();

			if (lEveSqlDao.next())
				lEvePresente = (EventoModel) lEveSqlDao.getModelEvento();

			lEveSqlDao.stop();

			BigDecimal lKey = null;

			if (lEvePresente != null && lEvePresente.getCodTipoProvvedimento() != null
					&& (lEvePresente.getCodTipoProvvedimento().equals("20")
							|| lEvePresente.getCodTipoProvvedimento().equals("21")
							|| lEvePresente.getCodTipoProvvedimento().equals("22")
							|| lEvePresente.getCodTipoProvvedimento().equals("23")
							|| lEvePresente.getCodTipoProvvedimento().equals("25")) // STUB 17/10/2005 REWORK
																					// STATO
																					// ESECUZIONE
					&& (!"S".equals(lEvePresente.getFlagDocumentoRegistrato())
							&& !"A".equals(lEvePresente.getFlagDocumentoRegistrato()))) {
				// aggiorno
				lKey = lEvePresente.getIdEvento();
				lEveDao.setDAOFromModel(lEveMod);
				lEveDao.setFlagDocumentoRegistrato(null); // Per costringere a rieffettuare la stampa
				lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
				lEveDao.setIdEvento(lKey);
				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				// aggiornamento archiviazione
				lArcSqlDao.ricercaArchiviazioneByIdEvento(lKey);
				ArchiviazioneModel lArchiviazioneMod = (ArchiviazioneModel) lArcSqlDao.getModelByKey();
				if (lArchiviazioneMod != null && lArchiviazioneMod.getIdArchiviazione() != null) {
					lArchMod.setIdArchiviazione(lArchiviazioneMod.getIdArchiviazione());
					lArchMod.setEveIdEvento(lKey);
					lArcDao.setDAOFromModel(lArchMod);
					lArcDao.selByKey();
					lArcDao.update();
					lArcDao.stop();
				}

				// Cancella le NOTIFICHE associate al EVENTO
				lNotDao.setCondizioneEvento(lKey);

				lNotDao.delete();
				lNotDao.stop();
			} else {
				// Se non presente lo inserisco
				// inserimento evento definizione procedimento
				lEveDao.setDAOFromModel(lEveMod);
				lKey = lEveDao.insert();
				lEveDao.stop();

				// inserimento archiviazione
				lArchMod.setEveIdEvento(lKey);
				lArcDao.setDAOFromModel(lArchMod);
				BigDecimal lKeyArc = null;
				lKeyArc = lArcDao.insert();
				lArchMod.setIdArchiviazione(lKeyArc);
				lArcDao.stop();
			}

			// aggiornamento fascicolo
			if (lArchMod.getCodTipoProvvedimento().equals("22")
					|| lArchMod.getCodTipoProvvedimento().equals("25")) {
				// STUB 17/10/2005 REWORK STATO ESECUZIONE
				lFasDao.setAnnoFascicoloUnione(aFascicolo.getAnnoFascicoloUnione());
				lFasDao.setNumFascicoloUnione(aFascicolo.getNumFascicoloUnione());
				lFasDao.setDataUnione(aFascicolo.getDataUnione());
				lFasDao.setCodUfficioUnione(aFascicolo.getCodUfficioUnione());
				lFasDao.setDataAggiornamento(aFascicolo.getDataAggiornamento());
				lFasDao.setCodUfficioAggiornamento(aFascicolo.getCodUfficioAggiornamento());
				lFasDao.setCodOperatoreAggiornamento(aFascicolo.getCodOperatoreAggiornamento());
				lFasDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());
				lFasDao.update();
				lFasDao.stop();
			}

			// inserimento Notifiche Evento definizione procedimento
			BigDecimal lKeyAutorita = null;
			int count = 0;
			if (aEveNotMod.getNotifiche() != null) {
				while (count < aEveNotMod.getNotifiche().length) {
					if (aEveNotMod.getNotifiche()[count] != null) {
						if (aEveNotMod.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao.setRicercaByAutSede(
									aEveNotMod.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								lAutDao.setDAOFromModel(
										aEveNotMod.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								aEveNotMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEveNotMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						aEveNotMod.getNotifiche()[count].setEveIdEvento(lKey);

						lNotDao.setDAOFromModel(aEveNotMod.getNotifiche()[count]);
						lNotDao.insert();
						lNotDao.stop();
					}
					count++;
				}
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ArchiviazioneController.ExInserisciEventoNotificaArchiviazione: Non posso inserire: "
							+ ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ArchiviazioneController.ExInserisciEventoNotificaArchiviazione: Non posso inserire il soggetti : "
							+ ex);
		} finally {
			cleanup(lArcDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lFasDao);
			cleanup(lEveSqlDao);
			cleanup(lArcSqlDao);

			cleanup(lConn);
		}

		return lArchMod;
	}

	public Vector ExRicercaArchiviazione(ArchiviazioneModel aArchiviazione) throws F3BException {

		Connection lConn = null;
		Vector lArchiviazioni = new Vector();
		ArchiviazioneSqlDAO lArcDao = null;

		try {
			lConn = getDBConnection();
			lArcDao = new ArchiviazioneSqlDAO(lConn);
			lArcDao.ricercaArchiviazione(aArchiviazione);
			lArchiviazioni = new Vector(lArcDao.getModels());

			if (lArchiviazioni.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ArchiviazioneController.ExRicercaArchiviazione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lArcDao);
			cleanup(lConn);
		}

		return lArchiviazioni;
	}

	public ArchiviazioneModel ExRicercaArchiviazioneByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		ArchiviazioneSqlDAO lArcDao = null;
		ArchiviazioneModel lArcMod;

		try {
			lConn = getDBConnection();
			lArcDao = new ArchiviazioneSqlDAO(lConn);
			lArcDao.ricercaArchiviazioneByKey(aKey);
			lArcMod = (ArchiviazioneModel) lArcDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ArchiviazioneController.ExRicercaArchiviazione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lArcDao);
			cleanup(lConn);
		}

		return lArcMod;
	}

	public ArchiviazioneModel ExRicercaArchiviazioneCssaIstitutoByIdEvento(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;

		ArchiviazioneSqlDAO lArcDao = null;
		IstitutoDetenzioneSqlDAO lIstDetDao = null;
		CSSASqlDAO lCssaDao = null;

		ArchiviazioneModel lArcMod;
		CSSAModel lCssaMod;
		IstitutoDetenzioneModel lIstDetMod;

		try {
			lConn = getDBConnection();

			lArcDao = new ArchiviazioneSqlDAO(lConn);
			lIstDetDao = new IstitutoDetenzioneSqlDAO(lConn);
			lCssaDao = new CSSASqlDAO(lConn);

			// ricerca archiviazione
			lArcDao.ricercaArchiviazioneByIdEvento(aKey);
			lArcMod = (ArchiviazioneModel) lArcDao.getModelByKey();

			// ricerca Istituto
			if (lArcMod != null && lArcMod.getIstDetIdIstitutoDetenzione() != null) {
				lIstDetDao.ricercaIstitutoDetenzioneByKey(lArcMod.getIstDetIdIstitutoDetenzione());
				lIstDetMod = (IstitutoDetenzioneModel) lIstDetDao.getModelByKey();
				lArcMod.setIstitutoDetenzione(lIstDetMod);
			}

			// ricerca cssa
			if (lArcMod != null && lArcMod.getCssIdCssa() != null) {
				lCssaDao.ricercaCSSAByKey(lArcMod.getCssIdCssa());
				lCssaMod = (CSSAModel) lCssaDao.getModelByKey();
				lArcMod.setCssa(lCssaMod);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ArchiviazioneController.ExRicercaArchiviazioneCssaIstitutoByIdEvento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lArcDao);
			cleanup(lIstDetDao);
			cleanup(lCssaDao);

			cleanup(lConn);
		}

		return lArcMod;
	}

	public ArchiviazioneModel ExRicercaArchiviazioneByIdEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		ArchiviazioneSqlDAO lArcDao = null;
		ArchiviazioneModel lArcMod;

		try {
			lConn = getDBConnection();
			lArcDao = new ArchiviazioneSqlDAO(lConn);
			lArcDao.ricercaArchiviazioneByIdEvento(aKey);
			lArcMod = (ArchiviazioneModel) lArcDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ArchiviazioneController.ExRicercaArchiviazioneByIdEvento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lArcDao);
			cleanup(lConn);
		}

		return lArcMod;
	}

	public EventoModel ExUpdateValidaArchiviazione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		return ExUpdateValidaArchiviazione(aEvento, aFascicolo, null);
	}

	public EventoModel ExUpdateValidaArchiviazione(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		FascicoloSiepDAO lFascDao = null;
		ArchiviazioneSqlDAO lArchSqlDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		ScadenzarioDAO lScaDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		String lRapp = "";

		try {
			siesLogger.debug("--XX-- >>>>>>>>>>>>>>>>>>>>>  - ExUpdateValidaArchiviazione -fasc = "
					+ aFascicolo.getChiaveAnno() + "/" + aFascicolo.getChiaveProgr());
			if (aDBConnection != null) {
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			// SETTA LO STATO PROCEDIMENTO
			lArchSqlDao = new ArchiviazioneSqlDAO(lConn);

			ArchiviazioneModel lArchMod = new ArchiviazioneModel();
			lArchSqlDao.ricercaArchiviazioneByIdEvento(lEveMod.getIdEvento());
			lArchMod = (ArchiviazioneModel) lArchSqlDao.getModelByKey();

			String lMotivoArchiviazione = "-";
			String lAzzeraAmmendaMulta = null;
			if (lArchMod != null) {
				if (lArchMod.getCodTipoProvvedimento() != null) {
					String lStato = null;
					if (lArchMod.getCodTipoProvvedimento().equals("20")) {
						if (lArchMod.getCodOggettoDefinizione().equals("0006"))
							lStato = "0300";
						else if (lArchMod.getCodOggettoDefinizione().equals("0007"))
							lStato = "0301";
						else if (lArchMod.getCodOggettoDefinizione().equals("0008"))
							lStato = "0302";
						else if (lArchMod.getCodOggettoDefinizione().equals("0009"))
							lStato = "0303";
						else if (lArchMod.getCodOggettoDefinizione().equals("0120"))
							lStato = "0304";
						else if (lArchMod.getCodOggettoDefinizione().equals("0353"))
							lStato = "0305";
						else if (lArchMod.getCodOggettoDefinizione().equals("0478"))
							lStato = "0337";

						lMotivoArchiviazione = "09";
					}
					if (lArchMod.getCodTipoProvvedimento().equals("21")) {
						if (lArchMod.getCodOggettoDefinizione().equals("0097"))
							lStato = "0308";
						else if (lArchMod.getCodOggettoDefinizione().equals("0096"))
							lStato = "0309";
						else if (lArchMod.getCodOggettoDefinizione().equals("0098"))
							lStato = "0310";
						else if (lArchMod.getCodOggettoDefinizione().equals("0099"))
							lStato = "0311";
						else if (lArchMod.getCodOggettoDefinizione().equals("0473"))
							lStato = "0312";
						else if (lArchMod.getCodOggettoDefinizione().equals("0479"))
							lStato = "0338";
						else if (lArchMod.getCodOggettoDefinizione().equals("0480"))
							lStato = "0339";
						else if (lArchMod.getCodOggettoDefinizione().equals("0481"))
							lStato = "0340";

						lMotivoArchiviazione = "02";
					} else if (lArchMod.getCodTipoProvvedimento().equals("22")) {
						if (lArchMod.getCodOggettoDefinizione().equals("0019"))
							lStato = "0315";
						else if (lArchMod.getCodOggettoDefinizione().equals("0022"))
							lStato = "0316";

						lMotivoArchiviazione = "01";
						lAzzeraAmmendaMulta = "S";
					} else if (lArchMod.getCodTipoProvvedimento().equals("23")) {
						if (lArchMod.getCodOggettoDefinizione().equals("0411"))
							lStato = "0317";
						else if (lArchMod.getCodOggettoDefinizione().equals("0412"))
							lStato = "0318";
						else if (lArchMod.getCodOggettoDefinizione().equals("0413"))
							lStato = "0319";
						else if (lArchMod.getCodOggettoDefinizione().equals("0414"))
							lStato = "0320";
						else if (lArchMod.getCodOggettoDefinizione().equals("0415"))
							lStato = "0321";
						else if (lArchMod.getCodOggettoDefinizione().equals("0416"))
							lStato = "0322";
						else if (lArchMod.getCodOggettoDefinizione().equals("0417"))
							lStato = "0323";
						else if (lArchMod.getCodOggettoDefinizione().equals("0418"))
							lStato = "0324";
						else if (lArchMod.getCodOggettoDefinizione().equals("0419"))
							lStato = "0325";
						else if (lArchMod.getCodOggettoDefinizione().equals("0420"))
							lStato = "0326";
						else if (lArchMod.getCodOggettoDefinizione().equals("0476"))
							lStato = "0327";
						else if (lArchMod.getCodOggettoDefinizione().equals("0477"))
							lStato = "0328";
						else if (lArchMod.getCodOggettoDefinizione().equals("0421"))
							lStato = "0329";
						else if (lArchMod.getCodOggettoDefinizione().equals("0422"))
							lStato = "0330";
						else if (lArchMod.getCodOggettoDefinizione().equals("0424"))
							lStato = "0331";
						else if (lArchMod.getCodOggettoDefinizione().equals("0425"))
							lStato = "0332";
						else if (lArchMod.getCodOggettoDefinizione().equals("0426"))
							lStato = "0333";
						else if (lArchMod.getCodOggettoDefinizione().equals("0474"))
							lStato = "0311";
						// AMBROSINO 05/2011 aggiunti i codici da qui in giu
						else if (lArchMod.getCodOggettoDefinizione().equals("1773")) {
							lStato = "0350";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1774")) {
							lStato = "0351";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1775")) {
							lStato = "0352";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1776")) {
							lStato = "0353";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1777")) {
							lStato = "0354";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1778")) {
							lStato = "0355";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1779")) {
							lStato = "0356";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1780")) {
							lStato = "0357";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1781")) {
							lStato = "0358";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1782")) {
							lStato = "0359";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1783")) {
							lStato = "0360";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1784")) {
							lStato = "0361";
							lAzzeraAmmendaMulta = "S";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1875")) {
							lStato = "0362";
							lAzzeraAmmendaMulta = "S";
						}
						// 21/07/2011 Aggiunti Codici Oggetti.
						else if (lArchMod.getCodOggettoDefinizione().equals("0492")) {
							lStato = "0306";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1120")) {
							lStato = "0307";
						} else if (lArchMod.getCodOggettoDefinizione().equals("1121")) {
							lStato = "0313";
						}

						lMotivoArchiviazione = "10";
					}

					Date lData = lArchMod.getDataDefinizione();
					lRapp = "InserimentoCancellazioneStatoProcedimento";
					InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveMod,
							lStato, lData);
				}
			}

			// Aggiorna Inserisci PENA_RESIDUA
			lRapp = "AzzeramentoPenaResidua";
			if (aFascicolo.getFlagValidato().equalsIgnoreCase("S"))
				AzzeramentoPenaResidua(lConn, lEveMod, aFascicolo.getIdFascicoloSiep(), lAzzeraAmmendaMulta);
			// InserimentoAggiornamentoPenaResidua(lConn,lEveMod,aFascicolo.getIdFascicoloSiep());

			// Aggiorna POSIZIONE_GIURIDICA
			lRapp = "InserimentoAggiornamentoPosizioneGiuridica";
			if (aFascicolo.getFlagValidato().equalsIgnoreCase("S"))
				InserimentoAggiornamentoPosizioneGiuridica(lConn, "10", lPosMod,
						lArchMod.getDataDefinizione(), lEveMod, aFascicolo.getIdFascicoloSiep(),
						lEveMod.getIdEvento());

			// Cancellazione scadenzari
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);

			ScadenzarioModel lScaMod = null;

			// fine pena
			lRapp = "RicercaScadenzarioByTipoScadenzarioIdFascicolo";
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("02", aFascicolo.getIdFascicoloSiep());
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
			if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
				lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
				lScaDao.delete();
				lScaDao.stop();
			}

			// L.165/98
			// ===================================================================
			// Cancella tutti gli scadenzari di tipo LEGGE SIMEONE ( Tipo = 01 )
			// ===================================================================
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFascicolo.getIdFascicoloSiep(), "01");
			lScaDao.delete();

			// Aggiorna il fascicolo con stato = "01" archiviato/definito
			lFascDao = new FascicoloSiepDAO(lConn);

			lFascDao.setCodMotivoArchiviazione(lMotivoArchiviazione);
			lFascDao.setCodStatoFascicolo("01");
			lFascDao.setDataArchiviazione(lArchMod.getDataDefinizione());

			lFascDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
			lFascDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
			lFascDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());

			lFascDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());
			lFascDao.update();
			lFascDao.stop();

			// ------- EVENTO--------
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException daoEx) {
			if (aDBConnection == null) {
				rollback(lConn);
				daoEx.printStackTrace();
				throw new F3BException("ArchiviazioneController.ExUpdateValidaArchiviazione : " + daoEx);
			} else {
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile eseguire " + lRapp);
			}
		} catch (Exception ex) {
			if (aDBConnection == null) {
				rollback(lConn);
				ex.printStackTrace();
				throw new F3BException("ArchiviazioneController.ExUpdateValidaArchiviazione : " + ex);
			} else {
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile eseguire " + lRapp);
			}
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lFascDao);
			cleanup(lScaSqlDao);
			cleanup(lScaDao);
			cleanup(lArchSqlDao);
			cleanup(lEveDaoBlob);

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}

		return lEveMod;
	}

	public EventoModel ExUpdateValidaVistoAttesa(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			String aStato) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		FascicoloSiepDAO lFascDao = null;
		ScadenzarioDAO lScaDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			// SETTA LO STATO PROCEDIMENTO
			Date lData = lEveMod.getDataEmissione();
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveMod, aStato,
					lData);

			// Aggiorna Inserisci PENA_RESIDUA
			InserimentoAggiornamentoPenaResidua(lConn, lEveMod, aFascicolo.getIdFascicoloSiep());

			// Aggiorna POSIZIONE_GIURIDICA
			lPosSqlDao.inserimentoAggiornamentoPosizioneGiuridica(lConn, lPosMod.getCodPosizioneGiuridica(),
					lPosMod, lData, lEveMod, aFascicolo.getIdFascicoloSiep(), lEveMod.getIdEvento(), "S");

			// ===================================================================
			// Cancella tutti gli scadenzari di tipo LEGGE SIMEONE ( Tipo = 01 )
			// ===================================================================
			lScaDao = new ScadenzarioDAO(lConn);
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFascicolo.getIdFascicoloSiep(), "01");
			lScaDao.delete();

			// ------- EVENTO--------
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("ArchiviazioneController.ExUpdateValidaVistoPm : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("ArchiviazioneController.ExUpdateValidaVistoPm : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lFascDao);
			cleanup(lScaDao);
			cleanup(lEveDaoBlob);

			cleanup(lConn);
		}

		return lEveMod;
	}

	public EventoModel ExUpdateValidaAnnProvCumulo(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		return ExUpdateValidaAnnProvCumulo(aEvento, aFascicolo, null);
	}

	public EventoModel ExUpdateValidaAnnProvCumulo(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		String lRapp = "";
		try {
			siesLogger.debug(
					"--XX-- >>>>>>>>>>>>>>>>>>>>>  - ArchiviazioneController - ExUpdateValidaAnnProvCumulo");
			if (aDBConnection != null) {
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// STATO PROCEDIMENTO
			lRapp = "InserimentoCancellazioneStatoProcedimento";
			Date lData = DateUtils.getSysDate();
			InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveMod, "0132",
					lData);

			// PENA RESIDUA
			lRapp = "InserimentoAggiornamentoPenaResidua";
			// PenaResiduaModel lPenResMod = new PenaResiduaModel();
			/* lPenResMod = */InserimentoAggiornamentoPenaResidua(lConn, lEveMod,
					aFascicolo.getIdFascicoloSiep());

			// ------- EVENTO--------

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException daoEx) {
			if (aDBConnection == null) {
				rollback(lConn);
				daoEx.printStackTrace();
				throw new F3BException("ArchiviazioneController.ExUpdateValidaAnnProvCumulo : " + daoEx);
			} else {
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile eseguire " + lRapp);
			}
		} catch (Exception ex) {
			if (aDBConnection == null) {
				rollback(lConn);
				ex.printStackTrace();
				throw new F3BException("ArchiviazioneController.ExUpdateValidaAnnProvCumulo : " + ex);
			} else {
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile eseguire " + lRapp);
			}
		} finally {
			if (aDBConnection == null) {
				cleanup(lConn);
			}

			cleanup(lEveDaoBlob);
		}

		return lEveMod;
	}

	// metodi privati
	private PenaResiduaModel InserimentoAggiornamentoPenaResidua(Connection lConn, EventoModel lEveModel,
			BigDecimal aKey) throws DAOException, F3BException {

		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaModel lPenResMod = null;

		try {
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResMod = new PenaResiduaModel();

			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aKey);
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			if (lPenResMod.getEveIdEvento() == null) {
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
				lPenResDao.setEveIdEvento(lEveModel.getIdEvento());
				lPenResDao.setFlagValidato("S");
				lPenResDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lPenResDao.setDataAggiornamento(DateUtils.getSysDate());
				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				lPenResDao.setDAOFromModel(lPenResMod);
				lPenResDao.setFlagValidato("S");
				lPenResDao.setEveIdEvento(lEveModel.getIdEvento());
				lPenResDao.setDataInserimento(DateUtils.getSysDate());
				lPenResDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lPenResDao.insert();
				lPenResDao.stop();
			}
		} finally {
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
		}

		return lPenResMod;
	}

	private PenaResiduaModel AzzeramentoPenaResidua(Connection lConn, EventoModel lEveModel, BigDecimal aKey,
			String lAzzeraMultaAmmenda) throws DAOException, F3BException {

		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;

		PenaResiduaModel lPenResMod = null;

		try {
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResMod = new PenaResiduaModel();

			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aKey);
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			if (lPenResMod != null) {
				if (lPenResMod.getEveIdEvento() == null) {
					lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
					lPenResDao.setEveIdEvento(lEveModel.getIdEvento());
					lPenResDao.setFlagValidato("S");
					lPenResDao.setNumAnniArresto(new BigDecimal(0));
					lPenResDao.setNumMesiArresto(new BigDecimal(0));
					lPenResDao.setNumGiorniArresto(new BigDecimal(0));
					lPenResDao.setNumAnniReclusione(new BigDecimal(0));
					lPenResDao.setNumMesiReclusione(new BigDecimal(0));
					lPenResDao.setNumGiorniReclusione(new BigDecimal(0));
					lPenResDao.setDataInizio(null);
					lPenResDao.setDataFine(null);
					lPenResDao.setDataFinePresunta(null);
					lPenResDao.setDataFineReclusione(null);
					lPenResDao.setDataInizioArresto(null);
					if (lAzzeraMultaAmmenda != null) {
						lPenResDao.setImportoAmmenda(new BigDecimal(0));
						lPenResDao.setImportoMulta(new BigDecimal(0));
					}

					lPenResDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
					lPenResDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
					lPenResDao.setDataAggiornamento(DateUtils.getSysDate());
					lPenResDao.selByKey();
					lPenResDao.update();
					lPenResDao.stop();
				} else {
					lPenResMod.setFlagValidato("S");
					lPenResMod.setEveIdEvento(lEveModel.getIdEvento());
					lPenResMod.setNumAnniArresto(new BigDecimal(0));
					lPenResMod.setNumMesiArresto(new BigDecimal(0));
					lPenResMod.setNumGiorniArresto(new BigDecimal(0));
					lPenResMod.setNumAnniReclusione(new BigDecimal(0));
					lPenResMod.setNumMesiReclusione(new BigDecimal(0));
					lPenResMod.setNumGiorniReclusione(new BigDecimal(0));
					lPenResMod.setCodOperatoreAggiornamento(null);
					lPenResMod.setDataAggiornamento(null);
					lPenResMod.setCodUfficioAggiornamento(null);
					lPenResMod.setDataInizio(null);
					lPenResMod.setDataFine(null);
					lPenResMod.setDataFinePresunta(null);
					lPenResMod.setDataFineReclusione(null);
					lPenResMod.setDataInizioArresto(null);
					if (lAzzeraMultaAmmenda != null) {
						lPenResMod.setImportoAmmenda(new BigDecimal(0));
						lPenResMod.setImportoMulta(new BigDecimal(0));
					}
					lPenResMod.setDataInserimento(DateUtils.getSysDate());
					lPenResMod.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
					lPenResMod.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());

					lPenResDao.setDAOFromModel(lPenResMod);

					lPenResDao.insert();
					lPenResDao.stop();
				}
			}
		} finally {
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
		}

		return lPenResMod;
	}

	private void InserimentoCancellazioneStatoProcedimento(Connection lConn, BigDecimal aKey,
			EventoModel lEveModel, String lStatoProcMod, Date lData) throws DAOException, F3BException {

		StatoProcedimentoDAO lStatoDao = new StatoProcedimentoDAO(lConn);

		try {
			// cancellazione
			lStatoDao.setCondizioneByIdFascicolo(aKey);
			lStatoDao.delete();
			lStatoDao.stop();
			// inserimento
			lStatoDao.setProgressivo(new BigDecimal(1));
			lStatoDao.setData(lData);
			lStatoDao.setCodStatoProcedimento(lStatoProcMod);
			lStatoDao.setFasSieIdFascicoloSiep(aKey);
			lStatoDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
			lStatoDao.setDataInserimento(lEveModel.getDataAggiornamento());
			lStatoDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
			lStatoDao.insert();
		} finally {
			cleanup(lStatoDao);
		}
	}

	// Aggiorna/inserisce POSIZIONE_GIURIDICA
	private BigDecimal InserimentoAggiornamentoPosizioneGiuridica(Connection lConn, String lPosizione,
			PosizioneGiuridicaModel lPos, Date lData, EventoModel lEveModel, BigDecimal aKeyFasc,
			BigDecimal aKeyEve) throws DAOException, F3BException {

		PosizioneGiuridicaDAO lPosDao = null;
		BigDecimal lIdPosizioneGiuridica = null;

		try {
			lPosDao = new PosizioneGiuridicaDAO(lConn);

			if (lPos != null && lPos.getDataFine() == null) {
				lPosDao.setDataFine(lData);
				lPosDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lPosDao.setDataAggiornamento(lEveModel.getDataAggiornamento());
				lPosDao.setCondizioneUpdate(lPos.getIdPosizioneGiuridica());
				lPosDao.update();
				lPosDao.stop();
			}

			if (lPosizione != null) {
				lPosDao.setCodPosizioneGiuridica(lPosizione);
				lPosDao.setDataInizio(lData);
				lPosDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(lEveModel.getDataAggiornamento());
				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
				lPosDao.setFasSieIdFascicoloSiep(aKeyFasc);
				lPosDao.setIdEventoRiferimento(aKeyEve);

				lIdPosizioneGiuridica = lPosDao.insert();
				lPosDao.stop();
			}
		} finally {
			cleanup(lPosDao);
		}

		return lIdPosizioneGiuridica;
	}

	/**
	 * Effettua l'archiviazione semplificata dei fascicolo migrati RES. - Aggiorna il fascicolo SIEP -
	 * Aggiorna la posizione giuridica in Libero - Aggiorna lo stato procedimento - Cancella eventuali
	 * scadenzari - Azzera la pena residua e le date.
	 *
	 * @param aFascicolo
	 * @throws F3BException
	 */
	public void ExArchiviazioneSemplificataRES(FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		FascicoloSiepDAO lFascDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		ScadenzarioDAO lScaDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Aggiorno il fascicolo
			// ========================================================================
			lFascDao = new FascicoloSiepDAO(lConn);

			lFascDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());

			lFascDao.setCodStatoFascicolo("01"); // 01 - Archiviato/Definito
			lFascDao.setDataArchiviazione(aFascicolo.getDataArchiviazione());
			lFascDao.setCodMotivoArchiviazione("11"); // 11 - Migrato da Res e Gia' Archiviato
			lFascDao.setNote(aFascicolo.getNote());
			lFascDao.setFlagValidato("S"); // n.b. potrebbe non essere validato

			lFascDao.setCodOperatoreAggiornamento(aFascicolo.getCodOperatoreAggiornamento());
			lFascDao.setCodUfficioAggiornamento(aFascicolo.getCodUfficioAggiornamento());
			lFascDao.setDataAggiornamento(aFascicolo.getDataAggiornamento());

			lFascDao.update();
			lFascDao.stop();

			// ========================================================================
			// Aggiorno la posizione giuridica in Libero.
			// ========================================================================
			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			lPosDao = new PosizioneGiuridicaDAO(lConn);

			// Aggiorno la posizione corrente (se presente)
			if (lPosMod != null && lPosMod.getDataFine() == null) {
				if (aFascicolo.getDataArchiviazione() != null) {
					lPosDao.setDataFine(aFascicolo.getDataArchiviazione());
				} else { // se data arch. non specificata utilizza la data iscrizione
					lPosDao.setDataFine(aFascicolo.getDataIscrizione());
				}

				lPosDao.setCodOperatoreAggiornamento(aFascicolo.getCodOperatoreAggiornamento());
				lPosDao.setCodUfficioAggiornamento(aFascicolo.getCodUfficioAggiornamento());
				lPosDao.setDataAggiornamento(aFascicolo.getDataAggiornamento());

				lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());
				lPosDao.update();
				lPosDao.stop();
			}

			// Inserisco la nuova posizione giuridica
			lPosDao.setCodPosizioneGiuridica("10");
			if (aFascicolo.getDataArchiviazione() != null) {
				lPosDao.setDataInizio(aFascicolo.getDataArchiviazione());
			} else { // se data arch. non specificata utilizza la data iscrizione
				lPosDao.setDataInizio(aFascicolo.getDataIscrizione());
			}

			lPosDao.setCodOperatoreInserimento(aFascicolo.getCodOperatoreAggiornamento());
			lPosDao.setCodUfficioInserimento(aFascicolo.getCodUfficioAggiornamento());
			lPosDao.setDataInserimento(aFascicolo.getDataAggiornamento());

			lPosDao.setCodPosizioneProcessuale("-");
			lPosDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			lPosDao.insert();
			lPosDao.stop();

			// ========================================================================
			// Aggiorno lo stato Procedimento
			// ========================================================================
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// cancellazione
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();
			lStatoDao.stop();

			// inserimento
			lStatoDao.setProgressivo(new BigDecimal(1));
			lStatoDao.setData(aFascicolo.getDataArchiviazione()); //
			lStatoDao.setCodStatoProcedimento("0076"); // 0076 - Archiviato, Definito

			lStatoDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			lStatoDao.setCodOperatoreInserimento(aFascicolo.getCodOperatoreAggiornamento());
			lStatoDao.setCodUfficioInserimento(aFascicolo.getCodUfficioAggiornamento());
			lStatoDao.setDataInserimento(aFascicolo.getDataAggiornamento());
			lStatoDao.insert();

			// ========================================================================
			// Cancellazione scadenzari
			// ========================================================================
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);

			ScadenzarioModel lScaMod = null;

			// fine pena
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("02", aFascicolo.getIdFascicoloSiep());
			lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();
			if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
				lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
				lScaDao.delete();
				lScaDao.stop();
			}

			// Cancella tutti gli scadenzari di tipo LEGGE SIMEONE L.165/98( Tipo = 01 )
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFascicolo.getIdFascicoloSiep(), "01");
			lScaDao.delete();

			// ========================================================================
			// Se presente pena residua la duplico e azzero i quantum e le date
			// altrimenti non faccio nulla.
			// ========================================================================
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao = new PenaResiduaDAO(lConn);

			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			if (lPenResMod != null) {
				lPenResMod.setIdPenaResidua(null);
				lPenResMod.setEveIdEvento(null);
				lPenResMod.setMisAltIdMisuraAlternativa(null);

				lPenResMod.setDataInizio(null);
				lPenResMod.setDataFineReclusione(null);
				lPenResMod.setDataInizioArresto(null);
				lPenResMod.setDataFine(null);
				lPenResMod.setDataFinePresunta(null);

				lPenResMod.setDataInizioIsolamentoDiurno(null);
				lPenResMod.setDataFineIsolamentoDiurno(null);

				// Azzero i quantum
				lPenResMod.setNumAnniArresto(null);
				lPenResMod.setNumMesiArresto(null);
				lPenResMod.setNumGiorniArresto(null);
				lPenResMod.setNumAnniReclusione(null);
				lPenResMod.setNumMesiReclusione(null);
				lPenResMod.setNumGiorniReclusione(null);

				lPenResMod.setNumGiorniIsolamentoDiurno(null);
				lPenResMod.setNumMesiIsolamentoDiurno(null);
				lPenResMod.setNumAnniIsolamentoDiurno(null);

				lPenResMod.setFlagErgastolo("N");
				lPenResMod.setFlagPenaSospesa(null);

				lPenResMod.setCodOperatoreInserimento(aFascicolo.getCodOperatoreAggiornamento());
				lPenResMod.setCodUfficioInserimento(aFascicolo.getCodUfficioAggiornamento());
				lPenResMod.setDataInserimento(aFascicolo.getDataAggiornamento());

				lPenResMod.setCodOperatoreAggiornamento(aFascicolo.getCodOperatoreAggiornamento());
				lPenResMod.setCodUfficioAggiornamento(aFascicolo.getCodUfficioAggiornamento());
				lPenResMod.setDataAggiornamento(aFascicolo.getDataAggiornamento());

				lPenResDao.setDAOFromModel(lPenResMod);

				lPenResDao.insert();
				lPenResDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("ArchiviazioneController.ExArchiviazioneSemplificataRES : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("ArchiviazioneController.ExArchiviazioneSemplificataRES : " + ex);
		} finally {
			cleanup(lPosSqlDao);
			cleanup(lPosDao);
			cleanup(lFascDao);
			cleanup(lStatoDao);
			cleanup(lScaSqlDao);
			cleanup(lScaDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);

			cleanup(lConn);
		}
	}

}