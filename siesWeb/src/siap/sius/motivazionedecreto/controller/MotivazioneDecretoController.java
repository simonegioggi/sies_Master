package siap.sius.motivazionedecreto.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.motivazionedecreto.dao.MotivazioneDecretoDAO;
import siap.sius.motivazionedecreto.dao.MotivazioneDecretoSqlDAO;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.produzioneatti.action.ICostantiProduzioneAtti;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MotivazioneDecretoController
 * </p>
 * <p>
 * Description: Classe Controller per MotivazioneDecreto
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
public class MotivazioneDecretoController extends SiapController implements IMotivazioneDecreto {

	/* STUB - 20030903 Da Eliminare Verrà Usato ? */
	public MotivazioneDecretoModel ExInserisciMotivazioneDecreto(MotivazioneDecretoModel aMotivazioneDecreto)
			throws F3BException {
		Connection lConn = null;
		MotivazioneDecretoDAO lMotDao = null;
		MotivazioneDecretoModel lMotMod = null;

		try {
			lConn = getDBConnection();
			lMotMod = new MotivazioneDecretoModel(aMotivazioneDecreto);
			lMotDao = new MotivazioneDecretoDAO(lConn);
			lMotDao.setDAOFromModel(aMotivazioneDecreto);
			BigDecimal lKey = null;
			lKey = lMotDao.insert();
			commit(lConn);
			lMotMod.setIdMotivazioneDecreto(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new SIUSException("MotivazioneDecretoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lMotDao);
			cleanup(lConn);
		}

		return lMotMod;
	}

	/**
	 * Esegue l'inserimento delle motivazioni decreto.
	 * <p>
	 * 
	 * @param aMotivazioniDecreto
	 *            Array di model delle MotivazioneDecreto
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExInserisciMotivazioniDecreto(MotivazioneDecretoModel[] aMotivazioniDecreto)
			throws F3BException {
		Connection lConn = null;

		try {
			lConn = getDBTransaction();
			ExInserisciMotivazioniDecreto(aMotivazioniDecreto, lConn);

			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIUSException(
					"MotivazioneDecretoController.ExInserisciMotivazioniDecreto: Non posso inserire: " + ex);
		} finally {
			cleanup(lConn);
		}
	}

	private void ExInserisciMotivazioniDecreto(MotivazioneDecretoModel[] aMotivazioniDecreto, Connection aConn)
			throws Exception {
		MotivazioneDecretoDAO lMotDao = null;

		lMotDao = new MotivazioneDecretoDAO(aConn);

		// Effettua l'inserimento delle Motivazioni
		int lSize = aMotivazioniDecreto.length;
		for (int lIndex = 0; lIndex < lSize; lIndex++) {
			// Imposta nel model il progressivo motivazione lIndex + 1.
			aMotivazioniDecreto[lIndex].setProgrMotivazione(new BigDecimal((double) lIndex + 1));
			lMotDao.setDAOFromModel(aMotivazioniDecreto[lIndex]);
			lMotDao.insert();
			lMotDao.stop();
		}
		cleanup(lMotDao);
	}

	/**
	 * Inserisce Evento Notifica, Autorita Esterne associate e eventuali Campi note aggiuntive, Motivazioni
	 * Decreto per la "Richiesta Parere".
	 * <p>
	 * 
	 * @param EventoNotificaModel
	 *            , MotivazioneDecretoModel[]
	 * @return EventoNotificaModel
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciRichiestaParere(EventoNotificaModel aEvento,
			MotivazioneDecretoModel[] aMotivazioniDecreto) throws F3BException {
		Connection lConn = null;
		EventoNotificaModel lEveRet = null;

		try {
			lConn = getDBTransaction();
			// Chiamata all'EventoController per inserire Evento e Notifiche
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lEveRet = lCtrl.ExInserisciEventoNotifica(aEvento, lConn);
			BigDecimal lIdEvento = lEveRet.getEvento().getIdEvento();
			// Eventuale Inserimento dei Motivi Decreti
			if (aMotivazioniDecreto != null && aMotivazioniDecreto.length > 0) {
				// Viene valorizzato il campo EVE_ID_EVENTO all'evento appena inserito
				for (int i = 0; i < aMotivazioniDecreto.length; i++) {
					aMotivazioniDecreto[i].setEveIdEvento(lIdEvento);
				}
				ExInserisciMotivazioniDecreto(aMotivazioniDecreto, lConn);
			}

			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("ExInserisciRichiestaParere: " + ex);
		} finally {
			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Esegue l'inserimento delle motivazioni decreto incompetenza.
	 * <p>
	 * 
	 * @param aMotivazioniDecreto
	 *            Array di model delle MotivazioneDecreto
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExInserisciMotivazioniDecretoIncompetenza(MotivazioneDecretoModel[] aMotivazioniDecreto)
			throws F3BException {
		Connection lConn = null;
		MotivazioneDecretoDAO lMotDao = null;

		try {
			lConn = getDBTransaction();
			lMotDao = new MotivazioneDecretoDAO(lConn);

			// Effettua l'inserimento delle Motivazioni
			int lSize = aMotivazioniDecreto.length;
			for (int lIndex = 0; lIndex < lSize; lIndex++) {
				// Imposta nel model il progressivo motivazione lIndex + 1.
				aMotivazioniDecreto[lIndex].setProgrMotivazione(new BigDecimal((double) lIndex + 1));
				lMotDao.setDAOFromModelIncompetenza(aMotivazioniDecreto[lIndex]);
				lMotDao.insert();
				lMotDao.stop();

			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new SIUSException(
					"MotivazioneDecretoController.ExInserisciMotivazioniDecretoIncompetenza: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lMotDao);
			cleanup(lConn);
		}
	}

	/**
	 * Esegue la ricerca delle Motivazioni per id del deposito decreto.
	 * <p>
	 * 
	 * @param aKey
	 *            id del deposito decreto.
	 * @return l'insieme di MovitazioneDecretoModel
	 * @throws F3BException
	 *             propagazione dell'errore di eccezione.
	 */

	public Vector ExRicercaMotivazioniDecretoInammissibilitaByDepDecr(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		Vector lMotivazioneDecreti = new Vector();
		MotivazioneDecretoSqlDAO lMotSqlDao = null;

		try {
			lConn = getDBConnection();
			lMotSqlDao = new MotivazioneDecretoSqlDAO(lConn);
			lMotSqlDao.ricercaMotivazioneDecretoInammissibilitaByDepDec(aKey);
			lMotivazioneDecreti = new Vector(lMotSqlDao.getModels());
			if (lMotivazioneDecreti.size() == 0) {
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MotivazioneDecretoController.ExRicercaMotivazioneDecreto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMotSqlDao);
			cleanup(lConn);
		}

		return lMotivazioneDecreti;
	}

	/**
	 * Esegue la ricerca delle Motivazioni per id Evento. La ricerca ritorna le descrizioni delle motivazioni
	 * comprensive anche dei campi variabili.
	 * <p>
	 * 
	 * @param aKey
	 *            id Evento.
	 * @return l'insieme di MovitazioneDecretoModel
	 * @throws F3BException
	 *             propagazione dell'errore di eccezione.
	 */
	public Vector ExRicercaMotivazioniDecretoInammissibilitaByEve(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		Vector lMotivazioneDecreti = new Vector();
		MotivazioneDecretoSqlDAO lMotSqlDao = null;

		try {
			lConn = getDBConnection();
			lMotSqlDao = new MotivazioneDecretoSqlDAO(lConn);
			lMotSqlDao.ricercaMotivazioneDecretoInammissibilitaByEve(aKey);
			lMotivazioneDecreti = new Vector(lMotSqlDao.getModels());
		} catch (Exception e) {
			throw new F3BException("Non posso leggere  : " + e);
		} finally {
			cleanup(lMotSqlDao);
			cleanup(lConn);
		}

		return lMotivazioneDecreti;
	}

	/**
	 * Esegue la ricerca delle Motivazioni per id Evento.
	 * <p>
	 * 
	 * @param aKey
	 *            id Evento.
	 * @return l'insieme di MovitazioneDecretoModel
	 * @throws F3BException
	 *             propagazione dell'errore di eccezione.
	 */
	public Vector ExRicercaMotivazioniDecretoByEve(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		Vector lMotivazioneDecreti = new Vector();
		MotivazioneDecretoSqlDAO lMotSqlDao = null;

		try {
			lConn = getDBConnection();
			lMotSqlDao = new MotivazioneDecretoSqlDAO(lConn);
			lMotSqlDao.ricercaMotivazioneDecretoByEve(aKey);
			lMotivazioneDecreti = new Vector(lMotSqlDao.getModels());
		} catch (Exception e) {
			throw new F3BException("Non posso leggere  : " + e);
		} finally {
			cleanup(lMotSqlDao);
			cleanup(lConn);
		}

		return lMotivazioneDecreti;
	}

	/**
	 * Esegue la ricerca delle Motivazioni per id Fascicolo SIUS. Viene prima cercato l'ultimo Evento di tipo
	 * Richiesta Parere Inammissibilità per quel fascicolo, poi si cercano le Motivazioni collegate a
	 * quell'evento
	 * <p>
	 * 
	 * @param aKey
	 *            id Fascicolo SIUS.
	 * @return l'insieme di MovitazioneDecretoModel
	 * @throws F3BException
	 *             propagazione dell'errore di eccezione.
	 */
	public Vector ExRicercaUltimeMotivazioniDecretoByIdFasSius(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		Vector lMotivazioneDecreti = new Vector();
		MotivazioneDecretoSqlDAO lMotSqlDao = null;
		EventoSqlDAO lEveDao = null;
		EventoModel lEvento = null;

		try {
			lConn = getDBConnection();

			// Ricerca Evento di Richiesta Parere Inammissibilità
			lEvento = new EventoModel();
			lEvento.setFasSiuIdFascicoloSius(aKey);
			lEvento.setCodTipoEvento("08");
			lEvento.setCodMotivo(ICostantiProduzioneAtti.PARERE_INAMMISSIBILITA);
			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEvento(lEvento);
			lEvento = (EventoModel) lEveDao.getModelByKey();
			cleanup(lEveDao);
			if (lEvento != null && lEvento.getIdEvento() != null) {
				// Ricerca delle Motivazioni decreto
				lMotSqlDao = new MotivazioneDecretoSqlDAO(lConn);
				lMotSqlDao.ricercaMotivazioneDecretoByEve(lEvento.getIdEvento());
				lMotivazioneDecreti = new Vector(lMotSqlDao.getModels());
				cleanup(lMotSqlDao);
			}
		} catch (Exception e) {
			throw new F3BException("Non posso leggere  : " + e);
		} finally {
			cleanup(lConn);
		}

		return lMotivazioneDecreti;
	}

	public MotivazioneDecretoModel ExModificaMotivazioneDecreto(MotivazioneDecretoModel aMotivazioneDecreto)
			throws F3BException {
		Connection lConn = null;
		MotivazioneDecretoDAO lMotDao = null;
		MotivazioneDecretoModel lMotMod = new MotivazioneDecretoModel(aMotivazioneDecreto);

		try {
			lConn = getDBConnection();
			lMotDao = new MotivazioneDecretoDAO(lConn);
			lMotDao.setDAOFromModelForUpdate(aMotivazioneDecreto);
			lMotDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("MotivazioneDecretoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lMotDao);
			cleanup(lConn);
		}
		return lMotMod;
	}

	public void ExCancellaMotivazioneDecreto(MotivazioneDecretoModel aMotivazioneDecreto) throws F3BException {
		Connection lConn = null;
		MotivazioneDecretoDAO lMotDao = null;

		try {
			lConn = getDBConnection();
			lMotDao = new MotivazioneDecretoDAO(lConn);
			lMotDao.setCondizioneUpdate(aMotivazioneDecreto.getIdMotivazioneDecreto());
			lMotDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MotivazioneDecretoController.ExCancellaMotivazioneDecreto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMotDao);
			cleanup(lConn);
		}
	}

}