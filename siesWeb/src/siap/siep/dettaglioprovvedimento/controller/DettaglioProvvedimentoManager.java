package siap.siep.dettaglioprovvedimento.controller;

import java.sql.Connection;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.evento.model.EventoModel;
import siap.siep.dettaglioprovvedimento.dao.DettaglioProvvedimentoSqlDAO;
import siap.siep.dettaglioprovvedimento.model.DettaglioProvvedimentoModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: DettaglioProvvedimentoController
 * </p>
 * <p>
 * Description: Classe Singleton che gestisce il Dettaglio Provvedimento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DettaglioProvvedimentoManager extends SiapController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// unica istanza del DettaglioProvvedimentoManager
	private static DettaglioProvvedimentoManager mDettaglioProvvedimentoManager = null;

	private Hashtable mDettaglioProvvedimento;

	// [FT] - 03/08/2016 - MAC_LOG - Commento la dichiarazione di mLog in favore della variabile siesLogger
	// static Logger mLog = LogF3B.getLogger();

	protected DettaglioProvvedimentoManager() {
	}

	/**
	 * Metodo di accesso all'istanza del singleton
	 * 
	 * @return
	 */
	public static DettaglioProvvedimentoManager getInstance() {
		if (mDettaglioProvvedimentoManager == null) {
			mDettaglioProvvedimentoManager = new DettaglioProvvedimentoManager();
			mDettaglioProvvedimentoManager.init();
		}

		// mDettaglioProvvedimentoManager.init();
		return mDettaglioProvvedimentoManager;
	}

	/**
	 * Metodo che carica inizializza il singleton e carica l'hashtable con tutti i valori ricavati dalla
	 * tabella Dettaglio provvedimento
	 * 
	 * @throws F3BException
	 */
	private void init() {
		Connection lConn = null;
		// Vector lDettaglioProvvedimenti = new Vector();
		DettaglioProvvedimentoSqlDAO lDetDao = null;

		try {
			/**
			 * Con questo metodo attualizzo l'hash Table con i dati recuoerati dalla tabella
			 * Dettaglio_Provvedimento
			 */

			lConn = getDBConnection();
			mDettaglioProvvedimento = new Hashtable();
			lDetDao = new DettaglioProvvedimentoSqlDAO(lConn);
			lDetDao.ricercaDettaglioProvvedimento();
			lDetDao.start();

			while (lDetDao.next()) {
				DettaglioProvvedimentoModel lModel = (DettaglioProvvedimentoModel) lDetDao.getModel();
				mDettaglioProvvedimento.put(lModel.getChiaveEvento(), lModel);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Carico Hash Table * Chiave = " + lModel.getChiaveEvento() + " * " + lModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
		} catch (F3BException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("F3BException: " + sqe);
		} finally {
			try {
				cleanup(lDetDao);
				cleanup(lConn);
			} catch (F3BException eEx) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("F3BException: Errore durante la cleanup" + eEx);
			}
		}
	}

	/**
	 * Restituisce l'action di dettaglio partendo dal tipo evento
	 * 
	 * @param aTipoEvento
	 * @param aTipoProvvedimento
	 * @param aMotivo
	 * @return
	 */
	public String getActionDettaglio(String aTipoEvento, String aTipoProvvedimento, String aMotivo,
			String aValidato) {
		// Imposto il dettaglio di default da restituire nel caso in cui
		// non venga trovato il dettaglio specifico

		// questa azione non è generico ma viene chiamata sia dalla sorveglianza che da ge nella voce di
		// sottomenù
		// Altre Ordinanze/Decreti
		// String lAction = "siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico";
		String lAction = "siap.sico.evento.action.ActDettaglioDocumento";

		// la vecchia gestione prevedeva tutti i 3 parametri per ricercare
		// il dettaglio corrispondente ma per ora non ha mai rappresentato una discriminante per
		// decidere come caricare un dettaglio
		// mentre lo è il codice motivo
		// String lChiaveEvento = aTipoEvento + aTipoProvvedimento + aMotivo;

		// Compongo la chiave dal tipo evento e codice motivo
		String lChiaveEvento = aTipoEvento + aTipoProvvedimento + aMotivo;

		// SE l'evento è delle richieste al GE devo discriminare se è validato o meno...
		if (aMotivo.equals("0286") && (aValidato == null || aValidato.equals("N"))) {
			// questa azione non è generico ma viene chiamata sia dalla sorveglianza che da ge nella voce di
			// sottomenù
			// Altre Ordinanze/Decreti
			// String lAction =
			// "siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico";
			lAction = "siap.sico.evento.action.ActDettaglioDocumento";
		}

		// Gestione del Caso particolare per l'istanza
		// Tutte le istanze hanno lo stesso dettaglio
		// distinguo però fra nuova istanza e vecchia istanza 0993 = nuova istanza
		// 19/10/2011 inclusi anche i motivi 1001 e 1002
		if (aTipoEvento.equals("03")) {
			// 19/10/2011 if (!aMotivo.equals("0993")) {
			if (!aMotivo.equals("0993") && !aMotivo.equals("1001") && !aMotivo.equals("1002")) {
				lChiaveEvento = aTipoEvento + "--";
			} else {
				lChiaveEvento = aTipoEvento + "-" + aMotivo;
			}
		}

		// Gestione del caso particolare per le Pene Accessorie
		// I Dettagli delle pene accessorie dipendono solo dal Tipo Evento
		if (aTipoEvento.equals("16") || aTipoEvento.equals("17") || aTipoEvento.equals("18"))
			lChiaveEvento = aTipoEvento + "--";

		// Ricerco l'action dei dettaglio dal tipo e cod motivo

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("---> lChiaveEvento " + lChiaveEvento);
		DettaglioProvvedimentoModel lModel = (DettaglioProvvedimentoModel) mDettaglioProvvedimento
				.get(lChiaveEvento);
		if (lModel != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("--->Restituisco per " + lChiaveEvento + "= " + lModel.getActionDettaglio());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("--->Restituisco " + lChiaveEvento + "= NULL");

		if (lModel != null)
			lAction = lModel.getActionDettaglio();

		return lAction;

	}

	/**
	 * Restituisce l'action di Upload partendo da Tipo Evento, Tipo Provvediemnto e Codice Motivo
	 * 
	 * @param EventoModel
	 * @return L'Action di Upload
	 */
	public String getActionUpload(EventoModel aEvento) {
		// Imposto il dettaglio di default da restituire nel caso in cui
		// non venga trovato il dettaglio specifico
		String lAction = "siap.sico.evento.action.ActUploadDocument";

		String lMotivo = aEvento.getCodMotivo();
		String lProvv = aEvento.getCodTipoProvvedimento();
		String lTipoEvento = aEvento.getCodTipoEvento();

		// Compongo la chiave con Tipo Evento, Tipo Provvediemnto e Codice Motivo
		String lChiaveEvento = lTipoEvento + lProvv + lMotivo;

		// SE l'evento è delle richieste al GE devo discriminare se è validato o meno...
		if (lMotivo.equals("0286") && aEvento.getFlagDocumentoRegistrato().equals("N")) {
			lAction = "siap.sico.evento.action.ActUploadDocument";
		}

		// Gestione del Caso particolare per l'istanza
		// Tutte le istanze hanno lo stesso dettaglio
		// distinguo però fra nuova istanza e vecchia istanza 0993 = nuova istanza
		// 19/10/2011 inclusi anche i motivi 1001 e 1002
		if (lTipoEvento.equals("03")) {
			// 19/10/2011 if (!lMotivo.equals("0993")) {
			if (!lMotivo.equals("0993") && !lMotivo.equals("1001") && !lMotivo.equals("1002")) {
				lChiaveEvento = lTipoEvento + "--";
			} else {
				lChiaveEvento = lTipoEvento + "-" + lMotivo;
			}
		}

		// Gestione del caso particolare per le Pene Accessorie
		// I Dettagli delle pene accessorie dipendono solo dal Tipo Evento
		if (lTipoEvento.equals("16") || lTipoEvento.equals("17") || lTipoEvento.equals("18"))
			lChiaveEvento = lTipoEvento + "--";

		// Ricerco l'action di Upload dal Tipo Evento, Tipo Provvediemnto e Codice Motivo
		DettaglioProvvedimentoModel lModel = (DettaglioProvvedimentoModel) mDettaglioProvvedimento
				.get(lChiaveEvento);

		if (lModel != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("--->Restituisco per " + lChiaveEvento + "= " + lModel.getActionUpload());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("--->Restituisco per " + lChiaveEvento + "= NULL");

		if (lModel != null)
			lAction = lModel.getActionUpload();

		return lAction;

	}

}