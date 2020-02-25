package siap.siepe.assistentesocialeattivita.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siepe.assistentesocialeattivita.dao.AssistenteSocialeAttivitaDAO;
import siap.siepe.assistentesocialeattivita.dao.AssistenteSocialeAttivitaSqlDAO;
import siap.siepe.assistentesocialeattivita.model.AssistenteSocialeAttivitaModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: AssistenteSocialeAttivitaController
 * </p>
 * <p>
 * Description: Classe Controller per AssistenteSocialeAttivita
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
public class AssistenteSocialeAttivitaController extends SiapController implements IAssistenteSocialeAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public AssistenteSocialeAttivitaModel ExInserisciAssistenteSocialeAttivita(
			AssistenteSocialeAttivitaModel aAssistenteSocialeAttivita, Connection aConn) throws F3BException {
		AssistenteSocialeAttivitaDAO lAssDao = null;
		AssistenteSocialeAttivitaModel lAssMod = null;
		boolean lAssistentePresente = false;

		try {
			lAssMod = new AssistenteSocialeAttivitaModel(aAssistenteSocialeAttivita);
			lAssDao = new AssistenteSocialeAttivitaDAO(aConn);
			// Prima si controlla se l'Asistente Sociale è già presente ed attivo
			lAssDao.setCondizioneAssAttivoXAtt(lAssMod);
			lAssDao.start();
			if (lAssDao.next()) {
				lAssistentePresente = true;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Assistente Sociale già collegato all'Attività");
			}
			lAssDao.stop();
			// La relazione tra Assistente Sociale ed Attività viene
			// inserita solo se non già presente.
			if (!lAssistentePresente) {
				// Chiusura dell'Assistente Sociale attualmente collegato all'attivitù
				lAssDao.setDAOFromModelForchiusura(aAssistenteSocialeAttivita);
				lAssDao.update();
				lAssDao.stop();

				// Inserimento del nuovo Assistente Sociale
				lAssDao.setDAOFromModel(aAssistenteSocialeAttivita);
				lAssDao.insert();
			}

		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException("AssistenteSocialeAttivitaController.ExInserisci: " + ex);
		} finally {
			cleanup(lAssDao);
		}
		return lAssMod;
	}

	/**
	 * Funzione di ricerca. Trova tutti gli Assistenti Sociali associati ad una attività specificata
	 * attraverso la sua chiave.
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAssistentiSocialiXAttivita(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		AssistenteSocialeAttivitaSqlDAO lAssSocDao = null;
		Vector lEspertiAttivita = new Vector();

		try {
			lConn = getDBConnection();
			lAssSocDao = new AssistenteSocialeAttivitaSqlDAO(lConn);
			lAssSocDao.ricercaAssistentiSocXAttivita(aKey);
			lEspertiAttivita = new Vector(lAssSocDao.getModels());
		} catch (Exception e) {
			throw new F3BException(
					"AssistenteSocialeAttivitaController.ExRicercaAssistenteSocialeXAttivita: " + e);
		} finally {
			cleanup(lAssSocDao);
			cleanup(lConn);
		}
		return lEspertiAttivita;
	}

	/**
	 * Funzione di ricerca. Trova tutti gli Assistenti Sociali associati ad una attività specificata che siano
	 * attivi, ovvero che abbiano un periodo di abilitazione che comprenda la data di sistema.
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAssistentiSocialiAttiiviXAttivita(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		AssistenteSocialeAttivitaSqlDAO lAssSocDao = null;
		Vector lEspertiAttivita = new Vector();

		try {
			lConn = getDBConnection();
			lAssSocDao = new AssistenteSocialeAttivitaSqlDAO(lConn);
			lAssSocDao.ricercaAssistentiSocialiAttiviXAttivita(aKey, DateUtils.getSysDate());
			lEspertiAttivita = new Vector(lAssSocDao.getModels());
		} catch (Exception e) {
			throw new F3BException(
					"AssistenteSocialeAttivitaController.ExRicercaAssistentiSocialiAttiviXAttivita: " + e);
		} finally {
			cleanup(lAssSocDao);
			cleanup(lConn);
		}
		return lEspertiAttivita;
	}

	public AssistenteSocialeAttivitaModel ExModificaAssistenteSocialeAttivita(
			AssistenteSocialeAttivitaModel aAssistenteSocialeAttivita) throws F3BException {
		Connection lConn = null;
		AssistenteSocialeAttivitaDAO lAssDao = null;
		AssistenteSocialeAttivitaModel lAssMod = new AssistenteSocialeAttivitaModel(
				aAssistenteSocialeAttivita);

		try {
			lConn = getDBConnection();
			lAssDao = new AssistenteSocialeAttivitaDAO(lConn);
			lAssDao.setDAOFromModelForUpdate(aAssistenteSocialeAttivita);
			lAssDao.update();
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException("AssistenteSocialeAttivitaController.ExModifica: Non posso inserire: "
					+ ex);
		} finally {
			cleanup(lAssDao);
			cleanup(lConn);
		}
		return lAssMod;
	}

	public void ExCancellaAssistenteSocialeAttivita(AssistenteSocialeAttivitaModel aAssistenteSocialeAttivita)
			throws F3BException {
		Connection lConn = null;
		AssistenteSocialeAttivitaDAO lAssDao = null;

		try {
			lConn = getDBConnection();
			lAssDao = new AssistenteSocialeAttivitaDAO(lConn);
			// lAssDao.setCondizioneUpdate(aAssistenteSocialeAttivita.getIdAssistenteSocialeAttivita());
			lAssDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AssistenteSocialeAttivitaController.ExCancellaAssistenteSocialeAttivita: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAssDao);
			cleanup(lConn);
		}
	}

}