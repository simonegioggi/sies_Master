package siap.sico.evento.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.dao.CampoNotaSqlDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.cssa.dao.CSSASqlDAO;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoDepositoModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.magistrato.dao.MagistratoSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.template.controller.TemplateManager;
import siap.sico.template.dao.TemplateSqlDAO;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.dao.AutoritaEsternaSqlDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepPerEventoSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaDAO;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.pagoPA.dao.CivilmenteObbligatoSqlDAO;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.penapecuniaria.dao.RichiestaConversioneDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scambiosanzione.dao.ScambioSanzioneDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoSqlDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.sige.avvocato.dao.AvvocatoFascicoloSigeSqlDAO;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.curatore.dao.CuratoreSqlDAO;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.provvedimento.dao.ProvvedimentoSigeDAO;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sius.SIUSException;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusSqlDAO;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.avvocatura.action.ICostantiAvvisiAvvocato;
import siap.sius.avvocatura.dao.AvvisiAvvocatoDAO;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.curatore.dao.CuratoreSiusDAO;
import siap.sius.curatore.model.CuratoreSiusModel;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.dao.DepositoSentenzaSqlDAO;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sius.documentoallegato.dao.DocumentoAllegatoSqlDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.esecuzionemisurasicurezza.dao.EsecuzioneMisuraSicurezzaDAO;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.dao.FascicoloSiusSqlDAO;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoSqlDAO;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.misurasicurezza.dao.PeriodoAltraMisuraDAO;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienzaprocedimento.dao.UdienzaProcedimentoDAO;
import siap.sius.udienzaprocedimento.dao.UdienzaProcedimentoSqlDAO;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoUdiModel;

