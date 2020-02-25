/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sius.SIUSException;
import siap.sius.avvocatura.dao.AvvisiAvvocatoDAO;
import siap.sius.avvocatura.dao.AvvisiAvvocatoSqlDAO;
import siap.sius.avvocatura.model.AvvisiElencoModel;
import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * @author caporizzo
 */
public class AvvisiSiusController extends GenericController implements IAvvisiSius {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Metodo per l'aggiornamento degli avvisi per avvocato
	 * 
	 * @param idAvviso
	 * @throws F3BException
	 */
	public void aggiornaAvvisiAvvocato(BigDecimal idAvviso) throws F3BException {

		// info per il log
		avvocaturaLogger
				.info("Starting Point della classe: AvvisiSiusController, metodo: aggiornaAvvisiAvvocato");

		// instanzio oggetti di tipo "Connection" e "AvvisiAvvocatoDAO"
		Connection connection = null;
		AvvisiAvvocatoDAO aad = null;

		try {
			// prendo la connessione
			connection = getDBConnection();
			// riempio il model avvisi avvocato
			aad = new AvvisiAvvocatoDAO(connection);
			// aggiorno la tabella "AVVISI_AVVOCATO"
			String query = "UPDATE AVVISI_AVVOCATO SET FLAG_VISUALIZZAZIONE = 'S' " + "WHERE ID_AVVISO = "
					+ idAvviso;
			// info per il log
			avvocaturaLogger.info("query");
			aad.update(query);
			// eseguo commit della transizione
			commit(connection);
		} catch (DAOException ex) {
			// annullamento transizione
			rollback(connection);
			// info per il log
			avvocaturaLogger.error("DAOException: " + ex);
			// lancio nuova eccezione
			throw new SIUSException("AvvocaturaSiusController.aggiornaAvvisiAvvocato: " + ex);
		} catch (Exception ex) {
			// annullamento transizione
			rollback(connection);
			// info per il log
			avvocaturaLogger.error("Exception: " + ex);
			// lancio nuova eccezione
			throw new SIUSException("AvvocaturaSiusController.aggiornaAvvisiAvvocato: " + ex);
		} finally {
			// pulizia delle connessioni
			cleanup(aad);
			cleanup(connection);
		}
	}

	/**
	 * Il metodo effettua una ricerca di avvisi dati i parametri di input
	 * 
	 * @param datiAvviso
	 * @return
	 * @throws F3BException
	 */
	public Vector<AvvisiElencoModel> ricercaAvvisiSius(String codFiscaleAvv, String codTipoUfficio,
			String codDistretto, String flagVisualizzazione, Date dataInizioRicerca, Date dataFineRicerca)
			throws F3BException {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: AvvisiSiusController, metodo: ricercaAvvisiSius");

		Connection connection = null;
		Vector<AvvisiElencoModel> elencoAvvisi = new Vector<AvvisiElencoModel>();
		AvvisiAvvocatoSqlDAO sqldao = null;
		AvvisiElencoModel elencom = null;

		try {
			connection = getDBConnection();
			sqldao = new AvvisiAvvocatoSqlDAO(connection);
			sqldao.ricercaAvvisiSius(codFiscaleAvv, codTipoUfficio, codDistretto, flagVisualizzazione,
					dataInizioRicerca, dataFineRicerca);
			sqldao.start();
			while (sqldao.next()) {
				elencom = (AvvisiElencoModel) sqldao.getElencoAvvisiModel();
				// aggiungo alla lista
				elencoAvvisi.add(elencom);
			}
			sqldao.stop();
		} catch (DAOException daoEx) {
			rollback(connection);
			avvocaturaLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE, "AvvisiSiusController.ricercaAvvisiSius: "
					+ daoEx);
		} catch (Exception e) {
			rollback(connection);
			avvocaturaLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			// stop degli oggetti di tipo "GenericDAO"
			cleanup(sqldao);
			cleanup(connection);
		}
		// valore di ritorno
		return elencoAvvisi;
	}

}