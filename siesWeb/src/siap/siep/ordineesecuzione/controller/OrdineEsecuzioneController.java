package siap.siep.ordineesecuzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import f3b.util.Utils;
import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import siap.bdmc.notifichesies.controller.INotificheSies;
import siap.bdmc.notifichesies.model.NotificheSiesModel;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.dao.CampoNotaSqlDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.dao.EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO;
import siap.sico.evento.dao.EventoStoreProcedureAggiornaScadenzarioSimeoneDAO;
import siap.sico.evento.dao.EventoStoreProcedurePulisciDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.dao.AltraCausaDAO;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.archiviazione.dao.ArchiviazioneDAO;
import siap.siep.archiviazione.dao.ArchiviazioneSqlDAO;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepDAO;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepSqlDAO;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.cumulo.dao.CumuloDAO;
import siap.siep.cumulo.dao.CumuloSqlDAO;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.dao.FungibilitaDAO;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelare.dao.MisuraCautelareSqlDAO;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.misurasicurezza.dao.FascMsToFascSiepDAO;
import siap.siep.misurasicurezza.dao.FascMsToFascSiepSqlDAO;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaDAO;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.modulocumulo.dao.DatiFinaliCumuloSqlDAO;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.motivoevento.dao.MotivoEventoDAO;
import siap.siep.motivoevento.model.MotivoEventoModel;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.nomeprovvedimento.model.NomeProvvedimentoModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.dao.OrdineEsecuzioneSqlDao;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.dao.ParametroSqlDAO;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.penacomplessiva.dao.PenaComplessivaDAO;
import siap.siep.penacumulo.dao.PenaCumuloDAO;
import siap.siep.penacumulo.dao.PenaCumuloSqlDAO;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.penasospesa.dao.AnnmanPenacomplDAO;
import siap.siep.penasospesa.dao.AnnmanPenacomplSqlDAO;
import siap.siep.penasospesa.dao.AnnmanReatoDAO;
import siap.siep.penasospesa.dao.AnnmanReatoSqlDAO;
import siap.siep.penasospesa.model.AnnmanPenacomplModel;
import siap.siep.penasospesa.model.AnnmanReatoModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.dao.ReatoDAO;
import siap.siep.rinnovo.dao.RinnovoSqlDAO;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostResiduaDAO;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaDAO;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sospensione.dao.SospensioneDAO;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;

