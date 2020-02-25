package siap.siep.refertoscarcerazione.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.refertoscarcerazione.dao.RefertoScarcerazioneDAO;
import siap.siep.refertoscarcerazione.dao.RefertoScarcerazioneSqlDAO;
import siap.siep.refertoscarcerazione.model.RefertoScarcerazioneModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RefertoScarcerazioneController
 * </p>
 * <p>
 * Description: Classe Controller per RefertoScarcerazione
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
public class RefertoScarcerazioneController extends SiapController implements IRefertoScarcerazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisce un Referto scarcerazione
	 * 
	 * @param aEve
	 * @param aRefertoScarcerazione
	 * @param aEveMod
	 * @param aPenMod
	 * @param aMisMod
	 * @return Evento model
	 * @throws F3BException
	 */
	public EventoModel ExInserisciEventoRefertoScarcerazione(EventoModel aEve,
			RefertoScarcerazioneModel aRefertoScarcerazione, EventoNotificaModel aEveMod,
			PenaResiduaModel aPenMod, MisuraAlternativaModel aMisMod) throws F3BException {
		Connection lConn = null;
		RefertoScarcerazioneDAO lRefDao = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaDAO lPenaResDao = null;
		MisuraAlternativaDAO lMisDao = null;
		EventoModel lEveMod = new EventoModel();

		try {
			lConn = getDBTransaction();
			lRefDao = new RefertoScarcerazioneDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lPenaResDao = new PenaResiduaDAO(lConn);
			lMisDao = new MisuraAlternativaDAO(lConn);

			// inserimento evento referto scarcerazione
			lEveDao.setDAOFromModel(aEve);
			BigDecimal lKey = null;
			lKey = lEveDao.insert();
			lEveDao.stop();

			// inserimento referto scarcerazione
			aRefertoScarcerazione.setEveIdEvento(lKey);
			lRefDao.setDAOFromModel(aRefertoScarcerazione);
			lRefDao.insert();
			lRefDao.stop();

			// inserimento evento provvedimento agganciato con l'evento referto ordinanza aEveMod
			lEveDao.setDAOFromModel(aEveMod.getEvento());
			lEveDao.setEveIdEvento(lKey);
			BigDecimal lKeyEventoProvv = null;
			lKeyEventoProvv = lEveDao.insert();
			lEveDao.stop();
			lEveMod.setIdEvento(lKeyEventoProvv);

			// inserimento misura alternativa
			aMisMod.setEveIdEvento(lKeyEventoProvv);
			lMisDao.setDAOFromModel(aMisMod);
			lMisDao.insert();
			lMisDao.stop();

			// inserimento Notifiche Evento
			BigDecimal lKeyAutorita = null;
			int count = 0;
			while (count < aEveMod.getNotifiche().length) {
				if (aEveMod.getNotifiche()[count] != null) {
					if (aEveMod.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEveMod.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEveMod.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEveMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEveMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					aEveMod.getNotifiche()[count].setEveIdEvento(lKeyEventoProvv);

					lNotDao.setDAOFromModel(aEveMod.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// data fine pena aggiornamento
			lPenaResDao = new PenaResiduaDAO(lConn);
			if (aPenMod != null && aPenMod.getIdPenaResidua() != null && aPenMod.getDataFine() != null) {
				lPenaResDao.setIdPenaResidua(aPenMod.getIdPenaResidua());
				lPenaResDao.setDataFine(aPenMod.getDataFine());

				lPenaResDao.selByKey();
				lPenaResDao.update();
				lPenaResDao.stop();
			}
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"RefertoScarcerazioneController.ExInserisciEventoRefertoScarcerazione: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRefDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lPenaResDao);
			cleanup(lMisDao);

			cleanup(lConn);
		}
		return lEveMod;
	}

	/**
	 * Ricerca il referto scarcerazione dalla chiave
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public RefertoScarcerazioneModel ExRicercaRefertoScarcerazioneByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		RefertoScarcerazioneSqlDAO lRefDao = null;
		RefertoScarcerazioneModel lRefMod;

		try {
			lConn = getDBConnection();
			lRefDao = new RefertoScarcerazioneSqlDAO(lConn);
			lRefDao.ricercaRefertoScarcerazioneByKey(aKey);
			lRefMod = (RefertoScarcerazioneModel) lRefDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RefertoScarcerazioneController.ExRicercaRefertoScarcerazione: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRefDao);
			cleanup(lConn);
		}
		return lRefMod;
	}

	/**
	 * Ricerca l'ultimo referto scarcerzione
	 * 
	 * @return
	 * @throws F3BException
	 */
	public RefertoScarcerazioneModel ExRicercaUltimoRefertoScarcerazione() throws F3BException {
		Connection lConn = null;
		RefertoScarcerazioneSqlDAO lRefDao = null;
		RefertoScarcerazioneModel lRefMod;

		try {
			lConn = getDBConnection();
			lRefDao = new RefertoScarcerazioneSqlDAO(lConn);
			lRefDao.ricercaUltimoRefertoScarcerazione();
			lRefMod = (RefertoScarcerazioneModel) lRefDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RefertoScarcerazioneController.ExRicercaUltimoRefertoScarcerazione: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRefDao);
			cleanup(lConn);
		}
		return lRefMod;
	}

	/**
	 * RIcerca il referto scarcerazione legato all'evento
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public RefertoScarcerazioneModel ExRicercaRefertoScarcerazioneByEveIdEvento(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		RefertoScarcerazioneSqlDAO lRefDao = null;
		RefertoScarcerazioneModel lRefMod;

		try {
			lConn = getDBConnection();
			lRefDao = new RefertoScarcerazioneSqlDAO(lConn);
			lRefDao.ricercaRefertoScarcerazioneByEveIdEvento(aKey);
			lRefMod = (RefertoScarcerazioneModel) lRefDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RefertoScarcerazioneController.ExRicercaRefertoScarcerazioneByEveIdEvento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRefDao);
			cleanup(lConn);
		}
		return lRefMod;
	}

	/**
	 * Inserisci Evento Referto Scarcerazione
	 * 
	 * @param aKeyEvento
	 * @param aEveMod
	 * @param aPenMod
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExInserisciEventoRefertoScarcerazione(BigDecimal aKeyEvento,
			EventoNotificaModel aEveMod, PenaResiduaModel aPenMod) throws F3BException {
		Connection lConn = null;
		RefertoScarcerazioneDAO lRefDao = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaDAO lPenaResDao = null;
		EventoModel lEveMod = new EventoModel();

		try {
			lConn = getDBTransaction();
			lRefDao = new RefertoScarcerazioneDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lPenaResDao = new PenaResiduaDAO(lConn);

			// inserimento evento provvedimento agganciato con l'evento referto ordinanza aEveMod
			lEveDao.setDAOFromModel(aEveMod.getEvento());
			if (aKeyEvento != null) {
				lEveDao.setEveIdEvento(aKeyEvento);
			}
			BigDecimal lKeyEventoProvv = null;
			lKeyEventoProvv = lEveDao.insert();
			lEveDao.stop();
			lEveMod.setIdEvento(lKeyEventoProvv);

			// inserimento Notifiche Evento
			BigDecimal lKeyAutorita = null;
			int count = 0;
			while (count < aEveMod.getNotifiche().length) {
				if (aEveMod.getNotifiche()[count] != null) {
					if (aEveMod.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEveMod.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEveMod.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEveMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEveMod.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					aEveMod.getNotifiche()[count].setEveIdEvento(lKeyEventoProvv);

					lNotDao.setDAOFromModel(aEveMod.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// data fine pena aggiornamento
			lPenaResDao = new PenaResiduaDAO(lConn);
			if (aPenMod != null && aPenMod.getIdPenaResidua() != null && aPenMod.getDataFine() != null) {
				lPenaResDao.setIdPenaResidua(aPenMod.getIdPenaResidua());
				lPenaResDao.setDataFine(aPenMod.getDataFine());

				lPenaResDao.selByKey();
				lPenaResDao.update();
				lPenaResDao.stop();
			}
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"RefertoScarcerazioneController.ExInserisciEventoRefertoScarcerazione: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRefDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lPenaResDao);
			cleanup(lConn);
		}
		return lEveMod;
	}

}