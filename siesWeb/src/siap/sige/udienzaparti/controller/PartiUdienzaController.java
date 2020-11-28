package siap.sige.udienzaparti.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaModel;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.dao.AutoritaEsternaSqlDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.SIGEException;
import siap.sige.udienzaparti.dao.PartiUdienzaDAO;
import siap.sige.udienzaparti.dao.PartiUdienzaDifensoreDAO;
import siap.sige.udienzaparti.dao.PartiUdienzaDifensoreSqlDAO;
import siap.sige.udienzaparti.dao.PartiUdienzaSqlDAO;
import siap.sige.udienzaparti.dao.UdienzaPartiDAO;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.udienzaparti.model.ParteCivileUdienzaModel;
import siap.sige.udienzaparti.model.ParteOffesaUdienzaModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;
import siap.sige.udienzaparti.model.UdienzaPartiModel;

/**
 * <p>
 * Title: PartiUdienzaController
 * </p>
 * <p>
 * Description: Classe Controller per l'Aula
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PartiUdienzaController extends SiapController implements IPartiUdienza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Metodo che esegue la ricerca delle parti (Offese o Civili) associate ad una udienza
	 * <p>
	 *
	 * @param aIdUdienza
	 *            idUdienza
	 * @param aCodTipoPart
	 *            tipo della parte interessata
	 * @return ritorna l'insieme delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector<AnagraficaPartiUdienzaModel> ExRicercaPartiUdienzaByIdUdienza(BigDecimal aIdUdienza,
			String aCodTipoPart) throws F3BException {

		Connection lConn = null;
		Vector<AnagraficaPartiUdienzaModel> lParti = new Vector<>();

		PartiUdienzaSqlDAO lPartiDao = null;
		PartiUdienzaDifensoreSqlDAO lDifensDao = null;
		ResidenzaSqlDAO lResidenzaDao = null;
		NotificaSqlDAO notificaDao = null;

		try {
			lConn = getDBConnection();
			lPartiDao = new PartiUdienzaSqlDAO(lConn);
			lPartiDao.ricercaPartiUdienzaByIdUdienza(aIdUdienza, aCodTipoPart);
			lPartiDao.start();

			while (lPartiDao.next()) {
				AnagraficaPartiUdienzaModel lPartiModel = (AnagraficaPartiUdienzaModel) lPartiDao.getModel();

				lDifensDao = new PartiUdienzaDifensoreSqlDAO(lConn);
				lDifensDao.ricercaDifensoreByIdSoggetto(lPartiModel.getIdSoggetto());
				List<PartiUdienzaDifensoreModel> difensori = new ArrayList(lDifensDao.getModels());
				notificaDao = new NotificaSqlDAO(lConn);
				for (PartiUdienzaDifensoreModel difensore : difensori) {
					notificaDao.ricercaNotificaByIdParteUdienzaDifensore(lPartiModel.getIdSoggetto(),
							difensore.getAvvIdAvvocato());
					notificaDao.start();
					if (notificaDao.next()) {
						NotificaModel notifica = (NotificaModel) notificaDao.getModel();
						difensore.setNotifica(notifica);
					}
				}

				lPartiModel.setDifensori(difensori);

				// Residenza/Domicilio
				lResidenzaDao = new ResidenzaSqlDAO(lConn);
				lResidenzaDao.ricercaDomicilioCorrenteByIdParteUdienza(lPartiModel.getIdSoggetto());
				lPartiModel.setResidenza((ResidenzaModel) lResidenzaDao.getModelByKey());

				lParti.add(lPartiModel);
			}
			lPartiDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PartiUdienzaController.ExRicercaPartiUdienzaByIdUdienza: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			throw new F3BException("PartiUdienzaController.ExRicercaPartiUdienzaByIdUdienza: " + e);
		} finally {
			cleanup(lPartiDao);
			cleanup(lDifensDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lResidenzaDao);
			cleanup(notificaDao);
			cleanup(lConn);
		}

		return lParti;
	}

	/**
	 * Inserisce la Parte (Offesa/Civile) associata all'udienza.
	 *
	 * @param aAnagParteUdienzaModel
	 * @param aUdienzaParteModel
	 * @return AnagraficaPartiUdienzaModel
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public AnagraficaPartiUdienzaModel ExInserisciParteUdienza(
			AnagraficaPartiUdienzaModel aAnagParteUdienzaModel, UdienzaPartiModel aUdienzaParteModel)
			throws F3BException {

		AnagraficaPartiUdienzaModel lAnagPartiUdienzaMod = null;
		ResidenzaDAO lResidenzaDao = null;
		PartiUdienzaDAO lPartiDao = null;
		UdienzaPartiDAO iUdienzaPartiDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();

			lAnagPartiUdienzaMod = new AnagraficaPartiUdienzaModel(aAnagParteUdienzaModel);
			lPartiDao = new PartiUdienzaDAO(lConn);

			// Inserimento parte sulla tabella ANAGRAFICA_PARTI_UDIENZA
			lPartiDao.setDAOFromModel(aAnagParteUdienzaModel);

			BigDecimal lKeyParte = null;
			lKeyParte = lPartiDao.insert();
			lAnagPartiUdienzaMod.setIdSoggetto(lKeyParte);

			// Inserimento sulla tabella UDIENZA_PARTI
			// (associativa tra l'Udienza SIGE e le parte coinvolta)
			iUdienzaPartiDao = new UdienzaPartiDAO(lConn);

			aUdienzaParteModel.setIdSoggetto(lKeyParte);

			iUdienzaPartiDao.setDAOFromModel(aUdienzaParteModel);
			iUdienzaPartiDao.insert();

			// Inserimento sulla tabella RESIDENZA
			lResidenzaDao = new ResidenzaDAO(lConn);
			lAnagPartiUdienzaMod.getResidenza().setIdParteUdienza(lKeyParte);
			lResidenzaDao.setDAOFromModel(lAnagPartiUdienzaMod.getResidenza());
			lResidenzaDao.insert();

			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("PartiUdienzaController.ExInserisciParteUdienza : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("PartiUdienzaController.ExInserisciParteUdienza : " + e);
		} finally {
			cleanup(iUdienzaPartiDao);
			cleanup(lPartiDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lResidenzaDao);
			cleanup(lConn);
		}

		return lAnagPartiUdienzaMod;
	}

	/**
	 * Metodo che esegue la ricerca della parte (Offesa o Civile - Fisica o Giuridica) associata ad una
	 * udienza
	 * <p>
	 *
	 * @param aIdSoggetto
	 *            identificativo della parte da ricercare
	 * @return ritorna la parte ricercata.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public AnagraficaPartiUdienzaModel ExRicercaParteUdienzaByKey(BigDecimal aIdSoggetto)
			throws F3BException {

		Connection lConn = null;

		AnagraficaPartiUdienzaModel lAnagParteMod = new AnagraficaPartiUdienzaModel();
		PartiUdienzaSqlDAO lPartiDao = null;
		PartiUdienzaDifensoreSqlDAO lDifensDao = null;
		ResidenzaSqlDAO lResidenzaDao = null;

		try {

			lConn = getDBConnection();

			lPartiDao = new PartiUdienzaSqlDAO(lConn);
			lPartiDao.ricercaParteUdienzaByKey(aIdSoggetto);
			// INIZIO 20200624 [SG]: corretto nullpointerException poichè se dopo la conferma, cancellavi la
			// parte udienza
			// la ricercaParteUdienzaByKey tornava 0 records
			lPartiDao.start();
			while (lPartiDao.next()) {
				lAnagParteMod = new AnagraficaPartiUdienzaModel(
						(AnagraficaPartiUdienzaModel) lPartiDao.getModelByKey());

				// Difensori (max 2)
				lDifensDao = new PartiUdienzaDifensoreSqlDAO(lConn);
				lDifensDao.ricercaDifensoreByIdSoggetto(lAnagParteMod.getIdSoggetto());
				lAnagParteMod.setDifensori(new ArrayList(lDifensDao.getModels()));

				// Residenza/Domicilio
				lResidenzaDao = new ResidenzaSqlDAO(lConn);
				lResidenzaDao.ricercaDomicilioCorrenteByIdParteUdienza(lAnagParteMod.getIdSoggetto());
				lAnagParteMod.setResidenza((ResidenzaModel) lResidenzaDao.getModelByKey());
			}
			lPartiDao.stop();
			// FINE 20200624
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(
					"PartiUdienzaController.ExRicercaParteUdienzaByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPartiDao);
			cleanup(lResidenzaDao);
			cleanup(lDifensDao);
			cleanup(lConn);
		}

		return lAnagParteMod;
	}

	/**
	 * Metodo che esegue la ricerca dei difensori assegnati alla Parte (Offesa o Civile)
	 *
	 * @param aIdSoggetto
	 *            parte associata all'udienza
	 * @return ritorna l'insieme delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector<PartiUdienzaDifensoreModel> ExRicercaDifensoreByIdSoggetto(BigDecimal aIdSoggetto)
			throws F3BException {

		Connection lConn = null;
		Vector<PartiUdienzaDifensoreModel> lDifensori = new Vector<>();

		PartiUdienzaDifensoreSqlDAO lDifensoriDao = null;
		NotificaSqlDAO lNotificaDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;

		try {
			lConn = getDBConnection();
			lDifensoriDao = new PartiUdienzaDifensoreSqlDAO(lConn);
			lDifensoriDao.ricercaDifensoreByIdSoggetto(aIdSoggetto);
			lDifensoriDao.start();

			lNotificaDao = new NotificaSqlDAO(lConn);

			while (lDifensoriDao.next()) {
				PartiUdienzaDifensoreModel lDifensoreModel = (PartiUdienzaDifensoreModel) lDifensoriDao
						.getModel();

				// notifica del difensore
				NotificaModel notifica = new NotificaModel();
				lNotificaDao.ricercaNotificaByIdParteUdienzaDifensore(aIdSoggetto,
						lDifensoreModel.getAvvIdAvvocato());
				lNotificaDao.start();
				while (lNotificaDao.next()) {
					notifica = (NotificaModel) lNotificaDao.getModel();
				}

				// Autorita Destinazione
				if (notifica.getAutEstIdAutoritaEsterna() != null) {
					lDifensoreModel.setFlagSNT(false);
					lAutoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(notifica.getAutEstIdAutoritaEsterna());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();

					// Inserisce l'occorenza nel model delle notifiche.
					notifica.setAutoritaEsterna(lAutorita);
					cleanup(lAutoritaSqlDao);
				} else {
					if (notifica.getIdNotifica() != null) {
						// è stato valorizzato il flag SNT (Sistema Notifiche Telematiche)
						lDifensoreModel.setFlagSNT(true);
					} else {
						lDifensoreModel.setFlagSNT(false);
					}
				}

				lDifensoreModel.setNotifica(notifica);

				lDifensori.add(lDifensoreModel);
			}

			lDifensoriDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PartiUdienzaController.ExRicercaDifensoreByIdSoggetto: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			throw new F3BException("PartiUdienzaController.ExRicercaDifensoreByIdSoggetto: " + e);
		} finally {
			cleanup(lDifensoriDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lNotificaDao);
			cleanup(lAutoritaSqlDao);
			cleanup(lConn);
		}

		return lDifensori;
	}

	/**
	 * Modifica Parte Udienza. Inserimento Notifiche avvovati (max 2), soggetto.
	 *
	 * @param aParteUdienza
	 * @param aNotifiche
	 * @param aEveIdEvento
	 *            identificativo evento della fissazione udienza
	 * @throws F3BException
	 */
	public void ExModificaParteUdienza(AnagraficaPartiUdienzaModel aParteUdienza, ArrayList aNotifiche,
			BigDecimal aEveIdEvento) throws F3BException {

		Connection lConn = null;

		ResidenzaDAO lResidenzaDao = null;
		PartiUdienzaDAO lPartiDao = null;
		NotificaSqlDAO lNotSqlDao = null;
		NotificaDAO lNotificaDao = null;
		AutoritaEsternaDAO lAutDao = null;

		try {
			// EventoNotificaModel lEve = new EventoNotificaModel();

			// Prende una connessione in transazione.
			lConn = getDBTransaction();

			lNotSqlDao = new NotificaSqlDAO(lConn);
			lNotificaDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);

			// Modifica Difensore Parte
			if (aNotifiche != null) {
				// Recupero delle notifiche precedenti associate alla parte
				Vector listaVecchieNotifiche = null;

				lNotSqlDao.ricercaNotificaByIdParteUdienza(aParteUdienza.getIdSoggetto());
				listaVecchieNotifiche = new Vector(lNotSqlDao.getModels());

				if (listaVecchieNotifiche != null) {
					Iterator itxNot = listaVecchieNotifiche.iterator();
					while (itxNot.hasNext()) {
						// Cancellare notifiche precedenti
						lNotificaDao.setCondizioneUpdate(((NotificaModel) itxNot.next()).getIdNotifica());
						lNotificaDao.delete();
					}
				}

				// Inserimento nuove notifiche
				ArrayList listaNuoveNotifiche = new ArrayList();

				BigDecimal lKeyAutorita = null;
				BigDecimal lKey = null;

				Iterator iter = aNotifiche.iterator();
				while (iter.hasNext()) {
					NotificaModel lNotModel = (NotificaModel) iter.next();
					if (lNotModel.getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(lNotModel.getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(lNotModel.getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							lNotModel.setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							lNotModel.setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					lNotificaDao.setDAOFromModel(lNotModel);
					lKey = lNotificaDao.insert();
					lNotificaDao.stop();

					lNotModel.setIdNotifica(lKey);
					listaNuoveNotifiche.add(lNotModel);

				}
			}

			// Modifica Parte
			// Aggiornamento sulla tabella ANAGRAFICA_PARTI_UDIENZA
			lPartiDao = new PartiUdienzaDAO(lConn);
			lPartiDao.setDAOFromModelForUpdate(aParteUdienza);
			lPartiDao.update();
			lPartiDao.stop();

			lResidenzaDao = new ResidenzaDAO(lConn);
			BigDecimal idResidenza = aParteUdienza.getResidenza().getIdResidenza();
			if (idResidenza != null) {
				// aggiorno la residenza
				lResidenzaDao.setDAOFromModelForUpdate(aParteUdienza.getResidenza());
				lResidenzaDao.update();
			} else {
				// inserisco la residenza
				lResidenzaDao.setDAOFromModel(aParteUdienza.getResidenza());
				BigDecimal lSequence = lResidenzaDao.insert();
				aParteUdienza.getResidenza().setIdResidenza(lSequence);
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);
			throw new SIGEException("PartiUdienzaController.ExModificaParteUdienza: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new SIGEException("PartiUdienzaController.ExModificaParteUdienza: " + e);
		} finally {
			cleanup(lResidenzaDao);
			cleanup(lPartiDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lNotSqlDao);
			cleanup(lNotificaDao);
			cleanup(lAutDao);
			cleanup(lConn);
		}
	}

	/**
	 * Metodo che esegue la ricerca della notifica Al Soggetto (Parte Offesa/Civile)
	 *
	 * @param aIdSoggetto
	 *            parte associata all'udienza
	 * @return NotificaModel ritorna l'occorrenza trovata.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public NotificaModel ExRicercaNotificaByIdSoggetto(BigDecimal aIdSoggetto) throws F3BException {

		Connection lConn = null;
		NotificaModel lNotificaSoggetto = null;
		NotificaSqlDAO lNotificaDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;

		try {
			lConn = getDBConnection();

			lNotificaDao = new NotificaSqlDAO(lConn);
			lAutoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);

			// notifica del difensore
			lNotificaSoggetto = new NotificaModel();
			lNotificaDao.ricercaNotificaByIdSoggetto(aIdSoggetto);
			lNotificaDao.start();

			while (lNotificaDao.next())
				lNotificaSoggetto = (NotificaModel) lNotificaDao.getModel();

			// Autorita Destinazione
			if (lNotificaSoggetto.getAutEstIdAutoritaEsterna() != null) {
				lAutoritaSqlDao.ricercaAutoritaEsternaByKey(lNotificaSoggetto.getAutEstIdAutoritaEsterna());
				AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
				// Inserisce l'occorenza nel model delle notifiche.
				lNotificaSoggetto.setAutoritaEsterna(lAutorita);
				cleanup(lAutoritaSqlDao);
			}

			lAutoritaSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PartiUdienzaController.ExRicercaNotificaByIdSoggetto: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			throw new F3BException("PartiUdienzaController.ExRicercaNotificaByIdSoggetto: " + e);
		} finally {
			cleanup(lAutoritaSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lNotificaDao);
			cleanup(lConn);
		}

		return lNotificaSoggetto;
	}

	/**
	 * Metodo che esegue la cancellazione di una Parte di una Udienza
	 *
	 * @param aIdSoggetto
	 *            parte associata all'udienza
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExCancellaParteUdienza(BigDecimal aIdSoggetto) throws F3BException {

		Connection lConn = null;

		NotificaDAO lNotificaDao = null;
		PartiUdienzaDifensoreDAO lPartiUdienzaDifDao = null;
		UdienzaPartiDAO lUdienzaPartiDao = null;
		PartiUdienzaDAO lPartiUdienzaDao = null;
		ResidenzaDAO lResidenzaDao = null;

		try {
			lConn = getDBTransaction();

			// Cancellazione di eventuali Notifiche dei difensori della Parte
			// tabella NOTIFICA
			lNotificaDao = new NotificaDAO(lConn);
			lNotificaDao.selCondizioneDeleteByIdParteUdienza(aIdSoggetto);
			lNotificaDao.delete();
			lNotificaDao.stop();

			// Cancellazione di eventuali Difensori associati alla Parte
			// tabella PARTI_UDIENZA_DIFENSORE
			lPartiUdienzaDifDao = new PartiUdienzaDifensoreDAO(lConn);
			lPartiUdienzaDifDao.selCondizioneDeleteByIdParteUdienza(aIdSoggetto);
			lPartiUdienzaDifDao.delete();
			lPartiUdienzaDifDao.stop();

			// Cancellazione dell'associazione tra l'Udienza e la Parte
			// tabella UDIENZA_PARTI
			lUdienzaPartiDao = new UdienzaPartiDAO(lConn);
			lUdienzaPartiDao.selCondizioneDeleteByIdParteUdienza(aIdSoggetto);
			lUdienzaPartiDao.delete();
			lUdienzaPartiDao.stop();

			// Cancellazione della Residenza della Parte
			// tabella RESIDENZA
			lResidenzaDao = new ResidenzaDAO(lConn);
			lResidenzaDao.selPerIdParteUdienza(aIdSoggetto);
			lResidenzaDao.delete();
			lResidenzaDao.stop();

			// Cancellazione Parte
			// tabella ANAGRAFICA_PARTI_UDIENZA
			lPartiUdienzaDao = new PartiUdienzaDAO(lConn);
			lPartiUdienzaDao.selCondizioneDeleteByKey(aIdSoggetto);
			lPartiUdienzaDao.delete();
			lPartiUdienzaDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PartiUdienzaController.ExCancellaParteUdienza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lNotificaDao);
			cleanup(lPartiUdienzaDifDao);
			cleanup(lUdienzaPartiDao);
			cleanup(lPartiUdienzaDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lResidenzaDao);
			cleanup(lConn);
		}
	}

	@Override
	public List<ParteOffesaUdienzaModel> ExRicercaPartiOffesaUdienzaByIdUdienza(
			BigDecimal idUdienzaProcedimentoSige) throws F3BException {

		Vector<AnagraficaPartiUdienzaModel> parti = ExRicercaPartiUdienzaByIdUdienza(
				idUdienzaProcedimentoSige, "O");
		List<ParteOffesaUdienzaModel> partiOffese = new ArrayList<>();
		for (AnagraficaPartiUdienzaModel parte : parti) {
			partiOffese.add(new ParteOffesaUdienzaModel(parte));
		}

		return partiOffese;
	}

	@Override
	public List<ParteCivileUdienzaModel> ExRicercaPartiCiviliUdienzaByIdUdienza(
			BigDecimal idUdienzaProcedimentoSige) throws F3BException {

		Vector<AnagraficaPartiUdienzaModel> parti = ExRicercaPartiUdienzaByIdUdienza(
				idUdienzaProcedimentoSige, "C");
		List<ParteCivileUdienzaModel> partiCivili = new ArrayList<>();
		for (AnagraficaPartiUdienzaModel parte : parti) {
			partiCivili.add(new ParteCivileUdienzaModel(parte));
		}

		return partiCivili;
	}

}