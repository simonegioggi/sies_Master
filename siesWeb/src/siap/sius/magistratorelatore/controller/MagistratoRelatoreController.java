package siap.sius.magistratorelatore.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException;
import siap.controller.SiapController;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO; // STUB 03/03/2005
import siap.sius.magistratorelatore.dao.MagistratoRelatoreDAO;
import siap.sius.magistratorelatore.dao.MagistratoRelatoreSqlDAO;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: MagistratoRelatoreController
 * </p>
 * <p>
 * Description: Classe Controller per MagistratoRelatore
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
public class MagistratoRelatoreController extends SiapController implements IMagistratoRelatore {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esegue l'inserimento del magistrato relatore, verificando se il magistrato da inserire è lo stesso di
	 * quello corrente se non lo è, chiude il record precedente e inserisce quello nuovo.
	 * <p>
	 *
	 * @param aMagRelMod
	 *            dati del magistrato relatore da inserire.
	 * @return i dati del magistrato relatore appena inseriro.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MagistratoRelatoreModel ExInserisciMagistratoRelatore(MagistratoRelatoreModel aMagRelMod)
			throws F3BException {

		Connection lConn = null;
		MagistratoRelatoreDAO lMagRelDao = null;
		MagistratoRelatoreSqlDAO lMagRelSqlDao = null;
		MagistratoRelatoreModel lMagRelMod = null;
		GeneraleProcedimentoDAO lGPDao = null; // STUB 03/03/2005

		try {
			lConn = getDBTransaction();
			// Interrogazione Magistrato Impostato.
			lMagRelDao = new MagistratoRelatoreDAO(lConn);
			lMagRelSqlDao = new MagistratoRelatoreSqlDAO(lConn);
			lGPDao = new GeneraleProcedimentoDAO(lConn); // STUB 03/03/2005
			lMagRelSqlDao.ricercaMagistratoRelatoreCorrenteByFascicolo(aMagRelMod.getFasSiuIdFascicoloSius());
			lMagRelMod = (MagistratoRelatoreModel) lMagRelSqlDao.getModelByKey();

			if (lMagRelMod != null) {
				lMagRelDao.setCodOperatoreAggiornamento(aMagRelMod.getCodOperatoreInserimento());
				lMagRelDao.setCodUfficioAggiornamento(aMagRelMod.getCodUfficioInserimento());
				lMagRelDao.setDataAggiornamento(DateUtils.getSysDate());
				lMagRelDao.setDataFine(DateUtils.getSysDate());
				lMagRelDao.setCondizioneUpdate(aMagRelMod.getFasSiuIdFascicoloSius(),
						lMagRelMod.getMagCodMagistrato(), lMagRelMod.getEspIdEsperto());

				lMagRelDao.update();
				lMagRelDao.stop();
			}

			// Si Esegue l'inserimento del Nuovo Magistrato.
			lMagRelDao.setDAOFromModel(aMagRelMod);
			lMagRelDao.insert();

			// STUB 03/03/2005 Aggiornamento di CodAutoritaDelegata (Cod. Magistrato) in
			// GENERALE_PROCEDIMENTO.
			lGPDao = new GeneraleProcedimentoDAO(lConn);
			lGPDao.setCodAutoritaDelegata(aMagRelMod.getMagCodMagistrato());
			lGPDao.setCodUfficioAggiornamento(aMagRelMod.getCodUfficioInserimento());
			lGPDao.setDataAggiornamento(aMagRelMod.getDataInserimento());
			lGPDao.setCodOperatoreAggiornamento(aMagRelMod.getCodOperatoreInserimento());
			lGPDao.setCondizioneUpdateByIdFasSius(aMagRelMod.getFasSiuIdFascicoloSius());
			lGPDao.update();
			lGPDao.stop();

			commit(lConn);

		} catch (SIUSException siusEx) {
			rollback(lConn);
			throw siusEx;
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoex);

			if (daoex.getMessage().indexOf("MAG_REL_PK") != -1)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Impossibile inserire il Magistrato Relatore! Esiste un Magistrato per lo stesso numero SIUS");

			throw new SIUSException(
					"MagistratoRelatoreController.ExInserisciMagistratoRelatore : Non posso inserire: "
							+ daoex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lMagRelDao);
			cleanup(lMagRelSqlDao);
			cleanup(lGPDao); // 03/03/2005
			cleanup(lConn);
		}

