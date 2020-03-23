package siap.sige.statistiche.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sige.statistiche.dao.EveFasGepSogFogSqlDAO;
import siap.sige.statistiche.dao.EveFasGepSogOrdSqlDAO;
import siap.sige.statistiche.dao.EveFasGepSogSqlDAO;
import siap.sige.statistiche.dao.StatisticheFogliComplementariSqlDAO;
import siap.sige.statistiche.model.EveFasGepSogProvModel;
import siap.sige.statistiche.model.RicercaFogliCompModel;
import siap.sige.statistiche.model.StatisticheFogliComplementariContainerModel;
import siap.sige.statistiche.model.StatisticheFogliComplementariModel;

/**
 * <p>
 * Title: StatisticheSigeController
 * </p>
 * <p>
 * Description: Classe Controller per le Statistiche
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
public class StatisticheSigeController extends SiapController implements IStatisticheSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Ritorna n.ro di record risultato della ExRicercaFogliComplementariPaginata
	 *
	 * @param RicercaFogliCompModel
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaFogliComplementari(RicercaFogliCompModel aModel) throws F3BException {

		Connection lConn = null; // connessione

		// SqlDAO utilizzato viene definito come generico
		EveFasGepSogSqlDAO lDao = null;
		// Risultato
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();

			// Scelta del DAO specializzato al tipo di ricerca
			if (aModel.isRicercaXOrdinanza())
				lDao = new EveFasGepSogOrdSqlDAO(lConn);
			else if (aModel.isRicercaXFoglioComplementare())
				lDao = new EveFasGepSogFogSqlDAO(lConn);

			else
				throw new F3BException(F3BException.USER_MESSAGE, " Modalità di ricerca non definita !");

			lDao.ricercaProcedimentiSige(aModel);
			lCont = lDao.getNumRowsSelected();
			aModel.setNumTotali(lCont);

			// Calcolo parziale effettuato solo se la ricerca non filtra lo stato
			if (aModel.isRicercaStatoTutti()) {
				aModel.setNumAnnullati(ExGetNumAnnullati(aModel, lDao));
				if (aModel.isRicercaXOrdinanza())
					aModel.setNumNonValidati(ExGetNumNonValidati(aModel, lDao));
				aModel.setCalcolatiTotali(true);
			}
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(e.getMessage());
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDao);
			cleanup(lConn);
		}
		return lCont;
	}

	private BigDecimal ExGetNumAnnullati(RicercaFogliCompModel aModel, EveFasGepSogSqlDAO aDao)
			throws Exception {

		BigDecimal lCont = new BigDecimal(0);

		// Si copia RicercaModel per effettuare la stessa ricerca già definita
		// ma aggiungendo solo lo stato Annullato.
		RicercaFogliCompModel lRicercaModel = new RicercaFogliCompModel(aModel);
		lRicercaModel.setStatoAnnullati();

		aDao.ricercaProcedimentiSige(lRicercaModel);
		lCont = aDao.getNumRowsSelected();

		return lCont;
	}

	private BigDecimal ExGetNumNonValidati(RicercaFogliCompModel aModel, EveFasGepSogSqlDAO aDao)
			throws Exception {

		BigDecimal lCont = new BigDecimal(0);

		// Si copia RicercaModel per effettuare la stessa ricerca già definita
		// ma aggiungendo solo lo stato Non Validato.
		RicercaFogliCompModel lRicercaModel = new RicercaFogliCompModel(aModel);
		lRicercaModel.setStatoNonValidati();

		aDao.ricercaProcedimentiSige(lRicercaModel);
		lCont = aDao.getNumRowsSelected();

		return lCont;
	}

	/**
	 * La funzione realizza una ricerca dei Fogli Complementari emessi per un Provvedimento Sige. I
	 * Provvedimenti possono essere Ordinanze oppure Decreti in base alla modalità di ricerca attivata. Il
	 * filtro di ricerca da realizzare è codificato nel RicercaFogliCompModel passato come argomento. Il
	 * risultato è una lista (Vector) di model EveFasGepSogProvModel; in questi sono presenti oltre ai dati
	 * dell'Ordinanza anche dati relativi a: Fascicolo Sige, ProvvedimentoSige, Evento, Soggetto.
	 *
	 * @param aModel
	 *            RicercaFogliCompModel.
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaFogliComplementariPaginata(RicercaFogliCompModel aModel, int aPageNum)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExRicercaFogliComplementariPaginata(): inizio ");
		// connessione
		Connection lConn = null; // connessione

		// SqlDAO utilizzato viene definito come generico
		EveFasGepSogSqlDAO lDao = null;

		// Aggregato di dati risultato della ricerca
		EveFasGepSogProvModel lProvvedimento = null;

		// Elenco risultati
		Vector lElencoProc = new Vector();

		try {
			lConn = getDBConnection();
			// Scelta del DAO specializzato al tipo di ricerca
			if (aModel.isRicercaXOrdinanza())
				lDao = new EveFasGepSogOrdSqlDAO(lConn);
			else if (aModel.isRicercaXFoglioComplementare())
				lDao = new EveFasGepSogFogSqlDAO(lConn);
			else
				throw new F3BException(F3BException.USER_MESSAGE, " Modalità di ricerca non definita !");

			lDao.ricercaProcedimentiSige(aModel);

			// Ricerca paginata
			// Viene fatta la ricerca non paginata se aPageNum ha un valore <= 0
			if (aPageNum > 0)
				lDao.startPage(aPageNum);
			else
				lDao.start();

			while (lDao.next()) {
				lProvvedimento = (EveFasGepSogProvModel) lDao.getModel();
				lElencoProc.add(lProvvedimento);
			}
			lDao.stop();

			if (lElencoProc.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("ExRicercaFogliComplementariPaginata: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExRicercaFogliComplementariPaginata(): fine ");

		return lElencoProc;
	}

	@Override
	public Vector<StatisticheFogliComplementariModel> ExEstraiStatisticheFogliComplementari(
			RicercaFogliCompModel aModel, int aPageNum) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExEstraiStatisticheFogliComplementari(): inizio ");
		// connessione
		Connection lConn = getDBConnection(); // connessione
		// Elenco risultati
		Vector<StatisticheFogliComplementariModel> lElencoFC = new Vector<>();
		StatisticheFogliComplementariSqlDAO lDao = new StatisticheFogliComplementariSqlDAO(lConn);

		try {
			// Scelta del DAO specializzato al tipo di ricerca
			lDao.ricercaStatisticheFogliComplementari(aModel);

			if (aPageNum > 0)
				lDao.startPage(aPageNum);
			else
				lDao.start();

			while (lDao.next()) {
				lElencoFC.add(lDao.getModel());
			}
			lDao.stop();
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("ExEstraiStatisticheFogliComplementari: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExRicercaFogliComplementariPaginata(): fine ");

		return lElencoFC;
	}

	@Override
	public BigDecimal ExCountEstraiStatisticheFogliComplementari(RicercaFogliCompModel aModel)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug("" + getClass().getName() + " .ExCountEstraiStatisticheFogliComplementari(): inizio ");
		// connessione
		Connection lConn = null; // connessione

		StatisticheFogliComplementariSqlDAO lDao = null;
		BigDecimal numRecord = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lDao = new StatisticheFogliComplementariSqlDAO(lConn);

			// Scelta del DAO specializzato al tipo di ricerca

			lDao.ricercaStatisticheFogliComplementari(aModel);
			numRecord = lDao.getNumRowsSelected();
			lDao.stop();
		} catch (F3BException fe) {
			fe.printStackTrace();
			throw fe;
		} catch (Exception e) {
			e.printStackTrace();
			throw new F3BException("ExCountEstraiStatisticheFogliComplementari: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExCountEstraiStatisticheFogliComplementari(): fine ");
		return numRecord;
	}

	@Override
	public StatisticheFogliComplementariContainerModel ExEstraiStatisticheFogliComplementariExportExcel(
			RicercaFogliCompModel aModel) throws F3BException {

		StatisticheFogliComplementariContainerModel container = new StatisticheFogliComplementariContainerModel();

		Connection lConn = null;
		StatisticheFogliComplementariSqlDAO lDao = null;

		try {
			lConn = getDBConnection();
			lDao = new StatisticheFogliComplementariSqlDAO(lConn);
			if (aModel.isFcAnnullati()) {
				lDao.ricercaFcAnnullati(aModel);
				container.setFcAnnullati(lDao.getLista());
				lDao.ricercaConteggioFcAnnullati(aModel);
				container.setRiepilogoAnnullati(lDao.getListaRiepilogo());
			}

			if (aModel.isFcIscrittiManualmente()) {
				lDao.ricercaIscrittiManualmente(aModel);
				container.setFcIscrittiManualmente(lDao.getLista());
				lDao.ricercaConteggioFcIscrittiManualmente(aModel);
				container.setRiepilogFCIscrittiManualmente(lDao.getListaRiepilogo());
			}

			if (aModel.isProvvedimentiPriviFC()) {
				lDao.ricercaProvvedimentiPriviFC(aModel);
				container.setProvvedimentiPriviFc(lDao.getLista());
				lDao.ricercaConteggioProvvedimentiPriviFC(aModel);
				container.setRiepilogoProvvedimentiPriviFC(lDao.getListaRiepilogo());
			}

			if (aModel.isFcTrasmessi()) {
				lDao.ricercaProvvedimentiConFC(aModel);
				container.setProvvedimentiConFc(lDao.getLista());
				lDao.ricercaConteggioProvvedimentiConFC(aModel);
				container.setRiepilogoProvvedimentiConFC(lDao.getListaRiepilogo());
			}
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("ExEstraiStatisticheFogliComplementariExportExcel: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDao);
			cleanup(lConn);
		}

		return container;
	}

}