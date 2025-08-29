package siap.sige.impugnazione.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.report.ReportGenerator;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.dao.AutoritaEsternaSqlDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.dao.FascicoloSigeDAO;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.impugnazione.dao.ImpugnazioneSigeDAO;
import siap.sige.impugnazione.dao.ImpugnazioneSigeSqlDAO;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.dao.ProvvedimentoSigeDAO;
import siap.sige.provvedimento.dao.ProvvedimentoSigeSqlDAO;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.scadenzario.dao.ScadenzarioSigeDAO;
import siap.sige.stampa.action.ICostantiStampaSige;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.util.SIGELookupRemote;

/**
 * ImpugnazioneController - Classe Controller per Impugnazione
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ImpugnazioneSigeController extends SiapController implements IImpugnazioneSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@Override
	public ImpugnazioneSigeModel ExInserisciImpugnazione(ImpugnazioneSigeModel aImpugnazione,
			BigDecimal aIdProvvedimento, EventoModel evento, FascicoloSigeModel aFascicoloSige)
			throws F3BException {

		Connection lConn = null;
		ImpugnazioneSigeDAO lImpSigeDao = null;
		ImpugnazioneSigeSqlDAO lImpSigeSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDao = null;
		ProvvedimentoSigeDAO lPSDao = null;
		ScadenzarioSigeDAO lScaDao = null;
		ProvvedimentoSigeModel lPSMod = null;
		ProvvedimentoSigeModel lPSModGenerato = null;
		FascicoloSigeDAO lFasDAO = null;

		try {
			lConn = getDBTransaction();
			lImpSigeDao = new ImpugnazioneSigeDAO(lConn);
			lImpSigeSqlDao = new ImpugnazioneSigeSqlDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lScaDao = new ScadenzarioSigeDAO(lConn);
			lPSDao = new ProvvedimentoSigeDAO(lConn);
			lPSMod = new ProvvedimentoSigeModel();
			lFasDAO = new FascicoloSigeDAO(lConn);

			// Recupero del provvedimento che si impugna.
			lPSMod.setIdProvvedimentoSige(aIdProvvedimento);
			// lPSDao.ricercaProvvedimentoSige(lPSMod);
			lPSDao.setIdProvvedimentoSige(aIdProvvedimento);
			lPSDao.selByKey();
			lPSMod = (ProvvedimentoSigeModel) lPSDao.getModelByKey();

			if (lPSMod == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Provvedimento non recuperato al momento dell'iscrizione dell'impugnazione");

			// Verificare se la ricerca va effettuata per ogni tipo provvedimento
			// if (lPSMod.getCodTipoProvvedimentoSige().equals("02") ||
			// lPSMod.getCodTipoProvvedimentoSige().equals("03") ||
			// lPSMod.getCodTipoProvvedimentoSige().equals("04") ||
			// lPSMod.getCodTipoProvvedimentoSige().equals("05") ||
			// lPSMod.getCodTipoProvvedimentoSige().equals("06"))
			// {
			// Valorizzazione di PROVV_ID_PROVVEDIMENTO_SIGE
			// aImpugnazione.setProvvIdProvvedimentoSige(lPSMod.getIdProvvedimentoSige());
			// }

			// else // Né Decreto Né Ordinanza.
			// throw new F3BException(F3BException.USER_MESSAGE,"Tipo Provvedimento non riconosciuto");

			aImpugnazione.setProvvIdProvvedimentoSige(lPSMod.getIdProvvedimentoSige());

			// ******************************
			lEveDao.setDAOFromModel(evento);
			BigDecimal idEvento = lEveDao.insert();
			evento.setIdEvento(idEvento);
			lPSModGenerato = new ProvvedimentoSigeModel();
			lPSModGenerato.setIdEventoGenerato(idEvento);
			lPSModGenerato.setCodTipoProvvedimento("15");
			lPSModGenerato.setCodTipoProvvedimentoSige("16");
			lPSModGenerato.setDefinitorio("N");
			lPSModGenerato.setFasIdFascicoloSige(aFascicoloSige.getIdFascicoloSige());
			lPSModGenerato.setCodOperatoreInserimento(evento.getCodOperatoreInserimento());
			lPSModGenerato.setCodUfficioInserimento(evento.getCodUfficioInserimento());
			lPSModGenerato.setDataInserimento(new Date());
			lPSDao.setDAOFromModel(lPSModGenerato);
			BigDecimal idProvvedimentoGenerato = lPSDao.insert();
			// aImpugnazione.setIdProvvedimentoGenerato(idProvvedimentoGenerato);
			// lImpSigeDao.setDAOFromModelForUpdate(aImpugnazione);
			// lImpSigeDao.update();
			// commit(lConn);

			// IProvvedimentoSige procCtrl = SIGELookupRemote.getProvvedimentoRemote();
			// ProvvedimentoSigeEventoModel provvedimento =
			// procCtrl.ExRicercaProvvedimentoById(impugnazione.getProvvIdProvvedimentoSige());
			// ProvvedimentoSigeEventoModel provvedimentoGenerato =
			// procCtrl.ExRicercaProvvedimentoById(idProvvedimentoGenerato);
			// impugnazione.setProvvedimentoSige(provvedimento);
			aImpugnazione.setIdProvvedimentoGenerato(idProvvedimentoGenerato);

			// ******************************

			// Calcolo del Progressivo PROGR_S7.
			lImpSigeSqlDao.getProgressivoImpugnazioneSige(aImpugnazione);
			lImpSigeSqlDao.start();

			BigDecimal lBigDec = new BigDecimal(0);
			if (lImpSigeSqlDao.next() && (lImpSigeSqlDao.getBigDecimal("aMAX") != null))
				lBigDec = lImpSigeSqlDao.getBigDecimal("aMAX");
			lImpSigeSqlDao.stop();

			if (lBigDec == null)
				lBigDec = new BigDecimal(0);

			// Setto il PROGR_S7 del Model di Impugnazione con il MAX + 1
			aImpugnazione.setProgrS7(new BigDecimal(lBigDec.intValue() + 1));
			lImpSigeDao.setDAOFromModel(aImpugnazione);
			BigDecimal lKeyImp = null;
			lKeyImp = lImpSigeDao.insert();
			aImpugnazione.setIdImpugnazioneSige(lKeyImp);

			// Modifica del 02/12/2016 MEV_15_S4
			// In fase di inserimento dell'Opposizione/Ricorso lo stato del fascicolo deve
			// essere aggiornato, settato a Opposizione (cod. 17) e Ricorso (cod. 18)
			if (aImpugnazione.getCodTipoImpugnazione().equals("01")) {
				// Ricorso
				lFasDAO.setCodStatoFascicolo(ICostantiFascicoloSige.COD_RICORSO);
			} else {
				// Opposizione
				lFasDAO.setCodStatoFascicolo(ICostantiFascicoloSige.COD_OPPOSIZIONE);
			}

			lFasDAO.setDataDefinizione(null);
			lFasDAO.setCodUfficioAggiornamento(evento.getCodUfficioInserimento());
			lFasDAO.setCodOperatoreAggiornamento(evento.getCodOperatoreInserimento());
			lFasDAO.setDataAggiornamento(new Date());

			lFasDAO.setCondizioneUpdate(aFascicoloSige.getIdFascicoloSige());
			lFasDAO.update();
			lFasDAO.stop();

			// VERIFICARE SE VA AGGIORNATO L'EVENTO PER FLAG_PIU_MENO
			// Si aggiorna l'Evento nel campo FLAG_PIU_MENO.
			// STUB 12/09/2003 In futuro il Campo FLAG_PIU_MENO sarà sostituito da COD_STATO_EVENTO.
			// lEveDao.setFlagPiuMeno("R");
			// lEveDao.selCondizioneUpdate(lEveMod.getIdEvento());
			// lEveDao.update();

			// Infine bisogna cancellare il record dello scadenzario.
			// Le tre istruzione che seguono predispongono la cancellazione. Decommentare per la
			// cancellazione.
			// lScaDao = new ScadenzarioSigeDAO(lConn);
			// lScaDao.setCondizioniByIdFascicoloTipo(aIdFascicolo, aTipo);
			// lScaDao.delete();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ImpugnazioneSigeController.ExInserisciImpugnazione: " + ex);
		} finally {
			cleanup(lImpSigeDao);
			cleanup(lEveSqlDao);
			cleanup(lEveDao);
			cleanup(lImpSigeSqlDao);
			cleanup(lScaDao);
			cleanup(lPSDao);
			cleanup(lFasDAO);
			cleanup(lConn);
		}
		return aImpugnazione;
	}

	public Vector<ImpugnazioneSigeModel> ExRicercaImpugnazione(ImpugnazioneSigeModel aImpugnazione)
			throws F3BException {

		Connection lConn = null;
		Vector<ImpugnazioneSigeModel> lImpugnazioni = new Vector<>();
		ImpugnazioneSigeSqlDAO lImpDao = null;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeSqlDAO(lConn);
			lImpDao.ricercaImpugnazioneSige(aImpugnazione);
			lImpugnazioni = new Vector<ImpugnazioneSigeModel>(lImpDao.getModels());
			if (lImpugnazioni.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ImpugnazioneSigeController.ExRicercaImpugnazione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpugnazioni;
	}

	/**
	 * La funzione ricerca le impugnazioni annullate per uno specifico Provvedimento.
	 *
	 * @param aIdProv
	 *            : ID del Provvedimento
	 * @param
	 * @return
	 * @throws F3BException
	 */
	public Vector<ImpugnazioneSigeModel> ExRicercaImpugnazioniAnnullateByProv(BigDecimal aIdProv)
			throws F3BException {

		Connection lConn = null;
		Vector<ImpugnazioneSigeModel> lImpugnazioni = null;
		ImpugnazioneSigeSqlDAO lImpDao = null;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeSqlDAO(lConn);

			lImpDao.ricercaImpugnazioniAnnullateByProv(aIdProv);

			lImpugnazioni = new Vector<ImpugnazioneSigeModel>(lImpDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ImpugnazioneSigeController.ExRicercaImpugnazioneAnnullatiByProv: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("" + e);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpugnazioni;
	}

	@Override
	public ImpugnazioneSigeModel ExRicercaImpugnazioneByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		ImpugnazioneSigeSqlDAO lImpDao = null;
		ImpugnazioneSigeModel impugnazione;
		NotificaSqlDAO notificaSqlDao = null;
		AutoritaEsternaSqlDAO autoritaSqlDao = null;
		UfficioSqlDAO ufficioSqlDao = null;
		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeSqlDAO(lConn);
			lImpDao.ricercaImpugnazioneByKey(aKey);
			impugnazione = (ImpugnazioneSigeModel) lImpDao.getModelByKey();
			notificaSqlDao = new NotificaSqlDAO(lConn);
			ufficioSqlDao = new UfficioSqlDAO(lConn);
			IProvvedimentoSige iProvvSigeCtrl = SIGELookupRemote.getProvvedimentoRemote();

			if (impugnazione != null) {
				ProvvedimentoSigeEventoModel prvovvEvento = iProvvSigeCtrl
						.ExRicercaProvvedimentoById(impugnazione.getProvvIdProvvedimentoSige());
				impugnazione.setProvvedimentoSige(prvovvEvento);
			}
			if (impugnazione != null && impugnazione.getIdProvvedimentoGenerato() != null) {
				ProvvedimentoSigeEventoModel prvovvEventoGenerato = iProvvSigeCtrl
						.ExRicercaProvvedimentoById(impugnazione.getIdProvvedimentoGenerato());
				impugnazione.setProvvedimentoSigeGenerato(prvovvEventoGenerato);
				notificaSqlDao.ricercaNotificaByEvento(impugnazione.getProvvedimentoSigeGenerato()
						.getEventoNotifica().getEvento().getIdEvento());
				Vector<NotificaModel> notifiche = new Vector<NotificaModel>(notificaSqlDao.getModels());
				autoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);
				for (NotificaModel notifica : notifiche) {
					if (notifica.getAutEstIdAutoritaEsterna() != null) {
						autoritaSqlDao.ricercaAutoritaEsternaByKey(notifica.getAutEstIdAutoritaEsterna());
						autoritaSqlDao.start();

						if (autoritaSqlDao.next())
							notifica.setAutoritaEsterna((AutoritaEsternaModel) autoritaSqlDao.getModel());

						autoritaSqlDao.stop();
					}

					if (notifica.getUffCodUfficio() != null) {
						ufficioSqlDao.ricercaUfficioByCod(notifica.getUffCodUfficio());
						ufficioSqlDao.start();

						if (ufficioSqlDao.next())
							notifica.setUfficio((UfficioModel) ufficioSqlDao.getModel());

						ufficioSqlDao.stop();
					}
				}

				impugnazione.setNotifiche(notifiche);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ImpugnazioneSigeController.ExRicercaImpugnazione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(notificaSqlDao);
			cleanup(autoritaSqlDao);
			cleanup(ufficioSqlDao);
			cleanup(lConn);
		}
		return impugnazione;
	}

	public ImpugnazioneSigeModel ExModificaImpugnazione(ImpugnazioneSigeModel aImpugnazione)
			throws F3BException {

		Connection lConn = null;
		ImpugnazioneSigeDAO lImpDao = null;
		ImpugnazioneSigeModel lImpMod = new ImpugnazioneSigeModel(aImpugnazione);
		EventoDAO eventoDao = null;
		NotificaDAO notificaDao = null;
		NotificaSqlDAO notificaSqlDao = null;
		AutoritaEsternaDAO autoritaEsternaDao = null;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeDAO(lConn);

			// Impostazione dei soli campi modificabili dalla form.
			lImpDao.setCondizioneUpdate(aImpugnazione.getIdImpugnazioneSige());
			lImpDao.setCodOperatoreAggiornamento(aImpugnazione.getCodOperatoreAggiornamento());
			lImpDao.setCodUfficioAggiornamento(aImpugnazione.getCodUfficioAggiornamento());
			lImpDao.setDataAggiornamento(aImpugnazione.getDataAggiornamento());
			lImpDao.setCodTipoImpugnazione(aImpugnazione.getCodTipoImpugnazione());
			lImpDao.setSoggettoImpugnante(aImpugnazione.getSoggettoImpugnante());
			lImpDao.setDataRicorso(aImpugnazione.getDataRicorso());
			lImpDao.setDataArrivoCancelleria(aImpugnazione.getDataArrivoCancelleria());
			lImpDao.setDataTrasmissioneAtti(aImpugnazione.getDataTrasmissioneAtti());
			lImpDao.setCodAutoritaDestinataria(aImpugnazione.getCodAutoritaDestinataria());
			lImpDao.setDataDecisione(aImpugnazione.getDataDecisione());
			lImpDao.setCodTenoreDecisione(aImpugnazione.getCodTenoreDecisione());
			lImpDao.setDataRestituzioneAtti(aImpugnazione.getDataRestituzioneAtti());
			lImpDao.setAnnotazione(aImpugnazione.getAnnotazione());
			lImpDao.setFlagSospEsec(aImpugnazione.getFlagSospEsec());
			// lImpDao.setDAOFromModelForUpdate(aImpugnazione );

			// @emma 09072018 intervento post COLLAUDO 11.2
			if (aImpugnazione.getFlagValidazioneEsito() != null) {
				String valoreFlaImpugnazione = aImpugnazione.getFlagValidazioneEsito();
				lImpDao.setFlagValidazioneEsito(valoreFlaImpugnazione);
			}

			if (aImpugnazione.getProvvedimentoSigeGenerato() != null) {
				EventoModel evento = aImpugnazione.getProvvedimentoSigeGenerato().getEventoNotifica()
						.getEvento();
				eventoDao = new EventoDAO(lConn);
				eventoDao.setDAOFromModelForUpdate(evento);
				eventoDao.update();
			}

			lImpDao.update();
			// Ticket#201911050112 — RIF. Vs ticket 20191104014
			if (aImpugnazione.getProvvedimentoSigeGenerato() != null) {
				notificaSqlDao = new NotificaSqlDAO(lConn);
				notificaSqlDao.ricercaNotificaByEvento(aImpugnazione.getProvvedimentoSigeGenerato()
						.getEventoNotifica().getEvento().getIdEvento());

				Vector<NotificaModel> oldNotifiche = new Vector<NotificaModel>(notificaSqlDao.getModels());
				notificaDao = new NotificaDAO(lConn);
				autoritaEsternaDao = new AutoritaEsternaDAO(lConn);

				for (NotificaModel oldNotifica : oldNotifiche) {
					notificaDao.setCondizioneUpdate(oldNotifica.getIdNotifica());
					notificaDao.delete();
					if (oldNotifica.getAutEstIdAutoritaEsterna() != null) {
						autoritaEsternaDao.setCondizioneUpdate(oldNotifica.getAutEstIdAutoritaEsterna());
						autoritaEsternaDao.delete();
					}
				}
			}

			Vector<NotificaModel> newNotifiche = aImpugnazione.getNotifiche();
			for (NotificaModel newNotifica : newNotifiche) {
				if (newNotifica.getAutoritaEsterna() != null) {
					autoritaEsternaDao.setDAOFromModel(newNotifica.getAutoritaEsterna());
					BigDecimal idAutorita = autoritaEsternaDao.insert();
					newNotifica.setAutEstIdAutoritaEsterna(idAutorita);
				}
				notificaDao.setDAOFromModel(newNotifica);
				notificaDao.insert();
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ImpugnazioneSigeController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lConn);
			cleanup(lImpDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(eventoDao);
			cleanup(notificaDao);
			cleanup(notificaSqlDao);
			cleanup(autoritaEsternaDao);
		}
		return lImpMod;
	}

	/**
	 * Funzione di Annullamento di una Impugnazione.
	 * <p>
	 * La funzione effettua l'update di un record nella tabella IMPUGNAZIONE_SIGE.
	 *
	 * @param aImpugnazione
	 * @param aFascicoloSige
	 * @throws F3BException
	 */
	public void ExAnnullaImpugnazione(ImpugnazioneSigeModel aImpugnazione, FascicoloSigeModel aFascicoloSige)
			throws F3BException {

		Connection lConn = null;
		ImpugnazioneSigeDAO lImpDao = null;
		FascicoloSigeDAO lFasDao = null;
		ImpugnazioneSigeSqlDAO lImpSqlDao = null;
		ImpugnazioneSigeModel impugnazione;
		ProvvedimentoSigeSqlDAO lProvSqlDao = null;

		try {
			lConn = getDBConnection();

			lImpSqlDao = new ImpugnazioneSigeSqlDAO(lConn);
			lImpSqlDao.ricercaImpugnazioneByKey(aImpugnazione.getIdImpugnazioneSige());
			impugnazione = (ImpugnazioneSigeModel) lImpSqlDao.getModelByKey();

			// In fase di annullamento dell'Opposizione (Opposizione con CodTenoreDecisione = 10) oppure
			// In fase di annullamento del Ricorso (Ricorso con CodTenoreDecisione = 02 oppure = 12)
			// se lo stato del Fascicolo Sige è uguale a Opposizione - Accoglie (fissa l'udienza) = 14
			// o Annulla con Rinvio = 15 oppure Ricorso convertito in opposizione (Fissa Udienza) = 16
			// lo Stato del Fascicolo deve essere impostato a Emesso Provvedimento = 07
			if (impugnazione != null
					&& (impugnazione.getCodTenoreDecisione() != null
							&& (impugnazione.getCodTenoreDecisione().equals("10")
									|| impugnazione.getCodTenoreDecisione().equals("02")
									|| impugnazione.getCodTenoreDecisione().equals("12"))
							&& aFascicoloSige != null && aFascicoloSige.getCodStatoFascicolo() != null
							&& !aFascicoloSige.getCodStatoFascicolo().equals("")
							&& (aFascicoloSige.getCodStatoFascicolo().equals("14")
									|| aFascicoloSige.getCodStatoFascicolo().equals("15")
									|| aFascicoloSige.getCodStatoFascicolo().equals("16")))
					// Modifica del 02/12/2016
					// In fase di annullamento dell'Opposizione/Ricorso, lo stato del fascicolo
					// Sige deve essere impostato a "Emesso Provvedimento (cod.07)" anche quando
					// lo stato attuale del Fascicolo è uguale a Opposizione (cod.17) oppure Ricorso (cod.18)
					|| (aFascicoloSige != null && aFascicoloSige.getCodStatoFascicolo() != null
							&& !aFascicoloSige.getCodStatoFascicolo().equals("")
							&& (aFascicoloSige.getCodStatoFascicolo().equals("17")
									|| aFascicoloSige.getCodStatoFascicolo().equals("18")))) {
				lFasDao = new FascicoloSigeDAO(lConn);

				// 20250827 [SG]: modifica della gestione annullamento impugnazione se anche l'ordinanza è
				// annullata
				IProvvedimentoSige ips = SIGELookupRemote.getProvvedimentoRemote();
				Vector<ProvvedimentoSigeEventoModel> vpsem = ips
						.ExRicercaProvvedimentiSigePerIdFasSige(aFascicoloSige.getIdFascicoloSige());
				boolean existDecFisUdiVal = false;
				boolean existOrdVal = false;
				boolean testDataDef = false;
				Iterator itx = vpsem.iterator();
				while (itx.hasNext()) {
					ProvvedimentoSigeEventoModel psem = (ProvvedimentoSigeEventoModel) itx.next();
					if (psem.getEventoNotifica() != null && psem.getEventoNotifica().getEvento() != null) {
						String flag = psem.getEventoNotifica().getEvento().getFlagDocumentoRegistrato();
						String esito = psem.getEventoNotifica().getEvento().getCodEsito();
						String tipoProvv = psem.getEventoNotifica().getEvento().getCodTipoProvvedimento();
						if ("S".equals(flag) && "0601".equals(esito) && "02".equals(tipoProvv))
							existDecFisUdiVal = true;
						else if ("S".equals(flag) && "03".equals(tipoProvv))
							existOrdVal = true;
					}
				}
				String codStatoFasc = ICostantiFascicoloSige.COD_EMESSO_PROVVEDIMENTO;
				if (existDecFisUdiVal && !existOrdVal) {
					codStatoFasc = ICostantiFascicoloSige.COD_DECRETO_FISSAZIONE_UDIENZA;
					testDataDef = true;
				} else if (!existDecFisUdiVal && !existOrdVal) {
					codStatoFasc = ICostantiFascicoloSige.COD_ISCRITTO;
					testDataDef = true;
				}
				// Iscritto ("02"); Decreto Fissazione Udienza ("20"); Emesso Provvedimento ("07");
				lFasDao.setCodStatoFascicolo(codStatoFasc);

				lFasDao.setCodOperatoreAggiornamento(aImpugnazione.getCodOperatoreAggiornamento());
				lFasDao.setCodUfficioAggiornamento(aImpugnazione.getCodUfficioAggiornamento());
				lFasDao.setDataAggiornamento(aImpugnazione.getDataAggiornamento());

				// La Data definizione del fascicolo viene annullata quando viene
				// inserita una Impugnazione(Opposizione/Ricorso), ma deve essere impostata nuovamente
				// se l'Impugnazione viene annullata, con la Data di Deposito dell'Ordinanza
				if (!testDataDef) {
					Date dataDeposito = null;
					lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
					lProvSqlDao.ricercaProvvSigePerIdFasSigeTipiProvv(aFascicoloSige.getIdFascicoloSige(),
							"03");
					lProvSqlDao.start();
					while (lProvSqlDao.next()) {
						ProvvedimentoSigeModel lProvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
						dataDeposito = lProvModel.getDataDeposito();
					}
					lFasDao.setDataDefinizione(dataDeposito);
				} else {
					lFasDao.setDataDefinizione(null);
					lFasDao.setCodTipoDefinizione(null);
					lFasDao.setDescrDefinizione("");
				}

				// Set del DAO e aggiornamento del FascicoloSige.
				lFasDao.setCondizioneUpdate(aFascicoloSige.getIdFascicoloSige());
				lFasDao.update();
				lFasDao.stop();
			}

			/*
			 * if(aFascicoloSige != null && aFascicoloSige.getCodStatoFascicolo() != null &&
			 * !aFascicoloSige.getCodStatoFascicolo().equals("") &&
			 * (aFascicoloSige.getCodStatoFascicolo().equals("17") ||
			 * aFascicoloSige.getCodStatoFascicolo().equals("18")) ) {
			 *
			 * lFasDao.setCodStatoFascicolo( "07" ); //Emesso Provvedimento
			 * lFasDao.setCodOperatoreAggiornamento( aImpugnazione.getCodOperatoreAggiornamento() );
			 * lFasDao.setCodUfficioAggiornamento( aImpugnazione.getCodUfficioAggiornamento() );
			 * lFasDao.setDataAggiornamento( aImpugnazione.getDataAggiornamento() );
			 *
			 * // Set del DAO e aggiornamento del FascicoloSige.
			 * lFasDao.setCondizioneUpdate(aFascicoloSige.getIdFascicoloSige()); lFasDao.update();
			 * lFasDao.stop(); }
			 */
			// Update IMPUGNAZIONE
			lImpDao = new ImpugnazioneSigeDAO(lConn);
			lImpDao.setCodOperatoreAggiornamento(aImpugnazione.getCodOperatoreAggiornamento());
			lImpDao.setCodUfficioAggiornamento(aImpugnazione.getCodUfficioAggiornamento());
			lImpDao.setDataAggiornamento(aImpugnazione.getDataAggiornamento());
			lImpDao.setFlagAnnullamento(aImpugnazione.getFlagAnnullamento());
			lImpDao.setDataAnnullamento(aImpugnazione.getDataAnnullamento());
			lImpDao.setMotivoAnnullamento(aImpugnazione.getMotivoAnnullamento());
			lImpDao.setCondizioneUpdate(aImpugnazione.getIdImpugnazioneSige());
			lImpDao.update();
			lImpDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"ImpugnazioneSigeController.ExAnnullaImpugnazione: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("" + e);
		} finally {
			cleanup(lImpDao);
			cleanup(lFasDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lImpSqlDao);
			cleanup(lProvSqlDao);
			cleanup(lConn);
		}
	}

	public ImpugnazioneSigeModel ExRicercaImpugnazioneByIdEvento(BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;
		ImpugnazioneSigeSqlDAO lImpDao = null;
		ImpugnazioneSigeModel lImpMod;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeSqlDAO(lConn);
			lImpDao.ricercaImpugnazioneByIdEvento(aIdEvento);
			lImpMod = (ImpugnazioneSigeModel) lImpDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ImpugnazioneController.ExRicercaImpugnazioneByIdEventoTipoProvv : " + daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpMod;
	}

	public ImpugnazioneSigeModel ExRicercaImpugnazioneByIdProvvTipoImp(BigDecimal aIdProvv, String aTipo)
			throws F3BException {

		Connection lConn = null;
		ImpugnazioneSigeSqlDAO lImpDao = null;
		ImpugnazioneSigeModel lImpMod;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeSqlDAO(lConn);
			lImpDao.ricercaImpugnazioniByIdProvvTipoImp(aIdProvv, aTipo);
			lImpMod = (ImpugnazioneSigeModel) lImpDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ImpugnazioneSigeController.ExRicercaImpugnazioneByIdProvvTipoImp : " + daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpMod;
	}

	/**
	 * Verifica se il procedimento individuato è attualmente impugnato. STUB 12/09/2003 Al momento il test è
	 * realizzato controllando il Campo FLAG_PIU_MENO, ma in futuro sarà gestito COD_STATO_EVENTO
	 *
	 * @param aFascKey
	 * @return aResponse
	 * @throws F3BException
	 */
	public boolean ExVerificaImpugnazione(BigDecimal aFascKey, String aTipoEvento) throws F3BException {

		siesLogger.error("ImpugnazioneSigeController.ExVerificaImpugnazione aFascKey + aTipoEvento = "
				+ aFascKey + " " + aTipoEvento);
		boolean aResponse = false;
		Connection lConn = null;
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);

			// lEveDao.ricercaEventoByFascicoloSius(aFascKey,aTipoEvento);
			// lEveDao.ricercaEventoByFascicoloSige(aFascKey,aTipoEvento);
			lEveDao.start();
			while (lEveDao.next()) {
				if (!Utils.isNullObj(((EventoModel) lEveDao.getModel()).getFlagPiuMeno())
						&& ((EventoModel) lEveDao.getModel()).getFlagPiuMeno().compareTo("R") == 0) {
					aResponse = true;
					break;
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneSigeController.ExVerificaImpugnazione: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return aResponse;
	}

	/**
	 * Esecuzione stampa dell'Impugnazione
	 * <p>
	 *
	 * @param lEvento
	 * @param lUfficio
	 * @param aUtenteModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	// public ByteArrayOutputStream ExStampaImpugnazioneSige ( BigDecimal aIdFascicolo, EventoModel lEvento,
	// String aCodUff, UtenteModel aUtenteModel)
	public ByteArrayOutputStream ExStampaImpugnazioneSige(BigDecimal aIdFascicolo, BigDecimal aIdImpugnazione,
			String aCodTemplate, String aCodUff, UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		EventoDAO lEveDao = null;
		Connection lConn = null;

		try {
			// Generazione documento di stampa
			IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();

			// Riempie l'Array contenente le tipologie di dati da prelevare
			int[] aTipoDati = { ICostantiStampaSige.TREE_SOGGETTO,
					ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO, ICostantiStampaSige.TREE_PROVVEDIMENTO,
					ICostantiStampaSige.TREE_FASCICOLOSIEP, ICostantiStampaSige.TREE_SENTENZA,
					ICostantiStampaSige.TREE_AVVOCATO, ICostantiStampaSige.TREE_LUOGODET,
					ICostantiStampaSige.TREE_MAGISTRATO, ICostantiStampaSige.TREEs_PROVVEDIMENTI,
					ICostantiStampaSige.TREE_TIT_ESE_REF, ICostantiStampaSige.TREE_UDIENZA,
					ICostantiStampaSige.TREE_IMPUGNAZIONE };
			int aTipoStampa = ICostantiStampaSige.STAMPA_IMPUGNAZIONE;

			TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa(aIdImpugnazione, aIdFascicolo, aTipoDati,
					aTipoStampa, aCodUff);

			ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

			if (aCodTemplate == null || aCodTemplate.length() < 1)
				aCodTemplate = "SIGE_IM_001";
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aCodTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + ex);
			throw new F3BException(
					"ImpugnazioneSigeController.ExStampaImpugnazioneSige: Non posso inserire il documento nell'evento : "
							+ ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	public String ExRicercaDataRicorso(BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;
		ImpugnazioneSigeSqlDAO lImpDao = null;
		String retval = " ";
		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeSqlDAO(lConn);
			retval = lImpDao.getDataRicorso(aIdEvento);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneController.ExRicercaDataRicorso : " + daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return retval;
	}

	public String ExRicercaDateRicorsi(BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;
		ImpugnazioneSigeSqlDAO lImpDao = null;

		Vector<String> lRicorsi = new Vector<>();
		String lDateRicorsi = "";
		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeSqlDAO(lConn);

			Collection<String> lColl = lImpDao.getDateRicorsi(aIdEvento);
			if (lColl != null)
				lRicorsi = new Vector<>(lImpDao.getDateRicorsi(aIdEvento));

			for (int i = 0; i < lRicorsi.size(); i++)
				lDateRicorsi = lRicorsi.toString() + "  ";

			// lDateRicorsi = new Vector(lImpDao.getDateRicorsi(aIdEvento, aTipo));

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneController.ExRicercaDateRicorsi : " + daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lDateRicorsi;
	}

	/**
	 * Conteggio del N.ro delle impugnazioni afferenti ad un particolare provvedimento.
	 *
	 * @param aFascKey
	 * @return lCont
	 * @throws F3BException
	 */
	public Vector<ImpugnazioneSigeModel> ExRicercaImpugnazioniFascicoloSige(BigDecimal aFascKey,
			String aTipoRicorso) throws F3BException {

		Connection lConn = null;

		Vector<ImpugnazioneSigeModel> lImpugnazioni = new Vector<>();
		ProvvedimentoSigeSqlDAO lPSDao = null;
		ImpugnazioneSigeSqlDAO lImpDao = null;
		try {
			lConn = getDBConnection();

			lPSDao = new ProvvedimentoSigeSqlDAO(lConn);
			lPSDao.ricercaProvvedimentiSigePerIdFasSige(aFascKey);
			Vector<ProvvedimentoSigeModel> Provvedimenti = new Vector<ProvvedimentoSigeModel>(
					lPSDao.getModels());

			for (ProvvedimentoSigeModel lPSMod : Provvedimenti) {
				lImpDao = new ImpugnazioneSigeSqlDAO(lConn);
				lImpDao.ricercaImpugnazioniByIdProvvTipoImp(lPSMod.getIdProvvedimentoSige(), aTipoRicorso);
				Collection<ImpugnazioneSigeModel> impugnazioni = lImpDao.getModels();
				for (ImpugnazioneSigeModel impugnazione : impugnazioni) {
					IProvvedimentoSige iProvvSigeCtrl = SIGELookupRemote.getProvvedimentoRemote();
					ProvvedimentoSigeEventoModel prvovvEvento = iProvvSigeCtrl
							.ExRicercaProvvedimentoById(lPSMod.getIdProvvedimentoSige());
					impugnazione.setProvvedimentoSige(prvovvEvento);

					if (impugnazione.getIdProvvedimentoGenerato() != null) {
						ProvvedimentoSigeEventoModel provvGenerato = iProvvSigeCtrl
								.ExRicercaProvvedimentoById(impugnazione.getIdProvvedimentoGenerato());
						impugnazione.setProvvedimentoSigeGenerato(provvGenerato);
					}
					lImpugnazioni.add(impugnazione);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneSigeController.ExRicercaImpugnazioni: " + daoEx);
		} finally {
			cleanup(lPSDao);
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpugnazioni;
	}

	// 06/11/2007 Nuova ricerca elenco impugnazioni del provvedimento
	/**
	 * La funzione ricerca le impugnazioni per uno specifico Provvedimento.
	 *
	 * @param aIdProv
	 *            : ID del Decreto o dell'Ordinanza
	 * @param aTipoProv
	 *            : tipo di impugnazione: '01' Ricorso , '04' Opposizione.
	 * @return
	 * @throws F3BException
	 */
	public Vector<ImpugnazioneSigeModel> ExRicercaImpugnazioniProvvedimentoSige(BigDecimal aIdProv,
			String aTipoRicorso) throws F3BException {

		Connection lConn = null;
		Vector<ImpugnazioneSigeModel> lImpugnazioni = null;
		ImpugnazioneSigeSqlDAO lImpDao = null;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeSqlDAO(lConn);

			lImpDao.ricercaImpugnazioniByIdProvvTipoImp(aIdProv, aTipoRicorso);

			lImpugnazioni = new Vector<ImpugnazioneSigeModel>(lImpDao.getModels());

			IProvvedimentoSige iProvvSigeCtrl = SIGELookupRemote.getProvvedimentoRemote();
			ProvvedimentoSigeEventoModel provvEvento = iProvvSigeCtrl.ExRicercaProvvedimentoById(aIdProv);

			for (ImpugnazioneSigeModel impugnazione : lImpugnazioni) {
				impugnazione.setProvvedimentoSige(provvEvento);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ImpugnazioneSigeController.ExRicercaImpugnazioniProvvedimentoSige: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("" + e);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpugnazioni;
	}

	/**
	 * La funzione ricerca le impugnazioni (non annullate) per uno specifico Provvedimento.
	 *
	 * @param aIdProv
	 *            : ID del Decreto o dell'Ordinanza
	 * @return
	 * @throws F3BException
	 */
	public Vector<ImpugnazioneSigeModel> ExRicercaImpugnazioniProvvedimentoSige(BigDecimal aIdProv)
			throws F3BException {

		Connection lConn = null;
		Vector<ImpugnazioneSigeModel> lImpugnazioni = null;
		ImpugnazioneSigeSqlDAO lImpDao = null;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeSqlDAO(lConn);

			lImpDao.ricercaImpugnazioniByIdProvv(aIdProv);

			lImpugnazioni = new Vector<ImpugnazioneSigeModel>(lImpDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ImpugnazioneSigeController.ExRicercaImpugnazioniProvvedimentoSige: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("" + e);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpugnazioni;
	}

	public Vector<ImpugnazioneSigeModel> ExRicercaImpugnazioneByIdProvvedimentoSige(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		Vector<ImpugnazioneSigeModel> lImpugnazioni = new Vector<>();
		ImpugnazioneSigeSqlDAO lImpDao = null;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeSqlDAO(lConn);
			lImpDao.ricercaImpugnazioneByIdProvvedimentoSigeNoJoin(aKey);
			lImpDao.start();

			lImpugnazioni = new Vector<ImpugnazioneSigeModel>(lImpDao.getModels());
			// if ( lImpugnazioni.size() == 0 )
			// {
			// throw new F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato");
			// }
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ImpugnazioneSigeController.ExRicercaImpugnazione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpugnazioni;
	}

	public Vector<ImpugnazioneSigeModel> ExRicercaImpugnazioniFascicoloSigePerEsitoDecisione(
			BigDecimal aFascKey, String aTipoRicorso) throws F3BException {

		Connection lConn = null;

		Vector<ImpugnazioneSigeModel> lImpugnazioni = new Vector<>();
		ProvvedimentoSigeSqlDAO lPSDao = null;
		ImpugnazioneSigeSqlDAO lImpDao = null;
		try {
			lConn = getDBConnection();

			lPSDao = new ProvvedimentoSigeSqlDAO(lConn);
			lPSDao.ricercaProvvedimentiSigePerIdFasSige(aFascKey);
			Vector<ProvvedimentoSigeModel> Provvedimenti = new Vector<ProvvedimentoSigeModel>(
					lPSDao.getModels());
			for (ProvvedimentoSigeModel lPSMod : Provvedimenti) {
				lImpDao = new ImpugnazioneSigeSqlDAO(lConn);
				lImpDao.ricercaImpugnazioniByIdProvvTipoImpPerEsitoDecisione(lPSMod.getIdProvvedimentoSige(),
						aTipoRicorso);
				Collection<ImpugnazioneSigeModel> impugnazioni = lImpDao.getModels();
				for (ImpugnazioneSigeModel impugnazione : impugnazioni) {
					IProvvedimentoSige iProvvSigeCtrl = SIGELookupRemote.getProvvedimentoRemote();
					ProvvedimentoSigeEventoModel prvovvEvento = iProvvSigeCtrl
							.ExRicercaProvvedimentoById(lPSMod.getIdProvvedimentoSige());
					impugnazione.setProvvedimentoSige(prvovvEvento);
					if (impugnazione.getIdProvvedimentoGenerato() != null) {
						ProvvedimentoSigeEventoModel prvovvEventoGenerato = iProvvSigeCtrl
								.ExRicercaProvvedimentoById(impugnazione.getIdProvvedimentoGenerato());
						impugnazione.setProvvedimentoSigeGenerato(prvovvEventoGenerato);
					}
					lImpugnazioni.add(impugnazione);
				}

			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ImpugnazioneSigeController.ExRicercaImpugnazioni: " + daoEx);
		} finally {
			cleanup(lPSDao);
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpugnazioni;
	}

	@Override
	public ImpugnazioneSigeModel ExImpostaEsitoImpugnazione(ImpugnazioneSigeModel impugnazione,
			EventoModel evento, BigDecimal idFascicolo) throws F3BException {

		Connection lConn = null;
		ImpugnazioneSigeDAO lImpSigeDao = null;
		ImpugnazioneSigeSqlDAO lImpSigeSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDao = null;
		ProvvedimentoSigeDAO lPSDao = null;
		ProvvedimentoSigeModel lPSModGenerato = null;
		FascicoloSigeDAO lFasDAO = null;

		try {
			lConn = getDBTransaction();
			lImpSigeDao = new ImpugnazioneSigeDAO(lConn);
			lImpSigeSqlDao = new ImpugnazioneSigeSqlDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lPSDao = new ProvvedimentoSigeDAO(lConn);
			lFasDAO = new FascicoloSigeDAO(lConn);

			lEveDao.setDAOFromModel(evento);
			BigDecimal idEvento = lEveDao.insert();
			evento.setIdEvento(idEvento);
			lPSModGenerato = new ProvvedimentoSigeModel();
			lPSModGenerato.setIdEventoGenerato(idEvento);
			lPSModGenerato.setCodTipoProvvedimento("15");
			lPSModGenerato.setCodTipoProvvedimentoSige("16");
			lPSModGenerato.setDefinitorio("N");
			lPSModGenerato.setFasIdFascicoloSige(idFascicolo);
			lPSModGenerato.setCodOperatoreInserimento(evento.getCodOperatoreInserimento());
			lPSModGenerato.setCodUfficioInserimento(evento.getCodUfficioInserimento());
			lPSModGenerato.setDataInserimento(new Date());
			lPSDao.setDAOFromModel(lPSModGenerato);
			BigDecimal idProvvedimentoGenerato = lPSDao.insert();
			impugnazione.setIdProvvedimentoGenerato(idProvvedimentoGenerato);
			lImpSigeDao.setDAOFromModelForUpdate(impugnazione);
			lImpSigeDao.update();

			// Modifica del 02/12/2016
			// Quando inserisco l'esito aggiorno lo stato del fascicolo settandolo
			// uguale alla descrizione Tenore Decisione solo nel caso di Tenore Decisione
			// uguale a 11 e 12 (Converte in Ricorso in Cassazione o Converte ricorso in Opposizione),
			// e valorizzo la Data Definizione del Fascicolo solo per i seguenti codici
			// Tenore Decisione 03-04-05-06-08-13
			if (impugnazione.getCodTenoreDecisione().equals("11")
					|| impugnazione.getCodTenoreDecisione().equals("12")
					|| impugnazione.getCodTenoreDecisione().equals("03")
					|| impugnazione.getCodTenoreDecisione().equals("04")
					|| impugnazione.getCodTenoreDecisione().equals("05")
					|| impugnazione.getCodTenoreDecisione().equals("06")
					|| impugnazione.getCodTenoreDecisione().equals("08")
					|| impugnazione.getCodTenoreDecisione().equals("13")) {
				if (impugnazione.getCodTenoreDecisione().equals("11")) {
					lFasDAO.setCodStatoFascicolo(
							ICostantiFascicoloSige.COD_CONVERTE_IN_RICORSO_IN_CASSAZIONE);
				} else if (impugnazione.getCodTenoreDecisione().equals("12")) {
					// TODO completare
				} else if (impugnazione.getCodTenoreDecisione().equals("03")
						|| impugnazione.getCodTenoreDecisione().equals("04")
						|| impugnazione.getCodTenoreDecisione().equals("05")
						|| impugnazione.getCodTenoreDecisione().equals("06")
						|| impugnazione.getCodTenoreDecisione().equals("08")
						|| impugnazione.getCodTenoreDecisione().equals("13")) {
					lFasDAO.setDataDefinizione(impugnazione.getDataDecisione());
				}

				lFasDAO.setCodUfficioAggiornamento(evento.getCodUfficioInserimento());
				lFasDAO.setCodOperatoreAggiornamento(evento.getCodOperatoreInserimento());
				lFasDAO.setDataAggiornamento(new Date());

				lFasDAO.setCondizioneUpdate(idFascicolo);
				lFasDAO.update();
				lFasDAO.stop();

			}

			commit(lConn);

			IProvvedimentoSige procCtrl = SIGELookupRemote.getProvvedimentoRemote();
			ProvvedimentoSigeEventoModel provvedimento = procCtrl
					.ExRicercaProvvedimentoById(impugnazione.getProvvIdProvvedimentoSige());
			ProvvedimentoSigeEventoModel provvedimentoGenerato = procCtrl
					.ExRicercaProvvedimentoById(impugnazione.getIdProvvedimentoGenerato());
			impugnazione.setProvvedimentoSige(provvedimento);
			impugnazione.setProvvedimentoSigeGenerato(provvedimentoGenerato);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ImpugnazioneSigeController.ExInserisciImpugnazione: " + ex);
		} finally {
			cleanup(lImpSigeDao);
			cleanup(lEveSqlDao);
			cleanup(lEveDao);
			cleanup(lImpSigeSqlDao);
			cleanup(lPSDao);
			cleanup(lFasDAO);
			cleanup(lConn);
		}
		return impugnazione;
	}

	@Override
	public ImpugnazioneSigeModel ExAggiornaEsitoImpugnazione(ImpugnazioneSigeModel impugnazione,
			BigDecimal idFascicolo) throws F3BException {

		Connection lConn = null;
		ImpugnazioneSigeDAO lImpSigeDao = null;
		EventoDAO lEveDao = null;
		NotificaDAO notificaDao = null;
		AutoritaEsternaDAO autoritaEsternaDao = null;
		FascicoloSigeDAO lFasDAO = null;

		try {
			lConn = getDBTransaction();

			lImpSigeDao = new ImpugnazioneSigeDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			notificaDao = new NotificaDAO(lConn);
			autoritaEsternaDao = new AutoritaEsternaDAO(lConn);
			lFasDAO = new FascicoloSigeDAO(lConn);
			// Ticket#201911050112 — RIF. Vs ticket 20191104014
			if (impugnazione.getProvvedimentoSigeGenerato() != null) {
				lEveDao.setDAOFromModelForUpdate(
						impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento());
				lEveDao.update();
			}

			lImpSigeDao.setDAOFromModelForUpdate(impugnazione);
			lImpSigeDao.update();

			// Modifica del 02/12/2016
			// Quando inserisco l'esito aggiorno lo stato del fascicolo settandolo
			// uguale alla descrizione Tenore Decisione solo nel caso di Tenore Decisione
			// uguale a 11 e 12 (Converte in Ricorso in Cassazione o Converte ricorso in Opposizione),
			// e valorizzo la Data Definizione del Fascicolo solo per i seguenti codici
			// Tenore Decisione 03-04-05-06-08-13
			if (impugnazione.getCodTenoreDecisione() != null) {
				if (impugnazione.getCodTenoreDecisione().equals("11")
						|| impugnazione.getCodTenoreDecisione().equals("12")
						|| impugnazione.getCodTenoreDecisione().equals("03")
						|| impugnazione.getCodTenoreDecisione().equals("04")
						|| impugnazione.getCodTenoreDecisione().equals("05")
						|| impugnazione.getCodTenoreDecisione().equals("06")
						|| impugnazione.getCodTenoreDecisione().equals("08")
						|| impugnazione.getCodTenoreDecisione().equals("13")) {
					if (impugnazione.getCodTenoreDecisione().equals("11")) {
						lFasDAO.setCodStatoFascicolo(
								ICostantiFascicoloSige.COD_CONVERTE_IN_RICORSO_IN_CASSAZIONE);
					} else if (impugnazione.getCodTenoreDecisione().equals("12")) {
						// TODO completare
					} else if (impugnazione.getCodTenoreDecisione().equals("03")
							|| impugnazione.getCodTenoreDecisione().equals("04")
							|| impugnazione.getCodTenoreDecisione().equals("05")
							|| impugnazione.getCodTenoreDecisione().equals("06")
							|| impugnazione.getCodTenoreDecisione().equals("08")
							|| impugnazione.getCodTenoreDecisione().equals("13")) {
						lFasDAO.setDataDefinizione(impugnazione.getDataDecisione());
					}
				}

				lFasDAO.setCodUfficioAggiornamento(impugnazione.getCodUfficioAggiornamento());
				lFasDAO.setCodOperatoreAggiornamento(impugnazione.getCodOperatoreAggiornamento());
				lFasDAO.setDataAggiornamento(new Date());

				lFasDAO.setCondizioneUpdate(idFascicolo);
				lFasDAO.update();
				lFasDAO.stop();

			}

			// Si verifica la presenza di notifiche (mi trovo in aggiorna opposizione),
			// in caso affermativo si procede con l'eliminazione e successivamente
			// si effettua l'inserimento
			Vector listaVecchieNotifiche = null;
			INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
			try {
				listaVecchieNotifiche = lCtrlNot.ExRicercaNotificaByKeyEvento(
						impugnazione.getProvvedimentoSigeGenerato().getProvvedimento().getIdEventoGenerato());
			} catch (F3BException f3bex) {
				if (f3bex.getMessage().contains("Nessun Elemento trovato")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(" ImpugnazioneSigeController: Trappato Errore di assenza Notifiche ");
				} else
					throw f3bex;
			}

			if (listaVecchieNotifiche != null) {
				Iterator itxNot = listaVecchieNotifiche.iterator();
				while (itxNot.hasNext())
					lCtrlNot.ExCancellaNotifica((NotificaModel) itxNot.next());
			}

			Vector<NotificaModel> newNotifiche = impugnazione.getNotifiche();
			for (NotificaModel newNotifica : newNotifiche) {
				if (newNotifica.getAutoritaEsterna() != null) {
					autoritaEsternaDao.setDAOFromModel(newNotifica.getAutoritaEsterna());
					BigDecimal idAutorita = autoritaEsternaDao.insert();
					newNotifica.setAutEstIdAutoritaEsterna(idAutorita);
				}
				notificaDao.setDAOFromModel(newNotifica);
				notificaDao.insert();
				notificaDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ImpugnazioneSigeController.ExInserisciImpugnazione: " + ex);
		} finally {
			cleanup(lImpSigeDao);
			cleanup(lEveDao);
			cleanup(notificaDao);
			cleanup(autoritaEsternaDao);
			cleanup(lFasDAO);
			cleanup(lConn);
		}
		return impugnazione;
	}

	@Override
	public ImpugnazioneSigeModel ExEliminaImpugnazione(ImpugnazioneSigeModel impugnazione)
			throws F3BException {

		ProvvedimentoSigeDAO provvDao = null;
		EventoDAO eventoDao = null;
		ImpugnazioneSigeDAO impDao = null;
		FascicoloSigeDAO lFasDAO = null;
		NotificaDAO lNotDao = null;
		Connection lConn = null;

		try {
			lConn = getDBTransaction();

			if (impugnazione.getProvvedimentoSigeGenerato() != null) {
				// Cancellazione Notifiche
				if (impugnazione.getNotifiche() != null
						&& impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica() != null
						&& impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica()
								.getEvento() != null) {
					lNotDao = new NotificaDAO(lConn);
					lNotDao.setCondizioneEvento(impugnazione.getProvvedimentoSigeGenerato()
							.getEventoNotifica().getEvento().getIdEvento());
					lNotDao.delete();
				}

				provvDao = new ProvvedimentoSigeDAO(lConn);
				provvDao.setDAOFromModelForUpdate(
						impugnazione.getProvvedimentoSigeGenerato().getProvvedimento());
				provvDao.delete();
				eventoDao = new EventoDAO(lConn);
				eventoDao.setDAOFromModelForUpdate(
						impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento());
				eventoDao.delete();
			}

			impDao = new ImpugnazioneSigeDAO(lConn);

			// Se cancello il Ricorso iscritto dopo aver convertito l'opposizione
			// in ricorso devo sbloccare l'opposizione, permettendo le azioni di cancellazione,
			// validazione e modifica, impostando il campo CONV_RICORSO_IN_CASS = 'N'
			if (impugnazione.getSoggettoImpugnante() != null
					&& impugnazione.getSoggettoImpugnante().startsWith("Opposizione")
					&& impugnazione.getIdOpposizioneConvRicorso() != null) {
				impDao = new ImpugnazioneSigeDAO(lConn);
				impDao.setConvRicorsoInCass("N");
				impDao.setDataAggiornamento(new Date());

				impDao.setCondizioneUpdate(impugnazione.getIdOpposizioneConvRicorso());
				impDao.update();
				impDao.stop();

				// Devo impostare lo stato del fascicolo = "Opposizione"
				if (impugnazione.getProvvedimentoSigeGenerato() != null
						&& impugnazione.getProvvedimentoSigeGenerato().getProvvedimento() != null
						&& impugnazione.getProvvedimentoSigeGenerato().getProvvedimento()
								.getFasIdFascicoloSige() != null) {
					lFasDAO = new FascicoloSigeDAO(lConn);

					lFasDAO.setCodStatoFascicolo(ICostantiFascicoloSige.COD_OPPOSIZIONE);
					lFasDAO.setDataAggiornamento(new Date());

					lFasDAO.setCondizioneUpdate(impugnazione.getProvvedimentoSigeGenerato().getProvvedimento()
							.getFasIdFascicoloSige());
					lFasDAO.update();
					lFasDAO.stop();
				}
			} else {
				// In tutti gli altri casi, quando cancello un Ricorso/Opposizione
				// impostare lo stato del fascicolo = "Emesso Provvedimento"
				if (impugnazione.getProvvedimentoSigeGenerato() != null
						&& impugnazione.getProvvedimentoSigeGenerato().getProvvedimento() != null
						&& impugnazione.getProvvedimentoSigeGenerato().getProvvedimento()
								.getFasIdFascicoloSige() != null) {
					lFasDAO = new FascicoloSigeDAO(lConn);

					// 20250827 [SG]: modifica della gestione annullamento impugnazione se anche l'ordinanza è
					// annullata
					IProvvedimentoSige ips = SIGELookupRemote.getProvvedimentoRemote();
					Vector<ProvvedimentoSigeEventoModel> vpsem = ips.ExRicercaProvvedimentiSigePerIdFasSige(
							impugnazione.getProvvedimentoSigeGenerato().getProvvedimento()
									.getFasIdFascicoloSige());
					boolean existDecFisUdiVal = false;
					boolean existOrdVal = false;
					boolean testDataDef = false;
					Iterator itx = vpsem.iterator();
					while (itx.hasNext()) {
						ProvvedimentoSigeEventoModel psem = (ProvvedimentoSigeEventoModel) itx.next();
						if (psem.getEventoNotifica() != null
								&& psem.getEventoNotifica().getEvento() != null) {
							String flag = psem.getEventoNotifica().getEvento().getFlagDocumentoRegistrato();
							String esito = psem.getEventoNotifica().getEvento().getCodEsito();
							String tipoProvv = psem.getEventoNotifica().getEvento().getCodTipoProvvedimento();
							if ("S".equals(flag) && "0601".equals(esito) && "02".equals(tipoProvv))
								existDecFisUdiVal = true;
							else if ("S".equals(flag) && "03".equals(tipoProvv))
								existOrdVal = true;
						}
					}
					String codStatoFasc = ICostantiFascicoloSige.COD_EMESSO_PROVVEDIMENTO;
					if (existDecFisUdiVal && !existOrdVal) {
						codStatoFasc = ICostantiFascicoloSige.COD_DECRETO_FISSAZIONE_UDIENZA;
						testDataDef = true;
					} else if (!existDecFisUdiVal && !existOrdVal) {
						codStatoFasc = ICostantiFascicoloSige.COD_ISCRITTO;
						testDataDef = true;
					}
					// Iscritto ("02"); Decreto Fissazione Udienza ("20"); Emesso Provvedimento ("07");
					lFasDAO.setCodStatoFascicolo(codStatoFasc);
					if (testDataDef) {
						lFasDAO.setDataDefinizione(null);
						lFasDAO.setCodTipoDefinizione(null);
						lFasDAO.setDescrDefinizione("");
					}
					// lFasDAO.setCodStatoFascicolo(ICostantiFascicoloSige.COD_EMESSO_PROVVEDIMENTO);
					lFasDAO.setDataAggiornamento(new Date());
					lFasDAO.setCondizioneUpdate(impugnazione.getProvvedimentoSigeGenerato().getProvvedimento()
							.getFasIdFascicoloSige());
					lFasDAO.update();
					lFasDAO.stop();
				}
			}

			impDao = new ImpugnazioneSigeDAO(lConn);
			impDao.setDAOFromModelForUpdate(impugnazione);
			impDao.delete();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ImpugnazioneSigeController.ExEliminaImpugnazione: " + ex);
		} finally {
			cleanup(provvDao);
			cleanup(eventoDao);
			cleanup(impDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFasDAO);
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return null;
	}

	public Vector<ImpugnazioneSigeModel> ExRicercaImpugnazioniAccolteByIdProvvedimento(
			BigDecimal idProvvedimento) throws F3BException {

		Connection lConn = null;
		Vector<ImpugnazioneSigeModel> lImpugnazioni = new Vector<>();
		ImpugnazioneSigeSqlDAO lImpDao = null;

		try {
			lConn = getDBConnection();
			lImpDao = new ImpugnazioneSigeSqlDAO(lConn);
			lImpDao.ricercaImpugnazioniAccolteByIdProvvedimentoSige(idProvvedimento);
			lImpDao.start();
			lImpugnazioni = new Vector<ImpugnazioneSigeModel>(lImpDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ImpugnazioneSigeController.ExRicercaImpugnazioniAccolteByIdProvvedimento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lImpDao);
			cleanup(lConn);
		}
		return lImpugnazioni;
	}

	@Override
	public ImpugnazioneSigeModel ExEliminaEsitoImpugnazione(ImpugnazioneSigeModel impugnazione,
			BigDecimal idFascicolo) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		NotificaDAO notificaDao = null;
		AutoritaEsternaDAO autoritaEsternaDao = null;
		// FascicoloSigeDAO lFasDAO = null;
		EventoModel ev = null;

		try {
			lConn = getDBTransaction();

			// lImpSigeDao = new ImpugnazioneSigeDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			notificaDao = new NotificaDAO(lConn);
			autoritaEsternaDao = new AutoritaEsternaDAO(lConn);

			// lFasDAO = new FascicoloSigeDAO(lConn);
			// Ticket#201911050112 — RIF. Vs ticket 20191104014
			if (impugnazione.getProvvedimentoSigeGenerato() != null) {
				ev = impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento();
				ev.setCodEsito("-");
				lEveDao.setDAOFromModelForUpdate(ev);
				lEveDao.update();
			}

			// lFasDAO.setCodUfficioAggiornamento(impugnazione.getCodUfficioAggiornamento());
			// lFasDAO.setCodOperatoreAggiornamento(impugnazione.getCodOperatoreAggiornamento());
			// lFasDAO.setDataAggiornamento(new Date());
			//
			// lFasDAO.setCondizioneUpdate(idFascicolo);
			// lFasDAO.update();
			// lFasDAO.stop();

			// Si verifica la presenza di notifiche (mi trovo in aggiorna opposizione),
			// in caso affermativo si procede con l'eliminazione e successivamente
			// si effettua l'inserimento
			Vector listaVecchieNotifiche = null;
			INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
			try {
				listaVecchieNotifiche = lCtrlNot.ExRicercaNotificaByKeyEvento(
						impugnazione.getProvvedimentoSigeGenerato().getProvvedimento().getIdEventoGenerato());
			} catch (F3BException f3bex) {
				if (f3bex.getMessage().contains("Nessun Elemento trovato")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(" ImpugnazioneSigeController: Trappato Errore di assenza Notifiche ");
				} else
					throw f3bex;
			}

			if (listaVecchieNotifiche != null) {
				Iterator itxNot = listaVecchieNotifiche.iterator();
				while (itxNot.hasNext())
					lCtrlNot.ExCancellaNotifica((NotificaModel) itxNot.next());
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ImpugnazioneSigeController.ExInserisciImpugnazione: " + ex);
		} finally {
			// cleanup(lImpSigeDao);
			cleanup(lEveDao);
			cleanup(notificaDao);
			cleanup(autoritaEsternaDao);
			// cleanup(lFasDAO);
			cleanup(lConn);
		}
		return impugnazione;
	}

}