		return aMagRelMod;
	}

	/**
	 * Esegue la ricerca del magistrato relatore corrente per id fascicolo.
	 * <p>
	 *
	 * @param aKey
	 *            id del fascicolo.
	 * @return model aggregato con i dati del magistrato relatore e quelli del magistrato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MagistratoRelatoreModel ExRicercaMagRelByFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MagistratoRelatoreSqlDAO lMagRelSqlDao = null;
		MagistratoRelatoreModel lMagRelMod;

		try {
			lConn = getDBConnection();
			lMagRelSqlDao = new MagistratoRelatoreSqlDAO(lConn);
			lMagRelSqlDao.ricercaMagistratoRelatoreCorrenteByFascicolo(aKey);
			lMagRelMod = (MagistratoRelatoreModel) lMagRelSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new SIUSException(
					"MagistratoRelatoreController.ExRicercaMagistratoRelatoreMagistratoCorrenteByFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMagRelSqlDao);
			cleanup(lConn);
		}
		return lMagRelMod;
	}

	/**
	 * Esegue la ricerca del magistrato precedente e corrente, per l'id del fascicolo sius.
	 *
	 * @param aKey
	 *            id del fasciclo sius.
	 * @return l'elenco dei magistrati Corrente e il precedente.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector ExRicercaMagRelCorrentePrecedenteByFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MagistratoRelatoreSqlDAO lMagRelSqlDao = null;
		Vector lMagistrati = null;

		try {
			lConn = getDBConnection();
			lMagRelSqlDao = new MagistratoRelatoreSqlDAO(lConn);
			lMagRelSqlDao.ricercaMagistratoRelatoreByDataFine(aKey);
			lMagistrati = new Vector(lMagRelSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new SIUSException(
					"MagistratoRelatoreController.ExRicercaMagistratoRelatoreCorrenteByFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMagRelSqlDao);
			cleanup(lConn);
		}
		return lMagistrati;
	}

	/**
	 * Esegue la ricerca del magistrato relatore corrente per id fascicolo.
	 * <p>
	 * Se il Magistrato relatore è un Magistrato effettua anche la ricerca del Magistrato.
	 * <p>
	 * Se il Magistrato Relatore è un Esperto effettua la ricerca dell'Esperto.
	 * <p>
	 *
	 * @param aKey
	 *            id del fascicolo.
	 * @return model aggregato con i dati del magistrato relatore e quelli del magistrato o dell'esperto.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MagistratoRelatoreModel ExRicercaEstesaMagRelByFascicolo(BigDecimal aKey) throws F3BException {

		MagistratoRelatoreModel lMagRel = null;

		// Magistrato relatore
		lMagRel = ExRicercaMagRelByFascicolo(aKey);

		if (lMagRel != null) {
			// Magistrato
			if (lMagRel.getMagCodMagistrato() != null && !lMagRel.getMagCodMagistrato().equals("-")) {
				IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
				lMagRel.setMagistrato(lMagCtrl.ExRicercaMagistratoByCod(lMagRel.getMagCodMagistrato()));
			}

			// Esperto
			if (lMagRel.getEspIdEsperto() != null && !lMagRel.getEspIdEsperto().toString().equals("-")) {
				IEsperto lEspCtrl = SIUSLookupRemote.getEspertoRemote();
				lMagRel.setEsperto(lEspCtrl.ExRicercaEspertoByKey(lMagRel.getEspIdEsperto()));
			}
		}
		return lMagRel;
	}

	public void ExModificaMultiplaMagistratoRelatore(MagistratoRelatoreModel aMagRelModel,
			String[] aListaFascicoli) throws F3BException {

		Connection lConn = null;

		MagistratoRelatoreDAO lMagRelDao = null;
		GeneraleProcedimentoDAO lGPDao = null;

		try {
			lConn = getDBConnection();

			// ========================================================================
			//
			// ========================================================================
			lMagRelDao = new MagistratoRelatoreDAO(lConn);
			for (int i = 0; i < aListaFascicoli.length; i++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("id_fascicolo = " + aListaFascicoli[i]);

				BigDecimal lIdFascicoloSius = new BigDecimal(aListaFascicoli[i]);

				// ======================================================================
				// Aggiorno la data fine validità del vecchio magistrato
				// n.b. Il magistrato relatore attuale è quello con DATA_FINE = null
				// ======================================================================

				lMagRelDao.setDataFine(DateUtils.getSysDate()); // '?????????????????????????????

				lMagRelDao.setCodOperatoreAggiornamento(aMagRelModel.getCodOperatoreInserimento());
				lMagRelDao.setCodUfficioAggiornamento(aMagRelModel.getCodUfficioInserimento());
				lMagRelDao.setDataAggiornamento(DateUtils.getSysDate());

				lMagRelDao.setCondizioneUpdateMagistratoSorveglianzaCorrente(lIdFascicoloSius);
				lMagRelDao.update();
				lMagRelDao.stop();

				// ======================================================================
				// Inserisco il nuovo magistrato
				// ======================================================================
				aMagRelModel.setFasSiuIdFascicoloSius(lIdFascicoloSius);
				lMagRelDao.setDAOFromModel(aMagRelModel);
				lMagRelDao.insert();
				lMagRelDao.stop();

				// Aggiornamento di CodAutoritaDelegata (Cod. Magistrato) in GENERALE_PROCEDIMENTO.
				lGPDao = new GeneraleProcedimentoDAO(lConn);
				lGPDao.setCodAutoritaDelegata(aMagRelModel.getMagCodMagistrato());
				lGPDao.setCodUfficioAggiornamento(aMagRelModel.getCodUfficioInserimento());
				lGPDao.setDataAggiornamento(aMagRelModel.getDataInserimento());
				lGPDao.setCodOperatoreAggiornamento(aMagRelModel.getCodOperatoreInserimento());
				lGPDao.setCondizioneUpdateByIdFasSius(aMagRelModel.getFasSiuIdFascicoloSius());
				lGPDao.update();
				lGPDao.stop();
			}

			commit(lConn);
			// rollback(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"MagistratoRelatoreController.ExModificaMultiplaMagistratoRelatore: " + ex);
		} catch (Exception e) { // MEV_2025-48: aggiunta nuova funzionalita': paginata la ricerca
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			if (e instanceof SearchLimitException || e instanceof IllegalStateException)
				throw new SIUSException(
						"Attenzione: con la selezione 'Tutti' il sistema non riesce a completare "
								+ "l'aggiornamento, procedere con la selezione per pagina!");
			else
				throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lMagRelDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lGPDao);

			cleanup(lConn);
		}
	}

}