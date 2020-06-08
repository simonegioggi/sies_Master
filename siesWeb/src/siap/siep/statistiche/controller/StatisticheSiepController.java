package siap.siep.statistiche.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.statistiche.dao.StatisticheFogliComplementariSqlDAO;
import siap.siep.statistiche.model.RicercaFogliCompModel;
import siap.siep.statistiche.model.StatisticheFogliComplementariContainerModel;
import siap.siep.statistiche.model.StatisticheFogliComplementariModel;

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
public class StatisticheSiepController extends SiapController implements IStatisticheSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@Override
	public Vector<StatisticheFogliComplementariModel> ExEstraiStatisticheFogliComplementari(
			RicercaFogliCompModel aModel, int aPageNum) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ExEstraiStatisticheFogliComplementari: inizio");
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
		siesLogger.debug(getClass().getName() + ".ExEstraiStatisticheFogliComplementari: fine");

		return lElencoFC;
	}

	@Override
	public BigDecimal ExCountEstraiStatisticheFogliComplementari(RicercaFogliCompModel aModel)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ExCountEstraiStatisticheFogliComplementari(): inizio");
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
		siesLogger.debug(getClass().getName() + ".ExCountEstraiStatisticheFogliComplementari: fine");
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

			/*
			 * if (aModel.isProvvedimentiPriviFC()) { lDao.ricercaProvvedimentiPriviFC(aModel);
			 * container.setProvvedimentiPriviFc(lDao.getLista());
			 * lDao.ricercaConteggioProvvedimentiPriviFC(aModel);
			 * container.setRiepilogoProvvedimentiPriviFC(lDao.getListaRiepilogo()); }
			 */

			if (aModel.isFcTrasmessi()) {
				lDao.ricercaProvvedimentiConFC(aModel);
				container.setProvvedimentiConFc(lDao.getLista());
				lDao.ricercaConteggioProvvedimentiConFC(aModel);
				container.setRiepilogoProvvedimentiConFC(lDao.getListaRiepilogo());
			}

			if (aModel.isFcNonTrasmessi()) {
				lDao.ricercaFCNonTrasmessi(aModel);
				container.setFcNonTrasmessi(lDao.getLista());
				lDao.ricercaConteggioFCNonTrasmessi(aModel);
				container.setRiepilogoFCNonTrasmessi(lDao.getListaRiepilogo());
			}

			if (aModel.isFcTrasmessiErrore()) {
				lDao.ricercaFCTrasmessiConErrore(aModel);
				container.setFcTrasmessiConErrore(lDao.getLista());
				lDao.ricercaConteggioFCTrasmessiConErrore(aModel);
				container.setRiepilogoFCTrasmessiConErrore(lDao.getListaRiepilogo());
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