/**
 * EventoController - Classe Controller per Evento
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EventoController extends SiapController implements IEvento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisce Evento Notifica e Autorita Esterne associate
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExInserisciEvento(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		EventoModel lEveRet = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			// Setto l'anno e il progressivo...
			lSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento);
			aEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));
			lEveDao.setDAOFromModel(aEvento);

			BigDecimal lKeyEvento = lEveDao.insert();
			lEveRet.setIdEvento(lKeyEvento);

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("EventoController.ExInserisciEvento: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("EventoController.ExInserisciEvento: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lSqlDAO);
			cleanup(lConn);
		}
		return lEveRet;
	}

	/**
	 * Inserisce Evento Notifica, Autorita Esterne associate e eventuali Campi note aggiuntive
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciEventoNotifica(EventoNotificaModel aEvento) throws F3BException {

		Connection lConn = null;
		EventoNotificaModel lEveRet = null;

		try {
			lConn = getDBTransaction();
			lEveRet = ExInserisciEventoNotifica(aEvento, lConn);
			commit(lConn);
		} catch (F3BException fEx) {
			rollback(lConn);
			throw new F3BException("EventoController.ExInserisciEventoNotifica:" + fEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExInserisciEventoNotifica: " + ex);
		} finally {
			cleanup(lConn);
		}

		return lEveRet;
	}

	// / Luigi 2-2-2005
	public EventoNotificaModel ExInserisciEventoNotifica(EventoNotificaModel aEvento, Connection aConn)
			throws F3BException {

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lEveDao = new EventoDAO(aConn);
			lAutDao = new AutoritaEsternaDAO(aConn);
			lNotDao = new NotificaDAO(aConn);
			lCampoNotaDao = new CampoNotaDAO(aConn);
			// Setto l'anno e il progressivo...
			lSqlDAO = new EventoSqlDAO(aConn);
			BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
			aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			lEveDao.setDAOFromModel(aEvento.getEvento());

			BigDecimal lKeyEvento = lEveDao.insert();
			lEveRet.getEvento().setIdEvento(lKeyEvento);

			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (aEvento != null && aEvento.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + aEvento.getNotifiche().length + " notifiche");

				while (count < aEvento.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + aEvento.getNotifiche()[count]);

					if (aEvento.getNotifiche()[count] != null) {

						if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
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

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserito evento" + lKeyEvento);
					}
					count++;
				}
			}

			// Inserimento delle eventuali note aggiuntive.
			if (aEvento.getCampoNote() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
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
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExInserisciEventoNotifica: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("EventoController.ExInserisciEventoNotifica: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
		}

		return lEveRet;
	}

	/**
	 * Inserisce Evento Notifica, Autorita Esterne associate e eventuali pena res e Campi note aggiuntive.
	 *
	 * @param aEvento
	 * @param aPenaResidua
	 * @return EventoNotificaModelEventoNotificaModel
	 * @throws F3BException
	 */
	// public EventoNotificaModel ExInserisciEventoNotifica(EventoNotificaModel aEvento,
	// PenaResiduaModel aPenaResidua) throws F3BException {
	//
	// Connection lConn = null;
	// EventoDAO lEveDao = null;
	// EventoSqlDAO lSqlDAO = null;
	// NotificaDAO lNotDao = null;
	// AutoritaEsternaDAO lAutDao = null;
	// PenaResiduaDAO lPenDao = null;
	// // CampoNotaDAO lCampoNotaDao = null;
	//
	// EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);
	//
	// try {
	// lConn = getDBTransaction();
	// lEveDao = new EventoDAO(lConn);
	// lEveDao.setDAOFromModel(aEvento.getEvento());
	// BigDecimal lKeyEvento = lEveDao.insert();
	//
	// lAutDao = new AutoritaEsternaDAO(lConn);
	// lNotDao = new NotificaDAO(lConn);
	//
	// lEveRet.getEvento().setIdEvento(lKeyEvento);
	//
	// BigDecimal lKeyAutorita = null;
	// int count = 0;
	//
	// if (aEvento != null && aEvento.getNotifiche() != null) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Presenti " + aEvento.getNotifiche().length + " notifiche");
	//
	// while (count < aEvento.getNotifiche().length) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("Notifica[" + count + "] = " + aEvento.getNotifiche()[count]);
	//
	// if (aEvento.getNotifiche()[count] != null) {
	//
	// if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
	// lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
	// AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
	// lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();
	//
	// if (lAutMod == null) {
	// lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
	// lKeyAutorita = lAutDao.insert();
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
	// // al posto di LogF3B.getLogger()
	// siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
	// aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
	// } else {
	// lKeyAutorita = lAutMod.getIdAutoritaEsterna();
	// aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
	// }
	// }
	//
	// aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);
	//
	// lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
	// lNotDao.insert();
	// lNotDao.stop();
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
	// // di LogF3B.getLogger()
	// siesLogger.debug("Inserito evento" + lKeyEvento);
	// }
	// count++;
	// }
	// }
	//
	// lPenDao = new PenaResiduaDAO(lConn);
	// // Inserisce Pena Residua
	// PenaResiduaModel lPenMod = aPenaResidua;
	// if (lPenMod != null) {
	// lPenMod.setIdPenaResidua(null);
	// lPenMod.setCodOperatoreAggiornamento(null);
	// lPenMod.setDataAggiornamento(null);
	// lPenMod.setCodUfficioAggiornamento(null);
	//
	// lPenMod.setFlagValidato("S");
	// lPenMod.setEveIdEvento(lKeyEvento);
	// lPenMod.setCodOperatoreInserimento(aEvento.getEvento().getCodOperatoreInserimento());
	// lPenMod.setDataInserimento(DateUtils.getSysDate());
	// lPenMod.setCodUfficioInserimento(aEvento.getEvento().getCodUfficioInserimento());
	//
	// lPenDao.setDAOFromModel(lPenMod);
	// lPenDao.insert();
	// lPenDao.stop();
	// }
	//
	// /*
	// * // Inserimento delle eventuali note aggiuntive. lCampoNotaDao = new CampoNotaDAO(lConn); if
	// * (aEvento.getCampoNote() != null) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
	// * istanza siesLogger al posto di LogF3B.getLogger()
	// * siesLogger.debug("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : " +
	// * aEvento.getCampoNote().length); count = 0; while (count < aEvento.getCampoNote().length) {
	// * aEvento.getCampoNote()[count].setEveIdEvento(lKeyEvento);
	// * aEvento.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
	// * lCampoNotaDao.setDAOFromModel(aEvento.getCampoNote()[count]); lCampoNotaDao.insert();
	// * lCampoNotaDao.stop();
	// *
	// * count++; } }
	// */
	//
	// commit(lConn);
	// } catch (DAOException daoEx) {
	// throw new F3BException("EventoController.ExInserisciEventoNotifica: " + daoEx);
	// } catch (Exception ex) {
	// throw new F3BException("EventoController.ExInserisciEventoNotifica: " + ex);
	// } finally {
	// // cleanup(lCampoNotaDao);
	// cleanup(lEveDao);
	// cleanup(lNotDao);
	// cleanup(lAutDao);
	// cleanup(lSqlDAO);
	// cleanup(lPenDao);
	// cleanup(lConn);
	// }
	//
	// return lEveRet;
	// }

	/**
	 * Restituisce l'elenco degli eventi che rispettano le condizioni passate con il model in input ordinati
	 * per data EMISSIONE descrescente. n.b. non tutti i campi del model vengono utilizzati per comporre la
	 * query n.b. se non trava corrispondenza viene rilanciata una eccezione con error code
	 * F3BException.USER_MESSAGE = 'Nessun elemento trovato'
	 *
	 * @param aEvento
	 *            - Model di ricerca
	 * @return vettore di EventoModel
	 * @throws F3BException
	 */
	public Vector ExRicercaEvento(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		Vector lEventi = new Vector();
		EventoSqlDAO lEveDao = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEvento(aEvento);
			lEventi = new Vector(lEveDao.getModels());
			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEvento: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEvento: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	public boolean ExRicercaEventoInoltroDispPM(BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;
		Vector lEventi = new Vector();
		EventoSqlDAO lEveDao = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoInoltroDispPMByEveIdEvento(aIdEvento);
			lEventi = new Vector(lEveDao.getModels());
			if (lEventi.size() == 0)
				return false;
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoInoltroDispPM: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoInoltroDispPM: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return true;
	}

	/**
	 * ExRicercaEventoIstanzaRigettata
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoIstanzaRigettata(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		Vector lEventi = new Vector();
		EventoSqlDAO lEveDao = null;
		EventoSqlDAO lEveDecDao = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDecDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaRevocaOrdinanzaRigettataByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
			lEventi = new Vector(lEveDao.getModels());
			if (lEventi.size() == 0) {
				lEveDecDao
						.ricercaRevocaDecretoInammisibilitaByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
				lEventi = new Vector(lEveDecDao.getModels());
			}
			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		}

		catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoIstanzaRigettata: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoIstanzaRigettata: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveDecDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * ricerca evento istanza
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoIstanza(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lEventi = new Vector();
		EventoSqlDAO lEveDao = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaIstanzeByFascicolo(aKey);
			lEventi = new Vector(lEveDao.getModels());
			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("EventoController.ExRicercaEventoIstanza: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoIstanza: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * ricerca evento Misura alternativa flag_registrato=n Affidamento In Prova
	 *
	 * @param aKey
	 * @param aEveKey
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoMANonRegistratoByFascicoloSiep(BigDecimal aKey, BigDecimal aEveKey)
			throws F3BException {

		Connection lConn = null;
		EventoModel lEvento = new EventoModel();
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoMANonRegistratoMAByFascicoloSiep(aKey, aEveKey);
			lEvento = (EventoModel) lEveDao.getModelByKey();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoMANonRegistratoByFascicoloSiep: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoMANonRegistratoByFascicoloSiep: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	/**
	 * Ricerca evento Misura alternativa per fascicolo sius
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoMisuraAlternativaByIdFasSius(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		EventoModel lEvento = null;
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoMisuraAlternativaByIdFasSius(aKey);
			lEvento = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoMisuraAlternativaByIdFasSius: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoMisuraAlternativaByIdFasSius: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	/**
	 * ricerca evento non registrato generico
	 *
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoNonRegistrato(EventoModel aModel) throws F3BException {

		Connection lConn = null;
		EventoModel lEvento = new EventoModel();
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoNonRegistrato(aModel);
			lEvento = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoNonRegistratoGenerico: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoNonRegistratoGenerico: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	/**
	 * Ricerca Evento per Motivo
	 *
	 * @param aMotivo
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoPerMotivo(String[] aMotivo, EventoModel aModel) throws F3BException {

		Connection lConn = null;

		EventoModel lEvento = new EventoModel();
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();

			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoPerMotivo(aMotivo, aModel);
			lEvento = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoPerMotivo: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoPerMotivo: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	/**
	 * Ricerca Eventi per Motivo e tipo provvedimento
	 *
	 * @param aMotivo
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventiPerMotivoTipoProvv(String[] aMotivo, String[] aTipo, EventoModel aModel)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveDao = null;
		Vector lEventi = new Vector();

		try {
			lConn = getDBConnection();

			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoPerMotivoProvv(aMotivo, aTipo, aModel);
			lEveDao.start();
			while (lEveDao.next()) {
				MisuraAlternativaAggregatoModel lAgg = lEveDao.getModelDecretoOrdinanzaUfficio();
				lEventi.add(lAgg);
			}
			lEveDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventiPerMotivoTipoProvv: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventiPerMotivoTipoProvv: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * ExRicercaEventoPerMotivoOrderDesc
	 *
	 * @param aMotivo
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoPerMotivoOrderDesc(String[] aMotivo, EventoModel aModel)
			throws F3BException {

		Connection lConn = null;
		EventoModel lEvento = new EventoModel();
		EventoSqlDAO lEveDao = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoPerMotivoOrderDesc(aMotivo, aModel);
			lEvento = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoPerMotivoDesc: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoPerMotivoDesc: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	// STUB 29/09/2005 REWORK STATO ESECUZIONE
	/**
	 * Ricerca l'ultimo evento che ha codice motivo e tipo provvedimento tra quelli passati in input, mentre
	 * le condizione su idFascicolo codTipoEvento e FlagDocumentoRegistrato vengono recuperati del Model.
	 *
	 * @param aMotivo
	 *            vettore contenente l'elenco dei codici motivo su cui effettuare la ricerca
	 * @param aTipoProvv
	 *            vettore contenente l'elenco dei codici tipo provvedimento su cui effettuare la ricerca
	 * @param aModel
	 *            model da cui vengono estratti IdFascicolo, codTipoEvento e flagDocumentoRegistrato
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoPerMotivoPerProvv(String[] aMotivo, String[] aTipoProvv,
			EventoModel aModel) throws F3BException {

		Connection lConn = null;
		EventoModel lEvento = new EventoModel();
		EventoSqlDAO lEveDao = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoPerMotivoPerProvv(aMotivo, aTipoProvv, aModel);
			lEvento = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoPerMotivoPerProvv: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoPerMotivoPerProvv: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	/**
	 * ExRicercaEventoTipoProv
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoTipoProv(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		EventoModel lEvento = new EventoModel();
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoTipoProv(aEvento);
			lEvento = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoTipoProv: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoTipoProv: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	/**
	 * Ricerca un Ordine di Esecuzione non registrato attraverso la chiave del Fascicolo.
	 *
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExRicercaOENotificheNonRegistratoByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoNotificaModel lEve = null;
		EventoSqlDAO lEveDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		UfficioSqlDAO lUffDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		AvvocatoFascicoloSiepPerEventoSqlDAO lAvvDao = null;
		MagistratoSqlDAO lMagDAO = null;

		try {
			lConn = getDBConnection();

			lEveDao = new EventoSqlDAO(lConn);
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lMagDAO = new MagistratoSqlDAO(lConn);
			lEveDao.ricercaOrdineEsecuzioneNonRegistratoByFascicoloSiep(aIdFascicolo);
			lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());

			if (lEve.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

			if (lEve.getEvento().getCodMagistrato() != null) {
				// ***** GDV direttamente sul Magistrato al posto di andare sul MAgistratoCompetente ******
				lMagDAO.ricercaMagistratoByCod(lEve.getEvento().getCodMagistrato());
				MagistratoModel lMag = (MagistratoModel) lMagDAO.getModelByKey();
				lEve.setMagistrato(lMag);
			}

			lUffDao = new UfficioSqlDAO(lConn);
			lAutoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);
			lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(lEve.getEvento().getIdEvento());
			Vector lNotifiche = new Vector(lNotEveDao.getModels());
			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

			Vector lAvvocati = new Vector();
			// Verifica ed inserisce le Autorita Esterne e gli uffici
			int count = 0;
			while (count < lEve.getNotifiche().length) {
				// Autorita Esterne
				if (lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna() != null) {
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(
							lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setAutoritaEsterna(lAutorita);
					lAutoritaSqlDao.stop();
				}

				// Preleva gli uffici
				if (lEve.getNotifiche()[count].getUffCodUfficio() != null) {
					lUffDao.selUfficioByCod(lEve.getNotifiche()[count].getUffCodUfficio());
					UfficioModel lUffMod = (UfficioModel) lUffDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lEve.getNotifiche()[count].setUfficio(lUffMod);
					lUffDao.stop();
				}

				// Preleva gli avvocati
				if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep() != null) {
					lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);
					lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSiep(
							lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep());
					lAvvocati.add(lAvvDao.getModelByKey());
				}

				count++;
			}

			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

			if (lAvvocati.size() > 0)
				lEve.setAvvocati((AvvocatoSiepModel[]) lAvvocati.toArray(new AvvocatoSiepModel[0]));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Evento = " + lEve);
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			throw new F3BException(
					"EventoController.ExRicercaOENotificheNonRegistratoByIdFascicolo: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			e.printStackTrace();
			throw new F3BException("EventoController.ExRicercaOENotificheNonRegistratoByIdFascicolo: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lUffDao);
			cleanup(lAutoritaSqlDao);
			cleanup(lAvvDao);
			cleanup(lMagDAO);
			cleanup(lNotEveDao);

			cleanup(lConn);
		}

		return lEve;
	}

	/**
	 * Ricerca Evento attraverso la chiave.
	 *
	 * @param aEventoKey
	 * @return lEve
	 * @throws F3BException
	 */
	public EventoNotificaModel ExRicercaEventoNotificaByKey(BigDecimal aEventoKey) throws F3BException {

		Connection lConn = null;

		EventoNotificaModel lEve = null;

		try {
			lConn = getDBConnection();
			lEve = ExRicercaEventoNotificaByKey(aEventoKey, lConn);
		} catch (F3BException fe) {
			throw fe;
		} finally {
			cleanup(lConn);
		}
		return lEve;
	}

	/**
	 * Ricerca Evento attraverso la chiave.
	 *
	 * @param aEventoKey
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExRicercaEventoNotificaByKey(BigDecimal aEventoKey, Connection lConn)
			throws F3BException {

		EventoNotificaModel lEve = null;

		EventoSqlDAO lEveDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		CSSASqlDAO lCssaDao = null;
		UfficioSqlDAO lUffDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		AvvocatoFascicoloSiepPerEventoSqlDAO lAvvDao = null;
		AvvocatoFascicoloSiusSqlDAO lAvvSiusDao = null;
		AvvocatoFascicoloSigeSqlDAO lAvvSigeDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		MagistratoSqlDAO lMagDAO = null;
		CampoNotaSqlDAO lCampoNotaSqlDao = null;

		// MEV_2023-13
		CivilmenteObbligatoSqlDAO lCivilmenteObbSqlDao = null;
		ResidenzaSqlDAO lResidenzaSqlDao = null;

		try {
			lEveDao = new EventoSqlDAO(lConn);
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lCampoNotaSqlDao = new CampoNotaSqlDAO(lConn);
			lMagDAO = new MagistratoSqlDAO(lConn);

			// MEV_2023-13
			lCivilmenteObbSqlDao = new CivilmenteObbligatoSqlDAO(lConn);

			lEveDao.ricercaEventoByKey(aEventoKey);

			lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());

			if (lEve.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

			if (lEve.getEvento().getCodMagistrato() != null) {
				lMagDAO.ricercaMagistratoByCod(lEve.getEvento().getCodMagistrato());
				MagistratoModel lMag = (MagistratoModel) lMagDAO.getModelByKey();
				lEve.setMagistrato(lMag);
			}

			lUffDao = new UfficioSqlDAO(lConn);
			lCssaDao = new CSSASqlDAO(lConn);
			lAutoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);
			lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(aEventoKey);
			Vector lNotifiche = new Vector(lNotEveDao.getModels());
			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

			Vector lAvvocati = new Vector();
			Vector lAvvocatiSius = new Vector();
			Vector lAvvocatiSige = new Vector(); // 23/12/2008
			// Verifica ed inserisce le Autorita Esterne e gli uffici e gli Avvocati
			int count = 0;
			while (count < lEve.getNotifiche().length) {
				// Autorita Esterne
				if (lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna() != null) {
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(
							lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setAutoritaEsterna(lAutorita);
					lAutoritaSqlDao.stop();
				}
				// modifica relativa al tipo istituto
				if (lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione() != null
						&& !lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione().equals("")) {
					lIstDao.ricercaIstitutoDetenzioneByKey(
							lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione());
					IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setIstitutoDetenzione(lIstituto);
					lIstDao.stop();
				}
				// fine modifica relativa al tipo istituto

				// Preleva gli uffici
				if (lEve.getNotifiche()[count].getUffCodUfficio() != null) {
					lUffDao.selUfficioByCod(lEve.getNotifiche()[count].getUffCodUfficio());
					UfficioModel lUffMod = (UfficioModel) lUffDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lEve.getNotifiche()[count].setUfficio(lUffMod);
					lUffDao.stop();
				}

				// Preleva il Cssa
				if (lEve.getNotifiche()[count].getCssIdCssa() != null) {
					lCssaDao.selModelCssabyKey(lEve.getNotifiche()[count].getCssIdCssa());
					CSSAModel lCssaMod = (CSSAModel) lCssaDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lEve.getNotifiche()[count].setCSSA(lCssaMod);
					lCssaDao.stop();
				}

				// Preleva gli avvocati
				if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep() != null) {
					lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);

					lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSiep(
							lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep());
					AvvocatoSiepModel lAvvSiep = (AvvocatoSiepModel) lAvvDao.getModelByKey();
					lAvvocati.add(lAvvSiep);

					// Aggiunge l'AvvocatoSiepModel al model di Notifica
					lEve.getNotifiche()[count].setAvvSiep(lAvvSiep);
				}
				// Preleva gli avvocati SIUS
				if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSius() != null) {
					lAvvSiusDao = new AvvocatoFascicoloSiusSqlDAO(lConn);

					lAvvSiusDao.ricercaAvvocatoByKeyAvvocatoFasSius(
							lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSius());
					AvvocatoSiusModel lAvvSius = (AvvocatoSiusModel) lAvvSiusDao.getModelByKey();
					lAvvocatiSius.add(lAvvSius);

					// Aggiunge l'AvvocatoSiusModel al model di Notifica
					lEve.getNotifiche()[count].setAvvSius(lAvvSius);
				}
				// Preleva gli avvocati SIGE //23/12/2008
				if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSige() != null) {
					lAvvSigeDao = new AvvocatoFascicoloSigeSqlDAO(lConn);

					lAvvSigeDao.ricercaAvvocatoByKeyAvvocatoFasSige(
							lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSige());
					AvvocatoSigeModel lAvvSige = (AvvocatoSigeModel) lAvvSigeDao.getModelByKey();
					lAvvocatiSige.add(lAvvSige);

					// Aggiunge l'AvvocatoSigeModel al model di Notifica
					lEve.getNotifiche()[count].setAvvSige(lAvvSige);
				}

				// MEV_2023-13 - Preleva i Civilmente Obbligati
				if (lEve.getNotifiche()[count].getIdCivilmenteObbligato() != null) {
					lCivilmenteObbSqlDao = new CivilmenteObbligatoSqlDAO(lConn);
					lCivilmenteObbSqlDao.ricercaCivilmenteObbligatoByKey(
							lEve.getNotifiche()[count].getIdCivilmenteObbligato());
					CivilmenteObbligatoModel lObbligatoModel = (CivilmenteObbligatoModel) lCivilmenteObbSqlDao
							.getModelByKey();

					lEve.getNotifiche()[count].setCivilmenteObbligato(lObbligatoModel);

					// 2023.12.11 Aggiungo la residenza
					if (lObbligatoModel != null) {
						lResidenzaSqlDao = new ResidenzaSqlDAO(lConn);
						lResidenzaSqlDao.ricercaDomicilioCorrenteByIdCivilmenteObbligato(
								lObbligatoModel.getIdCivilmenteObbligato());
						ResidenzaModel lResidenza = (ResidenzaModel) lResidenzaSqlDao.getModelByKey();
						if (lResidenza != null)
							lObbligatoModel.setResidenza(lResidenza);
					}
				}

				// Autorita Esterne Delegata
				if (lEve.getNotifiche()[count].getAutEstIdAutoritaEstDeleg() != null) {
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(
							lEve.getNotifiche()[count].getAutEstIdAutoritaEstDeleg());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setAutoritaEsternaDelegata(lAutorita);
					lAutoritaSqlDao.stop();
				}
				// MEV_2023-13 - FINE

				count++;
			}

			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

			if (lAvvocati.size() > 0)
				lEve.setAvvocati((AvvocatoSiepModel[]) lAvvocati.toArray(new AvvocatoSiepModel[0]));
			if (lAvvocatiSius.size() > 0)
				lEve.setAvvocatiSius((AvvocatoSiusModel[]) lAvvocatiSius.toArray(new AvvocatoSiusModel[0]));
			if (lAvvocatiSige.size() > 0) // 23/12/2008
				lEve.setAvvocatiSige((AvvocatoSigeModel[]) lAvvocatiSige.toArray(new AvvocatoSigeModel[0]));

			// Campi note
			lCampoNotaSqlDao.ricercaCampoNotaByKeyEvento(aEventoKey);
			Vector lCampiNote = new Vector(lCampoNotaSqlDao.getModels());
			lEve.setCampoNote((CampoNotaModel[]) lCampiNote.toArray(new CampoNotaModel[0]));
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			throw new F3BException("EventoController.ExRicercaEventoNotificaByKey: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			e.printStackTrace();
			throw new F3BException("EventoController.ExRicercaEventoNotificaByKey: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lUffDao);
			cleanup(lAutoritaSqlDao);
			cleanup(lAvvDao);
			cleanup(lAvvSiusDao);
			cleanup(lMagDAO);
			cleanup(lNotEveDao);
			cleanup(lCssaDao);
			// modifica relativa al tipo istituto
			cleanup(lIstDao);
			cleanup(lCampoNotaSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lAvvSigeDao);

			cleanup(lCivilmenteObbSqlDao); // MEV_2023-13
			cleanup(lResidenzaSqlDao);
		}
		return lEve;
	}

	/**
	 * Ricerca Evento attraverso EVE_ID_EVENTO.
	 *
	 * @param aEventoKey
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExRicercaEventoNotificaByEveIdEvento(BigDecimal aEventoKey)
			throws F3BException {

		Connection lConn = null;

		EventoNotificaModel lEve = null;
		EventoSqlDAO lEveDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		CSSASqlDAO lCssaDao = null;
		UfficioSqlDAO lUffDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		AvvocatoFascicoloSiepPerEventoSqlDAO lAvvDao = null;
		AvvocatoFascicoloSiusSqlDAO lAvvSiusDao = null;
		AvvocatoFascicoloSigeSqlDAO lAvvSigeDao = null; // 23/12/2008
		IstitutoDetenzioneSqlDAO lIstDao = null;
		MagistratoSqlDAO lMagDAO = null;
		CuratoreSqlDAO lCurSqlDao = null; // 25/05/2011
		CuratoreSiusDAO lCurSiusDao = null; // 25/05/2011

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lMagDAO = new MagistratoSqlDAO(lConn);

			lEveDao.ricercaEventoByEveIdEvento(aEventoKey);

			lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());

			if (lEve.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

			if (lEve.getEvento().getCodMagistrato() != null) {
				lMagDAO.ricercaMagistratoByCod(lEve.getEvento().getCodMagistrato());
				MagistratoModel lMag = (MagistratoModel) lMagDAO.getModelByKey();
				lEve.setMagistrato(lMag);
			}

			lUffDao = new UfficioSqlDAO(lConn);
			lCssaDao = new CSSASqlDAO(lConn);
			lAutoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);
			lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEvento(lEve.getEvento().getIdEvento());
			Vector lNotifiche = new Vector(lNotEveDao.getModels());
			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

			Vector lAvvocati = new Vector();
			Vector lAvvocatiSius = new Vector();
			Vector lAvvocatiSige = new Vector(); // 23/12/2008
			// Verfica ed inserisce le Autorita Esterne e gli uffici e gli Avvocati
			int count = 0;
			while (count < lEve.getNotifiche().length) {
				// Autorita Esterne
				if (lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna() != null) {
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(
							lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setAutoritaEsterna(lAutorita);
					lAutoritaSqlDao.stop();
				}
				// modifica relativa al tipo istituto
				if (lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione() != null
						&& !lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione().equals("")) {
					lIstDao.ricercaIstitutoDetenzioneByKey(
							lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione());
					IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setIstitutoDetenzione(lIstituto);
					lIstDao.stop();
				}
				// fine modifica relativa al tipo istituto

				// Preleva gli uffici
				if (lEve.getNotifiche()[count].getUffCodUfficio() != null) {
					lUffDao.selUfficioByCod(lEve.getNotifiche()[count].getUffCodUfficio());
					UfficioModel lUffMod = (UfficioModel) lUffDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lEve.getNotifiche()[count].setUfficio(lUffMod);
					lUffDao.stop();
				}

				// Preleva il Cssa
				if (lEve.getNotifiche()[count].getCssIdCssa() != null) {
					lCssaDao.selModelCssabyKey(lEve.getNotifiche()[count].getCssIdCssa());
					CSSAModel lCssaMod = (CSSAModel) lCssaDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lEve.getNotifiche()[count].setCSSA(lCssaMod);
					lCssaDao.stop();
				}

				// Preleva gli avvocati
				if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep() != null) {
					lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);

					lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSiep(
							lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep());
					AvvocatoSiepModel lAvvSiep = (AvvocatoSiepModel) lAvvDao.getModelByKey();
					lAvvocati.add(lAvvSiep);

					// Aggiunge l'AvvocatoSiepModel al model di Notifica
					lEve.getNotifiche()[count].setAvvSiep(lAvvSiep);
				}
				// Preleva gli avvocati SIUS
				if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSius() != null) {
					lAvvSiusDao = new AvvocatoFascicoloSiusSqlDAO(lConn);

					lAvvSiusDao.ricercaAvvocatoByKeyAvvocatoFasSius(
							lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSius());
					AvvocatoSiusModel lAvvSius = (AvvocatoSiusModel) lAvvSiusDao.getModelByKey();
					lAvvocatiSius.add(lAvvSius);

					// Aggiunge l'AvvocatoSiusModel al model di Notifica
					lEve.getNotifiche()[count].setAvvSius(lAvvSius);
				}
				// Preleva gli avvocati SIGE // 23/12/2008
				if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSige() != null) {
					lAvvSigeDao = new AvvocatoFascicoloSigeSqlDAO(lConn);

					lAvvSigeDao.ricercaAvvocatoByKeyAvvocatoFasSige(
							lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSige());
					AvvocatoSigeModel lAvvSige = (AvvocatoSigeModel) lAvvSigeDao.getModelByKey();
					lAvvocatiSige.add(lAvvSige);

					// Aggiunge l'AvvocatoSigeModel al model di Notifica
					lEve.getNotifiche()[count].setAvvSige(lAvvSige);
				}
				// 25/05/2011 Preleva curatore/tutore SIUS
				if (lEve.getNotifiche()[count].getCurIdCuratore() != null) {
					CuratoreSiusModel lCuratore = null;
					lCurSiusDao = new CuratoreSiusDAO(lConn);
					lCurSiusDao.setCondizioneAttivo(lEve.getEvento().getFasSiuIdFascicoloSius());
					lCuratore = (CuratoreSiusModel) lCurSiusDao.getModelByKey();
					if (lCuratore != null) {
						lCuratore.setDescrTipo(DecodificheUtils.getDescbyCode(
								DecodificheManager.getInstance().getTipoCuratore(), lCuratore.getFlagTipo()));
						lCurSqlDao = new CuratoreSqlDAO(lConn);
						lCurSqlDao.ricercaCuratoreByKey(lEve.getNotifiche()[count].getCurIdCuratore());
						CuratoreModel lCurMod = (CuratoreModel) lCurSqlDao.getModelByKey();

						lCuratore.setCuratore(lCurMod);
					}

					// Aggiunge il Curatore / Tutore al model di Notifica
					lEve.getNotifiche()[count].setCurSius(lCuratore);
				}
				count++;
			}

			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));
			if (lAvvocati.size() > 0)
				lEve.setAvvocati((AvvocatoSiepModel[]) lAvvocati.toArray(new AvvocatoSiepModel[0]));
			if (lAvvocatiSius.size() > 0)
				lEve.setAvvocatiSius((AvvocatoSiusModel[]) lAvvocatiSius.toArray(new AvvocatoSiusModel[0]));
			if (lAvvocatiSige.size() > 0)
				lEve.setAvvocatiSige((AvvocatoSigeModel[]) lAvvocatiSige.toArray(new AvvocatoSigeModel[0]));
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKey: Non posso leggere : " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			e.printStackTrace();
			throw new F3BException("EventoController.ExRicercaEventoNotificaByKey:  " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lUffDao);
			cleanup(lAutoritaSqlDao);
			cleanup(lAvvDao);
			cleanup(lAvvSiusDao);
			cleanup(lMagDAO);
			cleanup(lNotEveDao);
			cleanup(lCssaDao);
			cleanup(lIstDao);
			cleanup(lCurSiusDao); // 25/05/2011
			cleanup(lCurSqlDao); // 25/05/2011
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lAvvSigeDao);
			cleanup(lConn);
		}

		return lEve;
	}

	/**
	 * PM -- per richiesta atti. Da rivedere il tutto Ricerca Evento attraverso la chiave.
	 *
	 * @param aEventoKey
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExRicercaEventoNotificaByKeyForRichiestaAtti(BigDecimal aEventoKey)
			throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		// PassaggioEventoSqlDAO lPEveDao = null;
		NotificaEventoSqlDAO lNotDao = null;
		EventoNotificaModel lEve = null;
		UfficioSqlDAO lUffDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		CSSASqlDAO lCSSASqlDao = null;
		CampoNotaSqlDAO lCampoNotaSqlDao = null;
		// Gestione Istituto Penitenziario
		IstitutoDetenzioneSqlDAO lIstDao = null;

		// MagistratoCompetenteMagistratoSqlDAO lMagDAO = null;
		// MagistratoCompetenteMagistratoModel lMag = new MagistratoCompetenteMagistratoModel();
		// PassaggioEventoModel lPEve = new PassaggioEventoModel();

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			// lPEveDao = new PassaggioEventoSqlDAO(lConn);
			lNotDao = new NotificaEventoSqlDAO(lConn);
			lAutoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);
			lUffDao = new UfficioSqlDAO(lConn);
			lCSSASqlDao = new CSSASqlDAO(lConn);
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lCampoNotaSqlDao = new CampoNotaSqlDAO(lConn);
			// lMagDAO = new MagistratoCompetenteMagistratoSqlDAO(lConn);
			// lEveDao.ricercaEvento(aEvento);
			lEveDao.ricercaEventoByKey(aEventoKey);

			lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());

			if (lEve.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato.");

			lNotDao.ricercaNotificaByEvento(aEventoKey);
			Vector lNotifiche = new Vector(lNotDao.getModels());
			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

			// Verfica ed inserisce le Autorita Esterne e gli uffici
			// destinatari.
			int lSize = lEve.getNotifiche().length;
			for (int count = 0; count < lSize; count++) {
				// MEV_66: modificato controllo (x2)
				// Autorita Esterne.
				if (lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna() != null
						&& !"-".equals(lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna().toString())) {
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(
							lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setAutoritaEsterna(lAutorita);
				} // Preleva gli uffici
				else if (lEve.getNotifiche()[count].getUffCodUfficio() != null
						&& !lEve.getNotifiche()[count].getUffCodUfficio().equals("-")) {
					lUffDao.selUfficioByCod(lEve.getNotifiche()[count].getUffCodUfficio());
					UfficioModel lUffMod = (UfficioModel) lUffDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lEve.getNotifiche()[count].setUfficio(lUffMod);
				} // CSSA
				else if (lEve.getNotifiche()[count].getCssIdCssa() != null
						&& !"-".equals(lEve.getNotifiche()[count].getCssIdCssa().toString())) {
					lCSSASqlDao.ricercaCSSAByKey(lEve.getNotifiche()[count].getCssIdCssa());
					CSSAModel lCSSA = (CSSAModel) lCSSASqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setCSSA(lCSSA);
				} // modifica relativa al tipo istituto
				else if (lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione() != null
						&& !lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione().equals("")) {
					lIstDao.ricercaIstitutoDetenzioneByKey(
							lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione());
					IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setIstitutoDetenzione(lIstituto);
					lIstDao.stop();
				} // fine modifica relativa al tipo istituto
			}

			// Campi note
			lCampoNotaSqlDao.ricercaCampoNotaByKeyEvento(aEventoKey);
			Vector lCampiNote = new Vector(lCampoNotaSqlDao.getModels());
			lEve.setCampoNote((CampoNotaModel[]) lCampiNote.toArray(new CampoNotaModel[0]));
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKeyForRichiestaAtti: Non posso leggere : "
							+ daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKeyForRichiestaAtti: Non posso leggere  : "
							+ e);
		} finally {
			cleanup(lEveDao);
			cleanup(lAutoritaSqlDao);
			cleanup(lNotDao);
			cleanup(lUffDao);
			cleanup(lCSSASqlDao);
			cleanup(lIstDao);
			cleanup(lCampoNotaSqlDao);
			cleanup(lConn);
		}
		return lEve;
	}

	/**
	 * Ricerca Evento attraverso la chiave.
	 *
	 * @param aEventoKey
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExRicercaEventoNotificaByKeyForUdienza(BigDecimal aEventoKey)
			throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		NotificaSqlDAO lNotDao = null;
		EventoNotificaModel lEve = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lNotDao = new NotificaSqlDAO(lConn);
			// lMagDAO = new MagistratoCompetenteMagistratoSqlDAO(lConn);
			// lEveDao.ricercaEvento(aEvento);
			lEveDao.ricercaEventoByKey(aEventoKey);

			lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());

			if (lEve.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

			lNotDao.ricercaNotificaByEvento(aEventoKey);
			Vector lNotifiche = new Vector(lNotDao.getModels());
			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoNotificaByKeyForUdienza: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoNotificaByKeyForUdienza: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lEve;
	}

	/**
	 * Ricerca Evento attraverso la chiave (per Trasm. Atti).
	 *
	 * @param aEventoKey
	 * @return lEve
	 * @throws F3BException
	 */
	public EventoNotificaModel ExRicercaEventoNotificaByKeyForTrasmAtti(BigDecimal aEventoKey)
			throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		// PassaggioEventoSqlDAO lPEveDao = null;
		NotificaEventoSqlDAO lNotDao = null;
		EventoNotificaModel lEve = null;
		// PassaggioEventoModel lPEve = new PassaggioEventoModel();

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			// lPEveDao = new PassaggioEventoSqlDAO(lConn);
			lNotDao = new NotificaEventoSqlDAO(lConn);

			lEveDao.ricercaEventoByKeyForTrasmAtti(aEventoKey);

			lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());

			if (lEve.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

			lNotDao.ricercaNotificaByEvento(aEventoKey);
			Vector lNotifiche = new Vector(lNotDao.getModels());
			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKeyForTrasmAtti: Non posso leggere : "
							+ daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKeyForTrasmAtti: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			// cleanup(lPEveDao);
			cleanup(lNotDao);

			cleanup(lConn);
		}
		return lEve;
	}

	/**
	 * Ricerca Evento attraverso la chiave del Fascicolo Siep.
	 *
	 * @param aEventoKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoNotificaByFascicoloSiep(BigDecimal aFascKey, String[] aTipoEvento)
			throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		Vector lEventi = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			// lNotDao = new NotificaAutoritaSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloSiepAsc(aFascKey, aTipoEvento);

			lEventi = new Vector(lEveDao.getModels());

			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByFascicoloSiep: Non posso leggere : " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByFascicoloSiep: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * 15/06/2011 Ricerca Evento attraverso la chiave del Fascicolo Siep e la chiave Ufficio.
	 *
	 * @param aEventoKey
	 * @param aChiaveUfficio
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoNotificaByFascicoloSiepChiaveUfficio(BigDecimal aFascKey,
			String aChiaveUfficio, String[] aTipoEvento) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		Vector lEventi = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloSiepAsc(aFascKey, aChiaveUfficio, aTipoEvento);

			lEventi = new Vector(lEveDao.getModels());

			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByFascicoloSiep: Non posso leggere : " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByFascicoloSiep: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * Ricerca Evento attraverso la chiave del Fascicolo Siep.e tipo provv
	 *
	 * @param aEventoKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoFascicoloSiepProvvSius(BigDecimal aFascKey) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		Vector lEventi = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoByFascicoloSiepProvvSiusAsc(aFascKey);
			lEventi = new Vector(lEveDao.getModels());
			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByFascicoloSiep: Non posso leggere : " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByFascicoloSiep: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * Ricerca Eventi per CFC attraverso la chiave del Fascicolo Sius. MEV10-s3: aggiunto parametro di
	 * passaggio per gestire tipologia ufficio minorenni
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param strCodTipoUfficio
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoXCFC(BigDecimal aFascKey, String aTipoEvento, String strCodTipoUfficio)
			throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		Vector lEventi = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoXCFC(aFascKey, aTipoEvento, strCodTipoUfficio);
			lEventi = new Vector(lEveDao.getModels());
			// Individuazione della presenza di documenti allegati
			lEventi = RicercaNumAllegati(lEventi, lConn, "06");
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoXCFC: Non posso leggere : " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException("EventoController.ExRicercaEventoXCFC: Non posso leggere  : " + sqe);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoXCFC: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * Ricerca Evento attraverso la chiave del Fascicolo Sius.
	 *
	 * @param aEventoKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoByFascicoloSius(BigDecimal aFascKey, String aTipoEvento)
			throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		// EventoNotificaModel lEve = null;
		Vector lEventi = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoByFascicoloSius(aFascKey, aTipoEvento);
			lEventi = new Vector(lEveDao.getModels());
			// Individuazione della presenza di documenti allegati
			lEventi = RicercaNumAllegati(lEventi, lConn, null);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSius: Non posso leggere : " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSius: Non posso leggere  : " + sqe);
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSius: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	// genny 23/03/2004
	/**
	 * Ricerca Evento attraverso la chiave del Fascicolo Sius.
	 *
	 * @param aEventoKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoByFascEsitoParereInamm(BigDecimal aFascKey, String aTipoEvento)
			throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		Vector lEventi = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoByFascEsitoParereInamm(aFascKey, aTipoEvento);
			lEventi = new Vector(lEveDao.getModels());
			// Individuazione della presenza di documenti allegati
			lEventi = RicercaNumAllegati(lEventi, lConn, null);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascEsitoParereInamm: Non posso leggere : " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascEsitoParereInamm: Non posso leggere  : " + sqe);
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascEsitoParereInamm: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * Ricerca Evento attraverso la chiave del Fascicolo Sius.
	 *
	 * @param aEventoKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAltroEventoByFascicoloSius(BigDecimal aFascKey, String aTipoEvento)
			throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		Vector lEventi = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaAltroEventoByFascicoloSius(aFascKey, aTipoEvento);
			lEventi = new Vector(lEveDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaAltroEventoByFascicoloSius: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaAltroEventoByFascicoloSius: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * Ricerca Provvedimenti SIUS comprensivi del numero di documenti allegati e della data di Deposito.
	 *
	 * @param aEvento
	 *            : contiene le condizioni di filtro della Ricerca
	 * @return Vector lEventi : elenco di EventoDepositoModel.
	 * @throws F3BException
	 */
	public Vector ExRicercaProvvedimentiDeposito(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		Vector lEventi = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEvento(aEvento);
			lEventi = new Vector(lEveDao.getModels());
			// Individuazione della presenza di documenti allegati e della data di deposito
			lEventi = RicercaNumAllegati(lEventi, lConn, null);
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaProvvedimentiByFascicoloSius: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEventi;
	}

	private Vector RicercaNumAllegati(Vector aEventi, Connection aConn, String aCodTipoDoc) throws Exception {

		// Correzione del 24-6-2005 by Luigi
		// Ora la funzione ricava anche la Data di Deposito del Decreto/Ordinanza

		// Nuovo Vettore esteso
		Vector lEventiDep = aEventi;

		EventoSqlDAO lEveDao = null;
		if (aEventi != null) {
			try {
				Iterator itx = aEventi.iterator();
				lEveDao = new EventoSqlDAO(aConn);
				int lnum = -1;
				int lNumValidati = -1;
				EventoModel lEvento = null;

				// Model Esteso e Nuovo Vector
				EventoDepositoModel lEventoDep = null;
				lEventiDep = new Vector();

				while (itx.hasNext()) {
					Date lDataDeposito = null;
					lEvento = (EventoModel) itx.next();
					lnum = lEveDao.getNumDocumentiAllegati(lEvento.getIdEvento(), aCodTipoDoc);
					lEvento.setNumAllegati(lnum);
					if (lnum > 0) {
						lNumValidati = lEveDao.getNumAllegatiValidati(lEvento.getIdEvento());
						lEvento.setNumAllValidati(lNumValidati);
						// Qua bisogna inserire la ricerca della data deposito
						// :::::::::::::::::::::::
						// STUB: Luigi
						if (lEvento.getDescrTipoProvvedimento().compareToIgnoreCase("Decreto") == 0) {
							DepositoDecretoSqlDAO DepDecDao = new DepositoDecretoSqlDAO(aConn);
							lDataDeposito = DepDecDao.getDataDepositoByEve(lEvento.getIdEvento());
							cleanup(DepDecDao);
						} else if (lEvento.getDescrTipoProvvedimento()
								.compareToIgnoreCase("Ordinanza") == 0) {
							DepositoOrdinanzaPcSqlDAO DepOrdDao = new DepositoOrdinanzaPcSqlDAO(aConn);
							lDataDeposito = DepOrdDao.getDataDepositoByEve(lEvento.getIdEvento());
							cleanup(DepOrdDao);
						}
						// MEV10-s3: gestione casistica per "sentenza"
						else if (lEvento.getDescrTipoProvvedimento().compareToIgnoreCase("Sentenza") == 0) {
							DepositoSentenzaSqlDAO lDepSentDao = new DepositoSentenzaSqlDAO(aConn);
							lDepSentDao.ricercaDepositoSentenzaByIdEveGenerato(lEvento.getIdEvento());
							DepositoSentenzaModel lDepSenMod = (DepositoSentenzaModel) lDepSentDao
									.getModelByKey();
							lDataDeposito = lDepSenMod.getDataDeposito();
							cleanup(lDepSentDao);
						}
					}
					// Costruzione del nuovo Vector
					lEventoDep = new EventoDepositoModel(lEvento, lDataDeposito);
					lEventiDep.add(lEventoDep);
				}
			} finally {
				cleanup(lEveDao);
			}
		}
		return lEventiDep;
	}

	public EventoNotificaModel ExConfermaTrasmissione(EventoModel aEvento, NotificaModel aNotifica,
			String aCodStatoProcedimento) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NotificaDAO lNotDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);

			lEveDao.setDataTrasmissioneAtti(aEvento.getDataTrasmissioneAtti());
			lEveDao.setCodUfficioDestinatario(aEvento.getCodUfficioDestinatario());
			lEveDao.setCodLuogoDestinatario(aEvento.getCodLuogoDestinatario());
			lEveDao.setCodEsito("37"); // TRASFERITA A uds

			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.update();

			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoIstanzaByKey(aEvento.getIdEvento());
			aEvento = (EventoModel) lEveSqlDao.getModelByKey();

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);

			// Trasmessa istanza
			lStatoDao.setCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
			lStatoDao.delete();
			lStatoDao.stop();

			StatoProcedimentoModel lStatoUno = new StatoProcedimentoModel();
			lStatoUno.setProgressivo(new BigDecimal(1));
			lStatoUno.setCodStatoProcedimento(aCodStatoProcedimento);
			lStatoUno.setData(aNotifica.getDataInserimento());
			lStatoUno.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
			lStatoUno.setEveIdEvento(aEvento.getIdEvento());
			lStatoUno.setCodOperatoreInserimento(aNotifica.getCodOperatoreInserimento());
			lStatoUno.setDataInserimento(aNotifica.getDataAggiornamento());
			lStatoUno.setCodUfficioInserimento(aNotifica.getCodUfficioInserimento());

			lStatoDao.setDAOFromModel(lStatoUno);
			lStatoDao.insert();
			lStatoDao.stop();

			commit(lConn);

			EventoNotificaModel lEveNotMod = new EventoNotificaModel();
			lEveNotMod.setEvento(aEvento);

			NotificaModel lNotifiche[] = new NotificaModel[1];
			lNotifiche[0] = aNotifica;

			lEveNotMod.setNotifiche(lNotifiche);

			return lEveNotMod;
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExConfermaTrasmissione: " + ex);
		} catch (Exception ex) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			throw new F3BException("EventoController.ExConfermaTrasmissione: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lNotDao);
			cleanup(lStatoDao);
			cleanup(lStatoSqlDao);

			cleanup(lConn);
		}
	}

	/*
	 * modifica 31-03-05 -- Dario -- aStatoProcedimento serve per capire se va inserito lo stato del
	 * procedimento a "13" e va inserito se provengo da ActConfermaTrasmissioneProvvedimentoDS e
	 * ActConfermaTrasmissioneProvvedimentoLS invece va inserito a "0159" se provengo da
	 * ActUploadDocumentConfermaTrasmissione
	 */
	public EventoNotificaModel ExConfermaTrasferisciIstanza(EventoModel aEvento, NotificaModel aNotifica,
			boolean aStatoProcedimento) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NotificaDAO lNotDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;
		ScadenzarioDAO lScaDao = null;

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);

			lEveDao.setDataTrasmissioneAtti(aEvento.getDataTrasmissioneAtti());
			if (aEvento.getCodTipoUfficioDestinatario() != null)
				lEveDao.setCodTipoUfficioDestinatario(aEvento.getCodTipoUfficioDestinatario());
			lEveDao.setCodUfficioDestinatario(aEvento.getCodUfficioDestinatario());
			lEveDao.setCodLuogoDestinatario(aEvento.getCodLuogoDestinatario());
			// 04/03/2011 Nuova impostazione Codice Esito.
			lEveDao.setCodEsito("34"); // TRASFERITA AD ALTRA AUTORITA'
			if (aEvento.getCodTipoUfficioDestinatario() != null) {
				String tipoUffDest = aEvento.getCodTipoUfficioDestinatario().trim();
				if (tipoUffDest.compareToIgnoreCase("TDS") == 0)
					lEveDao.setCodEsito("35"); // TRASFERITA A TDS
				else if (tipoUffDest.compareToIgnoreCase("UDS") == 0)
					lEveDao.setCodEsito("37"); // TRASFERITA A UDS
			}

			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.update();

			/*----- GDV levata temporaneamente perche' e' stata fatta nella ActTrasferisciIstanza
			       lNotDao = new NotificaDAO(lConn);
			       lNotDao.setDAOFromModel(aNotifica);
			       BigDecimal lKey = null;
			       lKey = lNotDao.insert();
			       aNotifica.setIdNotifica(lKey);*/
			//

			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoIstanzaByKey(aEvento.getIdEvento());
			aEvento = (EventoModel) lEveSqlDao.getModelByKey();

			// ** STATO_PROCEDIMENTO **
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);
			BigDecimal codStatoMax = lStatoSqlDao.getProgressivo(aEvento.getFasSieIdFascicoloSiep());

			if (aStatoProcedimento) {
				// Atti trasmessi al tds
				StatoProcedimentoModel lStatoTre = new StatoProcedimentoModel();
				lStatoTre.setProgressivo(new BigDecimal(codStatoMax.intValue() + 1));
				lStatoTre.setCodStatoProcedimento("0013"); // Atti Trasmessi al TDS (default)

				// 16/05/2016 Modifica per integrazione MEV2
				if (aEvento.getCodTipoUfficioDestinatario() != null) {
					if (aEvento.getCodTipoUfficioDestinatario().compareToIgnoreCase("TDSM") == 0)
						lStatoTre.setCodStatoProcedimento("0554"); // Atti Trasmessi all' TDS Minori
					else if (aEvento.getCodTipoUfficioDestinatario().compareToIgnoreCase("UDS") == 0)
						lStatoTre.setCodStatoProcedimento("0410"); // Atti Trasmessi all' UDS
					else if (aEvento.getCodTipoUfficioDestinatario().compareToIgnoreCase("UDSM") == 0)
						lStatoTre.setCodStatoProcedimento("0555"); // Atti Trasmessi all' UDS Minori
				}
				// 16/05/2016 Fine Modifica per integrazione MEV2

				lStatoTre.setData(aNotifica.getDataInserimento());
				lStatoTre.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());

				/*
				 * 24/03/2011 recupero dati dall'EVENTO aggiornato.
				 * lStatoTre.setCodOperatoreInserimento(aNotifica.getCodOperatoreInserimento());
				 * lStatoTre.setDataInserimento(aNotifica.getDataAggiornamento());
				 * lStatoTre.setCodUfficioInserimento(aNotifica.getCodUfficioInserimento());
				 */
				lStatoTre.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lStatoTre.setDataInserimento(aEvento.getDataAggiornamento());
				lStatoTre.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());

				lStatoDao.setDAOFromModel(lStatoTre);
				lStatoDao.insert();
			} else {
				// Trasmessa istanza
				StatoProcedimentoModel lStatoUno = new StatoProcedimentoModel();
				lStatoUno.setProgressivo(new BigDecimal(codStatoMax.intValue() + 1));

				// 04/03/2011 Nuova Impostazione CodStatoProcedimento x Trasferimento Istanza.
				lStatoUno.setCodStatoProcedimento("0159"); // Trasmessa istanza al TDS
				if (aEvento.getCodTipoUfficioDestinatario() != null) {
					String tipoUffDest = aEvento.getCodTipoUfficioDestinatario().trim();
					if (tipoUffDest.compareToIgnoreCase("UDS") == 0)
						lStatoUno.setCodStatoProcedimento("0413"); // Trasmessa Istanza all' UDS
					else if (tipoUffDest.compareToIgnoreCase("PM") == 0)
						lStatoUno.setCodStatoProcedimento("0414"); // Trasmessa Istanza alla Procura c/o
																	// Tribunale
					else if (tipoUffDest.compareToIgnoreCase("PGCAP") == 0)
						lStatoUno.setCodStatoProcedimento("0415"); // Trasmessa Istanza alla Procura Generale
					else if (tipoUffDest.compareToIgnoreCase("CAP") == 0)
						lStatoUno.setCodStatoProcedimento("0416"); // Trasmessa Istanza alla Corte d'Appello
					else if (tipoUffDest.compareToIgnoreCase("CAS") == 0)
						lStatoUno.setCodStatoProcedimento("0417"); // Trasmessa Istanza alla Corte d'Assise
					else if (tipoUffDest.compareToIgnoreCase("CASAP") == 0)
						lStatoUno.setCodStatoProcedimento("0418"); // Trasmessa Istanza alla Corte d'Assise
																	// d'Appello
					else if (tipoUffDest.compareToIgnoreCase("CAPMI") == 0)
						lStatoUno.setCodStatoProcedimento("0419"); // Trasmessa Istanza alla Corte Militare
																	// d'Appello
					else if (tipoUffDest.compareToIgnoreCase("CAPMID") == 0)
						lStatoUno.setCodStatoProcedimento("0420"); // Trasmessa Istanza alla Corte Militare
																	// d'Appello - Sez. Distaccata
					else if (tipoUffDest.compareToIgnoreCase("CSS") == 0)
						lStatoUno.setCodStatoProcedimento("0421"); // Trasmessa Istanza alla Corte Suprema di
																	// Cassazione
					else if (tipoUffDest.compareToIgnoreCase("GIPMI") == 0)
						lStatoUno.setCodStatoProcedimento("0422"); // Trasmessa Istanza al GIP presso
																	// Tribunale Militare
					else if (tipoUffDest.compareToIgnoreCase("GIP") == 0)
						lStatoUno.setCodStatoProcedimento("0423"); // Trasmessa Istanza al GIP presso
																	// Tribunale Ordinario
					else if (tipoUffDest.compareToIgnoreCase("GIPM") == 0)
						lStatoUno.setCodStatoProcedimento("0424"); // Trasmessa Istanza al GIP presso
																	// Tribunale per i Minorenni
					else if (tipoUffDest.compareToIgnoreCase("GP") == 0)
						lStatoUno.setCodStatoProcedimento("0425"); // Trasmessa Istanza al Giudice di Pace
					else if (tipoUffDest.compareToIgnoreCase("GUPMI") == 0)
						lStatoUno.setCodStatoProcedimento("0426"); // Trasmessa Istanza al GUP presso
																	// Tribunale Militare
					else if (tipoUffDest.compareToIgnoreCase("GUP") == 0)
						lStatoUno.setCodStatoProcedimento("0427"); // Trasmessa Istanza al GUP presso
																	// Tribunale Ordinario
					else if (tipoUffDest.compareToIgnoreCase("GUPM") == 0)
						lStatoUno.setCodStatoProcedimento("0428"); // Trasmessa Istanza al GUP presso
																	// Tribunale per i Minorenni
					else if (tipoUffDest.compareToIgnoreCase("PT") == 0)
						lStatoUno.setCodStatoProcedimento("0429"); // Trasmessa Istanza alla Pretura
					else if (tipoUffDest.compareToIgnoreCase("TRIBSD") == 0)
						lStatoUno.setCodStatoProcedimento("0430"); // Trasmessa istanza alla Sezione
																	// Distaccata di Tribunale
					else if (tipoUffDest.compareToIgnoreCase("CAPSM") == 0)
						lStatoUno.setCodStatoProcedimento("0431"); // Trasmessa istanza alla Sezione Minorenni
																	// per la Corte d'Appello
					else if (tipoUffDest.compareToIgnoreCase("TMI") == 0)
						lStatoUno.setCodStatoProcedimento("0432"); // Trasmessa istanza al Tribunale Militare
					else if (tipoUffDest.compareToIgnoreCase("TMIDS") == 0)
						lStatoUno.setCodStatoProcedimento("0433"); // Trasmessa istanza al Tribunale Militare
																	// di Sorveglianza
					else if (tipoUffDest.compareToIgnoreCase("DIB") == 0)
						lStatoUno.setCodStatoProcedimento("0434"); // Trasmessa istanza al Tribunale Ordinario
					else if (tipoUffDest.compareToIgnoreCase("DIBM") == 0)
						lStatoUno.setCodStatoProcedimento("0435"); // Trasmessa istanza al Tribunale per i
																	// Minorenni
				}

				lStatoUno.setData(aNotifica.getDataInserimento());
				lStatoUno.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());

				/*
				 * 24/03/2011 recupero dati dall'EVENTO aggiornato.
				 * lStatoUno.setCodOperatoreInserimento(aNotifica.getCodOperatoreInserimento());
				 * lStatoUno.setDataInserimento(aNotifica.getDataAggiornamento());
				 * lStatoUno.setCodUfficioInserimento(aNotifica.getCodUfficioInserimento());
				 */
				lStatoUno.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lStatoUno.setDataInserimento(aEvento.getDataAggiornamento());
				lStatoUno.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());

				lStatoDao.setDAOFromModel(lStatoUno);
				lStatoDao.insert();
				lStatoDao.stop();
			}

			// *************** Cancella Scadenzario Ordine di Esecuzione ********************
			lScaDao = new ScadenzarioDAO(lConn);

			// Seleziona Tutti gli scadenzari di tipo LEGGE SIMEONE -- 01 --
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aEvento.getFasSieIdFascicoloSiep(), "01");
			lScaDao.delete();

			commit(lConn);

			EventoNotificaModel lEveNotMod = new EventoNotificaModel();
			lEveNotMod.setEvento(aEvento);

			NotificaModel lNotifiche[] = new NotificaModel[1];
			lNotifiche[0] = aNotifica;

			lEveNotMod.setNotifiche(lNotifiche);

			return lEveNotMod;
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExConfermaTrasferisciIstanza: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			throw new F3BException("EventoController.ExConfermaTrasferisciIstanza: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lNotDao);
			cleanup(lStatoDao);
			cleanup(lStatoSqlDao);
			cleanup(lScaDao);

			cleanup(lConn);
		}
	}

	/**
	 * Modifica del Documento Stampato...
	 *
	 * @param aEvento
	 * @throws F3BException
	 */
	public void ExModificaEvento(EventoModel aEvento) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(aEvento);
			lEveDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExModificaEvento: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("EventoController.ExModificaEvento: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
	}

	/**
	 * Aggiornameto data ricezione Atti Istruttori
	 *
	 * @param aEvento
	 *            : ArrayList
	 * @throws F3BException
	 */
	public void ExAggiornaDateRicezioneEvento(ArrayList aEventi) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		int lLungLista = aEventi.size();
		int i;

		try {
			lConn = getDBTransaction();
			lEveDao = new EventoDAO(lConn);
			for (i = 0; i < lLungLista; i++) {
				lEveDao.setDAOFromModelForUpdateDataRicezione((EventoModel) aEventi.get(i));
				lEveDao.update();
				lEveDao.stop();
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExAggiornaDateRicezioneEvento: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("EventoController.ExAggiornaDateRicezioneEvento: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
	}

	/**
	 * Cancellaizone Evento
	 *
	 * @param aEvento
	 * @throws F3BException
	 */
	public void ExCancellaEvento(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("EventoController.ExCancellaEvento: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("EventoController.ExCancellaEvento: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
	}

	/**
	 * Stampa un documento di Ordine d'esecuzione per condannato Libero
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumento(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			EventoNotificaModel lEveMod = this
					.ExRicercaEventoNotificaByKey(aEvento.getEvento().getIdEvento());

			lEveMod.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());
			// QUI setto l'id del template con il nemo vero e proprio
			lEveMod.getEvento().setTemIdTemplate(lNomeTemplate);

			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			TreeModel lTree = lStampa.prelevaDatiEventoSiep(lEveMod, aUtente);
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
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" EventoController - ---------> DaoException" + daoEx, daoEx);
			throw new F3BException("EventoController.ExRicercaTemplateByCodMotivo: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("EventoController - -------> Exception: " + e, e);
			throw new F3BException("EventoController.ExStampaDocumento: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Effettua l'operazione di update di un documento mandato tramite upload
	 *
	 * @param aEvento
	 * @throws F3BException
	 */

	public EventoModel ExUpdateDocument(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		try {
			lConn = getDBTransaction();
			lEveDao = new EventoDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);

			// Validazione evento
			lEveDao.setDAOFromModelForUpdateBlob(aEvento);
			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.update();
			lEveDao.stop();

			// ricerca evento
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();
			lEveSqlDao.stop();

			// valido l'evento riferito al decreto/ordinanza
			if (lEveModel != null && lEveModel.getEveIdEvento() != null) {
				lEveSqlDao.ricercaEventoByKey(lEveModel.getEveIdEvento());
				EventoModel lEveModelOD = (EventoModel) lEveSqlDao.getModelByKey();

				if (lEveModelOD != null && (lEveModelOD.getFlagDocumentoRegistrato() == null
						|| lEveModelOD.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelOD.setFlagDocumentoRegistrato("S");
					lEveModelOD.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelOD.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelOD.setDataAggiornamento(DateUtils.getSysDate());

					lEveDao.setDAOFromModelForUpdate(lEveModelOD);
					lEveDao.update();
					lEveDao.stop();
				}
			}

			// MEV_2023-33: aggiunto ricalcolo della pena
			if ("1312".equals(lEveModel.getCodMotivo()) || "1313".equals(lEveModel.getCodMotivo()))
				ricalcoloPena(lConn, lEveModel.getFasSieIdFascicoloSiep());

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("EventoController.ExUpdateDocument: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("EventoController.ExUpdateDocument: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}
		return lEveMod;
	}

	/**
	 * @author sgioggi
	 * @since MEV_2023-33
	 *
	 *        Metodo privato che esegue il ricalcolo della pena solo per: 1312 Al Mds per l'esecuzione di pene
	 *        sostitutive 1313 Al Mds per l'esecuzione di pene sostitutive a seguito restituzione
	 *
	 * @param lConn
	 * @param idFascicoloSiep
	 * @throws Exception
	 */
	private void ricalcoloPena(Connection lConn, BigDecimal idFascicoloSiep) throws Exception {

		PenaResiduaSqlDAO prsdao = null;

		PenaResiduaModel prm = null;

		try {
			prsdao = new PenaResiduaSqlDAO(lConn);
			prsdao.ricercaPenaResiduaByIdFascicoloDataDesc(idFascicoloSiep);
			prm = (PenaResiduaModel) prsdao.getModelByKey();
			prsdao.inserisciOModificaPenaResidua(prm);
		} finally {
			cleanup(prsdao);
		}
	}

	/**
	 * Effettua l'operazione di update del campo FLAG_DOCUMENTO_REGISTRATO.
	 *
	 * @param aEvento
	 * @throws F3BException
	 */
	public void ExAggiornaValidazione(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lEveDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lEveDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lEveDao.setFlagDocumentoRegistrato(aEvento.getFlagDocumentoRegistrato());
			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("ExAggiornaValidazione.ExUpdateDocument: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ExAggiornaValidazione.ExUpdateDocument: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return;
	}

	/**
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream
	 *
	 * @param aProvvedimento
	 * @return Array con il Documento recuperato dal DB
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExGetDocumento(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		ByteArrayOutputStream lByteArrayOut = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start(1);

			if (lEveDao.next())
				lByteArrayOut = lEveDao.getDocBlob();

			lEveDao.stop();

			if ((lByteArrayOut == null) || (lByteArrayOut.size() == 0))
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Documento Associato");
		} catch (F3BException eF3b) {
			throw eF3b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	// DECRETO SOSPENSIONE
	public EventoModel ExRicercaEventoByFascicoloSiepDecretoSospensione(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoModel lEvento = new EventoModel();
		EventoSqlDAO lDao = null;

		try {
			lConn = getDBConnection();
			lDao = new EventoSqlDAO(lConn);
			lDao.ricercaOrdineEsecuzioneByIdFascicoloDecretoSospensione(aIdFascicolo);
			lEvento = (EventoModel) lDao.getModelByKey();

			if (lEvento == null) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Ordine Esecuzione non applicato per il fascicolo");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSiepDecretoSospensione: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoByFascicoloSiepDecretoSospensione: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	// DECRETO SOSPENSIONE
	public EventoModel ExRicercaEventoByFascicoloSiepOESospensione(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;

		EventoModel lEvento = new EventoModel();
		EventoSqlDAO lDao = null;

		try {
			lConn = getDBConnection();
			lDao = new EventoSqlDAO(lConn);
			lDao.ricercaOrdineEsecuzioneByIdFascicoloDecretoSospensione(aIdFascicolo);
			lEvento = (EventoModel) lDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoByFascicoloSiepOESospensione: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoByFascicoloSiepOESospensione: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	/**
	 *
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoTipoMotProvEveDocReg(EventoModel aModel) throws F3BException {

		Connection lConn = null;

		EventoModel lEvento = new EventoModel();
		EventoSqlDAO lDao = null;
		AnnotazioneManualeSqlDAO lAnnManSqlDao = null;
		AnnotazioneManualeDAO lAnnDao = null;

		try {
			lConn = getDBConnection();
			lDao = new EventoSqlDAO(lConn);
			lAnnDao = new AnnotazioneManualeDAO(lConn);
			lAnnManSqlDao = new AnnotazioneManualeSqlDAO(lConn);

			lDao.ricercaEventoTipoMotProvEveDocReg(aModel);
			lEvento = (EventoModel) lDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSiepDecretoSospensione: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoByFascicoloSiepDecretoSospensione: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lAnnManSqlDao);
			cleanup(lAnnDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	/**
	 * Ricerca gli eventi eventi in base a idFascicolo, tipoEvento, tipoProvvedimento, motivoProvvedimento
	 * recuperati dal model. Ordinati per data Inserimento decrescente
	 *
	 * @param aModel
	 *            - EventoModel con i parametri per la ricerca
	 * @param lFlagDocReg
	 *            se N o non specificato ricerca gli eventi non validati se S solo quelli non validati
	 * @return Vettore di EventoModel
	 * @throws F3BException
	 *             se errore o nessun elemento trovato
	 */
	public Vector ExRicercaEventoTipoEveTipoProvMot(EventoModel aModel, String lFalgDocReg)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lDao = null;
		Vector lEventi = new Vector();

		try {
			lConn = getDBConnection();
			lDao = new EventoSqlDAO(lConn);

			lDao.ricercaEventoTipoEveTipoProvMot(aModel, lFalgDocReg);
			lEventi = new Vector(lDao.getModels());
			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoTipoEveTipoProvMot: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoTipoEveTipoProvMot: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	// ricerca ordinanza di revoca simeone
	public EventoModel ExRicercaRevocaOrdinanzaAcquisitaDecretoSospensioneByFascicoloSiep(
			BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;

		EventoModel lEvento = new EventoModel();
		EventoSqlDAO lDao = null;

		try {
			lConn = getDBConnection();

			lDao = new EventoSqlDAO(lConn);
			lDao.ricercaRevocaOrdinanzaAcquisitaByIdFascicolo(aIdFascicolo);
			lEvento = (EventoModel) lDao.getModelByKey();

			if (lEvento == null) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Legge Simeone non applicata per il fascicolo");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSiepDecretoSospensione: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoByFascicoloSiepDecretoSospensione: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lEvento;
	}

	/**
	 *
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoByFascicoloSiep(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;

		Vector lEvento = new Vector();
		EventoSqlDAO lDao = null;

		try {
			lConn = getDBConnection();
			lDao = new EventoSqlDAO(lConn);
			lDao.ricercaOrdineEsecuzioneByIdFascicolo(aIdFascicolo);

			lEvento = new Vector(lDao.getModels());
			if (lEvento.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEsecuzioneByFascicoloSiep: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEsecuzioneByFascicoloSiep: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	public Vector ExRicercaEventoStatoEsecuzioneByFascicoloSiep(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;

		Vector lEvento = new Vector();
		EventoSqlDAO lDao = null;

		try {
			lConn = getDBConnection();

			lDao = new EventoSqlDAO(lConn);

			lDao.ricercaOrdineEsecuzioneSoloEventiVisualizzazioneByIdFascicolo(aIdFascicolo);

			lEvento = new Vector(lDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoStatoEsecuzioneByFascicoloSiep: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoStatoEsecuzioneByFascicoloSiep: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lEvento;
	}

	public EventoNotificaModel ExRicercaEventoByKeyPerMotivo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		EventoNotificaModel lEve = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoByKeyPerMotivo(aKey);
			lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKey: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			e.printStackTrace();
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKey: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEve;
	}

	public EventoNotificaModel ExRicercaEventoNotificaCondannatoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		EventoNotificaModel lEve = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		UfficioSqlDAO lUffDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		AvvocatoFascicoloSiepPerEventoSqlDAO lAvvDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		MagistratoSqlDAO lMagDAO = null;
		try {
			lConn = getDBConnection();

			lEveDao = new EventoSqlDAO(lConn);
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			// modifica relativa al tipo istituto
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);

			// GDV--- Modificata la Ricerca del Magistrato!
			lMagDAO = new MagistratoSqlDAO(lConn);

			lEveDao.ricercaEventoByKeyCondannato(aKey);

			lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());

			if (lEve.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Evento trovato");

			if (lEve.getEvento().getCodMagistrato() != null) {
				// ***** GDV direttamente sul Magistrato al posto di andare sul MAgistratoCompetente ******
				lMagDAO.ricercaMagistratoByCod(lEve.getEvento().getCodMagistrato());
				MagistratoModel lMag = (MagistratoModel) lMagDAO.getModelByKey();
				lEve.setMagistrato(lMag);
			}

			lUffDao = new UfficioSqlDAO(lConn);
			lAutoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);
			lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEventoCondannato(lEve.getEvento().getIdEvento());
			Vector lNotifiche = new Vector(lNotEveDao.getModels());
			if (lNotifiche == null || lNotifiche.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Notifica trovato");

			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

			Vector lAvvocati = new Vector();
			// Verfica ed inserisce le Autorita Esterne e gli uffici
			int count = 0;
			while (count < lEve.getNotifiche().length) {
				// Autorita Esterne
				if (lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna() != null) {
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(
							lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setAutoritaEsterna(lAutorita);
					lAutoritaSqlDao.stop();
				}

				// Preleva gli uffici
				if (lEve.getNotifiche()[count].getUffCodUfficio() != null) {
					lUffDao.selUfficioByCod(lEve.getNotifiche()[count].getUffCodUfficio());
					UfficioModel lUffMod = (UfficioModel) lUffDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lEve.getNotifiche()[count].setUfficio(lUffMod);
					lUffDao.stop();
				}
				// modifica relativa al tipo istituto
				if (lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione() != null
						&& !lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione().equals("")) {
					lIstDao.ricercaIstitutoDetenzioneByKey(
							lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione());
					IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setIstitutoDetenzione(lIstituto);
					lIstDao.stop();
				}
				// fine modifica relativa al tipo istituto

				// Preleva gli avvocati
				if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep() != null) {
					lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);
					lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSiep(
							lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep());
					lAvvocati.add(lAvvDao.getModelByKey());
				}

				count++;
			}

			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

			if (lAvvocati.size() > 0)
				lEve.setAvvocati((AvvocatoSiepModel[]) lAvvocati.toArray(new AvvocatoSiepModel[0]));
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKey: Non posso leggere : " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKey: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lUffDao);
			cleanup(lAutoritaSqlDao);
			cleanup(lAvvDao);
			cleanup(lMagDAO);
			cleanup(lNotEveDao);
			cleanup(lIstDao);
			cleanup(lConn);
		}
		return lEve;
	}

	public EventoNotificaModel ExRicercaEventoNotificaDifensoreByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		EventoNotificaModel lEve = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		UfficioSqlDAO lUffDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		AvvocatoFascicoloSiepPerEventoSqlDAO lAvvDao = null;
		MagistratoSqlDAO lMagDAO = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lMagDAO = new MagistratoSqlDAO(lConn);

			lEveDao.ricercaEventoByKeyCondannato(aKey);
			lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());

			if (lEve.getEvento() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Evento trovato");

			if (lEve.getEvento().getCodMagistrato() != null) {
				// ***** GDV direttamente sul Magistrato al posto di andare sul MAgistratoCompetente ******
				lMagDAO.ricercaMagistratoByCod(lEve.getEvento().getCodMagistrato());
				MagistratoModel lMag = (MagistratoModel) lMagDAO.getModelByKey();
				lEve.setMagistrato(lMag);
			}

			lUffDao = new UfficioSqlDAO(lConn);
			lAutoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);
			lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);

			lNotEveDao.ricercaNotificaByEventoDifensore(lEve.getEvento().getIdEvento());
			Vector lNotifiche = new Vector(lNotEveDao.getModels());
			if (lNotifiche == null || lNotifiche.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Notifica trovata");

			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

			Vector lAvvocati = new Vector();
			// Verfica ed inserisce le Autorita Esterne e gli uffici
			int count = 0;
			while (count < lEve.getNotifiche().length) {
				// Autorita Esterne
				if (lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna() != null) {
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(
							lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lEve.getNotifiche()[count].setAutoritaEsterna(lAutorita);
					lAutoritaSqlDao.stop();
				}

				// Preleva gli uffici
				if (lEve.getNotifiche()[count].getUffCodUfficio() != null) {
					lUffDao.selUfficioByCod(lEve.getNotifiche()[count].getUffCodUfficio());
					UfficioModel lUffMod = (UfficioModel) lUffDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lEve.getNotifiche()[count].setUfficio(lUffMod);
					lUffDao.stop();
				}

				// Preleva gli avvocati
				if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep() != null) {
					lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);
					lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSiep(
							lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep());
					lAvvocati.add(lAvvDao.getModelByKey());
				}
				count++;
			}

			lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

			if (lAvvocati.size() > 0)
				lEve.setAvvocati((AvvocatoSiepModel[]) lAvvocati.toArray(new AvvocatoSiepModel[0]));
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKey: Non posso leggere : " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKey: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lUffDao);
			cleanup(lAutoritaSqlDao);
			cleanup(lAvvDao);
			cleanup(lMagDAO);
			cleanup(lNotEveDao);
			cleanup(lConn);
		}
		return lEve;
	}

	public EventoModel ExRicercaEventoByDataInserimentoUguale(EventoModel lEveModel) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		EventoModel lEveMod = new EventoModel();

		try {
			lConn = getDBConnection();

			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoByDataInserimentoUguale(lEveModel);
			lEveMod = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByDataInserimentoUguale: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByDataInserimentoUguale: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * Ricerca l'evento per Id evento
	 *
	 * @param aKey
	 *            id dell'evento
	 * @return Model dell'evento
	 */
	public EventoModel ExRicercaEventoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		EventoModel lEveMod;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoByKey(aKey);
			lEveMod = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoByKey: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoByKey: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEveMod;
	}

	/**
	 * Ricerca l'evento per Id evento X conversione pene pec
	 *
	 * @param aKey
	 *            id dell'evento
	 * @return Model dell'evento
	 */
	public EventoModel ExRicercaEventoByKeyTenore(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		EventoModel lEveMod;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoByKeyTenore(aKey);
			lEveMod = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoByKeyTenore: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoByKeyTenore: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEveMod;
	}

	public EventoModel ExRicercaEventoEsitoParereInamm(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		EventoModel lEveMod;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoEsitoParereInamm(aKey);
			lEveMod = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoEsitoParereInamm: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoEsitoParereInamm: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEveMod;
	}

	public EventoModel ExRicercaEventoIstanzaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveDao = null;
		EventoModel lEveMod;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoIstanzaByKey(aKey);
			lEveMod = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaEventoIstanzaByKey: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoIstanzaByKey: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEveMod;
	}

	public TemplateModel ExRicercaTemplateByCodMotivo(String aCodMotivo) throws F3BException {

		TemplateSqlDAO lTemplateSqlDao = null;
		TemplateModel lTemplateMod = null;
		Connection lConn = null;
		try {
			lConn = getDBConnection();
			lTemplateSqlDao = new TemplateSqlDAO(lConn);
			lTemplateSqlDao.ricercaTemplateByCodMotivo(aCodMotivo);
			lTemplateMod = (TemplateModel) lTemplateSqlDao.getModelByKey();
			if (lTemplateMod == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Template non trovato per il codice motivo richiesto : " + aCodMotivo);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaTemplateByCodMotivo: Non posso leggere : " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception Ex) {
			throw new F3BException(
					"EventoController.ExRicercaTemplateByCodMotivo: Non posso leggere : " + Ex);
		} finally {
			cleanup(lTemplateSqlDao);
			cleanup(lConn);
		}

		return lTemplateMod;
	}

	public EventoModel ExRicercaUltimoTipoEventoByIdFascicolo(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		EventoModel lEvento = null;
		EventoSqlDAO lEveDao = null;
		try {
			lConn = getDBConnection();

			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoTipoMotProvEveDataEmis(aEvento);
			lEvento = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaUltimoTipoEventoByIdFascicolo: " + daoEx);
		} catch (Exception Ex) {
			throw new F3BException("EventoController.ExRicercaUltimoTipoEventoByIdFascicolo: " + Ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEvento;
	}

	/**
	 *
	 * @param @return
	 * @throws
	 */
	public EventoModel ExRicercaUltimoEventoGeneratoByCodUtente(String aCodUtente) throws F3BException {

		Connection lConn = null;

		EventoModel lEvento = null;
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaUltimoEventoGeneratoByCodUtente(aCodUtente);
			lEvento = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaUltimoTipoEventoByIdFascicolo: " + daoEx);
		} catch (Exception Ex) {
			throw new F3BException("EventoController.ExRicercaUltimoTipoEventoByIdFascicolo: " + Ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEvento;
	}

	/**
	 * Ricerca l'ultimo evento inserito per fascicolo sius
	 *
	 * @param aKey
	 *            Fascicolo Sius
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaUltimoByFasSius(String aKey) throws F3BException {

		Connection lConn = null;

		EventoModel lEvento = null;
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaUltimoEventoByFasSius(aKey);
			lEvento = (EventoModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {

			throw new F3BException("EventoController.ExRicercaUltimoByFasSius: " + daoEx);
		} catch (Exception Ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + Ex);
			throw new F3BException("EventoController.ExRicercaUltimoByFasSius: " + Ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEvento;
	}

	/**
	 * Estrae il BLOB dall'EVENTO e lo restituisce come Byte Array
	 *
	 * @param aIdEvento
	 * @return byte[]
	 * @throws F3BException
	 */
	public byte[] ExGetDocPerTrasferimento(BigDecimal aIdEvento) throws F3BException {

		byte[] lDocPerTrasferimento = null;

		Connection lConn = null;
		EventoDAO lEveDao = null;
		ByteArrayOutputStream lByteArrayOut = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);

			lEveDao.setIdEvento(aIdEvento);
			lEveDao.selByKey();

			lEveDao.start(1);

			if (lEveDao.next())
				lByteArrayOut = lEveDao.getDocBlob();
			lEveDao.stop();

			if (lByteArrayOut != null && lByteArrayOut.size() > 0)
				lDocPerTrasferimento = lByteArrayOut.toByteArray();
		} catch (Exception e) {
			throw new F3BException(F3BException.EX_OPERATION_FAILED, e.toString());
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lDocPerTrasferimento;
	}

	/**
	 * Effettua l'update del campo FLAG_DOCUMENTO_REGISTRATO ad 'A' ed inserisce le motivazioni
	 * dell'annullamento in Campo_nota.
	 *
	 * @param aEvento
	 * @throws F3BException
	 */
	public void ExAnnullaEventoInserisciCampoNota(CampoNotaModel aCampoNota) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		CampoNotaDAO lCampoNotaDao = null;
		LicenzaLibanticipataDAO lLicLibAntDao = null;
		UdienzaProcedimentoDAO lUdiProDao = null;
		UdienzaProcedimentoSqlDAO lUdiProSqlDao = null;
		GeneraleProcedimentoDAO lGenProDao = null;
		RichiestaConversioneDAO lRicConvDao = null;
		ScambioSanzioneDAO lScaSanDao = null;
		MisuraSicurezzaDAO lMisSicDao = null;
		EsecuzioneMisuraSicurezzaDAO lEseMisSicDao = null;
		DepositoOrdinanzaPcSqlDAO lDOPSqlDAO = null;
		PeriodoAltraMisuraDAO lPAMDao = null;
		DocumentoAllegatoDAO lDocAllDAO = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDAO = null;

		try {
			lConn = getDBTransaction();

			// Inserimento nuovo record CampoNota
			// 08/02/2006 Solo se la nota e' stata valorizzata.
			if (aCampoNota.getDescr() != null && aCampoNota.getDescr().trim().length() > 0) {
				lCampoNotaDao = new CampoNotaDAO(lConn);
				aCampoNota.setProgressivo(new BigDecimal(1));
				lCampoNotaDao.setDAOFromModel(aCampoNota);
				BigDecimal lIdCampoNota = lCampoNotaDao.insert();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserito Campo_NOTA : " + lIdCampoNota);
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
			siesLogger.debug("Aggiornato Evento : " + aCampoNota.getEveIdEvento());

			// Nel caso in cui il Provvedimento sia un provvedimento di Revoca
			// si effettua la cancellazione dei riferimenti in tabella Evento
			// al record annullato attraverso EVE_ID_EVENTO_REVOCA
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

			// Eventuale Update LICENZA_LIBANTICIPATA
			lLicLibAntDao = new LicenzaLibanticipataDAO(lConn);
			lLicLibAntDao.setDataAggiornamento(aCampoNota.getDataInserimento());
			lLicLibAntDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
			lLicLibAntDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
			lLicLibAntDao.setFlagElaborato("A");
			lLicLibAntDao.setCondizioneIdEvento(aCampoNota.getEveIdEvento());
			lLicLibAntDao.update();
			lLicLibAntDao.stop();

			// Ricerca Evento modificato
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aCampoNota.getEveIdEvento());
			EventoModel lEve = (EventoModel) lEveSqlDAO.getModelByKey();

			// 09-03-2009 Eventuale Cancellazione di SCAMBIO_SANZIONE e aggiornamento di RICHIESTA_CONVERSIONE
			// in caso di Annullamento Ordinanza di Conversione Pene Pecuniarie.
			if (lEve.getCodMotivo().compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_CONVERSIONE) == 0 || lEve
					.getCodMotivo().compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_RATEIZZAZIONE) == 0) {
				lRicConvDao = new RichiestaConversioneDAO(lConn);
				RichiestaConversioneModel lRCModel = new RichiestaConversioneModel();
				lRCModel.setEveIdEvento(lEve.getIdEvento());
				lRCModel.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				lRCModel.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				lRCModel.setDataAggiornamento(aCampoNota.getDataInserimento());
				lRicConvDao.setDAOFromModelForCancOrdinanzaCPP(lRCModel);
				lRicConvDao.selCondizioneByIdFasSius(lEve.getFasSiuIdFascicoloSius());
				lRicConvDao.update();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						">>>> Aggiornata RICHIESTA_CONVERSIONE x Annullamento Ordinanza con IdFascicoloSius = "
								+ lEve.getFasSiuIdFascicoloSius());

				// Cancellazione Scambio Sanzione.
				lScaSanDao = new ScambioSanzioneDAO(lConn);

				if (lScaSanDao.esisteScambioSanzionePerEvento(lEve.getIdEvento())) {
					lScaSanDao.setEveIdEvento(lEve.getIdEvento());
					lScaSanDao.selCondizionebyEvento(lEve.getIdEvento());
					lScaSanDao.delete();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							">>>> Eliminata Scambio Sanzione x  Annull. Ordinanza Conv. Pene Pecuniarie con Evento :  "
									+ lEve.getIdEvento());
				}
			}

			// Per le Ordinanze di Applicazione Misure Sicurezza, quando si annulla un'Ordinanza che ha
			// trasformato la misura
			// occorre cancellare la misura generata dall' Ordinanza
			if (lEve.getCodMotivo().compareTo("1125") == 0 || lEve.getCodMotivo().compareTo("1135") == 0
					|| lEve.getCodMotivo().compareTo("1136") == 0
					|| lEve.getCodMotivo().compareTo("1137") == 0
					|| lEve.getCodMotivo().compareTo("1138") == 0
					|| lEve.getCodMotivo().compareTo("1139") == 0
					|| lEve.getCodMotivo().compareTo("1140") == 0
					|| lEve.getCodMotivo().compareTo("1141") == 0
					|| lEve.getCodMotivo().compareTo("1142") == 0
					|| lEve.getCodMotivo().compareTo("1143") == 0
					|| lEve.getCodMotivo().compareTo("1144") == 0
					|| lEve.getCodMotivo().compareTo("1145") == 0
					|| lEve.getCodMotivo().compareTo("1146") == 0
					|| lEve.getCodMotivo().compareTo("1147") == 0
					|| lEve.getCodMotivo().compareTo("1148") == 0
					|| lEve.getCodMotivo().compareTo("2110") == 0
					|| lEve.getCodMotivo().compareTo("2111") == 0
					|| lEve.getCodMotivo().compareTo("2112") == 0
					|| lEve.getCodMotivo().compareTo("2113") == 0
					|| lEve.getCodMotivo().compareTo("2114") == 0
					|| lEve.getCodMotivo().compareTo("2116") == 0
					|| lEve.getCodMotivo().compareTo("2117") == 0
					|| lEve.getCodMotivo().compareTo("2118") == 0
					|| lEve.getCodMotivo().compareTo("2119") == 0) {
				lMisSicDao = new MisuraSicurezzaDAO(lConn);
				lMisSicDao.setCondizioneByDepOrdPC(lEve.getIdEvento());
				lMisSicDao.delete();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						">>>> Cancellate misure sicurezza collegate all'Evento  " + lEve.getIdEvento());
			}

			// Per le Ordinanze di Esecuzione Misure Sicurezza, quando si annulla un'Ordinanza che ha
			// trasformato la misura
			// occorre cancellare la misura generata dall' Ordinanza
			if (lEve.getCodMotivo().compareTo("2440") == 0 || lEve.getCodMotivo().compareTo("2441") == 0
					|| lEve.getCodMotivo().compareTo("2442") == 0
					|| lEve.getCodMotivo().compareTo("2660") == 0
					|| lEve.getCodMotivo().compareTo("2430") == 0) {
				DepositoOrdinanzaPcModel lDOPMod = new DepositoOrdinanzaPcModel();
				lDOPSqlDAO = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDOPSqlDAO.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEve.getIdEvento());
				lDOPMod = (DepositoOrdinanzaPcModel) lDOPSqlDAO.getModelByKey();

				if (lDOPMod != null && lDOPMod.getIdDepositoOrdinanzaPc() != null) {
					lEseMisSicDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
					lEseMisSicDao.setCondizioneDeleteByIdOrdinanza(lDOPMod.getIdDepositoOrdinanzaPc());
					lEseMisSicDao.delete();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							">>>> Cancellate evemtuali esecuzioni misure sicurezza collegate all'Evento  "
									+ lEve.getIdEvento());
				}
			}

			// Per i Decreti di Sospensione Esecuzione Misure Sicurezza, quando si annulla un Decreto che ha
			// sospeso la misura
			// occorrerebbe cancellare il periodo generato dal decreto
			// Questo pero' al momento non viene fatto automaticamente, ma si manda un messaggio di avviso.
			// Per rendere automatica la cancellazione del periodo eliminare il commento seguente!
			/**
			 * if (lEve.getCodMotivo().compareTo("2410")==0 ) { // Cancellazione eventuale Periodo Altra
			 * Misura collegata lPAMDao = new PeriodoAltraMisuraDAO(lConn); // Il Periodo Altra Misura e'
			 * collegato all'evento lPAMDao.setCondizioneByEveIdEvento(lEve.getIdEvento()); lPAMDao.delete();
			 *
			 *
			 * }
			 **/

			// Conteggio dei Provvedimenti rimasti al Fascicolo SIUS dopo l'annullamento
			// int lNumProv = lEveSqlDAO.getNumProvSIUSDEpositati(lEve.getFasSiuIdFascicoloSius());
			// Esclusione dei provvedimenti di sospensione per rimessione atti dal conteggio
			int lNumProv = lEveSqlDAO.getNumProvSIUSDEpositatiNonDefinitori(lEve.getFasSiuIdFascicoloSius());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Num Provv Fascicolo SIUS: " + lNumProv);

			/*
			 * ISSUE MEV : cambio stato fascicolo se annullo un decreto di designazione Magistrato Relatore
			 * Numero MEV : 9 
			 * Autore : Gioggi 
			 * Data : 2 dic 2020 
			 * Branch : MEV_9
			 */
			if (lNumProv < 1) {
				FascicoloSiusSqlDAO fssDAO = new FascicoloSiusSqlDAO(lConn);
				fssDAO.ricercaFascicoloByKey(lEve.getFasSiuIdFascicoloSius());
				FascicoloSiusModel fsm = (FascicoloSiusModel) fssDAO.getModelByKey();
				String codStatoFascicolo = fsm.getCodStatoFascicolo();
				if (ICostantiFascicoloSius.COD_EMESSO_PROVVEDIMENTO.equals(codStatoFascicolo)
						|| "13".equals(codStatoFascicolo)
						|| ICostantiFascicoloSius.COD_EMESSO_DECRETO_DESIGNAZIONE.equals(codStatoFascicolo)
						// d.f.
						|| ICostantiFascicoloSius.COD_EMESSA_ORDINANZA_APPLICAZIONE_PROVVISORIA
								.equals(codStatoFascicolo)) {
					FascicoloSiusDAO lFasSiusDao = new FascicoloSiusDAO(lConn);
					lFasSiusDao.setCodStatoFascicolo(ICostantiFascicoloSius.COD_ISCRITTO);
					lFasSiusDao.setDataAggiornamento(aCampoNota.getDataInserimento());
					lFasSiusDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
					lFasSiusDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
					if (ICostantiFascicoloSius.COD_EMESSO_PROVVEDIMENTO.equals(codStatoFascicolo))
						// Aggiorno il fascicolo a stato_fascicolo = 02 se lo stato attuale e' 07
						lFasSiusDao.setCondizioneUpdateStatoFascicolo(lEve.getFasSiuIdFascicoloSius(),
								ICostantiFascicoloSius.COD_EMESSO_PROVVEDIMENTO);
					else if ("13".equals(codStatoFascicolo))
						// Aggiorno il fascicolo a stato_fascicolo = 02 se lo stato attuale e' 13 (cioe'
						// sospeso)
						lFasSiusDao.setCondizioneUpdateStatoFascicolo(lEve.getFasSiuIdFascicoloSius(), "13");
					else if (ICostantiFascicoloSius.COD_EMESSA_ORDINANZA_APPLICAZIONE_PROVVISORIA
							.equals(codStatoFascicolo)) { // d.f
						// Aggiorno il fascicolo a stato_fascicolo = 22 se lo stato attuale e' 24
						lFasSiusDao
								.setCodStatoFascicolo(ICostantiFascicoloSius.COD_EMESSO_DECRETO_DESIGNAZIONE);
						lFasSiusDao.setCondizioneUpdateStatoFascicolo(lEve.getFasSiuIdFascicoloSius(),
								ICostantiFascicoloSius.COD_EMESSA_ORDINANZA_APPLICAZIONE_PROVVISORIA);
					} else
						// Aggiorno il fascicolo a stato_fascicolo = "02" (iscritto) se lo stato attuale e'
						// "22"
						lFasSiusDao.setCondizioneUpdateStatoFascicolo(lEve.getFasSiuIdFascicoloSius(),
								ICostantiFascicoloSius.COD_EMESSO_DECRETO_DESIGNAZIONE);
					lFasSiusDao.update();
					lFasSiusDao.stop();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.debug("Aggiornamento Stato Fascicolo SIUS: " + lEve.getFasSiuIdFascicoloSius());
					cleanup(lFasSiusDao);
				}
			}
			// se trattasi di Conferma Decisione Magistrato Relatore ci sta sicuramente una ordinanza di
			// applicazione provvisoria di MA
			if ("0271".equals(lEve.getCodEsito())) {
				FascicoloSiusDAO fsdao = new FascicoloSiusDAO(lConn);
				fsdao.setCodStatoFascicolo(
						ICostantiFascicoloSius.COD_EMESSA_ORDINANZA_APPLICAZIONE_PROVVISORIA);
				fsdao.setDataAggiornamento(aCampoNota.getDataInserimento());
				fsdao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				fsdao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				// Aggiorno il fascicolo a stato_fascicolo = "24" se lo stato attuale e' "07"
				fsdao.setCondizioneUpdateStatoFascicolo(lEve.getFasSiuIdFascicoloSius(),
						ICostantiFascicoloSius.COD_EMESSO_PROVVEDIMENTO);
				fsdao.update();
				fsdao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiornamento Stato Fascicolo SIUS: " + lEve.getFasSiuIdFascicoloSius());
				cleanup(fsdao);
				// passo all'aggiornamento del tenore
				// Generale Procedimento
				GeneraleProcedimentoSqlDAO gpsdao = new GeneraleProcedimentoSqlDAO(lConn);
				gpsdao.ricercaGeneraleProcedimentoByIdFas(lEve.getFasSiuIdFascicoloSius());
				GeneraleProcedimentoModel gpm = (GeneraleProcedimentoModel) gpsdao.getModelByKey();
				cleanup(gpsdao);
				// Deposito Ordinanza Pc
				lDOPSqlDAO = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDOPSqlDAO.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEve.getIdEvento());
				DepositoOrdinanzaPcModel dopm = (DepositoOrdinanzaPcModel) lDOPSqlDAO.getModelByKey();
				if (Utils.isNullObj(gpm) || Utils.isNullObj(dopm))
					throw new F3BException(F3BException.USER_MESSAGE, "Tenore NON aggiornabile!");
				TenoreModel tm = new TenoreModel();
				// Valorizzazione dei campi da aggiornare + update
				tm.setDataAggiornamento(aCampoNota.getDataInserimento());
				tm.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				tm.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				tm.setDataFine(aCampoNota.getDataInserimento());
				tm.setGenPridGeneraleProcedimento(gpm.getIdGeneraleProcedimento());
				tm.setDepOpidDepositoOrdinanzaPc(dopm.getIdDepositoOrdinanzaPc());
				TenoreDAO tdao = new TenoreDAO(lConn);
				tdao.setDAOFromModelForUpdateDataFineCM(tm);
				tdao.update();
				tdao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Aggiornamento Tenore: IdGeneraleProcedimento = " + gpm.getIdGeneraleProcedimento()
								+ " ed IdDepositoOrdinanzaPc = " + dopm.getIdDepositoOrdinanzaPc());
				cleanup(tdao);
			}
			// ***** FINE INTERVENTO MEV_9 *****//

			// ------------------------------------------------------------------------
			// Gestione Aggiornamnto Udienza Procedimento
			// Questa parte di codice viene eseguita esclusivamente quando si tratta
			// un'evento / ordinanza rinvio udienza ( 0603 )
			// STUB 15/10/2009 Oppure per un decreto di fissazione udienza ( 0601 )
			// ------------------------------------------------------------------------

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("**** Fase di Manipolazione Udienza Procedimento **** ");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("**** Valore EventoModel lEve " + lEve);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("**** Valore EventoModel lEvento " + lEvento);

			if (lEve.getCodEsito().equals("0603") || lEve.getCodEsito().equals("0601")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Trattasi di Udienza Procedimento ****");
				// Aggiornamento ultimo record rinvio udienza legato
				// all'evento
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Update Udienza Procedimento****");
				lUdiProDao = new UdienzaProcedimentoDAO(lConn);
				lUdiProDao.setCondizioneByIdEvento(lEvento.getIdEvento());
				lUdiProDao.setCodOperatoreAggiornamento(lEvento.getCodOperatoreAggiornamento());
				lUdiProDao.setCodUfficioAggiornamento(lEvento.getCodUfficioAggiornamento());
				lUdiProDao.setFlagRinviata("A");
				lUdiProDao.update();
				lUdiProDao.stop();
				// ---//

				// Rilegge il record dell'udienza procedimento appena modificato, al fine di recuperare
				// l'id procedimento necessario per il recupero del record precedente
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Select Udienza Procedimento****");
				BigDecimal lIdGenPro = null;
				lUdiProDao.setCondizioneByIdEvento(lEvento.getIdEvento());
				lUdiProDao.start();

				if (lUdiProDao.next())
					lIdGenPro = lUdiProDao.getGenPridGeneraleProcedimento();

				lUdiProDao.stop();
				// ---//

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Select Udienza Procedimento SQL DAO ****");
				// Lettura dell'ultimo Record, attraverso l'id generale procedimento
				UdienzaProcedimentoUdiModel lUdiProUdi = null;
				lUdiProSqlDao = new UdienzaProcedimentoSqlDAO(lConn);
				lUdiProSqlDao.ricercaUdienzaProcedimentoUdienzaByGenProByFlagRinviata(lIdGenPro, "'R','M'");
				lUdiProSqlDao.start();

				if (lUdiProSqlDao.next())
					lUdiProUdi = (UdienzaProcedimentoUdiModel) lUdiProSqlDao.getModelConUdienza();

				lUdiProSqlDao.stop();
				// ---//

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** II Update Udienza Procedimento ****");
				// Update UDIENZA_PROCEDIMENTO.
				// STUB 15/10/2009 corretta valorizzazione del "flag Rinviata", leggendo prima l'Evento per
				// testare l'EveIdEvento.
				// lUdiProDao.setFlagRinviata("F");
				if (lUdiProUdi != null && lUdiProUdi.getUdienzaProcedimento() != null
						&& lUdiProUdi.getUdienzaProcedimento().getEveIdEvento() != null) {
					lEveSqlDAO = new EventoSqlDAO(lConn);
					lEveSqlDAO.ricercaEventoByKey(lUdiProUdi.getUdienzaProcedimento().getEveIdEvento());
					lEvento = (EventoModel) lEveSqlDAO.getModelByKey();

					if (lEvento.getIdEvento() != null) {
						if (lEve.getCodEsito().equals("0603"))
							lUdiProDao.setFlagRinviata("S");
						else
							lUdiProDao.setFlagRinviata("F");
					} else
						lUdiProDao.setFlagRinviata("P");

					lUdiProDao.setUdiIdUdienzaRinvio(null);
					lUdiProDao.setCondizioneUpdate(
							lUdiProUdi.getUdienzaProcedimento().getIdUdienzaProcedimento());
					lUdiProDao.update();
					lUdiProDao.stop();
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Update Generale Procedimento ****");
				// Update di GENERALE_PROCEDIMENTO.
				lGenProDao = new GeneraleProcedimentoDAO(lConn);
				if (lUdiProUdi != null) {
					lGenProDao.setDataCameraConsiglio(lUdiProUdi.getDataUdienza());
					lGenProDao.setUdiIdUdienza(lUdiProUdi.getUdienzaProcedimento().getUdiIdUdienza());
				} else {
					lGenProDao.setDataCameraConsiglio(null);
					lGenProDao.setUdiIdUdienza(null);
				}
				lGenProDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioAggiornamento());
				lGenProDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreAggiornamento());
				lGenProDao.setDataAggiornamento(aCampoNota.getDataAggiornamento());
				lGenProDao.setCondizioneUpdate(lIdGenPro);
				lGenProDao.update();
				lGenProDao.stop();
			}

			// Ricerca il documento (Foglio Complementare non trasmesso) allegato all'evento,
			// se presente setta sulla tabella DOCUMENTO_ALLEGATO la DATA_ANNULLAMENTO
			// uguale a sysdate, MOTIVO_ANNULLAMENTO uguale alle motivazioni inserite nel form
			// e FLAG_DOCUMENTO_REGISTRATO uguale ad 'A'
			lDocAllSqlDAO = new DocumentoAllegatoSqlDAO(lConn);
			lDocAllSqlDAO.ricercaDocumentoAllegatoByIdEventoAndCodTipoDoc(aCampoNota.getEveIdEvento(), "06");
			DocumentoAllegatoModel lDocAll = (DocumentoAllegatoModel) lDocAllSqlDAO.getModelByKey();
			if (lDocAll != null) {
				lDocAll.setDataAnnullamento(DateUtils.getSysDate());
				lDocAll.setMotivoAnnullamento(aCampoNota.getDescr());
				lDocAll.setFlagDocumentoRegistrato("A");

				lDocAll.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				lDocAll.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				lDocAll.setDataAggiornamento(DateUtils.getSysDate());

				lDocAllDAO = new DocumentoAllegatoDAO(lConn);
				lDocAllDAO.setDAOFromModelForUpdate(lDocAll);
				lDocAllDAO.setCondizioneUpdate(lDocAll.getIdDocumentoAllegato());
				lDocAllDAO.update();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("ExAnnullaEventoInserisciCampoNota: Non posso aggiornare : " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ExAnnullaEventoInserisciCampoNota: Non posso aggiornare  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lCampoNotaDao);
			cleanup(lEveSqlDAO);
			cleanup(lLicLibAntDao);
			cleanup(lUdiProDao);
			cleanup(lUdiProSqlDao);
			cleanup(lGenProDao);
			cleanup(lRicConvDao);
			cleanup(lScaSanDao);
			cleanup(lMisSicDao);
			cleanup(lEseMisSicDao);
			cleanup(lDOPSqlDAO);
			cleanup(lPAMDao);
			cleanup(lDocAllSqlDAO);
			cleanup(lDocAllDAO);
			cleanup(lConn);
		}
		return;
	}

	/**
	 * Ricerca gli Eventi con coppie COD_MOTIVO e COD_TIPO_PROVVEDIMENTO passati attraverso due array di
	 * String. Gli eventi vengono restituiti ordinati per DATA_INSERIMENTO DESC La condzione sullo stato di
	 * validazione viene specificata nel model
	 *
	 * @param aModel
	 * @param aTipoProv
	 * @param aCodMotiv
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoTipoProvTipoMot(EventoModel aModel, String[] aTipoProv, String[] aCodMotiv)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lDao = null;
		Vector lEventi = new Vector();

		try {
			lConn = getDBConnection();
			lDao = new EventoSqlDAO(lConn);
			lDao.ricercaEventoTipoProvTipoMot(aModel, aTipoProv, aCodMotiv);
			lEventi = new Vector(lDao.getModels());
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaEventoByFascicoloSiepDecretoSospensione: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lEventi;
	}

	/**
	 *
	 * @param aModel
	 * @param aTipoProv
	 * @param aCodMotiv
	 * @return
	 */
	public EventoModel ExRicercaEventoUnicoTipoProvTipoMot(EventoModel aModel, String[] aTipoProv,
			String[] aCodMotiv) throws F3BException {

		EventoModel lEvento = null;
		Vector lEventi = null;

		lEventi = ExRicercaEventoTipoProvTipoMot(aModel, aTipoProv, aCodMotiv);
		if (lEventi != null && lEventi.size() > 0)
			lEvento = (EventoModel) lEventi.get(0);

		return lEvento;
	}

	public List ExRicercaEventiNOTAnnullati(BigDecimal aIdFascicolo, String[] aMotivo, String aTipoProvv,
			String aTipoEve) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveDao = null;

		List lEventi = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventiNOTAnnullati(aIdFascicolo, aMotivo, aTipoProvv, aTipoEve);
			lEventi = new ArrayList(lEveDao.getModels());
		} catch (Exception exc) {
			throw new F3BException("EventoController.ExRicercaEventiNOTAnnullati : " + exc);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEventi;
	}

	/**
	 * Ricerca gli Ordini di Esecuzione emessi per la richiesta di restituzione. Gli OE sono ordinati per
	 * data_emissione decrescente
	 *
	 * @param aFascKey
	 * @return Vettore di EventoModel
	 * @throws F3BException
	 */
	public Vector ExRicercaOEPerRestituzioneByFascicolo(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;

		Vector lListaOrdiniEsecuzione = new Vector();
		EventoSqlDAO lEveSqlDao = null;

		try {
			lConn = getDBConnection();
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaOrdiniEsecuzioneByIdFascicoloPerRestituzione(aIdFascicolo);
			lListaOrdiniEsecuzione = new Vector(lEveSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException", daoEx);
			throw new F3BException("EventoController.ExRicercaOEPerRestituzioneByFascicolo: " + daoEx);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}
		return lListaOrdiniEsecuzione;
	}

	/**
	 * Modifica il Magistrato dell'Evento, la Data Emissione dell'Evento e la data Trasmissione delle
	 * Notifiche.
	 *
	 * @param aEvento
	 * @param aNotMod
	 * @throws F3BException
	 */
	public void ExModificaEventoNotifiche(EventoModel aEvento, NotificaModel aNotMod) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;

		try {
			lConn = getDBTransaction();

			// Modifica Evento
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(aEvento);
			lEveDao.update();

			// Modifica Notifiche
			lNotDao = new NotificaDAO(lConn);
			lNotDao.setDataInvio(aNotMod.getDataInvio());
			lNotDao.setCondizioneEvento(aEvento.getIdEvento());
			lNotDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExModificaEventoNotifiche: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExModificaEventoNotifiche: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lConn);
		}
	}

	/**
	 * Modifica l'Evento, le notifiche e i campi nota, secondo i parametri passati
	 *
	 * @param aEvento
	 * @param aNotMod
	 * @param aCampoNota
	 *
	 * @throws F3BException
	 */
	public void ExModificaEventoNotificheCampoNote(EventoModel aEvento, NotificaModel aNotMod,
			CampoNotaModel aCampoNota) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		CampoNotaDAO lCNoteDAO = null;

		try {
			lConn = getDBTransaction();

			// Modifica Evento
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(aEvento);
			lEveDao.update();

			// Modifica Notifiche
			lNotDao = new NotificaDAO(lConn);
			lNotDao.setDataInvio(aNotMod.getDataInvio());
			// lNotDao.setCondizioneEvento(aEvento.getIdEvento());
			lNotDao.setDAOFromModelForUpdate(aNotMod);
			lNotDao.update();

			// Modifica Campo Note
			lCNoteDAO = new CampoNotaDAO(lConn);
			lCNoteDAO.setDAOFromModelForUpdate(aCampoNota);
			lCNoteDAO.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExModificaEventoNotificheCampoNote: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExModificaEventoNotificheCampoNote: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lCNoteDAO);
			cleanup(lConn);
		}
	}

	/**
	 * Modifica l'evento e cancella lo scadenzario (01 = SIMEONE) passato come parametro
	 *
	 * @param EventoModel
	 *            aEvento
	 * @param FascicoloSiepModel
	 *            aFas
	 * @param String
	 *            aTipoScadenzario
	 *
	 * @throws F3BException
	 */
	public void ExModificaEventoTrasmissioneCompetenza(EventoModel aEvento, FascicoloSiepModel aFas,
			String aTipoScadenzario) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		ScadenzarioDAO lScaDao = null;

		try {
			lConn = getDBConnection();

			lEveDao = new EventoDAO(lConn);

			lEveDao.setDAOFromModelForUpdate(aEvento);
			lEveDao.update();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("ExModificaEventoTrasmissioneCompetenza: EventoModel " + aEvento.getDescrMotivo()
					+ " aggiornato");

			lScaDao = new ScadenzarioDAO(lConn);
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFas.getIdFascicoloSiep(),
					aTipoScadenzario);
			lScaDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("ExModificaEventoTrasmissioneCompetenza: Scadezario tipo " + aTipoScadenzario
					+ " eliminato");

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExModificaEventoTrasmissioneCompetenza: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("EventoController.ExModificaEventoTrasmissioneCompetenza: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lScaDao);
			cleanup(lConn);
		}
	}

	public EventoModel ExUpdateValidaProvvedimento(EventoModel aEvento, String StatoPro) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		PenaResiduaDAO lPenResDAO = null;
		PenaResiduaSqlDAO lPenResSqlDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		EventoDAO lEveDaoBlob = null;
		EventoSqlDAO lEveSqlDao = null; // 27/07/2015

		PenaResiduaModel lPenResMod = null;
		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEvento);
			/*
			 * lEveDao.setIdEvento(aEvento.getIdEvento());
			 *
			 * lEveDao.start(); if (lEveDao.next()) { lEveApp.setIdEvento(lEveDao.getIdEvento());
			 * lEveApp.setDataEmissione(lEveDao.getDataEmissione()); }
			 */

			lPenResSqlDAO = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDAO.ricercaPenaResiduaByIdFascicoloDataDesc(aEvento.getFasSieIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDAO.getModelByKey();

			// Se esiste non validata aggiorna la pena
			if (lPenResMod != null && (!"S".equals(lPenResMod.getFlagValidato()))) {
				lPenResMod.setFlagValidato("S");
				lPenResMod.setEveIdEvento(aEvento.getIdEvento());

				lPenResDAO = new PenaResiduaDAO(lConn);
				lPenResDAO.setDAOFromModelForUpdate(lPenResMod);
				lPenResDAO.update();
				lPenResDAO.stop();
			}

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			lStatoDao = new StatoProcedimentoDAO(lConn);
			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
			lStatoDao.delete();

			lStatoProcMod.setCodStatoProcedimento(StatoPro);
			lStatoProcMod.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(aEvento.getDataEmissione());
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// chiudo ed inserisco posizione giuridica
			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setCodUfficioAggiornamento(aEvento.getCodUfficioInserimento());
			lPosDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreInserimento());
			lPosDao.setDataAggiornamento(DateUtils.getSysDate());
			lPosDao.setDataFine(DateUtils.getSysDate());
			lPosDao.setCondizioneUpdate(lPosMod.getIdPosizioneGiuridica());
			lPosDao.update();
			lPosDao.stop();

			lPosMod.setIdPosizioneGiuridica(null);
			lPosMod.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
			lPosMod.setDataInserimento(DateUtils.getSysDate());
			lPosMod.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
			lPosMod.setIdEventoRiferimento(aEvento.getIdEvento());
			lPosMod.setDataInizio(DateUtils.getSysDate());

			lPosDao.setDAOFromModel(lPosMod);
			lPosDao.insert();
			lPosDao.stop();

			/*
			 * lEveDaoBlob = new EventoDAO(lConn); lEveDaoBlob.setDAOFromModelForUpdate(aEvento);
			 * lEveDaoBlob.update(); lEveDaoBlob.stop();
			 */
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			// 27/07/2015 Si valida l'evento riferito al provvedimento della sorveglianza iscritto da SIEP.
			if (aEvento != null && aEvento.getEveIdEvento() != null) {
				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(aEvento.getEveIdEvento());
				EventoModel lEveModelTDS = (EventoModel) lEveSqlDao.getModelByKey();
				lEveDao = new EventoDAO(lConn);

				if (lEveModelTDS != null && (lEveModelTDS.getFlagDocumentoRegistrato() == null
						|| lEveModelTDS.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelTDS.setFlagDocumentoRegistrato("S");
					lEveModelTDS.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelTDS.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelTDS.setDataAggiornamento(DateUtils.getSysDate());

					lEveDao.setDAOFromModelForUpdate(lEveModelTDS);
					lEveDao.update();
					lEveDao.stop();
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("EventoController.ExUpdateValidaProvvedimento 1 : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("EventoController.ExUpdateValidaRichiesta : 3 " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lPenResSqlDAO);
			cleanup(lPenResDAO);
			cleanup(lStatoDao);
			cleanup(lEveDaoBlob);
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}

		return aEvento;
	}

	/**
	 * Ricerca il Foglio Complementare legato alll'evento in input
	 *
	 * @param aKey
	 *            id dell'evento
	 * @return booelan return true se e' presente il Foglio Complementare false altrimenti
	 */
	public boolean ExRicercaFoglioComplementare(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDAO = null;
		boolean foglioPresente = false;

		try {
			lConn = getDBConnection();
			lDocAllSqlDAO = new DocumentoAllegatoSqlDAO(lConn);
			lDocAllSqlDAO.ricercaDocumentoAllegatoByIdEventoAndCodTipoDoc(aKey, "06");
			DocumentoAllegatoModel lDocAll = (DocumentoAllegatoModel) lDocAllSqlDAO.getModelByKey();
			if (lDocAll != null) {
				foglioPresente = true;
			}
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaFoglioComplementare: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaFoglioComplementare: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDocAllSqlDAO);
			cleanup(lConn);
		}
		return foglioPresente;
	}

	/**
	 * @param aCampoNota
	 * @throws F3BException
	 */
	public void ExAnnullaEventoInserisciCampoNotaFC(CampoNotaModel aCampoNota) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		CampoNotaDAO lCampoNotaDao = null;
		LicenzaLibanticipataDAO lLicLibAntDao = null;
		UdienzaProcedimentoDAO lUdiProDao = null;
		UdienzaProcedimentoSqlDAO lUdiProSqlDao = null;
		GeneraleProcedimentoDAO lGenProDao = null;
		RichiestaConversioneDAO lRicConvDao = null;
		ScambioSanzioneDAO lScaSanDao = null;
		MisuraSicurezzaDAO lMisSicDao = null;
		EsecuzioneMisuraSicurezzaDAO lEseMisSicDao = null;
		DepositoOrdinanzaPcSqlDAO lDOPSqlDAO = null;
		PeriodoAltraMisuraDAO lPAMDao = null;
		DocumentoAllegatoDAO lDocAllDAO = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDAO = null;

		try {
			lConn = getDBTransaction();

			// Inserimento nuovo record CampoNota
			// 08/02/2006 Solo se la nota e' stata valorizzata.
			if (aCampoNota.getDescr() != null && aCampoNota.getDescr().trim().length() > 0) {

				lCampoNotaDao = new CampoNotaDAO(lConn);
				aCampoNota.setProgressivo(new BigDecimal(1));
				lCampoNotaDao.setDAOFromModel(aCampoNota);
				BigDecimal lIdCampoNota = lCampoNotaDao.insert();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserito Campo_NOTA : " + lIdCampoNota);
			}
			// Update Evento
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDataAggiornamento(aCampoNota.getDataInserimento());
			lEveDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
			lEveDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
			// TODO setto il campo FLAG_DOCUMENTO_REGISTRATO uguale ad 'A' in SiesEsecuzione
			// lEveDao.setFlagDocumentoRegistrato( "A" );
			lEveDao.selCondizioneUpdate(aCampoNota.getEveIdEvento());
			lEveDao.update();
			lEveDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornato Evento : " + aCampoNota.getEveIdEvento());

			// Nel caso in cui il Provvedimento sia un provvedimento di Revoca
			// si effettua la cancellazione dei riferimenti in tabella Evento
			// al record annullato attraverso EVE_ID_EVENTO_REVOCA
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

			// Eventuale Update LICENZA_LIBANTICIPATA
			lLicLibAntDao = new LicenzaLibanticipataDAO(lConn);
			lLicLibAntDao.setDataAggiornamento(aCampoNota.getDataInserimento());
			lLicLibAntDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
			lLicLibAntDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
			lLicLibAntDao.setFlagElaborato("A");
			lLicLibAntDao.setCondizioneIdEvento(aCampoNota.getEveIdEvento());
			lLicLibAntDao.update();
			lLicLibAntDao.stop();

			// Ricerca Evento modificato
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aCampoNota.getEveIdEvento());
			EventoModel lEve = (EventoModel) lEveSqlDAO.getModelByKey();

			// 09-03-2009 Eventuale Cancellazione di SCAMBIO_SANZIONE e aggiornamento di RICHIESTA_CONVERSIONE
			// in caso di Annullamento Ordinanza di Conversione Pene Pecuniarie.
			if (lEve.getCodMotivo().compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_CONVERSIONE) == 0 || lEve
					.getCodMotivo().compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_RATEIZZAZIONE) == 0) {
				lRicConvDao = new RichiestaConversioneDAO(lConn);
				RichiestaConversioneModel lRCModel = new RichiestaConversioneModel();
				lRCModel.setEveIdEvento(lEve.getIdEvento());
				lRCModel.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				lRCModel.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				lRCModel.setDataAggiornamento(aCampoNota.getDataInserimento());
				lRicConvDao.setDAOFromModelForCancOrdinanzaCPP(lRCModel);
				lRicConvDao.selCondizioneByIdFasSius(lEve.getFasSiuIdFascicoloSius());
				lRicConvDao.update();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						">>>> Aggiornata RICHIESTA_CONVERSIONE x Annullamento Ordinanza con IdFascicoloSius = "
								+ lEve.getFasSiuIdFascicoloSius());

				// Cancellazione Scambio Sanzione.
				lScaSanDao = new ScambioSanzioneDAO(lConn);

				if (lScaSanDao.esisteScambioSanzionePerEvento(lEve.getIdEvento())) {
					lScaSanDao.setEveIdEvento(lEve.getIdEvento());
					lScaSanDao.selCondizionebyEvento(lEve.getIdEvento());
					lScaSanDao.delete();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							">>>> Eliminata Scambio Sanzione x  Annull. Ordinanza Conv. Pene Pecuniarie con Evento :  "
									+ lEve.getIdEvento());
				}
			}

			// Per le Ordinanze di Applicazione Misure Sicurezza, quando si annulla un'Ordinanza che ha
			// trasformato la misura
			// occorre cancellare la misura generata dall' Ordinanza
			if (lEve.getCodMotivo().compareTo("2110") == 0 || lEve.getCodMotivo().compareTo("2111") == 0
					|| lEve.getCodMotivo().compareTo("2112") == 0
					|| lEve.getCodMotivo().compareTo("2113") == 0
					|| lEve.getCodMotivo().compareTo("2114") == 0
					|| lEve.getCodMotivo().compareTo("21106") == 0
					|| lEve.getCodMotivo().compareTo("2117") == 0
					|| lEve.getCodMotivo().compareTo("2118") == 0
					|| lEve.getCodMotivo().compareTo("2119") == 0) {
				lMisSicDao = new MisuraSicurezzaDAO(lConn);
				lMisSicDao.setCondizioneByDepOrdPC(lEve.getIdEvento());
				lMisSicDao.delete();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						">>>> Cancellate misure sicurezza collegate all'Evento  " + lEve.getIdEvento());
			}

			// Per le Ordinanze di Esecuzione Misure Sicurezza, quando si annulla un'Ordinanza che ha
			// trasformato la misura
			// occorre cancellare la misura generata dall' Ordinanza
			if (lEve.getCodMotivo().compareTo("2440") == 0 || lEve.getCodMotivo().compareTo("2441") == 0
					|| lEve.getCodMotivo().compareTo("2442") == 0
					|| lEve.getCodMotivo().compareTo("2660") == 0
					|| lEve.getCodMotivo().compareTo("2430") == 0) {
				DepositoOrdinanzaPcModel lDOPMod = new DepositoOrdinanzaPcModel();
				lDOPSqlDAO = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDOPSqlDAO.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEve.getIdEvento());
				lDOPMod = (DepositoOrdinanzaPcModel) lDOPSqlDAO.getModelByKey();

				if (lDOPMod != null && lDOPMod.getIdDepositoOrdinanzaPc() != null) {
					lEseMisSicDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
					lEseMisSicDao.setCondizioneDeleteByIdOrdinanza(lDOPMod.getIdDepositoOrdinanzaPc());
					lEseMisSicDao.delete();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							">>>> Cancellate evemtuali esecuzioni misure sicurezza collegate all'Evento  "
									+ lEve.getIdEvento());
				}
			}

			// Per i Decreti di Sospensione Esecuzione Misure Sicurezza, quando si annulla un Decreto che ha
			// sospeso la misura
			// occorrerebbe cancellare il periodo generato dal decreto
			// Questo pero' al momento non viene fatto automaticamente, ma si manda un messaggio di avviso.
			// Per rendere automatica la cancellazione del periodo eliminare il commento seguente!
			/**
			 * if (lEve.getCodMotivo().compareTo("2410")==0 ) { // Cancellazione eventuale Periodo Altra
			 * Misura collegata lPAMDao = new PeriodoAltraMisuraDAO(lConn); // Il Periodo Altra Misura e'
			 * collegato all'evento lPAMDao.setCondizioneByEveIdEvento(lEve.getIdEvento()); lPAMDao.delete();
			 *
			 *
			 * }
			 **/

			// Conteggio dei Provvedimenti rimasti al Fascicolo SIUS dopo l'annullamento
			// int lNumProv = lEveSqlDAO.getNumProvSIUSDEpositati(lEve.getFasSiuIdFascicoloSius());
			// Esclusione dei provvedimenti di sospensione per rimessione atti dal conteggio
			int lNumProv = lEveSqlDAO.getNumProvSIUSDEpositatiNonDefinitori(lEve.getFasSiuIdFascicoloSius());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Num Provv Fascicolo SIUS: " + lNumProv);

			if (lNumProv < 1) {
				// Aggiorno il fascicolo a stato_fascicolo = 02 se lo stato attuale e' 07
				FascicoloSiusDAO lFasSiusDao = new FascicoloSiusDAO(lConn);
				lFasSiusDao.setCodStatoFascicolo(ICostantiFascicoloSius.COD_ISCRITTO);
				lFasSiusDao.setDataAggiornamento(aCampoNota.getDataInserimento());
				lFasSiusDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				lFasSiusDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				lFasSiusDao.setCondizioneUpdateStatoFascicolo(lEve.getFasSiuIdFascicoloSius(),
						ICostantiFascicoloSius.COD_EMESSO_PROVVEDIMENTO);
				lFasSiusDao.update();
				lFasSiusDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiornamento Stato Fascicolo SIUS: " + lEve.getFasSiuIdFascicoloSius());
				cleanup(lFasSiusDao);
			}
			if (lNumProv < 1) {
				// Aggiorno il fascicolo a stato_fascicolo = 02 se lo stato attuale e' 13 (cioe' sospeso)
				FascicoloSiusDAO lFasSiusDao = new FascicoloSiusDAO(lConn);
				lFasSiusDao.setCodStatoFascicolo(ICostantiFascicoloSius.COD_ISCRITTO);
				lFasSiusDao.setDataAggiornamento(aCampoNota.getDataInserimento());
				lFasSiusDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				lFasSiusDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				lFasSiusDao.setCondizioneUpdateStatoFascicolo(lEve.getFasSiuIdFascicoloSius(), "13");
				lFasSiusDao.update();
				lFasSiusDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiornamento Stato Fascicolo SIUS: " + lEve.getFasSiuIdFascicoloSius());
				cleanup(lFasSiusDao);
			}

			// ------------------------------------------------------------------------
			// Gestione Aggiornamnto Udienza Procedimento
			// Questa parte di codice viene eseguita esclusivamente quando si tratta
			// un'evento / ordinanza rinvio udienza ( 0603 )
			// STUB 15/10/2009 Oppure per un decreto di fissazione udienza ( 0601 )
			// ------------------------------------------------------------------------

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("**** Fase di Manipolazione Udienza Procedimento **** ");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("**** Valore EventoModel lEve " + lEve);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("**** Valore EventoModel lEvento " + lEvento);

			if (lEve.getCodEsito().equals("0603") || lEve.getCodEsito().equals("0601")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Trattasi di Udienza Procedimento ****");
				// Aggiornamento ultimo record rinvio udienza legato
				// all'evento
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Update Udienza Procedimento****");
				lUdiProDao = new UdienzaProcedimentoDAO(lConn);
				lUdiProDao.setCondizioneByIdEvento(lEvento.getIdEvento());
				lUdiProDao.setCodOperatoreAggiornamento(lEvento.getCodOperatoreAggiornamento());
				lUdiProDao.setCodUfficioAggiornamento(lEvento.getCodUfficioAggiornamento());
				lUdiProDao.setFlagRinviata("A");
				lUdiProDao.update();
				lUdiProDao.stop();

				// Rilegge il record dell'udienza procedimento appena modificato, al fine di recuperare
				// l'id procedimento necessario per il recupero del record precedente
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Select Udienza Procedimento****");
				BigDecimal lIdGenPro = null;
				lUdiProDao.setCondizioneByIdEvento(lEvento.getIdEvento());
				lUdiProDao.start();

				if (lUdiProDao.next())
					lIdGenPro = lUdiProDao.getGenPridGeneraleProcedimento();

				lUdiProDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Select Udienza Procedimento SQL DAO ****");
				// Lettura dell'ultimo Record, attraverso l'id generale procedimento
				UdienzaProcedimentoUdiModel lUdiProUdi = null;
				lUdiProSqlDao = new UdienzaProcedimentoSqlDAO(lConn);
				lUdiProSqlDao.ricercaUdienzaProcedimentoUdienzaByGenProByFlagRinviata(lIdGenPro, "'R','M'");
				lUdiProSqlDao.start();

				if (lUdiProSqlDao.next())
					lUdiProUdi = (UdienzaProcedimentoUdiModel) lUdiProSqlDao.getModelConUdienza();

				lUdiProSqlDao.stop();
				// ---//

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** II Update Udienza Procedimento ****");
				// Update UDIENZA_PROCEDIMENTO.
				// STUB 15/10/2009 corretta valorizzazione del "flag Rinviata", leggendo prima l'Evento per
				// testare l'EveIdEvento.
				// lUdiProDao.setFlagRinviata("F");
				if (lUdiProUdi != null && lUdiProUdi.getUdienzaProcedimento() != null
						&& lUdiProUdi.getUdienzaProcedimento().getEveIdEvento() != null) {
					lEveSqlDAO = new EventoSqlDAO(lConn);
					lEveSqlDAO.ricercaEventoByKey(lUdiProUdi.getUdienzaProcedimento().getEveIdEvento());
					lEvento = (EventoModel) lEveSqlDAO.getModelByKey();

					if (lEvento.getIdEvento() != null) {
						if (lEve.getCodEsito().equals("0603"))
							lUdiProDao.setFlagRinviata("S");
						else
							lUdiProDao.setFlagRinviata("F");
					} else
						lUdiProDao.setFlagRinviata("P");

					lUdiProDao.setUdiIdUdienzaRinvio(null);
					lUdiProDao.setCondizioneUpdate(
							lUdiProUdi.getUdienzaProcedimento().getIdUdienzaProcedimento());
					lUdiProDao.update();
					lUdiProDao.stop();
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("**** Update Generale Procedimento ****");
				// Update di GENERALE_PROCEDIMENTO.
				lGenProDao = new GeneraleProcedimentoDAO(lConn);
				if (lUdiProUdi != null) {
					lGenProDao.setDataCameraConsiglio(lUdiProUdi.getDataUdienza());
					lGenProDao.setUdiIdUdienza(lUdiProUdi.getUdienzaProcedimento().getUdiIdUdienza());
				} else {
					lGenProDao.setDataCameraConsiglio(null);
					lGenProDao.setUdiIdUdienza(null);
				}
				lGenProDao.setCodUfficioAggiornamento(aCampoNota.getCodUfficioAggiornamento());
				lGenProDao.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreAggiornamento());
				lGenProDao.setDataAggiornamento(aCampoNota.getDataAggiornamento());
				lGenProDao.setCondizioneUpdate(lIdGenPro);
				lGenProDao.update();
				lGenProDao.stop();
			}

			// Ricerca il documento (Foglio Complementare) allegato all'evento,
			// se presente setta sulla tabella DOCUMENTO_ALLEGATO il campo MOTIVO_ANNULLAMENTO
			// uguale alle motivazioni inserite nel form.
			// La DATA_ANNULLAMENTO uguale a sysdate e il FLAG_DOCUMENTO_REGISTRATO
			// uguale ad 'A' vengono settati in SiesEsecuzione
			lDocAllSqlDAO = new DocumentoAllegatoSqlDAO(lConn);
			lDocAllSqlDAO.ricercaDocumentoAllegatoByIdEventoAndCodTipoDocTrasmesso(
					aCampoNota.getEveIdEvento(), "06");
			DocumentoAllegatoModel lDocAll = (DocumentoAllegatoModel) lDocAllSqlDAO.getModelByKey();
			if (lDocAll != null) {
				// TODO setto il campo DATA_ANNULLAMENTO uguale a sysdate in SiesEsecuzione
				// lDocAll.setDataAnnullamento(DateUtils.getSysDate());
				lDocAll.setMotivoAnnullamento(aCampoNota.getDescr());

				lDocAll.setCodUfficioAggiornamento(aCampoNota.getCodUfficioInserimento());
				lDocAll.setCodOperatoreAggiornamento(aCampoNota.getCodOperatoreInserimento());
				lDocAll.setDataAggiornamento(DateUtils.getSysDate());

				lDocAllDAO = new DocumentoAllegatoDAO(lConn);
				lDocAllDAO.setDAOFromModelForUpdate(lDocAll);
				lDocAllDAO.setCondizioneUpdate(lDocAll.getIdDocumentoAllegato());
				lDocAllDAO.update();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("ExAnnullaEventoInserisciCampoNotaFC: Non posso aggiornare : " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ExAnnullaEventoInserisciCampoNotaFC: Non posso aggiornare  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lCampoNotaDao);
			cleanup(lEveSqlDAO);
			cleanup(lLicLibAntDao);
			cleanup(lUdiProDao);
			cleanup(lUdiProSqlDao);
			cleanup(lGenProDao);
			cleanup(lRicConvDao);
			cleanup(lScaSanDao);
			cleanup(lMisSicDao);
			cleanup(lEseMisSicDao);
			cleanup(lDOPSqlDAO);
			cleanup(lPAMDao);
			cleanup(lDocAllSqlDAO);
			cleanup(lDocAllDAO);
			cleanup(lConn);
		}
		return;
	}

	/**
	 * Ricerca il Foglio Complementare Trasmesso, legato all'evento in input
	 *
	 * @param aKey
	 *            id dell'evento
	 * @return booelan return true se e' presente il Foglio Complementare false altrimenti
	 */
	public boolean ExRicercaFoglioComplementareTrasmesso(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDAO = null;
		boolean foglioPresente = false;
		BigDecimal lCount = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lDocAllSqlDAO = new DocumentoAllegatoSqlDAO(lConn);
			lDocAllSqlDAO.ricercaDocumentoAllegatoByIdEventoAndCodTipoDocTrasmesso(aKey, "06");
			DocumentoAllegatoModel lDocAll = (DocumentoAllegatoModel) lDocAllSqlDAO.getModelByKey();
			// Foglio Complementare Trasmesso
			if (lDocAll != null) {
				// verifico se la trasmissione e' andata a buon fine
				lDocAllSqlDAO.verificoEsitoTrasmissioneFC(aKey);
				lDocAllSqlDAO.start();
				lDocAllSqlDAO.next();
				lCount = lDocAllSqlDAO.getBigDecimal("HowManyRecords");
				lDocAllSqlDAO.stop();

				if (lCount.compareTo(new BigDecimal(0)) > 0) {
					foglioPresente = true;
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaFoglioComplementareTrasmesso: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaFoglioComplementareTrasmesso: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDocAllSqlDAO);
			cleanup(lConn);
		}
		return foglioPresente;
	}

	/**
	 * ExRicercaDataEmissioneCertCasellario
	 *
	 * @param aFascKey
	 * @param aChiaveUfficio
	 * @param aTipoEvento
	 * @param aCodMotivo
	 * @return Date
	 * @throws F3BException
	 */
	public Date ExRicercaDataEmissioneCertCasellario(BigDecimal aFascKey, String aChiaveUfficio,
			String aTipoEvento, String aCodMotivo) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		Date dataEmissioneCert = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);

			dataEmissioneCert = lEveDao.getDataEmissioneCertCasellario(aFascKey, aChiaveUfficio, aTipoEvento,
					aCodMotivo);
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExRicercaDataEmissioneCertCasellario: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaDataEmissioneCertCasellario: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return dataEmissioneCert;
	}

	/**
	 * Effettua la ricerca dell'Annotazione Designazione Istituto da parte del DAP nella'ambito della
	 * esecuzione Misure Sicurezza. Viene Ricercato un VERBALE legato ad un EVENTO di provvedimento
	 * Annotazione (la PENA_RESIDUA aggiornata ??)
	 *
	 * @param adEvento
	 * @return EventoVerbaleModel
	 * @throws F3BException
	 */
	public EventoVerbaleModel ExRicercaEventoVerbaleByIdEve(BigDecimal aEveKey) throws F3BException {

		Connection lConn = null;
		EventoVerbaleModel lEveVerMod = null;

		EventoSqlDAO lEveDao = null;
		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoVerbaleByIdEve(aEveKey);
			lEveDao.start();
			if (lEveDao.next())
				lEveVerMod = lEveDao.getModelIstituto();
			lEveDao.stop();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("-----------> DAOException: " + ex);
			throw new F3BException("EventoController.ExRicercaEventoVerbaleByIdEve: " + ex);
		} catch (F3BException fex) {
			rollback(lConn);
			fex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("-----------> F3BException: " + fex, fex);
			throw new F3BException("EventoController.ExRicercaEventoVerbaleByIdEve: " + fex);
		} catch (Exception es) {
			rollback(lConn);
			es.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("-----------> SQLException: " + es, es);
			throw new F3BException("EventoController.ExRicercaEventoVerbaleByIdEve: " + es);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEveVerMod;
	}

	/**
	 * Modifica l'Evento, le notifiche e i campi nota, secondo i parametri passati
	 *
	 * @param aEvento
	 * @param aNotMod
	 * @param aCampoNota
	 *
	 * @throws F3BException
	 */
	public void ExModificaEventoProvvedimentoSIGE(EventoModel aEvento,
			ProvvedimentoSigeEventoModel aProvvSIGEMod) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		ProvvedimentoSigeDAO lProvvSIGEDao = null;

		try {
			lConn = getDBTransaction();

			// Modifica Evento
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(aEvento);
			lEveDao.update();

			// Modifica Provvedimentoi SIGE
			lProvvSIGEDao = new ProvvedimentoSigeDAO(lConn);
			lProvvSIGEDao.setDAOFromModelForUpdate(aProvvSIGEMod.getProvvedimento());
			lProvvSIGEDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExModificaEventoProvvedimentoSIGE: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("EventoController.ExModificaEventoProvvedimentoSIGE: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lProvvSIGEDao);
			cleanup(lConn);
		}
	}

	public boolean existsReallyFC(BigDecimal idEvento) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		boolean exist = false;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Ricerca esistenza foglio complementare: " + idEvento);
			// Ricerca
			exist = lEveDao.ricercaEsistenzaFoglioComplementare(idEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Ricerca esistenza foglio complementare: " + exist);
		} catch (Exception sqe) {
			throw new SIUSException("EventoController.existsReallyFC: " + sqe);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return exist;
	}

	/**
	 * 07/2015 - ActStampaComunicazionePoliziaEsecMs - Cerca tutti gli eventi per idFascicoloSiep Order desc
	 *
	 * @param aIdFascicolo
	 * @return Vettore di Eventi
	 * @throws F3BException
	 */
	public Vector ExRicercaTuttiEventiValidatiByFascicoloSiepDesc(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;

		Vector lEvento = new Vector();
		EventoSqlDAO lDao = null;

		try {
			lConn = getDBConnection();
			lDao = new EventoSqlDAO(lConn);
			lDao.ricercaEventoByIdFascicoloSiepValidati(aIdFascicolo);

			lEvento = new Vector(lDao.getModels());

			// MERGE v10: cancello codice come in Mev2-s2
			// if (lEvento.size() == 0) {
			// throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			// }
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaTuttiEventiValidatiByFascicoloSiepDesc: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("EventoController.ExRicercaTuttiEventiValidatiByFascicoloSiepDesc: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lEvento;
	} // End ExRicercaTuttiEventiValidatiByFascicoloSiepDesc()

	/**
	 * Effettua l'operazione di update di un documento mandato tramite upload e inserisce uno/due (dipende dal
	 * numero di avvocati legati al fascicolo) avvisi sulla tabella AVVISI_AVVOCATO
	 *
	 * @param aEvento
	 * @param lAvvvisiAvvocato
	 * @throws F3BException
	 */
	public EventoModel ExUpdateDocument(EventoModel aEvento, Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		AvvisiAvvocatoDAO lAvvisiAvvocatoDao = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		try {
			lConn = getDBTransaction();
			lEveDao = new EventoDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);
			lAvvisiAvvocatoDao = new AvvisiAvvocatoDAO(lConn);

			// Validazione evento
			lEveDao.setDAOFromModelForUpdateBlob(aEvento);
			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.update();
			lEveDao.stop();

			// ricerca evento
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();
			lEveSqlDao.stop();

			// valido l'evento riferito al decreto/ordinanza
			if (lEveModel != null && lEveModel.getEveIdEvento() != null) {
				lEveSqlDao.ricercaEventoByKey(lEveModel.getEveIdEvento());
				EventoModel lEveModelOD = (EventoModel) lEveSqlDao.getModelByKey();

				if (lEveModelOD != null && (lEveModelOD.getFlagDocumentoRegistrato() == null
						|| lEveModelOD.getFlagDocumentoRegistrato().equals("N"))) {
					lEveModelOD.setFlagDocumentoRegistrato("S");
					lEveModelOD.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lEveModelOD.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lEveModelOD.setDataAggiornamento(DateUtils.getSysDate());

					lEveDao.setDAOFromModelForUpdate(lEveModelOD);
					lEveDao.update();
					lEveDao.stop();
				}
			}

			// inserisco gli avvisi sulla tabella AVVISI_AVVOCATO
			if (lAvvvisiAvvocato != null && lAvvvisiAvvocato.size() > 0) {
				for (AvvisiAvvocatoModel avvisoAvvocato : lAvvvisiAvvocato) {
					// 20180111 [EC] : punto 2 nuovo PLO avvocatura (SIGI_PL_PO_2017 07 21-1.0-PLO MEV
					// SIUS_AVVOCATURA.DOC)
					if (!ICostantiAvvisiAvvocato.CONTENUTO_EMISSIONE_ORDINANZA
							.equals(avvisoAvvocato.getTestoAvviso())
							&& !ICostantiAvvisiAvvocato.CONTENUTO_EMISSIONE_DECRETO
									.equals(avvisoAvvocato.getTestoAvviso())
							// Ticket#20230201017 - Anomalia Sies: Ordinanza Rinvio Udienza va trattata come
							// Ordinanza classica '03'
							&& !ICostantiAvvisiAvvocato.CONTENUTO_ORDINANZA_RINVIO_UDIENZA
									.equals(avvisoAvvocato.getTestoAvviso())) {
						lAvvisiAvvocatoDao.setDAOFromModel(avvisoAvvocato);
						lAvvisiAvvocatoDao.insert();
						lAvvisiAvvocatoDao.stop();
					}
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("EventoController.ExUpdateDocument: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("EventoController.ExUpdateDocument: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lAvvisiAvvocatoDao);
			cleanup(lConn);
		}
		return lEveMod;
	}

	// MEV 26 CUMULO
	public Vector ExRicercaEventoByTipoEveKeyIstruttoriaCumulo(BigDecimal aIstruCumKey, String[] aTipoEvento)
			throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		Vector lEventi = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoByTipoEveKeyIstruttoriaCumulo(aIstruCumKey, aTipoEvento);
			lEventi = new Vector(lEveDao.getModels());

			if (lEventi.size() == 0) {
				// throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"--XX-- EventoController - ExRicercaEventoByTipoEveKeyIstruttoriaCumulo - NESSUN EVENTO TROVATO");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByTipoEveKeyIstruttoriaCumulo: Non posso leggere : "
							+ daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaEventoByTipoEveKeyIstruttoriaCumulo: Non posso leggere  : "
							+ e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEventi;
	}

	/**
	 * intervento per MEV 64 - AVVOCATURA (anche in stampa devono apparire solo le ordinanze/decreti
	 * depositati)
	 *
	 * @param aEventi
	 * @param aConn
	 * @param aCodTipoDoc
	 * @return
	 * @throws Exception
	 */
	private Vector RicercaNumAllegatiConDeposito(Vector aEventi, Connection aConn, String aCodTipoDoc)
			throws Exception {

		// Correzione del 24-6-2005 by Luigi
		// Ora la funzione ricava anche la Data di Deposito del Decreto/Ordinanza

		// Nuovo Vettore esteso
		Vector lEventiDepnew = null;

		EventoSqlDAO lEveDao = null;
		if (aEventi != null) {
			try {
				Iterator itx = aEventi.iterator();
				lEveDao = new EventoSqlDAO(aConn);
				int lnum = -1;
				int lNumValidati = -1;

				// Model Esteso e Nuovo Vector
				EventoDepositoModel lEventoDep = null;
				lEventiDepnew = new Vector();

				while (itx.hasNext()) {
					Date lDataDeposito = null;
					EventoModel evento = (EventoModel) itx.next();
					// le fissazioni udienze non devono essere visualizzate
					if (!"0601".equals(evento.getCodEsito())) {
						lEventoDep = new EventoDepositoModel(evento);
						lnum = lEveDao.getNumDocumentiAllegati(lEventoDep.getIdEvento(), aCodTipoDoc);
						lEventoDep.setNumAllegati(lnum);
						if (lnum > 0) {
							lNumValidati = lEveDao.getNumAllegatiValidati(lEventoDep.getIdEvento());
							lEventoDep.setNumAllValidati(lNumValidati);
							// Qua bisogna inserire la ricerca della data deposito
							// :::::::::::::::::::::::
							// STUB: Luigi
							if (lEventoDep.getDescrTipoProvvedimento().compareToIgnoreCase("Decreto") == 0) {
								DepositoDecretoSqlDAO DepDecDao = new DepositoDecretoSqlDAO(aConn);
								lDataDeposito = DepDecDao.getDataDepositoByEve(lEventoDep.getIdEvento());
								cleanup(DepDecDao);
							} else if (lEventoDep.getDescrTipoProvvedimento()
									.compareToIgnoreCase("Ordinanza") == 0) {
								DepositoOrdinanzaPcSqlDAO DepOrdDao = new DepositoOrdinanzaPcSqlDAO(aConn);
								lDataDeposito = DepOrdDao.getDataDepositoByEve(lEventoDep.getIdEvento());
								cleanup(DepOrdDao);
							}
							// MEV10-s3: gestione casistica per "sentenza"
							else if (lEventoDep.getDescrTipoProvvedimento()
									.compareToIgnoreCase("Sentenza") == 0) {
								DepositoSentenzaSqlDAO lDepSentDao = new DepositoSentenzaSqlDAO(aConn);
								lDepSentDao.ricercaDepositoSentenzaByIdEveGenerato(lEventoDep.getIdEvento());
								DepositoSentenzaModel lDepSenMod = (DepositoSentenzaModel) lDepSentDao
										.getModelByKey();
								lDataDeposito = lDepSenMod.getDataDeposito();
								cleanup(lDepSentDao);
							}
						}
						// Costruzione del nuovo Vector
						// lEventoDep = new EventoDepositoModel(lEvento, lDataDeposito);
						// lEventiDep.add(lEventoDep);
						lEventoDep.setDataDeposito(lDataDeposito);
						lEventiDepnew.add(lEventoDep);
					} // chiudo if
				}
			} finally {
				cleanup(lEveDao);
			}
		}
		return lEventiDepnew;
	}

	/**
	 * Ricerca Provvedimenti SIUS comprensivi del numero di documenti allegati e della data di Deposito.
	 *
	 * @param aEvento
	 *            : contiene le condizioni di filtro della Ricerca
	 * @return Vector lEventi : elenco di EventoDepositoModel
	 * @throws F3BException
	 */
	public Vector ExRicercaProvvedimentiConDataDeposito(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		Vector lEventi = null;
		Vector lEventiConDeposito = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);

			lEveDao.ricercaEvento(aEvento);
			lEventi = new Vector(lEveDao.getModels());

			// Individuazione della presenza di documenti allegati e della data di deposito
			lEventiConDeposito = new Vector(RicercaNumAllegatiConDeposito(lEventi, lConn, null));
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ExRicercaProvvedimentiByFascicoloSius: Non posso leggere  : " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEventiConDeposito;
	}

	/**
	 * MEV 16 CUMULO: aggiunto metodo di controllo
	 */
	public boolean isCumulo(String lCodMotivo, BigDecimal idEvento) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Trattasi di cumulo??? " + lCodMotivo);
			// Ricerca
			return lEveDao.isCumulo(lCodMotivo, idEvento);
		} catch (Exception sqe) {
			throw new SIUSException("EventoController.isCumulo: " + sqe);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
	}

	/**
	 * Aggiunto metodo di modifica evento e notifiche
	 * 
	 * @author 	sgioggi
	 * @since	MEV_2023-33
	 */
	@Override
	public void ExModificaEventoNotifiche(EventoNotificaModel enm) throws F3BException {

		Connection c = null;

		EventoDAO edao = null;
		NotificaDAO ndao = null;
		AutoritaEsternaDAO aedao = null;

		try {
			c = getDBConnection();

			// aggiorno l'evento
			edao = new EventoDAO(c);
			edao.setDAOFromModelForUpdate(enm.getEvento());
			edao.update();

			// prima cancello le notifiche e poi le inserisco nuovamente
			ndao = new NotificaDAO(c);
			ndao.setCondizioneEvento(enm.getEvento().getIdEvento());
			ndao.delete();
			// ============================================
			// Inserisco le Notifiche collegate all'evento
			// ============================================
			int count = 0;
			aedao = new AutoritaEsternaDAO(c);
			BigDecimal idAutorita = null;
			if (enm != null && enm.getNotifiche() != null) {
				siesLogger.debug("Presenti " + enm.getNotifiche().length + " notifiche");
				while (count < enm.getNotifiche().length) {
					siesLogger.debug("count = " + count);
					siesLogger.debug("Notifica[" + count + "] = " + enm.getNotifiche()[count]);
					if (enm.getNotifiche()[count] != null) {
						// Se è stata specificata anche l'autorità esterna per l'avvocato,
						// recupero l'id da inserire nella notifica
						// n.b. se autorità non presente la creo
						if (enm.getNotifiche()[count].getAutoritaEsterna() != null) {
							// Provo a verificare se a sistema (tab AUTORITA_ESTERNA) esiste
							// già l'autorità esterna specificata nella form (dalla form ho solo
							// codice e sede)
							aedao.setRicercaByAutSede(
									enm.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel aem = new AutoritaEsternaModel();
							aem = (AutoritaEsternaModel) aedao.getModelByKey();
							if (aem == null) { // non esiste, la inserisco (n.b. ho solo tipo e sede)
								aedao.setDAOFromModel(
										enm.getNotifiche()[count].getAutoritaEsterna());
								idAutorita = aedao.insert();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(idAutorita);
							} else {
								idAutorita = aem.getIdAutoritaEsterna();
								enm.getNotifiche()[count].setAutEstIdAutoritaEsterna(idAutorita);
							}
						}
						enm.getNotifiche()[count].setEveIdEvento(enm.getEvento().getIdEvento());
						ndao = new NotificaDAO(c);
						ndao.setDAOFromModel(enm.getNotifiche()[count]);
						BigDecimal idNotifica = ndao.insert();
						ndao.stop();
						siesLogger.debug("Inserita Notifica con ID = " + idNotifica);
					}
					count++;
				}
			}

			commit(c);
		} catch (DAOException ex) {
			siesLogger.error("EventoController.ExModificaEventoNotifiche --> Eccezione DAO: ", ex);
			rollback(c);
			throw new F3BException("EventoController.ExModificaEventoNotifiche: " + ex);
		} catch (Exception ex) {
			siesLogger.error("EventoController.ExModificaEventoNotifiche --> Eccezione Generica: ", ex);
			rollback(c);
			throw new F3BException("EventoController.ExModificaEventoNotifiche: " + ex);
		} finally {
			cleanup(edao);
			cleanup(ndao);
			cleanup(aedao);

			cleanup(c);
		}
	}

	/**
	 * MEV_9 (D.lgs. 123/2018). Ricerca ultimo evento di Fase istruttoria » Richiesta Atti in cui è
	 * valorizzata la DATA_RESTITUZIONE_AI per poterla precaricare nelle successive richieste dove prevista
	 *
	 * @param aIdFascicoloSius
	 * @throws DAOException
	 */
	public EventoModel ricercaUltimoEventoRichiestaAttiIsruttoriByIdFasc(BigDecimal aIdFascicoloSius)
			throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoModel lEveMod;

		try {
			lConn = getDBConnection();
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaUltimoEventoRichiestaAttiIsruttoriByIdFasc(aIdFascicoloSius);
			lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoController.ricercaUltimoEventoRichiestaAttiIsruttoriByIdFasc: " + daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"EventoController.ricercaUltimoEventoRichiestaAttiIsruttoriByIdFasc: " + e);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}
		return lEveMod;
	}

}