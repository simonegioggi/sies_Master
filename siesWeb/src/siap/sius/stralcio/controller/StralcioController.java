package siap.sius.stralcio.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

//import f3b.util.report.ReportGenerator;

/**
 * <p>
 * Title: StralcioController
 * </p>
 * <p>
 * Description: Classe Controller per Stralcio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StralcioController extends SiapController implements IStralcio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Verifica del Procedimento destinazione dello Stralcio.
	 *
	 * @param aAnnoProcStralcio
	 * @param aNumeroDestStralcio
	 * @param aUfficioUtenteConnesso
	 * @return aFasDaUnif
	 * @throws F3BException
	 */
	public FascicoloGPModel ExVerificaFascicoloDestStralcio(BigDecimal idSoggetto, String aAnnoDestStralcio,
			String aNumeroDestStralcio, String aUfficioUtenteConnesso) throws F3BException {

		Connection lConn = null;
		FascicoloGPSqlDAO lFasDao = null;

		FascicoloGPModel lFasDestStralcio = new FascicoloGPModel();

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloGPSqlDAO(lConn);

			// Ricerca per Progressivo/Anno/codUfficio del fascicolo destinazione di Stralcio.
			if (!lFasDao.existFasSiusUfficio(new BigDecimal(aAnnoDestStralcio),
					new BigDecimal(aNumeroDestStralcio), aUfficioUtenteConnesso)) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Procedimento destinazione di Stralcio non esistente in archivio");
			}

			// Verifica Fascicolo destinazione di Stralcio.
			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasDestStralcio = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(
					new BigDecimal(aAnnoDestStralcio), new BigDecimal(aNumeroDestStralcio),
					aUfficioUtenteConnesso);

			if (!lFasDestStralcio.getFascicoloSiusModel().getCodStatoFascicolo().equals("02"))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Procedimento destinazione di Stralcio archiviato :  Stralcio Impossibile");

			if (lFasDestStralcio.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.compareTo("U004") == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Il Procedimento destinazione di Stralcio è di Esecuzione Misure Alternative :  Stralcio Impossibile");

			if (lFasDestStralcio.getFascicoloSiusModel().getSogIdSoggetto().compareTo(idSoggetto) != 0)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Il Procedimento destinazione di Stralcio è riferito ad un soggetto diverso :  Stralcio Impossibile");
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("StralcioController.ExVerificaFascicoloDestStralcio: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFasDestStralcio;
	}

	/**
	 * Esecuzione di uno Stralcio : sono state già Verificate le condizioni operative (i due Procedimenti
	 * Individuati devono essere a STATO = 2 Iscritto).; Vengono replicati i Tenori del FasSiusDaStralciare in
	 * SiusDestStralcio; Il FasDaUnif viene aggiornato come Stralciato; Si effettua l'inserimento dell'EVENTO
	 * relativo.
	 *
	 * @param lIdFasSiusDaStralciare
	 * @param lIdFasSiusDestStralcio
	 * @param dataStralcio
	 * @param lArrayCheckBox
	 * @param annoProcedimentoStralcio
	 * @param progrProcedimentoStralcio
	 * @param aUfficioUtenteConnesso
	 * @return EventoModel
	 * @throws F3BException
	 */
	public EventoModel ExInserisciStralcio(BigDecimal lIdFasSiusDaStralciare,
			FascicoloGPModel lFasDestStralcio, Date dataStralcio, String[] lArrayCheckBox,
			Integer numOggettiIniziali, String aUfficioUtenteConnesso, EventoModel aEvento)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;
		TenoreDAO lTenDao = null;
		EventoDAO lEveDao = null;

		// Fascicolo che subisce lo stralcio.
		FascicoloGPModel lFasOrigStralcio = new FascicoloGPModel();

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);
			lTenDao = new TenoreDAO(lConn);
			lEveDao = new EventoDAO(lConn);

			// Lettura del PROCEDIMENTO da stralciare.
			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasOrigStralcio = lCtrl.ExRicercaFascicoloByKey(lIdFasSiusDaStralciare);

			// Controllo congruenza N.ro Oggetti del Procedimento Origine.
			if (lFasOrigStralcio.getTenori().length != lArrayCheckBox.length)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Errore : N.ro Oggetti da stralciare non conforme al numero Oggetti presenti in archivio");

			// ELABORAZIONE CASO FASCICOLO ESISTENTE.
			// Lettura del PROCEDIMENTO di Destinazione Stralcio o Inserimento Nuovo Procedimento x Stralcio.
			if ((lFasDestStralcio.getFascicoloSiusModel() != null)
					&& (lFasDestStralcio.getFascicoloSiusModel().getIdFascicoloSius() != null)) {
				// Accodamento dei TENORE da Stralciare in lFasDestStralcio.
				if (lFasOrigStralcio.getTenori() != null) {
					// Impostazione del ciclo di scrittura dei TENORE in lFasDestStralcio;
					TenoreModel lTenore = new TenoreModel();
					for (int j = numOggettiIniziali.intValue(); j < lFasDestStralcio
							.getTenori().length; j++) {
						lTenore = lFasDestStralcio.getTenori()[j];
						// Setto il DAO dal Model ed inserisco il Tenore
						lTenore.setDataFine(null);
						lTenore.setNote(
								"Stralcio dal " + lFasOrigStralcio.getFascicoloSiusModel().getChiaveAnno()
										+ " / " + lFasOrigStralcio.getFascicoloSiusModel().getChiaveProgr()
										+ " (proc. stralciato)");
						lTenDao.setDAOFromModel(lTenore);
						lTenDao.setGenPridGeneraleProcedimento(
								lFasDestStralcio.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
						lTenDao.setDataFine(null);
						lTenDao.insert();
					}
				}
			} else {
				// ELABORAZIONE CASO FASCICOLO INESISTENTE.
				// Iscrizione di FascicoloSius, GeneraleProcedimento e Tenori per il Nuovo Procedimento.
				lFasDestStralcio = lCtrl.ExInserisciFascicoloSius(lFasDestStralcio);

				// Impostazione del ciclo di riscrittura dei TENORE di lFasDestStralcio;
				TenoreModel lTenore = new TenoreModel();
				for (int j = 0; j < lFasDestStralcio.getTenori().length; j++) {
					lTenore = lFasDestStralcio.getTenori()[j];
					// Setto il DAO dal Model ed inserisco il Tenore
					lTenore.setDataFine(null);
					lTenore.setNote("Stralcio dal " + lFasOrigStralcio.getFascicoloSiusModel().getChiaveAnno()
							+ " / " + lFasOrigStralcio.getFascicoloSiusModel().getChiaveProgr()
							+ " (proc. stralciato)");
					lTenDao.setDAOFromModel(lTenore);
					lTenDao.setDataFine(null);
					lTenDao.setDAOFromModelForUpdateStralcio(lTenore);
					lTenDao.update();
					lTenDao.stop();
				}
				aEvento.setFasSiuIdFascicoloSius(
						lFasOrigStralcio.getFascicoloSiusModel().getIdFascicoloSius());
				aEvento.setFasSiuIdFascicoloSiusDest(
						lFasDestStralcio.getFascicoloSiusModel().getIdFascicoloSius());
			}

			// Stralcio dal Procedimento Origine: Impostazione del ciclo di riscrittura dei TENORE di
			// lFasOrigStralcio;
			for (int j = 0; j < lFasOrigStralcio.getTenori().length; j++) {
				// Si stabilisce se l'oggetto è da stralciare.
				if (lArrayCheckBox[j].compareTo("S") == 0) {
					TenoreModel lTenore = lFasOrigStralcio.getTenori()[j];
					lTenore.setCodEsitoTenore("0604");
					lTenore.setCodMagistrato(
							lFasOrigStralcio.getGeneraleProcedimentoModel().getCodAutoritaDelegata());
					lTenore.setData(dataStralcio);
					lTenore.setCodOperatoreAggiornamento(
							lFasOrigStralcio.getTenori()[lArrayCheckBox.length - 1]
									.getCodOperatoreInserimento());
					lTenore.setDataAggiornamento(
							lFasOrigStralcio.getTenori()[lArrayCheckBox.length - 1].getDataInserimento());
					lTenore.setCodUfficioAggiornamento(lFasOrigStralcio.getTenori()[lArrayCheckBox.length - 1]
							.getCodUfficioInserimento());
					lTenore.setNote(lFasDestStralcio.getFascicoloSiusModel().getChiaveAnno().toString() + "/"
							+ lFasDestStralcio.getFascicoloSiusModel().getChiaveProgr().toString());
					// lTenore.setGenPridGeneraleProcedimento(lFasOrigStralcio.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					lTenore.setDataFine(DateUtils.getSysDate());
					// Setto il DAO dal Model ed aggiorno il Tenore
					lTenDao.setDAOFromModelForUpdateStralcio(lTenore);
					lTenDao.setGenPridGeneraleProcedimento(
							lFasOrigStralcio.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					lTenDao.update();
					lTenDao.stop();
				}
			}

			// Inserimento dell'EVENTO.
			aEvento.setFasSiuIdFascicoloSiusDest(
					lFasDestStralcio.getFascicoloSiusModel().getIdFascicoloSius());

			lEveDao.setDAOFromModel(aEvento);
			aEvento.setIdEvento(lEveDao.insert());

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("StralcioController.exInserisciStralcio: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException("StralcioController.exInserisciStralcio: " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lTenDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return aEvento;
	}

	/**
	 * Ricerca Tenori Stralciati dal Fascicolo di riferimento
	 *
	 * @param aIdFascicoloStralciato
	 * @return Vettore di TenoriStralciati
	 * @throws F3BException
	 */
	public Vector ExRicercaTenoriStralciatiByIdFascicolo(BigDecimal aIdFascicoloStralciato)
			throws F3BException {

		Connection lConn = null;
		Vector lTenori = new Vector();
		TenoreSqlDAO lTenSqlDao = null;

		try {
			lConn = getDBConnection();

			lTenSqlDao = new TenoreSqlDAO(lConn);
			lTenSqlDao.ricercaTenoriStralcio(aIdFascicoloStralciato);
			// MERGE v10 COLLAUDO: lo start & stop avviene nel metodo "getModels()"
			// altrimenti esegue 2 volte la stessa query
			// lTenSqlDao.start();
			lTenori = new Vector(lTenSqlDao.getModels());
			// lTenSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"StralcioController.ExRicercaTenoriStralciatiByIdFascicolo : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
		return lTenori;
	}

	/**
	 * Esegue la cancellazione di un di Stralcio.
	 * <p>
	 *
	 * @param aKeyEvento:
	 *            chiave del record
	 *
	 * @throws F3BException
	 */
	public void ExCancellaStralcio(BigDecimal aKeyEvento, String aUfficioUtenteConnesso,
			String aUtenteConnesso, String aLuogoUfficioUtenteConnesso) throws F3BException {

		Connection lConn = null;
		FascicoloGPSqlDAO lFasGPSqlDao = null;
		FascicoloSiusDAO lFasDao = null;
		TenoreDAO lTenDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		EventoDAO lEveDao = null;

		try {
			// Preleva l'eventoModel da cancellare.
			IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
			EventoModel aEvento = lEveCtrl.ExRicercaEventoByKey(aKeyEvento);
			if (aEvento == null)
				throw new F3BException("Attenzione!  Stralcio non trovato!");

			lConn = getDBConnection();
			lFasDao = new FascicoloSiusDAO(lConn);
			lTenDao = new TenoreDAO(lConn);
			lTenSqlDao = new TenoreSqlDAO(lConn);
			lEveDao = new EventoDAO(lConn);

			// Lettura del Procedimento SIUS Stralciato.

			lFasGPSqlDao = new FascicoloGPSqlDAO(lConn);
			lFasGPSqlDao.ricercaFascicoloByKey(aEvento.getFasSiuIdFascicoloSius());
			FascicoloGPModel lFGPStralciato = (FascicoloGPModel) lFasGPSqlDao.getModelByKey();

			// Caricamento dei Tenore del Procedimento Stralciato.
			lTenSqlDao.ricercaTenoreByStralcio(
					lFGPStralciato.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(),
					DateUtils.getDateToString(aEvento.getDataEmissione(), "yyyyMMdd"));

			Vector lVectTenori = new Vector(lTenSqlDao.getModels());
			if (lVectTenori != null) {
				TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
				lFGPStralciato.setTenori(lTenoriModel);
			}
			// lTenSqlDao.stop();

			// Smarcamento dei TENORE precedentemente stralciati.
			if (lFGPStralciato.getTenori() != null) {
				// Impostazione del ciclo di smarcamento dei TENORE precedentemente stralciati;
				for (int j = 0; j < lFGPStralciato.getTenori().length; j++) {
					TenoreModel lTenoreDiStralcio = lFGPStralciato.getTenori()[j];
					lTenoreDiStralcio.setDataAggiornamento(DateUtils.getSysDate());
					lTenoreDiStralcio.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
					lTenoreDiStralcio.setCodOperatoreAggiornamento(aUtenteConnesso);
					// Si Imposta il DAO dal Model e si aggiorna il Tenore.
					lTenDao.setDAOFromModelForDeleteStralcio(lTenoreDiStralcio);
					lTenDao.update();
				}
				lTenDao.stop();
			}

			// cancellazione Evento collegato.
			lEveDao.selCondizioneUpdate(aKeyEvento);
			lEveDao.delete();

			// COMMIT
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException("StralcioController.ExCancellaStralcio: " + daoEx);
			// } catch (SQLException sqe) {
			// rollback(lConn);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.error("SQLException: " + sqe);
			// throw new SIUSException("StralcioController.ExCancellaStralcio: " + sqe);
		} finally {
			cleanup(lFasGPSqlDao);
			cleanup(lFasDao);
			cleanup(lTenDao);
			cleanup(lEveDao);
			cleanup(lTenSqlDao); // sca

			cleanup(lConn);
		}
	}

}