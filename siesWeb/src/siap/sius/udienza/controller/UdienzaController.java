package siap.sius.udienza.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.dao.NotificaDAO;
import siap.sius.SIUSException;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusDAO;
import siap.sius.avvocatura.dao.AvvisiAvvocatoDAO;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.dao.DepositoSentenzaDAO;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.dao.UdienzaDAO;
import siap.sius.udienza.dao.UdienzaSqlDAO;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienza.model.UdienzaNModel;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.dao.ProcedimentixUdienzaSqlDAO;
import siap.sius.udienzaprocedimento.dao.UdienzaProcedimentoDAO;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: UdienzaController
 * </p>
 * <p>
 * Description: Classe Controller per Udienza
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
public class UdienzaController extends SiapController implements IUdienza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Effettua l'inserimento di una undienza.
	 *
	 * @param aUdienza
	 *            istanza del model.
	 * @return istanza del model.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public UdienzaModel ExInserisciUdienza(UdienzaModel aUdienza) throws F3BException {

		Connection lConn = null;
		UdienzaDAO lUdiDao = null;
		UdienzaModel lUdiMod = null;

		try {
			lConn = getDBConnection();
			lUdiMod = new UdienzaModel(aUdienza);
			lUdiDao = new UdienzaDAO(lConn);
			lUdiDao.setDAOFromModel(aUdienza);

			BigDecimal lKey = lUdiDao.insert();
			commit(lConn);
			lUdiMod.setIdUdienza(lKey);
		} catch (DAOException ex) {
			rollback(lConn);

			if (ex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Inserimento impossibile: gia' esiste un'udienza nella stessa data, per lo stesso collegio.");

			throw new SIUSException("UdienzaController.ExInserisciUdienza: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	/**
	 * Effettua l'inserimento di una undienza.
	 *
	 * @param aUdienza
	 *            istanza del model.
	 * @return istanza del model.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public UdienzaModel ExInserisciUdienzaUDS(UdienzaModel aUdienza) throws F3BException {

		Connection lConn = null;
		UdienzaDAO lUdiDao = null;
		UdienzaModel lUdiMod = null;

		try {
			lConn = getDBConnection();
			lUdiMod = new UdienzaModel(aUdienza);
			lUdiDao = new UdienzaDAO(lConn);
			lUdiDao.setDAOFromModelUDS(aUdienza);

			BigDecimal lKey = lUdiDao.insert();
			commit(lConn);
			lUdiMod.setIdUdienza(lKey);
		} catch (DAOException ex) {
			rollback(lConn);

			if (ex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Inserimento impossibile: gia' esiste un'udienza nella stessa data, per lo stesso collegio.");

			throw new SIUSException("UdienzaController.ExInserisciUdienzaUDS: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	/**
	 * Effettua la ricerca di una udienza, per data.
	 *
	 * @param aUdienza
	 *            Istanza del model <code>Udienza</code>.
	 * @return l'insieme delle istanze model.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaUdienza(UdienzaModel aUdienza) throws F3BException {

		Connection lConn = null;
		Vector lUdienze = new Vector();
		UdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSqlDAO(lConn);
			lUdiDao.ricercaUdienza(aUdienza);
			lUdienze = new Vector(lUdiDao.getModels());

			if (lUdienze.size() == 0)
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIUSException("UdienzaController.ExRicercaUdienza: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdienze;
	}

	/**
	 * Effettua la ricerca di una udienza, per data e del numero di Procedimenti ad essa assegnati.
	 *
	 * @param aUdienza
	 *            Istanza del model <code>Udienza</code>.
	 * @return l'insieme delle istanze model.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaUdienzaNumProc(UdienzaModel aUdienza) throws F3BException {

		Connection lConn = null;
		Vector lUdienze = ExRicercaUdienza(aUdienza);
		ProcedimentixUdienzaSqlDAO lProUdiDao = null;
		Vector lUdienzeN = null;
		if (lUdienze != null && lUdienze.size() > 0) {
			try {
				lConn = getDBConnection();
				lProUdiDao = new ProcedimentixUdienzaSqlDAO(lConn);

				lUdienzeN = new Vector();

				Iterator itx = lUdienze.iterator();
				while (itx.hasNext()) {
					UdienzaModel lUdienza = (UdienzaModel) itx.next();
					BigDecimal lNum = lProUdiDao.getNumProcedimentiXUdienza(lUdienza.getIdUdienza());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Num proc -> " + lNum);
					BigDecimal lNumRinvio = lProUdiDao.getNumProcDaRinvioXUdienza(lUdienza.getIdUdienza());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Num proc Da Rinvio -> " + lNumRinvio);
					lUdienzeN.add(new UdienzaNModel(lUdienza, lNum, lNumRinvio));
				}
			} catch (DAOException daoEx) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("DAOException: " + daoEx);
				throw new SIUSException("UdienzaController.ExRicercaUdienzaNumProc: " + daoEx);
			} finally {
				cleanup(lProUdiDao);
				cleanup(lConn);
			}
		}
		return lUdienzeN;
	}

	/**
	 * Effettua la ricerca di una udienza in UDS, per data.
	 *
	 * @param aUdienza
	 *            Istanza del model <code>Udienza</code>.
	 * @return l'insieme delle istanze model.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaUdienzaUDS(UdienzaModel aUdienza) throws F3BException {

		Connection lConn = null;
		Vector lUdienze = new Vector();
		UdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSqlDAO(lConn);
			lUdiDao.ricercaUdienzaUDS(aUdienza);
			lUdiDao.start();

			while (lUdiDao.next()) {
				UdienzaModel lUdiMod = new UdienzaModel();
				lUdiMod = (UdienzaModel) lUdiDao.getModelByKeyUDS();
				lUdienze.add(lUdiMod);
			}
			lUdiDao.stop();

			if (lUdienze.size() == 0)
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("UdienzaController.ExRicercaUdienzaUDS: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdienze;
	}

	/**
	 * Effettua La ricerca delle 30 udienze precedenti
	 *
	 * @param aUdienza
	 *            istanza della classe UdienzaModel
	 * @param aNumOccorrenze
	 *            numero di occorrenze da visualizzare.
	 * @return l'insieme delle istanze UdienzaModel.
	 * @throws F3BException
	 *             propagazione dell'errore di eccezione.
	 */
	public String ExRicercaUdienzePrecedenti(UdienzaModel aUdienza,
			GeneraleProcedimentoModel aGeneraleProcedimento) throws F3BException {

		Connection lConn = null;
		String sUdienze = new String();
		UdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSqlDAO(lConn);
			lUdiDao.ricercaUdienzePrecedenti(aUdienza, aGeneraleProcedimento);
			// lUdienze = new Vector(lUdiDao.getModels( aNumOccorrenze ));

			// carico la stringa con valori fittizi
			// sUdienze = "12/04/1997; 19/05/1999; fare la ricerca sulle tabelle ";

			lUdiDao.start();

			sUdienze = "";
			UdienzaModel aModel = new UdienzaModel();
			while (lUdiDao.next()) {
				aModel = (UdienzaModel) lUdiDao.getModelUdienzePrecedenti();
				sUdienze += DateUtils.getDateToString(aModel.getDataUdienza(), "dd-MM-yyyy");
				sUdienze += "; ";
			}
			lUdiDao.stop();

			if (sUdienze.equalsIgnoreCase("")) {
				sUdienze = " Nessuna data di udienza precedente a quella attuale.";
				// throw new
				// SIUSException(SIUSException.USER_MESSAGE,"Nessuna data di udienza precedente a quella
				// attuale.");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("UdienzaController.ExRicercaUdienzePrecedenti: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}

		return sUdienze;
	}

	/**
	 * Effettua La ricerca delle udienze ritornano l'elenco di esse, ma ponendo un limite al numero di
	 * occorrenze passato come argomento.
	 *
	 * @param aUdienza
	 *            istanza della classe UdienzaModel
	 * @param aNumOccorrenze
	 *            numero di occorrenze da visualizzare.
	 * @return l'insieme delle istanze UdienzaModel.
	 * @throws F3BException
	 *             propagazione dell'errore di eccezione.
	 */
	public Vector ExRicercaUdienza(UdienzaModel aUdienza, int aNumOccorrenze) throws F3BException {

		Connection lConn = null;
		Vector lUdienze = new Vector();
		UdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSqlDAO(lConn);
			lUdiDao.ricercaUdienza(aUdienza);
			lUdienze = new Vector(lUdiDao.getModels(aNumOccorrenze));

			if (lUdienze.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("UdienzaController.ExRicercaUdienza: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdienze;
	}

	/**
	 * Effettua La ricerca delle udienze ritornano l'elenco di esse, ma ponendo un limite al numero di
	 * occorrenze passato come argomento.
	 *
	 * @param aUdienza
	 *            istanza della classe UdienzaModel
	 * @param aNumOccorrenze
	 *            numero di occorrenze da visualizzare.
	 * @return l'insieme delle istanze UdienzaModel.
	 * @throws F3BException
	 *             propagazione dell'errore di eccezione.
	 */
	public Vector ExRicercaUdienzaGenerale(UdienzaModel aUdienza, int aNumOccorrenze) throws F3BException {

		Connection lConn = null;
		Vector lUdienze = new Vector();
		UdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSqlDAO(lConn);
			lUdiDao.ricercaUdienzaGenerale(aUdienza);
			lUdienze = new Vector(lUdiDao.getModels(aNumOccorrenze));

			if (lUdienze.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("UdienzaController.ExRicercaUdienzaGenerale: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdienze;
	}

	/**
	 * Effettua La ricerca delle udienze ritornano l'elenco di 30 di esse.
	 *
	 * @param aUdienza
	 *            istanza della classe UdienzaModel
	 * @param aNumOccorrenze
	 *            numero di occorrenze da visualizzare.
	 * @return l'insieme delle istanze UdienzaModel.
	 * @throws F3BException
	 *             propagazione dell'errore di eccezione.
	 */
	public Vector ExRicercaNuoveUdienze(UdienzaModel lUdienzaMod,
			GeneraleProcedimentoModel aGeneraleProcedimento) throws F3BException {

		Connection lConn = null;
		Vector lUdienze = new Vector();
		UdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSqlDAO(lConn);
			lUdiDao.ricercaNuoveUdienze(lUdienzaMod, aGeneraleProcedimento);

			lUdiDao.start();
			UdienzaModel aModel = new UdienzaModel();
			while (lUdiDao.next()) {
				aModel = (UdienzaModel) lUdiDao.getModelNuoveUdienze();
				lUdienze.add(aModel);
			}
			lUdiDao.stop();

			if (lUdienze.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("UdienzaController.ExRicercaNuoveUdienze: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdienze;
	}

	/**
	 * Ricerca Udienza attraverso la data passata come parametro.
	 *
	 * @param aDate
	 *            data con la quale ricercare l'udienza.
	 * @return
	 * @throws F3BException
	 */
	public UdienzaModel ExRicercaUdienzaByDate(Date aDate, String aCodUfficio) throws F3BException {

		Connection lConn = null;
		UdienzaSqlDAO lUdiDao = null;
		UdienzaModel lUdiMod;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSqlDAO(lConn);
			lUdiDao.ricercaUdienzaByDate(DateUtils.getDateToString(aDate, "yyyyMMdd"), aCodUfficio);
			lUdiMod = (UdienzaModel) lUdiDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("UdienzaController.ExRicercaUdienzaByDate: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}

		return lUdiMod;
	}

	/**
	 * Effettua ricerca di una udienza per la relativa chiave.
	 *
	 * @param aKey
	 *            chiave dell'udienza.
	 * @return l'istanza del UdienzaModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public UdienzaModel ExRicercaUdienzaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		UdienzaSqlDAO lUdiDao = null;
		UdienzaModel lUdiMod;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSqlDAO(lConn);
			lUdiDao.ricercaDattaglioUdienzaByKey(aKey);
			lUdiMod = (UdienzaModel) lUdiDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("UdienzaController.ExRicercaUdienzaByKey: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}

		return lUdiMod;
	}

	/**
	 * Effettua ricerca di una udienza per la relativa chiave.
	 *
	 * @param aKey
	 *            chiave dell'udienza.
	 * @return l'istanza del UdienzaModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public UdienzaModel ExRicercaUdienzaByKeyUDS(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		UdienzaSqlDAO lUdiDao = null;
		UdienzaModel lUdiMod;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSqlDAO(lConn);
			lUdiDao.ricercaDattaglioUdienzaByKeyUDS(aKey);
			lUdiDao.start();
			lUdiMod = new UdienzaModel();
			while (lUdiDao.next()) {
				lUdiMod = (UdienzaModel) lUdiDao.getModelByKeyUDS();
			}
			lUdiDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"UdienzaController.ExRicercaUdienzaByKeyExRicercaUdienzaByKeyUDS: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}

		return lUdiMod;
	}

	/**
	 * Modifica di una udienza.
	 *
	 * @param aUdienza
	 *            passaggio udienza model.
	 * @return ritorna udienza model.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public UdienzaModel ExModificaUdienza(UdienzaModel aUdienza) throws F3BException {

		Connection lConn = null;
		UdienzaDAO lUdiDao = null;
		UdienzaModel lUdiMod = new UdienzaModel(aUdienza);

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaDAO(lConn);
			lUdiDao.setDAOFromModelForUpdate(aUdienza);
			lUdiDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			if (ex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Aggiornamento impossibile: gia' esiste udienza nella stessa data per lo stesso collegio.");
			throw new SIUSException("UdienzaController.ExModific aUdienza: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	/**
	 * Modifica di una udienza per UDS.
	 *
	 * @param aUdienza
	 *            passaggio udienza model.
	 * @return ritorna udienza model.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public UdienzaModel ExModificaUdienzaUDS(UdienzaModel aUdienza) throws F3BException {

		Connection lConn = null;
		UdienzaDAO lUdiDao = null;
		UdienzaModel lUdiMod = new UdienzaModel(aUdienza);

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaDAO(lConn);
			lUdiDao.setDAOFromModelForUpdateUDS(aUdienza);
			lUdiDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			// if(ex.UNIQUE_CONSTRAINT_VIOLATED)
			// throw new
			// SIUSException(SIUSException.USER_MESSAGE,"Aggiornamento impossibile: gia' esiste udienza nella
			// stessa data per lo stesso collegio.");
			throw new SIUSException("UdienzaController.ExModificaUdienzaUDS: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	/**
	 * Modifica di una udienza aggiornando il GeneraleProcedimento ed inserendo un nuovo record su
	 * UdienzaProcedimento.
	 *
	 * @param aUdienza
	 *            passaggio udienza model, aGeneraleProcedimento.
	 * @return ritorna udienza model.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public UdienzaModel ExRinvioUdienzaVerbale(UdienzaModel aUdienza,
			GeneraleProcedimentoModel aGeneraleProcedimento, EventoModel aEvento, TenoreModel[] aTenori,
			// MEV_AVVOCATURA - Aggiunto Parametro
			Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato) throws F3BException {

		Connection lConn = null;
		GeneraleProcedimentoDAO lGenProcDao = null;
		UdienzaProcedimentoDAO lUdienzaProcDao = null;
		FascicoloSiusDAO lFasSiusDao = null;
		EventoDAO lEventoDao = null;
		TenoreDAO lTenDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		// MEV_AVVOCATURA - Aggiunta variabile
		AvvisiAvvocatoDAO lAvvisiAvvocatoDao = null;

		try {
			lConn = getDBTransaction();

			// Ricerca se esistono udienze per quel GP con stato F o S
			UdienzaProcedimentoModel lUdiMod = new UdienzaProcedimentoModel();
			IUdienzaProcedimento lCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
			lUdiMod = lCtrl.ExRicercaUdienzaProcedimentoByGenProFlagRinviata(
					aGeneraleProcedimento.getIdGeneraleProcedimento(), "'F','S'");

			if (lUdiMod == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Rinvio Udienza non consentito. Per il procedimento non risulta fissata una precedente data udienza.");

			// Iserisce un evento
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setDAOFromModel(aEvento);
			aEvento.setIdEvento(lEventoDao.insert());
			lEventoDao.stop();

			// Sulla tabella Udienza Procedimento effettuo l'update del flag_rinviata
			lUdienzaProcDao = new UdienzaProcedimentoDAO(lConn);

			if (aUdienza.getDataUdienza() != null) {
				// Esegue controllo se esiste già un rinvio udienza con la stessa udienza.
				lGenProcDao = new GeneraleProcedimentoDAO(lConn);
				if (lGenProcDao.isExistUdienzaByIdGenProcIdUdi(
						aGeneraleProcedimento.getIdGeneraleProcedimento(), aUdienza.getIdUdienza()))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Procedimento già fissato con l'udienza richiesta !");

				// Esegue Aggiornamento record UdienzaProcedimento
				lUdienzaProcDao.setFlagRinviata("R");
				lUdienzaProcDao.setUdiIdUdienzaRinvio(aUdienza.getIdUdienza());
				lUdienzaProcDao.setCodOperatoreAggiornamento(aUdienza.getCodOperatoreInserimento());
				lUdienzaProcDao.setCodUfficioAggiornamento(aUdienza.getCodUfficioInserimento());
				lUdienzaProcDao.setDataAggiornamento(DateUtils.getSysDate());
				lUdienzaProcDao.setCondizioneUpdateIdUdienzaProcedimento(lUdiMod);
				lUdienzaProcDao.update();
				lUdienzaProcDao.stop();

				// Esegue inserimento record udienza procedimento
				lUdienzaProcDao.setEveIdEvento(aEvento.getIdEvento());
				lUdienzaProcDao
						.setGenPridGeneraleProcedimento(aGeneraleProcedimento.getIdGeneraleProcedimento());
				lUdienzaProcDao.setUdiIdUdienza(aUdienza.getIdUdienza());
				lUdienzaProcDao.setDataInserimento(DateUtils.getSysDate());
				lUdienzaProcDao.setCodOperatoreInserimento(aUdienza.getCodOperatoreInserimento());
				lUdienzaProcDao.setCodUfficioInserimento(aUdienza.getCodUfficioInserimento());
				lUdienzaProcDao.setFlagRinviata("S");
				lUdienzaProcDao.insert();
				lUdienzaProcDao.stop();

				// Esegue aggiornmanto generale procedimento.
				lGenProcDao = new GeneraleProcedimentoDAO(lConn);
				lGenProcDao.setDataCameraConsiglio(aUdienza.getDataUdienza());
				lGenProcDao.setUdiIdUdienza(aUdienza.getIdUdienza());
				lGenProcDao.setCondizioneUpdate(aGeneraleProcedimento.getIdGeneraleProcedimento());
				lGenProcDao.update();
				lGenProcDao.stop();
			} else { // STUB 21/06/2004 Condizione di Rinvio a nuovo ruolo.
				// Esegue Aggiornamento ultimo record in (M).
				lUdienzaProcDao.setCodOperatoreAggiornamento(aUdienza.getCodOperatoreInserimento());
				lUdienzaProcDao.setCodUfficioAggiornamento(aUdienza.getCodUfficioInserimento());
				lUdienzaProcDao.setDataAggiornamento(DateUtils.getSysDate());
				lUdienzaProcDao.setFlagRinviata("M");
				lUdienzaProcDao.setCondizioneUpdateIdUdienzaProcedimento(lUdiMod);
				lUdienzaProcDao.update();
				lUdienzaProcDao.stop();

				// Esegue inserimento nuovo record come Nuovo Ruolo (N).
				lUdienzaProcDao
						.setGenPridGeneraleProcedimento(aGeneraleProcedimento.getIdGeneraleProcedimento());
				lUdienzaProcDao.setUdiIdUdienza(lUdiMod.getUdiIdUdienza());
				lUdienzaProcDao.setDataInserimento(DateUtils.getSysDate());
				lUdienzaProcDao.setCodOperatoreInserimento(aUdienza.getCodOperatoreInserimento());
				lUdienzaProcDao.setCodUfficioInserimento(aUdienza.getCodUfficioInserimento());
				lUdienzaProcDao.setFlagRinviata("N");
				lUdienzaProcDao.setEveIdEvento(aEvento.getIdEvento());
				lUdienzaProcDao.insert();
				lUdienzaProcDao.stop();

				// Fascicolo SIUS Rinviato a nuovo ruolo.
				lFasSiusDao = new FascicoloSiusDAO(lConn);
				lFasSiusDao.setCodStatoFascicolo("10");
				lFasSiusDao.setCondizioneUpdate(aGeneraleProcedimento.getFasSiuIdFascicoloSius());
				lFasSiusDao.update();
				lFasSiusDao.stop();

				// Esegue aggiornmanto generale procedimento.
				lGenProcDao = new GeneraleProcedimentoDAO(lConn);
				lGenProcDao.setDataCameraConsiglio(null);
				lGenProcDao.setUdiIdUdienza(null);
				lGenProcDao.setCondizioneUpdate(aGeneraleProcedimento.getIdGeneraleProcedimento());
				lGenProcDao.update();
				lGenProcDao.stop();
			}

			// Gestione Tenori
			lTenDao = new TenoreDAO(lConn);

			// I Tenori non vengono più cancellati ma chiusi
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("######## [ Fase di chiusura per il Tenore ] #########");

			TenoreModel lTenore = new TenoreModel();

			// Valorizzazione dei campi da aggiornare + update
			lTenore.setCodEsitoTenore("0603");
			// lTenore.setCodMagistrato(aEve.getCodMagistrato());
			lTenore.setData(aGeneraleProcedimento.getDataCameraConsiglio());
			lTenore.setCodOperatoreAggiornamento(aGeneraleProcedimento.getCodOperatoreAggiornamento());
			lTenore.setDataAggiornamento(DateUtils.getSysDate());
			lTenore.setCodUfficioAggiornamento(aGeneraleProcedimento.getCodUfficioAggiornamento());
			lTenore.setDataFine(DateUtils.getSysDate());
			lTenore.setGenPridGeneraleProcedimento(aGeneraleProcedimento.getIdGeneraleProcedimento());
			lTenDao.setDAOFromModelForUpdateDataFine(lTenore);

			lTenDao.update();
			lTenDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("######## [ Fase di inserimento Tenori ] #########");

			int lNumTenori = aTenori.length;
			for (int x = 0; x < lNumTenori; x++) {
				// Imposta l'id del generale procedimento nel tenore, prima di inserirlo
				// nel dbase.
				aTenori[x].setGenPridGeneraleProcedimento(aGeneraleProcedimento.getIdGeneraleProcedimento());
				// aTenori[x].setDepOpidDepositoOrdinanzaPc(lIdOrd);
				lTenDao.setDAOFromModel(aTenori[x]);
				aTenori[x].setIdTenore(lTenDao.insert());
				lTenDao.stop();
			}

			// Esegue la ricerca dei tenori sortati per peso esito tenore.
			lTenSqlDao = new TenoreSqlDAO(lConn);
			// Ricerca tenori x Id Generale Procedimento.
			lTenSqlDao.ricercaTenoriByGeneraleProcOrderByPeso(
					aGeneraleProcedimento.getIdGeneraleProcedimento());
			// lTenSqlDao.ricercaTenoriByGeneraleProcOrderByPeso(
			// aModel.getOrdinanza().getGenPridGeneraleProcedimento());
			Vector lTenori = new Vector(lTenSqlDao.getModels());

			if (lTenori.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Tenori assenti");

			TenoreModel[] lTenoriModel = (TenoreModel[]) lTenori.toArray(new TenoreModel[0]);

			// Aggiornamento del codice motivo dell'evento con il cod_oggetto_tenore del 1° record tenore.
			lEventoDao = new EventoDAO(lConn);

			aEvento.setCodMotivo((lTenoriModel[0]).getCodOggettoTenore());
			// Aggiornamento del codice motivo dell'evento con il cod_oggetto_tenore del 1° record tenore.
			aEvento.setDataAggiornamento(DateUtils.getSysDate());
			aEvento.setTenIdTenore((lTenoriModel[0]).getIdTenore());

			lEventoDao.setDAOFromModelForUpdate(aEvento);
			lEventoDao.update();
			lEventoDao.stop();

			// ******************************************************************************
			// MEV_AVVOCATURA - INIZIO -inserisco gli avvisi sulla tabella AVVISI_AVVOCATO
			// ******************************************************************************
			if (lAvvvisiAvvocato != null && lAvvvisiAvvocato.size() > 0) {
				lAvvisiAvvocatoDao = new AvvisiAvvocatoDAO(lConn);
				for (AvvisiAvvocatoModel avvisoAvvocato : lAvvvisiAvvocato) {
					// avvisoAvvocato.setIdProvvedimento(aEvento.getIdEvento());
					// setto idEvento (emma 22/08/2016)
					avvisoAvvocato.setIdEvento(aEvento.getIdEvento());
					lAvvisiAvvocatoDao.setDAOFromModel(avvisoAvvocato);
					lAvvisiAvvocatoDao.insert();
					lAvvisiAvvocatoDao.stop();
				}
			}
			// ************************
			// MEV_AVVOCATURA - FINE
			// ************************

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);

			if (daoex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Rinvio Udienza non consentito all'udienza selezionata.");
			throw new SIUSException("UdienzaController.ExRinvioUdienza: " + daoex);
		} catch (F3BException f3bex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("F3BException: " + f3bex);

			throw f3bex;
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);

			throw new SIUSException("UdienzaController.ExRinvioUdienza: " + ex);
		} finally {
			cleanup(lGenProcDao);
			cleanup(lUdienzaProcDao);
			cleanup(lFasSiusDao);
			cleanup(lEventoDao);
			cleanup(lTenDao);
			cleanup(lTenSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lAvvisiAvvocatoDao);
			cleanup(lConn);
		}

		return aUdienza;
	}

	/**
	 * Effettua la concellazione di una udienza.
	 *
	 * @param aUdienza
	 *            udienza model.
	 * @throws F3BException
	 *             propaga gli errorei di eccezione.
	 */
	public void ExCancellaUdienza(UdienzaModel aUdienza) throws F3BException {

		Connection lConn = null;
		UdienzaDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaDAO(lConn);
			lUdiDao.setCondizioneUpdate(aUdienza.getIdUdienza());

			lUdiDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicoli gia' fissati per l'udienza. Impossibile effettuare la Cancellazione!");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("UdienzaController.ExCancellaUdienza: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
	}

	/**
	 * Inserisci FIssazioen Udienza
	 *
	 * @param aUdienza
	 * @param aFasc
	 * @param aEve
	 * @return
	 * @throws F3BException
	 */
	public UdienzaProcedimentoModel ExInserisciFissazioneUdienza(UdienzaProcedimentoModel aNuovaUdienzaProc,
			FascicoloGPModel aFasc, EventoNotificaModel aEve, TenoreModel[] aTenori,
			UdienzaProcedimentoModel aVecchiaUdienzaProc) throws F3BException {

		Connection lConn = null;

		UdienzaProcedimentoDAO lUdiDao = null;
		GeneraleProcedimentoDAO lGenDAO = null;
		EventoDAO lEveDao = null;
		AvvocatoFascicoloSiusDAO lAvvFasSiusDAO = null;
		TenoreDAO lTenDao = null;
		DepositoDecretoDAO lDepDao = null;
		DepositoDecretoModel lDepMod = null;
		TenoreSqlDAO lTenoreSqlDao = null;
		FascicoloSiusDAO lFasSiuDAO = null;

		if (aFasc == null || aFasc.getFascicoloSiusModel() == null
				|| aFasc.getFascicoloSiusModel().getIdFascicoloSius() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Impossibile aggiornare il Fascicolo SIUS");
		if (aFasc.getGeneraleProcedimentoModel() == null
				|| aFasc.getGeneraleProcedimentoModel().getIdGeneraleProcedimento() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Impossibile aggiornare il Generale Procedimento");

		try {
			EventoNotificaModel lEve = new EventoNotificaModel();

			// Prende una connessione in transazione.
			lConn = getDBTransaction();

			// Chiamata al Controller per inserimento EVENTO NOTIFICA
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			lEve = lCtrlEve.ExInserisciEventoNotifica(aEve, lConn);

			// Inserimento di un nuovo record in UDIENZA_PROCEDIMENTO.
			lUdiDao = new UdienzaProcedimentoDAO(lConn);
			aNuovaUdienzaProc.seEveIdEvento(lEve.getEvento().getIdEvento());
			lUdiDao.setDAOFromModel(aNuovaUdienzaProc);
			BigDecimal lKey = null;
			lKey = lUdiDao.insert();
			lUdiDao.stop();
			aNuovaUdienzaProc.setIdUdienzaProcedimento(lKey);

			// Eventuale aggiornamento del vecchio record in UDIENZA_PROCEDIMENTO
			if (aVecchiaUdienzaProc != null && aVecchiaUdienzaProc.getIdUdienzaProcedimento() != null) {
				lUdiDao.setDAOFromModelForUpdateFissazione(aVecchiaUdienzaProc);
				lUdiDao.update();
				lUdiDao.stop();
			}

			// Aggiornamento di GENERALE_PROCEDIMENTO
			lGenDAO = new GeneraleProcedimentoDAO(lConn);
			lGenDAO.setDataCameraConsiglio(aFasc.getGeneraleProcedimentoModel().getDataCameraConsiglio());
			// STUB 15/04/2004 E.A. Mancava la valorizzazione del COD_CONTENUTO impostato in fissazione
			// Udienza, e i dati di aggiornamento.
			lGenDAO.setCodOggettoProcedimento(
					aFasc.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
			lGenDAO.setCodUfficioAggiornamento(
					aFasc.getGeneraleProcedimentoModel().getCodUfficioAggiornamento());
			lGenDAO.setCodOperatoreAggiornamento(
					aFasc.getGeneraleProcedimentoModel().getCodOperatoreAggiornamento());
			lGenDAO.setDataAggiornamento(aFasc.getGeneraleProcedimentoModel().getDataAggiornamento());
			lGenDAO.setUdiIdUdienza(aNuovaUdienzaProc.getUdiIdUdienza());
			lGenDAO.setCondizioneUpdate(aFasc.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lGenDAO.update();
			lGenDAO.stop();

			// STUB 21/06/2004 Aggiornamento COD_STATO_FASCICOLO.
			lFasSiuDAO = new FascicoloSiusDAO(lConn);
			lFasSiuDAO.setCodStatoFascicolo("02");
			lFasSiuDAO.setCondizioneUpdate(aFasc.getFascicoloSiusModel().getIdFascicoloSius());
			lFasSiuDAO.update();
			lFasSiuDAO.stop();

			// Inserimento Deposito Decreto
			lDepMod = new DepositoDecretoModel();
			lDepMod.setCodTipoDecreto(ICostantiDepositoDecreto.CITAZIONE);
			lDepMod.setDataEmissione(lEve.getEvento().getDataEmissione());
			lDepMod.setDataInserimento(lEve.getEvento().getDataInserimento());
			lDepMod.setCodOperatoreInserimento(lEve.getEvento().getCodOperatoreInserimento());
			lDepMod.setCodUfficioInserimento(lEve.getEvento().getCodUfficioInserimento());
			lDepMod.setGenPridGeneraleProcedimento(
					aFasc.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lDepMod.setIdEventoGenerato(lEve.getEvento().getIdEvento());
			lDepMod.setNote(aFasc.getGeneraleProcedimentoModel().getAnnotazione());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID EVENTO -> " + lEve.getEvento().getIdEvento().toString());

			lDepDao = new DepositoDecretoDAO(lConn);
			lDepDao.setDAOFromModel(lDepMod);
			BigDecimal lKeyDepDec = null;
			lKeyDepDec = lDepDao.insert();
			lDepDao.stop();
			lDepMod.setIdDepositoDecreto(lKeyDepDec);

			// Gestione dei Tenori.
			lTenDao = new TenoreDAO(lConn);

			// I Tenori non vengono più cancellati ma chiusi ! Luigi 10-12-2003
			TenoreModel lTenore = new TenoreModel();
			// Valorizzazione dei campi da aggiornare + update
			lTenore.setCodOperatoreAggiornamento(
					aFasc.getGeneraleProcedimentoModel().getCodOperatoreAggiornamento());
			lTenore.setCodUfficioAggiornamento(
					aFasc.getGeneraleProcedimentoModel().getCodUfficioAggiornamento());
			lTenore.setDataAggiornamento(aFasc.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setDataFine(aFasc.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setGenPridGeneraleProcedimento(
					aFasc.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lTenDao.setDAOFromModelForUpdateDataFine(lTenore);
			lTenDao.update();
			lTenDao.stop();

			int lNumTenori = aTenori.length;
			for (int x = 0; x < lNumTenori; x++) {
				// Imposta l'id del generale procedimento nel tenore, prima di inserirlo
				// nel dbase.
				aTenori[x].setGenPridGeneraleProcedimento(
						aFasc.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				aTenori[x].setDepDecIdDepositoDecreto(lKeyDepDec);
				aTenori[x].setData(lEve.getEvento().getDataEmissione());
				aTenori[x].setCodEsitoTenore("0601");
				lTenDao.setDAOFromModel(aTenori[x]);
				lTenDao.insert();
				lTenDao.stop();
			}

			// Select dati dal tenore.
			lTenoreSqlDao = new TenoreSqlDAO(lConn);
			lTenoreSqlDao.ricercaTenoriByDecretoOrderByPeso(lKeyDepDec);
			TenoreModel lTenoreMod = (TenoreModel) lTenoreSqlDao.getModelByKey();
			if (lTenoreMod != null) {
				// throw new SIUSException("UdienzaController.ExInserisciFissazioneUdienza: tenori assenti ");

				// Update Evento
				// Imposta COD_MOTIVO e IdTenore, nel model.
				lEveDao = new EventoDAO(lConn);
				lEveDao.setCodMotivo(lTenoreMod.getCodOggettoTenore());
				lEveDao.setTenIdTenore(lTenoreMod.getIdTenore());
				lEveDao.selCondizioneUpdate(lEve.getEvento().getIdEvento());
				lEveDao.update();
				lEveDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);

			// Controlla l'esistenza di un procedimento
			// per l'udienza richiesta.
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Esiste già un procedimento per l'udienza richiesta!");

			throw new SIUSException("UdienzaController.ExInserisciFissazioneUdienza: " + daoEx);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new SIUSException("UdienzaController.ExInserisciFissazioneUdienza: " + sqe);
		} finally {
			cleanup(lTenoreSqlDao);
			cleanup(lAvvFasSiusDAO);
			cleanup(lEveDao);
			cleanup(lGenDAO);
			cleanup(lUdiDao);
			cleanup(lTenDao);
			cleanup(lDepDao);
			cleanup(lFasSiuDAO);

			cleanup(lConn);
		}
		return aNuovaUdienzaProc;
	}

	/**
	 * Cancella la Fissazione Udienza.
	 *
	 * @param aUdienza
	 *            udienza model.
	 * @throws F3BException
	 *             propaga gli errorei di eccezione.
	 */
	// STUB: Da ultimare ! Non utilizzabile ! Luigi 26-11-2004
	public void ExCancellaFissazioneUdienza(BigDecimal aIdDecreto, String aCodUtente, String aCodUfficio)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		GeneraleProcedimentoDAO lGenDAO = null;
		UdienzaModel lUdienza = null;
		DepositoDecretoModel lDepDec = null;
		BigDecimal lIdEvento = null;
		BigDecimal lIdGenPro = null;

		try {
			// ricerca del record DEPOSITO_DECRETO da cancellare
			IDepositoDecreto lDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
			lDepDec = lDecrCtrl.ExRicercaDepositoDecretoByKey(aIdDecreto);
			if (lDepDec == null)
				throw new SIUSException("record DepositoDecreto inesistente");

			lIdEvento = lDepDec.getIdEventoGenerato();
			if (lIdEvento == null)
				throw new SIUSException("ID Evento non valorizzato nel Decreto");

			lIdGenPro = lDepDec.getGenPridGeneraleProcedimento();
			if (lIdGenPro == null)
				throw new SIUSException("ID Generale Procedimento non valorizzato nel Decreto");

			IFascicoloSius lFas = SIUSLookupRemote.getFascicoloSiusRemote();
			/* FascicoloGPModel lFascicoloGPModel = */
			lFas.ExRicercaFascicoloByKey(lDepDec.getGenPridGeneraleProcedimento());
			// notifica per Udienza.
			// BigDecimal lIdUdienza = lFascicoloGPModel.getGeneraleProcedimentoModel().getUdiIdUdienza();

			lConn = getDBConnection();

			// Cancellazione Notifiche
			lNotDao = new NotificaDAO(lConn);
			lNotDao.setCondizioneEvento(lIdEvento);
			lNotDao.delete();
			lNotDao.stop();

			// Cancellazione Campo Note
			lCampoNotaDao = new CampoNotaDAO(lConn);
			lCampoNotaDao.setCondizioneEvento(lIdEvento);
			lCampoNotaDao.delete();
			lCampoNotaDao.stop();

			// Cancellazione Evento
			lEveDao = new EventoDAO(lConn);
			lEveDao.selCondizioneUpdate(lIdEvento);
			lEveDao.delete();
			lEveDao.stop();

			// Cancellazione dal procedimento DAO

			// Update di GENERALE_PROCEDIMENTO
			BigDecimal lIdGenProcedimento = null;
			lUdienza = new UdienzaModel();
			lGenDAO = new GeneraleProcedimentoDAO(lConn);

			// DA CAMBIARE !!
			lGenDAO.setDataCameraConsiglio(lUdienza.getDataUdienza());
			lGenDAO.setUdiIdUdienza(lUdienza.getIdUdienza());

			// STUB
			lGenDAO.setCodUfficioAggiornamento(aCodUfficio);
			lGenDAO.setCodOperatoreAggiornamento(aCodUtente);

			lGenDAO.setDataAggiornamento(DateUtils.getSysDate());

			lGenDAO.setCondizioneUpdate(lIdGenProcedimento);
			lGenDAO.update();
			lGenDAO.stop();

			// FINE
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicoli gia' fissati per l'udienza. Impossibile effettuare la Cancellazione!");

			throw new SIUSException("UdienzaController.ExCancellaFissazioneUdienza:  " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new SIUSException("UdienzaController.ExCancellaFissazioneUdienza: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lCampoNotaDao);
			cleanup(lNotDao);
			cleanup(lGenDAO);
			cleanup(lConn);
		}
	}

	/**
	 * Inserisci OrdinanzaRinvioUdienza
	 *
	 * @param aUdienza
	 *            Dati udienza.
	 * @param aFasc
	 *            dati fascicolo SIUS.
	 * @param aEve
	 *            dati evento.
	 * @param aTenori
	 *            dati tenori.
	 * @param lDepositoOrdinanzaPc
	 *            dati deposito ordinanza
	 * @param aGeneraleProcedimentoold
	 *            dati Generale procedimento.
	 * @return
	 *
	 * @throws F3BException
	 *             propaga errore di eccezione
	 */
	public EventoModel ExInserisciOrdinanzaRinvioUdienza(UdienzaModel aUdienza, FascicoloGPModel aFasc,
			EventoModel aEve, TenoreModel[] aTenori, DepositoOrdinanzaPcModel lDepositoOrdinanzaPc,
			GeneraleProcedimentoModel aGeneraleProcedimentoold) throws F3BException {

		Connection lConn = null;

		GeneraleProcedimentoDAO lGenProcDao = null;
		UdienzaProcedimentoDAO lUdienzaProcDao = null;
		UdienzaProcedimentoDAO lUdiProcDao = null;
		FascicoloSiusDAO lFasSiusDao = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		EventoSqlDAO lSqlDAO = null;
		AvvocatoFascicoloSiusDAO lAvvFasSiusDAO = null;
		TenoreDAO lTenDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		DepositoOrdinanzaPcDAO lDepOrdDAO = null;

		EventoModel lEve = new EventoModel();

		try {
			lConn = getDBTransaction();

			// Si caricano i dati del vecchio model.
			lEve = aEve;

			// Esegue inserimento evento.
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(lEve);
			lEve.setIdEvento(lEveDao.insert());
			lEveDao.stop();

			// Ricerca se esistono udienze per quel GP con stato F o S
			UdienzaProcedimentoModel lUdiMod = new UdienzaProcedimentoModel();
			IUdienzaProcedimento lCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
			lUdiMod = lCtrl.ExRicercaUdienzaProcedimentoByGenProFlagRinviata(
					aGeneraleProcedimentoold.getIdGeneraleProcedimento(), "'F','S'");
			if (lUdiMod == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Rinvio Udienza non consentito. Per il procedimento non risulta fissata una precedente data udienza.");

			// Sulla tabella Udienza Procedimento effettuo l'update del flag_rinviata
			lUdiProcDao = new UdienzaProcedimentoDAO(lConn);

			if (aUdienza.getDataUdienza() != null) {
				lGenProcDao = new GeneraleProcedimentoDAO(lConn);
				if (lGenProcDao.isExistUdienzaByIdGenProcIdUdi(
						aGeneraleProcedimentoold.getIdGeneraleProcedimento(), aUdienza.getIdUdienza()))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Procedimento già fissato con l'udienza richiesta !");

				lUdiProcDao.setFlagRinviata("R");
				lUdiProcDao.setUdiIdUdienzaRinvio(aUdienza.getIdUdienza());
				lUdiProcDao.setCodOperatoreAggiornamento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioAggiornamento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setDataAggiornamento(DateUtils.getSysDate());
				lUdiProcDao.setCondizioneUpdateIdUdienzaProcedimento(lUdiMod);
				lUdiProcDao.update();

				lUdienzaProcDao = new UdienzaProcedimentoDAO(lConn);
				lUdienzaProcDao
						.setGenPridGeneraleProcedimento(aGeneraleProcedimentoold.getIdGeneraleProcedimento());
				lUdienzaProcDao.setUdiIdUdienza(aUdienza.getIdUdienza());
				lUdienzaProcDao.setDataInserimento(DateUtils.getSysDate());
				lUdienzaProcDao.setCodOperatoreInserimento(aUdienza.getCodOperatoreInserimento());
				lUdienzaProcDao.setCodUfficioInserimento(aUdienza.getCodUfficioInserimento());
				lUdienzaProcDao.setFlagRinviata("S");
				lUdienzaProcDao.setEveIdEvento(lEve.getIdEvento());
				lUdienzaProcDao.insert();
				lUdienzaProcDao.stop();

				lGenProcDao = new GeneraleProcedimentoDAO(lConn);
				lGenProcDao.setDataCameraConsiglio(aUdienza.getDataUdienza());
				lGenProcDao.setAnnotazione(aFasc.getGeneraleProcedimentoModel().getAnnotazione());
				lGenProcDao.setUdiIdUdienza(aUdienza.getIdUdienza());
				lGenProcDao.setCondizioneUpdate(aGeneraleProcedimentoold.getIdGeneraleProcedimento());
				lGenProcDao.update();
				lGenProcDao.stop();
			} else // STUB 21/06/2004 Condizione di Rinvio a nuovo ruolo.
			{
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Prima Update record ....");

				// 20071123
				// Esegue Aggiornamento ultimo record in (M).
				lUdiProcDao.setCodOperatoreAggiornamento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioAggiornamento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setDataAggiornamento(DateUtils.getSysDate());
				lUdiProcDao.setFlagRinviata("M");
				lUdiProcDao.setCondizioneUpdateIdUdienzaProcedimento(lUdiMod);
				lUdiProcDao.update();
				lUdiProcDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Dopo Update record ....");

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Prima Insert record ....");

				// 20071123
				// Esegue inserimento nuovo record come Nuovo Ruolo (N).
				lUdiProcDao
						.setGenPridGeneraleProcedimento(aGeneraleProcedimentoold.getIdGeneraleProcedimento());
				lUdiProcDao.setUdiIdUdienza(lUdiMod.getUdiIdUdienza());
				lUdiProcDao.setDataInserimento(DateUtils.getSysDate());
				lUdiProcDao.setCodOperatoreInserimento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioInserimento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setFlagRinviata("N");
				lUdiProcDao.setEveIdEvento(lEve.getIdEvento());
				lUdiProcDao.insert();
				lUdiProcDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Dopo Insert record ....");

				// Fascicolo SIUS Rinviato a nuovo ruolo.
				lFasSiusDao = new FascicoloSiusDAO(lConn);
				lFasSiusDao.setCodStatoFascicolo("10");
				lFasSiusDao
						.setCondizioneUpdate(aFasc.getGeneraleProcedimentoModel().getFasSiuIdFascicoloSius());
				lFasSiusDao.update();
				lFasSiusDao.stop();

				lGenProcDao = new GeneraleProcedimentoDAO(lConn);
				lGenProcDao.setDataCameraConsiglio(null);
				lGenProcDao.setAnnotazione(aFasc.getGeneraleProcedimentoModel().getAnnotazione());
				// 07/10/2003 Rework per inserire il collegamento tra il generale Procedimento e l'udienza.
				lGenProcDao.setUdiIdUdienza(null);
				lGenProcDao.setCondizioneUpdate(
						aFasc.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lGenProcDao.update();
				lGenProcDao.stop();
			}

			// Gestione dei Tenori.
			lTenDao = new TenoreDAO(lConn);

			// I Tenori non vengono più cancellati ma chiusi ! Luigi 10-12-2003
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Fase di chiusura per il Tenore");
			TenoreModel lTenore = new TenoreModel();
			// Valorizzazione dei campi da aggiornare + update
			lTenore.setData(aFasc.getGeneraleProcedimentoModel().getDataCameraConsiglio());
			lTenore.setCodOperatoreAggiornamento(
					aFasc.getGeneraleProcedimentoModel().getCodOperatoreAggiornamento());
			lTenore.setDataAggiornamento(aFasc.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setCodUfficioAggiornamento(
					aFasc.getGeneraleProcedimentoModel().getCodUfficioAggiornamento());
			lTenore.setDataFine(aFasc.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setGenPridGeneraleProcedimento(
					aFasc.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

			lTenDao.setDAOFromModelForUpdateDataFine(lTenore);
			lTenDao.update();
			lTenDao.stop();

			// Inserimento Ordinanza.
			lDepOrdDAO = new DepositoOrdinanzaPcDAO(lConn);
			lDepositoOrdinanzaPc.setDataCameraConsiglio(lEve.getDataEmissione());
			lDepositoOrdinanzaPc.setIdEventoGenerato(lEve.getIdEvento());
			lDepOrdDAO.setDAOFromModel(lDepositoOrdinanzaPc);
			BigDecimal lIdOrd = lDepOrdDAO.insert();
			lDepOrdDAO.stop();

			int lNumTenori = aTenori.length;
			for (int x = 0; x < lNumTenori; x++) {
				// Imposta l'id del generale procedimento nel tenore, prima di inserirlo
				// nel dbase.
				aTenori[x].setGenPridGeneraleProcedimento(
						aFasc.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				aTenori[x].setDepOpidDepositoOrdinanzaPc(lIdOrd);
				lTenDao.setDAOFromModel(aTenori[x]);
				aTenori[x].setIdTenore(lTenDao.insert());
				lTenDao.stop();
			}

			// Esegue la ricerca dei tenori sortati per peso esito tenore.
			lTenSqlDao = new TenoreSqlDAO(lConn);
			// Ricerca tenori x Id Deposito Ordinanza PC.
			lTenSqlDao.ricercaTenoriByOrdinanzaOrderByPeso(lIdOrd);
			// lTenSqlDao.ricercaTenoriByGeneraleProcOrderByPeso(
			// aModel.getOrdinanza().getGenPridGeneraleProcedimento());
			Vector lTenori = new Vector(lTenSqlDao.getModels());
			if (lTenori.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Tenori assenti");

			TenoreModel[] lTenoriModel = (TenoreModel[]) lTenori.toArray(new TenoreModel[0]);

			// Aggiornamento del codice motivo dell'evento con il cod_oggetto_tenore del 1° record tenore.
			lEveDao = new EventoDAO(lConn);
			lEve.setCodMotivo((lTenoriModel[0]).getCodOggettoTenore());
			// Aggiornamento del codice motivo dell'evento con il cod_oggetto_tenore del 1° record tenore.
			lEve.setDataAggiornamento(DateUtils.getSysDate());
			lEve.setTenIdTenore((lTenoriModel[0]).getIdTenore());
			lEveDao.setDAOFromModelForUpdate(lEve);
			lEveDao.update();
			lEveDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);

			// Controlla l'esistenza di un procedimento
			// per l'udienza richiesta.
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Esiste già un procedimento per l'udienza richiesta!");

			throw new SIUSException("UdienzaController.ExInserisciOrdinanzaRinvioUdienza : " + daoEx);
		} catch (F3BException f3bex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("F3BException: " + f3bex);
			throw f3bex;
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			throw new SIUSException("UdienzaController.ExInserisciOrdinanzaRinvioUdienza : " + ex);
		} finally {
			cleanup(lAvvFasSiusDAO);
			cleanup(lEveDao);
			cleanup(lSqlDAO);
			cleanup(lNotDao);
			cleanup(lGenProcDao);
			cleanup(lUdiProcDao);
			cleanup(lUdienzaProcDao);
			cleanup(lTenDao);
			cleanup(lTenSqlDao);
			cleanup(lDepOrdDAO);
			cleanup(lFasSiusDao); // 28/04/2009
			cleanup(lConn);
		}
		return lEve;
	}

	/**
	 * Esecuzione stampa Verbale Udienza
	 *
	 * @param aModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaVerbaleUdienza(EventoModel lEvento, UfficioModel lUfficio,
			UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		// EventoNotificaModel lEveNotifica = null;

		// Generazione documento di stampa
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
		lByteArrayOut = lCtrlSta.ExPreStampaVerbaleUdienza(lEvento, lUfficio.getCodUfficio(), aUtenteModel);

		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug( ">>>>>> Generato il Documento ." );
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("EVENTO >>> "+lEvento.toString() );
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
			throw new F3BException(
					"UdienzaController.ExStampaVerbaleUdienza: Non posso inserire il documento nell'evento : "
							+ daoex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Esecuzione stampa Fissazione Udienza
	 *
	 * @param aModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaFissazioneUdienza(EventoModel lEvento, UfficioModel lUfficio,
			UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		// EventoNotificaModel lEveNotifica = null;

		// Generazione documento di stampa
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
		lByteArrayOut = lCtrlSta.ExPreStampaFissazioneUdienza(lEvento, lUfficio.getCodUfficio(),
				aUtenteModel);

		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug( ">>>>>> Generato il Documento ." );
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("EVENTO >>> "+lEvento.toString() );
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
			throw new F3BException(
					"UdienzaController.ExStampaFissazioneUdienza: Non posso inserire il documento nell'evento : "
							+ daoex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Inserisci Sentenza Rinvio Udienza
	 *
	 * @param aUdienza
	 *            Dati udienza.
	 * @param aFasc
	 *            dati fascicolo SIUS.
	 * @param aEve
	 *            dati evento.
	 * @param aTenori
	 *            dati tenori.
	 * @param lDepositoSentenza
	 *            dati deposito sentenza
	 * @param aGeneraleProcedimentoold
	 *            dati Generale procedimento.
	 * @return EventoModel
	 *
	 * @throws F3BException
	 *             propaga errore di eccezione
	 */
	public EventoModel ExInserisciSentenzaRinvioUdienza(UdienzaModel aUdienza, FascicoloGPModel aFasc,
			EventoModel aEve, TenoreModel[] aTenori, DepositoSentenzaModel lDepositoSentenza,
			GeneraleProcedimentoModel aGeneraleProcedimentoold) throws F3BException {

		Connection lConn = null;

		GeneraleProcedimentoDAO lGenProcDao = null;
		UdienzaProcedimentoDAO lUdienzaProcDao = null;
		UdienzaProcedimentoDAO lUdiProcDao = null;
		FascicoloSiusDAO lFasSiusDao = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		EventoSqlDAO lSqlDAO = null;
		AvvocatoFascicoloSiusDAO lAvvFasSiusDAO = null;
		TenoreDAO lTenDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		DepositoSentenzaDAO lDepSenDao = null;

		EventoModel lEve = new EventoModel();

		try {
			lConn = getDBTransaction();

			// Si caricano i dati del vecchio model.
			lEve = aEve;

			// Esegue inserimento evento.
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(lEve);
			lEve.setIdEvento(lEveDao.insert());
			lEveDao.stop();

			// Ricerca se esistono udienze per quel GP con stato F o S
			UdienzaProcedimentoModel lUdiMod = new UdienzaProcedimentoModel();
			IUdienzaProcedimento lCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
			lUdiMod = lCtrl.ExRicercaUdienzaProcedimentoByGenProFlagRinviata(
					aGeneraleProcedimentoold.getIdGeneraleProcedimento(), "'F','S'");
			if (lUdiMod == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Rinvio Udienza non consentito. Per il procedimento non risulta fissata una precedente data udienza.");

			// Sulla tabella Udienza Procedimento effettuo l'update del flag_rinviata
			lUdiProcDao = new UdienzaProcedimentoDAO(lConn);

			if (aUdienza.getDataUdienza() != null) {
				lGenProcDao = new GeneraleProcedimentoDAO(lConn);
				if (lGenProcDao.isExistUdienzaByIdGenProcIdUdi(
						aGeneraleProcedimentoold.getIdGeneraleProcedimento(), aUdienza.getIdUdienza()))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Procedimento già fissato con l'udienza richiesta !");

				lUdiProcDao.setFlagRinviata("R");
				lUdiProcDao.setUdiIdUdienzaRinvio(aUdienza.getIdUdienza());
				lUdiProcDao.setCodOperatoreAggiornamento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioAggiornamento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setDataAggiornamento(DateUtils.getSysDate());
				lUdiProcDao.setCondizioneUpdateIdUdienzaProcedimento(lUdiMod);
				lUdiProcDao.update();

				lUdienzaProcDao = new UdienzaProcedimentoDAO(lConn);
				lUdienzaProcDao
						.setGenPridGeneraleProcedimento(aGeneraleProcedimentoold.getIdGeneraleProcedimento());
				lUdienzaProcDao.setUdiIdUdienza(aUdienza.getIdUdienza());
				lUdienzaProcDao.setDataInserimento(DateUtils.getSysDate());
				lUdienzaProcDao.setCodOperatoreInserimento(aUdienza.getCodOperatoreInserimento());
				lUdienzaProcDao.setCodUfficioInserimento(aUdienza.getCodUfficioInserimento());
				lUdienzaProcDao.setFlagRinviata("S");
				lUdienzaProcDao.setEveIdEvento(lEve.getIdEvento());
				lUdienzaProcDao.insert();
				lUdienzaProcDao.stop();

				lGenProcDao = new GeneraleProcedimentoDAO(lConn);
				lGenProcDao.setDataCameraConsiglio(aUdienza.getDataUdienza());
				lGenProcDao.setAnnotazione(aFasc.getGeneraleProcedimentoModel().getAnnotazione());
				lGenProcDao.setUdiIdUdienza(aUdienza.getIdUdienza());
				lGenProcDao.setCondizioneUpdate(aGeneraleProcedimentoold.getIdGeneraleProcedimento());
				lGenProcDao.update();
				lGenProcDao.stop();
			} else // Condizione di Rinvio a nuovo ruolo.
			{
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Prima Update record ....");

				// Esegue Aggiornamento ultimo record in (M).
				lUdiProcDao.setCodOperatoreAggiornamento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioAggiornamento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setDataAggiornamento(DateUtils.getSysDate());
				lUdiProcDao.setFlagRinviata("M");
				lUdiProcDao.setCondizioneUpdateIdUdienzaProcedimento(lUdiMod);
				lUdiProcDao.update();
				lUdiProcDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Dopo Update record ....");

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Prima Insert record ....");

				// Esegue inserimento nuovo record come Nuovo Ruolo (N).
				lUdiProcDao
						.setGenPridGeneraleProcedimento(aGeneraleProcedimentoold.getIdGeneraleProcedimento());
				lUdiProcDao.setUdiIdUdienza(lUdiMod.getUdiIdUdienza());
				lUdiProcDao.setDataInserimento(DateUtils.getSysDate());
				lUdiProcDao.setCodOperatoreInserimento(aUdienza.getCodOperatoreInserimento());
				lUdiProcDao.setCodUfficioInserimento(aUdienza.getCodUfficioInserimento());
				lUdiProcDao.setFlagRinviata("N");
				lUdiProcDao.setEveIdEvento(lEve.getIdEvento());
				lUdiProcDao.insert();
				lUdiProcDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Dopo Insert record ....");

				// Fascicolo SIUS Rinviato a nuovo ruolo.
				lFasSiusDao = new FascicoloSiusDAO(lConn);
				lFasSiusDao.setCodStatoFascicolo("10");
				lFasSiusDao
						.setCondizioneUpdate(aFasc.getGeneraleProcedimentoModel().getFasSiuIdFascicoloSius());
				lFasSiusDao.update();
				lFasSiusDao.stop();

				lGenProcDao = new GeneraleProcedimentoDAO(lConn);
				lGenProcDao.setDataCameraConsiglio(null);
				lGenProcDao.setAnnotazione(aFasc.getGeneraleProcedimentoModel().getAnnotazione());
				// Rework per inserire il collegamento tra il generale Procedimento e l'udienza.
				lGenProcDao.setUdiIdUdienza(null);
				lGenProcDao.setCondizioneUpdate(
						aFasc.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lGenProcDao.update();
				lGenProcDao.stop();
			}

			// Gestione dei Tenori.
			lTenDao = new TenoreDAO(lConn);

			// I Tenori non vengono più cancellati ma chiusi !
			TenoreModel lTenore = new TenoreModel();
			// Valorizzazione dei campi da aggiornare + update
			lTenore.setData(aFasc.getGeneraleProcedimentoModel().getDataCameraConsiglio());
			lTenore.setCodOperatoreAggiornamento(
					aFasc.getGeneraleProcedimentoModel().getCodOperatoreAggiornamento());
			lTenore.setDataAggiornamento(aFasc.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setCodUfficioAggiornamento(
					aFasc.getGeneraleProcedimentoModel().getCodUfficioAggiornamento());
			lTenore.setDataFine(aFasc.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setGenPridGeneraleProcedimento(
					aFasc.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

			lTenDao.setDAOFromModelForUpdateDataFine(lTenore);
			lTenDao.update();
			lTenDao.stop();

			// Inserimento Deposito Sentenza
			lDepSenDao = new DepositoSentenzaDAO(lConn);
			lDepositoSentenza.setDataEmissione(lEve.getDataEmissione());
			lDepositoSentenza.setIdEventoGenerato(lEve.getIdEvento());
			lDepSenDao.setDAOFromModel(lDepositoSentenza);
			BigDecimal lIdSen = lDepSenDao.insert();
			lDepSenDao.stop();

			int lNumTenori = aTenori.length;
			for (int x = 0; x < lNumTenori; x++) {
				// Imposta l'id del generale procedimento nel tenore, prima di inserirlo
				// nel dbase.
				aTenori[x].setGenPridGeneraleProcedimento(
						aFasc.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				aTenori[x].setDepIdDepositoSentenza(lIdSen);
				lTenDao.setDAOFromModel(aTenori[x]);
				aTenori[x].setIdTenore(lTenDao.insert());
				lTenDao.stop();
			}

			// Esegue la ricerca dei tenori sortati per peso esito tenore.
			lTenSqlDao = new TenoreSqlDAO(lConn);
			// Ricerca tenori x Id Deposito Ordinanza PC.
			lTenSqlDao.ricercaTenoriBySentenzaOrderByPeso(lIdSen);

			Vector lTenori = new Vector(lTenSqlDao.getModels());
			if (lTenori.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Tenori assenti");

			TenoreModel[] lTenoriModel = (TenoreModel[]) lTenori.toArray(new TenoreModel[0]);

			// Aggiornamento del codice motivo dell'evento con il cod_oggetto_tenore del 1° record tenore.
			lEveDao = new EventoDAO(lConn);
			lEve.setCodMotivo((lTenoriModel[0]).getCodOggettoTenore());
			// Aggiornamento del codice motivo dell'evento con il cod_oggetto_tenore del 1° record tenore.
			lEve.setDataAggiornamento(DateUtils.getSysDate());
			lEve.setTenIdTenore((lTenoriModel[0]).getIdTenore());
			lEveDao.setDAOFromModelForUpdate(lEve);
			lEveDao.update();
			lEveDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);

			// Controlla l'esistenza di un procedimento
			// per l'udienza richiesta.
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Esiste già un procedimento per l'udienza richiesta!");

			throw new SIUSException("UdienzaController.ExInserisciSentenzaRinvioUdienza : " + daoEx);
		} catch (F3BException f3bex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("F3BException: " + f3bex);
			throw f3bex;
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			throw new SIUSException("UdienzaController.ExInserisciSentenzaRinvioUdienza : " + ex);
		} finally {
			cleanup(lAvvFasSiusDAO);
			cleanup(lEveDao);
			cleanup(lSqlDAO);
			cleanup(lNotDao);
			cleanup(lGenProcDao);
			cleanup(lUdiProcDao);
			cleanup(lUdienzaProcDao);
			cleanup(lTenDao);
			cleanup(lTenSqlDao);
			cleanup(lDepSenDao);
			cleanup(lFasSiusDao);
			cleanup(lConn);
		}
		return lEve;
	}

}