/**
 * Title: OrdineEsecuzioneController
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OrdineEsecuzioneController extends SiapController implements IOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisci Ordine Esecuzione Altra Causa
	 *
	 * @param aEvento
	 * @param aFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOrdineEsecuzioneAltraCausa(EventoNotificaModel aEvento,
			FascicoloSiepModel aFascicoloSiep) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		FascicoloSiepDAO lFasDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();
			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);

			// Setto l'anno e il progressivo...
			lSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
			aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			lEveDao.setDAOFromModel(aEvento.getEvento());

			BigDecimal lKeyEvento = lEveDao.insert();
			lEveRet.getEvento().setIdEvento(lKeyEvento);

			BigDecimal lKeyAutorita = null;
			int count = 0;

			while (count < aEvento.getNotifiche().length) {

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

				count++;
			}
			// Fascicolo Update
			lFasDao = new FascicoloSiepDAO(lConn);

			lFasDao.setDAOFromModelForUpdate(aFascicoloSiep);
			lFasDao.selCondizioneUpdate(aFascicoloSiep.getIdFascicoloSiep());
			lFasDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("EventoController.ExInserisciOrdineEsecuzioneAltraCausa: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("EventoController.ExInserisciEventoNotifica: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lFasDao); // sca

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Inserisce Evento di tipo Ordine Esecuzione, Notifica, Autorita Esterne associate e eventuali Campi note
	 * aggiuntive e Aggiorna Pena Residua.
	 * <p>
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOENotifica(EventoNotificaModel aEvento,
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

			// Setto l'anno e il progressivo...
			lSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
			aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			lEveDao.setDAOFromModel(aEvento.getEvento());

			BigDecimal lKeyEvento = lEveDao.insert();
			lEveRet.getEvento().setIdEvento(lKeyEvento);

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

			// Aggiorna Pena Residua
			if (aPenaResidua != null && aPenaResidua.getIdPenaResidua() != null) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" ---AAAA PENA RES "+aPenaResidua);
				lPenaResDao = new PenaResiduaDAO(lConn);

				lPenaResDao.setIdPenaResidua(aPenaResidua.getIdPenaResidua());

				lPenaResDao.setDataFine(aPenaResidua.getDataFine());

				lPenaResDao.selByKey();
				lPenaResDao.update();
				lPenaResDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOENotifica: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOENotifica: " + ex);
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
	 * Inserisce Evento di tipo Annotazione, Notifica, Autorita Esterne associate e eventuali Campi note
	 * aggiuntive e Aggiorna Pena Residua, e l'ordinanza SIUS collegata .
	 * <p>
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciAnnNotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, String IdEveOrd) throws F3BException {

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

			// Setto l'anno e il progressivo...
			lSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
			aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			lEveDao.setDAOFromModel(aEvento.getEvento());

			BigDecimal lKeyEvento = lEveDao.insert();
			lEveRet.getEvento().setIdEvento(lKeyEvento);

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

			// Aggiorna Pena Residua
			if (aPenaResidua != null && aPenaResidua.getIdPenaResidua() != null) {
				lPenaResDao = new PenaResiduaDAO(lConn);

				lPenaResDao.setIdPenaResidua(aPenaResidua.getIdPenaResidua());

				lPenaResDao.setDataFine(aPenaResidua.getDataFine());

				lPenaResDao.selByKey();
				lPenaResDao.update();
				lPenaResDao.stop();
			}

			// Legame tra Ordinanza della Sorveglianza di 'Applicazione M.S. - Accertamento pericolosita'
			// Sociale' e
			// Il conseguente provvedimento latp SIEP appena inserito.

			/*
			 * lEveDao = new EventoDAO(lConn); lSqlDAO = new EventoSqlDAO(lConn);
			 *
			 * lSqlDAO.ricercaEventoByKey(new BigDecimal(IdEveOrd)); EventoModel lEveOrd = (EventoModel)
			 * lSqlDAO.getModelByKey();
			 *
			 * lEveOrd.setEveIdEvento(lKeyEvento); lEveDao.setDAOFromModel(lEveOrd);
			 *
			 * lEveDao.selByKey(); lEveDao.update(); lEveDao.stop();
			 */
			// commit Finale

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOENotifica: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOENotifica: " + ex);
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
	 * Inserisci O Modifica OE Notifica
	 *
	 * @param aEvento
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaOENotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaDAO lPenaResDao = null;
		// FascicoloSiepSqlDAO lFascDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);

			// Cerca L'OE se presente (By IdFascicolo)
			lSqlDAO = new EventoSqlDAO(lConn);
			lSqlDAO.ricercaOrdineEsecuzioneNonRegistratoByFascicoloSiep(
					aEvento.getEvento().getFasSieIdFascicoloSiep());
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

				lEveDao.setIdEvento(lKeyEvento);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
				// ************************************************************************

				// *********** Cancella le NOTIFICHE associate al EVENTO ******************
				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();
				// ************************************************************************
			}

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

			lPenaResDao = new PenaResiduaDAO(lConn);

			if (aPenaResidua != null && aPenaResidua.getIdPenaResidua() != null) {
				lPenaResDao.setIdPenaResidua(aPenaResidua.getIdPenaResidua());

				if (aPenaResidua != null && aPenaResidua.getDataFine() != null) {
					lPenaResDao.setDataFine(aPenaResidua.getDataFine());
				}

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
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOModificaOENotifica: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOModificaOENotifica: " + ex);
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
	 * Metodo per l'annullamento logico di eventi validati. Deve annullare l'evento, cancellare a cascata le
	 * entita' non piu' necessarie e ripristinare lo stato del fascicolo prima dell'evento che viene
	 * annullato.
	 *
	 * Cancella: Pena residua legata all'evento e Sospensione se presente Fungibilita' legata all'evento
	 *
	 *
	 * @param aEvento
	 *            - evento da annullare
	 * @param aCampoNota
	 *            - Model campo nota contenente le motivazioni dell'annullamento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExAggiornaEventoInserisciCampoNota(EventoModel aEvento, CampoNotaModel aCampoNota)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaSqlDAO lPenSql = null;
		PenaResiduaDAO lPenDAO = null;
		PosizioneGiuridicaSqlDAO lPosSql = null;
		PosizioneGiuridicaDAO lPosDAO = null;
		AnnotazioneManualeSqlDAO lAnnSql = null;
		AnnotazioneManualeDAO lAnnDAO = null;
		ScadenzarioSqlDAO lScaSqlDAO = null;
		ScadenzarioDAO lScaDAO = null;
		SospensioneSqlDAO lSospSqlDAO = null;
		SospensioneDAO lSospDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		FascicoloSiepDAO lFascDao = null;
		ArchiviazioneDAO lArcDao = null;
		ArchiviazioneSqlDAO lArcSqlDao = null;
		CumuloSqlDAO lCumSqlDao = null;
		CumuloDAO lCumDao = null;
		PenaCumuloSqlDAO lPenCumSqlDao = null;
		PenaCumuloDAO lPenCumDao = null;
		MisuraAlternativaSqlDAO lMisSqlDao = null;
		MisuraAlternativaDAO lMisDAO = null;
		LicenzaLibanticipataDAO lLicDao = null;
		SanzioneSostResiduaDAO lSSRDAO = null;
		FungibilitaDAO lFunDao = null;
		AvvocatoFascicoloSiepDAO lAvvFascDao = null;
		AvvocatoFascicoloSiepSqlDAO lAvvFascSqlDao = null;
		NotificaDAO lNotDao = null;
		FascicoloSiepSqlDAO lFascSqlDao = null;

		// TODO Eliminare, se presente Foglio Complementare sulla DOCUMENTO_ALLEGATO
		DocumentoAllegatoDAO lDocDao = null;

		FascMsToFascSiepDAO lFascMsToFascSiepDao = null;
		FascMsToFascSiepSqlDAO lFascMsToFascSiepSqlDao = null;

		// 15-12-2014 Misure Sicurezza
		MisuraSicurezzaSqlDAO lMisSicSqlDao = null;
		MisuraSicurezzaDAO lMisSicDao = null;

		// Ticket#202301250123
		AnnmanReatoDAO annmanReatoDAO = null;
		AnnmanReatoSqlDAO annmanReatoSqlDAO = null;

		AnnmanPenacomplDAO annmanPenacomplDAO = null;
		AnnmanPenacomplSqlDAO annmanPenacomplSqlDAO = null;

		ReatoDAO reatoDAO = null;
		PenaComplessivaDAO penaComplessivaDAO = null;

		SanzioneSostitutivaDAO sanzioneSostDAO = null;
		// Ticket#202301250123 - FINE

		// MEV_2023-13
		// RateizzazionePPDAO rateizzazioneDAO = null;

		Vector lAnnVect = null;
		Vector eventi = null;
		Vector lPosizioni = null;
		// Vector eventiPrec = null;

		CampoNotaModel lCampoMod = null;
		EventoModel lEveRet = new EventoModel(aEvento);
		PosizioneGiuridicaModel lPosMod = null;

		EventoStoreProcedureAggiornaScadenzarioSimeoneDAO lEventoProc = null;
		EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO lEventoProc03 = null;

		// Per Annullamento Nuovo Cumulo
		DatiFinaliCumuloSqlDAO lDatiFinaliCumSqlDao = null;

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);
			lPenDAO = new PenaResiduaDAO(lConn);
			lCampoMod = new CampoNotaModel(aCampoNota);
			lPenSql = new PenaResiduaSqlDAO(lConn);
			lPosSql = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDAO = new PosizioneGiuridicaDAO(lConn);
			lCumSqlDao = new CumuloSqlDAO(lConn);
			lCumDao = new CumuloDAO(lConn);
			lPenCumSqlDao = new PenaCumuloSqlDAO(lConn);
			lPenCumDao = new PenaCumuloDAO(lConn);
			lScaSqlDAO = new ScadenzarioSqlDAO(lConn);
			lScaDAO = new ScadenzarioDAO(lConn);
			EventoSqlDAO lEveSqlDao = null;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("ANNULLO EVENTO ======>> " + aEvento.getIdEvento());

			// ---------------------------------------------------------
			// RICERCA POSIZIONE GIURIDICA ASSOCIATA ALL'EVENTO
			// ---------------------------------------------------------
			lPosSql.ricercaPosizioneGiuridicaByIdEvento(aEvento.getIdEvento());
			lPosMod = (PosizioneGiuridicaModel) lPosSql.getModelByKey();

			// ricerca evento per vedere se e' l'ultimo oppure no
			// n.b. la cancellazione puo' essere effettuata dell'elenco provv del PM o
			// sorveglianza ordinato per data emissione o data inserimento per cui
			// l'evento che cancello potrebbe non essere l'ultimo inserito in ordine
			// cronologico
			// Attenzione la select esclude decreti e ordinanze
			lSqlDAO.ricercaEventiValidatiPerDataInserimentoDescByIdFascicoloSiep(
					aEvento.getFasSieIdFascicoloSiep());
			eventi = new Vector(lSqlDAO.getModels());

			// setto stato procedimento
			lStatoDao = new StatoProcedimentoDAO(lConn);

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			lStatoProcMod.setCodStatoProcedimento("0129"); // 0129 - Ultimo Provvedimento Annullato
			lStatoProcMod.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aCampoNota.getCodOperatoreInserimento());
			lStatoProcMod.setDataInserimento(aCampoNota.getDataInserimento());
			lStatoProcMod.setCodUfficioInserimento(aCampoNota.getCodUfficioInserimento());

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveRet.getDataEmissione());

			// se esite almeno un altro evento...

			if (eventi != null && eventi.size() >= 2) {
				EventoModel UltimoEvento = (EventoModel) eventi.get(0);

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// mLog
				// siesLogger.debug("UltimoEvento EVENTO ======>> " +UltimoEvento.getIdEvento());

				if (UltimoEvento.getIdEvento().compareTo(aEvento.getIdEvento()) >= 0) {

					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di mLog
					// siesLogger.debug("UltimoEvento EVENTO entro ======>> " +UltimoEvento.getIdEvento());
					// ---------------------------------------------------------
					// L'EVENTO CHE SI VUOLE ANNULLARE E' L'ULTIMO di una lista
					// CANCELLA POSIZIONE GIURIDICA
					// ---------------------------------------------------------
					if (lPosMod != null) {
						lPosDAO.setDAOFromModelForUpdate(lPosMod);
						lPosDAO.delete();
						lPosDAO.stop();

						lPosSql.ricercaPosGiuCorrenteByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
						lPosizioni = new Vector(lPosSql.getModels());
						
						// Ticket#20231003017 - Non è detto che esista una PG precendente per cui in alcuni casi
						// lPosizioni.get(0) genera un arrayIndexOutOfBoundException
						if (lPosizioni.size()>0) 
						{
							PosizioneGiuridicaModel lPosPrec = (PosizioneGiuridicaModel) lPosizioni.get(0);
	
							// ---------------------------------------------------------
							// ripristino posizione giuridica pecedente
							// ---------------------------------------------------------
							lPosPrec.setDataFine(null);
							lPosPrec.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
							lPosPrec.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
							lPosPrec.setDataAggiornamento(aCampoNota.getDataInserimento());
	
							lPosDAO.setDAOFromModelForUpdate(lPosPrec);
							lPosDAO.update();
							lPosDAO.stop();
						}
					}

					// ---------------------------------------------------------
					// ricerca evento precedente validato
					// ---------------------------------------------------------
					lSqlDAO.ricercaEventiValidatiPerDataInserimentoDescByIdFascicoloSiep(
							aEvento.getFasSieIdFascicoloSiep());
					// eventiPrec = new Vector(lSqlDAO.getModels());
					EventoModel EventoPrec = (EventoModel) eventi.get(1);
					lSqlDAO.stop();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di mLog
					// siesLogger.debug("evento precedente-->" + EventoPrec);

					// ---------------------------------------------------------
					// ricerca pena residua precedente legata a evento precedente
					// ---------------------------------------------------------
					lPenSql.ricercaPenaResiduaByKeyEvento(EventoPrec.getIdEvento());
					PenaResiduaModel lPenPrecMod = (PenaResiduaModel) lPenSql.getModelByKey();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di mLog
					// siesLogger.debug("pena residua precedente-->" + lPenPrecMod);

					// ---------------------------------------------------------
					// paolo cherubini ripristino la fine della misura uguale al fine pena 22/11/2010
					// solo se il fine misura era gia riempito ed escludendo la detenzione domiciliare a
					// termine
					// se data inizio pena e' null metto anche data inizio misura a null
					// ---------------------------------------------------------
					lMisSqlDao = new MisuraAlternativaSqlDAO(lConn);
					lMisDAO = new MisuraAlternativaDAO(lConn);
					lMisSqlDao.ricercaMisuraAlternativaByIdFascicolo(EventoPrec.getFasSieIdFascicoloSiep());
					MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisSqlDao.getModelByKey();

					// Paolo Cherubini 13/02/2012 b2/rr/008 su segnalazione di Pina Marchese non funziona
					// l'annullamento del verbale sottoscrizione obblighi dopo concessione detenzione
					// domiciliare a termine x cui sposto il controllo nell'if successivo. Il caso segnalato
					// e' fatto da detenuto poi concessione DDT poi libero con differimento provvisorio e di
					// nuovo DDT + verbale SO
					if (lMisMod != null) {
						if (lPenPrecMod == null || lPenPrecMod.getDataInizio() == null) {
							lMisMod.setDataInizioMisura(null);
							lMisMod.setDataFineMisura(null);
						} else {
							if (lMisMod.getDataFineMisura() != null
									&& !"0011".equals(lMisMod.getCodTipoMisura()))
								lMisMod.setDataFineMisura(lPenPrecMod.getDataFine());
						}
						lMisDAO.setDAOFromModelForUpdate(lMisMod);
						lMisDAO.update();
						lMisDAO.stop();
					}

					// ---------------------------------------------------------
					// SCADENZARIO
					// ---------------------------------------------------------
					ScadenzarioModel lScaMod = null;
					ScadenzarioModel lScaModSet = new ScadenzarioModel();
					lScaModSet.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
					// MEV 39 (ELIMINARE ANCHE SCADENZIARIO DI DIFFERIMENTO MISURA SICUREZZA : 21 E LIMINARE
					// ANCHE SCADENZIARIO DI INIZIO MISURA SICUREZZA : 20)
					String[] tipoSca = { "02", "01", "05", "13", "20", "21" };
					lScaSqlDAO.ricercaScadenzarioPerTipoScadenzario(tipoSca,
							aEvento.getFasSieIdFascicoloSiep());
					lScaMod = (ScadenzarioModel) lScaSqlDAO.getModelByKey();

					if (lScaMod != null && lScaMod.getIdScadenzario() != null) {
						if (lPenPrecMod != null && lPenPrecMod.getDataFine() != null) {
							lScaDAO.setDataFineScadenza(lPenPrecMod.getDataFine());
							lScaDAO.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
							lScaDAO.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
							lScaDAO.setDataAggiornamento(aCampoNota.getDataInserimento());
							lScaMod.setFlagVisto("N");

							lScaDAO.setCondizioneUpdate(lScaMod.getIdScadenzario());
							lScaDAO.update();
							lScaDAO.stop();
						}
					}
					// ---------------------------------------------------------
					// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
					// ---------------------------------------------------------
					lStatoDao.setCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
					lStatoDao.delete();
					// ---------------------------------------------------------
					// aggiorno stato procedimento
					// ---------------------------------------------------------
					lStatoDao.setDAOFromModel(lStatoProcMod);
					lStatoDao.insert();
					lStatoDao.stop();
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("L'evento da annullare non e' l'ultimo evento in ordine cronologico "
							+ UltimoEvento.getIdEvento());

					// L'evento da annullare non e' l'ultimo evento in ordine cronologico
					// ---------------------------------------------------------
					// CANCELLA POSIZIONE GIURIDICA associata all'evento
					// ---------------------------------------------------------
					if (lPosMod != null) {
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di mLog
						// siesLogger.debug("cancella pos giu non e' l'ultimo evento-->" + lPosMod);
						lPosDAO.setDAOFromModelForUpdate(lPosMod);
						lPosDAO.delete();
						lPosDAO.stop();
					}
				}
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("ESISTE SOLO UN UNICO EVENTO");
				// ---------------------------------------------------------
				// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
				// ---------------------------------------------------------
				lStatoDao.setCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
				lStatoDao.delete();
				// ---------------------------------------------------------
				// aggiorno stato procedimento
				// ---------------------------------------------------------
				lStatoDao.setDAOFromModel(lStatoProcMod);

				lStatoDao.insert();
				lStatoDao.stop();

				// paolo cherubini 03 novembre 2010
				// quando ho un solo evento aggiungo il ripristino della posizione giuridica iniziale
				// che cmq e' obbligatoria

				if (lPosMod != null) {
					lPosDAO.setDAOFromModelForUpdate(lPosMod);

					// ricerca della misura cautelare da cancellare perche' legata alla posizione giuriduca
					// che
					// devo vancellare
					MisuraCautelareModel misuraCautelare = new MisuraCautelareModel();
					MisuraCautelareSqlDAO lMisCauDao = null;
					misuraCautelare.setPosGiuIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
					lMisCauDao = new MisuraCautelareSqlDAO(lConn);
					lMisCauDao.ricercaMisuraCautelare(misuraCautelare);
					misuraCautelare = (MisuraCautelareModel) lMisCauDao.getModelByKey();
					// MisuraCautelareModel lMod = new MisuraCautelareModel();
					// lMod.setIdMisuraCautelare(lId);
					if (misuraCautelare != null) {
						IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
						lCtrl.ExCancellaMisuraCautelare(misuraCautelare);
					}

					lPosDAO.delete();
					lPosDAO.stop();

					lPosSql.ricercaPosGiuCorrenteByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
					lPosizioni = new Vector(lPosSql.getModels());
					
					// Ticket#20231003017 - Non è detto che esista una PG precendente per cui in alcuni casi
					// lPosizioni.get(0) genera un arryIndexoutOfBoundException
					if (lPosizioni.size()>0) 
					{
						PosizioneGiuridicaModel lPosPrec = (PosizioneGiuridicaModel) lPosizioni.get(0);
	
						// ---------------------------------------------------------
						// ripristino posizione giuridica pecedente
						// ---------------------------------------------------------
						lPosPrec.setDataFine(null);
						lPosPrec.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
						lPosPrec.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
						lPosPrec.setDataAggiornamento(aCampoNota.getDataInserimento());
	
						lPosDAO.setDAOFromModelForUpdate(lPosPrec);
						lPosDAO.update();
						lPosDAO.stop();
	
						// Elenco Provvedimenti PM
						// azione: cancella Provvedimento
						// aggiornamento FLAG_ALTRA_CAUSA nella tabella FASCICOLO_SIEP='S' se
						// POSIZIONE_GIURIDICA.ALT_CAU_ID_ALTRA_CAUSA!=null
						FascicoloSiepDAO lFasDao = null;
						lFasDao = new FascicoloSiepDAO(lConn);
						if (lPosPrec != null && lPosPrec.getAltCauIdAltraCausa() != null && aEvento != null
								&& aEvento.getFasSieIdFascicoloSiep() != null) {
							lFasDao.setFlagAltraCausa("S");
							lFasDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
							lFasDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
							lFasDao.setDataAggiornamento(aCampoNota.getDataInserimento());
							lFasDao.selCondizioneUpdate(aEvento.getFasSieIdFascicoloSiep());
							lFasDao.update();
							lFasDao.stop();
						}
					}
					// Ticket#20231003017 - FINE
				}

			}

			// ---------------------------------------------------------
			/*
			 * modifica il fascicolo e la tabella archiviazione se l'evento e' di questo genere 20 non luogo a
			 * provvedere 21 fine espiazione 22 assorbimento in cumulo 23 provedimento di altra autorita'
			 */
			// ---------------------------------------------------------
			lFascDao = new FascicoloSiepDAO(lConn);
			lArcDao = new ArchiviazioneDAO(lConn);
			lArcSqlDao = new ArchiviazioneSqlDAO(lConn);

			if (aEvento.getCodTipoProvvedimento().equals("20")
					|| aEvento.getCodTipoProvvedimento().equals("21")
					|| aEvento.getCodTipoProvvedimento().equals("22")
					|| aEvento.getCodTipoProvvedimento().equals("23")
					// Nuovo codice. Luigi 17-10-2005
					|| aEvento.getCodTipoProvvedimento().equals("25")
					|| (aEvento.getCodTipoProvvedimento().equals("04") // Archiviazioni RES
							&& (aEvento.getCodMotivo().equals("0400") || aEvento.getCodMotivo().equals("0401")
									|| aEvento.getCodMotivo().equals("0402")
									|| aEvento.getCodMotivo().equals("0403")
									|| aEvento.getCodMotivo().equals("0404")
									|| aEvento.getCodMotivo().equals("0405")
									|| aEvento.getCodMotivo().equals("0406")
									|| aEvento.getCodMotivo().equals("0407")
									|| aEvento.getCodMotivo().equals("0408")
									|| aEvento.getCodMotivo().equals("0409")))) {
				// fascicolo
				lFascDao.setDataArchiviazione(null);
				lFascDao.setCodMotivoArchiviazione("-");
				lFascDao.setCodStatoFascicolo("03");
				lFascDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				lFascDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				lFascDao.setDataAggiornamento(aCampoNota.getDataInserimento());

				lFascDao.selCondizioneUpdate(aEvento.getFasSieIdFascicoloSiep());
				lFascDao.update();
				lFascDao.stop();

				// archiviazione
				lArcSqlDao.ricercaArchiviazioneByIdEvento(aEvento.getIdEvento());
				ArchiviazioneModel lArcMod = new ArchiviazioneModel();
				lArcMod = (ArchiviazioneModel) lArcSqlDao.getModelByKey();

				if (lArcMod != null && lArcMod.getIdArchiviazione() != null) {
					lArcDao.setFlagAnnullamento("S");
					lArcDao.setDataAnnullamento(aCampoNota.getDataInserimento());

					lArcDao.setCondizioneUpdate(lArcMod.getIdArchiviazione());
					lArcDao.update();
					lArcDao.stop();
				}
			}

			// ---------------------------------------------------------
			// Cancello PENA_RESIDUA legata all' Evento selezionato ed eventuali record
			// SOSPENSIONE legati alla pena residua
			//
			// La pena potrebbe essere legata ad altri eventi
			// tramite il campo PEN_ID_PENA_RESIDUA sulla tabella EVENTO,
			// ---------------------------------------------------------

			lPenSql.ricercaPenaResiduaByKeyEvento(lEveRet.getIdEvento());
			lSospSqlDAO = new SospensioneSqlDAO(lConn);
			lSospDAO = new SospensioneDAO(lConn);

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.debug("EveRet.getIdEvento() = "+lEveRet.getIdEvento());

			PenaResiduaModel lPenMod = (PenaResiduaModel) lPenSql.getModelByKey();
			if (lPenMod != null) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// mLog
				// siesLogger.debug("lPenMod != null "+lPenMod.getIdPenaResidua());

				lSospSqlDAO.ricercaSospensioneByIdPenaResidua(lPenMod.getIdPenaResidua());
				Vector lVectSosp = new Vector(lSospSqlDAO.getModels());
				Iterator iter = lVectSosp.iterator();
				while (iter.hasNext()) {
					SospensioneModel lSospMod = (SospensioneModel) iter.next();
					if (lSospMod != null) {
						lSospDAO.setDAOFromModelForUpdate(lSospMod);
						lSospDAO.delete();
						lSospDAO.stop();
					}
				}

				// Se l'evento punta alla pena residua che si sta cancellando
				// viene messo a null il campo PEN_ID_PENA_RESIDUA
				if (lPenMod.getEveIdEvento().equals(lEveRet.getIdEvento())) {
					lEveDao.setIdEvento(lEveRet.getIdEvento());
					lEveDao.setPenIdPenaResidua(null);
					lEveDao.selByKey();
					lEveDao.update();
					lEveDao.stop();
				}

				// Cancello l'eventuale Sanzione Sostitutiva Residua associata alla pena
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello eventuale SS collegata a PR");
				lSSRDAO = new SanzioneSostResiduaDAO(lConn);
				lSSRDAO.setCondizioneUpdateByIdPenRes(lPenMod.getIdPenaResidua());
				lSSRDAO.delete();
				lSSRDAO.stop();

				// paolo cherubini 3 novembre 2010
				// modifico la condizione per la cancellazione della pena residua
				// cancello quindi per eve_id_evento cosi da cancellare le 2 pene residue
				// dell'evento di sospensione
				// il motivo per il quale ci sono 2 pene residue non si sa
				// lPenDAO.setDAOFromModelForUpdate(lPenMod);

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// mLog
				// siesLogger.debug("CANCELLO "+lPenMod.getEveIdEvento());
				lPenDAO.setCondizioneByIdEvento(lPenMod.getEveIdEvento());
				lPenDAO.delete();
				lPenDAO.stop();

				// ---------------------------------------------------------
				// paolo cherubini 23 novembre 2010 cancello anche la pena residua collegata all'evento SIUS
				// naturalmente se l'evento collegato esiste
				// ---------------------------------------------------------
				if (lEveRet.getEveIdEvento() != null) {
					// ricerca evento collegato
					lEveSqlDao = new EventoSqlDAO(lConn);
					EventoModel lEveColl = new EventoModel();
					lEveSqlDao.ricercaEventoByKey(lEveRet.getEveIdEvento());
					lEveColl = (EventoModel) lEveSqlDao.getModelByKey();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di mLog
					// siesLogger.debug("evento collegato");

					// cancello pena residua
					if (lEveColl.getFasSiuIdFascicoloSius() != null) {
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di mLog
						// siesLogger.debug("CANCELLO id evento = "+lEveColl.getIdEvento());
						lPenDAO.setCondizioneByIdEvento(lEveColl.getIdEvento());
						lPenDAO.delete();
						lPenDAO.stop();
					}
				}
			}

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.debug("FINE ----------------------------------------->>>>>>>>>>>>");
			// ========================================================================
			// Cancello fungibilita' legata all'evento (new 22/06/2007)
			// ========================================================================
			lFunDao = new FungibilitaDAO(lConn);
			lFunDao.setCondizioneUpdateEveIdEvento(lEveRet.getIdEvento());
			lFunDao.delete();
			lFunDao.stop();

			/*
			 *
			 *
			 * //a6-rr-328 Paolo e Claudio 23/11/2010
			 * //--------------------------------------------------------- // CANCELLO SCADENZARIO VANE
			 * RICERCHE //---------------------------------------------------------
			 *
			 *
			 * if (lEveRet.getCodMotivo().equals("0057") || lEveRet.getCodMotivo().equals("0133") ||
			 * lEveRet.getCodMotivo().equals("0217") || lEveRet.getCodMotivo().equals("0218") ||
			 * lEveRet.getCodMotivo().equals("0271") || lEveRet.getCodMotivo().equals("0356") ||
			 * lEveRet.getCodMotivo().equals("0357"))
			 *
			 *
			 * (lEveRet.getCodMotivo().equals("0057" || // per la carcerazione - Libero
			 * (lEveRet.getCodMotivo().equals("0078', -- per la carcerazione - Libero
			 * (lEveRet.getCodMotivo().equals("0130', -- per la Carcerazione con rideterminazione pena -
			 * libero (lEveRet.getCodMotivo().equals("0222', -- di unificazione di pene concorrenti (con
			 * contestuale Ordine di Esecuzione Condannato Libero) (lEveRet.getCodMotivo().equals("0217', --
			 * per la carcerazione (lEveRet.getCodMotivo().equals("0218', -- per la carcerazione e Ordine di
			 * Scarcerazione (lEveRet.getCodMotivo().equals("0397', -- Per la carcerazione - Libero -
			 * Conversione Sanzione Sostitutiva (lEveRet.getCodMotivo().equals("0960', -- per la Carcerazione
			 * con rideterminazione pena - libero (lEveRet.getCodMotivo().equals("0057', -- Ordine di
			 * Esecuzione per la carcerazione (condannato libero) (lEveRet.getCodMotivo().equals("0078', --
			 * Revoca decr.sosp. - revoca da PM. / Revoca decr.sosp.- omessa istanza libero / Revoca
			 * decr.sosp.- rigetto istanza libero (lEveRet.getCodMotivo().equals("0079', -- Revoca decr.sosp.-
			 * rigetto istanza det.a.c. / Revoca decr.sosp. - revoca da PM. / Revoca decr.sosp.- rigetto
			 * istanza det.a.c. decorr/scad / Revoca decr.sosp.- omessa istanza det.a.c.decorr/scad / Revoca
			 * decr.sosp.- omessa istanza det.a.c. (lEveRet.getCodMotivo().equals("0080', -- Revoca
			 * decr.sosp.- rigetto istanza arr.dom. / Revoca decr.sosp. - revoca da PM. / Revoca decr.sosp.-
			 * rigetto istanza sosp.arr.dom. (lEveRet.getCodMotivo().equals("0130', -- Ordine Esecuzione per
			 * carcerazione con rideterminazione pena - Libero (lEveRet.getCodMotivo().equals("0131', --
			 * Ordine Esecuzione per carcerazione con rideterminazione pena e ordine di scarcerazione -
			 * Detenuto per questa causa (lEveRet.getCodMotivo().equals(
			 * "0132',	-- Ordine Esecuzione per carcerazione con rideterminazione pena - Detenuto per altra causa / Ordine Esecuzione per carcerazione con rideterminazione pena e ordine scarcerazione - Detenuto per altra causa"
			 * + (lEveRet.getCodMotivo().equals("0133', -- Ordine Esecuzione per carcerazione con
			 * rideterminazione pena - Detenuto per altra causa / Ordine Esecuzione per carcerazione con
			 * rideterminazione pena e ordine scarcerazione - Detenuto per altra causa
			 * (lEveRet.getCodMotivo().equals("0134', -- Ordine Esecuzione per carcerazione con
			 * rideterminazione pena e ordine scarcerazione - Detenuto agli arresti domiciliari
			 * (lEveRet.getCodMotivo().equals("0217', -- Ordine di Esecuzione per la carcerazione -
			 * sospensione (lEveRet.getCodMotivo().equals("0222', -- Esecuzione pene concorrenti - libero
			 * (lEveRet.getCodMotivo().equals("0354', -- Ordine esecuzione per la carcerazione (Rigetto
			 * Differimento) (lEveRet.getCodMotivo().equals("0355', -- Ordine esecuzione per la carcerazione
			 * (Revoca Differimento) (lEveRet.getCodMotivo().equals("0397', -- Ordine di Esecuzione per
			 * sanzione sostitutiva - libero (lEveRet.getCodMotivo().equals("0398', -- Ordine di Esecuzione
			 * per sanzione sostitutiva - detenuto altra causa (lEveRet.getCodMotivo().equals("0399', -- OE
			 * Nuovo Residuo Pena per Revoca Misure Cautelari - Det Questa Causa
			 * (lEveRet.getCodMotivo().equals("0490', -- Ordine di Esecuzione per sanzione sostitutiva -
			 * libero (lEveRet.getCodMotivo().equals("0491', -- Ordine di Esecuzione con sospensione per
			 * sanzione sostitutiva - detenuto altra causa (lEveRet.getCodMotivo().equals("0494', -- Ordine di
			 * Esecuzione Revoca decreto sosp. oe per la carcerazione - sospensione
			 * (lEveRet.getCodMotivo().equals("0495', -- Revoca decr.sosp.- rigetto istanza libero- legge
			 * 199/2010 (lEveRet.getCodMotivo().equals("0935', -- OE Nuovo Residuo Pena per Revoca Misure
			 * Cautelari - In Mis Alt (lEveRet.getCodMotivo().equals("0960', -- Ordine Esecuzione Ridet. Pena
			 * Altro - Libero (lEveRet.getCodMotivo().equals("0961', -- Ordine Esecuzione Ridet. Pena Altro -
			 * Arresti Domiciliari (lEveRet.getCodMotivo().equals("0962') -- Ordine Esecuzione Ridet. Pena
			 * Altro - Detenuto Altra Causa {
			 * lScaDAO.setCondizioneByIdFascicoloSiepTipoScadenzario(lEveRet.getFasSieIdFascicoloSiep(),"03");
			 * lScaDAO.delete(); lScaDAO.stop(); } // se e' un Verbale Vane Ricerche deve reinserire la data
			 * dell'OE precedente. if (lEveRet.getCodMotivo().equals("0313")) { ScadenzarioModel lScaMod =
			 * null; String[] tipoSca ={"03"}; lScaSqlDAO.ricercaScadenzarioPerTipoScadenzario(tipoSca,
			 * lEveRet.getFasSieIdFascicoloSiep()); lScaMod = (ScadenzarioModel) lScaSqlDAO.getModelByKey();
			 * ParametroModel lParMod = new ParametroModel(); lParMod.setNomeParametro("VANE RICERCHE");
			 * lParMod.setCodUfficioValidita(aEvento.getCodUfficioAggiornamento()); Vector lVectPar = null;
			 * IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote(); lVectPar =
			 * lCtrlPar.ExRicercaParametroScadenzario(lParMod); Iterator lIter = lVectPar.iterator(); Date
			 * lSommaAnni = null; Date lSommaMesi = null; Date lFineScadenza = null; if (lIter.hasNext()) {
			 * ParametroModel lParModel = (ParametroModel) lIter.next(); lSommaAnni =
			 * DateUtils.moveDateTo(lScaMod.getDataInizioScadenza(), java.util.Calendar.YEAR,
			 * lParModel.getAnni().intValue()); lSommaMesi = DateUtils.moveDateTo(lSommaAnni,
			 * java.util.Calendar.MONTH, lParModel.getMesi().intValue()); lFineScadenza =
			 * DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
			 * lParModel.getGiorni().intValue()); } lScaDAO.setDataFineScadenza(lFineScadenza);
			 * lScaDAO.setCodStatoNotifica("NP");
			 * lScaDAO.setCondizioneByIdFascicoloSiepTipoScadenzario(lEveRet.getFasSieIdFascicoloSiep(),"03");
			 * lScaDAO.update(); lScaDAO.stop(); } // fine a6-rr-328 Paolo e Claudio 23/11/2010
			 */

			// Paolo Cherubuini 03/02/2011
			// Commento tutta la parte sopra poiche' ho aggiunto la procedura che ricalcolo lo scadenzario 03
			// quindi cancello e via
			// ---------------------------------------------------------
			// CANCELLO SCADENZARIO VERBALE VANE RICERCHE
			// ---------------------------------------------------------
			lScaDAO.setCondizioneByIdFascicoloSiepTipoScadenzario(lEveRet.getFasSieIdFascicoloSiep(), "03");
			lScaDAO.delete();
			lScaDAO.stop();

			// ---------------------------------------------------------
			// CANCELLO SCADENZARIO SIMEONE
			// ---------------------------------------------------------
			// stessa motivazione di sopra
			// lScaDAO.setCondizioneByEveIdEventoTipoScadenzario(lEveRet.getIdEvento(), "01");
			lScaDAO.setCondizioneByIdFascicoloSiepTipoScadenzario(lEveRet.getFasSieIdFascicoloSiep(), "01");
			lScaDAO.delete();
			lScaDAO.stop();

			// MEV_2023-33: storicizzazione dei bollettini
			// MEV_2023-13 - Sgancio le rate dall'evento
			// rateizzazioneDAO = new RateizzazionePPDAO(lConn);
			// rateizzazioneDAO.setEveIdEvento(null);
			// rateizzazioneDAO.selCondizioneByIdEvento(lEveRet.getIdEvento());
			// rateizzazioneDAO.update();
			// rateizzazioneDAO.stop();
			// MEV_2023-13 - FINE

			// ========================================================================
			// Annullo l'evento
			// ========================================================================
			lEveDao.setIdEvento(lEveRet.getIdEvento());
			lEveDao.setAnnIdAnnotazioneManuale(null);
			lEveDao.setPenIdPenaResidua(null);
			lEveDao.setFlagDocumentoRegistrato(lEveRet.getFlagDocumentoRegistrato());
			lEveDao.selByKey();
			lEveDao.update();
			lEveDao.stop();

			// ========================================================================
			// Se l'evento e' l'annotazione di Rideterminazione pena Altro e ha
			// collegato un provvedimento altra autorita', devo annullare anche tale
			// provvedimento che e' stato inserito contestualmente.
			// n.b. lo annullo solo se e' stato inserito dallo stesso utente
			// ========================================================================
			if (aEvento.getCodTipoProvvedimento().equals("25") && (aEvento.getCodMotivo().equals("0948")
					|| aEvento.getCodMotivo().equals("0949") || aEvento.getCodMotivo().equals("0950")
					|| aEvento.getCodMotivo().equals("0951") || aEvento.getCodMotivo().equals("0952")
					|| aEvento.getCodMotivo().equals("0953") || aEvento.getCodMotivo().equals("0954")
					|| aEvento.getCodMotivo().equals("0955") || aEvento.getCodMotivo().equals("0956")
					|| aEvento.getCodMotivo().equals("0957") || aEvento.getCodMotivo().equals("0958")
					|| aEvento.getCodMotivo().equals("0959") || aEvento.getCodMotivo().equals("0987")
					|| aEvento.getCodMotivo().equals("0988") || aEvento.getCodMotivo().equals("1006")// ticket
																										// 20191210013
			) && aEvento.getEveIdEvento() != null) {
				lSqlDAO.ricercaEventoByKey(aEvento.getEveIdEvento());
				EventoModel lEveAltraAut = (EventoModel) lSqlDAO.getModelByKey();
				if (lEveAltraAut.getCodOperatoreInserimento().equals(aEvento.getCodOperatoreInserimento())) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Annullo anche l'evento Altra Autorita = " + lEveAltraAut);
					lEveDao.setIdEvento(lEveAltraAut.getIdEvento());
					lEveDao.setFlagDocumentoRegistrato(lEveRet.getFlagDocumentoRegistrato());
					lEveDao.selByKey();
					lEveDao.update();
					lEveDao.stop();
				}
			}
			// =========================================
			// controllo se l'evento da cancellare e' l'evento di ripristino detenzione in carcere
			// o ordine esecuzione proveniente da detenzione domiciliare
			// =========================================

			if (lEveRet != null
					&& ("0427".equals(lEveRet.getCodMotivo()) || "0246".equals(lEveRet.getCodMotivo()))) {
				// =========================================
				// aggiorno la misura alternativa con flag_situazione a NULL solo se si tratta
				// del ripristino detenzione in carcere o dell'ordine esecuzione
				// DETENZIONE DOMICILIARE A TERMINE
				// =========================================
				lMisSqlDao = new MisuraAlternativaSqlDAO(lConn);
				lMisDAO = new MisuraAlternativaDAO(lConn);

				// Paolo Cherubini 22/11/2010
				// sostituisco query perche' la MA va considerata sul solo quando e' legata ad un evento non
				// annullato
				// lMisSqlDao.ricercaMisuraAlternativaByIdFascicolo (lEveRet.getFasSieIdFascicoloSiep());
				lMisSqlDao.ricercaMisuraAlternativaByIdFascicolo(lEveRet.getFasSieIdFascicoloSiep());
				MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisSqlDao.getModelByKey();

				if (lMisMod != null && ("0011".equals(lMisMod.getCodTipoMisura())
						|| "0197".equals(lMisMod.getCodTipoMisura())
						|| "2340".equals(lMisMod.getCodTipoMisura()))) {
					lMisMod.setFlagSituazione(null);
					lMisDAO.setDAOFromModelForUpdate(lMisMod);
					lMisDAO.update();
					lMisDAO.stop();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("flag situazione " + lMisMod.toString());
				}
			}

			// ---------------------------------------------------------
			// annullo il cumulo
			// ---------------------------------------------------------
			boolean lAnnullaCumulo = false;
			String lFlagPrimo = "";

			lCumSqlDao.ricercaCumuloByEveIdEvento(lEveRet.getIdEvento());
			CumuloModel lCumMod = (CumuloModel) lCumSqlDao.getModelByKey();
			if (lCumMod != null && lCumMod.getIdCumulo() != null) {
				lAnnullaCumulo = true;
				if (lCumMod.getPrimoCumulo() != null)
					lFlagPrimo = lCumMod.getPrimoCumulo();

				lCumDao.setIdCumulo(lCumMod.getIdCumulo());
				lCumDao.setFlagValidato("A");
				lCumDao.selByKey();
				lCumDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				lCumDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				lCumDao.setDataAggiornamento(aCampoNota.getDataInserimento());

				lCumDao.update();
				lCumDao.stop();

				// // ---------------------------------------------------------
				// // rimetto a 'null' il flagCumulante se PrimoCumulo e' uguale a 'P'
				// // ---------------------------------------------------------
				// if ("P".equals(lCumMod.getPrimoCumulo())) {
				// // fascicolo
				// lFascDao.setFlagCumulante(null);
				// lFascDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				// lFascDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				// lFascDao.setDataAggiornamento(aCampoNota.getDataInserimento());
				//
				// lFascDao.selCondizioneUpdate(aEvento.getFasSieIdFascicoloSiep());
				// lFascDao.update();
				// lFascDao.stop();
				// }

				// ---------------------------------------------------------
				// cancello pena_cumulo
				// ---------------------------------------------------------
				lPenCumSqlDao.ricercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo());
				Vector lVectPenCum = new Vector(lPenCumSqlDao.getModels());
				Iterator iter = lVectPenCum.iterator();
				while (iter.hasNext()) {
					PenaCumuloModel lPenCum = (PenaCumuloModel) iter.next();
					lPenCumDao.setDAOFromModelForUpdate(lPenCum);
					lPenCumDao.delete();
					lPenCumDao.stop();
				}
			}

			// ------------------------------------------------------------
			// Nuovo Cumulo
			// ------------------------------------------------------------
			lDatiFinaliCumSqlDao = new DatiFinaliCumuloSqlDAO(lConn);
			lDatiFinaliCumSqlDao.ricercaDatiFinaliCumuloByIdEvento(lEveRet.getIdEvento());
			DatiFinaliCumuloModel lDatiFinMod = (DatiFinaliCumuloModel) lDatiFinaliCumSqlDao.getModelByKey();
			if (lDatiFinMod != null && lDatiFinMod.getIdDatiFinaliCumulo() != null) {
				lAnnullaCumulo = true;
				if (lDatiFinMod.getFlagPrimoCumulo() != null)
					lFlagPrimo = lDatiFinMod.getFlagPrimoCumulo();
			}

			// ---------------------------------------------------------
			// rimetto a 'null' il flagCumulante se PrimoCumulo uguale a 'P'
			// ---------------------------------------------------------
			if (lAnnullaCumulo) {
				if ("P".equals(lFlagPrimo)) {
					// fascicolo
					lFascDao.setFlagCumulante(null);
					lFascDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
					lFascDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
					lFascDao.setDataAggiornamento(aCampoNota.getDataInserimento());

					lFascDao.selCondizioneUpdate(aEvento.getFasSieIdFascicoloSiep());
					lFascDao.update();
					lFascDao.stop();
				}
			}

			// ========================================================================
			// Inserisco il campo nota
			// ========================================================================
			if (!aCampoNota.getDescr().equals("")) {
				lCampoMod.setEveIdEvento(lEveRet.getIdEvento());
				lCampoMod.setProgressivo(new BigDecimal(1));
				lCampoNotaDao.setDAOFromModel(lCampoMod);
				lCampoNotaDao.insert();
				lCampoNotaDao.stop();
			}

			// ------------------------------------------------------------------------------
			// Cancello l'eventuale Foglio Complementare collegato all'evento
			// ------------------------------------------------------------------------------
			// TODO verificare
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello eventuale Foglio Complementare collegato a EVENTO");
			lDocDao = new DocumentoAllegatoDAO(lConn);
			lDocDao.setCondizioneDelete(aEvento.getIdEvento(), "06");
			lDocDao.delete();
			lDocDao.stop();

			// ------------------------------------------------------------------------------
			// Cancella AnnotazioniManuali (Nel caso l'evento sia un'Ordinanza)
			// ------------------------------------------------------------------------------
			// ========================================================================
			// Nel caso di Decisioni del GE amnistia/indulto, prima di cancellare tutte
			// le annotazioni legate alla richiesta corrente, devo aggiornare il
			// campo
			// ANNOTAZIONE_MANUALE.ANNO_ID_ANNOTAZIONE_MANUALE
			// delle Richieste che sono state collegate alle decisioni correnti.
			// ========================================================================

			// ANNA per Pene Sospese maggio 2011
			if (aEvento.getCodTipoProvvedimento().equals("25") && (aEvento.getCodMotivo().equals("1100")
					|| aEvento.getCodMotivo().equals("1101") || aEvento.getCodMotivo().equals("1011") // 01/04/2015
																										// Provvedimento
																										// Revoca
																										// Sanzione
																										// Sostitutiva
																										// in
																										// pena
																										// detentiva
																										// (Art.
																										// 66
																										// L.
																										// 689/81
																										// per
																										// Pene
																										// Pecuniarie)
					|| aEvento.getCodMotivo().equals("1012") // 10/04/2015 Provvedimento Conversione
																// Sanzione Sostitutiva in pena
																// detentiva (Art. 66 L. 689/81 per
																// Pene Pecuniarie)
					|| aEvento.getCodMotivo().equals("1102") || aEvento.getCodMotivo().equals("1103")
					|| aEvento.getCodMotivo().equals("1104") || aEvento.getCodMotivo().equals("1105")
					|| aEvento.getCodMotivo().equals("1106") || aEvento.getCodMotivo().equals("1107")
					|| aEvento.getCodMotivo().equals("1108"))) {
				// non devo cancellare l' annotazione corrispondente perche' contiene i dati della
				// senteza che revoca la sospensione. Devo pero' archiviare il proc. di classe I
				// che avevo creato nell'inserimento dell'annotazione
				// Inizio

				lFascDao.setIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
				lFascDao.selByKey();
				FascicoloSiepModel lFascModel = (FascicoloSiepModel) lFascDao.getModelByKey();
				lFascDao.stop();

				FascicoloSiepDAO lFascDaoI = new FascicoloSiepDAO(lConn);
				lFascDaoI.selCondizioneUpdate(lFascModel.getFasSieIdFascicoloSiep());
				lFascDaoI.setCodStatoFascicolo("01"); // 01 - Archiviato/Definito
				lFascDaoI.setDataArchiviazione(DateUtils.getSysDate());
				lFascDaoI.setCodMotivoArchiviazione("09"); // 09 - Non luogo a provvedere
				lFascDaoI.setFlagValidato("S");
				lFascDaoI.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lFascDaoI.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lFascDaoI.setDataAggiornamento(aEvento.getDataAggiornamento());
				lFascDaoI.update();
				lFascDaoI.stop();

				lFascDaoI.setIdFascicoloSiep(lFascModel.getFasSieIdFascicoloSiep());
				lFascDaoI.selByKey();
				FascicoloSiepModel lFascIModel = (FascicoloSiepModel) lFascDaoI.getModelByKey();
				lFascDaoI.stop();

				StatoProcedimentoDAO lStatoProcDao = null;
				lStatoProcDao = new StatoProcedimentoDAO(lConn);
				lStatoProcDao.setCondizioneByIdFascicolo(lFascIModel.getIdFascicoloSiep());
				lStatoProcDao.delete();
				lStatoProcDao.stop();

				StatoProcedimentoModel lStat = new StatoProcedimentoModel();
				lStat.setFasSieIdFascicoloSiep(lFascIModel.getIdFascicoloSiep());
				lStat.setProgressivo(new BigDecimal(1));
				lStat.setDataInserimento(DateUtils.getSysDate());
				lStat.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
				lStat.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lStat.setCodStatoProcedimento("0305"); // Definito - Archiviazione per N.L.P.: fascicolo
														// iscritto per errore
				lStatoProcDao.setDAOFromModel(lStat);
				lStatoProcDao.insert();

				// Fine
			} else {
				lAnnSql = new AnnotazioneManualeSqlDAO(lConn);
				lAnnDAO = new AnnotazioneManualeDAO(lConn);

				lAnnSql.ricercaAnnotazioneManualeByIdEvento(aEvento.getIdEvento());

				lAnnVect = new Vector(lAnnSql.getModels());

				for (int i = 0; i < lAnnVect.size(); i++) {
					AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lAnnVect.get(i);

					// Aggiorno le annotazioni (richieste) che puntano quella corrente che
					// sto per cancellare (new 01/10/2007)
					lAnnDAO = new AnnotazioneManualeDAO(lConn);
					lAnnDAO.setCondizioneLinkDecisione(lAnnMod.getIdAnnotazioneManuale());
					lAnnDAO.setAnnoIdAnnotazioneManuale(null);
					lAnnDAO.update();
					lAnnDAO.stop();

					// Ticket#202301250123 - Prima di eliminare le annotazoni vanno eliminati eventuali record
					// - ANNMAN_PENACOMPL, PENA_COMPLESSIVA
					// - ANNMAN_REATO, REATO
					siesLogger.debug(
							"Procedo alla cancellazione eventuali ANNMAN_REATO collegati all'annotazione con id = "
									+ lAnnMod.getIdAnnotazioneManuale());
					annmanReatoSqlDAO = new AnnmanReatoSqlDAO(lConn);
					annmanReatoDAO = new AnnmanReatoDAO(lConn);
					reatoDAO = new ReatoDAO(lConn);

					AnnmanReatoModel annReatoModel = new AnnmanReatoModel(null,
							lAnnMod.getIdAnnotazioneManuale(), null);
					annmanReatoSqlDAO.ricercaAnnmanReato(annReatoModel);
					Vector<AnnmanReatoModel> listaAnnReati = new Vector<AnnmanReatoModel>(
							annmanReatoSqlDAO.getModels());
					for (AnnmanReatoModel annRea : listaAnnReati) {
						siesLogger.debug("AnnmanReatoModel id = " + annRea.getIdAnnmanReato());

						siesLogger
								.debug("AnnmanReatoModel Cancello annmareato = " + annRea.getIdAnnmanReato());
						annmanReatoDAO.setCondizioneUpdate(annRea.getIdAnnmanReato());
						annmanReatoDAO.delete();

						siesLogger
								.debug("AnnmanReatoModel Cancello il reato con id = " + annRea.getReatoId());
						reatoDAO.setCondizioneUpdate(annRea.getReatoId());
						reatoDAO.delete();
					}

					// annmanReatoDAO.setCondizioneUpdateByIdAnn(lAnnMod.getIdAnnotazioneManuale());
					// annmanReatoDAO.delete();
					annmanPenacomplSqlDAO = new AnnmanPenacomplSqlDAO(lConn);
					annmanPenacomplDAO = new AnnmanPenacomplDAO(lConn);
					penaComplessivaDAO = new PenaComplessivaDAO(lConn);
					sanzioneSostDAO = new SanzioneSostitutivaDAO(lConn);
					AnnmanPenacomplModel annmaPenMod = new AnnmanPenacomplModel(null,
							lAnnMod.getIdAnnotazioneManuale(), null);
					annmanPenacomplSqlDAO.ricercaAnnmanPenacompl(annmaPenMod);
					Vector<AnnmanPenacomplModel> listaAnnPenaComp = new Vector<AnnmanPenacomplModel>(
							annmanPenacomplSqlDAO.getModels());
					for (AnnmanPenacomplModel annPena : listaAnnPenaComp) {
						siesLogger.debug("AnnmanPenacomplModel = " + annPena.getIdAnnmanPenacompl());

						// cancello prima eventuale SANZIONE_SOSTITUTIVA che punta la pena complessiva
						sanzioneSostDAO.setCondizioneByIdPenaComplessiva(annPena.getPenacomplessivaId());
						sanzioneSostDAO.delete();

						siesLogger.debug("AnnmanReatoModel Cancello annmaPenacomp = "
								+ annPena.getIdAnnmanPenacompl());
						annmanPenacomplDAO.setCondizioneUpdate(annPena.getIdAnnmanPenacompl());
						annmanPenacomplDAO.delete();

						siesLogger
								.debug("AnnmanPenacomplModel Cancello la pena complessiva collegata con id = "
										+ annPena.getPenacomplessivaId());
						penaComplessivaDAO.setCondizioneUpdate(annPena.getPenacomplessivaId());
						penaComplessivaDAO.delete();
					}

					// annmanPenacomplDAO.setCondizioneUpdateByIdAnn(lAnnMod.getIdAnnotazioneManuale());
					// annmanReatoDAO.delete();
					// Ticket#202301250123 - FINE

					// Cancello l'annotazione corrente
					lAnnDAO = new AnnotazioneManualeDAO(lConn);
					lAnnDAO.setDAOFromModelForUpdate(lAnnMod);
					lAnnDAO.delete();
					lAnnDAO.stop();
				}
			}

			// Modifico la tabella misura_cautelare_bdmc per gestione provv annullato
			MisuraCautelareBdmcModel lModBdmc = new MisuraCautelareBdmcModel();
			lModBdmc.setEveIdEvento(aEvento.getIdEvento());
			lModBdmc.setFlagStato("V");
			IMisuraCautelareBdmc lCtrlBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
			Vector misCautBdmc = lCtrlBdmc.ExRicercaMisuraCautelareBdmc(lModBdmc);
			if (misCautBdmc != null && misCautBdmc.size() != 0) {
				for (int kk = 0; kk < misCautBdmc.size(); kk++) {
					lModBdmc = new MisuraCautelareBdmcModel();
					lModBdmc = (MisuraCautelareBdmcModel) misCautBdmc.get(kk);
					lModBdmc.setFlagStato("A");
					if (lModBdmc.getStatoTrasmissioneVal().compareTo("S") == 0)
						lModBdmc.setStatoTrasmissioneVal("N");
					else
						lModBdmc.setStatoTrasmissioneVal("S");
					lCtrlBdmc.ExModificaMisuraCautelareBdmcNoCommit(lConn, lModBdmc, null);
				}
			}

			// -----------------------------------------------------------------------------
			// Inizio modifica Festa Carlo
			// modififico le seguenti tabelle
			// 1) Fascicolo_siep_bdmc
			// 2) Notifiche_Sies
			// per la gestione dell'annullamento del provvedimeno che ha
			// generato l'inserimento dell'associazione
			// ------------------------------------------------------------------------------

			// Aggiorno la tabella Fascicolo_siep_bdmc
			FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel();
			lFasMod.setIdEvento(aEvento.getIdEvento());

			// ===================================================
			// Recupera il controller ed effettua la ricerca
			// dell' associazione
			// ===================================================
			IFascicoloSiepBdmc lCtrl1 = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
			Vector vectFas = lCtrl1.ExRicercaFascicoloSiepBdmc(lFasMod);
			if (vectFas != null && vectFas.size() != 0) {
				FascicoloSiepBdmcModel lFasRetMod = (FascicoloSiepBdmcModel) vectFas.get(0);
				lFasRetMod.setFlagOrdineEsecuzione("N");
				lCtrl1.ExModificaFascicoloSiepBdmc(lFasRetMod);

			}

			// ===================================================
			// Aggiorno la realtiva riga per gestione notifiche
			// Verso Bdmc
			// ===================================================
			NotificheSiesModel lNotMod = new NotificheSiesModel();
			INotificheSies lCtrl2 = BDMCLookupRemote.getNotificheSiesRemote();
			// lNotMod.setTipoNotifica("E");
			lNotMod.setIdEvento(aEvento.getIdEvento());
			Vector notificheSies = lCtrl2.ExRicercaNotificheSies(lNotMod);
			if (notificheSies != null && notificheSies.size() != 0) {
				NotificheSiesModel lAggNotMod = (NotificheSiesModel) notificheSies.get(0);
				lAggNotMod.setTipoNotifica("C");
				if (lAggNotMod.getStatoTrasmissione().compareTo("S") == 0)
					lAggNotMod.setStatoTrasmissione("N");
				else
					lAggNotMod.setStatoTrasmissione("S");
				lCtrl2.ExModificaNotificheSiesNoCommit(lConn, lAggNotMod);

			}

			// ------------------------------------------------------------------------------
			// Aggiorna le Liberazioni Anticipate
			// come non elaborate
			// ------------------------------------------------------------------------------
			if (aEvento.getEveIdEvento() != null) {
				lLicDao = new LicenzaLibanticipataDAO(lConn);

				lLicDao.setCondizioneIdEvento(aEvento.getEveIdEvento());
				lLicDao.start();

				if (lLicDao.next()) {
					lLicDao.stop();

					lLicDao.setFlagElaborato("N");
					lLicDao.setCondizioneIdEvento(aEvento.getEveIdEvento());
					lLicDao.update();
					lLicDao.stop();
				}
			}

			// ========================================================================
			// Se trattasi di annullamento di un provvedimento di cumulo, cancello
			// fisicamente le eventuali LA iscritte in cumulo
			// ========================================================================
			if (aEvento.getCodTipoProvvedimento().equals("04") && (aEvento.getCodMotivo().equals("0222")
					|| aEvento.getCodMotivo().equals("0223") || aEvento.getCodMotivo().equals("0224")
					|| aEvento.getCodMotivo().equals("0277"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello le LA collegate al provvedimeto di cumulo");
				lLicDao = new LicenzaLibanticipataDAO(lConn);

				lLicDao.setCondizioneIdEvento(aEvento.getIdEvento());
				lLicDao.delete();
				lLicDao.stop();
			}

			// ---------------------------------------------------------
			// paolo 4 dicembre 2008 annullamento sostituzione avvocato d'ufficio
			// ---------------------------------------------------------
			// lAvvFascDao = new AvvocatoFascicoloSiepDAO(lConn);
			lAvvFascDao = new AvvocatoFascicoloSiepDAO(lConn);
			lAvvFascSqlDao = new AvvocatoFascicoloSiepSqlDAO(lConn);
			// 06/04/2010 Revisione Codici Motivo per Pene Accessorie.
			// if (aEvento.getCodMotivo().equals("5137")){
			if (aEvento.getCodMotivo().equals("5407")) {

				// cerco il collegamento avvocato-fascicolo collegato all'evento da annullare
				lAvvFascSqlDao.ricercaAvvocatiByKeyEvento(aEvento.getIdEvento());
				AvvocatoFascicoloSiepModel lAvvFascMod = null;
				lAvvFascSqlDao.start();
				if (lAvvFascSqlDao.next())
					lAvvFascMod = (AvvocatoFascicoloSiepModel) lAvvFascSqlDao.getModelAvvFas();
				lAvvFascSqlDao.stop();

				// annullo la data fine validita del collegamento avvocato-fascicolo
				// sostituito con l'evento poi annullato, cosi lo ripristino
				if (lAvvFascMod != null && lAvvFascMod.getAvvIdAvvocatoFascicoloSost() != null) {
					lAvvFascDao.selCondizioneUpdateIdAvvFascSiep(lAvvFascMod.getAvvIdAvvocatoFascicoloSost());
					lAvvFascDao.setDataFineValidita(null);
					lAvvFascDao.update();
					lAvvFascSqlDao.stop();
				}

				// sbianco le notifiche collegate all'evento annullato
				if (aEvento != null && aEvento.getIdEvento() != null) {
					lNotDao = new NotificaDAO(lConn);
					lNotDao.setCondizioneEvento(aEvento.getIdEvento());
					lNotDao.setAvvIdAvvocatoFascicoloSiep(null);
					lNotDao.update();
					lNotDao.stop();
				}

				// riavvaloro eventuali notifiche dell'avvocato ripristinato
				// (in realta' le notifiche sono collegate al collegamento avvocato-fascicolo)
				if (lAvvFascMod != null && lAvvFascMod.getAvvIdAvvocatoFascicoloSost() != null
						&& lAvvFascMod.getIdAvvocatoFascicoloSiep() != null) {
					lNotDao = new NotificaDAO(lConn);
					// NotificaModel lNotAvvMod = new NotificaModel();
					// lNotAvvMod.setAvvIdAvvocatoFascicoloSiep(lAvvFascMod.getIdAvvocatoFascicoloSiep());
					lNotDao.setCondizioneAvvIdAvvocatoFascicoloSiep(lAvvFascMod.getIdAvvocatoFascicoloSiep());
					lNotDao.setAvvIdAvvocatoFascicoloSiep(lAvvFascMod.getAvvIdAvvocatoFascicoloSost());
					lNotDao.update();
					lNotDao.stop();
				}

				if (aEvento != null && aEvento.getIdEvento() != null) {
					// cancello avvocato collegato all'evento da annullare
					lAvvFascDao.selCondizioneDeleteEveIdEvento(aEvento.getIdEvento());
					lAvvFascDao.delete();
					lAvvFascDao.stop();
				}

			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(" passo qui =" + aEvento.getCodMotivo());

			if (aEvento.getCodMotivo().equals("0942")) {

				// =========================================
				// Paolo Cherubini 29/04/2011 inserisco annullamento richiesta di conversione
				// evento di tipo 01 - 12 - 0942
				// se sono un procedimento di classe I devo cercare il procedimento di classe VII che lo punta
				// e cancellare tale puntamento
				// se sono un procedimento di classe VII cancello il puntamento al procedimento di classe I
				// =========================================

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" si e' una richiesta conversione");

				lFascSqlDao = new FascicoloSiepSqlDAO(lConn);
				lFascSqlDao.ricercaFascicoloByKey(aEvento.getFasSieIdFascicoloSiep());
				FascicoloSiepModel lFascMod = (FascicoloSiepModel) lFascSqlDao.getModelByKey();

				int lFascProg = lFascMod.getChiaveProgr().intValue();
				if (lFascProg > 70000 && lFascProg < 80000) {
					// se sono in classe VII
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug(" sono in classe VII idfas = " + lFascMod.getIdFascicoloSiep());
					lFascDao.setFasSieIdFascicoloSiep(null);
					lFascDao.selCondizioneUpdate(lFascMod.getIdFascicoloSiep());
					lFascDao.update();
					lFascDao.stop();
				} else {
					// se sono in classe I
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug(" sono in classe I idfas classe VII = " + lFascMod.getIdFascicoloSiep());
					lFascDao.setFasSieIdFascicoloSiep(null);
					lFascDao.selCondizioneUpdateFasSieid(lFascMod.getIdFascicoloSiep());
					lFascDao.update();
					lFascDao.stop();
				}
			}

			// ==========================================================================
			// Se evento di Annotazione Esito Iscrizione procedimento di classe IV,
			// devo eliminare il riferimento sulla tabella FASC_MS_TO_FASC_SIEP
			// new d.f. 13/01/2014
			// ==========================================================================
			if (aEvento.getCodMotivo().equals("5200")) {
				lFascMsToFascSiepSqlDao = new FascMsToFascSiepSqlDAO(lConn);
				FascMsToFascSiepModel lFascMSModel = null;
				lFascMsToFascSiepSqlDao.ricercaByEveIdEvento(aEvento.getIdEvento());
				lFascMSModel = (FascMsToFascSiepModel) lFascMsToFascSiepSqlDao.getModelByKey();
				lFascMsToFascSiepSqlDao.stop();

				if (lFascMSModel != null && lFascMSModel.getIdFascMsToFascSiep() != null) {
					lFascMsToFascSiepDao = new FascMsToFascSiepDAO(lConn);
					lFascMsToFascSiepDao.setCondizioneUpdate(lFascMSModel.getIdFascMsToFascSiep());
					lFascMsToFascSiepDao.delete();
					lFascMsToFascSiepDao.stop();
				}
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(
					"OrdineEsecuzioneController.ExAggiornaEventoInserisciCampoNota -- XXXX -- Misure Sicurezza");
			// ==================================================================
			// Annulla Eventuale Misura_Sicurezza collegata all'evento Annullato
			// e Ripristino della Vecchia Misura collegata alla Misura Annullata
			// ===================================================================
			lMisSicSqlDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisSicDao = new MisuraSicurezzaDAO(lConn);

			MisuraSicurezzaModel lMisMod = null;
			MisuraSicurezzaModel lMisOldMod = null;
			Vector MisSicVec = null;
			Vector OldMisSicVec = null;

			lMisSicSqlDao.ricercaMisuraSicurezzaByEventoKey(aEvento.getIdEvento());
			MisSicVec = new Vector(lMisSicSqlDao.getModels());
			lMisSicSqlDao.stop();

			// -- Misura da Annullare (inserita dall'evento che si sta annullando)
			// -------------------------------
			BigDecimal lNewMisKey = null;
			if (MisSicVec != null && MisSicVec.size() > 0) {
				Iterator Itx1 = MisSicVec.iterator();
				while (Itx1.hasNext()) {
					lMisMod = (MisuraSicurezzaModel) Itx1.next();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.info("--XX-- Misura da Annullare - " + lMisMod);
					if (lMisMod != null && lMisMod.getIdMisuraSicurezza() != null) {
						lNewMisKey = lMisMod.getIdMisuraSicurezza();
						lMisMod.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
						lMisMod.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
						lMisMod.setDataAggiornamento(aCampoNota.getDataInserimento());
						lMisMod.setFlagAnnullaMisura("A");

						lMisSicDao.setDAOFromModelForUpdate(lMisMod);
						lMisSicDao.update();
						lMisSicDao.stop();

						// Vecchia Misura (Da Ripristinare al posto di quella annullata)
						lMisSicSqlDao.ricercaMisuraSicurezzaByKeyMisuraCollegata(lNewMisKey);
						OldMisSicVec = new Vector(lMisSicSqlDao.getModels());
						lMisSicSqlDao.stop();

						if (OldMisSicVec != null && OldMisSicVec.size() > 0) {
							Iterator Itx2 = OldMisSicVec.iterator();
							while (Itx2.hasNext()) {
								lMisOldMod = (MisuraSicurezzaModel) Itx2.next();
								// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
								// siesLogger al posto di LogF3B.getLogger()
								// siesLogger.info("--XX-- Misura da Ripristina - " + lMisOldMod);
								if (lMisOldMod != null && lMisOldMod.getIdMisuraSicurezza() != null) {
									lMisOldMod.setDataAggiornamento(aCampoNota.getDataInserimento());
									lMisOldMod.setCodUfficioAggiornamento(
											aCampoNota.getCodUfficioInserimento());
									lMisOldMod.setCodOperatoreAggiornamento(
											aCampoNota.getCodOperatoreInserimento());
									lMisOldMod.setDataFineValidita(null);

									lMisSicDao.setDAOFromModelForUpdate(lMisOldMod);
									lMisSicDao.update();
									lMisSicDao.stop();
								}
							}
						}
					}
				}
			}
			//

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("EventoStoreProcedureAggiornaScadenzarioSimeoneDAO");

			// =========================================
			// Richiama la store procedure per ripristinare lo scadenzario Simeone
			// =========================================

			BigDecimal par_anno = new BigDecimal(DateUtils.getSysDate("yyyy"));
			String par_bdi = "";
			String par_ufficio = aEvento.getCodUfficioInserimento();
			BigDecimal par_id_fascicolo = aEvento.getFasSieIdFascicoloSiep();
			lEventoProc = new EventoStoreProcedureAggiornaScadenzarioSimeoneDAO(lConn);
			lEventoProc.setpar_anno(par_anno);
			lEventoProc.setpar_bdi(par_bdi);
			lEventoProc.setpar_ufficio(par_ufficio);
			lEventoProc.setpar_id_fascicolo(par_id_fascicolo);
			lEventoProc.execute();

			// =========================================
			// Richiama la store procedure per ripristinare lo scadenzario 03 Vane Ricerche
			// =========================================

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("par_anno = " + par_anno + "par_bdi = " + par_bdi);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("par_ufficio = " + par_ufficio + "par_id_fascicolo = " + par_id_fascicolo);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO");

			// BigDecimal par_anno = new BigDecimal(DateUtils.getSysDate("yyyy"));
			// String par_bdi = "";
			// String par_ufficio = aEvento.getCodUfficioInserimento();
			// BigDecimal par_id_fascicolo = aEvento.getFasSieIdFascicoloSiep();
			lEventoProc03 = new EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO(lConn);
			lEventoProc03.setpar_anno(par_anno);
			lEventoProc03.setpar_bdi(par_bdi);
			lEventoProc03.setpar_ufficio(par_ufficio);
			lEventoProc03.setpar_id_fascicolo(par_id_fascicolo);
			lEventoProc03.execute();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("fine EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO");

			commit(lConn);
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();

			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExAggiornaEventoInserisciCampoNota: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("OrdineEsecuzioneController.ExAggiornaEventoInserisciCampoNota: ", ex);
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExAggiornaEventoInserisciCampoNota: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lPenSql);
			cleanup(lPenDAO);
			cleanup(lEveDao);
			cleanup(lSqlDAO);
			cleanup(lAnnSql);
			cleanup(lAnnDAO);
			cleanup(lPosSql);
			cleanup(lPosDAO);
			cleanup(lStatoDao);
			cleanup(lScaSqlDAO);
			cleanup(lScaDAO);
			cleanup(lFascDao);
			cleanup(lArcDao);
			cleanup(lArcSqlDao);
			cleanup(lSospDAO);
			cleanup(lSospSqlDAO);
			cleanup(lCumSqlDao);
			cleanup(lCumDao);
			cleanup(lPenCumSqlDao);
			cleanup(lPenCumDao);
			cleanup(lMisSqlDao);
			cleanup(lMisDAO);
			cleanup(lLicDao);
			cleanup(lSSRDAO);
			cleanup(lFunDao);
			cleanup(lAvvFascDao);
			cleanup(lAvvFascSqlDao);
			cleanup(lNotDao);
			cleanup(lFascSqlDao);
			cleanup(lDocDao);
			cleanup(lEventoProc);
			cleanup(lFascMsToFascSiepSqlDao);
			cleanup(lFascMsToFascSiepDao);
			cleanup(lMisSicSqlDao);
			cleanup(lMisSicDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lEventoProc03);
			cleanup(lDatiFinaliCumSqlDao);

			// Ticket#202301250123 -
			cleanup(annmanReatoDAO);
			cleanup(annmanPenacomplDAO);

			cleanup(annmanReatoSqlDAO);
			cleanup(annmanPenacomplSqlDAO);

			cleanup(reatoDAO);
			cleanup(penaComplessivaDAO);

			cleanup(sanzioneSostDAO);

			// Ticket#202301250123 - FINE

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * cancellazione fisica di evento e di tutte le sue chiavi in cascata utilizzando la store procedure
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExCancellaEventoConStoreProcedure(EventoModel aEvento) throws F3BException {

		Connection lConn = null;

		EventoModel lEveRet = new EventoModel(aEvento);
		EventoStoreProcedurePulisciDAO lEventoProc = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaDAO lMisDAO = null;
		EventoSqlDAO lEveSqlDAO = null;

		try {
			lConn = getDBTransaction();
			// =========================================
			// Richiama la store procedure per cancellare l'evento selezionato
			// =========================================
			lEventoProc = new EventoStoreProcedurePulisciDAO(lConn);
			lEventoProc.setIdEvento(lEveRet.getIdEvento());
			lEventoProc.execute();

			// ========================================================================
			// Se l'evento e' l'annotazione di Rideterminazione pena Altro e ha
			// collegato un provvedimento altra autorita', devo annullare anche tale
			// provvedimento che e' stato inserito contestualmente.
			// n.b. lo annullo solo se e' stato inserito dallo stesso utente
			// ========================================================================
			if (aEvento.getCodTipoProvvedimento().equals("25")
					&& (aEvento.getCodMotivo().equals("0948") || aEvento.getCodMotivo().equals("0949")
							|| aEvento.getCodMotivo().equals("0950") || aEvento.getCodMotivo().equals("0951")
							|| aEvento.getCodMotivo().equals("0952") || aEvento.getCodMotivo().equals("0953")
							|| aEvento.getCodMotivo().equals("0954") || aEvento.getCodMotivo().equals("0955")
							|| aEvento.getCodMotivo().equals("0956") || aEvento.getCodMotivo().equals("0957")
							|| aEvento.getCodMotivo().equals("0958") || aEvento.getCodMotivo().equals("0959")
							// ==========================================
							// 12/12/2014 aggiunti ulteriori codici che non erano stati censiti
							|| aEvento.getCodMotivo().equals("1005") // Altra Aut
							|| aEvento.getCodMotivo().equals("0987") // GE
							|| aEvento.getCodMotivo().equals("0988") // GE
							|| aEvento.getCodMotivo().equals("1006") // GE
							|| aEvento.getCodMotivo().equals("0994") // Giudice di Sorveglianza
							|| aEvento.getCodMotivo().equals("0999") // Giudice di Sorveglianza
					) && aEvento.getEveIdEvento() != null) {
				lEveSqlDAO = new EventoSqlDAO(lConn);
				lEveSqlDAO.ricercaEventoByKey(aEvento.getEveIdEvento());
				EventoModel lEveAltraAut = (EventoModel) lEveSqlDAO.getModelByKey();
				lEveSqlDAO.stop();

				if (lEveAltraAut.getCodOperatoreInserimento().equals(aEvento.getCodOperatoreInserimento())) {
					// lEventoProc = new EventoStoreProcedurePulisciDAO(lConn);
					lEventoProc.setIdEvento(aEvento.getEveIdEvento());
					lEventoProc.execute();
				}
			}

			// ==============================================================================
			// controllo se l'evento da cancellare e' l'evento di ripristino detenzione in carcere
			// o ordine esecuzione proveniente da detenzione domiciliare
			// ==============================================================================
			if (lEveRet != null
					&& ("0427".equals(lEveRet.getCodMotivo()) || "0246".equals(lEveRet.getCodMotivo()))) {
				// ==============================================================================
				// aggiorno la misura alternativa con flag_situazione a NULL solo se si tratta
				// del ripristino detenzione in carcere o dell'ordine esecuzione
				// DETENZIONE DOMICILIARE A TERMINE
				// ==============================================================================
				lMisDao = new MisuraAlternativaSqlDAO(lConn);
				lMisDAO = new MisuraAlternativaDAO(lConn);

				// Paolo Cherubini 22/11/2010
				// sostituisco query perche' la MA va considerata sul solo quando e' legata ad un evento non
				// annullato
				// lMisDao.ricercaMisuraAlternativaByIdFascicolo(lEveRet.getFasSieIdFascicoloSiep());
				lMisDao.ricercaMisuraAlternativaByIdFascicolo(lEveRet.getFasSieIdFascicoloSiep());

				MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

				if (lMisMod != null && ("0011".equals(lMisMod.getCodTipoMisura())
						|| "0197".equals(lMisMod.getCodTipoMisura())
						|| "2340".equals(lMisMod.getCodTipoMisura()))) {
					lMisMod.setFlagSituazione(null);
					lMisDAO.setDAOFromModelForUpdate(lMisMod);

					lMisDAO.update();
					lMisDAO.stop();
				}
			}

			// Ticket#20190723011 — Anomalia fascicolo SIEP: 20190725 [SG]
			// Durante la concessione dell'affidamento in prova si può creare un SIUS manualmente
			// Alla cancellazione del SIEP il SIUS rimane appeso
			if (Utils.isPresent(lEveRet.getEveIdEvento())) {
				lEveSqlDAO = new EventoSqlDAO(lConn);
				lEveSqlDAO.ricercaEventoByKey(lEveRet.getEveIdEvento());
				EventoModel em = (EventoModel) lEveSqlDAO.getModelByKey();
				lEveSqlDAO.stop();
				// 20211013 [SG]: a seguito di correzione della SP "PULISCI.Pulisci_Evento"
				// il SIUS ("lEveRet.getEveIdEvento()") viene cancellato già, per scrupolo lasciamo questo
				// controllo; da Rideterminazione della Pena - Altro
				if (!Utils.isNullObj(em)) {
					if (("02".equals(em.getCodTipoProvvedimento())
							|| "03".equals(em.getCodTipoProvvedimento()))
							&& "01".equals(em.getCodTipoEvento()) && em.getDataTrasmissioneAtti() != null
							&& em.getDataTrasmissioneAtti().compareTo(em.getDataEmissione()) == 0
							&& em.getCodOperatoreInserimento().equals(aEvento.getCodOperatoreInserimento())) {
						lEventoProc.setIdEvento(lEveRet.getEveIdEvento());
						lEventoProc.execute();
					}
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExCancellaEventoConStoreProcedure: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExCancellaEventoConStoreProcedure: " + ex);
		} finally {
			cleanup(lEventoProc);
			cleanup(lMisDao);
			cleanup(lMisDAO);
			cleanup(lEveSqlDAO);
			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Emissione Evento Annotazione Manuale
	 *
	 * @param aEvento
	 * @param aPenMod
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaEventoNotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenMod) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		OrdineEsecuzioneSqlDao lOrdSql = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaDAO lPenaResDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lOrdSql = new OrdineEsecuzioneSqlDao(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);

			// Cerca L'OE se presente (By IdFascicolo)
			lOrdSql.ricercaEventoOERPNonRegistratoByFascicoloSiep(
					aEvento.getEvento().getFasSieIdFascicoloSiep(), aEvento.getEvento().getCodTipoEvento(),
					aEvento.getEvento().getCodTipoProvvedimento());
			EventoModel lEvePresente = (EventoModel) lOrdSql.getModelByKey();
			BigDecimal lKeyEvento = null;
			if (lEvePresente != null) // Se presente lo aggiorno
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

				lEveDao.setIdEvento(lKeyEvento);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
				// ************************************************************************

				// *********** Cancella le NOTIFICHE associate al EVENTO ******************
				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();
				// ************************************************************************
			} else // Se non presente lo inserisco
			{
				// Setto l'anno e il progressivo...
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
				aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				lEveDao.setDAOFromModel(aEvento.getEvento());
				lKeyEvento = lEveDao.insert();
				lEveRet.getEvento().setIdEvento(lKeyEvento);
			}

			BigDecimal lKeyAutorita = null;
			int count = 0;

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
			// Aggiorna Pena Residua
			lPenaResDao = new PenaResiduaDAO(lConn);
			if (aPenMod != null && aPenMod.getIdPenaResidua() != null && aPenMod.getDataFine() != null) {
				lPenaResDao.setIdPenaResidua(aPenMod.getIdPenaResidua());

				lPenaResDao.setDataFine(aPenMod.getDataFine());

				lPenaResDao.selByKey();
				lPenaResDao.update();
				lPenaResDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"OrdineEsecuzioneController.ExInserisciOModificaEmissioneNotifica: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOModificaEmissioneNotifica: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lOrdSql);
			cleanup(lPenaResDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * METODO DI INSERIMENTO LEGGE SIMEONE
	 *
	 * @param aEvento
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaLSNotifica(EventoNotificaModel aEvento,
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
			lSqlDAO.ricercaOrdineEsecuzioneNonRegistratoByFascicoloSiep(
					aEvento.getEvento().getFasSieIdFascicoloSiep());
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

				lEveDao.setIdEvento(lKeyEvento);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				// *********** Cancella le NOTIFICHE associate all' EVENTO ******************
				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();
				// ************************************************************************
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

			/*
			 * // Aggiorna Pena Residua if( aPenaResidua != null ) { lPenaResDao = new PenaResiduaDAO(lConn);
			 *
			 * lPenaResDao.setIdPenaResidua(aPenaResidua.getIdPenaResidua());
			 *
			 * lPenaResDao.setDataFine(aPenaResidua.getDataFine());
			 *
			 * lPenaResDao.selByKey(); lPenaResDao.update(); lPenaResDao.stop(); }
			 */

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOModificaLSNotifica: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOModificaLSNotifica: " + ex);
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
	 * METODO DI INSERIMENTO "REVOCA" LEGGE SIMEONE
	 *
	 * @param aEvento
	 * @param aPenaResidua
	 * @param aMotEve
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaRevocaLSNotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, MotivoEventoModel aMotEve) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaDAO lPenaResDao = null;
		MotivoEventoDAO lMotEveDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);
			lMotEveDao = new MotivoEventoDAO(lConn);

			// Cerca L'OE se presente (By IdFascicolo)
			lSqlDAO = new EventoSqlDAO(lConn);
			lSqlDAO.ricercaOrdineEsecuzioneRevocaLSNonRegistratoByFascicoloSiep(
					aEvento.getEvento().getFasSieIdFascicoloSiep());
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

				lEveDao.setIdEvento(lKeyEvento);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				// *********** Cancella le NOTIFICHE associate all' EVENTO ******************
				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();
				// ************************************************************************
			}

			// motivo evento
			aMotEve.setEveIdEvento(lKeyEvento);
			lMotEveDao.setDAOFromModel(aMotEve);
			lMotEveDao.insert();
			lMotEveDao.stop();

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

			// Aggiorna Pena Residua (che deve essere presente)
			lPenaResDao = new PenaResiduaDAO(lConn);

			lPenaResDao.setIdPenaResidua(aPenaResidua.getIdPenaResidua());

			lPenaResDao.setDataFine(aPenaResidua.getDataFine());

			lPenaResDao.selByKey();
			lPenaResDao.update();
			lPenaResDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException(
					"OrdineEsecuzioneController.ExInserisciOModificaRevocaLSNotifica: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOModificaRevocaLSNotifica: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lPenaResDao);
			cleanup(lMotEveDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Ricerca OE Scadenzario Evento By Fascicolo
	 *
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaOEScadenzarioEventoByFascicolo(BigDecimal aKeyFascicolo) throws F3BException {

		Connection lConn = null;
		Vector lRicercaVect = new Vector();
		OrdineEsecuzioneSqlDao lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new OrdineEsecuzioneSqlDao(lConn);
			lRicDao.ricercaEventoNonNotificatoByIdFascicolo(aKeyFascicolo);
			lRicDao.start();

			while (lRicDao.next()) {
				lRicercaVect.add(lRicDao.getModel());
			}

			lRicDao.stop();

			if (lRicercaVect.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"OrdineEsecuzioneController.ExRicercaOEScadenzarioEventoByFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}

		return lRicercaVect;
	}

	/**
	 * Ricerca tutti gli eventi
	 *
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaTuttiEventiByFascicolo(BigDecimal aKeyFascicolo) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("OrdineEsecuzioneController.ExRicercaTuttiEventiByFascicolo");

		Connection lConn = null;

		Vector lRicVect = new Vector();
		OrdineEsecuzioneSqlDao lRicDao = null;

		try {
			lConn = getDBConnection();

			lRicDao = new OrdineEsecuzioneSqlDao(lConn);

			// Ricerca Eventi
			lRicDao.ricercaTuttiEventiByIdFascicolo(aKeyFascicolo);
			lRicVect = new Vector(lRicDao.getModels());
			lRicDao.stop();

			if (lRicVect.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("OrdineEsecuzioneController.ExRicercaTuttiEventiByFascicolo: " + daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}

		return lRicVect;
	}

	/**
	 * Ricerca Tutti Eventi By Fascicolo Paged
	 *
	 * @param aKeyFascicolo
	 * @param aPage
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaTuttiEventiByFascicoloPaged(BigDecimal aKeyFascicolo, int aPage)
			throws F3BException {

		Connection lConn = null;

		Vector lRicVect = new Vector();
		OrdineEsecuzioneSqlDao lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new OrdineEsecuzioneSqlDao(lConn);

			// Ricerca Eventi
			lRicDao.ricercaTuttiEventiByIdFascicoloPaged(aKeyFascicolo, aPage);
			lRicVect = new Vector(lRicDao.getModels());
			lRicDao.stop();

			if (lRicVect.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("OrdineEsecuzioneController.ExRicercaTuttiEventiByFascicolo: " + daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}

		return lRicVect;
	}

	/**
	 * ExGetCountEventi
	 *
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountEventi(BigDecimal aKeyFascicolo) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		OrdineEsecuzioneSqlDao lSqlDao = null;
		try {
			lConn = getDBConnection();
			lSqlDao = new OrdineEsecuzioneSqlDao(lConn);
			lSqlDao.getCountEventiStatoEsecuzione(aKeyFascicolo);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("OrdineEsecuzioneController.ExGetCountEventi: " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * RICERCA EVENTI DI TIPO: PROVVEDIMENTO, RICHIESTA, ISTANZA
	 *
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventiPerCodiceTipoEventoByFascicolo(BigDecimal aKeyFascicolo)
			throws F3BException {

		Connection lConn = null;
		Vector lEveVect = new Vector();
		OrdineEsecuzioneSqlDao lRicDao = null;
		try {
			lConn = getDBConnection();

			lRicDao = new OrdineEsecuzioneSqlDao(lConn);

			lRicDao.ricercaEventiPerCodiceTipoEventoByIdFascicolo(aKeyFascicolo);

			lRicDao.start();
			while (lRicDao.next()) {
				lEveVect.add(lRicDao.getModel());
			}
			lRicDao.stop();

			if (lEveVect.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"OrdineEsecuzioneController.ExRicercaEventiConCodiceMotivoByFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRicDao);

			cleanup(lConn);
		}

		return lEveVect;
	}

	/**
	 * RICERCA EVENTO E CAMPO NOTA ASSOCIATO
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public CampoNotaModel ExRicercaEventoCampoNotaByIdEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		CampoNotaSqlDAO lCampoNotaSqlDAO = null;
		CampoNotaModel lCampoMod = null;

		try {
			lConn = getDBConnection();
			lCampoNotaSqlDAO = new CampoNotaSqlDAO(lConn);
			lCampoNotaSqlDAO.ricercaCampoNotaByKeyEventoDesc(aKey);
			lCampoMod = (CampoNotaModel) lCampoNotaSqlDAO.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"OrdineEsecuzioneController.ExRicercaEventoCampoNotaByIdEvento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCampoNotaSqlDAO);

			cleanup(lConn);
		}

		return lCampoMod;
	}

	public Vector ExAggiornaAvvenutaNotifica(NotificaModel[] IdNotifiche, BigDecimal aFasc,
			boolean isIrreperibilita) throws F3BException {

		Connection lConn = null;

		Vector lVectNot = new Vector();

		NotificaDAO lNotDAO = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		AutoritaEsternaDAO lAutDao = null;
		RinnovoSqlDAO lRinSqlDao = null;

		try {
			lConn = getDBTransaction();
			lNotDAO = new NotificaDAO(lConn);
			lScaDao = new ScadenzarioDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			// BigDecimal lIdEvento = null;

			EventoModel lEveMod = null;

			String lCodUfficioUtente = "";
			String lCodUtente = "";

			// Cerca l'evento legato alla notifica
			if (IdNotifiche != null && IdNotifiche.length > 0) {
				int lLungVec = IdNotifiche.length;

				for (int z = 0; z < lLungVec; z++) {
					if (IdNotifiche[z] != null) {
						lCodUfficioUtente = IdNotifiche[z].getCodUfficioAggiornamento();
						lCodUtente = IdNotifiche[z].getCodiceOperatoreAggiornamento();
						lEveDao.setIdEvento(IdNotifiche[z].getEveIdEvento());
					}
				}

				lEveDao.selByKey();

				lEveDao.start();
				if (lEveDao.next()) {
					lEveMod = new EventoModel();

					lEveMod.setIdEvento(lEveDao.getIdEvento());
					lEveMod.setCodMotivo(lEveDao.getCodMotivo());
					lEveMod.setDataEmissione(lEveDao.getDataEmissione());
				}

				lEveDao.stop();
			}

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.debug("lEveMod : " + lEveMod);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("aFasc : " + aFasc);

			BigDecimal lIdNotificaCondannato = null;

			int lLungVect = IdNotifiche.length;
			for (int i = 0; i < lLungVect; i++) {
				NotificaModel lNot = new NotificaModel();
				lNot = IdNotifiche[i];
				if (lNot != null) {
					if (lNot != null && "E".equals(lNot.getCodTipoNotifica())) {
						lIdNotificaCondannato = lNot.getIdNotifica();
					}

					BigDecimal lKeyAutDeleg = null;
					if (lNot != null && lNot.getAutoritaEsternaDelegata() != null) {
						lAutDao = new AutoritaEsternaDAO(lConn);

						lAutDao.setRicercaByAutSede(lNot.getAutoritaEsternaDelegata());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(lNot.getAutoritaEsternaDelegata());
							lKeyAutDeleg = lAutDao.insert();
							lNot.getAutoritaEsternaDelegata().setIdAutoritaEsterna(lKeyAutDeleg);
							lAutDao.stop();
						} else {
							lKeyAutDeleg = lAutMod.getIdAutoritaEsterna();

							// Il campo descrizione rappresenta l'indirizzo in maschera
							// della 'Autorita' che ha effettuato la notifica'
							lAutDao.setDescrizione(lNot.getAutoritaEsternaDelegata().getDescrizione());

							lAutDao.setCondizioneUpdate(lKeyAutDeleg);
							lAutDao.update();
							lAutDao.stop();

							lNot.getAutoritaEsternaDelegata().setIdAutoritaEsterna(lKeyAutDeleg);
						}
					}

					lNotDAO.setDataAvvenutaNotifica(lNot.getDataAvvenutaNotifica());
					lNotDAO.setCodiceOperatoreAggiornamento(lNot.getCodiceOperatoreAggiornamento());
					lNotDAO.setCodUfficioAggiornamento(lNot.getCodUfficioAggiornamento());
					lNotDAO.setDataAggiornamento(lNot.getDataAggiornamento());
					lNotDAO.setCondizioneUpdate(lNot.getIdNotifica());
					lNotDAO.setEveIdEvento(lNot.getEveIdEvento());
					lNotDAO.setCodEsito(lNot.getCodEsito());
					lNotDAO.setAutEstIdAutoritaEstDeleg(lKeyAutDeleg);
					lNotDAO.update();
					lNotDAO.stop();

					lVectNot.add(lNot);
				}

			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("lVectNot.size() : " + lVectNot.size());

			List lListNotifiche = new ArrayList(lVectNot);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("lListNotifiche.size() : " + lListNotifiche.size());

			// Gestione data scadenzario
			if (isIrreperibilita) // se provengo dalle notifiche del Decreto di Irreperibilita' cerco le
									// notifiche dell'OE Simeone
			{
				lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aFasc, "LS");
				EventoModel lEveSimeone = (EventoModel) lEveSqlDao.getModelByKey();

				List lNotificheSimeone = null;
				if (lEveSimeone != null && lEveSimeone.getIdEvento() != null) {
					lNotEveDao.ricercaNotificaByEvento(lEveSimeone.getIdEvento());
					lNotificheSimeone = new ArrayList(lNotEveDao.getModels());
					lNotEveDao.stop();
				}

				if (lNotificheSimeone != null && !lNotificheSimeone.isEmpty()) {
					for (Iterator lIter = lNotificheSimeone.iterator(); lIter.hasNext();) {
						NotificaModel lElement = (NotificaModel) lIter.next();

						if (lElement != null && "E".equals(lElement.getCodTipoNotifica())) {
							lIdNotificaCondannato = lElement.getIdNotifica();

							lNotificheSimeone.remove(lElement);

							lListNotifiche.addAll(lNotificheSimeone);
							break;
						}
					}
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(
						"lListNotifiche.size() prima dei calcoli irreperibilita': " + lListNotifiche.size());
			} else // se provengo dalle notifiche dell'OE simeone cerco tutte le notifiche (perche' potrei
					// provenire da quelle all'avvocato o da quella al condannato) e le notifiche del Decreto
					// di Irreperibilita'
			{
				lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aFasc, "LS");
				EventoModel lEveSimeone = (EventoModel) lEveSqlDao.getModelByKey();

				List lNotificheSimeone = null;
				if (lEveSimeone != null && lEveSimeone.getIdEvento() != null) {
					lNotEveDao.ricercaNotificaByEvento(lEveSimeone.getIdEvento());
					lNotificheSimeone = new ArrayList(lNotEveDao.getModels());
					lNotEveDao.stop();
				}

				lListNotifiche = new ArrayList(lNotificheSimeone);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("lListNotifiche.size() prima dei calcoli: " + lListNotifiche.size());

				if (lNotificheSimeone != null && !lNotificheSimeone.isEmpty()) {
					for (Iterator lIter = lNotificheSimeone.iterator(); lIter.hasNext();) {
						NotificaModel lElement = (NotificaModel) lIter.next();

						if (lElement != null && "E".equals(lElement.getCodTipoNotifica())) {
							lIdNotificaCondannato = lElement.getIdNotifica();

							break;
						}
					}
				}

				lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aFasc, "8BIS");
				EventoModel lEveIrr = (EventoModel) lEveSqlDao.getModelByKey();

				List lNotificheIrr = null;
				if (lEveIrr != null && lEveIrr.getIdEvento() != null) {
					lNotEveDao.ricercaNotificaByEvento(lEveIrr.getIdEvento());
					lNotificheIrr = new ArrayList(lNotEveDao.getModels());
					lNotEveDao.stop();
				}

				if (lNotificheIrr != null && !lNotificheIrr.isEmpty()) {
					for (Iterator lIter = lListNotifiche.iterator(); lIter.hasNext();) {
						NotificaModel lElement = (NotificaModel) lIter.next();

						if (lElement != null && "E".equals(lElement.getCodTipoNotifica())) {
							lListNotifiche.remove(lElement);

							lListNotifiche.addAll(lNotificheIrr);
							break;
						}
					}
				}
			}

			// Calcola la Data di Avvenuta notifica massima su tutte le notifiche utili
			// Ticket#20191128014 + Ticket#20191106018:
			// Non fornisce lo scadenziario per DS notificato con Decreto Irreperibilita'
			// 28/11/2019: In caso di decreto di Irreperibilità viene eseguito il calcolo con il metodo
			// calcolaDataMaggioreIrreperibilita
			// Date lDataAvvenutaNotifica = calcolaDataMaggiore(
			// (NotificaModel[]) lListNotifiche.toArray(new NotificaModel[0]));
			Date lDataAvvenutaNotifica = null;
			if (isIrreperibilita) {
				lDataAvvenutaNotifica = calcolaDataMaggioreIrreperibilita(
						(NotificaModel[]) lListNotifiche.toArray(new NotificaModel[0]));
			} else {
				lDataAvvenutaNotifica = calcolaDataMaggiore(
						(NotificaModel[]) lListNotifiche.toArray(new NotificaModel[0]));
			}
			// Calcola periodo feriale opzionale sulla data scadenza
			Date lDataFineScadenza = null;
			if (lDataAvvenutaNotifica != null) {
				lDataFineScadenza = calcolaPeriodoFeriale(lConn, lDataAvvenutaNotifica, lCodUfficioUtente);
			}

			// Cerca l'eventuale ultimo Rinnovo legato alla notifica al condannato
			RinnovoModel lRinnovo = null;

			if (lIdNotificaCondannato != null) {
				lRinSqlDao = new RinnovoSqlDAO(lConn);

				lRinSqlDao.ricercaRinnovoIdNotificaCodTipoRinnovo(lIdNotificaCondannato, null);
				lRinnovo = (RinnovoModel) lRinSqlDao.getModelByKey();
			}

			// Controlla l'esistenza dello scadenzario
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFasc, "01");
			ScadenzarioModel lScaMod = (ScadenzarioModel) lScaDao.getModelByKey();
			lScaDao.stop();

			lScaDao.setCodTipoScadenzario("01");
			lScaDao.setDataInizioScadenza(lDataAvvenutaNotifica);
			lScaDao.setDataFineScadenza(lDataFineScadenza);
			lScaDao.setFlagVisto("N");
			lScaDao.setCodOperatoreInserimento(lCodUtente);
			lScaDao.setCodUfficioInserimento(lCodUfficioUtente);
			lScaDao.setDataInserimento(DateUtils.getSysDate());
			lScaDao.setFasSieIdFascicoloSiep(aFasc);
			// lScaDao.setNotIdNotifica(IdNotifiche[i].getIdNotifica());

			// Inserisce/Modifica lo scadenzario
			lScaDao.setCodStatoNotifica(
					calcolaCodStatoNotifica(lListNotifiche, lDataAvvenutaNotifica, lRinnovo));

			if (lScaMod == null && lDataAvvenutaNotifica != null) {
				// lScaDao.insert(); // Paolo Cherubini 25 ottobre 2011 su segnalazione di Pina Marchese
				// se lo scadenzario non esiste non e' corretto crearlo a fronte della notifica poiche' se non
				// esiste e' stato giustamente
				// cancellato da un provvedimento (es. trasmissione istanza)
			} else if (lScaMod != null && lScaMod.getIdScadenzario() != null
					&& lDataAvvenutaNotifica != null) {
				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
				lScaDao.update();
			} else if (lScaMod != null && lScaMod.getIdScadenzario() != null
					&& lDataAvvenutaNotifica == null) {
				lScaDao.setDataInizioScadenza(lEveMod.getDataEmissione());

				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
				lScaDao.update();
			}

			lScaDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"OrdineEsecuzioneController.ExAggiornaAvvenutaNotifica: daoEx --> " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("OrdineEsecuzioneController.ExAggiornaAvvenutaNotifica: ex --> " + ex);
		} finally {
			cleanup(lNotDAO);
			cleanup(lNotEveDao);
			cleanup(lScaDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lAutDao);
			cleanup(lRinSqlDao);

			cleanup(lConn);
		}

		return lVectNot;
	}

	/*
	 * Calcola la data avvenuta notifica maggiore. Se esiste almeno una data a null, non esiste la data
	 * maggiore e ritorna null
	 */
	public Date calcolaDataMaggioreIrreperibilita(NotificaModel[] aNotifiche) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("aNotifiche.length : " + aNotifiche.length);
		Date lDataMaggiore = null;
		for (int i = 0; i < aNotifiche.length; i++) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("i : " + i);
			NotificaModel lNotMod = aNotifiche[i];
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("lNotMod.getIdNotifica() : " + lNotMod.getIdNotifica());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("lNotMod.getCodTipoNotifica() : " + lNotMod.getCodTipoNotifica());
			// i calcoli valgono solo per le notifiche al condannato ("E") e agli avvocati ("N")
			if ("E".equals(lNotMod.getCodTipoNotifica()) || "N".equals(lNotMod.getCodTipoNotifica())) {
				if (i == 0) { // la prima volta lDataMaggiore e' uguale a null
					lDataMaggiore = lNotMod.getDataAvvenutaNotifica();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("lDataMaggiore if i = 0: " + lDataMaggiore);
				} else if (lNotMod.getDataAvvenutaNotifica() != null && lDataMaggiore != null) {
					lDataMaggiore = DateUtils.getMaxDate(lNotMod.getDataAvvenutaNotifica(), lDataMaggiore);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("lDataMaggiore if date != null : " + lDataMaggiore);
				} else {
					lDataMaggiore = null;

					break;
				}
			}

			/*
			 * if( lNotMod.getDataAvvenutaNotifica() == null ) { lDataMaggiore = null;
			 *
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.debug("lDataMaggiore if 1: " + lDataMaggiore);
			 *
			 * break; } else { if( i == 0 ) // la prima volta lDataMaggiore e' uguale a null { lDataMaggiore =
			 * lNotMod.getDataAvvenutaNotifica();
			 *
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.debug("lDataMaggiore if 2: " + lDataMaggiore); } else { lDataMaggiore =
			 * DateUtils.getMaxDate(lNotMod.getDataAvvenutaNotifica(), lDataMaggiore);
			 *
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.debug("lDataMaggiore if 3: " + lDataMaggiore); } }
			 */
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("lDataMaggiore : " + lDataMaggiore);

		return lDataMaggiore;
	}

	/*
	 * Elimina la notifica al condannato (viene richiamato se presente Decreto di Irreperibilita')
	 *
	 * N - attesa notifica condannato A - attesa notifica avvocato S - notificato
	 */
	public String calcolaCodStatoNotifica(List aNotifiche, Date aDataAvvenutaNotifica,
			RinnovoModel aRinnovo) {

		String lCodStatoNotifica = "N";

		if (aDataAvvenutaNotifica != null) {
			lCodStatoNotifica = "S";
		} else {
			for (Iterator lIter = aNotifiche.iterator(); lIter.hasNext();) {
				NotificaModel lElement = (NotificaModel) lIter.next();

				if (lElement != null && "E".equals(lElement.getCodTipoNotifica())) {
					if (lElement.getDataAvvenutaNotifica() != null) {
						lCodStatoNotifica = "A";
					} else {
						// La notifica al condannato non e' avvenuta ( DataAvvenutaNotifica == null)
						if (aRinnovo != null && aRinnovo.getCodTipoRinnovo() != null) {
							if ("R".equals(aRinnovo.getCodTipoRinnovo())
									|| "N".equals(aRinnovo.getCodTipoRinnovo())
									|| "A".equals(aRinnovo.getCodTipoRinnovo())) {
								lCodStatoNotifica = "M";
							} else if ("D".equals(aRinnovo.getCodTipoRinnovo())
									|| "I".equals(aRinnovo.getCodTipoRinnovo())) {
								lCodStatoNotifica = "I";
							} else if ("U".equals(aRinnovo.getCodTipoRinnovo())
									|| "P".equals(aRinnovo.getCodTipoRinnovo())) {
								lCodStatoNotifica = "R";
							} else if ("S".equals(aRinnovo.getCodTipoRinnovo())) {
								lCodStatoNotifica = "O";
							}
						}
					}
					break;
				}
			}
		}

		return lCodStatoNotifica;
	}

	/*
	 * Calcola la data avvenuta notifica maggiore. Se esiste almeno una data a null, non esiste la data
	 * maggiore e ritorna null
	 */
	public Date calcolaDataMaggiore(NotificaModel[] aNotifiche) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		// siesLogger.debug("aNotifiche.length : " + aNotifiche.length);

		// 17/05/2019 MEV70 Rielaborazione della maggiore data di Notifica solo per i tipi "E" ed "N".
		Date lDataMaggiore = null;
		Date lDataMaggioreE = null;
		Date lDataMaggioreN = null;
		for (int i = 0; i < aNotifiche.length; i++) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("i : " + i);
			NotificaModel lNotMod = aNotifiche[i];

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("lNotMod.getIdNotifica() : " + lNotMod.getIdNotifica());
			siesLogger.debug("lNotMod.getCodTipoNotifica() : " + lNotMod.getCodTipoNotifica());

			// i calcoli valgono solo per le notifiche al condannato ("E") e agli avvocati ("N")
			if ("E".equals(lNotMod.getCodTipoNotifica())) {
				if (lDataMaggioreE == null) {
					lDataMaggioreE = lNotMod.getDataAvvenutaNotifica();
					siesLogger.debug("lDataMaggiore if i = 0: " + lDataMaggiore);
				} else if (lNotMod.getDataAvvenutaNotifica() != null && lDataMaggioreE != null) {
					lDataMaggioreE = DateUtils.getMaxDate(lNotMod.getDataAvvenutaNotifica(), lDataMaggioreE);
					siesLogger.debug("lDataMaggiore if date != null : " + lDataMaggioreE);
				}
			}
			if ("N".equals(lNotMod.getCodTipoNotifica())) {
				if (lDataMaggioreN == null) {
					lDataMaggioreN = lNotMod.getDataAvvenutaNotifica();
					siesLogger.debug("lDataMaggiore if i = 0: " + lDataMaggioreN);
				} else if (lNotMod.getDataAvvenutaNotifica() != null && lDataMaggioreN != null) {
					lDataMaggioreN = DateUtils.getMaxDate(lNotMod.getDataAvvenutaNotifica(), lDataMaggioreN);
					siesLogger.debug("lDataMaggiore if date != null : " + lDataMaggioreN);
				}
			}
		}

		// 17/05/2017 Devono essere presenti entrambi le notifiche (E=Avvocato; N=Condannato)
		if (lDataMaggioreE != null && lDataMaggioreN != null) {
			lDataMaggiore = DateUtils.getMaxDate(lDataMaggioreE, lDataMaggioreN);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("lDataMaggiore : " + lDataMaggiore);

		return lDataMaggiore;
	}

	/*
	 * Calcola sulla Data avvenuta notifica maggiore il periodo feriale opzionale
	 */
	public Date calcolaPeriodoFeriale(Connection aConn, Date aDataMaggiore, String aCodUfficioUtente)
			throws F3BException {

		ParametroSqlDAO lParSqlDao = null;

		ParametroModel lParMod = null;

		Date lDataFineScadenza = aDataMaggiore;

		if (lDataFineScadenza == null)
			return null;

		try {
			lParSqlDao = new ParametroSqlDAO(aConn);
			lParSqlDao.ricercaParametroUfficioConnesso("PERIODO FERIALE", aCodUfficioUtente);
			lParMod = (ParametroModel) lParSqlDao.getModelByKey();

			// Nel caso non esista il record viene considerato come periodo di ferie
			// dal 1 Agosto al 15 Settembre
			if (lParMod == null) {
				GregorianCalendar lDataFine = new GregorianCalendar();
				lDataFine.setTime(lDataFineScadenza);

				GregorianCalendar lDataAgosto = new GregorianCalendar(lDataFine.get(Calendar.YEAR),
						Calendar.AUGUST, 1);
				GregorianCalendar lDataSettembre = new GregorianCalendar(lDataFine.get(Calendar.YEAR),
						Calendar.SEPTEMBER, 15);

				lParMod = new ParametroModel();

				lParMod.setDataInizioValidita(lDataAgosto.getTime());
				lParMod.setDataFineValidita(lDataSettembre.getTime());
				lParMod.setGiorni(new BigDecimal(45));
			}

			// Aggiunge i 30 giorni previsti dalla scadenzario
			lDataFineScadenza = DateUtils.moveDateTo(lDataFineScadenza, Calendar.DAY_OF_MONTH, 30);

			GregorianCalendar lDataFine = new GregorianCalendar();
			lDataFine.setTime(lDataFineScadenza);

			// La data inizio periodo di ferie e' quella impostata da funzioni amministrative
			// L'anno invece e' quello relativo alla data avvenuta notifica maggiore
			GregorianCalendar lDataInizioFerie = new GregorianCalendar();
			lDataInizioFerie.setTime(lParMod.getDataInizioValidita());
			lDataInizioFerie.set(Calendar.YEAR, lDataFine.get(Calendar.YEAR));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("lDataInizioFerie : " + lDataInizioFerie);

			// La data fine periodo di ferie e' quella impostata da funzioni amministrative
			// L'anno invece e' quello relativo alla data avvenuta notifica maggiore
			GregorianCalendar lDataFineFerie = new GregorianCalendar();
			lDataFineFerie.setTime(lParMod.getDataFineValidita());
			lDataFineFerie.set(Calendar.YEAR, lDataFine.get(Calendar.YEAR));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("lDataFineFerie : " + lDataFineFerie);

			if (!aDataMaggiore.before(lDataInizioFerie.getTime())
					&& !aDataMaggiore.after(lDataFineFerie.getTime())) {
				lDataFineScadenza = DateUtils.moveDateTo(lDataFineFerie.getTime(), Calendar.DAY_OF_MONTH, 30); // i
																												// 30
																												// giorni
																												// dello
																												// scadenzario
																												// simeone
			} else if (!lDataFine.before(lDataInizioFerie) && !lDataFine.after(lDataFineFerie)) {
				lDataFineScadenza = DateUtils.moveDateTo(lDataFineScadenza, Calendar.DAY_OF_MONTH,
						lParMod.getGiorni().intValue());
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);

			throw new F3BException("OrdineEsecuzioneController.calcolaPeriodoFeriale: " + daoEx);
		} finally {
			cleanup(lParSqlDao);
		}

		return lDataFineScadenza;
	}

	/**
	 * ExAggiornaNotifichePosizioneGiuridicaLuogoDetenzioneVerbaleArresto
	 *
	 * @param IdNotifiche
	 * @param aPosMod
	 * @param aLuoDetMod
	 * @return
	 * @throws F3BException
	 */
	public Vector ExAggiornaNotifichePosizioneGiuridicaLuogoDetenzioneVerbaleArresto(
			NotificaModel[] IdNotifiche, PosizioneGiuridicaModel aPosMod, LuogoDetenzioneModel aLuoDetMod)
			throws F3BException {

		Connection lConn = null;
		Vector lVectNot = new Vector();

		NotificaDAO lNotDAO = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;

		try {
			lConn = getDBTransaction();

			lNotDAO = new NotificaDAO(lConn);

			int lLungVect = IdNotifiche.length;

			for (int i = 0; i < lLungVect; i++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Elemento :" + i);
				// lNotDAO.setEveIdEvento(IdNotifiche[i].getEveIdEvento());
				lNotDAO.setDataAvvenutaNotifica(IdNotifiche[i].getDataAvvenutaNotifica());
				lNotDAO.setCodEsito(IdNotifiche[i].getCodEsito());

				lNotDAO.setCodiceOperatoreAggiornamento(IdNotifiche[i].getCodiceOperatoreAggiornamento());
				lNotDAO.setCodUfficioAggiornamento(IdNotifiche[i].getCodUfficioAggiornamento());
				lNotDAO.setDataAggiornamento(IdNotifiche[i].getDataAggiornamento());

				lNotDAO.setCondizioneUpdate(IdNotifiche[i].getIdNotifica());
				lNotDAO.update();
				lVectNot.add(IdNotifiche[i]);
				lNotDAO.stop();
			}

			/******************************** Posizione Giuridica *************************************/
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
				lPosDao.setCodOperatoreAggiornamento(lPosMod.getCodOperatoreInserimento());
				lPosDao.setCodUfficioAggiornamento(lPosMod.getCodUfficioInserimento());
				lPosDao.setDataAggiornamento(lPosMod.getDataInserimento());
				lPosDao.setDataFine(lPosMod.getDataInizio());

				lPosDao.selByKey();
				lPosDao.update();
				lPosDao.stop();
			}
			// insert

			lPosDao.setDAOFromModel(aPosMod);
			// BigDecimal lKeyPosGiu = null;
			/* lKeyPosGiu = */lPosDao.insert();
			lPosDao.stop();

			/*************************** Fine Posizione Giuridica **********************************/

			/****************************** Luogo Detenzione ***************************************/
			// insert
			lLuoDetDao = new LuogoDetenzioneDAO(lConn);

			lLuoDetDao.setDAOFromModel(aLuoDetMod);
			// BigDecimal lKeyLuoDet = null;
			/* lKeyLuoDet = */lLuoDetDao.insert();
			lLuoDetDao.stop();
			/**************************** Fine Luogo Detenzione ***********************************/
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			throw new F3BException(
					"OrdineEsecuzioneController.ExAggiornaAvvenutaNotifica: daoEx--> " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new F3BException("OrdineEsecuzioneController.ExAggiornaAvvenutaNotifica: ex--> " + ex);
		} finally {
			cleanup(lNotDAO);
			cleanup(lPosSqlDao);
			cleanup(lPosDao);
			cleanup(lLuoDetDao);

			cleanup(lConn);
		}

		return lVectNot;
	}

	/**
	 * Aggiorna Evento Stato Esecuzione
	 *
	 * @param aEventi
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExAggiornaEventoStatoEsecuzione(List aEventi) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		EventoModel lEveMod = null;

		try {
			lConn = getDBTransaction();

			// --GDV---
			// RIflettendoci sopra sulla stessa transazione devono essere
			// aperti piu' DAO nel ciclo for non fuori...
			// lEveDao = new EventoDAO(lConn);

			for (int i = 0; i < aEventi.size(); i++) {
				lEveDao = new EventoDAO(lConn);
				lEveMod = new EventoModel((EventoModel) aEventi.get(i));
				lEveDao.setIdEvento(lEveMod.getIdEvento());
				// lEveDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				// lEveDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
				// lEveDao.setDataAggiornamento(lEveMod.getDataAggiornamento());

				lEveDao.setFlagStampaSiep(lEveMod.getFlagStampaSiep());
				lEveDao.setFlagVideoSiep(lEveMod.getFlagVideoSiep());

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"OrdineEsecuzioneController.ExAggiornaEventoStatoEsecuzione: Non posso inserire: " + ex);
		} catch (Exception exc) {
			exc.printStackTrace();
			rollback(lConn);
			throw new F3BException(
					"OrdineEsecuzioneController.ExAggiornaEventoStatoEsecuzione: ex--> " + exc);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEveMod;
	}

	/**
	 * Effettua l'operazione di update e validazione di un documento mandato tramite upload
	 *
	 * @param aEvento
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaOE(EventoModel aEvento, FascicoloSiepModel aFascicolo)
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
		ScadenzarioSqlDAO lScadeDao = null;
		NomeProvvedimentoDAO lNomProvDao = null;
		EventoSqlDAO lEveSql = null;
		LuogoDetenzioneDAO lLuogoDAO = null;
		LuogoDetenzioneSqlDAO lLuogoSql = null;
		LicenzaLibanticipataSqlDAO lLicSqlDao = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaDAO lMisDAO = null;
		FascicoloSiepDAO lFasDao = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		BigDecimal lKeyPos = null;

		try {
			lConn = getDBTransaction();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			// 20170831: [SG] nuova gestione se non esiste la posizione giuridica
			String lCodPosizione = "0";
			if (lPosMod != null) {
				lKeyPos = lPosMod.getIdPosizioneGiuridica();
				lCodPosizione = lPosMod.getCodPosizioneGiuridica();
			}

			// =========================================
			// aggiorno la misura alternativa con flag_situazione ad N solo se si tratta
			// della datenzione domiciliare a termine-->concessione;proroga;proroga provvisoria
			// =========================================
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDAO = new MisuraAlternativaDAO(lConn);
			if (lCodPosizione.equals("12")) {
				// Paolo Cherubini 22/11/2010
				// sostituisco query perche' la MA va considerata sul solo quando e' legata ad un evento non
				// annullato
				lMisDao.ricercaMisuraAlternativaByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				// lMisDao.ricercaMisuraAlternativaByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

				if (lMisMod != null && ("0011".equals(lMisMod.getCodTipoMisura())
						|| "0197".equals(lMisMod.getCodTipoMisura())
						|| "2340".equals(lMisMod.getCodTipoMisura()))) {
					lMisDAO.setDAOFromModelForUpdate(lMisMod);
					lMisDAO.setFlagSituazione("N");
					lMisDAO.update();
					lMisDAO.stop();
				}
			}

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
			}
			// lEveDao.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			Vector lNotifiche = new Vector(lNotEveDao.getModels());

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			// lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveApp.getDataEmissione());

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			// ** POSIZIONE_GIURIDICA **
			boolean lCodStatoAggiornato = false;
			// TODO MEV 10_S3 verificare
			// if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) //
			// Altra causa
			// {
			// lStatoProcMod.setCodStatoProcedimento("0007");
			// lCodStatoAggiornato = true;
			// }

			int lIntPos = Integer.parseInt(lCodPosizione);

			AltraCausaModel lAcModel = new AltraCausaModel();
			// 20170831: [SG] aggiunto controllo preventivo
			if (lPosMod != null && lPosMod.getAltCauIdAltraCausa() != null || lIntPos == 07) {
				IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
				lAcModel = lAC.ExRicercaAltraCausaByFascicolo(aFascicolo.getIdFascicoloSiep());
				if (lAcModel != null) {
					lIntPos = Integer.valueOf(lAcModel.getCodTipoPosGiuridica());
				}
			}

			switch (lIntPos) {
			case 1:
			case 3: // Detenuto per questa causa
			case 73: {
				lCodPosizione = "03";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0007");
				// lStatoProcMod.setCodStatoProcedimento("0005"); modifica 14/10/03
				break;
			}
			case 2:
			case 4: // Arresti domiciliari
			case 70:
			case 71:
			case 72: {
				// lCodPosizione = "04"; 14/10/03 modifica
				lCodPosizione = "03";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0009");
				break;
			}
			case 12: // Detenzione Domiciliare
			{
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0009");
				break;
			}
			case 7:
			case 10: // Libero
			case 16:
			case 17:
			case 30:
			case 26:
			case 20:
			case 46:
			case 47: {
				lCodPosizione = "10";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0001");
				break;
			}
			case 74:
			case 75:
			case 76:
			case 77:
			case 78:
			case 79:
			case 80:
			case 81: {
				lCodPosizione = "07";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0007");
				break;
			}
			case 64: {
				lCodPosizione = "03";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0546");
				break;
			}
			default: // Altri casi
				lStatoProcMod.setCodStatoProcedimento("0057");
			}

			// ** Aggiorna SCADENZARIO se la POSIZIONE_GIURIDICA e' LIBERO **
			if ((lIntPos == 7 || lIntPos == 10 || lIntPos == 16 || lIntPos == 17 || lIntPos == 20
					|| lIntPos == 26 || lIntPos == 30 || lIntPos == 46 || lIntPos == 47)
					&& (aFascicolo.getFlagAltraCausa() != null
							&& aFascicolo.getFlagAltraCausa().equals("N"))) {
				ParametroModel lParMod = new ParametroModel();

				lParMod.setNomeParametro("VANE RICERCHE");
				lParMod.setCodUfficioValidita(aEvento.getCodUfficioAggiornamento());

				Vector lVectPar = null;
				IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
				lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

				ScadenzarioModel lScaMod = new ScadenzarioModel();

				lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				// MEV_10S3 A.S.
				if (lNotifiche != null && lNotifiche.size() > 0) {
					NotificaModel lNotifica = (NotificaModel) lNotifiche.get(0);
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
					lScadeDao = new ScadenzarioSqlDAO(lConn);
					// cerca un scadenzario per id fascicolo e per tipo scadenzario
					lScadeDao.ricercaScadenzarioVerbaleArresto(lScaMod);
					lScadenzarii = new Vector(lScadeDao.getModels());

					lScaDao = new ScadenzarioDAO(lConn);

					if (lScadenzarii.size() == 0) {
						lScaMod.setFlagVisto("N");
						lScaMod.setDataFineScadenza(lFineScadenza);
						lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
						lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
						lScaMod.setDataInserimento(aEvento.getDataAggiornamento());
						// a6-rr-238 // AMBROSINO se creo lo scadenzario lo lego all'evento che l'ha creato
						lScaMod.setEveIdEvento(aEvento.getIdEvento());
						lScaMod.setCodStatoNotifica("NP");
						lScaDao.setDAOFromModel(lScaMod);
						// BigDecimal lKeyScad = null;
						/* lKeyScad = */lScaDao.insert();
					} else {
						ScadenzarioModel lScaModID = (ScadenzarioModel) lScadenzarii.get(0);
						lScaMod.setIdScadenzario(lScaModID.getIdScadenzario());
						lScaMod.setDataFineScadenza(lFineScadenza);
						lScaMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lScaMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
						lScaMod.setDataAggiornamento(aEvento.getDataAggiornamento());
						// a6-rr-238
						lScaMod.setCodStatoNotifica("NP");
						// AMBROSINO 30/06/2011
						lScaMod.setEveIdEvento(aEvento.getIdEvento());

						lScaDao.setDAOFromModelForUpdate(lScaMod);
						lScaDao.update();
					}
				}
			}

			// 20170831: [SG] nuova gestione della pena residua (aggiunta and condition
			// per controllare consistenza pena residua)
			lPenResDao = new PenaResiduaDAO(lConn);
			// Controllo se IdEvento di PenaResidua e' uguale a null, se e' uguale a null Aggiorno PenaResidua
			// altrimenti Inserisco PenaResidua(sempre con l'evento corrente)
			IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			PenaResiduaModel lPenResMod = lCtrl
					.ExRicercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			if (lPenResMod == null)
				lPenResMod = new PenaResiduaModel();
			if (lPenResMod.getEveIdEvento() == null && lPenResMod.getIdPenaResidua() != null) {
				// Aggiorna PENA_RESIDUA
				lPenResMod.setFlagValidato(aEvento.getFlagDocumentoRegistrato());
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
				lPenResDao.setDataFine(lPenResMod.getDataFine());
				lPenResDao.setEveIdEvento(aEvento.getIdEvento());
				lPenResDao.setFlagValidato(aEvento.getFlagDocumentoRegistrato());
				// lo trovo ad "S" se prima e' stata fatta un'avvenuta espulsione
				if ("S".equals(lPenResMod.getFlagPenaSospesa()))
					lPenResDao.setFlagPenaSospesa("N");
				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				// INSERISCE PENA_RESIDUA
				lPenResMod.setFlagValidato(aEvento.getFlagDocumentoRegistrato());
				lPenResMod.setEveIdEvento(aEvento.getIdEvento());
				lPenResMod.setDataInserimento(DateUtils.getSysDate());
				// lo trovo ad "S" se prima e' stata fatta un'avvenuta espulsione
				if ("S".equals(lPenResMod.getFlagPenaSospesa()))
					lPenResMod.setFlagPenaSospesa("N");
				PenaResiduaDAO lPenDao = new PenaResiduaDAO(lConn);
				lPenDao.setDAOFromModel(lPenResMod);
				lPenDao.insert();
			}

			// 20170908: [SG] x le richieste istruttorie di cumulo (tipo evento 05)
			// non bisogna cambiare lo stato del procedimento
			List<String> l = Arrays.asList("0044", "0045", "0048", "0049", "0050", "0051", "0052", "0053",
					"0054", "0557", "0565", "0566", "0578", "0579", "0580", "1050");
			if (("05".equals(lEveDao.getCodTipoEvento()) || ("02".equals(lEveDao.getCodTipoEvento())
					&& "26".equals(lEveDao.getCodTipoProvvedimento())))
					&& l.contains(lEveDao.getCodMotivo())) {
				siesLogger.info("PER QUESTO CODICE MOTIVO: " + aEvento.getCodMotivo()
						+ " NON AGGIORNO LO STATO DEL PROCEDIMENTO PER UN EVENTO DI RICHIESTA ISTRUTTORIA [TIPO EVENTO = 05]");
			} else {
				// ** STATO_PROCEDIMENTO **
				lStatoDao = new StatoProcedimentoDAO(lConn);

				// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
				lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				lStatoDao.delete();
				// Inserisci STATO Del PROCEDIMENTO
				for (int ins = 1; ins < 3; ins++) {
					lStatoProcMod.setProgressivo(new BigDecimal(ins));
					lStatoDao.setDAOFromModel(lStatoProcMod);
					lStatoDao.insert();
					lStatoDao.stop();
					if ((lIntPos == 1 || lIntPos == 3
							|| (aFascicolo.getFlagAltraCausa() != null
									&& aFascicolo.getFlagAltraCausa().equals("S")))
							&& lPenResMod.getDataInizio() != null) {
						// Aggiungo allo stato procedimento 0008 - con Inizio Esecuzione Dal
						lStatoProcMod.setCodStatoProcedimento("0008");
						lStatoProcMod.setData(lPenResMod.getDataInizio());
					} else if (lIntPos == 2 || lIntPos == 4 || lIntPos == 12 || lIntPos == 70 || lIntPos == 71
							|| lIntPos == 72) { // 0010 - Pena in Esecuzione Fino al
						lStatoProcMod.setCodStatoProcedimento("0010");
						lStatoProcMod.setData(lPenResMod.getDataFine());
					} else { // esco
						ins = 3;
					}
				}
			}

			/*************************************************
			 * FINE
			 **************************************************************/
			/************************************************
			 * NOME PROVVEDIMENTO
			 ************************************************/
			NomeProvvedimentoModel lNomProvMod = new NomeProvvedimentoModel();
			lNomProvDao = new NomeProvvedimentoDAO(lConn);

			if (lIntPos == 7 || lIntPos == 10 || lIntPos == 16 || lIntPos == 17 || lIntPos == 20
					|| lIntPos == 26 || lIntPos == 30 || lIntPos == 46 || lIntPos == 47) {
				lNomProvMod.setCodNomeProvvedimento("NP001");
			} else if (lIntPos == 1 || lIntPos == 3) {
				lNomProvMod.setCodNomeProvvedimento("NP002");
			} else if (lIntPos == 2 || lIntPos == 4) {
				lNomProvMod.setCodNomeProvvedimento("NP006");
			} else if (lIntPos == 12) {
				lNomProvMod.setCodNomeProvvedimento("NP210");
			} else if ((lIntPos == 7 || lIntPos == 10 || lIntPos == 16 || lIntPos == 17 || lIntPos == 20
					|| lIntPos == 26 || lIntPos == 30 || lIntPos == 46 || lIntPos == 47)
					&& (aFascicolo.getFlagAltraCausa() != null
							&& aFascicolo.getFlagAltraCausa().equals("S"))) {
				if (lStatoProcMod.getData() == null) {
					lNomProvMod.setCodNomeProvvedimento("NP003");
				} else {
					lNomProvMod.setCodNomeProvvedimento("NP004");
				}
			} else {
				lNomProvMod.setCodNomeProvvedimento("NP046");
			}

			lNomProvMod.setEveIdEvento(aEvento.getIdEvento());
			lNomProvDao.setDAOFromModel(lNomProvMod);
			lNomProvDao.insert();

			/*********************************************
			 * FINE NOME PROVVEDIMENTO
			 **********************************************/
			/********************************************
			 * POSIZIONE GIURIDICA
			 **************************************************/
			// 20170831: [SG] aggiunto controllo preventivo
			if (lIntPos != 0 && lIntPos != 16 && lIntPos != 20 && lIntPos != 46 && lIntPos != 47
					&& lIntPos != 12) {
				// Aggiorna POSIZIONE_GIURIDICA
				lPosDao = new PosizioneGiuridicaDAO(lConn);

				lPosDao.setDataFine(lEveApp.getDataEmissione());
				lPosDao.setDataAggiornamento(DateUtils.getSysDate());
				lPosDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());

				lPosDao.update();
				lPosDao.stop();

				// Inserisco la nuova posizione giuridica
				// e la aggancio all'evento validato
				lPosDao.setCodPosizioneGiuridica(lCodPosizione);
				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(DateUtils.getSysDate());
				lPosDao.setDataInizio(lEveApp.getDataEmissione());
				lPosDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPosDao.setIdEventoRiferimento(aEvento.getIdEvento()); // **

				int intPos = 0;
				if (lCodPosizione != null) {
					intPos = Integer.parseInt(lCodPosizione);
					switch (intPos) {
					case 0:
					case 5:
					case 6:
					case 8:
					case 10:
					case 16:
					case 17:
					case 20:
					case 26:
					case 27:
					case 46:
					case 47:
					case 48: {
						lPosDao.setCodMaschera("L");
						break;
					}
					case 2:
					case 4:
					case 11:
					case 12:
					case 13:
					case 15:
					case 18:
					case 21:
					case 23:
					case 25:
					case 29:
					case 28:
					case 41:
					case 42:
					case 43:
					case 44:
					case 45:
					case 50:
					case 51:
					case 52:
					case 53:
					case 54:
					case 67:
					case 68:
					case 69:
					case 70:
					case 71:
					case 72:
					case 82:
					case 83:
					case 84:
					case 85:
					case 86:
					case 87: {
						lPosDao.setCodMaschera("EA");
						break;
					}
					case 1:
					case 3:
					case 9:
					case 14:
					case 19:
					case 22:
					case 24:
					case 30:
					case 31:
					case 32:
					case 33:
					case 34:
					case 35:
					case 36:
					case 37:
					case 38:
					case 39:
					case 40:
					case 49:
					case 55:
					case 62:
					case 63:
					case 64:
					case 65:
					case 73: {
						lPosDao.setCodMaschera("EI");
						break;
					}
					case 7: {
						if (lIntPos == 74 || lIntPos == 75) {
							lPosDao.setCodMaschera("L1");
							break;
						} else if (lIntPos == 76 || lIntPos == 77) {
							lPosDao.setCodMaschera("L2");
							break;
						} else if (lIntPos == 78 || lIntPos == 79 || lIntPos == 80 || lIntPos == 81) {
							lPosDao.setCodMaschera("L3");
							break;
						}
					}
					}
				}

				// ricerca della misura cautelare legata alla posizione giuriduca modificata per essere
				// inserita
				// insert la nuova misura cautelare legata alla nuova posizione giuridica
				// (la nuova misura cautelare e' uguale alla misura cautelare legata alla precedente posizione
				// giuridica modificata)
				// decisione presa con S.S./V.B. Settembre 2015
				MisuraCautelareModel misuraCautelare = new MisuraCautelareModel();
				misuraCautelare.setPosGiuIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
				MisuraCautelareSqlDAO lMisCauDao = null;
				lMisCauDao = new MisuraCautelareSqlDAO(lConn);
				lMisCauDao.ricercaMisuraCautelare(misuraCautelare);
				misuraCautelare = (MisuraCautelareModel) lMisCauDao.getModelByKey();

				// AltraCausaSqlDAO lAltCauDao = null;
				AltraCausaModel lAltraCausa = new AltraCausaModel();

				// Altra causa
				if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) {
					// inserisco la nuova ALTRA_CAUSA uguale alla precedente, serve per mantenere lo storico
					// di diverse posizioni giuridiche
					// anche se hanno uguali valori(arresti domiciliari che si riferiscono a due ordinanze
					// diverse ==> cambia solo il periodo)
					// lAltCauDao = new AltraCausaSqlDAO(lConn);
					if (lPosMod != null && lPosMod.getAltCauIdAltraCausa() != null) {
						IAltraCausa altraCausa = SIEPLookupRemote.getAltraCausa();
						lAltraCausa = altraCausa
								.ExRicercaAltraCausaIstitutoByKey(lPosMod.getAltCauIdAltraCausa());
					}
					AltraCausaDAO lAltraCausaDao = null;
					lAltraCausaDao = new AltraCausaDAO(lConn);
					BigDecimal lKeyAltra = null;
					lAltraCausa.setDataInserimento(DateUtils.getSysDate());
					lAltraCausaDao.setDAOFromModel(lAltraCausa);
					lKeyAltra = lAltraCausaDao.insert();
					lPosDao.setAltCauIdAltraCausa(lKeyAltra);
				}

				lKeyPos = lPosDao.insert();

				// insert la nuova misura cautelare legata alla nuova posizione giuridica
				// (la nuova misura cautelare e' uguale alla misura cautelare legata alla precedente posizione
				// giuridica modificata)
				// decisione presa con Salvatore/Vito Settembre 2015
				if (misuraCautelare != null) {
					misuraCautelare.setPosGiuIdPosizioneGiuridica(lKeyPos);
					Vector lVectMod = new Vector();
					lVectMod.add(misuraCautelare);
					IMisuraCautelare lCtrlMisCau = SIEPLookupRemote.getMisuraCautelareRemote();
					lCtrlMisCau.ExInserisciMisuraCautelare(lVectMod);
				}

				lPosDao.stop();

				// Modifica Fascicolo Associato alla Posizione Giuridica
				lFasDao = new FascicoloSiepDAO(lConn);
				if (intPos == 3) {
					lFasDao.setFlagAltraCausa(null);
					lFasDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());
					lFasDao.update();
				}

			}
			/********************************************
			 * FINE POSIZIONE GIURIDICA
			 ***********************************************/

			// SCADENZARIO FINE PENA (02)
			lScaDao = new ScadenzarioDAO(lConn);
			ScadenzarioSqlDAO lScaSqlDao = new ScadenzarioSqlDAO(lConn);

			if (lPenResMod != null && lPenResMod.getFlagValidato() != null
					&& "S".equals(lPenResMod.getFlagValidato()) && lPenResMod.getDataFine() != null) {
				ScadenzarioModel lScaMod = new ScadenzarioModel();
				// Che vo di sta condizione??? e' sempre vera! non si possono mettere in
				// or condizioni di diseguaglianza!!!!!!!!!!!!!!!!!!!
				if (!lCodPosizione.equals("12") || !lCodPosizione.equals("07") || !lCodPosizione.equals("10")
						|| !lCodPosizione.equals("16") || !lCodPosizione.equals("17")
						|| !lCodPosizione.equals("30") || !lCodPosizione.equals("20")
						|| !lCodPosizione.equals("26") || !lCodPosizione.equals("46")
						|| !lCodPosizione.equals("47")) {
					lScaSqlDao.ricercaScadenzarioByIdFascicolo(aFascicolo.getIdFascicoloSiep());
					lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

					if (lScaMod != null) {
						// aggiorna scadenzario
						lScaDao.setDataFineScadenza(lPenResMod.getDataFine());
						lScaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
						lScaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lScaDao.setDataAggiornamento(DateUtils.getSysDate());

						lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());

						lScaDao.update();
						lScaDao.stop();
					} else {
						if (lPenResMod.getDataInizio() != null) {
							lScaMod = new ScadenzarioModel();

							// inserisce scadenzario
							lScaMod.setCodTipoScadenzario("02"); // Fine Pena
							lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio());
							lScaMod.setDataFineScadenza(lPenResMod.getDataFine());
							lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
							lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
							lScaMod.setDataInserimento(DateUtils.getSysDate());
							lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

							lScaDao.setDAOFromModel(lScaMod);
							lScaDao.insert();
							lScaDao.stop();
						}
					}
				}
			}

			// CANCELLA SCADENZARIO DETENZIONE A TERMINE
			if (lCodPosizione.equals("12")) {
				String[] lTipo = { "14" };
				lScaSqlDao.ricercaScadenzarioPerTipoScadenzario(lTipo, aFascicolo.getIdFascicoloSiep());
				ScadenzarioModel lScaModDet = (ScadenzarioModel) lScaSqlDao.getModelByKey();
				if (lScaModDet != null) {
					lScaDao.setCondizioneDelete(lScaModDet.getIdScadenzario());
					lScaDao.delete();
					lScaDao.stop();
				}
			}

			// aggiorna il luogo detenzione LUOGO DETENZIONE MODIFICA DEL 25/05/2004
			lLuogoDAO = new LuogoDetenzioneDAO(lConn);
			lLuogoSql = new LuogoDetenzioneSqlDAO(lConn);

			LuogoDetenzioneModel lLuoDet = null;

			if (lPosMod != null && lPosMod.getIdPosizioneGiuridica() != null) {
				lLuogoSql.ricercaLuogoDetenzioneByIdPosizione(lPosMod.getIdPosizioneGiuridica());
				lLuoDet = (LuogoDetenzioneModel) lLuogoSql.getModelByKey();

				if (lLuoDet != null && lLuoDet.getIdLuogoDetenzione() != null
						&& lLuoDet.getIstDetIdIstitutoDetenzione() != null && lKeyPos != null) {
					lLuogoDAO.setCondizioneUpdate(lLuoDet.getIdLuogoDetenzione());
					lLuogoDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lLuogoDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lLuogoDAO.setDataAggiornamento(DateUtils.getSysDate());
					lLuogoDAO.setDataFineDetenzione(DateUtils.getSysDate());
					lLuogoDAO.update();
					lLuogoDAO.stop();

					lLuogoDAO.setDataInserimento(DateUtils.getSysDate());
					lLuogoDAO.setDataInizioDetenzione(DateUtils.getSysDate());
					lLuogoDAO.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lLuogoDAO.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lLuogoDAO.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					lLuogoDAO.setIstDetIdIstitutoDetenzione(lLuoDet.getIstDetIdIstitutoDetenzione());
					lLuogoDAO.setPosGiuIdPosizioneGiuridica(lKeyPos);
					lLuogoDAO.insert();
					lLuogoDAO.stop();
				}
			}

			// Aggiorna i giorni di Lib Anticipata computati da E ad S
			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicSqlDao.updateFlagElaboratoByIdFascicolo(aFascicolo.getIdFascicoloSiep(), "E", "S");

			// ======================================================================
			// Aggiorno la riga della tabella misure_cautelare_bdmc
			// legata all'annotazione in esame
			// ======================================================================

			MisuraCautelareBdmcModel lMisCautBdmc = new MisuraCautelareBdmcModel();
			lMisCautBdmc.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lMisCautBdmc.setFlagStato("I");
			IMisuraCautelareBdmc lCtrMisCauBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
			Vector misCautBdmc = lCtrMisCauBdmc.ExRicercaMisuraCautelareBdmc(lMisCautBdmc);

			if (misCautBdmc != null && misCautBdmc.size() != 0) {
				for (int ii = 0; ii < misCautBdmc.size(); ii++) {
					MisuraCautelareBdmcModel lMisBdmc = (MisuraCautelareBdmcModel) misCautBdmc.get(ii);
					lMisBdmc.setEveIdEvento(aEvento.getIdEvento());
					lMisBdmc.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lMisBdmc.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lMisBdmc.setDataAggiornamento(aEvento.getDataAggiornamento());
					lMisBdmc.setFlagStato("V");
					lMisBdmc.setStatoTrasmissioneVal("N");
					IMisuraCautelareBdmc lCtrMisBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
					lCtrMisBdmc.ExModificaMisuraCautelareBdmcNoCommit(lConn, lMisBdmc, aEvento);
				}
			}
			// ======================================================================
			// ==
			//
			// Aggiorno la riga nella tabella notifiche_sies collagata all'OE
			// in presenza dei seguenti ordini di esecuzione :
			// 1) Ordine di esecuzione per detenuto in misura cautelare per altra causa:
			// 2) Ordine di esecuzione per detenuto in misura cautelare non custodiale:
			// e dell'associazione con fascicolo bdmc
			//
			// ==
			// ======================================================================

			NotificheSiesModel lNotMod = new NotificheSiesModel();
			INotificheSies lCtrl2 = BDMCLookupRemote.getNotificheSiesRemote();
			// lNotMod.setTipoNotifica("E");
			lNotMod.setIdEvento(aEvento.getIdEvento());
			Vector notificheSies = lCtrl2.ExRicercaNotificheSies(lNotMod);
			if (notificheSies != null && notificheSies.size() != 0) {
				NotificheSiesModel lAggNotMod = (NotificheSiesModel) notificheSies.get(0);
				// if (lAggNotMod.getTipoNotifica().compareTo("E") ==0)
				lAggNotMod.setTipoNotifica("V");
				// else
				// lAggNotMod.setTipoNotifica("A");
				lAggNotMod.setStatoTrasmissione("N");
				lCtrl2.ExModificaNotificheSiesNoCommit(lConn, lAggNotMod);

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

			throw new F3BException("OrdineEsecuzioneController.ExUpdateValidaOE : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			rollback(lConnBlob);

			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("OrdineEsecuzioneController.ExUpdateValidaOE", ex);

			throw new F3BException("OrdineEsecuzioneController.ExUpdateValidaOE : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lScaDao);
			cleanup(lScadeDao); // sca
			cleanup(lEveSql);
			cleanup(lNomProvDao);
			cleanup(lLuogoDAO);
			cleanup(lLuogoSql);
			cleanup(lLicSqlDao);
			cleanup(lMisDao);
			cleanup(lMisDAO);
			cleanup(lEveDaoBlob);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFasDao);

			cleanup(lConn);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	public EventoModel ExUpdateValidaOESanSos(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScadeDao = null;
		NomeProvvedimentoDAO lNomProvDao = null;
		EventoSqlDAO lEveSql = null;
		LuogoDetenzioneDAO lLuogoDAO = null;
		LuogoDetenzioneSqlDAO lLuogoSql = null;
		LicenzaLibanticipataSqlDAO lLicSqlDao = null;
		MisuraAlternativaSqlDAO lMisDao = null;
		MisuraAlternativaDAO lMisDAO = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		BigDecimal lKeyPos = null;

		try {
			lConn = getDBTransaction();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			lKeyPos = lPosMod.getIdPosizioneGiuridica();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// =========================================
			// aggiorno la misura alternativa con flag_situazione ad N solo se si tratta
			// della datenzione domiciliare a termine-->concessione;proroga;proroga provvisoria
			// =========================================
			lMisDao = new MisuraAlternativaSqlDAO(lConn);
			lMisDAO = new MisuraAlternativaDAO(lConn);
			if (lCodPosizione.equals("12")) {
				// Paolo Cherubini 22/11/2010
				// sostituisco query perche' la MA va considerata sul solo quando e' legata ad un evento non
				// annullato
				lMisDao.ricercaMisuraAlternativaByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				// lMisDao.ricercaMisuraAlternativaByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisDao.getModelByKey();

				if (lMisMod != null && ("0011".equals(lMisMod.getCodTipoMisura())
						|| "0197".equals(lMisMod.getCodTipoMisura())
						|| "2340".equals(lMisMod.getCodTipoMisura()))) {
					lMisDAO.setDAOFromModelForUpdate(lMisMod);
					lMisDAO.setFlagSituazione("N");
					lMisDAO.update();
					lMisDAO.stop();
				}
			}

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
			}
			// lEveDao.stop();

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			Vector lNotifiche = new Vector(lNotEveDao.getModels());

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			// lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveApp.getDataEmissione());

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			// ** POSIZIONE_GIURIDICA **
			boolean lCodStatoAggiornato = false;
			if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) // Altra
																										// causa
			{
				lStatoProcMod.setCodStatoProcedimento("0237"); // Emesso Ordine di Esecuzione in Carcere in
																// seguito a Revoca Sanzione Sostitutiva il
				lCodStatoAggiornato = true;
			}

			int lIntPos = Integer.parseInt(lCodPosizione);

			switch (lIntPos) {
			case 07: // Libero
			case 10: // Libero
			{
				lCodPosizione = "10";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0236"); // Emesso Ordine di Esecuzione con Arresto
																	// in seguito a Revoca Sanzione
																	// Sostitutiva il
				break;
			}
			default: // Altri casi
				lStatoProcMod.setCodStatoProcedimento("0057"); // Emesso Ordine di Esecuzione Il
			}

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			// Inserisci STATO Del PROCEDIMENTO
			lStatoProcMod.setProgressivo(new BigDecimal("1"));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// ** Aggiorna SCADENZARIO se la POSIZIONE_GIURIDICA e' LIBERO **
			if ((lIntPos == 7 || lIntPos == 10 || lIntPos == 16 || lIntPos == 17 || lIntPos == 20
					|| lIntPos == 26 || lIntPos == 30 || lIntPos == 46 || lIntPos == 47)
					&& (aFascicolo.getFlagAltraCausa() != null
							&& aFascicolo.getFlagAltraCausa().equals("N"))) {
				ParametroModel lParMod = new ParametroModel();

				lParMod.setNomeParametro("VANE RICERCHE");
				lParMod.setCodUfficioValidita(aEvento.getCodUfficioAggiornamento());

				Vector lVectPar = null;
				IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
				lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

				ScadenzarioModel lScaMod = new ScadenzarioModel();

				lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				NotificaModel lNotifica = (NotificaModel) lNotifiche.get(0);
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
				lScaMod.setCodTipoScadenzario("03");

				Vector lScadenzarii = null;
				lScadeDao = new ScadenzarioSqlDAO(lConn);
				// cerca un scadenzario per id fascicolo e per tipo scadenzario
				lScadeDao.ricercaScadenzarioVerbaleArresto(lScaMod);
				lScadenzarii = new Vector(lScadeDao.getModels());

				lScaDao = new ScadenzarioDAO(lConn);

				if (lScadenzarii.size() == 0) {
					lScaMod.setFlagVisto("N");
					lScaMod.setDataFineScadenza(lFineScadenza);
					lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lScaMod.setDataInserimento(aEvento.getDataAggiornamento());
					// AMBROSINO 09-02-2011 - Lego lo scadenzario all'evento che lo genera
					lScaMod.setEveIdEvento(aEvento.getIdEvento());
					// a6-rr-238
					lScaMod.setCodStatoNotifica("NP");
					lScaDao.setDAOFromModel(lScaMod);
					// BigDecimal lKeyScad = null;
					/* lKeyScad = */lScaDao.insert();
				} else {
					ScadenzarioModel lScaModID = (ScadenzarioModel) lScadenzarii.get(0);
					lScaMod.setIdScadenzario(lScaModID.getIdScadenzario());
					lScaMod.setDataFineScadenza(lFineScadenza);
					lScaMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaMod.setDataAggiornamento(aEvento.getDataAggiornamento());
					// AMBROSINO 30/06/2011
					lScaMod.setEveIdEvento(aEvento.getIdEvento());
					lScaMod.setCodStatoNotifica("NP");
					// lScaDao.setDAOFromModelForUpdate(lScaMod);

					lScaDao.setEveIdEvento(lScaMod.getEveIdEvento());
					lScaDao.update();
				}
			}

			/****************** Aggiorna PENA_RESIDUA ************************************************/

			// lPenResDao = new PenaResiduaDAO(lConn);
			PenaResiduaModel lPenResMod = new PenaResiduaModel();

			// Prende la Pena Residua legata all'evento
			IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			lPenResMod = lCtrl.ExRicercaPenaResiduaByIdEvento(aEvento.getIdEvento());

			lPenResMod.setFlagValidato(aEvento.getFlagDocumentoRegistrato());
			// lPenResMod.setEveIdEvento(aEvento.getIdEvento());
			lPenResMod.setDataInserimento(DateUtils.getSysDate());

			PenaResiduaDAO lPenDao = new PenaResiduaDAO(lConn);
			lPenDao.setDAOFromModelForUpdate(lPenResMod);
			lPenDao.update();

			/*************************************************
			 * FINE
			 **************************************************************/

			// ********************************************POSIZIONE
			// GIURIDICA**************************************************/
			if (lIntPos != 16 && lIntPos != 20 && lIntPos != 46 && lIntPos != 47 && lIntPos != 12) {
				// Aggiorna POSIZIONE_GIURIDICA
				lPosDao = new PosizioneGiuridicaDAO(lConn);

				lPosDao.setDataFine(lEveApp.getDataEmissione());
				lPosDao.setDataAggiornamento(DateUtils.getSysDate());
				lPosDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());

				lPosDao.update();
				lPosDao.stop();

				// Inserisco la nuova posizione giuridica
				// e la aggancio all'evento validato
				lPosDao.setCodPosizioneGiuridica(lCodPosizione);
				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(DateUtils.getSysDate());
				lPosDao.setDataInizio(lEveApp.getDataEmissione());
				lPosDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPosDao.setIdEventoRiferimento(aEvento.getIdEvento()); // **

				lKeyPos = lPosDao.insert();
				lPosDao.stop();
			}
			/********************************************
			 * FINE POSIZIONE GIURIDICA
			 ***********************************************/

			// SCADENZARIO FINE PENA
			lScaDao = new ScadenzarioDAO(lConn);
			ScadenzarioSqlDAO lScaSqlDao = new ScadenzarioSqlDAO(lConn);

			if (lPenResMod != null && lPenResMod.getFlagValidato().equals("S")
					&& lPenResMod.getDataFine() != null) {
				ScadenzarioModel lScaMod = new ScadenzarioModel();
				if (!lCodPosizione.equals("12") || !lCodPosizione.equals("07") || !lCodPosizione.equals("10")
						|| !lCodPosizione.equals("16") || !lCodPosizione.equals("17")
						|| !lCodPosizione.equals("30") || !lCodPosizione.equals("20")
						|| !lCodPosizione.equals("26") || !lCodPosizione.equals("46")
						|| !lCodPosizione.equals("47")) {
					lScaSqlDao.ricercaScadenzarioByIdFascicolo(aFascicolo.getIdFascicoloSiep());
					lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

					if (lScaMod != null) {
						// aggiorna scadenzario
						lScaDao.setDataFineScadenza(lPenResMod.getDataFine());
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
						lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio());
						lScaMod.setDataFineScadenza(lPenResMod.getDataFine());
						lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
						lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
						lScaMod.setDataInserimento(DateUtils.getSysDate());
						lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

						lScaDao.setDAOFromModel(lScaMod);
						lScaDao.insert();
						lScaDao.stop();
					}
				}
			}

			// CANCELLA SCADENZARIO DETENZIONE A TERMINE
			if (lCodPosizione.equals("12")) {
				String[] lTipo = { "14" };
				lScaSqlDao.ricercaScadenzarioPerTipoScadenzario(lTipo, aFascicolo.getIdFascicoloSiep());
				ScadenzarioModel lScaModDet = (ScadenzarioModel) lScaSqlDao.getModelByKey();
				if (lScaModDet != null) {
					lScaDao.setCondizioneDelete(lScaModDet.getIdScadenzario());
					lScaDao.delete();
					lScaDao.stop();
				}
			}

			// aggiorna il luogo detenzione LUOGO DETENZIONE MODIFICA DEL 25/05/2004
			lLuogoDAO = new LuogoDetenzioneDAO(lConn);
			lLuogoSql = new LuogoDetenzioneSqlDAO(lConn);

			LuogoDetenzioneModel lLuoDet = null;

			if (lPosMod != null && lPosMod.getIdPosizioneGiuridica() != null) {
				lLuogoSql.ricercaLuogoDetenzioneByIdPosizione(lPosMod.getIdPosizioneGiuridica());
				lLuoDet = (LuogoDetenzioneModel) lLuogoSql.getModelByKey();

				if (lLuoDet != null && lLuoDet.getIdLuogoDetenzione() != null
						&& lLuoDet.getIstDetIdIstitutoDetenzione() != null && lKeyPos != null) {
					lLuogoDAO.setCondizioneUpdate(lLuoDet.getIdLuogoDetenzione());
					lLuogoDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lLuogoDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lLuogoDAO.setDataAggiornamento(DateUtils.getSysDate());
					lLuogoDAO.setDataFineDetenzione(DateUtils.getSysDate());
					lLuogoDAO.update();
					lLuogoDAO.stop();

					lLuogoDAO.setDataInserimento(DateUtils.getSysDate());
					lLuogoDAO.setDataInizioDetenzione(DateUtils.getSysDate());
					lLuogoDAO.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lLuogoDAO.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lLuogoDAO.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					lLuogoDAO.setIstDetIdIstitutoDetenzione(lLuoDet.getIstDetIdIstitutoDetenzione());
					lLuogoDAO.setPosGiuIdPosizioneGiuridica(lKeyPos);
					lLuogoDAO.insert();
					lLuogoDAO.stop();
				}
			}

			// Aggiorna i giorni di Lib Anticipata computati da E ad S
			/*
			 * lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
			 * lLicSqlDao.updateFlagElaboratoByIdFascicolo(aFascicolo.getIdFascicoloSiep(), "E", "S");
			 */
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

			throw new F3BException("OrdineEsecuzioneController.ExUpdateValidaOESanSos : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("OrdineEsecuzioneController.ExUpdateValidaOESanSos : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lNotEveDao);
			cleanup(lScaDao);
			cleanup(lScadeDao); // sca
			cleanup(lEveSql); // sca
			cleanup(lNomProvDao);
			cleanup(lLuogoDAO);
			cleanup(lLuogoSql);
			cleanup(lLicSqlDao);
			cleanup(lMisDao);
			cleanup(lMisDAO);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * legge simeone
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaLS(EventoModel aEvento, FascicoloSiepModel aFascicolo)
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
		FascicoloSiepDAO lFasDao = null;
		MisuraCautelareSqlDAO lMisCauDao = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

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
			// lEveDao.stop();
			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			// Vector lNotifiche = new Vector(lNotEveDao.getModels());

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// ============================================================
			// Cerca POSIZIONE_GIURIDICA corrente e se esiste ALTRA_CAUSA
			// ============================================================
			AltraCausaModel lAcModel = new AltraCausaModel();
			IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
			// if (lPosMod!=null && lPosMod.getAltCauIdAltraCausa()!=null ||
			// lCodPosizione.equalsIgnoreCase("07")){
			if (lPosMod != null && lPosMod.getAltCauIdAltraCausa() != null) {
				lAcModel = lAC.ExRicercaAltraCausaIstitutoByKey(lPosMod.getAltCauIdAltraCausa());
			} else if (lCodPosizione != null && lCodPosizione.equalsIgnoreCase("07")) {
				lAcModel = lAC.ExRicercaAltraCausaByFascicolo(aFascicolo.getIdFascicoloSiep());
			}

			if (lPosMod.getAltCauIdAltraCausa() != null || lCodPosizione.equalsIgnoreCase("07")) {
				if (lAcModel != null) {
					lCodPosizione = Integer.valueOf(lAcModel.getCodTipoPosGiuridica()).toString();
				}
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

				// /-- Transazione!!!--- IPenaResidua lCtrlPen = SIEPLookupRemote.getPenaResiduaRemote();
				// /-- Transazione!!!--- PenaResiduaModel llPenModRet =
				// lCtrlPen.ExInserisciPenaResidua(lPenResMod);
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

			case 2:
			case 4: // Arresti domiciliari
			{

				// lCodPosizione = "04"; 14/10/03 modifica
				lPosizione = "04";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0011");
				break;
			}
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
					lStatoProcMod.setCodStatoProcedimento("0011");
				break;
			}
			case 70: {
				lPosizione = "84";
				lStatoProcMod.setCodStatoProcedimento("0011");
				break;
			}
			case 71: // Libero
			{
				lPosizione = "82";
				lStatoProcMod.setCodStatoProcedimento("0011");
				break;
			}
			case 72: {
				lPosizione = "83";
				lStatoProcMod.setCodStatoProcedimento("0011");
				break;
			}
			case 76: // Libero
			case 77: // Libero
			case 78: // Libero
			case 79: // Libero
			case 80: // Libero
			case 81: // Libero
			{
				// per le posizioni iniziali 76, 77, 78, 79, 80, 81(ALTRA_CAUSA)
				// la posizione finale(POSIZIONE_GIURIDICA) non deve essere 10 ma deve essere uguale a quella
				// iniziale 07
				// posizioni giuridiche finali dopo emissione OE(Ordini di Esecuzione/Scarcerazione
				// Sospensione Esecuzione ex art. 656 c.p.p. Revoca Sospensione Esecuzione ex art. 656
				// c.p.p.)
				// devono essere uguali a quelle iniziali
				// la vecchia posizione giuridica iniziale va chiusa valorizzando il campo DATA_FINE con data
				// sistema

				// lPosizione = "10";
				lPosizione = "07";
				lStatoProcMod.setCodStatoProcedimento("0011");
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

			/*********************************************
			 * FINE
			 **********************************************/

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			/*********************** INSERISCE 3 RECORD SE POS.GIU. COMPRESO TRA (2,4) **********************/

			// modifica richiesta il 7-12-2004 da luciana per indicazione del cliente
			// for (int ins = 1; ins < 4; ins++)
			// {
			// lStatoProcMod.setProgressivo(new BigDecimal(ins));
			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			/*************************************************
			 * FINE
			 **************************************************************/
			/*********************************************
			 * NOME PROVVEDIMENTO
			 ************************************************/
			NomeProvvedimentoModel lNomProvMod = new NomeProvvedimentoModel();
			lNomProvDao = new NomeProvvedimentoDAO(lConn);

			if (lIntPos == 2 || lIntPos == 4) {
				lNomProvMod.setCodNomeProvvedimento("NP016");
			} else {
				lNomProvMod.setCodNomeProvvedimento("NP015");
			}

			lNomProvMod.setEveIdEvento(aEvento.getIdEvento());
			lNomProvDao.setDAOFromModel(lNomProvMod);
			lNomProvDao.insert();

			/*********************************************
			 * FINE NOME PROVVEDIMENTO
			 **********************************************/
			/********************************************
			 * POSIZIONE GIURIDICA
			 **************************************************/

			// Aggiorna POSIZIONE_GIURIDICA

			if (lCodPosizione.equals("02") || lCodPosizione.equals("07") || lCodPosizione.equals("10")
					|| lCodPosizione.equals("70") || lCodPosizione.equals("71") || lCodPosizione.equals("72")
					|| lCodPosizione.equals("76") || lCodPosizione.equals("77") || lCodPosizione.equals("78")
					|| lCodPosizione.equals("79") || lCodPosizione.equals("80") || lCodPosizione.equals("81")
					|| (aFascicolo.getFlagAltraCausa() != null
							&& aFascicolo.getFlagAltraCausa().equals("S"))) {
				lPosDao = new PosizioneGiuridicaDAO(lConn);

				lPosDao.setDataFine(DateUtils.getSysDate());
				lPosDao.setDataAggiornamento(DateUtils.getSysDate());
				lPosDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());

				lPosDao.update();
				lPosDao.stop();

				// ricerca della misura cautelare legata alla posizione giuriduca modificata per essere
				// inserita
				// insert nuova misura cautelare legata alla nuova posizione giuridica
				// (la nuova misura cautelare e' uguale alla misura cautelare legata alla precedente posizione
				// giuridica modificata)
				// decisione presa con S.S./V.B. Settembre 2015
				MisuraCautelareModel misuraCautelare = new MisuraCautelareModel();
				misuraCautelare.setPosGiuIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
				lMisCauDao = new MisuraCautelareSqlDAO(lConn);
				lMisCauDao.ricercaMisuraCautelare(misuraCautelare);
				misuraCautelare = (MisuraCautelareModel) lMisCauDao.getModelByKey();

				// inserisco la nuova posizione giuridica
				if (lCodPosizione.equalsIgnoreCase("76") || lCodPosizione.equalsIgnoreCase("77")
						|| lCodPosizione.equalsIgnoreCase("78") || lCodPosizione.equalsIgnoreCase("79")
						|| lCodPosizione.equalsIgnoreCase("80") || lCodPosizione.equalsIgnoreCase("81")) {
					// inserisco la nuova posizione giuridica e' perfettamente uguale alla precedente, serve
					// per mantenere lo storico di diverse posizioni giuridiche
					// anche se hanno uguali valori(arresti domiciliari che si riferiscono a due ordinanze
					// diverse ==> cambia solo il periodo)
					lPosDao.setCodMaschera(lPosMod.getCodMaschera());
					// AltraCausaSqlDAO lAltCauDao = null;
					AltraCausaModel lAltraCausa = new AltraCausaModel();
					if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) // Altra
																												// causa
					{
						// inserisco la nuova ALTRA_CAUSA uguale alla precedente, serve per mantenere lo
						// storico di diverse posizioni giuridiche
						// anche se hanno uguali valori(arresti domiciliari che si riferiscono a due ordinanze
						// diverse ==> cambia solo il periodo)
						// lAltCauDao = new AltraCausaSqlDAO(lConn);
						// lAltCauDao.ricercaAltraCausaByIdFascicolo(aFascicolo.getIdFascicoloSiep());
						// AltraCausaModel lAltrMod = new AltraCausaModel();
						if (lPosMod != null && lPosMod.getAltCauIdAltraCausa() != null) {
							IAltraCausa altraCausa = SIEPLookupRemote.getAltraCausa();
							lAltraCausa = altraCausa
									.ExRicercaAltraCausaIstitutoByKey(lPosMod.getAltCauIdAltraCausa());
						}
						// lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();
						// Ticket#20220803019 - In assenza del record AC legato all'ultima PG
						// il sistema andava in errore sql inquanto cercava di inserire un model lAltraCausa
						// vuoto
						if (lAltraCausa != null && lAltraCausa.getIdAltraCausa() != null) {
							AltraCausaDAO lAltraCausaDao = null;
							lAltraCausaDao = new AltraCausaDAO(lConn);
							BigDecimal lKeyAltra = null;
							lAltraCausa.setDataInserimento(DateUtils.getSysDate());
							lAltraCausaDao.setDAOFromModel(lAltraCausa);
							lKeyAltra = lAltraCausaDao.insert();
							lPosDao.setAltCauIdAltraCausa(lKeyAltra);
						}
						// Ticket#20220803019 - FINE
					}
				}
				lPosDao.setCodPosizioneGiuridica(lPosizione);
				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(DateUtils.getSysDate());
				lPosDao.setDataInizio(DateUtils.getSysDate());
				lPosDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPosDao.setIdEventoRiferimento(aEvento.getIdEvento()); // **
				lPosDao.setLuogoEspiazione(lPosMod.getLuogoEspiazione());

				BigDecimal lKeyPosGiu = lPosDao.insert();

				// insert la nuova misura cautelare legata alla nuova posizione giuridica
				// (la nuova misura cautelare e' uguale alla misura cautelare legata alla precedente posizione
				// giuridica modificata)
				// decisione presa con Salvatore/Vito Settembre 2015

				// Modifica del 23/02/2016 Nuova Infrastruttura INIZIO ******
				if (misuraCautelare != null) {
					// Modifica del 23/02/2016 Nuova Infrastruttura FINE ******
					misuraCautelare.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);
					Vector lVectMod = new Vector();
					lVectMod.add(misuraCautelare);
					IMisuraCautelare lCtrlMisCau = SIEPLookupRemote.getMisuraCautelareRemote();
					lCtrlMisCau.ExInserisciMisuraCautelare(lVectMod);
				}

				// MERGE v10: eliminata riga per doppio inserimento
				lPosDao.stop();
			}
			/********************************************
			 * FINE POSIZIONE GIURIDICA
			 ***********************************************/

			lScaDao = new ScadenzarioDAO(lConn);

			// SCADENZARIO FINE PENA
			if (lPenResMod != null && lPenResMod.getFlagValidato().equals("S")
					&& lPenResMod.getDataFine() != null) {
				ScadenzarioModel lScaMod = new ScadenzarioModel();
				if (!lCodPosizione.equals("07") || !lCodPosizione.equals("10")) {
					lScaSqlDao = new ScadenzarioSqlDAO(lConn);

					lScaSqlDao.ricercaScadenzarioByIdFascicolo(aFascicolo.getIdFascicoloSiep());
					lScaMod = (ScadenzarioModel) lScaSqlDao.getModelByKey();

					if (lScaMod != null) {
						// aggiorna scadenzario
						lScaDao.setDataFineScadenza(lPenResMod.getDataFine());
						lScaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
						lScaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lScaDao.setDataAggiornamento(DateUtils.getSysDate());
						// lScaModAgg.setIdScadenzario(lScaMod.getIdScadenzario());
						lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
						lScaDao.update();
						lScaDao.stop();
					} else {
						lScaMod = new ScadenzarioModel();

						// inserisce scadenzario
						lScaMod.setCodTipoScadenzario("02");

						lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio());
						lScaMod.setDataFineScadenza(lPenResMod.getDataFine());

						lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
						lScaMod.setDataInserimento(DateUtils.getSysDate());
						lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

						lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

						lScaDao.setDAOFromModel(lScaMod);
						lScaDao.insert();
						lScaDao.stop();
					}
				}
			}

			// SCADENZARIO SIMEONE:
			// Nel caso sia emesso un OE 0061 o 0117 viene inserita sulla tabella SCADENZARIO_SIEP
			// un record con DATA_INIZIO_SCADENZA uguale a quella di emissione dell'OE.
			// Funzionalmente questo record su SCADENZARIO_SIEP dice che l'OE Simeone e' in attesa di notifica

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Inizio Scadenzario Simeone");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("aEvento.getCodMotivo() : " + lEveApp.getCodMotivo());

			if ("0061".equals(lEveApp.getCodMotivo()) // 0061 = [OE] - con contestuale Sospensione - Libero
					|| "0104".equals(lEveApp.getCodMotivo()) // 0104 = [OE] - con contestuale Sospensione -
																// Libero con istanza paolo cherubini
																// 23/06/2011
					|| "0117".equals(lEveApp.getCodMotivo()) // 0117 = [OE] - con contestuale Sospensione -
																// Detenuto altra causa
					// Modifica del 13/01/2017
					// Anche nel caso sia emesso un OE con i seguenti codici:
					// 5510-5511-5512-5513 (Cod. MOTIVO_PROVVEDIMENTO)
					// deve essere inserito un record sulla tabella SCADENZARIO_SIEP
					|| "5510".equals(lEveApp.getCodMotivo()) // 5510 = [OE] Altra causa - Arresti domiciliari
					|| "5511".equals(lEveApp.getCodMotivo()) // 5511 = [OE] Altra causa - Permanenza in casa
					|| "5512".equals(lEveApp.getCodMotivo()) // 5512 = [OE] Altra causa - Collocamento in
																// comunita'
					|| "5513".equals(lEveApp.getCodMotivo()) // 5513 = [OE] Altra causa - Arresti domiciliari
																// ex art. 89 DPR309/90
			) {
				// 20171128 [CARMELA] SEGNALAZIONE DI TESTA SU DUPLICAZIONE DI RECORD IN SCADENZARIO Decreti
				// in corso di Definizione
				// ********** INIZIO ***************
				lScaSqlDao = new ScadenzarioSqlDAO(lConn);
				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("01",
						aFascicolo.getIdFascicoloSiep());
				ScadenzarioModel lScadModel = (ScadenzarioModel) lScaSqlDao.getModelByKey();
				if (lScadModel != null) {
					// aggiorna scadenzario
					lScaDao.setDataInizioScadenza(lEveApp.getDataEmissione());
					lScaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDao.setDataAggiornamento(DateUtils.getSysDate());
					lScaDao.setCondizioneUpdate(lScadModel.getIdScadenzario());
					lScaDao.update();
					lScaDao.stop();

				} else {
					// CARMELA ********** FINE ***************
					lScaDao.setCodTipoScadenzario("01");
					lScaDao.setDataInizioScadenza(lEveApp.getDataEmissione());
					lScaDao.setDataFineScadenza(null);
					lScaDao.setFlagVisto("N");
					lScaDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lScaDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lScaDao.setDataInserimento(DateUtils.getSysDate());
					lScaDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					lScaDao.setCodStatoNotifica("N");
					lScaDao.setEveIdEvento(aEvento.getIdEvento());
					// lScaDao.setNotIdNotifica(lNotMod.getIdNotifica());

					lScaDao.insert();
					lScaDao.stop();
				}
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Fine Scadenzario Simeone");

			// Aggiorna i giorni di Lib Anticipata computati non elaborati ad E
			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicSqlDao.updateFlagElaboratoByIdFascicolo(aFascicolo.getIdFascicoloSiep(), "E", "S");

			commit(lConn);

			// ------- EVENTO--------
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			// aggiornamento FLAG_ALTRA_CAUSA nella tabella FASCICOLO_SIEP
			// POS_GIU=10 && COD_MASCHERA=L ==> FASCICOLO_SIEP.FLAG_ALTRA_CAUSA=NULL
			lFasDao = new FascicoloSiepDAO(lConn);
			if (lPosizione != null && lPosMod != null && lPosMod.getCodMaschera() != null
					&& lPosizione.equalsIgnoreCase("10") && lPosMod.getCodMaschera().equalsIgnoreCase("L")) {
				aFascicolo.setFlagAltraCausa(null);
				lFasDao.setDAOFromModelForUpdate(aFascicolo);
				lFasDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());
				lFasDao.update();
			} else if (lPosizione != null && lPosizione.equalsIgnoreCase("07") && lCodPosizione != null
					&& (lCodPosizione.equalsIgnoreCase("76") || lCodPosizione.equalsIgnoreCase("77")
							|| lCodPosizione.equalsIgnoreCase("78") || lCodPosizione.equalsIgnoreCase("79")
							|| lCodPosizione.equalsIgnoreCase("80")
							|| lCodPosizione.equalsIgnoreCase("81"))) {
				// la nuova posizione giuridica e' perfettamente uguale alla precedente, serve per mantenere
				// lo
				// storico di diverse posizioni giuridiche
				// anche se hanno uguali valori(arresti domiciliari che si riferiscono a due ordinanze diverse
				// ==> cambia solo il periodo)
				// questa posizione conserva ALTRA_CAUSA
				aFascicolo.setFlagAltraCausa("S");
			} else {
				aFascicolo.setFlagAltraCausa(null);
			}
			lFasDao.setDAOFromModelForUpdate(aFascicolo);
			lFasDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());
			lFasDao.update();

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			rollback(lConn);
			rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("OrdineEsecuzioneController.ExUpdateLS : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Exception: " + ex);
			rollback(lConn);
			rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("OrdineEsecuzioneController.ExUpdateLS : " + ex);
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
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFasDao);
			cleanup(lMisCauDao);
		}

		return lEveMod;
	}

	/**
	 * legge simeone per Sanzioni Sostitutive
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaLSSanSos(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lSca03Dao = null;
		ScadenzarioSqlDAO lScadeDao = null;
		NomeProvvedimentoDAO lNomProvDao = null;
		EventoSqlDAO lEveSql = null;
		LicenzaLibanticipataSqlDAO lLicSqlDao = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

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
			// lEveDao.stop();
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

			/****************** Aggiorna PENA_RESIDUA ************************************************/

			// lPenResDao = new PenaResiduaDAO(lConn);
			PenaResiduaModel lPenResMod = new PenaResiduaModel();

			// Prende la Pena Residua legata all'evento
			IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			lPenResMod = lCtrl.ExRicercaPenaResiduaByIdEvento(aEvento.getIdEvento());

			lPenResMod.setFlagValidato(aEvento.getFlagDocumentoRegistrato());
			// lPenResMod.setEveIdEvento(aEvento.getIdEvento());
			lPenResMod.setDataInserimento(DateUtils.getSysDate());

			PenaResiduaDAO lPenDao = new PenaResiduaDAO(lConn);
			lPenDao.setDAOFromModelForUpdate(lPenResMod);
			lPenDao.update();

			/*************************************************
			 * FINE
			 **************************************************************/

			// ** POSIZIONE_GIURIDICA **
			boolean lCodStatoAggiornato = false;
			if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) // Altra
																										// causa
			{
				lStatoProcMod.setCodStatoProcedimento("0239");
				lCodStatoAggiornato = true;
			}

			int lIntPos = Integer.parseInt(lCodPosizione);

			String lPosizione = null;
			switch (lIntPos) {
			case 7:
			case 10: // Libero
			{
				lPosizione = "10";
				if (!lCodStatoAggiornato)
					lStatoProcMod.setCodStatoProcedimento("0239");
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

			/*********************************************
			 * FINE
			 **********************************************/

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			/*********************** INSERISCE 3 RECORD SE POS.GIU. COMPRESO TRA (2,4) **********************/

			// modifica richiesta il 7-12-2004 da luciana per indicazione del cliente
			// for (int ins = 1; ins < 4; ins++)
			// {
			// lStatoProcMod.setProgressivo(new BigDecimal(ins));
			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			/********************************************
			 * POSIZIONE GIURIDICA
			 **************************************************/

			// Aggiorna POSIZIONE_GIURIDICA
			if (lCodPosizione.equals("02") || lCodPosizione.equals("07") || lCodPosizione.equals("10")
					|| (aFascicolo.getFlagAltraCausa() != null
							&& aFascicolo.getFlagAltraCausa().equals("S"))) {
				lPosDao = new PosizioneGiuridicaDAO(lConn);

				lPosDao.setDataFine(DateUtils.getSysDate());
				lPosDao.setDataAggiornamento(DateUtils.getSysDate());
				lPosDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());

				lPosDao.update();
				lPosDao.stop();

				// inserisco la nuova posizione giuridica
				lPosDao.setCodPosizioneGiuridica(lPosizione);
				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(DateUtils.getSysDate());
				lPosDao.setDataInizio(DateUtils.getSysDate());
				lPosDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPosDao.setIdEventoRiferimento(aEvento.getIdEvento()); // **

				lPosDao.insert();
				lPosDao.stop();
			}
			/********************************************
			 * FINE POSIZIONE GIURIDICA
			 ***********************************************/

			// SCADENZARIO 03
			// AMBROSINO 07-02-2011 - Se NON ESISTE la penaresidua INSERISCE SCADENZARIO 03

			if (lPenResMod.getDataFine() == null) {
				ParametroModel lParMod = new ParametroModel();

				lParMod.setNomeParametro("VANE RICERCHE");
				lParMod.setCodUfficioValidita(aEvento.getCodUfficioAggiornamento());

				Vector lVectPar = null;
				IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
				lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

				ScadenzarioModel lSca03Mod = new ScadenzarioModel();

				lSca03Mod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				NotificaModel lNotifica = (NotificaModel) lNotifiche.get(0);
				lSca03Mod.setDataInizioScadenza(lNotifica.getDataInvio());

				Iterator lIter = lVectPar.iterator();
				Date lSommaAnni = null;
				Date lSommaMesi = null;
				Date lFineScadenza = null;

				if (lIter.hasNext()) {
					ParametroModel lParModel = (ParametroModel) lIter.next();
					lSommaAnni = DateUtils.moveDateTo(lSca03Mod.getDataInizioScadenza(),
							java.util.Calendar.YEAR, lParModel.getAnni().intValue());
					lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
							lParModel.getMesi().intValue());
					lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
							lParModel.getGiorni().intValue());
				}

				// MODIFICA
				lSca03Mod.setCodTipoScadenzario("03"); // Vane ricerche
				Vector lScadenzarii = null;
				lScadeDao = new ScadenzarioSqlDAO(lConn);
				// cerca un scadenzario per id fascicolo e per tipo scadenzario
				lScadeDao.ricercaScadenzarioVerbaleArresto(lSca03Mod);
				lScadenzarii = new Vector(lScadeDao.getModels());

				lSca03Dao = new ScadenzarioDAO(lConn);

				if (lScadenzarii.size() == 0) {
					lSca03Mod.setFlagVisto("N");
					lSca03Mod.setDataFineScadenza(lFineScadenza);
					lSca03Mod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lSca03Mod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lSca03Mod.setDataInserimento(aEvento.getDataAggiornamento());
					// a6-rr-238
					lSca03Mod.setEveIdEvento(aEvento.getIdEvento());
					lSca03Mod.setCodStatoNotifica("NP");
					lSca03Dao.setDAOFromModel(lSca03Mod);
					// BigDecimal lKeyScad = null;
					/* lKeyScad = */lSca03Dao.insert();
				} else {
					ScadenzarioModel lScaModID = (ScadenzarioModel) lScadenzarii.get(0);
					lSca03Mod.setIdScadenzario(lScaModID.getIdScadenzario());
					lSca03Mod.setDataFineScadenza(lFineScadenza);
					lSca03Mod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lSca03Mod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lSca03Mod.setDataAggiornamento(aEvento.getDataAggiornamento());
					// a6-rr-238
					lSca03Mod.setCodStatoNotifica("NP");
					// AMBROSINO 30/06/2011
					lSca03Mod.setEveIdEvento(aEvento.getIdEvento());
					lSca03Dao.setDAOFromModelForUpdate(lSca03Mod);
					lSca03Dao.update();
				}
			}

			// END AMBROSINO 07-02-2

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

			throw new F3BException("OrdineEsecuzioneController.ExUpdateLS : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Exception: " + ex);
			rollback(lConn);
			rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("OrdineEsecuzioneController.ExUpdateLS : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lNotEveDao);
			cleanup(lSca03Dao);
			cleanup(lScadeDao);
			cleanup(lNomProvDao);
			cleanup(lLicSqlDao);
			cleanup(lEveSql);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	// metodo revoca legge simeone
	public EventoModel ExUpdateValidaRS(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosGiuDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		AltraCausaSqlDAO lAltCauDao = null;
		FascicoloSiepDAO lFasDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		ScadenzarioDAO lSca03Dao = null;
		ScadenzarioSqlDAO lScadeDao = null;
		MisuraCautelareSqlDAO lMisCauDao = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);

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
			Vector lNotifiche = new Vector(lNotEveDao.getModels());

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = "";
			if (lPosMod != null)
				lCodPosizione = lPosMod.getCodPosizioneGiuridica();
			// ============================================================
			// Cerca POSIZIONE_GIURIDICA corrente e se esiste ALTRA_CAUSA
			// ============================================================
			AltraCausaModel lAcModel = new AltraCausaModel();
			IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
			// if (lPosMod!=null && lPosMod.getAltCauIdAltraCausa()!=null ||
			// lCodPosizione.equalsIgnoreCase("07")){
			if (lPosMod != null && lPosMod.getAltCauIdAltraCausa() != null) {
				lAcModel = lAC.ExRicercaAltraCausaIstitutoByKey(lPosMod.getAltCauIdAltraCausa());
			} else if (lCodPosizione != null && lCodPosizione.equalsIgnoreCase("07")) {
				lAcModel = lAC.ExRicercaAltraCausaByFascicolo(aFascicolo.getIdFascicoloSiep());
			}

			if (lPosMod != null
					&& (lPosMod.getAltCauIdAltraCausa() != null || lCodPosizione.equalsIgnoreCase("07"))) {
				if (lAcModel != null) {
					lCodPosizione = Integer.valueOf(lAcModel.getCodTipoPosGiuridica()).toString();
				}
			}

			// CANCELLAZIONE SCADENZARIO SIMEONE MODIFICA 15-10-04 RICHIESTA DA VIVIANA---DARIO
			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);

			ScadenzarioModel lScaMod = null;
			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("01", aFascicolo.getIdFascicoloSiep());
			Vector scadenzari = new Vector(lScaSqlDao.getModels());
			for (int y = 0; y < scadenzari.size(); y++) {
				lScaMod = (ScadenzarioModel) scadenzari.get(y);
				if (lScaMod != null) {
					lScaDao.setCondizioneDelete(lScaMod.getIdScadenzario());
					lScaDao.delete();
					lScaDao.stop();
				}
			}

			// Aggiorna PENA_RESIDUA
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			PenaResiduaModel lPenResMod = new PenaResiduaModel();

			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			if (lPenResMod.getEveIdEvento() == null) {
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
				lPenResDao.setEveIdEvento(aEvento.getIdEvento());
				lPenResDao.setFlagValidato("S");
				lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPenResDao.setDataAggiornamento(DateUtils.getSysDate());
				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				lPenResDao.setDAOFromModel(lPenResMod);
				lPenResDao.setFlagValidato("S");
				lPenResDao.setEveIdEvento(aEvento.getIdEvento());
				lPenResDao.setDataInserimento(DateUtils.getSysDate());
				lPenResDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lPenResDao.insert();
				lPenResDao.stop();
			}

			// INSERISCE SCADENZARIO FINE PENA MODIFICA 20-01-05--SCA
			ScadenzarioModel lScaModel = null;

			lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo("02", aFascicolo.getIdFascicoloSiep());
			lScaModel = (ScadenzarioModel) lScaSqlDao.getModelByKey();

			if (lScaModel != null && lPenResMod != null && lPenResMod.getDataFine() != null) {
				// aggiorna scadenzario
				lScaDao.setDataFineScadenza(lPenResMod.getDataFine());
				lScaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lScaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lScaDao.setDataAggiornamento(DateUtils.getSysDate());
				// lScaModAgg.setIdScadenzario(lScaMod.getIdScadenzario());
				lScaDao.setCondizioneUpdate(lScaModel.getIdScadenzario());
				lScaDao.update();
				lScaDao.stop();
			} else if (lScaModel == null && lPenResMod != null && lPenResMod.getDataFine() != null
					&& lPenResMod.getDataInizio() != null) {
				lScaMod = new ScadenzarioModel();

				// inserisce scadenzario
				lScaMod.setCodTipoScadenzario("02");

				lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio());
				lScaMod.setDataFineScadenza(lPenResMod.getDataFine());

				lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lScaMod.setDataInserimento(DateUtils.getSysDate());
				lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

				lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

				lScaDao.setDAOFromModel(lScaMod);
				lScaDao.insert();
				lScaDao.stop();
			}

			// AMBROSINO 07-02-2011 - Se NON ESISTE la penaresidua INSERISCE SCADENZARIO 03

			if (lPenResMod.getDataFine() == null) {
				ParametroModel lParMod = new ParametroModel();

				lParMod.setNomeParametro("VANE RICERCHE");
				lParMod.setCodUfficioValidita(aEvento.getCodUfficioAggiornamento());

				Vector lVectPar = null;
				IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
				lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

				ScadenzarioModel lSca03Mod = new ScadenzarioModel();

				lSca03Mod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				NotificaModel lNotifica = (NotificaModel) lNotifiche.get(0);
				lSca03Mod.setDataInizioScadenza(lNotifica.getDataInvio());

				Iterator lIter = lVectPar.iterator();
				Date lSommaAnni = null;
				Date lSommaMesi = null;
				Date lFineScadenza = null;

				if (lIter.hasNext()) {
					ParametroModel lParModel = (ParametroModel) lIter.next();
					lSommaAnni = DateUtils.moveDateTo(lSca03Mod.getDataInizioScadenza(),
							java.util.Calendar.YEAR, lParModel.getAnni().intValue());
					lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
							lParModel.getMesi().intValue());
					lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
							lParModel.getGiorni().intValue());
				}

				// MODIFICA
				lSca03Mod.setCodTipoScadenzario("03"); // Vane ricerche
				Vector lScadenzarii = null;
				lScadeDao = new ScadenzarioSqlDAO(lConn);
				// cerca un scadenzario per id fascicolo e per tipo scadenzario
				lScadeDao.ricercaScadenzarioVerbaleArresto(lSca03Mod);
				lScadenzarii = new Vector(lScadeDao.getModels());

				lSca03Dao = new ScadenzarioDAO(lConn);

				if (lScadenzarii.size() == 0) {
					lSca03Mod.setFlagVisto("N");
					lSca03Mod.setDataFineScadenza(lFineScadenza);
					lSca03Mod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lSca03Mod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lSca03Mod.setDataInserimento(aEvento.getDataAggiornamento());
					// a6-rr-238
					lSca03Mod.setEveIdEvento(aEvento.getIdEvento());
					lSca03Mod.setCodStatoNotifica("NP");
					lSca03Dao.setDAOFromModel(lSca03Mod);
					// BigDecimal lKeyScad = null;
					/* lKeyScad = */lSca03Dao.insert();
				} else {
					ScadenzarioModel lScaModID = (ScadenzarioModel) lScadenzarii.get(0);
					lSca03Mod.setIdScadenzario(lScaModID.getIdScadenzario());
					lSca03Mod.setDataFineScadenza(lFineScadenza);
					lSca03Mod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lSca03Mod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lSca03Mod.setDataAggiornamento(aEvento.getDataAggiornamento());
					// a6-rr-238
					lSca03Mod.setCodStatoNotifica("NP");
					// AMBROSINO 30/06/2011
					lSca03Mod.setEveIdEvento(aEvento.getIdEvento());

					lSca03Dao.setDAOFromModelForUpdate(lSca03Mod);
					lSca03Dao.update();
				}
			}

			// END AMBROSINO 07-02-2

			// STATO PROCEDIMENTO
			lStatoDao = new StatoProcedimentoDAO(lConn);

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			lStatoProcMod.setData(lEveApp.getDataEmissione());
			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			AltraCausaModel lAltraCausa = new AltraCausaModel();
			if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) // Altra
																										// causa
			{
				lAltCauDao = new AltraCausaSqlDAO(lConn);
				lAltCauDao.ricercaAltraCausaByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();

				if (lAltraCausa != null && lAltraCausa.getIdAltraCausa() != null
						&& lAltraCausa.getDataDecorrenza() != null && lAltraCausa.getDataScadenza() != null) {
					lStatoProcMod.setCodStatoProcedimento("0121");
				} else {
					lStatoProcMod.setCodStatoProcedimento("0120");
				}
			} else {
				if (lCodPosizione.equals("49") || lCodPosizione.equals("04") || lCodPosizione.equals("02")
						|| lCodPosizione.equals("82") || lCodPosizione.equals("83")
						|| lCodPosizione.equals("84")) {
					lStatoProcMod.setCodStatoProcedimento("0121");
				} else if (lCodPosizione.equals("76") || lCodPosizione.equals("77")
						|| lCodPosizione.equals("78") || lCodPosizione.equals("79")
						|| lCodPosizione.equals("80") || lCodPosizione.equals("81")) {
					lStatoProcMod.setCodStatoProcedimento("0120");
				} else {
					lStatoProcMod.setCodStatoProcedimento("0120");
				}
			}

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// POSIZIONE GIURIDICA
			lPosGiuDao = new PosizioneGiuridicaDAO(lConn);
			String lPosizioInsAgg = "N";
			String lPosizione = null;
			if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) // Altra
																										// causa
			{
				// per le posizioni iniziali 76, 77, 78, 79, 80, 81(ALTRA_CAUSA)
				// la posizione finale(POSIZIONE_GIURIDICA) non deve essere 10 ma deve essere uguale a quella
				// iniziale 07
				// posizioni giuridiche finali dopo emissione OE(Ordini di Esecuzione/Scarcerazione
				// Sospensione Esecuzione ex art. 656 c.p.p. Revoca Sospensione Esecuzione ex art. 656
				// c.p.p.)
				// devono essere uguali a quelle iniziali
				// la vecchia posizione giuridica iniziale va chiusa valorizzando il campo DATA_FINE con data
				// sistema
				if (lAltraCausa != null && lAltraCausa.getIdAltraCausa() != null
						&& lAltraCausa.getCodTipoPosGiuridica() != null
						|| (lAltraCausa.getCodTipoPosGiuridica().equalsIgnoreCase("76")
								|| lAltraCausa.getCodTipoPosGiuridica().equalsIgnoreCase("77")
								|| lAltraCausa.getCodTipoPosGiuridica().equalsIgnoreCase("78")
								|| lAltraCausa.getCodTipoPosGiuridica().equalsIgnoreCase("79")
								|| lAltraCausa.getCodTipoPosGiuridica().equalsIgnoreCase("80")
								|| lAltraCausa.getCodTipoPosGiuridica().equalsIgnoreCase("81"))) {
					// lCodPosizione = "07";
					lPosizione = "07";
					lPosizioInsAgg = "S";
				} else if (lAltraCausa != null && lAltraCausa.getIdAltraCausa() != null
						&& lAltraCausa.getDataDecorrenza() != null && lAltraCausa.getDataScadenza() != null) {
					// lCodPosizione = "03";
					lPosizione = "03";
					lPosizioInsAgg = "S";
				} else {
					// lCodPosizione = "10";
					lPosizione = "10";
					lPosizioInsAgg = "S";
				}
			} else if (lCodPosizione.equals("49") || lCodPosizione.equals("04") || lCodPosizione.equals("02")
					|| lCodPosizione.equals("82") || lCodPosizione.equals("83")
					|| lCodPosizione.equals("84")) {
				// lCodPosizione = "03";
				lPosizione = "03";
				lPosizioInsAgg = "S";
			}

			if (lPosizioInsAgg.equals("S")) {
				// Chiude occorrenza
				lPosGiuDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
				lPosGiuDao.setDataFine(lEveApp.getDataEmissione());
				lPosGiuDao.setDataAggiornamento(DateUtils.getSysDate());
				lPosGiuDao.setCodUfficioAggiornamento(aFascicolo.getCodUfficioAggiornamento());
				lPosGiuDao.setCodOperatoreAggiornamento(aFascicolo.getCodOperatoreAggiornamento());

				lPosGiuDao.selByKey();
				lPosGiuDao.update();
				lPosGiuDao.stop();

				// ricerca della misura cautelare legata alla posizione giuriduca modificata per essere
				// inserita
				// insert la nuova misura cautelare legata alla nuova posizione giuridica
				// (la nuova misura cautelare e' uguale alla misura cautelare legata alla precedente posizione
				// giuridica modificata)
				// decisione presa con S.S./V.B. Settembre 2015
				MisuraCautelareModel misuraCautelare = new MisuraCautelareModel();
				misuraCautelare.setPosGiuIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
				lMisCauDao = new MisuraCautelareSqlDAO(lConn);
				lMisCauDao.ricercaMisuraCautelare(misuraCautelare);
				misuraCautelare = (MisuraCautelareModel) lMisCauDao.getModelByKey();

				// inserisco la nuova posizione giuridica
				if (lCodPosizione.equalsIgnoreCase("76") || lCodPosizione.equalsIgnoreCase("77")
						|| lCodPosizione.equalsIgnoreCase("78") || lCodPosizione.equalsIgnoreCase("79")
						|| lCodPosizione.equalsIgnoreCase("80") || lCodPosizione.equalsIgnoreCase("81")) {
					// inserisco la nuova posizione giuridica e' perfettamente uguale alla precedente, serve
					// per mantenere lo storico di diverse posizioni giuridiche
					// anche se hanno uguali valori(arresti domiciliari che si riferiscono a due ordinanze
					// diverse ==> cambia solo il periodo)
					lPosGiuDao.setCodMaschera(lPosMod.getCodMaschera());
					// AltraCausaSqlDAO lAltCauDao = null;
					// AltraCausaModel lAltraCausa = new AltraCausaModel();
					if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) // Altra
																												// causa
					{
						// inserisco la nuova ALTRA_CAUSA uguale alla precedente, serve per mantenere lo
						// storico di diverse posizioni giuridiche
						// anche se hanno uguali valori(arresti domiciliari che si riferiscono a due ordinanze
						// diverse ==> cambia solo il periodo)
						lAltCauDao = new AltraCausaSqlDAO(lConn);
						if (lPosMod != null && lPosMod.getAltCauIdAltraCausa() != null) {
							IAltraCausa altraCausa = SIEPLookupRemote.getAltraCausa();
							lAltraCausa = altraCausa
									.ExRicercaAltraCausaIstitutoByKey(lPosMod.getAltCauIdAltraCausa());
						}
						AltraCausaDAO lAltraCausaDao = null;
						lAltraCausaDao = new AltraCausaDAO(lConn);
						BigDecimal lKeyAltra = null;
						lAltraCausa.setDataInserimento(DateUtils.getSysDate());
						lAltraCausaDao.setDAOFromModel(lAltraCausa);
						lKeyAltra = lAltraCausaDao.insert();
						lPosGiuDao.setAltCauIdAltraCausa(lKeyAltra);
					}
				}

				// Inserisce la nuova occorrenza
				lPosGiuDao.setCodPosizioneGiuridica(lPosizione);
				lPosGiuDao.setCodPosizioneProcessuale("-");
				lPosGiuDao.setCodUfficioInserimento(aFascicolo.getCodUfficioInserimento());
				lPosGiuDao.setCodOperatoreInserimento(aFascicolo.getCodOperatoreInserimento());
				lPosGiuDao.setDataInserimento(DateUtils.getSysDate());
				lPosGiuDao.setDataInizio(lEveApp.getDataEmissione());
				lPosGiuDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPosGiuDao.setIdEventoRiferimento(aEvento.getIdEvento());
				lPosGiuDao.setLuogoEspiazione(lPosMod.getLuogoEspiazione());

				BigDecimal lKeyPosGiu = lPosGiuDao.insert();

				// insert la nuova misura cautelare legata alla nuova posizione giuridica
				// (la nuova misura cautelare e' uguale alla misura cautelare legata alla precedente posizione
				// giuridica modificata)
				// decisione presa con Salvatore/Vito Settembre 2015

				// Modifica del 23/02/2016 Nuova Infrastruttura INIZIO ******
				if (misuraCautelare != null) {
					// Modifica del 23/02/2016 Nuova Infrastruttura FINE ******
					misuraCautelare.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);
					Vector lVectMod = new Vector();
					lVectMod.add(misuraCautelare);
					IMisuraCautelare lCtrlMisCau = SIEPLookupRemote.getMisuraCautelareRemote();
					lCtrlMisCau.ExInserisciMisuraCautelare(lVectMod);
				}

				lPosGiuDao.insert();
				lPosGiuDao.stop();
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

			// aggiornamento FLAG_ALTRA_CAUSA nella tabella FASCICOLO_SIEP
			// POS_GIU=10 && COD_MASCHERA=L ==> FASCICOLO_SIEP.FLAG_ALTRA_CAUSA=NULL
			lFasDao = new FascicoloSiepDAO(lConn);
			if (lCodPosizione != null && lPosMod != null && lPosMod.getCodMaschera() != null
					&& lCodPosizione.equalsIgnoreCase("10")
					&& lPosMod.getCodMaschera().equalsIgnoreCase("L")) {
				aFascicolo.setFlagAltraCausa(null);
				lFasDao.setDAOFromModelForUpdate(aFascicolo);
				lFasDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());
				lFasDao.update();
			} else if (lCodPosizione != null && lCodPosizione.equalsIgnoreCase("07")
					&& (lCodPosizione.equalsIgnoreCase("76") || lCodPosizione.equalsIgnoreCase("77")
							|| lCodPosizione.equalsIgnoreCase("78") || lCodPosizione.equalsIgnoreCase("79")
							|| lCodPosizione.equalsIgnoreCase("80")
							|| lCodPosizione.equalsIgnoreCase("81"))) {
				// la nuova posizione giuridica e' perfettamente uguale alla precedente, serve per mantenere
				// lo
				// storico di diverse posizioni giuridiche
				// anche se hanno uguali valori(arresti domiciliari che si riferiscono a due ordinanze diverse
				// ==> cambia solo il periodo)
				// questa posizione conserva ALTRA_CAUSA
				aFascicolo.setFlagAltraCausa("S");
			} else {
				aFascicolo.setFlagAltraCausa(null);
			}
			lFasDao.setDAOFromModelForUpdate(aFascicolo);
			lFasDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());
			lFasDao.update();

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			rollback(lConn);
			rollback(lConnBlob);
			daoEx.printStackTrace();
			throw new F3BException("OrdineEsecuzioneController.ExUpdateRS : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException("OrdineEsecuzioneController.ExUpdateRS : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lPosGiuDao);
			cleanup(lAltCauDao);
			cleanup(lFasDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lSca03Dao);
			cleanup(lScadeDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lMisCauDao);
		}
		return lEveMod;
	}

	/**
	 * Effettua la validazione dell'ordine di Esecuzione per rideterminazione pena nel caso di Computi Misure
	 * Cautelari.
	 *
	 * n.b. se l'evento di computo non e' stato validato deve effettuare la contestuale validazione
	 * dell'evento di computo
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaOrdineEsecuzioneRidetPena(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

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
		NotificaEventoSqlDAO lNotEveDao = null;
		AnnotazioneManualeSqlDAO lAnnManSqlDao = null;
		AnnotazioneManualeDAO lAnnDao = null;
		FungibilitaDAO lFunDao = null;
		FungibilitaSqlDAO lFunSqlDao = null;
		MisuraAlternativaSqlDAO lMisAltSqlDao = null;
		MisuraAlternativaDAO lMisAltDao = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// =================================
			// Recupera l'evento da validare (ordine esecuzione)
			// =================================
			EventoModel lEveApp = new EventoModel();

			lEveDao = new EventoDAO(lConn);
			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				lEveApp.setEveIdEvento(lEveDao.getEveIdEvento());
			}
			lEveDao.stop();

			// ======================================
			// Cerca POSIZIONE_GIURIDICA corrente
			// ======================================
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// =============================================
			// Ricerca l'ultima pena residua per fascicolo
			// =============================================
			PenaResiduaModel lPenResMod = new PenaResiduaModel();

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			// ????
			// n.b. deve duplicare la pena
			// ======================================================================
			// Valido la pena residua se non e' gia' validata agganciandola all'evento
			// corrente
			// ======================================================================
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResMod.setEveIdEvento(aEvento.getIdEvento());
			lPenResMod.setFlagValidato("S");

			lPenResMod.setDataAggiornamento(aEvento.getDataAggiornamento());
			lPenResMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lPenResMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

			lPenResSqlDao.inserisciOModificaPenaResidua(lPenResMod);

			// Scadenzario -
			ScadenzarioModel lScaMod = new ScadenzarioModel();

			lScaMod.setFlagVisto("N");
			lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			lScaMod.setDataInserimento(aEvento.getDataAggiornamento());
			lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());

			// Stato Procedimento
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setData(lEveApp.getDataEmissione());
			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			int lIntPos = Integer.parseInt(lCodPosizione);

			if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) { // Attenzione!
																										// nel
																										// caso
																										// di
																										// det
																										// altra
																										// causa
																										// lo
																										// scadenzario
																										// (02)
																										// viene
																										// inserito
																										// solo
																										// se
																										// e'
																										// stato
																										// calcolato
																										// il
																										// fine
																										// pena
																										// questa
																										// causa
				lStatoProcMod.setCodStatoProcedimento("0007"); // Emesso Ordine di Esecuzione in Carcere il
				if (lPenResMod != null && lPenResMod.getDataInizio() != null) {
					lScaMod.setCodTipoScadenzario("02"); // Fine Pena
					lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio()); // !!! DataInizioScadenza e'
																				// not null sul DB
					if (lPenResMod.getDataFine() != null)
						lScaMod.setDataFineScadenza(lPenResMod.getDataFine());
				}
			} else {
				switch (lIntPos) {
				case 7:
				case 16:
				case 20:
				case 46:
				case 47:
				case 10: { // libero
					lStatoProcMod.setCodStatoProcedimento("0001"); // Emesso Ordine di Esecuzione con Arresto
																	// Il
					lScaMod.setCodTipoScadenzario("03"); // Vane Ricerche
					// a6-rr-238 - AMBROSINO Cerco NOTIFICA per prendere data trasmissione

					lNotEveDao = new NotificaEventoSqlDAO(lConn);
					lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
					Vector lNotifiche = new Vector(lNotEveDao.getModels());

					NotificaModel lNotifica = (NotificaModel) lNotifiche.get(0);

					lScaMod.setCodStatoNotifica("NP");
					lScaMod.setDataInizioScadenza(lNotifica.getDataInvio());
					// AMBROSINO 30/06/2011
					lScaMod.setEveIdEvento(aEvento.getIdEvento());
					break;
				}

				case 2:
				case 4: { // Arresti domiciliari
					lStatoProcMod.setCodStatoProcedimento("0009"); // Emesso Ordine di Esecuzione con
																	// Traduzione in Carcere il
					lScaMod.setCodTipoScadenzario("02"); // Fine Pena
					if (lPenResMod != null) {
						if (lPenResMod.getDataInizio() != null)
							lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio()); // !!!
																						// DataInizioScadenza
																						// e' not null sul DB
						if (lPenResMod.getDataFine() != null)
							lScaMod.setDataFineScadenza(lPenResMod.getDataFine());
					}

					break;
				}
				default: { // altri casi
					lStatoProcMod.setCodStatoProcedimento("0057"); // Emesso Ordine di Esecuzione Il
					lScaMod.setCodTipoScadenzario("02"); // Fine Pena
					if (lPenResMod != null) {
						if (lPenResMod.getDataInizio() != null)
							lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio()); // !!!
																						// DataInizioScadenza
																						// e' not null sul DB
						if (lPenResMod.getDataFine() != null)
							lScaMod.setDataFineScadenza(lPenResMod.getDataFine());
					}

					break;
				}
				}
			}

			// CANCELLA LO STATO PROCEDIMENTO
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			// INSERIMENTO STATO_PROCEDIMENTO
			for (int ins = 1; ins < 3; ins++) {
				lStatoProcMod.setProgressivo(new BigDecimal(ins));
				lStatoDao.setDAOFromModel(lStatoProcMod);
				lStatoDao.insert();
				lStatoDao.stop();

				if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")
						&& lPenResMod != null && lPenResMod.getDataInizio() != null) {
					lStatoProcMod.setCodStatoProcedimento("0008"); // con Inizio Esecuzione Dal
					lStatoProcMod.setData(lPenResMod.getDataInizio());
				} else {
					ins = 3;
				}
			}

			// INSERIMENTO SCADENZARIO
			// 15/05/2008 aggionto controllo. La scadenzario non va inserito se non sono
			// stati valorizzati i campi dichiarati not null sul DB
			if (lScaMod.getCodTipoScadenzario() != null && lScaMod.getDataInizioScadenza() != null) {

				// a6-rr-238 - AMBROSINO - 12/2010 e 08-02-2011 -se scadenzario 03 Controllo se gia' esiste e
				// metto data scadenza con parametro

				if (("03").equals(lScaMod.getCodTipoScadenzario())) {
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
						lSommaAnni = DateUtils.moveDateTo(lScaMod.getDataInizioScadenza(),
								java.util.Calendar.YEAR, lParModel.getAnni().intValue());
						lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
								lParModel.getMesi().intValue());
						lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
								lParModel.getGiorni().intValue());
					}

					Vector lScadenzarii = null;
					lScadeDao = new ScadenzarioSqlDAO(lConn);
					// cerca un scadenzario per id fascicolo e per tipo scadenzario
					lScadeDao.ricercaScadenzarioVerbaleArresto(lScaMod);
					lScadenzarii = new Vector(lScadeDao.getModels());
					lScaDao = new ScadenzarioDAO(lConn);
					if (lScadenzarii.size() == 0) {
						lScaMod.setFlagVisto("N");
						lScaMod.setCodStatoNotifica("NP");
						lScaMod.setEveIdEvento(aEvento.getIdEvento());
						lScaMod.setDataFineScadenza(lFineScadenza);
						lScaDao.setDAOFromModel(lScaMod);
						// BigDecimal lKeyScad = null;
						/* lKeyScad = */lScaDao.insert();
					} else {
						ScadenzarioModel lScaModID = (ScadenzarioModel) lScadenzarii.get(0);
						// lScaMod = lScaModID;
						lScaMod.setIdScadenzario(lScaModID.getIdScadenzario());
						lScaMod.setFasSieIdFascicoloSiep(lScaModID.getFasSieIdFascicoloSiep());
						lScaMod.setDataFineScadenza(lFineScadenza);
						// AMBROSINO 30/06/2011
						lScaMod.setCodStatoNotifica("NP");
						lScaMod.setEveIdEvento(aEvento.getIdEvento());

						lScaMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lScaMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
						lScaMod.setDataAggiornamento(aEvento.getDataAggiornamento());
						lScaDao.setDAOFromModelForUpdate(lScaMod);
						lScaDao.update();
					}

				} else { // Non e' Scadenzario 03
					lScaDao = new ScadenzarioDAO(lConn);
					lScaDao.setDAOFromModel(lScaMod);
					lScaDao.insert();

				}

			}

			// =============================
			// NOME PROVVEDIMENTO
			// =============================
			NomeProvvedimentoModel lNomProvMod = new NomeProvvedimentoModel();
			lNomProvDao = new NomeProvvedimentoDAO(lConn);

			if (lIntPos == 7 || lIntPos == 10 || lIntPos == 16 || lIntPos == 20 || lIntPos == 46
					|| lIntPos == 47) {
				lNomProvMod.setCodNomeProvvedimento("NP056");
			} else if (lIntPos == 1 || lIntPos == 3) {
				lNomProvMod.setCodNomeProvvedimento("NP057");
			} else if (lIntPos == 2 || lIntPos == 4) {
				lNomProvMod.setCodNomeProvvedimento("NP060");
			} else if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) {
				if (lPenResMod != null && lPenResMod.getDataInizio() != null) {
					lNomProvMod.setCodNomeProvvedimento("NP059");
				} else {
					lNomProvMod.setCodNomeProvvedimento("NP058");
				}
			} else {
				lNomProvMod.setCodNomeProvvedimento("NPAF0");
			}

			// INSERISCI NOME PROVVEDIMENTO
			lNomProvMod.setEveIdEvento(aEvento.getIdEvento());
			lNomProvDao.setDAOFromModel(lNomProvMod);
			lNomProvDao.insert();

			// ========================================================================
			// Aggiorna POSIZIONE_GIURIDICA solo se
			// 01 - Custodia Cautelare per Questa Causa in Regime di Detenzione
			// 02 - Custodia Cautelare per Questa Causa in Regime di Arresti Domiciliari
			// 07 - Libero (prima di OE)
			// ========================================================================
			lPosDao = new PosizioneGiuridicaDAO(lConn);

			if (lCodPosizione.equals("07") || lCodPosizione.equals("01") || lCodPosizione.equals("02")) {
				// Aggiorna la posizione giuridica corrente
				lPosDao.setDataFine(DateUtils.getSysDate());
				lPosDao.setDataAggiornamento(DateUtils.getSysDate());

				lPosDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());

				lPosDao.update();
				lPosDao.stop();

				// inserisco la nuova posizione giuridica
				if (lCodPosizione.equals("07")) {
					lPosDao.setCodPosizioneGiuridica("10");
				} else if (lCodPosizione.equals("01")) {
					lPosDao.setCodPosizioneGiuridica("03");
				} else if (lCodPosizione.equals("02")) {
					lPosDao.setCodPosizioneGiuridica("04");
				}

				lPosDao.setCodPosizioneProcessuale("-");
				lPosDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setDataInserimento(DateUtils.getSysDate());
				lPosDao.setDataInizio(DateUtils.getSysDate());
				lPosDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPosDao.setIdEventoRiferimento(aEvento.getIdEvento());

				lPosDao.insert();
				lPosDao.stop();
			}

			// ========================================================================
			// Verifico se l'evento di computo collegato all'OE risulta validato, in
			// caso contrario lo valido insieme alle annotazioni e alla pena residua.
			// ========================================================================
			EventoModel lEveComputo = null;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lEveApp.getEveIdEvento() = " + lEveApp.getEveIdEvento());
			if (lEveApp.getEveIdEvento() != null) {
				lEveDao.stop();
				lEveDao.setIdEvento(lEveApp.getEveIdEvento());
				lEveDao.selByKey();
				lEveComputo = (EventoModel) lEveDao.getModelByKey();
				lEveDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Evento di Computo = " + lEveComputo);
			}

			AnnotazioneManualeModel lAnnMod = null;
			lAnnDao = new AnnotazioneManualeDAO(lConn);
			lAnnManSqlDao = new AnnotazioneManualeSqlDAO(lConn);

			if (lEveComputo != null && lEveComputo.getIdEvento() != null
					&& (lEveComputo.getFlagDocumentoRegistrato() == null
							|| (lEveComputo.getFlagDocumentoRegistrato() != null
									&& lEveComputo.getFlagDocumentoRegistrato().equals("N")))) {
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lEveDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lEveDao.setDataAggiornamento(aEvento.getDataAggiornamento());

				lEveDao.setIdEvento(lEveComputo.getIdEvento()); // condizione di Update in chiave
				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				// Ricerco le Annotazioni collegate al Computo e le valido
				lAnnManSqlDao.ricercaAnnotazioneManualeByIdEvento(lEveComputo.getIdEvento());
				Vector lAnnotazioni = new Vector(lAnnManSqlDao.getModels());

				if (lAnnotazioni != null) {
					Iterator iter = lAnnotazioni.iterator();
					while (iter.hasNext()) {
						lAnnMod = (AnnotazioneManualeModel) iter.next();
						lAnnMod.setFlagValidato("S");
						lAnnMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
						lAnnMod.setDataInserimento(aEvento.getDataAggiornamento());
						lAnnMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

						// ==================================================================
						// Aggiorno la riga della tabella misure_cautelare_bdmc
						// legata all'annotazione in esame
						// ==================================================================

						MisuraCautelareBdmcModel lMisCautBdmc = new MisuraCautelareBdmcModel();
						lMisCautBdmc.setIdAnnotazioneManuale(lAnnMod.getIdAnnotazioneManuale());
						lMisCautBdmc.setFlagStato("I");
						IMisuraCautelareBdmc lCtrMisCauBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
						Vector misCautBdmc = lCtrMisCauBdmc.ExRicercaMisuraCautelareBdmc(lMisCautBdmc);

						if (misCautBdmc != null && misCautBdmc.size() == 1) {
							MisuraCautelareBdmcModel lMisBdmc = (MisuraCautelareBdmcModel) misCautBdmc.get(0);
							lMisBdmc.setEveIdEvento(lAnnMod.getEveIdEvento());
							lMisBdmc.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
							lMisBdmc.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
							lMisBdmc.setDataAggiornamento(aEvento.getDataAggiornamento());
							lMisBdmc.setFlagStato("V");
							lMisBdmc.setStatoTrasmissioneVal("N");
							IMisuraCautelareBdmc lCtrMisBdmc = SIEPLookupRemote
									.getMisuraCautelareBdmcRemote();
							lCtrMisBdmc.ExModificaMisuraCautelareBdmcNoCommit(lConn, lMisBdmc, aEvento);

						} else {
							if (misCautBdmc != null && misCautBdmc.size() != 0)
								throw new F3BException(
										"E' stata riscontrata un'incongruenza dei dati legati alla tabella Annotazione_Manuale e tabella per tramissione dati a BDMC");
						}

						lAnnDao.setDAOFromModelForUpdate(lAnnMod);
						lAnnDao.update();
						lAnnDao.stop();
					}
				}
			}

			// ========================================================================
			// Se il computo ha generato fungibilita' la valido
			// (verificare questa situazione sul calcolo della pena)
			// Attenzione!! lAnnMod e' l'ultima annotazione di quelle del ciclo for
			// precedente
			// ========================================================================
			lFunDao = new FungibilitaDAO(lConn);
			lFunSqlDao = new FungibilitaSqlDAO(lConn);
			FungibilitaModel lFunMod = new FungibilitaModel();
			if (lAnnMod != null && lAnnMod.getFunIdFungibilita() != null) {
				lFunSqlDao.ricercaFungibilitaByKey(lAnnMod.getFunIdFungibilita());
				lFunMod = (FungibilitaModel) lFunSqlDao.getModelByKey();
				if (lFunMod != null) {
					lFunDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lFunDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lFunDao.setDataAggiornamento(aEvento.getDataAggiornamento());
					lFunDao.setFlagValidato("S");

					lFunDao.setCondizioneUpdate(lFunMod.getIdFungibilita());
					lFunDao.update();
					lFunDao.stop();
				}
			}

			/*
			 * ricerco l'ultima occorrenza di annotazioni manuali con FlagAppProvvisoria = "A" e = "R" per
			 * cancellare sia pena residua che fungibilita'
			 */
			AnnotazioneManualeModel lAnnoMod = new AnnotazioneManualeModel();
			lAnnManSqlDao.ricercaAnnotazioneManualeFlagAppProvByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lAnnoMod = (AnnotazioneManualeModel) lAnnManSqlDao.getModelByKey();

			if (lAnnoMod != null) {
				if (lAnnoMod.getPenResIdPenaResidua() != null) {
					lPenResDao.setCondizioneUpdate(lAnnoMod.getPenResIdPenaResidua());
					lPenResDao.delete();
					lPenResDao.stop();
				}
				if (lAnnoMod.getFunIdFungibilita() != null) {
					lFunDao.setCondizioneUpdate(lAnnoMod.getFunIdFungibilita());
					lFunDao.delete();
					lFunDao.stop();
				}
			}

			// ============================
			// SE IN MISURA ALTERNATIVA
			// ============================
			if (lPosMod.isMisAlt()) {
				lMisAltSqlDao = new MisuraAlternativaSqlDAO(lConn);
				lMisAltDao = new MisuraAlternativaDAO(lConn);
				// lMisAltSqlDao.ricercaMisuraAlternativaByFascicoloOrdinanza(aFascicolo.getIdFascicoloSiep());
				lMisAltSqlDao.ricercaMisuraAlternativaCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());
				MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisAltSqlDao.getModelByKey();

				if (lMisModel != null) {
					// Se la DATA_FINE_MISURA e' successiva alla
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
			rollback(lConn);
			rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException(
					"OrdineEsecuzioneController.ExUpdateValidaOrdineEsecuzioneRidetPena : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException(
					"OrdineEsecuzioneController.ExUpdateValidaOrdineEsecuzioneRidetPena : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lScaDao);
			cleanup(lScadeDao);
			cleanup(lNomProvDao);
			cleanup(lAnnManSqlDao);
			cleanup(lAnnDao);
			cleanup(lFunDao);
			cleanup(lFunSqlDao);
			cleanup(lMisAltSqlDao);
			cleanup(lMisAltDao);
			cleanup(lNotEveDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 *
	 * @param aKey
	 * @param aTipEve
	 * @param aTipProv
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoOERDNonRegistratoByFascicoloSiep(BigDecimal aKey, String aTipEve,
			String aTipProv) throws F3BException {

		Connection lConn = null;
		EventoModel lEvento = new EventoModel();
		OrdineEsecuzioneSqlDao lOrdEveDao = null;

		try {
			lConn = getDBConnection();
			lOrdEveDao = new OrdineEsecuzioneSqlDao(lConn);
			lOrdEveDao.ricercaEventoOERPNonRegistratoByFascicoloSiep(aKey, aTipEve, aTipProv);
			lEvento = (EventoModel) lOrdEveDao.getModelByKey();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoOERDNonRegistratoByFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lOrdEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	/**
	 *
	 * @param aKey
	 * @param aTipEve
	 * @param aTipProv
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoNonRegistratoByFascicoloSiep(BigDecimal aKey, String aTipEve,
			String aTipProv) throws F3BException {

		Connection lConn = null;
		EventoModel lEvento = new EventoModel();
		OrdineEsecuzioneSqlDao lOrdEveDao = null;

		try {
			lConn = getDBConnection();
			lOrdEveDao = new OrdineEsecuzioneSqlDao(lConn);
			lOrdEveDao.ricercaEventoNonRegistratoByFascicoloSiep(aKey, aTipEve, aTipProv);
			lEvento = (EventoModel) lOrdEveDao.getModelByKey();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoMANonRegistratoByFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lOrdEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	/**
	 * ExEsisteOrdineEsecuzioneByFascicoloSiep
	 *
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public boolean ExEsisteOrdineEsecuzioneByFascicoloSiep(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;
		OrdineEsecuzioneSqlDao lOrdEveDao = null;
		boolean lFlagOrdineEsecuzione = false;

		try {
			lConn = getDBConnection();
			lOrdEveDao = new OrdineEsecuzioneSqlDao(lConn);
			lOrdEveDao.ricercaOrdineEsecuzionePeneConcorrentiByIdFascicolo(aIdFascicolo);

			lOrdEveDao.start();
			if (lOrdEveDao.next())
				lFlagOrdineEsecuzione = true;

			lOrdEveDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExEsisteOrdineEsecuzioneByFascicoloSiep: " + daoEx);
		} finally {
			cleanup(lOrdEveDao);
			cleanup(lConn);
		}

		return lFlagOrdineEsecuzione;
	}

	/**
	 * ExInserisciOModificaMANotificaRevocaLS
	 *
	 * @param aEvento
	 * @param aPenaResidua
	 * @param aMisura
	 * @param aMotEve
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaMANotificaRevocaLS(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, MisuraAlternativaModel aMisura, MotivoEventoModel aMotEve)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaDAO lPenaResDao = null;
		MisuraAlternativaDAO lMisDAO = null;
		MotivoEventoDAO lMotEveDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);
			lMotEveDao = new MotivoEventoDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);

			lSqlDAO.ricercaEventoMANonRegistratoMAByFascicoloSiep(
					aEvento.getEvento().getFasSieIdFascicoloSiep(), aEvento.getEvento().getEveIdEvento());

			EventoModel lEveModel = (EventoModel) lSqlDAO.getModelByKey();
			BigDecimal lKeyEvento = null;
			if (lEveModel == null) // Se non presente lo inserisce
			{
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
				aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				lEveDao.setDAOFromModel(aEvento.getEvento());

				lKeyEvento = lEveDao.insert();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

			} else // Se presente lo aggiorna
			{
				lKeyEvento = lEveModel.getIdEvento();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

				EventoModel lEveMod = aEvento.getEvento();
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

				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();

			}

			// motivo evento
			aMotEve.setEveIdEvento(lKeyEvento);
			lMotEveDao.setDAOFromModel(aMotEve);
			lMotEveDao.insert();
			lMotEveDao.stop();

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

			// Inserisce Pena Residua
			lPenaResDao = new PenaResiduaDAO(lConn);
			if (aPenaResidua != null && aPenaResidua.getIdPenaResidua() != null
					&& aPenaResidua.getDataFine() != null) {
				lPenaResDao.setIdPenaResidua(aPenaResidua.getIdPenaResidua());
				lPenaResDao.setDataFine(aPenaResidua.getDataFine());

				lPenaResDao.selByKey();
				lPenaResDao.update();
				lPenaResDao.stop();
			}

			// Aggiorna Misura alternativa
			lMisDAO = new MisuraAlternativaDAO(lConn);
			if (aMisura != null) {
				lMisDAO.setDAOFromModelForUpdate(aMisura);
				lMisDAO.update();
				lMisDAO.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException(
					"OrdineEsecuzioneController.ExInserisciOModificaMANotificaRevocaLS: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException(
					"OrdineEsecuzioneController.ExInserisciOModificaMANotificaRevocaLS: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lPenaResDao);
			cleanup(lMisDAO);
			cleanup(lMotEveDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Effettua l'inserimento dell'ordine di esecuzione e delle notifiche associate. Duplica la pena
	 * rideterminata sul provvedimento di computo.
	 *
	 * @since 4.0
	 */
	public EventoModel ExInserisciOERidetPenaAltro(EventoNotificaModel aEveNotModel) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDAO = null;

		EventoModel lEventoOE = aEveNotModel.getEvento();

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Determino il protocollo e inserisco l'evento
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco Ordine di esecuzione...");

			lEveSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgProtocollo = lEveSqlDAO.getProgressivo(lEventoOE);
			lEventoOE.setProgrProtocollo(new BigDecimal(lProgProtocollo.intValue() + 1));

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(lEventoOE);
			BigDecimal lIdEventoOE = lEveDao.insert();
			lEventoOE.setIdEvento(lIdEventoOE);

			// ========================================================================
			// Inserisco le Notifiche
			// ========================================================================
			BigDecimal lKeyAutorita = null;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inizio inserimento notifiche ");

			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			int count = 0;
			while (count < aEveNotModel.getNotifiche().length) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Notifica[" + count + "] = " + aEveNotModel.getNotifiche()[count]);

				if (aEveNotModel.getNotifiche()[count] != null) {
					if (aEveNotModel.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEveNotModel.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) { // Autorita' non presente, la inserisco
							lAutDao.setDAOFromModel(aEveNotModel.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEveNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else { // Autorita' gia' presente aggiorno solo l'id
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEveNotModel.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					// Inserisco il record Notifica
					aEveNotModel.getNotifiche()[count].setEveIdEvento(lIdEventoOE);

					lNotDao.setDAOFromModel(aEveNotModel.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// ========================================================================
			// Recupero la pena residua da eseguire dal Provvedimento di Computo che
			// ha rideterminato la pena e la duplico agganciandola all'OE
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la pena residua associata al computo");
			BigDecimal lIdEveComputo = lEventoOE.getEveIdEvento();

			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);

			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(lIdEveComputo);
			PenaResiduaModel lPenaResidua = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Da duplicare: " + lPenaResidua);

			// Ripulisco i campi non necessari
			lPenaResidua.setIdPenaResidua(null);
			lPenaResidua.setEveIdEvento(lIdEventoOE);
			lPenaResidua.setFlagValidato("N"); // quella che sto duplicando potrebbe essere validata

			lPenaResidua.setCodOperatoreInserimento(lEventoOE.getCodOperatoreInserimento());
			lPenaResidua.setCodUfficioInserimento(lEventoOE.getCodUfficioInserimento());
			lPenaResidua.setDataInserimento(lEventoOE.getDataInserimento());

			lPenaResidua.setCodOperatoreAggiornamento(null);
			lPenaResidua.setCodUfficioAggiornamento(null);
			lPenaResidua.setDataAggiornamento(null);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena da inserire: " + lPenaResidua);

			// Inserisco la pena
			lPenResDAO = new PenaResiduaDAO(lConn);
			lPenResDAO.setDAOFromModel(lPenaResidua);
			BigDecimal lIdPenaResNew = lPenResDAO.insert();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Id Pena Res Inserita: " + lIdPenaResNew);

			// rollback(lConn);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException", daoEx);
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOERidetPenaAltro: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception", ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOERidetPenaAltro: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDAO);

			cleanup(lConn);
		}

		return lEventoOE;

	}

	/**
	 * Metodo per la validazione dell'ordine di esecuzione per Rideterminazione Pena Altro. Effettua la
	 * contestuale validazione del decreto di computo se non gia' validato.
	 *
	 * Viene validato: - evento corrente - pena residua - fungibilita' (?)
	 *
	 * - Aggiorna Stato procedimento - Aggiorna Scadenzario (vane ricerche)
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 * @since 4.0
	 */
	public EventoModel ExUpdateValidaOERidetPenaAltro(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		PenaResiduaDAO lPenResDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		ScadenzarioSqlDAO lScadSqlDao = null;
		ScadenzarioDAO lScaDao = null;
		NotificaEventoSqlDAO lNotEveSqlDao = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveModOE = null;

		try {
			lConn = getDBTransaction();

			// ===================================================
			// Recupera l'evento da validare (ordine esecuzione)
			// ===================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero l'evento da validare (ordine esecuzione)");

			lEveDao = new EventoDAO(lConn);
			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();
			lEveModOE = (EventoModel) lEveDao.getModelByKey();
			lEveDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lEveModOE = " + lEveModOE);

			// ========================================================================
			// Recupero il provvedimneto di computo e verifico se devo validarlo
			// contestualmente all'Ordine di esecuzione.
			// n.b. potrebbe essere gia' stato validato
			// ========================================================================
			if (lEveModOE.getEveIdEvento() != null) {
				EventoModel lEveComputo = null;
				lEveDao = new EventoDAO(lConn);
				lEveDao.setIdEvento(lEveModOE.getEveIdEvento());
				lEveDao.selByKey();
				lEveComputo = (EventoModel) lEveDao.getModelByKey();
				lEveDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Evento di Computo = " + lEveComputo);

				if (lEveComputo.getFlagDocumentoRegistrato() == null
						|| (lEveComputo.getFlagDocumentoRegistrato() != null
								&& lEveComputo.getFlagDocumentoRegistrato().equals("N"))) { // Procedo alla
																							// validazione del
																							// Provvedimento
																							// di Computo
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Procedo alla validazione del Provvedimento di Computo");
					lEveComputo.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveComputo.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveComputo.setDataAggiornamento(aEvento.getDataAggiornamento());
					lEveComputo.setFlagDocumentoRegistrato(aEvento.getFlagDocumentoRegistrato());

					IAnnotazioneManuale lCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
					lCtrl.ExValidaRideterminazionePenaAltro(lEveComputo, aFascicolo, lConn);
				} else
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Evento di Computo gia' validato");
			}

			// ========================================================================
			// Valido la Pena Residua.
			// La pena da eseguire e' quella associata al provvedimento di computo che
			// ha rideterminato la pena duplicata in fase di inserimento dell'OE
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Valido Pena Residua su OE");
			lPenResDao = new PenaResiduaDAO(lConn);

			lPenResDao.setFlagValidato("S");

			lPenResDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

			lPenResDao.setCondizioneByIdEvento(aEvento.getIdEvento());

			lPenResDao.update();
			lPenResDao.stop();

			// ========================================================================
			// Aggiorno Stato Procedimento in funzione della posizione giuridica di
			// partenza
			// ========================================================================
			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();
			String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			String lCodStatoProcedimento = null;

			if (lPosMod.isLibero()) { // Libero
				lCodStatoProcedimento = "0001"; // Emesso Ordine di Esecuzione con Arresto Il
			} else if (lCodPosizione.equals("02") || lCodPosizione.equals("04")) { // Arresti Domiciliari
				lCodStatoProcedimento = "0009"; // Emesso Ordine di Esecuzione con Traduzione in Carcere il
			} else if (aFascicolo.getFlagAltraCausa() != null && aFascicolo.getFlagAltraCausa().equals("S")) // Altra
																												// causa
			{ // Detenuto Altra Causa
				lCodStatoProcedimento = "0007"; // Emesso Ordine di Esecuzione in Carcere il
			} else { // Tutti gli altri casi
				lCodStatoProcedimento = "0057"; // Emesso Ordine di Esecuzione Il
			}

			// Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			// Inserisce lo stato procedimento
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setCodStatoProcedimento(lCodStatoProcedimento);
			lStatoProcMod.setData(lEveModOE.getDataEmissione());
			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			// lStatoProcMod.setEveIdEvento(aValore);

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// Se agli arresti domiciliari aggiungo un secondo record
			if (lCodPosizione.equals("02") || lCodPosizione.equals("04")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Arresti Domiciliari: recupero la PR");
				lPenResDao.setCondizioneByIdEvento(lEveModOE.getIdEvento());
				PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
				lPenResDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lPenResMod = " + lPenResMod);

				lStatoProcMod.setProgressivo(new BigDecimal(2));
				lStatoProcMod.setCodStatoProcedimento("0010"); // 0010 - Pena in Esecuzione Fino al
				lStatoProcMod.setData(lPenResMod.getDataFine());

				// Inserisco
				lStatoDao.setDAOFromModel(lStatoProcMod);
				lStatoDao.insert();
				lStatoDao.stop();
			}

			// ========================================================================
			// Aggiorno lo scadenzario VANE RICERCHE se presente altrimenti ne viene
			// inserito uno nuovo
			// Solo se posizione giuridica LIBERO (questa causa)
			// Per le posizioni Detenuto AC e arresti domiciliari da verificare se
			// inserire/modificare lo scadenzario FINE PENA
			// ========================================================================
			if (lPosMod.isLibero() && (aFascicolo.getFlagAltraCausa() != null
					&& aFascicolo.getFlagAltraCausa().equals("N"))) {
				ParametroModel lParMod = new ParametroModel();

				lParMod.setNomeParametro("VANE RICERCHE");
				lParMod.setCodUfficioValidita(aEvento.getCodUfficioAggiornamento());

				Vector lVectPar = null;
				IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
				lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

				// Recupero le notifiche per la data di trasmissione
				lNotEveSqlDao = new NotificaEventoSqlDAO(lConn);
				lNotEveSqlDao.ricercaNotificaByEvento(aEvento.getIdEvento());
				Vector lNotifiche = new Vector(lNotEveSqlDao.getModels());

				ScadenzarioModel lScaMod = new ScadenzarioModel();
				lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				NotificaModel lNotifica = (NotificaModel) lNotifiche.get(0);
				lScaMod.setDataInizioScadenza(lNotifica.getDataInvio()); // data trasmissione

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

				// Recupera gli scadenzari Vane Ricerche del fascicolo
				lScaMod.setCodTipoScadenzario("03"); // Vane ricerche

				Vector lScadenzarii = null;
				lScadSqlDao = new ScadenzarioSqlDAO(lConn);
				// cerca un scadenzario per id fascicolo e per tipo scadenzario
				lScadSqlDao.ricercaScadenzarioVerbaleArresto(lScaMod);
				lScadenzarii = new Vector(lScadSqlDao.getModels());

				lScaDao = new ScadenzarioDAO(lConn);

				if (lScadenzarii.size() == 0) { // Scadenzario assente, lo inserisco
					lScaMod.setFlagVisto("N");
					lScaMod.setDataFineScadenza(lFineScadenza);

					lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lScaMod.setDataInserimento(aEvento.getDataAggiornamento());
					// a6-rr-238
					lScaMod.setEveIdEvento(aEvento.getIdEvento());
					lScaMod.setCodStatoNotifica("NP");
					lScaDao.setDAOFromModel(lScaMod);
					// BigDecimal lKeyScad = null;
					/* lKeyScad = */lScaDao.insert();
				} else { // Aggiorna gli scadenzari se gia' presenti
					ScadenzarioModel lScaModID = (ScadenzarioModel) lScadenzarii.get(0);
					lScaMod.setIdScadenzario(lScaModID.getIdScadenzario());
					lScaMod.setDataFineScadenza(lFineScadenza);

					lScaMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaMod.setDataAggiornamento(aEvento.getDataAggiornamento());
					// a6-rr-238
					lScaMod.setCodStatoNotifica("NP");
					// AMBROSINO 30/06/2011
					lScaMod.setEveIdEvento(aEvento.getIdEvento());

					lScaDao.setDAOFromModelForUpdate(lScaMod);
					lScaDao.update();
				}
			}

			// rollback(lConn);
			commit(lConn);

			// ========================================================================

			// ====================
			// Update del Blob
			// ====================
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
			throw new F3BException("OrdineEsecuzioneController.ExUpdateValidaOERidetPenaAltro : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException(
					"OrdineEsecuzioneController.ExUpdateValidaOrdineEsecuzioneRidetPena : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lPenResDao);
			cleanup(lPosSqlDao);
			cleanup(lStatoDao);
			cleanup(lScadSqlDao);
			cleanup(lScaDao);
			cleanup(lNotEveSqlDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveModOE;
	}

	/**
	*
	*/
	public EventoNotificaModel ExInserisciVariazioneDecorrenzaScadenzaQC(EventoNotificaModel aEveNotMod,
			PenaResiduaModel aPenaResidua) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaDAO lPenaResDao = null;

		EventoNotificaModel lEveNotRet = new EventoNotificaModel(aEveNotMod);

		try {
			lConn = getDBTransaction();

			// ===========================================================
			// Inserisco Comunicazione Variazione decorrenza scadenza
			// ===========================================================
			// Setto l'anno e il progressivo...
			lEveSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEveSqlDAO.getProgressivo(aEveNotMod.getEvento());
			aEveNotMod.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEveNotMod.getEvento());

			BigDecimal lKeyEvento = lEveDao.insert();
			lEveNotRet.getEvento().setIdEvento(lKeyEvento);

			// ============================================
			// Inserisco le notifiche
			// ============================================
			BigDecimal lKeyAutorita = null;
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);

			int count = 0;
			while (count < aEveNotMod.getNotifiche().length) {
				if (aEveNotMod.getNotifiche()[count] != null) {
					if (aEveNotMod.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEveNotMod.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEveNotMod.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEveNotMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEveNotMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					aEveNotMod.getNotifiche()[count].setEveIdEvento(lKeyEvento);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Insert Notifica: " + aEveNotMod.getNotifiche()[count]);

					lNotDao.setDAOFromModel(aEveNotMod.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// ===============================================
			// Inserimento delle eventuali note aggiuntive.
			// ===============================================
			lCampoNotaDao = new CampoNotaDAO(lConn);
			if (aEveNotMod.getCampoNote() != null) {
				count = 0;
				while (count < aEveNotMod.getCampoNote().length) {
					aEveNotMod.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					aEveNotMod.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEveNotMod.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}

			// ===============================================
			// Inserisco la nuova Pena Residua
			// ===============================================
			lPenaResDao = new PenaResiduaDAO(lConn);

			aPenaResidua.setEveIdEvento(lKeyEvento);

			lPenaResDao.setDAOFromModel(aPenaResidua);
			lPenaResDao.insert();
			lPenaResDao.stop();

			commit(lConn);
			// rollback(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"OrdineEsecuzioneController.ExInserisciVariazioneDecorrenzaScadenzaQC : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(
					"OrdineEsecuzioneController.ExInserisciVariazioneDecorrenzaScadenzaQC : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lCampoNotaDao);
			cleanup(lPenaResDao);

			cleanup(lConn);
		}

		return lEveNotRet;
	}

	/**
	 * Effettua la validazione della comunicazione variazione decorrenza scadenza pena per condannati in
	 * espiazione questa causa
	 */
	public EventoModel ExUpdateValidaVariazioneDecScadQC(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		ScadenzarioSqlDAO lScadSqlDao = null;
		ScadenzarioDAO lScaDao = null;
		NotificaEventoSqlDAO lNotEveSqlDao = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = null;

		try {
			lConn = getDBTransaction();

			// ===================================================
			// Recupera l'evento da validare (Comunicazione)
			// ===================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupera l'evento da validare (Comunicazione)");

			lEveDao = new EventoDAO(lConn);
			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();
			lEveMod = (EventoModel) lEveDao.getModelByKey();
			lEveDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lEveMod = " + lEveMod);

			// ========================================================================
			// Valido la Nuova Pena Residua.
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Valido Pena Residua su OE");
			lPenResDao = new PenaResiduaDAO(lConn);

			lPenResDao.setFlagValidato("S");

			lPenResDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

			lPenResDao.setCondizioneByIdEvento(aEvento.getIdEvento());

			lPenResDao.update();
			lPenResDao.stop();

			// ========================================================================
			// Aggiorno Stato Procedimento NO!!!!
			// ========================================================================

			// ========================================================================
			// Aggiorno lo scadenzario FINE_PENA
			// Solo se posizione giuridica LIBERO (questa causa)
			// Per le posizioni Detenuto AC e arresti domiciliari da verificare se
			// inserire/modificare lo scadenzario FINE PENA
			// ========================================================================
			lScadSqlDao = new ScadenzarioSqlDAO(lConn);
			lScadSqlDao.ricercaScadenzarioFinePenaVaneRicerche(aFascicolo.getIdFascicoloSiep());
			ScadenzarioModel lScaMod = (ScadenzarioModel) lScadSqlDao.getModelByKey();

			PenaResiduaModel lPenResMod = null;
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEvento.getIdEvento());
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lPenResMod= " + lPenResMod);
			if (lScaMod != null) {
				// aggiorna scadenzario
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("aggiorno scadenzario");
				lScaDao = new ScadenzarioDAO(lConn);
				lScaDao.setDataFineScadenza(lPenResMod.getDataFine());
				lScaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lScaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lScaDao.setDataAggiornamento(aEvento.getDataAggiornamento());
				lScaDao.setFlagVisto("N");

				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
				lScaDao.update();
				lScaDao.stop();
			}

			commit(lConn);

			// ========================================================================

			// ====================
			// Update del Blob
			// ====================
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
			throw new F3BException("OrdineEsecuzioneController.ExUpdateValidaVariazioneDecScadQC : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException("OrdineEsecuzioneController.ExUpdateValidaVariazioneDecScadQC : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lStatoDao);
			cleanup(lScadSqlDao);
			cleanup(lScaDao);
			cleanup(lNotEveSqlDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	// metodo revoca legge alfano
	public EventoModel ExUpdateValidaRED(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		// 30-05-2016 - MEV_YY da rilasciare dopo Primo Collaudo per V.10
		EventoSqlDAO lEveSqlDao = null;
		// 30-05-2016 - END MEV_YY
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosGiuDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		AltraCausaSqlDAO lAltCauDao = null;
		FascicoloSiepDAO lFasDao = null;
		ScadenzarioDAO lSca03Dao = null;
		ScadenzarioSqlDAO lScadeDao = null;
		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);

			// * Cerca L'EVENTO *
			EventoModel lEveApp = new EventoModel();
			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				// 30-05-2016 - MEV_YY da rilasciare dopo Primo Collaudo per V.10
				lEveApp.setEveIdEvento(lEveDao.getEveIdEvento());
				lEveApp.setCodMotivo(lEveDao.getCodMotivo());
				// 30-05-2016 - END MEV_YY
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

			// Aggiorna PENA_RESIDUA
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			PenaResiduaModel lPenResMod = new PenaResiduaModel();

			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			if (lPenResMod.getEveIdEvento() == null) {
				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
				lPenResDao.setEveIdEvento(aEvento.getIdEvento());
				lPenResDao.setFlagValidato("S");
				lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPenResDao.setDataAggiornamento(DateUtils.getSysDate());
				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				lPenResDao.setDAOFromModel(lPenResMod);
				lPenResDao.setFlagValidato("S");
				lPenResDao.setEveIdEvento(aEvento.getIdEvento());
				lPenResDao.setDataInserimento(DateUtils.getSysDate());
				lPenResDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lPenResDao.insert();
				lPenResDao.stop();
			}

			// SCADENZARIO 03
			// AMBROSINO 07-02-2011 - Se NON ESISTE la penaresidua INSERISCE SCADENZARIO 03

			if (lPenResMod.getDataFine() == null) {
				ParametroModel lParMod = new ParametroModel();

				lParMod.setNomeParametro("VANE RICERCHE");
				lParMod.setCodUfficioValidita(aEvento.getCodUfficioAggiornamento());

				Vector lVectPar = null;
				IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
				lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

				ScadenzarioModel lSca03Mod = new ScadenzarioModel();

				lSca03Mod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				NotificaModel lNotifica = (NotificaModel) lNotifiche.get(0);
				lSca03Mod.setDataInizioScadenza(lNotifica.getDataInvio());

				Iterator lIter = lVectPar.iterator();
				Date lSommaAnni = null;
				Date lSommaMesi = null;
				Date lFineScadenza = null;

				if (lIter.hasNext()) {
					ParametroModel lParModel = (ParametroModel) lIter.next();
					lSommaAnni = DateUtils.moveDateTo(lSca03Mod.getDataInizioScadenza(),
							java.util.Calendar.YEAR, lParModel.getAnni().intValue());
					lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH,
							lParModel.getMesi().intValue());
					lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
							lParModel.getGiorni().intValue());
				}

				// MODIFICA
				lSca03Mod.setCodTipoScadenzario("03"); // Vane ricerche
				Vector lScadenzarii = null;
				lScadeDao = new ScadenzarioSqlDAO(lConn);
				// cerca un scadenzario per id fascicolo e per tipo scadenzario
				lScadeDao.ricercaScadenzarioVerbaleArresto(lSca03Mod);
				lScadenzarii = new Vector(lScadeDao.getModels());

				lSca03Dao = new ScadenzarioDAO(lConn);

				if (lScadenzarii.size() == 0) {
					lSca03Mod.setFlagVisto("N");
					lSca03Mod.setDataFineScadenza(lFineScadenza);
					lSca03Mod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lSca03Mod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lSca03Mod.setDataInserimento(aEvento.getDataAggiornamento());
					// a6-rr-238
					lSca03Mod.setEveIdEvento(aEvento.getIdEvento());
					lSca03Mod.setCodStatoNotifica("NP");
					lSca03Dao.setDAOFromModel(lSca03Mod);
					// BigDecimal lKeyScad = null;
					/* lKeyScad = */lSca03Dao.insert();
				} else {
					ScadenzarioModel lScaModID = (ScadenzarioModel) lScadenzarii.get(0);
					lSca03Mod.setIdScadenzario(lScaModID.getIdScadenzario());
					lSca03Mod.setDataFineScadenza(lFineScadenza);
					lSca03Mod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lSca03Mod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lSca03Mod.setDataAggiornamento(aEvento.getDataAggiornamento());
					// a6-rr-238
					lSca03Mod.setCodStatoNotifica("NP");
					// AMBROSINO 30/06/2011
					lSca03Mod.setEveIdEvento(aEvento.getIdEvento());

					lSca03Dao.setDAOFromModelForUpdate(lSca03Mod);
					lSca03Dao.update();
				}
			}

			// END AMBROSINO 07-02-2

			// STATO PROCEDIMENTO
			lStatoDao = new StatoProcedimentoDAO(lConn);

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			lStatoProcMod.setData(lEveApp.getDataEmissione());
			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			if (lCodPosizione.equals("07") || lCodPosizione.equals("10") || lCodPosizione.equals("16")
					|| lCodPosizione.equals("17") || lCodPosizione.equals("46") || lCodPosizione.equals("47")) // Libero
			{
				lStatoProcMod.setCodStatoProcedimento("0407");
			} else if (lCodPosizione.equals("01") || lCodPosizione.equals("03")
					|| lCodPosizione.equals("73")) {
				lStatoProcMod.setCodStatoProcedimento("0408");
			} else if (lCodPosizione.equals("02")
					// TODO INIZIO codice sovrascritto con intervento 14 della MEV 29 (fatta da Diego
					// Forletta)
					|| lCodPosizione.equals("53") // 25/03/2015 d.f. MEV 29 Punto 14
					|| lCodPosizione.equals("70") || lCodPosizione.equals("71") || lCodPosizione.equals("72")
					|| lCodPosizione.equals("85") || lCodPosizione.equals("86")
					|| lCodPosizione.equals("87")) {
				lStatoProcMod.setCodStatoProcedimento("0409");
			}
			// TODO FINE codice sovrascritto con intervento 14 della MEV 29 (fatta da Diego Forletta)

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// POSIZIONE GIURIDICA
			lPosGiuDao = new PosizioneGiuridicaDAO(lConn);
			String lPosizioInsAgg = "N";
			// TODO INIZIO codice sovrascritto con intervento 14 della MEV 29 (fatta da Diego Forletta)
			if (lCodPosizione.equals("01") || lCodPosizione.equals("02") || lCodPosizione.equals("03")
					|| lCodPosizione.equals("53") // 25/03/2015 d.f. MEV 29
					|| lCodPosizione.equals("70") || lCodPosizione.equals("71") || lCodPosizione.equals("72")
					|| lCodPosizione.equals("73") || lCodPosizione.equals("85") || lCodPosizione.equals("86")
					|| lCodPosizione.equals("87")) // da verificare la 02
			{
				lCodPosizione = "03";
				lPosizioInsAgg = "S";
			}
			// TODO FINE codice sovrascritto con intervento 14 della MEV 29 (fatta da Diego Forletta)

			// per il libero rimane la posizione iniziale
			if (lPosizioInsAgg.equals("S")) {
				// Chiude occorrenza
				lPosGiuDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
				lPosGiuDao.setDataFine(lEveApp.getDataEmissione());
				lPosGiuDao.setDataAggiornamento(DateUtils.getSysDate());
				lPosGiuDao.setCodUfficioAggiornamento(aFascicolo.getCodUfficioAggiornamento());
				lPosGiuDao.setCodOperatoreAggiornamento(aFascicolo.getCodOperatoreAggiornamento());

				lPosGiuDao.selByKey();
				lPosGiuDao.update();
				lPosGiuDao.stop();

				// Inserisce la nuova occorrenza
				lPosGiuDao.setCodPosizioneGiuridica(lCodPosizione);
				lPosGiuDao.setCodPosizioneProcessuale("-");
				lPosGiuDao.setCodUfficioInserimento(aFascicolo.getCodUfficioInserimento());
				lPosGiuDao.setCodOperatoreInserimento(aFascicolo.getCodOperatoreInserimento());
				lPosGiuDao.setDataInserimento(DateUtils.getSysDate());
				lPosGiuDao.setDataInizio(lEveApp.getDataEmissione());
				lPosGiuDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
				lPosGiuDao.setIdEventoRiferimento(aEvento.getIdEvento());

				lPosGiuDao.insert();
				lPosGiuDao.stop();
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

			// 30-05-2016 - MEV_YY da rilasciare dopo Primo Collaudo per V.10
			// -- EVENTO -- (Evento 03 Ordinanza )
			EventoModel lEveOrd = null;

			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(lEveApp.getEveIdEvento());

			lEveOrd = (EventoModel) lEveSqlDao.getModelByKey();
			if (lEveOrd != null && lEveOrd.getIdEvento() != null && lEveOrd.getCodMotivo() != null) {
				if (lEveOrd.getCodMotivo().compareTo("9000") == 0
						|| lEveOrd.getCodMotivo().compareTo("9001") == 0
						|| lEveOrd.getCodMotivo().compareTo("9002") == 0) {
					lEveOrd.setFlagDocumentoRegistrato("S");
					lEveOrd.setDataAggiornamento(aEvento.getDataAggiornamento());
					lEveOrd.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveOrd.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());

					lEveDaoBlob.setDAOFromModelForUpdateBlob(lEveOrd);

					lEveDaoBlob.selCondizioneUpdate(lEveOrd.getIdEvento());
					lEveDaoBlob.update();
					lEveDaoBlob.stop();
				}
			}
			// 30-05-2016 - END MEV_YY

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("OrdineEsecuzioneController.ExUpdateValidaRED", daoEx);
			rollback(lConn);
			rollback(lConnBlob);
			daoEx.printStackTrace();
			throw new F3BException("OrdineEsecuzioneController.ExUpdateValidaRED : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("OrdineEsecuzioneController.ExUpdateValidaRED", ex);
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException("OrdineEsecuzioneController.ExUpdateValidaRED : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lNotEveDao);
			cleanup(lPosGiuDao);
			cleanup(lAltCauDao);
			cleanup(lFasDao);
			// 30-05-2016 - MEV_YY da rilasciare dopo Primo Collaudo per V.10
			cleanup(lEveSqlDao);
			// 30-05-2016 - END MEV_YY
			cleanup(lSca03Dao);
			cleanup(lScadeDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}
		return lEveMod;
	}

	/**
	 * ExInserisciOModificaMANotificaRevocaLAlf
	 *
	 * @param aEvento
	 * @param aPenaResidua
	 * @param aMisura
	 * @param aMotEve
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaMANotificaRevocaLAlf(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, MisuraAlternativaModel aMisura, MotivoEventoModel aMotEve)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaDAO lPenaResDao = null;
		MisuraAlternativaDAO lMisDAO = null;
		MotivoEventoDAO lMotEveDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);
			lMotEveDao = new MotivoEventoDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);

			lSqlDAO.ricercaEventoMANonRegistratoMAByFascicoloSiep(
					aEvento.getEvento().getFasSieIdFascicoloSiep(), aEvento.getEvento().getEveIdEvento());

			EventoModel lEveModel = (EventoModel) lSqlDAO.getModelByKey();
			BigDecimal lKeyEvento = null;
			if (lEveModel == null) // Se non presente lo inserisce
			{
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
				aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				lEveDao.setDAOFromModel(aEvento.getEvento());

				lKeyEvento = lEveDao.insert();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

			} else // Se presente lo aggiorna
			{
				lKeyEvento = lEveModel.getIdEvento();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

				EventoModel lEveMod = aEvento.getEvento();
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

				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();

			}

			// motivo evento
			aMotEve.setEveIdEvento(lKeyEvento);
			lMotEveDao.setDAOFromModel(aMotEve);
			lMotEveDao.insert();
			lMotEveDao.stop();

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

			// Inserisce Pena Residua
			lPenaResDao = new PenaResiduaDAO(lConn);
			if (aPenaResidua != null && aPenaResidua.getIdPenaResidua() != null
					&& aPenaResidua.getDataFine() != null) {
				lPenaResDao.setIdPenaResidua(aPenaResidua.getIdPenaResidua());
				lPenaResDao.setDataFine(aPenaResidua.getDataFine());

				lPenaResDao.selByKey();
				lPenaResDao.update();
				lPenaResDao.stop();
			}

			// Aggiorna Misura alternativa
			lMisDAO = new MisuraAlternativaDAO(lConn);
			if (aMisura != null) {
				lMisDAO.setDAOFromModelForUpdate(aMisura);
				lMisDAO.update();
				lMisDAO.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException(
					"OrdineEsecuzioneController.ExInserisciOModificaMANotificaRevocaLAlf: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException(
					"OrdineEsecuzioneController.ExInserisciOModificaMANotificaRevocaLAlf: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lPenaResDao);
			cleanup(lMisDAO);
			cleanup(lMotEveDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * METODO DI INSERIMENTO "REVOCA" LEGGE ALFANO
	 *
	 * @param aEvento
	 * @param aPenaResidua
	 * @param aMotEve
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaRevocaLAlfNotifica(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, MotivoEventoModel aMotEve) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaDAO lPenaResDao = null;
		MotivoEventoDAO lMotEveDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);
			lMotEveDao = new MotivoEventoDAO(lConn);

			// Cerca L'OE se presente (By IdFascicolo)
			lSqlDAO = new EventoSqlDAO(lConn);
			// lSqlDAO.ricercaOrdineEsecuzioneRevocaLSNonRegistratoByFascicoloSiep(aEvento.getEvento().getFasSieIdFascicoloSiep());
			lSqlDAO.ricercaOrdineEsecuzioneRevocaLAlfNonRegistratoByFascicoloSiep(
					aEvento.getEvento().getFasSieIdFascicoloSiep());
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

				lEveDao.setIdEvento(lKeyEvento);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				// *********** Cancella le NOTIFICHE associate all' EVENTO ******************
				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();
				// ************************************************************************
			}

			// motivo evento
			aMotEve.setEveIdEvento(lKeyEvento);
			lMotEveDao.setDAOFromModel(aMotEve);
			lMotEveDao.insert();
			lMotEveDao.stop();

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

			// Aggiorna Pena Residua (che deve essere presente)
			lPenaResDao = new PenaResiduaDAO(lConn);

			lPenaResDao.setIdPenaResidua(aPenaResidua.getIdPenaResidua());

			lPenaResDao.setDataFine(aPenaResidua.getDataFine());

			lPenaResDao.selByKey();
			lPenaResDao.update();
			lPenaResDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException(
					"OrdineEsecuzioneController.ExInserisciOModificaRevocaLAlfNotifica: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException(
					"OrdineEsecuzioneController.ExInserisciOModificaRevocaLAlfNotifica: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lPenaResDao);
			cleanup(lMotEveDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * METODO DI INSERIMENTO LEGGE ALFANO 199/2010
	 *
	 * @param aEvento
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaLAlfNotifica(EventoNotificaModel aEvento,
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
			lSqlDAO.ricercaOrdineEsecuzioneNonRegistratoByFascicoloSiep(
					aEvento.getEvento().getFasSieIdFascicoloSiep());
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

				lEveDao.setIdEvento(lKeyEvento);

				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				// *********** Cancella le NOTIFICHE associate all' EVENTO ******************
				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();
				// ************************************************************************
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
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOModificaLAlfNotifica: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOModificaLAlfNotifica: " + ex);
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
	 * METODO DI INSERIMENTO DECRETO LEGGE 78/2013
	 *
	 * @param aEvento
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaLegge78del2013(EventoNotificaModel aEvento,
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

			if (aEvento.getEvento().getCodMotivo().equals("1024"))
				lSqlDAO.ricercaOrdineEsecuzioneNonRegistratoByFascicoloSiep(
						aEvento.getEvento().getFasSieIdFascicoloSiep());
			else
				lSqlDAO.ricercaComunicazioneNonRegistratoByFascicoloSiep(
						aEvento.getEvento().getFasSieIdFascicoloSiep());

			EventoModel lEvePresente = (EventoModel) lSqlDAO.getModelByKey();

			BigDecimal lKeyEvento = null;
			if (lEvePresente == null) {// Se non presente lo inserisce
				// Setto l'anno e il progressivo...
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

				// *********** Cancella le NOTIFICHE associate all' EVENTO ******************
				lNotDao.setCondizioneEvento(lKeyEvento);

				lNotDao.delete();
				lNotDao.stop();
				// ************************************************************************
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
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOModificaLAlfNotifica: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOModificaLAlfNotifica: " + ex);
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
	} // Chiudo ExInserisciOModificaLegge78del2013

	// AMBROS 01--2014 - Misure di Sicurezza SIEP
	// 09/02/2015 Modificato il criterio di individuazione delle MIS.SIC. attraverso il dato
	// "ProvvedimentoEventoTenoreFascicoloSiusModel.TipoEsitoMisura".
	// 06-03-2015 cambiati gli ultimi 2 parametri (da bigDecimal a list, e da Model a Vector)
	public EventoNotificaModel ExInserisciOENotificaMisSic(EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, List aMisureOld, Vector aMisureNewSius) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaDAO lPenaResDao = null;
		MisuraSicurezzaSqlDAO lMisSqlDao = null;
		MisuraSicurezzaDAO lMisDao = null;
		MisuraSicurezzaDAO lMisDao1 = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);

			// Setto l'anno e il progressivo...
			lSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
			aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			lEveDao.setDAOFromModel(aEvento.getEvento());

			BigDecimal lKeyEvento = lEveDao.insert();
			lEveRet.getEvento().setIdEvento(lKeyEvento);

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
			// Aggiorna Pena Residua
			if (aPenaResidua != null && aPenaResidua.getIdPenaResidua() != null) {
				lPenaResDao = new PenaResiduaDAO(lConn);

				lPenaResDao.setIdPenaResidua(aPenaResidua.getIdPenaResidua());

				lPenaResDao.setDataFine(aPenaResidua.getDataFine());

				lPenaResDao.selByKey();
				lPenaResDao.update();
				lPenaResDao.stop();
			}

			// IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
			BigDecimal lKey = null;
			// MISURE di SICUREZZA
			// -----> Trovo la vecchia misura
			for (int k = 0; k < aMisureOld.size(); k++) {
				MisuraSicurezzaModel lMisMod = (MisuraSicurezzaModel) aMisureOld.get(k);
				BigDecimal NumFasSiep = lMisMod.getFasSieIdFascicoloSiep();
				// ---------> Inserisco la Nuova Misura lato SIEP
				if (aMisureNewSius.size() > 0) {
					if (k < aMisureNewSius.size()) {
						MisuraSicurezzaModel lMisModSius = (MisuraSicurezzaModel) aMisureNewSius.get(k);
						MisuraSicurezzaModel lMisModnew = new MisuraSicurezzaModel();
						lMisDao = new MisuraSicurezzaDAO(lConn);

						lMisModnew.setCodNatura(lMisModSius.getCodNatura());
						lMisModnew.setCodTipo(lMisModSius.getCodTipo());

						if (lMisModSius != null && lMisModSius.getNumGiorni() != null)
							lMisModnew.setNumGiorni(lMisModSius.getNumGiorni());
						else
							lMisModnew.setNumGiorni(new BigDecimal(0));

						if (lMisModSius != null && lMisModSius.getNumMesi() != null)
							lMisModnew.setNumMesi(lMisModSius.getNumMesi());
						else
							lMisModnew.setNumMesi(new BigDecimal(0));

						if (lMisModSius != null && lMisModSius.getNumAnni() != null)
							lMisModnew.setNumAnni(lMisModSius.getNumAnni());
						else
							lMisModnew.setNumAnni(new BigDecimal(0));

						if (lMisModSius != null && lMisModSius.getAnnoReg38() != null)
							lMisModnew.setAnnoReg38(lMisModSius.getAnnoReg38());

						lMisModnew
								.setCodOperatoreInserimento(aEvento.getEvento().getCodOperatoreInserimento());
						lMisModnew.setCodUfficioInserimento(aEvento.getEvento().getCodUfficioInserimento());
						lMisModnew.setDataInserimento(DateUtils.getSysDate());

						// proviamo a legare la nuova misura al provvedimento SIEP corrente e al fascicolo
						// SIEP
						// lMisModnew.setMisIdMisuraSicurezza(lMisMod.getIdMisuraSicurezza());
						lMisModnew.setFasSieIdFascicoloSiep(NumFasSiep);
						lMisModnew.setEveIdEvento(lKeyEvento);

						if (lMisModSius != null && lMisModSius.getAnnoReg38() != null) {
							lMisSqlDao = new MisuraSicurezzaSqlDAO(lConn);
							BigDecimal lProgr38 = lMisSqlDao.getMaxNumReg38(lMisModnew.getAnnoReg38(),
									lMisModnew.getCodUfficioInserimento());
							lMisModnew.setNumReg38(new BigDecimal(lProgr38.intValue() + 1));
						}

						lMisDao.setDAOFromModel(lMisModnew);
						lKey = lMisDao.insert();
						lMisModnew.setIdMisuraSicurezza(lKey);
					}
				}

				lMisDao1 = new MisuraSicurezzaDAO(lConn);
				lMisMod.setMisIdMisuraSicurezza(lKey);
				lMisDao1.setDAOFromModelForUpdate(lMisMod);
				lMisDao1.update();
				lMisDao1.stop();
			}
			// commit Finale
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOENotificaMisSic: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + ex, ex);
			rollback(lConn);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOENotificaMisSic: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lPenaResDao);
			cleanup(lMisDao);
			cleanup(lMisSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lMisDao1);

			cleanup(lConn);
		}

		return lEveRet;
	} // Chiude ExInserisciOENotificaMisSis

	/*
	 * ISSUE MEV : aggiunto metodo di inserimento OE differimento con notifiche 
	 * Numero MEV : 39 
	 * Autore : Gioggi 
	 * Data : 07/mar/2017 
	 * Branch : MEV_39
	 */
	public EventoNotificaModel ExInserisciOLDifferimentoConNotifiche(EventoNotificaModel enm,
			PenaResiduaModel prm, MisuraAlternativaModel mam) throws F3BException {

		Connection connection = null;

		// insert
		EventoDAO eventoDAO = null;
		NotificaDAO notificaDAO = null;
		ScadenzarioDAO scadenzarioDAO = null;
		// altri insert
		AutoritaEsternaDAO autoritaEsternaDAO = null;
		CampoNotaDAO campoNotaDAO = null;
		MisuraAlternativaDAO misAltDAO = null;
		// altri update
		PenaResiduaDAO penaResiduaDAO = null;
		// recupero dati
		EventoSqlDAO eventoSqlDAO = null;
		EventoNotificaModel enmRet = new EventoNotificaModel(enm);

		try {
			connection = getDBTransaction();

			// INS + UPD
			eventoDAO = new EventoDAO(connection);
			autoritaEsternaDAO = new AutoritaEsternaDAO(connection);
			notificaDAO = new NotificaDAO(connection);
			campoNotaDAO = new CampoNotaDAO(connection);
			scadenzarioDAO = new ScadenzarioDAO(connection);
			// depositoOrdinanzaPcDAO = new DepositoOrdinanzaPcDAO(connection);

			// SEARCH + UPD
			// depositoOrdinanzaPcSqlDAO = new DepositoOrdinanzaPcSqlDAO(connection);

			// Setto l'anno ed il progressivo
			eventoSqlDAO = new EventoSqlDAO(connection);
			BigDecimal lProgr = eventoSqlDAO.getProgressivo(enm.getEvento());
			enm.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			eventoDAO.setDAOFromModel(enm.getEvento());

			BigDecimal lKeyEvento = eventoDAO.insert();
			enmRet.getEvento().setIdEvento(lKeyEvento);

			BigDecimal lKeyAutorita = null;
			int count = 0;

			while (count < enm.getNotifiche().length) {
				if (enm.getNotifiche()[count] != null) {
					if (enm.getNotifiche()[count].getAutoritaEsterna() != null) {
						autoritaEsternaDAO
								.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) autoritaEsternaDAO.getModelByKey();
						if (lAutMod == null) {
							autoritaEsternaDAO
									.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = autoritaEsternaDAO.insert();
							enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					enm.getNotifiche()[count].setEveIdEvento(lKeyEvento);
					notificaDAO.setDAOFromModel(enm.getNotifiche()[count]);
					notificaDAO.insert();
				}
				count++;
			}

			// Inserimento delle eventuali note aggiuntive.
			if (enm.getCampoNote() != null) {
				count = 0;
				while (count < enm.getCampoNote().length) {
					enm.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					enm.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					campoNotaDAO.setDAOFromModel(enm.getCampoNote()[count]);
					campoNotaDAO.insert();
					count++;
				}
			}

			// aggiorno deposito ordinanza
			// depositoOrdinanzaPcSqlDAO.ricercaDepositoOrdinanzaPcByIdEveGenerato(idEveFascSius);
			// depositoOrdinanzaPcModel = (DepositoOrdinanzaPcModel)
			// depositoOrdinanzaPcSqlDAO.getModelByKey();
			// if (depositoOrdinanzaPcModel != null) {
			// depositoOrdinanzaPcDAO.setIdDepositoOrdinanzaPc(depositoOrdinanzaPcModel
			// .getIdDepositoOrdinanzaPc());
			// depositoOrdinanzaPcDAO.setDataInizioPeriodo(dataDifferimento);
			// depositoOrdinanzaPcDAO.setDataFineMisura(dataFineRinvio);
			// depositoOrdinanzaPcDAO.setCodOperatoreAggiornamento(enm.getEvento()
			// .getCodOperatoreInserimento());
			// depositoOrdinanzaPcDAO.setDataAggiornamento(DateUtils.getSysDate());
			// depositoOrdinanzaPcDAO.setCodUfficioAggiornamento(enm.getEvento().getCodUfficioInserimento());
			// depositoOrdinanzaPcDAO.selByKey();
			// depositoOrdinanzaPcDAO.update();
			// }
			// INSERT INTO MISURA_ALTERNATIVA
			misAltDAO = new MisuraAlternativaDAO(connection);
			mam.setEveIdEvento(lKeyEvento);
			misAltDAO.setDAOFromModel(mam);
			misAltDAO.insert();

			// Aggiorna Pena Residua
			if (prm != null && prm.getIdPenaResidua() != null) {
				penaResiduaDAO = new PenaResiduaDAO(connection);
				penaResiduaDAO.setIdPenaResidua(prm.getIdPenaResidua());
				penaResiduaDAO.setDataFine(prm.getDataFine());
				penaResiduaDAO.setCodOperatoreAggiornamento(enm.getEvento().getCodOperatoreAggiornamento());
				penaResiduaDAO.setCodUfficioAggiornamento(enm.getEvento().getCodUfficioAggiornamento());
				penaResiduaDAO.setDataAggiornamento(DateUtils.getSysDate());
				penaResiduaDAO.selByKey();
				penaResiduaDAO.update();
			}

			// scadenziario SIEP
			ScadenzarioModel sm = new ScadenzarioModel();
			sm.setCodTipoScadenzario("21");
			// prelevo i valori dla model della MA
			sm.setDataInizioScadenza(mam.getDataInizioMisura());
			if (mam.getDataFineMisura() != null) {
				sm.setDataFineScadenza(mam.getDataFineMisura());
			} else {
				Date dataFineMis = mam.getDataInizioMisura();
				Calendar data = Calendar.getInstance();
				data.setTime(dataFineMis);
				BigDecimal ggM = mam.getNumGiorniMisura();
				if (ggM != null)
					data.add(Calendar.DAY_OF_YEAR, ggM.intValue());

				BigDecimal mmM = mam.getNumMesiMisura();
				if (mmM != null)
					data.add(Calendar.MONTH, mmM.intValue());

				BigDecimal aaM = mam.getNumAnniMisura();
				if (aaM != null)
					data.add(Calendar.YEAR, aaM.intValue());

				sm.setDataFineScadenza(data.getTime());
			}
			sm.setFlagVisto("N");
			sm.setDataVisto(null);
			sm.setFasSieIdFascicoloSiep(enm.getEvento().getFasSieIdFascicoloSiep());
			sm.setEveIdEvento(enmRet.getEvento().getIdEvento());
			sm.setCodOperatoreInserimento(enm.getEvento().getCodOperatoreInserimento());
			sm.setCodUfficioInserimento(enm.getEvento().getCodUfficioInserimento());
			sm.setDataInserimento(DateUtils.getSysDate());
			sm.setCodOperatoreAggiornamento(null);
			sm.setCodUfficioAggiornamento(null);
			sm.setDataAggiornamento(null);
			sm.setNotIdNotifica(null);
			sm.setCodStatoNotifica(null);
			sm.setIdFascicoloSiepOrigine(null);
			// se valorizzata la durata allora determino la scadenza comunicazione
			if (!Utils.isNullObj(mam.getNumAnniMisura()) || !Utils.isNullObj(mam.getNumMesiMisura())
					|| !Utils.isNullObj(mam.getNumGiorniMisura())) {
				Date dsc = mam.getDataInizioMisura();
				if (!Utils.isNullObj(mam.getNumAnniMisura()))
					DateUtils.moveDateTo(dsc, Calendar.YEAR, mam.getNumAnniMisura().intValue());
				if (!Utils.isNullObj(mam.getNumMesiMisura()))
					DateUtils.moveDateTo(dsc, Calendar.MONTH, mam.getNumMesiMisura().intValue());
				if (!Utils.isNullObj(mam.getNumGiorniMisura()))
					DateUtils.moveDateTo(dsc, Calendar.DAY_OF_MONTH, mam.getNumGiorniMisura().intValue());
				sm.setDataScadenzaComunicazione(dsc);
			}

			scadenzarioDAO.setDAOFromModel(sm);
			scadenzarioDAO.insert();

			// commit della connessione
			commit(connection);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			rollback(connection);
			throw new F3BException(
					"OrdineEsecuzioneController.ExInserisciOLDifferimentoConNotifiche: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			ex.printStackTrace();
			rollback(connection);
			throw new F3BException("OrdineEsecuzioneController.ExInserisciOLDifferimentoConNotifiche: " + ex);
		} finally {
			cleanup(campoNotaDAO);
			cleanup(eventoDAO);
			cleanup(notificaDAO);
			cleanup(autoritaEsternaDAO);
			cleanup(eventoSqlDAO);
			cleanup(penaResiduaDAO);
			cleanup(scadenzarioDAO);
			// cleanup(depositoOrdinanzaPcSqlDAO);
			// cleanup(depositoOrdinanzaPcDAO);
			cleanup(misAltDAO);

			cleanup(connection);
		}

		// valore di ritorno
		return enmRet;
	}

	public EventoNotificaModel ExModificaOLDifferimentoConNotifiche(EventoNotificaModel enm,
			PenaResiduaModel prm, MisuraAlternativaModel mam) throws F3BException {

		Connection connection = null;

		// update
		EventoDAO eventoDAO = null;
		NotificaDAO notificaDAO = null;
		ScadenzarioDAO scadenzarioDAO = null;
		AutoritaEsternaDAO autoritaEsternaDAO = null;
		CampoNotaDAO campoNotaDAO = null;
		// DepositoOrdinanzaPcDAO depositoOrdinanzaPcDAO = null;
		PenaResiduaDAO penaResiduaDAO = null;
		MisuraAlternativaDAO misAltDAO = null;
		// recupero dati
		// DepositoOrdinanzaPcSqlDAO depositoOrdinanzaPcSqlDAO = null;
		ScadenzarioSqlDAO scadenzarioSqlDAO = null;

		// MODEL
		// DepositoOrdinanzaPcModel depositoOrdinanzaPcModel = null;
		EventoModel eventoModel = null;
		EventoNotificaModel eventoNotificaModel = new EventoNotificaModel();
		ScadenzarioModel sm = null;

		try {
			connection = getDBTransaction();

			eventoDAO = new EventoDAO(connection);
			eventoModel = new EventoModel(enm.getEvento());
			eventoDAO.setDAOFromModelForUpdate(eventoModel);
			eventoDAO.setIdEvento(eventoModel.getIdEvento());
			eventoDAO.selByKey();
			eventoDAO.update();
			eventoDAO.stop();

			BigDecimal idEvento = eventoModel.getIdEvento();
			eventoNotificaModel.getEvento().setIdEvento(idEvento);

			// *********** Cancello le NOTIFICHE associate all'EVENTO ******************
			notificaDAO = new NotificaDAO(connection);
			notificaDAO.setCondizioneEvento(idEvento);
			notificaDAO.delete();

			BigDecimal lKeyAutorita = null;
			int count = 0;

			while (count < enm.getNotifiche().length) {
				if (enm.getNotifiche()[count] != null) {
					if (enm.getNotifiche()[count].getAutoritaEsterna() != null) {
						autoritaEsternaDAO = new AutoritaEsternaDAO(connection);
						autoritaEsternaDAO
								.setRicercaByAutSede(enm.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) autoritaEsternaDAO.getModelByKey();
						if (lAutMod == null) {
							autoritaEsternaDAO
									.setDAOFromModel(enm.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = autoritaEsternaDAO.insert();
							enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					notificaDAO = new NotificaDAO(connection);
					enm.getNotifiche()[count].setEveIdEvento(idEvento);
					notificaDAO.setDAOFromModel(enm.getNotifiche()[count]);
					notificaDAO.insert();
				}
				count++;
			}

			autoritaEsternaDAO = new AutoritaEsternaDAO(connection);
			campoNotaDAO = new CampoNotaDAO(connection);
			scadenzarioDAO = new ScadenzarioDAO(connection);
			// depositoOrdinanzaPcDAO = new DepositoOrdinanzaPcDAO(connection);
			// depositoOrdinanzaPcSqlDAO = new DepositoOrdinanzaPcSqlDAO(connection);

			// Inserimento delle eventuali note aggiuntive.
			if (enm.getCampoNote() != null) {
				count = 0;
				while (count < enm.getCampoNote().length) {
					if (enm.getCampoNote()[count].getDescr().compareTo("") != 0) {
						campoNotaDAO
								.setCodOperatoreAggiornamento(enm.getEvento().getCodOperatoreAggiornamento());
						campoNotaDAO.setCodUfficioAggiornamento(enm.getEvento().getCodUfficioAggiornamento());
						campoNotaDAO.setDataAggiornamento(DateUtils.getSysDate());
						campoNotaDAO.setDescr(enm.getCampoNote()[count].getDescr());
						campoNotaDAO.setDAOFromModelForUpdate(enm.getCampoNote()[count]);
						campoNotaDAO.update();
						count++;
					}
				}
			}

			// aggiorno deposito ordinanza
			// depositoOrdinanzaPcSqlDAO.ricercaDepositoOrdinanzaPcByIdEveGenerato(idEveFascSius);
			// depositoOrdinanzaPcModel = (DepositoOrdinanzaPcModel)
			// depositoOrdinanzaPcSqlDAO.getModelByKey();
			// if (depositoOrdinanzaPcModel != null) {
			// depositoOrdinanzaPcDAO.setIdDepositoOrdinanzaPc(depositoOrdinanzaPcModel
			// .getIdDepositoOrdinanzaPc());
			// depositoOrdinanzaPcDAO.setDataInizioPeriodo(dataDifferimento);
			// depositoOrdinanzaPcDAO.setDataFineMisura(dataFineRinvio);
			// depositoOrdinanzaPcDAO.setCodOperatoreAggiornamento(enm.getEvento()
			// .getCodOperatoreAggiornamento());
			// depositoOrdinanzaPcDAO.setDataAggiornamento(DateUtils.getSysDate());
			// depositoOrdinanzaPcDAO.setCodUfficioAggiornamento(enm.getEvento()
			// .getCodUfficioAggiornamento());
			// depositoOrdinanzaPcDAO.selByKey();
			// depositoOrdinanzaPcDAO.update();
			// }
			// UPDATE INTO MISURA_ALTERNATIVA
			misAltDAO = new MisuraAlternativaDAO(connection);
			misAltDAO.setDAOFromModelForUpdate(mam);
			misAltDAO.update();

			// Aggiorna Pena Residua
			if (prm != null && prm.getIdPenaResidua() != null) {
				penaResiduaDAO = new PenaResiduaDAO(connection);
				penaResiduaDAO.setIdPenaResidua(prm.getIdPenaResidua());
				penaResiduaDAO.setDataFine(prm.getDataFine());
				penaResiduaDAO.setCodOperatoreAggiornamento(enm.getEvento().getCodOperatoreAggiornamento());
				penaResiduaDAO.setCodUfficioAggiornamento(enm.getEvento().getCodUfficioAggiornamento());
				penaResiduaDAO.setDataAggiornamento(DateUtils.getSysDate());
				penaResiduaDAO.selByKey();
				penaResiduaDAO.update();
			}

			// scadenziario SIEP
			scadenzarioSqlDAO = new ScadenzarioSqlDAO(connection);
			scadenzarioSqlDAO
					.ricercaScadenzarioByIdFascicoloCorrente(enm.getEvento().getFasSieIdFascicoloSiep());
			sm = (ScadenzarioModel) scadenzarioSqlDAO.getModelByKey();
			if (sm != null) {
				scadenzarioDAO.setDAOFromModelForUpdate(sm);
				sm.setDataInizioScadenza(mam.getDataInizioMisura());
				sm.setDataFineScadenza(mam.getDataFineMisura());
				scadenzarioDAO.setCodOperatoreAggiornamento(enm.getEvento().getCodOperatoreAggiornamento());
				scadenzarioDAO.setDataAggiornamento(DateUtils.getSysDate());
				scadenzarioDAO.setCodUfficioAggiornamento(enm.getEvento().getCodUfficioAggiornamento());
				scadenzarioDAO.update();
			}

			// commit della connessione
			commit(connection);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			rollback(connection);
			throw new F3BException(
					"OrdineEsecuzioneController.ExModificaOLDifferimentoConNotifiche: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			ex.printStackTrace();
			rollback(connection);
			throw new F3BException("OrdineEsecuzioneController.ExModificaOLDifferimentoConNotifiche: " + ex);
		} finally {
			cleanup(campoNotaDAO);
			cleanup(eventoDAO);
			cleanup(notificaDAO);
			cleanup(autoritaEsternaDAO);
			cleanup(penaResiduaDAO);
			cleanup(scadenzarioDAO);
			// cleanup(depositoOrdinanzaPcSqlDAO);
			// cleanup(depositoOrdinanzaPcDAO);
			cleanup(misAltDAO);
			cleanup(scadenzarioSqlDAO);

			cleanup(connection);
		}

		// valore di ritorno
		return eventoNotificaModel;
	}
	// ***** FINE INTERVENTO MEV_39 *****//

} // CHIUDO ORDINEESECUZIONECONTROLLER